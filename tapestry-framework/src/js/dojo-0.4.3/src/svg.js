dojo.provide("dojo.svg");
dojo.require("dojo.lang.common");
dojo.require("dojo.dom");
dojo.mixin(dojo.svg,dojo.dom);
dojo.svg.graphics=dojo.svg.g=new function(d){
this.suspend=function(){
try{
d.documentElement.suspendRedraw(0);
}
catch(e){
}
};
this.resume=function(){
try{
d.documentElement.unsuspendRedraw(0);
}
catch(e){
}
};
this.force=function(){
try{
d.documentElement.forceRedraw();
}
catch(e){
}
};
}(document);
dojo.svg.animations=dojo.svg.anim=new function(d){
this.arePaused=function(){
try{
return d.documentElement.animationsPaused();
}
catch(e){
return false;
}
};
this.pause=function(){
try{
d.documentElement.pauseAnimations();
}
catch(e){
}
};
this.resume=function(){
try{
d.documentElement.unpauseAnimations();
}
catch(e){
}
};
}(document);
dojo.svg.toCamelCase=function(_3){
var _4=_3.split("-"),cc=_4[0];
for(var i=1;i<_4.length;i++){
cc+=_4[i].charAt(0).toUpperCase()+_4[i].substring(1);
}
return cc;
};
dojo.svg.toSelectorCase=function(_7){
return _7.replace(/([A-Z])/g,"-$1").toLowerCase();
};
dojo.svg.getStyle=function(_8,_9){
return document.defaultView.getComputedStyle(_8,_9);
};
dojo.svg.getNumericStyle=function(_a,_b){
return parseFloat(dojo.svg.getStyle(_a,_b));
};
dojo.svg.getOpacity=function(_c){
return Math.min(1,dojo.svg.getNumericStyle(_c,"fill-opacity"));
};
dojo.svg.setOpacity=function(_d,_e){
_d.setAttributeNS(this.xmlns.svg,"fill-opacity",_e);
_d.setAttributeNS(this.xmlns.svg,"stroke-opacity",_e);
};
dojo.svg.clearOpacity=function(_f){
_f.setAttributeNS(this.xmlns.svg,"fill-opacity","1.0");
_f.setAttributeNS(this.xmlns.svg,"stroke-opacity","1.0");
};
dojo.svg.getCoords=function(_10){
if(_10.getBBox){
var box=_10.getBBox();
return {x:box.x,y:box.y};
}
return null;
};
dojo.svg.setCoords=function(_12,_13){
var p=dojo.svg.getCoords();
if(!p){
return;
}
var dx=p.x-_13.x;
var dy=p.y-_13.y;
dojo.svg.translate(_12,dx,dy);
};
dojo.svg.getDimensions=function(_17){
if(_17.getBBox){
var box=_17.getBBox();
return {width:box.width,height:box.height};
}
return null;
};
dojo.svg.setDimensions=function(_19,dim){
if(_19.width){
_19.width.baseVal.value=dim.width;
_19.height.baseVal.value=dim.height;
}else{
if(_19.r){
_19.r.baseVal.value=Math.min(dim.width,dim.height)/2;
}else{
if(_19.rx){
_19.rx.baseVal.value=dim.width/2;
_19.ry.baseVal.value=dim.height/2;
}
}
}
};
dojo.svg.translate=function(_1b,dx,dy){
if(_1b.transform&&_1b.ownerSVGElement&&_1b.ownerSVGElement.createSVGTransform){
var t=_1b.ownerSVGElement.createSVGTransform();
t.setTranslate(dx,dy);
_1b.transform.baseVal.appendItem(t);
}
};
dojo.svg.scale=function(_1f,_20,_21){
if(!_21){
var _21=_20;
}
if(_1f.transform&&_1f.ownerSVGElement&&_1f.ownerSVGElement.createSVGTransform){
var t=_1f.ownerSVGElement.createSVGTransform();
t.setScale(_20,_21);
_1f.transform.baseVal.appendItem(t);
}
};
dojo.svg.rotate=function(_23,ang,cx,cy){
if(_23.transform&&_23.ownerSVGElement&&_23.ownerSVGElement.createSVGTransform){
var t=_23.ownerSVGElement.createSVGTransform();
if(cx==null){
t.setMatrix(t.matrix.rotate(ang));
}else{
t.setRotate(ang,cx,cy);
}
_23.transform.baseVal.appendItem(t);
}
};
dojo.svg.skew=function(_28,ang,_2a){
var dir=_2a||"x";
if(_28.transform&&_28.ownerSVGElement&&_28.ownerSVGElement.createSVGTransform){
var t=_28.ownerSVGElement.createSVGTransform();
if(dir!="x"){
t.setSkewY(ang);
}else{
t.setSkewX(ang);
}
_28.transform.baseVal.appendItem(t);
}
};
dojo.svg.flip=function(_2d,_2e){
var dir=_2e||"x";
if(_2d.transform&&_2d.ownerSVGElement&&_2d.ownerSVGElement.createSVGTransform){
var t=_2d.ownerSVGElement.createSVGTransform();
t.setMatrix((dir!="x")?t.matrix.flipY():t.matrix.flipX());
_2d.transform.baseVal.appendItem(t);
}
};
dojo.svg.invert=function(_31){
if(_31.transform&&_31.ownerSVGElement&&_31.ownerSVGElement.createSVGTransform){
var t=_31.ownerSVGElement.createSVGTransform();
t.setMatrix(t.matrix.inverse());
_31.transform.baseVal.appendItem(t);
}
};
dojo.svg.applyMatrix=function(_33,a,b,c,d,e,f){
if(_33.transform&&_33.ownerSVGElement&&_33.ownerSVGElement.createSVGTransform){
var m;
if(b){
var m=_33.ownerSVGElement.createSVGMatrix();
m.a=a;
m.b=b;
m.c=c;
m.d=d;
m.e=e;
m.f=f;
}else{
m=a;
}
var t=_33.ownerSVGElement.createSVGTransform();
t.setMatrix(m);
_33.transform.baseVal.appendItem(t);
}
};
dojo.svg.group=function(_3c){
var p=_3c.item(0).parentNode;
var g=document.createElementNS(this.xmlns.svg,"g");
for(var i=0;i<_3c.length;i++){
g.appendChild(_3c.item(i));
}
p.appendChild(g);
return g;
};
dojo.svg.ungroup=function(g){
var p=g.parentNode;
while(g.childNodes.length>0){
p.appendChild(g.childNodes.item(0));
}
p.removeChild(g);
};
dojo.svg.getGroup=function(_42){
var a=this.getAncestors(_42);
for(var i=0;i<a.length;i++){
if(a[i].nodeType==this.ELEMENT_NODE&&a[i].nodeName.toLowerCase()=="g"){
return a[i];
}
}
return null;
};
dojo.svg.bringToFront=function(_45){
var n=this.getGroup(_45)||_45;
n.ownerSVGElement.appendChild(n);
};
dojo.svg.sendToBack=function(_47){
var n=this.getGroup(_47)||_47;
n.ownerSVGElement.insertBefore(n,n.ownerSVGElement.firstChild);
};
dojo.svg.bringForward=function(_49){
var n=this.getGroup(_49)||_49;
if(this.getLastChildElement(n.parentNode)!=n){
this.insertAfter(n,this.getNextSiblingElement(n),true);
}
};
dojo.svg.sendBackward=function(_4b){
var n=this.getGroup(_4b)||_4b;
if(this.getFirstChildElement(n.parentNode)!=n){
this.insertBefore(n,this.getPreviousSiblingElement(n),true);
}
};
dojo.svg.createNodesFromText=function(txt,_4e){
var _4f=(new DOMParser()).parseFromString(txt,"text/xml").normalize();
if(_4e){
return [_4f.firstChild.cloneNode(true)];
}
var _50=[];
for(var x=0;x<_4f.childNodes.length;x++){
_50.push(_4f.childNodes.item(x).cloneNode(true));
}
return _50;
};
