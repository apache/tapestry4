dojo.provide("tapestry");
dojo.provide("tapestry.core");
dojo.require("dojo.logging.Logger");

dojo.setModulePrefix("tapestry", "../tapestry");

tapestry={
	version:"4.1",
	
	error:function(type, exception, http, kwArgs){
		dojo.log.exception("Error received in IO response.", exception);
	},
	
	load:function(type, data, http, kwArgs){
		dojo.log.debug("Response recieved.");
	}
}