/*
	Copyright (c) 2004-2005, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

/**
* @file bootstrap1.js
*
* bootstrap file that runs before hostenv_*.js file.
*
* @author Copyright 2004 Mark D. Anderson (mda@discerning.com)
* @author Licensed under the Academic Free License 2.1 http://www.opensource.org/licenses/afl-2.1.php
*
* $Id: bootstrap1.js 2889 2006-01-20 18:17:40Z alex $
*/

/**
 * The global djConfig can be set prior to loading the library, to override
 * certain settings.  It does not exist under dojo.* so that it can be set
 * before the dojo variable exists. Setting any of these variables *after* the
 * library has loaded does nothing at all. The variables that can be set are
 * as follows:
 */

/**
 * dj_global is an alias for the top-level global object in the host
 * environment (the "window" object in a browser).
 */
var dj_global = this; //typeof window == 'undefined' ? this : window;

function dj_undef(name, obj){
	if(!obj){ obj = dj_global; }
	return (typeof obj[name] == "undefined");
}

if(dj_undef("djConfig")){
	var djConfig = {};
}

/**
 * dojo is the root variable of (almost all) our public symbols.
 */
var dojo;
if(dj_undef("dojo")){ dojo = {}; }

dojo.version = {
	major: 0, minor: 2, patch: 2, flag: "+",
	revision: Number("$Rev: 2889 $".match(/[0-9]+/)[0]),
	toString: function() {
		with (dojo.version) {
			return major + "." + minor + "." + patch + flag + " (" + revision + ")";
		}
	}
};

/*
 * evaluate a string like "A.B" without using eval.
 */
dojo.evalObjPath = function(objpath, create){
	// fast path for no periods
	if(typeof objpath != "string"){ return dj_global; }
	if(objpath.indexOf('.') == -1){
		if((dj_undef(objpath, dj_global))&&(create)){
			dj_global[objpath] = {};
		}
		return dj_global[objpath];
	}

	var syms = objpath.split(/\./);
	var obj = dj_global;
	for(var i=0;i<syms.length;++i){
		if(!create){
			obj = obj[syms[i]];
			if((typeof obj == 'undefined')||(!obj)){
				return obj;
			}
		}else{
			if(dj_undef(syms[i], obj)){
				obj[syms[i]] = {};
			}
			obj = obj[syms[i]];
		}
	}
	return obj;
};


// ****************************************************************
// global public utils
// ****************************************************************

/*
 * utility to print an Error. 
 * TODO: overriding Error.prototype.toString won't accomplish this?
 * ... since natively generated Error objects do not always reflect such things?
 */
dojo.errorToString = function(excep){
	return ((!dj_undef("message", excep)) ? excep.message : (dj_undef("description", excep) ? excep : excep.description ));
};

/**
* Throws an Error object given the string err. For now, will also do a println
* to the user first.
*/
dojo.raise = function(message, excep){
	if(excep){
		message = message + ": "+dojo.errorToString(excep);
	}
	var he = dojo.hostenv;
	if((!dj_undef("hostenv", dojo))&&(!dj_undef("println", dojo.hostenv))){ 
		dojo.hostenv.println("FATAL: " + message);
	}
	throw Error(message);
};

dj_throw = dj_rethrow = function(m, e){
	dojo.deprecated("dj_throw and dj_rethrow deprecated, use dojo.raise instead");
	dojo.raise(m, e);
};

/**
 * Produce a line of debug output. 
 * Does nothing unless djConfig.isDebug is true.
 * varargs, joined with ''.
 * Caller should not supply a trailing "\n".
 */
dojo.debug = function(){
	if (!djConfig.isDebug) { return; }
	var args = arguments;
	if(dj_undef("println", dojo.hostenv)){
		dojo.raise("dojo.debug not available (yet?)");
	}
	var isJUM = dj_global["jum"] && !dj_global["jum"].isBrowser;
	var s = [(isJUM ? "": "DEBUG: ")];
	for(var i=0;i<args.length;++i){
		if(!false && args[i] instanceof Error){
			var msg = "[" + args[i].name + ": " + dojo.errorToString(args[i]) +
				(args[i].fileName ? ", file: " + args[i].fileName : "") +
				(args[i].lineNumber ? ", line: " + args[i].lineNumber : "") + "]";
		} else {
			try {
				var msg = String(args[i]);
			} catch(e) {
				if(dojo.render.html.ie) {
					var msg = "[ActiveXObject]";
				} else {
					var msg = "[unknown]";
				}
			}
		}
		s.push(msg);
	}
	if(isJUM){ // this seems to be the only way to get JUM to "play nice"
		jum.debug(s.join(" "));
	}else{
		dojo.hostenv.println(s.join(" "));
	}
}

/**
 * this is really hacky for now - just 
 * display the properties of the object
**/

dojo.debugShallow = function(obj){
	if (!djConfig.isDebug) { return; }
	dojo.debug('------------------------------------------------------------');
	dojo.debug('Object: '+obj);
	var props = [];
	for(var prop in obj){
		try {
			props.push(prop + ': ' + obj[prop]);
		} catch(E) {
			props.push(prop + ': ERROR - ' + E.message);
		}
	}
	props.sort();
	for(var i = 0; i < props.length; i++) {
		dojo.debug(props[i]);
	}
	dojo.debug('------------------------------------------------------------');
}

var dj_debug = dojo.debug;

/**
 * We put eval() in this separate function to keep down the size of the trapped
 * evaluation context.
 *
 * Note that:
 * - JSC eval() takes an optional second argument which can be 'unsafe'.
 * - Mozilla/SpiderMonkey eval() takes an optional second argument which is the
 *   scope object for new symbols.
*/
function dj_eval(s){ return dj_global.eval ? dj_global.eval(s) : eval(s); }


/**
 * Convenience for throwing an exception because some function is not
 * implemented.
 */
dj_unimplemented = dojo.unimplemented = function(funcname, extra){
	// FIXME: need to move this away from dj_*
	var mess = "'" + funcname + "' not implemented";
	if((!dj_undef(extra))&&(extra)){ mess += " " + extra; }
	dojo.raise(mess);
}

/**
 * Convenience for informing of deprecated behaviour.
 */
dj_deprecated = dojo.deprecated = function(behaviour, extra, removal){
	var mess = "DEPRECATED: " + behaviour;
	if(extra){ mess += " " + extra; }
	if(removal){ mess += " -- will be removed in version: " + removal; }
	dojo.debug(mess);
}

/**
 * Does inheritance
 */
dojo.inherits = function(subclass, superclass){
	if(typeof superclass != 'function'){ 
		dojo.raise("superclass: "+superclass+" borken");
	}
	subclass.prototype = new superclass();
	subclass.prototype.constructor = subclass;
	subclass.superclass = superclass.prototype;
	// DEPRICATED: super is a reserved word, use 'superclass'
	subclass['super'] = superclass.prototype;
}

dj_inherits = function(subclass, superclass){
	dojo.deprecated("dj_inherits deprecated, use dojo.inherits instead");
	dojo.inherits(subclass, superclass);
}

// an object that authors use determine what host we are running under
dojo.render = (function(){

	function vscaffold(prefs, names){
		var tmp = {
			capable: false,
			support: {
				builtin: false,
				plugin: false
			},
			prefixes: prefs
		};
		for(var x in names){
			tmp[x] = false;
		}
		return tmp;
	}

	return {
		name: "",
		ver: dojo.version,
		os: { win: false, linux: false, osx: false },
		html: vscaffold(["html"], ["ie", "opera", "khtml", "safari", "moz"]),
		svg: vscaffold(["svg"], ["corel", "adobe", "batik"]),
		vml: vscaffold(["vml"], ["ie"]),
		swf: vscaffold(["Swf", "Flash", "Mm"], ["mm"]),
		swt: vscaffold(["Swt"], ["ibm"])
	};
})();

// ****************************************************************
// dojo.hostenv methods that must be defined in hostenv_*.js
// ****************************************************************

/**
 * The interface definining the interaction with the EcmaScript host environment.
*/

/*
 * None of these methods should ever be called directly by library users.
 * Instead public methods such as loadModule should be called instead.
 */
dojo.hostenv = (function(){

	// default configuration options
	var config = {
		isDebug: false,
		allowQueryConfig: false,
		baseScriptUri: "",
		baseRelativePath: "",
		libraryScriptUri: "",
		iePreventClobber: false,
		ieClobberMinimal: true,
		preventBackButtonFix: true,
		searchIds: [],
		parseWidgets: true
	};

	if (typeof djConfig == "undefined") { djConfig = config; }
	else {
		for (var option in config) {
			if (typeof djConfig[option] == "undefined") {
				djConfig[option] = config[option];
			}
		}
	}

	var djc = djConfig;
	function _def(obj, name, def){
		return (dj_undef(name, obj) ? def : obj[name]);
	}

	return {
		name_: '(unset)',
		version_: '(unset)',
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
			this.modulePrefixes_[module] = {name: module, value: prefix};
		},

		getModulePrefix: function(module){
			var mp = this.modulePrefixes_;
			if((mp[module])&&(mp[module]["name"])){
				return mp[module].value;
			}
			return module;
		},

		getTextStack: [],
		loadUriStack: [],
		loadedUris: [],
		// lookup cache for modules.
		// NOTE: this is partially redundant a private variable in the jsdown
		// implementation, but we don't want to couple the two.
		// modules_ : {},
		post_load_: false,
		modulesLoadedListeners: [],
		/**
		 * Return the name of the hostenv.
		 */
		getName: function(){ return this.name_; },

		/**
		* Return the version of the hostenv.
		*/
		getVersion: function(){ return this.version_; },

		/**
		 * Read the plain/text contents at the specified uri.  If getText() is
		 * not implemented, then it is necessary to override loadUri() with an
		 * implementation that doesn't rely on it.
		 */
		getText: function(uri){
			dojo.unimplemented('getText', "uri=" + uri);
		},

		/**
		 * return the uri of the script that defined this function
		 * private method that must be implemented by the hostenv.
		 */
		getLibraryScriptUri: function(){
			// FIXME: need to implement!!!
			dojo.unimplemented('getLibraryScriptUri','');
		}
	};
})();

/**
 * Display a line of text to the user.
 * The line argument should not contain a trailing "\n"; that is added by the
 * implementation.
 */
//dojo.hostenv.println = function(line) {}

// ****************************************************************
// dojo.hostenv methods not defined in hostenv_*.js
// ****************************************************************

/**
 * Return the base script uri that other scripts are found relative to.
 * It is either the empty string, or a non-empty string ending in '/'.
 */
dojo.hostenv.getBaseScriptUri = function(){
	if(djConfig.baseScriptUri.length){ 
		return djConfig.baseScriptUri;
	}
	var uri = new String(djConfig.libraryScriptUri||djConfig.baseRelativePath);
	if (!uri) { dojo.raise("Nothing returned by getLibraryScriptUri(): " + uri); }

	var lastslash = uri.lastIndexOf('/');
	djConfig.baseScriptUri = djConfig.baseRelativePath;
	return djConfig.baseScriptUri;
}

/**
* Set the base script uri.
*/
// In JScript .NET, see interface System._AppDomain implemented by
// System.AppDomain.CurrentDomain. Members include AppendPrivatePath,
// RelativeSearchPath, BaseDirectory.
dojo.hostenv.setBaseScriptUri = function(uri){ djConfig.baseScriptUri = uri }

/**
 * Loads and interprets the script located at relpath, which is relative to the
 * script root directory.  If the script is found but its interpretation causes
 * a runtime exception, that exception is not caught by us, so the caller will
 * see it.  We return a true value if and only if the script is found.
 *
 * For now, we do not have an implementation of a true search path.  We
 * consider only the single base script uri, as returned by getBaseScriptUri().
 *
 * @param relpath A relative path to a script (no leading '/', and typically
 * ending in '.js').
 * @param module A module whose existance to check for after loading a path.
 * Can be used to determine success or failure of the load.
 */
dojo.hostenv.loadPath = function(relpath, module /*optional*/, cb /*optional*/){
	if((relpath.charAt(0) == '/')||(relpath.match(/^\w+:/))){
		dojo.raise("relpath '" + relpath + "'; must be relative");
	}
	var uri = this.getBaseScriptUri() + relpath;
	if(djConfig.cacheBust && dojo.render.html.capable) { uri += "?" + String(djConfig.cacheBust).replace(/\W+/g,""); }
	try{
		return ((!module) ? this.loadUri(uri, cb) : this.loadUriAndCheck(uri, module, cb));
	}catch(e){
		dojo.debug(e);
		return false;
	}
}

/**
 * Reads the contents of the URI, and evaluates the contents.
 * Returns true if it succeeded. Returns false if the URI reading failed.
 * Throws if the evaluation throws.
 * The result of the eval is not available to the caller.
 */
dojo.hostenv.loadUri = function(uri, cb){
	if(this.loadedUris[uri]){
		return;
	}
	var contents = this.getText(uri, null, true);
	if(contents == null){ return 0; }
	this.loadedUris[uri] = true;
	var value = dj_eval(contents);
	return 1;
}

// FIXME: probably need to add logging to this method
dojo.hostenv.loadUriAndCheck = function(uri, module, cb){
	var ok = true;
	try{
		ok = this.loadUri(uri, cb);
	}catch(e){
		dojo.debug("failed loading ", uri, " with error: ", e);
	}
	return ((ok)&&(this.findModule(module, false))) ? true : false;
}

dojo.loaded = function(){ }

dojo.hostenv.loaded = function(){
	this.post_load_ = true;
	var mll = this.modulesLoadedListeners;
	for(var x=0; x<mll.length; x++){
		mll[x]();
	}
	dojo.loaded();
}

/*
Call styles:
	dojo.addOnLoad(functionPointer)
	dojo.addOnLoad(object, "functionName")
*/
dojo.addOnLoad = function(obj, fcnName) {
	if(arguments.length == 1) {
		dojo.hostenv.modulesLoadedListeners.push(obj);
	} else if(arguments.length > 1) {
		dojo.hostenv.modulesLoadedListeners.push(function() {
			obj[fcnName]();
		});
	}
};

dojo.hostenv.modulesLoaded = function(){
	if(this.post_load_){ return; }
	if((this.loadUriStack.length==0)&&(this.getTextStack.length==0)){
		if(this.inFlightCount > 0){ 
			dojo.debug("files still in flight!");
			return;
		}
		if(typeof setTimeout == "object"){
			setTimeout("dojo.hostenv.loaded();", 0);
		}else{
			dojo.hostenv.loaded();
		}
	}
}

dojo.hostenv.moduleLoaded = function(modulename){
	var modref = dojo.evalObjPath((modulename.split(".").slice(0, -1)).join('.'));
	this.loaded_modules_[(new String(modulename)).toLowerCase()] = modref;
}

/**
* loadModule("A.B") first checks to see if symbol A.B is defined. 
* If it is, it is simply returned (nothing to do).
*
* If it is not defined, it will look for "A/B.js" in the script root directory,
* followed by "A.js".
*
* It throws if it cannot find a file to load, or if the symbol A.B is not
* defined after loading.
*
* It returns the object A.B.
*
* This does nothing about importing symbols into the current package.
* It is presumed that the caller will take care of that. For example, to import
* all symbols:
*
*    with (dojo.hostenv.loadModule("A.B")) {
*       ...
*    }
*
* And to import just the leaf symbol:
*
*    var B = dojo.hostenv.loadModule("A.B");
*    ...
*
* dj_load is an alias for dojo.hostenv.loadModule
*/
dojo.hostenv._global_omit_module_check = false;
dojo.hostenv.loadModule = function(modulename, exact_only, omit_module_check){
	if(!modulename){ return; }
	omit_module_check = this._global_omit_module_check || omit_module_check;
	var module = this.findModule(modulename, false);
	if(module){
		return module;
	}

	// protect against infinite recursion from mutual dependencies
	if(dj_undef(modulename, this.loading_modules_)){
		this.addedToLoadingCount.push(modulename);
	}
	this.loading_modules_[modulename] = 1;

	// convert periods to slashes
	var relpath = modulename.replace(/\./g, '/') + '.js';

	var syms = modulename.split(".");
	var nsyms = modulename.split(".");
	for (var i = syms.length - 1; i > 0; i--) {
		var parentModule = syms.slice(0, i).join(".");
		var parentModulePath = this.getModulePrefix(parentModule);
		if (parentModulePath != parentModule) {
			syms.splice(0, i, parentModulePath);
			break;
		}
	}
	var last = syms[syms.length - 1];
	// figure out if we're looking for a full package, if so, we want to do
	// things slightly diffrently
	if(last=="*"){
		modulename = (nsyms.slice(0, -1)).join('.');

		while(syms.length){
			syms.pop();
			syms.push(this.pkgFileName);
			relpath = syms.join("/") + '.js';
			if(relpath.charAt(0)=="/"){
				relpath = relpath.slice(1);
			}
			ok = this.loadPath(relpath, ((!omit_module_check) ? modulename : null));
			if(ok){ break; }
			syms.pop();
		}
	}else{
		relpath = syms.join("/") + '.js';
		modulename = nsyms.join('.');
		var ok = this.loadPath(relpath, ((!omit_module_check) ? modulename : null));
		if((!ok)&&(!exact_only)){
			syms.pop();
			while(syms.length){
				relpath = syms.join('/') + '.js';
				ok = this.loadPath(relpath, ((!omit_module_check) ? modulename : null));
				if(ok){ break; }
				syms.pop();
				relpath = syms.join('/') + '/'+this.pkgFileName+'.js';
				if(relpath.charAt(0)=="/"){
					relpath = relpath.slice(1);
				}
				ok = this.loadPath(relpath, ((!omit_module_check) ? modulename : null));
				if(ok){ break; }
			}
		}

		if((!ok)&&(!omit_module_check)){
			dojo.raise("Could not load '" + modulename + "'; last tried '" + relpath + "'");
		}
	}

	// check that the symbol was defined
	if(!omit_module_check){
		// pass in false so we can give better error
		module = this.findModule(modulename, false);
		if(!module){
			dojo.raise("symbol '" + modulename + "' is not defined after loading '" + relpath + "'"); 
		}
	}

	return module;
}

/**
* startPackage("A.B") follows the path, and at each level creates a new empty
* object or uses what already exists. It returns the result.
*/
dojo.hostenv.startPackage = function(packname){
	var syms = packname.split(/\./);
	if(syms[syms.length-1]=="*"){
		syms.pop();
	}
	return dojo.evalObjPath(syms.join("."), true);
}

/**
 * findModule("A.B") returns the object A.B if it exists, otherwise null.
 * @param modulename A string like 'A.B'.
 * @param must_exist Optional, defualt false. throw instead of returning null
 * if the module does not currently exist.
 */
dojo.hostenv.findModule = function(modulename, must_exist) {
	// check cache
	/*
	if(!dj_undef(modulename, this.modules_)){
		return this.modules_[modulename];
	}
	*/

	var lmn = (new String(modulename)).toLowerCase();

	if(this.loaded_modules_[lmn]){
		return this.loaded_modules_[lmn];
	}

	// see if symbol is defined anyway
	var module = dojo.evalObjPath(modulename);
	if((modulename)&&(typeof module != 'undefined')&&(module)){
		this.loaded_modules_[lmn] = module;
		return module;
	}

	if(must_exist){
		dojo.raise("no loaded module named '" + modulename + "'");
	}
	return null;
}

/**
* @file hostenv_browser.js
*
* Implements the hostenv interface for a browser environment.
*
* Perhaps it could be called a "dom" or "useragent" environment.
*
* @author Copyright 2004 Mark D. Anderson (mda@discerning.com)
* @author Licensed under the Academic Free License 2.1 http://www.opensource.org/licenses/afl-2.1.php
*/

// make jsc shut up (so we can use jsc to sanity check the code even if it will never run it).
/*@cc_on
@if (@_jscript_version >= 7)
var window; var XMLHttpRequest;
@end
@*/

if(typeof window == 'undefined'){
	dojo.raise("no window object");
}

