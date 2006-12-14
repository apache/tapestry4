
dojo.debug = function(){if (!djConfig.isDebug) { return; }
var args = arguments;if(dj_undef("println", dojo.hostenv)){dojo.raise("dojo.debug not available (yet?)");}
var isJUM = dj_global["jum"] && !dj_global["jum"].isBrowser;var s = [(isJUM ? "": "DEBUG: ")];for(var i=0;i<args.length;++i){if(!false && args[i] && args[i] instanceof Error){var msg = "[" + args[i].name + ": " + dojo.errorToString(args[i]) +
(args[i].fileName ? ", file: " + args[i].fileName : "") +
(args[i].lineNumber ? ", line: " + args[i].lineNumber : "") + "]";} else {try {var msg = String(args[i]);} catch(e) {if(dojo.render.html.ie) {var msg = "[ActiveXObject]";} else {var msg = "[unknown]";}}
}
s.push(msg);}
dojo.hostenv.println(s.join(" "));}
dojo.debugShallow = function(obj){if (!djConfig.isDebug) { return; }
dojo.debug('------------------------------------------------------------');dojo.debug('Object: '+obj);var props = [];for(var prop in obj){try {props.push(prop + ': ' + obj[prop]);} catch(E) {props.push(prop + ': ERROR - ' + E.message);}}
props.sort();for(var i = 0; i < props.length; i++) {dojo.debug(props[i]);}
dojo.debug('------------------------------------------------------------');}
dojo.debugDeep = function(obj){if (!djConfig.isDebug) { return; }
if (!dojo.uri || !dojo.uri.dojoUri){ return dojo.debug("You'll need to load dojo.uri.* for deep debugging - sorry!"); }
if (!window.open){ return dojo.debug('Deep debugging is only supported in host environments with window.open'); }
var idx = dojo.debugDeep.debugVars.length;dojo.debugDeep.debugVars.push(obj);var url = new dojo.uri.Uri(location, dojo.uri.dojoUri("src/debug/deep.html?var="+idx)).toString();var win = window.open(url, '_blank', 'width=600, height=400, resizable=yes, scrollbars=yes, status=yes');try{win.debugVar = obj;}catch(e){}}
dojo.debugDeep.debugVars = [];