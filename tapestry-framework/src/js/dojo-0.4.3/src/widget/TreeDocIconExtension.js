dojo.provide("dojo.widget.TreeDocIconExtension");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.widget.TreeExtension");
dojo.widget.defineWidget("dojo.widget.TreeDocIconExtension",[dojo.widget.TreeExtension],{templateCssString:"\n/* CSS for TreeDocIconExtension */\n\n\n/* long vertical line under docIcon, connecting w/ children */\n.TreeStateChildrenYes-ExpandOpen .TreeIconContent {\n    background-image : url('../templates/images/TreeV3/i_long.gif');\n    background-repeat : no-repeat;\n    background-position: 18px 9px;\n}\n\n/* close has higher priority */\n.TreeStateChildrenYes-ExpandClosed .TreeIconContent {\n    background-image : url();\n}\n\n/* higher priotity: same length and appear after background-definition */\n.TreeStateChildrenNo-ExpandLeaf .TreeIconContent {\n    background-image : url();\n}\n\n.TreeStateChildrenNo-ExpandClosed .TreeIconContent {\n    background-image : url();\n}\n\n.TreeStateChildrenNo-ExpandOpen .TreeIconContent {\n    background-image : url();\n}\n\n\n/* highest priority */\n.TreeIconDocument {\n    background-image: url('../templates/images/TreeV3/document.gif');\n}\n\n.TreeExpandOpen .TreeIconFolder {\n    background-image: url('../templates/images/TreeV3/open.gif');\n}\n\n.TreeExpandClosed .TreeIconFolder {\n    background-image: url('../templates/images/TreeV3/closed.gif');\n}\n\n/* generic class for docIcon */\n.TreeIcon {\n    width: 18px;\n    height: 18px;\n    float: left;\n    display: inline;\n    background-repeat : no-repeat;\n}\n\ndiv.TreeContent {\n    margin-left: 36px;\n}\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/TreeDocIcon.css"),listenTreeEvents:["afterChangeTree","afterSetFolder","afterUnsetFolder"],listenNodeFilter:function(_1){
return _1 instanceof dojo.widget.Widget;
},getnodeDocType:function(_2){
var _3=_2.getnodeDocType();
if(!_3){
_3=_2.isFolder?"Folder":"Document";
}
return _3;
},setnodeDocTypeClass:function(_4){
var _5=new RegExp("(^|\\s)"+_4.tree.classPrefix+"Icon\\w+","g");
var _6=dojo.html.getClass(_4.iconNode).replace(_5,"")+" "+_4.tree.classPrefix+"Icon"+this.getnodeDocType(_4);
dojo.html.setClass(_4.iconNode,_6);
},onAfterSetFolder:function(_7){
if(_7.source.iconNode){
this.setnodeDocTypeClass(_7.source);
}
},onAfterUnsetFolder:function(_8){
this.setnodeDocTypeClass(_8.source);
},listenNode:function(_9){
_9.contentIconNode=document.createElement("div");
var _a=_9.tree.classPrefix+"IconContent";
if(dojo.render.html.ie){
_a=_a+" "+_9.tree.classPrefix+"IEIconContent";
}
dojo.html.setClass(_9.contentIconNode,_a);
_9.contentNode.parentNode.replaceChild(_9.contentIconNode,_9.expandNode);
_9.iconNode=document.createElement("div");
dojo.html.setClass(_9.iconNode,_9.tree.classPrefix+"Icon"+" "+_9.tree.classPrefix+"Icon"+this.getnodeDocType(_9));
_9.contentIconNode.appendChild(_9.expandNode);
_9.contentIconNode.appendChild(_9.iconNode);
dojo.dom.removeNode(_9.contentNode);
_9.contentIconNode.appendChild(_9.contentNode);
},onAfterChangeTree:function(_b){
var _c=this;
if(!_b.oldTree||!this.listenedTrees[_b.oldTree.widgetId]){
this.processDescendants(_b.node,this.listenNodeFilter,this.listenNode);
}
}});
