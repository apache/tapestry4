dojo.provide("dojo.io.ScriptSrcIO");
dojo.require("dojo.io.BrowserIO");
dojo.require("dojo.undo.browser");
dojo.io.ScriptSrcTransport=new function(){
this.preventCache=false;
this.maxUrlLength=1000;
this.inFlightTimer=null;
this.DsrStatusCodes={Continue:100,Ok:200,Error:500};
this.startWatchingInFlight=function(){
if(!this.inFlightTimer){
this.inFlightTimer=setInterval("dojo.io.ScriptSrcTransport.watchInFlight();",100);
}
};
this.watchInFlight=function(){
var _1=0;
var _2=0;
for(var _3 in this._state){
_1++;
var _4=this._state[_3];
if(_4.isDone){
_2++;
delete this._state[_3];
}else{
if(!_4.isFinishing){
var _5=_4.kwArgs;
try{
if(_4.checkString&&eval("typeof("+_4.checkString+") != 'undefined'")){
_4.isFinishing=true;
this._finish(_4,"load");
_2++;
delete this._state[_3];
}else{
if(_5.timeoutSeconds&&_5.timeout){
if(_4.startTime+(_5.timeoutSeconds*1000)<(new Date()).getTime()){
_4.isFinishing=true;
this._finish(_4,"timeout");
_2++;
delete this._state[_3];
}
}else{
if(!_5.timeoutSeconds){
_2++;
}
}
}
}
catch(e){
_4.isFinishing=true;
this._finish(_4,"error",{status:this.DsrStatusCodes.Error,response:e});
}
}
}
}
if(_2>=_1){
clearInterval(this.inFlightTimer);
this.inFlightTimer=null;
}
};
this.canHandle=function(_6){
return dojo.lang.inArray(["text/javascript","text/json","application/json"],(_6["mimetype"].toLowerCase()))&&(_6["method"].toLowerCase()=="get")&&!(_6["formNode"]&&dojo.io.formHasFile(_6["formNode"]))&&(!_6["sync"]||_6["sync"]==false)&&!_6["file"]&&!_6["multipart"];
};
this.removeScripts=function(){
var _7=document.getElementsByTagName("script");
for(var i=0;_7&&i<_7.length;i++){
var _9=_7[i];
if(_9.className=="ScriptSrcTransport"){
var _a=_9.parentNode;
_a.removeChild(_9);
i--;
}
}
};
this.bind=function(_b){
var _c=_b.url;
var _d="";
if(_b["formNode"]){
var ta=_b.formNode.getAttribute("action");
if((ta)&&(!_b["url"])){
_c=ta;
}
var tp=_b.formNode.getAttribute("method");
if((tp)&&(!_b["method"])){
_b.method=tp;
}
_d+=dojo.io.encodeForm(_b.formNode,_b.encoding,_b["formFilter"]);
}
if(_c.indexOf("#")>-1){
dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",_c);
_c=_c.split("#")[0];
}
var _10=_c.split("?");
if(_10&&_10.length==2){
_c=_10[0];
_d+=(_d?"&":"")+_10[1];
}
if(_b["backButton"]||_b["back"]||_b["changeUrl"]){
dojo.undo.browser.addToHistory(_b);
}
var id=_b["apiId"]?_b["apiId"]:"id"+this._counter++;
var _12=_b["content"];
var _13=_b.jsonParamName;
if(_b.sendTransport||_13){
if(!_12){
_12={};
}
if(_b.sendTransport){
_12["dojo.transport"]="scriptsrc";
}
if(_13){
_12[_13]="dojo.io.ScriptSrcTransport._state."+id+".jsonpCall";
}
}
if(_b.postContent){
_d=_b.postContent;
}else{
if(_12){
_d+=((_d)?"&":"")+dojo.io.argsFromMap(_12,_b.encoding,_13);
}
}
if(_b["apiId"]){
_b["useRequestId"]=true;
}
var _14={"id":id,"idParam":"_dsrid="+id,"url":_c,"query":_d,"kwArgs":_b,"startTime":(new Date()).getTime(),"isFinishing":false};
if(!_c){
this._finish(_14,"error",{status:this.DsrStatusCodes.Error,statusText:"url.none"});
return;
}
if(_12&&_12[_13]){
_14.jsonp=_12[_13];
_14.jsonpCall=function(_15){
if(_15["Error"]||_15["error"]){
if(dojo["json"]&&dojo["json"]["serialize"]){
dojo.debug(dojo.json.serialize(_15));
}
dojo.io.ScriptSrcTransport._finish(this,"error",_15);
}else{
dojo.io.ScriptSrcTransport._finish(this,"load",_15);
}
};
}
if(_b["useRequestId"]||_b["checkString"]||_14["jsonp"]){
this._state[id]=_14;
}
if(_b["checkString"]){
_14.checkString=_b["checkString"];
}
_14.constantParams=(_b["constantParams"]==null?"":_b["constantParams"]);
if(_b["preventCache"]||(this.preventCache==true&&_b["preventCache"]!=false)){
_14.nocacheParam="dojo.preventCache="+new Date().valueOf();
}else{
_14.nocacheParam="";
}
var _16=_14.url.length+_14.query.length+_14.constantParams.length+_14.nocacheParam.length+this._extraPaddingLength;
if(_b["useRequestId"]){
_16+=_14.idParam.length;
}
if(!_b["checkString"]&&_b["useRequestId"]&&!_14["jsonp"]&&!_b["forceSingleRequest"]&&_16>this.maxUrlLength){
if(_c>this.maxUrlLength){
this._finish(_14,"error",{status:this.DsrStatusCodes.Error,statusText:"url.tooBig"});
return;
}else{
this._multiAttach(_14,1);
}
}else{
var _17=[_14.constantParams,_14.nocacheParam,_14.query];
if(_b["useRequestId"]&&!_14["jsonp"]){
_17.unshift(_14.idParam);
}
var _18=this._buildUrl(_14.url,_17);
_14.finalUrl=_18;
this._attach(_14.id,_18);
}
this.startWatchingInFlight();
};
this._counter=1;
this._state={};
this._extraPaddingLength=16;
this._buildUrl=function(url,_1a){
var _1b=url;
var _1c="?";
for(var i=0;i<_1a.length;i++){
if(_1a[i]){
_1b+=_1c+_1a[i];
_1c="&";
}
}
return _1b;
};
this._attach=function(id,url){
var _20=document.createElement("script");
_20.type="text/javascript";
_20.src=url;
_20.id=id;
_20.className="ScriptSrcTransport";
document.getElementsByTagName("head")[0].appendChild(_20);
};
this._multiAttach=function(_21,_22){
if(_21.query==null){
this._finish(_21,"error",{status:this.DsrStatusCodes.Error,statusText:"query.null"});
return;
}
if(!_21.constantParams){
_21.constantParams="";
}
var _23=this.maxUrlLength-_21.idParam.length-_21.constantParams.length-_21.url.length-_21.nocacheParam.length-this._extraPaddingLength;
var _24=_21.query.length<_23;
var _25;
if(_24){
_25=_21.query;
_21.query=null;
}else{
var _26=_21.query.lastIndexOf("&",_23-1);
var _27=_21.query.lastIndexOf("=",_23-1);
if(_26>_27||_27==_23-1){
_25=_21.query.substring(0,_26);
_21.query=_21.query.substring(_26+1,_21.query.length);
}else{
_25=_21.query.substring(0,_23);
var _28=_25.substring((_26==-1?0:_26+1),_27);
_21.query=_28+"="+_21.query.substring(_23,_21.query.length);
}
}
var _29=[_25,_21.idParam,_21.constantParams,_21.nocacheParam];
if(!_24){
_29.push("_part="+_22);
}
var url=this._buildUrl(_21.url,_29);
this._attach(_21.id+"_"+_22,url);
};
this._finish=function(_2b,_2c,_2d){
if(_2c!="partOk"&&!_2b.kwArgs[_2c]&&!_2b.kwArgs["handle"]){
if(_2c=="error"){
_2b.isDone=true;
throw _2d;
}
}else{
switch(_2c){
case "load":
var _2e=_2d?_2d.response:null;
if(!_2e){
_2e=_2d;
}
_2b.kwArgs[(typeof _2b.kwArgs.load=="function")?"load":"handle"]("load",_2e,_2d,_2b.kwArgs);
_2b.isDone=true;
break;
case "partOk":
var _2f=parseInt(_2d.response.part,10)+1;
if(_2d.response.constantParams){
_2b.constantParams=_2d.response.constantParams;
}
this._multiAttach(_2b,_2f);
_2b.isDone=false;
break;
case "error":
_2b.kwArgs[(typeof _2b.kwArgs.error=="function")?"error":"handle"]("error",_2d.response,_2d,_2b.kwArgs);
_2b.isDone=true;
break;
default:
_2b.kwArgs[(typeof _2b.kwArgs[_2c]=="function")?_2c:"handle"](_2c,_2d,_2d,_2b.kwArgs);
_2b.isDone=true;
}
}
};
dojo.io.transports.addTransport("ScriptSrcTransport");
};
window.onscriptload=function(_30){
var _31=null;
var _32=dojo.io.ScriptSrcTransport;
if(_32._state[_30.id]){
_31=_32._state[_30.id];
}else{
var _33;
for(var _34 in _32._state){
_33=_32._state[_34];
if(_33.finalUrl&&_33.finalUrl==_30.id){
_31=_33;
break;
}
}
if(_31==null){
var _35=document.getElementsByTagName("script");
for(var i=0;_35&&i<_35.length;i++){
var _37=_35[i];
if(_37.getAttribute("class")=="ScriptSrcTransport"&&_37.src==_30.id){
_31=_32._state[_37.id];
break;
}
}
}
if(_31==null){
throw "No matching state for onscriptload event.id: "+_30.id;
}
}
var _38="error";
switch(_30.status){
case dojo.io.ScriptSrcTransport.DsrStatusCodes.Continue:
_38="partOk";
break;
case dojo.io.ScriptSrcTransport.DsrStatusCodes.Ok:
_38="load";
break;
}
_32._finish(_31,_38,_30);
};
