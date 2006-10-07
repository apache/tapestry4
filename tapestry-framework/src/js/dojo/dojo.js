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
var dj_currentContext=this;
function dj_undef(_1,_2){
return (typeof (_2||dj_currentContext)[_1]=="undefined");
}
if(dj_undef("djConfig",this)){
var djConfig={};
}
if(dj_undef("dojo",this)){
var dojo={};
}
dojo.global=function(){
return dj_currentContext;
};
dojo.locale=djConfig.locale;
dojo.version={major:0,minor:0,patch:0,flag:"dev",revision:Number("$Rev: 5779 $".match(/[0-9]+/)[0]),toString:function(){
with(dojo.version){
return major+"."+minor+"."+patch+flag+" ("+revision+")";
}
}};
dojo.evalProp=function(_3,_4,_5){
if((!_4)||(!_3)){
return undefined;
}
if(!dj_undef(_3,_4)){
return _4[_3];
}
return (_5?(_4[_3]={}):undefined);
};
dojo.parseObjPath=function(_6,_7,_8){
var _9=(_7||dojo.global());
var _a=_6.split(".");
var _b=_a.pop();
for(var i=0,l=_a.length;i<l&&_9;i++){
_9=dojo.evalProp(_a[i],_9,_8);
}
return {obj:_9,prop:_b};
};
dojo.evalObjPath=function(_e,_f){
if(typeof _e!="string"){
return dojo.global();
}
if(_e.indexOf(".")==-1){
return dojo.evalProp(_e,dojo.global(),_f);
}
var ref=dojo.parseObjPath(_e,dojo.global(),_f);
if(ref){
return dojo.evalProp(ref.prop,ref.obj,_f);
}
return null;
};
dojo.errorToString=function(_11){
if(!dj_undef("message",_11)){
return _11.message;
}else{
if(!dj_undef("description",_11)){
return _11.description;
}else{
return _11;
}
}
};
dojo.raise=function(_12,_13){
if(_13){
_12=_12+": "+dojo.errorToString(_13);
}
try{
if(djConfig.isDebug){
dojo.hostenv.println("FATAL exception raised: "+_12);
}
}
catch(e){
}
throw _13||Error(_12);
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
function dj_eval(_15){
return dj_global.eval?dj_global.eval(_15):eval(_15);
}
dojo.unimplemented=function(_16,_17){
var _18="'"+_16+"' not implemented";
if(_17!=null){
_18+=" "+_17;
}
dojo.raise(_18);
};
dojo.deprecated=function(_19,_1a,_1b){
var _1c="DEPRECATED: "+_19;
if(_1a){
_1c+=" "+_1a;
}
if(_1b){
_1c+=" -- will be removed in version: "+_1b;
}
dojo.debug(_1c);
};
dojo.render=(function(){
function vscaffold(_1d,_1e){
var tmp={capable:false,support:{builtin:false,plugin:false},prefixes:_1d};
for(var i=0;i<_1e.length;i++){
tmp[_1e[i]]=false;
}
return tmp;
}
return {name:"",ver:dojo.version,os:{win:false,linux:false,osx:false},html:vscaffold(["html"],["ie","opera","khtml","safari","moz"]),svg:vscaffold(["svg"],["corel","adobe","batik"]),vml:vscaffold(["vml"],["ie"]),swf:vscaffold(["Swf","Flash","Mm"],["mm"]),swt:vscaffold(["Swt"],["ibm"])};
})();
dojo.hostenv=(function(){
var _21={isDebug:false,allowQueryConfig:false,baseScriptUri:"",baseRelativePath:"",libraryScriptUri:"",iePreventClobber:false,ieClobberMinimal:true,preventBackButtonFix:true,searchIds:[],parseWidgets:true};
if(typeof djConfig=="undefined"){
djConfig=_21;
}else{
for(var _22 in _21){
if(typeof djConfig[_22]=="undefined"){
djConfig[_22]=_21[_22];
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
var _25=uri.lastIndexOf("/");
djConfig.baseScriptUri=djConfig.baseRelativePath;
return djConfig.baseScriptUri;
};
(function(){
var _26={pkgFileName:"__package__",loading_modules_:{},loaded_modules_:{},addedToLoadingCount:[],removedFromLoadingCount:[],inFlightCount:0,modulePrefixes_:{dojo:{name:"dojo",value:"src"}},setModulePrefix:function(_27,_28){
this.modulePrefixes_[_27]={name:_27,value:_28};
},moduleHasPrefix:function(_29){
var mp=this.modulePrefixes_;
return Boolean(mp[_29]&&mp[_29].value);
},getModulePrefix:function(_2b){
if(this.moduleHasPrefix(_2b)){
return this.modulePrefixes_[_2b].value;
}
return _2b;
},getTextStack:[],loadUriStack:[],loadedUris:[],post_load_:false,modulesLoadedListeners:[],unloadListeners:[],loadNotifying:false};
for(var _2c in _26){
dojo.hostenv[_2c]=_26[_2c];
}
})();
dojo.hostenv.loadPath=function(_2d,_2e,cb){
var uri;
if(_2d.charAt(0)=="/"||_2d.match(/^\w+:/)){
uri=_2d;
}else{
uri=this.getBaseScriptUri()+_2d;
}
if(djConfig.cacheBust&&dojo.render.html.capable){
uri+="?"+String(djConfig.cacheBust).replace(/\W+/g,"");
}
try{
return !_2e?this.loadUri(uri,cb):this.loadUriAndCheck(uri,_2e,cb);
}
catch(e){
dojo.debug(e);
return false;
}
};
dojo.hostenv.loadUri=function(uri,cb){
if(this.loadedUris[uri]){
return true;
}
var _33=this.getText(uri,null,true);
if(!_33){
return false;
}
this.loadedUris[uri]=true;
if(cb){
_33="("+_33+")";
}
var _34=dj_eval(_33);
if(cb){
cb(_34);
}
return true;
};
dojo.hostenv.loadUriAndCheck=function(uri,_36,cb){
var ok=true;
try{
ok=this.loadUri(uri,cb);
}
catch(e){
dojo.debug("failed loading ",uri," with error: ",e);
}
return Boolean(ok&&this.findModule(_36,false));
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
if(this.loadUriStack.length==0&&this.getTextStack.length==0){
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
for(var i=_43.length;i>0;i--){
var _45=_43.slice(0,i).join(".");
if((i==1)&&!this.moduleHasPrefix(_45)){
_43[0]="../"+_43[0];
}else{
var _46=this.getModulePrefix(_45);
if(_46!=_45){
_43.splice(0,i,_46);
break;
}
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
var _4c=_47.split(".");
var _4d=this.getModuleSymbols(_47);
var _4e=((_4d[0].charAt(0)!="/")&&!_4d[0].match(/^\w+:/));
var _4f=_4d[_4d.length-1];
var ok;
if(_4f=="*"){
_47=_4c.slice(0,-1).join(".");
while(_4d.length){
_4d.pop();
_4d.push(this.pkgFileName);
_4b=_4d.join("/")+".js";
if(_4e&&_4b.charAt(0)=="/"){
_4b=_4b.slice(1);
}
ok=this.loadPath(_4b,!_49?_47:null);
if(ok){
break;
}
_4d.pop();
}
}else{
_4b=_4d.join("/")+".js";
_47=_4c.join(".");
var _51=!_49?_47:null;
ok=this.loadPath(_4b,_51);
if(!ok&&!_48){
_4d.pop();
while(_4d.length){
_4b=_4d.join("/")+".js";
ok=this.loadPath(_4b,_51);
if(ok){
break;
}
_4d.pop();
_4b=_4d.join("/")+"/"+this.pkgFileName+".js";
if(_4e&&_4b.charAt(0)=="/"){
_4b=_4b.slice(1);
}
ok=this.loadPath(_4b,_51);
if(ok){
break;
}
}
}
if(!ok&&!_49){
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
dojo.hostenv.startPackage=function(_52){
var _53=String(_52);
var _54=_53;
var _55=_52.split(/\./);
if(_55[_55.length-1]=="*"){
_55.pop();
_54=_55.join(".");
}
var _56=dojo.evalObjPath(_54,true);
this.loaded_modules_[_53]=_56;
this.loaded_modules_[_54]=_56;
return _56;
};
dojo.hostenv.findModule=function(_57,_58){
var lmn=String(_57);
if(this.loaded_modules_[lmn]){
return this.loaded_modules_[lmn];
}
if(_58){
dojo.raise("no loaded module named '"+_57+"'");
}
return null;
};
dojo.kwCompoundRequire=function(_5a){
var _5b=_5a["common"]||[];
var _5c=_5a[dojo.hostenv.name_]?_5b.concat(_5a[dojo.hostenv.name_]||[]):_5b.concat(_5a["default"]||[]);
for(var x=0;x<_5c.length;x++){
var _5e=_5c[x];
if(_5e.constructor==Array){
dojo.hostenv.loadModule.apply(dojo.hostenv,_5e);
}else{
dojo.hostenv.loadModule(_5e);
}
}
};
dojo.require=function(){
dojo.hostenv.loadModule.apply(dojo.hostenv,arguments);
};
dojo.requireIf=function(){
var _5f=arguments[0];
if((_5f===true)||(_5f=="common")||(_5f&&dojo.render[_5f].capable)){
var _60=[];
for(var i=1;i<arguments.length;i++){
_60.push(arguments[i]);
}
dojo.require.apply(dojo,_60);
}
};
dojo.requireAfterIf=dojo.requireIf;
dojo.provide=function(){
return dojo.hostenv.startPackage.apply(dojo.hostenv,arguments);
};
dojo.registerModulePath=function(_62,_63){
return dojo.hostenv.setModulePrefix(_62,_63);
};
dojo.setModulePrefix=function(_64,_65){
dojo.deprecated("dojo.setModulePrefix(\""+_64+"\", \""+_65+"\")","replaced by dojo.registerModulePath","0.5");
return dojo.registerModulePath(_64,_65);
};
dojo.exists=function(obj,_67){
var p=_67.split(".");
for(var i=0;i<p.length;i++){
if(!obj[p[i]]){
return false;
}
obj=obj[p[i]];
}
return true;
};
dojo.hostenv.normalizeLocale=function(_6a){
return _6a?_6a.toLowerCase():dojo.locale;
};
dojo.hostenv.searchLocalePath=function(_6b,_6c,_6d){
_6b=dojo.hostenv.normalizeLocale(_6b);
var _6e=_6b.split("-");
var _6f=[];
for(var i=_6e.length;i>0;i--){
_6f.push(_6e.slice(0,i).join("-"));
}
_6f.push(false);
if(_6c){
_6f.reverse();
}
for(var j=_6f.length-1;j>=0;j--){
var loc=_6f[j]||"ROOT";
var _73=_6d(loc);
if(_73){
break;
}
}
};
dojo.hostenv.preloadLocalizations=function(){
var _74;
if(_74){
dojo.registerModulePath("nls","nls");
function preload(_75){
_75=dojo.hostenv.normalizeLocale(_75);
dojo.hostenv.searchLocalePath(_75,true,function(loc){
for(var i=0;i<_74.length;i++){
if(_74[i]==loc){
dojo["require"]("nls.dojo_"+loc);
return true;
}
}
return false;
});
}
preload();
var _78=djConfig.extraLocale||[];
for(var i=0;i<_78.length;i++){
preload(_78[i]);
}
}
dojo.hostenv.preloadLocalizations=function(){
};
};
dojo.requireLocalization=function(_7a,_7b,_7c){
dojo.hostenv.preloadLocalizations();
var _7d=[_7a,"nls",_7b].join(".");
var _7e=dojo.hostenv.findModule(_7d);
if(_7e){
if(djConfig.localizationComplete&&_7e._built){
return;
}
var _7f=dojo.hostenv.normalizeLocale(_7c).replace("-","_");
var _80=_7d+"."+_7f;
if(dojo.hostenv.findModule(_80)){
return;
}
}
_7e=dojo.hostenv.startPackage(_7d);
var _81=dojo.hostenv.getModuleSymbols(_7a);
var _82=_81.concat("nls").join("/");
var _83;
dojo.hostenv.searchLocalePath(_7c,false,function(loc){
var _85=loc.replace("-","_");
var _86=_7d+"."+_85;
var _87=false;
if(!dojo.hostenv.findModule(_86)){
dojo.hostenv.startPackage(_86);
var _88=[_82];
if(loc!="ROOT"){
_88.push(loc);
}
_88.push(_7b);
var _89=_88.join("/")+".js";
_87=dojo.hostenv.loadPath(_89,null,function(_8a){
var _8b=function(){
};
_8b.prototype=_83;
_7e[_85]=new _8b();
for(var j in _8a){
_7e[_85][j]=_8a[j];
}
});
}else{
_87=true;
}
if(_87&&_7e[_85]){
_83=_7e[_85];
}else{
_7e[_85]=_83;
}
});
};
(function(){
var _8d=djConfig.extraLocale;
if(_8d){
if(!_8d instanceof Array){
_8d=[_8d];
}
var req=dojo.requireLocalization;
dojo.requireLocalization=function(m,b,_91){
req(m,b,_91);
if(_91){
return;
}
for(var i=0;i<_8d.length;i++){
req(m,b,_8d[i]);
}
};
}
})();
}
if(typeof window!="undefined"){
(function(){
if(djConfig.allowQueryConfig){
var _93=document.location.toString();
var _94=_93.split("?",2);
if(_94.length>1){
var _95=_94[1];
var _96=_95.split("&");
for(var x in _96){
var sp=_96[x].split("=");
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
var _9a=document.getElementsByTagName("script");
var _9b=/(__package__|dojo|bootstrap1)\.js([\?\.]|$)/i;
for(var i=0;i<_9a.length;i++){
var src=_9a[i].getAttribute("src");
if(!src){
continue;
}
var m=src.match(_9b);
if(m){
var _9f=src.substring(0,m.index);
if(src.indexOf("bootstrap1")>-1){
_9f+="../";
}
if(!this["djConfig"]){
djConfig={};
}
if(djConfig["baseScriptUri"]==""){
djConfig["baseScriptUri"]=_9f;
}
if(djConfig["baseRelativePath"]==""){
djConfig["baseRelativePath"]=_9f;
}
break;
}
}
}
var dr=dojo.render;
var drh=dojo.render.html;
var drs=dojo.render.svg;
var dua=(drh.UA=navigator.userAgent);
var dav=(drh.AV=navigator.appVersion);
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
var _a7=dua.indexOf("Gecko");
drh.mozilla=drh.moz=(_a7>=0)&&(!drh.khtml);
if(drh.mozilla){
drh.geckoVersion=dua.substring(_a7+6,_a7+14);
}
drh.ie=(document.all)&&(!drh.opera);
drh.ie50=drh.ie&&dav.indexOf("MSIE 5.0")>=0;
drh.ie55=drh.ie&&dav.indexOf("MSIE 5.5")>=0;
drh.ie60=drh.ie&&dav.indexOf("MSIE 6.0")>=0;
drh.ie70=drh.ie&&dav.indexOf("MSIE 7.0")>=0;
var cm=document["compatMode"];
drh.quirks=(cm=="BackCompat")||(cm=="QuirksMode")||drh.ie55||drh.ie50;
dojo.locale=dojo.locale||(drh.ie?navigator.userLanguage:navigator.language).toLowerCase();
dr.vml.capable=drh.ie;
drs.capable=f;
drs.support.plugin=f;
drs.support.builtin=f;
var _a9=window["document"];
var tdi=_a9["implementation"];
if((tdi)&&(tdi["hasFeature"])&&(tdi.hasFeature("org.w3c.dom.svg","1.0"))){
drs.capable=t;
drs.support.builtin=t;
drs.support.plugin=f;
}
if(drh.safari){
var tmp=dua.split("AppleWebKit/")[1];
var ver=parseFloat(tmp.split(" ")[0]);
if(ver>=420){
drs.capable=t;
drs.support.builtin=t;
drs.support.plugin=f;
}
}
})();
dojo.hostenv.startPackage("dojo.hostenv");
dojo.render.name=dojo.hostenv.name_="browser";
dojo.hostenv.searchIds=[];
dojo.hostenv._XMLHTTP_PROGIDS=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"];
dojo.hostenv.getXmlhttpObject=function(){
var _ad=null;
var _ae=null;
try{
_ad=new XMLHttpRequest();
}
catch(e){
}
if(!_ad){
for(var i=0;i<3;++i){
var _b0=dojo.hostenv._XMLHTTP_PROGIDS[i];
try{
_ad=new ActiveXObject(_b0);
}
catch(e){
_ae=e;
}
if(_ad){
dojo.hostenv._XMLHTTP_PROGIDS=[_b0];
break;
}
}
}
if(!_ad){
return dojo.raise("XMLHTTP not available",_ae);
}
return _ad;
};
dojo.hostenv._blockAsync=false;
dojo.hostenv.getText=function(uri,_b2,_b3){
if(!_b2){
this._blockAsync=true;
}
var _b4=this.getXmlhttpObject();
function isDocumentOk(_b5){
var _b6=_b5["status"];
return Boolean((!_b6)||((200<=_b6)&&(300>_b6))||(_b6==304));
}
if(_b2){
var _b7=this,_b8=null,gbl=dojo.global();
var xhr=dojo.evalObjPath("dojo.io.XMLHTTPTransport");
_b4.onreadystatechange=function(){
if(_b8){
gbl.clearTimeout(_b8);
_b8=null;
}
if(_b7._blockAsync||(xhr&&xhr._blockAsync)){
_b8=gbl.setTimeout(function(){
_b4.onreadystatechange.apply(this);
},10);
}else{
if(4==_b4.readyState){
if(isDocumentOk(_b4)){
_b2(_b4.responseText);
}
}
}
};
}
_b4.open("GET",uri,_b2?true:false);
try{
_b4.send(null);
if(_b2){
return null;
}
if(!isDocumentOk(_b4)){
var err=Error("Unable to load "+uri+" status:"+_b4.status);
err.status=_b4.status;
err.responseText=_b4.responseText;
throw err;
}
}
catch(e){
this._blockAsync=false;
if((_b3)&&(!_b2)){
return null;
}else{
throw e;
}
}
this._blockAsync=false;
return _b4.responseText;
};
dojo.hostenv.defaultDebugContainerId="dojoDebug";
dojo.hostenv._println_buffer=[];
dojo.hostenv._println_safe=false;
dojo.hostenv.println=function(_bc){
if(!dojo.hostenv._println_safe){
dojo.hostenv._println_buffer.push(_bc);
}else{
try{
var _bd=document.getElementById(djConfig.debugContainerId?djConfig.debugContainerId:dojo.hostenv.defaultDebugContainerId);
if(!_bd){
_bd=dojo.body();
}
var div=document.createElement("div");
div.appendChild(document.createTextNode(_bc));
_bd.appendChild(div);
}
catch(e){
try{
document.write("<div>"+_bc+"</div>");
}
catch(e2){
window.status=_bc;
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
function dj_addNodeEvtHdlr(_bf,_c0,fp,_c2){
var _c3=_bf["on"+_c0]||function(){
};
_bf["on"+_c0]=function(){
fp.apply(_bf,arguments);
_c3.apply(_bf,arguments);
};
return true;
}
function dj_load_init(e){
var _c5=(e&&e.type)?e.type.toLowerCase():"load";
if(arguments.callee.initialized||(_c5!="domcontentloaded"&&_c5!="load")){
return;
}
arguments.callee.initialized=true;
if(typeof (_timer)!="undefined"){
clearInterval(_timer);
delete _timer;
}
var _c6=function(){
if(dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
};
if(dojo.hostenv.inFlightCount==0){
_c6();
dojo.hostenv.modulesLoaded();
}else{
dojo.addOnLoad(_c6);
}
}
if(document.addEventListener){
document.addEventListener("DOMContentLoaded",dj_load_init,null);
document.addEventListener("load",dj_load_init,null);
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
dj_addNodeEvtHdlr(window,"unload",function(){
dojo.hostenv.unloaded();
});
dojo.hostenv.makeWidgets=function(){
var _c8=[];
if(djConfig.searchIds&&djConfig.searchIds.length>0){
_c8=_c8.concat(djConfig.searchIds);
}
if(dojo.hostenv.searchIds&&dojo.hostenv.searchIds.length>0){
_c8=_c8.concat(dojo.hostenv.searchIds);
}
if((djConfig.parseWidgets)||(_c8.length>0)){
if(dojo.evalObjPath("dojo.widget.Parse")){
var _c9=new dojo.xml.Parse();
if(_c8.length>0){
for(var x=0;x<_c8.length;x++){
var _cb=document.getElementById(_c8[x]);
if(!_cb){
continue;
}
var _cc=_c9.parseElement(_cb,null,true);
dojo.widget.getParser().createComponents(_cc);
}
}else{
if(djConfig.parseWidgets){
var _cc=_c9.parseElement(dojo.body(),null,true);
dojo.widget.getParser().createComponents(_cc);
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
dojo.setContext=function(_d1,_d2){
dj_currentContext=_d1;
dj_currentDocument=_d2;
};
dojo._fireCallback=function(_d3,_d4,_d5){
if((_d4)&&((typeof _d3=="string")||(_d3 instanceof String))){
_d3=_d4[_d3];
}
return (_d4?_d3.apply(_d4,_d5||[]):_d3());
};
dojo.withGlobal=function(_d6,_d7,_d8,_d9){
var _da;
var _db=dj_currentContext;
var _dc=dj_currentDocument;
try{
dojo.setContext(_d6,_d6.document);
_da=dojo._fireCallback(_d7,_d8,_d9);
}
finally{
dojo.setContext(_db,_dc);
}
return _da;
};
dojo.withDoc=function(_dd,_de,_df,_e0){
var _e1;
var _e2=dj_currentDocument;
try{
dj_currentDocument=_dd;
_e1=dojo._fireCallback(_de,_df,_e0);
}
finally{
dj_currentDocument=_e2;
}
return _e1;
};
}
(function(){
if(typeof dj_usingBootstrap!="undefined"){
return;
}
var _e3=false;
var _e4=false;
var _e5=false;
if((typeof this["load"]=="function")&&((typeof this["Packages"]=="function")||(typeof this["Packages"]=="object"))){
_e3=true;
}else{
if(typeof this["load"]=="function"){
_e4=true;
}else{
if(window.widget){
_e5=true;
}
}
}
var _e6=[];
if((this["djConfig"])&&((djConfig["isDebug"])||(djConfig["debugAtAllCosts"]))){
_e6.push("debug.js");
}
if((this["djConfig"])&&(djConfig["debugAtAllCosts"])&&(!_e3)&&(!_e5)){
_e6.push("browser_debug.js");
}
var _e7=djConfig["baseScriptUri"];
if((this["djConfig"])&&(djConfig["baseLoaderUri"])){
_e7=djConfig["baseLoaderUri"];
}
for(var x=0;x<_e6.length;x++){
var _e9=_e7+"src/"+_e6[x];
if(_e3||_e4){
load(_e9);
}else{
try{
document.write("<scr"+"ipt type='text/javascript' src='"+_e9+"'></scr"+"ipt>");
}
catch(e){
var _ea=document.createElement("script");
_ea.src=_e9;
document.getElementsByTagName("head")[0].appendChild(_ea);
}
}
}
})();
dojo.provide("dojo.lang.common");
dojo.lang.inherits=function(_eb,_ec){
if(typeof _ec!="function"){
dojo.raise("dojo.inherits: superclass argument ["+_ec+"] must be a function (subclass: ["+_eb+"']");
}
_eb.prototype=new _ec();
_eb.prototype.constructor=_eb;
_eb.superclass=_ec.prototype;
_eb["super"]=_ec.prototype;
};
dojo.lang._mixin=function(obj,_ee){
var _ef={};
for(var x in _ee){
if((typeof _ef[x]=="undefined")||(_ef[x]!=_ee[x])){
obj[x]=_ee[x];
}
}
if(dojo.render.html.ie&&(typeof (_ee["toString"])=="function")&&(_ee["toString"]!=obj["toString"])&&(_ee["toString"]!=_ef["toString"])){
obj.toString=_ee.toString;
}
return obj;
};
dojo.lang.mixin=function(obj,_f2){
for(var i=1,l=arguments.length;i<l;i++){
dojo.lang._mixin(obj,arguments[i]);
}
return obj;
};
dojo.lang.extend=function(_f5,_f6){
for(var i=1,l=arguments.length;i<l;i++){
dojo.lang._mixin(_f5.prototype,arguments[i]);
}
return _f5;
};
dojo.inherits=dojo.lang.inherits;
dojo.mixin=dojo.lang.mixin;
dojo.extend=dojo.lang.extend;
dojo.lang.find=function(_f9,_fa,_fb,_fc){
if(!dojo.lang.isArrayLike(_f9)&&dojo.lang.isArrayLike(_fa)){
dojo.deprecated("dojo.lang.find(value, array)","use dojo.lang.find(array, value) instead","0.5");
var _fd=_f9;
_f9=_fa;
_fa=_fd;
}
var _fe=dojo.lang.isString(_f9);
if(_fe){
_f9=_f9.split("");
}
if(_fc){
var _ff=-1;
var i=_f9.length-1;
var end=-1;
}else{
var _ff=1;
var i=0;
var end=_f9.length;
}
if(_fb){
while(i!=end){
if(_f9[i]===_fa){
return i;
}
i+=_ff;
}
}else{
while(i!=end){
if(_f9[i]==_fa){
return i;
}
i+=_ff;
}
}
return -1;
};
dojo.lang.indexOf=dojo.lang.find;
dojo.lang.findLast=function(_102,_103,_104){
return dojo.lang.find(_102,_103,_104,true);
};
dojo.lang.lastIndexOf=dojo.lang.findLast;
dojo.lang.inArray=function(_105,_106){
return dojo.lang.find(_105,_106)>-1;
};
dojo.lang.isObject=function(it){
if(typeof it=="undefined"){
return false;
}
return (typeof it=="object"||it===null||dojo.lang.isArray(it)||dojo.lang.isFunction(it));
};
dojo.lang.isArray=function(it){
return (it&&it instanceof Array||typeof it=="array");
};
dojo.lang.isArrayLike=function(it){
if((!it)||(dojo.lang.isUndefined(it))){
return false;
}
if(dojo.lang.isString(it)){
return false;
}
if(dojo.lang.isFunction(it)){
return false;
}
if(dojo.lang.isArray(it)){
return true;
}
if((it.tagName)&&(it.tagName.toLowerCase()=="form")){
return false;
}
if(dojo.lang.isNumber(it.length)&&isFinite(it.length)){
return true;
}
return false;
};
dojo.lang.isFunction=function(it){
if(!it){
return false;
}
if((typeof (it)=="function")&&(it=="[object NodeList]")){
return false;
}
return (it instanceof Function||typeof it=="function");
};
dojo.lang.isString=function(it){
return (typeof it=="string"||it instanceof String);
};
dojo.lang.isAlien=function(it){
if(!it){
return false;
}
return !dojo.lang.isFunction()&&/\{\s*\[native code\]\s*\}/.test(String(it));
};
dojo.lang.isBoolean=function(it){
return (it instanceof Boolean||typeof it=="boolean");
};
dojo.lang.isNumber=function(it){
return (it instanceof Number||typeof it=="number");
};
dojo.lang.isUndefined=function(it){
return ((typeof (it)=="undefined")&&(it==undefined));
};
dojo.provide("dojo.lang.array");
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
var _114=0;
for(var x in obj){
if(obj[x]&&(!tmp[x])){
_114++;
break;
}
}
return (_114==0);
}else{
if(dojo.lang.isArrayLike(obj)||dojo.lang.isString(obj)){
return obj.length==0;
}
}
};
dojo.lang.map=function(arr,obj,_118){
var _119=dojo.lang.isString(arr);
if(_119){
arr=arr.split("");
}
if(dojo.lang.isFunction(obj)&&(!_118)){
_118=obj;
obj=dj_global;
}else{
if(dojo.lang.isFunction(obj)&&_118){
var _11a=obj;
obj=_118;
_118=_11a;
}
}
if(Array.map){
var _11b=Array.map(arr,_118,obj);
}else{
var _11b=[];
for(var i=0;i<arr.length;++i){
_11b.push(_118.call(obj,arr[i]));
}
}
if(_119){
return _11b.join("");
}else{
return _11b;
}
};
dojo.lang.reduce=function(arr,_11e,obj,_120){
var _121=_11e;
var ob=obj?obj:dj_global;
dojo.lang.map(arr,function(val){
_121=_120.call(ob,_121,val);
});
return _121;
};
dojo.lang.forEach=function(_124,_125,_126){
if(dojo.lang.isString(_124)){
_124=_124.split("");
}
if(Array.forEach){
Array.forEach(_124,_125,_126);
}else{
if(!_126){
_126=dj_global;
}
for(var i=0,l=_124.length;i<l;i++){
_125.call(_126,_124[i],i,_124);
}
}
};
dojo.lang._everyOrSome=function(_129,arr,_12b,_12c){
if(dojo.lang.isString(arr)){
arr=arr.split("");
}
if(Array.every){
return Array[(_129)?"every":"some"](arr,_12b,_12c);
}else{
if(!_12c){
_12c=dj_global;
}
for(var i=0,l=arr.length;i<l;i++){
var _12f=_12b.call(_12c,arr[i],i,arr);
if((_129)&&(!_12f)){
return false;
}else{
if((!_129)&&(_12f)){
return true;
}
}
}
return (_129)?true:false;
}
};
dojo.lang.every=function(arr,_131,_132){
return this._everyOrSome(true,arr,_131,_132);
};
dojo.lang.some=function(arr,_134,_135){
return this._everyOrSome(false,arr,_134,_135);
};
dojo.lang.filter=function(arr,_137,_138){
var _139=dojo.lang.isString(arr);
if(_139){
arr=arr.split("");
}
if(Array.filter){
var _13a=Array.filter(arr,_137,_138);
}else{
if(!_138){
if(arguments.length>=3){
dojo.raise("thisObject doesn't exist!");
}
_138=dj_global;
}
var _13a=[];
for(var i=0;i<arr.length;i++){
if(_137.call(_138,arr[i],i,arr)){
_13a.push(arr[i]);
}
}
}
if(_139){
return _13a.join("");
}else{
return _13a;
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
dojo.lang.toArray=function(_13f,_140){
var _141=[];
for(var i=_140||0;i<_13f.length;i++){
_141.push(_13f[i]);
}
return _141;
};
dojo.provide("dojo.lang.extras");
dojo.lang.setTimeout=function(func,_144){
var _145=window,_146=2;
if(!dojo.lang.isFunction(func)){
_145=func;
func=_144;
_144=arguments[2];
_146++;
}
if(dojo.lang.isString(func)){
func=_145[func];
}
var args=[];
for(var i=_146;i<arguments.length;i++){
args.push(arguments[i]);
}
return dojo.global().setTimeout(function(){
func.apply(_145,args);
},_144);
};
dojo.lang.clearTimeout=function(_149){
dojo.global().clearTimeout(_149);
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
dojo.lang.shallowCopy=function(obj,deep){
var i,ret;
if(obj===null){
return null;
}
if(dojo.lang.isObject(obj)){
ret=new obj.constructor();
for(i in obj){
if(dojo.lang.isUndefined(ret[i])){
ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];
}
}
}else{
if(dojo.lang.isArray(obj)){
ret=[];
for(i=0;i<obj.length;i++){
ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];
}
}else{
ret=obj;
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
dojo.lang.getObjPathValue=function(_152,_153,_154){
with(dojo.parseObjPath(_152,_153,_154)){
return dojo.evalProp(prop,obj,_154);
}
};
dojo.lang.setObjPathValue=function(_155,_156,_157,_158){
if(arguments.length<4){
_158=true;
}
with(dojo.parseObjPath(_155,_157,_158)){
if(obj&&(_158||(prop in obj))){
obj[prop]=_156;
}
}
};
dojo.provide("dojo.lang.func");
dojo.lang.hitch=function(_159,_15a){
var fcn=(dojo.lang.isString(_15a)?_159[_15a]:_15a)||function(){
};
return function(){
return fcn.apply(_159,arguments);
};
};
dojo.lang.anonCtr=0;
dojo.lang.anon={};
dojo.lang.nameAnonFunc=function(_15c,_15d,_15e){
var nso=(_15d||dojo.lang.anon);
if((_15e)||((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true))){
for(var x in nso){
try{
if(nso[x]===_15c){
return x;
}
}
catch(e){
}
}
}
var ret="__"+dojo.lang.anonCtr++;
while(typeof nso[ret]!="undefined"){
ret="__"+dojo.lang.anonCtr++;
}
nso[ret]=_15c;
return ret;
};
dojo.lang.forward=function(_162){
return function(){
return this[_162].apply(this,arguments);
};
};
dojo.lang.curry=function(ns,func){
var _165=[];
ns=ns||dj_global;
if(dojo.lang.isString(func)){
func=ns[func];
}
for(var x=2;x<arguments.length;x++){
_165.push(arguments[x]);
}
var _167=(func["__preJoinArity"]||func.length)-_165.length;
function gather(_168,_169,_16a){
var _16b=_16a;
var _16c=_169.slice(0);
for(var x=0;x<_168.length;x++){
_16c.push(_168[x]);
}
_16a=_16a-_168.length;
if(_16a<=0){
var res=func.apply(ns,_16c);
_16a=_16b;
return res;
}else{
return function(){
return gather(arguments,_16c,_16a);
};
}
}
return gather([],_165,_167);
};
dojo.lang.curryArguments=function(ns,func,args,_172){
var _173=[];
var x=_172||0;
for(x=_172;x<args.length;x++){
_173.push(args[x]);
}
return dojo.lang.curry.apply(dojo.lang,[ns,func].concat(_173));
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
dojo.lang.delayThese=function(farr,cb,_179,_17a){
if(!farr.length){
if(typeof _17a=="function"){
_17a();
}
return;
}
if((typeof _179=="undefined")&&(typeof cb=="number")){
_179=cb;
cb=function(){
};
}else{
if(!cb){
cb=function(){
};
if(!_179){
_179=0;
}
}
}
setTimeout(function(){
(farr.shift())();
cb();
dojo.lang.delayThese(farr,cb,_179,_17a);
},_179);
};
dojo.provide("dojo.event.common");
dojo.event=new function(){
this._canTimeout=dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);
function interpolateArgs(args,_17c){
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
var _17f=dl.nameAnonFunc(args[2],ao.adviceObj,_17c);
ao.adviceFunc=_17f;
}else{
if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){
ao.adviceType="after";
ao.srcObj=dj_global;
var _17f=dl.nameAnonFunc(args[0],ao.srcObj,_17c);
ao.srcFunc=_17f;
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
var _17f=dl.nameAnonFunc(args[1],dj_global,_17c);
ao.srcFunc=_17f;
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){
ao.srcObj=args[1];
ao.srcFunc=args[2];
var _17f=dl.nameAnonFunc(args[3],dj_global,_17c);
ao.adviceObj=dj_global;
ao.adviceFunc=_17f;
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
var _17f=dl.nameAnonFunc(ao.aroundFunc,ao.aroundObj,_17c);
ao.aroundFunc=_17f;
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
if(!ao.adviceFunc){
dojo.debug("bad adviceFunc for srcFunc: "+ao.srcFunc);
dojo.debugShallow(ao);
}
return ao;
}
this.connect=function(){
if(arguments.length==1){
var ao=arguments[0];
}else{
var ao=interpolateArgs(arguments,true);
}
if(dojo.lang.isString(ao.srcFunc)&&(ao.srcFunc.toLowerCase()=="onkey")){
if(dojo.render.html.ie){
ao.srcFunc="onkeydown";
this.connect(ao);
}
ao.srcFunc="onkeypress";
}
if(dojo.lang.isArray(ao.srcObj)&&ao.srcObj!=""){
var _181={};
for(var x in ao){
_181[x]=ao[x];
}
var mjps=[];
dojo.lang.forEach(ao.srcObj,function(src){
if((dojo.render.html.capable)&&(dojo.lang.isString(src))){
src=dojo.byId(src);
}
_181.srcObj=src;
mjps.push(dojo.event.connect.call(dojo.event,_181));
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
var _189;
if((arguments.length==1)&&(typeof a1=="object")){
_189=a1;
}else{
_189={srcObj:a1,srcFunc:a2};
}
_189.adviceFunc=function(){
var _18a=[];
for(var x=0;x<arguments.length;x++){
_18a.push(arguments[x]);
}
dojo.debug("("+_189.srcObj+")."+_189.srcFunc,":",_18a.join(", "));
};
this.kwConnect(_189);
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
this._kwConnectImpl=function(_191,_192){
var fn=(_192)?"disconnect":"connect";
if(typeof _191["srcFunc"]=="function"){
_191.srcObj=_191["srcObj"]||dj_global;
var _194=dojo.lang.nameAnonFunc(_191.srcFunc,_191.srcObj,true);
_191.srcFunc=_194;
}
if(typeof _191["adviceFunc"]=="function"){
_191.adviceObj=_191["adviceObj"]||dj_global;
var _194=dojo.lang.nameAnonFunc(_191.adviceFunc,_191.adviceObj,true);
_191.adviceFunc=_194;
}
_191.srcObj=_191["srcObj"]||dj_global;
_191.adviceObj=_191["adviceObj"]||_191["targetObj"]||dj_global;
_191.adviceFunc=_191["adviceFunc"]||_191["targetFunc"];
return dojo.event[fn](_191);
};
this.kwConnect=function(_195){
return this._kwConnectImpl(_195,false);
};
this.disconnect=function(){
if(arguments.length==1){
var ao=arguments[0];
}else{
var ao=interpolateArgs(arguments,true);
}
if(!ao.adviceFunc){
return;
}
if(dojo.lang.isString(ao.srcFunc)&&(ao.srcFunc.toLowerCase()=="onkey")){
if(dojo.render.html.ie){
ao.srcFunc="onkeydown";
this.disconnect(ao);
}
ao.srcFunc="onkeypress";
}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);
return mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);
};
this.kwDisconnect=function(_198){
return this._kwConnectImpl(_198,true);
};
};
dojo.event.MethodInvocation=function(_199,obj,args){
this.jp_=_199;
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
dojo.event.MethodJoinPoint=function(obj,_1a1){
this.object=obj||dj_global;
this.methodname=_1a1;
this.methodfunc=this.object[_1a1];
this.squelch=false;
};
dojo.event.MethodJoinPoint.getForMethod=function(obj,_1a3){
if(!obj){
obj=dj_global;
}
if(!obj[_1a3]){
obj[_1a3]=function(){
};
if(!obj[_1a3]){
dojo.raise("Cannot set do-nothing method on that object "+_1a3);
}
}else{
if((!dojo.lang.isFunction(obj[_1a3]))&&(!dojo.lang.isAlien(obj[_1a3]))){
return null;
}
}
var _1a4=_1a3+"$joinpoint";
var _1a5=_1a3+"$joinpoint$method";
var _1a6=obj[_1a4];
if(!_1a6){
var _1a7=false;
if(dojo.event["browser"]){
if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){
_1a7=true;
dojo.event.browser.addClobberNodeAttrs(obj,[_1a4,_1a5,_1a3]);
}
}
var _1a8=obj[_1a3].length;
obj[_1a5]=obj[_1a3];
_1a6=obj[_1a4]=new dojo.event.MethodJoinPoint(obj,_1a5);
obj[_1a3]=function(){
var args=[];
if((_1a7)&&(!arguments.length)){
var evt=null;
try{
if(obj.ownerDocument){
evt=obj.ownerDocument.parentWindow.event;
}else{
if(obj.documentElement){
evt=obj.documentElement.ownerDocument.parentWindow.event;
}else{
if(obj.event){
evt=obj.event;
}else{
evt=window.event;
}
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
if((x==0)&&(_1a7)&&(dojo.event.browser.isEvent(arguments[x]))){
args.push(dojo.event.browser.fixEvent(arguments[x],this));
}else{
args.push(arguments[x]);
}
}
}
return _1a6.run.apply(_1a6,args);
};
obj[_1a3].__preJoinArity=_1a8;
}
return _1a6;
};
dojo.lang.extend(dojo.event.MethodJoinPoint,{unintercept:function(){
this.object[this.methodname]=this.methodfunc;
this.before=[];
this.after=[];
this.around=[];
},disconnect:dojo.lang.forward("unintercept"),run:function(){
var obj=this.object||dj_global;
var args=arguments;
var _1ae=[];
for(var x=0;x<args.length;x++){
_1ae[x]=args[x];
}
var _1b0=function(marr){
if(!marr){
dojo.debug("Null argument to unrollAdvice()");
return;
}
var _1b2=marr[0]||dj_global;
var _1b3=marr[1];
if(!_1b2[_1b3]){
dojo.raise("function \""+_1b3+"\" does not exist on \""+_1b2+"\"");
}
var _1b4=marr[2]||dj_global;
var _1b5=marr[3];
var msg=marr[6];
var _1b7;
var to={args:[],jp_:this,object:obj,proceed:function(){
return _1b2[_1b3].apply(_1b2,to.args);
}};
to.args=_1ae;
var _1b9=parseInt(marr[4]);
var _1ba=((!isNaN(_1b9))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));
if(marr[5]){
var rate=parseInt(marr[5]);
var cur=new Date();
var _1bd=false;
if((marr["last"])&&((cur-marr.last)<=rate)){
if(dojo.event._canTimeout){
if(marr["delayTimer"]){
clearTimeout(marr.delayTimer);
}
var tod=parseInt(rate*2);
var mcpy=dojo.lang.shallowCopy(marr);
marr.delayTimer=setTimeout(function(){
mcpy[5]=0;
_1b0(mcpy);
},tod);
}
return;
}else{
marr.last=cur;
}
}
if(_1b5){
_1b4[_1b5].call(_1b4,to);
}else{
if((_1ba)&&((dojo.render.html)||(dojo.render.svg))){
dj_global["setTimeout"](function(){
if(msg){
_1b2[_1b3].call(_1b2,to);
}else{
_1b2[_1b3].apply(_1b2,args);
}
},_1b9);
}else{
if(msg){
_1b2[_1b3].call(_1b2,to);
}else{
_1b2[_1b3].apply(_1b2,args);
}
}
}
};
var _1c0=function(){
if(this.squelch){
try{
return _1b0.apply(this,arguments);
}
catch(e){
dojo.debug(e);
}
}else{
return _1b0.apply(this,arguments);
}
};
if((this["before"])&&(this.before.length>0)){
dojo.lang.forEach(this.before.concat(new Array()),_1c0);
}
var _1c1;
try{
if((this["around"])&&(this.around.length>0)){
var mi=new dojo.event.MethodInvocation(this,obj,args);
_1c1=mi.proceed();
}else{
if(this.methodfunc){
_1c1=this.object[this.methodname].apply(this.object,args);
}
}
}
catch(e){
if(!this.squelch){
dojo.raise(e);
}
}
if((this["after"])&&(this.after.length>0)){
dojo.lang.forEach(this.after.concat(new Array()),_1c0);
}
return (this.methodfunc)?_1c1:null;
},getArr:function(kind){
var type="after";
if((typeof kind=="string")&&(kind.indexOf("before")!=-1)){
type="before";
}else{
if(kind=="around"){
type="around";
}
}
if(!this[type]){
this[type]=[];
}
return this[type];
},kwAddAdvice:function(args){
this.addAdvice(args["adviceObj"],args["adviceFunc"],args["aroundObj"],args["aroundFunc"],args["adviceType"],args["precedence"],args["once"],args["delay"],args["rate"],args["adviceMsg"]);
},addAdvice:function(_1c6,_1c7,_1c8,_1c9,_1ca,_1cb,once,_1cd,rate,_1cf){
var arr=this.getArr(_1ca);
if(!arr){
dojo.raise("bad this: "+this);
}
var ao=[_1c6,_1c7,_1c8,_1c9,_1cd,rate,_1cf];
if(once){
if(this.hasAdvice(_1c6,_1c7,_1ca,arr)>=0){
return;
}
}
if(_1cb=="first"){
arr.unshift(ao);
}else{
arr.push(ao);
}
},hasAdvice:function(_1d2,_1d3,_1d4,arr){
if(!arr){
arr=this.getArr(_1d4);
}
var ind=-1;
for(var x=0;x<arr.length;x++){
var aao=(typeof _1d3=="object")?(new String(_1d3)).toString():_1d3;
var a1o=(typeof arr[x][1]=="object")?(new String(arr[x][1])).toString():arr[x][1];
if((arr[x][0]==_1d2)&&(a1o==aao)){
ind=x;
}
}
return ind;
},removeAdvice:function(_1da,_1db,_1dc,once){
var arr=this.getArr(_1dc);
var ind=this.hasAdvice(_1da,_1db,_1dc,arr);
if(ind==-1){
return false;
}
while(ind!=-1){
arr.splice(ind,1);
if(once){
break;
}
ind=this.hasAdvice(_1da,_1db,_1dc,arr);
}
return true;
}});
dojo.provide("dojo.event.browser");
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
this.clobber=function(_1e2){
var na;
var tna;
if(_1e2){
tna=_1e2.all||_1e2.getElementsByTagName("*");
na=[_1e2];
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
var _1e6={};
for(var i=na.length-1;i>=0;i=i-1){
var el=na[i];
try{
if(el&&el["__clobberAttrs__"]){
for(var j=0;j<el.__clobberAttrs__.length;j++){
nukeProp(el,el.__clobberAttrs__[j]);
}
nukeProp(el,"__clobberAttrs__");
nukeProp(el,"__doClobber__");
}
}
catch(e){
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
var _1ea=0;
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
this.addClobberNodeAttrs=function(node,_1ee){
if(!dojo.render.html.ie){
return;
}
this.addClobberNode(node);
for(var x=0;x<_1ee.length;x++){
node.__clobberAttrs__.push(_1ee[x]);
}
};
this.removeListener=function(node,_1f1,fp,_1f3){
if(!_1f3){
var _1f3=false;
}
_1f1=_1f1.toLowerCase();
if((_1f1=="onkey")||(_1f1=="key")){
if(dojo.render.html.ie){
this.removeListener(node,"onkeydown",fp,_1f3);
}
_1f1="onkeypress";
}
if(_1f1.substr(0,2)=="on"){
_1f1=_1f1.substr(2);
}
if(node.removeEventListener){
node.removeEventListener(_1f1,fp,_1f3);
}
};
this.addListener=function(node,_1f5,fp,_1f7,_1f8){
if(!node){
return;
}
if(!_1f7){
var _1f7=false;
}
_1f5=_1f5.toLowerCase();
if((_1f5=="onkey")||(_1f5=="key")){
if(dojo.render.html.ie){
this.addListener(node,"onkeydown",fp,_1f7,_1f8);
}
_1f5="onkeypress";
}
if(_1f5.substr(0,2)!="on"){
_1f5="on"+_1f5;
}
if(!_1f8){
var _1f9=function(evt){
if(!evt){
evt=window.event;
}
var ret=fp(dojo.event.browser.fixEvent(evt,this));
if(_1f7){
dojo.event.browser.stopEvent(evt);
}
return ret;
};
}else{
_1f9=fp;
}
if(node.addEventListener){
node.addEventListener(_1f5.substr(2),_1f9,_1f7);
return _1f9;
}else{
if(typeof node[_1f5]=="function"){
var _1fc=node[_1f5];
node[_1f5]=function(e){
_1fc(e);
return _1f9(e);
};
}else{
node[_1f5]=_1f9;
}
if(dojo.render.html.ie){
this.addClobberNodeAttrs(node,[_1f5]);
}
return _1f9;
}
};
this.isEvent=function(obj){
return (typeof obj!="undefined")&&(typeof Event!="undefined")&&(obj.eventPhase);
};
this.currentEvent=null;
this.callListener=function(_1ff,_200){
if(typeof _1ff!="function"){
dojo.raise("listener not a function: "+_1ff);
}
dojo.event.browser.currentEvent.currentTarget=_200;
return _1ff.call(_200,dojo.event.browser.currentEvent);
};
this.stopPropagation=function(){
dojo.event.browser.currentEvent.cancelBubble=true;
};
this.preventDefault=function(){
dojo.event.browser.currentEvent.returnValue=false;
};
this.keys={KEY_BACKSPACE:8,KEY_TAB:9,KEY_CLEAR:12,KEY_ENTER:13,KEY_SHIFT:16,KEY_CTRL:17,KEY_ALT:18,KEY_PAUSE:19,KEY_CAPS_LOCK:20,KEY_ESCAPE:27,KEY_SPACE:32,KEY_PAGE_UP:33,KEY_PAGE_DOWN:34,KEY_END:35,KEY_HOME:36,KEY_LEFT_ARROW:37,KEY_UP_ARROW:38,KEY_RIGHT_ARROW:39,KEY_DOWN_ARROW:40,KEY_INSERT:45,KEY_DELETE:46,KEY_HELP:47,KEY_LEFT_WINDOW:91,KEY_RIGHT_WINDOW:92,KEY_SELECT:93,KEY_NUMPAD_0:96,KEY_NUMPAD_1:97,KEY_NUMPAD_2:98,KEY_NUMPAD_3:99,KEY_NUMPAD_4:100,KEY_NUMPAD_5:101,KEY_NUMPAD_6:102,KEY_NUMPAD_7:103,KEY_NUMPAD_8:104,KEY_NUMPAD_9:105,KEY_NUMPAD_MULTIPLY:106,KEY_NUMPAD_PLUS:107,KEY_NUMPAD_ENTER:108,KEY_NUMPAD_MINUS:109,KEY_NUMPAD_PERIOD:110,KEY_NUMPAD_DIVIDE:111,KEY_F1:112,KEY_F2:113,KEY_F3:114,KEY_F4:115,KEY_F5:116,KEY_F6:117,KEY_F7:118,KEY_F8:119,KEY_F9:120,KEY_F10:121,KEY_F11:122,KEY_F12:123,KEY_F13:124,KEY_F14:125,KEY_F15:126,KEY_NUM_LOCK:144,KEY_SCROLL_LOCK:145};
this.revKeys=[];
for(var key in this.keys){
this.revKeys[this.keys[key]]=key;
}
this.fixEvent=function(evt,_203){
if(!evt){
if(window["event"]){
evt=window.event;
}
}
if((evt["type"])&&(evt["type"].indexOf("key")==0)){
evt.keys=this.revKeys;
for(var key in this.keys){
evt[key]=this.keys[key];
}
if(evt["type"]=="keydown"&&dojo.render.html.ie){
switch(evt.keyCode){
case evt.KEY_SHIFT:
case evt.KEY_CTRL:
case evt.KEY_ALT:
case evt.KEY_CAPS_LOCK:
case evt.KEY_LEFT_WINDOW:
case evt.KEY_RIGHT_WINDOW:
case evt.KEY_SELECT:
case evt.KEY_NUM_LOCK:
case evt.KEY_SCROLL_LOCK:
case evt.KEY_NUMPAD_0:
case evt.KEY_NUMPAD_1:
case evt.KEY_NUMPAD_2:
case evt.KEY_NUMPAD_3:
case evt.KEY_NUMPAD_4:
case evt.KEY_NUMPAD_5:
case evt.KEY_NUMPAD_6:
case evt.KEY_NUMPAD_7:
case evt.KEY_NUMPAD_8:
case evt.KEY_NUMPAD_9:
case evt.KEY_NUMPAD_PERIOD:
break;
case evt.KEY_NUMPAD_MULTIPLY:
case evt.KEY_NUMPAD_PLUS:
case evt.KEY_NUMPAD_ENTER:
case evt.KEY_NUMPAD_MINUS:
case evt.KEY_NUMPAD_DIVIDE:
break;
case evt.KEY_PAUSE:
case evt.KEY_TAB:
case evt.KEY_BACKSPACE:
case evt.KEY_ENTER:
case evt.KEY_ESCAPE:
case evt.KEY_PAGE_UP:
case evt.KEY_PAGE_DOWN:
case evt.KEY_END:
case evt.KEY_HOME:
case evt.KEY_LEFT_ARROW:
case evt.KEY_UP_ARROW:
case evt.KEY_RIGHT_ARROW:
case evt.KEY_DOWN_ARROW:
case evt.KEY_INSERT:
case evt.KEY_DELETE:
case evt.KEY_F1:
case evt.KEY_F2:
case evt.KEY_F3:
case evt.KEY_F4:
case evt.KEY_F5:
case evt.KEY_F6:
case evt.KEY_F7:
case evt.KEY_F8:
case evt.KEY_F9:
case evt.KEY_F10:
case evt.KEY_F11:
case evt.KEY_F12:
case evt.KEY_F12:
case evt.KEY_F13:
case evt.KEY_F14:
case evt.KEY_F15:
case evt.KEY_CLEAR:
case evt.KEY_HELP:
evt.key=evt.keyCode;
break;
default:
if(evt.ctrlKey||evt.altKey){
var _205=evt.keyCode;
if(_205>=65&&_205<=90&&evt.shiftKey==false){
_205+=32;
}
if(_205>=1&&_205<=26&&evt.ctrlKey){
_205+=96;
}
evt.key=String.fromCharCode(_205);
}
}
}else{
if(evt["type"]=="keypress"){
if(dojo.render.html.opera){
if(evt.which==0){
evt.key=evt.keyCode;
}else{
if(evt.which>0){
switch(evt.which){
case evt.KEY_SHIFT:
case evt.KEY_CTRL:
case evt.KEY_ALT:
case evt.KEY_CAPS_LOCK:
case evt.KEY_NUM_LOCK:
case evt.KEY_SCROLL_LOCK:
break;
case evt.KEY_PAUSE:
case evt.KEY_TAB:
case evt.KEY_BACKSPACE:
case evt.KEY_ENTER:
case evt.KEY_ESCAPE:
evt.key=evt.which;
break;
default:
var _205=evt.which;
if((evt.ctrlKey||evt.altKey||evt.metaKey)&&(evt.which>=65&&evt.which<=90&&evt.shiftKey==false)){
_205+=32;
}
evt.key=String.fromCharCode(_205);
}
}
}
}else{
if(dojo.render.html.ie){
if(!evt.ctrlKey&&!evt.altKey&&evt.keyCode>=evt.KEY_SPACE){
evt.key=String.fromCharCode(evt.keyCode);
}
}else{
if(dojo.render.html.safari){
switch(evt.keyCode){
case 63232:
evt.key=evt.KEY_UP_ARROW;
break;
case 63233:
evt.key=evt.KEY_DOWN_ARROW;
break;
case 63234:
evt.key=evt.KEY_LEFT_ARROW;
break;
case 63235:
evt.key=evt.KEY_RIGHT_ARROW;
break;
default:
evt.key=evt.charCode>0?String.fromCharCode(evt.charCode):evt.keyCode;
}
}else{
evt.key=evt.charCode>0?String.fromCharCode(evt.charCode):evt.keyCode;
}
}
}
}
}
}
if(dojo.render.html.ie){
if(!evt.target){
evt.target=evt.srcElement;
}
if(!evt.currentTarget){
evt.currentTarget=(_203?_203:evt.srcElement);
}
if(!evt.layerX){
evt.layerX=evt.offsetX;
}
if(!evt.layerY){
evt.layerY=evt.offsetY;
}
var doc=(evt.srcElement&&evt.srcElement.ownerDocument)?evt.srcElement.ownerDocument:document;
var _207=((dojo.render.html.ie55)||(doc["compatMode"]=="BackCompat"))?doc.body:doc.documentElement;
if(!evt.pageX){
evt.pageX=evt.clientX+(_207.scrollLeft||0);
}
if(!evt.pageY){
evt.pageY=evt.clientY+(_207.scrollTop||0);
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
dojo.provide("dojo.dom");
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
if(typeof Element=="function"){
try{
return wh instanceof Element;
}
catch(E){
}
}else{
return wh&&!isNaN(wh.nodeType);
}
};
dojo.dom.getUniqueId=function(){
var _20a=dojo.doc();
do{
var id="dj_unique_"+(++arguments.callee._idIncrement);
}while(_20a.getElementById(id));
return id;
};
dojo.dom.getUniqueId._idIncrement=0;
dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_20c,_20d){
var node=_20c.firstChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.nextSibling;
}
if(_20d&&node&&node.tagName&&node.tagName.toLowerCase()!=_20d.toLowerCase()){
node=dojo.dom.nextElement(node,_20d);
}
return node;
};
dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_20f,_210){
var node=_20f.lastChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.previousSibling;
}
if(_210&&node&&node.tagName&&node.tagName.toLowerCase()!=_210.toLowerCase()){
node=dojo.dom.prevElement(node,_210);
}
return node;
};
dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(node,_213){
if(!node){
return null;
}
do{
node=node.nextSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_213&&_213.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.nextElement(node,_213);
}
return node;
};
dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(node,_215){
if(!node){
return null;
}
if(_215){
_215=_215.toLowerCase();
}
do{
node=node.previousSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_215&&_215.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.prevElement(node,_215);
}
return node;
};
dojo.dom.moveChildren=function(_216,_217,trim){
var _219=0;
if(trim){
while(_216.hasChildNodes()&&_216.firstChild.nodeType==dojo.dom.TEXT_NODE){
_216.removeChild(_216.firstChild);
}
while(_216.hasChildNodes()&&_216.lastChild.nodeType==dojo.dom.TEXT_NODE){
_216.removeChild(_216.lastChild);
}
}
while(_216.hasChildNodes()){
_217.appendChild(_216.firstChild);
_219++;
}
return _219;
};
dojo.dom.copyChildren=function(_21a,_21b,trim){
var _21d=_21a.cloneNode(true);
return this.moveChildren(_21d,_21b,trim);
};
dojo.dom.removeChildren=function(node){
var _21f=node.childNodes.length;
while(node.hasChildNodes()){
node.removeChild(node.firstChild);
}
return _21f;
};
dojo.dom.replaceChildren=function(node,_221){
dojo.dom.removeChildren(node);
node.appendChild(_221);
};
dojo.dom.removeNode=function(node){
if(node&&node.parentNode){
return node.parentNode.removeChild(node);
}
};
dojo.dom.getAncestors=function(node,_224,_225){
var _226=[];
var _227=(_224&&(_224 instanceof Function||typeof _224=="function"));
while(node){
if(!_227||_224(node)){
_226.push(node);
}
if(_225&&_226.length>0){
return _226[0];
}
node=node.parentNode;
}
if(_225){
return null;
}
return _226;
};
dojo.dom.getAncestorsByTag=function(node,tag,_22a){
tag=tag.toLowerCase();
return dojo.dom.getAncestors(node,function(el){
return ((el.tagName)&&(el.tagName.toLowerCase()==tag));
},_22a);
};
dojo.dom.getFirstAncestorByTag=function(node,tag){
return dojo.dom.getAncestorsByTag(node,tag,true);
};
dojo.dom.isDescendantOf=function(node,_22f,_230){
if(_230&&node){
node=node.parentNode;
}
while(node){
if(node==_22f){
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
var _233=dojo.doc();
if(!dj_undef("ActiveXObject")){
var _234=["MSXML2","Microsoft","MSXML","MSXML3"];
for(var i=0;i<_234.length;i++){
try{
doc=new ActiveXObject(_234[i]+".XMLDOM");
}
catch(e){
}
if(doc){
break;
}
}
}else{
if((_233.implementation)&&(_233.implementation.createDocument)){
doc=_233.implementation.createDocument("","",null);
}
}
return doc;
};
dojo.dom.createDocumentFromText=function(str,_237){
if(!_237){
_237="text/xml";
}
if(!dj_undef("DOMParser")){
var _238=new DOMParser();
return _238.parseFromString(str,_237);
}else{
if(!dj_undef("ActiveXObject")){
var _239=dojo.dom.createDocument();
if(_239){
_239.async=false;
_239.loadXML(str);
return _239;
}else{
dojo.debug("toXml didn't work?");
}
}else{
var _23a=dojo.doc();
if(_23a.createElement){
var tmp=_23a.createElement("xml");
tmp.innerHTML=str;
if(_23a.implementation&&_23a.implementation.createDocument){
var _23c=_23a.implementation.createDocument("foo","",null);
for(var i=0;i<tmp.childNodes.length;i++){
_23c.importNode(tmp.childNodes.item(i),true);
}
return _23c;
}
return ((tmp.document)&&(tmp.document.firstChild?tmp.document.firstChild:tmp));
}
}
}
return null;
};
dojo.dom.prependChild=function(node,_23f){
if(_23f.firstChild){
_23f.insertBefore(node,_23f.firstChild);
}else{
_23f.appendChild(node);
}
return true;
};
dojo.dom.insertBefore=function(node,ref,_242){
if(_242!=true&&(node===ref||node.nextSibling===ref)){
return false;
}
var _243=ref.parentNode;
_243.insertBefore(node,ref);
return true;
};
dojo.dom.insertAfter=function(node,ref,_246){
var pn=ref.parentNode;
if(ref==pn.lastChild){
if((_246!=true)&&(node===ref)){
return false;
}
pn.appendChild(node);
}else{
return this.insertBefore(node,ref.nextSibling,_246);
}
return true;
};
dojo.dom.insertAtPosition=function(node,ref,_24a){
if((!node)||(!ref)||(!_24a)){
return false;
}
switch(_24a.toLowerCase()){
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
dojo.dom.insertAtIndex=function(node,_24c,_24d){
var _24e=_24c.childNodes;
if(!_24e.length){
_24c.appendChild(node);
return true;
}
var _24f=null;
for(var i=0;i<_24e.length;i++){
var _251=_24e.item(i)["getAttribute"]?parseInt(_24e.item(i).getAttribute("dojoinsertionindex")):-1;
if(_251<_24d){
_24f=_24e.item(i);
}
}
if(_24f){
return dojo.dom.insertAfter(node,_24f);
}else{
return dojo.dom.insertBefore(node,_24e.item(0));
}
};
dojo.dom.textContent=function(node,text){
if(arguments.length>1){
var _254=dojo.doc();
dojo.dom.replaceChildren(node,_254.createTextNode(text));
return text;
}else{
if(node.textContent!=undefined){
return node.textContent;
}
var _255="";
if(node==null){
return _255;
}
for(var i=0;i<node.childNodes.length;i++){
switch(node.childNodes[i].nodeType){
case 1:
case 5:
_255+=dojo.dom.textContent(node.childNodes[i]);
break;
case 3:
case 2:
case 4:
_255+=node.childNodes[i].nodeValue;
break;
default:
break;
}
}
return _255;
}
};
dojo.dom.hasParent=function(node){
return node&&node.parentNode&&dojo.dom.isNode(node.parentNode);
};
dojo.dom.isTag=function(node){
if(node&&node.tagName){
for(var i=1;i<arguments.length;i++){
if(node.tagName==String(arguments[i])){
return String(arguments[i]);
}
}
}
return "";
};
dojo.dom.setAttributeNS=function(elem,_25b,_25c,_25d){
if(elem==null||((elem==undefined)&&(typeof elem=="undefined"))){
dojo.raise("No element given to dojo.dom.setAttributeNS");
}
if(!((elem.setAttributeNS==undefined)&&(typeof elem.setAttributeNS=="undefined"))){
elem.setAttributeNS(_25b,_25c,_25d);
}else{
var _25e=elem.ownerDocument;
var _25f=_25e.createNode(2,_25c,_25b);
_25f.nodeValue=_25d;
elem.setAttributeNode(_25f);
}
};

