dojo.provide("dojo.widget.Tree");
dojo.require("dojo.widget.*");
dojo.require("dojo.event.*");
dojo.require("dojo.io.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.widget.TreeNode");
dojo.require("dojo.html.common");
dojo.require("dojo.html.selection");
dojo.widget.defineWidget("dojo.widget.Tree",dojo.widget.HtmlWidget,function(){
this.eventNames={};
this.tree=this;
this.DNDAcceptTypes=[];
this.actionsDisabled=[];
},{widgetType:"Tree",eventNamesDefault:{createDOMNode:"createDOMNode",treeCreate:"treeCreate",treeDestroy:"treeDestroy",treeClick:"treeClick",iconClick:"iconClick",titleClick:"titleClick",moveFrom:"moveFrom",moveTo:"moveTo",addChild:"addChild",removeNode:"removeNode",expand:"expand",collapse:"collapse"},isContainer:true,DNDMode:"off",lockLevel:0,strictFolders:true,DNDModes:{BETWEEN:1,ONTO:2},DNDAcceptTypes:"",templateCssString:"\n.dojoTree {\n\tfont: caption;\n\tfont-size: 11px;\n\tfont-weight: normal;\n\toverflow: auto;\n}\n\n\n.dojoTreeNodeLabelTitle {\n\tpadding-left: 2px;\n\tcolor: WindowText;\n}\n\n.dojoTreeNodeLabel {\n\tcursor:hand;\n\tcursor:pointer;\n}\n\n.dojoTreeNodeLabelTitle:hover {\n\ttext-decoration: underline;\n}\n\n.dojoTreeNodeLabelSelected {\n\tbackground-color: Highlight;\n\tcolor: HighlightText;\n}\n\n.dojoTree div {\n\twhite-space: nowrap;\n}\n\n.dojoTree img, .dojoTreeNodeLabel img {\n\tvertical-align: middle;\n}\n\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/images/Tree/Tree.css"),templateString:"<div class=\"dojoTree\"></div>",isExpanded:true,isTree:true,objectId:"",controller:"",selector:"",menu:"",expandLevel:"",blankIconSrc:dojo.uri.moduleUri("dojo.widget","templates/images/Tree/treenode_blank.gif"),gridIconSrcT:dojo.uri.moduleUri("dojo.widget","templates/images/Tree/treenode_grid_t.gif"),gridIconSrcL:dojo.uri.moduleUri("dojo.widget","templates/images/Tree/treenode_grid_l.gif"),gridIconSrcV:dojo.uri.moduleUri("dojo.widget","templates/images/Tree/treenode_grid_v.gif"),gridIconSrcP:dojo.uri.moduleUri("dojo.widget","templates/images/Tree/treenode_grid_p.gif"),gridIconSrcC:dojo.uri.moduleUri("dojo.widget","templates/images/Tree/treenode_grid_c.gif"),gridIconSrcX:dojo.uri.moduleUri("dojo.widget","templates/images/Tree/treenode_grid_x.gif"),gridIconSrcY:dojo.uri.moduleUri("dojo.widget","templates/images/Tree/treenode_grid_y.gif"),gridIconSrcZ:dojo.uri.moduleUri("dojo.widget","templates/images/Tree/treenode_grid_z.gif"),expandIconSrcPlus:dojo.uri.moduleUri("dojo.widget","templates/images/Tree/treenode_expand_plus.gif"),expandIconSrcMinus:dojo.uri.moduleUri("dojo.widget","templates/images/Tree/treenode_expand_minus.gif"),expandIconSrcLoading:dojo.uri.moduleUri("dojo.widget","templates/images/Tree/treenode_loading.gif"),iconWidth:18,iconHeight:18,showGrid:true,showRootGrid:true,actionIsDisabled:function(_1){
var _2=this;
return dojo.lang.inArray(_2.actionsDisabled,_1);
},actions:{ADDCHILD:"ADDCHILD"},getInfo:function(){
var _3={widgetId:this.widgetId,objectId:this.objectId};
return _3;
},initializeController:function(){
if(this.controller!="off"){
if(this.controller){
this.controller=dojo.widget.byId(this.controller);
}else{
dojo.require("dojo.widget.TreeBasicController");
this.controller=dojo.widget.createWidget("TreeBasicController",{DNDController:(this.DNDMode?"create":""),dieWithTree:true});
}
this.controller.listenTree(this);
}else{
this.controller=null;
}
},initializeSelector:function(){
if(this.selector!="off"){
if(this.selector){
this.selector=dojo.widget.byId(this.selector);
}else{
dojo.require("dojo.widget.TreeSelector");
this.selector=dojo.widget.createWidget("TreeSelector",{dieWithTree:true});
}
this.selector.listenTree(this);
}else{
this.selector=null;
}
},initialize:function(_4,_5){
var _6=this;
for(name in this.eventNamesDefault){
if(dojo.lang.isUndefined(this.eventNames[name])){
this.eventNames[name]=this.widgetId+"/"+this.eventNamesDefault[name];
}
}
for(var i=0;i<this.actionsDisabled.length;i++){
this.actionsDisabled[i]=this.actionsDisabled[i].toUpperCase();
}
if(this.DNDMode=="off"){
this.DNDMode=0;
}else{
if(this.DNDMode=="between"){
this.DNDMode=this.DNDModes.ONTO|this.DNDModes.BETWEEN;
}else{
if(this.DNDMode=="onto"){
this.DNDMode=this.DNDModes.ONTO;
}
}
}
this.expandLevel=parseInt(this.expandLevel);
this.initializeSelector();
this.initializeController();
if(this.menu){
this.menu=dojo.widget.byId(this.menu);
this.menu.listenTree(this);
}
this.containerNode=this.domNode;
},postCreate:function(){
this.createDOMNode();
},createDOMNode:function(){
dojo.html.disableSelection(this.domNode);
for(var i=0;i<this.children.length;i++){
this.children[i].parent=this;
var _9=this.children[i].createDOMNode(this,0);
this.domNode.appendChild(_9);
}
if(!this.showRootGrid){
for(var i=0;i<this.children.length;i++){
this.children[i].expand();
}
}
dojo.event.topic.publish(this.eventNames.treeCreate,{source:this});
},destroy:function(){
dojo.event.topic.publish(this.tree.eventNames.treeDestroy,{source:this});
return dojo.widget.HtmlWidget.prototype.destroy.apply(this,arguments);
},addChild:function(_a,_b){
var _c={child:_a,index:_b,parent:this,domNodeInitialized:_a.domNodeInitialized};
this.doAddChild.apply(this,arguments);
dojo.event.topic.publish(this.tree.eventNames.addChild,_c);
},doAddChild:function(_d,_e){
if(dojo.lang.isUndefined(_e)){
_e=this.children.length;
}
if(!_d.isTreeNode){
dojo.raise("You can only add TreeNode widgets to a "+this.widgetType+" widget!");
return;
}
if(this.isTreeNode){
if(!this.isFolder){
this.setFolder();
}
}
var _f=this;
dojo.lang.forEach(_d.getDescendants(),function(_10){
_10.tree=_f.tree;
});
_d.parent=this;
if(this.isTreeNode){
this.state=this.loadStates.LOADED;
}
if(_e<this.children.length){
dojo.html.insertBefore(_d.domNode,this.children[_e].domNode);
}else{
this.containerNode.appendChild(_d.domNode);
if(this.isExpanded&&this.isTreeNode){
this.showChildren();
}
}
this.children.splice(_e,0,_d);
if(_d.domNodeInitialized){
var d=this.isTreeNode?this.depth:-1;
_d.adjustDepth(d-_d.depth+1);
_d.updateIconTree();
}else{
_d.depth=this.isTreeNode?this.depth+1:0;
_d.createDOMNode(_d.tree,_d.depth);
}
var _12=_d.getPreviousSibling();
if(_d.isLastChild()&&_12){
_12.updateExpandGridColumn();
}
},makeBlankImg:function(){
var img=document.createElement("img");
img.style.width=this.iconWidth+"px";
img.style.height=this.iconHeight+"px";
img.src=this.blankIconSrc;
img.style.verticalAlign="middle";
return img;
},updateIconTree:function(){
if(!this.isTree){
this.updateIcons();
}
for(var i=0;i<this.children.length;i++){
this.children[i].updateIconTree();
}
},toString:function(){
return "["+this.widgetType+" ID:"+this.widgetId+"]";
},move:function(_15,_16,_17){
var _18=_15.parent;
var _19=_15.tree;
this.doMove.apply(this,arguments);
var _16=_15.parent;
var _1a=_15.tree;
var _1b={oldParent:_18,oldTree:_19,newParent:_16,newTree:_1a,child:_15};
dojo.event.topic.publish(_19.eventNames.moveFrom,_1b);
dojo.event.topic.publish(_1a.eventNames.moveTo,_1b);
},doMove:function(_1c,_1d,_1e){
_1c.parent.doRemoveNode(_1c);
_1d.doAddChild(_1c,_1e);
},removeNode:function(_1f){
if(!_1f.parent){
return;
}
var _20=_1f.tree;
var _21=_1f.parent;
var _22=this.doRemoveNode.apply(this,arguments);
dojo.event.topic.publish(this.tree.eventNames.removeNode,{child:_22,tree:_20,parent:_21});
return _22;
},doRemoveNode:function(_23){
if(!_23.parent){
return;
}
var _24=_23.parent;
var _25=_24.children;
var _26=_23.getParentIndex();
if(_26<0){
dojo.raise("Couldn't find node "+_23+" for removal");
}
_25.splice(_26,1);
dojo.html.removeNode(_23.domNode);
if(_24.children.length==0&&!_24.isTree){
_24.containerNode.style.display="none";
}
if(_26==_25.length&&_26>0){
_25[_26-1].updateExpandGridColumn();
}
if(_24 instanceof dojo.widget.Tree&&_26==0&&_25.length>0){
_25[0].updateExpandGrid();
}
_23.parent=_23.tree=null;
return _23;
},markLoading:function(){
},unMarkLoading:function(){
},lock:function(){
!this.lockLevel&&this.markLoading();
this.lockLevel++;
},unlock:function(){
if(!this.lockLevel){
dojo.raise("unlock: not locked");
}
this.lockLevel--;
!this.lockLevel&&this.unMarkLoading();
},isLocked:function(){
var _27=this;
while(true){
if(_27.lockLevel){
return true;
}
if(_27 instanceof dojo.widget.Tree){
break;
}
_27=_27.parent;
}
return false;
},flushLock:function(){
this.lockLevel=0;
this.unMarkLoading();
}});
