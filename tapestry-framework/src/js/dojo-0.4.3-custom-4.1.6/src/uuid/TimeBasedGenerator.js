dojo.provide("dojo.uuid.TimeBasedGenerator");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.type");
dojo.require("dojo.lang.assert");
dojo.uuid.TimeBasedGenerator=new function(){
this.GREGORIAN_CHANGE_OFFSET_IN_HOURS=3394248;
var _1=null;
var _2=null;
var _3=null;
var _4=0;
var _5=null;
var _6=null;
var _7=null;
var _8=16;
function _carry(_9){
_9[2]+=_9[3]>>>16;
_9[3]&=65535;
_9[1]+=_9[2]>>>16;
_9[2]&=65535;
_9[0]+=_9[1]>>>16;
_9[1]&=65535;
dojo.lang.assert((_9[0]>>>16)===0);
}
function _get64bitArrayFromFloat(x){
var _b=new Array(0,0,0,0);
_b[3]=x%65536;
x-=_b[3];
x/=65536;
_b[2]=x%65536;
x-=_b[2];
x/=65536;
_b[1]=x%65536;
x-=_b[1];
x/=65536;
_b[0]=x;
return _b;
}
function _addTwo64bitArrays(_c,_d){
dojo.lang.assertType(_c,Array);
dojo.lang.assertType(_d,Array);
dojo.lang.assert(_c.length==4);
dojo.lang.assert(_d.length==4);
var _e=new Array(0,0,0,0);
_e[3]=_c[3]+_d[3];
_e[2]=_c[2]+_d[2];
_e[1]=_c[1]+_d[1];
_e[0]=_c[0]+_d[0];
_carry(_e);
return _e;
}
function _multiplyTwo64bitArrays(_f,_10){
dojo.lang.assertType(_f,Array);
dojo.lang.assertType(_10,Array);
dojo.lang.assert(_f.length==4);
dojo.lang.assert(_10.length==4);
var _11=false;
if(_f[0]*_10[0]!==0){
_11=true;
}
if(_f[0]*_10[1]!==0){
_11=true;
}
if(_f[0]*_10[2]!==0){
_11=true;
}
if(_f[1]*_10[0]!==0){
_11=true;
}
if(_f[1]*_10[1]!==0){
_11=true;
}
if(_f[2]*_10[0]!==0){
_11=true;
}
dojo.lang.assert(!_11);
var _12=new Array(0,0,0,0);
_12[0]+=_f[0]*_10[3];
_carry(_12);
_12[0]+=_f[1]*_10[2];
_carry(_12);
_12[0]+=_f[2]*_10[1];
_carry(_12);
_12[0]+=_f[3]*_10[0];
_carry(_12);
_12[1]+=_f[1]*_10[3];
_carry(_12);
_12[1]+=_f[2]*_10[2];
_carry(_12);
_12[1]+=_f[3]*_10[1];
_carry(_12);
_12[2]+=_f[2]*_10[3];
_carry(_12);
_12[2]+=_f[3]*_10[2];
_carry(_12);
_12[3]+=_f[3]*_10[3];
_carry(_12);
return _12;
}
function _padWithLeadingZeros(_13,_14){
while(_13.length<_14){
_13="0"+_13;
}
return _13;
}
function _generateRandomEightCharacterHexString(){
var _15=Math.floor((Math.random()%1)*Math.pow(2,32));
var _16=_15.toString(_8);
while(_16.length<8){
_16="0"+_16;
}
return _16;
}
function _generateUuidString(_17){
dojo.lang.assertType(_17,String,{optional:true});
if(_17){
dojo.lang.assert(_17.length==12);
}else{
if(_7){
_17=_7;
}else{
if(!_1){
var _18=32768;
var _19=Math.floor((Math.random()%1)*Math.pow(2,15));
var _1a=(_18|_19).toString(_8);
_1=_1a+_generateRandomEightCharacterHexString();
}
_17=_1;
}
}
if(!_2){
var _1b=32768;
var _1c=Math.floor((Math.random()%1)*Math.pow(2,14));
_2=(_1b|_1c).toString(_8);
}
var now=new Date();
var _1e=now.valueOf();
var _1f=_get64bitArrayFromFloat(_1e);
if(!_5){
var _20=_get64bitArrayFromFloat(60*60);
var _21=_get64bitArrayFromFloat(dojo.uuid.TimeBasedGenerator.GREGORIAN_CHANGE_OFFSET_IN_HOURS);
var _22=_multiplyTwo64bitArrays(_21,_20);
var _23=_get64bitArrayFromFloat(1000);
_5=_multiplyTwo64bitArrays(_22,_23);
_6=_get64bitArrayFromFloat(10000);
}
var _24=_1f;
var _25=_addTwo64bitArrays(_5,_24);
var _26=_multiplyTwo64bitArrays(_25,_6);
if(now.valueOf()==_3){
_26[3]+=_4;
_carry(_26);
_4+=1;
if(_4==10000){
while(now.valueOf()==_3){
now=new Date();
}
}
}else{
_3=now.valueOf();
_4=1;
}
var _27=_26[2].toString(_8);
var _28=_26[3].toString(_8);
var _29=_padWithLeadingZeros(_27,4)+_padWithLeadingZeros(_28,4);
var _2a=_26[1].toString(_8);
_2a=_padWithLeadingZeros(_2a,4);
var _2b=_26[0].toString(_8);
_2b=_padWithLeadingZeros(_2b,3);
var _2c="-";
var _2d="1";
var _2e=_29+_2c+_2a+_2c+_2d+_2b+_2c+_2+_2c+_17;
_2e=_2e.toLowerCase();
return _2e;
}
this.setNode=function(_2f){
dojo.lang.assert((_2f===null)||(_2f.length==12));
_7=_2f;
};
this.getNode=function(){
return _7;
};
this.generate=function(_30){
var _31=null;
var _32=null;
if(_30){
if(dojo.lang.isObject(_30)&&!dojo.lang.isBuiltIn(_30)){
var _33=_30;
dojo.lang.assertValidKeywords(_33,["node","hardwareNode","pseudoNode","returnType"]);
var _34=_33["node"];
var _35=_33["hardwareNode"];
var _36=_33["pseudoNode"];
_31=(_34||_36||_35);
if(_31){
var _37=_31.charAt(0);
var _38=parseInt(_37,_8);
if(_35){
dojo.lang.assert((_38>=0)&&(_38<=7));
}
if(_36){
dojo.lang.assert((_38>=8)&&(_38<=15));
}
}
_32=_33["returnType"];
dojo.lang.assertType(_32,Function,{optional:true});
}else{
if(dojo.lang.isString(_30)){
_31=_30;
_32=null;
}else{
if(dojo.lang.isFunction(_30)){
_31=null;
_32=_30;
}
}
}
if(_31){
dojo.lang.assert(_31.length==12);
var _39=parseInt(_31,_8);
dojo.lang.assert(isFinite(_39));
}
dojo.lang.assertType(_32,Function,{optional:true});
}
var _3a=_generateUuidString(_31);
var _3b;
if(_32&&(_32!=String)){
_3b=new _32(_3a);
}else{
_3b=_3a;
}
return _3b;
};
}();
