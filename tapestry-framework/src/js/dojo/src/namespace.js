/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.namespace");

dojo["namespace"] = {
	dojo: "dojo",
	namespaces: {},
	failed: {},
	loading: {},
	loaded: {},
	register: function(name, module, resolver /*optional*/, noOverride) {
		if((!noOverride)&&(!this.namespaces[name])){
			this.namespaces[name] = new dojo["namespace"].Namespace(name, module, resolver);
		}
	},
	allow: function(name) {
		// summary: Return false if 'name' is filtered by configuration or has failed to load, true otherwise
		if(this.failed[name]){return false;} // if this namespace is known unloadable
		// if the user has specified that this namespace be disallowed, return false.
		var excl = djConfig.excludeNamespace;
		if((excl)&&(dojo.lang.inArray(excl, name))){return false;}
		// If the namespace is "dojo", or the user has not specified allowed namespaces return true.
		// Otherwise, if the user has specifically allowed this namespace, return true, otherwise false.
		var incl = djConfig.includeNamespace;
		return((name==this.dojo)||(!incl)||(dojo.lang.inArray(incl, name)));
	},
	get: function(name){
		// summary
		//  Return Namespace object registered to 'name', if any
		return this.namespaces[name];
	},
	require: function(name){
		// summary
  	//  Try to ensure that 'name' is registered by loading a namespace manifest
		var ns = this.namespaces[name];
		if((ns)&&(this.loaded[name])){return ns;}
		if(!this.allow(name)){return false;}
 		if(this.loading[name]){
			// FIXME: do we really ever have re-entrancy situation? this would appear to be really bad
			// original code did not throw an exception, although that seems the only course
			// adding debug output here to track if this occurs.
			dojo.debug('dojo.namespace.require: re-entrant request to load namespace "' + name + '" must fail.'); 
			return false;
		}
		// workaround so we don't break the build system
		var req = dojo.require;
		this.loading[name] = true;
		try {
			//dojo namespace file is always in the Dojo namespace folder, not a custom namespace folder
			if(name==this.dojo){
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
		return this.namespaces[name];
	}
}

dojo.registerNamespace = function(name, module, resolver /*optional*/){
	dojo["namespace"].register.apply(dojo["namespace"], arguments);
}

dojo.registerNamespaceResolver = function(name, resolver){
	var n = dojo["namespace"].namespaces[name];
	if(n){
		n.resolver = resolver;
	}
}

dojo.registerNamespaceManifest = function(module, path, name, widgetModule, resolver /*optional*/){
	dojo.registerModulePath(name, path);
	dojo.registerNamespace(name, widgetModule, resolver);
}

dojo.defineNamespace = function(objRoot, location, nsPrefix, resolver /*optional*/, widgetPackage /*optional*/){
	dojo.deprecated("dojo.defineNamespace", " is replaced by other systems. See the Dojo Wiki [http://dojo.jot.com/WikiHome/Modules & Namespaces].", "0.5");
	dojo.registerNamespaceManifest(objRoot, location, nsPrefix, widgetPackage, resolver);
}

// namespace bookkeeping object

dojo["namespace"].Namespace = function(name, module, resolver){
	this.name = name;
	this.module = module;
	this.resolver = resolver;
}

dojo["namespace"].Namespace.prototype._loaded = {};
dojo["namespace"].Namespace.prototype._failed = {};

// map component with 'name' and 'domain' to a module via 
// namespace resolver, if specified
dojo["namespace"].Namespace.prototype.resolve = function(name, domain, omit_module_check){
	if(!this.resolver){return false;}
	var fullName = this.resolver(name,domain);
	//only load a widget once. This is a quicker check than dojo.require does
	if((fullName)&&(!this._loaded[fullName])&&(!this._failed[fullName])){
		//workaround so we don't break the build system
		var req = dojo.require;
		req(fullName, false, true); //omit the module check, we'll do it ourselves.
		if(dojo.hostenv.findModule(fullName, false)){
			this._loaded[fullName] = true;
		}else{
			if(!omit_module_check){dojo.raise("dojo.namespace.Namespace.resolve: module '" + fullName + "' not found after loading via namespace '" + this.name + "'");} 
			this._failed[fullName] = true;
		}
	}
	return Boolean(this._loaded[fullName]);
}

// NOTE: rather put this in dojo.widget.Widget, but that fubars debugAtAllCosts
dojo.registerNamespace("dojo", "dojo.widget");