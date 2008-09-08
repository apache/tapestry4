dojo.provide("dojo.widget.svg.Chart");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.widget.Chart");
dojo.require("dojo.html.layout");
dojo.require("dojo.math");
dojo.require("dojo.svg");
dojo.require("dojo.gfx.color");
dojo.require("dojo.json");
dojo.widget.defineWidget("dojo.widget.svg.Chart",[dojo.widget.HtmlWidget,dojo.widget.Chart],function(){
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
dojo.svg.g.suspend();
if(this.vectorNode){
this.destroy();
}
this.vectorNode=document.createElementNS(dojo.svg.xmlns.svg,"svg");
this.vectorNode.setAttribute("width",this.properties.width);
this.vectorNode.setAttribute("height",this.properties.height);
dojo.svg.g.resume();
},drawPlotArea:function(){
dojo.svg.g.suspend();
if(this.plotArea){
this.plotArea.parentNode.removeChild(this.plotArea);
this.plotArea=null;
}
var _a=document.createElementNS(dojo.svg.xmlns.svg,"defs");
var _b=document.createElementNS(dojo.svg.xmlns.svg,"clipPath");
_b.setAttribute("id","plotClip"+this.widgetId);
var _c=document.createElementNS(dojo.svg.xmlns.svg,"rect");
_c.setAttribute("x",this.properties.padding.left);
_c.setAttribute("y",this.properties.padding.top);
_c.setAttribute("width",this.properties.width-this.properties.padding.left-this.properties.padding.right);
_c.setAttribute("height",this.properties.height-this.properties.padding.top-this.properties.padding.bottom);
_b.appendChild(_c);
_a.appendChild(_b);
this.vectorNode.appendChild(_a);
this.plotArea=document.createElementNS(dojo.svg.xmlns.svg,"g");
this.vectorNode.appendChild(this.plotArea);
var _c=document.createElementNS(dojo.svg.xmlns.svg,"rect");
_c.setAttribute("x",this.properties.padding.left);
_c.setAttribute("y",this.properties.padding.top);
_c.setAttribute("width",this.properties.width-this.properties.padding.left-this.properties.padding.right);
_c.setAttribute("height",this.properties.height-this.properties.padding.top-this.properties.padding.bottom);
_c.setAttribute("fill","#fff");
this.plotArea.appendChild(_c);
dojo.svg.g.resume();
},drawDataGroup:function(){
dojo.svg.g.suspend();
if(this.dataGroup){
this.dataGroup.parentNode.removeChild(this.dataGroup);
this.dataGroup=null;
}
this.dataGroup=document.createElementNS(dojo.svg.xmlns.svg,"g");
this.dataGroup.setAttribute("style","clip-path:url(#plotClip"+this.widgetId+");");
this.plotArea.appendChild(this.dataGroup);
dojo.svg.g.resume();
},drawAxes:function(){
dojo.svg.g.suspend();
if(this.axisGroup){
this.axisGroup.parentNode.removeChild(this.axisGroup);
this.axisGroup=null;
}
this.axisGroup=document.createElementNS(dojo.svg.xmlns.svg,"g");
this.plotArea.appendChild(this.axisGroup);
var _d=1;
var _e=document.createElementNS(dojo.svg.xmlns.svg,"line");
var y=dojo.widget.svg.Chart.Plotter.getY(this.properties.axes.x.plotAt,this);
_e.setAttribute("y1",y);
_e.setAttribute("y2",y);
_e.setAttribute("x1",this.properties.padding.left-_d);
_e.setAttribute("x2",this.properties.width-this.properties.padding.right);
_e.setAttribute("style","stroke:#000;stroke-width:"+_d+";");
this.axisGroup.appendChild(_e);
var _10=10;
var _11=document.createElementNS(dojo.svg.xmlns.svg,"text");
_11.setAttribute("x",this.properties.padding.left);
_11.setAttribute("y",this.properties.height-this.properties.padding.bottom+_10+2);
_11.setAttribute("style","text-anchor:middle;font-size:"+_10+"px;fill:#000;");
_11.appendChild(document.createTextNode(dojo.math.round(parseFloat(this.properties.axes.x.range.min),2)));
this.axisGroup.appendChild(_11);
var _11=document.createElementNS(dojo.svg.xmlns.svg,"text");
_11.setAttribute("x",this.properties.width-this.properties.padding.right-(_10/2));
_11.setAttribute("y",this.properties.height-this.properties.padding.bottom+_10+2);
_11.setAttribute("style","text-anchor:middle;font-size:"+_10+"px;fill:#000;");
_11.appendChild(document.createTextNode(dojo.math.round(parseFloat(this.properties.axes.x.range.max),2)));
this.axisGroup.appendChild(_11);
var _e=document.createElementNS(dojo.svg.xmlns.svg,"line");
var x=dojo.widget.svg.Chart.Plotter.getX(this.properties.axes.y.plotAt,this);
_e.setAttribute("x1",x);
_e.setAttribute("x2",x);
_e.setAttribute("y1",this.properties.padding.top);
_e.setAttribute("y2",this.properties.height-this.properties.padding.bottom);
_e.setAttribute("style","stroke:#000;stroke-width:"+_d+";");
this.axisGroup.appendChild(_e);
var _11=document.createElementNS(dojo.svg.xmlns.svg,"text");
_11.setAttribute("x",this.properties.padding.left-4);
_11.setAttribute("y",this.properties.height-this.properties.padding.bottom);
_11.setAttribute("style","text-anchor:end;font-size:"+_10+"px;fill:#000;");
_11.appendChild(document.createTextNode(dojo.math.round(parseFloat(this.properties.axes.y.range.min),2)));
this.axisGroup.appendChild(_11);
var _11=document.createElementNS(dojo.svg.xmlns.svg,"text");
_11.setAttribute("x",this.properties.padding.left-4);
_11.setAttribute("y",this.properties.padding.top+(_10/2));
_11.setAttribute("style","text-anchor:end;font-size:"+_10+"px;fill:#000;");
_11.appendChild(document.createTextNode(dojo.math.round(parseFloat(this.properties.axes.y.range.max),2)));
this.axisGroup.appendChild(_11);
dojo.svg.g.resume();
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
this.domNode.appendChild(this.vectorNode);
this.assignColors();
this._isInitialized=true;
},destroy:function(){
while(this.domNode.childNodes.length>0){
this.domNode.removeChild(this.domNode.childNodes.item(0));
}
this.vectorNode=this.plotArea=this.dataGroup=this.axisGroup=null;
},render:function(){
dojo.svg.g.suspend();
if(this.dataGroup){
while(this.dataGroup.childNodes.length>0){
this.dataGroup.removeChild(this.dataGroup.childNodes.item(0));
}
}else{
this.init();
}
for(var i=0;i<this.series.length;i++){
dojo.widget.svg.Chart.Plotter.plot(this.series[i],this);
}
dojo.svg.g.resume();
},postCreate:function(){
var _15=this.domNode.getElementsByTagName("table")[0];
if(_15){
var _16=this.parseProperties(_15);
var _17=false;
var _18=false;
var _19=this.parseData(_15);
if(!_17){
this.properties.axes.x.range={min:_19.x.min,max:_19.x.max};
}
if(!_18){
this.properties.axes.y.range={min:_19.y.min,max:_19.y.max};
}
this.setAxesPlot(_15);
this.domNode.removeChild(_15);
}
if(this.series.length>0){
this.render();
}
}});
dojo.widget.svg.Chart.Plotter=new function(){
var _1a=this;
var _1b={};
var _1c=dojo.widget.Chart.PlotTypes;
this.getX=function(_1d,_1e){
var v=parseFloat(_1d);
var min=_1e.properties.axes.x.range.min;
var max=_1e.properties.axes.x.range.max;
var _22=0-min;
min+=_22;
max+=_22;
v+=_22;
var _23=_1e.properties.padding.left;
var _24=_1e.properties.width-_1e.properties.padding.right;
var x=(v*((_24-_23)/max))+_23;
return x;
};
this.getY=function(_26,_27){
var v=parseFloat(_26);
var max=_27.properties.axes.y.range.max;
var min=_27.properties.axes.y.range.min;
var _2b=0;
if(min<0){
_2b+=Math.abs(min);
}
min+=_2b;
max+=_2b;
v+=_2b;
var _2c=_27.properties.height-_27.properties.padding.bottom;
var _2d=_27.properties.padding.top;
var y=(((_2c-_2d)/(max-min))*(max-v))+_2d;
return y;
};
this.addPlotter=function(_2f,_30){
_1b[_2f]=_30;
};
this.plot=function(_31,_32){
if(_31.values.length==0){
return;
}
if(_31.plotType&&_1b[_31.plotType]){
return _1b[_31.plotType](_31,_32);
}else{
if(_32.plotType&&_1b[_32.plotType]){
return _1b[_32.plotType](_31,_32);
}
}
};
_1b["bar"]=function(_33,_34){
var _35=1;
var _36=0;
for(var i=0;i<_33.values.length;i++){
var x=_1a.getX(_33.values[i].x,_34);
var w;
if(i==_33.values.length-1){
w=_36;
}else{
w=_1a.getX(_33.values[i+1].x,_34)-x-_35;
_36=w;
}
x-=(w/2);
var yA=_1a.getY(_34.properties.axes.x.plotAt,_34);
var y=_1a.getY(_33.values[i].value,_34);
var h=Math.abs(yA-y);
if(parseFloat(_33.values[i].value)<_34.properties.axes.x.plotAt){
var oy=yA;
yA=y;
y=oy;
}
var bar=document.createElementNS(dojo.svg.xmlns.svg,"rect");
bar.setAttribute("fill",_33.color);
bar.setAttribute("title",_33.label+": "+_33.values[i].value);
bar.setAttribute("stroke-width","0");
bar.setAttribute("x",x);
bar.setAttribute("y",y);
bar.setAttribute("width",w);
bar.setAttribute("height",h);
bar.setAttribute("fill-opacity","0.9");
_34.dataGroup.appendChild(bar);
}
};
_1b["line"]=function(_3f,_40){
var _41=1.5;
var _42=document.createElementNS(dojo.svg.xmlns.svg,"path");
_42.setAttribute("fill","none");
_42.setAttribute("stroke",_3f.color);
_42.setAttribute("stroke-width","2");
_42.setAttribute("stroke-opacity","0.85");
_42.setAttribute("title",_3f.label);
_40.dataGroup.appendChild(_42);
var _43=[];
for(var i=0;i<_3f.values.length;i++){
var x=_1a.getX(_3f.values[i].x,_40);
var y=_1a.getY(_3f.values[i].value,_40);
var dx=_40.properties.padding.left+1;
var dy=_40.properties.height-_40.properties.padding.bottom;
if(i>0){
dx=x-_1a.getX(_3f.values[i-1].x,_40);
dy=_1a.getY(_3f.values[i-1].value,_40);
}
if(i==0){
_43.push("M");
}else{
_43.push("C");
var cx=x-(_41-1)*(dx/_41);
_43.push(cx+","+dy);
cx=x-(dx/_41);
_43.push(cx+","+y);
}
_43.push(x+","+y);
}
_42.setAttribute("d",_43.join(" "));
};
_1b["area"]=function(_4a,_4b){
var _4c=1.5;
var _4d=document.createElementNS(dojo.svg.xmlns.svg,"path");
_4d.setAttribute("fill",_4a.color);
_4d.setAttribute("fill-opacity","0.4");
_4d.setAttribute("stroke",_4a.color);
_4d.setAttribute("stroke-width","1");
_4d.setAttribute("stroke-opacity","0.8");
_4d.setAttribute("title",_4a.label);
_4b.dataGroup.appendChild(_4d);
var _4e=[];
for(var i=0;i<_4a.values.length;i++){
var x=_1a.getX(_4a.values[i].x,_4b);
var y=_1a.getY(_4a.values[i].value,_4b);
var dx=_4b.properties.padding.left+1;
var dy=_4b.properties.height-_4b.properties.padding.bottom;
if(i>0){
dx=x-_1a.getX(_4a.values[i-1].x,_4b);
dy=_1a.getY(_4a.values[i-1].value,_4b);
}
if(i==0){
_4e.push("M");
}else{
_4e.push("C");
var cx=x-(_4c-1)*(dx/_4c);
_4e.push(cx+","+dy);
cx=x-(dx/_4c);
_4e.push(cx+","+y);
}
_4e.push(x+","+y);
}
_4e.push("L");
_4e.push(x+","+_1a.getY(0,_4b));
_4e.push("L");
_4e.push(_1a.getX(0,_4b)+","+_1a.getY(0,_4b));
_4e.push("Z");
_4d.setAttribute("d",_4e.join(" "));
},_1b["scatter"]=function(_55,_56){
var r=7;
for(var i=0;i<_55.values.length;i++){
var x=_1a.getX(_55.values[i].x,_56);
var y=_1a.getY(_55.values[i].value,_56);
var _5b=document.createElementNS(dojo.svg.xmlns.svg,"path");
_5b.setAttribute("fill",_55.color);
_5b.setAttribute("stroke-width","0");
_5b.setAttribute("title",_55.label+": "+_55.values[i].value);
_5b.setAttribute("d","M "+x+","+(y-r)+" "+"Q "+x+","+y+" "+(x+r)+","+y+" "+"Q "+x+","+y+" "+x+","+(y+r)+" "+"Q "+x+","+y+" "+(x-r)+","+y+" "+"Q "+x+","+y+" "+x+","+(y-r)+" "+"Z");
_56.dataGroup.appendChild(_5b);
}
};
_1b["bubble"]=function(_5c,_5d){
var _5e=1;
var min=_5d.properties.axes.x.range.min;
var max=_5d.properties.axes.x.range.max;
var _61=0-min;
min+=_61;
max+=_61;
var _62=_5d.properties.padding.left;
var _63=_5d.properties.width-_5d.properties.padding.right;
var _64=(max-min)/(_63-_62)*25;
for(var i=0;i<_5c.values.length;i++){
var _66=_5c.values[i].size;
if(isNaN(parseFloat(_66))){
_66=_5e;
}
var _67=document.createElementNS(dojo.svg.xmlns.svg,"circle");
_67.setAttribute("stroke-width",0);
_67.setAttribute("fill",_5c.color);
_67.setAttribute("fill-opacity","0.8");
_67.setAttribute("r",(parseFloat(_66)*_64)/2);
_67.setAttribute("cx",_1a.getX(_5c.values[i].x,_5d));
_67.setAttribute("cy",_1a.getY(_5c.values[i].value,_5d));
_67.setAttribute("title",_5c.label+": "+_5c.values[i].value+" ("+_66+")");
_5d.dataGroup.appendChild(_67);
}
};
}();
