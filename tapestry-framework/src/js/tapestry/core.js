dojo.provide("tapestry.core");
dojo.provide("tapestry.html");
dojo.provide("tapestry.event");
dojo.provide("tapestry.lang");

dojo.require("dojo.lang.common");
dojo.require("dojo.logging.Logger");
dojo.require("dojo.io.BrowserIO");
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
	version:"4.1.2",
	scriptInFlight:false, // whether or not javascript is currently being eval'd, default false
	ScriptFragment:'(?:<script.*?>)((\n|.|\r)*?)(?:<\/script>)', // regexp for script elements
    requestsInFlight:0, // how many ajax requests are currently in progress

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
            error: (function(){tapestry.error.apply(this, arguments);})
		};
		
		// setup content type
		if (typeof json != "undefined" && json == true) {
			parms.mimetype = "text/json";
			parms.headers={"json":true};
			parms.load=(function(){tapestry.loadJson.apply(this, arguments);});
		} else {
			parms.headers={"dojo-ajax-request":true};
			parms.mimetype="text/xml";
			parms.encoding="UTF-8";
			parms.load=(function(){tapestry.load.apply(this, arguments);});
		}

        tapestry.requestsInFlight++;
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
        tapestry.requestsInFlight--;
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
		dojo.log.debug("tapestry.load() Response received.", data);
        tapestry.requestsInFlight--;
        if (!data) {
			dojo.log.warn("No data received in response.");
			return;
		}
		
		var resp=data.getElementsByTagName("ajax-response");
		if (!resp || resp.length < 1 || !resp[0].childNodes) {
			dojo.log.warn("No ajax-response elements received.");
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
			
			if (type == "page") {
				window.location=elms[i].getAttribute("url");
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
					// includes get processed immediately (synchronously)
					var includes=elms[i].getElementsByTagName("include");
					if (!includes){continue;}
					for (var e=0; e<includes.length;e++) {
						tapestry.loadScriptFromUrl(includes[e].getAttribute("url"));
					}
					continue;
				}
			}
			
			if (!id){
				dojo.warn("No element id found in ajax-response node.");
				continue;
			}
			
			var node=dojo.byId(id);
			if (!node) {
				dojo.log.warn("No node could be found to update content in with id " + id);
				continue;
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
	
	loadJson:function(type, data, http, kwArgs){
        dojo.log.debug("tapestry.loadJson() Response received.", data);
        tapestry.requestsInFlight--;
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
            	if (nodeId && nodeId) {
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
    	} else {
    		node.innerHTML=tapestry.html.getContentAsString(element);
    	}
    	
    	// copy style/class css attributes
    	var style=element.getAttribute("style");
    	if(style) {
    		dojo.html.setStyleText(node, style);
    	}
    	var classStr=element.getAttribute("class");
    	if (classStr) {
    		dojo.html.setClass(node, classStr);
    	}
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
		
		if (tapestry.scriptInFlight) {
			dojo.log.debug("loadScriptContent(): scriptInFlight is true, sleeping");
			setTimeout(function() { tapestry.loadScriptContent(element, async);}, 5);
			return;
		}
        
		var text=tapestry.html.getContentAsString(element);
		
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
	    if (scripts){
		    for (var i = 0; i < scripts.length; i++) {
		        var src = scripts[i].src;
		        if (src && src.length > 0 && src.indexOf(url)>=0 ) {
		            return;
		        }
		    }
	    }
	    
	    if (djConfig.isDebug) {
	    	dojo.log.debug("loadScriptFromUrl: " + url + " success?: " + dojo.hostenv.loadUri(url));
	    } else {
	    	dojo.hostenv.loadUri(url);
	    }
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
	 * 	kwArgs - The kwArgs used to initiate the original IO request.
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
	},
	
	linkOnClick:function(url, id, isJson){
		var content={beventname:"onClick"};
		content["beventtarget.id"]=id;
		
		tapestry.bind(url, content, isJson);
		return false;
	},

    /**
	 * Function: isServingRequests
	 *
	 * Utility used to find out if there are any ajax requests in progress.
	 */
    isServingRequests:function(){
	    return (tapestry.requestsInFlight > 0);
    }
}

