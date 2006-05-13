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
dojo.version={major:0,minor:2,patch:2,flag:"+",revision:Number("$Rev: 3802 $".match(/[0-9]+/)[0]),toString:function(){
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
},getTextStack:[],loadUriStack:[],loadedUris:[],post_load_:false,modulesLoadedListeners:[]};
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
return;
}
var _33=this.getText(uri,null,true);
if(_33==null){
return 0;
}
this.loadedUris[uri]=true;
var _34=dj_eval(_33);
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
this.post_load_=true;
var mll=this.modulesLoadedListeners;
this.modulesLoadedListeners=[];
for(var x=0;x<mll.length;x++){
mll[x]();
}
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
if(dh.post_load_&&dh.inFlightCount==0){
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
dojo.hostenv._global_omit_module_check=false;
dojo.hostenv.loadModule=function(_3e,_3f,_40){
if(!_3e){
return;
}
_40=this._global_omit_module_check||_40;
var _41=this.findModule(_3e,false);
if(_41){
return _41;
}
if(dj_undef(_3e,this.loading_modules_)){
this.addedToLoadingCount.push(_3e);
}
this.loading_modules_[_3e]=1;
var _42=_3e.replace(/\./g,"/")+".js";
var _43=_3e.split(".");
var _44=_3e.split(".");
for(var i=_43.length-1;i>0;i--){
var _46=_43.slice(0,i).join(".");
var _47=this.getModulePrefix(_46);
if(_47!=_46){
_43.splice(0,i,_47);
break;
}
}
var _48=_43[_43.length-1];
if(_48=="*"){
_3e=(_44.slice(0,-1)).join(".");
while(_43.length){
_43.pop();
_43.push(this.pkgFileName);
_42=_43.join("/")+".js";
if(_42.charAt(0)=="/"){
_42=_42.slice(1);
}
ok=this.loadPath(_42,((!_40)?_3e:null));
if(ok){
break;
}
_43.pop();
}
}else{
_42=_43.join("/")+".js";
_3e=_44.join(".");
var ok=this.loadPath(_42,((!_40)?_3e:null));
if((!ok)&&(!_3f)){
_43.pop();
while(_43.length){
_42=_43.join("/")+".js";
ok=this.loadPath(_42,((!_40)?_3e:null));
if(ok){
break;
}
_43.pop();
_42=_43.join("/")+"/"+this.pkgFileName+".js";
if(_42.charAt(0)=="/"){
_42=_42.slice(1);
}
ok=this.loadPath(_42,((!_40)?_3e:null));
if(ok){
break;
}
}
}
if((!ok)&&(!_40)){
dojo.raise("Could not load '"+_3e+"'; last tried '"+_42+"'");
}
}
if(!_40&&!this["isXDomain"]){
_41=this.findModule(_3e,false);
if(!_41){
dojo.raise("symbol '"+_3e+"' is not defined after loading '"+_42+"'");
}
}
return _41;
};
dojo.hostenv.startPackage=function(_4a){
var _4b=dojo.evalObjPath((_4a.split(".").slice(0,-1)).join("."));
this.loaded_modules_[(new String(_4a)).toLowerCase()]=_4b;
var _4c=_4a.split(/\./);
if(_4c[_4c.length-1]=="*"){
_4c.pop();
}
return dojo.evalObjPath(_4c.join("."),true);
};
dojo.hostenv.findModule=function(_4d,_4e){
var lmn=(new String(_4d)).toLowerCase();
if(this.loaded_modules_[lmn]){
return this.loaded_modules_[lmn];
}
var _50=dojo.evalObjPath(_4d);
if((_4d)&&(typeof _50!="undefined")&&(_50)){
this.loaded_modules_[lmn]=_50;
return _50;
}
if(_4e){
dojo.raise("no loaded module named '"+_4d+"'");
}
return null;
};
dojo.kwCompoundRequire=function(_51){
var _52=_51["common"]||[];
var _53=(_51[dojo.hostenv.name_])?_52.concat(_51[dojo.hostenv.name_]||[]):_52.concat(_51["default"]||[]);
for(var x=0;x<_53.length;x++){
var _55=_53[x];
if(_55.constructor==Array){
dojo.hostenv.loadModule.apply(dojo.hostenv,_55);
}else{
dojo.hostenv.loadModule(_55);
}
}
};
dojo.require=function(){
dojo.hostenv.loadModule.apply(dojo.hostenv,arguments);
};
dojo.requireIf=function(){
if((arguments[0]===true)||(arguments[0]=="common")||(arguments[0]&&dojo.render[arguments[0]].capable)){
var _56=[];
for(var i=1;i<arguments.length;i++){
_56.push(arguments[i]);
}
dojo.require.apply(dojo,_56);
}
};
dojo.requireAfterIf=dojo.requireIf;
dojo.provide=function(){
return dojo.hostenv.startPackage.apply(dojo.hostenv,arguments);
};
dojo.setModulePrefix=function(_58,_59){
return dojo.hostenv.setModulePrefix(_58,_59);
};
dojo.exists=function(obj,_5b){
var p=_5b.split(".");
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
var _5e=document.location.toString();
var _5f=_5e.split("?",2);
if(_5f.length>1){
var _60=_5f[1];
var _61=_60.split("&");
for(var x in _61){
var sp=_61[x].split("=");
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
var _65=document.getElementsByTagName("script");
var _66=/(__package__|dojo|bootstrap1)\.js([\?\.]|$)/i;
for(var i=0;i<_65.length;i++){
var src=_65[i].getAttribute("src");
if(!src){
continue;
}
var m=src.match(_66);
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
var _71=dua.indexOf("Gecko");
drh.mozilla=drh.moz=(_71>=0)&&(!drh.khtml);
if(drh.mozilla){
drh.geckoVersion=dua.substring(_71+6,_71+14);
}
drh.ie=(document.all)&&(!drh.opera);
drh.ie50=drh.ie&&dav.indexOf("MSIE 5.0")>=0;
drh.ie55=drh.ie&&dav.indexOf("MSIE 5.5")>=0;
drh.ie60=drh.ie&&dav.indexOf("MSIE 6.0")>=0;
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
var _72=null;
var _73=null;
try{
_72=new XMLHttpRequest();
}
catch(e){
}
if(!_72){
for(var i=0;i<3;++i){
var _75=DJ_XMLHTTP_PROGIDS[i];
try{
_72=new ActiveXObject(_75);
}
catch(e){
_73=e;
}
if(_72){
DJ_XMLHTTP_PROGIDS=[_75];
break;
}
}
}
if(!_72){
return dojo.raise("XMLHTTP not available",_73);
}
return _72;
};
dojo.hostenv.getText=function(uri,_77,_78){
var _79=this.getXmlhttpObject();
if(_77){
_79.onreadystatechange=function(){
if((4==_79.readyState)&&(_79["status"])){
if(_79.status==200){
_77(_79.responseText);
}
}
};
}
_79.open("GET",uri,_77?true:false);
try{
_79.send(null);
}
catch(e){
if(_78&&!_77){
return null;
}else{
throw e;
}
}
if(_77){
return null;
}
return _79.responseText;
};
dojo.hostenv.defaultDebugContainerId="dojoDebug";
dojo.hostenv._println_buffer=[];
dojo.hostenv._println_safe=false;
dojo.hostenv.println=function(_7a){
if(!dojo.hostenv._println_safe){
dojo.hostenv._println_buffer.push(_7a);
}else{
try{
var _7b=document.getElementById(djConfig.debugContainerId?djConfig.debugContainerId:dojo.hostenv.defaultDebugContainerId);
if(!_7b){
_7b=document.getElementsByTagName("body")[0]||document.body;
}
var div=document.createElement("div");
div.appendChild(document.createTextNode(_7a));
_7b.appendChild(div);
}
catch(e){
try{
document.write("<div>"+_7a+"</div>");
}
catch(e2){
window.status=_7a;
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
function dj_addNodeEvtHdlr(_7d,_7e,fp,_80){
var _81=_7d["on"+_7e]||function(){
};
_7d["on"+_7e]=function(){
fp.apply(_7d,arguments);
_81.apply(_7d,arguments);
};
return true;
}
dj_addNodeEvtHdlr(window,"load",function(){
if(arguments.callee.initialized){
return;
}
arguments.callee.initialized=true;
var _82=function(){
if(dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
};
if(dojo.hostenv.inFlightCount==0){
_82();
dojo.hostenv.modulesLoaded();
}else{
dojo.addOnLoad(_82);
}
});
dojo.hostenv.makeWidgets=function(){
var _83=[];
if(djConfig.searchIds&&djConfig.searchIds.length>0){
_83=_83.concat(djConfig.searchIds);
}
if(dojo.hostenv.searchIds&&dojo.hostenv.searchIds.length>0){
_83=_83.concat(dojo.hostenv.searchIds);
}
if((djConfig.parseWidgets)||(_83.length>0)){
if(dojo.evalObjPath("dojo.widget.Parse")){
try{
var _84=new dojo.xml.Parse();
if(_83.length>0){
for(var x=0;x<_83.length;x++){
var _86=document.getElementById(_83[x]);
if(!_86){
continue;
}
var _87=_84.parseElement(_86,null,true);
dojo.widget.getParser().createComponents(_87);
}
}else{
if(djConfig.parseWidgets){
var _87=_84.parseElement(document.getElementsByTagName("body")[0]||document.body,null,true);
dojo.widget.getParser().createComponents(_87);
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
document.namespaces.add("v","urn:schemas-microsoft-com:vml");
document.createStyleSheet().addRule("v\\:*","behavior:url(#default#VML)");
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
var _8a=false;
var _8b=false;
var _8c=false;
if((typeof this["load"]=="function")&&(typeof this["Packages"]=="function")){
_8a=true;
}else{
if(typeof this["load"]=="function"){
_8b=true;
}else{
if(window.widget){
_8c=true;
}
}
}
var _8d=[];
if((this["djConfig"])&&((djConfig["isDebug"])||(djConfig["debugAtAllCosts"]))){
_8d.push("debug.js");
}
if((this["djConfig"])&&(djConfig["debugAtAllCosts"])&&(!_8a)&&(!_8c)){
_8d.push("browser_debug.js");
}
if((this["djConfig"])&&(djConfig["compat"])){
_8d.push("compat/"+djConfig["compat"]+".js");
}
var _8e=djConfig["baseScriptUri"];
if((this["djConfig"])&&(djConfig["baseLoaderUri"])){
_8e=djConfig["baseLoaderUri"];
}
for(var x=0;x<_8d.length;x++){
var _90=_8e+"src/"+_8d[x];
if(_8a||_8b){
load(_90);
}else{
try{
document.write("<scr"+"ipt type='text/javascript' src='"+_90+"'></scr"+"ipt>");
}
catch(e){
var _91=document.createElement("script");
_91.src=_90;
document.getElementsByTagName("head")[0].appendChild(_91);
}
}
}
})();
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
dojo.string.repeat=function(str,_98,_99){
var out="";
for(var i=0;i<_98;i++){
out+=str;
if(_99&&i<_98-1){
out+=_99;
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
dojo.lang.mixin=function(obj,_a8){
var _a9={};
for(var x in _a8){
if(typeof _a9[x]=="undefined"||_a9[x]!=_a8[x]){
obj[x]=_a8[x];
}
}
if(dojo.render.html.ie&&dojo.lang.isFunction(_a8["toString"])&&_a8["toString"]!=obj["toString"]){
obj.toString=_a8.toString;
}
return obj;
};
dojo.lang.extend=function(_ab,_ac){
this.mixin(_ab.prototype,_ac);
};
dojo.lang.find=function(arr,val,_af,_b0){
if(!dojo.lang.isArrayLike(arr)&&dojo.lang.isArrayLike(val)){
var a=arr;
arr=val;
val=a;
}
var _b2=dojo.lang.isString(arr);
if(_b2){
arr=arr.split("");
}
if(_b0){
var _b3=-1;
var i=arr.length-1;
var end=-1;
}else{
var _b3=1;
var i=0;
var end=arr.length;
}
if(_af){
while(i!=end){
if(arr[i]===val){
return i;
}
i+=_b3;
}
}else{
while(i!=end){
if(arr[i]==val){
return i;
}
i+=_b3;
}
}
return -1;
};
dojo.lang.indexOf=dojo.lang.find;
dojo.lang.findLast=function(arr,val,_b8){
return dojo.lang.find(arr,val,_b8,true);
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
dojo.provide("dojo.lang.extras");
dojo.require("dojo.lang.common");
dojo.lang.setTimeout=function(_c4,_c5){
var _c6=window,argsStart=2;
if(!dojo.lang.isFunction(_c4)){
_c6=_c4;
_c4=_c5;
_c5=arguments[2];
argsStart++;
}
if(dojo.lang.isString(_c4)){
_c4=_c6[_c4];
}
var _c7=[];
for(var i=argsStart;i<arguments.length;i++){
_c7.push(arguments[i]);
}
return setTimeout(function(){
_c4.apply(_c6,_c7);
},_c5);
};
dojo.lang.getNameInObj=function(ns,_ca){
if(!ns){
ns=dj_global;
}
for(var x in ns){
if(ns[x]===_ca){
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
dojo.lang.getObjPathValue=function(_cf,_d0,_d1){
with(dojo.parseObjPath(_cf,_d0,_d1)){
return dojo.evalProp(prop,obj,_d1);
}
};
dojo.lang.setObjPathValue=function(_d2,_d3,_d4,_d5){
if(arguments.length<4){
_d5=true;
}
with(dojo.parseObjPath(_d2,_d4,_d5)){
if(obj&&(_d5||(prop in obj))){
obj[prop]=_d3;
}
}
};
dojo.provide("dojo.io.IO");
dojo.require("dojo.string");
dojo.require("dojo.lang.extras");
dojo.io.transports=[];
dojo.io.hdlrFuncNames=["load","error","timeout"];
dojo.io.Request=function(url,_d7,_d8,_d9){
if((arguments.length==1)&&(arguments[0].constructor==Object)){
this.fromKwArgs(arguments[0]);
}else{
this.url=url;
if(_d7){
this.mimetype=_d7;
}
if(_d8){
this.transport=_d8;
}
if(arguments.length>=4){
this.changeUrl=_d9;
}
}
};
dojo.lang.extend(dojo.io.Request,{url:"",mimetype:"text/plain",method:"GET",content:undefined,transport:undefined,changeUrl:undefined,formNode:undefined,sync:false,bindSuccess:false,useCache:false,preventCache:false,load:function(_da,_db,evt){
},error:function(_dd,_de){
},timeout:function(_df){
},handle:function(){
},timeoutSeconds:0,abort:function(){
},fromKwArgs:function(_e0){
if(_e0["url"]){
_e0.url=_e0.url.toString();
}
if(_e0["formNode"]){
_e0.formNode=dojo.byId(_e0.formNode);
}
if(!_e0["method"]&&_e0["formNode"]&&_e0["formNode"].method){
_e0.method=_e0["formNode"].method;
}
if(!_e0["handle"]&&_e0["handler"]){
_e0.handle=_e0.handler;
}
if(!_e0["load"]&&_e0["loaded"]){
_e0.load=_e0.loaded;
}
if(!_e0["changeUrl"]&&_e0["changeURL"]){
_e0.changeUrl=_e0.changeURL;
}
_e0.encoding=dojo.lang.firstValued(_e0["encoding"],djConfig["bindEncoding"],"");
_e0.sendTransport=dojo.lang.firstValued(_e0["sendTransport"],djConfig["ioSendTransport"],false);
var _e1=dojo.lang.isFunction;
for(var x=0;x<dojo.io.hdlrFuncNames.length;x++){
var fn=dojo.io.hdlrFuncNames[x];
if(_e1(_e0[fn])){
continue;
}
if(_e1(_e0["handle"])){
_e0[fn]=_e0.handle;
}
}
dojo.lang.mixin(this,_e0);
}});
dojo.io.Error=function(msg,_e5,num){
this.message=msg;
this.type=_e5||"unknown";
this.number=num||0;
};
dojo.io.transports.addTransport=function(_e7){
this.push(_e7);
this[_e7]=dojo.io[_e7];
};
dojo.io.bind=function(_e8){
if(!(_e8 instanceof dojo.io.Request)){
try{
_e8=new dojo.io.Request(_e8);
}
catch(e){
dojo.debug(e);
}
}
var _e9="";
if(_e8["transport"]){
_e9=_e8["transport"];
if(!this[_e9]){
return _e8;
}
}else{
for(var x=0;x<dojo.io.transports.length;x++){
var tmp=dojo.io.transports[x];
if((this[tmp])&&(this[tmp].canHandle(_e8))){
_e9=tmp;
}
}
if(_e9==""){
return _e8;
}
}
this[_e9].bind(_e8);
_e8.bindSuccess=true;
return _e8;
};
dojo.io.queueBind=function(_ec){
if(!(_ec instanceof dojo.io.Request)){
try{
_ec=new dojo.io.Request(_ec);
}
catch(e){
dojo.debug(e);
}
}
var _ed=_ec.load;
_ec.load=function(){
dojo.io._queueBindInFlight=false;
var ret=_ed.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
var _ef=_ec.error;
_ec.error=function(){
dojo.io._queueBindInFlight=false;
var ret=_ef.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
dojo.io._bindQueue.push(_ec);
dojo.io._dispatchNextQueueBind();
return _ec;
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
dojo.io.argsFromMap=function(map,_f2,_f3){
var enc=/utf/i.test(_f2||"")?encodeURIComponent:dojo.string.encodeAscii;
var _f5=[];
var _f6=new Object();
for(var _f7 in map){
var _f8=function(elt){
var val=enc(_f7)+"="+enc(elt);
_f5[(_f3==_f7)?"push":"unshift"](val);
};
if(!_f6[_f7]){
var _fb=map[_f7];
if(dojo.lang.isArray(_fb)){
dojo.lang.forEach(_fb,_f8);
}else{
_f8(_fb);
}
}
}
return _f5.join("&");
};
dojo.io.setIFrameSrc=function(_fc,src,_fe){
try{
var r=dojo.render.html;
if(!_fe){
if(r.safari){
_fc.location=src;
}else{
frames[_fc.name].location=src;
}
}else{
var idoc;
if(r.ie){
idoc=_fc.contentWindow.document;
}else{
if(r.safari){
idoc=_fc.document;
}else{
idoc=_fc.contentWindow;
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
var _105=0;
for(var x in obj){
if(obj[x]&&(!tmp[x])){
_105++;
break;
}
}
return (_105==0);
}else{
if(dojo.lang.isArrayLike(obj)||dojo.lang.isString(obj)){
return obj.length==0;
}
}
};
dojo.lang.map=function(arr,obj,_109){
var _10a=dojo.lang.isString(arr);
if(_10a){
arr=arr.split("");
}
if(dojo.lang.isFunction(obj)&&(!_109)){
_109=obj;
obj=dj_global;
}else{
if(dojo.lang.isFunction(obj)&&_109){
var _10b=obj;
obj=_109;
_109=_10b;
}
}
if(Array.map){
var _10c=Array.map(arr,_109,obj);
}else{
var _10c=[];
for(var i=0;i<arr.length;++i){
_10c.push(_109.call(obj,arr[i]));
}
}
if(_10a){
return _10c.join("");
}else{
return _10c;
}
};
dojo.lang.forEach=function(_10e,_10f,_110){
if(dojo.lang.isString(_10e)){
_10e=_10e.split("");
}
if(Array.forEach){
Array.forEach(_10e,_10f,_110);
}else{
if(!_110){
_110=dj_global;
}
for(var i=0,l=_10e.length;i<l;i++){
_10f.call(_110,_10e[i],i,_10e);
}
}
};
dojo.lang._everyOrSome=function(_112,arr,_114,_115){
if(dojo.lang.isString(arr)){
arr=arr.split("");
}
if(Array.every){
return Array[(_112)?"every":"some"](arr,_114,_115);
}else{
if(!_115){
_115=dj_global;
}
for(var i=0,l=arr.length;i<l;i++){
var _117=_114.call(_115,arr[i],i,arr);
if((_112)&&(!_117)){
return false;
}else{
if((!_112)&&(_117)){
return true;
}
}
}
return (_112)?true:false;
}
};
dojo.lang.every=function(arr,_119,_11a){
return this._everyOrSome(true,arr,_119,_11a);
};
dojo.lang.some=function(arr,_11c,_11d){
return this._everyOrSome(false,arr,_11c,_11d);
};
dojo.lang.filter=function(arr,_11f,_120){
var _121=dojo.lang.isString(arr);
if(_121){
arr=arr.split("");
}
if(Array.filter){
var _122=Array.filter(arr,_11f,_120);
}else{
if(!_120){
if(arguments.length>=3){
dojo.raise("thisObject doesn't exist!");
}
_120=dj_global;
}
var _122=[];
for(var i=0;i<arr.length;i++){
if(_11f.call(_120,arr[i],i,arr)){
_122.push(arr[i]);
}
}
}
if(_121){
return _122.join("");
}else{
return _122;
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
dojo.lang.toArray=function(_127,_128){
var _129=[];
for(var i=_128||0;i<_127.length;i++){
_129.push(_127[i]);
}
return _129;
};
dojo.provide("dojo.lang.func");
dojo.require("dojo.lang.common");
dojo.lang.hitch=function(_12b,_12c){
if(dojo.lang.isString(_12c)){
var fcn=_12b[_12c];
}else{
var fcn=_12c;
}
return function(){
return fcn.apply(_12b,arguments);
};
};
dojo.lang.anonCtr=0;
dojo.lang.anon={};
dojo.lang.nameAnonFunc=function(_12e,_12f){
var nso=(_12f||dojo.lang.anon);
if((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true)){
for(var x in nso){
if(nso[x]===_12e){
return x;
}
}
}
var ret="__"+dojo.lang.anonCtr++;
while(typeof nso[ret]!="undefined"){
ret="__"+dojo.lang.anonCtr++;
}
nso[ret]=_12e;
return ret;
};
dojo.lang.forward=function(_133){
return function(){
return this[_133].apply(this,arguments);
};
};
dojo.lang.curry=function(ns,func){
var _136=[];
ns=ns||dj_global;
if(dojo.lang.isString(func)){
func=ns[func];
}
for(var x=2;x<arguments.length;x++){
_136.push(arguments[x]);
}
var _138=(func["__preJoinArity"]||func.length)-_136.length;
function gather(_139,_13a,_13b){
var _13c=_13b;
var _13d=_13a.slice(0);
for(var x=0;x<_139.length;x++){
_13d.push(_139[x]);
}
_13b=_13b-_139.length;
if(_13b<=0){
var res=func.apply(ns,_13d);
_13b=_13c;
return res;
}else{
return function(){
return gather(arguments,_13d,_13b);
};
}
}
return gather([],_136,_138);
};
dojo.lang.curryArguments=function(ns,func,args,_143){
var _144=[];
var x=_143||0;
for(x=_143;x<args.length;x++){
_144.push(args[x]);
}
return dojo.lang.curry.apply(dojo.lang,[ns,func].concat(_144));
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
dojo.lang.delayThese=function(farr,cb,_14a,_14b){
if(!farr.length){
if(typeof _14b=="function"){
_14b();
}
return;
}
if((typeof _14a=="undefined")&&(typeof cb=="number")){
_14a=cb;
cb=function(){
};
}else{
if(!cb){
cb=function(){
};
if(!_14a){
_14a=0;
}
}
}
setTimeout(function(){
(farr.shift())();
cb();
dojo.lang.delayThese(farr,cb,_14a,_14b);
},_14a);
};
dojo.provide("dojo.string.extras");
dojo.require("dojo.string.common");
dojo.require("dojo.lang");
dojo.string.paramString=function(str,_14d,_14e){
for(var name in _14d){
var re=new RegExp("\\%\\{"+name+"\\}","g");
str=str.replace(re,_14d[name]);
}
if(_14e){
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
var _152=str.split(" ");
var _153="";
var len=_152.length;
for(var i=0;i<len;i++){
var word=_152[i];
word=word.charAt(0).toUpperCase()+word.substring(1,word.length);
_153+=word;
if(i<len-1){
_153+=" ";
}
}
return new String(_153);
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
var _15a=escape(str);
var _15b,re=/%u([0-9A-F]{4})/i;
while((_15b=_15a.match(re))){
var num=Number("0x"+_15b[1]);
var _15d=escape("&#"+num+";");
ret+=_15a.substring(0,_15b.index)+_15d;
_15a=_15a.substring(_15b.index+_15b[0].length);
}
ret+=_15a.replace(/\+/g,"%2B");
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
dojo.string.escapeXml=function(str,_163){
str=str.replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");
if(!_163){
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
dojo.string.endsWith=function(str,end,_16c){
if(_16c){
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
dojo.string.startsWith=function(str,_170,_171){
if(_171){
str=str.toLowerCase();
_170=_170.toLowerCase();
}
return str.indexOf(_170)==0;
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
dojo.string.normalizeNewlines=function(text,_177){
if(_177=="\n"){
text=text.replace(/\r\n/g,"\n");
text=text.replace(/\r/g,"\n");
}else{
if(_177=="\r"){
text=text.replace(/\r\n/g,"\r");
text=text.replace(/\n/g,"\r");
}else{
text=text.replace(/([^\r])\n/g,"$1\r\n");
text=text.replace(/\r([^\n])/g,"\r\n$1");
}
}
return text;
};
dojo.string.splitEscaped=function(str,_179){
var _17a=[];
for(var i=0,prevcomma=0;i<str.length;i++){
if(str.charAt(i)=="\\"){
i++;
continue;
}
if(str.charAt(i)==_179){
_17a.push(str.substring(prevcomma,i));
prevcomma=i+1;
}
}
_17a.push(str.substr(prevcomma));
return _17a;
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
var _17e=node.tagName;
if(_17e.substr(0,5).toLowerCase()!="dojo:"){
if(_17e.substr(0,4).toLowerCase()=="dojo"){
return "dojo:"+_17e.substring(4).toLowerCase();
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
var _180=node.className||node.getAttribute("class");
if((_180)&&(_180.indexOf)&&(_180.indexOf("dojo-")!=-1)){
var _181=_180.split(" ");
for(var x=0;x<_181.length;x++){
if((_181[x].length>5)&&(_181[x].indexOf("dojo-")>=0)){
return "dojo:"+_181[x].substr(5).toLowerCase();
}
}
}
}
}
return _17e.toLowerCase();
};
dojo.dom.getUniqueId=function(){
do{
var id="dj_unique_"+(++arguments.callee._idIncrement);
}while(document.getElementById(id));
return id;
};
dojo.dom.getUniqueId._idIncrement=0;
dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_184,_185){
var node=_184.firstChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.nextSibling;
}
if(_185&&node&&node.tagName&&node.tagName.toLowerCase()!=_185.toLowerCase()){
node=dojo.dom.nextElement(node,_185);
}
return node;
};
dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_187,_188){
var node=_187.lastChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.previousSibling;
}
if(_188&&node&&node.tagName&&node.tagName.toLowerCase()!=_188.toLowerCase()){
node=dojo.dom.prevElement(node,_188);
}
return node;
};
dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(node,_18b){
if(!node){
return null;
}
do{
node=node.nextSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_18b&&_18b.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.nextElement(node,_18b);
}
return node;
};
dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(node,_18d){
if(!node){
return null;
}
if(_18d){
_18d=_18d.toLowerCase();
}
do{
node=node.previousSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_18d&&_18d.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.prevElement(node,_18d);
}
return node;
};
dojo.dom.moveChildren=function(_18e,_18f,trim){
var _191=0;
if(trim){
while(_18e.hasChildNodes()&&_18e.firstChild.nodeType==dojo.dom.TEXT_NODE){
_18e.removeChild(_18e.firstChild);
}
while(_18e.hasChildNodes()&&_18e.lastChild.nodeType==dojo.dom.TEXT_NODE){
_18e.removeChild(_18e.lastChild);
}
}
while(_18e.hasChildNodes()){
_18f.appendChild(_18e.firstChild);
_191++;
}
return _191;
};
dojo.dom.copyChildren=function(_192,_193,trim){
var _195=_192.cloneNode(true);
return this.moveChildren(_195,_193,trim);
};
dojo.dom.removeChildren=function(node){
var _197=node.childNodes.length;
while(node.hasChildNodes()){
node.removeChild(node.firstChild);
}
return _197;
};
dojo.dom.replaceChildren=function(node,_199){
dojo.dom.removeChildren(node);
node.appendChild(_199);
};
dojo.dom.removeNode=function(node){
if(node&&node.parentNode){
return node.parentNode.removeChild(node);
}
};
dojo.dom.getAncestors=function(node,_19c,_19d){
var _19e=[];
var _19f=dojo.lang.isFunction(_19c);
while(node){
if(!_19f||_19c(node)){
_19e.push(node);
}
if(_19d&&_19e.length>0){
return _19e[0];
}
node=node.parentNode;
}
if(_19d){
return null;
}
return _19e;
};
dojo.dom.getAncestorsByTag=function(node,tag,_1a2){
tag=tag.toLowerCase();
return dojo.dom.getAncestors(node,function(el){
return ((el.tagName)&&(el.tagName.toLowerCase()==tag));
},_1a2);
};
dojo.dom.getFirstAncestorByTag=function(node,tag){
return dojo.dom.getAncestorsByTag(node,tag,true);
};
dojo.dom.isDescendantOf=function(node,_1a7,_1a8){
if(_1a8&&node){
node=node.parentNode;
}
while(node){
if(node==_1a7){
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
dojo.dom.createDocumentFromText=function(str,_1ab){
if(!_1ab){
_1ab="text/xml";
}
if(typeof DOMParser!="undefined"){
var _1ac=new DOMParser();
return _1ac.parseFromString(str,_1ab);
}else{
if(typeof ActiveXObject!="undefined"){
var _1ad=new ActiveXObject("Microsoft.XMLDOM");
if(_1ad){
_1ad.async=false;
_1ad.loadXML(str);
return _1ad;
}else{
dojo.debug("toXml didn't work?");
}
}else{
if(document.createElement){
var tmp=document.createElement("xml");
tmp.innerHTML=str;
if(document.implementation&&document.implementation.createDocument){
var _1af=document.implementation.createDocument("foo","",null);
for(var i=0;i<tmp.childNodes.length;i++){
_1af.importNode(tmp.childNodes.item(i),true);
}
return _1af;
}
return tmp.document&&tmp.document.firstChild?tmp.document.firstChild:tmp;
}
}
}
return null;
};
dojo.dom.prependChild=function(node,_1b2){
if(_1b2.firstChild){
_1b2.insertBefore(node,_1b2.firstChild);
}else{
_1b2.appendChild(node);
}
return true;
};
dojo.dom.insertBefore=function(node,ref,_1b5){
if(_1b5!=true&&(node===ref||node.nextSibling===ref)){
return false;
}
var _1b6=ref.parentNode;
_1b6.insertBefore(node,ref);
return true;
};
dojo.dom.insertAfter=function(node,ref,_1b9){
var pn=ref.parentNode;
if(ref==pn.lastChild){
if((_1b9!=true)&&(node===ref)){
return false;
}
pn.appendChild(node);
}else{
return this.insertBefore(node,ref.nextSibling,_1b9);
}
return true;
};
dojo.dom.insertAtPosition=function(node,ref,_1bd){
if((!node)||(!ref)||(!_1bd)){
return false;
}
switch(_1bd.toLowerCase()){
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
dojo.dom.insertAtIndex=function(node,_1bf,_1c0){
var _1c1=_1bf.childNodes;
if(!_1c1.length){
_1bf.appendChild(node);
return true;
}
var _1c2=null;
for(var i=0;i<_1c1.length;i++){
var _1c4=_1c1.item(i)["getAttribute"]?parseInt(_1c1.item(i).getAttribute("dojoinsertionindex")):-1;
if(_1c4<_1c0){
_1c2=_1c1.item(i);
}
}
if(_1c2){
return dojo.dom.insertAfter(node,_1c2);
}else{
return dojo.dom.insertBefore(node,_1c1.item(0));
}
};
dojo.dom.textContent=function(node,text){
if(text){
dojo.dom.replaceChildren(node,document.createTextNode(text));
return text;
}else{
var _1c7="";
if(node==null){
return _1c7;
}
for(var i=0;i<node.childNodes.length;i++){
switch(node.childNodes[i].nodeType){
case 1:
case 5:
_1c7+=dojo.dom.textContent(node.childNodes[i]);
break;
case 3:
case 2:
case 4:
_1c7+=node.childNodes[i].nodeValue;
break;
default:
break;
}
}
return _1c7;
}
};
dojo.dom.collectionToArray=function(_1c9){
dojo.deprecated("dojo.dom.collectionToArray","use dojo.lang.toArray instead","0.4");
return dojo.lang.toArray(_1c9);
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
var _1d1=args["back"]||args["backButton"]||args["handle"];
var tcb=function(_1d3){
if(window.location.hash!=""){
setTimeout("window.location.href = '"+hash+"';",1);
}
_1d1.apply(this,[_1d3]);
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
var _1d4=args["forward"]||args["forwardButton"]||args["handle"];
var tfw=function(_1d6){
if(window.location.hash!=""){
window.location.href=hash;
}
if(_1d4){
_1d4.apply(this,[_1d6]);
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
},iframeLoaded:function(evt,_1d9){
var _1da=this._getUrlQuery(_1d9.href);
if(_1da==null){
if(this.historyStack.length==1){
this.handleBackButton();
}
return;
}
if(this.moveForward){
this.moveForward=false;
return;
}
if(this.historyStack.length>=2&&_1da==this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){
this.handleBackButton();
}else{
if(this.forwardStack.length>0&&_1da==this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){
this.handleForwardButton();
}
}
},handleBackButton:function(){
var _1db=this.historyStack.pop();
if(!_1db){
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
this.forwardStack.push(_1db);
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
var _1df=url.split("?");
if(_1df.length<2){
return null;
}else{
return _1df[1];
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
var _1e1=false;
var _1e2=node.getElementsByTagName("input");
dojo.lang.forEach(_1e2,function(_1e3){
if(_1e1){
return;
}
if(_1e3.getAttribute("type")=="file"){
_1e1=true;
}
});
return _1e1;
};
dojo.io.formHasFile=function(_1e4){
return dojo.io.checkChildrenForFile(_1e4);
};
dojo.io.updateNode=function(node,_1e6){
node=dojo.byId(node);
var args=_1e6;
if(dojo.lang.isString(_1e6)){
args={url:_1e6};
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
dojo.io.encodeForm=function(_1ed,_1ee,_1ef){
if((!_1ed)||(!_1ed.tagName)||(!_1ed.tagName.toLowerCase()=="form")){
dojo.raise("Attempted to encode a non-form element.");
}
if(!_1ef){
_1ef=dojo.io.formFilter;
}
var enc=/utf/i.test(_1ee||"")?encodeURIComponent:dojo.string.encodeAscii;
var _1f1=[];
for(var i=0;i<_1ed.elements.length;i++){
var elm=_1ed.elements[i];
if(!elm||elm.tagName.toLowerCase()=="fieldset"||!_1ef(elm)){
continue;
}
var name=enc(elm.name);
var type=elm.type.toLowerCase();
if(type=="select-multiple"){
for(var j=0;j<elm.options.length;j++){
if(elm.options[j].selected){
_1f1.push(name+"="+enc(elm.options[j].value));
}
}
}else{
if(dojo.lang.inArray(type,["radio","checkbox"])){
if(elm.checked){
_1f1.push(name+"="+enc(elm.value));
}
}else{
_1f1.push(name+"="+enc(elm.value));
}
}
}
var _1f7=_1ed.getElementsByTagName("input");
for(var i=0;i<_1f7.length;i++){
var _1f8=_1f7[i];
if(_1f8.type.toLowerCase()=="image"&&_1f8.form==_1ed&&_1ef(_1f8)){
var name=enc(_1f8.name);
_1f1.push(name+"="+enc(_1f8.value));
_1f1.push(name+".x=0");
_1f1.push(name+".y=0");
}
}
return _1f1.join("&")+"&";
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
var _1fe=form.getElementsByTagName("input");
for(var i=0;i<_1fe.length;i++){
var _1ff=_1fe[i];
if(_1ff.type.toLowerCase()=="image"&&_1ff.form==form){
this.connect(_1ff,"onclick","click");
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
var _206=false;
if(node.disabled||!node.name){
_206=false;
}else{
if(dojo.lang.inArray(type,["submit","button","image"])){
if(!this.clickedButton){
this.clickedButton=node;
}
_206=node==this.clickedButton;
}else{
_206=!dojo.lang.inArray(type,["file","submit","reset","button"]);
}
}
return _206;
},connect:function(_207,_208,_209){
if(dojo.evalObjPath("dojo.event.connect")){
dojo.event.connect(_207,_208,this,_209);
}else{
var fcn=dojo.lang.hitch(this,_209);
_207[_208]=function(e){
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
var _20c=this;
var _20d={};
this.useCache=false;
this.preventCache=false;
function getCacheKey(url,_20f,_210){
return url+"|"+_20f+"|"+_210.toLowerCase();
}
function addToCache(url,_212,_213,http){
_20d[getCacheKey(url,_212,_213)]=http;
}
function getFromCache(url,_216,_217){
return _20d[getCacheKey(url,_216,_217)];
}
this.clearCache=function(){
_20d={};
};
function doLoad(_218,http,url,_21b,_21c){
if((http.status==200)||(http.status==304)||(http.status==204)||(location.protocol=="file:"&&(http.status==0||http.status==undefined))||(location.protocol=="chrome:"&&(http.status==0||http.status==undefined))){
var ret;
if(_218.method.toLowerCase()=="head"){
var _21e=http.getAllResponseHeaders();
ret={};
ret.toString=function(){
return _21e;
};
var _21f=_21e.split(/[\r\n]+/g);
for(var i=0;i<_21f.length;i++){
var pair=_21f[i].match(/^([^:]+)\s*:\s*(.+)$/i);
if(pair){
ret[pair[1]]=pair[2];
}
}
}else{
if(_218.mimetype=="text/javascript"){
try{
ret=dj_eval(http.responseText);
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=null;
}
}else{
if(_218.mimetype=="text/json"){
try{
ret=dj_eval("("+http.responseText+")");
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=false;
}
}else{
if((_218.mimetype=="application/xml")||(_218.mimetype=="text/xml")){
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
if(_21c){
addToCache(url,_21b,_218.method,http);
}
_218[(typeof _218.load=="function")?"load":"handle"]("load",ret,http,_218);
}else{
var _222=new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);
_218[(typeof _218.error=="function")?"error":"handle"]("error",_222,http,_218);
}
}
function setHeaders(http,_224){
if(_224["headers"]){
for(var _225 in _224["headers"]){
if(_225.toLowerCase()=="content-type"&&!_224["contentType"]){
_224["contentType"]=_224["headers"][_225];
}else{
http.setRequestHeader(_225,_224["headers"][_225]);
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
var _229=dojo.hostenv.getXmlhttpObject()?true:false;
this.canHandle=function(_22a){
return _229&&dojo.lang.inArray((_22a["mimetype"].toLowerCase()||""),["text/plain","text/html","application/xml","text/xml","text/javascript","text/json"])&&!(_22a["formNode"]&&dojo.io.formHasFile(_22a["formNode"]));
};
this.multipartBoundary="45309FFF-BD65-4d50-99C9-36986896A96F";
this.bind=function(_22b){
if(!_22b["url"]){
if(!_22b["formNode"]&&(_22b["backButton"]||_22b["back"]||_22b["changeUrl"]||_22b["watchForURL"])&&(!djConfig.preventBackButtonFix)){
dojo.deprecated("Using dojo.io.XMLHTTPTransport.bind() to add to browser history without doing an IO request","Use dojo.undo.browser.addToHistory() instead.","0.4");
dojo.undo.browser.addToHistory(_22b);
return true;
}
}
var url=_22b.url;
var _22d="";
if(_22b["formNode"]){
var ta=_22b.formNode.getAttribute("action");
if((ta)&&(!_22b["url"])){
url=ta;
}
var tp=_22b.formNode.getAttribute("method");
if((tp)&&(!_22b["method"])){
_22b.method=tp;
}
_22d+=dojo.io.encodeForm(_22b.formNode,_22b.encoding,_22b["formFilter"]);
}
if(url.indexOf("#")>-1){
dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);
url=url.split("#")[0];
}
if(_22b["file"]){
_22b.method="post";
}
if(!_22b["method"]){
_22b.method="get";
}
if(_22b.method.toLowerCase()=="get"){
_22b.multipart=false;
}else{
if(_22b["file"]){
_22b.multipart=true;
}else{
if(!_22b["multipart"]){
_22b.multipart=false;
}
}
}
if(_22b["backButton"]||_22b["back"]||_22b["changeUrl"]){
dojo.undo.browser.addToHistory(_22b);
}
var _230=_22b["content"]||{};
if(_22b.sendTransport){
_230["dojo.transport"]="xmlhttp";
}
do{
if(_22b.postContent){
_22d=_22b.postContent;
break;
}
if(_230){
_22d+=dojo.io.argsFromMap(_230,_22b.encoding);
}
if(_22b.method.toLowerCase()=="get"||!_22b.multipart){
break;
}
var t=[];
if(_22d.length){
var q=_22d.split("&");
for(var i=0;i<q.length;++i){
if(q[i].length){
var p=q[i].split("=");
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+p[0]+"\"","",p[1]);
}
}
}
if(_22b.file){
if(dojo.lang.isArray(_22b.file)){
for(var i=0;i<_22b.file.length;++i){
var o=_22b.file[i];
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}else{
var o=_22b.file;
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}
if(t.length){
t.push("--"+this.multipartBoundary+"--","");
_22d=t.join("\r\n");
}
}while(false);
var _236=_22b["sync"]?false:true;
var _237=_22b["preventCache"]||(this.preventCache==true&&_22b["preventCache"]!=false);
var _238=_22b["useCache"]==true||(this.useCache==true&&_22b["useCache"]!=false);
if(!_237&&_238){
var _239=getFromCache(url,_22d,_22b.method);
if(_239){
doLoad(_22b,_239,url,_22d,false);
return;
}
}
var http=dojo.hostenv.getXmlhttpObject(_22b);
var _23b=false;
if(_236){
var _23c=this.inFlight.push({"req":_22b,"http":http,"url":url,"query":_22d,"useCache":_238,"startTime":_22b.timeoutSeconds?(new Date()).getTime():0});
this.startWatchingInFlight();
}
if(_22b.method.toLowerCase()=="post"){
http.open("POST",url,_236);
setHeaders(http,_22b);
http.setRequestHeader("Content-Type",_22b.multipart?("multipart/form-data; boundary="+this.multipartBoundary):(_22b.contentType||"application/x-www-form-urlencoded"));
try{
http.send(_22d);
}
catch(e){
if(typeof http.abort=="function"){
http.abort();
}
doLoad(_22b,{status:404},url,_22d,_238);
}
}else{
var _23d=url;
if(_22d!=""){
_23d+=(_23d.indexOf("?")>-1?"&":"?")+_22d;
}
if(_237){
_23d+=(dojo.string.endsWithAny(_23d,"?","&")?"":(_23d.indexOf("?")>-1?"&":"?"))+"dojo.preventCache="+new Date().valueOf();
}
http.open(_22b.method.toUpperCase(),_23d,_236);
setHeaders(http,_22b);
try{
http.send(null);
}
catch(e){
if(typeof http.abort=="function"){
http.abort();
}
doLoad(_22b,{status:404},url,_22d,_238);
}
}
if(!_236){
doLoad(_22b,http,url,_22d,_238);
}
_22b.abort=function(){
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
var _241=dojo.lang.nameAnonFunc(args[2],ao.adviceObj);
ao.adviceFunc=_241;
}else{
if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){
ao.adviceType="after";
ao.srcObj=dj_global;
var _241=dojo.lang.nameAnonFunc(args[0],ao.srcObj);
ao.srcFunc=_241;
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
var _241=dojo.lang.nameAnonFunc(args[1],dj_global);
ao.srcFunc=_241;
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){
ao.srcObj=args[1];
ao.srcFunc=args[2];
var _241=dojo.lang.nameAnonFunc(args[3],dj_global);
ao.adviceObj=dj_global;
ao.adviceFunc=_241;
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
var _241=dojo.lang.nameAnonFunc(ao.aroundFunc,ao.aroundObj);
ao.aroundFunc=_241;
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
var _243={};
for(var x in ao){
_243[x]=ao[x];
}
var mjps=[];
dojo.lang.forEach(ao.srcObj,function(src){
if((dojo.render.html.capable)&&(dojo.lang.isString(src))){
src=dojo.byId(src);
}
_243.srcObj=src;
mjps.push(dojo.event.connect.call(dojo.event,_243));
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
var _24b;
if((arguments.length==1)&&(typeof a1=="object")){
_24b=a1;
}else{
_24b={srcObj:a1,srcFunc:a2};
}
_24b.adviceFunc=function(){
var _24c=[];
for(var x=0;x<arguments.length;x++){
_24c.push(arguments[x]);
}
dojo.debug("("+_24b.srcObj+")."+_24b.srcFunc,":",_24c.join(", "));
};
this.kwConnect(_24b);
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
this._kwConnectImpl=function(_253,_254){
var fn=(_254)?"disconnect":"connect";
if(typeof _253["srcFunc"]=="function"){
_253.srcObj=_253["srcObj"]||dj_global;
var _256=dojo.lang.nameAnonFunc(_253.srcFunc,_253.srcObj);
_253.srcFunc=_256;
}
if(typeof _253["adviceFunc"]=="function"){
_253.adviceObj=_253["adviceObj"]||dj_global;
var _256=dojo.lang.nameAnonFunc(_253.adviceFunc,_253.adviceObj);
_253.adviceFunc=_256;
}
return dojo.event[fn]((_253["type"]||_253["adviceType"]||"after"),_253["srcObj"]||dj_global,_253["srcFunc"],_253["adviceObj"]||_253["targetObj"]||dj_global,_253["adviceFunc"]||_253["targetFunc"],_253["aroundObj"],_253["aroundFunc"],_253["once"],_253["delay"],_253["rate"],_253["adviceMsg"]||false);
};
this.kwConnect=function(_257){
return this._kwConnectImpl(_257,false);
};
this.disconnect=function(){
var ao=interpolateArgs(arguments);
if(!ao.adviceFunc){
return;
}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);
return mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);
};
this.kwDisconnect=function(_25a){
return this._kwConnectImpl(_25a,true);
};
};
dojo.event.MethodInvocation=function(_25b,obj,args){
this.jp_=_25b;
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
dojo.event.MethodJoinPoint=function(obj,_263){
this.object=obj||dj_global;
this.methodname=_263;
this.methodfunc=this.object[_263];
this.before=[];
this.after=[];
this.around=[];
};
dojo.event.MethodJoinPoint.getForMethod=function(obj,_265){
if(!obj){
obj=dj_global;
}
if(!obj[_265]){
obj[_265]=function(){
};
}else{
if((!dojo.lang.isFunction(obj[_265]))&&(!dojo.lang.isAlien(obj[_265]))){
return null;
}
}
var _266=_265+"$joinpoint";
var _267=_265+"$joinpoint$method";
var _268=obj[_266];
if(!_268){
var _269=false;
if(dojo.event["browser"]){
if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){
_269=true;
dojo.event.browser.addClobberNodeAttrs(obj,[_266,_267,_265]);
}
}
var _26a=obj[_265].length;
obj[_267]=obj[_265];
_268=obj[_266]=new dojo.event.MethodJoinPoint(obj,_267);
obj[_265]=function(){
var args=[];
if((_269)&&(!arguments.length)){
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
if((x==0)&&(_269)&&(dojo.event.browser.isEvent(arguments[x]))){
args.push(dojo.event.browser.fixEvent(arguments[x],this));
}else{
args.push(arguments[x]);
}
}
}
return _268.run.apply(_268,args);
};
obj[_265].__preJoinArity=_26a;
}
return _268;
};
dojo.lang.extend(dojo.event.MethodJoinPoint,{unintercept:function(){
this.object[this.methodname]=this.methodfunc;
this.before=[];
this.after=[];
this.around=[];
},disconnect:dojo.lang.forward("unintercept"),run:function(){
var obj=this.object||dj_global;
var args=arguments;
var _270=[];
for(var x=0;x<args.length;x++){
_270[x]=args[x];
}
var _272=function(marr){
if(!marr){
dojo.debug("Null argument to unrollAdvice()");
return;
}
var _274=marr[0]||dj_global;
var _275=marr[1];
if(!_274[_275]){
dojo.raise("function \""+_275+"\" does not exist on \""+_274+"\"");
}
var _276=marr[2]||dj_global;
var _277=marr[3];
var msg=marr[6];
var _279;
var to={args:[],jp_:this,object:obj,proceed:function(){
return _274[_275].apply(_274,to.args);
}};
to.args=_270;
var _27b=parseInt(marr[4]);
var _27c=((!isNaN(_27b))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));
if(marr[5]){
var rate=parseInt(marr[5]);
var cur=new Date();
var _27f=false;
if((marr["last"])&&((cur-marr.last)<=rate)){
if(dojo.event.canTimeout){
if(marr["delayTimer"]){
clearTimeout(marr.delayTimer);
}
var tod=parseInt(rate*2);
var mcpy=dojo.lang.shallowCopy(marr);
marr.delayTimer=setTimeout(function(){
mcpy[5]=0;
_272(mcpy);
},tod);
}
return;
}else{
marr.last=cur;
}
}
if(_277){
_276[_277].call(_276,to);
}else{
if((_27c)&&((dojo.render.html)||(dojo.render.svg))){
dj_global["setTimeout"](function(){
if(msg){
_274[_275].call(_274,to);
}else{
_274[_275].apply(_274,args);
}
},_27b);
}else{
if(msg){
_274[_275].call(_274,to);
}else{
_274[_275].apply(_274,args);
}
}
}
};
if(this.before.length>0){
dojo.lang.forEach(this.before,_272);
}
var _282;
if(this.around.length>0){
var mi=new dojo.event.MethodInvocation(this,obj,args);
_282=mi.proceed();
}else{
if(this.methodfunc){
_282=this.object[this.methodname].apply(this.object,args);
}
}
if(this.after.length>0){
dojo.lang.forEach(this.after,_272);
}
return (this.methodfunc)?_282:null;
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
},addAdvice:function(_287,_288,_289,_28a,_28b,_28c,once,_28e,rate,_290){
var arr=this.getArr(_28b);
if(!arr){
dojo.raise("bad this: "+this);
}
var ao=[_287,_288,_289,_28a,_28e,rate,_290];
if(once){
if(this.hasAdvice(_287,_288,_28b,arr)>=0){
return;
}
}
if(_28c=="first"){
arr.unshift(ao);
}else{
arr.push(ao);
}
},hasAdvice:function(_293,_294,_295,arr){
if(!arr){
arr=this.getArr(_295);
}
var ind=-1;
for(var x=0;x<arr.length;x++){
if((arr[x][0]==_293)&&(arr[x][1]==_294)){
ind=x;
}
}
return ind;
},removeAdvice:function(_299,_29a,_29b,once){
var arr=this.getArr(_29b);
var ind=this.hasAdvice(_299,_29a,_29b,arr);
if(ind==-1){
return false;
}
while(ind!=-1){
arr.splice(ind,1);
if(once){
break;
}
ind=this.hasAdvice(_299,_29a,_29b,arr);
}
return true;
}});
dojo.require("dojo.event");
dojo.provide("dojo.event.topic");
dojo.event.topic=new function(){
this.topics={};
this.getTopic=function(_29f){
if(!this.topics[_29f]){
this.topics[_29f]=new this.TopicImpl(_29f);
}
return this.topics[_29f];
};
this.registerPublisher=function(_2a0,obj,_2a2){
var _2a0=this.getTopic(_2a0);
_2a0.registerPublisher(obj,_2a2);
};
this.subscribe=function(_2a3,obj,_2a5){
var _2a3=this.getTopic(_2a3);
_2a3.subscribe(obj,_2a5);
};
this.unsubscribe=function(_2a6,obj,_2a8){
var _2a6=this.getTopic(_2a6);
_2a6.unsubscribe(obj,_2a8);
};
this.destroy=function(_2a9){
this.getTopic(_2a9).destroy();
delete this.topics[_2a9];
};
this.publish=function(_2aa,_2ab){
var _2aa=this.getTopic(_2aa);
var args=[];
if(arguments.length==2&&(dojo.lang.isArray(_2ab)||_2ab.callee)){
args=_2ab;
}else{
var args=[];
for(var x=1;x<arguments.length;x++){
args.push(arguments[x]);
}
}
_2aa.sendMessage.apply(_2aa,args);
};
};
dojo.event.topic.TopicImpl=function(_2ae){
this.topicName=_2ae;
this.subscribe=function(_2af,_2b0){
var tf=_2b0||_2af;
var to=(!_2b0)?dj_global:_2af;
dojo.event.kwConnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
this.unsubscribe=function(_2b3,_2b4){
var tf=(!_2b4)?_2b3:_2b4;
var to=(!_2b4)?null:_2b3;
dojo.event.kwDisconnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
this.destroy=function(){
dojo.event.MethodJoinPoint.getForMethod(this,"sendMessage").disconnect();
};
this.registerPublisher=function(_2b7,_2b8){
dojo.event.connect(_2b7,_2b8,this,"sendMessage");
};
this.sendMessage=function(_2b9){
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
this.clobber=function(_2bc){
var na;
var tna;
if(_2bc){
tna=_2bc.all||_2bc.getElementsByTagName("*");
na=[_2bc];
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
var _2c0={};
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
var _2c4=0;
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
this.addClobberNodeAttrs=function(node,_2c8){
this.addClobberNode(node);
for(var x=0;x<_2c8.length;x++){
node.__clobberAttrs__.push(_2c8[x]);
}
};
this.removeListener=function(node,_2cb,fp,_2cd){
if(!_2cd){
var _2cd=false;
}
_2cb=_2cb.toLowerCase();
if(_2cb.substr(0,2)=="on"){
_2cb=_2cb.substr(2);
}
if(node.removeEventListener){
node.removeEventListener(_2cb,fp,_2cd);
}
};
this.addListener=function(node,_2cf,fp,_2d1,_2d2){
if(!node){
return;
}
if(!_2d1){
var _2d1=false;
}
_2cf=_2cf.toLowerCase();
if(_2cf.substr(0,2)!="on"){
_2cf="on"+_2cf;
}
if(!_2d2){
var _2d3=function(evt){
if(!evt){
evt=window.event;
}
var ret=fp(dojo.event.browser.fixEvent(evt,this));
if(_2d1){
dojo.event.browser.stopEvent(evt);
}
return ret;
};
}else{
_2d3=fp;
}
if(node.addEventListener){
node.addEventListener(_2cf.substr(2),_2d3,_2d1);
return _2d3;
}else{
if(typeof node[_2cf]=="function"){
var _2d6=node[_2cf];
node[_2cf]=function(e){
_2d6(e);
return _2d3(e);
};
}else{
node[_2cf]=_2d3;
}
if(dojo.render.html.ie){
this.addClobberNodeAttrs(node,[_2cf]);
}
return _2d3;
}
};
this.isEvent=function(obj){
return (typeof obj!="undefined")&&(typeof Event!="undefined")&&(obj.eventPhase);
};
this.currentEvent=null;
this.callListener=function(_2d9,_2da){
if(typeof _2d9!="function"){
dojo.raise("listener not a function: "+_2d9);
}
dojo.event.browser.currentEvent.currentTarget=_2da;
return _2d9.call(_2da,dojo.event.browser.currentEvent);
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
this.fixEvent=function(evt,_2dd){
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
evt.currentTarget=(_2dd?_2dd:evt.srcElement);
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
dojo.lang.extend(dojo.graphics.color.Color,{toRgb:function(_2e6){
if(_2e6){
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
},blend:function(_2e7,_2e8){
return dojo.graphics.color.blend(this.toRgb(),new Color(_2e7).toRgb(),_2e8);
}});
dojo.graphics.color.named={white:[255,255,255],black:[0,0,0],red:[255,0,0],green:[0,255,0],blue:[0,0,255],navy:[0,0,128],gray:[128,128,128],silver:[192,192,192]};
dojo.graphics.color.blend=function(a,b,_2eb){
if(typeof a=="string"){
return dojo.graphics.color.blendHex(a,b,_2eb);
}
if(!_2eb){
_2eb=0;
}else{
if(_2eb>1){
_2eb=1;
}else{
if(_2eb<-1){
_2eb=-1;
}
}
}
var c=new Array(3);
for(var i=0;i<3;i++){
var half=Math.abs(a[i]-b[i])/2;
c[i]=Math.floor(Math.min(a[i],b[i])+half+(half*_2eb));
}
return c;
};
dojo.graphics.color.blendHex=function(a,b,_2f1){
return dojo.graphics.color.rgb2hex(dojo.graphics.color.blend(dojo.graphics.color.hex2rgb(a),dojo.graphics.color.hex2rgb(b),_2f1));
};
dojo.graphics.color.extractRGB=function(_2f2){
var hex="0123456789abcdef";
_2f2=_2f2.toLowerCase();
if(_2f2.indexOf("rgb")==0){
var _2f4=_2f2.match(/rgba*\((\d+), *(\d+), *(\d+)/i);
var ret=_2f4.splice(1,3);
return ret;
}else{
var _2f6=dojo.graphics.color.hex2rgb(_2f2);
if(_2f6){
return _2f6;
}else{
return dojo.graphics.color.named[_2f2]||[255,255,255];
}
}
};
dojo.graphics.color.hex2rgb=function(hex){
var _2f8="0123456789ABCDEF";
var rgb=new Array(3);
if(hex.indexOf("#")==0){
hex=hex.substring(1);
}
hex=hex.toUpperCase();
if(hex.replace(new RegExp("["+_2f8+"]","g"),"")!=""){
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
rgb[i]=_2f8.indexOf(rgb[i].charAt(0))*16+_2f8.indexOf(rgb[i].charAt(1));
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
var _306=new dojo.uri.Uri(arguments[i].toString());
var _307=new dojo.uri.Uri(uri.toString());
if(_306.path==""&&_306.scheme==null&&_306.authority==null&&_306.query==null){
if(_306.fragment!=null){
_307.fragment=_306.fragment;
}
_306=_307;
}
if(_306.scheme!=null&&_306.authority!=null){
uri="";
}
if(_306.scheme!=null){
uri+=_306.scheme+":";
}
if(_306.authority!=null){
uri+="//"+_306.authority;
}
uri+=_306.path;
if(_306.query!=null){
uri+="?"+_306.query;
}
if(_306.fragment!=null){
uri+="#"+_306.fragment;
}
}
this.uri=uri.toString();
var _308="^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$";
var r=this.uri.match(new RegExp(_308));
this.scheme=r[2]||(r[1]?"":null);
this.authority=r[4]||(r[3]?"":null);
this.path=r[5];
this.query=r[7]||(r[6]?"":null);
this.fragment=r[9]||(r[8]?"":null);
if(this.authority!=null){
_308="^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$";
r=this.authority.match(new RegExp(_308));
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
var _310=ds.getStyle(node,"-moz-box-sizing");
if(!_310){
_310=ds.getStyle(node,"box-sizing");
}
return (_310?_310:bs.CONTENT_BOX);
}
};
ds.isBorderBox=function(node){
return (ds.getBoxSizing(node)==bs.BORDER_BOX);
};
ds.getUnitValue=function(node,_313,_314){
var s=ds.getComputedStyle(node,_313);
if((!s)||((s=="auto")&&(_314))){
return {value:0,units:"px"};
}
if(dojo.lang.isUndefined(s)){
return ds.getUnitValue.bad;
}
var _316=s.match(/(\-?[\d.]+)([a-z%]*)/i);
if(!_316){
return ds.getUnitValue.bad;
}
return {value:Number(_316[1]),units:_316[2].toLowerCase()};
};
ds.getUnitValue.bad={value:NaN,units:""};
ds.getPixelValue=function(node,_318,_319){
var _31a=ds.getUnitValue(node,_318,_319);
if(isNaN(_31a.value)){
return 0;
}
if((_31a.value)&&(_31a.units!="px")){
return NaN;
}
return _31a.value;
};
ds.getNumericStyle=function(){
dojo.deprecated("dojo.(style|html).getNumericStyle","in favor of dojo.(style|html).getPixelValue","0.4");
return ds.getPixelValue.apply(this,arguments);
};
ds.setPositivePixelValue=function(node,_31c,_31d){
if(isNaN(_31d)){
return false;
}
node.style[_31c]=Math.max(0,_31d)+"px";
return true;
};
ds._sumPixelValues=function(node,_31f,_320){
var _321=0;
for(x=0;x<_31f.length;x++){
_321+=ds.getPixelValue(node,_31f[x],_320);
}
return _321;
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
ds.setContentBoxWidth=function(node,_32d){
node=dojo.byId(node);
if(ds.isBorderBox(node)){
_32d+=ds.getPadBorderWidth(node);
}
return ds.setPositivePixelValue(node,"width",_32d);
};
ds.setMarginBoxWidth=function(node,_32f){
node=dojo.byId(node);
if(!ds.isBorderBox(node)){
_32f-=ds.getPadBorderWidth(node);
}
_32f-=ds.getMarginWidth(node);
return ds.setPositivePixelValue(node,"width",_32f);
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
ds.setContentBoxHeight=function(node,_338){
node=dojo.byId(node);
if(ds.isBorderBox(node)){
_338+=ds.getPadBorderHeight(node);
}
return ds.setPositivePixelValue(node,"height",_338);
};
ds.setMarginBoxHeight=function(node,_33a){
node=dojo.byId(node);
if(!ds.isBorderBox(node)){
_33a-=ds.getPadBorderHeight(node);
}
_33a-=ds.getMarginHeight(node);
return ds.setPositivePixelValue(node,"height",_33a);
};
ds.getContentHeight=ds.getContentBoxHeight;
ds.getInnerHeight=ds.getBorderBoxHeight;
ds.getOuterHeight=ds.getMarginBoxHeight;
ds.setContentHeight=ds.setContentBoxHeight;
ds.setOuterHeight=ds.setMarginBoxHeight;
ds.getAbsolutePosition=ds.abs=function(node,_33c){
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
var _340;
if((h.safari)&&(node.style.getPropertyValue("position")=="absolute")&&(node.parentNode==db)){
_340=db;
}else{
_340=db.parentNode;
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
}while((node!=_340)&&(node!=null));
}else{
if(node["x"]&&node["y"]){
ret.x+=isNaN(node.x)?0:node.x;
ret.y+=isNaN(node.y)?0:node.y;
}
}
}
if(_33c){
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
var _345=0;
while(node){
var val=node[prop];
if(val){
_345+=val-0;
}
node=node.parentNode;
}
return _345;
};
ds.getTotalOffset=function(node,type,_349){
node=dojo.byId(node);
return ds.abs(node,_349)[(type=="top")?"y":"x"];
};
ds.getAbsoluteX=ds.totalOffsetLeft=function(node,_34b){
return ds.getTotalOffset(node,"left",_34b);
};
ds.getAbsoluteY=ds.totalOffsetTop=function(node,_34d){
return ds.getTotalOffset(node,"top",_34d);
};
ds.styleSheet=null;
ds.insertCssRule=function(_34e,_34f,_350){
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
_350=ds.styleSheet.cssRules.length;
}else{
if(ds.styleSheet.rules){
_350=ds.styleSheet.rules.length;
}else{
return null;
}
}
}
if(ds.styleSheet.insertRule){
var rule=_34e+" { "+_34f+" }";
return ds.styleSheet.insertRule(rule,_350);
}else{
if(ds.styleSheet.addRule){
return ds.styleSheet.addRule(_34e,_34f,_350);
}else{
return null;
}
}
};
ds.removeCssRule=function(_352){
if(!ds.styleSheet){
dojo.debug("no stylesheet defined for removing rules");
return false;
}
if(h.ie){
if(!_352){
_352=ds.styleSheet.rules.length;
ds.styleSheet.removeRule(_352);
}
}else{
if(document.styleSheets[0]){
if(!_352){
_352=ds.styleSheet.cssRules.length;
}
ds.styleSheet.deleteRule(_352);
}
}
return true;
};
ds.insertCssFile=function(URI,doc,_355){
if(!URI){
return;
}
if(!doc){
doc=document;
}
var _356=dojo.hostenv.getText(URI);
_356=ds.fixPathsInCssText(_356,URI);
if(_355){
var _357=doc.getElementsByTagName("style");
var _358="";
for(var i=0;i<_357.length;i++){
_358=(_357[i].styleSheet&&_357[i].styleSheet.cssText)?_357[i].styleSheet.cssText:_357[i].innerHTML;
if(_356==_358){
return;
}
}
}
var _35a=ds.insertCssText(_356);
if(_35a&&djConfig.isDebug){
_35a.setAttribute("dbgHref",URI);
}
return _35a;
};
ds.insertCssText=function(_35b,doc,URI){
if(!_35b){
return;
}
if(!doc){
doc=document;
}
if(URI){
_35b=ds.fixPathsInCssText(_35b,URI);
}
var _35e=doc.createElement("style");
_35e.setAttribute("type","text/css");
if(_35e.styleSheet){
_35e.styleSheet.cssText=_35b;
}else{
var _35f=doc.createTextNode(_35b);
_35e.appendChild(_35f);
}
var head=doc.getElementsByTagName("head")[0];
if(!head){
dojo.debug("No head tag in document, aborting styles");
}else{
head.appendChild(_35e);
}
return _35e;
};
ds.fixPathsInCssText=function(_361,URI){
if(!_361||!URI){
return;
}
var pos=0;
var str="";
var url="";
while(pos!=-1){
pos=0;
url="";
pos=_361.indexOf("url(",pos);
if(pos<0){
break;
}
str+=_361.slice(0,pos+4);
_361=_361.substring(pos+4,_361.length);
url+=_361.match(/^[\t\s\w()\/.\\'"-:#=&?]*\)/)[0];
_361=_361.substring(url.length-1,_361.length);
url=url.replace(/^[\s\t]*(['"]?)([\w()\/.\\'"-:#=&?]*)\1[\s\t]*?\)/,"$2");
if(url.search(/(file|https?|ftps?):\/\//)==-1){
url=(new dojo.uri.Uri(URI,url).toString());
}
str+=url;
}
return str+_361;
};
ds.getBackgroundColor=function(node){
node=dojo.byId(node);
var _367;
do{
_367=ds.getStyle(node,"background-color");
if(_367.toLowerCase()=="rgba(0, 0, 0, 0)"){
_367="transparent";
}
if(node==document.getElementsByTagName("body")[0]){
node=null;
break;
}
node=node.parentNode;
}while(node&&dojo.lang.inArray(_367,["transparent",""]));
if(_367=="transparent"){
_367=[255,255,255,0];
}else{
_367=dojo.graphics.color.extractRGB(_367);
}
return _367;
};
ds.getComputedStyle=function(node,_369,_36a){
node=dojo.byId(node);
var _369=ds.toSelectorCase(_369);
var _36b=ds.toCamelCase(_369);
if(!node||!node.style){
return _36a;
}else{
if(document.defaultView){
try{
var cs=document.defaultView.getComputedStyle(node,"");
if(cs){
return cs.getPropertyValue(_369);
}
}
catch(e){
if(node.style.getPropertyValue){
return node.style.getPropertyValue(_369);
}else{
return _36a;
}
}
}else{
if(node.currentStyle){
return node.currentStyle[_36b];
}
}
}
if(node.style.getPropertyValue){
return node.style.getPropertyValue(_369);
}else{
return _36a;
}
};
ds.getStyleProperty=function(node,_36e){
node=dojo.byId(node);
return (node&&node.style?node.style[ds.toCamelCase(_36e)]:undefined);
};
ds.getStyle=function(node,_370){
var _371=ds.getStyleProperty(node,_370);
return (_371?_371:ds.getComputedStyle(node,_370));
};
ds.setStyle=function(node,_373,_374){
node=dojo.byId(node);
if(node&&node.style){
var _375=ds.toCamelCase(_373);
node.style[_375]=_374;
}
};
ds.toCamelCase=function(_376){
var arr=_376.split("-"),cc=arr[0];
for(var i=1;i<arr.length;i++){
cc+=arr[i].charAt(0).toUpperCase()+arr[i].substring(1);
}
return cc;
};
ds.toSelectorCase=function(_379){
return _379.replace(/([A-Z])/g,"-$1").toLowerCase();
};
ds.setOpacity=function setOpacity(node,_37b,_37c){
node=dojo.byId(node);
if(!_37c){
if(_37b>=1){
if(h.ie){
ds.clearOpacity(node);
return;
}else{
_37b=0.999999;
}
}else{
if(_37b<0){
_37b=0;
}
}
}
if(h.ie){
if(node.nodeName.toLowerCase()=="tr"){
var tds=node.getElementsByTagName("td");
for(var x=0;x<tds.length;x++){
tds[x].style.filter="Alpha(Opacity="+_37b*100+")";
}
}
node.style.filter="Alpha(Opacity="+_37b*100+")";
}else{
if(h.moz){
node.style.opacity=_37b;
node.style.MozOpacity=_37b;
}else{
if(h.safari){
node.style.opacity=_37b;
node.style.KhtmlOpacity=_37b;
}else{
node.style.opacity=_37b;
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
ds._toggle=function(node,_384,_385){
node=dojo.byId(node);
_385(node,!_384(node));
return _384(node);
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
ds.setShowing=function(node,_38a){
ds[(_38a?"show":"hide")](node);
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
ds.setDisplay=function(node,_390){
ds.setStyle(node,"display",(dojo.lang.isString(_390)?_390:(_390?ds.suggestDisplayByTagName(node):"none")));
};
ds.isDisplayed=function(node){
return (ds.getComputedStyle(node,"display")!="none");
};
ds.toggleDisplay=function(node){
return ds._toggle(node,ds.isDisplayed,ds.setDisplay);
};
ds.setVisibility=function(node,_394){
ds.setStyle(node,"visibility",(dojo.lang.isString(_394)?_394:(_394?"visible":"hidden")));
};
ds.isVisible=function(node){
return (ds.getComputedStyle(node,"visibility")!="hidden");
};
ds.toggleVisibility=function(node){
return ds._toggle(node,ds.isVisible,ds.setVisibility);
};
ds.toCoordinateArray=function(_397,_398){
if(dojo.lang.isArray(_397)){
while(_397.length<4){
_397.push(0);
}
while(_397.length>4){
_397.pop();
}
var ret=_397;
}else{
var node=dojo.byId(_397);
var pos=ds.getAbsolutePosition(node,_398);
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
dojo.html.disableSelection=function(_39c){
_39c=dojo.byId(_39c)||document.body;
var h=dojo.render.html;
if(h.mozilla){
_39c.style.MozUserSelect="none";
}else{
if(h.safari){
_39c.style.KhtmlUserSelect="none";
}else{
if(h.ie){
_39c.unselectable="on";
}else{
return false;
}
}
}
return true;
};
dojo.html.enableSelection=function(_39e){
_39e=dojo.byId(_39e)||document.body;
var h=dojo.render.html;
if(h.mozilla){
_39e.style.MozUserSelect="";
}else{
if(h.safari){
_39e.style.KhtmlUserSelect="";
}else{
if(h.ie){
_39e.unselectable="off";
}else{
return false;
}
}
}
return true;
};
dojo.html.selectElement=function(_3a0){
_3a0=dojo.byId(_3a0);
if(document.selection&&document.body.createTextRange){
var _3a1=document.body.createTextRange();
_3a1.moveToElementText(_3a0);
_3a1.select();
}else{
if(window["getSelection"]){
var _3a2=window.getSelection();
if(_3a2["selectAllChildren"]){
_3a2.selectAllChildren(_3a0);
}
}
}
};
dojo.html.selectInputText=function(_3a3){
_3a3=dojo.byId(_3a3);
if(document.selection&&document.body.createTextRange){
var _3a4=_3a3.createTextRange();
_3a4.moveStart("character",0);
_3a4.moveEnd("character",_3a3.value.length);
_3a4.select();
}else{
if(window["getSelection"]){
var _3a5=window.getSelection();
_3a3.setSelectionRange(0,_3a3.value.length);
}
}
_3a3.focus();
};
dojo.html.isSelectionCollapsed=function(){
if(document["selection"]){
return document.selection.createRange().text=="";
}else{
if(window["getSelection"]){
var _3a6=window.getSelection();
if(dojo.lang.isString(_3a6)){
return _3a6=="";
}else{
return _3a6.isCollapsed;
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
var _3b1=dojo.byId(node);
type=type.toLowerCase();
while((_3b1)&&(_3b1.nodeName.toLowerCase()!=type)){
if(_3b1==(document["body"]||document["documentElement"])){
return null;
}
_3b1=_3b1.parentNode;
}
return _3b1;
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
dojo.html.hasClass=function(node,_3bd){
return dojo.lang.inArray(dojo.html.getClasses(node),_3bd);
};
dojo.html.prependClass=function(node,_3bf){
_3bf+=" "+dojo.html.getClass(node);
return dojo.html.setClass(node,_3bf);
};
dojo.html.addClass=function(node,_3c1){
if(dojo.html.hasClass(node,_3c1)){
return false;
}
_3c1=dojo.string.trim(dojo.html.getClass(node)+" "+_3c1);
return dojo.html.setClass(node,_3c1);
};
dojo.html.setClass=function(node,_3c3){
node=dojo.byId(node);
var cs=new String(_3c3);
try{
if(typeof node.className=="string"){
node.className=cs;
}else{
if(node.setAttribute){
node.setAttribute("class",_3c3);
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
dojo.html.removeClass=function(node,_3c6,_3c7){
var _3c6=dojo.string.trim(new String(_3c6));
try{
var cs=dojo.html.getClasses(node);
var nca=[];
if(_3c7){
for(var i=0;i<cs.length;i++){
if(cs[i].indexOf(_3c6)==-1){
nca.push(cs[i]);
}
}
}else{
for(var i=0;i<cs.length;i++){
if(cs[i]!=_3c6){
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
dojo.html.replaceClass=function(node,_3cc,_3cd){
dojo.html.removeClass(node,_3cd);
dojo.html.addClass(node,_3cc);
};
dojo.html.classMatchType={ContainsAll:0,ContainsAny:1,IsOnly:2};
dojo.html.getElementsByClass=function(_3ce,_3cf,_3d0,_3d1){
_3cf=dojo.byId(_3cf)||document;
var _3d2=_3ce.split(/\s+/g);
var _3d3=[];
if(_3d1!=1&&_3d1!=2){
_3d1=0;
}
var _3d4=new RegExp("(\\s|^)(("+_3d2.join(")|(")+"))(\\s|$)");
if(!_3d0){
_3d0="*";
}
var _3d5=_3cf.getElementsByTagName(_3d0);
var node,i=0;
outer:
while(node=_3d5[i++]){
var _3d7=dojo.html.getClasses(node);
if(_3d7.length==0){
continue outer;
}
var _3d8=0;
for(var j=0;j<_3d7.length;j++){
if(_3d4.test(_3d7[j])){
if(_3d1==dojo.html.classMatchType.ContainsAny){
_3d3.push(node);
continue outer;
}else{
_3d8++;
}
}else{
if(_3d1==dojo.html.classMatchType.IsOnly){
continue outer;
}
}
}
if(_3d8==_3d2.length){
if(_3d1==dojo.html.classMatchType.IsOnly&&_3d8==_3d7.length){
_3d3.push(node);
}else{
if(_3d1==dojo.html.classMatchType.ContainsAll){
_3d3.push(node);
}
}
}
}
return _3d3;
};
dojo.html.getElementsByClassName=dojo.html.getElementsByClass;
dojo.html.getCursorPosition=function(e){
e=e||window.event;
var _3db={x:0,y:0};
if(e.pageX||e.pageY){
_3db.x=e.pageX;
_3db.y=e.pageY;
}else{
var de=document.documentElement;
var db=document.body;
_3db.x=e.clientX+((de||db)["scrollLeft"])-((de||db)["clientLeft"]);
_3db.y=e.clientY+((de||db)["scrollTop"])-((de||db)["clientTop"]);
}
return _3db;
};
dojo.html.overElement=function(_3de,e){
_3de=dojo.byId(_3de);
var _3e0=dojo.html.getCursorPosition(e);
with(dojo.html){
var top=getAbsoluteY(_3de,true);
var _3e2=top+getInnerHeight(_3de);
var left=getAbsoluteX(_3de,true);
var _3e4=left+getInnerWidth(_3de);
}
return (_3e0.x>=left&&_3e0.x<=_3e4&&_3e0.y>=top&&_3e0.y<=_3e2);
};
dojo.html.setActiveStyleSheet=function(_3e5){
var i=0,a,els=document.getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")){
a.disabled=true;
if(a.getAttribute("title")==_3e5){
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
dojo.html.copyStyle=function(_3ec,_3ed){
if(dojo.lang.isUndefined(_3ed.style.cssText)){
_3ec.setAttribute("style",_3ed.getAttribute("style"));
}else{
_3ec.style.cssText=_3ed.style.cssText;
}
dojo.html.addClass(_3ec,dojo.html.getClass(_3ed));
};
dojo.html._callExtrasDeprecated=function(_3ee,args){
var _3f0="dojo.html.extras";
dojo.deprecated("dojo.html."+_3ee,"moved to "+_3f0,"0.4");
dojo["require"](_3f0);
return dojo.html[_3ee].apply(dojo.html,args);
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
dojo.lang.extend(dojo.AdapterRegistry,{register:function(name,_3f2,wrap,_3f4){
if(_3f4){
this.pairs.unshift([name,_3f2,wrap]);
}else{
this.pairs.push([name,_3f2,wrap]);
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
dojo.json={jsonRegistry:new dojo.AdapterRegistry(),register:function(name,_3fb,wrap,_3fd){
dojo.json.jsonRegistry.register(name,_3fb,wrap,_3fd);
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
var _401=typeof (o);
if(_401=="undefined"){
return "undefined";
}else{
if((_401=="number")||(_401=="boolean")){
return o+"";
}else{
if(o===null){
return "null";
}
}
}
if(_401=="string"){
return dojo.string.escapeString(o);
}
var me=arguments.callee;
var _403;
if(typeof (o.__json__)=="function"){
_403=o.__json__();
if(o!==_403){
return me(_403);
}
}
if(typeof (o.json)=="function"){
_403=o.json();
if(o!==_403){
return me(_403);
}
}
if(_401!="function"&&typeof (o.length)=="number"){
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
_403=dojo.json.jsonRegistry.match(o);
return me(_403);
}
catch(e){
}
if(_401=="function"){
return null;
}
res=[];
for(var k in o){
var _408;
if(typeof (k)=="number"){
_408="\""+k+"\"";
}else{
if(typeof (k)=="string"){
_408=dojo.string.escapeString(k);
}else{
continue;
}
}
val=me(o[k]);
if(typeof (val)!="string"){
continue;
}
res.push(_408+":"+val);
}
return "{"+res.join(",")+"}";
}};
dojo.provide("dojo.widget.Manager");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.func");
dojo.require("dojo.event.*");
dojo.widget.manager=new function(){
this.widgets=[];
this.widgetIds=[];
this.topWidgets={};
var _409={};
var _40a=[];
this.getUniqueId=function(_40b){
return _40b+"_"+(_409[_40b]!=undefined?++_409[_40b]:_409[_40b]=0);
};
this.add=function(_40c){
dojo.profile.start("dojo.widget.manager.add");
this.widgets.push(_40c);
if(!_40c.extraArgs["id"]){
_40c.extraArgs["id"]=_40c.extraArgs["ID"];
}
if(_40c.widgetId==""){
if(_40c["id"]){
_40c.widgetId=_40c["id"];
}else{
if(_40c.extraArgs["id"]){
_40c.widgetId=_40c.extraArgs["id"];
}else{
_40c.widgetId=this.getUniqueId(_40c.widgetType);
}
}
}
if(this.widgetIds[_40c.widgetId]){
dojo.debug("widget ID collision on ID: "+_40c.widgetId);
}
this.widgetIds[_40c.widgetId]=_40c;
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
this.remove=function(_40e){
var tw=this.widgets[_40e].widgetId;
delete this.widgetIds[tw];
this.widgets.splice(_40e,1);
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
this.getWidgetsByFilter=function(_418,_419){
var ret=[];
dojo.lang.every(this.widgets,function(x){
if(_418(x)){
ret.push(x);
if(_419){
return false;
}
}
return true;
});
return (_419?ret[0]:ret);
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
var _41f={};
var _420=["dojo.widget"];
for(var i=0;i<_420.length;i++){
_420[_420[i]]=true;
}
this.registerWidgetPackage=function(_422){
if(!_420[_422]){
_420[_422]=true;
_420.push(_422);
}
};
this.getWidgetPackageList=function(){
return dojo.lang.map(_420,function(elt){
return (elt!==true?elt:undefined);
});
};
this.getImplementation=function(_424,_425,_426){
var impl=this.getImplementationName(_424);
if(impl){
var ret=new impl(_425);
return ret;
}
};
this.getImplementationName=function(_429){
var _42a=_429.toLowerCase();
var impl=_41f[_42a];
if(impl){
return impl;
}
if(!_40a.length){
for(var _42c in dojo.render){
if(dojo.render[_42c]["capable"]===true){
var _42d=dojo.render[_42c].prefixes;
for(var i=0;i<_42d.length;i++){
_40a.push(_42d[i].toLowerCase());
}
}
}
_40a.push("");
}
for(var i=0;i<_420.length;i++){
var _42f=dojo.evalObjPath(_420[i]);
if(!_42f){
continue;
}
for(var j=0;j<_40a.length;j++){
if(!_42f[_40a[j]]){
continue;
}
for(var _431 in _42f[_40a[j]]){
if(_431.toLowerCase()!=_42a){
continue;
}
_41f[_42a]=_42f[_40a[j]][_431];
return _41f[_42a];
}
}
for(var j=0;j<_40a.length;j++){
for(var _431 in _42f){
if(_431.toLowerCase()!=(_40a[j]+_42a)){
continue;
}
_41f[_42a]=_42f[_431];
return _41f[_42a];
}
}
}
throw new Error("Could not locate \""+_429+"\" class");
};
this.resizing=false;
this.onWindowResized=function(){
if(this.resizing){
return;
}
try{
this.resizing=true;
for(var id in this.topWidgets){
var _433=this.topWidgets[id];
if(_433.onParentResized){
_433.onParentResized();
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
var g=function(_438,_439){
dw[(_439||_438)]=h(_438);
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
var _43b=dwm.getAllWidgets.apply(dwm,arguments);
if(arguments.length>0){
return _43b[n];
}
return _43b;
};
g("registerWidgetPackage");
g("getImplementation","getWidgetImplementation");
g("getImplementationName","getWidgetImplementationName");
dw.widgets=dwm.widgets;
dw.widgetIds=dwm.widgetIds;
dw.root=dwm.root;
})();

