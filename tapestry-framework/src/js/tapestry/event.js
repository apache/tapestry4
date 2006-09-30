dojo.provide("tapestry.event");

dojo.require("tapestry.lang");
dojo.require("dojo.event.browser");
dojo.require("dojo.dom");

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
