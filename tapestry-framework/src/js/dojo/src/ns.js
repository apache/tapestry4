

dojo.provide("dojo.ns");

dojo.ns = {

namespaces: {},
failed: {},
loading: {},
loaded: {},
register: function(name, module, resolver, noOverride){
// summary: creates and registers a dojo.ns.Ns object
if(!noOverride || !this.namespaces[name]){
this.namespaces[name] = new dojo.ns.Ns(name, module, resolver);
}
},
allow: function(name){
// summary: Returns false if 'name' is filtered by configuration or has failed to load, true otherwise
if(this.failed[name]){return false;} // Boolean
if((djConfig.excludeNamespace)&&(dojo.lang.inArray(djConfig.excludeNamespace, name))){return false;} // Boolean
// If the namespace is "dojo", or the user has not specified allowed namespaces return true.
// Otherwise, if the user has specifically allowed this namespace, return true, otherwise false.
return((name==this.dojo)||(!djConfig.includeNamespace)||(dojo.lang.inArray(djConfig.includeNamespace, name))); // Boolean
},
get: function(name){
// summary
//  Return Ns object registered to 'name', if any
return this.namespaces[name]; // Ns
},
require: function(name){
// summary
//  Try to ensure that 'name' is registered, loading a namespace manifest if necessary
var ns = this.namespaces[name];
if((ns)&&(this.loaded[name])){return ns;} // Ns
if(!this.allow(name)){return false;} // Boolean
if(this.loading[name]){
// FIXME: do we really ever have re-entrancy situation? this would appear to be really bad
// original code did not throw an exception, although that seems the only course
// adding debug output here to track if this occurs.
dojo.debug('dojo.namespace.require: re-entrant request to load namespace "' + name + '" must fail.');
return false; // Boolean
}
// workaround so we don't break the build system
var req = dojo.require;
this.loading[name] = true;
try {
//dojo namespace file is always in the Dojo namespaces folder, not any custom folder
if(name=="dojo"){
req("dojo.namespaces.dojo");
}else{
// if no registered module prefix, use ../<name> by convention
if(!dojo.hostenv.moduleHasPrefix(name)){
dojo.registerModulePath(name, "../" + name);
}
req([name, 'manifest'].join('.'), false, true);
}
if(!this.namespaces[name]){
this.failed[name] = true; //only look for a namespace once
}
}finally{
this.loading[name]=false;
}
return this.namespaces[name]; // Ns
}
}

dojo.ns.Ns = function(name, module, resolver){

this.name = name;
this.module = module;
this.resolver = resolver;
this._loaded = [ ];
this._failed = [ ];
}

dojo.ns.Ns.prototype.resolve = function(name, domain, omitModuleCheck){

if(!this.resolver || djConfig["skipAutoRequire"]){return false;} // Boolean
var fullName = this.resolver(name, domain);

if((fullName)&&(!this._loaded[fullName])&&(!this._failed[fullName])){
//workaround so we don't break the build system
var req = dojo.require;
req(fullName, false, true); //omit the module check, we'll do it ourselves.
if(dojo.hostenv.findModule(fullName, false)){
this._loaded[fullName] = true;
}else{
if(!omitModuleCheck){dojo.raise("dojo.ns.Ns.resolve: module '" + fullName + "' not found after loading via namespace '" + this.name + "'");}
this._failed[fullName] = true;
}
}
return Boolean(this._loaded[fullName]); // Boolean
}

dojo.registerNamespace = function(name, module, resolver){





dojo.ns.register.apply(dojo.ns, arguments);
}

dojo.registerNamespaceResolver = function(name, resolver){
















var n = dojo.ns.namespaces[name];
if(n){
n.resolver = resolver;
}
}

dojo.registerNamespaceManifest = function(module, path, name, widgetModule, resolver){

dojo.registerModulePath(name, path);
dojo.registerNamespace(name, widgetModule, resolver);
}


dojo.registerNamespace("dojo", "dojo.widget");
