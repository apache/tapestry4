dojo.provide("dojo.gfx.common");
dojo.require("dojo.gfx.color");
dojo.require("dojo.lang.declare");
dojo.require("dojo.lang.extras");
dojo.require("dojo.dom");
dojo.lang.mixin(dojo.gfx,{defaultPath:{type:"path",path:""},defaultPolyline:{type:"polyline",points:[]},defaultRect:{type:"rect",x:0,y:0,width:100,height:100,r:0},defaultEllipse:{type:"ellipse",cx:0,cy:0,rx:200,ry:100},defaultCircle:{type:"circle",cx:0,cy:0,r:100},defaultLine:{type:"line",x1:0,y1:0,x2:100,y2:100},defaultImage:{type:"image",width:0,height:0,src:""},defaultStroke:{color:"black",width:1,cap:"butt",join:4},defaultLinearGradient:{type:"linear",x1:0,y1:0,x2:100,y2:100,colors:[{offset:0,color:"black"},{offset:1,color:"white"}]},defaultRadialGradient:{type:"radial",cx:0,cy:0,r:100,colors:[{offset:0,color:"black"},{offset:1,color:"white"}]},defaultPattern:{type:"pattern",x:0,y:0,width:0,height:0,src:""},normalizeColor:function(_1){
return (_1 instanceof dojo.gfx.color.Color)?_1:new dojo.gfx.color.Color(_1);
},normalizeParameters:function(_2,_3){
if(_3){
var _4={};
for(var x in _2){
if(x in _3&&!(x in _4)){
_2[x]=_3[x];
}
}
}
return _2;
},makeParameters:function(_6,_7){
if(!_7){
return dojo.lang.shallowCopy(_6,true);
}
var _8={};
for(var i in _6){
if(!(i in _8)){
_8[i]=dojo.lang.shallowCopy((i in _7)?_7[i]:_6[i],true);
}
}
return _8;
},formatNumber:function(x,_b){
var _c=x.toString();
if(_c.indexOf("e")>=0){
_c=x.toFixed(4);
}else{
var _d=_c.indexOf(".");
if(_d>=0&&_c.length-_d>5){
_c=x.toFixed(4);
}
}
if(x<0){
return _c;
}
return _b?" "+_c:_c;
},pathRegExp:/([A-Za-z]+)|(\d+(\.\d+)?)|(\.\d+)|(-\d+(\.\d+)?)|(-\.\d+)/g});
dojo.declare("dojo.gfx.Surface",null,{initializer:function(){
this.rawNode=null;
},getEventSource:function(){
return this.rawNode;
}});
dojo.declare("dojo.gfx.Point",null,{});
dojo.declare("dojo.gfx.Rectangle",null,{});
