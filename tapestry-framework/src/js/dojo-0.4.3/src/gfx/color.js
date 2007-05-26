dojo.provide("dojo.gfx.color");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.array");
dojo.gfx.color.Color=function(r,g,b,a){
if(dojo.lang.isArray(r)){
this.r=r[0];
this.g=r[1];
this.b=r[2];
this.a=r[3]||1;
}else{
if(dojo.lang.isString(r)){
var _5=dojo.gfx.color.extractRGB(r);
this.r=_5[0];
this.g=_5[1];
this.b=_5[2];
this.a=g||1;
}else{
if(r instanceof dojo.gfx.color.Color){
this.r=r.r;
this.b=r.b;
this.g=r.g;
this.a=r.a;
}else{
this.r=r;
this.g=g;
this.b=b;
this.a=a;
}
}
}
};
dojo.gfx.color.Color.fromArray=function(_6){
return new dojo.gfx.color.Color(_6[0],_6[1],_6[2],_6[3]);
};
dojo.extend(dojo.gfx.color.Color,{toRgb:function(_7){
if(_7){
return this.toRgba();
}else{
return [this.r,this.g,this.b];
}
},toRgba:function(){
return [this.r,this.g,this.b,this.a];
},toHex:function(){
return dojo.gfx.color.rgb2hex(this.toRgb());
},toCss:function(){
return "rgb("+this.toRgb().join()+")";
},toString:function(){
return this.toHex();
},blend:function(_8,_9){
var _a=null;
if(dojo.lang.isArray(_8)){
_a=_8;
}else{
if(_8 instanceof dojo.gfx.color.Color){
_a=_8.toRgb();
}else{
_a=new dojo.gfx.color.Color(_8).toRgb();
}
}
return dojo.gfx.color.blend(this.toRgb(),_a,_9);
}});
dojo.gfx.color.named={white:[255,255,255],black:[0,0,0],red:[255,0,0],green:[0,255,0],lime:[0,255,0],blue:[0,0,255],navy:[0,0,128],gray:[128,128,128],silver:[192,192,192]};
dojo.gfx.color.blend=function(a,b,_d){
if(typeof a=="string"){
return dojo.gfx.color.blendHex(a,b,_d);
}
if(!_d){
_d=0;
}
_d=Math.min(Math.max(-1,_d),1);
_d=((_d+1)/2);
var c=[];
for(var x=0;x<3;x++){
c[x]=parseInt(b[x]+((a[x]-b[x])*_d));
}
return c;
};
dojo.gfx.color.blendHex=function(a,b,_12){
return dojo.gfx.color.rgb2hex(dojo.gfx.color.blend(dojo.gfx.color.hex2rgb(a),dojo.gfx.color.hex2rgb(b),_12));
};
dojo.gfx.color.extractRGB=function(_13){
var hex="0123456789abcdef";
_13=_13.toLowerCase();
if(_13.indexOf("rgb")==0){
var _15=_13.match(/rgba*\((\d+), *(\d+), *(\d+)/i);
var ret=_15.splice(1,3);
return ret;
}else{
var _17=dojo.gfx.color.hex2rgb(_13);
if(_17){
return _17;
}else{
return dojo.gfx.color.named[_13]||[255,255,255];
}
}
};
dojo.gfx.color.hex2rgb=function(hex){
var _19="0123456789ABCDEF";
var rgb=new Array(3);
if(hex.indexOf("#")==0){
hex=hex.substring(1);
}
hex=hex.toUpperCase();
if(hex.replace(new RegExp("["+_19+"]","g"),"")!=""){
return null;
}
if(hex.length==3){
rgb[0]=hex.charAt(0)+hex.charAt(0);
rgb[1]=hex.charAt(1)+hex.charAt(1);
rgb[2]=hex.charAt(2)+hex.charAt(2);
}else{
rgb[0]=hex.substring(0,2);
rgb[1]=hex.substring(2,4);
rgb[2]=hex.substring(4);
}
for(var i=0;i<rgb.length;i++){
rgb[i]=_19.indexOf(rgb[i].charAt(0))*16+_19.indexOf(rgb[i].charAt(1));
}
return rgb;
};
dojo.gfx.color.rgb2hex=function(r,g,b){
if(dojo.lang.isArray(r)){
g=r[1]||0;
b=r[2]||0;
r=r[0]||0;
}
var ret=dojo.lang.map([r,g,b],function(x){
x=new Number(x);
var s=x.toString(16);
while(s.length<2){
s="0"+s;
}
return s;
});
ret.unshift("#");
return ret.join("");
};
