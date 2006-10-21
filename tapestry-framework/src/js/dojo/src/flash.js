

dojo.provide("dojo.flash");

dojo.require("dojo.string.*");
dojo.require("dojo.uri.*");
dojo.require("dojo.html.common");



dojo.flash = {
flash6_version: null,
flash8_version: null,
ready: false,
_visible: true,
_loadedListeners: new Array(),
_installingListeners: new Array(),


setSwf: function(fileInfo){
//dojo.debug("setSwf");
if(fileInfo == null || dojo.lang.isUndefined(fileInfo)){
return;
}

if(fileInfo.flash6 != null && !dojo.lang.isUndefined(fileInfo.flash6)){
this.flash6_version = fileInfo.flash6;
}

if(fileInfo.flash8 != null && !dojo.lang.isUndefined(fileInfo.flash8)){
this.flash8_version = fileInfo.flash8;
}

if(!dojo.lang.isUndefined(fileInfo.visible)){
this._visible = fileInfo.visible;
}

// initialize ourselves
this._initialize();
},


useFlash6: function(){
if(this.flash6_version == null){
return false;
}else if (this.flash6_version != null && dojo.flash.info.commVersion == 6){
// if we have a flash 6 version of this SWF, and this browser supports
// communicating using Flash 6 features...
return true;
}else{
return false;
}
},


useFlash8: function(){
if(this.flash8_version == null){
return false;
}else if (this.flash8_version != null && dojo.flash.info.commVersion == 8){
// if we have a flash 8 version of this SWF, and this browser supports
// communicating using Flash 8 features...
return true;
}else{
return false;
}
},


addLoadedListener: function(listener){
this._loadedListeners.push(listener);
},


addInstallingListener: function(listener){
this._installingListeners.push(listener);
},


loaded: function(){
//dojo.debug("dojo.flash.loaded");
dojo.flash.ready = true;
if(dojo.flash._loadedListeners.length > 0){
for(var i = 0;i < dojo.flash._loadedListeners.length; i++){
dojo.flash._loadedListeners[i].call(null);
}
}
},


installing: function(){
//dojo.debug("installing");
if(dojo.flash._installingListeners.length > 0){
for(var i = 0; i < dojo.flash._installingListeners.length; i++){
dojo.flash._installingListeners[i].call(null);
}
}
},


_initialize: function(){
//dojo.debug("dojo.flash._initialize");
// see if we need to rev or install Flash on this platform
var installer = new dojo.flash.Install();
dojo.flash.installer = installer;

if(installer.needed() == true){
installer.install();
}else{
//dojo.debug("Writing object out");
// write the flash object into the page
dojo.flash.obj = new dojo.flash.Embed(this._visible);
dojo.flash.obj.write(dojo.flash.info.commVersion);

// initialize the way we do Flash/JavaScript communication
dojo.flash.comm = new dojo.flash.Communicator();
}
}
};



dojo.flash.Info = function(){


if(dojo.render.html.ie){
document.writeln('<script language="VBScript" type="text/vbscript"\>');
document.writeln('Function VBGetSwfVer(i)');
document.writeln('  on error resume next');
document.writeln('  Dim swControl, swVersion');
document.writeln('  swVersion = 0');
document.writeln('  set swControl = CreateObject("ShockwaveFlash.ShockwaveFlash." + CStr(i))');
document.writeln('  if (IsObject(swControl)) then');
document.writeln('    swVersion = swControl.GetVariable("$version")');
document.writeln('  end if');
document.writeln('  VBGetSwfVer = swVersion');
document.writeln('End Function');
document.writeln('</script\>');
}

this._detectVersion();
this._detectCommunicationVersion();
}