// attempt to figure out the path to dojo if it isn't set in the config
(function() {
	// before we get any further with the config options, try to pick them out
	// of the URL. Most of this code is from NW
	if(djConfig.allowQueryConfig){
		var baseUrl = document.location.toString(); // FIXME: use location.query instead?
		var params = baseUrl.split("?", 2);
		if(params.length > 1){
			var paramStr = params[1];
			var pairs = paramStr.split("&");
			for(var x in pairs){
				var sp = pairs[x].split("=");
				// FIXME: is this eval dangerous?
				if((sp[0].length > 9)&&(sp[0].substr(0, 9) == "djConfig.")){
					var opt = sp[0].substr(9);
					try{
						djConfig[opt]=eval(sp[1]);
					}catch(e){
						djConfig[opt]=sp[1];
					}
				}
			}
		}
	}

	if(((djConfig["baseScriptUri"] == "")||(djConfig["baseRelativePath"] == "")) &&(document && document.getElementsByTagName)){
		var scripts = document.getElementsByTagName("script");
		var rePkg = /(__package__|dojo|bootstrap1)\.js([\?\.]|$)/i;
		for(var i = 0; i < scripts.length; i++) {
			var src = scripts[i].getAttribute("src");
			if(!src) { continue; }
			var m = src.match(rePkg);
			if(m) {
				root = src.substring(0, m.index);
				if(src.indexOf("bootstrap1") > -1) { root += "../"; }
				if(!this["djConfig"]) { djConfig = {}; }
				if(djConfig["baseScriptUri"] == "") { djConfig["baseScriptUri"] = root; }
				if(djConfig["baseRelativePath"] == "") { djConfig["baseRelativePath"] = root; }
				break;
			}
		}
	}

	var dr = dojo.render;
	var drh = dojo.render.html;
	var drs = dojo.render.svg;
	var dua = drh.UA = navigator.userAgent;
	var dav = drh.AV = navigator.appVersion;
	var t = true;
	var f = false;
	drh.capable = t;
	drh.support.builtin = t;

	dr.ver = parseFloat(drh.AV);
	dr.os.mac = dav.indexOf("Macintosh") >= 0;
	dr.os.win = dav.indexOf("Windows") >= 0;
	// could also be Solaris or something, but it's the same browser
	dr.os.linux = dav.indexOf("X11") >= 0;

	drh.opera = dua.indexOf("Opera") >= 0;
	drh.khtml = (dav.indexOf("Konqueror") >= 0)||(dav.indexOf("Safari") >= 0);
	drh.safari = dav.indexOf("Safari") >= 0;
	var geckoPos = dua.indexOf("Gecko");
	drh.mozilla = drh.moz = (geckoPos >= 0)&&(!drh.khtml);
	if (drh.mozilla) {
		// gecko version is YYYYMMDD
		drh.geckoVersion = dua.substring(geckoPos + 6, geckoPos + 14);
	}
	drh.ie = (document.all)&&(!drh.opera);
	drh.ie50 = drh.ie && dav.indexOf("MSIE 5.0")>=0;
	drh.ie55 = drh.ie && dav.indexOf("MSIE 5.5")>=0;
	drh.ie60 = drh.ie && dav.indexOf("MSIE 6.0")>=0;

	dr.vml.capable=drh.ie;
	drs.capable = f;
	drs.support.plugin = f;
	drs.support.builtin = f;
	drs.adobe = f;
	if (document.implementation
		&& document.implementation.hasFeature
		&& document.implementation.hasFeature("org.w3c.dom.svg", "1.0")
	){
		drs.capable = t;
		drs.support.builtin = t;
		drs.support.plugin = f;
		drs.adobe = f;
	}else{
		//	check for ASVG
		if(navigator.mimeTypes && navigator.mimeTypes.length > 0){
			var result = navigator.mimeTypes["image/svg+xml"] ||
				navigator.mimeTypes["image/svg"] ||
				navigator.mimeTypes["image/svg-xml"];
			if (result){
				drs.adobe = result && result.enabledPlugin &&
					result.enabledPlugin.description &&
					(result.enabledPlugin.description.indexOf("Adobe") > -1);
				if(drs.adobe) {
					drs.capable = t;
					drs.support.plugin = t;
				}
			}
		}else if(drh.ie && dr.os.win){
			var result = f;
			try {
				var test = new ActiveXObject("Adobe.SVGCtl");
				result = t;
			} catch(e){}
			if (result){
				drs.capable = t;
				drs.support.plugin = t;
				drs.adobe = t;
			}
		}else{
			drs.capable = f;
			drs.support.plugin = f;
			drs.adobe = f;
		}
	}
})();

dojo.hostenv.startPackage("dojo.hostenv");

dojo.hostenv.name_ = 'browser';
dojo.hostenv.searchIds = [];

// These are in order of decreasing likelihood; this will change in time.
var DJ_XMLHTTP_PROGIDS = ['Msxml2.XMLHTTP', 'Microsoft.XMLHTTP', 'Msxml2.XMLHTTP.4.0'];

dojo.hostenv.getXmlhttpObject = function(){
    var http = null;
	var last_e = null;
	try{ http = new XMLHttpRequest(); }catch(e){}
    if(!http){
		for(var i=0; i<3; ++i){
			var progid = DJ_XMLHTTP_PROGIDS[i];
			try{
				http = new ActiveXObject(progid);
			}catch(e){
				last_e = e;
			}

			if(http){
				DJ_XMLHTTP_PROGIDS = [progid];  // so faster next time
				break;
			}
		}

		/*if(http && !http.toString) {
			http.toString = function() { "[object XMLHttpRequest]"; }
		}*/
	}

	if(!http){
		return dojo.raise("XMLHTTP not available", last_e);
	}

	return http;
}

/**
 * Read the contents of the specified uri and return those contents.
 *
 * @param uri A relative or absolute uri. If absolute, it still must be in the
 * same "domain" as we are.
 *
 * @param async_cb If not specified, load synchronously. If specified, load
 * asynchronously, and use async_cb as the progress handler which takes the
 * xmlhttp object as its argument. If async_cb, this function returns null.
 *
 * @param fail_ok Default false. If fail_ok and !async_cb and loading fails,
 * return null instead of throwing.
 */
dojo.hostenv.getText = function(uri, async_cb, fail_ok){

	var http = this.getXmlhttpObject();

	if(async_cb){
		http.onreadystatechange = function(){
			if((4==http.readyState)&&(http["status"])){
				if(http.status==200){
					// dojo.debug("LOADED URI: "+uri);
					async_cb(http.responseText);
				}
			}
		}
	}

	http.open('GET', uri, async_cb ? true : false);
	try {
		http.send(null);
	} catch (e) {
		if (fail_ok && !async_cb) {
			return null;
		} else {
			throw e;
		}
	}
	if(async_cb){
		return null;
	}

	return http.responseText;
}

/*
 * It turns out that if we check *right now*, as this script file is being loaded,
 * then the last script element in the window DOM is ourselves.
 * That is because any subsequent script elements haven't shown up in the document
 * object yet.
 */
 /*
function dj_last_script_src() {
    var scripts = window.document.getElementsByTagName('script');
    if(scripts.length < 1){
		dojo.raise("No script elements in window.document, so can't figure out my script src");
	}
    var script = scripts[scripts.length - 1];
    var src = script.src;
    if(!src){
		dojo.raise("Last script element (out of " + scripts.length + ") has no src");
	}
    return src;
}

if(!dojo.hostenv["library_script_uri_"]){
	dojo.hostenv.library_script_uri_ = dj_last_script_src();
}
*/

dojo.hostenv.defaultDebugContainerId = 'dojoDebug';
dojo.hostenv._println_buffer = [];
dojo.hostenv._println_safe = false;
dojo.hostenv.println = function (line){
	if(!dojo.hostenv._println_safe){
		dojo.hostenv._println_buffer.push(line);
	}else{
		try {
			var console = document.getElementById(djConfig.debugContainerId ?
				djConfig.debugContainerId : dojo.hostenv.defaultDebugContainerId);
			if(!console) { console = document.getElementsByTagName("body")[0] || document.body; }

			var div = document.createElement("div");
			div.appendChild(document.createTextNode(line));
			console.appendChild(div);
		} catch (e) {
			try{
				// safari needs the output wrapped in an element for some reason
				document.write("<div>" + line + "</div>");
			}catch(e2){
				window.status = line;
			}
		}
	}
}

dojo.addOnLoad(function(){
	dojo.hostenv._println_safe = true;
	while(dojo.hostenv._println_buffer.length > 0){
		dojo.hostenv.println(dojo.hostenv._println_buffer.shift());
	}
});

function dj_addNodeEvtHdlr (node, evtName, fp, capture){
	var oldHandler = node["on"+evtName] || function(){};
	node["on"+evtName] = function(){
		fp.apply(node, arguments);
		oldHandler.apply(node, arguments);
	}
	return true;
}


// dj_load_init should be called right after DOM is loaded and parsed. Needs testing.

dj_load_init = function(){
	// allow multiple calls, only first one will take effect
   if (arguments.callee.initialized) return;
   arguments.callee.initialized = true;

   // perform initialization
	if(dojo.render.html.ie){
		dojo.hostenv.makeWidgets();
	}
	dojo.hostenv.modulesLoaded();
};
/*

// Mozilla exposes the event we could use
if (dojo.render.html.mozilla) {
   document.addEventListener("DOMContentLoaded", dj_load_init, null);
}

// for Internet Explorer. readyState will not be achieved on init call, but dojo doesn't need it
if (dojo.render.html.ie) {
   document.write("<script defer>dj_load_init()<"+"/script>");
}
*/

// default for other browsers
// potential TODO: apply setTimeout approach for other browsers
// that will cause flickering though ( document is loaded and THEN is processed)
// maybe show/hide required in this case..
// TODO: other browsers may support DOMContentLoaded/defer attribute. Add them to above.
dj_addNodeEvtHdlr(window, "load", dj_load_init);



dojo.hostenv.makeWidgets = function(){
	// you can put searchIds in djConfig and dojo.hostenv at the moment
	// we should probably eventually move to one or the other
	var sids = [];
	if(djConfig.searchIds && djConfig.searchIds.length > 0) {
		sids = sids.concat(djConfig.searchIds);
	}
	if(dojo.hostenv.searchIds && dojo.hostenv.searchIds.length > 0) {
		sids = sids.concat(dojo.hostenv.searchIds);
	}

	if((djConfig.parseWidgets)||(sids.length > 0)){
		if(dojo.evalObjPath("dojo.widget.Parse")){
			// we must do this on a delay to avoid:
			//	http://www.shaftek.org/blog/archives/000212.html
			// IE is such a tremendous peice of shit.
			try{
				var parser = new dojo.xml.Parse();
				if(sids.length > 0){
					for(var x=0; x<sids.length; x++){
						var tmpNode = document.getElementById(sids[x]);
						if(!tmpNode){ continue; }
						var frag = parser.parseElement(tmpNode, null, true);
						dojo.widget.getParser().createComponents(frag);
					}
				}else if(djConfig.parseWidgets){
					var frag  = parser.parseElement(document.getElementsByTagName("body")[0] || document.body, null, true);
					dojo.widget.getParser().createComponents(frag);
				}
			}catch(e){
				dojo.debug("auto-build-widgets error:", e);
			}
		}
	}
}

dojo.hostenv.modulesLoadedListeners.push(function(){
	if(!dojo.render.html.ie) {
		dojo.hostenv.makeWidgets();
	}
});

try {
	if (dojo.render.html.ie) {
		//	easier and safer VML addition.  Thanks Emil!
		document.namespaces.add("v", "urn:schemas-microsoft-com:vml");
		document.createStyleSheet().addRule("v\\:*", "behavior:url(#default#VML)");
	}
} catch (e) { }

// stub, over-ridden by debugging code. This will at least keep us from
// breaking when it's not included
dojo.hostenv.writeIncludes = function(){}

dojo.hostenv.byId = dojo.byId = function(id, doc){
	if(id && (typeof id == "string" || id instanceof String)){
		if(!doc){ doc = document; }
		return doc.getElementById(id);
	}
	return id; // assume it's a node
}

dojo.hostenv.byIdArray = dojo.byIdArray = function(){
	var ids = [];
	for(var i = 0; i < arguments.length; i++){
		if((arguments[i] instanceof Array)||(typeof arguments[i] == "array")){
			for(var j = 0; j < arguments[i].length; j++){
				ids = ids.concat(dojo.hostenv.byIdArray(arguments[i][j]));
			}
		}else{
			ids.push(dojo.hostenv.byId(arguments[i]));
		}
	}
	return ids;
}

/*
 * bootstrap2.js - runs after the hostenv_*.js file.
 */

/*
 * This method taks a "map" of arrays which one can use to optionally load dojo
 * modules. The map is indexed by the possible dojo.hostenv.name_ values, with
 * two additional values: "default" and "common". The items in the "default"
 * array will be loaded if none of the other items have been choosen based on
 * the hostenv.name_ item. The items in the "common" array will _always_ be
 * loaded, regardless of which list is chosen.  Here's how it's normally
 * called:
 *
 *	dojo.hostenv.conditionalLoadModule({
 *		browser: [
 *			["foo.bar.baz", true, true], // an example that passes multiple args to loadModule()
 *			"foo.sample.*",
 *			"foo.test,
 *		],
 *		default: [ "foo.sample.*" ],
 *		common: [ "really.important.module.*" ]
 *	});
 */
dojo.hostenv.conditionalLoadModule = function(modMap){
	var common = modMap["common"]||[];
	var result = (modMap[dojo.hostenv.name_]) ? common.concat(modMap[dojo.hostenv.name_]||[]) : common.concat(modMap["default"]||[]);

	for(var x=0; x<result.length; x++){
		var curr = result[x];
		if(curr.constructor == Array){
			dojo.hostenv.loadModule.apply(dojo.hostenv, curr);
		}else{
			dojo.hostenv.loadModule(curr);
		}
	}
}

dojo.hostenv.require = dojo.hostenv.loadModule;
dojo.require = function(){
	dojo.hostenv.loadModule.apply(dojo.hostenv, arguments);
}
dojo.requireAfter = dojo.require;

dojo.requireIf = function(){
	if((arguments[0] === true)||(arguments[0]=="common")||(dojo.render[arguments[0]].capable)){
		var args = [];
		for (var i = 1; i < arguments.length; i++) { args.push(arguments[i]); }
		dojo.require.apply(dojo, args);
	}
}

dojo.requireAfterIf = dojo.requireIf;
dojo.conditionalRequire = dojo.requireIf;

dojo.requireAll = function() {
	for(var i = 0; i < arguments.length; i++) { dojo.require(arguments[i]); }
}

dojo.kwCompoundRequire = function(){
	dojo.hostenv.conditionalLoadModule.apply(dojo.hostenv, arguments);
}

dojo.hostenv.provide = dojo.hostenv.startPackage;
dojo.provide = function(){
	return dojo.hostenv.startPackage.apply(dojo.hostenv, arguments);
}

dojo.setModulePrefix = function(module, prefix){
	return dojo.hostenv.setModulePrefix(module, prefix);
}

// stub
dojo.profile = { start: function(){}, end: function(){}, stop: function(){}, dump: function(){} };

// determine if an object supports a given method
// useful for longer api chains where you have to test each object in the chain
dojo.exists = function(obj, name){
	var p = name.split(".");
	for(var i = 0; i < p.length; i++){
	if(!(obj[p[i]])) return false;
		obj = obj[p[i]];
	}
	return true;
}

dojo.provide("dojo.string.common");

dojo.require("dojo.string");

/**
 * Trim whitespace from 'str'. If 'wh' > 0,
 * only trim from start, if 'wh' < 0, only trim
 * from end, otherwise trim both ends
 */
dojo.string.trim = function(str, wh){
	if(!str.replace){ return str; }
	if(!str.length){ return str; }
	var re = (wh > 0) ? (/^\s+/) : (wh < 0) ? (/\s+$/) : (/^\s+|\s+$/g);
	return str.replace(re, "");
}

/**
 * Trim whitespace at the beginning of 'str'
 */
dojo.string.trimStart = function(str) {
	return dojo.string.trim(str, 1);
}

/**
 * Trim whitespace at the end of 'str'
 */
dojo.string.trimEnd = function(str) {
	return dojo.string.trim(str, -1);
}

/**
 * Return 'str' repeated 'count' times, optionally
 * placing 'separator' between each rep
 */
dojo.string.repeat = function(str, count, separator) {
	var out = "";
	for(var i = 0; i < count; i++) {
		out += str;
		if(separator && i < count - 1) {
			out += separator;
		}
	}
	return out;
}

/**
 * Pad 'str' to guarantee that it is at least 'len' length
 * with the character 'c' at either the start (dir=1) or
 * end (dir=-1) of the string
 */
dojo.string.pad = function(str, len/*=2*/, c/*='0'*/, dir/*=1*/) {
	var out = String(str);
	if(!c) {
		c = '0';
	}
	if(!dir) {
		dir = 1;
	}
	while(out.length < len) {
		if(dir > 0) {
			out = c + out;
		} else {
			out += c;
		}
	}
	return out;
}

/** same as dojo.string.pad(str, len, c, 1) */
dojo.string.padLeft = function(str, len, c) {
	return dojo.string.pad(str, len, c, 1);
}

/** same as dojo.string.pad(str, len, c, -1) */
dojo.string.padRight = function(str, len, c) {
	return dojo.string.pad(str, len, c, -1);
}

dojo.provide("dojo.string");
dojo.require("dojo.string.common");

dojo.provide("dojo.lang.common");

dojo.require("dojo.lang");

dojo.lang.mixin = function(obj, props){
	var tobj = {};
	for(var x in props){
		if(typeof tobj[x] == "undefined" || tobj[x] != props[x]) {
			obj[x] = props[x];
		}
	}
	// IE doesn't recognize custom toStrings in for..in
	if(dojo.render.html.ie && dojo.lang.isFunction(props["toString"]) && props["toString"] != obj["toString"]) {
		obj.toString = props.toString;
	}
	return obj;
}

dojo.lang.extend = function(ctor, props){
	this.mixin(ctor.prototype, props);
}

/**
 * See if val is in arr. Call signatures:
 *  find(array, value, identity) // recommended
 *  find(value, array, identity)
**/
dojo.lang.find = function(arr, val, identity, findLast){
	// support both (arr, val) and (val, arr)
	if(!dojo.lang.isArrayLike(arr) && dojo.lang.isArrayLike(val)) {
		var a = arr;
		arr = val;
		val = a;
	}
	var isString = dojo.lang.isString(arr);
	if(isString) { arr = arr.split(""); }

	if(findLast) {
		var step = -1;
		var i = arr.length - 1;
		var end = -1;
	} else {
		var step = 1;
		var i = 0;
		var end = arr.length;
	}
	if(identity){
		while(i != end) {
			if(arr[i] === val){ return i; }
			i += step;
		}
	}else{
		while(i != end) {
			if(arr[i] == val){ return i; }
			i += step;
		}
	}
	return -1;
}

dojo.lang.indexOf = dojo.lang.find;

dojo.lang.findLast = function(arr, val, identity) {
	return dojo.lang.find(arr, val, identity, true);
}

dojo.lang.lastIndexOf = dojo.lang.findLast;

dojo.lang.inArray = function(arr, val){
	return dojo.lang.find(arr, val) > -1;
}

/**
 * Partial implmentation of is* functions from
 * http://www.crockford.com/javascript/recommend.html
 * NOTE: some of these may not be the best thing to use in all situations
 * as they aren't part of core JS and therefore can't work in every case.
 * See WARNING messages inline for tips.
 *
 * The following is* functions are fairly "safe"
 */

dojo.lang.isObject = function(wh) {
	return typeof wh == "object" || dojo.lang.isArray(wh) || dojo.lang.isFunction(wh);
}

dojo.lang.isArray = function(wh) {
	return (wh instanceof Array || typeof wh == "array");
}

dojo.lang.isArrayLike = function(wh) {
	if(dojo.lang.isString(wh)){ return false; }
	if(dojo.lang.isFunction(wh)){ return false; } // keeps out built-in ctors (Number, String, ...) which have length properties
	if(dojo.lang.isArray(wh)){ return true; }
	if(typeof wh != "undefined" && wh
		&& dojo.lang.isNumber(wh.length) && isFinite(wh.length)){ return true; }
	return false;
}

dojo.lang.isFunction = function(wh) {
	return (wh instanceof Function || typeof wh == "function");
}

dojo.lang.isString = function(wh) {
	return (wh instanceof String || typeof wh == "string");
}

dojo.lang.isAlien = function(wh) {
	return !dojo.lang.isFunction() && /\{\s*\[native code\]\s*\}/.test(String(wh));
}

dojo.lang.isBoolean = function(wh) {
	return (wh instanceof Boolean || typeof wh == "boolean");
}

/**
 * The following is***() functions are somewhat "unsafe". Fortunately,
 * there are workarounds the the language provides and are mentioned
 * in the WARNING messages.
 *
 * WARNING: In most cases, isNaN(wh) is sufficient to determine whether or not
 * something is a number or can be used as such. For example, a number or string
 * can be used interchangably when accessing array items (arr["1"] is the same as
 * arr[1]) and isNaN will return false for both values ("1" and 1). Should you
 * use isNumber("1"), that will return false, which is generally not too useful.
 * Also, isNumber(NaN) returns true, again, this isn't generally useful, but there
 * are corner cases (like when you want to make sure that two things are really
 * the same type of thing). That is really where isNumber "shines".
 *
 * RECOMMENDATION: Use isNaN(wh) when possible
 */
