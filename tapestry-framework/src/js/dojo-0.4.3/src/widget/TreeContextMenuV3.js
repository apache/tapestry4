dojo.provide("dojo.widget.TreeContextMenuV3");
dojo.require("dojo.event.*");
dojo.require("dojo.io.*");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.Menu2");
dojo.require("dojo.widget.TreeCommon");
dojo.widget.defineWidget("dojo.widget.TreeContextMenuV3",[dojo.widget.PopupMenu2,dojo.widget.TreeCommon],function(){
this.listenedTrees={};
},{listenTreeEvents:["afterTreeCreate","beforeTreeDestroy"],listenNodeFilter:function(_1){
return _1 instanceof dojo.widget.Widget;
},onAfterTreeCreate:function(_2){
var _3=_2.source;
this.bindDomNode(_3.domNode);
},onBeforeTreeDestroy:function(_4){
this.unBindDomNode(_4.source.domNode);
},getTreeNode:function(){
var _5=this.getTopOpenEvent().target;
var _6=this.domElement2TreeNode(_5);
return _6;
},open:function(){
var _7=dojo.widget.PopupMenu2.prototype.open.apply(this,arguments);
for(var i=0;i<this.children.length;i++){
if(this.children[i].menuOpen){
this.children[i].menuOpen(this.getTreeNode());
}
}
return _7;
},close:function(){
for(var i=0;i<this.children.length;i++){
if(this.children[i].menuClose){
this.children[i].menuClose(this.getTreeNode());
}
}
var _a=dojo.widget.PopupMenu2.prototype.close.apply(this,arguments);
return _a;
}});
dojo.widget.defineWidget("dojo.widget.TreeMenuItemV3",[dojo.widget.MenuItem2,dojo.widget.TreeCommon],function(){
this.treeActions=[];
},{treeActions:"",initialize:function(_b,_c){
for(var i=0;i<this.treeActions.length;i++){
this.treeActions[i]=this.treeActions[i].toUpperCase();
}
},getTreeNode:function(){
var _e=this;
while(!(_e instanceof dojo.widget.TreeContextMenuV3)){
_e=_e.parent;
}
var _f=_e.getTreeNode();
return _f;
},menuOpen:function(_10){
_10.viewEmphasize();
this.setDisabled(false);
var _11=this;
dojo.lang.forEach(_11.treeActions,function(_12){
_11.setDisabled(_10.actionIsDisabledNow(_12));
});
},menuClose:function(_13){
_13.viewUnemphasize();
},toString:function(){
return "["+this.widgetType+" node "+this.getTreeNode()+"]";
}});
