dojo.provide("dojo.widget.Editor2Plugin.AlwaysShowToolbar");
dojo.event.topic.subscribe("dojo.widget.Editor2::onLoad",function(_1){
if(_1.toolbarAlwaysVisible){
var p=new dojo.widget.Editor2Plugin.AlwaysShowToolbar(_1);
}
});
dojo.declare("dojo.widget.Editor2Plugin.AlwaysShowToolbar",null,function(_3){
this.editor=_3;
this.editor.registerLoadedPlugin(this);
this.setup();
},{_scrollSetUp:false,_fixEnabled:false,_scrollThreshold:false,_handleScroll:true,setup:function(){
var _4=this.editor.toolbarWidget;
if(!_4.tbBgIframe){
_4.tbBgIframe=new dojo.html.BackgroundIframe(_4.domNode);
_4.tbBgIframe.onResized();
}
this.scrollInterval=setInterval(dojo.lang.hitch(this,"globalOnScrollHandler"),100);
dojo.event.connect("before",this.editor.toolbarWidget,"destroy",this,"destroy");
},globalOnScrollHandler:function(){
var _5=dojo.render.html.ie;
if(!this._handleScroll){
return;
}
var dh=dojo.html;
var _7=this.editor.toolbarWidget.domNode;
var db=dojo.body();
if(!this._scrollSetUp){
this._scrollSetUp=true;
var _9=dh.getMarginBox(this.editor.domNode).width;
this._scrollThreshold=dh.abs(_7,true).y;
if((_5)&&(db)&&(dh.getStyle(db,"background-image")=="none")){
with(db.style){
backgroundImage="url("+dojo.uri.moduleUri("dojo.widget","templates/images/blank.gif")+")";
backgroundAttachment="fixed";
}
}
}
var _a=(window["pageYOffset"])?window["pageYOffset"]:(document["documentElement"]||document["body"]).scrollTop;
if(_a>this._scrollThreshold){
if(!this._fixEnabled){
var _b=dojo.html.getMarginBox(_7);
this.editor.editorObject.style.marginTop=_b.height+"px";
if(_5){
_7.style.left=dojo.html.abs(_7,dojo.html.boxSizing.MARGIN_BOX).x;
if(_7.previousSibling){
this._IEOriginalPos=["after",_7.previousSibling];
}else{
if(_7.nextSibling){
this._IEOriginalPos=["before",_7.nextSibling];
}else{
this._IEOriginalPos=["",_7.parentNode];
}
}
dojo.body().appendChild(_7);
dojo.html.addClass(_7,"IEFixedToolbar");
}else{
with(_7.style){
position="fixed";
top="0px";
}
}
_7.style.width=_b.width+"px";
_7.style.zIndex=1000;
this._fixEnabled=true;
}
if(!dojo.render.html.safari){
var _c=(this.height)?parseInt(this.editor.height):this.editor._lastHeight;
if(_a>(this._scrollThreshold+_c)){
_7.style.display="none";
}else{
_7.style.display="";
}
}
}else{
if(this._fixEnabled){
(this.editor.object||this.editor.iframe).style.marginTop=null;
with(_7.style){
position="";
top="";
zIndex="";
display="";
}
if(_5){
_7.style.left="";
dojo.html.removeClass(_7,"IEFixedToolbar");
if(this._IEOriginalPos){
dojo.html.insertAtPosition(_7,this._IEOriginalPos[1],this._IEOriginalPos[0]);
this._IEOriginalPos=null;
}else{
dojo.html.insertBefore(_7,this.editor.object||this.editor.iframe);
}
}
_7.style.width="";
this._fixEnabled=false;
}
}
},destroy:function(){
this._IEOriginalPos=null;
this._handleScroll=false;
clearInterval(this.scrollInterval);
this.editor.unregisterLoadedPlugin(this);
if(dojo.render.html.ie){
dojo.html.removeClass(this.editor.toolbarWidget.domNode,"IEFixedToolbar");
}
}});
