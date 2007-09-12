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
dojo.version={major:0,minor:0,patch:0,flag:"dev",revision:Number("$Rev$".match(/[0-9]+/)[0]),toString:function(){
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
}else{
_12=dojo.errorToString(_12);
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
var _21={isDebug:false,allowQueryConfig:false,baseScriptUri:"",baseRelativePath:"",libraryScriptUri:"",iePreventClobber:false,ieClobberMinimal:true,preventBackButtonFix:true,delayMozLoadingFix:false,searchIds:[],parseWidgets:true};
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
if(typeof setTimeout=="object"||(djConfig["useXDomain"]&&dojo.render.html.opera)){
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
dojo.require=function(_5f){
dojo.hostenv.loadModule.apply(dojo.hostenv,arguments);
};
dojo.requireIf=function(_60,_61){
var _62=arguments[0];
if((_62===true)||(_62=="common")||(_62&&dojo.render[_62].capable)){
var _63=[];
for(var i=1;i<arguments.length;i++){
_63.push(arguments[i]);
}
dojo.require.apply(dojo,_63);
}
};
dojo.requireAfterIf=dojo.requireIf;
dojo.provide=function(_65){
return dojo.hostenv.startPackage.apply(dojo.hostenv,arguments);
};
dojo.registerModulePath=function(_66,_67){
return dojo.hostenv.setModulePrefix(_66,_67);
};
if(djConfig["modulePaths"]){
for(var param in djConfig["modulePaths"]){
dojo.registerModulePath(param,djConfig["modulePaths"][param]);
}
}
dojo.setModulePrefix=function(_68,_69){
dojo.deprecated("dojo.setModulePrefix(\""+_68+"\", \""+_69+"\")","replaced by dojo.registerModulePath","0.5");
return dojo.registerModulePath(_68,_69);
};
dojo.exists=function(obj,_6b){
var p=_6b.split(".");
for(var i=0;i<p.length;i++){
if(!obj[p[i]]){
return false;
}
obj=obj[p[i]];
}
return true;
};
dojo.hostenv.normalizeLocale=function(_6e){
var _6f=_6e?_6e.toLowerCase():dojo.locale;
if(_6f=="root"){
_6f="ROOT";
}
return _6f;
};
dojo.hostenv.searchLocalePath=function(_70,_71,_72){
_70=dojo.hostenv.normalizeLocale(_70);
var _73=_70.split("-");
var _74=[];
for(var i=_73.length;i>0;i--){
_74.push(_73.slice(0,i).join("-"));
}
_74.push(false);
if(_71){
_74.reverse();
}
for(var j=_74.length-1;j>=0;j--){
var loc=_74[j]||"ROOT";
var _78=_72(loc);
if(_78){
break;
}
}
};
dojo.hostenv.localesGenerated=["ROOT","es-es","es","it-it","pt-br","de","fr-fr","zh-cn","pt","en-us","zh","fr","zh-tw","it","en-gb","xx","de-de","ko-kr","ja-jp","ko","en","ja"];
dojo.hostenv.registerNlsPrefix=function(){
dojo.registerModulePath("nls","nls");
};
dojo.hostenv.preloadLocalizations=function(){
if(dojo.hostenv.localesGenerated){
dojo.hostenv.registerNlsPrefix();
function preload(_79){
_79=dojo.hostenv.normalizeLocale(_79);
dojo.hostenv.searchLocalePath(_79,true,function(loc){
for(var i=0;i<dojo.hostenv.localesGenerated.length;i++){
if(dojo.hostenv.localesGenerated[i]==loc){
dojo["require"]("nls.dojo_"+loc);
return true;
}
}
return false;
});
}
preload();
var _7c=djConfig.extraLocale||[];
for(var i=0;i<_7c.length;i++){
preload(_7c[i]);
}
}
dojo.hostenv.preloadLocalizations=function(){
};
};
dojo.requireLocalization=function(_7e,_7f,_80,_81){
dojo.hostenv.preloadLocalizations();
var _82=dojo.hostenv.normalizeLocale(_80);
var _83=[_7e,"nls",_7f].join(".");
var _84="";
if(_81){
var _85=_81.split(",");
for(var i=0;i<_85.length;i++){
if(_82.indexOf(_85[i])==0 || _85[i].indexOf(_82)==0){
if(_85[i].length>_84.length){
_84=_85[i];
}
}
}
if(!_84){
_84="ROOT";
}
}
var _87=_81?_84:_82;
var _88=dojo.hostenv.findModule(_83);
var _89=null;
if(_88){
if(djConfig.localizationComplete&&_88._built){
return;
}
var _8a=_87.replace("-","_");
var _8b=_83+"."+_8a;
_89=dojo.hostenv.findModule(_8b);
}
if(!_89){
_88=dojo.hostenv.startPackage(_83);
var _8c=dojo.hostenv.getModuleSymbols(_7e);
var _8d=_8c.concat("nls").join("/");
var _8e;
dojo.hostenv.searchLocalePath(_87,_81,function(loc){
var _90=loc.replace("-","_");
var _91=_83+"."+_90;
var _92=false;
if(!dojo.hostenv.findModule(_91)){
dojo.hostenv.startPackage(_91);
var _93=[_8d];
if(loc!="ROOT"){
_93.push(loc);
}
_93.push(_7f);
var _94=_93.join("/")+".js";
_92=dojo.hostenv.loadPath(_94,null,function(_95){
var _96=function(){
};
_96.prototype=_8e;
_88[_90]=new _96();
for(var j in _95){
_88[_90][j]=_95[j];
}
});
}else{
_92=true;
}
if(_92&&_88[_90]){
_8e=_88[_90];
}else{
_88[_90]=_8e;
}
if(_81){
return true;
}
});
}
if(_81&&_82!=_84){
_88[_82.replace("-","_")]=_88[_84.replace("-","_")];
}
};
(function(){
var _98=djConfig.extraLocale;
if(_98){
if(!_98 instanceof Array){
_98=[_98];
}
var req=dojo.requireLocalization;
dojo.requireLocalization=function(m,b,_9c,_9d){
req(m,b,_9c,_9d);
if(_9c){
return;
}
for(var i=0;i<_98.length;i++){
req(m,b,_98[i],_9d);
}
};
}
})();
}
if(typeof window!="undefined"){
(function(){
if(djConfig.allowQueryConfig){
var _9f=document.location.toString();
var _a0=_9f.split("?",2);
if(_a0.length>1){
var _a1=_a0[1];
var _a2=_a1.split("&");
for(var x in _a2){
var sp=_a2[x].split("=");
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
var _a6=document.getElementsByTagName("script");
var _a7=/(__package__|dojo|bootstrap1)\.js([\?\.]|$)/i;
for(var i=0;i<_a6.length;i++){
var src=_a6[i].getAttribute("src");
if(!src){
continue;
}
var m=src.match(_a7);
if(m){
var _ab=src.substring(0,m.index);
if(src.indexOf("bootstrap1")>-1){
_ab+="../";
}
if(!this["djConfig"]){
djConfig={};
}
if(djConfig["baseScriptUri"]==""){
djConfig["baseScriptUri"]=_ab;
}
if(djConfig["baseRelativePath"]==""){
djConfig["baseRelativePath"]=_ab;
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
var _b3=dua.indexOf("Gecko");
drh.mozilla=drh.moz=(_b3>=0)&&(!drh.khtml);
if(drh.mozilla){
drh.geckoVersion=dua.substring(_b3+6,_b3+14);
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
var _b5=window["document"];
var tdi=_b5["implementation"];
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
}else{
}
})();
dojo.hostenv.startPackage("dojo.hostenv");
dojo.render.name=dojo.hostenv.name_="browser";
dojo.hostenv.searchIds=[];
dojo.hostenv._XMLHTTP_PROGIDS=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"];
dojo.hostenv.getXmlhttpObject=function(){
var _b9=null;
var _ba=null;
try{
_b9=new XMLHttpRequest();
}
catch(e){
}
if(!_b9){
for(var i=0;i<3;++i){
var _bc=dojo.hostenv._XMLHTTP_PROGIDS[i];
try{
_b9=new ActiveXObject(_bc);
}
catch(e){
_ba=e;
}
if(_b9){
dojo.hostenv._XMLHTTP_PROGIDS=[_bc];
break;
}
}
}
if(!_b9){
return dojo.raise("XMLHTTP not available",_ba);
}
return _b9;
};
dojo.hostenv._blockAsync=false;
dojo.hostenv.getText=function(uri,_be,_bf){
if(!_be){
this._blockAsync=true;
}
var _c0=this.getXmlhttpObject();
function isDocumentOk(_c1){
var _c2=_c1["status"];
return Boolean((!_c2)||((200<=_c2)&&(300>_c2))||(_c2==304));
}
if(_be){
var _c3=this,_c4=null,gbl=dojo.global();
var xhr=dojo.evalObjPath("dojo.io.XMLHTTPTransport");
_c0.onreadystatechange=function(){
if(_c4){
gbl.clearTimeout(_c4);
_c4=null;
}
if(_c3._blockAsync||(xhr&&xhr._blockAsync)){
_c4=gbl.setTimeout(function(){
_c0.onreadystatechange.apply(this);
},10);
}else{
if(4==_c0.readyState){
if(isDocumentOk(_c0)){
_be(_c0.responseText);
}
}
}
};
}
_c0.open("GET",uri,_be?true:false);
try{
_c0.send(null);
if(_be){
return null;
}
if(!isDocumentOk(_c0)){
var err=Error("Unable to load "+uri+" status:"+_c0.status);
err.status=_c0.status;
err.responseText=_c0.responseText;
throw err;
}
}
catch(e){
this._blockAsync=false;
if((_bf)&&(!_be)){
return null;
}else{
throw e;
}
}
this._blockAsync=false;
return _c0.responseText;
};
dojo.hostenv.defaultDebugContainerId="dojoDebug";
dojo.hostenv._println_buffer=[];
dojo.hostenv._println_safe=false;
dojo.hostenv.println=function(_c8){
if(!dojo.hostenv._println_safe){
dojo.hostenv._println_buffer.push(_c8);
}else{
try{
var _c9=document.getElementById(djConfig.debugContainerId?djConfig.debugContainerId:dojo.hostenv.defaultDebugContainerId);
if(!_c9){
_c9=dojo.body();
}
var div=document.createElement("div");
div.appendChild(document.createTextNode(_c8));
_c9.appendChild(div);
}
catch(e){
try{
document.write("<div>"+_c8+"</div>");
}
catch(e2){
window.status=_c8;
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
function dj_addNodeEvtHdlr(_cb,_cc,fp){
var _ce=_cb["on"+_cc]||function(){
};
_cb["on"+_cc]=function(){
fp.apply(_cb,arguments);
_ce.apply(_cb,arguments);
};
return true;
}
dojo.hostenv._djInitFired=false;
function dj_load_init(e){
dojo.hostenv._djInitFired=true;
var _d0=(e&&e.type)?e.type.toLowerCase():"load";
if(arguments.callee.initialized||(_d0!="domcontentloaded"&&_d0!="load")){
return;
}
arguments.callee.initialized=true;
if(typeof (_timer)!="undefined"){
clearInterval(_timer);
delete _timer;
}
var _d1=function(){
if(dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
};
if(dojo.hostenv.inFlightCount==0){
_d1();
dojo.hostenv.modulesLoaded();
}else{
dojo.hostenv.modulesLoadedListeners.unshift(_d1);
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
var _d3=[];
if(djConfig.searchIds&&djConfig.searchIds.length>0){
_d3=_d3.concat(djConfig.searchIds);
}
if(dojo.hostenv.searchIds&&dojo.hostenv.searchIds.length>0){
_d3=_d3.concat(dojo.hostenv.searchIds);
}
if((djConfig.parseWidgets)||(_d3.length>0)){
if(dojo.evalObjPath("dojo.widget.Parse")){
var _d4=new dojo.xml.Parse();
if(_d3.length>0){
for(var x=0;x<_d3.length;x++){
var _d6=document.getElementById(_d3[x]);
if(!_d6){
continue;
}
var _d7=_d4.parseElement(_d6,null,true);
dojo.widget.getParser().createComponents(_d7);
}
}else{
if(djConfig.parseWidgets){
var _d7=_d4.parseElement(dojo.body(),null,true);
dojo.widget.getParser().createComponents(_d7);
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
dojo.setContext=function(_dc,_dd){
dj_currentContext=_dc;
dj_currentDocument=_dd;
};
dojo._fireCallback=function(_de,_df,_e0){
if((_df)&&((typeof _de=="string")||(_de instanceof String))){
_de=_df[_de];
}
return (_df?_de.apply(_df,_e0||[]):_de());
};
dojo.withGlobal=function(_e1,_e2,_e3,_e4){
var _e5;
var _e6=dj_currentContext;
var _e7=dj_currentDocument;
try{
dojo.setContext(_e1,_e1.document);
_e5=dojo._fireCallback(_e2,_e3,_e4);
}
finally{
dojo.setContext(_e6,_e7);
}
return _e5;
};
dojo.withDoc=function(_e8,_e9,_ea,_eb){
var _ec;
var _ed=dj_currentDocument;
try{
dj_currentDocument=_e8;
_ec=dojo._fireCallback(_e9,_ea,_eb);
}
finally{
dj_currentDocument=_ed;
}
return _ec;
};
}
dojo.requireIf((djConfig["isDebug"]||djConfig["debugAtAllCosts"]),"dojo.debug");
dojo.requireIf(djConfig["debugAtAllCosts"]&&!window.widget&&!djConfig["useXDomain"],"dojo.browser_debug");
dojo.requireIf(djConfig["debugAtAllCosts"]&&!window.widget&&djConfig["useXDomain"],"dojo.browser_debug_xd");
dojo.provide("dojo.lang.common");
dojo.lang.inherits=function(_ee,_ef){
if(!dojo.lang.isFunction(_ef)){
dojo.raise("dojo.inherits: superclass argument ["+_ef+"] must be a function (subclass: ["+_ee+"']");
}
_ee.prototype=new _ef();
_ee.prototype.constructor=_ee;
_ee.superclass=_ef.prototype;
_ee["super"]=_ef.prototype;
};
dojo.lang._mixin=function(obj,_f1){
var _f2={};
for(var x in _f1){
if((typeof _f2[x]=="undefined")||(_f2[x]!=_f1[x])){
obj[x]=_f1[x];
}
}
if(dojo.render.html.ie&&(typeof (_f1["toString"])=="function")&&(_f1["toString"]!=obj["toString"])&&(_f1["toString"]!=_f2["toString"])){
obj.toString=_f1.toString;
}
return obj;
};
dojo.lang.mixin=function(obj,_f5){
for(var i=1,l=arguments.length;i<l;i++){
dojo.lang._mixin(obj,arguments[i]);
}
return obj;
};
dojo.lang.extend=function(_f8,_f9){
for(var i=1,l=arguments.length;i<l;i++){
dojo.lang._mixin(_f8.prototype,arguments[i]);
}
return _f8;
};
dojo.inherits=dojo.lang.inherits;
dojo.mixin=dojo.lang.mixin;
dojo.extend=dojo.lang.extend;
dojo.lang.find=function(_fc,_fd,_fe,_ff){
if(!dojo.lang.isArrayLike(_fc)&&dojo.lang.isArrayLike(_fd)){
dojo.deprecated("dojo.lang.find(value, array)","use dojo.lang.find(array, value) instead","0.5");
var temp=_fc;
_fc=_fd;
_fd=temp;
}
var _101=dojo.lang.isString(_fc);
if(_101){
_fc=_fc.split("");
}
if(_ff){
var step=-1;
var i=_fc.length-1;
var end=-1;
}else{
var step=1;
var i=0;
var end=_fc.length;
}
if(_fe){
while(i!=end){
if(_fc[i]===_fd){
return i;
}
i+=step;
}
}else{
while(i!=end){
if(_fc[i]==_fd){
return i;
}
i+=step;
}
}
return -1;
};
dojo.lang.indexOf=dojo.lang.find;
dojo.lang.findLast=function(_105,_106,_107){
return dojo.lang.find(_105,_106,_107,true);
};
dojo.lang.lastIndexOf=dojo.lang.findLast;
dojo.lang.inArray=function(_108,_109){
return dojo.lang.find(_108,_109)>-1;
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
return (it instanceof Function||typeof it=="function");
};
(function(){
if((dojo.render.html.capable)&&(dojo.render.html["safari"])){
dojo.lang.isFunction=function(it){
if((typeof (it)=="function")&&(it=="[object NodeList]")){
return false;
}
return (it instanceof Function||typeof it=="function");
};
}
})();
dojo.lang.isString=function(it){
return (typeof it=="string"||it instanceof String);
};
dojo.lang.isAlien=function(it){
if(!it){
return false;
}
return !dojo.lang.isFunction(it)&&/\{\s*\[native code\]\s*\}/.test(String(it));
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
dojo.lang.mixin(dojo.lang,{has:function(obj,name){
try{
return typeof obj[name]!="undefined";
}
catch(e){
return false;
}
},isEmpty:function(obj){
if(dojo.lang.isObject(obj)){
var tmp={};
var _118=0;
for(var x in obj){
if(obj[x]&&(!tmp[x])){
_118++;
break;
}
}
return _118==0;
}else{
if(dojo.lang.isArrayLike(obj)||dojo.lang.isString(obj)){
return obj.length==0;
}
}
},map:function(arr,obj,_11c){
var _11d=dojo.lang.isString(arr);
if(_11d){
arr=arr.split("");
}
if(dojo.lang.isFunction(obj)&&(!_11c)){
_11c=obj;
obj=dj_global;
}else{
if(dojo.lang.isFunction(obj)&&_11c){
var _11e=obj;
obj=_11c;
_11c=_11e;
}
}
if(Array.map){
var _11f=Array.map(arr,_11c,obj);
}else{
var _11f=[];
for(var i=0;i<arr.length;++i){
_11f.push(_11c.call(obj,arr[i]));
}
}
if(_11d){
return _11f.join("");
}else{
return _11f;
}
},reduce:function(arr,_122,obj,_124){
var _125=_122;
if(arguments.length==2){
_124=_122;
_125=arr[0];
arr=arr.slice(1);
}else{
if(arguments.length==3){
if(dojo.lang.isFunction(obj)){
_124=obj;
obj=null;
}
}else{
if(dojo.lang.isFunction(obj)){
var tmp=_124;
_124=obj;
obj=tmp;
}
}
}
var ob=obj||dj_global;
dojo.lang.map(arr,function(val){
_125=_124.call(ob,_125,val);
});
return _125;
},forEach:function(_129,_12a,_12b){
if(dojo.lang.isString(_129)){
_129=_129.split("");
}
if(Array.forEach){
Array.forEach(_129,_12a,_12b);
}else{
if(!_12b){
_12b=dj_global;
}
for(var i=0,l=_129.length;i<l;i++){
_12a.call(_12b,_129[i],i,_129);
}
}
},_everyOrSome:function(_12e,arr,_130,_131){
if(dojo.lang.isString(arr)){
arr=arr.split("");
}
if(Array.every){
return Array[_12e?"every":"some"](arr,_130,_131);
}else{
if(!_131){
_131=dj_global;
}
for(var i=0,l=arr.length;i<l;i++){
var _134=_130.call(_131,arr[i],i,arr);
if(_12e&&!_134){
return false;
}else{
if((!_12e)&&(_134)){
return true;
}
}
}
return Boolean(_12e);
}
},every:function(arr,_136,_137){
return this._everyOrSome(true,arr,_136,_137);
},some:function(arr,_139,_13a){
return this._everyOrSome(false,arr,_139,_13a);
},filter:function(arr,_13c,_13d){
var _13e=dojo.lang.isString(arr);
if(_13e){
arr=arr.split("");
}
var _13f;
if(Array.filter){
_13f=Array.filter(arr,_13c,_13d);
}else{
if(!_13d){
if(arguments.length>=3){
dojo.raise("thisObject doesn't exist!");
}
_13d=dj_global;
}
_13f=[];
for(var i=0;i<arr.length;i++){
if(_13c.call(_13d,arr[i],i,arr)){
_13f.push(arr[i]);
}
}
}
if(_13e){
return _13f.join("");
}else{
return _13f;
}
},unnest:function(){
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
},toArray:function(_144,_145){
var _146=[];
for(var i=_145||0;i<_144.length;i++){
_146.push(_144[i]);
}
return _146;
}});
dojo.provide("dojo.lang.extras");
dojo.lang.setTimeout=function(func,_149){
var _14a=window,_14b=2;
if(!dojo.lang.isFunction(func)){
_14a=func;
func=_149;
_149=arguments[2];
_14b++;
}
if(dojo.lang.isString(func)){
func=_14a[func];
}
var args=[];
for(var i=_14b;i<arguments.length;i++){
args.push(arguments[i]);
}
return dojo.global().setTimeout(function(){
func.apply(_14a,args);
},_149);
};
dojo.lang.clearTimeout=function(_14e){
dojo.global().clearTimeout(_14e);
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
dojo.lang.getObjPathValue=function(_157,_158,_159){
with(dojo.parseObjPath(_157,_158,_159)){
return dojo.evalProp(prop,obj,_159);
}
};
dojo.lang.setObjPathValue=function(_15a,_15b,_15c,_15d){
dojo.deprecated("dojo.lang.setObjPathValue","use dojo.parseObjPath and the '=' operator","0.6");
if(arguments.length<4){
_15d=true;
}
with(dojo.parseObjPath(_15a,_15c,_15d)){
if(obj&&(_15d||(prop in obj))){
obj[prop]=_15b;
}
}
};
dojo.provide("dojo.lang.func");
dojo.lang.hitch=function(_15e,_15f){
var args=[];
for(var x=2;x<arguments.length;x++){
args.push(arguments[x]);
}
var fcn=(dojo.lang.isString(_15f)?_15e[_15f]:_15f)||function(){
};
return function(){
var ta=args.concat([]);
for(var x=0;x<arguments.length;x++){
ta.push(arguments[x]);
}
return fcn.apply(_15e,ta);
};
};
dojo.lang.anonCtr=0;
dojo.lang.anon={};
dojo.lang.nameAnonFunc=function(_165,_166,_167){
var nso=(_166||dojo.lang.anon);
if((_167)||((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true))){
for(var x in nso){
try{
if(nso[x]===_165){
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
nso[ret]=_165;
return ret;
};
dojo.lang.forward=function(_16b){
return function(){
return this[_16b].apply(this,arguments);
};
};
dojo.lang.curry=function(_16c,func){
var _16e=[];
_16c=_16c||dj_global;
if(dojo.lang.isString(func)){
func=_16c[func];
}
for(var x=2;x<arguments.length;x++){
_16e.push(arguments[x]);
}
var _170=(func["__preJoinArity"]||func.length)-_16e.length;
function gather(_171,_172,_173){
var _174=_173;
var _175=_172.slice(0);
for(var x=0;x<_171.length;x++){
_175.push(_171[x]);
}
_173=_173-_171.length;
if(_173<=0){
var res=func.apply(_16c,_175);
_173=_174;
return res;
}else{
return function(){
return gather(arguments,_175,_173);
};
}
}
return gather([],_16e,_170);
};
dojo.lang.curryArguments=function(_178,func,args,_17b){
var _17c=[];
var x=_17b||0;
for(x=_17b;x<args.length;x++){
_17c.push(args[x]);
}
return dojo.lang.curry.apply(dojo.lang,[_178,func].concat(_17c));
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
dojo.lang.delayThese=function(farr,cb,_182,_183){
if(!farr.length){
if(typeof _183=="function"){
_183();
}
return;
}
if((typeof _182=="undefined")&&(typeof cb=="number")){
_182=cb;
cb=function(){
};
}else{
if(!cb){
cb=function(){
};
if(!_182){
_182=0;
}
}
}
setTimeout(function(){
(farr.shift())();
cb();
dojo.lang.delayThese(farr,cb,_182,_183);
},_182);
};
dojo.provide("dojo.event.common");
dojo.event=new function(){
this._canTimeout=dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);
function interpolateArgs(args,_185){
var dl=dojo.lang;
var ao={srcObj:dj_global,srcFunc:null,adviceObj:dj_global,adviceFunc:null,aroundObj:null,aroundFunc:null,adviceType:(args.length>2)?args[0]:"after",precedence:"last",once:false,delay:null,rate:0,adviceMsg:false,maxCalls:-1};
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
var _188=dl.nameAnonFunc(args[2],ao.adviceObj,_185);
ao.adviceFunc=_188;
}else{
if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){
ao.adviceType="after";
ao.srcObj=dj_global;
var _188=dl.nameAnonFunc(args[0],ao.srcObj,_185);
ao.srcFunc=_188;
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
var _188=dl.nameAnonFunc(args[1],dj_global,_185);
ao.srcFunc=_188;
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){
ao.srcObj=args[1];
ao.srcFunc=args[2];
var _188=dl.nameAnonFunc(args[3],dj_global,_185);
ao.adviceObj=dj_global;
ao.adviceFunc=_188;
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
ao.maxCalls=(!isNaN(parseInt(args[11])))?args[11]:-1;
break;
}
if(dl.isFunction(ao.aroundFunc)){
var _188=dl.nameAnonFunc(ao.aroundFunc,ao.aroundObj,_185);
ao.aroundFunc=_188;
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
var _18a={};
for(var x in ao){
_18a[x]=ao[x];
}
var mjps=[];
dojo.lang.forEach(ao.srcObj,function(src){
if((dojo.render.html.capable)&&(dojo.lang.isString(src))){
src=dojo.byId(src);
}
_18a.srcObj=src;
mjps.push(dojo.event.connect.call(dojo.event,_18a));
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
var _192;
if((arguments.length==1)&&(typeof a1=="object")){
_192=a1;
}else{
_192={srcObj:a1,srcFunc:a2};
}
_192.adviceFunc=function(){
var _193=[];
for(var x=0;x<arguments.length;x++){
_193.push(arguments[x]);
}
dojo.debug("("+_192.srcObj+")."+_192.srcFunc,":",_193.join(", "));
};
this.kwConnect(_192);
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
this.connectRunOnce=function(){
var ao=interpolateArgs(arguments,true);
ao.maxCalls=1;
return this.connect(ao);
};
this._kwConnectImpl=function(_19b,_19c){
var fn=(_19c)?"disconnect":"connect";
if(typeof _19b["srcFunc"]=="function"){
_19b.srcObj=_19b["srcObj"]||dj_global;
var _19e=dojo.lang.nameAnonFunc(_19b.srcFunc,_19b.srcObj,true);
_19b.srcFunc=_19e;
}
if(typeof _19b["adviceFunc"]=="function"){
_19b.adviceObj=_19b["adviceObj"]||dj_global;
var _19e=dojo.lang.nameAnonFunc(_19b.adviceFunc,_19b.adviceObj,true);
_19b.adviceFunc=_19e;
}
_19b.srcObj=_19b["srcObj"]||dj_global;
_19b.adviceObj=_19b["adviceObj"]||_19b["targetObj"]||dj_global;
_19b.adviceFunc=_19b["adviceFunc"]||_19b["targetFunc"];
return dojo.event[fn](_19b);
};
this.kwConnect=function(_19f){
return this._kwConnectImpl(_19f,false);
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
if(!ao.srcObj[ao.srcFunc]){
return null;
}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc,true);
mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);
return mjp;
};
this.kwDisconnect=function(_1a2){
return this._kwConnectImpl(_1a2,true);
};
};
dojo.event.MethodInvocation=function(_1a3,obj,args){
this.jp_=_1a3;
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
dojo.event.MethodJoinPoint=function(obj,_1ab){
this.object=obj||dj_global;
this.methodname=_1ab;
this.methodfunc=this.object[_1ab];
this.squelch=false;
};
dojo.event.MethodJoinPoint.getForMethod=function(obj,_1ad){
if(!obj){
obj=dj_global;
}
var ofn=obj[_1ad];
if(!ofn){
ofn=obj[_1ad]=function(){
};
if(!obj[_1ad]){
dojo.raise("Cannot set do-nothing method on that object "+_1ad);
}
}else{
if((typeof ofn!="function")&&(!dojo.lang.isFunction(ofn))&&(!dojo.lang.isAlien(ofn))){
return null;
}
}
var _1af=_1ad+"$joinpoint";
var _1b0=_1ad+"$joinpoint$method";
var _1b1=obj[_1af];
if(!_1b1){
var _1b2=false;
if(dojo.event["browser"]){
if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){
_1b2=true;
dojo.event.browser.addClobberNodeAttrs(obj,[_1af,_1b0,_1ad]);
}
}
var _1b3=ofn.length;
obj[_1b0]=ofn;
_1b1=obj[_1af]=new dojo.event.MethodJoinPoint(obj,_1b0);
if(!_1b2){
obj[_1ad]=function(){
return _1b1.run.apply(_1b1,arguments);
};
}else{
obj[_1ad]=function(){
var args=[];
if(!arguments.length){
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
if((x==0)&&(dojo.event.browser.isEvent(arguments[x]))){
args.push(dojo.event.browser.fixEvent(arguments[x],this));
}else{
args.push(arguments[x]);
}
}
}
return _1b1.run.apply(_1b1,args);
};
}
obj[_1ad].__preJoinArity=_1b3;
}
return _1b1;
};
dojo.lang.extend(dojo.event.MethodJoinPoint,{squelch:false,unintercept:function(){
this.object[this.methodname]=this.methodfunc;
this.before=[];
this.after=[];
this.around=[];
},disconnect:dojo.lang.forward("unintercept"),run:function(){
var obj=this.object||dj_global;
var args=arguments;
var _1b9=[];
for(var x=0;x<args.length;x++){
_1b9[x]=args[x];
}
var _1bb=function(marr){
if(!marr){
dojo.debug("Null argument to unrollAdvice()");
return;
}
var _1bd=marr[0]||dj_global;
var _1be=marr[1];
if(!_1bd[_1be]){
dojo.raise("function \""+_1be+"\" does not exist on \""+_1bd+"\"");
}
var _1bf=marr[2]||dj_global;
var _1c0=marr[3];
var msg=marr[6];
var _1c2=marr[7];
if(_1c2>-1){
if(_1c2==0){
return;
}
marr[7]--;
}
var _1c3;
var to={args:[],jp_:this,object:obj,proceed:function(){
return _1bd[_1be].apply(_1bd,to.args);
}};
to.args=_1b9;
var _1c5=parseInt(marr[4]);
var _1c6=((!isNaN(_1c5))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));
if(marr[5]){
var rate=parseInt(marr[5]);
var cur=new Date();
var _1c9=false;
if((marr["last"])&&((cur-marr.last)<=rate)){
if(dojo.event._canTimeout){
if(marr["delayTimer"]){
clearTimeout(marr.delayTimer);
}
var tod=parseInt(rate*2);
var mcpy=dojo.lang.shallowCopy(marr);
marr.delayTimer=setTimeout(function(){
mcpy[5]=0;
_1bb(mcpy);
},tod);
}
return;
}else{
marr.last=cur;
}
}
if(_1c0){
_1bf[_1c0].call(_1bf,to);
}else{
if((_1c6)&&((dojo.render.html)||(dojo.render.svg))){
dj_global["setTimeout"](function(){
if(msg){
_1bd[_1be].call(_1bd,to);
}else{
_1bd[_1be].apply(_1bd,args);
}
},_1c5);
}else{
if(msg){
_1bd[_1be].call(_1bd,to);
}else{
_1bd[_1be].apply(_1bd,args);
}
}
}
};
var _1cc=function(){
if(this.squelch){
try{
return _1bb.apply(this,arguments);
}
catch(e){
dojo.debug(e);
}
}else{
return _1bb.apply(this,arguments);
}
};
if((this["before"])&&(this.before.length>0)){
dojo.lang.forEach(this.before.concat(new Array()),_1cc);
}
var _1cd;
try{
if((this["around"])&&(this.around.length>0)){
var mi=new dojo.event.MethodInvocation(this,obj,args);
_1cd=mi.proceed();
}else{
if(this.methodfunc){
_1cd=this.object[this.methodname].apply(this.object,args);
}
}
}
catch(e){
if(!this.squelch){
dojo.debug(e,"when calling",this.methodname,"on",this.object,"with arguments",args);
dojo.raise(e);
}
}
if((this["after"])&&(this.after.length>0)){
dojo.lang.forEach(this.after.concat(new Array()),_1cc);
}
return (this.methodfunc)?_1cd:null;
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
this.addAdvice(args["adviceObj"],args["adviceFunc"],args["aroundObj"],args["aroundFunc"],args["adviceType"],args["precedence"],args["once"],args["delay"],args["rate"],args["adviceMsg"],args["maxCalls"]);
},addAdvice:function(_1d2,_1d3,_1d4,_1d5,_1d6,_1d7,once,_1d9,rate,_1db,_1dc){
var arr=this.getArr(_1d6);
if(!arr){
dojo.raise("bad this: "+this);
}
var ao=[_1d2,_1d3,_1d4,_1d5,_1d9,rate,_1db,_1dc];
if(once){
if(this.hasAdvice(_1d2,_1d3,_1d6,arr)>=0){
return;
}
}
if(_1d7=="first"){
arr.unshift(ao);
}else{
arr.push(ao);
}
},hasAdvice:function(_1df,_1e0,_1e1,arr){
if(!arr){
arr=this.getArr(_1e1);
}
var ind=-1;
for(var x=0;x<arr.length;x++){
var aao=(typeof _1e0=="object")?(new String(_1e0)).toString():_1e0;
var a1o=(typeof arr[x][1]=="object")?(new String(arr[x][1])).toString():arr[x][1];
if((arr[x][0]==_1df)&&(a1o==aao)){
ind=x;
}
}
return ind;
},removeAdvice:function(_1e7,_1e8,_1e9,once){
var arr=this.getArr(_1e9);
var ind=this.hasAdvice(_1e7,_1e8,_1e9,arr);
if(ind==-1){
return false;
}
while(ind!=-1){
arr.splice(ind,1);
if(once){
break;
}
ind=this.hasAdvice(_1e7,_1e8,_1e9,arr);
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
this.clobber=function(_1ef){
var na;
var tna;
if(_1ef){
tna=_1ef.all||_1ef.getElementsByTagName("*");
na=[_1ef];
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
var _1f3={};
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
if(dojo.widget){
for(var name in dojo.widget._templateCache){
if(dojo.widget._templateCache[name].node){
dojo.dom.destroyNode(dojo.widget._templateCache[name].node);
dojo.widget._templateCache[name].node=null;
delete dojo.widget._templateCache[name].node;
}
}
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
var _1f8=0;
this.normalizedEventName=function(_1f9){
switch(_1f9){
case "CheckboxStateChange":
case "DOMAttrModified":
case "DOMMenuItemActive":
case "DOMMenuItemInactive":
case "DOMMouseScroll":
case "DOMNodeInserted":
case "DOMNodeRemoved":
case "RadioStateChange":
return _1f9;
break;
default:
var lcn=_1f9.toLowerCase();
return (lcn.indexOf("on")==0)?lcn.substr(2):lcn;
break;
}
};
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
this.addClobberNodeAttrs=function(node,_1fe){
if(!dojo.render.html.ie){
return;
}
this.addClobberNode(node);
for(var x=0;x<_1fe.length;x++){
node.__clobberAttrs__.push(_1fe[x]);
}
};
this.removeListener=function(node,_201,fp,_203){
if(!_203){
var _203=false;
}
_201=dojo.event.browser.normalizedEventName(_201);
if(_201=="key"){
if(dojo.render.html.ie){
this.removeListener(node,"onkeydown",fp,_203);
}
_201="keypress";
}
if(node.removeEventListener){
node.removeEventListener(_201,fp,_203);
}
};
this.addListener=function(node,_205,fp,_207,_208){
if(!node){
return;
}
if(!_207){
var _207=false;
}
_205=dojo.event.browser.normalizedEventName(_205);
if(_205=="key"){
if(dojo.render.html.ie){
this.addListener(node,"onkeydown",fp,_207,_208);
}
_205="keypress";
}
if(!_208){
var _209=function(evt){
if(!evt){
evt=window.event;
}
var ret=fp(dojo.event.browser.fixEvent(evt,this));
if(_207){
dojo.event.browser.stopEvent(evt);
}
return ret;
};
}else{
_209=fp;
}
if(node.addEventListener){
node.addEventListener(_205,_209,_207);
return _209;
}else{
_205="on"+_205;
if(typeof node[_205]=="function"){
var _20c=node[_205];
node[_205]=function(e){
_20c(e);
return _209(e);
};
}else{
node[_205]=_209;
}
if(dojo.render.html.ie){
this.addClobberNodeAttrs(node,[_205]);
}
return _209;
}
};
this.isEvent=function(obj){
return (typeof obj!="undefined")&&(obj)&&(typeof Event!="undefined")&&(obj.eventPhase);
};
this.currentEvent=null;
this.callListener=function(_20f,_210){
if(typeof _20f!="function"){
dojo.raise("listener not a function: "+_20f);
}
dojo.event.browser.currentEvent.currentTarget=_210;
return _20f.call(_210,dojo.event.browser.currentEvent);
};
this._stopPropagation=function(){
dojo.event.browser.currentEvent.cancelBubble=true;
};
this._preventDefault=function(){
dojo.event.browser.currentEvent.returnValue=false;
};
this.keys={KEY_BACKSPACE:8,KEY_TAB:9,KEY_CLEAR:12,KEY_ENTER:13,KEY_SHIFT:16,KEY_CTRL:17,KEY_ALT:18,KEY_PAUSE:19,KEY_CAPS_LOCK:20,KEY_ESCAPE:27,KEY_SPACE:32,KEY_PAGE_UP:33,KEY_PAGE_DOWN:34,KEY_END:35,KEY_HOME:36,KEY_LEFT_ARROW:37,KEY_UP_ARROW:38,KEY_RIGHT_ARROW:39,KEY_DOWN_ARROW:40,KEY_INSERT:45,KEY_DELETE:46,KEY_HELP:47,KEY_LEFT_WINDOW:91,KEY_RIGHT_WINDOW:92,KEY_SELECT:93,KEY_NUMPAD_0:96,KEY_NUMPAD_1:97,KEY_NUMPAD_2:98,KEY_NUMPAD_3:99,KEY_NUMPAD_4:100,KEY_NUMPAD_5:101,KEY_NUMPAD_6:102,KEY_NUMPAD_7:103,KEY_NUMPAD_8:104,KEY_NUMPAD_9:105,KEY_NUMPAD_MULTIPLY:106,KEY_NUMPAD_PLUS:107,KEY_NUMPAD_ENTER:108,KEY_NUMPAD_MINUS:109,KEY_NUMPAD_PERIOD:110,KEY_NUMPAD_DIVIDE:111,KEY_F1:112,KEY_F2:113,KEY_F3:114,KEY_F4:115,KEY_F5:116,KEY_F6:117,KEY_F7:118,KEY_F8:119,KEY_F9:120,KEY_F10:121,KEY_F11:122,KEY_F12:123,KEY_F13:124,KEY_F14:125,KEY_F15:126,KEY_NUM_LOCK:144,KEY_SCROLL_LOCK:145};
this.revKeys=[];
for(var key in this.keys){
this.revKeys[this.keys[key]]=key;
}
this.fixEvent=function(evt,_213){
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
var _215=evt.keyCode;
if(_215>=65&&_215<=90&&evt.shiftKey==false){
_215+=32;
}
if(_215>=1&&_215<=26&&evt.ctrlKey){
_215+=96;
}
evt.key=String.fromCharCode(_215);
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
var _215=evt.which;
if((evt.ctrlKey||evt.altKey||evt.metaKey)&&(evt.which>=65&&evt.which<=90&&evt.shiftKey==false)){
_215+=32;
}
evt.key=String.fromCharCode(_215);
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
case 25:
evt.key=evt.KEY_TAB;
evt.shift=true;
break;
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
case 63236:
evt.key=evt.KEY_F1;
break;
case 63237:
evt.key=evt.KEY_F2;
break;
case 63238:
evt.key=evt.KEY_F3;
break;
case 63239:
evt.key=evt.KEY_F4;
break;
case 63240:
evt.key=evt.KEY_F5;
break;
case 63241:
evt.key=evt.KEY_F6;
break;
case 63242:
evt.key=evt.KEY_F7;
break;
case 63243:
evt.key=evt.KEY_F8;
break;
case 63244:
evt.key=evt.KEY_F9;
break;
case 63245:
evt.key=evt.KEY_F10;
break;
case 63246:
evt.key=evt.KEY_F11;
break;
case 63247:
evt.key=evt.KEY_F12;
break;
case 63250:
evt.key=evt.KEY_PAUSE;
break;
case 63272:
evt.key=evt.KEY_DELETE;
break;
case 63273:
evt.key=evt.KEY_HOME;
break;
case 63275:
evt.key=evt.KEY_END;
break;
case 63276:
evt.key=evt.KEY_PAGE_UP;
break;
case 63277:
evt.key=evt.KEY_PAGE_DOWN;
break;
case 63302:
evt.key=evt.KEY_INSERT;
break;
case 63248:
case 63249:
case 63289:
break;
default:
evt.key=evt.charCode>=evt.KEY_SPACE?String.fromCharCode(evt.charCode):evt.keyCode;
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
evt.currentTarget=(_213?_213:evt.srcElement);
}
if(!evt.layerX){
evt.layerX=evt.offsetX;
}
if(!evt.layerY){
evt.layerY=evt.offsetY;
}
var doc=(evt.srcElement&&evt.srcElement.ownerDocument)?evt.srcElement.ownerDocument:document;
var _217=((dojo.render.html.ie55)||(doc["compatMode"]=="BackCompat"))?doc.body:doc.documentElement;
if(!evt.pageX){
evt.pageX=evt.clientX+(_217.scrollLeft||0);
}
if(!evt.pageY){
evt.pageY=evt.clientY+(_217.scrollTop||0);
}
if(evt.type=="mouseover"){
evt.relatedTarget=evt.fromElement;
}
if(evt.type=="mouseout"){
evt.relatedTarget=evt.toElement;
}
this.currentEvent=evt;
evt.callListener=this.callListener;
evt.stopPropagation=this._stopPropagation;
evt.preventDefault=this._preventDefault;
}
return evt;
};
this.stopEvent=function(evt){
if(window.event){
evt.cancelBubble=true;
evt.returnValue=false;
}else{
evt.preventDefault();
evt.stopPropagation();
}
};
};
dojo.provide("dojo.string.common");
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
dojo.string.repeat=function(str,_21f,_220){
var out="";
for(var i=0;i<_21f;i++){
out+=str;
if(_220&&i<_21f-1){
out+=_220;
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
dojo.provide("dojo.io.common");
dojo.io.transports=[];
dojo.io.hdlrFuncNames=["load","error","timeout"];
dojo.io.Request=function(url,_22f,_230,_231){
if((arguments.length==1)&&(arguments[0].constructor==Object)){
this.fromKwArgs(arguments[0]);
}else{
this.url=url;
if(_22f){
this.mimetype=_22f;
}
if(_230){
this.transport=_230;
}
if(arguments.length>=4){
this.changeUrl=_231;
}
}
};
dojo.lang.extend(dojo.io.Request,{url:"",mimetype:"text/plain",method:"GET",content:undefined,transport:undefined,changeUrl:undefined,formNode:undefined,sync:false,bindSuccess:false,useCache:false,preventCache:false,jsonFilter:function(_232){
if((this.mimetype=="text/json-comment-filtered")||(this.mimetype=="application/json-comment-filtered")){
var _233=_232.indexOf("/*");
var _234=_232.lastIndexOf("*/");
if((_233==-1)||(_234==-1)){
dojo.debug("your JSON wasn't comment filtered!");
return "";
}
return _232.substring(_233+2,_234);
}
dojo.debug("please consider using a mimetype of text/json-comment-filtered to avoid potential security issues with JSON endpoints");
return _232;
},load:function(type,data,_237,_238){
},error:function(type,_23a,_23b,_23c){
},timeout:function(type,_23e,_23f,_240){
},handle:function(type,data,_243,_244){
},timeoutSeconds:0,abort:function(){
},fromKwArgs:function(_245){
if(_245["url"]){
_245.url=_245.url.toString();
}
if(_245["formNode"]){
_245.formNode=dojo.byId(_245.formNode);
}
if(!_245["method"]&&_245["formNode"]&&_245["formNode"].method){
_245.method=_245["formNode"].method;
}
if(!_245["handle"]&&_245["handler"]){
_245.handle=_245.handler;
}
if(!_245["load"]&&_245["loaded"]){
_245.load=_245.loaded;
}
if(!_245["changeUrl"]&&_245["changeURL"]){
_245.changeUrl=_245.changeURL;
}
_245.encoding=dojo.lang.firstValued(_245["encoding"],djConfig["bindEncoding"],"");
_245.sendTransport=dojo.lang.firstValued(_245["sendTransport"],djConfig["ioSendTransport"],false);
var _246=dojo.lang.isFunction;
for(var x=0;x<dojo.io.hdlrFuncNames.length;x++){
var fn=dojo.io.hdlrFuncNames[x];
if(_245[fn]&&_246(_245[fn])){
continue;
}
if(_245["handle"]&&_246(_245["handle"])){
_245[fn]=_245.handle;
}
}
dojo.lang.mixin(this,_245);
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
dojo.io.bind=function(_24d){
if(!(_24d instanceof dojo.io.Request)){
try{
_24d=new dojo.io.Request(_24d);
}
catch(e){
dojo.debug(e);
}
}
var _24e="";
if(_24d["transport"]){
_24e=_24d["transport"];
if(!this[_24e]){
dojo.io.sendBindError(_24d,"No dojo.io.bind() transport with name '"+_24d["transport"]+"'.");
return _24d;
}
if(!this[_24e].canHandle(_24d)){
dojo.io.sendBindError(_24d,"dojo.io.bind() transport with name '"+_24d["transport"]+"' cannot handle this type of request.");
return _24d;
}
}else{
for(var x=0;x<dojo.io.transports.length;x++){
var tmp=dojo.io.transports[x];
if((this[tmp])&&(this[tmp].canHandle(_24d))){
_24e=tmp;
break;
}
}
if(_24e==""){
dojo.io.sendBindError(_24d,"None of the loaded transports for dojo.io.bind()"+" can handle the request.");
return _24d;
}
}
this[_24e].bind(_24d);
_24d.bindSuccess=true;
return _24d;
};
dojo.io.sendBindError=function(_251,_252){
if((typeof _251.error=="function"||typeof _251.handle=="function")&&(typeof setTimeout=="function"||typeof setTimeout=="object")){
var _253=new dojo.io.Error(_252);
setTimeout(function(){
_251[(typeof _251.error=="function")?"error":"handle"]("error",_253,null,_251);
},50);
}else{
dojo.raise(_252);
}
};
dojo.io.queueBind=function(_254){
if(!(_254 instanceof dojo.io.Request)){
try{
_254=new dojo.io.Request(_254);
}
catch(e){
dojo.debug(e);
}
}
var _255=_254.load;
_254.load=function(){
dojo.io._queueBindInFlight=false;
var ret=_255.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
var _257=_254.error;
_254.error=function(){
dojo.io._queueBindInFlight=false;
var ret=_257.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
dojo.io._bindQueue.push(_254);
dojo.io._dispatchNextQueueBind();
return _254;
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
dojo.io.argsFromMap=function(map,_25a,last){
var enc=/utf/i.test(_25a||"")?encodeURIComponent:dojo.string.encodeAscii;
var _25d=[];
var _25e=new Object();
for(var name in map){
var _260=function(elt){
var val=enc(name)+"="+enc(elt);
_25d[(last==name)?"push":"unshift"](val);
};
if(!_25e[name]){
var _263=map[name];
if(dojo.lang.isArray(_263)){
dojo.lang.forEach(_263,_260);
}else{
_260(_263);
}
}
}
return _25d.join("&");
};
dojo.io.setIFrameSrc=function(_264,src,_266){
try{
var r=dojo.render.html;
if(!_266){
if(r.safari){
_264.location=src;
}else{
frames[_264.name].location=src;
}
}else{
var idoc;
if(r.ie){
idoc=_264.contentWindow.document;
}else{
if(r.safari){
idoc=_264.document;
}else{
idoc=_264.contentWindow;
}
}
if(!idoc){
_264.location=src;
return;
}else{
idoc.location.replace(src);
}
}
}
catch(e){
dojo.debug(e);
dojo.debug("setIFrameSrc: "+e);
}
};
dojo.provide("dojo.string.extras");
dojo.string.substituteParams=function(_269,hash){
var map=(typeof hash=="object")?hash:dojo.lang.toArray(arguments,1);
return _269.replace(/\%\{(\w+)\}/g,function(_26c,key){
if(typeof (map[key])!="undefined"&&map[key]!=null){
return map[key];
}
dojo.raise("Substitution not found: "+key);
});
};
dojo.string.capitalize=function(str){
if(!dojo.lang.isString(str)){
return "";
}
if(arguments.length==0){
str=this;
}
var _26f=str.split(" ");
for(var i=0;i<_26f.length;i++){
_26f[i]=_26f[i].charAt(0).toUpperCase()+_26f[i].substring(1);
}
return _26f.join(" ");
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
var _274=escape(str);
var _275,re=/%u([0-9A-F]{4})/i;
while((_275=_274.match(re))){
var num=Number("0x"+_275[1]);
var _278=escape("&#"+num+";");
ret+=_274.substring(0,_275.index)+_278;
_274=_274.substring(_275.index+_275[0].length);
}
ret+=_274.replace(/\+/g,"%2B");
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
dojo.string.escapeXml=function(str,_27d){
str=str.replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");
if(!_27d){
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
}
return str.substring(0,len).replace(/\.+$/,"")+"...";
};
dojo.string.endsWith=function(str,end,_286){
if(_286){
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
dojo.string.startsWith=function(str,_28a,_28b){
if(_28b){
str=str.toLowerCase();
_28a=_28a.toLowerCase();
}
return str.indexOf(_28a)==0;
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
dojo.string.normalizeNewlines=function(text,_291){
if(_291=="\n"){
text=text.replace(/\r\n/g,"\n");
text=text.replace(/\r/g,"\n");
}else{
if(_291=="\r"){
text=text.replace(/\r\n/g,"\r");
text=text.replace(/\n/g,"\r");
}else{
text=text.replace(/([^\r])\n/g,"$1\r\n").replace(/\r([^\n])/g,"\r\n$1");
}
}
return text;
};
dojo.string.splitEscaped=function(str,_293){
var _294=[];
for(var i=0,_296=0;i<str.length;i++){
if(str.charAt(i)=="\\"){
i++;
continue;
}
if(str.charAt(i)==_293){
_294.push(str.substring(_296,i));
_296=i+1;
}
}
_294.push(str.substr(_296));
return _294;
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
catch(e){
}
}else{
return wh&&!isNaN(wh.nodeType);
}
};
dojo.dom.getUniqueId=function(){
var _298=dojo.doc();
do{
var id="dj_unique_"+(++arguments.callee._idIncrement);
}while(_298.getElementById(id));
return id;
};
dojo.dom.getUniqueId._idIncrement=0;
dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_29a,_29b){
var node=_29a.firstChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.nextSibling;
}
if(_29b&&node&&node.tagName&&node.tagName.toLowerCase()!=_29b.toLowerCase()){
node=dojo.dom.nextElement(node,_29b);
}
return node;
};
dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_29d,_29e){
var node=_29d.lastChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.previousSibling;
}
if(_29e&&node&&node.tagName&&node.tagName.toLowerCase()!=_29e.toLowerCase()){
node=dojo.dom.prevElement(node,_29e);
}
return node;
};
dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(node,_2a1){
if(!node){
return null;
}
do{
node=node.nextSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_2a1&&_2a1.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.nextElement(node,_2a1);
}
return node;
};
dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(node,_2a3){
if(!node){
return null;
}
if(_2a3){
_2a3=_2a3.toLowerCase();
}
do{
node=node.previousSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_2a3&&_2a3.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.prevElement(node,_2a3);
}
return node;
};
dojo.dom.moveChildren=function(_2a4,_2a5,trim){
var _2a7=0;
if(trim){
while(_2a4.hasChildNodes()&&_2a4.firstChild.nodeType==dojo.dom.TEXT_NODE){
_2a4.removeChild(_2a4.firstChild);
}
while(_2a4.hasChildNodes()&&_2a4.lastChild.nodeType==dojo.dom.TEXT_NODE){
_2a4.removeChild(_2a4.lastChild);
}
}
while(_2a4.hasChildNodes()){
_2a5.appendChild(_2a4.firstChild);
_2a7++;
}
return _2a7;
};
dojo.dom.copyChildren=function(_2a8,_2a9,trim){
var _2ab=_2a8.cloneNode(true);
return this.moveChildren(_2ab,_2a9,trim);
};
dojo.dom.replaceChildren=function(node,_2ad){
var _2ae=[];
if(dojo.render.html.ie){
for(var i=0;i<node.childNodes.length;i++){
_2ae.push(node.childNodes[i]);
}
}
dojo.dom.removeChildren(node);
node.appendChild(_2ad);
for(var i=0;i<_2ae.length;i++){
dojo.dom.destroyNode(_2ae[i]);
}
};
dojo.dom.removeChildren=function(node){
var _2b1=node.childNodes.length;
while(node.hasChildNodes()){
dojo.dom.removeNode(node.firstChild);
}
return _2b1;
};
dojo.dom.replaceNode=function(node,_2b3){
return node.parentNode.replaceChild(_2b3,node);
};
dojo.dom.destroyNode=function(node){
if(node.parentNode){
node=dojo.dom.removeNode(node);
}
if(node.nodeType!=3){
if(dojo.evalObjPath("dojo.event.browser.clean",false)){
dojo.event.browser.clean(node);
}
if(dojo.render.html.ie){
node.outerHTML="";
}
}
};
dojo.dom.removeNode=function(node){
if(node&&node.parentNode){
return node.parentNode.removeChild(node);
}
};
dojo.dom.getAncestors=function(node,_2b7,_2b8){
var _2b9=[];
var _2ba=(_2b7&&(_2b7 instanceof Function||typeof _2b7=="function"));
while(node){
if(!_2ba||_2b7(node)){
_2b9.push(node);
}
if(_2b8&&_2b9.length>0){
return _2b9[0];
}
node=node.parentNode;
}
if(_2b8){
return null;
}
return _2b9;
};
dojo.dom.getAncestorsByTag=function(node,tag,_2bd){
tag=tag.toLowerCase();
return dojo.dom.getAncestors(node,function(el){
return ((el.tagName)&&(el.tagName.toLowerCase()==tag));
},_2bd);
};
dojo.dom.getFirstAncestorByTag=function(node,tag){
return dojo.dom.getAncestorsByTag(node,tag,true);
};
dojo.dom.isDescendantOf=function(node,_2c2,_2c3){
if(_2c3&&node){
node=node.parentNode;
}
while(node){
if(node==_2c2){
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
var _2c6=dojo.doc();
if(!dj_undef("ActiveXObject")){
var _2c7=["MSXML2","Microsoft","MSXML","MSXML3"];
for(var i=0;i<_2c7.length;i++){
try{
doc=new ActiveXObject(_2c7[i]+".XMLDOM");
}
catch(e){
}
if(doc){
break;
}
}
}else{
if((_2c6.implementation)&&(_2c6.implementation.createDocument)){
doc=_2c6.implementation.createDocument("","",null);
}
}
return doc;
};
dojo.dom.createDocumentFromText=function(str,_2ca){
if(!_2ca){
_2ca="text/xml";
}
if(!dj_undef("DOMParser")){
var _2cb=new DOMParser();
return _2cb.parseFromString(str,_2ca);
}else{
if(!dj_undef("ActiveXObject")){
var _2cc=dojo.dom.createDocument();
if(_2cc){
_2cc.async=false;
_2cc.loadXML(str);
return _2cc;
}else{
dojo.debug("toXml didn't work?");
}
}else{
var _2cd=dojo.doc();
if(_2cd.createElement){
var tmp=_2cd.createElement("xml");
tmp.innerHTML=str;
if(_2cd.implementation&&_2cd.implementation.createDocument){
var _2cf=_2cd.implementation.createDocument("foo","",null);
for(var i=0;i<tmp.childNodes.length;i++){
_2cf.importNode(tmp.childNodes.item(i),true);
}
return _2cf;
}
return ((tmp.document)&&(tmp.document.firstChild?tmp.document.firstChild:tmp));
}
}
}
return null;
};
dojo.dom.prependChild=function(node,_2d2){
if(_2d2.firstChild){
_2d2.insertBefore(node,_2d2.firstChild);
}else{
_2d2.appendChild(node);
}
return true;
};
dojo.dom.insertBefore=function(node,ref,_2d5){
if((_2d5!=true)&&(node===ref||node.nextSibling===ref)){
return false;
}
var _2d6=ref.parentNode;
_2d6.insertBefore(node,ref);
return true;
};
dojo.dom.insertAfter=function(node,ref,_2d9){
var pn=ref.parentNode;
if(ref==pn.lastChild){
if((_2d9!=true)&&(node===ref)){
return false;
}
pn.appendChild(node);
}else{
return this.insertBefore(node,ref.nextSibling,_2d9);
}
return true;
};
dojo.dom.insertAtPosition=function(node,ref,_2dd){
if((!node)||(!ref)||(!_2dd)){
return false;
}
switch(_2dd.toLowerCase()){
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
dojo.dom.insertAtIndex=function(node,_2df,_2e0){
var _2e1=_2df.childNodes;
if(!_2e1.length||_2e1.length==_2e0){
_2df.appendChild(node);
return true;
}
if(_2e0==0){
return dojo.dom.prependChild(node,_2df);
}
return dojo.dom.insertAfter(node,_2e1[_2e0-1]);
};
dojo.dom.textContent=function(node,text){
if(arguments.length>1){
var _2e4=dojo.doc();
dojo.dom.replaceChildren(node,_2e4.createTextNode(text));
return text;
}else{
if(node.textContent!=undefined){
return node.textContent;
}
var _2e5="";
if(node==null){
return _2e5;
}
for(var i=0;i<node.childNodes.length;i++){
switch(node.childNodes[i].nodeType){
case 1:
case 5:
_2e5+=dojo.dom.textContent(node.childNodes[i]);
break;
case 3:
case 2:
case 4:
_2e5+=node.childNodes[i].nodeValue;
break;
default:
break;
}
}
return _2e5;
}
};
dojo.dom.hasParent=function(node){
return Boolean(node&&node.parentNode&&dojo.dom.isNode(node.parentNode));
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
dojo.dom.setAttributeNS=function(elem,_2eb,_2ec,_2ed){
if(elem==null||((elem==undefined)&&(typeof elem=="undefined"))){
dojo.raise("No element given to dojo.dom.setAttributeNS");
}
if(!((elem.setAttributeNS==undefined)&&(typeof elem.setAttributeNS=="undefined"))){
elem.setAttributeNS(_2eb,_2ec,_2ed);
}else{
var _2ee=elem.ownerDocument;
var _2ef=_2ee.createNode(2,_2ec,_2eb);
_2ef.nodeValue=_2ed;
elem.setAttributeNode(_2ef);
}
};
dojo.provide("dojo.undo.browser");
try{
if((!djConfig["preventBackButtonFix"])&&(!dojo.hostenv.post_load_)){
document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(djConfig["dojoIframeHistoryUrl"]||dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"'></iframe>");
}
}
catch(e){
}
if(dojo.render.html.opera){
dojo.debug("Opera is not supported with dojo.undo.browser, so back/forward detection will not work.");
}
dojo.undo.browser={initialHref:(!dj_undef("window"))?window.location.href:"",initialHash:(!dj_undef("window"))?window.location.hash:"",moveForward:false,historyStack:[],forwardStack:[],historyIframe:null,bookmarkAnchor:null,locationTimer:null,setInitialState:function(args){
this.initialState=this._createState(this.initialHref,args,this.initialHash);
},addToHistory:function(args){
this.forwardStack=[];
var hash=null;
var url=null;
if(!this.historyIframe){
if(djConfig["useXDomain"]&&!djConfig["dojoIframeHistoryUrl"]){
dojo.debug("dojo.undo.browser: When using cross-domain Dojo builds,"+" please save iframe_history.html to your domain and set djConfig.dojoIframeHistoryUrl"+" to the path on your domain to iframe_history.html");
}
this.historyIframe=window.frames["djhistory"];
}
if(!this.bookmarkAnchor){
this.bookmarkAnchor=document.createElement("a");
dojo.body().appendChild(this.bookmarkAnchor);
this.bookmarkAnchor.style.display="none";
}
if(args["changeUrl"]){
hash="#"+((args["changeUrl"]!==true)?args["changeUrl"]:(new Date()).getTime());
if(this.historyStack.length==0&&this.initialState.urlHash==hash){
this.initialState=this._createState(url,args,hash);
return;
}else{
if(this.historyStack.length>0&&this.historyStack[this.historyStack.length-1].urlHash==hash){
this.historyStack[this.historyStack.length-1]=this._createState(url,args,hash);
return;
}
}
this.changingUrl=true;
setTimeout("window.location.href = '"+hash+"'; dojo.undo.browser.changingUrl = false;",1);
this.bookmarkAnchor.href=hash;
if(dojo.render.html.ie){
url=this._loadIframeHistory();
var _2f4=args["back"]||args["backButton"]||args["handle"];
var tcb=function(_2f6){
if(window.location.hash!=""){
setTimeout("window.location.href = '"+hash+"';",1);
}
_2f4.apply(this,[_2f6]);
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
var _2f7=args["forward"]||args["forwardButton"]||args["handle"];
var tfw=function(_2f9){
if(window.location.hash!=""){
window.location.href=hash;
}
if(_2f7){
_2f7.apply(this,[_2f9]);
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
}else{
url=this._loadIframeHistory();
}
this.historyStack.push(this._createState(url,args,hash));
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
},iframeLoaded:function(evt,_2fc){
if(!dojo.render.html.opera){
var _2fd=this._getUrlQuery(_2fc.href);
if(_2fd==null){
if(this.historyStack.length==1){
this.handleBackButton();
}
return;
}
if(this.moveForward){
this.moveForward=false;
return;
}
if(this.historyStack.length>=2&&_2fd==this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){
this.handleBackButton();
}else{
if(this.forwardStack.length>0&&_2fd==this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){
this.handleForwardButton();
}
}
}
},handleBackButton:function(){
var _2fe=this.historyStack.pop();
if(!_2fe){
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
this.forwardStack.push(_2fe);
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
},_createState:function(url,args,hash){
return {"url":url,"kwArgs":args,"urlHash":hash};
},_getUrlQuery:function(url){
var _305=url.split("?");
if(_305.length<2){
return null;
}else{
return _305[1];
}
},_loadIframeHistory:function(){
var url=(djConfig["dojoIframeHistoryUrl"]||dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"?"+(new Date()).getTime();
this.moveForward=true;
dojo.io.setIFrameSrc(this.historyIframe,url,false);
return url;
}};
dojo.provide("dojo.io.BrowserIO");
if(!dj_undef("window")){
dojo.io.checkChildrenForFile=function(node){
var _308=false;
var _309=node.getElementsByTagName("input");
dojo.lang.forEach(_309,function(_30a){
if(_308){
return;
}
if(_30a.getAttribute("type")=="file"){
_308=true;
}
});
return _308;
};
dojo.io.formHasFile=function(_30b){
return dojo.io.checkChildrenForFile(_30b);
};
dojo.io.updateNode=function(node,_30d){
node=dojo.byId(node);
var args=_30d;
if(dojo.lang.isString(_30d)){
args={url:_30d};
}
args.mimetype="text/html";
args.load=function(t,d,e){
while(node.firstChild){
dojo.dom.destroyNode(node.firstChild);
}
node.innerHTML=d;
};
dojo.io.bind(args);
};
dojo.io.formFilter=function(node){
var type=(node.type||"").toLowerCase();
return !node.disabled&&node.name&&!dojo.lang.inArray(["file","submit","image","reset","button"],type);
};
dojo.io.encodeForm=function(_314,_315,_316){
if((!_314)||(!_314.tagName)||(!_314.tagName.toLowerCase()=="form")){
dojo.raise("Attempted to encode a non-form element.");
}
if(!_316){
_316=dojo.io.formFilter;
}
var enc=/utf/i.test(_315||"")?encodeURIComponent:dojo.string.encodeAscii;
var _318=[];
for(var i=0;i<_314.elements.length;i++){
var elm=_314.elements[i];
if(!elm||elm.tagName.toLowerCase()=="fieldset"||!_316(elm)){
continue;
}
var name=enc(elm.name);
var type=elm.type.toLowerCase();
if(type=="select-multiple"){
for(var j=0;j<elm.options.length;j++){
if(elm.options[j].selected){
_318.push(name+"="+enc(elm.options[j].value));
}
}
}else{
if(dojo.lang.inArray(["radio","checkbox"],type)){
if(elm.checked){
_318.push(name+"="+enc(elm.value));
}
}else{
_318.push(name+"="+enc(elm.value));
}
}
}
var _31e=_314.getElementsByTagName("input");
for(var i=0;i<_31e.length;i++){
var _31f=_31e[i];
if(_31f.type.toLowerCase()=="image"&&_31f.form==_314&&_316(_31f)){
var name=enc(_31f.name);
_318.push(name+"="+enc(_31f.value));
_318.push(name+".x=0");
_318.push(name+".y=0");
}
}
return _318.join("&")+"&";
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
if(node&&node.type&&dojo.lang.inArray(["submit","button"],node.type.toLowerCase())){
this.connect(node,"onclick","click");
}
}
var _325=form.getElementsByTagName("input");
for(var i=0;i<_325.length;i++){
var _326=_325[i];
if(_326.type.toLowerCase()=="image"&&_326.form==form){
this.connect(_326,"onclick","click");
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
var _32d=false;
if(node.disabled||!node.name){
_32d=false;
}else{
if(dojo.lang.inArray(["submit","button","image"],type)){
if(!this.clickedButton){
this.clickedButton=node;
}
_32d=node==this.clickedButton;
}else{
_32d=!dojo.lang.inArray(["file","submit","reset","button"],type);
}
}
return _32d;
},connect:function(_32e,_32f,_330){
if(dojo.evalObjPath("dojo.event.connect")){
dojo.event.connect(_32e,_32f,this,_330);
}else{
var fcn=dojo.lang.hitch(this,_330);
_32e[_32f]=function(e){
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
var _333=this;
var _334={};
this.useCache=false;
this.preventCache=false;
function getCacheKey(url,_336,_337){
return url+"|"+_336+"|"+_337.toLowerCase();
}
function addToCache(url,_339,_33a,http){
_334[getCacheKey(url,_339,_33a)]=http;
}
function getFromCache(url,_33d,_33e){
return _334[getCacheKey(url,_33d,_33e)];
}
this.clearCache=function(){
_334={};
};
function doLoad(_33f,http,url,_342,_343){
if(((http.status>=200)&&(http.status<300))||(http.status==304)||(http.status==1223)||(location.protocol=="file:"&&(http.status==0||http.status==undefined))||(location.protocol=="chrome:"&&(http.status==0||http.status==undefined))){
var ret;
if(_33f.method.toLowerCase()=="head"){
var _345=http.getAllResponseHeaders();
ret={};
ret.toString=function(){
return _345;
};
var _346=_345.split(/[\r\n]+/g);
for(var i=0;i<_346.length;i++){
var pair=_346[i].match(/^([^:]+)\s*:\s*(.+)$/i);
if(pair){
ret[pair[1]]=pair[2];
}
}
}else{
if(_33f.mimetype=="text/javascript"){
try{
ret=dj_eval(http.responseText);
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=null;
}
}else{
if(_33f.mimetype.substr(0,9)=="text/json"||_33f.mimetype.substr(0,16)=="application/json"){
try{
ret=dj_eval("("+_33f.jsonFilter(http.responseText)+")");
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=false;
}
}else{
if((_33f.mimetype=="application/xml")||(_33f.mimetype=="text/xml")){
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
if(_343){
addToCache(url,_342,_33f.method,http);
}
_33f[(typeof _33f.load=="function")?"load":"handle"]("load",ret,http,_33f);
}else{
var _349=new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);
_33f[(typeof _33f.error=="function")?"error":"handle"]("error",_349,http,_33f);
}
}
function setHeaders(http,_34b){
if(_34b["headers"]){
for(var _34c in _34b["headers"]){
if(_34c.toLowerCase()=="content-type"&&!_34b["contentType"]){
_34b["contentType"]=_34b["headers"][_34c];
}else{
http.setRequestHeader(_34c,_34b["headers"][_34c]);
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
if(!dojo.hostenv._blockAsync&&!_333._blockAsync){
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
var _350=new dojo.io.Error("XMLHttpTransport.watchInFlight Error: "+e);
tif.req[(typeof tif.req.error=="function")?"error":"handle"]("error",_350,tif.http,tif.req);
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
var _351=dojo.hostenv.getXmlhttpObject()?true:false;
this.canHandle=function(_352){
var mlc=_352["mimetype"].toLowerCase()||"";
return _351&&((dojo.lang.inArray(["text/plain","text/html","application/xml","text/xml","text/javascript"],mlc))||(mlc.substr(0,9)=="text/json"||mlc.substr(0,16)=="application/json"))&&!(_352["formNode"]&&dojo.io.formHasFile(_352["formNode"]));
};
this.multipartBoundary="45309FFF-BD65-4d50-99C9-36986896A96F";
this.bind=function(_354){
if(!_354["url"]){
if(!_354["formNode"]&&(_354["backButton"]||_354["back"]||_354["changeUrl"]||_354["watchForURL"])&&(!djConfig.preventBackButtonFix)){
dojo.deprecated("Using dojo.io.XMLHTTPTransport.bind() to add to browser history without doing an IO request","Use dojo.undo.browser.addToHistory() instead.","0.4");
dojo.undo.browser.addToHistory(_354);
return true;
}
}
var url=_354.url;
var _356="";
if(_354["formNode"]){
var ta=_354.formNode.getAttribute("action");
if((ta)&&(!_354["url"])){
url=ta;
}
var tp=_354.formNode.getAttribute("method");
if((tp)&&(!_354["method"])){
_354.method=tp;
}
_356+=dojo.io.encodeForm(_354.formNode,_354.encoding,_354["formFilter"]);
}
if(url.indexOf("#")>-1){
dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);
url=url.split("#")[0];
}
if(_354["file"]){
_354.method="post";
}
if(!_354["method"]){
_354.method="get";
}
if(_354.method.toLowerCase()=="get"){
_354.multipart=false;
}else{
if(_354["file"]){
_354.multipart=true;
}else{
if(!_354["multipart"]){
_354.multipart=false;
}
}
}
if(_354["backButton"]||_354["back"]||_354["changeUrl"]){
dojo.undo.browser.addToHistory(_354);
}
var _359=_354["content"]||{};
if(_354.sendTransport){
_359["dojo.transport"]="xmlhttp";
}
do{
if(_354.postContent){
_356=_354.postContent;
break;
}
if(_359){
_356+=dojo.io.argsFromMap(_359,_354.encoding);
}
if(_354.method.toLowerCase()=="get"||!_354.multipart){
break;
}
var t=[];
if(_356.length){
var q=_356.split("&");
for(var i=0;i<q.length;++i){
if(q[i].length){
var p=q[i].split("=");
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+p[0]+"\"","",p[1]);
}
}
}
if(_354.file){
if(dojo.lang.isArray(_354.file)){
for(var i=0;i<_354.file.length;++i){
var o=_354.file[i];
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}else{
var o=_354.file;
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}
if(t.length){
t.push("--"+this.multipartBoundary+"--","");
_356=t.join("\r\n");
}
}while(false);
var _35f=_354["sync"]?false:true;
var _360=_354["preventCache"]||(this.preventCache==true&&_354["preventCache"]!=false);
var _361=_354["useCache"]==true||(this.useCache==true&&_354["useCache"]!=false);
if(!_360&&_361){
var _362=getFromCache(url,_356,_354.method);
if(_362){
doLoad(_354,_362,url,_356,false);
return;
}
}
var http=dojo.hostenv.getXmlhttpObject(_354);
var _364=false;
if(_35f){
var _365=this.inFlight.push({"req":_354,"http":http,"url":url,"query":_356,"useCache":_361,"startTime":_354.timeoutSeconds?(new Date()).getTime():0});
this.startWatchingInFlight();
}else{
_333._blockAsync=true;
}
if(_354.method.toLowerCase()=="post"){
if(!_354.user){
http.open("POST",url,_35f);
}else{
http.open("POST",url,_35f,_354.user,_354.password);
}
setHeaders(http,_354);
http.setRequestHeader("Content-Type",_354.multipart?("multipart/form-data; boundary="+this.multipartBoundary):(_354.contentType||"application/x-www-form-urlencoded"));
try{
http.send(_356);
}
catch(e){
if(typeof http.abort=="function"){
http.abort();
}
doLoad(_354,{status:404},url,_356,_361);
}
}else{
var _366=url;
if(_356!=""){
_366+=(_366.indexOf("?")>-1?"&":"?")+_356;
}
if(_360){
_366+=(dojo.string.endsWithAny(_366,"?","&")?"":(_366.indexOf("?")>-1?"&":"?"))+"dojo.preventCache="+new Date().valueOf();
}
if(!_354.user){
http.open(_354.method.toUpperCase(),_366,_35f);
}else{
http.open(_354.method.toUpperCase(),_366,_35f,_354.user,_354.password);
}
setHeaders(http,_354);
try{
http.send(null);
}
catch(e){
if(typeof http.abort=="function"){
http.abort();
}
doLoad(_354,{status:404},url,_356,_361);
}
}
if(!_35f){
doLoad(_354,http,url,_356,_361);
_333._blockAsync=false;
}
_354.abort=function(){
try{
http._aborted=true;
}
catch(e){
}
return http.abort();
};
return;
};
dojo.io.transports.addTransport("XMLHTTPTransport");
};
}
dojo.provide("dojo.io.cookie");
dojo.io.cookie.setCookie=function(name,_368,days,path,_36b,_36c){
var _36d=-1;
if((typeof days=="number")&&(days>=0)){
var d=new Date();
d.setTime(d.getTime()+(days*24*60*60*1000));
_36d=d.toGMTString();
}
_368=escape(_368);
document.cookie=name+"="+_368+";"+(_36d!=-1?" expires="+_36d+";":"")+(path?"path="+path:"")+(_36b?"; domain="+_36b:"")+(_36c?"; secure":"");
};
dojo.io.cookie.set=dojo.io.cookie.setCookie;
dojo.io.cookie.getCookie=function(name){
var idx=document.cookie.lastIndexOf(name+"=");
if(idx==-1){
return null;
}
var _371=document.cookie.substring(idx+name.length+1);
var end=_371.indexOf(";");
if(end==-1){
end=_371.length;
}
_371=_371.substring(0,end);
_371=unescape(_371);
return _371;
};
dojo.io.cookie.get=dojo.io.cookie.getCookie;
dojo.io.cookie.deleteCookie=function(name){
dojo.io.cookie.setCookie(name,"-",0);
};
dojo.io.cookie.setObjectCookie=function(name,obj,days,path,_378,_379,_37a){
if(arguments.length==5){
_37a=_378;
_378=null;
_379=null;
}
var _37b=[],_37c,_37d="";
if(!_37a){
_37c=dojo.io.cookie.getObjectCookie(name);
}
if(days>=0){
if(!_37c){
_37c={};
}
for(var prop in obj){
if(obj[prop]==null){
delete _37c[prop];
}else{
if((typeof obj[prop]=="string")||(typeof obj[prop]=="number")){
_37c[prop]=obj[prop];
}
}
}
prop=null;
for(var prop in _37c){
_37b.push(escape(prop)+"="+escape(_37c[prop]));
}
_37d=_37b.join("&");
}
dojo.io.cookie.setCookie(name,_37d,days,path,_378,_379);
};
dojo.io.cookie.getObjectCookie=function(name){
var _380=null,_381=dojo.io.cookie.getCookie(name);
if(_381){
_380={};
var _382=_381.split("&");
for(var i=0;i<_382.length;i++){
var pair=_382[i].split("=");
var _385=pair[1];
if(isNaN(_385)){
_385=unescape(pair[1]);
}
_380[unescape(pair[0])]=_385;
}
}
return _380;
};
dojo.io.cookie.isSupported=function(){
if(typeof navigator.cookieEnabled!="boolean"){
dojo.io.cookie.setCookie("__TestingYourBrowserForCookieSupport__","CookiesAllowed",90,null);
var _386=dojo.io.cookie.getCookie("__TestingYourBrowserForCookieSupport__");
navigator.cookieEnabled=(_386=="CookiesAllowed");
if(navigator.cookieEnabled){
this.deleteCookie("__TestingYourBrowserForCookieSupport__");
}
}
return navigator.cookieEnabled;
};
if(!dojo.io.cookies){
dojo.io.cookies=dojo.io.cookie;
}
dojo.provide("dojo.lang.declare");
dojo.lang.declare=function(_387,_388,init,_38a){
if((dojo.lang.isFunction(_38a))||((!_38a)&&(!dojo.lang.isFunction(init)))){
var temp=_38a;
_38a=init;
init=temp;
}
var _38c=[];
if(dojo.lang.isArray(_388)){
_38c=_388;
_388=_38c.shift();
}
if(!init){
init=dojo.evalObjPath(_387,false);
if((init)&&(!dojo.lang.isFunction(init))){
init=null;
}
}
var ctor=dojo.lang.declare._makeConstructor();
var scp=(_388?_388.prototype:null);
if(scp){
scp.prototyping=true;
ctor.prototype=new _388();
scp.prototyping=false;
}
ctor.superclass=scp;
ctor.mixins=_38c;
for(var i=0,l=_38c.length;i<l;i++){
dojo.lang.extend(ctor,_38c[i].prototype);
}
ctor.prototype.initializer=null;
ctor.prototype.declaredClass=_387;
if(dojo.lang.isArray(_38a)){
dojo.lang.extend.apply(dojo.lang,[ctor].concat(_38a));
}else{
dojo.lang.extend(ctor,(_38a)||{});
}
dojo.lang.extend(ctor,dojo.lang.declare._common);
ctor.prototype.constructor=ctor;
ctor.prototype.initializer=(ctor.prototype.initializer)||(init)||(function(){
});
var _391=dojo.parseObjPath(_387,null,true);
_391.obj[_391.prop]=ctor;
return ctor;
};
dojo.lang.declare._makeConstructor=function(){
return function(){
var self=this._getPropContext();
var s=self.constructor.superclass;
if((s)&&(s.constructor)){
if(s.constructor==arguments.callee){
this._inherited("constructor",arguments);
}else{
this._contextMethod(s,"constructor",arguments);
}
}
var ms=(self.constructor.mixins)||([]);
for(var i=0,m;(m=ms[i]);i++){
(((m.prototype)&&(m.prototype.initializer))||(m)).apply(this,arguments);
}
if((!this.prototyping)&&(self.initializer)){
self.initializer.apply(this,arguments);
}
};
};
dojo.lang.declare._common={_getPropContext:function(){
return (this.___proto||this);
},_contextMethod:function(_397,_398,args){
var _39a,_39b=this.___proto;
this.___proto=_397;
try{
_39a=_397[_398].apply(this,(args||[]));
}
catch(e){
throw e;
}
finally{
this.___proto=_39b;
}
return _39a;
},_inherited:function(prop,args){
var p=this._getPropContext();
do{
if((!p.constructor)||(!p.constructor.superclass)){
return;
}
p=p.constructor.superclass;
}while(!(prop in p));
return (dojo.lang.isFunction(p[prop])?this._contextMethod(p,prop,args):p[prop]);
},inherited:function(prop,args){
dojo.deprecated("'inherited' method is dangerous, do not up-call! 'inherited' is slated for removal in 0.5; name your super class (or use superclass property) instead.","0.5");
this._inherited(prop,args);
}};
dojo.declare=dojo.lang.declare;
dojo.provide("dojo.html.common");
dojo.lang.mixin(dojo.html,dojo.dom);
dojo.html.body=function(){
dojo.deprecated("dojo.html.body() moved to dojo.body()","0.5");
return dojo.body();
};
dojo.html.getEventTarget=function(evt){
if(!evt){
evt=dojo.global().event||{};
}
var t=(evt.srcElement?evt.srcElement:(evt.target?evt.target:null));
while((t)&&(t.nodeType!=1)){
t=t.parentNode;
}
return t;
};
dojo.html.getViewport=function(){
var _3a3=dojo.global();
var _3a4=dojo.doc();
var w=0;
var h=0;
if(dojo.render.html.mozilla){
w=_3a4.documentElement.clientWidth;
h=_3a3.innerHeight;
}else{
if(!dojo.render.html.opera&&_3a3.innerWidth){
w=_3a3.innerWidth;
h=_3a3.innerHeight;
}else{
if(!dojo.render.html.opera&&dojo.exists(_3a4,"documentElement.clientWidth")){
var w2=_3a4.documentElement.clientWidth;
if(!w||w2&&w2<w){
w=w2;
}
h=_3a4.documentElement.clientHeight;
}else{
if(dojo.body().clientWidth){
w=dojo.body().clientWidth;
h=dojo.body().clientHeight;
}
}
}
}
return {width:w,height:h};
};
dojo.html.getScroll=function(){
var _3a8=dojo.global();
var _3a9=dojo.doc();
var top=_3a8.pageYOffset||_3a9.documentElement.scrollTop||dojo.body().scrollTop||0;
var left=_3a8.pageXOffset||_3a9.documentElement.scrollLeft||dojo.body().scrollLeft||0;
return {top:top,left:left,offset:{x:left,y:top}};
};
dojo.html.getParentByType=function(node,type){
var _3ae=dojo.doc();
var _3af=dojo.byId(node);
type=type.toLowerCase();
while((_3af)&&(_3af.nodeName.toLowerCase()!=type)){
if(_3af==(_3ae["body"]||_3ae["documentElement"])){
return null;
}
_3af=_3af.parentNode;
}
return _3af;
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
return dojo.html.getAttribute(dojo.byId(node),attr)?true:false;
};
dojo.html.getCursorPosition=function(e){
e=e||dojo.global().event;
var _3b7={x:0,y:0};
if(e.pageX||e.pageY){
_3b7.x=e.pageX;
_3b7.y=e.pageY;
}else{
var de=dojo.doc().documentElement;
var db=dojo.body();
_3b7.x=e.clientX+((de||db)["scrollLeft"])-((de||db)["clientLeft"]);
_3b7.y=e.clientY+((de||db)["scrollTop"])-((de||db)["clientTop"]);
}
return _3b7;
};
dojo.html.isTag=function(node){
node=dojo.byId(node);
if(node&&node.tagName){
for(var i=1;i<arguments.length;i++){
if(node.tagName.toLowerCase()==String(arguments[i]).toLowerCase()){
return String(arguments[i]).toLowerCase();
}
}
}
return "";
};
if(dojo.render.html.ie&&!dojo.render.html.ie70){
if(window.location.href.substr(0,6).toLowerCase()!="https:"){
(function(){
var _3bc=dojo.doc().createElement("script");
_3bc.src="javascript:'dojo.html.createExternalElement=function(doc, tag){ return doc.createElement(tag); }'";
dojo.doc().getElementsByTagName("head")[0].appendChild(_3bc);
})();
}
}else{
dojo.html.createExternalElement=function(doc,tag){
return doc.createElement(tag);
};
}
dojo.html._callDeprecated=function(_3bf,_3c0,args,_3c2,_3c3){
dojo.deprecated("dojo.html."+_3bf,"replaced by dojo.html."+_3c0+"("+(_3c2?"node, {"+_3c2+": "+_3c2+"}":"")+")"+(_3c3?"."+_3c3:""),"0.5");
var _3c4=[];
if(_3c2){
var _3c5={};
_3c5[_3c2]=args[1];
_3c4.push(args[0]);
_3c4.push(_3c5);
}else{
_3c4=args;
}
var ret=dojo.html[_3c0].apply(dojo.html,args);
if(_3c3){
return ret[_3c3];
}else{
return ret;
}
};
dojo.html.getViewportWidth=function(){
return dojo.html._callDeprecated("getViewportWidth","getViewport",arguments,null,"width");
};
dojo.html.getViewportHeight=function(){
return dojo.html._callDeprecated("getViewportHeight","getViewport",arguments,null,"height");
};
dojo.html.getViewportSize=function(){
return dojo.html._callDeprecated("getViewportSize","getViewport",arguments);
};
dojo.html.getScrollTop=function(){
return dojo.html._callDeprecated("getScrollTop","getScroll",arguments,null,"top");
};
dojo.html.getScrollLeft=function(){
return dojo.html._callDeprecated("getScrollLeft","getScroll",arguments,null,"left");
};
dojo.html.getScrollOffset=function(){
return dojo.html._callDeprecated("getScrollOffset","getScroll",arguments,null,"offset");
};
dojo.provide("dojo.uri.Uri");
dojo.uri=new function(){
var _3c7=new RegExp("^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$");
var _3c8=new RegExp("(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$");
var _3c9=new RegExp("/(\\w+.css)");
this.dojoUri=function(uri){
return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri(),uri);
};
this.moduleUri=function(_3cb,uri){
var loc=dojo.hostenv.getModuleSymbols(_3cb).join("/");
if(!loc){
return null;
}
if(loc.lastIndexOf("/")!=loc.length-1){
loc+="/";
}
var _3ce=loc.indexOf(":");
var _3cf=loc.indexOf("/");
if(loc.charAt(0)!="/"&&(_3ce==-1||_3ce>_3cf)){
loc=dojo.hostenv.getBaseScriptUri()+loc;
}
return new dojo.uri.Uri(loc,uri);
};
this.Uri=function(){
var uri=arguments[0];
if(uri&&arguments.length>1){
var _3d1=_3c9.exec(uri);
if(_3d1){
uri=uri.toString().replace(_3d1[1],"");
}
}
for(var i=1;i<arguments.length;i++){
if(!arguments[i]){
continue;
}
var _3d3=new dojo.uri.Uri(arguments[i].toString());
var _3d4=new dojo.uri.Uri(uri.toString());
if((_3d3.path=="")&&(_3d3.scheme==null)&&(_3d3.authority==null)&&(_3d3.query==null)){
if(_3d3.fragment!=null){
_3d4.fragment=_3d3.fragment;
}
_3d3=_3d4;
}
if(_3d3.scheme!=null&&_3d3.authority!=null){
uri="";
}
if(_3d3.scheme!=null){
uri+=_3d3.scheme+":";
}
if(_3d3.authority!=null){
uri+="//"+_3d3.authority;
}
uri+=_3d3.path;
if(_3d3.query!=null){
uri+="?"+_3d3.query;
}
if(_3d3.fragment!=null){
uri+="#"+_3d3.fragment;
}
}
this.uri=uri.toString();
var r=this.uri.match(_3c8);
this.scheme=r[2]||(r[1]?"":null);
this.authority=r[4]||(r[3]?"":null);
this.path=r[5];
this.query=r[7]||(r[6]?"":null);
this.fragment=r[9]||(r[8]?"":null);
if(this.authority!=null){
r=this.authority.match(_3c7);
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
dojo.provide("dojo.html.style");
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
return cs.replace(/^\s+|\s+$/g,"");
};
dojo.html.getClasses=function(node){
var c=dojo.html.getClass(node);
return (c=="")?[]:c.split(/\s+/g);
};
dojo.html.hasClass=function(node,_3db){
return (new RegExp("(^|\\s+)"+_3db+"(\\s+|$)")).test(dojo.html.getClass(node));
};
dojo.html.prependClass=function(node,_3dd){
_3dd+=" "+dojo.html.getClass(node);
return dojo.html.setClass(node,_3dd);
};
dojo.html.addClass=function(node,_3df){
if(dojo.html.hasClass(node,_3df)){
return false;
}
_3df=(dojo.html.getClass(node)+" "+_3df).replace(/^\s+|\s+$/g,"");
return dojo.html.setClass(node,_3df);
};
dojo.html.setClass=function(node,_3e1){
node=dojo.byId(node);
var cs=new String(_3e1);
try{
if(typeof node.className=="string"){
node.className=cs;
}else{
if(node.setAttribute){
node.setAttribute("class",_3e1);
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
dojo.html.removeClass=function(node,_3e4,_3e5){
try{
if(!_3e5){
var _3e6=dojo.html.getClass(node).replace(new RegExp("(^|\\s+)"+_3e4+"(\\s+|$)"),"$1$2");
}else{
var _3e6=dojo.html.getClass(node).replace(_3e4,"");
}
dojo.html.setClass(node,_3e6);
}
catch(e){
dojo.debug("dojo.html.removeClass() failed",e);
}
return true;
};
dojo.html.replaceClass=function(node,_3e8,_3e9){
dojo.html.removeClass(node,_3e9);
dojo.html.addClass(node,_3e8);
};
dojo.html.classMatchType={ContainsAll:0,ContainsAny:1,IsOnly:2};
dojo.html.getElementsByClass=function(_3ea,_3eb,_3ec,_3ed,_3ee){
_3ee=false;
var _3ef=dojo.doc();
_3eb=dojo.byId(_3eb)||_3ef;
var _3f0=_3ea.split(/\s+/g);
var _3f1=[];
if(_3ed!=1&&_3ed!=2){
_3ed=0;
}
var _3f2=new RegExp("(\\s|^)(("+_3f0.join(")|(")+"))(\\s|$)");
var _3f3=_3f0.join(" ").length;
var _3f4=[];
if(!_3ee&&_3ef.evaluate){
var _3f5=".//"+(_3ec||"*")+"[contains(";
if(_3ed!=dojo.html.classMatchType.ContainsAny){
_3f5+="concat(' ',@class,' '), ' "+_3f0.join(" ') and contains(concat(' ',@class,' '), ' ")+" ')";
if(_3ed==2){
_3f5+=" and string-length(@class)="+_3f3+"]";
}else{
_3f5+="]";
}
}else{
_3f5+="concat(' ',@class,' '), ' "+_3f0.join(" ') or contains(concat(' ',@class,' '), ' ")+" ')]";
}
var _3f6=_3ef.evaluate(_3f5,_3eb,null,XPathResult.ANY_TYPE,null);
var _3f7=_3f6.iterateNext();
while(_3f7){
try{
_3f4.push(_3f7);
_3f7=_3f6.iterateNext();
}
catch(e){
break;
}
}
return _3f4;
}else{
if(!_3ec){
_3ec="*";
}
_3f4=_3eb.getElementsByTagName(_3ec);
var node,i=0;
outer:
while(node=_3f4[i++]){
var _3fa=dojo.html.getClasses(node);
if(_3fa.length==0){
continue outer;
}
var _3fb=0;
for(var j=0;j<_3fa.length;j++){
if(_3f2.test(_3fa[j])){
if(_3ed==dojo.html.classMatchType.ContainsAny){
_3f1.push(node);
continue outer;
}else{
_3fb++;
}
}else{
if(_3ed==dojo.html.classMatchType.IsOnly){
continue outer;
}
}
}
if(_3fb==_3f0.length){
if((_3ed==dojo.html.classMatchType.IsOnly)&&(_3fb==_3fa.length)){
_3f1.push(node);
}else{
if(_3ed==dojo.html.classMatchType.ContainsAll){
_3f1.push(node);
}
}
}
}
return _3f1;
}
};
dojo.html.getElementsByClassName=dojo.html.getElementsByClass;
dojo.html.toCamelCase=function(_3fd){
var arr=_3fd.split("-"),cc=arr[0];
for(var i=1;i<arr.length;i++){
cc+=arr[i].charAt(0).toUpperCase()+arr[i].substring(1);
}
return cc;
};
dojo.html.toSelectorCase=function(_401){
return _401.replace(/([A-Z])/g,"-$1").toLowerCase();
};
if(dojo.render.html.ie){
dojo.html.getComputedStyle=function(node,_403,_404){
node=dojo.byId(node);
if(!node||!node.currentStyle){
return _404;
}
return node.currentStyle[dojo.html.toCamelCase(_403)];
};
dojo.html.getComputedStyles=function(node){
return node.currentStyle;
};
}else{
dojo.html.getComputedStyle=function(node,_407,_408){
node=dojo.byId(node);
if(!node||!node.style){
return _408;
}
var s=document.defaultView.getComputedStyle(node,null);
return (s&&s[dojo.html.toCamelCase(_407)])||"";
};
dojo.html.getComputedStyles=function(node){
return document.defaultView.getComputedStyle(node,null);
};
}
dojo.html.getStyleProperty=function(node,_40c){
node=dojo.byId(node);
return (node&&node.style?node.style[dojo.html.toCamelCase(_40c)]:undefined);
};
dojo.html.getStyle=function(node,_40e){
var _40f=dojo.html.getStyleProperty(node,_40e);
return (_40f?_40f:dojo.html.getComputedStyle(node,_40e));
};
dojo.html.setStyle=function(node,_411,_412){
node=dojo.byId(node);
if(node&&node.style){
var _413=dojo.html.toCamelCase(_411);
node.style[_413]=_412;
}
};
dojo.html.setStyleText=function(_414,text){
try{
_414.style.cssText=text;
}
catch(e){
_414.setAttribute("style",text);
}
};
dojo.html.copyStyle=function(_416,_417){
if(!_417.style.cssText){
_416.setAttribute("style",_417.getAttribute("style"));
}else{
_416.style.cssText=_417.style.cssText;
}
dojo.html.addClass(_416,dojo.html.getClass(_417));
};
dojo.html.getUnitValue=function(node,_419,_41a){
var s=dojo.html.getComputedStyle(node,_419);
if((!s)||((s=="auto")&&(_41a))){
return {value:0,units:"px"};
}
var _41c=s.match(/(\-?[\d.]+)([a-z%]*)/i);
if(!_41c){
return dojo.html.getUnitValue.bad;
}
return {value:Number(_41c[1]),units:_41c[2].toLowerCase()};
};
dojo.html.getUnitValue.bad={value:NaN,units:""};
if(dojo.render.html.ie){
dojo.html.toPixelValue=function(_41d,_41e){
if(!_41e){
return 0;
}
if(_41e.slice(-2)=="px"){
return parseFloat(_41e);
}
var _41f=0;
with(_41d){
var _420=style.left;
var _421=runtimeStyle.left;
runtimeStyle.left=currentStyle.left;
try{
style.left=_41e||0;
_41f=style.pixelLeft;
style.left=_420;
runtimeStyle.left=_421;
}
catch(e){
}
}
return _41f;
};
}else{
dojo.html.toPixelValue=function(_422,_423){
return (_423&&(_423.slice(-2)=="px")?parseFloat(_423):0);
};
}
dojo.html.getPixelValue=function(node,_425,_426){
return dojo.html.toPixelValue(node,dojo.html.getComputedStyle(node,_425));
};
dojo.html.setPositivePixelValue=function(node,_428,_429){
if(isNaN(_429)){
return false;
}
node.style[_428]=Math.max(0,_429)+"px";
return true;
};
dojo.html.styleSheet=null;
dojo.html.insertCssRule=function(_42a,_42b,_42c){
if(!dojo.html.styleSheet){
if(document.createStyleSheet){
dojo.html.styleSheet=document.createStyleSheet();
}else{
if(document.styleSheets[0]){
dojo.html.styleSheet=document.styleSheets[0];
}else{
return null;
}
}
}
if(arguments.length<3){
if(dojo.html.styleSheet.cssRules){
_42c=dojo.html.styleSheet.cssRules.length;
}else{
if(dojo.html.styleSheet.rules){
_42c=dojo.html.styleSheet.rules.length;
}else{
return null;
}
}
}
if(dojo.html.styleSheet.insertRule){
var rule=_42a+" { "+_42b+" }";
return dojo.html.styleSheet.insertRule(rule,_42c);
}else{
if(dojo.html.styleSheet.addRule){
return dojo.html.styleSheet.addRule(_42a,_42b,_42c);
}else{
return null;
}
}
};
dojo.html.removeCssRule=function(_42e){
if(!dojo.html.styleSheet){
dojo.debug("no stylesheet defined for removing rules");
return false;
}
if(dojo.render.html.ie){
if(!_42e){
_42e=dojo.html.styleSheet.rules.length;
dojo.html.styleSheet.removeRule(_42e);
}
}else{
if(document.styleSheets[0]){
if(!_42e){
_42e=dojo.html.styleSheet.cssRules.length;
}
dojo.html.styleSheet.deleteRule(_42e);
}
}
return true;
};
dojo.html._insertedCssFiles=[];
dojo.html.insertCssFile=function(URI,doc,_431,_432){
if(!URI){
return;
}
if(!doc){
doc=document;
}
var _433=dojo.hostenv.getText(URI,false,_432);
if(_433===null){
return;
}
_433=dojo.html.fixPathsInCssText(_433,URI);
if(_431){
var idx=-1,node,ent=dojo.html._insertedCssFiles;
for(var i=0;i<ent.length;i++){
if((ent[i].doc==doc)&&(ent[i].cssText==_433)){
idx=i;
node=ent[i].nodeRef;
break;
}
}
if(node){
var _438=doc.getElementsByTagName("style");
for(var i=0;i<_438.length;i++){
if(_438[i]==node){
return;
}
}
dojo.html._insertedCssFiles.shift(idx,1);
}
}
var _439=dojo.html.insertCssText(_433,doc);
dojo.html._insertedCssFiles.push({"doc":doc,"cssText":_433,"nodeRef":_439});
if(_439&&djConfig.isDebug){
_439.setAttribute("dbgHref",URI);
}
return _439;
};
dojo.html.insertCssText=function(_43a,doc,URI){
if(!_43a){
return;
}
if(!doc){
doc=document;
}
if(URI){
_43a=dojo.html.fixPathsInCssText(_43a,URI);
}
var _43d=doc.createElement("style");
_43d.setAttribute("type","text/css");
var head=doc.getElementsByTagName("head")[0];
if(!head){
dojo.debug("No head tag in document, aborting styles");
return;
}else{
head.appendChild(_43d);
}
if(_43d.styleSheet){
var _43f=function(){
try{
_43d.styleSheet.cssText=_43a;
}
catch(e){
dojo.debug(e);
}
};
if(_43d.styleSheet.disabled){
setTimeout(_43f,10);
}else{
_43f();
}
}else{
var _440=doc.createTextNode(_43a);
_43d.appendChild(_440);
}
return _43d;
};
dojo.html.fixPathsInCssText=function(_441,URI){
if(!_441||!URI){
return;
}
var _443,str="",url="",_446="[\\t\\s\\w\\(\\)\\/\\.\\\\'\"-:#=&?~]+";
var _447=new RegExp("url\\(\\s*("+_446+")\\s*\\)");
var _448=/(file|https?|ftps?):\/\//;
regexTrim=new RegExp("^[\\s]*(['\"]?)("+_446+")\\1[\\s]*?$");
if(dojo.render.html.ie55||dojo.render.html.ie60){
var _449=new RegExp("AlphaImageLoader\\((.*)src=['\"]("+_446+")['\"]");
while(_443=_449.exec(_441)){
url=_443[2].replace(regexTrim,"$2");
if(!_448.exec(url)){
url=(new dojo.uri.Uri(URI,url).toString());
}
str+=_441.substring(0,_443.index)+"AlphaImageLoader("+_443[1]+"src='"+url+"'";
_441=_441.substr(_443.index+_443[0].length);
}
_441=str+_441;
str="";
}
while(_443=_447.exec(_441)){
url=_443[1].replace(regexTrim,"$2");
if(!_448.exec(url)){
url=(new dojo.uri.Uri(URI,url).toString());
}
str+=_441.substring(0,_443.index)+"url("+url+")";
_441=_441.substr(_443.index+_443[0].length);
}
return str+_441;
};
dojo.html.setActiveStyleSheet=function(_44a){
var i=0,a,els=dojo.doc().getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")){
a.disabled=true;
if(a.getAttribute("title")==_44a){
a.disabled=false;
}
}
}
};
dojo.html.getActiveStyleSheet=function(){
var i=0,a,els=dojo.doc().getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")&&!a.disabled){
return a.getAttribute("title");
}
}
return null;
};
dojo.html.getPreferredStyleSheet=function(){
var i=0,a,els=dojo.doc().getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("rel").indexOf("alt")==-1&&a.getAttribute("title")){
return a.getAttribute("title");
}
}
return null;
};
dojo.html.applyBrowserClass=function(node){
var drh=dojo.render.html;
var _456={dj_ie:drh.ie,dj_ie55:drh.ie55,dj_ie6:drh.ie60,dj_ie7:drh.ie70,dj_iequirks:drh.ie&&drh.quirks,dj_opera:drh.opera,dj_opera8:drh.opera&&(Math.floor(dojo.render.version)==8),dj_opera9:drh.opera&&(Math.floor(dojo.render.version)==9),dj_khtml:drh.khtml,dj_safari:drh.safari,dj_gecko:drh.mozilla};
for(var p in _456){
if(_456[p]){
dojo.html.addClass(node,p);
}
}
};
dojo.kwCompoundRequire({common:["dojo.html.common","dojo.html.style"]});
dojo.provide("dojo.html.*");
dojo.provide("dojo.html.display");
dojo.html._toggle=function(node,_459,_45a){
node=dojo.byId(node);
_45a(node,!_459(node));
return _459(node);
};
dojo.html.show=function(node){
node=dojo.byId(node);
if(dojo.html.getStyleProperty(node,"display")=="none"){
dojo.html.setStyle(node,"display",(node.dojoDisplayCache||""));
node.dojoDisplayCache=undefined;
}
};
dojo.html.hide=function(node){
node=dojo.byId(node);
if(typeof node["dojoDisplayCache"]=="undefined"){
var d=dojo.html.getStyleProperty(node,"display");
if(d!="none"){
node.dojoDisplayCache=d;
}
}
dojo.html.setStyle(node,"display","none");
};
dojo.html.setShowing=function(node,_45f){
dojo.html[(_45f?"show":"hide")](node);
};
dojo.html.isShowing=function(node){
return (dojo.html.getStyleProperty(node,"display")!="none");
};
dojo.html.toggleShowing=function(node){
return dojo.html._toggle(node,dojo.html.isShowing,dojo.html.setShowing);
};
dojo.html.displayMap={tr:"",td:"",th:"",img:"inline",span:"inline",input:"inline",button:"inline"};
dojo.html.suggestDisplayByTagName=function(node){
node=dojo.byId(node);
if(node&&node.tagName){
var tag=node.tagName.toLowerCase();
return (tag in dojo.html.displayMap?dojo.html.displayMap[tag]:"block");
}
};
dojo.html.setDisplay=function(node,_465){
dojo.html.setStyle(node,"display",((_465 instanceof String||typeof _465=="string")?_465:(_465?dojo.html.suggestDisplayByTagName(node):"none")));
};
dojo.html.isDisplayed=function(node){
return (dojo.html.getComputedStyle(node,"display")!="none");
};
dojo.html.toggleDisplay=function(node){
return dojo.html._toggle(node,dojo.html.isDisplayed,dojo.html.setDisplay);
};
dojo.html.setVisibility=function(node,_469){
dojo.html.setStyle(node,"visibility",((_469 instanceof String||typeof _469=="string")?_469:(_469?"visible":"hidden")));
};
dojo.html.isVisible=function(node){
return (dojo.html.getComputedStyle(node,"visibility")!="hidden");
};
dojo.html.toggleVisibility=function(node){
return dojo.html._toggle(node,dojo.html.isVisible,dojo.html.setVisibility);
};
dojo.html.setOpacity=function(node,_46d,_46e){
node=dojo.byId(node);
var h=dojo.render.html;
if(!_46e){
if(_46d>=1){
if(h.ie){
dojo.html.clearOpacity(node);
return;
}else{
_46d=0.999999;
}
}else{
if(_46d<0){
_46d=0;
}
}
}
if(h.ie){
if(node.nodeName.toLowerCase()=="tr"){
var tds=node.getElementsByTagName("td");
for(var x=0;x<tds.length;x++){
tds[x].style.filter="Alpha(Opacity="+_46d*100+")";
}
}
node.style.filter="Alpha(Opacity="+_46d*100+")";
}else{
if(h.moz){
node.style.opacity=_46d;
node.style.MozOpacity=_46d;
}else{
if(h.safari){
node.style.opacity=_46d;
node.style.KhtmlOpacity=_46d;
}else{
node.style.opacity=_46d;
}
}
}
};
dojo.html.clearOpacity=function(node){
node=dojo.byId(node);
var ns=node.style;
var h=dojo.render.html;
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
dojo.html.getOpacity=function(node){
node=dojo.byId(node);
var h=dojo.render.html;
if(h.ie){
var opac=(node.filters&&node.filters.alpha&&typeof node.filters.alpha.opacity=="number"?node.filters.alpha.opacity:100)/100;
}else{
var opac=node.style.opacity||node.style.MozOpacity||node.style.KhtmlOpacity||1;
}
return opac>=0.999999?1:Number(opac);
};
dojo.provide("dojo.ns");
dojo.ns={namespaces:{},failed:{},loading:{},loaded:{},register:function(name,_479,_47a,_47b){
if(!_47b||!this.namespaces[name]){
this.namespaces[name]=new dojo.ns.Ns(name,_479,_47a);
}
},allow:function(name){
if(this.failed[name]){
return false;
}
if((djConfig.excludeNamespace)&&(dojo.lang.inArray(djConfig.excludeNamespace,name))){
return false;
}
return ((name==this.dojo)||(!djConfig.includeNamespace)||(dojo.lang.inArray(djConfig.includeNamespace,name)));
},get:function(name){
return this.namespaces[name];
},require:function(name){
var ns=this.namespaces[name];
if((ns)&&(this.loaded[name])){
return ns;
}
if(!this.allow(name)){
return false;
}
if(this.loading[name]){
dojo.debug("dojo.namespace.require: re-entrant request to load namespace \""+name+"\" must fail.");
return false;
}
var req=dojo.require;
this.loading[name]=true;
try{
if(name=="dojo"){
req("dojo.namespaces.dojo");
}else{
if(!dojo.hostenv.moduleHasPrefix(name)){
dojo.registerModulePath(name,"../"+name);
}
req([name,"manifest"].join("."),false,true);
}
if(!this.namespaces[name]){
this.failed[name]=true;
}
}
finally{
this.loading[name]=false;
}
return this.namespaces[name];
}};
dojo.ns.Ns=function(name,_482,_483){
this.name=name;
this.module=_482;
this.resolver=_483;
this._loaded=[];
this._failed=[];
};
dojo.ns.Ns.prototype.resolve=function(name,_485,_486){
if(!this.resolver||djConfig["skipAutoRequire"]){
return false;
}
var _487=this.resolver(name,_485);
if((_487)&&(!this._loaded[_487])&&(!this._failed[_487])){
var req=dojo.require;
req(_487,false,true);
if(dojo.hostenv.findModule(_487,false)){
this._loaded[_487]=true;
}else{
if(!_486){
dojo.raise("dojo.ns.Ns.resolve: module '"+_487+"' not found after loading via namespace '"+this.name+"'");
}
this._failed[_487]=true;
}
}
return Boolean(this._loaded[_487]);
};
dojo.registerNamespace=function(name,_48a,_48b){
dojo.ns.register.apply(dojo.ns,arguments);
};
dojo.registerNamespaceResolver=function(name,_48d){
var n=dojo.ns.namespaces[name];
if(n){
n.resolver=_48d;
}
};
dojo.registerNamespaceManifest=function(_48f,path,name,_492,_493){
dojo.registerModulePath(name,path);
dojo.registerNamespace(name,_492,_493);
};
dojo.registerNamespace("dojo","dojo.widget");
dojo.provide("dojo.event.topic");
dojo.event.topic=new function(){
this.topics={};
this.getTopic=function(_494){
if(!this.topics[_494]){
this.topics[_494]=new this.TopicImpl(_494);
}
return this.topics[_494];
};
this.registerPublisher=function(_495,obj,_497){
var _495=this.getTopic(_495);
_495.registerPublisher(obj,_497);
};
this.subscribe=function(_498,obj,_49a){
var _498=this.getTopic(_498);
_498.subscribe(obj,_49a);
};
this.unsubscribe=function(_49b,obj,_49d){
var _49b=this.getTopic(_49b);
_49b.unsubscribe(obj,_49d);
};
this.destroy=function(_49e){
this.getTopic(_49e).destroy();
delete this.topics[_49e];
};
this.publishApply=function(_49f,args){
var _49f=this.getTopic(_49f);
_49f.sendMessage.apply(_49f,args);
};
this.publish=function(_4a1,_4a2){
var _4a1=this.getTopic(_4a1);
var args=[];
for(var x=1;x<arguments.length;x++){
args.push(arguments[x]);
}
_4a1.sendMessage.apply(_4a1,args);
};
};
dojo.event.topic.TopicImpl=function(_4a5){
this.topicName=_4a5;
this.subscribe=function(_4a6,_4a7){
var tf=_4a7||_4a6;
var to=(!_4a7)?dj_global:_4a6;
return dojo.event.kwConnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
this.unsubscribe=function(_4aa,_4ab){
var tf=(!_4ab)?_4aa:_4ab;
var to=(!_4ab)?null:_4aa;
return dojo.event.kwDisconnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
this._getJoinPoint=function(){
return dojo.event.MethodJoinPoint.getForMethod(this,"sendMessage");
};
this.setSquelch=function(_4ae){
this._getJoinPoint().squelch=_4ae;
};
this.destroy=function(){
this._getJoinPoint().disconnect();
};
this.registerPublisher=function(_4af,_4b0){
dojo.event.connect(_4af,_4b0,this,"sendMessage");
};
this.sendMessage=function(_4b1){
};
};
dojo.kwCompoundRequire({common:["dojo.event.common","dojo.event.topic"],browser:["dojo.event.browser"],dashboard:["dojo.event.browser"]});
dojo.provide("dojo.event.*");
dojo.provide("dojo.widget.Manager");
dojo.widget.manager=new function(){
this.widgets=[];
this.widgetIds=[];
this.topWidgets={};
var _4b2={};
var _4b3=[];
this.getUniqueId=function(_4b4){
var _4b5;
do{
_4b5=_4b4+"_"+(_4b2[_4b4]!=undefined?++_4b2[_4b4]:_4b2[_4b4]=0);
}while(this.getWidgetById(_4b5));
return _4b5;
};
this.add=function(_4b6){
this.widgets.push(_4b6);
if(!_4b6.extraArgs["id"]){
_4b6.extraArgs["id"]=_4b6.extraArgs["ID"];
}
if(_4b6.widgetId==""){
if(_4b6["id"]){
_4b6.widgetId=_4b6["id"];
}else{
if(_4b6.extraArgs["id"]){
_4b6.widgetId=_4b6.extraArgs["id"];
}else{
_4b6.widgetId=this.getUniqueId(_4b6.ns+"_"+_4b6.widgetType);
}
}
}
if(this.widgetIds[_4b6.widgetId]){
dojo.debug("widget ID collision on ID: "+_4b6.widgetId);
}
this.widgetIds[_4b6.widgetId]=_4b6;
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
this.remove=function(_4b8){
if(dojo.lang.isNumber(_4b8)){
var tw=this.widgets[_4b8].widgetId;
delete this.topWidgets[tw];
delete this.widgetIds[tw];
this.widgets.splice(_4b8,1);
}else{
this.removeById(_4b8);
}
};
this.removeById=function(id){
if(!dojo.lang.isString(id)){
id=id["widgetId"];
if(!id){
dojo.debug("invalid widget or id passed to removeById");
return;
}
}
for(var i=0;i<this.widgets.length;i++){
if(this.widgets[i].widgetId==id){
this.remove(i);
break;
}
}
};
this.getWidgetById=function(id){
if(dojo.lang.isString(id)){
return this.widgetIds[id];
}
return id;
};
this.getWidgetsByType=function(type){
var lt=type.toLowerCase();
var _4bf=(type.indexOf(":")<0?function(x){
return x.widgetType.toLowerCase();
}:function(x){
return x.getNamespacedType();
});
var ret=[];
dojo.lang.forEach(this.widgets,function(x){
if(_4bf(x)==lt){
ret.push(x);
}
});
return ret;
};
this.getWidgetsByFilter=function(_4c4,_4c5){
var ret=[];
dojo.lang.every(this.widgets,function(x){
if(_4c4(x)){
ret.push(x);
if(_4c5){
return false;
}
}
return true;
});
return (_4c5?ret[0]:ret);
};
this.getAllWidgets=function(){
return this.widgets.concat();
};
this.getWidgetByNode=function(node){
var w=this.getAllWidgets();
node=dojo.byId(node);
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
var _4cb={};
var _4cc=["dojo.widget"];
for(var i=0;i<_4cc.length;i++){
_4cc[_4cc[i]]=true;
}
this.registerWidgetPackage=function(_4ce){
if(!_4cc[_4ce]){
_4cc[_4ce]=true;
_4cc.push(_4ce);
}
};
this.getWidgetPackageList=function(){
return dojo.lang.map(_4cc,function(elt){
return (elt!==true?elt:undefined);
});
};
this.getImplementation=function(_4d0,_4d1,_4d2,ns){
var impl=this.getImplementationName(_4d0,ns);
if(impl){
var ret=_4d1?new impl(_4d1):new impl();
return ret;
}
};
function buildPrefixCache(){
for(var _4d6 in dojo.render){
if(dojo.render[_4d6]["capable"]===true){
var _4d7=dojo.render[_4d6].prefixes;
for(var i=0;i<_4d7.length;i++){
_4b3.push(_4d7[i].toLowerCase());
}
}
}
}
var _4d9=function(_4da,_4db){
if(!_4db){
return null;
}
for(var i=0,l=_4b3.length,_4de;i<=l;i++){
_4de=(i<l?_4db[_4b3[i]]:_4db);
if(!_4de){
continue;
}
for(var name in _4de){
if(name.toLowerCase()==_4da){
return _4de[name];
}
}
}
return null;
};
var _4e0=function(_4e1,_4e2){
var _4e3=dojo.evalObjPath(_4e2,false);
return (_4e3?_4d9(_4e1,_4e3):null);
};
this.getImplementationName=function(_4e4,ns){
var _4e6=_4e4.toLowerCase();
ns=ns||"dojo";
var imps=_4cb[ns]||(_4cb[ns]={});
var impl=imps[_4e6];
if(impl){
return impl;
}
if(!_4b3.length){
buildPrefixCache();
}
var _4e9=dojo.ns.get(ns);
if(!_4e9){
dojo.ns.register(ns,ns+".widget");
_4e9=dojo.ns.get(ns);
}
if(_4e9){
_4e9.resolve(_4e4);
}
impl=_4e0(_4e6,_4e9.module);
if(impl){
return (imps[_4e6]=impl);
}
_4e9=dojo.ns.require(ns);
if((_4e9)&&(_4e9.resolver)){
_4e9.resolve(_4e4);
impl=_4e0(_4e6,_4e9.module);
if(impl){
return (imps[_4e6]=impl);
}
}
dojo.deprecated("dojo.widget.Manager.getImplementationName","Could not locate widget implementation for \""+_4e4+"\" in \""+_4e9.module+"\" registered to namespace \""+_4e9.name+"\". "+"Developers must specify correct namespaces for all non-Dojo widgets","0.5");
for(var i=0;i<_4cc.length;i++){
impl=_4e0(_4e6,_4cc[i]);
if(impl){
return (imps[_4e6]=impl);
}
}
throw new Error("Could not locate widget implementation for \""+_4e4+"\" in \""+_4e9.module+"\" registered to namespace \""+_4e9.name+"\"");
};
this.resizing=false;
this.onWindowResized=function(){
if(this.resizing){
return;
}
try{
this.resizing=true;
for(var id in this.topWidgets){
var _4ec=this.topWidgets[id];
if(_4ec.checkSize){
_4ec.checkSize();
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
var g=function(_4f1,_4f2){
dw[(_4f2||_4f1)]=h(_4f1);
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
var _4f4=dwm.getAllWidgets.apply(dwm,arguments);
if(arguments.length>0){
return _4f4[n];
}
return _4f4;
};
g("registerWidgetPackage");
g("getImplementation","getWidgetImplementation");
g("getImplementationName","getWidgetImplementationName");
dw.widgets=dwm.widgets;
dw.widgetIds=dwm.widgetIds;
dw.root=dwm.root;
})();
dojo.provide("dojo.i18n.common");
dojo.i18n.getLocalization=function(_4f5,_4f6,_4f7){
dojo.hostenv.preloadLocalizations();
_4f7=dojo.hostenv.normalizeLocale(_4f7);
var _4f8=_4f7.split("-");
var _4f9=[_4f5,"nls",_4f6].join(".");
var _4fa=dojo.hostenv.findModule(_4f9,true);
var _4fb;
for(var i=_4f8.length;i>0;i--){
var loc=_4f8.slice(0,i).join("_");
if(_4fa[loc]){
_4fb=_4fa[loc];
break;
}
}
if(!_4fb){
_4fb=_4fa.ROOT;
}
if(_4fb){
var _4fe=function(){
};
_4fe.prototype=_4fb;
return new _4fe();
}
dojo.raise("Bundle not found: "+_4f6+" in "+_4f5+" , locale="+_4f7);
};
dojo.i18n.isLTR=function(_4ff){
var lang=dojo.hostenv.normalizeLocale(_4ff).split("-")[0];
var RTL={ar:true,fa:true,he:true,ur:true,yi:true};
return !RTL[lang];
};
dojo.provide("dojo.tlocale");

