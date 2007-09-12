dojo.provide("dojo.widget.Menu2");
dojo.require("dojo.widget.PopupContainer");
dojo.declare("dojo.widget.MenuBase",null,function(){
this.eventNames={open:""};
},{isContainer:true,isMenu:true,eventNaming:"default",templateCssString:"\n.dojoPopupMenu2 {\n\tposition: absolute;\n\tborder: 1px solid #7298d0;\n\tbackground:#85aeec url(images/soriaMenuBg.gif) repeat-x bottom left !important;\n\tpadding: 1px;\n\tmargin-top: 1px;\n\tmargin-bottom: 1px;\n}\n\n.dojoMenuItem2{\n\twhite-space: nowrap;\n\tfont: menu;\n\tmargin: 0;\n}\n\n.dojoMenuItem2Hover {\n\tbackground-color: #D2E4FD;\n\tcursor:pointer;\n\tcursor:hand;\n}\n\n.dojoMenuItem2Icon {\n\tposition: relative;\n\tbackground-position: center center;\n\tbackground-repeat: no-repeat;\n\twidth: 16px;\n\theight: 16px;\n\tpadding-right: 3px;\n}\n\n.dojoMenuItem2Label {\n\tposition: relative;\n\tvertical-align: middle;\n}\n\n/* main label text */\n.dojoMenuItem2Label {\n\tposition: relative;\n\tvertical-align: middle;\n}\n\n.dojoMenuItem2Accel {\n\tposition: relative;\n\tvertical-align: middle;\n\tpadding-left: 3px;\n}\n\n.dojoMenuItem2Disabled .dojoMenuItem2Label,\n.dojoMenuItem2Disabled .dojoMenuItem2Accel {\n\tcolor: #607a9e;\n}\n\n.dojoMenuItem2Submenu {\n\tposition: relative;\n\tbackground-position: center center;\n\tbackground-repeat: no-repeat;\n\tbackground-image: url(images/submenu_off.gif);\n\twidth: 5px;\n\theight: 9px;\n\tpadding-left: 3px;\n}\n.dojoMenuItem2Hover .dojoMenuItem2Submenu {\n\tbackground-image: url(images/submenu_on.gif);\n}\n\n.dojoMenuItem2Disabled .dojoMenuItem2Submenu {\n\tbackground-image: url(images/submenu_disabled.gif);\n}\n\n.dojoMenuSeparator2 {\n\tfont-size: 1px;\n\tmargin: 0;\n}\n\n.dojoMenuSeparator2Top {\n\theight: 50%;\n\tborder-bottom: 1px solid #7a98c4;\n\tmargin: 0px 2px;\n\tfont-size: 1px;\n}\n\n.dojoMenuSeparator2Bottom {\n\theight: 50%;\n\tborder-top: 1px solid #c9deff;\n\tmargin: 0px 2px;\n\tfont-size: 1px;\n}\n\n.dojoMenuBar2 {\n\tbackground:#85aeec url(images/soriaBarBg.gif) repeat-x top left;\n\t/*border-bottom:1px solid #6b9fec;*/\n\tpadding: 1px;\n}\n\n.dojoMenuBar2 .dojoMenuItem2 {\n\twhite-space: nowrap;\n\tfont: menu;\n\tmargin: 0;\n\tposition: relative;\n\tvertical-align: middle;\n\tz-index: 1;\n\tpadding: 3px 8px;\n\tdisplay: inline;/* needed in khtml to display correctly */\n\tdisplay: -moz-inline-box;/* needed in firefox */\n\tcursor:pointer;\n\tcursor:hand;\n}\n\n.dojoMenuBar2 .dojoMenuItem2Hover {\n\tbackground-color:#d2e4fd;\n}\n\n.dojoMenuBar2 .dojoMenuItem2Disabled span {\n\tcolor: #4f6582;\n}\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/Menu2.css"),submenuDelay:500,initialize:function(_1,_2){
if(this.eventNaming=="default"){
for(var _3 in this.eventNames){
this.eventNames[_3]=this.widgetId+"/"+_3;
}
}
},_moveToNext:function(_4){
this._highlightOption(1);
return true;
},_moveToPrevious:function(_5){
this._highlightOption(-1);
return true;
},_moveToParentMenu:function(_6){
if(this._highlighted_option&&this.parentMenu){
if(_6._menu2UpKeyProcessed){
return true;
}else{
this._highlighted_option.onUnhover();
this.closeSubmenu();
_6._menu2UpKeyProcessed=true;
}
}
return false;
},_moveToChildMenu:function(_7){
if(this._highlighted_option&&this._highlighted_option.submenuId){
this._highlighted_option._onClick(true);
return true;
}
return false;
},_selectCurrentItem:function(_8){
if(this._highlighted_option){
this._highlighted_option._onClick();
return true;
}
return false;
},processKey:function(_9){
if(_9.ctrlKey||_9.altKey||!_9.key){
return false;
}
var _a=false;
switch(_9.key){
case _9.KEY_DOWN_ARROW:
_a=this._moveToNext(_9);
break;
case _9.KEY_UP_ARROW:
_a=this._moveToPrevious(_9);
break;
case _9.KEY_RIGHT_ARROW:
_a=this._moveToChildMenu(_9);
break;
case _9.KEY_LEFT_ARROW:
_a=this._moveToParentMenu(_9);
break;
case " ":
case _9.KEY_ENTER:
if(_a=this._selectCurrentItem(_9)){
break;
}
case _9.KEY_ESCAPE:
case _9.KEY_TAB:
this.close(true);
_a=true;
break;
}
return _a;
},_findValidItem:function(_b,_c){
if(_c){
_c=_b>0?_c.getNextSibling():_c.getPreviousSibling();
}
for(var i=0;i<this.children.length;++i){
if(!_c){
_c=_b>0?this.children[0]:this.children[this.children.length-1];
}
if(_c.onHover&&_c.isShowing()){
return _c;
}
_c=_b>0?_c.getNextSibling():_c.getPreviousSibling();
}
},_highlightOption:function(_e){
var _f;
if((!this._highlighted_option)){
_f=this._findValidItem(_e);
}else{
_f=this._findValidItem(_e,this._highlighted_option);
}
if(_f){
if(this._highlighted_option){
this._highlighted_option.onUnhover();
}
_f.onHover();
dojo.html.scrollIntoView(_f.domNode);
try{
var _10=dojo.html.getElementsByClass("dojoMenuItem2Label",_f.domNode)[0];
_10.focus();
}
catch(e){
}
}
},onItemClick:function(_11){
},closeSubmenu:function(_12){
if(this.currentSubmenu==null){
return;
}
this.currentSubmenu.close(_12);
this.currentSubmenu=null;
this.currentSubmenuTrigger.is_open=false;
this.currentSubmenuTrigger._closedSubmenu(_12);
this.currentSubmenuTrigger=null;
}});
dojo.widget.defineWidget("dojo.widget.PopupMenu2",[dojo.widget.HtmlWidget,dojo.widget.PopupContainerBase,dojo.widget.MenuBase],function(){
this.targetNodeIds=[];
},{templateString:"<table class=\"dojoPopupMenu2\" border=0 cellspacing=0 cellpadding=0 style=\"display: none; position: absolute;\">"+"<tbody dojoAttachPoint=\"containerNode\"></tbody>"+"</table>",submenuOverlap:5,contextMenuForWindow:false,parentMenu:null,postCreate:function(){
if(this.contextMenuForWindow){
var doc=dojo.body();
this.bindDomNode(doc);
}else{
if(this.targetNodeIds.length>0){
dojo.lang.forEach(this.targetNodeIds,this.bindDomNode,this);
}
}
this._subscribeSubitemsOnOpen();
},_subscribeSubitemsOnOpen:function(){
var _14=this.getChildrenOfType(dojo.widget.MenuItem2);
for(var i=0;i<_14.length;i++){
dojo.event.topic.subscribe(this.eventNames.open,_14[i],"menuOpen");
}
},getTopOpenEvent:function(){
var _16=this;
while(_16.parentMenu){
_16=_16.parentMenu;
}
return _16.openEvent;
},bindDomNode:function(_17){
_17=dojo.byId(_17);
var win=dojo.html.getElementWindow(_17);
if(dojo.html.isTag(_17,"iframe")=="iframe"){
win=dojo.html.iframeContentWindow(_17);
_17=dojo.withGlobal(win,dojo.body);
}
dojo.widget.Menu2.OperaAndKonqFixer.fixNode(_17);
dojo.event.kwConnect({srcObj:_17,srcFunc:"oncontextmenu",targetObj:this,targetFunc:"onOpen",once:true});
if(dojo.render.html.moz&&win.document.designMode.toLowerCase()=="on"){
dojo.event.browser.addListener(_17,"contextmenu",dojo.lang.hitch(this,"onOpen"));
}
dojo.widget.PopupManager.registerWin(win);
},unBindDomNode:function(_19){
var _1a=dojo.byId(_19);
dojo.event.kwDisconnect({srcObj:_1a,srcFunc:"oncontextmenu",targetObj:this,targetFunc:"onOpen",once:true});
dojo.widget.Menu2.OperaAndKonqFixer.cleanNode(_1a);
},_openAsSubmenu:function(_1b,_1c,_1d){
if(this.isShowingNow){
return;
}
this.parentMenu=_1b;
this.open(_1c,_1b,_1c,_1d);
},close:function(_1e){
if(this.animationInProgress){
dojo.widget.PopupContainerBase.prototype.close.call(this,_1e);
return;
}
if(this._highlighted_option){
this._highlighted_option.onUnhover();
}
dojo.widget.PopupContainerBase.prototype.close.call(this,_1e);
this.parentMenu=null;
},closeAll:function(_1f){
if(this.parentMenu){
this.parentMenu.closeAll(_1f);
}else{
this.close(_1f);
}
},_openSubmenu:function(_20,_21){
_20._openAsSubmenu(this,_21.arrow,{"TR":"TL","TL":"TR"});
this.currentSubmenu=_20;
this.currentSubmenuTrigger=_21;
this.currentSubmenuTrigger.is_open=true;
},focus:function(){
if(this.currentSubmenuTrigger){
if(this.currentSubmenuTrigger.caption){
try{
this.currentSubmenuTrigger.caption.focus();
}
catch(e){
}
}else{
try{
this.currentSubmenuTrigger.domNode.focus();
}
catch(e){
}
}
}
},onOpen:function(e){
this.openEvent=e;
if(e["target"]){
this.openedForWindow=dojo.html.getElementWindow(e.target);
}else{
this.openedForWindow=null;
}
var x=e.pageX,y=e.pageY;
var win=dojo.html.getElementWindow(e.target);
var _26=win._frameElement||win.frameElement;
if(_26){
var _27=dojo.html.abs(_26,true);
x+=_27.x-dojo.withGlobal(win,dojo.html.getScroll).left;
y+=_27.y-dojo.withGlobal(win,dojo.html.getScroll).top;
}
this.open(x,y,null,[x,y]);
dojo.event.browser.stopEvent(e);
}});
dojo.widget.defineWidget("dojo.widget.MenuItem2",dojo.widget.HtmlWidget,function(){
this.eventNames={engage:""};
},{templateString:"<tr class=\"dojoMenuItem2\" dojoAttachEvent=\"onMouseOver: onHover; onMouseOut: onUnhover; onClick: _onClick; onKey:onKey;\">"+"<td><div class=\"${this.iconClass}\" style=\"${this.iconStyle}\"></div></td>"+"<td tabIndex=\"-1\" class=\"dojoMenuItem2Label\" dojoAttachPoint=\"caption\">${this.caption}</td>"+"<td class=\"dojoMenuItem2Accel\">${this.accelKey}</td>"+"<td><div class=\"dojoMenuItem2Submenu\" style=\"display:${this.arrowDisplay};\" dojoAttachPoint=\"arrow\"></div></td>"+"</tr>",is_hovering:false,hover_timer:null,is_open:false,topPosition:0,caption:"Untitled",accelKey:"",iconSrc:"",disabledClass:"dojoMenuItem2Disabled",iconClass:"dojoMenuItem2Icon",submenuId:"",eventNaming:"default",highlightClass:"dojoMenuItem2Hover",postMixInProperties:function(){
this.iconStyle="";
if(this.iconSrc){
if((this.iconSrc.toLowerCase().substring(this.iconSrc.length-4)==".png")&&(dojo.render.html.ie55||dojo.render.html.ie60)){
this.iconStyle="filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+this.iconSrc+"', sizingMethod='image')";
}else{
this.iconStyle="background-image: url("+this.iconSrc+")";
}
}
this.arrowDisplay=this.submenuId?"block":"none";
dojo.widget.MenuItem2.superclass.postMixInProperties.apply(this,arguments);
},fillInTemplate:function(){
dojo.html.disableSelection(this.domNode);
if(this.disabled){
this.setDisabled(true);
}
if(this.eventNaming=="default"){
for(var _28 in this.eventNames){
this.eventNames[_28]=this.widgetId+"/"+_28;
}
}
},onHover:function(){
this.onUnhover();
if(this.is_hovering){
return;
}
if(this.is_open){
return;
}
if(this.parent._highlighted_option){
this.parent._highlighted_option.onUnhover();
}
this.parent.closeSubmenu();
this.parent._highlighted_option=this;
dojo.widget.PopupManager.setFocusedMenu(this.parent);
this._highlightItem();
if(this.is_hovering){
this._stopSubmenuTimer();
}
this.is_hovering=true;
this._startSubmenuTimer();
},onUnhover:function(){
if(!this.is_open){
this._unhighlightItem();
}
this.is_hovering=false;
this.parent._highlighted_option=null;
if(this.parent.parentMenu){
dojo.widget.PopupManager.setFocusedMenu(this.parent.parentMenu);
}
this._stopSubmenuTimer();
},_onClick:function(_29){
var _2a=false;
if(this.disabled){
return false;
}
if(this.submenuId){
if(!this.is_open){
this._stopSubmenuTimer();
this._openSubmenu();
}
_2a=true;
}else{
this.onUnhover();
this.parent.closeAll(true);
}
this.onClick();
dojo.event.topic.publish(this.eventNames.engage,this);
if(_2a&&_29){
dojo.widget.getWidgetById(this.submenuId)._highlightOption(1);
}
return;
},onClick:function(){
this.parent.onItemClick(this);
},_highlightItem:function(){
dojo.html.addClass(this.domNode,this.highlightClass);
},_unhighlightItem:function(){
dojo.html.removeClass(this.domNode,this.highlightClass);
},_startSubmenuTimer:function(){
this._stopSubmenuTimer();
if(this.disabled){
return;
}
var _2b=this;
var _2c=function(){
return function(){
_2b._openSubmenu();
};
}();
this.hover_timer=dojo.lang.setTimeout(_2c,this.parent.submenuDelay);
},_stopSubmenuTimer:function(){
if(this.hover_timer){
dojo.lang.clearTimeout(this.hover_timer);
this.hover_timer=null;
}
},_openSubmenu:function(){
if(this.disabled){
return;
}
this.parent.closeSubmenu();
var _2d=dojo.widget.getWidgetById(this.submenuId);
if(_2d){
this.parent._openSubmenu(_2d,this);
}
},_closedSubmenu:function(){
this.onUnhover();
},setDisabled:function(_2e){
this.disabled=_2e;
if(this.disabled){
dojo.html.addClass(this.domNode,this.disabledClass);
}else{
dojo.html.removeClass(this.domNode,this.disabledClass);
}
},enable:function(){
this.setDisabled(false);
},disable:function(){
this.setDisabled(true);
},menuOpen:function(_2f){
}});
dojo.widget.defineWidget("dojo.widget.MenuSeparator2",dojo.widget.HtmlWidget,{templateString:"<tr class=\"dojoMenuSeparator2\"><td colspan=4>"+"<div class=\"dojoMenuSeparator2Top\"></div>"+"<div class=\"dojoMenuSeparator2Bottom\"></div>"+"</td></tr>",postCreate:function(){
dojo.html.disableSelection(this.domNode);
}});
dojo.widget.defineWidget("dojo.widget.MenuBar2",[dojo.widget.HtmlWidget,dojo.widget.MenuBase],{menuOverlap:2,templateString:"<div class=\"dojoMenuBar2\" dojoAttachPoint=\"containerNode\" tabIndex=\"0\"></div>",close:function(_30){
if(this._highlighted_option){
this._highlighted_option.onUnhover();
}
this.closeSubmenu(_30);
},closeAll:function(_31){
this.close(_31);
},processKey:function(evt){
if(evt.ctrlKey||evt.altKey){
return false;
}
var _33=false;
switch(evt.key){
case evt.KEY_DOWN_ARROW:
_33=this._moveToChildMenu(evt);
break;
case evt.KEY_UP_ARROW:
_33=this._moveToParentMenu(evt);
break;
case evt.KEY_RIGHT_ARROW:
_33=this._moveToNext(evt);
break;
case evt.KEY_LEFT_ARROW:
_33=this._moveToPrevious(evt);
break;
default:
_33=dojo.widget.MenuBar2.superclass.processKey.apply(this,arguments);
break;
}
return _33;
},postCreate:function(){
dojo.widget.MenuBar2.superclass.postCreate.apply(this,arguments);
this.isShowingNow=true;
},_openSubmenu:function(_34,_35){
_34._openAsSubmenu(this,_35.domNode,{"BL":"TL","TL":"BL"});
this.currentSubmenu=_34;
this.currentSubmenuTrigger=_35;
this.currentSubmenuTrigger.is_open=true;
}});
dojo.widget.defineWidget("dojo.widget.MenuBarItem2",dojo.widget.MenuItem2,{templateString:"<span class=\"dojoMenuItem2\" dojoAttachEvent=\"onMouseOver: onHover; onMouseOut: onUnhover; onClick: _onClick;\">${this.caption}</span>"});
dojo.widget.Menu2.OperaAndKonqFixer=new function(){
var _36=true;
var _37=false;
if(!dojo.lang.isFunction(dojo.doc().oncontextmenu)){
dojo.doc().oncontextmenu=function(){
_36=false;
_37=true;
};
}
if(dojo.doc().createEvent){
try{
var e=dojo.doc().createEvent("MouseEvents");
e.initMouseEvent("contextmenu",1,1,dojo.global(),1,0,0,0,0,0,0,0,0,0,null);
dojo.doc().dispatchEvent(e);
}
catch(e){
}
}else{
_36=false;
}
if(_37){
delete dojo.doc().oncontextmenu;
}
this.fixNode=function(_39){
if(_36){
if(!dojo.lang.isFunction(_39.oncontextmenu)){
_39.oncontextmenu=function(e){
};
}
if(dojo.render.html.opera){
_39._menufixer_opera=function(e){
if(e.ctrlKey){
this.oncontextmenu(e);
}
};
dojo.event.connect(_39,"onclick",_39,"_menufixer_opera");
}else{
_39._menufixer_konq=function(e){
if(e.button==2){
e.preventDefault();
this.oncontextmenu(e);
}
};
dojo.event.connect(_39,"onmousedown",_39,"_menufixer_konq");
}
}
};
this.cleanNode=function(_3d){
if(_36){
if(_3d._menufixer_opera){
dojo.event.disconnect(_3d,"onclick",_3d,"_menufixer_opera");
delete _3d._menufixer_opera;
}else{
if(_3d._menufixer_konq){
dojo.event.disconnect(_3d,"onmousedown",_3d,"_menufixer_konq");
delete _3d._menufixer_konq;
}
}
if(_3d.oncontextmenu){
delete _3d.oncontextmenu;
}
}
};
};
