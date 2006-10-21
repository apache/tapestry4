

dojo.provide("dojo.lang.type");
dojo.require("dojo.lang.common");

dojo.lang.whatAmI = function(value) {
dojo.deprecated("dojo.lang.whatAmI", "use dojo.lang.getType instead", "0.5");
return dojo.lang.getType(value);
}
dojo.lang.whatAmI.custom = {};

dojo.lang.getType = function( value){

try {
if(dojo.lang.isArray(value)) {
return "array";	//	string
}
if(dojo.lang.isFunction(value)) {
return "function";	//	string
}
if(dojo.lang.isString(value)) {
return "string";	//	string
}
if(dojo.lang.isNumber(value)) {
return "number";	//	string
}
if(dojo.lang.isBoolean(value)) {
return "boolean";	//	string
}
if(dojo.lang.isAlien(value)) {
return "alien";	//	string
}
if(dojo.lang.isUndefined(value)) {
return "undefined";	//	string
}
// FIXME: should this go first?
for(var name in dojo.lang.whatAmI.custom) {
if(dojo.lang.whatAmI.custom[name](value)) {
return name;	//	string
}
}
if(dojo.lang.isObject(value)) {
return "object";	//	string
}
} catch(e) {}
return "unknown";	//	string
}

dojo.lang.isNumeric = function( value){



return (!isNaN(value)
&& isFinite(value)
&& (value != null)
&& !dojo.lang.isBoolean(value)
&& !dojo.lang.isArray(value)
&& !/^\s*$/.test(value)
);	//	boolean
}

dojo.lang.isBuiltIn = function( value){

return (dojo.lang.isArray(value)
|| dojo.lang.isFunction(value)
|| dojo.lang.isString(value)
|| dojo.lang.isNumber(value)
|| dojo.lang.isBoolean(value)
|| (value == null)
|| (value instanceof Error)
|| (typeof value == "error")
);	//	boolean
}

dojo.lang.isPureObject = function( value){



return ((value != null)
&& dojo.lang.isObject(value)
&& value.constructor == Object
);	//	boolean
}

dojo.lang.isOfType = function( value,  type,  keywordParameters) {



var optional = false;
if (keywordParameters) {
optional = keywordParameters["optional"];
}
if (optional && ((value === null) || dojo.lang.isUndefined(value))) {
return true;	//	boolean
}
if(dojo.lang.isArray(type)){
var arrayOfTypes = type;
for(var i in arrayOfTypes){
var aType = arrayOfTypes[i];
if(dojo.lang.isOfType(value, aType)) {
return true; 	//	boolean
}
}
return false;	//	boolean
}else{
if(dojo.lang.isString(type)){
type = type.toLowerCase();
}
switch (type) {
case Array:
case "array":
return dojo.lang.isArray(value);	//	boolean
case Function:
case "function":
return dojo.lang.isFunction(value);	//	boolean
case String:
case "string":
return dojo.lang.isString(value);	//	boolean
case Number:
case "number":
return dojo.lang.isNumber(value);	//	boolean
case "numeric":
return dojo.lang.isNumeric(value);	//	boolean
case Boolean:
case "boolean":
return dojo.lang.isBoolean(value);	//	boolean
case Object:
case "object":
return dojo.lang.isObject(value);	//	boolean
case "pureobject":
return dojo.lang.isPureObject(value);	//	boolean
case "builtin":
return dojo.lang.isBuiltIn(value);	//	boolean
case "alien":
return dojo.lang.isAlien(value);	//	boolean
case "undefined":
return dojo.lang.isUndefined(value);	//	boolean
case null:
case "null":
return (value === null);	//	boolean
case "optional":
dojo.deprecated('dojo.lang.isOfType(value, [type, "optional"])', 'use dojo.lang.isOfType(value, type, {optional: true} ) instead', "0.5");
return ((value === null) || dojo.lang.isUndefined(value));	//	boolean
default:
if (dojo.lang.isFunction(type)) {
return (value instanceof type);	//	boolean
} else {
dojo.raise("dojo.lang.isOfType() was passed an invalid type");
}
}
}
dojo.raise("If we get here, it means a bug was introduced above.");
}

dojo.lang.getObject=function( str){


var parts=str.split("."), i=0, obj=dj_global;
do{
obj=obj[parts[i++]];
}while(i<parts.length&&obj);
return (obj!=dj_global)?obj:null;	//	Object
}

dojo.lang.doesObjectExist=function( str){


var parts=str.split("."), i=0, obj=dj_global;
do{
obj=obj[parts[i++]];
}while(i<parts.length&&obj);
return (obj&&obj!=dj_global);	//	boolean
}
