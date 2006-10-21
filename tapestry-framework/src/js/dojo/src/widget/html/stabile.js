



dojo.provide("dojo.widget.html.stabile");

dojo.widget.html.stabile = {

_sqQuotables: new RegExp("([\\\\'])", "g"),


_depth: 0,



_recur: false,



depthLimit: 2
};










dojo.widget.html.stabile.getState = function(id){
dojo.widget.html.stabile.setup();
return dojo.widget.html.stabile.widgetState[id];
}





dojo.widget.html.stabile.setState = function(id, state, isCommit){
dojo.widget.html.stabile.setup();
dojo.widget.html.stabile.widgetState[id] = state;
if(isCommit){
dojo.widget.html.stabile.commit(dojo.widget.html.stabile.widgetState);
}
}






dojo.widget.html.stabile.setup = function(){
if(!dojo.widget.html.stabile.widgetState){
var text = dojo.widget.html.stabile.getStorage().value;
dojo.widget.html.stabile.widgetState = text ? dj_eval("("+text+")") : {};
}
}





dojo.widget.html.stabile.commit = function(state){
dojo.widget.html.stabile.getStorage().value = dojo.widget.html.stabile.description(state);
}







dojo.widget.html.stabile.description = function(v, showAll){

var depth = dojo.widget.html.stabile._depth;

var describeThis = function() {
return this.description(this, true);
}

try {

if(v===void(0)){
return "undefined";
}
if(v===null){
return "null";
}
if(typeof(v)=="boolean" || typeof(v)=="number"
|| v instanceof Boolean || v instanceof Number){
return v.toString();
}

if(typeof(v)=="string" || v instanceof String){
// Quote strings and their contents as required.
// Replacing by $& fails in IE 5.0
var v1 = v.replace(dojo.widget.html.stabile._sqQuotables, "\\$1");
v1 = v1.replace(/\n/g, "\\n");
v1 = v1.replace(/\r/g, "\\r");
// Any other important special cases?
return "'"+v1+"'";
}

if(v instanceof Date){
// Create a data constructor.
return "new Date("+d.getFullYear+","+d.getMonth()+","+d.getDate()+")";
}

var d;
if(v instanceof Array || v.push){
// "push" test needed for KHTML/Safari, don't know why -cp

if(depth>=dojo.widget.html.stabile.depthLimit)
return "[ ... ]";

d = "[";
var first = true;
dojo.widget.html.stabile._depth++;
for(var i=0; i<v.length; i++){
// Skip functions and undefined values
// if(v[i]==undef || typeof(v[i])=="function")
//   continue;
if(first){
first = false;
}else{
d += ",";
}
d+=arguments.callee(v[i], showAll);
}
return d+"]";
}

if(v.constructor==Object
|| v.toString==describeThis){
if(depth>=dojo.widget.html.stabile.depthLimit)
return "{ ... }";

// Instanceof Hash is good, or if we just use Objects,
// we can say v.constructor==Object.
// IE (5?) lacks hasOwnProperty, but perhaps objects do not always
// have prototypes??
if(typeof(v.hasOwnProperty)!="function" && v.prototype){
throw new Error("description: "+v+" not supported by script engine");
}
var first = true;
d = "{";
dojo.widget.html.stabile._depth++;
for(var key in v){
// Skip values that are functions or undefined.
if(v[key]==void(0) || typeof(v[key])=="function")
continue;
if(first){
first = false;
}else{
d += ", ";
}
var kd = key;
// If the key is not a legal identifier, use its description.
// For strings this will quote the stirng.
if(!kd.match(/^[a-zA-Z_][a-zA-Z0-9_]*$/)){
kd = arguments.callee(key, showAll);
}
d += kd+": "+arguments.callee(v[key], showAll);
}
return d+"}";
}

if(showAll){
if(dojo.widget.html.stabile._recur){
// Save the original definitions of toString;
var objectToString = Object.prototype.toString;
return objectToString.apply(v, []);
}else{
dojo.widget.html.stabile._recur = true;
return v.toString();
}
}else{
// log("Description? "+v.toString()+", "+typeof(v));
throw new Error("Unknown type: "+v);
return "'unknown'";
}

} finally {
// Always restore the global current depth.
dojo.widget.html.stabile._depth = depth;
}

}







dojo.widget.html.stabile.getStorage = function(){
if (dojo.widget.html.stabile.dataField) {
return dojo.widget.html.stabile.dataField;
}
var form = document.forms._dojo_form;
return dojo.widget.html.stabile.dataField = form ? form.stabile : {value: ""};
}

