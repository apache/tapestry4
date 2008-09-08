dojo.provide("dojo.widget.TreeDndControllerV3");
dojo.require("dojo.dnd.TreeDragAndDropV3");
dojo.require("dojo.experimental");
dojo.experimental("Tree drag'n'drop' has lots of problems/bugs, it requires dojo drag'n'drop overhaul to work, probably in 0.5");
dojo.widget.defineWidget("dojo.widget.TreeDndControllerV3",[dojo.widget.HtmlWidget,dojo.widget.TreeCommon],function(){
this.dragSources={};
this.dropTargets={};
this.listenedTrees={};
},{listenTreeEvents:["afterChangeTree","beforeTreeDestroy","afterAddChild"],listenNodeFilter:function(_1){
return _1 instanceof dojo.widget.Widget;
},initialize:function(_2){
this.treeController=dojo.lang.isString(_2.controller)?dojo.widget.byId(_2.controller):_2.controller;
if(!this.treeController){
dojo.raise("treeController must be declared");
}
},onBeforeTreeDestroy:function(_3){
this.unlistenTree(_3.source);
},onAfterAddChild:function(_4){
this.listenNode(_4.child);
},onAfterChangeTree:function(_5){
if(!_5.oldTree){
return;
}
if(!_5.newTree||!this.listenedTrees[_5.newTree.widgetId]){
this.processDescendants(_5.node,this.listenNodeFilter,this.unlistenNode);
}
if(!this.listenedTrees[_5.oldTree.widgetId]){
this.processDescendants(_5.node,this.listenNodeFilter,this.listenNode);
}
},listenNode:function(_6){
if(!_6.tree.DndMode){
return;
}
if(this.dragSources[_6.widgetId]||this.dropTargets[_6.widgetId]){
return;
}
var _7=null;
var _8=null;
if(!_6.actionIsDisabled(_6.actions.MOVE)){
var _7=this.makeDragSource(_6);
this.dragSources[_6.widgetId]=_7;
}
var _8=this.makeDropTarget(_6);
this.dropTargets[_6.widgetId]=_8;
},makeDragSource:function(_9){
return new dojo.dnd.TreeDragSourceV3(_9.contentNode,this,_9.tree.widgetId,_9);
},makeDropTarget:function(_a){
return new dojo.dnd.TreeDropTargetV3(_a.contentNode,this.treeController,_a.tree.DndAcceptTypes,_a);
},unlistenNode:function(_b){
if(this.dragSources[_b.widgetId]){
dojo.dnd.dragManager.unregisterDragSource(this.dragSources[_b.widgetId]);
delete this.dragSources[_b.widgetId];
}
if(this.dropTargets[_b.widgetId]){
dojo.dnd.dragManager.unregisterDropTarget(this.dropTargets[_b.widgetId]);
delete this.dropTargets[_b.widgetId];
}
}});
