dojo.provide("dojo.widget.PageContainer");
dojo.require("dojo.lang.func");
dojo.require("dojo.widget.*");
dojo.require("dojo.event.*");
dojo.require("dojo.html.selection");
dojo.widget.defineWidget("dojo.widget.PageContainer",dojo.widget.HtmlWidget,{isContainer:true,doLayout:true,templateString:"<div dojoAttachPoint='containerNode'></div>",selectedChild:"",fillInTemplate:function(_1,_2){
var _3=this.getFragNodeRef(_2);
dojo.html.copyStyle(this.domNode,_3);
dojo.widget.PageContainer.superclass.fillInTemplate.apply(this,arguments);
},postCreate:function(_4,_5){
if(this.children.length){
dojo.lang.forEach(this.children,this._setupChild,this);
var _6;
if(this.selectedChild){
this.selectChild(this.selectedChild);
}else{
for(var i=0;i<this.children.length;i++){
if(this.children[i].selected){
this.selectChild(this.children[i]);
break;
}
}
if(!this.selectedChildWidget){
this.selectChild(this.children[0]);
}
}
}
},addChild:function(_8){
dojo.widget.PageContainer.superclass.addChild.apply(this,arguments);
this._setupChild(_8);
this.onResized();
if(!this.selectedChildWidget){
this.selectChild(_8);
}
},_setupChild:function(_9){
_9.hide();
_9.domNode.style.position="relative";
dojo.event.topic.publish(this.widgetId+"-addChild",_9);
},removeChild:function(_a){
dojo.widget.PageContainer.superclass.removeChild.apply(this,arguments);
if(this._beingDestroyed){
return;
}
dojo.event.topic.publish(this.widgetId+"-removeChild",_a);
this.onResized();
if(this.selectedChildWidget===_a){
this.selectedChildWidget=undefined;
if(this.children.length>0){
this.selectChild(this.children[0],true);
}
}
},selectChild:function(_b,_c){
_b=dojo.widget.byId(_b);
this.correspondingPageButton=_c;
if(this.selectedChildWidget){
this._hideChild(this.selectedChildWidget);
}
this.selectedChildWidget=_b;
this.selectedChild=_b.widgetId;
this._showChild(_b);
_b.isFirstChild=(_b==this.children[0]);
_b.isLastChild=(_b==this.children[this.children.length-1]);
dojo.event.topic.publish(this.widgetId+"-selectChild",_b);
},forward:function(){
var _d=dojo.lang.find(this.children,this.selectedChildWidget);
this.selectChild(this.children[_d+1]);
},back:function(){
var _e=dojo.lang.find(this.children,this.selectedChildWidget);
this.selectChild(this.children[_e-1]);
},onResized:function(){
if(this.doLayout&&this.selectedChildWidget){
with(this.selectedChildWidget.domNode.style){
top=dojo.html.getPixelValue(this.containerNode,"padding-top",true);
left=dojo.html.getPixelValue(this.containerNode,"padding-left",true);
}
var _f=dojo.html.getContentBox(this.containerNode);
this.selectedChildWidget.resizeTo(_f.width,_f.height);
}
},_showChild:function(_10){
if(this.doLayout){
var _11=dojo.html.getContentBox(this.containerNode);
_10.resizeTo(_11.width,_11.height);
}
_10.selected=true;
_10.show();
},_hideChild:function(_12){
_12.selected=false;
_12.hide();
},closeChild:function(_13){
var _14=_13.onClose(this,_13);
if(_14){
this.removeChild(_13);
_13.destroy();
}
},destroy:function(){
this._beingDestroyed=true;
dojo.event.topic.destroy(this.widgetId+"-addChild");
dojo.event.topic.destroy(this.widgetId+"-removeChild");
dojo.event.topic.destroy(this.widgetId+"-selectChild");
dojo.widget.PageContainer.superclass.destroy.apply(this,arguments);
}});
dojo.widget.defineWidget("dojo.widget.PageController",dojo.widget.HtmlWidget,{templateString:"<span wairole='tablist' dojoAttachEvent='onKey'></span>",isContainer:true,containerId:"",buttonWidget:"PageButton","class":"dojoPageController",fillInTemplate:function(){
dojo.html.addClass(this.domNode,this["class"]);
dojo.widget.wai.setAttr(this.domNode,"waiRole","role","tablist");
},postCreate:function(){
this.pane2button={};
var _15=dojo.widget.byId(this.containerId);
if(_15){
dojo.lang.forEach(_15.children,this.onAddChild,this);
}
dojo.event.topic.subscribe(this.containerId+"-addChild",this,"onAddChild");
dojo.event.topic.subscribe(this.containerId+"-removeChild",this,"onRemoveChild");
dojo.event.topic.subscribe(this.containerId+"-selectChild",this,"onSelectChild");
},destroy:function(){
dojo.event.topic.unsubscribe(this.containerId+"-addChild",this,"onAddChild");
dojo.event.topic.unsubscribe(this.containerId+"-removeChild",this,"onRemoveChild");
dojo.event.topic.unsubscribe(this.containerId+"-selectChild",this,"onSelectChild");
dojo.widget.PageController.superclass.destroy.apply(this,arguments);
},onAddChild:function(_16){
var _17=dojo.widget.createWidget(this.buttonWidget,{label:_16.label,closeButton:_16.closable});
this.addChild(_17);
this.domNode.appendChild(_17.domNode);
this.pane2button[_16]=_17;
_16.controlButton=_17;
var _18=this;
dojo.event.connect(_17,"onClick",function(){
_18.onButtonClick(_16);
});
dojo.event.connect(_17,"onCloseButtonClick",function(){
_18.onCloseButtonClick(_16);
});
},onRemoveChild:function(_19){
if(this._currentChild==_19){
this._currentChild=null;
}
var _1a=this.pane2button[_19];
if(_1a){
_1a.destroy();
}
this.pane2button[_19]=null;
},onSelectChild:function(_1b){
if(this._currentChild){
var _1c=this.pane2button[this._currentChild];
_1c.clearSelected();
}
var _1d=this.pane2button[_1b];
_1d.setSelected();
this._currentChild=_1b;
},onButtonClick:function(_1e){
var _1f=dojo.widget.byId(this.containerId);
_1f.selectChild(_1e,false,this);
},onCloseButtonClick:function(_20){
var _21=dojo.widget.byId(this.containerId);
_21.closeChild(_20);
},onKey:function(evt){
if((evt.keyCode==evt.KEY_RIGHT_ARROW)||(evt.keyCode==evt.KEY_LEFT_ARROW)){
var _23=0;
var _24=null;
var _23=dojo.lang.find(this.children,this.pane2button[this._currentChild]);
if(evt.keyCode==evt.KEY_RIGHT_ARROW){
_24=this.children[(_23+1)%this.children.length];
}else{
_24=this.children[(_23+(this.children.length-1))%this.children.length];
}
dojo.event.browser.stopEvent(evt);
_24.onClick();
}
}});
dojo.widget.defineWidget("dojo.widget.PageButton",dojo.widget.HtmlWidget,{templateString:"<span class='item'>"+"<span dojoAttachEvent='onClick' dojoAttachPoint='titleNode' class='selectButton'>${this.label}</span>"+"<span dojoAttachEvent='onClick:onCloseButtonClick' class='closeButton'>[X]</span>"+"</span>",label:"foo",closeButton:false,onClick:function(){
this.focus();
},onCloseButtonMouseOver:function(){
dojo.html.addClass(this.closeButtonNode,"closeHover");
},onCloseButtonMouseOut:function(){
dojo.html.removeClass(this.closeButtonNode,"closeHover");
},onCloseButtonClick:function(evt){
},setSelected:function(){
dojo.html.addClass(this.domNode,"current");
this.titleNode.setAttribute("tabIndex","0");
},clearSelected:function(){
dojo.html.removeClass(this.domNode,"current");
this.titleNode.setAttribute("tabIndex","-1");
},focus:function(){
if(this.titleNode.focus){
this.titleNode.focus();
}
}});
dojo.lang.extend(dojo.widget.Widget,{label:"",selected:false,closable:false,onClose:function(){
return true;
}});
