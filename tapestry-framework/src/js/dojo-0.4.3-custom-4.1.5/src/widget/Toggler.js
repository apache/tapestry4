dojo.provide("dojo.widget.Toggler");
dojo.require("dojo.widget.*");
dojo.require("dojo.event.*");
dojo.widget.defineWidget("dojo.widget.Toggler",dojo.widget.HtmlWidget,{targetId:"",fillInTemplate:function(){
dojo.event.connect(this.domNode,"onclick",this,"onClick");
},onClick:function(){
var _1=dojo.widget.byId(this.targetId);
if(!_1){
return;
}
_1.explodeSrc=this.domNode;
_1.toggleShowing();
}});
