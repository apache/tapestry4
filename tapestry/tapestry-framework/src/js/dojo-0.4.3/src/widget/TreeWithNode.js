dojo.require("dojo.lang.declare");
dojo.provide("dojo.widget.TreeWithNode");
dojo.declare("dojo.widget.TreeWithNode",null,function(){
},{loadStates:{UNCHECKED:"UNCHECKED",LOADING:"LOADING",LOADED:"LOADED"},state:"UNCHECKED",objectId:"",isContainer:true,lockLevel:0,lock:function(){
this.lockLevel++;
},unlock:function(){
if(!this.lockLevel){
dojo.raise(this.widgetType+" unlock: not locked");
}
this.lockLevel--;
},expandLevel:0,loadLevel:0,hasLock:function(){
return this.lockLevel>0;
},isLocked:function(){
var _1=this;
while(true){
if(_1.lockLevel){
return true;
}
if(!_1.parent||_1.isTree){
break;
}
_1=_1.parent;
}
return false;
},flushLock:function(){
this.lockLevel=0;
},actionIsDisabled:function(_2){
var _3=false;
if(dojo.lang.inArray(this.actionsDisabled,_2)){
_3=true;
}
if(this.isTreeNode){
if(!this.tree.allowAddChildToLeaf&&_2==this.actions.ADDCHILD&&!this.isFolder){
_3=true;
}
}
return _3;
},actionIsDisabledNow:function(_4){
return this.actionIsDisabled(_4)||this.isLocked();
},setChildren:function(_5){
if(this.isTreeNode&&!this.isFolder){
this.setFolder();
}else{
if(this.isTreeNode){
this.state=this.loadStates.LOADED;
}
}
var _6=this.children.length>0;
if(_6&&_5){
this.destroyChildren();
}
if(_5){
this.children=_5;
}
var _7=this.children.length>0;
if(this.isTreeNode&&_7!=_6){
this.viewSetHasChildren();
}
for(var i=0;i<this.children.length;i++){
var _9=this.children[i];
if(!(_9 instanceof dojo.widget.Widget)){
_9=this.children[i]=this.tree.createNode(_9);
var _a=true;
}else{
var _a=false;
}
if(!_9.parent){
_9.parent=this;
if(this.tree!==_9.tree){
_9.updateTree(this.tree);
}
_9.viewAddLayout();
this.containerNode.appendChild(_9.domNode);
var _b={child:_9,index:i,parent:this,childWidgetCreated:_a};
delete dojo.widget.manager.topWidgets[_9.widgetId];
dojo.event.topic.publish(this.tree.eventNames.afterAddChild,_b);
}
if(this.tree.eagerWidgetInstantiation){
dojo.lang.forEach(this.children,function(_c){
_c.setChildren();
});
}
}
},doAddChild:function(_d,_e){
return this.addChild(_d,_e,true);
},addChild:function(_f,_10,_11){
if(dojo.lang.isUndefined(_10)){
_10=this.children.length;
}
if(!_f.isTreeNode){
dojo.raise("You can only add TreeNode widgets to a "+this.widgetType+" widget!");
return;
}
this.children.splice(_10,0,_f);
_f.parent=this;
_f.addedTo(this,_10,_11);
delete dojo.widget.manager.topWidgets[_f.widgetId];
},onShow:function(){
this.animationInProgress=false;
},onHide:function(){
this.animationInProgress=false;
}});
