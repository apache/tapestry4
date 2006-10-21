








;(function(){

var _addHostEnv = {
pkgFileName: "__package__",

// for recursion protection
loading_modules_: {},
loaded_modules_: {},
addedToLoadingCount: [],
removedFromLoadingCount: [],

inFlightCount: 0,

// FIXME: it should be possible to pull module prefixes in from djConfig
modulePrefixes_: {
dojo: {name: "dojo", value: "src"}
},

setModulePrefix: function(module, prefix){
// summary: establishes module/prefix pair
this.modulePrefixes_[module] = {name: module, value: prefix};
},

moduleHasPrefix: function(module){
// summary: checks to see if module has been established
var mp = this.modulePrefixes_;
return Boolean(mp[module] && mp[module].value); // Boolean
},

getModulePrefix: function(module){
// summary: gets the prefix associated with module
if(this.moduleHasPrefix(module)){
return this.modulePrefixes_[module].value; // String
}
return module; // String
},

getTextStack: [],
loadUriStack: [],
loadedUris: [],

//WARNING: This variable is referenced by packages outside of bootstrap: FloatingPane.js and undo/browser.js
post_load_: false,

//Egad! Lots of test files push on this directly instead of using dojo.addOnLoad.
modulesLoadedListeners: [],
unloadListeners: [],
loadNotifying: false
};


for(var param in _addHostEnv){
dojo.hostenv[param] = _addHostEnv[param];
}
})();

dojo.hostenv.loadPath = function(relpath, module, cb){


















var uri;
if(relpath.charAt(0) == '/' || relpath.match(/^\w+:/)){
// dojo.raise("relpath '" + relpath + "'; must be relative");
uri = relpath;
}else{
uri = this.getBaseScriptUri() + relpath;
}
if(djConfig.cacheBust && dojo.render.html.capable){
uri += "?" + String(djConfig.cacheBust).replace(/\W+/g,"");
}
try{
return !module ? this.loadUri(uri, cb) : this.loadUriAndCheck(uri, module, cb); // Boolean
}catch(e){
dojo.debug(e);
return false; // Boolean
}
}

dojo.hostenv.loadUri = function(uri, cb){












if(this.loadedUris[uri]){
return true; // Boolean
}
var contents = this.getText(uri, null, true);
if(!contents){ return false; } // Boolean
this.loadedUris[uri] = true;
if(cb){ contents = '('+contents+')'; }
var value = dj_eval(contents);
if(cb){ cb(value); }
return true; // Boolean
}


dojo.hostenv.loadUriAndCheck = function(uri, moduleName, cb){

var ok = true;
try{
ok = this.loadUri(uri, cb);
}catch(e){
dojo.debug("failed loading ", uri, " with error: ", e);
}
return Boolean(ok && this.findModule(moduleName, false)); // Boolean
}

dojo.loaded = function(){ }
dojo.unloaded = function(){ }

dojo.hostenv.loaded = function(){
this.loadNotifying = true;
this.post_load_ = true;
var mll = this.modulesLoadedListeners;
for(var x=0; x<mll.length; x++){
mll[x]();
}



this.modulesLoadedListeners = [];
this.loadNotifying = false;

dojo.loaded();
}

dojo.hostenv.unloaded = function(){
var mll = this.unloadListeners;
while(mll.length){
(mll.pop())();
}
dojo.unloaded();
}

dojo.addOnLoad = function(obj, functionName) {











var dh = dojo.hostenv;
if(arguments.length == 1) {
dh.modulesLoadedListeners.push(obj);
} else if(arguments.length > 1) {
dh.modulesLoadedListeners.push(function() {
obj[functionName]();
});
}





if(dh.post_load_ && dh.inFlightCount == 0 && !dh.loadNotifying){
dh.callLoaded();
}
}

dojo.addOnUnload = function(obj, functionName){





var dh = dojo.hostenv;
if(arguments.length == 1){
dh.unloadListeners.push(obj);
} else if(arguments.length > 1) {
dh.unloadListeners.push(function() {
obj[functionName]();
});
}
}

dojo.hostenv.modulesLoaded = function(){
if(this.post_load_){ return; }
if(this.loadUriStack.length==0 && this.getTextStack.length==0){
if(this.inFlightCount > 0){
dojo.debug("files still in flight!");
return;
}
dojo.hostenv.callLoaded();
}
}

dojo.hostenv.callLoaded = function(){
if(typeof setTimeout == "object"){
setTimeout("dojo.hostenv.loaded();", 0);
}else{
dojo.hostenv.loaded();
}
}

