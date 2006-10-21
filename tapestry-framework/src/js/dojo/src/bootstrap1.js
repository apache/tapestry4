






































var dj_global = this;










var dj_currentContext = this;







function dj_undef( name,  object){


return (typeof (object || dj_currentContext)[name] == "undefined");	// Boolean
}


if(dj_undef("djConfig", this)){
var djConfig = {};
}



if(dj_undef("dojo", this)){
var dojo = {};
}

dojo.global = function(){






return dj_currentContext;
}


dojo.locale  = djConfig.locale;


dojo.version = {

major: 0, minor: 3, patch: 1, flag: "+",
revision: Number("$Rev: 6258 $".match(/[0-9]+/)[0]),
toString: function(){
with(dojo.version){
return major + "." + minor + "." + patch + flag + " (" + revision + ")";	// String
}
}
}

dojo.evalProp = function( name,  object,  create){




if((!object)||(!name)) return undefined; // undefined
if(!dj_undef(name, object)) return object[name]; // mixed
return (create ? (object[name]={}) : undefined);	// mixed
}

dojo.parseObjPath = function( path,  context,  create){







var object = (context || dojo.global());
var names = path.split('.');
var prop = names.pop();
for (var i=0,l=names.length;i<l && object;i++){
object = dojo.evalProp(names[i], object, create);
}
return {obj: object, prop: prop};	// Object: {obj: Object, prop: String}
}

dojo.evalObjPath = function( path,  create){



if(typeof path != "string"){
return dojo.global();
}

if(path.indexOf('.') == -1){
return dojo.evalProp(path, dojo.global(), create);		// mixed
}


var ref = dojo.parseObjPath(path, dojo.global(), create);
if(ref){
return dojo.evalProp(ref.prop, ref.obj, create);	// mixed
}
return null;
}

dojo.errorToString = function( exception){



// 		... since natively generated Error objects do not always reflect such things?
if(!dj_undef("message", exception)){
return exception.message;		// String
}else if(!dj_undef("description", exception)){
return exception.description;	// String
}else{
return exception;				// Error
}
}

dojo.raise = function( message,  exception){




if(exception){
message = message + ": "+dojo.errorToString(exception);
}


try { if(djConfig.isDebug){ dojo.hostenv.println("FATAL exception raised: "+message); } } catch (e) {}

throw exception || Error(message);
}



dojo.debug = function(){};
dojo.debugShallow = function(obj){};
dojo.profile = { start: function(){}, end: function(){}, stop: function(){}, dump: function(){} };

function dj_eval( scriptFragment){






return dj_global.eval ? dj_global.eval(scriptFragment) : eval(scriptFragment); 	// mixed
}

dojo.unimplemented = function( funcname,  extra){


var message = "'" + funcname + "' not implemented";
if (extra != null) { message += " " + extra; }
dojo.raise(message);
}

dojo.deprecated = function( behaviour,  extra,  removal){



var message = "DEPRECATED: " + behaviour;
if(extra){ message += " " + extra; }
if(removal){ message += " -- will be removed in version: " + removal; }
dojo.debug(message);
}

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








dojo.hostenv = (function(){







var config = {
isDebug: false,
allowQueryConfig: false,
baseScriptUri: "",
baseRelativePath: "",
libraryScriptUri: "",
iePreventClobber: false,
ieClobberMinimal: true,
preventBackButtonFix: true,
delayMozLoadingFix: false,
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

getText: function( uri){
// summary:	Read the plain/text contents at the specified 'uri'.
// description:
//			If 'getText()' is not implemented, then it is necessary to override
//			'loadUri()' with an implementation that doesn't rely on it.

dojo.unimplemented('getText', "uri=" + uri);
}
};
})();


dojo.hostenv.getBaseScriptUri = function(){




if(djConfig.baseScriptUri.length){
return djConfig.baseScriptUri;
}




var uri = new String(djConfig.libraryScriptUri||djConfig.baseRelativePath);
if (!uri) { dojo.raise("Nothing returned by getLibraryScriptUri(): " + uri); }


var lastslash = uri.lastIndexOf('/');		// MOW ???
djConfig.baseScriptUri = djConfig.baseRelativePath;
return djConfig.baseScriptUri;	// String
}
