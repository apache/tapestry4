

dojo.hostenv.loadedUris.push("../src/bootstrap1.js");
dojo.hostenv.loadedUris.push("../src/loader.js");
dojo.hostenv.loadedUris.push("../src/hostenv_browser.js");
dojo.hostenv.loadedUris.push("../src/bootstrap2.js");
dojo.hostenv._loadedUrisListStart = dojo.hostenv.loadedUris.length;

function removeComments(contents){
contents = new String((!contents) ? "" : contents);



dojo.clobberLastObject = function(objpath){
if(objpath.indexOf('.') == -1){
if(!dj_undef(objpath, dj_global)){
delete dj_global[objpath];
}
return true;
}

var syms = objpath.split(/\./);
var base = dojo.evalObjPath(syms.slice(0, -1).join("."), false);
var child = syms[syms.length-1];
if(!dj_undef(child, base)){
// alert(objpath);
delete base[child];
return true;
}
return false;
}

var removals = [];

function zip(arr){
var ret = [];
var seen = {};
for(var x=0; x<arr.length; x++){
if(!seen[arr[x]]){
ret.push(arr[x]);
seen[arr[x]] = true;
}
}
return ret;
}


var old_dj_eval = dj_eval;
dj_eval = function(){ return true; }
dojo.hostenv.oldLoadUri = dojo.hostenv.loadUri;
dojo.hostenv.loadUri = function(uri, cb ){
if(dojo.hostenv.loadedUris[uri]){
return true; // fixes endless recursion opera trac 471
}
try{
var text = this.getText(uri, null, true);
if(!text) { return false; }
if(cb){
// No way to load i18n bundles but to eval them, and they usually
// don't have script needing to be debugged anyway
var expr = old_dj_eval('('+text+')');
cb(expr);
}else {
var requires = dojo.hostenv.getRequiresAndProvides(text);
eval(requires.join(";"));
dojo.hostenv.loadedUris.push(uri);
dojo.hostenv.loadedUris[uri] = true;
var delayRequires = dojo.hostenv.getDelayRequiresAndProvides(text);
eval(delayRequires.join(";"));
}
}catch(e){
alert(e);
}
return true;
}

dojo.hostenv._writtenIncludes = {};
dojo.hostenv.writeIncludes = function(willCallAgain){
for(var x=removals.length-1; x>=0; x--){
dojo.clobberLastObject(removals[x]);
}
var depList = [];
var seen = dojo.hostenv._writtenIncludes;
for(var x=0; x<dojo.hostenv.loadedUris.length; x++){
var curi = dojo.hostenv.loadedUris[x];
// dojo.debug(curi);
if(!seen[curi]){
seen[curi] = true;
depList.push(curi);
}
}

dojo.hostenv._global_omit_module_check = true;

for(var x= dojo.hostenv._loadedUrisListStart; x<depList.length; x++){
document.write("<script type='text/javascript' src='"+depList[x]+"'></script>");
}
document.write("<script type='text/javascript'>dojo.hostenv._global_omit_module_check = false;</script>");
dojo.hostenv._loadedUrisListStart = 0;
if (!willCallAgain) {
// turn off debugAtAllCosts, so that dojo.require() calls inside of ContentPane hrefs
// work correctly
dj_eval = old_dj_eval;
dojo.hostenv.loadUri = dojo.hostenv.oldLoadUri;
}
}
