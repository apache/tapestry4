dojo.provide("dojo.widget.TreeDisableWrapExtension");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.widget.TreeExtension");
dojo.widget.defineWidget("dojo.widget.TreeDisableWrapExtension",dojo.widget.TreeExtension,{templateCssString:"\n/* CSS for TreeDisableWrapExtension */\n\n.TreeDisableWrap {\n\twhite-space: nowrap;\n}\n.TreeIEDisableWrap {\n\twidth: expression( 5 + firstChild.offsetWidth );\n}\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/TreeDisableWrap.css"),listenTree:function(_1){
var _2=document.createElement("div");
var _3=_1.classPrefix+"DisableWrap";
if(dojo.render.html.ie){
_3=_3+" "+_1.classPrefix+"IEDisableWrap";
}
dojo.html.setClass(_2,_3);
var _4=document.createElement("table");
_2.appendChild(_4);
var _5=document.createElement("tbody");
_4.appendChild(_5);
var tr=document.createElement("tr");
_5.appendChild(tr);
var td=document.createElement("td");
tr.appendChild(td);
if(_1.domNode.parentNode){
_1.domNode.parentNode.replaceChild(_2,_1.domNode);
}
td.appendChild(_1.domNode);
_1.domNode=_2;
}});
