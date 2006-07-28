dojo.provide("tapestry.event");

dojo.require("dojo.event.browser");
dojo.require("dojo.dom");

tapestry.event = {
	
	/**
	 * Takes an incoming browser generated event (like key/mouse events) and
	 * creates a js object holding the basic values of the event in order for 
	 * it to be submitted to the server.
	 * 
	 * @param event The javascript event method is based on, if it isn't a valid
	 * 				browser event it will be ignored.
	 * @param props The existing property object to set the values on, if it doesn't
	 * 				exist one will be created.
	 * @return The desired event properties bound to an object. Ie obj.target,obj.charCode, etc..
	 */
	buildEventProperties:function(event, props){
		if (!dojo.event.browser.isEvent(event)) return {};
		if (!props) props={};
		
		if(event["type"]) props.beventtype=event.type;
		if(event["keys"]) props.beventkeys=event.keys;
		if (event["charCode"]) props.beventcharCode=event.charCode;
		if(event["pageX"]) props.beventpageX=event.pageX;
		if(event["pageY"]) props.beventpageY=event.pageY;
		if(event["layerX"]) props.beventlayerX=event.layerX;
		if(event["layerY"]) props.beventlayerY=event.layerY;
		
		if (event["target"]) this.buildTargetProperties(props, event.target);
		
		return props;
	},
	
	/**
	 * Generic function to build a properties object populated with
	 * relevent target data.
	 */
	buildTargetProperties:function(props, target){
		if(!target) return;
		
		if (dojo.dom.isNode(target))
			return this.buildNodeProperties(props, target);
		else
			dojo.raise("buildTargetProperties() Unknown target type:" + target);
	},
	
	/**
	 * Builds needed target node properties, like the nodes id.
	 */
	buildNodeProperties:function(props, node) {
		if (node.getAttribute("id"))
			props["beventtarget.id"]=node.getAttribute("id");
	}
}