dojo.flash.Info.prototype = {

version: -1,


versionMajor: -1,
versionMinor: -1,
versionRevision: -1,


capable: false,


commVersion: 6,


installing: false,


isVersionOrAbove: function(reqMajorVer, reqMinorVer, reqVer){
// make the revision a decimal (i.e. transform revision 14 into
// 0.14
reqVer = parseFloat("." + reqVer);

if(this.versionMajor >= reqMajorVer && this.versionMinor >= reqMinorVer
&& this.versionRevision >= reqVer){
return true;
}else{
return false;
}
},

_detectVersion: function(){
var versionStr;

// loop backwards through the versions until we find the newest version
for(var testVersion = 25; testVersion > 0; testVersion--){
if(dojo.render.html.ie){
versionStr = VBGetSwfVer(testVersion);
}else{
versionStr = this._JSFlashInfo(testVersion);
}

if(versionStr == -1 ){
this.capable = false;
return;
}else if(versionStr != 0){
var versionArray;
if(dojo.render.html.ie){
var tempArray = versionStr.split(" ");
var tempString = tempArray[1];
versionArray = tempString.split(",");
}else{
versionArray = versionStr.split(".");
}

this.versionMajor = versionArray[0];
this.versionMinor = versionArray[1];
this.versionRevision = versionArray[2];

// 7.0r24 == 7.24
var versionString = this.versionMajor + "." + this.versionRevision;
this.version = parseFloat(versionString);

this.capable = true;

break;
}
}
},


_JSFlashInfo: function(testVersion){
// NS/Opera version >= 3 check for Flash plugin in plugin array
if(navigator.plugins != null && navigator.plugins.length > 0){
if(navigator.plugins["Shockwave Flash 2.0"] ||
navigator.plugins["Shockwave Flash"]){
var swVer2 = navigator.plugins["Shockwave Flash 2.0"] ? " 2.0" : "";
var flashDescription = navigator.plugins["Shockwave Flash" + swVer2].description;
var descArray = flashDescription.split(" ");
var tempArrayMajor = descArray[2].split(".");
var versionMajor = tempArrayMajor[0];
var versionMinor = tempArrayMajor[1];
if(descArray[3] != ""){
var tempArrayMinor = descArray[3].split("r");
}else{
var tempArrayMinor = descArray[4].split("r");
}
var versionRevision = tempArrayMinor[1] > 0 ? tempArrayMinor[1] : 0;
var version = versionMajor + "." + versionMinor + "."
+ versionRevision;

return version;
}
}

return -1;
},


_detectCommunicationVersion: function(){
if(this.capable == false){
this.commVersion = null;
return;
}

// detect if the user has over-ridden the default flash version
if (typeof djConfig["forceFlashComm"] != "undefined" &&
typeof djConfig["forceFlashComm"] != null){
this.commVersion = djConfig["forceFlashComm"];
return;
}

// we prefer Flash 6 features over Flash 8, because they are much faster
// and much less buggy

// at this point, we don't have a flash file to detect features on,
// so we need to instead look at the browser environment we are in
if(dojo.render.html.safari == true || dojo.render.html.opera == true){
this.commVersion = 8;
}else{
this.commVersion = 6;
}
}
};


dojo.flash.Embed = function(visible){
this._visible = visible;
}

