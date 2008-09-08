dojo.provide("dojo.widget.TreeLinkExtension");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.widget.TreeExtension");
dojo.widget.defineWidget("dojo.widget.TreeLinkExtension",dojo.widget.TreeExtension,function(){
this.params={};
},{listenTreeEvents:["afterChangeTree"],listenTree:function(_1){
dojo.widget.TreeCommon.prototype.listenTree.call(this,_1);
var _2=_1.labelNodeTemplate;
var _3=this.makeALabel();
dojo.html.setClass(_3,dojo.html.getClass(_2));
_2.parentNode.replaceChild(_3,_2);
},makeALabel:function(){
var _4=document.createElement("a");
for(var _5 in this.params){
if(_5 in {}){
continue;
}
_4.setAttribute(_5,this.params[_5]);
}
return _4;
},onAfterChangeTree:function(_6){
var _7=this;
if(!_6.oldTree){
this.listenNode(_6.node);
}
},listenNode:function(_8){
for(var _9 in _8.object){
if(_9 in {}){
continue;
}
_8.labelNode.setAttribute(_9,_8.object[_9]);
}
}});
