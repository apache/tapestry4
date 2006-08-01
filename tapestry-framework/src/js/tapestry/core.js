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
	
	version:"4.1", // tapestry script version
	scriptInFlight:false, // whether or not javascript is currently being eval'd
	ScriptFragment:'(?:<script.*?>)((\n|.|\r)*?)(?:<\/script>)', // regexp for script elements
	
	/**
	 * Global XHR bind function for tapestry internals. The 
	 * error/load functions defined in this package are used to handle
	 * load/error of dojo.io.bind.
	 * 
	 * @param url 
	 * 			The url to bind the request to.
	 * @param content 
	 * 			A properties map of optional extra content to send.
	 * @param json 
	 * 			Boolean, optional parameter specifying whether or not to create a json request.
	 * 			If not specified the default is to use XHR.
	 */
	bind:function(url, content, json){
		var parms = {
			url:url,
			content:content,
            useCache:true,
            preventCache:true,
            load: (function(){tapestry.load.apply(this, arguments);}),
            error: (function(){tapestry.error.apply(this, arguments);})
		};
		
		// setup content type
		if (typeof json != "undefined" && json) {
			parms.mimetype = "text/json";
		} else {
			parms.headers={"dojo-ajax-request":true};
			parms.mimetype="text/xml";
			parms.encoding="UTF-8";
		}
		
		dojo.io.bind(parms);
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
		if (!data) {
			dojo.log.err("No data received in response.");
			return;
		}
		
		var resp=data.getElementsByTagName("ajax-response");
		if (!resp || resp.length < 1 || !resp[0].childNodes) {
			dojo.log.warn("No ajax-response elements recieved.");
			return; 
		}
		
		var elms=resp[0].childNodes;
		var bodyScripts=[];
		var initScripts=[];
		for (var i=0; i<elms.length; i++) {
			var type=elms[i].getAttribute("type");
			var id=elms[i].getAttribute("id");
			
			if (type == "exception") {
				dojo.log.err("Remote server exception received.");
				tapestry.presentException(elms[i], kwArgs);
				return;
			}
			
			// handle javascript evaluations
			if (type == "script") {
				
				if (id == "initializationscript") {
					initScripts.push(elms[i]);
					continue;
				} else if (id == "bodyscript") {
					bodyScripts.push(elms[i]);
					continue;
				} else if (id == "includescript") {
					// includes get processed immediately (syncrhonously)
					tapestry.loadScriptContent(elms[i], false);
				}
				
			}
			
			if (!id){
				dojo.raise("No element id found in ajax-response node.");
				return;
			}
			
			var node=dojo.byId(id);
			if (!node) {
				dojo.log.err("No node could be found to update content in with id " + id);
				return;
			}
			
			tapestry.loadContent(id, node, elms[i]);
		}
		
		// load body scripts before initialization
		for (var i=0; i<bodyScripts.length; i++) {
			tapestry.loadScriptContent(bodyScripts[i], true);
		}
		for (var i=0; i<initScripts.length; i++) {
			tapestry.loadScriptContent(initScripts[i], true);
		}
	},
	
	loadContent:function(id, node, element){
    	if (element.childNodes && element.childNodes.length > 0) {
        	for (var i = 0; i < element.childNodes.length; i++) {
            	if (element.childNodes[i].nodeType != 1) { continue; }
				
            	var nodeId = element.childNodes[i].getAttribute("id");
            	if (nodeId && nodeId == id) {
                	element=element.childNodes[i];
                	break;
            	}
        	}
    	}
    	
    	if (djConfig["isDebug"]) {
    		var content=tapestry.html.getContentAsString(element);
    		dojo.log.debug("Received element content for id <" + id + "> of:\n" + content);
    		node.innerHTML=content;
    		return;
    	}
    	
    	node.innerHTML=tapestry.html.getContentAsString(element);
	},
	
	loadScriptContent:function(element, async){
		if (typeof async == "undefined") { async = true; }
		
		var text=tapestry.html.getContentAsString(element);
		
		if (tapestry.scriptInFlight) {
			dojo.log.debug("loadScriptContent(): scriptInFlight is true, sleeping");
			setTimeout(function() { tapestry.loadScriptContent(text, async);}, 5);
			return;
		}
		
		var match = new RegExp(tapestry.ScriptFragment, 'img');
	    var response = text.replace(match, '');
	    var scripts = text.match(match);
		
		if (!scripts) { return; }
		
        match = new RegExp(tapestry.ScriptFragment, 'im');
        if (async) {
        	setTimeout(function() { 
        		tapestry.evaluateScripts(scripts, match); 
        	}, 60);
        } else {
        	tapestry.evaluateScripts(scripts, match);
        }
	},
	
	evaluateScripts:function(scripts, match){
		tapestry.scriptInFlight = true;
       	
        for (var i=0; i<scripts.length; i++) {
            var scr = scripts[i].match(match)[1];
            try {
                dojo.log.debug("evaluating script:" + scr);
                eval(scr);
            } catch (e) {
            	tapestry.scriptInFlight = false;
                dojo.log.exception("Error evaluating script: " + scr, e, false);
            }
        }
        
        tapestry.scriptInFlight = false;
	},
	
	loadScriptFromUrl:function(url){
	    var scripts = window.document.getElementsByTagName("script");
	    for (var i = 0; i < scripts.length; i++) {
	        var src = scripts[i].src;
	        if (src && src.length > 0 && src.indexOf(url)>=0 ) {
	            return;
	        }
	    }
		
	    var e = document.createElement("script");
	    e.src = url;
	    e.type = "text/javascript";
	    document.getElementsByTagName("head")[0].appendChild(e);
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
