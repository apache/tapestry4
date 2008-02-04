dojo.provide("dojo.collections.SortedList");
dojo.require("dojo.collections.Collections");
dojo.collections.SortedList=function(_1){
var _2=this;
var _3={};
var q=[];
var _5=function(a,b){
if(a.key>b.key){
return 1;
}
if(a.key<b.key){
return -1;
}
return 0;
};
var _8=function(){
q=[];
var e=_2.getIterator();
while(!e.atEnd()){
q.push(e.get());
}
q.sort(_5);
};
var _a={};
this.count=q.length;
this.add=function(k,v){
if(!_3[k]){
_3[k]=new dojo.collections.DictionaryEntry(k,v);
this.count=q.push(_3[k]);
q.sort(_5);
}
};
this.clear=function(){
_3={};
q=[];
this.count=q.length;
};
this.clone=function(){
return new dojo.collections.SortedList(this);
};
this.contains=this.containsKey=function(k){
if(_a[k]){
return false;
}
return (_3[k]!=null);
};
this.containsValue=function(o){
var e=this.getIterator();
while(!e.atEnd()){
var _10=e.get();
if(_10.value==o){
return true;
}
}
return false;
};
this.copyTo=function(arr,i){
var e=this.getIterator();
var idx=i;
while(!e.atEnd()){
arr.splice(idx,0,e.get());
idx++;
}
};
this.entry=function(k){
return _3[k];
};
this.forEach=function(fn,_17){
var s=_17||dj_global;
if(Array.forEach){
Array.forEach(q,fn,s);
}else{
for(var i=0;i<q.length;i++){
fn.call(s,q[i],i,q);
}
}
};
this.getByIndex=function(i){
return q[i].valueOf();
};
this.getIterator=function(){
return new dojo.collections.DictionaryIterator(_3);
};
this.getKey=function(i){
return q[i].key;
};
this.getKeyList=function(){
var arr=[];
var e=this.getIterator();
while(!e.atEnd()){
arr.push(e.get().key);
}
return arr;
};
this.getValueList=function(){
var arr=[];
var e=this.getIterator();
while(!e.atEnd()){
arr.push(e.get().value);
}
return arr;
};
this.indexOfKey=function(k){
for(var i=0;i<q.length;i++){
if(q[i].key==k){
return i;
}
}
return -1;
};
this.indexOfValue=function(o){
for(var i=0;i<q.length;i++){
if(q[i].value==o){
return i;
}
}
return -1;
};
this.item=function(k){
if(k in _3&&!_a[k]){
return _3[k].valueOf();
}
return undefined;
};
this.remove=function(k){
delete _3[k];
_8();
this.count=q.length;
};
this.removeAt=function(i){
delete _3[q[i].key];
_8();
this.count=q.length;
};
this.replace=function(k,v){
if(!_3[k]){
this.add(k,v);
return false;
}else{
_3[k]=new dojo.collections.DictionaryEntry(k,v);
q.sort(_5);
return true;
}
};
this.setByIndex=function(i,o){
_3[q[i].key].value=o;
_8();
this.count=q.length;
};
if(_1){
var e=_1.getIterator();
while(!e.atEnd()){
var _2c=e.get();
q[q.length]=_3[_2c.key]=new dojo.collections.DictionaryEntry(_2c.key,_2c.value);
}
q.sort(_5);
}
};
