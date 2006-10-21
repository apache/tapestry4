

dojo.provide("dojo.io.common");
dojo.require("dojo.string");
dojo.require("dojo.lang.extras");





dojo.io.transports = [];
dojo.io.hdlrFuncNames = [ "load", "error", "timeout" ]; // we're omitting a progress() event for now

dojo.io.Request = function( url,  mimetype,  transport,  changeUrl){






if((arguments.length == 1)&&(arguments[0].constructor == Object)){
this.fromKwArgs(arguments[0]);
}else{
this.url = url;
if(mimetype){ this.mimetype = mimetype; }
if(transport){ this.transport = transport; }
if(arguments.length >= 4){ this.changeUrl = changeUrl; }
}
}

dojo.lang.extend(dojo.io.Request, {


url: "",


mimetype: "text/plain",


method: "GET",


content: undefined, // Object


transport: undefined, // String


changeUrl: undefined, // String


formNode: undefined, // HTMLFormElement


sync: false,

bindSuccess: false,


useCache: false,


preventCache: false,


load: function( type,  data,  transportImplementation,  kwArgs){
// summary:
//		Called on successful completion of a bind.
//		type:
//				A string with value "load"
//		data:
//				The object representing the result of the bind. The actual structure
//				of the data object will depend on the mimetype that was given to bind
//				in the bind arguments.
//		transportImplementation:
//				The object that implements a particular transport. Structure is depedent
//				on the transport. For XMLHTTPTransport (dojo.io.BrowserIO), it will be the
//				XMLHttpRequest object from the browser.
//		kwArgs:
//				Object that contains the request parameters that were given to the
//				bind call. Useful for storing and retrieving state from when bind
//				was called.
},
error: function( type,  error,  transportImplementation,  kwArgs){
// summary:
//		Called when there is an error with a bind.
//		type:
//				A string with value "error"
//		error:
//				The error object. Should be a dojo.io.Error object, but not guaranteed.
//		transportImplementation:
//				The object that implements a particular transport. Structure is depedent
//				on the transport. For XMLHTTPTransport (dojo.io.BrowserIO), it will be the
//				XMLHttpRequest object from the browser.
//		kwArgs:
//				Object that contains the request parameters that were given to the
//				bind call. Useful for storing and retrieving state from when bind
//				was called.
},
timeout: function( type,  empty,  transportImplementation,  kwArgs){
// summary:
//		Called when there is an error with a bind. Only implemented in certain transports at this time.
//		type:
//				A string with value "timeout"
//		empty:
//				Should be null. Just a spacer argument so that load, error, timeout and handle have the
//				same signatures.
//		transportImplementation:
//				The object that implements a particular transport. Structure is depedent
//				on the transport. For XMLHTTPTransport (dojo.io.BrowserIO), it will be the
//				XMLHttpRequest object from the browser. May be null for the timeout case for
//				some transports.
//		kwArgs:
//				Object that contains the request parameters that were given to the
//				bind call. Useful for storing and retrieving state from when bind
//				was called.
},
handle: function( type,  data,  transportImplementation,  kwArgs){
// summary:
//		The handle method can be defined instead of defining separate load, error and timeout
//		callbacks.
//		type:
//				A string with the type of callback: "load", "error", or "timeout".
//		data:
//				See the above callbacks for what this parameter could be.
//		transportImplementation:
//				The object that implements a particular transport. Structure is depedent
//				on the transport. For XMLHTTPTransport (dojo.io.BrowserIO), it will be the
//				XMLHttpRequest object from the browser.
//		kwArgs:
//				Object that contains the request parameters that were given to the
//				bind call. Useful for storing and retrieving state from when bind
//				was called.
},




timeoutSeconds: 0,



abort: function(){ },




fromKwArgs: function( kwArgs){
// summary:
//		Creates a dojo.io.Request from a simple object (kwArgs object).

// normalize args
if(kwArgs["url"]){ kwArgs.url = kwArgs.url.toString(); }
if(kwArgs["formNode"]) { kwArgs.formNode = dojo.byId(kwArgs.formNode); }
if(!kwArgs["method"] && kwArgs["formNode"] && kwArgs["formNode"].method) {
kwArgs.method = kwArgs["formNode"].method;
}

// backwards compatibility
if(!kwArgs["handle"] && kwArgs["handler"]){ kwArgs.handle = kwArgs.handler; }
if(!kwArgs["load"] && kwArgs["loaded"]){ kwArgs.load = kwArgs.loaded; }
if(!kwArgs["changeUrl"] && kwArgs["changeURL"]) { kwArgs.changeUrl = kwArgs.changeURL; }

// encoding fun!
kwArgs.encoding = dojo.lang.firstValued(kwArgs["encoding"], djConfig["bindEncoding"], "");

kwArgs.sendTransport = dojo.lang.firstValued(kwArgs["sendTransport"], djConfig["ioSendTransport"], false);

var isFunction = dojo.lang.isFunction;
for(var x=0; x<dojo.io.hdlrFuncNames.length; x++){
var fn = dojo.io.hdlrFuncNames[x];
if(kwArgs[fn] && isFunction(kwArgs[fn])){ continue; }
if(kwArgs["handle"] && isFunction(kwArgs["handle"])){
kwArgs[fn] = kwArgs.handle;
}
// handler is aliased above, shouldn't need this check

}
dojo.lang.mixin(this, kwArgs);
}

});

