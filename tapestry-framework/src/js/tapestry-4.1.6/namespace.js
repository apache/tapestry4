dojo.provide("tapestry.namespace");

dojo.require("dojo.ns");

(function(){
	
	var map = {
		html: {
			"alertdialog": "tapestry.widget.AlertDialog",
			"timepicker": "tapestry.widget.TimePicker"
		}
	};
	
	function resolveNamespace(name, domain){
		if(!domain){ domain="html"; }
		if(!map[domain]){ return null; }
		return map[domain][name];
	}
	
	var tpath;
	if (dojo.hostenv.moduleHasPrefix("tapestry")){
		tpath=dojo.hostenv.getModulePrefix("tapestry");
	} else {
		tpath="../tapestry";
	}
	dojo.registerNamespaceManifest("tapestry", tpath, "tapestry", "tapestry.widget");
	dojo.registerNamespaceResolver("tapestry", resolveNamespace);
})();
