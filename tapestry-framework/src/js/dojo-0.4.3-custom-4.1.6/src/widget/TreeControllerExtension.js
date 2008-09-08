dojo.provide("dojo.widget.TreeControllerExtension");
dojo.declare("dojo.widget.TreeControllerExtension",null,{saveExpandedIndices:function(_1,_2){
var _3={};
for(var i=0;i<_1.children.length;i++){
if(_1.children[i].isExpanded){
var _5=dojo.lang.isUndefined(_2)?i:_1.children[i][_2];
_3[_5]=this.saveExpandedIndices(_1.children[i],_2);
}
}
return _3;
},restoreExpandedIndices:function(_6,_7,_8){
var _9=this;
var _a=function(_b,_c){
this.node=_b;
this.savedIndices=_c;
this.process=function(){
_9.restoreExpandedIndices(this.node,this.savedIndices,_8);
};
};
for(var i=0;i<_6.children.length;i++){
var _e=_6.children[i];
var _f=false;
var key=-1;
if(dojo.lang.isUndefined(_8)&&_7[i]){
_f=true;
key=i;
}
if(_8){
for(var key in _7){
if(key==_e[_8]){
_f=true;
break;
}
}
}
if(_f){
var h=new _a(_e,_7[key]);
_9.expand(_e,false,h,h.process);
}else{
if(_e.isExpanded){
dojo.lang.forEach(_e.getDescendants(),function(_12){
_9.collapse(_12);
});
}
}
}
}});
