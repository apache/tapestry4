dojo.provide("dojo.uuid.LightweightGenerator");
dojo.uuid.LightweightGenerator=new function(){
var _1=16;
function _generateRandomEightCharacterHexString(){
var _2=Math.floor((Math.random()%1)*Math.pow(2,32));
var _3=_2.toString(_1);
while(_3.length<8){
_3="0"+_3;
}
return _3;
}
this.generate=function(_4){
var _5="-";
var _6="4";
var _7="8";
var a=_generateRandomEightCharacterHexString();
var b=_generateRandomEightCharacterHexString();
b=b.substring(0,4)+_5+_6+b.substring(5,8);
var c=_generateRandomEightCharacterHexString();
c=_7+c.substring(1,4)+_5+c.substring(4,8);
var d=_generateRandomEightCharacterHexString();
var _c=a+_5+b+_5+c+d;
_c=_c.toLowerCase();
if(_4&&(_4!=String)){
_c=new _4(_c);
}
return _c;
};
}();