/**
 * package: tapestry.html
 * Provides functionality related to parsing and rendering dom nodes.
 */
tapestry.html={
    
    TextareaMatcher:'<textarea(.*?)/>', // regexp for compact textarea elements
    TextareaReplacer:'<textarea$1></textarea>', // replace pattern for compact textareas
	
    /**
	 * Function: getContentAsString
	 * 
	 * Takes a dom node and returns its contents rendered in a string.
     *
     * The resulting string does NOT contain any markup (or attributes) of
     * the given node - only child nodes are rendered and returned.Content
     *
     * Implementation Note: This function tries to make use of browser 
     * specific features (the xml attribute of nodes in IE and the XMLSerializer
     * object in Mozilla derivatives) - if those fails, a generic implementation
     * is used that is guaranteed to work in all platforms.
	 * 
	 * Parameters: 
	 * 
	 *	node - The dom node.
	 * Returns:
	 * 
	 * The string representation of the given node's contents.
	 */    
	getContentAsString:function(node){
		if (typeof node.xml != "undefined")
			return this._getContentAsStringIE(node);
		else if (typeof XMLSerializer != "undefined" )
			return this._getContentAsStringMozilla(node);
		else
			return this._getContentAsStringGeneric(node);
	},        
	
   /**
	 * Function: getElementAsString
	 * 
	 * Takes a dom node and returns itself and its contents rendered in a string.
     *
     * Implementation Note: This function uses a generic implementation in order
     * to generate the returned string.
	 * 
	 * Parameters: 
	 * 
	 *	node - The dom node.
	 * Returns:
	 * 
	 * The string representation of the given node.
	 */         
	getElementAsString:function(node){
		if (!node) { return ""; }
		
		var s='<' + node.nodeName;
		// add attributes
		if (node.attributes && node.attributes.length > 0) {
			for (var i=0; i < node.attributes.length; i++) {
				s += " " + node.attributes[i].name + "=\"" + node.attributes[i].value + "\"";	
			}
		}
		// close start tag
		s += '>';
		// content of tag
		s += this._getContentAsStringGeneric(node);
		// end tag
		s += '</' + node.nodeName + '>';
		return s;
	},        

	_getContentAsStringIE:function(node){
		var s="";
    	for (var i = 0; i < node.childNodes.length; i++)
        	s += node.childNodes[i].xml;
    	return s;
	},
	
	_getContentAsStringMozilla:function(node){
		var xmlSerializer = new XMLSerializer();
	    var s = "";
	    for (var i = 0; i < node.childNodes.length; i++) {
	        s += xmlSerializer.serializeToString(node.childNodes[i]);
	        if (s == "undefined")
		        return this._getContentAsStringGeneric(node);
	    }
	    
        s = this._processTextareas(s);
        
	    return s;
	},
	
	_getContentAsStringGeneric:function(node){
		var s="";
		if (node == null) { return s; }
		for (var i = 0; i < node.childNodes.length; i++) {
			switch (node.childNodes[i].nodeType) {
				case 1: // ELEMENT_NODE
				case 5: // ENTITY_REFERENCE_NODE
					s += this.getElementAsString(node.childNodes[i]);
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
	},

	_processTextareas:function(htmlData)
 	{
        var match = new RegExp(tapestry.html.TextareaMatcher);
        while (htmlData.match(match)){
            htmlData = htmlData.replace(match, tapestry.html.TextareaReplacer);
        }
        return htmlData;
 	}
}

/**
 * package: tapestry.event
 * 
 * Utility functions that handle converting javascript event objects into 
 * a name/value pair format that can be sent to the remote server.
 */
tapestry.event={
	
	/**
	 * Function: buildEventProperties
	 * 
	 * Takes an incoming browser generated event (like key/mouse events) and
	 * creates a js object holding the basic values of the event in order for 
	 * it to be submitted to the server.
	 * 
	 * Parameters: 
	 * 
	 *	event - The javascript event method is based on, if it isn't a valid
	 * 				browser event it will be ignored.
	 *	props - The existing property object to set the values on, if it doesn't
	 * 				exist one will be created.
	 * Returns:
	 * 
	 * The desired event properties bound to an object. Ie obj.target,obj.charCode, etc..
	 */
	buildEventProperties:function(event, props){
		if (!dojo.event.browser.isEvent(event)) return {};
		if (!props) props={};
		
		if(event["type"]) props.beventtype=event.type;
		if(event["keys"]) props.beventkeys=event.keys;
		if(event["charCode"]) props.beventcharCode=event.charCode;
		if(event["pageX"]) props.beventpageX=event.pageX;
		if(event["pageY"]) props.beventpageY=event.pageY;
		if(event["layerX"]) props.beventlayerX=event.layerX;
		if(event["layerY"]) props.beventlayerY=event.layerY;
		
		if (event["target"]) this.buildTargetProperties(props, event.target);
		
		return props;
	},
	
	/**
	 * Function: buildTargetProperties
	 * 
	 * Generic function to build a properties object populated with
	 * relevent target data.
	 * 
	 * Parameters:
	 * 	
	 * 	props - The object that event properties are being set on to return to
	 * 			the server.
	 * 	target - The javscript Event.target object that the original event was targeted for.
	 * 
	 * Returns:
	 * 	The original props object passed in, populated with any data found.
	 */
	buildTargetProperties:function(props, target){
		if(!target) { return; }
		
		if (dojo.dom.isNode(target)) {
			return this.buildNodeProperties(props, target);
		} else {
			dojo.raise("buildTargetProperties() Unknown target type:" + target);
		}
	},
	
	/**
	 * Function: buildNodeProperties
	 * 
	 * Builds needed target node properties, like the node's id.
	 * 
	 * Parameters:
	 * 	props - The object that event properties are being set on to return to
	 * 			the server.
	 * 	node - The dom node specified as the Event.target in a javascript event.
	 */
	buildNodeProperties:function(props, node) {
		if (node.getAttribute("id")) {
			props["beventtarget.id"]=node.getAttribute("id");
		}
	}
}

tapestry.lang = {

	/**
	 * Searches the specified list for an object with a matching propertyName/value pair. 
	 * @param list 			The array of objects to search.
	 * @param properyName	The object property key to match on. (ie object[propertyName]) 
	 * 			Can also be a template object to match in the form of {key:{key:value}} nested
	 * 			as deeply as you like. 
	 * @param value 		The value to be matched against
	 * @return The matching array object found, or null.
	 */
	find:function(list, property, value){
		if (!list || !property || list.length < 1) return null;
		
		// if not propMatch then template object was passed in
		var propMatch=dojo.lang.isString(property);
		if (propMatch && !value) return null; //if doing string/other non template match and no value
		
		for (var i=0; i < list.length; i++) {
			if (!list[i]) continue;
			if (propMatch) {
				if (list[i] && list[i][property] && list[i][property] == value) return list[i];
			} else {
				if (this.matchProperty(property, list[i])) return list[i];
			}
		}
		return null;
	},
	
	// called recursively to match object properties
	// partially stolen logic from dojo.widget.html.SortableTable.sort
	matchProperty:function(template, object){
		if(!dojo.lang.isObject(template) || !dojo.lang.isObject(object))
			return template.valueOf() == object.valueOf();
		
		for(var p in template){
			if(!(p in object)) return false;	//	boolean
			if (!this.matchProperty(template[p], object[p])) return false;
		}
		return true;
	}
}