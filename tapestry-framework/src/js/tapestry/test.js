dojo.provide("tapestry.test");

dojo.require("dojo.logging.Logger");
dojo.require("dojo.event.browser");

// override to make sure our fake events pass
dojo.event.browser.isEvent=function() { return true; }

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

