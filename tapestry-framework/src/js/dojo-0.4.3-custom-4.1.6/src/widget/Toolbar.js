dojo.provide("dojo.widget.Toolbar");
dojo.require("dojo.widget.*");
dojo.require("dojo.html.style");
dojo.widget.defineWidget("dojo.widget.ToolbarContainer",dojo.widget.HtmlWidget,{isContainer:true,templateString:"<div class=\"toolbarContainer\" dojoAttachPoint=\"containerNode\"></div>",templateCssString:".toolbarContainer {\n\tborder-bottom : 0;\n\tbackground-color : #def;\n\tcolor : ButtonText;\n\tfont : Menu;\n\tbackground-image: url(images/toolbar-bg.gif);\n}\n\n.toolbar {\n\tpadding : 2px 4px;\n\tmin-height : 26px;\n\t_height : 26px;\n}\n\n.toolbarItem {\n\tfloat : left;\n\tpadding : 1px 2px;\n\tmargin : 0 2px 1px 0;\n\tcursor : pointer;\n}\n\n.toolbarItem.selected, .toolbarItem.down {\n\tmargin : 1px 1px 0 1px;\n\tpadding : 0px 1px;\n\tborder : 1px solid #bbf;\n\tbackground-color : #fafaff;\n}\n\n.toolbarButton img {\n\tvertical-align : bottom;\n}\n\n.toolbarButton span {\n\tline-height : 16px;\n\tvertical-align : middle;\n}\n\n.toolbarButton.hover {\n\tpadding : 0px 1px;\n\tborder : 1px solid #99c;\n}\n\n.toolbarItem.disabled {\n\topacity : 0.3;\n\tfilter : alpha(opacity=30);\n\tcursor : default;\n}\n\n.toolbarSeparator {\n\tcursor : default;\n}\n\n.toolbarFlexibleSpace {\n}\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/Toolbar.css"),getItem:function(_1){
if(_1 instanceof dojo.widget.ToolbarItem){
return _1;
}
for(var i=0;i<this.children.length;i++){
var _3=this.children[i];
if(_3 instanceof dojo.widget.Toolbar){
var _4=_3.getItem(_1);
if(_4){
return _4;
}
}
}
return null;
},getItems:function(){
var _5=[];
for(var i=0;i<this.children.length;i++){
var _7=this.children[i];
if(_7 instanceof dojo.widget.Toolbar){
_5=_5.concat(_7.getItems());
}
}
return _5;
},enable:function(){
for(var i=0;i<this.children.length;i++){
var _9=this.children[i];
if(_9 instanceof dojo.widget.Toolbar){
_9.enable.apply(_9,arguments);
}
}
},disable:function(){
for(var i=0;i<this.children.length;i++){
var _b=this.children[i];
if(_b instanceof dojo.widget.Toolbar){
_b.disable.apply(_b,arguments);
}
}
},select:function(_c){
for(var i=0;i<this.children.length;i++){
var _e=this.children[i];
if(_e instanceof dojo.widget.Toolbar){
_e.select(arguments);
}
}
},deselect:function(_f){
for(var i=0;i<this.children.length;i++){
var _11=this.children[i];
if(_11 instanceof dojo.widget.Toolbar){
_11.deselect(arguments);
}
}
},getItemsState:function(){
var _12={};
for(var i=0;i<this.children.length;i++){
var _14=this.children[i];
if(_14 instanceof dojo.widget.Toolbar){
dojo.lang.mixin(_12,_14.getItemsState());
}
}
return _12;
},getItemsActiveState:function(){
var _15={};
for(var i=0;i<this.children.length;i++){
var _17=this.children[i];
if(_17 instanceof dojo.widget.Toolbar){
dojo.lang.mixin(_15,_17.getItemsActiveState());
}
}
return _15;
},getItemsSelectedState:function(){
var _18={};
for(var i=0;i<this.children.length;i++){
var _1a=this.children[i];
if(_1a instanceof dojo.widget.Toolbar){
dojo.lang.mixin(_18,_1a.getItemsSelectedState());
}
}
return _18;
}});
dojo.widget.defineWidget("dojo.widget.Toolbar",dojo.widget.HtmlWidget,{isContainer:true,templateString:"<div class=\"toolbar\" dojoAttachPoint=\"containerNode\" unselectable=\"on\" dojoOnMouseover=\"_onmouseover\" dojoOnMouseout=\"_onmouseout\" dojoOnClick=\"_onclick\" dojoOnMousedown=\"_onmousedown\" dojoOnMouseup=\"_onmouseup\"></div>",_getItem:function(_1b){
var _1c=new Date();
var _1d=null;
while(_1b&&_1b!=this.domNode){
if(dojo.html.hasClass(_1b,"toolbarItem")){
var _1e=dojo.widget.manager.getWidgetsByFilter(function(w){
return w.domNode==_1b;
});
if(_1e.length==1){
_1d=_1e[0];
break;
}else{
if(_1e.length>1){
dojo.raise("Toolbar._getItem: More than one widget matches the node");
}
}
}
_1b=_1b.parentNode;
}
return _1d;
},_onmouseover:function(e){
var _21=this._getItem(e.target);
if(_21&&_21._onmouseover){
_21._onmouseover(e);
}
},_onmouseout:function(e){
var _23=this._getItem(e.target);
if(_23&&_23._onmouseout){
_23._onmouseout(e);
}
},_onclick:function(e){
var _25=this._getItem(e.target);
if(_25&&_25._onclick){
_25._onclick(e);
}
},_onmousedown:function(e){
var _27=this._getItem(e.target);
if(_27&&_27._onmousedown){
_27._onmousedown(e);
}
},_onmouseup:function(e){
var _29=this._getItem(e.target);
if(_29&&_29._onmouseup){
_29._onmouseup(e);
}
},addChild:function(_2a,pos,_2c){
var _2d=dojo.widget.ToolbarItem.make(_2a,null,_2c);
var ret=dojo.widget.Toolbar.superclass.addChild.call(this,_2d,null,pos,null);
return ret;
},push:function(){
for(var i=0;i<arguments.length;i++){
this.addChild(arguments[i]);
}
},getItem:function(_30){
if(_30 instanceof dojo.widget.ToolbarItem){
return _30;
}
for(var i=0;i<this.children.length;i++){
var _32=this.children[i];
if(_32 instanceof dojo.widget.ToolbarItem&&_32._name==_30){
return _32;
}
}
return null;
},getItems:function(){
var _33=[];
for(var i=0;i<this.children.length;i++){
var _35=this.children[i];
if(_35 instanceof dojo.widget.ToolbarItem){
_33.push(_35);
}
}
return _33;
},getItemsState:function(){
var _36={};
for(var i=0;i<this.children.length;i++){
var _38=this.children[i];
if(_38 instanceof dojo.widget.ToolbarItem){
_36[_38._name]={selected:_38._selected,enabled:!_38.disabled};
}
}
return _36;
},getItemsActiveState:function(){
var _39=this.getItemsState();
for(var _3a in _39){
_39[_3a]=_39[_3a].enabled;
}
return _39;
},getItemsSelectedState:function(){
var _3b=this.getItemsState();
for(var _3c in _3b){
_3b[_3c]=_3b[_3c].selected;
}
return _3b;
},enable:function(){
var _3d=arguments.length?arguments:this.children;
for(var i=0;i<_3d.length;i++){
var _3f=this.getItem(_3d[i]);
if(_3f instanceof dojo.widget.ToolbarItem){
_3f.enable(false,true);
}
}
},disable:function(){
var _40=arguments.length?arguments:this.children;
for(var i=0;i<_40.length;i++){
var _42=this.getItem(_40[i]);
if(_42 instanceof dojo.widget.ToolbarItem){
_42.disable();
}
}
},select:function(){
for(var i=0;i<arguments.length;i++){
var _44=arguments[i];
var _45=this.getItem(_44);
if(_45){
_45.select();
}
}
},deselect:function(){
for(var i=0;i<arguments.length;i++){
var _47=arguments[i];
var _48=this.getItem(_47);
if(_48){
_48.disable();
}
}
},setValue:function(){
for(var i=0;i<arguments.length;i+=2){
var _4a=arguments[i],_4b=arguments[i+1];
var _4c=this.getItem(_4a);
if(_4c){
if(_4c instanceof dojo.widget.ToolbarItem){
_4c.setValue(_4b);
}
}
}
}});
dojo.widget.defineWidget("dojo.widget.ToolbarItem",dojo.widget.HtmlWidget,{templateString:"<span unselectable=\"on\" class=\"toolbarItem\"></span>",_name:null,getName:function(){
return this._name;
},setName:function(_4d){
return (this._name=_4d);
},getValue:function(){
return this.getName();
},setValue:function(_4e){
return this.setName(_4e);
},_selected:false,isSelected:function(){
return this._selected;
},setSelected:function(is,_50,_51){
if(!this._toggleItem&&!_50){
return;
}
is=Boolean(is);
if(_50||!this.disabled&&this._selected!=is){
this._selected=is;
this.update();
if(!_51){
this._fireEvent(is?"onSelect":"onDeselect");
this._fireEvent("onChangeSelect");
}
}
},select:function(_52,_53){
return this.setSelected(true,_52,_53);
},deselect:function(_54,_55){
return this.setSelected(false,_54,_55);
},_toggleItem:false,isToggleItem:function(){
return this._toggleItem;
},setToggleItem:function(_56){
this._toggleItem=Boolean(_56);
},toggleSelected:function(_57){
return this.setSelected(!this._selected,_57);
},isEnabled:function(){
return !this.disabled;
},setEnabled:function(is,_59,_5a){
is=Boolean(is);
if(_59||this.disabled==is){
this.disabled=!is;
this.update();
if(!_5a){
this._fireEvent(this.disabled?"onDisable":"onEnable");
this._fireEvent("onChangeEnabled");
}
}
return !this.disabled;
},enable:function(_5b,_5c){
return this.setEnabled(true,_5b,_5c);
},disable:function(_5d,_5e){
return this.setEnabled(false,_5d,_5e);
},toggleEnabled:function(_5f,_60){
return this.setEnabled(this.disabled,_5f,_60);
},_icon:null,getIcon:function(){
return this._icon;
},setIcon:function(_61){
var _62=dojo.widget.Icon.make(_61);
if(this._icon){
this._icon.setIcon(_62);
}else{
this._icon=_62;
}
var _63=this._icon.getNode();
if(_63.parentNode!=this.domNode){
if(this.domNode.hasChildNodes()){
this.domNode.insertBefore(_63,this.domNode.firstChild);
}else{
this.domNode.appendChild(_63);
}
}
return this._icon;
},_label:"",getLabel:function(){
return this._label;
},setLabel:function(_64){
var ret=(this._label=_64);
if(!this.labelNode){
this.labelNode=document.createElement("span");
this.domNode.appendChild(this.labelNode);
}
this.labelNode.innerHTML="";
this.labelNode.appendChild(document.createTextNode(this._label));
this.update();
return ret;
},update:function(){
if(this.disabled){
this._selected=false;
dojo.html.addClass(this.domNode,"disabled");
dojo.html.removeClass(this.domNode,"down");
dojo.html.removeClass(this.domNode,"hover");
}else{
dojo.html.removeClass(this.domNode,"disabled");
if(this._selected){
dojo.html.addClass(this.domNode,"selected");
}else{
dojo.html.removeClass(this.domNode,"selected");
}
}
this._updateIcon();
},_updateIcon:function(){
if(this._icon){
if(this.disabled){
this._icon.disable();
}else{
if(this._cssHover){
this._icon.hover();
}else{
if(this._selected){
this._icon.select();
}else{
this._icon.enable();
}
}
}
}
},_fireEvent:function(evt){
if(typeof this[evt]=="function"){
var _67=[this];
for(var i=1;i<arguments.length;i++){
_67.push(arguments[i]);
}
this[evt].apply(this,_67);
}
},_onmouseover:function(e){
if(this.disabled){
return;
}
dojo.html.addClass(this.domNode,"hover");
this._fireEvent("onMouseOver");
},_onmouseout:function(e){
dojo.html.removeClass(this.domNode,"hover");
dojo.html.removeClass(this.domNode,"down");
if(!this._selected){
dojo.html.removeClass(this.domNode,"selected");
}
this._fireEvent("onMouseOut");
},_onclick:function(e){
if(!this.disabled&&!this._toggleItem){
this._fireEvent("onClick");
}
},_onmousedown:function(e){
if(e.preventDefault){
e.preventDefault();
}
if(this.disabled){
return;
}
dojo.html.addClass(this.domNode,"down");
if(this._toggleItem){
if(this.parent.preventDeselect&&this._selected){
return;
}
this.toggleSelected();
}
this._fireEvent("onMouseDown");
},_onmouseup:function(e){
dojo.html.removeClass(this.domNode,"down");
this._fireEvent("onMouseUp");
},onClick:function(){
},onMouseOver:function(){
},onMouseOut:function(){
},onMouseDown:function(){
},onMouseUp:function(){
},fillInTemplate:function(_6e,_6f){
if(_6e.name){
this._name=_6e.name;
}
if(_6e.selected){
this.select();
}
if(_6e.disabled){
this.disable();
}
if(_6e.label){
this.setLabel(_6e.label);
}
if(_6e.icon){
this.setIcon(_6e.icon);
}
if(_6e.toggleitem||_6e.toggleItem){
this.setToggleItem(true);
}
}});
dojo.widget.ToolbarItem.make=function(wh,_71,_72){
var _73=null;
if(wh instanceof Array){
_73=dojo.widget.createWidget("ToolbarButtonGroup",_72);
_73.setName(wh[0]);
for(var i=1;i<wh.length;i++){
_73.addChild(wh[i]);
}
}else{
if(wh instanceof dojo.widget.ToolbarItem){
_73=wh;
}else{
if(wh instanceof dojo.uri.Uri){
_73=dojo.widget.createWidget("ToolbarButton",dojo.lang.mixin(_72||{},{icon:new dojo.widget.Icon(wh.toString())}));
}else{
if(_71){
_73=dojo.widget.createWidget(wh,_72);
}else{
if(typeof wh=="string"||wh instanceof String){
switch(wh.charAt(0)){
case "|":
case "-":
case "/":
_73=dojo.widget.createWidget("ToolbarSeparator",_72);
break;
case " ":
if(wh.length==1){
_73=dojo.widget.createWidget("ToolbarSpace",_72);
}else{
_73=dojo.widget.createWidget("ToolbarFlexibleSpace",_72);
}
break;
default:
if(/\.(gif|jpg|jpeg|png)$/i.test(wh)){
_73=dojo.widget.createWidget("ToolbarButton",dojo.lang.mixin(_72||{},{icon:new dojo.widget.Icon(wh.toString())}));
}else{
_73=dojo.widget.createWidget("ToolbarButton",dojo.lang.mixin(_72||{},{label:wh.toString()}));
}
}
}else{
if(wh&&wh.tagName&&/^img$/i.test(wh.tagName)){
_73=dojo.widget.createWidget("ToolbarButton",dojo.lang.mixin(_72||{},{icon:wh}));
}else{
_73=dojo.widget.createWidget("ToolbarButton",dojo.lang.mixin(_72||{},{label:wh.toString()}));
}
}
}
}
}
}
return _73;
};
dojo.widget.defineWidget("dojo.widget.ToolbarButtonGroup",dojo.widget.ToolbarItem,{isContainer:true,templateString:"<span unselectable=\"on\" class=\"toolbarButtonGroup\" dojoAttachPoint=\"containerNode\"></span>",defaultButton:"",postCreate:function(){
for(var i=0;i<this.children.length;i++){
this._injectChild(this.children[i]);
}
},addChild:function(_76,pos,_78){
var _79=dojo.widget.ToolbarItem.make(_76,null,dojo.lang.mixin(_78||{},{toggleItem:true}));
var ret=dojo.widget.ToolbarButtonGroup.superclass.addChild.call(this,_79,null,pos,null);
this._injectChild(_79);
return ret;
},_injectChild:function(_7b){
dojo.event.connect(_7b,"onSelect",this,"onChildSelected");
dojo.event.connect(_7b,"onDeselect",this,"onChildDeSelected");
if(_7b._name==this.defaultButton||(typeof this.defaultButton=="number"&&this.children.length-1==this.defaultButton)){
_7b.select(false,true);
}
},getItem:function(_7c){
if(_7c instanceof dojo.widget.ToolbarItem){
return _7c;
}
for(var i=0;i<this.children.length;i++){
var _7e=this.children[i];
if(_7e instanceof dojo.widget.ToolbarItem&&_7e._name==_7c){
return _7e;
}
}
return null;
},getItems:function(){
var _7f=[];
for(var i=0;i<this.children.length;i++){
var _81=this.children[i];
if(_81 instanceof dojo.widget.ToolbarItem){
_7f.push(_81);
}
}
return _7f;
},onChildSelected:function(e){
this.select(e._name);
},onChildDeSelected:function(e){
this._fireEvent("onChangeSelect",this._value);
},enable:function(_84,_85){
for(var i=0;i<this.children.length;i++){
var _87=this.children[i];
if(_87 instanceof dojo.widget.ToolbarItem){
_87.enable(_84,_85);
if(_87._name==this._value){
_87.select(_84,_85);
}
}
}
},disable:function(_88,_89){
for(var i=0;i<this.children.length;i++){
var _8b=this.children[i];
if(_8b instanceof dojo.widget.ToolbarItem){
_8b.disable(_88,_89);
}
}
},_value:"",getValue:function(){
return this._value;
},select:function(_8c,_8d,_8e){
for(var i=0;i<this.children.length;i++){
var _90=this.children[i];
if(_90 instanceof dojo.widget.ToolbarItem){
if(_90._name==_8c){
_90.select(_8d,_8e);
this._value=_8c;
}else{
_90.deselect(true,true);
}
}
}
if(!_8e){
this._fireEvent("onSelect",this._value);
this._fireEvent("onChangeSelect",this._value);
}
},setValue:this.select,preventDeselect:false});
dojo.widget.defineWidget("dojo.widget.ToolbarButton",dojo.widget.ToolbarItem,{fillInTemplate:function(_91,_92){
dojo.widget.ToolbarButton.superclass.fillInTemplate.call(this,_91,_92);
dojo.html.addClass(this.domNode,"toolbarButton");
if(this._icon){
this.setIcon(this._icon);
}
if(this._label){
this.setLabel(this._label);
}
if(!this._name){
if(this._label){
this.setName(this._label);
}else{
if(this._icon){
var src=this._icon.getSrc("enabled").match(/[\/^]([^\.\/]+)\.(gif|jpg|jpeg|png)$/i);
if(src){
this.setName(src[1]);
}
}else{
this._name=this._widgetId;
}
}
}
}});
dojo.widget.defineWidget("dojo.widget.ToolbarDialog",dojo.widget.ToolbarButton,{fillInTemplate:function(_94,_95){
dojo.widget.ToolbarDialog.superclass.fillInTemplate.call(this,_94,_95);
dojo.event.connect(this,"onSelect",this,"showDialog");
dojo.event.connect(this,"onDeselect",this,"hideDialog");
},showDialog:function(e){
dojo.lang.setTimeout(dojo.event.connect,1,document,"onmousedown",this,"deselect");
},hideDialog:function(e){
dojo.event.disconnect(document,"onmousedown",this,"deselect");
}});
dojo.widget.defineWidget("dojo.widget.ToolbarMenu",dojo.widget.ToolbarDialog,{});
dojo.widget.ToolbarMenuItem=function(){
};
dojo.widget.defineWidget("dojo.widget.ToolbarSeparator",dojo.widget.ToolbarItem,{templateString:"<span unselectable=\"on\" class=\"toolbarItem toolbarSeparator\"></span>",defaultIconPath:new dojo.uri.moduleUri("dojo.widget","templates/buttons/sep.gif"),fillInTemplate:function(_98,_99,_9a){
dojo.widget.ToolbarSeparator.superclass.fillInTemplate.call(this,_98,_99);
this._name=this.widgetId;
if(!_9a){
if(!this._icon){
this.setIcon(this.defaultIconPath);
}
this.domNode.appendChild(this._icon.getNode());
}
},_onmouseover:null,_onmouseout:null,_onclick:null,_onmousedown:null,_onmouseup:null});
dojo.widget.defineWidget("dojo.widget.ToolbarSpace",dojo.widget.ToolbarSeparator,{fillInTemplate:function(_9b,_9c,_9d){
dojo.widget.ToolbarSpace.superclass.fillInTemplate.call(this,_9b,_9c,true);
if(!_9d){
dojo.html.addClass(this.domNode,"toolbarSpace");
}
}});
dojo.widget.defineWidget("dojo.widget.ToolbarSelect",dojo.widget.ToolbarItem,{templateString:"<span class=\"toolbarItem toolbarSelect\" unselectable=\"on\"><select dojoAttachPoint=\"selectBox\" dojoOnChange=\"changed\"></select></span>",fillInTemplate:function(_9e,_9f){
dojo.widget.ToolbarSelect.superclass.fillInTemplate.call(this,_9e,_9f,true);
var _a0=_9e.values;
var i=0;
for(var val in _a0){
var opt=document.createElement("option");
opt.setAttribute("value",_a0[val]);
opt.innerHTML=val;
this.selectBox.appendChild(opt);
}
},changed:function(e){
this._fireEvent("onSetValue",this.selectBox.value);
},setEnabled:function(is,_a6,_a7){
var ret=dojo.widget.ToolbarSelect.superclass.setEnabled.call(this,is,_a6,_a7);
this.selectBox.disabled=this.disabled;
return ret;
},_onmouseover:null,_onmouseout:null,_onclick:null,_onmousedown:null,_onmouseup:null});
dojo.widget.Icon=function(_a9,_aa,_ab,_ac){
if(!arguments.length){
throw new Error("Icon must have at least an enabled state");
}
var _ad=["enabled","disabled","hovered","selected"];
var _ae="enabled";
var _af=document.createElement("img");
this.getState=function(){
return _ae;
};
this.setState=function(_b0){
if(dojo.lang.inArray(_ad,_b0)){
if(this[_b0]){
_ae=_b0;
var img=this[_ae];
if((dojo.render.html.ie55||dojo.render.html.ie60)&&img.src&&img.src.match(/[.]png$/i)){
_af.width=img.width||img.offsetWidth;
_af.height=img.height||img.offsetHeight;
_af.setAttribute("src",dojo.uri.moduleUri("dojo.widget","templates/images/blank.gif").uri);
_af.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+img.src+"',sizingMethod='image')";
}else{
_af.setAttribute("src",img.src);
}
}
}else{
throw new Error("Invalid state set on Icon (state: "+_b0+")");
}
};
this.setSrc=function(_b2,_b3){
if(/^img$/i.test(_b3.tagName)){
this[_b2]=_b3;
}else{
if(typeof _b3=="string"||_b3 instanceof String||_b3 instanceof dojo.uri.Uri){
this[_b2]=new Image();
this[_b2].src=_b3.toString();
}
}
return this[_b2];
};
this.setIcon=function(_b4){
for(var i=0;i<_ad.length;i++){
if(_b4[_ad[i]]){
this.setSrc(_ad[i],_b4[_ad[i]]);
}
}
this.update();
};
this.enable=function(){
this.setState("enabled");
};
this.disable=function(){
this.setState("disabled");
};
this.hover=function(){
this.setState("hovered");
};
this.select=function(){
this.setState("selected");
};
this.getSize=function(){
return {width:_af.width||_af.offsetWidth,height:_af.height||_af.offsetHeight};
};
this.setSize=function(w,h){
_af.width=w;
_af.height=h;
return {width:w,height:h};
};
this.getNode=function(){
return _af;
};
this.getSrc=function(_b8){
if(_b8){
return this[_b8].src;
}
return _af.src||"";
};
this.update=function(){
this.setState(_ae);
};
for(var i=0;i<_ad.length;i++){
var arg=arguments[i];
var _bb=_ad[i];
this[_bb]=null;
if(!arg){
continue;
}
this.setSrc(_bb,arg);
}
this.enable();
};
dojo.widget.Icon.make=function(a,b,c,d){
for(var i=0;i<arguments.length;i++){
if(arguments[i] instanceof dojo.widget.Icon){
return arguments[i];
}
}
return new dojo.widget.Icon(a,b,c,d);
};
dojo.widget.defineWidget("dojo.widget.ToolbarColorDialog",dojo.widget.ToolbarDialog,{palette:"7x10",fillInTemplate:function(_c1,_c2){
dojo.widget.ToolbarColorDialog.superclass.fillInTemplate.call(this,_c1,_c2);
this.dialog=dojo.widget.createWidget("ColorPalette",{palette:this.palette});
this.dialog.domNode.style.position="absolute";
dojo.event.connect(this.dialog,"onColorSelect",this,"_setValue");
},_setValue:function(_c3){
this._value=_c3;
this._fireEvent("onSetValue",_c3);
},showDialog:function(e){
dojo.widget.ToolbarColorDialog.superclass.showDialog.call(this,e);
var abs=dojo.html.getAbsolutePosition(this.domNode,true);
var y=abs.y+dojo.html.getBorderBox(this.domNode).height;
this.dialog.showAt(abs.x,y);
},hideDialog:function(e){
dojo.widget.ToolbarColorDialog.superclass.hideDialog.call(this,e);
this.dialog.hide();
}});
