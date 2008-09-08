dojo.provide("dojo.charting.Series");
dojo.require("dojo.lang.common");
dojo.require("dojo.charting.Plotters");
dojo.charting.Series=function(_1){
var _2=_1||{length:1};
this.dataSource=_2.dataSource||null;
this.bindings={};
this.color=_2.color;
this.label=_2.label;
if(_2.bindings){
for(var p in _2.bindings){
this.addBinding(p,_2.bindings[p]);
}
}
};
dojo.extend(dojo.charting.Series,{bind:function(_4,_5){
this.dataSource=_4;
this.bindings=_5;
},addBinding:function(_6,_7){
this.bindings[_6]=_7;
},evaluate:function(_8){
var _9=[];
var a=this.dataSource.getData();
var l=a.length;
var _c=0;
var _d=l;
if(_8){
if(_8.between){
for(var i=0;i<l;i++){
var _f=this.dataSource.getField(a[i],_8.between.field);
if(_f>=_8.between.low&&_f<=_8.between.high){
var o={src:a[i],series:this};
for(var p in this.bindings){
o[p]=this.dataSource.getField(a[i],this.bindings[p]);
}
_9.push(o);
}
}
}else{
if(_8.from||_8.length){
if(_8.from){
_c=Math.max(_8.from,0);
if(_8.to){
_d=Math.min(_8.to,_d);
}
}else{
if(_8.length<0){
_c=Math.max((_d+length),0);
}else{
_d=Math.min((_c+length),_d);
}
}
for(var i=_c;i<_d;i++){
var o={src:a[i],series:this};
for(var p in this.bindings){
o[p]=this.dataSource.getField(a[i],this.bindings[p]);
}
_9.push(o);
}
}
}
}else{
for(var i=_c;i<_d;i++){
var o={src:a[i],series:this};
for(var p in this.bindings){
o[p]=this.dataSource.getField(a[i],this.bindings[p]);
}
_9.push(o);
}
}
if(_9.length>0&&typeof (_9[0].x)!="undefined"){
_9.sort(function(a,b){
if(a.x>b.x){
return 1;
}
if(a.x<b.x){
return -1;
}
return 0;
});
}
return _9;
},trends:{createRange:function(_14,len){
var idx=_14.length-1;
var _17=(len||_14.length);
return {"index":idx,"length":_17,"start":Math.max(idx-_17,0)};
},mean:function(_18,len){
var _1a=this.createRange(_18,len);
if(_1a.index<0){
return 0;
}
var _1b=0;
var _1c=0;
for(var i=_1a.index;i>=_1a.start;i--){
_1b+=_18[i].y;
_1c++;
}
_1b/=Math.max(_1c,1);
return _1b;
},variance:function(_1e,len){
var _20=this.createRange(_1e,len);
if(_20.index<0){
return 0;
}
var _21=0;
var _22=0;
var _23=0;
for(var i=_20.index;i>=_20.start;i--){
_21+=_1e[i].y;
_22+=Math.pow(_1e[i].y,2);
_23++;
}
return (_22/_23)-Math.pow(_21/_23,2);
},standardDeviation:function(_25,len){
return Math.sqrt(this.getVariance(_25,len));
},max:function(_27,len){
var _29=this.createRange(_27,len);
if(_29.index<0){
return 0;
}
var max=Number.MIN_VALUE;
for(var i=_29.index;i>=_29.start;i--){
max=Math.max(_27[i].y,max);
}
return max;
},min:function(_2c,len){
var _2e=this.createRange(_2c,len);
if(_2e.index<0){
return 0;
}
var min=Number.MAX_VALUE;
for(var i=_2e.index;i>=_2e.start;i--){
min=Math.min(_2c[i].y,min);
}
return min;
},median:function(_31,len){
var _33=this.createRange(_31,len);
if(_33.index<0){
return 0;
}
var a=[];
for(var i=_33.index;i>=_33.start;i--){
var b=false;
for(var j=0;j<a.length;j++){
if(_31[i].y==a[j]){
b=true;
break;
}
}
if(!b){
a.push(_31[i].y);
}
}
a.sort();
if(a.length>0){
return a[Math.ceil(a.length/2)];
}
return 0;
},mode:function(_38,len){
var _3a=this.createRange(_38,len);
if(_3a.index<0){
return 0;
}
var o={};
var ret=0;
var _3d=Number.MIN_VALUE;
for(var i=_3a.index;i>=_3a.start;i--){
if(!o[_38[i].y]){
o[_38[i].y]=1;
}else{
o[_38[i].y]++;
}
}
for(var p in o){
if(_3d<o[p]){
_3d=o[p];
ret=p;
}
}
return ret;
}}});
