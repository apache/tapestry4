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
		
		var resp=data.getElementsByTagName("ajax-response");
		if (!resp || resp.length < 1 || !resp[0].childNodes) { dojo.log.warn("No ajax-response elements recieved."); return; }
		
		var elms=resp[0].childNodes;
		for (var i=0; i<elms.length; i++) {
			var type=elms[i].getAttribute("type");
			
			if (type == "exception") {
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
		contentnode.innerHTML=this.getContentAsString(node);
		dojo.html.setClass(contentnode, "exceptionDialog");
		
		var navnode=document.createElement("div");
		navnode.setAttribute("id", "exceptionDialogHandle");
		dojo.html.setClass(navnode, "exceptionCloseLink");
		navnode.appendChild(document.createTextNode("Close"));
		
		excnode.appendChild(navnode);
		excnode.appendChild(contentnode);
		
		var dialog=dojo.widget.createWidget("Dialog", {widgetId:"exception"}, excnode);
		
		dojo.event.connect(navnode, "onclick", dialog, "hide");
		dojo.event.connect(dialog, "hide", function(e) {
			dojo.widget.byId("exception").destroy();
			var node=dojo.byId("exceptiondialog");
			if (node) dojo.dom.removeNode(node);
		});
		
		dialog.show();
	},
	
	getContentAsString:function(node){
		if (typeof node.xml != "undefined")
			return this.getContentAsStringIE(node);
		else if (typeof XMLSerializer != "undefined" )
			return this.getContentAsStringMozilla(node);
		else
			return this.getContentAsStringGeneric(node);
	},
	
	getContentAsStringIE:function(node){
		var s="";
    	for (var i = 0; i < node.childNodes.length; i++)
        	s += node.childNodes[i].xml;
    	return s;
	},
	
	getContentAsStringMozilla:function(node){
		var xmlSerializer = new XMLSerializer();
	    var s = "";
	    for (var i = 0; i < node.childNodes.length; i++) {
	        s += xmlSerializer.serializeToString(node.childNodes[i]);
	        if (s == "undefined")
		        return this.getContentAsStringGeneric(node);
	    }
	    return s;
	},
	
	getContentAsStringGeneric:function(node){
		var s="";
		if (node == null) { return s; }
		for (var i = 0; i < node.childNodes.length; i++) {
			switch (node.childNodes[i].nodeType) {
				case 1: // ELEMENT_NODE
				case 5: // ENTITY_REFERENCE_NODE
					s += tacos.getElementAsStringGeneric(node.childNodes[i]);
					break;
				case 3: // TEXT_NODE
				case 2: // ATTRIBUTE_NODE
				case 4: // CDATA_SECTION_NODE
					s += node.childNodes[i].nodeValue;
					break;
				default:
					break;
			}
		}
		return s;	
	}
}
