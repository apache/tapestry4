dojo.provide("tapestry.core");

dojo.require("dojo.logging.Logger");
dojo.require("dojo.io.*");
dojo.require("dojo.event.browser");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.Dialog");
dojo.require("dojo.html.style");

/**
 * package: tapestry
 * Provides the core functionality for the Tapestry javascript package libraries. 
 * 
 * Most of the functions in here are related to initiating and parsing IO 
 * requests. 
 */
tapestry={
	
	// property: version 
	// The current client side library version, usually matching the current java library version. (ie 4.1, etc..)
	version:"4.1",
	scriptInFlight:false, // whether or not javascript is currently being eval'd, default false
	ScriptFragment:'(?:<script.*?>)((\n|.|\r)*?)(?:<\/script>)', // regexp for script elements
	
	/**
	 * Function: bind
	 * 
	 * Core XHR bind function for tapestry internals. The 
	 * <error>/<load> functions defined in this package are used to handle
	 * load/error of dojo.io.bind.
	 * 
	 * Parameters: 
	 * 
	 * 	url - The url to bind the request to.
	 * 	content - A properties map of optional extra content to send.
	 *  json - Boolean, optional parameter specifying whether or not to create a 
	 * 		   json request. If not specified the default is to use XHR.
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
		if (typeof json != "undefined" && json == true) {
			parms.mimetype = "text/json";
			parms.headers={"json":true};
		} else {
			parms.headers={"dojo-ajax-request":true};
			parms.mimetype="text/xml";
			parms.encoding="UTF-8";
		}
		
		dojo.io.queueBind(parms);
	},
	
	/**
	 * Function: error
	 * 
	 * Global error handling function for dojo.io.bind requests. This function is mapped 
	 * as the "error:functionName" part of a request in the dojo.io.bind arguments 
	 * in <tapestry.bind> calls.
	 * 
	 * See Also:
	 * 	<tapestry.bind>
	 */
	error:function(type, exception, http, kwArgs){
		dojo.log.exception("Error received in IO response.", exception);
	},
	
	/**
	 * Function: load
	 * 
	 * Global load handling function for dojo.io.bind requests. This isn't typically
	 * called directly by anything, but passed in as the "load" argument to 
	 * dojo.io.bind when making IO requests as the function that will handle the 
	 * return response.
	 * 
	 * Parameters:
	 * 	type - Type of request.
	 * 	data - The data returned, depending on the request type might be an xml document / 
	 * 			plaintext / json / etc.
	 * 	http - The http object used in request, like XmlHttpRequest.
	 * 	kwArgs - The original set of arguments passed into dojo.io.bind({arg:val,arg1:val2}).
	 * 
	 */
	load:function(type, data, http, kwArgs){
		dojo.log.debug("Response recieved.");
		if (!data) {
			dojo.log.warn("No data received in response.");
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
					continue;
				}
				
			}
			
			if (!id){
				dojo.warn("No element id found in ajax-response node.");
				continue;
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
	
	/**
	 * Function: loadContent
	 * 
	 * Used by <tapestry.load> when handling xml responses to iterate over the tapestry 
	 * specific xml response and appropriately load all content types / perform animations / 
	 * execute scripts in the proper order / etc..
	 * 
	 * Parameters: 
	 * 	id - The element id that this content should be applied to in the existing document.
	 * 	node - The node that this new content will be applied to. 
	 * 	element - The incoming xml node containing rules/content to apply to this node.
	 * 
	 */
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
    	
    	dojo.event.browser.clean(node); // prevent mem leaks in ie
    	
    	if (djConfig["isDebug"]) {
    		var content=tapestry.html.getContentAsString(element);
    		dojo.log.debug("Received element content for id <" + id + "> of:", content);
    		node.innerHTML=content;
    		return;
    	}
    	
    	node.innerHTML=tapestry.html.getContentAsString(element);
	},
	
	/**
	 * Function: loadScriptContent
	 * 
	 * Manages loading javascript content for a specific incoming xml element.
	 * 
	 * Parameters:
	 * 	element - The element to parse javascript statements from and execute.
	 * 	async - Whether or not to process the script content asynchronously, meaning
	 * 			whether or not to execute the script in a block done in a setTimeout call
	 * 			so as to avoid IE specific issues.
	 */
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
            if(!scr || scr.length <= 0){continue;}
            try {
                dojo.log.debug("evaluating script:", scr);
                eval(scr);
            } catch (e) {
            	tapestry.scriptInFlight = false;
                dojo.log.exception("Error evaluating script: " + scr, e, false);
            }
        }
        
        tapestry.scriptInFlight = false;
	},
	
	/**
	 * Function: loadScriptFromUrl
	 * 
	 * Takes a url string and loads the javascript it points to as a normal 
	 * document head script include section. ie:
	 * 
	 * : <script type="text/javascript" src="http://localhost/js/foo.js"></script>
	 * 
	 * Parameters:
	 * 	url - The url to the script to load into this documents head.
	 */
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
	
	/**
	 * Function: presentException
	 * 
	 * When remote exceptions are caught on the server special xml blocks are returned to 
	 * the client when the requests are initiated via async IO. This function takes the incoming
	 * Tapestry exception page content and dumps it into a modal dialog that is presented to the user.
	 * 
	 * Parameters: 
	 * 	node - The incoming xml exception node.
	 * 	kwArgs - The kwArfs used to initiate the original IO request.
	 */
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
	},
	
	/**
	 * Function: cleanConnect
	 * 
	 * Utility used to disconnect a previously connected event/function.
	 * 
	 * This assumes that the incoming function name is being attached to 
	 * the global namespace "tapestry".
	 */
	cleanConnect:function(target, event, funcName){
		if (!dj_undef(funcName, tapestry)){
        	dojo.event.disconnect(target, event, tapestry, funcName);
        }
	}
}

dojo.require("tapestry.html");
