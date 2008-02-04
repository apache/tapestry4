dojo.require("dojo.widget.DomWidget");
dojo.provide("dojo.widget.SvgWidget");
dojo.provide("dojo.widget.SVGWidget");
dojo.require("dojo.dom");
dojo.require("dojo.experimental");
dojo.experimental("dojo.widget.SvgWidget");
dojo.widget.declare("dojo.widget.SvgWidget",dojo.widget.DomWidget,{createNodesFromText:function(_1,_2){
return dojo.svg.createNodesFromText(_1,_2);
}});
dojo.widget.SVGWidget=dojo.widget.SvgWidget;
try{
(function(){
var tf=function(){
var rw=new function(){
dojo.widget.SvgWidget.call(this);
this.buildRendering=function(){
return;
};
this.destroyRendering=function(){
return;
};
this.postInitialize=function(){
return;
};
this.widgetType="SVGRootWidget";
this.domNode=document.documentElement;
};
var wm=dojo.widget.manager;
wm.root=rw;
wm.add(rw);
wm.getWidgetFromNode=function(_6){
var _7=function(x){
if(x.domNode==_6){
return true;
}
};
var _9=[];
while((_6)&&(_9.length<1)){
_9=this.getWidgetsByFilter(_7);
_6=_6.parentNode;
}
if(_9.length>0){
return _9[0];
}else{
return null;
}
};
wm.getWidgetFromEvent=function(_a){
return this.getWidgetFromNode(_a.target);
};
wm.getWidgetFromPrimitive=wm.getWidgetFromNode;
};
dojo.event.connect(dojo.hostenv,"loaded",tf);
})();
}
catch(e){
alert(e);
}
