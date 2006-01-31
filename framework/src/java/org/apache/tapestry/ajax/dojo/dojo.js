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
_76.send(null);
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
dj_addNodeEvtHdlr(window,"load",function(){
if(dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
dojo.hostenv.modulesLoaded();
});
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
document.write("<style>v:*{ behavior:url(#default#VML); }</style>");
document.write("<xml:namespace ns=\"urn:schemas-microsoft-com:vml\" prefix=\"v\"/>");
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
dojo.provide("dojo.lang.common");
dojo.require("dojo.lang");
dojo.lang.mixin=function(obj,_98){
var _99={};
for(var x in _98){
if(typeof _99[x]=="undefined"||_99[x]!=_98[x]){
obj[x]=_98[x];
}
}
if(dojo.render.html.ie&&dojo.lang.isFunction(_98["toString"])&&_98["toString"]!=obj["toString"]){
obj.toString=_98.toString;
}
return obj;
};
dojo.lang.extend=function(_9b,_9c){
this.mixin(_9b.prototype,_9c);
};
dojo.lang.find=function(arr,val,_9f,_a0){
if(!dojo.lang.isArrayLike(arr)&&dojo.lang.isArrayLike(val)){
var a=arr;
arr=val;
val=a;
}
var _a2=dojo.lang.isString(arr);
if(_a2){
arr=arr.split("");
}
if(_a0){
var _a3=-1;
var i=arr.length-1;
var end=-1;
}else{
var _a3=1;
var i=0;
var end=arr.length;
}
if(_9f){
while(i!=end){
if(arr[i]===val){
return i;
}
i+=_a3;
}
}else{
while(i!=end){
if(arr[i]==val){
return i;
}
i+=_a3;
}
}
return -1;
};
dojo.lang.indexOf=dojo.lang.find;
dojo.lang.findLast=function(arr,val,_a8){
return dojo.lang.find(arr,val,_a8,true);
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
dojo.provide("dojo.lang");
dojo.provide("dojo.lang.Lang");
dojo.require("dojo.lang.common");
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
for(var _b5 in dojo.lang.whatAmI.custom){
if(dojo.lang.whatAmI.custom[_b5](wh)){
return _b5;
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
dojo.lang.isOfType=function(_b9,_ba){
if(dojo.lang.isArray(_ba)){
var _bb=_ba;
for(var i in _bb){
var _bd=_bb[i];
if(dojo.lang.isOfType(_b9,_bd)){
return true;
}
}
return false;
}else{
if(dojo.lang.isString(_ba)){
_ba=_ba.toLowerCase();
}
switch(_ba){
case Array:
case "array":
return dojo.lang.isArray(_b9);
break;
case Function:
case "function":
return dojo.lang.isFunction(_b9);
break;
case String:
case "string":
return dojo.lang.isString(_b9);
break;
case Number:
case "number":
return dojo.lang.isNumber(_b9);
break;
case "numeric":
return dojo.lang.isNumeric(_b9);
break;
case Boolean:
case "boolean":
return dojo.lang.isBoolean(_b9);
break;
case Object:
case "object":
return dojo.lang.isObject(_b9);
break;
case "pureobject":
return dojo.lang.isPureObject(_b9);
break;
case "builtin":
return dojo.lang.isBuiltIn(_b9);
break;
case "alien":
return dojo.lang.isAlien(_b9);
break;
case "undefined":
return dojo.lang.isUndefined(_b9);
break;
case null:
case "null":
return (_b9===null);
break;
case "optional":
return ((_b9===null)||dojo.lang.isUndefined(_b9));
break;
default:
if(dojo.lang.isFunction(_ba)){
return (_b9 instanceof _ba);
}else{
dojo.raise("dojo.lang.isOfType() was passed an invalid type");
}
break;
}
}
dojo.raise("If we get here, it means a bug was introduced above.");
};
dojo.provide("dojo.lang.func");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.type");
dojo.lang.hitch=function(_be,_bf){
if(dojo.lang.isString(_bf)){
var fcn=_be[_bf];
}else{
var fcn=_bf;
}
return function(){
return fcn.apply(_be,arguments);
};
};
dojo.lang.anonCtr=0;
dojo.lang.anon={};
dojo.lang.nameAnonFunc=function(_c1,_c2){
var nso=(_c2||dojo.lang.anon);
if((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true)){
for(var x in nso){
if(nso[x]===_c1){
return x;
}
}
}
var ret="__"+dojo.lang.anonCtr++;
while(typeof nso[ret]!="undefined"){
ret="__"+dojo.lang.anonCtr++;
}
nso[ret]=_c1;
return ret;
};
dojo.lang.forward=function(_c6){
return function(){
return this[_c6].apply(this,arguments);
};
};
dojo.lang.curry=function(ns,_c8){
var _c9=[];
ns=ns||dj_global;
if(dojo.lang.isString(_c8)){
_c8=ns[_c8];
}
for(var x=2;x<arguments.length;x++){
_c9.push(arguments[x]);
}
var _cb=_c8.length-_c9.length;
function gather(_cc,_cd,_ce){
var _cf=_ce;
var _d0=_cd.slice(0);
for(var x=0;x<_cc.length;x++){
_d0.push(_cc[x]);
}
_ce=_ce-_cc.length;
if(_ce<=0){
var res=_c8.apply(ns,_d0);
_ce=_cf;
return res;
}else{
return function(){
return gather(arguments,_d0,_ce);
};
}
}
return gather([],_c9,_cb);
};
dojo.lang.curryArguments=function(ns,_d4,_d5,_d6){
var _d7=[];
var x=_d6||0;
for(x=_d6;x<_d5.length;x++){
_d7.push(_d5[x]);
}
return dojo.lang.curry.apply(dojo.lang,[ns,_d4].concat(_d7));
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
dojo.lang.delayThese=function(_db,cb,_dd,_de){
if(!_db.length){
if(typeof _de=="function"){
_de();
}
return;
}
if((typeof _dd=="undefined")&&(typeof cb=="number")){
_dd=cb;
cb=function(){
};
}else{
if(!cb){
cb=function(){
};
if(!_dd){
_dd=0;
}
}
}
setTimeout(function(){
(_db.shift())();
cb();
dojo.lang.delayThese(_db,cb,_dd,_de);
},_dd);
};
dojo.provide("dojo.lang.array");
dojo.require("dojo.lang.common");
dojo.lang.has=function(obj,_e0){
return (typeof obj[_e0]!=="undefined");
};
dojo.lang.isEmpty=function(obj){
if(dojo.lang.isObject(obj)){
var tmp={};
var _e3=0;
for(var x in obj){
if(obj[x]&&(!tmp[x])){
_e3++;
break;
}
}
return (_e3==0);
}else{
if(dojo.lang.isArrayLike(obj)||dojo.lang.isString(obj)){
return obj.length==0;
}
}
};
dojo.lang.forEach=function(arr,_e6,_e7){
var _e8=dojo.lang.isString(arr);
if(_e8){
arr=arr.split("");
}
var il=arr.length;
for(var i=0;i<((_e7)?il:arr.length);i++){
if(_e6(arr[i],i,arr)=="break"){
break;
}
}
};
dojo.lang.map=function(arr,obj,_ed){
var _ee=dojo.lang.isString(arr);
if(_ee){
arr=arr.split("");
}
if(dojo.lang.isFunction(obj)&&(!_ed)){
_ed=obj;
obj=dj_global;
}else{
if(dojo.lang.isFunction(obj)&&_ed){
var _ef=obj;
obj=_ed;
_ed=_ef;
}
}
if(Array.map){
var _f0=Array.map(arr,_ed,obj);
}else{
var _f0=[];
for(var i=0;i<arr.length;++i){
_f0.push(_ed.call(obj,arr[i]));
}
}
if(_ee){
return _f0.join("");
}else{
return _f0;
}
};
dojo.lang.every=function(arr,_f3,_f4){
var _f5=dojo.lang.isString(arr);
if(_f5){
arr=arr.split("");
}
if(Array.every){
return Array.every(arr,_f3,_f4);
}else{
if(!_f4){
if(arguments.length>=3){
dojo.raise("thisObject doesn't exist!");
}
_f4=dj_global;
}
for(var i=0;i<arr.length;i++){
if(!_f3.call(_f4,arr[i],i,arr)){
return false;
}
}
return true;
}
};
dojo.lang.some=function(arr,_f8,_f9){
var _fa=dojo.lang.isString(arr);
if(_fa){
arr=arr.split("");
}
if(Array.some){
return Array.some(arr,_f8,_f9);
}else{
if(!_f9){
if(arguments.length>=3){
dojo.raise("thisObject doesn't exist!");
}
_f9=dj_global;
}
for(var i=0;i<arr.length;i++){
if(_f8.call(_f9,arr[i],i,arr)){
return true;
}
}
return false;
}
};
dojo.lang.filter=function(arr,_fd,_fe){
var _ff=dojo.lang.isString(arr);
if(_ff){
arr=arr.split("");
}
if(Array.filter){
var _100=Array.filter(arr,_fd,_fe);
}else{
if(!_fe){
if(arguments.length>=3){
dojo.raise("thisObject doesn't exist!");
}
_fe=dj_global;
}
var _100=[];
for(var i=0;i<arr.length;i++){
if(_fd.call(_fe,arr[i],i,arr)){
_100.push(arr[i]);
}
}
}
if(_ff){
return _100.join("");
}else{
return _100;
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
dojo.lang.toArray=function(_105,_106){
var _107=[];
for(var i=_106||0;i<_105.length;i++){
_107.push(_105[i]);
}
return _107;
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
var _10b=node.tagName;
if(_10b.substr(0,5).toLowerCase()!="dojo:"){
if(_10b.substr(0,4).toLowerCase()=="dojo"){
return "dojo:"+_10b.substring(4).toLowerCase();
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
var _10d=node.className||node.getAttribute("class");
if((_10d)&&(_10d.indexOf)&&(_10d.indexOf("dojo-")!=-1)){
var _10e=_10d.split(" ");
for(var x=0;x<_10e.length;x++){
if((_10e[x].length>5)&&(_10e[x].indexOf("dojo-")>=0)){
return "dojo:"+_10e[x].substr(5).toLowerCase();
}
}
}
}
}
return _10b.toLowerCase();
};
dojo.dom.getUniqueId=function(){
do{
var id="dj_unique_"+(++arguments.callee._idIncrement);
}while(document.getElementById(id));
return id;
};
dojo.dom.getUniqueId._idIncrement=0;
dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_111,_112){
var node=_111.firstChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.nextSibling;
}
if(_112&&node&&node.tagName&&node.tagName.toLowerCase()!=_112.toLowerCase()){
node=dojo.dom.nextElement(node,_112);
}
return node;
};
dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_114,_115){
var node=_114.lastChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.previousSibling;
}
if(_115&&node&&node.tagName&&node.tagName.toLowerCase()!=_115.toLowerCase()){
node=dojo.dom.prevElement(node,_115);
}
return node;
};
dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(node,_118){
if(!node){
return null;
}
do{
node=node.nextSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_118&&_118.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.nextElement(node,_118);
}
return node;
};
dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(node,_11a){
if(!node){
return null;
}
if(_11a){
_11a=_11a.toLowerCase();
}
do{
node=node.previousSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_11a&&_11a.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.prevElement(node,_11a);
}
return node;
};
dojo.dom.moveChildren=function(_11b,_11c,trim){
var _11e=0;
if(trim){
while(_11b.hasChildNodes()&&_11b.firstChild.nodeType==dojo.dom.TEXT_NODE){
_11b.removeChild(_11b.firstChild);
}
while(_11b.hasChildNodes()&&_11b.lastChild.nodeType==dojo.dom.TEXT_NODE){
_11b.removeChild(_11b.lastChild);
}
}
while(_11b.hasChildNodes()){
_11c.appendChild(_11b.firstChild);
_11e++;
}
return _11e;
};
dojo.dom.copyChildren=function(_11f,_120,trim){
var _122=_11f.cloneNode(true);
return this.moveChildren(_122,_120,trim);
};
dojo.dom.removeChildren=function(node){
var _124=node.childNodes.length;
while(node.hasChildNodes()){
node.removeChild(node.firstChild);
}
return _124;
};
dojo.dom.replaceChildren=function(node,_126){
dojo.dom.removeChildren(node);
node.appendChild(_126);
};
dojo.dom.removeNode=function(node){
if(node&&node.parentNode){
return node.parentNode.removeChild(node);
}
};
dojo.dom.getAncestors=function(node,_129,_12a){
var _12b=[];
var _12c=dojo.lang.isFunction(_129);
while(node){
if(!_12c||_129(node)){
_12b.push(node);
}
if(_12a&&_12b.length>0){
return _12b[0];
}
node=node.parentNode;
}
if(_12a){
return null;
}
return _12b;
};
dojo.dom.getAncestorsByTag=function(node,tag,_12f){
tag=tag.toLowerCase();
return dojo.dom.getAncestors(node,function(el){
return ((el.tagName)&&(el.tagName.toLowerCase()==tag));
},_12f);
};
dojo.dom.getFirstAncestorByTag=function(node,tag){
return dojo.dom.getAncestorsByTag(node,tag,true);
};
dojo.dom.isDescendantOf=function(node,_134,_135){
if(_135&&node){
node=node.parentNode;
}
while(node){
if(node==_134){
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
dojo.dom.createDocumentFromText=function(str,_138){
if(!_138){
_138="text/xml";
}
if(typeof DOMParser!="undefined"){
var _139=new DOMParser();
return _139.parseFromString(str,_138);
}else{
if(typeof ActiveXObject!="undefined"){
var _13a=new ActiveXObject("Microsoft.XMLDOM");
if(_13a){
_13a.async=false;
_13a.loadXML(str);
return _13a;
}else{
dojo.debug("toXml didn't work?");
}
}else{
if(document.createElement){
var tmp=document.createElement("xml");
tmp.innerHTML=str;
if(document.implementation&&document.implementation.createDocument){
var _13c=document.implementation.createDocument("foo","",null);
for(var i=0;i<tmp.childNodes.length;i++){
_13c.importNode(tmp.childNodes.item(i),true);
}
return _13c;
}
return tmp.document&&tmp.document.firstChild?tmp.document.firstChild:tmp;
}
}
}
return null;
};
dojo.dom.prependChild=function(node,_13f){
if(_13f.firstChild){
_13f.insertBefore(node,_13f.firstChild);
}else{
_13f.appendChild(node);
}
return true;
};
dojo.dom.insertBefore=function(node,ref,_142){
if(_142!=true&&(node===ref||node.nextSibling===ref)){
return false;
}
var _143=ref.parentNode;
_143.insertBefore(node,ref);
return true;
};
dojo.dom.insertAfter=function(node,ref,_146){
var pn=ref.parentNode;
if(ref==pn.lastChild){
if((_146!=true)&&(node===ref)){
return false;
}
pn.appendChild(node);
}else{
return this.insertBefore(node,ref.nextSibling,_146);
}
return true;
};
dojo.dom.insertAtPosition=function(node,ref,_14a){
if((!node)||(!ref)||(!_14a)){
return false;
}
switch(_14a.toLowerCase()){
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
dojo.dom.insertAtIndex=function(node,_14c,_14d){
var _14e=_14c.childNodes;
if(!_14e.length){
_14c.appendChild(node);
return true;
}
var _14f=null;
for(var i=0;i<_14e.length;i++){
var _151=_14e.item(i)["getAttribute"]?parseInt(_14e.item(i).getAttribute("dojoinsertionindex")):-1;
if(_151<_14d){
_14f=_14e.item(i);
}
}
if(_14f){
return dojo.dom.insertAfter(node,_14f);
}else{
return dojo.dom.insertBefore(node,_14e.item(0));
}
};
dojo.dom.textContent=function(node,text){
if(text){
dojo.dom.replaceChildren(node,document.createTextNode(text));
return text;
}else{
var _154="";
if(node==null){
return _154;
}
for(var i=0;i<node.childNodes.length;i++){
switch(node.childNodes[i].nodeType){
case 1:
case 5:
_154+=dojo.dom.textContent(node.childNodes[i]);
break;
case 3:
case 2:
case 4:
_154+=node.childNodes[i].nodeValue;
break;
default:
break;
}
}
return _154;
}
};
dojo.dom.collectionToArray=function(_156){
dojo.deprecated("dojo.dom.collectionToArray","use dojo.lang.toArray instead");
return dojo.lang.toArray(_156);
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
dojo.provide("dojo.math");
dojo.math.degToRad=function(x){
return (x*Math.PI)/180;
};
dojo.math.radToDeg=function(x){
return (x*180)/Math.PI;
};
dojo.math.factorial=function(n){
if(n<1){
return 0;
}
var _15d=1;
for(var i=1;i<=n;i++){
_15d*=i;
}
return _15d;
};
dojo.math.permutations=function(n,k){
if(n==0||k==0){
return 1;
}
return (dojo.math.factorial(n)/dojo.math.factorial(n-k));
};
dojo.math.combinations=function(n,r){
if(n==0||r==0){
return 1;
}
return (dojo.math.factorial(n)/(dojo.math.factorial(n-r)*dojo.math.factorial(r)));
};
dojo.math.bernstein=function(t,n,i){
return (dojo.math.combinations(n,i)*Math.pow(t,i)*Math.pow(1-t,n-i));
};
dojo.math.gaussianRandom=function(){
var k=2;
do{
var i=2*Math.random()-1;
var j=2*Math.random()-1;
k=i*i+j*j;
}while(k>=1);
k=Math.sqrt((-2*Math.log(k))/k);
return i*k;
};
dojo.math.mean=function(){
var _169=dojo.lang.isArray(arguments[0])?arguments[0]:arguments;
var mean=0;
for(var i=0;i<_169.length;i++){
mean+=_169[i];
}
return mean/_169.length;
};
dojo.math.round=function(_16c,_16d){
if(!_16d){
var _16e=1;
}else{
var _16e=Math.pow(10,_16d);
}
return Math.round(_16c*_16e)/_16e;
};
dojo.math.sd=function(){
var _16f=dojo.lang.isArray(arguments[0])?arguments[0]:arguments;
return Math.sqrt(dojo.math.variance(_16f));
};
dojo.math.variance=function(){
var _170=dojo.lang.isArray(arguments[0])?arguments[0]:arguments;
var mean=0,squares=0;
for(var i=0;i<_170.length;i++){
mean+=_170[i];
squares+=Math.pow(_170[i],2);
}
return (squares/_170.length)-Math.pow(mean/_170.length,2);
};
dojo.math.range=function(a,b,step){
if(arguments.length<2){
b=a;
a=0;
}
if(arguments.length<3){
step=1;
}
var _176=[];
if(step>0){
for(var i=a;i<b;i+=step){
_176.push(i);
}
}else{
if(step<0){
for(var i=a;i>b;i+=step){
_176.push(i);
}
}else{
throw new Error("dojo.math.range: step must be non-zero");
}
}
return _176;
};
dojo.provide("dojo.graphics.color");
dojo.require("dojo.lang.array");
dojo.require("dojo.math");
dojo.graphics.color.Color=function(r,g,b,a){
if(dojo.lang.isArray(r)){
this.r=r[0];
this.g=r[1];
this.b=r[2];
this.a=r[3]||1;
}else{
if(dojo.lang.isString(r)){
var rgb=dojo.graphics.color.extractRGB(r);
this.r=rgb[0];
this.g=rgb[1];
this.b=rgb[2];
this.a=g||1;
}else{
if(r instanceof dojo.graphics.color.Color){
this.r=r.r;
this.b=r.b;
this.g=r.g;
this.a=r.a;
}else{
this.r=r;
this.g=g;
this.b=b;
this.a=a;
}
}
}
};
dojo.graphics.color.Color.fromArray=function(arr){
return new dojo.graphics.color.Color(arr[0],arr[1],arr[2],arr[3]);
};
dojo.lang.extend(dojo.graphics.color.Color,{toRgb:function(_17e){
if(_17e){
return this.toRgba();
}else{
return [this.r,this.g,this.b];
}
},toRgba:function(){
return [this.r,this.g,this.b,this.a];
},toHex:function(){
return dojo.graphics.color.rgb2hex(this.toRgb());
},toCss:function(){
return "rgb("+this.toRgb().join()+")";
},toString:function(){
return this.toHex();
},toHsv:function(){
return dojo.graphics.color.rgb2hsv(this.toRgb());
},toHsl:function(){
return dojo.graphics.color.rgb2hsl(this.toRgb());
},blend:function(_17f,_180){
return dojo.graphics.color.blend(this.toRgb(),new Color(_17f).toRgb(),_180);
}});
dojo.graphics.color.named={white:[255,255,255],black:[0,0,0],red:[255,0,0],green:[0,255,0],blue:[0,0,255],navy:[0,0,128],gray:[128,128,128],silver:[192,192,192]};
dojo.graphics.color.blend=function(a,b,_183){
if(typeof a=="string"){
return dojo.graphics.color.blendHex(a,b,_183);
}
if(!_183){
_183=0;
}else{
if(_183>1){
_183=1;
}else{
if(_183<-1){
_183=-1;
}
}
}
var c=new Array(3);
for(var i=0;i<3;i++){
var half=Math.abs(a[i]-b[i])/2;
c[i]=Math.floor(Math.min(a[i],b[i])+half+(half*_183));
}
return c;
};
dojo.graphics.color.blendHex=function(a,b,_189){
return dojo.graphics.color.rgb2hex(dojo.graphics.color.blend(dojo.graphics.color.hex2rgb(a),dojo.graphics.color.hex2rgb(b),_189));
};
dojo.graphics.color.extractRGB=function(_18a){
var hex="0123456789abcdef";
_18a=_18a.toLowerCase();
if(_18a.indexOf("rgb")==0){
var _18c=_18a.match(/rgba*\((\d+), *(\d+), *(\d+)/i);
var ret=_18c.splice(1,3);
return ret;
}else{
var _18e=dojo.graphics.color.hex2rgb(_18a);
if(_18e){
return _18e;
}else{
return dojo.graphics.color.named[_18a]||[255,255,255];
}
}
};
dojo.graphics.color.hex2rgb=function(hex){
var _190="0123456789ABCDEF";
var rgb=new Array(3);
if(hex.indexOf("#")==0){
hex=hex.substring(1);
}
hex=hex.toUpperCase();
if(hex.replace(new RegExp("["+_190+"]","g"),"")!=""){
return null;
}
if(hex.length==3){
rgb[0]=hex.charAt(0)+hex.charAt(0);
rgb[1]=hex.charAt(1)+hex.charAt(1);
rgb[2]=hex.charAt(2)+hex.charAt(2);
}else{
rgb[0]=hex.substring(0,2);
rgb[1]=hex.substring(2,4);
rgb[2]=hex.substring(4);
}
for(var i=0;i<rgb.length;i++){
rgb[i]=_190.indexOf(rgb[i].charAt(0))*16+_190.indexOf(rgb[i].charAt(1));
}
return rgb;
};
dojo.graphics.color.rgb2hex=function(r,g,b){
if(dojo.lang.isArray(r)){
g=r[1]||0;
b=r[2]||0;
r=r[0]||0;
}
function pad(x){
while(x.length<2){
x="0"+x;
}
return x;
}
var ret=dojo.lang.map([r,g,b],function(x){
var s=x.toString(16);
while(s.length<2){
s="0"+s;
}
return s;
});
ret.unshift("#");
return ret.join("");
};
dojo.graphics.color.rgb2hsv=function(r,g,b){
if(dojo.lang.isArray(r)){
b=r[2]||0;
g=r[1]||0;
r=r[0]||0;
}
var h=null;
var s=null;
var v=null;
var min=Math.min(r,g,b);
v=Math.max(r,g,b);
var _1a1=v-min;
s=(v==0)?0:_1a1/v;
if(s==0){
h=0;
}else{
if(r==v){
h=60*(g-b)/_1a1;
}else{
if(g==v){
h=120+60*(b-r)/_1a1;
}else{
if(b==v){
h=240+60*(r-g)/_1a1;
}
}
}
if(h<0){
h+=360;
}
}
h=(h==0)?360:Math.ceil((h/360)*255);
s=Math.ceil(s*255);
return [h,s,v];
};
dojo.graphics.color.hsv2rgb=function(h,s,v){
if(dojo.lang.isArray(h)){
v=h[2]||0;
s=h[1]||0;
h=h[0]||0;
}
h=(h/255)*360;
if(h==360){
h=0;
}
s=s/255;
v=v/255;
var r=null;
var g=null;
var b=null;
if(s==0){
r=v;
g=v;
b=v;
}else{
var _1a8=h/60;
var i=Math.floor(_1a8);
var f=_1a8-i;
var p=v*(1-s);
var q=v*(1-(s*f));
var t=v*(1-(s*(1-f)));
switch(i){
case 0:
r=v;
g=t;
b=p;
break;
case 1:
r=q;
g=v;
b=p;
break;
case 2:
r=p;
g=v;
b=t;
break;
case 3:
r=p;
g=q;
b=v;
break;
case 4:
r=t;
g=p;
b=v;
break;
case 5:
r=v;
g=p;
b=q;
break;
}
}
r=Math.ceil(r*255);
g=Math.ceil(g*255);
b=Math.ceil(b*255);
return [r,g,b];
};
dojo.graphics.color.rgb2hsl=function(r,g,b){
if(dojo.lang.isArray(r)){
b=r[2]||0;
g=r[1]||0;
r=r[0]||0;
}
r/=255;
g/=255;
b/=255;
var h=null;
var s=null;
var l=null;
var min=Math.min(r,g,b);
var max=Math.max(r,g,b);
var _1b6=max-min;
l=(min+max)/2;
s=0;
if((l>0)&&(l<1)){
s=_1b6/((l<0.5)?(2*l):(2-2*l));
}
h=0;
if(_1b6>0){
if((max==r)&&(max!=g)){
h+=(g-b)/_1b6;
}
if((max==g)&&(max!=b)){
h+=(2+(b-r)/_1b6);
}
if((max==b)&&(max!=r)){
h+=(4+(r-g)/_1b6);
}
h*=60;
}
h=(h==0)?360:Math.ceil((h/360)*255);
s=Math.ceil(s*255);
l=Math.ceil(l*255);
return [h,s,l];
};
dojo.graphics.color.hsl2rgb=function(h,s,l){
if(dojo.lang.isArray(h)){
l=h[2]||0;
s=h[1]||0;
h=h[0]||0;
}
h=(h/255)*360;
if(h==360){
h=0;
}
s=s/255;
l=l/255;
while(h<0){
h+=360;
}
while(h>360){
h-=360;
}
if(h<120){
r=(120-h)/60;
g=h/60;
b=0;
}else{
if(h<240){
r=0;
g=(240-h)/60;
b=(h-120)/60;
}else{
r=(h-240)/60;
g=0;
b=(360-h)/60;
}
}
r=Math.min(r,1);
g=Math.min(g,1);
b=Math.min(b,1);
r=2*s*r+(1-s);
g=2*s*g+(1-s);
b=2*s*b+(1-s);
if(l<0.5){
r=l*r;
g=l*g;
b=l*b;
}else{
r=(1-l)*r+2*l-1;
g=(1-l)*g+2*l-1;
b=(1-l)*b+2*l-1;
}
r=Math.ceil(r*255);
g=Math.ceil(g*255);
b=Math.ceil(b*255);
return [r,g,b];
};
dojo.graphics.color.hsl2hex=function(h,s,l){
var rgb=dojo.graphics.color.hsl2rgb(h,s,l);
return dojo.graphics.color.rgb2hex(rgb[0],rgb[1],rgb[2]);
};
dojo.graphics.color.hex2hsl=function(hex){
var rgb=dojo.graphics.color.hex2rgb(hex);
return dojo.graphics.color.rgb2hsl(rgb[0],rgb[1],rgb[2]);
};
dojo.provide("dojo.style");
dojo.require("dojo.graphics.color");
dojo.style.boxSizing={marginBox:"margin-box",borderBox:"border-box",paddingBox:"padding-box",contentBox:"content-box"};
dojo.style.getBoxSizing=function(node){
node=dojo.byId(node);
if(dojo.render.html.ie||dojo.render.html.opera){
var cm=document["compatMode"];
if(cm=="BackCompat"||cm=="QuirksMode"){
return dojo.style.boxSizing.borderBox;
}else{
return dojo.style.boxSizing.contentBox;
}
}else{
if(arguments.length==0){
node=document.documentElement;
}
var _1c2=dojo.style.getStyle(node,"-moz-box-sizing");
if(!_1c2){
_1c2=dojo.style.getStyle(node,"box-sizing");
}
return (_1c2?_1c2:dojo.style.boxSizing.contentBox);
}
};
dojo.style.isBorderBox=function(node){
return (dojo.style.getBoxSizing(node)==dojo.style.boxSizing.borderBox);
};
dojo.style.getUnitValue=function(node,_1c5,_1c6){
node=dojo.byId(node);
var _1c7={value:0,units:"px"};
var s=dojo.style.getComputedStyle(node,_1c5);
if(s==""||(s=="auto"&&_1c6)){
return _1c7;
}
if(dojo.lang.isUndefined(s)){
_1c7.value=NaN;
}else{
var _1c9=s.match(/([\d.]+)([a-z%]*)/i);
if(!_1c9){
_1c7.value=NaN;
}else{
_1c7.value=Number(_1c9[1]);
_1c7.units=_1c9[2].toLowerCase();
}
}
return _1c7;
};
dojo.style.getPixelValue=function(node,_1cb,_1cc){
node=dojo.byId(node);
var _1cd=dojo.style.getUnitValue(node,_1cb,_1cc);
if(isNaN(_1cd.value)){
return 0;
}
if((_1cd.value)&&(_1cd.units!="px")){
return NaN;
}
return _1cd.value;
};
dojo.style.getNumericStyle=dojo.style.getPixelValue;
dojo.style.isPositionAbsolute=function(node){
node=dojo.byId(node);
return (dojo.style.getComputedStyle(node,"position")=="absolute");
};
dojo.style.getMarginWidth=function(node){
node=dojo.byId(node);
var _1d0=dojo.style.isPositionAbsolute(node);
var left=dojo.style.getPixelValue(node,"margin-left",_1d0);
var _1d2=dojo.style.getPixelValue(node,"margin-right",_1d0);
return left+_1d2;
};
dojo.style.getBorderWidth=function(node){
node=dojo.byId(node);
var left=(dojo.style.getStyle(node,"border-left-style")=="none"?0:dojo.style.getPixelValue(node,"border-left-width"));
var _1d5=(dojo.style.getStyle(node,"border-right-style")=="none"?0:dojo.style.getPixelValue(node,"border-right-width"));
return left+_1d5;
};
dojo.style.getPaddingWidth=function(node){
node=dojo.byId(node);
var left=dojo.style.getPixelValue(node,"padding-left",true);
var _1d8=dojo.style.getPixelValue(node,"padding-right",true);
return left+_1d8;
};
dojo.style.getContentWidth=function(node){
node=dojo.byId(node);
return node.offsetWidth-dojo.style.getPaddingWidth(node)-dojo.style.getBorderWidth(node);
};
dojo.style.getInnerWidth=function(node){
node=dojo.byId(node);
return node.offsetWidth;
};
dojo.style.getOuterWidth=function(node){
node=dojo.byId(node);
return dojo.style.getInnerWidth(node)+dojo.style.getMarginWidth(node);
};
dojo.style.setOuterWidth=function(node,_1dd){
node=dojo.byId(node);
if(!dojo.style.isBorderBox(node)){
_1dd-=dojo.style.getPaddingWidth(node)+dojo.style.getBorderWidth(node);
}
_1dd-=dojo.style.getMarginWidth(node);
if(!isNaN(_1dd)&&_1dd>0){
node.style.width=_1dd+"px";
return true;
}else{
return false;
}
};
dojo.style.getContentBoxWidth=dojo.style.getContentWidth;
dojo.style.getBorderBoxWidth=dojo.style.getInnerWidth;
dojo.style.getMarginBoxWidth=dojo.style.getOuterWidth;
dojo.style.setMarginBoxWidth=dojo.style.setOuterWidth;
dojo.style.getMarginHeight=function(node){
node=dojo.byId(node);
var _1df=dojo.style.isPositionAbsolute(node);
var top=dojo.style.getPixelValue(node,"margin-top",_1df);
var _1e1=dojo.style.getPixelValue(node,"margin-bottom",_1df);
return top+_1e1;
};
dojo.style.getBorderHeight=function(node){
node=dojo.byId(node);
var top=(dojo.style.getStyle(node,"border-top-style")=="none"?0:dojo.style.getPixelValue(node,"border-top-width"));
var _1e4=(dojo.style.getStyle(node,"border-bottom-style")=="none"?0:dojo.style.getPixelValue(node,"border-bottom-width"));
return top+_1e4;
};
dojo.style.getPaddingHeight=function(node){
node=dojo.byId(node);
var top=dojo.style.getPixelValue(node,"padding-top",true);
var _1e7=dojo.style.getPixelValue(node,"padding-bottom",true);
return top+_1e7;
};
dojo.style.getContentHeight=function(node){
node=dojo.byId(node);
return node.offsetHeight-dojo.style.getPaddingHeight(node)-dojo.style.getBorderHeight(node);
};
dojo.style.getInnerHeight=function(node){
node=dojo.byId(node);
return node.offsetHeight;
};
dojo.style.getOuterHeight=function(node){
node=dojo.byId(node);
return dojo.style.getInnerHeight(node)+dojo.style.getMarginHeight(node);
};
dojo.style.setOuterHeight=function(node,_1ec){
node=dojo.byId(node);
if(!dojo.style.isBorderBox(node)){
_1ec-=dojo.style.getPaddingHeight(node)+dojo.style.getBorderHeight(node);
}
_1ec-=dojo.style.getMarginHeight(node);
if(!isNaN(_1ec)&&_1ec>0){
node.style.height=_1ec+"px";
return true;
}else{
return false;
}
};
dojo.style.setContentWidth=function(node,_1ee){
node=dojo.byId(node);
if(dojo.style.isBorderBox(node)){
_1ee+=dojo.style.getPaddingWidth(node)+dojo.style.getBorderWidth(node);
}
if(!isNaN(_1ee)&&_1ee>0){
node.style.width=_1ee+"px";
return true;
}else{
return false;
}
};
dojo.style.setContentHeight=function(node,_1f0){
node=dojo.byId(node);
if(dojo.style.isBorderBox(node)){
_1f0+=dojo.style.getPaddingHeight(node)+dojo.style.getBorderHeight(node);
}
if(!isNaN(_1f0)&&_1f0>0){
node.style.height=_1f0+"px";
return true;
}else{
return false;
}
};
dojo.style.getContentBoxHeight=dojo.style.getContentHeight;
dojo.style.getBorderBoxHeight=dojo.style.getInnerHeight;
dojo.style.getMarginBoxHeight=dojo.style.getOuterHeight;
dojo.style.setMarginBoxHeight=dojo.style.setOuterHeight;
dojo.style.getTotalOffset=function(node,type,_1f3){
node=dojo.byId(node);
var _1f4=(type=="top")?"offsetTop":"offsetLeft";
var _1f5=(type=="top")?"scrollTop":"scrollLeft";
var _1f6=(type=="top")?"y":"x";
var _1f7=0;
if(node["offsetParent"]){
if(dojo.render.html.safari&&node.style.getPropertyValue("position")=="absolute"&&node.parentNode==document.body){
var _1f8=document.body;
}else{
var _1f8=document.body.parentNode;
}
if(_1f3&&node.parentNode!=document.body){
_1f7-=dojo.style.sumAncestorProperties(node,_1f5);
}
do{
_1f7+=node[_1f4];
node=node.offsetParent;
}while(node!=_1f8&&node!=null);
}else{
if(node[_1f6]){
_1f7+=node[_1f6];
}
}
return _1f7;
};
dojo.style.sumAncestorProperties=function(node,prop){
node=dojo.byId(node);
if(!node){
return 0;
}
var _1fb=0;
while(node){
var val=node[prop];
if(val){
_1fb+=val-0;
}
node=node.parentNode;
}
return _1fb;
};
dojo.style.totalOffsetLeft=function(node,_1fe){
node=dojo.byId(node);
return dojo.style.getTotalOffset(node,"left",_1fe);
};
dojo.style.getAbsoluteX=dojo.style.totalOffsetLeft;
dojo.style.totalOffsetTop=function(node,_200){
node=dojo.byId(node);
return dojo.style.getTotalOffset(node,"top",_200);
};
dojo.style.getAbsoluteY=dojo.style.totalOffsetTop;
dojo.style.getAbsolutePosition=function(node,_202){
node=dojo.byId(node);
var _203=[dojo.style.getAbsoluteX(node,_202),dojo.style.getAbsoluteY(node,_202)];
_203.x=_203[0];
_203.y=_203[1];
return _203;
};
dojo.style.styleSheet=null;
dojo.style.insertCssRule=function(_204,_205,_206){
if(!dojo.style.styleSheet){
if(document.createStyleSheet){
dojo.style.styleSheet=document.createStyleSheet();
}else{
if(document.styleSheets[0]){
dojo.style.styleSheet=document.styleSheets[0];
}else{
return null;
}
}
}
if(arguments.length<3){
if(dojo.style.styleSheet.cssRules){
_206=dojo.style.styleSheet.cssRules.length;
}else{
if(dojo.style.styleSheet.rules){
_206=dojo.style.styleSheet.rules.length;
}else{
return null;
}
}
}
if(dojo.style.styleSheet.insertRule){
var rule=_204+" { "+_205+" }";
return dojo.style.styleSheet.insertRule(rule,_206);
}else{
if(dojo.style.styleSheet.addRule){
return dojo.style.styleSheet.addRule(_204,_205,_206);
}else{
return null;
}
}
};
dojo.style.removeCssRule=function(_208){
if(!dojo.style.styleSheet){
dojo.debug("no stylesheet defined for removing rules");
return false;
}
if(dojo.render.html.ie){
if(!_208){
_208=dojo.style.styleSheet.rules.length;
dojo.style.styleSheet.removeRule(_208);
}
}else{
if(document.styleSheets[0]){
if(!_208){
_208=dojo.style.styleSheet.cssRules.length;
}
dojo.style.styleSheet.deleteRule(_208);
}
}
return true;
};
dojo.style.getBackgroundColor=function(node){
node=dojo.byId(node);
var _20a;
do{
_20a=dojo.style.getStyle(node,"background-color");
if(_20a.toLowerCase()=="rgba(0, 0, 0, 0)"){
_20a="transparent";
}
if(node==document.getElementsByTagName("body")[0]){
node=null;
break;
}
node=node.parentNode;
}while(node&&dojo.lang.inArray(_20a,["transparent",""]));
if(_20a=="transparent"){
_20a=[255,255,255,0];
}else{
_20a=dojo.graphics.color.extractRGB(_20a);
}
return _20a;
};
dojo.style.getComputedStyle=function(node,_20c,_20d){
node=dojo.byId(node);
var _20e=_20d;
if(node.style.getPropertyValue){
_20e=node.style.getPropertyValue(_20c);
}
if(!_20e){
if(document.defaultView){
var cs=document.defaultView.getComputedStyle(node,"");
if(cs){
_20e=cs.getPropertyValue(_20c);
}
}else{
if(node.currentStyle){
_20e=node.currentStyle[dojo.style.toCamelCase(_20c)];
}
}
}
return _20e;
};
dojo.style.getStyle=function(node,_211){
node=dojo.byId(node);
var _212=dojo.style.toCamelCase(_211);
var _213=node.style[_212];
return (_213?_213:dojo.style.getComputedStyle(node,_211,_213));
};
dojo.style.toCamelCase=function(_214){
var arr=_214.split("-"),cc=arr[0];
for(var i=1;i<arr.length;i++){
cc+=arr[i].charAt(0).toUpperCase()+arr[i].substring(1);
}
return cc;
};
dojo.style.toSelectorCase=function(_217){
return _217.replace(/([A-Z])/g,"-$1").toLowerCase();
};
dojo.style.setOpacity=function setOpacity(node,_219,_21a){
node=dojo.byId(node);
var h=dojo.render.html;
if(!_21a){
if(_219>=1){
if(h.ie){
dojo.style.clearOpacity(node);
return;
}else{
_219=0.999999;
}
}else{
if(_219<0){
_219=0;
}
}
}
if(h.ie){
if(node.nodeName.toLowerCase()=="tr"){
var tds=node.getElementsByTagName("td");
for(var x=0;x<tds.length;x++){
tds[x].style.filter="Alpha(Opacity="+_219*100+")";
}
}
node.style.filter="Alpha(Opacity="+_219*100+")";
}else{
if(h.moz){
node.style.opacity=_219;
node.style.MozOpacity=_219;
}else{
if(h.safari){
node.style.opacity=_219;
node.style.KhtmlOpacity=_219;
}else{
node.style.opacity=_219;
}
}
}
};
dojo.style.getOpacity=function getOpacity(node){
node=dojo.byId(node);
if(dojo.render.html.ie){
var opac=(node.filters&&node.filters.alpha&&typeof node.filters.alpha.opacity=="number"?node.filters.alpha.opacity:100)/100;
}else{
var opac=node.style.opacity||node.style.MozOpacity||node.style.KhtmlOpacity||1;
}
return opac>=0.999999?1:Number(opac);
};
dojo.style.clearOpacity=function clearOpacity(node){
node=dojo.byId(node);
var h=dojo.render.html;
if(h.ie){
if(node.filters&&node.filters.alpha){
node.style.filter="";
}
}else{
if(h.moz){
node.style.opacity=1;
node.style.MozOpacity=1;
}else{
if(h.safari){
node.style.opacity=1;
node.style.KhtmlOpacity=1;
}else{
node.style.opacity=1;
}
}
}
};
dojo.style.isVisible=function(node){
node=dojo.byId(node);
return dojo.style.getComputedStyle(node||this.domNode,"display")!="none";
};
dojo.style.show=function(node){
node=dojo.byId(node);
if(node.style){
node.style.display=dojo.lang.inArray(["tr","td","th"],node.tagName.toLowerCase())?"":"block";
}
};
dojo.style.hide=function(node){
node=dojo.byId(node);
if(node.style){
node.style.display="none";
}
};
dojo.style.toggleVisible=function(node){
if(dojo.style.isVisible(node)){
dojo.style.hide(node);
return false;
}else{
dojo.style.show(node);
return true;
}
};
dojo.style.toCoordinateArray=function(_226,_227){
if(dojo.lang.isArray(_226)){
while(_226.length<4){
_226.push(0);
}
while(_226.length>4){
_226.pop();
}
var ret=_226;
}else{
var node=dojo.byId(_226);
var ret=[dojo.style.getAbsoluteX(node,_227),dojo.style.getAbsoluteY(node,_227),dojo.style.getInnerWidth(node),dojo.style.getInnerHeight(node)];
}
ret.x=ret[0];
ret.y=ret[1];
ret.w=ret[2];
ret.h=ret[3];
return ret;
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
dojo.string.repeat=function(str,_230,_231){
var out="";
for(var i=0;i<_230;i++){
out+=str;
if(_231&&i<_230-1){
out+=_231;
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
dojo.provide("dojo.string.extras");
dojo.require("dojo.string.common");
dojo.require("dojo.lang");
dojo.string.paramString=function(str,_240,_241){
for(var name in _240){
var re=new RegExp("\\%\\{"+name+"\\}","g");
str=str.replace(re,_240[name]);
}
if(_241){
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
var _245=str.split(" ");
var _246="";
var len=_245.length;
for(var i=0;i<len;i++){
var word=_245[i];
word=word.charAt(0).toUpperCase()+word.substring(1,word.length);
_246+=word;
if(i<len-1){
_246+=" ";
}
}
return new String(_246);
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
var _24d=escape(str);
var _24e,re=/%u([0-9A-F]{4})/i;
while((_24e=_24d.match(re))){
var num=Number("0x"+_24e[1]);
var _250=escape("&#"+num+";");
ret+=_24d.substring(0,_24e.index)+_250;
_24d=_24d.substring(_24e.index+_24e[0].length);
}
ret+=_24d.replace(/\+/g,"%2B");
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
dojo.string.escapeXml=function(str,_256){
str=str.replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");
if(!_256){
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
dojo.string.endsWith=function(str,end,_25e){
if(_25e){
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
dojo.string.startsWith=function(str,_262,_263){
if(_263){
str=str.toLowerCase();
_262=_262.toLowerCase();
}
return str.indexOf(_262)==0;
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
if(str.indexOf(arguments[i]>-1)){
return true;
}
}
return false;
};
dojo.string.normalizeNewlines=function(text,_269){
if(_269=="\n"){
text=text.replace(/\r\n/g,"\n");
text=text.replace(/\r/g,"\n");
}else{
if(_269=="\r"){
text=text.replace(/\r\n/g,"\r");
text=text.replace(/\n/g,"\r");
}else{
text=text.replace(/([^\r])\n/g,"$1\r\n");
text=text.replace(/\r([^\n])/g,"\r\n$1");
}
}
return text;
};
dojo.string.splitEscaped=function(str,_26b){
var _26c=[];
for(var i=0,prevcomma=0;i<str.length;i++){
if(str.charAt(i)=="\\"){
i++;
continue;
}
if(str.charAt(i)==_26b){
_26c.push(str.substring(prevcomma,i));
prevcomma=i+1;
}
}
_26c.push(str.substr(prevcomma));
return _26c;
};
dojo.provide("dojo.uri.Uri");
dojo.uri=new function(){
this.joinPath=function(){
var arr=[];
for(var i=0;i<arguments.length;i++){
arr.push(arguments[i]);
}
return arr.join("/").replace(/\/{2,}/g,"/").replace(/((https*|ftps*):)/i,"$1/");
};
this.dojoUri=function(uri){
return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri(),uri);
};
this.Uri=function(){
var uri=arguments[0];
for(var i=1;i<arguments.length;i++){
if(!arguments[i]){
continue;
}
var _273=new dojo.uri.Uri(arguments[i].toString());
var _274=new dojo.uri.Uri(uri.toString());
if(_273.path==""&&_273.scheme==null&&_273.authority==null&&_273.query==null){
if(_273.fragment!=null){
_274.fragment=_273.fragment;
}
_273=_274;
}else{
if(_273.scheme==null){
_273.scheme=_274.scheme;
if(_273.authority==null){
_273.authority=_274.authority;
if(_273.path.charAt(0)!="/"){
var path=_274.path.substring(0,_274.path.lastIndexOf("/")+1)+_273.path;
var segs=path.split("/");
for(var j=0;j<segs.length;j++){
if(segs[j]=="."){
if(j==segs.length-1){
segs[j]="";
}else{
segs.splice(j,1);
j--;
}
}else{
if(j>0&&!(j==1&&segs[0]=="")&&segs[j]==".."&&segs[j-1]!=".."){
if(j==segs.length-1){
segs.splice(j,1);
segs[j-1]="";
}else{
segs.splice(j-1,2);
j-=2;
}
}
}
}
_273.path=segs.join("/");
}
}
}
}
uri="";
if(_273.scheme!=null){
uri+=_273.scheme+":";
}
if(_273.authority!=null){
uri+="//"+_273.authority;
}
uri+=_273.path;
if(_273.query!=null){
uri+="?"+_273.query;
}
if(_273.fragment!=null){
uri+="#"+_273.fragment;
}
}
this.uri=uri.toString();
var _278="^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$";
var r=this.uri.match(new RegExp(_278));
this.scheme=r[2]||(r[1]?"":null);
this.authority=r[4]||(r[3]?"":null);
this.path=r[5];
this.query=r[7]||(r[6]?"":null);
this.fragment=r[9]||(r[8]?"":null);
if(this.authority!=null){
_278="^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$";
r=this.authority.match(new RegExp(_278));
this.user=r[3]||null;
this.password=r[4]||null;
this.host=r[5];
this.port=r[7]||null;
}
this.toString=function(){
return this.uri;
};
};
};
dojo.provide("dojo.html");
dojo.require("dojo.lang.func");
dojo.require("dojo.dom");
dojo.require("dojo.style");
dojo.require("dojo.string");
dojo.require("dojo.string.extras");
dojo.require("dojo.uri.Uri");
dojo.lang.mixin(dojo.html,dojo.dom);
dojo.lang.mixin(dojo.html,dojo.style);
dojo.html.clearSelection=function(){
try{
if(window["getSelection"]){
if(dojo.render.html.safari){
window.getSelection().collapse();
}else{
window.getSelection().removeAllRanges();
}
}else{
if(document.selection){
if(document.selection.empty){
document.selection.empty();
}else{
if(document.selection.clear){
document.selection.clear();
}
}
}
}
return true;
}
catch(e){
dojo.debug(e);
return false;
}
};
dojo.html.disableSelection=function(_27a){
_27a=dojo.byId(_27a)||document.body;
var h=dojo.render.html;
if(h.mozilla){
_27a.style.MozUserSelect="none";
}else{
if(h.safari){
_27a.style.KhtmlUserSelect="none";
}else{
if(h.ie){
_27a.unselectable="on";
}else{
return false;
}
}
}
return true;
};
dojo.html.enableSelection=function(_27c){
_27c=dojo.byId(_27c)||document.body;
var h=dojo.render.html;
if(h.mozilla){
_27c.style.MozUserSelect="";
}else{
if(h.safari){
_27c.style.KhtmlUserSelect="";
}else{
if(h.ie){
_27c.unselectable="off";
}else{
return false;
}
}
}
return true;
};
dojo.html.selectElement=function(_27e){
_27e=dojo.byId(_27e);
if(document.selection&&document.body.createTextRange){
var _27f=document.body.createTextRange();
_27f.moveToElementText(_27e);
_27f.select();
}else{
if(window["getSelection"]){
var _280=window.getSelection();
if(_280["selectAllChildren"]){
_280.selectAllChildren(_27e);
}
}
}
};
dojo.html.isSelectionCollapsed=function(){
if(document["selection"]){
return document.selection.createRange().text=="";
}else{
if(window["getSelection"]){
var _281=window.getSelection();
if(dojo.lang.isString(_281)){
return _281=="";
}else{
return _281.isCollapsed;
}
}
}
};
dojo.html.getEventTarget=function(evt){
if(!evt){
evt=window.event||{};
}
if(evt.srcElement){
return evt.srcElement;
}else{
if(evt.target){
return evt.target;
}
}
return null;
};
dojo.html.getScrollTop=function(){
return document.documentElement.scrollTop||document.body.scrollTop||0;
};
dojo.html.getScrollLeft=function(){
return document.documentElement.scrollLeft||document.body.scrollLeft||0;
};
dojo.html.getDocumentWidth=function(){
dojo.deprecated("dojo.html.getDocument* has been deprecated in favor of dojo.html.getViewport*");
return dojo.html.getViewportWidth();
};
dojo.html.getDocumentHeight=function(){
dojo.deprecated("dojo.html.getDocument* has been deprecated in favor of dojo.html.getViewport*");
return dojo.html.getViewportHeight();
};
dojo.html.getDocumentSize=function(){
dojo.deprecated("dojo.html.getDocument* has been deprecated in favor of dojo.html.getViewport*");
return dojo.html.getViewportSize();
};
dojo.html.getViewportWidth=function(){
var w=0;
if(window.innerWidth){
w=window.innerWidth;
}
if(dojo.exists(document,"documentElement.clientWidth")){
var w2=document.documentElement.clientWidth;
if(!w||w2&&w2<w){
w=w2;
}
return w;
}
if(document.body){
return document.body.clientWidth;
}
return 0;
};
dojo.html.getViewportHeight=function(){
if(window.innerHeight){
return window.innerHeight;
}
if(dojo.exists(document,"documentElement.clientHeight")){
return document.documentElement.clientHeight;
}
if(document.body){
return document.body.clientHeight;
}
return 0;
};
dojo.html.getViewportSize=function(){
var ret=[dojo.html.getViewportWidth(),dojo.html.getViewportHeight()];
ret.w=ret[0];
ret.h=ret[1];
return ret;
};
dojo.html.getScrollOffset=function(){
var ret=[0,0];
if(window.pageYOffset){
ret=[window.pageXOffset,window.pageYOffset];
}else{
if(dojo.exists(document,"documentElement.scrollTop")){
ret=[document.documentElement.scrollLeft,document.documentElement.scrollTop];
}else{
if(document.body){
ret=[document.body.scrollLeft,document.body.scrollTop];
}
}
}
ret.x=ret[0];
ret.y=ret[1];
return ret;
};
dojo.html.getParentOfType=function(node,type){
dojo.deprecated("dojo.html.getParentOfType has been deprecated in favor of dojo.html.getParentByType*");
return dojo.html.getParentByType(node,type);
};
dojo.html.getParentByType=function(node,type){
var _28b=dojo.byId(node);
type=type.toLowerCase();
while((_28b)&&(_28b.nodeName.toLowerCase()!=type)){
if(_28b==(document["body"]||document["documentElement"])){
return null;
}
_28b=_28b.parentNode;
}
return _28b;
};
dojo.html.getAttribute=function(node,attr){
node=dojo.byId(node);
if((!node)||(!node.getAttribute)){
return null;
}
var ta=typeof attr=="string"?attr:new String(attr);
var v=node.getAttribute(ta.toUpperCase());
if((v)&&(typeof v=="string")&&(v!="")){
return v;
}
if(v&&v.value){
return v.value;
}
if((node.getAttributeNode)&&(node.getAttributeNode(ta))){
return (node.getAttributeNode(ta)).value;
}else{
if(node.getAttribute(ta)){
return node.getAttribute(ta);
}else{
if(node.getAttribute(ta.toLowerCase())){
return node.getAttribute(ta.toLowerCase());
}
}
}
return null;
};
dojo.html.hasAttribute=function(node,attr){
node=dojo.byId(node);
return dojo.html.getAttribute(node,attr)?true:false;
};
dojo.html.getClass=function(node){
node=dojo.byId(node);
if(!node){
return "";
}
var cs="";
if(node.className){
cs=node.className;
}else{
if(dojo.html.hasAttribute(node,"class")){
cs=dojo.html.getAttribute(node,"class");
}
}
return dojo.string.trim(cs);
};
dojo.html.getClasses=function(node){
node=dojo.byId(node);
var c=dojo.html.getClass(node);
return (c=="")?[]:c.split(/\s+/g);
};
dojo.html.hasClass=function(node,_297){
node=dojo.byId(node);
return dojo.lang.inArray(dojo.html.getClasses(node),_297);
};
dojo.html.prependClass=function(node,_299){
node=dojo.byId(node);
if(!node){
return false;
}
_299+=" "+dojo.html.getClass(node);
return dojo.html.setClass(node,_299);
};
dojo.html.addClass=function(node,_29b){
node=dojo.byId(node);
if(!node){
return false;
}
if(dojo.html.hasClass(node,_29b)){
return false;
}
_29b=dojo.string.trim(dojo.html.getClass(node)+" "+_29b);
return dojo.html.setClass(node,_29b);
};
dojo.html.setClass=function(node,_29d){
node=dojo.byId(node);
if(!node){
return false;
}
var cs=new String(_29d);
try{
if(typeof node.className=="string"){
node.className=cs;
}else{
if(node.setAttribute){
node.setAttribute("class",_29d);
node.className=cs;
}else{
return false;
}
}
}
catch(e){
dojo.debug("dojo.html.setClass() failed",e);
}
return true;
};
dojo.html.removeClass=function(node,_2a0,_2a1){
node=dojo.byId(node);
if(!node){
return false;
}
var _2a0=dojo.string.trim(new String(_2a0));
try{
var cs=dojo.html.getClasses(node);
var nca=[];
if(_2a1){
for(var i=0;i<cs.length;i++){
if(cs[i].indexOf(_2a0)==-1){
nca.push(cs[i]);
}
}
}else{
for(var i=0;i<cs.length;i++){
if(cs[i]!=_2a0){
nca.push(cs[i]);
}
}
}
dojo.html.setClass(node,nca.join(" "));
}
catch(e){
dojo.debug("dojo.html.removeClass() failed",e);
}
return true;
};
dojo.html.replaceClass=function(node,_2a6,_2a7){
node=dojo.byId(node);
dojo.html.removeClass(node,_2a7);
dojo.html.addClass(node,_2a6);
};
dojo.html.classMatchType={ContainsAll:0,ContainsAny:1,IsOnly:2};
dojo.html.getElementsByClass=function(_2a8,_2a9,_2aa,_2ab){
_2a9=dojo.byId(_2a9);
if(!_2a9){
_2a9=document;
}
var _2ac=_2a8.split(/\s+/g);
var _2ad=[];
if(_2ab!=1&&_2ab!=2){
_2ab=0;
}
var _2ae=new RegExp("(\\s|^)(("+_2ac.join(")|(")+"))(\\s|$)");
if(!_2aa){
_2aa="*";
}
var _2af=_2a9.getElementsByTagName(_2aa);
outer:
for(var i=0;i<_2af.length;i++){
var node=_2af[i];
var _2b2=dojo.html.getClasses(node);
if(_2b2.length==0){
continue outer;
}
var _2b3=0;
for(var j=0;j<_2b2.length;j++){
if(_2ae.test(_2b2[j])){
if(_2ab==dojo.html.classMatchType.ContainsAny){
_2ad.push(node);
continue outer;
}else{
_2b3++;
}
}else{
if(_2ab==dojo.html.classMatchType.IsOnly){
continue outer;
}
}
}
if(_2b3==_2ac.length){
if(_2ab==dojo.html.classMatchType.IsOnly&&_2b3==_2b2.length){
_2ad.push(node);
}else{
if(_2ab==dojo.html.classMatchType.ContainsAll){
_2ad.push(node);
}
}
}
}
return _2ad;
};
dojo.html.getElementsByClassName=dojo.html.getElementsByClass;
dojo.html.gravity=function(node,e){
node=dojo.byId(node);
var _2b7=e.pageX||e.clientX+document.body.scrollLeft;
var _2b8=e.pageY||e.clientY+document.body.scrollTop;
with(dojo.html){
var _2b9=getAbsoluteX(node)+(getInnerWidth(node)/2);
var _2ba=getAbsoluteY(node)+(getInnerHeight(node)/2);
}
with(dojo.html.gravity){
return ((_2b7<_2b9?WEST:EAST)|(_2b8<_2ba?NORTH:SOUTH));
}
};
dojo.html.gravity.NORTH=1;
dojo.html.gravity.SOUTH=1<<1;
dojo.html.gravity.EAST=1<<2;
dojo.html.gravity.WEST=1<<3;
dojo.html.overElement=function(_2bb,e){
_2bb=dojo.byId(_2bb);
var _2bd=e.pageX||e.clientX+document.body.scrollLeft;
var _2be=e.pageY||e.clientY+document.body.scrollTop;
with(dojo.html){
var top=getAbsoluteY(_2bb);
var _2c0=top+getInnerHeight(_2bb);
var left=getAbsoluteX(_2bb);
var _2c2=left+getInnerWidth(_2bb);
}
return (_2bd>=left&&_2bd<=_2c2&&_2be>=top&&_2be<=_2c0);
};
dojo.html.renderedTextContent=function(node){
node=dojo.byId(node);
var _2c4="";
if(node==null){
return _2c4;
}
for(var i=0;i<node.childNodes.length;i++){
switch(node.childNodes[i].nodeType){
case 1:
case 5:
var _2c6="unknown";
try{
_2c6=dojo.style.getStyle(node.childNodes[i],"display");
}
catch(E){
}
switch(_2c6){
case "block":
case "list-item":
case "run-in":
case "table":
case "table-row-group":
case "table-header-group":
case "table-footer-group":
case "table-row":
case "table-column-group":
case "table-column":
case "table-cell":
case "table-caption":
_2c4+="\n";
_2c4+=dojo.html.renderedTextContent(node.childNodes[i]);
_2c4+="\n";
break;
case "none":
break;
default:
if(node.childNodes[i].tagName&&node.childNodes[i].tagName.toLowerCase()=="br"){
_2c4+="\n";
}else{
_2c4+=dojo.html.renderedTextContent(node.childNodes[i]);
}
break;
}
break;
case 3:
case 2:
case 4:
var text=node.childNodes[i].nodeValue;
var _2c8="unknown";
try{
_2c8=dojo.style.getStyle(node,"text-transform");
}
catch(E){
}
switch(_2c8){
case "capitalize":
text=dojo.string.capitalize(text);
break;
case "uppercase":
text=text.toUpperCase();
break;
case "lowercase":
text=text.toLowerCase();
break;
default:
break;
}
switch(_2c8){
case "nowrap":
break;
case "pre-wrap":
break;
case "pre-line":
break;
case "pre":
break;
default:
text=text.replace(/\s+/," ");
if(/\s$/.test(_2c4)){
text.replace(/^\s/,"");
}
break;
}
_2c4+=text;
break;
default:
break;
}
}
return _2c4;
};
dojo.html.setActiveStyleSheet=function(_2c9){
var i,a,main;
for(i=0;(a=document.getElementsByTagName("link")[i]);i++){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")){
a.disabled=true;
if(a.getAttribute("title")==_2c9){
a.disabled=false;
}
}
}
};
dojo.html.getActiveStyleSheet=function(){
var i,a;
for(i=0;(a=document.getElementsByTagName("link")[i]);i++){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")&&!a.disabled){
return a.getAttribute("title");
}
}
return null;
};
dojo.html.getPreferredStyleSheet=function(){
var i,a;
for(i=0;(a=document.getElementsByTagName("link")[i]);i++){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("rel").indexOf("alt")==-1&&a.getAttribute("title")){
return a.getAttribute("title");
}
}
return null;
};
dojo.html.body=function(){
dojo.deprecated("dojo.html.body","use document.body instead");
return document.body||document.getElementsByTagName("body")[0];
};
dojo.html.createNodesFromText=function(txt,trim){
if(trim){
txt=dojo.string.trim(txt);
}
var tn=document.createElement("div");
tn.style.visibility="hidden";
document.body.appendChild(tn);
var _2d0="none";
if((/^<t[dh][\s\r\n>]/i).test(dojo.string.trimStart(txt))){
txt="<table><tbody><tr>"+txt+"</tr></tbody></table>";
_2d0="cell";
}else{
if((/^<tr[\s\r\n>]/i).test(dojo.string.trimStart(txt))){
txt="<table><tbody>"+txt+"</tbody></table>";
_2d0="row";
}else{
if((/^<(thead|tbody|tfoot)[\s\r\n>]/i).test(dojo.string.trimStart(txt))){
txt="<table>"+txt+"</table>";
_2d0="section";
}
}
}
tn.innerHTML=txt;
tn.normalize();
var _2d1=null;
switch(_2d0){
case "cell":
_2d1=tn.getElementsByTagName("tr")[0];
break;
case "row":
_2d1=tn.getElementsByTagName("tbody")[0];
break;
case "section":
_2d1=tn.getElementsByTagName("table")[0];
break;
default:
_2d1=tn;
break;
}
var _2d2=[];
for(var x=0;x<_2d1.childNodes.length;x++){
_2d2.push(_2d1.childNodes[x].cloneNode(true));
}
tn.style.display="none";
document.body.removeChild(tn);
return _2d2;
};
if(!dojo.evalObjPath("dojo.dom.createNodesFromText")){
dojo.dom.createNodesFromText=function(){
dojo.deprecated("dojo.dom.createNodesFromText","use dojo.html.createNodesFromText instead");
return dojo.html.createNodesFromText.apply(dojo.html,arguments);
};
}
dojo.html.isTag=function(node){
node=dojo.byId(node);
if(node&&node.tagName){
var arr=dojo.lang.map(dojo.lang.toArray(arguments,1),function(a){
return String(a).toLowerCase();
});
return arr[dojo.lang.find(node.tagName.toLowerCase(),arr)]||"";
}
return "";
};
dojo.html.placeOnScreen=function(node,_2d8,_2d9,_2da,_2db){
if(dojo.lang.isArray(_2d8)){
_2db=_2da;
_2da=_2d9;
_2d9=_2d8[1];
_2d8=_2d8[0];
}
if(!isNaN(_2da)){
_2da=[Number(_2da),Number(_2da)];
}else{
if(!dojo.lang.isArray(_2da)){
_2da=[0,0];
}
}
var _2dc=dojo.html.getScrollOffset();
var view=dojo.html.getViewportSize();
node=dojo.byId(node);
var w=node.offsetWidth+_2da[0];
var h=node.offsetHeight+_2da[1];
if(_2db){
_2d8-=_2dc.x;
_2d9-=_2dc.y;
}
var x=_2d8+w;
if(x>view.w){
x=view.w-w;
}else{
x=_2d8;
}
x=Math.max(_2da[0],x)+_2dc.x;
var y=_2d9+h;
if(y>view.h){
y=view.h-h;
}else{
y=_2d9;
}
y=Math.max(_2da[1],y)+_2dc.y;
node.style.left=x+"px";
node.style.top=y+"px";
var ret=[x,y];
ret.x=x;
ret.y=y;
return ret;
};
dojo.html.placeOnScreenPoint=function(node,_2e4,_2e5,_2e6,_2e7){
if(dojo.lang.isArray(_2e4)){
_2e7=_2e6;
_2e6=_2e5;
_2e5=_2e4[1];
_2e4=_2e4[0];
}
var _2e8=dojo.html.getScrollOffset();
var view=dojo.html.getViewportSize();
node=dojo.byId(node);
var w=node.offsetWidth;
var h=node.offsetHeight;
if(_2e7){
_2e4-=_2e8.x;
_2e5-=_2e8.y;
}
var x=-1,y=-1;
if(_2e4+w<=view.w&&_2e5+h<=view.h){
x=_2e4;
y=_2e5;
}
if((x<0||y<0)&&_2e4<=view.w&&_2e5+h<=view.h){
x=_2e4-w;
y=_2e5;
}
if((x<0||y<0)&&_2e4+w<=view.w&&_2e5<=view.h){
x=_2e4;
y=_2e5-h;
}
if((x<0||y<0)&&_2e4<=view.w&&_2e5<=view.h){
x=_2e4-w;
y=_2e5-h;
}
if(x<0||y<0||(x+w>view.w)||(y+h>view.h)){
return dojo.html.placeOnScreen(node,_2e4,_2e5,_2e6,_2e7);
}
x+=_2e8.x;
y+=_2e8.y;
node.style.left=x+"px";
node.style.top=y+"px";
var ret=[x,y];
ret.x=x;
ret.y=y;
return ret;
};
dojo.style.insertCssFile=function(URI,doc,_2f0){
if(!URI){
return;
}
if(!doc){
doc=document;
}
if(doc.baseURI){
URI=new dojo.uri.Uri(doc.baseURI,URI);
}
if(_2f0&&doc.styleSheets){
var loc=location.href.split("#")[0].substring(0,location.href.indexOf(location.pathname));
for(var i=0;i<doc.styleSheets.length;i++){
if(doc.styleSheets[i].href&&URI.toString()==new dojo.uri.Uri(doc.styleSheets[i].href.toString())){
return;
}
}
}
var file=doc.createElement("link");
file.setAttribute("type","text/css");
file.setAttribute("rel","stylesheet");
file.setAttribute("href",URI);
var head=doc.getElementsByTagName("head")[0];
if(head){
head.appendChild(file);
}
};
dojo.html.BackgroundIframe=function(){
if(this.ie){
this.iframe=document.createElement("<iframe frameborder='0' src='about:blank'>");
var s=this.iframe.style;
s.position="absolute";
s.left=s.top="0px";
s.zIndex=2;
s.display="none";
dojo.style.setOpacity(this.iframe,0);
document.body.appendChild(this.iframe);
}else{
this.enabled=false;
}
};
dojo.lang.extend(dojo.html.BackgroundIframe,{ie:dojo.render.html.ie,enabled:true,visibile:false,iframe:null,sizeNode:null,sizeCoords:null,size:function(node){
if(!this.ie||!this.enabled){
return;
}
if(dojo.dom.isNode(node)){
this.sizeNode=node;
}else{
if(arguments.length>0){
this.sizeNode=null;
this.sizeCoords=node;
}
}
this.update();
},update:function(){
if(!this.ie||!this.enabled){
return;
}
if(this.sizeNode){
this.sizeCoords=dojo.html.toCoordinateArray(this.sizeNode,true);
}else{
if(this.sizeCoords){
this.sizeCoords=dojo.html.toCoordinateArray(this.sizeCoords,true);
}else{
return;
}
}
var s=this.iframe.style;
var dims=this.sizeCoords;
s.width=dims.w+"px";
s.height=dims.h+"px";
s.left=dims.x+"px";
s.top=dims.y+"px";
},setZIndex:function(node){
if(!this.ie||!this.enabled){
return;
}
if(dojo.dom.isNode(node)){
this.iframe.zIndex=dojo.html.getStyle(node,"z-index")-1;
}else{
if(!isNaN(node)){
this.iframe.zIndex=node;
}
}
},show:function(node){
if(!this.ie||!this.enabled){
return;
}
this.size(node);
this.iframe.style.display="block";
},hide:function(){
if(!this.ie){
return;
}
var s=this.iframe.style;
s.display="none";
s.width=s.height="1px";
},remove:function(){
dojo.dom.removeNode(this.iframe);
}});
dojo.provide("dojo.animation.AnimationEvent");
dojo.require("dojo.lang");
dojo.animation.AnimationEvent=function(anim,type,_2fe,_2ff,_300,_301,dur,pct,fps){
this.type=type;
this.animation=anim;
this.coords=_2fe;
this.x=_2fe[0];
this.y=_2fe[1];
this.z=_2fe[2];
this.startTime=_2ff;
this.currentTime=_300;
this.endTime=_301;
this.duration=dur;
this.percent=pct;
this.fps=fps;
};
dojo.lang.extend(dojo.animation.AnimationEvent,{coordsAsInts:function(){
var _305=new Array(this.coords.length);
for(var i=0;i<this.coords.length;i++){
_305[i]=Math.round(this.coords[i]);
}
return _305;
}});
dojo.provide("dojo.math.curves");
dojo.require("dojo.math");
dojo.math.curves={Line:function(_307,end){
this.start=_307;
this.end=end;
this.dimensions=_307.length;
for(var i=0;i<_307.length;i++){
_307[i]=Number(_307[i]);
}
for(var i=0;i<end.length;i++){
end[i]=Number(end[i]);
}
this.getValue=function(n){
var _30b=new Array(this.dimensions);
for(var i=0;i<this.dimensions;i++){
_30b[i]=((this.end[i]-this.start[i])*n)+this.start[i];
}
return _30b;
};
return this;
},Bezier:function(pnts){
this.getValue=function(step){
if(step>=1){
return this.p[this.p.length-1];
}
if(step<=0){
return this.p[0];
}
var _30f=new Array(this.p[0].length);
for(var k=0;j<this.p[0].length;k++){
_30f[k]=0;
}
for(var j=0;j<this.p[0].length;j++){
var C=0;
var D=0;
for(var i=0;i<this.p.length;i++){
C+=this.p[i][j]*this.p[this.p.length-1][0]*dojo.math.bernstein(step,this.p.length,i);
}
for(var l=0;l<this.p.length;l++){
D+=this.p[this.p.length-1][0]*dojo.math.bernstein(step,this.p.length,l);
}
_30f[j]=C/D;
}
return _30f;
};
this.p=pnts;
return this;
},CatmullRom:function(pnts,c){
this.getValue=function(step){
var _319=step*(this.p.length-1);
var node=Math.floor(_319);
var _31b=_319-node;
var i0=node-1;
if(i0<0){
i0=0;
}
var i=node;
var i1=node+1;
if(i1>=this.p.length){
i1=this.p.length-1;
}
var i2=node+2;
if(i2>=this.p.length){
i2=this.p.length-1;
}
var u=_31b;
var u2=_31b*_31b;
var u3=_31b*_31b*_31b;
var _323=new Array(this.p[0].length);
for(var k=0;k<this.p[0].length;k++){
var x1=(-this.c*this.p[i0][k])+((2-this.c)*this.p[i][k])+((this.c-2)*this.p[i1][k])+(this.c*this.p[i2][k]);
var x2=(2*this.c*this.p[i0][k])+((this.c-3)*this.p[i][k])+((3-2*this.c)*this.p[i1][k])+(-this.c*this.p[i2][k]);
var x3=(-this.c*this.p[i0][k])+(this.c*this.p[i1][k]);
var x4=this.p[i][k];
_323[k]=x1*u3+x2*u2+x3*u+x4;
}
return _323;
};
if(!c){
this.c=0.7;
}else{
this.c=c;
}
this.p=pnts;
return this;
},Arc:function(_329,end,ccw){
var _32c=dojo.math.points.midpoint(_329,end);
var _32d=dojo.math.points.translate(dojo.math.points.invert(_32c),_329);
var rad=Math.sqrt(Math.pow(_32d[0],2)+Math.pow(_32d[1],2));
var _32f=dojo.math.radToDeg(Math.atan(_32d[1]/_32d[0]));
if(_32d[0]<0){
_32f-=90;
}else{
_32f+=90;
}
dojo.math.curves.CenteredArc.call(this,_32c,rad,_32f,_32f+(ccw?-180:180));
},CenteredArc:function(_330,_331,_332,end){
this.center=_330;
this.radius=_331;
this.start=_332||0;
this.end=end;
this.getValue=function(n){
var _335=new Array(2);
var _336=dojo.math.degToRad(this.start+((this.end-this.start)*n));
_335[0]=this.center[0]+this.radius*Math.sin(_336);
_335[1]=this.center[1]-this.radius*Math.cos(_336);
return _335;
};
return this;
},Circle:function(_337,_338){
dojo.math.curves.CenteredArc.call(this,_337,_338,0,360);
return this;
},Path:function(){
var _339=[];
var _33a=[];
var _33b=[];
var _33c=0;
this.add=function(_33d,_33e){
if(_33e<0){
dojo.raise("dojo.math.curves.Path.add: weight cannot be less than 0");
}
_339.push(_33d);
_33a.push(_33e);
_33c+=_33e;
computeRanges();
};
this.remove=function(_33f){
for(var i=0;i<_339.length;i++){
if(_339[i]==_33f){
_339.splice(i,1);
_33c-=_33a.splice(i,1)[0];
break;
}
}
computeRanges();
};
this.removeAll=function(){
_339=[];
_33a=[];
_33c=0;
};
this.getValue=function(n){
var _342=false,value=0;
for(var i=0;i<_33b.length;i++){
var r=_33b[i];
if(n>=r[0]&&n<r[1]){
var subN=(n-r[0])/r[2];
value=_339[i].getValue(subN);
_342=true;
break;
}
}
if(!_342){
value=_339[_339.length-1].getValue(1);
}
for(j=0;j<i;j++){
value=dojo.math.points.translate(value,_339[j].getValue(1));
}
return value;
};
function computeRanges(){
var _346=0;
for(var i=0;i<_33a.length;i++){
var end=_346+_33a[i]/_33c;
var len=end-_346;
_33b[i]=[_346,end,len];
_346=end;
}
}
return this;
}};
dojo.provide("dojo.animation.Animation");
dojo.require("dojo.animation.AnimationEvent");
dojo.require("dojo.lang.func");
dojo.require("dojo.math");
dojo.require("dojo.math.curves");
dojo.animation.Animation=function(_34a,_34b,_34c,_34d,rate){
if(dojo.lang.isArray(_34a)){
_34a=new dojo.math.curves.Line(_34a[0],_34a[1]);
}
this.curve=_34a;
this.duration=_34b;
this.repeatCount=_34d||0;
this.rate=rate||25;
if(_34c){
if(dojo.lang.isFunction(_34c.getValue)){
this.accel=_34c;
}else{
var i=0.35*_34c+0.5;
this.accel=new dojo.math.curves.CatmullRom([[0],[i],[1]],0.45);
}
}
};
dojo.lang.extend(dojo.animation.Animation,{curve:null,duration:0,repeatCount:0,accel:null,onBegin:null,onAnimate:null,onEnd:null,onPlay:null,onPause:null,onStop:null,handler:null,_animSequence:null,_startTime:null,_endTime:null,_lastFrame:null,_timer:null,_percent:0,_active:false,_paused:false,_startRepeatCount:0,play:function(_350){
if(_350){
clearTimeout(this._timer);
this._active=false;
this._paused=false;
this._percent=0;
}else{
if(this._active&&!this._paused){
return;
}
}
this._startTime=new Date().valueOf();
if(this._paused){
this._startTime-=(this.duration*this._percent/100);
}
this._endTime=this._startTime+this.duration;
this._lastFrame=this._startTime;
var e=new dojo.animation.AnimationEvent(this,null,this.curve.getValue(this._percent),this._startTime,this._startTime,this._endTime,this.duration,this._percent,0);
this._active=true;
this._paused=false;
if(this._percent==0){
if(!this._startRepeatCount){
this._startRepeatCount=this.repeatCount;
}
e.type="begin";
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onBegin=="function"){
this.onBegin(e);
}
}
e.type="play";
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onPlay=="function"){
this.onPlay(e);
}
if(this._animSequence){
this._animSequence._setCurrent(this);
}
this._cycle();
},pause:function(){
clearTimeout(this._timer);
if(!this._active){
return;
}
this._paused=true;
var e=new dojo.animation.AnimationEvent(this,"pause",this.curve.getValue(this._percent),this._startTime,new Date().valueOf(),this._endTime,this.duration,this._percent,0);
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onPause=="function"){
this.onPause(e);
}
},playPause:function(){
if(!this._active||this._paused){
this.play();
}else{
this.pause();
}
},gotoPercent:function(pct,_354){
clearTimeout(this._timer);
this._active=true;
this._paused=true;
this._percent=pct;
if(_354){
this.play();
}
},stop:function(_355){
clearTimeout(this._timer);
var step=this._percent/100;
if(_355){
step=1;
}
var e=new dojo.animation.AnimationEvent(this,"stop",this.curve.getValue(step),this._startTime,new Date().valueOf(),this._endTime,this.duration,this._percent,Math.round(fps));
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onStop=="function"){
this.onStop(e);
}
this._active=false;
this._paused=false;
},status:function(){
if(this._active){
return this._paused?"paused":"playing";
}else{
return "stopped";
}
},_cycle:function(){
clearTimeout(this._timer);
if(this._active){
var curr=new Date().valueOf();
var step=(curr-this._startTime)/(this._endTime-this._startTime);
fps=1000/(curr-this._lastFrame);
this._lastFrame=curr;
if(step>=1){
step=1;
this._percent=100;
}else{
this._percent=step*100;
}
if(this.accel&&this.accel.getValue){
step=this.accel.getValue(step);
}
var e=new dojo.animation.AnimationEvent(this,"animate",this.curve.getValue(step),this._startTime,curr,this._endTime,this.duration,this._percent,Math.round(fps));
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onAnimate=="function"){
this.onAnimate(e);
}
if(step<1){
this._timer=setTimeout(dojo.lang.hitch(this,"_cycle"),this.rate);
}else{
e.type="end";
this._active=false;
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onEnd=="function"){
this.onEnd(e);
}
if(this.repeatCount>0){
this.repeatCount--;
this.play(true);
}else{
if(this.repeatCount==-1){
this.play(true);
}else{
if(this._startRepeatCount){
this.repeatCount=this._startRepeatCount;
this._startRepeatCount=0;
}
if(this._animSequence){
this._animSequence._playNext();
}
}
}
}
}
}});
dojo.provide("dojo.animation");
dojo.require("dojo.animation.Animation");
dojo.provide("dojo.lang.extras");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.type");
dojo.lang.setTimeout=function(func,_35c){
var _35d=window,argsStart=2;
if(!dojo.lang.isFunction(func)){
_35d=func;
func=_35c;
_35c=arguments[2];
argsStart++;
}
if(dojo.lang.isString(func)){
func=_35d[func];
}
var args=[];
for(var i=argsStart;i<arguments.length;i++){
args.push(arguments[i]);
}
return setTimeout(function(){
func.apply(_35d,args);
},_35c);
};
dojo.lang.getNameInObj=function(ns,item){
if(!ns){
ns=dj_global;
}
for(var x in ns){
if(ns[x]===item){
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
dojo.provide("dojo.event");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.extras");
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
var _369=dojo.lang.nameAnonFunc(args[2],ao.adviceObj);
ao.adviceFunc=_369;
}else{
if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){
ao.adviceType="after";
ao.srcObj=dj_global;
var _369=dojo.lang.nameAnonFunc(args[0],ao.srcObj);
ao.srcFunc=_369;
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
var _369=dojo.lang.nameAnonFunc(args[1],dj_global);
ao.srcFunc=_369;
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
this._kwConnectImpl=function(_371,_372){
var fn=(_372)?"disconnect":"connect";
if(typeof _371["srcFunc"]=="function"){
_371.srcObj=_371["srcObj"]||dj_global;
var _374=dojo.lang.nameAnonFunc(_371.srcFunc,_371.srcObj);
_371.srcFunc=_374;
}
if(typeof _371["adviceFunc"]=="function"){
_371.adviceObj=_371["adviceObj"]||dj_global;
var _374=dojo.lang.nameAnonFunc(_371.adviceFunc,_371.adviceObj);
_371.adviceFunc=_374;
}
return dojo.event[fn]((_371["type"]||_371["adviceType"]||"after"),_371["srcObj"]||dj_global,_371["srcFunc"],_371["adviceObj"]||_371["targetObj"]||dj_global,_371["adviceFunc"]||_371["targetFunc"],_371["aroundObj"],_371["aroundFunc"],_371["once"],_371["delay"],_371["rate"],_371["adviceMsg"]||false);
};
this.kwConnect=function(_375){
return this._kwConnectImpl(_375,false);
};
this.disconnect=function(){
var ao=interpolateArgs(arguments);
if(!ao.adviceFunc){
return;
}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);
return mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);
};
this.kwDisconnect=function(_378){
return this._kwConnectImpl(_378,true);
};
};
dojo.event.MethodInvocation=function(_379,obj,args){
this.jp_=_379;
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
dojo.event.MethodJoinPoint=function(obj,_381){
this.object=obj||dj_global;
this.methodname=_381;
this.methodfunc=this.object[_381];
this.before=[];
this.after=[];
this.around=[];
};
dojo.event.MethodJoinPoint.getForMethod=function(obj,_383){
if(!obj){
obj=dj_global;
}
if(!obj[_383]){
obj[_383]=function(){
};
}else{
if((!dojo.lang.isFunction(obj[_383]))&&(!dojo.lang.isAlien(obj[_383]))){
return null;
}
}
var _384=_383+"$joinpoint";
var _385=_383+"$joinpoint$method";
var _386=obj[_384];
if(!_386){
var _387=false;
if(dojo.event["browser"]){
if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){
_387=true;
dojo.event.browser.addClobberNodeAttrs(obj,[_384,_385,_383]);
}
}
obj[_385]=obj[_383];
_386=obj[_384]=new dojo.event.MethodJoinPoint(obj,_385);
obj[_383]=function(){
var args=[];
if((_387)&&(!arguments.length)){
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
if((x==0)&&(_387)&&(dojo.event.browser.isEvent(arguments[x]))){
args.push(dojo.event.browser.fixEvent(arguments[x]));
}else{
args.push(arguments[x]);
}
}
}
return _386.run.apply(_386,args);
};
}
return _386;
};
dojo.lang.extend(dojo.event.MethodJoinPoint,{unintercept:function(){
this.object[this.methodname]=this.methodfunc;
},run:function(){
var obj=this.object||dj_global;
var args=arguments;
var _38d=[];
for(var x=0;x<args.length;x++){
_38d[x]=args[x];
}
var _38f=function(marr){
if(!marr){
dojo.debug("Null argument to unrollAdvice()");
return;
}
var _391=marr[0]||dj_global;
var _392=marr[1];
if(!_391[_392]){
dojo.raise("function \""+_392+"\" does not exist on \""+_391+"\"");
}
var _393=marr[2]||dj_global;
var _394=marr[3];
var msg=marr[6];
var _396;
var to={args:[],jp_:this,object:obj,proceed:function(){
return _391[_392].apply(_391,to.args);
}};
to.args=_38d;
var _398=parseInt(marr[4]);
var _399=((!isNaN(_398))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));
if(marr[5]){
var rate=parseInt(marr[5]);
var cur=new Date();
var _39c=false;
if((marr["last"])&&((cur-marr.last)<=rate)){
if(dojo.event.canTimeout){
if(marr["delayTimer"]){
clearTimeout(marr.delayTimer);
}
var tod=parseInt(rate*2);
var mcpy=dojo.lang.shallowCopy(marr);
marr.delayTimer=setTimeout(function(){
mcpy[5]=0;
_38f(mcpy);
},tod);
}
return;
}else{
marr.last=cur;
}
}
if(_394){
_393[_394].call(_393,to);
}else{
if((_399)&&((dojo.render.html)||(dojo.render.svg))){
dj_global["setTimeout"](function(){
if(msg){
_391[_392].call(_391,to);
}else{
_391[_392].apply(_391,args);
}
},_398);
}else{
if(msg){
_391[_392].call(_391,to);
}else{
_391[_392].apply(_391,args);
}
}
}
};
if(this.before.length>0){
dojo.lang.forEach(this.before,_38f,true);
}
var _39f;
if(this.around.length>0){
var mi=new dojo.event.MethodInvocation(this,obj,args);
_39f=mi.proceed();
}else{
if(this.methodfunc){
_39f=this.object[this.methodname].apply(this.object,args);
}
}
if(this.after.length>0){
dojo.lang.forEach(this.after,_38f,true);
}
return (this.methodfunc)?_39f:null;
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
},addAdvice:function(_3a4,_3a5,_3a6,_3a7,_3a8,_3a9,once,_3ab,rate,_3ad){
var arr=this.getArr(_3a8);
if(!arr){
dojo.raise("bad this: "+this);
}
var ao=[_3a4,_3a5,_3a6,_3a7,_3ab,rate,_3ad];
if(once){
if(this.hasAdvice(_3a4,_3a5,_3a8,arr)>=0){
return;
}
}
if(_3a9=="first"){
arr.unshift(ao);
}else{
arr.push(ao);
}
},hasAdvice:function(_3b0,_3b1,_3b2,arr){
if(!arr){
arr=this.getArr(_3b2);
}
var ind=-1;
for(var x=0;x<arr.length;x++){
if((arr[x][0]==_3b0)&&(arr[x][1]==_3b1)){
ind=x;
}
}
return ind;
},removeAdvice:function(_3b6,_3b7,_3b8,once){
var arr=this.getArr(_3b8);
var ind=this.hasAdvice(_3b6,_3b7,_3b8,arr);
if(ind==-1){
return false;
}
while(ind!=-1){
arr.splice(ind,1);
if(once){
break;
}
ind=this.hasAdvice(_3b6,_3b7,_3b8,arr);
}
return true;
}});
dojo.require("dojo.event");
dojo.provide("dojo.event.topic");
dojo.event.topic=new function(){
this.topics={};
this.getTopic=function(_3bc){
if(!this.topics[_3bc]){
this.topics[_3bc]=new this.TopicImpl(_3bc);
}
return this.topics[_3bc];
};
this.registerPublisher=function(_3bd,obj,_3bf){
var _3bd=this.getTopic(_3bd);
_3bd.registerPublisher(obj,_3bf);
};
this.subscribe=function(_3c0,obj,_3c2){
var _3c0=this.getTopic(_3c0);
_3c0.subscribe(obj,_3c2);
};
this.unsubscribe=function(_3c3,obj,_3c5){
var _3c3=this.getTopic(_3c3);
_3c3.unsubscribe(obj,_3c5);
};
this.publish=function(_3c6,_3c7){
var _3c6=this.getTopic(_3c6);
var args=[];
if(arguments.length==2&&(dojo.lang.isArray(_3c7)||_3c7.callee)){
args=_3c7;
}else{
var args=[];
for(var x=1;x<arguments.length;x++){
args.push(arguments[x]);
}
}
_3c6.sendMessage.apply(_3c6,args);
};
};
dojo.event.topic.TopicImpl=function(_3ca){
this.topicName=_3ca;
var self=this;
self.subscribe=function(_3cc,_3cd){
var tf=_3cd||_3cc;
var to=(!_3cd)?dj_global:_3cc;
dojo.event.kwConnect({srcObj:self,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
self.unsubscribe=function(_3d0,_3d1){
var tf=(!_3d1)?_3d0:_3d1;
var to=(!_3d1)?null:_3d0;
dojo.event.kwDisconnect({srcObj:self,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
self.registerPublisher=function(_3d4,_3d5){
dojo.event.connect(_3d4,_3d5,self,"sendMessage");
};
self.sendMessage=function(_3d6){
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
this.clobber=function(_3d9){
var na;
var tna;
if(_3d9){
tna=_3d9.getElementsByTagName("*");
na=[_3d9];
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
var _3dd={};
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
var _3e1=0;
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
this.addClobberNodeAttrs=function(node,_3e5){
this.addClobberNode(node);
for(var x=0;x<_3e5.length;x++){
node.__clobberAttrs__.push(_3e5[x]);
}
};
this.removeListener=function(node,_3e8,fp,_3ea){
if(!_3ea){
var _3ea=false;
}
_3e8=_3e8.toLowerCase();
if(_3e8.substr(0,2)=="on"){
_3e8=_3e8.substr(2);
}
if(node.removeEventListener){
node.removeEventListener(_3e8,fp,_3ea);
}
};
this.addListener=function(node,_3ec,fp,_3ee,_3ef){
if(!node){
return;
}
if(!_3ee){
var _3ee=false;
}
_3ec=_3ec.toLowerCase();
if(_3ec.substr(0,2)!="on"){
_3ec="on"+_3ec;
}
if(!_3ef){
var _3f0=function(evt){
if(!evt){
evt=window.event;
}
var ret=fp(dojo.event.browser.fixEvent(evt));
if(_3ee){
dojo.event.browser.stopEvent(evt);
}
return ret;
};
}else{
_3f0=fp;
}
if(node.addEventListener){
node.addEventListener(_3ec.substr(2),_3f0,_3ee);
return _3f0;
}else{
if(typeof node[_3ec]=="function"){
var _3f3=node[_3ec];
node[_3ec]=function(e){
_3f3(e);
return _3f0(e);
};
}else{
node[_3ec]=_3f0;
}
if(dojo.render.html.ie){
this.addClobberNodeAttrs(node,[_3ec]);
}
return _3f0;
}
};
this.isEvent=function(obj){
return (typeof obj!="undefined")&&(typeof Event!="undefined")&&(obj.eventPhase);
};
this.currentEvent=null;
this.callListener=function(_3f6,_3f7){
if(typeof _3f6!="function"){
dojo.raise("listener not a function: "+_3f6);
}
dojo.event.browser.currentEvent.currentTarget=_3f7;
return _3f6.call(_3f7,dojo.event.browser.currentEvent);
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
dojo.provide("dojo.fx.html");
dojo.require("dojo.style");
dojo.require("dojo.lang.func");
dojo.require("dojo.animation");
dojo.require("dojo.event.*");
dojo.require("dojo.graphics.color");
dojo.fx.duration=300;
dojo.fx.html._makeFadeable=function(node){
if(dojo.render.html.ie){
if((node.style.zoom.length==0)&&(dojo.style.getStyle(node,"zoom")=="normal")){
node.style.zoom="1";
}
if((node.style.width.length==0)&&(dojo.style.getStyle(node,"width")=="auto")){
node.style.width="auto";
}
}
};
dojo.fx.html.fadeOut=function(node,_3fe,_3ff,_400){
return dojo.fx.html.fade(node,_3fe,dojo.style.getOpacity(node),0,_3ff,_400);
};
dojo.fx.html.fadeIn=function(node,_402,_403,_404){
return dojo.fx.html.fade(node,_402,dojo.style.getOpacity(node),1,_403,_404);
};
dojo.fx.html.fadeHide=function(node,_406,_407,_408){
node=dojo.byId(node);
if(!_406){
_406=150;
}
return dojo.fx.html.fadeOut(node,_406,function(node){
node.style.display="none";
if(typeof _407=="function"){
_407(node);
}
});
};
dojo.fx.html.fadeShow=function(node,_40b,_40c,_40d){
node=dojo.byId(node);
if(!_40b){
_40b=150;
}
node.style.display="block";
return dojo.fx.html.fade(node,_40b,0,1,_40c,_40d);
};
dojo.fx.html.fade=function(node,_40f,_410,_411,_412,_413){
node=dojo.byId(node);
dojo.fx.html._makeFadeable(node);
var anim=new dojo.animation.Animation(new dojo.math.curves.Line([_410],[_411]),_40f||dojo.fx.duration,0);
dojo.event.connect(anim,"onAnimate",function(e){
dojo.style.setOpacity(node,e.x);
});
if(_412){
dojo.event.connect(anim,"onEnd",function(e){
_412(node,anim);
});
}
if(!_413){
anim.play(true);
}
return anim;
};
dojo.fx.html.slideTo=function(node,_418,_419,_41a,_41b){
if(!dojo.lang.isNumber(_418)){
var tmp=_418;
_418=_419;
_419=tmp;
}
node=dojo.byId(node);
var top=node.offsetTop;
var left=node.offsetLeft;
var pos=dojo.style.getComputedStyle(node,"position");
if(pos=="relative"||pos=="static"){
top=parseInt(dojo.style.getComputedStyle(node,"top"))||0;
left=parseInt(dojo.style.getComputedStyle(node,"left"))||0;
}
return dojo.fx.html.slide(node,_418,[left,top],_419,_41a,_41b);
};
dojo.fx.html.slideBy=function(node,_421,_422,_423,_424){
if(!dojo.lang.isNumber(_421)){
var tmp=_421;
_421=_422;
_422=tmp;
}
node=dojo.byId(node);
var top=node.offsetTop;
var left=node.offsetLeft;
var pos=dojo.style.getComputedStyle(node,"position");
if(pos=="relative"||pos=="static"){
top=parseInt(dojo.style.getComputedStyle(node,"top"))||0;
left=parseInt(dojo.style.getComputedStyle(node,"left"))||0;
}
return dojo.fx.html.slideTo(node,_421,[left+_422[0],top+_422[1]],_423,_424);
};
dojo.fx.html.slide=function(node,_42a,_42b,_42c,_42d,_42e){
if(!dojo.lang.isNumber(_42a)){
var tmp=_42a;
_42a=_42c;
_42c=_42b;
_42b=tmp;
}
node=dojo.byId(node);
if(dojo.style.getComputedStyle(node,"position")=="static"){
node.style.position="relative";
}
var anim=new dojo.animation.Animation(new dojo.math.curves.Line(_42b,_42c),_42a||dojo.fx.duration,0);
dojo.event.connect(anim,"onAnimate",function(e){
with(node.style){
left=e.x+"px";
top=e.y+"px";
}
});
if(_42d){
dojo.event.connect(anim,"onEnd",function(e){
_42d(node,anim);
});
}
if(!_42e){
anim.play(true);
}
return anim;
};
dojo.fx.html.colorFadeIn=function(node,_434,_435,_436,_437,_438){
if(!dojo.lang.isNumber(_434)){
var tmp=_434;
_434=_435;
_435=tmp;
}
node=dojo.byId(node);
var _43a=dojo.style.getBackgroundColor(node);
var bg=dojo.style.getStyle(node,"background-color").toLowerCase();
var _43c=bg=="transparent"||bg=="rgba(0, 0, 0, 0)";
while(_43a.length>3){
_43a.pop();
}
var rgb=new dojo.graphics.color.Color(_435).toRgb();
var anim=dojo.fx.html.colorFade(node,_434||dojo.fx.duration,_435,_43a,_437,true);
dojo.event.connect(anim,"onEnd",function(e){
if(_43c){
node.style.backgroundColor="transparent";
}
});
if(_436>0){
node.style.backgroundColor="rgb("+rgb.join(",")+")";
if(!_438){
setTimeout(function(){
anim.play(true);
},_436);
}
}else{
if(!_438){
anim.play(true);
}
}
return anim;
};
dojo.fx.html.highlight=dojo.fx.html.colorFadeIn;
dojo.fx.html.colorFadeFrom=dojo.fx.html.colorFadeIn;
dojo.fx.html.colorFadeOut=function(node,_441,_442,_443,_444,_445){
if(!dojo.lang.isNumber(_441)){
var tmp=_441;
_441=_442;
_442=tmp;
}
node=dojo.byId(node);
var _447=new dojo.graphics.color.Color(dojo.style.getBackgroundColor(node)).toRgb();
var rgb=new dojo.graphics.color.Color(_442).toRgb();
var anim=dojo.fx.html.colorFade(node,_441||dojo.fx.duration,_447,rgb,_444,_443>0||_445);
if(_443>0){
node.style.backgroundColor="rgb("+_447.join(",")+")";
if(!_445){
setTimeout(function(){
anim.play(true);
},_443);
}
}
return anim;
};
dojo.fx.html.unhighlight=dojo.fx.html.colorFadeOut;
dojo.fx.html.colorFadeTo=dojo.fx.html.colorFadeOut;
dojo.fx.html.colorFade=function(node,_44b,_44c,_44d,_44e,_44f){
if(!dojo.lang.isNumber(_44b)){
var tmp=_44b;
_44b=_44d;
_44d=_44c;
_44c=tmp;
}
node=dojo.byId(node);
var _451=new dojo.graphics.color.Color(_44c).toRgb();
var _452=new dojo.graphics.color.Color(_44d).toRgb();
var anim=new dojo.animation.Animation(new dojo.math.curves.Line(_451,_452),_44b||dojo.fx.duration,0);
dojo.event.connect(anim,"onAnimate",function(e){
node.style.backgroundColor="rgb("+e.coordsAsInts().join(",")+")";
});
if(_44e){
dojo.event.connect(anim,"onEnd",function(e){
_44e(node,anim);
});
}
if(!_44f){
anim.play(true);
}
return anim;
};
dojo.fx.html.wipeIn=function(node,_457,_458,_459){
node=dojo.byId(node);
var _45a=dojo.style.getStyle(node,"overflow");
if(_45a=="visible"){
node.style.overflow="hidden";
}
node.style.height=0;
dojo.style.show(node);
var anim=dojo.fx.html.wipe(node,_457,0,node.scrollHeight,null,true);
dojo.event.connect(anim,"onEnd",function(){
node.style.overflow=_45a;
node.style.height="auto";
if(_458){
_458(node,anim);
}
});
if(!_459){
anim.play();
}
return anim;
};
dojo.fx.html.wipeOut=function(node,_45d,_45e,_45f){
node=dojo.byId(node);
var _460=dojo.style.getStyle(node,"overflow");
if(_460=="visible"){
node.style.overflow="hidden";
}
var anim=dojo.fx.html.wipe(node,_45d,node.offsetHeight,0,null,true);
dojo.event.connect(anim,"onEnd",function(){
dojo.style.hide(node);
node.style.overflow=_460;
if(_45e){
_45e(node,anim);
}
});
if(!_45f){
anim.play();
}
return anim;
};
dojo.fx.html.wipe=function(node,_463,_464,_465,_466,_467){
node=dojo.byId(node);
var anim=new dojo.animation.Animation([[_464],[_465]],_463||dojo.fx.duration,0);
dojo.event.connect(anim,"onAnimate",function(e){
node.style.height=e.x+"px";
});
dojo.event.connect(anim,"onEnd",function(){
if(_466){
_466(node,anim);
}
});
if(!_467){
anim.play();
}
return anim;
};
dojo.fx.html.wiper=function(node,_46b){
this.node=dojo.byId(node);
if(_46b){
dojo.event.connect(dojo.byId(_46b),"onclick",this,"toggle");
}
};
dojo.lang.extend(dojo.fx.html.wiper,{duration:dojo.fx.duration,_anim:null,toggle:function(){
if(!this._anim){
var type="wipe"+(dojo.style.isVisible(this.node)?"Hide":"Show");
this._anim=dojo.fx[type](this.node,this.duration,dojo.lang.hitch(this,"_callback"));
}
},_callback:function(){
this._anim=null;
}});
dojo.fx.html.explode=function(_46d,_46e,_46f,_470,_471){
var _472=dojo.style.toCoordinateArray(_46d);
var _473=document.createElement("div");
with(_473.style){
position="absolute";
border="1px solid black";
display="none";
}
document.body.appendChild(_473);
_46e=dojo.byId(_46e);
with(_46e.style){
visibility="hidden";
display="block";
}
var _474=dojo.style.toCoordinateArray(_46e);
with(_46e.style){
display="none";
visibility="visible";
}
var anim=new dojo.animation.Animation(new dojo.math.curves.Line(_472,_474),_46f||dojo.fx.duration,0);
dojo.event.connect(anim,"onBegin",function(e){
_473.style.display="block";
});
dojo.event.connect(anim,"onAnimate",function(e){
with(_473.style){
left=e.x+"px";
top=e.y+"px";
width=e.coords[2]+"px";
height=e.coords[3]+"px";
}
});
dojo.event.connect(anim,"onEnd",function(){
_46e.style.display="block";
_473.parentNode.removeChild(_473);
if(_470){
_470(_46e,anim);
}
});
if(!_471){
anim.play();
}
return anim;
};
dojo.fx.html.implode=function(_478,end,_47a,_47b,_47c){
var _47d=dojo.style.toCoordinateArray(_478);
var _47e=dojo.style.toCoordinateArray(end);
_478=dojo.byId(_478);
var _47f=document.createElement("div");
with(_47f.style){
position="absolute";
border="1px solid black";
display="none";
}
document.body.appendChild(_47f);
var anim=new dojo.animation.Animation(new dojo.math.curves.Line(_47d,_47e),_47a||dojo.fx.duration,0);
dojo.event.connect(anim,"onBegin",function(e){
_478.style.display="none";
_47f.style.display="block";
});
dojo.event.connect(anim,"onAnimate",function(e){
with(_47f.style){
left=e.x+"px";
top=e.y+"px";
width=e.coords[2]+"px";
height=e.coords[3]+"px";
}
});
dojo.event.connect(anim,"onEnd",function(){
_47f.parentNode.removeChild(_47f);
if(_47b){
_47b(_478,anim);
}
});
if(!_47c){
anim.play();
}
return anim;
};
dojo.fx.html.Exploder=function(_483,_484){
_483=dojo.byId(_483);
_484=dojo.byId(_484);
var _485=this;
this.waitToHide=500;
this.timeToShow=100;
this.waitToShow=200;
this.timeToHide=70;
this.autoShow=false;
this.autoHide=false;
var _486=null;
var _487=null;
var _488=null;
var _489=null;
var _48a=null;
var _48b=null;
this.showing=false;
this.onBeforeExplode=null;
this.onAfterExplode=null;
this.onBeforeImplode=null;
this.onAfterImplode=null;
this.onExploding=null;
this.onImploding=null;
this.timeShow=function(){
clearTimeout(_488);
_488=setTimeout(_485.show,_485.waitToShow);
};
this.show=function(){
clearTimeout(_488);
clearTimeout(_489);
if((_487&&_487.status()=="playing")||(_486&&_486.status()=="playing")||_485.showing){
return;
}
if(typeof _485.onBeforeExplode=="function"){
_485.onBeforeExplode(_483,_484);
}
_486=dojo.fx.html.explode(_483,_484,_485.timeToShow,function(e){
_485.showing=true;
if(typeof _485.onAfterExplode=="function"){
_485.onAfterExplode(_483,_484);
}
});
if(typeof _485.onExploding=="function"){
dojo.event.connect(_486,"onAnimate",this,"onExploding");
}
};
this.timeHide=function(){
clearTimeout(_488);
clearTimeout(_489);
if(_485.showing){
_489=setTimeout(_485.hide,_485.waitToHide);
}
};
this.hide=function(){
clearTimeout(_488);
clearTimeout(_489);
if(_486&&_486.status()=="playing"){
return;
}
_485.showing=false;
if(typeof _485.onBeforeImplode=="function"){
_485.onBeforeImplode(_483,_484);
}
_487=dojo.fx.html.implode(_484,_483,_485.timeToHide,function(e){
if(typeof _485.onAfterImplode=="function"){
_485.onAfterImplode(_483,_484);
}
});
if(typeof _485.onImploding=="function"){
dojo.event.connect(_487,"onAnimate",this,"onImploding");
}
};
dojo.event.connect(_483,"onclick",function(e){
if(_485.showing){
_485.hide();
}else{
_485.show();
}
});
dojo.event.connect(_483,"onmouseover",function(e){
if(_485.autoShow){
_485.timeShow();
}
});
dojo.event.connect(_483,"onmouseout",function(e){
if(_485.autoHide){
_485.timeHide();
}
});
dojo.event.connect(_484,"onmouseover",function(e){
clearTimeout(_489);
});
dojo.event.connect(_484,"onmouseout",function(e){
if(_485.autoHide){
_485.timeHide();
}
});
dojo.event.connect(document.documentElement||document.body,"onclick",function(e){
function isDesc(node,_495){
while(node){
if(node==_495){
return true;
}
node=node.parentNode;
}
return false;
}
if(_485.autoHide&&_485.showing&&!isDesc(e.target,_484)&&!isDesc(e.target,_483)){
_485.hide();
}
});
return this;
};
dojo.lang.mixin(dojo.fx,dojo.fx.html);
dojo.hostenv.conditionalLoadModule({browser:["dojo.fx.html"],dashboard:["dojo.fx.html"]});
dojo.hostenv.moduleLoaded("dojo.fx.*");
dojo.provide("dojo.logging.Logger");
dojo.provide("dojo.log");
dojo.require("dojo.lang");
dojo.logging.Record=function(lvl,msg){
this.level=lvl;
this.message=msg;
this.time=new Date();
};
dojo.logging.LogFilter=function(_498){
this.passChain=_498||"";
this.filter=function(_499){
return true;
};
};
dojo.logging.Logger=function(){
this.cutOffLevel=0;
this.propagate=true;
this.parent=null;
this.data=[];
this.filters=[];
this.handlers=[];
};
dojo.lang.extend(dojo.logging.Logger,{argsToArr:function(args){
var ret=[];
for(var x=0;x<args.length;x++){
ret.push(args[x]);
}
return ret;
},setLevel:function(lvl){
this.cutOffLevel=parseInt(lvl);
},isEnabledFor:function(lvl){
return parseInt(lvl)>=this.cutOffLevel;
},getEffectiveLevel:function(){
if((this.cutOffLevel==0)&&(this.parent)){
return this.parent.getEffectiveLevel();
}
return this.cutOffLevel;
},addFilter:function(flt){
this.filters.push(flt);
return this.filters.length-1;
},removeFilterByIndex:function(_4a0){
if(this.filters[_4a0]){
delete this.filters[_4a0];
return true;
}
return false;
},removeFilter:function(_4a1){
for(var x=0;x<this.filters.length;x++){
if(this.filters[x]===_4a1){
delete this.filters[x];
return true;
}
}
return false;
},removeAllFilters:function(){
this.filters=[];
},filter:function(rec){
for(var x=0;x<this.filters.length;x++){
if((this.filters[x]["filter"])&&(!this.filters[x].filter(rec))||(rec.level<this.cutOffLevel)){
return false;
}
}
return true;
},addHandler:function(hdlr){
this.handlers.push(hdlr);
return this.handlers.length-1;
},handle:function(rec){
if((!this.filter(rec))||(rec.level<this.cutOffLevel)){
return false;
}
for(var x=0;x<this.handlers.length;x++){
if(this.handlers[x]["handle"]){
this.handlers[x].handle(rec);
}
}
return true;
},log:function(lvl,msg){
if((this.propagate)&&(this.parent)&&(this.parent.rec.level>=this.cutOffLevel)){
this.parent.log(lvl,msg);
return false;
}
this.handle(new dojo.logging.Record(lvl,msg));
return true;
},debug:function(msg){
return this.logType("DEBUG",this.argsToArr(arguments));
},info:function(msg){
return this.logType("INFO",this.argsToArr(arguments));
},warning:function(msg){
return this.logType("WARNING",this.argsToArr(arguments));
},error:function(msg){
return this.logType("ERROR",this.argsToArr(arguments));
},critical:function(msg){
return this.logType("CRITICAL",this.argsToArr(arguments));
},exception:function(msg,e,_4b1){
if(e){
var _4b2=[e.name,(e.description||e.message)];
if(e.fileName){
_4b2.push(e.fileName);
_4b2.push("line "+e.lineNumber);
}
msg+=" "+_4b2.join(" : ");
}
this.logType("ERROR",msg);
if(!_4b1){
throw e;
}
},logType:function(type,args){
var na=[dojo.logging.log.getLevel(type)];
if(typeof args=="array"){
na=na.concat(args);
}else{
if((typeof args=="object")&&(args["length"])){
na=na.concat(this.argsToArr(args));
}else{
na=na.concat(this.argsToArr(arguments).slice(1));
}
}
return this.log.apply(this,na);
}});
void (function(){
var _4b6=dojo.logging.Logger.prototype;
_4b6.warn=_4b6.warning;
_4b6.err=_4b6.error;
_4b6.crit=_4b6.critical;
})();
dojo.logging.LogHandler=function(_4b7){
this.cutOffLevel=(_4b7)?_4b7:0;
this.formatter=null;
this.data=[];
this.filters=[];
};
dojo.logging.LogHandler.prototype.setFormatter=function(fmtr){
dj_unimplemented("setFormatter");
};
dojo.logging.LogHandler.prototype.flush=function(){
dj_unimplemented("flush");
};
dojo.logging.LogHandler.prototype.close=function(){
dj_unimplemented("close");
};
dojo.logging.LogHandler.prototype.handleError=function(){
dj_unimplemented("handleError");
};
dojo.logging.LogHandler.prototype.handle=function(_4b9){
if((this.filter(_4b9))&&(_4b9.level>=this.cutOffLevel)){
this.emit(_4b9);
}
};
dojo.logging.LogHandler.prototype.emit=function(_4ba){
dj_unimplemented("emit");
};
void (function(){
var _4bb=["setLevel","addFilter","removeFilterByIndex","removeFilter","removeAllFilters","filter"];
var tgt=dojo.logging.LogHandler.prototype;
var src=dojo.logging.Logger.prototype;
for(var x=0;x<_4bb.length;x++){
tgt[_4bb[x]]=src[_4bb[x]];
}
})();
dojo.logging.log=new dojo.logging.Logger();
dojo.logging.log.levels=[{"name":"DEBUG","level":1},{"name":"INFO","level":2},{"name":"WARNING","level":3},{"name":"ERROR","level":4},{"name":"CRITICAL","level":5}];
dojo.logging.log.loggers={};
dojo.logging.log.getLogger=function(name){
if(!this.loggers[name]){
this.loggers[name]=new dojo.logging.Logger();
this.loggers[name].parent=this;
}
return this.loggers[name];
};
dojo.logging.log.getLevelName=function(lvl){
for(var x=0;x<this.levels.length;x++){
if(this.levels[x].level==lvl){
return this.levels[x].name;
}
}
return null;
};
dojo.logging.log.addLevelName=function(name,lvl){
if(this.getLevelName(name)){
this.err("could not add log level "+name+" because a level with that name already exists");
return false;
}
this.levels.append({"name":name,"level":parseInt(lvl)});
return true;
};
dojo.logging.log.getLevel=function(name){
for(var x=0;x<this.levels.length;x++){
if(this.levels[x].name.toUpperCase()==name.toUpperCase()){
return this.levels[x].level;
}
}
return null;
};
dojo.logging.MemoryLogHandler=function(_4c6,_4c7,_4c8,_4c9){
dojo.logging.LogHandler.call(this,_4c6);
this.numRecords=(typeof djConfig["loggingNumRecords"]!="undefined")?djConfig["loggingNumRecords"]:((_4c7)?_4c7:-1);
this.postType=(typeof djConfig["loggingPostType"]!="undefined")?djConfig["loggingPostType"]:(_4c8||-1);
this.postInterval=(typeof djConfig["loggingPostInterval"]!="undefined")?djConfig["loggingPostInterval"]:(_4c8||-1);
};
dojo.logging.MemoryLogHandler.prototype=new dojo.logging.LogHandler();
dojo.logging.MemoryLogHandler.prototype.emit=function(_4ca){
this.data.push(_4ca);
if(this.numRecords!=-1){
while(this.data.length>this.numRecords){
this.data.shift();
}
}
};
dojo.logging.logQueueHandler=new dojo.logging.MemoryLogHandler(0,50,0,10000);
dojo.logging.logQueueHandler.emit=function(_4cb){
var _4cc=String(dojo.log.getLevelName(_4cb.level)+": "+_4cb.time.toLocaleTimeString())+": "+_4cb.message;
if(!dj_undef("debug",dj_global)){
dojo.debug(_4cc);
}else{
if((typeof dj_global["print"]=="function")&&(!dojo.render.html.capable)){
print(_4cc);
}
}
this.data.push(_4cb);
if(this.numRecords!=-1){
while(this.data.length>this.numRecords){
this.data.shift();
}
}
};
dojo.logging.log.addHandler(dojo.logging.logQueueHandler);
dojo.log=dojo.logging.log;
dojo.hostenv.conditionalLoadModule({common:["dojo.logging.Logger",false,false],rhino:["dojo.logging.RhinoLogger"]});
dojo.hostenv.moduleLoaded("dojo.logging.*");
dojo.provide("dojo.io.IO");
dojo.require("dojo.string");
dojo.require("dojo.lang.extras");
dojo.io.transports=[];
dojo.io.hdlrFuncNames=["load","error"];
dojo.io.Request=function(url,_4ce,_4cf,_4d0){
if((arguments.length==1)&&(arguments[0].constructor==Object)){
this.fromKwArgs(arguments[0]);
}else{
this.url=url;
if(_4ce){
this.mimetype=_4ce;
}
if(_4cf){
this.transport=_4cf;
}
if(arguments.length>=4){
this.changeUrl=_4d0;
}
}
};
dojo.lang.extend(dojo.io.Request,{url:"",mimetype:"text/plain",method:"GET",content:undefined,transport:undefined,changeUrl:undefined,formNode:undefined,sync:false,bindSuccess:false,useCache:false,preventCache:false,load:function(type,data,evt){
},error:function(type,_4d5){
},handle:function(){
},abort:function(){
},fromKwArgs:function(_4d6){
if(_4d6["url"]){
_4d6.url=_4d6.url.toString();
}
if(_4d6["formNode"]){
_4d6.formNode=dojo.byId(_4d6.formNode);
}
if(!_4d6["method"]&&_4d6["formNode"]&&_4d6["formNode"].method){
_4d6.method=_4d6["formNode"].method;
}
if(!_4d6["handle"]&&_4d6["handler"]){
_4d6.handle=_4d6.handler;
}
if(!_4d6["load"]&&_4d6["loaded"]){
_4d6.load=_4d6.loaded;
}
if(!_4d6["changeUrl"]&&_4d6["changeURL"]){
_4d6.changeUrl=_4d6.changeURL;
}
_4d6.encoding=dojo.lang.firstValued(_4d6["encoding"],djConfig["bindEncoding"],"");
_4d6.sendTransport=dojo.lang.firstValued(_4d6["sendTransport"],djConfig["ioSendTransport"],false);
var _4d7=dojo.lang.isFunction;
for(var x=0;x<dojo.io.hdlrFuncNames.length;x++){
var fn=dojo.io.hdlrFuncNames[x];
if(_4d7(_4d6[fn])){
continue;
}
if(_4d7(_4d6["handle"])){
_4d6[fn]=_4d6.handle;
}
}
dojo.lang.mixin(this,_4d6);
}});
dojo.io.Error=function(msg,type,num){
this.message=msg;
this.type=type||"unknown";
this.number=num||0;
};
dojo.io.transports.addTransport=function(name){
this.push(name);
this[name]=dojo.io[name];
};
dojo.io.bind=function(_4de){
if(!(_4de instanceof dojo.io.Request)){
try{
_4de=new dojo.io.Request(_4de);
}
catch(e){
dojo.debug(e);
}
}
var _4df="";
if(_4de["transport"]){
_4df=_4de["transport"];
if(!this[_4df]){
return _4de;
}
}else{
for(var x=0;x<dojo.io.transports.length;x++){
var tmp=dojo.io.transports[x];
if((this[tmp])&&(this[tmp].canHandle(_4de))){
_4df=tmp;
}
}
if(_4df==""){
return _4de;
}
}
this[_4df].bind(_4de);
_4de.bindSuccess=true;
return _4de;
};
dojo.io.queueBind=function(_4e2){
if(!(_4e2 instanceof dojo.io.Request)){
try{
_4e2=new dojo.io.Request(_4e2);
}
catch(e){
dojo.debug(e);
}
}
var _4e3=_4e2.load;
_4e2.load=function(){
dojo.io._queueBindInFlight=false;
var ret=_4e3.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
var _4e5=_4e2.error;
_4e2.error=function(){
dojo.io._queueBindInFlight=false;
var ret=_4e5.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
dojo.io._bindQueue.push(_4e2);
dojo.io._dispatchNextQueueBind();
return _4e2;
};
dojo.io._dispatchNextQueueBind=function(){
if(!dojo.io._queueBindInFlight){
dojo.io._queueBindInFlight=true;
dojo.io.bind(dojo.io._bindQueue.shift());
}
};
dojo.io._bindQueue=[];
dojo.io._queueBindInFlight=false;
dojo.io.argsFromMap=function(map,_4e8){
var _4e9=new Object();
var _4ea="";
var enc=/utf/i.test(_4e8||"")?encodeURIComponent:dojo.string.encodeAscii;
for(var x in map){
if(!_4e9[x]){
_4ea+=enc(x)+"="+enc(map[x])+"&";
}
}
return _4ea;
};
dojo.provide("dojo.io.BrowserIO");
dojo.require("dojo.io");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.func");
dojo.require("dojo.string.extras");
dojo.require("dojo.dom");
try{
if((!djConfig["preventBackButtonFix"])&&(!dojo.hostenv.post_load_)){
document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"'></iframe>");
}
}
catch(e){
}
dojo.io.checkChildrenForFile=function(node){
var _4ee=false;
var _4ef=node.getElementsByTagName("input");
dojo.lang.forEach(_4ef,function(_4f0){
if(_4ee){
return;
}
if(_4f0.getAttribute("type")=="file"){
_4ee=true;
}
});
return _4ee;
};
dojo.io.formHasFile=function(_4f1){
return dojo.io.checkChildrenForFile(_4f1);
};
dojo.io.formFilter=function(node){
var type=(node.type||"").toLowerCase();
return !node.disabled&&node.name&&!dojo.lang.inArray(type,["file","submit","image","reset","button"]);
};
dojo.io.encodeForm=function(_4f4,_4f5,_4f6){
if((!_4f4)||(!_4f4.tagName)||(!_4f4.tagName.toLowerCase()=="form")){
dojo.raise("Attempted to encode a non-form element.");
}
if(!_4f6){
_4f6=dojo.io.formFilter;
}
var enc=/utf/i.test(_4f5||"")?encodeURIComponent:dojo.string.encodeAscii;
var _4f8=[];
for(var i=0;i<_4f4.elements.length;i++){
var elm=_4f4.elements[i];
if(!elm||elm.tagName.toLowerCase()=="fieldset"||!_4f6(elm)){
continue;
}
var name=enc(elm.name);
var type=elm.type.toLowerCase();
if(type=="select-multiple"){
for(var j=0;j<elm.options.length;j++){
if(elm.options[j].selected){
_4f8.push(name+"="+enc(elm.options[j].value));
}
}
}else{
if(dojo.lang.inArray(type,["radio","checkbox"])){
if(elm.checked){
_4f8.push(name+"="+enc(elm.value));
}
}else{
_4f8.push(name+"="+enc(elm.value));
}
}
}
var _4fe=_4f4.getElementsByTagName("input");
for(var i=0;i<_4fe.length;i++){
var _4ff=_4fe[i];
if(_4ff.type.toLowerCase()=="image"&&_4ff.form==_4f4&&_4f6(_4ff)){
var name=enc(_4ff.name);
_4f8.push(name+"="+enc(_4ff.value));
_4f8.push(name+".x=0");
_4f8.push(name+".y=0");
}
}
return _4f8.join("&")+"&";
};
dojo.io.setIFrameSrc=function(_500,src,_502){
try{
var r=dojo.render.html;
if(!_502){
if(r.safari){
_500.location=src;
}else{
frames[_500.name].location=src;
}
}else{
var idoc;
if(r.ie){
idoc=_500.contentWindow.document;
}else{
if(r.moz){
idoc=_500.contentWindow;
}else{
if(r.safari){
idoc=_500.document;
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
var _50a=form.getElementsByTagName("input");
for(var i=0;i<_50a.length;i++){
var _50b=_50a[i];
if(_50b.type.toLowerCase()=="image"&&_50b.form==form){
this.connect(_50b,"onclick","click");
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
var _512=false;
if(node.disabled||!node.name){
_512=false;
}else{
if(dojo.lang.inArray(type,["submit","button","image"])){
if(!this.clickedButton){
this.clickedButton=node;
}
_512=node==this.clickedButton;
}else{
_512=!dojo.lang.inArray(type,["file","submit","reset","button"]);
}
}
return _512;
},connect:function(_513,_514,_515){
if(dojo.evalObjPath("dojo.event.connect")){
dojo.event.connect(_513,_514,this,_515);
}else{
var fcn=dojo.lang.hitch(this,_515);
_513[_514]=function(e){
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
var _518=this;
this.initialHref=window.location.href;
this.initialHash=window.location.hash;
this.moveForward=false;
var _519={};
this.useCache=false;
this.preventCache=false;
this.historyStack=[];
this.forwardStack=[];
this.historyIframe=null;
this.bookmarkAnchor=null;
this.locationTimer=null;
function getCacheKey(url,_51b,_51c){
return url+"|"+_51b+"|"+_51c.toLowerCase();
}
function addToCache(url,_51e,_51f,http){
_519[getCacheKey(url,_51e,_51f)]=http;
}
function getFromCache(url,_522,_523){
return _519[getCacheKey(url,_522,_523)];
}
this.clearCache=function(){
_519={};
};
function doLoad(_524,http,url,_527,_528){
if((http.status==200)||(location.protocol=="file:"&&http.status==0)){
var ret;
if(_524.method.toLowerCase()=="head"){
var _52a=http.getAllResponseHeaders();
ret={};
ret.toString=function(){
return _52a;
};
var _52b=_52a.split(/[\r\n]+/g);
for(var i=0;i<_52b.length;i++){
var pair=_52b[i].match(/^([^:]+)\s*:\s*(.+)$/i);
if(pair){
ret[pair[1]]=pair[2];
}
}
}else{
if(_524.mimetype=="text/javascript"){
try{
ret=dj_eval(http.responseText);
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=null;
}
}else{
if(_524.mimetype=="text/json"){
try{
ret=dj_eval("("+http.responseText+")");
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=false;
}
}else{
if((_524.mimetype=="application/xml")||(_524.mimetype=="text/xml")){
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
if(_528){
addToCache(url,_527,_524.method,http);
}
_524[(typeof _524.load=="function")?"load":"handle"]("load",ret,http);
}else{
var _52e=new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);
_524[(typeof _524.error=="function")?"error":"handle"]("error",_52e,http);
}
}
function setHeaders(http,_530){
if(_530["headers"]){
for(var _531 in _530["headers"]){
if(_531.toLowerCase()=="content-type"&&!_530["contentType"]){
_530["contentType"]=_530["headers"][_531];
}else{
http.setRequestHeader(_531,_530["headers"][_531]);
}
}
}
}
this.addToHistory=function(args){
var _533=args["back"]||args["backButton"]||args["handle"];
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
var _536=_533;
var lh=null;
var hsl=this.historyStack.length-1;
if(hsl>=0){
while(!this.historyStack[hsl]["urlHash"]){
hsl--;
}
lh=this.historyStack[hsl]["urlHash"];
}
if(lh){
_533=function(){
if(window.location.hash!=""){
setTimeout("window.location.href = '"+lh+"';",1);
}
_536();
};
}
this.forwardStack=[];
var _539=args["forward"]||args["forwardButton"];
var tfw=function(){
if(window.location.hash!=""){
window.location.href=hash;
}
if(_539){
_539();
}
};
if(args["forward"]){
args.forward=tfw;
}else{
if(args["forwardButton"]){
args.forwardButton=tfw;
}
}
}else{
if(dojo.render.html.moz){
if(!this.locationTimer){
this.locationTimer=setInterval("dojo.io.XMLHTTPTransport.checkLocation();",200);
}
}
}
}
this.historyStack.push({"url":url,"callback":_533,"kwArgs":args,"urlHash":hash});
};
this.checkLocation=function(){
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
};
this.iframeLoaded=function(evt,_53d){
var isp=_53d.href.split("?");
if(isp.length<2){
if(this.historyStack.length==1){
this.handleBackButton();
}
return;
}
var _53f=isp[1];
if(this.moveForward){
this.moveForward=false;
return;
}
var last=this.historyStack.pop();
if(!last){
if(this.forwardStack.length>0){
var next=this.forwardStack[this.forwardStack.length-1];
if(_53f==next.url.split("?")[1]){
this.handleForwardButton();
}
}
return;
}
this.historyStack.push(last);
if(this.historyStack.length>=2){
if(isp[1]==this.historyStack[this.historyStack.length-2].url.split("?")[1]){
this.handleBackButton();
}
}else{
this.handleBackButton();
}
};
this.handleBackButton=function(){
var last=this.historyStack.pop();
if(!last){
return;
}
if(last["callback"]){
last.callback();
}else{
if(last.kwArgs["backButton"]){
last.kwArgs["backButton"]();
}else{
if(last.kwArgs["back"]){
last.kwArgs["back"]();
}else{
if(last.kwArgs["handle"]){
last.kwArgs.handle("back");
}
}
}
}
this.forwardStack.push(last);
};
this.handleForwardButton=function(){
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
};
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
var _546=dojo.hostenv.getXmlhttpObject()?true:false;
this.canHandle=function(_547){
return _546&&dojo.lang.inArray((_547["mimetype"]||"".toLowerCase()),["text/plain","text/html","application/xml","text/xml","text/javascript","text/json"])&&dojo.lang.inArray(_547["method"].toLowerCase(),["post","get","head"])&&!(_547["formNode"]&&dojo.io.formHasFile(_547["formNode"]));
};
this.multipartBoundary="45309FFF-BD65-4d50-99C9-36986896A96F";
this.bind=function(_548){
if(!_548["url"]){
if(!_548["formNode"]&&(_548["backButton"]||_548["back"]||_548["changeUrl"]||_548["watchForURL"])&&(!djConfig.preventBackButtonFix)){
this.addToHistory(_548);
return true;
}
}
var url=_548.url;
var _54a="";
if(_548["formNode"]){
var ta=_548.formNode.getAttribute("action");
if((ta)&&(!_548["url"])){
url=ta;
}
var tp=_548.formNode.getAttribute("method");
if((tp)&&(!_548["method"])){
_548.method=tp;
}
_54a+=dojo.io.encodeForm(_548.formNode,_548.encoding,_548["formFilter"]);
}
if(url.indexOf("#")>-1){
dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);
url=url.split("#")[0];
}
if(_548["file"]){
_548.method="post";
}
if(!_548["method"]){
_548.method="get";
}
if(_548.method.toLowerCase()=="get"){
_548.multipart=false;
}else{
if(_548["file"]){
_548.multipart=true;
}else{
if(!_548["multipart"]){
_548.multipart=false;
}
}
}
if(_548["backButton"]||_548["back"]||_548["changeUrl"]){
this.addToHistory(_548);
}
var _54d=_548["content"]||{};
if(_548.sendTransport){
_54d["dojo.transport"]="xmlhttp";
}
do{
if(_548.postContent){
_54a=_548.postContent;
break;
}
if(_54d){
_54a+=dojo.io.argsFromMap(_54d,_548.encoding);
}
if(_548.method.toLowerCase()=="get"||!_548.multipart){
break;
}
var t=[];
if(_54a.length){
var q=_54a.split("&");
for(var i=0;i<q.length;++i){
if(q[i].length){
var p=q[i].split("=");
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+p[0]+"\"","",p[1]);
}
}
}
if(_548.file){
if(dojo.lang.isArray(_548.file)){
for(var i=0;i<_548.file.length;++i){
var o=_548.file[i];
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}else{
var o=_548.file;
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}
if(t.length){
t.push("--"+this.multipartBoundary+"--","");
_54a=t.join("\r\n");
}
}while(false);
var _553=_548["sync"]?false:true;
var _554=_548["preventCache"]||(this.preventCache==true&&_548["preventCache"]!=false);
var _555=_548["useCache"]==true||(this.useCache==true&&_548["useCache"]!=false);
if(!_554&&_555){
var _556=getFromCache(url,_54a,_548.method);
if(_556){
doLoad(_548,_556,url,_54a,false);
return;
}
}
var http=dojo.hostenv.getXmlhttpObject(_548);
var _558=false;
if(_553){
this.inFlight.push({"req":_548,"http":http,"url":url,"query":_54a,"useCache":_555});
this.startWatchingInFlight();
}
if(_548.method.toLowerCase()=="post"){
http.open("POST",url,_553);
setHeaders(http,_548);
http.setRequestHeader("Content-Type",_548.multipart?("multipart/form-data; boundary="+this.multipartBoundary):(_548.contentType||"application/x-www-form-urlencoded"));
http.send(_54a);
}else{
var _559=url;
if(_54a!=""){
_559+=(_559.indexOf("?")>-1?"&":"?")+_54a;
}
if(_554){
_559+=(dojo.string.endsWithAny(_559,"?","&")?"":(_559.indexOf("?")>-1?"&":"?"))+"dojo.preventCache="+new Date().valueOf();
}
http.open(_548.method.toUpperCase(),_559,_553);
setHeaders(http,_548);
http.send(null);
}
if(!_553){
doLoad(_548,http,url,_54a,_555);
}
_548.abort=function(){
return http.abort();
};
return;
};
dojo.io.transports.addTransport("XMLHTTPTransport");
};
dojo.provide("dojo.io.cookie");
dojo.io.cookie.setCookie=function(name,_55b,days,path,_55e,_55f){
var _560=-1;
if(typeof days=="number"&&days>=0){
var d=new Date();
d.setTime(d.getTime()+(days*24*60*60*1000));
_560=d.toGMTString();
}
_55b=escape(_55b);
document.cookie=name+"="+_55b+";"+(_560!=-1?" expires="+_560+";":"")+(path?"path="+path:"")+(_55e?"; domain="+_55e:"")+(_55f?"; secure":"");
};
dojo.io.cookie.set=dojo.io.cookie.setCookie;
dojo.io.cookie.getCookie=function(name){
var idx=document.cookie.indexOf(name+"=");
if(idx==-1){
return null;
}
value=document.cookie.substring(idx+name.length+1);
var end=value.indexOf(";");
if(end==-1){
end=value.length;
}
value=value.substring(0,end);
value=unescape(value);
return value;
};
dojo.io.cookie.get=dojo.io.cookie.getCookie;
dojo.io.cookie.deleteCookie=function(name){
dojo.io.cookie.setCookie(name,"-",0);
};
dojo.io.cookie.setObjectCookie=function(name,obj,days,path,_56a,_56b,_56c){
if(arguments.length==5){
_56c=_56a;
_56a=null;
_56b=null;
}
var _56d=[],cookie,value="";
if(!_56c){
cookie=dojo.io.cookie.getObjectCookie(name);
}
if(days>=0){
if(!cookie){
cookie={};
}
for(var prop in obj){
if(prop==null){
delete cookie[prop];
}else{
if(typeof obj[prop]=="string"||typeof obj[prop]=="number"){
cookie[prop]=obj[prop];
}
}
}
prop=null;
for(var prop in cookie){
_56d.push(escape(prop)+"="+escape(cookie[prop]));
}
value=_56d.join("&");
}
dojo.io.cookie.setCookie(name,value,days,path,_56a,_56b);
};
dojo.io.cookie.getObjectCookie=function(name){
var _570=null,cookie=dojo.io.cookie.getCookie(name);
if(cookie){
_570={};
var _571=cookie.split("&");
for(var i=0;i<_571.length;i++){
var pair=_571[i].split("=");
var _574=pair[1];
if(isNaN(_574)){
_574=unescape(pair[1]);
}
_570[unescape(pair[0])]=_574;
}
}
return _570;
};
dojo.io.cookie.isSupported=function(){
if(typeof navigator.cookieEnabled!="boolean"){
dojo.io.cookie.setCookie("__TestingYourBrowserForCookieSupport__","CookiesAllowed",90,null);
var _575=dojo.io.cookie.getCookie("__TestingYourBrowserForCookieSupport__");
navigator.cookieEnabled=(_575=="CookiesAllowed");
if(navigator.cookieEnabled){
this.deleteCookie("__TestingYourBrowserForCookieSupport__");
}
}
return navigator.cookieEnabled;
};
if(!dojo.io.cookies){
dojo.io.cookies=dojo.io.cookie;
}
dojo.hostenv.conditionalLoadModule({common:["dojo.io"],rhino:["dojo.io.RhinoIO"],browser:["dojo.io.BrowserIO","dojo.io.cookie"],dashboard:["dojo.io.BrowserIO","dojo.io.cookie"]});
dojo.hostenv.moduleLoaded("dojo.io.*");
dojo.hostenv.conditionalLoadModule({common:["dojo.uri.Uri",false,false]});
dojo.hostenv.moduleLoaded("dojo.uri.*");
dojo.provide("dojo.io.IframeIO");
dojo.require("dojo.io.BrowserIO");
dojo.require("dojo.uri.*");
dojo.io.createIFrame=function(_576,_577){
if(window[_576]){
return window[_576];
}
if(window.frames[_576]){
return window.frames[_576];
}
var r=dojo.render.html;
var _579=null;
var turi=dojo.uri.dojoUri("iframe_history.html?noInit=true");
var _57b=((r.ie)&&(dojo.render.os.win))?"<iframe name='"+_576+"' src='"+turi+"' onload='"+_577+"'>":"iframe";
_579=document.createElement(_57b);
with(_579){
name=_576;
setAttribute("name",_576);
id=_576;
}
(document.body||document.getElementsByTagName("body")[0]).appendChild(_579);
window[_576]=_579;
with(_579.style){
position="absolute";
left=top="0px";
height=width="1px";
visibility="hidden";
}
if(!r.ie){
dojo.io.setIFrameSrc(_579,turi,true);
_579.onload=new Function(_577);
}
return _579;
};
dojo.io.iframeContentWindow=function(_57c){
var win=_57c.contentWindow||dojo.io.iframeContentDocument(_57c).defaultView||dojo.io.iframeContentDocument(_57c).__parent__||(_57c.name&&document.frames[_57c.name])||null;
return win;
};
dojo.io.iframeContentDocument=function(_57e){
var doc=_57e.contentDocument||((_57e.contentWindow)&&(_57e.contentWindow.document))||((_57e.name)&&(document.frames[_57e.name])&&(document.frames[_57e.name].document))||null;
return doc;
};
dojo.io.IframeTransport=new function(){
var _580=this;
this.currentRequest=null;
this.requestQueue=[];
this.iframeName="dojoIoIframe";
this.fireNextRequest=function(){
if((this.currentRequest)||(this.requestQueue.length==0)){
return;
}
var cr=this.currentRequest=this.requestQueue.shift();
cr._contentToClean=[];
var fn=cr["formNode"];
var _583=cr["content"]||{};
if(cr.sendTransport){
_583["dojo.transport"]="iframe";
}
if(fn){
if(_583){
for(var x in _583){
if(!fn[x]){
var tn;
if(dojo.render.html.ie){
tn=document.createElement("<input type='hidden' name='"+x+"' value='"+_583[x]+"'>");
fn.appendChild(tn);
}else{
tn=document.createElement("input");
fn.appendChild(tn);
tn.type="hidden";
tn.name=x;
tn.value=_583[x];
}
cr._contentToClean.push(x);
}else{
fn[x].value=_583[x];
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
var _586=dojo.io.argsFromMap(this.currentRequest.content);
var _587=(cr.url.indexOf("?")>-1?"&":"?")+_586;
dojo.io.setIFrameSrc(this.iframe,_587,true);
}
};
this.canHandle=function(_588){
return ((dojo.lang.inArray(_588["mimetype"],["text/plain","text/html","application/xml","text/xml","text/javascript","text/json"]))&&((_588["formNode"])&&(dojo.io.checkChildrenForFile(_588["formNode"])))&&(dojo.lang.inArray(_588["method"].toLowerCase(),["post","get"]))&&(!((_588["sync"])&&(_588["sync"]==true))));
};
this.bind=function(_589){
this.requestQueue.push(_589);
this.fireNextRequest();
return;
};
this.setUpIframe=function(){
this.iframe=dojo.io.createIFrame(this.iframeName,"dojo.io.IframeTransport.iframeOnload();");
};
this.iframeOnload=function(){
if(!_580.currentRequest){
_580.fireNextRequest();
return;
}
var req=_580.currentRequest;
var _58b=req._contentToClean;
for(var i=0;i<_58b.length;i++){
var key=_58b[i];
var _58e=req.formNode[key];
req.formNode.removeChild(_58e);
req.formNode[key]=null;
}
req.formNode.setAttribute("action",req._originalAction);
req.formNode.setAttribute("target",req._originalTarget);
req.formNode.target=req._originalTarget;
var ifr=_580.iframe;
var ifw=dojo.io.iframeContentWindow(ifr);
var _591;
var _592=false;
try{
var cmt=req.mimetype;
if((cmt=="text/javascript")||(cmt=="text/json")){
var cd=dojo.io.iframeContentDocument(_580.iframe);
var js=cd.getElementsByTagName("textarea")[0].value;
if(cmt=="text/json"){
js="("+js+")";
}
_591=dj_eval(js);
}else{
if((cmt=="application/xml")||(cmt=="text/xml")){
_591=dojo.io.iframeContentDocument(_580.iframe);
}else{
_591=ifw.innerHTML;
}
}
_592=true;
}
catch(e){
var _596=new dojo.io.Error("IframeTransport Error");
if(dojo.lang.isFunction(req["error"])){
req.error("error",_596,req);
}
}
try{
if(_592&&dojo.lang.isFunction(req["load"])){
req.load("load",_591,req);
}
}
catch(e){
throw e;
}
finally{
_580.currentRequest=null;
_580.fireNextRequest();
}
};
dojo.io.transports.addTransport("IframeTransport");
};
dojo.addOnLoad(function(){
dojo.io.IframeTransport.setUpIframe();
});
dojo.provide("dojo.date");
dojo.date.setIso8601=function(_597,_598){
var _599=_598.split("T");
dojo.date.setIso8601Date(_597,_599[0]);
if(_599.length==2){
dojo.date.setIso8601Time(_597,_599[1]);
}
return _597;
};
dojo.date.fromIso8601=function(_59a){
return dojo.date.setIso8601(new Date(0),_59a);
};
dojo.date.setIso8601Date=function(_59b,_59c){
var _59d="^([0-9]{4})((-?([0-9]{2})(-?([0-9]{2}))?)|"+"(-?([0-9]{3}))|(-?W([0-9]{2})(-?([1-7]))?))?$";
var d=_59c.match(new RegExp(_59d));
var year=d[1];
var _5a0=d[4];
var date=d[6];
var _5a2=d[8];
var week=d[10];
var _5a4=(d[12])?d[12]:1;
_59b.setYear(year);
if(_5a2){
dojo.date.setDayOfYear(_59b,Number(_5a2));
}else{
if(week){
_59b.setMonth(0);
_59b.setDate(1);
var gd=_59b.getDay();
var day=(gd)?gd:7;
var _5a7=Number(_5a4)+(7*Number(week));
if(day<=4){
_59b.setDate(_5a7+1-day);
}else{
_59b.setDate(_5a7+8-day);
}
}else{
if(_5a0){
_59b.setDate(1);
_59b.setMonth(_5a0-1);
}
if(date){
_59b.setDate(date);
}
}
}
return _59b;
};
dojo.date.fromIso8601Date=function(_5a8){
return dojo.date.setIso8601Date(new Date(0),_5a8);
};
dojo.date.setIso8601Time=function(_5a9,_5aa){
var _5ab="Z|(([-+])([0-9]{2})(:?([0-9]{2}))?)$";
var d=_5aa.match(new RegExp(_5ab));
var _5ad=0;
if(d){
if(d[0]!="Z"){
_5ad=(Number(d[3])*60)+Number(d[5]);
_5ad*=((d[2]=="-")?1:-1);
}
_5ad-=_5a9.getTimezoneOffset();
_5aa=_5aa.substr(0,_5aa.length-d[0].length);
}
var _5ae="^([0-9]{2})(:?([0-9]{2})(:?([0-9]{2})(.([0-9]+))?)?)?$";
var d=_5aa.match(new RegExp(_5ae));
var _5af=d[1];
var mins=Number((d[3])?d[3]:0)+_5ad;
var secs=(d[5])?d[5]:0;
var ms=d[7]?(Number("0."+d[7])*1000):0;
_5a9.setHours(_5af);
_5a9.setMinutes(mins);
_5a9.setSeconds(secs);
_5a9.setMilliseconds(ms);
return _5a9;
};
dojo.date.fromIso8601Time=function(_5b3){
return dojo.date.setIso8601Time(new Date(0),_5b3);
};
dojo.date.setDayOfYear=function(_5b4,_5b5){
_5b4.setMonth(0);
_5b4.setDate(_5b5);
return _5b4;
};
dojo.date.getDayOfYear=function(_5b6){
var _5b7=new Date("1/1/"+_5b6.getFullYear());
return Math.floor((_5b6.getTime()-_5b7.getTime())/86400000);
};
dojo.date.getWeekOfYear=function(_5b8,_5b9){
if(!_5b9){
_5b9=0;
}
var _5ba=new Date(_5b8.getFullYear(),0,1);
var day=_5ba.getDay();
if(day>_5b9){
_5ba.setDate(_5ba.getDate()-day+_5b9);
}else{
_5ba.setDate(_5ba.getDate()-day+_5b9-7);
}
return Math.floor((_5b8.getTime()-_5ba.getTime())/604800000);
};
dojo.date.daysInMonth=function(_5bc,year){
dojo.deprecated("daysInMonth(month, year)","replaced by getDaysInMonth(dateObject)","0.4");
return dojo.date.getDaysInMonth(new Date(year,_5bc,1));
};
dojo.date.getDaysInMonth=function(_5be){
var _5bf=_5be.getMonth();
var year=_5be.getFullYear();
var days=[31,28,31,30,31,30,31,31,30,31,30,31];
if(_5bf==1&&year){
if((!(year%4)&&(year%100))||(!(year%4)&&!(year%100)&&!(year%400))){
return 29;
}else{
return 28;
}
}else{
return days[_5bf];
}
};
dojo.date.months=["January","February","March","April","May","June","July","August","September","October","November","December"];
dojo.date.shortMonths=["Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"];
dojo.date.days=["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];
dojo.date.shortDays=["Sun","Mon","Tues","Wed","Thur","Fri","Sat"];
dojo.date.toLongDateString=function(date){
return dojo.date.months[date.getMonth()]+" "+date.getDate()+", "+date.getFullYear();
};
dojo.date.toShortDateString=function(date){
return dojo.date.shortMonths[date.getMonth()]+" "+date.getDate()+", "+date.getFullYear();
};
dojo.date.toMilitaryTimeString=function(date){
dojo.deprecated("dojo.date.toMilitaryTimeString","use dojo.date.strftime(date, \"%T\")","0.4");
return dojo.date.strftime(date,"%T");
};
dojo.date.toRelativeString=function(date){
var now=new Date();
var diff=(now-date)/1000;
var end=" ago";
var _5c9=false;
if(diff<0){
_5c9=true;
end=" from now";
diff=-diff;
}
if(diff<60){
diff=Math.round(diff);
return diff+" second"+(diff==1?"":"s")+end;
}else{
if(diff<3600){
diff=Math.round(diff/60);
return diff+" minute"+(diff==1?"":"s")+end;
}else{
if(diff<3600*24&&date.getDay()==now.getDay()){
diff=Math.round(diff/3600);
return diff+" hour"+(diff==1?"":"s")+end;
}else{
if(diff<3600*24*7){
diff=Math.round(diff/(3600*24));
if(diff==1){
return _5c9?"Tomorrow":"Yesterday";
}else{
return diff+" days"+end;
}
}else{
return dojo.date.toShortDateString(date);
}
}
}
}
};
dojo.date.getDayOfWeekName=function(date){
return dojo.date.days[date.getDay()];
};
dojo.date.getShortDayOfWeekName=function(date){
return dojo.date.shortDays[date.getDay()];
};
dojo.date.getMonthName=function(date){
return dojo.date.months[date.getMonth()];
};
dojo.date.getShortMonthName=function(date){
return dojo.date.shortMonths[date.getMonth()];
};
dojo.date.toString=function(date,_5cf){
if(_5cf.indexOf("#d")>-1){
_5cf=_5cf.replace(/#dddd/g,dojo.date.getDayOfWeekName(date));
_5cf=_5cf.replace(/#ddd/g,dojo.date.getShortDayOfWeekName(date));
_5cf=_5cf.replace(/#dd/g,(date.getDate().toString().length==1?"0":"")+date.getDate());
_5cf=_5cf.replace(/#d/g,date.getDate());
}
if(_5cf.indexOf("#M")>-1){
_5cf=_5cf.replace(/#MMMM/g,dojo.date.getMonthName(date));
_5cf=_5cf.replace(/#MMM/g,dojo.date.getShortMonthName(date));
_5cf=_5cf.replace(/#MM/g,((date.getMonth()+1).toString().length==1?"0":"")+(date.getMonth()+1));
_5cf=_5cf.replace(/#M/g,date.getMonth()+1);
}
if(_5cf.indexOf("#y")>-1){
var _5d0=date.getFullYear().toString();
_5cf=_5cf.replace(/#yyyy/g,_5d0);
_5cf=_5cf.replace(/#yy/g,_5d0.substring(2));
_5cf=_5cf.replace(/#y/g,_5d0.substring(3));
}
if(_5cf.indexOf("#")==-1){
return _5cf;
}
if(_5cf.indexOf("#h")>-1){
var _5d1=date.getHours();
_5d1=(_5d1>12?_5d1-12:(_5d1==0)?12:_5d1);
_5cf=_5cf.replace(/#hh/g,(_5d1.toString().length==1?"0":"")+_5d1);
_5cf=_5cf.replace(/#h/g,_5d1);
}
if(_5cf.indexOf("#H")>-1){
_5cf=_5cf.replace(/#HH/g,(date.getHours().toString().length==1?"0":"")+date.getHours());
_5cf=_5cf.replace(/#H/g,date.getHours());
}
if(_5cf.indexOf("#m")>-1){
_5cf=_5cf.replace(/#mm/g,(date.getMinutes().toString().length==1?"0":"")+date.getMinutes());
_5cf=_5cf.replace(/#m/g,date.getMinutes());
}
if(_5cf.indexOf("#s")>-1){
_5cf=_5cf.replace(/#ss/g,(date.getSeconds().toString().length==1?"0":"")+date.getSeconds());
_5cf=_5cf.replace(/#s/g,date.getSeconds());
}
if(_5cf.indexOf("#T")>-1){
_5cf=_5cf.replace(/#TT/g,date.getHours()>=12?"PM":"AM");
_5cf=_5cf.replace(/#T/g,date.getHours()>=12?"P":"A");
}
if(_5cf.indexOf("#t")>-1){
_5cf=_5cf.replace(/#tt/g,date.getHours()>=12?"pm":"am");
_5cf=_5cf.replace(/#t/g,date.getHours()>=12?"p":"a");
}
return _5cf;
};
dojo.date.toSql=function(date,_5d3){
return dojo.date.strftime(date,"%F"+!_5d3?" %T":"");
};
dojo.date.fromSql=function(_5d4){
var _5d5=_5d4.split(/[\- :]/g);
while(_5d5.length<6){
_5d5.push(0);
}
return new Date(_5d5[0],_5d5[1],_5d5[2],_5d5[3],_5d5[4],_5d5[5]);
};
dojo.date.strftime=function(date,_5d7){
var _5d8={"n":"\n","t":"\t","%":"%"};
function $(s){
s=String(s);
while(s.length<2){
s="0"+s;
}
return s;
}
function getProperty(_5da){
if(!_5d8[_5da]){
switch(_5da){
case "a":
_5d8["a"]=dojo.date.getShortDayOfWeekName(date);
break;
case "A":
_5d8["A"]=dojo.date.getDayOfWeekName(date);
break;
case "b":
case "h":
_5da="b";
_5d8["b"]=dojo.date.getShortMonthName(date);
break;
case "B":
_5d8["B"]=dojo.date.getMonthName(date);
break;
case "c":
_5d8["c"]=date.toLocaleString();
break;
case "C":
_5d8["C"]=$(Math.floor(date.getFullYear()/100));
break;
case "d":
_5d8["d"]=$(date.getDate());
break;
case "D":
_5d8["D"]=getProperty("m")+"/"+getProperty("d")+"/"+getProperty("y");
case "e":
var e=String(date.getDate());
if(e.length<2){
e=" "+e;
}
_5d8["e"]=e;
case "F":
_5d8["F"]=getProperty("Y")+"-"+getProperty("m")+"-"+getProperty("d");
case "H":
_5d8["H"]=$(date.getHours());
break;
case "I":
var _5dc=date.getHours();
_5d8["I"]=$(_5dc>12?_5dc-12:_5dc);
break;
case "j":
var j=$(dojo.date.getDayOfYear(date));
if(j.length<3){
j="0"+j;
}
_5d8["j"]=j;
break;
case "m":
_5d8["m"]=$(date.getMonth()+1);
break;
case "M":
_5d8["M"]=$(date.getMinutes());
break;
case "p":
_5d8["p"]=date.getHours()<12?"am":"pm";
case "r":
_5d8["r"]=getProperty("I")+":"+getProperty("M")+":"+getProperty("S")+" "+getProperty("p");
case "R":
_5d8["R"]=getProperty("H")+":"+getProperty("M");
case "S":
_5d8["S"]=$(date.getSeconds());
break;
case "T":
_5d8["T"]=getProperty("H")+":"+getProperty("M")+":"+getProperty("S");
case "u":
var day=date.getDay();
_5d8["u"]=$(day==0?7:day);
break;
case "U":
_5d8["U"]=$(dojo.date.getWeekOfYear(date));
break;
case "V":
break;
case "W":
_5d8["W"]=$(dojo.date.getWeekOfYear(date,1));
break;
case "w":
_5d8["w"]=$(date.getDay());
break;
case "y":
var y=date.getFullYear();
_5d8["y"]=$(y-Math.floor(y/100)*100);
case "Y":
_5d8["Y"]=String(date.getFullYear());
break;
}
}
return _5d8[_5da];
}
var str="";
var i=0,index=0;
while((index=_5d7.indexOf("%",i))!=-1){
str+=_5d7.substring(i,index);
str+=getProperty(_5d7.charAt(index+1));
i=index+2;
}
str+=_5d7.substring(i);
return str;
};
dojo.provide("dojo.string.Builder");
dojo.require("dojo.string");
dojo.string.Builder=function(str){
this.arrConcat=(dojo.render.html.capable&&dojo.render.html["ie"]);
var a=[];
var b=str||"";
var _5e5=this.length=b.length;
if(this.arrConcat){
if(b.length>0){
a.push(b);
}
b="";
}
this.toString=this.valueOf=function(){
return (this.arrConcat)?a.join(""):b;
};
this.append=function(s){
if(this.arrConcat){
a.push(s);
}else{
b+=s;
}
_5e5+=s.length;
this.length=_5e5;
return this;
};
this.clear=function(){
a=[];
b="";
_5e5=this.length=0;
return this;
};
this.remove=function(f,l){
var s="";
if(this.arrConcat){
b=a.join("");
}
a=[];
if(f>0){
s=b.substring(0,(f-1));
}
b=s+b.substring(f+l);
_5e5=this.length=b.length;
if(this.arrConcat){
a.push(b);
b="";
}
return this;
};
this.replace=function(o,n){
if(this.arrConcat){
b=a.join("");
}
a=[];
b=b.replace(o,n);
_5e5=this.length=b.length;
if(this.arrConcat){
a.push(b);
b="";
}
return this;
};
this.insert=function(idx,s){
if(this.arrConcat){
b=a.join("");
}
a=[];
if(idx==0){
b=s+b;
}else{
var t=b.split("");
t.splice(idx,0,s);
b=t.join("");
}
_5e5=this.length=b.length;
if(this.arrConcat){
a.push(b);
b="";
}
return this;
};
};
dojo.hostenv.conditionalLoadModule({common:["dojo.string","dojo.string.common","dojo.string.extras","dojo.string.Builder"]});
dojo.hostenv.moduleLoaded("dojo.string.*");
if(!this["dojo"]){
alert("\"dojo/__package__.js\" is now located at \"dojo/dojo.js\". Please update your includes accordingly");
}
dojo.provide("dojo.AdapterRegistry");
dojo.require("dojo.lang.func");
dojo.require("dojo.lang.type");
dojo.AdapterRegistry=function(){
this.pairs=[];
};
dojo.lang.extend(dojo.AdapterRegistry,{register:function(name,_5f0,wrap,_5f2){
if(_5f2){
this.pairs.unshift([name,_5f0,wrap]);
}else{
this.pairs.push([name,_5f0,wrap]);
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
dojo.lang.registerRepr=function(name,_5f9,wrap,_5fb){
dojo.lang.reprRegistry.register(name,_5f9,wrap,_5fb);
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
dojo.provide("dojo.json");
dojo.require("dojo.lang.func");
dojo.require("dojo.lang.repr");
dojo.require("dojo.AdapterRegistry");
dojo.json={jsonRegistry:new dojo.AdapterRegistry(),register:function(name,_604,wrap,_606){
dojo.json.jsonRegistry.register(name,_604,wrap,_606);
},evalJson:function(json){
try{
return eval("("+json+")");
}
catch(e){
dojo.debug(e);
return json;
}
},evalJSON:dojo.lang.forward("evalJson"),serialize:function(o){
var _609=typeof (o);
if(_609=="undefined"){
return "undefined";
}else{
if((_609=="number")||(_609=="boolean")){
return o+"";
}else{
if(o===null){
return "null";
}
}
}
var m=dojo.lang;
if(_609=="string"){
return m.reprString(o);
}
var me=arguments.callee;
var _60c;
if(typeof (o.__json__)=="function"){
_60c=o.__json__();
if(o!==_60c){
return me(_60c);
}
}
if(typeof (o.json)=="function"){
_60c=o.json();
if(o!==_60c){
return me(_60c);
}
}
if(_609!="function"&&typeof (o.length)=="number"){
var res=[];
for(var i=0;i<o.length;i++){
var val=me(o[i]);
if(typeof (val)!="string"){
val="undefined";
}
res.push(val);
}
return "["+res.join(",")+"]";
}
try{
window.o=o;
_60c=dojo.json.jsonRegistry.match(o);
return me(_60c);
}
catch(e){
}
if(_609=="function"){
return null;
}
res=[];
for(var k in o){
var _611;
if(typeof (k)=="number"){
_611="\""+k+"\"";
}else{
if(typeof (k)=="string"){
_611=m.reprString(k);
}else{
continue;
}
}
val=me(o[k]);
if(typeof (val)!="string"){
continue;
}
res.push(_611+":"+val);
}
return "{"+res.join(",")+"}";
}};
dojo.provide("dojo.rpc.Deferred");
dojo.require("dojo.lang.func");
dojo.rpc.Deferred=function(_612){
this.chain=[];
this.id=this._nextId();
this.fired=-1;
this.paused=0;
this.results=[null,null];
this.canceller=_612;
this.silentlyCancelled=false;
};
dojo.lang.extend(dojo.rpc.Deferred,{getFunctionFromArgs:function(){
var a=arguments;
if((a[0])&&(!a[1])){
if(dojo.lang.isFunction(a[0])){
return a[0];
}else{
if(dojo.lang.isString(a[0])){
return dj_global[a[0]];
}
}
}else{
if((a[0])&&(a[1])){
return dojo.lang.hitch(a[0],a[1]);
}
}
return null;
},repr:function(){
var _614;
if(this.fired==-1){
_614="unfired";
}else{
if(this.fired==0){
_614="success";
}else{
_614="error";
}
}
return "Deferred("+this.id+", "+_614+")";
},toString:dojo.lang.forward("repr"),_nextId:(function(){
var n=1;
return function(){
return n++;
};
})(),cancel:function(){
if(this.fired==-1){
if(this.canceller){
this.canceller(this);
}else{
this.silentlyCancelled=true;
}
if(this.fired==-1){
this.errback(new Error(this.repr()));
}
}else{
if((this.fired==0)&&(this.results[0] instanceof dojo.rpc.Deferred)){
this.results[0].cancel();
}
}
},_pause:function(){
this.paused++;
},_unpause:function(){
this.paused--;
if((this.paused==0)&&(this.fired>=0)){
this._fire();
}
},_continue:function(res){
this._resback(res);
this._unpause();
},_resback:function(res){
this.fired=((res instanceof Error)?1:0);
this.results[this.fired]=res;
this._fire();
},_check:function(){
if(this.fired!=-1){
if(!this.silentlyCancelled){
dojo.raise("already called!");
}
this.silentlyCancelled=false;
return;
}
},callback:function(res){
this._check();
this._resback(res);
},errback:function(res){
this._check();
if(!(res instanceof Error)){
res=new Error(res);
}
this._resback(res);
},addBoth:function(cb,cbfn){
var _61c=this.getFunctionFromArgs(cb,cbfn);
if(arguments.length>2){
_61c=dojo.lang.curryArguments(null,_61c,arguments,2);
}
return this.addCallbacks(_61c,_61c);
},addCallback:function(cb,cbfn){
var _61f=this.getFunctionFromArgs(cb,cbfn);
if(arguments.length>2){
_61f=dojo.lang.curryArguments(null,_61f,arguments,2);
}
return this.addCallbacks(_61f,null);
},addErrback:function(cb,cbfn){
var _622=this.getFunctionFromArgs(cb,cbfn);
if(arguments.length>2){
_622=dojo.lang.curryArguments(null,_622,arguments,2);
}
return this.addCallbacks(null,_622);
return this.addCallbacks(null,fn);
},addCallbacks:function(cb,eb){
this.chain.push([cb,eb]);
if(this.fired>=0){
this._fire();
}
return this;
},_fire:function(){
var _625=this.chain;
var _626=this.fired;
var res=this.results[_626];
var self=this;
var cb=null;
while(_625.length>0&&this.paused==0){
var pair=_625.shift();
var f=pair[_626];
if(f==null){
continue;
}
try{
res=f(res);
_626=((res instanceof Error)?1:0);
if(res instanceof dojo.rpc.Deferred){
cb=function(res){
self._continue(res);
};
this._pause();
}
}
catch(err){
_626=1;
res=err;
}
}
this.fired=_626;
this.results[_626]=res;
if((cb)&&(this.paused)){
res.addBoth(cb);
}
}});
dojo.provide("dojo.rpc.RpcService");
dojo.require("dojo.io.*");
dojo.require("dojo.json");
dojo.require("dojo.lang.func");
dojo.require("dojo.rpc.Deferred");
dojo.rpc.RpcService=function(url){
if(url){
this.connect(url);
}
};
dojo.lang.extend(dojo.rpc.RpcService,{strictArgChecks:true,serviceUrl:"",parseResults:function(obj){
return obj;
},errorCallback:function(_62f){
return function(type,obj,e){
_62f.errback(e);
};
},resultCallback:function(_633){
var tf=dojo.lang.hitch(this,function(type,obj,e){
var _638=this.parseResults(obj);
_633.callback(_638);
});
return tf;
},generateMethod:function(_639,_63a){
var _63b=this;
return function(){
var _63c=new dojo.rpc.Deferred();
if((!_63b.strictArgChecks)||((_63a!=null)&&(arguments.length!=_63a.length))){
dojo.raise("Invalid number of parameters for remote method.");
}else{
_63b.bind(_639,arguments,_63c);
}
return _63c;
};
},processSmd:function(_63d){
dojo.debug("RpcService: Processing returned SMD.");
for(var n=0;n<_63d.methods.length;n++){
dojo.debug("RpcService: Creating Method: this.",_63d.methods[n].name,"()");
this[_63d.methods[n].name]=this.generateMethod(_63d.methods[n].name,_63d.methods[n].parameters);
if(dojo.lang.isFunction(this[_63d.methods[n].name])){
dojo.debug("RpcService: Successfully created",_63d.methods[n].name,"()");
}else{
dojo.debug("RpcService: Failed to create",_63d.methods[n].name,"()");
}
}
this.serviceUrl=_63d.serviceUrl||_63d.serviceURL;
dojo.debug("RpcService: Dojo RpcService is ready for use.");
},connect:function(_63f){
dojo.debug("RpcService: Attempting to load SMD document from:",_63f);
dojo.io.bind({url:_63f,mimetype:"text/json",load:dojo.lang.hitch(this,function(type,_641,e){
return this.processSmd(_641);
}),sync:true});
}});
dojo.provide("dojo.rpc.JsonService");
dojo.require("dojo.rpc.RpcService");
dojo.require("dojo.io.*");
dojo.require("dojo.json");
dojo.require("dojo.lang");
dojo.rpc.JsonService=function(args){
if(args){
if(dojo.lang.isString(args)){
this.connect(args);
}else{
if(args["smdUrl"]){
this.connect(args.smdUrl);
}
if(args["smdStr"]){
this.processSmd(dj_eval("("+args.smdStr+")"));
}
if(args["smdObj"]){
this.processSmd(args.smdObj);
}
if(args["serviceUrl"]){
this.serviceUrl=args.serviceUrl;
}
if(args["strictArgChecks"]){
this.strictArgChecks=args.strictArgChecks;
}
}
}
};
dojo.inherits(dojo.rpc.JsonService,dojo.rpc.RpcService);
dojo.lang.extend(dojo.rpc.JsonService,{bustCache:false,lastSubmissionId:0,callRemote:function(_644,_645){
var _646=new dojo.rpc.Deferred();
this.bind(_644,_645,_646);
return _646;
},bind:function(_647,_648,_649){
dojo.io.bind({url:this.serviceUrl,postContent:this.createRequest(_647,_648),method:"POST",mimetype:"text/json",load:this.resultCallback(_649),preventCache:this.bustCache});
},createRequest:function(_64a,_64b){
var req={"params":_64b,"method":_64a,"id":this.lastSubmissionId++};
var data=dojo.json.serialize(req);
dojo.debug("JsonService: JSON-RPC Request: "+data);
return data;
},parseResults:function(obj){
if(obj["result"]){
return obj["result"];
}else{
return obj;
}
}});
dojo.hostenv.conditionalLoadModule({common:["dojo.rpc.JsonService",false,false]});
dojo.hostenv.moduleLoaded("dojo.rpc.*");
dojo.provide("dojo.xml.Parse");
dojo.require("dojo.dom");
dojo.xml.Parse=function(){
this.parseFragment=function(_64f){
var _650={};
var _651=dojo.dom.getTagName(_64f);
_650[_651]=new Array(_64f.tagName);
var _652=this.parseAttributes(_64f);
for(var attr in _652){
if(!_650[attr]){
_650[attr]=[];
}
_650[attr][_650[attr].length]=_652[attr];
}
var _654=_64f.childNodes;
for(var _655 in _654){
switch(_654[_655].nodeType){
case dojo.dom.ELEMENT_NODE:
_650[_651].push(this.parseElement(_654[_655]));
break;
case dojo.dom.TEXT_NODE:
if(_654.length==1){
if(!_650[_64f.tagName]){
_650[_651]=[];
}
_650[_651].push({value:_654[0].nodeValue});
}
break;
}
}
return _650;
};
this.parseElement=function(node,_657,_658,_659){
var _65a={};
var _65b=dojo.dom.getTagName(node);
_65a[_65b]=[];
if((!_658)||(_65b.substr(0,4).toLowerCase()=="dojo")){
var _65c=this.parseAttributes(node);
for(var attr in _65c){
if((!_65a[_65b][attr])||(typeof _65a[_65b][attr]!="array")){
_65a[_65b][attr]=[];
}
_65a[_65b][attr].push(_65c[attr]);
}
_65a[_65b].nodeRef=node;
_65a.tagName=_65b;
_65a.index=_659||0;
}
var _65e=0;
for(var i=0;i<node.childNodes.length;i++){
var tcn=node.childNodes.item(i);
switch(tcn.nodeType){
case dojo.dom.ELEMENT_NODE:
_65e++;
var ctn=dojo.dom.getTagName(tcn);
if(!_65a[ctn]){
_65a[ctn]=[];
}
_65a[ctn].push(this.parseElement(tcn,true,_658,_65e));
if((tcn.childNodes.length==1)&&(tcn.childNodes.item(0).nodeType==dojo.dom.TEXT_NODE)){
_65a[ctn][_65a[ctn].length-1].value=tcn.childNodes.item(0).nodeValue;
}
break;
case dojo.dom.TEXT_NODE:
if(node.childNodes.length==1){
_65a[_65b].push({value:node.childNodes.item(0).nodeValue});
}
break;
default:
break;
}
}
return _65a;
};
this.parseAttributes=function(node){
var _663={};
var atts=node.attributes;
for(var i=0;i<atts.length;i++){
var _666=atts.item(i);
if((dojo.render.html.capable)&&(dojo.render.html.ie)){
if(!_666){
continue;
}
if((typeof _666=="object")&&(typeof _666.nodeValue=="undefined")||(_666.nodeValue==null)||(_666.nodeValue=="")){
continue;
}
}
var nn=(_666.nodeName.indexOf("dojo:")==-1)?_666.nodeName:_666.nodeName.split("dojo:")[1];
_663[nn]={value:_666.nodeValue};
}
return _663;
};
};
dojo.provide("dojo.xml.domUtil");
dojo.require("dojo.graphics.color");
dojo.require("dojo.dom");
dojo.require("dojo.style");
dj_deprecated("dojo.xml.domUtil is deprecated, use dojo.dom instead");
dojo.xml.domUtil=new function(){
this.nodeTypes={ELEMENT_NODE:1,ATTRIBUTE_NODE:2,TEXT_NODE:3,CDATA_SECTION_NODE:4,ENTITY_REFERENCE_NODE:5,ENTITY_NODE:6,PROCESSING_INSTRUCTION_NODE:7,COMMENT_NODE:8,DOCUMENT_NODE:9,DOCUMENT_TYPE_NODE:10,DOCUMENT_FRAGMENT_NODE:11,NOTATION_NODE:12};
this.dojoml="http://www.dojotoolkit.org/2004/dojoml";
this.idIncrement=0;
this.getTagName=function(){
return dojo.dom.getTagName.apply(dojo.dom,arguments);
};
this.getUniqueId=function(){
return dojo.dom.getUniqueId.apply(dojo.dom,arguments);
};
this.getFirstChildTag=function(){
return dojo.dom.getFirstChildElement.apply(dojo.dom,arguments);
};
this.getLastChildTag=function(){
return dojo.dom.getLastChildElement.apply(dojo.dom,arguments);
};
this.getNextSiblingTag=function(){
return dojo.dom.getNextSiblingElement.apply(dojo.dom,arguments);
};
this.getPreviousSiblingTag=function(){
return dojo.dom.getPreviousSiblingElement.apply(dojo.dom,arguments);
};
this.forEachChildTag=function(node,_669){
var _66a=this.getFirstChildTag(node);
while(_66a){
if(_669(_66a)=="break"){
break;
}
_66a=this.getNextSiblingTag(_66a);
}
};
this.moveChildren=function(){
return dojo.dom.moveChildren.apply(dojo.dom,arguments);
};
this.copyChildren=function(){
return dojo.dom.copyChildren.apply(dojo.dom,arguments);
};
this.clearChildren=function(){
return dojo.dom.removeChildren.apply(dojo.dom,arguments);
};
this.replaceChildren=function(){
return dojo.dom.replaceChildren.apply(dojo.dom,arguments);
};
this.getStyle=function(){
return dojo.style.getStyle.apply(dojo.style,arguments);
};
this.toCamelCase=function(){
return dojo.style.toCamelCase.apply(dojo.style,arguments);
};
this.toSelectorCase=function(){
return dojo.style.toSelectorCase.apply(dojo.style,arguments);
};
this.getAncestors=function(){
return dojo.dom.getAncestors.apply(dojo.dom,arguments);
};
this.isChildOf=function(){
return dojo.dom.isDescendantOf.apply(dojo.dom,arguments);
};
this.createDocumentFromText=function(){
return dojo.dom.createDocumentFromText.apply(dojo.dom,arguments);
};
if(dojo.render.html.capable||dojo.render.svg.capable){
this.createNodesFromText=function(txt,wrap){
return dojo.dom.createNodesFromText.apply(dojo.dom,arguments);
};
}
this.extractRGB=function(_66d){
return dojo.graphics.color.extractRGB(_66d);
};
this.hex2rgb=function(hex){
return dojo.graphics.color.hex2rgb(hex);
};
this.rgb2hex=function(r,g,b){
return dojo.graphics.color.rgb2hex(r,g,b);
};
this.insertBefore=function(){
return dojo.dom.insertBefore.apply(dojo.dom,arguments);
};
this.before=this.insertBefore;
this.insertAfter=function(){
return dojo.dom.insertAfter.apply(dojo.dom,arguments);
};
this.after=this.insertAfter;
this.insert=function(){
return dojo.dom.insertAtPosition.apply(dojo.dom,arguments);
};
this.insertAtIndex=function(){
return dojo.dom.insertAtIndex.apply(dojo.dom,arguments);
};
this.textContent=function(){
return dojo.dom.textContent.apply(dojo.dom,arguments);
};
this.renderedTextContent=function(){
return dojo.dom.renderedTextContent.apply(dojo.dom,arguments);
};
this.remove=function(node){
return dojo.dom.removeNode.apply(dojo.dom,arguments);
};
};
dojo.provide("dojo.xml.htmlUtil");
dojo.require("dojo.html");
dojo.require("dojo.style");
dojo.require("dojo.dom");
dj_deprecated("dojo.xml.htmlUtil is deprecated, use dojo.html instead");
dojo.xml.htmlUtil=new function(){
this.styleSheet=dojo.style.styleSheet;
this._clobberSelection=function(){
return dojo.html.clearSelection.apply(dojo.html,arguments);
};
this.disableSelect=function(){
return dojo.html.disableSelection.apply(dojo.html,arguments);
};
this.enableSelect=function(){
return dojo.html.enableSelection.apply(dojo.html,arguments);
};
this.getInnerWidth=function(){
return dojo.style.getInnerWidth.apply(dojo.style,arguments);
};
this.getOuterWidth=function(node){
dj_unimplemented("dojo.xml.htmlUtil.getOuterWidth");
};
this.getInnerHeight=function(){
return dojo.style.getInnerHeight.apply(dojo.style,arguments);
};
this.getOuterHeight=function(node){
dj_unimplemented("dojo.xml.htmlUtil.getOuterHeight");
};
this.getTotalOffset=function(){
return dojo.style.getTotalOffset.apply(dojo.style,arguments);
};
this.totalOffsetLeft=function(){
return dojo.style.totalOffsetLeft.apply(dojo.style,arguments);
};
this.getAbsoluteX=this.totalOffsetLeft;
this.totalOffsetTop=function(){
return dojo.style.totalOffsetTop.apply(dojo.style,arguments);
};
this.getAbsoluteY=this.totalOffsetTop;
this.getEventTarget=function(){
return dojo.html.getEventTarget.apply(dojo.html,arguments);
};
this.getScrollTop=function(){
return dojo.html.getScrollTop.apply(dojo.html,arguments);
};
this.getScrollLeft=function(){
return dojo.html.getScrollLeft.apply(dojo.html,arguments);
};
this.evtTgt=this.getEventTarget;
this.getParentOfType=function(){
return dojo.html.getParentOfType.apply(dojo.html,arguments);
};
this.getAttribute=function(){
return dojo.html.getAttribute.apply(dojo.html,arguments);
};
this.getAttr=function(node,attr){
dj_deprecated("dojo.xml.htmlUtil.getAttr is deprecated, use dojo.xml.htmlUtil.getAttribute instead");
return dojo.xml.htmlUtil.getAttribute(node,attr);
};
this.hasAttribute=function(){
return dojo.html.hasAttribute.apply(dojo.html,arguments);
};
this.hasAttr=function(node,attr){
dj_deprecated("dojo.xml.htmlUtil.hasAttr is deprecated, use dojo.xml.htmlUtil.hasAttribute instead");
return dojo.xml.htmlUtil.hasAttribute(node,attr);
};
this.getClass=function(){
return dojo.html.getClass.apply(dojo.html,arguments);
};
this.hasClass=function(){
return dojo.html.hasClass.apply(dojo.html,arguments);
};
this.prependClass=function(){
return dojo.html.prependClass.apply(dojo.html,arguments);
};
this.addClass=function(){
return dojo.html.addClass.apply(dojo.html,arguments);
};
this.setClass=function(){
return dojo.html.setClass.apply(dojo.html,arguments);
};
this.removeClass=function(){
return dojo.html.removeClass.apply(dojo.html,arguments);
};
this.classMatchType={ContainsAll:0,ContainsAny:1,IsOnly:2};
this.getElementsByClass=function(){
return dojo.html.getElementsByClass.apply(dojo.html,arguments);
};
this.getElementsByClassName=this.getElementsByClass;
this.setOpacity=function(){
return dojo.style.setOpacity.apply(dojo.style,arguments);
};
this.getOpacity=function(){
return dojo.style.getOpacity.apply(dojo.style,arguments);
};
this.clearOpacity=function(){
return dojo.style.clearOpacity.apply(dojo.style,arguments);
};
this.gravity=function(){
return dojo.html.gravity.apply(dojo.html,arguments);
};
this.gravity.NORTH=1;
this.gravity.SOUTH=1<<1;
this.gravity.EAST=1<<2;
this.gravity.WEST=1<<3;
this.overElement=function(){
return dojo.html.overElement.apply(dojo.html,arguments);
};
this.insertCssRule=function(){
return dojo.style.insertCssRule.apply(dojo.style,arguments);
};
this.insertCSSRule=function(_679,_67a,_67b){
dj_deprecated("dojo.xml.htmlUtil.insertCSSRule is deprecated, use dojo.xml.htmlUtil.insertCssRule instead");
return dojo.xml.htmlUtil.insertCssRule(_679,_67a,_67b);
};
this.removeCssRule=function(){
return dojo.style.removeCssRule.apply(dojo.style,arguments);
};
this.removeCSSRule=function(_67c){
dj_deprecated("dojo.xml.htmlUtil.removeCSSRule is deprecated, use dojo.xml.htmlUtil.removeCssRule instead");
return dojo.xml.htmlUtil.removeCssRule(_67c);
};
this.insertCssFile=function(){
return dojo.style.insertCssFile.apply(dojo.style,arguments);
};
this.insertCSSFile=function(URI,doc,_67f){
dj_deprecated("dojo.xml.htmlUtil.insertCSSFile is deprecated, use dojo.xml.htmlUtil.insertCssFile instead");
return dojo.xml.htmlUtil.insertCssFile(URI,doc,_67f);
};
this.getBackgroundColor=function(){
return dojo.style.getBackgroundColor.apply(dojo.style,arguments);
};
this.getUniqueId=function(){
return dojo.dom.getUniqueId();
};
this.getStyle=function(){
return dojo.style.getStyle.apply(dojo.style,arguments);
};
};
dojo.require("dojo.xml.Parse");
dojo.hostenv.conditionalLoadModule({common:["dojo.xml.domUtil"],browser:["dojo.xml.htmlUtil"],dashboard:["dojo.xml.htmlUtil"],svg:["dojo.xml.svgUtil"]});
dojo.hostenv.moduleLoaded("dojo.xml.*");
dojo.provide("dojo.lang.assert");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.type");
dojo.lang.assert=function(_680,_681){
if(!_680){
var _682="An assert statement failed.\n"+"The method dojo.lang.assert() was called with a 'false' value.\n";
if(_681){
_682+="Here's the assert message:\n"+_681+"\n";
}
throw new Error(_682);
}
};
dojo.lang.assertType=function(_683,type,_685){
if(!dojo.lang.isOfType(_683,type)){
if(!_685){
if(!dojo.lang.assertType._errorMessage){
dojo.lang.assertType._errorMessage="Type mismatch: dojo.lang.assertType() failed.";
}
_685=dojo.lang.assertType._errorMessage;
}
dojo.lang.assert(false,_685);
}
};
dojo.lang.assertValidKeywords=function(_686,_687,_688){
var key;
if(!_688){
if(!dojo.lang.assertValidKeywords._errorMessage){
dojo.lang.assertValidKeywords._errorMessage="In dojo.lang.assertValidKeywords(), found invalid keyword:";
}
_688=dojo.lang.assertValidKeywords._errorMessage;
}
if(dojo.lang.isArray(_687)){
for(key in _686){
if(!dojo.lang.inArray(_687,key)){
dojo.lang.assert(false,_688+" "+key);
}
}
}else{
for(key in _686){
if(!(key in _687)){
dojo.lang.assert(false,_688+" "+key);
}
}
}
};
dojo.hostenv.conditionalLoadModule({common:["dojo.lang","dojo.lang.common","dojo.lang.assert","dojo.lang.array","dojo.lang.type","dojo.lang.func","dojo.lang.extras","dojo.lang.repr"]});
dojo.hostenv.moduleLoaded("dojo.lang.*");
dojo.require("dojo.lang.*");
dojo.provide("dojo.storage");
dojo.provide("dojo.storage.StorageProvider");
dojo.storage=new function(){
this.provider=null;
this.setProvider=function(obj){
this.provider=obj;
};
this.set=function(key,_68c,_68d){
if(!this.provider){
return false;
}
return this.provider.set(key,_68c,_68d);
};
this.get=function(key,_68f){
if(!this.provider){
return false;
}
return this.provider.get(key,_68f);
};
this.remove=function(key,_691){
return this.provider.remove(key,_691);
};
};
dojo.storage.StorageProvider=function(){
};
dojo.lang.extend(dojo.storage.StorageProvider,{namespace:"*",initialized:false,free:function(){
dojo.unimplemented("dojo.storage.StorageProvider.free");
return 0;
},freeK:function(){
return dojo.math.round(this.free()/1024,0);
},set:function(key,_693,_694){
dojo.unimplemented("dojo.storage.StorageProvider.set");
},get:function(key,_696){
dojo.unimplemented("dojo.storage.StorageProvider.get");
},remove:function(key,_698,_699){
dojo.unimplemented("dojo.storage.StorageProvider.set");
}});
dojo.provide("dojo.storage.browser");
dojo.require("dojo.storage");
dojo.require("dojo.uri.*");
dojo.storage.browser.StorageProvider=function(){
this.initialized=false;
this.flash=null;
this.backlog=[];
};
dojo.inherits(dojo.storage.browser.StorageProvider,dojo.storage.StorageProvider);
dojo.lang.extend(dojo.storage.browser.StorageProvider,{storageOnLoad:function(){
this.initialized=true;
this.hideStore();
while(this.backlog.length){
this.set.apply(this,this.backlog.shift());
}
},unHideStore:function(){
var _69a=dojo.byId("dojo-storeContainer");
with(_69a.style){
position="absolute";
overflow="visible";
width="215px";
height="138px";
left="30px";
top="30px";
visiblity="visible";
zIndex="20";
border="1px solid black";
}
},hideStore:function(_69b){
var _69c=dojo.byId("dojo-storeContainer");
with(_69c.style){
left="-300px";
top="-300px";
}
},set:function(key,_69e,ns){
if(!this.initialized){
this.backlog.push([key,_69e,ns]);
return "pending";
}
return this.flash.set(key,_69e,ns||this.namespace);
},get:function(key,ns){
return this.flash.get(key,ns||this.namespace);
},writeStorage:function(){
var _6a2=dojo.uri.dojoUri("src/storage/Storage.swf").toString();
var _6a3=["<div id=\"dojo-storeContainer\"","style=\"position: absolute; left: -300px; top: -300px;\">"];
if(dojo.render.html.ie){
_6a3.push("<object");
_6a3.push("\tstyle=\"border: 1px solid black;\"");
_6a3.push("\tclassid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\"");
_6a3.push("\tcodebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0\"");
_6a3.push("\twidth=\"215\" height=\"138\" id=\"dojoStorage\">");
_6a3.push("\t<param name=\"movie\" value=\""+_6a2+"\">");
_6a3.push("\t<param name=\"quality\" value=\"high\">");
_6a3.push("</object>");
}else{
_6a3.push("<embed src=\""+_6a2+"\" width=\"215\" height=\"138\" ");
_6a3.push("\tquality=\"high\" ");
_6a3.push("\tpluginspage=\"http://www.macromedia.com/go/getflashplayer\" ");
_6a3.push("\ttype=\"application/x-shockwave-flash\" ");
_6a3.push("\tname=\"dojoStorage\">");
_6a3.push("</embed>");
}
_6a3.push("</div>");
document.write(_6a3.join(""));
}});
dojo.storage.setProvider(new dojo.storage.browser.StorageProvider());
dojo.storage.provider.writeStorage();
dojo.addOnLoad(function(){
dojo.storage.provider.flash=(dojo.render.html.ie)?window["dojoStorage"]:document["dojoStorage"];
});
dojo.hostenv.conditionalLoadModule({common:["dojo.storage"],browser:["dojo.storage.browser"],dashboard:["dojo.storage.dashboard"]});
dojo.hostenv.moduleLoaded("dojo.storage.*");
dojo.provide("dojo.crypto");
dojo.crypto.cipherModes={ECB:0,CBC:1,PCBC:2,CFB:3,OFB:4,CTR:5};
dojo.crypto.outputTypes={Base64:0,Hex:1,String:2,Raw:3};
dojo.require("dojo.crypto");
dojo.provide("dojo.crypto.MD5");
dojo.crypto.MD5=new function(){
var _6a4=8;
var mask=(1<<_6a4)-1;
function toWord(s){
var wa=[];
for(var i=0;i<s.length*_6a4;i+=_6a4){
wa[i>>5]|=(s.charCodeAt(i/_6a4)&mask)<<(i%32);
}
return wa;
}
function toString(wa){
var s=[];
for(var i=0;i<wa.length*32;i+=_6a4){
s.push(String.fromCharCode((wa[i>>5]>>>(i%32))&mask));
}
return s.join("");
}
function toHex(wa){
var h="0123456789abcdef";
var s=[];
for(var i=0;i<wa.length*4;i++){
s.push(h.charAt((wa[i>>2]>>((i%4)*8+4))&15)+h.charAt((wa[i>>2]>>((i%4)*8))&15));
}
return s.join("");
}
function toBase64(wa){
var p="=";
var tab="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
var s=[];
for(var i=0;i<wa.length*4;i+=3){
var t=(((wa[i>>2]>>8*(i%4))&255)<<16)|(((wa[i+1>>2]>>8*((i+1)%4))&255)<<8)|((wa[i+2>>2]>>8*((i+2)%4))&255);
for(var j=0;j<4;j++){
if(i*8+j*6>wa.length*32){
s.push(p);
}else{
s.push(tab.charAt((t>>6*(3-j))&63));
}
}
}
return s.join("");
}
function add(x,y){
var l=(x&65535)+(y&65535);
var m=(x>>16)+(y>>16)+(l>>16);
return (m<<16)|(l&65535);
}
function R(n,c){
return (n<<c)|(n>>>(32-c));
}
function C(q,a,b,x,s,t){
return add(R(add(add(a,q),add(x,t)),s),b);
}
function FF(a,b,c,d,x,s,t){
return C((b&c)|((~b)&d),a,b,x,s,t);
}
function GG(a,b,c,d,x,s,t){
return C((b&d)|(c&(~d)),a,b,x,s,t);
}
function HH(a,b,c,d,x,s,t){
return C(b^c^d,a,b,x,s,t);
}
function II(a,b,c,d,x,s,t){
return C(c^(b|(~d)),a,b,x,s,t);
}
function core(x,len){
x[len>>5]|=128<<((len)%32);
x[(((len+64)>>>9)<<4)+14]=len;
var a=1732584193;
var b=-271733879;
var c=-1732584194;
var d=271733878;
for(var i=0;i<x.length;i+=16){
var olda=a;
var oldb=b;
var oldc=c;
var oldd=d;
a=FF(a,b,c,d,x[i+0],7,-680876936);
d=FF(d,a,b,c,x[i+1],12,-389564586);
c=FF(c,d,a,b,x[i+2],17,606105819);
b=FF(b,c,d,a,x[i+3],22,-1044525330);
a=FF(a,b,c,d,x[i+4],7,-176418897);
d=FF(d,a,b,c,x[i+5],12,1200080426);
c=FF(c,d,a,b,x[i+6],17,-1473231341);
b=FF(b,c,d,a,x[i+7],22,-45705983);
a=FF(a,b,c,d,x[i+8],7,1770035416);
d=FF(d,a,b,c,x[i+9],12,-1958414417);
c=FF(c,d,a,b,x[i+10],17,-42063);
b=FF(b,c,d,a,x[i+11],22,-1990404162);
a=FF(a,b,c,d,x[i+12],7,1804603682);
d=FF(d,a,b,c,x[i+13],12,-40341101);
c=FF(c,d,a,b,x[i+14],17,-1502002290);
b=FF(b,c,d,a,x[i+15],22,1236535329);
a=GG(a,b,c,d,x[i+1],5,-165796510);
d=GG(d,a,b,c,x[i+6],9,-1069501632);
c=GG(c,d,a,b,x[i+11],14,643717713);
b=GG(b,c,d,a,x[i+0],20,-373897302);
a=GG(a,b,c,d,x[i+5],5,-701558691);
d=GG(d,a,b,c,x[i+10],9,38016083);
c=GG(c,d,a,b,x[i+15],14,-660478335);
b=GG(b,c,d,a,x[i+4],20,-405537848);
a=GG(a,b,c,d,x[i+9],5,568446438);
d=GG(d,a,b,c,x[i+14],9,-1019803690);
c=GG(c,d,a,b,x[i+3],14,-187363961);
b=GG(b,c,d,a,x[i+8],20,1163531501);
a=GG(a,b,c,d,x[i+13],5,-1444681467);
d=GG(d,a,b,c,x[i+2],9,-51403784);
c=GG(c,d,a,b,x[i+7],14,1735328473);
b=GG(b,c,d,a,x[i+12],20,-1926607734);
a=HH(a,b,c,d,x[i+5],4,-378558);
d=HH(d,a,b,c,x[i+8],11,-2022574463);
c=HH(c,d,a,b,x[i+11],16,1839030562);
b=HH(b,c,d,a,x[i+14],23,-35309556);
a=HH(a,b,c,d,x[i+1],4,-1530992060);
d=HH(d,a,b,c,x[i+4],11,1272893353);
c=HH(c,d,a,b,x[i+7],16,-155497632);
b=HH(b,c,d,a,x[i+10],23,-1094730640);
a=HH(a,b,c,d,x[i+13],4,681279174);
d=HH(d,a,b,c,x[i+0],11,-358537222);
c=HH(c,d,a,b,x[i+3],16,-722521979);
b=HH(b,c,d,a,x[i+6],23,76029189);
a=HH(a,b,c,d,x[i+9],4,-640364487);
d=HH(d,a,b,c,x[i+12],11,-421815835);
c=HH(c,d,a,b,x[i+15],16,530742520);
b=HH(b,c,d,a,x[i+2],23,-995338651);
a=II(a,b,c,d,x[i+0],6,-198630844);
d=II(d,a,b,c,x[i+7],10,1126891415);
c=II(c,d,a,b,x[i+14],15,-1416354905);
b=II(b,c,d,a,x[i+5],21,-57434055);
a=II(a,b,c,d,x[i+12],6,1700485571);
d=II(d,a,b,c,x[i+3],10,-1894986606);
c=II(c,d,a,b,x[i+10],15,-1051523);
b=II(b,c,d,a,x[i+1],21,-2054922799);
a=II(a,b,c,d,x[i+8],6,1873313359);
d=II(d,a,b,c,x[i+15],10,-30611744);
c=II(c,d,a,b,x[i+6],15,-1560198380);
b=II(b,c,d,a,x[i+13],21,1309151649);
a=II(a,b,c,d,x[i+4],6,-145523070);
d=II(d,a,b,c,x[i+11],10,-1120210379);
c=II(c,d,a,b,x[i+2],15,718787259);
b=II(b,c,d,a,x[i+9],21,-343485551);
a=add(a,olda);
b=add(b,oldb);
c=add(c,oldc);
d=add(d,oldd);
}
return [a,b,c,d];
}
function hmac(data,key){
var wa=toWord(key);
if(wa.length>16){
wa=core(wa,key.length*_6a4);
}
var l=[],r=[];
for(var i=0;i<16;i++){
l[i]=wa[i]^909522486;
r[i]=wa[i]^1549556828;
}
var h=core(l.concat(toWord(data)),512+data.length*_6a4);
return core(r.concat(h),640);
}
this.compute=function(data,_6f1){
var out=_6f1||dojo.crypto.outputTypes.Base64;
switch(out){
case dojo.crypto.outputTypes.Hex:
return toHex(core(toWord(data),data.length*_6a4));
case dojo.crypto.outputTypes.String:
return toString(core(toWord(data),data.length*_6a4));
default:
return toBase64(core(toWord(data),data.length*_6a4));
}
};
this.getHMAC=function(data,key,_6f5){
var out=_6f5||dojo.crypto.outputTypes.Base64;
switch(out){
case dojo.crypto.outputTypes.Hex:
return toHex(hmac(data,key));
case dojo.crypto.outputTypes.String:
return toString(hmac(data,key));
default:
return toBase64(hmac(data,key));
}
};
}();
dojo.hostenv.conditionalLoadModule({common:["dojo.crypto","dojo.crypto.MD5"]});
dojo.hostenv.moduleLoaded("dojo.crypto.*");
dojo.provide("dojo.collections.Collections");
dojo.collections={Collections:true};
dojo.collections.DictionaryEntry=function(k,v){
this.key=k;
this.value=v;
this.valueOf=function(){
return this.value;
};
this.toString=function(){
return this.value;
};
};
dojo.collections.Iterator=function(a){
var obj=a;
var _6fb=0;
this.atEnd=(_6fb>=obj.length-1);
this.current=obj[_6fb];
this.moveNext=function(){
if(++_6fb>=obj.length){
this.atEnd=true;
}
if(this.atEnd){
return false;
}
this.current=obj[_6fb];
return true;
};
this.reset=function(){
_6fb=0;
this.atEnd=false;
this.current=obj[_6fb];
};
};
dojo.collections.DictionaryIterator=function(obj){
var arr=[];
for(var p in obj){
arr.push(obj[p]);
}
var _6ff=0;
this.atEnd=(_6ff>=arr.length-1);
this.current=arr[_6ff]||null;
this.entry=this.current||null;
this.key=(this.entry)?this.entry.key:null;
this.value=(this.entry)?this.entry.value:null;
this.moveNext=function(){
if(++_6ff>=arr.length){
this.atEnd=true;
}
if(this.atEnd){
return false;
}
this.entry=this.current=arr[_6ff];
if(this.entry){
this.key=this.entry.key;
this.value=this.entry.value;
}
return true;
};
this.reset=function(){
_6ff=0;
this.atEnd=false;
this.current=arr[_6ff]||null;
this.entry=this.current||null;
this.key=(this.entry)?this.entry.key:null;
this.value=(this.entry)?this.entry.value:null;
};
};
dojo.provide("dojo.collections.ArrayList");
dojo.require("dojo.collections.Collections");
dojo.collections.ArrayList=function(arr){
var _701=[];
if(arr){
_701=_701.concat(arr);
}
this.count=_701.length;
this.add=function(obj){
_701.push(obj);
this.count=_701.length;
};
this.addRange=function(a){
if(a.getIterator){
var e=a.getIterator();
while(!e.atEnd){
this.add(e.current);
e.moveNext();
}
this.count=_701.length;
}else{
for(var i=0;i<a.length;i++){
_701.push(a[i]);
}
this.count=_701.length;
}
};
this.clear=function(){
_701.splice(0,_701.length);
this.count=0;
};
this.clone=function(){
return new dojo.collections.ArrayList(_701);
};
this.contains=function(obj){
for(var i=0;i<_701.length;i++){
if(_701[i]==obj){
return true;
}
}
return false;
};
this.getIterator=function(){
return new dojo.collections.Iterator(_701);
};
this.indexOf=function(obj){
for(var i=0;i<_701.length;i++){
if(_701[i]==obj){
return i;
}
}
return -1;
};
this.insert=function(i,obj){
_701.splice(i,0,obj);
this.count=_701.length;
};
this.item=function(k){
return _701[k];
};
this.remove=function(obj){
var i=this.indexOf(obj);
if(i>=0){
_701.splice(i,1);
}
this.count=_701.length;
};
this.removeAt=function(i){
_701.splice(i,1);
this.count=_701.length;
};
this.reverse=function(){
_701.reverse();
};
this.sort=function(fn){
if(fn){
_701.sort(fn);
}else{
_701.sort();
}
};
this.setByIndex=function(i,obj){
_701[i]=obj;
this.count=_701.length;
};
this.toArray=function(){
return [].concat(_701);
};
this.toString=function(){
return _701.join(",");
};
};
dojo.provide("dojo.collections.Queue");
dojo.require("dojo.collections.Collections");
dojo.collections.Queue=function(arr){
var q=[];
if(arr){
q=q.concat(arr);
}
this.count=q.length;
this.clear=function(){
q=[];
this.count=q.length;
};
this.clone=function(){
return new dojo.collections.Queue(q);
};
this.contains=function(o){
for(var i=0;i<q.length;i++){
if(q[i]==o){
return true;
}
}
return false;
};
this.copyTo=function(arr,i){
arr.splice(i,0,q);
};
this.dequeue=function(){
var r=q.shift();
this.count=q.length;
return r;
};
this.enqueue=function(o){
this.count=q.push(o);
};
this.getIterator=function(){
return new dojo.collections.Iterator(q);
};
this.peek=function(){
return q[0];
};
this.toArray=function(){
return [].concat(q);
};
};
dojo.provide("dojo.collections.Stack");
dojo.require("dojo.collections.Collections");
dojo.collections.Stack=function(arr){
var q=[];
if(arr){
q=q.concat(arr);
}
this.count=q.length;
this.clear=function(){
q=[];
this.count=q.length;
};
this.clone=function(){
return new dojo.collections.Stack(q);
};
this.contains=function(o){
for(var i=0;i<q.length;i++){
if(q[i]==o){
return true;
}
}
return false;
};
this.copyTo=function(arr,i){
arr.splice(i,0,q);
};
this.getIterator=function(){
return new dojo.collections.Iterator(q);
};
this.peek=function(){
return q[(q.length-1)];
};
this.pop=function(){
var r=q.pop();
this.count=q.length;
return r;
};
this.push=function(o){
this.count=q.push(o);
};
this.toArray=function(){
return [].concat(q);
};
};
dojo.provide("dojo.graphics.htmlEffects");
dojo.require("dojo.fx.*");
dj_deprecated("dojo.graphics.htmlEffects is deprecated, use dojo.fx.html instead");
dojo.graphics.htmlEffects=dojo.fx.html;
dojo.hostenv.conditionalLoadModule({browser:["dojo.graphics.htmlEffects"],dashboard:["dojo.graphics.htmlEffects"]});
dojo.hostenv.moduleLoaded("dojo.graphics.*");
dojo.provide("dojo.animation.AnimationSequence");
dojo.require("dojo.animation.AnimationEvent");
dojo.require("dojo.animation.Animation");
dojo.animation.AnimationSequence=function(_723){
this._anims=[];
this.repeatCount=_723||0;
};
dojo.lang.extend(dojo.animation.AnimationSequence,{repeateCount:0,_anims:[],_currAnim:-1,onBegin:null,onEnd:null,onNext:null,handler:null,add:function(){
for(var i=0;i<arguments.length;i++){
this._anims.push(arguments[i]);
arguments[i]._animSequence=this;
}
},remove:function(anim){
for(var i=0;i<this._anims.length;i++){
if(this._anims[i]==anim){
this._anims[i]._animSequence=null;
this._anims.splice(i,1);
break;
}
}
},removeAll:function(){
for(var i=0;i<this._anims.length;i++){
this._anims[i]._animSequence=null;
}
this._anims=[];
this._currAnim=-1;
},clear:function(){
this.removeAll();
},play:function(_728){
if(this._anims.length==0){
return;
}
if(_728||!this._anims[this._currAnim]){
this._currAnim=0;
}
if(this._anims[this._currAnim]){
if(this._currAnim==0){
var e={type:"begin",animation:this._anims[this._currAnim]};
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onBegin=="function"){
this.onBegin(e);
}
}
this._anims[this._currAnim].play(_728);
}
},pause:function(){
if(this._anims[this._currAnim]){
this._anims[this._currAnim].pause();
}
},playPause:function(){
if(this._anims.length==0){
return;
}
if(this._currAnim==-1){
this._currAnim=0;
}
if(this._anims[this._currAnim]){
this._anims[this._currAnim].playPause();
}
},stop:function(){
if(this._anims[this._currAnim]){
this._anims[this._currAnim].stop();
}
},status:function(){
if(this._anims[this._currAnim]){
return this._anims[this._currAnim].status();
}else{
return "stopped";
}
},_setCurrent:function(anim){
for(var i=0;i<this._anims.length;i++){
if(this._anims[i]==anim){
this._currAnim=i;
break;
}
}
},_playNext:function(){
if(this._currAnim==-1||this._anims.length==0){
return;
}
this._currAnim++;
if(this._anims[this._currAnim]){
var e={type:"next",animation:this._anims[this._currAnim]};
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onNext=="function"){
this.onNext(e);
}
this._anims[this._currAnim].play(true);
}else{
var e={type:"end",animation:this._anims[this._anims.length-1]};
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onEnd=="function"){
this.onEnd(e);
}
if(this.repeatCount>0){
this._currAnim=0;
this.repeatCount--;
this._anims[this._currAnim].play(true);
}else{
if(this.repeatCount==-1){
this._currAnim=0;
this._anims[this._currAnim].play(true);
}else{
this._currAnim=-1;
}
}
}
}});
dojo.hostenv.conditionalLoadModule({common:["dojo.animation.AnimationEvent","dojo.animation.Animation","dojo.animation.AnimationSequence"]});
dojo.hostenv.moduleLoaded("dojo.animation.*");
dojo.require("dojo.lang");
dojo.provide("dojo.dnd.DragSource");
dojo.provide("dojo.dnd.DropTarget");
dojo.provide("dojo.dnd.DragObject");
dojo.provide("dojo.dnd.DragAndDrop");
dojo.dnd.DragSource=function(){
dojo.dnd.dragManager.registerDragSource(this);
};
dojo.lang.extend(dojo.dnd.DragSource,{type:"",onDragEnd:function(){
},onDragStart:function(){
},unregister:function(){
dojo.dnd.dragManager.unregisterDragSource(this);
},reregister:function(){
dojo.dnd.dragManager.registerDragSource(this);
}});
dojo.dnd.DragObject=function(){
dojo.dnd.dragManager.registerDragObject(this);
};
dojo.lang.extend(dojo.dnd.DragObject,{type:"",onDragStart:function(){
},onDragMove:function(){
},onDragOver:function(){
},onDragOut:function(){
},onDragEnd:function(){
},onDragLeave:this.onDragOut,onDragEnter:this.onDragOver,ondragout:this.onDragOut,ondragover:this.onDragOver});
dojo.dnd.DropTarget=function(){
if(this.constructor==dojo.dnd.DropTarget){
return;
}
this.acceptedTypes=[];
dojo.dnd.dragManager.registerDropTarget(this);
};
dojo.lang.extend(dojo.dnd.DropTarget,{acceptsType:function(type){
if(!dojo.lang.inArray(this.acceptedTypes,"*")){
if(!dojo.lang.inArray(this.acceptedTypes,type)){
return false;
}
}
return true;
},accepts:function(_72e){
if(!dojo.lang.inArray(this.acceptedTypes,"*")){
for(var i=0;i<_72e.length;i++){
if(!dojo.lang.inArray(this.acceptedTypes,_72e[i].type)){
return false;
}
}
}
return true;
},onDragOver:function(){
},onDragOut:function(){
},onDragMove:function(){
},onDropStart:function(){
},onDrop:function(){
},onDropEnd:function(){
}});
dojo.dnd.DragEvent=function(){
this.dragSource=null;
this.dragObject=null;
this.target=null;
this.eventStatus="success";
};
dojo.dnd.DragManager=function(){
};
dojo.lang.extend(dojo.dnd.DragManager,{selectedSources:[],dragObjects:[],dragSources:[],registerDragSource:function(){
},dropTargets:[],registerDropTarget:function(){
},lastDragTarget:null,currentDragTarget:null,onKeyDown:function(){
},onMouseOut:function(){
},onMouseMove:function(){
},onMouseUp:function(){
}});
dojo.provide("dojo.dnd.HtmlDragManager");
dojo.require("dojo.dnd.DragAndDrop");
dojo.require("dojo.event.*");
dojo.require("dojo.lang.array");
dojo.require("dojo.html");
dojo.require("dojo.style");
dojo.dnd.HtmlDragManager=function(){
};
dojo.inherits(dojo.dnd.HtmlDragManager,dojo.dnd.DragManager);
dojo.lang.extend(dojo.dnd.HtmlDragManager,{disabled:false,nestedTargets:false,mouseDownTimer:null,dsCounter:0,dsPrefix:"dojoDragSource",dropTargetDimensions:[],currentDropTarget:null,previousDropTarget:null,_dragTriggered:false,selectedSources:[],dragObjects:[],currentX:null,currentY:null,lastX:null,lastY:null,mouseDownX:null,mouseDownY:null,threshold:7,dropAcceptable:false,registerDragSource:function(ds){
if(ds["domNode"]){
var dp=this.dsPrefix;
var _732=dp+"Idx_"+(this.dsCounter++);
ds.dragSourceId=_732;
this.dragSources[_732]=ds;
ds.domNode.setAttribute(dp,_732);
}
},unregisterDragSource:function(ds){
if(ds["domNode"]){
var dp=this.dsPrefix;
var _735=ds.dragSourceId;
delete ds.dragSourceId;
delete this.dragSources[_735];
ds.domNode.setAttribute(dp,null);
}
},registerDropTarget:function(dt){
this.dropTargets.push(dt);
},unregisterDropTarget:function(dt){
var _738=dojo.lang.find(this.dropTargets,dt,true);
if(_738>=0){
this.dropTargets.splice(_738,1);
}
},getDragSource:function(e){
var tn=e.target;
if(tn===document.body){
return;
}
var ta=dojo.html.getAttribute(tn,this.dsPrefix);
while((!ta)&&(tn)){
tn=tn.parentNode;
if((!tn)||(tn===document.body)){
return;
}
ta=dojo.html.getAttribute(tn,this.dsPrefix);
}
return this.dragSources[ta];
},onKeyDown:function(e){
},onMouseDown:function(e){
if(this.disabled){
return;
}
this.mouseDownX=e.clientX;
this.mouseDownY=e.clientY;
var _73e=e.target.nodeType==dojo.dom.TEXT_NODE?e.target.parentNode:e.target;
switch(_73e.tagName.toLowerCase()){
case "a":
case "button":
case "textarea":
case "input":
return;
}
var ds=this.getDragSource(e);
if(!ds){
return;
}
if(!dojo.lang.inArray(this.selectedSources,ds)){
this.selectedSources.push(ds);
}
e.preventDefault();
dojo.event.connect(document,"onmousemove",this,"onMouseMove");
},onMouseUp:function(e){
this.mouseDownX=null;
this.mouseDownY=null;
this._dragTriggered=false;
var _741=this;
e.dragSource=this.dragSource;
if((!e.shiftKey)&&(!e.ctrlKey)){
if(_741.currentDropTarget){
_741.currentDropTarget.onDropStart();
}
dojo.lang.forEach(this.dragObjects,function(_742){
var ret=null;
if(!_742){
return;
}
if(_741.currentDropTarget){
e.dragObject=_742;
var ce=_741.currentDropTarget.domNode.childNodes;
if(ce.length>0){
e.dropTarget=ce[0];
while(e.dropTarget==_742.domNode){
e.dropTarget=e.dropTarget.nextSibling;
}
}else{
e.dropTarget=_741.currentDropTarget.domNode;
}
if(_741.dropAcceptable){
ret=_741.currentDropTarget.onDrop(e);
}else{
_741.currentDropTarget.onDragOut(e);
}
}
e.dragStatus=_741.dropAcceptable&&ret?"dropSuccess":"dropFailure";
_742.onDragEnd(e);
});
this.selectedSources=[];
this.dragObjects=[];
this.dragSource=null;
if(_741.currentDropTarget){
_741.currentDropTarget.onDropEnd();
}
}
dojo.event.disconnect(document,"onmousemove",this,"onMouseMove");
this.currentDropTarget=null;
},onScroll:function(){
for(var i=0;i<this.dragObjects.length;i++){
if(this.dragObjects[i].updateDragOffset){
this.dragObjects[i].updateDragOffset();
}
}
this.cacheTargetLocations();
},_dragStartDistance:function(x,y){
if((!this.mouseDownX)||(!this.mouseDownX)){
return;
}
var dx=Math.abs(x-this.mouseDownX);
var dx2=dx*dx;
var dy=Math.abs(y-this.mouseDownY);
var dy2=dy*dy;
return parseInt(Math.sqrt(dx2+dy2),10);
},cacheTargetLocations:function(){
var _74c=this;
this.dropTargetDimensions=[];
dojo.lang.forEach(this.dropTargets,function(_74d){
var tn=_74d.domNode;
if(!tn){
return;
}
var ttx=dojo.style.getAbsoluteX(tn,true);
var tty=dojo.style.getAbsoluteY(tn,true);
_74c.dropTargetDimensions.push([[ttx,tty],[ttx+dojo.style.getInnerWidth(tn),tty+dojo.style.getInnerHeight(tn)],_74d]);
});
},onMouseMove:function(e){
var _752=this;
if((this.selectedSources.length)&&(!this.dragObjects.length)){
var dx;
var dy;
if(!this._dragTriggered){
this._dragTriggered=(this._dragStartDistance(e.clientX,e.clientY)>this.threshold);
if(!this._dragTriggered){
return;
}
dx=e.clientX-this.mouseDownX;
dy=e.clientY-this.mouseDownY;
}
if(this.selectedSources.length==1){
this.dragSource=this.selectedSources[0];
}
dojo.lang.forEach(this.selectedSources,function(_755){
if(!_755){
return;
}
var tdo=_755.onDragStart(e);
if(tdo){
tdo.onDragStart(e);
tdo.dragOffset.top+=dy;
tdo.dragOffset.left+=dx;
_752.dragObjects.push(tdo);
}
});
this.previousDropTarget=null;
this.cacheTargetLocations();
}
for(var i=0;i<this.dragObjects.length;i++){
if(this.dragObjects[i]){
this.dragObjects[i].onDragMove(e);
}
}
if(this.currentDropTarget){
var c=dojo.html.toCoordinateArray(this.currentDropTarget.domNode);
var dtp=[[c[0],c[1]],[c[0]+c[2],c[1]+c[3]]];
}
if((!this.nestedTargets)&&(dtp)&&(this.isInsideBox(e,dtp))){
if(this.dropAcceptable){
this.currentDropTarget.onDragMove(e,this.dragObjects);
}
}else{
var _75a=this.findBestTarget(e);
if(_75a.target===null){
if(this.currentDropTarget){
this.currentDropTarget.onDragOut(e);
this.previousDropTarget=this.currentDropTarget;
this.currentDropTarget=null;
}
this.dropAcceptable=false;
return;
}
if(this.currentDropTarget!==_75a.target){
if(this.currentDropTarget){
this.previousDropTarget=this.currentDropTarget;
this.currentDropTarget.onDragOut(e);
}
this.currentDropTarget=_75a.target;
e.dragObjects=this.dragObjects;
this.dropAcceptable=this.currentDropTarget.onDragOver(e);
}else{
if(this.dropAcceptable){
this.currentDropTarget.onDragMove(e,this.dragObjects);
}
}
}
},findBestTarget:function(e){
var _75c=this;
var _75d=new Object();
_75d.target=null;
_75d.points=null;
dojo.lang.forEach(this.dropTargetDimensions,function(_75e){
if(_75c.isInsideBox(e,_75e)){
_75d.target=_75e[2];
_75d.points=_75e;
if(!_75c.nestedTargets){
return "break";
}
}
});
return _75d;
},isInsideBox:function(e,_760){
if((e.clientX>_760[0][0])&&(e.clientX<_760[1][0])&&(e.clientY>_760[0][1])&&(e.clientY<_760[1][1])){
return true;
}
return false;
},onMouseOver:function(e){
},onMouseOut:function(e){
}});
dojo.dnd.dragManager=new dojo.dnd.HtmlDragManager();
(function(){
var d=document;
var dm=dojo.dnd.dragManager;
dojo.event.connect(d,"onkeydown",dm,"onKeyDown");
dojo.event.connect(d,"onmouseover",dm,"onMouseOver");
dojo.event.connect(d,"onmouseout",dm,"onMouseOut");
dojo.event.connect(d,"onmousedown",dm,"onMouseDown");
dojo.event.connect(d,"onmouseup",dm,"onMouseUp");
dojo.event.connect(window,"onscroll",dm,"onScroll");
})();
dojo.provide("dojo.dnd.HtmlDragAndDrop");
dojo.provide("dojo.dnd.HtmlDragSource");
dojo.provide("dojo.dnd.HtmlDropTarget");
dojo.provide("dojo.dnd.HtmlDragObject");
dojo.require("dojo.dnd.HtmlDragManager");
dojo.require("dojo.dnd.DragAndDrop");
dojo.require("dojo.animation.*");
dojo.require("dojo.dom");
dojo.require("dojo.style");
dojo.require("dojo.html");
dojo.require("dojo.lang.extras");
dojo.dnd.HtmlDragSource=function(node,type){
node=dojo.byId(node);
this.constrainToContainer=false;
if(node){
this.domNode=node;
this.dragObject=node;
dojo.dnd.DragSource.call(this);
this.type=type||this.domNode.nodeName.toLowerCase();
}
};
dojo.lang.extend(dojo.dnd.HtmlDragSource,{dragClass:"",onDragStart:function(){
var _767=new dojo.dnd.HtmlDragObject(this.dragObject,this.type);
if(this.dragClass){
_767.dragClass=this.dragClass;
}
if(this.constrainToContainer){
_767.constrainTo(this.constrainingContainer);
}
return _767;
},setDragHandle:function(node){
node=dojo.byId(node);
dojo.dnd.dragManager.unregisterDragSource(this);
this.domNode=node;
dojo.dnd.dragManager.registerDragSource(this);
},setDragTarget:function(node){
this.dragObject=node;
},constrainTo:function(_76a){
this.constrainToContainer=true;
if(_76a){
this.constrainingContainer=_76a;
}else{
this.constrainingContainer=this.domNode.parentNode;
}
}});
dojo.dnd.HtmlDragObject=function(node,type){
this.domNode=dojo.byId(node);
this.type=type;
this.constrainToContainer=false;
};
dojo.lang.extend(dojo.dnd.HtmlDragObject,{dragClass:"",opacity:0.5,createIframe:true,disableX:false,disableY:false,createDragNode:function(){
var node=this.domNode.cloneNode(true);
if(this.dragClass){
dojo.html.addClass(node,this.dragClass);
}
if(this.opacity<1){
dojo.style.setOpacity(node,this.opacity);
}
if(dojo.render.html.ie&&this.createIframe){
var _76e=document.createElement("div");
_76e.appendChild(node);
this.bgIframe=new dojo.html.BackgroundIframe();
this.bgIframe.size([0,0,dojo.style.getOuterWidth(node),dojo.style.getOuterHeight(node)]);
_76e.appendChild(this.bgIframe.iframe);
node=_76e;
}
node.style.zIndex=500;
return node;
},onDragStart:function(e){
dojo.html.clearSelection();
this.scrollOffset={top:dojo.html.getScrollTop(),left:dojo.html.getScrollLeft()};
this.dragStartPosition={top:dojo.style.getAbsoluteY(this.domNode,true)+this.scrollOffset.top,left:dojo.style.getAbsoluteX(this.domNode,true)+this.scrollOffset.left};
this.dragOffset={top:this.dragStartPosition.top-e.clientY,left:this.dragStartPosition.left-e.clientX};
this.dragClone=this.createDragNode();
if((this.domNode.parentNode.nodeName.toLowerCase()=="body")||(dojo.style.getComputedStyle(this.domNode.parentNode,"position")=="static")){
this.parentPosition={top:0,left:0};
}else{
this.parentPosition={top:dojo.style.getAbsoluteY(this.domNode.parentNode,true),left:dojo.style.getAbsoluteX(this.domNode.parentNode,true)};
}
if(this.constrainToContainer){
this.constraints=this.getConstraints();
}
with(this.dragClone.style){
position="absolute";
top=this.dragOffset.top+e.clientY+"px";
left=this.dragOffset.left+e.clientX+"px";
}
document.body.appendChild(this.dragClone);
},getConstraints:function(){
if(this.constrainingContainer.nodeName.toLowerCase()=="body"){
width=dojo.html.getViewportWidth();
height=dojo.html.getViewportHeight();
padLeft=0;
padTop=0;
}else{
width=dojo.style.getContentWidth(this.constrainingContainer);
height=dojo.style.getContentHeight(this.constrainingContainer);
padLeft=dojo.style.getPixelValue(this.constrainingContainer,"padding-left",true);
padTop=dojo.style.getPixelValue(this.constrainingContainer,"padding-top",true);
}
return {minX:padLeft,minY:padTop,maxX:padLeft+width-dojo.style.getOuterWidth(this.domNode),maxY:padTop+height-dojo.style.getOuterHeight(this.domNode)};
},updateDragOffset:function(){
var sTop=dojo.html.getScrollTop();
var _771=dojo.html.getScrollLeft();
if(sTop!=this.scrollOffset.top){
var diff=sTop-this.scrollOffset.top;
this.dragOffset.top+=diff;
this.scrollOffset.top=sTop;
}
},onDragMove:function(e){
this.updateDragOffset();
var x=this.dragOffset.left+e.clientX-this.parentPosition.left;
var y=this.dragOffset.top+e.clientY-this.parentPosition.top;
if(this.constrainToContainer){
if(x<this.constraints.minX){
x=this.constraints.minX;
}
if(y<this.constraints.minY){
y=this.constraints.minY;
}
if(x>this.constraints.maxX){
x=this.constraints.maxX;
}
if(y>this.constraints.maxY){
y=this.constraints.maxY;
}
}
if(!this.disableY){
this.dragClone.style.top=y+"px";
}
if(!this.disableX){
this.dragClone.style.left=x+"px";
}
},onDragEnd:function(e){
switch(e.dragStatus){
case "dropSuccess":
dojo.dom.removeNode(this.dragClone);
this.dragClone=null;
break;
case "dropFailure":
var _777=[dojo.style.getAbsoluteX(this.dragClone),dojo.style.getAbsoluteY(this.dragClone)];
var _778=[this.dragStartPosition.left+1,this.dragStartPosition.top+1];
var line=new dojo.math.curves.Line(_777,_778);
var anim=new dojo.animation.Animation(line,300,0,0);
var _77b=this;
dojo.event.connect(anim,"onAnimate",function(e){
_77b.dragClone.style.left=e.x+"px";
_77b.dragClone.style.top=e.y+"px";
});
dojo.event.connect(anim,"onEnd",function(e){
dojo.lang.setTimeout(dojo.dom.removeNode,200,_77b.dragClone);
});
anim.play();
break;
}
},constrainTo:function(_77e){
this.constrainToContainer=true;
if(_77e){
this.constrainingContainer=_77e;
}else{
this.constrainingContainer=this.domNode.parentNode;
}
}});
dojo.dnd.HtmlDropTarget=function(node,_780){
if(arguments.length==0){
return;
}
this.domNode=dojo.byId(node);
dojo.dnd.DropTarget.call(this);
this.acceptedTypes=_780||[];
};
dojo.inherits(dojo.dnd.HtmlDropTarget,dojo.dnd.DropTarget);
dojo.lang.extend(dojo.dnd.HtmlDropTarget,{onDragOver:function(e){
if(!this.accepts(e.dragObjects)){
return false;
}
this.childBoxes=[];
for(var i=0,child;i<this.domNode.childNodes.length;i++){
child=this.domNode.childNodes[i];
if(child.nodeType!=dojo.dom.ELEMENT_NODE){
continue;
}
var top=dojo.style.getAbsoluteY(child);
var _784=top+dojo.style.getInnerHeight(child);
var left=dojo.style.getAbsoluteX(child);
var _786=left+dojo.style.getInnerWidth(child);
this.childBoxes.push({top:top,bottom:_784,left:left,right:_786,node:child});
}
return true;
},_getNodeUnderMouse:function(e){
var _788=e.pageX||e.clientX+document.body.scrollLeft;
var _789=e.pageY||e.clientY+document.body.scrollTop;
for(var i=0,child;i<this.childBoxes.length;i++){
with(this.childBoxes[i]){
if(_788>=left&&_788<=right&&_789>=top&&_789<=bottom){
return i;
}
}
}
return -1;
},createDropIndicator:function(){
this.dropIndicator=document.createElement("div");
with(this.dropIndicator.style){
position="absolute";
zIndex=1;
borderTopWidth="1px";
borderTopColor="black";
borderTopStyle="solid";
width=dojo.style.getInnerWidth(this.domNode)+"px";
left=dojo.style.getAbsoluteX(this.domNode)+"px";
}
},onDragMove:function(e,_78c){
var i=this._getNodeUnderMouse(e);
if(!this.dropIndicator){
this.createDropIndicator();
}
if(i<0){
if(this.childBoxes.length){
var _78e=(dojo.html.gravity(this.childBoxes[0].node,e)&dojo.html.gravity.NORTH);
}else{
var _78e=true;
}
}else{
var _78f=this.childBoxes[i];
var _78e=(dojo.html.gravity(_78f.node,e)&dojo.html.gravity.NORTH);
}
this.placeIndicator(e,_78c,i,_78e);
if(!dojo.html.hasParent(this.dropIndicator)){
document.body.appendChild(this.dropIndicator);
}
},placeIndicator:function(e,_791,_792,_793){
with(this.dropIndicator.style){
if(_792<0){
if(this.childBoxes.length){
top=(_793?this.childBoxes[0].top:this.childBoxes[this.childBoxes.length-1].bottom)+"px";
}else{
top=dojo.style.getAbsoluteY(this.domNode)+"px";
}
}else{
var _794=this.childBoxes[_792];
top=(_793?_794.top:_794.bottom)+"px";
}
}
},onDragOut:function(e){
if(this.dropIndicator){
dojo.dom.removeNode(this.dropIndicator);
delete this.dropIndicator;
}
},onDrop:function(e){
this.onDragOut(e);
var i=this._getNodeUnderMouse(e);
if(i<0){
if(this.childBoxes.length){
if(dojo.html.gravity(this.childBoxes[0].node,e)&dojo.html.gravity.NORTH){
return this.insert(e,this.childBoxes[0].node,"before");
}else{
return this.insert(e,this.childBoxes[this.childBoxes.length-1].node,"after");
}
}
return this.insert(e,this.domNode,"append");
}
var _798=this.childBoxes[i];
if(dojo.html.gravity(_798.node,e)&dojo.html.gravity.NORTH){
return this.insert(e,_798.node,"before");
}else{
return this.insert(e,_798.node,"after");
}
},insert:function(e,_79a,_79b){
var node=e.dragObject.domNode;
if(_79b=="before"){
return dojo.html.insertBefore(node,_79a);
}else{
if(_79b=="after"){
return dojo.html.insertAfter(node,_79a);
}else{
if(_79b=="append"){
_79a.appendChild(node);
return true;
}
}
}
return false;
}});
dojo.hostenv.conditionalLoadModule({common:["dojo.dnd.DragAndDrop"],browser:["dojo.dnd.HtmlDragAndDrop"],dashboard:["dojo.dnd.HtmlDragAndDrop"]});
dojo.hostenv.moduleLoaded("dojo.dnd.*");
dojo.provide("dojo.widget.Manager");
dojo.require("dojo.lang.array");
dojo.require("dojo.event.*");
dojo.widget.manager=new function(){
this.widgets=[];
this.widgetIds=[];
this.topWidgets={};
var _79d={};
var _79e=[];
this.getUniqueId=function(_79f){
return _79f+"_"+(_79d[_79f]!=undefined?++_79d[_79f]:_79d[_79f]=0);
};
this.add=function(_7a0){
dojo.profile.start("dojo.widget.manager.add");
this.widgets.push(_7a0);
if(_7a0.widgetId==""){
if(_7a0["id"]){
_7a0.widgetId=_7a0["id"];
}else{
if(_7a0.extraArgs["id"]){
_7a0.widgetId=_7a0.extraArgs["id"];
}else{
_7a0.widgetId=this.getUniqueId(_7a0.widgetType);
}
}
}
if(this.widgetIds[_7a0.widgetId]){
dojo.debug("widget ID collision on ID: "+_7a0.widgetId);
}
this.widgetIds[_7a0.widgetId]=_7a0;
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
this.remove=function(_7a2){
var tw=this.widgets[_7a2].widgetId;
delete this.widgetIds[tw];
this.widgets.splice(_7a2,1);
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
this.getWidgetsByFilter=function(_7ac){
var ret=[];
dojo.lang.forEach(this.widgets,function(x){
if(_7ac(x)){
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
var _7b2={};
var _7b3=["dojo.widget"];
for(var i=0;i<_7b3.length;i++){
_7b3[_7b3[i]]=true;
}
this.registerWidgetPackage=function(_7b5){
if(!_7b3[_7b5]){
_7b3[_7b5]=true;
_7b3.push(_7b5);
}
};
this.getWidgetPackageList=function(){
return dojo.lang.map(_7b3,function(elt){
return (elt!==true?elt:undefined);
});
};
this.getImplementation=function(_7b7,_7b8,_7b9){
var impl=this.getImplementationName(_7b7);
if(impl){
var ret=new impl(_7b8);
return ret;
}
};
this.getImplementationName=function(_7bc){
var _7bd=_7bc.toLowerCase();
var impl=_7b2[_7bd];
if(impl){
return impl;
}
if(!_79e.length){
for(var _7bf in dojo.render){
if(dojo.render[_7bf]["capable"]===true){
var _7c0=dojo.render[_7bf].prefixes;
for(var i=0;i<_7c0.length;i++){
_79e.push(_7c0[i].toLowerCase());
}
}
}
_79e.push("");
}
for(var i=0;i<_7b3.length;i++){
var _7c2=dojo.evalObjPath(_7b3[i]);
if(!_7c2){
continue;
}
for(var j=0;j<_79e.length;j++){
if(!_7c2[_79e[j]]){
continue;
}
for(var _7c4 in _7c2[_79e[j]]){
if(_7c4.toLowerCase()!=_7bd){
continue;
}
_7b2[_7bd]=_7c2[_79e[j]][_7c4];
return _7b2[_7bd];
}
}
for(var j=0;j<_79e.length;j++){
for(var _7c4 in _7c2){
if(_7c4.toLowerCase()!=(_79e[j]+_7bd)){
continue;
}
_7b2[_7bd]=_7c2[_7c4];
return _7b2[_7bd];
}
}
}
throw new Error("Could not locate \""+_7bc+"\" class");
};
this.resizing=false;
this.onResized=function(){
if(this.resizing){
return;
}
try{
this.resizing=true;
for(var id in this.topWidgets){
var _7c6=this.topWidgets[id];
if(_7c6.onResized){
_7c6.onResized();
}
}
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
var _7c8=dojo.widget.manager.getAllWidgets.apply(dojo.widget.manager,arguments);
if(arguments.length>0){
return _7c8[n];
}
return _7c8;
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
dojo.provide("dojo.widget.Widget");
dojo.provide("dojo.widget.tags");
dojo.require("dojo.lang.func");
dojo.require("dojo.lang.array");
dojo.require("dojo.widget.Manager");
dojo.require("dojo.event.*");
dojo.widget.Widget=function(){
this.children=[];
this.extraArgs={};
};
dojo.lang.extend(dojo.widget.Widget,{parent:null,isTopLevel:false,isModal:false,isEnabled:true,isHidden:false,isContainer:false,widgetId:"",widgetType:"Widget",toString:function(){
return "[Widget "+this.widgetType+", "+(this.widgetId||"NO ID")+"]";
},repr:function(){
return this.toString();
},enable:function(){
this.isEnabled=true;
},disable:function(){
this.isEnabled=false;
},hide:function(){
this.isHidden=true;
},show:function(){
this.isHidden=false;
},onResized:function(){
this.notifyChildrenOfResize();
},notifyChildrenOfResize:function(){
for(var i=0;i<this.children.length;i++){
var _7ca=this.children[i];
if(_7ca.onResized){
_7ca.onResized();
}
}
},create:function(args,_7cc,_7cd){
this.satisfyPropertySets(args,_7cc,_7cd);
this.mixInProperties(args,_7cc,_7cd);
this.postMixInProperties(args,_7cc,_7cd);
dojo.widget.manager.add(this);
this.buildRendering(args,_7cc,_7cd);
this.initialize(args,_7cc,_7cd);
this.postInitialize(args,_7cc,_7cd);
this.postCreate(args,_7cc,_7cd);
return this;
},destroy:function(_7ce){
this.uninitialize();
this.destroyRendering(_7ce);
dojo.widget.manager.removeById(this.widgetId);
},destroyChildren:function(_7cf){
_7cf=(!_7cf)?function(){
return true;
}:_7cf;
for(var x=0;x<this.children.length;x++){
var tc=this.children[x];
if((tc)&&(_7cf(tc))){
tc.destroy();
}
}
},destroyChildrenOfType:function(type){
type=type.toLowerCase();
this.destroyChildren(function(item){
if(item.widgetType.toLowerCase()==type){
return true;
}else{
return false;
}
});
},getChildrenOfType:function(type,_7d5){
var ret=[];
var _7d7=dojo.lang.isFunction(type);
if(!_7d7){
type=type.toLowerCase();
}
for(var x=0;x<this.children.length;x++){
if(_7d7){
if(this.children[x] instanceof type){
ret.push(this.children[x]);
}
}else{
if(this.children[x].widgetType.toLowerCase()==type){
ret.push(this.children[x]);
}
}
if(_7d5){
ret=ret.concat(this.children[x].getChildrenOfType(type,_7d5));
}
}
return ret;
},getDescendants:function(){
var _7d9=[];
var _7da=[this];
var elem;
while(elem=_7da.pop()){
_7d9.push(elem);
dojo.lang.forEach(elem.children,function(elem){
_7da.push(elem);
});
}
return _7d9;
},satisfyPropertySets:function(args){
return args;
},mixInProperties:function(args,frag){
if((args["fastMixIn"])||(frag["fastMixIn"])){
for(var x in args){
this[x]=args[x];
}
return;
}
var _7e1;
var _7e2=dojo.widget.lcArgsCache[this.widgetType];
if(_7e2==null){
_7e2={};
for(var y in this){
_7e2[((new String(y)).toLowerCase())]=y;
}
dojo.widget.lcArgsCache[this.widgetType]=_7e2;
}
var _7e4={};
for(var x in args){
if(!this[x]){
var y=_7e2[(new String(x)).toLowerCase()];
if(y){
args[y]=args[x];
x=y;
}
}
if(_7e4[x]){
continue;
}
_7e4[x]=true;
if((typeof this[x])!=(typeof _7e1)){
if(typeof args[x]!="string"){
this[x]=args[x];
}else{
if(dojo.lang.isString(this[x])){
this[x]=args[x];
}else{
if(dojo.lang.isNumber(this[x])){
this[x]=new Number(args[x]);
}else{
if(dojo.lang.isBoolean(this[x])){
this[x]=(args[x].toLowerCase()=="false")?false:true;
}else{
if(dojo.lang.isFunction(this[x])){
var tn=dojo.lang.nameAnonFunc(new Function(args[x]),this);
dojo.event.connect(this,x,this,tn);
}else{
if(dojo.lang.isArray(this[x])){
this[x]=args[x].split(";");
}else{
if(this[x] instanceof Date){
this[x]=new Date(Number(args[x]));
}else{
if(typeof this[x]=="object"){
var _7e6=args[x].split(";");
for(var y=0;y<_7e6.length;y++){
var si=_7e6[y].indexOf(":");
if((si!=-1)&&(_7e6[y].length>si)){
this[x][_7e6[y].substr(0,si).replace(/^\s+|\s+$/g,"")]=_7e6[y].substr(si+1);
}
}
}else{
this[x]=args[x];
}
}
}
}
}
}
}
}
}else{
this.extraArgs[x]=args[x];
}
}
},postMixInProperties:function(){
},initialize:function(args,frag){
return false;
},postInitialize:function(args,frag){
return false;
},postCreate:function(args,frag){
return false;
},uninitialize:function(){
return false;
},buildRendering:function(){
dj_unimplemented("dojo.widget.Widget.buildRendering, on "+this.toString()+", ");
return false;
},destroyRendering:function(){
dj_unimplemented("dojo.widget.Widget.destroyRendering");
return false;
},cleanUp:function(){
dj_unimplemented("dojo.widget.Widget.cleanUp");
return false;
},addedTo:function(_7ee){
},addChild:function(_7ef){
dj_unimplemented("dojo.widget.Widget.addChild");
return false;
},addChildAtIndex:function(_7f0,_7f1){
dj_unimplemented("dojo.widget.Widget.addChildAtIndex");
return false;
},removeChild:function(_7f2){
dj_unimplemented("dojo.widget.Widget.removeChild");
return false;
},removeChildAtIndex:function(_7f3){
dj_unimplemented("dojo.widget.Widget.removeChildAtIndex");
return false;
},resize:function(_7f4,_7f5){
this.setWidth(_7f4);
this.setHeight(_7f5);
},setWidth:function(_7f6){
if((typeof _7f6=="string")&&(_7f6.substr(-1)=="%")){
this.setPercentageWidth(_7f6);
}else{
this.setNativeWidth(_7f6);
}
},setHeight:function(_7f7){
if((typeof _7f7=="string")&&(_7f7.substr(-1)=="%")){
this.setPercentageHeight(_7f7);
}else{
this.setNativeHeight(_7f7);
}
},setPercentageHeight:function(_7f8){
return false;
},setNativeHeight:function(_7f9){
return false;
},setPercentageWidth:function(_7fa){
return false;
},setNativeWidth:function(_7fb){
return false;
},getPreviousSibling:function(){
var idx=this.getParentIndex();
if(idx<=0){
return null;
}
return this.getSiblings()[idx-1];
},getSiblings:function(){
return this.parent.children;
},getParentIndex:function(){
return dojo.lang.indexOf(this.getSiblings(),this,true);
},getNextSibling:function(){
var idx=this.getParentIndex();
if(idx==this.getSiblings().length-1){
return null;
}
if(idx<0){
return null;
}
return this.getSiblings()[idx+1];
}});
dojo.widget.lcArgsCache={};
dojo.widget.tags={};
dojo.widget.tags.addParseTreeHandler=function(type){
var _7ff=type.toLowerCase();
this[_7ff]=function(_800,_801,_802,_803,_804){
return dojo.widget.buildWidgetFromParseTree(_7ff,_800,_801,_802,_803,_804);
};
};
dojo.widget.tags.addParseTreeHandler("dojo:widget");
dojo.widget.tags["dojo:propertyset"]=function(_805,_806,_807){
var _808=_806.parseProperties(_805["dojo:propertyset"]);
};
dojo.widget.tags["dojo:connect"]=function(_809,_80a,_80b){
var _80c=_80a.parseProperties(_809["dojo:connect"]);
};
dojo.widget.buildWidgetFromParseTree=function(type,frag,_80f,_810,_811,_812){
var _813=type.split(":");
_813=(_813.length==2)?_813[1]:type;
var _814=_812||_80f.parseProperties(frag["dojo:"+_813]);
var _815=dojo.widget.manager.getImplementation(_813);
if(!_815){
throw new Error("cannot find \""+_813+"\" widget");
}else{
if(!_815.create){
throw new Error("\""+_813+"\" widget object does not appear to implement *Widget");
}
}
_814["dojoinsertionindex"]=_811;
var ret=_815.create(_814,frag,_810);
return ret;
};
dojo.provide("dojo.widget.Parse");
dojo.require("dojo.widget.Manager");
dojo.require("dojo.dom");
dojo.widget.Parse=function(_817){
this.propertySetsList=[];
this.fragment=_817;
this.createComponents=function(_818,_819){
var _81a=dojo.widget.tags;
var _81b=[];
for(var item in _818){
var _81d=false;
try{
if(_818[item]&&(_818[item]["tagName"])&&(_818[item]!=_818["nodeRef"])){
var tn=new String(_818[item]["tagName"]);
var tna=tn.split(";");
for(var x=0;x<tna.length;x++){
var ltn=(tna[x].replace(/^\s+|\s+$/g,"")).toLowerCase();
if(_81a[ltn]){
_81d=true;
_818[item].tagName=ltn;
var ret=_81a[ltn](_818[item],this,_819,_818[item]["index"]);
_81b.push(ret);
}else{
if((dojo.lang.isString(ltn))&&(ltn.substr(0,5)=="dojo:")){
dojo.debug("no tag handler registed for type: ",ltn);
}
}
}
}
}
catch(e){
dojo.debug("fragment creation error:",e);
}
if((!_81d)&&(typeof _818[item]=="object")&&(_818[item]!=_818.nodeRef)&&(_818[item]!=_818["tagName"])){
_81b.push(this.createComponents(_818[item],_819));
}
}
return _81b;
};
this.parsePropertySets=function(_823){
return [];
var _824=[];
for(var item in _823){
if((_823[item]["tagName"]=="dojo:propertyset")){
_824.push(_823[item]);
}
}
this.propertySetsList.push(_824);
return _824;
};
this.parseProperties=function(_826){
var _827={};
for(var item in _826){
if((_826[item]==_826["tagName"])||(_826[item]==_826.nodeRef)){
}else{
if((_826[item]["tagName"])&&(dojo.widget.tags[_826[item].tagName.toLowerCase()])){
}else{
if((_826[item][0])&&(_826[item][0].value!="")){
try{
if(item.toLowerCase()=="dataprovider"){
var _829=this;
this.getDataProvider(_829,_826[item][0].value);
_827.dataProvider=this.dataProvider;
}
_827[item]=_826[item][0].value;
var _82a=this.parseProperties(_826[item]);
for(var _82b in _82a){
_827[_82b]=_82a[_82b];
}
}
catch(e){
dojo.debug(e);
}
}
}
}
}
return _827;
};
this.getDataProvider=function(_82c,_82d){
dojo.io.bind({url:_82d,load:function(type,_82f){
if(type=="load"){
_82c.dataProvider=_82f;
}
},mimetype:"text/javascript",sync:true});
};
this.getPropertySetById=function(_830){
for(var x=0;x<this.propertySetsList.length;x++){
if(_830==this.propertySetsList[x]["id"][0].value){
return this.propertySetsList[x];
}
}
return "";
};
this.getPropertySetsByType=function(_832){
var _833=[];
for(var x=0;x<this.propertySetsList.length;x++){
var cpl=this.propertySetsList[x];
var cpcc=cpl["componentClass"]||cpl["componentType"]||null;
if((cpcc)&&(propertySetId==cpcc[0].value)){
_833.push(cpl);
}
}
return _833;
};
this.getPropertySets=function(_837){
var ppl="dojo:propertyproviderlist";
var _839=[];
var _83a=_837["tagName"];
if(_837[ppl]){
var _83b=_837[ppl].value.split(" ");
for(propertySetId in _83b){
if((propertySetId.indexOf("..")==-1)&&(propertySetId.indexOf("://")==-1)){
var _83c=this.getPropertySetById(propertySetId);
if(_83c!=""){
_839.push(_83c);
}
}else{
}
}
}
return (this.getPropertySetsByType(_83a)).concat(_839);
};
this.createComponentFromScript=function(_83d,_83e,_83f){
var ltn="dojo:"+_83e.toLowerCase();
if(dojo.widget.tags[ltn]){
_83f.fastMixIn=true;
return [dojo.widget.tags[ltn](_83f,this,null,null,_83f)];
}else{
if(ltn.substr(0,5)=="dojo:"){
dojo.debug("no tag handler registed for type: ",ltn);
}
}
};
};
dojo.widget._parser_collection={"dojo":new dojo.widget.Parse()};
dojo.widget.getParser=function(name){
if(!name){
name="dojo";
}
if(!this._parser_collection[name]){
this._parser_collection[name]=new dojo.widget.Parse();
}
return this._parser_collection[name];
};
dojo.widget.createWidget=function(name,_843,_844,_845){
function fromScript(_846,name,_848){
var _849=name.toLowerCase();
var _84a="dojo:"+_849;
_848[_84a]={dojotype:[{value:_849}],nodeRef:_846,fastMixIn:true};
return dojo.widget.getParser().createComponentFromScript(_846,name,_848,true);
}
if(typeof name!="string"&&typeof _843=="string"){
dojo.deprecated("dojo.widget.createWidget","argument order is now of the form "+"dojo.widget.createWidget(NAME, [PROPERTIES, [REFERENCENODE, [POSITION]]])");
return fromScript(name,_843,_844);
}
_843=_843||{};
var _84b=false;
var tn=null;
var h=dojo.render.html.capable;
if(h){
tn=document.createElement("span");
}
if(!_844){
_84b=true;
_844=tn;
if(h){
document.body.appendChild(_844);
}
}else{
if(_845){
dojo.dom.insertAtPosition(tn,_844,_845);
}else{
tn=_844;
}
}
var _84e=fromScript(tn,name,_843);
if(!_84e[0]||typeof _84e[0].widgetType=="undefined"){
throw new Error("createWidget: Creation of \""+name+"\" widget failed.");
}
if(_84b){
if(_84e[0].domNode.parentNode){
_84e[0].domNode.parentNode.removeChild(_84e[0].domNode);
}
}
return _84e[0];
};
dojo.widget.fromScript=function(name,_850,_851,_852){
dojo.deprecated("dojo.widget.fromScript"," use "+"dojo.widget.createWidget instead");
return dojo.widget.createWidget(name,_850,_851,_852);
};
dojo.provide("dojo.widget.DomWidget");
dojo.require("dojo.event.*");
dojo.require("dojo.widget.Widget");
dojo.require("dojo.dom");
dojo.require("dojo.xml.Parse");
dojo.require("dojo.uri.*");
dojo.widget._cssFiles={};
dojo.widget.defaultStrings={dojoRoot:dojo.hostenv.getBaseScriptUri(),baseScriptUri:dojo.hostenv.getBaseScriptUri()};
dojo.widget.buildFromTemplate=function(obj,_854,_855,_856){
var _857=_854||obj.templatePath;
var _858=_855||obj.templateCssPath;
if(!_858&&obj.templateCSSPath){
obj.templateCssPath=_858=obj.templateCSSPath;
obj.templateCSSPath=null;
dj_deprecated("templateCSSPath is deprecated, use templateCssPath");
}
if(_857&&!(_857 instanceof dojo.uri.Uri)){
_857=dojo.uri.dojoUri(_857);
dj_deprecated("templatePath should be of type dojo.uri.Uri");
}
if(_858&&!(_858 instanceof dojo.uri.Uri)){
_858=dojo.uri.dojoUri(_858);
dj_deprecated("templateCssPath should be of type dojo.uri.Uri");
}
var _859=dojo.widget.DomWidget.templates;
if(!obj["widgetType"]){
do{
var _85a="__dummyTemplate__"+dojo.widget.buildFromTemplate.dummyCount++;
}while(_859[_85a]);
obj.widgetType=_85a;
}
if((_858)&&(!dojo.widget._cssFiles[_858])){
dojo.style.insertCssFile(_858);
obj.templateCssPath=null;
dojo.widget._cssFiles[_858]=true;
}
var ts=_859[obj.widgetType];
if(!ts){
_859[obj.widgetType]={};
ts=_859[obj.widgetType];
}
if(!obj.templateString){
obj.templateString=_856||ts["string"];
}
if(!obj.templateNode){
obj.templateNode=ts["node"];
}
if((!obj.templateNode)&&(!obj.templateString)&&(_857)){
var _85c=dojo.hostenv.getText(_857);
if(_85c){
var _85d=_85c.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
if(_85d){
_85c=_85d[1];
}
}else{
_85c="";
}
obj.templateString=_85c;
ts.string=_85c;
}
if(!ts["string"]){
ts.string=obj.templateString;
}
};
dojo.widget.buildFromTemplate.dummyCount=0;
dojo.widget.attachProperties=["dojoAttachPoint","id"];
dojo.widget.eventAttachProperty="dojoAttachEvent";
dojo.widget.onBuildProperty="dojoOnBuild";
dojo.widget.attachTemplateNodes=function(_85e,_85f,_860){
var _861=dojo.dom.ELEMENT_NODE;
function trim(str){
return str.replace(/^\s+|\s+$/g,"");
}
if(!_85e){
_85e=_85f.domNode;
}
if(_85e.nodeType!=_861){
return;
}
var _863=_85e.getElementsByTagName("*");
var _864=_85f;
for(var x=-1;x<_863.length;x++){
var _866=(x==-1)?_85e:_863[x];
var _867=[];
for(var y=0;y<this.attachProperties.length;y++){
var _869=_866.getAttribute(this.attachProperties[y]);
if(_869){
_867=_869.split(";");
for(var z=0;z<this.attachProperties.length;z++){
if((_85f[_867[z]])&&(dojo.lang.isArray(_85f[_867[z]]))){
_85f[_867[z]].push(_866);
}else{
_85f[_867[z]]=_866;
}
}
break;
}
}
var _86b=_866.getAttribute(this.templateProperty);
if(_86b){
_85f[_86b]=_866;
}
var _86c=_866.getAttribute(this.eventAttachProperty);
if(_86c){
var evts=_86c.split(";");
for(var y=0;y<evts.length;y++){
if((!evts[y])||(!evts[y].length)){
continue;
}
var _86e=null;
var tevt=trim(evts[y]);
if(evts[y].indexOf(":")>=0){
var _870=tevt.split(":");
tevt=trim(_870[0]);
_86e=trim(_870[1]);
}
if(!_86e){
_86e=tevt;
}
var tf=function(){
var ntf=new String(_86e);
return function(evt){
if(_864[ntf]){
_864[ntf](dojo.event.browser.fixEvent(evt));
}
};
}();
dojo.event.browser.addListener(_866,tevt,tf,false,true);
}
}
for(var y=0;y<_860.length;y++){
var _874=_866.getAttribute(_860[y]);
if((_874)&&(_874.length)){
var _86e=null;
var _875=_860[y].substr(4);
_86e=trim(_874);
var tf=function(){
var ntf=new String(_86e);
return function(evt){
if(_864[ntf]){
_864[ntf](dojo.event.browser.fixEvent(evt));
}
};
}();
dojo.event.browser.addListener(_866,_875,tf,false,true);
}
}
var _878=_866.getAttribute(this.onBuildProperty);
if(_878){
eval("var node = baseNode; var widget = targetObj; "+_878);
}
_866.id="";
}
};
dojo.widget.getDojoEventsFromStr=function(str){
var re=/(dojoOn([a-z]+)(\s?))=/gi;
var evts=str?str.match(re)||[]:[];
var ret=[];
var lem={};
for(var x=0;x<evts.length;x++){
if(evts[x].legth<1){
continue;
}
var cm=evts[x].replace(/\s/,"");
cm=(cm.slice(0,cm.length-1));
if(!lem[cm]){
lem[cm]=true;
ret.push(cm);
}
}
return ret;
};
dojo.widget.buildAndAttachTemplate=function(obj,_881,_882,_883,_884){
this.buildFromTemplate(obj,_881,_882,_883);
var node=dojo.dom.createNodesFromText(obj.templateString,true)[0];
this.attachTemplateNodes(node,_884||obj,dojo.widget.getDojoEventsFromStr(_883));
return node;
};
dojo.widget.DomWidget=function(){
dojo.widget.Widget.call(this);
if((arguments.length>0)&&(typeof arguments[0]=="object")){
this.create(arguments[0]);
}
};
dojo.inherits(dojo.widget.DomWidget,dojo.widget.Widget);
dojo.lang.extend(dojo.widget.DomWidget,{templateNode:null,templateString:null,preventClobber:false,domNode:null,containerNode:null,addChild:function(_886,_887,pos,ref,_88a){
if(!this.isContainer){
dojo.debug("dojo.widget.DomWidget.addChild() attempted on non-container widget");
return null;
}else{
this.addWidgetAsDirectChild(_886,_887,pos,ref,_88a);
this.registerChild(_886,_88a);
}
return _886;
},addWidgetAsDirectChild:function(_88b,_88c,pos,ref,_88f){
if((!this.containerNode)&&(!_88c)){
this.containerNode=this.domNode;
}
var cn=(_88c)?_88c:this.containerNode;
if(!pos){
pos="after";
}
if(!ref){
ref=cn.lastChild;
}
if(!_88f){
_88f=0;
}
_88b.domNode.setAttribute("dojoinsertionindex",_88f);
if(!ref){
cn.appendChild(_88b.domNode);
}else{
if(pos=="insertAtIndex"){
dojo.dom.insertAtIndex(_88b.domNode,ref.parentNode,_88f);
}else{
if((pos=="after")&&(ref===cn.lastChild)){
cn.appendChild(_88b.domNode);
}else{
dojo.dom.insertAtPosition(_88b.domNode,cn,pos);
}
}
}
},registerChild:function(_891,_892){
_891.dojoInsertionIndex=_892;
var idx=-1;
for(var i=0;i<this.children.length;i++){
if(this.children[i].dojoInsertionIndex<_892){
idx=i;
}
}
this.children.splice(idx+1,0,_891);
_891.parent=this;
_891.addedTo(this);
delete dojo.widget.manager.topWidgets[_891.widgetId];
},removeChild:function(_895){
for(var x=0;x<this.children.length;x++){
if(this.children[x]===_895){
this.children.splice(x,1);
break;
}
}
return _895;
},getFragNodeRef:function(frag){
if(!frag["dojo:"+this.widgetType.toLowerCase()]){
dojo.raise("Error: no frag for widget type "+this.widgetType+", id "+this.widgetId+" (maybe a widget has set it's type incorrectly)");
}
return (frag?frag["dojo:"+this.widgetType.toLowerCase()]["nodeRef"]:null);
},postInitialize:function(args,frag,_89a){
var _89b=this.getFragNodeRef(frag);
if(_89a&&(_89a.snarfChildDomOutput||!_89b)){
_89a.addWidgetAsDirectChild(this,"","insertAtIndex","",args["dojoinsertionindex"],_89b);
}else{
if(_89b){
if(this.domNode&&(this.domNode!==_89b)){
var _89c=_89b.parentNode.replaceChild(this.domNode,_89b);
}
}
}
if(_89a){
_89a.registerChild(this,args.dojoinsertionindex);
}else{
dojo.widget.manager.topWidgets[this.widgetId]=this;
}
if(this.isContainer){
var _89d=dojo.widget.getParser();
_89d.createComponents(frag,this);
}
},startResize:function(_89e){
dj_unimplemented("dojo.widget.DomWidget.startResize");
},updateResize:function(_89f){
dj_unimplemented("dojo.widget.DomWidget.updateResize");
},endResize:function(_8a0){
dj_unimplemented("dojo.widget.DomWidget.endResize");
},buildRendering:function(args,frag){
var ts=dojo.widget.DomWidget.templates[this.widgetType];
if((!this.preventClobber)&&((this.templatePath)||(this.templateNode)||((this["templateString"])&&(this.templateString.length))||((typeof ts!="undefined")&&((ts["string"])||(ts["node"]))))){
this.buildFromTemplate(args,frag);
}else{
this.domNode=this.getFragNodeRef(frag);
}
this.fillInTemplate(args,frag);
},buildFromTemplate:function(args,frag){
var ts=dojo.widget.DomWidget.templates[this.widgetType];
if(ts){
if(!this.templateString.length){
this.templateString=ts["string"];
}
if(!this.templateNode){
this.templateNode=ts["node"];
}
}
var _8a7=false;
var node=null;
var tstr=new String(this.templateString);
if((!this.templateNode)&&(this.templateString)){
_8a7=this.templateString.match(/\$\{([^\}]+)\}/g);
if(_8a7){
var hash=this.strings||{};
for(var key in dojo.widget.defaultStrings){
if(dojo.lang.isUndefined(hash[key])){
hash[key]=dojo.widget.defaultStrings[key];
}
}
for(var i=0;i<_8a7.length;i++){
var key=_8a7[i];
key=key.substring(2,key.length-1);
var kval=(key.substring(0,5)=="this.")?this[key.substring(5)]:hash[key];
var _8ae;
if((kval)||(dojo.lang.isString(kval))){
_8ae=(dojo.lang.isFunction(kval))?kval.call(this,key,this.templateString):kval;
tstr=tstr.replace(_8a7[i],_8ae);
}
}
}else{
this.templateNode=this.createNodesFromText(this.templateString,true)[0];
ts.node=this.templateNode;
}
}
if((!this.templateNode)&&(!_8a7)){
dojo.debug("weren't able to create template!");
return false;
}else{
if(!_8a7){
node=this.templateNode.cloneNode(true);
if(!node){
return false;
}
}else{
node=this.createNodesFromText(tstr,true)[0];
}
}
this.domNode=node;
this.attachTemplateNodes(this.domNode,this);
if(this.isContainer&&this.containerNode){
var src=this.getFragNodeRef(frag);
if(src){
dojo.dom.moveChildren(src,this.containerNode);
}
}
},attachTemplateNodes:function(_8b0,_8b1){
if(!_8b1){
_8b1=this;
}
return dojo.widget.attachTemplateNodes(_8b0,_8b1,dojo.widget.getDojoEventsFromStr(this.templateString));
},fillInTemplate:function(){
},destroyRendering:function(){
try{
var _8b2=this.domNode.parentNode.removeChild(this.domNode);
delete _8b2;
}
catch(e){
}
},cleanUp:function(){
},getContainerHeight:function(){
return dojo.html.getInnerHeight(this.domNode.parentNode);
},getContainerWidth:function(){
return dojo.html.getInnerWidth(this.domNode.parentNode);
},createNodesFromText:function(){
dj_unimplemented("dojo.widget.DomWidget.createNodesFromText");
}});
dojo.widget.DomWidget.templates={};
dojo.provide("dojo.widget.HtmlWidget");
dojo.require("dojo.widget.DomWidget");
dojo.require("dojo.html");
dojo.require("dojo.lang.extras");
dojo.require("dojo.lang.func");
dojo.widget.HtmlWidget=function(args){
dojo.widget.DomWidget.call(this);
};
dojo.inherits(dojo.widget.HtmlWidget,dojo.widget.DomWidget);
dojo.lang.extend(dojo.widget.HtmlWidget,{widgetType:"HtmlWidget",templateCssPath:null,templatePath:null,allowResizeX:true,allowResizeY:true,resizeGhost:null,initialResizeCoords:null,toggle:"plain",toggleDuration:150,animationInProgress:false,initialize:function(args,frag){
},postMixInProperties:function(args,frag){
this.toggleObj=dojo.widget.HtmlWidget.toggle[this.toggle.toLowerCase()]||dojo.widget.HtmlWidget.toggle.plain;
},getContainerHeight:function(){
dj_unimplemented("dojo.widget.HtmlWidget.getContainerHeight");
},getContainerWidth:function(){
return this.parent.domNode.offsetWidth;
},setNativeHeight:function(_8b8){
var ch=this.getContainerHeight();
},resizeSoon:function(){
if(this.isVisible()){
dojo.lang.setTimeout(this,this.onResized,0);
}
},createNodesFromText:function(txt,wrap){
return dojo.html.createNodesFromText(txt,wrap);
},_old_buildFromTemplate:dojo.widget.DomWidget.prototype.buildFromTemplate,buildFromTemplate:function(args,frag){
if(dojo.widget.DomWidget.templates[this.widgetType]){
var ot=dojo.widget.DomWidget.templates[this.widgetType];
dojo.widget.DomWidget.templates[this.widgetType]={};
}
if(args["templatecsspath"]){
args["templateCssPath"]=args["templatecsspath"];
}
if(args["templatepath"]){
args["templatePath"]=args["templatepath"];
}
dojo.widget.buildFromTemplate(this,args["templatePath"],args["templateCssPath"]);
this._old_buildFromTemplate(args,frag);
dojo.widget.DomWidget.templates[this.widgetType]=ot;
},destroyRendering:function(_8bf){
try{
var _8c0=this.domNode.parentNode.removeChild(this.domNode);
if(!_8bf){
dojo.event.browser.clean(_8c0);
}
delete _8c0;
}
catch(e){
}
},isVisible:function(){
return dojo.style.isVisible(this.domNode);
},doToggle:function(){
this.isVisible()?this.hide():this.show();
},show:function(){
this.animationInProgress=true;
this.toggleObj.show(this.domNode,this.toggleDuration,this.explodeSrc,dojo.lang.hitch(this,this.onShow));
},onShow:function(){
this.animationInProgress=false;
},hide:function(){
this.animationInProgress=true;
this.toggleObj.hide(this.domNode,this.toggleDuration,this.explodeSrc,dojo.lang.hitch(this,this.onHide));
},onHide:function(){
this.animationInProgress=false;
}});
dojo.widget.HtmlWidget.toggle={};
dojo.widget.HtmlWidget.toggle.plain={show:function(node,_8c2,_8c3,_8c4){
dojo.style.show(node);
if(dojo.lang.isFunction(_8c4)){
_8c4();
}
},hide:function(node,_8c6,_8c7,_8c8){
dojo.html.hide(node);
if(dojo.lang.isFunction(_8c8)){
_8c8();
}
}};
dojo.widget.HtmlWidget.toggle.fade={show:function(node,_8ca,_8cb,_8cc){
dojo.fx.html.fadeShow(node,_8ca,_8cc);
},hide:function(node,_8ce,_8cf,_8d0){
dojo.fx.html.fadeHide(node,_8ce,_8d0);
}};
dojo.widget.HtmlWidget.toggle.wipe={show:function(node,_8d2,_8d3,_8d4){
dojo.fx.html.wipeIn(node,_8d2,_8d4);
},hide:function(node,_8d6,_8d7,_8d8){
dojo.fx.html.wipeOut(node,_8d6,_8d8);
}};
dojo.widget.HtmlWidget.toggle.explode={show:function(node,_8da,_8db,_8dc){
dojo.fx.html.explode(_8db||[0,0,0,0],node,_8da,_8dc);
},hide:function(node,_8de,_8df,_8e0){
dojo.fx.html.implode(node,_8df||[0,0,0,0],_8de,_8e0);
}};
dojo.hostenv.conditionalLoadModule({common:["dojo.xml.Parse","dojo.widget.Widget","dojo.widget.Parse","dojo.widget.Manager"],browser:["dojo.widget.DomWidget","dojo.widget.HtmlWidget"],dashboard:["dojo.widget.DomWidget","dojo.widget.HtmlWidget"],svg:["dojo.widget.SvgWidget"]});
dojo.hostenv.moduleLoaded("dojo.widget.*");
dojo.provide("dojo.math.points");
dojo.require("dojo.math");
dojo.math.points={translate:function(a,b){
if(a.length!=b.length){
dojo.raise("dojo.math.translate: points not same size (a:["+a+"], b:["+b+"])");
}
var c=new Array(a.length);
for(var i=0;i<a.length;i++){
c[i]=a[i]+b[i];
}
return c;
},midpoint:function(a,b){
if(a.length!=b.length){
dojo.raise("dojo.math.midpoint: points not same size (a:["+a+"], b:["+b+"])");
}
var c=new Array(a.length);
for(var i=0;i<a.length;i++){
c[i]=(a[i]+b[i])/2;
}
return c;
},invert:function(a){
var b=new Array(a.length);
for(var i=0;i<a.length;i++){
b[i]=-a[i];
}
return b;
},distance:function(a,b){
return Math.sqrt(Math.pow(b[0]-a[0],2)+Math.pow(b[1]-a[1],2));
}};
dojo.hostenv.conditionalLoadModule({common:[["dojo.math",false,false],["dojo.math.curves",false,false],["dojo.math.points",false,false]]});
dojo.hostenv.moduleLoaded("dojo.math.*");

