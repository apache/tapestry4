dojo.provide("dojo.widget.Checkbox");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.event.*");
dojo.require("dojo.html.style");
dojo.require("dojo.html.selection");
dojo.widget.defineWidget("dojo.widget.Checkbox",dojo.widget.HtmlWidget,{templateString:"<span style=\"display: inline-block;\" tabIndex=\"${this.tabIndex}\" waiRole=\"checkbox\" id=\"${this.id}\">\n\t<img dojoAttachPoint=\"imageNode\" class=\"dojoHtmlCheckbox\" src=\"${dojoWidgetModuleUri}templates/images/blank.gif\" alt=\"\" />\n\t<input type=\"checkbox\" name=\"${this.name}\" style=\"display: none\" value=\"${this.value}\"\n\t\tdojoAttachPoint=\"inputNode\">\n</span>\n",templateCssString:".dojoHtmlCheckbox {\n\tborder: 0px;\n\twidth: 16px;\n\theight: 16px;\n\tmargin: 2px;\n\tvertical-align: middle;\n}\n\n.dojoHtmlCheckboxOn {\n\tbackground: url(check.gif) 0px 0px;\n}\n.dojoHtmlCheckboxOff {\n\tbackground: url(check.gif) -16px 0px;\n}\n.dojoHtmlCheckboxDisabledOn {\n\tbackground: url(check.gif) -32px 0px;\n}\n.dojoHtmlCheckboxDisabledOff {\n\tbackground: url(check.gif) -48px 0px;\n}\n.dojoHtmlCheckboxOnHover {\n\tbackground: url(check.gif) -64px 0px;\n}\n.dojoHtmlCheckboxOffHover {\n\tbackground: url(check.gif) -80px 0px;\n}\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/Checkbox.css"),name:"",id:"",checked:false,tabIndex:"",value:"on",postMixInProperties:function(){
dojo.widget.Checkbox.superclass.postMixInProperties.apply(this,arguments);
if(!this.disabled&&this.tabIndex==""){
this.tabIndex="0";
}
},fillInTemplate:function(){
this._setInfo();
},postCreate:function(){
var _1=true;
this.id=this.id!=""?this.id:this.widgetId;
if(this.id!=""){
var _2=document.getElementsByTagName("label");
if(_2!=null&&_2.length>0){
for(var i=0;i<_2.length;i++){
if(_2[i].htmlFor==this.id){
_2[i].id=(_2[i].htmlFor+"label");
this._connectEvents(_2[i]);
dojo.widget.wai.setAttr(this.domNode,"waiState","labelledby",_2[i].id);
break;
}
}
}
}
this._connectEvents(this.domNode);
this.inputNode.checked=this.checked;
},_connectEvents:function(_4){
dojo.event.connect(_4,"onmouseover",this,"mouseOver");
dojo.event.connect(_4,"onmouseout",this,"mouseOut");
dojo.event.connect(_4,"onkey",this,"onKey");
dojo.event.connect(_4,"onclick",this,"_onClick");
dojo.html.disableSelection(_4);
},_onClick:function(e){
if(this.disabled==false){
this.checked=!this.checked;
this._setInfo();
}
e.preventDefault();
e.stopPropagation();
this.onClick();
},setValue:function(_6){
if(this.disabled==false){
this.checked=_6;
this._setInfo();
}
},onClick:function(){
},onKey:function(e){
var k=dojo.event.browser.keys;
if(e.key==" "){
this._onClick(e);
}
},mouseOver:function(e){
this._hover(e,true);
},mouseOut:function(e){
this._hover(e,false);
},_hover:function(e,_c){
if(this.disabled==false){
var _d=this.checked?"On":"Off";
var _e="dojoHtmlCheckbox"+_d+"Hover";
if(_c){
dojo.html.addClass(this.imageNode,_e);
}else{
dojo.html.removeClass(this.imageNode,_e);
}
}
},_setInfo:function(){
var _f="dojoHtmlCheckbox"+(this.disabled?"Disabled":"")+(this.checked?"On":"Off");
dojo.html.setClass(this.imageNode,"dojoHtmlCheckbox "+_f);
this.inputNode.checked=this.checked;
if(this.disabled){
this.inputNode.setAttribute("disabled",true);
}else{
this.inputNode.removeAttribute("disabled");
}
dojo.widget.wai.setAttr(this.domNode,"waiState","checked",this.checked);
}});
dojo.widget.defineWidget("dojo.widget.a11y.Checkbox",dojo.widget.Checkbox,{templateString:"<span class='dojoHtmlCheckbox'>\n\t<input type=\"checkbox\" name=\"${this.name}\" tabIndex=\"${this.tabIndex}\" id=\"${this.id}\" value=\"${this.value}\"\n\t\t dojoAttachEvent=\"onClick: _onClick;\" dojoAttachPoint=\"inputNode\"> \n</span>\n",fillInTemplate:function(){
},postCreate:function(_10,_11){
this.inputNode.checked=this.checked;
if(this.disabled){
this.inputNode.setAttribute("disabled",true);
}
},_onClick:function(){
this.onClick();
}});
