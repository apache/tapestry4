if(typeof dojo == "undefined"){var dj_global = this;var dj_currentContext = this;function dj_undef( name,  object){return (typeof (object || dj_currentContext)[name] == "undefined");}
if(dj_undef("djConfig", this)){var djConfig = {};}
if(dj_undef("dojo", this)){var dojo = {};}
dojo.global = function(){return dj_currentContext;}
dojo.locale  = djConfig.locale;dojo.version = {major: 0, minor: 0, patch: 0, flag: "dev",revision: Number("$Rev: 6425 $".match(/[0-9]+/)[0]),toString: function(){with(dojo.version){return major + "." + minor + "." + patch + flag + " (" + revision + ")";}}}
dojo.evalProp = function( name,  object,  create){if((!object)||(!name)) return undefined;if(!dj_undef(name, object)) return object[name];return (create ? (object[name]={}) : undefined);}
dojo.parseObjPath = function( path,  context,  create){var object = (context || dojo.global());var names = path.split('.');var prop = names.pop();for (var i=0,l=names.length;i<l && object;i++){object = dojo.evalProp(names[i], object, create);}
return {obj: object, prop: prop};}
dojo.evalObjPath = function( path,  create){if(typeof path != "string"){return dojo.global();}
if(path.indexOf('.') == -1){return dojo.evalProp(path, dojo.global(), create);}
var ref = dojo.parseObjPath(path, dojo.global(), create);if(ref){return dojo.evalProp(ref.prop, ref.obj, create);}
return null;}
dojo.errorToString = function( exception){if(!dj_undef("message", exception)){return exception.message;}else if(!dj_undef("description", exception)){return exception.description;}else{return exception;}}
dojo.raise = function( message,  exception){if(exception){message = message + ": "+dojo.errorToString(exception);}else{message = dojo.errorToString(message);}
try { if(djConfig.isDebug){ dojo.hostenv.println("FATAL exception raised: "+message); }} catch (e) {}
throw exception || Error(message);}
dojo.debug = function(){};dojo.debugShallow = function(obj){};dojo.profile = { start: function(){}, end: function(){}, stop: function(){}, dump: function(){}};function dj_eval( scriptFragment){return dj_global.eval ? dj_global.eval(scriptFragment) : eval(scriptFragment);}
dojo.unimplemented = function( funcname,  extra){var message = "'" + funcname + "' not implemented";if (extra != null) { message += " " + extra; }
dojo.raise(message);}
dojo.deprecated = function( behaviour,  extra,  removal){var message = "DEPRECATED: " + behaviour;if(extra){ message += " " + extra; }
if(removal){ message += " -- will be removed in version: " + removal; }
dojo.debug(message);}
dojo.render = (function(){function vscaffold(prefs, names){var tmp = {capable: false,support: {builtin: false,plugin: false},prefixes: prefs};for(var i=0; i<names.length; i++){tmp[names[i]] = false;}
return tmp;}
return {name: "",ver: dojo.version,os: { win: false, linux: false, osx: false },html: vscaffold(["html"], ["ie", "opera", "khtml", "safari", "moz"]),svg: vscaffold(["svg"], ["corel", "adobe", "batik"]),vml: vscaffold(["vml"], ["ie"]),swf: vscaffold(["Swf", "Flash", "Mm"], ["mm"]),swt: vscaffold(["Swt"], ["ibm"])};})();dojo.hostenv = (function(){var config = {isDebug: false,allowQueryConfig: false,baseScriptUri: "",baseRelativePath: "",libraryScriptUri: "",iePreventClobber: false,ieClobberMinimal: true,preventBackButtonFix: true,delayMozLoadingFix: false,searchIds: [],parseWidgets: true};if (typeof djConfig == "undefined") { djConfig = config; }
else {for (var option in config) {if (typeof djConfig[option] == "undefined") {djConfig[option] = config[option];}}}
return {name_: '(unset)',version_: '(unset)',getName: function(){return this.name_;},getVersion: function(){return this.version_;},getText: function( uri){dojo.unimplemented('getText', "uri=" + uri);}};})();dojo.hostenv.getBaseScriptUri = function(){if(djConfig.baseScriptUri.length){return djConfig.baseScriptUri;}
var uri = new String(djConfig.libraryScriptUri||djConfig.baseRelativePath);if (!uri) { dojo.raise("Nothing returned by getLibraryScriptUri(): " + uri); }
var lastslash = uri.lastIndexOf('/');djConfig.baseScriptUri = djConfig.baseRelativePath;return djConfig.baseScriptUri;}
;(function(){var _addHostEnv = {pkgFileName: "__package__",loading_modules_: {},loaded_modules_: {},addedToLoadingCount: [],removedFromLoadingCount: [],inFlightCount: 0,modulePrefixes_: {dojo: {name: "dojo", value: "src"}},setModulePrefix: function(module, prefix){this.modulePrefixes_[module] = {name: module, value: prefix};},moduleHasPrefix: function(module){var mp = this.modulePrefixes_;return Boolean(mp[module] && mp[module].value);},getModulePrefix: function(module){if(this.moduleHasPrefix(module)){return this.modulePrefixes_[module].value;}
return module;},getTextStack: [],loadUriStack: [],loadedUris: [],post_load_: false,modulesLoadedListeners: [],unloadListeners: [],loadNotifying: false};for(var param in _addHostEnv){dojo.hostenv[param] = _addHostEnv[param];}})();dojo.hostenv.loadPath = function(relpath, module, cb){var uri;if(relpath.charAt(0) == '/' || relpath.match(/^\w+:/)){uri = relpath;}else{uri = this.getBaseScriptUri() + relpath;}
if(djConfig.cacheBust && dojo.render.html.capable){uri += "?" + String(djConfig.cacheBust).replace(/\W+/g,"");}
try{return !module ? this.loadUri(uri, cb) : this.loadUriAndCheck(uri, module, cb);}catch(e){dojo.debug(e);return false;}}
dojo.hostenv.loadUri = function(uri, cb){if(this.loadedUris[uri]){return true;}
var contents = this.getText(uri, null, true);if(!contents){ return false; }
this.loadedUris[uri] = true;if(cb){ contents = '('+contents+')'; }
var value = dj_eval(contents);if(cb){ cb(value); }
return true;}
dojo.hostenv.loadUriAndCheck = function(uri, moduleName, cb){var ok = true;try{ok = this.loadUri(uri, cb);}catch(e){dojo.debug("failed loading ", uri, " with error: ", e);}
return Boolean(ok && this.findModule(moduleName, false));}
dojo.loaded = function(){ }
dojo.unloaded = function(){ }
dojo.hostenv.loaded = function(){this.loadNotifying = true;this.post_load_ = true;var mll = this.modulesLoadedListeners;for(var x=0; x<mll.length; x++){mll[x]();}
this.modulesLoadedListeners = [];this.loadNotifying = false;dojo.loaded();}
dojo.hostenv.unloaded = function(){var mll = this.unloadListeners;while(mll.length){(mll.pop())();}
dojo.unloaded();}
dojo.addOnLoad = function(obj, functionName) {var dh = dojo.hostenv;if(arguments.length == 1) {dh.modulesLoadedListeners.push(obj);} else if(arguments.length > 1) {dh.modulesLoadedListeners.push(function() {obj[functionName]();});}
if(dh.post_load_ && dh.inFlightCount == 0 && !dh.loadNotifying){dh.callLoaded();}}
dojo.addOnUnload = function(obj, functionName){var dh = dojo.hostenv;if(arguments.length == 1){dh.unloadListeners.push(obj);} else if(arguments.length > 1) {dh.unloadListeners.push(function() {obj[functionName]();});}}
dojo.hostenv.modulesLoaded = function(){if(this.post_load_){ return; }
if(this.loadUriStack.length==0 && this.getTextStack.length==0){if(this.inFlightCount > 0){dojo.debug("files still in flight!");return;}
dojo.hostenv.callLoaded();}}
dojo.hostenv.callLoaded = function(){if(typeof setTimeout == "object"){setTimeout("dojo.hostenv.loaded();", 0);}else{dojo.hostenv.loaded();}}
dojo.hostenv.getModuleSymbols = function(modulename){var syms = modulename.split(".");for(var i = syms.length; i>0; i--){var parentModule = syms.slice(0, i).join(".");if((i==1) && !this.moduleHasPrefix(parentModule)){syms[0] = "../" + syms[0];}else{var parentModulePath = this.getModulePrefix(parentModule);if(parentModulePath != parentModule){syms.splice(0, i, parentModulePath);break;}}}
return syms;}
dojo.hostenv._global_omit_module_check = false;dojo.hostenv.loadModule = function(moduleName, exactOnly, omitModuleCheck){if(!moduleName){ return; }
omitModuleCheck = this._global_omit_module_check || omitModuleCheck;var module = this.findModule(moduleName, false);if(module){return module;}
if(dj_undef(moduleName, this.loading_modules_)){this.addedToLoadingCount.push(moduleName);}
this.loading_modules_[moduleName] = 1;var relpath = moduleName.replace(/\./g, '/') + '.js';var nsyms = moduleName.split(".");var syms = this.getModuleSymbols(moduleName);var startedRelative = ((syms[0].charAt(0) != '/') && !syms[0].match(/^\w+:/));var last = syms[syms.length - 1];var ok;if(last=="*"){moduleName = nsyms.slice(0, -1).join('.');while(syms.length){syms.pop();syms.push(this.pkgFileName);relpath = syms.join("/") + '.js';if(startedRelative && relpath.charAt(0)=="/"){relpath = relpath.slice(1);}
ok = this.loadPath(relpath, !omitModuleCheck ? moduleName : null);if(ok){ break; }
syms.pop();}}else{relpath = syms.join("/") + '.js';moduleName = nsyms.join('.');var modArg = !omitModuleCheck ? moduleName : null;ok = this.loadPath(relpath, modArg);if(!ok && !exactOnly){syms.pop();while(syms.length){relpath = syms.join('/') + '.js';ok = this.loadPath(relpath, modArg);if(ok){ break; }
syms.pop();relpath = syms.join('/') + '/'+this.pkgFileName+'.js';if(startedRelative && relpath.charAt(0)=="/"){relpath = relpath.slice(1);}
ok = this.loadPath(relpath, modArg);if(ok){ break; }}}
if(!ok && !omitModuleCheck){dojo.raise("Could not load '" + moduleName + "'; last tried '" + relpath + "'");}}
if(!omitModuleCheck && !this["isXDomain"]){module = this.findModule(moduleName, false);if(!module){dojo.raise("symbol '" + moduleName + "' is not defined after loading '" + relpath + "'");}}
return module;}
dojo.hostenv.startPackage = function(packageName){var fullPkgName = String(packageName);var strippedPkgName = fullPkgName;var syms = packageName.split(/\./);if(syms[syms.length-1]=="*"){syms.pop();strippedPkgName = syms.join(".");}
var evaledPkg = dojo.evalObjPath(strippedPkgName, true);this.loaded_modules_[fullPkgName] = evaledPkg;this.loaded_modules_[strippedPkgName] = evaledPkg;return evaledPkg;}
dojo.hostenv.findModule = function(moduleName, mustExist){var lmn = String(moduleName);if(this.loaded_modules_[lmn]){return this.loaded_modules_[lmn];}
if(mustExist){dojo.raise("no loaded module named '" + moduleName + "'");}
return null;}
dojo.kwCompoundRequire = function(modMap){var common = modMap["common"]||[];var result = modMap[dojo.hostenv.name_] ? common.concat(modMap[dojo.hostenv.name_]||[]) : common.concat(modMap["default"]||[]);for(var x=0; x<result.length; x++){var curr = result[x];if(curr.constructor == Array){dojo.hostenv.loadModule.apply(dojo.hostenv, curr);}else{dojo.hostenv.loadModule(curr);}}}
dojo.require = function( resourceName){dojo.hostenv.loadModule.apply(dojo.hostenv, arguments);}
dojo.requireIf = function( condition,  resourceName){var arg0 = arguments[0];if((arg0 === true)||(arg0=="common")||(arg0 && dojo.render[arg0].capable)){var args = [];for (var i = 1; i < arguments.length; i++) { args.push(arguments[i]); }
dojo.require.apply(dojo, args);}}
dojo.requireAfterIf = dojo.requireIf;dojo.provide = function( resourceName){return dojo.hostenv.startPackage.apply(dojo.hostenv, arguments);}
dojo.registerModulePath = function(module, prefix){return dojo.hostenv.setModulePrefix(module, prefix);}
dojo.setModulePrefix = function(module, prefix){dojo.deprecated('dojo.setModulePrefix("' + module + '", "' + prefix + '")', "replaced by dojo.registerModulePath", "0.5");return dojo.registerModulePath(module, prefix);}
dojo.exists = function(obj, name){var p = name.split(".");for(var i = 0; i < p.length; i++){if(!obj[p[i]]){ return false; }
obj = obj[p[i]];}
return true;}
dojo.hostenv.normalizeLocale = function(locale){return locale ? locale.toLowerCase() : dojo.locale;};dojo.hostenv.searchLocalePath = function(locale, down, searchFunc){locale = dojo.hostenv.normalizeLocale(locale);var elements = locale.split('-');var searchlist = [];for(var i = elements.length; i > 0; i--){searchlist.push(elements.slice(0, i).join('-'));}
searchlist.push(false);if(down){searchlist.reverse();}
for(var j = searchlist.length - 1; j >= 0; j--){var loc = searchlist[j] || "ROOT";var stop = searchFunc(loc);if(stop){ break; }}}
dojo.hostenv.localesGenerated =["ROOT","es-es","es","it-it","pt-br","de","de-at","fr-fr","zh-cn","pt","en-us","zh","fr","zh-tw","it","en-gb","xx","de-de","ko-kr","ja-jp","ko","en","ja"];dojo.hostenv.registerNlsPrefix = function(){dojo.registerModulePath("nls","nls");}
dojo.hostenv.preloadLocalizations = function(){if(dojo.hostenv.localesGenerated){dojo.hostenv.registerNlsPrefix();function preload(locale){locale = dojo.hostenv.normalizeLocale(locale);dojo.hostenv.searchLocalePath(locale, true, function(loc){for(var i=0; i<dojo.hostenv.localesGenerated.length;i++){if(dojo.hostenv.localesGenerated[i] == loc){dojo["require"]("nls.dojo_"+loc);return true;}}
return false;});}
preload();var extra = djConfig.extraLocale||[];for(var i=0; i<extra.length; i++){preload(extra[i]);}}
dojo.hostenv.preloadLocalizations = function(){};}
dojo.requireLocalization = function(moduleName, bundleName, locale){dojo.hostenv.preloadLocalizations();var bundlePackage = [moduleName, "nls", bundleName].join(".");var bundle = dojo.hostenv.findModule(bundlePackage);if(bundle){if(djConfig.localizationComplete && bundle._built){return;}
var jsLoc = dojo.hostenv.normalizeLocale(locale).replace('-', '_');var translationPackage = bundlePackage+"."+jsLoc;if(dojo.hostenv.findModule(translationPackage)){return;}}
bundle = dojo.hostenv.startPackage(bundlePackage);var syms = dojo.hostenv.getModuleSymbols(moduleName);var modpath = syms.concat("nls").join("/");var parent;dojo.hostenv.searchLocalePath(locale, false, function(loc){var jsLoc = loc.replace('-', '_');var translationPackage = bundlePackage + "." + jsLoc;var loaded = false;if(!dojo.hostenv.findModule(translationPackage)){dojo.hostenv.startPackage(translationPackage);var module = [modpath];if(loc != "ROOT"){module.push(loc);}
module.push(bundleName);var filespec = module.join("/") + '.js';loaded = dojo.hostenv.loadPath(filespec, null, function(hash){var clazz = function(){};clazz.prototype = parent;bundle[jsLoc] = new clazz();for(var j in hash){ bundle[jsLoc][j] = hash[j]; }});}else{loaded = true;}
if(loaded && bundle[jsLoc]){parent = bundle[jsLoc];}else{bundle[jsLoc] = parent;}});};(function(){var extra = djConfig.extraLocale;if(extra){if(!extra instanceof Array){extra = [extra];}
var req = dojo.requireLocalization;dojo.requireLocalization = function(m, b, locale){req(m,b,locale);if(locale){return;}
for(var i=0; i<extra.length; i++){req(m,b,extra[i]);}};}})();};if(typeof window != 'undefined'){(function(){if(djConfig.allowQueryConfig){var baseUrl = document.location.toString();var params = baseUrl.split("?", 2);if(params.length > 1){var paramStr = params[1];var pairs = paramStr.split("&");for(var x in pairs){var sp = pairs[x].split("=");if((sp[0].length > 9)&&(sp[0].substr(0, 9) == "djConfig.")){var opt = sp[0].substr(9);try{djConfig[opt]=eval(sp[1]);}catch(e){djConfig[opt]=sp[1];}}}}}
if(
((djConfig["baseScriptUri"] == "")||(djConfig["baseRelativePath"] == "")) &&
(document && document.getElementsByTagName)
){var scripts = document.getElementsByTagName("script");var rePkg = /(__package__|dojo|bootstrap1)\.js([\?\.]|$)/i;for(var i = 0; i < scripts.length; i++) {var src = scripts[i].getAttribute("src");if(!src) { continue; }
var m = src.match(rePkg);if(m) {var root = src.substring(0, m.index);if(src.indexOf("bootstrap1") > -1) { root += "../"; }
if(!this["djConfig"]) { djConfig = {}; }
if(djConfig["baseScriptUri"] == "") { djConfig["baseScriptUri"] = root; }
if(djConfig["baseRelativePath"] == "") { djConfig["baseRelativePath"] = root; }
break;}}}
var dr = dojo.render;var drh = dojo.render.html;var drs = dojo.render.svg;var dua = (drh.UA = navigator.userAgent);var dav = (drh.AV = navigator.appVersion);var t = true;var f = false;drh.capable = t;drh.support.builtin = t;dr.ver = parseFloat(drh.AV);dr.os.mac = dav.indexOf("Macintosh") >= 0;dr.os.win = dav.indexOf("Windows") >= 0;dr.os.linux = dav.indexOf("X11") >= 0;drh.opera = dua.indexOf("Opera") >= 0;drh.khtml = (dav.indexOf("Konqueror") >= 0)||(dav.indexOf("Safari") >= 0);drh.safari = dav.indexOf("Safari") >= 0;var geckoPos = dua.indexOf("Gecko");drh.mozilla = drh.moz = (geckoPos >= 0)&&(!drh.khtml);if (drh.mozilla) {drh.geckoVersion = dua.substring(geckoPos + 6, geckoPos + 14);}
drh.ie = (document.all)&&(!drh.opera);drh.ie50 = drh.ie && dav.indexOf("MSIE 5.0")>=0;drh.ie55 = drh.ie && dav.indexOf("MSIE 5.5")>=0;drh.ie60 = drh.ie && dav.indexOf("MSIE 6.0")>=0;drh.ie70 = drh.ie && dav.indexOf("MSIE 7.0")>=0;var cm = document["compatMode"];drh.quirks = (cm == "BackCompat")||(cm == "QuirksMode")||drh.ie55||drh.ie50;dojo.locale = dojo.locale || (drh.ie ? navigator.userLanguage : navigator.language).toLowerCase();dr.vml.capable=drh.ie;drs.capable = f;drs.support.plugin = f;drs.support.builtin = f;var tdoc = window["document"];var tdi = tdoc["implementation"];if((tdi)&&(tdi["hasFeature"])&&(tdi.hasFeature("org.w3c.dom.svg", "1.0"))){drs.capable = t;drs.support.builtin = t;drs.support.plugin = f;}
if(drh.safari){var tmp = dua.split("AppleWebKit/")[1];var ver = parseFloat(tmp.split(" ")[0]);if(ver >= 420){drs.capable = t;drs.support.builtin = t;drs.support.plugin = f;}}else{}})();dojo.hostenv.startPackage("dojo.hostenv");dojo.render.name = dojo.hostenv.name_ = 'browser';dojo.hostenv.searchIds = [];dojo.hostenv._XMLHTTP_PROGIDS = ['Msxml2.XMLHTTP', 'Microsoft.XMLHTTP', 'Msxml2.XMLHTTP.4.0'];dojo.hostenv.getXmlhttpObject = function(){var http = null;var last_e = null;try{ http = new XMLHttpRequest(); }catch(e){}
if(!http){for(var i=0; i<3; ++i){var progid = dojo.hostenv._XMLHTTP_PROGIDS[i];try{http = new ActiveXObject(progid);}catch(e){last_e = e;}
if(http){dojo.hostenv._XMLHTTP_PROGIDS = [progid];break;}}}
if(!http){return dojo.raise("XMLHTTP not available", last_e);}
return http;}
dojo.hostenv._blockAsync = false;dojo.hostenv.getText = function(uri, async_cb, fail_ok){if(!async_cb){ this._blockAsync = true; }
var http = this.getXmlhttpObject();function isDocumentOk(http){var stat = http["status"];return Boolean((!stat)||((200 <= stat)&&(300 > stat))||(stat==304));}
if(async_cb){var _this = this, timer = null, gbl = dojo.global();var xhr = dojo.evalObjPath("dojo.io.XMLHTTPTransport");http.onreadystatechange = function(){if(timer){ gbl.clearTimeout(timer); timer = null; }
if(_this._blockAsync || (xhr && xhr._blockAsync)){timer = gbl.setTimeout(function () { http.onreadystatechange.apply(this); }, 10);}else{if(4==http.readyState){if(isDocumentOk(http)){async_cb(http.responseText);}}}}}
http.open('GET', uri, async_cb ? true : false);try{http.send(null);if(async_cb){return null;}
if(!isDocumentOk(http)){var err = Error("Unable to load "+uri+" status:"+ http.status);err.status = http.status;err.responseText = http.responseText;throw err;}}catch(e){this._blockAsync = false;if((fail_ok)&&(!async_cb)){return null;}else{throw e;}}
this._blockAsync = false;return http.responseText;}
dojo.hostenv.defaultDebugContainerId = 'dojoDebug';dojo.hostenv._println_buffer = [];dojo.hostenv._println_safe = false;dojo.hostenv.println = function(line){if(!dojo.hostenv._println_safe){dojo.hostenv._println_buffer.push(line);}else{try {var console = document.getElementById(djConfig.debugContainerId ?
djConfig.debugContainerId : dojo.hostenv.defaultDebugContainerId);if(!console) { console = dojo.body(); }
var div = document.createElement("div");div.appendChild(document.createTextNode(line));console.appendChild(div);} catch (e) {try{document.write("<div>" + line + "</div>");}catch(e2){window.status = line;}}}}
dojo.addOnLoad(function(){dojo.hostenv._println_safe = true;while(dojo.hostenv._println_buffer.length > 0){dojo.hostenv.println(dojo.hostenv._println_buffer.shift());}});function dj_addNodeEvtHdlr(node, evtName, fp){var oldHandler = node["on"+evtName] || function(){};node["on"+evtName] = function(){fp.apply(node, arguments);oldHandler.apply(node, arguments);}
return true;}
function dj_load_init(e){var type = (e && e.type) ? e.type.toLowerCase() : "load";if(arguments.callee.initialized || (type!="domcontentloaded" && type!="load")){ return; }
arguments.callee.initialized = true;if(typeof(_timer) != 'undefined'){clearInterval(_timer);delete _timer;}
var initFunc = function(){if(dojo.render.html.ie){dojo.hostenv.makeWidgets();}};if(dojo.hostenv.inFlightCount == 0){initFunc();dojo.hostenv.modulesLoaded();}else{dojo.hostenv.modulesLoadedListeners.unshift(initFunc);}}
if(document.addEventListener){if(dojo.render.html.opera || (dojo.render.html.moz && !djConfig.delayMozLoadingFix)){document.addEventListener("DOMContentLoaded", dj_load_init, null);}
window.addEventListener("load", dj_load_init, null);}
if(dojo.render.html.ie && dojo.render.os.win){document.write('<scr'+'ipt defer src="//:" '
+ 'onreadystatechange="if(this.readyState==\'complete\'){dj_load_init();}">'
+ '</scr'+'ipt>'
);}
if (/(WebKit|khtml)/i.test(navigator.userAgent)) {var _timer = setInterval(function() {if (/loaded|complete/.test(document.readyState)) {dj_load_init();}}, 10);}
if(dojo.render.html.ie){dj_addNodeEvtHdlr(window, "beforeunload", function(){dojo.hostenv._unloading = true;window.setTimeout(function() {dojo.hostenv._unloading = false;}, 0);});}
dj_addNodeEvtHdlr(window, "unload", function(){dojo.hostenv.unloaded();if((!dojo.render.html.ie)||(dojo.render.html.ie && dojo.hostenv._unloading)){dojo.hostenv.unloaded();}});dojo.hostenv.makeWidgets = function(){var sids = [];if(djConfig.searchIds && djConfig.searchIds.length > 0) {sids = sids.concat(djConfig.searchIds);}
if(dojo.hostenv.searchIds && dojo.hostenv.searchIds.length > 0) {sids = sids.concat(dojo.hostenv.searchIds);}
if((djConfig.parseWidgets)||(sids.length > 0)){if(dojo.evalObjPath("dojo.widget.Parse")){var parser = new dojo.xml.Parse();if(sids.length > 0){for(var x=0; x<sids.length; x++){var tmpNode = document.getElementById(sids[x]);if(!tmpNode){ continue; }
var frag = parser.parseElement(tmpNode, null, true);dojo.widget.getParser().createComponents(frag);}}else if(djConfig.parseWidgets){var frag  = parser.parseElement(dojo.body(), null, true);dojo.widget.getParser().createComponents(frag);}}}}
dojo.addOnLoad(function(){if(!dojo.render.html.ie) {dojo.hostenv.makeWidgets();}});try{if(dojo.render.html.ie){document.namespaces.add("v","urn:schemas-microsoft-com:vml");document.createStyleSheet().addRule("v\\:*", "behavior:url(#default#VML)");}}catch(e){ }
dojo.hostenv.writeIncludes = function(){}
if(!dj_undef("document", this)){dj_currentDocument = this.document;}
dojo.doc = function(){return dj_currentDocument;}
dojo.body = function(){return dojo.doc().body || dojo.doc().getElementsByTagName("body")[0];}
dojo.byId = function(id, doc){if((id)&&((typeof id == "string")||(id instanceof String))){if(!doc){ doc = dj_currentDocument; }
var ele = doc.getElementById(id);if(ele && (ele.id != id) && doc.all){ele = null;eles = doc.all[id];if(eles){if(eles.length){for(var i=0; i<eles.length; i++){if(eles[i].id == id){ele = eles[i];break;}}}else{ele = eles;}}}
return ele;}
return id;}
dojo.setContext = function(globalObject, globalDocument){dj_currentContext = globalObject;dj_currentDocument = globalDocument;};dojo._fireCallback = function(callback, context, cbArguments){if((context)&&((typeof callback == "string")||(callback instanceof String))){callback=context[callback];}
return (context ? callback.apply(context, cbArguments || [ ]) : callback());}
dojo.withGlobal = function(globalObject, callback, thisObject, cbArguments){var rval;var oldGlob = dj_currentContext;var oldDoc = dj_currentDocument;try{dojo.setContext(globalObject, globalObject.document);rval = dojo._fireCallback(callback, thisObject, cbArguments);}finally{dojo.setContext(oldGlob, oldDoc);}
return rval;}
dojo.withDoc = function (documentObject, callback, thisObject, cbArguments) {var rval;var oldDoc = dj_currentDocument;try{dj_currentDocument = documentObject;rval = dojo._fireCallback(callback, thisObject, cbArguments);}finally{dj_currentDocument = oldDoc;}
return rval;}}
;(function(){if(typeof dj_usingBootstrap != "undefined"){return;}
var isRhino = false;var isSpidermonkey = false;var isDashboard = false;if((typeof this["load"] == "function")&&((typeof this["Packages"] == "function")||(typeof this["Packages"] == "object"))){isRhino = true;}else if(typeof this["load"] == "function"){isSpidermonkey  = true;}else if(window.widget){isDashboard = true;}
var tmps = [];if((this["djConfig"])&&((djConfig["isDebug"])||(djConfig["debugAtAllCosts"]))){tmps.push("debug.js");}
if((this["djConfig"])&&(djConfig["debugAtAllCosts"])&&(!isRhino)&&(!isDashboard)){tmps.push("browser_debug.js");}
var loaderRoot = djConfig["baseScriptUri"];if((this["djConfig"])&&(djConfig["baseLoaderUri"])){loaderRoot = djConfig["baseLoaderUri"];}
for(var x=0; x < tmps.length; x++){var spath = loaderRoot+"src/"+tmps[x];if(isRhino||isSpidermonkey){load(spath);} else {try {document.write("<scr"+"ipt type='text/javascript' src='"+spath+"'></scr"+"ipt>");} catch (e) {var script = document.createElement("script");script.src = spath;document.getElementsByTagName("head")[0].appendChild(script);}}}})();dojo.provide("dojo.lang.common");dojo.lang.inherits = function( subclass,  superclass){if(typeof superclass != 'function'){dojo.raise("dojo.inherits: superclass argument ["+superclass+"] must be a function (subclass: ["+subclass+"']");}
subclass.prototype = new superclass();subclass.prototype.constructor = subclass;subclass.superclass = superclass.prototype;subclass['super'] = superclass.prototype;}
dojo.lang._mixin = function( obj,  props){var tobj = {};for(var x in props){if((typeof tobj[x] == "undefined") || (tobj[x] != props[x])){obj[x] = props[x];}}
if(dojo.render.html.ie
&& (typeof(props["toString"]) == "function")
&& (props["toString"] != obj["toString"])
&& (props["toString"] != tobj["toString"]))
{obj.toString = props.toString;}
return obj;}
dojo.lang.mixin = function( obj, props){for(var i=1, l=arguments.length; i<l; i++){dojo.lang._mixin(obj, arguments[i]);}
return obj;}
dojo.lang.extend = function( constructor,  props){for(var i=1, l=arguments.length; i<l; i++){dojo.lang._mixin(constructor.prototype, arguments[i]);}
return constructor;}
dojo.lang._delegate = function(obj){function TMP(){};TMP.prototype = obj;return new TMP();}
dojo.inherits = dojo.lang.inherits;dojo.mixin = dojo.lang.mixin;dojo.extend = dojo.lang.extend;dojo.lang.find = function(			array,value,identity,findLast){if(!dojo.lang.isArrayLike(array) && dojo.lang.isArrayLike(value)) {dojo.deprecated('dojo.lang.find(value, array)', 'use dojo.lang.find(array, value) instead', "0.5");var temp = array;array = value;value = temp;}
var isString = dojo.lang.isString(array);if(isString) { array = array.split(""); }
if(findLast) {var step = -1;var i = array.length - 1;var end = -1;} else {var step = 1;var i = 0;var end = array.length;}
if(identity){while(i != end) {if(array[i] === value){ return i; }
i += step;}}else{while(i != end) {if(array[i] == value){ return i; }
i += step;}}
return -1;}
dojo.lang.indexOf = dojo.lang.find;dojo.lang.findLast = function( array,  value,  identity){return dojo.lang.find(array, value, identity, true);}
dojo.lang.lastIndexOf = dojo.lang.findLast;dojo.lang.inArray = function(array , value ){return dojo.lang.find(array, value) > -1;}
dojo.lang.isObject = function( it){if(typeof it == "undefined"){ return false; }
return (typeof it == "object" || it === null || dojo.lang.isArray(it) || dojo.lang.isFunction(it));}
dojo.lang.isArray = function( it){return (it && it instanceof Array || typeof it == "array");}
dojo.lang.isArrayLike = function( it){if((!it)||(dojo.lang.isUndefined(it))){ return false; }
if(dojo.lang.isString(it)){ return false; }
if(dojo.lang.isFunction(it)){ return false; }
if(dojo.lang.isArray(it)){ return true; }
if((it.tagName)&&(it.tagName.toLowerCase()=='form')){ return false; }
if(dojo.lang.isNumber(it.length) && isFinite(it.length)){ return true; }
return false;}
dojo.lang.isFunction = function( it){return (it instanceof Function || typeof it == "function");};(function(){if((dojo.render.html.capable)&&(dojo.render.html["safari"])){dojo.lang.isFunction = function( it){if((typeof(it) == "function") && (it == "[object NodeList]")) { return false; }
return (it instanceof Function || typeof it == "function");}}})();dojo.lang.isString = function( it){return (typeof it == "string" || it instanceof String);}
dojo.lang.isAlien = function( it){if(!it){ return false; }
return !dojo.lang.isFunction(it) && /\{\s*\[native code\]\s*\}/.test(String(it));}
dojo.lang.isBoolean = function( it){return (it instanceof Boolean || typeof it == "boolean");}
dojo.lang.isNumber = function( it){return (it instanceof Number || typeof it == "number");}
dojo.lang.isUndefined = function( it){return ((typeof(it) == "undefined")&&(it == undefined));}
dojo.provide("dojo.lang.array");dojo.lang.mixin(dojo.lang, {has: function(obj, name){try{return typeof obj[name] != "undefined";}catch(e){ return false; }},isEmpty: function(obj){if(dojo.lang.isObject(obj)){var tmp = {};var count = 0;for(var x in obj){if(obj[x] && (!tmp[x])){count++;break;}}
return count == 0;}else if(dojo.lang.isArrayLike(obj) || dojo.lang.isString(obj)){return obj.length == 0;}},map: function(arr, obj, unary_func){var isString = dojo.lang.isString(arr);if(isString){arr = arr.split("");}
if(dojo.lang.isFunction(obj)&&(!unary_func)){unary_func = obj;obj = dj_global;}else if(dojo.lang.isFunction(obj) && unary_func){var tmpObj = obj;obj = unary_func;unary_func = tmpObj;}
if(Array.map){var outArr = Array.map(arr, unary_func, obj);}else{var outArr = [];for(var i=0;i<arr.length;++i){outArr.push(unary_func.call(obj, arr[i]));}}
if(isString) {return outArr.join("");} else {return outArr;}},reduce: function(arr, initialValue, obj, binary_func){var reducedValue = initialValue;var ob = obj ? obj : dj_global;dojo.lang.map(arr,function(val){reducedValue = binary_func.call(ob, reducedValue, val);}
);return reducedValue;},forEach: function(anArray, callback, thisObject){if(dojo.lang.isString(anArray)){anArray = anArray.split("");}
if(Array.forEach){Array.forEach(anArray, callback, thisObject);}else{if(!thisObject){thisObject=dj_global;}
for(var i=0,l=anArray.length; i<l; i++){callback.call(thisObject, anArray[i], i, anArray);}}},_everyOrSome: function(every, arr, callback, thisObject){if(dojo.lang.isString(arr)){arr = arr.split("");}
if(Array.every){return Array[ every ? "every" : "some" ](arr, callback, thisObject);}else{if(!thisObject){thisObject = dj_global;}
for(var i=0,l=arr.length; i<l; i++){var result = callback.call(thisObject, arr[i], i, arr);if(every && !result){return false;}else if((!every)&&(result)){return true;}}
return Boolean(every);}},every: function(arr, callback, thisObject){return this._everyOrSome(true, arr, callback, thisObject);},some: function(arr, callback, thisObject){return this._everyOrSome(false, arr, callback, thisObject);},filter: function(arr, callback, thisObject){var isString = dojo.lang.isString(arr);if(isString){ arr = arr.split(""); }
var outArr;if(Array.filter){outArr = Array.filter(arr, callback, thisObject);} else {if(!thisObject){if(arguments.length >= 3){ dojo.raise("thisObject doesn't exist!"); }
thisObject = dj_global;}
outArr = [];for(var i = 0; i < arr.length; i++){if(callback.call(thisObject, arr[i], i, arr)){outArr.push(arr[i]);}}}
if(isString){return outArr.join("");} else {return outArr;}},unnest: function(){var out = [];for(var i = 0; i < arguments.length; i++){if(dojo.lang.isArrayLike(arguments[i])){var add = dojo.lang.unnest.apply(this, arguments[i]);out = out.concat(add);}else{out.push(arguments[i]);}}
return out;},toArray: function(arrayLike, startOffset){var array = [];for(var i = startOffset||0; i < arrayLike.length; i++){array.push(arrayLike[i]);}
return array;}});dojo.provide("dojo.lang.extras");dojo.lang.setTimeout = function(func, delay ){var context = window, argsStart = 2;if(!dojo.lang.isFunction(func)){context = func;func = delay;delay = arguments[2];argsStart++;}
if(dojo.lang.isString(func)){func = context[func];}
var args = [];for (var i = argsStart; i < arguments.length; i++){args.push(arguments[i]);}
return dojo.global().setTimeout(function () { func.apply(context, args); }, delay);}
dojo.lang.clearTimeout = function(timer){dojo.global().clearTimeout(timer);}
dojo.lang.getNameInObj = function(ns, item){if(!ns){ ns = dj_global; }
for(var x in ns){if(ns[x] === item){return new String(x);}}
return null;}
dojo.lang.shallowCopy = function(obj, deep){var i, ret;if(obj === null){  return null; }
if(dojo.lang.isObject(obj)){ret = new obj.constructor();for(i in obj){if(dojo.lang.isUndefined(ret[i])){ret[i] = deep ? dojo.lang.shallowCopy(obj[i], deep) : obj[i];}}} else if(dojo.lang.isArray(obj)){ret = [];for(i=0; i<obj.length; i++){ret[i] = deep ? dojo.lang.shallowCopy(obj[i], deep) : obj[i];}} else {ret = obj;}
return ret;}
dojo.lang.firstValued = function(){for(var i = 0; i < arguments.length; i++){if(typeof arguments[i] != "undefined"){return arguments[i];}}
return undefined;}
dojo.lang.getObjPathValue = function(objpath, context, create){with(dojo.parseObjPath(objpath, context, create)){return dojo.evalProp(prop, obj, create);}}
dojo.lang.setObjPathValue = function(objpath, value, context, create){if(arguments.length < 4){create = true;}
with(dojo.parseObjPath(objpath, context, create)){if(obj && (create || (prop in obj))){obj[prop] = value;}}}
dojo.provide("dojo.lang.func");dojo.lang.hitch = function(thisObject, method){var fcn = (dojo.lang.isString(method) ? thisObject[method] : method) || function(){};return function() {return fcn.apply(thisObject, arguments);};}
dojo.lang.anonCtr = 0;dojo.lang.anon = {};dojo.lang.nameAnonFunc = function(anonFuncPtr, namespaceObj, searchForNames){var nso = (namespaceObj || dojo.lang.anon);if( (searchForNames) ||
((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"] == true)) ){for(var x in nso){try{if(nso[x] === anonFuncPtr){return x;}}catch(e){}}}
var ret = "__"+dojo.lang.anonCtr++;while(typeof nso[ret] != "undefined"){ret = "__"+dojo.lang.anonCtr++;}
nso[ret] = anonFuncPtr;return ret;}
dojo.lang.forward = function(funcName){return function(){return this[funcName].apply(this, arguments);};}
dojo.lang.curry = function(ns, func ){var outerArgs = [];ns = ns||dj_global;if(dojo.lang.isString(func)){func = ns[func];}
for(var x=2; x<arguments.length; x++){outerArgs.push(arguments[x]);}
var ecount = (func["__preJoinArity"]||func.length) - outerArgs.length;function gather(nextArgs, innerArgs, expected){var texpected = expected;var totalArgs = innerArgs.slice(0);for(var x=0; x<nextArgs.length; x++){totalArgs.push(nextArgs[x]);}
expected = expected-nextArgs.length;if(expected<=0){var res = func.apply(ns, totalArgs);expected = texpected;return res;}else{return function(){return gather(arguments,totalArgs,expected);};}}
return gather([], outerArgs, ecount);}
dojo.lang.curryArguments = function(ns, func, args, offset){var targs = [];var x = offset||0;for(x=offset; x<args.length; x++){targs.push(args[x]);}
return dojo.lang.curry.apply(dojo.lang, [ns, func].concat(targs));}
dojo.lang.tryThese = function(){for(var x=0; x<arguments.length; x++){try{if(typeof arguments[x] == "function"){var ret = (arguments[x]());if(ret){return ret;}}}catch(e){dojo.debug(e);}}}
dojo.lang.delayThese = function(farr, cb, delay, onend){if(!farr.length){if(typeof onend == "function"){onend();}
return;}
if((typeof delay == "undefined")&&(typeof cb == "number")){delay = cb;cb = function(){};}else if(!cb){cb = function(){};if(!delay){ delay = 0; }}
setTimeout(function(){(farr.shift())();cb();dojo.lang.delayThese(farr, cb, delay, onend);}, delay);}
dojo.provide("dojo.event.common");dojo.event = new function(){this._canTimeout = dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);function interpolateArgs(args, searchForNames){var dl = dojo.lang;var ao = {srcObj: dj_global,srcFunc: null,adviceObj: dj_global,adviceFunc: null,aroundObj: null,aroundFunc: null,adviceType: (args.length>2) ? args[0] : "after",precedence: "last",once: false,delay: null,rate: 0,adviceMsg: false,maxCalls: -1};switch(args.length){case 0: return;case 1: return;case 2:
ao.srcFunc = args[0];ao.adviceFunc = args[1];break;case 3:
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isString(args[2]))){ao.adviceType = "after";ao.srcObj = args[0];ao.srcFunc = args[1];ao.adviceFunc = args[2];}else if((dl.isString(args[1]))&&(dl.isString(args[2]))){ao.srcFunc = args[1];ao.adviceFunc = args[2];}else if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isFunction(args[2]))){ao.adviceType = "after";ao.srcObj = args[0];ao.srcFunc = args[1];var tmpName  = dl.nameAnonFunc(args[2], ao.adviceObj, searchForNames);ao.adviceFunc = tmpName;}else if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){ao.adviceType = "after";ao.srcObj = dj_global;var tmpName  = dl.nameAnonFunc(args[0], ao.srcObj, searchForNames);ao.srcFunc = tmpName;ao.adviceObj = args[1];ao.adviceFunc = args[2];}
break;case 4:
if((dl.isObject(args[0]))&&(dl.isObject(args[2]))){ao.adviceType = "after";ao.srcObj = args[0];ao.srcFunc = args[1];ao.adviceObj = args[2];ao.adviceFunc = args[3];}else if((dl.isString(args[0]))&&(dl.isString(args[1]))&&(dl.isObject(args[2]))){ao.adviceType = args[0];ao.srcObj = dj_global;ao.srcFunc = args[1];ao.adviceObj = args[2];ao.adviceFunc = args[3];}else if((dl.isString(args[0]))&&(dl.isFunction(args[1]))&&(dl.isObject(args[2]))){ao.adviceType = args[0];ao.srcObj = dj_global;var tmpName  = dl.nameAnonFunc(args[1], dj_global, searchForNames);ao.srcFunc = tmpName;ao.adviceObj = args[2];ao.adviceFunc = args[3];}else if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){ao.srcObj = args[1];ao.srcFunc = args[2];var tmpName  = dl.nameAnonFunc(args[3], dj_global, searchForNames);ao.adviceObj = dj_global;ao.adviceFunc = tmpName;}else if(dl.isObject(args[1])){ao.srcObj = args[1];ao.srcFunc = args[2];ao.adviceObj = dj_global;ao.adviceFunc = args[3];}else if(dl.isObject(args[2])){ao.srcObj = dj_global;ao.srcFunc = args[1];ao.adviceObj = args[2];ao.adviceFunc = args[3];}else{ao.srcObj = ao.adviceObj = ao.aroundObj = dj_global;ao.srcFunc = args[1];ao.adviceFunc = args[2];ao.aroundFunc = args[3];}
break;case 6:
ao.srcObj = args[1];ao.srcFunc = args[2];ao.adviceObj = args[3]
ao.adviceFunc = args[4];ao.aroundFunc = args[5];ao.aroundObj = dj_global;break;default:
ao.srcObj = args[1];ao.srcFunc = args[2];ao.adviceObj = args[3]
ao.adviceFunc = args[4];ao.aroundObj = args[5];ao.aroundFunc = args[6];ao.once = args[7];ao.delay = args[8];ao.rate = args[9];ao.adviceMsg = args[10];ao.maxCalls = (!isNaN(parseInt(args[11]))) ? args[11] : -1;break;}
if(dl.isFunction(ao.aroundFunc)){var tmpName  = dl.nameAnonFunc(ao.aroundFunc, ao.aroundObj, searchForNames);ao.aroundFunc = tmpName;}
if(dl.isFunction(ao.srcFunc)){ao.srcFunc = dl.getNameInObj(ao.srcObj, ao.srcFunc);}
if(dl.isFunction(ao.adviceFunc)){ao.adviceFunc = dl.getNameInObj(ao.adviceObj, ao.adviceFunc);}
if((ao.aroundObj)&&(dl.isFunction(ao.aroundFunc))){ao.aroundFunc = dl.getNameInObj(ao.aroundObj, ao.aroundFunc);}
if(!ao.srcObj){dojo.raise("bad srcObj for srcFunc: "+ao.srcFunc);}
if(!ao.adviceObj){dojo.raise("bad adviceObj for adviceFunc: "+ao.adviceFunc);}
if(!ao.adviceFunc){dojo.debug("bad adviceFunc for srcFunc: "+ao.srcFunc);dojo.debugShallow(ao);}
return ao;}
this.connect = function(){if(arguments.length == 1){var ao = arguments[0];}else{var ao = interpolateArgs(arguments, true);}
if(dojo.lang.isString(ao.srcFunc) && (ao.srcFunc.toLowerCase() == "onkey") ){if(dojo.render.html.ie){ao.srcFunc = "onkeydown";this.connect(ao);}
ao.srcFunc = "onkeypress";}
if(dojo.lang.isArray(ao.srcObj) && ao.srcObj!=""){var tmpAO = {};for(var x in ao){tmpAO[x] = ao[x];}
var mjps = [];dojo.lang.forEach(ao.srcObj, function(src){if((dojo.render.html.capable)&&(dojo.lang.isString(src))){src = dojo.byId(src);}
tmpAO.srcObj = src;mjps.push(dojo.event.connect.call(dojo.event, tmpAO));});return mjps;}
var mjp = dojo.event.MethodJoinPoint.getForMethod(ao.srcObj, ao.srcFunc);if(ao.adviceFunc){var mjp2 = dojo.event.MethodJoinPoint.getForMethod(ao.adviceObj, ao.adviceFunc);}
mjp.kwAddAdvice(ao);return mjp;}
this.log = function( a1,  a2){var kwArgs;if((arguments.length == 1)&&(typeof a1 == "object")){kwArgs = a1;}else{kwArgs = {srcObj: a1,srcFunc: a2};}
kwArgs.adviceFunc = function(){var argsStr = [];for(var x=0; x<arguments.length; x++){argsStr.push(arguments[x]);}
dojo.debug("("+kwArgs.srcObj+")."+kwArgs.srcFunc, ":", argsStr.join(", "));}
this.kwConnect(kwArgs);}
this.connectBefore = function(){var args = ["before"];for(var i = 0; i < arguments.length; i++){ args.push(arguments[i]); }
return this.connect.apply(this, args);}
this.connectAround = function(){var args = ["around"];for(var i = 0; i < arguments.length; i++){ args.push(arguments[i]); }
return this.connect.apply(this, args);}
this.connectOnce = function(){var ao = interpolateArgs(arguments, true);ao.once = true;return this.connect(ao);}
this.connectRunOnce = function(){var ao = interpolateArgs(arguments, true);ao.maxCalls = 1;return this.connect(ao);}
this._kwConnectImpl = function(kwArgs, disconnect){var fn = (disconnect) ? "disconnect" : "connect";if(typeof kwArgs["srcFunc"] == "function"){kwArgs.srcObj = kwArgs["srcObj"]||dj_global;var tmpName  = dojo.lang.nameAnonFunc(kwArgs.srcFunc, kwArgs.srcObj, true);kwArgs.srcFunc = tmpName;}
if(typeof kwArgs["adviceFunc"] == "function"){kwArgs.adviceObj = kwArgs["adviceObj"]||dj_global;var tmpName  = dojo.lang.nameAnonFunc(kwArgs.adviceFunc, kwArgs.adviceObj, true);kwArgs.adviceFunc = tmpName;}
kwArgs.srcObj = kwArgs["srcObj"]||dj_global;kwArgs.adviceObj = kwArgs["adviceObj"]||kwArgs["targetObj"]||dj_global;kwArgs.adviceFunc = kwArgs["adviceFunc"]||kwArgs["targetFunc"];return dojo.event[fn](kwArgs);}
this.kwConnect = function( kwArgs){return this._kwConnectImpl(kwArgs, false);}
this.disconnect = function(){if(arguments.length == 1){var ao = arguments[0];}else{var ao = interpolateArgs(arguments, true);}
if(!ao.adviceFunc){ return; }
if(dojo.lang.isString(ao.srcFunc) && (ao.srcFunc.toLowerCase() == "onkey") ){if(dojo.render.html.ie){ao.srcFunc = "onkeydown";this.disconnect(ao);}
ao.srcFunc = "onkeypress";}
if(!ao.srcObj[ao.srcFunc]){ return null; }
var mjp = dojo.event.MethodJoinPoint.getForMethod(ao.srcObj, ao.srcFunc, true);mjp.removeAdvice(ao.adviceObj, ao.adviceFunc, ao.adviceType, ao.once);return mjp;}
this.kwDisconnect = function(kwArgs){return this._kwConnectImpl(kwArgs, true);}}
dojo.event.MethodInvocation = function(join_point, obj, args){this.jp_ = join_point;this.object = obj;this.args = [];for(var x=0; x<args.length; x++){this.args[x] = args[x];}
this.around_index = -1;}
dojo.event.MethodInvocation.prototype.proceed = function(){this.around_index++;if(this.around_index >= this.jp_.around.length){return this.jp_.object[this.jp_.methodname].apply(this.jp_.object, this.args);}else{var ti = this.jp_.around[this.around_index];var mobj = ti[0]||dj_global;var meth = ti[1];return mobj[meth].call(mobj, this);}}
dojo.event.MethodJoinPoint = function(obj, funcName){this.object = obj||dj_global;this.methodname = funcName;this.methodfunc = this.object[funcName];}
dojo.event.MethodJoinPoint.getForMethod = function(obj, funcName){if(!obj){ obj = dj_global; }
var ofn = obj[funcName];if(!ofn){ofn = obj[funcName] = function(){};if(!obj[funcName]){dojo.raise("Cannot set do-nothing method on that object "+funcName);}}else if((typeof ofn != "function")&&(!dojo.lang.isFunction(ofn))&&(!dojo.lang.isAlien(ofn))){return null;}
var jpname = funcName + "$joinpoint";var jpfuncname = funcName + "$joinpoint$method";var joinpoint = obj[jpname];if(!joinpoint){var isNode = false;if(dojo.event["browser"]){if( (obj["attachEvent"])||
(obj["nodeType"])||
(obj["addEventListener"]) ){isNode = true;dojo.event.browser.addClobberNodeAttrs(obj, [jpname, jpfuncname, funcName]);}}
var origArity = ofn.length;obj[jpfuncname] = ofn;joinpoint = obj[jpname] = new dojo.event.MethodJoinPoint(obj, jpfuncname);if(!isNode){obj[funcName] = function(){return joinpoint.run.apply(joinpoint, arguments);}}else{obj[funcName] = function(){var args = [];if(!arguments.length){var evt = null;try{if(obj.ownerDocument){evt = obj.ownerDocument.parentWindow.event;}else if(obj.documentElement){evt = obj.documentElement.ownerDocument.parentWindow.event;}else if(obj.event){evt = obj.event;}else{evt = window.event;}}catch(e){evt = window.event;}
if(evt){args.push(dojo.event.browser.fixEvent(evt, this));}}else{for(var x=0; x<arguments.length; x++){if((x==0)&&(dojo.event.browser.isEvent(arguments[x]))){args.push(dojo.event.browser.fixEvent(arguments[x], this));}else{args.push(arguments[x]);}}}
return joinpoint.run.apply(joinpoint, args);}}
obj[funcName].__preJoinArity = origArity;}
return joinpoint;}
dojo.lang.extend(dojo.event.MethodJoinPoint, {squelch: false,unintercept: function(){this.object[this.methodname] = this.methodfunc;this.before = [];this.after = [];this.around = [];},disconnect: dojo.lang.forward("unintercept"),run: function(){var obj = this.object||dj_global;var args = arguments;var aargs = [];for(var x=0; x<args.length; x++){aargs[x] = args[x];}
var unrollAdvice  = function(marr){if(!marr){dojo.debug("Null argument to unrollAdvice()");return;}
var callObj = marr[0]||dj_global;var callFunc = marr[1];if(!callObj[callFunc]){dojo.raise("function \"" + callFunc + "\" does not exist on \"" + callObj + "\"");}
var aroundObj = marr[2]||dj_global;var aroundFunc = marr[3];var msg = marr[6];var maxCount = marr[7];if(maxCount > -1){if(maxCount == 0){return;}
marr[7]--;}
var undef;var to = {args: [],jp_: this,object: obj,proceed: function(){return callObj[callFunc].apply(callObj, to.args);}};to.args = aargs;var delay = parseInt(marr[4]);var hasDelay = ((!isNaN(delay))&&(marr[4]!==null)&&(typeof marr[4] != "undefined"));if(marr[5]){var rate = parseInt(marr[5]);var cur = new Date();var timerSet = false;if((marr["last"])&&((cur-marr.last)<=rate)){if(dojo.event._canTimeout){if(marr["delayTimer"]){clearTimeout(marr.delayTimer);}
var tod = parseInt(rate*2);var mcpy = dojo.lang.shallowCopy(marr);marr.delayTimer = setTimeout(function(){mcpy[5] = 0;unrollAdvice(mcpy);}, tod);}
return;}else{marr.last = cur;}}
if(aroundFunc){aroundObj[aroundFunc].call(aroundObj, to);}else{if((hasDelay)&&((dojo.render.html)||(dojo.render.svg))){dj_global["setTimeout"](function(){if(msg){callObj[callFunc].call(callObj, to);}else{callObj[callFunc].apply(callObj, args);}}, delay);}else{if(msg){callObj[callFunc].call(callObj, to);}else{callObj[callFunc].apply(callObj, args);}}}}
var unRollSquelch = function(){if(this.squelch){try{return unrollAdvice.apply(this, arguments);}catch(e){dojo.debug(e);}}else{return unrollAdvice.apply(this, arguments);}}
if((this["before"])&&(this.before.length>0)){dojo.lang.forEach(this.before.concat(new Array()), unRollSquelch);}
var result;try{if((this["around"])&&(this.around.length>0)){var mi = new dojo.event.MethodInvocation(this, obj, args);result = mi.proceed();}else if(this.methodfunc){result = this.object[this.methodname].apply(this.object, args);}}catch(e){if(!this.squelch){dojo.debug(e,"when calling",this.methodname,"on",this.object,"with arguments",args);dojo.raise(e);}}
if((this["after"])&&(this.after.length>0)){dojo.lang.forEach(this.after.concat(new Array()), unRollSquelch);}
return (this.methodfunc) ? result : null;},getArr: function(kind){var type = "after";if((typeof kind == "string")&&(kind.indexOf("before")!=-1)){type = "before";}else if(kind=="around"){type = "around";}
if(!this[type]){ this[type] = []; }
return this[type];},kwAddAdvice: function(args){this.addAdvice(	args["adviceObj"], args["adviceFunc"],args["aroundObj"], args["aroundFunc"],args["adviceType"], args["precedence"],args["once"], args["delay"], args["rate"],args["adviceMsg"], args["maxCalls"]);},addAdvice: function(	thisAdviceObj, thisAdvice,thisAroundObj, thisAround,adviceType, precedence,once, delay, rate, asMessage,maxCalls){var arr = this.getArr(adviceType);if(!arr){dojo.raise("bad this: " + this);}
var ao = [thisAdviceObj, thisAdvice, thisAroundObj, thisAround, delay, rate, asMessage, maxCalls];if(once){if(this.hasAdvice(thisAdviceObj, thisAdvice, adviceType, arr) >= 0){return;}}
if(precedence == "first"){arr.unshift(ao);}else{arr.push(ao);}},hasAdvice: function(thisAdviceObj, thisAdvice, adviceType, arr){if(!arr){ arr = this.getArr(adviceType); }
var ind = -1;for(var x=0; x<arr.length; x++){var aao = (typeof thisAdvice == "object") ? (new String(thisAdvice)).toString() : thisAdvice;var a1o = (typeof arr[x][1] == "object") ? (new String(arr[x][1])).toString() : arr[x][1];if((arr[x][0] == thisAdviceObj)&&(a1o == aao)){ind = x;}}
return ind;},removeAdvice: function(thisAdviceObj, thisAdvice, adviceType, once){var arr = this.getArr(adviceType);var ind = this.hasAdvice(thisAdviceObj, thisAdvice, adviceType, arr);if(ind == -1){return false;}
while(ind != -1){arr.splice(ind, 1);if(once){ break; }
ind = this.hasAdvice(thisAdviceObj, thisAdvice, adviceType, arr);}
return true;}});dojo.provide("dojo.event.browser");dojo._ie_clobber = new function(){this.clobberNodes = [];function nukeProp(node, prop){try{ node[prop] = null; 			}catch(e){  }
try{ delete node[prop]; 			}catch(e){  }
try{ node.removeAttribute(prop);	}catch(e){  }}
this.clobber = function(nodeRef){var na;var tna;if(nodeRef){tna = nodeRef.all || nodeRef.getElementsByTagName("*");na = [nodeRef];for(var x=0; x<tna.length; x++){if(tna[x]["__doClobber__"]){na.push(tna[x]);}}}else{try{ window.onload = null; }catch(e){}
na = (this.clobberNodes.length) ? this.clobberNodes : document.all;}
tna = null;var basis = {};for(var i = na.length-1; i>=0; i=i-1){var el = na[i];try{if(el && el["__clobberAttrs__"]){for(var j=0; j<el.__clobberAttrs__.length; j++){nukeProp(el, el.__clobberAttrs__[j]);}
nukeProp(el, "__clobberAttrs__");nukeProp(el, "__doClobber__");}}catch(e){ };}
na = null;}}
if(dojo.render.html.ie){dojo.addOnUnload(function(){dojo._ie_clobber.clobber();try{if((dojo["widget"])&&(dojo.widget["manager"])){dojo.widget.manager.destroyAll();}}catch(e){}
if(dojo.widget){for(var name in dojo.widget._templateCache){if(dojo.widget._templateCache[name].node){dojo.dom.removeNode(dojo.widget._templateCache[name].node);dojo.widget._templateCache[name].node = null;delete dojo.widget._templateCache[name].node;}}}
if(dojo.dom){while (dojo.dom._ieRemovedNodes.length > 0) {var node = dojo.dom._ieRemovedNodes.pop();dojo.dom._discardElement(node);node = null;}}
try{ window.onload = null; }catch(e){}
try{ window.onunload = null; }catch(e){}
dojo._ie_clobber.clobberNodes = [];});}
dojo.event.browser = new function(){var clobberIdx = 0;this.normalizedEventName = function(eventName){switch(eventName){case "CheckboxStateChange":
case "DOMAttrModified":
case "DOMMenuItemActive":
case "DOMMenuItemInactive":
case "DOMMouseScroll":
case "DOMNodeInserted":
case "DOMNodeRemoved":
case "RadioStateChange":
return eventName;break;default:
return eventName.toLowerCase();break;}}
this.clean = function(node){if(dojo.render.html.ie){dojo._ie_clobber.clobber(node);}}
this.addClobberNode = function(node){if(!dojo.render.html.ie){ return; }
if(!node["__doClobber__"]){node.__doClobber__ = true;dojo._ie_clobber.clobberNodes.push(node);node.__clobberAttrs__ = [];}}
this.addClobberNodeAttrs = function(node, props){if(!dojo.render.html.ie){ return; }
this.addClobberNode(node);for(var x=0; x<props.length; x++){node.__clobberAttrs__.push(props[x]);}}
this.removeListener = function(	 node,evtName,fp,capture){if(!capture){ var capture = false; }
evtName = dojo.event.browser.normalizedEventName(evtName);if( (evtName == "onkey") || (evtName == "key") ){if(dojo.render.html.ie){this.removeListener(node, "onkeydown", fp, capture);}
evtName = "onkeypress";}
if(evtName.substr(0,2)=="on"){ evtName = evtName.substr(2); }
if(node.removeEventListener){node.removeEventListener(evtName, fp, capture);}}
this.addListener = function(node, evtName, fp, capture, dontFix){if(!node){ return; }
if(!capture){ var capture = false; }
evtName = dojo.event.browser.normalizedEventName(evtName);if( (evtName == "onkey") || (evtName == "key") ){if(dojo.render.html.ie){this.addListener(node, "onkeydown", fp, capture, dontFix);}
evtName = "onkeypress";}
if(evtName.substr(0,2)!="on"){ evtName = "on"+evtName; }
if(!dontFix){var newfp = function(evt){if(!evt){ evt = window.event; }
var ret = fp(dojo.event.browser.fixEvent(evt, this));if(capture){dojo.event.browser.stopEvent(evt);}
return ret;}}else{newfp = fp;}
if(node.addEventListener){node.addEventListener(evtName.substr(2), newfp, capture);return newfp;}else{if(typeof node[evtName] == "function" ){var oldEvt = node[evtName];node[evtName] = function(e){oldEvt(e);return newfp(e);}}else{node[evtName]=newfp;}
if(dojo.render.html.ie){this.addClobberNodeAttrs(node, [evtName]);}
return newfp;}}
this.isEvent = function(obj){return (typeof obj != "undefined")&&(typeof Event != "undefined")&&(obj.eventPhase);}
this.currentEvent = null;this.callListener = function(listener, curTarget){if(typeof listener != 'function'){dojo.raise("listener not a function: " + listener);}
dojo.event.browser.currentEvent.currentTarget = curTarget;return listener.call(curTarget, dojo.event.browser.currentEvent);}
this._stopPropagation = function(){dojo.event.browser.currentEvent.cancelBubble = true;}
this._preventDefault = function(){dojo.event.browser.currentEvent.returnValue = false;}
this.keys = {KEY_BACKSPACE: 8,KEY_TAB: 9,KEY_CLEAR: 12,KEY_ENTER: 13,KEY_SHIFT: 16,KEY_CTRL: 17,KEY_ALT: 18,KEY_PAUSE: 19,KEY_CAPS_LOCK: 20,KEY_ESCAPE: 27,KEY_SPACE: 32,KEY_PAGE_UP: 33,KEY_PAGE_DOWN: 34,KEY_END: 35,KEY_HOME: 36,KEY_LEFT_ARROW: 37,KEY_UP_ARROW: 38,KEY_RIGHT_ARROW: 39,KEY_DOWN_ARROW: 40,KEY_INSERT: 45,KEY_DELETE: 46,KEY_HELP: 47,KEY_LEFT_WINDOW: 91,KEY_RIGHT_WINDOW: 92,KEY_SELECT: 93,KEY_NUMPAD_0: 96,KEY_NUMPAD_1: 97,KEY_NUMPAD_2: 98,KEY_NUMPAD_3: 99,KEY_NUMPAD_4: 100,KEY_NUMPAD_5: 101,KEY_NUMPAD_6: 102,KEY_NUMPAD_7: 103,KEY_NUMPAD_8: 104,KEY_NUMPAD_9: 105,KEY_NUMPAD_MULTIPLY: 106,KEY_NUMPAD_PLUS: 107,KEY_NUMPAD_ENTER: 108,KEY_NUMPAD_MINUS: 109,KEY_NUMPAD_PERIOD: 110,KEY_NUMPAD_DIVIDE: 111,KEY_F1: 112,KEY_F2: 113,KEY_F3: 114,KEY_F4: 115,KEY_F5: 116,KEY_F6: 117,KEY_F7: 118,KEY_F8: 119,KEY_F9: 120,KEY_F10: 121,KEY_F11: 122,KEY_F12: 123,KEY_F13: 124,KEY_F14: 125,KEY_F15: 126,KEY_NUM_LOCK: 144,KEY_SCROLL_LOCK: 145};this.revKeys = [];for(var key in this.keys){this.revKeys[this.keys[key]] = key;}
this.fixEvent = function(evt, sender){if(!evt){if(window["event"]){evt = window.event;}}
if((evt["type"])&&(evt["type"].indexOf("key") == 0)){evt.keys = this.revKeys;for(var key in this.keys){evt[key] = this.keys[key];}
if(evt["type"] == "keydown" && dojo.render.html.ie){switch(evt.keyCode){case evt.KEY_SHIFT:
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
break;case evt.KEY_NUMPAD_MULTIPLY:
case evt.KEY_NUMPAD_PLUS:
case evt.KEY_NUMPAD_ENTER:
case evt.KEY_NUMPAD_MINUS:
case evt.KEY_NUMPAD_DIVIDE:
break;case evt.KEY_PAUSE:
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
evt.key = evt.keyCode;break;default:
if(evt.ctrlKey || evt.altKey){var unifiedCharCode = evt.keyCode;if(unifiedCharCode >= 65 && unifiedCharCode <= 90 && evt.shiftKey == false){unifiedCharCode += 32;}
if(unifiedCharCode >= 1 && unifiedCharCode <= 26 && evt.ctrlKey){unifiedCharCode += 96;}
evt.key = String.fromCharCode(unifiedCharCode);}}} else if(evt["type"] == "keypress"){if(dojo.render.html.opera){if(evt.which == 0){evt.key = evt.keyCode;}else if(evt.which > 0){switch(evt.which){case evt.KEY_SHIFT:
case evt.KEY_CTRL:
case evt.KEY_ALT:
case evt.KEY_CAPS_LOCK:
case evt.KEY_NUM_LOCK:
case evt.KEY_SCROLL_LOCK:
break;case evt.KEY_PAUSE:
case evt.KEY_TAB:
case evt.KEY_BACKSPACE:
case evt.KEY_ENTER:
case evt.KEY_ESCAPE:
evt.key = evt.which;break;default:
var unifiedCharCode = evt.which;if((evt.ctrlKey || evt.altKey || evt.metaKey) && (evt.which >= 65 && evt.which <= 90 && evt.shiftKey == false)){unifiedCharCode += 32;}
evt.key = String.fromCharCode(unifiedCharCode);}}}else if(dojo.render.html.ie){if(!evt.ctrlKey && !evt.altKey && evt.keyCode >= evt.KEY_SPACE){evt.key = String.fromCharCode(evt.keyCode);}}else if(dojo.render.html.safari){switch(evt.keyCode){case 25: evt.key = evt.KEY_TAB; evt.shift = true;break;case 63232: evt.key = evt.KEY_UP_ARROW; break;case 63233: evt.key = evt.KEY_DOWN_ARROW; break;case 63234: evt.key = evt.KEY_LEFT_ARROW; break;case 63235: evt.key = evt.KEY_RIGHT_ARROW; break;case 63236: evt.key = evt.KEY_F1; break;case 63237: evt.key = evt.KEY_F2; break;case 63238: evt.key = evt.KEY_F3; break;case 63239: evt.key = evt.KEY_F4; break;case 63240: evt.key = evt.KEY_F5; break;case 63241: evt.key = evt.KEY_F6; break;case 63242: evt.key = evt.KEY_F7; break;case 63243: evt.key = evt.KEY_F8; break;case 63244: evt.key = evt.KEY_F9; break;case 63245: evt.key = evt.KEY_F10; break;case 63246: evt.key = evt.KEY_F11; break;case 63247: evt.key = evt.KEY_F12; break;case 63250: evt.key = evt.KEY_PAUSE; break;case 63272: evt.key = evt.KEY_DELETE; break;case 63273: evt.key = evt.KEY_HOME; break;case 63275: evt.key = evt.KEY_END; break;case 63276: evt.key = evt.KEY_PAGE_UP; break;case 63277: evt.key = evt.KEY_PAGE_DOWN; break;case 63302: evt.key = evt.KEY_INSERT; break;case 63248://prtscr
case 63249://scrolllock
case 63289://numlock
break;default:
evt.key = evt.charCode >= evt.KEY_SPACE ? String.fromCharCode(evt.charCode) : evt.keyCode;}}else{evt.key = evt.charCode > 0 ? String.fromCharCode(evt.charCode) : evt.keyCode;}}}
if(dojo.render.html.ie){if(!evt.target){ evt.target = evt.srcElement; }
if(!evt.currentTarget){ evt.currentTarget = (sender ? sender : evt.srcElement); }
if(!evt.layerX){ evt.layerX = evt.offsetX; }
if(!evt.layerY){ evt.layerY = evt.offsetY; }
var doc = (evt.srcElement && evt.srcElement.ownerDocument) ? evt.srcElement.ownerDocument : document;var docBody = ((dojo.render.html.ie55)||(doc["compatMode"] == "BackCompat")) ? doc.body : doc.documentElement;if(!evt.pageX){ evt.pageX = evt.clientX + (docBody.scrollLeft || 0) }
if(!evt.pageY){ evt.pageY = evt.clientY + (docBody.scrollTop || 0) }
if(evt.type == "mouseover"){ evt.relatedTarget = evt.fromElement; }
if(evt.type == "mouseout"){ evt.relatedTarget = evt.toElement; }
this.currentEvent = evt;evt.callListener = this.callListener;evt.stopPropagation = this._stopPropagation;evt.preventDefault = this._preventDefault;}
return evt;}
this.stopEvent = function(evt){if(window.event){evt.cancelBubble = true;evt.returnValue = false;}else{evt.preventDefault();evt.stopPropagation();}}}
dojo.provide("dojo.dom");dojo.dom.ELEMENT_NODE                  = 1;dojo.dom.ATTRIBUTE_NODE                = 2;dojo.dom.TEXT_NODE                     = 3;dojo.dom.CDATA_SECTION_NODE            = 4;dojo.dom.ENTITY_REFERENCE_NODE         = 5;dojo.dom.ENTITY_NODE                   = 6;dojo.dom.PROCESSING_INSTRUCTION_NODE   = 7;dojo.dom.COMMENT_NODE                  = 8;dojo.dom.DOCUMENT_NODE                 = 9;dojo.dom.DOCUMENT_TYPE_NODE            = 10;dojo.dom.DOCUMENT_FRAGMENT_NODE        = 11;dojo.dom.NOTATION_NODE                 = 12;dojo.dom.dojoml = "http://www.dojotoolkit.org/2004/dojoml";dojo.dom.xmlns = {svg : "http://www.w3.org/2000/svg",smil : "http://www.w3.org/2001/SMIL20/",mml : "http://www.w3.org/1998/Math/MathML",cml : "http://www.xml-cml.org",xlink : "http://www.w3.org/1999/xlink",xhtml : "http://www.w3.org/1999/xhtml",xul : "http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul",xbl : "http://www.mozilla.org/xbl",fo : "http://www.w3.org/1999/XSL/Format",xsl : "http://www.w3.org/1999/XSL/Transform",xslt : "http://www.w3.org/1999/XSL/Transform",xi : "http://www.w3.org/2001/XInclude",xforms : "http://www.w3.org/2002/01/xforms",saxon : "http://icl.com/saxon",xalan : "http://xml.apache.org/xslt",xsd : "http://www.w3.org/2001/XMLSchema",dt: "http://www.w3.org/2001/XMLSchema-datatypes",xsi : "http://www.w3.org/2001/XMLSchema-instance",rdf : "http://www.w3.org/1999/02/22-rdf-syntax-ns#",rdfs : "http://www.w3.org/2000/01/rdf-schema#",dc : "http://purl.org/dc/elements/1.1/",dcq: "http://purl.org/dc/qualifiers/1.0","soap-env" : "http://schemas.xmlsoap.org/soap/envelope/",wsdl : "http://schemas.xmlsoap.org/wsdl/",AdobeExtensions : "http://ns.adobe.com/AdobeSVGViewerExtensions/3.0/"};dojo.dom.isNode = function(wh){if(typeof Element == "function") {try {return wh instanceof Element;} catch(e) {}} else {return wh && !isNaN(wh.nodeType);}}
dojo.dom.getUniqueId = function(){var _document = dojo.doc();do {var id = "dj_unique_" + (++arguments.callee._idIncrement);}while(_document.getElementById(id));return id;}
dojo.dom.getUniqueId._idIncrement = 0;dojo.dom.firstElement = dojo.dom.getFirstChildElement = function(parentNode, tagName){var node = parentNode.firstChild;while(node && node.nodeType != dojo.dom.ELEMENT_NODE){node = node.nextSibling;}
if(tagName && node && node.tagName && node.tagName.toLowerCase() != tagName.toLowerCase()) {node = dojo.dom.nextElement(node, tagName);}
return node;}
dojo.dom.lastElement = dojo.dom.getLastChildElement = function(parentNode, tagName){var node = parentNode.lastChild;while(node && node.nodeType != dojo.dom.ELEMENT_NODE) {node = node.previousSibling;}
if(tagName && node && node.tagName && node.tagName.toLowerCase() != tagName.toLowerCase()) {node = dojo.dom.prevElement(node, tagName);}
return node;}
dojo.dom.nextElement = dojo.dom.getNextSiblingElement = function(node, tagName){if(!node) { return null; }
do {node = node.nextSibling;} while(node && node.nodeType != dojo.dom.ELEMENT_NODE);if(node && tagName && tagName.toLowerCase() != node.tagName.toLowerCase()) {return dojo.dom.nextElement(node, tagName);}
return node;}
dojo.dom.prevElement = dojo.dom.getPreviousSiblingElement = function(node, tagName){if(!node) { return null; }
if(tagName) { tagName = tagName.toLowerCase(); }
do {node = node.previousSibling;} while(node && node.nodeType != dojo.dom.ELEMENT_NODE);if(node && tagName && tagName.toLowerCase() != node.tagName.toLowerCase()) {return dojo.dom.prevElement(node, tagName);}
return node;}
dojo.dom.moveChildren = function(srcNode, destNode, trim){var count = 0;if(trim) {while(srcNode.hasChildNodes() &&
srcNode.firstChild.nodeType == dojo.dom.TEXT_NODE) {srcNode.removeChild(srcNode.firstChild);}
while(srcNode.hasChildNodes() &&
srcNode.lastChild.nodeType == dojo.dom.TEXT_NODE) {srcNode.removeChild(srcNode.lastChild);}}
while(srcNode.hasChildNodes()){destNode.appendChild(srcNode.firstChild);count++;}
return count;}
dojo.dom.copyChildren = function(srcNode, destNode, trim){var clonedNode = srcNode.cloneNode(true);return this.moveChildren(clonedNode, destNode, trim);}
dojo.dom.replaceChildren = function(node, newChild){dojo.dom.removeChildren(node);node.appendChild(newChild);}
dojo.dom.removeChildren = function(node){var count = node.childNodes.length;while(node.hasChildNodes()){ dojo.dom.removeNode(node.firstChild); }
return count;}
dojo.dom.replaceNode = function(node, newNode){if(dojo.render.html.ie){node.parentNode.insertBefore(newNode, node);return dojo.dom.removeNode(node);}else{return node.parentNode.replaceChild(newNode, node);}}
dojo.dom._ieRemovedNodes = [];dojo.dom.removeNode = function(node, clobber){if(node && node.parentNode){try{if(clobber && dojo.evalObjPath("dojo.event.browser.clean", false)){dojo.event.browser.clean(node);}}catch(e){  }
if(dojo.render.html.ie){if(clobber){dojo.dom._discardElement(node);}else{dojo.dom._ieRemovedNodes.push(node);}}
if(clobber){return null;}
return node.parentNode.removeChild(node);}}
dojo.dom._discardElement = function(element){var garbageBin = document.getElementById('IELeakGarbageBin');if (!garbageBin){garbageBin = document.createElement('DIV');garbageBin.id = 'IELeakGarbageBin';garbageBin.style.display = 'none';document.body.appendChild(garbageBin);}
garbageBin.appendChild(element);garbageBin.innerHTML = '';}
dojo.dom.getAncestors = function(node, filterFunction, returnFirstHit){var ancestors = [];var isFunction = (filterFunction && (filterFunction instanceof Function || typeof filterFunction == "function"));while(node){if(!isFunction || filterFunction(node)){ancestors.push(node);}
if(returnFirstHit && ancestors.length > 0){return ancestors[0];}
node = node.parentNode;}
if(returnFirstHit){ return null; }
return ancestors;}
dojo.dom.getAncestorsByTag = function(node, tag, returnFirstHit){tag = tag.toLowerCase();return dojo.dom.getAncestors(node, function(el){return ((el.tagName)&&(el.tagName.toLowerCase() == tag));}, returnFirstHit);}
dojo.dom.getFirstAncestorByTag = function(node, tag){return dojo.dom.getAncestorsByTag(node, tag, true);}
dojo.dom.isDescendantOf = function(node, ancestor, guaranteeDescendant){if(guaranteeDescendant && node) { node = node.parentNode; }
while(node) {if(node == ancestor){return true;}
node = node.parentNode;}
return false;}
dojo.dom.innerXML = function(node){if(node.innerXML){return node.innerXML;}else if (node.xml){return node.xml;}else if(typeof XMLSerializer != "undefined"){return (new XMLSerializer()).serializeToString(node);}}
dojo.dom.createDocument = function(){var doc = null;var _document = dojo.doc();if(!dj_undef("ActiveXObject")){var prefixes = [ "MSXML2", "Microsoft", "MSXML", "MSXML3" ];for(var i = 0; i<prefixes.length; i++){try{doc = new ActiveXObject(prefixes[i]+".XMLDOM");}catch(e){  };if(doc){ break; }}}else if((_document.implementation)&&
(_document.implementation.createDocument)){doc = _document.implementation.createDocument("", "", null);}
return doc;}
dojo.dom.createDocumentFromText = function(str, mimetype){if(!mimetype){ mimetype = "text/xml"; }
if(!dj_undef("DOMParser")){var parser = new DOMParser();return parser.parseFromString(str, mimetype);}else if(!dj_undef("ActiveXObject")){var domDoc = dojo.dom.createDocument();if(domDoc){domDoc.async = false;domDoc.loadXML(str);return domDoc;}else{dojo.debug("toXml didn't work?");}}else{var _document = dojo.doc();if(_document.createElement){var tmp = _document.createElement("xml");tmp.innerHTML = str;if(_document.implementation && _document.implementation.createDocument){var xmlDoc = _document.implementation.createDocument("foo", "", null);for(var i = 0; i < tmp.childNodes.length; i++) {xmlDoc.importNode(tmp.childNodes.item(i), true);}
return xmlDoc;}
return ((tmp.document)&&
(tmp.document.firstChild ?  tmp.document.firstChild : tmp));}}
return null;}
dojo.dom.prependChild = function(node, parent){if(parent.firstChild) {parent.insertBefore(node, parent.firstChild);} else {parent.appendChild(node);}
return true;}
dojo.dom.insertBefore = function(node, ref, force){if(	(force != true)&&
(node === ref || node.nextSibling === ref)){ return false; }
var parent = ref.parentNode;parent.insertBefore(node, ref);return true;}
dojo.dom.insertAfter = function(node, ref, force){var pn = ref.parentNode;if(ref == pn.lastChild){if((force != true)&&(node === ref)){return false;}
pn.appendChild(node);}else{return this.insertBefore(node, ref.nextSibling, force);}
return true;}
dojo.dom.insertAtPosition = function(node, ref, position){if((!node)||(!ref)||(!position)){return false;}
switch(position.toLowerCase()){case "before":
return dojo.dom.insertBefore(node, ref);case "after":
return dojo.dom.insertAfter(node, ref);case "first":
if(ref.firstChild){return dojo.dom.insertBefore(node, ref.firstChild);}else{ref.appendChild(node);return true;}
break;default:
ref.appendChild(node);return true;}}
dojo.dom.insertAtIndex = function(node, containingNode, insertionIndex){var siblingNodes = containingNode.childNodes;if (!siblingNodes.length){containingNode.appendChild(node);return true;}
var after = null;for(var i=0; i<siblingNodes.length; i++){var sibling_index = siblingNodes.item(i)["getAttribute"] ? parseInt(siblingNodes.item(i).getAttribute("dojoinsertionindex")) : -1;if (sibling_index < insertionIndex){after = siblingNodes.item(i);}}
if (after){return dojo.dom.insertAfter(node, after);}else{return dojo.dom.insertBefore(node, siblingNodes.item(0));}}
dojo.dom.textContent = function(node, text){if (arguments.length>1) {var _document = dojo.doc();dojo.dom.replaceChildren(node, _document.createTextNode(text));return text;} else {if(node.textContent != undefined){return node.textContent;}
var _result = "";if (node == null) { return _result; }
for (var i = 0; i < node.childNodes.length; i++) {switch (node.childNodes[i].nodeType) {case 1:
case 5:
_result += dojo.dom.textContent(node.childNodes[i]);break;case 3:
case 2:
case 4:
_result += node.childNodes[i].nodeValue;break;default:
break;}}
return _result;}}
dojo.dom.hasParent = function(node){return Boolean(node && node.parentNode && dojo.dom.isNode(node.parentNode));}
dojo.dom.isTag = function(node ){if(node && node.tagName) {for(var i=1; i<arguments.length; i++){if(node.tagName==String(arguments[i])){return String(arguments[i]);}}}
return "";}
dojo.dom.setAttributeNS = function(	elem, namespaceURI,attrName, attrValue){if(elem == null || ((elem == undefined)&&(typeof elem == "undefined"))){dojo.raise("No element given to dojo.dom.setAttributeNS");}
if(!((elem.setAttributeNS == undefined)&&(typeof elem.setAttributeNS == "undefined"))){elem.setAttributeNS(namespaceURI, attrName, attrValue);}else{var ownerDoc = elem.ownerDocument;var attribute = ownerDoc.createNode(
2,attrName,namespaceURI
);attribute.nodeValue = attrValue;elem.setAttributeNode(attribute);}}
dojo.provide("dojo.string.common");dojo.string.trim = function(str, wh){if(!str.replace){ return str; }
if(!str.length){ return str; }
var re = (wh > 0) ? (/^\s+/) : (wh < 0) ? (/\s+$/) : (/^\s+|\s+$/g);return str.replace(re, "");}
dojo.string.trimStart = function(str) {return dojo.string.trim(str, 1);}
dojo.string.trimEnd = function(str) {return dojo.string.trim(str, -1);}
dojo.string.repeat = function(str, count, separator) {var out = "";for(var i = 0; i < count; i++) {out += str;if(separator && i < count - 1) {out += separator;}}
return out;}
dojo.string.pad = function(str, len,  c, dir) {var out = String(str);if(!c) {c = '0';}
if(!dir) {dir = 1;}
while(out.length < len) {if(dir > 0) {out = c + out;} else {out += c;}}
return out;}
dojo.string.padLeft = function(str, len, c) {return dojo.string.pad(str, len, c, 1);}
dojo.string.padRight = function(str, len, c) {return dojo.string.pad(str, len, c, -1);}
dojo.provide("dojo.string");dojo.provide("dojo.io.common");dojo.io.transports = [];dojo.io.hdlrFuncNames = [ "load", "error", "timeout" ];dojo.io.Request = function( url,  mimetype,  transport,  changeUrl){if((arguments.length == 1)&&(arguments[0].constructor == Object)){this.fromKwArgs(arguments[0]);}else{this.url = url;if(mimetype){ this.mimetype = mimetype; }
if(transport){ this.transport = transport; }
if(arguments.length >= 4){ this.changeUrl = changeUrl; }}}
dojo.lang.extend(dojo.io.Request, {url: "",mimetype: "text/plain",method: "GET",content: undefined,transport: undefined,changeUrl: undefined,formNode: undefined,sync: false,bindSuccess: false,useCache: false,preventCache: false,load: function(type, data, transportImplementation, kwArgs){},error: function(type, error, transportImplementation, kwArgs){},timeout: function(type, empty, transportImplementation, kwArgs){},handle: function(type, data, transportImplementation, kwArgs){},timeoutSeconds: 0,abort: function(){ },fromKwArgs: function( kwArgs){if(kwArgs["url"]){ kwArgs.url = kwArgs.url.toString(); }
if(kwArgs["formNode"]) { kwArgs.formNode = dojo.byId(kwArgs.formNode); }
if(!kwArgs["method"] && kwArgs["formNode"] && kwArgs["formNode"].method) {kwArgs.method = kwArgs["formNode"].method;}
if(!kwArgs["handle"] && kwArgs["handler"]){ kwArgs.handle = kwArgs.handler; }
if(!kwArgs["load"] && kwArgs["loaded"]){ kwArgs.load = kwArgs.loaded; }
if(!kwArgs["changeUrl"] && kwArgs["changeURL"]) { kwArgs.changeUrl = kwArgs.changeURL; }
kwArgs.encoding = dojo.lang.firstValued(kwArgs["encoding"], djConfig["bindEncoding"], "");kwArgs.sendTransport = dojo.lang.firstValued(kwArgs["sendTransport"], djConfig["ioSendTransport"], false);var isFunction = dojo.lang.isFunction;for(var x=0; x<dojo.io.hdlrFuncNames.length; x++){var fn = dojo.io.hdlrFuncNames[x];if(kwArgs[fn] && isFunction(kwArgs[fn])){ continue; }
if(kwArgs["handle"] && isFunction(kwArgs["handle"])){kwArgs[fn] = kwArgs.handle;}}
dojo.lang.mixin(this, kwArgs);}});dojo.io.Error = function( msg,  type, num){this.message = msg;this.type =  type || "unknown";this.number = num || 0;}
dojo.io.transports.addTransport = function(name){this.push(name);this[name] = dojo.io[name];}
dojo.io.bind = function(request){if(!(request instanceof dojo.io.Request)){try{request = new dojo.io.Request(request);}catch(e){ dojo.debug(e); }}
var tsName = "";if(request["transport"]){tsName = request["transport"];if(!this[tsName]){dojo.io.sendBindError(request, "No dojo.io.bind() transport with name '"
+ request["transport"] + "'.");return request;}
if(!this[tsName].canHandle(request)){dojo.io.sendBindError(request, "dojo.io.bind() transport with name '"
+ request["transport"] + "' cannot handle this type of request.");return request;}}else{for(var x=0; x<dojo.io.transports.length; x++){var tmp = dojo.io.transports[x];if((this[tmp])&&(this[tmp].canHandle(request))){tsName = tmp;break;}}
if(tsName == ""){dojo.io.sendBindError(request, "None of the loaded transports for dojo.io.bind()"
+ " can handle the request.");return request;}}
this[tsName].bind(request);request.bindSuccess = true;return request;}
dojo.io.sendBindError = function(request, message){if((typeof request.error == "function" || typeof request.handle == "function")
&& (typeof setTimeout == "function" || typeof setTimeout == "object")){var errorObject = new dojo.io.Error(message);setTimeout(function(){request[(typeof request.error == "function") ? "error" : "handle"]("error", errorObject, null, request);}, 50);}else{dojo.raise(message);}}
dojo.io.queueBind = function(request){if(!(request instanceof dojo.io.Request)){try{request = new dojo.io.Request(request);}catch(e){ dojo.debug(e); }}
var oldLoad = request.load;request.load = function(){dojo.io._queueBindInFlight = false;var ret = oldLoad.apply(this, arguments);dojo.io._dispatchNextQueueBind();return ret;}
var oldErr = request.error;request.error = function(){dojo.io._queueBindInFlight = false;var ret = oldErr.apply(this, arguments);dojo.io._dispatchNextQueueBind();return ret;}
dojo.io._bindQueue.push(request);dojo.io._dispatchNextQueueBind();return request;}
dojo.io._dispatchNextQueueBind = function(){if(!dojo.io._queueBindInFlight){dojo.io._queueBindInFlight = true;if(dojo.io._bindQueue.length > 0){dojo.io.bind(dojo.io._bindQueue.shift());}else{dojo.io._queueBindInFlight = false;}}}
dojo.io._bindQueue = [];dojo.io._queueBindInFlight = false;dojo.io.argsFromMap = function(map, encoding, last){var enc = /utf/i.test(encoding||"") ? encodeURIComponent : dojo.string.encodeAscii;var mapped = [];var control = new Object();for(var name in map){var domap = function(elt){var val = enc(name)+"="+enc(elt);mapped[(last == name) ? "push" : "unshift"](val);}
if(!control[name]){var value = map[name];if (dojo.lang.isArray(value)){dojo.lang.forEach(value, domap);}else{domap(value);}}}
return mapped.join("&");}
dojo.io.setIFrameSrc = function( iframe,  src,  replace){try{var r = dojo.render.html;if(!replace){if(r.safari){iframe.location = src;}else{frames[iframe.name].location = src;}}else{var idoc;if(r.ie){idoc = iframe.contentWindow.document;}else if(r.safari){idoc = iframe.document;}else{idoc = iframe.contentWindow;}
if(!idoc){iframe.location = src;return;}else{idoc.location.replace(src);}}}catch(e){dojo.debug(e);dojo.debug("setIFrameSrc: "+e);}}
dojo.provide("dojo.string.extras");dojo.string.substituteParams = function(template, hash){var map = (typeof hash == 'object') ? hash : dojo.lang.toArray(arguments, 1);return template.replace(/\%\{(\w+)\}/g, function(match, key){if(typeof(map[key]) != "undefined" && map[key] != null){return map[key];}
dojo.raise("Substitution not found: " + key);});};dojo.string.capitalize = function(str){if(!dojo.lang.isString(str)){ return ""; }
if(arguments.length == 0){ str = this; }
var words = str.split(' ');for(var i=0; i<words.length; i++){words[i] = words[i].charAt(0).toUpperCase() + words[i].substring(1);}
return words.join(" ");}
dojo.string.isBlank = function(str){if(!dojo.lang.isString(str)){ return true; }
return (dojo.string.trim(str).length == 0);}
dojo.string.encodeAscii = function(str){if(!dojo.lang.isString(str)){ return str; }
var ret = "";var value = escape(str);var match, re = /%u([0-9A-F]{4})/i;while((match = value.match(re))){var num = Number("0x"+match[1]);var newVal = escape("&#" + num + ";");ret += value.substring(0, match.index) + newVal;value = value.substring(match.index+match[0].length);}
ret += value.replace(/\+/g, "%2B");return ret;}
dojo.string.escape = function(type, str){var args = dojo.lang.toArray(arguments, 1);switch(type.toLowerCase()){case "xml":
case "html":
case "xhtml":
return dojo.string.escapeXml.apply(this, args);case "sql":
return dojo.string.escapeSql.apply(this, args);case "regexp":
case "regex":
return dojo.string.escapeRegExp.apply(this, args);case "javascript":
case "jscript":
case "js":
return dojo.string.escapeJavaScript.apply(this, args);case "ascii":
return dojo.string.encodeAscii.apply(this, args);default:
return str;}}
dojo.string.escapeXml = function(str, noSingleQuotes){str = str.replace(/&/gm, "&amp;").replace(/</gm, "&lt;")
.replace(/>/gm, "&gt;").replace(/"/gm, "&quot;");if(!noSingleQuotes){ str = str.replace(/'/gm, "&#39;"); }
return str;}
dojo.string.escapeSql = function(str){return str.replace(/'/gm, "''");}
dojo.string.escapeRegExp = function(str){return str.replace(/\\/gm, "\\\\").replace(/([\f\b\n\t\r[\^$|?*+(){}])/gm, "\\$1");}
dojo.string.escapeJavaScript = function(str){return str.replace(/(["'\f\b\n\t\r])/gm, "\\$1");}
dojo.string.escapeString = function(str){return ('"' + str.replace(/(["\\])/g, '\\$1') + '"'
).replace(/[\f]/g, "\\f"
).replace(/[\b]/g, "\\b"
).replace(/[\n]/g, "\\n"
).replace(/[\t]/g, "\\t"
).replace(/[\r]/g, "\\r");}
dojo.string.summary = function(str, len){if(!len || str.length <= len){return str;}
return str.substring(0, len).replace(/\.+$/, "") + "...";}
dojo.string.endsWith = function(str, end, ignoreCase){if(ignoreCase){str = str.toLowerCase();end = end.toLowerCase();}
if((str.length - end.length) < 0){return false;}
return str.lastIndexOf(end) == str.length - end.length;}
dojo.string.endsWithAny = function(str ){for(var i = 1; i < arguments.length; i++) {if(dojo.string.endsWith(str, arguments[i])) {return true;}}
return false;}
dojo.string.startsWith = function(str, start, ignoreCase){if(ignoreCase) {str = str.toLowerCase();start = start.toLowerCase();}
return str.indexOf(start) == 0;}
dojo.string.startsWithAny = function(str ){for(var i = 1; i < arguments.length; i++) {if(dojo.string.startsWith(str, arguments[i])) {return true;}}
return false;}
dojo.string.has = function(str ) {for(var i = 1; i < arguments.length; i++) {if(str.indexOf(arguments[i]) > -1){return true;}}
return false;}
dojo.string.normalizeNewlines = function(text, newlineChar){if (newlineChar == "\n"){text = text.replace(/\r\n/g, "\n");text = text.replace(/\r/g, "\n");} else if (newlineChar == "\r"){text = text.replace(/\r\n/g, "\r");text = text.replace(/\n/g, "\r");}else{text = text.replace(/([^\r])\n/g, "$1\r\n").replace(/\r([^\n])/g, "\r\n$1");}
return text;}
dojo.string.splitEscaped = function(str, charac){var components = [];for (var i = 0, prevcomma = 0; i < str.length; i++){if (str.charAt(i) == '\\'){ i++; continue; }
if (str.charAt(i) == charac){components.push(str.substring(prevcomma, i));prevcomma = i + 1;}}
components.push(str.substr(prevcomma));return components;}
dojo.provide("dojo.undo.browser");try{if((!djConfig["preventBackButtonFix"])&&(!dojo.hostenv.post_load_)){document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(dojo.hostenv.getBaseScriptUri()+'iframe_history.html')+"'></iframe>");}}catch(e){}
if(dojo.render.html.opera){dojo.debug("Opera is not supported with dojo.undo.browser, so back/forward detection will not work.");}
dojo.undo.browser = {initialHref: (!dj_undef("window")) ? window.location.href : "",initialHash: (!dj_undef("window")) ? window.location.hash : "",moveForward: false,historyStack: [],forwardStack: [],historyIframe: null,bookmarkAnchor: null,locationTimer: null,setInitialState: function(args){this.initialState = this._createState(this.initialHref, args, this.initialHash);},addToHistory: function(args){this.forwardStack = [];var hash = null;var url = null;if(!this.historyIframe){this.historyIframe = window.frames["djhistory"];}
if(!this.bookmarkAnchor){this.bookmarkAnchor = document.createElement("a");dojo.body().appendChild(this.bookmarkAnchor);this.bookmarkAnchor.style.display = "none";}
if(args["changeUrl"]){hash = "#"+ ((args["changeUrl"]!==true) ? args["changeUrl"] : (new Date()).getTime());if(this.historyStack.length == 0 && this.initialState.urlHash == hash){this.initialState = this._createState(url, args, hash);return;}else if(this.historyStack.length > 0 && this.historyStack[this.historyStack.length - 1].urlHash == hash){this.historyStack[this.historyStack.length - 1] = this._createState(url, args, hash);return;}
this.changingUrl = true;setTimeout("window.location.href = '"+hash+"'; dojo.undo.browser.changingUrl = false;", 1);this.bookmarkAnchor.href = hash;if(dojo.render.html.ie){url = this._loadIframeHistory();var oldCB = args["back"]||args["backButton"]||args["handle"];var tcb = function(handleName){if(window.location.hash != ""){setTimeout("window.location.href = '"+hash+"';", 1);}
oldCB.apply(this, [handleName]);}
if(args["back"]){args.back = tcb;}else if(args["backButton"]){args.backButton = tcb;}else if(args["handle"]){args.handle = tcb;}
var oldFW = args["forward"]||args["forwardButton"]||args["handle"];var tfw = function(handleName){if(window.location.hash != ""){window.location.href = hash;}
if(oldFW){oldFW.apply(this, [handleName]);}}
if(args["forward"]){args.forward = tfw;}else if(args["forwardButton"]){args.forwardButton = tfw;}else if(args["handle"]){args.handle = tfw;}}else if(dojo.render.html.moz){if(!this.locationTimer){this.locationTimer = setInterval("dojo.undo.browser.checkLocation();", 200);}}}else{url = this._loadIframeHistory();}
this.historyStack.push(this._createState(url, args, hash));},checkLocation: function(){if (!this.changingUrl){var hsl = this.historyStack.length;if((window.location.hash == this.initialHash||window.location.href == this.initialHref)&&(hsl == 1)){this.handleBackButton();return;}
if(this.forwardStack.length > 0){if(this.forwardStack[this.forwardStack.length-1].urlHash == window.location.hash){this.handleForwardButton();return;}}
if((hsl >= 2)&&(this.historyStack[hsl-2])){if(this.historyStack[hsl-2].urlHash==window.location.hash){this.handleBackButton();return;}}}},iframeLoaded: function(evt, ifrLoc){if(!dojo.render.html.opera){var query = this._getUrlQuery(ifrLoc.href);if(query == null){if(this.historyStack.length == 1){this.handleBackButton();}
return;}
if(this.moveForward){this.moveForward = false;return;}
if(this.historyStack.length >= 2 && query == this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){this.handleBackButton();}
else if(this.forwardStack.length > 0 && query == this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){this.handleForwardButton();}}},handleBackButton: function(){var current = this.historyStack.pop();if(!current){ return; }
var last = this.historyStack[this.historyStack.length-1];if(!last && this.historyStack.length == 0){last = this.initialState;}
if (last){if(last.kwArgs["back"]){last.kwArgs["back"]();}else if(last.kwArgs["backButton"]){last.kwArgs["backButton"]();}else if(last.kwArgs["handle"]){last.kwArgs.handle("back");}}
this.forwardStack.push(current);},handleForwardButton: function(){var last = this.forwardStack.pop();if(!last){ return; }
if(last.kwArgs["forward"]){last.kwArgs.forward();}else if(last.kwArgs["forwardButton"]){last.kwArgs.forwardButton();}else if(last.kwArgs["handle"]){last.kwArgs.handle("forward");}
this.historyStack.push(last);},_createState: function(url, args, hash){return {"url": url, "kwArgs": args, "urlHash": hash};},_getUrlQuery: function(url){var segments = url.split("?");if (segments.length < 2){return null;}
else{return segments[1];}},_loadIframeHistory: function(){var url = dojo.hostenv.getBaseScriptUri()+"iframe_history.html?"+(new Date()).getTime();this.moveForward = true;dojo.io.setIFrameSrc(this.historyIframe, url, false);return url;}}
dojo.provide("dojo.io.BrowserIO");if(!dj_undef("window")) {dojo.io.checkChildrenForFile = function(node){var hasFile = false;var inputs = node.getElementsByTagName("input");dojo.lang.forEach(inputs, function(input){if(hasFile){ return; }
if(input.getAttribute("type")=="file"){hasFile = true;}});return hasFile;}
dojo.io.formHasFile = function(formNode){return dojo.io.checkChildrenForFile(formNode);}
dojo.io.updateNode = function(node, urlOrArgs){node = dojo.byId(node);var args = urlOrArgs;if(dojo.lang.isString(urlOrArgs)){args = { url: urlOrArgs };}
args.mimetype = "text/html";args.load = function(t, d, e){while(node.firstChild){if(dojo["event"]){try{dojo.event.browser.clean(node.firstChild);}catch(e){}}
node.removeChild(node.firstChild);}
node.innerHTML = d;};dojo.io.bind(args);}
dojo.io.formFilter = function(node) {var type = (node.type||"").toLowerCase();return !node.disabled && node.name
&& !dojo.lang.inArray(["file", "submit", "image", "reset", "button"], type);}
dojo.io.encodeForm = function(formNode, encoding, formFilter){if((!formNode)||(!formNode.tagName)||(!formNode.tagName.toLowerCase() == "form")){dojo.raise("Attempted to encode a non-form element.");}
if(!formFilter) { formFilter = dojo.io.formFilter; }
var enc = /utf/i.test(encoding||"") ? encodeURIComponent : dojo.string.encodeAscii;var values = [];for(var i = 0; i < formNode.elements.length; i++){var elm = formNode.elements[i];if(!elm || elm.tagName.toLowerCase() == "fieldset" || !formFilter(elm)) { continue; }
var name = enc(elm.name);var type = elm.type.toLowerCase();if(type == "select-multiple"){for(var j = 0; j < elm.options.length; j++){if(elm.options[j].selected) {values.push(name + "=" + enc(elm.options[j].value));}}}else if(dojo.lang.inArray(["radio", "checkbox"], type)){if(elm.checked){values.push(name + "=" + enc(elm.value));}}else{values.push(name + "=" + enc(elm.value));}}
var inputs = formNode.getElementsByTagName("input");for(var i = 0; i < inputs.length; i++) {var input = inputs[i];if(input.type.toLowerCase() == "image" && input.form == formNode
&& formFilter(input)) {var name = enc(input.name);values.push(name + "=" + enc(input.value));values.push(name + ".x=0");values.push(name + ".y=0");}}
return values.join("&") + "&";}
dojo.io.FormBind = function(args) {this.bindArgs = {};if(args && args.formNode) {this.init(args);} else if(args) {this.init({formNode: args});}}
dojo.lang.extend(dojo.io.FormBind, {form: null,bindArgs: null,clickedButton: null,init: function(args) {var form = dojo.byId(args.formNode);if(!form || !form.tagName || form.tagName.toLowerCase() != "form") {throw new Error("FormBind: Couldn't apply, invalid form");} else if(this.form == form) {return;} else if(this.form) {throw new Error("FormBind: Already applied to a form");}
dojo.lang.mixin(this.bindArgs, args);this.form = form;this.connect(form, "onsubmit", "submit");for(var i = 0; i < form.elements.length; i++) {var node = form.elements[i];if(node && node.type && dojo.lang.inArray(["submit", "button"], node.type.toLowerCase())) {this.connect(node, "onclick", "click");}}
var inputs = form.getElementsByTagName("input");for(var i = 0; i < inputs.length; i++) {var input = inputs[i];if(input.type.toLowerCase() == "image" && input.form == form) {this.connect(input, "onclick", "click");}}},onSubmit: function(form) {return true;},submit: function(e) {e.preventDefault();if(this.onSubmit(this.form)) {dojo.io.bind(dojo.lang.mixin(this.bindArgs, {formFilter: dojo.lang.hitch(this, "formFilter")}));}},click: function(e) {var node = e.currentTarget;if(node.disabled) { return; }
this.clickedButton = node;},formFilter: function(node) {var type = (node.type||"").toLowerCase();var accept = false;if(node.disabled || !node.name) {accept = false;} else if(dojo.lang.inArray(["submit", "button", "image"], type)) {if(!this.clickedButton) { this.clickedButton = node; }
accept = node == this.clickedButton;} else {accept = !dojo.lang.inArray(["file", "submit", "reset", "button"], type);}
return accept;},connect: function(srcObj, srcFcn, targetFcn) {if(dojo.evalObjPath("dojo.event.connect")) {dojo.event.connect(srcObj, srcFcn, this, targetFcn);} else {var fcn = dojo.lang.hitch(this, targetFcn);srcObj[srcFcn] = function(e) {if(!e) { e = window.event; }
if(!e.currentTarget) { e.currentTarget = e.srcElement; }
if(!e.preventDefault) { e.preventDefault = function() { window.event.returnValue = false; }}
fcn(e);}}}});dojo.io.XMLHTTPTransport = new function(){var _this = this;var _cache = {};this.useCache = false;this.preventCache = false;function getCacheKey(url, query, method) {return url + "|" + query + "|" + method.toLowerCase();}
function addToCache(url, query, method, http) {_cache[getCacheKey(url, query, method)] = http;}
function getFromCache(url, query, method) {return _cache[getCacheKey(url, query, method)];}
this.clearCache = function() {_cache = {};}
function doLoad(kwArgs, http, url, query, useCache) {if(	((http.status>=200)&&(http.status<300))||
(http.status==304)||
(location.protocol=="file:" && (http.status==0 || http.status==undefined))||
(location.protocol=="chrome:" && (http.status==0 || http.status==undefined))
){var ret;if(kwArgs.method.toLowerCase() == "head"){var headers = http.getAllResponseHeaders();ret = {};ret.toString = function(){ return headers; }
var values = headers.split(/[\r\n]+/g);for(var i = 0; i < values.length; i++) {var pair = values[i].match(/^([^:]+)\s*:\s*(.+)$/i);if(pair) {ret[pair[1]] = pair[2];}}}else if(kwArgs.mimetype == "text/javascript"){try{ret = dj_eval(http.responseText);}catch(e){dojo.debug(e);dojo.debug(http.responseText);ret = null;}}else if(kwArgs.mimetype == "text/json" || kwArgs.mimetype == "application/json"){try{ret = dj_eval("("+http.responseText+")");}catch(e){dojo.debug(e);dojo.debug(http.responseText);ret = false;}}else if((kwArgs.mimetype == "application/xml")||
(kwArgs.mimetype == "text/xml")){ret = http.responseXML;if(!ret || typeof ret == "string" || !http.getResponseHeader("Content-Type")) {ret = dojo.dom.createDocumentFromText(http.responseText);}}else{ret = http.responseText;}
if(useCache){addToCache(url, query, kwArgs.method, http);}
kwArgs[(typeof kwArgs.load == "function") ? "load" : "handle"]("load", ret, http, kwArgs);}else{var errObj = new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);kwArgs[(typeof kwArgs.error == "function") ? "error" : "handle"]("error", errObj, http, kwArgs);}}
function setHeaders(http, kwArgs){if(kwArgs["headers"]) {for(var header in kwArgs["headers"]) {if(header.toLowerCase() == "content-type" && !kwArgs["contentType"]) {kwArgs["contentType"] = kwArgs["headers"][header];} else {http.setRequestHeader(header, kwArgs["headers"][header]);}}}}
this.inFlight = [];this.inFlightTimer = null;this.startWatchingInFlight = function(){if(!this.inFlightTimer){this.inFlightTimer = setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();", 10);}}
this.watchInFlight = function(){var now = null;if(!dojo.hostenv._blockAsync && !_this._blockAsync){for(var x=this.inFlight.length-1; x>=0; x--){try{var tif = this.inFlight[x];if(!tif || tif.http._aborted || !tif.http.readyState){this.inFlight.splice(x, 1); continue;}
if(4==tif.http.readyState){this.inFlight.splice(x, 1);doLoad(tif.req, tif.http, tif.url, tif.query, tif.useCache);}else if (tif.startTime){if(!now){now = (new Date()).getTime();}
if(tif.startTime + (tif.req.timeoutSeconds * 1000) < now){if(typeof tif.http.abort == "function"){tif.http.abort();}
this.inFlight.splice(x, 1);tif.req[(typeof tif.req.timeout == "function") ? "timeout" : "handle"]("timeout", null, tif.http, tif.req);}}}catch(e){try{var errObj = new dojo.io.Error("XMLHttpTransport.watchInFlight Error: " + e);tif.req[(typeof tif.req.error == "function") ? "error" : "handle"]("error", errObj, tif.http, tif.req);}catch(e2){dojo.debug("XMLHttpTransport error callback failed: " + e2);}}}}
clearTimeout(this.inFlightTimer);if(this.inFlight.length == 0){this.inFlightTimer = null;return;}
this.inFlightTimer = setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();", 10);}
var hasXmlHttp = dojo.hostenv.getXmlhttpObject() ? true : false;this.canHandle = function(kwArgs){return hasXmlHttp
&& dojo.lang.inArray(["text/plain", "text/html", "application/xml", "text/xml", "text/javascript", "text/json", "application/json"], (kwArgs["mimetype"].toLowerCase()||""))
&& !( kwArgs["formNode"] && dojo.io.formHasFile(kwArgs["formNode"]) );}
this.multipartBoundary = "45309FFF-BD65-4d50-99C9-36986896A96F";this.bind = function(kwArgs){if(!kwArgs["url"]){if( !kwArgs["formNode"]
&& (kwArgs["backButton"] || kwArgs["back"] || kwArgs["changeUrl"] || kwArgs["watchForURL"])
&& (!djConfig.preventBackButtonFix)) {dojo.deprecated("Using dojo.io.XMLHTTPTransport.bind() to add to browser history without doing an IO request","Use dojo.undo.browser.addToHistory() instead.", "0.4");dojo.undo.browser.addToHistory(kwArgs);return true;}}
var url = kwArgs.url;var query = "";if(kwArgs["formNode"]){var ta = kwArgs.formNode.getAttribute("action");if((ta)&&(!kwArgs["url"])){ url = ta; }
var tp = kwArgs.formNode.getAttribute("method");if((tp)&&(!kwArgs["method"])){ kwArgs.method = tp; }
query += dojo.io.encodeForm(kwArgs.formNode, kwArgs.encoding, kwArgs["formFilter"]);}
if(url.indexOf("#") > -1) {dojo.debug("Warning: dojo.io.bind: stripping hash values from url:", url);url = url.split("#")[0];}
if(kwArgs["file"]){kwArgs.method = "post";}
if(!kwArgs["method"]){kwArgs.method = "get";}
if(kwArgs.method.toLowerCase() == "get"){kwArgs.multipart = false;}else{if(kwArgs["file"]){kwArgs.multipart = true;}else if(!kwArgs["multipart"]){kwArgs.multipart = false;}}
if(kwArgs["backButton"] || kwArgs["back"] || kwArgs["changeUrl"]){dojo.undo.browser.addToHistory(kwArgs);}
var content = kwArgs["content"] || {};if(kwArgs.sendTransport) {content["dojo.transport"] = "xmlhttp";}
do {if(kwArgs.postContent){query = kwArgs.postContent;break;}
if(content) {query += dojo.io.argsFromMap(content, kwArgs.encoding);}
if(kwArgs.method.toLowerCase() == "get" || !kwArgs.multipart){break;}
var	t = [];if(query.length){var q = query.split("&");for(var i = 0; i < q.length; ++i){if(q[i].length){var p = q[i].split("=");t.push(	"--" + this.multipartBoundary,"Content-Disposition: form-data; name=\"" + p[0] + "\"","",p[1]);}}}
if(kwArgs.file){if(dojo.lang.isArray(kwArgs.file)){for(var i = 0; i < kwArgs.file.length; ++i){var o = kwArgs.file[i];t.push(	"--" + this.multipartBoundary,"Content-Disposition: form-data; name=\"" + o.name + "\"; filename=\"" + ("fileName" in o ? o.fileName : o.name) + "\"","Content-Type: " + ("contentType" in o ? o.contentType : "application/octet-stream"),"",o.content);}}else{var o = kwArgs.file;t.push(	"--" + this.multipartBoundary,"Content-Disposition: form-data; name=\"" + o.name + "\"; filename=\"" + ("fileName" in o ? o.fileName : o.name) + "\"","Content-Type: " + ("contentType" in o ? o.contentType : "application/octet-stream"),"",o.content);}}
if(t.length){t.push("--"+this.multipartBoundary+"--", "");query = t.join("\r\n");}}while(false);var async = kwArgs["sync"] ? false : true;var preventCache = kwArgs["preventCache"] ||
(this.preventCache == true && kwArgs["preventCache"] != false);var useCache = kwArgs["useCache"] == true ||
(this.useCache == true && kwArgs["useCache"] != false );if(!preventCache && useCache){var cachedHttp = getFromCache(url, query, kwArgs.method);if(cachedHttp){doLoad(kwArgs, cachedHttp, url, query, false);return;}}
var http = dojo.hostenv.getXmlhttpObject(kwArgs);var received = false;if(async){var startTime =
this.inFlight.push({"req":		kwArgs,"http":		http,"url":	 	url,"query":	query,"useCache":	useCache,"startTime": kwArgs.timeoutSeconds ? (new Date()).getTime() : 0});this.startWatchingInFlight();}else{_this._blockAsync = true;}
if(kwArgs.method.toLowerCase() == "post"){if (!kwArgs.user) {http.open("POST", url, async);}else{http.open("POST", url, async, kwArgs.user, kwArgs.password);}
setHeaders(http, kwArgs);http.setRequestHeader("Content-Type", kwArgs.multipart ? ("multipart/form-data; boundary=" + this.multipartBoundary) :
(kwArgs.contentType || "application/x-www-form-urlencoded"));try{http.send(query);}catch(e){if(typeof http.abort == "function"){http.abort();}
doLoad(kwArgs, {status: 404}, url, query, useCache);}}else{var tmpUrl = url;if(query != "") {tmpUrl += (tmpUrl.indexOf("?") > -1 ? "&" : "?") + query;}
if(preventCache) {tmpUrl += (dojo.string.endsWithAny(tmpUrl, "?", "&")
? "" : (tmpUrl.indexOf("?") > -1 ? "&" : "?")) + "dojo.preventCache=" + new Date().valueOf();}
if (!kwArgs.user) {http.open(kwArgs.method.toUpperCase(), tmpUrl, async);}else{http.open(kwArgs.method.toUpperCase(), tmpUrl, async, kwArgs.user, kwArgs.password);}
setHeaders(http, kwArgs);try {http.send(null);}catch(e)	{if(typeof http.abort == "function"){http.abort();}
doLoad(kwArgs, {status: 404}, url, query, useCache);}}
if( !async ) {doLoad(kwArgs, http, url, query, useCache);_this._blockAsync = false;}
kwArgs.abort = function(){try{http._aborted = true;}catch(e){}
return http.abort();}
return;}
dojo.io.transports.addTransport("XMLHTTPTransport");}}
dojo.provide("dojo.io.cookie");dojo.io.cookie.setCookie = function(name, value, days, path, domain, secure) {var expires = -1;if(typeof days == "number" && days >= 0) {var d = new Date();d.setTime(d.getTime()+(days*24*60*60*1000));expires = d.toGMTString();}
value = escape(value);document.cookie = name + "=" + value + ";"
+ (expires != -1 ? " expires=" + expires + ";" : "")
+ (path ? "path=" + path : "")
+ (domain ? "; domain=" + domain : "")
+ (secure ? "; secure" : "");}
dojo.io.cookie.set = dojo.io.cookie.setCookie;dojo.io.cookie.getCookie = function(name) {var idx = document.cookie.lastIndexOf(name+'=');if(idx == -1) { return null; }
var value = document.cookie.substring(idx+name.length+1);var end = value.indexOf(';');if(end == -1) { end = value.length; }
value = value.substring(0, end);value = unescape(value);return value;}
dojo.io.cookie.get = dojo.io.cookie.getCookie;dojo.io.cookie.deleteCookie = function(name) {dojo.io.cookie.setCookie(name, "-", 0);}
dojo.io.cookie.setObjectCookie = function(name, obj, days, path, domain, secure, clearCurrent) {if(arguments.length == 5) {clearCurrent = domain;domain = null;secure = null;}
var pairs = [], cookie, value = "";if(!clearCurrent) { cookie = dojo.io.cookie.getObjectCookie(name); }
if(days >= 0) {if(!cookie) { cookie = {}; }
for(var prop in obj) {if(prop == null) {delete cookie[prop];} else if(typeof obj[prop] == "string" || typeof obj[prop] == "number") {cookie[prop] = obj[prop];}}
prop = null;for(var prop in cookie) {pairs.push(escape(prop) + "=" + escape(cookie[prop]));}
value = pairs.join("&");}
dojo.io.cookie.setCookie(name, value, days, path, domain, secure);}
dojo.io.cookie.getObjectCookie = function(name) {var values = null, cookie = dojo.io.cookie.getCookie(name);if(cookie) {values = {};var pairs = cookie.split("&");for(var i = 0; i < pairs.length; i++) {var pair = pairs[i].split("=");var value = pair[1];if( isNaN(value) ) { value = unescape(pair[1]); }
values[ unescape(pair[0]) ] = value;}}
return values;}
dojo.io.cookie.isSupported = function() {if(typeof navigator.cookieEnabled != "boolean") {dojo.io.cookie.setCookie("__TestingYourBrowserForCookieSupport__","CookiesAllowed", 90, null);var cookieVal = dojo.io.cookie.getCookie("__TestingYourBrowserForCookieSupport__");navigator.cookieEnabled = (cookieVal == "CookiesAllowed");if(navigator.cookieEnabled) {this.deleteCookie("__TestingYourBrowserForCookieSupport__");}}
return navigator.cookieEnabled;}
if(!dojo.io.cookies) { dojo.io.cookies = dojo.io.cookie; }
dojo.provide("dojo.date.common");dojo.date.setDayOfYear = function(dateObject, dayOfYear){dateObject.setMonth(0);dateObject.setDate(dayOfYear);return dateObject;}
dojo.date.getDayOfYear = function(dateObject){var fullYear = dateObject.getFullYear();var lastDayOfPrevYear = new Date(fullYear-1, 11, 31);return Math.floor((dateObject.getTime() -
lastDayOfPrevYear.getTime()) / 86400000);}
dojo.date.setWeekOfYear = function(dateObject, week, firstDay){if(arguments.length == 1){ firstDay = 0; }
dojo.unimplemented("dojo.date.setWeekOfYear");}
dojo.date.getWeekOfYear = function(dateObject, firstDay){if(arguments.length == 1){ firstDay = 0; }
var firstDayOfYear = new Date(dateObject.getFullYear(), 0, 1);var day = firstDayOfYear.getDay();firstDayOfYear.setDate(firstDayOfYear.getDate() -
day + firstDay - (day > firstDay ? 7 : 0));return Math.floor((dateObject.getTime() -
firstDayOfYear.getTime()) / 604800000);}
dojo.date.setIsoWeekOfYear = function(dateObject, week, firstDay){if (arguments.length == 1) { firstDay = 1; }
dojo.unimplemented("dojo.date.setIsoWeekOfYear");}
dojo.date.getIsoWeekOfYear = function(dateObject, firstDay) {if (arguments.length == 1) { firstDay = 1; }
dojo.unimplemented("dojo.date.getIsoWeekOfYear");}
dojo.date.shortTimezones = ["IDLW", "BET", "HST", "MART", "AKST", "PST", "MST","CST", "EST", "AST", "NFT", "BST", "FST", "AT", "GMT", "CET", "EET", "MSK","IRT", "GST", "AFT", "AGTT", "IST", "NPT", "ALMT", "MMT", "JT", "AWST","JST", "ACST", "AEST", "LHST", "VUT", "NFT", "NZT", "CHAST", "PHOT","LINT"];dojo.date.timezoneOffsets = [-720, -660, -600, -570, -540, -480, -420, -360,-300, -240, -210, -180, -120, -60, 0, 60, 120, 180, 210, 240, 270, 300,330, 345, 360, 390, 420, 480, 540, 570, 600, 630, 660, 690, 720, 765, 780,840];dojo.date.getDaysInMonth = function(dateObject){var month = dateObject.getMonth();var days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];if (month == 1 && dojo.date.isLeapYear(dateObject)) { return 29; }
else { return days[month]; }}
dojo.date.isLeapYear = function(dateObject){var year = dateObject.getFullYear();return (year%400 == 0) ? true : (year%100 == 0) ? false : (year%4 == 0) ? true : false;}
dojo.date.getTimezoneName = function(dateObject){var str = dateObject.toString();var tz = '';var match;var pos = str.indexOf('(');if (pos > -1) {pos++;tz = str.substring(pos, str.indexOf(')'));}
else{var pat = /([A-Z\/]+) \d{4}$/;if((match = str.match(pat))) {tz = match[1];}
else{str = dateObject.toLocaleString();pat = / ([A-Z\/]+)$/;if((match = str.match(pat))) {tz = match[1];}}}
return tz == 'AM' || tz == 'PM' ? '' : tz;}
dojo.date.getOrdinal = function(dateObject){var date = dateObject.getDate();if(date%100 != 11 && date%10 == 1){ return "st"; }
else if(date%100 != 12 && date%10 == 2){ return "nd"; }
else if(date%100 != 13 && date%10 == 3){ return "rd"; }
else{ return "th"; }}
dojo.date.compareTypes={DATE:1, TIME:2};dojo.date.compare=function( dateA,  dateB,  options){var dA=dateA;var dB=dateB||new Date();var now=new Date();with(dojo.date.compareTypes){var opt=options||(DATE|TIME);var d1=new Date(
(opt&DATE)?dA.getFullYear():now.getFullYear(),(opt&DATE)?dA.getMonth():now.getMonth(),(opt&DATE)?dA.getDate():now.getDate(),(opt&TIME)?dA.getHours():0,(opt&TIME)?dA.getMinutes():0,(opt&TIME)?dA.getSeconds():0
);var d2=new Date(
(opt&DATE)?dB.getFullYear():now.getFullYear(),(opt&DATE)?dB.getMonth():now.getMonth(),(opt&DATE)?dB.getDate():now.getDate(),(opt&TIME)?dB.getHours():0,(opt&TIME)?dB.getMinutes():0,(opt&TIME)?dB.getSeconds():0
);}
if(d1.valueOf()>d2.valueOf()){return 1;}
if(d1.valueOf()<d2.valueOf()){return -1;}
return 0;}
dojo.date.dateParts={YEAR:0, MONTH:1, DAY:2, HOUR:3, MINUTE:4, SECOND:5, MILLISECOND:6, QUARTER:7, WEEK:8, WEEKDAY:9};dojo.date.add = function( dt,  interv,  incr){if(typeof dt == 'number'){dt = new Date(dt);}
function fixOvershoot(){if (sum.getDate() < dt.getDate()){sum.setDate(0);}}
var sum = new Date(dt);with(dojo.date.dateParts){switch(interv){case YEAR:
sum.setFullYear(dt.getFullYear()+incr);fixOvershoot();break;case QUARTER:
incr*=3;case MONTH:
sum.setMonth(dt.getMonth()+incr);fixOvershoot();break;case WEEK:
incr*=7;case DAY:
sum.setDate(dt.getDate() + incr);break;case WEEKDAY:
var dat = dt.getDate();var weeks = 0;var days = 0;var strt = 0;var trgt = 0;var adj = 0;var mod = incr % 5;if (mod == 0) {days = (incr > 0) ? 5 : -5;weeks = (incr > 0) ? ((incr-5)/5) : ((incr+5)/5);}
else {days = mod;weeks = parseInt(incr/5);}
strt = dt.getDay();if (strt == 6 && incr > 0) {adj = 1;}
else if (strt == 0 && incr < 0) {adj = -1;}
trgt = (strt + days);if (trgt == 0 || trgt == 6) {adj = (incr > 0) ? 2 : -2;}
sum.setDate(dat + (7*weeks) + days + adj);break;case HOUR:
sum.setHours(sum.getHours()+incr);break;case MINUTE:
sum.setMinutes(sum.getMinutes()+incr);break;case SECOND:
sum.setSeconds(sum.getSeconds()+incr);break;case MILLISECOND:
sum.setMilliseconds(sum.getMilliseconds()+incr);break;default:
break;}}
return sum;};dojo.date.diff = function( dtA,  dtB,  interv){if(typeof dtA == 'number'){dtA = new Date(dtA);}
if(typeof dtB == 'number'){dtB = new Date(dtB);}
var yeaDiff = dtB.getFullYear() - dtA.getFullYear();var monDiff = (dtB.getMonth() - dtA.getMonth()) + (yeaDiff * 12);var msDiff = dtB.getTime() - dtA.getTime();var secDiff = msDiff/1000;var minDiff = secDiff/60;var houDiff = minDiff/60;var dayDiff = houDiff/24;var weeDiff = dayDiff/7;var delta = 0;with(dojo.date.dateParts){switch(interv){case YEAR:
delta = yeaDiff;break;case QUARTER:
var mA = dtA.getMonth();var mB = dtB.getMonth();var qA = Math.floor(mA/3) + 1;var qB = Math.floor(mB/3) + 1;qB += (yeaDiff * 4);delta = qB - qA;break;case MONTH:
delta = monDiff;break;case WEEK:
delta = parseInt(weeDiff);break;case DAY:
delta = dayDiff;break;case WEEKDAY:
var days = Math.round(dayDiff);var weeks = parseInt(days/7);var mod = days % 7;if (mod == 0) {days = weeks*5;}
else {var adj = 0;var aDay = dtA.getDay();var bDay = dtB.getDay();weeks = parseInt(days/7);mod = days % 7;var dtMark = new Date(dtA);dtMark.setDate(dtMark.getDate()+(weeks*7));var dayMark = dtMark.getDay();if (dayDiff > 0) {switch (true) {case aDay == 6:
adj = -1;break;case aDay == 0:
adj = 0;break;case bDay == 6:
adj = -1;break;case bDay == 0:
adj = -2;break;case (dayMark + mod) > 5:
adj = -2;break;default:
break;}}
else if (dayDiff < 0) {switch (true) {case aDay == 6:
adj = 0;break;case aDay == 0:
adj = 1;break;case bDay == 6:
adj = 2;break;case bDay == 0:
adj = 1;break;case (dayMark + mod) < 0:
adj = 2;break;default:
break;}}
days += adj;days -= (weeks*2);}
delta = days;break;case HOUR:
delta = houDiff;break;case MINUTE:
delta = minDiff;break;case SECOND:
delta = secDiff;break;case MILLISECOND:
delta = msDiff;break;default:
break;}}
return Math.round(delta);};dojo.provide("dojo.date.supplemental");dojo.date.getFirstDayOfWeek = function(locale){var firstDay = {mv:5,ae:6,af:6,bh:6,dj:6,dz:6,eg:6,er:6,et:6,iq:6,ir:6,jo:6,ke:6,kw:6,lb:6,ly:6,ma:6,om:6,qa:6,sa:6,sd:6,so:6,tn:6,ye:6,as:0,au:0,az:0,bw:0,ca:0,cn:0,fo:0,ge:0,gl:0,gu:0,hk:0,ie:0,il:0,is:0,jm:0,jp:0,kg:0,kr:0,la:0,mh:0,mo:0,mp:0,mt:0,nz:0,ph:0,pk:0,sg:0,th:0,tt:0,tw:0,um:0,us:0,uz:0,vi:0,za:0,zw:0,et:0,mw:0,ng:0,tj:0,gb:0,sy:4};locale = dojo.hostenv.normalizeLocale(locale);var country = locale.split("-")[1];var dow = firstDay[country];return (typeof dow == 'undefined') ? 1 : dow;};dojo.date.getWeekend = function(locale){var weekendStart = {eg:5,il:5,sy:5,'in':0,ae:4,bh:4,dz:4,iq:4,jo:4,kw:4,lb:4,ly:4,ma:4,om:4,qa:4,sa:4,sd:4,tn:4,ye:4};var weekendEnd = {ae:5,bh:5,dz:5,iq:5,jo:5,kw:5,lb:5,ly:5,ma:5,om:5,qa:5,sa:5,sd:5,tn:5,ye:5,af:5,ir:5,eg:6,il:6,sy:6};locale = dojo.hostenv.normalizeLocale(locale);var country = locale.split("-")[1];var start = weekendStart[country];var end = weekendEnd[country];if(typeof start == 'undefined'){start=6;}
if(typeof end == 'undefined'){end=0;}
return {start:start, end:end};};dojo.date.isWeekend = function(dateObj, locale){var weekend = dojo.date.getWeekend(locale);var day = (dateObj || new Date()).getDay();if(weekend.end<weekend.start){weekend.end+=7;if(day<weekend.start){ day+=7; }}
return day >= weekend.start && day <= weekend.end;};dojo.provide("dojo.i18n.common");dojo.i18n.getLocalization = function(packageName, bundleName, locale){dojo.hostenv.preloadLocalizations();locale = dojo.hostenv.normalizeLocale(locale);var elements = locale.split('-');var module = [packageName,"nls",bundleName].join('.');var bundle = dojo.hostenv.findModule(module, true);var localization;for(var i = elements.length; i > 0; i--){var loc = elements.slice(0, i).join('_');if(bundle[loc]){localization = bundle[loc];break;}}
if(!localization){localization = bundle.ROOT;}
if(localization){var clazz = function(){};clazz.prototype = localization;return new clazz();}
dojo.raise("Bundle not found: " + bundleName + " in " + packageName+" , locale=" + locale);};dojo.i18n.isLTR = function(locale){var lang = dojo.hostenv.normalizeLocale(locale).split('-')[0];var RTL = {ar:true,fa:true,he:true,ur:true,yi:true};return !RTL[lang];};dojo.provide("dojo.date.format");(function(){dojo.date.format = function(dateObject, options){if(typeof options == "string"){dojo.deprecated("dojo.date.format", "To format dates with POSIX-style strings, please use dojo.date.strftime instead", "0.5");return dojo.date.strftime(dateObject, options);}
function formatPattern(dateObject, pattern){return pattern.replace(/([a-z])\1*/ig, function(match){var s;var c = match.charAt(0);var l = match.length;var pad;var widthList = ["abbr", "wide", "narrow"];switch(c){case 'G':
if(l>3){dojo.unimplemented("Era format not implemented");}
s = info.eras[dateObject.getFullYear() < 0 ? 1 : 0];break;case 'y':
s = dateObject.getFullYear();switch(l){case 1:
break;case 2:
s = String(s).substr(-2);break;default:
pad = true;}
break;case 'Q':
case 'q':
s = Math.ceil((dateObject.getMonth()+1)/3);switch(l){case 1: case 2:
pad = true;break;case 3:
case 4:
dojo.unimplemented("Quarter format not implemented");}
break;case 'M':
case 'L':
var m = dateObject.getMonth();var width;switch(l){case 1: case 2:
s = m+1; pad = true;break;case 3: case 4: case 5:
width = widthList[l-3];break;}
if(width){var type = (c == "L") ? "standalone" : "format";var prop = ["months",type,width].join("-");s = info[prop][m];}
break;case 'w':
var firstDay = 0;s = dojo.date.getWeekOfYear(dateObject, firstDay); pad = true;break;case 'd':
s = dateObject.getDate(); pad = true;break;case 'D':
s = dojo.date.getDayOfYear(dateObject); pad = true;break;case 'E':
case 'e':
case 'c':
var d = dateObject.getDay();var width;switch(l){case 1: case 2:
if(c == 'e'){var first = dojo.date.getFirstDayOfWeek(options.locale);d = (d-first+7)%7;}
if(c != 'c'){s = d+1; pad = true;break;}
case 3: case 4: case 5:
width = widthList[l-3];break;}
if(width){var type = (c == "c") ? "standalone" : "format";var prop = ["days",type,width].join("-");s = info[prop][d];}
break;case 'a':
var timePeriod = (dateObject.getHours() < 12) ? 'am' : 'pm';s = info[timePeriod];break;case 'h':
case 'H':
case 'K':
case 'k':
var h = dateObject.getHours();switch (c) {case 'h':
s = (h % 12) || 12;break;case 'H':
s = h;break;case 'K':
s = (h % 12);break;case 'k':
s = h || 24;break;}
pad = true;break;case 'm':
s = dateObject.getMinutes(); pad = true;break;case 's':
s = dateObject.getSeconds(); pad = true;break;case 'S':
s = Math.round(dateObject.getMilliseconds() * Math.pow(10, l-3));break;case 'v':
case 'z':
s = dojo.date.getTimezoneName(dateObject);if(s){break;}
l=4;case 'Z':
var offset = dateObject.getTimezoneOffset();var tz = [
(offset<=0 ? "+" : "-"),dojo.string.pad(Math.floor(Math.abs(offset)/60), 2),dojo.string.pad(Math.abs(offset)% 60, 2)
];if(l==4){tz.splice(0, 0, "GMT");tz.splice(3, 0, ":");}
s = tz.join("");break;case 'Y':
case 'u':
case 'W':
case 'F':
case 'g':
case 'A':
dojo.debug(match+" modifier not yet implemented");s = "?";break;default:
dojo.raise("dojo.date.format: invalid pattern char: "+pattern);}
if(pad){ s = dojo.string.pad(s, l); }
return s;});}
options = options || {};var locale = dojo.hostenv.normalizeLocale(options.locale);var formatLength = options.formatLength || 'full';var info = dojo.date._getGregorianBundle(locale);var str = [];var sauce = dojo.lang.curry(this, formatPattern, dateObject);if(options.selector != "timeOnly"){var datePattern = options.datePattern || info["dateFormat-"+formatLength];if(datePattern){str.push(_processPattern(datePattern, sauce));}}
if(options.selector != "dateOnly"){var timePattern = options.timePattern || info["timeFormat-"+formatLength];if(timePattern){str.push(_processPattern(timePattern, sauce));}}
var result = str.join(" ");return result;};dojo.date.parse = function(value, options){options = options || {};var locale = dojo.hostenv.normalizeLocale(options.locale);var info = dojo.date._getGregorianBundle(locale);var formatLength = options.formatLength || 'full';if(!options.selector){ options.selector = 'dateOnly'; }
var datePattern = options.datePattern || info["dateFormat-" + formatLength];var timePattern = options.timePattern || info["timeFormat-" + formatLength];var pattern;if(options.selector == 'dateOnly'){pattern = datePattern;}
else if(options.selector == 'timeOnly'){pattern = timePattern;}else if(options.selector == 'dateTime'){pattern = datePattern + ' ' + timePattern;}else{var msg = "dojo.date.parse: Unknown selector param passed: '" + options.selector + "'.";msg += " Defaulting to date pattern.";dojo.debug(msg);pattern = datePattern;}
var groups = [];var dateREString = _processPattern(pattern, dojo.lang.curry(this, _buildDateTimeRE, groups, info, options));var dateRE = new RegExp("^" + dateREString + "$");var match = dateRE.exec(value);if(!match){return null;}
var widthList = ['abbr', 'wide', 'narrow'];var result = new Date(1972, 0);var expected = {};for(var i=1; i<match.length; i++){var grp=groups[i-1];var l=grp.length;var v=match[i];switch(grp.charAt(0)){case 'y':
if(l != 2){result.setFullYear(v);expected.year = v;}else{if(v<100){v = Number(v);var year = '' + new Date().getFullYear();var century = year.substring(0, 2) * 100;var yearPart = Number(year.substring(2, 4));var cutoff = Math.min(yearPart + 20, 99);var num = (v < cutoff) ? century + v : century - 100 + v;result.setFullYear(num);expected.year = num;}else{if(options.strict){return null;}
result.setFullYear(v);expected.year = v;}}
break;case 'M':
if (l>2) {if(!options.strict){v = v.replace(/\./g,'');v = v.toLowerCase();}
var months = info['months-format-' + widthList[l-3]].concat();for (var j=0; j<months.length; j++){if(!options.strict){months[j] = months[j].toLowerCase();}
if(v == months[j]){result.setMonth(j);expected.month = j;break;}}
if(j==months.length){dojo.debug("dojo.date.parse: Could not parse month name: '" + v + "'.");return null;}}else{result.setMonth(v-1);expected.month = v-1;}
break;case 'E':
case 'e':
if(!options.strict){v = v.toLowerCase();}
var days = info['days-format-' + widthList[l-3]].concat();for (var j=0; j<days.length; j++){if(!options.strict){days[j] = days[j].toLowerCase();}
if(v == days[j]){break;}}
if(j==days.length){dojo.debug("dojo.date.parse: Could not parse weekday name: '" + v + "'.");return null;}
break;case 'd':
result.setDate(v);expected.date = v;break;case 'a':
var am = options.am || info.am;var pm = options.pm || info.pm;if(!options.strict){v = v.replace(/\./g,'').toLowerCase();am = am.replace(/\./g,'').toLowerCase();pm = pm.replace(/\./g,'').toLowerCase();}
if(options.strict && v != am && v != pm){dojo.debug("dojo.date.parse: Could not parse am/pm part.");return null;}
var hours = result.getHours();if(v == pm && hours < 12){result.setHours(hours + 12);} else if(v == am && hours == 12){result.setHours(0);}
break;case 'K':
if(v==24){v=0;}
case 'h':
case 'H':
case 'k':
if(v>23){dojo.debug("dojo.date.parse: Illegal hours value");return null;}
result.setHours(v);break;case 'm':
result.setMinutes(v);break;case 's':
result.setSeconds(v);break;case 'S':
result.setMilliseconds(v);break;default:
dojo.unimplemented("dojo.date.parse: unsupported pattern char=" + grp.charAt(0));}}
if(expected.year && result.getFullYear() != expected.year){dojo.debug("Parsed year: '" + result.getFullYear() + "' did not match input year: '" + expected.year + "'.");return null;}
if(expected.month && result.getMonth() != expected.month){dojo.debug("Parsed month: '" + result.getMonth() + "' did not match input month: '" + expected.month + "'.");return null;}
if(expected.date && result.getDate() != expected.date){dojo.debug("Parsed day of month: '" + result.getDate() + "' did not match input day of month: '" + expected.date + "'.");return null;}
return result;};function _processPattern(pattern, applyPattern, applyLiteral, applyAll){var identity = function(x){return x;};applyPattern = applyPattern || identity;applyLiteral = applyLiteral || identity;applyAll = applyAll || identity;var chunks = pattern.match(/(''|[^'])+/g);var literal = false;for(var i=0; i<chunks.length; i++){if(!chunks[i]){chunks[i]='';} else {chunks[i]=(literal ? applyLiteral : applyPattern)(chunks[i]);literal = !literal;}}
return applyAll(chunks.join(''));}
function _buildDateTimeRE(groups, info, options, pattern){return pattern.replace(/([a-z])\1*/ig, function(match){var s;var c = match.charAt(0);var l = match.length;switch(c){case 'y':
s = '\\d' + ((l==2) ? '{2,4}' : '+');break;case 'M':
s = (l>2) ? '\\S+' : '\\d{1,2}';break;case 'd':
s = '\\d{1,2}';break;case 'E':
s = '\\S+';break;case 'h':
case 'H':
case 'K':
case 'k':
s = '\\d{1,2}';break;case 'm':
case 's':
s = '[0-5]\\d';break;case 'S':
s = '\\d{1,3}';break;case 'a':
var am = options.am || info.am || 'AM';var pm = options.pm || info.pm || 'PM';if(options.strict){s = am + '|' + pm;}else{s = am;s += (am != am.toLowerCase()) ? '|' + am.toLowerCase() : '';s += '|';s += (pm != pm.toLowerCase()) ? pm + '|' + pm.toLowerCase() : pm;}
break;default:
dojo.unimplemented("parse of date format, pattern=" + pattern);}
if(groups){ groups.push(match); }
return '\\s*(' + s + ')\\s*';});}})();dojo.date.strftime = function(dateObject, format, locale){var padChar = null;function _(s, n){return dojo.string.pad(s, n || 2, padChar || "0");}
var info = dojo.date._getGregorianBundle(locale);function $(property){switch (property){case "a":
return dojo.date.getDayShortName(dateObject, locale);case "A":
return dojo.date.getDayName(dateObject, locale);case "b":
case "h":
return dojo.date.getMonthShortName(dateObject, locale);case "B":
return dojo.date.getMonthName(dateObject, locale);case "c":
return dojo.date.format(dateObject, {locale: locale});case "C":
return _(Math.floor(dateObject.getFullYear()/100));case "d":
return _(dateObject.getDate());case "D":
return $("m") + "/" + $("d") + "/" + $("y");case "e":
if(padChar == null){ padChar = " "; }
return _(dateObject.getDate());case "f":
if(padChar == null){ padChar = " "; }
return _(dateObject.getMonth()+1);case "g":
break;case "G":
dojo.unimplemented("unimplemented modifier 'G'");break;case "F":
return $("Y") + "-" + $("m") + "-" + $("d");case "H":
return _(dateObject.getHours());case "I":
return _(dateObject.getHours() % 12 || 12);case "j":
return _(dojo.date.getDayOfYear(dateObject), 3);case "k":
if (padChar == null) { padChar = " "; }
return _(dateObject.getHours());case "l":
if (padChar == null) { padChar = " "; }
return _(dateObject.getHours() % 12 || 12);case "m":
return _(dateObject.getMonth() + 1);case "M":
return _(dateObject.getMinutes());case "n":
return "\n";case "p":
return info[dateObject.getHours() < 12 ? "am" : "pm"];case "r":
return $("I") + ":" + $("M") + ":" + $("S") + " " + $("p");case "R":
return $("H") + ":" + $("M");case "S":
return _(dateObject.getSeconds());case "t":
return "\t";case "T":
return $("H") + ":" + $("M") + ":" + $("S");case "u":
return String(dateObject.getDay() || 7);case "U":
return _(dojo.date.getWeekOfYear(dateObject));case "V":
return _(dojo.date.getIsoWeekOfYear(dateObject));case "W":
return _(dojo.date.getWeekOfYear(dateObject, 1));case "w":
return String(dateObject.getDay());case "x":
return dojo.date.format(dateObject, {selector:'dateOnly', locale:locale});case "X":
return dojo.date.format(dateObject, {selector:'timeOnly', locale:locale});case "y":
return _(dateObject.getFullYear()%100);case "Y":
return String(dateObject.getFullYear());case "z":
var timezoneOffset = dateObject.getTimezoneOffset();return (timezoneOffset > 0 ? "-" : "+") +
_(Math.floor(Math.abs(timezoneOffset)/60)) + ":" +
_(Math.abs(timezoneOffset)%60);case "Z":
return dojo.date.getTimezoneName(dateObject);case "%":
return "%";}}
var string = "";var i = 0;var index = 0;var switchCase = null;while ((index = format.indexOf("%", i)) != -1){string += format.substring(i, index++);switch (format.charAt(index++)) {case "_":
padChar = " "; break;case "-":
padChar = ""; break;case "0":
padChar = "0"; break;case "^":
switchCase = "upper"; break;case "*":
switchCase = "lower"; break;case "#":
switchCase = "swap"; break;default:
padChar = null; index--; break;}
var property = $(format.charAt(index++));switch (switchCase){case "upper":
property = property.toUpperCase();break;case "lower":
property = property.toLowerCase();break;case "swap":
var compareString = property.toLowerCase();var swapString = '';var j = 0;var ch = '';while (j < property.length){ch = property.charAt(j);swapString += (ch == compareString.charAt(j)) ?
ch.toUpperCase() : ch.toLowerCase();j++;}
property = swapString;break;default:
break;}
switchCase = null;string += property;i = index;}
string += format.substring(i);return string;};(function(){var _customFormats = [];dojo.date.addCustomFormats = function(packageName, bundleName){_customFormats.push({pkg:packageName,name:bundleName});};dojo.date._getGregorianBundle = function(locale){var gregorian = {};dojo.lang.forEach(_customFormats, function(desc){var bundle = dojo.i18n.getLocalization(desc.pkg, desc.name, locale);gregorian = dojo.lang.mixin(gregorian, bundle);}, this);return gregorian;};})();dojo.date.addCustomFormats("dojo.i18n.calendar","gregorian");dojo.date.addCustomFormats("dojo.i18n.calendar","gregorianExtras");dojo.date.getNames = function(item, type, use, locale){var label;var lookup = dojo.date._getGregorianBundle(locale);var props = [item, use, type];if(use == 'standAlone'){label = lookup[props.join('-')];}
props[1] = 'format';return (label || lookup[props.join('-')]).concat();};dojo.date.getDayName = function(dateObject, locale){return dojo.date.getNames('days', 'wide', 'format', locale)[dateObject.getDay()];};dojo.date.getDayShortName = function(dateObject, locale){return dojo.date.getNames('days', 'abbr', 'format', locale)[dateObject.getDay()];};dojo.date.getMonthName = function(dateObject, locale){return dojo.date.getNames('months', 'wide', 'format', locale)[dateObject.getMonth()];};dojo.date.getMonthShortName = function(dateObject, locale){return dojo.date.getNames('months', 'abbr', 'format', locale)[dateObject.getMonth()];};dojo.date.toRelativeString = function(dateObject){var now = new Date();var diff = (now - dateObject) / 1000;var end = " ago";var future = false;if(diff < 0){future = true;end = " from now";diff = -diff;}
if(diff < 60){diff = Math.round(diff);return diff + " second" + (diff == 1 ? "" : "s") + end;}
if(diff < 60*60){diff = Math.round(diff/60);return diff + " minute" + (diff == 1 ? "" : "s") + end;}
if(diff < 60*60*24){diff = Math.round(diff/3600);return diff + " hour" + (diff == 1 ? "" : "s") + end;}
if(diff < 60*60*24*7){diff = Math.round(diff/(3600*24));if(diff == 1){return future ? "Tomorrow" : "Yesterday";}else{return diff + " days" + end;}}
return dojo.date.format(dateObject);};dojo.date.toSql = function(dateObject, noTime){return dojo.date.strftime(dateObject, "%F" + !noTime ? " %T" : "");};dojo.date.fromSql = function(sqlDate){var parts = sqlDate.split(/[\- :]/g);while(parts.length < 6){parts.push(0);}
return new Date(parts[0], (parseInt(parts[1],10)-1), parts[2], parts[3], parts[4], parts[5]);};dojo.provide("dojo.xml.Parse");dojo.xml.Parse = function(){var isIE = ((dojo.render.html.capable)&&(dojo.render.html.ie));function getTagName(node){try{return node.tagName.toLowerCase();}catch(e){return "";}}
function getDojoTagName(node){var tagName = getTagName(node);if (!tagName){return '';}
if((dojo.widget)&&(dojo.widget.tags[tagName])){return tagName;}
var p = tagName.indexOf(":");if(p>=0){return tagName;}
if(tagName.substr(0,5) == "dojo:"){return tagName;}
if(dojo.render.html.capable && dojo.render.html.ie && node.scopeName != 'HTML'){return node.scopeName.toLowerCase() + ':' + tagName;}
if(tagName.substr(0,4) == "dojo"){return "dojo:" + tagName.substring(4);}
var djt = node.getAttribute("dojoType") || node.getAttribute("dojotype");if(djt){if (djt.indexOf(":")<0){djt = "dojo:"+djt;}
return djt.toLowerCase();}
djt = node.getAttributeNS && node.getAttributeNS(dojo.dom.dojoml,"type");if(djt){return "dojo:" + djt.toLowerCase();}
try{djt = node.getAttribute("dojo:type");}catch(e){}
if(djt){ return "dojo:"+djt.toLowerCase(); }
if((dj_global["djConfig"])&&(!djConfig["ignoreClassNames"])){var classes = node.className||node.getAttribute("class");if((classes )&&(classes.indexOf)&&(classes.indexOf("dojo-")!=-1)){var aclasses = classes.split(" ");for(var x=0, c=aclasses.length; x<c; x++){if(aclasses[x].slice(0, 5) == "dojo-"){return "dojo:"+aclasses[x].substr(5).toLowerCase();}}}}
return '';}
this.parseElement = function(	node,hasParentNodeSet,optimizeForDojoML,thisIdx	){var tagName = getTagName(node);if(isIE && tagName.indexOf("/")==0){ return null; }
try{if(node.getAttribute("parseWidgets").toLowerCase() == "false"){return {};}}catch(e){}
var process = true;if(optimizeForDojoML){var dojoTagName = getDojoTagName(node);tagName = dojoTagName || tagName;process = Boolean(dojoTagName);}
var parsedNodeSet = {};parsedNodeSet[tagName] = [];var pos = tagName.indexOf(":");if(pos>0){var ns = tagName.substring(0,pos);parsedNodeSet["ns"] = ns;if((dojo.ns)&&(!dojo.ns.allow(ns))){process=false;}}
if(process){var attributeSet = this.parseAttributes(node);for(var attr in attributeSet){if((!parsedNodeSet[tagName][attr])||(typeof parsedNodeSet[tagName][attr] != "array")){parsedNodeSet[tagName][attr] = [];}
parsedNodeSet[tagName][attr].push(attributeSet[attr]);}
parsedNodeSet[tagName].nodeRef = node;parsedNodeSet.tagName = tagName;parsedNodeSet.index = thisIdx||0;}
var count = 0;for(var i = 0; i < node.childNodes.length; i++){var tcn = node.childNodes.item(i);switch(tcn.nodeType){case  dojo.dom.ELEMENT_NODE:
count++;var ctn = getDojoTagName(tcn) || getTagName(tcn);if(!parsedNodeSet[ctn]){parsedNodeSet[ctn] = [];}
parsedNodeSet[ctn].push(this.parseElement(tcn, true, optimizeForDojoML, count));if(	(tcn.childNodes.length == 1)&&
(tcn.childNodes.item(0).nodeType == dojo.dom.TEXT_NODE)){parsedNodeSet[ctn][parsedNodeSet[ctn].length-1].value = tcn.childNodes.item(0).nodeValue;}
break;case  dojo.dom.TEXT_NODE:
if(node.childNodes.length == 1){parsedNodeSet[tagName].push({ value: node.childNodes.item(0).nodeValue });}
break;default: break;}}
return parsedNodeSet;};this.parseAttributes = function(node){var parsedAttributeSet = {};var atts = node.attributes;var attnode, i=0;while((attnode=atts[i++])){if(isIE){if(!attnode){ continue; }
if((typeof attnode == "object")&&
(typeof attnode.nodeValue == 'undefined')||
(attnode.nodeValue == null)||
(attnode.nodeValue == '')){continue;}}
var nn = attnode.nodeName.split(":");nn = (nn.length == 2) ? nn[1] : attnode.nodeName;parsedAttributeSet[nn] = {value: attnode.nodeValue};}
return parsedAttributeSet;};};dojo.provide("dojo.lang.declare");dojo.lang.declare = function( className,  superclass,  init,  props){if((dojo.lang.isFunction(props))||((!props)&&(!dojo.lang.isFunction(init)))){var temp = props;props = init;init = temp;}
var mixins = [ ];if(dojo.lang.isArray(superclass)){mixins = superclass;superclass = mixins.shift();}
if(!init){init = dojo.evalObjPath(className, false);if((init)&&(!dojo.lang.isFunction(init))){ init = null };}
var ctor = dojo.lang.declare._makeConstructor();var scp = (superclass ? superclass.prototype : null);if(scp){scp.prototyping = true;ctor.prototype = new superclass();scp.prototyping = false;}
ctor.superclass = scp;ctor.mixins = mixins;for(var i=0,l=mixins.length; i<l; i++){dojo.lang.extend(ctor, mixins[i].prototype);}
ctor.prototype.initializer = null;ctor.prototype.declaredClass = className;if(dojo.lang.isArray(props)){dojo.lang.extend.apply(dojo.lang, [ctor].concat(props));}else{dojo.lang.extend(ctor, (props)||{});}
dojo.lang.extend(ctor, dojo.lang.declare._common);ctor.prototype.constructor = ctor;ctor.prototype.initializer = (ctor.prototype.initializer)||(init)||(function(){});dojo.lang.setObjPathValue(className, ctor, null, true);return ctor;}
dojo.lang.declare._makeConstructor = function() {return function(){var self = this._getPropContext();var s = self.constructor.superclass;if((s)&&(s.constructor)){if(s.constructor==arguments.callee){this._inherited("constructor", arguments);}else{this._contextMethod(s, "constructor", arguments);}}
var ms = (self.constructor.mixins)||([]);for(var i=0, m; (m=ms[i]); i++) {(((m.prototype)&&(m.prototype.initializer))||(m)).apply(this, arguments);}
if((!this.prototyping)&&(self.initializer)){self.initializer.apply(this, arguments);}}}
dojo.lang.declare._common = {_getPropContext: function() { return (this.___proto||this); },_contextMethod: function(ptype, method, args){var result, stack = this.___proto;this.___proto = ptype;try { result = ptype[method].apply(this,(args||[])); }
catch(e) { throw e; }
finally { this.___proto = stack; }
return result;},_inherited: function(prop, args){var p = this._getPropContext();do{if((!p.constructor)||(!p.constructor.superclass)){return;}
p = p.constructor.superclass;}while(!(prop in p));return (dojo.lang.isFunction(p[prop]) ? this._contextMethod(p, prop, args) : p[prop]);},inherited: function(prop, args){dojo.deprecated("'inherited' method is dangerous, do not up-call! 'inherited' is slated for removal in 0.5; name your super class (or use superclass property) instead.", "0.5");this._inherited(prop, args);}}
dojo.declare = dojo.lang.declare;dojo.provide("dojo.ns");dojo.ns = {namespaces: {},failed: {},loading: {},loaded: {},register: function(name, module, resolver, noOverride){if(!noOverride || !this.namespaces[name]){this.namespaces[name] = new dojo.ns.Ns(name, module, resolver);}},allow: function(name){if(this.failed[name]){return false;}
if((djConfig.excludeNamespace)&&(dojo.lang.inArray(djConfig.excludeNamespace, name))){return false;}
return((name==this.dojo)||(!djConfig.includeNamespace)||(dojo.lang.inArray(djConfig.includeNamespace, name)));},get: function(name){return this.namespaces[name];},require: function(name){var ns = this.namespaces[name];if((ns)&&(this.loaded[name])){return ns;}
if(!this.allow(name)){return false;}
if(this.loading[name]){dojo.debug('dojo.namespace.require: re-entrant request to load namespace "' + name + '" must fail.');return false;}
var req = dojo.require;this.loading[name] = true;try {if(name=="dojo"){req("dojo.namespaces.dojo");}else{if(!dojo.hostenv.moduleHasPrefix(name)){dojo.registerModulePath(name, "../" + name);}
req([name, 'manifest'].join('.'), false, true);}
if(!this.namespaces[name]){this.failed[name] = true;}}finally{this.loading[name]=false;}
return this.namespaces[name];}}
dojo.ns.Ns = function(name, module, resolver){this.name = name;this.module = module;this.resolver = resolver;this._loaded = [ ];this._failed = [ ];}
dojo.ns.Ns.prototype.resolve = function(name, domain, omitModuleCheck){if(!this.resolver || djConfig["skipAutoRequire"]){return false;}
var fullName = this.resolver(name, domain);if((fullName)&&(!this._loaded[fullName])&&(!this._failed[fullName])){var req = dojo.require;req(fullName, false, true);if(dojo.hostenv.findModule(fullName, false)){this._loaded[fullName] = true;}else{if(!omitModuleCheck){dojo.raise("dojo.ns.Ns.resolve: module '" + fullName + "' not found after loading via namespace '" + this.name + "'");}
this._failed[fullName] = true;}}
return Boolean(this._loaded[fullName]);}
dojo.registerNamespace = function(name, module, resolver){dojo.ns.register.apply(dojo.ns, arguments);}
dojo.registerNamespaceResolver = function(name, resolver){var n = dojo.ns.namespaces[name];if(n){n.resolver = resolver;}}
dojo.registerNamespaceManifest = function(module, path, name, widgetModule, resolver){dojo.registerModulePath(name, path);dojo.registerNamespace(name, widgetModule, resolver);}
dojo.registerNamespace("dojo", "dojo.widget");dojo.provide("dojo.event.topic");dojo.event.topic = new function(){this.topics = {};this.getTopic = function(topic){if(!this.topics[topic]){this.topics[topic] = new this.TopicImpl(topic);}
return this.topics[topic];}
this.registerPublisher = function(topic, obj, funcName){var topic = this.getTopic(topic);topic.registerPublisher(obj, funcName);}
this.subscribe = function(topic, obj, funcName){var topic = this.getTopic(topic);topic.subscribe(obj, funcName);}
this.unsubscribe = function(topic, obj, funcName){var topic = this.getTopic(topic);topic.unsubscribe(obj, funcName);}
this.destroy = function(topic){this.getTopic(topic).destroy();delete this.topics[topic];}
this.publishApply = function(topic, args){var topic = this.getTopic(topic);topic.sendMessage.apply(topic, args);}
this.publish = function(topic, message){var topic = this.getTopic(topic);var args = [];for(var x=1; x<arguments.length; x++){args.push(arguments[x]);}
topic.sendMessage.apply(topic, args);}}
dojo.event.topic.TopicImpl = function(topicName){this.topicName = topicName;this.subscribe = function(listenerObject, listenerMethod){var tf = listenerMethod||listenerObject;var to = (!listenerMethod) ? dj_global : listenerObject;return dojo.event.kwConnect({srcObj:		this,srcFunc:	"sendMessage",adviceObj:	to,adviceFunc: tf});}
this.unsubscribe = function(listenerObject, listenerMethod){var tf = (!listenerMethod) ? listenerObject : listenerMethod;var to = (!listenerMethod) ? null : listenerObject;return dojo.event.kwDisconnect({srcObj:		this,srcFunc:	"sendMessage",adviceObj:	to,adviceFunc: tf});}
this._getJoinPoint = function(){return dojo.event.MethodJoinPoint.getForMethod(this, "sendMessage");}
this.setSquelch = function(shouldSquelch){this._getJoinPoint().squelch = shouldSquelch;}
this.destroy = function(){this._getJoinPoint().disconnect();}
this.registerPublisher = function(	publisherObject,publisherMethod){dojo.event.connect(publisherObject, publisherMethod, this, "sendMessage");}
this.sendMessage = function(message){}}
dojo.provide("dojo.event.*");dojo.provide("dojo.widget.Manager");dojo.widget.manager = new function(){this.widgets = [];this.widgetIds = [];this.topWidgets = {};var widgetTypeCtr = {};var renderPrefixCache = [];this.getUniqueId = function (widgetType) {var widgetId;do{widgetId = widgetType + "_" + (widgetTypeCtr[widgetType] != undefined ?
++widgetTypeCtr[widgetType] : widgetTypeCtr[widgetType] = 0);}while(this.getWidgetById(widgetId));return widgetId;}
this.add = function(widget){this.widgets.push(widget);if(!widget.extraArgs["id"]){widget.extraArgs["id"] = widget.extraArgs["ID"];}
if(widget.widgetId == ""){if(widget["id"]){widget.widgetId = widget["id"];}else if(widget.extraArgs["id"]){widget.widgetId = widget.extraArgs["id"];}else{widget.widgetId = this.getUniqueId(widget.ns+'_'+widget.widgetType);}}
if(this.widgetIds[widget.widgetId]){dojo.debug("widget ID collision on ID: "+widget.widgetId);}
this.widgetIds[widget.widgetId] = widget;}
this.destroyAll = function(){for(var x=this.widgets.length-1; x>=0; x--){try{this.widgets[x].destroy(true);delete this.widgets[x];}catch(e){ }}}
this.remove = function(widgetIndex){if(dojo.lang.isNumber(widgetIndex)){var tw = this.widgets[widgetIndex].widgetId;delete this.widgetIds[tw];this.widgets.splice(widgetIndex, 1);}else{this.removeById(widgetIndex);}}
this.removeById = function(id) {if(!dojo.lang.isString(id)){id = id["widgetId"];if(!id){ dojo.debug("invalid widget or id passed to removeById"); return; }}
for (var i=0; i<this.widgets.length; i++){if(this.widgets[i].widgetId == id){this.remove(i);break;}}}
this.getWidgetById = function(id){if(dojo.lang.isString(id)){return this.widgetIds[id];}
return id;}
this.getWidgetsByType = function(type){var lt = type.toLowerCase();var getType = (type.indexOf(":") < 0 ?
function(x) { return x.widgetType.toLowerCase(); } :
function(x) { return x.getNamespacedType(); }
);var ret = [];dojo.lang.forEach(this.widgets, function(x){if(getType(x) == lt){ret.push(x);}});return ret;}
this.getWidgetsByFilter = function(unaryFunc, onlyOne){var ret = [];dojo.lang.every(this.widgets, function(x){if(unaryFunc(x)){ret.push(x);if(onlyOne){return false;}}
return true;});return (onlyOne ? ret[0] : ret);}
this.getAllWidgets = function() {return this.widgets.concat();}
this.getWidgetByNode = function( node){var w=this.getAllWidgets();node = dojo.byId(node);for(var i=0; i<w.length; i++){if(w[i].domNode==node){return w[i];}}
return null;}
this.byId = this.getWidgetById;this.byType = this.getWidgetsByType;this.byFilter = this.getWidgetsByFilter;this.byNode = this.getWidgetByNode;var knownWidgetImplementations = {};var widgetPackages = ["dojo.widget"];for (var i=0; i<widgetPackages.length; i++) {widgetPackages[widgetPackages[i]] = true;}
this.registerWidgetPackage = function(pname) {if(!widgetPackages[pname]){widgetPackages[pname] = true;widgetPackages.push(pname);}}
this.getWidgetPackageList = function() {return dojo.lang.map(widgetPackages, function(elt) { return(elt!==true ? elt : undefined); });}
this.getImplementation = function(widgetName, ctorObject, mixins, ns){var impl = this.getImplementationName(widgetName, ns);if(impl){var ret = ctorObject ? new impl(ctorObject) : new impl();return ret;}}
function buildPrefixCache() {for(var renderer in dojo.render){if(dojo.render[renderer]["capable"] === true){var prefixes = dojo.render[renderer].prefixes;for(var i=0; i<prefixes.length; i++){renderPrefixCache.push(prefixes[i].toLowerCase());}}}}
var findImplementationInModule = function(lowerCaseWidgetName, module){if(!module){return null;}
for(var i=0, l=renderPrefixCache.length, widgetModule; i<=l; i++){widgetModule = (i<l ? module[renderPrefixCache[i]] : module);if(!widgetModule){continue;}
for(var name in widgetModule){if(name.toLowerCase() == lowerCaseWidgetName){return widgetModule[name];}}}
return null;}
var findImplementation = function(lowerCaseWidgetName, moduleName){var module = dojo.evalObjPath(moduleName, false);return (module ? findImplementationInModule(lowerCaseWidgetName, module) : null);}
this.getImplementationName = function(widgetName, ns){var lowerCaseWidgetName = widgetName.toLowerCase();ns=ns||"dojo";var imps = knownWidgetImplementations[ns] || (knownWidgetImplementations[ns]={});var impl = imps[lowerCaseWidgetName];if(impl){return impl;}
if(!renderPrefixCache.length){buildPrefixCache();}
var nsObj = dojo.ns.get(ns);if(!nsObj){dojo.ns.register(ns, ns + '.widget');nsObj = dojo.ns.get(ns);}
if(nsObj){nsObj.resolve(widgetName);}
impl = findImplementation(lowerCaseWidgetName, nsObj.module);if(impl){return(imps[lowerCaseWidgetName] = impl)};nsObj = dojo.ns.require(ns);if((nsObj)&&(nsObj.resolver)){nsObj.resolve(widgetName);impl = findImplementation(lowerCaseWidgetName, nsObj.module);if(impl){return(imps[lowerCaseWidgetName] = impl)};}
dojo.deprecated('dojo.widget.Manager.getImplementationName','Could not locate widget implementation for "' + widgetName + '" in "' + nsObj.module + '" registered to namespace "' + nsObj.name + '". '
+ "Developers must specify correct namespaces for all non-Dojo widgets", "0.5");for(var i=0; i<widgetPackages.length; i++){impl = findImplementation(lowerCaseWidgetName, widgetPackages[i]);if(impl){return(imps[lowerCaseWidgetName] = impl)};}
throw new Error('Could not locate widget implementation for "' + widgetName + '" in "' + nsObj.module + '" registered to namespace "' + nsObj.name + '"');}
this.resizing=false;this.onWindowResized = function(){if(this.resizing){return;}
try{this.resizing=true;for(var id in this.topWidgets){var child = this.topWidgets[id];if(child.checkSize ){child.checkSize();}}}catch(e){}finally{this.resizing=false;}}
if(typeof window != "undefined") {dojo.addOnLoad(this, 'onWindowResized');dojo.event.connect(window, 'onresize', this, 'onWindowResized');}};(function(){var dw = dojo.widget;var dwm = dw.manager;var h = dojo.lang.curry(dojo.lang, "hitch", dwm);var g = function(oldName, newName){dw[(newName||oldName)] = h(oldName);}
g("add", "addWidget");g("destroyAll", "destroyAllWidgets");g("remove", "removeWidget");g("removeById", "removeWidgetById");g("getWidgetById");g("getWidgetById", "byId");g("getWidgetsByType");g("getWidgetsByFilter");g("getWidgetsByType", "byType");g("getWidgetsByFilter", "byFilter");g("getWidgetByNode", "byNode");dw.all = function(n){var widgets = dwm.getAllWidgets.apply(dwm, arguments);if(arguments.length > 0) {return widgets[n];}
return widgets;}
g("registerWidgetPackage");g("getImplementation", "getWidgetImplementation");g("getImplementationName", "getWidgetImplementationName");dw.widgets = dwm.widgets;dw.widgetIds = dwm.widgetIds;dw.root = dwm.root;})();dojo.provide("dojo.uri.Uri");dojo.uri = new function() {var authorityPattern = new RegExp("^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$");var uriPattern = new RegExp("(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$");this.dojoUri = function (uri) {return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri(), uri);}
this.moduleUri = function(module, uri){var loc = dojo.hostenv.getModuleSymbols(module).join('/');if(!loc){return null;}
if(loc.lastIndexOf("/") != loc.length-1){loc += "/";}
return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri()+loc,uri);}
this.Uri = function () {var uri = arguments[0];for (var i = 1; i < arguments.length; i++) {if(!arguments[i]) { continue; }
var relobj = new dojo.uri.Uri(arguments[i].toString());var uriobj = new dojo.uri.Uri(uri.toString());if ((relobj.path=="")&&(relobj.scheme==null)&&(relobj.authority==null)&&(relobj.query==null)) {if (relobj.fragment != null) { uriobj.fragment = relobj.fragment; }
relobj = uriobj;}
if (relobj.scheme != null && relobj.authority != null)
uri = "";if (relobj.scheme != null) { uri += relobj.scheme + ":"; }
if (relobj.authority != null) { uri += "//" + relobj.authority; }
uri += relobj.path;if (relobj.query != null) { uri += "?" + relobj.query; }
if (relobj.fragment != null) { uri += "#" + relobj.fragment; }}
this.uri = uri.toString();var r = this.uri.match(uriPattern);this.scheme = r[2] || (r[1] ? "" : null);this.authority = r[4] || (r[3] ? "" : null);this.path = r[5];this.query = r[7] || (r[6] ? "" : null);this.fragment  = r[9] || (r[8] ? "" : null);if (this.authority != null) {r = this.authority.match(authorityPattern);this.user = r[3] || null;this.password = r[4] || null;this.host = r[5];this.port = r[7] || null;}
this.toString = function(){ return this.uri; }}};dojo.provide("dojo.uri.*");dojo.provide("dojo.html.common");dojo.lang.mixin(dojo.html, dojo.dom);dojo.html.getEventTarget = function(evt){if(!evt) { evt = dojo.global().event || {}};var t = (evt.srcElement ? evt.srcElement : (evt.target ? evt.target : null));while((t)&&(t.nodeType!=1)){ t = t.parentNode; }
return t;}
dojo.html.getViewport = function(){var _window = dojo.global();var _document = dojo.doc();var w = 0;var h = 0;if(dojo.render.html.mozilla){w = _document.documentElement.clientWidth;h = _window.innerHeight;}else if(!dojo.render.html.opera && _window.innerWidth){w = _window.innerWidth;h = _window.innerHeight;} else if (!dojo.render.html.opera && dojo.exists(_document, "documentElement.clientWidth")){var w2 = _document.documentElement.clientWidth;if(!w || w2 && w2 < w) {w = w2;}
h = _document.documentElement.clientHeight;} else if (dojo.body().clientWidth){w = dojo.body().clientWidth;h = dojo.body().clientHeight;}
return { width: w, height: h };}
dojo.html.getScroll = function(){var _window = dojo.global();var _document = dojo.doc();var top = _window.pageYOffset || _document.documentElement.scrollTop || dojo.body().scrollTop || 0;var left = _window.pageXOffset || _document.documentElement.scrollLeft || dojo.body().scrollLeft || 0;return {top: top,left: left,offset:{ x: left, y: top }};}
dojo.html.getParentByType = function(node, type) {var _document = dojo.doc();var parent = dojo.byId(node);type = type.toLowerCase();while((parent)&&(parent.nodeName.toLowerCase()!=type)){if(parent==(_document["body"]||_document["documentElement"])){return null;}
parent = parent.parentNode;}
return parent;}
dojo.html.getAttribute = function(node, attr){node = dojo.byId(node);if((!node)||(!node.getAttribute)){return null;}
var ta = typeof attr == 'string' ? attr : new String(attr);var v = node.getAttribute(ta.toUpperCase());if((v)&&(typeof v == 'string')&&(v!="")){return v;}
if(v && v.value){return v.value;}
if((node.getAttributeNode)&&(node.getAttributeNode(ta))){return (node.getAttributeNode(ta)).value;}else if(node.getAttribute(ta)){return node.getAttribute(ta);}else if(node.getAttribute(ta.toLowerCase())){return node.getAttribute(ta.toLowerCase());}
return null;}
dojo.html.hasAttribute = function(node, attr){return dojo.html.getAttribute(dojo.byId(node), attr) ? true : false;}
dojo.html.getCursorPosition = function(e){e = e || dojo.global().event;var cursor = {x:0, y:0};if(e.pageX || e.pageY){cursor.x = e.pageX;cursor.y = e.pageY;}else{var de = dojo.doc().documentElement;var db = dojo.body();cursor.x = e.clientX + ((de||db)["scrollLeft"]) - ((de||db)["clientLeft"]);cursor.y = e.clientY + ((de||db)["scrollTop"]) - ((de||db)["clientTop"]);}
return cursor;}
dojo.html.isTag = function(node) {node = dojo.byId(node);if(node && node.tagName) {for (var i=1; i<arguments.length; i++){if (node.tagName.toLowerCase()==String(arguments[i]).toLowerCase()){return String(arguments[i]).toLowerCase();}}}
return "";}
if(dojo.render.html.ie && !dojo.render.html.ie70){if(window.location.href.substr(0,6).toLowerCase() != "https:"){(function(){var xscript = dojo.doc().createElement('script');xscript.src = "javascript:'dojo.html.createExternalElement=function(doc, tag){ return doc.createElement(tag); }'";dojo.doc().getElementsByTagName("head")[0].appendChild(xscript);})();}}else{dojo.html.createExternalElement = function(doc, tag){return doc.createElement(tag);}}
dojo.provide("dojo.a11y");dojo.a11y = {imgPath:dojo.uri.dojoUri("src/widget/templates/images"),doAccessibleCheck: true,accessible: null,checkAccessible: function(){if(this.accessible === null){this.accessible = false;if(this.doAccessibleCheck == true){this.accessible = this.testAccessible();}}
return this.accessible;},testAccessible: function(){this.accessible = false;if (dojo.render.html.ie || dojo.render.html.mozilla){var div = document.createElement("div");div.style.backgroundImage = "url(\"" + this.imgPath + "/tab_close.gif\")";dojo.body().appendChild(div);var bkImg = null;if (window.getComputedStyle  ) {var cStyle = getComputedStyle(div, "");bkImg = cStyle.getPropertyValue("background-image");}else{bkImg = div.currentStyle.backgroundImage;}
var bUseImgElem = false;if (bkImg != null && (bkImg == "none" || bkImg == "url(invalid-url:)" )) {this.accessible = true;}
dojo.body().removeChild(div);}
return this.accessible;},setCheckAccessible: function( bTest){this.doAccessibleCheck = bTest;},setAccessibleMode: function(){if (this.accessible === null){if (this.checkAccessible()){dojo.render.html.prefixes.unshift("a11y");}}
return this.accessible;}};dojo.provide("dojo.widget.Widget");dojo.declare("dojo.widget.Widget", null,function(){this.children = [];this.extraArgs = {};},{parent: null,isTopLevel:  false,disabled: false,isContainer: false,widgetId: "",widgetType: "Widget",ns: "dojo",getNamespacedType: function(){return (this.ns ? this.ns + ":" + this.widgetType : this.widgetType).toLowerCase();},toString: function(){return '[Widget ' + this.getNamespacedType() + ', ' + (this.widgetId || 'NO ID') + ']';},repr: function(){return this.toString();},enable: function(){this.disabled = false;},disable: function(){this.disabled = true;},onResized: function(){this.notifyChildrenOfResize();},notifyChildrenOfResize: function(){for(var i=0; i<this.children.length; i++){var child = this.children[i];if( child.onResized ){child.onResized();}}},create: function(args, fragment, parent, ns){if(ns){this.ns = ns;}
this.satisfyPropertySets(args, fragment, parent);this.mixInProperties(args, fragment, parent);this.postMixInProperties(args, fragment, parent);dojo.widget.manager.add(this);this.buildRendering(args, fragment, parent);this.initialize(args, fragment, parent);this.postInitialize(args, fragment, parent);this.postCreate(args, fragment, parent);return this;},destroy: function(finalize){if(this.parent){this.parent.removeChild(this);}
this.destroyChildren();this.uninitialize();this.destroyRendering(finalize);dojo.widget.manager.removeById(this.widgetId);},destroyChildren: function(){var widget;var i=0;while(this.children.length > i){widget = this.children[i];if (widget instanceof dojo.widget.Widget) {this.removeChild(widget);widget.destroy();continue;}
i++;}},getChildrenOfType: function(type, recurse){var ret = [];var isFunc = dojo.lang.isFunction(type);if(!isFunc){type = type.toLowerCase();}
for(var x=0; x<this.children.length; x++){if(isFunc){if(this.children[x] instanceof type){ret.push(this.children[x]);}}else{if(this.children[x].widgetType.toLowerCase() == type){ret.push(this.children[x]);}}
if(recurse){ret = ret.concat(this.children[x].getChildrenOfType(type, recurse));}}
return ret;},getDescendants: function(){var result = [];var stack = [this];var elem;while ((elem = stack.pop())){result.push(elem);if (elem.children) {dojo.lang.forEach(elem.children, function(elem) { stack.push(elem); });}}
return result;},isFirstChild: function(){return this === this.parent.children[0];},isLastChild: function() {return this === this.parent.children[this.parent.children.length-1];},satisfyPropertySets: function(args){return args;},mixInProperties: function(args, frag){if((args["fastMixIn"])||(frag["fastMixIn"])){for(var x in args){this[x] = args[x];}
return;}
var undef;var lcArgs = dojo.widget.lcArgsCache[this.widgetType];if ( lcArgs == null ){lcArgs = {};for(var y in this){lcArgs[((new String(y)).toLowerCase())] = y;}
dojo.widget.lcArgsCache[this.widgetType] = lcArgs;}
var visited = {};for(var x in args){if(!this[x]){var y = lcArgs[(new String(x)).toLowerCase()];if(y){args[y] = args[x];x = y;}}
if(visited[x]){ continue; }
visited[x] = true;if((typeof this[x]) != (typeof undef)){if(typeof args[x] != "string"){this[x] = args[x];}else{if(dojo.lang.isString(this[x])){this[x] = args[x];}else if(dojo.lang.isNumber(this[x])){this[x] = new Number(args[x]);}else if(dojo.lang.isBoolean(this[x])){this[x] = (args[x].toLowerCase()=="false") ? false : true;}else if(dojo.lang.isFunction(this[x])){if(args[x].search(/[^\w\.]+/i) == -1){this[x] = dojo.evalObjPath(args[x], false);}else{var tn = dojo.lang.nameAnonFunc(new Function(args[x]), this);dojo.event.kwConnect({srcObj: this,srcFunc: x,adviceObj: this,adviceFunc: tn});}}else if(dojo.lang.isArray(this[x])){this[x] = args[x].split(";");} else if (this[x] instanceof Date) {this[x] = new Date(Number(args[x]));}else if(typeof this[x] == "object"){if (this[x] instanceof dojo.uri.Uri){this[x] = dojo.uri.dojoUri(args[x]);}else{var pairs = args[x].split(";");for(var y=0; y<pairs.length; y++){var si = pairs[y].indexOf(":");if((si != -1)&&(pairs[y].length>si)){this[x][pairs[y].substr(0, si).replace(/^\s+|\s+$/g, "")] = pairs[y].substr(si+1);}}}}else{this[x] = args[x];}}}else{this.extraArgs[x.toLowerCase()] = args[x];}}},postMixInProperties: function(args, frag, parent){},initialize: function(args, frag, parent){return false;},postInitialize: function(args, frag, parent){return false;},postCreate: function(args, frag, parent){return false;},uninitialize: function(){return false;},buildRendering: function(args, frag, parent){dojo.unimplemented("dojo.widget.Widget.buildRendering, on "+this.toString()+", ");return false;},destroyRendering: function(){dojo.unimplemented("dojo.widget.Widget.destroyRendering");return false;},addedTo: function(parent){},addChild: function(child){dojo.unimplemented("dojo.widget.Widget.addChild");return false;},removeChild: function(widget){for(var x=0; x<this.children.length; x++){if(this.children[x] === widget){this.children.splice(x, 1);widget.parent=null;break;}}
return widget;},getPreviousSibling: function(){var idx = this.getParentIndex();if (idx<=0) return null;return this.parent.children[idx-1];},getSiblings: function(){return this.parent.children;},getParentIndex: function(){return dojo.lang.indexOf(this.parent.children, this, true);},getNextSibling: function(){var idx = this.getParentIndex();if (idx == this.parent.children.length-1){return null;}
if (idx < 0){return null;}
return this.parent.children[idx+1];}});dojo.widget.lcArgsCache = {};dojo.widget.tags = {};dojo.widget.tags.addParseTreeHandler = function(type){dojo.deprecated("addParseTreeHandler", ". ParseTreeHandlers are now reserved for components. Any unfiltered DojoML tag without a ParseTreeHandler is assumed to be a widget", "0.5");}
dojo.widget.tags["dojo:propertyset"] = function(fragment, widgetParser, parentComp){var properties = widgetParser.parseProperties(fragment["dojo:propertyset"]);}
dojo.widget.tags["dojo:connect"] = function(fragment, widgetParser, parentComp){var properties = widgetParser.parseProperties(fragment["dojo:connect"]);}
dojo.widget.buildWidgetFromParseTree = function(				type,frag,parser,parentComp,insertionIndex,localProps){dojo.a11y.setAccessibleMode();var stype = type.split(":");stype = (stype.length == 2) ? stype[1] : type;var localProperties = localProps || parser.parseProperties(frag[frag["ns"]+":"+stype]);var twidget = dojo.widget.manager.getImplementation(stype,null,null,frag["ns"]);if(!twidget){throw new Error('cannot find "' + type + '" widget');}else if (!twidget.create){throw new Error('"' + type + '" widget object has no "create" method and does not appear to implement *Widget');}
localProperties["dojoinsertionindex"] = insertionIndex;var ret = twidget.create(localProperties, frag, parentComp, frag["ns"]);return ret;}
dojo.widget.defineWidget = function(widgetClass, renderer, superclasses, init, props){if(dojo.lang.isString(arguments[3])){dojo.widget._defineWidget(arguments[0], arguments[3], arguments[1], arguments[4], arguments[2]);}else{var args = [ arguments[0] ], p = 3;if(dojo.lang.isString(arguments[1])){args.push(arguments[1], arguments[2]);}else{args.push('', arguments[1]);p = 2;}
if(dojo.lang.isFunction(arguments[p])){args.push(arguments[p], arguments[p+1]);}else{args.push(null, arguments[p]);}
dojo.widget._defineWidget.apply(this, args);}}
dojo.widget.defineWidget.renderers = "html|svg|vml";dojo.widget._defineWidget = function(widgetClass , renderer , superclasses , init , props ){var module = widgetClass.split(".");var type = module.pop();var regx = "\\.(" + (renderer ? renderer + '|' : '') + dojo.widget.defineWidget.renderers + ")\\.";var r = widgetClass.search(new RegExp(regx));module = (r < 0 ? module.join(".") : widgetClass.substr(0, r));dojo.widget.manager.registerWidgetPackage(module);var pos = module.indexOf(".");var nsName = (pos > -1) ? module.substring(0,pos) : module;props=(props)||{};props.widgetType = type;if((!init)&&(props["classConstructor"])){init = props.classConstructor;delete props.classConstructor;}
dojo.declare(widgetClass, superclasses, init, props);}
dojo.provide("dojo.widget.Parse");dojo.widget.Parse = function(fragment){this.propertySetsList = [];this.fragment = fragment;this.createComponents = function(frag, parentComp){var comps = [];var built = false;try{if(frag && frag.tagName && (frag != frag.nodeRef)){var djTags = dojo.widget.tags;var tna = String(frag.tagName).split(";");for(var x=0; x<tna.length; x++){var ltn = tna[x].replace(/^\s+|\s+$/g, "").toLowerCase();frag.tagName = ltn;var ret;if(djTags[ltn]){built = true;ret = djTags[ltn](frag, this, parentComp, frag.index);comps.push(ret);}else{if(ltn.indexOf(":") == -1){ltn = "dojo:"+ltn;}
ret = dojo.widget.buildWidgetFromParseTree(ltn, frag, this, parentComp, frag.index);if(ret){built = true;comps.push(ret);}}}}}catch(e){dojo.debug("dojo.widget.Parse: error:", e);}
if(!built){comps = comps.concat(this.createSubComponents(frag, parentComp));}
return comps;}
this.createSubComponents = function(fragment, parentComp){var frag, comps = [];for(var item in fragment){frag = fragment[item];if(frag && typeof frag == "object"
&&(frag!=fragment.nodeRef)
&&(frag!=fragment.tagName)
&&(!dojo.dom.isNode(frag))){comps = comps.concat(this.createComponents(frag, parentComp));}}
return comps;}
this.parsePropertySets = function(fragment){return [];}
this.parseProperties = function(fragment){var properties = {};for(var item in fragment){if((fragment[item] == fragment.tagName)||(fragment[item] == fragment.nodeRef)){}else{var frag = fragment[item];if(frag.tagName && dojo.widget.tags[frag.tagName.toLowerCase()]){}else if(frag[0] && frag[0].value!="" && frag[0].value!=null){try{if(item.toLowerCase() == "dataprovider"){var _this = this;this.getDataProvider(_this, frag[0].value);properties.dataProvider = this.dataProvider;}
properties[item] = frag[0].value;var nestedProperties = this.parseProperties(frag);for(var property in nestedProperties){properties[property] = nestedProperties[property];}}catch(e){ dojo.debug(e); }}
switch(item.toLowerCase()){case "checked":
case "disabled":
if (typeof properties[item] != "boolean"){properties[item] = true;}
break;}}}
return properties;}
this.getDataProvider = function(objRef, dataUrl){dojo.io.bind({url: dataUrl,load: function(type, evaldObj){if(type=="load"){objRef.dataProvider = evaldObj;}},mimetype: "text/javascript",sync: true});}
this.getPropertySetById = function(propertySetId){for(var x = 0; x < this.propertySetsList.length; x++){if(propertySetId == this.propertySetsList[x]["id"][0].value){return this.propertySetsList[x];}}
return "";}
this.getPropertySetsByType = function(componentType){var propertySets = [];for(var x=0; x < this.propertySetsList.length; x++){var cpl = this.propertySetsList[x];var cpcc = cpl.componentClass || cpl.componentType || null;var propertySetId = this.propertySetsList[x]["id"][0].value;if(cpcc && (propertySetId == cpcc[0].value)){propertySets.push(cpl);}}
return propertySets;}
this.getPropertySets = function(fragment){var ppl = "dojo:propertyproviderlist";var propertySets = [];var tagname = fragment.tagName;if(fragment[ppl]){var propertyProviderIds = fragment[ppl].value.split(" ");for(var propertySetId in propertyProviderIds){if((propertySetId.indexOf("..")==-1)&&(propertySetId.indexOf("://")==-1)){var propertySet = this.getPropertySetById(propertySetId);if(propertySet != ""){propertySets.push(propertySet);}}else{}}}
return this.getPropertySetsByType(tagname).concat(propertySets);}
this.createComponentFromScript = function(nodeRef, componentName, properties, ns){properties.fastMixIn = true;var ltn = (ns || "dojo") + ":" + componentName.toLowerCase();if(dojo.widget.tags[ltn]){return [dojo.widget.tags[ltn](properties, this, null, null, properties)];}
return [dojo.widget.buildWidgetFromParseTree(ltn, properties, this, null, null, properties)];}}
dojo.widget._parser_collection = {"dojo": new dojo.widget.Parse() };dojo.widget.getParser = function(name){if(!name){ name = "dojo"; }
if(!this._parser_collection[name]){this._parser_collection[name] = new dojo.widget.Parse();}
return this._parser_collection[name];}
dojo.widget.createWidget = function(name, props, refNode, position){var isNode = false;var isNameStr = (typeof name == "string");if(isNameStr){var pos = name.indexOf(":");var ns = (pos > -1) ? name.substring(0,pos) : "dojo";if(pos > -1){ name = name.substring(pos+1); }
var lowerCaseName = name.toLowerCase();var namespacedName = ns + ":" + lowerCaseName;isNode = (dojo.byId(name) && !dojo.widget.tags[namespacedName]);}
if((arguments.length == 1) && (isNode || !isNameStr)){var xp = new dojo.xml.Parse();var tn = isNode ? dojo.byId(name) : name;return dojo.widget.getParser().createComponents(xp.parseElement(tn, null, true))[0];}
function fromScript(placeKeeperNode, name, props, ns){props[namespacedName] = {dojotype: [{value: lowerCaseName}],nodeRef: placeKeeperNode,fastMixIn: true};props.ns = ns;return dojo.widget.getParser().createComponentFromScript(placeKeeperNode, name, props, ns);}
props = props||{};var notRef = false;var tn = null;var h = dojo.render.html.capable;if(h){tn = document.createElement("span");}
if(!refNode){notRef = true;refNode = tn;if(h){dojo.body().appendChild(refNode);}}else if(position){dojo.dom.insertAtPosition(tn, refNode, position);}else{tn = refNode;}
var widgetArray = fromScript(tn, name.toLowerCase(), props, ns);if(	(!widgetArray)||(!widgetArray[0])||
(typeof widgetArray[0].widgetType == "undefined") ){throw new Error("createWidget: Creation of \"" + name + "\" widget failed.");}
try{if(notRef && widgetArray[0].domNode.parentNode){widgetArray[0].domNode.parentNode.removeChild(widgetArray[0].domNode);}}catch(e){dojo.debug(e);}
return widgetArray[0];}
dojo.provide("dojo.html.style");dojo.html.getClass = function(node){node = dojo.byId(node);if(!node){ return ""; }
var cs = "";if(node.className){cs = node.className;}else if(dojo.html.hasAttribute(node, "class")){cs = dojo.html.getAttribute(node, "class");}
return cs.replace(/^\s+|\s+$/g, "");}
dojo.html.getClasses = function(node) {var c = dojo.html.getClass(node);return (c == "") ? [] : c.split(/\s+/g);}
dojo.html.hasClass = function(node, classname){return (new RegExp('(^|\\s+)'+classname+'(\\s+|$)')).test(dojo.html.getClass(node))}
dojo.html.prependClass = function(node, classStr){classStr += " " + dojo.html.getClass(node);return dojo.html.setClass(node, classStr);}
dojo.html.addClass = function(node, classStr){if (dojo.html.hasClass(node, classStr)) {return false;}
classStr = (dojo.html.getClass(node) + " " + classStr).replace(/^\s+|\s+$/g,"");return dojo.html.setClass(node, classStr);}
dojo.html.setClass = function(node, classStr){node = dojo.byId(node);var cs = new String(classStr);try{if(typeof node.className == "string"){node.className = cs;}else if(node.setAttribute){node.setAttribute("class", classStr);node.className = cs;}else{return false;}}catch(e){dojo.debug("dojo.html.setClass() failed", e);}
return true;}
dojo.html.removeClass = function(node, classStr, allowPartialMatches){try{if (!allowPartialMatches) {var newcs = dojo.html.getClass(node).replace(new RegExp('(^|\\s+)'+classStr+'(\\s+|$)'), "$1$2");} else {var newcs = dojo.html.getClass(node).replace(classStr,'');}
dojo.html.setClass(node, newcs);}catch(e){dojo.debug("dojo.html.removeClass() failed", e);}
return true;}
dojo.html.replaceClass = function(node, newClass, oldClass) {dojo.html.removeClass(node, oldClass);dojo.html.addClass(node, newClass);}
dojo.html.classMatchType = {ContainsAll : 0,ContainsAny : 1,IsOnly : 2}
dojo.html.getElementsByClass = function(
classStr,parent,nodeType,classMatchType,useNonXpath
){useNonXpath = false;var _document = dojo.doc();parent = dojo.byId(parent) || _document;var classes = classStr.split(/\s+/g);var nodes = [];if( classMatchType != 1 && classMatchType != 2 ) classMatchType = 0;var reClass = new RegExp("(\\s|^)((" + classes.join(")|(") + "))(\\s|$)");var srtLength = classes.join(" ").length;var candidateNodes = [];if(!useNonXpath && _document.evaluate) {var xpath = ".//" + (nodeType || "*") + "[contains(";if(classMatchType != dojo.html.classMatchType.ContainsAny){xpath += "concat(' ',@class,' '), ' " +
classes.join(" ') and contains(concat(' ',@class,' '), ' ") +
" ')";if (classMatchType == 2) {xpath += " and string-length(@class)="+srtLength+"]";}else{xpath += "]";}}else{xpath += "concat(' ',@class,' '), ' " +
classes.join(" ') or contains(concat(' ',@class,' '), ' ") +
" ')]";}
var xpathResult = _document.evaluate(xpath, parent, null, XPathResult.ANY_TYPE, null);var result = xpathResult.iterateNext();while(result){try{candidateNodes.push(result);result = xpathResult.iterateNext();}catch(e){ break; }}
return candidateNodes;}else{if(!nodeType){nodeType = "*";}
candidateNodes = parent.getElementsByTagName(nodeType);var node, i = 0;outer:
while(node = candidateNodes[i++]){var nodeClasses = dojo.html.getClasses(node);if(nodeClasses.length == 0){ continue outer; }
var matches = 0;for(var j = 0; j < nodeClasses.length; j++){if(reClass.test(nodeClasses[j])){if(classMatchType == dojo.html.classMatchType.ContainsAny){nodes.push(node);continue outer;}else{matches++;}}else{if(classMatchType == dojo.html.classMatchType.IsOnly){continue outer;}}}
if(matches == classes.length){if(	(classMatchType == dojo.html.classMatchType.IsOnly)&&
(matches == nodeClasses.length)){nodes.push(node);}else if(classMatchType == dojo.html.classMatchType.ContainsAll){nodes.push(node);}}}
return nodes;}}
dojo.html.getElementsByClassName = dojo.html.getElementsByClass;dojo.html.toCamelCase = function(selector){var arr = selector.split('-'), cc = arr[0];for(var i = 1; i < arr.length; i++) {cc += arr[i].charAt(0).toUpperCase() + arr[i].substring(1);}
return cc;}
dojo.html.toSelectorCase = function(selector){return selector.replace(/([A-Z])/g, "-$1" ).toLowerCase();}
dojo.html.getComputedStyle = function(node, cssSelector, inValue){node = dojo.byId(node);var cssSelector = dojo.html.toSelectorCase(cssSelector);var property = dojo.html.toCamelCase(cssSelector);if(!node || !node.style){return inValue;} else if (document.defaultView && dojo.html.isDescendantOf(node, node.ownerDocument)){try{var cs = document.defaultView.getComputedStyle(node, "");if(cs){return cs.getPropertyValue(cssSelector);}}catch(e){if(node.style.getPropertyValue){return node.style.getPropertyValue(cssSelector);} else {return inValue;}}} else if(node.currentStyle){return node.currentStyle[property];}
if(node.style.getPropertyValue){return node.style.getPropertyValue(cssSelector);}else{return inValue;}}
dojo.html.getStyleProperty = function(node, cssSelector){node = dojo.byId(node);return (node && node.style ? node.style[dojo.html.toCamelCase(cssSelector)] : undefined);}
dojo.html.getStyle = function(node, cssSelector){var value = dojo.html.getStyleProperty(node, cssSelector);return (value ? value : dojo.html.getComputedStyle(node, cssSelector));}
dojo.html.setStyle = function(node, cssSelector, value){node = dojo.byId(node);if(node && node.style){var camelCased = dojo.html.toCamelCase(cssSelector);node.style[camelCased] = value;}}
dojo.html.setStyleText = function (target, text) {try {target.style.cssText = text;} catch (e) {target.setAttribute("style", text);}}
dojo.html.copyStyle = function(target, source){if(!source.style.cssText){target.setAttribute("style", source.getAttribute("style"));}else{target.style.cssText = source.style.cssText;}
dojo.html.addClass(target, dojo.html.getClass(source));}
dojo.html.getUnitValue = function(node, cssSelector, autoIsZero){var s = dojo.html.getComputedStyle(node, cssSelector);if((!s)||((s == 'auto')&&(autoIsZero))){return { value: 0, units: 'px' };}
var match = s.match(/(\-?[\d.]+)([a-z%]*)/i);if (!match){return dojo.html.getUnitValue.bad;}
return { value: Number(match[1]), units: match[2].toLowerCase() };}
dojo.html.getUnitValue.bad = { value: NaN, units: '' };dojo.html.getPixelValue = function(node, cssSelector, autoIsZero){var result = dojo.html.getUnitValue(node, cssSelector, autoIsZero);if(isNaN(result.value)){return 0;}
if((result.value)&&(result.units != 'px')){return NaN;}
return result.value;}
dojo.html.setPositivePixelValue = function(node, selector, value){if(isNaN(value)){return false;}
node.style[selector] = Math.max(0, value) + 'px';return true;}
dojo.html.styleSheet = null;dojo.html.insertCssRule = function(selector, declaration, index) {if (!dojo.html.styleSheet) {if (document.createStyleSheet) {dojo.html.styleSheet = document.createStyleSheet();} else if (document.styleSheets[0]) {dojo.html.styleSheet = document.styleSheets[0];} else {return null;}}
if (arguments.length < 3) {if (dojo.html.styleSheet.cssRules) {index = dojo.html.styleSheet.cssRules.length;} else if (dojo.html.styleSheet.rules) {index = dojo.html.styleSheet.rules.length;} else {return null;}}
if (dojo.html.styleSheet.insertRule) {var rule = selector + " { " + declaration + " }";return dojo.html.styleSheet.insertRule(rule, index);} else if (dojo.html.styleSheet.addRule) {return dojo.html.styleSheet.addRule(selector, declaration, index);} else {return null;}}
dojo.html.removeCssRule = function(index){if(!dojo.html.styleSheet){dojo.debug("no stylesheet defined for removing rules");return false;}
if(dojo.render.html.ie){if(!index){index = dojo.html.styleSheet.rules.length;dojo.html.styleSheet.removeRule(index);}}else if(document.styleSheets[0]){if(!index){index = dojo.html.styleSheet.cssRules.length;}
dojo.html.styleSheet.deleteRule(index);}
return true;}
dojo.html._insertedCssFiles = [];dojo.html.insertCssFile = function(URI, doc, checkDuplicates, fail_ok){if(!URI){ return; }
if(!doc){ doc = document; }
var cssStr = dojo.hostenv.getText(URI, false, fail_ok);if(cssStr===null){ return; }
cssStr = dojo.html.fixPathsInCssText(cssStr, URI);if(checkDuplicates){var idx = -1, node, ent = dojo.html._insertedCssFiles;for(var i = 0; i < ent.length; i++){if((ent[i].doc == doc) && (ent[i].cssText == cssStr)){idx = i; node = ent[i].nodeRef;break;}}
if(node){var styles = doc.getElementsByTagName("style");for(var i = 0; i < styles.length; i++){if(styles[i] == node){return;}}
dojo.html._insertedCssFiles.shift(idx, 1);}}
var style = dojo.html.insertCssText(cssStr, doc);dojo.html._insertedCssFiles.push({'doc': doc, 'cssText': cssStr, 'nodeRef': style});if(style && djConfig.isDebug){style.setAttribute("dbgHref", URI);}
return style;}
dojo.html.insertCssText = function(cssStr, doc, URI){if(!cssStr){return;}
if(!doc){ doc = document; }
if(URI){cssStr = dojo.html.fixPathsInCssText(cssStr, URI);}
var style = doc.createElement("style");style.setAttribute("type", "text/css");var head = doc.getElementsByTagName("head")[0];if(!head){dojo.debug("No head tag in document, aborting styles");return;}else{head.appendChild(style);}
if(style.styleSheet){var setFunc = function(){try{style.styleSheet.cssText = cssStr;}catch(e){ dojo.debug(e); }};if(style.styleSheet.disabled){setTimeout(setFunc, 10);}else{setFunc();}}else{var cssText = doc.createTextNode(cssStr);style.appendChild(cssText);}
return style;}
dojo.html.fixPathsInCssText = function(cssStr, URI){if(!cssStr || !URI){ return; }
var match, str = "", url = "", urlChrs = "[\\t\\s\\w\\(\\)\\/\\.\\\\'\"-:#=&?~]+";var regex = new RegExp('url\\(\\s*('+urlChrs+')\\s*\\)');var regexProtocol = /(file|https?|ftps?):\/\//;regexTrim = new RegExp("^[\\s]*(['\"]?)("+urlChrs+")\\1[\\s]*?$");if(dojo.render.html.ie55 || dojo.render.html.ie60){var regexIe = new RegExp("AlphaImageLoader\\((.*)src\=['\"]("+urlChrs+")['\"]");while(match = regexIe.exec(cssStr)){url = match[2].replace(regexTrim, "$2");if(!regexProtocol.exec(url)){url = (new dojo.uri.Uri(URI, url).toString());}
str += cssStr.substring(0, match.index) + "AlphaImageLoader(" + match[1] + "src='" + url + "'";cssStr = cssStr.substr(match.index + match[0].length);}
cssStr = str + cssStr;str = "";}
while(match = regex.exec(cssStr)){url = match[1].replace(regexTrim, "$2");if(!regexProtocol.exec(url)){url = (new dojo.uri.Uri(URI, url).toString());}
str += cssStr.substring(0, match.index) + "url(" + url + ")";cssStr = cssStr.substr(match.index + match[0].length);}
return str + cssStr;}
dojo.html.setActiveStyleSheet = function(title){var i = 0, a, els = dojo.doc().getElementsByTagName("link");while (a = els[i++]) {if(a.getAttribute("rel").indexOf("style") != -1 && a.getAttribute("title")){a.disabled = true;if (a.getAttribute("title") == title) { a.disabled = false; }}}}
dojo.html.getActiveStyleSheet = function(){var i = 0, a, els = dojo.doc().getElementsByTagName("link");while (a = els[i++]) {if (a.getAttribute("rel").indexOf("style") != -1
&& a.getAttribute("title")
&& !a.disabled
){return a.getAttribute("title");}}
return null;}
dojo.html.getPreferredStyleSheet = function(){var i = 0, a, els = dojo.doc().getElementsByTagName("link");while (a = els[i++]) {if(a.getAttribute("rel").indexOf("style") != -1
&& a.getAttribute("rel").indexOf("alt") == -1
&& a.getAttribute("title")
){return a.getAttribute("title");}}
return null;}
dojo.html.applyBrowserClass = function(node){var drh=dojo.render.html;var classes = {dj_ie: drh.ie,dj_ie55: drh.ie55,dj_ie6: drh.ie60,dj_ie7: drh.ie70,dj_iequirks: drh.ie && drh.quirks,dj_opera: drh.opera,dj_opera8: drh.opera && (Math.floor(dojo.render.version)==8),dj_opera9: drh.opera && (Math.floor(dojo.render.version)==9),dj_khtml: drh.khtml,dj_safari: drh.safari,dj_gecko: drh.mozilla};for(var p in classes){if(classes[p]){dojo.html.addClass(node, p);}}};dojo.provide("dojo.widget.DomWidget");dojo.widget._cssFiles = {};dojo.widget._cssStrings = {};dojo.widget._templateCache = {};dojo.widget.defaultStrings = {dojoRoot: dojo.hostenv.getBaseScriptUri(),baseScriptUri: dojo.hostenv.getBaseScriptUri()};dojo.widget.fillFromTemplateCache = function(obj, templatePath, templateString, avoidCache){var tpath = templatePath || obj.templatePath;var tmplts = dojo.widget._templateCache;if(!tpath && !obj["widgetType"]) {do {var dummyName = "__dummyTemplate__" + dojo.widget._templateCache.dummyCount++;} while(tmplts[dummyName]);obj.widgetType = dummyName;}
var wt = tpath?tpath.toString():obj.widgetType;var ts = tmplts[wt];if(!ts){tmplts[wt] = {"string": null, "node": null};if(avoidCache){ts = {};}else{ts = tmplts[wt];}}
if((!obj.templateString)&&(!avoidCache)){obj.templateString = templateString || ts["string"];}
if((!obj.templateNode)&&(!avoidCache)){obj.templateNode = ts["node"];}
if((!obj.templateNode)&&(!obj.templateString)&&(tpath)){var tstring = dojo.hostenv.getText(tpath);if(tstring){tstring = tstring.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im, "");var matches = tstring.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);if(matches){tstring = matches[1];}}else{tstring = "";}
obj.templateString = tstring;if(!avoidCache){tmplts[wt]["string"] = tstring;}}
if((!ts["string"])&&(!avoidCache)){ts.string = obj.templateString;}}
dojo.widget._templateCache.dummyCount = 0;dojo.widget.attachProperties = ["dojoAttachPoint", "id"];dojo.widget.eventAttachProperty = "dojoAttachEvent";dojo.widget.onBuildProperty = "dojoOnBuild";dojo.widget.waiNames  = ["waiRole", "waiState"];dojo.widget.wai = {waiRole: {name: "waiRole","namespace": "http://www.w3.org/TR/xhtml2",alias: "x2",prefix: "wairole:"},waiState: {name: "waiState","namespace": "http://www.w3.org/2005/07/aaa",alias: "aaa",prefix: ""},setAttr: function(node,  ns,  attr, value){if(dojo.render.html.ie){node.setAttribute(this[ns].alias+":"+ attr, this[ns].prefix+value);}else{node.setAttributeNS(this[ns]["namespace"], attr, this[ns].prefix+value);}},getAttr: function( node,  ns,  attr){if(dojo.render.html.ie){return node.getAttribute(this[ns].alias+":"+attr);}else{return node.getAttributeNS(this[ns]["namespace"], attr);}},removeAttr: function( node,  ns,  attr){var success = true;if(dojo.render.html.ie){success = node.removeAttribute(this[ns].alias+":"+attr);}else{node.removeAttributeNS(this[ns]["namespace"], attr);}
return success;}};dojo.widget.attachTemplateNodes = function(rootNode,  targetObj, events ){var elementNodeType = dojo.dom.ELEMENT_NODE;function trim(str){return str.replace(/^\s+|\s+$/g, "");}
if(!rootNode){rootNode = targetObj.domNode;}
if(rootNode.nodeType != elementNodeType){return;}
var nodes = rootNode.all || rootNode.getElementsByTagName("*");var _this = targetObj;for(var x=-1; x<nodes.length; x++){var baseNode = (x == -1) ? rootNode : nodes[x];var attachPoint = [];if(!targetObj.widgetsInTemplate || !baseNode.getAttribute('dojoType')){for(var y=0; y<this.attachProperties.length; y++){var tmpAttachPoint = baseNode.getAttribute(this.attachProperties[y]);if(tmpAttachPoint){attachPoint = tmpAttachPoint.split(";");for(var z=0; z<attachPoint.length; z++){if(dojo.lang.isArray(targetObj[attachPoint[z]])){targetObj[attachPoint[z]].push(baseNode);}else{targetObj[attachPoint[z]]=baseNode;}}
break;}}
var attachEvent = baseNode.getAttribute(this.eventAttachProperty);if(attachEvent){var evts = attachEvent.split(";");for(var y=0; y<evts.length; y++){if((!evts[y])||(!evts[y].length)){ continue; }
var thisFunc = null;var tevt = trim(evts[y]);if(evts[y].indexOf(":") >= 0){var funcNameArr = tevt.split(":");tevt = trim(funcNameArr[0]);thisFunc = trim(funcNameArr[1]);}
if(!thisFunc){thisFunc = tevt;}
var tf = function(){var ntf = new String(thisFunc);return function(evt){if(_this[ntf]){_this[ntf](dojo.event.browser.fixEvent(evt, this));}};}();dojo.event.browser.addListener(baseNode, tevt, tf, false, true);}}
for(var y=0; y<events.length; y++){var evtVal = baseNode.getAttribute(events[y]);if((evtVal)&&(evtVal.length)){var thisFunc = null;var domEvt = events[y].substr(4);thisFunc = trim(evtVal);var funcs = [thisFunc];if(thisFunc.indexOf(";")>=0){funcs = dojo.lang.map(thisFunc.split(";"), trim);}
for(var z=0; z<funcs.length; z++){if(!funcs[z].length){ continue; }
var tf = function(){var ntf = new String(funcs[z]);return function(evt){if(_this[ntf]){_this[ntf](dojo.event.browser.fixEvent(evt, this));}}}();dojo.event.browser.addListener(baseNode, domEvt, tf, false, true);}}}}
var tmpltPoint = baseNode.getAttribute(this.templateProperty);if(tmpltPoint){targetObj[tmpltPoint]=baseNode;}
dojo.lang.forEach(dojo.widget.waiNames, function(name){var wai = dojo.widget.wai[name];var val = baseNode.getAttribute(wai.name);if(val){if(val.indexOf('-') == -1){dojo.widget.wai.setAttr(baseNode, wai.name, "role", val);}else{var statePair = val.split('-');dojo.widget.wai.setAttr(baseNode, wai.name, statePair[0], statePair[1]);}}}, this);var onBuild = baseNode.getAttribute(this.onBuildProperty);if(onBuild){eval("var node = baseNode; var widget = targetObj; "+onBuild);}}}
dojo.widget.getDojoEventsFromStr = function(str){var re = /(dojoOn([a-z]+)(\s?))=/gi;var evts = str ? str.match(re)||[] : [];var ret = [];var lem = {};for(var x=0; x<evts.length; x++){if(evts[x].length < 1){ continue; }
var cm = evts[x].replace(/\s/, "");cm = (cm.slice(0, cm.length-1));if(!lem[cm]){lem[cm] = true;ret.push(cm);}}
return ret;}
dojo.declare("dojo.widget.DomWidget",dojo.widget.Widget,function(){if((arguments.length>0)&&(typeof arguments[0] == "object")){this.create(arguments[0]);}},{templateNode: null,templateString: null,templateCssString: null,preventClobber: false,domNode: null,containerNode: null,widgetsInTemplate: false,addChild: function(	widget, overrideContainerNode, pos, ref, insertIndex){if(!this.isContainer){dojo.debug("dojo.widget.DomWidget.addChild() attempted on non-container widget");return null;}else{if(insertIndex == undefined){insertIndex = this.children.length;}
this.addWidgetAsDirectChild(widget, overrideContainerNode, pos, ref, insertIndex);this.registerChild(widget, insertIndex);}
return widget;},addWidgetAsDirectChild: function(	widget, overrideContainerNode, pos, ref, insertIndex){if((!this.containerNode)&&(!overrideContainerNode)){this.containerNode = this.domNode;}
var cn = (overrideContainerNode) ? overrideContainerNode : this.containerNode;if(!pos){ pos = "after"; }
if(!ref){if(!cn){ cn = dojo.body(); }
ref = cn.lastChild;}
if(!insertIndex) { insertIndex = 0; }
widget.domNode.setAttribute("dojoinsertionindex", insertIndex);if(!ref){cn.appendChild(widget.domNode);}else{if (pos == 'insertAtIndex'){dojo.dom.insertAtIndex(widget.domNode, ref.parentNode, insertIndex);}else{if((pos == "after")&&(ref === cn.lastChild)){cn.appendChild(widget.domNode);}else{dojo.dom.insertAtPosition(widget.domNode, cn, pos);}}}},registerChild: function(widget, insertionIndex){widget.dojoInsertionIndex = insertionIndex;var idx = -1;for(var i=0; i<this.children.length; i++){if (this.children[i].dojoInsertionIndex <= insertionIndex){idx = i;}}
this.children.splice(idx+1, 0, widget);widget.parent = this;widget.addedTo(this, idx+1);delete dojo.widget.manager.topWidgets[widget.widgetId];},removeChild: function( widget){dojo.dom.removeNode(widget.domNode);return dojo.widget.DomWidget.superclass.removeChild.call(this, widget);},getFragNodeRef: function(frag){if(!frag){return null;}
if(!frag[this.getNamespacedType()]){dojo.raise("Error: no frag for widget type " + this.getNamespacedType()
+ ", id " + this.widgetId
+ " (maybe a widget has set it's type incorrectly)");}
return frag[this.getNamespacedType()]["nodeRef"];},postInitialize: function( args,  frag,  parentComp){var sourceNodeRef = this.getFragNodeRef(frag);if (parentComp && (parentComp.snarfChildDomOutput || !sourceNodeRef)){parentComp.addWidgetAsDirectChild(this, "", "insertAtIndex", "",  args["dojoinsertionindex"], sourceNodeRef);} else if (sourceNodeRef){if(this.domNode && (this.domNode !== sourceNodeRef)){dojo.dom.replaceNode(sourceNodeRef, this.domNode);}}
if ( parentComp ) {parentComp.registerChild(this, args.dojoinsertionindex);} else {dojo.widget.manager.topWidgets[this.widgetId]=this;}
if(this.widgetsInTemplate){var parser = new dojo.xml.Parse();var subContainerNode;var subnodes = this.domNode.getElementsByTagName("*");for(var i=0;i<subnodes.length;i++){if(subnodes[i].getAttribute('dojoAttachPoint') == 'subContainerWidget'){subContainerNode = subnodes[i];}
if(subnodes[i].getAttribute('dojoType')){subnodes[i].setAttribute('_isSubWidget', true);}}
if (this.isContainer && !this.containerNode){if(subContainerNode){var src = this.getFragNodeRef(frag);if (src){dojo.dom.moveChildren(src, subContainerNode);frag['dojoDontFollow'] = true;}}else{dojo.debug("No subContainerWidget node can be found in template file for widget "+this);}}
var templatefrag = parser.parseElement(this.domNode, null, true);dojo.widget.getParser().createSubComponents(templatefrag, this);var subwidgets = [];var stack = [this];var w;while((w = stack.pop())){for(var i = 0; i < w.children.length; i++){var cwidget = w.children[i];if(cwidget._processedSubWidgets || !cwidget.extraArgs['_issubwidget']){ continue; }
subwidgets.push(cwidget);if(cwidget.isContainer){stack.push(cwidget);}}}
for(var i = 0; i < subwidgets.length; i++){var widget = subwidgets[i];if(widget._processedSubWidgets){dojo.debug("This should not happen: widget._processedSubWidgets is already true!");return;}
widget._processedSubWidgets = true;if(widget.extraArgs['dojoattachevent']){var evts = widget.extraArgs['dojoattachevent'].split(";");for(var j=0; j<evts.length; j++){var thisFunc = null;var tevt = dojo.string.trim(evts[j]);if(tevt.indexOf(":") >= 0){var funcNameArr = tevt.split(":");tevt = dojo.string.trim(funcNameArr[0]);thisFunc = dojo.string.trim(funcNameArr[1]);}
if(!thisFunc){thisFunc = tevt;}
if(dojo.lang.isFunction(widget[tevt])){dojo.event.kwConnect({srcObj: widget,srcFunc: tevt,targetObj: this,targetFunc: thisFunc});}else{alert(tevt+" is not a function in widget "+widget);}}}
if(widget.extraArgs['dojoattachpoint']){this[widget.extraArgs['dojoattachpoint']] = widget;}}}
if(this.isContainer && !frag["dojoDontFollow"]){dojo.widget.getParser().createSubComponents(frag, this);}},buildRendering: function( args,  frag){var ts = dojo.widget._templateCache[this.widgetType];if(args["templatecsspath"]){args["templateCssPath"] = args["templatecsspath"];}
var cpath = args["templateCssPath"] || this.templateCssPath;if(cpath && !dojo.widget._cssFiles[cpath.toString()]){if((!this.templateCssString)&&(cpath)){this.templateCssString = dojo.hostenv.getText(cpath);this.templateCssPath = null;}
dojo.widget._cssFiles[cpath.toString()] = true;}
if((this["templateCssString"])&&(!this.templateCssString["loaded"])){dojo.html.insertCssText(this.templateCssString, null, cpath);if(!this.templateCssString){ this.templateCssString = ""; }
this.templateCssString.loaded = true;}
if(
(!this.preventClobber)&&(
(this.templatePath)||
(this.templateNode)||
(
(this["templateString"])&&(this.templateString.length)
)||
(
(typeof ts != "undefined")&&( (ts["string"])||(ts["node"]) )
)
)
){this.buildFromTemplate(args, frag);}else{this.domNode = this.getFragNodeRef(frag);}
this.fillInTemplate(args, frag);},buildFromTemplate: function( args,  frag){var avoidCache = false;if(args["templatepath"]){args["templatePath"] = args["templatepath"];}
dojo.widget.fillFromTemplateCache(	this,args["templatePath"],null,avoidCache);var ts = dojo.widget._templateCache[this.templatePath?this.templatePath.toString():this.widgetType];if((ts)&&(!avoidCache)){if(!this.templateString.length){this.templateString = ts["string"];}
if(!this.templateNode){this.templateNode = ts["node"];}}
var matches = false;var node = null;var tstr = this.templateString;if((!this.templateNode)&&(this.templateString)){matches = this.templateString.match(/\$\{([^\}]+)\}/g);if(matches) {var hash = this.strings || {};for(var key in dojo.widget.defaultStrings) {if(dojo.lang.isUndefined(hash[key])) {hash[key] = dojo.widget.defaultStrings[key];}}
for(var i = 0; i < matches.length; i++) {var key = matches[i];key = key.substring(2, key.length-1);var kval = (key.substring(0, 5) == "this.") ? dojo.lang.getObjPathValue(key.substring(5), this) : hash[key];var value;if((kval)||(dojo.lang.isString(kval))){value = new String((dojo.lang.isFunction(kval)) ? kval.call(this, key, this.templateString) : kval);while (value.indexOf("\"") > -1) {value=value.replace("\"","&quot;");}
tstr = tstr.replace(matches[i], value);}}}else{this.templateNode = this.createNodesFromText(this.templateString, true)[0];if(!avoidCache){ts.node = this.templateNode;}}}
if((!this.templateNode)&&(!matches)){dojo.debug("DomWidget.buildFromTemplate: could not create template");return false;}else if(!matches){node = this.templateNode.cloneNode(true);if(!node){ return false; }}else{node = this.createNodesFromText(tstr, true)[0];}
this.domNode = node;this.attachTemplateNodes();if (this.isContainer && this.containerNode){var src = this.getFragNodeRef(frag);if (src){dojo.dom.moveChildren(src, this.containerNode);}}},attachTemplateNodes: function(baseNode, targetObj){if(!baseNode){ baseNode = this.domNode; }
if(!targetObj){ targetObj = this; }
return dojo.widget.attachTemplateNodes(baseNode, targetObj,dojo.widget.getDojoEventsFromStr(this.templateString));},fillInTemplate: function(){},destroyRendering: function(){try{delete this.domNode;}catch(e){  }},createNodesFromText: function(){dojo.unimplemented("dojo.widget.DomWidget.createNodesFromText");}}
);dojo.provide("dojo.html.display");dojo.html._toggle = function(node, tester, setter){node = dojo.byId(node);setter(node, !tester(node));return tester(node);}
dojo.html.show = function(node){node = dojo.byId(node);if(dojo.html.getStyleProperty(node, 'display')=='none'){dojo.html.setStyle(node, 'display', (node.dojoDisplayCache||''));node.dojoDisplayCache = undefined;}}
dojo.html.hide = function(node){node = dojo.byId(node);if(typeof node["dojoDisplayCache"] == "undefined"){var d = dojo.html.getStyleProperty(node, 'display')
if(d!='none'){node.dojoDisplayCache = d;}}
dojo.html.setStyle(node, 'display', 'none');}
dojo.html.setShowing = function(node, showing){dojo.html[(showing ? 'show' : 'hide')](node);}
dojo.html.isShowing = function(node){return (dojo.html.getStyleProperty(node, 'display') != 'none');}
dojo.html.toggleShowing = function(node){return dojo.html._toggle(node, dojo.html.isShowing, dojo.html.setShowing);}
dojo.html.displayMap = { tr: '', td: '', th: '', img: 'inline', span: 'inline', input: 'inline', button: 'inline' };dojo.html.suggestDisplayByTagName = function(node){node = dojo.byId(node);if(node && node.tagName){var tag = node.tagName.toLowerCase();return (tag in dojo.html.displayMap ? dojo.html.displayMap[tag] : 'block');}}
dojo.html.setDisplay = function(node, display){dojo.html.setStyle(node, 'display', ((display instanceof String || typeof display == "string") ? display : (display ? dojo.html.suggestDisplayByTagName(node) : 'none')));}
dojo.html.isDisplayed = function(node){return (dojo.html.getComputedStyle(node, 'display') != 'none');}
dojo.html.toggleDisplay = function(node){return dojo.html._toggle(node, dojo.html.isDisplayed, dojo.html.setDisplay);}
dojo.html.setVisibility = function(node, visibility){dojo.html.setStyle(node, 'visibility', ((visibility instanceof String || typeof visibility == "string") ? visibility : (visibility ? 'visible' : 'hidden')));}
dojo.html.isVisible = function(node){return (dojo.html.getComputedStyle(node, 'visibility') != 'hidden');}
dojo.html.toggleVisibility = function(node){return dojo.html._toggle(node, dojo.html.isVisible, dojo.html.setVisibility);}
dojo.html.setOpacity = function(node, opacity, dontFixOpacity){node = dojo.byId(node);var h = dojo.render.html;if(!dontFixOpacity){if( opacity >= 1.0){if(h.ie){dojo.html.clearOpacity(node);return;}else{opacity = 0.999999;}}else if( opacity < 0.0){ opacity = 0; }}
if(h.ie){if(node.nodeName.toLowerCase() == "tr"){var tds = node.getElementsByTagName("td");for(var x=0; x<tds.length; x++){tds[x].style.filter = "Alpha(Opacity="+opacity*100+")";}}
node.style.filter = "Alpha(Opacity="+opacity*100+")";}else if(h.moz){node.style.opacity = opacity;node.style.MozOpacity = opacity;}else if(h.safari){node.style.opacity = opacity;node.style.KhtmlOpacity = opacity;}else{node.style.opacity = opacity;}}
dojo.html.clearOpacity = function(node){node = dojo.byId(node);var ns = node.style;var h = dojo.render.html;if(h.ie){try {if( node.filters && node.filters.alpha ){ns.filter = "";}} catch(e) {}}else if(h.moz){ns.opacity = 1;ns.MozOpacity = 1;}else if(h.safari){ns.opacity = 1;ns.KhtmlOpacity = 1;}else{ns.opacity = 1;}}
dojo.html.getOpacity = function(node){node = dojo.byId(node);var h = dojo.render.html;if(h.ie){var opac = (node.filters && node.filters.alpha &&
typeof node.filters.alpha.opacity == "number"
? node.filters.alpha.opacity : 100) / 100;}else{var opac = node.style.opacity || node.style.MozOpacity ||
node.style.KhtmlOpacity || 1;}
return opac >= 0.999999 ? 1.0 : Number(opac);}
dojo.provide("dojo.html.layout");dojo.html.sumAncestorProperties = function(node, prop){node = dojo.byId(node);if(!node){ return 0; }
var retVal = 0;while(node){if(dojo.html.getComputedStyle(node, 'position') == 'fixed'){return 0;}
var val = node[prop];if(val){retVal += val - 0;if(node==dojo.body()){ break; }}
node = node.parentNode;}
return retVal;}
dojo.html.setStyleAttributes = function(node, attributes) {node = dojo.byId(node);var splittedAttribs=attributes.replace(/(;)?\s*$/, "").split(";");for(var i=0; i<splittedAttribs.length; i++){var nameValue=splittedAttribs[i].split(":");var name=nameValue[0].replace(/\s*$/, "").replace(/^\s*/, "").toLowerCase();var value=nameValue[1].replace(/\s*$/, "").replace(/^\s*/, "");switch(name){case "opacity":
dojo.html.setOpacity(node, value);break;case "content-height":
dojo.html.setContentBox(node, {height: value});break;case "content-width":
dojo.html.setContentBox(node, {width: value});break;case "outer-height":
dojo.html.setMarginBox(node, {height: value});break;case "outer-width":
dojo.html.setMarginBox(node, {width: value});break;default:
node.style[dojo.html.toCamelCase(name)]=value;}}}
dojo.html.boxSizing = {MARGIN_BOX: "margin-box",BORDER_BOX: "border-box",PADDING_BOX: "padding-box",CONTENT_BOX: "content-box"};dojo.html.getAbsolutePosition = dojo.html.abs = function(node, includeScroll, boxType){node = dojo.byId(node);var ownerDocument = dojo.doc();var ret = {x: 0,y: 0};var bs = dojo.html.boxSizing;if(!boxType) { boxType = bs.CONTENT_BOX; }
var nativeBoxType = 2;var targetBoxType;switch(boxType){case bs.MARGIN_BOX:
targetBoxType = 3;break;case bs.BORDER_BOX:
targetBoxType = 2;break;case bs.PADDING_BOX:
default:
targetBoxType = 1;break;case bs.CONTENT_BOX:
targetBoxType = 0;break;}
var h = dojo.render.html;var db = ownerDocument["body"]||ownerDocument["documentElement"];if(h.ie){with(node.getBoundingClientRect()){ret.x = left-2;ret.y = top-2;}}else if(ownerDocument['getBoxObjectFor']){nativeBoxType = 1;try{var bo = ownerDocument.getBoxObjectFor(node);ret.x = bo.x - dojo.html.sumAncestorProperties(node, "scrollLeft");ret.y = bo.y - dojo.html.sumAncestorProperties(node, "scrollTop");}catch(e){}}else{if(node["offsetParent"]){var endNode;if(	(h.safari)&&
(node.style.getPropertyValue("position") == "absolute")&&
(node.parentNode == db)){endNode = db;}else{endNode = db.parentNode;}
if(node.parentNode != db){var nd = node;if(dojo.render.html.opera){ nd = db; }
ret.x -= dojo.html.sumAncestorProperties(nd, "scrollLeft");ret.y -= dojo.html.sumAncestorProperties(nd, "scrollTop");}
var curnode = node;do{var n = curnode["offsetLeft"];if(!h.opera || n>0){ret.x += isNaN(n) ? 0 : n;}
var m = curnode["offsetTop"];ret.y += isNaN(m) ? 0 : m;curnode = curnode.offsetParent;}while((curnode != endNode)&&(curnode != null));}else if(node["x"]&&node["y"]){ret.x += isNaN(node.x) ? 0 : node.x;ret.y += isNaN(node.y) ? 0 : node.y;}}
if(includeScroll){var scroll = dojo.html.getScroll();ret.y += scroll.top;ret.x += scroll.left;}
var extentFuncArray=[dojo.html.getPaddingExtent, dojo.html.getBorderExtent, dojo.html.getMarginExtent];if(nativeBoxType > targetBoxType){for(var i=targetBoxType;i<nativeBoxType;++i){ret.y += extentFuncArray[i](node, 'top');ret.x += extentFuncArray[i](node, 'left');}}else if(nativeBoxType < targetBoxType){for(var i=targetBoxType;i>nativeBoxType;--i){ret.y -= extentFuncArray[i-1](node, 'top');ret.x -= extentFuncArray[i-1](node, 'left');}}
ret.top = ret.y;ret.left = ret.x;return ret;}
dojo.html.isPositionAbsolute = function(node){return (dojo.html.getComputedStyle(node, 'position') == 'absolute');}
dojo.html._sumPixelValues = function(node, selectors, autoIsZero){var total = 0;for(var x=0; x<selectors.length; x++){total += dojo.html.getPixelValue(node, selectors[x], autoIsZero);}
return total;}
dojo.html.getMargin = function(node){return {width: dojo.html._sumPixelValues(node, ["margin-left", "margin-right"], (dojo.html.getComputedStyle(node, 'position') == 'absolute')),height: dojo.html._sumPixelValues(node, ["margin-top", "margin-bottom"], (dojo.html.getComputedStyle(node, 'position') == 'absolute'))};}
dojo.html.getBorder = function(node){return {width: dojo.html.getBorderExtent(node, 'left') + dojo.html.getBorderExtent(node, 'right'),height: dojo.html.getBorderExtent(node, 'top') + dojo.html.getBorderExtent(node, 'bottom')};}
dojo.html.getBorderExtent = function(node, side){return (dojo.html.getStyle(node, 'border-' + side + '-style') == 'none' ? 0 : dojo.html.getPixelValue(node, 'border-' + side + '-width'));}
dojo.html.getMarginExtent = function(node, side){return dojo.html._sumPixelValues(node, ["margin-" + side], dojo.html.isPositionAbsolute(node));}
dojo.html.getPaddingExtent = function(node, side){return dojo.html._sumPixelValues(node, ["padding-" + side], true);}
dojo.html.getPadding = function(node){return {width: dojo.html._sumPixelValues(node, ["padding-left", "padding-right"], true),height: dojo.html._sumPixelValues(node, ["padding-top", "padding-bottom"], true)};}
dojo.html.getPadBorder = function(node){var pad = dojo.html.getPadding(node);var border = dojo.html.getBorder(node);return { width: pad.width + border.width, height: pad.height + border.height };}
dojo.html.getBoxSizing = function(node){var h = dojo.render.html;var bs = dojo.html.boxSizing;if((h.ie)||(h.opera)){var cm = document["compatMode"];if((cm == "BackCompat")||(cm == "QuirksMode")){return bs.BORDER_BOX;}else{return bs.CONTENT_BOX;}}else{if(arguments.length == 0){ node = document.documentElement; }
var sizing = dojo.html.getStyle(node, "-moz-box-sizing");if(!sizing){ sizing = dojo.html.getStyle(node, "box-sizing"); }
return (sizing ? sizing : bs.CONTENT_BOX);}}
dojo.html.isBorderBox = function(node){return (dojo.html.getBoxSizing(node) == dojo.html.boxSizing.BORDER_BOX);}
dojo.html.getBorderBox = function(node){node = dojo.byId(node);return { width: node.offsetWidth, height: node.offsetHeight };}
dojo.html.getPaddingBox = function(node){var box = dojo.html.getBorderBox(node);var border = dojo.html.getBorder(node);return {width: box.width - border.width,height:box.height - border.height};}
dojo.html.getContentBox = function(node){node = dojo.byId(node);var padborder = dojo.html.getPadBorder(node);return {width: node.offsetWidth - padborder.width,height: node.offsetHeight - padborder.height};}
dojo.html.setContentBox = function(node, args){node = dojo.byId(node);var width = 0; var height = 0;var isbb = dojo.html.isBorderBox(node);var padborder = (isbb ? dojo.html.getPadBorder(node) : { width: 0, height: 0});var ret = {};if(typeof args.width != "undefined"){width = args.width + padborder.width;ret.width = dojo.html.setPositivePixelValue(node, "width", width);}
if(typeof args.height != "undefined"){height = args.height + padborder.height;ret.height = dojo.html.setPositivePixelValue(node, "height", height);}
return ret;}
dojo.html.getMarginBox = function(node){var borderbox = dojo.html.getBorderBox(node);var margin = dojo.html.getMargin(node);return { width: borderbox.width + margin.width, height: borderbox.height + margin.height };}
dojo.html.setMarginBox = function(node, args){node = dojo.byId(node);var width = 0; var height = 0;var isbb = dojo.html.isBorderBox(node);var padborder = (!isbb ? dojo.html.getPadBorder(node) : { width: 0, height: 0 });var margin = dojo.html.getMargin(node);var ret = {};if(typeof args.width != "undefined"){width = args.width - padborder.width;width -= margin.width;ret.width = dojo.html.setPositivePixelValue(node, "width", width);}
if(typeof args.height != "undefined"){height = args.height - padborder.height;height -= margin.height;ret.height = dojo.html.setPositivePixelValue(node, "height", height);}
return ret;}
dojo.html.getElementBox = function(node, type){var bs = dojo.html.boxSizing;switch(type){case bs.MARGIN_BOX:
return dojo.html.getMarginBox(node);case bs.BORDER_BOX:
return dojo.html.getBorderBox(node);case bs.PADDING_BOX:
return dojo.html.getPaddingBox(node);case bs.CONTENT_BOX:
default:
return dojo.html.getContentBox(node);}}
dojo.html.toCoordinateObject = dojo.html.toCoordinateArray = function(coords, includeScroll, boxtype) {if(!coords.nodeType && !(coords instanceof String || typeof coords == "string") &&
('width' in coords || 'height' in coords || 'left' in coords ||
'x' in coords || 'top' in coords || 'y' in coords)){var ret = {left: coords.left||coords.x||0,top: coords.top||coords.y||0,width: coords.width||0,height: coords.height||0};}else{var node = dojo.byId(coords);var pos = dojo.html.abs(node, includeScroll, boxtype);var marginbox = dojo.html.getMarginBox(node);var ret = {left: pos.left,top: pos.top,width: marginbox.width,height: marginbox.height};}
ret.x = ret.left;ret.y = ret.top;return ret;}
dojo.html.setMarginBoxWidth = dojo.html.setOuterWidth = function(node, width){return dojo.html._callDeprecated("setMarginBoxWidth", "setMarginBox", arguments, "width");}
dojo.html.setMarginBoxHeight = dojo.html.setOuterHeight = function(){return dojo.html._callDeprecated("setMarginBoxHeight", "setMarginBox", arguments, "height");}
dojo.html.getMarginBoxWidth = dojo.html.getOuterWidth = function(){return dojo.html._callDeprecated("getMarginBoxWidth", "getMarginBox", arguments, null, "width");}
dojo.html.getMarginBoxHeight = dojo.html.getOuterHeight = function(){return dojo.html._callDeprecated("getMarginBoxHeight", "getMarginBox", arguments, null, "height");}
dojo.html.getTotalOffset = function(node, type, includeScroll){return dojo.html._callDeprecated("getTotalOffset", "getAbsolutePosition", arguments, null, type);}
dojo.html.getAbsoluteX = function(node, includeScroll){return dojo.html._callDeprecated("getAbsoluteX", "getAbsolutePosition", arguments, null, "x");}
dojo.html.getAbsoluteY = function(node, includeScroll){return dojo.html._callDeprecated("getAbsoluteY", "getAbsolutePosition", arguments, null, "y");}
dojo.html.totalOffsetLeft = function(node, includeScroll){return dojo.html._callDeprecated("totalOffsetLeft", "getAbsolutePosition", arguments, null, "left");}
dojo.html.totalOffsetTop = function(node, includeScroll){return dojo.html._callDeprecated("totalOffsetTop", "getAbsolutePosition", arguments, null, "top");}
dojo.html.getMarginWidth = function(node){return dojo.html._callDeprecated("getMarginWidth", "getMargin", arguments, null, "width");}
dojo.html.getMarginHeight = function(node){return dojo.html._callDeprecated("getMarginHeight", "getMargin", arguments, null, "height");}
dojo.html.getBorderWidth = function(node){return dojo.html._callDeprecated("getBorderWidth", "getBorder", arguments, null, "width");}
dojo.html.getBorderHeight = function(node){return dojo.html._callDeprecated("getBorderHeight", "getBorder", arguments, null, "height");}
dojo.html.getPaddingWidth = function(node){return dojo.html._callDeprecated("getPaddingWidth", "getPadding", arguments, null, "width");}
dojo.html.getPaddingHeight = function(node){return dojo.html._callDeprecated("getPaddingHeight", "getPadding", arguments, null, "height");}
dojo.html.getPadBorderWidth = function(node){return dojo.html._callDeprecated("getPadBorderWidth", "getPadBorder", arguments, null, "width");}
dojo.html.getPadBorderHeight = function(node){return dojo.html._callDeprecated("getPadBorderHeight", "getPadBorder", arguments, null, "height");}
dojo.html.getBorderBoxWidth = dojo.html.getInnerWidth = function(){return dojo.html._callDeprecated("getBorderBoxWidth", "getBorderBox", arguments, null, "width");}
dojo.html.getBorderBoxHeight = dojo.html.getInnerHeight = function(){return dojo.html._callDeprecated("getBorderBoxHeight", "getBorderBox", arguments, null, "height");}
dojo.html.getContentBoxWidth = dojo.html.getContentWidth = function(){return dojo.html._callDeprecated("getContentBoxWidth", "getContentBox", arguments, null, "width");}
dojo.html.getContentBoxHeight = dojo.html.getContentHeight = function(){return dojo.html._callDeprecated("getContentBoxHeight", "getContentBox", arguments, null, "height");}
dojo.html.setContentBoxWidth = dojo.html.setContentWidth = function(node, width){return dojo.html._callDeprecated("setContentBoxWidth", "setContentBox", arguments, "width");}
dojo.html.setContentBoxHeight = dojo.html.setContentHeight = function(node, height){return dojo.html._callDeprecated("setContentBoxHeight", "setContentBox", arguments, "height");}
dojo.provide("dojo.html.util");dojo.html.getElementWindow = function(element){return dojo.html.getDocumentWindow( element.ownerDocument );}
dojo.html.getDocumentWindow = function(doc){if(dojo.render.html.safari && !doc._parentWindow){var fix=function(win){win.document._parentWindow=win;for(var i=0; i<win.frames.length; i++){fix(win.frames[i]);}}
fix(window.top);}
if(dojo.render.html.ie && window !== document.parentWindow && !doc._parentWindow){doc.parentWindow.execScript("document._parentWindow = window;", "Javascript");var win = doc._parentWindow;doc._parentWindow = null;return win;}
return doc._parentWindow || doc.parentWindow || doc.defaultView;}
dojo.html.getAbsolutePositionExt = function(node, includeScroll, boxType, topwin){var curwin = dojo.html.getElementWindow(node);var ret = dojo.withGlobal(curwin, 'getAbsolutePosition', dojo.html, arguments);var win = dojo.html.getElementWindow(node);if(topwin != win && win.frameElement){var ext = dojo.html.getAbsolutePositionExt(win.frameElement,includeScroll,boxType,topwin);ret.x += ext.x;ret.y += ext.y;}
ret.top = ret.y;ret.left = ret.x;return ret;}
dojo.html.gravity = function(node, e){node = dojo.byId(node);var mouse = dojo.html.getCursorPosition(e);with (dojo.html) {var absolute = getAbsolutePosition(node, true);var bb = getBorderBox(node);var nodecenterx = absolute.x + (bb.width / 2);var nodecentery = absolute.y + (bb.height / 2);}
with (dojo.html.gravity) {return ((mouse.x < nodecenterx ? WEST : EAST) | (mouse.y < nodecentery ? NORTH : SOUTH));}}
dojo.html.gravity.NORTH = 1;dojo.html.gravity.SOUTH = 1 << 1;dojo.html.gravity.EAST = 1 << 2;dojo.html.gravity.WEST = 1 << 3;dojo.html.overElement = function(element, e){element = dojo.byId(element);var mouse = dojo.html.getCursorPosition(e);var bb = dojo.html.getBorderBox(element);var absolute = dojo.html.getAbsolutePosition(element, true, dojo.html.boxSizing.BORDER_BOX);var top = absolute.y;var bottom = top + bb.height;var left = absolute.x;var right = left + bb.width;return (mouse.x >= left
&& mouse.x <= right
&& mouse.y >= top
&& mouse.y <= bottom
);}
dojo.html.renderedTextContent = function(node){node = dojo.byId(node);var result = "";if (node == null) { return result; }
for (var i = 0; i < node.childNodes.length; i++) {switch (node.childNodes[i].nodeType) {case 1:
case 5:
var display = "unknown";try {display = dojo.html.getStyle(node.childNodes[i], "display");} catch(E) {}
switch (display) {case "block": case "list-item": case "run-in":
case "table": case "table-row-group": case "table-header-group":
case "table-footer-group": case "table-row": case "table-column-group":
case "table-column": case "table-cell": case "table-caption":
result += "\n";result += dojo.html.renderedTextContent(node.childNodes[i]);result += "\n";break;case "none": break;default:
if(node.childNodes[i].tagName && node.childNodes[i].tagName.toLowerCase() == "br") {result += "\n";} else {result += dojo.html.renderedTextContent(node.childNodes[i]);}
break;}
break;case 3:
case 2:
case 4:
var text = node.childNodes[i].nodeValue;var textTransform = "unknown";try {textTransform = dojo.html.getStyle(node, "text-transform");} catch(E) {}
switch (textTransform){case "capitalize":
var words = text.split(' ');for(var i=0; i<words.length; i++){words[i] = words[i].charAt(0).toUpperCase() + words[i].substring(1);}
text = words.join(" ");break;case "uppercase": text = text.toUpperCase(); break;case "lowercase": text = text.toLowerCase(); break;default: break;}
switch (textTransform){case "nowrap": break;case "pre-wrap": break;case "pre-line": break;case "pre": break;default:
text = text.replace(/\s+/, " ");if (/\s$/.test(result)) { text.replace(/^\s/, ""); }
break;}
result += text;break;default:
break;}}
return result;}
dojo.html.createNodesFromText = function(txt, trim){if(trim) { txt = txt.replace(/^\s+|\s+$/g, ""); }
var tn = dojo.doc().createElement("div");tn.style.visibility= "hidden";dojo.body().appendChild(tn);var tableType = "none";if((/^<t[dh][\s\r\n>]/i).test(txt.replace(/^\s+/))) {txt = "<table><tbody><tr>" + txt + "</tr></tbody></table>";tableType = "cell";} else if((/^<tr[\s\r\n>]/i).test(txt.replace(/^\s+/))) {txt = "<table><tbody>" + txt + "</tbody></table>";tableType = "row";} else if((/^<(thead|tbody|tfoot)[\s\r\n>]/i).test(txt.replace(/^\s+/))) {txt = "<table>" + txt + "</table>";tableType = "section";}
tn.innerHTML = txt;if(tn["normalize"]){tn.normalize();}
var parent = null;switch(tableType) {case "cell":
parent = tn.getElementsByTagName("tr")[0];break;case "row":
parent = tn.getElementsByTagName("tbody")[0];break;case "section":
parent = tn.getElementsByTagName("table")[0];break;default:
parent = tn;break;}
var nodes = [];for(var x=0; x<parent.childNodes.length; x++){nodes.push(parent.childNodes[x].cloneNode(true));}
tn.style.display = "none";dojo.dom.removeNode(tn);return nodes;}
dojo.html.placeOnScreen = function(
node,desiredX,desiredY,padding,hasScroll,corners,tryOnly
){if(desiredX instanceof Array || typeof desiredX == "array") {tryOnly = corners;corners = hasScroll;hasScroll = padding;padding = desiredY;desiredY = desiredX[1];desiredX = desiredX[0];}
if(corners instanceof String || typeof corners == "string"){corners = corners.split(",");}
if(!isNaN(padding)) {padding = [Number(padding), Number(padding)];} else if(!(padding instanceof Array || typeof padding == "array")) {padding = [0, 0];}
var scroll = dojo.html.getScroll().offset;var view = dojo.html.getViewport();node = dojo.byId(node);var oldDisplay = node.style.display;node.style.display="";var bb = dojo.html.getBorderBox(node);var w = bb.width;var h = bb.height;node.style.display=oldDisplay;if(!(corners instanceof Array || typeof corners == "array")){corners = ['TL'];}
var bestx, besty, bestDistance = Infinity, bestCorner;for(var cidex=0; cidex<corners.length; ++cidex){var corner = corners[cidex];var match = true;var tryX = desiredX - (corner.charAt(1)=='L' ? 0 : w) + padding[0]*(corner.charAt(1)=='L' ? 1 : -1);var tryY = desiredY - (corner.charAt(0)=='T' ? 0 : h) + padding[1]*(corner.charAt(0)=='T' ? 1 : -1);if(hasScroll) {tryX -= scroll.x;tryY -= scroll.y;}
if(tryX < 0){tryX = 0;match = false;}
if(tryY < 0){tryY = 0;match = false;}
var x = tryX + w;if(x > view.width) {x = view.width - w;match = false;} else {x = tryX;}
x = Math.max(padding[0], x) + scroll.x;var y = tryY + h;if(y > view.height) {y = view.height - h;match = false;} else {y = tryY;}
y = Math.max(padding[1], y) + scroll.y;if(match){bestx = x;besty = y;bestDistance = 0;bestCorner = corner;break;}else{var dist = Math.pow(x-tryX-scroll.x,2)+Math.pow(y-tryY-scroll.y,2);if(bestDistance > dist){bestDistance = dist;bestx = x;besty = y;bestCorner = corner;}}}
if(!tryOnly){node.style.left = bestx + "px";node.style.top = besty + "px";}
return { left: bestx, top: besty, x: bestx, y: besty, dist: bestDistance, corner:  bestCorner};}
dojo.html.placeOnScreenAroundElement = function(
node,aroundNode,padding,aroundType,aroundCorners,tryOnly
){var best, bestDistance=Infinity;aroundNode = dojo.byId(aroundNode);var oldDisplay = aroundNode.style.display;aroundNode.style.display="";var mb = dojo.html.getElementBox(aroundNode, aroundType);var aroundNodeW = mb.width;var aroundNodeH = mb.height;var aroundNodePos = dojo.html.getAbsolutePosition(aroundNode, true, aroundType);aroundNode.style.display=oldDisplay;for(var nodeCorner in aroundCorners){var pos, desiredX, desiredY;var corners = aroundCorners[nodeCorner];desiredX = aroundNodePos.x + (nodeCorner.charAt(1)=='L' ? 0 : aroundNodeW);desiredY = aroundNodePos.y + (nodeCorner.charAt(0)=='T' ? 0 : aroundNodeH);pos = dojo.html.placeOnScreen(node, desiredX, desiredY, padding, true, corners, true);if(pos.dist == 0){best = pos;break;}else{if(bestDistance > pos.dist){bestDistance = pos.dist;best = pos;}}}
if(!tryOnly){node.style.left = best.left + "px";node.style.top = best.top + "px";}
return best;}
dojo.html.scrollIntoView = function(node){if(!node){ return; }
if(dojo.render.html.ie){if(dojo.html.getBorderBox(node.parentNode).height <= node.parentNode.scrollHeight){node.scrollIntoView(false);}}else if(dojo.render.html.mozilla){node.scrollIntoView(false);}else{var parent = node.parentNode;var parentBottom = parent.scrollTop + dojo.html.getBorderBox(parent).height;var nodeBottom = node.offsetTop + dojo.html.getMarginBox(node).height;if(parentBottom < nodeBottom){parent.scrollTop += (nodeBottom - parentBottom);}else if(parent.scrollTop > node.offsetTop){parent.scrollTop -= (parent.scrollTop - node.offsetTop);}}}
dojo.provide("dojo.gfx.color");dojo.gfx.color.Color = function(r, g, b, a) {if(dojo.lang.isArray(r)){this.r = r[0];this.g = r[1];this.b = r[2];this.a = r[3]||1.0;}else if(dojo.lang.isString(r)){var rgb = dojo.gfx.color.extractRGB(r);this.r = rgb[0];this.g = rgb[1];this.b = rgb[2];this.a = g||1.0;}else if(r instanceof dojo.gfx.color.Color){this.r = r.r;this.b = r.b;this.g = r.g;this.a = r.a;}else{this.r = r;this.g = g;this.b = b;this.a = a;}}
dojo.gfx.color.Color.fromArray = function(arr) {return new dojo.gfx.color.Color(arr[0], arr[1], arr[2], arr[3]);}
dojo.extend(dojo.gfx.color.Color, {toRgb: function(includeAlpha) {if(includeAlpha) {return this.toRgba();} else {return [this.r, this.g, this.b];}},toRgba: function() {return [this.r, this.g, this.b, this.a];},toHex: function() {return dojo.gfx.color.rgb2hex(this.toRgb());},toCss: function() {return "rgb(" + this.toRgb().join() + ")";},toString: function() {return this.toHex();},blend: function(color, weight){var rgb = null;if(dojo.lang.isArray(color)){rgb = color;}else if(color instanceof dojo.gfx.color.Color){rgb = color.toRgb();}else{rgb = new dojo.gfx.color.Color(color).toRgb();}
return dojo.gfx.color.blend(this.toRgb(), rgb, weight);}});dojo.gfx.color.named = {white:      [255,255,255],black:      [0,0,0],red:        [255,0,0],green:	    [0,255,0],lime:	    [0,255,0],blue:       [0,0,255],navy:       [0,0,128],gray:       [128,128,128],silver:     [192,192,192]};dojo.gfx.color.blend = function(a, b, weight){if(typeof a == "string"){return dojo.gfx.color.blendHex(a, b, weight);}
if(!weight){weight = 0;}
weight = Math.min(Math.max(-1, weight), 1);weight = ((weight + 1)/2);var c = [];for(var x = 0; x < 3; x++){c[x] = parseInt( b[x] + ( (a[x] - b[x]) * weight) );}
return c;}
dojo.gfx.color.blendHex = function(a, b, weight) {return dojo.gfx.color.rgb2hex(dojo.gfx.color.blend(dojo.gfx.color.hex2rgb(a), dojo.gfx.color.hex2rgb(b), weight));}
dojo.gfx.color.extractRGB = function(color) {var hex = "0123456789abcdef";color = color.toLowerCase();if( color.indexOf("rgb") == 0 ) {var matches = color.match(/rgba*\((\d+), *(\d+), *(\d+)/i);var ret = matches.splice(1, 3);return ret;} else {var colors = dojo.gfx.color.hex2rgb(color);if(colors) {return colors;} else {return dojo.gfx.color.named[color] || [255, 255, 255];}}}
dojo.gfx.color.hex2rgb = function(hex) {var hexNum = "0123456789ABCDEF";var rgb = new Array(3);if( hex.indexOf("#") == 0 ) { hex = hex.substring(1); }
hex = hex.toUpperCase();if(hex.replace(new RegExp("["+hexNum+"]", "g"), "") != "") {return null;}
if( hex.length == 3 ) {rgb[0] = hex.charAt(0) + hex.charAt(0)
rgb[1] = hex.charAt(1) + hex.charAt(1)
rgb[2] = hex.charAt(2) + hex.charAt(2);} else {rgb[0] = hex.substring(0, 2);rgb[1] = hex.substring(2, 4);rgb[2] = hex.substring(4);}
for(var i = 0; i < rgb.length; i++) {rgb[i] = hexNum.indexOf(rgb[i].charAt(0)) * 16 + hexNum.indexOf(rgb[i].charAt(1));}
return rgb;}
dojo.gfx.color.rgb2hex = function(r, g, b) {if(dojo.lang.isArray(r)) {g = r[1] || 0;b = r[2] || 0;r = r[0] || 0;}
var ret = dojo.lang.map([r, g, b], function(x) {x = new Number(x);var s = x.toString(16);while(s.length < 2) { s = "0" + s; }
return s;});ret.unshift("#");return ret.join("");}
dojo.provide("dojo.lfx.Animation");dojo.lfx.Line = function( start,  end){this.start = start;this.end = end;if(dojo.lang.isArray(start)){var diff = [];dojo.lang.forEach(this.start, function(s,i){diff[i] = this.end[i] - s;}, this);this.getValue = function( n){var res = [];dojo.lang.forEach(this.start, function(s, i){res[i] = (diff[i] * n) + s;}, this);return res;}}else{var diff = end - start;this.getValue = function( n){return (diff * n) + this.start;}}}
dojo.lfx.easeDefault = function( n){if(dojo.render.html.khtml){return (parseFloat("0.5")+((Math.sin( (n+parseFloat("1.5")) * Math.PI))/2));}else{return (0.5+((Math.sin( (n+1.5) * Math.PI))/2));}}
dojo.lfx.easeIn = function( n){return Math.pow(n, 3);}
dojo.lfx.easeOut = function( n){return ( 1 - Math.pow(1 - n, 3) );}
dojo.lfx.easeInOut = function( n){return ( (3 * Math.pow(n, 2)) - (2 * Math.pow(n, 3)) );}
dojo.lfx.IAnimation = function(){}
dojo.lang.extend(dojo.lfx.IAnimation, {curve: null,duration: 1000,easing: null,repeatCount: 0,rate: 25,handler: null,beforeBegin: null,onBegin: null,onAnimate: null,onEnd: null,onPlay: null,onPause: null,onStop: null,play: null,pause: null,stop: null,connect: function( evt,  scope,  newFunc){if(!newFunc){newFunc = scope;scope = this;}
newFunc = dojo.lang.hitch(scope, newFunc);var oldFunc = this[evt]||function(){};this[evt] = function(){var ret = oldFunc.apply(this, arguments);newFunc.apply(this, arguments);return ret;}
return this;},fire: function( evt,  args){if(this[evt]){this[evt].apply(this, (args||[]));}
return this;},repeat: function( count){this.repeatCount = count;return this;},_active: false,_paused: false});dojo.lfx.Animation = function(	 handlers,duration,curve,easing,repeatCount,rate){dojo.lfx.IAnimation.call(this);if(dojo.lang.isNumber(handlers)||(!handlers && duration.getValue)){rate = repeatCount;repeatCount = easing;easing = curve;curve = duration;duration = handlers;handlers = null;}else if(handlers.getValue||dojo.lang.isArray(handlers)){rate = easing;repeatCount = curve;easing = duration;curve = handlers;duration = null;handlers = null;}
if(dojo.lang.isArray(curve)){this.curve = new dojo.lfx.Line(curve[0], curve[1]);}else{this.curve = curve;}
if(duration != null && duration > 0){ this.duration = duration; }
if(repeatCount){ this.repeatCount = repeatCount; }
if(rate){ this.rate = rate; }
if(handlers){dojo.lang.forEach([
"handler", "beforeBegin", "onBegin","onEnd", "onPlay", "onStop", "onAnimate"
], function(item){if(handlers[item]){this.connect(item, handlers[item]);}}, this);}
if(easing && dojo.lang.isFunction(easing)){this.easing=easing;}}
dojo.inherits(dojo.lfx.Animation, dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Animation, {_startTime: null,_endTime: null,_timer: null,_percent: 0,_startRepeatCount: 0,play: function( delay,  gotoStart){if(gotoStart){clearTimeout(this._timer);this._active = false;this._paused = false;this._percent = 0;}else if(this._active && !this._paused){return this;}
this.fire("handler", ["beforeBegin"]);this.fire("beforeBegin");if(delay > 0){setTimeout(dojo.lang.hitch(this, function(){ this.play(null, gotoStart); }), delay);return this;}
this._startTime = new Date().valueOf();if(this._paused){this._startTime -= (this.duration * this._percent / 100);}
this._endTime = this._startTime + this.duration;this._active = true;this._paused = false;var step = this._percent / 100;var value = this.curve.getValue(step);if(this._percent == 0 ){if(!this._startRepeatCount){this._startRepeatCount = this.repeatCount;}
this.fire("handler", ["begin", value]);this.fire("onBegin", [value]);}
this.fire("handler", ["play", value]);this.fire("onPlay", [value]);this._cycle();return this;},pause: function(){clearTimeout(this._timer);if(!this._active){ return this; }
this._paused = true;var value = this.curve.getValue(this._percent / 100);this.fire("handler", ["pause", value]);this.fire("onPause", [value]);return this;},gotoPercent: function( pct,  andPlay){clearTimeout(this._timer);this._active = true;this._paused = true;this._percent = pct;if(andPlay){ this.play(); }
return this;},stop: function( gotoEnd){clearTimeout(this._timer);var step = this._percent / 100;if(gotoEnd){step = 1;}
var value = this.curve.getValue(step);this.fire("handler", ["stop", value]);this.fire("onStop", [value]);this._active = false;this._paused = false;return this;},status: function(){if(this._active){return this._paused ? "paused" : "playing";}else{return "stopped";}
return this;},_cycle: function(){clearTimeout(this._timer);if(this._active){var curr = new Date().valueOf();var step = (curr - this._startTime) / (this._endTime - this._startTime);if(step >= 1){step = 1;this._percent = 100;}else{this._percent = step * 100;}
if((this.easing)&&(dojo.lang.isFunction(this.easing))){step = this.easing(step);}
var value = this.curve.getValue(step);this.fire("handler", ["animate", value]);this.fire("onAnimate", [value]);if( step < 1 ){this._timer = setTimeout(dojo.lang.hitch(this, "_cycle"), this.rate);}else{this._active = false;this.fire("handler", ["end"]);this.fire("onEnd");if(this.repeatCount > 0){this.repeatCount--;this.play(null, true);}else if(this.repeatCount == -1){this.play(null, true);}else{if(this._startRepeatCount){this.repeatCount = this._startRepeatCount;this._startRepeatCount = 0;}}}}
return this;}});dojo.lfx.Combine = function( animations){dojo.lfx.IAnimation.call(this);this._anims = [];this._animsEnded = 0;var anims = arguments;if(anims.length == 1 && (dojo.lang.isArray(anims[0]) || dojo.lang.isArrayLike(anims[0]))){anims = anims[0];}
dojo.lang.forEach(anims, function(anim){this._anims.push(anim);anim.connect("onEnd", dojo.lang.hitch(this, "_onAnimsEnded"));}, this);}
dojo.inherits(dojo.lfx.Combine, dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Combine, {_animsEnded: 0,play: function( delay,  gotoStart){if( !this._anims.length ){ return this; }
this.fire("beforeBegin");if(delay > 0){setTimeout(dojo.lang.hitch(this, function(){ this.play(null, gotoStart); }), delay);return this;}
if(gotoStart || this._anims[0].percent == 0){this.fire("onBegin");}
this.fire("onPlay");this._animsCall("play", null, gotoStart);return this;},pause: function(){this.fire("onPause");this._animsCall("pause");return this;},stop: function( gotoEnd){this.fire("onStop");this._animsCall("stop", gotoEnd);return this;},_onAnimsEnded: function(){this._animsEnded++;if(this._animsEnded >= this._anims.length){this.fire("onEnd");}
return this;},_animsCall: function( funcName){var args = [];if(arguments.length > 1){for(var i = 1 ; i < arguments.length ; i++){args.push(arguments[i]);}}
var _this = this;dojo.lang.forEach(this._anims, function(anim){anim[funcName](args);}, _this);return this;}});dojo.lfx.Chain = function( animations) {dojo.lfx.IAnimation.call(this);this._anims = [];this._currAnim = -1;var anims = arguments;if(anims.length == 1 && (dojo.lang.isArray(anims[0]) || dojo.lang.isArrayLike(anims[0]))){anims = anims[0];}
var _this = this;dojo.lang.forEach(anims, function(anim, i, anims_arr){this._anims.push(anim);if(i < anims_arr.length - 1){anim.connect("onEnd", dojo.lang.hitch(this, "_playNext") );}else{anim.connect("onEnd", dojo.lang.hitch(this, function(){ this.fire("onEnd"); }) );}}, this);}
dojo.inherits(dojo.lfx.Chain, dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Chain, {_currAnim: -1,play: function( delay,  gotoStart){if( !this._anims.length ) { return this; }
if( gotoStart || !this._anims[this._currAnim] ) {this._currAnim = 0;}
var currentAnimation = this._anims[this._currAnim];this.fire("beforeBegin");if(delay > 0){setTimeout(dojo.lang.hitch(this, function(){ this.play(null, gotoStart); }), delay);return this;}
if(currentAnimation){if(this._currAnim == 0){this.fire("handler", ["begin", this._currAnim]);this.fire("onBegin", [this._currAnim]);}
this.fire("onPlay", [this._currAnim]);currentAnimation.play(null, gotoStart);}
return this;},pause: function(){if( this._anims[this._currAnim] ) {this._anims[this._currAnim].pause();this.fire("onPause", [this._currAnim]);}
return this;},playPause: function(){if(this._anims.length == 0){ return this; }
if(this._currAnim == -1){ this._currAnim = 0; }
var currAnim = this._anims[this._currAnim];if( currAnim ) {if( !currAnim._active || currAnim._paused ) {this.play();} else {this.pause();}}
return this;},stop: function(){var currAnim = this._anims[this._currAnim];if(currAnim){currAnim.stop();this.fire("onStop", [this._currAnim]);}
return currAnim;},_playNext: function(){if( this._currAnim == -1 || this._anims.length == 0 ) { return this; }
this._currAnim++;if( this._anims[this._currAnim] ){this._anims[this._currAnim].play(null, true);}
return this;}});dojo.lfx.combine = function( animations){var anims = arguments;if(dojo.lang.isArray(arguments[0])){anims = arguments[0];}
if(anims.length == 1){ return anims[0]; }
return new dojo.lfx.Combine(anims);}
dojo.lfx.chain = function( animations){var anims = arguments;if(dojo.lang.isArray(arguments[0])){anims = arguments[0];}
if(anims.length == 1){ return anims[0]; }
return new dojo.lfx.Chain(anims);}
dojo.provide("dojo.html.color");dojo.html.getBackgroundColor = function(node){node = dojo.byId(node);var color;do{color = dojo.html.getStyle(node, "background-color");if(color.toLowerCase() == "rgba(0, 0, 0, 0)") { color = "transparent"; }
if(node == document.getElementsByTagName("body")[0]) { node = null; break; }
node = node.parentNode;}while(node && dojo.lang.inArray(["transparent", ""], color));if(color == "transparent"){color = [255, 255, 255, 0];}else{color = dojo.gfx.color.extractRGB(color);}
return color;}
dojo.provide("dojo.lfx.html");dojo.lfx.html._byId = function(nodes){if(!nodes){ return []; }
if(dojo.lang.isArrayLike(nodes)){if(!nodes.alreadyChecked){var n = [];dojo.lang.forEach(nodes, function(node){n.push(dojo.byId(node));});n.alreadyChecked = true;return n;}else{return nodes;}}else{var n = [];n.push(dojo.byId(nodes));n.alreadyChecked = true;return n;}}
dojo.lfx.html.propertyAnimation = function(	 nodes,propertyMap,duration,easing,handlers){nodes = dojo.lfx.html._byId(nodes);var targs = {"propertyMap": propertyMap,"nodes": nodes,"duration": duration,"easing": easing||dojo.lfx.easeDefault};var setEmUp = function(args){if(args.nodes.length==1){var pm = args.propertyMap;if(!dojo.lang.isArray(args.propertyMap)){var parr = [];for(var pname in pm){pm[pname].property = pname;parr.push(pm[pname]);}
pm = args.propertyMap = parr;}
dojo.lang.forEach(pm, function(prop){if(dj_undef("start", prop)){if(prop.property != "opacity"){prop.start = parseInt(dojo.html.getComputedStyle(args.nodes[0], prop.property));}else{prop.start = dojo.html.getOpacity(args.nodes[0]);}}});}}
var coordsAsInts = function(coords){var cints = [];dojo.lang.forEach(coords, function(c){cints.push(Math.round(c));});return cints;}
var setStyle = function(n, style){n = dojo.byId(n);if(!n || !n.style){ return; }
for(var s in style){try{if(s == "opacity"){dojo.html.setOpacity(n, style[s]);}else{n.style[s] = style[s];}}catch(e){ dojo.debug(e); }}}
var propLine = function(properties){this._properties = properties;this.diffs = new Array(properties.length);dojo.lang.forEach(properties, function(prop, i){if(dojo.lang.isFunction(prop.start)){prop.start = prop.start(prop, i);}
if(dojo.lang.isFunction(prop.end)){prop.end = prop.end(prop, i);}
if(dojo.lang.isArray(prop.start)){this.diffs[i] = null;}else if(prop.start instanceof dojo.gfx.color.Color){prop.startRgb = prop.start.toRgb();prop.endRgb = prop.end.toRgb();}else{this.diffs[i] = prop.end - prop.start;}}, this);this.getValue = function(n){var ret = {};dojo.lang.forEach(this._properties, function(prop, i){var value = null;if(dojo.lang.isArray(prop.start)){}else if(prop.start instanceof dojo.gfx.color.Color){value = (prop.units||"rgb") + "(";for(var j = 0 ; j < prop.startRgb.length ; j++){value += Math.round(((prop.endRgb[j] - prop.startRgb[j]) * n) + prop.startRgb[j]) + (j < prop.startRgb.length - 1 ? "," : "");}
value += ")";}else{value = ((this.diffs[i]) * n) + prop.start + (prop.property != "opacity" ? prop.units||"px" : "");}
ret[dojo.html.toCamelCase(prop.property)] = value;}, this);return ret;}}
var anim = new dojo.lfx.Animation({beforeBegin: function(){setEmUp(targs);anim.curve = new propLine(targs.propertyMap);},onAnimate: function(propValues){dojo.lang.forEach(targs.nodes, function(node){setStyle(node, propValues);});}},targs.duration,null,targs.easing
);if(handlers){for(var x in handlers){if(dojo.lang.isFunction(handlers[x])){anim.connect(x, anim, handlers[x]);}}}
return anim;}
dojo.lfx.html._makeFadeable = function(nodes){var makeFade = function(node){if(dojo.render.html.ie){if( (node.style.zoom.length == 0) &&
(dojo.html.getStyle(node, "zoom") == "normal") ){node.style.zoom = "1";}
if(	(node.style.width.length == 0) &&
(dojo.html.getStyle(node, "width") == "auto") ){node.style.width = "auto";}}}
if(dojo.lang.isArrayLike(nodes)){dojo.lang.forEach(nodes, makeFade);}else{makeFade(nodes);}}
dojo.lfx.html.fade = function( nodes,values,duration,easing,callback){nodes = dojo.lfx.html._byId(nodes);var props = { property: "opacity" };if(!dj_undef("start", values)){props.start = values.start;}else{props.start = function(){ return dojo.html.getOpacity(nodes[0]); };}
if(!dj_undef("end", values)){props.end = values.end;}else{dojo.raise("dojo.lfx.html.fade needs an end value");}
var anim = dojo.lfx.propertyAnimation(nodes, [ props ], duration, easing);anim.connect("beforeBegin", function(){dojo.lfx.html._makeFadeable(nodes);});if(callback){anim.connect("onEnd", function(){ callback(nodes, anim); });}
return anim;}
dojo.lfx.html.fadeIn = function( nodes,  duration,  easing,  callback){return dojo.lfx.html.fade(nodes, { end: 1 }, duration, easing, callback);}
dojo.lfx.html.fadeOut = function( nodes,  duration,  easing,  callback){return dojo.lfx.html.fade(nodes, { end: 0 }, duration, easing, callback);}
dojo.lfx.html.fadeShow = function( nodes,  duration,  easing,  callback){nodes=dojo.lfx.html._byId(nodes);dojo.lang.forEach(nodes, function(node){dojo.html.setOpacity(node, 0.0);});var anim = dojo.lfx.html.fadeIn(nodes, duration, easing, callback);anim.connect("beforeBegin", function(){if(dojo.lang.isArrayLike(nodes)){dojo.lang.forEach(nodes, dojo.html.show);}else{dojo.html.show(nodes);}});return anim;}
dojo.lfx.html.fadeHide = function( nodes,  duration,  easing,  callback){var anim = dojo.lfx.html.fadeOut(nodes, duration, easing, function(){if(dojo.lang.isArrayLike(nodes)){dojo.lang.forEach(nodes, dojo.html.hide);}else{dojo.html.hide(nodes);}
if(callback){ callback(nodes, anim); }});return anim;}
dojo.lfx.html.wipeIn = function( nodes,  duration,  easing,  callback){nodes = dojo.lfx.html._byId(nodes);var anims = [];dojo.lang.forEach(nodes, function(node){var oprop = {  };var origTop, origLeft, origPosition;with(node.style){origTop=top; origLeft=left; origPosition=position;top="-9999px"; left="-9999px"; position="absolute";display="";}
var height = dojo.html.getBorderBox(node).height;with(node.style){top=origTop; left=origLeft; position=origPosition;display="none";}
var anim = dojo.lfx.propertyAnimation(node,{	"height": {start: 1,end: function(){ return height; }}},duration,easing);anim.connect("beforeBegin", function(){oprop.overflow = node.style.overflow;oprop.height = node.style.height;with(node.style){overflow = "hidden";height = "1px";}
dojo.html.show(node);});anim.connect("onEnd", function(){with(node.style){overflow = oprop.overflow;height = oprop.height;}
if(callback){ callback(node, anim); }});anims.push(anim);});return dojo.lfx.combine(anims);}
dojo.lfx.html.wipeOut = function( nodes,  duration,  easing,  callback){nodes = dojo.lfx.html._byId(nodes);var anims = [];dojo.lang.forEach(nodes, function(node){var oprop = {  };var anim = dojo.lfx.propertyAnimation(node,{	"height": {start: function(){ return dojo.html.getContentBox(node).height; },end: 1}},duration,easing,{"beforeBegin": function(){oprop.overflow = node.style.overflow;oprop.height = node.style.height;with(node.style){overflow = "hidden";}
dojo.html.show(node);},"onEnd": function(){dojo.html.hide(node);with(node.style){overflow = oprop.overflow;height = oprop.height;}
if(callback){ callback(node, anim); }}}
);anims.push(anim);});return dojo.lfx.combine(anims);}
dojo.lfx.html.slideTo = function( nodes,coords,duration,easing,callback){nodes = dojo.lfx.html._byId(nodes);var anims = [];var compute = dojo.html.getComputedStyle;dojo.lang.forEach(nodes, function(node){var top = null;var left = null;var init = (function(){var innerNode = node;return function(){var pos = compute(innerNode, 'position');top = (pos == 'absolute' ? node.offsetTop : parseInt(compute(node, 'top')) || 0);left = (pos == 'absolute' ? node.offsetLeft : parseInt(compute(node, 'left')) || 0);if (!dojo.lang.inArray(['absolute', 'relative'], pos)) {var ret = dojo.html.abs(innerNode, true);dojo.html.setStyleAttributes(innerNode, "position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");top = ret.y;left = ret.x;}}})();init();var anim = dojo.lfx.propertyAnimation(node,{	"top": { start: top, end: (coords.top||0) },"left": { start: left, end: (coords.left||0)  }},duration,easing,{ "beforeBegin": init }
);if(callback){anim.connect("onEnd", function(){ callback(nodes, anim); });}
anims.push(anim);});return dojo.lfx.combine(anims);}
dojo.lfx.html.slideBy = function( nodes,  coords,  duration,  easing,  callback){nodes = dojo.lfx.html._byId(nodes);var anims = [];var compute = dojo.html.getComputedStyle;dojo.lang.forEach(nodes, function(node){var top = null;var left = null;var init = (function(){var innerNode = node;return function(){var pos = compute(innerNode, 'position');top = (pos == 'absolute' ? node.offsetTop : parseInt(compute(node, 'top')) || 0);left = (pos == 'absolute' ? node.offsetLeft : parseInt(compute(node, 'left')) || 0);if (!dojo.lang.inArray(['absolute', 'relative'], pos)) {var ret = dojo.html.abs(innerNode, true);dojo.html.setStyleAttributes(innerNode, "position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");top = ret.y;left = ret.x;}}})();init();var anim = dojo.lfx.propertyAnimation(node,{"top": { start: top, end: top+(coords.top||0) },"left": { start: left, end: left+(coords.left||0) }},duration,easing).connect("beforeBegin", init);if(callback){anim.connect("onEnd", function(){ callback(nodes, anim); });}
anims.push(anim);});return dojo.lfx.combine(anims);}
dojo.lfx.html.explode = function( start,endNode,duration,easing,callback){var h = dojo.html;start = dojo.byId(start);endNode = dojo.byId(endNode);var startCoords = h.toCoordinateObject(start, true);var outline = document.createElement("div");h.copyStyle(outline, endNode);if(endNode.explodeClassName){ outline.className = endNode.explodeClassName; }
with(outline.style){position = "absolute";display = "none";var backgroundStyle = h.getStyle(start, "background-color");backgroundColor = backgroundStyle ? backgroundStyle.toLowerCase() : "transparent";backgroundColor = (backgroundColor == "transparent") ? "rgb(221, 221, 221)" : backgroundColor;}
dojo.body().appendChild(outline);with(endNode.style){visibility = "hidden";display = "block";}
var endCoords = h.toCoordinateObject(endNode, true);with(endNode.style){display = "none";visibility = "visible";}
var props = { opacity: { start: 0.5, end: 1.0 }};dojo.lang.forEach(["height", "width", "top", "left"], function(type){props[type] = { start: startCoords[type], end: endCoords[type] }});var anim = new dojo.lfx.propertyAnimation(outline,props,duration,easing,{"beforeBegin": function(){h.setDisplay(outline, "block");},"onEnd": function(){h.setDisplay(endNode, "block");outline.parentNode.removeChild(outline);}}
);if(callback){anim.connect("onEnd", function(){ callback(endNode, anim); });}
return anim;}
dojo.lfx.html.implode = function( startNode,end,duration,easing,callback){var h = dojo.html;startNode = dojo.byId(startNode);end = dojo.byId(end);var startCoords = dojo.html.toCoordinateObject(startNode, true);var endCoords = dojo.html.toCoordinateObject(end, true);var outline = document.createElement("div");dojo.html.copyStyle(outline, startNode);if (startNode.explodeClassName) { outline.className = startNode.explodeClassName; }
dojo.html.setOpacity(outline, 0.3);with(outline.style){position = "absolute";display = "none";backgroundColor = h.getStyle(startNode, "background-color").toLowerCase();}
dojo.body().appendChild(outline);var props = { opacity: { start: 1.0, end: 0.5 }};dojo.lang.forEach(["height", "width", "top", "left"], function(type){props[type] = { start: startCoords[type], end: endCoords[type] }});var anim = new dojo.lfx.propertyAnimation(outline,props,duration,easing,{"beforeBegin": function(){dojo.html.hide(startNode);dojo.html.show(outline);},"onEnd": function(){outline.parentNode.removeChild(outline);}}
);if(callback){anim.connect("onEnd", function(){ callback(startNode, anim); });}
return anim;}
dojo.lfx.html.highlight = function( nodes,startColor,duration,easing,callback){nodes = dojo.lfx.html._byId(nodes);var anims = [];dojo.lang.forEach(nodes, function(node){var color = dojo.html.getBackgroundColor(node);var bg = dojo.html.getStyle(node, "background-color").toLowerCase();var bgImage = dojo.html.getStyle(node, "background-image");var wasTransparent = (bg == "transparent" || bg == "rgba(0, 0, 0, 0)");while(color.length > 3) { color.pop(); }
var rgb = new dojo.gfx.color.Color(startColor);var endRgb = new dojo.gfx.color.Color(color);var anim = dojo.lfx.propertyAnimation(node,{ "background-color": { start: rgb, end: endRgb }},duration,easing,{"beforeBegin": function(){if(bgImage){node.style.backgroundImage = "none";}
node.style.backgroundColor = "rgb(" + rgb.toRgb().join(",") + ")";},"onEnd": function(){if(bgImage){node.style.backgroundImage = bgImage;}
if(wasTransparent){node.style.backgroundColor = "transparent";}
if(callback){callback(node, anim);}}}
);anims.push(anim);});return dojo.lfx.combine(anims);}
dojo.lfx.html.unhighlight = function( nodes,endColor,duration,easing,callback){nodes = dojo.lfx.html._byId(nodes);var anims = [];dojo.lang.forEach(nodes, function(node){var color = new dojo.gfx.color.Color(dojo.html.getBackgroundColor(node));var rgb = new dojo.gfx.color.Color(endColor);var bgImage = dojo.html.getStyle(node, "background-image");var anim = dojo.lfx.propertyAnimation(node,{ "background-color": { start: color, end: rgb }},duration,easing,{"beforeBegin": function(){if(bgImage){node.style.backgroundImage = "none";}
node.style.backgroundColor = "rgb(" + color.toRgb().join(",") + ")";},"onEnd": function(){if(callback){callback(node, anim);}}}
);anims.push(anim);});return dojo.lfx.combine(anims);}
dojo.lang.mixin(dojo.lfx, dojo.lfx.html);dojo.provide("dojo.lfx.*");dojo.provide("dojo.lfx.toggle");dojo.lfx.toggle.plain = {show: function(node, duration, easing, callback){dojo.html.show(node);if(dojo.lang.isFunction(callback)){ callback(); }},hide: function(node, duration, easing, callback){dojo.html.hide(node);if(dojo.lang.isFunction(callback)){ callback(); }}}
dojo.lfx.toggle.fade = {show: function(node, duration, easing, callback){dojo.lfx.fadeShow(node, duration, easing, callback).play();},hide: function(node, duration, easing, callback){dojo.lfx.fadeHide(node, duration, easing, callback).play();}}
dojo.lfx.toggle.wipe = {show: function(node, duration, easing, callback){dojo.lfx.wipeIn(node, duration, easing, callback).play();},hide: function(node, duration, easing, callback){dojo.lfx.wipeOut(node, duration, easing, callback).play();}}
dojo.lfx.toggle.explode = {show: function(node, duration, easing, callback, explodeSrc){dojo.lfx.explode(explodeSrc||{x:0,y:0,width:0,height:0}, node, duration, easing, callback).play();},hide: function(node, duration, easing, callback, explodeSrc){dojo.lfx.implode(node, explodeSrc||{x:0,y:0,width:0,height:0}, duration, easing, callback).play();}}
dojo.provide("dojo.widget.HtmlWidget");dojo.declare("dojo.widget.HtmlWidget", dojo.widget.DomWidget, {templateCssPath: null,templatePath: null,lang: "",toggle: "plain",toggleDuration: 150,initialize: function(args, frag){},postMixInProperties: function(args, frag){if(this.lang === ""){this.lang = null;}
this.toggleObj =
dojo.lfx.toggle[this.toggle.toLowerCase()] || dojo.lfx.toggle.plain;},createNodesFromText: function(txt, wrap){return dojo.html.createNodesFromText(txt, wrap);},destroyRendering: function(finalize){try{if(this.bgIframe){this.bgIframe.remove();delete this.bgIframe;}
if(!finalize && this.domNode){dojo.event.browser.clean(this.domNode);}
dojo.dom.removeNode(this.domNode);delete this.domNode;}catch(e){  }},isShowing: function(){return dojo.html.isShowing(this.domNode);},toggleShowing: function(){if(this.isShowing()){this.hide();}else{this.show();}},show: function(){if(this.isShowing()){ return; }
this.animationInProgress=true;this.toggleObj.show(this.domNode, this.toggleDuration, null,dojo.lang.hitch(this, this.onShow), this.explodeSrc);},onShow: function(){this.animationInProgress=false;this.checkSize();},hide: function(){if(!this.isShowing()){ return; }
this.animationInProgress = true;this.toggleObj.hide(this.domNode, this.toggleDuration, null,dojo.lang.hitch(this, this.onHide), this.explodeSrc);},onHide: function(){this.animationInProgress=false;},_isResized: function(w, h){if(!this.isShowing()){ return false; }
var wh = dojo.html.getMarginBox(this.domNode);var width=w||wh.width;var height=h||wh.height;if(this.width == width && this.height == height){ return false; }
this.width=width;this.height=height;return true;},checkSize: function(){if(!this._isResized()){ return; }
this.onResized();},resizeTo: function(w, h){dojo.html.setMarginBox(this.domNode, { width: w, height: h });if(this.isShowing()){this.onResized();}},resizeSoon: function(){if(this.isShowing()){dojo.lang.setTimeout(this, this.onResized, 0);}},onResized: function(){dojo.lang.forEach(this.children, function(child){ if(child.checkSize){child.checkSize();}});}});dojo.provide("dojo.widget.*");