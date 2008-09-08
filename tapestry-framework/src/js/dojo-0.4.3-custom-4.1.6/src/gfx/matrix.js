dojo.provide("dojo.gfx.matrix");
dojo.require("dojo.lang.common");
dojo.require("dojo.math.*");
dojo.gfx.matrix.Matrix2D=function(_1){
if(_1){
if(_1 instanceof Array){
if(_1.length>0){
var m=dojo.gfx.matrix.normalize(_1[0]);
for(var i=1;i<_1.length;++i){
var l=m;
var r=dojo.gfx.matrix.normalize(_1[i]);
m=new dojo.gfx.matrix.Matrix2D();
m.xx=l.xx*r.xx+l.xy*r.yx;
m.xy=l.xx*r.xy+l.xy*r.yy;
m.yx=l.yx*r.xx+l.yy*r.yx;
m.yy=l.yx*r.xy+l.yy*r.yy;
m.dx=l.xx*r.dx+l.xy*r.dy+l.dx;
m.dy=l.yx*r.dx+l.yy*r.dy+l.dy;
}
dojo.mixin(this,m);
}
}else{
dojo.mixin(this,_1);
}
}
};
dojo.extend(dojo.gfx.matrix.Matrix2D,{xx:1,xy:0,yx:0,yy:1,dx:0,dy:0});
dojo.mixin(dojo.gfx.matrix,{identity:new dojo.gfx.matrix.Matrix2D(),flipX:new dojo.gfx.matrix.Matrix2D({xx:-1}),flipY:new dojo.gfx.matrix.Matrix2D({yy:-1}),flipXY:new dojo.gfx.matrix.Matrix2D({xx:-1,yy:-1}),translate:function(a,b){
if(arguments.length>1){
return new dojo.gfx.matrix.Matrix2D({dx:a,dy:b});
}
return new dojo.gfx.matrix.Matrix2D({dx:a.x,dy:a.y});
},scale:function(a,b){
if(arguments.length>1){
return new dojo.gfx.matrix.Matrix2D({xx:a,yy:b});
}
if(typeof a=="number"){
return new dojo.gfx.matrix.Matrix2D({xx:a,yy:a});
}
return new dojo.gfx.matrix.Matrix2D({xx:a.x,yy:a.y});
},rotate:function(_a){
var c=Math.cos(_a);
var s=Math.sin(_a);
return new dojo.gfx.matrix.Matrix2D({xx:c,xy:s,yx:-s,yy:c});
},rotateg:function(_d){
return dojo.gfx.matrix.rotate(dojo.math.degToRad(_d));
},skewX:function(_e){
return new dojo.gfx.matrix.Matrix2D({xy:Math.tan(_e)});
},skewXg:function(_f){
return dojo.gfx.matrix.skewX(dojo.math.degToRad(_f));
},skewY:function(_10){
return new dojo.gfx.matrix.Matrix2D({yx:-Math.tan(_10)});
},skewYg:function(_11){
return dojo.gfx.matrix.skewY(dojo.math.degToRad(_11));
},normalize:function(_12){
return (_12 instanceof dojo.gfx.matrix.Matrix2D)?_12:new dojo.gfx.matrix.Matrix2D(_12);
},clone:function(_13){
var obj=new dojo.gfx.matrix.Matrix2D();
for(var i in _13){
if(typeof (_13[i])=="number"&&typeof (obj[i])=="number"&&obj[i]!=_13[i]){
obj[i]=_13[i];
}
}
return obj;
},invert:function(_16){
var m=dojo.gfx.matrix.normalize(_16);
var D=m.xx*m.yy-m.xy*m.yx;
var M=new dojo.gfx.matrix.Matrix2D({xx:m.yy/D,xy:-m.xy/D,yx:-m.yx/D,yy:m.xx/D,dx:(m.yx*m.dy-m.yy*m.dx)/D,dy:(m.xy*m.dx-m.xx*m.dy)/D});
return M;
},_multiplyPoint:function(m,x,y){
return {x:m.xx*x+m.xy*y+m.dx,y:m.yx*x+m.yy*y+m.dy};
},multiplyPoint:function(_1d,a,b){
var m=dojo.gfx.matrix.normalize(_1d);
if(typeof a=="number"&&typeof b=="number"){
return dojo.gfx.matrix._multiplyPoint(m,a,b);
}
return dojo.gfx.matrix._multiplyPoint(m,a.x,a.y);
},multiply:function(_21){
var m=dojo.gfx.matrix.normalize(_21);
for(var i=1;i<arguments.length;++i){
var l=m;
var r=dojo.gfx.matrix.normalize(arguments[i]);
m=new dojo.gfx.matrix.Matrix2D();
m.xx=l.xx*r.xx+l.xy*r.yx;
m.xy=l.xx*r.xy+l.xy*r.yy;
m.yx=l.yx*r.xx+l.yy*r.yx;
m.yy=l.yx*r.xy+l.yy*r.yy;
m.dx=l.xx*r.dx+l.xy*r.dy+l.dx;
m.dy=l.yx*r.dx+l.yy*r.dy+l.dy;
}
return m;
},_sandwich:function(m,x,y){
return dojo.gfx.matrix.multiply(dojo.gfx.matrix.translate(x,y),m,dojo.gfx.matrix.translate(-x,-y));
},scaleAt:function(a,b,c,d){
switch(arguments.length){
case 4:
return dojo.gfx.matrix._sandwich(dojo.gfx.matrix.scale(a,b),c,d);
case 3:
if(typeof c=="number"){
return dojo.gfx.matrix._sandwich(dojo.gfx.matrix.scale(a),b,c);
}
return dojo.gfx.matrix._sandwich(dojo.gfx.matrix.scale(a,b),c.x,c.y);
}
return dojo.gfx.matrix._sandwich(dojo.gfx.matrix.scale(a),b.x,b.y);
},rotateAt:function(_2d,a,b){
if(arguments.length>2){
return dojo.gfx.matrix._sandwich(dojo.gfx.matrix.rotate(_2d),a,b);
}
return dojo.gfx.matrix._sandwich(dojo.gfx.matrix.rotate(_2d),a.x,a.y);
},rotategAt:function(_30,a,b){
if(arguments.length>2){
return dojo.gfx.matrix._sandwich(dojo.gfx.matrix.rotateg(_30),a,b);
}
return dojo.gfx.matrix._sandwich(dojo.gfx.matrix.rotateg(_30),a.x,a.y);
},skewXAt:function(_33,a,b){
if(arguments.length>2){
return dojo.gfx.matrix._sandwich(dojo.gfx.matrix.skewX(_33),a,b);
}
return dojo.gfx.matrix._sandwich(dojo.gfx.matrix.skewX(_33),a.x,a.y);
},skewXgAt:function(_36,a,b){
if(arguments.length>2){
return dojo.gfx.matrix._sandwich(dojo.gfx.matrix.skewXg(_36),a,b);
}
return dojo.gfx.matrix._sandwich(dojo.gfx.matrix.skewXg(_36),a.x,a.y);
},skewYAt:function(_39,a,b){
if(arguments.length>2){
return dojo.gfx.matrix._sandwich(dojo.gfx.matrix.skewY(_39),a,b);
}
return dojo.gfx.matrix._sandwich(dojo.gfx.matrix.skewY(_39),a.x,a.y);
},skewYgAt:function(_3c,a,b){
if(arguments.length>2){
return dojo.gfx.matrix._sandwich(dojo.gfx.matrix.skewYg(_3c),a,b);
}
return dojo.gfx.matrix._sandwich(dojo.gfx.matrix.skewYg(_3c),a.x,a.y);
}});