dojo.lang.isNumber = function(wh) {
	return (wh instanceof Number || typeof wh == "number");
}

/**
 * WARNING: In some cases, isUndefined will not behave as you
 * might expect. If you do isUndefined(foo) and there is no earlier
 * reference to foo, an error will be thrown before isUndefined is
 * called. It behaves correctly if you scope yor object first, i.e.
 * isUndefined(foo.bar) where foo is an object and bar isn't a
 * property of the object.
 *
 * RECOMMENDATION: Use `typeof foo == "undefined"` when possible
 *
 * FIXME: Should isUndefined go away since it is error prone?
 */
dojo.lang.isUndefined = function(wh) {
	return ((wh == undefined)&&(typeof wh == "undefined"));
}

// end Crockford functions

dojo.provide("dojo.lang.type");

dojo.require("dojo.lang.common");

dojo.lang.whatAmI = function(wh) {
	try {
		if(dojo.lang.isArray(wh)) { return "array"; }
		if(dojo.lang.isFunction(wh)) { return "function"; }
		if(dojo.lang.isString(wh)) { return "string"; }
		if(dojo.lang.isNumber(wh)) { return "number"; }
		if(dojo.lang.isBoolean(wh)) { return "boolean"; }
		if(dojo.lang.isAlien(wh)) { return "alien"; }
		if(dojo.lang.isUndefined(wh)) { return "undefined"; }
		// FIXME: should this go first?
		for(var name in dojo.lang.whatAmI.custom) {
			if(dojo.lang.whatAmI.custom[name](wh)) {
				return name;
			}
		}
		if(dojo.lang.isObject(wh)) { return "object"; }
	} catch(E) {}
	return "unknown";
}
/*
 * dojo.lang.whatAmI.custom[typeName] = someFunction
 * will return typeName is someFunction(wh) returns true
 */
dojo.lang.whatAmI.custom = {};

/**
 * Returns true for values that commonly represent numbers.
 *
 * Examples:
 * <pre>
 *   dojo.lang.isNumeric(3);                 // returns true
 *   dojo.lang.isNumeric("3");               // returns true
 *   dojo.lang.isNumeric(new Number(3));     // returns true
 *   dojo.lang.isNumeric(new String("3"));   // returns true
 *
 *   dojo.lang.isNumeric(3/0);               // returns false
 *   dojo.lang.isNumeric("foo");             // returns false
 *   dojo.lang.isNumeric(new Number("foo")); // returns false
 *   dojo.lang.isNumeric(false);             // returns false
 *   dojo.lang.isNumeric(true);              // returns false
 * </pre>
 */
dojo.lang.isNumeric = function(wh){
	return (!isNaN(wh) && isFinite(wh) && (wh != null) &&
			!dojo.lang.isBoolean(wh) && !dojo.lang.isArray(wh));
}

/**
 * Returns true for any literal, and for any object that is an 
 * instance of a built-in type like String, Number, Boolean, 
 * Array, Function, or Error.
 */
dojo.lang.isBuiltIn = function(wh){
	return (dojo.lang.isArray(wh)		|| 
			dojo.lang.isFunction(wh)	|| 
			dojo.lang.isString(wh)		|| 
			dojo.lang.isNumber(wh)		|| 
			dojo.lang.isBoolean(wh)		|| 
			(wh == null)				|| 
			(wh instanceof Error)		|| 
			(typeof wh == "error") );
}

/**
 * Returns true for any object where the value of the 
 * property 'constructor' is 'Object'.  
 * 
 * Examples:
 * <pre>
 *   dojo.lang.isPureObject(new Object()); // returns true
 *   dojo.lang.isPureObject({a: 1, b: 2}); // returns true
 * 
 *   dojo.lang.isPureObject(new Date());   // returns false
 *   dojo.lang.isPureObject([11, 2, 3]);   // returns false
 * </pre>
 */
dojo.lang.isPureObject = function(wh){
	return ((wh != null) && dojo.lang.isObject(wh) && wh.constructor == Object);
}

/**
 * Given a value and a datatype, this method returns true if the
 * type of the value matches the datatype. The datatype parameter
 * can be an array of datatypes, in which case the method returns
 * true if the type of the value matches any of the datatypes.
 *
 * Examples:
 * <pre>
 *   dojo.lang.isOfType("foo", String);                // returns true
 *   dojo.lang.isOfType(12345, Number);                // returns true
 *   dojo.lang.isOfType(false, Boolean);               // returns true
 *   dojo.lang.isOfType([6, 8], Array);                // returns true
 *   dojo.lang.isOfType(dojo.lang.isOfType, Function); // returns true
 *   dojo.lang.isOfType({foo: "bar"}, Object);         // returns true
 *   dojo.lang.isOfType(new Date(), Date);             // returns true
 *   dojo.lang.isOfType(xxxxx, Date);                  // returns true
 *
 *   dojo.lang.isOfType("foo", "string");                // returns true
 *   dojo.lang.isOfType(12345, "number");                // returns true
 *   dojo.lang.isOfType(false, "boolean");               // returns true
 *   dojo.lang.isOfType([6, 8], "array");                // returns true
 *   dojo.lang.isOfType(dojo.lang.isOfType, "function"); // returns true
 *   dojo.lang.isOfType({foo: "bar"}, "object");         // returns true
 *   dojo.lang.isOfType(xxxxx, "undefined");             // returns true
 *   dojo.lang.isOfType(null, "null");                   // returns true

 *   dojo.lang.isOfType("foo", [Number, String, Boolean]); // returns true
 *   dojo.lang.isOfType(12345, [Number, String, Boolean]); // returns true
 *   dojo.lang.isOfType(false, [Number, String, Boolean]); // returns true
 *   dojo.lang.isOfType(xxxxx, "undefined");               // returns true
 * </pre>
 *
 * @param	value	Any literal value or object instance.
 * @param	type	A class of object, or a literal type, or the string name of a type, or an array with a list of types.
 * @return	Returns a boolean
 */
dojo.lang.isOfType = function(value, type) {
	if(dojo.lang.isArray(type)){
		var arrayOfTypes = type;
		for(var i in arrayOfTypes){
			var aType = arrayOfTypes[i];
			if(dojo.lang.isOfType(value, aType)) {
				return true;
			}
		}
		return false;
	}else{
		if(dojo.lang.isString(type)){
			type = type.toLowerCase();
		}
		switch (type) {
			case Array:
			case "array":
				return dojo.lang.isArray(value);
				break;
			case Function:
			case "function":
				return dojo.lang.isFunction(value);
				break;
			case String:
			case "string":
				return dojo.lang.isString(value);
				break;
			case Number:
			case "number":
				return dojo.lang.isNumber(value);
				break;
			case "numeric":
				return dojo.lang.isNumeric(value);
				break;
			case Boolean:
			case "boolean":
				return dojo.lang.isBoolean(value);
				break;
			case Object:
			case "object":
				return dojo.lang.isObject(value);
				break;
			case "pureobject":
				return dojo.lang.isPureObject(value);
				break;
			case "builtin":
				return dojo.lang.isBuiltIn(value);
				break;
			case "alien":
				return dojo.lang.isAlien(value);
				break;
			case "undefined":
				return dojo.lang.isUndefined(value);
				break;
			case null:
			case "null":
				return (value === null);
				break;
			case "optional":
				return ((value === null) || dojo.lang.isUndefined(value));
				break;
			default:
				if (dojo.lang.isFunction(type)) {
					return (value instanceof type);
				} else {
					dojo.raise("dojo.lang.isOfType() was passed an invalid type");
				}
				break;
		}
	}
	dojo.raise("If we get here, it means a bug was introduced above.");
}

/*
 * 	From reflection code, part of merge.
 *	TRT 2006-02-01
 */
dojo.lang.getObject=function(/* String */ str){
	//	summary
	//	Will return an object, if it exists, based on the name in the passed string.
	var parts=str.split("."), i=0, obj=dj_global; 
	do{ 
		obj=obj[parts[i++]]; 
	}while(i<parts.length&&obj); 
	return (obj!=dj_global)?obj:null;	//	Object
}

dojo.lang.doesObjectExist=function(/* String */ str){
	//	summary
	//	Check to see if object [str] exists, based on the passed string.
	var parts=str.split("."), i=0, obj=dj_global; 
	do{ 
		obj=obj[parts[i++]]; 
	}while(i<parts.length&&obj); 
	return (obj&&obj!=dj_global);	//	boolean
}

dojo.lang.getConstructor=function(/* object */ obj){
	//	summary
	//	Returns a reference to the passed object's constructor function.
	return obj.constructor;	//	Function
}

dojo.lang.isConstructedBy=function(/* object */ obj, /* function */ type){
	//	summary
	//	Returns a boolean if the passed [obj] was created by [type]
	return dojo.lang.getConstructor(obj)==type;	//	boolean
}

dojo.lang.isSubOf=function(/* object */ obj, /* object */ type){
	//	summary
	//	Test to see if type is in obj's prototype chain
	return obj instanceof type; 	// boolean
}

dojo.lang.isBaseOf=function(/* object */ type, /* object */ obj){
	//	summary
	//	Test to see if obj is in type's prototype chain
	return obj instanceof type; 	// boolean
}

//	TODO: pass the constructor an arguments object.
dojo.lang.createInstance=function(/* string */ type){
	//	summary
	//	create an instance of type.
	var o=null;
	var f=type;
	if(typeof(f)=="string"){
		f=dojo.lang.getObject(type);
	}
	if(typeof(f)=="function"){
		try{
			o=new f();
		}catch(e){
		}
	}
	return o;	//	object
}

dojo.provide("dojo.lang.extras");

dojo.require("dojo.lang.common");
dojo.require("dojo.lang.type");

/**
 * Sets a timeout in milliseconds to execute a function in a given context
 * with optional arguments.
 *
 * setTimeout (Object context, function func, number delay[, arg1[, ...]]);
 * setTimeout (function func, number delay[, arg1[, ...]]);
 */
dojo.lang.setTimeout = function(func, delay){
	var context = window, argsStart = 2;
	if(!dojo.lang.isFunction(func)){
		context = func;
		func = delay;
		delay = arguments[2];
		argsStart++;
	}

	if(dojo.lang.isString(func)){
		func = context[func];
	}
	
	var args = [];
	for (var i = argsStart; i < arguments.length; i++) {
		args.push(arguments[i]);
	}
	return setTimeout(function () { func.apply(context, args); }, delay);
}

dojo.lang.getNameInObj = function(ns, item){
	if(!ns){ ns = dj_global; }

	for(var x in ns){
		if(ns[x] === item){
			return new String(x);
		}
	}
	return null;
}

dojo.lang.shallowCopy = function(obj) {
	var ret = {}, key;
	for(key in obj) {
		if(dojo.lang.isUndefined(ret[key])) {
			ret[key] = obj[key];
		}
	}
	return ret;
}

/**
 * Return the first argument that isn't undefined
 */
dojo.lang.firstValued = function(/* ... */) {
	for(var i = 0; i < arguments.length; i++) {
		if(typeof arguments[i] != "undefined") {
			return arguments[i];
		}
	}
	return undefined;
}

dojo.provide("dojo.io.IO");
dojo.require("dojo.string");
dojo.require("dojo.lang.extras");

/******************************************************************************
 *	Notes about dojo.io design:
 *	
 *	The dojo.io.* package has the unenviable task of making a lot of different
 *	types of I/O feel natural, despite a universal lack of good (or even
 *	reasonable!) I/O capability in the host environment. So lets pin this down
 *	a little bit further.
 *
 *	Rhino:
 *		perhaps the best situation anywhere. Access to Java classes allows you
 *		to do anything one might want in terms of I/O, both synchronously and
 *		async. Can open TCP sockets and perform low-latency client/server
 *		interactions. HTTP transport is available through Java HTTP client and
 *		server classes. Wish it were always this easy.
 *
 *	xpcshell:
 *		XPCOM for I/O. A cluster-fuck to be sure.
 *
 *	spidermonkey:
 *		S.O.L.
 *
 *	Browsers:
 *		Browsers generally do not provide any useable filesystem access. We are
 *		therefore limited to HTTP for moving information to and from Dojo
 *		instances living in a browser.
 *
 *		XMLHTTP:
 *			Sync or async, allows reading of arbitrary text files (including
 *			JS, which can then be eval()'d), writing requires server
 *			cooperation and is limited to HTTP mechanisms (POST and GET).
 *
 *		<iframe> hacks:
 *			iframe document hacks allow browsers to communicate asynchronously
 *			with a server via HTTP POST and GET operations. With significant
 *			effort and server cooperation, low-latency data transit between
 *			client and server can be acheived via iframe mechanisms (repubsub).
 *
 *		SVG:
 *			Adobe's SVG viewer implements helpful primitives for XML-based
 *			requests, but receipt of arbitrary text data seems unlikely w/o
 *			<![CDATA[]]> sections.
 *
 *
 *	A discussion between Dylan, Mark, Tom, and Alex helped to lay down a lot
 *	the IO API interface. A transcript of it can be found at:
 *		http://dojotoolkit.org/viewcvs/viewcvs.py/documents/irc/irc_io_api_log.txt?rev=307&view=auto
 *	
 *	Also referenced in the design of the API was the DOM 3 L&S spec:
 *		http://www.w3.org/TR/2004/REC-DOM-Level-3-LS-20040407/load-save.html
 ******************************************************************************/

// a map of the available transport options. Transports should add themselves
// by calling add(name)
dojo.io.transports = [];
dojo.io.hdlrFuncNames = [ "load", "error", "timeout" ]; // we're omitting a progress() event for now

dojo.io.Request = function(url, mimetype, transport, changeUrl){
	if((arguments.length == 1)&&(arguments[0].constructor == Object)){
		this.fromKwArgs(arguments[0]);
	}else{
		this.url = url;
		if(mimetype){ this.mimetype = mimetype; }
		if(transport){ this.transport = transport; }
		if(arguments.length >= 4){ this.changeUrl = changeUrl; }
	}
}

dojo.lang.extend(dojo.io.Request, {

	/** The URL to hit */
	url: "",
	
	/** The mime type used to interrpret the response body */
	mimetype: "text/plain",
	
	/** The HTTP method to use */
	method: "GET",
	
	/** An Object containing key-value pairs to be included with the request */
	content: undefined, // Object
	
	/** The transport medium to use */
	transport: undefined, // String
	
	/** If defined the URL of the page is physically changed */
	changeUrl: undefined, // String
	
	/** A form node to use in the request */
	formNode: undefined, // HTMLFormElement
	
	/** Whether the request should be made synchronously */
	sync: false,
	
	bindSuccess: false,

	/** Cache/look for the request in the cache before attempting to request?
	 *  NOTE: this isn't a browser cache, this is internal and would only cache in-page
	 */
	useCache: false,

	/** Prevent the browser from caching this by adding a query string argument to the URL */
	preventCache: false,
	
	// events stuff
	load: function(type, data, evt){ },
	error: function(type, error){ },
	timeout: function(type){ },
	handle: function(){ },

	//FIXME: change BrowserIO.js to use timeouts? IframeIO?
	// The number of seconds to wait until firing a timeout callback.
	// If it is zero, that means, don't do a timeout check.
	timeoutSeconds: 0,
	
	// the abort method needs to be filled in by the transport that accepts the
	// bind() request
	abort: function(){ },
	
	// backButton: function(){ },
	// forwardButton: function(){ },

	fromKwArgs: function(kwArgs){
		// normalize args
		if(kwArgs["url"]){ kwArgs.url = kwArgs.url.toString(); }
		if(kwArgs["formNode"]) { kwArgs.formNode = dojo.byId(kwArgs.formNode); }
		if(!kwArgs["method"] && kwArgs["formNode"] && kwArgs["formNode"].method) {
			kwArgs.method = kwArgs["formNode"].method;
		}
		
		// backwards compatibility
		if(!kwArgs["handle"] && kwArgs["handler"]){ kwArgs.handle = kwArgs.handler; }
		if(!kwArgs["load"] && kwArgs["loaded"]){ kwArgs.load = kwArgs.loaded; }
		if(!kwArgs["changeUrl"] && kwArgs["changeURL"]) { kwArgs.changeUrl = kwArgs.changeURL; }

		// encoding fun!
		kwArgs.encoding = dojo.lang.firstValued(kwArgs["encoding"], djConfig["bindEncoding"], "");

		kwArgs.sendTransport = dojo.lang.firstValued(kwArgs["sendTransport"], djConfig["ioSendTransport"], false);

		var isFunction = dojo.lang.isFunction;
		for(var x=0; x<dojo.io.hdlrFuncNames.length; x++){
			var fn = dojo.io.hdlrFuncNames[x];
			if(isFunction(kwArgs[fn])){ continue; }
			if(isFunction(kwArgs["handle"])){
				kwArgs[fn] = kwArgs.handle;
			}
			// handler is aliased above, shouldn't need this check
			/* else if(dojo.lang.isObject(kwArgs.handler)){
				if(isFunction(kwArgs.handler[fn])){
					kwArgs[fn] = kwArgs.handler[fn]||kwArgs.handler["handle"]||function(){};
				}
			}*/
		}
		dojo.lang.mixin(this, kwArgs);
	}

});

dojo.io.Error = function(msg, type, num){
	this.message = msg;
	this.type =  type || "unknown"; // must be one of "io", "parse", "unknown"
	this.number = num || 0; // per-substrate error number, not normalized
}

dojo.io.transports.addTransport = function(name){
	this.push(name);
	// FIXME: do we need to handle things that aren't direct children of the
	// dojo.io namespace? (say, dojo.io.foo.fooTransport?)
	this[name] = dojo.io[name];
}

// binding interface, the various implementations register their capabilities
// and the bind() method dispatches
dojo.io.bind = function(request){
	// if the request asks for a particular implementation, use it
	if(!(request instanceof dojo.io.Request)){
		try{
			request = new dojo.io.Request(request);
		}catch(e){ dojo.debug(e); }
	}
	var tsName = "";
	if(request["transport"]){
		tsName = request["transport"];
		// FIXME: it would be good to call the error handler, although we'd
		// need to use setTimeout or similar to accomplish this and we can't
		// garuntee that this facility is available.
		if(!this[tsName]){ return request; }
	}else{
		// otherwise we do our best to auto-detect what available transports
		// will handle 
		for(var x=0; x<dojo.io.transports.length; x++){
			var tmp = dojo.io.transports[x];
			if((this[tmp])&&(this[tmp].canHandle(request))){
				tsName = tmp;
			}
		}
		if(tsName == ""){ return request; }
	}
	this[tsName].bind(request);
	request.bindSuccess = true;
	return request;
}

dojo.io.queueBind = function(request){
	if(!(request instanceof dojo.io.Request)){
		try{
			request = new dojo.io.Request(request);
		}catch(e){ dojo.debug(e); }
	}

	// make sure we get called if/when we get a response
	var oldLoad = request.load;
	request.load = function(){
		dojo.io._queueBindInFlight = false;
		var ret = oldLoad.apply(this, arguments);
		dojo.io._dispatchNextQueueBind();
		return ret;
	}

	var oldErr = request.error;
	request.error = function(){
		dojo.io._queueBindInFlight = false;
		var ret = oldErr.apply(this, arguments);
		dojo.io._dispatchNextQueueBind();
		return ret;
	}

	dojo.io._bindQueue.push(request);
	dojo.io._dispatchNextQueueBind();
	return request;
}

dojo.io._dispatchNextQueueBind = function(){
	if(!dojo.io._queueBindInFlight){
		dojo.io._queueBindInFlight = true;
		if(dojo.io._bindQueue.length > 0){
			dojo.io.bind(dojo.io._bindQueue.shift());
		}else{
			dojo.io._queueBindInFlight = false;
		}
	}
}
dojo.io._bindQueue = [];
dojo.io._queueBindInFlight = false;

//FIXME: This seems to return a string with an extra & at the end. Is that the desired behavior?
//       ScriptSrcIO.js would prefer that it is not on the end, but it is not a critical thing.
dojo.io.argsFromMap = function(map, encoding){
	var control = new Object();
	var mapStr = "";
	var enc = /utf/i.test(encoding||"") ? encodeURIComponent : dojo.string.encodeAscii;
	for(var x in map){
		if(!control[x]){
			mapStr+= enc(x)+"="+enc(map[x])+"&";
		}
	}

	return mapStr;
}

