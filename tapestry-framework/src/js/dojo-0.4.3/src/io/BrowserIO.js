dojo.provide("dojo.io.BrowserIO");
dojo.require("dojo.io.common");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.func");
dojo.require("dojo.string.extras");
dojo.require("dojo.dom");
dojo.require("dojo.undo.browser");
if(!dj_undef("window")){
dojo.io.checkChildrenForFile=function(_1){
var _2=false;
var _3=_1.getElementsByTagName("input");
dojo.lang.forEach(_3,function(_4){
if(_2){
return;
}
if(_4.getAttribute("type")=="file"){
_2=true;
}
});
return _2;
};
dojo.io.formHasFile=function(_5){
return dojo.io.checkChildrenForFile(_5);
};
dojo.io.updateNode=function(_6,_7){
_6=dojo.byId(_6);
var _8=_7;
if(dojo.lang.isString(_7)){
_8={url:_7};
}
_8.mimetype="text/html";
_8.load=function(t,d,e){
while(_6.firstChild){
dojo.dom.destroyNode(_6.firstChild);
}
_6.innerHTML=d;
};
dojo.io.bind(_8);
};
dojo.io.formFilter=function(_c){
var _d=(_c.type||"").toLowerCase();
return !_c.disabled&&_c.name&&!dojo.lang.inArray(["file","submit","image","reset","button"],_d);
};
dojo.io.encodeForm=function(_e,_f,_10){
if((!_e)||(!_e.tagName)||(!_e.tagName.toLowerCase()=="form")){
dojo.raise("Attempted to encode a non-form element.");
}
if(!_10){
_10=dojo.io.formFilter;
}
var enc=/utf/i.test(_f||"")?encodeURIComponent:dojo.string.encodeAscii;
var _12=[];
for(var i=0;i<_e.elements.length;i++){
var elm=_e.elements[i];
if(!elm||elm.tagName.toLowerCase()=="fieldset"||!_10(elm)){
continue;
}
var _15=enc(elm.name);
var _16=elm.type.toLowerCase();
if(_16=="select-multiple"){
for(var j=0;j<elm.options.length;j++){
if(elm.options[j].selected){
_12.push(_15+"="+enc(elm.options[j].value));
}
}
}else{
if(dojo.lang.inArray(["radio","checkbox"],_16)){
if(elm.checked){
_12.push(_15+"="+enc(elm.value));
}
}else{
_12.push(_15+"="+enc(elm.value));
}
}
}
var _18=_e.getElementsByTagName("input");
for(var i=0;i<_18.length;i++){
var _19=_18[i];
if(_19.type.toLowerCase()=="image"&&_19.form==_e&&_10(_19)){
var _15=enc(_19.name);
_12.push(_15+"="+enc(_19.value));
_12.push(_15+".x=0");
_12.push(_15+".y=0");
}
}
return _12.join("&")+"&";
};
dojo.io.FormBind=function(_1a){
this.bindArgs={};
if(_1a&&_1a.formNode){
this.init(_1a);
}else{
if(_1a){
this.init({formNode:_1a});
}
}
};
dojo.lang.extend(dojo.io.FormBind,{form:null,bindArgs:null,clickedButton:null,init:function(_1b){
var _1c=dojo.byId(_1b.formNode);
if(!_1c||!_1c.tagName||_1c.tagName.toLowerCase()!="form"){
throw new Error("FormBind: Couldn't apply, invalid form");
}else{
if(this.form==_1c){
return;
}else{
if(this.form){
throw new Error("FormBind: Already applied to a form");
}
}
}
dojo.lang.mixin(this.bindArgs,_1b);
this.form=_1c;
this.connect(_1c,"onsubmit","submit");
for(var i=0;i<_1c.elements.length;i++){
var _1e=_1c.elements[i];
if(_1e&&_1e.type&&dojo.lang.inArray(["submit","button"],_1e.type.toLowerCase())){
this.connect(_1e,"onclick","click");
}
}
var _1f=_1c.getElementsByTagName("input");
for(var i=0;i<_1f.length;i++){
var _20=_1f[i];
if(_20.type.toLowerCase()=="image"&&_20.form==_1c){
this.connect(_20,"onclick","click");
}
}
},onSubmit:function(_21){
return true;
},submit:function(e){
e.preventDefault();
if(this.onSubmit(this.form)){
dojo.io.bind(dojo.lang.mixin(this.bindArgs,{formFilter:dojo.lang.hitch(this,"formFilter")}));
}
},click:function(e){
var _24=e.currentTarget;
if(_24.disabled){
return;
}
this.clickedButton=_24;
},formFilter:function(_25){
var _26=(_25.type||"").toLowerCase();
var _27=false;
if(_25.disabled||!_25.name){
_27=false;
}else{
if(dojo.lang.inArray(["submit","button","image"],_26)){
if(!this.clickedButton){
this.clickedButton=_25;
}
_27=_25==this.clickedButton;
}else{
_27=!dojo.lang.inArray(["file","submit","reset","button"],_26);
}
}
return _27;
},connect:function(_28,_29,_2a){
if(dojo.evalObjPath("dojo.event.connect")){
dojo.event.connect(_28,_29,this,_2a);
}else{
var fcn=dojo.lang.hitch(this,_2a);
_28[_29]=function(e){
if(!e){
e=window.event;
}
if(!e.currentTarget){
e.currentTarget=e.srcElement;
}
if(!e.preventDefault){
e.preventDefault=function(){
window.event.returnValue=false;
};
}
fcn(e);
};
}
}});
dojo.io.XMLHTTPTransport=new function(){
var _2d=this;
var _2e={};
this.useCache=false;
this.preventCache=false;
function getCacheKey(url,_30,_31){
return url+"|"+_30+"|"+_31.toLowerCase();
}
function addToCache(url,_33,_34,_35){
_2e[getCacheKey(url,_33,_34)]=_35;
}
function getFromCache(url,_37,_38){
return _2e[getCacheKey(url,_37,_38)];
}
this.clearCache=function(){
_2e={};
};
function doLoad(_39,_3a,url,_3c,_3d){
if(((_3a.status>=200)&&(_3a.status<300))||(_3a.status==304)||(_3a.status==1223)||(location.protocol=="file:"&&(_3a.status==0||_3a.status==undefined))||(location.protocol=="chrome:"&&(_3a.status==0||_3a.status==undefined))){
var ret;
if(_39.method.toLowerCase()=="head"){
var _3f=_3a.getAllResponseHeaders();
ret={};
ret.toString=function(){
return _3f;
};
var _40=_3f.split(/[\r\n]+/g);
for(var i=0;i<_40.length;i++){
var _42=_40[i].match(/^([^:]+)\s*:\s*(.+)$/i);
if(_42){
ret[_42[1]]=_42[2];
}
}
}else{
if(_39.mimetype=="text/javascript"){
try{
ret=dj_eval(_3a.responseText);
}
catch(e){
dojo.debug(e);
dojo.debug(_3a.responseText);
ret=null;
}
}else{
if(_39.mimetype.substr(0,9)=="text/json"||_39.mimetype.substr(0,16)=="application/json"){
try{
ret=dj_eval("("+_39.jsonFilter(_3a.responseText)+")");
}
catch(e){
dojo.debug(e);
dojo.debug(_3a.responseText);
ret=false;
}
}else{
if((_39.mimetype=="application/xml")||(_39.mimetype=="text/xml")){
ret=_3a.responseXML;
if(!ret||typeof ret=="string"||!_3a.getResponseHeader("Content-Type")){
ret=dojo.dom.createDocumentFromText(_3a.responseText);
}
}else{
ret=_3a.responseText;
}
}
}
}
if(_3d){
addToCache(url,_3c,_39.method,_3a);
}
_39[(typeof _39.load=="function")?"load":"handle"]("load",ret,_3a,_39);
}else{
var _43=new dojo.io.Error("XMLHttpTransport Error: "+_3a.status+" "+_3a.statusText);
_39[(typeof _39.error=="function")?"error":"handle"]("error",_43,_3a,_39);
}
}
function setHeaders(_44,_45){
if(_45["headers"]){
for(var _46 in _45["headers"]){
if(_46.toLowerCase()=="content-type"&&!_45["contentType"]){
_45["contentType"]=_45["headers"][_46];
}else{
_44.setRequestHeader(_46,_45["headers"][_46]);
}
}
}
}
this.inFlight=[];
this.inFlightTimer=null;
this.startWatchingInFlight=function(){
if(!this.inFlightTimer){
this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);
}
};
this.watchInFlight=function(){
var now=null;
if(!dojo.hostenv._blockAsync&&!_2d._blockAsync){
for(var x=this.inFlight.length-1;x>=0;x--){
try{
var tif=this.inFlight[x];
if(!tif||tif.http._aborted||!tif.http.readyState){
this.inFlight.splice(x,1);
continue;
}
if(4==tif.http.readyState){
this.inFlight.splice(x,1);
doLoad(tif.req,tif.http,tif.url,tif.query,tif.useCache);
}else{
if(tif.startTime){
if(!now){
now=(new Date()).getTime();
}
if(tif.startTime+(tif.req.timeoutSeconds*1000)<now){
if(typeof tif.http.abort=="function"){
tif.http.abort();
}
this.inFlight.splice(x,1);
tif.req[(typeof tif.req.timeout=="function")?"timeout":"handle"]("timeout",null,tif.http,tif.req);
}
}
}
}
catch(e){
try{
var _4a=new dojo.io.Error("XMLHttpTransport.watchInFlight Error: "+e);
tif.req[(typeof tif.req.error=="function")?"error":"handle"]("error",_4a,tif.http,tif.req);
}
catch(e2){
dojo.debug("XMLHttpTransport error callback failed: "+e2);
}
}
}
}
clearTimeout(this.inFlightTimer);
if(this.inFlight.length==0){
this.inFlightTimer=null;
return;
}
this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);
};
var _4b=dojo.hostenv.getXmlhttpObject()?true:false;
this.canHandle=function(_4c){
var mlc=_4c["mimetype"].toLowerCase()||"";
return _4b&&((dojo.lang.inArray(["text/plain","text/html","application/xml","text/xml","text/javascript"],mlc))||(mlc.substr(0,9)=="text/json"||mlc.substr(0,16)=="application/json"))&&!(_4c["formNode"]&&dojo.io.formHasFile(_4c["formNode"]));
};
this.multipartBoundary="45309FFF-BD65-4d50-99C9-36986896A96F";
this.bind=function(_4e){
if(!_4e["url"]){
if(!_4e["formNode"]&&(_4e["backButton"]||_4e["back"]||_4e["changeUrl"]||_4e["watchForURL"])&&(!djConfig.preventBackButtonFix)){
dojo.deprecated("Using dojo.io.XMLHTTPTransport.bind() to add to browser history without doing an IO request","Use dojo.undo.browser.addToHistory() instead.","0.4");
dojo.undo.browser.addToHistory(_4e);
return true;
}
}
var url=_4e.url;
var _50="";
if(_4e["formNode"]){
var ta=_4e.formNode.getAttribute("action");
if((ta)&&(!_4e["url"])){
url=ta;
}
var tp=_4e.formNode.getAttribute("method");
if((tp)&&(!_4e["method"])){
_4e.method=tp;
}
_50+=dojo.io.encodeForm(_4e.formNode,_4e.encoding,_4e["formFilter"]);
}
if(url.indexOf("#")>-1){
dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);
url=url.split("#")[0];
}
if(_4e["file"]){
_4e.method="post";
}
if(!_4e["method"]){
_4e.method="get";
}
if(_4e.method.toLowerCase()=="get"){
_4e.multipart=false;
}else{
if(_4e["file"]){
_4e.multipart=true;
}else{
if(!_4e["multipart"]){
_4e.multipart=false;
}
}
}
if(_4e["backButton"]||_4e["back"]||_4e["changeUrl"]){
dojo.undo.browser.addToHistory(_4e);
}
var _53=_4e["content"]||{};
if(_4e.sendTransport){
_53["dojo.transport"]="xmlhttp";
}
do{
if(_4e.postContent){
_50=_4e.postContent;
break;
}
if(_53){
_50+=dojo.io.argsFromMap(_53,_4e.encoding);
}
if(_4e.method.toLowerCase()=="get"||!_4e.multipart){
break;
}
var t=[];
if(_50.length){
var q=_50.split("&");
for(var i=0;i<q.length;++i){
if(q[i].length){
var p=q[i].split("=");
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+p[0]+"\"","",p[1]);
}
}
}
if(_4e.file){
if(dojo.lang.isArray(_4e.file)){
for(var i=0;i<_4e.file.length;++i){
var o=_4e.file[i];
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}else{
var o=_4e.file;
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}
if(t.length){
t.push("--"+this.multipartBoundary+"--","");
_50=t.join("\r\n");
}
}while(false);
var _59=_4e["sync"]?false:true;
var _5a=_4e["preventCache"]||(this.preventCache==true&&_4e["preventCache"]!=false);
var _5b=_4e["useCache"]==true||(this.useCache==true&&_4e["useCache"]!=false);
if(!_5a&&_5b){
var _5c=getFromCache(url,_50,_4e.method);
if(_5c){
doLoad(_4e,_5c,url,_50,false);
return;
}
}
var _5d=dojo.hostenv.getXmlhttpObject(_4e);
var _5e=false;
if(_59){
var _5f=this.inFlight.push({"req":_4e,"http":_5d,"url":url,"query":_50,"useCache":_5b,"startTime":_4e.timeoutSeconds?(new Date()).getTime():0});
this.startWatchingInFlight();
}else{
_2d._blockAsync=true;
}
if(_4e.method.toLowerCase()=="post"){
if(!_4e.user){
_5d.open("POST",url,_59);
}else{
_5d.open("POST",url,_59,_4e.user,_4e.password);
}
setHeaders(_5d,_4e);
_5d.setRequestHeader("Content-Type",_4e.multipart?("multipart/form-data; boundary="+this.multipartBoundary):(_4e.contentType||"application/x-www-form-urlencoded"));
try{
_5d.send(_50);
}
catch(e){
if(typeof _5d.abort=="function"){
_5d.abort();
}
doLoad(_4e,{status:404},url,_50,_5b);
}
}else{
var _60=url;
if(_50!=""){
_60+=(_60.indexOf("?")>-1?"&":"?")+_50;
}
if(_5a){
_60+=(dojo.string.endsWithAny(_60,"?","&")?"":(_60.indexOf("?")>-1?"&":"?"))+"dojo.preventCache="+new Date().valueOf();
}
if(!_4e.user){
_5d.open(_4e.method.toUpperCase(),_60,_59);
}else{
_5d.open(_4e.method.toUpperCase(),_60,_59,_4e.user,_4e.password);
}
setHeaders(_5d,_4e);
try{
_5d.send(null);
}
catch(e){
if(typeof _5d.abort=="function"){
_5d.abort();
}
doLoad(_4e,{status:404},url,_50,_5b);
}
}
if(!_59){
doLoad(_4e,_5d,url,_50,_5b);
_2d._blockAsync=false;
}
_4e.abort=function(){
try{
_5d._aborted=true;
}
catch(e){
}
return _5d.abort();
};
return;
};
dojo.io.transports.addTransport("XMLHTTPTransport");
};
}
