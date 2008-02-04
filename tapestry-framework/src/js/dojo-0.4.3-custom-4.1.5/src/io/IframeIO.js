dojo.provide("dojo.io.IframeIO");
dojo.require("dojo.io.BrowserIO");
dojo.require("dojo.uri.*");
dojo.io.createIFrame=function(_1,_2,_3){
if(window[_1]){
return window[_1];
}
if(window.frames[_1]){
return window.frames[_1];
}
var r=dojo.render.html;
var _5=null;
var _6=_3;
if(!_6){
if(djConfig["useXDomain"]&&!djConfig["dojoIframeHistoryUrl"]){
dojo.debug("dojo.io.createIFrame: When using cross-domain Dojo builds,"+" please save iframe_history.html to your domain and set djConfig.dojoIframeHistoryUrl"+" to the path on your domain to iframe_history.html");
}
_6=(djConfig["dojoIframeHistoryUrl"]||dojo.uri.moduleUri("dojo","../iframe_history.html"))+"#noInit=true";
}
var _7=((r.ie)&&(dojo.render.os.win))?"<iframe name=\""+_1+"\" src=\""+_6+"\" onload=\""+_2+"\">":"iframe";
_5=document.createElement(_7);
with(_5){
name=_1;
setAttribute("name",_1);
id=_1;
}
dojo.body().appendChild(_5);
window[_1]=_5;
with(_5.style){
if(!r.safari){
position="absolute";
}
left=top="0px";
height=width="1px";
visibility="hidden";
}
if(!r.ie){
dojo.io.setIFrameSrc(_5,_6,true);
_5.onload=new Function(_2);
}
return _5;
};
dojo.io.IframeTransport=new function(){
var _8=this;
this.currentRequest=null;
this.requestQueue=[];
this.iframeName="dojoIoIframe";
this.fireNextRequest=function(){
try{
if((this.currentRequest)||(this.requestQueue.length==0)){
return;
}
var cr=this.currentRequest=this.requestQueue.shift();
cr._contentToClean=[];
var fn=cr["formNode"];
var _b=cr["content"]||{};
if(cr.sendTransport){
_b["dojo.transport"]="iframe";
}
if(fn){
if(_b){
for(var x in _b){
if(!fn[x]){
var tn;
if(dojo.render.html.ie){
tn=document.createElement("<input type='hidden' name='"+x+"' value='"+_b[x]+"'>");
fn.appendChild(tn);
}else{
tn=document.createElement("input");
fn.appendChild(tn);
tn.type="hidden";
tn.name=x;
tn.value=_b[x];
}
cr._contentToClean.push(x);
}else{
fn[x].value=_b[x];
}
}
}
if(cr["url"]){
cr._originalAction=fn.getAttribute("action");
fn.setAttribute("action",cr.url);
}
if(!fn.getAttribute("method")){
fn.setAttribute("method",(cr["method"])?cr["method"]:"post");
}
cr._originalTarget=fn.getAttribute("target");
fn.setAttribute("target",this.iframeName);
fn.target=this.iframeName;
fn.submit();
}else{
var _e=dojo.io.argsFromMap(this.currentRequest.content);
var _f=cr.url+(cr.url.indexOf("?")>-1?"&":"?")+_e;
dojo.io.setIFrameSrc(this.iframe,_f,true);
}
}
catch(e){
this.iframeOnload(e);
}
};
this.canHandle=function(_10){
return ((dojo.lang.inArray(["text/plain","text/html","text/javascript","text/json","application/json"],_10["mimetype"]))&&(dojo.lang.inArray(["post","get"],_10["method"].toLowerCase()))&&(!((_10["sync"])&&(_10["sync"]==true))));
};
this.bind=function(_11){
if(!this["iframe"]){
this.setUpIframe();
}
this.requestQueue.push(_11);
this.fireNextRequest();
return;
};
this.setUpIframe=function(){
this.iframe=dojo.io.createIFrame(this.iframeName,"dojo.io.IframeTransport.iframeOnload();");
};
this.iframeOnload=function(_12){
if(!_8.currentRequest){
_8.fireNextRequest();
return;
}
var req=_8.currentRequest;
if(req.formNode){
var _14=req._contentToClean;
for(var i=0;i<_14.length;i++){
var key=_14[i];
if(dojo.render.html.safari){
var _17=req.formNode;
for(var j=0;j<_17.childNodes.length;j++){
var _19=_17.childNodes[j];
if(_19.name==key){
var _1a=_19.parentNode;
_1a.removeChild(_19);
break;
}
}
}else{
var _1b=req.formNode[key];
req.formNode.removeChild(_1b);
req.formNode[key]=null;
}
}
if(req["_originalAction"]){
req.formNode.setAttribute("action",req._originalAction);
}
if(req["_originalTarget"]){
req.formNode.setAttribute("target",req._originalTarget);
req.formNode.target=req._originalTarget;
}
}
var _1c=function(_1d){
var doc=_1d.contentDocument||((_1d.contentWindow)&&(_1d.contentWindow.document))||((_1d.name)&&(document.frames[_1d.name])&&(document.frames[_1d.name].document))||null;
return doc;
};
var _1f;
var _20=false;
if(_12){
this._callError(req,"IframeTransport Request Error: "+_12);
}else{
var ifd=_1c(_8.iframe);
try{
var cmt=req.mimetype;
if((cmt=="text/javascript")||(cmt=="text/json")||(cmt=="application/json")){
var js=ifd.getElementsByTagName("textarea")[0].value;
if(cmt=="text/json"||cmt=="application/json"){
js="("+js+")";
}
_1f=dj_eval(js);
}else{
if(cmt=="text/html"){
_1f=ifd;
}else{
_1f=ifd.getElementsByTagName("textarea")[0].value;
}
}
_20=true;
}
catch(e){
this._callError(req,"IframeTransport Error: "+e);
}
}
try{
if(_20&&dojo.lang.isFunction(req["load"])){
req.load("load",_1f,req);
}
}
catch(e){
throw e;
}
finally{
_8.currentRequest=null;
_8.fireNextRequest();
}
};
this._callError=function(req,_25){
var _26=new dojo.io.Error(_25);
if(dojo.lang.isFunction(req["error"])){
req.error("error",_26,req);
}
};
dojo.io.transports.addTransport("IframeTransport");
};