dojo.io.setIFrameSrc = function(iframe, src, replace){
	try{
		var r = dojo.render.html;
		// dojo.debug(iframe);
		if(!replace){
			if(r.safari){
				iframe.location = src;
			}else{
				frames[iframe.name].location = src;
			}
		}else{
			// Fun with DOM 0 incompatibilities!
			var idoc;
			if(r.ie){
				idoc = iframe.contentWindow.document;
			}else if(r.moz){
				idoc = iframe.contentWindow;
			}else if(r.safari){
				idoc = iframe.document;
			}
			idoc.location.replace(src);
		}
	}catch(e){ 
		dojo.debug(e); 
		dojo.debug("setIFrameSrc: "+e); 
	}
}

/*
dojo.io.sampleTranport = new function(){
	this.canHandle = function(kwArgs){
		// canHandle just tells dojo.io.bind() if this is a good transport to
		// use for the particular type of request.
		if(	
			(
				(kwArgs["mimetype"] == "text/plain") ||
				(kwArgs["mimetype"] == "text/html") ||
				(kwArgs["mimetype"] == "text/javascript")
			)&&(
				(kwArgs["method"] == "get") ||
				( (kwArgs["method"] == "post") && (!kwArgs["formNode"]) )
			)
		){
			return true;
		}

		return false;
	}

	this.bind = function(kwArgs){
		var hdlrObj = {};

		// set up a handler object
		for(var x=0; x<dojo.io.hdlrFuncNames.length; x++){
			var fn = dojo.io.hdlrFuncNames[x];
			if(typeof kwArgs.handler == "object"){
				if(typeof kwArgs.handler[fn] == "function"){
					hdlrObj[fn] = kwArgs.handler[fn]||kwArgs.handler["handle"];
				}
			}else if(typeof kwArgs[fn] == "function"){
				hdlrObj[fn] = kwArgs[fn];
			}else{
				hdlrObj[fn] = kwArgs["handle"]||function(){};
			}
		}

		// build a handler function that calls back to the handler obj
		var hdlrFunc = function(evt){
			if(evt.type == "onload"){
				hdlrObj.load("load", evt.data, evt);
			}else if(evt.type == "onerr"){
				var errObj = new dojo.io.Error("sampleTransport Error: "+evt.msg);
				hdlrObj.error("error", errObj);
			}
		}

		// the sample transport would attach the hdlrFunc() when sending the
		// request down the pipe at this point
		var tgtURL = kwArgs.url+"?"+dojo.io.argsFromMap(kwArgs.content);
		// sampleTransport.sendRequest(tgtURL, hdlrFunc);
	}

	dojo.io.transports.addTransport("sampleTranport");
}
*/

dojo.provide("dojo.lang.array");

dojo.require("dojo.lang.common");

// FIXME: Is this worthless since you can do: if(name in obj)
// is this the right place for this?
dojo.lang.has = function(obj, name){
	return (typeof obj[name] !== 'undefined');
}

dojo.lang.isEmpty = function(obj) {
	if(dojo.lang.isObject(obj)) {
		var tmp = {};
		var count = 0;
		for(var x in obj){
			if(obj[x] && (!tmp[x])){
				count++;
				break;
			} 
		}
		return (count == 0);
	} else if(dojo.lang.isArrayLike(obj) || dojo.lang.isString(obj)) {
		return obj.length == 0;
	}
}

dojo.lang.forEach = function(arr, unary_func, fix_length){
	var isString = dojo.lang.isString(arr);
	if(isString) { arr = arr.split(""); }
	var il = arr.length;
	for(var i=0; i< ((fix_length) ? il : arr.length); i++){
		if(unary_func(arr[i], i, arr) == "break"){
			break;
		}
	}
}

dojo.lang.map = function(arr, obj, unary_func){
	var isString = dojo.lang.isString(arr);
	if(isString){
		arr = arr.split("");
	}
	if(dojo.lang.isFunction(obj)&&(!unary_func)){
		unary_func = obj;
		obj = dj_global;
	}else if(dojo.lang.isFunction(obj) && unary_func){
		// ff 1.5 compat
		var tmpObj = obj;
		obj = unary_func;
		unary_func = tmpObj;
	}

	if(Array.map){
	 	var outArr = Array.map(arr, unary_func, obj);
	}else{
		var outArr = [];
		for(var i=0;i<arr.length;++i){
			outArr.push(unary_func.call(obj, arr[i]));
		}
	}

	if(isString) {
		return outArr.join("");
	} else {
		return outArr;
	}
}

dojo.lang.every = function(arr, callback, thisObject) {
	var isString = dojo.lang.isString(arr);
	if(isString) { arr = arr.split(""); }
	if(Array.every) {
		return Array.every(arr, callback, thisObject);
	} else {
		if(!thisObject) {
			if(arguments.length >= 3) { dojo.raise("thisObject doesn't exist!"); }
			thisObject = dj_global;
		}

		for(var i = 0; i < arr.length; i++) {
			if(!callback.call(thisObject, arr[i], i, arr)) {
				return false;
			}
		}
		return true;
	}
}

dojo.lang.some = function(arr, callback, thisObject) {
	var isString = dojo.lang.isString(arr);
	if(isString) { arr = arr.split(""); }
	if(Array.some) {
		return Array.some(arr, callback, thisObject);
	} else {
		if(!thisObject) {
			if(arguments.length >= 3) { dojo.raise("thisObject doesn't exist!"); }
			thisObject = dj_global;
		}

		for(var i = 0; i < arr.length; i++) {
			if(callback.call(thisObject, arr[i], i, arr)) {
				return true;
			}
		}
		return false;
	}
}

dojo.lang.filter = function(arr, callback, thisObject) {
	var isString = dojo.lang.isString(arr);
	if(isString) { arr = arr.split(""); }
	if(Array.filter) {
		var outArr = Array.filter(arr, callback, thisObject);
	} else {
		if(!thisObject) {
			if(arguments.length >= 3) { dojo.raise("thisObject doesn't exist!"); }
			thisObject = dj_global;
		}

		var outArr = [];
		for(var i = 0; i < arr.length; i++) {
			if(callback.call(thisObject, arr[i], i, arr)) {
				outArr.push(arr[i]);
			}
		}
	}
	if(isString) {
		return outArr.join("");
	} else {
		return outArr;
	}
}

/**
 * Creates a 1-D array out of all the arguments passed,
 * unravelling any array-like objects in the process
 *
 * Ex:
 * unnest(1, 2, 3) ==> [1, 2, 3]
 * unnest(1, [2, [3], [[[4]]]]) ==> [1, 2, 3, 4]
 */
dojo.lang.unnest = function(/* ... */) {
	var out = [];
	for(var i = 0; i < arguments.length; i++) {
		if(dojo.lang.isArrayLike(arguments[i])) {
			var add = dojo.lang.unnest.apply(this, arguments[i]);
			out = out.concat(add);
		} else {
			out.push(arguments[i]);
		}
	}
	return out;
}

/**
 * Converts an array-like object (i.e. arguments, DOMCollection)
 * to an array
**/
dojo.lang.toArray = function(arrayLike, startOffset) {
	var array = [];
	for(var i = startOffset||0; i < arrayLike.length; i++) {
		array.push(arrayLike[i]);
	}
	return array;
}

dojo.provide("dojo.lang.func");

dojo.require("dojo.lang.common");
dojo.require("dojo.lang.type");

/**
 * Runs a function in a given scope (thisObject), can
 * also be used to preserve scope.
 *
 * hitch(foo, "bar"); // runs foo.bar() in the scope of foo
 * hitch(foo, myFunction); // runs myFunction in the scope of foo
 */
dojo.lang.hitch = function(thisObject, method) {
	if(dojo.lang.isString(method)) {
		var fcn = thisObject[method];
	} else {
		var fcn = method;
	}

	return function() {
		return fcn.apply(thisObject, arguments);
	}
}

dojo.lang.anonCtr = 0;
dojo.lang.anon = {};
dojo.lang.nameAnonFunc = function(anonFuncPtr, namespaceObj){
	var nso = (namespaceObj || dojo.lang.anon);
	if((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"] == true)){
		for(var x in nso){
			if(nso[x] === anonFuncPtr){
				return x;
			}
		}
	}
	var ret = "__"+dojo.lang.anonCtr++;
	while(typeof nso[ret] != "undefined"){
		ret = "__"+dojo.lang.anonCtr++;
	}
	nso[ret] = anonFuncPtr;
	return ret;
}

dojo.lang.forward = function(funcName){
	// Returns a function that forwards a method call to this.func(...)
	return function(){
		return this[funcName].apply(this, arguments);
	};
}

dojo.lang.curry = function(ns, func /* args ... */){
	var outerArgs = [];
	ns = ns||dj_global;
	if(dojo.lang.isString(func)){
		func = ns[func];
	}
	for(var x=2; x<arguments.length; x++){
		outerArgs.push(arguments[x]);
	}
	var ecount = func.length - outerArgs.length;
	// borrowed from svend tofte
	function gather(nextArgs, innerArgs, expected){
		var texpected = expected;
		var totalArgs = innerArgs.slice(0); // copy
		for(var x=0; x<nextArgs.length; x++){
			totalArgs.push(nextArgs[x]);
		}
		// check the list of provided nextArgs to see if it, plus the
		// number of innerArgs already supplied, meets the total
		// expected.
		expected = expected-nextArgs.length;
		if(expected<=0){
			var res = func.apply(ns, totalArgs);
			expected = texpected;
			return res;
		}else{
			return function(){
				return gather(arguments,// check to see if we've been run
										// with enough args
							totalArgs,	// a copy
							expected);	// how many more do we need to run?;
			}
		}
	}
	return gather([], outerArgs, ecount);
}

dojo.lang.curryArguments = function(ns, func, args, offset){
	var targs = [];
	var x = offset||0;
	for(x=offset; x<args.length; x++){
		targs.push(args[x]); // ensure that it's an arr
	}
	return dojo.lang.curry.apply(dojo.lang, [ns, func].concat(targs));
}

dojo.lang.tryThese = function(){
	for(var x=0; x<arguments.length; x++){
		try{
			if(typeof arguments[x] == "function"){
				var ret = (arguments[x]());
				if(ret){
					return ret;
				}
			}
		}catch(e){
			dojo.debug(e);
		}
	}
}

dojo.lang.delayThese = function(farr, cb, delay, onend){
	/**
	 * alternate: (array funcArray, function callback, function onend)
	 * alternate: (array funcArray, function callback)
	 * alternate: (array funcArray)
	 */
	if(!farr.length){ 
		if(typeof onend == "function"){
			onend();
		}
		return;
	}
	if((typeof delay == "undefined")&&(typeof cb == "number")){
		delay = cb;
		cb = function(){};
	}else if(!cb){
		cb = function(){};
		if(!delay){ delay = 0; }
	}
	setTimeout(function(){
		(farr.shift())();
		cb();
		dojo.lang.delayThese(farr, cb, delay, onend);
	}, delay);
}

dojo.provide("dojo.string.extras");

dojo.require("dojo.string.common");
dojo.require("dojo.lang");

/**
 * Parameterized string function
 * str - formatted string with %{values} to be replaces
 * pairs - object of name: "value" value pairs
 * killExtra - remove all remaining %{values} after pairs are inserted
 */
dojo.string.paramString = function(str, pairs, killExtra) {
	for(var name in pairs) {
		var re = new RegExp("\\%\\{" + name + "\\}", "g");
		str = str.replace(re, pairs[name]);
	}

	if(killExtra) { str = str.replace(/%\{([^\}\s]+)\}/g, ""); }
	return str;
}

/** Uppercases the first letter of each word */
dojo.string.capitalize = function (str) {
	if (!dojo.lang.isString(str)) { return ""; }
	if (arguments.length == 0) { str = this; }
	var words = str.split(' ');
	var retval = "";
	var len = words.length;
	for (var i=0; i<len; i++) {
		var word = words[i];
		word = word.charAt(0).toUpperCase() + word.substring(1, word.length);
		retval += word;
		if (i < len-1)
			retval += " ";
	}
	
	return new String(retval);
}

/**
 * Return true if the entire string is whitespace characters
 */
dojo.string.isBlank = function (str) {
	if(!dojo.lang.isString(str)) { return true; }
	return (dojo.string.trim(str).length == 0);
}

dojo.string.encodeAscii = function(str) {
	if(!dojo.lang.isString(str)) { return str; }
	var ret = "";
	var value = escape(str);
	var match, re = /%u([0-9A-F]{4})/i;
	while((match = value.match(re))) {
		var num = Number("0x"+match[1]);
		var newVal = escape("&#" + num + ";");
		ret += value.substring(0, match.index) + newVal;
		value = value.substring(match.index+match[0].length);
	}
	ret += value.replace(/\+/g, "%2B");
	return ret;
}

dojo.string.escape = function(type, str) {
	var args = [];
	for(var i = 1; i < arguments.length; i++) { args.push(arguments[i]); }
	switch(type.toLowerCase()) {
		case "xml":
		case "html":
		case "xhtml":
			return dojo.string.escapeXml.apply(this, args);
		case "sql":
			return dojo.string.escapeSql.apply(this, args);
		case "regexp":
		case "regex":
			return dojo.string.escapeRegExp.apply(this, args);
		case "javascript":
		case "jscript":
		case "js":
			return dojo.string.escapeJavaScript.apply(this, args);
		case "ascii":
			// so it's encode, but it seems useful
			return dojo.string.encodeAscii.apply(this, args);
		default:
			return str;
	}
}

