

dojo.provide("dojo.undo.browser");
dojo.require("dojo.io.common");

try{
if((!djConfig["preventBackButtonFix"])&&(!dojo.hostenv.post_load_)){
document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(dojo.hostenv.getBaseScriptUri()+'iframe_history.html')+"'></iframe>");
}
}catch(e){}

if(dojo.render.html.opera){
dojo.debug("Opera is not supported with dojo.undo.browser, so back/forward detection will not work.");
}



dojo.undo.browser = {
initialHref: window.location.href,
initialHash: window.location.hash,

moveForward: false,
historyStack: [],
forwardStack: [],
historyIframe: null,
bookmarkAnchor: null,
locationTimer: null,


setInitialState: function(args){
this.initialState = this._createState(this.initialHref, args, this.initialHash);
},





addToHistory: function(args){
//If addToHistory is called, then that means we prune the
//forward stack -- the user went back, then wanted to
//start a new forward path.
this.forwardStack = [];

var hash = null;
var url = null;
if(!this.historyIframe){
this.historyIframe = window.frames["djhistory"];
}
if(!this.bookmarkAnchor){
this.bookmarkAnchor = document.createElement("a");
dojo.body().appendChild(this.bookmarkAnchor);
this.bookmarkAnchor.style.display = "none";
}
if(args["changeUrl"]){
hash = "#"+ ((args["changeUrl"]!==true) ? args["changeUrl"] : (new Date()).getTime());

//If the current hash matches the new one, just replace the history object with
//this new one. It doesn't make sense to track different state objects for the same
//logical URL. This matches the browser behavior of only putting in one history
//item no matter how many times you click on the same #hash link, at least in Firefox
//and Safari, and there is no reliable way in those browsers to know if a #hash link
//has been clicked on multiple times. So making this the standard behavior in all browsers
//so that dojo.undo.browser's behavior is the same in all browsers.
if(this.historyStack.length == 0 && this.initialState.urlHash == hash){
this.initialState = this._createState(url, args, hash);
return;
}else if(this.historyStack.length > 0 && this.historyStack[this.historyStack.length - 1].urlHash == hash){
this.historyStack[this.historyStack.length - 1] = this._createState(url, args, hash);
return;
}

this.changingUrl = true;
setTimeout("window.location.href = '"+hash+"'; dojo.undo.browser.changingUrl = false;", 1);
this.bookmarkAnchor.href = hash;

if(dojo.render.html.ie){
url = this._loadIframeHistory();

var oldCB = args["back"]||args["backButton"]||args["handle"];

//The function takes handleName as a parameter, in case the
//callback we are overriding was "handle". In that case,
//we will need to pass the handle name to handle.
var tcb = function(handleName){
if(window.location.hash != ""){
setTimeout("window.location.href = '"+hash+"';", 1);
}
//Use apply to set "this" to args, and to try to avoid memory leaks.
oldCB.apply(this, [handleName]);
}

//Set interceptor function in the right place.
if(args["back"]){
args.back = tcb;
}else if(args["backButton"]){
args.backButton = tcb;
}else if(args["handle"]){
args.handle = tcb;
}

var oldFW = args["forward"]||args["forwardButton"]||args["handle"];

//The function takes handleName as a parameter, in case the
//callback we are overriding was "handle". In that case,
//we will need to pass the handle name to handle.
var tfw = function(handleName){
if(window.location.hash != ""){
window.location.href = hash;
}
if(oldFW){ // we might not actually have one
//Use apply to set "this" to args, and to try to avoid memory leaks.
oldFW.apply(this, [handleName]);
}
}

//Set interceptor function in the right place.
if(args["forward"]){
args.forward = tfw;
}else if(args["forwardButton"]){
args.forwardButton = tfw;
}else if(args["handle"]){
args.handle = tfw;
}

}else if(dojo.render.html.moz){
// start the timer
if(!this.locationTimer){
this.locationTimer = setInterval("dojo.undo.browser.checkLocation();", 200);
}
}
}else{
url = this._loadIframeHistory();
}

this.historyStack.push(this._createState(url, args, hash));
},

checkLocation: function(){
if (!this.changingUrl){
var hsl = this.historyStack.length;

if((window.location.hash == this.initialHash||window.location.href == this.initialHref)&&(hsl == 1)){
// FIXME: could this ever be a forward button?
// we can't clear it because we still need to check for forwards. Ugg.
// clearInterval(this.locationTimer);
this.handleBackButton();
return;
}

// first check to see if we could have gone forward. We always halt on
// a no-hash item.
if(this.forwardStack.length > 0){
if(this.forwardStack[this.forwardStack.length-1].urlHash == window.location.hash){
this.handleForwardButton();
return;
}
}

// ok, that didn't work, try someplace back in the history stack
if((hsl >= 2)&&(this.historyStack[hsl-2])){
if(this.historyStack[hsl-2].urlHash==window.location.hash){
this.handleBackButton();
return;
}
}
}
},

iframeLoaded: function(evt, ifrLoc){
if(!dojo.render.html.opera){
var query = this._getUrlQuery(ifrLoc.href);
if(query == null){
// alert("iframeLoaded");
// we hit the end of the history, so we should go back
if(this.historyStack.length == 1){
this.handleBackButton();
}
return;
}
if(this.moveForward){
// we were expecting it, so it's not either a forward or backward movement
this.moveForward = false;
return;
}

//Check the back stack first, since it is more likely.
//Note that only one step back or forward is supported.
if(this.historyStack.length >= 2 && query == this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){
this.handleBackButton();
}
else if(this.forwardStack.length > 0 && query == this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){
this.handleForwardButton();
}
}
},

handleBackButton: function(){
//The "current" page is always at the top of the history stack.
var current = this.historyStack.pop();
if(!current){ return; }
var last = this.historyStack[this.historyStack.length-1];
if(!last && this.historyStack.length == 0){
last = this.initialState;
}
if (last){
if(last.kwArgs["back"]){
last.kwArgs["back"]();
}else if(last.kwArgs["backButton"]){
last.kwArgs["backButton"]();
}else if(last.kwArgs["handle"]){
last.kwArgs.handle("back");
}
}
this.forwardStack.push(current);
},

handleForwardButton: function(){
var last = this.forwardStack.pop();
if(!last){ return; }
if(last.kwArgs["forward"]){
last.kwArgs.forward();
}else if(last.kwArgs["forwardButton"]){
last.kwArgs.forwardButton();
}else if(last.kwArgs["handle"]){
last.kwArgs.handle("forward");
}
this.historyStack.push(last);
},

_createState: function(url, args, hash){
return {"url": url, "kwArgs": args, "urlHash": hash};
},

_getUrlQuery: function(url){
var segments = url.split("?");
if (segments.length < 2){
return null;
}
else{
return segments[1];
}
},

_loadIframeHistory: function(){
var url = dojo.hostenv.getBaseScriptUri()+"iframe_history.html?"+(new Date()).getTime();
this.moveForward = true;
dojo.io.setIFrameSrc(this.historyIframe, url, false);
return url;
}
}
