dojo.provide("dojo.widget.TreeBasicController");
dojo.require("dojo.event.*");
dojo.require("dojo.json");
dojo.require("dojo.io.*");
dojo.widget.defineWidget("dojo.widget.TreeBasicController",dojo.widget.HtmlWidget,{widgetType:"TreeBasicController",DNDController:"",dieWithTree:false,initialize:function(_1,_2){
if(this.DNDController=="create"){
dojo.require("dojo.dnd.TreeDragAndDrop");
this.DNDController=new dojo.dnd.TreeDNDController(this);
}
},listenTree:function(_3){
dojo.event.topic.subscribe(_3.eventNames.createDOMNode,this,"onCreateDOMNode");
dojo.event.topic.subscribe(_3.eventNames.treeClick,this,"onTreeClick");
dojo.event.topic.subscribe(_3.eventNames.treeCreate,this,"onTreeCreate");
dojo.event.topic.subscribe(_3.eventNames.treeDestroy,this,"onTreeDestroy");
if(this.DNDController){
this.DNDController.listenTree(_3);
}
},unlistenTree:function(_4){
dojo.event.topic.unsubscribe(_4.eventNames.createDOMNode,this,"onCreateDOMNode");
dojo.event.topic.unsubscribe(_4.eventNames.treeClick,this,"onTreeClick");
dojo.event.topic.unsubscribe(_4.eventNames.treeCreate,this,"onTreeCreate");
dojo.event.topic.unsubscribe(_4.eventNames.treeDestroy,this,"onTreeDestroy");
},onTreeDestroy:function(_5){
var _6=_5.source;
this.unlistenTree(_6);
if(this.dieWithTree){
this.destroy();
}
},onCreateDOMNode:function(_7){
var _8=_7.source;
if(_8.expandLevel>0){
this.expandToLevel(_8,_8.expandLevel);
}
},onTreeCreate:function(_9){
var _a=_9.source;
var _b=this;
if(_a.expandLevel){
dojo.lang.forEach(_a.children,function(_c){
_b.expandToLevel(_c,_a.expandLevel-1);
});
}
},expandToLevel:function(_d,_e){
if(_e==0){
return;
}
var _f=_d.children;
var _10=this;
var _11=function(_12,_13){
this.node=_12;
this.expandLevel=_13;
this.process=function(){
for(var i=0;i<this.node.children.length;i++){
var _15=_12.children[i];
_10.expandToLevel(_15,this.expandLevel);
}
};
};
var h=new _11(_d,_e-1);
this.expand(_d,false,h,h.process);
},onTreeClick:function(_17){
var _18=_17.source;
if(_18.isLocked()){
return false;
}
if(_18.isExpanded){
this.collapse(_18);
}else{
this.expand(_18);
}
},expand:function(_19,_1a,_1b,_1c){
_19.expand();
if(_1c){
_1c.apply(_1b,[_19]);
}
},collapse:function(_1d){
_1d.collapse();
},canMove:function(_1e,_1f){
if(_1e.actionIsDisabled(_1e.actions.MOVE)){
return false;
}
if(_1e.parent!==_1f&&_1f.actionIsDisabled(_1f.actions.ADDCHILD)){
return false;
}
var _20=_1f;
while(_20.isTreeNode){
if(_20===_1e){
return false;
}
_20=_20.parent;
}
return true;
},move:function(_21,_22,_23){
if(!this.canMove(_21,_22)){
return false;
}
var _24=this.doMove(_21,_22,_23);
if(!_24){
return _24;
}
if(_22.isTreeNode){
this.expand(_22);
}
return _24;
},doMove:function(_25,_26,_27){
_25.tree.move(_25,_26,_27);
return true;
},canRemoveNode:function(_28){
if(_28.actionIsDisabled(_28.actions.REMOVE)){
return false;
}
return true;
},removeNode:function(_29,_2a,_2b){
if(!this.canRemoveNode(_29)){
return false;
}
return this.doRemoveNode(_29,_2a,_2b);
},doRemoveNode:function(_2c,_2d,_2e){
_2c.tree.removeNode(_2c);
if(_2e){
_2e.apply(dojo.lang.isUndefined(_2d)?this:_2d,[_2c]);
}
},canCreateChild:function(_2f,_30,_31){
if(_2f.actionIsDisabled(_2f.actions.ADDCHILD)){
return false;
}
return true;
},createChild:function(_32,_33,_34,_35,_36){
if(!this.canCreateChild(_32,_33,_34)){
return false;
}
return this.doCreateChild.apply(this,arguments);
},doCreateChild:function(_37,_38,_39,_3a,_3b){
var _3c=_39.widgetType?_39.widgetType:"TreeNode";
var _3d=dojo.widget.createWidget(_3c,_39);
_37.addChild(_3d,_38);
this.expand(_37);
if(_3b){
_3b.apply(_3a,[_3d]);
}
return _3d;
}});