dojo.string.escapeXml = function(str, noSingleQuotes) {
	str = str.replace(/&/gm, "&amp;").replace(/</gm, "&lt;")
		.replace(/>/gm, "&gt;").replace(/"/gm, "&quot;");
	if(!noSingleQuotes) { str = str.replace(/'/gm, "&#39;"); }
	return str;
}

dojo.string.escapeSql = function(str) {
	return str.replace(/'/gm, "''");
}

dojo.string.escapeRegExp = function(str) {
	return str.replace(/\\/gm, "\\\\").replace(/([\f\b\n\t\r])/gm, "\\$1");
}

dojo.string.escapeJavaScript = function(str) {
	return str.replace(/(["'\f\b\n\t\r])/gm, "\\$1");
}
// TODO: make an HTML version
dojo.string.summary = function(str, len) {
	if(!len || str.length <= len) {
		return str;
	} else {
		return str.substring(0, len).replace(/\.+$/, "") + "...";
	}
}

/**
 * Returns true if 'str' ends with 'end'
 */
dojo.string.endsWith = function(str, end, ignoreCase) {
	if(ignoreCase) {
		str = str.toLowerCase();
		end = end.toLowerCase();
	}
	return str.lastIndexOf(end) == str.length - end.length;
}

/**
 * Returns true if 'str' ends with any of the arguments[2 -> n]
 */
dojo.string.endsWithAny = function(str /* , ... */) {
	for(var i = 1; i < arguments.length; i++) {
		if(dojo.string.endsWith(str, arguments[i])) {
			return true;
		}
	}
	return false;
}

/**
 * Returns true if 'str' starts with 'start'
 */
dojo.string.startsWith = function(str, start, ignoreCase) {
	if(ignoreCase) {
		str = str.toLowerCase();
		start = start.toLowerCase();
	}
	return str.indexOf(start) == 0;
}

/**
 * Returns true if 'str' starts with any of the arguments[2 -> n]
 */
dojo.string.startsWithAny = function(str /* , ... */) {
	for(var i = 1; i < arguments.length; i++) {
		if(dojo.string.startsWith(str, arguments[i])) {
			return true;
		}
	}
	return false;
}

/**
 * Returns true if 'str' starts with any of the arguments 2 -> n
 */
dojo.string.has = function(str /* , ... */) {
	for(var i = 1; i < arguments.length; i++) {
		if(str.indexOf(arguments[i]) > -1){
			return true;
		}
	}
	return false;
}

dojo.string.normalizeNewlines = function (text,newlineChar) {
	if (newlineChar == "\n") {
		text = text.replace(/\r\n/g, "\n");
		text = text.replace(/\r/g, "\n");
	} else if (newlineChar == "\r") {
		text = text.replace(/\r\n/g, "\r");
		text = text.replace(/\n/g, "\r");
	} else {
		text = text.replace(/([^\r])\n/g, "$1\r\n");
		text = text.replace(/\r([^\n])/g, "\r\n$1");
	}
	return text;
}

dojo.string.splitEscaped = function (str,charac) {
	var components = [];
	for (var i = 0, prevcomma = 0; i < str.length; i++) {
		if (str.charAt(i) == '\\') { i++; continue; }
		if (str.charAt(i) == charac) {
			components.push(str.substring(prevcomma, i));
			prevcomma = i + 1;
		}
	}
	components.push(str.substr(prevcomma));
	return components;
}

dojo.provide("dojo.dom");
dojo.require("dojo.lang.array");

dojo.dom.ELEMENT_NODE                  = 1;
dojo.dom.ATTRIBUTE_NODE                = 2;
dojo.dom.TEXT_NODE                     = 3;
dojo.dom.CDATA_SECTION_NODE            = 4;
dojo.dom.ENTITY_REFERENCE_NODE         = 5;
dojo.dom.ENTITY_NODE                   = 6;
dojo.dom.PROCESSING_INSTRUCTION_NODE   = 7;
dojo.dom.COMMENT_NODE                  = 8;
dojo.dom.DOCUMENT_NODE                 = 9;
dojo.dom.DOCUMENT_TYPE_NODE            = 10;
dojo.dom.DOCUMENT_FRAGMENT_NODE        = 11;
dojo.dom.NOTATION_NODE                 = 12;
	
dojo.dom.dojoml = "http://www.dojotoolkit.org/2004/dojoml";

/**
 *	comprehensive list of XML namespaces
**/
dojo.dom.xmlns = {
	svg : "http://www.w3.org/2000/svg",
	smil : "http://www.w3.org/2001/SMIL20/",
	mml : "http://www.w3.org/1998/Math/MathML",
	cml : "http://www.xml-cml.org",
	xlink : "http://www.w3.org/1999/xlink",
	xhtml : "http://www.w3.org/1999/xhtml",
	xul : "http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul",
	xbl : "http://www.mozilla.org/xbl",
	fo : "http://www.w3.org/1999/XSL/Format",
	xsl : "http://www.w3.org/1999/XSL/Transform",
	xslt : "http://www.w3.org/1999/XSL/Transform",
	xi : "http://www.w3.org/2001/XInclude",
	xforms : "http://www.w3.org/2002/01/xforms",
	saxon : "http://icl.com/saxon",
	xalan : "http://xml.apache.org/xslt",
	xsd : "http://www.w3.org/2001/XMLSchema",
	dt: "http://www.w3.org/2001/XMLSchema-datatypes",
	xsi : "http://www.w3.org/2001/XMLSchema-instance",
	rdf : "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
	rdfs : "http://www.w3.org/2000/01/rdf-schema#",
	dc : "http://purl.org/dc/elements/1.1/",
	dcq: "http://purl.org/dc/qualifiers/1.0",
	"soap-env" : "http://schemas.xmlsoap.org/soap/envelope/",
	wsdl : "http://schemas.xmlsoap.org/wsdl/",
	AdobeExtensions : "http://ns.adobe.com/AdobeSVGViewerExtensions/3.0/"
};

dojo.dom.isNode = function(wh){
	if(typeof Element == "object") {
		try {
			return wh instanceof Element;
		} catch(E) {}
	} else {
		// best-guess
		return wh && !isNaN(wh.nodeType);
	}
}

dojo.dom.getTagName = function(node){
	var tagName = node.tagName;
	if(tagName.substr(0,5).toLowerCase()!="dojo:"){
		
		if(tagName.substr(0,4).toLowerCase()=="dojo"){
			// FIXME: this assuumes tag names are always lower case
			return "dojo:" + tagName.substring(4).toLowerCase();
		}

		// allow lower-casing
		var djt = node.getAttribute("dojoType")||node.getAttribute("dojotype");
		if(djt){
			return "dojo:"+djt.toLowerCase();
		}
		
		if((node.getAttributeNS)&&(node.getAttributeNS(this.dojoml,"type"))){
			return "dojo:" + node.getAttributeNS(this.dojoml,"type").toLowerCase();
		}
		try{
			// FIXME: IE really really doesn't like this, so we squelch
			// errors for it
			djt = node.getAttribute("dojo:type");
		}catch(e){ /* FIXME: log? */ }
		if(djt){
			return "dojo:"+djt.toLowerCase();
		}

		if((!dj_global["djConfig"])||(!djConfig["ignoreClassNames"])){
			// FIXME: should we make this optionally enabled via djConfig?
			var classes = node.className||node.getAttribute("class");
			// FIXME: following line, without check for existence of classes.indexOf
			// breaks firefox 1.5's svg widgets
			if((classes)&&(classes.indexOf)&&(classes.indexOf("dojo-") != -1)){
				var aclasses = classes.split(" ");
				for(var x=0; x<aclasses.length; x++){
					if((aclasses[x].length>5)&&(aclasses[x].indexOf("dojo-")>=0)){
						return "dojo:"+aclasses[x].substr(5).toLowerCase();
					}
				}
			}
		}

	}
	return tagName.toLowerCase();
}

dojo.dom.getUniqueId = function(){
	do {
		var id = "dj_unique_" + (++arguments.callee._idIncrement);
	}while(document.getElementById(id));
	return id;
}
dojo.dom.getUniqueId._idIncrement = 0;

dojo.dom.firstElement = dojo.dom.getFirstChildElement = function(parentNode, tagName){
	var node = parentNode.firstChild;
	while(node && node.nodeType != dojo.dom.ELEMENT_NODE){
		node = node.nextSibling;
	}
	if(tagName && node && node.tagName && node.tagName.toLowerCase() != tagName.toLowerCase()) {
		node = dojo.dom.nextElement(node, tagName);
	}
	return node;
}

dojo.dom.lastElement = dojo.dom.getLastChildElement = function(parentNode, tagName){
	var node = parentNode.lastChild;
	while(node && node.nodeType != dojo.dom.ELEMENT_NODE) {
		node = node.previousSibling;
	}
	if(tagName && node && node.tagName && node.tagName.toLowerCase() != tagName.toLowerCase()) {
		node = dojo.dom.prevElement(node, tagName);
	}
	return node;
}

dojo.dom.nextElement = dojo.dom.getNextSiblingElement = function(node, tagName){
	if(!node) { return null; }
	do {
		node = node.nextSibling;
	} while(node && node.nodeType != dojo.dom.ELEMENT_NODE);

	if(node && tagName && tagName.toLowerCase() != node.tagName.toLowerCase()) {
		return dojo.dom.nextElement(node, tagName);
	}
	return node;
}

dojo.dom.prevElement = dojo.dom.getPreviousSiblingElement = function(node, tagName){
	if(!node) { return null; }
	if(tagName) { tagName = tagName.toLowerCase(); }
	do {
		node = node.previousSibling;
	} while(node && node.nodeType != dojo.dom.ELEMENT_NODE);

	if(node && tagName && tagName.toLowerCase() != node.tagName.toLowerCase()) {
		return dojo.dom.prevElement(node, tagName);
	}
	return node;
}

// TODO: hmph
/*this.forEachChildTag = function(node, unaryFunc) {
	var child = this.getFirstChildTag(node);
	while(child) {
		if(unaryFunc(child) == "break") { break; }
		child = this.getNextSiblingTag(child);
	}
}*/

dojo.dom.moveChildren = function(srcNode, destNode, trim){
	var count = 0;
	if(trim) {
		while(srcNode.hasChildNodes() &&
			srcNode.firstChild.nodeType == dojo.dom.TEXT_NODE) {
			srcNode.removeChild(srcNode.firstChild);
		}
		while(srcNode.hasChildNodes() &&
			srcNode.lastChild.nodeType == dojo.dom.TEXT_NODE) {
			srcNode.removeChild(srcNode.lastChild);
		}
	}
	while(srcNode.hasChildNodes()){
		destNode.appendChild(srcNode.firstChild);
		count++;
	}
	return count;
}

dojo.dom.copyChildren = function(srcNode, destNode, trim){
	var clonedNode = srcNode.cloneNode(true);
	return this.moveChildren(clonedNode, destNode, trim);
}

dojo.dom.removeChildren = function(node){
	var count = node.childNodes.length;
	while(node.hasChildNodes()){ node.removeChild(node.firstChild); }
	return count;
}

dojo.dom.replaceChildren = function(node, newChild){
	// FIXME: what if newChild is an array-like object?
	dojo.dom.removeChildren(node);
	node.appendChild(newChild);
}

dojo.dom.removeNode = function(node){
	if(node && node.parentNode){
		// return a ref to the removed child
		return node.parentNode.removeChild(node);
	}
}

dojo.dom.getAncestors = function(node, filterFunction, returnFirstHit) {
	var ancestors = [];
	var isFunction = dojo.lang.isFunction(filterFunction);
	while(node) {
		if (!isFunction || filterFunction(node)) {
			ancestors.push(node);
		}
		if (returnFirstHit && ancestors.length > 0) { return ancestors[0]; }
		
		node = node.parentNode;
	}
	if (returnFirstHit) { return null; }
	return ancestors;
}

dojo.dom.getAncestorsByTag = function(node, tag, returnFirstHit) {
	tag = tag.toLowerCase();
	return dojo.dom.getAncestors(node, function(el){
		return ((el.tagName)&&(el.tagName.toLowerCase() == tag));
	}, returnFirstHit);
}

dojo.dom.getFirstAncestorByTag = function(node, tag) {
	return dojo.dom.getAncestorsByTag(node, tag, true);
}

dojo.dom.isDescendantOf = function(node, ancestor, guaranteeDescendant){
	// guaranteeDescendant allows us to be a "true" isDescendantOf function
	if(guaranteeDescendant && node) { node = node.parentNode; }
	while(node) {
		if(node == ancestor){ return true; }
		node = node.parentNode;
	}
	return false;
}

dojo.dom.innerXML = function(node){
	if(node.innerXML){
		return node.innerXML;
	}else if(typeof XMLSerializer != "undefined"){
		return (new XMLSerializer()).serializeToString(node);
	}
}

dojo.dom.createDocumentFromText = function(str, mimetype){
	if(!mimetype) { mimetype = "text/xml"; }
	if(typeof DOMParser != "undefined") {
		var parser = new DOMParser();
		return parser.parseFromString(str, mimetype);
	}else if(typeof ActiveXObject != "undefined"){
		var domDoc = new ActiveXObject("Microsoft.XMLDOM");
		if(domDoc) {
			domDoc.async = false;
			domDoc.loadXML(str);
			return domDoc;
		}else{
			dojo.debug("toXml didn't work?");
		}
	/*
	}else if((dojo.render.html.capable)&&(dojo.render.html.safari)){
		// FIXME: this doesn't appear to work!
		// from: http://web-graphics.com/mtarchive/001606.php
		// var xml = '<?xml version="1.0"?>'+str;
		var mtype = "text/xml";
		var xml = '<?xml version="1.0"?>'+str;
		var url = "data:"+mtype+";charset=utf-8,"+encodeURIComponent(xml);
		var req = new XMLHttpRequest();
		req.open("GET", url, false);
		req.overrideMimeType(mtype);
		req.send(null);
		return req.responseXML;
	*/
	}else if(document.createElement){
		// FIXME: this may change all tags to uppercase!
		var tmp = document.createElement("xml");
		tmp.innerHTML = str;
		if(document.implementation && document.implementation.createDocument) {
			var xmlDoc = document.implementation.createDocument("foo", "", null);
			for(var i = 0; i < tmp.childNodes.length; i++) {
				xmlDoc.importNode(tmp.childNodes.item(i), true);
			}
			return xmlDoc;
		}
		// FIXME: probably not a good idea to have to return an HTML fragment
		// FIXME: the tmp.doc.firstChild is as tested from IE, so it may not
		// work that way across the board
		return tmp.document && tmp.document.firstChild ?
			tmp.document.firstChild : tmp;
	}
	return null;
}

dojo.dom.prependChild = function(node, parent) {
	if(parent.firstChild) {
		parent.insertBefore(node, parent.firstChild);
	} else {
		parent.appendChild(node);
	}
	return true;
}

dojo.dom.insertBefore = function(node, ref, force){
	if (force != true &&
		(node === ref || node.nextSibling === ref)){ return false; }
	var parent = ref.parentNode;
	parent.insertBefore(node, ref);
	return true;
}

dojo.dom.insertAfter = function(node, ref, force){
	var pn = ref.parentNode;
	if(ref == pn.lastChild){
		if((force != true)&&(node === ref)){
			return false;
		}
		pn.appendChild(node);
	}else{
		return this.insertBefore(node, ref.nextSibling, force);
	}
	return true;
}

dojo.dom.insertAtPosition = function(node, ref, position){
	if((!node)||(!ref)||(!position)){ return false; }
	switch(position.toLowerCase()){
		case "before":
			return dojo.dom.insertBefore(node, ref);
		case "after":
			return dojo.dom.insertAfter(node, ref);
		case "first":
			if(ref.firstChild){
				return dojo.dom.insertBefore(node, ref.firstChild);
			}else{
				ref.appendChild(node);
				return true;
			}
			break;
		default: // aka: last
			ref.appendChild(node);
			return true;
	}
}

dojo.dom.insertAtIndex = function(node, containingNode, insertionIndex){
	var siblingNodes = containingNode.childNodes;

	// if there aren't any kids yet, just add it to the beginning

	if (!siblingNodes.length){
		containingNode.appendChild(node);
		return true;
	}

	// otherwise we need to walk the childNodes
	// and find our spot

	var after = null;

	for(var i=0; i<siblingNodes.length; i++){

		var sibling_index = siblingNodes.item(i)["getAttribute"] ? parseInt(siblingNodes.item(i).getAttribute("dojoinsertionindex")) : -1;

		if (sibling_index < insertionIndex){
			after = siblingNodes.item(i);
		}
	}

	if (after){
		// add it after the node in {after}

		return dojo.dom.insertAfter(node, after);
	}else{
		// add it to the start

		return dojo.dom.insertBefore(node, siblingNodes.item(0));
	}
}
	
/**
 * implementation of the DOM Level 3 attribute.
 * 
 * @param node The node to scan for text
 * @param text Optional, set the text to this value.
 */
dojo.dom.textContent = function(node, text){
	if (text) {
		dojo.dom.replaceChildren(node, document.createTextNode(text));
		return text;
	} else {
		var _result = "";
		if (node == null) { return _result; }
		for (var i = 0; i < node.childNodes.length; i++) {
			switch (node.childNodes[i].nodeType) {
				case 1: // ELEMENT_NODE
				case 5: // ENTITY_REFERENCE_NODE
					_result += dojo.dom.textContent(node.childNodes[i]);
					break;
				case 3: // TEXT_NODE
				case 2: // ATTRIBUTE_NODE
				case 4: // CDATA_SECTION_NODE
					_result += node.childNodes[i].nodeValue;
					break;
				default:
					break;
			}
		}
		return _result;
	}
}

dojo.dom.collectionToArray = function(collection){
	dojo.deprecated("dojo.dom.collectionToArray", "use dojo.lang.toArray instead");
	return dojo.lang.toArray(collection);
}

dojo.dom.hasParent = function(node) {
	if(!node || !node.parentNode || (node.parentNode && !node.parentNode.tagName)) {
		return false;
	}
	return true;
}

/**
 * Determines if node has any of the provided tag names and
 * returns the tag name that matches, empty string otherwise.
 *
 * Examples:
 *
 * myFooNode = <foo />
 * isTag(myFooNode, "foo"); // returns "foo"
 * isTag(myFooNode, "bar"); // returns ""
 * isTag(myFooNode, "FOO"); // returns ""
 * isTag(myFooNode, "hey", "foo", "bar"); // returns "foo"
**/
dojo.dom.isTag = function(node /* ... */) {
	if(node && node.tagName) {
		var arr = dojo.lang.toArray(arguments, 1);
		return arr[ dojo.lang.find(node.tagName, arr) ] || "";
	}
	return "";
}

dojo.provide("dojo.undo.browser");
dojo.require("dojo.io");

try{
	if((!djConfig["preventBackButtonFix"])&&(!dojo.hostenv.post_load_)){
		document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(dojo.hostenv.getBaseScriptUri()+'iframe_history.html')+"'></iframe>");
	}
}catch(e){/* squelch */}

/* NOTES:
 *  Safari 1.2: 
 *	back button "works" fine, however it's not possible to actually
 *	DETECT that you've moved backwards by inspecting window.location.
 *	Unless there is some other means of locating.
 *	FIXME: perhaps we can poll on history.length?
 *  Safari 2.0.3+ (and probably 1.3.2+):
 *	works fine, except when changeUrl is used. When changeUrl is used,
 *	Safari jumps all the way back to whatever page was shown before
 *	the page that uses dojo.undo.browser support.
 *  IE 5.5 SP2:
 *	back button behavior is macro. It does not move back to the
 *	previous hash value, but to the last full page load. This suggests
 *	that the iframe is the correct way to capture the back button in
 *	these cases.
 *	Don't test this page using local disk for MSIE. MSIE will not create 
 *	a history list for iframe_history.html if served from a file: URL. 
 *	The XML served back from the XHR tests will also not be properly 
 *	created if served from local disk. Serve the test pages from a web 
 *	server to test in that browser.
 *  IE 6.0:
 *	same behavior as IE 5.5 SP2
 * Firefox 1.0:
 *	the back button will return us to the previous hash on the same
 *	page, thereby not requiring an iframe hack, although we do then
 *	need to run a timer to detect inter-page movement.
 */
dojo.undo.browser = {
	initialHref: window.location.href,
	initialHash: window.location.hash,

	moveForward: false,
	historyStack: [],
	forwardStack: [],
	historyIframe: null,
	bookmarkAnchor: null,
	locationTimer: null,

	//FIXME: what about allowing for a handler for the first page that comes up?
	//FIXME: Would like to support arbitrary back/forward jumps. Have to rework iframeLoaded among other things.
	//FIXME: is there a slight race condition in moz using change URL with the timer check and when
	//       the hash gets set? I think I have seen a back/forward call in quick succession, but not consistent.
	/**
	 * addToHistory takes one argument, and it is an object that defines the following functions:
	 * - To support getting back button notifications, the object argument should implement a
	 *   function called either "back", "backButton", or "handle". The string "back" will be
	 *   passed as the first and only argument to this callback.
	 * - To support getting forward button notifications, the object argument should implement a
	 *   function called either "forward", "forwardButton", or "handle". The string "forward" will be
	 *   passed as the first and only argument to this callback.
	 * - If you want the browser location string to change, define "changeUrl" on the object. If the
	 *   value of "changeUrl" is true, then a unique number will be appended to the URL as a fragment
	 *   identifier (http://some.domain.com/path#uniquenumber). If it is any other value that does
	 *   not evaluate to false, that value will be used as the fragment identifier. For example,
	 *   if changeUrl: 'page1', then the URL will look like: http://some.domain.com/path#page1
	 *   
	 * Full example:
	 * 
	 * dojo.undo.browser.addToHistory({
	 *   back: function() { alert('back pressed'); },
	 *   forward: function() { alert('forward pressed'); },
	 *   changeUrl: true
	 * });
	 */
	addToHistory: function(args){
		var hash = null;
		if(!this.historyIframe){
			this.historyIframe = window.frames["djhistory"];
		}
		if(!this.bookmarkAnchor){
			this.bookmarkAnchor = document.createElement("a");
			(document.body||document.getElementsByTagName("body")[0]).appendChild(this.bookmarkAnchor);
			this.bookmarkAnchor.style.display = "none";
		}
		if((!args["changeUrl"])||(dojo.render.html.ie)){
			var url = dojo.hostenv.getBaseScriptUri()+"iframe_history.html?"+(new Date()).getTime();
			this.moveForward = true;
			dojo.io.setIFrameSrc(this.historyIframe, url, false);
		}
		if(args["changeUrl"]){
			hash = "#"+ ((args["changeUrl"]!==true) ? args["changeUrl"] : (new Date()).getTime());
			setTimeout("window.location.href = '"+hash+"';", 1);
			this.bookmarkAnchor.href = hash;
			
			if(dojo.render.html.ie){
				var oldCB = args["back"]||args["backButton"]||args["handle"];

				//The function takes handleName as a parameter, in case the
				//callback we are overriding was "handle". In that case,
				//we will need to pass the handle name to handle.
				var tcb = function(handleName){
					if(window.location.hash != ""){
						setTimeout("window.location.href = '"+hash+"';", 1);
					}
					//Use apply to set "this" to args, and to try to avoid memory leaks.
					oldCB.apply(this, [handleName]);
				}
		
				//Set interceptor function in the right place.
				if(args["back"]){
					args.back = tcb;
				}else if(args["backButton"]){
					args.backButton = tcb;
				}else if(args["handle"]){
					args.handle = tcb;
				}
		
				//If addToHistory is called, then that means we prune the
				//forward stack -- the user went back, then wanted to
				//start a new forward path.
				this.forwardStack = []; 
				var oldFW = args["forward"]||args["forwardButton"]||args["handle"];
		
				//The function takes handleName as a parameter, in case the
				//callback we are overriding was "handle". In that case,
				//we will need to pass the handle name to handle.
				var tfw = function(handleName){
					if(window.location.hash != ""){
						window.location.href = hash;
					}
					if(oldFW){ // we might not actually have one
						//Use apply to set "this" to args, and to try to avoid memory leaks.
						oldFW.apply(this, [handleName]);
					}
				}

				//Set interceptor function in the right place.
				if(args["forward"]){
					args.forward = tfw;
				}else if(args["forwardButton"]){
					args.forwardButton = tfw;
				}else if(args["handle"]){
					args.handle = tfw;
				}
		
			}else if(dojo.render.html.moz){
				// start the timer
				if(!this.locationTimer){
					this.locationTimer = setInterval("dojo.undo.browser.checkLocation();", 200);
				}
			}
		}

		this.historyStack.push({"url": url, "kwArgs": args, "urlHash": hash});
	},

	checkLocation: function(){
		var hsl = this.historyStack.length;

		if((window.location.hash == this.initialHash)||(window.location.href == this.initialHref)&&(hsl == 1)){
			// FIXME: could this ever be a forward button?
			// we can't clear it because we still need to check for forwards. Ugg.
			// clearInterval(this.locationTimer);
			this.handleBackButton();
			return;
		}
		// first check to see if we could have gone forward. We always halt on
		// a no-hash item.
		if(this.forwardStack.length > 0){
			if(this.forwardStack[this.forwardStack.length-1].urlHash == window.location.hash){
				this.handleForwardButton();
				return;
			}
		}

		// ok, that didn't work, try someplace back in the history stack
		if((hsl >= 2)&&(this.historyStack[hsl-2])){
			if(this.historyStack[hsl-2].urlHash==window.location.hash){
				this.handleBackButton();
				return;
			}
		}
	},

	iframeLoaded: function(evt, ifrLoc){
		var query = this._getUrlQuery(ifrLoc.href);
		if(query == null){ 
			// alert("iframeLoaded");
			// we hit the end of the history, so we should go back
			if(this.historyStack.length == 1){
				this.handleBackButton();
			}
			return;
		}
		if(this.moveForward){
			// we were expecting it, so it's not either a forward or backward movement
			this.moveForward = false;
			return;
		}

		//Check the back stack first, since it is more likely.
		//Note that only one step back or forward is supported.
		if(this.historyStack.length >= 2 && query == this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){
			this.handleBackButton();
		}
		else if(this.forwardStack.length > 0 && query == this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){
			this.handleForwardButton();
		}
	},

	handleBackButton: function(){
		//The "current" page is always at the top of the history stack.
		var current = this.historyStack.pop();
		var last = this.historyStack[this.historyStack.length-1];
		if(!current){ return; }
		if (last){
			if(last.kwArgs["back"]){
				last.kwArgs["back"]();
			}else if(last.kwArgs["backButton"]){
				last.kwArgs["backButton"]();
			}else if(last.kwArgs["handle"]){
				last.kwArgs.handle("back");
			}
		}
		this.forwardStack.push(current);
	},

	handleForwardButton: function(){
		var last = this.forwardStack.pop();
		if(!last){ return; }
		if(last.kwArgs["forward"]){
			last.kwArgs.forward();
		}else if(last.kwArgs["forwardButton"]){
			last.kwArgs.forwardButton();
		}else if(last.kwArgs["handle"]){
			last.kwArgs.handle("forward");
		}
		this.historyStack.push(last);
	},

	_getUrlQuery: function(url){
		var segments = url.split("?");
		if (segments.length < 2){
			return null;
		}
		else{
			return segments[1];
		}
	}
}

dojo.provide("dojo.io.BrowserIO");

dojo.require("dojo.io");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.func");
dojo.require("dojo.string.extras");
dojo.require("dojo.dom");
dojo.require("dojo.undo.browser");

dojo.io.checkChildrenForFile = function(node){
	var hasFile = false;
	var inputs = node.getElementsByTagName("input");
	dojo.lang.forEach(inputs, function(input){
		if(hasFile){ return; }
		if(input.getAttribute("type")=="file"){
			hasFile = true;
		}
	});
	return hasFile;
}

dojo.io.formHasFile = function(formNode){
	return dojo.io.checkChildrenForFile(formNode);
}

dojo.io.formFilter = function(node) {
	var type = (node.type||"").toLowerCase();
	return !node.disabled && node.name
		&& !dojo.lang.inArray(type, ["file", "submit", "image", "reset", "button"]);
}

// TODO: Move to htmlUtils
dojo.io.encodeForm = function(formNode, encoding, formFilter){
	if((!formNode)||(!formNode.tagName)||(!formNode.tagName.toLowerCase() == "form")){
		dojo.raise("Attempted to encode a non-form element.");
	}
	if(!formFilter) { formFilter = dojo.io.formFilter; }
	var enc = /utf/i.test(encoding||"") ? encodeURIComponent : dojo.string.encodeAscii;
	var values = [];

	for(var i = 0; i < formNode.elements.length; i++){
		var elm = formNode.elements[i];
		if(!elm || elm.tagName.toLowerCase() == "fieldset" || !formFilter(elm)) { continue; }
		var name = enc(elm.name);
		var type = elm.type.toLowerCase();

		if(type == "select-multiple"){
			for(var j = 0; j < elm.options.length; j++){
				if(elm.options[j].selected) {
					values.push(name + "=" + enc(elm.options[j].value));
				}
			}
		}else if(dojo.lang.inArray(type, ["radio", "checkbox"])){
			if(elm.checked){
				values.push(name + "=" + enc(elm.value));
			}
		}else{
			values.push(name + "=" + enc(elm.value));
		}
	}

	// now collect input type="image", which doesn't show up in the elements array
	var inputs = formNode.getElementsByTagName("input");
	for(var i = 0; i < inputs.length; i++) {
		var input = inputs[i];
		if(input.type.toLowerCase() == "image" && input.form == formNode
			&& formFilter(input)) {
			var name = enc(input.name);
			values.push(name + "=" + enc(input.value));
			values.push(name + ".x=0");
			values.push(name + ".y=0");
		}
	}
	return values.join("&") + "&";
}

dojo.io.FormBind = function(args) {
	this.bindArgs = {};

	if(args && args.formNode) {
		this.init(args);
	} else if(args) {
		this.init({formNode: args});
	}
}
dojo.lang.extend(dojo.io.FormBind, {
	form: null,

	bindArgs: null,

	clickedButton: null,

	init: function(args) {
		var form = dojo.byId(args.formNode);

		if(!form || !form.tagName || form.tagName.toLowerCase() != "form") {
			throw new Error("FormBind: Couldn't apply, invalid form");
		} else if(this.form == form) {
			return;
		} else if(this.form) {
			throw new Error("FormBind: Already applied to a form");
		}

		dojo.lang.mixin(this.bindArgs, args);
		this.form = form;

		this.connect(form, "onsubmit", "submit");

		for(var i = 0; i < form.elements.length; i++) {
			var node = form.elements[i];
			if(node && node.type && dojo.lang.inArray(node.type.toLowerCase(), ["submit", "button"])) {
				this.connect(node, "onclick", "click");
			}
		}

		var inputs = form.getElementsByTagName("input");
		for(var i = 0; i < inputs.length; i++) {
			var input = inputs[i];
			if(input.type.toLowerCase() == "image" && input.form == form) {
				this.connect(input, "onclick", "click");
			}
		}
	},

	onSubmit: function(form) {
		return true;
	},

	submit: function(e) {
		e.preventDefault();
		if(this.onSubmit(this.form)) {
			dojo.io.bind(dojo.lang.mixin(this.bindArgs, {
				formFilter: dojo.lang.hitch(this, "formFilter")
			}));
		}
	},

	click: function(e) {
		var node = e.currentTarget;
		if(node.disabled) { return; }
		this.clickedButton = node;
	},

	formFilter: function(node) {
		var type = (node.type||"").toLowerCase();
		var accept = false;
		if(node.disabled || !node.name) {
			accept = false;
		} else if(dojo.lang.inArray(type, ["submit", "button", "image"])) {
			if(!this.clickedButton) { this.clickedButton = node; }
			accept = node == this.clickedButton;
		} else {
			accept = !dojo.lang.inArray(type, ["file", "submit", "reset", "button"]);
		}
		return accept;
	},

	// in case you don't have dojo.event.* pulled in
	connect: function(srcObj, srcFcn, targetFcn) {
		if(dojo.evalObjPath("dojo.event.connect")) {
			dojo.event.connect(srcObj, srcFcn, this, targetFcn);
		} else {
			var fcn = dojo.lang.hitch(this, targetFcn);
			srcObj[srcFcn] = function(e) {
				if(!e) { e = window.event; }
				if(!e.currentTarget) { e.currentTarget = e.srcElement; }
				if(!e.preventDefault) { e.preventDefault = function() { window.event.returnValue = false; } }
				fcn(e);
			}
		}
	}
});

dojo.io.XMLHTTPTransport = new function(){
	var _this = this;

	var _cache = {}; // FIXME: make this public? do we even need to?
	this.useCache = false; // if this is true, we'll cache unless kwArgs.useCache = false
	this.preventCache = false; // if this is true, we'll always force GET requests to cache

	// FIXME: Should this even be a function? or do we just hard code it in the next 2 functions?
	function getCacheKey(url, query, method) {
		return url + "|" + query + "|" + method.toLowerCase();
	}

	function addToCache(url, query, method, http) {
		_cache[getCacheKey(url, query, method)] = http;
	}

	function getFromCache(url, query, method) {
		return _cache[getCacheKey(url, query, method)];
	}

	this.clearCache = function() {
		_cache = {};
	}

	// moved successful load stuff here
	function doLoad(kwArgs, http, url, query, useCache) {
		if(	(http.status==200)||
			(http.status==304)||
			(location.protocol=="file:" && (http.status==0 || http.status==undefined))||
			(location.protocol=="chrome:" && (http.status==0 || http.status==undefined))
		){
			var ret;
			if(kwArgs.method.toLowerCase() == "head"){
				var headers = http.getAllResponseHeaders();
				ret = {};
				ret.toString = function(){ return headers; }
				var values = headers.split(/[\r\n]+/g);
				for(var i = 0; i < values.length; i++) {
					var pair = values[i].match(/^([^:]+)\s*:\s*(.+)$/i);
					if(pair) {
						ret[pair[1]] = pair[2];
					}
				}
			}else if(kwArgs.mimetype == "text/javascript"){
				try{
					ret = dj_eval(http.responseText);
				}catch(e){
					dojo.debug(e);
					dojo.debug(http.responseText);
					ret = null;
				}
			}else if(kwArgs.mimetype == "text/json"){
				try{
					ret = dj_eval("("+http.responseText+")");
				}catch(e){
					dojo.debug(e);
					dojo.debug(http.responseText);
					ret = false;
				}
			}else if((kwArgs.mimetype == "application/xml")||
						(kwArgs.mimetype == "text/xml")){
				ret = http.responseXML;
				if(!ret || typeof ret == "string") {
					ret = dojo.dom.createDocumentFromText(http.responseText);
				}
			}else{
				ret = http.responseText;
			}

			if(useCache){ // only cache successful responses
				addToCache(url, query, kwArgs.method, http);
			}
			kwArgs[(typeof kwArgs.load == "function") ? "load" : "handle"]("load", ret, http);
		}else{
			var errObj = new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);
			kwArgs[(typeof kwArgs.error == "function") ? "error" : "handle"]("error", errObj, http);
		}
	}

	// set headers (note: Content-Type will get overriden if kwArgs.contentType is set)
	function setHeaders(http, kwArgs){
		if(kwArgs["headers"]) {
			for(var header in kwArgs["headers"]) {
				if(header.toLowerCase() == "content-type" && !kwArgs["contentType"]) {
					kwArgs["contentType"] = kwArgs["headers"][header];
				} else {
					http.setRequestHeader(header, kwArgs["headers"][header]);
				}
			}
		}
	}

	this.inFlight = [];
	this.inFlightTimer = null;

	this.startWatchingInFlight = function(){
		if(!this.inFlightTimer){
			this.inFlightTimer = setInterval("dojo.io.XMLHTTPTransport.watchInFlight();", 10);
		}
	}

	this.watchInFlight = function(){
		for(var x=this.inFlight.length-1; x>=0; x--){
			var tif = this.inFlight[x];
			if(!tif){ this.inFlight.splice(x, 1); continue; }
			if(4==tif.http.readyState){
				// remove it so we can clean refs
				this.inFlight.splice(x, 1);
				doLoad(tif.req, tif.http, tif.url, tif.query, tif.useCache);
				if(this.inFlight.length == 0){
					clearInterval(this.inFlightTimer);
					this.inFlightTimer = null;
				}
			} // FIXME: need to implement a timeout param here!
		}
	}

	var hasXmlHttp = dojo.hostenv.getXmlhttpObject() ? true : false;
	this.canHandle = function(kwArgs){
		// canHandle just tells dojo.io.bind() if this is a good transport to
		// use for the particular type of request.

		// FIXME: we need to determine when form values need to be
		// multi-part mime encoded and avoid using this transport for those
		// requests.
		return hasXmlHttp
			&& dojo.lang.inArray((kwArgs["mimetype"].toLowerCase()||""), ["text/plain", "text/html", "application/xml", "text/xml", "text/javascript", "text/json"])
			&& dojo.lang.inArray(kwArgs["method"].toLowerCase(), ["post", "get", "head"])
			&& !( kwArgs["formNode"] && dojo.io.formHasFile(kwArgs["formNode"]) );
	}

	this.multipartBoundary = "45309FFF-BD65-4d50-99C9-36986896A96F";	// unique guid as a boundary value for multipart posts

	this.bind = function(kwArgs){
		if(!kwArgs["url"]){
			// are we performing a history action?
			if( !kwArgs["formNode"]
				&& (kwArgs["backButton"] || kwArgs["back"] || kwArgs["changeUrl"] || kwArgs["watchForURL"])
				&& (!djConfig.preventBackButtonFix)) {
        dj_deprecated("Using dojo.io.XMLHTTPTransport.bind() to add to browser history without doing an IO request is deprecated. Use dojo.undo.browser.addToHistory() instead.");
				dojo.undo.browser.addToHistory(kwArgs);
				return true;
			}
		}

		// build this first for cache purposes
		var url = kwArgs.url;
		var query = "";
		if(kwArgs["formNode"]){
			var ta = kwArgs.formNode.getAttribute("action");
			if((ta)&&(!kwArgs["url"])){ url = ta; }
			var tp = kwArgs.formNode.getAttribute("method");
			if((tp)&&(!kwArgs["method"])){ kwArgs.method = tp; }
			query += dojo.io.encodeForm(kwArgs.formNode, kwArgs.encoding, kwArgs["formFilter"]);
		}

		if(url.indexOf("#") > -1) {
			dojo.debug("Warning: dojo.io.bind: stripping hash values from url:", url);
			url = url.split("#")[0];
		}

		if(kwArgs["file"]){
			// force post for file transfer
			kwArgs.method = "post";
		}

		if(!kwArgs["method"]){
			kwArgs.method = "get";
		}

		// guess the multipart value		
		if(kwArgs.method.toLowerCase() == "get"){
			// GET cannot use multipart
			kwArgs.multipart = false;
		}else{
			if(kwArgs["file"]){
				// enforce multipart when sending files
				kwArgs.multipart = true;
			}else if(!kwArgs["multipart"]){
				// default 
				kwArgs.multipart = false;
			}
		}

		if(kwArgs["backButton"] || kwArgs["back"] || kwArgs["changeUrl"]){
			dojo.undo.browser.addToHistory(kwArgs);
		}

		var content = kwArgs["content"] || {};

		if(kwArgs.sendTransport) {
			content["dojo.transport"] = "xmlhttp";
		}

		do { // break-block
			if(kwArgs.postContent){
				query = kwArgs.postContent;
				break;
			}

			if(content) {
				query += dojo.io.argsFromMap(content, kwArgs.encoding);
			}
			
			if(kwArgs.method.toLowerCase() == "get" || !kwArgs.multipart){
				break;
			}

			var	t = [];
			if(query.length){
				var q = query.split("&");
				for(var i = 0; i < q.length; ++i){
					if(q[i].length){
						var p = q[i].split("=");
						t.push(	"--" + this.multipartBoundary,
								"Content-Disposition: form-data; name=\"" + p[0] + "\"", 
								"",
								p[1]);
					}
				}
			}

			if(kwArgs.file){
				if(dojo.lang.isArray(kwArgs.file)){
					for(var i = 0; i < kwArgs.file.length; ++i){
						var o = kwArgs.file[i];
						t.push(	"--" + this.multipartBoundary,
								"Content-Disposition: form-data; name=\"" + o.name + "\"; filename=\"" + ("fileName" in o ? o.fileName : o.name) + "\"",
								"Content-Type: " + ("contentType" in o ? o.contentType : "application/octet-stream"),
								"",
								o.content);
					}
				}else{
					var o = kwArgs.file;
					t.push(	"--" + this.multipartBoundary,
							"Content-Disposition: form-data; name=\"" + o.name + "\"; filename=\"" + ("fileName" in o ? o.fileName : o.name) + "\"",
							"Content-Type: " + ("contentType" in o ? o.contentType : "application/octet-stream"),
							"",
							o.content);
				}
			}

			if(t.length){
				t.push("--"+this.multipartBoundary+"--", "");
				query = t.join("\r\n");
			}
		}while(false);

		// kwArgs.Connection = "close";

		var async = kwArgs["sync"] ? false : true;

		var preventCache = kwArgs["preventCache"] ||
			(this.preventCache == true && kwArgs["preventCache"] != false);
		var useCache = kwArgs["useCache"] == true ||
			(this.useCache == true && kwArgs["useCache"] != false );

		// preventCache is browser-level (add query string junk), useCache
		// is for the local cache. If we say preventCache, then don't attempt
		// to look in the cache, but if useCache is true, we still want to cache
		// the response
		if(!preventCache && useCache){
			var cachedHttp = getFromCache(url, query, kwArgs.method);
			if(cachedHttp){
				doLoad(kwArgs, cachedHttp, url, query, false);
				return;
			}
		}

		// much of this is from getText, but reproduced here because we need
		// more flexibility
		var http = dojo.hostenv.getXmlhttpObject(kwArgs);	
		var received = false;
		
		// Attached extra information to the "event"
		if(kwArgs['attach']){
			http['attach'] = kwArgs['attach'];
		}

		// build a handler function that calls back to the handler obj
		if(async){
			// FIXME: setting up this callback handler leaks on IE!!!
			this.inFlight.push({
				"req":		kwArgs,
				"http":		http,
				"url":		url,
				"query":	query,
				"useCache":	useCache
			});
			this.startWatchingInFlight();
		}

		if(kwArgs.method.toLowerCase() == "post"){
			// FIXME: need to hack in more flexible Content-Type setting here!
			http.open("POST", url, async);
			setHeaders(http, kwArgs);
			http.setRequestHeader("Content-Type", kwArgs.multipart ? ("multipart/form-data; boundary=" + this.multipartBoundary) : 
				(kwArgs.contentType || "application/x-www-form-urlencoded"));
			http.send(query);
		}else{
			var tmpUrl = url;
			if(query != "") {
				tmpUrl += (tmpUrl.indexOf("?") > -1 ? "&" : "?") + query;
			}
			if(preventCache) {
				tmpUrl += (dojo.string.endsWithAny(tmpUrl, "?", "&")
					? "" : (tmpUrl.indexOf("?") > -1 ? "&" : "?")) + "dojo.preventCache=" + new Date().valueOf();
			}
			http.open(kwArgs.method.toUpperCase(), tmpUrl, async);
			setHeaders(http, kwArgs);
			http.send(null);
		}

		if( !async ) {
			doLoad(kwArgs, http, url, query, useCache);
		}

		kwArgs.abort = function(){
			return http.abort();
		}

		return;
	}
	dojo.io.transports.addTransport("XMLHTTPTransport");
}

dojo.provide("dojo.event");

dojo.require("dojo.lang.array");
dojo.require("dojo.lang.extras");
dojo.require("dojo.lang.func");

dojo.event = new function(){
	this.canTimeout = dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);

	// FIXME: where should we put this method (not here!)?
	function interpolateArgs(args){
		var dl = dojo.lang;
		var ao = {
			srcObj: dj_global,
			srcFunc: null,
			adviceObj: dj_global,
			adviceFunc: null,
			aroundObj: null,
			aroundFunc: null,
			adviceType: (args.length>2) ? args[0] : "after",
			precedence: "last",
			once: false,
			delay: null,
			rate: 0,
			adviceMsg: false
		};

		switch(args.length){
			case 0: return;
			case 1: return;
			case 2:
				ao.srcFunc = args[0];
				ao.adviceFunc = args[1];
				break;
			case 3:
				if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isString(args[2]))){
					ao.adviceType = "after";
					ao.srcObj = args[0];
					ao.srcFunc = args[1];
					ao.adviceFunc = args[2];
				}else if((dl.isString(args[1]))&&(dl.isString(args[2]))){
					ao.srcFunc = args[1];
					ao.adviceFunc = args[2];
				}else if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isFunction(args[2]))){
					ao.adviceType = "after";
					ao.srcObj = args[0];
					ao.srcFunc = args[1];
					var tmpName  = dojo.lang.nameAnonFunc(args[2], ao.adviceObj);
					ao.adviceFunc = tmpName;
				}else if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){
					ao.adviceType = "after";
					ao.srcObj = dj_global;
					var tmpName  = dojo.lang.nameAnonFunc(args[0], ao.srcObj);
					ao.srcFunc = tmpName;
					ao.adviceObj = args[1];
					ao.adviceFunc = args[2];
				}
				break;
			case 4:
				if((dl.isObject(args[0]))&&(dl.isObject(args[2]))){
					// we can assume that we've got an old-style "connect" from
					// the sigslot school of event attachment. We therefore
					// assume after-advice.
					ao.adviceType = "after";
					ao.srcObj = args[0];
					ao.srcFunc = args[1];
					ao.adviceObj = args[2];
					ao.adviceFunc = args[3];
				}else if((dl.isString(args[0]))&&(dl.isString(args[1]))&&(dl.isObject(args[2]))){
					ao.adviceType = args[0];
					ao.srcObj = dj_global;
					ao.srcFunc = args[1];
					ao.adviceObj = args[2];
					ao.adviceFunc = args[3];
				}else if((dl.isString(args[0]))&&(dl.isFunction(args[1]))&&(dl.isObject(args[2]))){
					ao.adviceType = args[0];
					ao.srcObj = dj_global;
					var tmpName  = dojo.lang.nameAnonFunc(args[1], dj_global);
					ao.srcFunc = tmpName;
					ao.adviceObj = args[2];
					ao.adviceFunc = args[3];
				}else if(dl.isObject(args[1])){
					ao.srcObj = args[1];
					ao.srcFunc = args[2];
					ao.adviceObj = dj_global;
					ao.adviceFunc = args[3];
				}else if(dl.isObject(args[2])){
					ao.srcObj = dj_global;
					ao.srcFunc = args[1];
					ao.adviceObj = args[2];
					ao.adviceFunc = args[3];
				}else{
					ao.srcObj = ao.adviceObj = ao.aroundObj = dj_global;
					ao.srcFunc = args[1];
					ao.adviceFunc = args[2];
					ao.aroundFunc = args[3];
				}
				break;
			case 6:
				ao.srcObj = args[1];
				ao.srcFunc = args[2];
				ao.adviceObj = args[3]
				ao.adviceFunc = args[4];
				ao.aroundFunc = args[5];
				ao.aroundObj = dj_global;
				break;
			default:
				ao.srcObj = args[1];
				ao.srcFunc = args[2];
				ao.adviceObj = args[3]
				ao.adviceFunc = args[4];
				ao.aroundObj = args[5];
				ao.aroundFunc = args[6];
				ao.once = args[7];
				ao.delay = args[8];
				ao.rate = args[9];
				ao.adviceMsg = args[10];
				break;
		}

		if((typeof ao.srcFunc).toLowerCase() != "string"){
			ao.srcFunc = dojo.lang.getNameInObj(ao.srcObj, ao.srcFunc);
		}

		if((typeof ao.adviceFunc).toLowerCase() != "string"){
			ao.adviceFunc = dojo.lang.getNameInObj(ao.adviceObj, ao.adviceFunc);
		}

		if((ao.aroundObj)&&((typeof ao.aroundFunc).toLowerCase() != "string")){
			ao.aroundFunc = dojo.lang.getNameInObj(ao.aroundObj, ao.aroundFunc);
		}

		if(!ao.srcObj){
			dojo.raise("bad srcObj for srcFunc: "+ao.srcFunc);
		}
		if(!ao.adviceObj){
			dojo.raise("bad adviceObj for adviceFunc: "+ao.adviceFunc);
		}
		return ao;
	}

	this.connect = function(){
		var ao = interpolateArgs(arguments);

		// FIXME: just doing a "getForMethod()" seems to be enough to put this into infinite recursion!!
		var mjp = dojo.event.MethodJoinPoint.getForMethod(ao.srcObj, ao.srcFunc);
		if(ao.adviceFunc){
			var mjp2 = dojo.event.MethodJoinPoint.getForMethod(ao.adviceObj, ao.adviceFunc);
		}

		mjp.kwAddAdvice(ao);

		return mjp;	// advanced users might want to fsck w/ the join point
					// manually
	}

	this.connectBefore = function() {
		var args = ["before"];
		for(var i = 0; i < arguments.length; i++) { args.push(arguments[i]); }
		return this.connect.apply(this, args);
	}

	this.connectAround = function() {
		var args = ["around"];
		for(var i = 0; i < arguments.length; i++) { args.push(arguments[i]); }
		return this.connect.apply(this, args);
	}

	this._kwConnectImpl = function(kwArgs, disconnect){
		var fn = (disconnect) ? "disconnect" : "connect";
		if(typeof kwArgs["srcFunc"] == "function"){
			kwArgs.srcObj = kwArgs["srcObj"]||dj_global;
			var tmpName  = dojo.lang.nameAnonFunc(kwArgs.srcFunc, kwArgs.srcObj);
			kwArgs.srcFunc = tmpName;
		}
		if(typeof kwArgs["adviceFunc"] == "function"){
			kwArgs.adviceObj = kwArgs["adviceObj"]||dj_global;
			var tmpName  = dojo.lang.nameAnonFunc(kwArgs.adviceFunc, kwArgs.adviceObj);
			kwArgs.adviceFunc = tmpName;
		}
		return dojo.event[fn](	(kwArgs["type"]||kwArgs["adviceType"]||"after"),
									kwArgs["srcObj"]||dj_global,
									kwArgs["srcFunc"],
									kwArgs["adviceObj"]||kwArgs["targetObj"]||dj_global,
									kwArgs["adviceFunc"]||kwArgs["targetFunc"],
									kwArgs["aroundObj"],
									kwArgs["aroundFunc"],
									kwArgs["once"],
									kwArgs["delay"],
									kwArgs["rate"],
									kwArgs["adviceMsg"]||false );
	}

	this.kwConnect = function(kwArgs){
		return this._kwConnectImpl(kwArgs, false);

	}

	this.disconnect = function(){
		var ao = interpolateArgs(arguments);
		if(!ao.adviceFunc){ return; } // nothing to disconnect
		var mjp = dojo.event.MethodJoinPoint.getForMethod(ao.srcObj, ao.srcFunc);
		return mjp.removeAdvice(ao.adviceObj, ao.adviceFunc, ao.adviceType, ao.once);
	}

	this.kwDisconnect = function(kwArgs){
		return this._kwConnectImpl(kwArgs, true);
	}
}

