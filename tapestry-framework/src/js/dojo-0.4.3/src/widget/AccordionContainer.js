dojo.provide("dojo.widget.AccordionContainer");
dojo.require("dojo.widget.*");
dojo.require("dojo.html.*");
dojo.require("dojo.lfx.html");
dojo.require("dojo.html.selection");
dojo.require("dojo.widget.html.layout");
dojo.require("dojo.widget.PageContainer");
dojo.widget.defineWidget("dojo.widget.AccordionContainer",dojo.widget.HtmlWidget,{isContainer:true,labelNodeClass:"label",containerNodeClass:"accBody",duration:250,fillInTemplate:function(){
with(this.domNode.style){
if(position!="absolute"){
position="relative";
}
overflow="hidden";
}
},addChild:function(_1){
var _2=this._addChild(_1);
this._setSizes();
return _2;
},_addChild:function(_3){
if(_3.open){
dojo.deprecated("open parameter deprecated, use 'selected=true' instead will be removed in ","0.5");
dojo.debug(_3.widgetId+": open == "+_3.open);
_3.selected=true;
}
if(_3.widgetType!="AccordionPane"){
var _4=dojo.widget.createWidget("AccordionPane",{label:_3.label,selected:_3.selected,labelNodeClass:this.labelNodeClass,containerNodeClass:this.containerNodeClass,allowCollapse:this.allowCollapse});
_4.addChild(_3);
this.addWidgetAsDirectChild(_4);
this.registerChild(_4,this.children.length);
return _4;
}else{
dojo.html.addClass(_3.containerNode,this.containerNodeClass);
dojo.html.addClass(_3.labelNode,this.labelNodeClass);
this.addWidgetAsDirectChild(_3);
this.registerChild(_3,this.children.length);
return _3;
}
},postCreate:function(){
var _5=this.children;
this.children=[];
dojo.html.removeChildren(this.domNode);
dojo.lang.forEach(_5,dojo.lang.hitch(this,"_addChild"));
this._setSizes();
},removeChild:function(_6){
dojo.widget.AccordionContainer.superclass.removeChild.call(this,_6);
this._setSizes();
},onResized:function(){
this._setSizes();
},_setSizes:function(){
var _7=0;
var _8=0;
dojo.lang.forEach(this.children,function(_9,_a){
_7+=_9.getLabelHeight();
if(_9.selected){
_8=_a;
}
});
var _b=dojo.html.getContentBox(this.domNode);
var y=0;
dojo.lang.forEach(this.children,function(_d,_e){
var _f=_d.getLabelHeight();
_d.resizeTo(_b.width,_b.height-_7+_f);
_d.domNode.style.zIndex=_e+1;
_d.domNode.style.position="absolute";
_d.domNode.style.top=y+"px";
y+=(_e==_8)?dojo.html.getBorderBox(_d.domNode).height:_f;
});
},selectChild:function(_10){
dojo.lang.forEach(this.children,function(_11){
_11.setSelected(_11==_10);
});
var y=0;
var _13=[];
dojo.lang.forEach(this.children,function(_14,idx){
if(_14.domNode.style.top!=(y+"px")){
_13.push(dojo.lfx.html.slideTo(_14.domNode,{top:y,left:0},this.duration));
}
y+=_14.selected?dojo.html.getBorderBox(_14.domNode).height:_14.getLabelHeight();
},this);
dojo.lfx.combine(_13).play();
}});
dojo.widget.defineWidget("dojo.widget.AccordionPane",dojo.widget.HtmlWidget,{label:"","class":"dojoAccordionPane",labelNodeClass:"label",containerNodeClass:"accBody",selected:false,templateString:"<div dojoAttachPoint=\"domNode\">\n<div dojoAttachPoint=\"labelNode\" dojoAttachEvent=\"onclick: onLabelClick\" class=\"${this.labelNodeClass}\">${this.label}</div>\n<div dojoAttachPoint=\"containerNode\" style=\"overflow: hidden;\" class=\"${this.containerNodeClass}\"></div>\n</div>\n",templateCssString:".dojoAccordionPane .label {\n\tcolor: #000;\n\tfont-weight: bold;\n\tbackground: url(\"images/soriaAccordionOff.gif\") repeat-x top left #85aeec;\n\tborder:1px solid #d9d9d9;\n\tfont-size:0.9em;\n}\n\n.dojoAccordionPane-selected .label {\n\tbackground: url(\"images/soriaAccordionSelected.gif\") repeat-x top left #85aeec;\n\tborder:1px solid #84a3d1;\n}\n\n.dojoAccordionPane .label:hover {\n\tcursor: pointer;\n}\n\n.dojoAccordionPane .accBody {\n\tbackground: #fff;\n\toverflow: auto;\n\tborder:1px solid #84a3d1;\n}\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/AccordionPane.css"),isContainer:true,fillInTemplate:function(){
dojo.html.addClass(this.domNode,this["class"]);
dojo.widget.AccordionPane.superclass.fillInTemplate.call(this);
dojo.html.disableSelection(this.labelNode);
this.setSelected(this.selected);
},setLabel:function(_16){
this.labelNode.innerHTML=_16;
},resizeTo:function(_17,_18){
dojo.html.setMarginBox(this.domNode,{width:_17,height:_18});
var _19=[{domNode:this.labelNode,layoutAlign:"top"},{domNode:this.containerNode,layoutAlign:"client"}];
dojo.widget.html.layout(this.domNode,_19);
var _1a=dojo.html.getContentBox(this.containerNode);
this.children[0].resizeTo(_1a.width,_1a.height);
},getLabelHeight:function(){
return dojo.html.getMarginBox(this.labelNode).height;
},onLabelClick:function(){
this.parent.selectChild(this);
},setSelected:function(_1b){
this.selected=_1b;
(_1b?dojo.html.addClass:dojo.html.removeClass)(this.domNode,this["class"]+"-selected");
var _1c=this.children[0];
if(_1c){
if(_1b){
if(!_1c.isShowing()){
_1c.show();
}else{
_1c.onShow();
}
}else{
_1c.onHide();
}
}
}});
dojo.lang.extend(dojo.widget.Widget,{open:false});
