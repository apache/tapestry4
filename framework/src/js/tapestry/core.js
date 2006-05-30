dojo.provide("tapestry");
dojo.provide("tapestry.core");

dojo.require("dojo.logging.Logger");
dojo.require("dojo.io");
dojo.require("dojo.event");

dojo.setModulePrefix("tapestry", "../tapestry");

tapestry={
	
	version:"4.1",
	
	/**
	 * Global XHR bind function for tapestry internals. The 
	 * error/load functions of this same package are used to handle
	 * load/error of dojo.io.bind.
	 * 
	 * @param url The url to bind the request to.
	 * @param content A properties map of optional extra content to send.
	 */
	bind:function(url, content){
		dojo.io.bind({
			url:url,
            content:content,
            headers:{"dojo-ajax-request":true},
            useCache:true,
            preventCache:true,
            load: (function(){tapestry.load.apply(this, arguments);}),
            error: (function(){tapestry.error.apply(this, arguments);}),
            mimetype: "text/xml",
            encoding: "UTF-8"
        });
	},
	
	/**
	 * Global error handling function for dojo.io.bind requests.
	 */
	error:function(type, exception, http, kwArgs){
		dojo.log.exception("Error received in IO response.", exception);
	},
	
	/**
	 * Global load handling function for dojo.io.bind requests.
	 */
	load:function(type, data, http, kwArgs){
		dojo.log.debug("Response recieved.");
	}
}
