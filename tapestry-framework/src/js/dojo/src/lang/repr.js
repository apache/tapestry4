

dojo.provide("dojo.lang.repr");

dojo.require("dojo.lang.common");
dojo.require("dojo.AdapterRegistry");
dojo.require("dojo.string.extras");

dojo.lang.reprRegistry = new dojo.AdapterRegistry();
dojo.lang.registerRepr = function(name, check, wrap, override){








dojo.lang.reprRegistry.register(name, check, wrap, override);
};

dojo.lang.repr = function(obj){



if(typeof(obj) == "undefined"){
// obj: undefined
return "undefined"; // String
}else if(obj === null){
// obj: null
return "null"; // String
}

try{
if(typeof(obj["__repr__"]) == 'function'){
return obj["__repr__"]();
}else if((typeof(obj["repr"]) == 'function')&&(obj.repr != arguments.callee)){
return obj["repr"]();
}
return dojo.lang.reprRegistry.match(obj);
}catch(e){
if(typeof(obj.NAME) == 'string' && (
obj.toString == Function.prototype.toString ||
obj.toString == Object.prototype.toString
)){
return obj.NAME; // String
}
}

if(typeof(obj) == "function"){
// obj: Function
obj = (obj + "").replace(/^\s+/, "");
var idx = obj.indexOf("{");
if(idx != -1){
obj = obj.substr(0, idx) + "{...}";
}
}
return obj + ""; // String
}

dojo.lang.reprArrayLike = function(arr){


try{
var na = dojo.lang.map(arr, dojo.lang.repr);
return "[" + na.join(", ") + "]"; // String
}catch(e){ }
};

(function(){
var m = dojo.lang;
m.registerRepr("arrayLike", m.isArrayLike, m.reprArrayLike);
m.registerRepr("string", m.isString, m.reprString);
m.registerRepr("numbers", m.isNumber, m.reprNumber);
m.registerRepr("boolean", m.isBoolean, m.reprNumber);

})();
