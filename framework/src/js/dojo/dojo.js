/*
	Copyright (c) 2004-2006, The Dojo Foundation
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
if(dj_undef("dojo")){
var dojo={};
}
dojo.version={major:0,minor:3,patch:0,flag:"+",revision:Number("$Rev: 4030 $".match(/[0-9]+/)[0]),toString:function(){
with(dojo.version){
return major+"."+minor+"."+patch+flag+" ("+revision+")";
}
}};
dojo.evalProp=function(_3,_4,_5){
return (_4&&!dj_undef(_3,_4)?_4[_3]:(_5?(_4[_3]={}):undefined));
};
dojo.parseObjPath=function(_6,_7,_8){
var _9=(_7?_7:dj_global);
var _a=_6.split(".");
var _b=_a.pop();
for(var i=0,l=_a.length;i<l&&_9;i++){
_9=dojo.evalProp(_a[i],_9,_8);
}
return {obj:_9,prop:_b};
};
dojo.evalObjPath=function(_d,_e){
if(typeof _d!="string"){
return dj_global;
}
if(_d.indexOf(".")==-1){
return dojo.evalProp(_d,dj_global,_e);
}
with(dojo.parseObjPath(_d,dj_global,_e)){
return dojo.evalProp(prop,obj,_e);
}
};
dojo.errorToString=function(_f){
return ((!dj_undef("message",_f))?_f.message:(dj_undef("description",_f)?_f:_f.description));
};
dojo.raise=function(_10,_11){
if(_11){
_10=_10+": "+dojo.errorToString(_11);
}
var he=dojo.hostenv;
if((!dj_undef("hostenv",dojo))&&(!dj_undef("println",dojo.hostenv))){
dojo.hostenv.println("FATAL: "+_10);
}
throw Error(_10);
};
dojo.debug=function(){
};
dojo.debugShallow=function(obj){
};
dojo.profile={start:function(){
},end:function(){
},stop:function(){
},dump:function(){
}};
function dj_eval(s){
return dj_global.eval?dj_global.eval(s):eval(s);
}
dojo.unimplemented=function(_15,_16){
var _17="'"+_15+"' not implemented";
if((!dj_undef(_16))&&(_16)){
_17+=" "+_16;
}
dojo.raise(_17);
};
dojo.deprecated=function(_18,_19,_1a){
var _1b="DEPRECATED: "+_18;
if(_19){
_1b+=" "+_19;
}
if(_1a){
_1b+=" -- will be removed in version: "+_1a;
}
dojo.debug(_1b);
};
dojo.inherits=function(_1c,_1d){
if(typeof _1d!="function"){
dojo.raise("dojo.inherits: superclass argument ["+_1d+"] must be a function (subclass: ["+_1c+"']");
}
_1c.prototype=new _1d();
_1c.prototype.constructor=_1c;
_1c.superclass=_1d.prototype;
_1c["super"]=_1d.prototype;
};
dojo.render=(function(){
function vscaffold(_1e,_1f){
var tmp={capable:false,support:{builtin:false,plugin:false},prefixes:_1e};
for(var x in _1f){
tmp[x]=false;
}
return tmp;
}
return {name:"",ver:dojo.version,os:{win:false,linux:false,osx:false},html:vscaffold(["html"],["ie","opera","khtml","safari","moz"]),svg:vscaffold(["svg"],["corel","adobe","batik"]),vml:vscaffold(["vml"],["ie"]),swf:vscaffold(["Swf","Flash","Mm"],["mm"]),swt:vscaffold(["Swt"],["ibm"])};
})();
dojo.hostenv=(function(){
var _22={isDebug:false,allowQueryConfig:false,baseScriptUri:"",baseRelativePath:"",libraryScriptUri:"",iePreventClobber:false,ieClobberMinimal:true,preventBackButtonFix:true,searchIds:[],parseWidgets:true};
if(typeof djConfig=="undefined"){
djConfig=_22;
}else{
for(var _23 in _22){
if(typeof djConfig[_23]=="undefined"){
djConfig[_23]=_22[_23];
}
}
}
return {name_:"(unset)",version_:"(unset)",getName:function(){
return this.name_;
},getVersion:function(){
return this.version_;
},getText:function(uri){
dojo.unimplemented("getText","uri="+uri);
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
var _26=uri.lastIndexOf("/");
djConfig.baseScriptUri=djConfig.baseRelativePath;
return djConfig.baseScriptUri;
};
(function(){
var _27={pkgFileName:"__package__",loading_modules_:{},loaded_modules_:{},addedToLoadingCount:[],removedFromLoadingCount:[],inFlightCount:0,modulePrefixes_:{dojo:{name:"dojo",value:"src"}},setModulePrefix:function(_28,_29){
this.modulePrefixes_[_28]={name:_28,value:_29};
},getModulePrefix:function(_2a){
var mp=this.modulePrefixes_;
if((mp[_2a])&&(mp[_2a]["name"])){
return mp[_2a].value;
}
return _2a;
},getTextStack:[],loadUriStack:[],loadedUris:[],post_load_:false,modulesLoadedListeners:[],loadNotifying:false};
for(var _2c in _27){
dojo.hostenv[_2c]=_27[_2c];
}
})();
dojo.hostenv.loadPath=function(_2d,_2e,cb){
if((_2d.charAt(0)=="/")||(_2d.match(/^\w+:/))){
dojo.raise("relpath '"+_2d+"'; must be relative");
}
var uri=this.getBaseScriptUri()+_2d;
if(djConfig.cacheBust&&dojo.render.html.capable){
uri+="?"+String(djConfig.cacheBust).replace(/\W+/g,"");
}
try{
return ((!_2e)?this.loadUri(uri,cb):this.loadUriAndCheck(uri,_2e,cb));
}
catch(e){
dojo.debug(e);
return false;
}
};
dojo.hostenv.loadUri=function(uri,cb){
if(this.loadedUris[uri]){
return 1;
}
var _33=this.getText(uri,null,true);
if(_33==null){
return 0;
}
this.loadedUris[uri]=true;
if(cb){
_33="("+_33+")";
}
var _34=dj_eval(_33);
if(cb){
cb(_34);
}
return 1;
};
dojo.hostenv.loadUriAndCheck=function(uri,_36,cb){
var ok=true;
try{
ok=this.loadUri(uri,cb);
}
catch(e){
dojo.debug("failed loading ",uri," with error: ",e);
}
return ((ok)&&(this.findModule(_36,false)))?true:false;
};
dojo.loaded=function(){
};
dojo.hostenv.loaded=function(){
this.loadNotifying=true;
this.post_load_=true;
var mll=this.modulesLoadedListeners;
for(var x=0;x<mll.length;x++){
mll[x]();
}
this.modulesLoadedListeners=[];
this.loadNotifying=false;
dojo.loaded();
};
dojo.addOnLoad=function(obj,_3c){
var dh=dojo.hostenv;
if(arguments.length==1){
dh.modulesLoadedListeners.push(obj);
}else{
if(arguments.length>1){
dh.modulesLoadedListeners.push(function(){
obj[_3c]();
});
}
}
if(dh.post_load_&&dh.inFlightCount==0&&!dh.loadNotifying){
dh.callLoaded();
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
dojo.hostenv.callLoaded();
}
};
dojo.hostenv.callLoaded=function(){
if(typeof setTimeout=="object"){
setTimeout("dojo.hostenv.loaded();",0);
}else{
dojo.hostenv.loaded();
}
};
dojo.hostenv.getModuleSymbols=function(_3e){
var _3f=_3e.split(".");
for(var i=_3f.length-1;i>0;i--){
var _41=_3f.slice(0,i).join(".");
var _42=this.getModulePrefix(_41);
if(_42!=_41){
_3f.splice(0,i,_42);
break;
}
}
return _3f;
};
dojo.hostenv._global_omit_module_check=false;
dojo.hostenv.loadModule=function(_43,_44,_45){
if(!_43){
return;
}
_45=this._global_omit_module_check||_45;
var _46=this.findModule(_43,false);
if(_46){
return _46;
}
if(dj_undef(_43,this.loading_modules_)){
this.addedToLoadingCount.push(_43);
}
this.loading_modules_[_43]=1;
var _47=_43.replace(/\./g,"/")+".js";
var _48=this.getModuleSymbols(_43);
var _49=_48[_48.length-1];
var _4a=_43.split(".");
if(_49=="*"){
_43=(_4a.slice(0,-1)).join(".");
while(_48.length){
_48.pop();
_48.push(this.pkgFileName);
_47=_48.join("/")+".js";
if(_47.charAt(0)=="/"){
_47=_47.slice(1);
}
ok=this.loadPath(_47,((!_45)?_43:null));
if(ok){
break;
}
_48.pop();
}
}else{
_47=_48.join("/")+".js";
_43=_4a.join(".");
var ok=this.loadPath(_47,((!_45)?_43:null));
if((!ok)&&(!_44)){
_48.pop();
while(_48.length){
_47=_48.join("/")+".js";
ok=this.loadPath(_47,((!_45)?_43:null));
if(ok){
break;
}
_48.pop();
_47=_48.join("/")+"/"+this.pkgFileName+".js";
if(_47.charAt(0)=="/"){
_47=_47.slice(1);
}
ok=this.loadPath(_47,((!_45)?_43:null));
if(ok){
break;
}
}
}
if((!ok)&&(!_45)){
dojo.raise("Could not load '"+_43+"'; last tried '"+_47+"'");
}
}
if(!_45&&!this["isXDomain"]){
_46=this.findModule(_43,false);
if(!_46){
dojo.raise("symbol '"+_43+"' is not defined after loading '"+_47+"'");
}
}
return _46;
};
dojo.hostenv.startPackage=function(_4c){
var _4d=dojo.evalObjPath((_4c.split(".").slice(0,-1)).join("."));
this.loaded_modules_[(new String(_4c)).toLowerCase()]=_4d;
var _4e=_4c.split(/\./);
if(_4e[_4e.length-1]=="*"){
_4e.pop();
}
return dojo.evalObjPath(_4e.join("."),true);
};
dojo.hostenv.findModule=function(_4f,_50){
var lmn=(new String(_4f)).toLowerCase();
if(this.loaded_modules_[lmn]){
return this.loaded_modules_[lmn];
}
var _52=dojo.evalObjPath(_4f);
if((_4f)&&(typeof _52!="undefined")&&(_52)){
this.loaded_modules_[lmn]=_52;
return _52;
}
if(_50){
dojo.raise("no loaded module named '"+_4f+"'");
}
return null;
};
dojo.kwCompoundRequire=function(_53){
var _54=_53["common"]||[];
var _55=(_53[dojo.hostenv.name_])?_54.concat(_53[dojo.hostenv.name_]||[]):_54.concat(_53["default"]||[]);
for(var x=0;x<_55.length;x++){
var _57=_55[x];
if(_57.constructor==Array){
dojo.hostenv.loadModule.apply(dojo.hostenv,_57);
}else{
dojo.hostenv.loadModule(_57);
}
}
};
dojo.require=function(){
dojo.hostenv.loadModule.apply(dojo.hostenv,arguments);
};
dojo.requireIf=function(){
if((arguments[0]===true)||(arguments[0]=="common")||(arguments[0]&&dojo.render[arguments[0]].capable)){
var _58=[];
for(var i=1;i<arguments.length;i++){
_58.push(arguments[i]);
}
dojo.require.apply(dojo,_58);
}
};
dojo.requireAfterIf=dojo.requireIf;
dojo.provide=function(){
return dojo.hostenv.startPackage.apply(dojo.hostenv,arguments);
};
dojo.setModulePrefix=function(_5a,_5b){
return dojo.hostenv.setModulePrefix(_5a,_5b);
};
dojo.exists=function(obj,_5d){
var p=_5d.split(".");
for(var i=0;i<p.length;i++){
if(!(obj[p[i]])){
return false;
}
obj=obj[p[i]];
}
return true;
};
if(typeof window=="undefined"){
dojo.raise("no window object");
}
(function(){
if(djConfig.allowQueryConfig){
var _60=document.location.toString();
var _61=_60.split("?",2);
if(_61.length>1){
var _62=_61[1];
var _63=_62.split("&");
for(var x in _63){
var sp=_63[x].split("=");
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
var _67=document.getElementsByTagName("script");
var _68=/(__package__|dojo|bootstrap1)\.js([\?\.]|$)/i;
for(var i=0;i<_67.length;i++){
var src=_67[i].getAttribute("src");
if(!src){
continue;
}
var m=src.match(_68);
if(m){
var _6c=src.substring(0,m.index);
if(src.indexOf("bootstrap1")>-1){
_6c+="../";
}
if(!this["djConfig"]){
djConfig={};
}
if(djConfig["baseScriptUri"]==""){
djConfig["baseScriptUri"]=_6c;
}
if(djConfig["baseRelativePath"]==""){
djConfig["baseRelativePath"]=_6c;
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
var _74=dua.indexOf("Gecko");
drh.mozilla=drh.moz=(_74>=0)&&(!drh.khtml);
if(drh.mozilla){
drh.geckoVersion=dua.substring(_74+6,_74+14);
}
drh.ie=(document.all)&&(!drh.opera);
drh.ie50=drh.ie&&dav.indexOf("MSIE 5.0")>=0;
drh.ie55=drh.ie&&dav.indexOf("MSIE 5.5")>=0;
drh.ie60=drh.ie&&dav.indexOf("MSIE 6.0")>=0;
dojo.locale=(drh.ie?navigator.userLanguage:navigator.language).toLowerCase();
dr.vml.capable=drh.ie;
drs.capable=f;
drs.support.plugin=f;
drs.support.builtin=f;
if(document.implementation&&document.implementation.hasFeature&&document.implementation.hasFeature("org.w3c.dom.svg","1.0")){
drs.capable=t;
drs.support.builtin=t;
drs.support.plugin=f;
}
})();
dojo.hostenv.startPackage("dojo.hostenv");
dojo.render.name=dojo.hostenv.name_="browser";
dojo.hostenv.searchIds=[];
var DJ_XMLHTTP_PROGIDS=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"];
dojo.hostenv.getXmlhttpObject=function(){
var _75=null;
var _76=null;
try{
_75=new XMLHttpRequest();
}
catch(e){
}
if(!_75){
for(var i=0;i<3;++i){
var _78=DJ_XMLHTTP_PROGIDS[i];
try{
_75=new ActiveXObject(_78);
}
catch(e){
_76=e;
}
if(_75){
DJ_XMLHTTP_PROGIDS=[_78];
break;
}
}
}
if(!_75){
return dojo.raise("XMLHTTP not available",_76);
}
return _75;
};
dojo.hostenv.getText=function(uri,_7a,_7b){
var _7c=this.getXmlhttpObject();
if(_7a){
_7c.onreadystatechange=function(){
if((4==_7c.readyState)&&(_7c["status"])){
if(_7c.status==200){
_7a(_7c.responseText);
}
}
};
}
_7c.open("GET",uri,_7a?true:false);
try{
_7c.send(null);
}
catch(e){
if(_7b&&!_7a){
return null;
}else{
throw e;
}
}
if(_7a){
return null;
}
return _7c.responseText;
};
dojo.hostenv.defaultDebugContainerId="dojoDebug";
dojo.hostenv._println_buffer=[];
dojo.hostenv._println_safe=false;
dojo.hostenv.println=function(_7d){
if(!dojo.hostenv._println_safe){
dojo.hostenv._println_buffer.push(_7d);
}else{
try{
var _7e=document.getElementById(djConfig.debugContainerId?djConfig.debugContainerId:dojo.hostenv.defaultDebugContainerId);
if(!_7e){
_7e=document.getElementsByTagName("body")[0]||document.body;
}
var div=document.createElement("div");
div.appendChild(document.createTextNode(_7d));
_7e.appendChild(div);
}
catch(e){
try{
document.write("<div>"+_7d+"</div>");
}
catch(e2){
window.status=_7d;
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
function dj_addNodeEvtHdlr(_80,_81,fp,_83){
var _84=_80["on"+_81]||function(){
};
_80["on"+_81]=function(){
fp.apply(_80,arguments);
_84.apply(_80,arguments);
};
return true;
}
dj_addNodeEvtHdlr(window,"load",function(){
if(arguments.callee.initialized){
return;
}
arguments.callee.initialized=true;
var _85=function(){
if(dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
};
if(dojo.hostenv.inFlightCount==0){
_85();
dojo.hostenv.modulesLoaded();
}else{
dojo.addOnLoad(_85);
}
});
dojo.hostenv.makeWidgets=function(){
var _86=[];
if(djConfig.searchIds&&djConfig.searchIds.length>0){
_86=_86.concat(djConfig.searchIds);
}
if(dojo.hostenv.searchIds&&dojo.hostenv.searchIds.length>0){
_86=_86.concat(dojo.hostenv.searchIds);
}
if((djConfig.parseWidgets)||(_86.length>0)){
if(dojo.evalObjPath("dojo.widget.Parse")){
try{
var _87=new dojo.xml.Parse();
if(_86.length>0){
for(var x=0;x<_86.length;x++){
var _89=document.getElementById(_86[x]);
if(!_89){
continue;
}
var _8a=_87.parseElement(_89,null,true);
dojo.widget.getParser().createComponents(_8a);
}
}else{
if(djConfig.parseWidgets){
var _8a=_87.parseElement(document.getElementsByTagName("body")[0]||document.body,null,true);
dojo.widget.getParser().createComponents(_8a);
}
}
}
catch(e){
dojo.debug("auto-build-widgets error:",e);
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
document.write("<style>v:*{ behavior:url(#default#VML); }</style>");
document.write("<xml:namespace ns=\"urn:schemas-microsoft-com:vml\" prefix=\"v\"/>");
}
}
catch(e){
}
dojo.hostenv.writeIncludes=function(){
};
dojo.byId=function(id,doc){
if(id&&(typeof id=="string"||id instanceof String)){
if(!doc){
doc=document;
}
return doc.getElementById(id);
}
return id;
};
(function(){
if(typeof dj_usingBootstrap!="undefined"){
return;
}
var _8d=false;
var _8e=false;
var _8f=false;
if((typeof this["load"]=="function")&&((typeof this["Packages"]=="function")||(typeof this["Packages"]=="object"))){
_8d=true;
}else{
if(typeof this["load"]=="function"){
_8e=true;
}else{
if(window.widget){
_8f=true;
}
}
}
var _90=[];
if((this["djConfig"])&&((djConfig["isDebug"])||(djConfig["debugAtAllCosts"]))){
_90.push("debug.js");
}
if((this["djConfig"])&&(djConfig["debugAtAllCosts"])&&(!_8d)&&(!_8f)){
_90.push("browser_debug.js");
}
if((this["djConfig"])&&(djConfig["compat"])){
_90.push("compat/"+djConfig["compat"]+".js");
}
var _91=djConfig["baseScriptUri"];
if((this["djConfig"])&&(djConfig["baseLoaderUri"])){
_91=djConfig["baseLoaderUri"];
}
for(var x=0;x<_90.length;x++){
var _93=_91+"src/"+_90[x];
if(_8d||_8e){
load(_93);
}else{
try{
document.write("<scr"+"ipt type='text/javascript' src='"+_93+"'></scr"+"ipt>");
}
catch(e){
var _94=document.createElement("script");
_94.src=_93;
document.getElementsByTagName("head")[0].appendChild(_94);
}
}
}
})();
dojo.fallback_locale="en";
dojo.normalizeLocale=function(_95){
return _95?_95.toLowerCase():dojo.locale;
};
dojo.requireLocalization=function(_96,_97,_98){
dojo.debug("EXPERIMENTAL: dojo.requireLocalization");
var _99=dojo.hostenv.getModuleSymbols(_96);
var _9a=_99.concat("nls").join("/");
_98=dojo.normalizeLocale(_98);
var _9b=_98.split("-");
var _9c=[];
for(var i=_9b.length;i>0;i--){
_9c.push(_9b.slice(0,i).join("-"));
}
if(_9c[_9c.length-1]!=dojo.fallback_locale){
_9c.push(dojo.fallback_locale);
}
var _9e=[_96,"_nls",_97].join(".");
var _9f=dojo.hostenv.startPackage(_9e);
dojo.hostenv.loaded_modules_[_9e]=_9f;
var _a0=false;
for(var i=_9c.length-1;i>=0;i--){
var loc=_9c[i];
var pkg=[_9e,loc].join(".");
var _a3=false;
if(!dojo.hostenv.findModule(pkg)){
dojo.hostenv.loaded_modules_[pkg]=null;
var _a4=[_9a,loc,_97].join("/")+".js";
_a3=dojo.hostenv.loadPath(_a4,null,function(_a5){
_9f[loc]=_a5;
if(_a0){
for(var x in _a0){
if(!_9f[loc][x]){
_9f[loc][x]=_a0[x];
}
}
}
});
}else{
_a3=true;
}
if(_a3&&_9f[loc]){
_a0=_9f[loc];
}
}
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
dojo.string.repeat=function(str,_ad,_ae){
var out="";
for(var i=0;i<_ad;i++){
out+=str;
if(_ae&&i<_ad-1){
out+=_ae;
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
dojo.lang.mixin=function(obj,_bd){
var _be={};
for(var x in _bd){
if(typeof _be[x]=="undefined"||_be[x]!=_bd[x]){
obj[x]=_bd[x];
}
}
if(dojo.render.html.ie&&dojo.lang.isFunction(_bd["toString"])&&_bd["toString"]!=obj["toString"]){
obj.toString=_bd.toString;
}
return obj;
};
dojo.lang.extend=function(_c0,_c1){
this.mixin(_c0.prototype,_c1);
};
dojo.lang.find=function(arr,val,_c4,_c5){
if(!dojo.lang.isArrayLike(arr)&&dojo.lang.isArrayLike(val)){
var a=arr;
arr=val;
val=a;
}
var _c7=dojo.lang.isString(arr);
if(_c7){
arr=arr.split("");
}
if(_c5){
var _c8=-1;
var i=arr.length-1;
var end=-1;
}else{
var _c8=1;
var i=0;
var end=arr.length;
}
if(_c4){
while(i!=end){
if(arr[i]===val){
return i;
}
i+=_c8;
}
}else{
while(i!=end){
if(arr[i]==val){
return i;
}
i+=_c8;
}
}
return -1;
};
dojo.lang.indexOf=dojo.lang.find;
dojo.lang.findLast=function(arr,val,_cd){
return dojo.lang.find(arr,val,_cd,true);
};
dojo.lang.lastIndexOf=dojo.lang.findLast;
dojo.lang.inArray=function(arr,val){
return dojo.lang.find(arr,val)>-1;
};
dojo.lang.isObject=function(wh){
if(!wh){
return false;
}
return (typeof wh=="object"||dojo.lang.isArray(wh)||dojo.lang.isFunction(wh));
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
if(!wh){
return false;
}
return (wh instanceof Function||typeof wh=="function");
};
dojo.lang.isString=function(wh){
return (wh instanceof String||typeof wh=="string");
};
dojo.lang.isAlien=function(wh){
if(!wh){
return false;
}
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
dojo.provide("dojo.lang.extras");
dojo.require("dojo.lang.common");
dojo.lang.setTimeout=function(_d9,_da){
var _db=window,argsStart=2;
if(!dojo.lang.isFunction(_d9)){
_db=_d9;
_d9=_da;
_da=arguments[2];
argsStart++;
}
if(dojo.lang.isString(_d9)){
_d9=_db[_d9];
}
var _dc=[];
for(var i=argsStart;i<arguments.length;i++){
_dc.push(arguments[i]);
}
return setTimeout(function(){
_d9.apply(_db,_dc);
},_da);
};
dojo.lang.getNameInObj=function(ns,_df){
if(!ns){
ns=dj_global;
}
for(var x in ns){
if(ns[x]===_df){
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
dojo.lang.getObjPathValue=function(_e4,_e5,_e6){
with(dojo.parseObjPath(_e4,_e5,_e6)){
return dojo.evalProp(prop,obj,_e6);
}
};
dojo.lang.setObjPathValue=function(_e7,_e8,_e9,_ea){
if(arguments.length<4){
_ea=true;
}
with(dojo.parseObjPath(_e7,_e9,_ea)){
if(obj&&(_ea||(prop in obj))){
obj[prop]=_e8;
}
}
};
dojo.provide("dojo.io.IO");
dojo.require("dojo.string");
dojo.require("dojo.lang.extras");
dojo.io.transports=[];
dojo.io.hdlrFuncNames=["load","error","timeout"];
dojo.io.Request=function(url,_ec,_ed,_ee){
if((arguments.length==1)&&(arguments[0].constructor==Object)){
this.fromKwArgs(arguments[0]);
}else{
this.url=url;
if(_ec){
this.mimetype=_ec;
}
if(_ed){
this.transport=_ed;
}
if(arguments.length>=4){
this.changeUrl=_ee;
}
}
};
dojo.lang.extend(dojo.io.Request,{url:"",mimetype:"text/plain",method:"GET",content:undefined,transport:undefined,changeUrl:undefined,formNode:undefined,sync:false,bindSuccess:false,useCache:false,preventCache:false,load:function(_ef,_f0,evt){
},error:function(_f2,_f3){
},timeout:function(_f4){
},handle:function(){
},timeoutSeconds:0,abort:function(){
},fromKwArgs:function(_f5){
if(_f5["url"]){
_f5.url=_f5.url.toString();
}
if(_f5["formNode"]){
_f5.formNode=dojo.byId(_f5.formNode);
}
if(!_f5["method"]&&_f5["formNode"]&&_f5["formNode"].method){
_f5.method=_f5["formNode"].method;
}
if(!_f5["handle"]&&_f5["handler"]){
_f5.handle=_f5.handler;
}
if(!_f5["load"]&&_f5["loaded"]){
_f5.load=_f5.loaded;
}
if(!_f5["changeUrl"]&&_f5["changeURL"]){
_f5.changeUrl=_f5.changeURL;
}
_f5.encoding=dojo.lang.firstValued(_f5["encoding"],djConfig["bindEncoding"],"");
_f5.sendTransport=dojo.lang.firstValued(_f5["sendTransport"],djConfig["ioSendTransport"],false);
var _f6=dojo.lang.isFunction;
for(var x=0;x<dojo.io.hdlrFuncNames.length;x++){
var fn=dojo.io.hdlrFuncNames[x];
if(_f6(_f5[fn])){
continue;
}
if(_f6(_f5["handle"])){
_f5[fn]=_f5.handle;
}
}
dojo.lang.mixin(this,_f5);
}});
dojo.io.Error=function(msg,_fa,num){
this.message=msg;
this.type=_fa||"unknown";
this.number=num||0;
};
dojo.io.transports.addTransport=function(_fc){
this.push(_fc);
this[_fc]=dojo.io[_fc];
};
dojo.io.bind=function(_fd){
if(!(_fd instanceof dojo.io.Request)){
try{
_fd=new dojo.io.Request(_fd);
}
catch(e){
dojo.debug(e);
}
}
var _fe="";
if(_fd["transport"]){
_fe=_fd["transport"];
if(!this[_fe]){
return _fd;
}
}else{
for(var x=0;x<dojo.io.transports.length;x++){
var tmp=dojo.io.transports[x];
if((this[tmp])&&(this[tmp].canHandle(_fd))){
_fe=tmp;
}
}
if(_fe==""){
return _fd;
}
}
this[_fe].bind(_fd);
_fd.bindSuccess=true;
return _fd;
};
dojo.io.queueBind=function(_101){
if(!(_101 instanceof dojo.io.Request)){
try{
_101=new dojo.io.Request(_101);
}
catch(e){
dojo.debug(e);
}
}
var _102=_101.load;
_101.load=function(){
dojo.io._queueBindInFlight=false;
var ret=_102.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
var _104=_101.error;
_101.error=function(){
dojo.io._queueBindInFlight=false;
var ret=_104.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
dojo.io._bindQueue.push(_101);
dojo.io._dispatchNextQueueBind();
return _101;
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
dojo.io.argsFromMap=function(map,_107,last){
var enc=/utf/i.test(_107||"")?encodeURIComponent:dojo.string.encodeAscii;
var _10a=[];
var _10b=new Object();
for(var name in map){
var _10d=function(elt){
var val=enc(name)+"="+enc(elt);
_10a[(last==name)?"push":"unshift"](val);
};
if(!_10b[name]){
var _110=map[name];
if(dojo.lang.isArray(_110)){
dojo.lang.forEach(_110,_10d);
}else{
_10d(_110);
}
}
}
return _10a.join("&");
};
dojo.io.setIFrameSrc=function(_111,src,_113){
try{
var r=dojo.render.html;
if(!_113){
if(r.safari){
_111.location=src;
}else{
frames[_111.name].location=src;
}
}else{
var idoc;
if(r.ie){
idoc=_111.contentWindow.document;
}else{
if(r.safari){
idoc=_111.document;
}else{
idoc=_111.contentWindow;
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
try{
return (typeof obj[name]!="undefined");
}
catch(e){
return false;
}
};
dojo.lang.isEmpty=function(obj){
if(dojo.lang.isObject(obj)){
var tmp={};
var _11a=0;
for(var x in obj){
if(obj[x]&&(!tmp[x])){
_11a++;
break;
}
}
return (_11a==0);
}else{
if(dojo.lang.isArrayLike(obj)||dojo.lang.isString(obj)){
return obj.length==0;
}
}
};
dojo.lang.map=function(arr,obj,_11e){
var _11f=dojo.lang.isString(arr);
if(_11f){
arr=arr.split("");
}
if(dojo.lang.isFunction(obj)&&(!_11e)){
_11e=obj;
obj=dj_global;
}else{
if(dojo.lang.isFunction(obj)&&_11e){
var _120=obj;
obj=_11e;
_11e=_120;
}
}
if(Array.map){
var _121=Array.map(arr,_11e,obj);
}else{
var _121=[];
for(var i=0;i<arr.length;++i){
_121.push(_11e.call(obj,arr[i]));
}
}
if(_11f){
return _121.join("");
}else{
return _121;
}
};
dojo.lang.forEach=function(_123,_124,_125){
if(dojo.lang.isString(_123)){
_123=_123.split("");
}
if(Array.forEach){
Array.forEach(_123,_124,_125);
}else{
if(!_125){
_125=dj_global;
}
for(var i=0,l=_123.length;i<l;i++){
_124.call(_125,_123[i],i,_123);
}
}
};
dojo.lang._everyOrSome=function(_127,arr,_129,_12a){
if(dojo.lang.isString(arr)){
arr=arr.split("");
}
if(Array.every){
return Array[(_127)?"every":"some"](arr,_129,_12a);
}else{
if(!_12a){
_12a=dj_global;
}
for(var i=0,l=arr.length;i<l;i++){
var _12c=_129.call(_12a,arr[i],i,arr);
if((_127)&&(!_12c)){
return false;
}else{
if((!_127)&&(_12c)){
return true;
}
}
}
return (_127)?true:false;
}
};
dojo.lang.every=function(arr,_12e,_12f){
return this._everyOrSome(true,arr,_12e,_12f);
};
dojo.lang.some=function(arr,_131,_132){
return this._everyOrSome(false,arr,_131,_132);
};
dojo.lang.filter=function(arr,_134,_135){
var _136=dojo.lang.isString(arr);
if(_136){
arr=arr.split("");
}
if(Array.filter){
var _137=Array.filter(arr,_134,_135);
}else{
if(!_135){
if(arguments.length>=3){
dojo.raise("thisObject doesn't exist!");
}
_135=dj_global;
}
var _137=[];
for(var i=0;i<arr.length;i++){
if(_134.call(_135,arr[i],i,arr)){
_137.push(arr[i]);
}
}
}
if(_136){
return _137.join("");
}else{
return _137;
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
dojo.lang.toArray=function(_13c,_13d){
var _13e=[];
for(var i=_13d||0;i<_13c.length;i++){
_13e.push(_13c[i]);
}
return _13e;
};
dojo.provide("dojo.lang.func");
dojo.require("dojo.lang.common");
dojo.lang.hitch=function(_140,_141){
if(dojo.lang.isString(_141)){
var fcn=_140[_141];
}else{
var fcn=_141;
}
return function(){
return fcn.apply(_140,arguments);
};
};
dojo.lang.anonCtr=0;
dojo.lang.anon={};
dojo.lang.nameAnonFunc=function(_143,_144){
var nso=(_144||dojo.lang.anon);
if((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true)){
for(var x in nso){
if(nso[x]===_143){
return x;
}
}
}
var ret="__"+dojo.lang.anonCtr++;
while(typeof nso[ret]!="undefined"){
ret="__"+dojo.lang.anonCtr++;
}
nso[ret]=_143;
return ret;
};
dojo.lang.forward=function(_148){
return function(){
return this[_148].apply(this,arguments);
};
};
dojo.lang.curry=function(ns,func){
var _14b=[];
ns=ns||dj_global;
if(dojo.lang.isString(func)){
func=ns[func];
}
for(var x=2;x<arguments.length;x++){
_14b.push(arguments[x]);
}
var _14d=(func["__preJoinArity"]||func.length)-_14b.length;
function gather(_14e,_14f,_150){
var _151=_150;
var _152=_14f.slice(0);
for(var x=0;x<_14e.length;x++){
_152.push(_14e[x]);
}
_150=_150-_14e.length;
if(_150<=0){
var res=func.apply(ns,_152);
_150=_151;
return res;
}else{
return function(){
return gather(arguments,_152,_150);
};
}
}
return gather([],_14b,_14d);
};
dojo.lang.curryArguments=function(ns,func,args,_158){
var _159=[];
var x=_158||0;
for(x=_158;x<args.length;x++){
_159.push(args[x]);
}
return dojo.lang.curry.apply(dojo.lang,[ns,func].concat(_159));
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
dojo.lang.delayThese=function(farr,cb,_15f,_160){
if(!farr.length){
if(typeof _160=="function"){
_160();
}
return;
}
if((typeof _15f=="undefined")&&(typeof cb=="number")){
_15f=cb;
cb=function(){
};
}else{
if(!cb){
cb=function(){
};
if(!_15f){
_15f=0;
}
}
}
setTimeout(function(){
(farr.shift())();
cb();
dojo.lang.delayThese(farr,cb,_15f,_160);
},_15f);
};
dojo.provide("dojo.string.extras");
dojo.require("dojo.string.common");
dojo.require("dojo.lang");
dojo.string.substituteParams=function(_161,hash){
var map=(typeof hash=="object")?hash:dojo.lang.toArray(arguments,1);
return _161.replace(/\%\{(\w+)\}/g,function(_164,key){
return map[key]||dojo.raise("Substitution not found: "+key);
});
};
dojo.string.paramString=function(str,_167,_168){
dojo.deprecated("dojo.string.paramString","use dojo.string.substituteParams instead","0.4");
for(var name in _167){
var re=new RegExp("\\%\\{"+name+"\\}","g");
str=str.replace(re,_167[name]);
}
if(_168){
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
var _16c=str.split(" ");
for(var i in _16c){
_16c[i]=_16c[i].charAt(0).toUpperCase()+_16c[i].substring(1);
}
return _16c.join(" ");
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
var _171=escape(str);
var _172,re=/%u([0-9A-F]{4})/i;
while((_172=_171.match(re))){
var num=Number("0x"+_172[1]);
var _174=escape("&#"+num+";");
ret+=_171.substring(0,_172.index)+_174;
_171=_171.substring(_172.index+_172[0].length);
}
ret+=_171.replace(/\+/g,"%2B");
return ret;
};
dojo.string.escape=function(type,str){
var args=dojo.lang.toArray(arguments,1);
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
dojo.string.escapeXml=function(str,_179){
str=str.replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");
if(!_179){
str=str.replace(/'/gm,"&#39;");
}
return str;
};
dojo.string.escapeSql=function(str){
return str.replace(/'/gm,"''");
};
dojo.string.escapeRegExp=function(str){
return str.replace(/\\/gm,"\\\\").replace(/([\f\b\n\t\r[\^$|?*+(){}])/gm,"\\$1");
};
dojo.string.escapeJavaScript=function(str){
return str.replace(/(["'\f\b\n\t\r])/gm,"\\$1");
};
dojo.string.escapeString=function(str){
return ("\""+str.replace(/(["\\])/g,"\\$1")+"\"").replace(/[\f]/g,"\\f").replace(/[\b]/g,"\\b").replace(/[\n]/g,"\\n").replace(/[\t]/g,"\\t").replace(/[\r]/g,"\\r");
};
dojo.string.summary=function(str,len){
if(!len||str.length<=len){
return str;
}else{
return str.substring(0,len).replace(/\.+$/,"")+"...";
}
};
dojo.string.endsWith=function(str,end,_182){
if(_182){
str=str.toLowerCase();
end=end.toLowerCase();
}
if((str.length-end.length)<0){
return false;
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
dojo.string.startsWith=function(str,_186,_187){
if(_187){
str=str.toLowerCase();
_186=_186.toLowerCase();
}
return str.indexOf(_186)==0;
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
dojo.string.normalizeNewlines=function(text,_18d){
if(_18d=="\n"){
text=text.replace(/\r\n/g,"\n");
text=text.replace(/\r/g,"\n");
}else{
if(_18d=="\r"){
text=text.replace(/\r\n/g,"\r");
text=text.replace(/\n/g,"\r");
}else{
text=text.replace(/([^\r])\n/g,"$1\r\n");
text=text.replace(/\r([^\n])/g,"\r\n$1");
}
}
return text;
};
dojo.string.splitEscaped=function(str,_18f){
var _190=[];
for(var i=0,prevcomma=0;i<str.length;i++){
if(str.charAt(i)=="\\"){
i++;
continue;
}
if(str.charAt(i)==_18f){
_190.push(str.substring(prevcomma,i));
prevcomma=i+1;
}
}
_190.push(str.substr(prevcomma));
return _190;
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
dojo.deprecated("dojo.dom.getTagName","use node.tagName instead","0.4");
var _194=node.tagName;
if(_194.substr(0,5).toLowerCase()!="dojo:"){
if(_194.substr(0,4).toLowerCase()=="dojo"){
return "dojo:"+_194.substring(4).toLowerCase();
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
var _196=node.className||node.getAttribute("class");
if((_196)&&(_196.indexOf)&&(_196.indexOf("dojo-")!=-1)){
var _197=_196.split(" ");
for(var x=0;x<_197.length;x++){
if((_197[x].length>5)&&(_197[x].indexOf("dojo-")>=0)){
return "dojo:"+_197[x].substr(5).toLowerCase();
}
}
}
}
}
return _194.toLowerCase();
};
dojo.dom.getUniqueId=function(){
do{
var id="dj_unique_"+(++arguments.callee._idIncrement);
}while(document.getElementById(id));
return id;
};
dojo.dom.getUniqueId._idIncrement=0;
dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_19a,_19b){
var node=_19a.firstChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.nextSibling;
}
if(_19b&&node&&node.tagName&&node.tagName.toLowerCase()!=_19b.toLowerCase()){
node=dojo.dom.nextElement(node,_19b);
}
return node;
};
dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_19d,_19e){
var node=_19d.lastChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.previousSibling;
}
if(_19e&&node&&node.tagName&&node.tagName.toLowerCase()!=_19e.toLowerCase()){
node=dojo.dom.prevElement(node,_19e);
}
return node;
};
dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(node,_1a1){
if(!node){
return null;
}
do{
node=node.nextSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_1a1&&_1a1.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.nextElement(node,_1a1);
}
return node;
};
dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(node,_1a3){
if(!node){
return null;
}
if(_1a3){
_1a3=_1a3.toLowerCase();
}
do{
node=node.previousSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_1a3&&_1a3.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.prevElement(node,_1a3);
}
return node;
};
dojo.dom.moveChildren=function(_1a4,_1a5,trim){
var _1a7=0;
if(trim){
while(_1a4.hasChildNodes()&&_1a4.firstChild.nodeType==dojo.dom.TEXT_NODE){
_1a4.removeChild(_1a4.firstChild);
}
while(_1a4.hasChildNodes()&&_1a4.lastChild.nodeType==dojo.dom.TEXT_NODE){
_1a4.removeChild(_1a4.lastChild);
}
}
while(_1a4.hasChildNodes()){
_1a5.appendChild(_1a4.firstChild);
_1a7++;
}
return _1a7;
};
dojo.dom.copyChildren=function(_1a8,_1a9,trim){
var _1ab=_1a8.cloneNode(true);
return this.moveChildren(_1ab,_1a9,trim);
};
dojo.dom.removeChildren=function(node){
var _1ad=node.childNodes.length;
while(node.hasChildNodes()){
node.removeChild(node.firstChild);
}
return _1ad;
};
dojo.dom.replaceChildren=function(node,_1af){
dojo.dom.removeChildren(node);
node.appendChild(_1af);
};
dojo.dom.removeNode=function(node){
if(node&&node.parentNode){
return node.parentNode.removeChild(node);
}
};
dojo.dom.getAncestors=function(node,_1b2,_1b3){
var _1b4=[];
var _1b5=dojo.lang.isFunction(_1b2);
while(node){
if(!_1b5||_1b2(node)){
_1b4.push(node);
}
if(_1b3&&_1b4.length>0){
return _1b4[0];
}
node=node.parentNode;
}
if(_1b3){
return null;
}
return _1b4;
};
dojo.dom.getAncestorsByTag=function(node,tag,_1b8){
tag=tag.toLowerCase();
return dojo.dom.getAncestors(node,function(el){
return ((el.tagName)&&(el.tagName.toLowerCase()==tag));
},_1b8);
};
dojo.dom.getFirstAncestorByTag=function(node,tag){
return dojo.dom.getAncestorsByTag(node,tag,true);
};
dojo.dom.isDescendantOf=function(node,_1bd,_1be){
if(_1be&&node){
node=node.parentNode;
}
while(node){
if(node==_1bd){
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
if(node.xml){
return node.xml;
}else{
if(typeof XMLSerializer!="undefined"){
return (new XMLSerializer()).serializeToString(node);
}
}
}
};
dojo.dom.createDocument=function(){
var doc=null;
if(!dj_undef("ActiveXObject")){
var _1c1=["MSXML2","Microsoft","MSXML","MSXML3"];
for(var i=0;i<_1c1.length;i++){
try{
doc=new ActiveXObject(_1c1[i]+".XMLDOM");
}
catch(e){
}
if(doc){
break;
}
}
}else{
if((document.implementation)&&(document.implementation.createDocument)){
doc=document.implementation.createDocument("","",null);
}
}
return doc;
};
dojo.dom.createDocumentFromText=function(str,_1c4){
if(!_1c4){
_1c4="text/xml";
}
if(!dj_undef("DOMParser")){
var _1c5=new DOMParser();
return _1c5.parseFromString(str,_1c4);
}else{
if(!dj_undef("ActiveXObject")){
var _1c6=dojo.dom.createDocument();
if(_1c6){
_1c6.async=false;
_1c6.loadXML(str);
return _1c6;
}else{
dojo.debug("toXml didn't work?");
}
}else{
if(document.createElement){
var tmp=document.createElement("xml");
tmp.innerHTML=str;
if(document.implementation&&document.implementation.createDocument){
var _1c8=document.implementation.createDocument("foo","",null);
for(var i=0;i<tmp.childNodes.length;i++){
_1c8.importNode(tmp.childNodes.item(i),true);
}
return _1c8;
}
return ((tmp.document)&&(tmp.document.firstChild?tmp.document.firstChild:tmp));
}
}
}
return null;
};
dojo.dom.prependChild=function(node,_1cb){
if(_1cb.firstChild){
_1cb.insertBefore(node,_1cb.firstChild);
}else{
_1cb.appendChild(node);
}
return true;
};
dojo.dom.insertBefore=function(node,ref,_1ce){
if(_1ce!=true&&(node===ref||node.nextSibling===ref)){
return false;
}
var _1cf=ref.parentNode;
_1cf.insertBefore(node,ref);
return true;
};
dojo.dom.insertAfter=function(node,ref,_1d2){
var pn=ref.parentNode;
if(ref==pn.lastChild){
if((_1d2!=true)&&(node===ref)){
return false;
}
pn.appendChild(node);
}else{
return this.insertBefore(node,ref.nextSibling,_1d2);
}
return true;
};
dojo.dom.insertAtPosition=function(node,ref,_1d6){
if((!node)||(!ref)||(!_1d6)){
return false;
}
switch(_1d6.toLowerCase()){
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
dojo.dom.insertAtIndex=function(node,_1d8,_1d9){
var _1da=_1d8.childNodes;
if(!_1da.length){
_1d8.appendChild(node);
return true;
}
var _1db=null;
for(var i=0;i<_1da.length;i++){
var _1dd=_1da.item(i)["getAttribute"]?parseInt(_1da.item(i).getAttribute("dojoinsertionindex")):-1;
if(_1dd<_1d9){
_1db=_1da.item(i);
}
}
if(_1db){
return dojo.dom.insertAfter(node,_1db);
}else{
return dojo.dom.insertBefore(node,_1da.item(0));
}
};
dojo.dom.textContent=function(node,text){
if(text){
dojo.dom.replaceChildren(node,document.createTextNode(text));
return text;
}else{
var _1e0="";
if(node==null){
return _1e0;
}
for(var i=0;i<node.childNodes.length;i++){
switch(node.childNodes[i].nodeType){
case 1:
case 5:
_1e0+=dojo.dom.textContent(node.childNodes[i]);
break;
case 3:
case 2:
case 4:
_1e0+=node.childNodes[i].nodeValue;
break;
default:
break;
}
}
return _1e0;
}
};
dojo.dom.collectionToArray=function(_1e2){
dojo.deprecated("dojo.dom.collectionToArray","use dojo.lang.toArray instead","0.4");
return dojo.lang.toArray(_1e2);
};
dojo.dom.hasParent=function(node){
return node&&node.parentNode&&dojo.dom.isNode(node.parentNode);
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
dojo.undo.browser={initialHref:window.location.href,initialHash:window.location.hash,moveForward:false,historyStack:[],forwardStack:[],historyIframe:null,bookmarkAnchor:null,locationTimer:null,setInitialState:function(args){
this.initialState={"url":this.initialHref,"kwArgs":args,"urlHash":this.initialHash};
},addToHistory:function(args){
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
this.changingUrl=true;
hash="#"+((args["changeUrl"]!==true)?args["changeUrl"]:(new Date()).getTime());
setTimeout("window.location.href = '"+hash+"'; dojo.undo.browser.changingUrl = false;",1);
this.bookmarkAnchor.href=hash;
if(dojo.render.html.ie){
var _1ea=args["back"]||args["backButton"]||args["handle"];
var tcb=function(_1ec){
if(window.location.hash!=""){
setTimeout("window.location.href = '"+hash+"';",1);
}
_1ea.apply(this,[_1ec]);
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
var _1ed=args["forward"]||args["forwardButton"]||args["handle"];
var tfw=function(_1ef){
if(window.location.hash!=""){
window.location.href=hash;
}
if(_1ed){
_1ed.apply(this,[_1ef]);
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
if(!this.changingUrl){
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
}
},iframeLoaded:function(evt,_1f2){
var _1f3=this._getUrlQuery(_1f2.href);
if(_1f3==null){
if(this.historyStack.length==1){
this.handleBackButton();
}
return;
}
if(this.moveForward){
this.moveForward=false;
return;
}
if(this.historyStack.length>=2&&_1f3==this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){
this.handleBackButton();
}else{
if(this.forwardStack.length>0&&_1f3==this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){
this.handleForwardButton();
}
}
},handleBackButton:function(){
var _1f4=this.historyStack.pop();
if(!_1f4){
return;
}
var last=this.historyStack[this.historyStack.length-1];
if(!last&&this.historyStack.length==0){
last=this.initialState;
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
this.forwardStack.push(_1f4);
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
var _1f8=url.split("?");
if(_1f8.length<2){
return null;
}else{
return _1f8[1];
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
var _1fa=false;
var _1fb=node.getElementsByTagName("input");
dojo.lang.forEach(_1fb,function(_1fc){
if(_1fa){
return;
}
if(_1fc.getAttribute("type")=="file"){
_1fa=true;
}
});
return _1fa;
};
dojo.io.formHasFile=function(_1fd){
return dojo.io.checkChildrenForFile(_1fd);
};
dojo.io.updateNode=function(node,_1ff){
node=dojo.byId(node);
var args=_1ff;
if(dojo.lang.isString(_1ff)){
args={url:_1ff};
}
args.mimetype="text/html";
args.load=function(t,d,e){
while(node.firstChild){
if(dojo["event"]){
try{
dojo.event.browser.clean(node.firstChild);
}
catch(e){
}
}
node.removeChild(node.firstChild);
}
node.innerHTML=d;
};
dojo.io.bind(args);
};
dojo.io.formFilter=function(node){
var type=(node.type||"").toLowerCase();
return !node.disabled&&node.name&&!dojo.lang.inArray(type,["file","submit","image","reset","button"]);
};
dojo.io.encodeForm=function(_206,_207,_208){
if((!_206)||(!_206.tagName)||(!_206.tagName.toLowerCase()=="form")){
dojo.raise("Attempted to encode a non-form element.");
}
if(!_208){
_208=dojo.io.formFilter;
}
var enc=/utf/i.test(_207||"")?encodeURIComponent:dojo.string.encodeAscii;
var _20a=[];
for(var i=0;i<_206.elements.length;i++){
var elm=_206.elements[i];
if(!elm||elm.tagName.toLowerCase()=="fieldset"||!_208(elm)){
continue;
}
var name=enc(elm.name);
var type=elm.type.toLowerCase();
if(type=="select-multiple"){
for(var j=0;j<elm.options.length;j++){
if(elm.options[j].selected){
_20a.push(name+"="+enc(elm.options[j].value));
}
}
}else{
if(dojo.lang.inArray(type,["radio","checkbox"])){
if(elm.checked){
_20a.push(name+"="+enc(elm.value));
}
}else{
_20a.push(name+"="+enc(elm.value));
}
}
}
var _210=_206.getElementsByTagName("input");
for(var i=0;i<_210.length;i++){
var _211=_210[i];
if(_211.type.toLowerCase()=="image"&&_211.form==_206&&_208(_211)){
var name=enc(_211.name);
_20a.push(name+"="+enc(_211.value));
_20a.push(name+".x=0");
_20a.push(name+".y=0");
}
}
return _20a.join("&")+"&";
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
var _217=form.getElementsByTagName("input");
for(var i=0;i<_217.length;i++){
var _218=_217[i];
if(_218.type.toLowerCase()=="image"&&_218.form==form){
this.connect(_218,"onclick","click");
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
var _21f=false;
if(node.disabled||!node.name){
_21f=false;
}else{
if(dojo.lang.inArray(type,["submit","button","image"])){
if(!this.clickedButton){
this.clickedButton=node;
}
_21f=node==this.clickedButton;
}else{
_21f=!dojo.lang.inArray(type,["file","submit","reset","button"]);
}
}
return _21f;
},connect:function(_220,_221,_222){
if(dojo.evalObjPath("dojo.event.connect")){
dojo.event.connect(_220,_221,this,_222);
}else{
var fcn=dojo.lang.hitch(this,_222);
_220[_221]=function(e){
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
var _225=this;
var _226={};
this.useCache=false;
this.preventCache=false;
function getCacheKey(url,_228,_229){
return url+"|"+_228+"|"+_229.toLowerCase();
}
function addToCache(url,_22b,_22c,http){
_226[getCacheKey(url,_22b,_22c)]=http;
}
function getFromCache(url,_22f,_230){
return _226[getCacheKey(url,_22f,_230)];
}
this.clearCache=function(){
_226={};
};
function doLoad(_231,http,url,_234,_235){
if(((http.status>=200)&&(http.status<300))||(http.status==304)||(location.protocol=="file:"&&(http.status==0||http.status==undefined))||(location.protocol=="chrome:"&&(http.status==0||http.status==undefined))){
var ret;
if(_231.method.toLowerCase()=="head"){
var _237=http.getAllResponseHeaders();
ret={};
ret.toString=function(){
return _237;
};
var _238=_237.split(/[\r\n]+/g);
for(var i=0;i<_238.length;i++){
var pair=_238[i].match(/^([^:]+)\s*:\s*(.+)$/i);
if(pair){
ret[pair[1]]=pair[2];
}
}
}else{
if(_231.mimetype=="text/javascript"){
try{
ret=dj_eval(http.responseText);
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=null;
}
}else{
if(_231.mimetype=="text/json"){
try{
ret=dj_eval("("+http.responseText+")");
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=false;
}
}else{
if((_231.mimetype=="application/xml")||(_231.mimetype=="text/xml")){
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
if(_235){
addToCache(url,_234,_231.method,http);
}
_231[(typeof _231.load=="function")?"load":"handle"]("load",ret,http,_231);
}else{
var _23b=new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);
_231[(typeof _231.error=="function")?"error":"handle"]("error",_23b,http,_231);
}
}
function setHeaders(http,_23d){
if(_23d["headers"]){
for(var _23e in _23d["headers"]){
if(_23e.toLowerCase()=="content-type"&&!_23d["contentType"]){
_23d["contentType"]=_23d["headers"][_23e];
}else{
http.setRequestHeader(_23e,_23d["headers"][_23e]);
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
var now=null;
for(var x=this.inFlight.length-1;x>=0;x--){
var tif=this.inFlight[x];
if(!tif){
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
if(this.inFlight.length==0){
clearInterval(this.inFlightTimer);
this.inFlightTimer=null;
}
};
var _242=dojo.hostenv.getXmlhttpObject()?true:false;
this.canHandle=function(_243){
return _242&&dojo.lang.inArray((_243["mimetype"].toLowerCase()||""),["text/plain","text/html","application/xml","text/xml","text/javascript","text/json"])&&!(_243["formNode"]&&dojo.io.formHasFile(_243["formNode"]));
};
this.multipartBoundary="45309FFF-BD65-4d50-99C9-36986896A96F";
this.bind=function(_244){
if(!_244["url"]){
if(!_244["formNode"]&&(_244["backButton"]||_244["back"]||_244["changeUrl"]||_244["watchForURL"])&&(!djConfig.preventBackButtonFix)){
dojo.deprecated("Using dojo.io.XMLHTTPTransport.bind() to add to browser history without doing an IO request","Use dojo.undo.browser.addToHistory() instead.","0.4");
dojo.undo.browser.addToHistory(_244);
return true;
}
}
var url=_244.url;
var _246="";
if(_244["formNode"]){
var ta=_244.formNode.getAttribute("action");
if((ta)&&(!_244["url"])){
url=ta;
}
var tp=_244.formNode.getAttribute("method");
if((tp)&&(!_244["method"])){
_244.method=tp;
}
_246+=dojo.io.encodeForm(_244.formNode,_244.encoding,_244["formFilter"]);
}
if(url.indexOf("#")>-1){
dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);
url=url.split("#")[0];
}
if(_244["file"]){
_244.method="post";
}
if(!_244["method"]){
_244.method="get";
}
if(_244.method.toLowerCase()=="get"){
_244.multipart=false;
}else{
if(_244["file"]){
_244.multipart=true;
}else{
if(!_244["multipart"]){
_244.multipart=false;
}
}
}
if(_244["backButton"]||_244["back"]||_244["changeUrl"]){
dojo.undo.browser.addToHistory(_244);
}
var _249=_244["content"]||{};
if(_244.sendTransport){
_249["dojo.transport"]="xmlhttp";
}
do{
if(_244.postContent){
_246=_244.postContent;
break;
}
if(_249){
_246+=dojo.io.argsFromMap(_249,_244.encoding);
}
if(_244.method.toLowerCase()=="get"||!_244.multipart){
break;
}
var t=[];
if(_246.length){
var q=_246.split("&");
for(var i=0;i<q.length;++i){
if(q[i].length){
var p=q[i].split("=");
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+p[0]+"\"","",p[1]);
}
}
}
if(_244.file){
if(dojo.lang.isArray(_244.file)){
for(var i=0;i<_244.file.length;++i){
var o=_244.file[i];
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}else{
var o=_244.file;
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}
if(t.length){
t.push("--"+this.multipartBoundary+"--","");
_246=t.join("\r\n");
}
}while(false);
var _24f=_244["sync"]?false:true;
var _250=_244["preventCache"]||(this.preventCache==true&&_244["preventCache"]!=false);
var _251=_244["useCache"]==true||(this.useCache==true&&_244["useCache"]!=false);
if(!_250&&_251){
var _252=getFromCache(url,_246,_244.method);
if(_252){
doLoad(_244,_252,url,_246,false);
return;
}
}
var http=dojo.hostenv.getXmlhttpObject(_244);
var _254=false;
if(_24f){
var _255=this.inFlight.push({"req":_244,"http":http,"url":url,"query":_246,"useCache":_251,"startTime":_244.timeoutSeconds?(new Date()).getTime():0});
this.startWatchingInFlight();
}
if(_244.method.toLowerCase()=="post"){
http.open("POST",url,_24f);
setHeaders(http,_244);
http.setRequestHeader("Content-Type",_244.multipart?("multipart/form-data; boundary="+this.multipartBoundary):(_244.contentType||"application/x-www-form-urlencoded"));
try{
http.send(_246);
}
catch(e){
if(typeof http.abort=="function"){
http.abort();
}
doLoad(_244,{status:404},url,_246,_251);
}
}else{
var _256=url;
if(_246!=""){
_256+=(_256.indexOf("?")>-1?"&":"?")+_246;
}
if(_250){
_256+=(dojo.string.endsWithAny(_256,"?","&")?"":(_256.indexOf("?")>-1?"&":"?"))+"dojo.preventCache="+new Date().valueOf();
}
http.open(_244.method.toUpperCase(),_256,_24f);
setHeaders(http,_244);
try{
http.send(null);
}
catch(e){
if(typeof http.abort=="function"){
http.abort();
}
doLoad(_244,{status:404},url,_246,_251);
}
}
if(!_24f){
doLoad(_244,http,url,_246,_251);
}
_244.abort=function(){
return http.abort();
};
return;
};
dojo.io.transports.addTransport("XMLHTTPTransport");
};
dojo.provide("dojo.io.cookie");
dojo.io.cookie.setCookie=function(name,_258,days,path,_25b,_25c){
var _25d=-1;
if(typeof days=="number"&&days>=0){
var d=new Date();
d.setTime(d.getTime()+(days*24*60*60*1000));
_25d=d.toGMTString();
}
_258=escape(_258);
document.cookie=name+"="+_258+";"+(_25d!=-1?" expires="+_25d+";":"")+(path?"path="+path:"")+(_25b?"; domain="+_25b:"")+(_25c?"; secure":"");
};
dojo.io.cookie.set=dojo.io.cookie.setCookie;
dojo.io.cookie.getCookie=function(name){
var idx=document.cookie.lastIndexOf(name+"=");
if(idx==-1){
return null;
}
var _261=document.cookie.substring(idx+name.length+1);
var end=_261.indexOf(";");
if(end==-1){
end=_261.length;
}
_261=_261.substring(0,end);
_261=unescape(_261);
return _261;
};
dojo.io.cookie.get=dojo.io.cookie.getCookie;
dojo.io.cookie.deleteCookie=function(name){
dojo.io.cookie.setCookie(name,"-",0);
};
dojo.io.cookie.setObjectCookie=function(name,obj,days,path,_268,_269,_26a){
if(arguments.length==5){
_26a=_268;
_268=null;
_269=null;
}
var _26b=[],cookie,value="";
if(!_26a){
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
_26b.push(escape(prop)+"="+escape(cookie[prop]));
}
value=_26b.join("&");
}
dojo.io.cookie.setCookie(name,value,days,path,_268,_269);
};
dojo.io.cookie.getObjectCookie=function(name){
var _26e=null,cookie=dojo.io.cookie.getCookie(name);
if(cookie){
_26e={};
var _26f=cookie.split("&");
for(var i=0;i<_26f.length;i++){
var pair=_26f[i].split("=");
var _272=pair[1];
if(isNaN(_272)){
_272=unescape(pair[1]);
}
_26e[unescape(pair[0])]=_272;
}
}
return _26e;
};
dojo.io.cookie.isSupported=function(){
if(typeof navigator.cookieEnabled!="boolean"){
dojo.io.cookie.setCookie("__TestingYourBrowserForCookieSupport__","CookiesAllowed",90,null);
var _273=dojo.io.cookie.getCookie("__TestingYourBrowserForCookieSupport__");
navigator.cookieEnabled=(_273=="CookiesAllowed");
if(navigator.cookieEnabled){
this.deleteCookie("__TestingYourBrowserForCookieSupport__");
}
}
return navigator.cookieEnabled;
};
if(!dojo.io.cookies){
dojo.io.cookies=dojo.io.cookie;
}
dojo.kwCompoundRequire({common:["dojo.io"],rhino:["dojo.io.RhinoIO"],browser:["dojo.io.BrowserIO","dojo.io.cookie"],dashboard:["dojo.io.BrowserIO","dojo.io.cookie"]});
dojo.provide("dojo.io.*");
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
var _277=dojo.lang.nameAnonFunc(args[2],ao.adviceObj);
ao.adviceFunc=_277;
}else{
if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){
ao.adviceType="after";
ao.srcObj=dj_global;
var _277=dojo.lang.nameAnonFunc(args[0],ao.srcObj);
ao.srcFunc=_277;
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
var _277=dojo.lang.nameAnonFunc(args[1],dj_global);
ao.srcFunc=_277;
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){
ao.srcObj=args[1];
ao.srcFunc=args[2];
var _277=dojo.lang.nameAnonFunc(args[3],dj_global);
ao.adviceObj=dj_global;
ao.adviceFunc=_277;
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
if(dl.isFunction(ao.aroundFunc)){
var _277=dojo.lang.nameAnonFunc(ao.aroundFunc,ao.aroundObj);
ao.aroundFunc=_277;
}
if(!dl.isString(ao.srcFunc)){
ao.srcFunc=dojo.lang.getNameInObj(ao.srcObj,ao.srcFunc);
}
if(!dl.isString(ao.adviceFunc)){
ao.adviceFunc=dojo.lang.getNameInObj(ao.adviceObj,ao.adviceFunc);
}
if((ao.aroundObj)&&(!dl.isString(ao.aroundFunc))){
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
if(arguments.length==1){
var ao=arguments[0];
}else{
var ao=interpolateArgs(arguments);
}
if(dojo.lang.isArray(ao.srcObj)&&ao.srcObj!=""){
var _279={};
for(var x in ao){
_279[x]=ao[x];
}
var mjps=[];
dojo.lang.forEach(ao.srcObj,function(src){
if((dojo.render.html.capable)&&(dojo.lang.isString(src))){
src=dojo.byId(src);
}
_279.srcObj=src;
mjps.push(dojo.event.connect.call(dojo.event,_279));
});
return mjps;
}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);
if(ao.adviceFunc){
var mjp2=dojo.event.MethodJoinPoint.getForMethod(ao.adviceObj,ao.adviceFunc);
}
mjp.kwAddAdvice(ao);
return mjp;
};
this.log=function(a1,a2){
var _281;
if((arguments.length==1)&&(typeof a1=="object")){
_281=a1;
}else{
_281={srcObj:a1,srcFunc:a2};
}
_281.adviceFunc=function(){
var _282=[];
for(var x=0;x<arguments.length;x++){
_282.push(arguments[x]);
}
dojo.debug("("+_281.srcObj+")."+_281.srcFunc,":",_282.join(", "));
};
this.kwConnect(_281);
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
this.connectOnce=function(){
var ao=interpolateArgs(arguments);
ao.once=true;
return this.connect(ao);
};
this._kwConnectImpl=function(_289,_28a){
var fn=(_28a)?"disconnect":"connect";
if(typeof _289["srcFunc"]=="function"){
_289.srcObj=_289["srcObj"]||dj_global;
var _28c=dojo.lang.nameAnonFunc(_289.srcFunc,_289.srcObj);
_289.srcFunc=_28c;
}
if(typeof _289["adviceFunc"]=="function"){
_289.adviceObj=_289["adviceObj"]||dj_global;
var _28c=dojo.lang.nameAnonFunc(_289.adviceFunc,_289.adviceObj);
_289.adviceFunc=_28c;
}
return dojo.event[fn]((_289["type"]||_289["adviceType"]||"after"),_289["srcObj"]||dj_global,_289["srcFunc"],_289["adviceObj"]||_289["targetObj"]||dj_global,_289["adviceFunc"]||_289["targetFunc"],_289["aroundObj"],_289["aroundFunc"],_289["once"],_289["delay"],_289["rate"],_289["adviceMsg"]||false);
};
this.kwConnect=function(_28d){
return this._kwConnectImpl(_28d,false);
};
this.disconnect=function(){
var ao=interpolateArgs(arguments);
if(!ao.adviceFunc){
return;
}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);
return mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);
};
this.kwDisconnect=function(_290){
return this._kwConnectImpl(_290,true);
};
};
dojo.event.MethodInvocation=function(_291,obj,args){
this.jp_=_291;
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
dojo.event.MethodJoinPoint=function(obj,_299){
this.object=obj||dj_global;
this.methodname=_299;
this.methodfunc=this.object[_299];
this.before=[];
this.after=[];
this.around=[];
};
dojo.event.MethodJoinPoint.getForMethod=function(obj,_29b){
if(!obj){
obj=dj_global;
}
if(!obj[_29b]){
obj[_29b]=function(){
};
}else{
if((!dojo.lang.isFunction(obj[_29b]))&&(!dojo.lang.isAlien(obj[_29b]))){
return null;
}
}
var _29c=_29b+"$joinpoint";
var _29d=_29b+"$joinpoint$method";
var _29e=obj[_29c];
if(!_29e){
var _29f=false;
if(dojo.event["browser"]){
if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){
_29f=true;
dojo.event.browser.addClobberNodeAttrs(obj,[_29c,_29d,_29b]);
}
}
var _2a0=obj[_29b].length;
obj[_29d]=obj[_29b];
_29e=obj[_29c]=new dojo.event.MethodJoinPoint(obj,_29d);
obj[_29b]=function(){
var args=[];
if((_29f)&&(!arguments.length)){
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
catch(e){
evt=window.event;
}
if(evt){
args.push(dojo.event.browser.fixEvent(evt,this));
}
}else{
for(var x=0;x<arguments.length;x++){
if((x==0)&&(_29f)&&(dojo.event.browser.isEvent(arguments[x]))){
args.push(dojo.event.browser.fixEvent(arguments[x],this));
}else{
args.push(arguments[x]);
}
}
}
return _29e.run.apply(_29e,args);
};
obj[_29b].__preJoinArity=_2a0;
}
return _29e;
};
dojo.lang.extend(dojo.event.MethodJoinPoint,{unintercept:function(){
this.object[this.methodname]=this.methodfunc;
this.before=[];
this.after=[];
this.around=[];
},disconnect:dojo.lang.forward("unintercept"),run:function(){
var obj=this.object||dj_global;
var args=arguments;
var _2a6=[];
for(var x=0;x<args.length;x++){
_2a6[x]=args[x];
}
var _2a8=function(marr){
if(!marr){
dojo.debug("Null argument to unrollAdvice()");
return;
}
var _2aa=marr[0]||dj_global;
var _2ab=marr[1];
if(!_2aa[_2ab]){
dojo.raise("function \""+_2ab+"\" does not exist on \""+_2aa+"\"");
}
var _2ac=marr[2]||dj_global;
var _2ad=marr[3];
var msg=marr[6];
var _2af;
var to={args:[],jp_:this,object:obj,proceed:function(){
return _2aa[_2ab].apply(_2aa,to.args);
}};
to.args=_2a6;
var _2b1=parseInt(marr[4]);
var _2b2=((!isNaN(_2b1))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));
if(marr[5]){
var rate=parseInt(marr[5]);
var cur=new Date();
var _2b5=false;
if((marr["last"])&&((cur-marr.last)<=rate)){
if(dojo.event.canTimeout){
if(marr["delayTimer"]){
clearTimeout(marr.delayTimer);
}
var tod=parseInt(rate*2);
var mcpy=dojo.lang.shallowCopy(marr);
marr.delayTimer=setTimeout(function(){
mcpy[5]=0;
_2a8(mcpy);
},tod);
}
return;
}else{
marr.last=cur;
}
}
if(_2ad){
_2ac[_2ad].call(_2ac,to);
}else{
if((_2b2)&&((dojo.render.html)||(dojo.render.svg))){
dj_global["setTimeout"](function(){
if(msg){
_2aa[_2ab].call(_2aa,to);
}else{
_2aa[_2ab].apply(_2aa,args);
}
},_2b1);
}else{
if(msg){
_2aa[_2ab].call(_2aa,to);
}else{
_2aa[_2ab].apply(_2aa,args);
}
}
}
};
if(this.before.length>0){
dojo.lang.forEach(this.before,_2a8);
}
var _2b8;
if(this.around.length>0){
var mi=new dojo.event.MethodInvocation(this,obj,args);
_2b8=mi.proceed();
}else{
if(this.methodfunc){
_2b8=this.object[this.methodname].apply(this.object,args);
}
}
if(this.after.length>0){
dojo.lang.forEach(this.after,_2a8);
}
return (this.methodfunc)?_2b8:null;
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
},addAdvice:function(_2bd,_2be,_2bf,_2c0,_2c1,_2c2,once,_2c4,rate,_2c6){
var arr=this.getArr(_2c1);
if(!arr){
dojo.raise("bad this: "+this);
}
var ao=[_2bd,_2be,_2bf,_2c0,_2c4,rate,_2c6];
if(once){
if(this.hasAdvice(_2bd,_2be,_2c1,arr)>=0){
return;
}
}
if(_2c2=="first"){
arr.unshift(ao);
}else{
arr.push(ao);
}
},hasAdvice:function(_2c9,_2ca,_2cb,arr){
if(!arr){
arr=this.getArr(_2cb);
}
var ind=-1;
for(var x=0;x<arr.length;x++){
if((arr[x][0]==_2c9)&&(arr[x][1]==_2ca)){
ind=x;
}
}
return ind;
},removeAdvice:function(_2cf,_2d0,_2d1,once){
var arr=this.getArr(_2d1);
var ind=this.hasAdvice(_2cf,_2d0,_2d1,arr);
if(ind==-1){
return false;
}
while(ind!=-1){
arr.splice(ind,1);
if(once){
break;
}
ind=this.hasAdvice(_2cf,_2d0,_2d1,arr);
}
return true;
}});
dojo.require("dojo.event");
dojo.provide("dojo.event.topic");
dojo.event.topic=new function(){
this.topics={};
this.getTopic=function(_2d5){
if(!this.topics[_2d5]){
this.topics[_2d5]=new this.TopicImpl(_2d5);
}
return this.topics[_2d5];
};
this.registerPublisher=function(_2d6,obj,_2d8){
var _2d6=this.getTopic(_2d6);
_2d6.registerPublisher(obj,_2d8);
};
this.subscribe=function(_2d9,obj,_2db){
var _2d9=this.getTopic(_2d9);
_2d9.subscribe(obj,_2db);
};
this.unsubscribe=function(_2dc,obj,_2de){
var _2dc=this.getTopic(_2dc);
_2dc.unsubscribe(obj,_2de);
};
this.destroy=function(_2df){
this.getTopic(_2df).destroy();
delete this.topics[_2df];
};
this.publish=function(_2e0,_2e1){
var _2e0=this.getTopic(_2e0);
var args=[];
if(arguments.length==2&&(dojo.lang.isArray(_2e1)||_2e1.callee)){
args=_2e1;
}else{
var args=[];
for(var x=1;x<arguments.length;x++){
args.push(arguments[x]);
}
}
_2e0.sendMessage.apply(_2e0,args);
};
};
dojo.event.topic.TopicImpl=function(_2e4){
this.topicName=_2e4;
this.subscribe=function(_2e5,_2e6){
var tf=_2e6||_2e5;
var to=(!_2e6)?dj_global:_2e5;
dojo.event.kwConnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
this.unsubscribe=function(_2e9,_2ea){
var tf=(!_2ea)?_2e9:_2ea;
var to=(!_2ea)?null:_2e9;
dojo.event.kwDisconnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
this.destroy=function(){
dojo.event.MethodJoinPoint.getForMethod(this,"sendMessage").disconnect();
};
this.registerPublisher=function(_2ed,_2ee){
dojo.event.connect(_2ed,_2ee,this,"sendMessage");
};
this.sendMessage=function(_2ef){
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
this.clobber=function(_2f2){
var na;
var tna;
if(_2f2){
tna=_2f2.all||_2f2.getElementsByTagName("*");
na=[_2f2];
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
var _2f6={};
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
var _2fa=0;
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
this.addClobberNodeAttrs=function(node,_2fe){
this.addClobberNode(node);
for(var x=0;x<_2fe.length;x++){
node.__clobberAttrs__.push(_2fe[x]);
}
};
this.removeListener=function(node,_301,fp,_303){
if(!_303){
var _303=false;
}
_301=_301.toLowerCase();
if(_301.substr(0,2)=="on"){
_301=_301.substr(2);
}
if(node.removeEventListener){
node.removeEventListener(_301,fp,_303);
}
};
this.addListener=function(node,_305,fp,_307,_308){
if(!node){
return;
}
if(!_307){
var _307=false;
}
_305=_305.toLowerCase();
if(_305.substr(0,2)!="on"){
_305="on"+_305;
}
if(!_308){
var _309=function(evt){
if(!evt){
evt=window.event;
}
var ret=fp(dojo.event.browser.fixEvent(evt,this));
if(_307){
dojo.event.browser.stopEvent(evt);
}
return ret;
};
}else{
_309=fp;
}
if(node.addEventListener){
node.addEventListener(_305.substr(2),_309,_307);
return _309;
}else{
if(typeof node[_305]=="function"){
var _30c=node[_305];
node[_305]=function(e){
_30c(e);
return _309(e);
};
}else{
node[_305]=_309;
}
if(dojo.render.html.ie){
this.addClobberNodeAttrs(node,[_305]);
}
return _309;
}
};
this.isEvent=function(obj){
return (typeof obj!="undefined")&&(typeof Event!="undefined")&&(obj.eventPhase);
};
this.currentEvent=null;
this.callListener=function(_30f,_310){
if(typeof _30f!="function"){
dojo.raise("listener not a function: "+_30f);
}
dojo.event.browser.currentEvent.currentTarget=_310;
return _30f.call(_310,dojo.event.browser.currentEvent);
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
this.fixEvent=function(evt,_313){
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
evt.currentTarget=(_313?_313:evt.srcElement);
}
if(!evt.layerX){
evt.layerX=evt.offsetX;
}
if(!evt.layerY){
evt.layerY=evt.offsetY;
}
if(!evt.pageX){
evt.pageX=evt.clientX+(window.pageXOffset||document.documentElement.scrollLeft||document.body.scrollLeft||0);
}
if(!evt.pageY){
evt.pageY=evt.clientY+(window.pageYOffset||document.documentElement.scrollTop||document.body.scrollTop||0);
}
if(evt.type=="mouseover"){
evt.relatedTarget=evt.fromElement;
}
if(evt.type=="mouseout"){
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
dojo.kwCompoundRequire({common:["dojo.event","dojo.event.topic"],browser:["dojo.event.browser"],dashboard:["dojo.event.browser"]});
dojo.provide("dojo.event.*");
dojo.provide("dojo.graphics.color");
dojo.require("dojo.lang.array");
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
dojo.lang.extend(dojo.graphics.color.Color,{toRgb:function(_31c){
if(_31c){
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
},blend:function(_31d,_31e){
return dojo.graphics.color.blend(this.toRgb(),new Color(_31d).toRgb(),_31e);
}});
dojo.graphics.color.named={white:[255,255,255],black:[0,0,0],red:[255,0,0],green:[0,255,0],blue:[0,0,255],navy:[0,0,128],gray:[128,128,128],silver:[192,192,192]};
dojo.graphics.color.blend=function(a,b,_321){
if(typeof a=="string"){
return dojo.graphics.color.blendHex(a,b,_321);
}
if(!_321){
_321=0;
}else{
if(_321>1){
_321=1;
}else{
if(_321<-1){
_321=-1;
}
}
}
var c=new Array(3);
for(var i=0;i<3;i++){
var half=Math.abs(a[i]-b[i])/2;
c[i]=Math.floor(Math.min(a[i],b[i])+half+(half*_321));
}
return c;
};
dojo.graphics.color.blendHex=function(a,b,_327){
return dojo.graphics.color.rgb2hex(dojo.graphics.color.blend(dojo.graphics.color.hex2rgb(a),dojo.graphics.color.hex2rgb(b),_327));
};
dojo.graphics.color.extractRGB=function(_328){
var hex="0123456789abcdef";
_328=_328.toLowerCase();
if(_328.indexOf("rgb")==0){
var _32a=_328.match(/rgba*\((\d+), *(\d+), *(\d+)/i);
var ret=_32a.splice(1,3);
return ret;
}else{
var _32c=dojo.graphics.color.hex2rgb(_328);
if(_32c){
return _32c;
}else{
return dojo.graphics.color.named[_328]||[255,255,255];
}
}
};
dojo.graphics.color.hex2rgb=function(hex){
var _32e="0123456789ABCDEF";
var rgb=new Array(3);
if(hex.indexOf("#")==0){
hex=hex.substring(1);
}
hex=hex.toUpperCase();
if(hex.replace(new RegExp("["+_32e+"]","g"),"")!=""){
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
rgb[i]=_32e.indexOf(rgb[i].charAt(0))*16+_32e.indexOf(rgb[i].charAt(1));
}
return rgb;
};
dojo.graphics.color.rgb2hex=function(r,g,b){
if(dojo.lang.isArray(r)){
g=r[1]||0;
b=r[2]||0;
r=r[0]||0;
}
var ret=dojo.lang.map([r,g,b],function(x){
x=new Number(x);
var s=x.toString(16);
while(s.length<2){
s="0"+s;
}
return s;
});
ret.unshift("#");
return ret.join("");
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
var _33c=new dojo.uri.Uri(arguments[i].toString());
var _33d=new dojo.uri.Uri(uri.toString());
if(_33c.path==""&&_33c.scheme==null&&_33c.authority==null&&_33c.query==null){
if(_33c.fragment!=null){
_33d.fragment=_33c.fragment;
}
_33c=_33d;
}
if(_33c.scheme!=null&&_33c.authority!=null){
uri="";
}
if(_33c.scheme!=null){
uri+=_33c.scheme+":";
}
if(_33c.authority!=null){
uri+="//"+_33c.authority;
}
uri+=_33c.path;
if(_33c.query!=null){
uri+="?"+_33c.query;
}
if(_33c.fragment!=null){
uri+="#"+_33c.fragment;
}
}
this.uri=uri.toString();
var _33e="^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$";
var r=this.uri.match(new RegExp(_33e));
this.scheme=r[2]||(r[1]?"":null);
this.authority=r[4]||(r[3]?"":null);
this.path=r[5];
this.query=r[7]||(r[6]?"":null);
this.fragment=r[9]||(r[8]?"":null);
if(this.authority!=null){
_33e="^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$";
r=this.authority.match(new RegExp(_33e));
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
dojo.provide("dojo.style");
dojo.require("dojo.graphics.color");
dojo.require("dojo.uri.Uri");
dojo.require("dojo.lang.common");
(function(){
var h=dojo.render.html;
var ds=dojo.style;
var db=document["body"]||document["documentElement"];
ds.boxSizing={MARGIN_BOX:"margin-box",BORDER_BOX:"border-box",PADDING_BOX:"padding-box",CONTENT_BOX:"content-box"};
var bs=ds.boxSizing;
ds.getBoxSizing=function(node){
if((h.ie)||(h.opera)){
var cm=document["compatMode"];
if((cm=="BackCompat")||(cm=="QuirksMode")){
return bs.BORDER_BOX;
}else{
return bs.CONTENT_BOX;
}
}else{
if(arguments.length==0){
node=document.documentElement;
}
var _346=ds.getStyle(node,"-moz-box-sizing");
if(!_346){
_346=ds.getStyle(node,"box-sizing");
}
return (_346?_346:bs.CONTENT_BOX);
}
};
ds.isBorderBox=function(node){
return (ds.getBoxSizing(node)==bs.BORDER_BOX);
};
ds.getUnitValue=function(node,_349,_34a){
var s=ds.getComputedStyle(node,_349);
if((!s)||((s=="auto")&&(_34a))){
return {value:0,units:"px"};
}
if(dojo.lang.isUndefined(s)){
return ds.getUnitValue.bad;
}
var _34c=s.match(/(\-?[\d.]+)([a-z%]*)/i);
if(!_34c){
return ds.getUnitValue.bad;
}
return {value:Number(_34c[1]),units:_34c[2].toLowerCase()};
};
ds.getUnitValue.bad={value:NaN,units:""};
ds.getPixelValue=function(node,_34e,_34f){
var _350=ds.getUnitValue(node,_34e,_34f);
if(isNaN(_350.value)){
return 0;
}
if((_350.value)&&(_350.units!="px")){
return NaN;
}
return _350.value;
};
ds.getNumericStyle=function(){
dojo.deprecated("dojo.(style|html).getNumericStyle","in favor of dojo.(style|html).getPixelValue","0.4");
return ds.getPixelValue.apply(this,arguments);
};
ds.setPositivePixelValue=function(node,_352,_353){
if(isNaN(_353)){
return false;
}
node.style[_352]=Math.max(0,_353)+"px";
return true;
};
ds._sumPixelValues=function(node,_355,_356){
var _357=0;
for(var x=0;x<_355.length;x++){
_357+=ds.getPixelValue(node,_355[x],_356);
}
return _357;
};
ds.isPositionAbsolute=function(node){
return (ds.getComputedStyle(node,"position")=="absolute");
};
ds.getBorderExtent=function(node,side){
return (ds.getStyle(node,"border-"+side+"-style")=="none"?0:ds.getPixelValue(node,"border-"+side+"-width"));
};
ds.getMarginWidth=function(node){
return ds._sumPixelValues(node,["margin-left","margin-right"],ds.isPositionAbsolute(node));
};
ds.getBorderWidth=function(node){
return ds.getBorderExtent(node,"left")+ds.getBorderExtent(node,"right");
};
ds.getPaddingWidth=function(node){
return ds._sumPixelValues(node,["padding-left","padding-right"],true);
};
ds.getPadBorderWidth=function(node){
return ds.getPaddingWidth(node)+ds.getBorderWidth(node);
};
ds.getContentBoxWidth=function(node){
node=dojo.byId(node);
return node.offsetWidth-ds.getPadBorderWidth(node);
};
ds.getBorderBoxWidth=function(node){
node=dojo.byId(node);
return node.offsetWidth;
};
ds.getMarginBoxWidth=function(node){
return ds.getInnerWidth(node)+ds.getMarginWidth(node);
};
ds.setContentBoxWidth=function(node,_364){
node=dojo.byId(node);
if(ds.isBorderBox(node)){
_364+=ds.getPadBorderWidth(node);
}
return ds.setPositivePixelValue(node,"width",_364);
};
ds.setMarginBoxWidth=function(node,_366){
node=dojo.byId(node);
if(!ds.isBorderBox(node)){
_366-=ds.getPadBorderWidth(node);
}
_366-=ds.getMarginWidth(node);
return ds.setPositivePixelValue(node,"width",_366);
};
ds.getContentWidth=ds.getContentBoxWidth;
ds.getInnerWidth=ds.getBorderBoxWidth;
ds.getOuterWidth=ds.getMarginBoxWidth;
ds.setContentWidth=ds.setContentBoxWidth;
ds.setOuterWidth=ds.setMarginBoxWidth;
ds.getMarginHeight=function(node){
return ds._sumPixelValues(node,["margin-top","margin-bottom"],ds.isPositionAbsolute(node));
};
ds.getBorderHeight=function(node){
return ds.getBorderExtent(node,"top")+ds.getBorderExtent(node,"bottom");
};
ds.getPaddingHeight=function(node){
return ds._sumPixelValues(node,["padding-top","padding-bottom"],true);
};
ds.getPadBorderHeight=function(node){
return ds.getPaddingHeight(node)+ds.getBorderHeight(node);
};
ds.getContentBoxHeight=function(node){
node=dojo.byId(node);
return node.offsetHeight-ds.getPadBorderHeight(node);
};
ds.getBorderBoxHeight=function(node){
node=dojo.byId(node);
return node.offsetHeight;
};
ds.getMarginBoxHeight=function(node){
return ds.getInnerHeight(node)+ds.getMarginHeight(node);
};
ds.setContentBoxHeight=function(node,_36f){
node=dojo.byId(node);
if(ds.isBorderBox(node)){
_36f+=ds.getPadBorderHeight(node);
}
return ds.setPositivePixelValue(node,"height",_36f);
};
ds.setMarginBoxHeight=function(node,_371){
node=dojo.byId(node);
if(!ds.isBorderBox(node)){
_371-=ds.getPadBorderHeight(node);
}
_371-=ds.getMarginHeight(node);
return ds.setPositivePixelValue(node,"height",_371);
};
ds.getContentHeight=ds.getContentBoxHeight;
ds.getInnerHeight=ds.getBorderBoxHeight;
ds.getOuterHeight=ds.getMarginBoxHeight;
ds.setContentHeight=ds.setContentBoxHeight;
ds.setOuterHeight=ds.setMarginBoxHeight;
ds.getAbsolutePosition=ds.abs=function(node,_373){
var ret=[];
ret.x=ret.y=0;
var st=dojo.html.getScrollTop();
var sl=dojo.html.getScrollLeft();
if(h.ie){
with(node.getBoundingClientRect()){
ret.x=left-2;
ret.y=top-2;
}
}else{
if(node["offsetParent"]){
var _377;
if((h.safari)&&(node.style.getPropertyValue("position")=="absolute")&&(node.parentNode==db)){
_377=db;
}else{
_377=db.parentNode;
}
if(node.parentNode!=db){
ret.x-=ds.sumAncestorProperties(node,"scrollLeft");
ret.y-=ds.sumAncestorProperties(node,"scrollTop");
}
do{
var n=node["offsetLeft"];
ret.x+=isNaN(n)?0:n;
var m=node["offsetTop"];
ret.y+=isNaN(m)?0:m;
node=node.offsetParent;
}while((node!=_377)&&(node!=null));
}else{
if(node["x"]&&node["y"]){
ret.x+=isNaN(node.x)?0:node.x;
ret.y+=isNaN(node.y)?0:node.y;
}
}
}
if(_373){
ret.y+=st;
ret.x+=sl;
}
ret[0]=ret.x;
ret[1]=ret.y;
return ret;
};
ds.sumAncestorProperties=function(node,prop){
node=dojo.byId(node);
if(!node){
return 0;
}
var _37c=0;
while(node){
var val=node[prop];
if(val){
_37c+=val-0;
}
node=node.parentNode;
}
return _37c;
};
ds.getTotalOffset=function(node,type,_380){
node=dojo.byId(node);
return ds.abs(node,_380)[(type=="top")?"y":"x"];
};
ds.getAbsoluteX=ds.totalOffsetLeft=function(node,_382){
return ds.getTotalOffset(node,"left",_382);
};
ds.getAbsoluteY=ds.totalOffsetTop=function(node,_384){
return ds.getTotalOffset(node,"top",_384);
};
ds.styleSheet=null;
ds.insertCssRule=function(_385,_386,_387){
if(!ds.styleSheet){
if(document.createStyleSheet){
ds.styleSheet=document.createStyleSheet();
}else{
if(document.styleSheets[0]){
ds.styleSheet=document.styleSheets[0];
}else{
return null;
}
}
}
if(arguments.length<3){
if(ds.styleSheet.cssRules){
_387=ds.styleSheet.cssRules.length;
}else{
if(ds.styleSheet.rules){
_387=ds.styleSheet.rules.length;
}else{
return null;
}
}
}
if(ds.styleSheet.insertRule){
var rule=_385+" { "+_386+" }";
return ds.styleSheet.insertRule(rule,_387);
}else{
if(ds.styleSheet.addRule){
return ds.styleSheet.addRule(_385,_386,_387);
}else{
return null;
}
}
};
ds.removeCssRule=function(_389){
if(!ds.styleSheet){
dojo.debug("no stylesheet defined for removing rules");
return false;
}
if(h.ie){
if(!_389){
_389=ds.styleSheet.rules.length;
ds.styleSheet.removeRule(_389);
}
}else{
if(document.styleSheets[0]){
if(!_389){
_389=ds.styleSheet.cssRules.length;
}
ds.styleSheet.deleteRule(_389);
}
}
return true;
};
ds.insertCssFile=function(URI,doc,_38c){
if(!URI){
return;
}
if(!doc){
doc=document;
}
var _38d=dojo.hostenv.getText(URI);
_38d=ds.fixPathsInCssText(_38d,URI);
if(_38c){
var _38e=doc.getElementsByTagName("style");
var _38f="";
for(var i=0;i<_38e.length;i++){
_38f=(_38e[i].styleSheet&&_38e[i].styleSheet.cssText)?_38e[i].styleSheet.cssText:_38e[i].innerHTML;
if(_38d==_38f){
return;
}
}
}
var _391=ds.insertCssText(_38d);
if(_391&&djConfig.isDebug){
_391.setAttribute("dbgHref",URI);
}
return _391;
};
ds.insertCssText=function(_392,doc,URI){
if(!_392){
return;
}
if(!doc){
doc=document;
}
if(URI){
_392=ds.fixPathsInCssText(_392,URI);
}
var _395=doc.createElement("style");
_395.setAttribute("type","text/css");
var head=doc.getElementsByTagName("head")[0];
if(!head){
dojo.debug("No head tag in document, aborting styles");
return;
}else{
head.appendChild(_395);
}
if(_395.styleSheet){
_395.styleSheet.cssText=_392;
}else{
var _397=doc.createTextNode(_392);
_395.appendChild(_397);
}
return _395;
};
ds.fixPathsInCssText=function(_398,URI){
if(!_398||!URI){
return;
}
var pos=0;
var str="";
var url="";
while(pos!=-1){
pos=0;
url="";
pos=_398.indexOf("url(",pos);
if(pos<0){
break;
}
str+=_398.slice(0,pos+4);
_398=_398.substring(pos+4,_398.length);
url+=_398.match(/^[\t\s\w()\/.\\'"-:#=&?]*\)/)[0];
_398=_398.substring(url.length-1,_398.length);
url=url.replace(/^[\s\t]*(['"]?)([\w()\/.\\'"-:#=&?]*)\1[\s\t]*?\)/,"$2");
if(url.search(/(file|https?|ftps?):\/\//)==-1){
url=(new dojo.uri.Uri(URI,url).toString());
}
str+=url;
}
return str+_398;
};
ds.getBackgroundColor=function(node){
node=dojo.byId(node);
var _39e;
do{
_39e=ds.getStyle(node,"background-color");
if(_39e.toLowerCase()=="rgba(0, 0, 0, 0)"){
_39e="transparent";
}
if(node==document.getElementsByTagName("body")[0]){
node=null;
break;
}
node=node.parentNode;
}while(node&&dojo.lang.inArray(_39e,["transparent",""]));
if(_39e=="transparent"){
_39e=[255,255,255,0];
}else{
_39e=dojo.graphics.color.extractRGB(_39e);
}
return _39e;
};
ds.getComputedStyle=function(node,_3a0,_3a1){
node=dojo.byId(node);
var _3a0=ds.toSelectorCase(_3a0);
var _3a2=ds.toCamelCase(_3a0);
if(!node||!node.style){
return _3a1;
}else{
if(document.defaultView){
try{
var cs=document.defaultView.getComputedStyle(node,"");
if(cs){
return cs.getPropertyValue(_3a0);
}
}
catch(e){
if(node.style.getPropertyValue){
return node.style.getPropertyValue(_3a0);
}else{
return _3a1;
}
}
}else{
if(node.currentStyle){
return node.currentStyle[_3a2];
}
}
}
if(node.style.getPropertyValue){
return node.style.getPropertyValue(_3a0);
}else{
return _3a1;
}
};
ds.getStyleProperty=function(node,_3a5){
node=dojo.byId(node);
return (node&&node.style?node.style[ds.toCamelCase(_3a5)]:undefined);
};
ds.getStyle=function(node,_3a7){
var _3a8=ds.getStyleProperty(node,_3a7);
return (_3a8?_3a8:ds.getComputedStyle(node,_3a7));
};
ds.setStyle=function(node,_3aa,_3ab){
node=dojo.byId(node);
if(node&&node.style){
var _3ac=ds.toCamelCase(_3aa);
node.style[_3ac]=_3ab;
}
};
ds.toCamelCase=function(_3ad){
var arr=_3ad.split("-"),cc=arr[0];
for(var i=1;i<arr.length;i++){
cc+=arr[i].charAt(0).toUpperCase()+arr[i].substring(1);
}
return cc;
};
ds.toSelectorCase=function(_3b0){
return _3b0.replace(/([A-Z])/g,"-$1").toLowerCase();
};
ds.setOpacity=function setOpacity(node,_3b2,_3b3){
node=dojo.byId(node);
if(!_3b3){
if(_3b2>=1){
if(h.ie){
ds.clearOpacity(node);
return;
}else{
_3b2=0.999999;
}
}else{
if(_3b2<0){
_3b2=0;
}
}
}
if(h.ie){
if(node.nodeName.toLowerCase()=="tr"){
var tds=node.getElementsByTagName("td");
for(var x=0;x<tds.length;x++){
tds[x].style.filter="Alpha(Opacity="+_3b2*100+")";
}
}
node.style.filter="Alpha(Opacity="+_3b2*100+")";
}else{
if(h.moz){
node.style.opacity=_3b2;
node.style.MozOpacity=_3b2;
}else{
if(h.safari){
node.style.opacity=_3b2;
node.style.KhtmlOpacity=_3b2;
}else{
node.style.opacity=_3b2;
}
}
}
};
ds.getOpacity=function getOpacity(node){
node=dojo.byId(node);
if(h.ie){
var opac=(node.filters&&node.filters.alpha&&typeof node.filters.alpha.opacity=="number"?node.filters.alpha.opacity:100)/100;
}else{
var opac=node.style.opacity||node.style.MozOpacity||node.style.KhtmlOpacity||1;
}
return opac>=0.999999?1:Number(opac);
};
ds.clearOpacity=function clearOpacity(node){
node=dojo.byId(node);
var ns=node.style;
if(h.ie){
try{
if(node.filters&&node.filters.alpha){
ns.filter="";
}
}
catch(e){
}
}else{
if(h.moz){
ns.opacity=1;
ns.MozOpacity=1;
}else{
if(h.safari){
ns.opacity=1;
ns.KhtmlOpacity=1;
}else{
ns.opacity=1;
}
}
}
};
ds._toggle=function(node,_3bb,_3bc){
node=dojo.byId(node);
_3bc(node,!_3bb(node));
return _3bb(node);
};
ds.show=function(node){
node=dojo.byId(node);
if(ds.getStyleProperty(node,"display")=="none"){
ds.setStyle(node,"display",(node.dojoDisplayCache||""));
node.dojoDisplayCache=undefined;
}
};
ds.hide=function(node){
node=dojo.byId(node);
if(typeof node["dojoDisplayCache"]=="undefined"){
var d=ds.getStyleProperty(node,"display");
if(d!="none"){
node.dojoDisplayCache=d;
}
}
ds.setStyle(node,"display","none");
};
ds.setShowing=function(node,_3c1){
ds[(_3c1?"show":"hide")](node);
};
ds.isShowing=function(node){
return (ds.getStyleProperty(node,"display")!="none");
};
ds.toggleShowing=function(node){
return ds._toggle(node,ds.isShowing,ds.setShowing);
};
ds.displayMap={tr:"",td:"",th:"",img:"inline",span:"inline",input:"inline",button:"inline"};
ds.suggestDisplayByTagName=function(node){
node=dojo.byId(node);
if(node&&node.tagName){
var tag=node.tagName.toLowerCase();
return (tag in ds.displayMap?ds.displayMap[tag]:"block");
}
};
ds.setDisplay=function(node,_3c7){
ds.setStyle(node,"display",(dojo.lang.isString(_3c7)?_3c7:(_3c7?ds.suggestDisplayByTagName(node):"none")));
};
ds.isDisplayed=function(node){
return (ds.getComputedStyle(node,"display")!="none");
};
ds.toggleDisplay=function(node){
return ds._toggle(node,ds.isDisplayed,ds.setDisplay);
};
ds.setVisibility=function(node,_3cb){
ds.setStyle(node,"visibility",(dojo.lang.isString(_3cb)?_3cb:(_3cb?"visible":"hidden")));
};
ds.isVisible=function(node){
return (ds.getComputedStyle(node,"visibility")!="hidden");
};
ds.toggleVisibility=function(node){
return ds._toggle(node,ds.isVisible,ds.setVisibility);
};
ds.toCoordinateArray=function(_3ce,_3cf){
if(dojo.lang.isArray(_3ce)){
while(_3ce.length<4){
_3ce.push(0);
}
while(_3ce.length>4){
_3ce.pop();
}
var ret=_3ce;
}else{
var node=dojo.byId(_3ce);
var pos=ds.getAbsolutePosition(node,_3cf);
var ret=[pos.x,pos.y,ds.getBorderBoxWidth(node),ds.getBorderBoxHeight(node)];
}
ret.x=ret[0];
ret.y=ret[1];
ret.w=ret[2];
ret.h=ret[3];
return ret;
};
})();
dojo.provide("dojo.html");
dojo.require("dojo.lang.func");
dojo.require("dojo.dom");
dojo.require("dojo.style");
dojo.require("dojo.string");
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
dojo.html.disableSelection=function(_3d3){
_3d3=dojo.byId(_3d3)||document.body;
var h=dojo.render.html;
if(h.mozilla){
_3d3.style.MozUserSelect="none";
}else{
if(h.safari){
_3d3.style.KhtmlUserSelect="none";
}else{
if(h.ie){
_3d3.unselectable="on";
}else{
return false;
}
}
}
return true;
};
dojo.html.enableSelection=function(_3d5){
_3d5=dojo.byId(_3d5)||document.body;
var h=dojo.render.html;
if(h.mozilla){
_3d5.style.MozUserSelect="";
}else{
if(h.safari){
_3d5.style.KhtmlUserSelect="";
}else{
if(h.ie){
_3d5.unselectable="off";
}else{
return false;
}
}
}
return true;
};
dojo.html.selectElement=function(_3d7){
_3d7=dojo.byId(_3d7);
if(document.selection&&document.body.createTextRange){
var _3d8=document.body.createTextRange();
_3d8.moveToElementText(_3d7);
_3d8.select();
}else{
if(window["getSelection"]){
var _3d9=window.getSelection();
if(_3d9["selectAllChildren"]){
_3d9.selectAllChildren(_3d7);
}
}
}
};
dojo.html.selectInputText=function(_3da){
_3da=dojo.byId(_3da);
if(document.selection&&document.body.createTextRange){
var _3db=_3da.createTextRange();
_3db.moveStart("character",0);
_3db.moveEnd("character",_3da.value.length);
_3db.select();
}else{
if(window["getSelection"]){
var _3dc=window.getSelection();
_3da.setSelectionRange(0,_3da.value.length);
}
}
_3da.focus();
};
dojo.html.isSelectionCollapsed=function(){
if(document["selection"]){
return document.selection.createRange().text=="";
}else{
if(window["getSelection"]){
var _3dd=window.getSelection();
if(dojo.lang.isString(_3dd)){
return _3dd=="";
}else{
return _3dd.isCollapsed;
}
}
}
};
dojo.html.getEventTarget=function(evt){
if(!evt){
evt=window.event||{};
}
var t=(evt.srcElement?evt.srcElement:(evt.target?evt.target:null));
while((t)&&(t.nodeType!=1)){
t=t.parentNode;
}
return t;
};
dojo.html.getDocumentWidth=function(){
dojo.deprecated("dojo.html.getDocument*","replaced by dojo.html.getViewport*","0.4");
return dojo.html.getViewportWidth();
};
dojo.html.getDocumentHeight=function(){
dojo.deprecated("dojo.html.getDocument*","replaced by dojo.html.getViewport*","0.4");
return dojo.html.getViewportHeight();
};
dojo.html.getDocumentSize=function(){
dojo.deprecated("dojo.html.getDocument*","replaced of dojo.html.getViewport*","0.4");
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
dojo.html.getScrollTop=function(){
return window.pageYOffset||document.documentElement.scrollTop||document.body.scrollTop||0;
};
dojo.html.getScrollLeft=function(){
return window.pageXOffset||document.documentElement.scrollLeft||document.body.scrollLeft||0;
};
dojo.html.getScrollOffset=function(){
var off=[dojo.html.getScrollLeft(),dojo.html.getScrollTop()];
off.x=off[0];
off.y=off[1];
return off;
};
dojo.html.getParentOfType=function(node,type){
dojo.deprecated("dojo.html.getParentOfType","replaced by dojo.html.getParentByType*","0.4");
return dojo.html.getParentByType(node,type);
};
dojo.html.getParentByType=function(node,type){
var _3e8=dojo.byId(node);
type=type.toLowerCase();
while((_3e8)&&(_3e8.nodeName.toLowerCase()!=type)){
if(_3e8==(document["body"]||document["documentElement"])){
return null;
}
_3e8=_3e8.parentNode;
}
return _3e8;
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
var c=dojo.html.getClass(node);
return (c=="")?[]:c.split(/\s+/g);
};
dojo.html.hasClass=function(node,_3f4){
return dojo.lang.inArray(dojo.html.getClasses(node),_3f4);
};
dojo.html.prependClass=function(node,_3f6){
_3f6+=" "+dojo.html.getClass(node);
return dojo.html.setClass(node,_3f6);
};
dojo.html.addClass=function(node,_3f8){
if(dojo.html.hasClass(node,_3f8)){
return false;
}
_3f8=dojo.string.trim(dojo.html.getClass(node)+" "+_3f8);
return dojo.html.setClass(node,_3f8);
};
dojo.html.setClass=function(node,_3fa){
node=dojo.byId(node);
var cs=new String(_3fa);
try{
if(typeof node.className=="string"){
node.className=cs;
}else{
if(node.setAttribute){
node.setAttribute("class",_3fa);
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
dojo.html.removeClass=function(node,_3fd,_3fe){
var _3fd=dojo.string.trim(new String(_3fd));
try{
var cs=dojo.html.getClasses(node);
var nca=[];
if(_3fe){
for(var i=0;i<cs.length;i++){
if(cs[i].indexOf(_3fd)==-1){
nca.push(cs[i]);
}
}
}else{
for(var i=0;i<cs.length;i++){
if(cs[i]!=_3fd){
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
dojo.html.replaceClass=function(node,_403,_404){
dojo.html.removeClass(node,_404);
dojo.html.addClass(node,_403);
};
dojo.html.classMatchType={ContainsAll:0,ContainsAny:1,IsOnly:2};
dojo.html.getElementsByClass=function(_405,_406,_407,_408,_409){
_406=dojo.byId(_406)||document;
var _40a=_405.split(/\s+/g);
var _40b=[];
if(_408!=1&&_408!=2){
_408=0;
}
var _40c=new RegExp("(\\s|^)(("+_40a.join(")|(")+"))(\\s|$)");
var _40d=[];
if(!_409&&document.evaluate){
var _40e="//"+(_407||"*")+"[contains(";
if(_408!=dojo.html.classMatchType.ContainsAny){
_40e+="concat(' ',@class,' '), ' "+_40a.join(" ') and contains(concat(' ',@class,' '), ' ")+" ')]";
}else{
_40e+="concat(' ',@class,' '), ' "+_40a.join(" ')) or contains(concat(' ',@class,' '), ' ")+" ')]";
}
var _40f=document.evaluate(_40e,_406,null,XPathResult.ANY_TYPE,null);
var _410=_40f.iterateNext();
while(_410){
try{
_40d.push(_410);
_410=_40f.iterateNext();
}
catch(e){
break;
}
}
return _40d;
}else{
if(!_407){
_407="*";
}
_40d=_406.getElementsByTagName(_407);
var node,i=0;
outer:
while(node=_40d[i++]){
var _412=dojo.html.getClasses(node);
if(_412.length==0){
continue outer;
}
var _413=0;
for(var j=0;j<_412.length;j++){
if(_40c.test(_412[j])){
if(_408==dojo.html.classMatchType.ContainsAny){
_40b.push(node);
continue outer;
}else{
_413++;
}
}else{
if(_408==dojo.html.classMatchType.IsOnly){
continue outer;
}
}
}
if(_413==_40a.length){
if((_408==dojo.html.classMatchType.IsOnly)&&(_413==_412.length)){
_40b.push(node);
}else{
if(_408==dojo.html.classMatchType.ContainsAll){
_40b.push(node);
}
}
}
}
return _40b;
}
};
dojo.html.getElementsByClassName=dojo.html.getElementsByClass;
dojo.html.getCursorPosition=function(e){
e=e||window.event;
var _416={x:0,y:0};
if(e.pageX||e.pageY){
_416.x=e.pageX;
_416.y=e.pageY;
}else{
var de=document.documentElement;
var db=document.body;
_416.x=e.clientX+((de||db)["scrollLeft"])-((de||db)["clientLeft"]);
_416.y=e.clientY+((de||db)["scrollTop"])-((de||db)["clientTop"]);
}
return _416;
};
dojo.html.overElement=function(_419,e){
_419=dojo.byId(_419);
var _41b=dojo.html.getCursorPosition(e);
with(dojo.html){
var top=getAbsoluteY(_419,true);
var _41d=top+getInnerHeight(_419);
var left=getAbsoluteX(_419,true);
var _41f=left+getInnerWidth(_419);
}
return (_41b.x>=left&&_41b.x<=_41f&&_41b.y>=top&&_41b.y<=_41d);
};
dojo.html.setActiveStyleSheet=function(_420){
var i=0,a,els=document.getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")){
a.disabled=true;
if(a.getAttribute("title")==_420){
a.disabled=false;
}
}
}
};
dojo.html.getActiveStyleSheet=function(){
var i=0,a,els=document.getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")&&!a.disabled){
return a.getAttribute("title");
}
}
return null;
};
dojo.html.getPreferredStyleSheet=function(){
var i=0,a,els=document.getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("rel").indexOf("alt")==-1&&a.getAttribute("title")){
return a.getAttribute("title");
}
}
return null;
};
dojo.html.body=function(){
return document.body||document.getElementsByTagName("body")[0];
};
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
dojo.html.copyStyle=function(_427,_428){
if(dojo.lang.isUndefined(_428.style.cssText)){
_427.setAttribute("style",_428.getAttribute("style"));
}else{
_427.style.cssText=_428.style.cssText;
}
dojo.html.addClass(_427,dojo.html.getClass(_428));
};
dojo.html._callExtrasDeprecated=function(_429,args){
var _42b="dojo.html.extras";
dojo.deprecated("dojo.html."+_429,"moved to "+_42b,"0.4");
dojo["require"](_42b);
return dojo.html[_429].apply(dojo.html,args);
};
dojo.html.createNodesFromText=function(){
return dojo.html._callExtrasDeprecated("createNodesFromText",arguments);
};
dojo.html.gravity=function(){
return dojo.html._callExtrasDeprecated("gravity",arguments);
};
dojo.html.placeOnScreen=function(){
return dojo.html._callExtrasDeprecated("placeOnScreen",arguments);
};
dojo.html.placeOnScreenPoint=function(){
return dojo.html._callExtrasDeprecated("placeOnScreenPoint",arguments);
};
dojo.html.renderedTextContent=function(){
return dojo.html._callExtrasDeprecated("renderedTextContent",arguments);
};
dojo.html.BackgroundIframe=function(){
return dojo.html._callExtrasDeprecated("BackgroundIframe",arguments);
};
dojo.provide("dojo.AdapterRegistry");
dojo.require("dojo.lang.func");
dojo.AdapterRegistry=function(){
this.pairs=[];
};
dojo.lang.extend(dojo.AdapterRegistry,{register:function(name,_42d,wrap,_42f){
if(_42f){
this.pairs.unshift([name,_42d,wrap]);
}else{
this.pairs.push([name,_42d,wrap]);
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
dojo.provide("dojo.json");
dojo.require("dojo.lang.func");
dojo.require("dojo.string.extras");
dojo.require("dojo.AdapterRegistry");
dojo.json={jsonRegistry:new dojo.AdapterRegistry(),register:function(name,_436,wrap,_438){
dojo.json.jsonRegistry.register(name,_436,wrap,_438);
},evalJson:function(json){
try{
return eval("("+json+")");
}
catch(e){
dojo.debug(e);
return json;
}
},evalJSON:function(json){
dojo.deprecated("dojo.json.evalJSON","use dojo.json.evalJson","0.4");
return this.evalJson(json);
},serialize:function(o){
var _43c=typeof (o);
if(_43c=="undefined"){
return "undefined";
}else{
if((_43c=="number")||(_43c=="boolean")){
return o+"";
}else{
if(o===null){
return "null";
}
}
}
if(_43c=="string"){
return dojo.string.escapeString(o);
}
var me=arguments.callee;
var _43e;
if(typeof (o.__json__)=="function"){
_43e=o.__json__();
if(o!==_43e){
return me(_43e);
}
}
if(typeof (o.json)=="function"){
_43e=o.json();
if(o!==_43e){
return me(_43e);
}
}
if(_43c!="function"&&typeof (o.length)=="number"){
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
_43e=dojo.json.jsonRegistry.match(o);
return me(_43e);
}
catch(e){
}
if(_43c=="function"){
return null;
}
res=[];
for(var k in o){
var _443;
if(typeof (k)=="number"){
_443="\""+k+"\"";
}else{
if(typeof (k)=="string"){
_443=dojo.string.escapeString(k);
}else{
continue;
}
}
val=me(o[k]);
if(typeof (val)!="string"){
continue;
}
res.push(_443+":"+val);
}
return "{"+res.join(",")+"}";
}};
if(!this["dojo"]){
alert("\"dojo/__package__.js\" is now located at \"dojo/dojo.js\". Please update your includes accordingly");
}
dojo.provide("dojo.xml.Parse");
dojo.require("dojo.dom");
dojo.xml.Parse=function(){
function getDojoTagName(node){
var _445=node.tagName;
if(_445.substr(0,5).toLowerCase()!="dojo:"){
if(_445.substr(0,4).toLowerCase()=="dojo"){
return "dojo:"+_445.substring(4).toLowerCase();
}
var djt=node.getAttribute("dojoType")||node.getAttribute("dojotype");
if(djt){
return "dojo:"+djt.toLowerCase();
}
if(node.getAttributeNS&&node.getAttributeNS(dojo.dom.dojoml,"type")){
return "dojo:"+node.getAttributeNS(dojo.dom.dojoml,"type").toLowerCase();
}
try{
djt=node.getAttribute("dojo:type");
}
catch(e){
}
if(djt){
return "dojo:"+djt.toLowerCase();
}
if(!dj_global["djConfig"]||!djConfig["ignoreClassNames"]){
var _447=node.className||node.getAttribute("class");
if(_447&&_447.indexOf&&_447.indexOf("dojo-")!=-1){
var _448=_447.split(" ");
for(var x=0;x<_448.length;x++){
if(_448[x].length>5&&_448[x].indexOf("dojo-")>=0){
return "dojo:"+_448[x].substr(5).toLowerCase();
}
}
}
}
}
return _445.toLowerCase();
}
this.parseElement=function(node,_44b,_44c,_44d){
if(node.getAttribute("parseWidgets")=="false"){
return {};
}
var _44e={};
var _44f=getDojoTagName(node);
_44e[_44f]=[];
if((!_44c)||(_44f.substr(0,4).toLowerCase()=="dojo")){
var _450=parseAttributes(node);
for(var attr in _450){
if((!_44e[_44f][attr])||(typeof _44e[_44f][attr]!="array")){
_44e[_44f][attr]=[];
}
_44e[_44f][attr].push(_450[attr]);
}
_44e[_44f].nodeRef=node;
_44e.tagName=_44f;
_44e.index=_44d||0;
}
var _452=0;
var tcn,i=0,nodes=node.childNodes;
while(tcn=nodes[i++]){
switch(tcn.nodeType){
case dojo.dom.ELEMENT_NODE:
_452++;
var ctn=getDojoTagName(tcn);
if(!_44e[ctn]){
_44e[ctn]=[];
}
_44e[ctn].push(this.parseElement(tcn,true,_44c,_452));
if((tcn.childNodes.length==1)&&(tcn.childNodes.item(0).nodeType==dojo.dom.TEXT_NODE)){
_44e[ctn][_44e[ctn].length-1].value=tcn.childNodes.item(0).nodeValue;
}
break;
case dojo.dom.TEXT_NODE:
if(node.childNodes.length==1){
_44e[_44f].push({value:node.childNodes.item(0).nodeValue});
}
break;
default:
break;
}
}
return _44e;
};
function parseAttributes(node){
var _456={};
var atts=node.attributes;
var _458,i=0;
while(_458=atts[i++]){
if((dojo.render.html.capable)&&(dojo.render.html.ie)){
if(!_458){
continue;
}
if((typeof _458=="object")&&(typeof _458.nodeValue=="undefined")||(_458.nodeValue==null)||(_458.nodeValue=="")){
continue;
}
}
var nn=(_458.nodeName.indexOf("dojo:")==-1)?_458.nodeName:_458.nodeName.split("dojo:")[1];
_456[nn]={value:_458.nodeValue};
}
return _456;
}
};
dojo.provide("dojo.lang.declare");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.extras");
dojo.lang.declare=function(_45a,_45b,_45c,init){
var ctor=function(){
var self=this._getPropContext();
var s=self.constructor.superclass;
if((s)&&(s.constructor)){
if(s.constructor==arguments.callee){
this.inherited("constructor",arguments);
}else{
this._inherited(s,"constructor",arguments);
}
}
if((!this.prototyping)&&(self.initializer)){
self.initializer.apply(this,arguments);
}
};
var scp=(_45b?_45b.prototype:null);
if(scp){
scp.prototyping=true;
ctor.prototype=new _45b();
scp.prototyping=false;
}
ctor.prototype.constructor=ctor;
ctor.superclass=scp;
dojo.lang.extend(ctor,dojo.lang.declare.base);
_45c=(_45c||{});
_45c.initializer=(_45c.initializer)||(init)||(function(){
});
_45c.className=_45a;
dojo.lang.extend(ctor,_45c);
dojo.lang.setObjPathValue(_45a,ctor,null,true);
};
dojo.lang.declare.base={_getPropContext:function(){
return (this.___proto||this);
},_inherited:function(_462,_463,args){
var _465=this.___proto;
this.___proto=_462;
var _466=_462[_463].apply(this,(args||[]));
this.___proto=_465;
return _466;
},inherited:function(prop,args){
var p=this._getPropContext();
do{
if((!p.constructor)||(!p.constructor.superclass)){
return;
}
p=p.constructor.superclass;
}while(!(prop in p));
return (typeof p[prop]=="function"?this._inherited(p,prop,args):p[prop]);
}};
dojo.declare=dojo.lang.declare;
dojo.provide("dojo.widget.Manager");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.func");
dojo.require("dojo.event.*");
dojo.widget.manager=new function(){
this.widgets=[];
this.widgetIds=[];
this.topWidgets={};
var _46a={};
var _46b=[];
this.getUniqueId=function(_46c){
return _46c+"_"+(_46a[_46c]!=undefined?++_46a[_46c]:_46a[_46c]=0);
};
this.add=function(_46d){
dojo.profile.start("dojo.widget.manager.add");
this.widgets.push(_46d);
if(!_46d.extraArgs["id"]){
_46d.extraArgs["id"]=_46d.extraArgs["ID"];
}
if(_46d.widgetId==""){
if(_46d["id"]){
_46d.widgetId=_46d["id"];
}else{
if(_46d.extraArgs["id"]){
_46d.widgetId=_46d.extraArgs["id"];
}else{
_46d.widgetId=this.getUniqueId(_46d.widgetType);
}
}
}
if(this.widgetIds[_46d.widgetId]){
dojo.debug("widget ID collision on ID: "+_46d.widgetId);
}
this.widgetIds[_46d.widgetId]=_46d;
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
this.remove=function(_46f){
var tw=this.widgets[_46f].widgetId;
delete this.widgetIds[tw];
this.widgets.splice(_46f,1);
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
dojo.deprecated("getWidgetsOfType","use getWidgetsByType","0.4");
return dojo.widget.manager.getWidgetsByType(id);
};
this.getWidgetsByFilter=function(_479,_47a){
var ret=[];
dojo.lang.every(this.widgets,function(x){
if(_479(x)){
ret.push(x);
if(_47a){
return false;
}
}
return true;
});
return (_47a?ret[0]:ret);
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
var _480={};
var _481=["dojo.widget"];
for(var i=0;i<_481.length;i++){
_481[_481[i]]=true;
}
this.registerWidgetPackage=function(_483){
if(!_481[_483]){
_481[_483]=true;
_481.push(_483);
}
};
this.getWidgetPackageList=function(){
return dojo.lang.map(_481,function(elt){
return (elt!==true?elt:undefined);
});
};
this.getImplementation=function(_485,_486,_487){
var impl=this.getImplementationName(_485);
if(impl){
var ret=new impl(_486);
return ret;
}
};
this.getImplementationName=function(_48a){
var _48b=_48a.toLowerCase();
var impl=_480[_48b];
if(impl){
return impl;
}
if(!_46b.length){
for(var _48d in dojo.render){
if(dojo.render[_48d]["capable"]===true){
var _48e=dojo.render[_48d].prefixes;
for(var i=0;i<_48e.length;i++){
_46b.push(_48e[i].toLowerCase());
}
}
}
_46b.push("");
}
for(var i=0;i<_481.length;i++){
var _490=dojo.evalObjPath(_481[i]);
if(!_490){
continue;
}
for(var j=0;j<_46b.length;j++){
if(!_490[_46b[j]]){
continue;
}
for(var _492 in _490[_46b[j]]){
if(_492.toLowerCase()!=_48b){
continue;
}
_480[_48b]=_490[_46b[j]][_492];
return _480[_48b];
}
}
for(var j=0;j<_46b.length;j++){
for(var _492 in _490){
if(_492.toLowerCase()!=(_46b[j]+_48b)){
continue;
}
_480[_48b]=_490[_492];
return _480[_48b];
}
}
}
throw new Error("Could not locate \""+_48a+"\" class");
};
this.resizing=false;
this.onWindowResized=function(){
if(this.resizing){
return;
}
try{
this.resizing=true;
for(var id in this.topWidgets){
var _494=this.topWidgets[id];
if(_494.onParentResized){
_494.onParentResized();
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
dojo.addOnLoad(this,"onWindowResized");
dojo.event.connect(window,"onresize",this,"onWindowResized");
}
};
(function(){
var dw=dojo.widget;
var dwm=dw.manager;
var h=dojo.lang.curry(dojo.lang,"hitch",dwm);
var g=function(_499,_49a){
dw[(_49a||_499)]=h(_499);
};
g("add","addWidget");
g("destroyAll","destroyAllWidgets");
g("remove","removeWidget");
g("removeById","removeWidgetById");
g("getWidgetById");
g("getWidgetById","byId");
g("getWidgetsByType");
g("getWidgetsByFilter");
g("getWidgetsByType","byType");
g("getWidgetsByFilter","byFilter");
g("getWidgetByNode","byNode");
dw.all=function(n){
var _49c=dwm.getAllWidgets.apply(dwm,arguments);
if(arguments.length>0){
return _49c[n];
}
return _49c;
};
g("registerWidgetPackage");
g("getImplementation","getWidgetImplementation");
g("getImplementationName","getWidgetImplementationName");
dw.widgets=dwm.widgets;
dw.widgetIds=dwm.widgetIds;
dw.root=dwm.root;
})();
dojo.provide("dojo.widget.Widget");
dojo.provide("dojo.widget.tags");
dojo.require("dojo.lang.func");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.extras");
dojo.require("dojo.lang.declare");
dojo.require("dojo.widget.Manager");
dojo.require("dojo.event.*");
dojo.declare("dojo.widget.Widget",null,{initializer:function(){
this.children=[];
this.extraArgs={};
},parent:null,isTopLevel:false,isModal:false,isEnabled:true,isHidden:false,isContainer:false,widgetId:"",widgetType:"Widget",toString:function(){
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
var _49e=this.children[i];
if(_49e.onResized){
_49e.onResized();
}
}
},create:function(args,_4a0,_4a1){
this.satisfyPropertySets(args,_4a0,_4a1);
this.mixInProperties(args,_4a0,_4a1);
this.postMixInProperties(args,_4a0,_4a1);
dojo.widget.manager.add(this);
this.buildRendering(args,_4a0,_4a1);
this.initialize(args,_4a0,_4a1);
this.postInitialize(args,_4a0,_4a1);
this.postCreate(args,_4a0,_4a1);
return this;
},destroy:function(_4a2){
this.destroyChildren();
this.uninitialize();
this.destroyRendering(_4a2);
dojo.widget.manager.removeById(this.widgetId);
},destroyChildren:function(){
while(this.children.length>0){
var tc=this.children[0];
this.removeChild(tc);
tc.destroy();
}
},getChildrenOfType:function(type,_4a5){
var ret=[];
var _4a7=dojo.lang.isFunction(type);
if(!_4a7){
type=type.toLowerCase();
}
for(var x=0;x<this.children.length;x++){
if(_4a7){
if(this.children[x] instanceof type){
ret.push(this.children[x]);
}
}else{
if(this.children[x].widgetType.toLowerCase()==type){
ret.push(this.children[x]);
}
}
if(_4a5){
ret=ret.concat(this.children[x].getChildrenOfType(type,_4a5));
}
}
return ret;
},getDescendants:function(){
var _4a9=[];
var _4aa=[this];
var elem;
while(elem=_4aa.pop()){
_4a9.push(elem);
dojo.lang.forEach(elem.children,function(elem){
_4aa.push(elem);
});
}
return _4a9;
},satisfyPropertySets:function(args){
return args;
},mixInProperties:function(args,frag){
if((args["fastMixIn"])||(frag["fastMixIn"])){
for(var x in args){
this[x]=args[x];
}
return;
}
var _4b1;
var _4b2=dojo.widget.lcArgsCache[this.widgetType];
if(_4b2==null){
_4b2={};
for(var y in this){
_4b2[((new String(y)).toLowerCase())]=y;
}
dojo.widget.lcArgsCache[this.widgetType]=_4b2;
}
var _4b4={};
for(var x in args){
if(!this[x]){
var y=_4b2[(new String(x)).toLowerCase()];
if(y){
args[y]=args[x];
x=y;
}
}
if(_4b4[x]){
continue;
}
_4b4[x]=true;
if((typeof this[x])!=(typeof _4b1)){
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
if(args[x].search(/[^\w\.]+/i)==-1){
this[x]=dojo.evalObjPath(args[x],false);
}else{
var tn=dojo.lang.nameAnonFunc(new Function(args[x]),this);
dojo.event.connect(this,x,this,tn);
}
}else{
if(dojo.lang.isArray(this[x])){
this[x]=args[x].split(";");
}else{
if(this[x] instanceof Date){
this[x]=new Date(Number(args[x]));
}else{
if(typeof this[x]=="object"){
if(this[x] instanceof dojo.uri.Uri){
this[x]=args[x];
}else{
var _4b6=args[x].split(";");
for(var y=0;y<_4b6.length;y++){
var si=_4b6[y].indexOf(":");
if((si!=-1)&&(_4b6[y].length>si)){
this[x][_4b6[y].substr(0,si).replace(/^\s+|\s+$/g,"")]=_4b6[y].substr(si+1);
}
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
this.extraArgs[x.toLowerCase()]=args[x];
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
dojo.unimplemented("dojo.widget.Widget.buildRendering, on "+this.toString()+", ");
return false;
},destroyRendering:function(){
dojo.unimplemented("dojo.widget.Widget.destroyRendering");
return false;
},cleanUp:function(){
dojo.unimplemented("dojo.widget.Widget.cleanUp");
return false;
},addedTo:function(_4be){
},addChild:function(_4bf){
dojo.unimplemented("dojo.widget.Widget.addChild");
return false;
},removeChild:function(_4c0){
for(var x=0;x<this.children.length;x++){
if(this.children[x]===_4c0){
this.children.splice(x,1);
break;
}
}
return _4c0;
},resize:function(_4c2,_4c3){
this.setWidth(_4c2);
this.setHeight(_4c3);
},setWidth:function(_4c4){
if((typeof _4c4=="string")&&(_4c4.substr(-1)=="%")){
this.setPercentageWidth(_4c4);
}else{
this.setNativeWidth(_4c4);
}
},setHeight:function(_4c5){
if((typeof _4c5=="string")&&(_4c5.substr(-1)=="%")){
this.setPercentageHeight(_4c5);
}else{
this.setNativeHeight(_4c5);
}
},setPercentageHeight:function(_4c6){
return false;
},setNativeHeight:function(_4c7){
return false;
},setPercentageWidth:function(_4c8){
return false;
},setNativeWidth:function(_4c9){
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
var _4cd=type.toLowerCase();
this[_4cd]=function(_4ce,_4cf,_4d0,_4d1,_4d2){
return dojo.widget.buildWidgetFromParseTree(_4cd,_4ce,_4cf,_4d0,_4d1,_4d2);
};
};
dojo.widget.tags.addParseTreeHandler("dojo:widget");
dojo.widget.tags["dojo:propertyset"]=function(_4d3,_4d4,_4d5){
var _4d6=_4d4.parseProperties(_4d3["dojo:propertyset"]);
};
dojo.widget.tags["dojo:connect"]=function(_4d7,_4d8,_4d9){
var _4da=_4d8.parseProperties(_4d7["dojo:connect"]);
};
dojo.widget.buildWidgetFromParseTree=function(type,frag,_4dd,_4de,_4df,_4e0){
var _4e1=type.split(":");
_4e1=(_4e1.length==2)?_4e1[1]:type;
var _4e2=_4e0||_4dd.parseProperties(frag["dojo:"+_4e1]);
var _4e3=dojo.widget.manager.getImplementation(_4e1);
if(!_4e3){
throw new Error("cannot find \""+_4e1+"\" widget");
}else{
if(!_4e3.create){
throw new Error("\""+_4e1+"\" widget object does not appear to implement *Widget");
}
}
_4e2["dojoinsertionindex"]=_4df;
var ret=_4e3.create(_4e2,frag,_4de);
return ret;
};
dojo.widget.defineWidget=function(_4e5,_4e6,_4e7,_4e8,ctor){
var _4ea=_4e5.split(".");
var type=_4ea.pop();
if(_4e8){
while((_4ea.length)&&(_4ea.pop()!=_4e8)){
}
}
_4ea=_4ea.join(".");
dojo.widget.manager.registerWidgetPackage(_4ea);
dojo.widget.tags.addParseTreeHandler("dojo:"+type.toLowerCase());
if(!_4e7){
_4e7={};
}
_4e7.widgetType=type;
if((!ctor)&&(_4e7["classConstructor"])){
ctor=_4e7.classConstructor;
delete _4e7.classConstructor;
}
dojo.declare(_4e5,_4e6,_4e7,ctor);
};
dojo.provide("dojo.widget.Parse");
dojo.require("dojo.widget.Manager");
dojo.require("dojo.dom");
dojo.widget.Parse=function(_4ec){
this.propertySetsList=[];
this.fragment=_4ec;
this.createComponents=function(frag,_4ee){
var _4ef=[];
var _4f0=false;
try{
if((frag)&&(frag["tagName"])&&(frag!=frag["nodeRef"])){
var _4f1=dojo.widget.tags;
var tna=String(frag["tagName"]).split(";");
for(var x=0;x<tna.length;x++){
var ltn=(tna[x].replace(/^\s+|\s+$/g,"")).toLowerCase();
if(_4f1[ltn]){
_4f0=true;
frag.tagName=ltn;
var ret=_4f1[ltn](frag,this,_4ee,frag["index"]);
_4ef.push(ret);
}else{
if((dojo.lang.isString(ltn))&&(ltn.substr(0,5)=="dojo:")){
dojo.debug("no tag handler registed for type: ",ltn);
}
}
}
}
}
catch(e){
dojo.debug("dojo.widget.Parse: error:",e);
}
if(!_4f0){
_4ef=_4ef.concat(this.createSubComponents(frag,_4ee));
}
return _4ef;
};
this.createSubComponents=function(_4f6,_4f7){
var frag,comps=[];
for(var item in _4f6){
frag=_4f6[item];
if((frag)&&(typeof frag=="object")&&(frag!=_4f6.nodeRef)&&(frag!=_4f6["tagName"])){
comps=comps.concat(this.createComponents(frag,_4f7));
}
}
return comps;
};
this.parsePropertySets=function(_4fa){
return [];
var _4fb=[];
for(var item in _4fa){
if((_4fa[item]["tagName"]=="dojo:propertyset")){
_4fb.push(_4fa[item]);
}
}
this.propertySetsList.push(_4fb);
return _4fb;
};
this.parseProperties=function(_4fd){
var _4fe={};
for(var item in _4fd){
if((_4fd[item]==_4fd["tagName"])||(_4fd[item]==_4fd.nodeRef)){
}else{
if((_4fd[item]["tagName"])&&(dojo.widget.tags[_4fd[item].tagName.toLowerCase()])){
}else{
if((_4fd[item][0])&&(_4fd[item][0].value!="")&&(_4fd[item][0].value!=null)){
try{
if(item.toLowerCase()=="dataprovider"){
var _500=this;
this.getDataProvider(_500,_4fd[item][0].value);
_4fe.dataProvider=this.dataProvider;
}
_4fe[item]=_4fd[item][0].value;
var _501=this.parseProperties(_4fd[item]);
for(var _502 in _501){
_4fe[_502]=_501[_502];
}
}
catch(e){
dojo.debug(e);
}
}
}
}
}
return _4fe;
};
this.getDataProvider=function(_503,_504){
dojo.io.bind({url:_504,load:function(type,_506){
if(type=="load"){
_503.dataProvider=_506;
}
},mimetype:"text/javascript",sync:true});
};
this.getPropertySetById=function(_507){
for(var x=0;x<this.propertySetsList.length;x++){
if(_507==this.propertySetsList[x]["id"][0].value){
return this.propertySetsList[x];
}
}
return "";
};
this.getPropertySetsByType=function(_509){
var _50a=[];
for(var x=0;x<this.propertySetsList.length;x++){
var cpl=this.propertySetsList[x];
var cpcc=cpl["componentClass"]||cpl["componentType"]||null;
if((cpcc)&&(propertySetId==cpcc[0].value)){
_50a.push(cpl);
}
}
return _50a;
};
this.getPropertySets=function(_50e){
var ppl="dojo:propertyproviderlist";
var _510=[];
var _511=_50e["tagName"];
if(_50e[ppl]){
var _512=_50e[ppl].value.split(" ");
for(var _513 in _512){
if((_513.indexOf("..")==-1)&&(_513.indexOf("://")==-1)){
var _514=this.getPropertySetById(_513);
if(_514!=""){
_510.push(_514);
}
}else{
}
}
}
return (this.getPropertySetsByType(_511)).concat(_510);
};
this.createComponentFromScript=function(_515,_516,_517){
var ltn="dojo:"+_516.toLowerCase();
if(dojo.widget.tags[ltn]){
_517.fastMixIn=true;
return [dojo.widget.tags[ltn](_517,this,null,null,_517)];
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
dojo.widget.createWidget=function(name,_51b,_51c,_51d){
var _51e=name.toLowerCase();
var _51f="dojo:"+_51e;
var _520=(dojo.byId(name)&&(!dojo.widget.tags[_51f]));
if((arguments.length==1)&&((typeof name!="string")||(_520))){
var xp=new dojo.xml.Parse();
var tn=(_520)?dojo.byId(name):name;
return dojo.widget.getParser().createComponents(xp.parseElement(tn,null,true))[0];
}
function fromScript(_523,name,_525){
_525[_51f]={dojotype:[{value:_51e}],nodeRef:_523,fastMixIn:true};
return dojo.widget.getParser().createComponentFromScript(_523,name,_525,true);
}
if(typeof name!="string"&&typeof _51b=="string"){
dojo.deprecated("dojo.widget.createWidget","argument order is now of the form "+"dojo.widget.createWidget(NAME, [PROPERTIES, [REFERENCENODE, [POSITION]]])","0.4");
return fromScript(name,_51b,_51c);
}
_51b=_51b||{};
var _526=false;
var tn=null;
var h=dojo.render.html.capable;
if(h){
tn=document.createElement("span");
}
if(!_51c){
_526=true;
_51c=tn;
if(h){
document.body.appendChild(_51c);
}
}else{
if(_51d){
dojo.dom.insertAtPosition(tn,_51c,_51d);
}else{
tn=_51c;
}
}
var _528=fromScript(tn,name,_51b);
if(!_528[0]||typeof _528[0].widgetType=="undefined"){
throw new Error("createWidget: Creation of \""+name+"\" widget failed.");
}
if(_526){
if(_528[0].domNode.parentNode){
_528[0].domNode.parentNode.removeChild(_528[0].domNode);
}
}
return _528[0];
};
dojo.widget.fromScript=function(name,_52a,_52b,_52c){
dojo.deprecated("dojo.widget.fromScript"," use "+"dojo.widget.createWidget instead","0.4");
return dojo.widget.createWidget(name,_52a,_52b,_52c);
};
dojo.kwCompoundRequire({common:["dojo.uri.Uri",false,false]});
dojo.provide("dojo.uri.*");
dojo.provide("dojo.widget.DomWidget");
dojo.require("dojo.event.*");
dojo.require("dojo.widget.Widget");
dojo.require("dojo.dom");
dojo.require("dojo.xml.Parse");
dojo.require("dojo.uri.*");
dojo.require("dojo.lang.func");
dojo.require("dojo.lang.extras");
dojo.widget._cssFiles={};
dojo.widget._cssStrings={};
dojo.widget._templateCache={};
dojo.widget.defaultStrings={dojoRoot:dojo.hostenv.getBaseScriptUri(),baseScriptUri:dojo.hostenv.getBaseScriptUri()};
dojo.widget.buildFromTemplate=function(){
dojo.lang.forward("fillFromTemplateCache");
};
dojo.widget.fillFromTemplateCache=function(obj,_52e,_52f,_530,_531){
var _532=_52e||obj.templatePath;
var _533=_52f||obj.templateCssPath;
if(_532&&!(_532 instanceof dojo.uri.Uri)){
_532=dojo.uri.dojoUri(_532);
dojo.deprecated("templatePath should be of type dojo.uri.Uri",null,"0.4");
}
if(_533&&!(_533 instanceof dojo.uri.Uri)){
_533=dojo.uri.dojoUri(_533);
dojo.deprecated("templateCssPath should be of type dojo.uri.Uri",null,"0.4");
}
var _534=dojo.widget._templateCache;
if(!obj["widgetType"]){
do{
var _535="__dummyTemplate__"+dojo.widget._templateCache.dummyCount++;
}while(_534[_535]);
obj.widgetType=_535;
}
var wt=obj.widgetType;
if(_533&&!dojo.widget._cssFiles[_533.toString()]){
if((!obj.templateCssString)&&(_533)){
obj.templateCssString=dojo.hostenv.getText(_533);
obj.templateCssPath=null;
}
if((obj["templateCssString"])&&(!obj.templateCssString["loaded"])){
dojo.style.insertCssText(obj.templateCssString,null,_533);
if(!obj.templateCssString){
obj.templateCssString="";
}
obj.templateCssString.loaded=true;
}
dojo.widget._cssFiles[_533.toString()]=true;
}
var ts=_534[wt];
if(!ts){
_534[wt]={"string":null,"node":null};
if(_531){
ts={};
}else{
ts=_534[wt];
}
}
if((!obj.templateString)&&(!_531)){
obj.templateString=_530||ts["string"];
}
if((!obj.templateNode)&&(!_531)){
obj.templateNode=ts["node"];
}
if((!obj.templateNode)&&(!obj.templateString)&&(_532)){
var _538=dojo.hostenv.getText(_532);
if(_538){
var _539=_538.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
if(_539){
_538=_539[1];
}
}else{
_538="";
}
obj.templateString=_538;
if(!_531){
_534[wt]["string"]=_538;
}
}
if((!ts["string"])&&(!_531)){
ts.string=obj.templateString;
}
};
dojo.widget._templateCache.dummyCount=0;
dojo.widget.attachProperties=["dojoAttachPoint","id"];
dojo.widget.eventAttachProperty="dojoAttachEvent";
dojo.widget.onBuildProperty="dojoOnBuild";
dojo.widget.waiNames=["waiRole","waiState"];
dojo.widget.wai={waiRole:{name:"waiRole",namespace:"http://www.w3.org/TR/xhtml2",alias:"x2",prefix:"wairole:",nsName:"role"},waiState:{name:"waiState",namespace:"http://www.w3.org/2005/07/aaa",alias:"aaa",prefix:"",nsName:"state"},setAttr:function(node,attr,_53c){
if(dojo.render.html.ie){
node.setAttribute(this[attr].alias+":"+this[attr].nsName,this[attr].prefix+_53c);
}else{
node.setAttributeNS(this[attr].namespace,this[attr].nsName,this[attr].prefix+_53c);
}
}};
dojo.widget.attachTemplateNodes=function(_53d,_53e,_53f){
var _540=dojo.dom.ELEMENT_NODE;
function trim(str){
return str.replace(/^\s+|\s+$/g,"");
}
if(!_53d){
_53d=_53e.domNode;
}
if(_53d.nodeType!=_540){
return;
}
var _542=_53d.all||_53d.getElementsByTagName("*");
var _543=_53e;
for(var x=-1;x<_542.length;x++){
var _545=(x==-1)?_53d:_542[x];
var _546=[];
for(var y=0;y<this.attachProperties.length;y++){
var _548=_545.getAttribute(this.attachProperties[y]);
if(_548){
_546=_548.split(";");
for(var z=0;z<_546.length;z++){
if(dojo.lang.isArray(_53e[_546[z]])){
_53e[_546[z]].push(_545);
}else{
_53e[_546[z]]=_545;
}
}
break;
}
}
var _54a=_545.getAttribute(this.templateProperty);
if(_54a){
_53e[_54a]=_545;
}
dojo.lang.forEach(dojo.widget.waiNames,function(name){
var wai=dojo.widget.wai[name];
var val=_545.getAttribute(wai.name);
if(val){
dojo.widget.wai.setAttr(_545,wai.name,val);
}
},this);
var _54e=_545.getAttribute(this.eventAttachProperty);
if(_54e){
var evts=_54e.split(";");
for(var y=0;y<evts.length;y++){
if((!evts[y])||(!evts[y].length)){
continue;
}
var _550=null;
var tevt=trim(evts[y]);
if(evts[y].indexOf(":")>=0){
var _552=tevt.split(":");
tevt=trim(_552[0]);
_550=trim(_552[1]);
}
if(!_550){
_550=tevt;
}
var tf=function(){
var ntf=new String(_550);
return function(evt){
if(_543[ntf]){
_543[ntf](dojo.event.browser.fixEvent(evt,this));
}
};
}();
dojo.event.browser.addListener(_545,tevt,tf,false,true);
}
}
for(var y=0;y<_53f.length;y++){
var _556=_545.getAttribute(_53f[y]);
if((_556)&&(_556.length)){
var _550=null;
var _557=_53f[y].substr(4);
_550=trim(_556);
var _558=[_550];
if(_550.indexOf(";")>=0){
_558=dojo.lang.map(_550.split(";"),trim);
}
for(var z=0;z<_558.length;z++){
if(!_558[z].length){
continue;
}
var tf=function(){
var ntf=new String(_558[z]);
return function(evt){
if(_543[ntf]){
_543[ntf](dojo.event.browser.fixEvent(evt,this));
}
};
}();
dojo.event.browser.addListener(_545,_557,tf,false,true);
}
}
}
var _55b=_545.getAttribute(this.onBuildProperty);
if(_55b){
eval("var node = baseNode; var widget = targetObj; "+_55b);
}
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
dojo.declare("dojo.widget.DomWidget",dojo.widget.Widget,{initializer:function(){
if((arguments.length>0)&&(typeof arguments[0]=="object")){
this.create(arguments[0]);
}
},templateNode:null,templateString:null,templateCssString:null,preventClobber:false,domNode:null,containerNode:null,addChild:function(_563,_564,pos,ref,_567){
if(!this.isContainer){
dojo.debug("dojo.widget.DomWidget.addChild() attempted on non-container widget");
return null;
}else{
this.addWidgetAsDirectChild(_563,_564,pos,ref,_567);
this.registerChild(_563,_567);
}
return _563;
},addWidgetAsDirectChild:function(_568,_569,pos,ref,_56c){
if((!this.containerNode)&&(!_569)){
this.containerNode=this.domNode;
}
var cn=(_569)?_569:this.containerNode;
if(!pos){
pos="after";
}
if(!ref){
if(!cn){
cn=document.body;
}
ref=cn.lastChild;
}
if(!_56c){
_56c=0;
}
_568.domNode.setAttribute("dojoinsertionindex",_56c);
if(!ref){
cn.appendChild(_568.domNode);
}else{
if(pos=="insertAtIndex"){
dojo.dom.insertAtIndex(_568.domNode,ref.parentNode,_56c);
}else{
if((pos=="after")&&(ref===cn.lastChild)){
cn.appendChild(_568.domNode);
}else{
dojo.dom.insertAtPosition(_568.domNode,cn,pos);
}
}
}
},registerChild:function(_56e,_56f){
_56e.dojoInsertionIndex=_56f;
var idx=-1;
for(var i=0;i<this.children.length;i++){
if(this.children[i].dojoInsertionIndex<_56f){
idx=i;
}
}
this.children.splice(idx+1,0,_56e);
_56e.parent=this;
_56e.addedTo(this);
delete dojo.widget.manager.topWidgets[_56e.widgetId];
},removeChild:function(_572){
dojo.dom.removeNode(_572.domNode);
return dojo.widget.DomWidget.superclass.removeChild.call(this,_572);
},getFragNodeRef:function(frag){
if(!frag||!frag["dojo:"+this.widgetType.toLowerCase()]){
dojo.raise("Error: no frag for widget type "+this.widgetType+", id "+this.widgetId+" (maybe a widget has set it's type incorrectly)");
}
return (frag?frag["dojo:"+this.widgetType.toLowerCase()]["nodeRef"]:null);
},postInitialize:function(args,frag,_576){
var _577=this.getFragNodeRef(frag);
if(_576&&(_576.snarfChildDomOutput||!_577)){
_576.addWidgetAsDirectChild(this,"","insertAtIndex","",args["dojoinsertionindex"],_577);
}else{
if(_577){
if(this.domNode&&(this.domNode!==_577)){
var _578=_577.parentNode.replaceChild(this.domNode,_577);
}
}
}
if(_576){
_576.registerChild(this,args.dojoinsertionindex);
}else{
dojo.widget.manager.topWidgets[this.widgetId]=this;
}
if(this.isContainer){
var _579=dojo.widget.getParser();
_579.createSubComponents(frag,this);
}
},buildRendering:function(args,frag){
var ts=dojo.widget._templateCache[this.widgetType];
if((!this.preventClobber)&&((this.templatePath)||(this.templateNode)||((this["templateString"])&&(this.templateString.length))||((typeof ts!="undefined")&&((ts["string"])||(ts["node"]))))){
this.buildFromTemplate(args,frag);
}else{
this.domNode=this.getFragNodeRef(frag);
}
this.fillInTemplate(args,frag);
},buildFromTemplate:function(args,frag){
var _57f=false;
if(args["templatecsspath"]){
args["templateCssPath"]=args["templatecsspath"];
}
if(args["templatepath"]){
_57f=true;
args["templatePath"]=args["templatepath"];
}
dojo.widget.fillFromTemplateCache(this,args["templatePath"],args["templateCssPath"],null,_57f);
var ts=dojo.widget._templateCache[this.widgetType];
if((ts)&&(!_57f)){
if(!this.templateString.length){
this.templateString=ts["string"];
}
if(!this.templateNode){
this.templateNode=ts["node"];
}
}
var _581=false;
var node=null;
var tstr=this.templateString;
if((!this.templateNode)&&(this.templateString)){
_581=this.templateString.match(/\$\{([^\}]+)\}/g);
if(_581){
var hash=this.strings||{};
for(var key in dojo.widget.defaultStrings){
if(dojo.lang.isUndefined(hash[key])){
hash[key]=dojo.widget.defaultStrings[key];
}
}
for(var i=0;i<_581.length;i++){
var key=_581[i];
key=key.substring(2,key.length-1);
var kval=(key.substring(0,5)=="this.")?dojo.lang.getObjPathValue(key.substring(5),this):hash[key];
var _588;
if((kval)||(dojo.lang.isString(kval))){
_588=(dojo.lang.isFunction(kval))?kval.call(this,key,this.templateString):kval;
tstr=tstr.replace(_581[i],_588);
}
}
}else{
this.templateNode=this.createNodesFromText(this.templateString,true)[0];
if(!_57f){
ts.node=this.templateNode;
}
}
}
if((!this.templateNode)&&(!_581)){
dojo.debug("weren't able to create template!");
return false;
}else{
if(!_581){
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
},attachTemplateNodes:function(_58a,_58b){
if(!_58b){
_58b=this;
}
return dojo.widget.attachTemplateNodes(_58a,_58b,dojo.widget.getDojoEventsFromStr(this.templateString));
},fillInTemplate:function(){
},destroyRendering:function(){
try{
delete this.domNode;
}
catch(e){
}
},cleanUp:function(){
},getContainerHeight:function(){
dojo.unimplemented("dojo.widget.DomWidget.getContainerHeight");
},getContainerWidth:function(){
dojo.unimplemented("dojo.widget.DomWidget.getContainerWidth");
},createNodesFromText:function(){
dojo.unimplemented("dojo.widget.DomWidget.createNodesFromText");
}});
dojo.require("dojo.html");
dojo.provide("dojo.html.extras");
dojo.require("dojo.string.extras");
dojo.html.gravity=function(node,e){
node=dojo.byId(node);
var _58e=dojo.html.getCursorPosition(e);
with(dojo.html){
var _58f=getAbsoluteX(node,true)+(getInnerWidth(node)/2);
var _590=getAbsoluteY(node,true)+(getInnerHeight(node)/2);
}
with(dojo.html.gravity){
return ((_58e.x<_58f?WEST:EAST)|(_58e.y<_590?NORTH:SOUTH));
}
};
dojo.html.gravity.NORTH=1;
dojo.html.gravity.SOUTH=1<<1;
dojo.html.gravity.EAST=1<<2;
dojo.html.gravity.WEST=1<<3;
dojo.html.renderedTextContent=function(node){
node=dojo.byId(node);
var _592="";
if(node==null){
return _592;
}
for(var i=0;i<node.childNodes.length;i++){
switch(node.childNodes[i].nodeType){
case 1:
case 5:
var _594="unknown";
try{
_594=dojo.style.getStyle(node.childNodes[i],"display");
}
catch(E){
}
switch(_594){
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
_592+="\n";
_592+=dojo.html.renderedTextContent(node.childNodes[i]);
_592+="\n";
break;
case "none":
break;
default:
if(node.childNodes[i].tagName&&node.childNodes[i].tagName.toLowerCase()=="br"){
_592+="\n";
}else{
_592+=dojo.html.renderedTextContent(node.childNodes[i]);
}
break;
}
break;
case 3:
case 2:
case 4:
var text=node.childNodes[i].nodeValue;
var _596="unknown";
try{
_596=dojo.style.getStyle(node,"text-transform");
}
catch(E){
}
switch(_596){
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
switch(_596){
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
if(/\s$/.test(_592)){
text.replace(/^\s/,"");
}
break;
}
_592+=text;
break;
default:
break;
}
}
return _592;
};
dojo.html.createNodesFromText=function(txt,trim){
if(trim){
txt=dojo.string.trim(txt);
}
var tn=document.createElement("div");
tn.style.visibility="hidden";
document.body.appendChild(tn);
var _59a="none";
if((/^<t[dh][\s\r\n>]/i).test(dojo.string.trimStart(txt))){
txt="<table><tbody><tr>"+txt+"</tr></tbody></table>";
_59a="cell";
}else{
if((/^<tr[\s\r\n>]/i).test(dojo.string.trimStart(txt))){
txt="<table><tbody>"+txt+"</tbody></table>";
_59a="row";
}else{
if((/^<(thead|tbody|tfoot)[\s\r\n>]/i).test(dojo.string.trimStart(txt))){
txt="<table>"+txt+"</table>";
_59a="section";
}
}
}
tn.innerHTML=txt;
if(tn["normalize"]){
tn.normalize();
}
var _59b=null;
switch(_59a){
case "cell":
_59b=tn.getElementsByTagName("tr")[0];
break;
case "row":
_59b=tn.getElementsByTagName("tbody")[0];
break;
case "section":
_59b=tn.getElementsByTagName("table")[0];
break;
default:
_59b=tn;
break;
}
var _59c=[];
for(var x=0;x<_59b.childNodes.length;x++){
_59c.push(_59b.childNodes[x].cloneNode(true));
}
tn.style.display="none";
document.body.removeChild(tn);
return _59c;
};
dojo.html.placeOnScreen=function(node,_59f,_5a0,_5a1,_5a2){
if(dojo.lang.isArray(_59f)){
_5a2=_5a1;
_5a1=_5a0;
_5a0=_59f[1];
_59f=_59f[0];
}
if(!isNaN(_5a1)){
_5a1=[Number(_5a1),Number(_5a1)];
}else{
if(!dojo.lang.isArray(_5a1)){
_5a1=[0,0];
}
}
var _5a3=dojo.html.getScrollOffset();
var view=dojo.html.getViewportSize();
node=dojo.byId(node);
var w=node.offsetWidth+_5a1[0];
var h=node.offsetHeight+_5a1[1];
if(_5a2){
_59f-=_5a3.x;
_5a0-=_5a3.y;
}
var x=_59f+w;
if(x>view.w){
x=view.w-w;
}else{
x=_59f;
}
x=Math.max(_5a1[0],x)+_5a3.x;
var y=_5a0+h;
if(y>view.h){
y=view.h-h;
}else{
y=_5a0;
}
y=Math.max(_5a1[1],y)+_5a3.y;
node.style.left=x+"px";
node.style.top=y+"px";
var ret=[x,y];
ret.x=x;
ret.y=y;
return ret;
};
dojo.html.placeOnScreenPoint=function(node,_5ab,_5ac,_5ad,_5ae){
if(dojo.lang.isArray(_5ab)){
_5ae=_5ad;
_5ad=_5ac;
_5ac=_5ab[1];
_5ab=_5ab[0];
}
if(!isNaN(_5ad)){
_5ad=[Number(_5ad),Number(_5ad)];
}else{
if(!dojo.lang.isArray(_5ad)){
_5ad=[0,0];
}
}
var _5af=dojo.html.getScrollOffset();
var view=dojo.html.getViewportSize();
node=dojo.byId(node);
var _5b1=node.style.display;
node.style.display="";
var w=dojo.style.getInnerWidth(node);
var h=dojo.style.getInnerHeight(node);
node.style.display=_5b1;
if(_5ae){
_5ab-=_5af.x;
_5ac-=_5af.y;
}
var x=-1,y=-1;
if((_5ab+_5ad[0])+w<=view.w&&(_5ac+_5ad[1])+h<=view.h){
x=(_5ab+_5ad[0]);
y=(_5ac+_5ad[1]);
}
if((x<0||y<0)&&(_5ab-_5ad[0])<=view.w&&(_5ac+_5ad[1])+h<=view.h){
x=(_5ab-_5ad[0])-w;
y=(_5ac+_5ad[1]);
}
if((x<0||y<0)&&(_5ab+_5ad[0])+w<=view.w&&(_5ac-_5ad[1])<=view.h){
x=(_5ab+_5ad[0]);
y=(_5ac-_5ad[1])-h;
}
if((x<0||y<0)&&(_5ab-_5ad[0])<=view.w&&(_5ac-_5ad[1])<=view.h){
x=(_5ab-_5ad[0])-w;
y=(_5ac-_5ad[1])-h;
}
if(x<0||y<0||(x+w>view.w)||(y+h>view.h)){
return dojo.html.placeOnScreen(node,_5ab,_5ac,_5ad,_5ae);
}
x+=_5af.x;
y+=_5af.y;
node.style.left=x+"px";
node.style.top=y+"px";
var ret=[x,y];
ret.x=x;
ret.y=y;
return ret;
};
dojo.html.BackgroundIframe=function(node){
if(dojo.render.html.ie){
var html="<iframe "+"style='position: absolute; left: 0px; top: 0px; width: 100%; height: 100%;"+"z-index: -1; filter:Alpha(Opacity=\"0\");' "+">";
this.iframe=document.createElement(html);
if(node){
node.appendChild(this.iframe);
this.domNode=node;
}else{
document.body.appendChild(this.iframe);
this.iframe.style.display="none";
}
}
};
dojo.lang.extend(dojo.html.BackgroundIframe,{iframe:null,onResized:function(){
if(this.iframe&&this.domNode){
var w=dojo.style.getOuterWidth(this.domNode);
var h=dojo.style.getOuterHeight(this.domNode);
if(w==0||h==0){
dojo.lang.setTimeout(this,this.onResized,50);
return;
}
var s=this.iframe.style;
s.width=w+"px";
s.height=h+"px";
}
},size:function(node){
if(!this.iframe){
return;
}
var _5bc=dojo.style.toCoordinateArray(node,true);
var s=this.iframe.style;
s.width=_5bc.w+"px";
s.height=_5bc.h+"px";
s.left=_5bc.x+"px";
s.top=_5bc.y+"px";
},setZIndex:function(node){
if(!this.iframe){
return;
}
if(dojo.dom.isNode(node)){
this.iframe.style.zIndex=dojo.html.getStyle(node,"z-index")-1;
}else{
if(!isNaN(node)){
this.iframe.style.zIndex=node;
}
}
},show:function(){
if(!this.iframe){
return;
}
this.iframe.style.display="block";
},hide:function(){
if(!this.ie){
return;
}
var s=this.iframe.style;
s.display="none";
},remove:function(){
dojo.dom.removeNode(this.iframe);
}});
dojo.provide("dojo.lfx.Animation");
dojo.provide("dojo.lfx.Line");
dojo.require("dojo.lang.func");
dojo.lfx.Line=function(_5c0,end){
this.start=_5c0;
this.end=end;
if(dojo.lang.isArray(_5c0)){
var diff=[];
dojo.lang.forEach(this.start,function(s,i){
diff[i]=this.end[i]-s;
},this);
this.getValue=function(n){
var res=[];
dojo.lang.forEach(this.start,function(s,i){
res[i]=(diff[i]*n)+s;
},this);
return res;
};
}else{
var diff=end-_5c0;
this.getValue=function(n){
return (diff*n)+this.start;
};
}
};
dojo.lfx.easeIn=function(n){
return Math.pow(n,3);
};
dojo.lfx.easeOut=function(n){
return (1-Math.pow(1-n,3));
};
dojo.lfx.easeInOut=function(n){
return ((3*Math.pow(n,2))-(2*Math.pow(n,3)));
};
dojo.lfx.IAnimation=function(){
};
dojo.lang.extend(dojo.lfx.IAnimation,{curve:null,duration:1000,easing:null,repeatCount:0,rate:25,handler:null,beforeBegin:null,onBegin:null,onAnimate:null,onEnd:null,onPlay:null,onPause:null,onStop:null,play:null,pause:null,stop:null,fire:function(evt,args){
if(this[evt]){
this[evt].apply(this,(args||[]));
}
},_active:false,_paused:false});
dojo.lfx.Animation=function(_5cf,_5d0,_5d1,_5d2,_5d3,rate){
dojo.lfx.IAnimation.call(this);
if(dojo.lang.isNumber(_5cf)||(!_5cf&&_5d0.getValue)){
rate=_5d3;
_5d3=_5d2;
_5d2=_5d1;
_5d1=_5d0;
_5d0=_5cf;
_5cf=null;
}else{
if(_5cf.getValue||dojo.lang.isArray(_5cf)){
rate=_5d2;
_5d3=_5d1;
_5d2=_5d0;
_5d1=_5cf;
_5d0=null;
_5cf=null;
}
}
if(dojo.lang.isArray(_5d1)){
this.curve=new dojo.lfx.Line(_5d1[0],_5d1[1]);
}else{
this.curve=_5d1;
}
if(_5d0!=null&&_5d0>0){
this.duration=_5d0;
}
if(_5d3){
this.repeatCount=_5d3;
}
if(rate){
this.rate=rate;
}
if(_5cf){
this.handler=_5cf.handler;
this.beforeBegin=_5cf.beforeBegin;
this.onBegin=_5cf.onBegin;
this.onEnd=_5cf.onEnd;
this.onPlay=_5cf.onPlay;
this.onPause=_5cf.onPause;
this.onStop=_5cf.onStop;
this.onAnimate=_5cf.onAnimate;
}
if(_5d2&&dojo.lang.isFunction(_5d2)){
this.easing=_5d2;
}
};
dojo.inherits(dojo.lfx.Animation,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Animation,{_startTime:null,_endTime:null,_timer:null,_percent:0,_startRepeatCount:0,play:function(_5d5,_5d6){
if(_5d6){
clearTimeout(this._timer);
this._active=false;
this._paused=false;
this._percent=0;
}else{
if(this._active&&!this._paused){
return;
}
}
this.fire("handler",["beforeBegin"]);
this.fire("beforeBegin");
if(_5d5>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_5d6);
}),_5d5);
return;
}
this._startTime=new Date().valueOf();
if(this._paused){
this._startTime-=(this.duration*this._percent/100);
}
this._endTime=this._startTime+this.duration;
this._active=true;
this._paused=false;
var step=this._percent/100;
var _5d8=this.curve.getValue(step);
if(this._percent==0){
if(!this._startRepeatCount){
this._startRepeatCount=this.repeatCount;
}
this.fire("handler",["begin",_5d8]);
this.fire("onBegin",[_5d8]);
}
this.fire("handler",["play",_5d8]);
this.fire("onPlay",[_5d8]);
this._cycle();
},pause:function(){
clearTimeout(this._timer);
if(!this._active){
return;
}
this._paused=true;
var _5d9=this.curve.getValue(this._percent/100);
this.fire("handler",["pause",_5d9]);
this.fire("onPause",[_5d9]);
},gotoPercent:function(pct,_5db){
clearTimeout(this._timer);
this._active=true;
this._paused=true;
this._percent=pct;
if(_5db){
this.play();
}
},stop:function(_5dc){
clearTimeout(this._timer);
var step=this._percent/100;
if(_5dc){
step=1;
}
var _5de=this.curve.getValue(step);
this.fire("handler",["stop",_5de]);
this.fire("onStop",[_5de]);
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
if(step>=1){
step=1;
this._percent=100;
}else{
this._percent=step*100;
}
if(this.easing&&dojo.lang.isFunction(this.easing)){
step=this.easing(step);
}
var _5e1=this.curve.getValue(step);
this.fire("handler",["animate",_5e1]);
this.fire("onAnimate",[_5e1]);
if(step<1){
this._timer=setTimeout(dojo.lang.hitch(this,"_cycle"),this.rate);
}else{
this._active=false;
this.fire("handler",["end"]);
this.fire("onEnd");
if(this.repeatCount>0){
this.repeatCount--;
this.play(null,true);
}else{
if(this.repeatCount==-1){
this.play(null,true);
}else{
if(this._startRepeatCount){
this.repeatCount=this._startRepeatCount;
this._startRepeatCount=0;
}
}
}
}
}
}});
dojo.lfx.Combine=function(){
dojo.lfx.IAnimation.call(this);
this._anims=[];
this._animsEnded=0;
var _5e2=arguments;
if(_5e2.length==1&&(dojo.lang.isArray(_5e2[0])||dojo.lang.isArrayLike(_5e2[0]))){
_5e2=_5e2[0];
}
var _5e3=this;
dojo.lang.forEach(_5e2,function(anim){
_5e3._anims.push(anim);
dojo.event.connect(anim,"onEnd",function(){
_5e3._onAnimsEnded();
});
});
};
dojo.inherits(dojo.lfx.Combine,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Combine,{_animsEnded:0,play:function(_5e5,_5e6){
if(!this._anims.length){
return;
}
this.fire("beforeBegin");
if(_5e5>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_5e6);
}),_5e5);
return;
}
if(_5e6||this._anims[0].percent==0){
this.fire("onBegin");
}
this.fire("onPlay");
this._animsCall("play",null,_5e6);
},pause:function(){
this.fire("onPause");
this._animsCall("pause");
},stop:function(_5e7){
this.fire("onStop");
this._animsCall("stop",_5e7);
},_onAnimsEnded:function(){
this._animsEnded++;
if(this._animsEnded>=this._anims.length){
this.fire("onEnd");
}
},_animsCall:function(_5e8){
var args=[];
if(arguments.length>1){
for(var i=1;i<arguments.length;i++){
args.push(arguments[i]);
}
}
var _5eb=this;
dojo.lang.forEach(this._anims,function(anim){
anim[_5e8](args);
},_5eb);
}});
dojo.lfx.Chain=function(){
dojo.lfx.IAnimation.call(this);
this._anims=[];
this._currAnim=-1;
var _5ed=arguments;
if(_5ed.length==1&&(dojo.lang.isArray(_5ed[0])||dojo.lang.isArrayLike(_5ed[0]))){
_5ed=_5ed[0];
}
var _5ee=this;
dojo.lang.forEach(_5ed,function(anim,i,_5f1){
_5ee._anims.push(anim);
if(i<_5f1.length-1){
dojo.event.connect(anim,"onEnd",function(){
_5ee._playNext();
});
}else{
dojo.event.connect(anim,"onEnd",function(){
_5ee.fire("onEnd");
});
}
},_5ee);
};
dojo.inherits(dojo.lfx.Chain,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Chain,{_currAnim:-1,play:function(_5f2,_5f3){
if(!this._anims.length){
return;
}
if(_5f3||!this._anims[this._currAnim]){
this._currAnim=0;
}
this.fire("beforeBegin");
if(_5f2>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_5f3);
}),_5f2);
return;
}
if(this._anims[this._currAnim]){
if(this._currAnim==0){
this.fire("handler",["begin",this._currAnim]);
this.fire("onBegin",[this._currAnim]);
}
this.fire("onPlay",[this._currAnim]);
this._anims[this._currAnim].play(null,_5f3);
}
},pause:function(){
if(this._anims[this._currAnim]){
this._anims[this._currAnim].pause();
this.fire("onPause",[this._currAnim]);
}
},playPause:function(){
if(this._anims.length==0){
return;
}
if(this._currAnim==-1){
this._currAnim=0;
}
var _5f4=this._anims[this._currAnim];
if(_5f4){
if(!_5f4._active||_5f4._paused){
this.play();
}else{
this.pause();
}
}
},stop:function(){
if(this._anims[this._currAnim]){
this._anims[this._currAnim].stop();
this.fire("onStop",[this._currAnim]);
}
},_playNext:function(){
if(this._currAnim==-1||this._anims.length==0){
return;
}
this._currAnim++;
if(this._anims[this._currAnim]){
this._anims[this._currAnim].play(null,true);
}
}});
dojo.lfx.combine=function(){
var _5f5=arguments;
if(dojo.lang.isArray(arguments[0])){
_5f5=arguments[0];
}
return new dojo.lfx.Combine(_5f5);
};
dojo.lfx.chain=function(){
var _5f6=arguments;
if(dojo.lang.isArray(arguments[0])){
_5f6=arguments[0];
}
return new dojo.lfx.Chain(_5f6);
};
dojo.provide("dojo.lfx.html");
dojo.require("dojo.lfx.Animation");
dojo.require("dojo.html");
dojo.require("dojo.event");
dojo.require("dojo.lang.func");
dojo.lfx.html._byId=function(_5f7){
if(dojo.lang.isArrayLike(_5f7)){
if(!_5f7.alreadyChecked){
var n=[];
dojo.lang.forEach(_5f7,function(node){
n.push(dojo.byId(node));
});
n.alreadyChecked=true;
return n;
}else{
return _5f7;
}
}else{
var n=[];
n.push(dojo.byId(_5f7));
n.alreadyChecked=true;
return n;
}
};
dojo.lfx.html.propertyAnimation=function(_5fa,_5fb,_5fc,_5fd){
_5fa=dojo.lfx.html._byId(_5fa);
if(_5fa.length==1){
dojo.lang.forEach(_5fb,function(prop){
if(typeof prop["start"]=="undefined"){
if(prop.property!="opacity"){
prop.start=parseInt(dojo.style.getComputedStyle(_5fa[0],prop.property));
}else{
prop.start=dojo.style.getOpacity(_5fa[0]);
}
}
});
}
var _5ff=function(_600){
var _601=new Array(_600.length);
for(var i=0;i<_600.length;i++){
_601[i]=Math.round(_600[i]);
}
return _601;
};
var _603=function(n,_605){
n=dojo.byId(n);
if(!n||!n.style){
return;
}
for(var s in _605){
if(s=="opacity"){
dojo.style.setOpacity(n,_605[s]);
}else{
n.style[s]=_605[s];
}
}
};
var _607=function(_608){
this._properties=_608;
this.diffs=new Array(_608.length);
dojo.lang.forEach(_608,function(prop,i){
if(dojo.lang.isArray(prop.start)){
this.diffs[i]=null;
}else{
if(prop.start instanceof dojo.graphics.color.Color){
prop.startRgb=prop.start.toRgb();
prop.endRgb=prop.end.toRgb();
}else{
this.diffs[i]=prop.end-prop.start;
}
}
},this);
this.getValue=function(n){
var ret={};
dojo.lang.forEach(this._properties,function(prop,i){
var _60f=null;
if(dojo.lang.isArray(prop.start)){
}else{
if(prop.start instanceof dojo.graphics.color.Color){
_60f=(prop.units||"rgb")+"(";
for(var j=0;j<prop.startRgb.length;j++){
_60f+=Math.round(((prop.endRgb[j]-prop.startRgb[j])*n)+prop.startRgb[j])+(j<prop.startRgb.length-1?",":"");
}
_60f+=")";
}else{
_60f=((this.diffs[i])*n)+prop.start+(prop.property!="opacity"?prop.units||"px":"");
}
}
ret[dojo.style.toCamelCase(prop.property)]=_60f;
},this);
return ret;
};
};
var anim=new dojo.lfx.Animation(_5fc,new _607(_5fb),_5fd);
dojo.event.connect(anim,"onAnimate",function(_612){
dojo.lang.forEach(_5fa,function(node){
_603(node,_612);
});
});
return anim;
};
dojo.lfx.html._makeFadeable=function(_614){
var _615=function(node){
if(dojo.render.html.ie){
if((node.style.zoom.length==0)&&(dojo.style.getStyle(node,"zoom")=="normal")){
node.style.zoom="1";
}
if((node.style.width.length==0)&&(dojo.style.getStyle(node,"width")=="auto")){
node.style.width="auto";
}
}
};
if(dojo.lang.isArrayLike(_614)){
dojo.lang.forEach(_614,_615);
}else{
_615(_614);
}
};
dojo.lfx.html.fadeIn=function(_617,_618,_619,_61a){
_617=dojo.lfx.html._byId(_617);
dojo.lfx.html._makeFadeable(_617);
var anim=dojo.lfx.propertyAnimation(_617,[{property:"opacity",start:dojo.style.getOpacity(_617[0]),end:1}],_618,_619);
if(_61a){
dojo.event.connect(anim,"onEnd",function(){
_61a(_617,anim);
});
}
return anim;
};
dojo.lfx.html.fadeOut=function(_61c,_61d,_61e,_61f){
_61c=dojo.lfx.html._byId(_61c);
dojo.lfx.html._makeFadeable(_61c);
var anim=dojo.lfx.propertyAnimation(_61c,[{property:"opacity",start:dojo.style.getOpacity(_61c[0]),end:0}],_61d,_61e);
if(_61f){
dojo.event.connect(anim,"onEnd",function(){
_61f(_61c,anim);
});
}
return anim;
};
dojo.lfx.html.fadeShow=function(_621,_622,_623,_624){
var anim=dojo.lfx.html.fadeIn(_621,_622,_623,_624);
dojo.event.connect(anim,"beforeBegin",function(){
if(dojo.lang.isArrayLike(_621)){
dojo.lang.forEach(_621,dojo.style.show);
}else{
dojo.style.show(_621);
}
});
return anim;
};
dojo.lfx.html.fadeHide=function(_626,_627,_628,_629){
var anim=dojo.lfx.html.fadeOut(_626,_627,_628,function(){
if(dojo.lang.isArrayLike(_626)){
dojo.lang.forEach(_626,dojo.style.hide);
}else{
dojo.style.hide(_626);
}
if(_629){
_629(_626,anim);
}
});
return anim;
};
dojo.lfx.html.wipeIn=function(_62b,_62c,_62d,_62e){
_62b=dojo.lfx.html._byId(_62b);
var _62f=[];
dojo.lang.forEach(_62b,function(node){
var _631=dojo.style.getStyle(node,"overflow");
if(_631=="visible"){
node.style.overflow="hidden";
}
dojo.style.show(node);
node.style.height=0;
var anim=dojo.lfx.propertyAnimation(node,[{property:"height",start:0,end:node.scrollHeight}],_62c,_62d);
dojo.event.connect(anim,"onEnd",function(){
node.style.overflow=_631;
node.style.height="auto";
if(_62e){
_62e(node,anim);
}
});
_62f.push(anim);
});
if(_62b.length>1){
return dojo.lfx.combine(_62f);
}else{
return _62f[0];
}
};
dojo.lfx.html.wipeOut=function(_633,_634,_635,_636){
_633=dojo.lfx.html._byId(_633);
var _637=[];
dojo.lang.forEach(_633,function(node){
var _639=dojo.style.getStyle(node,"overflow");
if(_639=="visible"){
node.style.overflow="hidden";
}
dojo.style.show(node);
var anim=dojo.lfx.propertyAnimation(node,[{property:"height",start:dojo.style.getContentBoxHeight(node),end:0}],_634,_635);
dojo.event.connect(anim,"onEnd",function(){
dojo.style.hide(node);
node.style.overflow=_639;
if(_636){
_636(node,anim);
}
});
_637.push(anim);
});
if(_633.length>1){
return dojo.lfx.combine(_637);
}else{
return _637[0];
}
};
dojo.lfx.html.slideTo=function(_63b,_63c,_63d,_63e,_63f){
_63b=dojo.lfx.html._byId(_63b);
var _640=[];
dojo.lang.forEach(_63b,function(node){
var top=null;
var left=null;
var pos=null;
var init=(function(){
var _646=node;
return function(){
top=_646.offsetTop;
left=_646.offsetLeft;
pos=dojo.style.getComputedStyle(_646,"position");
if(pos=="relative"||pos=="static"){
top=parseInt(dojo.style.getComputedStyle(_646,"top"))||0;
left=parseInt(dojo.style.getComputedStyle(_646,"left"))||0;
}
};
})();
init();
var anim=dojo.lfx.propertyAnimation(node,[{property:"top",start:top,end:_63c[0]},{property:"left",start:left,end:_63c[1]}],_63d,_63e);
dojo.event.connect(anim,"beforeBegin",init);
if(_63f){
dojo.event.connect(anim,"onEnd",function(){
_63f(node,anim);
});
}
_640.push(anim);
});
if(_63b.length>1){
return dojo.lfx.combine(_640);
}else{
return _640[0];
}
};
dojo.lfx.html.slideBy=function(_648,_649,_64a,_64b,_64c){
_648=dojo.lfx.html._byId(_648);
var _64d=[];
dojo.lang.forEach(_648,function(node){
var top=null;
var left=null;
var pos=null;
var init=(function(){
var _653=node;
return function(){
top=node.offsetTop;
left=node.offsetLeft;
pos=dojo.style.getComputedStyle(node,"position");
if(pos=="relative"||pos=="static"){
top=parseInt(dojo.style.getComputedStyle(node,"top"))||0;
left=parseInt(dojo.style.getComputedStyle(node,"left"))||0;
}
};
})();
init();
var anim=dojo.lfx.propertyAnimation(node,[{property:"top",start:top,end:top+_649[0]},{property:"left",start:left,end:left+_649[1]}],_64a,_64b);
dojo.event.connect(anim,"beforeBegin",init);
if(_64c){
dojo.event.connect(anim,"onEnd",function(){
_64c(node,anim);
});
}
_64d.push(anim);
});
if(_648.length>1){
return dojo.lfx.combine(_64d);
}else{
return _64d[0];
}
};
dojo.lfx.html.explode=function(_655,_656,_657,_658,_659){
_655=dojo.byId(_655);
_656=dojo.byId(_656);
var _65a=dojo.style.toCoordinateArray(_655);
var _65b=document.createElement("div");
with(_65b.style){
position="absolute";
var srgb;
try{
srgb=dojo.style.getBackgroundColor(_656);
if(srgb.length>3){
srgb.pop();
}
srgb="rgb("+srgb+")";
}
catch(e){
srgb="#9f9fa0";
}
backgroundColor=srgb;
display="none";
}
document.body.appendChild(_65b);
with(_656.style){
visibility="hidden";
display="block";
}
var _65d=dojo.style.toCoordinateArray(_656);
with(_656.style){
display="none";
visibility="visible";
}
var anim=new dojo.lfx.propertyAnimation(_65b,[{property:"height",start:_65a[3],end:_65d[3]},{property:"width",start:_65a[2],end:_65d[2]},{property:"top",start:_65a[1],end:_65d[1]},{property:"left",start:_65a[0],end:_65d[0]},{property:"opacity",start:0.3,end:1}],_657,_658);
anim.beforeBegin=function(){
dojo.style.setDisplay(_65b,"block");
};
anim.onEnd=function(){
dojo.style.setDisplay(_656,"block");
_65b.parentNode.removeChild(_65b);
};
if(_659){
dojo.event.connect(anim,"onEnd",function(){
_659(_656,anim);
});
}
return anim;
};
dojo.lfx.html.implode=function(_65f,end,_661,_662,_663){
_65f=dojo.byId(_65f);
end=dojo.byId(end);
var _664=dojo.style.toCoordinateArray(_65f);
var _665=dojo.style.toCoordinateArray(end);
var _666=document.createElement("div");
dojo.style.setOpacity(_666,0.3);
with(_666.style){
position="absolute";
var srgb;
try{
srgb=dojo.style.getBackgroundColor(_65f);
if(srgb.length>3){
srgb.pop();
}
srgb="rgb("+srgb+")";
}
catch(e){
srgb="#9f9fa0";
}
backgroundColor=srgb;
display="none";
}
document.body.appendChild(_666);
var anim=new dojo.lfx.propertyAnimation(_666,[{property:"height",start:_664[3],end:_665[3]},{property:"width",start:_664[2],end:_665[2]},{property:"top",start:_664[1],end:_665[1]},{property:"left",start:_664[0],end:_665[0]},{property:"opacity",start:1,end:0.3}],_661,_662);
anim.beforeBegin=function(){
dojo.style.hide(_65f);
dojo.style.show(_666);
};
anim.onEnd=function(){
_666.parentNode.removeChild(_666);
};
if(_663){
dojo.event.connect(anim,"onEnd",function(){
_663(_65f,anim);
});
}
return anim;
};
dojo.lfx.html.highlight=function(_669,_66a,_66b,_66c,_66d){
_669=dojo.lfx.html._byId(_669);
var _66e=[];
dojo.lang.forEach(_669,function(node){
var _670=dojo.style.getBackgroundColor(node);
var bg=dojo.style.getStyle(node,"background-color").toLowerCase();
var _672=dojo.style.getStyle(node,"background-image");
var _673=(bg=="transparent"||bg=="rgba(0, 0, 0, 0)");
while(_670.length>3){
_670.pop();
}
var rgb=new dojo.graphics.color.Color(_66a);
var _675=new dojo.graphics.color.Color(_670);
var anim=dojo.lfx.propertyAnimation(node,[{property:"background-color",start:rgb,end:_675}],_66b,_66c);
dojo.event.connect(anim,"beforeBegin",function(){
if(_672){
node.style.backgroundImage="none";
}
node.style.backgroundColor="rgb("+rgb.toRgb().join(",")+")";
});
dojo.event.connect(anim,"onEnd",function(){
if(_672){
node.style.backgroundImage=_672;
}
if(_673){
node.style.backgroundColor="transparent";
}
if(_66d){
_66d(node,anim);
}
});
_66e.push(anim);
});
if(_669.length>1){
return dojo.lfx.combine(_66e);
}else{
return _66e[0];
}
};
dojo.lfx.html.unhighlight=function(_677,_678,_679,_67a,_67b){
_677=dojo.lfx.html._byId(_677);
var _67c=[];
dojo.lang.forEach(_677,function(node){
var _67e=new dojo.graphics.color.Color(dojo.style.getBackgroundColor(node));
var rgb=new dojo.graphics.color.Color(_678);
var _680=dojo.style.getStyle(node,"background-image");
var anim=dojo.lfx.propertyAnimation(node,[{property:"background-color",start:_67e,end:rgb}],_679,_67a);
dojo.event.connect(anim,"beforeBegin",function(){
if(_680){
node.style.backgroundImage="none";
}
node.style.backgroundColor="rgb("+_67e.toRgb().join(",")+")";
});
dojo.event.connect(anim,"onEnd",function(){
if(_67b){
_67b(node,anim);
}
});
_67c.push(anim);
});
if(_677.length>1){
return dojo.lfx.combine(_67c);
}else{
return _67c[0];
}
};
dojo.lang.mixin(dojo.lfx,dojo.lfx.html);
dojo.kwCompoundRequire({browser:["dojo.lfx.html"],dashboard:["dojo.lfx.html"]});
dojo.provide("dojo.lfx.*");
dojo.provide("dojo.lfx.toggle");
dojo.require("dojo.lfx.*");
dojo.lfx.toggle.plain={show:function(node,_683,_684,_685){
dojo.style.show(node);
if(dojo.lang.isFunction(_685)){
_685();
}
},hide:function(node,_687,_688,_689){
dojo.style.hide(node);
if(dojo.lang.isFunction(_689)){
_689();
}
}};
dojo.lfx.toggle.fade={show:function(node,_68b,_68c,_68d){
dojo.lfx.fadeShow(node,_68b,_68c,_68d).play();
},hide:function(node,_68f,_690,_691){
dojo.lfx.fadeHide(node,_68f,_690,_691).play();
}};
dojo.lfx.toggle.wipe={show:function(node,_693,_694,_695){
dojo.lfx.wipeIn(node,_693,_694,_695).play();
},hide:function(node,_697,_698,_699){
dojo.lfx.wipeOut(node,_697,_698,_699).play();
}};
dojo.lfx.toggle.explode={show:function(node,_69b,_69c,_69d,_69e){
dojo.lfx.explode(_69e||[0,0,0,0],node,_69b,_69c,_69d).play();
},hide:function(node,_6a0,_6a1,_6a2,_6a3){
dojo.lfx.implode(node,_6a3||[0,0,0,0],_6a0,_6a1,_6a2).play();
}};
dojo.provide("dojo.widget.HtmlWidget");
dojo.require("dojo.widget.DomWidget");
dojo.require("dojo.html");
dojo.require("dojo.html.extras");
dojo.require("dojo.lang.extras");
dojo.require("dojo.lang.func");
dojo.require("dojo.lfx.toggle");
dojo.declare("dojo.widget.HtmlWidget",dojo.widget.DomWidget,{widgetType:"HtmlWidget",templateCssPath:null,templatePath:null,toggle:"plain",toggleDuration:150,animationInProgress:false,initialize:function(args,frag){
},postMixInProperties:function(args,frag){
this.toggleObj=dojo.lfx.toggle[this.toggle.toLowerCase()]||dojo.lfx.toggle.plain;
},getContainerHeight:function(){
dojo.unimplemented("dojo.widget.HtmlWidget.getContainerHeight");
},getContainerWidth:function(){
return this.parent.domNode.offsetWidth;
},setNativeHeight:function(_6a8){
var ch=this.getContainerHeight();
},createNodesFromText:function(txt,wrap){
return dojo.html.createNodesFromText(txt,wrap);
},destroyRendering:function(_6ac){
try{
if(!_6ac){
dojo.event.browser.clean(this.domNode);
}
this.domNode.parentNode.removeChild(this.domNode);
delete this.domNode;
}
catch(e){
}
},isShowing:function(){
return dojo.style.isShowing(this.domNode);
},toggleShowing:function(){
if(this.isHidden){
this.show();
}else{
this.hide();
}
},show:function(){
this.animationInProgress=true;
this.isHidden=false;
this.toggleObj.show(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onShow),this.explodeSrc);
},onShow:function(){
this.animationInProgress=false;
},hide:function(){
this.animationInProgress=true;
this.isHidden=true;
this.toggleObj.hide(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onHide),this.explodeSrc);
},onHide:function(){
this.animationInProgress=false;
},_isResized:function(w,h){
if(!this.isShowing()){
return false;
}
w=w||dojo.style.getOuterWidth(this.domNode);
h=h||dojo.style.getOuterHeight(this.domNode);
if(this.width==w&&this.height==h){
return false;
}
this.width=w;
this.height=h;
return true;
},onParentResized:function(){
if(!this._isResized()){
return;
}
this.onResized();
},resizeTo:function(w,h){
if(!this._isResized(w,h)){
return;
}
dojo.style.setOuterWidth(this.domNode,w);
dojo.style.setOuterHeight(this.domNode,h);
this.onResized();
},resizeSoon:function(){
if(this.isShowing()){
dojo.lang.setTimeout(this,this.onResized,0);
}
},onResized:function(){
dojo.lang.forEach(this.children,function(_6b1){
_6b1.onParentResized();
});
}});
dojo.kwCompoundRequire({common:["dojo.xml.Parse","dojo.widget.Widget","dojo.widget.Parse","dojo.widget.Manager"],browser:["dojo.widget.DomWidget","dojo.widget.HtmlWidget"],dashboard:["dojo.widget.DomWidget","dojo.widget.HtmlWidget"],svg:["dojo.widget.SvgWidget"],rhino:["dojo.widget.SwtWidget"]});
dojo.provide("dojo.widget.*");

