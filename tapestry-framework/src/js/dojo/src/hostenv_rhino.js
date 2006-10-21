





dojo.hostenv.println=function(line){
print(line);
}

dojo.locale = dojo.locale || java.util.Locale.getDefault().toString().replace('_','-').toLowerCase();
dojo.render.name = dojo.hostenv.name_ = 'rhino';
dojo.hostenv.getVersion = function() {return version();};

if (dj_undef("byId")) {
dojo.byId = function(id, doc){
if(id && (typeof id == "string" || id instanceof String)){
if(!doc){ doc = document; }
return doc.getElementById(id);
}
return id; // assume it's a node
}
}


dojo.hostenv.loadUri = function(uri, cb){
try{
var local = (new java.io.File(uri)).exists();
if(!local){
try{
// try it as a file first, URL second
var stream = (new java.net.URL(uri)).openStream();
// close the stream so we don't leak resources
stream.close();
}catch(e){
// no debug output; this failure just means the uri was not found.
return false;
}
}

if(cb){
var contents = (local ? readText : readUri)(uri, "UTF-8");
cb(eval('('+contents+')'));
}else{
load(uri);
}
return true;
}catch(e){
dojo.debug("rhino load('" + uri + "') failed. Exception: " + e);
return false;
}
}

dojo.hostenv.exit = function(exitcode){
quit(exitcode);
}

















































function dj_rhino_current_script_via_java(depth) {
var optLevel = Packages.org.mozilla.javascript.Context.getCurrentContext().getOptimizationLevel();
// if (optLevel == -1){ dojo.unimplemented("getCurrentScriptURI (determine current script path for rhino when interpreter mode)", ''); }
var caw = new java.io.CharArrayWriter();
var pw = new java.io.PrintWriter(caw);
var exc = new java.lang.Exception();
var s = caw.toString();
// we have to exclude the ones with or without line numbers because they put double entries in:
//   at org.mozilla.javascript.gen.c3._c4(/Users/mda/Sites/burstproject/burst/Runtime.js:56)
//   at org.mozilla.javascript.gen.c3.call(/Users/mda/Sites/burstproject/burst/Runtime.js)
var matches = s.match(/[^\(]*\.js\)/gi);
if(!matches){
throw Error("cannot parse printStackTrace output: " + s);
}

// matches[0] is entire string, matches[1] is this function, matches[2] is caller, ...
var fname = ((typeof depth != 'undefined')&&(depth)) ? matches[depth + 1] : matches[matches.length - 1];
var fname = matches[3];
if(!fname){ fname = matches[1]; }
// print("got fname '" + fname + "' from stack string '" + s + "'");
if (!fname){ throw Error("could not find js file in printStackTrace output: " + s); }
//print("Rhino getCurrentScriptURI returning '" + fname + "' from: " + s);
return fname;
}






function readText(path, encoding){
encoding = encoding || "utf-8";


var jf = new java.io.File(path);
var is = new java.io.FileInputStream(jf);
return dj_readInputStream(is, encoding);
}

function readUri(uri, encoding){
var conn = (new java.net.URL(uri)).openConnection();
encoding = encoding || conn.getContentEncoding() || "utf-8";
var is = conn.getInputStream();
return dj_readInputStream(is, encoding);
}

function dj_readInputStream(is, encoding){
var input = new java.io.BufferedReader(new java.io.InputStreamReader(is, encoding));
try {
var sb = new java.lang.StringBuffer();
var line = "";
while((line = input.readLine()) !== null){
sb.append(line);
sb.append(java.lang.System.getProperty("line.separator"));
}
return sb.toString();
} finally {
input.close();
}
}


if(!djConfig.libraryScriptUri.length){
try{
djConfig.libraryScriptUri = dj_rhino_current_script_via_java(1);
}catch(e){
// otherwise just fake it
if(djConfig["isDebug"]){
print("\n");
print("we have no idea where Dojo is located.");
print("Please try loading rhino in a non-interpreted mode or set a");
print("\n\tdjConfig.libraryScriptUri\n");
print("Setting the dojo path to './'");
print("This is probably wrong!");
print("\n");
print("Dojo will try to load anyway");
}
djConfig.libraryScriptUri = "./";
}
}

dojo.doc = function(){


return document;
}

dojo.body = function(){
return document.body;
}

function setTimeout(func, delay){


var def={
sleepTime:delay,
hasSlept:false,

run:function(){
if (!this.hasSlept){
this.hasSlept=true;
java.lang.Thread.currentThread().sleep(this.sleepTime);
}
try {
func();
} catch(e){dojo.debug("Error running setTimeout thread:" + e);}
}
};

var runnable=new java.lang.Runnable(def);
var thread=new java.lang.Thread(runnable);
thread.start();
}
