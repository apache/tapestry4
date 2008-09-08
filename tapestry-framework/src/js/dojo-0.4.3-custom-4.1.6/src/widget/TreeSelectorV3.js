dojo.provide("dojo.widget.TreeSelectorV3");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.widget.TreeCommon");
dojo.widget.defineWidget("dojo.widget.TreeSelectorV3",[dojo.widget.HtmlWidget,dojo.widget.TreeCommon],function(){
this.eventNames={};
this.listenedTrees={};
this.selectedNodes=[];
this.lastClicked={};
},{listenTreeEvents:["afterTreeCreate","afterCollapse","afterChangeTree","afterDetach","beforeTreeDestroy"],listenNodeFilter:function(_1){
return _1 instanceof dojo.widget.Widget;
},allowedMulti:true,dblselectTimeout:300,eventNamesDefault:{select:"select",deselect:"deselect",dblselect:"dblselect"},onAfterTreeCreate:function(_2){
var _3=_2.source;
dojo.event.browser.addListener(_3.domNode,"onclick",dojo.lang.hitch(this,this.onTreeClick));
if(dojo.render.html.ie){
dojo.event.browser.addListener(_3.domNode,"ondblclick",dojo.lang.hitch(this,this.onTreeDblClick));
}
dojo.event.browser.addListener(_3.domNode,"onKey",dojo.lang.hitch(this,this.onKey));
},onKey:function(e){
if(!e.key||e.ctrkKey||e.altKey){
return;
}
switch(e.key){
case e.KEY_ENTER:
var _5=this.domElement2TreeNode(e.target);
if(_5){
this.processNode(_5,e);
}
}
},onAfterChangeTree:function(_6){
if(!_6.oldTree&&_6.node.selected){
this.select(_6.node);
}
if(!_6.newTree||!this.listenedTrees[_6.newTree.widgetId]){
if(this.selectedNode&&_6.node.children){
this.deselectIfAncestorMatch(_6.node);
}
}
},initialize:function(_7){
for(var _8 in this.eventNamesDefault){
if(dojo.lang.isUndefined(this.eventNames[_8])){
this.eventNames[_8]=this.widgetId+"/"+this.eventNamesDefault[_8];
}
}
},onBeforeTreeDestroy:function(_9){
this.unlistenTree(_9.source);
},onAfterCollapse:function(_a){
this.deselectIfAncestorMatch(_a.source);
},onTreeDblClick:function(_b){
this.onTreeClick(_b);
},checkSpecialEvent:function(_c){
return _c.shiftKey||_c.ctrlKey;
},onTreeClick:function(_d){
var _e=this.domElement2TreeNode(_d.target);
if(!_e){
return;
}
var _f=function(_10){
return _10===_e.labelNode;
};
if(this.checkPathCondition(_d.target,_f)){
this.processNode(_e,_d);
}
},processNode:function(_11,_12){
if(_11.actionIsDisabled(_11.actions.SELECT)){
return;
}
if(dojo.lang.inArray(this.selectedNodes,_11)){
if(this.checkSpecialEvent(_12)){
this.deselect(_11);
return;
}
var _13=this;
var i=0;
var _15;
while(this.selectedNodes.length>i){
_15=this.selectedNodes[i];
if(_15!==_11){
this.deselect(_15);
continue;
}
i++;
}
var _16=this.checkRecentClick(_11);
eventName=_16?this.eventNames.dblselect:this.eventNames.select;
if(_16){
eventName=this.eventNames.dblselect;
this.forgetLastClicked();
}else{
eventName=this.eventNames.select;
this.setLastClicked(_11);
}
dojo.event.topic.publish(eventName,{node:_11});
return;
}
this.deselectIfNoMulti(_12);
this.setLastClicked(_11);
this.select(_11);
},forgetLastClicked:function(){
this.lastClicked={};
},setLastClicked:function(_17){
this.lastClicked.date=new Date();
this.lastClicked.node=_17;
},checkRecentClick:function(_18){
var _19=new Date()-this.lastClicked.date;
if(this.lastClicked.node&&_19<this.dblselectTimeout){
return true;
}else{
return false;
}
},deselectIfNoMulti:function(_1a){
if(!this.checkSpecialEvent(_1a)||!this.allowedMulti){
this.deselectAll();
}
},deselectIfAncestorMatch:function(_1b){
var _1c=this;
dojo.lang.forEach(this.selectedNodes,function(_1d){
var _1e=_1d;
_1d=_1d.parent;
while(_1d&&_1d.isTreeNode){
if(_1d===_1b){
_1c.deselect(_1e);
return;
}
_1d=_1d.parent;
}
});
},onAfterDetach:function(_1f){
this.deselectIfAncestorMatch(_1f.child);
},select:function(_20){
var _21=dojo.lang.find(this.selectedNodes,_20,true);
if(_21>=0){
return;
}
this.selectedNodes.push(_20);
dojo.event.topic.publish(this.eventNames.select,{node:_20});
},deselect:function(_22){
var _23=dojo.lang.find(this.selectedNodes,_22,true);
if(_23<0){
return;
}
this.selectedNodes.splice(_23,1);
dojo.event.topic.publish(this.eventNames.deselect,{node:_22});
},deselectAll:function(){
while(this.selectedNodes.length){
this.deselect(this.selectedNodes[0]);
}
}});