// exactly one of these is created whenever a method with a joint point is run,
// if there is at least one 'around' advice.
dojo.event.MethodInvocation = function(join_point, obj, args) {
	this.jp_ = join_point;
	this.object = obj;
	this.args = [];
	for(var x=0; x<args.length; x++){
		this.args[x] = args[x];
	}
	// the index of the 'around' that is currently being executed.
	this.around_index = -1;
}

dojo.event.MethodInvocation.prototype.proceed = function() {
	this.around_index++;
	if(this.around_index >= this.jp_.around.length){
		return this.jp_.object[this.jp_.methodname].apply(this.jp_.object, this.args);
		// return this.jp_.run_before_after(this.object, this.args);
	}else{
		var ti = this.jp_.around[this.around_index];
		var mobj = ti[0]||dj_global;
		var meth = ti[1];
		return mobj[meth].call(mobj, this);
	}
} 


dojo.event.MethodJoinPoint = function(obj, methname){
	this.object = obj||dj_global;
	this.methodname = methname;
	this.methodfunc = this.object[methname];
	this.before = [];
	this.after = [];
	this.around = [];
}

dojo.event.MethodJoinPoint.getForMethod = function(obj, methname) {
	// if(!(methname in obj)){
	if(!obj){ obj = dj_global; }
	if(!obj[methname]){
		// supply a do-nothing method implementation
		obj[methname] = function(){};
	}else if((!dojo.lang.isFunction(obj[methname]))&&(!dojo.lang.isAlien(obj[methname]))){
		return null; // FIXME: should we throw an exception here instead?
	}
	// we hide our joinpoint instance in obj[methname + '$joinpoint']
	var jpname = methname + "$joinpoint";
	var jpfuncname = methname + "$joinpoint$method";
	var joinpoint = obj[jpname];
	if(!joinpoint){
		var isNode = false;
		if(dojo.event["browser"]){
			if( (obj["attachEvent"])||
				(obj["nodeType"])||
				(obj["addEventListener"]) ){
				isNode = true;
				dojo.event.browser.addClobberNodeAttrs(obj, [jpname, jpfuncname, methname]);
			}
		}
		obj[jpfuncname] = obj[methname];
		// joinpoint = obj[jpname] = new dojo.event.MethodJoinPoint(obj, methname);
		joinpoint = obj[jpname] = new dojo.event.MethodJoinPoint(obj, jpfuncname);
		obj[methname] = function(){ 
			var args = [];

			if((isNode)&&(!arguments.length)){
				var evt = null;
				try {
					if(obj.ownerDocument) {
						evt = obj.ownerDocument.parentWindow.event;
					} else if(obj.documentElement) {
						evt = obj.documentElement.ownerDocument.parentWindow.event;
					} else {
						evt = window.event;
					}
				} catch(E) {
					evt = window.event;
				}

				if(evt) {
					args.push(dojo.event.browser.fixEvent(evt));
				}
			}else{
				for(var x=0; x<arguments.length; x++){
					if((x==0)&&(isNode)&&(dojo.event.browser.isEvent(arguments[x]))){
						args.push(dojo.event.browser.fixEvent(arguments[x]));
					}else{
						args.push(arguments[x]);
					}
				}
			}
			// return joinpoint.run.apply(joinpoint, arguments); 
			return joinpoint.run.apply(joinpoint, args); 
		}
	}
	return joinpoint;
}

