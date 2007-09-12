dojo.provide("dojo.collections.Set");
dojo.require("dojo.collections.Collections");
dojo.require("dojo.collections.ArrayList");
dojo.collections.Set=new function(){
this.union=function(_1,_2){
if(_1.constructor==Array){
var _1=new dojo.collections.ArrayList(_1);
}
if(_2.constructor==Array){
var _2=new dojo.collections.ArrayList(_2);
}
if(!_1.toArray||!_2.toArray){
dojo.raise("Set operations can only be performed on array-based collections.");
}
var _3=new dojo.collections.ArrayList(_1.toArray());
var e=_2.getIterator();
while(!e.atEnd()){
var _5=e.get();
if(!_3.contains(_5)){
_3.add(_5);
}
}
return _3;
};
this.intersection=function(_6,_7){
if(_6.constructor==Array){
var _6=new dojo.collections.ArrayList(_6);
}
if(_7.constructor==Array){
var _7=new dojo.collections.ArrayList(_7);
}
if(!_6.toArray||!_7.toArray){
dojo.raise("Set operations can only be performed on array-based collections.");
}
var _8=new dojo.collections.ArrayList();
var e=_7.getIterator();
while(!e.atEnd()){
var _a=e.get();
if(_6.contains(_a)){
_8.add(_a);
}
}
return _8;
};
this.difference=function(_b,_c){
if(_b.constructor==Array){
var _b=new dojo.collections.ArrayList(_b);
}
if(_c.constructor==Array){
var _c=new dojo.collections.ArrayList(_c);
}
if(!_b.toArray||!_c.toArray){
dojo.raise("Set operations can only be performed on array-based collections.");
}
var _d=new dojo.collections.ArrayList();
var e=_b.getIterator();
while(!e.atEnd()){
var _f=e.get();
if(!_c.contains(_f)){
_d.add(_f);
}
}
return _d;
};
this.isSubSet=function(_10,_11){
if(_10.constructor==Array){
var _10=new dojo.collections.ArrayList(_10);
}
if(_11.constructor==Array){
var _11=new dojo.collections.ArrayList(_11);
}
if(!_10.toArray||!_11.toArray){
dojo.raise("Set operations can only be performed on array-based collections.");
}
var e=_10.getIterator();
while(!e.atEnd()){
if(!_11.contains(e.get())){
return false;
}
}
return true;
};
this.isSuperSet=function(_13,_14){
if(_13.constructor==Array){
var _13=new dojo.collections.ArrayList(_13);
}
if(_14.constructor==Array){
var _14=new dojo.collections.ArrayList(_14);
}
if(!_13.toArray||!_14.toArray){
dojo.raise("Set operations can only be performed on array-based collections.");
}
var e=_14.getIterator();
while(!e.atEnd()){
if(!_13.contains(e.get())){
return false;
}
}
return true;
};
}();
