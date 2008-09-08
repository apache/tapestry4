dojo.provide("dojo.widget.TreeTimeoutIterator");
dojo.require("dojo.event.*");
dojo.require("dojo.json");
dojo.require("dojo.io.*");
dojo.require("dojo.widget.TreeCommon");
dojo.declare("dojo.widget.TreeTimeoutIterator",null,function(_1,_2,_3){
var _4=this;
this.currentParent=_1;
this.callFunc=_2;
this.callObj=_3?_3:this;
this.stack=[];
},{maxStackDepth:Number.POSITIVE_INFINITY,stack:null,currentParent:null,currentIndex:0,filterFunc:function(){
return true;
},finishFunc:function(){
return true;
},setFilter:function(_5,_6){
this.filterFunc=_5;
this.filterObj=_6;
},setMaxLevel:function(_7){
this.maxStackDepth=_7-2;
},forward:function(_8){
var _9=this;
if(this.timeout){
var _a=setTimeout(function(){
_9.processNext();
clearTimeout(_a);
},_9.timeout);
}else{
return this.processNext();
}
},start:function(_b){
if(_b){
return this.callFunc.call(this.callObj,this.currentParent,this);
}
return this.processNext();
},processNext:function(){
var _c;
var _d=this;
var _e;
var _f;
if(this.maxStackDepth==-2){
return;
}
while(true){
var _10=this.currentParent.children;
if(_10&&_10.length){
do{
_f=_10[this.currentIndex];
}while(this.currentIndex++<_10.length&&!(_e=this.filterFunc.call(this.filterObj,_f)));
if(_e){
if(_f.isFolder&&this.stack.length<=this.maxStackDepth){
this.moveParent(_f,0);
}
return this.callFunc.call(this.callObj,_f,this);
}
}
if(this.stack.length){
this.popParent();
continue;
}
break;
}
return this.finishFunc.call(this.finishObj);
},setFinish:function(_11,obj){
this.finishFunc=_11;
this.finishObj=obj;
},popParent:function(){
var p=this.stack.pop();
this.currentParent=p[0];
this.currentIndex=p[1];
},moveParent:function(_14,_15){
this.stack.push([this.currentParent,this.currentIndex]);
this.currentParent=_14;
this.currentIndex=_15;
}});