dojo.hostenv.getModuleSymbols = function(modulename){



var syms = modulename.split(".");
for(var i = syms.length; i>0; i--){
var parentModule = syms.slice(0, i).join(".");
if ((i==1) && !this.moduleHasPrefix(parentModule)){
//Support default module directory (sibling of dojo)
syms[0] = "../" + syms[0];
}else{
var parentModulePath = this.getModulePrefix(parentModule);
if(parentModulePath != parentModule){
syms.splice(0, i, parentModulePath);
break;
}
}
}
return syms; // Array
}

dojo.hostenv._global_omit_module_check = false;
dojo.hostenv.loadModule = function(moduleName, exactOnly, omitModuleCheck){






























if(!moduleName){ return; }
omitModuleCheck = this._global_omit_module_check || omitModuleCheck;
var module = this.findModule(moduleName, false);
if(module){
return module;
}


if(dj_undef(moduleName, this.loading_modules_)){
this.addedToLoadingCount.push(moduleName);
}
this.loading_modules_[moduleName] = 1;


var relpath = moduleName.replace(/\./g, '/') + '.js';

var nsyms = moduleName.split(".");










var syms = this.getModuleSymbols(moduleName);
var startedRelative = ((syms[0].charAt(0) != '/') && !syms[0].match(/^\w+:/));
var last = syms[syms.length - 1];
var ok;


if(last=="*"){
moduleName = nsyms.slice(0, -1).join('.');
while(syms.length){
syms.pop();
syms.push(this.pkgFileName);
relpath = syms.join("/") + '.js';
if(startedRelative && relpath.charAt(0)=="/"){
relpath = relpath.slice(1);
}
ok = this.loadPath(relpath, !omitModuleCheck ? moduleName : null);
if(ok){ break; }
syms.pop();
}
}else{
relpath = syms.join("/") + '.js';
moduleName = nsyms.join('.');
var modArg = !omitModuleCheck ? moduleName : null;
ok = this.loadPath(relpath, modArg);
if(!ok && !exactOnly){
syms.pop();
while(syms.length){
relpath = syms.join('/') + '.js';
ok = this.loadPath(relpath, modArg);
if(ok){ break; }
syms.pop();
relpath = syms.join('/') + '/'+this.pkgFileName+'.js';
if(startedRelative && relpath.charAt(0)=="/"){
relpath = relpath.slice(1);
}
ok = this.loadPath(relpath, modArg);
if(ok){ break; }
}
}

if(!ok && !omitModuleCheck){
dojo.raise("Could not load '" + moduleName + "'; last tried '" + relpath + "'");
}
}



if(!omitModuleCheck && !this["isXDomain"]){
// pass in false so we can give better error
module = this.findModule(moduleName, false);
if(!module){
dojo.raise("symbol '" + moduleName + "' is not defined after loading '" + relpath + "'");
}
}

return module;
}

dojo.hostenv.startPackage = function(packageName){










var fullPkgName = String(packageName);
var strippedPkgName = fullPkgName;

var syms = packageName.split(/\./);
if(syms[syms.length-1]=="*"){
syms.pop();
strippedPkgName = syms.join(".");
}
var evaledPkg = dojo.evalObjPath(strippedPkgName, true);
this.loaded_modules_[fullPkgName] = evaledPkg;
this.loaded_modules_[strippedPkgName] = evaledPkg;

return evaledPkg; // Object
}

dojo.hostenv.findModule = function(moduleName, mustExist){







var lmn = String(moduleName);

if(this.loaded_modules_[lmn]){
return this.loaded_modules_[lmn]; // Object
}

if(mustExist){
dojo.raise("no loaded module named '" + moduleName + "'");
}
return null; // null
}



dojo.kwCompoundRequire = function(modMap){



















var common = modMap["common"]||[];
var result = modMap[dojo.hostenv.name_] ? common.concat(modMap[dojo.hostenv.name_]||[]) : common.concat(modMap["default"]||[]);

for(var x=0; x<result.length; x++){
var curr = result[x];
if(curr.constructor == Array){
dojo.hostenv.loadModule.apply(dojo.hostenv, curr);
}else{
dojo.hostenv.loadModule(curr);
}
}
}

dojo.require = function( resourceName){












dojo.hostenv.loadModule.apply(dojo.hostenv, arguments);
}

dojo.requireIf = function( condition,  resourceName){


var arg0 = arguments[0];
if((arg0 === true)||(arg0=="common")||(arg0 && dojo.render[arg0].capable)){
var args = [];
for (var i = 1; i < arguments.length; i++) { args.push(arguments[i]); }
dojo.require.apply(dojo, args);
}
}

