dojo.provide("tapestry.test");
dojo.require("dojo.logging.Logger");
dojo.require("dojo.io");
dojo.require("dojo.io.*");

// override to make sure our fake events pass
dojo.event.browser.isEvent=function() { return true; }

if (dj_undef("byId", dj_global)) {
dojo.byId = function(id, doc){
	if(id && (typeof id == "string" || id instanceof String)){
		if(!doc){ doc = document; }
		return doc.getElementById(id);
	}
	return id; // assume it's a node
}
}

dojo.debug=function(message){
	dojo.log.debug(message);
}

dojo.debugShallow = function(obj){
	if (!djConfig.isDebug) { return; }
	dojo.debug('------------------------------------------------------------');
	dojo.debug('Object: '+obj);
	var props = [];
	for(var prop in obj){
		try {
			props.push(prop + ': ' + obj[prop]);
		} catch(E) {
			props.push(prop + ': ERROR - ' + E.message);
		}
	}
	props.sort();
	for(var i = 0; i < props.length; i++) {
		dojo.debug(props[i]);
	}
	dojo.debug('------------------------------------------------------------');
}

function lastMsgContains(str){
	if (arguments.length < 1) return false;
	
	var last=dojo.logging.logQueueHandler.data.pop();
	if (!last || !last.message) return false;
	
	return last.message.toUpperCase().indexOf(str.toUpperCase()) > -1;
}

// helper object for logging method calls
function mock(){
	this.mockArgs=arguments;
	this.called=false;
	
	this.intercept=function(){
		this.called=true;
		jum.assertEquals("mockArgLength", this.mockArgs.length, arguments.length);
		
		for (var i=0; i < this.mockArgs.length; i++) {
			jum.assertEquals("mockArgument", this.mockArgs[i], arguments[i]);
		}
	}
}

dojo.io.bind=function(kwArgs){
	dojo.log.info("dojo.io.RhinoIO.bind():");
	dojo.debugShallow(kwArgs);
}
