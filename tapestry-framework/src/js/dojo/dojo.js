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
dojo.version={major:0,minor:0,patch:0,flag:"dev",revision:Number("$Rev: 5297 $".match(/[0-9]+/)[0]),toString:function(){
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
dojo.evalObjPath=function(_d,_e){
if(typeof _d!="string"){
return dojo.global();
}
if(_d.indexOf(".")==-1){
return dojo.evalProp(_d,dojo.global(),_e);
}
var _f=dojo.parseObjPath(_d,dojo.global(),_e);
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
dojo.render=(function(){
function vscaffold(_1c,_1d){
var tmp={capable:false,support:{builtin:false,plugin:false},prefixes:_1c};
for(var i=0;i<_1d.length;i++){
tmp[_1d[i]]=false;
}
return tmp;
}
return {name:"",ver:dojo.version,os:{win:false,linux:false,osx:false},html:vscaffold(["html"],["ie","opera","khtml","safari","moz"]),svg:vscaffold(["svg"],["corel","adobe","batik"]),vml:vscaffold(["vml"],["ie"]),swf:vscaffold(["Swf","Flash","Mm"],["mm"]),swt:vscaffold(["Swt"],["ibm"])};
})();
dojo.hostenv=(function(){
var _20={isDebug:false,allowQueryConfig:false,baseScriptUri:"",baseRelativePath:"",libraryScriptUri:"",iePreventClobber:false,ieClobberMinimal:true,preventBackButtonFix:true,searchIds:[],parseWidgets:true};
if(typeof djConfig=="undefined"){
djConfig=_20;
}else{
for(var _21 in _20){
if(typeof djConfig[_21]=="undefined"){
djConfig[_21]=_20[_21];
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
var _24=uri.lastIndexOf("/");
djConfig.baseScriptUri=djConfig.baseRelativePath;
return djConfig.baseScriptUri;
};
(function(){
var _25={pkgFileName:"__package__",loading_modules_:{},loaded_modules_:{},addedToLoadingCount:[],removedFromLoadingCount:[],inFlightCount:0,modulePrefixes_:{dojo:{name:"dojo",value:"src"}},setModulePrefix:function(_26,_27){
this.modulePrefixes_[_26]={name:_26,value:_27};
},moduleHasPrefix:function(_28){
var mp=this.modulePrefixes_;
return Boolean(mp[_28]&&mp[_28].value);
},getModulePrefix:function(_2a){
if(this.moduleHasPrefix(_2a)){
return this.modulePrefixes_[_2a].value;
}
return _2a;
},getTextStack:[],loadUriStack:[],loadedUris:[],post_load_:false,modulesLoadedListeners:[],unloadListeners:[],loadNotifying:false};
for(var _2b in _25){
dojo.hostenv[_2b]=_25[_2b];
}
})();
dojo.hostenv.loadPath=function(_2c,_2d,cb){
var uri;
if(_2c.charAt(0)=="/"||_2c.match(/^\w+:/)){
uri=_2c;
}else{
uri=this.getBaseScriptUri()+_2c;
}
if(djConfig.cacheBust&&dojo.render.html.capable){
uri+="?"+String(djConfig.cacheBust).replace(/\W+/g,"");
}
try{
return !_2d?this.loadUri(uri,cb):this.loadUriAndCheck(uri,_2d,cb);
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
var _32=this.getText(uri,null,true);
if(!_32){
return false;
}
this.loadedUris[uri]=true;
if(cb){
_32="("+_32+")";
}
var _33=dj_eval(_32);
if(cb){
cb(_33);
}
return true;
};
dojo.hostenv.loadUriAndCheck=function(uri,_35,cb){
var ok=true;
try{
ok=this.loadUri(uri,cb);
}
catch(e){
dojo.debug("failed loading ",uri," with error: ",e);
}
return Boolean(ok&&this.findModule(_35,false));
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
dojo.addOnUnload=function(obj,_3f){
var dh=dojo.hostenv;
if(arguments.length==1){
dh.unloadListeners.push(obj);
}else{
if(arguments.length>1){
dh.unloadListeners.push(function(){
obj[_3f]();
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
dojo.hostenv.getModuleSymbols=function(_41){
var _42=_41.split(".");
for(var i=_42.length;i>0;i--){
var _44=_42.slice(0,i).join(".");
if((i==1)&&!this.moduleHasPrefix(_44)){
_42[0]="../"+_42[0];
}else{
var _45=this.getModulePrefix(_44);
if(_45!=_44){
_42.splice(0,i,_45);
break;
}
}
}
return _42;
};
dojo.hostenv._global_omit_module_check=false;
dojo.hostenv.loadModule=function(_46,_47,_48){
if(!_46){
return;
}
_48=this._global_omit_module_check||_48;
var _49=this.findModule(_46,false);
if(_49){
return _49;
}
if(dj_undef(_46,this.loading_modules_)){
this.addedToLoadingCount.push(_46);
}
this.loading_modules_[_46]=1;
var _4a=_46.replace(/\./g,"/")+".js";
var _4b=_46.split(".");
var _4c=this.getModuleSymbols(_46);
var _4d=((_4c[0].charAt(0)!="/")&&!_4c[0].match(/^\w+:/));
var _4e=_4c[_4c.length-1];
var ok;
if(_4e=="*"){
_46=_4b.slice(0,-1).join(".");
while(_4c.length){
_4c.pop();
_4c.push(this.pkgFileName);
_4a=_4c.join("/")+".js";
if(_4d&&_4a.charAt(0)=="/"){
_4a=_4a.slice(1);
}
ok=this.loadPath(_4a,!_48?_46:null);
if(ok){
break;
}
_4c.pop();
}
}else{
_4a=_4c.join("/")+".js";
_46=_4b.join(".");
var _50=!_48?_46:null;
ok=this.loadPath(_4a,_50);
if(!ok&&!_47){
_4c.pop();
while(_4c.length){
_4a=_4c.join("/")+".js";
ok=this.loadPath(_4a,_50);
if(ok){
break;
}
_4c.pop();
_4a=_4c.join("/")+"/"+this.pkgFileName+".js";
if(_4d&&_4a.charAt(0)=="/"){
_4a=_4a.slice(1);
}
ok=this.loadPath(_4a,_50);
if(ok){
break;
}
}
}
if(!ok&&!_48){
dojo.raise("Could not load '"+_46+"'; last tried '"+_4a+"'");
}
}
if(!_48&&!this["isXDomain"]){
_49=this.findModule(_46,false);
if(!_49){
dojo.raise("symbol '"+_46+"' is not defined after loading '"+_4a+"'");
}
}
return _49;
};
dojo.hostenv.startPackage=function(_51){
var _52=String(_51);
var _53=_52;
var _54=_51.split(/\./);
if(_54[_54.length-1]=="*"){
_54.pop();
_53=_54.join(".");
}
var _55=dojo.evalObjPath(_53,true);
this.loaded_modules_[_52]=_55;
this.loaded_modules_[_53]=_55;
return _55;
};
dojo.hostenv.findModule=function(_56,_57){
var lmn=String(_56);
if(this.loaded_modules_[lmn]){
return this.loaded_modules_[lmn];
}
if(_57){
dojo.raise("no loaded module named '"+_56+"'");
}
return null;
};
dojo.kwCompoundRequire=function(_59){
var _5a=_59["common"]||[];
var _5b=_59[dojo.hostenv.name_]?_5a.concat(_59[dojo.hostenv.name_]||[]):_5a.concat(_59["default"]||[]);
for(var x=0;x<_5b.length;x++){
var _5d=_5b[x];
if(_5d.constructor==Array){
dojo.hostenv.loadModule.apply(dojo.hostenv,_5d);
}else{
dojo.hostenv.loadModule(_5d);
}
}
};
dojo.require=function(){
dojo.hostenv.loadModule.apply(dojo.hostenv,arguments);
};
dojo.requireIf=function(){
var _5e=arguments[0];
if((_5e===true)||(_5e=="common")||(_5e&&dojo.render[_5e].capable)){
var _5f=[];
for(var i=1;i<arguments.length;i++){
_5f.push(arguments[i]);
}
dojo.require.apply(dojo,_5f);
}
};
dojo.requireAfterIf=dojo.requireIf;
dojo.provide=function(){
return dojo.hostenv.startPackage.apply(dojo.hostenv,arguments);
};
dojo.registerModulePath=function(_61,_62){
return dojo.hostenv.setModulePrefix(_61,_62);
};
dojo.setModulePrefix=function(_63,_64){
dojo.deprecated("dojo.setModulePrefix(\""+_63+"\", \""+_64+"\")","replaced by dojo.registerModulePath","0.5");
return dojo.registerModulePath(_63,_64);
};
dojo.exists=function(obj,_66){
var p=_66.split(".");
for(var i=0;i<p.length;i++){
if(!obj[p[i]]){
return false;
}
obj=obj[p[i]];
}
return true;
};
dojo.hostenv.normalizeLocale=function(_69){
return _69?_69.toLowerCase():dojo.locale;
};
dojo.hostenv.searchLocalePath=function(_6a,_6b,_6c){
_6a=dojo.hostenv.normalizeLocale(_6a);
var _6d=_6a.split("-");
var _6e=[];
for(var i=_6d.length;i>0;i--){
_6e.push(_6d.slice(0,i).join("-"));
}
_6e.push(false);
if(_6b){
_6e.reverse();
}
for(var j=_6e.length-1;j>=0;j--){
var loc=_6e[j]||"ROOT";
var _72=_6c(loc);
if(_72){
break;
}
}
};
dojo.hostenv.preloadLocalizations=function(){
var _73;
if(_73){
dojo.registerModulePath("nls","nls");
function preload(_74){
_74=dojo.hostenv.normalizeLocale(_74);
dojo.hostenv.searchLocalePath(_74,true,function(loc){
for(var i=0;i<_73.length;i++){
if(_73[i]==loc){
dojo.require("nls.dojo_"+loc);
return true;
}
}
return false;
});
}
preload();
var _77=djConfig.extraLocale||[];
for(var i=0;i<_77.length;i++){
preload(_77[i]);
}
}
dojo.hostenv.preloadLocalizations=function(){
};
};
dojo.requireLocalization=function(_79,_7a,_7b){
dojo.hostenv.preloadLocalizations();
var _7c=[_79,"nls",_7a].join(".");
var _7d=dojo.hostenv.findModule(_7c);
if(_7d){
if(djConfig.localizationComplete&&_7d._built){
return;
}
var _7e=dojo.hostenv.normalizeLocale(_7b).replace("-","_");
var _7f=_7c+"."+_7e;
if(dojo.hostenv.findModule(_7f)){
return;
}
}
_7d=dojo.hostenv.startPackage(_7c);
var _80=dojo.hostenv.getModuleSymbols(_79);
var _81=_80.concat("nls").join("/");
var _82;
dojo.hostenv.searchLocalePath(_7b,false,function(loc){
var _84=loc.replace("-","_");
var _85=_7c+"."+_84;
var _86=false;
if(!dojo.hostenv.findModule(_85)){
dojo.hostenv.startPackage(_85);
var _87=[_81];
if(loc!="ROOT"){
_87.push(loc);
}
_87.push(_7a);
var _88=_87.join("/")+".js";
_86=dojo.hostenv.loadPath(_88,null,function(_89){
var _8a=function(){
};
_8a.prototype=_82;
_7d[_84]=new _8a();
for(var j in _89){
_7d[_84][j]=_89[j];
}
});
}else{
_86=true;
}
if(_86&&_7d[_84]){
_82=_7d[_84];
}else{
_7d[_84]=_82;
}
});
};
(function(){
var _8c=djConfig.extraLocale;
if(_8c){
if(!_8c instanceof Array){
_8c=[_8c];
}
var req=dojo.requireLocalization;
dojo.requireLocalization=function(m,b,_90){
req(m,b,_90);
if(_90){
return;
}
for(var i=0;i<_8c.length;i++){
req(m,b,_8c[i]);
}
};
}
})();
}
if(typeof window!="undefined"){
(function(){
if(djConfig.allowQueryConfig){
var _92=document.location.toString();
var _93=_92.split("?",2);
if(_93.length>1){
var _94=_93[1];
var _95=_94.split("&");
for(var x in _95){
var sp=_95[x].split("=");
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
var _99=document.getElementsByTagName("script");
var _9a=/(__package__|dojo|bootstrap1)\.js([\?\.]|$)/i;
for(var i=0;i<_99.length;i++){
var src=_99[i].getAttribute("src");
if(!src){
continue;
}
var m=src.match(_9a);
if(m){
var _9e=src.substring(0,m.index);
if(src.indexOf("bootstrap1")>-1){
_9e+="../";
}
if(!this["djConfig"]){
djConfig={};
}
if(djConfig["baseScriptUri"]==""){
djConfig["baseScriptUri"]=_9e;
}
if(djConfig["baseRelativePath"]==""){
djConfig["baseRelativePath"]=_9e;
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
var _a6=dua.indexOf("Gecko");
drh.mozilla=drh.moz=(_a6>=0)&&(!drh.khtml);
if(drh.mozilla){
drh.geckoVersion=dua.substring(_a6+6,_a6+14);
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
var _a8=window["document"];
var tdi=_a8["implementation"];
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
var _ac=null;
var _ad=null;
try{
_ac=new XMLHttpRequest();
}
catch(e){
}
if(!_ac){
for(var i=0;i<3;++i){
var _af=dojo.hostenv._XMLHTTP_PROGIDS[i];
try{
_ac=new ActiveXObject(_af);
}
catch(e){
_ad=e;
}
if(_ac){
dojo.hostenv._XMLHTTP_PROGIDS=[_af];
break;
}
}
}
if(!_ac){
return dojo.raise("XMLHTTP not available",_ad);
}
return _ac;
};
dojo.hostenv._blockAsync=false;
dojo.hostenv.getText=function(uri,_b1,_b2){
if(!_b1){
this._blockAsync=true;
}
var _b3=this.getXmlhttpObject();
function isDocumentOk(_b4){
var _b5=_b4["status"];
return Boolean((!_b5)||((200<=_b5)&&(300>_b5))||(_b5==304));
}
if(_b1){
var _b6=this,timer=null,gbl=dojo.global();
var xhr=dojo.evalObjPath("dojo.io.XMLHTTPTransport");
_b3.onreadystatechange=function(){
if(timer){
gbl.clearTimeout(timer);
timer=null;
}
if(_b6._blockAsync||(xhr&&xhr._blockAsync)){
timer=gbl.setTimeout(function(){
_b3.onreadystatechange.apply(this);
},10);
}else{
if(4==_b3.readyState){
if(isDocumentOk(_b3)){
_b1(_b3.responseText);
}
}
}
};
}
_b3.open("GET",uri,_b1?true:false);
try{
_b3.send(null);
if(_b1){
return null;
}
if(!isDocumentOk(_b3)){
var err=Error("Unable to load "+uri+" status:"+_b3.status);
err.status=_b3.status;
err.responseText=_b3.responseText;
throw err;
}
}
catch(e){
this._blockAsync=false;
if((_b2)&&(!_b1)){
return null;
}else{
throw e;
}
}
this._blockAsync=false;
return _b3.responseText;
};
dojo.hostenv.defaultDebugContainerId="dojoDebug";
dojo.hostenv._println_buffer=[];
dojo.hostenv._println_safe=false;
dojo.hostenv.println=function(_b9){
if(!dojo.hostenv._println_safe){
dojo.hostenv._println_buffer.push(_b9);
}else{
try{
var _ba=document.getElementById(djConfig.debugContainerId?djConfig.debugContainerId:dojo.hostenv.defaultDebugContainerId);
if(!_ba){
_ba=dojo.body();
}
var div=document.createElement("div");
div.appendChild(document.createTextNode(_b9));
_ba.appendChild(div);
}
catch(e){
try{
document.write("<div>"+_b9+"</div>");
}
catch(e2){
window.status=_b9;
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
function dj_addNodeEvtHdlr(_bc,_bd,fp,_bf){
var _c0=_bc["on"+_bd]||function(){
};
_bc["on"+_bd]=function(){
fp.apply(_bc,arguments);
_c0.apply(_bc,arguments);
};
return true;
}
function dj_load_init(e){
var _c2=(e&&e.type)?e.type.toLowerCase():"load";
if(arguments.callee.initialized||(_c2!="domcontentloaded"&&_c2!="load")){
return;
}
arguments.callee.initialized=true;
if(typeof (_timer)!="undefined"){
clearInterval(_timer);
delete _timer;
}
var _c3=function(){
if(dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
};
if(dojo.hostenv.inFlightCount==0){
_c3();
dojo.hostenv.modulesLoaded();
}else{
dojo.addOnLoad(_c3);
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
var _c5=[];
if(djConfig.searchIds&&djConfig.searchIds.length>0){
_c5=_c5.concat(djConfig.searchIds);
}
if(dojo.hostenv.searchIds&&dojo.hostenv.searchIds.length>0){
_c5=_c5.concat(dojo.hostenv.searchIds);
}
if((djConfig.parseWidgets)||(_c5.length>0)){
if(dojo.evalObjPath("dojo.widget.Parse")){
var _c6=new dojo.xml.Parse();
if(_c5.length>0){
for(var x=0;x<_c5.length;x++){
var _c8=document.getElementById(_c5[x]);
if(!_c8){
continue;
}
var _c9=_c6.parseElement(_c8,null,true);
dojo.widget.getParser().createComponents(_c9);
}
}else{
if(djConfig.parseWidgets){
var _c9=_c6.parseElement(dojo.body(),null,true);
dojo.widget.getParser().createComponents(_c9);
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
dojo.setContext=function(_ce,_cf){
dj_currentContext=_ce;
dj_currentDocument=_cf;
};
dojo._fireCallback=function(_d0,_d1,_d2){
if((_d1)&&((typeof _d0=="string")||(_d0 instanceof String))){
_d0=_d1[_d0];
}
return (_d1?_d0.apply(_d1,_d2||[]):_d0());
};
dojo.withGlobal=function(_d3,_d4,_d5,_d6){
var _d7;
var _d8=dj_currentContext;
var _d9=dj_currentDocument;
try{
dojo.setContext(_d3,_d3.document);
_d7=dojo._fireCallback(_d4,_d5,_d6);
}
finally{
dojo.setContext(_d8,_d9);
}
return _d7;
};
dojo.withDoc=function(_da,_db,_dc,_dd){
var _de;
var _df=dj_currentDocument;
try{
dj_currentDocument=_da;
_de=dojo._fireCallback(_db,_dc,_dd);
}
finally{
dj_currentDocument=_df;
}
return _de;
};
}
(function(){
if(typeof dj_usingBootstrap!="undefined"){
return;
}
var _e0=false;
var _e1=false;
var _e2=false;
if((typeof this["load"]=="function")&&((typeof this["Packages"]=="function")||(typeof this["Packages"]=="object"))){
_e0=true;
}else{
if(typeof this["load"]=="function"){
_e1=true;
}else{
if(window.widget){
_e2=true;
}
}
}
var _e3=[];
if((this["djConfig"])&&((djConfig["isDebug"])||(djConfig["debugAtAllCosts"]))){
_e3.push("debug.js");
}
if((this["djConfig"])&&(djConfig["debugAtAllCosts"])&&(!_e0)&&(!_e2)){
_e3.push("browser_debug.js");
}
if((this["djConfig"])&&(djConfig["compat"])){
_e3.push("compat/"+djConfig["compat"]+".js");
}
var _e4=djConfig["baseScriptUri"];
if((this["djConfig"])&&(djConfig["baseLoaderUri"])){
_e4=djConfig["baseLoaderUri"];
}
for(var x=0;x<_e3.length;x++){
var _e6=_e4+"src/"+_e3[x];
if(_e0||_e1){
load(_e6);
}else{
try{
document.write("<scr"+"ipt type='text/javascript' src='"+_e6+"'></scr"+"ipt>");
}
catch(e){
var _e7=document.createElement("script");
_e7.src=_e6;
document.getElementsByTagName("head")[0].appendChild(_e7);
}
}
}
})();
dojo.provide("dojo.lang.common");
dojo.lang.inherits=function(_e8,_e9){
if(typeof _e9!="function"){
dojo.raise("dojo.inherits: superclass argument ["+_e9+"] must be a function (subclass: ["+_e8+"']");
}
_e8.prototype=new _e9();
_e8.prototype.constructor=_e8;
_e8.superclass=_e9.prototype;
_e8["super"]=_e9.prototype;
};
dojo.lang._mixin=function(obj,_eb){
var _ec={};
for(var x in _eb){
if((typeof _ec[x]=="undefined")||(_ec[x]!=_eb[x])){
obj[x]=_eb[x];
}
}
if(dojo.render.html.ie&&(typeof (_eb["toString"])=="function")&&(_eb["toString"]!=obj["toString"])&&(_eb["toString"]!=_ec["toString"])){
obj.toString=_eb.toString;
}
return obj;
};
dojo.lang.mixin=function(obj,_ef){
for(var i=1,l=arguments.length;i<l;i++){
dojo.lang._mixin(obj,arguments[i]);
}
return obj;
};
dojo.lang.extend=function(_f1,_f2){
for(var i=1,l=arguments.length;i<l;i++){
dojo.lang._mixin(_f1.prototype,arguments[i]);
}
return _f1;
};
dojo.inherits=dojo.lang.inherits;
dojo.mixin=dojo.lang.mixin;
dojo.extend=dojo.lang.extend;
dojo.lang.find=function(_f4,_f5,_f6,_f7){
if(!dojo.lang.isArrayLike(_f4)&&dojo.lang.isArrayLike(_f5)){
dojo.deprecated("dojo.lang.find(value, array)","use dojo.lang.find(array, value) instead","0.5");
var _f8=_f4;
_f4=_f5;
_f5=_f8;
}
var _f9=dojo.lang.isString(_f4);
if(_f9){
_f4=_f4.split("");
}
if(_f7){
var _fa=-1;
var i=_f4.length-1;
var end=-1;
}else{
var _fa=1;
var i=0;
var end=_f4.length;
}
if(_f6){
while(i!=end){
if(_f4[i]===_f5){
return i;
}
i+=_fa;
}
}else{
while(i!=end){
if(_f4[i]==_f5){
return i;
}
i+=_fa;
}
}
return -1;
};
dojo.lang.indexOf=dojo.lang.find;
dojo.lang.findLast=function(_fd,_fe,_ff){
return dojo.lang.find(_fd,_fe,_ff,true);
};
dojo.lang.lastIndexOf=dojo.lang.findLast;
dojo.lang.inArray=function(_100,_101){
return dojo.lang.find(_100,_101)>-1;
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
return (it instanceof Function||typeof it=="function");
};
dojo.lang.isString=function(it){
return (it instanceof String||typeof it=="string");
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
var _10f=0;
for(var x in obj){
if(obj[x]&&(!tmp[x])){
_10f++;
break;
}
}
return (_10f==0);
}else{
if(dojo.lang.isArrayLike(obj)||dojo.lang.isString(obj)){
return obj.length==0;
}
}
};
dojo.lang.map=function(arr,obj,_113){
var _114=dojo.lang.isString(arr);
if(_114){
arr=arr.split("");
}
if(dojo.lang.isFunction(obj)&&(!_113)){
_113=obj;
obj=dj_global;
}else{
if(dojo.lang.isFunction(obj)&&_113){
var _115=obj;
obj=_113;
_113=_115;
}
}
if(Array.map){
var _116=Array.map(arr,_113,obj);
}else{
var _116=[];
for(var i=0;i<arr.length;++i){
_116.push(_113.call(obj,arr[i]));
}
}
if(_114){
return _116.join("");
}else{
return _116;
}
};
dojo.lang.reduce=function(arr,_119,obj,_11b){
var _11c=_119;
var ob=obj?obj:dj_global;
dojo.lang.map(arr,function(val){
_11c=_11b.call(ob,_11c,val);
});
return _11c;
};
dojo.lang.forEach=function(_11f,_120,_121){
if(dojo.lang.isString(_11f)){
_11f=_11f.split("");
}
if(Array.forEach){
Array.forEach(_11f,_120,_121);
}else{
if(!_121){
_121=dj_global;
}
for(var i=0,l=_11f.length;i<l;i++){
_120.call(_121,_11f[i],i,_11f);
}
}
};
dojo.lang._everyOrSome=function(_123,arr,_125,_126){
if(dojo.lang.isString(arr)){
arr=arr.split("");
}
if(Array.every){
return Array[(_123)?"every":"some"](arr,_125,_126);
}else{
if(!_126){
_126=dj_global;
}
for(var i=0,l=arr.length;i<l;i++){
var _128=_125.call(_126,arr[i],i,arr);
if((_123)&&(!_128)){
return false;
}else{
if((!_123)&&(_128)){
return true;
}
}
}
return (_123)?true:false;
}
};
dojo.lang.every=function(arr,_12a,_12b){
return this._everyOrSome(true,arr,_12a,_12b);
};
dojo.lang.some=function(arr,_12d,_12e){
return this._everyOrSome(false,arr,_12d,_12e);
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
dojo.provide("dojo.lang.extras");
dojo.lang.setTimeout=function(func,_13d){
var _13e=window,argsStart=2;
if(!dojo.lang.isFunction(func)){
_13e=func;
func=_13d;
_13d=arguments[2];
argsStart++;
}
if(dojo.lang.isString(func)){
func=_13e[func];
}
var args=[];
for(var i=argsStart;i<arguments.length;i++){
args.push(arguments[i]);
}
return dojo.global().setTimeout(function(){
func.apply(_13e,args);
},_13d);
};
dojo.lang.clearTimeout=function(_141){
dojo.global().clearTimeout(_141);
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
dojo.lang.getObjPathValue=function(_149,_14a,_14b){
with(dojo.parseObjPath(_149,_14a,_14b)){
return dojo.evalProp(prop,obj,_14b);
}
};
dojo.lang.setObjPathValue=function(_14c,_14d,_14e,_14f){
if(arguments.length<4){
_14f=true;
}
with(dojo.parseObjPath(_14c,_14e,_14f)){
if(obj&&(_14f||(prop in obj))){
obj[prop]=_14d;
}
}
};
dojo.provide("dojo.lang.func");
dojo.lang.hitch=function(_150,_151){
var fcn=(dojo.lang.isString(_151)?_150[_151]:_151)||function(){
};
return function(){
return fcn.apply(_150,arguments);
};
};
dojo.lang.anonCtr=0;
dojo.lang.anon={};
dojo.lang.nameAnonFunc=function(_153,_154,_155){
var nso=(_154||dojo.lang.anon);
if((_155)||((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true))){
for(var x in nso){
try{
if(nso[x]===_153){
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
nso[ret]=_153;
return ret;
};
dojo.lang.forward=function(_159){
return function(){
return this[_159].apply(this,arguments);
};
};
dojo.lang.curry=function(ns,func){
var _15c=[];
ns=ns||dj_global;
if(dojo.lang.isString(func)){
func=ns[func];
}
for(var x=2;x<arguments.length;x++){
_15c.push(arguments[x]);
}
var _15e=(func["__preJoinArity"]||func.length)-_15c.length;
function gather(_15f,_160,_161){
var _162=_161;
var _163=_160.slice(0);
for(var x=0;x<_15f.length;x++){
_163.push(_15f[x]);
}
_161=_161-_15f.length;
if(_161<=0){
var res=func.apply(ns,_163);
_161=_162;
return res;
}else{
return function(){
return gather(arguments,_163,_161);
};
}
}
return gather([],_15c,_15e);
};
dojo.lang.curryArguments=function(ns,func,args,_169){
var _16a=[];
var x=_169||0;
for(x=_169;x<args.length;x++){
_16a.push(args[x]);
}
return dojo.lang.curry.apply(dojo.lang,[ns,func].concat(_16a));
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
dojo.lang.delayThese=function(farr,cb,_170,_171){
if(!farr.length){
if(typeof _171=="function"){
_171();
}
return;
}
if((typeof _170=="undefined")&&(typeof cb=="number")){
_170=cb;
cb=function(){
};
}else{
if(!cb){
cb=function(){
};
if(!_170){
_170=0;
}
}
}
setTimeout(function(){
(farr.shift())();
cb();
dojo.lang.delayThese(farr,cb,_170,_171);
},_170);
};
dojo.provide("dojo.event.common");
dojo.event=new function(){
this.canTimeout=dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);
function interpolateArgs(args,_173){
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
var _176=dl.nameAnonFunc(args[2],ao.adviceObj,_173);
ao.adviceFunc=_176;
}else{
if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){
ao.adviceType="after";
ao.srcObj=dj_global;
var _176=dl.nameAnonFunc(args[0],ao.srcObj,_173);
ao.srcFunc=_176;
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
var _176=dl.nameAnonFunc(args[1],dj_global,_173);
ao.srcFunc=_176;
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){
ao.srcObj=args[1];
ao.srcFunc=args[2];
var _176=dl.nameAnonFunc(args[3],dj_global,_173);
ao.adviceObj=dj_global;
ao.adviceFunc=_176;
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
var _176=dl.nameAnonFunc(ao.aroundFunc,ao.aroundObj,_173);
ao.aroundFunc=_176;
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
var _178={};
for(var x in ao){
_178[x]=ao[x];
}
var mjps=[];
dojo.lang.forEach(ao.srcObj,function(src){
if((dojo.render.html.capable)&&(dojo.lang.isString(src))){
src=dojo.byId(src);
}
_178.srcObj=src;
mjps.push(dojo.event.connect.call(dojo.event,_178));
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
var _180;
if((arguments.length==1)&&(typeof a1=="object")){
_180=a1;
}else{
_180={srcObj:a1,srcFunc:a2};
}
_180.adviceFunc=function(){
var _181=[];
for(var x=0;x<arguments.length;x++){
_181.push(arguments[x]);
}
dojo.debug("("+_180.srcObj+")."+_180.srcFunc,":",_181.join(", "));
};
this.kwConnect(_180);
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
this._kwConnectImpl=function(_188,_189){
var fn=(_189)?"disconnect":"connect";
if(typeof _188["srcFunc"]=="function"){
_188.srcObj=_188["srcObj"]||dj_global;
var _18b=dojo.lang.nameAnonFunc(_188.srcFunc,_188.srcObj,true);
_188.srcFunc=_18b;
}
if(typeof _188["adviceFunc"]=="function"){
_188.adviceObj=_188["adviceObj"]||dj_global;
var _18b=dojo.lang.nameAnonFunc(_188.adviceFunc,_188.adviceObj,true);
_188.adviceFunc=_18b;
}
return dojo.event[fn]((_188["type"]||_188["adviceType"]||"after"),_188["srcObj"]||dj_global,_188["srcFunc"],_188["adviceObj"]||_188["targetObj"]||dj_global,_188["adviceFunc"]||_188["targetFunc"],_188["aroundObj"],_188["aroundFunc"],_188["once"],_188["delay"],_188["rate"],_188["adviceMsg"]||false);
};
this.kwConnect=function(_18c){
return this._kwConnectImpl(_18c,false);
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
this.kwDisconnect=function(_18f){
return this._kwConnectImpl(_18f,true);
};
};
dojo.event.MethodInvocation=function(_190,obj,args){
this.jp_=_190;
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
dojo.event.MethodJoinPoint=function(obj,_198){
this.object=obj||dj_global;
this.methodname=_198;
this.methodfunc=this.object[_198];
this.before=[];
this.after=[];
this.around=[];
};
dojo.event.MethodJoinPoint.getForMethod=function(obj,_19a){
if(!obj){
obj=dj_global;
}
if(!obj[_19a]){
obj[_19a]=function(){
};
if(!obj[_19a]){
dojo.raise("Cannot set do-nothing method on that object "+_19a);
}
}else{
if((!dojo.lang.isFunction(obj[_19a]))&&(!dojo.lang.isAlien(obj[_19a]))){
return null;
}
}
var _19b=_19a+"$joinpoint";
var _19c=_19a+"$joinpoint$method";
var _19d=obj[_19b];
if(!_19d){
var _19e=false;
if(dojo.event["browser"]){
if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){
_19e=true;
dojo.event.browser.addClobberNodeAttrs(obj,[_19b,_19c,_19a]);
}
}
var _19f=obj[_19a].length;
obj[_19c]=obj[_19a];
_19d=obj[_19b]=new dojo.event.MethodJoinPoint(obj,_19c);
obj[_19a]=function(){
var args=[];
if((_19e)&&(!arguments.length)){
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
if((x==0)&&(_19e)&&(dojo.event.browser.isEvent(arguments[x]))){
args.push(dojo.event.browser.fixEvent(arguments[x],this));
}else{
args.push(arguments[x]);
}
}
}
return _19d.run.apply(_19d,args);
};
obj[_19a].__preJoinArity=_19f;
}
return _19d;
};
dojo.lang.extend(dojo.event.MethodJoinPoint,{unintercept:function(){
this.object[this.methodname]=this.methodfunc;
this.before=[];
this.after=[];
this.around=[];
},disconnect:dojo.lang.forward("unintercept"),run:function(){
var obj=this.object||dj_global;
var args=arguments;
var _1a5=[];
for(var x=0;x<args.length;x++){
_1a5[x]=args[x];
}
var _1a7=function(marr){
if(!marr){
dojo.debug("Null argument to unrollAdvice()");
return;
}
var _1a9=marr[0]||dj_global;
var _1aa=marr[1];
if(!_1a9[_1aa]){
dojo.raise("function \""+_1aa+"\" does not exist on \""+_1a9+"\"");
}
var _1ab=marr[2]||dj_global;
var _1ac=marr[3];
var msg=marr[6];
var _1ae;
var to={args:[],jp_:this,object:obj,proceed:function(){
return _1a9[_1aa].apply(_1a9,to.args);
}};
to.args=_1a5;
var _1b0=parseInt(marr[4]);
var _1b1=((!isNaN(_1b0))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));
if(marr[5]){
var rate=parseInt(marr[5]);
var cur=new Date();
var _1b4=false;
if((marr["last"])&&((cur-marr.last)<=rate)){
if(dojo.event.canTimeout){
if(marr["delayTimer"]){
clearTimeout(marr.delayTimer);
}
var tod=parseInt(rate*2);
var mcpy=dojo.lang.shallowCopy(marr);
marr.delayTimer=setTimeout(function(){
mcpy[5]=0;
_1a7(mcpy);
},tod);
}
return;
}else{
marr.last=cur;
}
}
if(_1ac){
_1ab[_1ac].call(_1ab,to);
}else{
if((_1b1)&&((dojo.render.html)||(dojo.render.svg))){
dj_global["setTimeout"](function(){
if(msg){
_1a9[_1aa].call(_1a9,to);
}else{
_1a9[_1aa].apply(_1a9,args);
}
},_1b0);
}else{
if(msg){
_1a9[_1aa].call(_1a9,to);
}else{
_1a9[_1aa].apply(_1a9,args);
}
}
}
};
if(this.before.length>0){
dojo.lang.forEach(this.before.concat(new Array()),_1a7);
}
var _1b7;
if(this.around.length>0){
var mi=new dojo.event.MethodInvocation(this,obj,args);
_1b7=mi.proceed();
}else{
if(this.methodfunc){
_1b7=this.object[this.methodname].apply(this.object,args);
}
}
if(this.after.length>0){
dojo.lang.forEach(this.after.concat(new Array()),_1a7);
}
return (this.methodfunc)?_1b7:null;
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
},addAdvice:function(_1bc,_1bd,_1be,_1bf,_1c0,_1c1,once,_1c3,rate,_1c5){
var arr=this.getArr(_1c0);
if(!arr){
dojo.raise("bad this: "+this);
}
var ao=[_1bc,_1bd,_1be,_1bf,_1c3,rate,_1c5];
if(once){
if(this.hasAdvice(_1bc,_1bd,_1c0,arr)>=0){
return;
}
}
if(_1c1=="first"){
arr.unshift(ao);
}else{
arr.push(ao);
}
},hasAdvice:function(_1c8,_1c9,_1ca,arr){
if(!arr){
arr=this.getArr(_1ca);
}
var ind=-1;
for(var x=0;x<arr.length;x++){
var aao=(typeof _1c9=="object")?(new String(_1c9)).toString():_1c9;
var a1o=(typeof arr[x][1]=="object")?(new String(arr[x][1])).toString():arr[x][1];
if((arr[x][0]==_1c8)&&(a1o==aao)){
ind=x;
}
}
return ind;
},removeAdvice:function(_1d0,_1d1,_1d2,once){
var arr=this.getArr(_1d2);
var ind=this.hasAdvice(_1d0,_1d1,_1d2,arr);
if(ind==-1){
return false;
}
while(ind!=-1){
arr.splice(ind,1);
if(once){
break;
}
ind=this.hasAdvice(_1d0,_1d1,_1d2,arr);
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
this.clobber=function(_1d8){
var na;
var tna;
if(_1d8){
tna=_1d8.all||_1d8.getElementsByTagName("*");
na=[_1d8];
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
var _1dc={};
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
var _1e0=0;
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
this.addClobberNodeAttrs=function(node,_1e4){
if(!dojo.render.html.ie){
return;
}
this.addClobberNode(node);
for(var x=0;x<_1e4.length;x++){
node.__clobberAttrs__.push(_1e4[x]);
}
};
this.removeListener=function(node,_1e7,fp,_1e9){
if(!_1e9){
var _1e9=false;
}
_1e7=_1e7.toLowerCase();
if((_1e7=="onkey")||(_1e7=="key")){
if(dojo.render.html.ie){
this.removeListener(node,"onkeydown",fp,_1e9);
}
_1e7="onkeypress";
}
if(_1e7.substr(0,2)=="on"){
_1e7=_1e7.substr(2);
}
if(node.removeEventListener){
node.removeEventListener(_1e7,fp,_1e9);
}
};
this.addListener=function(node,_1eb,fp,_1ed,_1ee){
if(!node){
return;
}
if(!_1ed){
var _1ed=false;
}
_1eb=_1eb.toLowerCase();
if((_1eb=="onkey")||(_1eb=="key")){
if(dojo.render.html.ie){
this.addListener(node,"onkeydown",fp,_1ed,_1ee);
}
_1eb="onkeypress";
}
if(_1eb.substr(0,2)!="on"){
_1eb="on"+_1eb;
}
if(!_1ee){
var _1ef=function(evt){
if(!evt){
evt=window.event;
}
var ret=fp(dojo.event.browser.fixEvent(evt,this));
if(_1ed){
dojo.event.browser.stopEvent(evt);
}
return ret;
};
}else{
_1ef=fp;
}
if(node.addEventListener){
node.addEventListener(_1eb.substr(2),_1ef,_1ed);
return _1ef;
}else{
if(typeof node[_1eb]=="function"){
var _1f2=node[_1eb];
node[_1eb]=function(e){
_1f2(e);
return _1ef(e);
};
}else{
node[_1eb]=_1ef;
}
if(dojo.render.html.ie){
this.addClobberNodeAttrs(node,[_1eb]);
}
return _1ef;
}
};
this.isEvent=function(obj){
return (typeof obj!="undefined")&&(typeof Event!="undefined")&&(obj.eventPhase);
};
this.currentEvent=null;
this.callListener=function(_1f5,_1f6){
if(typeof _1f5!="function"){
dojo.raise("listener not a function: "+_1f5);
}
dojo.event.browser.currentEvent.currentTarget=_1f6;
return _1f5.call(_1f6,dojo.event.browser.currentEvent);
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
this.fixEvent=function(evt,_1f9){
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
var _1fb=evt.keyCode;
if(_1fb>=65&&_1fb<=90&&evt.shiftKey==false){
_1fb+=32;
}
if(_1fb>=1&&_1fb<=26&&evt.ctrlKey){
_1fb+=96;
}
evt.key=String.fromCharCode(_1fb);
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
var _1fb=evt.which;
if((evt.ctrlKey||evt.altKey||evt.metaKey)&&(evt.which>=65&&evt.which<=90&&evt.shiftKey==false)){
_1fb+=32;
}
evt.key=String.fromCharCode(_1fb);
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
evt.currentTarget=(_1f9?_1f9:evt.srcElement);
}
if(!evt.layerX){
evt.layerX=evt.offsetX;
}
if(!evt.layerY){
evt.layerY=evt.offsetY;
}
var doc=(evt.srcElement&&evt.srcElement.ownerDocument)?evt.srcElement.ownerDocument:document;
var _1fd=((dojo.render.html.ie55)||(doc["compatMode"]=="BackCompat"))?doc.body:doc.documentElement;
if(!evt.pageX){
evt.pageX=evt.clientX+(_1fd.scrollLeft||0);
}
if(!evt.pageY){
evt.pageY=evt.clientY+(_1fd.scrollTop||0);
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

