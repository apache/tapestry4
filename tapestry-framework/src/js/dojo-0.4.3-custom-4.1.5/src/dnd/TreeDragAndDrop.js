dojo.provide("dojo.dnd.TreeDragAndDrop");
dojo.require("dojo.dnd.HtmlDragAndDrop");
dojo.require("dojo.lang.func");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.extras");
dojo.require("dojo.html.layout");
dojo.dnd.TreeDragSource=function(_1,_2,_3,_4){
this.controller=_2;
this.treeNode=_4;
dojo.dnd.HtmlDragSource.call(this,_1,_3);
};
dojo.inherits(dojo.dnd.TreeDragSource,dojo.dnd.HtmlDragSource);
dojo.lang.extend(dojo.dnd.TreeDragSource,{onDragStart:function(){
var _5=dojo.dnd.HtmlDragSource.prototype.onDragStart.call(this);
_5.treeNode=this.treeNode;
_5.onDragStart=dojo.lang.hitch(_5,function(e){
this.savedSelectedNode=this.treeNode.tree.selector.selectedNode;
if(this.savedSelectedNode){
this.savedSelectedNode.unMarkSelected();
}
var _7=dojo.dnd.HtmlDragObject.prototype.onDragStart.apply(this,arguments);
var _8=this.dragClone.getElementsByTagName("img");
for(var i=0;i<_8.length;i++){
_8.item(i).style.backgroundImage="url()";
}
return _7;
});
_5.onDragEnd=function(e){
if(this.savedSelectedNode){
this.savedSelectedNode.markSelected();
}
return dojo.dnd.HtmlDragObject.prototype.onDragEnd.apply(this,arguments);
};
return _5;
},onDragEnd:function(e){
var _c=dojo.dnd.HtmlDragSource.prototype.onDragEnd.call(this,e);
return _c;
}});
dojo.dnd.TreeDropTarget=function(_d,_e,_f,_10){
this.treeNode=_10;
this.controller=_e;
dojo.dnd.HtmlDropTarget.apply(this,[_d,_f]);
};
dojo.inherits(dojo.dnd.TreeDropTarget,dojo.dnd.HtmlDropTarget);
dojo.lang.extend(dojo.dnd.TreeDropTarget,{autoExpandDelay:1500,autoExpandTimer:null,position:null,indicatorStyle:"2px black solid",showIndicator:function(_11){
if(this.position==_11){
return;
}
this.hideIndicator();
this.position=_11;
if(_11=="before"){
this.treeNode.labelNode.style.borderTop=this.indicatorStyle;
}else{
if(_11=="after"){
this.treeNode.labelNode.style.borderBottom=this.indicatorStyle;
}else{
if(_11=="onto"){
this.treeNode.markSelected();
}
}
}
},hideIndicator:function(){
this.treeNode.labelNode.style.borderBottom="";
this.treeNode.labelNode.style.borderTop="";
this.treeNode.unMarkSelected();
this.position=null;
},onDragOver:function(e){
var _13=dojo.dnd.HtmlDropTarget.prototype.onDragOver.apply(this,arguments);
if(_13&&this.treeNode.isFolder&&!this.treeNode.isExpanded){
this.setAutoExpandTimer();
}
return _13;
},accepts:function(_14){
var _15=dojo.dnd.HtmlDropTarget.prototype.accepts.apply(this,arguments);
if(!_15){
return false;
}
var _16=_14[0].treeNode;
if(dojo.lang.isUndefined(_16)||!_16||!_16.isTreeNode){
dojo.raise("Source is not TreeNode or not found");
}
if(_16===this.treeNode){
return false;
}
return true;
},setAutoExpandTimer:function(){
var _17=this;
var _18=function(){
if(dojo.dnd.dragManager.currentDropTarget===_17){
_17.controller.expand(_17.treeNode);
}
};
this.autoExpandTimer=dojo.lang.setTimeout(_18,_17.autoExpandDelay);
},getDNDMode:function(){
return this.treeNode.tree.DNDMode;
},getAcceptPosition:function(e,_1a){
var _1b=this.getDNDMode();
if(_1b&dojo.widget.Tree.prototype.DNDModes.ONTO&&!(!this.treeNode.actionIsDisabled(dojo.widget.TreeNode.prototype.actions.ADDCHILD)&&_1a.parent!==this.treeNode&&this.controller.canMove(_1a,this.treeNode))){
_1b&=~dojo.widget.Tree.prototype.DNDModes.ONTO;
}
var _1c=this.getPosition(e,_1b);
if(_1c=="onto"||(!this.isAdjacentNode(_1a,_1c)&&this.controller.canMove(_1a,this.treeNode.parent))){
return _1c;
}else{
return false;
}
},onDragOut:function(e){
this.clearAutoExpandTimer();
this.hideIndicator();
},clearAutoExpandTimer:function(){
if(this.autoExpandTimer){
clearTimeout(this.autoExpandTimer);
this.autoExpandTimer=null;
}
},onDragMove:function(e,_1f){
var _20=_1f[0].treeNode;
var _21=this.getAcceptPosition(e,_20);
if(_21){
this.showIndicator(_21);
}
},isAdjacentNode:function(_22,_23){
if(_22===this.treeNode){
return true;
}
if(_22.getNextSibling()===this.treeNode&&_23=="before"){
return true;
}
if(_22.getPreviousSibling()===this.treeNode&&_23=="after"){
return true;
}
return false;
},getPosition:function(e,_25){
var _26=dojo.byId(this.treeNode.labelNode);
var _27=e.pageY||e.clientY+dojo.body().scrollTop;
var _28=dojo.html.getAbsolutePosition(_26).y;
var _29=dojo.html.getBorderBox(_26).height;
var _2a=_27-_28;
var p=_2a/_29;
var _2c="";
if(_25&dojo.widget.Tree.prototype.DNDModes.ONTO&&_25&dojo.widget.Tree.prototype.DNDModes.BETWEEN){
if(p<=0.3){
_2c="before";
}else{
if(p<=0.7){
_2c="onto";
}else{
_2c="after";
}
}
}else{
if(_25&dojo.widget.Tree.prototype.DNDModes.BETWEEN){
if(p<=0.5){
_2c="before";
}else{
_2c="after";
}
}else{
if(_25&dojo.widget.Tree.prototype.DNDModes.ONTO){
_2c="onto";
}
}
}
return _2c;
},getTargetParentIndex:function(_2d,_2e){
var _2f=_2e=="before"?this.treeNode.getParentIndex():this.treeNode.getParentIndex()+1;
if(this.treeNode.parent===_2d.parent&&this.treeNode.getParentIndex()>_2d.getParentIndex()){
_2f--;
}
return _2f;
},onDrop:function(e){
var _31=this.position;
this.onDragOut(e);
var _32=e.dragObject.treeNode;
if(!dojo.lang.isObject(_32)){
dojo.raise("TreeNode not found in dragObject");
}
if(_31=="onto"){
return this.controller.move(_32,this.treeNode,0);
}else{
var _33=this.getTargetParentIndex(_32,_31);
return this.controller.move(_32,this.treeNode.parent,_33);
}
}});
dojo.dnd.TreeDNDController=function(_34){
this.treeController=_34;
this.dragSources={};
this.dropTargets={};
};
dojo.lang.extend(dojo.dnd.TreeDNDController,{listenTree:function(_35){
dojo.event.topic.subscribe(_35.eventNames.createDOMNode,this,"onCreateDOMNode");
dojo.event.topic.subscribe(_35.eventNames.moveFrom,this,"onMoveFrom");
dojo.event.topic.subscribe(_35.eventNames.moveTo,this,"onMoveTo");
dojo.event.topic.subscribe(_35.eventNames.addChild,this,"onAddChild");
dojo.event.topic.subscribe(_35.eventNames.removeNode,this,"onRemoveNode");
dojo.event.topic.subscribe(_35.eventNames.treeDestroy,this,"onTreeDestroy");
},unlistenTree:function(_36){
dojo.event.topic.unsubscribe(_36.eventNames.createDOMNode,this,"onCreateDOMNode");
dojo.event.topic.unsubscribe(_36.eventNames.moveFrom,this,"onMoveFrom");
dojo.event.topic.unsubscribe(_36.eventNames.moveTo,this,"onMoveTo");
dojo.event.topic.unsubscribe(_36.eventNames.addChild,this,"onAddChild");
dojo.event.topic.unsubscribe(_36.eventNames.removeNode,this,"onRemoveNode");
dojo.event.topic.unsubscribe(_36.eventNames.treeDestroy,this,"onTreeDestroy");
},onTreeDestroy:function(_37){
this.unlistenTree(_37.source);
},onCreateDOMNode:function(_38){
this.registerDNDNode(_38.source);
},onAddChild:function(_39){
this.registerDNDNode(_39.child);
},onMoveFrom:function(_3a){
var _3b=this;
dojo.lang.forEach(_3a.child.getDescendants(),function(_3c){
_3b.unregisterDNDNode(_3c);
});
},onMoveTo:function(_3d){
var _3e=this;
dojo.lang.forEach(_3d.child.getDescendants(),function(_3f){
_3e.registerDNDNode(_3f);
});
},registerDNDNode:function(_40){
if(!_40.tree.DNDMode){
return;
}
var _41=null;
var _42=null;
if(!_40.actionIsDisabled(_40.actions.MOVE)){
var _41=new dojo.dnd.TreeDragSource(_40.labelNode,this,_40.tree.widgetId,_40);
this.dragSources[_40.widgetId]=_41;
}
var _42=new dojo.dnd.TreeDropTarget(_40.labelNode,this.treeController,_40.tree.DNDAcceptTypes,_40);
this.dropTargets[_40.widgetId]=_42;
},unregisterDNDNode:function(_43){
if(this.dragSources[_43.widgetId]){
dojo.dnd.dragManager.unregisterDragSource(this.dragSources[_43.widgetId]);
delete this.dragSources[_43.widgetId];
}
if(this.dropTargets[_43.widgetId]){
dojo.dnd.dragManager.unregisterDropTarget(this.dropTargets[_43.widgetId]);
delete this.dropTargets[_43.widgetId];
}
}});
