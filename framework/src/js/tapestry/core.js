dojo.provide("tapestry");
dojo.provide("tapestry.core");
dojo.setModulePrefix("tapestry", "../tapestry");

dojo.require("dojo.logging.Logger");
dojo.require("dojo.io");
dojo.require("dojo.event");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.Dialog");
dojo.require("dojo.html");

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
		
		var resp=data.getElementsByTagName("ajax-response");
		if (!resp || resp.length < 1 || !resp[0].childNodes) { dojo.log.warn("No ajax-response elements recieved."); return; }
		
		var elms=resp[0].childNodes;
		for (var i=0; i<elms.length; i++) {
			var type=elms[i].getAttribute("type");
			
			if (type == "exception") {
				dojo.log.err("Remote server exception received.");
				tapestry.presentException(elms[i], kwArgs);
				return;
			}
		}
	},
	
	presentException:function(node, kwArgs) {
		var excnode=document.createElement("div");
		excnode.setAttribute("id", "exceptiondialog");
		document.body.appendChild(excnode);
		
		var contentnode=document.createElement("div");
		contentnode.innerHTML=tapestry.html.getContentAsString(node);
		dojo.html.setClass(contentnode, "exceptionDialog");
		
		var navnode=document.createElement("div");
		navnode.setAttribute("id", "exceptionDialogHandle");
		dojo.html.setClass(navnode, "exceptionCloseLink");
		navnode.appendChild(document.createTextNode("Close"));
		
		excnode.appendChild(navnode);
		excnode.appendChild(contentnode);
		
		var dialog=dojo.widget.createWidget("Dialog", {widgetId:"exception"}, excnode);
		dojo.event.connect(navnode, "onclick", dialog, "hide");
		dojo.event.connect(dialog, "hide", dialog, "destroy");
		
		dialog.show();
	}
}

dojo.require("tapestry.html");