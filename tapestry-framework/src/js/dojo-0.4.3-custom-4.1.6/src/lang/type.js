dojo.provide("dojo.lang.type");
dojo.require("dojo.lang.common");
dojo.lang.whatAmI=function(_1){
dojo.deprecated("dojo.lang.whatAmI","use dojo.lang.getType instead","0.5");
return dojo.lang.getType(_1);
};
dojo.lang.whatAmI.custom={};
dojo.lang.getType=function(_2){
try{
if(dojo.lang.isArray(_2)){
return "array";
}
if(dojo.lang.isFunction(_2)){
return "function";
}
if(dojo.lang.isString(_2)){
return "string";
}
if(dojo.lang.isNumber(_2)){
return "number";
}
if(dojo.lang.isBoolean(_2)){
return "boolean";
}
if(dojo.lang.isAlien(_2)){
return "alien";
}
if(dojo.lang.isUndefined(_2)){
return "undefined";
}
for(var _3 in dojo.lang.whatAmI.custom){
if(dojo.lang.whatAmI.custom[_3](_2)){
return _3;
}
}
if(dojo.lang.isObject(_2)){
return "object";
}
}
catch(e){
}
return "unknown";
};
dojo.lang.isNumeric=function(_4){
return (!isNaN(_4)&&isFinite(_4)&&(_4!=null)&&!dojo.lang.isBoolean(_4)&&!dojo.lang.isArray(_4)&&!/^\s*$/.test(_4));
};
dojo.lang.isBuiltIn=function(_5){
return (dojo.lang.isArray(_5)||dojo.lang.isFunction(_5)||dojo.lang.isString(_5)||dojo.lang.isNumber(_5)||dojo.lang.isBoolean(_5)||(_5==null)||(_5 instanceof Error)||(typeof _5=="error"));
};
dojo.lang.isPureObject=function(_6){
return ((_6!=null)&&dojo.lang.isObject(_6)&&_6.constructor==Object);
};
dojo.lang.isOfType=function(_7,_8,_9){
var _a=false;
if(_9){
_a=_9["optional"];
}
if(_a&&((_7===null)||dojo.lang.isUndefined(_7))){
return true;
}
if(dojo.lang.isArray(_8)){
var _b=_8;
for(var i in _b){
var _d=_b[i];
if(dojo.lang.isOfType(_7,_d)){
return true;
}
}
return false;
}else{
if(dojo.lang.isString(_8)){
_8=_8.toLowerCase();
}
switch(_8){
case Array:
case "array":
return dojo.lang.isArray(_7);
case Function:
case "function":
return dojo.lang.isFunction(_7);
case String:
case "string":
return dojo.lang.isString(_7);
case Number:
case "number":
return dojo.lang.isNumber(_7);
case "numeric":
return dojo.lang.isNumeric(_7);
case Boolean:
case "boolean":
return dojo.lang.isBoolean(_7);
case Object:
case "object":
return dojo.lang.isObject(_7);
case "pureobject":
return dojo.lang.isPureObject(_7);
case "builtin":
return dojo.lang.isBuiltIn(_7);
case "alien":
return dojo.lang.isAlien(_7);
case "undefined":
return dojo.lang.isUndefined(_7);
case null:
case "null":
return (_7===null);
case "optional":
dojo.deprecated("dojo.lang.isOfType(value, [type, \"optional\"])","use dojo.lang.isOfType(value, type, {optional: true} ) instead","0.5");
return ((_7===null)||dojo.lang.isUndefined(_7));
default:
if(dojo.lang.isFunction(_8)){
return (_7 instanceof _8);
}else{
dojo.raise("dojo.lang.isOfType() was passed an invalid type");
}
}
}
dojo.raise("If we get here, it means a bug was introduced above.");
};
dojo.lang.getObject=function(_e){
var _f=_e.split("."),i=0,obj=dj_global;
do{
obj=obj[_f[i++]];
}while(i<_f.length&&obj);
return (obj!=dj_global)?obj:null;
};
dojo.lang.doesObjectExist=function(str){
var _13=str.split("."),i=0,obj=dj_global;
do{
obj=obj[_13[i++]];
}while(i<_13.length&&obj);
return (obj&&obj!=dj_global);
};
