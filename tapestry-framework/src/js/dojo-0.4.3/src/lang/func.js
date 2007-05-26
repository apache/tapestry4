dojo.provide("dojo.lang.func");
dojo.require("dojo.lang.common");
dojo.lang.hitch=function(_1,_2){
var _3=[];
for(var x=2;x<arguments.length;x++){
_3.push(arguments[x]);
}
var _5=(dojo.lang.isString(_2)?_1[_2]:_2)||function(){
};
return function(){
var ta=_3.concat([]);
for(var x=0;x<arguments.length;x++){
ta.push(arguments[x]);
}
return _5.apply(_1,ta);
};
};
dojo.lang.anonCtr=0;
dojo.lang.anon={};
dojo.lang.nameAnonFunc=function(_8,_9,_a){
var _b=(_9||dojo.lang.anon);
if((_a)||((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true))){
for(var x in _b){
try{
if(_b[x]===_8){
return x;
}
}
catch(e){
}
}
}
var _d="__"+dojo.lang.anonCtr++;
while(typeof _b[_d]!="undefined"){
_d="__"+dojo.lang.anonCtr++;
}
_b[_d]=_8;
return _d;
};
dojo.lang.forward=function(_e){
return function(){
return this[_e].apply(this,arguments);
};
};
dojo.lang.curry=function(_f,_10){
var _11=[];
_f=_f||dj_global;
if(dojo.lang.isString(_10)){
_10=_f[_10];
}
for(var x=2;x<arguments.length;x++){
_11.push(arguments[x]);
}
var _13=(_10["__preJoinArity"]||_10.length)-_11.length;
function gather(_14,_15,_16){
var _17=_16;
var _18=_15.slice(0);
for(var x=0;x<_14.length;x++){
_18.push(_14[x]);
}
_16=_16-_14.length;
if(_16<=0){
var res=_10.apply(_f,_18);
_16=_17;
return res;
}else{
return function(){
return gather(arguments,_18,_16);
};
}
}
return gather([],_11,_13);
};
dojo.lang.curryArguments=function(_1b,_1c,_1d,_1e){
var _1f=[];
var x=_1e||0;
for(x=_1e;x<_1d.length;x++){
_1f.push(_1d[x]);
}
return dojo.lang.curry.apply(dojo.lang,[_1b,_1c].concat(_1f));
};
dojo.lang.tryThese=function(){
for(var x=0;x<arguments.length;x++){
try{
if(typeof arguments[x]=="function"){
var ret=(arguments[x]());
if(ret){
return ret;
}
}
}
catch(e){
dojo.debug(e);
}
}
};
dojo.lang.delayThese=function(_23,cb,_25,_26){
if(!_23.length){
if(typeof _26=="function"){
_26();
}
return;
}
if((typeof _25=="undefined")&&(typeof cb=="number")){
_25=cb;
cb=function(){
};
}else{
if(!cb){
cb=function(){
};
if(!_25){
_25=0;
}
}
}
setTimeout(function(){
(_23.shift())();
cb();
dojo.lang.delayThese(_23,cb,_25,_26);
},_25);
};
