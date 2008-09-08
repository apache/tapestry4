dojo.require("dojo.widget.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.widget.RichText");
dojo.provide("dojo.widget.TreeEditor");
dojo.widget.defineWidget("dojo.widget.TreeEditor",dojo.widget.HtmlWidget,{singleLineMode:false,saveOnBlur:true,sync:false,selectOnOpen:true,controller:null,node:null,richTextParams:{styleSheets:"src/widget/templates/TreeEditor.css"},getContents:function(){
return this.richText.getEditorContent();
},open:function(_1){
if(!this.richText){
this.richText=dojo.widget.createWidget("RichText",this.richTextParams,_1.labelNode);
dojo.event.connect("around",this.richText,"onKeyDown",this,"richText_onKeyDown");
dojo.event.connect(this.richText,"onBlur",this,"richText_onBlur");
var _2=this;
dojo.event.connect(this.richText,"onLoad",function(){
if(_2.selectOnOpen){
_2.richText.execCommand("selectall");
}
});
}else{
this.richText.open(_1.labelNode);
}
this.node=_1;
},close:function(_3){
this.richText.close(_3);
this.node=null;
},isClosed:function(){
return !this.richText||this.richText.isClosed;
},execCommand:function(){
this.richText.execCommand.apply(this.richText,arguments);
},richText_onKeyDown:function(_4){
var e=_4.args[0];
if((!e)&&(this.object)){
e=dojo.event.browser.fixEvent(this.editor.window.event);
}
switch(e.keyCode){
case e.KEY_ESCAPE:
this.finish(false);
dojo.event.browser.stopEvent(e);
break;
case e.KEY_ENTER:
if(e.ctrlKey&&!this.singleLineMode){
this.execCommand("inserthtml","<br/>");
}else{
this.finish(true);
}
dojo.event.browser.stopEvent(e);
break;
default:
return _4.proceed();
}
},richText_onBlur:function(){
this.finish(this.saveOnBlur);
},finish:function(_6){
return this.controller.editLabelFinish(_6,this.sync);
}});
