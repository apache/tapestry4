dojo.provide("dojo.widget.TreeContextMenu");
dojo.require("dojo.event.*");
dojo.require("dojo.io.*");
dojo.require("dojo.widget.Menu2");
dojo.widget.defineWidget("dojo.widget.TreeContextMenu",dojo.widget.PopupMenu2,function(){
this.listenedTrees=[];
},{open:function(x,y,_3,_4){
var _5=dojo.widget.PopupMenu2.prototype.open.apply(this,arguments);
dojo.event.topic.publish(this.eventNames.open,{menu:this});
return _5;
},listenTree:function(_6){
var _7=_6.getDescendants();
for(var i=0;i<_7.length;i++){
if(!_7[i].isTreeNode){
continue;
}
this.bindDomNode(_7[i].labelNode);
}
var _9=this;
dojo.event.topic.subscribe(_6.eventNames.createDOMNode,this,"onCreateDOMNode");
dojo.event.topic.subscribe(_6.eventNames.moveFrom,this,"onMoveFrom");
dojo.event.topic.subscribe(_6.eventNames.moveTo,this,"onMoveTo");
dojo.event.topic.subscribe(_6.eventNames.removeNode,this,"onRemoveNode");
dojo.event.topic.subscribe(_6.eventNames.addChild,this,"onAddChild");
dojo.event.topic.subscribe(_6.eventNames.treeDestroy,this,"onTreeDestroy");
this.listenedTrees.push(_6);
},unlistenTree:function(_a){
dojo.event.topic.unsubscribe(_a.eventNames.createDOMNode,this,"onCreateDOMNode");
dojo.event.topic.unsubscribe(_a.eventNames.moveFrom,this,"onMoveFrom");
dojo.event.topic.unsubscribe(_a.eventNames.moveTo,this,"onMoveTo");
dojo.event.topic.unsubscribe(_a.eventNames.removeNode,this,"onRemoveNode");
dojo.event.topic.unsubscribe(_a.eventNames.addChild,this,"onAddChild");
dojo.event.topic.unsubscribe(_a.eventNames.treeDestroy,this,"onTreeDestroy");
for(var i=0;i<this.listenedTrees.length;i++){
if(this.listenedTrees[i]===_a){
this.listenedTrees.splice(i,1);
break;
}
}
},onTreeDestroy:function(_c){
this.unlistenTree(_c.source);
},bindTreeNode:function(_d){
var _e=this;
dojo.lang.forEach(_d.getDescendants(),function(e){
_e.bindDomNode(e.labelNode);
});
},unBindTreeNode:function(_10){
var _11=this;
dojo.lang.forEach(_10.getDescendants(),function(e){
_11.unBindDomNode(e.labelNode);
});
},onCreateDOMNode:function(_13){
this.bindTreeNode(_13.source);
},onMoveFrom:function(_14){
if(!dojo.lang.inArray(this.listenedTrees,_14.newTree)){
this.unBindTreeNode(_14.child);
}
},onMoveTo:function(_15){
if(dojo.lang.inArray(this.listenedTrees,_15.newTree)){
this.bindTreeNode(_15.child);
}
},onRemoveNode:function(_16){
this.unBindTreeNode(_16.child);
},onAddChild:function(_17){
if(_17.domNodeInitialized){
this.bindTreeNode(_17.child);
}
}});
dojo.widget.defineWidget("dojo.widget.TreeMenuItem",dojo.widget.MenuItem2,{treeActions:"",initialize:function(_18,_19){
this.treeActions=this.treeActions.split(",");
for(var i=0;i<this.treeActions.length;i++){
this.treeActions[i]=this.treeActions[i].toUpperCase();
}
},getTreeNode:function(){
var _1b=this;
while(!(_1b instanceof dojo.widget.TreeContextMenu)){
_1b=_1b.parent;
}
var _1c=_1b.getTopOpenEvent().target;
while(!_1c.getAttribute("treeNode")&&_1c.tagName!="body"){
_1c=_1c.parentNode;
}
if(_1c.tagName=="body"){
dojo.raise("treeNode not detected");
}
var _1d=dojo.widget.manager.getWidgetById(_1c.getAttribute("treeNode"));
return _1d;
},menuOpen:function(_1e){
var _1f=this.getTreeNode();
this.setDisabled(false);
var _20=this;
dojo.lang.forEach(_20.treeActions,function(_21){
_20.setDisabled(_1f.actionIsDisabled(_21));
});
},toString:function(){
return "["+this.widgetType+" node "+this.getTreeNode()+"]";
}});
