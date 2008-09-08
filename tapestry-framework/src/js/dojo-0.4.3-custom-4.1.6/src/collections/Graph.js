dojo.provide("dojo.collections.Graph");
dojo.require("dojo.collections.Collections");
dojo.experimental("dojo.collections.Graph");
dojo.collections.Graph=function(_1){
function node(_2,_3,_4){
this.key=_2;
this.data=_3;
this.neighbors=_4||new adjacencyList();
this.addDirected=function(){
if(arguments[0].constructor==edgeToNeighbor){
this.neighbors.add(arguments[0]);
}else{
var n=arguments[0];
var _6=arguments[1]||0;
this.neighbors.add(new edgeToNeighbor(n,_6));
}
};
}
function nodeList(){
var d=new dojo.collections.Dictionary();
function nodelistiterator(){
var o=[];
var e=d.getIterator();
while(e.get()){
o[o.length]=e.element;
}
var _a=0;
this.element=o[_a]||null;
this.atEnd=function(){
return (_a>=o.length);
};
this.get=function(){
if(this.atEnd()){
return null;
}
this.element=o[_a++];
return this.element;
};
this.map=function(fn,_c){
var s=_c||dj_global;
if(Array.map){
return Array.map(o,fn,s);
}else{
var _e=[];
for(var i=0;i<o.length;i++){
_e.push(fn.call(s,o[i]));
}
return _e;
}
};
this.reset=function(){
_a=0;
this.element=o[_a];
};
}
this.add=function(_10){
d.add(_10.key,_10);
};
this.clear=function(){
d.clear();
};
this.containsKey=function(key){
return d.containsKey(key);
};
this.getIterator=function(){
return new nodelistiterator(this);
};
this.item=function(key){
return d.item(key);
};
this.remove=function(_13){
d.remove(_13.key);
};
}
function edgeToNeighbor(_14,_15){
this.neighbor=_14;
this.cost=_15;
}
function adjacencyList(){
var d=[];
this.add=function(o){
d.push(o);
};
this.item=function(i){
return d[i];
};
this.getIterator=function(){
return new dojo.collections.Iterator([].concat(d));
};
}
this.nodes=_1||new nodeList();
this.count=this.nodes.count;
this.clear=function(){
this.nodes.clear();
this.count=0;
};
this.addNode=function(){
var n=arguments[0];
if(arguments.length>1){
n=new node(arguments[0],arguments[1]);
}
if(!this.nodes.containsKey(n.key)){
this.nodes.add(n);
this.count++;
}
};
this.addDirectedEdge=function(_1a,_1b,_1c){
var _1d,_1e;
if(_1a.constructor!=node){
_1d=this.nodes.item(_1a);
_1e=this.nodes.item(_1b);
}else{
_1d=_1a;
_1e=_1b;
}
var c=_1c||0;
_1d.addDirected(_1e,c);
};
this.addUndirectedEdge=function(_20,_21,_22){
var _23,_24;
if(_20.constructor!=node){
_23=this.nodes.item(_20);
_24=this.nodes.item(_21);
}else{
_23=_20;
_24=_21;
}
var c=_22||0;
_23.addDirected(_24,c);
_24.addDirected(_23,c);
};
this.contains=function(n){
return this.nodes.containsKey(n.key);
};
this.containsKey=function(k){
return this.nodes.containsKey(k);
};
};
