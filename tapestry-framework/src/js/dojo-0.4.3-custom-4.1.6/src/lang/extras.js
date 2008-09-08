dojo.provide("dojo.lang.extras");
dojo.require("dojo.lang.common");
dojo.lang.setTimeout=function(_1,_2){
var _3=window,_4=2;
if(!dojo.lang.isFunction(_1)){
_3=_1;
_1=_2;
_2=arguments[2];
_4++;
}
if(dojo.lang.isString(_1)){
_1=_3[_1];
}
var _5=[];
for(var i=_4;i<arguments.length;i++){
_5.push(arguments[i]);
}
return dojo.global().setTimeout(function(){
_1.apply(_3,_5);
},_2);
};
dojo.lang.clearTimeout=function(_7){
dojo.global().clearTimeout(_7);
};
dojo.lang.getNameInObj=function(ns,_9){
if(!ns){
ns=dj_global;
}
for(var x in ns){
if(ns[x]===_9){
return new String(x);
}
}
return null;
};
dojo.lang.shallowCopy=function(_b,_c){
var i,_e;
if(_b===null){
return null;
}
if(dojo.lang.isObject(_b)){
_e=new _b.constructor();
for(i in _b){
if(dojo.lang.isUndefined(_e[i])){
_e[i]=_c?dojo.lang.shallowCopy(_b[i],_c):_b[i];
}
}
}else{
if(dojo.lang.isArray(_b)){
_e=[];
for(i=0;i<_b.length;i++){
_e[i]=_c?dojo.lang.shallowCopy(_b[i],_c):_b[i];
}
}else{
_e=_b;
}
}
return _e;
};
dojo.lang.firstValued=function(){
for(var i=0;i<arguments.length;i++){
if(typeof arguments[i]!="undefined"){
return arguments[i];
}
}
return undefined;
};
dojo.lang.getObjPathValue=function(_10,_11,_12){
with(dojo.parseObjPath(_10,_11,_12)){
return dojo.evalProp(prop,obj,_12);
}
};
dojo.lang.setObjPathValue=function(_13,_14,_15,_16){
dojo.deprecated("dojo.lang.setObjPathValue","use dojo.parseObjPath and the '=' operator","0.6");
if(arguments.length<4){
_16=true;
}
with(dojo.parseObjPath(_13,_15,_16)){
if(obj&&(_16||(prop in obj))){
obj[prop]=_14;
}
}
};
