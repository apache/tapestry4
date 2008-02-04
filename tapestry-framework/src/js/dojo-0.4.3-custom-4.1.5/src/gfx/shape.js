dojo.provide("dojo.gfx.shape");
dojo.require("dojo.lang.declare");
dojo.require("dojo.gfx.common");
dojo.declare("dojo.gfx.Shape",null,{initializer:function(){
this.rawNode=null;
this.shape=null;
this.matrix=null;
this.fillStyle=null;
this.strokeStyle=null;
this.bbox=null;
this.parent=null;
this.parentMatrix=null;
},getNode:function(){
return this.rawNode;
},getShape:function(){
return this.shape;
},getTransform:function(){
return this.matrix;
},getFill:function(){
return this.fillStyle;
},getStroke:function(){
return this.strokeStyle;
},getParent:function(){
return this.parent;
},getBoundingBox:function(){
return this.bbox;
},getEventSource:function(){
return this.rawNode;
},setShape:function(_1){
return this;
},setFill:function(_2){
return this;
},setStroke:function(_3){
return this;
},moveToFront:function(){
return this;
},moveToBack:function(){
return this;
},setTransform:function(_4){
this.matrix=dojo.gfx.matrix.clone(_4?dojo.gfx.matrix.normalize(_4):dojo.gfx.identity,true);
return this._applyTransform();
},applyRightTransform:function(_5){
return _5?this.setTransform([this.matrix,_5]):this;
},applyLeftTransform:function(_6){
return _6?this.setTransform([_6,this.matrix]):this;
},applyTransform:function(_7){
return _7?this.setTransform([this.matrix,_7]):this;
},remove:function(_8){
if(this.parent){
this.parent.remove(this,_8);
}
return this;
},_setParent:function(_9,_a){
this.parent=_9;
return this._updateParentMatrix(_a);
},_updateParentMatrix:function(_b){
this.parentMatrix=_b?dojo.gfx.matrix.clone(_b):null;
return this._applyTransform();
},_getRealMatrix:function(){
return this.parentMatrix?new dojo.gfx.matrix.Matrix2D([this.parentMatrix,this.matrix]):this.matrix;
}});
dojo.declare("dojo.gfx.shape.VirtualGroup",dojo.gfx.Shape,{initializer:function(){
this.children=[];
},add:function(_c){
var _d=_c.getParent();
if(_d){
_d.remove(_c,true);
}
this.children.push(_c);
return _c._setParent(this,this._getRealMatrix());
},remove:function(_e,_f){
for(var i=0;i<this.children.length;++i){
if(this.children[i]==_e){
if(_f){
}else{
_e._setParent(null,null);
}
this.children.splice(i,1);
break;
}
}
return this;
},_applyTransform:function(){
var _11=this._getRealMatrix();
for(var i=0;i<this.children.length;++i){
this.children[i]._updateParentMatrix(_11);
}
return this;
}});
dojo.declare("dojo.gfx.shape.Rect",dojo.gfx.Shape,{initializer:function(_13){
this.shape=dojo.lang.shallowCopy(dojo.gfx.defaultRect,true);
this.attach(_13);
},getBoundingBox:function(){
return this.shape;
}});
dojo.declare("dojo.gfx.shape.Ellipse",dojo.gfx.Shape,{initializer:function(_14){
this.shape=dojo.lang.shallowCopy(dojo.gfx.defaultEllipse,true);
this.attach(_14);
},getBoundingBox:function(){
if(!this.bbox){
var _15=this.shape;
this.bbox={x:_15.cx-_15.rx,y:_15.cy-_15.ry,width:2*_15.rx,height:2*_15.ry};
}
return this.bbox;
}});
dojo.declare("dojo.gfx.shape.Circle",dojo.gfx.Shape,{initializer:function(_16){
this.shape=dojo.lang.shallowCopy(dojo.gfx.defaultCircle,true);
this.attach(_16);
},getBoundingBox:function(){
if(!this.bbox){
var _17=this.shape;
this.bbox={x:_17.cx-_17.r,y:_17.cy-_17.r,width:2*_17.r,height:2*_17.r};
}
return this.bbox;
}});
dojo.declare("dojo.gfx.shape.Line",dojo.gfx.Shape,{initializer:function(_18){
this.shape=dojo.lang.shallowCopy(dojo.gfx.defaultLine,true);
this.attach(_18);
},getBoundingBox:function(){
if(!this.bbox){
var _19=this.shape;
this.bbox={x:Math.min(_19.x1,_19.x2),y:Math.min(_19.y1,_19.y2),width:Math.abs(_19.x2-_19.x1),height:Math.abs(_19.y2-_19.y1)};
}
return this.bbox;
}});
dojo.declare("dojo.gfx.shape.Polyline",dojo.gfx.Shape,{initializer:function(_1a){
this.shape=dojo.lang.shallowCopy(dojo.gfx.defaultPolyline,true);
this.attach(_1a);
},getBoundingBox:function(){
if(!this.bbox&&this.shape.points.length){
var p=this.shape.points;
var l=p.length;
var t=p[0];
var _1e={l:t.x,t:t.y,r:t.x,b:t.y};
for(var i=1;i<l;++i){
t=p[i];
if(_1e.l>t.x){
_1e.l=t.x;
}
if(_1e.r<t.x){
_1e.r=t.x;
}
if(_1e.t>t.y){
_1e.t=t.y;
}
if(_1e.b<t.y){
_1e.b=t.y;
}
}
this.bbox={x:_1e.l,y:_1e.t,width:_1e.r-_1e.l,height:_1e.b-_1e.t};
}
return this.bbox;
}});
dojo.declare("dojo.gfx.shape.Image",dojo.gfx.Shape,{initializer:function(_20){
this.shape=dojo.lang.shallowCopy(dojo.gfx.defaultImage,true);
this.attach(_20);
},getBoundingBox:function(){
if(!this.bbox){
var _21=this.shape;
this.bbox={x:0,y:0,width:_21.width,height:_21.height};
}
return this.bbox;
}});
