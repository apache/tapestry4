dojo.provide("dojo.widget.Chart");
dojo.require("dojo.widget.*");
dojo.require("dojo.gfx.color");
dojo.require("dojo.gfx.color.hsl");
dojo.declare("dojo.widget.Chart",null,function(){
this.series=[];
},{isContainer:false,assignColors:function(){
var _1=30;
var _2=120;
var _3=120;
var _4=Math.round(330/this.series.length);
for(var i=0;i<this.series.length;i++){
var c=dojo.gfx.color.hsl2rgb(_1,_2,_3);
if(!this.series[i].color){
this.series[i].color=dojo.gfx.color.rgb2hex(c[0],c[1],c[2]);
}
_1+=_4;
}
},parseData:function(_7){
var _8=_7.getElementsByTagName("thead")[0];
var _9=_7.getElementsByTagName("tbody")[0];
if(!(_8&&_9)){
dojo.raise("dojo.widget.Chart: supplied table must define a head and a body.");
}
var _a=_8.getElementsByTagName("tr")[0].getElementsByTagName("th");
for(var i=1;i<_a.length;i++){
var _c="column"+i;
var _d=_a[i].innerHTML;
var _e=_a[i].getAttribute("plotType")||"line";
var _f=_a[i].getAttribute("color");
var ds=new dojo.widget.Chart.DataSeries(_c,_d,_e,_f);
this.series.push(ds);
}
var _11=_9.rows;
var _12=Number.MAX_VALUE,_13=Number.MIN_VALUE;
var _14=Number.MAX_VALUE,_15=Number.MIN_VALUE;
var _16=["accesskey","align","bgcolor","class","colspan","height","id","nowrap","rowspan","style","tabindex","title","valign","width"];
for(var i=0;i<_11.length;i++){
var row=_11[i];
var _18=row.cells;
var x=Number.MIN_VALUE;
for(var j=0;j<_18.length;j++){
if(j==0){
x=parseFloat(_18[j].innerHTML);
_12=Math.min(_12,x);
_13=Math.max(_13,x);
}else{
var ds=this.series[j-1];
var y=parseFloat(_18[j].innerHTML);
_14=Math.min(_14,y);
_15=Math.max(_15,y);
var o={x:x,value:y};
var _1d=_18[j].attributes;
for(var k=0;k<_1d.length;k++){
var _1f=_1d.item(k);
var _20=false;
for(var l=0;l<_16.length;l++){
if(_1f.nodeName.toLowerCase()==_16[l]){
_20=true;
break;
}
}
if(!_20){
o[_1f.nodeName]=_1f.nodeValue;
}
}
ds.add(o);
}
}
}
return {x:{min:_12,max:_13},y:{min:_14,max:_15}};
}});
dojo.declare("dojo.widget.Chart.DataSeries",null,function(key,_23,_24,_25){
this.id="DataSeries"+dojo.widget.Chart.DataSeries.count++;
this.key=key;
this.label=_23||this.id;
this.plotType=_24||"line";
this.color=_25;
this.values=[];
},{add:function(v){
if(v.x==null||v.value==null){
dojo.raise("dojo.widget.Chart.DataSeries.add: v must have both an 'x' and 'value' property.");
}
this.values.push(v);
},clear:function(){
this.values=[];
},createRange:function(len){
var idx=this.values.length-1;
var _29=(len||this.values.length);
return {"index":idx,"length":_29,"start":Math.max(idx-_29,0)};
},getMean:function(len){
var _2b=this.createRange(len);
if(_2b.index<0){
return 0;
}
var t=0;
var c=0;
for(var i=_2b.index;i>=_2b.start;i--){
var n=parseFloat(this.values[i].value);
if(!isNaN(n)){
t+=n;
c++;
}
}
t/=Math.max(c,1);
return t;
},getMovingAverage:function(len){
var _31=this.createRange(len);
if(_31.index<0){
return 0;
}
var t=0;
var c=0;
for(var i=_31.index;i>=_31.start;i--){
var n=parseFloat(this.values[i].value);
if(!isNaN(n)){
t+=n;
c++;
}
}
t/=Math.max(c,1);
return t;
},getVariance:function(len){
var _37=this.createRange(len);
if(_37.index<0){
return 0;
}
var t=0;
var s=0;
var c=0;
for(var i=_37.index;i>=_37.start;i--){
var n=parseFloat(this.values[i].value);
if(!isNaN(n)){
t+=n;
s+=Math.pow(n,2);
c++;
}
}
return (s/c)-Math.pow(t/c,2);
},getStandardDeviation:function(len){
return Math.sqrt(this.getVariance(len));
},getMax:function(len){
var _3f=this.createRange(len);
if(_3f.index<0){
return 0;
}
var t=0;
for(var i=_3f.index;i>=_3f.start;i--){
var n=parseFloat(this.values[i].value);
if(!isNaN(n)){
t=Math.max(n,t);
}
}
return t;
},getMin:function(len){
var _44=this.createRange(len);
if(_44.index<0){
return 0;
}
var t=0;
for(var i=_44.index;i>=_44.start;i--){
var n=parseFloat(this.values[i].value);
if(!isNaN(n)){
t=Math.min(n,t);
}
}
return t;
},getMedian:function(len){
var _49=this.createRange(len);
if(_49.index<0){
return 0;
}
var a=[];
for(var i=_49.index;i>=_49.start;i--){
var n=parseFloat(this.values[i].value);
if(!isNaN(n)){
var b=false;
for(var j=0;j<a.length&&!b;j++){
if(n==a[j]){
b=true;
}
}
if(!b){
a.push(n);
}
}
}
a.sort();
if(a.length>0){
return a[Math.ceil(a.length/2)];
}
return 0;
},getMode:function(len){
var _50=this.createRange(len);
if(_50.index<0){
return 0;
}
var o={};
var ret=0;
var m=0;
for(var i=_50.index;i>=_50.start;i--){
var n=parseFloat(this.values[i].value);
if(!isNaN(n)){
if(!o[this.values[i].value]){
o[this.values[i].value]=1;
}else{
o[this.values[i].value]++;
}
}
}
for(var p in o){
if(m<o[p]){
m=o[p];
ret=p;
}
}
return parseFloat(ret);
}});
dojo.requireIf(dojo.render.svg.capable,"dojo.widget.svg.Chart");
dojo.requireIf(!dojo.render.svg.capable&&dojo.render.vml.capable,"dojo.widget.vml.Chart");
