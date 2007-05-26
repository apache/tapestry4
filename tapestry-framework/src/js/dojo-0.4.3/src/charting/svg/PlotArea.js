dojo.provide("dojo.charting.svg.PlotArea");
dojo.require("dojo.lang.common");
if(dojo.render.svg.capable){
dojo.require("dojo.svg");
dojo.extend(dojo.charting.PlotArea,{resize:function(){
var _1=this.getArea();
this.nodes.area.setAttribute("width",this.size.width);
this.nodes.area.setAttribute("height",this.size.height);
var _2=this.nodes.area.getElementsByTagName("rect")[0];
_2.setAttribute("x",_1.left);
_2.setAttribute("y",_1.top);
_2.setAttribute("width",_1.right-_1.left);
_2.setAttribute("height",_1.bottom-_1.top);
this.nodes.background.setAttribute("width",this.size.width);
this.nodes.background.setAttribute("height",this.size.height);
if(this.nodes.plots){
this.nodes.area.removeChild(this.nodes.plots);
this.nodes.plots=null;
}
this.nodes.plots=document.createElementNS(dojo.svg.xmlns.svg,"g");
this.nodes.plots.setAttribute("id",this.getId()+"-plots");
this.nodes.plots.setAttribute("style","clip-path:url(#"+this.getId()+"-clip);");
this.nodes.area.appendChild(this.nodes.plots);
for(var i=0;i<this.plots.length;i++){
this.nodes.plots.appendChild(this.initializePlot(this.plots[i]));
}
if(this.nodes.axes){
this.nodes.area.removeChild(this.nodes.axes);
this.nodes.axes=null;
}
this.nodes.axes=document.createElementNS(dojo.svg.xmlns.svg,"g");
this.nodes.axes.setAttribute("id",this.getId()+"-axes");
this.nodes.area.appendChild(this.nodes.axes);
var _4=this.getAxes();
for(var p in _4){
var _6=_4[p];
this.nodes.axes.appendChild(_6.axis.initialize(this,_6.plot,_6.drawAgainst,_6.plane));
}
},initializePlot:function(_7){
_7.destroy();
_7.dataNode=document.createElementNS(dojo.svg.xmlns.svg,"g");
_7.dataNode.setAttribute("id",_7.getId());
return _7.dataNode;
},initialize:function(){
this.destroy();
this.nodes.main=document.createElement("div");
this.nodes.area=document.createElementNS(dojo.svg.xmlns.svg,"svg");
this.nodes.area.setAttribute("id",this.getId());
this.nodes.main.appendChild(this.nodes.area);
var _8=document.createElementNS(dojo.svg.xmlns.svg,"defs");
var _9=document.createElementNS(dojo.svg.xmlns.svg,"clipPath");
_9.setAttribute("id",this.getId()+"-clip");
var _a=document.createElementNS(dojo.svg.xmlns.svg,"rect");
_9.appendChild(_a);
_8.appendChild(_9);
this.nodes.area.appendChild(_8);
this.nodes.background=document.createElementNS(dojo.svg.xmlns.svg,"rect");
this.nodes.background.setAttribute("id",this.getId()+"-background");
this.nodes.background.setAttribute("fill","#fff");
this.nodes.area.appendChild(this.nodes.background);
this.resize();
return this.nodes.main;
}});
}
