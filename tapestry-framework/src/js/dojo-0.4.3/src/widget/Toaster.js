dojo.provide("dojo.widget.Toaster");
dojo.require("dojo.widget.*");
dojo.require("dojo.lfx.*");
dojo.require("dojo.html.iframe");
dojo.widget.defineWidget("dojo.widget.Toaster",dojo.widget.HtmlWidget,{templateString:"<div dojoAttachPoint=\"clipNode\"><div dojoAttachPoint=\"containerNode\" dojoAttachEvent=\"onClick:onSelect\"><div dojoAttachPoint=\"contentNode\"></div></div></div>",templateCssString:".dojoToasterClip {\n\tposition: absolute;\n\toverflow: hidden;\n}\n\n.dojoToasterContainer {\n\tdisplay: block;\n\tposition: absolute;\n\twidth: 17.5em;\n\tz-index: 5000;\n\tmargin: 0px;\n\tfont:0.75em Tahoma, Helvetica, Verdana, Arial;\n}\n\n.dojoToasterContent{\n\tpadding:1em;\n\tpadding-top:0.25em;\n\tbackground:#73c74a;\n}\n\n.dojoToasterMessage{ \n\tcolor:#fff;\n}\n.dojoToasterWarning{ }\n.dojoToasterError,\n.dojoToasterFatal{\n\tfont-weight:bold;\n\tcolor:#fff;\n}\n\n\n.dojoToasterWarning .dojoToasterContent{\n\tpadding:1em;\n\tpadding-top:0.25em;\n\tbackground:#d4d943;\n} \n\n.dojoToasterError .dojoToasterContent{\n\tpadding:1em;\n\tpadding-top:0.25em;\n\tbackground:#c46600;\n} \n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/Toaster.css"),messageTopic:"",messageTypes:{MESSAGE:"MESSAGE",WARNING:"WARNING",ERROR:"ERROR",FATAL:"FATAL"},defaultType:"MESSAGE",clipCssClass:"dojoToasterClip",containerCssClass:"dojoToasterContainer",contentCssClass:"dojoToasterContent",messageCssClass:"dojoToasterMessage",warningCssClass:"dojoToasterWarning",errorCssClass:"dojoToasterError",fatalCssClass:"dojoToasterFatal",positionDirection:"br-up",positionDirectionTypes:["br-up","br-left","bl-up","bl-right","tr-down","tr-left","tl-down","tl-right"],showDelay:2000,postCreate:function(){
this.hide();
dojo.html.setClass(this.clipNode,this.clipCssClass);
dojo.html.addClass(this.containerNode,this.containerCssClass);
dojo.html.setClass(this.contentNode,this.contentCssClass);
if(this.messageTopic){
dojo.event.topic.subscribe(this.messageTopic,this,"_handleMessage");
}
if(!this.positionDirection||!dojo.lang.inArray(this.positionDirectionTypes,this.positionDirection)){
this.positionDirection=this.positionDirectionTypes.BRU;
}
},_handleMessage:function(_1){
if(dojo.lang.isString(_1)){
this.setContent(_1);
}else{
this.setContent(_1["message"],_1["type"],_1["delay"]);
}
},setContent:function(_2,_3,_4){
var _4=_4||this.showDelay;
if(this.slideAnim&&this.slideAnim.status()=="playing"){
dojo.lang.setTimeout(50,dojo.lang.hitch(this,function(){
this.setContent(_2,_3);
}));
return;
}else{
if(this.slideAnim){
this.slideAnim.stop();
if(this.fadeAnim){
this.fadeAnim.stop();
}
}
}
if(!_2){
dojo.debug(this.widgetId+".setContent() incoming content was null, ignoring.");
return;
}
if(!this.positionDirection||!dojo.lang.inArray(this.positionDirectionTypes,this.positionDirection)){
dojo.raise(this.widgetId+".positionDirection is an invalid value: "+this.positionDirection);
}
dojo.html.removeClass(this.containerNode,this.messageCssClass);
dojo.html.removeClass(this.containerNode,this.warningCssClass);
dojo.html.removeClass(this.containerNode,this.errorCssClass);
dojo.html.removeClass(this.containerNode,this.fatalCssClass);
dojo.html.clearOpacity(this.containerNode);
if(_2 instanceof String||typeof _2=="string"){
this.contentNode.innerHTML=_2;
}else{
if(dojo.html.isNode(_2)){
this.contentNode.innerHTML=dojo.html.getContentAsString(_2);
}else{
dojo.raise("Toaster.setContent(): msg is of unknown type:"+_2);
}
}
switch(_3){
case this.messageTypes.WARNING:
dojo.html.addClass(this.containerNode,this.warningCssClass);
break;
case this.messageTypes.ERROR:
dojo.html.addClass(this.containerNode,this.errorCssClass);
break;
case this.messageTypes.FATAL:
dojo.html.addClass(this.containerNode,this.fatalCssClass);
break;
case this.messageTypes.MESSAGE:
default:
dojo.html.addClass(this.containerNode,this.messageCssClass);
break;
}
this.show();
var _5=dojo.html.getMarginBox(this.containerNode);
if(this.positionDirection.indexOf("-up")>=0){
this.containerNode.style.left=0+"px";
this.containerNode.style.top=_5.height+10+"px";
}else{
if(this.positionDirection.indexOf("-left")>=0){
this.containerNode.style.left=_5.width+10+"px";
this.containerNode.style.top=0+"px";
}else{
if(this.positionDirection.indexOf("-right")>=0){
this.containerNode.style.left=0-_5.width-10+"px";
this.containerNode.style.top=0+"px";
}else{
if(this.positionDirection.indexOf("-down")>=0){
this.containerNode.style.left=0+"px";
this.containerNode.style.top=0-_5.height-10+"px";
}else{
dojo.raise(this.widgetId+".positionDirection is an invalid value: "+this.positionDirection);
}
}
}
}
this.slideAnim=dojo.lfx.html.slideTo(this.containerNode,{top:0,left:0},450,null,dojo.lang.hitch(this,function(_6,_7){
dojo.lang.setTimeout(dojo.lang.hitch(this,function(_8){
if(this.bgIframe){
this.bgIframe.hide();
}
this.fadeAnim=dojo.lfx.html.fadeOut(this.containerNode,1000,null,dojo.lang.hitch(this,function(_9){
this.hide();
})).play();
}),_4);
})).play();
},_placeClip:function(){
var _a=dojo.html.getScroll();
var _b=dojo.html.getViewport();
var _c=dojo.html.getMarginBox(this.containerNode);
this.clipNode.style.height=_c.height+"px";
this.clipNode.style.width=_c.width+"px";
if(this.positionDirection.match(/^t/)){
this.clipNode.style.top=_a.top+"px";
}else{
if(this.positionDirection.match(/^b/)){
this.clipNode.style.top=(_b.height-_c.height-2+_a.top)+"px";
}
}
if(this.positionDirection.match(/^[tb]r-/)){
this.clipNode.style.left=(_b.width-_c.width-1-_a.left)+"px";
}else{
if(this.positionDirection.match(/^[tb]l-/)){
this.clipNode.style.left=0+"px";
}
}
this.clipNode.style.clip="rect(0px, "+_c.width+"px, "+_c.height+"px, 0px)";
if(dojo.render.html.ie){
if(!this.bgIframe){
this.bgIframe=new dojo.html.BackgroundIframe(this.containerNode);
this.bgIframe.setZIndex(this.containerNode);
}
this.bgIframe.onResized();
this.bgIframe.show();
}
},onSelect:function(e){
},show:function(){
dojo.widget.Toaster.superclass.show.call(this);
this._placeClip();
if(!this._scrollConnected){
this._scrollConnected=true;
dojo.event.connect(window,"onscroll",this,"_placeClip");
}
},hide:function(){
dojo.widget.Toaster.superclass.hide.call(this);
if(this._scrollConnected){
this._scrollConnected=false;
dojo.event.disconnect(window,"onscroll",this,"_placeClip");
}
dojo.html.setOpacity(this.containerNode,1);
}});