dojo.lang.extend(dojo.event.MethodJoinPoint, {
	unintercept: function() {
		this.object[this.methodname] = this.methodfunc;
	},

	run: function() {
		var obj = this.object||dj_global;
		var args = arguments;

		// optimization. We only compute once the array version of the arguments
		// pseudo-arr in order to prevent building it each time advice is unrolled.
		var aargs = [];
		for(var x=0; x<args.length; x++){
			aargs[x] = args[x];
		}

		var unrollAdvice  = function(marr){ 
			if(!marr){
				dojo.debug("Null argument to unrollAdvice()");
				return;
			}
		  
			var callObj = marr[0]||dj_global;
			var callFunc = marr[1];
			
			if(!callObj[callFunc]){
				dojo.raise("function \"" + callFunc + "\" does not exist on \"" + callObj + "\"");
			}
			
			var aroundObj = marr[2]||dj_global;
			var aroundFunc = marr[3];
			var msg = marr[6];
			var undef;

			var to = {
				args: [],
				jp_: this,
				object: obj,
				proceed: function(){
					return callObj[callFunc].apply(callObj, to.args);
				}
			};
			to.args = aargs;

			var delay = parseInt(marr[4]);
			var hasDelay = ((!isNaN(delay))&&(marr[4]!==null)&&(typeof marr[4] != "undefined"));
			if(marr[5]){
				var rate = parseInt(marr[5]);
				var cur = new Date();
				var timerSet = false;
				if((marr["last"])&&((cur-marr.last)<=rate)){
					if(dojo.event.canTimeout){
						if(marr["delayTimer"]){
							clearTimeout(marr.delayTimer);
						}
						var tod = parseInt(rate*2); // is rate*2 naive?
						var mcpy = dojo.lang.shallowCopy(marr);
						marr.delayTimer = setTimeout(function(){
							// FIXME: on IE at least, event objects from the
							// browser can go out of scope. How (or should?) we
							// deal with it?
							mcpy[5] = 0;
							unrollAdvice(mcpy);
						}, tod);
					}
					return;
				}else{
					marr.last = cur;
				}
			}

			// FIXME: need to enforce rates for a connection here!

			if(aroundFunc){
				// NOTE: around advice can't delay since we might otherwise depend
				// on execution order!
				aroundObj[aroundFunc].call(aroundObj, to);
			}else{
				// var tmjp = dojo.event.MethodJoinPoint.getForMethod(obj, methname);
				if((hasDelay)&&((dojo.render.html)||(dojo.render.svg))){  // FIXME: the render checks are grotty!
					dj_global["setTimeout"](function(){
						if(msg){
							callObj[callFunc].call(callObj, to); 
						}else{
							callObj[callFunc].apply(callObj, args); 
						}
					}, delay);
				}else{ // many environments can't support delay!
					if(msg){
						callObj[callFunc].call(callObj, to); 
					}else{
						callObj[callFunc].apply(callObj, args); 
					}
				}
			}
		}

		if(this.before.length>0){
			dojo.lang.forEach(this.before, unrollAdvice, true);
		}

		var result;
		if(this.around.length>0){
			var mi = new dojo.event.MethodInvocation(this, obj, args);
			result = mi.proceed();
		}else if(this.methodfunc){
			result = this.object[this.methodname].apply(this.object, args);
		}

		if(this.after.length>0){
			dojo.lang.forEach(this.after, unrollAdvice, true);
		}

		return (this.methodfunc) ? result : null;
	},

	getArr: function(kind){
		var arr = this.after;
		// FIXME: we should be able to do this through props or Array.in()
		if((typeof kind == "string")&&(kind.indexOf("before")!=-1)){
			arr = this.before;
		}else if(kind=="around"){
			arr = this.around;
		}
		return arr;
	},

	kwAddAdvice: function(args){
		this.addAdvice(	args["adviceObj"], args["adviceFunc"], 
						args["aroundObj"], args["aroundFunc"], 
						args["adviceType"], args["precedence"], 
						args["once"], args["delay"], args["rate"], 
						args["adviceMsg"]);
	},

	addAdvice: function(	thisAdviceObj, thisAdvice, 
							thisAroundObj, thisAround, 
							advice_kind, precedence, 
							once, delay, rate, asMessage){
		var arr = this.getArr(advice_kind);
		if(!arr){
			dojo.raise("bad this: " + this);
		}

		var ao = [thisAdviceObj, thisAdvice, thisAroundObj, thisAround, delay, rate, asMessage];
		
		if(once){
			if(this.hasAdvice(thisAdviceObj, thisAdvice, advice_kind, arr) >= 0){
				return;
			}
		}

		if(precedence == "first"){
			arr.unshift(ao);
		}else{
			arr.push(ao);
		}
	},

	hasAdvice: function(thisAdviceObj, thisAdvice, advice_kind, arr){
		if(!arr){ arr = this.getArr(advice_kind); }
		var ind = -1;
		for(var x=0; x<arr.length; x++){
			if((arr[x][0] == thisAdviceObj)&&(arr[x][1] == thisAdvice)){
				ind = x;
			}
		}
		return ind;
	},

	removeAdvice: function(thisAdviceObj, thisAdvice, advice_kind, once){
		var arr = this.getArr(advice_kind);
		var ind = this.hasAdvice(thisAdviceObj, thisAdvice, advice_kind, arr);
		if(ind == -1){
			return false;
		}
		while(ind != -1){
			arr.splice(ind, 1);
			if(once){ break; }
			ind = this.hasAdvice(thisAdviceObj, thisAdvice, advice_kind, arr);
		}
		return true;
	}
});

dojo.require("dojo.event");
dojo.provide("dojo.event.topic");

dojo.event.topic = new function(){
	this.topics = {};

	this.getTopic = function(topicName){
		if(!this.topics[topicName]){
			this.topics[topicName] = new this.TopicImpl(topicName);
		}
		return this.topics[topicName];
	}

	this.registerPublisher = function(topic, obj, funcName){
		var topic = this.getTopic(topic);
		topic.registerPublisher(obj, funcName);
	}

	this.subscribe = function(topic, obj, funcName){
		var topic = this.getTopic(topic);
		topic.subscribe(obj, funcName);
	}

	this.unsubscribe = function(topic, obj, funcName){
		var topic = this.getTopic(topic);
		topic.unsubscribe(obj, funcName);
	}

	this.publish = function(topic, message){
		var topic = this.getTopic(topic);
		// if message is an array, we treat it as a set of arguments,
		// otherwise, we just pass on the arguments passed in as-is
		var args = [];
		if(arguments.length == 2 && (dojo.lang.isArray(message) || message.callee)){
			args = message;
		}else{
			var args = [];
			for(var x=1; x<arguments.length; x++){
				args.push(arguments[x]);
			}
		}
		topic.sendMessage.apply(topic, args);
	}
}

dojo.event.topic.TopicImpl = function(topicName){
	this.topicName = topicName;
	var self = this;

	self.subscribe = function(listenerObject, listenerMethod){
		var tf = listenerMethod||listenerObject;
		var to = (!listenerMethod) ? dj_global : listenerObject;
		dojo.event.kwConnect({
			srcObj:		self, 
			srcFunc:	"sendMessage", 
			adviceObj:	to,
			adviceFunc: tf
		});
	}

	self.unsubscribe = function(listenerObject, listenerMethod){
		var tf = (!listenerMethod) ? listenerObject : listenerMethod;
		var to = (!listenerMethod) ? null : listenerObject;
		dojo.event.kwDisconnect({
			srcObj:		self, 
			srcFunc:	"sendMessage", 
			adviceObj:	to,
			adviceFunc: tf
		});
	}

	self.registerPublisher = function(publisherObject, publisherMethod){
		dojo.event.connect(publisherObject, publisherMethod, self, "sendMessage");
	}

	self.sendMessage = function(message){
		// The message has been propagated
	}
}


dojo.provide("dojo.event.browser");
dojo.require("dojo.event");

dojo_ie_clobber = new function(){
	this.clobberNodes = [];

	function nukeProp(node, prop){
		// try{ node.removeAttribute(prop); 	}catch(e){ /* squelch */ }
		try{ node[prop] = null; 			}catch(e){ /* squelch */ }
		try{ delete node[prop]; 			}catch(e){ /* squelch */ }
		// FIXME: JotLive needs this, but I'm not sure if it's too slow or not
		try{ node.removeAttribute(prop);	}catch(e){ /* squelch */ }
	}

	this.clobber = function(nodeRef){
		var na;
		var tna;
		if(nodeRef){
			tna = nodeRef.getElementsByTagName("*");
			na = [nodeRef];
			for(var x=0; x<tna.length; x++){
				// if we're gonna be clobbering the thing, at least make sure
				// we aren't trying to do it twice
				if(tna[x]["__doClobber__"]){
					na.push(tna[x]);
				}
			}
		}else{
			try{ window.onload = null; }catch(e){}
			na = (this.clobberNodes.length) ? this.clobberNodes : document.all;
		}
		tna = null;
		var basis = {};
		for(var i = na.length-1; i>=0; i=i-1){
			var el = na[i];
			if(el["__clobberAttrs__"]){
				for(var j=0; j<el.__clobberAttrs__.length; j++){
					nukeProp(el, el.__clobberAttrs__[j]);
				}
				nukeProp(el, "__clobberAttrs__");
				nukeProp(el, "__doClobber__");
			}
		}
		na = null;
	}
}

if(dojo.render.html.ie){
	window.onunload = function(){
		dojo_ie_clobber.clobber();
		try{
			if((dojo["widget"])&&(dojo.widget["manager"])){
				dojo.widget.manager.destroyAll();
			}
		}catch(e){}
		try{ window.onload = null; }catch(e){}
		try{ window.onunload = null; }catch(e){}
		dojo_ie_clobber.clobberNodes = [];
		// CollectGarbage();
	}
}

dojo.event.browser = new function(){

	var clobberIdx = 0;

	this.clean = function(node){
		if(dojo.render.html.ie){ 
			dojo_ie_clobber.clobber(node);
		}
	}

	this.addClobberNode = function(node){
		if(!node["__doClobber__"]){
			node.__doClobber__ = true;
			dojo_ie_clobber.clobberNodes.push(node);
			// this might not be the most efficient thing to do, but it's
			// much less error prone than other approaches which were
			// previously tried and failed
			node.__clobberAttrs__ = [];
		}
	}

	this.addClobberNodeAttrs = function(node, props){
		this.addClobberNode(node);
		for(var x=0; x<props.length; x++){
			node.__clobberAttrs__.push(props[x]);
		}
	}

	this.removeListener = function(node, evtName, fp, capture){
		if(!capture){ var capture = false; }
		evtName = evtName.toLowerCase();
		if(evtName.substr(0,2)=="on"){ evtName = evtName.substr(2); }
		// FIXME: this is mostly a punt, we aren't actually doing anything on IE
		if(node.removeEventListener){
			node.removeEventListener(evtName, fp, capture);
		}
	}

	this.addListener = function(node, evtName, fp, capture, dontFix){
		if(!node){ return; } // FIXME: log and/or bail?
		if(!capture){ var capture = false; }
		evtName = evtName.toLowerCase();
		if(evtName.substr(0,2)!="on"){ evtName = "on"+evtName; }

		if(!dontFix){
			// build yet another closure around fp in order to inject fixEvent
			// around the resulting event
			var newfp = function(evt){
				if(!evt){ evt = window.event; }
				var ret = fp(dojo.event.browser.fixEvent(evt));
				if(capture){
					dojo.event.browser.stopEvent(evt);
				}
				return ret;
			}
		}else{
			newfp = fp;
		}

		if(node.addEventListener){ 
			node.addEventListener(evtName.substr(2), newfp, capture);
			return newfp;
		}else{
			if(typeof node[evtName] == "function" ){
				var oldEvt = node[evtName];
				node[evtName] = function(e){
					oldEvt(e);
					return newfp(e);
				}
			}else{
				node[evtName]=newfp;
			}
			if(dojo.render.html.ie){
				this.addClobberNodeAttrs(node, [evtName]);
			}
			return newfp;
		}
	}

	this.isEvent = function(obj){
		// FIXME: event detection hack ... could test for additional attributes
		// if necessary
		return (typeof obj != "undefined")&&(typeof Event != "undefined")&&(obj.eventPhase);
		// Event does not support instanceof in Opera, otherwise:
		//return (typeof Event != "undefined")&&(obj instanceof Event);
	}

	this.currentEvent = null;
	
	this.callListener = function(listener, curTarget){
		if(typeof listener != 'function'){
			dojo.raise("listener not a function: " + listener);
		}
		dojo.event.browser.currentEvent.currentTarget = curTarget;
		return listener.call(curTarget, dojo.event.browser.currentEvent);
	}

	this.stopPropagation = function(){
		dojo.event.browser.currentEvent.cancelBubble = true;
	}

	this.preventDefault = function(){
	  dojo.event.browser.currentEvent.returnValue = false;
	}

	this.keys = {
		KEY_BACKSPACE: 8,
		KEY_TAB: 9,
		KEY_ENTER: 13,
		KEY_SHIFT: 16,
		KEY_CTRL: 17,
		KEY_ALT: 18,
		KEY_PAUSE: 19,
		KEY_CAPS_LOCK: 20,
		KEY_ESCAPE: 27,
		KEY_SPACE: 32,
		KEY_PAGE_UP: 33,
		KEY_PAGE_DOWN: 34,
		KEY_END: 35,
		KEY_HOME: 36,
		KEY_LEFT_ARROW: 37,
		KEY_UP_ARROW: 38,
		KEY_RIGHT_ARROW: 39,
		KEY_DOWN_ARROW: 40,
		KEY_INSERT: 45,
		KEY_DELETE: 46,
		KEY_LEFT_WINDOW: 91,
		KEY_RIGHT_WINDOW: 92,
		KEY_SELECT: 93,
		KEY_F1: 112,
		KEY_F2: 113,
		KEY_F3: 114,
		KEY_F4: 115,
		KEY_F5: 116,
		KEY_F6: 117,
		KEY_F7: 118,
		KEY_F8: 119,
		KEY_F9: 120,
		KEY_F10: 121,
		KEY_F11: 122,
		KEY_F12: 123,
		KEY_NUM_LOCK: 144,
		KEY_SCROLL_LOCK: 145
	};

	// reverse lookup
	this.revKeys = [];
	for(var key in this.keys){
		this.revKeys[this.keys[key]] = key;
	}

	this.fixEvent = function(evt){
		if((!evt)&&(window["event"])){
			var evt = window.event;
		}
		
		if((evt["type"])&&(evt["type"].indexOf("key") == 0)){ // key events
			evt.keys = this.revKeys;
			// FIXME: how can we eliminate this iteration?
			for(var key in this.keys) {
				evt[key] = this.keys[key];
			}
			if((dojo.render.html.ie)&&(evt["type"] == "keypress")){
				evt.charCode = evt.keyCode;
			}
		}
	
		if(dojo.render.html.ie){
			if(!evt.target){ evt.target = evt.srcElement; }
			if(!evt.currentTarget){ evt.currentTarget = evt.srcElement; }
			if(!evt.layerX){ evt.layerX = evt.offsetX; }
			if(!evt.layerY){ evt.layerY = evt.offsetY; }
			// mouseover
			if(evt.fromElement){ evt.relatedTarget = evt.fromElement; }
			// mouseout
			if(evt.toElement){ evt.relatedTarget = evt.toElement; }
			this.currentEvent = evt;
			evt.callListener = this.callListener;
			evt.stopPropagation = this.stopPropagation;
			evt.preventDefault = this.preventDefault;
		}
		return evt;
	}

	this.stopEvent = function(ev) {
		if(window.event){
			ev.returnValue = false;
			ev.cancelBubble = true;
		}else{
			ev.preventDefault();
			ev.stopPropagation();
		}
	}
}