dojo.requireAfterIf = dojo.requireIf;

dojo.provide = function( resourceName){




















return dojo.hostenv.startPackage.apply(dojo.hostenv, arguments);
}

dojo.registerModulePath = function(module, prefix){




return dojo.hostenv.setModulePrefix(module, prefix);
}

dojo.setModulePrefix = function(module, prefix){

dojo.deprecated('dojo.setModulePrefix("' + module + '", "' + prefix + '")', "replaced by dojo.registerModulePath", "0.5");
return dojo.registerModulePath(module, prefix);
}

dojo.exists = function(obj, name){


var p = name.split(".");
for(var i = 0; i < p.length; i++){
if(!obj[p[i]]){ return false; } // Boolean
obj = obj[p[i]];
}
return true; // Boolean
}



dojo.hostenv.normalizeLocale = function(locale){




return locale ? locale.toLowerCase() : dojo.locale; // String
};

dojo.hostenv.searchLocalePath = function(locale, down, searchFunc){





locale = dojo.hostenv.normalizeLocale(locale);

var elements = locale.split('-');
var searchlist = [];
for(var i = elements.length; i > 0; i--){
searchlist.push(elements.slice(0, i).join('-'));
}
searchlist.push(false);
if(down){searchlist.reverse();}

for(var j = searchlist.length - 1; j >= 0; j--){
var loc = searchlist[j] || "ROOT";
var stop = searchFunc(loc);
if(stop){ break; }
}
}



dojo.hostenv.localesGenerated ; // value will be inserted here at build time, if necessary

dojo.hostenv.registerNlsPrefix = function(){


dojo.registerModulePath("nls","nls");
}

dojo.hostenv.preloadLocalizations = function(){




if(dojo.hostenv.localesGenerated){
dojo.hostenv.registerNlsPrefix();

function preload(locale){
locale = dojo.hostenv.normalizeLocale(locale);
dojo.hostenv.searchLocalePath(locale, true, function(loc){
for(var i=0; i<dojo.hostenv.localesGenerated.length;i++){
if(dojo.hostenv.localesGenerated[i] == loc){
dojo["require"]("nls.dojo_"+loc);
return true; // Boolean
}
}
return false; // Boolean
});
}
preload();
var extra = djConfig.extraLocale||[];
for(var i=0; i<extra.length; i++){
preload(extra[i]);
}
}
dojo.hostenv.preloadLocalizations = function(){};
}

dojo.requireLocalization = function(moduleName, bundleName, locale){













































dojo.hostenv.preloadLocalizations();
var bundlePackage = [moduleName, "nls", bundleName].join(".");







var bundle = dojo.hostenv.findModule(bundlePackage);
if(bundle){
if(djConfig.localizationComplete && bundle._built){return;}
var jsLoc = dojo.hostenv.normalizeLocale(locale).replace('-', '_');
var translationPackage = bundlePackage+"."+jsLoc;
if(dojo.hostenv.findModule(translationPackage)){return;}
}

bundle = dojo.hostenv.startPackage(bundlePackage);
var syms = dojo.hostenv.getModuleSymbols(moduleName);
var modpath = syms.concat("nls").join("/");
var parent;
dojo.hostenv.searchLocalePath(locale, false, function(loc){
var jsLoc = loc.replace('-', '_');
var translationPackage = bundlePackage + "." + jsLoc;
var loaded = false;
if(!dojo.hostenv.findModule(translationPackage)){
// Mark loaded whether it's found or not, so that further load attempts will not be made
dojo.hostenv.startPackage(translationPackage);
var module = [modpath];
if(loc != "ROOT"){module.push(loc);}
module.push(bundleName);
var filespec = module.join("/") + '.js';
loaded = dojo.hostenv.loadPath(filespec, null, function(hash){
// Use singleton with prototype to point to parent bundle, then mix-in result from loadPath
var clazz = function(){};
clazz.prototype = parent;
bundle[jsLoc] = new clazz();
for(var j in hash){ bundle[jsLoc][j] = hash[j]; }
});
}else{
loaded = true;
}
if(loaded && bundle[jsLoc]){
parent = bundle[jsLoc];
}else{
bundle[jsLoc] = parent;
}
});
};

(function(){




var extra = djConfig.extraLocale;
if(extra){
if(!extra instanceof Array){
extra = [extra];
}

var req = dojo.requireLocalization;
dojo.requireLocalization = function(m, b, locale){
req(m,b,locale);
if(locale){return;}
for(var i=0; i<extra.length; i++){
req(m,b,extra[i]);
}
};
}
})();
