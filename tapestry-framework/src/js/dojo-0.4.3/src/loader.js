(function(){
var _1={pkgFileName:"__package__",loading_modules_:{},loaded_modules_:{},addedToLoadingCount:[],removedFromLoadingCount:[],inFlightCount:0,modulePrefixes_:{dojo:{name:"dojo",value:"src"}},setModulePrefix:function(_2,_3){
this.modulePrefixes_[_2]={name:_2,value:_3};
},moduleHasPrefix:function(_4){
var mp=this.modulePrefixes_;
return Boolean(mp[_4]&&mp[_4].value);
},getModulePrefix:function(_6){
if(this.moduleHasPrefix(_6)){
return this.modulePrefixes_[_6].value;
}
return _6;
},getTextStack:[],loadUriStack:[],loadedUris:[],post_load_:false,modulesLoadedListeners:[],unloadListeners:[],loadNotifying:false};
for(var _7 in _1){
dojo.hostenv[_7]=_1[_7];
}
})();
dojo.hostenv.loadPath=function(_8,_9,cb){
var _b;
if(_8.charAt(0)=="/"||_8.match(/^\w+:/)){
_b=_8;
}else{
_b=this.getBaseScriptUri()+_8;
}
if(djConfig.cacheBust&&dojo.render.html.capable){
_b+="?"+String(djConfig.cacheBust).replace(/\W+/g,"");
}
try{
return !_9?this.loadUri(_b,cb):this.loadUriAndCheck(_b,_9,cb);
}
catch(e){
dojo.debug(e);
return false;
}
};
dojo.hostenv.loadUri=function(_c,cb){
if(this.loadedUris[_c]){
return true;
}
var _e=this.getText(_c,null,true);
if(!_e){
return false;
}
this.loadedUris[_c]=true;
var _f=dj_eval(_e);
if(cb){
cb(_f);
}
return true;
};
dojo.hostenv.loadUriAndCheck=function(uri,_11,cb){
var ok=true;
try{
ok=this.loadUri(uri,cb);
}
catch(e){
dojo.debug("failed loading ",uri," with error: ",e);
}
return Boolean(ok&&this.findModule(_11,false));
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
dojo.addOnLoad=function(obj,_18){
var dh=dojo.hostenv;
if(arguments.length==1){
dh.modulesLoadedListeners.push(obj);
}else{
if(arguments.length>1){
dh.modulesLoadedListeners.push(function(){
obj[_18]();
});
}
}
if(dh.post_load_&&dh.inFlightCount==0&&!dh.loadNotifying){
dh.callLoaded();
}
};
dojo.addOnUnload=function(obj,_1b){
var dh=dojo.hostenv;
if(arguments.length==1){
dh.unloadListeners.push(obj);
}else{
if(arguments.length>1){
dh.unloadListeners.push(function(){
obj[_1b]();
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
dojo.hostenv.getModuleSymbols=function(_1d){
var _1e=_1d.split(".");
for(var i=_1e.length;i>0;i--){
var _20=_1e.slice(0,i).join(".");
if((i==1)&&!this.moduleHasPrefix(_20)){
_1e[0]="../"+_1e[0];
}else{
var _21=this.getModulePrefix(_20);
if(_21!=_20){
_1e.splice(0,i,_21);
break;
}
}
}
return _1e;
};
dojo.hostenv._global_omit_module_check=false;
dojo.hostenv.loadModule=function(_22,_23,_24){
if(!_22){
return;
}
_24=this._global_omit_module_check||_24;
var _25=this.findModule(_22,false);
if(_25){
return _25;
}
if(dj_undef(_22,this.loading_modules_)){
this.addedToLoadingCount.push(_22);
}
this.loading_modules_[_22]=1;
var _26=_22.replace(/\./g,"/")+".js";
var _27=_22.split(".");
var _28=this.getModuleSymbols(_22);
var _29=((_28[0].charAt(0)!="/")&&!_28[0].match(/^\w+:/));
var _2a=_28[_28.length-1];
var ok;
if(_2a=="*"){
_22=_27.slice(0,-1).join(".");
while(_28.length){
_28.pop();
_28.push(this.pkgFileName);
_26=_28.join("/")+".js";
if(_29&&_26.charAt(0)=="/"){
_26=_26.slice(1);
}
ok=this.loadPath(_26,!_24?_22:null);
if(ok){
break;
}
_28.pop();
}
}else{
_26=_28.join("/")+".js";
_22=_27.join(".");
var _2c=!_24?_22:null;
ok=this.loadPath(_26,_2c);
if(!ok&&!_23){
_28.pop();
while(_28.length){
_26=_28.join("/")+".js";
ok=this.loadPath(_26,_2c);
if(ok){
break;
}
_28.pop();
_26=_28.join("/")+"/"+this.pkgFileName+".js";
if(_29&&_26.charAt(0)=="/"){
_26=_26.slice(1);
}
ok=this.loadPath(_26,_2c);
if(ok){
break;
}
}
}
if(!ok&&!_24){
dojo.raise("Could not load '"+_22+"'; last tried '"+_26+"'");
}
}
if(!_24&&!this["isXDomain"]){
_25=this.findModule(_22,false);
if(!_25){
dojo.raise("symbol '"+_22+"' is not defined after loading '"+_26+"'");
}
}
return _25;
};
dojo.hostenv.startPackage=function(_2d){
var _2e=String(_2d);
var _2f=_2e;
var _30=_2d.split(/\./);
if(_30[_30.length-1]=="*"){
_30.pop();
_2f=_30.join(".");
}
var _31=dojo.evalObjPath(_2f,true);
this.loaded_modules_[_2e]=_31;
this.loaded_modules_[_2f]=_31;
return _31;
};
dojo.hostenv.findModule=function(_32,_33){
var lmn=String(_32);
if(this.loaded_modules_[lmn]){
return this.loaded_modules_[lmn];
}
if(_33){
dojo.raise("no loaded module named '"+_32+"'");
}
return null;
};
dojo.kwCompoundRequire=function(_35){
var _36=_35["common"]||[];
var _37=_35[dojo.hostenv.name_]?_36.concat(_35[dojo.hostenv.name_]||[]):_36.concat(_35["default"]||[]);
for(var x=0;x<_37.length;x++){
var _39=_37[x];
if(_39.constructor==Array){
dojo.hostenv.loadModule.apply(dojo.hostenv,_39);
}else{
dojo.hostenv.loadModule(_39);
}
}
};
dojo.require=function(_3a){
dojo.hostenv.loadModule.apply(dojo.hostenv,arguments);
};
dojo.requireIf=function(_3b,_3c){
var _3d=arguments[0];
if((_3d===true)||(_3d=="common")||(_3d&&dojo.render[_3d].capable)){
var _3e=[];
for(var i=1;i<arguments.length;i++){
_3e.push(arguments[i]);
}
dojo.require.apply(dojo,_3e);
}
};
dojo.requireAfterIf=dojo.requireIf;
dojo.provide=function(_40){
return dojo.hostenv.startPackage.apply(dojo.hostenv,arguments);
};
dojo.registerModulePath=function(_41,_42){
return dojo.hostenv.setModulePrefix(_41,_42);
};
if(djConfig["modulePaths"]){
for(var param in djConfig["modulePaths"]){
dojo.registerModulePath(param,djConfig["modulePaths"][param]);
}
}
dojo.setModulePrefix=function(_43,_44){
dojo.deprecated("dojo.setModulePrefix(\""+_43+"\", \""+_44+"\")","replaced by dojo.registerModulePath","0.5");
return dojo.registerModulePath(_43,_44);
};
dojo.exists=function(obj,_46){
var p=_46.split(".");
for(var i=0;i<p.length;i++){
if(!obj[p[i]]){
return false;
}
obj=obj[p[i]];
}
return true;
};
dojo.hostenv.normalizeLocale=function(_49){
var _4a=_49?_49.toLowerCase():dojo.locale;
if(_4a=="root"){
_4a="ROOT";
}
return _4a;
};
dojo.hostenv.searchLocalePath=function(_4b,_4c,_4d){
_4b=dojo.hostenv.normalizeLocale(_4b);
var _4e=_4b.split("-");
var _4f=[];
for(var i=_4e.length;i>0;i--){
_4f.push(_4e.slice(0,i).join("-"));
}
_4f.push(false);
if(_4c){
_4f.reverse();
}
for(var j=_4f.length-1;j>=0;j--){
var loc=_4f[j]||"ROOT";
var _53=_4d(loc);
if(_53){
break;
}
}
};
dojo.hostenv.localesGenerated;
dojo.hostenv.registerNlsPrefix=function(){
dojo.registerModulePath("nls","nls");
};
dojo.hostenv.preloadLocalizations=function(){
if(dojo.hostenv.localesGenerated){
dojo.hostenv.registerNlsPrefix();
function preload(_54){
_54=dojo.hostenv.normalizeLocale(_54);
dojo.hostenv.searchLocalePath(_54,true,function(loc){
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
var _57=djConfig.extraLocale||[];
for(var i=0;i<_57.length;i++){
preload(_57[i]);
}
}
dojo.hostenv.preloadLocalizations=function(){
};
};
dojo.requireLocalization=function(_59,_5a,_5b,_5c){
dojo.hostenv.preloadLocalizations();
var _5d=dojo.hostenv.normalizeLocale(_5b);
var _5e=[_59,"nls",_5a].join(".");
var _5f="";
if(_5c){
var _60=_5c.split(",");
for(var i=0;i<_60.length;i++){
if(_5d.indexOf(_60[i])==0){
if(_60[i].length>_5f.length){
_5f=_60[i];
}
}
}
if(!_5f){
_5f="ROOT";
}
}
var _62=_5c?_5f:_5d;
var _63=dojo.hostenv.findModule(_5e);
var _64=null;
if(_63){
if(djConfig.localizationComplete&&_63._built){
return;
}
var _65=_62.replace("-","_");
var _66=_5e+"."+_65;
_64=dojo.hostenv.findModule(_66);
}
if(!_64){
_63=dojo.hostenv.startPackage(_5e);
var _67=dojo.hostenv.getModuleSymbols(_59);
var _68=_67.concat("nls").join("/");
var _69;
dojo.hostenv.searchLocalePath(_62,_5c,function(loc){
var _6b=loc.replace("-","_");
var _6c=_5e+"."+_6b;
var _6d=false;
if(!dojo.hostenv.findModule(_6c)){
dojo.hostenv.startPackage(_6c);
var _6e=[_68];
if(loc!="ROOT"){
_6e.push(loc);
}
_6e.push(_5a);
var _6f=_6e.join("/")+".js";
_6d=dojo.hostenv.loadPath(_6f,null,function(_70){
var _71=function(){
};
_71.prototype=_69;
_63[_6b]=new _71();
for(var j in _70){
_63[_6b][j]=_70[j];
}
});
}else{
_6d=true;
}
if(_6d&&_63[_6b]){
_69=_63[_6b];
}else{
_63[_6b]=_69;
}
if(_5c){
return true;
}
});
}
if(_5c&&_5d!=_5f){
_63[_5d.replace("-","_")]=_63[_5f.replace("-","_")];
}
};
(function(){
var _73=djConfig.extraLocale;
if(_73){
if(!_73 instanceof Array){
_73=[_73];
}
var req=dojo.requireLocalization;
dojo.requireLocalization=function(m,b,_77,_78){
req(m,b,_77,_78);
if(_77){
return;
}
for(var i=0;i<_73.length;i++){
req(m,b,_73[i],_78);
}
};
}
})();
