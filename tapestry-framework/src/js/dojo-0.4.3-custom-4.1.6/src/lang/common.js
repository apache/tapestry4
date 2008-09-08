dojo.provide("dojo.lang.common");
dojo.lang.inherits=function(_1,_2){
if(!dojo.lang.isFunction(_2)){
dojo.raise("dojo.inherits: superclass argument ["+_2+"] must be a function (subclass: ["+_1+"']");
}
_1.prototype=new _2();
_1.prototype.constructor=_1;
_1.superclass=_2.prototype;
_1["super"]=_2.prototype;
};
dojo.lang._mixin=function(_3,_4){
var _5={};
for(var x in _4){
if((typeof _5[x]=="undefined")||(_5[x]!=_4[x])){
_3[x]=_4[x];
}
}
if(dojo.render.html.ie&&(typeof (_4["toString"])=="function")&&(_4["toString"]!=_3["toString"])&&(_4["toString"]!=_5["toString"])){
_3.toString=_4.toString;
}
return _3;
};
dojo.lang.mixin=function(_7,_8){
for(var i=1,l=arguments.length;i<l;i++){
dojo.lang._mixin(_7,arguments[i]);
}
return _7;
};
dojo.lang.extend=function(_b,_c){
for(var i=1,l=arguments.length;i<l;i++){
dojo.lang._mixin(_b.prototype,arguments[i]);
}
return _b;
};
dojo.inherits=dojo.lang.inherits;
dojo.mixin=dojo.lang.mixin;
dojo.extend=dojo.lang.extend;
dojo.lang.find=function(_f,_10,_11,_12){
if(!dojo.lang.isArrayLike(_f)&&dojo.lang.isArrayLike(_10)){
dojo.deprecated("dojo.lang.find(value, array)","use dojo.lang.find(array, value) instead","0.5");
var _13=_f;
_f=_10;
_10=_13;
}
var _14=dojo.lang.isString(_f);
if(_14){
_f=_f.split("");
}
if(_12){
var _15=-1;
var i=_f.length-1;
var end=-1;
}else{
var _15=1;
var i=0;
var end=_f.length;
}
if(_11){
while(i!=end){
if(_f[i]===_10){
return i;
}
i+=_15;
}
}else{
while(i!=end){
if(_f[i]==_10){
return i;
}
i+=_15;
}
}
return -1;
};
dojo.lang.indexOf=dojo.lang.find;
dojo.lang.findLast=function(_18,_19,_1a){
return dojo.lang.find(_18,_19,_1a,true);
};
dojo.lang.lastIndexOf=dojo.lang.findLast;
dojo.lang.inArray=function(_1b,_1c){
return dojo.lang.find(_1b,_1c)>-1;
};
dojo.lang.isObject=function(it){
if(typeof it=="undefined"){
return false;
}
return (typeof it=="object"||it===null||dojo.lang.isArray(it)||dojo.lang.isFunction(it));
};
dojo.lang.isArray=function(it){
return (it&&it instanceof Array||typeof it=="array");
};
dojo.lang.isArrayLike=function(it){
if((!it)||(dojo.lang.isUndefined(it))){
return false;
}
if(dojo.lang.isString(it)){
return false;
}
if(dojo.lang.isFunction(it)){
return false;
}
if(dojo.lang.isArray(it)){
return true;
}
if((it.tagName)&&(it.tagName.toLowerCase()=="form")){
return false;
}
if(dojo.lang.isNumber(it.length)&&isFinite(it.length)){
return true;
}
return false;
};
dojo.lang.isFunction=function(it){
return (it instanceof Function||typeof it=="function");
};
(function(){
if((dojo.render.html.capable)&&(dojo.render.html["safari"])){
dojo.lang.isFunction=function(it){
if((typeof (it)=="function")&&(it=="[object NodeList]")){
return false;
}
return (it instanceof Function||typeof it=="function");
};
}
})();
dojo.lang.isString=function(it){
return (typeof it=="string"||it instanceof String);
};
dojo.lang.isAlien=function(it){
if(!it){
return false;
}
return !dojo.lang.isFunction(it)&&/\{\s*\[native code\]\s*\}/.test(String(it));
};
dojo.lang.isBoolean=function(it){
return (it instanceof Boolean||typeof it=="boolean");
};
dojo.lang.isNumber=function(it){
return (it instanceof Number||typeof it=="number");
};
dojo.lang.isUndefined=function(it){
return ((typeof (it)=="undefined")&&(it==undefined));
};
