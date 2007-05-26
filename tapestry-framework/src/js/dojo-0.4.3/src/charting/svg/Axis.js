dojo.provide("dojo.charting.svg.Axis");
dojo.require("dojo.lang.common");
if(dojo.render.svg.capable){
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
var g=this.nodes.lines=document.createElementNS(dojo.svg.xmlns.svg,"g");
g.setAttribute("id",this.getId()+"-lines");
for(var i=0;i<this._labels.length;i++){
if(this._labels[i].value==this.origin){
continue;
}
var v=this.getCoord(this._labels[i].value,_1,_2);
var l=document.createElementNS(dojo.svg.xmlns.svg,"line");
l.setAttribute("style","stroke:#999;stroke-width:1px;stroke-dasharray:1,4;");
if(_3=="x"){
l.setAttribute("y1",_4.top);
l.setAttribute("y2",_4.bottom);
l.setAttribute("x1",v);
l.setAttribute("x2",v);
}else{
if(_3=="y"){
l.setAttribute("y1",v);
l.setAttribute("y2",v);
l.setAttribute("x1",_4.left);
l.setAttribute("x2",_4.right);
}
}
g.appendChild(l);
}
return g;
},renderTicks:function(_9,_a,_b,_c){
if(this.nodes.ticks){
while(this.nodes.ticks.childNodes.length>0){
this.nodes.ticks.removeChild(this.nodes.ticks.childNodes[0]);
}
if(this.nodes.ticks.parentNode){
this.nodes.ticks.parentNode.removeChild(this.nodes.ticks);
this.nodes.ticks=null;
}
}
var g=this.nodes.ticks=document.createElementNS(dojo.svg.xmlns.svg,"g");
g.setAttribute("id",this.getId()+"-ticks");
for(var i=0;i<this._labels.length;i++){
var v=this.getCoord(this._labels[i].value,_9,_a);
var l=document.createElementNS(dojo.svg.xmlns.svg,"line");
l.setAttribute("style","stroke:#000;stroke-width:1pt;");
if(_b=="x"){
l.setAttribute("y1",_c);
l.setAttribute("y2",_c+3);
l.setAttribute("x1",v);
l.setAttribute("x2",v);
}else{
if(_b=="y"){
l.setAttribute("y1",v);
l.setAttribute("y2",v);
l.setAttribute("x1",_c-2);
l.setAttribute("x2",_c+2);
}
}
g.appendChild(l);
}
return g;
},renderLabels:function(_11,_12,_13,_14,_15,_16){
function createLabel(_17,x,y,_1a,_1b){
var _1c=document.createElementNS(dojo.svg.xmlns.svg,"text");
_1c.setAttribute("x",x);
_1c.setAttribute("y",(_13=="x"?y:y+2));
_1c.setAttribute("style","text-anchor:"+_1b+";font-family:sans-serif;font-size:"+_1a+"px;fill:#000;");
_1c.appendChild(document.createTextNode(_17));
return _1c;
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
var g=this.nodes.labels=document.createElementNS(dojo.svg.xmlns.svg,"g");
g.setAttribute("id",this.getId()+"-labels");
for(var i=0;i<this._labels.length;i++){
var v=this.getCoord(this._labels[i].value,_11,_12);
if(_13=="x"){
g.appendChild(createLabel(this._labels[i].label,v,_14,_15,_16));
}else{
if(_13=="y"){
g.appendChild(createLabel(this._labels[i].label,_14,v,_15,_16));
}
}
}
return g;
},render:function(_20,_21,_22,_23){
if(!this._rerender&&this.nodes.main){
return this.nodes.main;
}
this._rerender=false;
var _24=_20.getArea();
var _25=1;
var _26="stroke:#000;stroke-width:"+_25+"px;";
var _27=10;
var _28=_22.getCoord(this.origin,_20,_21);
this.nodes.main=document.createElementNS(dojo.svg.xmlns.svg,"g");
var g=this.nodes.main;
g.setAttribute("id",this.getId());
var _2a=this.nodes.axis=document.createElementNS(dojo.svg.xmlns.svg,"line");
if(_23=="x"){
_2a.setAttribute("y1",_28);
_2a.setAttribute("y2",_28);
_2a.setAttribute("x1",_24.left-_25);
_2a.setAttribute("x2",_24.right+_25);
_2a.setAttribute("style",_26);
var y=_28+_27+2;
if(this.showLines){
g.appendChild(this.renderLines(_20,_21,_23,y));
}
if(this.showTicks){
g.appendChild(this.renderTicks(_20,_21,_23,_28));
}
if(this.showLabels){
g.appendChild(this.renderLabels(_20,_21,_23,y,_27,"middle"));
}
if(this.showLabel&&this.label){
var x=_20.size.width/2;
var _2d=document.createElementNS(dojo.svg.xmlns.svg,"text");
_2d.setAttribute("x",x);
_2d.setAttribute("y",(_28+(_27*2)+(_27/2)));
_2d.setAttribute("style","text-anchor:middle;font-family:sans-serif;font-weight:bold;font-size:"+(_27+2)+"px;fill:#000;");
_2d.appendChild(document.createTextNode(this.label));
g.appendChild(_2d);
}
}else{
_2a.setAttribute("x1",_28);
_2a.setAttribute("x2",_28);
_2a.setAttribute("y1",_24.top);
_2a.setAttribute("y2",_24.bottom);
_2a.setAttribute("style",_26);
var _2e=this.origin==_22.range.upper;
var x=_28+(_2e?4:-4);
var _2f=_2e?"start":"end";
if(this.showLines){
g.appendChild(this.renderLines(_20,_21,_23,x));
}
if(this.showTicks){
g.appendChild(this.renderTicks(_20,_21,_23,_28));
}
if(this.showLabels){
g.appendChild(this.renderLabels(_20,_21,_23,x,_27,_2f));
}
if(this.showLabel&&this.label){
var x=_2e?(_28+(_27*2)+(_27/2)):(_28-(_27*4));
var y=_20.size.height/2;
var _2d=document.createElementNS(dojo.svg.xmlns.svg,"text");
_2d.setAttribute("x",x);
_2d.setAttribute("y",y);
_2d.setAttribute("transform","rotate(90, "+x+", "+y+")");
_2d.setAttribute("style","text-anchor:middle;font-family:sans-serif;font-weight:bold;font-size:"+(_27+2)+"px;fill:#000;");
_2d.appendChild(document.createTextNode(this.label));
g.appendChild(_2d);
}
}
g.appendChild(_2a);
return g;
}});
}
