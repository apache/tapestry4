dojo.provide("dojo.lang.repr");
dojo.require("dojo.lang.common");
dojo.require("dojo.AdapterRegistry");
dojo.require("dojo.string.extras");
dojo.lang.reprRegistry=new dojo.AdapterRegistry();
dojo.lang.registerRepr=function(_1,_2,_3,_4){
dojo.lang.reprRegistry.register(_1,_2,_3,_4);
};
dojo.lang.repr=function(_5){
if(typeof (_5)=="undefined"){
return "undefined";
}else{
if(_5===null){
return "null";
}
}
try{
if(typeof (_5["__repr__"])=="function"){
return _5["__repr__"]();
}else{
if((typeof (_5["repr"])=="function")&&(_5.repr!=arguments.callee)){
return _5["repr"]();
}
}
return dojo.lang.reprRegistry.match(_5);
}
catch(e){
if(typeof (_5.NAME)=="string"&&(_5.toString==Function.prototype.toString||_5.toString==Object.prototype.toString)){
return _5.NAME;
}
}
if(typeof (_5)=="function"){
_5=(_5+"").replace(/^\s+/,"");
var _6=_5.indexOf("{");
if(_6!=-1){
_5=_5.substr(0,_6)+"{...}";
}
}
return _5+"";
};
dojo.lang.reprArrayLike=function(_7){
try{
var na=dojo.lang.map(_7,dojo.lang.repr);
return "["+na.join(", ")+"]";
}
catch(e){
}
};
(function(){
var m=dojo.lang;
m.registerRepr("arrayLike",m.isArrayLike,m.reprArrayLike);
m.registerRepr("string",m.isString,m.reprString);
m.registerRepr("numbers",m.isNumber,m.reprNumber);
m.registerRepr("boolean",m.isBoolean,m.reprNumber);
})();
