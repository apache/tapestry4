dojo.provide("dojo.gfx.vml");
dojo.require("dojo.dom");
dojo.require("dojo.math");
dojo.require("dojo.lang.declare");
dojo.require("dojo.lang.extras");
dojo.require("dojo.string.*");
dojo.require("dojo.html.metrics");
dojo.require("dojo.gfx.color");
dojo.require("dojo.gfx.common");
dojo.require("dojo.gfx.shape");
dojo.require("dojo.gfx.path");
dojo.require("dojo.experimental");
dojo.experimental("dojo.gfx.vml");
dojo.gfx.vml.xmlns="urn:schemas-microsoft-com:vml";
dojo.gfx.vml._parseFloat=function(_1){
return _1.match(/^\d+f$/i)?parseInt(_1)/65536:parseFloat(_1);
};
dojo.gfx.vml.cm_in_pt=72/2.54;
dojo.gfx.vml.mm_in_pt=7.2/2.54;
dojo.gfx.vml.px_in_pt=function(){
return dojo.html.getCachedFontMeasurements()["12pt"]/12;
};
dojo.gfx.vml.pt2px=function(_2){
return _2*this.px_in_pt();
};
dojo.gfx.vml.px2pt=function(_3){
return _3/this.px_in_pt();
};
dojo.gfx.vml.normalizedLength=function(_4){
if(_4.length==0){
return 0;
}
if(_4.length>2){
var _5=this.px_in_pt();
var _6=parseFloat(_4);
switch(_4.slice(-2)){
case "px":
return _6;
case "pt":
return _6*_5;
case "in":
return _6*72*_5;
case "pc":
return _6*12*_5;
case "mm":
return _6/this.mm_in_pt*_5;
case "cm":
return _6/this.cm_in_pt*_5;
}
}
return parseFloat(_4);
};
dojo.lang.extend(dojo.gfx.Shape,{setFill:function(_7){
if(!_7){
this.fillStyle=null;
this.rawNode.filled=false;
return this;
}
if(typeof (_7)=="object"&&"type" in _7){
switch(_7.type){
case "linear":
var f=dojo.gfx.makeParameters(dojo.gfx.defaultLinearGradient,_7);
this.fillStyle=f;
var s="";
for(var i=0;i<f.colors.length;++i){
f.colors[i].color=dojo.gfx.normalizeColor(f.colors[i].color);
s+=f.colors[i].offset.toFixed(8)+" "+f.colors[i].color.toHex()+";";
}
var fo=this.rawNode.fill;
fo.colors.value=s;
fo.method="sigma";
fo.type="gradient";
fo.angle=(dojo.math.radToDeg(Math.atan2(f.x2-f.x1,f.y2-f.y1))+180)%360;
fo.on=true;
break;
case "radial":
var f=dojo.gfx.makeParameters(dojo.gfx.defaultRadialGradient,_7);
this.fillStyle=f;
var w=parseFloat(this.rawNode.style.width);
var h=parseFloat(this.rawNode.style.height);
var c=isNaN(w)?1:2*f.r/w;
var i=f.colors.length-1;
f.colors[i].color=dojo.gfx.normalizeColor(f.colors[i].color);
var s="0 "+f.colors[i].color.toHex();
for(;i>=0;--i){
f.colors[i].color=dojo.gfx.normalizeColor(f.colors[i].color);
s+=(1-c*f.colors[i].offset).toFixed(8)+" "+f.colors[i].color.toHex()+";";
}
var fo=this.rawNode.fill;
fo.colors.value=s;
fo.method="sigma";
fo.type="gradientradial";
if(isNaN(w)||isNaN(h)){
fo.focusposition="0.5 0.5";
}else{
fo.focusposition=(f.cx/w).toFixed(8)+" "+(f.cy/h).toFixed(8);
}
fo.focussize="0 0";
fo.on=true;
break;
case "pattern":
var f=dojo.gfx.makeParameters(dojo.gfx.defaultPattern,_7);
this.fillStyle=f;
var fo=this.rawNode.fill;
fo.type="tile";
fo.src=f.src;
if(f.width&&f.height){
fo.size.x=dojo.gfx.vml.px2pt(f.width);
fo.size.y=dojo.gfx.vml.px2pt(f.height);
}
fo.alignShape=false;
fo.position.x=0;
fo.position.y=0;
fo.origin.x=f.width?f.x/f.width:0;
fo.origin.y=f.height?f.y/f.height:0;
fo.on=true;
break;
}
this.rawNode.fill.opacity=1;
return this;
}
this.fillStyle=dojo.gfx.normalizeColor(_7);
this.rawNode.fillcolor=this.fillStyle.toHex();
this.rawNode.fill.opacity=this.fillStyle.a;
this.rawNode.filled=true;
return this;
},setStroke:function(_f){
if(!_f){
this.strokeStyle=null;
this.rawNode.stroked=false;
return this;
}
this.strokeStyle=dojo.gfx.makeParameters(dojo.gfx.defaultStroke,_f);
this.strokeStyle.color=dojo.gfx.normalizeColor(this.strokeStyle.color);
var s=this.strokeStyle;
this.rawNode.stroked=true;
this.rawNode.strokecolor=s.color.toCss();
this.rawNode.strokeweight=s.width+"px";
if(this.rawNode.stroke){
this.rawNode.stroke.opacity=s.color.a;
this.rawNode.stroke.endcap=this._translate(this._capMap,s.cap);
if(typeof (s.join)=="number"){
this.rawNode.stroke.joinstyle="miter";
this.rawNode.stroke.miterlimit=s.join;
}else{
this.rawNode.stroke.joinstyle=s.join;
}
}
return this;
},_capMap:{butt:"flat"},_capMapReversed:{flat:"butt"},_translate:function(_11,_12){
return (_12 in _11)?_11[_12]:_12;
},_applyTransform:function(){
var _13=this._getRealMatrix();
if(!_13){
return this;
}
var _14=this.rawNode.skew;
if(typeof (_14)=="undefined"){
for(var i=0;i<this.rawNode.childNodes.length;++i){
if(this.rawNode.childNodes[i].tagName=="skew"){
_14=this.rawNode.childNodes[i];
break;
}
}
}
if(_14){
_14.on=false;
var mt=_13.xx.toFixed(8)+" "+_13.xy.toFixed(8)+" "+_13.yx.toFixed(8)+" "+_13.yy.toFixed(8)+" 0 0";
var _17=Math.floor(_13.dx).toFixed()+"px "+Math.floor(_13.dy).toFixed()+"px";
var l=parseFloat(this.rawNode.style.left);
var t=parseFloat(this.rawNode.style.top);
var w=parseFloat(this.rawNode.style.width);
var h=parseFloat(this.rawNode.style.height);
if(isNaN(l)){
l=0;
}
if(isNaN(t)){
t=0;
}
if(isNaN(w)){
w=1;
}
if(isNaN(h)){
h=1;
}
var _1c=(-l/w-0.5).toFixed(8)+" "+(-t/h-0.5).toFixed(8);
_14.matrix=mt;
_14.origin=_1c;
_14.offset=_17;
_14.on=true;
}
return this;
},setRawNode:function(_1d){
_1d.stroked=false;
_1d.filled=false;
this.rawNode=_1d;
},attachFill:function(_1e){
var _1f=null;
var fo=_1e.fill;
if(_1e){
if(fo.on&&fo.type=="gradient"){
var _1f=dojo.lang.shallowCopy(dojo.gfx.defaultLinearGradient,true);
var rad=dojo.math.degToRad(fo.angle);
_1f.x2=Math.cos(rad);
_1f.y2=Math.sin(rad);
_1f.colors=[];
var _22=fo.colors.value.split(";");
for(var i=0;i<_22.length;++i){
var t=_22[i].match(/\S+/g);
if(!t||t.length!=2){
continue;
}
_1f.colors.push({offset:dojo.gfx.vml._parseFloat(t[0]),color:new dojo.gfx.color.Color(t[1])});
}
}else{
if(fo.on&&fo.type=="gradientradial"){
var _1f=dojo.lang.shallowCopy(dojo.gfx.defaultRadialGradient,true);
var w=parseFloat(_1e.style.width);
var h=parseFloat(_1e.style.height);
_1f.cx=isNaN(w)?0:fo.focusposition.x*w;
_1f.cy=isNaN(h)?0:fo.focusposition.y*h;
_1f.r=isNaN(w)?1:w/2;
_1f.colors=[];
var _22=fo.colors.value.split(";");
for(var i=_22.length-1;i>=0;--i){
var t=_22[i].match(/\S+/g);
if(!t||t.length!=2){
continue;
}
_1f.colors.push({offset:dojo.gfx.vml._parseFloat(t[0]),color:new dojo.gfx.color.Color(t[1])});
}
}else{
if(fo.on&&fo.type=="tile"){
var _1f=dojo.lang.shallowCopy(dojo.gfx.defaultPattern,true);
_1f.width=dojo.gfx.vml.pt2px(fo.size.x);
_1f.height=dojo.gfx.vml.pt2px(fo.size.y);
_1f.x=fo.origin.x*_1f.width;
_1f.y=fo.origin.y*_1f.height;
_1f.src=fo.src;
}else{
if(fo.on&&_1e.fillcolor){
_1f=new dojo.gfx.color.Color(_1e.fillcolor+"");
_1f.a=fo.opacity;
}
}
}
}
}
return _1f;
},attachStroke:function(_27){
var _28=dojo.lang.shallowCopy(dojo.gfx.defaultStroke,true);
if(_27&&_27.stroked){
_28.color=new dojo.gfx.color.Color(_27.strokecolor.value);
dojo.debug("We are expecting an .75pt here, instead of strokeweight = "+_27.strokeweight);
_28.width=dojo.gfx.vml.normalizedLength(_27.strokeweight+"");
_28.color.a=_27.stroke.opacity;
_28.cap=this._translate(this._capMapReversed,_27.stroke.endcap);
_28.join=_27.stroke.joinstyle=="miter"?_27.stroke.miterlimit:_27.stroke.joinstyle;
}else{
return null;
}
return _28;
},attachTransform:function(_29){
var _2a={};
if(_29){
var s=_29.skew;
_2a.xx=s.matrix.xtox;
_2a.xy=s.matrix.ytox;
_2a.yx=s.matrix.xtoy;
_2a.yy=s.matrix.ytoy;
_2a.dx=dojo.gfx.vml.pt2px(s.offset.x);
_2a.dy=dojo.gfx.vml.pt2px(s.offset.y);
}
return dojo.gfx.matrix.normalize(_2a);
},attach:function(_2c){
if(_2c){
this.rawNode=_2c;
this.shape=this.attachShape(_2c);
this.fillStyle=this.attachFill(_2c);
this.strokeStyle=this.attachStroke(_2c);
this.matrix=this.attachTransform(_2c);
}
}});
dojo.declare("dojo.gfx.Group",dojo.gfx.shape.VirtualGroup,{add:function(_2d){
if(this!=_2d.getParent()){
this.rawNode.appendChild(_2d.rawNode);
dojo.gfx.Group.superclass.add.apply(this,arguments);
}
return this;
},remove:function(_2e,_2f){
if(this==_2e.getParent()){
if(this.rawNode==_2e.rawNode.parentNode){
this.rawNode.removeChild(_2e.rawNode);
}
dojo.gfx.Group.superclass.remove.apply(this,arguments);
}
return this;
},attach:function(_30){
if(_30){
this.rawNode=_30;
this.shape=null;
this.fillStyle=null;
this.strokeStyle=null;
this.matrix=null;
}
}});
dojo.gfx.Group.nodeType="group";
var zIndex={moveToFront:function(){
this.rawNode.parentNode.appendChild(this.rawNode);
return this;
},moveToBack:function(){
this.rawNode.parentNode.insertBefore(this.rawNode,this.rawNode.parentNode.firstChild);
return this;
}};
dojo.lang.extend(dojo.gfx.Shape,zIndex);
dojo.lang.extend(dojo.gfx.Group,zIndex);
delete zIndex;
dojo.declare("dojo.gfx.Rect",dojo.gfx.shape.Rect,{attachShape:function(_31){
var _32=_31.outerHTML.match(/arcsize = \"(\d*\.?\d+[%f]?)\"/)[1];
_32=(_32.indexOf("%")>=0)?parseFloat(_32)/100:dojo.gfx.vml._parseFloat(_32);
var _33=_31.style;
var _34=parseFloat(_33.width);
var _35=parseFloat(_33.height);
var o=dojo.gfx.makeParameters(dojo.gfx.defaultRect,{x:parseInt(_33.left),y:parseInt(_33.top),width:_34,height:_35,r:Math.min(_34,_35)*_32});
return o;
},setShape:function(_37){
var _38=this.shape=dojo.gfx.makeParameters(this.shape,_37);
this.bbox=null;
var _39=this.rawNode.style;
_39.left=_38.x.toFixed();
_39.top=_38.y.toFixed();
_39.width=(typeof (_38.width)=="string"&&_38.width.indexOf("%")>=0)?_38.width:_38.width.toFixed();
_39.height=(typeof (_38.width)=="string"&&_38.height.indexOf("%")>=0)?_38.height:_38.height.toFixed();
var r=Math.min(1,(_38.r/Math.min(parseFloat(_38.width),parseFloat(_38.height)))).toFixed(8);
var _3b=this.rawNode.parentNode;
var _3c=null;
if(_3b){
if(_3b.lastChild!=this.rawNode){
for(var i=0;i<_3b.childNodes.length;++i){
if(_3b.childNodes[i]==this.rawNode){
_3c=_3b.childNodes[i+1];
break;
}
}
}
_3b.removeChild(this.rawNode);
}
this.rawNode.arcsize=r;
if(_3b){
if(_3c){
_3b.insertBefore(this.rawNode,_3c);
}else{
_3b.appendChild(this.rawNode);
}
}
return this.setTransform(this.matrix);
}});
dojo.gfx.Rect.nodeType="roundrect";
dojo.declare("dojo.gfx.Ellipse",dojo.gfx.shape.Ellipse,{attachShape:function(_3e){
var _3f=this.rawNode.style;
var rx=parseInt(_3f.width)/2;
var ry=parseInt(_3f.height)/2;
var o=dojo.gfx.makeParameters(dojo.gfx.defaultEllipse,{cx:parseInt(_3f.left)+rx,cy:parseInt(_3f.top)+ry,rx:rx,ry:ry});
return o;
},setShape:function(_43){
var _44=this.shape=dojo.gfx.makeParameters(this.shape,_43);
this.bbox=null;
var _45=this.rawNode.style;
_45.left=(_44.cx-_44.rx).toFixed();
_45.top=(_44.cy-_44.ry).toFixed();
_45.width=(_44.rx*2).toFixed();
_45.height=(_44.ry*2).toFixed();
return this.setTransform(this.matrix);
}});
dojo.gfx.Ellipse.nodeType="oval";
dojo.declare("dojo.gfx.Circle",dojo.gfx.shape.Circle,{attachShape:function(_46){
var _47=this.rawNode.style;
var r=parseInt(_47.width)/2;
var o=dojo.gfx.makeParameters(dojo.gfx.defaultCircle,{cx:parseInt(_47.left)+r,cy:parseInt(_47.top)+r,r:r});
return o;
},setShape:function(_4a){
var _4b=this.shape=dojo.gfx.makeParameters(this.shape,_4a);
this.bbox=null;
var _4c=this.rawNode.style;
_4c.left=(_4b.cx-_4b.r).toFixed();
_4c.top=(_4b.cy-_4b.r).toFixed();
_4c.width=(_4b.r*2).toFixed();
_4c.height=(_4b.r*2).toFixed();
return this;
}});
dojo.gfx.Circle.nodeType="oval";
dojo.declare("dojo.gfx.Line",dojo.gfx.shape.Line,function(_4d){
if(_4d){
_4d.setAttribute("dojoGfxType","line");
}
},{attachShape:function(_4e){
var p=_4e.path.v.match(dojo.gfx.pathRegExp);
var _50={};
do{
if(p.length<7||p[0]!="m"||p[3]!="l"||p[6]!="e"){
break;
}
_50.x1=parseInt(p[1]);
_50.y1=parseInt(p[2]);
_50.x2=parseInt(p[4]);
_50.y2=parseInt(p[5]);
}while(false);
return dojo.gfx.makeParameters(dojo.gfx.defaultLine,_50);
},setShape:function(_51){
var _52=this.shape=dojo.gfx.makeParameters(this.shape,_51);
this.bbox=null;
this.rawNode.path.v="m"+_52.x1.toFixed()+" "+_52.y1.toFixed()+"l"+_52.x2.toFixed()+" "+_52.y2.toFixed()+"e";
return this.setTransform(this.matrix);
}});
dojo.gfx.Line.nodeType="shape";
dojo.declare("dojo.gfx.Polyline",dojo.gfx.shape.Polyline,function(_53){
if(_53){
_53.setAttribute("dojoGfxType","polyline");
}
},{attachShape:function(_54){
var _55=dojo.lang.shallowCopy(dojo.gfx.defaultPolyline,true);
var p=_54.path.v.match(dojo.gfx.pathRegExp);
do{
if(p.length<3||p[0]!="m"){
break;
}
var x=parseInt(p[0]);
var y=parseInt(p[1]);
if(isNaN(x)||isNaN(y)){
break;
}
_55.points.push({x:x,y:y});
if(p.length<6||p[3]!="l"){
break;
}
for(var i=4;i<p.length;i+=2){
x=parseInt(p[i]);
y=parseInt(p[i+1]);
if(isNaN(x)||isNaN(y)){
break;
}
_55.points.push({x:x,y:y});
}
}while(false);
return _55;
},setShape:function(_5a,_5b){
if(_5a&&_5a instanceof Array){
this.shape=dojo.gfx.makeParameters(this.shape,{points:_5a});
if(_5b&&this.shape.points.length){
this.shape.points.push(this.shape.points[0]);
}
}else{
this.shape=dojo.gfx.makeParameters(this.shape,_5a);
}
this.bbox=null;
var _5c=[];
var p=this.shape.points;
if(p.length>0){
_5c.push("m");
_5c.push(p[0].x.toFixed());
_5c.push(p[0].y.toFixed());
if(p.length>1){
_5c.push("l");
for(var i=1;i<p.length;++i){
_5c.push(p[i].x.toFixed());
_5c.push(p[i].y.toFixed());
}
}
}
_5c.push("e");
this.rawNode.path.v=_5c.join(" ");
return this.setTransform(this.matrix);
}});
dojo.gfx.Polyline.nodeType="shape";
dojo.declare("dojo.gfx.Image",dojo.gfx.shape.Image,{getEventSource:function(){
return this.rawNode?this.rawNode.firstChild:null;
},attachShape:function(_5f){
var _60=dojo.lang.shallowCopy(dojo.gfx.defaultImage,true);
_60.src=_5f.firstChild.src;
return _60;
},setShape:function(_61){
var _62=this.shape=dojo.gfx.makeParameters(this.shape,_61);
this.bbox=null;
var _63=this.rawNode.firstChild;
_63.src=_62.src;
if(_62.width||_62.height){
_63.style.width=_62.width;
_63.style.height=_62.height;
}
return this.setTransform(this.matrix);
},setStroke:function(){
return this;
},setFill:function(){
return this;
},attachStroke:function(_64){
return null;
},attachFill:function(_65){
return null;
},attachTransform:function(_66){
var _67={};
if(_66){
var m=_66.filters["DXImageTransform.Microsoft.Matrix"];
_67.xx=m.M11;
_67.xy=m.M12;
_67.yx=m.M21;
_67.yy=m.M22;
_67.dx=m.Dx;
_67.dy=m.Dy;
}
return dojo.gfx.matrix.normalize(_67);
},_applyTransform:function(){
var _69=this._getRealMatrix();
if(!_69){
return this;
}
with(this.rawNode.filters["DXImageTransform.Microsoft.Matrix"]){
M11=_69.xx;
M12=_69.xy;
M21=_69.yx;
M22=_69.yy;
Dx=_69.dx;
Dy=_69.dy;
}
return this;
}});
dojo.gfx.Image.nodeType="image";
dojo.gfx.path._calcArc=function(_6a){
var _6b=Math.cos(_6a);
var _6c=Math.sin(_6a);
var p2={x:_6b+(4/3)*(1-_6b),y:_6c-(4/3)*_6b*(1-_6b)/_6c};
return {s:{x:_6b,y:_6c},c1:p2,c2:{x:p2.x,y:-p2.y},e:{x:_6b,y:-_6c}};
};
dojo.declare("dojo.gfx.Path",dojo.gfx.path.Path,function(_6e){
if(_6e){
_6e.setAttribute("dojoGfxType","path");
}
this.vmlPath="";
this.lastControl={};
},{_updateWithSegment:function(_6f){
var _70=dojo.lang.shallowCopy(this.last);
dojo.gfx.Path.superclass._updateWithSegment.apply(this,arguments);
var _71=this[this.renderers[_6f.action]](_6f,_70);
if(typeof (this.vmlPath)=="string"){
this.vmlPath+=_71.join("");
}else{
this.vmlPath=this.vmlPath.concat(_71);
}
if(typeof (this.vmlPath)=="string"){
this.rawNode.path.v=this.vmlPath+" e";
}
},attachShape:function(_72){
var _73=dojo.lang.shallowCopy(dojo.gfx.defaultPath,true);
var p=_72.path.v.match(dojo.gfx.pathRegExp);
var t=[],_76=false;
for(var i=0;i<p.length;++p){
var s=p[i];
if(s in this._pathVmlToSvgMap){
_76=false;
t.push(this._pathVmlToSvgMap[s]);
}else{
if(!_76){
var n=parseInt(s);
if(isNaN(n)){
_76=true;
}else{
t.push(n);
}
}
}
}
if(t.length){
_73.path=t.join(" ");
}
return _73;
},setShape:function(_7a){
this.vmlPath=[];
this.lastControl={};
dojo.gfx.Path.superclass.setShape.apply(this,arguments);
this.vmlPath=this.vmlPath.join("");
this.rawNode.path.v=this.vmlPath+" e";
return this;
},_pathVmlToSvgMap:{m:"M",l:"L",t:"m",r:"l",c:"C",v:"c",qb:"Q",x:"z",e:""},renderers:{M:"_moveToA",m:"_moveToR",L:"_lineToA",l:"_lineToR",H:"_hLineToA",h:"_hLineToR",V:"_vLineToA",v:"_vLineToR",C:"_curveToA",c:"_curveToR",S:"_smoothCurveToA",s:"_smoothCurveToR",Q:"_qCurveToA",q:"_qCurveToR",T:"_qSmoothCurveToA",t:"_qSmoothCurveToR",A:"_arcTo",a:"_arcTo",Z:"_closePath",z:"_closePath"},_addArgs:function(_7b,_7c,_7d,_7e){
if(typeof (_7e)=="undefined"){
_7e=_7c.length;
}
if(typeof (_7d)=="undefined"){
_7d=0;
}
for(var i=_7d;i<_7e;++i){
_7b.push(" ");
_7b.push(_7c[i].toFixed());
}
},_addArgsAdjusted:function(_80,_81,_82,_83,_84){
if(typeof (_84)=="undefined"){
_84=_82.length;
}
if(typeof (_83)=="undefined"){
_83=0;
}
for(var i=_83;i<_84;i+=2){
_80.push(" ");
_80.push((_81.x+_82[i]).toFixed());
_80.push(" ");
_80.push((_81.y+_82[i+1]).toFixed());
}
},_moveToA:function(_86){
var p=[" m"];
var n=_86.args;
var l=n.length;
if(l==2){
this._addArgs(p,n);
}else{
this._addArgs(p,n,0,2);
p.push(" l");
this._addArgs(p,n,2);
}
this.lastControl={};
return p;
},_moveToR:function(_8a,_8b){
var p=["x" in _8b?" t":" m"];
var n=_8a.args;
var l=n.length;
if(l==2){
this._addArgs(p,n);
}else{
this._addArgs(p,n,0,2);
p.push(" r");
this._addArgs(p,n,2);
}
this.lastControl={};
return p;
},_lineToA:function(_8f){
var p=[" l"];
this._addArgs(p,_8f.args);
this.lastControl={};
return p;
},_lineToR:function(_91){
var p=[" r"];
this._addArgs(p,_91.args);
this.lastControl={};
return p;
},_hLineToA:function(_93,_94){
var p=[" l"];
var n=_93.args;
var l=n.length;
var y=" "+_94.y.toFixed();
for(var i=0;i<l;++i){
p.push(" ");
p.push(n[i].toFixed());
p.push(y);
}
this.lastControl={};
return p;
},_hLineToR:function(_9a){
var p=[" r"];
var n=_9a.args;
var l=n.length;
for(var i=0;i<l;++i){
p.push(" ");
p.push(n[i].toFixed());
p.push(" 0");
}
this.lastControl={};
return p;
},_vLineToA:function(_9f,_a0){
var p=[" l"];
var n=_9f.args;
var l=n.length;
var x=" "+_a0.x.toFixed();
for(var i=0;i<l;++i){
p.push(x);
p.push(" ");
p.push(n[i].toFixed());
}
this.lastControl={};
return p;
},_vLineToR:function(_a6){
var p=[" r"];
var n=_a6.args;
var l=n.length;
for(var i=0;i<l;++i){
p.push(" 0 ");
p.push(n[i].toFixed());
}
this.lastControl={};
return p;
},_curveToA:function(_ab){
var p=[];
var n=_ab.args;
var l=n.length;
for(var i=0;i<l;i+=6){
p.push(" c");
this._addArgs(p,n,i,i+6);
}
this.lastControl={x:n[l-4],y:n[l-3],type:"C"};
return p;
},_curveToR:function(_b0,_b1){
var p=[];
var n=_b0.args;
var l=n.length;
for(var i=0;i<l;i+=6){
p.push(" v");
this._addArgs(p,n,i,i+6);
this.lastControl={x:_b1.x+n[i+2],y:_b1.y+n[i+3]};
_b1.x+=n[i+4];
_b1.y+=n[i+5];
}
this.lastControl.type="C";
return p;
},_smoothCurveToA:function(_b6,_b7){
var p=[];
var n=_b6.args;
var l=n.length;
for(var i=0;i<l;i+=4){
p.push(" c");
if(this.lastControl.type=="C"){
this._addArgs(p,[2*_b7.x-this.lastControl.x,2*_b7.y-this.lastControl.y]);
}else{
this._addArgs(p,[_b7.x,_b7.y]);
}
this._addArgs(p,n,i,i+4);
}
this.lastControl={x:n[l-4],y:n[l-3],type:"C"};
return p;
},_smoothCurveToR:function(_bc,_bd){
var p=[];
var n=_bc.args;
var l=n.length;
for(var i=0;i<l;i+=4){
p.push(" v");
if(this.lastControl.type=="C"){
this._addArgs(p,[_bd.x-this.lastControl.x,_bd.y-this.lastControl.y]);
}else{
this._addArgs(p,[0,0]);
}
this._addArgs(p,n,i,i+4);
this.lastControl={x:_bd.x+n[i],y:_bd.y+n[i+1]};
_bd.x+=n[i+2];
_bd.y+=n[i+3];
}
this.lastControl.type="C";
return p;
},_qCurveToA:function(_c2){
var p=[];
var n=_c2.args;
var l=n.length;
for(var i=0;i<l;i+=4){
p.push(" qb");
this._addArgs(p,n,i,i+4);
}
this.lastControl={x:n[l-4],y:n[l-3],type:"Q"};
return p;
},_qCurveToR:function(_c7,_c8){
var p=[];
var n=_c7.args;
var l=n.length;
for(var i=0;i<l;i+=4){
p.push(" qb");
this._addArgsAdjusted(p,_c8,n,i,i+4);
this.lastControl={x:_c8.x+n[i],y:_c8.y+n[i+1]};
_c8.x+=n[i+2];
_c8.y+=n[i+3];
}
this.lastControl.type="Q";
return p;
},_qSmoothCurveToA:function(_cd,_ce){
var p=[];
var n=_cd.args;
var l=n.length;
for(var i=0;i<l;i+=2){
p.push(" qb");
if(this.lastControl.type=="Q"){
this._addArgs(p,[this.lastControl.x=2*_ce.x-this.lastControl.x,this.lastControl.y=2*_ce.y-this.lastControl.y]);
}else{
this._addArgs(p,[this.lastControl.x=_ce.x,this.lastControl.y=_ce.y]);
}
this._addArgs(p,n,i,i+2);
}
this.lastControl.type="Q";
return p;
},_qSmoothCurveToR:function(_d3,_d4){
var p=[];
var n=_d3.args;
var l=n.length;
for(var i=0;i<l;i+=2){
p.push(" qb");
if(this.lastControl.type=="Q"){
this._addArgs(p,[this.lastControl.x=2*_d4.x-this.lastControl.x,this.lastControl.y=2*_d4.y-this.lastControl.y]);
}else{
this._addArgs(p,[this.lastControl.x=_d4.x,this.lastControl.y=_d4.y]);
}
this._addArgsAdjusted(p,_d4,n,i,i+2);
}
this.lastControl.type="Q";
return p;
},_PI4:Math.PI/4,_curvePI4:dojo.gfx.path._calcArc(Math.PI/8),_calcArcTo:function(_d9,_da,rx,ry,_dd,_de,cw,x,y){
var m=dojo.gfx.matrix;
var _e3=-dojo.math.degToRad(_dd);
var rx2=rx*rx;
var ry2=ry*ry;
var pa=m.multiplyPoint(m.rotate(-_e3),{x:(_da.x-x)/2,y:(_da.y-y)/2});
var _e7=pa.x*pa.x;
var _e8=pa.y*pa.y;
var c1=Math.sqrt((rx2*ry2-rx2*_e8-ry2*_e7)/(rx2*_e8+ry2*_e7));
var ca={x:c1*rx*pa.y/ry,y:-c1*ry*pa.x/rx};
if(_de==cw){
ca={x:-ca.x,y:-ca.y};
}
var c=m.multiplyPoint([m.translate((_da.x+x)/2,(_da.y+y)/2),m.rotate(_e3)],ca);
var _ec=Math.atan2(c.y-_da.y,_da.x-c.x)-_e3;
var _ed=Math.atan2(c.y-y,x-c.x)-_e3;
var _ee=cw?_ec-_ed:_ed-_ec;
if(_ee<0){
_ee+=this._2PI;
}else{
if(_ee>this._2PI){
_ee=this._2PI;
}
}
var _ef=m.normalize([m.translate(c.x,c.y),m.rotate(_e3),m.scale(rx,ry)]);
var _f0=this._PI4/2;
var _f1=this._curvePI4;
var _f2=cw?-_f0:_f0;
for(var _f3=_ee;_f3>0;_f3-=this._PI4){
if(_f3<this._PI4){
_f0=_f3/2;
_f1=dojo.gfx.path._calcArc(_f0);
_f2=cw?-_f0:_f0;
}
var c1,c2,e;
var M=m.normalize([_ef,m.rotate(_ec+_f2)]);
if(cw){
c1=m.multiplyPoint(M,_f1.c2);
c2=m.multiplyPoint(M,_f1.c1);
e=m.multiplyPoint(M,_f1.s);
}else{
c1=m.multiplyPoint(M,_f1.c1);
c2=m.multiplyPoint(M,_f1.c2);
e=m.multiplyPoint(M,_f1.e);
}
_d9.push(" c");
this._addArgs(_d9,[c1.x,c1.y,c2.x,c2.y,e.x,e.y]);
_ec+=2*_f2;
}
},_arcTo:function(_f7,_f8){
var p=[];
var n=_f7.args;
var l=n.length;
var _fc=_f7.action=="a";
for(var i=0;i<l;i+=7){
var x1=n[i+5];
var y1=n[i+6];
if(_fc){
x1+=_f8.x;
y1+=_f8.y;
}
this._calcArcTo(p,_f8,n[i],n[i+1],n[i+2],n[i+3]?1:0,n[i+4]?1:0,x1,y1);
_f8={x:x1,y:y1};
}
this.lastControl={};
return p;
},_closePath:function(){
this.lastControl={};
return ["x"];
}});
dojo.gfx.Path.nodeType="shape";
dojo.gfx._creators={createPath:function(path){
return this.createObject(dojo.gfx.Path,path,true);
},createRect:function(rect){
return this.createObject(dojo.gfx.Rect,rect);
},createCircle:function(_102){
return this.createObject(dojo.gfx.Circle,_102);
},createEllipse:function(_103){
return this.createObject(dojo.gfx.Ellipse,_103);
},createLine:function(line){
return this.createObject(dojo.gfx.Line,line,true);
},createPolyline:function(_105){
return this.createObject(dojo.gfx.Polyline,_105,true);
},createImage:function(_106){
if(!this.rawNode){
return null;
}
var _107=new dojo.gfx.Image();
var node=document.createElement("div");
node.style.position="relative";
node.style.width=this.rawNode.style.width;
node.style.height=this.rawNode.style.height;
node.style.filter="progid:DXImageTransform.Microsoft.Matrix(M11=1, M12=0, M21=0, M22=1, Dx=0, Dy=0)";
var img=document.createElement("img");
node.appendChild(img);
_107.setRawNode(node);
this.rawNode.appendChild(node);
_107.setShape(_106);
this.add(_107);
return _107;
},createGroup:function(){
return this.createObject(dojo.gfx.Group,null,true);
},createObject:function(_10a,_10b,_10c){
if(!this.rawNode){
return null;
}
var _10d=new _10a();
var node=document.createElement("v:"+_10a.nodeType);
_10d.setRawNode(node);
this.rawNode.appendChild(node);
if(_10c){
this._overrideSize(node);
}
_10d.setShape(_10b);
this.add(_10d);
return _10d;
},_overrideSize:function(node){
node.style.width=this.rawNode.style.width;
node.style.height=this.rawNode.style.height;
node.coordsize=parseFloat(node.style.width)+" "+parseFloat(node.style.height);
}};
dojo.lang.extend(dojo.gfx.Group,dojo.gfx._creators);
dojo.lang.extend(dojo.gfx.Surface,dojo.gfx._creators);
delete dojo.gfx._creators;
dojo.gfx.attachNode=function(node){
if(!node){
return null;
}
var s=null;
switch(node.tagName.toLowerCase()){
case dojo.gfx.Rect.nodeType:
s=new dojo.gfx.Rect();
break;
case dojo.gfx.Ellipse.nodeType:
s=(node.style.width==node.style.height)?new dojo.gfx.Circle():new dojo.gfx.Ellipse();
break;
case dojo.gfx.Path.nodeType:
switch(node.getAttribute("dojoGfxType")){
case "line":
s=new dojo.gfx.Line();
break;
case "polyline":
s=new dojo.gfx.Polyline();
break;
case "path":
s=new dojo.gfx.Path();
break;
}
break;
case dojo.gfx.Image.nodeType:
s=new dojo.gfx.Image();
break;
default:
dojo.debug("FATAL ERROR! tagName = "+node.tagName);
}
s.attach(node);
return s;
};
dojo.lang.extend(dojo.gfx.Surface,{setDimensions:function(_112,_113){
if(!this.rawNode){
return this;
}
this.rawNode.style.width=_112;
this.rawNode.style.height=_113;
this.rawNode.coordsize=_112+" "+_113;
return this;
},getDimensions:function(){
return this.rawNode?{width:this.rawNode.style.width,height:this.rawNode.style.height}:null;
},add:function(_114){
var _115=_114.getParent();
if(this!=_115){
this.rawNode.appendChild(_114.rawNode);
if(_115){
_115.remove(_114,true);
}
_114._setParent(this,null);
}
return this;
},remove:function(_116,_117){
if(this==_116.getParent()){
if(this.rawNode==_116.rawNode.parentNode){
this.rawNode.removeChild(_116.rawNode);
}
_116._setParent(null,null);
}
return this;
}});
dojo.gfx.createSurface=function(_118,_119,_11a){
var s=new dojo.gfx.Surface();
s.rawNode=document.createElement("v:group");
s.rawNode.style.width=_119?_119:"100%";
s.rawNode.style.height=_11a?_11a:"100%";
s.rawNode.coordsize=(_119&&_11a)?(parseFloat(_119)+" "+parseFloat(_11a)):"100% 100%";
s.rawNode.coordorigin="0 0";
dojo.byId(_118).appendChild(s.rawNode);
return s;
};
dojo.gfx.attachSurface=function(node){
var s=new dojo.gfx.Surface();
s.rawNode=node;
return s;
};
