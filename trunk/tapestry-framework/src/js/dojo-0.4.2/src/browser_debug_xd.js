
dojo.provide("dojo.browser_debug_xd");dojo.nonDebugProvide = dojo.provide;dojo.provide = function(resourceName){var dbgQueue = dojo.hostenv["xdDebugQueue"];if(dbgQueue && dbgQueue.length > 0 && resourceName == dbgQueue["currentResourceName"]){window.setTimeout("dojo.hostenv.xdDebugFileLoaded('" + resourceName + "')", 1);}
dojo.nonDebugProvide.apply(dojo, arguments);}
dojo.hostenv.xdDebugFileLoaded = function(resourceName){var dbgQueue = this.xdDebugQueue;if(resourceName && resourceName == dbgQueue.currentResourceName){dbgQueue.shift();}
if(dbgQueue.length == 0){dbgQueue.currentResourceName = null;this.xdNotifyLoaded();}else{dbgQueue.currentResourceName = dbgQueue[0].resourceName;var element = document.createElement("script");element.type = "text/javascript";element.src = dbgQueue[0].resourcePath;document.getElementsByTagName("head")[0].appendChild(element);}}
