dojo.provide("dojo.charting.vml.Axis");
dojo.require("dojo.lang.common");
if(dojo.render.vml.capable){
dojo.extend(dojo.charting.Axis,{renderLines:function(_1,_2,_3){
if(this.nodes.lines){
while(this.nodes.lines.childNodes.length>0){
this.nodes.lines.removeChild(this.nodes.lines.childNodes[0]);
}
if(this.nodes.lines.parentNode){
this.nodes.lines.parentNode.removeChild(this.nodes.lines);
this.nodes.lines=null;
}
}
var _4=_1.getArea();
var g=this.nodes.lines=document.createElement("div");
g.setAttribute("id",this.getId()+"-lines");
for(var i=0;i<this._labels.length;i++){
if(this._labels[i].value==this.origin){
continue;
}
var v=this.getCoord(this._labels[i].value,_1,_2);
var l=document.createElement("v:line");
var _9=document.createElement("v:stroke");
_9.dashstyle="dot";
l.appendChild(_9);
l.setAttribute("strokecolor","#666");
l.setAttribute("strokeweight","1px");
var s=l.style;
s.position="absolute";
s.top="0px";
s.left="0px";
s.antialias="false";
if(_3=="x"){
l.setAttribute("from",v+"px,"+_4.top+"px");
l.setAttribute("to",v+"px,"+_4.bottom+"px");
}else{
if(_3=="y"){
l.setAttribute("from",_4.left+"px,"+v+"px");
l.setAttribute("to",_4.right+"px,"+v+"px");
}
}
g.appendChild(l);
}
return g;
},renderTicks:function(_b,_c,_d,_e){
if(this.nodes.ticks){
while(this.nodes.ticks.childNodes.length>0){
this.nodes.ticks.removeChild(this.nodes.ticks.childNodes[0]);
}
if(this.nodes.ticks.parentNode){
this.nodes.ticks.parentNode.removeChild(this.nodes.ticks);
this.nodes.ticks=null;
}
}
var g=this.nodes.ticks=document.createElement("div");
g.setAttribute("id",this.getId()+"-ticks");
for(var i=0;i<this._labels.length;i++){
var v=this.getCoord(this._labels[i].value,_b,_c);
var l=document.createElement("v:line");
l.setAttribute("strokecolor","#000");
l.setAttribute("strokeweight","1px");
var s=l.style;
s.position="absolute";
s.top="0px";
s.left="0px";
s.antialias="false";
if(_d=="x"){
l.setAttribute("from",v+"px,"+_e+"px");
l.setAttribute("to",v+"px,"+(_e+3)+"px");
}else{
if(_d=="y"){
l.setAttribute("from",(_e-2)+"px,"+v+"px");
l.setAttribute("to",(_e+2)+"px,"+v+"px");
}
}
g.appendChild(l);
}
return g;
},renderLabels:function(_14,_15,_16,_17,_18,_19){
function createLabel(_1a,x,y,_1d,_1e){
var _1f=document.createElement("div");
var s=_1f.style;
_1f.innerHTML=_1a;
s.fontSize=_1d+"px";
s.fontFamily="sans-serif";
s.position="absolute";
s.top=y+"px";
if(_1e=="center"){
s.left=x+"px";
s.textAlign="center";
}else{
if(_1e=="left"){
s.left=x+"px";
s.textAlign="left";
}else{
if(_1e=="right"){
s.right=x+"px";
s.textAlign="right";
}
}
}
return _1f;
}
if(this.nodes.labels){
while(this.nodes.labels.childNodes.length>0){
this.nodes.labels.removeChild(this.nodes.labels.childNodes[0]);
}
if(this.nodes.labels.parentNode){
this.nodes.labels.parentNode.removeChild(this.nodes.labels);
this.nodes.labels=null;
}
}
var g=this.nodes.labels=document.createElement("div");
g.setAttribute("id",this.getId()+"-labels");
for(var i=0;i<this._labels.length;i++){
var v=this.getCoord(this._labels[i].value,_14,_15);
if(_16=="x"){
var _24=createLabel(this._labels[i].label,v,_17,_18,_19);
document.body.appendChild(_24);
_24.style.left=v-(_24.offsetWidth/2)+"px";
g.appendChild(_24);
}else{
if(_16=="y"){
var _24=createLabel(this._labels[i].label,_17,v,_18,_19);
document.body.appendChild(_24);
_24.style.top=v-(_24.offsetHeight/2)+"px";
g.appendChild(_24);
}
}
}
return g;
},render:function(_25,_26,_27,_28){
if(!this._rerender&&this.nodes.main){
return this.nodes.main;
}
this._rerender=false;
var _29=_25.getArea();
var _2a=1;
var _2b="stroke:#000;stroke-width:"+_2a+"px;";
var _2c=10;
var _2d=_27.getCoord(this.origin,_25,_26);
var g=this.nodes.main=document.createElement("div");
g.setAttribute("id",this.getId());
var _2f=this.nodes.axis=document.createElement("v:line");
_2f.setAttribute("strokecolor","#000");
_2f.setAttribute("strokeweight",_2a+"px");
var s=_2f.style;
s.position="absolute";
s.top="0px";
s.left="0px";
s.antialias="false";
if(_28=="x"){
_2f.setAttribute("from",_29.left+"px,"+_2d+"px");
_2f.setAttribute("to",_29.right+"px,"+_2d+"px");
var y=_2d+Math.floor(_2c/2);
if(this.showLines){
g.appendChild(this.renderLines(_25,_26,_28,y));
}
if(this.showTicks){
g.appendChild(this.renderTicks(_25,_26,_28,_2d));
}
if(this.showLabels){
g.appendChild(this.renderLabels(_25,_26,_28,y,_2c,"center"));
}
if(this.showLabel&&this.label){
var x=_25.size.width/2;
var y=_2d+Math.round(_2c*1.5);
var _33=document.createElement("div");
var s=_33.style;
_33.innerHTML=this.label;
s.fontSize=(_2c+2)+"px";
s.fontFamily="sans-serif";
s.fontWeight="bold";
s.position="absolute";
s.top=y+"px";
s.left=x+"px";
s.textAlign="center";
document.body.appendChild(_33);
_33.style.left=x-(_33.offsetWidth/2)+"px";
g.appendChild(_33);
}
}else{
_2f.setAttribute("from",_2d+"px,"+_29.top+"px");
_2f.setAttribute("to",_2d+"px,"+_29.bottom+"px");
var _34=this.origin==_27.range.upper;
var x=_2d+4;
var _35="left";
if(!_34){
x=_29.right-_2d+_2c+4;
_35="right";
if(_2d==_29.left){
x+=(_2c*2)-(_2c/2);
}
}
if(this.showLines){
g.appendChild(this.renderLines(_25,_26,_28,x));
}
if(this.showTicks){
g.appendChild(this.renderTicks(_25,_26,_28,_2d));
}
if(this.showLabels){
g.appendChild(this.renderLabels(_25,_26,_28,x,_2c,_35));
}
if(this.showLabel&&this.label){
x+=(_2c*2)-2;
var y=_25.size.height/2;
var _33=document.createElement("div");
var s=_33.style;
_33.innerHTML=this.label;
s.fontSize=(_2c+2)+"px";
s.fontFamily="sans-serif";
s.fontWeight="bold";
s.position="absolute";
s.height=_25.size.height+"px";
s.writingMode="tb-rl";
s.textAlign="center";
s[_35]=x+"px";
document.body.appendChild(_33);
s.top=y-(_33.offsetHeight/2)+"px";
g.appendChild(_33);
}
}
g.appendChild(_2f);
return g;
}});
}
