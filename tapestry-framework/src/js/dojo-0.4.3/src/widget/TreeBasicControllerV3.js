dojo.provide("dojo.widget.TreeBasicControllerV3");
dojo.require("dojo.event.*");
dojo.require("dojo.json");
dojo.require("dojo.io.*");
dojo.require("dojo.widget.TreeCommon");
dojo.require("dojo.widget.TreeNodeV3");
dojo.require("dojo.widget.TreeV3");
dojo.widget.defineWidget("dojo.widget.TreeBasicControllerV3",[dojo.widget.HtmlWidget,dojo.widget.TreeCommon],function(){
this.listenedTrees={};
},{listenTreeEvents:["afterSetFolder","afterTreeCreate","beforeTreeDestroy"],listenNodeFilter:function(_1){
return _1 instanceof dojo.widget.Widget;
},editor:null,initialize:function(_2){
if(_2.editor){
this.editor=dojo.widget.byId(_2.editor);
this.editor.controller=this;
}
},getInfo:function(_3){
return _3.getInfo();
},onBeforeTreeDestroy:function(_4){
this.unlistenTree(_4.source);
},onAfterSetFolder:function(_5){
if(_5.source.expandLevel>0){
this.expandToLevel(_5.source,_5.source.expandLevel);
}
if(_5.source.loadLevel>0){
this.loadToLevel(_5.source,_5.source.loadLevel);
}
},_focusNextVisible:function(_6){
if(_6.isFolder&&_6.isExpanded&&_6.children.length>0){
_7=_6.children[0];
}else{
while(_6.isTreeNode&&_6.isLastChild()){
_6=_6.parent;
}
if(_6.isTreeNode){
var _7=_6.parent.children[_6.getParentIndex()+1];
}
}
if(_7&&_7.isTreeNode){
this._focusLabel(_7);
return _7;
}
},_focusPreviousVisible:function(_8){
var _9=_8;
if(!_8.isFirstChild()){
var _a=_8.parent.children[_8.getParentIndex()-1];
_8=_a;
while(_8.isFolder&&_8.isExpanded&&_8.children.length>0){
_9=_8;
_8=_8.children[_8.children.length-1];
}
}else{
_8=_8.parent;
}
if(_8&&_8.isTreeNode){
_9=_8;
}
if(_9&&_9.isTreeNode){
this._focusLabel(_9);
return _9;
}
},_focusZoomIn:function(_b){
var _c=_b;
if(_b.isFolder&&!_b.isExpanded){
this.expand(_b);
}else{
if(_b.children.length>0){
_b=_b.children[0];
}
}
if(_b&&_b.isTreeNode){
_c=_b;
}
if(_c&&_c.isTreeNode){
this._focusLabel(_c);
return _c;
}
},_focusZoomOut:function(_d){
var _e=_d;
if(_d.isFolder&&_d.isExpanded){
this.collapse(_d);
}else{
_d=_d.parent;
}
if(_d&&_d.isTreeNode){
_e=_d;
}
if(_e&&_e.isTreeNode){
this._focusLabel(_e);
return _e;
}
},onFocusNode:function(e){
var _10=this.domElement2TreeNode(e.target);
if(_10){
_10.viewFocus();
dojo.event.browser.stopEvent(e);
}
},onBlurNode:function(e){
var _12=this.domElement2TreeNode(e.target);
if(!_12){
return;
}
var _13=_12.labelNode;
_13.setAttribute("tabIndex","-1");
_12.viewUnfocus();
dojo.event.browser.stopEvent(e);
_12.tree.domNode.setAttribute("tabIndex","0");
},_focusLabel:function(_14){
var _15=_14.tree.lastFocused;
var _16;
if(_15&&_15.labelNode){
_16=_15.labelNode;
dojo.event.disconnect(_16,"onblur",this,"onBlurNode");
_16.setAttribute("tabIndex","-1");
dojo.html.removeClass(_16,"TreeLabelFocused");
}
_16=_14.labelNode;
_16.setAttribute("tabIndex","0");
_14.tree.lastFocused=_14;
dojo.html.addClass(_16,"TreeLabelFocused");
dojo.event.connectOnce(_16,"onblur",this,"onBlurNode");
dojo.event.connectOnce(_16,"onfocus",this,"onFocusNode");
_16.focus();
},onKey:function(e){
if(!e.key||e.ctrkKey||e.altKey){
return;
}
var _18=this.domElement2TreeNode(e.target);
if(!_18){
return;
}
var _19=_18.tree;
if(_19.lastFocused&&_19.lastFocused.labelNode){
_18=_19.lastFocused;
}
switch(e.key){
case e.KEY_TAB:
if(e.shiftKey){
_19.domNode.setAttribute("tabIndex","-1");
}
break;
case e.KEY_RIGHT_ARROW:
this._focusZoomIn(_18);
dojo.event.browser.stopEvent(e);
break;
case e.KEY_LEFT_ARROW:
this._focusZoomOut(_18);
dojo.event.browser.stopEvent(e);
break;
case e.KEY_UP_ARROW:
this._focusPreviousVisible(_18);
dojo.event.browser.stopEvent(e);
break;
case e.KEY_DOWN_ARROW:
this._focusNextVisible(_18);
dojo.event.browser.stopEvent(e);
break;
}
},onFocusTree:function(e){
if(!e.currentTarget){
return;
}
try{
var _1b=this.getWidgetByNode(e.currentTarget);
if(!_1b||!_1b.isTree){
return;
}
var _1c=this.getWidgetByNode(_1b.domNode.firstChild);
if(_1c&&_1c.isTreeNode){
if(_1b.lastFocused&&_1b.lastFocused.isTreeNode){
_1c=_1b.lastFocused;
}
this._focusLabel(_1c);
}
}
catch(e){
}
},onAfterTreeCreate:function(_1d){
var _1e=_1d.source;
dojo.event.browser.addListener(_1e.domNode,"onKey",dojo.lang.hitch(this,this.onKey));
dojo.event.browser.addListener(_1e.domNode,"onmousedown",dojo.lang.hitch(this,this.onTreeMouseDown));
dojo.event.browser.addListener(_1e.domNode,"onclick",dojo.lang.hitch(this,this.onTreeClick));
dojo.event.browser.addListener(_1e.domNode,"onfocus",dojo.lang.hitch(this,this.onFocusTree));
_1e.domNode.setAttribute("tabIndex","0");
if(_1e.expandLevel){
this.expandToLevel(_1e,_1e.expandLevel);
}
if(_1e.loadLevel){
this.loadToLevel(_1e,_1e.loadLevel);
}
},onTreeMouseDown:function(e){
},onTreeClick:function(e){
var _21=e.target;
var _22=this.domElement2TreeNode(_21);
if(!_22||!_22.isTreeNode){
return;
}
var _23=function(el){
return el===_22.expandNode;
};
if(this.checkPathCondition(_21,_23)){
this.processExpandClick(_22);
}
this._focusLabel(_22);
},processExpandClick:function(_25){
if(_25.isExpanded){
this.collapse(_25);
}else{
this.expand(_25);
}
},batchExpandTimeout:20,expandAll:function(_26){
return this.expandToLevel(_26,Number.POSITIVE_INFINITY);
},collapseAll:function(_27){
var _28=this;
var _29=function(_2a){
return (_2a instanceof dojo.widget.Widget)&&_2a.isFolder&&_2a.isExpanded;
};
if(_27.isTreeNode){
this.processDescendants(_27,_29,this.collapse);
}else{
if(_27.isTree){
dojo.lang.forEach(_27.children,function(c){
_28.processDescendants(c,_29,_28.collapse);
});
}
}
},expandToNode:function(_2c,_2d){
n=_2d?_2c:_2c.parent;
s=[];
while(!n.isExpanded){
s.push(n);
n=n.parent;
}
dojo.lang.forEach(s,function(n){
n.expand();
});
},expandToLevel:function(_2f,_30){
dojo.require("dojo.widget.TreeTimeoutIterator");
var _31=this;
var _32=function(_33){
var res=_33.isFolder||_33.children&&_33.children.length;
return res;
};
var _35=function(_36,_37){
_31.expand(_36,true);
_37.forward();
};
var _38=new dojo.widget.TreeTimeoutIterator(_2f,_35,this);
_38.setFilter(_32);
_38.timeout=this.batchExpandTimeout;
_38.setMaxLevel(_2f.isTreeNode?_30-1:_30);
return _38.start(_2f.isTreeNode);
},getWidgetByNode:function(_39){
var _3a;
var _3b=_39;
while(!(_3a=_3b.widgetId)){
_3b=_3b.parentNode;
if(_3b==null){
break;
}
}
if(_3a){
return dojo.widget.byId(_3a);
}else{
if(_39==null){
return null;
}else{
return dojo.widget.manager.byNode(_39);
}
}
},expand:function(_3c){
if(_3c.isFolder){
_3c.expand();
}
},collapse:function(_3d){
if(_3d.isFolder){
_3d.collapse();
}
},canEditLabel:function(_3e){
if(_3e.actionIsDisabledNow(_3e.actions.EDIT)){
return false;
}
return true;
},editLabelStart:function(_3f){
if(!this.canEditLabel(_3f)){
return false;
}
if(!this.editor.isClosed()){
this.editLabelFinish(this.editor.saveOnBlur);
}
this.doEditLabelStart(_3f);
},editLabelFinish:function(_40){
this.doEditLabelFinish(_40);
},doEditLabelStart:function(_41){
if(!this.editor){
dojo.raise(this.widgetType+": no editor specified");
}
this.editor.open(_41);
},doEditLabelFinish:function(_42,_43){
if(!this.editor){
dojo.raise(this.widgetType+": no editor specified");
}
var _44=this.editor.node;
var _45=this.editor.getContents();
this.editor.close(_42);
if(_42){
var _46={title:_45};
if(_43){
dojo.lang.mixin(_46,_43);
}
if(_44.isPhantom){
var _47=_44.parent;
var _48=_44.getParentIndex();
_44.destroy();
dojo.widget.TreeBasicControllerV3.prototype.doCreateChild.call(this,_47,_48,_46);
}else{
var _49=_43&&_43.title?_43.title:_45;
_44.setTitle(_49);
}
}else{
if(_44.isPhantom){
_44.destroy();
}
}
},makeDefaultNode:function(_4a,_4b){
var _4c={title:_4a.tree.defaultChildTitle};
return dojo.widget.TreeBasicControllerV3.prototype.doCreateChild.call(this,_4a,_4b,_4c);
},runStages:function(_4d,_4e,_4f,_50,_51,_52){
if(_4d&&!_4d.apply(this,_52)){
return false;
}
if(_4e&&!_4e.apply(this,_52)){
return false;
}
var _53=_4f.apply(this,_52);
if(_50){
_50.apply(this,_52);
}
if(!_53){
return _53;
}
if(_51){
_51.apply(this,_52);
}
return _53;
}});
dojo.lang.extend(dojo.widget.TreeBasicControllerV3,{createAndEdit:function(_54,_55){
var _56={title:_54.tree.defaultChildTitle};
if(!this.canCreateChild(_54,_55,_56)){
return false;
}
var _57=this.doCreateChild(_54,_55,_56);
if(!_57){
return false;
}
this.exposeCreateChild(_54,_55,_56);
_57.isPhantom=true;
if(!this.editor.isClosed()){
this.editLabelFinish(this.editor.saveOnBlur);
}
this.doEditLabelStart(_57);
}});
dojo.lang.extend(dojo.widget.TreeBasicControllerV3,{canClone:function(_58,_59,_5a,_5b){
return true;
},clone:function(_5c,_5d,_5e,_5f){
return this.runStages(this.canClone,this.prepareClone,this.doClone,this.finalizeClone,this.exposeClone,arguments);
},exposeClone:function(_60,_61){
if(_61.isTreeNode){
this.expand(_61);
}
},doClone:function(_62,_63,_64,_65){
var _66=_62.clone(_65);
_63.addChild(_66,_64);
return _66;
}});
dojo.lang.extend(dojo.widget.TreeBasicControllerV3,{canDetach:function(_67){
if(_67.actionIsDisabledNow(_67.actions.DETACH)){
return false;
}
return true;
},detach:function(_68){
return this.runStages(this.canDetach,this.prepareDetach,this.doDetach,this.finalizeDetach,this.exposeDetach,arguments);
},doDetach:function(_69,_6a,_6b){
_69.detach();
}});
dojo.lang.extend(dojo.widget.TreeBasicControllerV3,{canDestroyChild:function(_6c){
if(_6c.parent&&!this.canDetach(_6c)){
return false;
}
return true;
},destroyChild:function(_6d){
return this.runStages(this.canDestroyChild,this.prepareDestroyChild,this.doDestroyChild,this.finalizeDestroyChild,this.exposeDestroyChild,arguments);
},doDestroyChild:function(_6e){
_6e.destroy();
}});
dojo.lang.extend(dojo.widget.TreeBasicControllerV3,{canMoveNotANode:function(_6f,_70){
if(_6f.treeCanMove){
return _6f.treeCanMove(_70);
}
return true;
},canMove:function(_71,_72){
if(!_71.isTreeNode){
return this.canMoveNotANode(_71,_72);
}
if(_71.actionIsDisabledNow(_71.actions.MOVE)){
return false;
}
if(_71.parent!==_72&&_72.actionIsDisabledNow(_72.actions.ADDCHILD)){
return false;
}
var _73=_72;
while(_73.isTreeNode){
if(_73===_71){
return false;
}
_73=_73.parent;
}
return true;
},move:function(_74,_75,_76){
return this.runStages(this.canMove,this.prepareMove,this.doMove,this.finalizeMove,this.exposeMove,arguments);
},doMove:function(_77,_78,_79){
_77.tree.move(_77,_78,_79);
return true;
},exposeMove:function(_7a,_7b){
if(_7b.isTreeNode){
this.expand(_7b);
}
}});
dojo.lang.extend(dojo.widget.TreeBasicControllerV3,{canCreateChild:function(_7c,_7d,_7e){
if(_7c.actionIsDisabledNow(_7c.actions.ADDCHILD)){
return false;
}
return true;
},createChild:function(_7f,_80,_81){
if(!_81){
_81={title:_7f.tree.defaultChildTitle};
}
return this.runStages(this.canCreateChild,this.prepareCreateChild,this.doCreateChild,this.finalizeCreateChild,this.exposeCreateChild,[_7f,_80,_81]);
},prepareCreateChild:function(){
return true;
},finalizeCreateChild:function(){
},doCreateChild:function(_82,_83,_84){
var _85=_82.tree.createNode(_84);
_82.addChild(_85,_83);
return _85;
},exposeCreateChild:function(_86){
return this.expand(_86);
}});
