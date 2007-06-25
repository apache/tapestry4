dojo.provide("dojo.widget.TreeV3");
dojo.require("dojo.widget.TreeWithNode");
dojo.require("dojo.widget.*");
dojo.require("dojo.event.*");
dojo.require("dojo.io.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.widget.TreeNodeV3");
dojo.widget.defineWidget("dojo.widget.TreeV3",[dojo.widget.HtmlWidget,dojo.widget.TreeWithNode],function(){
this.eventNames={};
this.DndAcceptTypes=[];
this.actionsDisabled=[];
this.listeners=[];
this.tree=this;
},{DndMode:"",defaultChildWidget:null,defaultChildTitle:"New Node",eagerWidgetInstantiation:false,eventNamesDefault:{afterTreeCreate:"afterTreeCreate",beforeTreeDestroy:"beforeTreeDestroy",beforeNodeDestroy:"beforeNodeDestroy",afterChangeTree:"afterChangeTree",afterSetFolder:"afterSetFolder",afterUnsetFolder:"afterUnsetFolder",beforeMoveFrom:"beforeMoveFrom",beforeMoveTo:"beforeMoveTo",afterMoveFrom:"afterMoveFrom",afterMoveTo:"afterMoveTo",afterAddChild:"afterAddChild",afterDetach:"afterDetach",afterExpand:"afterExpand",beforeExpand:"beforeExpand",afterSetTitle:"afterSetTitle",afterCollapse:"afterCollapse",beforeCollapse:"beforeCollapse"},classPrefix:"Tree",style:"",allowAddChildToLeaf:true,unsetFolderOnEmpty:true,DndModes:{BETWEEN:1,ONTO:2},DndAcceptTypes:"",templateCssString:"/* indent for all tree children excepts root */\n.TreeNode {\n    background-image : url('../templates/images/TreeV3/i.gif');\n    background-position : top left;\n    background-repeat : repeat-y;\n    margin-left: 19px;\n    zoom: 1;\n}\n.TreeIsRoot {\n    margin-left: 0;\n}\n \n/* left vertical line (grid) for all nodes */\n.TreeIsLast {\n    background-image: url('../templates/images/TreeV3/i_half.gif');\n    background-repeat : no-repeat;\n}\n \n.TreeExpandOpen .TreeExpand {\n    background-image: url('../templates/images/TreeV3/expand_minus.gif');\n}\n \n/* closed is higher priority than open */\n.TreeExpandClosed .TreeExpand {\n    background-image: url('../templates/images/TreeV3/expand_plus.gif');\n}\n \n/* highest priority */\n.TreeExpandLeaf .TreeExpand {\n    background-image: url('../templates/images/TreeV3/expand_leaf.gif');\n}\n\n/* \nshould always override any expand setting, but do not touch children.\nif I add .TreeExpand .TreeExpandLoading same time and put it to top/bottom, then it will take precedence over +- for all descendants or always fail\nso I have to remove TreeExpand and process this one specifically\n*/\n\n.TreeExpandLoading   {\n    width: 18px;\n    height: 18px;\n    float: left;\n    display: inline;\n    background-repeat : no-repeat;\n    background-image: url('../templates/images/TreeV3/expand_loading.gif');\n}\n \n.TreeContent {\n    min-height: 18px;\n    min-width: 18px;\n    margin-left:18px;\n    cursor: default;\n    /* can't make inline - multiline bugs */\n}\n\n.TreeIEContent {\n\theight: 18px;\n}\n \n.TreeExpand {\n    width: 18px;\n    height: 18px;\n    float: left;\n    display: inline;\n    background-repeat : no-repeat;\n}\n \n/* same style as IE selection */\n.TreeNodeEmphasized {\n    background-color: Highlight;\n    color: HighlightText;\n}\n \n.TreeContent .RichTextEditable, .TreeContent .RichTextEditable iframe {\n      background-color: #ffc;\n      color: black;\n}\n\n/* don't use :focus due to opera's lack of support on div's */\n.TreeLabelFocused {\n      outline: 1px invert dotted;\n}\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/TreeV3.css"),templateString:"<div style=\"${this.style}\">\n</div>",isExpanded:true,isTree:true,createNode:function(_1){
_1.tree=this.widgetId;
if(_1.widgetName){
return dojo.widget.createWidget(_1.widgetName,_1);
}else{
if(this.defaultChildWidget.prototype.createSimple){
return this.defaultChildWidget.prototype.createSimple(_1);
}else{
var ns=this.defaultChildWidget.prototype.ns;
var wt=this.defaultChildWidget.prototype.widgetType;
return dojo.widget.createWidget(ns+":"+wt,_1);
}
}
},makeNodeTemplate:function(){
var _4=document.createElement("div");
dojo.html.setClass(_4,this.classPrefix+"Node "+this.classPrefix+"ExpandLeaf "+this.classPrefix+"ChildrenNo");
this.nodeTemplate=_4;
var _5=document.createElement("div");
var _6=this.classPrefix+"Expand";
if(dojo.render.html.ie){
_6=_6+" "+this.classPrefix+"IEExpand";
}
dojo.html.setClass(_5,_6);
this.expandNodeTemplate=_5;
var _7=document.createElement("span");
dojo.html.setClass(_7,this.classPrefix+"Label");
this.labelNodeTemplate=_7;
var _8=document.createElement("div");
var _6=this.classPrefix+"Content";
if(dojo.render.html.ie&&!dojo.render.html.ie70){
_6=_6+" "+this.classPrefix+"IEContent";
}
dojo.html.setClass(_8,_6);
this.contentNodeTemplate=_8;
_4.appendChild(_5);
_4.appendChild(_8);
_8.appendChild(_7);
},makeContainerNodeTemplate:function(){
var _9=document.createElement("div");
_9.style.display="none";
dojo.html.setClass(_9,this.classPrefix+"Container");
this.containerNodeTemplate=_9;
},actions:{ADDCHILD:"ADDCHILD"},getInfo:function(){
var _a={widgetId:this.widgetId,objectId:this.objectId};
return _a;
},adjustEventNames:function(){
for(var _b in this.eventNamesDefault){
if(dojo.lang.isUndefined(this.eventNames[_b])){
this.eventNames[_b]=this.widgetId+"/"+this.eventNamesDefault[_b];
}
}
},adjustDndMode:function(){
var _c=this;
var _d=0;
dojo.lang.forEach(this.DndMode.split(";"),function(_e){
var _f=_c.DndModes[dojo.string.trim(_e).toUpperCase()];
if(_f){
_d=_d|_f;
}
});
this.DndMode=_d;
},destroy:function(){
dojo.event.topic.publish(this.tree.eventNames.beforeTreeDestroy,{source:this});
return dojo.widget.HtmlWidget.prototype.destroy.apply(this,arguments);
},initialize:function(_10){
this.domNode.widgetId=this.widgetId;
for(var i=0;i<this.actionsDisabled.length;i++){
this.actionsDisabled[i]=this.actionsDisabled[i].toUpperCase();
}
if(!_10.defaultChildWidget){
this.defaultChildWidget=dojo.widget.TreeNodeV3;
}else{
this.defaultChildWidget=dojo.lang.getObjPathValue(_10.defaultChildWidget);
}
this.adjustEventNames();
this.adjustDndMode();
this.makeNodeTemplate();
this.makeContainerNodeTemplate();
this.containerNode=this.domNode;
dojo.html.setClass(this.domNode,this.classPrefix+"Container");
var _12=this;
dojo.lang.forEach(this.listeners,function(_13){
var t=dojo.lang.isString(_13)?dojo.widget.byId(_13):_13;
t.listenTree(_12);
});
},postCreate:function(){
dojo.event.topic.publish(this.eventNames.afterTreeCreate,{source:this});
},move:function(_15,_16,_17){
if(!_15.parent){
dojo.raise(this.widgetType+": child can be moved only while it's attached");
}
var _18=_15.parent;
var _19=_15.tree;
var _1a=_15.getParentIndex();
var _1b=_16.tree;
var _16=_16;
var _1c=_17;
var _1d={oldParent:_18,oldTree:_19,oldIndex:_1a,newParent:_16,newTree:_1b,newIndex:_1c,child:_15};
dojo.event.topic.publish(_19.eventNames.beforeMoveFrom,_1d);
dojo.event.topic.publish(_1b.eventNames.beforeMoveTo,_1d);
this.doMove.apply(this,arguments);
dojo.event.topic.publish(_19.eventNames.afterMoveFrom,_1d);
dojo.event.topic.publish(_1b.eventNames.afterMoveTo,_1d);
},doMove:function(_1e,_1f,_20){
_1e.doDetach();
_1f.doAddChild(_1e,_20);
},toString:function(){
return "["+this.widgetType+" ID:"+this.widgetId+"]";
}});
