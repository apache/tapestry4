dojo.provide("dojo.widget.LinkPane");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.ContentPane");
dojo.require("dojo.html.style");
dojo.widget.defineWidget("dojo.widget.LinkPane",dojo.widget.ContentPane,{templateString:"<div class=\"dojoLinkPane\"></div>",fillInTemplate:function(_1,_2){
var _3=this.getFragNodeRef(_2);
this.label+=_3.innerHTML;
var _3=this.getFragNodeRef(_2);
dojo.html.copyStyle(this.domNode,_3);
}});