dojo.flash.Embed.prototype = {

width: 215,


height: 138,


id: "flashObject",


_visible: true,

protocol: function(){
switch(window.location.protocol){
case "https:":
return "https";
break;
default:
return "http";
break;
}
},



write: function(flashVer, doExpressInstall){
//dojo.debug("write");
if(dojo.lang.isUndefined(doExpressInstall)){
doExpressInstall = false;
}

// determine our container div's styling
var containerStyle = new dojo.string.Builder();
containerStyle.append("width: " + this.width + "px; ");
containerStyle.append("height: " + this.height + "px; ");
if(this._visible == false){
containerStyle.append("position: absolute; ");
containerStyle.append("z-index: 10000; ");
containerStyle.append("top: -1000px; ");
containerStyle.append("left: -1000px; ");
}
containerStyle = containerStyle.toString();

// figure out the SWF file to get and how to write out the correct HTML
// for this Flash version
var objectHTML;
var swfloc;
// Flash 6
if(flashVer == 6){
swfloc = dojo.flash.flash6_version;
var dojoPath = djConfig.baseRelativePath;
swfloc = swfloc + "?baseRelativePath=" + escape(dojoPath);
objectHTML =
'<embed id="' + this.id + '" src="' + swfloc + '" '
+ '    quality="high" bgcolor="#ffffff" '
+ '    width="' + this.width + '" height="' + this.height + '" '
+ '    name="' + this.id + '" '
+ '    align="middle" allowScriptAccess="sameDomain" '
+ '    type="application/x-shockwave-flash" swLiveConnect="true" '
+ '    pluginspage="'
+ this.protocol()
+ '://www.macromedia.com/go/getflashplayer">';
}else{ // Flash 8
swfloc = dojo.flash.flash8_version;
var swflocObject = swfloc;
var swflocEmbed = swfloc;
var dojoPath = djConfig.baseRelativePath;
if(doExpressInstall){
// the location to redirect to after installing
var redirectURL = escape(window.location);
document.title = document.title.slice(0, 47) + " - Flash Player Installation";
var docTitle = escape(document.title);
swflocObject += "?MMredirectURL=" + redirectURL
+ "&MMplayerType=ActiveX"
+ "&MMdoctitle=" + docTitle
+ "&baseRelativePath=" + escape(dojoPath);
swflocEmbed += "?MMredirectURL=" + redirectURL
+ "&MMplayerType=PlugIn"
+ "&baseRelativePath=" + escape(dojoPath);
}

if(swflocEmbed.indexOf("?") == -1){
swflocEmbed +=  "?baseRelativePath="+escape(dojoPath)+"' ";
}

objectHTML =
'<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" '
+ 'codebase="'
+ this.protocol()
+ '://fpdownload.macromedia.com/pub/shockwave/cabs/flash/'
+ 'swflash.cab#version=8,0,0,0" '
+ 'width="' + this.width + '" '
+ 'height="' + this.height + '" '
+ 'id="' + this.id + '" '
+ 'align="middle"> '
+ '<param name="allowScriptAccess" value="sameDomain" /> '
+ '<param name="movie" value="' + swflocObject + '" /> '
+ '<param name="quality" value="high" /> '
+ '<param name="bgcolor" value="#ffffff" /> '
+ '<embed src="' + swflocEmbed + "' "
+ 'quality="high" '
+ 'bgcolor="#ffffff" '
+ 'width="' + this.width + '" '
+ 'height="' + this.height + '" '
+ 'id="' + this.id + '" '
+ 'name="' + this.id + '" '
+ 'swLiveConnect="true" '
+ 'align="middle" '
+ 'allowScriptAccess="sameDomain" '
+ 'type="application/x-shockwave-flash" '
+ 'pluginspage="'
+ this.protocol()
+'://www.macromedia.com/go/getflashplayer" />'
+ '</object>';
}

// now write everything out
objectHTML = '<div id="' + this.id + 'Container" style="' + containerStyle + '"> '
+ objectHTML
+ '</div>';
document.writeln(objectHTML);
},


get: function(){
//return (dojo.render.html.ie) ? window[this.id] : document[this.id];

// more robust way to get Flash object; version above can break
// communication on IE sometimes
return document.getElementById(this.id);
},


setVisible: function(visible){
var container = dojo.byId(this.id + "Container");
if(visible == true){
container.style.visibility = "visible";
}else{
container.style.position = "absolute";
container.style.x = "-1000px";
container.style.y = "-1000px";
container.style.visibility = "hidden";
}
},


center: function(){
var elementWidth = this.width;
var elementHeight = this.height;

var scroll_offset = dojo.html.getScroll().offset;
var viewport_size = dojo.html.getViewport();

// compute the centered position
var x = scroll_offset.x + (viewport_size.x - elementWidth) / 2;
var y = scroll_offset.y + (viewport_size.y - elementHeight) / 2;

// set the centered position
var container = dojo.byId(this.id + "Container");
container.style.top = y + "px";
container.style.left = x + "px";
}
};



