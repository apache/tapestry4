dojo.provide("dojo.widget.TreeNodeV3");
dojo.require("dojo.html.*");
dojo.require("dojo.event.*");
dojo.require("dojo.io.*");
dojo.require("dojo.widget.TreeWithNode");
dojo.widget.defineWidget("dojo.widget.TreeNodeV3",[dojo.widget.HtmlWidget,dojo.widget.TreeWithNode],function(){
this.actionsDisabled=[];
this.object={};
},{tryLazyInit:true,actions:{MOVE:"MOVE",DETACH:"DETACH",EDIT:"EDIT",ADDCHILD:"ADDCHILD",SELECT:"SELECT"},labelClass:"",contentClass:"",expandNode:null,labelNode:null,nodeDocType:"",selected:false,getnodeDocType:function(){
return this.nodeDocType;
},cloneProperties:["actionsDisabled","tryLazyInit","nodeDocType","objectId","object","title","isFolder","isExpanded","state"],clone:function(_1){
var _2=new this.constructor();
for(var i=0;i<this.cloneProperties.length;i++){
var _4=this.cloneProperties[i];
_2[_4]=dojo.lang.shallowCopy(this[_4],true);
}
if(this.tree.unsetFolderOnEmpty&&!_1&&this.isFolder){
_2.isFolder=false;
}
_2.toggleObj=this.toggleObj;
dojo.widget.manager.add(_2);
_2.tree=this.tree;
_2.buildRendering({},{});
_2.initialize({},{});
if(_1&&this.children.length){
for(var i=0;i<this.children.length;i++){
var _5=this.children[i];
if(_5.clone){
_2.children.push(_5.clone(_1));
}else{
_2.children.push(dojo.lang.shallowCopy(_5,_1));
}
}
_2.setChildren();
}
return _2;
},markProcessing:function(){
this.markProcessingSavedClass=dojo.html.getClass(this.expandNode);
dojo.html.setClass(this.expandNode,this.tree.classPrefix+"ExpandLoading");
},unmarkProcessing:function(){
dojo.html.setClass(this.expandNode,this.markProcessingSavedClass);
},buildRendering:function(_6,_7,_8){
if(_6.tree){
this.tree=dojo.lang.isString(_6.tree)?dojo.widget.manager.getWidgetById(_6.tree):_6.tree;
}else{
if(_8&&_8.tree){
this.tree=_8.tree;
}
}
if(!this.tree){
dojo.raise("Can't evaluate tree from arguments or parent");
}
this.domNode=this.tree.nodeTemplate.cloneNode(true);
this.expandNode=this.domNode.firstChild;
this.contentNode=this.domNode.childNodes[1];
this.labelNode=this.contentNode.firstChild;
if(this.labelClass){
dojo.html.addClass(this.labelNode,this.labelClass);
}
if(this.contentClass){
dojo.html.addClass(this.contentNode,this.contentClass);
}
this.domNode.widgetId=this.widgetId;
this.labelNode.innerHTML=this.title;
},isTreeNode:true,object:{},title:"",isFolder:null,contentNode:null,expandClass:"",isExpanded:false,containerNode:null,getInfo:function(){
var _9={widgetId:this.widgetId,objectId:this.objectId,index:this.getParentIndex()};
return _9;
},setFolder:function(){
this.isFolder=true;
this.viewSetExpand();
if(!this.containerNode){
this.viewAddContainer();
}
dojo.event.topic.publish(this.tree.eventNames.afterSetFolder,{source:this});
},initialize:function(_a,_b,_c){
if(_a.isFolder){
this.isFolder=true;
}
if(this.children.length||this.isFolder){
this.setFolder();
}else{
this.viewSetExpand();
}
for(var i=0;i<this.actionsDisabled.length;i++){
this.actionsDisabled[i]=this.actionsDisabled[i].toUpperCase();
}
dojo.event.topic.publish(this.tree.eventNames.afterChangeTree,{oldTree:null,newTree:this.tree,node:this});
},unsetFolder:function(){
this.isFolder=false;
this.viewSetExpand();
dojo.event.topic.publish(this.tree.eventNames.afterUnsetFolder,{source:this});
},insertNode:function(_e,_f){
if(!_f){
_f=0;
}
if(_f==0){
dojo.html.prependChild(this.domNode,_e.containerNode);
}else{
dojo.html.insertAfter(this.domNode,_e.children[_f-1].domNode);
}
},updateTree:function(_10){
if(this.tree===_10){
return;
}
var _11=this.tree;
dojo.lang.forEach(this.getDescendants(),function(_12){
_12.tree=_10;
});
if(_11.classPrefix!=_10.classPrefix){
var _13=[this.domNode];
var _14;
var reg=new RegExp("(^|\\s)"+_11.classPrefix,"g");
while(_14=_13.pop()){
for(var i=0;i<_14.childNodes.length;i++){
var _17=_14.childNodes[i];
if(_17.nodeDocType!=1){
continue;
}
dojo.html.setClass(_17,dojo.html.getClass(_17).replace(reg,"$1"+_10.classPrefix));
_13.push(_17);
}
}
}
var _18={oldTree:_11,newTree:_10,node:this};
dojo.event.topic.publish(this.tree.eventNames.afterChangeTree,_18);
dojo.event.topic.publish(_10.eventNames.afterChangeTree,_18);
},addedTo:function(_19,_1a,_1b){
if(this.tree!==_19.tree){
this.updateTree(_19.tree);
}
if(_19.isTreeNode){
if(!_19.isFolder){
_19.setFolder();
_19.state=_19.loadStates.LOADED;
}
}
var _1c=_19.children.length;
this.insertNode(_19,_1a);
this.viewAddLayout();
if(_1c>1){
if(_1a==0&&_19.children[1] instanceof dojo.widget.Widget){
_19.children[1].viewUpdateLayout();
}
if(_1a==_1c-1&&_19.children[_1c-2] instanceof dojo.widget.Widget){
_19.children[_1c-2].viewUpdateLayout();
}
}else{
if(_19.isTreeNode){
_19.viewSetHasChildren();
}
}
if(!_1b){
var _1d={child:this,index:_1a,parent:_19};
dojo.event.topic.publish(this.tree.eventNames.afterAddChild,_1d);
}
},createSimple:function(_1e,_1f){
if(_1e.tree){
var _20=_1e.tree;
}else{
if(_1f){
var _20=_1f.tree;
}else{
dojo.raise("createSimple: can't evaluate tree");
}
}
_20=dojo.widget.byId(_20);
var _21=new _20.defaultChildWidget();
for(var x in _1e){
_21[x]=_1e[x];
}
_21.toggleObj=dojo.lfx.toggle[_21.toggle.toLowerCase()]||dojo.lfx.toggle.plain;
dojo.widget.manager.add(_21);
_21.buildRendering(_1e,{},_1f);
_21.initialize(_1e,{},_1f);
if(_21.parent){
delete dojo.widget.manager.topWidgets[_21.widgetId];
}
return _21;
},viewUpdateLayout:function(){
this.viewRemoveLayout();
this.viewAddLayout();
},viewAddContainer:function(){
this.containerNode=this.tree.containerNodeTemplate.cloneNode(true);
this.domNode.appendChild(this.containerNode);
},viewAddLayout:function(){
if(this.parent["isTree"]){
dojo.html.setClass(this.domNode,dojo.html.getClass(this.domNode)+" "+this.tree.classPrefix+"IsRoot");
}
if(this.isLastChild()){
dojo.html.setClass(this.domNode,dojo.html.getClass(this.domNode)+" "+this.tree.classPrefix+"IsLast");
}
},viewRemoveLayout:function(){
dojo.html.removeClass(this.domNode,this.tree.classPrefix+"IsRoot");
dojo.html.removeClass(this.domNode,this.tree.classPrefix+"IsLast");
},viewGetExpandClass:function(){
if(this.isFolder){
return this.isExpanded?"ExpandOpen":"ExpandClosed";
}else{
return "ExpandLeaf";
}
},viewSetExpand:function(){
var _23=this.tree.classPrefix+this.viewGetExpandClass();
var reg=new RegExp("(^|\\s)"+this.tree.classPrefix+"Expand\\w+","g");
dojo.html.setClass(this.domNode,dojo.html.getClass(this.domNode).replace(reg,"")+" "+_23);
this.viewSetHasChildrenAndExpand();
},viewGetChildrenClass:function(){
return "Children"+(this.children.length?"Yes":"No");
},viewSetHasChildren:function(){
var _25=this.tree.classPrefix+this.viewGetChildrenClass();
var reg=new RegExp("(^|\\s)"+this.tree.classPrefix+"Children\\w+","g");
dojo.html.setClass(this.domNode,dojo.html.getClass(this.domNode).replace(reg,"")+" "+_25);
this.viewSetHasChildrenAndExpand();
},viewSetHasChildrenAndExpand:function(){
var _27=this.tree.classPrefix+"State"+this.viewGetChildrenClass()+"-"+this.viewGetExpandClass();
var reg=new RegExp("(^|\\s)"+this.tree.classPrefix+"State[\\w-]+","g");
dojo.html.setClass(this.domNode,dojo.html.getClass(this.domNode).replace(reg,"")+" "+_27);
},viewUnfocus:function(){
dojo.html.removeClass(this.labelNode,this.tree.classPrefix+"LabelFocused");
},viewFocus:function(){
dojo.html.addClass(this.labelNode,this.tree.classPrefix+"LabelFocused");
},viewEmphasize:function(){
dojo.html.clearSelection(this.labelNode);
dojo.html.addClass(this.labelNode,this.tree.classPrefix+"NodeEmphasized");
},viewUnemphasize:function(){
dojo.html.removeClass(this.labelNode,this.tree.classPrefix+"NodeEmphasized");
},detach:function(){
if(!this.parent){
return;
}
var _29=this.parent;
var _2a=this.getParentIndex();
this.doDetach.apply(this,arguments);
dojo.event.topic.publish(this.tree.eventNames.afterDetach,{child:this,parent:_29,index:_2a});
},doDetach:function(){
var _2b=this.parent;
if(!_2b){
return;
}
var _2c=this.getParentIndex();
this.viewRemoveLayout();
dojo.widget.DomWidget.prototype.removeChild.call(_2b,this);
var _2d=_2b.children.length;
if(_2d>0){
if(_2c==0){
_2b.children[0].viewUpdateLayout();
}
if(_2c==_2d){
_2b.children[_2d-1].viewUpdateLayout();
}
}else{
if(_2b.isTreeNode){
_2b.viewSetHasChildren();
}
}
if(this.tree.unsetFolderOnEmpty&&!_2b.children.length&&_2b.isTreeNode){
_2b.unsetFolder();
}
this.parent=null;
},destroy:function(){
dojo.event.topic.publish(this.tree.eventNames.beforeNodeDestroy,{source:this});
this.detach();
return dojo.widget.HtmlWidget.prototype.destroy.apply(this,arguments);
},expand:function(){
if(this.isExpanded){
return;
}
if(this.tryLazyInit){
this.setChildren();
this.tryLazyInit=false;
}
this.isExpanded=true;
this.viewSetExpand();
this.showChildren();
},collapse:function(){
if(!this.isExpanded){
return;
}
this.isExpanded=false;
this.hideChildren();
},hideChildren:function(){
this.tree.toggleObj.hide(this.containerNode,this.tree.toggleDuration,this.explodeSrc,dojo.lang.hitch(this,"onHideChildren"));
},showChildren:function(){
this.tree.toggleObj.show(this.containerNode,this.tree.toggleDuration,this.explodeSrc,dojo.lang.hitch(this,"onShowChildren"));
},onShowChildren:function(){
this.onShow();
dojo.event.topic.publish(this.tree.eventNames.afterExpand,{source:this});
},onHideChildren:function(){
this.viewSetExpand();
this.onHide();
dojo.event.topic.publish(this.tree.eventNames.afterCollapse,{source:this});
},setTitle:function(_2e){
var _2f=this.title;
this.labelNode.innerHTML=this.title=_2e;
dojo.event.topic.publish(this.tree.eventNames.afterSetTitle,{source:this,oldTitle:_2f});
},toString:function(){
return "["+this.widgetType+", "+this.title+"]";
}});
