dojo.provide("dojo.collections.Dictionary");
dojo.require("dojo.collections.Collections");
dojo.collections.Dictionary=function(_1){
var _2={};
this.count=0;
var _3={};
this.add=function(k,v){
var b=(k in _2);
_2[k]=new dojo.collections.DictionaryEntry(k,v);
if(!b){
this.count++;
}
};
this.clear=function(){
_2={};
this.count=0;
};
this.clone=function(){
return new dojo.collections.Dictionary(this);
};
this.contains=this.containsKey=function(k){
if(_3[k]){
return false;
}
return (_2[k]!=null);
};
this.containsValue=function(v){
var e=this.getIterator();
while(e.get()){
if(e.element.value==v){
return true;
}
}
return false;
};
this.entry=function(k){
return _2[k];
};
this.forEach=function(fn,_c){
var a=[];
for(var p in _2){
if(!_3[p]){
a.push(_2[p]);
}
}
var s=_c||dj_global;
if(Array.forEach){
Array.forEach(a,fn,s);
}else{
for(var i=0;i<a.length;i++){
fn.call(s,a[i],i,a);
}
}
};
this.getKeyList=function(){
return (this.getIterator()).map(function(_11){
return _11.key;
});
};
this.getValueList=function(){
return (this.getIterator()).map(function(_12){
return _12.value;
});
};
this.item=function(k){
if(k in _2){
return _2[k].valueOf();
}
return undefined;
};
this.getIterator=function(){
return new dojo.collections.DictionaryIterator(_2);
};
this.remove=function(k){
if(k in _2&&!_3[k]){
delete _2[k];
this.count--;
return true;
}
return false;
};
if(_1){
var e=_1.getIterator();
while(e.get()){
this.add(e.element.key,e.element.value);
}
}
};
