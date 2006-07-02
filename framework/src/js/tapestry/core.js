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
	scriptInFlight:false,
	ScriptFragment:'(?:<script.*?>)((\n|.|\r)*?)(?:<\/script>)',
	
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
		var scripts=[];
		for (var i=0; i<elms.length; i++) {
			var type=elms[i].getAttribute("type");
			var id=elms[i].getAttribute("id");
			
			if (type == "exception") {
				dojo.log.err("Remote server exception received.");
				tapestry.presentException(elms[i], kwArgs);
				return;
			}
			
			if (type == "script") {
				scripts.push(elms[i]);
				continue;
			}
			
			if (!id) {
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
		
		for (var i=0; i<scripts.length; i++) {
			tapestry.loadScriptContent(scripts[i], true);
		}
	},
	
	loadContent:function(id, node, element){
    	if (element.childNodes && element.childNodes.length > 0) {
        	for (var i = 0; i < element.childNodes.length; i++) {
            	if (element.childNodes[i].nodeType != 1) continue;
				
            	var nodeId = element.childNodes[i].getAttribute("id");
            	if (nodeId && nodeId == id) {
                	element=element.childNodes[i];
                	break;
            	}
        	}
    	}
    	
    	node.innerHTML=tapestry.html.getContentAsString(element);
	},
	
	loadScriptContent:function(element, async){
		if (typeof async == "undefined") async = true;
		
		var text=tapestry.html.getContentAsString(element);
		
		if (tapestry.scriptInFlight) {
			dojo.log.debug("loadScriptContent(): scriptInFlight is true, sleeping");
			setTimeout(function() { tapestry.loadScriptContent(text, async);}, 5);
			return;
		}
		
		var match = new RegExp(tapestry.ScriptFragment, 'img');
	    var response = text.replace(match, '');
	    var scripts = text.match(match);
		
		if (!scripts) return;
		
        match = new RegExp(tapestry.ScriptFragment, 'im');
        if (async) {
        	setTimeout(function() {
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
            }, 60);
        } else {
        	tapestry.scriptInFlight = true;
        	
            for (var i=0; i<scripts.length; i++) {
                var scr = scripts[i].match(match)[1];
                try {
                    dojo.log.debug("synchronous eval of script:" + scr);
                    eval(scr);
                } catch (e) {
                	tapestry.scriptInFlight = false;
                    dojo.log.exception("Error synchronously evaluating script: " + scr, e, false);
                }
            }
            
            tapestry.scriptInFlight = false;
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