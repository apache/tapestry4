
var dj_global = this;var dj_currentContext = this;function dj_undef( name,  object){return (typeof (object || dj_currentContext)[name] == "undefined");}
if(dj_undef("djConfig", this)){var djConfig = {};}
if(dj_undef("dojo", this)){var dojo = {};}
dojo.global = function(){return dj_currentContext;}
dojo.locale  = djConfig.locale;dojo.version = {major: 0, minor: 4, patch: 1, flag: "+",revision: Number("$Rev: 6986 $".match(/[0-9]+/)[0]),toString: function(){with(dojo.version){return major + "." + minor + "." + patch + flag + " (" + revision + ")";}}
}
dojo.getObject = function(name, create, obj, returnWrapper){var tobj, tprop;if(typeof name != "string"){return undefined;}
tobj = obj;if(!tobj){ tobj = dojo.global(); }
var parts=name.split("."), i=0, lobj, tmp, tname;do{lobj = tobj;tname = parts[i];tmp = tobj[parts[i]];if((create)&&(!tmp)){tmp = tobj[parts[i]] = {};}
tobj = tmp;i++;}while(i<parts.length && tobj);tprop = tobj;tobj = lobj;return (returnWrapper) ? { obj: tobj, prop: tname } : tprop;}
dojo.exists = function(name, obj){if(typeof obj == "string"){dojo.deprecated("dojo.exists(obj, name)", "use dojo.exists(name, obj, create)", "0.6");var tmp = name;name = obj;obj = tmp;}
return (!!dojo.getObject(name, false, obj));}
dojo.evalProp = function(name, object, create){dojo.deprecated("dojo.evalProp", "just use hash syntax. Sheesh.", "0.6");return object[name] || (create ? (object[name]={}) : undefined);}
dojo.parseObjPath = function( path,  context,  create){dojo.deprecated("dojo.parseObjPath", "use dojo.getObject(path, create, context, true)", "0.6");return dojo.getObject(path, create, context, true);}
dojo.evalObjPath = function(path, create){dojo.deprecated("dojo.evalObjPath", "use dojo.getObject(path, create)", "0.6");return dojo.getObject(path, create);}
dojo.errorToString = function( exception){return (exception["message"]||exception["description"]||exception);}
dojo.raise = function( message,  exception){if(exception){message = message + ": "+dojo.errorToString(exception);}else{message = dojo.errorToString(message);}
try{if(djConfig.isDebug){dojo.hostenv.println("FATAL exception raised: "+message);}}catch(e){}
throw exception || Error(message);}
dojo.debug = function(){};dojo.debugShallow = function(obj){};dojo.profile = {start: function(){},end: function(){},stop: function(){},dump: function(){}};function dj_eval( scriptFragment){return dj_global.eval ? dj_global.eval(scriptFragment) : eval(scriptFragment);}
dojo.unimplemented = function( funcname,  extra){var message = "'" + funcname + "' not implemented";if(extra != null){ message += " " + extra; }
dojo.raise(message);}
dojo.deprecated = function( behaviour,  extra,  removal){var message = "DEPRECATED: " + behaviour;if(extra){ message += " " + extra; }
if(removal){ message += " -- will be removed in version: " + removal; }
dojo.debug(message);}
dojo.render = (function(){function vscaffold(prefs, names){var tmp = {capable: false,support: {builtin: false,plugin: false
},prefixes: prefs
};for(var i=0; i<names.length; i++){tmp[names[i]] = false;}
return tmp;}
return {name: "",ver: dojo.version,os: { win: false, linux: false, osx: false },html: vscaffold(["html"], ["ie", "opera", "khtml", "safari", "moz"]),svg: vscaffold(["svg"], ["corel", "adobe", "batik"]),vml: vscaffold(["vml"], ["ie"]),swf: vscaffold(["Swf", "Flash", "Mm"], ["mm"]),swt: vscaffold(["Swt"], ["ibm"])
};})();dojo.hostenv = (function(){var config = {isDebug: false,allowQueryConfig: false,baseScriptUri: "",baseRelativePath: "",libraryScriptUri: "",iePreventClobber: false,ieClobberMinimal: true,preventBackButtonFix: true,delayMozLoadingFix: false,searchIds: [],parseWidgets: true
};if(typeof djConfig == "undefined"){djConfig = config;}else{for(var option in config){if(typeof djConfig[option] == "undefined"){djConfig[option] = config[option];}}
}
return {name_: '(unset)',version_: '(unset)',getName: function(){return this.name_;},getVersion: function(){return this.version_;},getText: function( uri){dojo.unimplemented('getText', "uri=" + uri);}};})();dojo.hostenv.getBaseScriptUri = function(){if(djConfig.baseScriptUri.length){return djConfig.baseScriptUri;}
var uri = new String(djConfig.libraryScriptUri||djConfig.baseRelativePath);if(!uri){dojo.raise("Nothing returned by getLibraryScriptUri(): " + uri);}
djConfig.baseScriptUri = djConfig.baseRelativePath;return djConfig.baseScriptUri;}
