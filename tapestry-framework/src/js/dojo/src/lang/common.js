

dojo.provide("dojo.lang.common");

dojo.lang.inherits = function( subclass,  superclass){

if(typeof superclass != 'function'){
dojo.raise("dojo.inherits: superclass argument ["+superclass+"] must be a function (subclass: ["+subclass+"']");
}
subclass.prototype = new superclass();
subclass.prototype.constructor = subclass;
subclass.superclass = superclass.prototype;

subclass['super'] = superclass.prototype;
}

dojo.lang._mixin = function( obj,  props){

var tobj = {};
for(var x in props){
// the "tobj" condition avoid copying properties in "props"
// inherited from Object.prototype.  For example, if obj has a custom
// toString() method, don't overwrite it with the toString() method
// that props inherited from Object.protoype
if((typeof tobj[x] == "undefined") || (tobj[x] != props[x])){
obj[x] = props[x];
}
}

if(dojo.render.html.ie
&& (typeof(props["toString"]) == "function")
&& (props["toString"] != obj["toString"])
&& (props["toString"] != tobj["toString"]))
{
obj.toString = props.toString;
}
return obj; // Object
}

dojo.lang.mixin = function( obj, props){

for(var i=1, l=arguments.length; i<l; i++){
dojo.lang._mixin(obj, arguments[i]);
}
return obj; // Object
}

dojo.lang.extend = function( constructor,  props){


for(var i=1, l=arguments.length; i<l; i++){
dojo.lang._mixin(constructor.prototype, arguments[i]);
}
return constructor; // Object
}


dojo.inherits = dojo.lang.inherits;

dojo.mixin = dojo.lang.mixin;
dojo.extend = dojo.lang.extend;

dojo.lang.find = function(			array,
value,
identity,
findLast){







//  find(value, array[, identity [findLast]]) // deprecated


if(!dojo.lang.isArrayLike(array) && dojo.lang.isArrayLike(value)) {
dojo.deprecated('dojo.lang.find(value, array)', 'use dojo.lang.find(array, value) instead', "0.5");
var temp = array;
array = value;
value = temp;
}
var isString = dojo.lang.isString(array);
if(isString) { array = array.split(""); }

if(findLast) {
var step = -1;
var i = array.length - 1;
var end = -1;
} else {
var step = 1;
var i = 0;
var end = array.length;
}
if(identity){
while(i != end) {
if(array[i] === value){ return i; }
i += step;
}
}else{
while(i != end) {
if(array[i] == value){ return i; }
i += step;
}
}
return -1;	// number
}

dojo.lang.indexOf = dojo.lang.find;

dojo.lang.findLast = function( array,  value,  identity){


return dojo.lang.find(array, value, identity, true); // number
}

dojo.lang.lastIndexOf = dojo.lang.findLast;

dojo.lang.inArray = function(array , value ){

return dojo.lang.find(array, value) > -1; // boolean
}



dojo.lang.isObject = function( it){

if(typeof it == "undefined"){ return false; }
return (typeof it == "object" || it === null || dojo.lang.isArray(it) || dojo.lang.isFunction(it)); // Boolean
}

dojo.lang.isArray = function( it){

return (it && it instanceof Array || typeof it == "array"); // Boolean
}

dojo.lang.isArrayLike = function( it){

if((!it)||(dojo.lang.isUndefined(it))){ return false; }
if(dojo.lang.isString(it)){ return false; }
if(dojo.lang.isFunction(it)){ return false; } // keeps out built-in constructors (Number, String, ...) which have length properties
if(dojo.lang.isArray(it)){ return true; }

if((it.tagName)&&(it.tagName.toLowerCase()=='form')){ return false; }
if(dojo.lang.isNumber(it.length) && isFinite(it.length)){ return true; }
return false; // Boolean
}

dojo.lang.isFunction = function( it){

if(!it){ return false; }

if((typeof(it) == "function") && (it == "[object NodeList]")) { return false; }
return (it instanceof Function || typeof it == "function"); // Boolean
}

dojo.lang.isString = function( it){

return (typeof it == "string" || it instanceof String);
}

dojo.lang.isAlien = function( it){

if(!it){ return false; }
return !dojo.lang.isFunction() && /\{\s*\[native code\]\s*\}/.test(String(it)); // Boolean
}

dojo.lang.isBoolean = function( it){

return (it instanceof Boolean || typeof it == "boolean"); // Boolean
}


dojo.lang.isNumber = function( it){













return (it instanceof Number || typeof it == "number"); // Boolean
}


dojo.lang.isUndefined = function( it){











return ((typeof(it) == "undefined")&&(it == undefined)); // Boolean
}


