if(typeof window=="undefined"){
dojo.raise("attempt to use adobe svg hostenv when no window object");
}
dojo.debug=function(){
if(!djConfig.isDebug){
return;
}
var _1=arguments;
var _2=dj_global["jum"];
var s=_2?"":"DEBUG: ";
for(var i=0;i<_1.length;++i){
s+=_1[i];
}
if(_2){
jum.debug(s);
}else{
dojo.hostenv.println(s);
}
};
dojo.render.name=navigator.appName;
dojo.render.ver=parseFloat(navigator.appVersion,10);
switch(navigator.platform){
case "MacOS":
dojo.render.os.osx=true;
break;
case "Linux":
dojo.render.os.linux=true;
break;
case "Windows":
dojo.render.os.win=true;
break;
default:
dojo.render.os.linux=true;
break;
}
dojo.render.svg.capable=true;
dojo.render.svg.support.builtin=true;
dojo.render.svg.moz=((navigator.userAgent.indexOf("Gecko")>=0)&&(!((navigator.appVersion.indexOf("Konqueror")>=0)||(navigator.appVersion.indexOf("Safari")>=0))));
dojo.render.svg.adobe=(window.parseXML!=null);
dojo.hostenv.startPackage("dojo.hostenv");
dojo.hostenv.println=function(s){
try{
var ti=document.createElement("text");
ti.setAttribute("x","50");
ti.setAttribute("y",(25+15*document.getElementsByTagName("text").length));
ti.appendChild(document.createTextNode(s));
document.documentElement.appendChild(ti);
}
catch(e){
}
};
dojo.hostenv.name_="svg";
dojo.hostenv.setModulePrefix=function(_7,_8){
};
dojo.hostenv.getModulePrefix=function(_9){
};
dojo.hostenv.getTextStack=[];
dojo.hostenv.loadUriStack=[];
dojo.hostenv.loadedUris=[];
dojo.hostenv.modules_={};
dojo.hostenv.modulesLoadedFired=false;
dojo.hostenv.modulesLoadedListeners=[];
dojo.hostenv.getText=function(_a,cb,_c){
if(!cb){
var cb=function(_d){
window.alert(_d);
};
}
if(!_c){
window.getUrl(_a,cb);
}else{
window.postUrl(_a,_c,cb);
}
};
dojo.hostenv.getLibaryScriptUri=function(){
};
dojo.hostenv.loadUri=function(_e){
};
dojo.hostenv.loadUriAndCheck=function(_f,_10){
};
dojo.hostenv.loadModule=function(_11){
var a=_11.split(".");
var _13=window;
var s=[];
for(var i=0;i<a.length;i++){
if(a[i]=="*"){
continue;
}
s.push(a[i]);
if(!_13[a[i]]){
dojo.raise("dojo.require('"+_11+"'): module does not exist.");
}else{
_13=_13[a[i]];
}
}
return;
};
dojo.hostenv.startPackage=function(_16){
var a=_16.split(".");
var _18=window;
var s=[];
for(var i=0;i<a.length;i++){
if(a[i]=="*"){
continue;
}
s.push(a[i]);
if(!_18[a[i]]){
_18[a[i]]={};
}
_18=_18[a[i]];
}
return;
};
if(window.parseXML){
window.XMLSerialzer=function(){
function nodeToString(n,a){
function fixText(s){
return String(s).replace(/\&/g,"&amp;").replace(/>/g,"&gt;").replace(/</g,"&lt;");
}
function fixAttribute(s){
return fixText(s).replace(/\"/g,"&quot;");
}
switch(n.nodeType){
case 1:
var _1f=n.nodeName;
a.push("<"+_1f);
for(var i=0;i<n.attributes.length;i++){
if(n.attributes.item(i).specified){
a.push(" "+n.attributes.item(i).nodeName.toLowerCase()+"=\""+fixAttribute(n.attributes.item(i).nodeValue)+"\"");
}
}
if(n.canHaveChildren||n.hasChildNodes()){
a.push(">");
for(var i=0;i<n.childNodes.length;i++){
nodeToString(n.childNodes.item(i),a);
}
a.push("</"+_1f+">\n");
}else{
a.push(" />\n");
}
break;
case 3:
a.push(fixText(n.nodeValue));
break;
case 4:
a.push("<![CDA"+"TA[\n"+n.nodeValue+"\n]"+"]>");
break;
case 7:
a.push(n.nodeValue);
if(/(^<\?xml)|(^<\!DOCTYPE)/.test(n.nodeValue)){
a.push("\n");
}
break;
case 8:
a.push("<!-- "+n.nodeValue+" -->\n");
break;
case 9:
case 11:
for(var i=0;i<n.childNodes.length;i++){
nodeToString(n.childNodes.item(i),a);
}
break;
default:
a.push("<!--\nNot Supported:\n\n"+"nodeType: "+n.nodeType+"\nnodeName: "+n.nodeName+"\n-->");
}
}
this.serializeToString=function(_21){
var a=[];
nodeToString(_21,a);
return a.join("");
};
};
window.DOMParser=function(){
this.parseFromString=function(s){
return parseXML(s,window.document);
};
};
window.XMLHttpRequest=function(){
var uri=null;
var _25="POST";
var _26=true;
var cb=function(d){
this.responseText=d.content;
try{
this.responseXML=parseXML(this.responseText,window.document);
}
catch(e){
}
this.status="200";
this.statusText="OK";
if(!d.success){
this.status="500";
this.statusText="Internal Server Error";
}
this.onload();
this.onreadystatechange();
};
this.onload=function(){
};
this.readyState=4;
this.onreadystatechange=function(){
};
this.status=0;
this.statusText="";
this.responseBody=null;
this.responseStream=null;
this.responseXML=null;
this.responseText=null;
this.abort=function(){
return;
};
this.getAllResponseHeaders=function(){
return [];
};
this.getResponseHeader=function(n){
return null;
};
this.setRequestHeader=function(nm,val){
};
this.open=function(_2c,url,_2e){
_25=_2c;
uri=url;
};
this.send=function(_2f){
var d=_2f||null;
if(_25=="GET"){
getURL(uri,cb);
}else{
postURL(uri,_2f,cb);
}
};
};
}
dojo.requireIf((djConfig["isDebug"]||djConfig["debugAtAllCosts"]),"dojo.debug");
