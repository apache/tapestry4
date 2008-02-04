dojo.provide("dojo.charting.Axis");
dojo.require("dojo.lang.common");
dojo.charting.Axis=function(_1,_2,_3){
var id="dojo-charting-axis-"+dojo.charting.Axis.count++;
this.getId=function(){
return id;
};
this.setId=function(_5){
id=_5;
};
this.scale=_2||"linear";
this.label=_1||"";
this.showLabel=true;
this.showLabels=true;
this.showLines=false;
this.showTicks=false;
this.range={upper:100,lower:0};
this.origin="min";
this._origin=null;
this.labels=_3||[];
this._labels=[];
this.nodes={main:null,axis:null,label:null,labels:null,lines:null,ticks:null};
this._rerender=false;
};
dojo.charting.Axis.count=0;
dojo.extend(dojo.charting.Axis,{getCoord:function(_6,_7,_8){
_6=parseFloat(_6,10);
var _9=_7.getArea();
if(_8.axisX==this){
var _a=0-this.range.lower;
var _b=this.range.lower+_a;
var _c=this.range.upper+_a;
_6+=_a;
return (_6*((_9.right-_9.left)/_c))+_9.left;
}else{
var _c=this.range.upper;
var _b=this.range.lower;
var _a=0;
if(_b<0){
_a+=Math.abs(_b);
}
_c+=_a;
_b+=_a;
_6+=_a;
var _d=_9.bottom;
var _e=_9.top;
return (((_d-_e)/(_c-_b))*(_c-_6))+_e;
}
},initializeOrigin:function(_f,_10){
if(this._origin==null){
this._origin=this.origin;
}
if(isNaN(this._origin)){
if(this._origin.toLowerCase()=="max"){
this.origin=_f.range[(_10=="y")?"upper":"lower"];
}else{
if(this._origin.toLowerCase()=="min"){
this.origin=_f.range[(_10=="y")?"lower":"upper"];
}else{
this.origin=0;
}
}
}
},initializeLabels:function(){
this._labels=[];
if(this.labels.length==0){
this.showLabels=false;
this.showLines=false;
this.showTicks=false;
}else{
if(this.labels[0].label&&this.labels[0].value!=null){
for(var i=0;i<this.labels.length;i++){
this._labels.push(this.labels[i]);
}
}else{
if(!isNaN(this.labels[0])){
for(var i=0;i<this.labels.length;i++){
this._labels.push({label:this.labels[i],value:this.labels[i]});
}
}else{
var a=[];
for(var i=0;i<this.labels.length;i++){
a.push(this.labels[i]);
}
var s=a.shift();
this._labels.push({label:s,value:this.range.lower});
if(a.length>0){
var s=a.pop();
this._labels.push({label:s,value:this.range.upper});
}
if(a.length>0){
var _14=this.range.upper-this.range.lower;
var _15=_14/(this.labels.length-1);
for(var i=1;i<=a.length;i++){
this._labels.push({label:a[i-1],value:this.range.lower+(_15*i)});
}
}
}
}
}
},initialize:function(_16,_17,_18,_19){
this.destroy();
this.initializeOrigin(_18,_19);
this.initializeLabels();
var _1a=this.render(_16,_17,_18,_19);
return _1a;
},destroy:function(){
for(var p in this.nodes){
while(this.nodes[p]&&this.nodes[p].childNodes.length>0){
this.nodes[p].removeChild(this.nodes[p].childNodes[0]);
}
if(this.nodes[p]&&this.nodes[p].parentNode){
this.nodes[p].parentNode.removeChild(this.nodes[p]);
}
this.nodes[p]=null;
}
}});
dojo.requireIf(dojo.render.svg.capable,"dojo.charting.svg.Axis");
dojo.requireIf(dojo.render.vml.capable,"dojo.charting.vml.Axis");
