dojo.provide("dojo.charting.Plot");
dojo.require("dojo.lang.common");
dojo.require("dojo.charting.Axis");
dojo.require("dojo.charting.Series");
dojo.charting.RenderPlotSeries={Singly:"single",Grouped:"grouped"};
dojo.charting.Plot=function(_1,_2,_3){
var id="dojo-charting-plot-"+dojo.charting.Plot.count++;
this.getId=function(){
return id;
};
this.setId=function(_5){
id=_5;
};
this.axisX=null;
this.axisY=null;
this.series=[];
this.dataNode=null;
this.renderType=dojo.charting.RenderPlotSeries.Singly;
if(_1){
this.setAxis(_1,"x");
}
if(_2){
this.setAxis(_2,"y");
}
if(_3){
for(var i=0;i<_3.length;i++){
this.addSeries(_3[i]);
}
}
};
dojo.charting.Plot.count=0;
dojo.extend(dojo.charting.Plot,{addSeries:function(_7,_8){
if(_7.plotter){
this.series.push(_7);
}else{
this.series.push({data:_7,plotter:_8||dojo.charting.Plotters["Default"]});
}
},setAxis:function(_9,_a){
if(_a.toLowerCase()=="x"){
this.axisX=_9;
}else{
if(_a.toLowerCase()=="y"){
this.axisY=_9;
}
}
},getRanges:function(){
var _b,_c,_d,_e;
_b=_d=Number.MAX_VALUE;
_c=_e=Number.MIN_VALUE;
for(var i=0;i<this.series.length;i++){
var _10=this.series[i].data.evaluate();
for(var j=0;j<_10.length;j++){
var _12=_10[j];
_b=Math.min(_12.x,_b);
_d=Math.min(_12.y,_d);
_c=Math.max(_12.x,_c);
_e=Math.max(_12.y,_e);
}
}
return {x:{upper:_c,lower:_b},y:{upper:_e,lower:_d},toString:function(){
return "[ x:"+_c+" - "+_b+", y:"+_e+" - "+_d+"]";
}};
},destroy:function(){
var _13=this.dataNode;
while(_13&&_13.childNodes&&_13.childNodes.length>0){
_13.removeChild(_13.childNodes[0]);
}
this.dataNode=null;
}});
