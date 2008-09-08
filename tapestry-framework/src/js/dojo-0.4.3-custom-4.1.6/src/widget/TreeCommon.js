dojo.provide("dojo.widget.TreeCommon");
dojo.require("dojo.widget.*");
dojo.declare("dojo.widget.TreeCommon",null,{listenTreeEvents:[],listenedTrees:{},listenNodeFilter:null,listenTree:function(_1){
var _2=this;
if(this.listenedTrees[_1.widgetId]){
return;
}
dojo.lang.forEach(this.listenTreeEvents,function(_3){
var _4="on"+_3.charAt(0).toUpperCase()+_3.substr(1);
dojo.event.topic.subscribe(_1.eventNames[_3],_2,_4);
});
var _5;
if(this.listenNodeFilter){
this.processDescendants(_1,this.listenNodeFilter,this.listenNode,true);
}
this.listenedTrees[_1.widgetId]=true;
},listenNode:function(){
},unlistenNode:function(){
},unlistenTree:function(_6,_7){
var _8=this;
if(!this.listenedTrees[_6.widgetId]){
return;
}
dojo.lang.forEach(this.listenTreeEvents,function(_9){
var _a="on"+_9.charAt(0).toUpperCase()+_9.substr(1);
dojo.event.topic.unsubscribe(_6.eventNames[_9],_8,_a);
});
if(this.listenNodeFilter){
this.processDescendants(_6,this.listenNodeFilter,this.unlistenNode,true);
}
delete this.listenedTrees[_6.widgetId];
},checkPathCondition:function(_b,_c){
while(_b&&!_b.widgetId){
if(_c.call(null,_b)){
return true;
}
_b=_b.parentNode;
}
return false;
},domElement2TreeNode:function(_d){
while(_d&&!_d.widgetId){
_d=_d.parentNode;
}
if(!_d){
return null;
}
var _e=dojo.widget.byId(_d.widgetId);
if(!_e.isTreeNode){
return null;
}
return _e;
},processDescendants:function(_f,_10,_11,_12){
var _13=this;
if(!_12){
if(!_10.call(_13,_f)){
return;
}
_11.call(_13,_f);
}
var _14=[_f];
while(_f=_14.pop()){
dojo.lang.forEach(_f.children,function(_15){
if(_10.call(_13,_15)){
_11.call(_13,_15);
_14.push(_15);
}
});
}
}});
