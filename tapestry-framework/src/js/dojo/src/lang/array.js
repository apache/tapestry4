

dojo.provide("dojo.lang.array");

dojo.require("dojo.lang.common");



dojo.lang.has = function(obj, name){
try{
return typeof obj[name] != "undefined";
}catch(e){ return false; }
}

dojo.lang.isEmpty = function(obj){
if(dojo.lang.isObject(obj)){
var tmp = {};
var count = 0;
for(var x in obj){
if(obj[x] && (!tmp[x])){
count++;
break;
}
}
return count == 0;
}else if(dojo.lang.isArrayLike(obj) || dojo.lang.isString(obj)){
return obj.length == 0;
}
}

dojo.lang.map = function(arr, obj, unary_func){
var isString = dojo.lang.isString(arr);
if(isString){
// arr: String
arr = arr.split("");
}
if(dojo.lang.isFunction(obj)&&(!unary_func)){
unary_func = obj;
obj = dj_global;
}else if(dojo.lang.isFunction(obj) && unary_func){
// ff 1.5 compat
var tmpObj = obj;
obj = unary_func;
unary_func = tmpObj;
}
if(Array.map){
var outArr = Array.map(arr, unary_func, obj);
}else{
var outArr = [];
for(var i=0;i<arr.length;++i){
outArr.push(unary_func.call(obj, arr[i]));
}
}
if(isString) {
return outArr.join(""); // String
} else {
return outArr; // Array
}
}

dojo.lang.reduce = function(arr, initialValue, obj, binary_func){
var reducedValue = initialValue;
var ob = obj ? obj : dj_global;
dojo.lang.map(arr,
function(val){
reducedValue = binary_func.call(ob, reducedValue, val);
}
);
return reducedValue;
}


dojo.lang.forEach = function(anArray, callback, thisObject){
if(dojo.lang.isString(anArray)){
// anArray: String
anArray = anArray.split("");
}
if(Array.forEach){
Array.forEach(anArray, callback, thisObject);
}else{
// FIXME: there are several ways of handilng thisObject. Is dj_global always the default context?
if(!thisObject){
thisObject=dj_global;
}
for(var i=0,l=anArray.length; i<l; i++){
callback.call(thisObject, anArray[i], i, anArray);
}
}
}

dojo.lang._everyOrSome = function(every, arr, callback, thisObject){
if(dojo.lang.isString(arr)){
//arr: String
arr = arr.split("");
}
if(Array.every){
return Array[ every ? "every" : "some" ](arr, callback, thisObject);
}else{
if(!thisObject){
thisObject = dj_global;
}
for(var i=0,l=arr.length; i<l; i++){
var result = callback.call(thisObject, arr[i], i, arr);
if(every && !result){
return false; // Boolean
}else if((!every)&&(result)){
return true; // Boolean
}
}
return Boolean(every); // Boolean
}
}

dojo.lang.every = function(arr, callback, thisObject){
return this._everyOrSome(true, arr, callback, thisObject); // Boolean
}

dojo.lang.some = function(arr, callback, thisObject){
return this._everyOrSome(false, arr, callback, thisObject); // Boolean
}

dojo.lang.filter = function(arr, callback, thisObject){
var isString = dojo.lang.isString(arr);
if(isString){ arr = arr.split(""); }
var outArr;
if(Array.filter){
outArr = Array.filter(arr, callback, thisObject);
} else {
if(!thisObject){
if(arguments.length >= 3){ dojo.raise("thisObject doesn't exist!"); }
thisObject = dj_global;
}

outArr = [];
for(var i = 0; i < arr.length; i++){
if(callback.call(thisObject, arr[i], i, arr)){
outArr.push(arr[i]);
}
}
}
if(isString){
return outArr.join(""); // String
} else {
return outArr; // Array
}
}

dojo.lang.unnest = function(){








var out = [];
for(var i = 0; i < arguments.length; i++){
if(dojo.lang.isArrayLike(arguments[i])){
var add = dojo.lang.unnest.apply(this, arguments[i]);
out = out.concat(add);
}else{
out.push(arguments[i]);
}
}
return out; // Array
}

dojo.lang.toArray = function(arrayLike, startOffset){



var array = [];
for(var i = startOffset||0; i < arrayLike.length; i++){
array.push(arrayLike[i]);
}
return array; // Array
}
