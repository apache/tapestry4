dojo.provide("tapestry");
dojo.provide("tapestry.core");

dojo.require("dojo.logging.Logger");
dojo.require("dojo.io");
dojo.require("dojo.event");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.Dialog");
dojo.require("dojo.html");

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
		dojo.require("tapestry.html");
		
		var resp=data.getElementsByTagName("ajax-response");
		if (!resp || resp.length < 1 || !resp[0].childNodes) { dojo.log.warn("No ajax-response elements recieved."); return; }
		
		var elms=resp[0].childNodes;
		for (var i=0; i<elms.length; i++) {
			var type=elms[i].getAttribute("type");
			
			if (type == "exception") {
				tapestry.html.presentException(elms[i], kwArgs);
				return;
			}
		}
	}
}
