dojo.require("dojo.crypto");
dojo.provide("dojo.crypto.SHA1");
dojo.require("dojo.experimental");
dojo.experimental("dojo.crypto.SHA1");
dojo.crypto.SHA1=new function(){
var _1=8;
var _2=(1<<_1)-1;
function toWord(s){
var wa=[];
for(var i=0;i<s.length*_1;i+=_1){
wa[i>>5]|=(s.charCodeAt(i/_1)&_2)<<(i%32);
}
return wa;
}
function toString(wa){
var s=[];
for(var i=0;i<wa.length*32;i+=_1){
s.push(String.fromCharCode((wa[i>>5]>>>(i%32))&_2));
}
return s.join("");
}
function toHex(wa){
var h="0123456789abcdef";
var s=[];
for(var i=0;i<wa.length*4;i++){
s.push(h.charAt((wa[i>>2]>>((i%4)*8+4))&15)+h.charAt((wa[i>>2]>>((i%4)*8))&15));
}
return s.join("");
}
function toBase64(wa){
var p="=";
var _f="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
var s=[];
for(var i=0;i<wa.length*4;i+=3){
var t=(((wa[i>>2]>>8*(i%4))&255)<<16)|(((wa[i+1>>2]>>8*((i+1)%4))&255)<<8)|((wa[i+2>>2]>>8*((i+2)%4))&255);
for(var j=0;j<4;j++){
if(i*8+j*6>wa.length*32){
s.push(p);
}else{
s.push(_f.charAt((t>>6*(3-j))&63));
}
}
}
return s.join("");
}
function add(x,y){
var l=(x&65535)+(y&65535);
var m=(x>>16)+(y>>16)+(l>>16);
return (m<<16)|(l&65535);
}
function r(x,n){
return (x<<n)|(x>>>(32-n));
}
function f(u,v,w){
return ((u&v)|(~u&w));
}
function g(u,v,w){
return ((u&v)|(u&w)|(v&w));
}
function h(u,v,w){
return (u^v^w);
}
function fn(i,u,v,w){
if(i<20){
return f(u,v,w);
}
if(i<40){
return h(u,v,w);
}
if(i<60){
return g(u,v,w);
}
return h(u,v,w);
}
function cnst(i){
if(i<20){
return 1518500249;
}
if(i<40){
return 1859775393;
}
if(i<60){
return -1894007588;
}
return -899497514;
}
function core(x,len){
x[len>>5]|=128<<(24-len%32);
x[((len+64>>9)<<4)+15]=len;
var w=[];
var a=1732584193;
var b=-271733879;
var c=-1732584194;
var d=271733878;
var e=-1009589776;
for(var i=0;i<x.length;i+=16){
var _31=a;
var _32=b;
var _33=c;
var _34=d;
var _35=e;
for(var j=0;j<80;j++){
if(j<16){
w[j]=x[i+j];
}else{
w[j]=r(w[j-3]^w[j-8]^w[j-14]^w[j-16],1);
}
var t=add(add(r(a,5),fn(j,b,c,d)),add(add(e,w[j]),cnst(j)));
e=d;
d=c;
c=r(b,30);
b=a;
a=t;
}
a=add(a,_31);
b=add(b,_32);
c=add(c,_33);
d=add(d,_34);
e=add(e,_35);
}
return [a,b,c,d,e];
}
function hmac(_38,key){
var wa=toWord(key);
if(wa.length>16){
wa=core(wa,key.length*_1);
}
var l=[],r=[];
for(var i=0;i<16;i++){
l[i]=wa[i]^909522486;
r[i]=wa[i]^1549556828;
}
var h=core(l.concat(toWord(_38)),512+_38.length*_1);
return core(r.concat(h),640);
}
this.compute=function(_3f,_40){
var out=_40||dojo.crypto.outputTypes.Base64;
switch(out){
case dojo.crypto.outputTypes.Hex:
return toHex(core(toWord(_3f),_3f.length*_1));
case dojo.crypto.outputTypes.String:
return toString(core(toWord(_3f),_3f.length*_1));
default:
return toBase64(core(toWord(_3f),_3f.length*_1));
}
};
this.getHMAC=function(_42,key,_44){
var out=_44||dojo.crypto.outputTypes.Base64;
switch(out){
case dojo.crypto.outputTypes.Hex:
return toHex(hmac(_42,key));
case dojo.crypto.outputTypes.String:
return toString(hmac(_42,key));
default:
return toBase64(hmac(_42,key));
}
};
}();
