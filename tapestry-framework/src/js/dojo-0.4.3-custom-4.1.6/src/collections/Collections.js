dojo.provide("dojo.collections.Collections");
dojo.collections.DictionaryEntry=function(k,v){
this.key=k;
this.value=v;
this.valueOf=function(){
return this.value;
};
this.toString=function(){
return String(this.value);
};
};
dojo.collections.Iterator=function(_3){
var a=_3;
var _5=0;
this.element=a[_5]||null;
this.atEnd=function(){
return (_5>=a.length);
};
this.get=function(){
if(this.atEnd()){
return null;
}
this.element=a[_5++];
return this.element;
};
this.map=function(fn,_7){
var s=_7||dj_global;
if(Array.map){
return Array.map(a,fn,s);
}else{
var _9=[];
for(var i=0;i<a.length;i++){
_9.push(fn.call(s,a[i]));
}
return _9;
}
};
this.reset=function(){
_5=0;
this.element=a[_5];
};
};
dojo.collections.DictionaryIterator=function(_b){
var a=[];
var _d={};
for(var p in _b){
if(!_d[p]){
a.push(_b[p]);
}
}
var _f=0;
this.element=a[_f]||null;
this.atEnd=function(){
return (_f>=a.length);
};
this.get=function(){
if(this.atEnd()){
return null;
}
this.element=a[_f++];
return this.element;
};
this.map=function(fn,_11){
var s=_11||dj_global;
if(Array.map){
return Array.map(a,fn,s);
}else{
var arr=[];
for(var i=0;i<a.length;i++){
arr.push(fn.call(s,a[i]));
}
return arr;
}
};
this.reset=function(){
_f=0;
this.element=a[_f];
};
};
