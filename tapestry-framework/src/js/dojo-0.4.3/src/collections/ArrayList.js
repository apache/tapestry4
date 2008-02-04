dojo.provide("dojo.collections.ArrayList");
dojo.require("dojo.collections.Collections");
dojo.collections.ArrayList=function(_1){
var _2=[];
if(_1){
_2=_2.concat(_1);
}
this.count=_2.length;
this.add=function(_3){
_2.push(_3);
this.count=_2.length;
};
this.addRange=function(a){
if(a.getIterator){
var e=a.getIterator();
while(!e.atEnd()){
this.add(e.get());
}
this.count=_2.length;
}else{
for(var i=0;i<a.length;i++){
_2.push(a[i]);
}
this.count=_2.length;
}
};
this.clear=function(){
_2.splice(0,_2.length);
this.count=0;
};
this.clone=function(){
return new dojo.collections.ArrayList(_2);
};
this.contains=function(_7){
for(var i=0;i<_2.length;i++){
if(_2[i]==_7){
return true;
}
}
return false;
};
this.forEach=function(fn,_a){
var s=_a||dj_global;
if(Array.forEach){
Array.forEach(_2,fn,s);
}else{
for(var i=0;i<_2.length;i++){
fn.call(s,_2[i],i,_2);
}
}
};
this.getIterator=function(){
return new dojo.collections.Iterator(_2);
};
this.indexOf=function(_d){
for(var i=0;i<_2.length;i++){
if(_2[i]==_d){
return i;
}
}
return -1;
};
this.insert=function(i,obj){
_2.splice(i,0,obj);
this.count=_2.length;
};
this.item=function(i){
return _2[i];
};
this.remove=function(obj){
var i=this.indexOf(obj);
if(i>=0){
_2.splice(i,1);
}
this.count=_2.length;
};
this.removeAt=function(i){
_2.splice(i,1);
this.count=_2.length;
};
this.reverse=function(){
_2.reverse();
};
this.sort=function(fn){
if(fn){
_2.sort(fn);
}else{
_2.sort();
}
};
this.setByIndex=function(i,obj){
_2[i]=obj;
this.count=_2.length;
};
this.toArray=function(){
return [].concat(_2);
};
this.toString=function(_18){
return _2.join((_18||","));
};
};
