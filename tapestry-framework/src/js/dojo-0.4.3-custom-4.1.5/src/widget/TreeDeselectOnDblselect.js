dojo.provide("dojo.widget.TreeDeselectOnDblselect");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.widget.TreeSelectorV3");
dojo.deprecated("Does anyone still need this extension? (TreeDeselectOnDblselect)");
dojo.widget.defineWidget("dojo.widget.TreeDeselectOnDblselect",[dojo.widget.HtmlWidget],{selector:"",initialize:function(){
this.selector=dojo.widget.byId(this.selector);
dojo.event.topic.subscribe(this.selector.eventNames.dblselect,this,"onDblselect");
},onDblselect:function(_1){
this.selector.deselect(_1.node);
}});