dojo.hostenv.conditionalLoadModule({
	common: ["dojo.event", "dojo.event.topic"],
	browser: ["dojo.event.browser"],
	dashboard: ["dojo.event.browser"]
});
dojo.hostenv.moduleLoaded("dojo.event.*");

dojo.provide("dojo.lang.assert");

dojo.require("dojo.lang.common");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.type");

// -------------------------------------------------------------------
// Assertion methods
// -------------------------------------------------------------------

/**
 * Throws an exception if the assertion fails.
 *
 * If the asserted condition is true, this method does nothing. If the
 * condition is false, we throw an error with a error message.  
 *
 * @param	booleanValue	A boolean value, which needs to be true for the assertion to succeed.
 * @param	message	Optional. A string describing the assertion.
 * @throws	Throws an Error if 'booleanValue' is false.
 */
dojo.lang.assert = function(booleanValue, message){
	if(!booleanValue){
		var errorMessage = "An assert statement failed.\n" +
			"The method dojo.lang.assert() was called with a 'false' value.\n";
		if(message){
			errorMessage += "Here's the assert message:\n" + message + "\n";
		}
		// Use throw instead of dojo.raise, until bug #264 is fixed:
		// dojo.raise(errorMessage);
		throw new Error(errorMessage);
	}
}

/**
 * Given a value and a data type, this method checks the type of the value
 * to make sure it matches the data type, and throws an exception if there
 * is a mismatch.
 *
 * Examples:
 * <pre>
 *   dojo.lang.assertType("foo", String);
 *   dojo.lang.assertType(12345, Number);
 *   dojo.lang.assertType(false, Boolean);
 *   dojo.lang.assertType([6, 8], Array);
 *   dojo.lang.assertType(dojo.lang.assertType, Function);
 *   dojo.lang.assertType({foo: "bar"}, Object);
 *   dojo.lang.assertType(new Date(), Date);
 * </pre>
 *
 * @scope	public function
 * @param	value	Any literal value or object instance.
 * @param	type	A class of object, or a literal type, or the string name of a type, or an array with a list of types.
 * @param	message	Optional. A string describing the assertion.
 * @throws	Throws an Error if 'value' is not of type 'type'.
 */
dojo.lang.assertType = function(value, type, message){
	if(!dojo.lang.isOfType(value, type)){
		if(!message){
			if(!dojo.lang.assertType._errorMessage){
				dojo.lang.assertType._errorMessage = "Type mismatch: dojo.lang.assertType() failed.";
			}
			message = dojo.lang.assertType._errorMessage;
		}
		dojo.lang.assert(false, message);
	}
}

/**
 * Given an anonymous object and a list of expected property names, this
 * method check to make sure the object does not have any properties
 * that aren't on the list of expected properties, and throws an Error
 * if there are unexpected properties. This is useful for doing error
 * checking on keyword arguments, to make sure there aren't typos.
 *
 * Examples:
 * <pre>
 *   dojo.lang.assertValidKeywords({a: 1, b: 2}, ["a", "b"]);
 *   dojo.lang.assertValidKeywords({a: 1, b: 2}, ["a", "b", "c"]);
 *   dojo.lang.assertValidKeywords({foo: "iggy"}, ["foo"]);
 *   dojo.lang.assertValidKeywords({foo: "iggy"}, ["foo", "bar"]);
 *   dojo.lang.assertValidKeywords({foo: "iggy"}, {foo: null, bar: null});
 * </pre>
 *
 * @scope	public function
 * @param	object	An anonymous object.
 * @param	expectedProperties	An array of strings (or an object with all the expected properties).
 * @param	message	Optional. A string describing the assertion.
 * @throws	Throws an Error if 'value' is not of type 'type'.
 */
dojo.lang.assertValidKeywords = function(object, expectedProperties, message){
	var key;
	if(!message){
		if(!dojo.lang.assertValidKeywords._errorMessage){
			dojo.lang.assertValidKeywords._errorMessage = "In dojo.lang.assertValidKeywords(), found invalid keyword:";
		}
		message = dojo.lang.assertValidKeywords._errorMessage;
	}
	if(dojo.lang.isArray(expectedProperties)){
		for(key in object){
			if(!dojo.lang.inArray(expectedProperties, key)){
				dojo.lang.assert(false, message + " " + key);
			}
		}
	}else{
		for(key in object){
			if(!(key in expectedProperties)){
				dojo.lang.assert(false, message + " " + key);
			}
		}
	}
}

dojo.provide("dojo.AdapterRegistry");
dojo.require("dojo.lang.func");
dojo.require("dojo.lang.type");

dojo.AdapterRegistry = function(){
    /***
        A registry to facilitate adaptation.

        Pairs is an array of [name, check, wrap] triples
        
        All check/wrap functions in this registry should be of the same arity.
    ***/
    this.pairs = [];
}

dojo.lang.extend(dojo.AdapterRegistry, {
    register: function (name, check, wrap, /* optional */ override){
        /***
			The check function should return true if the given arguments are
			appropriate for the wrap function.

			If override is given and true, the check function will be given
			highest priority.  Otherwise, it will be the lowest priority
			adapter.
        ***/

        if (override) {
            this.pairs.unshift([name, check, wrap]);
        } else {
            this.pairs.push([name, check, wrap]);
        }
    },

    match: function (/* ... */) {
        /***
			Find an adapter for the given arguments.

			If no suitable adapter is found, throws NotFound.
        ***/
        for(var i = 0; i < this.pairs.length; i++){
            var pair = this.pairs[i];
            if(pair[1].apply(this, arguments)){
                return pair[2].apply(this, arguments);
            }
        }
		throw new Error("No match found");
        // dojo.raise("No match found");
    },

    unregister: function (name) {
        /***
			Remove a named adapter from the registry
        ***/
        for(var i = 0; i < this.pairs.length; i++){
            var pair = this.pairs[i];
            if(pair[0] == name){
                this.pairs.splice(i, 1);
                return true;
            }
        }
        return false;
    }
});

dojo.provide("dojo.lang.repr");

dojo.require("dojo.lang.common");
dojo.require("dojo.AdapterRegistry");

dojo.lang.reprRegistry = new dojo.AdapterRegistry();
dojo.lang.registerRepr = function(name, check, wrap, /*optional*/ override){
        /***
			Register a repr function.  repr functions should take
			one argument and return a string representation of it
			suitable for developers, primarily used when debugging.

			If override is given, it is used as the highest priority
			repr, otherwise it will be used as the lowest.
        ***/
        dojo.lang.reprRegistry.register(name, check, wrap, override);
    };

dojo.lang.repr = function(obj){
	/***
		Return a "programmer representation" for an object
	***/
	if(typeof(obj) == "undefined"){
		return "undefined";
	}else if(obj === null){
		return "null";
	}

	try{
		if(typeof(obj["__repr__"]) == 'function'){
			return obj["__repr__"]();
		}else if((typeof(obj["repr"]) == 'function')&&(obj.repr != arguments.callee)){
			return obj["repr"]();
		}
		return dojo.lang.reprRegistry.match(obj);
	}catch(e){
		if(typeof(obj.NAME) == 'string' && (
				obj.toString == Function.prototype.toString ||
				obj.toString == Object.prototype.toString
			)){
			return o.NAME;
		}
	}

	if(typeof(obj) == "function"){
		obj = (obj + "").replace(/^\s+/, "");
		var idx = obj.indexOf("{");
		if(idx != -1){
			obj = obj.substr(0, idx) + "{...}";
		}
	}
	return obj + "";
}

dojo.lang.reprArrayLike = function(arr){
	try{
		var na = dojo.lang.map(arr, dojo.lang.repr);
		return "[" + na.join(", ") + "]";
	}catch(e){ }
};

dojo.lang.reprString = function(str){ 
	return ('"' + str.replace(/(["\\])/g, '\\$1') + '"'
		).replace(/[\f]/g, "\\f"
		).replace(/[\b]/g, "\\b"
		).replace(/[\n]/g, "\\n"
		).replace(/[\t]/g, "\\t"
		).replace(/[\r]/g, "\\r");
};

dojo.lang.reprNumber = function(num){
	return num + "";
};

(function(){
	var m = dojo.lang;
	m.registerRepr("arrayLike", m.isArrayLike, m.reprArrayLike);
	m.registerRepr("string", m.isString, m.reprString);
	m.registerRepr("numbers", m.isNumber, m.reprNumber);
	m.registerRepr("boolean", m.isBoolean, m.reprNumber);
	// m.registerRepr("numbers", m.typeMatcher("number", "boolean"), m.reprNumber);
})();

dojo.hostenv.conditionalLoadModule({
	common: [
		"dojo.lang",
		"dojo.lang.common",
		"dojo.lang.assert",
		"dojo.lang.array",
		"dojo.lang.type",
		"dojo.lang.func",
		"dojo.lang.extras",
		"dojo.lang.repr"
	]
});
dojo.hostenv.moduleLoaded("dojo.lang.*");

dojo.provide("dojo.widget.Manager");
dojo.require("dojo.lang.array");
dojo.require("dojo.event.*");

// Manager class
dojo.widget.manager = new function(){
	this.widgets = [];
	this.widgetIds = [];
	
	// map of widgetId-->widget for widgets without parents (top level widgets)
	this.topWidgets = {};

	var widgetTypeCtr = {};
	var renderPrefixCache = [];

	this.getUniqueId = function (widgetType) {
		return widgetType + "_" + (widgetTypeCtr[widgetType] != undefined ?
			++widgetTypeCtr[widgetType] : widgetTypeCtr[widgetType] = 0);
	}

	this.add = function(widget){
		dojo.profile.start("dojo.widget.manager.add");
		this.widgets.push(widget);
		// FIXME: the rest of this method is very slow!
		if(widget.widgetId == ""){
			if(widget["id"]){
				widget.widgetId = widget["id"];
			}else if(widget.extraArgs["id"]){
				widget.widgetId = widget.extraArgs["id"];
			}else{
				widget.widgetId = this.getUniqueId(widget.widgetType);
			}
		}
		if(this.widgetIds[widget.widgetId]){
			dojo.debug("widget ID collision on ID: "+widget.widgetId);
		}
		this.widgetIds[widget.widgetId] = widget;
		// Widget.destroy already calls removeById(), so we don't need to
		// connect() it here
		dojo.profile.end("dojo.widget.manager.add");
	}

	this.destroyAll = function(){
		for(var x=this.widgets.length-1; x>=0; x--){
			try{
				// this.widgets[x].destroyChildren();
				this.widgets[x].destroy(true);
				delete this.widgets[x];
			}catch(e){ }
		}
	}

	// FIXME: we should never allow removal of the root widget until all others
	// are removed!
	this.remove = function(widgetIndex){
		var tw = this.widgets[widgetIndex].widgetId;
		delete this.widgetIds[tw];
		this.widgets.splice(widgetIndex, 1);
	}
	
	// FIXME: suboptimal performance
	this.removeById = function(id) {
		for (var i=0; i<this.widgets.length; i++){
			if(this.widgets[i].widgetId == id){
				this.remove(i);
				break;
			}
		}
	}

	this.getWidgetById = function(id){
		return this.widgetIds[id];
	}

	this.getWidgetsByType = function(type){
		var lt = type.toLowerCase();
		var ret = [];
		dojo.lang.forEach(this.widgets, function(x){
			if(x.widgetType.toLowerCase() == lt){
				ret.push(x);
			}
		});
		return ret;
	}

	this.getWidgetsOfType = function (id) {
		dj_deprecated("getWidgetsOfType is depecrecated, use getWidgetsByType");
		return dojo.widget.manager.getWidgetsByType(id);
	}

	this.getWidgetsByFilter = function(unaryFunc){
		var ret = [];
		dojo.lang.forEach(this.widgets, function(x){
			if(unaryFunc(x)){
				ret.push(x);
			}
		});
		return ret;
	}

	this.getAllWidgets = function() {
		return this.widgets.concat();
	}

	//	added, trt 2006-01-20
	this.getWidgetByNode = function(/* DOMNode */ node){
		var w=this.getAllWidgets();
		for (var i=0; i<w.length; i++){
			if (w[i].domNode==node){
				return w[i];
			}
		}
		return null;
	}

	// shortcuts, baby
	this.byId = this.getWidgetById;
	this.byType = this.getWidgetsByType;
	this.byFilter = this.getWidgetsByFilter;
	this.byNode = this.getWidgetByNode;

	// map of previousally discovered implementation names to constructors
	var knownWidgetImplementations = {};

	// support manually registered widget packages
	var widgetPackages = ["dojo.widget"];
	for (var i=0; i<widgetPackages.length; i++) {
		// convenience for checking if a package exists (reverse lookup)
		widgetPackages[widgetPackages[i]] = true;
	}

	this.registerWidgetPackage = function(pname) {
		if(!widgetPackages[pname]){
			widgetPackages[pname] = true;
			widgetPackages.push(pname);
		}
	}
	
	this.getWidgetPackageList = function() {
		return dojo.lang.map(widgetPackages, function(elt) { return(elt!==true ? elt : undefined); });
	}
	
	this.getImplementation = function(widgetName, ctorObject, mixins){
		// try and find a name for the widget
		var impl = this.getImplementationName(widgetName);
		if(impl){ 
			// var tic = new Date();
			var ret = new impl(ctorObject);
			// dojo.debug(new Date() - tic);
			return ret;
		}
	}

	this.getImplementationName = function(widgetName){
		/*
		 * This is the overly-simplistic implemention of getImplementation (har
		 * har). In the future, we are going to want something that allows more
		 * freedom of expression WRT to specifying different specializations of
		 * a widget.
		 *
		 * Additionally, this implementation treats widget names as case
		 * insensitive, which does not necessarialy mesh with the markup which
		 * can construct a widget.
		 */

		var lowerCaseWidgetName = widgetName.toLowerCase();

		var impl = knownWidgetImplementations[lowerCaseWidgetName];
		if(impl){
			return impl;
		}

		// first store a list of the render prefixes we are capable of rendering
		if(!renderPrefixCache.length){
			for(var renderer in dojo.render){
				if(dojo.render[renderer]["capable"] === true){
					var prefixes = dojo.render[renderer].prefixes;
					for(var i = 0; i < prefixes.length; i++){
						renderPrefixCache.push(prefixes[i].toLowerCase());
					}
				}
			}
			// make sure we don't HAVE to prefix widget implementation names
			// with anything to get them to render
			renderPrefixCache.push("");
		}

		// look for a rendering-context specific version of our widget name
		for(var i = 0; i < widgetPackages.length; i++){
			var widgetPackage = dojo.evalObjPath(widgetPackages[i]);
			if(!widgetPackage) { continue; }

			for (var j = 0; j < renderPrefixCache.length; j++) {
				if (!widgetPackage[renderPrefixCache[j]]) { continue; }
				for (var widgetClass in widgetPackage[renderPrefixCache[j]]) {
					if (widgetClass.toLowerCase() != lowerCaseWidgetName) { continue; }
					knownWidgetImplementations[lowerCaseWidgetName] =
						widgetPackage[renderPrefixCache[j]][widgetClass];
					return knownWidgetImplementations[lowerCaseWidgetName];
				}
			}

			for (var j = 0; j < renderPrefixCache.length; j++) {
				for (var widgetClass in widgetPackage) {
					if (widgetClass.toLowerCase() !=
						(renderPrefixCache[j] + lowerCaseWidgetName)) { continue; }
	
					knownWidgetImplementations[lowerCaseWidgetName] =
						widgetPackage[widgetClass];
					return knownWidgetImplementations[lowerCaseWidgetName];
				}
			}
		}
		
		throw new Error('Could not locate "' + widgetName + '" class');
	}

	// FIXME: does it even belong in this name space?
	// NOTE: this method is implemented by DomWidget.js since not all
	// hostenv's would have an implementation.
	/*this.getWidgetFromPrimitive = function(baseRenderType){
		dj_unimplemented("dojo.widget.manager.getWidgetFromPrimitive");
	}

	this.getWidgetFromEvent = function(nativeEvt){
		dj_unimplemented("dojo.widget.manager.getWidgetFromEvent");
	}*/

	// Catch window resize events and notify top level widgets
	this.resizing=false;
	this.onResized = function(){
		if(this.resizing){
			return;	// duplicate event
		}
		try{
			this.resizing=true;
			for(var id in this.topWidgets){
				var child = this.topWidgets[id];
				//dojo.debug("root resizing child " + child.widgetId);
				if(child.onResized){
					child.onResized();
				}
			}
		}catch(e){
		}finally{
			this.resizing=false;
		}
	}
	if(typeof window != "undefined") {
		dojo.addOnLoad(this, 'onResized');							// initial sizing
		dojo.event.connect(window, 'onresize', this, 'onResized');	// window resize
	}

	// FIXME: what else?
}

// copy the methods from the default manager (this) to the widget namespace
dojo.widget.getUniqueId = function(){ return dojo.widget.manager.getUniqueId.apply(dojo.widget.manager, arguments); }
dojo.widget.addWidget = function(){ return dojo.widget.manager.add.apply(dojo.widget.manager, arguments); }
dojo.widget.destroyAllWidgets = function(){ return dojo.widget.manager.destroyAll.apply(dojo.widget.manager, arguments); }
dojo.widget.removeWidget = function(){ return dojo.widget.manager.remove.apply(dojo.widget.manager, arguments); }
dojo.widget.removeWidgetById = function(){ return dojo.widget.manager.removeById.apply(dojo.widget.manager, arguments); }
dojo.widget.getWidgetById = function(){ return dojo.widget.manager.getWidgetById.apply(dojo.widget.manager, arguments); }
dojo.widget.getWidgetsByType = function(){ return dojo.widget.manager.getWidgetsByType.apply(dojo.widget.manager, arguments); }
dojo.widget.getWidgetsByFilter = function(){ return dojo.widget.manager.getWidgetsByFilter.apply(dojo.widget.manager, arguments); }
dojo.widget.byId = function(){ return dojo.widget.manager.getWidgetById.apply(dojo.widget.manager, arguments); }
dojo.widget.byType = function(){ return dojo.widget.manager.getWidgetsByType.apply(dojo.widget.manager, arguments); }
dojo.widget.byFilter = function(){ return dojo.widget.manager.getWidgetsByFilter.apply(dojo.widget.manager, arguments); }
dojo.widget.byNode = function(){ return dojo.widget.manager.getWidgetByNode.apply(dojo.widget.manager, arguments); }
dojo.widget.all = function(n){
	var widgets = dojo.widget.manager.getAllWidgets.apply(dojo.widget.manager, arguments);
	if(arguments.length > 0) {
		return widgets[n];
	}
	return widgets;
}
dojo.widget.registerWidgetPackage = function(){ return dojo.widget.manager.registerWidgetPackage.apply(dojo.widget.manager, arguments); }
dojo.widget.getWidgetImplementation = function(){ return dojo.widget.manager.getImplementation.apply(dojo.widget.manager, arguments); }
dojo.widget.getWidgetImplementationName = function(){ return dojo.widget.manager.getImplementationName.apply(dojo.widget.manager, arguments); }

dojo.widget.widgets = dojo.widget.manager.widgets;
dojo.widget.widgetIds = dojo.widget.manager.widgetIds;
dojo.widget.root = dojo.widget.manager.root;

