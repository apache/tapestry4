if(typeof dojo == "undefined"){var dj_global = this;var dj_currentContext = this;function dj_undef( name,  object){return (typeof (object || dj_currentContext)[name] == "undefined");}
if(dj_undef("djConfig", this)){var djConfig = {};}
if(dj_undef("dojo", this)){var dojo = {};}
dojo.global = function(){return dj_currentContext;}
dojo.locale  = djConfig.locale;dojo.version = {major: 0, minor: 0, patch: 0, flag: "dev",revision: Number("$Rev: 6258 $".match(/[0-9]+/)[0]),toString: function(){with(dojo.version){return major + "." + minor + "." + patch + flag + " (" + revision + ")";}}}
dojo.evalProp = function( name,  object,  create){if((!object)||(!name)) return undefined;if(!dj_undef(name, object)) return object[name];return (create ? (object[name]={}) : undefined);}
dojo.parseObjPath = function( path,  context,  create){var object = (context || dojo.global());var names = path.split('.');var prop = names.pop();for (var i=0,l=names.length;i<l && object;i++){object = dojo.evalProp(names[i], object, create);}
return {obj: object, prop: prop};}
dojo.evalObjPath = function( path,  create){if(typeof path != "string"){return dojo.global();}
if(path.indexOf('.') == -1){return dojo.evalProp(path, dojo.global(), create);}
var ref = dojo.parseObjPath(path, dojo.global(), create);if(ref){return dojo.evalProp(ref.prop, ref.obj, create);}
return null;}
dojo.errorToString = function( exception){if(!dj_undef("message", exception)){return exception.message;}else if(!dj_undef("description", exception)){return exception.description;}else{return exception;}}
dojo.raise = function( message,  exception){if(exception){message = message + ": "+dojo.errorToString(exception);}
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
dojo.hostenv.localesGenerated =["ROOT","es-es","es","it-it","pt-br","de","fr-fr","zh-cn","pt","en-us","zh","fr","zh-tw","it","en-gb","xx","de-de","ko-kr","ja-jp","ko","en","ja"];dojo.hostenv.registerNlsPrefix = function(){dojo.registerModulePath("nls","nls");}
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
for(var i=0; i<extra.length; i++){req(m,b,extra[i]);}};}})();};if (typeof window != 'undefined') {(function() {if(djConfig.allowQueryConfig){var baseUrl = document.location.toString();var params = baseUrl.split("?", 2);if(params.length > 1){var paramStr = params[1];var pairs = paramStr.split("&");for(var x in pairs){var sp = pairs[x].split("=");if((sp[0].length > 9)&&(sp[0].substr(0, 9) == "djConfig.")){var opt = sp[0].substr(9);try{djConfig[opt]=eval(sp[1]);}catch(e){djConfig[opt]=sp[1];}}}}}
if(((djConfig["baseScriptUri"] == "")||(djConfig["baseRelativePath"] == "")) &&(document && document.getElementsByTagName)){var scripts = document.getElementsByTagName("script");var rePkg = /(__package__|dojo|bootstrap1)\.js([\?\.]|$)/i;for(var i = 0; i < scripts.length; i++) {var src = scripts[i].getAttribute("src");if(!src) { continue; }
var m = src.match(rePkg);if(m) {var root = src.substring(0, m.index);if(src.indexOf("bootstrap1") > -1) { root += "../"; }
if(!this["djConfig"]) { djConfig = {}; }
if(djConfig["baseScriptUri"] == "") { djConfig["baseScriptUri"] = root; }
if(djConfig["baseRelativePath"] == "") { djConfig["baseRelativePath"] = root; }
break;}}}
var dr = dojo.render;var drh = dojo.render.html;var drs = dojo.render.svg;var dua = (drh.UA = navigator.userAgent);var dav = (drh.AV = navigator.appVersion);var t = true;var f = false;drh.capable = t;drh.support.builtin = t;dr.ver = parseFloat(drh.AV);dr.os.mac = dav.indexOf("Macintosh") >= 0;dr.os.win = dav.indexOf("Windows") >= 0;dr.os.linux = dav.indexOf("X11") >= 0;drh.opera = dua.indexOf("Opera") >= 0;drh.khtml = (dav.indexOf("Konqueror") >= 0)||(dav.indexOf("Safari") >= 0);drh.safari = dav.indexOf("Safari") >= 0;var geckoPos = dua.indexOf("Gecko");drh.mozilla = drh.moz = (geckoPos >= 0)&&(!drh.khtml);if (drh.mozilla) {drh.geckoVersion = dua.substring(geckoPos + 6, geckoPos + 14);}
drh.ie = (document.all)&&(!drh.opera);drh.ie50 = drh.ie && dav.indexOf("MSIE 5.0")>=0;drh.ie55 = drh.ie && dav.indexOf("MSIE 5.5")>=0;drh.ie60 = drh.ie && dav.indexOf("MSIE 6.0")>=0;drh.ie70 = drh.ie && dav.indexOf("MSIE 7.0")>=0;var cm = document["compatMode"];drh.quirks = (cm == "BackCompat")||(cm == "QuirksMode")||drh.ie55||drh.ie50;dojo.locale = dojo.locale || (drh.ie ? navigator.userLanguage : navigator.language).toLowerCase();dr.vml.capable=drh.ie;drs.capable = f;drs.support.plugin = f;drs.support.builtin = f;var tdoc = window["document"];var tdi = tdoc["implementation"];if((tdi)&&(tdi["hasFeature"])&&(tdi.hasFeature("org.w3c.dom.svg", "1.0"))){drs.capable = t;drs.support.builtin = t;drs.support.plugin = f;}
if(drh.safari){var tmp = dua.split("AppleWebKit/")[1];var ver = parseFloat(tmp.split(" ")[0]);if(ver >= 420){drs.capable = t;drs.support.builtin = t;drs.support.plugin = f;}}})();dojo.hostenv.startPackage("dojo.hostenv");dojo.render.name = dojo.hostenv.name_ = 'browser';dojo.hostenv.searchIds = [];dojo.hostenv._XMLHTTP_PROGIDS = ['Msxml2.XMLHTTP', 'Microsoft.XMLHTTP', 'Msxml2.XMLHTTP.4.0'];dojo.hostenv.getXmlhttpObject = function(){var http = null;var last_e = null;try{ http = new XMLHttpRequest(); }catch(e){}
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
dojo.hostenv.defaultDebugContainerId = 'dojoDebug';dojo.hostenv._println_buffer = [];dojo.hostenv._println_safe = false;dojo.hostenv.println = function (line){if(!dojo.hostenv._println_safe){dojo.hostenv._println_buffer.push(line);}else{try {var console = document.getElementById(djConfig.debugContainerId ?
djConfig.debugContainerId : dojo.hostenv.defaultDebugContainerId);if(!console) { console = dojo.body(); }
var div = document.createElement("div");div.appendChild(document.createTextNode(line));console.appendChild(div);} catch (e) {try{document.write("<div>" + line + "</div>");}catch(e2){window.status = line;}}}}
dojo.addOnLoad(function(){dojo.hostenv._println_safe = true;while(dojo.hostenv._println_buffer.length > 0){dojo.hostenv.println(dojo.hostenv._println_buffer.shift());}});function dj_addNodeEvtHdlr(node, evtName, fp, capture){var oldHandler = node["on"+evtName] || function(){};node["on"+evtName] = function(){fp.apply(node, arguments);oldHandler.apply(node, arguments);}
return true;}
function dj_load_init(e){var type = (e && e.type) ? e.type.toLowerCase() : "load";if(arguments.callee.initialized || (type!="domcontentloaded" && type!="load")){ return; }
arguments.callee.initialized = true;if(typeof(_timer) != 'undefined'){clearInterval(_timer);delete _timer;}
var initFunc = function(){if(dojo.render.html.ie){dojo.hostenv.makeWidgets();}};if(dojo.hostenv.inFlightCount == 0){initFunc();dojo.hostenv.modulesLoaded();}else{dojo.addOnLoad(initFunc);}}
if(document.addEventListener){if(dojo.render.html.opera || (dojo.render.html.moz && !djConfig.delayMozLoadingFix)){document.addEventListener("DOMContentLoaded", dj_load_init, null);}
window.addEventListener("load", dj_load_init, null);}
if(dojo.render.html.ie && dojo.render.os.win){document.attachEvent("onreadystatechange", function(e){if(document.readyState == "complete"){dj_load_init();}});}
if (/(WebKit|khtml)/i.test(navigator.userAgent)) {var _timer = setInterval(function() {if (/loaded|complete/.test(document.readyState)) {dj_load_init();}}, 10);}
if(dojo.render.html.ie){dj_addNodeEvtHdlr(window, "beforeunload", function(){dojo.hostenv._unloading = true;window.setTimeout(function() {dojo.hostenv._unloading = false;}, 0);});}
dj_addNodeEvtHdlr(window, "unload", function(){dojo.hostenv.unloaded();if((!dojo.render.html.ie)||(dojo.render.html.ie && dojo.hostenv._unloading)){dojo.hostenv.unloaded();}});dojo.hostenv.makeWidgets = function(){var sids = [];if(djConfig.searchIds && djConfig.searchIds.length > 0) {sids = sids.concat(djConfig.searchIds);}
if(dojo.hostenv.searchIds && dojo.hostenv.searchIds.length > 0) {sids = sids.concat(dojo.hostenv.searchIds);}
if((djConfig.parseWidgets)||(sids.length > 0)){if(dojo.evalObjPath("dojo.widget.Parse")){var parser = new dojo.xml.Parse();if(sids.length > 0){for(var x=0; x<sids.length; x++){var tmpNode = document.getElementById(sids[x]);if(!tmpNode){ continue; }
var frag = parser.parseElement(tmpNode, null, true);dojo.widget.getParser().createComponents(frag);}}else if(djConfig.parseWidgets){var frag  = parser.parseElement(dojo.body(), null, true);dojo.widget.getParser().createComponents(frag);}}}}
dojo.addOnLoad(function(){if(!dojo.render.html.ie) {dojo.hostenv.makeWidgets();}});try {if (dojo.render.html.ie) {document.namespaces.add("v","urn:schemas-microsoft-com:vml");document.createStyleSheet().addRule("v\\:*", "behavior:url(#default#VML)");}} catch (e) { }
dojo.hostenv.writeIncludes = function(){}
if(!dj_undef("document", this)){dj_currentDocument = this.document;}
dojo.doc = function(){return dj_currentDocument;}
dojo.body = function(){return dojo.doc().body || dojo.doc().getElementsByTagName("body")[0];}
dojo.byId = function(id, doc){if((id)&&((typeof id == "string")||(id instanceof String))){if (!doc) { doc = dj_currentDocument; }
var ele = doc.getElementById(id);if (ele && (ele.id != id) && doc.all) {ele = null;eles = doc.all[id];if (eles) {if (eles.length) {for (var i=0; i < eles.length; i++) {if (eles[i].id == id) {ele = eles[i];break;}}} else { ele = eles; }}}
return ele;}
return id;}
dojo.setContext = function(globalObject,  globalDocument){dj_currentContext = globalObject;dj_currentDocument = globalDocument;};dojo._fireCallback = function(callback, context, cbArguments) {if((context)&&((typeof callback == "string")||(callback instanceof String))){callback=context[callback];}
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
dojo.lang.isFunction = function( it){if(!it){ return false; }
if((typeof(it) == "function") && (it == "[object NodeList]")) { return false; }
return (it instanceof Function || typeof it == "function");}
dojo.lang.isString = function( it){return (typeof it == "string" || it instanceof String);}
dojo.lang.isAlien = function( it){if(!it){ return false; }
return !dojo.lang.isFunction() && /\{\s*\[native code\]\s*\}/.test(String(it));}
dojo.lang.isBoolean = function( it){return (it instanceof Boolean || typeof it == "boolean");}
dojo.lang.isNumber = function( it){return (it instanceof Number || typeof it == "number");}
dojo.lang.isUndefined = function( it){return ((typeof(it) == "undefined")&&(it == undefined));}
dojo.provide("dojo.lang.array");dojo.lang.has = function(obj, name){try{return typeof obj[name] != "undefined";}catch(e){ return false; }}
dojo.lang.isEmpty = function(obj){if(dojo.lang.isObject(obj)){var tmp = {};var count = 0;for(var x in obj){if(obj[x] && (!tmp[x])){count++;break;}}
return count == 0;}else if(dojo.lang.isArrayLike(obj) || dojo.lang.isString(obj)){return obj.length == 0;}}
dojo.lang.map = function(arr, obj, unary_func){var isString = dojo.lang.isString(arr);if(isString){arr = arr.split("");}
if(dojo.lang.isFunction(obj)&&(!unary_func)){unary_func = obj;obj = dj_global;}else if(dojo.lang.isFunction(obj) && unary_func){var tmpObj = obj;obj = unary_func;unary_func = tmpObj;}
if(Array.map){var outArr = Array.map(arr, unary_func, obj);}else{var outArr = [];for(var i=0;i<arr.length;++i){outArr.push(unary_func.call(obj, arr[i]));}}
if(isString) {return outArr.join("");} else {return outArr;}}
dojo.lang.reduce = function(arr, initialValue, obj, binary_func){var reducedValue = initialValue;var ob = obj ? obj : dj_global;dojo.lang.map(arr,function(val){reducedValue = binary_func.call(ob, reducedValue, val);}
);return reducedValue;}
dojo.lang.forEach = function(anArray, callback, thisObject){if(dojo.lang.isString(anArray)){anArray = anArray.split("");}
if(Array.forEach){Array.forEach(anArray, callback, thisObject);}else{if(!thisObject){thisObject=dj_global;}
for(var i=0,l=anArray.length; i<l; i++){callback.call(thisObject, anArray[i], i, anArray);}}}
dojo.lang._everyOrSome = function(every, arr, callback, thisObject){if(dojo.lang.isString(arr)){arr = arr.split("");}
if(Array.every){return Array[ every ? "every" : "some" ](arr, callback, thisObject);}else{if(!thisObject){thisObject = dj_global;}
for(var i=0,l=arr.length; i<l; i++){var result = callback.call(thisObject, arr[i], i, arr);if(every && !result){return false;}else if((!every)&&(result)){return true;}}
return Boolean(every);}}
dojo.lang.every = function(arr, callback, thisObject){return this._everyOrSome(true, arr, callback, thisObject);}
dojo.lang.some = function(arr, callback, thisObject){return this._everyOrSome(false, arr, callback, thisObject);}
dojo.lang.filter = function(arr, callback, thisObject){var isString = dojo.lang.isString(arr);if(isString){ arr = arr.split(""); }
var outArr;if(Array.filter){outArr = Array.filter(arr, callback, thisObject);} else {if(!thisObject){if(arguments.length >= 3){ dojo.raise("thisObject doesn't exist!"); }
thisObject = dj_global;}
outArr = [];for(var i = 0; i < arr.length; i++){if(callback.call(thisObject, arr[i], i, arr)){outArr.push(arr[i]);}}}
if(isString){return outArr.join("");} else {return outArr;}}
dojo.lang.unnest = function(){var out = [];for(var i = 0; i < arguments.length; i++){if(dojo.lang.isArrayLike(arguments[i])){var add = dojo.lang.unnest.apply(this, arguments[i]);out = out.concat(add);}else{out.push(arguments[i]);}}
return out;}
dojo.lang.toArray = function(arrayLike, startOffset){var array = [];for(var i = startOffset||0; i < arrayLike.length; i++){array.push(arrayLike[i]);}
return array;}
dojo.provide("dojo.lang.extras");dojo.lang.setTimeout = function(func, delay ){var context = window, argsStart = 2;if(!dojo.lang.isFunction(func)){context = func;func = delay;delay = arguments[2];argsStart++;}
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
dojo.provide("dojo.event.common");dojo.event = new function(){this._canTimeout = dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);function interpolateArgs(args, searchForNames){var dl = dojo.lang;var ao = {srcObj: dj_global,srcFunc: null,adviceObj: dj_global,adviceFunc: null,aroundObj: null,aroundFunc: null,adviceType: (args.length>2) ? args[0] : "after",precedence: "last",once: false,delay: null,rate: 0,adviceMsg: false};switch(args.length){case 0: return;case 1: return;case 2:
ao.srcFunc = args[0];ao.adviceFunc = args[1];break;case 3:
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isString(args[2]))){ao.adviceType = "after";ao.srcObj = args[0];ao.srcFunc = args[1];ao.adviceFunc = args[2];}else if((dl.isString(args[1]))&&(dl.isString(args[2]))){ao.srcFunc = args[1];ao.adviceFunc = args[2];}else if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isFunction(args[2]))){ao.adviceType = "after";ao.srcObj = args[0];ao.srcFunc = args[1];var tmpName  = dl.nameAnonFunc(args[2], ao.adviceObj, searchForNames);ao.adviceFunc = tmpName;}else if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){ao.adviceType = "after";ao.srcObj = dj_global;var tmpName  = dl.nameAnonFunc(args[0], ao.srcObj, searchForNames);ao.srcFunc = tmpName;ao.adviceObj = args[1];ao.adviceFunc = args[2];}
break;case 4:
if((dl.isObject(args[0]))&&(dl.isObject(args[2]))){ao.adviceType = "after";ao.srcObj = args[0];ao.srcFunc = args[1];ao.adviceObj = args[2];ao.adviceFunc = args[3];}else if((dl.isString(args[0]))&&(dl.isString(args[1]))&&(dl.isObject(args[2]))){ao.adviceType = args[0];ao.srcObj = dj_global;ao.srcFunc = args[1];ao.adviceObj = args[2];ao.adviceFunc = args[3];}else if((dl.isString(args[0]))&&(dl.isFunction(args[1]))&&(dl.isObject(args[2]))){ao.adviceType = args[0];ao.srcObj = dj_global;var tmpName  = dl.nameAnonFunc(args[1], dj_global, searchForNames);ao.srcFunc = tmpName;ao.adviceObj = args[2];ao.adviceFunc = args[3];}else if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){ao.srcObj = args[1];ao.srcFunc = args[2];var tmpName  = dl.nameAnonFunc(args[3], dj_global, searchForNames);ao.adviceObj = dj_global;ao.adviceFunc = tmpName;}else if(dl.isObject(args[1])){ao.srcObj = args[1];ao.srcFunc = args[2];ao.adviceObj = dj_global;ao.adviceFunc = args[3];}else if(dl.isObject(args[2])){ao.srcObj = dj_global;ao.srcFunc = args[1];ao.adviceObj = args[2];ao.adviceFunc = args[3];}else{ao.srcObj = ao.adviceObj = ao.aroundObj = dj_global;ao.srcFunc = args[1];ao.adviceFunc = args[2];ao.aroundFunc = args[3];}
break;case 6:
ao.srcObj = args[1];ao.srcFunc = args[2];ao.adviceObj = args[3]
ao.adviceFunc = args[4];ao.aroundFunc = args[5];ao.aroundObj = dj_global;break;default:
ao.srcObj = args[1];ao.srcFunc = args[2];ao.adviceObj = args[3]
ao.adviceFunc = args[4];ao.aroundObj = args[5];ao.aroundFunc = args[6];ao.once = args[7];ao.delay = args[8];ao.rate = args[9];ao.adviceMsg = args[10];break;}
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
this._kwConnectImpl = function(kwArgs, disconnect){var fn = (disconnect) ? "disconnect" : "connect";if(typeof kwArgs["srcFunc"] == "function"){kwArgs.srcObj = kwArgs["srcObj"]||dj_global;var tmpName  = dojo.lang.nameAnonFunc(kwArgs.srcFunc, kwArgs.srcObj, true);kwArgs.srcFunc = tmpName;}
if(typeof kwArgs["adviceFunc"] == "function"){kwArgs.adviceObj = kwArgs["adviceObj"]||dj_global;var tmpName  = dojo.lang.nameAnonFunc(kwArgs.adviceFunc, kwArgs.adviceObj, true);kwArgs.adviceFunc = tmpName;}
kwArgs.srcObj = kwArgs["srcObj"]||dj_global;kwArgs.adviceObj = kwArgs["adviceObj"]||kwArgs["targetObj"]||dj_global;kwArgs.adviceFunc = kwArgs["adviceFunc"]||kwArgs["targetFunc"];return dojo.event[fn](kwArgs);}
this.kwConnect = function( kwArgs){return this._kwConnectImpl(kwArgs, false);}
this.disconnect = function(){if(arguments.length == 1){var ao = arguments[0];}else{var ao = interpolateArgs(arguments, true);}
if(!ao.adviceFunc){ return; }
if(dojo.lang.isString(ao.srcFunc) && (ao.srcFunc.toLowerCase() == "onkey") ){if(dojo.render.html.ie){ao.srcFunc = "onkeydown";this.disconnect(ao);}
ao.srcFunc = "onkeypress";}
var mjp = dojo.event.MethodJoinPoint.getForMethod(ao.srcObj, ao.srcFunc);return mjp.removeAdvice(ao.adviceObj, ao.adviceFunc, ao.adviceType, ao.once);}
this.kwDisconnect = function(kwArgs){return this._kwConnectImpl(kwArgs, true);}}
dojo.event.MethodInvocation = function(join_point, obj, args){this.jp_ = join_point;this.object = obj;this.args = [];for(var x=0; x<args.length; x++){this.args[x] = args[x];}
this.around_index = -1;}
dojo.event.MethodInvocation.prototype.proceed = function(){this.around_index++;if(this.around_index >= this.jp_.around.length){return this.jp_.object[this.jp_.methodname].apply(this.jp_.object, this.args);}else{var ti = this.jp_.around[this.around_index];var mobj = ti[0]||dj_global;var meth = ti[1];return mobj[meth].call(mobj, this);}}
dojo.event.MethodJoinPoint = function(obj, funcName){this.object = obj||dj_global;this.methodname = funcName;this.methodfunc = this.object[funcName];this.squelch = false;}
dojo.event.MethodJoinPoint.getForMethod = function(obj, funcName){if(!obj){ obj = dj_global; }
if(!obj[funcName]){obj[funcName] = function(){};if(!obj[funcName]){dojo.raise("Cannot set do-nothing method on that object "+funcName);}}else if((!dojo.lang.isFunction(obj[funcName]))&&(!dojo.lang.isAlien(obj[funcName]))){return null;}
var jpname = funcName + "$joinpoint";var jpfuncname = funcName + "$joinpoint$method";var joinpoint = obj[jpname];if(!joinpoint){var isNode = false;if(dojo.event["browser"]){if( (obj["attachEvent"])||
(obj["nodeType"])||
(obj["addEventListener"]) ){isNode = true;dojo.event.browser.addClobberNodeAttrs(obj, [jpname, jpfuncname, funcName]);}}
var origArity = obj[funcName].length;obj[jpfuncname] = obj[funcName];joinpoint = obj[jpname] = new dojo.event.MethodJoinPoint(obj, jpfuncname);obj[funcName] = function(){var args = [];if((isNode)&&(!arguments.length)){var evt = null;try{if(obj.ownerDocument){evt = obj.ownerDocument.parentWindow.event;}else if(obj.documentElement){evt = obj.documentElement.ownerDocument.parentWindow.event;}else if(obj.event){evt = obj.event;}else{evt = window.event;}}catch(e){evt = window.event;}
if(evt){args.push(dojo.event.browser.fixEvent(evt, this));}}else{for(var x=0; x<arguments.length; x++){if((x==0)&&(isNode)&&(dojo.event.browser.isEvent(arguments[x]))){args.push(dojo.event.browser.fixEvent(arguments[x], this));}else{args.push(arguments[x]);}}}
return joinpoint.run.apply(joinpoint, args);}
obj[funcName].__preJoinArity = origArity;}
return joinpoint;}
dojo.lang.extend(dojo.event.MethodJoinPoint, {unintercept: function(){this.object[this.methodname] = this.methodfunc;this.before = [];this.after = [];this.around = [];},disconnect: dojo.lang.forward("unintercept"),run: function(){var obj = this.object||dj_global;var args = arguments;var aargs = [];for(var x=0; x<args.length; x++){aargs[x] = args[x];}
var unrollAdvice  = function(marr){if(!marr){dojo.debug("Null argument to unrollAdvice()");return;}
var callObj = marr[0]||dj_global;var callFunc = marr[1];if(!callObj[callFunc]){dojo.raise("function \"" + callFunc + "\" does not exist on \"" + callObj + "\"");}
var aroundObj = marr[2]||dj_global;var aroundFunc = marr[3];var msg = marr[6];var undef;var to = {args: [],jp_: this,object: obj,proceed: function(){return callObj[callFunc].apply(callObj, to.args);}};to.args = aargs;var delay = parseInt(marr[4]);var hasDelay = ((!isNaN(delay))&&(marr[4]!==null)&&(typeof marr[4] != "undefined"));if(marr[5]){var rate = parseInt(marr[5]);var cur = new Date();var timerSet = false;if((marr["last"])&&((cur-marr.last)<=rate)){if(dojo.event._canTimeout){if(marr["delayTimer"]){clearTimeout(marr.delayTimer);}
var tod = parseInt(rate*2);var mcpy = dojo.lang.shallowCopy(marr);marr.delayTimer = setTimeout(function(){mcpy[5] = 0;unrollAdvice(mcpy);}, tod);}
return;}else{marr.last = cur;}}
if(aroundFunc){aroundObj[aroundFunc].call(aroundObj, to);}else{if((hasDelay)&&((dojo.render.html)||(dojo.render.svg))){dj_global["setTimeout"](function(){if(msg){callObj[callFunc].call(callObj, to);}else{callObj[callFunc].apply(callObj, args);}}, delay);}else{if(msg){callObj[callFunc].call(callObj, to);}else{callObj[callFunc].apply(callObj, args);}}}}
var unRollSquelch = function(){if(this.squelch){try{return unrollAdvice.apply(this, arguments);}catch(e){dojo.debug(e);}}else{return unrollAdvice.apply(this, arguments);}}
if((this["before"])&&(this.before.length>0)){dojo.lang.forEach(this.before.concat(new Array()), unRollSquelch);}
var result;try{if((this["around"])&&(this.around.length>0)){var mi = new dojo.event.MethodInvocation(this, obj, args);result = mi.proceed();}else if(this.methodfunc){result = this.object[this.methodname].apply(this.object, args);}}catch(e){ if(!this.squelch){ dojo.raise(e); }}
if((this["after"])&&(this.after.length>0)){dojo.lang.forEach(this.after.concat(new Array()), unRollSquelch);}
return (this.methodfunc) ? result : null;},getArr: function(kind){var type = "after";if((typeof kind == "string")&&(kind.indexOf("before")!=-1)){type = "before";}else if(kind=="around"){type = "around";}
if(!this[type]){ this[type] = []; }
return this[type];},kwAddAdvice: function(args){this.addAdvice(	args["adviceObj"], args["adviceFunc"],args["aroundObj"], args["aroundFunc"],args["adviceType"], args["precedence"],args["once"], args["delay"], args["rate"],args["adviceMsg"]);},addAdvice: function(	thisAdviceObj, thisAdvice,thisAroundObj, thisAround,adviceType, precedence,once, delay, rate, asMessage){var arr = this.getArr(adviceType);if(!arr){dojo.raise("bad this: " + this);}
var ao = [thisAdviceObj, thisAdvice, thisAroundObj, thisAround, delay, rate, asMessage];if(once){if(this.hasAdvice(thisAdviceObj, thisAdvice, adviceType, arr) >= 0){return;}}
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
evt.key = String.fromCharCode(unifiedCharCode);}}}else if(dojo.render.html.ie){if(!evt.ctrlKey && !evt.altKey && evt.keyCode >= evt.KEY_SPACE){evt.key = String.fromCharCode(evt.keyCode);}}else if(dojo.render.html.safari){switch(evt.keyCode){case 63232: evt.key = evt.KEY_UP_ARROW; break;case 63233: evt.key = evt.KEY_DOWN_ARROW; break;case 63234: evt.key = evt.KEY_LEFT_ARROW; break;case 63235: evt.key = evt.KEY_RIGHT_ARROW; break;default:
evt.key = evt.charCode > 0 ? String.fromCharCode(evt.charCode) : evt.keyCode;}}else{evt.key = evt.charCode > 0 ? String.fromCharCode(evt.charCode) : evt.keyCode;}}}
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
this.stopEvent = function(evt){if(window.event){evt.returnValue = false;evt.cancelBubble = true;}else{evt.preventDefault();evt.stopPropagation();}}}
dojo.provide("dojo.dom");dojo.dom.ELEMENT_NODE                  = 1;dojo.dom.ATTRIBUTE_NODE                = 2;dojo.dom.TEXT_NODE                     = 3;dojo.dom.CDATA_SECTION_NODE            = 4;dojo.dom.ENTITY_REFERENCE_NODE         = 5;dojo.dom.ENTITY_NODE                   = 6;dojo.dom.PROCESSING_INSTRUCTION_NODE   = 7;dojo.dom.COMMENT_NODE                  = 8;dojo.dom.DOCUMENT_NODE                 = 9;dojo.dom.DOCUMENT_TYPE_NODE            = 10;dojo.dom.DOCUMENT_FRAGMENT_NODE        = 11;dojo.dom.NOTATION_NODE                 = 12;dojo.dom.dojoml = "http://www.dojotoolkit.org/2004/dojoml";dojo.dom.xmlns = {svg : "http://www.w3.org/2000/svg",smil : "http://www.w3.org/2001/SMIL20/",mml : "http://www.w3.org/1998/Math/MathML",cml : "http://www.xml-cml.org",xlink : "http://www.w3.org/1999/xlink",xhtml : "http://www.w3.org/1999/xhtml",xul : "http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul",xbl : "http://www.mozilla.org/xbl",fo : "http://www.w3.org/1999/XSL/Format",xsl : "http://www.w3.org/1999/XSL/Transform",xslt : "http://www.w3.org/1999/XSL/Transform",xi : "http://www.w3.org/2001/XInclude",xforms : "http://www.w3.org/2002/01/xforms",saxon : "http://icl.com/saxon",xalan : "http://xml.apache.org/xslt",xsd : "http://www.w3.org/2001/XMLSchema",dt: "http://www.w3.org/2001/XMLSchema-datatypes",xsi : "http://www.w3.org/2001/XMLSchema-instance",rdf : "http://www.w3.org/1999/02/22-rdf-syntax-ns#",rdfs : "http://www.w3.org/2000/01/rdf-schema#",dc : "http://purl.org/dc/elements/1.1/",dcq: "http://purl.org/dc/qualifiers/1.0","soap-env" : "http://schemas.xmlsoap.org/soap/envelope/",wsdl : "http://schemas.xmlsoap.org/wsdl/",AdobeExtensions : "http://ns.adobe.com/AdobeSVGViewerExtensions/3.0/"};dojo.dom.isNode = function(wh){if(typeof Element == "function") {try {return wh instanceof Element;} catch(E) {}} else {return wh && !isNaN(wh.nodeType);}}
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
dojo.dom.removeChildren = function(node){var count = node.childNodes.length;while(node.hasChildNodes()){ node.removeChild(node.firstChild); }
return count;}
dojo.dom.replaceChildren = function(node, newChild){dojo.dom.removeChildren(node);node.appendChild(newChild);}
dojo.dom.removeNode = function(node){if(node && node.parentNode){return node.parentNode.removeChild(node);}}
dojo.dom.getAncestors = function(node, filterFunction, returnFirstHit) {var ancestors = [];var isFunction = (filterFunction && (filterFunction instanceof Function || typeof filterFunction == "function"));while(node) {if (!isFunction || filterFunction(node)) {ancestors.push(node);}
if (returnFirstHit && ancestors.length > 0) {return ancestors[0];}
node = node.parentNode;}
if (returnFirstHit) { return null; }
return ancestors;}
dojo.dom.getAncestorsByTag = function(node, tag, returnFirstHit) {tag = tag.toLowerCase();return dojo.dom.getAncestors(node, function(el){return ((el.tagName)&&(el.tagName.toLowerCase() == tag));}, returnFirstHit);}
dojo.dom.getFirstAncestorByTag = function(node, tag) {return dojo.dom.getAncestorsByTag(node, tag, true);}
dojo.dom.isDescendantOf = function(node, ancestor, guaranteeDescendant){if(guaranteeDescendant && node) { node = node.parentNode; }
while(node) {if(node == ancestor){return true;}
node = node.parentNode;}
return false;}
dojo.dom.innerXML = function(node){if(node.innerXML){return node.innerXML;}else if (node.xml){return node.xml;}else if(typeof XMLSerializer != "undefined"){return (new XMLSerializer()).serializeToString(node);}}
dojo.dom.createDocument = function(){var doc = null;var _document = dojo.doc();if(!dj_undef("ActiveXObject")){var prefixes = [ "MSXML2", "Microsoft", "MSXML", "MSXML3" ];for(var i = 0; i<prefixes.length; i++){try{doc = new ActiveXObject(prefixes[i]+".XMLDOM");}catch(e){  };if(doc){ break; }}}else if((_document.implementation)&&
(_document.implementation.createDocument)){doc = _document.implementation.createDocument("", "", null);}
return doc;}
dojo.dom.createDocumentFromText = function(str, mimetype){if(!mimetype){ mimetype = "text/xml"; }
if(!dj_undef("DOMParser")){var parser = new DOMParser();return parser.parseFromString(str, mimetype);}else if(!dj_undef("ActiveXObject")){var domDoc = dojo.dom.createDocument();if(domDoc){domDoc.async = false;domDoc.loadXML(str);return domDoc;}else{dojo.debug("toXml didn't work?");}}else{var _document = dojo.doc();if(_document.createElement){var tmp = _document.createElement("xml");tmp.innerHTML = str;if(_document.implementation && _document.implementation.createDocument) {var xmlDoc = _document.implementation.createDocument("foo", "", null);for(var i = 0; i < tmp.childNodes.length; i++) {xmlDoc.importNode(tmp.childNodes.item(i), true);}
return xmlDoc;}
return ((tmp.document)&&
(tmp.document.firstChild ?  tmp.document.firstChild : tmp));}}
return null;}
dojo.dom.prependChild = function(node, parent) {if(parent.firstChild) {parent.insertBefore(node, parent.firstChild);} else {parent.appendChild(node);}
return true;}
dojo.dom.insertBefore = function(node, ref, force){if (force != true &&
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
dojo.dom.hasParent = function (node) {return node && node.parentNode && dojo.dom.isNode(node.parentNode);}
dojo.dom.isTag = function(node ) {if(node && node.tagName) {for(var i=1; i<arguments.length; i++){if(node.tagName==String(arguments[i])){return String(arguments[i]);}}}
return "";}
dojo.dom.setAttributeNS = function(elem, namespaceURI, attrName, attrValue){if(elem == null || ((elem == undefined)&&(typeof elem == "undefined"))){dojo.raise("No element given to dojo.dom.setAttributeNS");}
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
dojo.lang.extend(dojo.io.Request, {url: "",mimetype: "text/plain",method: "GET",content: undefined,transport: undefined,changeUrl: undefined,formNode: undefined,sync: false,bindSuccess: false,useCache: false,preventCache: false,load: function( type,  data,  transportImplementation,  kwArgs){},error: function( type,  error,  transportImplementation,  kwArgs){},timeout: function( type,  empty,  transportImplementation,  kwArgs){},handle: function( type,  data,  transportImplementation,  kwArgs){},timeoutSeconds: 0,abort: function(){ },fromKwArgs: function( kwArgs){if(kwArgs["url"]){ kwArgs.url = kwArgs.url.toString(); }
if(kwArgs["formNode"]) { kwArgs.formNode = dojo.byId(kwArgs.formNode); }
if(!kwArgs["method"] && kwArgs["formNode"] && kwArgs["formNode"].method) {kwArgs.method = kwArgs["formNode"].method;}
if(!kwArgs["handle"] && kwArgs["handler"]){ kwArgs.handle = kwArgs.handler; }
if(!kwArgs["load"] && kwArgs["loaded"]){ kwArgs.load = kwArgs.loaded; }
if(!kwArgs["changeUrl"] && kwArgs["changeURL"]) { kwArgs.changeUrl = kwArgs.changeURL; }
kwArgs.encoding = dojo.lang.firstValued(kwArgs["encoding"], djConfig["bindEncoding"], "");kwArgs.sendTransport = dojo.lang.firstValued(kwArgs["sendTransport"], djConfig["ioSendTransport"], false);var isFunction = dojo.lang.isFunction;for(var x=0; x<dojo.io.hdlrFuncNames.length; x++){var fn = dojo.io.hdlrFuncNames[x];if(kwArgs[fn] && isFunction(kwArgs[fn])){ continue; }
if(kwArgs["handle"] && isFunction(kwArgs["handle"])){kwArgs[fn] = kwArgs.handle;}}
dojo.lang.mixin(this, kwArgs);}});dojo.io.Error = function( msg,  type, num){this.message = msg;this.type =  type || "unknown";this.number = num || 0;}
dojo.io.transports.addTransport = function(name){this.push(name);this[name] = dojo.io[name];}
dojo.io.bind = function( request){if(!(request instanceof dojo.io.Request)){try{request = new dojo.io.Request(request);}catch(e){ dojo.debug(e); }}
var tsName = "";if(request["transport"]){tsName = request["transport"];if(!this[tsName]){dojo.io.sendBindError(request, "No dojo.io.bind() transport with name '"
+ request["transport"] + "'.");return request;}
if(!this[tsName].canHandle(request)){dojo.io.sendBindError(request, "dojo.io.bind() transport with name '"
+ request["transport"] + "' cannot handle this type of request.");return request;}}else{for(var x=0; x<dojo.io.transports.length; x++){var tmp = dojo.io.transports[x];if((this[tmp])&&(this[tmp].canHandle(request))){tsName = tmp;break;}}
if(tsName == ""){dojo.io.sendBindError(request, "None of the loaded transports for dojo.io.bind()"
+ " can handle the request.");return request;}}
this[tsName].bind(request);request.bindSuccess = true;return request;}
dojo.io.sendBindError = function(request , message ){if((typeof request.error == "function" || typeof request.handle == "function")
&& (typeof setTimeout == "function" || typeof setTimeout == "object")){var errorObject = new dojo.io.Error(message);setTimeout(function(){request[(typeof request.error == "function") ? "error" : "handle"]("error", errorObject, null, request);}, 50);}else{dojo.raise(message);}}
dojo.io.queueBind = function( request){if(!(request instanceof dojo.io.Request)){try{request = new dojo.io.Request(request);}catch(e){ dojo.debug(e); }}
var oldLoad = request.load;request.load = function(){dojo.io._queueBindInFlight = false;var ret = oldLoad.apply(this, arguments);dojo.io._dispatchNextQueueBind();return ret;}
var oldErr = request.error;request.error = function(){dojo.io._queueBindInFlight = false;var ret = oldErr.apply(this, arguments);dojo.io._dispatchNextQueueBind();return ret;}
dojo.io._bindQueue.push(request);dojo.io._dispatchNextQueueBind();return request;}
dojo.io._dispatchNextQueueBind = function(){if(!dojo.io._queueBindInFlight){dojo.io._queueBindInFlight = true;if(dojo.io._bindQueue.length > 0){dojo.io.bind(dojo.io._bindQueue.shift());}else{dojo.io._queueBindInFlight = false;}}}
dojo.io._bindQueue = [];dojo.io._queueBindInFlight = false;dojo.io.argsFromMap = function( map,  encoding,  last){var enc = /utf/i.test(encoding||"") ? encodeURIComponent : dojo.string.encodeAscii;var mapped = [];var control = new Object();for(var name in map){var domap = function(elt){var val = enc(name)+"="+enc(elt);mapped[(last == name) ? "push" : "unshift"](val);}
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
dojo.undo.browser = {initialHref: window.location.href,initialHash: window.location.hash,moveForward: false,historyStack: [],forwardStack: [],historyIframe: null,bookmarkAnchor: null,locationTimer: null,setInitialState: function(args){this.initialState = this._createState(this.initialHref, args, this.initialHash);},addToHistory: function(args){this.forwardStack = [];var hash = null;var url = null;if(!this.historyIframe){this.historyIframe = window.frames["djhistory"];}
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
dojo.provide("dojo.io.BrowserIO");dojo.io.checkChildrenForFile = function(node){var hasFile = false;var inputs = node.getElementsByTagName("input");dojo.lang.forEach(inputs, function(input){if(hasFile){ return; }
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
dojo.io.transports.addTransport("XMLHTTPTransport");}
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
function formatPattern(dateObject, pattern){return pattern.replace(/[a-zA-Z]+/g, function(match){var s;var c = match.charAt(0);var l = match.length;var pad;var widthList = ["abbr", "wide", "narrow"];switch(c){case 'G':
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
function _buildDateTimeRE(groups, info, options, pattern){return pattern.replace(/[a-zA-Z]+/g, function(match){var s;var c = match.charAt(0);var l = match.length;switch(c){case 'y':
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
return new Date(parts[0], (parseInt(parts[1],10)-1), parts[2], parts[3], parts[4], parts[5]);};