dojo.flash.Communicator = function(){
if(dojo.flash.useFlash6()){
this._writeFlash6();
}else if (dojo.flash.useFlash8()){
this._writeFlash8();
}
}

dojo.flash.Communicator.prototype = {
_writeFlash6: function(){
var id = dojo.flash.obj.id;

// global function needed for Flash 6 callback;
// we write it out as a script tag because the VBScript hook for IE
// callbacks does not work properly if this function is evalled() from
// within the Dojo system
document.writeln('<script language="JavaScript">');
document.writeln('  function ' + id + '_DoFSCommand(command, args){ ');
document.writeln('    dojo.flash.comm._handleFSCommand(command, args); ');
document.writeln('}');
document.writeln('</script>');

// hook for Internet Explorer to receive FSCommands from Flash
if(dojo.render.html.ie){
document.writeln('<SCRIPT LANGUAGE=VBScript\> ');
document.writeln('on error resume next ');
document.writeln('Sub ' + id + '_FSCommand(ByVal command, ByVal args)');
document.writeln(' call ' + id + '_DoFSCommand(command, args)');
document.writeln('end sub');
document.writeln('</SCRIPT\> ');
}
},

_writeFlash8: function(){
// nothing needs to be written out for Flash 8 communication;
// happens automatically
},




_handleFSCommand: function(command, args){
//dojo.debug("fscommand, command="+command+", args="+args);
// Flash 8 on Mac/Firefox precedes all commands with the string "FSCommand:";
// strip it off if it is present
if(command != null && !dojo.lang.isUndefined(command)
&& /^FSCommand:(.*)/.test(command) == true){
command = command.match(/^FSCommand:(.*)/)[1];
}

if(command == "addCallback"){ // add Flash method for JavaScript callback
this._fscommandAddCallback(command, args);
}else if(command == "call"){ // Flash to JavaScript method call
this._fscommandCall(command, args);
}else if(command == "fscommandReady"){ // see if fscommands are ready
this._fscommandReady();
}
},


_fscommandAddCallback: function(command, args){
var functionName = args;

// do a trick, where we link this function name to our wrapper
// function, _call, that does the actual JavaScript to Flash call
var callFunc = function(){
return dojo.flash.comm._call(functionName, arguments);
};
dojo.flash.comm[functionName] = callFunc;

// indicate that the call was successful
dojo.flash.obj.get().SetVariable("_succeeded", true);
},


_fscommandCall: function(command, args){
var plugin = dojo.flash.obj.get();
var functionName = args;

// get the number of arguments to this method call and build them up
var numArgs = parseInt(plugin.GetVariable("_numArgs"));
var flashArgs = new Array();
for(var i = 0; i < numArgs; i++){
var currentArg = plugin.GetVariable("_" + i);
flashArgs.push(currentArg);
}

// get the function instance; we technically support more capabilities
// than ExternalInterface, which can only call global functions; if
// the method name has a dot in it, such as "dojo.flash.loaded", we
// eval it so that the method gets run against an instance
var runMe;
if(functionName.indexOf(".") == -1){ // global function
runMe = window[functionName];
}else{
// instance function
runMe = eval(functionName);
}

// make the call and get the results
var results = null;
if(!dojo.lang.isUndefined(runMe) && runMe != null){
results = runMe.apply(null, flashArgs);
}

// return the results to flash
plugin.SetVariable("_returnResult", results);
},


_fscommandReady: function(){
var plugin = dojo.flash.obj.get();
plugin.SetVariable("fscommandReady", "true");
},


_call: function(functionName, args){
// we do JavaScript to Flash method calls by setting a Flash variable
// "_functionName" with the function name; "_numArgs" with the number
// of arguments; and "_0", "_1", etc for each numbered argument. Flash
// reads these, executes the function call, and returns the result
// in "_returnResult"
var plugin = dojo.flash.obj.get();
plugin.SetVariable("_functionName", functionName);
plugin.SetVariable("_numArgs", args.length);
for(var i = 0; i < args.length; i++){
// unlike Flash 8's ExternalInterface, Flash 6 has no problem with
// any special characters _except_ for the null character \0; double
// encode this so the Flash side never sees it, but we can get it
// back if the value comes back to JavaScript
var value = args[i];
value = value.replace(/\0/g, "\\0");

plugin.SetVariable("_" + i, value);
}

// now tell Flash to execute this method using the Flash Runner
plugin.TCallLabel("/_flashRunner", "execute");

// get the results
var results = plugin.GetVariable("_returnResult");

// we double encoded all null characters as //0 because Flash breaks
// if they are present; turn the //0 back into /0
results = results.replace(/\\0/g, "\0");

return results;
},




_addExternalInterfaceCallback: function(methodName){
var wrapperCall = function(){
// some browsers don't like us changing values in the 'arguments' array, so
// make a fresh copy of it
var methodArgs = new Array(arguments.length);
for(var i = 0; i < arguments.length; i++){
methodArgs[i] = arguments[i];
}
return dojo.flash.comm._execFlash(methodName, methodArgs);
};

dojo.flash.comm[methodName] = wrapperCall;
},


_encodeData: function(data){
// double encode all entity values, or they will be mis-decoded
// by Flash when returned
var entityRE = /\&([^;]*)\;/g;
data = data.replace(entityRE, "&amp;$1;");

// entity encode XML-ish characters, or Flash's broken XML serializer
// breaks
data = data.replace(/</g, "&lt;");
data = data.replace(/>/g, "&gt;");

// transforming \ into \\ doesn't work; just use a custom encoding
data = data.replace("\\", "&custom_backslash;&custom_backslash;");

data = data.replace(/\n/g, "\\n");
data = data.replace(/\r/g, "\\r");
data = data.replace(/\f/g, "\\f");
data = data.replace(/\0/g, "\\0"); // null character
data = data.replace(/\'/g, "\\\'");
data = data.replace(/\"/g, '\\\"');

return data;
},


_decodeData: function(data){
if(data == null || typeof data == "undefined"){
return data;
}

// certain XMLish characters break Flash's wire serialization for
// ExternalInterface; these are encoded on the
// DojoExternalInterface side into a custom encoding, rather than
// the standard entity encoding, because otherwise we won't be able to
// differentiate between our own encoding and any entity characters
// that are being used in the string itself
data = data.replace(/\&custom_lt\;/g, "<");
data = data.replace(/\&custom_gt\;/g, ">");

// Unfortunately, Flash returns us our String with special characters
// like newlines broken into seperate characters. So if \n represents
// a new line, Flash returns it as "\" and "n". This means the character
// is _not_ a newline. This forces us to eval() the string to cause
// escaped characters to turn into their real special character values.
data = eval('"' + data + '"');

return data;
},


_chunkArgumentData: function(value, argIndex){
var plugin = dojo.flash.obj.get();

// cut up the string into pieces, and push over each piece one
// at a time
var numSegments = Math.ceil(value.length / 1024);
for(var i = 0; i < numSegments; i++){
var startCut = i * 1024;
var endCut = i * 1024 + 1024;
if(i == (numSegments - 1)){
endCut = i * 1024 + value.length;
}

var piece = value.substring(startCut, endCut);

// encode each piece seperately, rather than the entire
// argument data, because ocassionally a special
// character, such as an entity like &foobar;, will fall between
// piece boundaries, and we _don't_ want to encode that value if
// it falls between boundaries, or else we will end up with incorrect
// data when we patch the pieces back together on the other side
piece = this._encodeData(piece);

// directly use the underlying CallFunction method used by
// ExternalInterface, which is vastly faster for large strings
// and lets us bypass some Flash serialization bugs
plugin.CallFunction('<invoke name="chunkArgumentData" '
+ 'returntype="javascript">'
+ '<arguments>'
+ '<string>' + piece + '</string>'
+ '<number>' + argIndex + '</number>'
+ '</arguments>'
+ '</invoke>');
}
},


_chunkReturnData: function(){
var plugin = dojo.flash.obj.get();

var numSegments = plugin.getReturnLength();
var resultsArray = new Array();
for(var i = 0; i < numSegments; i++){
// directly use the underlying CallFunction method used by
// ExternalInterface, which is vastly faster for large strings
var piece =
plugin.CallFunction('<invoke name="chunkReturnData" '
+ 'returntype="javascript">'
+ '<arguments>'
+ '<number>' + i + '</number>'
+ '</arguments>'
+ '</invoke>');

// remove any leading or trailing JavaScript delimiters, which surround
// our String when it comes back from Flash since we bypass Flash's
// deserialization routines by directly calling CallFunction on the
// plugin
if(piece == '""' || piece == "''"){
piece = "";
}else{
piece = piece.substring(1, piece.length-1);
}

resultsArray.push(piece);
}
var results = resultsArray.join("");

return results;
},


_execFlash: function(methodName, methodArgs){
var plugin = dojo.flash.obj.get();

// begin Flash method execution
plugin.startExec();

// set the number of arguments
plugin.setNumberArguments(methodArgs.length);

// chunk and send over each argument
for(var i = 0; i < methodArgs.length; i++){
this._chunkArgumentData(methodArgs[i], i);
}

// execute the method
plugin.exec(methodName);

// get the return result
var results = this._chunkReturnData();

// decode the results
results = this._decodeData(results);

// reset everything
plugin.endExec();

return results;

}
}


dojo.flash.Install = function(){
}

dojo.flash.Install.prototype = {

needed: function(){
// do we even have flash?
if(dojo.flash.info.capable == false){
return true;
}

// are we on the Mac? Safari needs Flash version 8 to do Flash 8
// communication, while Firefox/Mac needs Flash 8 to fix bugs it has
// with Flash 6 communication
if(dojo.render.os.mac == true && !dojo.flash.info.isVersionOrAbove(8, 0, 0)){
return true;
}

// other platforms need at least Flash 6 or above
if(!dojo.flash.info.isVersionOrAbove(6, 0, 0)){
return true;
}

// otherwise we don't need installation
return false;
},


install: function(){
//dojo.debug("install");
// indicate that we are installing
dojo.flash.info.installing = true;
dojo.flash.installing();

if(dojo.flash.info.capable == false){ // we have no Flash at all
//dojo.debug("Completely new install");
// write out a simple Flash object to force the browser to prompt
// the user to install things
var installObj = new dojo.flash.Embed(false);
installObj.write(8); // write out HTML for Flash 8 version+
}else if(dojo.flash.info.isVersionOrAbove(6, 0, 65)){ // Express Install
//dojo.debug("Express install");
var installObj = new dojo.flash.Embed(false);
installObj.write(8, true); // write out HTML for Flash 8 version+
installObj.setVisible(true);
installObj.center();
}else{ // older Flash install than version 6r65
alert("This content requires a more recent version of the Macromedia "
+" Flash Player.");
window.location.href = + dojo.flash.Embed.protocol() +
"://www.macromedia.com/go/getflashplayer";
}
},


_onInstallStatus: function(msg){
if (msg == "Download.Complete"){
// Installation is complete.
dojo.flash._initialize();
}else if(msg == "Download.Cancelled"){
alert("This content requires a more recent version of the Macromedia "
+" Flash Player.");
window.location.href = dojo.flash.Embed.protocol() +
"://www.macromedia.com/go/getflashplayer";
}else if (msg == "Download.Failed"){
// The end user failed to download the installer due to a network failure
alert("There was an error downloading the Flash Player update. "
+ "Please try again later, or visit macromedia.com to download "
+ "the latest version of the Flash plugin.");
}
}
}


dojo.flash.info = new dojo.flash.Info();


