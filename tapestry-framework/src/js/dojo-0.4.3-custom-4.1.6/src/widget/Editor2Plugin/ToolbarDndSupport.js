dojo.provide("dojo.widget.Editor2Plugin.ToolbarDndSupport");
dojo.require("dojo.dnd.*");
dojo.event.topic.subscribe("dojo.widget.Editor2::preLoadingToolbar",function(_1){
dojo.dnd.dragManager.nestedTargets=true;
var p=new dojo.widget.Editor2Plugin.ToolbarDndSupport(_1);
});
dojo.declare("dojo.widget.Editor2Plugin.ToolbarDndSupport",null,{lookForClass:"dojoEditorToolbarDnd TB_ToolbarSet TB_Toolbar",initializer:function(_3){
this.editor=_3;
dojo.event.connect(this.editor,"toolbarLoaded",this,"setup");
this.editor.registerLoadedPlugin(this);
},setup:function(){
dojo.event.disconnect(this.editor,"toolbarLoaded",this,"setup");
var _4=this.editor.toolbarWidget;
dojo.event.connect("before",_4,"destroy",this,"destroy");
var _5=dojo.html.getElementsByClass(this.lookForClass,_4.domNode,null,dojo.html.classMatchType.ContainsAny);
if(!_5){
dojo.debug("dojo.widget.Editor2Plugin.ToolbarDndSupport: No dom node with class in "+this.lookForClass);
return;
}
for(var i=0;i<_5.length;i++){
var _7=_5[i];
var _8=_7.getAttribute("dojoETDropTarget");
if(_8){
(new dojo.dnd.HtmlDropTarget(_7,[_8+_4.widgetId])).vertical=true;
}
var _9=_7.getAttribute("dojoETDragSource");
if(_9){
new dojo.dnd.HtmlDragSource(_7,_9+_4.widgetId);
}
}
},destroy:function(){
this.editor.unregisterLoadedPlugin(this);
}});
