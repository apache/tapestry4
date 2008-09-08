dojo.provide("dojo.collections.SkipList");
dojo.require("dojo.collections.Collections");
dojo.require("dojo.experimental");
dojo.experimental("dojo.collections.SkipList");
dojo.collections.SkipList=function(){
function node(_1,_2){
this.value=_2;
this.height=_1;
this.nodes=new nodeList(_1);
this.compare=function(_3){
if(this.value>_3){
return 1;
}
if(this.value<_3){
return -1;
}
return 0;
};
this.incrementHeight=function(){
this.nodes.incrementHeight();
this.height++;
};
this.decrementHeight=function(){
this.nodes.decrementHeight();
this.height--;
};
}
function nodeList(_4){
var _5=[];
this.height=_4;
for(var i=0;i<_4;i++){
_5[i]=null;
}
this.item=function(i){
return _5[i];
};
this.incrementHeight=function(){
this.height++;
_5[this.height]=null;
};
this.decrementHeight=function(){
_5.splice(_5.length-1,1);
this.height--;
};
}
function iterator(_8){
this.element=_8.head;
this.atEnd=function(){
return (this.element==null);
};
this.get=function(){
if(this.atEnd()){
return null;
}
this.element=this.element.nodes[0];
return this.element;
};
this.reset=function(){
this.element=_8.head;
};
}
function chooseRandomHeight(_9){
var _a=1;
while(Math.random()<_b&&_a<_9){
_a++;
}
return _a;
}
var _b=0.5;
var _c=0;
this.head=new node(1);
this.count=0;
this.add=function(_d){
var _e=[];
var _f=this.head;
for(var i=this.head.height;i>=0;i--){
if(!(_f.nodes[i]!=null&&_f.nodes[i].compare(_d)<0)){
_c++;
}
while(_f.nodes[i]!=null&&_f.nodes[i].compare(_d)<0){
_f=_f.nodes[i];
_c++;
}
_e[i]=_f;
}
if(_f.nodes[0]!=null&&_f.nodes[0].compare(_d)==0){
return;
}
var n=new node(_d,chooseRandomHeight(this.head.height+1));
this.count++;
if(n.height>this.head.height){
this.head.incrementHeight();
this.head.nodes[this.head.height-1]=n;
}
for(i=0;i<n.height;i++){
if(i<_e.length){
n.nodes[i]=_e[i].nodes[i];
_e[i].nodes[i]=n;
}
}
};
this.contains=function(val){
var _13=this.head;
var i;
for(i=this.head.height-1;i>=0;i--){
while(_13.item(i)!=null){
_c++;
var _15=_13.nodes[i].compare(val);
if(_15==0){
return true;
}else{
if(_15<0){
_13=_13.nodes[i];
}else{
break;
}
}
}
}
return false;
};
this.getIterator=function(){
return new iterator(this);
};
this.remove=function(val){
var _17=[];
var _18=this.head;
for(var i=this.head.height-1;i>=0;i--){
if(!(_18.nodes[i]!=null&&_18.nodes[i].compare(val)<0)){
_c++;
}
while(_18.nodes[i]!=null&&_18.nodes[i].compare(val)<0){
_18=_18.nodes[i];
_c++;
}
_17[i]=_18;
}
_18=_18.nodes[0];
if(_18!=null&&_18.compare(val)==0){
this.count--;
for(var i=0;i<this.head.height;i++){
if(_17[i].nodes[i]!=_18){
break;
}else{
_17[i].nodes[i]=_18.nodes[i];
}
}
if(this.head.nodes[this.head.height-1]==null){
this.head.decrementHeight();
}
}
};
this.resetComparisons=function(){
_c=0;
};
};
