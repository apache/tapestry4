dojo.provide("dojo.charting.vml.PlotArea");
dojo.require("dojo.lang.common");
if(dojo.render.vml.capable){
dojo.extend(dojo.charting.PlotArea,{resize:function(){
var a=this.getArea();
this.nodes.area.style.width=this.size.width+"px";
this.nodes.area.style.height=this.size.height+"px";
this.nodes.background.style.width=this.size.width+"px";
this.nodes.background.style.height=this.size.height+"px";
this.nodes.plots.width=this.size.width+"px";
this.nodes.plots.height=this.size.height+"px";
this.nodes.plots.style.clip="rect("+a.top+" "+a.right+" "+a.bottom+" "+a.left+")";
if(this.nodes.axes){
this.nodes.area.removeChild(this.nodes.axes);
}
var _2=this.nodes.axes=document.createElement("div");
_2.id=this.getId()+"-axes";
this.nodes.area.appendChild(_2);
var ax=this.getAxes();
for(var p in ax){
var _5=ax[p];
_2.appendChild(_5.axis.initialize(this,_5.plot,_5.drawAgainst,_5.plane));
}
},initializePlot:function(_6){
_6.destroy();
_6.dataNode=document.createElement("div");
_6.dataNode.id=_6.getId();
return _6.dataNode;
},initialize:function(){
this.destroy();
var _7=this.nodes.main=document.createElement("div");
var _8=this.nodes.area=document.createElement("div");
_8.id=this.getId();
_8.style.position="absolute";
_7.appendChild(_8);
var bg=this.nodes.background=document.createElement("div");
bg.id=this.getId()+"-background";
bg.style.position="absolute";
bg.style.top="0px";
bg.style.left="0px";
bg.style.backgroundColor="#fff";
_8.appendChild(bg);
var a=this.getArea();
var _b=this.nodes.plots=document.createElement("div");
_b.id=this.getId()+"-plots";
_b.style.position="absolute";
_b.style.top="0px";
_b.style.left="0px";
_8.appendChild(_b);
for(var i=0;i<this.plots.length;i++){
_b.appendChild(this.initializePlot(this.plots[i]));
}
this.resize();
return _7;
}});
}
