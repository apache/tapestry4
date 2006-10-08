if(typeof dojo == "undefined"){

/**
* @file bootstrap1.js
*
* summary: First file that is loaded that 'bootstraps' the entire dojo library suite.
* note:  Must run before hostenv_*.js file.
*
* @author  Copyright 2004 Mark D. Anderson (mda@discerning.com)
* TODOC: should the copyright be changed to Dojo Foundation?
* @license Licensed under the Academic Free License 2.1 http://www.opensource.org/licenses/afl-2.1.php
*
* $Id: bootstrap1.js 5779 2006-09-24 02:22:48Z peller $
*/

// TODOC: HOW TO DOC THE BELOW?
// @global: djConfig
// summary:  
//		Application code can set the global 'djConfig' prior to loading
//		the library to override certain global settings for how dojo works.  
// description:  The variables that can be set are as follows:
//			- isDebug: false
//			- allowQueryConfig: false
//			- baseScriptUri: ""
//			- baseRelativePath: ""
//			- libraryScriptUri: ""
//			- iePreventClobber: false
//			- ieClobberMinimal: true
//			- locale: undefined
//			- extraLocale: undefined
//			- preventBackButtonFix: true
//			- searchIds: []
//			- parseWidgets: true
// TODOC: HOW TO DOC THESE VARIABLES?
// TODOC: IS THIS A COMPLETE LIST?
// note:
//		'djConfig' does not exist under 'dojo.*' so that it can be set before the 
//		'dojo' variable exists.  
// note:
//		Setting any of these variables *after* the library has loaded does nothing at all. 
// TODOC: is this still true?  Release notes for 0.3 indicated they could be set after load.
//


//TODOC:  HOW TO DOC THIS?
// @global: dj_global
// summary: 
//		an alias for the top-level global object in the host environment
//		(e.g., the window object in a browser).
// description:  
//		Refer to 'dj_global' rather than referring to window to ensure your
//		code runs correctly in contexts other than web browsers (eg: Rhino on a server).
var dj_global = this;

//TODOC:  HOW TO DOC THIS?
// @global: dj_currentContext
// summary: 
//		Private global context object. Where 'dj_global' always refers to the boot-time
//    global context, 'dj_currentContext' can be modified for temporary context shifting.
//    dojo.global() returns dj_currentContext.
// description:  
//		Refer to dojo.global() rather than referring to dj_global to ensure your
//		code runs correctly in managed contexts.
var dj_currentContext = this;


// ****************************************************************
// global public utils
// TODOC: DO WE WANT TO NOTE THAT THESE ARE GLOBAL PUBLIC UTILS?
// ****************************************************************

function dj_undef(/*String*/ name, /*Object?*/ object){
	//summary: Returns true if 'name' is defined on 'object' (or globally if 'object' is null).
	//description: Note that 'defined' and 'exists' are not the same concept.
	return (typeof (object || dj_currentContext)[name] == "undefined");	// Boolean
}

// make sure djConfig is defined
if(dj_undef("djConfig", this)){ 
	var djConfig = {}; 
}

//TODOC:  HOW TO DOC THIS?
// dojo is the root variable of (almost all) our public symbols -- make sure it is defined.
if(dj_undef("dojo", this)){ 
	var dojo = {}; 
}

dojo.global = function(){
	// summary:
	//		return the current global context object
	//		(e.g., the window object in a browser).
	// description: 
	//		Refer to 'dojo.global()' rather than referring to window to ensure your
	//		code runs correctly in contexts other than web browsers (eg: Rhino on a server).
	return dj_currentContext;
}

// Override locale setting, if specified
dojo.locale  = djConfig.locale;

//TODOC:  HOW TO DOC THIS?
dojo.version = {
	// summary: version number of this instance of dojo.
	major: 0, minor: 0, patch: 0, flag: "dev",
	revision: Number("$Rev: 5779 $".match(/[0-9]+/)[0]),
	toString: function(){
		with(dojo.version){
			return major + "." + minor + "." + patch + flag + " (" + revision + ")";	// String
		}
	}
}

dojo.evalProp = function(/*String*/ name, /*Object*/ object, /*Boolean?*/ create){
	// summary: Returns 'object[name]'.  If not defined and 'create' is true, will return a new Object.
	// description: 
	//		Returns null if 'object[name]' is not defined and 'create' is not true.
	// 		Note: 'defined' and 'exists' are not the same concept.	
	if((!object)||(!name)) return undefined; // undefined
	if(!dj_undef(name, object)) return object[name]; // mixed
	return (create ? (object[name]={}) : undefined);	// mixed
}

dojo.parseObjPath = function(/*String*/ path, /*Object?*/ context, /*Boolean?*/ create){
	// summary: Parse string path to an object, and return corresponding object reference and property name.
	// description: 
	//		Returns an object with two properties, 'obj' and 'prop'.  
	//		'obj[prop]' is the reference indicated by 'path'.
	// path: Path to an object, in the form "A.B.C".
	// context: Object to use as root of path.  Defaults to 'dojo.global()'.
	// create: If true, Objects will be created at any point along the 'path' that is undefined.
	var object = (context || dojo.global());
	var names = path.split('.');
	var prop = names.pop();
	for (var i=0,l=names.length;i<l && object;i++){
		object = dojo.evalProp(names[i], object, create);
	}
	return {obj: object, prop: prop};	// Object: {obj: Object, prop: String}
}

dojo.evalObjPath = function(/*String*/ path, /*Boolean?*/ create){
	// summary: Return the value of object at 'path' in the global scope, without using 'eval()'.
	// path: Path to an object, in the form "A.B.C".
	// create: If true, Objects will be created at any point along the 'path' that is undefined.
	if(typeof path != "string"){ 
		return dojo.global(); 
	}
	// fast path for no periods
	if(path.indexOf('.') == -1){
		return dojo.evalProp(path, dojo.global(), create);		// mixed
	}

	//MOW: old 'with' syntax was confusing and would throw an error if parseObjPath returned null.
	var ref = dojo.parseObjPath(path, dojo.global(), create);
	if(ref){
		return dojo.evalProp(ref.prop, ref.obj, create);	// mixed
	}
	return null;
}

dojo.errorToString = function(/*Error*/ exception){
	// summary: Return an exception's 'message', 'description' or text.

	// TODO: overriding Error.prototype.toString won't accomplish this?
 	// 		... since natively generated Error objects do not always reflect such things?
	if(!dj_undef("message", exception)){
		return exception.message;		// String
	}else if(!dj_undef("description", exception)){
		return exception.description;	// String
	}else{
		return exception;				// Error
	}
}

dojo.raise = function(/*String*/ message, /*Error?*/ exception){
	// summary: Common point for raising exceptions in Dojo to enable logging.
	//	Throws an error message with text of 'exception' if provided, or
	//	rethrows exception object.

	if(exception){
		message = message + ": "+dojo.errorToString(exception);
	}

	// print the message to the user if hostenv.println is defined
	try { if(djConfig.isDebug){ dojo.hostenv.println("FATAL exception raised: "+message); } } catch (e) {}

	throw exception || Error(message);
}

//Stub functions so things don't break.
//TODOC:  HOW TO DOC THESE?
dojo.debug = function(){};
dojo.debugShallow = function(obj){};
dojo.profile = { start: function(){}, end: function(){}, stop: function(){}, dump: function(){} };

function dj_eval(/*String*/ scriptFragment){ 
	// summary: Perform an evaluation in the global scope.  Use this rather than calling 'eval()' directly.
	// description: Placed in a separate function to minimize size of trapped evaluation context.
	// note:
	//	 - JSC eval() takes an optional second argument which can be 'unsafe'.
	//	 - Mozilla/SpiderMonkey eval() takes an optional second argument which is the
	//  	 scope object for new symbols.
	return dj_global.eval ? dj_global.eval(scriptFragment) : eval(scriptFragment); 	// mixed
}

dojo.unimplemented = function(/*String*/ funcname, /*String?*/ extra){
	// summary: Throw an exception because some function is not implemented.
	// extra: Text to append to the exception message.
	var message = "'" + funcname + "' not implemented";
	if (extra != null) { message += " " + extra; }
	dojo.raise(message);
}

dojo.deprecated = function(/*String*/ behaviour, /*String?*/ extra, /*String?*/ removal){
	// summary: Log a debug message to indicate that a behavior has been deprecated.
	// extra: Text to append to the message.
	// removal: Text to indicate when in the future the behavior will be removed.
	var message = "DEPRECATED: " + behaviour;
	if(extra){ message += " " + extra; }
	if(removal){ message += " -- will be removed in version: " + removal; }
	dojo.debug(message);
}

dojo.render = (function(){
	//TODOC: HOW TO DOC THIS?
	// summary: Details rendering support, OS and browser of the current environment.
	// TODOC: is this something many folks will interact with?  If so, we should doc the structure created...
	function vscaffold(prefs, names){
		var tmp = {
			capable: false,
			support: {
				builtin: false,
				plugin: false
			},
			prefixes: prefs
		};
		for(var i=0; i<names.length; i++){
			tmp[names[i]] = false;
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
	// TODOC:  HOW TO DOC THIS?
	// summary: Provides encapsulation of behavior that changes across different 'host environments' 
	//			(different browsers, server via Rhino, etc).
	// description: None of these methods should ever be called directly by library users.
	//				Use public methods such as 'loadModule' instead.
	
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

	return {
		name_: '(unset)',
		version_: '(unset)',


		getName: function(){ 
			// sumary: Return the name of the host environment.
			return this.name_; 	// String
		},


		getVersion: function(){ 
			// summary: Return the version of the hostenv.
			return this.version_; // String
		},

		getText: function(/*String*/ uri){
			// summary:	Read the plain/text contents at the specified 'uri'.
			// description: 
			//			If 'getText()' is not implemented, then it is necessary to override 
			//			'loadUri()' with an implementation that doesn't rely on it.

			dojo.unimplemented('getText', "uri=" + uri);
		}
	};
})();


dojo.hostenv.getBaseScriptUri = function(){
	// summary: Return the base script uri that other scripts are found relative to.
	// TODOC: HUH?  This comment means nothing to me.  What other scripts? Is this the path to other dojo libraries?
	//		MAYBE:  Return the base uri to scripts in the dojo library.	 ???
	// return: Empty string or a path ending in '/'.
	if(djConfig.baseScriptUri.length){ 
		return djConfig.baseScriptUri;
	}

	// MOW: Why not:
	//			uri = djConfig.libraryScriptUri || djConfig.baseRelativePath
	//		??? Why 'new String(...)'
	var uri = new String(djConfig.libraryScriptUri||djConfig.baseRelativePath);
	if (!uri) { dojo.raise("Nothing returned by getLibraryScriptUri(): " + uri); }

	// MOW: uri seems to not be actually used.  Seems to be hard-coding to djConfig.baseRelativePath... ???
	var lastslash = uri.lastIndexOf('/');		// MOW ???
	djConfig.baseScriptUri = djConfig.baseRelativePath;
	return djConfig.baseScriptUri;	// String
}

/*
 * loader.js - A bootstrap module.  Runs before the hostenv_*.js file. Contains all of the package loading methods.
 */

//A semi-colon is at the start of the line because after doing a build, this function definition
//get compressed onto the same line as the last line in bootstrap1.js. That list line is just a
//curly bracket, and the browser complains about that syntax. The semicolon fixes it. Putting it
//here instead of at the end of bootstrap1.js, since it is more of an issue for this file, (using
//the closure), and bootstrap1.js could change in the future.
;(function(){
	//Additional properties for dojo.hostenv
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
			this.modulePrefixes_[module] = {name: module, value: prefix};
		},

		moduleHasPrefix: function(module){
			var mp = this.modulePrefixes_;
			return Boolean(mp[module] && mp[module].value);
		},

		getModulePrefix: function(module){
			if(this.moduleHasPrefix(module)){
				return this.modulePrefixes_[module].value;
			}
			return module;
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
	
	//Add all of these properties to dojo.hostenv
	for(var param in _addHostEnv){
		dojo.hostenv[param] = _addHostEnv[param];
	}
})();

dojo.hostenv.loadPath = function(/*String*/relpath, /*String?*/module, /*Function?*/cb){
// summary:
//	Load a Javascript module given a relative path
//
// description:
//	Loads and interprets the script located at relpath, which is relative to the
//	script root directory.  If the script is found but its interpretation causes
//	a runtime exception, that exception is not caught by us, so the caller will
//	see it.  We return a true value if and only if the script is found.
//
//	For now, we do not have an implementation of a true search path.  We
//	consider only the single base script uri, as returned by getBaseScriptUri().
//
// relpath: A relative path to a script (no leading '/', and typically
// 	ending in '.js').
// module: A module whose existance to check for after loading a path.
//	Can be used to determine success or failure of the load.
// cb: a callback function to pass the result of evaluating the script

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

dojo.hostenv.loadUri = function(/*String (URL)*/uri, /*Function?*/cb){
// summary:
//	Loads JavaScript from a URI
//
// description:
//	Reads the contents of the URI, and evaluates the contents.  This is used to load modules as well
//	as resource bundles.  Returns true if it succeeded. Returns false if the URI reading failed.
//	Throws if the evaluation throws.
//
// uri: a uri which points at the script to be loaded
// cb: a callback function to process the result of evaluating the script as an expression, typically
//	used by the resource bundle loader to load JSON-style resources

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

// FIXME: probably need to add logging to this method
dojo.hostenv.loadUriAndCheck = function(uri, module, /*Function?*/cb){
	var ok = true;
	try{
		ok = this.loadUri(uri, cb);
	}catch(e){
		dojo.debug("failed loading ", uri, " with error: ", e);
	}
	return Boolean(ok && this.findModule(module, false)); // Boolean
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

	//Clear listeners so new ones can be added
	//For other xdomain package loads after the initial load.
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

/*
Call styles:
	dojo.addOnLoad(functionPointer)
	dojo.addOnLoad(object, "functionName")
*/
dojo.addOnLoad = function(obj, fcnName) {
	var dh = dojo.hostenv;
	if(arguments.length == 1) {
		dh.modulesLoadedListeners.push(obj);
	} else if(arguments.length > 1) {
		dh.modulesLoadedListeners.push(function() {
			obj[fcnName]();
		});
	}

	//Added for xdomain loading. dojo.addOnLoad is used to
	//indicate callbacks after doing some dojo.require() statements.
	//In the xdomain case, if all the requires are loaded (after initial
	//page load), then immediately call any listeners.
	if(dh.post_load_ && dh.inFlightCount == 0 && !dh.loadNotifying){
		dh.callLoaded();
	}
}

dojo.addOnUnload = function(obj, fcnName){
	var dh = dojo.hostenv;
	if(arguments.length == 1){
		dh.unloadListeners.push(obj);
	} else if(arguments.length > 1) {
		dh.unloadListeners.push(function() {
			obj[fcnName]();
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

dojo.hostenv.getModuleSymbols = function(/*String*/modulename){
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
dojo.hostenv.loadModule = function(/*String*/modulename, /*Boolean?*/exactOnly, /*Boolean?*/omitModuleCheck){
// summary:
//	loads a Javascript module from the appropriate URI
//
// description:
//	loadModule("A.B") first checks to see if symbol A.B is defined. 
//	If it is, it is simply returned (nothing to do).
//	
//	If it is not defined, it will look for "A/B.js" in the script root directory,
//	followed by "A.js".
//	
//	It throws if it cannot find a file to load, or if the symbol A.B is not
//	defined after loading.
//	
//	It returns the object A.B.
//	
//	This does nothing about importing symbols into the current package.
//	It is presumed that the caller will take care of that. For example, to import
//	all symbols:
//	
//	   with (dojo.hostenv.loadModule("A.B")) {
//	      ...
//	   }
//	
//	And to import just the leaf symbol:
//	
//	   var B = dojo.hostenv.loadModule("A.B");
//	   ...
//	
//	dj_load is an alias for dojo.hostenv.loadModule

	if(!modulename){ return; }
	omitModuleCheck = this._global_omit_module_check || omitModuleCheck;
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

	var nsyms = modulename.split(".");
	
	// this line allowed loading of a module manifest as if it were a namespace
	// it's an interesting idea, but shouldn't be combined with 'namespaces' proper
	// and leads to unwanted dependencies
	// the effect can be achieved in other (albeit less-flexible) ways now, so I am
	// removing this pending further design work
	// perhaps we can explicitly define this idea of a 'module manifest', and subclass
	// 'namespace manifest' from that
	//dojo.getNamespace(nsyms[0]);

	var syms = this.getModuleSymbols(modulename);
	var startedRelative = ((syms[0].charAt(0) != '/') && !syms[0].match(/^\w+:/));
	var last = syms[syms.length - 1];
	var ok;
	// figure out if we're looking for a full package, if so, we want to do
	// things slightly diffrently
	if(last=="*"){
		modulename = nsyms.slice(0, -1).join('.');
		while(syms.length){
			syms.pop();
			syms.push(this.pkgFileName);
			relpath = syms.join("/") + '.js';
			if(startedRelative && relpath.charAt(0)=="/"){
				relpath = relpath.slice(1);
			}
			ok = this.loadPath(relpath, !omitModuleCheck ? modulename : null);
			if(ok){ break; }
			syms.pop();
		}
	}else{
		relpath = syms.join("/") + '.js';
		modulename = nsyms.join('.');
		var modArg = !omitModuleCheck ? modulename : null;
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
			dojo.raise("Could not load '" + modulename + "'; last tried '" + relpath + "'");
		}
	}

	// check that the symbol was defined
	//Don't bother if we're doing xdomain (asynchronous) loading.
	if(!omitModuleCheck && !this["isXDomain"]){
		// pass in false so we can give better error
		module = this.findModule(modulename, false);
		if(!module){
			dojo.raise("symbol '" + modulename + "' is not defined after loading '" + relpath + "'"); 
		}
	}

	return module;
}

dojo.hostenv.startPackage = function(/*String*/packageName){
// summary:
//	Creates a JavaScript package
//
// description:
//	startPackage("A.B") follows the path, and at each level creates a new empty
//	object or uses what already exists. It returns the result.
//
// packageName: the package to be created as a String in dot notation

	//Make sure we have a string.
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

dojo.hostenv.findModule = function(/*String*/moduleName, /*Boolean?*/mustExist){
// summary:
//	Returns the Object representing the module, if it exists, otherwise null.
//
// moduleName A fully qualified module including package name, like 'A.B'.
// mustExist Optional, default false. throw instead of returning null
//	if the module does not currently exist.

	var lmn = String(moduleName);

	if(this.loaded_modules_[lmn]){
		return this.loaded_modules_[lmn]; // Object
	}

	if(mustExist){
		dojo.raise("no loaded module named '" + moduleName + "'");
	}
	return null; // null
}

//Start of old bootstrap2:

dojo.kwCompoundRequire = function(modMap){
// description:
//	This method taks a "map" of arrays which one can use to optionally load dojo
//	modules. The map is indexed by the possible dojo.hostenv.name_ values, with
//	two additional values: "default" and "common". The items in the "default"
//	array will be loaded if none of the other items have been choosen based on
//	the hostenv.name_ item. The items in the "common" array will _always_ be
//	loaded, regardless of which list is chosen.  Here's how it's normally
//	called:
//	
//	dojo.kwCompoundRequire({
//		browser: [
//			["foo.bar.baz", true, true], // an example that passes multiple args to loadModule()
//			"foo.sample.*",
//			"foo.test,
//		],
//		default: [ "foo.sample.*" ],
//		common: [ "really.important.module.*" ]
//	});

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

dojo.require = function(){
	dojo.hostenv.loadModule.apply(dojo.hostenv, arguments);
}

dojo.requireIf = function(){
	var arg0 = arguments[0];
	if((arg0 === true)||(arg0=="common")||(arg0 && dojo.render[arg0].capable)){
		var args = [];
		for (var i = 1; i < arguments.length; i++) { args.push(arguments[i]); }
		dojo.require.apply(dojo, args);
	}
}

dojo.requireAfterIf = dojo.requireIf;

dojo.provide = function(){
	return dojo.hostenv.startPackage.apply(dojo.hostenv, arguments);
}

dojo.registerModulePath = function(/*String*/module, /*String*/prefix){
	// summary: maps a module name to a path
	// description: An unregistered module is given the default path of ../<module>,
	//	relative to Dojo root. For example, module acme is mapped to ../acme.
	//	If you want to use a different module name, use dojo.registerModulePath. 
	return dojo.hostenv.setModulePrefix(module, prefix);
}

dojo.setModulePrefix = function(/*String*/module, /*String*/prefix){
	// summary: maps a module name to a path
	dojo.deprecated('dojo.setModulePrefix("' + module + '", "' + prefix + '")', "replaced by dojo.registerModulePath", "0.5");
	return dojo.registerModulePath(module, prefix);
}

dojo.exists = function(/*Object*/obj, /*String*/name){
	// summary: determine if an object supports a given method
	// description: useful for longer api chains where you have to test each object in the chain
	var p = name.split(".");
	for(var i = 0; i < p.length; i++){
		if(!obj[p[i]]){ return false; }
		obj = obj[p[i]];
	}
	return true; // Boolean
}

// Localization routines

dojo.hostenv.normalizeLocale = function(/*String?*/locale){
//	summary:
//		Returns canonical form of locale, as used by Dojo.  All variants are case-insensitive and are separated by '-'
//		as specified in RFC 3066. If no locale is specified, the user agent's default is returned.

	return locale ? locale.toLowerCase() : dojo.locale; // String
};

dojo.hostenv.searchLocalePath = function(/*String*/locale, /*Boolean*/down, /*Function*/searchFunc){
//	summary:
//		A helper method to assist in searching for locale-based resources.  Will iterate through
//		the variants of a particular locale, either up or down, executing a callback function.
//		For example, "en-us" and true will try "en-us" followed by "en" and finally "ROOT".

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

dojo.hostenv.preloadLocalizations = function(){
// summary:
//	Load built, flattened resource bundles, if available for all locales used in the page.
//	Execute only once.  Note that this is a no-op unless there is a build.

	var localesGenerated /***BUILD:localesGenerated***/; // value will be inserted at build time, if necessary

	if(localesGenerated){
		dojo.registerModulePath("nls","nls");

		function preload(locale){
			locale = dojo.hostenv.normalizeLocale(locale);
			dojo.hostenv.searchLocalePath(locale, true, function(loc){
				for(var i=0; i<localesGenerated.length;i++){
					if(localesGenerated[i] == loc){
						dojo["require"]("nls.dojo_"+loc);
						return true;
					}
				}
				return false;
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

dojo.requireLocalization = function(/*String*/moduleName, /*String*/bundleName, /*String?*/locale){
// summary:
//	Declares translated resources and loads them if necessary, in the same style as dojo.require.
//	Contents of the resource bundle are typically strings, but may be any name/value pair,
//	represented in JSON format.  See also dojo.i18n.getLocalization.
//
// moduleName: name of the package containing the "nls" directory in which the bundle is found
// bundleName: bundle name, i.e. the filename without the '.js' suffix
// locale: the locale to load (optional)  By default, the browser's user locale as defined by dojo.locale
//
// description:
//	Load translated resource bundles provided underneath the "nls" directory within a package.
//	Translated resources may be located in different packages throughout the source tree.  For example,
//	a particular widget may define one or more resource bundles, structured in a program as follows,
//	where moduleName is mycode.mywidget and bundleNames available include bundleone and bundletwo:
//	...
//	mycode/
//	 mywidget/
//	  nls/
//	   bundleone.js (the fallback translation, English in this example)
//	   bundletwo.js (also a fallback translation)
//	   de/
//	    bundleone.js
//	    bundletwo.js
//	   de-at/
//	    bundleone.js
//	   en/
//	    (empty; use the fallback translation)
//	   en-us/
//	    bundleone.js
//	   en-gb/
//	    bundleone.js
//	   es/
//	    bundleone.js
//	    bundletwo.js
//	  ...etc
//	...
//	Each directory is named for a locale as specified by RFC 3066, (http://www.ietf.org/rfc/rfc3066.txt),
//	normalized in lowercase.  Note that the two bundles in the example do not define all the same variants.
//	For a given locale, bundles will be loaded for that locale and all more general locales above it, including
//	a fallback at the root directory.  For example, a declaration for the "de-at" locale will first
//	load nls/de-at/bundleone.js, then nls/de/bundleone.js and finally nls/bundleone.js.  The data will
//	be flattened into a single Object so that lookups will follow this cascading pattern.  An optional build
//	step can preload the bundles to avoid data redundancy and the multiple network hits normally required to
//	load these resources.

	dojo.hostenv.preloadLocalizations();
 	var bundlePackage = [moduleName, "nls", bundleName].join(".");
//NOTE: When loading these resources, the packaging does not match what is on disk.  This is an
// implementation detail, as this is just a private data structure to hold the loaded resources.
// e.g. tests/hello/nls/en-us/salutations.js is loaded as the object tests.hello.nls.salutations.en_us={...}
// The structure on disk is intended to be most convenient for developers and translators, but in memory
// it is more logical and efficient to store in a different order.  Locales cannot use dashes, since the
// resulting path will not evaluate as valid JS, so we translate them to underscores.

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
	// If other locales are used, dojo.requireLocalization should load them as well, by default.
	// Override dojo.requireLocalization to do load the default bundle, then iterate through the
	// extraLocale list and load those translations as well, unless a particular locale was requested.

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

};

if (typeof window != 'undefined') {

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
				var root = src.substring(0, m.index);
				if(src.indexOf("bootstrap1") > -1) { root += "../"; }
				if(!this["djConfig"]) { djConfig = {}; }
				if(djConfig["baseScriptUri"] == "") { djConfig["baseScriptUri"] = root; }
				if(djConfig["baseRelativePath"] == "") { djConfig["baseRelativePath"] = root; }
				break;
			}
		}
	}

	// fill in the rendering support information in dojo.render.*
	var dr = dojo.render;
	var drh = dojo.render.html;
	var drs = dojo.render.svg;
	var dua = (drh.UA = navigator.userAgent);
	var dav = (drh.AV = navigator.appVersion);
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
	drh.ie70 = drh.ie && dav.indexOf("MSIE 7.0")>=0;

	var cm = document["compatMode"];
	drh.quirks = (cm == "BackCompat")||(cm == "QuirksMode")||drh.ie55||drh.ie50;

	// TODO: is the HTML LANG attribute relevant?
	dojo.locale = dojo.locale || (drh.ie ? navigator.userLanguage : navigator.language).toLowerCase();

	dr.vml.capable=drh.ie;
	drs.capable = f;
	drs.support.plugin = f;
	drs.support.builtin = f;
	var tdoc = window["document"];
	var tdi = tdoc["implementation"];

	if((tdi)&&(tdi["hasFeature"])&&(tdi.hasFeature("org.w3c.dom.svg", "1.0"))){
		drs.capable = t;
		drs.support.builtin = t;
		drs.support.plugin = f;
	}
	// webkits after 420 support SVG natively. The test string is "AppleWebKit/420+"
	if(drh.safari){
		var tmp = dua.split("AppleWebKit/")[1];
		var ver = parseFloat(tmp.split(" ")[0]);
		if(ver >= 420){
			drs.capable = t;
			drs.support.builtin = t;
			drs.support.plugin = f;
		}
	}
})();

dojo.hostenv.startPackage("dojo.hostenv");

dojo.render.name = dojo.hostenv.name_ = 'browser';
dojo.hostenv.searchIds = [];

// These are in order of decreasing likelihood; this will change in time.
dojo.hostenv._XMLHTTP_PROGIDS = ['Msxml2.XMLHTTP', 'Microsoft.XMLHTTP', 'Msxml2.XMLHTTP.4.0'];

dojo.hostenv.getXmlhttpObject = function(){
    var http = null;
	var last_e = null;
	try{ http = new XMLHttpRequest(); }catch(e){}
    if(!http){
		for(var i=0; i<3; ++i){
			var progid = dojo.hostenv._XMLHTTP_PROGIDS[i];
			try{
				http = new ActiveXObject(progid);
			}catch(e){
				last_e = e;
			}

			if(http){
				dojo.hostenv._XMLHTTP_PROGIDS = [progid];  // so faster next time
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
dojo.hostenv._blockAsync = false;
dojo.hostenv.getText = function(uri, async_cb, fail_ok){
	// need to block async callbacks from snatching this thread as the result 
	// of an async callback might call another sync XHR, this hangs khtml forever
	// hostenv._blockAsync must also be checked in BrowserIO's watchInFlight()
	// NOTE: must be declared before scope switches ie. this.getXmlhttpObject()
	if(!async_cb){ this._blockAsync = true; }

	var http = this.getXmlhttpObject();

	function isDocumentOk(http){
		var stat = http["status"];
		// allow a 304 use cache, needed in konq (is this compliant with the http spec?)
		return Boolean((!stat)||((200 <= stat)&&(300 > stat))||(stat==304));
	}

	if(async_cb){
		var _this = this, timer = null, gbl = dojo.global();
		var xhr = dojo.evalObjPath("dojo.io.XMLHTTPTransport");
		http.onreadystatechange = function(){
			if(timer){ gbl.clearTimeout(timer); timer = null; }
			if(_this._blockAsync || (xhr && xhr._blockAsync)){
				timer = gbl.setTimeout(function () { http.onreadystatechange.apply(this); }, 10);
			}else{
				if(4==http.readyState){
					if(isDocumentOk(http)){
						// dojo.debug("LOADED URI: "+uri);
						async_cb(http.responseText);
					}
				}
			}
		}
	}

	http.open('GET', uri, async_cb ? true : false);
	try{
		http.send(null);
		if(async_cb){
			return null;
		}
		if(!isDocumentOk(http)){
			var err = Error("Unable to load "+uri+" status:"+ http.status);
			err.status = http.status;
			err.responseText = http.responseText;
			throw err;
		}
	}catch(e){
		this._blockAsync = false;
		if((fail_ok)&&(!async_cb)){
			return null;
		}else{
			throw e;
		}
	}

	this._blockAsync = false;
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
			if(!console) { console = dojo.body(); }

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

function dj_addNodeEvtHdlr(node, evtName, fp, capture){
	var oldHandler = node["on"+evtName] || function(){};
	node["on"+evtName] = function(){
		fp.apply(node, arguments);
		oldHandler.apply(node, arguments);
	}
	return true;
}

//	BEGIN DOMContentLoaded, from Dean Edwards (http://dean.edwards.name/weblog/2006/06/again/)
function dj_load_init(e){
	// allow multiple calls, only first one will take effect
	// A bug in khtml calls events callbacks for document for event which isnt supported
	// for example a created contextmenu event calls DOMContentLoaded, workaround
	var type = (e && e.type) ? e.type.toLowerCase() : "load";
	if(arguments.callee.initialized || (type!="domcontentloaded" && type!="load")){ return; }
	arguments.callee.initialized = true;
	if(typeof(_timer) != 'undefined'){
		clearInterval(_timer);
		delete _timer;
	}

	var initFunc = function(){
		//perform initialization
		if(dojo.render.html.ie){
			dojo.hostenv.makeWidgets();
		}
	};

	if(dojo.hostenv.inFlightCount == 0){
		initFunc();
		dojo.hostenv.modulesLoaded();
	}else{
		dojo.addOnLoad(initFunc);
	}
}

//	START DOMContentLoaded
// Mozilla and Opera 9 expose the event we could use
if(document.addEventListener) {
	document.addEventListener("DOMContentLoaded", dj_load_init, null);

	//	mainly for Opera 8.5, won't be fired if DOMContentLoaded fired already.
	document.addEventListener("load", dj_load_init, null);
}

// 	for Internet Explorer. readyState will not be achieved on init call, but dojo doesn't need it
//	however, we'll include it because we don't know if there are other functions added that might.
//	Note that this has changed because the build process strips all comments--including conditional
//		ones.
if(dojo.render.html.ie && dojo.render.os.win){
	document.attachEvent("onreadystatechange", function(e){
		if(document.readyState == "complete"){
			dj_load_init();
		}
	});
}

if (/(WebKit|khtml)/i.test(navigator.userAgent)) { // sniff
    var _timer = setInterval(function() {
        if (/loaded|complete/.test(document.readyState)) {
            dj_load_init(); // call the onload handler
        }
    }, 10);
}
//	END DOMContentLoaded

dj_addNodeEvtHdlr(window, "unload", function(){
	dojo.hostenv.unloaded();
});

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
			// (IE bug)
				var parser = new dojo.xml.Parse();
				if(sids.length > 0){
					for(var x=0; x<sids.length; x++){
						var tmpNode = document.getElementById(sids[x]);
						if(!tmpNode){ continue; }
						var frag = parser.parseElement(tmpNode, null, true);
						dojo.widget.getParser().createComponents(frag);
					}
				}else if(djConfig.parseWidgets){
					var frag  = parser.parseElement(dojo.body(), null, true);
					dojo.widget.getParser().createComponents(frag);
				}
		}
	}
}

dojo.addOnLoad(function(){
	if(!dojo.render.html.ie) {
		dojo.hostenv.makeWidgets();
	}
});

try {
	if (dojo.render.html.ie) {
		document.namespaces.add("v","urn:schemas-microsoft-com:vml");
		document.createStyleSheet().addRule("v\\:*", "behavior:url(#default#VML)");
	}
} catch (e) { }

// stub, over-ridden by debugging code. This will at least keep us from
// breaking when it's not included
dojo.hostenv.writeIncludes = function(){}

//TODOC:  HOW TO DOC THIS?
// @global: dj_currentDocument
// summary: 
//		Current document object. 'dj_currentDocument' can be modified for temporary context shifting.
// description:  
//    dojo.doc() returns dojo.currentDocument.
//		Refer to dojo.doc() rather than referring to 'window.document' to ensure your
//		code runs correctly in managed contexts.
if(!dj_undef("document", this)){
	dj_currentDocument = this.document;
}

dojo.doc = function(){
	// summary:
	//		return the document object associated with the dojo.global()
	return dj_currentDocument;
}

dojo.body = function(){
	// summary:
	//		return the body object associated with dojo.doc()
	// Note: document.body is not defined for a strict xhtml document
	return dojo.doc().body || dojo.doc().getElementsByTagName("body")[0];
}

dojo.byId = function(id, doc){
	if((id)&&((typeof id == "string")||(id instanceof String))){
		if (!doc) { doc = dj_currentDocument; }
		var ele = doc.getElementById(id);
		// workaround bug in IE and Opera 8.2 where getElementById returns wrong element
		if (ele && (ele.id != id) && doc.all) {
			ele = null;
			// get all matching elements with this id
			eles = doc.all[id];
			if (eles) {
				// if more than 1, choose first with the correct id
				if (eles.length) {
					for (var i=0; i < eles.length; i++) {
						if (eles[i].id == id) {
							ele = eles[i];
							break;
						}
					}
				// return 1 and only element
				} else { ele = eles; }
			}
		}
		return ele;
	}
	return id; // assume it's a node
}
 
dojo.setContext = function(/*Object*/globalObject, /*Object*/ globalDocument){
	dj_currentContext = globalObject;
	dj_currentDocument = globalDocument;
};

dojo._fireCallback = function(callback, context, cbArguments) {
	if((context)&&((typeof callback == "string")||(callback instanceof String))){
		callback=context[callback];
	}
	return (context ? callback.apply(context, cbArguments || [ ]) : callback());
}

dojo.withGlobal = function(/*Object*/globalObject, /*Function*/callback, /*Object?*/thisObject, /*Array?*/cbArguments){
	// summary:
	//		Call callback with globalObject as dojo.global() and globalObject.document 
	//		as dojo.doc(). If provided, globalObject will be executed in the context of 
	//		object thisObject
	// description: 
	//		When callback() returns or throws an error, the dojo.global() and dojo.doc() will
	//		be restored to its previous state.
	var rval;
	var oldGlob = dj_currentContext;
	var oldDoc = dj_currentDocument;
	try{
		dojo.setContext(globalObject, globalObject.document);
		rval = dojo._fireCallback(callback, thisObject, cbArguments);
	}finally{
		dojo.setContext(oldGlob, oldDoc);
	}
	return rval;
}

dojo.withDoc = function (/*Object*/documentObject, /*Function*/callback, /*Object?*/thisObject, /*Array?*/cbArguments) {
	// summary:
	//		Call callback with documentObject as dojo.doc(). If provided, callback will be executed
	//		in the context of object thisObject
	// description: 
	//		When callback() returns or throws an error, the dojo.doc() will
	//		be restored to its previous state.
	var rval;
	var oldDoc = dj_currentDocument;
	try{
		dj_currentDocument = documentObject;
		rval = dojo._fireCallback(callback, thisObject, cbArguments);
	}finally{
		dj_currentDocument = oldDoc;
	}
	return rval;
}

}
//Semicolon is for when this file is integrated with a custom build on one line
//with some other file's contents. Sometimes that makes things not get defined
//properly, particularly with the using the closure below to do all the work.
;(function(){
	//Don't do this work if dojo.js has already done it.
	if(typeof dj_usingBootstrap != "undefined"){
		return;
	}

	var isRhino = false;
	var isSpidermonkey = false;
	var isDashboard = false;
	if((typeof this["load"] == "function")&&((typeof this["Packages"] == "function")||(typeof this["Packages"] == "object"))){
		isRhino = true;
	}else if(typeof this["load"] == "function"){
		isSpidermonkey  = true;
	}else if(window.widget){
		isDashboard = true;
	}

	var tmps = [];
	if((this["djConfig"])&&((djConfig["isDebug"])||(djConfig["debugAtAllCosts"]))){
		tmps.push("debug.js");
	}

	if((this["djConfig"])&&(djConfig["debugAtAllCosts"])&&(!isRhino)&&(!isDashboard)){
		tmps.push("browser_debug.js");
	}

	var loaderRoot = djConfig["baseScriptUri"];
	if((this["djConfig"])&&(djConfig["baseLoaderUri"])){
		loaderRoot = djConfig["baseLoaderUri"];
	}

	for(var x=0; x < tmps.length; x++){
		var spath = loaderRoot+"src/"+tmps[x];
		if(isRhino||isSpidermonkey){
			load(spath);
		} else {
			try {
				document.write("<scr"+"ipt type='text/javascript' src='"+spath+"'></scr"+"ipt>");
			} catch (e) {
				var script = document.createElement("script");
				script.src = spath;
				document.getElementsByTagName("head")[0].appendChild(script);
			}
		}
	}
})();

dojo.provide("dojo.lang.common");

dojo.lang.inherits = function(/*Function*/ subclass, /*Function*/ superclass){
	// summary: Set up inheritance between two classes.
	if(typeof superclass != 'function'){ 
		dojo.raise("dojo.inherits: superclass argument ["+superclass+"] must be a function (subclass: ["+subclass+"']");
	}
	subclass.prototype = new superclass();
	subclass.prototype.constructor = subclass;
	subclass.superclass = superclass.prototype;
	// DEPRICATED: super is a reserved word, use 'superclass'
	subclass['super'] = superclass.prototype;
}

dojo.lang._mixin = function(/*Object*/ obj, /*Object*/ props){
	// summary:	Adds all properties and methods of props to obj.
	var tobj = {};
	for(var x in props){
		// the "tobj" condition avoid copying properties in "props"
		// inherited from Object.prototype.  For example, if obj has a custom
		// toString() method, don't overwrite it with the toString() method
		// that props inherited from Object.protoype
		if((typeof tobj[x] == "undefined") || (tobj[x] != props[x])){
			obj[x] = props[x];
		}
	}
	// IE doesn't recognize custom toStrings in for..in
	if(dojo.render.html.ie 
		&& (typeof(props["toString"]) == "function")
		&& (props["toString"] != obj["toString"])
		&& (props["toString"] != tobj["toString"]))
	{
		obj.toString = props.toString;
	}
	return obj;
}

dojo.lang.mixin = function(/*Object*/ obj, /*Object...*/props){
	// summary:	Adds all properties and methods of props to obj.
	for(var i=1, l=arguments.length; i<l; i++){
		dojo.lang._mixin(obj, arguments[i]);
	}
	return obj; // Object
}

dojo.lang.extend = function(/*Object*/ constructor, /*Object...*/ props){
	// summary:	Adds all properties and methods of props to constructor's prototype,
	//			making them available to all instances created with constructor.
	for(var i=1, l=arguments.length; i<l; i++){
		dojo.lang._mixin(constructor.prototype, arguments[i]);
	}
	return constructor;
}

// Promote to dojo module
dojo.inherits = dojo.lang.inherits;
//dojo.lang._mixin = dojo.lang._mixin;
dojo.mixin = dojo.lang.mixin;
dojo.extend = dojo.lang.extend;

dojo.lang.find = function(	/*Array*/		array, 
							/*Object*/		value,
							/*Boolean?*/	identity,
							/*Boolean?*/	findLast){
	// summary:	Return the index of value in array, returning -1 if not found.
	
	// param: identity:  If true, matches with identity comparison (===).  
	//					 If false, uses normal comparison (==).
	// param: findLast:  If true, returns index of last instance of value.
	// usage:
	//  find(array, value[, identity [findLast]]) // recommended
	// usage:
 	//  find(value, array[, identity [findLast]]) // deprecated
							
	// support both (array, value) and (value, array)
	if(!dojo.lang.isArrayLike(array) && dojo.lang.isArrayLike(value)) {
		dojo.deprecated('dojo.lang.find(value, array)', 'use dojo.lang.find(array, value) instead', "0.5");
		var temp = array;
		array = value;
		value = temp;
	}
	var isString = dojo.lang.isString(array);
	if(isString) { array = array.split(""); }

	if(findLast) {
		var step = -1;
		var i = array.length - 1;
		var end = -1;
	} else {
		var step = 1;
		var i = 0;
		var end = array.length;
	}
	if(identity){
		while(i != end) {
			if(array[i] === value){ return i; }
			i += step;
		}
	}else{
		while(i != end) {
			if(array[i] == value){ return i; }
			i += step;
		}
	}
	return -1;	// number
}

dojo.lang.indexOf = dojo.lang.find;

dojo.lang.findLast = function(/*Array*/ array, /*Object*/ value, /*boolean?*/ identity){
	// summary:	Return index of last occurance of value in array, returning -1 if not found.

	// param: identity:  If true, matches with identity comparison (===).  
	//					 If false, uses normal comparison (==).
	return dojo.lang.find(array, value, identity, true);
}

dojo.lang.lastIndexOf = dojo.lang.findLast;

dojo.lang.inArray = function(array /*Array*/, value /*Object*/){
	// summary:	Return true if value is present in array.
	return dojo.lang.find(array, value) > -1; // return: boolean
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

dojo.lang.isObject = function(it){
	// summary:	Return true if it is an Object, Array or Function.
	if(typeof it == "undefined"){ return false; }
	return (typeof it == "object" || it === null || dojo.lang.isArray(it) || dojo.lang.isFunction(it));
}

dojo.lang.isArray = function(it){
	// summary:	Return true if it is an Array.
	return (it && it instanceof Array || typeof it == "array");
}

dojo.lang.isArrayLike = function(it){
	// summary:	Return true if it can be used as an array (i.e. is an object with an integer length property).
	if((!it)||(dojo.lang.isUndefined(it))){ return false; }
	if(dojo.lang.isString(it)){ return false; }
	if(dojo.lang.isFunction(it)){ return false; } // keeps out built-in constructors (Number, String, ...) which have length properties
	if(dojo.lang.isArray(it)){ return true; }
	// form node itself is ArrayLike, but not always iterable. Use form.elements instead.
	if((it.tagName)&&(it.tagName.toLowerCase()=='form')){ return false; }
	if(dojo.lang.isNumber(it.length) && isFinite(it.length)){ return true; }
	return false;
}

dojo.lang.isFunction = function(it){
	// summary:	Return true if it is a Function.
	if(!it){ return false; }
	// webkit treats NodeList as a function, which is bad
	if((typeof(it) == "function") && (it == "[object NodeList]")) { return false; }
	return (it instanceof Function || typeof it == "function");
}

dojo.lang.isString = function(it){
	// summary:	Return true if it is a String.
	return (typeof it == "string" || it instanceof String);
}

dojo.lang.isAlien = function(it){
	// summary:	Return true if it is not a built-in function.
	if(!it){ return false; }
	return !dojo.lang.isFunction() && /\{\s*\[native code\]\s*\}/.test(String(it));
}

dojo.lang.isBoolean = function(it){
	// summary:	Return true if it is a Boolean.
	return (it instanceof Boolean || typeof it == "boolean");
}

/**
 * The following is***() functions are somewhat "unsafe". Fortunately,
 * there are workarounds the the language provides and are mentioned
 * in the WARNING messages.
 *
 */
dojo.lang.isNumber = function(it){
	// summary:	Return true if it is a number.

	// warning: 
	//		In most cases, isNaN(it) is sufficient to determine whether or not
	// 		something is a number or can be used as such. For example, a number or string
	// 		can be used interchangably when accessing array items (array["1"] is the same as
	// 		array[1]) and isNaN will return false for both values ("1" and 1). However,
	// 		isNumber("1")  will return false, which is generally not too useful.
	// 		Also, isNumber(NaN) returns true, again, this isn't generally useful, but there
	// 		are corner cases (like when you want to make sure that two things are really
	// 		the same type of thing). That is really where isNumber "shines".
	//
	// recommendation: Use isNaN(it) when possible
	
	return (it instanceof Number || typeof it == "number");
}

/*
 * FIXME: Should isUndefined go away since it is error prone?
 */
dojo.lang.isUndefined = function(it){
	// summary: Return true if it is not defined.
	
	// warning: In some cases, isUndefined will not behave as you
	// 		might expect. If you do isUndefined(foo) and there is no earlier
	// 		reference to foo, an error will be thrown before isUndefined is
	// 		called. It behaves correctly if you scope yor object first, i.e.
	// 		isUndefined(foo.bar) where foo is an object and bar isn't a
	// 		property of the object.
	//
	// recommendation: Use typeof foo == "undefined" when possible

	return ((typeof(it) == "undefined")&&(it == undefined));
}

// end Crockford functions

dojo.provide("dojo.lang.array");


// FIXME: Is this worthless since you can do: if(name in obj)
// is this the right place for this?
dojo.lang.has = function(obj, name){
	try{
		return (typeof obj[name] != "undefined");
	}catch(e){ return false; }
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

dojo.lang.reduce = function(arr, initialValue, obj, binary_func){
	var reducedValue = initialValue;
	var ob = obj ? obj : dj_global;
	dojo.lang.map(arr, 
		function(val){
			reducedValue = binary_func.call(ob, reducedValue, val);
		}
	);
	return reducedValue;
}

// http://developer.mozilla.org/en/docs/Core_JavaScript_1.5_Reference:Global_Objects:Array:forEach
dojo.lang.forEach = function(anArray /* Array */, callback /* Function */, thisObject /* Object */){
	if(dojo.lang.isString(anArray)){ 
		anArray = anArray.split(""); 
	}
	if(Array.forEach){
		Array.forEach(anArray, callback, thisObject);
	}else{
		// FIXME: there are several ways of handilng thisObject. Is dj_global always the default context?
		if(!thisObject){
			thisObject=dj_global;
		}
		for(var i=0,l=anArray.length; i<l; i++){ 
			callback.call(thisObject, anArray[i], i, anArray);
		}
	}
}

dojo.lang._everyOrSome = function(every, arr, callback, thisObject){
	if(dojo.lang.isString(arr)){ 
		arr = arr.split(""); 
	}
	if(Array.every){
		return Array[ (every) ? "every" : "some" ](arr, callback, thisObject);
	}else{
		if(!thisObject){
			thisObject = dj_global;
		}
		for(var i=0,l=arr.length; i<l; i++){
			var result = callback.call(thisObject, arr[i], i, arr);
			if((every)&&(!result)){
				return false;
			}else if((!every)&&(result)){
				return true;
			}
		}
		return (every) ? true : false;
	}
}

dojo.lang.every = function(arr, callback, thisObject){
	return this._everyOrSome(true, arr, callback, thisObject);
}

dojo.lang.some = function(arr, callback, thisObject){
	return this._everyOrSome(false, arr, callback, thisObject);
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

dojo.provide("dojo.lang.extras");


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
	return dojo.global().setTimeout(function () { func.apply(context, args); }, delay);
}

dojo.lang.clearTimeout = function(timer){
	dojo.global().clearTimeout(timer);
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

dojo.lang.shallowCopy = function(obj, deep) {	
	var i, ret;	
	
	if (obj === null) return null;
	
	if (dojo.lang.isObject(obj)) {		
		ret = new obj.constructor();
		for (i in obj) {
			if(dojo.lang.isUndefined(ret[i])) {
				ret[i] = deep ? dojo.lang.shallowCopy(obj[i], deep) : obj[i];
			}
		}
	} else if (dojo.lang.isArray(obj)) {
		ret = [];
		for(i=0; i<obj.length; i++) {
			ret[i] = deep ? dojo.lang.shallowCopy(obj[i], deep) : obj[i];
		}
	} else {
		ret = obj;
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

/**
 * Get a value from a reference specified as a string descriptor,
 * (e.g. "A.B") in the given context.
 * 
 * getObjPathValue(String objpath [, Object context, Boolean create])
 *
 * If context is not specified, dj_global is used
 * If create is true, undefined objects in the path are created.
 */
dojo.lang.getObjPathValue = function(objpath, context, create){
	with(dojo.parseObjPath(objpath, context, create)){
		return dojo.evalProp(prop, obj, create);
	}
}

/**
 * Set a value on a reference specified as a string descriptor. 
 * (e.g. "A.B") in the given context.
 * 
 * setObjPathValue(String objpath, value [, Object context, Boolean create])
 *
 * If context is not specified, dj_global is used
 * If create is true, undefined objects in the path are created.
 */
dojo.lang.setObjPathValue = function(objpath, value, context, create){
	if(arguments.length < 4){
		create = true;
	}
	with(dojo.parseObjPath(objpath, context, create)){
		if(obj && (create || (prop in obj))){
			obj[prop] = value;
		}
	}
}

dojo.provide("dojo.lang.func");


/**
 * Runs a function in a given scope (thisObject), can
 * also be used to preserve scope.
 *
 * hitch(foo, "bar"); // runs foo.bar() in the scope of foo
 * hitch(foo, myFunction); // runs myFunction in the scope of foo
 */
dojo.lang.hitch = function(thisObject, method){
	var fcn = (dojo.lang.isString(method) ? thisObject[method] : method) || function(){};

	return function() {
		return fcn.apply(thisObject, arguments);
	};
}

dojo.lang.anonCtr = 0;
dojo.lang.anon = {};
dojo.lang.nameAnonFunc = function(anonFuncPtr, namespaceObj, searchForNames){
	var nso = (namespaceObj || dojo.lang.anon);
	if( (searchForNames) ||
		((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"] == true)) ){
		for(var x in nso){
			try{
				if(nso[x] === anonFuncPtr){
					return x;
				}
			}catch(e){} // window.external fails in IE embedded in Eclipse (Eclipse bug #151165)
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
	// since the event system replaces the original function with a new
	// join-point runner with an arity of 0, we check to see if it's left us
	// any clues about the original arity in lieu of the function's actual
	// length property
	var ecount = (func["__preJoinArity"]||func.length) - outerArgs.length;
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
			};
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

dojo.provide("dojo.event.common");


// TODO: connection filter functions
//			these are functions that accept a method invocation (like around
//			advice) and return a boolean based on it. That value determines
//			whether or not the connection proceeds. It could "feel" like around
//			advice for those who know what it is (calling proceed() or not),
//			but I think presenting it as a "filter" and/or calling it with the
//			function args and not the MethodInvocation might make it more
//			palletable to "normal" users than around-advice currently is
// TODO: execution scope mangling
//			YUI's event facility by default executes listeners in the context
//			of the source object. This is very odd, but should probably be
//			supported as an option (both for the source and for the dest). It
//			can be thought of as a connection-specific hitch().
// TODO: more resiliency for 4+ arguments to connect()

dojo.event = new function(){
	this._canTimeout = dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);

	// FIXME: where should we put this method (not here!)?
	function interpolateArgs(args, searchForNames){
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
					var tmpName  = dl.nameAnonFunc(args[2], ao.adviceObj, searchForNames);
					ao.adviceFunc = tmpName;
				}else if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){
					ao.adviceType = "after";
					ao.srcObj = dj_global;
					var tmpName  = dl.nameAnonFunc(args[0], ao.srcObj, searchForNames);
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
					var tmpName  = dl.nameAnonFunc(args[1], dj_global, searchForNames);
					ao.srcFunc = tmpName;
					ao.adviceObj = args[2];
					ao.adviceFunc = args[3];
				}else if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){
					ao.srcObj = args[1];
					ao.srcFunc = args[2];
					var tmpName  = dl.nameAnonFunc(args[3], dj_global, searchForNames);
					ao.adviceObj = dj_global;
					ao.adviceFunc = tmpName;
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

		if(dl.isFunction(ao.aroundFunc)){
			var tmpName  = dl.nameAnonFunc(ao.aroundFunc, ao.aroundObj, searchForNames);
			ao.aroundFunc = tmpName;
		}

		if(dl.isFunction(ao.srcFunc)){
			ao.srcFunc = dl.getNameInObj(ao.srcObj, ao.srcFunc);
		}

		if(dl.isFunction(ao.adviceFunc)){
			ao.adviceFunc = dl.getNameInObj(ao.adviceObj, ao.adviceFunc);
		}

		if((ao.aroundObj)&&(dl.isFunction(ao.aroundFunc))){
			ao.aroundFunc = dl.getNameInObj(ao.aroundObj, ao.aroundFunc);
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

	this.connect = function(/*...*/){
		// summary:
		//		dojo.event.connect is the glue that holds most Dojo-based
		//		applications together. Most combinations of arguments are
		//		supported, with the connect() method attempting to disambiguate
		//		the implied types of positional parameters. The following will
		//		all work:
		//			dojo.event.connect("globalFunctionName1", "globalFunctionName2");
		//			dojo.event.connect(functionReference1, functionReference2);
		//			dojo.event.connect("globalFunctionName1", functionReference2);
		//			dojo.event.connect(functionReference1, "globalFunctionName2");
		//			dojo.event.connect(scope1, "functionName1", "globalFunctionName2");
		//			dojo.event.connect("globalFunctionName1", scope2, "functionName2");
		//			dojo.event.connect(scope1, "functionName1", scope2, "functionName2");
		//			dojo.event.connect("after", scope1, "functionName1", scope2, "functionName2");
		//			dojo.event.connect("before", scope1, "functionName1", scope2, "functionName2");
		//			dojo.event.connect("around", 	scope1, "functionName1", 
		//											scope2, "functionName2",
		//											aroundFunctionReference);
		//			dojo.event.connect("around", 	scope1, "functionName1", 
		//											scope2, "functionName2",
		//											scope3, "aroundFunctionName");
		//			dojo.event.connect("before-around", 	scope1, "functionName1", 
		//													scope2, "functionName2",
		//													aroundFunctionReference);
		//			dojo.event.connect("after-around", 		scope1, "functionName1", 
		//													scope2, "functionName2",
		//													aroundFunctionReference);
		//			dojo.event.connect("after-around", 		scope1, "functionName1", 
		//													scope2, "functionName2",
		//													scope3, "aroundFunctionName");
		//			dojo.event.connect("around", 	scope1, "functionName1", 
		//											scope2, "functionName2",
		//											scope3, "aroundFunctionName", true, 30);
		//			dojo.event.connect("around", 	scope1, "functionName1", 
		//											scope2, "functionName2",
		//											scope3, "aroundFunctionName", null, null, 10);
		// adviceType: 
		//		Optional. String. One of "before", "after", "around",
		//		"before-around", or "after-around". FIXME
		// srcObj:
		//		the scope in which to locate/execute the named srcFunc. Along
		//		with srcFunc, this creates a way to dereference the function to
		//		call. So if the function in question is "foo.bar", the
		//		srcObj/srcFunc pair would be foo and "bar", where "bar" is a
		//		string and foo is an object reference.
		// srcFunc:
		//		the name of the function to connect to. When it is executed,
		//		the listener being registered with this call will be called.
		//		The adviceType defines the call order between the source and
		//		the target functions.
		// adviceObj:
		//		the scope in which to locate/execute the named adviceFunc.
		// adviceFunc:
		//		the name of the function being conected to srcObj.srcFunc
		// aroundObj:
		//		the scope in which to locate/execute the named aroundFunc.
		// aroundFunc:
		//		the name of, or a reference to, the function that will be used
		//		to mediate the advice call. Around advice requires a special
		//		unary function that will be passed a "MethodInvocation" object.
		//		These objects have several important properties, namely:
		//			- args
		//				a mutable array of arguments to be passed into the
		//				wrapped function
		//			- proceed
		//				a function that "continues" the invocation. The result
		//				of this function is the return of the wrapped function.
		//				You can then manipulate this return before passing it
		//				back out (or take further action based on it).
		// once:
		//		boolean that determines whether or not this connect() will
		//		create a new connection if an identical connect() has already
		//		been made. Defaults to "false".
		// delay:
		//		an optional delay (in ms), as an integer, for dispatch of a
		//		listener after the source has been fired.
		// rate:
		//		an optional rate throttling parameter (integer, in ms). When
		//		specified, this particular connection will not fire more than
		//		once in the interval specified by the rate
		// adviceMsg:
		//		boolean. Should the listener have all the parameters passed in
		//		as a single argument?

		/*
				ao.adviceType = args[0];
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
		*/
		if(arguments.length == 1){
			var ao = arguments[0];
		}else{
			var ao = interpolateArgs(arguments, true);
		}
		if(dojo.lang.isString(ao.srcFunc) && (ao.srcFunc.toLowerCase() == "onkey") ){
			if(dojo.render.html.ie){
				ao.srcFunc = "onkeydown";
				this.connect(ao);
			}
			ao.srcFunc = "onkeypress";
		}


		if(dojo.lang.isArray(ao.srcObj) && ao.srcObj!=""){
			var tmpAO = {};
			for(var x in ao){
				tmpAO[x] = ao[x];
			}
			var mjps = [];
			dojo.lang.forEach(ao.srcObj, function(src){
				if((dojo.render.html.capable)&&(dojo.lang.isString(src))){
					src = dojo.byId(src);
					// dojo.debug(src);
				}
				tmpAO.srcObj = src;
				// dojo.debug(tmpAO.srcObj, tmpAO.srcFunc);
				// dojo.debug(tmpAO.adviceObj, tmpAO.adviceFunc);
				mjps.push(dojo.event.connect.call(dojo.event, tmpAO));
			});
			return mjps;
		}

		// FIXME: just doing a "getForMethod()" seems to be enough to put this into infinite recursion!!
		var mjp = dojo.event.MethodJoinPoint.getForMethod(ao.srcObj, ao.srcFunc);
		if(ao.adviceFunc){
			var mjp2 = dojo.event.MethodJoinPoint.getForMethod(ao.adviceObj, ao.adviceFunc);
		}

		mjp.kwAddAdvice(ao);

		// advanced users might want to fsck w/ the join point manually
		return mjp; // a MethodJoinPoint object
	}

	this.log = function(/*object or funcName*/ a1, /*funcName*/ a2){
		// summary:
		//		a function that will wrap and log all calls to the specified
		//		a1.a2() function. If only a1 is passed, it'll be used as a
		//		function or function name on the global context. Logging will
		//		be sent to dojo.debug
		// a1:
		//		if a2 is passed, this should be an object. If not, it can be a
		//		function or function name.
		// a2:
		//		a function name
		var kwArgs;
		if((arguments.length == 1)&&(typeof a1 == "object")){
			kwArgs = a1;
		}else{
			kwArgs = {
				srcObj: a1,
				srcFunc: a2
			};
		}
		kwArgs.adviceFunc = function(){
			var argsStr = [];
			for(var x=0; x<arguments.length; x++){
				argsStr.push(arguments[x]);
			}
			dojo.debug("("+kwArgs.srcObj+")."+kwArgs.srcFunc, ":", argsStr.join(", "));
		}
		this.kwConnect(kwArgs);
	}

	this.connectBefore = function(){
		// summary:
		//	 	takes the same parameters as dojo.event.connect(), except that
		//	 	the advice type will always be "before"
		var args = ["before"];
		for(var i = 0; i < arguments.length; i++){ args.push(arguments[i]); }
		return this.connect.apply(this, args); // a MethodJoinPoint object
	}

	this.connectAround = function(){
		// summary:
		//	 	takes the same parameters as dojo.event.connect(), except that
		//	 	the advice type will always be "around"
		var args = ["around"];
		for(var i = 0; i < arguments.length; i++){ args.push(arguments[i]); }
		return this.connect.apply(this, args); // a MethodJoinPoint object
	}

	this.connectOnce = function(){
		// summary:
		//	 	takes the same parameters as dojo.event.connect(), except that
		//	 	the "once" flag will always be set to "true"
		var ao = interpolateArgs(arguments, true);
		ao.once = true;
		return this.connect(ao); // a MethodJoinPoint object
	}

	this._kwConnectImpl = function(kwArgs, disconnect){
		var fn = (disconnect) ? "disconnect" : "connect";
		if(typeof kwArgs["srcFunc"] == "function"){
			kwArgs.srcObj = kwArgs["srcObj"]||dj_global;
			var tmpName  = dojo.lang.nameAnonFunc(kwArgs.srcFunc, kwArgs.srcObj, true);
			kwArgs.srcFunc = tmpName;
		}
		if(typeof kwArgs["adviceFunc"] == "function"){
			kwArgs.adviceObj = kwArgs["adviceObj"]||dj_global;
			var tmpName  = dojo.lang.nameAnonFunc(kwArgs.adviceFunc, kwArgs.adviceObj, true);
			kwArgs.adviceFunc = tmpName;
		}
		kwArgs.srcObj = kwArgs["srcObj"]||dj_global;
		kwArgs.adviceObj = kwArgs["adviceObj"]||kwArgs["targetObj"]||dj_global;
		kwArgs.adviceFunc = kwArgs["adviceFunc"]||kwArgs["targetFunc"];
		// pass kwargs to avoid unrolling/repacking
		return dojo.event[fn](kwArgs);
	}

	this.kwConnect = function(/*Object*/ kwArgs){
		// summary:
		//		A version of dojo.event.connect() that takes a map of named
		//		parameters instead of the positional parameters that
		//		dojo.event.connect() uses. For many advanced connection types,
		//		this can be a much more readable (and potentially faster)
		//		alternative.
		// kwArgs:
		// 		An object that can have the following properties:
		//			- adviceType
		//			- srcObj
		//			- srcFunc
		//			- adviceObj
		//			- adviceFunc 
		//			- aroundObj
		//			- aroundFunc
		//			- once
		//			- delay
		//			- rate
		//			- adviceMsg
		//		As with connect, only srcFunc and adviceFunc are generally
		//		required

		return this._kwConnectImpl(kwArgs, false); // a MethodJoinPoint object

	}

	this.disconnect = function(){
		// summary:
		//		Takes the same parameters as dojo.event.connect() but destroys
		//		an existing connection instead of building a new one. For
		//		multiple identical connections, multiple disconnect() calls
		//		will unroll one each time it's called.
		if(arguments.length == 1){
			var ao = arguments[0];
		}else{
			var ao = interpolateArgs(arguments, true);
		}
		if(!ao.adviceFunc){ return; } // nothing to disconnect
		if(dojo.lang.isString(ao.srcFunc) && (ao.srcFunc.toLowerCase() == "onkey") ){
			if(dojo.render.html.ie){
				ao.srcFunc = "onkeydown";
				this.disconnect(ao);
			}
			ao.srcFunc = "onkeypress";
		}
		var mjp = dojo.event.MethodJoinPoint.getForMethod(ao.srcObj, ao.srcFunc);
		return mjp.removeAdvice(ao.adviceObj, ao.adviceFunc, ao.adviceType, ao.once); // a MethodJoinPoint object
	}

	this.kwDisconnect = function(kwArgs){
		// summary:
		//		Takes the same parameters as dojo.event.kwConnect() but
		//		destroys an existing connection instead of building a new one.
		return this._kwConnectImpl(kwArgs, true);
	}
}

// exactly one of these is created whenever a method with a joint point is run,
// if there is at least one 'around' advice.
dojo.event.MethodInvocation = function(/*dojo.event.MethodJoinPoint*/join_point, /*Object*/obj, /*Array*/args){
	// summary:
	//		a class the models the call into a function. This is used under the
	//		covers for all method invocations on both ends of a
	//		connect()-wrapped function dispatch. This allows us to "pickle"
	//		calls, such as in the case of around advice.
	// join_point:
	//		a dojo.event.MethodJoinPoint object that represents a connection
	// obj:
	//		the scope the call will execute in
	// args:
	//		an array of parameters that will get passed to the callee
	this.jp_ = join_point;
	this.object = obj;
	this.args = [];
	// make sure we don't lock into a mutable object which can change under us.
	// It's ok if the individual items change, though.
	for(var x=0; x<args.length; x++){
		this.args[x] = args[x];
	}
	// the index of the 'around' that is currently being executed.
	this.around_index = -1;
}

dojo.event.MethodInvocation.prototype.proceed = function(){
	// summary:
	//		proceed with the method call that's represented by this invocation
	//		object
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


dojo.event.MethodJoinPoint = function(/*Object*/obj, /*String*/funcName){
	this.object = obj||dj_global;
	this.methodname = funcName;
	this.methodfunc = this.object[funcName];
	this.squelch = false;
	// this.before = [];
	// this.after = [];
	// this.around = [];
}

dojo.event.MethodJoinPoint.getForMethod = function(/*Object*/obj, /*String*/funcName){
	// summary:
	//		"static" class function for returning a MethodJoinPoint from a
	//		scoped function. If one doesn't exist, one is created.
	// obj:
	//		the scope to search for the function in
	// funcName:
	//		the name of the function to return a MethodJoinPoint for
	if(!obj){ obj = dj_global; }
	if(!obj[funcName]){
		// supply a do-nothing method implementation
		obj[funcName] = function(){};
		if(!obj[funcName]){
			// e.g. cannot add to inbuilt objects in IE6
			dojo.raise("Cannot set do-nothing method on that object "+funcName);
		}
	}else if((!dojo.lang.isFunction(obj[funcName]))&&(!dojo.lang.isAlien(obj[funcName]))){
		// FIXME: should we throw an exception here instead?
		return null; 
	}
	// we hide our joinpoint instance in obj[funcName + '$joinpoint']
	var jpname = funcName + "$joinpoint";
	var jpfuncname = funcName + "$joinpoint$method";
	var joinpoint = obj[jpname];
	if(!joinpoint){
		var isNode = false;
		if(dojo.event["browser"]){
			if( (obj["attachEvent"])||
				(obj["nodeType"])||
				(obj["addEventListener"]) ){
				isNode = true;
				dojo.event.browser.addClobberNodeAttrs(obj, [jpname, jpfuncname, funcName]);
			}
		}
		var origArity = obj[funcName].length;
		obj[jpfuncname] = obj[funcName];
		// joinpoint = obj[jpname] = new dojo.event.MethodJoinPoint(obj, funcName);
		joinpoint = obj[jpname] = new dojo.event.MethodJoinPoint(obj, jpfuncname);
		obj[funcName] = function(){ 
			var args = [];

			if((isNode)&&(!arguments.length)){
				var evt = null;
				try{
					if(obj.ownerDocument){
						evt = obj.ownerDocument.parentWindow.event;
					}else if(obj.documentElement){
						evt = obj.documentElement.ownerDocument.parentWindow.event;
					}else if(obj.event){ //obj is a window
						evt = obj.event;
					}else{
						evt = window.event;
					}
				}catch(e){
					evt = window.event;
				}

				if(evt){
					args.push(dojo.event.browser.fixEvent(evt, this));
				}
			}else{
				for(var x=0; x<arguments.length; x++){
					if((x==0)&&(isNode)&&(dojo.event.browser.isEvent(arguments[x]))){
						args.push(dojo.event.browser.fixEvent(arguments[x], this));
					}else{
						args.push(arguments[x]);
					}
				}
			}
			// return joinpoint.run.apply(joinpoint, arguments); 
			return joinpoint.run.apply(joinpoint, args); 
		}
		obj[funcName].__preJoinArity = origArity;
	}
	return joinpoint; // dojo.event.MethodJoinPoint
}

dojo.lang.extend(dojo.event.MethodJoinPoint, {
	unintercept: function(){
		// summary: 
		//		destroy the connection to all listeners that may have been
		//		registered on this joinpoint
		this.object[this.methodname] = this.methodfunc;
		this.before = [];
		this.after = [];
		this.around = [];
	},

	disconnect: dojo.lang.forward("unintercept"),

	run: function(){
		// summary:
		//		execute the connection represented by this join point. The
		//		arguments passed to run() will be passed to the function and
		//		its listeners.
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
					if(dojo.event._canTimeout){
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

		var unRollSquelch = function(){
			if(this.squelch){
				try{
					return unrollAdvice.apply(this, arguments);
				}catch(e){ 
					dojo.debug(e);
				}
			}else{
				return unrollAdvice.apply(this, arguments);
			}
		}

		if((this["before"])&&(this.before.length>0)){
			// pass a cloned array, if this event disconnects this event forEach on this.before wont work
			dojo.lang.forEach(this.before.concat(new Array()), unRollSquelch);
		}

		var result;
		try{
			if((this["around"])&&(this.around.length>0)){
				var mi = new dojo.event.MethodInvocation(this, obj, args);
				result = mi.proceed();
			}else if(this.methodfunc){
				result = this.object[this.methodname].apply(this.object, args);
			}
		}catch(e){ if(!this.squelch){ dojo.raise(e); } }

		if((this["after"])&&(this.after.length>0)){
			// see comment on this.before above
			dojo.lang.forEach(this.after.concat(new Array()), unRollSquelch);
		}

		return (this.methodfunc) ? result : null;
	},

	getArr: function(/*String*/kind){
		// summary: return a list of listeners of the past "kind"
		// kind:
		//		can be one of: "before", "after", "around", "before-around", or
		//		"after-around"
		var type = "after";
		// FIXME: we should be able to do this through props or Array.in()
		if((typeof kind == "string")&&(kind.indexOf("before")!=-1)){
			type = "before";
		}else if(kind=="around"){
			type = "around";
		}
		if(!this[type]){ this[type] = []; }
		return this[type]; // Array
	},

	kwAddAdvice: function(/*Object*/args){
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
			var aao = (typeof thisAdvice == "object") ? (new String(thisAdvice)).toString() : thisAdvice;
			var a1o = (typeof arr[x][1] == "object") ? (new String(arr[x][1])).toString() : arr[x][1];
			if((arr[x][0] == thisAdviceObj)&&(a1o == aao)){
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

dojo.provide("dojo.event.browser");

// FIXME: any particular reason this is in the global scope?
dojo._ie_clobber = new function(){
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
			tna = nodeRef.all || nodeRef.getElementsByTagName("*");
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
			try{
				if(el && el["__clobberAttrs__"]){
					for(var j=0; j<el.__clobberAttrs__.length; j++){
						nukeProp(el, el.__clobberAttrs__[j]);
					}
					nukeProp(el, "__clobberAttrs__");
					nukeProp(el, "__doClobber__");
				}
			}catch(e){ /* squelch! */};
		}
		na = null;
	}
}

if(dojo.render.html.ie){
	dojo.addOnUnload(function(){
		dojo._ie_clobber.clobber();
		try{
			if((dojo["widget"])&&(dojo.widget["manager"])){
				dojo.widget.manager.destroyAll();
			}
		}catch(e){}
		try{ window.onload = null; }catch(e){}
		try{ window.onunload = null; }catch(e){}
		dojo._ie_clobber.clobberNodes = [];
		// CollectGarbage();
	});
}

dojo.event.browser = new function(){

	var clobberIdx = 0;

	this.clean = function(node){
		if(dojo.render.html.ie){ 
			dojo._ie_clobber.clobber(node);
		}
	}

	this.addClobberNode = function(node){
		if(!dojo.render.html.ie){ return; }
		if(!node["__doClobber__"]){
			node.__doClobber__ = true;
			dojo._ie_clobber.clobberNodes.push(node);
			// this might not be the most efficient thing to do, but it's
			// much less error prone than other approaches which were
			// previously tried and failed
			node.__clobberAttrs__ = [];
		}
	}

	this.addClobberNodeAttrs = function(node, props){
		if(!dojo.render.html.ie){ return; }
		this.addClobberNode(node);
		for(var x=0; x<props.length; x++){
			node.__clobberAttrs__.push(props[x]);
		}
	}

	this.removeListener = function(node, evtName, fp, capture){
		if(!capture){ var capture = false; }
		evtName = evtName.toLowerCase();
		if( (evtName == "onkey") || (evtName == "key") ){
			if(dojo.render.html.ie){
				this.removeListener(node, "onkeydown", fp, capture);
			}
			evtName = "onkeypress";
		}
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
		if( (evtName == "onkey") || (evtName == "key") ){
			if(dojo.render.html.ie){
				this.addListener(node, "onkeydown", fp, capture, dontFix);
			}
			evtName = "onkeypress";
		}
		if(evtName.substr(0,2)!="on"){ evtName = "on"+evtName; }

		if(!dontFix){
			// build yet another closure around fp in order to inject fixEvent
			// around the resulting event
			var newfp = function(evt){
				if(!evt){ evt = window.event; }
				var ret = fp(dojo.event.browser.fixEvent(evt, this));
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
		KEY_CLEAR: 12,
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
		KEY_HELP: 47,
		KEY_LEFT_WINDOW: 91,
		KEY_RIGHT_WINDOW: 92,
		KEY_SELECT: 93,
		KEY_NUMPAD_0: 96,
		KEY_NUMPAD_1: 97,
		KEY_NUMPAD_2: 98,
		KEY_NUMPAD_3: 99,
		KEY_NUMPAD_4: 100,
		KEY_NUMPAD_5: 101,
		KEY_NUMPAD_6: 102,
		KEY_NUMPAD_7: 103,
		KEY_NUMPAD_8: 104,
		KEY_NUMPAD_9: 105,
		KEY_NUMPAD_MULTIPLY: 106,
		KEY_NUMPAD_PLUS: 107,
		KEY_NUMPAD_ENTER: 108,
		KEY_NUMPAD_MINUS: 109,
		KEY_NUMPAD_PERIOD: 110,
		KEY_NUMPAD_DIVIDE: 111,
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
		KEY_F13: 124,
		KEY_F14: 125,
		KEY_F15: 126,
		KEY_NUM_LOCK: 144,
		KEY_SCROLL_LOCK: 145
	};

	// reverse lookup
	this.revKeys = [];
	for(var key in this.keys){
		this.revKeys[this.keys[key]] = key;
	}

	this.fixEvent = function(evt, sender){
		if(!evt){
			if(window["event"]){
				evt = window.event;
			}
		}
		
		if((evt["type"])&&(evt["type"].indexOf("key") == 0)){ // key events
			evt.keys = this.revKeys;
			// FIXME: how can we eliminate this iteration?
			for(var key in this.keys){
				evt[key] = this.keys[key];
			}
			if(evt["type"] == "keydown" && dojo.render.html.ie){
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
					// I'll get these in keypress after the OS munges them based on numlock
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
						break; // just ignore the keys that can morph
					case evt.KEY_NUMPAD_MULTIPLY:
					case evt.KEY_NUMPAD_PLUS:
					case evt.KEY_NUMPAD_ENTER:
					case evt.KEY_NUMPAD_MINUS:
					case evt.KEY_NUMPAD_DIVIDE:
						break; // I could handle these but just pick them up in keypress
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
						evt.key = evt.keyCode;
						break;
					default:
						if(evt.ctrlKey || evt.altKey){
							var unifiedCharCode = evt.keyCode;
							// if lower case but keycode is uppercase, convert it
							if(unifiedCharCode >= 65 && unifiedCharCode <= 90 && evt.shiftKey == false){
								unifiedCharCode += 32;
							}
							if(unifiedCharCode >= 1 && unifiedCharCode <= 26 && evt.ctrlKey){
								unifiedCharCode += 96; // 001-032 = ctrl+[a-z]
							}
							evt.key = String.fromCharCode(unifiedCharCode);
						}
				}
			} else if(evt["type"] == "keypress"){
				if(dojo.render.html.opera){
					if(evt.which == 0){
						evt.key = evt.keyCode;
					}else if(evt.which > 0){
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
								evt.key = evt.which;
								break;
							default:
								var unifiedCharCode = evt.which;
								if((evt.ctrlKey || evt.altKey || evt.metaKey) && (evt.which >= 65 && evt.which <= 90 && evt.shiftKey == false)){
									unifiedCharCode += 32;
								}
								evt.key = String.fromCharCode(unifiedCharCode);
						}
					}
				}else if(dojo.render.html.ie){ // catch some IE keys that are hard to get in keyDown
					// key combinations were handled in onKeyDown
					if(!evt.ctrlKey && !evt.altKey && evt.keyCode >= evt.KEY_SPACE){
						evt.key = String.fromCharCode(evt.keyCode);
					}
				}else if(dojo.render.html.safari){
					switch(evt.keyCode){
						case 63232: evt.key = evt.KEY_UP_ARROW; break;
						case 63233: evt.key = evt.KEY_DOWN_ARROW; break;
						case 63234: evt.key = evt.KEY_LEFT_ARROW; break;
						case 63235: evt.key = evt.KEY_RIGHT_ARROW; break;
						default: 
							evt.key = evt.charCode > 0 ? String.fromCharCode(evt.charCode) : evt.keyCode;
					}
				}else{
					evt.key = evt.charCode > 0 ? String.fromCharCode(evt.charCode) : evt.keyCode;
				}
			}
		}
		if(dojo.render.html.ie){
			if(!evt.target){ evt.target = evt.srcElement; }
			if(!evt.currentTarget){ evt.currentTarget = (sender ? sender : evt.srcElement); }
			if(!evt.layerX){ evt.layerX = evt.offsetX; }
			if(!evt.layerY){ evt.layerY = evt.offsetY; }
			// FIXME: scroll position query is duped from dojo.html to avoid dependency on that entire module
			// DONOT replace the following to use dojo.body(), in IE, document.documentElement should be used
			// here rather than document.body
			var doc = (evt.srcElement && evt.srcElement.ownerDocument) ? evt.srcElement.ownerDocument : document;
			var docBody = ((dojo.render.html.ie55)||(doc["compatMode"] == "BackCompat")) ? doc.body : doc.documentElement;
			if(!evt.pageX){ evt.pageX = evt.clientX + (docBody.scrollLeft || 0) }
			if(!evt.pageY){ evt.pageY = evt.clientY + (docBody.scrollTop || 0) }
			// mouseover
			if(evt.type == "mouseover"){ evt.relatedTarget = evt.fromElement; }
			// mouseout
			if(evt.type == "mouseout"){ evt.relatedTarget = evt.toElement; }
			this.currentEvent = evt;
			evt.callListener = this.callListener;
			evt.stopPropagation = this.stopPropagation;
			evt.preventDefault = this.preventDefault;
		}
		return evt;
	}

	this.stopEvent = function(ev){
		if(window.event){
			ev.returnValue = false;
			ev.cancelBubble = true;
		}else{
			ev.preventDefault();
			ev.stopPropagation();
		}
	}
}

dojo.provide("dojo.dom");

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
	//	summary
	//	aliases for various common XML namespaces
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

dojo.dom.isNode = function(/* object */wh){
	//	summary
	//	checks to see if wh is actually a node.
	if(typeof Element == "function") {
		try {
			return wh instanceof Element;	//	boolean
		} catch(E) {}
	} else {
		// best-guess
		return wh && !isNaN(wh.nodeType);	//	boolean
	}
}

dojo.dom.getUniqueId = function(){
	//	summary
	//	returns a unique string for use with any DOM element
	var _document = dojo.doc();
	do {
		var id = "dj_unique_" + (++arguments.callee._idIncrement);
	}while(_document.getElementById(id));
	return id;	//	string
}
dojo.dom.getUniqueId._idIncrement = 0;

dojo.dom.firstElement = dojo.dom.getFirstChildElement = function(/* Element */parentNode, /* string? */tagName){
	//	summary
	//	returns the first child element matching tagName
	var node = parentNode.firstChild;
	while(node && node.nodeType != dojo.dom.ELEMENT_NODE){
		node = node.nextSibling;
	}
	if(tagName && node && node.tagName && node.tagName.toLowerCase() != tagName.toLowerCase()) {
		node = dojo.dom.nextElement(node, tagName);
	}
	return node;	//	Element
}

dojo.dom.lastElement = dojo.dom.getLastChildElement = function(/* Element */parentNode, /* string? */tagName){
	//	summary
	//	returns the last child element matching tagName
	var node = parentNode.lastChild;
	while(node && node.nodeType != dojo.dom.ELEMENT_NODE) {
		node = node.previousSibling;
	}
	if(tagName && node && node.tagName && node.tagName.toLowerCase() != tagName.toLowerCase()) {
		node = dojo.dom.prevElement(node, tagName);
	}
	return node;	//	Element
}

dojo.dom.nextElement = dojo.dom.getNextSiblingElement = function(/* Node */node, /* string? */tagName){
	//	summary
	//	returns the next sibling element matching tagName
	if(!node) { return null; }
	do {
		node = node.nextSibling;
	} while(node && node.nodeType != dojo.dom.ELEMENT_NODE);

	if(node && tagName && tagName.toLowerCase() != node.tagName.toLowerCase()) {
		return dojo.dom.nextElement(node, tagName);
	}
	return node;	//	Element
}

dojo.dom.prevElement = dojo.dom.getPreviousSiblingElement = function(/* Node */node, /* string? */tagName){
	//	summary
	//	returns the previous sibling element matching tagName
	if(!node) { return null; }
	if(tagName) { tagName = tagName.toLowerCase(); }
	do {
		node = node.previousSibling;
	} while(node && node.nodeType != dojo.dom.ELEMENT_NODE);

	if(node && tagName && tagName.toLowerCase() != node.tagName.toLowerCase()) {
		return dojo.dom.prevElement(node, tagName);
	}
	return node;	//	Element
}

// TODO: hmph
/*this.forEachChildTag = function(node, unaryFunc) {
	var child = this.getFirstChildTag(node);
	while(child) {
		if(unaryFunc(child) == "break") { break; }
		child = this.getNextSiblingTag(child);
	}
}*/

dojo.dom.moveChildren = function(/* Element */srcNode, /* Element */destNode, /* boolean? */trim){
	//	summary
	//	Moves children from srcNode to destNode and returns the count of children moved; 
	//		will trim off text nodes if trim == true
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
	return count;	//	number
}

dojo.dom.copyChildren = function(/* Element */srcNode, /* Element */destNode, /* boolean? */trim){
	//	summary
	//	Copies children from srcNde to destNode and returns the count of children copied;
	//		will trim off text nodes if trim == true
	var clonedNode = srcNode.cloneNode(true);
	return this.moveChildren(clonedNode, destNode, trim);	//	number
}

dojo.dom.removeChildren = function(/* Element */node){
	//	summary
	//	removes all children from node and returns the count of children removed.
	var count = node.childNodes.length;
	while(node.hasChildNodes()){ node.removeChild(node.firstChild); }
	return count;	//	number
}

dojo.dom.replaceChildren = function(/* Element */node, /* Node */newChild){
	//	summary
	//	Removes all children of node and appends newChild
	// FIXME: what if newChild is an array-like object?
	dojo.dom.removeChildren(node);
	node.appendChild(newChild);
}

dojo.dom.removeNode = function(/* Node */node){
	//	summary
	//	if node has a parent, removes node from parent and returns a reference to the removed child.
	if(node && node.parentNode){
		// return a ref to the removed child
		return node.parentNode.removeChild(node);	//	Node
	}
}

dojo.dom.getAncestors = function(/* Node */node, /* function? */filterFunction, /* boolean? */returnFirstHit) {
	//	summary
	//	returns all ancestors matching optional filterFunction; will return only the first if returnFirstHit
	var ancestors = [];
	var isFunction = (filterFunction && (filterFunction instanceof Function || typeof filterFunction == "function"));
	while(node) {
		if (!isFunction || filterFunction(node)) {
			ancestors.push(node);
		}
		if (returnFirstHit && ancestors.length > 0) { 
			return ancestors[0]; 	//	Node
		}
		
		node = node.parentNode;
	}
	if (returnFirstHit) { return null; }
	return ancestors;	//	array
}

dojo.dom.getAncestorsByTag = function(/* Node */node, /* string */tag, /* boolean? */returnFirstHit) {
	//	summary
	//	returns all ancestors matching tag (as tagName), will only return first one if returnFirstHit
	tag = tag.toLowerCase();
	return dojo.dom.getAncestors(node, function(el){
		return ((el.tagName)&&(el.tagName.toLowerCase() == tag));
	}, returnFirstHit);	//	Node || array
}

dojo.dom.getFirstAncestorByTag = function(/* Node */node, /* string */tag) {
	//	summary
	//	Returns first ancestor of node with tag tagName
	return dojo.dom.getAncestorsByTag(node, tag, true);	//	Node
}

dojo.dom.isDescendantOf = function(/* Node */node, /* Node */ancestor, /* boolean? */guaranteeDescendant){
	//	summary
	//	Returns boolean if node is a descendant of ancestor
	// guaranteeDescendant allows us to be a "true" isDescendantOf function
	if(guaranteeDescendant && node) { node = node.parentNode; }
	while(node) {
		if(node == ancestor){ 
			return true; 	//	boolean
		}
		node = node.parentNode;
	}
	return false;	//	boolean
}

dojo.dom.innerXML = function(/* Node */node){
	//	summary
	//	Implementation of MS's innerXML function.
	if(node.innerXML){
		return node.innerXML;	//	string
	}else if (node.xml){
		return node.xml;		//	string
	}else if(typeof XMLSerializer != "undefined"){
		return (new XMLSerializer()).serializeToString(node);	//	string
	}
}

dojo.dom.createDocument = function(){
	//	summary
	//	cross-browser implementation of creating an XML document object.
	var doc = null;
	var _document = dojo.doc();

	if(!dj_undef("ActiveXObject")){
		var prefixes = [ "MSXML2", "Microsoft", "MSXML", "MSXML3" ];
		for(var i = 0; i<prefixes.length; i++){
			try{
				doc = new ActiveXObject(prefixes[i]+".XMLDOM");
			}catch(e){ /* squelch */ };

			if(doc){ break; }
		}
	}else if((_document.implementation)&&
		(_document.implementation.createDocument)){
		doc = _document.implementation.createDocument("", "", null);
	}
	
	return doc;	//	DOMDocument
}

dojo.dom.createDocumentFromText = function(/* string */str, /* string? */mimetype){
	//	summary
	//	attempts to create a Document object based on optional mime-type, using str as the contents of the document
	if(!mimetype){ mimetype = "text/xml"; }
	if(!dj_undef("DOMParser")){
		var parser = new DOMParser();
		return parser.parseFromString(str, mimetype);	//	DOMDocument
	}else if(!dj_undef("ActiveXObject")){
		var domDoc = dojo.dom.createDocument();
		if(domDoc){
			domDoc.async = false;
			domDoc.loadXML(str);
			return domDoc;	//	DOMDocument
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
	}else{
		var _document = dojo.doc();
		if(_document.createElement){
			// FIXME: this may change all tags to uppercase!
			var tmp = _document.createElement("xml");
			tmp.innerHTML = str;
			if(_document.implementation && _document.implementation.createDocument) {
				var xmlDoc = _document.implementation.createDocument("foo", "", null);
				for(var i = 0; i < tmp.childNodes.length; i++) {
					xmlDoc.importNode(tmp.childNodes.item(i), true);
				}
				return xmlDoc;	//	DOMDocument
			}
			// FIXME: probably not a good idea to have to return an HTML fragment
			// FIXME: the tmp.doc.firstChild is as tested from IE, so it may not
			// work that way across the board
			return ((tmp.document)&&
				(tmp.document.firstChild ?  tmp.document.firstChild : tmp));	//	DOMDocument
		}
	}
	return null;
}

dojo.dom.prependChild = function(/* Element */node, /* Element */parent) {
	// summary
	//	prepends node to parent's children nodes
	if(parent.firstChild) {
		parent.insertBefore(node, parent.firstChild);
	} else {
		parent.appendChild(node);
	}
	return true;	//	boolean
}

dojo.dom.insertBefore = function(/* Node */node, /* Node */ref, /* boolean? */force){
	//	summary
	//	Try to insert node before ref
	if (force != true &&
		(node === ref || node.nextSibling === ref)){ return false; }
	var parent = ref.parentNode;
	parent.insertBefore(node, ref);
	return true;	//	boolean
}

dojo.dom.insertAfter = function(/* Node */node, /* Node */ref, /* boolean? */force){
	//	summary
	//	Try to insert node after ref
	var pn = ref.parentNode;
	if(ref == pn.lastChild){
		if((force != true)&&(node === ref)){
			return false;	//	boolean
		}
		pn.appendChild(node);
	}else{
		return this.insertBefore(node, ref.nextSibling, force);	//	boolean
	}
	return true;	//	boolean
}

dojo.dom.insertAtPosition = function(/* Node */node, /* Node */ref, /* string */position){
	//	summary
	//	attempt to insert node in relation to ref based on position
	if((!node)||(!ref)||(!position)){ 
		return false;	//	boolean 
	}
	switch(position.toLowerCase()){
		case "before":
			return dojo.dom.insertBefore(node, ref);	//	boolean
		case "after":
			return dojo.dom.insertAfter(node, ref);		//	boolean
		case "first":
			if(ref.firstChild){
				return dojo.dom.insertBefore(node, ref.firstChild);	//	boolean
			}else{
				ref.appendChild(node);
				return true;	//	boolean
			}
			break;
		default: // aka: last
			ref.appendChild(node);
			return true;	//	boolean
	}
}

dojo.dom.insertAtIndex = function(/* Node */node, /* Element */containingNode, /* number */insertionIndex){
	//	summary
	//	insert node into child nodes nodelist of containingNode at insertionIndex.
	var siblingNodes = containingNode.childNodes;

	// if there aren't any kids yet, just add it to the beginning

	if (!siblingNodes.length){
		containingNode.appendChild(node);
		return true;	//	boolean
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

		return dojo.dom.insertAfter(node, after);	//	boolean
	}else{
		// add it to the start

		return dojo.dom.insertBefore(node, siblingNodes.item(0));	//	boolean
	}
}
	
dojo.dom.textContent = function(/* Node */node, /* string */text){
	//	summary
	//	implementation of the DOM Level 3 attribute; scan node for text
	if (arguments.length>1) {
		var _document = dojo.doc();
		dojo.dom.replaceChildren(node, _document.createTextNode(text));
		return text;	//	string
	} else {
		if(node.textContent != undefined){ //FF 1.5
			return node.textContent;	//	string
		}
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
		return _result;	//	string
	}
}

dojo.dom.hasParent = function (/* Node */node) {
	//	summary
	//	returns whether or not node is a child of another node.
	return node && node.parentNode && dojo.dom.isNode(node.parentNode);	//	boolean
}

/**
 * Examples:
 *
 * myFooNode = <foo />
 * isTag(myFooNode, "foo"); // returns "foo"
 * isTag(myFooNode, "bar"); // returns ""
 * isTag(myFooNode, "FOO"); // returns ""
 * isTag(myFooNode, "hey", "foo", "bar"); // returns "foo"
**/
dojo.dom.isTag = function(/* Node */node /* ... */) {
	//	summary
	//	determines if node has any of the provided tag names and returns the tag name that matches, empty string otherwise.
	if(node && node.tagName) {
		for(var i=1; i<arguments.length; i++){
			if(node.tagName==String(arguments[i])){
				return String(arguments[i]);	//	string
			}
		}
	}
	return "";	//	string
}

dojo.dom.setAttributeNS = function(/* Element */elem, /* string */namespaceURI, /* string */attrName, /* string */attrValue){
	//	summary
	//	implementation of DOM2 setAttributeNS that works cross browser.
	if(elem == null || ((elem == undefined)&&(typeof elem == "undefined"))){
		dojo.raise("No element given to dojo.dom.setAttributeNS");
	}
	
	if(!((elem.setAttributeNS == undefined)&&(typeof elem.setAttributeNS == "undefined"))){ // w3c
		elem.setAttributeNS(namespaceURI, attrName, attrValue);
	}else{ // IE
		// get a root XML document
		var ownerDoc = elem.ownerDocument;
		var attribute = ownerDoc.createNode(
			2, // node type
			attrName,
			namespaceURI
		);
		
		// set value
		attribute.nodeValue = attrValue;
		
		// attach to element
		elem.setAttributeNode(attribute);
	}
}

/*		This is the dojo logging facility, which is imported from nWidgets
		(written by Alex Russell, CLA on file), which is patterned on the
		Python logging module, which in turn has been heavily influenced by
		log4j (execpt with some more pythonic choices, which we adopt as well).

		While the dojo logging facilities do provide a set of familiar
		interfaces, many of the details are changed to reflect the constraints
		of the browser environment. Mainly, file and syslog-style logging
		facilites are not provided, with HTTP POST and GET requests being the
		only ways of getting data from the browser back to a server. Minimal
		support for this (and XML serialization of logs) is provided, but may
		not be of practical use in a deployment environment.

		The Dojo logging classes are agnostic of any environment, and while
		default loggers are provided for browser-based interpreter
		environments, this file and the classes it define are explicitly
		designed to be portable to command-line interpreters and other
		ECMA-262v3 envrionments.

	the logger needs to accomidate:
		log "levels"
		type identifiers
		file?
		message
		tic/toc?

	The logger should ALWAYS record:
		time/date logged
		message
		type
		level
*/
// TODO: conver documentation to javadoc style once we confirm that is our choice
// TODO: define DTD for XML-formatted log messages
// TODO: write XML Formatter class
// TODO: write HTTP Handler which uses POST to send log lines/sections

// Filename:	LogCore.js
// Purpose:		a common logging infrastructure for dojo
// Classes:		dojo.logging, dojo.logging.Logger, dojo.logging.Record, dojo.logging.LogFilter
// Global Objects:	dojo.logging
// Dependencies:	none

dojo.provide("dojo.logging.Logger");
dojo.provide("dojo.log");

/*
	A simple data structure class that stores information for and about
	a logged event. Objects of this type are created automatically when
	an event is logged and are the internal format in which information
	about log events is kept.
*/

dojo.logging.Record = function(lvl, msg){
	this.level = lvl;
	this.message = "";
	this.msgArgs = [];
	this.time = new Date();
	
	if(dojo.lang.isArray(msg)){
		if(msg.length > 0 && dojo.lang.isString(msg[0])){
			this.message=msg.shift();
		}
		this.msgArgs=msg;
	}else{
		this.message=msg;
	}
	// FIXME: what other information can we receive/discover here?
}

// an empty parent (abstract) class which concrete filters should inherit from.
dojo.logging.LogFilter = function(loggerChain){
	this.passChain = loggerChain || "";
	this.filter = function(record){
		// FIXME: need to figure out a way to enforce the loggerChain
		// restriction
		return true; // pass all records
	}
}

dojo.logging.Logger = function(){
	this.cutOffLevel = 0;
	this.propagate = true;
	this.parent = null;
	// storage for dojo.logging.Record objects seen and accepted by this logger
	this.data = [];
	this.filters = [];
	this.handlers = [];
}

dojo.extend(dojo.logging.Logger,{
	argsToArr: function(args){
		// utility function, reproduced from __util__ here to remove dependency
		var ret = [];
		for(var x=0; x<args.length; x++){
			ret.push(args[x]);
		}
		return ret;
	},

	setLevel: function(lvl){
		this.cutOffLevel = parseInt(lvl);
	},

	isEnabledFor: function(lvl){
		return parseInt(lvl) >= this.cutOffLevel;
	},

	getEffectiveLevel: function(){
		if((this.cutOffLevel==0)&&(this.parent)){
			return this.parent.getEffectiveLevel();
		}
		return this.cutOffLevel;
	},

	addFilter: function(flt){
		this.filters.push(flt);
		return this.filters.length-1;
	},

	removeFilterByIndex: function(fltIndex){
		if(this.filters[fltIndex]){
			delete this.filters[fltIndex];
			return true;
		}
		return false;
	},

	removeFilter: function(fltRef){
		for(var x=0; x<this.filters.length; x++){
			if(this.filters[x]===fltRef){
				delete this.filters[x];
				return true;
			}
		}
		return false;
	},

	removeAllFilters: function(){
		this.filters = []; // clobber all of them
	},

	filter: function(rec){
		for(var x=0; x<this.filters.length; x++){
			if((this.filters[x]["filter"])&&
			   (!this.filters[x].filter(rec))||
			   (rec.level<this.cutOffLevel)){
				return false;
			}
		}
		return true;
	},

	addHandler: function(hdlr){
		this.handlers.push(hdlr);
		return this.handlers.length-1;
	},

	handle: function(rec){
		if((!this.filter(rec))||(rec.level<this.cutOffLevel)){ return false; }
		for(var x=0; x<this.handlers.length; x++){
			if(this.handlers[x]["handle"]){
			   this.handlers[x].handle(rec);
			}
		}
		// FIXME: not sure what to do about records to be propagated that may have
		// been modified by the handlers or the filters at this logger. Should
		// parents always have pristine copies? or is passing the modified record
		// OK?
		// if((this.propagate)&&(this.parent)){ this.parent.handle(rec); }
		return true;
	},

	// the heart and soul of the logging system
	log: function(lvl, msg){
		if(	(this.propagate)&&(this.parent)&&
			(this.parent.rec.level>=this.cutOffLevel)){
			this.parent.log(lvl, msg);
			return false;
		}
		// FIXME: need to call logging providers here!
		this.handle(new dojo.logging.Record(lvl, msg));
		return true;
	},

	// logger helpers
	debug:function(msg){
		return this.logType("DEBUG", this.argsToArr(arguments));
	},

	info: function(msg){
		return this.logType("INFO", this.argsToArr(arguments));
	},

	warning: function(msg){
		return this.logType("WARNING", this.argsToArr(arguments));
	},

	error: function(msg){
		return this.logType("ERROR", this.argsToArr(arguments));
	},

	critical: function(msg){
		return this.logType("CRITICAL", this.argsToArr(arguments));
	},

	exception: function(msg, e, squelch){
		// FIXME: this needs to be modified to put the exception in the msg
		// if we're on Moz, we can get the following from the exception object:
		//		lineNumber
		//		message
		//		fileName
		//		stack
		//		name
		// on IE, we get:
		//		name
		//		message (from MDA?)
		//		number
		//		description (same as message!)
		if(e){
			var eparts = [e.name, (e.description||e.message)];
			if(e.fileName){
				eparts.push(e.fileName);
				eparts.push("line "+e.lineNumber);
				// eparts.push(e.stack);
			}
			msg += " "+eparts.join(" : ");
		}

		this.logType("ERROR", msg);
		if(!squelch){
			throw e;
		}
	},

	logType: function(type, args){
		return this.log.apply(this, [dojo.logging.log.getLevel(type), 
			args]);
	},
	
	warn:function(){
		this.warning.apply(this,arguments);
	},
	err:function(){
		this.error.apply(this,arguments);
	},
	crit:function(){
		this.critical.apply(this,arguments);
	}
});

// the Handler class
dojo.logging.LogHandler = function(level){
	this.cutOffLevel = (level) ? level : 0;
	this.formatter = null; // FIXME: default formatter?
	this.data = [];
	this.filters = [];
}
dojo.lang.extend(dojo.logging.LogHandler,{
	
	setFormatter:function(formatter){
		dojo.unimplemented("setFormatter");
	},
	
	flush:function(){},
	close:function(){},
	handleError:function(){},
	
	handle:function(record){
		if((this.filter(record))&&(record.level>=this.cutOffLevel)){
			this.emit(record);
		}
	},
	
	emit:function(record){
		dojo.unimplemented("emit");
	}
});

// set aliases since we don't want to inherit from dojo.logging.Logger
void(function(){ // begin globals protection closure
	var names = [
		"setLevel", "addFilter", "removeFilterByIndex", "removeFilter",
		"removeAllFilters", "filter"
	];
	var tgt = dojo.logging.LogHandler.prototype;
	var src = dojo.logging.Logger.prototype;
	for(var x=0; x<names.length; x++){
		tgt[names[x]] = src[names[x]];
	}
})(); // end globals protection closure

dojo.logging.log = new dojo.logging.Logger();

// an associative array of logger objects. This object inherits from
// a list of level names with their associated numeric levels
dojo.logging.log.levels = [ {"name": "DEBUG", "level": 1},
						   {"name": "INFO", "level": 2},
						   {"name": "WARNING", "level": 3},
						   {"name": "ERROR", "level": 4},
						   {"name": "CRITICAL", "level": 5} ];

dojo.logging.log.loggers = {};

dojo.logging.log.getLogger = function(name){
	if(!this.loggers[name]){
		this.loggers[name] = new dojo.logging.Logger();
		this.loggers[name].parent = this;
	}
	return this.loggers[name];
}

dojo.logging.log.getLevelName = function(lvl){
	for(var x=0; x<this.levels.length; x++){
		if(this.levels[x].level == lvl){
			return this.levels[x].name;
		}
	}
	return null;
}

dojo.logging.log.addLevelName = function(name, lvl){
	if(this.getLevelName(name)){
		this.err("could not add log level "+name+" because a level with that name already exists");
		return false;
	}
	this.levels.append({"name": name, "level": parseInt(lvl)});
	return true;
}

dojo.logging.log.getLevel = function(name){
	for(var x=0; x<this.levels.length; x++){
		if(this.levels[x].name.toUpperCase() == name.toUpperCase()){
			return this.levels[x].level;
		}
	}
	return null;
}

// a default handler class, it simply saves all of the handle()'d records in
// memory. Useful for attaching to with dojo.event.connect()
dojo.logging.MemoryLogHandler = function(level, recordsToKeep, postType, postInterval){
	// mixin style inheritance
	dojo.logging.LogHandler.call(this, level);
	// default is unlimited
	this.numRecords = (typeof djConfig['loggingNumRecords'] != 'undefined') ? djConfig['loggingNumRecords'] : ((recordsToKeep) ? recordsToKeep : -1);
	// 0=count, 1=time, -1=don't post TODO: move this to a better location for prefs
	this.postType = (typeof djConfig['loggingPostType'] != 'undefined') ? djConfig['loggingPostType'] : ( postType || -1);
	// milliseconds for time, interger for number of records, -1 for non-posting,
	this.postInterval = (typeof djConfig['loggingPostInterval'] != 'undefined') ? djConfig['loggingPostInterval'] : ( postType || -1);
}

dojo.lang.inherits(dojo.logging.MemoryLogHandler, dojo.logging.LogHandler);
dojo.lang.extend(dojo.logging.MemoryLogHandler,{
	
	emit:function(record){
		if (!djConfig.isDebug) { return; }
		
		var logStr = String(dojo.log.getLevelName(record.level)+": "
					+record.time.toLocaleTimeString())+": "+record.message;
		if(!dj_undef("println", dojo.hostenv)){
			dojo.hostenv.println(logStr);
		}
		
		this.data.push(record);
		if(this.numRecords != -1){
			while(this.data.length>this.numRecords){
				this.data.shift();
			}
		}
	}
});

dojo.logging.logQueueHandler = new dojo.logging.MemoryLogHandler(0,50,0,10000);

dojo.logging.log.addHandler(dojo.logging.logQueueHandler);
dojo.log = dojo.logging.log;

