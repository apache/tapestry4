dojo.provide("dojo.dnd.TreeDragAndDropV3");
dojo.require("dojo.dnd.HtmlDragAndDrop");
dojo.require("dojo.lang.func");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.extras");
dojo.require("dojo.Deferred");
dojo.require("dojo.html.layout");
dojo.dnd.TreeDragSourceV3=function(_1,_2,_3,_4){
this.controller=_2;
this.treeNode=_4;
dojo.dnd.HtmlDragSource.call(this,_1,_3);
};
dojo.inherits(dojo.dnd.TreeDragSourceV3,dojo.dnd.HtmlDragSource);
dojo.dnd.TreeDropTargetV3=function(_5,_6,_7,_8){
this.treeNode=_8;
this.controller=_6;
dojo.dnd.HtmlDropTarget.call(this,_5,_7);
};
dojo.inherits(dojo.dnd.TreeDropTargetV3,dojo.dnd.HtmlDropTarget);
dojo.lang.extend(dojo.dnd.TreeDropTargetV3,{autoExpandDelay:1500,autoExpandTimer:null,position:null,indicatorStyle:"2px black groove",showIndicator:function(_9){
if(this.position==_9){
return;
}
this.hideIndicator();
this.position=_9;
var _a=this.treeNode;
_a.contentNode.style.width=dojo.html.getBorderBox(_a.labelNode).width+"px";
if(_9=="onto"){
_a.contentNode.style.border=this.indicatorStyle;
}else{
if(_9=="before"){
_a.contentNode.style.borderTop=this.indicatorStyle;
}else{
if(_9=="after"){
_a.contentNode.style.borderBottom=this.indicatorStyle;
}
}
}
},hideIndicator:function(){
this.treeNode.contentNode.style.borderBottom="";
this.treeNode.contentNode.style.borderTop="";
this.treeNode.contentNode.style.border="";
this.treeNode.contentNode.style.width="";
this.position=null;
},onDragOver:function(e){
var _c=dojo.dnd.HtmlDropTarget.prototype.onDragOver.apply(this,arguments);
if(_c&&this.treeNode.isFolder&&!this.treeNode.isExpanded){
this.setAutoExpandTimer();
}
if(_c){
this.cacheNodeCoords();
}
return _c;
},accepts:function(_d){
var _e=dojo.dnd.HtmlDropTarget.prototype.accepts.apply(this,arguments);
if(!_e){
return false;
}
for(var i=0;i<_d.length;i++){
var _10=_d[i].treeNode;
if(_10===this.treeNode){
return false;
}
}
return true;
},setAutoExpandTimer:function(){
var _11=this;
var _12=function(){
if(dojo.dnd.dragManager.currentDropTarget===_11){
_11.controller.expand(_11.treeNode);
dojo.dnd.dragManager.cacheTargetLocations();
}
};
this.autoExpandTimer=dojo.lang.setTimeout(_12,_11.autoExpandDelay);
},getAcceptPosition:function(e,_14){
var _15=this.treeNode.tree.DndMode;
if(_15&dojo.widget.TreeV3.prototype.DndModes.ONTO&&this.treeNode.actionIsDisabledNow(this.treeNode.actions.ADDCHILD)){
_15&=~dojo.widget.TreeV3.prototype.DndModes.ONTO;
}
var _16=this.getPosition(e,_15);
if(_16=="onto"){
return _16;
}
for(var i=0;i<_14.length;i++){
var _18=_14[i].dragSource;
if(_18.treeNode&&this.isAdjacentNode(_18.treeNode,_16)){
continue;
}
if(!this.controller.canMove(_18.treeNode?_18.treeNode:_18,this.treeNode.parent)){
return false;
}
}
return _16;
},onDropEnd:function(e){
this.clearAutoExpandTimer();
this.hideIndicator();
},onDragOut:function(e){
this.clearAutoExpandTimer();
this.hideIndicator();
},clearAutoExpandTimer:function(){
if(this.autoExpandTimer){
clearTimeout(this.autoExpandTimer);
this.autoExpandTimer=null;
}
},onDragMove:function(e,_1c){
var _1d=this.getAcceptPosition(e,_1c);
if(_1d){
this.showIndicator(_1d);
}
},isAdjacentNode:function(_1e,_1f){
if(_1e===this.treeNode){
return true;
}
if(_1e.getNextSibling()===this.treeNode&&_1f=="before"){
return true;
}
if(_1e.getPreviousSibling()===this.treeNode&&_1f=="after"){
return true;
}
return false;
},cacheNodeCoords:function(){
var _20=this.treeNode.contentNode;
this.cachedNodeY=dojo.html.getAbsolutePosition(_20).y;
this.cachedNodeHeight=dojo.html.getBorderBox(_20).height;
},getPosition:function(e,_22){
var _23=e.pageY||e.clientY+dojo.body().scrollTop;
var _24=_23-this.cachedNodeY;
var p=_24/this.cachedNodeHeight;
var _26="";
if(_22&dojo.widget.TreeV3.prototype.DndModes.ONTO&&_22&dojo.widget.TreeV3.prototype.DndModes.BETWEEN){
if(p<=0.33){
_26="before";
}else{
if(p<=0.66||this.treeNode.isExpanded&&this.treeNode.children.length&&!this.treeNode.isLastChild()){
_26="onto";
}else{
_26="after";
}
}
}else{
if(_22&dojo.widget.TreeV3.prototype.DndModes.BETWEEN){
if(p<=0.5||this.treeNode.isExpanded&&this.treeNode.children.length&&!this.treeNode.isLastChild()){
_26="before";
}else{
_26="after";
}
}else{
if(_22&dojo.widget.TreeV3.prototype.DndModes.ONTO){
_26="onto";
}
}
}
return _26;
},getTargetParentIndex:function(_27,_28){
var _29=_28=="before"?this.treeNode.getParentIndex():this.treeNode.getParentIndex()+1;
if(_27.treeNode&&this.treeNode.parent===_27.treeNode.parent&&this.treeNode.getParentIndex()>_27.treeNode.getParentIndex()){
_29--;
}
return _29;
},onDrop:function(e){
var _2b=this.position;
var _2c=e.dragObject.dragSource;
var _2d,_2e;
if(_2b=="onto"){
_2d=this.treeNode;
_2e=0;
}else{
_2e=this.getTargetParentIndex(_2c,_2b);
_2d=this.treeNode.parent;
}
var r=this.getDropHandler(e,_2c,_2d,_2e)();
return r;
},getDropHandler:function(e,_31,_32,_33){
var _34;
var _35=this;
_34=function(){
var _36;
if(_31.treeNode){
_36=_35.controller.move(_31.treeNode,_32,_33,true);
}else{
if(dojo.lang.isFunction(_31.onDrop)){
_31.onDrop(_32,_33);
}
var _37=_31.getTreeNode();
if(_37){
_36=_35.controller.createChild(_32,_33,_37,true);
}else{
_36=true;
}
}
if(_36 instanceof dojo.Deferred){
var _38=_36.fired==0;
if(!_38){
_35.handleDropError(_31,_32,_33,_36);
}
return _38;
}else{
return _36;
}
};
return _34;
},handleDropError:function(_39,_3a,_3b,_3c){
dojo.debug("TreeDropTargetV3.handleDropError: DND error occured");
dojo.debugShallow(_3c);
}});
