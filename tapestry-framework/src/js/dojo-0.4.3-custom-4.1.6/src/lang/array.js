dojo.provide("dojo.lang.array");
dojo.require("dojo.lang.common");
dojo.lang.mixin(dojo.lang,{has:function(_1,_2){
try{
return typeof _1[_2]!="undefined";
}
catch(e){
return false;
}
},isEmpty:function(_3){
if(dojo.lang.isObject(_3)){
var _4={};
var _5=0;
for(var x in _3){
if(_3[x]&&(!_4[x])){
_5++;
break;
}
}
return _5==0;
}else{
if(dojo.lang.isArrayLike(_3)||dojo.lang.isString(_3)){
return _3.length==0;
}
}
},map:function(_7,_8,_9){
var _a=dojo.lang.isString(_7);
if(_a){
_7=_7.split("");
}
if(dojo.lang.isFunction(_8)&&(!_9)){
_9=_8;
_8=dj_global;
}else{
if(dojo.lang.isFunction(_8)&&_9){
var _b=_8;
_8=_9;
_9=_b;
}
}
if(Array.map){
var _c=Array.map(_7,_9,_8);
}else{
var _c=[];
for(var i=0;i<_7.length;++i){
_c.push(_9.call(_8,_7[i]));
}
}
if(_a){
return _c.join("");
}else{
return _c;
}
},reduce:function(_e,_f,obj,_11){
var _12=_f;
if(arguments.length==2){
_11=_f;
_12=_e[0];
_e=_e.slice(1);
}else{
if(arguments.length==3){
if(dojo.lang.isFunction(obj)){
_11=obj;
obj=null;
}
}else{
if(dojo.lang.isFunction(obj)){
var tmp=_11;
_11=obj;
obj=tmp;
}
}
}
var ob=obj||dj_global;
dojo.lang.map(_e,function(val){
_12=_11.call(ob,_12,val);
});
return _12;
},forEach:function(_16,_17,_18){
if(dojo.lang.isString(_16)){
_16=_16.split("");
}
if(Array.forEach){
Array.forEach(_16,_17,_18);
}else{
if(!_18){
_18=dj_global;
}
for(var i=0,l=_16.length;i<l;i++){
_17.call(_18,_16[i],i,_16);
}
}
},_everyOrSome:function(_1b,arr,_1d,_1e){
if(dojo.lang.isString(arr)){
arr=arr.split("");
}
if(Array.every){
return Array[_1b?"every":"some"](arr,_1d,_1e);
}else{
if(!_1e){
_1e=dj_global;
}
for(var i=0,l=arr.length;i<l;i++){
var _21=_1d.call(_1e,arr[i],i,arr);
if(_1b&&!_21){
return false;
}else{
if((!_1b)&&(_21)){
return true;
}
}
}
return Boolean(_1b);
}
},every:function(arr,_23,_24){
return this._everyOrSome(true,arr,_23,_24);
},some:function(arr,_26,_27){
return this._everyOrSome(false,arr,_26,_27);
},filter:function(arr,_29,_2a){
var _2b=dojo.lang.isString(arr);
if(_2b){
arr=arr.split("");
}
var _2c;
if(Array.filter){
_2c=Array.filter(arr,_29,_2a);
}else{
if(!_2a){
if(arguments.length>=3){
dojo.raise("thisObject doesn't exist!");
}
_2a=dj_global;
}
_2c=[];
for(var i=0;i<arr.length;i++){
if(_29.call(_2a,arr[i],i,arr)){
_2c.push(arr[i]);
}
}
}
if(_2b){
return _2c.join("");
}else{
return _2c;
}
},unnest:function(){
var out=[];
for(var i=0;i<arguments.length;i++){
if(dojo.lang.isArrayLike(arguments[i])){
var add=dojo.lang.unnest.apply(this,arguments[i]);
out=out.concat(add);
}else{
out.push(arguments[i]);
}
}
return out;
},toArray:function(_31,_32){
var _33=[];
for(var i=_32||0;i<_31.length;i++){
_33.push(_31[i]);
}
return _33;
}});
