/*
	Copyright (c) 2004-2005, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

/*
	This is a compiled version of Dojo, built for deployment and not for
	development. To get an editable version, please visit:

		http://dojotoolkit.org

	for documentation and information on getting the source.
*/

var dj_global=this;
function dj_undef(_1,_2){
if(!_2){
_2=dj_global;
}
return (typeof _2[_1]=="undefined");
}
if(dj_undef("djConfig")){
var djConfig={};
}
var dojo;
if(dj_undef("dojo")){
dojo={};
}
dojo.version={major:0,minor:2,patch:2,flag:"+",revision:Number("$Rev: 2889 $".match(/[0-9]+/)[0]),toString:function(){
with(dojo.version){
return major+"."+minor+"."+patch+flag+" ("+revision+")";
}
}};
dojo.evalObjPath=function(_3,_4){
if(typeof _3!="string"){
return dj_global;
}
if(_3.indexOf(".")==-1){
if((dj_undef(_3,dj_global))&&(_4)){
dj_global[_3]={};
}
return dj_global[_3];
}
var _5=_3.split(/\./);
var _6=dj_global;
for(var i=0;i<_5.length;++i){
if(!_4){
_6=_6[_5[i]];
if((typeof _6=="undefined")||(!_6)){
return _6;
}
}else{
if(dj_undef(_5[i],_6)){
_6[_5[i]]={};
}
_6=_6[_5[i]];
}
}
return _6;
};
dojo.errorToString=function(_8){
return ((!dj_undef("message",_8))?_8.message:(dj_undef("description",_8)?_8:_8.description));
};
dojo.raise=function(_9,_a){
if(_a){
_9=_9+": "+dojo.errorToString(_a);
}
var he=dojo.hostenv;
if((!dj_undef("hostenv",dojo))&&(!dj_undef("println",dojo.hostenv))){
dojo.hostenv.println("FATAL: "+_9);
}
throw Error(_9);
};
dj_throw=dj_rethrow=function(m,e){
dojo.deprecated("dj_throw and dj_rethrow deprecated, use dojo.raise instead");
dojo.raise(m,e);
};
dojo.debug=function(){
if(!djConfig.isDebug){
return;
}
var _e=arguments;
if(dj_undef("println",dojo.hostenv)){
dojo.raise("dojo.debug not available (yet?)");
}
var _f=dj_global["jum"]&&!dj_global["jum"].isBrowser;
var s=[(_f?"":"DEBUG: ")];
for(var i=0;i<_e.length;++i){
if(!false&&_e[i] instanceof Error){
var msg="["+_e[i].name+": "+dojo.errorToString(_e[i])+(_e[i].fileName?", file: "+_e[i].fileName:"")+(_e[i].lineNumber?", line: "+_e[i].lineNumber:"")+"]";
}else{
try{
var msg=String(_e[i]);
}
catch(e){
if(dojo.render.html.ie){
var msg="[ActiveXObject]";
}else{
var msg="[unknown]";
}
}
}
s.push(msg);
}
if(_f){
jum.debug(s.join(" "));
}else{
dojo.hostenv.println(s.join(" "));
}
};
dojo.debugShallow=function(obj){
if(!djConfig.isDebug){
return;
}
dojo.debug("------------------------------------------------------------");
dojo.debug("Object: "+obj);
var _14=[];
for(var _15 in obj){
try{
_14.push(_15+": "+obj[_15]);
}
catch(E){
_14.push(_15+": ERROR - "+E.message);
}
}
_14.sort();
for(var i=0;i<_14.length;i++){
dojo.debug(_14[i]);
}
dojo.debug("------------------------------------------------------------");
};
var dj_debug=dojo.debug;
function dj_eval(s){
return dj_global.eval?dj_global.eval(s):eval(s);
}
dj_unimplemented=dojo.unimplemented=function(_18,_19){
var _1a="'"+_18+"' not implemented";
if((!dj_undef(_19))&&(_19)){
_1a+=" "+_19;
}
dojo.raise(_1a);
};
dj_deprecated=dojo.deprecated=function(_1b,_1c,_1d){
var _1e="DEPRECATED: "+_1b;
if(_1c){
_1e+=" "+_1c;
}
if(_1d){
_1e+=" -- will be removed in version: "+_1d;
}
dojo.debug(_1e);
};
dojo.inherits=function(_1f,_20){
if(typeof _20!="function"){
dojo.raise("superclass: "+_20+" borken");
}
_1f.prototype=new _20();
_1f.prototype.constructor=_1f;
_1f.superclass=_20.prototype;
_1f["super"]=_20.prototype;
};
dj_inherits=function(_21,_22){
dojo.deprecated("dj_inherits deprecated, use dojo.inherits instead");
dojo.inherits(_21,_22);
};
dojo.render=(function(){
function vscaffold(_23,_24){
var tmp={capable:false,support:{builtin:false,plugin:false},prefixes:_23};
for(var x in _24){
tmp[x]=false;
}
return tmp;
}
return {name:"",ver:dojo.version,os:{win:false,linux:false,osx:false},html:vscaffold(["html"],["ie","opera","khtml","safari","moz"]),svg:vscaffold(["svg"],["corel","adobe","batik"]),vml:vscaffold(["vml"],["ie"]),swf:vscaffold(["Swf","Flash","Mm"],["mm"]),swt:vscaffold(["Swt"],["ibm"])};
})();
dojo.hostenv=(function(){
var _27={isDebug:false,allowQueryConfig:false,baseScriptUri:"",baseRelativePath:"",libraryScriptUri:"",iePreventClobber:false,ieClobberMinimal:true,preventBackButtonFix:true,searchIds:[],parseWidgets:true};
if(typeof djConfig=="undefined"){
djConfig=_27;
}else{
for(var _28 in _27){
if(typeof djConfig[_28]=="undefined"){
djConfig[_28]=_27[_28];
}
}
}
var djc=djConfig;
function _def(obj,_2b,def){
return (dj_undef(_2b,obj)?def:obj[_2b]);
}
return {name_:"(unset)",version_:"(unset)",pkgFileName:"__package__",loading_modules_:{},loaded_modules_:{},addedToLoadingCount:[],removedFromLoadingCount:[],inFlightCount:0,modulePrefixes_:{dojo:{name:"dojo",value:"src"}},setModulePrefix:function(_2d,_2e){
this.modulePrefixes_[_2d]={name:_2d,value:_2e};
},getModulePrefix:function(_2f){
var mp=this.modulePrefixes_;
if((mp[_2f])&&(mp[_2f]["name"])){
return mp[_2f].value;
}
return _2f;
},getTextStack:[],loadUriStack:[],loadedUris:[],post_load_:false,modulesLoadedListeners:[],getName:function(){
return this.name_;
},getVersion:function(){
return this.version_;
},getText:function(uri){
dojo.unimplemented("getText","uri="+uri);
},getLibraryScriptUri:function(){
dojo.unimplemented("getLibraryScriptUri","");
}};
})();
dojo.hostenv.getBaseScriptUri=function(){
if(djConfig.baseScriptUri.length){
return djConfig.baseScriptUri;
}
var uri=new String(djConfig.libraryScriptUri||djConfig.baseRelativePath);
if(!uri){
dojo.raise("Nothing returned by getLibraryScriptUri(): "+uri);
}
var _33=uri.lastIndexOf("/");
djConfig.baseScriptUri=djConfig.baseRelativePath;
return djConfig.baseScriptUri;
};
dojo.hostenv.setBaseScriptUri=function(uri){
djConfig.baseScriptUri=uri;
};
dojo.hostenv.loadPath=function(_35,_36,cb){
if((_35.charAt(0)=="/")||(_35.match(/^\w+:/))){
dojo.raise("relpath '"+_35+"'; must be relative");
}
var uri=this.getBaseScriptUri()+_35;
if(djConfig.cacheBust&&dojo.render.html.capable){
uri+="?"+String(djConfig.cacheBust).replace(/\W+/g,"");
}
try{
return ((!_36)?this.loadUri(uri,cb):this.loadUriAndCheck(uri,_36,cb));
}
catch(e){
dojo.debug(e);
return false;
}
};
dojo.hostenv.loadUri=function(uri,cb){
if(this.loadedUris[uri]){
return;
}
var _3b=this.getText(uri,null,true);
if(_3b==null){
return 0;
}
this.loadedUris[uri]=true;
var _3c=dj_eval(_3b);
return 1;
};
dojo.hostenv.loadUriAndCheck=function(uri,_3e,cb){
var ok=true;
try{
ok=this.loadUri(uri,cb);
}
catch(e){
dojo.debug("failed loading ",uri," with error: ",e);
}
return ((ok)&&(this.findModule(_3e,false)))?true:false;
};
dojo.loaded=function(){
};
dojo.hostenv.loaded=function(){
this.post_load_=true;
var mll=this.modulesLoadedListeners;
for(var x=0;x<mll.length;x++){
mll[x]();
}
dojo.loaded();
};
dojo.addOnLoad=function(obj,_44){
if(arguments.length==1){
dojo.hostenv.modulesLoadedListeners.push(obj);
}else{
if(arguments.length>1){
dojo.hostenv.modulesLoadedListeners.push(function(){
obj[_44]();
});
}
}
};
dojo.hostenv.modulesLoaded=function(){
if(this.post_load_){
return;
}
if((this.loadUriStack.length==0)&&(this.getTextStack.length==0)){
if(this.inFlightCount>0){
dojo.debug("files still in flight!");
return;
}
if(typeof setTimeout=="object"){
setTimeout("dojo.hostenv.loaded();",0);
}else{
dojo.hostenv.loaded();
}
}
};
dojo.hostenv.moduleLoaded=function(_45){
var _46=dojo.evalObjPath((_45.split(".").slice(0,-1)).join("."));
this.loaded_modules_[(new String(_45)).toLowerCase()]=_46;
};
dojo.hostenv._global_omit_module_check=false;
dojo.hostenv.loadModule=function(_47,_48,_49){
if(!_47){
return;
}
_49=this._global_omit_module_check||_49;
var _4a=this.findModule(_47,false);
if(_4a){
return _4a;
}
if(dj_undef(_47,this.loading_modules_)){
this.addedToLoadingCount.push(_47);
}
this.loading_modules_[_47]=1;
var _4b=_47.replace(/\./g,"/")+".js";
var _4c=_47.split(".");
var _4d=_47.split(".");
for(var i=_4c.length-1;i>0;i--){
var _4f=_4c.slice(0,i).join(".");
var _50=this.getModulePrefix(_4f);
if(_50!=_4f){
_4c.splice(0,i,_50);
break;
}
}
var _51=_4c[_4c.length-1];
if(_51=="*"){
_47=(_4d.slice(0,-1)).join(".");
while(_4c.length){
_4c.pop();
_4c.push(this.pkgFileName);
_4b=_4c.join("/")+".js";
if(_4b.charAt(0)=="/"){
_4b=_4b.slice(1);
}
ok=this.loadPath(_4b,((!_49)?_47:null));
if(ok){
break;
}
_4c.pop();
}
}else{
_4b=_4c.join("/")+".js";
_47=_4d.join(".");
var ok=this.loadPath(_4b,((!_49)?_47:null));
if((!ok)&&(!_48)){
_4c.pop();
while(_4c.length){
_4b=_4c.join("/")+".js";
ok=this.loadPath(_4b,((!_49)?_47:null));
if(ok){
break;
}
_4c.pop();
_4b=_4c.join("/")+"/"+this.pkgFileName+".js";
if(_4b.charAt(0)=="/"){
_4b=_4b.slice(1);
}
ok=this.loadPath(_4b,((!_49)?_47:null));
if(ok){
break;
}
}
}
if((!ok)&&(!_49)){
dojo.raise("Could not load '"+_47+"'; last tried '"+_4b+"'");
}
}
if(!_49){
_4a=this.findModule(_47,false);
if(!_4a){
dojo.raise("symbol '"+_47+"' is not defined after loading '"+_4b+"'");
}
}
return _4a;
};
dojo.hostenv.startPackage=function(_53){
var _54=_53.split(/\./);
if(_54[_54.length-1]=="*"){
_54.pop();
}
return dojo.evalObjPath(_54.join("."),true);
};
dojo.hostenv.findModule=function(_55,_56){
var lmn=(new String(_55)).toLowerCase();
if(this.loaded_modules_[lmn]){
return this.loaded_modules_[lmn];
}
var _58=dojo.evalObjPath(_55);
if((_55)&&(typeof _58!="undefined")&&(_58)){
this.loaded_modules_[lmn]=_58;
return _58;
}
if(_56){
dojo.raise("no loaded module named '"+_55+"'");
}
return null;
};
if(typeof window=="undefined"){
dojo.raise("no window object");
}
(function(){
if(djConfig.allowQueryConfig){
var _59=document.location.toString();
var _5a=_59.split("?",2);
if(_5a.length>1){
var _5b=_5a[1];
var _5c=_5b.split("&");
for(var x in _5c){
var sp=_5c[x].split("=");
if((sp[0].length>9)&&(sp[0].substr(0,9)=="djConfig.")){
var opt=sp[0].substr(9);
try{
djConfig[opt]=eval(sp[1]);
}
catch(e){
djConfig[opt]=sp[1];
}
}
}
}
}
if(((djConfig["baseScriptUri"]=="")||(djConfig["baseRelativePath"]==""))&&(document&&document.getElementsByTagName)){
var _60=document.getElementsByTagName("script");
var _61=/(__package__|dojo|bootstrap1)\.js([\?\.]|$)/i;
for(var i=0;i<_60.length;i++){
var src=_60[i].getAttribute("src");
if(!src){
continue;
}
var m=src.match(_61);
if(m){
root=src.substring(0,m.index);
if(src.indexOf("bootstrap1")>-1){
root+="../";
}
if(!this["djConfig"]){
djConfig={};
}
if(djConfig["baseScriptUri"]==""){
djConfig["baseScriptUri"]=root;
}
if(djConfig["baseRelativePath"]==""){
djConfig["baseRelativePath"]=root;
}
break;
}
}
}
var dr=dojo.render;
var drh=dojo.render.html;
var drs=dojo.render.svg;
var dua=drh.UA=navigator.userAgent;
var dav=drh.AV=navigator.appVersion;
var t=true;
var f=false;
drh.capable=t;
drh.support.builtin=t;
dr.ver=parseFloat(drh.AV);
dr.os.mac=dav.indexOf("Macintosh")>=0;
dr.os.win=dav.indexOf("Windows")>=0;
dr.os.linux=dav.indexOf("X11")>=0;
drh.opera=dua.indexOf("Opera")>=0;
drh.khtml=(dav.indexOf("Konqueror")>=0)||(dav.indexOf("Safari")>=0);
drh.safari=dav.indexOf("Safari")>=0;
var _6c=dua.indexOf("Gecko");
drh.mozilla=drh.moz=(_6c>=0)&&(!drh.khtml);
if(drh.mozilla){
drh.geckoVersion=dua.substring(_6c+6,_6c+14);
}
drh.ie=(document.all)&&(!drh.opera);
drh.ie50=drh.ie&&dav.indexOf("MSIE 5.0")>=0;
drh.ie55=drh.ie&&dav.indexOf("MSIE 5.5")>=0;
drh.ie60=drh.ie&&dav.indexOf("MSIE 6.0")>=0;
dr.vml.capable=drh.ie;
drs.capable=f;
drs.support.plugin=f;
drs.support.builtin=f;
drs.adobe=f;
if(document.implementation&&document.implementation.hasFeature&&document.implementation.hasFeature("org.w3c.dom.svg","1.0")){
drs.capable=t;
drs.support.builtin=t;
drs.support.plugin=f;
drs.adobe=f;
}else{
if(navigator.mimeTypes&&navigator.mimeTypes.length>0){
var _6d=navigator.mimeTypes["image/svg+xml"]||navigator.mimeTypes["image/svg"]||navigator.mimeTypes["image/svg-xml"];
if(_6d){
drs.adobe=_6d&&_6d.enabledPlugin&&_6d.enabledPlugin.description&&(_6d.enabledPlugin.description.indexOf("Adobe")>-1);
if(drs.adobe){
drs.capable=t;
drs.support.plugin=t;
}
}
}else{
if(drh.ie&&dr.os.win){
var _6d=f;
try{
var _6e=new ActiveXObject("Adobe.SVGCtl");
_6d=t;
}
catch(e){
}
if(_6d){
drs.capable=t;
drs.support.plugin=t;
drs.adobe=t;
}
}else{
drs.capable=f;
drs.support.plugin=f;
drs.adobe=f;
}
}
}
})();
dojo.hostenv.startPackage("dojo.hostenv");
dojo.hostenv.name_="browser";
dojo.hostenv.searchIds=[];
var DJ_XMLHTTP_PROGIDS=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"];
dojo.hostenv.getXmlhttpObject=function(){
var _6f=null;
var _70=null;
try{
_6f=new XMLHttpRequest();
}
catch(e){
}
if(!_6f){
for(var i=0;i<3;++i){
var _72=DJ_XMLHTTP_PROGIDS[i];
try{
_6f=new ActiveXObject(_72);
}
catch(e){
_70=e;
}
if(_6f){
DJ_XMLHTTP_PROGIDS=[_72];
break;
}
}
}
if(!_6f){
return dojo.raise("XMLHTTP not available",_70);
}
return _6f;
};
dojo.hostenv.getText=function(uri,_74,_75){
var _76=this.getXmlhttpObject();
if(_74){
_76.onreadystatechange=function(){
if((4==_76.readyState)&&(_76["status"])){
if(_76.status==200){
_74(_76.responseText);
}
}
};
}
_76.open("GET",uri,_74?true:false);
try{
_76.send(null);
}
catch(e){
if(_75&&!_74){
return null;
}else{
throw e;
}
}
if(_74){
return null;
}
return _76.responseText;
};
dojo.hostenv.defaultDebugContainerId="dojoDebug";
dojo.hostenv._println_buffer=[];
dojo.hostenv._println_safe=false;
dojo.hostenv.println=function(_77){
if(!dojo.hostenv._println_safe){
dojo.hostenv._println_buffer.push(_77);
}else{
try{
var _78=document.getElementById(djConfig.debugContainerId?djConfig.debugContainerId:dojo.hostenv.defaultDebugContainerId);
if(!_78){
_78=document.getElementsByTagName("body")[0]||document.body;
}
var div=document.createElement("div");
div.appendChild(document.createTextNode(_77));
_78.appendChild(div);
}
catch(e){
try{
document.write("<div>"+_77+"</div>");
}
catch(e2){
window.status=_77;
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
function dj_addNodeEvtHdlr(_7a,_7b,fp,_7d){
var _7e=_7a["on"+_7b]||function(){
};
_7a["on"+_7b]=function(){
fp.apply(_7a,arguments);
_7e.apply(_7a,arguments);
};
return true;
}
dj_load_init=function(){
if(arguments.callee.initialized){
return;
}
arguments.callee.initialized=true;
if(dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
dojo.hostenv.modulesLoaded();
};
dj_addNodeEvtHdlr(window,"load",dj_load_init);
dojo.hostenv.makeWidgets=function(){
var _7f=[];
if(djConfig.searchIds&&djConfig.searchIds.length>0){
_7f=_7f.concat(djConfig.searchIds);
}
if(dojo.hostenv.searchIds&&dojo.hostenv.searchIds.length>0){
_7f=_7f.concat(dojo.hostenv.searchIds);
}
if((djConfig.parseWidgets)||(_7f.length>0)){
if(dojo.evalObjPath("dojo.widget.Parse")){
try{
var _80=new dojo.xml.Parse();
if(_7f.length>0){
for(var x=0;x<_7f.length;x++){
var _82=document.getElementById(_7f[x]);
if(!_82){
continue;
}
var _83=_80.parseElement(_82,null,true);
dojo.widget.getParser().createComponents(_83);
}
}else{
if(djConfig.parseWidgets){
var _83=_80.parseElement(document.getElementsByTagName("body")[0]||document.body,null,true);
dojo.widget.getParser().createComponents(_83);
}
}
}
catch(e){
dojo.debug("auto-build-widgets error:",e);
}
}
}
};
dojo.hostenv.modulesLoadedListeners.push(function(){
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
dojo.hostenv.byId=dojo.byId=function(id,doc){
if(id&&(typeof id=="string"||id instanceof String)){
if(!doc){
doc=document;
}
return doc.getElementById(id);
}
return id;
};
dojo.hostenv.byIdArray=dojo.byIdArray=function(){
var ids=[];
for(var i=0;i<arguments.length;i++){
if((arguments[i] instanceof Array)||(typeof arguments[i]=="array")){
for(var j=0;j<arguments[i].length;j++){
ids=ids.concat(dojo.hostenv.byIdArray(arguments[i][j]));
}
}else{
ids.push(dojo.hostenv.byId(arguments[i]));
}
}
return ids;
};
dojo.hostenv.conditionalLoadModule=function(_89){
var _8a=_89["common"]||[];
var _8b=(_89[dojo.hostenv.name_])?_8a.concat(_89[dojo.hostenv.name_]||[]):_8a.concat(_89["default"]||[]);
for(var x=0;x<_8b.length;x++){
var _8d=_8b[x];
if(_8d.constructor==Array){
dojo.hostenv.loadModule.apply(dojo.hostenv,_8d);
}else{
dojo.hostenv.loadModule(_8d);
}
}
};
dojo.hostenv.require=dojo.hostenv.loadModule;
dojo.require=function(){
dojo.hostenv.loadModule.apply(dojo.hostenv,arguments);
};
dojo.requireAfter=dojo.require;
dojo.requireIf=function(){
if((arguments[0]===true)||(arguments[0]=="common")||(dojo.render[arguments[0]].capable)){
var _8e=[];
for(var i=1;i<arguments.length;i++){
_8e.push(arguments[i]);
}
dojo.require.apply(dojo,_8e);
}
};
dojo.requireAfterIf=dojo.requireIf;
dojo.conditionalRequire=dojo.requireIf;
dojo.requireAll=function(){
for(var i=0;i<arguments.length;i++){
dojo.require(arguments[i]);
}
};
dojo.kwCompoundRequire=function(){
dojo.hostenv.conditionalLoadModule.apply(dojo.hostenv,arguments);
};
dojo.hostenv.provide=dojo.hostenv.startPackage;
dojo.provide=function(){
return dojo.hostenv.startPackage.apply(dojo.hostenv,arguments);
};
dojo.setModulePrefix=function(_91,_92){
return dojo.hostenv.setModulePrefix(_91,_92);
};
dojo.profile={start:function(){
},end:function(){
},stop:function(){
},dump:function(){
}};
dojo.exists=function(obj,_94){
var p=_94.split(".");
for(var i=0;i<p.length;i++){
if(!(obj[p[i]])){
return false;
}
obj=obj[p[i]];
}
return true;
};
dojo.provide("dojo.string.common");
dojo.require("dojo.string");
dojo.string.trim=function(str,wh){
if(!str.replace){
return str;
}
if(!str.length){
return str;
}
var re=(wh>0)?(/^\s+/):(wh<0)?(/\s+$/):(/^\s+|\s+$/g);
return str.replace(re,"");
};
dojo.string.trimStart=function(str){
return dojo.string.trim(str,1);
};
dojo.string.trimEnd=function(str){
return dojo.string.trim(str,-1);
};
dojo.string.repeat=function(str,_9d,_9e){
var out="";
for(var i=0;i<_9d;i++){
out+=str;
if(_9e&&i<_9d-1){
out+=_9e;
}
}
return out;
};
dojo.string.pad=function(str,len,c,dir){
var out=String(str);
if(!c){
c="0";
}
if(!dir){
dir=1;
}
while(out.length<len){
if(dir>0){
out=c+out;
}else{
out+=c;
}
}
return out;
};
dojo.string.padLeft=function(str,len,c){
return dojo.string.pad(str,len,c,1);
};
dojo.string.padRight=function(str,len,c){
return dojo.string.pad(str,len,c,-1);
};
dojo.provide("dojo.string");
dojo.require("dojo.string.common");
dojo.provide("dojo.lang.common");
dojo.require("dojo.lang");
dojo.lang.mixin=function(obj,_ad){
var _ae={};
for(var x in _ad){
if(typeof _ae[x]=="undefined"||_ae[x]!=_ad[x]){
obj[x]=_ad[x];
}
}
if(dojo.render.html.ie&&dojo.lang.isFunction(_ad["toString"])&&_ad["toString"]!=obj["toString"]){
obj.toString=_ad.toString;
}
return obj;
};
dojo.lang.extend=function(_b0,_b1){
this.mixin(_b0.prototype,_b1);
};
dojo.lang.find=function(arr,val,_b4,_b5){
if(!dojo.lang.isArrayLike(arr)&&dojo.lang.isArrayLike(val)){
var a=arr;
arr=val;
val=a;
}
var _b7=dojo.lang.isString(arr);
if(_b7){
arr=arr.split("");
}
if(_b5){
var _b8=-1;
var i=arr.length-1;
var end=-1;
}else{
var _b8=1;
var i=0;
var end=arr.length;
}
if(_b4){
while(i!=end){
if(arr[i]===val){
return i;
}
i+=_b8;
}
}else{
while(i!=end){
if(arr[i]==val){
return i;
}
i+=_b8;
}
}
return -1;
};
dojo.lang.indexOf=dojo.lang.find;
dojo.lang.findLast=function(arr,val,_bd){
return dojo.lang.find(arr,val,_bd,true);
};
dojo.lang.lastIndexOf=dojo.lang.findLast;
dojo.lang.inArray=function(arr,val){
return dojo.lang.find(arr,val)>-1;
};
dojo.lang.isObject=function(wh){
return typeof wh=="object"||dojo.lang.isArray(wh)||dojo.lang.isFunction(wh);
};
dojo.lang.isArray=function(wh){
return (wh instanceof Array||typeof wh=="array");
};
dojo.lang.isArrayLike=function(wh){
if(dojo.lang.isString(wh)){
return false;
}
if(dojo.lang.isFunction(wh)){
return false;
}
if(dojo.lang.isArray(wh)){
return true;
}
if(typeof wh!="undefined"&&wh&&dojo.lang.isNumber(wh.length)&&isFinite(wh.length)){
return true;
}
return false;
};
dojo.lang.isFunction=function(wh){
return (wh instanceof Function||typeof wh=="function");
};
dojo.lang.isString=function(wh){
return (wh instanceof String||typeof wh=="string");
};
dojo.lang.isAlien=function(wh){
return !dojo.lang.isFunction()&&/\{\s*\[native code\]\s*\}/.test(String(wh));
};
dojo.lang.isBoolean=function(wh){
return (wh instanceof Boolean||typeof wh=="boolean");
};
dojo.lang.isNumber=function(wh){
return (wh instanceof Number||typeof wh=="number");
};
dojo.lang.isUndefined=function(wh){
return ((wh==undefined)&&(typeof wh=="undefined"));
};
dojo.provide("dojo.lang.type");
dojo.require("dojo.lang.common");
dojo.lang.whatAmI=function(wh){
try{
if(dojo.lang.isArray(wh)){
return "array";
}
if(dojo.lang.isFunction(wh)){
return "function";
}
if(dojo.lang.isString(wh)){
return "string";
}
if(dojo.lang.isNumber(wh)){
return "number";
}
if(dojo.lang.isBoolean(wh)){
return "boolean";
}
if(dojo.lang.isAlien(wh)){
return "alien";
}
if(dojo.lang.isUndefined(wh)){
return "undefined";
}
for(var _ca in dojo.lang.whatAmI.custom){
if(dojo.lang.whatAmI.custom[_ca](wh)){
return _ca;
}
}
if(dojo.lang.isObject(wh)){
return "object";
}
}
catch(E){
}
return "unknown";
};
dojo.lang.whatAmI.custom={};
dojo.lang.isNumeric=function(wh){
return (!isNaN(wh)&&isFinite(wh)&&(wh!=null)&&!dojo.lang.isBoolean(wh)&&!dojo.lang.isArray(wh));
};
dojo.lang.isBuiltIn=function(wh){
return (dojo.lang.isArray(wh)||dojo.lang.isFunction(wh)||dojo.lang.isString(wh)||dojo.lang.isNumber(wh)||dojo.lang.isBoolean(wh)||(wh==null)||(wh instanceof Error)||(typeof wh=="error"));
};
dojo.lang.isPureObject=function(wh){
return ((wh!=null)&&dojo.lang.isObject(wh)&&wh.constructor==Object);
};
dojo.lang.isOfType=function(_ce,_cf){
if(dojo.lang.isArray(_cf)){
var _d0=_cf;
for(var i in _d0){
var _d2=_d0[i];
if(dojo.lang.isOfType(_ce,_d2)){
return true;
}
}
return false;
}else{
if(dojo.lang.isString(_cf)){
_cf=_cf.toLowerCase();
}
switch(_cf){
case Array:
case "array":
return dojo.lang.isArray(_ce);
break;
case Function:
case "function":
return dojo.lang.isFunction(_ce);
break;
case String:
case "string":
return dojo.lang.isString(_ce);
break;
case Number:
case "number":
return dojo.lang.isNumber(_ce);
break;
case "numeric":
return dojo.lang.isNumeric(_ce);
break;
case Boolean:
case "boolean":
return dojo.lang.isBoolean(_ce);
break;
case Object:
case "object":
return dojo.lang.isObject(_ce);
break;
case "pureobject":
return dojo.lang.isPureObject(_ce);
break;
case "builtin":
return dojo.lang.isBuiltIn(_ce);
break;
case "alien":
return dojo.lang.isAlien(_ce);
break;
case "undefined":
return dojo.lang.isUndefined(_ce);
break;
case null:
case "null":
return (_ce===null);
break;
case "optional":
return ((_ce===null)||dojo.lang.isUndefined(_ce));
break;
default:
if(dojo.lang.isFunction(_cf)){
return (_ce instanceof _cf);
}else{
dojo.raise("dojo.lang.isOfType() was passed an invalid type");
}
break;
}
}
dojo.raise("If we get here, it means a bug was introduced above.");
};
dojo.lang.getObject=function(str){
var _d4=str.split("."),i=0,obj=dj_global;
do{
obj=obj[_d4[i++]];
}while(i<_d4.length&&obj);
return (obj!=dj_global)?obj:null;
};
dojo.lang.doesObjectExist=function(str){
var _d6=str.split("."),i=0,obj=dj_global;
do{
obj=obj[_d6[i++]];
}while(i<_d6.length&&obj);
return (obj&&obj!=dj_global);
};
dojo.lang.getConstructor=function(obj){
return obj.constructor;
};
dojo.lang.isConstructedBy=function(obj,_d9){
return dojo.lang.getConstructor(obj)==_d9;
};
dojo.lang.isSubOf=function(obj,_db){
return obj instanceof _db;
};
dojo.lang.isBaseOf=function(_dc,obj){
return obj instanceof _dc;
};
dojo.lang.createInstance=function(_de){
var o=null;
var f=_de;
if(typeof (f)=="string"){
f=dojo.lang.getObject(_de);
}
if(typeof (f)=="function"){
try{
o=new f();
}
catch(e){
}
}
return o;
};
dojo.provide("dojo.lang.extras");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.type");
dojo.lang.setTimeout=function(_e1,_e2){
var _e3=window,argsStart=2;
if(!dojo.lang.isFunction(_e1)){
_e3=_e1;
_e1=_e2;
_e2=arguments[2];
argsStart++;
}
if(dojo.lang.isString(_e1)){
_e1=_e3[_e1];
}
var _e4=[];
for(var i=argsStart;i<arguments.length;i++){
_e4.push(arguments[i]);
}
return setTimeout(function(){
_e1.apply(_e3,_e4);
},_e2);
};
dojo.lang.getNameInObj=function(ns,_e7){
if(!ns){
ns=dj_global;
}
for(var x in ns){
if(ns[x]===_e7){
return new String(x);
}
}
return null;
};
dojo.lang.shallowCopy=function(obj){
var ret={},key;
for(key in obj){
if(dojo.lang.isUndefined(ret[key])){
ret[key]=obj[key];
}
}
return ret;
};
dojo.lang.firstValued=function(){
for(var i=0;i<arguments.length;i++){
if(typeof arguments[i]!="undefined"){
return arguments[i];
}
}
return undefined;
};
dojo.provide("dojo.io.IO");
dojo.require("dojo.string");
dojo.require("dojo.lang.extras");
dojo.io.transports=[];
dojo.io.hdlrFuncNames=["load","error","timeout"];
dojo.io.Request=function(url,_ed,_ee,_ef){
if((arguments.length==1)&&(arguments[0].constructor==Object)){
this.fromKwArgs(arguments[0]);
}else{
this.url=url;
if(_ed){
this.mimetype=_ed;
}
if(_ee){
this.transport=_ee;
}
if(arguments.length>=4){
this.changeUrl=_ef;
}
}
};
dojo.lang.extend(dojo.io.Request,{url:"",mimetype:"text/plain",method:"GET",content:undefined,transport:undefined,changeUrl:undefined,formNode:undefined,sync:false,bindSuccess:false,useCache:false,preventCache:false,load:function(_f0,_f1,evt){
},error:function(_f3,_f4){
},timeout:function(_f5){
},handle:function(){
},timeoutSeconds:0,abort:function(){
},fromKwArgs:function(_f6){
if(_f6["url"]){
_f6.url=_f6.url.toString();
}
if(_f6["formNode"]){
_f6.formNode=dojo.byId(_f6.formNode);
}
if(!_f6["method"]&&_f6["formNode"]&&_f6["formNode"].method){
_f6.method=_f6["formNode"].method;
}
if(!_f6["handle"]&&_f6["handler"]){
_f6.handle=_f6.handler;
}
if(!_f6["load"]&&_f6["loaded"]){
_f6.load=_f6.loaded;
}
if(!_f6["changeUrl"]&&_f6["changeURL"]){
_f6.changeUrl=_f6.changeURL;
}
_f6.encoding=dojo.lang.firstValued(_f6["encoding"],djConfig["bindEncoding"],"");
_f6.sendTransport=dojo.lang.firstValued(_f6["sendTransport"],djConfig["ioSendTransport"],false);
var _f7=dojo.lang.isFunction;
for(var x=0;x<dojo.io.hdlrFuncNames.length;x++){
var fn=dojo.io.hdlrFuncNames[x];
if(_f7(_f6[fn])){
continue;
}
if(_f7(_f6["handle"])){
_f6[fn]=_f6.handle;
}
}
dojo.lang.mixin(this,_f6);
}});
dojo.io.Error=function(msg,_fb,num){
this.message=msg;
this.type=_fb||"unknown";
this.number=num||0;
};
dojo.io.transports.addTransport=function(_fd){
this.push(_fd);
this[_fd]=dojo.io[_fd];
};
dojo.io.bind=function(_fe){
if(!(_fe instanceof dojo.io.Request)){
try{
_fe=new dojo.io.Request(_fe);
}
catch(e){
dojo.debug(e);
}
}
var _ff="";
if(_fe["transport"]){
_ff=_fe["transport"];
if(!this[_ff]){
return _fe;
}
}else{
for(var x=0;x<dojo.io.transports.length;x++){
var tmp=dojo.io.transports[x];
if((this[tmp])&&(this[tmp].canHandle(_fe))){
_ff=tmp;
}
}
if(_ff==""){
return _fe;
}
}
this[_ff].bind(_fe);
_fe.bindSuccess=true;
return _fe;
};
dojo.io.queueBind=function(_102){
if(!(_102 instanceof dojo.io.Request)){
try{
_102=new dojo.io.Request(_102);
}
catch(e){
dojo.debug(e);
}
}
var _103=_102.load;
_102.load=function(){
dojo.io._queueBindInFlight=false;
var ret=_103.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
var _105=_102.error;
_102.error=function(){
dojo.io._queueBindInFlight=false;
var ret=_105.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
dojo.io._bindQueue.push(_102);
dojo.io._dispatchNextQueueBind();
return _102;
};
dojo.io._dispatchNextQueueBind=function(){
if(!dojo.io._queueBindInFlight){
dojo.io._queueBindInFlight=true;
if(dojo.io._bindQueue.length>0){
dojo.io.bind(dojo.io._bindQueue.shift());
}else{
dojo.io._queueBindInFlight=false;
}
}
};
dojo.io._bindQueue=[];
dojo.io._queueBindInFlight=false;
dojo.io.argsFromMap=function(map,_108){
var _109=new Object();
var _10a="";
var enc=/utf/i.test(_108||"")?encodeURIComponent:dojo.string.encodeAscii;
for(var x in map){
if(!_109[x]){
_10a+=enc(x)+"="+enc(map[x])+"&";
}
}
return _10a;
};
dojo.io.setIFrameSrc=function(_10d,src,_10f){
try{
var r=dojo.render.html;
if(!_10f){
if(r.safari){
_10d.location=src;
}else{
frames[_10d.name].location=src;
}
}else{
var idoc;
if(r.ie){
idoc=_10d.contentWindow.document;
}else{
if(r.moz){
idoc=_10d.contentWindow;
}else{
if(r.safari){
idoc=_10d.document;
}
}
}
idoc.location.replace(src);
}
}
catch(e){
dojo.debug(e);
dojo.debug("setIFrameSrc: "+e);
}
};
dojo.provide("dojo.lang.array");
dojo.require("dojo.lang.common");
dojo.lang.has=function(obj,name){
return (typeof obj[name]!=="undefined");
};
dojo.lang.isEmpty=function(obj){
if(dojo.lang.isObject(obj)){
var tmp={};
var _116=0;
for(var x in obj){
if(obj[x]&&(!tmp[x])){
_116++;
break;
}
}
return (_116==0);
}else{
if(dojo.lang.isArrayLike(obj)||dojo.lang.isString(obj)){
return obj.length==0;
}
}
};
dojo.lang.forEach=function(arr,_119,_11a){
var _11b=dojo.lang.isString(arr);
if(_11b){
arr=arr.split("");
}
var il=arr.length;
for(var i=0;i<((_11a)?il:arr.length);i++){
if(_119(arr[i],i,arr)=="break"){
break;
}
}
};
dojo.lang.map=function(arr,obj,_120){
var _121=dojo.lang.isString(arr);
if(_121){
arr=arr.split("");
}
if(dojo.lang.isFunction(obj)&&(!_120)){
_120=obj;
obj=dj_global;
}else{
if(dojo.lang.isFunction(obj)&&_120){
var _122=obj;
obj=_120;
_120=_122;
}
}
if(Array.map){
var _123=Array.map(arr,_120,obj);
}else{
var _123=[];
for(var i=0;i<arr.length;++i){
_123.push(_120.call(obj,arr[i]));
}
}
if(_121){
return _123.join("");
}else{
return _123;
}
};
dojo.lang.every=function(arr,_126,_127){
var _128=dojo.lang.isString(arr);
if(_128){
arr=arr.split("");
}
if(Array.every){
return Array.every(arr,_126,_127);
}else{
if(!_127){
if(arguments.length>=3){
dojo.raise("thisObject doesn't exist!");
}
_127=dj_global;
}
for(var i=0;i<arr.length;i++){
if(!_126.call(_127,arr[i],i,arr)){
return false;
}
}
return true;
}
};
dojo.lang.some=function(arr,_12b,_12c){
var _12d=dojo.lang.isString(arr);
if(_12d){
arr=arr.split("");
}
if(Array.some){
return Array.some(arr,_12b,_12c);
}else{
if(!_12c){
if(arguments.length>=3){
dojo.raise("thisObject doesn't exist!");
}
_12c=dj_global;
}
for(var i=0;i<arr.length;i++){
if(_12b.call(_12c,arr[i],i,arr)){
return true;
}
}
return false;
}
};
dojo.lang.filter=function(arr,_130,_131){
var _132=dojo.lang.isString(arr);
if(_132){
arr=arr.split("");
}
if(Array.filter){
var _133=Array.filter(arr,_130,_131);
}else{
if(!_131){
if(arguments.length>=3){
dojo.raise("thisObject doesn't exist!");
}
_131=dj_global;
}
var _133=[];
for(var i=0;i<arr.length;i++){
if(_130.call(_131,arr[i],i,arr)){
_133.push(arr[i]);
}
}
}
if(_132){
return _133.join("");
}else{
return _133;
}
};
dojo.lang.unnest=function(){
var out=[];
for(var i=0;i<arguments.length;i++){
if(dojo.lang.isArrayLike(arguments[i])){
var add=dojo.lang.unnest.apply(this,arguments[i]);
out=out.concat(add);
}else{
out.push(arguments[i]);
}
}
return out;
};
dojo.lang.toArray=function(_138,_139){
var _13a=[];
for(var i=_139||0;i<_138.length;i++){
_13a.push(_138[i]);
}
return _13a;
};
dojo.provide("dojo.lang.func");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.type");
dojo.lang.hitch=function(_13c,_13d){
if(dojo.lang.isString(_13d)){
var fcn=_13c[_13d];
}else{
var fcn=_13d;
}
return function(){
return fcn.apply(_13c,arguments);
};
};
dojo.lang.anonCtr=0;
dojo.lang.anon={};
dojo.lang.nameAnonFunc=function(_13f,_140){
var nso=(_140||dojo.lang.anon);
if((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true)){
for(var x in nso){
if(nso[x]===_13f){
return x;
}
}
}
var ret="__"+dojo.lang.anonCtr++;
while(typeof nso[ret]!="undefined"){
ret="__"+dojo.lang.anonCtr++;
}
nso[ret]=_13f;
return ret;
};
dojo.lang.forward=function(_144){
return function(){
return this[_144].apply(this,arguments);
};
};
dojo.lang.curry=function(ns,func){
var _147=[];
ns=ns||dj_global;
if(dojo.lang.isString(func)){
func=ns[func];
}
for(var x=2;x<arguments.length;x++){
_147.push(arguments[x]);
}
var _149=func.length-_147.length;
function gather(_14a,_14b,_14c){
var _14d=_14c;
var _14e=_14b.slice(0);
for(var x=0;x<_14a.length;x++){
_14e.push(_14a[x]);
}
_14c=_14c-_14a.length;
if(_14c<=0){
var res=func.apply(ns,_14e);
_14c=_14d;
return res;
}else{
return function(){
return gather(arguments,_14e,_14c);
};
}
}
return gather([],_147,_149);
};
dojo.lang.curryArguments=function(ns,func,args,_154){
var _155=[];
var x=_154||0;
for(x=_154;x<args.length;x++){
_155.push(args[x]);
}
return dojo.lang.curry.apply(dojo.lang,[ns,func].concat(_155));
};
dojo.lang.tryThese=function(){
for(var x=0;x<arguments.length;x++){
try{
if(typeof arguments[x]=="function"){
var ret=(arguments[x]());
if(ret){
return ret;
}
}
}
catch(e){
dojo.debug(e);
}
}
};
dojo.lang.delayThese=function(farr,cb,_15b,_15c){
if(!farr.length){
if(typeof _15c=="function"){
_15c();
}
return;
}
if((typeof _15b=="undefined")&&(typeof cb=="number")){
_15b=cb;
cb=function(){
};
}else{
if(!cb){
cb=function(){
};
if(!_15b){
_15b=0;
}
}
}
setTimeout(function(){
(farr.shift())();
cb();
dojo.lang.delayThese(farr,cb,_15b,_15c);
},_15b);
};
dojo.provide("dojo.string.extras");
dojo.require("dojo.string.common");
dojo.require("dojo.lang");
dojo.string.paramString=function(str,_15e,_15f){
for(var name in _15e){
var re=new RegExp("\\%\\{"+name+"\\}","g");
str=str.replace(re,_15e[name]);
}
if(_15f){
str=str.replace(/%\{([^\}\s]+)\}/g,"");
}
return str;
};
dojo.string.capitalize=function(str){
if(!dojo.lang.isString(str)){
return "";
}
if(arguments.length==0){
str=this;
}
var _163=str.split(" ");
var _164="";
var len=_163.length;
for(var i=0;i<len;i++){
var word=_163[i];
word=word.charAt(0).toUpperCase()+word.substring(1,word.length);
_164+=word;
if(i<len-1){
_164+=" ";
}
}
return new String(_164);
};
dojo.string.isBlank=function(str){
if(!dojo.lang.isString(str)){
return true;
}
return (dojo.string.trim(str).length==0);
};
dojo.string.encodeAscii=function(str){
if(!dojo.lang.isString(str)){
return str;
}
var ret="";
var _16b=escape(str);
var _16c,re=/%u([0-9A-F]{4})/i;
while((_16c=_16b.match(re))){
var num=Number("0x"+_16c[1]);
var _16e=escape("&#"+num+";");
ret+=_16b.substring(0,_16c.index)+_16e;
_16b=_16b.substring(_16c.index+_16c[0].length);
}
ret+=_16b.replace(/\+/g,"%2B");
return ret;
};
dojo.string.escape=function(type,str){
var args=[];
for(var i=1;i<arguments.length;i++){
args.push(arguments[i]);
}
switch(type.toLowerCase()){
case "xml":
case "html":
case "xhtml":
return dojo.string.escapeXml.apply(this,args);
case "sql":
return dojo.string.escapeSql.apply(this,args);
case "regexp":
case "regex":
return dojo.string.escapeRegExp.apply(this,args);
case "javascript":
case "jscript":
case "js":
return dojo.string.escapeJavaScript.apply(this,args);
case "ascii":
return dojo.string.encodeAscii.apply(this,args);
default:
return str;
}
};
dojo.string.escapeXml=function(str,_174){
str=str.replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");
if(!_174){
str=str.replace(/'/gm,"&#39;");
}
return str;
};
dojo.string.escapeSql=function(str){
return str.replace(/'/gm,"''");
};
dojo.string.escapeRegExp=function(str){
return str.replace(/\\/gm,"\\\\").replace(/([\f\b\n\t\r])/gm,"\\$1");
};
dojo.string.escapeJavaScript=function(str){
return str.replace(/(["'\f\b\n\t\r])/gm,"\\$1");
};
dojo.string.summary=function(str,len){
if(!len||str.length<=len){
return str;
}else{
return str.substring(0,len).replace(/\.+$/,"")+"...";
}
};
dojo.string.endsWith=function(str,end,_17c){
if(_17c){
str=str.toLowerCase();
end=end.toLowerCase();
}
return str.lastIndexOf(end)==str.length-end.length;
};
dojo.string.endsWithAny=function(str){
for(var i=1;i<arguments.length;i++){
if(dojo.string.endsWith(str,arguments[i])){
return true;
}
}
return false;
};
dojo.string.startsWith=function(str,_180,_181){
if(_181){
str=str.toLowerCase();
_180=_180.toLowerCase();
}
return str.indexOf(_180)==0;
};
dojo.string.startsWithAny=function(str){
for(var i=1;i<arguments.length;i++){
if(dojo.string.startsWith(str,arguments[i])){
return true;
}
}
return false;
};
dojo.string.has=function(str){
for(var i=1;i<arguments.length;i++){
if(str.indexOf(arguments[i])>-1){
return true;
}
}
return false;
};
dojo.string.normalizeNewlines=function(text,_187){
if(_187=="\n"){
text=text.replace(/\r\n/g,"\n");
text=text.replace(/\r/g,"\n");
}else{
if(_187=="\r"){
text=text.replace(/\r\n/g,"\r");
text=text.replace(/\n/g,"\r");
}else{
text=text.replace(/([^\r])\n/g,"$1\r\n");
text=text.replace(/\r([^\n])/g,"\r\n$1");
}
}
return text;
};
dojo.string.splitEscaped=function(str,_189){
var _18a=[];
for(var i=0,prevcomma=0;i<str.length;i++){
if(str.charAt(i)=="\\"){
i++;
continue;
}
if(str.charAt(i)==_189){
_18a.push(str.substring(prevcomma,i));
prevcomma=i+1;
}
}
_18a.push(str.substr(prevcomma));
return _18a;
};
dojo.provide("dojo.dom");
dojo.require("dojo.lang.array");
dojo.dom.ELEMENT_NODE=1;
dojo.dom.ATTRIBUTE_NODE=2;
dojo.dom.TEXT_NODE=3;
dojo.dom.CDATA_SECTION_NODE=4;
dojo.dom.ENTITY_REFERENCE_NODE=5;
dojo.dom.ENTITY_NODE=6;
dojo.dom.PROCESSING_INSTRUCTION_NODE=7;
dojo.dom.COMMENT_NODE=8;
dojo.dom.DOCUMENT_NODE=9;
dojo.dom.DOCUMENT_TYPE_NODE=10;
dojo.dom.DOCUMENT_FRAGMENT_NODE=11;
dojo.dom.NOTATION_NODE=12;
dojo.dom.dojoml="http://www.dojotoolkit.org/2004/dojoml";
dojo.dom.xmlns={svg:"http://www.w3.org/2000/svg",smil:"http://www.w3.org/2001/SMIL20/",mml:"http://www.w3.org/1998/Math/MathML",cml:"http://www.xml-cml.org",xlink:"http://www.w3.org/1999/xlink",xhtml:"http://www.w3.org/1999/xhtml",xul:"http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul",xbl:"http://www.mozilla.org/xbl",fo:"http://www.w3.org/1999/XSL/Format",xsl:"http://www.w3.org/1999/XSL/Transform",xslt:"http://www.w3.org/1999/XSL/Transform",xi:"http://www.w3.org/2001/XInclude",xforms:"http://www.w3.org/2002/01/xforms",saxon:"http://icl.com/saxon",xalan:"http://xml.apache.org/xslt",xsd:"http://www.w3.org/2001/XMLSchema",dt:"http://www.w3.org/2001/XMLSchema-datatypes",xsi:"http://www.w3.org/2001/XMLSchema-instance",rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#",rdfs:"http://www.w3.org/2000/01/rdf-schema#",dc:"http://purl.org/dc/elements/1.1/",dcq:"http://purl.org/dc/qualifiers/1.0","soap-env":"http://schemas.xmlsoap.org/soap/envelope/",wsdl:"http://schemas.xmlsoap.org/wsdl/",AdobeExtensions:"http://ns.adobe.com/AdobeSVGViewerExtensions/3.0/"};
dojo.dom.isNode=function(wh){
if(typeof Element=="object"){
try{
return wh instanceof Element;
}
catch(E){
}
}else{
return wh&&!isNaN(wh.nodeType);
}
};
dojo.dom.getTagName=function(node){
var _18e=node.tagName;
if(_18e.substr(0,5).toLowerCase()!="dojo:"){
if(_18e.substr(0,4).toLowerCase()=="dojo"){
return "dojo:"+_18e.substring(4).toLowerCase();
}
var djt=node.getAttribute("dojoType")||node.getAttribute("dojotype");
if(djt){
return "dojo:"+djt.toLowerCase();
}
if((node.getAttributeNS)&&(node.getAttributeNS(this.dojoml,"type"))){
return "dojo:"+node.getAttributeNS(this.dojoml,"type").toLowerCase();
}
try{
djt=node.getAttribute("dojo:type");
}
catch(e){
}
if(djt){
return "dojo:"+djt.toLowerCase();
}
if((!dj_global["djConfig"])||(!djConfig["ignoreClassNames"])){
var _190=node.className||node.getAttribute("class");
if((_190)&&(_190.indexOf)&&(_190.indexOf("dojo-")!=-1)){
var _191=_190.split(" ");
for(var x=0;x<_191.length;x++){
if((_191[x].length>5)&&(_191[x].indexOf("dojo-")>=0)){
return "dojo:"+_191[x].substr(5).toLowerCase();
}
}
}
}
}
return _18e.toLowerCase();
};
dojo.dom.getUniqueId=function(){
do{
var id="dj_unique_"+(++arguments.callee._idIncrement);
}while(document.getElementById(id));
return id;
};
dojo.dom.getUniqueId._idIncrement=0;
dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_194,_195){
var node=_194.firstChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.nextSibling;
}
if(_195&&node&&node.tagName&&node.tagName.toLowerCase()!=_195.toLowerCase()){
node=dojo.dom.nextElement(node,_195);
}
return node;
};
dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_197,_198){
var node=_197.lastChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.previousSibling;
}
if(_198&&node&&node.tagName&&node.tagName.toLowerCase()!=_198.toLowerCase()){
node=dojo.dom.prevElement(node,_198);
}
return node;
};
dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(node,_19b){
if(!node){
return null;
}
do{
node=node.nextSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_19b&&_19b.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.nextElement(node,_19b);
}
return node;
};
dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(node,_19d){
if(!node){
return null;
}
if(_19d){
_19d=_19d.toLowerCase();
}
do{
node=node.previousSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_19d&&_19d.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.prevElement(node,_19d);
}
return node;
};
dojo.dom.moveChildren=function(_19e,_19f,trim){
var _1a1=0;
if(trim){
while(_19e.hasChildNodes()&&_19e.firstChild.nodeType==dojo.dom.TEXT_NODE){
_19e.removeChild(_19e.firstChild);
}
while(_19e.hasChildNodes()&&_19e.lastChild.nodeType==dojo.dom.TEXT_NODE){
_19e.removeChild(_19e.lastChild);
}
}
while(_19e.hasChildNodes()){
_19f.appendChild(_19e.firstChild);
_1a1++;
}
return _1a1;
};
dojo.dom.copyChildren=function(_1a2,_1a3,trim){
var _1a5=_1a2.cloneNode(true);
return this.moveChildren(_1a5,_1a3,trim);
};
dojo.dom.removeChildren=function(node){
var _1a7=node.childNodes.length;
while(node.hasChildNodes()){
node.removeChild(node.firstChild);
}
return _1a7;
};
dojo.dom.replaceChildren=function(node,_1a9){
dojo.dom.removeChildren(node);
node.appendChild(_1a9);
};
dojo.dom.removeNode=function(node){
if(node&&node.parentNode){
return node.parentNode.removeChild(node);
}
};
dojo.dom.getAncestors=function(node,_1ac,_1ad){
var _1ae=[];
var _1af=dojo.lang.isFunction(_1ac);
while(node){
if(!_1af||_1ac(node)){
_1ae.push(node);
}
if(_1ad&&_1ae.length>0){
return _1ae[0];
}
node=node.parentNode;
}
if(_1ad){
return null;
}
return _1ae;
};
dojo.dom.getAncestorsByTag=function(node,tag,_1b2){
tag=tag.toLowerCase();
return dojo.dom.getAncestors(node,function(el){
return ((el.tagName)&&(el.tagName.toLowerCase()==tag));
},_1b2);
};
dojo.dom.getFirstAncestorByTag=function(node,tag){
return dojo.dom.getAncestorsByTag(node,tag,true);
};
dojo.dom.isDescendantOf=function(node,_1b7,_1b8){
if(_1b8&&node){
node=node.parentNode;
}
while(node){
if(node==_1b7){
return true;
}
node=node.parentNode;
}
return false;
};
dojo.dom.innerXML=function(node){
if(node.innerXML){
return node.innerXML;
}else{
if(typeof XMLSerializer!="undefined"){
return (new XMLSerializer()).serializeToString(node);
}
}
};
dojo.dom.createDocumentFromText=function(str,_1bb){
if(!_1bb){
_1bb="text/xml";
}
if(typeof DOMParser!="undefined"){
var _1bc=new DOMParser();
return _1bc.parseFromString(str,_1bb);
}else{
if(typeof ActiveXObject!="undefined"){
var _1bd=new ActiveXObject("Microsoft.XMLDOM");
if(_1bd){
_1bd.async=false;
_1bd.loadXML(str);
return _1bd;
}else{
dojo.debug("toXml didn't work?");
}
}else{
if(document.createElement){
var tmp=document.createElement("xml");
tmp.innerHTML=str;
if(document.implementation&&document.implementation.createDocument){
var _1bf=document.implementation.createDocument("foo","",null);
for(var i=0;i<tmp.childNodes.length;i++){
_1bf.importNode(tmp.childNodes.item(i),true);
}
return _1bf;
}
return tmp.document&&tmp.document.firstChild?tmp.document.firstChild:tmp;
}
}
}
return null;
};
dojo.dom.prependChild=function(node,_1c2){
if(_1c2.firstChild){
_1c2.insertBefore(node,_1c2.firstChild);
}else{
_1c2.appendChild(node);
}
return true;
};
dojo.dom.insertBefore=function(node,ref,_1c5){
if(_1c5!=true&&(node===ref||node.nextSibling===ref)){
return false;
}
var _1c6=ref.parentNode;
_1c6.insertBefore(node,ref);
return true;
};
dojo.dom.insertAfter=function(node,ref,_1c9){
var pn=ref.parentNode;
if(ref==pn.lastChild){
if((_1c9!=true)&&(node===ref)){
return false;
}
pn.appendChild(node);
}else{
return this.insertBefore(node,ref.nextSibling,_1c9);
}
return true;
};
dojo.dom.insertAtPosition=function(node,ref,_1cd){
if((!node)||(!ref)||(!_1cd)){
return false;
}
switch(_1cd.toLowerCase()){
case "before":
return dojo.dom.insertBefore(node,ref);
case "after":
return dojo.dom.insertAfter(node,ref);
case "first":
if(ref.firstChild){
return dojo.dom.insertBefore(node,ref.firstChild);
}else{
ref.appendChild(node);
return true;
}
break;
default:
ref.appendChild(node);
return true;
}
};
dojo.dom.insertAtIndex=function(node,_1cf,_1d0){
var _1d1=_1cf.childNodes;
if(!_1d1.length){
_1cf.appendChild(node);
return true;
}
var _1d2=null;
for(var i=0;i<_1d1.length;i++){
var _1d4=_1d1.item(i)["getAttribute"]?parseInt(_1d1.item(i).getAttribute("dojoinsertionindex")):-1;
if(_1d4<_1d0){
_1d2=_1d1.item(i);
}
}
if(_1d2){
return dojo.dom.insertAfter(node,_1d2);
}else{
return dojo.dom.insertBefore(node,_1d1.item(0));
}
};
dojo.dom.textContent=function(node,text){
if(text){
dojo.dom.replaceChildren(node,document.createTextNode(text));
return text;
}else{
var _1d7="";
if(node==null){
return _1d7;
}
for(var i=0;i<node.childNodes.length;i++){
switch(node.childNodes[i].nodeType){
case 1:
case 5:
_1d7+=dojo.dom.textContent(node.childNodes[i]);
break;
case 3:
case 2:
case 4:
_1d7+=node.childNodes[i].nodeValue;
break;
default:
break;
}
}
return _1d7;
}
};
dojo.dom.collectionToArray=function(_1d9){
dojo.deprecated("dojo.dom.collectionToArray","use dojo.lang.toArray instead");
return dojo.lang.toArray(_1d9);
};
dojo.dom.hasParent=function(node){
if(!node||!node.parentNode||(node.parentNode&&!node.parentNode.tagName)){
return false;
}
return true;
};
dojo.dom.isTag=function(node){
if(node&&node.tagName){
var arr=dojo.lang.toArray(arguments,1);
return arr[dojo.lang.find(node.tagName,arr)]||"";
}
return "";
};
dojo.provide("dojo.undo.browser");
dojo.require("dojo.io");
try{
if((!djConfig["preventBackButtonFix"])&&(!dojo.hostenv.post_load_)){
document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"'></iframe>");
}
}
catch(e){
}
dojo.undo.browser={initialHref:window.location.href,initialHash:window.location.hash,moveForward:false,historyStack:[],forwardStack:[],historyIframe:null,bookmarkAnchor:null,locationTimer:null,addToHistory:function(args){
var hash=null;
if(!this.historyIframe){
this.historyIframe=window.frames["djhistory"];
}
if(!this.bookmarkAnchor){
this.bookmarkAnchor=document.createElement("a");
(document.body||document.getElementsByTagName("body")[0]).appendChild(this.bookmarkAnchor);
this.bookmarkAnchor.style.display="none";
}
if((!args["changeUrl"])||(dojo.render.html.ie)){
var url=dojo.hostenv.getBaseScriptUri()+"iframe_history.html?"+(new Date()).getTime();
this.moveForward=true;
dojo.io.setIFrameSrc(this.historyIframe,url,false);
}
if(args["changeUrl"]){
hash="#"+((args["changeUrl"]!==true)?args["changeUrl"]:(new Date()).getTime());
setTimeout("window.location.href = '"+hash+"';",1);
this.bookmarkAnchor.href=hash;
if(dojo.render.html.ie){
var _1e0=args["back"]||args["backButton"]||args["handle"];
var tcb=function(_1e2){
if(window.location.hash!=""){
setTimeout("window.location.href = '"+hash+"';",1);
}
_1e0.apply(this,[_1e2]);
};
if(args["back"]){
args.back=tcb;
}else{
if(args["backButton"]){
args.backButton=tcb;
}else{
if(args["handle"]){
args.handle=tcb;
}
}
}
this.forwardStack=[];
var _1e3=args["forward"]||args["forwardButton"]||args["handle"];
var tfw=function(_1e5){
if(window.location.hash!=""){
window.location.href=hash;
}
if(_1e3){
_1e3.apply(this,[_1e5]);
}
};
if(args["forward"]){
args.forward=tfw;
}else{
if(args["forwardButton"]){
args.forwardButton=tfw;
}else{
if(args["handle"]){
args.handle=tfw;
}
}
}
}else{
if(dojo.render.html.moz){
if(!this.locationTimer){
this.locationTimer=setInterval("dojo.undo.browser.checkLocation();",200);
}
}
}
}
this.historyStack.push({"url":url,"kwArgs":args,"urlHash":hash});
},checkLocation:function(){
var hsl=this.historyStack.length;
if((window.location.hash==this.initialHash)||(window.location.href==this.initialHref)&&(hsl==1)){
this.handleBackButton();
return;
}
if(this.forwardStack.length>0){
if(this.forwardStack[this.forwardStack.length-1].urlHash==window.location.hash){
this.handleForwardButton();
return;
}
}
if((hsl>=2)&&(this.historyStack[hsl-2])){
if(this.historyStack[hsl-2].urlHash==window.location.hash){
this.handleBackButton();
return;
}
}
},iframeLoaded:function(evt,_1e8){
var _1e9=this._getUrlQuery(_1e8.href);
if(_1e9==null){
if(this.historyStack.length==1){
this.handleBackButton();
}
return;
}
if(this.moveForward){
this.moveForward=false;
return;
}
if(this.historyStack.length>=2&&_1e9==this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){
this.handleBackButton();
}else{
if(this.forwardStack.length>0&&_1e9==this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){
this.handleForwardButton();
}
}
},handleBackButton:function(){
var _1ea=this.historyStack.pop();
var last=this.historyStack[this.historyStack.length-1];
if(!_1ea){
return;
}
if(last){
if(last.kwArgs["back"]){
last.kwArgs["back"]();
}else{
if(last.kwArgs["backButton"]){
last.kwArgs["backButton"]();
}else{
if(last.kwArgs["handle"]){
last.kwArgs.handle("back");
}
}
}
}
this.forwardStack.push(_1ea);
},handleForwardButton:function(){
var last=this.forwardStack.pop();
if(!last){
return;
}
if(last.kwArgs["forward"]){
last.kwArgs.forward();
}else{
if(last.kwArgs["forwardButton"]){
last.kwArgs.forwardButton();
}else{
if(last.kwArgs["handle"]){
last.kwArgs.handle("forward");
}
}
}
this.historyStack.push(last);
},_getUrlQuery:function(url){
var _1ee=url.split("?");
if(_1ee.length<2){
return null;
}else{
return _1ee[1];
}
}};
dojo.provide("dojo.io.BrowserIO");
dojo.require("dojo.io");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.func");
dojo.require("dojo.string.extras");
dojo.require("dojo.dom");
dojo.require("dojo.undo.browser");
dojo.io.checkChildrenForFile=function(node){
var _1f0=false;
var _1f1=node.getElementsByTagName("input");
dojo.lang.forEach(_1f1,function(_1f2){
if(_1f0){
return;
}
if(_1f2.getAttribute("type")=="file"){
_1f0=true;
}
});
return _1f0;
};
dojo.io.formHasFile=function(_1f3){
return dojo.io.checkChildrenForFile(_1f3);
};
dojo.io.formFilter=function(node){
var type=(node.type||"").toLowerCase();
return !node.disabled&&node.name&&!dojo.lang.inArray(type,["file","submit","image","reset","button"]);
};
dojo.io.encodeForm=function(_1f6,_1f7,_1f8){
if((!_1f6)||(!_1f6.tagName)||(!_1f6.tagName.toLowerCase()=="form")){
dojo.raise("Attempted to encode a non-form element.");
}
if(!_1f8){
_1f8=dojo.io.formFilter;
}
var enc=/utf/i.test(_1f7||"")?encodeURIComponent:dojo.string.encodeAscii;
var _1fa=[];
for(var i=0;i<_1f6.elements.length;i++){
var elm=_1f6.elements[i];
if(!elm||elm.tagName.toLowerCase()=="fieldset"||!_1f8(elm)){
continue;
}
var name=enc(elm.name);
var type=elm.type.toLowerCase();
if(type=="select-multiple"){
for(var j=0;j<elm.options.length;j++){
if(elm.options[j].selected){
_1fa.push(name+"="+enc(elm.options[j].value));
}
}
}else{
if(dojo.lang.inArray(type,["radio","checkbox"])){
if(elm.checked){
_1fa.push(name+"="+enc(elm.value));
}
}else{
_1fa.push(name+"="+enc(elm.value));
}
}
}
var _200=_1f6.getElementsByTagName("input");
for(var i=0;i<_200.length;i++){
var _201=_200[i];
if(_201.type.toLowerCase()=="image"&&_201.form==_1f6&&_1f8(_201)){
var name=enc(_201.name);
_1fa.push(name+"="+enc(_201.value));
_1fa.push(name+".x=0");
_1fa.push(name+".y=0");
}
}
return _1fa.join("&")+"&";
};
dojo.io.FormBind=function(args){
this.bindArgs={};
if(args&&args.formNode){
this.init(args);
}else{
if(args){
this.init({formNode:args});
}
}
};
dojo.lang.extend(dojo.io.FormBind,{form:null,bindArgs:null,clickedButton:null,init:function(args){
var form=dojo.byId(args.formNode);
if(!form||!form.tagName||form.tagName.toLowerCase()!="form"){
throw new Error("FormBind: Couldn't apply, invalid form");
}else{
if(this.form==form){
return;
}else{
if(this.form){
throw new Error("FormBind: Already applied to a form");
}
}
}
dojo.lang.mixin(this.bindArgs,args);
this.form=form;
this.connect(form,"onsubmit","submit");
for(var i=0;i<form.elements.length;i++){
var node=form.elements[i];
if(node&&node.type&&dojo.lang.inArray(node.type.toLowerCase(),["submit","button"])){
this.connect(node,"onclick","click");
}
}
var _207=form.getElementsByTagName("input");
for(var i=0;i<_207.length;i++){
var _208=_207[i];
if(_208.type.toLowerCase()=="image"&&_208.form==form){
this.connect(_208,"onclick","click");
}
}
},onSubmit:function(form){
return true;
},submit:function(e){
e.preventDefault();
if(this.onSubmit(this.form)){
dojo.io.bind(dojo.lang.mixin(this.bindArgs,{formFilter:dojo.lang.hitch(this,"formFilter")}));
}
},click:function(e){
var node=e.currentTarget;
if(node.disabled){
return;
}
this.clickedButton=node;
},formFilter:function(node){
var type=(node.type||"").toLowerCase();
var _20f=false;
if(node.disabled||!node.name){
_20f=false;
}else{
if(dojo.lang.inArray(type,["submit","button","image"])){
if(!this.clickedButton){
this.clickedButton=node;
}
_20f=node==this.clickedButton;
}else{
_20f=!dojo.lang.inArray(type,["file","submit","reset","button"]);
}
}
return _20f;
},connect:function(_210,_211,_212){
if(dojo.evalObjPath("dojo.event.connect")){
dojo.event.connect(_210,_211,this,_212);
}else{
var fcn=dojo.lang.hitch(this,_212);
_210[_211]=function(e){
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
var _215=this;
var _216={};
this.useCache=false;
this.preventCache=false;
function getCacheKey(url,_218,_219){
return url+"|"+_218+"|"+_219.toLowerCase();
}
function addToCache(url,_21b,_21c,http){
_216[getCacheKey(url,_21b,_21c)]=http;
}
function getFromCache(url,_21f,_220){
return _216[getCacheKey(url,_21f,_220)];
}
this.clearCache=function(){
_216={};
};
function doLoad(_221,http,url,_224,_225){
if((http.status==200)||(http.status==304)||(location.protocol=="file:"&&(http.status==0||http.status==undefined))||(location.protocol=="chrome:"&&(http.status==0||http.status==undefined))){
var ret;
if(_221.method.toLowerCase()=="head"){
var _227=http.getAllResponseHeaders();
ret={};
ret.toString=function(){
return _227;
};
var _228=_227.split(/[\r\n]+/g);
for(var i=0;i<_228.length;i++){
var pair=_228[i].match(/^([^:]+)\s*:\s*(.+)$/i);
if(pair){
ret[pair[1]]=pair[2];
}
}
}else{
if(_221.mimetype=="text/javascript"){
try{
ret=dj_eval(http.responseText);
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=null;
}
}else{
if(_221.mimetype=="text/json"){
try{
ret=dj_eval("("+http.responseText+")");
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=false;
}
}else{
if((_221.mimetype=="application/xml")||(_221.mimetype=="text/xml")){
ret=http.responseXML;
if(!ret||typeof ret=="string"){
ret=dojo.dom.createDocumentFromText(http.responseText);
}
}else{
ret=http.responseText;
}
}
}
}
if(_225){
addToCache(url,_224,_221.method,http);
}
_221[(typeof _221.load=="function")?"load":"handle"]("load",ret,http);
}else{
var _22b=new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);
_221[(typeof _221.error=="function")?"error":"handle"]("error",_22b,http);
}
}
function setHeaders(http,_22d){
if(_22d["headers"]){
for(var _22e in _22d["headers"]){
if(_22e.toLowerCase()=="content-type"&&!_22d["contentType"]){
_22d["contentType"]=_22d["headers"][_22e];
}else{
http.setRequestHeader(_22e,_22d["headers"][_22e]);
}
}
}
}
this.inFlight=[];
this.inFlightTimer=null;
this.startWatchingInFlight=function(){
if(!this.inFlightTimer){
this.inFlightTimer=setInterval("dojo.io.XMLHTTPTransport.watchInFlight();",10);
}
};
this.watchInFlight=function(){
for(var x=this.inFlight.length-1;x>=0;x--){
var tif=this.inFlight[x];
if(!tif){
this.inFlight.splice(x,1);
continue;
}
if(4==tif.http.readyState){
this.inFlight.splice(x,1);
doLoad(tif.req,tif.http,tif.url,tif.query,tif.useCache);
if(this.inFlight.length==0){
clearInterval(this.inFlightTimer);
this.inFlightTimer=null;
}
}
}
};
var _231=dojo.hostenv.getXmlhttpObject()?true:false;
this.canHandle=function(_232){
return _231&&dojo.lang.inArray((_232["mimetype"].toLowerCase()||""),["text/plain","text/html","application/xml","text/xml","text/javascript","text/json"])&&dojo.lang.inArray(_232["method"].toLowerCase(),["post","get","head"])&&!(_232["formNode"]&&dojo.io.formHasFile(_232["formNode"]));
};
this.multipartBoundary="45309FFF-BD65-4d50-99C9-36986896A96F";
this.bind=function(_233){
if(!_233["url"]){
if(!_233["formNode"]&&(_233["backButton"]||_233["back"]||_233["changeUrl"]||_233["watchForURL"])&&(!djConfig.preventBackButtonFix)){
dj_deprecated("Using dojo.io.XMLHTTPTransport.bind() to add to browser history without doing an IO request is deprecated. Use dojo.undo.browser.addToHistory() instead.");
dojo.undo.browser.addToHistory(_233);
return true;
}
}
var url=_233.url;
var _235="";
if(_233["formNode"]){
var ta=_233.formNode.getAttribute("action");
if((ta)&&(!_233["url"])){
url=ta;
}
var tp=_233.formNode.getAttribute("method");
if((tp)&&(!_233["method"])){
_233.method=tp;
}
_235+=dojo.io.encodeForm(_233.formNode,_233.encoding,_233["formFilter"]);
}
if(url.indexOf("#")>-1){
dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);
url=url.split("#")[0];
}
if(_233["file"]){
_233.method="post";
}
if(!_233["method"]){
_233.method="get";
}
if(_233.method.toLowerCase()=="get"){
_233.multipart=false;
}else{
if(_233["file"]){
_233.multipart=true;
}else{
if(!_233["multipart"]){
_233.multipart=false;
}
}
}
if(_233["backButton"]||_233["back"]||_233["changeUrl"]){
dojo.undo.browser.addToHistory(_233);
}
var _238=_233["content"]||{};
if(_233.sendTransport){
_238["dojo.transport"]="xmlhttp";
}
do{
if(_233.postContent){
_235=_233.postContent;
break;
}
if(_238){
_235+=dojo.io.argsFromMap(_238,_233.encoding);
}
if(_233.method.toLowerCase()=="get"||!_233.multipart){
break;
}
var t=[];
if(_235.length){
var q=_235.split("&");
for(var i=0;i<q.length;++i){
if(q[i].length){
var p=q[i].split("=");
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+p[0]+"\"","",p[1]);
}
}
}
if(_233.file){
if(dojo.lang.isArray(_233.file)){
for(var i=0;i<_233.file.length;++i){
var o=_233.file[i];
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}else{
var o=_233.file;
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}
if(t.length){
t.push("--"+this.multipartBoundary+"--","");
_235=t.join("\r\n");
}
}while(false);
var _23e=_233["sync"]?false:true;
var _23f=_233["preventCache"]||(this.preventCache==true&&_233["preventCache"]!=false);
var _240=_233["useCache"]==true||(this.useCache==true&&_233["useCache"]!=false);
if(!_23f&&_240){
var _241=getFromCache(url,_235,_233.method);
if(_241){
doLoad(_233,_241,url,_235,false);
return;
}
}
var http=dojo.hostenv.getXmlhttpObject(_233);
var _243=false;
if(_233["attach"]){
http["attach"]=_233["attach"];
}
if(_23e){
this.inFlight.push({"req":_233,"http":http,"url":url,"query":_235,"useCache":_240});
this.startWatchingInFlight();
}
if(_233.method.toLowerCase()=="post"){
http.open("POST",url,_23e);
setHeaders(http,_233);
http.setRequestHeader("Content-Type",_233.multipart?("multipart/form-data; boundary="+this.multipartBoundary):(_233.contentType||"application/x-www-form-urlencoded"));
http.send(_235);
}else{
var _244=url;
if(_235!=""){
_244+=(_244.indexOf("?")>-1?"&":"?")+_235;
}
if(_23f){
_244+=(dojo.string.endsWithAny(_244,"?","&")?"":(_244.indexOf("?")>-1?"&":"?"))+"dojo.preventCache="+new Date().valueOf();
}
http.open(_233.method.toUpperCase(),_244,_23e);
setHeaders(http,_233);
http.send(null);
}
if(!_23e){
doLoad(_233,http,url,_235,_240);
}
_233.abort=function(){
return http.abort();
};
return;
};
dojo.io.transports.addTransport("XMLHTTPTransport");
};
dojo.provide("dojo.event");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.extras");
dojo.require("dojo.lang.func");
dojo.event=new function(){
this.canTimeout=dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);
function interpolateArgs(args){
var dl=dojo.lang;
var ao={srcObj:dj_global,srcFunc:null,adviceObj:dj_global,adviceFunc:null,aroundObj:null,aroundFunc:null,adviceType:(args.length>2)?args[0]:"after",precedence:"last",once:false,delay:null,rate:0,adviceMsg:false};
switch(args.length){
case 0:
return;
case 1:
return;
case 2:
ao.srcFunc=args[0];
ao.adviceFunc=args[1];
break;
case 3:
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isString(args[2]))){
ao.adviceType="after";
ao.srcObj=args[0];
ao.srcFunc=args[1];
ao.adviceFunc=args[2];
}else{
if((dl.isString(args[1]))&&(dl.isString(args[2]))){
ao.srcFunc=args[1];
ao.adviceFunc=args[2];
}else{
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isFunction(args[2]))){
ao.adviceType="after";
ao.srcObj=args[0];
ao.srcFunc=args[1];
var _248=dojo.lang.nameAnonFunc(args[2],ao.adviceObj);
ao.adviceFunc=_248;
}else{
if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){
ao.adviceType="after";
ao.srcObj=dj_global;
var _248=dojo.lang.nameAnonFunc(args[0],ao.srcObj);
ao.srcFunc=_248;
ao.adviceObj=args[1];
ao.adviceFunc=args[2];
}
}
}
}
break;
case 4:
if((dl.isObject(args[0]))&&(dl.isObject(args[2]))){
ao.adviceType="after";
ao.srcObj=args[0];
ao.srcFunc=args[1];
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isString(args[1]))&&(dl.isObject(args[2]))){
ao.adviceType=args[0];
ao.srcObj=dj_global;
ao.srcFunc=args[1];
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isFunction(args[1]))&&(dl.isObject(args[2]))){
ao.adviceType=args[0];
ao.srcObj=dj_global;
var _248=dojo.lang.nameAnonFunc(args[1],dj_global);
ao.srcFunc=_248;
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if(dl.isObject(args[1])){
ao.srcObj=args[1];
ao.srcFunc=args[2];
ao.adviceObj=dj_global;
ao.adviceFunc=args[3];
}else{
if(dl.isObject(args[2])){
ao.srcObj=dj_global;
ao.srcFunc=args[1];
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
ao.srcObj=ao.adviceObj=ao.aroundObj=dj_global;
ao.srcFunc=args[1];
ao.adviceFunc=args[2];
ao.aroundFunc=args[3];
}
}
}
}
}
break;
case 6:
ao.srcObj=args[1];
ao.srcFunc=args[2];
ao.adviceObj=args[3];
ao.adviceFunc=args[4];
ao.aroundFunc=args[5];
ao.aroundObj=dj_global;
break;
default:
ao.srcObj=args[1];
ao.srcFunc=args[2];
ao.adviceObj=args[3];
ao.adviceFunc=args[4];
ao.aroundObj=args[5];
ao.aroundFunc=args[6];
ao.once=args[7];
ao.delay=args[8];
ao.rate=args[9];
ao.adviceMsg=args[10];
break;
}
if((typeof ao.srcFunc).toLowerCase()!="string"){
ao.srcFunc=dojo.lang.getNameInObj(ao.srcObj,ao.srcFunc);
}
if((typeof ao.adviceFunc).toLowerCase()!="string"){
ao.adviceFunc=dojo.lang.getNameInObj(ao.adviceObj,ao.adviceFunc);
}
if((ao.aroundObj)&&((typeof ao.aroundFunc).toLowerCase()!="string")){
ao.aroundFunc=dojo.lang.getNameInObj(ao.aroundObj,ao.aroundFunc);
}
if(!ao.srcObj){
dojo.raise("bad srcObj for srcFunc: "+ao.srcFunc);
}
if(!ao.adviceObj){
dojo.raise("bad adviceObj for adviceFunc: "+ao.adviceFunc);
}
return ao;
}
this.connect=function(){
var ao=interpolateArgs(arguments);
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);
if(ao.adviceFunc){
var mjp2=dojo.event.MethodJoinPoint.getForMethod(ao.adviceObj,ao.adviceFunc);
}
mjp.kwAddAdvice(ao);
return mjp;
};
this.connectBefore=function(){
var args=["before"];
for(var i=0;i<arguments.length;i++){
args.push(arguments[i]);
}
return this.connect.apply(this,args);
};
this.connectAround=function(){
var args=["around"];
for(var i=0;i<arguments.length;i++){
args.push(arguments[i]);
}
return this.connect.apply(this,args);
};
this._kwConnectImpl=function(_250,_251){
var fn=(_251)?"disconnect":"connect";
if(typeof _250["srcFunc"]=="function"){
_250.srcObj=_250["srcObj"]||dj_global;
var _253=dojo.lang.nameAnonFunc(_250.srcFunc,_250.srcObj);
_250.srcFunc=_253;
}
if(typeof _250["adviceFunc"]=="function"){
_250.adviceObj=_250["adviceObj"]||dj_global;
var _253=dojo.lang.nameAnonFunc(_250.adviceFunc,_250.adviceObj);
_250.adviceFunc=_253;
}
return dojo.event[fn]((_250["type"]||_250["adviceType"]||"after"),_250["srcObj"]||dj_global,_250["srcFunc"],_250["adviceObj"]||_250["targetObj"]||dj_global,_250["adviceFunc"]||_250["targetFunc"],_250["aroundObj"],_250["aroundFunc"],_250["once"],_250["delay"],_250["rate"],_250["adviceMsg"]||false);
};
this.kwConnect=function(_254){
return this._kwConnectImpl(_254,false);
};
this.disconnect=function(){
var ao=interpolateArgs(arguments);
if(!ao.adviceFunc){
return;
}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);
return mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);
};
this.kwDisconnect=function(_257){
return this._kwConnectImpl(_257,true);
};
};
dojo.event.MethodInvocation=function(_258,obj,args){
this.jp_=_258;
this.object=obj;
this.args=[];
for(var x=0;x<args.length;x++){
this.args[x]=args[x];
}
this.around_index=-1;
};
dojo.event.MethodInvocation.prototype.proceed=function(){
this.around_index++;
if(this.around_index>=this.jp_.around.length){
return this.jp_.object[this.jp_.methodname].apply(this.jp_.object,this.args);
}else{
var ti=this.jp_.around[this.around_index];
var mobj=ti[0]||dj_global;
var meth=ti[1];
return mobj[meth].call(mobj,this);
}
};
dojo.event.MethodJoinPoint=function(obj,_260){
this.object=obj||dj_global;
this.methodname=_260;
this.methodfunc=this.object[_260];
this.before=[];
this.after=[];
this.around=[];
};
dojo.event.MethodJoinPoint.getForMethod=function(obj,_262){
if(!obj){
obj=dj_global;
}
if(!obj[_262]){
obj[_262]=function(){
};
}else{
if((!dojo.lang.isFunction(obj[_262]))&&(!dojo.lang.isAlien(obj[_262]))){
return null;
}
}
var _263=_262+"$joinpoint";
var _264=_262+"$joinpoint$method";
var _265=obj[_263];
if(!_265){
var _266=false;
if(dojo.event["browser"]){
if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){
_266=true;
dojo.event.browser.addClobberNodeAttrs(obj,[_263,_264,_262]);
}
}
obj[_264]=obj[_262];
_265=obj[_263]=new dojo.event.MethodJoinPoint(obj,_264);
obj[_262]=function(){
var args=[];
if((_266)&&(!arguments.length)){
var evt=null;
try{
if(obj.ownerDocument){
evt=obj.ownerDocument.parentWindow.event;
}else{
if(obj.documentElement){
evt=obj.documentElement.ownerDocument.parentWindow.event;
}else{
evt=window.event;
}
}
}
catch(E){
evt=window.event;
}
if(evt){
args.push(dojo.event.browser.fixEvent(evt));
}
}else{
for(var x=0;x<arguments.length;x++){
if((x==0)&&(_266)&&(dojo.event.browser.isEvent(arguments[x]))){
args.push(dojo.event.browser.fixEvent(arguments[x]));
}else{
args.push(arguments[x]);
}
}
}
return _265.run.apply(_265,args);
};
}
return _265;
};
dojo.lang.extend(dojo.event.MethodJoinPoint,{unintercept:function(){
this.object[this.methodname]=this.methodfunc;
},run:function(){
var obj=this.object||dj_global;
var args=arguments;
var _26c=[];
for(var x=0;x<args.length;x++){
_26c[x]=args[x];
}
var _26e=function(marr){
if(!marr){
dojo.debug("Null argument to unrollAdvice()");
return;
}
var _270=marr[0]||dj_global;
var _271=marr[1];
if(!_270[_271]){
dojo.raise("function \""+_271+"\" does not exist on \""+_270+"\"");
}
var _272=marr[2]||dj_global;
var _273=marr[3];
var msg=marr[6];
var _275;
var to={args:[],jp_:this,object:obj,proceed:function(){
return _270[_271].apply(_270,to.args);
}};
to.args=_26c;
var _277=parseInt(marr[4]);
var _278=((!isNaN(_277))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));
if(marr[5]){
var rate=parseInt(marr[5]);
var cur=new Date();
var _27b=false;
if((marr["last"])&&((cur-marr.last)<=rate)){
if(dojo.event.canTimeout){
if(marr["delayTimer"]){
clearTimeout(marr.delayTimer);
}
var tod=parseInt(rate*2);
var mcpy=dojo.lang.shallowCopy(marr);
marr.delayTimer=setTimeout(function(){
mcpy[5]=0;
_26e(mcpy);
},tod);
}
return;
}else{
marr.last=cur;
}
}
if(_273){
_272[_273].call(_272,to);
}else{
if((_278)&&((dojo.render.html)||(dojo.render.svg))){
dj_global["setTimeout"](function(){
if(msg){
_270[_271].call(_270,to);
}else{
_270[_271].apply(_270,args);
}
},_277);
}else{
if(msg){
_270[_271].call(_270,to);
}else{
_270[_271].apply(_270,args);
}
}
}
};
if(this.before.length>0){
dojo.lang.forEach(this.before,_26e,true);
}
var _27e;
if(this.around.length>0){
var mi=new dojo.event.MethodInvocation(this,obj,args);
_27e=mi.proceed();
}else{
if(this.methodfunc){
_27e=this.object[this.methodname].apply(this.object,args);
}
}
if(this.after.length>0){
dojo.lang.forEach(this.after,_26e,true);
}
return (this.methodfunc)?_27e:null;
},getArr:function(kind){
var arr=this.after;
if((typeof kind=="string")&&(kind.indexOf("before")!=-1)){
arr=this.before;
}else{
if(kind=="around"){
arr=this.around;
}
}
return arr;
},kwAddAdvice:function(args){
this.addAdvice(args["adviceObj"],args["adviceFunc"],args["aroundObj"],args["aroundFunc"],args["adviceType"],args["precedence"],args["once"],args["delay"],args["rate"],args["adviceMsg"]);
},addAdvice:function(_283,_284,_285,_286,_287,_288,once,_28a,rate,_28c){
var arr=this.getArr(_287);
if(!arr){
dojo.raise("bad this: "+this);
}
var ao=[_283,_284,_285,_286,_28a,rate,_28c];
if(once){
if(this.hasAdvice(_283,_284,_287,arr)>=0){
return;
}
}
if(_288=="first"){
arr.unshift(ao);
}else{
arr.push(ao);
}
},hasAdvice:function(_28f,_290,_291,arr){
if(!arr){
arr=this.getArr(_291);
}
var ind=-1;
for(var x=0;x<arr.length;x++){
if((arr[x][0]==_28f)&&(arr[x][1]==_290)){
ind=x;
}
}
return ind;
},removeAdvice:function(_295,_296,_297,once){
var arr=this.getArr(_297);
var ind=this.hasAdvice(_295,_296,_297,arr);
if(ind==-1){
return false;
}
while(ind!=-1){
arr.splice(ind,1);
if(once){
break;
}
ind=this.hasAdvice(_295,_296,_297,arr);
}
return true;
}});
dojo.require("dojo.event");
dojo.provide("dojo.event.topic");
dojo.event.topic=new function(){
this.topics={};
this.getTopic=function(_29b){
if(!this.topics[_29b]){
this.topics[_29b]=new this.TopicImpl(_29b);
}
return this.topics[_29b];
};
this.registerPublisher=function(_29c,obj,_29e){
var _29c=this.getTopic(_29c);
_29c.registerPublisher(obj,_29e);
};
this.subscribe=function(_29f,obj,_2a1){
var _29f=this.getTopic(_29f);
_29f.subscribe(obj,_2a1);
};
this.unsubscribe=function(_2a2,obj,_2a4){
var _2a2=this.getTopic(_2a2);
_2a2.unsubscribe(obj,_2a4);
};
this.publish=function(_2a5,_2a6){
var _2a5=this.getTopic(_2a5);
var args=[];
if(arguments.length==2&&(dojo.lang.isArray(_2a6)||_2a6.callee)){
args=_2a6;
}else{
var args=[];
for(var x=1;x<arguments.length;x++){
args.push(arguments[x]);
}
}
_2a5.sendMessage.apply(_2a5,args);
};
};
dojo.event.topic.TopicImpl=function(_2a9){
this.topicName=_2a9;
var self=this;
self.subscribe=function(_2ab,_2ac){
var tf=_2ac||_2ab;
var to=(!_2ac)?dj_global:_2ab;
dojo.event.kwConnect({srcObj:self,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
self.unsubscribe=function(_2af,_2b0){
var tf=(!_2b0)?_2af:_2b0;
var to=(!_2b0)?null:_2af;
dojo.event.kwDisconnect({srcObj:self,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
self.registerPublisher=function(_2b3,_2b4){
dojo.event.connect(_2b3,_2b4,self,"sendMessage");
};
self.sendMessage=function(_2b5){
};
};
dojo.provide("dojo.event.browser");
dojo.require("dojo.event");
dojo_ie_clobber=new function(){
this.clobberNodes=[];
function nukeProp(node,prop){
try{
node[prop]=null;
}
catch(e){
}
try{
delete node[prop];
}
catch(e){
}
try{
node.removeAttribute(prop);
}
catch(e){
}
}
this.clobber=function(_2b8){
var na;
var tna;
if(_2b8){
tna=_2b8.getElementsByTagName("*");
na=[_2b8];
for(var x=0;x<tna.length;x++){
if(tna[x]["__doClobber__"]){
na.push(tna[x]);
}
}
}else{
try{
window.onload=null;
}
catch(e){
}
na=(this.clobberNodes.length)?this.clobberNodes:document.all;
}
tna=null;
var _2bc={};
for(var i=na.length-1;i>=0;i=i-1){
var el=na[i];
if(el["__clobberAttrs__"]){
for(var j=0;j<el.__clobberAttrs__.length;j++){
nukeProp(el,el.__clobberAttrs__[j]);
}
nukeProp(el,"__clobberAttrs__");
nukeProp(el,"__doClobber__");
}
}
na=null;
};
};
if(dojo.render.html.ie){
window.onunload=function(){
dojo_ie_clobber.clobber();
try{
if((dojo["widget"])&&(dojo.widget["manager"])){
dojo.widget.manager.destroyAll();
}
}
catch(e){
}
try{
window.onload=null;
}
catch(e){
}
try{
window.onunload=null;
}
catch(e){
}
dojo_ie_clobber.clobberNodes=[];
};
}
dojo.event.browser=new function(){
var _2c0=0;
this.clean=function(node){
if(dojo.render.html.ie){
dojo_ie_clobber.clobber(node);
}
};
this.addClobberNode=function(node){
if(!node["__doClobber__"]){
node.__doClobber__=true;
dojo_ie_clobber.clobberNodes.push(node);
node.__clobberAttrs__=[];
}
};
this.addClobberNodeAttrs=function(node,_2c4){
this.addClobberNode(node);
for(var x=0;x<_2c4.length;x++){
node.__clobberAttrs__.push(_2c4[x]);
}
};
this.removeListener=function(node,_2c7,fp,_2c9){
if(!_2c9){
var _2c9=false;
}
_2c7=_2c7.toLowerCase();
if(_2c7.substr(0,2)=="on"){
_2c7=_2c7.substr(2);
}
if(node.removeEventListener){
node.removeEventListener(_2c7,fp,_2c9);
}
};
this.addListener=function(node,_2cb,fp,_2cd,_2ce){
if(!node){
return;
}
if(!_2cd){
var _2cd=false;
}
_2cb=_2cb.toLowerCase();
if(_2cb.substr(0,2)!="on"){
_2cb="on"+_2cb;
}
if(!_2ce){
var _2cf=function(evt){
if(!evt){
evt=window.event;
}
var ret=fp(dojo.event.browser.fixEvent(evt));
if(_2cd){
dojo.event.browser.stopEvent(evt);
}
return ret;
};
}else{
_2cf=fp;
}
if(node.addEventListener){
node.addEventListener(_2cb.substr(2),_2cf,_2cd);
return _2cf;
}else{
if(typeof node[_2cb]=="function"){
var _2d2=node[_2cb];
node[_2cb]=function(e){
_2d2(e);
return _2cf(e);
};
}else{
node[_2cb]=_2cf;
}
if(dojo.render.html.ie){
this.addClobberNodeAttrs(node,[_2cb]);
}
return _2cf;
}
};
this.isEvent=function(obj){
return (typeof obj!="undefined")&&(typeof Event!="undefined")&&(obj.eventPhase);
};
this.currentEvent=null;
this.callListener=function(_2d5,_2d6){
if(typeof _2d5!="function"){
dojo.raise("listener not a function: "+_2d5);
}
dojo.event.browser.currentEvent.currentTarget=_2d6;
return _2d5.call(_2d6,dojo.event.browser.currentEvent);
};
this.stopPropagation=function(){
dojo.event.browser.currentEvent.cancelBubble=true;
};
this.preventDefault=function(){
dojo.event.browser.currentEvent.returnValue=false;
};
this.keys={KEY_BACKSPACE:8,KEY_TAB:9,KEY_ENTER:13,KEY_SHIFT:16,KEY_CTRL:17,KEY_ALT:18,KEY_PAUSE:19,KEY_CAPS_LOCK:20,KEY_ESCAPE:27,KEY_SPACE:32,KEY_PAGE_UP:33,KEY_PAGE_DOWN:34,KEY_END:35,KEY_HOME:36,KEY_LEFT_ARROW:37,KEY_UP_ARROW:38,KEY_RIGHT_ARROW:39,KEY_DOWN_ARROW:40,KEY_INSERT:45,KEY_DELETE:46,KEY_LEFT_WINDOW:91,KEY_RIGHT_WINDOW:92,KEY_SELECT:93,KEY_F1:112,KEY_F2:113,KEY_F3:114,KEY_F4:115,KEY_F5:116,KEY_F6:117,KEY_F7:118,KEY_F8:119,KEY_F9:120,KEY_F10:121,KEY_F11:122,KEY_F12:123,KEY_NUM_LOCK:144,KEY_SCROLL_LOCK:145};
this.revKeys=[];
for(var key in this.keys){
this.revKeys[this.keys[key]]=key;
}
this.fixEvent=function(evt){
if((!evt)&&(window["event"])){
var evt=window.event;
}
if((evt["type"])&&(evt["type"].indexOf("key")==0)){
evt.keys=this.revKeys;
for(var key in this.keys){
evt[key]=this.keys[key];
}
if((dojo.render.html.ie)&&(evt["type"]=="keypress")){
evt.charCode=evt.keyCode;
}
}
if(dojo.render.html.ie){
if(!evt.target){
evt.target=evt.srcElement;
}
if(!evt.currentTarget){
evt.currentTarget=evt.srcElement;
}
if(!evt.layerX){
evt.layerX=evt.offsetX;
}
if(!evt.layerY){
evt.layerY=evt.offsetY;
}
if(evt.fromElement){
evt.relatedTarget=evt.fromElement;
}
if(evt.toElement){
evt.relatedTarget=evt.toElement;
}
this.currentEvent=evt;
evt.callListener=this.callListener;
evt.stopPropagation=this.stopPropagation;
evt.preventDefault=this.preventDefault;
}
return evt;
};
this.stopEvent=function(ev){
if(window.event){
ev.returnValue=false;
ev.cancelBubble=true;
}else{
ev.preventDefault();
ev.stopPropagation();
}
};
};
dojo.hostenv.conditionalLoadModule({common:["dojo.event","dojo.event.topic"],browser:["dojo.event.browser"],dashboard:["dojo.event.browser"]});
dojo.hostenv.moduleLoaded("dojo.event.*");
dojo.provide("dojo.lang.assert");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.type");
dojo.lang.assert=function(_2db,_2dc){
if(!_2db){
var _2dd="An assert statement failed.\n"+"The method dojo.lang.assert() was called with a 'false' value.\n";
if(_2dc){
_2dd+="Here's the assert message:\n"+_2dc+"\n";
}
throw new Error(_2dd);
}
};
dojo.lang.assertType=function(_2de,type,_2e0){
if(!dojo.lang.isOfType(_2de,type)){
if(!_2e0){
if(!dojo.lang.assertType._errorMessage){
dojo.lang.assertType._errorMessage="Type mismatch: dojo.lang.assertType() failed.";
}
_2e0=dojo.lang.assertType._errorMessage;
}
dojo.lang.assert(false,_2e0);
}
};
dojo.lang.assertValidKeywords=function(_2e1,_2e2,_2e3){
var key;
if(!_2e3){
if(!dojo.lang.assertValidKeywords._errorMessage){
dojo.lang.assertValidKeywords._errorMessage="In dojo.lang.assertValidKeywords(), found invalid keyword:";
}
_2e3=dojo.lang.assertValidKeywords._errorMessage;
}
if(dojo.lang.isArray(_2e2)){
for(key in _2e1){
if(!dojo.lang.inArray(_2e2,key)){
dojo.lang.assert(false,_2e3+" "+key);
}
}
}else{
for(key in _2e1){
if(!(key in _2e2)){
dojo.lang.assert(false,_2e3+" "+key);
}
}
}
};
dojo.provide("dojo.AdapterRegistry");
dojo.require("dojo.lang.func");
dojo.require("dojo.lang.type");
dojo.AdapterRegistry=function(){
this.pairs=[];
};
dojo.lang.extend(dojo.AdapterRegistry,{register:function(name,_2e6,wrap,_2e8){
if(_2e8){
this.pairs.unshift([name,_2e6,wrap]);
}else{
this.pairs.push([name,_2e6,wrap]);
}
},match:function(){
for(var i=0;i<this.pairs.length;i++){
var pair=this.pairs[i];
if(pair[1].apply(this,arguments)){
return pair[2].apply(this,arguments);
}
}
throw new Error("No match found");
},unregister:function(name){
for(var i=0;i<this.pairs.length;i++){
var pair=this.pairs[i];
if(pair[0]==name){
this.pairs.splice(i,1);
return true;
}
}
return false;
}});
dojo.provide("dojo.lang.repr");
dojo.require("dojo.lang.common");
dojo.require("dojo.AdapterRegistry");
dojo.lang.reprRegistry=new dojo.AdapterRegistry();
dojo.lang.registerRepr=function(name,_2ef,wrap,_2f1){
dojo.lang.reprRegistry.register(name,_2ef,wrap,_2f1);
};
dojo.lang.repr=function(obj){
if(typeof (obj)=="undefined"){
return "undefined";
}else{
if(obj===null){
return "null";
}
}
try{
if(typeof (obj["__repr__"])=="function"){
return obj["__repr__"]();
}else{
if((typeof (obj["repr"])=="function")&&(obj.repr!=arguments.callee)){
return obj["repr"]();
}
}
return dojo.lang.reprRegistry.match(obj);
}
catch(e){
if(typeof (obj.NAME)=="string"&&(obj.toString==Function.prototype.toString||obj.toString==Object.prototype.toString)){
return o.NAME;
}
}
if(typeof (obj)=="function"){
obj=(obj+"").replace(/^\s+/,"");
var idx=obj.indexOf("{");
if(idx!=-1){
obj=obj.substr(0,idx)+"{...}";
}
}
return obj+"";
};
dojo.lang.reprArrayLike=function(arr){
try{
var na=dojo.lang.map(arr,dojo.lang.repr);
return "["+na.join(", ")+"]";
}
catch(e){
}
};
dojo.lang.reprString=function(str){
return ("\""+str.replace(/(["\\])/g,"\\$1")+"\"").replace(/[\f]/g,"\\f").replace(/[\b]/g,"\\b").replace(/[\n]/g,"\\n").replace(/[\t]/g,"\\t").replace(/[\r]/g,"\\r");
};
dojo.lang.reprNumber=function(num){
return num+"";
};
(function(){
var m=dojo.lang;
m.registerRepr("arrayLike",m.isArrayLike,m.reprArrayLike);
m.registerRepr("string",m.isString,m.reprString);
m.registerRepr("numbers",m.isNumber,m.reprNumber);
m.registerRepr("boolean",m.isBoolean,m.reprNumber);
})();
dojo.hostenv.conditionalLoadModule({common:["dojo.lang","dojo.lang.common","dojo.lang.assert","dojo.lang.array","dojo.lang.type","dojo.lang.func","dojo.lang.extras","dojo.lang.repr"]});
dojo.hostenv.moduleLoaded("dojo.lang.*");
dojo.provide("dojo.widget.Manager");
dojo.require("dojo.lang.array");
dojo.require("dojo.event.*");
dojo.widget.manager=new function(){
this.widgets=[];
this.widgetIds=[];
this.topWidgets={};
var _2f9={};
var _2fa=[];
this.getUniqueId=function(_2fb){
return _2fb+"_"+(_2f9[_2fb]!=undefined?++_2f9[_2fb]:_2f9[_2fb]=0);
};
this.add=function(_2fc){
dojo.profile.start("dojo.widget.manager.add");
this.widgets.push(_2fc);
if(_2fc.widgetId==""){
if(_2fc["id"]){
_2fc.widgetId=_2fc["id"];
}else{
if(_2fc.extraArgs["id"]){
_2fc.widgetId=_2fc.extraArgs["id"];
}else{
_2fc.widgetId=this.getUniqueId(_2fc.widgetType);
}
}
}
if(this.widgetIds[_2fc.widgetId]){
dojo.debug("widget ID collision on ID: "+_2fc.widgetId);
}
this.widgetIds[_2fc.widgetId]=_2fc;
dojo.profile.end("dojo.widget.manager.add");
};
this.destroyAll=function(){
for(var x=this.widgets.length-1;x>=0;x--){
try{
this.widgets[x].destroy(true);
delete this.widgets[x];
}
catch(e){
}
}
};
this.remove=function(_2fe){
var tw=this.widgets[_2fe].widgetId;
delete this.widgetIds[tw];
this.widgets.splice(_2fe,1);
};
this.removeById=function(id){
for(var i=0;i<this.widgets.length;i++){
if(this.widgets[i].widgetId==id){
this.remove(i);
break;
}
}
};
this.getWidgetById=function(id){
return this.widgetIds[id];
};
this.getWidgetsByType=function(type){
var lt=type.toLowerCase();
var ret=[];
dojo.lang.forEach(this.widgets,function(x){
if(x.widgetType.toLowerCase()==lt){
ret.push(x);
}
});
return ret;
};
this.getWidgetsOfType=function(id){
dj_deprecated("getWidgetsOfType is depecrecated, use getWidgetsByType");
return dojo.widget.manager.getWidgetsByType(id);
};
this.getWidgetsByFilter=function(_308){
var ret=[];
dojo.lang.forEach(this.widgets,function(x){
if(_308(x)){
ret.push(x);
}
});
return ret;
};
this.getAllWidgets=function(){
return this.widgets.concat();
};
this.getWidgetByNode=function(node){
var w=this.getAllWidgets();
for(var i=0;i<w.length;i++){
if(w[i].domNode==node){
return w[i];
}
}
return null;
};
this.byId=this.getWidgetById;
this.byType=this.getWidgetsByType;
this.byFilter=this.getWidgetsByFilter;
this.byNode=this.getWidgetByNode;
var _30e={};
var _30f=["dojo.widget"];
for(var i=0;i<_30f.length;i++){
_30f[_30f[i]]=true;
}
this.registerWidgetPackage=function(_311){
if(!_30f[_311]){
_30f[_311]=true;
_30f.push(_311);
}
};
this.getWidgetPackageList=function(){
return dojo.lang.map(_30f,function(elt){
return (elt!==true?elt:undefined);
});
};
this.getImplementation=function(_313,_314,_315){
var impl=this.getImplementationName(_313);
if(impl){
var ret=new impl(_314);
return ret;
}
};
this.getImplementationName=function(_318){
var _319=_318.toLowerCase();
var impl=_30e[_319];
if(impl){
return impl;
}
if(!_2fa.length){
for(var _31b in dojo.render){
if(dojo.render[_31b]["capable"]===true){
var _31c=dojo.render[_31b].prefixes;
for(var i=0;i<_31c.length;i++){
_2fa.push(_31c[i].toLowerCase());
}
}
}
_2fa.push("");
}
for(var i=0;i<_30f.length;i++){
var _31e=dojo.evalObjPath(_30f[i]);
if(!_31e){
continue;
}
for(var j=0;j<_2fa.length;j++){
if(!_31e[_2fa[j]]){
continue;
}
for(var _320 in _31e[_2fa[j]]){
if(_320.toLowerCase()!=_319){
continue;
}
_30e[_319]=_31e[_2fa[j]][_320];
return _30e[_319];
}
}
for(var j=0;j<_2fa.length;j++){
for(var _320 in _31e){
if(_320.toLowerCase()!=(_2fa[j]+_319)){
continue;
}
_30e[_319]=_31e[_320];
return _30e[_319];
}
}
}
throw new Error("Could not locate \""+_318+"\" class");
};
this.resizing=false;
this.onResized=function(){
if(this.resizing){
return;
}
try{
this.resizing=true;
for(var id in this.topWidgets){
var _322=this.topWidgets[id];
if(_322.onResized){
_322.onResized();
}
}
}
catch(e){
}
finally{
this.resizing=false;
}
};
if(typeof window!="undefined"){
dojo.addOnLoad(this,"onResized");
dojo.event.connect(window,"onresize",this,"onResized");
}
};
dojo.widget.getUniqueId=function(){
return dojo.widget.manager.getUniqueId.apply(dojo.widget.manager,arguments);
};
dojo.widget.addWidget=function(){
return dojo.widget.manager.add.apply(dojo.widget.manager,arguments);
};
dojo.widget.destroyAllWidgets=function(){
return dojo.widget.manager.destroyAll.apply(dojo.widget.manager,arguments);
};
dojo.widget.removeWidget=function(){
return dojo.widget.manager.remove.apply(dojo.widget.manager,arguments);
};
dojo.widget.removeWidgetById=function(){
return dojo.widget.manager.removeById.apply(dojo.widget.manager,arguments);
};
dojo.widget.getWidgetById=function(){
return dojo.widget.manager.getWidgetById.apply(dojo.widget.manager,arguments);
};
dojo.widget.getWidgetsByType=function(){
return dojo.widget.manager.getWidgetsByType.apply(dojo.widget.manager,arguments);
};
dojo.widget.getWidgetsByFilter=function(){
return dojo.widget.manager.getWidgetsByFilter.apply(dojo.widget.manager,arguments);
};
dojo.widget.byId=function(){
return dojo.widget.manager.getWidgetById.apply(dojo.widget.manager,arguments);
};
dojo.widget.byType=function(){
return dojo.widget.manager.getWidgetsByType.apply(dojo.widget.manager,arguments);
};
dojo.widget.byFilter=function(){
return dojo.widget.manager.getWidgetsByFilter.apply(dojo.widget.manager,arguments);
};
dojo.widget.byNode=function(){
return dojo.widget.manager.getWidgetByNode.apply(dojo.widget.manager,arguments);
};
dojo.widget.all=function(n){
var _324=dojo.widget.manager.getAllWidgets.apply(dojo.widget.manager,arguments);
if(arguments.length>0){
return _324[n];
}
return _324;
};
dojo.widget.registerWidgetPackage=function(){
return dojo.widget.manager.registerWidgetPackage.apply(dojo.widget.manager,arguments);
};
dojo.widget.getWidgetImplementation=function(){
return dojo.widget.manager.getImplementation.apply(dojo.widget.manager,arguments);
};
dojo.widget.getWidgetImplementationName=function(){
return dojo.widget.manager.getImplementationName.apply(dojo.widget.manager,arguments);
};
dojo.widget.widgets=dojo.widget.manager.widgets;
dojo.widget.widgetIds=dojo.widget.manager.widgetIds;
dojo.widget.root=dojo.widget.manager.root;

