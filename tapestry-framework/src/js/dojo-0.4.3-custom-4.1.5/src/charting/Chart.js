dojo.provide("dojo.charting.Chart");
dojo.require("dojo.lang.common");
dojo.require("dojo.charting.PlotArea");
dojo.charting.Chart=function(_1,_2,_3){
this.node=_1||null;
this.title=_2||"Chart";
this.description=_3||"";
this.plotAreas=[];
};
dojo.extend(dojo.charting.Chart,{addPlotArea:function(_4,_5){
if(_4.x!=null&&_4.left==null){
_4.left=_4.x;
}
if(_4.y!=null&&_4.top==null){
_4.top=_4.y;
}
this.plotAreas.push(_4);
if(_5){
this.render();
}
},onInitialize:function(_6){
},onRender:function(_7){
},onDestroy:function(_8){
},initialize:function(){
if(!this.node){
dojo.raise("dojo.charting.Chart.initialize: there must be a root node defined for the Chart.");
}
this.destroy();
this.render();
this.onInitialize(this);
},render:function(){
if(this.node.style.position!="absolute"){
this.node.style.position="relative";
}
for(var i=0;i<this.plotAreas.length;i++){
var _a=this.plotAreas[i].plotArea;
var _b=_a.initialize();
_b.style.position="absolute";
_b.style.top=this.plotAreas[i].top+"px";
_b.style.left=this.plotAreas[i].left+"px";
this.node.appendChild(_b);
_a.render();
}
},destroy:function(){
for(var i=0;i<this.plotAreas.length;i++){
this.plotAreas[i].plotArea.destroy();
}
while(this.node&&this.node.childNodes&&this.node.childNodes.length>0){
this.node.removeChild(this.node.childNodes[0]);
}
}});