dojo.io.Error = function( msg,  type, num){


this.message = msg;
this.type =  type || "unknown"; // must be one of "io", "parse", "unknown"
this.number = num || 0; // per-substrate error number, not normalized
}

dojo.io.transports.addTransport = function(name){


this.push(name);


this[name] = dojo.io[name];
}



dojo.io.bind = function( request){








if(!(request instanceof dojo.io.Request)){
try{
request = new dojo.io.Request(request);
}catch(e){ dojo.debug(e); }
}


var tsName = "";
if(request["transport"]){
tsName = request["transport"];
if(!this[tsName]){
dojo.io.sendBindError(request, "No dojo.io.bind() transport with name '"
+ request["transport"] + "'.");
return request; //dojo.io.Request
}
if(!this[tsName].canHandle(request)){
dojo.io.sendBindError(request, "dojo.io.bind() transport with name '"
+ request["transport"] + "' cannot handle this type of request.");
return request;	//dojo.io.Request
}
}else{
// otherwise we do our best to auto-detect what available transports
// will handle
for(var x=0; x<dojo.io.transports.length; x++){
var tmp = dojo.io.transports[x];
if((this[tmp])&&(this[tmp].canHandle(request))){
tsName = tmp;
break;
}
}
if(tsName == ""){
dojo.io.sendBindError(request, "None of the loaded transports for dojo.io.bind()"
+ " can handle the request.");
return request; //dojo.io.Request
}
}
this[tsName].bind(request);
request.bindSuccess = true;
return request; //dojo.io.Request
}

dojo.io.sendBindError = function(request , message ){




if((typeof request.error == "function" || typeof request.handle == "function")
&& (typeof setTimeout == "function" || typeof setTimeout == "object")){
var errorObject = new dojo.io.Error(message);
setTimeout(function(){
request[(typeof request.error == "function") ? "error" : "handle"]("error", errorObject, null, request);
}, 50);
}else{
dojo.raise(message);
}
}

dojo.io.queueBind = function( request){







if(!(request instanceof dojo.io.Request)){
try{
request = new dojo.io.Request(request);
}catch(e){ dojo.debug(e); }
}


var oldLoad = request.load;
request.load = function(){
dojo.io._queueBindInFlight = false;
var ret = oldLoad.apply(this, arguments);
dojo.io._dispatchNextQueueBind();
return ret;
}

var oldErr = request.error;
request.error = function(){
dojo.io._queueBindInFlight = false;
var ret = oldErr.apply(this, arguments);
dojo.io._dispatchNextQueueBind();
return ret;
}

dojo.io._bindQueue.push(request);
dojo.io._dispatchNextQueueBind();
return request; //dojo.io.Request
}

dojo.io._dispatchNextQueueBind = function(){


if(!dojo.io._queueBindInFlight){
dojo.io._queueBindInFlight = true;
if(dojo.io._bindQueue.length > 0){
dojo.io.bind(dojo.io._bindQueue.shift());
}else{
dojo.io._queueBindInFlight = false;
}
}
}
dojo.io._bindQueue = [];
dojo.io._queueBindInFlight = false;

dojo.io.argsFromMap = function( map,  encoding,  last){











var enc = /utf/i.test(encoding||"") ? encodeURIComponent : dojo.string.encodeAscii;
var mapped = [];
var control = new Object();
for(var name in map){
var domap = function(elt){
var val = enc(name)+"="+enc(elt);
mapped[(last == name) ? "push" : "unshift"](val);
}
if(!control[name]){
var value = map[name];
// FIXME: should be isArrayLike?
if (dojo.lang.isArray(value)){
dojo.lang.forEach(value, domap);
}else{
domap(value);
}
}
}
return mapped.join("&"); //String
}

dojo.io.setIFrameSrc = function( iframe,  src,  replace){



try{
var r = dojo.render.html;
// dojo.debug(iframe);
if(!replace){
if(r.safari){
iframe.location = src;
}else{
frames[iframe.name].location = src;
}
}else{
// Fun with DOM 0 incompatibilities!
var idoc;
if(r.ie){
idoc = iframe.contentWindow.document;
}else if(r.safari){
idoc = iframe.document;
}else{ //  if(r.moz){
idoc = iframe.contentWindow;
}

//For Safari (at least 2.0.3) and Opera, if the iframe
//has just been created but it doesn't have content
//yet, then iframe.document may be null. In that case,
//use iframe.location and return.
if(!idoc){
iframe.location = src;
return;
}else{
idoc.location.replace(src);
}
}
}catch(e){
dojo.debug(e);
dojo.debug("setIFrameSrc: "+e);
}
}


