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

if(typeof dojo=="undefined"){
var dj_global=this;
function dj_undef(_1,_2){
if(_2==null){
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
dojo.version={major:0,minor:0,patch:0,flag:"dev",revision:Number("$Rev: 4189 $".match(/[0-9]+/)[0]),toString:function(){
with(dojo.version){
return major+"."+minor+"."+patch+flag+" ("+revision+")";
}
}};
dojo.evalProp=function(_3,_4,_5){
return (_4&&!dj_undef(_3,_4)?_4[_3]:(_5?(_4[_3]={}):undefined));
};
dojo.parseObjPath=function(_6,_7,_8){
var _9=(_7!=null?_7:dj_global);
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
var _f=dojo.parseObjPath(_d,dj_global,_e);
if(_f){
return dojo.evalProp(_f.prop,_f.obj,_e);
}
return null;
};
dojo.errorToString=function(_10){
if(!dj_undef("message",_10)){
return _10.message;
}else{
if(!dj_undef("description",_10)){
return _10.description;
}else{
return _10;
}
}
};
dojo.raise=function(_11,_12){
if(_12){
_11=_11+": "+dojo.errorToString(_12);
}
try{
dojo.hostenv.println("FATAL: "+_11);
}
catch(e){
}
throw Error(_11);
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
function dj_eval(_14){
return dj_global.eval?dj_global.eval(_14):eval(_14);
}
dojo.unimplemented=function(_15,_16){
var _17="'"+_15+"' not implemented";
if(_16!=null){
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
for(var _21 in _1f){
tmp[_21]=false;
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
},getTextStack:[],loadUriStack:[],loadedUris:[],post_load_:false,modulesLoadedListeners:[],unloadListeners:[],loadNotifying:false};
for(var _2c in _27){
dojo.hostenv[_2c]=_27[_2c];
}
})();
dojo.hostenv.loadPath=function(_2d,_2e,cb){
var uri;
if((_2d.charAt(0)=="/")||(_2d.match(/^\w+:/))){
uri=_2d;
}else{
uri=this.getBaseScriptUri()+_2d;
}
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
dojo.unloaded=function(){
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
dojo.hostenv.unloaded=function(){
var mll=this.unloadListeners;
while(mll.length){
(mll.pop())();
}
dojo.unloaded();
};
dojo.addOnLoad=function(obj,_3d){
var dh=dojo.hostenv;
if(arguments.length==1){
dh.modulesLoadedListeners.push(obj);
}else{
if(arguments.length>1){
dh.modulesLoadedListeners.push(function(){
obj[_3d]();
});
}
}
if(dh.post_load_&&dh.inFlightCount==0&&!dh.loadNotifying){
dh.callLoaded();
}
};
dojo.addOnUnload=function(obj,_40){
var dh=dojo.hostenv;
if(arguments.length==1){
dh.unloadListeners.push(obj);
}else{
if(arguments.length>1){
dh.unloadListeners.push(function(){
obj[_40]();
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
dojo.hostenv.getModuleSymbols=function(_42){
var _43=_42.split(".");
for(var i=_43.length-1;i>0;i--){
var _45=_43.slice(0,i).join(".");
var _46=this.getModulePrefix(_45);
if(_46!=_45){
_43.splice(0,i,_46);
break;
}
}
return _43;
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
var _4c=this.getModuleSymbols(_47);
var _4d=((_4c[0].charAt(0)!="/")&&(!_4c[0].match(/^\w+:/)));
var _4e=_4c[_4c.length-1];
var _4f=_47.split(".");
if(_4e=="*"){
_47=(_4f.slice(0,-1)).join(".");
while(_4c.length){
_4c.pop();
_4c.push(this.pkgFileName);
_4b=_4c.join("/")+".js";
if(_4d&&(_4b.charAt(0)=="/")){
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
_47=_4f.join(".");
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
if(_4d&&(_4b.charAt(0)=="/")){
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
if(!_49&&!this["isXDomain"]){
_4a=this.findModule(_47,false);
if(!_4a){
dojo.raise("symbol '"+_47+"' is not defined after loading '"+_4b+"'");
}
}
return _4a;
};
dojo.hostenv.startPackage=function(_51){
var _52=dojo.evalObjPath((_51.split(".").slice(0,-1)).join("."));
this.loaded_modules_[(new String(_51)).toLowerCase()]=_52;
var _53=_51.split(/\./);
if(_53[_53.length-1]=="*"){
_53.pop();
}
return dojo.evalObjPath(_53.join("."),true);
};
dojo.hostenv.findModule=function(_54,_55){
var lmn=(new String(_54)).toLowerCase();
if(this.loaded_modules_[lmn]){
return this.loaded_modules_[lmn];
}
var _57=dojo.evalObjPath(_54);
if((_54)&&(typeof _57!="undefined")&&(_57)){
this.loaded_modules_[lmn]=_57;
return _57;
}
if(_55){
dojo.raise("no loaded module named '"+_54+"'");
}
return null;
};
dojo.kwCompoundRequire=function(_58){
var _59=_58["common"]||[];
var _5a=(_58[dojo.hostenv.name_])?_59.concat(_58[dojo.hostenv.name_]||[]):_59.concat(_58["default"]||[]);
for(var x=0;x<_5a.length;x++){
var _5c=_5a[x];
if(_5c.constructor==Array){
dojo.hostenv.loadModule.apply(dojo.hostenv,_5c);
}else{
dojo.hostenv.loadModule(_5c);
}
}
};
dojo.require=function(){
dojo.hostenv.loadModule.apply(dojo.hostenv,arguments);
};
dojo.requireIf=function(){
if((arguments[0]===true)||(arguments[0]=="common")||(arguments[0]&&dojo.render[arguments[0]].capable)){
var _5d=[];
for(var i=1;i<arguments.length;i++){
_5d.push(arguments[i]);
}
dojo.require.apply(dojo,_5d);
}
};
dojo.requireAfterIf=dojo.requireIf;
dojo.provide=function(){
return dojo.hostenv.startPackage.apply(dojo.hostenv,arguments);
};
dojo.setModulePrefix=function(_5f,_60){
return dojo.hostenv.setModulePrefix(_5f,_60);
};
dojo.exists=function(obj,_62){
var p=_62.split(".");
for(var i=0;i<p.length;i++){
if(!(obj[p[i]])){
return false;
}
obj=obj[p[i]];
}
return true;
};
}
if(typeof window=="undefined"){
dojo.raise("no window object");
}
(function(){
if(djConfig.allowQueryConfig){
var _65=document.location.toString();
var _66=_65.split("?",2);
if(_66.length>1){
var _67=_66[1];
var _68=_67.split("&");
for(var x in _68){
var sp=_68[x].split("=");
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
var _6c=document.getElementsByTagName("script");
var _6d=/(__package__|dojo|bootstrap1)\.js([\?\.]|$)/i;
for(var i=0;i<_6c.length;i++){
var src=_6c[i].getAttribute("src");
if(!src){
continue;
}
var m=src.match(_6d);
if(m){
var _71=src.substring(0,m.index);
if(src.indexOf("bootstrap1")>-1){
_71+="../";
}
if(!this["djConfig"]){
djConfig={};
}
if(djConfig["baseScriptUri"]==""){
djConfig["baseScriptUri"]=_71;
}
if(djConfig["baseRelativePath"]==""){
djConfig["baseRelativePath"]=_71;
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
var _79=dua.indexOf("Gecko");
drh.mozilla=drh.moz=(_79>=0)&&(!drh.khtml);
if(drh.mozilla){
drh.geckoVersion=dua.substring(_79+6,_79+14);
}
drh.ie=(document.all)&&(!drh.opera);
drh.ie50=drh.ie&&dav.indexOf("MSIE 5.0")>=0;
drh.ie55=drh.ie&&dav.indexOf("MSIE 5.5")>=0;
drh.ie60=drh.ie&&dav.indexOf("MSIE 6.0")>=0;
drh.ie70=drh.ie&&dav.indexOf("MSIE 7.0")>=0;
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
dojo.hostenv._XMLHTTP_PROGIDS=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"];
dojo.hostenv.getXmlhttpObject=function(){
var _7a=null;
var _7b=null;
try{
_7a=new XMLHttpRequest();
}
catch(e){
}
if(!_7a){
for(var i=0;i<3;++i){
var _7d=dojo.hostenv._XMLHTTP_PROGIDS[i];
try{
_7a=new ActiveXObject(_7d);
}
catch(e){
_7b=e;
}
if(_7a){
dojo.hostenv._XMLHTTP_PROGIDS=[_7d];
break;
}
}
}
if(!_7a){
return dojo.raise("XMLHTTP not available",_7b);
}
return _7a;
};
dojo.hostenv.getText=function(uri,_7f,_80){
var _81=this.getXmlhttpObject();
if(_7f){
_81.onreadystatechange=function(){
if(4==_81.readyState){
if((!_81["status"])||((200<=_81.status)&&(300>_81.status))){
_7f(_81.responseText);
}
}
};
}
_81.open("GET",uri,_7f?true:false);
try{
_81.send(null);
if(_7f){
return null;
}
if((_81["status"])&&((200>_81.status)&&(300<=_81.status))){
throw Error("Unable to load "+uri+" status:"+_81.status);
}
}
catch(e){
if((_80)&&(!_7f)){
return null;
}else{
throw e;
}
}
return _81.responseText;
};
dojo.hostenv.defaultDebugContainerId="dojoDebug";
dojo.hostenv._println_buffer=[];
dojo.hostenv._println_safe=false;
dojo.hostenv.println=function(_82){
if(!dojo.hostenv._println_safe){
dojo.hostenv._println_buffer.push(_82);
}else{
try{
var _83=document.getElementById(djConfig.debugContainerId?djConfig.debugContainerId:dojo.hostenv.defaultDebugContainerId);
if(!_83){
_83=document.getElementsByTagName("body")[0]||document.body;
}
var div=document.createElement("div");
div.appendChild(document.createTextNode(_82));
_83.appendChild(div);
}
catch(e){
try{
document.write("<div>"+_82+"</div>");
}
catch(e2){
window.status=_82;
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
function dj_addNodeEvtHdlr(_85,_86,fp,_88){
var _89=_85["on"+_86]||function(){
};
_85["on"+_86]=function(){
fp.apply(_85,arguments);
_89.apply(_85,arguments);
};
return true;
}
dj_addNodeEvtHdlr(window,"load",function(){
if(arguments.callee.initialized){
return;
}
arguments.callee.initialized=true;
var _8a=function(){
if(dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
};
if(dojo.hostenv.inFlightCount==0){
_8a();
dojo.hostenv.modulesLoaded();
}else{
dojo.addOnLoad(_8a);
}
});
dj_addNodeEvtHdlr(window,"unload",function(){
dojo.hostenv.unloaded();
});
dojo.hostenv.makeWidgets=function(){
var _8b=[];
if(djConfig.searchIds&&djConfig.searchIds.length>0){
_8b=_8b.concat(djConfig.searchIds);
}
if(dojo.hostenv.searchIds&&dojo.hostenv.searchIds.length>0){
_8b=_8b.concat(dojo.hostenv.searchIds);
}
if((djConfig.parseWidgets)||(_8b.length>0)){
if(dojo.evalObjPath("dojo.widget.Parse")){
var _8c=new dojo.xml.Parse();
if(_8b.length>0){
for(var x=0;x<_8b.length;x++){
var _8e=document.getElementById(_8b[x]);
if(!_8e){
continue;
}
var _8f=_8c.parseElement(_8e,null,true);
dojo.widget.getParser().createComponents(_8f);
}
}else{
if(djConfig.parseWidgets){
var _8f=_8c.parseElement(document.getElementsByTagName("body")[0]||document.body,null,true);
dojo.widget.getParser().createComponents(_8f);
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
var _92=false;
var _93=false;
var _94=false;
if((typeof this["load"]=="function")&&((typeof this["Packages"]=="function")||(typeof this["Packages"]=="object"))){
_92=true;
}else{
if(typeof this["load"]=="function"){
_93=true;
}else{
if(window.widget){
_94=true;
}
}
}
var _95=[];
if((this["djConfig"])&&((djConfig["isDebug"])||(djConfig["debugAtAllCosts"]))){
_95.push("debug.js");
}
if((this["djConfig"])&&(djConfig["debugAtAllCosts"])&&(!_92)&&(!_94)){
_95.push("browser_debug.js");
}
if((this["djConfig"])&&(djConfig["compat"])){
_95.push("compat/"+djConfig["compat"]+".js");
}
var _96=djConfig["baseScriptUri"];
if((this["djConfig"])&&(djConfig["baseLoaderUri"])){
_96=djConfig["baseLoaderUri"];
}
for(var x=0;x<_95.length;x++){
var _98=_96+"src/"+_95[x];
if(_92||_93){
load(_98);
}else{
try{
document.write("<scr"+"ipt type='text/javascript' src='"+_98+"'></scr"+"ipt>");
}
catch(e){
var _99=document.createElement("script");
_99.src=_98;
document.getElementsByTagName("head")[0].appendChild(_99);
}
}
}
})();
dojo.fallback_locale="en";
dojo.normalizeLocale=function(_9a){
return _9a?_9a.toLowerCase():dojo.locale;
};
dojo.requireLocalization=function(_9b,_9c,_9d){
dojo.debug("EXPERIMENTAL: dojo.requireLocalization");
var _9e=dojo.hostenv.getModuleSymbols(_9b);
var _9f=_9e.concat("nls").join("/");
_9d=dojo.normalizeLocale(_9d);
var _a0=_9d.split("-");
var _a1=[];
for(var i=_a0.length;i>0;i--){
_a1.push(_a0.slice(0,i).join("-"));
}
if(_a1[_a1.length-1]!=dojo.fallback_locale){
_a1.push(dojo.fallback_locale);
}
var _a3=[_9b,"_nls",_9c].join(".");
var _a4=dojo.hostenv.startPackage(_a3);
dojo.hostenv.loaded_modules_[_a3]=_a4;
var _a5=false;
for(var i=_a1.length-1;i>=0;i--){
var loc=_a1[i];
var pkg=[_a3,loc].join(".");
var _a8=false;
if(!dojo.hostenv.findModule(pkg)){
dojo.hostenv.loaded_modules_[pkg]=null;
var _a9=[_9f,loc,_9c].join("/")+".js";
_a8=dojo.hostenv.loadPath(_a9,null,function(_aa){
_a4[loc]=_aa;
if(_a5){
for(var x in _a5){
if(!_a4[loc][x]){
_a4[loc][x]=_a5[x];
}
}
}
});
}else{
_a8=true;
}
if(_a8&&_a4[loc]){
_a5=_a4[loc];
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
dojo.string.repeat=function(str,_b2,_b3){
var out="";
for(var i=0;i<_b2;i++){
out+=str;
if(_b3&&i<_b2-1){
out+=_b3;
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
dojo.lang.mixin=function(obj,_c2){
var _c3={};
for(var x in _c2){
if(typeof _c3[x]=="undefined"||_c3[x]!=_c2[x]){
obj[x]=_c2[x];
}
}
if(dojo.render.html.ie&&dojo.lang.isFunction(_c2["toString"])&&_c2["toString"]!=obj["toString"]){
obj.toString=_c2.toString;
}
return obj;
};
dojo.lang.extend=function(_c5,_c6){
this.mixin(_c5.prototype,_c6);
};
dojo.lang.find=function(arr,val,_c9,_ca){
if(!dojo.lang.isArrayLike(arr)&&dojo.lang.isArrayLike(val)){
var a=arr;
arr=val;
val=a;
}
var _cc=dojo.lang.isString(arr);
if(_cc){
arr=arr.split("");
}
if(_ca){
var _cd=-1;
var i=arr.length-1;
var end=-1;
}else{
var _cd=1;
var i=0;
var end=arr.length;
}
if(_c9){
while(i!=end){
if(arr[i]===val){
return i;
}
i+=_cd;
}
}else{
while(i!=end){
if(arr[i]==val){
return i;
}
i+=_cd;
}
}
return -1;
};
dojo.lang.indexOf=dojo.lang.find;
dojo.lang.findLast=function(arr,val,_d2){
return dojo.lang.find(arr,val,_d2,true);
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
dojo.lang.setTimeout=function(_de,_df){
var _e0=window,argsStart=2;
if(!dojo.lang.isFunction(_de)){
_e0=_de;
_de=_df;
_df=arguments[2];
argsStart++;
}
if(dojo.lang.isString(_de)){
_de=_e0[_de];
}
var _e1=[];
for(var i=argsStart;i<arguments.length;i++){
_e1.push(arguments[i]);
}
return setTimeout(function(){
_de.apply(_e0,_e1);
},_df);
};
dojo.lang.getNameInObj=function(ns,_e4){
if(!ns){
ns=dj_global;
}
for(var x in ns){
if(ns[x]===_e4){
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
dojo.lang.getObjPathValue=function(_e9,_ea,_eb){
with(dojo.parseObjPath(_e9,_ea,_eb)){
return dojo.evalProp(prop,obj,_eb);
}
};
dojo.lang.setObjPathValue=function(_ec,_ed,_ee,_ef){
if(arguments.length<4){
_ef=true;
}
with(dojo.parseObjPath(_ec,_ee,_ef)){
if(obj&&(_ef||(prop in obj))){
obj[prop]=_ed;
}
}
};
dojo.provide("dojo.io.IO");
dojo.require("dojo.string");
dojo.require("dojo.lang.extras");
dojo.io.transports=[];
dojo.io.hdlrFuncNames=["load","error","timeout"];
dojo.io.Request=function(url,_f1,_f2,_f3){
if((arguments.length==1)&&(arguments[0].constructor==Object)){
this.fromKwArgs(arguments[0]);
}else{
this.url=url;
if(_f1){
this.mimetype=_f1;
}
if(_f2){
this.transport=_f2;
}
if(arguments.length>=4){
this.changeUrl=_f3;
}
}
};
dojo.lang.extend(dojo.io.Request,{url:"",mimetype:"text/plain",method:"GET",content:undefined,transport:undefined,changeUrl:undefined,formNode:undefined,sync:false,bindSuccess:false,useCache:false,preventCache:false,load:function(_f4,_f5,evt){
},error:function(_f7,_f8){
},timeout:function(_f9){
},handle:function(){
},timeoutSeconds:0,abort:function(){
},fromKwArgs:function(_fa){
if(_fa["url"]){
_fa.url=_fa.url.toString();
}
if(_fa["formNode"]){
_fa.formNode=dojo.byId(_fa.formNode);
}
if(!_fa["method"]&&_fa["formNode"]&&_fa["formNode"].method){
_fa.method=_fa["formNode"].method;
}
if(!_fa["handle"]&&_fa["handler"]){
_fa.handle=_fa.handler;
}
if(!_fa["load"]&&_fa["loaded"]){
_fa.load=_fa.loaded;
}
if(!_fa["changeUrl"]&&_fa["changeURL"]){
_fa.changeUrl=_fa.changeURL;
}
_fa.encoding=dojo.lang.firstValued(_fa["encoding"],djConfig["bindEncoding"],"");
_fa.sendTransport=dojo.lang.firstValued(_fa["sendTransport"],djConfig["ioSendTransport"],false);
var _fb=dojo.lang.isFunction;
for(var x=0;x<dojo.io.hdlrFuncNames.length;x++){
var fn=dojo.io.hdlrFuncNames[x];
if(_fb(_fa[fn])){
continue;
}
if(_fb(_fa["handle"])){
_fa[fn]=_fa.handle;
}
}
dojo.lang.mixin(this,_fa);
}});
dojo.io.Error=function(msg,_ff,num){
this.message=msg;
this.type=_ff||"unknown";
this.number=num||0;
};
dojo.io.transports.addTransport=function(name){
this.push(name);
this[name]=dojo.io[name];
};
dojo.io.bind=function(_102){
if(!(_102 instanceof dojo.io.Request)){
try{
_102=new dojo.io.Request(_102);
}
catch(e){
dojo.debug(e);
}
}
var _103="";
if(_102["transport"]){
_103=_102["transport"];
if(!this[_103]){
return _102;
}
}else{
for(var x=0;x<dojo.io.transports.length;x++){
var tmp=dojo.io.transports[x];
if((this[tmp])&&(this[tmp].canHandle(_102))){
_103=tmp;
}
}
if(_103==""){
return _102;
}
}
this[_103].bind(_102);
_102.bindSuccess=true;
return _102;
};
dojo.io.queueBind=function(_106){
if(!(_106 instanceof dojo.io.Request)){
try{
_106=new dojo.io.Request(_106);
}
catch(e){
dojo.debug(e);
}
}
var _107=_106.load;
_106.load=function(){
dojo.io._queueBindInFlight=false;
var ret=_107.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
var _109=_106.error;
_106.error=function(){
dojo.io._queueBindInFlight=false;
var ret=_109.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
dojo.io._bindQueue.push(_106);
dojo.io._dispatchNextQueueBind();
return _106;
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
dojo.io.argsFromMap=function(map,_10c,last){
var enc=/utf/i.test(_10c||"")?encodeURIComponent:dojo.string.encodeAscii;
var _10f=[];
var _110=new Object();
for(var name in map){
var _112=function(elt){
var val=enc(name)+"="+enc(elt);
_10f[(last==name)?"push":"unshift"](val);
};
if(!_110[name]){
var _115=map[name];
if(dojo.lang.isArray(_115)){
dojo.lang.forEach(_115,_112);
}else{
_112(_115);
}
}
}
return _10f.join("&");
};
dojo.io.setIFrameSrc=function(_116,src,_118){
try{
var r=dojo.render.html;
if(!_118){
if(r.safari){
_116.location=src;
}else{
frames[_116.name].location=src;
}
}else{
var idoc;
if(r.ie){
idoc=_116.contentWindow.document;
}else{
if(r.safari){
idoc=_116.document;
if(!idoc){
_116.location=src;
return;
}
}else{
idoc=_116.contentWindow;
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
var _11f=0;
for(var x in obj){
if(obj[x]&&(!tmp[x])){
_11f++;
break;
}
}
return (_11f==0);
}else{
if(dojo.lang.isArrayLike(obj)||dojo.lang.isString(obj)){
return obj.length==0;
}
}
};
dojo.lang.map=function(arr,obj,_123){
var _124=dojo.lang.isString(arr);
if(_124){
arr=arr.split("");
}
if(dojo.lang.isFunction(obj)&&(!_123)){
_123=obj;
obj=dj_global;
}else{
if(dojo.lang.isFunction(obj)&&_123){
var _125=obj;
obj=_123;
_123=_125;
}
}
if(Array.map){
var _126=Array.map(arr,_123,obj);
}else{
var _126=[];
for(var i=0;i<arr.length;++i){
_126.push(_123.call(obj,arr[i]));
}
}
if(_124){
return _126.join("");
}else{
return _126;
}
};
dojo.lang.forEach=function(_128,_129,_12a){
if(dojo.lang.isString(_128)){
_128=_128.split("");
}
if(Array.forEach){
Array.forEach(_128,_129,_12a);
}else{
if(!_12a){
_12a=dj_global;
}
for(var i=0,l=_128.length;i<l;i++){
_129.call(_12a,_128[i],i,_128);
}
}
};
dojo.lang._everyOrSome=function(_12c,arr,_12e,_12f){
if(dojo.lang.isString(arr)){
arr=arr.split("");
}
if(Array.every){
return Array[(_12c)?"every":"some"](arr,_12e,_12f);
}else{
if(!_12f){
_12f=dj_global;
}
for(var i=0,l=arr.length;i<l;i++){
var _131=_12e.call(_12f,arr[i],i,arr);
if((_12c)&&(!_131)){
return false;
}else{
if((!_12c)&&(_131)){
return true;
}
}
}
return (_12c)?true:false;
}
};
dojo.lang.every=function(arr,_133,_134){
return this._everyOrSome(true,arr,_133,_134);
};
dojo.lang.some=function(arr,_136,_137){
return this._everyOrSome(false,arr,_136,_137);
};
dojo.lang.filter=function(arr,_139,_13a){
var _13b=dojo.lang.isString(arr);
if(_13b){
arr=arr.split("");
}
if(Array.filter){
var _13c=Array.filter(arr,_139,_13a);
}else{
if(!_13a){
if(arguments.length>=3){
dojo.raise("thisObject doesn't exist!");
}
_13a=dj_global;
}
var _13c=[];
for(var i=0;i<arr.length;i++){
if(_139.call(_13a,arr[i],i,arr)){
_13c.push(arr[i]);
}
}
}
if(_13b){
return _13c.join("");
}else{
return _13c;
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
dojo.lang.toArray=function(_141,_142){
var _143=[];
for(var i=_142||0;i<_141.length;i++){
_143.push(_141[i]);
}
return _143;
};
dojo.provide("dojo.lang.func");
dojo.require("dojo.lang.common");
dojo.lang.hitch=function(_145,_146){
if(dojo.lang.isString(_146)){
var fcn=_145[_146];
}else{
var fcn=_146;
}
return function(){
return fcn.apply(_145,arguments);
};
};
dojo.lang.anonCtr=0;
dojo.lang.anon={};
dojo.lang.nameAnonFunc=function(_148,_149,_14a){
var nso=(_149||dojo.lang.anon);
if((_14a)||((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true))){
for(var x in nso){
if(nso[x]===_148){
return x;
}
}
}
var ret="__"+dojo.lang.anonCtr++;
while(typeof nso[ret]!="undefined"){
ret="__"+dojo.lang.anonCtr++;
}
nso[ret]=_148;
return ret;
};
dojo.lang.forward=function(_14e){
return function(){
return this[_14e].apply(this,arguments);
};
};
dojo.lang.curry=function(ns,func){
var _151=[];
ns=ns||dj_global;
if(dojo.lang.isString(func)){
func=ns[func];
}
for(var x=2;x<arguments.length;x++){
_151.push(arguments[x]);
}
var _153=(func["__preJoinArity"]||func.length)-_151.length;
function gather(_154,_155,_156){
var _157=_156;
var _158=_155.slice(0);
for(var x=0;x<_154.length;x++){
_158.push(_154[x]);
}
_156=_156-_154.length;
if(_156<=0){
var res=func.apply(ns,_158);
_156=_157;
return res;
}else{
return function(){
return gather(arguments,_158,_156);
};
}
}
return gather([],_151,_153);
};
dojo.lang.curryArguments=function(ns,func,args,_15e){
var _15f=[];
var x=_15e||0;
for(x=_15e;x<args.length;x++){
_15f.push(args[x]);
}
return dojo.lang.curry.apply(dojo.lang,[ns,func].concat(_15f));
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
dojo.lang.delayThese=function(farr,cb,_165,_166){
if(!farr.length){
if(typeof _166=="function"){
_166();
}
return;
}
if((typeof _165=="undefined")&&(typeof cb=="number")){
_165=cb;
cb=function(){
};
}else{
if(!cb){
cb=function(){
};
if(!_165){
_165=0;
}
}
}
setTimeout(function(){
(farr.shift())();
cb();
dojo.lang.delayThese(farr,cb,_165,_166);
},_165);
};
dojo.provide("dojo.string.extras");
dojo.require("dojo.string.common");
dojo.require("dojo.lang");
dojo.string.substituteParams=function(_167,hash){
var map=(typeof hash=="object")?hash:dojo.lang.toArray(arguments,1);
return _167.replace(/\%\{(\w+)\}/g,function(_16a,key){
return map[key]||dojo.raise("Substitution not found: "+key);
});
};
dojo.string.paramString=function(str,_16d,_16e){
dojo.deprecated("dojo.string.paramString","use dojo.string.substituteParams instead","0.4");
for(var name in _16d){
var re=new RegExp("\\%\\{"+name+"\\}","g");
str=str.replace(re,_16d[name]);
}
if(_16e){
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
var _172=str.split(" ");
for(var i=0;i<_172.length;i++){
_172[i]=_172[i].charAt(0).toUpperCase()+_172[i].substring(1);
}
return _172.join(" ");
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
var _177=escape(str);
var _178,re=/%u([0-9A-F]{4})/i;
while((_178=_177.match(re))){
var num=Number("0x"+_178[1]);
var _17a=escape("&#"+num+";");
ret+=_177.substring(0,_178.index)+_17a;
_177=_177.substring(_178.index+_178[0].length);
}
ret+=_177.replace(/\+/g,"%2B");
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
dojo.string.escapeXml=function(str,_17f){
str=str.replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");
if(!_17f){
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
dojo.string.endsWith=function(str,end,_188){
if(_188){
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
dojo.string.startsWith=function(str,_18c,_18d){
if(_18d){
str=str.toLowerCase();
_18c=_18c.toLowerCase();
}
return str.indexOf(_18c)==0;
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
dojo.string.normalizeNewlines=function(text,_193){
if(_193=="\n"){
text=text.replace(/\r\n/g,"\n");
text=text.replace(/\r/g,"\n");
}else{
if(_193=="\r"){
text=text.replace(/\r\n/g,"\r");
text=text.replace(/\n/g,"\r");
}else{
text=text.replace(/([^\r])\n/g,"$1\r\n");
text=text.replace(/\r([^\n])/g,"\r\n$1");
}
}
return text;
};
dojo.string.splitEscaped=function(str,_195){
var _196=[];
for(var i=0,prevcomma=0;i<str.length;i++){
if(str.charAt(i)=="\\"){
i++;
continue;
}
if(str.charAt(i)==_195){
_196.push(str.substring(prevcomma,i));
prevcomma=i+1;
}
}
_196.push(str.substr(prevcomma));
return _196;
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
var _19a=node.tagName;
if(_19a.substr(0,5).toLowerCase()!="dojo:"){
if(_19a.substr(0,4).toLowerCase()=="dojo"){
return "dojo:"+_19a.substring(4).toLowerCase();
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
var _19c=node.className||node.getAttribute("class");
if((_19c)&&(_19c.indexOf)&&(_19c.indexOf("dojo-")!=-1)){
var _19d=_19c.split(" ");
for(var x=0;x<_19d.length;x++){
if((_19d[x].length>5)&&(_19d[x].indexOf("dojo-")>=0)){
return "dojo:"+_19d[x].substr(5).toLowerCase();
}
}
}
}
}
return _19a.toLowerCase();
};
dojo.dom.getUniqueId=function(){
do{
var id="dj_unique_"+(++arguments.callee._idIncrement);
}while(document.getElementById(id));
return id;
};
dojo.dom.getUniqueId._idIncrement=0;
dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_1a0,_1a1){
var node=_1a0.firstChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.nextSibling;
}
if(_1a1&&node&&node.tagName&&node.tagName.toLowerCase()!=_1a1.toLowerCase()){
node=dojo.dom.nextElement(node,_1a1);
}
return node;
};
dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_1a3,_1a4){
var node=_1a3.lastChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.previousSibling;
}
if(_1a4&&node&&node.tagName&&node.tagName.toLowerCase()!=_1a4.toLowerCase()){
node=dojo.dom.prevElement(node,_1a4);
}
return node;
};
dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(node,_1a7){
if(!node){
return null;
}
do{
node=node.nextSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_1a7&&_1a7.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.nextElement(node,_1a7);
}
return node;
};
dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(node,_1a9){
if(!node){
return null;
}
if(_1a9){
_1a9=_1a9.toLowerCase();
}
do{
node=node.previousSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_1a9&&_1a9.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.prevElement(node,_1a9);
}
return node;
};
dojo.dom.moveChildren=function(_1aa,_1ab,trim){
var _1ad=0;
if(trim){
while(_1aa.hasChildNodes()&&_1aa.firstChild.nodeType==dojo.dom.TEXT_NODE){
_1aa.removeChild(_1aa.firstChild);
}
while(_1aa.hasChildNodes()&&_1aa.lastChild.nodeType==dojo.dom.TEXT_NODE){
_1aa.removeChild(_1aa.lastChild);
}
}
while(_1aa.hasChildNodes()){
_1ab.appendChild(_1aa.firstChild);
_1ad++;
}
return _1ad;
};
dojo.dom.copyChildren=function(_1ae,_1af,trim){
var _1b1=_1ae.cloneNode(true);
return this.moveChildren(_1b1,_1af,trim);
};
dojo.dom.removeChildren=function(node){
var _1b3=node.childNodes.length;
while(node.hasChildNodes()){
node.removeChild(node.firstChild);
}
return _1b3;
};
dojo.dom.replaceChildren=function(node,_1b5){
dojo.dom.removeChildren(node);
node.appendChild(_1b5);
};
dojo.dom.removeNode=function(node){
if(node&&node.parentNode){
return node.parentNode.removeChild(node);
}
};
dojo.dom.getAncestors=function(node,_1b8,_1b9){
var _1ba=[];
var _1bb=dojo.lang.isFunction(_1b8);
while(node){
if(!_1bb||_1b8(node)){
_1ba.push(node);
}
if(_1b9&&_1ba.length>0){
return _1ba[0];
}
node=node.parentNode;
}
if(_1b9){
return null;
}
return _1ba;
};
dojo.dom.getAncestorsByTag=function(node,tag,_1be){
tag=tag.toLowerCase();
return dojo.dom.getAncestors(node,function(el){
return ((el.tagName)&&(el.tagName.toLowerCase()==tag));
},_1be);
};
dojo.dom.getFirstAncestorByTag=function(node,tag){
return dojo.dom.getAncestorsByTag(node,tag,true);
};
dojo.dom.isDescendantOf=function(node,_1c3,_1c4){
if(_1c4&&node){
node=node.parentNode;
}
while(node){
if(node==_1c3){
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
var _1c7=["MSXML2","Microsoft","MSXML","MSXML3"];
for(var i=0;i<_1c7.length;i++){
try{
doc=new ActiveXObject(_1c7[i]+".XMLDOM");
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
dojo.dom.createDocumentFromText=function(str,_1ca){
if(!_1ca){
_1ca="text/xml";
}
if(!dj_undef("DOMParser")){
var _1cb=new DOMParser();
return _1cb.parseFromString(str,_1ca);
}else{
if(!dj_undef("ActiveXObject")){
var _1cc=dojo.dom.createDocument();
if(_1cc){
_1cc.async=false;
_1cc.loadXML(str);
return _1cc;
}else{
dojo.debug("toXml didn't work?");
}
}else{
if(document.createElement){
var tmp=document.createElement("xml");
tmp.innerHTML=str;
if(document.implementation&&document.implementation.createDocument){
var _1ce=document.implementation.createDocument("foo","",null);
for(var i=0;i<tmp.childNodes.length;i++){
_1ce.importNode(tmp.childNodes.item(i),true);
}
return _1ce;
}
return ((tmp.document)&&(tmp.document.firstChild?tmp.document.firstChild:tmp));
}
}
}
return null;
};
dojo.dom.prependChild=function(node,_1d1){
if(_1d1.firstChild){
_1d1.insertBefore(node,_1d1.firstChild);
}else{
_1d1.appendChild(node);
}
return true;
};
dojo.dom.insertBefore=function(node,ref,_1d4){
if(_1d4!=true&&(node===ref||node.nextSibling===ref)){
return false;
}
var _1d5=ref.parentNode;
_1d5.insertBefore(node,ref);
return true;
};
dojo.dom.insertAfter=function(node,ref,_1d8){
var pn=ref.parentNode;
if(ref==pn.lastChild){
if((_1d8!=true)&&(node===ref)){
return false;
}
pn.appendChild(node);
}else{
return this.insertBefore(node,ref.nextSibling,_1d8);
}
return true;
};
dojo.dom.insertAtPosition=function(node,ref,_1dc){
if((!node)||(!ref)||(!_1dc)){
return false;
}
switch(_1dc.toLowerCase()){
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
dojo.dom.insertAtIndex=function(node,_1de,_1df){
var _1e0=_1de.childNodes;
if(!_1e0.length){
_1de.appendChild(node);
return true;
}
var _1e1=null;
for(var i=0;i<_1e0.length;i++){
var _1e3=_1e0.item(i)["getAttribute"]?parseInt(_1e0.item(i).getAttribute("dojoinsertionindex")):-1;
if(_1e3<_1df){
_1e1=_1e0.item(i);
}
}
if(_1e1){
return dojo.dom.insertAfter(node,_1e1);
}else{
return dojo.dom.insertBefore(node,_1e0.item(0));
}
};
dojo.dom.textContent=function(node,text){
if(text){
dojo.dom.replaceChildren(node,document.createTextNode(text));
return text;
}else{
var _1e6="";
if(node==null){
return _1e6;
}
for(var i=0;i<node.childNodes.length;i++){
switch(node.childNodes[i].nodeType){
case 1:
case 5:
_1e6+=dojo.dom.textContent(node.childNodes[i]);
break;
case 3:
case 2:
case 4:
_1e6+=node.childNodes[i].nodeValue;
break;
default:
break;
}
}
return _1e6;
}
};
dojo.dom.collectionToArray=function(_1e8){
dojo.deprecated("dojo.dom.collectionToArray","use dojo.lang.toArray instead","0.4");
return dojo.lang.toArray(_1e8);
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
var _1f0=args["back"]||args["backButton"]||args["handle"];
var tcb=function(_1f2){
if(window.location.hash!=""){
setTimeout("window.location.href = '"+hash+"';",1);
}
_1f0.apply(this,[_1f2]);
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
var _1f3=args["forward"]||args["forwardButton"]||args["handle"];
var tfw=function(_1f5){
if(window.location.hash!=""){
window.location.href=hash;
}
if(_1f3){
_1f3.apply(this,[_1f5]);
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
if((window.location.hash==this.initialHash||window.location.href==this.initialHref)&&(hsl==1)){
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
},iframeLoaded:function(evt,_1f8){
var _1f9=this._getUrlQuery(_1f8.href);
if(_1f9==null){
if(this.historyStack.length==1){
this.handleBackButton();
}
return;
}
if(this.moveForward){
this.moveForward=false;
return;
}
if(this.historyStack.length>=2&&_1f9==this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){
this.handleBackButton();
}else{
if(this.forwardStack.length>0&&_1f9==this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){
this.handleForwardButton();
}
}
},handleBackButton:function(){
var _1fa=this.historyStack.pop();
if(!_1fa){
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
this.forwardStack.push(_1fa);
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
var _1fe=url.split("?");
if(_1fe.length<2){
return null;
}else{
return _1fe[1];
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
var _200=false;
var _201=node.getElementsByTagName("input");
dojo.lang.forEach(_201,function(_202){
if(_200){
return;
}
if(_202.getAttribute("type")=="file"){
_200=true;
}
});
return _200;
};
dojo.io.formHasFile=function(_203){
return dojo.io.checkChildrenForFile(_203);
};
dojo.io.updateNode=function(node,_205){
node=dojo.byId(node);
var args=_205;
if(dojo.lang.isString(_205)){
args={url:_205};
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
dojo.io.encodeForm=function(_20c,_20d,_20e){
if((!_20c)||(!_20c.tagName)||(!_20c.tagName.toLowerCase()=="form")){
dojo.raise("Attempted to encode a non-form element.");
}
if(!_20e){
_20e=dojo.io.formFilter;
}
var enc=/utf/i.test(_20d||"")?encodeURIComponent:dojo.string.encodeAscii;
var _210=[];
for(var i=0;i<_20c.elements.length;i++){
var elm=_20c.elements[i];
if(!elm||elm.tagName.toLowerCase()=="fieldset"||!_20e(elm)){
continue;
}
var name=enc(elm.name);
var type=elm.type.toLowerCase();
if(type=="select-multiple"){
for(var j=0;j<elm.options.length;j++){
if(elm.options[j].selected){
_210.push(name+"="+enc(elm.options[j].value));
}
}
}else{
if(dojo.lang.inArray(type,["radio","checkbox"])){
if(elm.checked){
_210.push(name+"="+enc(elm.value));
}
}else{
_210.push(name+"="+enc(elm.value));
}
}
}
var _216=_20c.getElementsByTagName("input");
for(var i=0;i<_216.length;i++){
var _217=_216[i];
if(_217.type.toLowerCase()=="image"&&_217.form==_20c&&_20e(_217)){
var name=enc(_217.name);
_210.push(name+"="+enc(_217.value));
_210.push(name+".x=0");
_210.push(name+".y=0");
}
}
return _210.join("&")+"&";
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
var _21d=form.getElementsByTagName("input");
for(var i=0;i<_21d.length;i++){
var _21e=_21d[i];
if(_21e.type.toLowerCase()=="image"&&_21e.form==form){
this.connect(_21e,"onclick","click");
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
var _225=false;
if(node.disabled||!node.name){
_225=false;
}else{
if(dojo.lang.inArray(type,["submit","button","image"])){
if(!this.clickedButton){
this.clickedButton=node;
}
_225=node==this.clickedButton;
}else{
_225=!dojo.lang.inArray(type,["file","submit","reset","button"]);
}
}
return _225;
},connect:function(_226,_227,_228){
if(dojo.evalObjPath("dojo.event.connect")){
dojo.event.connect(_226,_227,this,_228);
}else{
var fcn=dojo.lang.hitch(this,_228);
_226[_227]=function(e){
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
var _22b=this;
var _22c={};
this.useCache=false;
this.preventCache=false;
function getCacheKey(url,_22e,_22f){
return url+"|"+_22e+"|"+_22f.toLowerCase();
}
function addToCache(url,_231,_232,http){
_22c[getCacheKey(url,_231,_232)]=http;
}
function getFromCache(url,_235,_236){
return _22c[getCacheKey(url,_235,_236)];
}
this.clearCache=function(){
_22c={};
};
function doLoad(_237,http,url,_23a,_23b){
if(((http.status>=200)&&(http.status<300))||(http.status==304)||(location.protocol=="file:"&&(http.status==0||http.status==undefined))||(location.protocol=="chrome:"&&(http.status==0||http.status==undefined))){
var ret;
if(_237.method.toLowerCase()=="head"){
var _23d=http.getAllResponseHeaders();
ret={};
ret.toString=function(){
return _23d;
};
var _23e=_23d.split(/[\r\n]+/g);
for(var i=0;i<_23e.length;i++){
var pair=_23e[i].match(/^([^:]+)\s*:\s*(.+)$/i);
if(pair){
ret[pair[1]]=pair[2];
}
}
}else{
if(_237.mimetype=="text/javascript"){
try{
ret=dj_eval(http.responseText);
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=null;
}
}else{
if(_237.mimetype=="text/json"){
try{
ret=dj_eval("("+http.responseText+")");
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=false;
}
}else{
if((_237.mimetype=="application/xml")||(_237.mimetype=="text/xml")){
ret=http.responseXML;
if(!ret||typeof ret=="string"||!http.getResponseHeader("Content-Type")){
ret=dojo.dom.createDocumentFromText(http.responseText);
}
}else{
ret=http.responseText;
}
}
}
}
if(_23b){
addToCache(url,_23a,_237.method,http);
}
_237[(typeof _237.load=="function")?"load":"handle"]("load",ret,http,_237);
}else{
var _241=new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);
_237[(typeof _237.error=="function")?"error":"handle"]("error",_241,http,_237);
}
}
function setHeaders(http,_243){
if(_243["headers"]){
for(var _244 in _243["headers"]){
if(_244.toLowerCase()=="content-type"&&!_243["contentType"]){
_243["contentType"]=_243["headers"][_244];
}else{
http.setRequestHeader(_244,_243["headers"][_244]);
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
var _248=dojo.hostenv.getXmlhttpObject()?true:false;
this.canHandle=function(_249){
return _248&&dojo.lang.inArray((_249["mimetype"].toLowerCase()||""),["text/plain","text/html","application/xml","text/xml","text/javascript","text/json"])&&!(_249["formNode"]&&dojo.io.formHasFile(_249["formNode"]));
};
this.multipartBoundary="45309FFF-BD65-4d50-99C9-36986896A96F";
this.bind=function(_24a){
if(!_24a["url"]){
if(!_24a["formNode"]&&(_24a["backButton"]||_24a["back"]||_24a["changeUrl"]||_24a["watchForURL"])&&(!djConfig.preventBackButtonFix)){
dojo.deprecated("Using dojo.io.XMLHTTPTransport.bind() to add to browser history without doing an IO request","Use dojo.undo.browser.addToHistory() instead.","0.4");
dojo.undo.browser.addToHistory(_24a);
return true;
}
}
var url=_24a.url;
var _24c="";
if(_24a["formNode"]){
var ta=_24a.formNode.getAttribute("action");
if((ta)&&(!_24a["url"])){
url=ta;
}
var tp=_24a.formNode.getAttribute("method");
if((tp)&&(!_24a["method"])){
_24a.method=tp;
}
_24c+=dojo.io.encodeForm(_24a.formNode,_24a.encoding,_24a["formFilter"]);
}
if(url.indexOf("#")>-1){
dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);
url=url.split("#")[0];
}
if(_24a["file"]){
_24a.method="post";
}
if(!_24a["method"]){
_24a.method="get";
}
if(_24a.method.toLowerCase()=="get"){
_24a.multipart=false;
}else{
if(_24a["file"]){
_24a.multipart=true;
}else{
if(!_24a["multipart"]){
_24a.multipart=false;
}
}
}
if(_24a["backButton"]||_24a["back"]||_24a["changeUrl"]){
dojo.undo.browser.addToHistory(_24a);
}
var _24f=_24a["content"]||{};
if(_24a.sendTransport){
_24f["dojo.transport"]="xmlhttp";
}
do{
if(_24a.postContent){
_24c=_24a.postContent;
break;
}
if(_24f){
_24c+=dojo.io.argsFromMap(_24f,_24a.encoding);
}
if(_24a.method.toLowerCase()=="get"||!_24a.multipart){
break;
}
var t=[];
if(_24c.length){
var q=_24c.split("&");
for(var i=0;i<q.length;++i){
if(q[i].length){
var p=q[i].split("=");
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+p[0]+"\"","",p[1]);
}
}
}
if(_24a.file){
if(dojo.lang.isArray(_24a.file)){
for(var i=0;i<_24a.file.length;++i){
var o=_24a.file[i];
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}else{
var o=_24a.file;
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}
if(t.length){
t.push("--"+this.multipartBoundary+"--","");
_24c=t.join("\r\n");
}
}while(false);
var _255=_24a["sync"]?false:true;
var _256=_24a["preventCache"]||(this.preventCache==true&&_24a["preventCache"]!=false);
var _257=_24a["useCache"]==true||(this.useCache==true&&_24a["useCache"]!=false);
if(!_256&&_257){
var _258=getFromCache(url,_24c,_24a.method);
if(_258){
doLoad(_24a,_258,url,_24c,false);
return;
}
}
var http=dojo.hostenv.getXmlhttpObject(_24a);
var _25a=false;
if(_255){
var _25b=this.inFlight.push({"req":_24a,"http":http,"url":url,"query":_24c,"useCache":_257,"startTime":_24a.timeoutSeconds?(new Date()).getTime():0});
this.startWatchingInFlight();
}
if(_24a.method.toLowerCase()=="post"){
http.open("POST",url,_255);
setHeaders(http,_24a);
http.setRequestHeader("Content-Type",_24a.multipart?("multipart/form-data; boundary="+this.multipartBoundary):(_24a.contentType||"application/x-www-form-urlencoded"));
try{
http.send(_24c);
}
catch(e){
if(typeof http.abort=="function"){
http.abort();
}
doLoad(_24a,{status:404},url,_24c,_257);
}
}else{
var _25c=url;
if(_24c!=""){
_25c+=(_25c.indexOf("?")>-1?"&":"?")+_24c;
}
if(_256){
_25c+=(dojo.string.endsWithAny(_25c,"?","&")?"":(_25c.indexOf("?")>-1?"&":"?"))+"dojo.preventCache="+new Date().valueOf();
}
http.open(_24a.method.toUpperCase(),_25c,_255);
setHeaders(http,_24a);
try{
http.send(null);
}
catch(e){
if(typeof http.abort=="function"){
http.abort();
}
doLoad(_24a,{status:404},url,_24c,_257);
}
}
if(!_255){
doLoad(_24a,http,url,_24c,_257);
}
_24a.abort=function(){
return http.abort();
};
return;
};
dojo.io.transports.addTransport("XMLHTTPTransport");
};
dojo.provide("dojo.io.cookie");
dojo.io.cookie.setCookie=function(name,_25e,days,path,_261,_262){
var _263=-1;
if(typeof days=="number"&&days>=0){
var d=new Date();
d.setTime(d.getTime()+(days*24*60*60*1000));
_263=d.toGMTString();
}
_25e=escape(_25e);
document.cookie=name+"="+_25e+";"+(_263!=-1?" expires="+_263+";":"")+(path?"path="+path:"")+(_261?"; domain="+_261:"")+(_262?"; secure":"");
};
dojo.io.cookie.set=dojo.io.cookie.setCookie;
dojo.io.cookie.getCookie=function(name){
var idx=document.cookie.lastIndexOf(name+"=");
if(idx==-1){
return null;
}
var _267=document.cookie.substring(idx+name.length+1);
var end=_267.indexOf(";");
if(end==-1){
end=_267.length;
}
_267=_267.substring(0,end);
_267=unescape(_267);
return _267;
};
dojo.io.cookie.get=dojo.io.cookie.getCookie;
dojo.io.cookie.deleteCookie=function(name){
dojo.io.cookie.setCookie(name,"-",0);
};
dojo.io.cookie.setObjectCookie=function(name,obj,days,path,_26e,_26f,_270){
if(arguments.length==5){
_270=_26e;
_26e=null;
_26f=null;
}
var _271=[],cookie,value="";
if(!_270){
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
_271.push(escape(prop)+"="+escape(cookie[prop]));
}
value=_271.join("&");
}
dojo.io.cookie.setCookie(name,value,days,path,_26e,_26f);
};
dojo.io.cookie.getObjectCookie=function(name){
var _274=null,cookie=dojo.io.cookie.getCookie(name);
if(cookie){
_274={};
var _275=cookie.split("&");
for(var i=0;i<_275.length;i++){
var pair=_275[i].split("=");
var _278=pair[1];
if(isNaN(_278)){
_278=unescape(pair[1]);
}
_274[unescape(pair[0])]=_278;
}
}
return _274;
};
dojo.io.cookie.isSupported=function(){
if(typeof navigator.cookieEnabled!="boolean"){
dojo.io.cookie.setCookie("__TestingYourBrowserForCookieSupport__","CookiesAllowed",90,null);
var _279=dojo.io.cookie.getCookie("__TestingYourBrowserForCookieSupport__");
navigator.cookieEnabled=(_279=="CookiesAllowed");
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
function interpolateArgs(args,_27b){
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
var _27e=dl.nameAnonFunc(args[2],ao.adviceObj,_27b);
ao.adviceFunc=_27e;
}else{
if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){
ao.adviceType="after";
ao.srcObj=dj_global;
var _27e=dl.nameAnonFunc(args[0],ao.srcObj,_27b);
ao.srcFunc=_27e;
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
var _27e=dl.nameAnonFunc(args[1],dj_global,_27b);
ao.srcFunc=_27e;
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){
ao.srcObj=args[1];
ao.srcFunc=args[2];
var _27e=dl.nameAnonFunc(args[3],dj_global,_27b);
ao.adviceObj=dj_global;
ao.adviceFunc=_27e;
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
var _27e=dl.nameAnonFunc(ao.aroundFunc,ao.aroundObj,_27b);
ao.aroundFunc=_27e;
}
if(dl.isFunction(ao.srcFunc)){
ao.srcFunc=dl.getNameInObj(ao.srcObj,ao.srcFunc);
}
if(dl.isFunction(ao.adviceFunc)){
ao.adviceFunc=dl.getNameInObj(ao.adviceObj,ao.adviceFunc);
}
if((ao.aroundObj)&&(dl.isFunction(ao.aroundFunc))){
ao.aroundFunc=dl.getNameInObj(ao.aroundObj,ao.aroundFunc);
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
var ao=interpolateArgs(arguments,true);
}
if(dojo.lang.isArray(ao.srcObj)&&ao.srcObj!=""){
var _280={};
for(var x in ao){
_280[x]=ao[x];
}
var mjps=[];
dojo.lang.forEach(ao.srcObj,function(src){
if((dojo.render.html.capable)&&(dojo.lang.isString(src))){
src=dojo.byId(src);
}
_280.srcObj=src;
mjps.push(dojo.event.connect.call(dojo.event,_280));
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
var _288;
if((arguments.length==1)&&(typeof a1=="object")){
_288=a1;
}else{
_288={srcObj:a1,srcFunc:a2};
}
_288.adviceFunc=function(){
var _289=[];
for(var x=0;x<arguments.length;x++){
_289.push(arguments[x]);
}
dojo.debug("("+_288.srcObj+")."+_288.srcFunc,":",_289.join(", "));
};
this.kwConnect(_288);
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
var ao=interpolateArgs(arguments,true);
ao.once=true;
return this.connect(ao);
};
this._kwConnectImpl=function(_290,_291){
var fn=(_291)?"disconnect":"connect";
if(typeof _290["srcFunc"]=="function"){
_290.srcObj=_290["srcObj"]||dj_global;
var _293=dojo.lang.nameAnonFunc(_290.srcFunc,_290.srcObj,true);
_290.srcFunc=_293;
}
if(typeof _290["adviceFunc"]=="function"){
_290.adviceObj=_290["adviceObj"]||dj_global;
var _293=dojo.lang.nameAnonFunc(_290.adviceFunc,_290.adviceObj,true);
_290.adviceFunc=_293;
}
return dojo.event[fn]((_290["type"]||_290["adviceType"]||"after"),_290["srcObj"]||dj_global,_290["srcFunc"],_290["adviceObj"]||_290["targetObj"]||dj_global,_290["adviceFunc"]||_290["targetFunc"],_290["aroundObj"],_290["aroundFunc"],_290["once"],_290["delay"],_290["rate"],_290["adviceMsg"]||false);
};
this.kwConnect=function(_294){
return this._kwConnectImpl(_294,false);
};
this.disconnect=function(){
var ao=interpolateArgs(arguments,true);
if(!ao.adviceFunc){
return;
}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);
return mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);
};
this.kwDisconnect=function(_297){
return this._kwConnectImpl(_297,true);
};
};
dojo.event.MethodInvocation=function(_298,obj,args){
this.jp_=_298;
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
dojo.event.MethodJoinPoint=function(obj,_2a0){
this.object=obj||dj_global;
this.methodname=_2a0;
this.methodfunc=this.object[_2a0];
this.before=[];
this.after=[];
this.around=[];
};
dojo.event.MethodJoinPoint.getForMethod=function(obj,_2a2){
if(!obj){
obj=dj_global;
}
if(!obj[_2a2]){
obj[_2a2]=function(){
};
if(!obj[_2a2]){
dojo.raise("Cannot set do-nothing method on that object "+_2a2);
}
}else{
if((!dojo.lang.isFunction(obj[_2a2]))&&(!dojo.lang.isAlien(obj[_2a2]))){
return null;
}
}
var _2a3=_2a2+"$joinpoint";
var _2a4=_2a2+"$joinpoint$method";
var _2a5=obj[_2a3];
if(!_2a5){
var _2a6=false;
if(dojo.event["browser"]){
if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){
_2a6=true;
dojo.event.browser.addClobberNodeAttrs(obj,[_2a3,_2a4,_2a2]);
}
}
var _2a7=obj[_2a2].length;
obj[_2a4]=obj[_2a2];
_2a5=obj[_2a3]=new dojo.event.MethodJoinPoint(obj,_2a4);
obj[_2a2]=function(){
var args=[];
if((_2a6)&&(!arguments.length)){
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
if((x==0)&&(_2a6)&&(dojo.event.browser.isEvent(arguments[x]))){
args.push(dojo.event.browser.fixEvent(arguments[x],this));
}else{
args.push(arguments[x]);
}
}
}
return _2a5.run.apply(_2a5,args);
};
obj[_2a2].__preJoinArity=_2a7;
}
return _2a5;
};
dojo.lang.extend(dojo.event.MethodJoinPoint,{unintercept:function(){
this.object[this.methodname]=this.methodfunc;
this.before=[];
this.after=[];
this.around=[];
},disconnect:dojo.lang.forward("unintercept"),run:function(){
var obj=this.object||dj_global;
var args=arguments;
var _2ad=[];
for(var x=0;x<args.length;x++){
_2ad[x]=args[x];
}
var _2af=function(marr){
if(!marr){
dojo.debug("Null argument to unrollAdvice()");
return;
}
var _2b1=marr[0]||dj_global;
var _2b2=marr[1];
if(!_2b1[_2b2]){
dojo.raise("function \""+_2b2+"\" does not exist on \""+_2b1+"\"");
}
var _2b3=marr[2]||dj_global;
var _2b4=marr[3];
var msg=marr[6];
var _2b6;
var to={args:[],jp_:this,object:obj,proceed:function(){
return _2b1[_2b2].apply(_2b1,to.args);
}};
to.args=_2ad;
var _2b8=parseInt(marr[4]);
var _2b9=((!isNaN(_2b8))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));
if(marr[5]){
var rate=parseInt(marr[5]);
var cur=new Date();
var _2bc=false;
if((marr["last"])&&((cur-marr.last)<=rate)){
if(dojo.event.canTimeout){
if(marr["delayTimer"]){
clearTimeout(marr.delayTimer);
}
var tod=parseInt(rate*2);
var mcpy=dojo.lang.shallowCopy(marr);
marr.delayTimer=setTimeout(function(){
mcpy[5]=0;
_2af(mcpy);
},tod);
}
return;
}else{
marr.last=cur;
}
}
if(_2b4){
_2b3[_2b4].call(_2b3,to);
}else{
if((_2b9)&&((dojo.render.html)||(dojo.render.svg))){
dj_global["setTimeout"](function(){
if(msg){
_2b1[_2b2].call(_2b1,to);
}else{
_2b1[_2b2].apply(_2b1,args);
}
},_2b8);
}else{
if(msg){
_2b1[_2b2].call(_2b1,to);
}else{
_2b1[_2b2].apply(_2b1,args);
}
}
}
};
if(this.before.length>0){
dojo.lang.forEach(this.before,_2af);
}
var _2bf;
if(this.around.length>0){
var mi=new dojo.event.MethodInvocation(this,obj,args);
_2bf=mi.proceed();
}else{
if(this.methodfunc){
_2bf=this.object[this.methodname].apply(this.object,args);
}
}
if(this.after.length>0){
dojo.lang.forEach(this.after,_2af);
}
return (this.methodfunc)?_2bf:null;
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
},addAdvice:function(_2c4,_2c5,_2c6,_2c7,_2c8,_2c9,once,_2cb,rate,_2cd){
var arr=this.getArr(_2c8);
if(!arr){
dojo.raise("bad this: "+this);
}
var ao=[_2c4,_2c5,_2c6,_2c7,_2cb,rate,_2cd];
if(once){
if(this.hasAdvice(_2c4,_2c5,_2c8,arr)>=0){
return;
}
}
if(_2c9=="first"){
arr.unshift(ao);
}else{
arr.push(ao);
}
},hasAdvice:function(_2d0,_2d1,_2d2,arr){
if(!arr){
arr=this.getArr(_2d2);
}
var ind=-1;
for(var x=0;x<arr.length;x++){
var aao=(typeof _2d1=="object")?(new String(_2d1)).toString():_2d1;
var a1o=(typeof arr[x][1]=="object")?(new String(arr[x][1])).toString():arr[x][1];
if((arr[x][0]==_2d0)&&(a1o==aao)){
ind=x;
}
}
return ind;
},removeAdvice:function(_2d8,_2d9,_2da,once){
var arr=this.getArr(_2da);
var ind=this.hasAdvice(_2d8,_2d9,_2da,arr);
if(ind==-1){
return false;
}
while(ind!=-1){
arr.splice(ind,1);
if(once){
break;
}
ind=this.hasAdvice(_2d8,_2d9,_2da,arr);
}
return true;
}});
dojo.require("dojo.event");
dojo.provide("dojo.event.topic");
dojo.event.topic=new function(){
this.topics={};
this.getTopic=function(_2de){
if(!this.topics[_2de]){
this.topics[_2de]=new this.TopicImpl(_2de);
}
return this.topics[_2de];
};
this.registerPublisher=function(_2df,obj,_2e1){
var _2df=this.getTopic(_2df);
_2df.registerPublisher(obj,_2e1);
};
this.subscribe=function(_2e2,obj,_2e4){
var _2e2=this.getTopic(_2e2);
_2e2.subscribe(obj,_2e4);
};
this.unsubscribe=function(_2e5,obj,_2e7){
var _2e5=this.getTopic(_2e5);
_2e5.unsubscribe(obj,_2e7);
};
this.destroy=function(_2e8){
this.getTopic(_2e8).destroy();
delete this.topics[_2e8];
};
this.publishApply=function(_2e9,args){
var _2e9=this.getTopic(_2e9);
_2e9.sendMessage.apply(_2e9,args);
};
this.publish=function(_2eb,_2ec){
var _2eb=this.getTopic(_2eb);
var args=[];
for(var x=1;x<arguments.length;x++){
args.push(arguments[x]);
}
_2eb.sendMessage.apply(_2eb,args);
};
};
dojo.event.topic.TopicImpl=function(_2ef){
this.topicName=_2ef;
this.subscribe=function(_2f0,_2f1){
var tf=_2f1||_2f0;
var to=(!_2f1)?dj_global:_2f0;
dojo.event.kwConnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
this.unsubscribe=function(_2f4,_2f5){
var tf=(!_2f5)?_2f4:_2f5;
var to=(!_2f5)?null:_2f4;
dojo.event.kwDisconnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
this.destroy=function(){
dojo.event.MethodJoinPoint.getForMethod(this,"sendMessage").disconnect();
};
this.registerPublisher=function(_2f8,_2f9){
dojo.event.connect(_2f8,_2f9,this,"sendMessage");
};
this.sendMessage=function(_2fa){
};
};
dojo.provide("dojo.event.browser");
dojo.require("dojo.event");
dojo._ie_clobber=new function(){
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
this.clobber=function(_2fd){
var na;
var tna;
if(_2fd){
tna=_2fd.all||_2fd.getElementsByTagName("*");
na=[_2fd];
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
var _301={};
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
dojo.addOnUnload(function(){
dojo._ie_clobber.clobber();
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
dojo._ie_clobber.clobberNodes=[];
});
}
dojo.event.browser=new function(){
var _305=0;
this.clean=function(node){
if(dojo.render.html.ie){
dojo._ie_clobber.clobber(node);
}
};
this.addClobberNode=function(node){
if(!dojo.render.html.ie){
return;
}
if(!node["__doClobber__"]){
node.__doClobber__=true;
dojo._ie_clobber.clobberNodes.push(node);
node.__clobberAttrs__=[];
}
};
this.addClobberNodeAttrs=function(node,_309){
if(!dojo.render.html.ie){
return;
}
this.addClobberNode(node);
for(var x=0;x<_309.length;x++){
node.__clobberAttrs__.push(_309[x]);
}
};
this.removeListener=function(node,_30c,fp,_30e){
if(!_30e){
var _30e=false;
}
_30c=_30c.toLowerCase();
if(_30c.substr(0,2)=="on"){
_30c=_30c.substr(2);
}
if(node.removeEventListener){
node.removeEventListener(_30c,fp,_30e);
}
};
this.addListener=function(node,_310,fp,_312,_313){
if(!node){
return;
}
if(!_312){
var _312=false;
}
_310=_310.toLowerCase();
if(_310.substr(0,2)!="on"){
_310="on"+_310;
}
if(!_313){
var _314=function(evt){
if(!evt){
evt=window.event;
}
var ret=fp(dojo.event.browser.fixEvent(evt,this));
if(_312){
dojo.event.browser.stopEvent(evt);
}
return ret;
};
}else{
_314=fp;
}
if(node.addEventListener){
node.addEventListener(_310.substr(2),_314,_312);
return _314;
}else{
if(typeof node[_310]=="function"){
var _317=node[_310];
node[_310]=function(e){
_317(e);
return _314(e);
};
}else{
node[_310]=_314;
}
if(dojo.render.html.ie){
this.addClobberNodeAttrs(node,[_310]);
}
return _314;
}
};
this.isEvent=function(obj){
return (typeof obj!="undefined")&&(typeof Event!="undefined")&&(obj.eventPhase);
};
this.currentEvent=null;
this.callListener=function(_31a,_31b){
if(typeof _31a!="function"){
dojo.raise("listener not a function: "+_31a);
}
dojo.event.browser.currentEvent.currentTarget=_31b;
return _31a.call(_31b,dojo.event.browser.currentEvent);
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
this.fixEvent=function(evt,_31e){
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
evt.currentTarget=(_31e?_31e:evt.srcElement);
}
if(!evt.layerX){
evt.layerX=evt.offsetX;
}
if(!evt.layerY){
evt.layerY=evt.offsetY;
}
if(!evt.pageX){
evt.pageX=evt.clientX+(window.pageXOffset||document.scrollLeft||document.documentElement.scrollLeft||document.body.scrollLeft||0);
}
if(!evt.pageY){
evt.pageY=evt.clientY+(window.pageYOffset||document.scrollTop||document.documentElement.scrollTop||document.body.scrollTop||0);
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
dojo.lang.extend(dojo.graphics.color.Color,{toRgb:function(_327){
if(_327){
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
},blend:function(_328,_329){
return dojo.graphics.color.blend(this.toRgb(),new dojo.graphics.color.Color(_328).toRgb(),_329);
}});
dojo.graphics.color.named={white:[255,255,255],black:[0,0,0],red:[255,0,0],green:[0,255,0],blue:[0,0,255],navy:[0,0,128],gray:[128,128,128],silver:[192,192,192]};
dojo.graphics.color.blend=function(a,b,_32c){
if(typeof a=="string"){
return dojo.graphics.color.blendHex(a,b,_32c);
}
if(!_32c){
_32c=0;
}else{
if(_32c>1){
_32c=1;
}else{
if(_32c<-1){
_32c=-1;
}
}
}
var c=new Array(3);
for(var i=0;i<3;i++){
var half=Math.abs(a[i]-b[i])/2;
c[i]=Math.floor(Math.min(a[i],b[i])+half+(half*_32c));
}
return c;
};
dojo.graphics.color.blendHex=function(a,b,_332){
return dojo.graphics.color.rgb2hex(dojo.graphics.color.blend(dojo.graphics.color.hex2rgb(a),dojo.graphics.color.hex2rgb(b),_332));
};
dojo.graphics.color.extractRGB=function(_333){
var hex="0123456789abcdef";
_333=_333.toLowerCase();
if(_333.indexOf("rgb")==0){
var _335=_333.match(/rgba*\((\d+), *(\d+), *(\d+)/i);
var ret=_335.splice(1,3);
return ret;
}else{
var _337=dojo.graphics.color.hex2rgb(_333);
if(_337){
return _337;
}else{
return dojo.graphics.color.named[_333]||[255,255,255];
}
}
};
dojo.graphics.color.hex2rgb=function(hex){
var _339="0123456789ABCDEF";
var rgb=new Array(3);
if(hex.indexOf("#")==0){
hex=hex.substring(1);
}
hex=hex.toUpperCase();
if(hex.replace(new RegExp("["+_339+"]","g"),"")!=""){
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
rgb[i]=_339.indexOf(rgb[i].charAt(0))*16+_339.indexOf(rgb[i].charAt(1));
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
var _347=new dojo.uri.Uri(arguments[i].toString());
var _348=new dojo.uri.Uri(uri.toString());
if(_347.path==""&&_347.scheme==null&&_347.authority==null&&_347.query==null){
if(_347.fragment!=null){
_348.fragment=_347.fragment;
}
_347=_348;
}
if(_347.scheme!=null&&_347.authority!=null){
uri="";
}
if(_347.scheme!=null){
uri+=_347.scheme+":";
}
if(_347.authority!=null){
uri+="//"+_347.authority;
}
uri+=_347.path;
if(_347.query!=null){
uri+="?"+_347.query;
}
if(_347.fragment!=null){
uri+="#"+_347.fragment;
}
}
this.uri=uri.toString();
var _349="^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$";
var r=this.uri.match(new RegExp(_349));
this.scheme=r[2]||(r[1]?"":null);
this.authority=r[4]||(r[3]?"":null);
this.path=r[5];
this.query=r[7]||(r[6]?"":null);
this.fragment=r[9]||(r[8]?"":null);
if(this.authority!=null){
_349="^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$";
r=this.authority.match(new RegExp(_349));
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
var _351=ds.getStyle(node,"-moz-box-sizing");
if(!_351){
_351=ds.getStyle(node,"box-sizing");
}
return (_351?_351:bs.CONTENT_BOX);
}
};
ds.isBorderBox=function(node){
return (ds.getBoxSizing(node)==bs.BORDER_BOX);
};
ds.getUnitValue=function(node,_354,_355){
var s=ds.getComputedStyle(node,_354);
if((!s)||((s=="auto")&&(_355))){
return {value:0,units:"px"};
}
if(dojo.lang.isUndefined(s)){
return ds.getUnitValue.bad;
}
var _357=s.match(/(\-?[\d.]+)([a-z%]*)/i);
if(!_357){
return ds.getUnitValue.bad;
}
return {value:Number(_357[1]),units:_357[2].toLowerCase()};
};
ds.getUnitValue.bad={value:NaN,units:""};
ds.getPixelValue=function(node,_359,_35a){
var _35b=ds.getUnitValue(node,_359,_35a);
if(isNaN(_35b.value)){
return 0;
}
if((_35b.value)&&(_35b.units!="px")){
return NaN;
}
return _35b.value;
};
ds.getNumericStyle=function(){
dojo.deprecated("dojo.(style|html).getNumericStyle","in favor of dojo.(style|html).getPixelValue","0.4");
return ds.getPixelValue.apply(this,arguments);
};
ds.setPositivePixelValue=function(node,_35d,_35e){
if(isNaN(_35e)){
return false;
}
node.style[_35d]=Math.max(0,_35e)+"px";
return true;
};
ds._sumPixelValues=function(node,_360,_361){
var _362=0;
for(var x=0;x<_360.length;x++){
_362+=ds.getPixelValue(node,_360[x],_361);
}
return _362;
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
ds.setContentBoxWidth=function(node,_36f){
node=dojo.byId(node);
if(ds.isBorderBox(node)){
_36f+=ds.getPadBorderWidth(node);
}
return ds.setPositivePixelValue(node,"width",_36f);
};
ds.setMarginBoxWidth=function(node,_371){
node=dojo.byId(node);
if(!ds.isBorderBox(node)){
_371-=ds.getPadBorderWidth(node);
}
_371-=ds.getMarginWidth(node);
return ds.setPositivePixelValue(node,"width",_371);
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
ds.setContentBoxHeight=function(node,_37a){
node=dojo.byId(node);
if(ds.isBorderBox(node)){
_37a+=ds.getPadBorderHeight(node);
}
return ds.setPositivePixelValue(node,"height",_37a);
};
ds.setMarginBoxHeight=function(node,_37c){
node=dojo.byId(node);
if(!ds.isBorderBox(node)){
_37c-=ds.getPadBorderHeight(node);
}
_37c-=ds.getMarginHeight(node);
return ds.setPositivePixelValue(node,"height",_37c);
};
ds.getContentHeight=ds.getContentBoxHeight;
ds.getInnerHeight=ds.getBorderBoxHeight;
ds.getOuterHeight=ds.getMarginBoxHeight;
ds.setContentHeight=ds.setContentBoxHeight;
ds.setOuterHeight=ds.setMarginBoxHeight;
ds.getAbsolutePosition=ds.abs=function(node,_37e){
node=dojo.byId(node);
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
if(document.getBoxObjectFor){
var bo=document.getBoxObjectFor(node);
ret.x=bo.x-ds.sumAncestorProperties(node,"scrollLeft");
ret.y=bo.y-ds.sumAncestorProperties(node,"scrollTop");
}else{
if(node["offsetParent"]){
var _383;
if((h.safari)&&(node.style.getPropertyValue("position")=="absolute")&&(node.parentNode==db)){
_383=db;
}else{
_383=db.parentNode;
}
if(node.parentNode!=db){
var nd=node;
if(window.opera){
nd=db;
}
ret.x-=ds.sumAncestorProperties(nd,"scrollLeft");
ret.y-=ds.sumAncestorProperties(nd,"scrollTop");
}
do{
var n=node["offsetLeft"];
ret.x+=isNaN(n)?0:n;
var m=node["offsetTop"];
ret.y+=isNaN(m)?0:m;
node=node.offsetParent;
}while((node!=_383)&&(node!=null));
}else{
if(node["x"]&&node["y"]){
ret.x+=isNaN(node.x)?0:node.x;
ret.y+=isNaN(node.y)?0:node.y;
}
}
}
}
if(_37e){
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
var _389=0;
while(node){
var val=node[prop];
if(val){
_389+=val-0;
if(node==document.body){
break;
}
}
node=node.parentNode;
}
return _389;
};
ds.getTotalOffset=function(node,type,_38d){
return ds.abs(node,_38d)[(type=="top")?"y":"x"];
};
ds.getAbsoluteX=ds.totalOffsetLeft=function(node,_38f){
return ds.getTotalOffset(node,"left",_38f);
};
ds.getAbsoluteY=ds.totalOffsetTop=function(node,_391){
return ds.getTotalOffset(node,"top",_391);
};
ds.styleSheet=null;
ds.insertCssRule=function(_392,_393,_394){
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
_394=ds.styleSheet.cssRules.length;
}else{
if(ds.styleSheet.rules){
_394=ds.styleSheet.rules.length;
}else{
return null;
}
}
}
if(ds.styleSheet.insertRule){
var rule=_392+" { "+_393+" }";
return ds.styleSheet.insertRule(rule,_394);
}else{
if(ds.styleSheet.addRule){
return ds.styleSheet.addRule(_392,_393,_394);
}else{
return null;
}
}
};
ds.removeCssRule=function(_396){
if(!ds.styleSheet){
dojo.debug("no stylesheet defined for removing rules");
return false;
}
if(h.ie){
if(!_396){
_396=ds.styleSheet.rules.length;
ds.styleSheet.removeRule(_396);
}
}else{
if(document.styleSheets[0]){
if(!_396){
_396=ds.styleSheet.cssRules.length;
}
ds.styleSheet.deleteRule(_396);
}
}
return true;
};
ds.insertCssFile=function(URI,doc,_399){
if(!URI){
return;
}
if(!doc){
doc=document;
}
var _39a=dojo.hostenv.getText(URI);
_39a=ds.fixPathsInCssText(_39a,URI);
if(_399){
var _39b=doc.getElementsByTagName("style");
var _39c="";
for(var i=0;i<_39b.length;i++){
_39c=(_39b[i].styleSheet&&_39b[i].styleSheet.cssText)?_39b[i].styleSheet.cssText:_39b[i].innerHTML;
if(_39a==_39c){
return;
}
}
}
var _39e=ds.insertCssText(_39a);
if(_39e&&djConfig.isDebug){
_39e.setAttribute("dbgHref",URI);
}
return _39e;
};
ds.insertCssText=function(_39f,doc,URI){
if(!_39f){
return;
}
if(!doc){
doc=document;
}
if(URI){
_39f=ds.fixPathsInCssText(_39f,URI);
}
var _3a2=doc.createElement("style");
_3a2.setAttribute("type","text/css");
var head=doc.getElementsByTagName("head")[0];
if(!head){
dojo.debug("No head tag in document, aborting styles");
return;
}else{
head.appendChild(_3a2);
}
if(_3a2.styleSheet){
_3a2.styleSheet.cssText=_39f;
}else{
var _3a4=doc.createTextNode(_39f);
_3a2.appendChild(_3a4);
}
return _3a2;
};
ds.fixPathsInCssText=function(_3a5,URI){
if(!_3a5||!URI){
return;
}
var pos=0;
var str="";
var url="";
while(pos!=-1){
pos=0;
url="";
pos=_3a5.indexOf("url(",pos);
if(pos<0){
break;
}
str+=_3a5.slice(0,pos+4);
_3a5=_3a5.substring(pos+4,_3a5.length);
url+=_3a5.match(/^[\t\s\w()\/.\\'"-:#=&?]*\)/)[0];
_3a5=_3a5.substring(url.length-1,_3a5.length);
url=url.replace(/^[\s\t]*(['"]?)([\w()\/.\\'"-:#=&?]*)\1[\s\t]*?\)/,"$2");
if(url.search(/(file|https?|ftps?):\/\//)==-1){
url=(new dojo.uri.Uri(URI,url).toString());
}
str+=url;
}
return str+_3a5;
};
ds.getBackgroundColor=function(node){
node=dojo.byId(node);
var _3ab;
do{
_3ab=ds.getStyle(node,"background-color");
if(_3ab.toLowerCase()=="rgba(0, 0, 0, 0)"){
_3ab="transparent";
}
if(node==document.getElementsByTagName("body")[0]){
node=null;
break;
}
node=node.parentNode;
}while(node&&dojo.lang.inArray(_3ab,["transparent",""]));
if(_3ab=="transparent"){
_3ab=[255,255,255,0];
}else{
_3ab=dojo.graphics.color.extractRGB(_3ab);
}
return _3ab;
};
ds.getComputedStyle=function(node,_3ad,_3ae){
node=dojo.byId(node);
var _3ad=ds.toSelectorCase(_3ad);
var _3af=ds.toCamelCase(_3ad);
if(!node||!node.style){
return _3ae;
}else{
if(document.defaultView){
try{
var cs=document.defaultView.getComputedStyle(node,"");
if(cs){
return cs.getPropertyValue(_3ad);
}
}
catch(e){
if(node.style.getPropertyValue){
return node.style.getPropertyValue(_3ad);
}else{
return _3ae;
}
}
}else{
if(node.currentStyle){
return node.currentStyle[_3af];
}
}
}
if(node.style.getPropertyValue){
return node.style.getPropertyValue(_3ad);
}else{
return _3ae;
}
};
ds.getStyleProperty=function(node,_3b2){
node=dojo.byId(node);
return (node&&node.style?node.style[ds.toCamelCase(_3b2)]:undefined);
};
ds.getStyle=function(node,_3b4){
var _3b5=ds.getStyleProperty(node,_3b4);
return (_3b5?_3b5:ds.getComputedStyle(node,_3b4));
};
ds.setStyle=function(node,_3b7,_3b8){
node=dojo.byId(node);
if(node&&node.style){
var _3b9=ds.toCamelCase(_3b7);
node.style[_3b9]=_3b8;
}
};
ds.toCamelCase=function(_3ba){
var arr=_3ba.split("-"),cc=arr[0];
for(var i=1;i<arr.length;i++){
cc+=arr[i].charAt(0).toUpperCase()+arr[i].substring(1);
}
return cc;
};
ds.toSelectorCase=function(_3bd){
return _3bd.replace(/([A-Z])/g,"-$1").toLowerCase();
};
ds.setOpacity=function setOpacity(node,_3bf,_3c0){
node=dojo.byId(node);
if(!_3c0){
if(_3bf>=1){
if(h.ie){
ds.clearOpacity(node);
return;
}else{
_3bf=0.999999;
}
}else{
if(_3bf<0){
_3bf=0;
}
}
}
if(h.ie){
if(node.nodeName.toLowerCase()=="tr"){
var tds=node.getElementsByTagName("td");
for(var x=0;x<tds.length;x++){
tds[x].style.filter="Alpha(Opacity="+_3bf*100+")";
}
}
node.style.filter="Alpha(Opacity="+_3bf*100+")";
}else{
if(h.moz){
node.style.opacity=_3bf;
node.style.MozOpacity=_3bf;
}else{
if(h.safari){
node.style.opacity=_3bf;
node.style.KhtmlOpacity=_3bf;
}else{
node.style.opacity=_3bf;
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
ds.setStyleAttributes=function(node,_3c8){
var _3c9={"opacity":dojo.style.setOpacity,"content-height":dojo.style.setContentHeight,"content-width":dojo.style.setContentWidth,"outer-height":dojo.style.setOuterHeight,"outer-width":dojo.style.setOuterWidth};
var _3ca=_3c8.replace(/(;)?\s*$/,"").split(";");
for(var i=0;i<_3ca.length;i++){
var _3cc=_3ca[i].split(":");
var name=_3cc[0].replace(/\s*$/,"").replace(/^\s*/,"").toLowerCase();
var _3ce=_3cc[1].replace(/\s*$/,"").replace(/^\s*/,"");
if(dojo.lang.has(_3c9,name)){
_3c9[name](node,_3ce);
}else{
node.style[dojo.style.toCamelCase(name)]=_3ce;
}
}
};
ds._toggle=function(node,_3d0,_3d1){
node=dojo.byId(node);
_3d1(node,!_3d0(node));
return _3d0(node);
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
ds.setShowing=function(node,_3d6){
ds[(_3d6?"show":"hide")](node);
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
ds.setDisplay=function(node,_3dc){
ds.setStyle(node,"display",(dojo.lang.isString(_3dc)?_3dc:(_3dc?ds.suggestDisplayByTagName(node):"none")));
};
ds.isDisplayed=function(node){
return (ds.getComputedStyle(node,"display")!="none");
};
ds.toggleDisplay=function(node){
return ds._toggle(node,ds.isDisplayed,ds.setDisplay);
};
ds.setVisibility=function(node,_3e0){
ds.setStyle(node,"visibility",(dojo.lang.isString(_3e0)?_3e0:(_3e0?"visible":"hidden")));
};
ds.isVisible=function(node){
return (ds.getComputedStyle(node,"visibility")!="hidden");
};
ds.toggleVisibility=function(node){
return ds._toggle(node,ds.isVisible,ds.setVisibility);
};
ds.toCoordinateArray=function(_3e3,_3e4){
if(dojo.lang.isArray(_3e3)){
while(_3e3.length<4){
_3e3.push(0);
}
while(_3e3.length>4){
_3e3.pop();
}
var ret=_3e3;
}else{
var node=dojo.byId(_3e3);
var pos=ds.getAbsolutePosition(node,_3e4);
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
dojo.html.disableSelection=function(_3e8){
_3e8=dojo.byId(_3e8)||document.body;
var h=dojo.render.html;
if(h.mozilla){
_3e8.style.MozUserSelect="none";
}else{
if(h.safari){
_3e8.style.KhtmlUserSelect="none";
}else{
if(h.ie){
_3e8.unselectable="on";
}else{
return false;
}
}
}
return true;
};
dojo.html.enableSelection=function(_3ea){
_3ea=dojo.byId(_3ea)||document.body;
var h=dojo.render.html;
if(h.mozilla){
_3ea.style.MozUserSelect="";
}else{
if(h.safari){
_3ea.style.KhtmlUserSelect="";
}else{
if(h.ie){
_3ea.unselectable="off";
}else{
return false;
}
}
}
return true;
};
dojo.html.selectElement=function(_3ec){
_3ec=dojo.byId(_3ec);
if(document.selection&&document.body.createTextRange){
var _3ed=document.body.createTextRange();
_3ed.moveToElementText(_3ec);
_3ed.select();
}else{
if(window["getSelection"]){
var _3ee=window.getSelection();
if(_3ee["selectAllChildren"]){
_3ee.selectAllChildren(_3ec);
}
}
}
};
dojo.html.selectInputText=function(_3ef){
_3ef=dojo.byId(_3ef);
if(document.selection&&document.body.createTextRange){
var _3f0=_3ef.createTextRange();
_3f0.moveStart("character",0);
_3f0.moveEnd("character",_3ef.value.length);
_3f0.select();
}else{
if(window["getSelection"]){
var _3f1=window.getSelection();
_3ef.setSelectionRange(0,_3ef.value.length);
}
}
_3ef.focus();
};
dojo.html.isSelectionCollapsed=function(){
if(document["selection"]){
return document.selection.createRange().text=="";
}else{
if(window["getSelection"]){
var _3f2=window.getSelection();
if(dojo.lang.isString(_3f2)){
return _3f2=="";
}else{
return _3f2.isCollapsed;
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
var _3fd=dojo.byId(node);
type=type.toLowerCase();
while((_3fd)&&(_3fd.nodeName.toLowerCase()!=type)){
if(_3fd==(document["body"]||document["documentElement"])){
return null;
}
_3fd=_3fd.parentNode;
}
return _3fd;
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
dojo.html.hasClass=function(node,_409){
return dojo.lang.inArray(dojo.html.getClasses(node),_409);
};
dojo.html.prependClass=function(node,_40b){
_40b+=" "+dojo.html.getClass(node);
return dojo.html.setClass(node,_40b);
};
dojo.html.addClass=function(node,_40d){
if(dojo.html.hasClass(node,_40d)){
return false;
}
_40d=dojo.string.trim(dojo.html.getClass(node)+" "+_40d);
return dojo.html.setClass(node,_40d);
};
dojo.html.setClass=function(node,_40f){
node=dojo.byId(node);
var cs=new String(_40f);
try{
if(typeof node.className=="string"){
node.className=cs;
}else{
if(node.setAttribute){
node.setAttribute("class",_40f);
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
dojo.html.removeClass=function(node,_412,_413){
var _412=dojo.string.trim(new String(_412));
try{
var cs=dojo.html.getClasses(node);
var nca=[];
if(_413){
for(var i=0;i<cs.length;i++){
if(cs[i].indexOf(_412)==-1){
nca.push(cs[i]);
}
}
}else{
for(var i=0;i<cs.length;i++){
if(cs[i]!=_412){
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
dojo.html.replaceClass=function(node,_418,_419){
dojo.html.removeClass(node,_419);
dojo.html.addClass(node,_418);
};
dojo.html.classMatchType={ContainsAll:0,ContainsAny:1,IsOnly:2};
dojo.html.getElementsByClass=function(_41a,_41b,_41c,_41d,_41e){
_41b=dojo.byId(_41b)||document;
var _41f=_41a.split(/\s+/g);
var _420=[];
if(_41d!=1&&_41d!=2){
_41d=0;
}
var _421=new RegExp("(\\s|^)(("+_41f.join(")|(")+"))(\\s|$)");
var _422=[];
if(!_41e&&document.evaluate){
var _423="//"+(_41c||"*")+"[contains(";
if(_41d!=dojo.html.classMatchType.ContainsAny){
_423+="concat(' ',@class,' '), ' "+_41f.join(" ') and contains(concat(' ',@class,' '), ' ")+" ')]";
}else{
_423+="concat(' ',@class,' '), ' "+_41f.join(" ')) or contains(concat(' ',@class,' '), ' ")+" ')]";
}
var _424=document.evaluate(_423,_41b,null,XPathResult.ANY_TYPE,null);
var _425=_424.iterateNext();
while(_425){
try{
_422.push(_425);
_425=_424.iterateNext();
}
catch(e){
break;
}
}
return _422;
}else{
if(!_41c){
_41c="*";
}
_422=_41b.getElementsByTagName(_41c);
var node,i=0;
outer:
while(node=_422[i++]){
var _427=dojo.html.getClasses(node);
if(_427.length==0){
continue outer;
}
var _428=0;
for(var j=0;j<_427.length;j++){
if(_421.test(_427[j])){
if(_41d==dojo.html.classMatchType.ContainsAny){
_420.push(node);
continue outer;
}else{
_428++;
}
}else{
if(_41d==dojo.html.classMatchType.IsOnly){
continue outer;
}
}
}
if(_428==_41f.length){
if((_41d==dojo.html.classMatchType.IsOnly)&&(_428==_427.length)){
_420.push(node);
}else{
if(_41d==dojo.html.classMatchType.ContainsAll){
_420.push(node);
}
}
}
}
return _420;
}
};
dojo.html.getElementsByClassName=dojo.html.getElementsByClass;
dojo.html.getCursorPosition=function(e){
e=e||window.event;
var _42b={x:0,y:0};
if(e.pageX||e.pageY){
_42b.x=e.pageX;
_42b.y=e.pageY;
}else{
var de=document.documentElement;
var db=document.body;
_42b.x=e.clientX+((de||db)["scrollLeft"])-((de||db)["clientLeft"]);
_42b.y=e.clientY+((de||db)["scrollTop"])-((de||db)["clientTop"]);
}
return _42b;
};
dojo.html.overElement=function(_42e,e){
_42e=dojo.byId(_42e);
var _430=dojo.html.getCursorPosition(e);
with(dojo.html){
var top=getAbsoluteY(_42e,true);
var _432=top+getInnerHeight(_42e);
var left=getAbsoluteX(_42e,true);
var _434=left+getInnerWidth(_42e);
}
return (_430.x>=left&&_430.x<=_434&&_430.y>=top&&_430.y<=_432);
};
dojo.html.setActiveStyleSheet=function(_435){
var i=0,a,els=document.getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")){
a.disabled=true;
if(a.getAttribute("title")==_435){
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
dojo.html.copyStyle=function(_43c,_43d){
if(dojo.lang.isUndefined(_43d.style.cssText)){
_43c.setAttribute("style",_43d.getAttribute("style"));
}else{
_43c.style.cssText=_43d.style.cssText;
}
dojo.html.addClass(_43c,dojo.html.getClass(_43d));
};
dojo.html._callExtrasDeprecated=function(_43e,args){
var _440="dojo.html.extras";
dojo.deprecated("dojo.html."+_43e,"moved to "+_440,"0.4");
dojo["require"](_440);
return dojo.html[_43e].apply(dojo.html,args);
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
dojo.lang.extend(dojo.AdapterRegistry,{register:function(name,_442,wrap,_444){
if(_444){
this.pairs.unshift([name,_442,wrap]);
}else{
this.pairs.push([name,_442,wrap]);
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
dojo.json={jsonRegistry:new dojo.AdapterRegistry(),register:function(name,_44b,wrap,_44d){
dojo.json.jsonRegistry.register(name,_44b,wrap,_44d);
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
var _451=typeof (o);
if(_451=="undefined"){
return "undefined";
}else{
if((_451=="number")||(_451=="boolean")){
return o+"";
}else{
if(o===null){
return "null";
}
}
}
if(_451=="string"){
return dojo.string.escapeString(o);
}
var me=arguments.callee;
var _453;
if(typeof (o.__json__)=="function"){
_453=o.__json__();
if(o!==_453){
return me(_453);
}
}
if(typeof (o.json)=="function"){
_453=o.json();
if(o!==_453){
return me(_453);
}
}
if(_451!="function"&&typeof (o.length)=="number"){
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
_453=dojo.json.jsonRegistry.match(o);
return me(_453);
}
catch(e){
}
if(_451=="function"){
return null;
}
res=[];
for(var k in o){
var _458;
if(typeof (k)=="number"){
_458="\""+k+"\"";
}else{
if(typeof (k)=="string"){
_458=dojo.string.escapeString(k);
}else{
continue;
}
}
val=me(o[k]);
if(typeof (val)!="string"){
continue;
}
res.push(_458+":"+val);
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
var _45a=node.tagName;
if(_45a.substr(0,5).toLowerCase()!="dojo:"){
if(_45a.substr(0,4).toLowerCase()=="dojo"){
return "dojo:"+_45a.substring(4).toLowerCase();
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
var _45c=node.className||node.getAttribute("class");
if(_45c&&_45c.indexOf&&_45c.indexOf("dojo-")!=-1){
var _45d=_45c.split(" ");
for(var x=0;x<_45d.length;x++){
if(_45d[x].length>5&&_45d[x].indexOf("dojo-")>=0){
return "dojo:"+_45d[x].substr(5).toLowerCase();
}
}
}
}
}
return _45a.toLowerCase();
}
this.parseElement=function(node,_460,_461,_462){
if(node.getAttribute("parseWidgets")=="false"){
return {};
}
var _463={};
var _464=getDojoTagName(node);
_463[_464]=[];
if((!_461)||(_464.substr(0,4).toLowerCase()=="dojo")){
var _465=parseAttributes(node);
for(var attr in _465){
if((!_463[_464][attr])||(typeof _463[_464][attr]!="array")){
_463[_464][attr]=[];
}
_463[_464][attr].push(_465[attr]);
}
_463[_464].nodeRef=node;
_463.tagName=_464;
_463.index=_462||0;
}
var _467=0;
var tcn,i=0,nodes=node.childNodes;
while(tcn=nodes[i++]){
switch(tcn.nodeType){
case dojo.dom.ELEMENT_NODE:
_467++;
var ctn=getDojoTagName(tcn);
if(!_463[ctn]){
_463[ctn]=[];
}
_463[ctn].push(this.parseElement(tcn,true,_461,_467));
if((tcn.childNodes.length==1)&&(tcn.childNodes.item(0).nodeType==dojo.dom.TEXT_NODE)){
_463[ctn][_463[ctn].length-1].value=tcn.childNodes.item(0).nodeValue;
}
break;
case dojo.dom.TEXT_NODE:
if(node.childNodes.length==1){
_463[_464].push({value:node.childNodes.item(0).nodeValue});
}
break;
default:
break;
}
}
return _463;
};
function parseAttributes(node){
var _46b={};
var atts=node.attributes;
var _46d,i=0;
while(_46d=atts[i++]){
if((dojo.render.html.capable)&&(dojo.render.html.ie)){
if(!_46d){
continue;
}
if((typeof _46d=="object")&&(typeof _46d.nodeValue=="undefined")||(_46d.nodeValue==null)||(_46d.nodeValue=="")){
continue;
}
}
var nn=(_46d.nodeName.indexOf("dojo:")==-1)?_46d.nodeName:_46d.nodeName.split("dojo:")[1];
_46b[nn]={value:_46d.nodeValue};
}
return _46b;
}
};
dojo.provide("dojo.lang.declare");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.extras");
dojo.lang.declare=function(_46f,_470,init,_472){
if((dojo.lang.isFunction(_472))||((!_472)&&(!dojo.lang.isFunction(init)))){
var temp=_472;
_472=init;
init=temp;
}
var _474=[];
if(dojo.lang.isArray(_470)){
_474=_470;
_470=_474.shift();
}
if(!init){
init=dojo.evalObjPath(_46f,false);
if((init)&&(!dojo.lang.isFunction(init))){
init=null;
}
}
var ctor=dojo.lang.declare._makeConstructor();
var scp=(_470?_470.prototype:null);
if(scp){
scp.prototyping=true;
ctor.prototype=new _470();
scp.prototyping=false;
}
ctor.superclass=scp;
ctor.mixins=_474;
for(var i=0,l=_474.length;i<l;i++){
dojo.lang.extend(ctor,_474[i].prototype);
}
ctor.prototype.declaredClass=_46f;
_472=(_472)||({});
if(_472){
dojo.lang.extend(ctor,_472);
}
dojo.lang.extend(ctor,dojo.lang.declare.base);
ctor.prototype.constructor=ctor;
ctor.prototype.initializer=(_472.initializer)||(init)||(function(){
});
dojo.lang.setObjPathValue(_46f,ctor,null,true);
};
dojo.lang.declare._makeConstructor=function(){
return function(){
var self=this._getPropContext();
var s=self.constructor.superclass;
if((s)&&(s.constructor)){
if(s.constructor==arguments.callee){
this.inherited("constructor",arguments);
}else{
this._inherited(s,"constructor",arguments);
}
}
var m=(self.constructor.mixins)||([]);
for(var i=0,l=m.length;i<l;i++){
(((m[i].prototype)&&(m[i].prototype.initializer))||(m[i])).apply(this,arguments);
}
if((!this.prototyping)&&(self.initializer)){
self.initializer.apply(this,arguments);
}
};
};
dojo.lang.declare.base={_getPropContext:function(){
return (this.___proto||this);
},_inherited:function(_47c,_47d,args){
var _47f=this.___proto;
this.___proto=_47c;
var _480=_47c[_47d].apply(this,(args||[]));
this.___proto=_47f;
return _480;
},inheritedFrom:function(ctor,prop,args){
var p=((ctor)&&(ctor.prototype)&&(ctor.prototype[prop]));
return (dojo.lang.isFunction(p)?p.apply(this,args):p);
},inherited:function(prop,args){
var p=this._getPropContext();
do{
if((!p.constructor)||(!p.constructor.superclass)){
return;
}
p=p.constructor.superclass;
}while(!(prop in p));
return (dojo.lang.isFunction(p[prop])?this._inherited(p,prop,args):p[prop]);
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
var _488={};
var _489=[];
this.getUniqueId=function(_48a){
return _48a+"_"+(_488[_48a]!=undefined?++_488[_48a]:_488[_48a]=0);
};
this.add=function(_48b){
dojo.profile.start("dojo.widget.manager.add");
this.widgets.push(_48b);
if(!_48b.extraArgs["id"]){
_48b.extraArgs["id"]=_48b.extraArgs["ID"];
}
if(_48b.widgetId==""){
if(_48b["id"]){
_48b.widgetId=_48b["id"];
}else{
if(_48b.extraArgs["id"]){
_48b.widgetId=_48b.extraArgs["id"];
}else{
_48b.widgetId=this.getUniqueId(_48b.widgetType);
}
}
}
if(this.widgetIds[_48b.widgetId]){
dojo.debug("widget ID collision on ID: "+_48b.widgetId);
}
this.widgetIds[_48b.widgetId]=_48b;
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
this.remove=function(_48d){
var tw=this.widgets[_48d].widgetId;
delete this.widgetIds[tw];
this.widgets.splice(_48d,1);
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
this.getWidgetsByFilter=function(_497,_498){
var ret=[];
dojo.lang.every(this.widgets,function(x){
if(_497(x)){
ret.push(x);
if(_498){
return false;
}
}
return true;
});
return (_498?ret[0]:ret);
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
var _49e={};
var _49f=["dojo.widget"];
for(var i=0;i<_49f.length;i++){
_49f[_49f[i]]=true;
}
this.registerWidgetPackage=function(_4a1){
if(!_49f[_4a1]){
_49f[_4a1]=true;
_49f.push(_4a1);
}
};
this.getWidgetPackageList=function(){
return dojo.lang.map(_49f,function(elt){
return (elt!==true?elt:undefined);
});
};
this.getImplementation=function(_4a3,_4a4,_4a5){
var impl=this.getImplementationName(_4a3);
if(impl){
var ret=new impl(_4a4);
return ret;
}
};
this.getImplementationName=function(_4a8){
var _4a9=_4a8.toLowerCase();
var impl=_49e[_4a9];
if(impl){
return impl;
}
if(!_489.length){
for(var _4ab in dojo.render){
if(dojo.render[_4ab]["capable"]===true){
var _4ac=dojo.render[_4ab].prefixes;
for(var i=0;i<_4ac.length;i++){
_489.push(_4ac[i].toLowerCase());
}
}
}
_489.push("");
}
for(var i=0;i<_49f.length;i++){
var _4ae=dojo.evalObjPath(_49f[i]);
if(!_4ae){
continue;
}
for(var j=0;j<_489.length;j++){
if(!_4ae[_489[j]]){
continue;
}
for(var _4b0 in _4ae[_489[j]]){
if(_4b0.toLowerCase()!=_4a9){
continue;
}
_49e[_4a9]=_4ae[_489[j]][_4b0];
return _49e[_4a9];
}
}
for(var j=0;j<_489.length;j++){
for(var _4b0 in _4ae){
if(_4b0.toLowerCase()!=(_489[j]+_4a9)){
continue;
}
_49e[_4a9]=_4ae[_4b0];
return _49e[_4a9];
}
}
}
throw new Error("Could not locate \""+_4a8+"\" class");
};
this.resizing=false;
this.onWindowResized=function(){
if(this.resizing){
return;
}
try{
this.resizing=true;
for(var id in this.topWidgets){
var _4b2=this.topWidgets[id];
if(_4b2.checkSize){
_4b2.checkSize();
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
var g=function(_4b7,_4b8){
dw[(_4b8||_4b7)]=h(_4b7);
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
var _4ba=dwm.getAllWidgets.apply(dwm,arguments);
if(arguments.length>0){
return _4ba[n];
}
return _4ba;
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
var _4bc=this.children[i];
if(_4bc.onResized){
_4bc.onResized();
}
}
},create:function(args,_4be,_4bf){
this.satisfyPropertySets(args,_4be,_4bf);
this.mixInProperties(args,_4be,_4bf);
this.postMixInProperties(args,_4be,_4bf);
dojo.widget.manager.add(this);
this.buildRendering(args,_4be,_4bf);
this.initialize(args,_4be,_4bf);
this.postInitialize(args,_4be,_4bf);
this.postCreate(args,_4be,_4bf);
return this;
},destroy:function(_4c0){
this.destroyChildren();
this.uninitialize();
this.destroyRendering(_4c0);
dojo.widget.manager.removeById(this.widgetId);
},destroyChildren:function(){
while(this.children.length>0){
var tc=this.children[0];
this.removeChild(tc);
tc.destroy();
}
},getChildrenOfType:function(type,_4c3){
var ret=[];
var _4c5=dojo.lang.isFunction(type);
if(!_4c5){
type=type.toLowerCase();
}
for(var x=0;x<this.children.length;x++){
if(_4c5){
if(this.children[x] instanceof type){
ret.push(this.children[x]);
}
}else{
if(this.children[x].widgetType.toLowerCase()==type){
ret.push(this.children[x]);
}
}
if(_4c3){
ret=ret.concat(this.children[x].getChildrenOfType(type,_4c3));
}
}
return ret;
},getDescendants:function(){
var _4c7=[];
var _4c8=[this];
var elem;
while(elem=_4c8.pop()){
_4c7.push(elem);
dojo.lang.forEach(elem.children,function(elem){
_4c8.push(elem);
});
}
return _4c7;
},satisfyPropertySets:function(args){
return args;
},mixInProperties:function(args,frag){
if((args["fastMixIn"])||(frag["fastMixIn"])){
for(var x in args){
this[x]=args[x];
}
return;
}
var _4cf;
var _4d0=dojo.widget.lcArgsCache[this.widgetType];
if(_4d0==null){
_4d0={};
for(var y in this){
_4d0[((new String(y)).toLowerCase())]=y;
}
dojo.widget.lcArgsCache[this.widgetType]=_4d0;
}
var _4d2={};
for(var x in args){
if(!this[x]){
var y=_4d0[(new String(x)).toLowerCase()];
if(y){
args[y]=args[x];
x=y;
}
}
if(_4d2[x]){
continue;
}
_4d2[x]=true;
if((typeof this[x])!=(typeof _4cf)){
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
var _4d4=args[x].split(";");
for(var y=0;y<_4d4.length;y++){
var si=_4d4[y].indexOf(":");
if((si!=-1)&&(_4d4[y].length>si)){
this[x][_4d4[y].substr(0,si).replace(/^\s+|\s+$/g,"")]=_4d4[y].substr(si+1);
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
},addedTo:function(_4dc){
},addChild:function(_4dd){
dojo.unimplemented("dojo.widget.Widget.addChild");
return false;
},removeChild:function(_4de){
for(var x=0;x<this.children.length;x++){
if(this.children[x]===_4de){
this.children.splice(x,1);
break;
}
}
return _4de;
},resize:function(_4e0,_4e1){
this.setWidth(_4e0);
this.setHeight(_4e1);
},setWidth:function(_4e2){
if((typeof _4e2=="string")&&(_4e2.substr(-1)=="%")){
this.setPercentageWidth(_4e2);
}else{
this.setNativeWidth(_4e2);
}
},setHeight:function(_4e3){
if((typeof _4e3=="string")&&(_4e3.substr(-1)=="%")){
this.setPercentageHeight(_4e3);
}else{
this.setNativeHeight(_4e3);
}
},setPercentageHeight:function(_4e4){
return false;
},setNativeHeight:function(_4e5){
return false;
},setPercentageWidth:function(_4e6){
return false;
},setNativeWidth:function(_4e7){
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
var _4eb=type.toLowerCase();
this[_4eb]=function(_4ec,_4ed,_4ee,_4ef,_4f0){
return dojo.widget.buildWidgetFromParseTree(_4eb,_4ec,_4ed,_4ee,_4ef,_4f0);
};
};
dojo.widget.tags.addParseTreeHandler("dojo:widget");
dojo.widget.tags["dojo:propertyset"]=function(_4f1,_4f2,_4f3){
var _4f4=_4f2.parseProperties(_4f1["dojo:propertyset"]);
};
dojo.widget.tags["dojo:connect"]=function(_4f5,_4f6,_4f7){
var _4f8=_4f6.parseProperties(_4f5["dojo:connect"]);
};
dojo.widget.buildWidgetFromParseTree=function(type,frag,_4fb,_4fc,_4fd,_4fe){
var _4ff=type.split(":");
_4ff=(_4ff.length==2)?_4ff[1]:type;
var _500=_4fe||_4fb.parseProperties(frag["dojo:"+_4ff]);
var _501=dojo.widget.manager.getImplementation(_4ff);
if(!_501){
throw new Error("cannot find \""+_4ff+"\" widget");
}else{
if(!_501.create){
throw new Error("\""+_4ff+"\" widget object does not appear to implement *Widget");
}
}
_500["dojoinsertionindex"]=_4fd;
var ret=_501.create(_500,frag,_4fc);
return ret;
};
dojo.widget.defineWidget=function(_503,_504,_505,init,_507){
if(dojo.lang.isString(arguments[3])){
dojo.widget._defineWidget(arguments[0],arguments[3],arguments[1],arguments[4],arguments[2]);
}else{
var args=[arguments[0]],p=3;
if(dojo.lang.isString(arguments[1])){
args.push(arguments[1],arguments[2]);
}else{
args.push("",arguments[1]);
p=2;
}
if(dojo.lang.isFunction(arguments[p])){
args.push(arguments[p],arguments[p+1]);
}else{
args.push(null,arguments[p]);
}
dojo.widget._defineWidget.apply(this,args);
}
};
dojo.widget.defineWidget.renderers="html|svg|vml";
dojo.widget._defineWidget=function(_509,_50a,_50b,init,_50d){
var _50e=_509.split(".");
var type=_50e.pop();
var regx="\\.("+(_50a?_50a+"|":"")+dojo.widget.defineWidget.renderers+")\\.";
var r=_509.search(new RegExp(regx));
_50e=(r<0?_50e.join("."):_509.substr(0,r));
dojo.widget.manager.registerWidgetPackage(_50e);
dojo.widget.tags.addParseTreeHandler("dojo:"+type.toLowerCase());
_50d=(_50d)||{};
_50d.widgetType=type;
if((!init)&&(_50d["classConstructor"])){
init=_50d.classConstructor;
delete _50d.classConstructor;
}
dojo.declare(_509,_50b,init,_50d);
};
dojo.provide("dojo.widget.Parse");
dojo.require("dojo.widget.Manager");
dojo.require("dojo.dom");
dojo.widget.Parse=function(_512){
this.propertySetsList=[];
this.fragment=_512;
this.createComponents=function(frag,_514){
var _515=[];
var _516=false;
try{
if((frag)&&(frag["tagName"])&&(frag!=frag["nodeRef"])){
var _517=dojo.widget.tags;
var tna=String(frag["tagName"]).split(";");
for(var x=0;x<tna.length;x++){
var ltn=(tna[x].replace(/^\s+|\s+$/g,"")).toLowerCase();
if(_517[ltn]){
_516=true;
frag.tagName=ltn;
var ret=_517[ltn](frag,this,_514,frag["index"]);
_515.push(ret);
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
if(!_516){
_515=_515.concat(this.createSubComponents(frag,_514));
}
return _515;
};
this.createSubComponents=function(_51c,_51d){
var frag,comps=[];
for(var item in _51c){
frag=_51c[item];
if((frag)&&(typeof frag=="object")&&(frag!=_51c.nodeRef)&&(frag!=_51c["tagName"])){
comps=comps.concat(this.createComponents(frag,_51d));
}
}
return comps;
};
this.parsePropertySets=function(_520){
return [];
var _521=[];
for(var item in _520){
if((_520[item]["tagName"]=="dojo:propertyset")){
_521.push(_520[item]);
}
}
this.propertySetsList.push(_521);
return _521;
};
this.parseProperties=function(_523){
var _524={};
for(var item in _523){
if((_523[item]==_523["tagName"])||(_523[item]==_523.nodeRef)){
}else{
if((_523[item]["tagName"])&&(dojo.widget.tags[_523[item].tagName.toLowerCase()])){
}else{
if((_523[item][0])&&(_523[item][0].value!="")&&(_523[item][0].value!=null)){
try{
if(item.toLowerCase()=="dataprovider"){
var _526=this;
this.getDataProvider(_526,_523[item][0].value);
_524.dataProvider=this.dataProvider;
}
_524[item]=_523[item][0].value;
var _527=this.parseProperties(_523[item]);
for(var _528 in _527){
_524[_528]=_527[_528];
}
}
catch(e){
dojo.debug(e);
}
}
}
}
}
return _524;
};
this.getDataProvider=function(_529,_52a){
dojo.io.bind({url:_52a,load:function(type,_52c){
if(type=="load"){
_529.dataProvider=_52c;
}
},mimetype:"text/javascript",sync:true});
};
this.getPropertySetById=function(_52d){
for(var x=0;x<this.propertySetsList.length;x++){
if(_52d==this.propertySetsList[x]["id"][0].value){
return this.propertySetsList[x];
}
}
return "";
};
this.getPropertySetsByType=function(_52f){
var _530=[];
for(var x=0;x<this.propertySetsList.length;x++){
var cpl=this.propertySetsList[x];
var cpcc=cpl["componentClass"]||cpl["componentType"]||null;
if((cpcc)&&(propertySetId==cpcc[0].value)){
_530.push(cpl);
}
}
return _530;
};
this.getPropertySets=function(_534){
var ppl="dojo:propertyproviderlist";
var _536=[];
var _537=_534["tagName"];
if(_534[ppl]){
var _538=_534[ppl].value.split(" ");
for(var _539 in _538){
if((_539.indexOf("..")==-1)&&(_539.indexOf("://")==-1)){
var _53a=this.getPropertySetById(_539);
if(_53a!=""){
_536.push(_53a);
}
}else{
}
}
}
return (this.getPropertySetsByType(_537)).concat(_536);
};
this.createComponentFromScript=function(_53b,_53c,_53d){
var ltn="dojo:"+_53c.toLowerCase();
if(dojo.widget.tags[ltn]){
_53d.fastMixIn=true;
return [dojo.widget.tags[ltn](_53d,this,null,null,_53d)];
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
dojo.widget.createWidget=function(name,_541,_542,_543){
var _544=name.toLowerCase();
var _545="dojo:"+_544;
var _546=(dojo.byId(name)&&(!dojo.widget.tags[_545]));
if((arguments.length==1)&&((typeof name!="string")||(_546))){
var xp=new dojo.xml.Parse();
var tn=(_546)?dojo.byId(name):name;
return dojo.widget.getParser().createComponents(xp.parseElement(tn,null,true))[0];
}
function fromScript(_549,name,_54b){
_54b[_545]={dojotype:[{value:_544}],nodeRef:_549,fastMixIn:true};
return dojo.widget.getParser().createComponentFromScript(_549,name,_54b,true);
}
if(typeof name!="string"&&typeof _541=="string"){
dojo.deprecated("dojo.widget.createWidget","argument order is now of the form "+"dojo.widget.createWidget(NAME, [PROPERTIES, [REFERENCENODE, [POSITION]]])","0.4");
return fromScript(name,_541,_542);
}
_541=_541||{};
var _54c=false;
var tn=null;
var h=dojo.render.html.capable;
if(h){
tn=document.createElement("span");
}
if(!_542){
_54c=true;
_542=tn;
if(h){
document.body.appendChild(_542);
}
}else{
if(_543){
dojo.dom.insertAtPosition(tn,_542,_543);
}else{
tn=_542;
}
}
var _54e=fromScript(tn,name,_541);
if(!_54e||!_54e[0]||typeof _54e[0].widgetType=="undefined"){
throw new Error("createWidget: Creation of \""+name+"\" widget failed.");
}
if(_54c){
if(_54e[0].domNode.parentNode){
_54e[0].domNode.parentNode.removeChild(_54e[0].domNode);
}
}
return _54e[0];
};
dojo.widget.fromScript=function(name,_550,_551,_552){
dojo.deprecated("dojo.widget.fromScript"," use "+"dojo.widget.createWidget instead","0.4");
return dojo.widget.createWidget(name,_550,_551,_552);
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
dojo.widget.fillFromTemplateCache=function(obj,_554,_555,_556,_557){
var _558=_554||obj.templatePath;
var _559=_555||obj.templateCssPath;
if(_558&&!(_558 instanceof dojo.uri.Uri)){
_558=dojo.uri.dojoUri(_558);
dojo.deprecated("templatePath should be of type dojo.uri.Uri",null,"0.4");
}
if(_559&&!(_559 instanceof dojo.uri.Uri)){
_559=dojo.uri.dojoUri(_559);
dojo.deprecated("templateCssPath should be of type dojo.uri.Uri",null,"0.4");
}
var _55a=dojo.widget._templateCache;
if(!obj["widgetType"]){
do{
var _55b="__dummyTemplate__"+dojo.widget._templateCache.dummyCount++;
}while(_55a[_55b]);
obj.widgetType=_55b;
}
var wt=obj.widgetType;
if(_559&&!dojo.widget._cssFiles[_559.toString()]){
if((!obj.templateCssString)&&(_559)){
obj.templateCssString=dojo.hostenv.getText(_559);
obj.templateCssPath=null;
}
if((obj["templateCssString"])&&(!obj.templateCssString["loaded"])){
dojo.style.insertCssText(obj.templateCssString,null,_559);
if(!obj.templateCssString){
obj.templateCssString="";
}
obj.templateCssString.loaded=true;
}
dojo.widget._cssFiles[_559.toString()]=true;
}
var ts=_55a[wt];
if(!ts){
_55a[wt]={"string":null,"node":null};
if(_557){
ts={};
}else{
ts=_55a[wt];
}
}
if((!obj.templateString)&&(!_557)){
obj.templateString=_556||ts["string"];
}
if((!obj.templateNode)&&(!_557)){
obj.templateNode=ts["node"];
}
if((!obj.templateNode)&&(!obj.templateString)&&(_558)){
var _55e=dojo.hostenv.getText(_558);
if(_55e){
_55e=_55e.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im,"");
var _55f=_55e.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
if(_55f){
_55e=_55f[1];
}
}else{
_55e="";
}
obj.templateString=_55e;
if(!_557){
_55a[wt]["string"]=_55e;
}
}
if((!ts["string"])&&(!_557)){
ts.string=obj.templateString;
}
};
dojo.widget._templateCache.dummyCount=0;
dojo.widget.attachProperties=["dojoAttachPoint","id"];
dojo.widget.eventAttachProperty="dojoAttachEvent";
dojo.widget.onBuildProperty="dojoOnBuild";
dojo.widget.waiNames=["waiRole","waiState"];
dojo.widget.wai={waiRole:{name:"waiRole",namespace:"http://www.w3.org/TR/xhtml2",alias:"x2",prefix:"wairole:",nsName:"role"},waiState:{name:"waiState",namespace:"http://www.w3.org/2005/07/aaa",alias:"aaa",prefix:"",nsName:"state"},setAttr:function(node,attr,_562){
if(dojo.render.html.ie){
node.setAttribute(this[attr].alias+":"+this[attr].nsName,this[attr].prefix+_562);
}else{
node.setAttributeNS(this[attr].namespace,this[attr].nsName,this[attr].prefix+_562);
}
}};
dojo.widget.attachTemplateNodes=function(_563,_564,_565){
var _566=dojo.dom.ELEMENT_NODE;
function trim(str){
return str.replace(/^\s+|\s+$/g,"");
}
if(!_563){
_563=_564.domNode;
}
if(_563.nodeType!=_566){
return;
}
var _568=_563.all||_563.getElementsByTagName("*");
var _569=_564;
for(var x=-1;x<_568.length;x++){
var _56b=(x==-1)?_563:_568[x];
var _56c=[];
for(var y=0;y<this.attachProperties.length;y++){
var _56e=_56b.getAttribute(this.attachProperties[y]);
if(_56e){
_56c=_56e.split(";");
for(var z=0;z<_56c.length;z++){
if(dojo.lang.isArray(_564[_56c[z]])){
_564[_56c[z]].push(_56b);
}else{
_564[_56c[z]]=_56b;
}
}
break;
}
}
var _570=_56b.getAttribute(this.templateProperty);
if(_570){
_564[_570]=_56b;
}
dojo.lang.forEach(dojo.widget.waiNames,function(name){
var wai=dojo.widget.wai[name];
var val=_56b.getAttribute(wai.name);
if(val){
dojo.widget.wai.setAttr(_56b,wai.name,val);
}
},this);
var _574=_56b.getAttribute(this.eventAttachProperty);
if(_574){
var evts=_574.split(";");
for(var y=0;y<evts.length;y++){
if((!evts[y])||(!evts[y].length)){
continue;
}
var _576=null;
var tevt=trim(evts[y]);
if(evts[y].indexOf(":")>=0){
var _578=tevt.split(":");
tevt=trim(_578[0]);
_576=trim(_578[1]);
}
if(!_576){
_576=tevt;
}
var tf=function(){
var ntf=new String(_576);
return function(evt){
if(_569[ntf]){
_569[ntf](dojo.event.browser.fixEvent(evt,this));
}
};
}();
dojo.event.browser.addListener(_56b,tevt,tf,false,true);
}
}
for(var y=0;y<_565.length;y++){
var _57c=_56b.getAttribute(_565[y]);
if((_57c)&&(_57c.length)){
var _576=null;
var _57d=_565[y].substr(4);
_576=trim(_57c);
var _57e=[_576];
if(_576.indexOf(";")>=0){
_57e=dojo.lang.map(_576.split(";"),trim);
}
for(var z=0;z<_57e.length;z++){
if(!_57e[z].length){
continue;
}
var tf=function(){
var ntf=new String(_57e[z]);
return function(evt){
if(_569[ntf]){
_569[ntf](dojo.event.browser.fixEvent(evt,this));
}
};
}();
dojo.event.browser.addListener(_56b,_57d,tf,false,true);
}
}
}
var _581=_56b.getAttribute(this.onBuildProperty);
if(_581){
eval("var node = baseNode; var widget = targetObj; "+_581);
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
},templateNode:null,templateString:null,templateCssString:null,preventClobber:false,domNode:null,containerNode:null,addChild:function(_589,_58a,pos,ref,_58d){
if(!this.isContainer){
dojo.debug("dojo.widget.DomWidget.addChild() attempted on non-container widget");
return null;
}else{
this.addWidgetAsDirectChild(_589,_58a,pos,ref,_58d);
this.registerChild(_589,_58d);
}
return _589;
},addWidgetAsDirectChild:function(_58e,_58f,pos,ref,_592){
if((!this.containerNode)&&(!_58f)){
this.containerNode=this.domNode;
}
var cn=(_58f)?_58f:this.containerNode;
if(!pos){
pos="after";
}
if(!ref){
if(!cn){
cn=document.body;
}
ref=cn.lastChild;
}
if(!_592){
_592=0;
}
_58e.domNode.setAttribute("dojoinsertionindex",_592);
if(!ref){
cn.appendChild(_58e.domNode);
}else{
if(pos=="insertAtIndex"){
dojo.dom.insertAtIndex(_58e.domNode,ref.parentNode,_592);
}else{
if((pos=="after")&&(ref===cn.lastChild)){
cn.appendChild(_58e.domNode);
}else{
dojo.dom.insertAtPosition(_58e.domNode,cn,pos);
}
}
}
},registerChild:function(_594,_595){
_594.dojoInsertionIndex=_595;
var idx=-1;
for(var i=0;i<this.children.length;i++){
if(this.children[i].dojoInsertionIndex<_595){
idx=i;
}
}
this.children.splice(idx+1,0,_594);
_594.parent=this;
_594.addedTo(this);
delete dojo.widget.manager.topWidgets[_594.widgetId];
},removeChild:function(_598){
dojo.dom.removeNode(_598.domNode);
return dojo.widget.DomWidget.superclass.removeChild.call(this,_598);
},getFragNodeRef:function(frag){
if(!frag||!frag["dojo:"+this.widgetType.toLowerCase()]){
dojo.raise("Error: no frag for widget type "+this.widgetType+", id "+this.widgetId+" (maybe a widget has set it's type incorrectly)");
}
return (frag?frag["dojo:"+this.widgetType.toLowerCase()]["nodeRef"]:null);
},postInitialize:function(args,frag,_59c){
var _59d=this.getFragNodeRef(frag);
if(_59c&&(_59c.snarfChildDomOutput||!_59d)){
_59c.addWidgetAsDirectChild(this,"","insertAtIndex","",args["dojoinsertionindex"],_59d);
}else{
if(_59d){
if(this.domNode&&(this.domNode!==_59d)){
var _59e=_59d.parentNode.replaceChild(this.domNode,_59d);
}
}
}
if(_59c){
_59c.registerChild(this,args.dojoinsertionindex);
}else{
dojo.widget.manager.topWidgets[this.widgetId]=this;
}
if(this.isContainer){
var _59f=dojo.widget.getParser();
_59f.createSubComponents(frag,this);
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
var _5a5=false;
if(args["templatecsspath"]){
args["templateCssPath"]=args["templatecsspath"];
}
if(args["templatepath"]){
_5a5=true;
args["templatePath"]=args["templatepath"];
}
dojo.widget.fillFromTemplateCache(this,args["templatePath"],args["templateCssPath"],null,_5a5);
var ts=dojo.widget._templateCache[this.widgetType];
if((ts)&&(!_5a5)){
if(!this.templateString.length){
this.templateString=ts["string"];
}
if(!this.templateNode){
this.templateNode=ts["node"];
}
}
var _5a7=false;
var node=null;
var tstr=this.templateString;
if((!this.templateNode)&&(this.templateString)){
_5a7=this.templateString.match(/\$\{([^\}]+)\}/g);
if(_5a7){
var hash=this.strings||{};
for(var key in dojo.widget.defaultStrings){
if(dojo.lang.isUndefined(hash[key])){
hash[key]=dojo.widget.defaultStrings[key];
}
}
for(var i=0;i<_5a7.length;i++){
var key=_5a7[i];
key=key.substring(2,key.length-1);
var kval=(key.substring(0,5)=="this.")?dojo.lang.getObjPathValue(key.substring(5),this):hash[key];
var _5ae;
if((kval)||(dojo.lang.isString(kval))){
_5ae=(dojo.lang.isFunction(kval))?kval.call(this,key,this.templateString):kval;
tstr=tstr.replace(_5a7[i],_5ae);
}
}
}else{
this.templateNode=this.createNodesFromText(this.templateString,true)[0];
if(!_5a5){
ts.node=this.templateNode;
}
}
}
if((!this.templateNode)&&(!_5a7)){
dojo.debug("weren't able to create template!");
return false;
}else{
if(!_5a7){
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
},attachTemplateNodes:function(_5b0,_5b1){
if(!_5b1){
_5b1=this;
}
return dojo.widget.attachTemplateNodes(_5b0,_5b1,dojo.widget.getDojoEventsFromStr(this.templateString));
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
var _5b4=dojo.html.getCursorPosition(e);
with(dojo.html){
var _5b5=getAbsoluteX(node,true)+(getInnerWidth(node)/2);
var _5b6=getAbsoluteY(node,true)+(getInnerHeight(node)/2);
}
with(dojo.html.gravity){
return ((_5b4.x<_5b5?WEST:EAST)|(_5b4.y<_5b6?NORTH:SOUTH));
}
};
dojo.html.gravity.NORTH=1;
dojo.html.gravity.SOUTH=1<<1;
dojo.html.gravity.EAST=1<<2;
dojo.html.gravity.WEST=1<<3;
dojo.html.renderedTextContent=function(node){
node=dojo.byId(node);
var _5b8="";
if(node==null){
return _5b8;
}
for(var i=0;i<node.childNodes.length;i++){
switch(node.childNodes[i].nodeType){
case 1:
case 5:
var _5ba="unknown";
try{
_5ba=dojo.style.getStyle(node.childNodes[i],"display");
}
catch(E){
}
switch(_5ba){
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
_5b8+="\n";
_5b8+=dojo.html.renderedTextContent(node.childNodes[i]);
_5b8+="\n";
break;
case "none":
break;
default:
if(node.childNodes[i].tagName&&node.childNodes[i].tagName.toLowerCase()=="br"){
_5b8+="\n";
}else{
_5b8+=dojo.html.renderedTextContent(node.childNodes[i]);
}
break;
}
break;
case 3:
case 2:
case 4:
var text=node.childNodes[i].nodeValue;
var _5bc="unknown";
try{
_5bc=dojo.style.getStyle(node,"text-transform");
}
catch(E){
}
switch(_5bc){
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
switch(_5bc){
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
if(/\s$/.test(_5b8)){
text.replace(/^\s/,"");
}
break;
}
_5b8+=text;
break;
default:
break;
}
}
return _5b8;
};
dojo.html.createNodesFromText=function(txt,trim){
if(trim){
txt=dojo.string.trim(txt);
}
var tn=document.createElement("div");
tn.style.visibility="hidden";
document.body.appendChild(tn);
var _5c0="none";
if((/^<t[dh][\s\r\n>]/i).test(dojo.string.trimStart(txt))){
txt="<table><tbody><tr>"+txt+"</tr></tbody></table>";
_5c0="cell";
}else{
if((/^<tr[\s\r\n>]/i).test(dojo.string.trimStart(txt))){
txt="<table><tbody>"+txt+"</tbody></table>";
_5c0="row";
}else{
if((/^<(thead|tbody|tfoot)[\s\r\n>]/i).test(dojo.string.trimStart(txt))){
txt="<table>"+txt+"</table>";
_5c0="section";
}
}
}
tn.innerHTML=txt;
if(tn["normalize"]){
tn.normalize();
}
var _5c1=null;
switch(_5c0){
case "cell":
_5c1=tn.getElementsByTagName("tr")[0];
break;
case "row":
_5c1=tn.getElementsByTagName("tbody")[0];
break;
case "section":
_5c1=tn.getElementsByTagName("table")[0];
break;
default:
_5c1=tn;
break;
}
var _5c2=[];
for(var x=0;x<_5c1.childNodes.length;x++){
_5c2.push(_5c1.childNodes[x].cloneNode(true));
}
tn.style.display="none";
document.body.removeChild(tn);
return _5c2;
};
dojo.html.placeOnScreen=function(node,_5c5,_5c6,_5c7,_5c8){
if(dojo.lang.isArray(_5c5)){
_5c8=_5c7;
_5c7=_5c6;
_5c6=_5c5[1];
_5c5=_5c5[0];
}
if(!isNaN(_5c7)){
_5c7=[Number(_5c7),Number(_5c7)];
}else{
if(!dojo.lang.isArray(_5c7)){
_5c7=[0,0];
}
}
var _5c9=dojo.html.getScrollOffset();
var view=dojo.html.getViewportSize();
node=dojo.byId(node);
var w=node.offsetWidth+_5c7[0];
var h=node.offsetHeight+_5c7[1];
if(_5c8){
_5c5-=_5c9.x;
_5c6-=_5c9.y;
}
var x=_5c5+w;
if(x>view.w){
x=view.w-w;
}else{
x=_5c5;
}
x=Math.max(_5c7[0],x)+_5c9.x;
var y=_5c6+h;
if(y>view.h){
y=view.h-h;
}else{
y=_5c6;
}
y=Math.max(_5c7[1],y)+_5c9.y;
node.style.left=x+"px";
node.style.top=y+"px";
var ret=[x,y];
ret.x=x;
ret.y=y;
return ret;
};
dojo.html.placeOnScreenPoint=function(node,_5d1,_5d2,_5d3,_5d4){
if(dojo.lang.isArray(_5d1)){
_5d4=_5d3;
_5d3=_5d2;
_5d2=_5d1[1];
_5d1=_5d1[0];
}
if(!isNaN(_5d3)){
_5d3=[Number(_5d3),Number(_5d3)];
}else{
if(!dojo.lang.isArray(_5d3)){
_5d3=[0,0];
}
}
var _5d5=dojo.html.getScrollOffset();
var view=dojo.html.getViewportSize();
node=dojo.byId(node);
var _5d7=node.style.display;
node.style.display="";
var w=dojo.style.getInnerWidth(node);
var h=dojo.style.getInnerHeight(node);
node.style.display=_5d7;
if(_5d4){
_5d1-=_5d5.x;
_5d2-=_5d5.y;
}
var x=-1,y=-1;
if((_5d1+_5d3[0])+w<=view.w&&(_5d2+_5d3[1])+h<=view.h){
x=(_5d1+_5d3[0]);
y=(_5d2+_5d3[1]);
}
if((x<0||y<0)&&(_5d1-_5d3[0])<=view.w&&(_5d2+_5d3[1])+h<=view.h){
x=(_5d1-_5d3[0])-w;
y=(_5d2+_5d3[1]);
}
if((x<0||y<0)&&(_5d1+_5d3[0])+w<=view.w&&(_5d2-_5d3[1])<=view.h){
x=(_5d1+_5d3[0]);
y=(_5d2-_5d3[1])-h;
}
if((x<0||y<0)&&(_5d1-_5d3[0])<=view.w&&(_5d2-_5d3[1])<=view.h){
x=(_5d1-_5d3[0])-w;
y=(_5d2-_5d3[1])-h;
}
if(x<0||y<0||(x+w>view.w)||(y+h>view.h)){
return dojo.html.placeOnScreen(node,_5d1,_5d2,_5d3,_5d4);
}
x+=_5d5.x;
y+=_5d5.y;
node.style.left=x+"px";
node.style.top=y+"px";
var ret=[x,y];
ret.x=x;
ret.y=y;
return ret;
};
dojo.html.BackgroundIframe=function(node){
if(dojo.render.html.ie55||dojo.render.html.ie60){
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
if(this.iframe&&this.domNode&&this.domNode.parentElement){
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
var _5e2=dojo.style.toCoordinateArray(node,true);
var s=this.iframe.style;
s.width=_5e2.w+"px";
s.height=_5e2.h+"px";
s.left=_5e2.x+"px";
s.top=_5e2.y+"px";
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
dojo.lfx.Line=function(_5e6,end){
this.start=_5e6;
this.end=end;
if(dojo.lang.isArray(_5e6)){
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
var diff=end-_5e6;
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
dojo.lfx.Animation=function(_5f5,_5f6,_5f7,_5f8,_5f9,rate){
dojo.lfx.IAnimation.call(this);
if(dojo.lang.isNumber(_5f5)||(!_5f5&&_5f6.getValue)){
rate=_5f9;
_5f9=_5f8;
_5f8=_5f7;
_5f7=_5f6;
_5f6=_5f5;
_5f5=null;
}else{
if(_5f5.getValue||dojo.lang.isArray(_5f5)){
rate=_5f8;
_5f9=_5f7;
_5f8=_5f6;
_5f7=_5f5;
_5f6=null;
_5f5=null;
}
}
if(dojo.lang.isArray(_5f7)){
this.curve=new dojo.lfx.Line(_5f7[0],_5f7[1]);
}else{
this.curve=_5f7;
}
if(_5f6!=null&&_5f6>0){
this.duration=_5f6;
}
if(_5f9){
this.repeatCount=_5f9;
}
if(rate){
this.rate=rate;
}
if(_5f5){
this.handler=_5f5.handler;
this.beforeBegin=_5f5.beforeBegin;
this.onBegin=_5f5.onBegin;
this.onEnd=_5f5.onEnd;
this.onPlay=_5f5.onPlay;
this.onPause=_5f5.onPause;
this.onStop=_5f5.onStop;
this.onAnimate=_5f5.onAnimate;
}
if(_5f8&&dojo.lang.isFunction(_5f8)){
this.easing=_5f8;
}
};
dojo.inherits(dojo.lfx.Animation,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Animation,{_startTime:null,_endTime:null,_timer:null,_percent:0,_startRepeatCount:0,play:function(_5fb,_5fc){
if(_5fc){
clearTimeout(this._timer);
this._active=false;
this._paused=false;
this._percent=0;
}else{
if(this._active&&!this._paused){
return this;
}
}
this.fire("handler",["beforeBegin"]);
this.fire("beforeBegin");
if(_5fb>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_5fc);
}),_5fb);
return this;
}
this._startTime=new Date().valueOf();
if(this._paused){
this._startTime-=(this.duration*this._percent/100);
}
this._endTime=this._startTime+this.duration;
this._active=true;
this._paused=false;
var step=this._percent/100;
var _5fe=this.curve.getValue(step);
if(this._percent==0){
if(!this._startRepeatCount){
this._startRepeatCount=this.repeatCount;
}
this.fire("handler",["begin",_5fe]);
this.fire("onBegin",[_5fe]);
}
this.fire("handler",["play",_5fe]);
this.fire("onPlay",[_5fe]);
this._cycle();
return this;
},pause:function(){
clearTimeout(this._timer);
if(!this._active){
return this;
}
this._paused=true;
var _5ff=this.curve.getValue(this._percent/100);
this.fire("handler",["pause",_5ff]);
this.fire("onPause",[_5ff]);
return this;
},gotoPercent:function(pct,_601){
clearTimeout(this._timer);
this._active=true;
this._paused=true;
this._percent=pct;
if(_601){
this.play();
}
},stop:function(_602){
clearTimeout(this._timer);
var step=this._percent/100;
if(_602){
step=1;
}
var _604=this.curve.getValue(step);
this.fire("handler",["stop",_604]);
this.fire("onStop",[_604]);
this._active=false;
this._paused=false;
return this;
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
if((this.easing)&&(dojo.lang.isFunction(this.easing))){
step=this.easing(step);
}
var _607=this.curve.getValue(step);
this.fire("handler",["animate",_607]);
this.fire("onAnimate",[_607]);
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
return this;
}});
dojo.lfx.Combine=function(){
dojo.lfx.IAnimation.call(this);
this._anims=[];
this._animsEnded=0;
var _608=arguments;
if(_608.length==1&&(dojo.lang.isArray(_608[0])||dojo.lang.isArrayLike(_608[0]))){
_608=_608[0];
}
var _609=this;
dojo.lang.forEach(_608,function(anim){
_609._anims.push(anim);
var _60b=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_60b();
_609._onAnimsEnded();
};
});
};
dojo.inherits(dojo.lfx.Combine,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Combine,{_animsEnded:0,play:function(_60c,_60d){
if(!this._anims.length){
return this;
}
this.fire("beforeBegin");
if(_60c>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_60d);
}),_60c);
return this;
}
if(_60d||this._anims[0].percent==0){
this.fire("onBegin");
}
this.fire("onPlay");
this._animsCall("play",null,_60d);
return this;
},pause:function(){
this.fire("onPause");
this._animsCall("pause");
return this;
},stop:function(_60e){
this.fire("onStop");
this._animsCall("stop",_60e);
return this;
},_onAnimsEnded:function(){
this._animsEnded++;
if(this._animsEnded>=this._anims.length){
this.fire("onEnd");
}
return this;
},_animsCall:function(_60f){
var args=[];
if(arguments.length>1){
for(var i=1;i<arguments.length;i++){
args.push(arguments[i]);
}
}
var _612=this;
dojo.lang.forEach(this._anims,function(anim){
anim[_60f](args);
},_612);
return this;
}});
dojo.lfx.Chain=function(){
dojo.lfx.IAnimation.call(this);
this._anims=[];
this._currAnim=-1;
var _614=arguments;
if(_614.length==1&&(dojo.lang.isArray(_614[0])||dojo.lang.isArrayLike(_614[0]))){
_614=_614[0];
}
var _615=this;
dojo.lang.forEach(_614,function(anim,i,_618){
_615._anims.push(anim);
var _619=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
if(i<_618.length-1){
anim.onEnd=function(){
_619();
_615._playNext();
};
}else{
anim.onEnd=function(){
_619();
_615.fire("onEnd");
};
}
},_615);
};
dojo.inherits(dojo.lfx.Chain,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Chain,{_currAnim:-1,play:function(_61a,_61b){
if(!this._anims.length){
return this;
}
if(_61b||!this._anims[this._currAnim]){
this._currAnim=0;
}
var _61c=this._anims[this._currAnim];
this.fire("beforeBegin");
if(_61a>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_61b);
}),_61a);
return this;
}
if(_61c){
if(this._currAnim==0){
this.fire("handler",["begin",this._currAnim]);
this.fire("onBegin",[this._currAnim]);
}
this.fire("onPlay",[this._currAnim]);
_61c.play(null,_61b);
}
return this;
},pause:function(){
if(this._anims[this._currAnim]){
this._anims[this._currAnim].pause();
this.fire("onPause",[this._currAnim]);
}
return this;
},playPause:function(){
if(this._anims.length==0){
return this;
}
if(this._currAnim==-1){
this._currAnim=0;
}
var _61d=this._anims[this._currAnim];
if(_61d){
if(!_61d._active||_61d._paused){
this.play();
}else{
this.pause();
}
}
return this;
},stop:function(){
var _61e=this._anims[this._currAnim];
if(_61e){
_61e.stop();
this.fire("onStop",[this._currAnim]);
}
return _61e;
},_playNext:function(){
if(this._currAnim==-1||this._anims.length==0){
return this;
}
this._currAnim++;
if(this._anims[this._currAnim]){
this._anims[this._currAnim].play(null,true);
}
return this;
}});
dojo.lfx.combine=function(){
var _61f=arguments;
if(dojo.lang.isArray(arguments[0])){
_61f=arguments[0];
}
return new dojo.lfx.Combine(_61f);
};
dojo.lfx.chain=function(){
var _620=arguments;
if(dojo.lang.isArray(arguments[0])){
_620=arguments[0];
}
return new dojo.lfx.Chain(_620);
};
dojo.provide("dojo.lfx.html");
dojo.require("dojo.lfx.Animation");
dojo.require("dojo.html");
dojo.lfx.html._byId=function(_621){
if(!_621){
return [];
}
if(dojo.lang.isArray(_621)){
if(!_621.alreadyChecked){
var n=[];
dojo.lang.forEach(_621,function(node){
n.push(dojo.byId(node));
});
n.alreadyChecked=true;
return n;
}else{
return _621;
}
}else{
var n=[];
n.push(dojo.byId(_621));
n.alreadyChecked=true;
return n;
}
};
dojo.lfx.html.propertyAnimation=function(_624,_625,_626,_627){
_624=dojo.lfx.html._byId(_624);
if(_624.length==1){
dojo.lang.forEach(_625,function(prop){
if(typeof prop["start"]=="undefined"){
if(prop.property!="opacity"){
prop.start=parseInt(dojo.style.getComputedStyle(_624[0],prop.property));
}else{
prop.start=dojo.style.getOpacity(_624[0]);
}
}
});
}
var _629=function(_62a){
var _62b=new Array(_62a.length);
for(var i=0;i<_62a.length;i++){
_62b[i]=Math.round(_62a[i]);
}
return _62b;
};
var _62d=function(n,_62f){
n=dojo.byId(n);
if(!n||!n.style){
return;
}
for(var s in _62f){
if(s=="opacity"){
dojo.style.setOpacity(n,_62f[s]);
}else{
n.style[s]=_62f[s];
}
}
};
var _631=function(_632){
this._properties=_632;
this.diffs=new Array(_632.length);
dojo.lang.forEach(_632,function(prop,i){
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
var _639=null;
if(dojo.lang.isArray(prop.start)){
}else{
if(prop.start instanceof dojo.graphics.color.Color){
_639=(prop.units||"rgb")+"(";
for(var j=0;j<prop.startRgb.length;j++){
_639+=Math.round(((prop.endRgb[j]-prop.startRgb[j])*n)+prop.startRgb[j])+(j<prop.startRgb.length-1?",":"");
}
_639+=")";
}else{
_639=((this.diffs[i])*n)+prop.start+(prop.property!="opacity"?prop.units||"px":"");
}
}
ret[dojo.style.toCamelCase(prop.property)]=_639;
},this);
return ret;
};
};
var anim=new dojo.lfx.Animation({onAnimate:function(_63c){
dojo.lang.forEach(_624,function(node){
_62d(node,_63c);
});
}},_626,new _631(_625),_627);
return anim;
};
dojo.lfx.html._makeFadeable=function(_63e){
var _63f=function(node){
if(dojo.render.html.ie){
if((node.style.zoom.length==0)&&(dojo.style.getStyle(node,"zoom")=="normal")){
node.style.zoom="1";
}
if((node.style.width.length==0)&&(dojo.style.getStyle(node,"width")=="auto")){
node.style.width="auto";
}
}
};
if(dojo.lang.isArrayLike(_63e)){
dojo.lang.forEach(_63e,_63f);
}else{
_63f(_63e);
}
};
dojo.lfx.html.fadeIn=function(_641,_642,_643,_644){
_641=dojo.lfx.html._byId(_641);
dojo.lfx.html._makeFadeable(_641);
var anim=dojo.lfx.propertyAnimation(_641,[{property:"opacity",start:dojo.style.getOpacity(_641[0]),end:1}],_642,_643);
if(_644){
var _646=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_646();
_644(_641,anim);
};
}
return anim;
};
dojo.lfx.html.fadeOut=function(_647,_648,_649,_64a){
_647=dojo.lfx.html._byId(_647);
dojo.lfx.html._makeFadeable(_647);
var anim=dojo.lfx.propertyAnimation(_647,[{property:"opacity",start:dojo.style.getOpacity(_647[0]),end:0}],_648,_649);
if(_64a){
var _64c=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_64c();
_64a(_647,anim);
};
}
return anim;
};
dojo.lfx.html.fadeShow=function(_64d,_64e,_64f,_650){
var anim=dojo.lfx.html.fadeIn(_64d,_64e,_64f,_650);
var _652=(anim["beforeBegin"])?dojo.lang.hitch(anim,"beforeBegin"):function(){
};
anim.beforeBegin=function(){
_652();
if(dojo.lang.isArrayLike(_64d)){
dojo.lang.forEach(_64d,dojo.style.show);
}else{
dojo.style.show(_64d);
}
};
return anim;
};
dojo.lfx.html.fadeHide=function(_653,_654,_655,_656){
var anim=dojo.lfx.html.fadeOut(_653,_654,_655,function(){
if(dojo.lang.isArrayLike(_653)){
dojo.lang.forEach(_653,dojo.style.hide);
}else{
dojo.style.hide(_653);
}
if(_656){
_656(_653,anim);
}
});
return anim;
};
dojo.lfx.html.wipeIn=function(_658,_659,_65a,_65b){
_658=dojo.lfx.html._byId(_658);
var _65c=[];
dojo.lang.forEach(_658,function(node){
var _65e=dojo.style.getStyle(node,"overflow");
if(_65e=="visible"){
node.style.overflow="hidden";
}
node.style.height="0px";
dojo.style.show(node);
var anim=dojo.lfx.propertyAnimation(node,[{property:"height",start:0,end:node.scrollHeight}],_659,_65a);
var _660=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_660();
node.style.overflow=_65e;
node.style.height="auto";
if(_65b){
_65b(node,anim);
}
};
_65c.push(anim);
});
if(_658.length>1){
return dojo.lfx.combine(_65c);
}else{
return _65c[0];
}
};
dojo.lfx.html.wipeOut=function(_661,_662,_663,_664){
_661=dojo.lfx.html._byId(_661);
var _665=[];
dojo.lang.forEach(_661,function(node){
var _667=dojo.style.getStyle(node,"overflow");
if(_667=="visible"){
node.style.overflow="hidden";
}
dojo.style.show(node);
var anim=dojo.lfx.propertyAnimation(node,[{property:"height",start:dojo.style.getContentBoxHeight(node),end:0}],_662,_663);
var _669=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_669();
dojo.style.hide(node);
node.style.overflow=_667;
if(_664){
_664(node,anim);
}
};
_665.push(anim);
});
if(_661.length>1){
return dojo.lfx.combine(_665);
}else{
return _665[0];
}
};
dojo.lfx.html.slideTo=function(_66a,_66b,_66c,_66d,_66e){
_66a=dojo.lfx.html._byId(_66a);
var _66f=[];
dojo.lang.forEach(_66a,function(node){
var top=null;
var left=null;
var init=(function(){
var _674=node;
return function(){
top=_674.offsetTop;
left=_674.offsetLeft;
if(!dojo.style.isPositionAbsolute(_674)){
var ret=dojo.style.abs(_674,true);
dojo.style.setStyleAttributes(_674,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");
top=ret.y;
left=ret.x;
}
};
})();
init();
var anim=dojo.lfx.propertyAnimation(node,[{property:"top",start:top,end:_66b[0]},{property:"left",start:left,end:_66b[1]}],_66c,_66d);
var _677=(anim["beforeBegin"])?dojo.lang.hitch(anim,"beforeBegin"):function(){
};
anim.beforeBegin=function(){
_677();
init();
};
if(_66e){
var _678=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_678();
_66e(_66a,anim);
};
}
_66f.push(anim);
});
if(_66a.length>1){
return dojo.lfx.combine(_66f);
}else{
return _66f[0];
}
};
dojo.lfx.html.slideBy=function(_679,_67a,_67b,_67c,_67d){
_679=dojo.lfx.html._byId(_679);
var _67e=[];
dojo.lang.forEach(_679,function(node){
var top=null;
var left=null;
var init=(function(){
var _683=node;
return function(){
top=node.offsetTop;
left=node.offsetLeft;
if(!dojo.style.isPositionAbsolute(_683)){
var ret=dojo.style.abs(_683);
dojo.style.setStyleAttributes(_683,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");
top=ret.y;
left=ret.x;
}
};
})();
init();
var anim=dojo.lfx.propertyAnimation(node,[{property:"top",start:top,end:top+_67a[0]},{property:"left",start:left,end:left+_67a[1]}],_67b,_67c);
var _686=(anim["beforeBegin"])?dojo.lang.hitch(anim,"beforeBegin"):function(){
};
anim.beforeBegin=function(){
_686();
init();
};
if(_67d){
var _687=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_687();
_67d(_679,anim);
};
}
_67e.push(anim);
});
if(_679.length>1){
return dojo.lfx.combine(_67e);
}else{
return _67e[0];
}
};
dojo.lfx.html.explode=function(_688,_689,_68a,_68b,_68c){
_688=dojo.byId(_688);
_689=dojo.byId(_689);
var _68d=dojo.style.toCoordinateArray(_688,true);
var _68e=document.createElement("div");
dojo.html.copyStyle(_68e,_689);
with(_68e.style){
position="absolute";
display="none";
}
document.body.appendChild(_68e);
with(_689.style){
visibility="hidden";
display="block";
}
var _68f=dojo.style.toCoordinateArray(_689,true);
with(_689.style){
display="none";
visibility="visible";
}
var anim=new dojo.lfx.propertyAnimation(_68e,[{property:"height",start:_68d[3],end:_68f[3]},{property:"width",start:_68d[2],end:_68f[2]},{property:"top",start:_68d[1],end:_68f[1]},{property:"left",start:_68d[0],end:_68f[0]},{property:"opacity",start:0.3,end:1}],_68a,_68b);
anim.beforeBegin=function(){
dojo.style.setDisplay(_68e,"block");
};
anim.onEnd=function(){
dojo.style.setDisplay(_689,"block");
_68e.parentNode.removeChild(_68e);
};
if(_68c){
var _691=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_691();
_68c(_689,anim);
};
}
return anim;
};
dojo.lfx.html.implode=function(_692,end,_694,_695,_696){
_692=dojo.byId(_692);
end=dojo.byId(end);
var _697=dojo.style.toCoordinateArray(_692,true);
var _698=dojo.style.toCoordinateArray(end,true);
var _699=document.createElement("div");
dojo.html.copyStyle(_699,_692);
dojo.style.setOpacity(_699,0.3);
with(_699.style){
position="absolute";
display="none";
}
document.body.appendChild(_699);
var anim=new dojo.lfx.propertyAnimation(_699,[{property:"height",start:_697[3],end:_698[3]},{property:"width",start:_697[2],end:_698[2]},{property:"top",start:_697[1],end:_698[1]},{property:"left",start:_697[0],end:_698[0]},{property:"opacity",start:1,end:0.3}],_694,_695);
anim.beforeBegin=function(){
dojo.style.hide(_692);
dojo.style.show(_699);
};
anim.onEnd=function(){
_699.parentNode.removeChild(_699);
};
if(_696){
var _69b=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_69b();
_696(_692,anim);
};
}
return anim;
};
dojo.lfx.html.highlight=function(_69c,_69d,_69e,_69f,_6a0){
_69c=dojo.lfx.html._byId(_69c);
var _6a1=[];
dojo.lang.forEach(_69c,function(node){
var _6a3=dojo.style.getBackgroundColor(node);
var bg=dojo.style.getStyle(node,"background-color").toLowerCase();
var _6a5=dojo.style.getStyle(node,"background-image");
var _6a6=(bg=="transparent"||bg=="rgba(0, 0, 0, 0)");
while(_6a3.length>3){
_6a3.pop();
}
var rgb=new dojo.graphics.color.Color(_69d);
var _6a8=new dojo.graphics.color.Color(_6a3);
var anim=dojo.lfx.propertyAnimation(node,[{property:"background-color",start:rgb,end:_6a8}],_69e,_69f);
var _6aa=(anim["beforeBegin"])?dojo.lang.hitch(anim,"beforeBegin"):function(){
};
anim.beforeBegin=function(){
_6aa();
if(_6a5){
node.style.backgroundImage="none";
}
node.style.backgroundColor="rgb("+rgb.toRgb().join(",")+")";
};
var _6ab=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_6ab();
if(_6a5){
node.style.backgroundImage=_6a5;
}
if(_6a6){
node.style.backgroundColor="transparent";
}
if(_6a0){
_6a0(node,anim);
}
};
_6a1.push(anim);
});
if(_69c.length>1){
return dojo.lfx.combine(_6a1);
}else{
return _6a1[0];
}
};
dojo.lfx.html.unhighlight=function(_6ac,_6ad,_6ae,_6af,_6b0){
_6ac=dojo.lfx.html._byId(_6ac);
var _6b1=[];
dojo.lang.forEach(_6ac,function(node){
var _6b3=new dojo.graphics.color.Color(dojo.style.getBackgroundColor(node));
var rgb=new dojo.graphics.color.Color(_6ad);
var _6b5=dojo.style.getStyle(node,"background-image");
var anim=dojo.lfx.propertyAnimation(node,[{property:"background-color",start:_6b3,end:rgb}],_6ae,_6af);
var _6b7=(anim["beforeBegin"])?dojo.lang.hitch(anim,"beforeBegin"):function(){
};
anim.beforeBegin=function(){
_6b7();
if(_6b5){
node.style.backgroundImage="none";
}
node.style.backgroundColor="rgb("+_6b3.toRgb().join(",")+")";
};
var _6b8=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_6b8();
if(_6b0){
_6b0(node,anim);
}
};
_6b1.push(anim);
});
if(_6ac.length>1){
return dojo.lfx.combine(_6b1);
}else{
return _6b1[0];
}
};
dojo.lang.mixin(dojo.lfx,dojo.lfx.html);
dojo.kwCompoundRequire({browser:["dojo.lfx.html"],dashboard:["dojo.lfx.html"]});
dojo.provide("dojo.lfx.*");
dojo.provide("dojo.lfx.toggle");
dojo.require("dojo.lfx.*");
dojo.lfx.toggle.plain={show:function(node,_6ba,_6bb,_6bc){
dojo.style.show(node);
if(dojo.lang.isFunction(_6bc)){
_6bc();
}
},hide:function(node,_6be,_6bf,_6c0){
dojo.style.hide(node);
if(dojo.lang.isFunction(_6c0)){
_6c0();
}
}};
dojo.lfx.toggle.fade={show:function(node,_6c2,_6c3,_6c4){
dojo.lfx.fadeShow(node,_6c2,_6c3,_6c4).play();
},hide:function(node,_6c6,_6c7,_6c8){
dojo.lfx.fadeHide(node,_6c6,_6c7,_6c8).play();
}};
dojo.lfx.toggle.wipe={show:function(node,_6ca,_6cb,_6cc){
dojo.lfx.wipeIn(node,_6ca,_6cb,_6cc).play();
},hide:function(node,_6ce,_6cf,_6d0){
dojo.lfx.wipeOut(node,_6ce,_6cf,_6d0).play();
}};
dojo.lfx.toggle.explode={show:function(node,_6d2,_6d3,_6d4,_6d5){
dojo.lfx.explode(_6d5||[0,0,0,0],node,_6d2,_6d3,_6d4).play();
},hide:function(node,_6d7,_6d8,_6d9,_6da){
dojo.lfx.implode(node,_6da||[0,0,0,0],_6d7,_6d8,_6d9).play();
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
},setNativeHeight:function(_6df){
var ch=this.getContainerHeight();
},createNodesFromText:function(txt,wrap){
return dojo.html.createNodesFromText(txt,wrap);
},destroyRendering:function(_6e3){
try{
if(!_6e3){
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
this.checkSize();
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
},checkSize:function(){
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
dojo.lang.forEach(this.children,function(_6e8){
_6e8.checkSize();
});
}});
dojo.kwCompoundRequire({common:["dojo.xml.Parse","dojo.widget.Widget","dojo.widget.Parse","dojo.widget.Manager"],browser:["dojo.widget.DomWidget","dojo.widget.HtmlWidget"],dashboard:["dojo.widget.DomWidget","dojo.widget.HtmlWidget"],svg:["dojo.widget.SvgWidget"],rhino:["dojo.widget.SwtWidget"]});
dojo.provide("dojo.widget.*");

