dojo.provide("dojo.widget.vml.Chart");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.widget.Chart");
dojo.require("dojo.math");
dojo.require("dojo.html.layout");
dojo.require("dojo.gfx.color");
dojo.widget.defineWidget("dojo.widget.vml.Chart",[dojo.widget.HtmlWidget,dojo.widget.Chart],function(){
this.templatePath=null;
this.templateCssPath=null;
this._isInitialize=false;
this.hasData=false;
this.vectorNode=null;
this.plotArea=null;
this.dataGroup=null;
this.axisGroup=null;
this.properties={height:0,width:0,defaultWidth:600,defaultHeight:400,plotType:null,padding:{top:10,bottom:2,left:60,right:30},axes:{x:{plotAt:0,label:"",unitLabel:"",unitType:Number,nUnitsToShow:10,range:{min:0,max:200}},y:{plotAt:0,label:"",unitLabel:"",unitType:Number,nUnitsToShow:10,range:{min:0,max:200}}}};
},{parseProperties:function(_1){
var _2=false;
var _3=false;
if(_1.getAttribute("width")){
this.properties.width=_1.getAttribute("width");
}
if(_1.getAttribute("height")){
this.properties.height=_1.getAttribute("height");
}
if(_1.getAttribute("plotType")){
this.properties.plotType=_1.getAttribute("plotType");
}
if(_1.getAttribute("padding")){
if(_1.getAttribute("padding").indexOf(",")>-1){
var p=_1.getAttribute("padding").split(",");
}else{
var p=_1.getAttribute("padding").split(" ");
}
if(p.length==1){
var _5=parseFloat(p[0]);
this.properties.padding.top=_5;
this.properties.padding.right=_5;
this.properties.padding.bottom=_5;
this.properties.padding.left=_5;
}else{
if(p.length==2){
var _6=parseFloat(p[0]);
var _7=parseFloat(p[1]);
this.properties.padding.top=_6;
this.properties.padding.right=_7;
this.properties.padding.bottom=_6;
this.properties.padding.left=_7;
}else{
if(p.length==4){
this.properties.padding.top=parseFloat(p[0]);
this.properties.padding.right=parseFloat(p[1]);
this.properties.padding.bottom=parseFloat(p[2]);
this.properties.padding.left=parseFloat(p[3]);
}
}
}
}
if(_1.getAttribute("rangeX")){
var p=_1.getAttribute("rangeX");
if(p.indexOf(",")>-1){
p=p.split(",");
}else{
p=p.split(" ");
}
this.properties.axes.x.range.min=parseFloat(p[0]);
this.properties.axes.x.range.max=parseFloat(p[1]);
_2=true;
}
if(_1.getAttribute("rangeY")){
var p=_1.getAttribute("rangeY");
if(p.indexOf(",")>-1){
p=p.split(",");
}else{
p=p.split(" ");
}
this.properties.axes.y.range.min=parseFloat(p[0]);
this.properties.axes.y.range.max=parseFloat(p[1]);
_3=true;
}
return {rangeX:_2,rangeY:_3};
},setAxesPlot:function(_8){
if(_8.getAttribute("axisAt")){
var p=_8.getAttribute("axisAt");
if(p.indexOf(",")>-1){
p=p.split(",");
}else{
p=p.split(" ");
}
if(!isNaN(parseFloat(p[0]))){
this.properties.axes.x.plotAt=parseFloat(p[0]);
}else{
if(p[0].toLowerCase()=="ymin"){
this.properties.axes.x.plotAt=this.properties.axes.y.range.min;
}else{
if(p[0].toLowerCase()=="ymax"){
this.properties.axes.x.plotAt=this.properties.axes.y.range.max;
}
}
}
if(!isNaN(parseFloat(p[1]))){
this.properties.axes.y.plotAt=parseFloat(p[1]);
}else{
if(p[1].toLowerCase()=="xmin"){
this.properties.axes.y.plotAt=this.properties.axes.x.range.min;
}else{
if(p[1].toLowerCase()=="xmax"){
this.properties.axes.y.plotAt=this.properties.axes.x.range.max;
}
}
}
}else{
this.properties.axes.x.plotAt=this.properties.axes.y.range.min;
this.properties.axes.y.plotAt=this.properties.axes.x.range.min;
}
},drawVectorNode:function(){
if(this.vectorNode){
this.destroy();
}
this.vectorNode=document.createElement("div");
this.vectorNode.style.width=this.properties.width+"px";
this.vectorNode.style.height=this.properties.height+"px";
this.vectorNode.style.position="relative";
this.domNode.appendChild(this.vectorNode);
},drawPlotArea:function(){
var _a=this.properties.width-this.properties.padding.left-this.properties.padding.right;
var _b=this.properties.height-this.properties.padding.top-this.properties.padding.bottom;
if(this.plotArea){
this.plotArea.parentNode.removeChild(this.plotArea);
this.plotArea=null;
}
this.plotArea=document.createElement("div");
this.plotArea.style.position="absolute";
this.plotArea.style.backgroundColor="#fff";
this.plotArea.style.top=(this.properties.padding.top)-2+"px";
this.plotArea.style.left=(this.properties.padding.left-1)+"px";
this.plotArea.style.width=_a+"px";
this.plotArea.style.height=_b+"px";
this.plotArea.style.clip="rect(0 "+_a+" "+_b+" 0)";
this.vectorNode.appendChild(this.plotArea);
},drawDataGroup:function(){
var _c=this.properties.width-this.properties.padding.left-this.properties.padding.right;
var _d=this.properties.height-this.properties.padding.top-this.properties.padding.bottom;
if(this.dataGroup){
this.dataGroup.parentNode.removeChild(this.dataGroup);
this.dataGroup=null;
}
this.dataGroup=document.createElement("div");
this.dataGroup.style.position="absolute";
this.dataGroup.setAttribute("title","Data Group");
this.dataGroup.style.top="0px";
this.dataGroup.style.left="0px";
this.dataGroup.style.width=_c+"px";
this.dataGroup.style.height=_d+"px";
this.plotArea.appendChild(this.dataGroup);
},drawAxes:function(){
var _e=this.properties.width-this.properties.padding.left-this.properties.padding.right;
var _f=this.properties.height-this.properties.padding.top-this.properties.padding.bottom;
if(this.axisGroup){
this.axisGroup.parentNode.removeChild(this.axisGroup);
this.axisGroup=null;
}
this.axisGroup=document.createElement("div");
this.axisGroup.style.position="absolute";
this.axisGroup.setAttribute("title","Axis Group");
this.axisGroup.style.top="0px";
this.axisGroup.style.left="0px";
this.axisGroup.style.width=_e+"px";
this.axisGroup.style.height=_f+"px";
this.plotArea.appendChild(this.axisGroup);
var _10=1;
var _11=document.createElement("v:line");
var y=dojo.widget.vml.Chart.Plotter.getY(this.properties.axes.x.plotAt,this);
_11.setAttribute("from","0px,"+y+"px");
_11.setAttribute("to",_e+"px,"+y+"px");
_11.style.position="absolute";
_11.style.top="0px";
_11.style.left="0px";
_11.style.antialias="false";
_11.setAttribute("strokecolor","#666");
_11.setAttribute("strokeweight",_10*2+"px");
this.axisGroup.appendChild(_11);
var _11=document.createElement("v:line");
var x=dojo.widget.vml.Chart.Plotter.getX(this.properties.axes.y.plotAt,this);
_11.setAttribute("from",x+"px,0px");
_11.setAttribute("to",x+"px,"+_f+"px");
_11.style.position="absolute";
_11.style.top="0px";
_11.style.left="0px";
_11.style.antialias="false";
_11.setAttribute("strokecolor","#666");
_11.setAttribute("strokeweight",_10*2+"px");
this.axisGroup.appendChild(_11);
var _14=10;
var t=document.createElement("div");
t.style.position="absolute";
t.style.top=(this.properties.height-this.properties.padding.bottom)+"px";
t.style.left=this.properties.padding.left+"px";
t.style.fontFamily="sans-serif";
t.style.fontSize=_14+"px";
t.innerHTML=dojo.math.round(parseFloat(this.properties.axes.x.range.min),2);
this.vectorNode.appendChild(t);
t=document.createElement("div");
t.style.position="absolute";
t.style.top=(this.properties.height-this.properties.padding.bottom)+"px";
t.style.left=(this.properties.width-this.properties.padding.right-_14)+"px";
t.style.fontFamily="sans-serif";
t.style.fontSize=_14+"px";
t.innerHTML=dojo.math.round(parseFloat(this.properties.axes.x.range.max),2);
this.vectorNode.appendChild(t);
t=document.createElement("div");
t.style.position="absolute";
t.style.top=(_14/2)+"px";
t.style.left="0px";
t.style.width=this.properties.padding.left+"px";
t.style.textAlign="right";
t.style.paddingRight="4px";
t.style.fontFamily="sans-serif";
t.style.fontSize=_14+"px";
t.innerHTML=dojo.math.round(parseFloat(this.properties.axes.y.range.max),2);
this.vectorNode.appendChild(t);
t=document.createElement("div");
t.style.position="absolute";
t.style.top=(this.properties.height-this.properties.padding.bottom-_14)+"px";
t.style.left="0px";
t.style.width=this.properties.padding.left+"px";
t.style.textAlign="right";
t.style.paddingRight="4px";
t.style.fontFamily="sans-serif";
t.style.fontSize=_14+"px";
t.innerHTML=dojo.math.round(parseFloat(this.properties.axes.y.range.min),2);
this.vectorNode.appendChild(t);
},init:function(){
if(!this.properties.width||!this.properties.height){
var box=dojo.html.getContentBox(this.domNode);
if(!this.properties.width){
this.properties.width=(box.width<32)?this.properties.defaultWidth:box.width;
}
if(!this.properties.height){
this.properties.height=(box.height<32)?this.properties.defaultHeight:box.height;
}
}
this.drawVectorNode();
this.drawPlotArea();
this.drawDataGroup();
this.drawAxes();
this.assignColors();
this._isInitialized=true;
},destroy:function(){
while(this.domNode.childNodes.length>0){
this.domNode.removeChild(this.domNode.childNodes[0]);
}
this.vectorNode=this.plotArea=this.dataGroup=this.axisGroup=null;
},render:function(){
if(this.dataGroup){
while(this.dataGroup.childNodes.length>0){
this.dataGroup.removeChild(this.dataGroup.childNodes[0]);
}
}else{
this.init();
}
for(var i=0;i<this.series.length;i++){
dojo.widget.vml.Chart.Plotter.plot(this.series[i],this);
}
},postCreate:function(){
var _18=this.domNode.getElementsByTagName("table")[0];
if(_18){
var _19=this.parseProperties(_18);
var _1a=false;
var _1b=false;
var _1c=this.parseData(_18);
if(!_1a){
this.properties.axes.x.range={min:_1c.x.min,max:_1c.x.max};
}
if(!_1b){
this.properties.axes.y.range={min:_1c.y.min,max:_1c.y.max};
}
this.setAxesPlot(_18);
this.domNode.removeChild(_18);
}
if(this.series.length>0){
this.render();
}
}});
dojo.widget.vml.Chart.Plotter=new function(){
var _1d=this;
var _1e={};
var _1f=dojo.widget.Chart.PlotTypes;
this.getX=function(_20,_21){
var v=parseFloat(_20);
var min=_21.properties.axes.x.range.min;
var max=_21.properties.axes.x.range.max;
var _25=0-min;
min+=_25;
max+=_25;
v+=_25;
var _26=0;
var _27=_21.properties.width-_21.properties.padding.left-_21.properties.padding.right;
var x=(v*((_27-_26)/max))+_26;
return x;
};
this.getY=function(_29,_2a){
var v=parseFloat(_29);
var max=_2a.properties.axes.y.range.max;
var min=_2a.properties.axes.y.range.min;
var _2e=0;
if(min<0){
_2e+=Math.abs(min);
}
min+=_2e;
max+=_2e;
v+=_2e;
var _2f=_2a.properties.height-_2a.properties.padding.top-_2a.properties.padding.bottom;
var _30=0;
var y=(((_2f-_30)/(max-min))*(max-v))+_30;
return y;
};
this.addPlotter=function(_32,_33){
_1e[_32]=_33;
};
this.plot=function(_34,_35){
if(_34.values.length==0){
return;
}
if(_34.plotType&&_1e[_34.plotType]){
return _1e[_34.plotType](_34,_35);
}else{
if(_35.plotType&&_1e[_35.plotType]){
return _1e[_35.plotType](_34,_35);
}
}
};
_1e["bar"]=function(_36,_37){
var _38=1;
var _39=0;
var ys=[];
var _3b=_1d.getY(_37.properties.axes.x.plotAt,_37);
var yA=_3b;
for(var i=0;i<_36.values.length;i++){
var x=_1d.getX(_36.values[i].x,_37);
var w;
if(i==_36.values.length-1){
w=_39;
}else{
w=_1d.getX(_36.values[i+1].x,_37)-x-_38;
_39=w;
}
x-=(w/2);
var y=_1d.getY(_36.values[i].value,_37);
var h=Math.abs(yA-y);
if(parseFloat(_36.values[i].value)<_37.properties.axes.x.plotAt){
y=yA;
}
var bar=document.createElement("v:rect");
bar.style.position="absolute";
bar.style.top=y+"px";
bar.style.left=x+"px";
bar.style.width=w+"px";
bar.style.height=h+"px";
bar.setAttribute("fillColor",_36.color);
bar.setAttribute("stroked","false");
bar.style.antialias="false";
bar.setAttribute("title",_36.label+" ("+i+"): "+_36.values[i].value);
var _43=document.createElement("v:fill");
_43.setAttribute("opacity","0.9");
bar.appendChild(_43);
_37.dataGroup.appendChild(bar);
}
};
_1e["line"]=function(_44,_45){
var _46=1.5;
var _47=document.createElement("v:shape");
_47.setAttribute("strokeweight","2px");
_47.setAttribute("strokecolor",_44.color);
_47.setAttribute("fillcolor","none");
_47.setAttribute("filled","false");
_47.setAttribute("title",_44.label);
_47.setAttribute("coordsize",_45.properties.width+","+_45.properties.height);
_47.style.position="absolute";
_47.style.top="0px";
_47.style.left="0px";
_47.style.width=_45.properties.width+"px";
_47.style.height=_45.properties.height+"px";
var _48=document.createElement("v:stroke");
_48.setAttribute("opacity","0.85");
_47.appendChild(_48);
var _49=[];
for(var i=0;i<_44.values.length;i++){
var x=Math.round(_1d.getX(_44.values[i].x,_45));
var y=Math.round(_1d.getY(_44.values[i].value,_45));
if(i==0){
_49.push("m");
_49.push(x+","+y);
}else{
var _4d=Math.round(_1d.getX(_44.values[i-1].x,_45));
var _4e=Math.round(_1d.getY(_44.values[i-1].value,_45));
var dx=x-_4d;
var dy=y-_4e;
_49.push("c");
var cx=Math.round((x-(_46-1)*(dx/_46)));
_49.push(cx+","+_4e);
cx=Math.round((x-(dx/_46)));
_49.push(cx+","+y);
_49.push(x+","+y);
}
}
_47.setAttribute("path",_49.join(" ")+" e");
_45.dataGroup.appendChild(_47);
};
_1e["area"]=function(_52,_53){
var _54=1.5;
var _55=document.createElement("v:shape");
_55.setAttribute("strokeweight","1px");
_55.setAttribute("strokecolor",_52.color);
_55.setAttribute("fillcolor",_52.color);
_55.setAttribute("title",_52.label);
_55.setAttribute("coordsize",_53.properties.width+","+_53.properties.height);
_55.style.position="absolute";
_55.style.top="0px";
_55.style.left="0px";
_55.style.width=_53.properties.width+"px";
_55.style.height=_53.properties.height+"px";
var _56=document.createElement("v:stroke");
_56.setAttribute("opacity","0.8");
_55.appendChild(_56);
var _57=document.createElement("v:fill");
_57.setAttribute("opacity","0.4");
_55.appendChild(_57);
var _58=[];
for(var i=0;i<_52.values.length;i++){
var x=Math.round(_1d.getX(_52.values[i].x,_53));
var y=Math.round(_1d.getY(_52.values[i].value,_53));
if(i==0){
_58.push("m");
_58.push(x+","+y);
}else{
var _5c=Math.round(_1d.getX(_52.values[i-1].x,_53));
var _5d=Math.round(_1d.getY(_52.values[i-1].value,_53));
var dx=x-_5c;
var dy=y-_5d;
_58.push("c");
var cx=Math.round((x-(_54-1)*(dx/_54)));
_58.push(cx+","+_5d);
cx=Math.round((x-(dx/_54)));
_58.push(cx+","+y);
_58.push(x+","+y);
}
}
_58.push("l");
_58.push(x+","+_1d.getY(0,_53));
_58.push("l");
_58.push(_1d.getX(0,_53)+","+_1d.getY(0,_53));
_55.setAttribute("path",_58.join(" ")+" x e");
_53.dataGroup.appendChild(_55);
};
_1e["scatter"]=function(_61,_62){
var r=6;
for(var i=0;i<_61.values.length;i++){
var x=_1d.getX(_61.values[i].x,_62);
var y=_1d.getY(_61.values[i].value,_62);
var mod=r/2;
var _68=document.createElement("v:rect");
_68.setAttribute("fillcolor",_61.color);
_68.setAttribute("strokecolor",_61.color);
_68.setAttribute("title",_61.label+": "+_61.values[i].value);
_68.style.position="absolute";
_68.style.rotation="45";
_68.style.top=(y-mod)+"px";
_68.style.left=(x-mod)+"px";
_68.style.width=r+"px";
_68.style.height=r+"px";
var _69=document.createElement("v:fill");
_69.setAttribute("opacity","0.6");
_68.appendChild(_69);
_62.dataGroup.appendChild(_68);
}
};
_1e["bubble"]=function(_6a,_6b){
var _6c=1;
var min=_6b.properties.axes.x.range.min;
var max=_6b.properties.axes.x.range.max;
var _6f=0-min;
min+=_6f;
max+=_6f;
var _70=_6b.properties.padding.left;
var _71=_6b.properties.width-_6b.properties.padding.right;
var _72=(max-min)/(_71-_70)*25;
for(var i=0;i<_6a.values.length;i++){
var _74=_6a.values[i].size;
if(isNaN(parseFloat(_74))){
_74=_6c;
}
var _75=(parseFloat(_74)*_72)/2;
var _76=_75*2;
var cx=_1d.getX(_6a.values[i].x,_6b);
var cy=_1d.getY(_6a.values[i].value,_6b);
var top=cy-_75;
var _7a=cx-_75;
var _7b=document.createElement("v:oval");
_7b.setAttribute("fillcolor",_6a.color);
_7b.setAttribute("title",_6a.label+": "+_6a.values[i].value+" ("+_74+")");
_7b.setAttribute("stroked","false");
_7b.style.position="absolute";
_7b.style.top=top+"px";
_7b.style.left=_7a+"px";
_7b.style.width=_76+"px";
_7b.style.height=_76+"px";
var _7c=document.createElement("v:fill");
_7c.setAttribute("opacity","0.8");
_7b.appendChild(_7c);
_6b.dataGroup.appendChild(_7b);
}
};
}();
