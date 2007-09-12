dojo.provide("dojo.charting.PlotArea");
dojo.require("dojo.lang.common");
dojo.require("dojo.gfx.color");
dojo.require("dojo.gfx.color.hsl");
dojo.require("dojo.charting.Plot");
dojo.charting.PlotArea=function(){
var id="dojo-charting-plotarea-"+dojo.charting.PlotArea.count++;
this.getId=function(){
return id;
};
this.setId=function(_2){
id=_2;
};
this.areaType="standard";
this.plots=[];
this.size={width:600,height:400};
this.padding={top:10,right:10,bottom:20,left:20};
this.nodes={main:null,area:null,background:null,axes:null,plots:null};
this._color={h:140,s:120,l:120,step:27};
};
dojo.charting.PlotArea.count=0;
dojo.extend(dojo.charting.PlotArea,{nextColor:function(){
var _3=dojo.gfx.color.hsl2rgb(this._color.h,this._color.s,this._color.l);
this._color.h=(this._color.h+this._color.step)%360;
while(this._color.h<140){
this._color.h+=this._color.step;
}
return dojo.gfx.color.rgb2hex(_3[0],_3[1],_3[2]);
},getArea:function(){
return {left:this.padding.left,right:this.size.width-this.padding.right,top:this.padding.top,bottom:this.size.height-this.padding.bottom,toString:function(){
var a=[this.top,this.right,this.bottom,this.left];
return "["+a.join()+"]";
}};
},getAxes:function(){
var _5={};
for(var i=0;i<this.plots.length;i++){
var _7=this.plots[i];
_5[_7.axisX.getId()]={axis:_7.axisX,drawAgainst:_7.axisY,plot:_7,plane:"x"};
_5[_7.axisY.getId()]={axis:_7.axisY,drawAgainst:_7.axisX,plot:_7,plane:"y"};
}
return _5;
},getLegendInfo:function(){
var a=[];
for(var i=0;i<this.plots.length;i++){
for(var j=0;j<this.plots[i].series.length;j++){
var _b=this.plots[i].series[j].data;
a.push({label:_b.label,color:_b.color});
}
}
return a;
},setAxesRanges:function(){
var _c={};
var _d={};
for(var i=0;i<this.plots.length;i++){
var _f=this.plots[i];
var _c=_f.getRanges();
var x=_c.x;
var y=_c.y;
var ax,ay;
if(!_d[_f.axisX.getId()]){
_d[_f.axisX.getId()]=_f.axisX;
_c[_f.axisX.getId()]={upper:x.upper,lower:x.lower};
}
ax=_c[_f.axisX.getId()];
ax.upper=Math.max(ax.upper,x.upper);
ax.lower=Math.min(ax.lower,x.lower);
if(!_d[_f.axisY.getId()]){
_d[_f.axisY.getId()]=_f.axisY;
_c[_f.axisY.getId()]={upper:y.upper,lower:y.lower};
}
ay=_c[_f.axisY.getId()];
ay.upper=Math.max(ay.upper,y.upper);
ay.lower=Math.min(ay.lower,y.lower);
}
for(var p in _d){
_d[p].range=_c[p];
}
},render:function(_15,_16){
if(!this.nodes.main||!this.nodes.area||!this.nodes.background||!this.nodes.plots||!this.nodes.axes){
this.initialize();
}
this.resize();
for(var i=0;i<this.plots.length;i++){
var _18=this.plots[i];
if(_18.dataNode){
this.nodes.plots.removeChild(_18.dataNode);
}
var _19=this.initializePlot(_18);
switch(_18.renderType){
case dojo.charting.RenderPlotSeries.Grouped:
if(_18.series[0]){
_19.appendChild(_18.series[0].plotter(this,_18,_15,_16));
}
break;
case dojo.charting.RenderPlotSeries.Singly:
default:
for(var j=0;j<_18.series.length;j++){
var _1b=_18.series[j];
var _1c=_1b.data.evaluate(_15);
_19.appendChild(_1b.plotter(_1c,this,_18,_16));
}
}
this.nodes.plots.appendChild(_19);
}
},destroy:function(){
for(var i=0;i<this.plots.length;i++){
this.plots[i].destroy();
}
for(var p in this.nodes){
var _1f=this.nodes[p];
if(!_1f){
continue;
}
if(!_1f.childNodes){
continue;
}
while(_1f.childNodes.length>0){
_1f.removeChild(_1f.childNodes[0]);
}
this.nodes[p]=null;
}
}});
dojo.requireIf(dojo.render.svg.capable,"dojo.charting.svg.PlotArea");
dojo.requireIf(dojo.render.vml.capable,"dojo.charting.vml.PlotArea");
