if(typeof window!="undefined"){
(function(){
if(djConfig.allowQueryConfig){
var _1=document.location.toString();
var _2=_1.split("?",2);
if(_2.length>1){
var _3=_2[1];
var _4=_3.split("&");
for(var x in _4){
var sp=_4[x].split("=");
if((sp[0].length>9)&&(sp[0].substr(0,9)=="djConfig.")){
var _7=sp[0].substr(9);
try{
djConfig[_7]=eval(sp[1]);
}
catch(e){
djConfig[_7]=sp[1];
}
}
}
}
}
if(((djConfig["baseScriptUri"]=="")||(djConfig["baseRelativePath"]==""))&&(document&&document.getElementsByTagName)){
var _8=document.getElementsByTagName("script");
var _9=/(__package__|dojo|bootstrap1)\.js([\?\.]|$)/i;
for(var i=0;i<_8.length;i++){
var _b=_8[i].getAttribute("src");
if(!_b){
continue;
}
var m=_b.match(_9);
if(m){
var _d=_b.substring(0,m.index);
if(_b.indexOf("bootstrap1")>-1){
_d+="../";
}
if(!this["djConfig"]){
djConfig={};
}
if(djConfig["baseScriptUri"]==""){
djConfig["baseScriptUri"]=_d;
}
if(djConfig["baseRelativePath"]==""){
djConfig["baseRelativePath"]=_d;
}
break;
}
}
}
var dr=dojo.render;
var _f=dojo.render.html;
var drs=dojo.render.svg;
var dua=(_f.UA=navigator.userAgent);
var dav=(_f.AV=navigator.appVersion);
var t=true;
var f=false;
_f.capable=t;
_f.support.builtin=t;
dr.ver=parseFloat(_f.AV);
dr.os.mac=dav.indexOf("Macintosh")>=0;
dr.os.win=dav.indexOf("Windows")>=0;
dr.os.linux=dav.indexOf("X11")>=0;
_f.opera=dua.indexOf("Opera")>=0;
_f.khtml=(dav.indexOf("Konqueror")>=0)||(dav.indexOf("Safari")>=0);
_f.safari=dav.indexOf("Safari")>=0;
var _15=dua.indexOf("Gecko");
_f.mozilla=_f.moz=(_15>=0)&&(!_f.khtml);
if(_f.mozilla){
_f.geckoVersion=dua.substring(_15+6,_15+14);
}
_f.ie=(document.all)&&(!_f.opera);
_f.ie50=_f.ie&&dav.indexOf("MSIE 5.0")>=0;
_f.ie55=_f.ie&&dav.indexOf("MSIE 5.5")>=0;
_f.ie60=_f.ie&&dav.indexOf("MSIE 6.0")>=0;
_f.ie70=_f.ie&&dav.indexOf("MSIE 7.0")>=0;
var cm=document["compatMode"];
_f.quirks=(cm=="BackCompat")||(cm=="QuirksMode")||_f.ie55||_f.ie50;
dojo.locale=dojo.locale||(_f.ie?navigator.userLanguage:navigator.language).toLowerCase();
dr.vml.capable=_f.ie;
drs.capable=f;
drs.support.plugin=f;
drs.support.builtin=f;
var _17=window["document"];
var tdi=_17["implementation"];
if((tdi)&&(tdi["hasFeature"])&&(tdi.hasFeature("org.w3c.dom.svg","1.0"))){
drs.capable=t;
drs.support.builtin=t;
drs.support.plugin=f;
}
if(_f.safari){
var tmp=dua.split("AppleWebKit/")[1];
var ver=parseFloat(tmp.split(" ")[0]);
if(ver>=420){
drs.capable=t;
drs.support.builtin=t;
drs.support.plugin=f;
}
}else{
}
})();
dojo.hostenv.startPackage("dojo.hostenv");
dojo.render.name=dojo.hostenv.name_="browser";
dojo.hostenv.searchIds=[];
dojo.hostenv._XMLHTTP_PROGIDS=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"];
dojo.hostenv.getXmlhttpObject=function(){
var _1b=null;
var _1c=null;
try{
_1b=new XMLHttpRequest();
}
catch(e){
}
if(!_1b){
for(var i=0;i<3;++i){
var _1e=dojo.hostenv._XMLHTTP_PROGIDS[i];
try{
_1b=new ActiveXObject(_1e);
}
catch(e){
_1c=e;
}
if(_1b){
dojo.hostenv._XMLHTTP_PROGIDS=[_1e];
break;
}
}
}
if(!_1b){
return dojo.raise("XMLHTTP not available",_1c);
}
return _1b;
};
dojo.hostenv._blockAsync=false;
dojo.hostenv.getText=function(uri,_20,_21){
if(!_20){
this._blockAsync=true;
}
var _22=this.getXmlhttpObject();
function isDocumentOk(_23){
var _24=_23["status"];
return Boolean((!_24)||((200<=_24)&&(300>_24))||(_24==304));
}
if(_20){
var _25=this,_26=null,gbl=dojo.global();
var xhr=dojo.evalObjPath("dojo.io.XMLHTTPTransport");
_22.onreadystatechange=function(){
if(_26){
gbl.clearTimeout(_26);
_26=null;
}
if(_25._blockAsync||(xhr&&xhr._blockAsync)){
_26=gbl.setTimeout(function(){
_22.onreadystatechange.apply(this);
},10);
}else{
if(4==_22.readyState){
if(isDocumentOk(_22)){
_20(_22.responseText);
}
}
}
};
}
_22.open("GET",uri,_20?true:false);
try{
_22.send(null);
if(_20){
return null;
}
if(!isDocumentOk(_22)){
var err=Error("Unable to load "+uri+" status:"+_22.status);
err.status=_22.status;
err.responseText=_22.responseText;
throw err;
}
}
catch(e){
this._blockAsync=false;
if((_21)&&(!_20)){
return null;
}else{
throw e;
}
}
this._blockAsync=false;
return _22.responseText;
};
dojo.hostenv.defaultDebugContainerId="dojoDebug";
dojo.hostenv._println_buffer=[];
dojo.hostenv._println_safe=false;
dojo.hostenv.println=function(_2a){
if(!dojo.hostenv._println_safe){
dojo.hostenv._println_buffer.push(_2a);
}else{
try{
var _2b=document.getElementById(djConfig.debugContainerId?djConfig.debugContainerId:dojo.hostenv.defaultDebugContainerId);
if(!_2b){
_2b=dojo.body();
}
var div=document.createElement("div");
div.appendChild(document.createTextNode(_2a));
_2b.appendChild(div);
}
catch(e){
try{
document.write("<div>"+_2a+"</div>");
}
catch(e2){
window.status=_2a;
}
}
}
};
dojo.addOnLoad(function(){
dojo.hostenv._println_safe=true;
while(dojo.hostenv._println_buffer.length>0){
dojo.hostenv.println(dojo.hostenv._println_buffer.shift());
}
});
function dj_addNodeEvtHdlr(_2d,_2e,fp){
var _30=_2d["on"+_2e]||function(){
};
_2d["on"+_2e]=function(){
fp.apply(_2d,arguments);
_30.apply(_2d,arguments);
};
return true;
}
dojo.hostenv._djInitFired=false;
function dj_load_init(e){
dojo.hostenv._djInitFired=true;
var _32=(e&&e.type)?e.type.toLowerCase():"load";
if(arguments.callee.initialized||(_32!="domcontentloaded"&&_32!="load")){
return;
}
arguments.callee.initialized=true;
if(typeof (_timer)!="undefined"){
clearInterval(_timer);
delete _timer;
}
var _33=function(){
if(dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
};
if(dojo.hostenv.inFlightCount==0){
_33();
dojo.hostenv.modulesLoaded();
}else{
dojo.hostenv.modulesLoadedListeners.unshift(_33);
}
}
if(document.addEventListener){
if(dojo.render.html.opera||(dojo.render.html.moz&&(djConfig["enableMozDomContentLoaded"]===true))){
document.addEventListener("DOMContentLoaded",dj_load_init,null);
}
window.addEventListener("load",dj_load_init,null);
}
if(dojo.render.html.ie&&dojo.render.os.win){
document.attachEvent("onreadystatechange",function(e){
if(document.readyState=="complete"){
dj_load_init();
}
});
}
if(/(WebKit|khtml)/i.test(navigator.userAgent)){
var _timer=setInterval(function(){
if(/loaded|complete/.test(document.readyState)){
dj_load_init();
}
},10);
}
if(dojo.render.html.ie){
dj_addNodeEvtHdlr(window,"beforeunload",function(){
dojo.hostenv._unloading=true;
window.setTimeout(function(){
dojo.hostenv._unloading=false;
},0);
});
}
dj_addNodeEvtHdlr(window,"unload",function(){
dojo.hostenv.unloaded();
if((!dojo.render.html.ie)||(dojo.render.html.ie&&dojo.hostenv._unloading)){
dojo.hostenv.unloaded();
}
});
dojo.hostenv.makeWidgets=function(){
var _35=[];
if(djConfig.searchIds&&djConfig.searchIds.length>0){
_35=_35.concat(djConfig.searchIds);
}
if(dojo.hostenv.searchIds&&dojo.hostenv.searchIds.length>0){
_35=_35.concat(dojo.hostenv.searchIds);
}
if((djConfig.parseWidgets)||(_35.length>0)){
if(dojo.evalObjPath("dojo.widget.Parse")){
var _36=new dojo.xml.Parse();
if(_35.length>0){
for(var x=0;x<_35.length;x++){
var _38=document.getElementById(_35[x]);
if(!_38){
continue;
}
var _39=_36.parseElement(_38,null,true);
dojo.widget.getParser().createComponents(_39);
}
}else{
if(djConfig.parseWidgets){
var _39=_36.parseElement(dojo.body(),null,true);
dojo.widget.getParser().createComponents(_39);
}
}
}
}
};
dojo.addOnLoad(function(){
if(!dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
});
try{
if(dojo.render.html.ie){
document.namespaces.add("v","urn:schemas-microsoft-com:vml");
document.createStyleSheet().addRule("v\\:*","behavior:url(#default#VML)");
}
}
catch(e){
}
dojo.hostenv.writeIncludes=function(){
};
if(!dj_undef("document",this)){
dj_currentDocument=this.document;
}
dojo.doc=function(){
return dj_currentDocument;
};
dojo.body=function(){
return dojo.doc().body||dojo.doc().getElementsByTagName("body")[0];
};
dojo.byId=function(id,doc){
if((id)&&((typeof id=="string")||(id instanceof String))){
if(!doc){
doc=dj_currentDocument;
}
var ele=doc.getElementById(id);
if(ele&&(ele.id!=id)&&doc.all){
ele=null;
eles=doc.all[id];
if(eles){
if(eles.length){
for(var i=0;i<eles.length;i++){
if(eles[i].id==id){
ele=eles[i];
break;
}
}
}else{
ele=eles;
}
}
}
return ele;
}
return id;
};
dojo.setContext=function(_3e,_3f){
dj_currentContext=_3e;
dj_currentDocument=_3f;
};
dojo._fireCallback=function(_40,_41,_42){
if((_41)&&((typeof _40=="string")||(_40 instanceof String))){
_40=_41[_40];
}
return (_41?_40.apply(_41,_42||[]):_40());
};
dojo.withGlobal=function(_43,_44,_45,_46){
var _47;
var _48=dj_currentContext;
var _49=dj_currentDocument;
try{
dojo.setContext(_43,_43.document);
_47=dojo._fireCallback(_44,_45,_46);
}
finally{
dojo.setContext(_48,_49);
}
return _47;
};
dojo.withDoc=function(_4a,_4b,_4c,_4d){
var _4e;
var _4f=dj_currentDocument;
try{
dj_currentDocument=_4a;
_4e=dojo._fireCallback(_4b,_4c,_4d);
}
finally{
dj_currentDocument=_4f;
}
return _4e;
};
}
dojo.requireIf((djConfig["isDebug"]||djConfig["debugAtAllCosts"]),"dojo.debug");
dojo.requireIf(djConfig["debugAtAllCosts"]&&!window.widget&&!djConfig["useXDomain"],"dojo.browser_debug");
dojo.requireIf(djConfig["debugAtAllCosts"]&&!window.widget&&djConfig["useXDomain"],"dojo.browser_debug_xd");
