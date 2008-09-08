dojo.provide("dojo.gfx.svg");
dojo.require("dojo.lang.declare");
dojo.require("dojo.svg");
dojo.require("dojo.gfx.color");
dojo.require("dojo.gfx.common");
dojo.require("dojo.gfx.shape");
dojo.require("dojo.gfx.path");
dojo.require("dojo.experimental");
dojo.experimental("dojo.gfx.svg");
dojo.gfx.svg.getRef=function(_1){
if(!_1||_1=="none"){
return null;
}
if(_1.match(/^url\(#.+\)$/)){
return dojo.byId(_1.slice(5,-1));
}
if(dojo.render.html.opera&&_1.match(/^#dj_unique_.+$/)){
return dojo.byId(_1.slice(1));
}
return null;
};
dojo.lang.extend(dojo.gfx.Shape,{setFill:function(_2){
if(!_2){
this.fillStyle=null;
this.rawNode.setAttribute("fill","none");
this.rawNode.setAttribute("fill-opacity",0);
return this;
}
if(typeof (_2)=="object"&&"type" in _2){
switch(_2.type){
case "linear":
var f=dojo.gfx.makeParameters(dojo.gfx.defaultLinearGradient,_2);
var _4=this._setFillObject(f,"linearGradient");
dojo.lang.forEach(["x1","y1","x2","y2"],function(x){
_4.setAttribute(x,f[x].toFixed(8));
});
break;
case "radial":
var f=dojo.gfx.makeParameters(dojo.gfx.defaultRadialGradient,_2);
var _4=this._setFillObject(f,"radialGradient");
dojo.lang.forEach(["cx","cy","r"],function(x){
_4.setAttribute(x,f[x].toFixed(8));
});
break;
case "pattern":
var f=dojo.gfx.makeParameters(dojo.gfx.defaultPattern,_2);
var _7=this._setFillObject(f,"pattern");
dojo.lang.forEach(["x","y","width","height"],function(x){
_7.setAttribute(x,f[x].toFixed(8));
});
break;
}
return this;
}
var f=dojo.gfx.normalizeColor(_2);
this.fillStyle=f;
this.rawNode.setAttribute("fill",f.toCss());
this.rawNode.setAttribute("fill-opacity",f.a);
return this;
},setStroke:function(_9){
if(!_9){
this.strokeStyle=null;
this.rawNode.setAttribute("stroke","none");
this.rawNode.setAttribute("stroke-opacity",0);
return this;
}
this.strokeStyle=dojo.gfx.makeParameters(dojo.gfx.defaultStroke,_9);
this.strokeStyle.color=dojo.gfx.normalizeColor(this.strokeStyle.color);
var s=this.strokeStyle;
var rn=this.rawNode;
if(s){
rn.setAttribute("stroke",s.color.toCss());
rn.setAttribute("stroke-opacity",s.color.a);
rn.setAttribute("stroke-width",s.width);
rn.setAttribute("stroke-linecap",s.cap);
if(typeof (s.join)=="number"){
rn.setAttribute("stroke-linejoin","miter");
rn.setAttribute("stroke-miterlimit",s.join);
}else{
rn.setAttribute("stroke-linejoin",s.join);
}
}
return this;
},_setFillObject:function(f,_d){
var _e=this.rawNode.parentNode.getElementsByTagName("defs");
if(_e.length==0){
return this;
}
this.fillStyle=f;
var _f=_e[0];
var _10=this.rawNode.getAttribute("fill");
var ref=dojo.gfx.svg.getRef(_10);
if(ref){
_10=ref;
if(_10.tagName.toLowerCase()!=_d.toLowerCase()){
var id=_10.id;
_10.parentNode.removeChild(_10);
_10=document.createElementNS(dojo.svg.xmlns.svg,_d);
_10.setAttribute("id",id);
_f.appendChild(_10);
}else{
while(_10.childNodes.length){
_10.removeChild(_10.lastChild);
}
}
}else{
_10=document.createElementNS(dojo.svg.xmlns.svg,_d);
_10.setAttribute("id",dojo.dom.getUniqueId());
_f.appendChild(_10);
}
if(_d=="pattern"){
_10.setAttribute("patternUnits","userSpaceOnUse");
var img=document.createElementNS(dojo.svg.xmlns.svg,"image");
img.setAttribute("x",0);
img.setAttribute("y",0);
img.setAttribute("width",f.width.toFixed(8));
img.setAttribute("height",f.height.toFixed(8));
img.setAttributeNS(dojo.svg.xmlns.xlink,"href",f.src);
_10.appendChild(img);
}else{
_10.setAttribute("gradientUnits","userSpaceOnUse");
for(var i=0;i<f.colors.length;++i){
f.colors[i].color=dojo.gfx.normalizeColor(f.colors[i].color);
var t=document.createElementNS(dojo.svg.xmlns.svg,"stop");
t.setAttribute("offset",f.colors[i].offset.toFixed(8));
t.setAttribute("stop-color",f.colors[i].color.toCss());
_10.appendChild(t);
}
}
this.rawNode.setAttribute("fill","url(#"+_10.getAttribute("id")+")");
this.rawNode.removeAttribute("fill-opacity");
return _10;
},_applyTransform:function(){
var _16=this._getRealMatrix();
if(_16){
var tm=this.matrix;
this.rawNode.setAttribute("transform","matrix("+tm.xx.toFixed(8)+","+tm.yx.toFixed(8)+","+tm.xy.toFixed(8)+","+tm.yy.toFixed(8)+","+tm.dx.toFixed(8)+","+tm.dy.toFixed(8)+")");
}else{
this.rawNode.removeAttribute("transform");
}
return this;
},setRawNode:function(_18){
with(_18){
setAttribute("fill","none");
setAttribute("fill-opacity",0);
setAttribute("stroke","none");
setAttribute("stroke-opacity",0);
setAttribute("stroke-width",1);
setAttribute("stroke-linecap","butt");
setAttribute("stroke-linejoin","miter");
setAttribute("stroke-miterlimit",4);
}
this.rawNode=_18;
},moveToFront:function(){
this.rawNode.parentNode.appendChild(this.rawNode);
return this;
},moveToBack:function(){
this.rawNode.parentNode.insertBefore(this.rawNode,this.rawNode.parentNode.firstChild);
return this;
},setShape:function(_19){
this.shape=dojo.gfx.makeParameters(this.shape,_19);
for(var i in this.shape){
if(i!="type"){
this.rawNode.setAttribute(i,this.shape[i]);
}
}
return this;
},attachFill:function(_1b){
var _1c=null;
if(_1b){
var _1d=_1b.getAttribute("fill");
if(_1d=="none"){
return;
}
var ref=dojo.gfx.svg.getRef(_1d);
if(ref){
var _1f=ref;
switch(_1f.tagName.toLowerCase()){
case "lineargradient":
_1c=this._getGradient(dojo.gfx.defaultLinearGradient,_1f);
dojo.lang.forEach(["x1","y1","x2","y2"],function(x){
_1c[x]=_1f.getAttribute(x);
});
break;
case "radialgradient":
_1c=this._getGradient(dojo.gfx.defaultRadialGradient,_1f);
dojo.lang.forEach(["cx","cy","r"],function(x){
_1c[x]=_1f.getAttribute(x);
});
_1c.cx=_1f.getAttribute("cx");
_1c.cy=_1f.getAttribute("cy");
_1c.r=_1f.getAttribute("r");
break;
case "pattern":
_1c=dojo.lang.shallowCopy(dojo.gfx.defaultPattern,true);
dojo.lang.forEach(["x","y","width","height"],function(x){
_1c[x]=_1f.getAttribute(x);
});
_1c.src=_1f.firstChild.getAttributeNS(dojo.svg.xmlns.xlink,"href");
break;
}
}else{
_1c=new dojo.gfx.color.Color(_1d);
var _23=_1b.getAttribute("fill-opacity");
if(_23!=null){
_1c.a=_23;
}
}
}
return _1c;
},_getGradient:function(_24,_25){
var _26=dojo.lang.shallowCopy(_24,true);
_26.colors=[];
for(var i=0;i<_25.childNodes.length;++i){
_26.colors.push({offset:_25.childNodes[i].getAttribute("offset"),color:new dojo.gfx.color.Color(_25.childNodes[i].getAttribute("stop-color"))});
}
return _26;
},attachStroke:function(_28){
if(!_28){
return;
}
var _29=_28.getAttribute("stroke");
if(_29==null||_29=="none"){
return null;
}
var _2a=dojo.lang.shallowCopy(dojo.gfx.defaultStroke,true);
var _2b=new dojo.gfx.color.Color(_29);
if(_2b){
_2a.color=_2b;
_2a.color.a=_28.getAttribute("stroke-opacity");
_2a.width=_28.getAttribute("stroke-width");
_2a.cap=_28.getAttribute("stroke-linecap");
_2a.join=_28.getAttribute("stroke-linejoin");
if(_2a.join=="miter"){
_2a.join=_28.getAttribute("stroke-miterlimit");
}
}
return _2a;
},attachTransform:function(_2c){
var _2d=null;
if(_2c){
_2d=_2c.getAttribute("transform");
if(_2d.match(/^matrix\(.+\)$/)){
var t=_2d.slice(7,-1).split(",");
_2d=dojo.gfx.matrix.normalize({xx:parseFloat(t[0]),xy:parseFloat(t[2]),yx:parseFloat(t[1]),yy:parseFloat(t[3]),dx:parseFloat(t[4]),dy:parseFloat(t[5])});
}
}
return _2d;
},attachShape:function(_2f){
var _30=null;
if(_2f){
_30=dojo.lang.shallowCopy(this.shape,true);
for(var i in _30){
_30[i]=_2f.getAttribute(i);
}
}
return _30;
},attach:function(_32){
if(_32){
this.rawNode=_32;
this.fillStyle=this.attachFill(_32);
this.strokeStyle=this.attachStroke(_32);
this.matrix=this.attachTransform(_32);
this.shape=this.attachShape(_32);
}
}});
dojo.declare("dojo.gfx.Group",dojo.gfx.Shape,{setRawNode:function(_33){
this.rawNode=_33;
}});
dojo.gfx.Group.nodeType="g";
dojo.declare("dojo.gfx.Rect",dojo.gfx.shape.Rect,{attachShape:function(_34){
var _35=null;
if(_34){
_35=dojo.gfx.Rect.superclass.attachShape.apply(this,arguments);
_35.r=Math.min(_34.getAttribute("rx"),_34.getAttribute("ry"));
}
return _35;
},setShape:function(_36){
this.shape=dojo.gfx.makeParameters(this.shape,_36);
this.bbox=null;
for(var i in this.shape){
if(i!="type"&&i!="r"){
this.rawNode.setAttribute(i,this.shape[i]);
}
}
this.rawNode.setAttribute("rx",this.shape.r);
this.rawNode.setAttribute("ry",this.shape.r);
return this;
}});
dojo.gfx.Rect.nodeType="rect";
dojo.gfx.Ellipse=dojo.gfx.shape.Ellipse;
dojo.gfx.Ellipse.nodeType="ellipse";
dojo.gfx.Circle=dojo.gfx.shape.Circle;
dojo.gfx.Circle.nodeType="circle";
dojo.gfx.Line=dojo.gfx.shape.Line;
dojo.gfx.Line.nodeType="line";
dojo.declare("dojo.gfx.Polyline",dojo.gfx.shape.Polyline,{setShape:function(_38){
if(_38&&_38 instanceof Array){
this.shape=dojo.gfx.makeParameters(this.shape,{points:_38});
if(closed&&this.shape.points.length){
this.shape.points.push(this.shape.points[0]);
}
}else{
this.shape=dojo.gfx.makeParameters(this.shape,_38);
}
this.box=null;
var _39=[];
var p=this.shape.points;
for(var i=0;i<p.length;++i){
_39.push(p[i].x.toFixed(8));
_39.push(p[i].y.toFixed(8));
}
this.rawNode.setAttribute("points",_39.join(" "));
return this;
}});
dojo.gfx.Polyline.nodeType="polyline";
dojo.declare("dojo.gfx.Image",dojo.gfx.shape.Image,{setShape:function(_3c){
this.shape=dojo.gfx.makeParameters(this.shape,_3c);
this.bbox=null;
var _3d=this.rawNode;
for(var i in this.shape){
if(i!="type"&&i!="src"){
_3d.setAttribute(i,this.shape[i]);
}
}
_3d.setAttributeNS(dojo.svg.xmlns.xlink,"href",this.shape.src);
return this;
},setStroke:function(){
return this;
},setFill:function(){
return this;
},attachStroke:function(_3f){
return null;
},attachFill:function(_40){
return null;
}});
dojo.gfx.Image.nodeType="image";
dojo.declare("dojo.gfx.Path",dojo.gfx.path.Path,{_updateWithSegment:function(_41){
dojo.gfx.Path.superclass._updateWithSegment.apply(this,arguments);
if(typeof (this.shape.path)=="string"){
this.rawNode.setAttribute("d",this.shape.path);
}
},setShape:function(_42){
dojo.gfx.Path.superclass.setShape.apply(this,arguments);
this.rawNode.setAttribute("d",this.shape.path);
return this;
}});
dojo.gfx.Path.nodeType="path";
dojo.gfx._creators={createPath:function(_43){
return this.createObject(dojo.gfx.Path,_43);
},createRect:function(_44){
return this.createObject(dojo.gfx.Rect,_44);
},createCircle:function(_45){
return this.createObject(dojo.gfx.Circle,_45);
},createEllipse:function(_46){
return this.createObject(dojo.gfx.Ellipse,_46);
},createLine:function(_47){
return this.createObject(dojo.gfx.Line,_47);
},createPolyline:function(_48){
return this.createObject(dojo.gfx.Polyline,_48);
},createImage:function(_49){
return this.createObject(dojo.gfx.Image,_49);
},createGroup:function(){
return this.createObject(dojo.gfx.Group);
},createObject:function(_4a,_4b){
if(!this.rawNode){
return null;
}
var _4c=new _4a();
var _4d=document.createElementNS(dojo.svg.xmlns.svg,_4a.nodeType);
_4c.setRawNode(_4d);
this.rawNode.appendChild(_4d);
_4c.setShape(_4b);
this.add(_4c);
return _4c;
},add:function(_4e){
var _4f=_4e.getParent();
if(_4f){
_4f.remove(_4e,true);
}
_4e._setParent(this,null);
this.rawNode.appendChild(_4e.rawNode);
return this;
},remove:function(_50,_51){
if(this.rawNode==_50.rawNode.parentNode){
this.rawNode.removeChild(_50.rawNode);
}
_50._setParent(null,null);
return this;
}};
dojo.gfx.attachNode=function(_52){
if(!_52){
return null;
}
var s=null;
switch(_52.tagName.toLowerCase()){
case dojo.gfx.Rect.nodeType:
s=new dojo.gfx.Rect();
break;
case dojo.gfx.Ellipse.nodeType:
s=new dojo.gfx.Ellipse();
break;
case dojo.gfx.Polyline.nodeType:
s=new dojo.gfx.Polyline();
break;
case dojo.gfx.Path.nodeType:
s=new dojo.gfx.Path();
break;
case dojo.gfx.Circle.nodeType:
s=new dojo.gfx.Circle();
break;
case dojo.gfx.Line.nodeType:
s=new dojo.gfx.Line();
break;
case dojo.gfx.Image.nodeType:
s=new dojo.gfx.Image();
break;
default:
dojo.debug("FATAL ERROR! tagName = "+_52.tagName);
}
s.attach(_52);
return s;
};
dojo.lang.extend(dojo.gfx.Surface,{setDimensions:function(_54,_55){
if(!this.rawNode){
return this;
}
this.rawNode.setAttribute("width",_54);
this.rawNode.setAttribute("height",_55);
return this;
},getDimensions:function(){
return this.rawNode?{width:this.rawNode.getAttribute("width"),height:this.rawNode.getAttribute("height")}:null;
}});
dojo.gfx.createSurface=function(_56,_57,_58){
var s=new dojo.gfx.Surface();
s.rawNode=document.createElementNS(dojo.svg.xmlns.svg,"svg");
s.rawNode.setAttribute("width",_57);
s.rawNode.setAttribute("height",_58);
var _5a=new dojo.gfx.svg.Defines();
var _5b=document.createElementNS(dojo.svg.xmlns.svg,dojo.gfx.svg.Defines.nodeType);
_5a.setRawNode(_5b);
s.rawNode.appendChild(_5b);
dojo.byId(_56).appendChild(s.rawNode);
return s;
};
dojo.gfx.attachSurface=function(_5c){
var s=new dojo.gfx.Surface();
s.rawNode=_5c;
return s;
};
dojo.lang.extend(dojo.gfx.Group,dojo.gfx._creators);
dojo.lang.extend(dojo.gfx.Surface,dojo.gfx._creators);
delete dojo.gfx._creators;
dojo.gfx.svg.Defines=function(){
this.rawNode=null;
};
dojo.lang.extend(dojo.gfx.svg.Defines,{setRawNode:function(_5e){
this.rawNode=_5e;
}});
dojo.gfx.svg.Defines.nodeType="defs";
