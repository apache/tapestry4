dojo.provide("dojo.widget.SplitContainer");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.ContentPane");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.html.style");
dojo.require("dojo.html.layout");
dojo.require("dojo.html.selection");
dojo.require("dojo.io.cookie");
dojo.widget.defineWidget("dojo.widget.SplitContainer",dojo.widget.HtmlWidget,function(){
this.sizers=[];
},{isContainer:true,templateCssString:".dojoSplitContainer{\n\tposition: relative;\n\toverflow: hidden;\n\tdisplay: block;\n}\n\n.dojoSplitPane{\n\tposition: absolute;\n}\n\n.dojoSplitContainerSizerH,\n.dojoSplitContainerSizerV {\n\tfont-size: 1px;\n\tcursor: move;\n\tcursor: w-resize;\n\tbackground-color: ThreeDFace;\n\tborder: 1px solid;\n\tborder-color: ThreeDHighlight ThreeDShadow ThreeDShadow ThreeDHighlight;\n\tmargin: 0;\n}\n\n.dojoSplitContainerSizerV {\n\tcursor: n-resize;\n}\n\n.dojoSplitContainerVirtualSizerH,\n.dojoSplitContainerVirtualSizerV {\n\tfont-size: 1px;\n\tcursor: move;\n\tcursor: w-resize;\n\tbackground-color: ThreeDShadow;\n\t-moz-opacity: 0.5;\n\topacity: 0.5;\n\tfilter: Alpha(Opacity=50);\n\tmargin: 0;\n}\n\n.dojoSplitContainerVirtualSizerV {\n\tcursor: n-resize;\n}\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/SplitContainer.css"),activeSizing:false,sizerWidth:15,orientation:"horizontal",persist:true,postMixInProperties:function(){
dojo.widget.SplitContainer.superclass.postMixInProperties.apply(this,arguments);
this.isHorizontal=(this.orientation=="horizontal");
},fillInTemplate:function(){
dojo.widget.SplitContainer.superclass.fillInTemplate.apply(this,arguments);
dojo.html.addClass(this.domNode,"dojoSplitContainer");
if(dojo.render.html.moz){
this.domNode.style.overflow="-moz-scrollbars-none";
}
var _1=dojo.html.getContentBox(this.domNode);
this.paneWidth=_1.width;
this.paneHeight=_1.height;
},onResized:function(e){
var _3=dojo.html.getContentBox(this.domNode);
this.paneWidth=_3.width;
this.paneHeight=_3.height;
this._layoutPanels();
},postCreate:function(_4,_5,_6){
dojo.widget.SplitContainer.superclass.postCreate.apply(this,arguments);
for(var i=0;i<this.children.length;i++){
with(this.children[i].domNode.style){
position="absolute";
}
dojo.html.addClass(this.children[i].domNode,"dojoSplitPane");
if(i==this.children.length-1){
break;
}
this._addSizer();
}
if(typeof this.sizerWidth=="object"){
try{
this.sizerWidth=parseInt(this.sizerWidth.toString());
}
catch(e){
this.sizerWidth=15;
}
}
this.virtualSizer=document.createElement("div");
this.virtualSizer.style.position="absolute";
this.virtualSizer.style.display="none";
this.virtualSizer.style.zIndex=10;
this.virtualSizer.className=this.isHorizontal?"dojoSplitContainerVirtualSizerH":"dojoSplitContainerVirtualSizerV";
this.domNode.appendChild(this.virtualSizer);
dojo.html.disableSelection(this.virtualSizer);
if(this.persist){
this._restoreState();
}
this.resizeSoon();
},_injectChild:function(_8){
with(_8.domNode.style){
position="absolute";
}
dojo.html.addClass(_8.domNode,"dojoSplitPane");
},_addSizer:function(){
var i=this.sizers.length;
this.sizers[i]=document.createElement("div");
this.sizers[i].style.position="absolute";
this.sizers[i].className=this.isHorizontal?"dojoSplitContainerSizerH":"dojoSplitContainerSizerV";
var _a=this;
var _b=(function(){
var _c=i;
return function(e){
_a.beginSizing(e,_c);
};
})();
dojo.event.connect(this.sizers[i],"onmousedown",_b);
this.domNode.appendChild(this.sizers[i]);
dojo.html.disableSelection(this.sizers[i]);
},removeChild:function(_e){
if(this.sizers.length>0){
for(var x=0;x<this.children.length;x++){
if(this.children[x]===_e){
var i=this.sizers.length-1;
this.domNode.removeChild(this.sizers[i]);
this.sizers.length=i;
break;
}
}
}
dojo.widget.SplitContainer.superclass.removeChild.call(this,_e,arguments);
this.onResized();
},addChild:function(_11){
dojo.widget.SplitContainer.superclass.addChild.apply(this,arguments);
this._injectChild(_11);
if(this.children.length>1){
this._addSizer();
}
this._layoutPanels();
},_layoutPanels:function(){
if(this.children.length==0){
return;
}
var _12=this.isHorizontal?this.paneWidth:this.paneHeight;
if(this.children.length>1){
_12-=this.sizerWidth*(this.children.length-1);
}
var _13=0;
for(var i=0;i<this.children.length;i++){
_13+=this.children[i].sizeShare;
}
var _15=_12/_13;
var _16=0;
for(var i=0;i<this.children.length-1;i++){
var _17=Math.round(_15*this.children[i].sizeShare);
this.children[i].sizeActual=_17;
_16+=_17;
}
this.children[this.children.length-1].sizeActual=_12-_16;
this._checkSizes();
var pos=0;
var _17=this.children[0].sizeActual;
this._movePanel(this.children[0],pos,_17);
this.children[0].position=pos;
pos+=_17;
for(var i=1;i<this.children.length;i++){
this._moveSlider(this.sizers[i-1],pos,this.sizerWidth);
this.sizers[i-1].position=pos;
pos+=this.sizerWidth;
_17=this.children[i].sizeActual;
this._movePanel(this.children[i],pos,_17);
this.children[i].position=pos;
pos+=_17;
}
},_movePanel:function(_19,pos,_1b){
if(this.isHorizontal){
_19.domNode.style.left=pos+"px";
_19.domNode.style.top=0;
_19.resizeTo(_1b,this.paneHeight);
}else{
_19.domNode.style.left=0;
_19.domNode.style.top=pos+"px";
_19.resizeTo(this.paneWidth,_1b);
}
},_moveSlider:function(_1c,pos,_1e){
if(this.isHorizontal){
_1c.style.left=pos+"px";
_1c.style.top=0;
dojo.html.setMarginBox(_1c,{width:_1e,height:this.paneHeight});
}else{
_1c.style.left=0;
_1c.style.top=pos+"px";
dojo.html.setMarginBox(_1c,{width:this.paneWidth,height:_1e});
}
},_growPane:function(_1f,_20){
if(_1f>0){
if(_20.sizeActual>_20.sizeMin){
if((_20.sizeActual-_20.sizeMin)>_1f){
_20.sizeActual=_20.sizeActual-_1f;
_1f=0;
}else{
_1f-=_20.sizeActual-_20.sizeMin;
_20.sizeActual=_20.sizeMin;
}
}
}
return _1f;
},_checkSizes:function(){
var _21=0;
var _22=0;
for(var i=0;i<this.children.length;i++){
_22+=this.children[i].sizeActual;
_21+=this.children[i].sizeMin;
}
if(_21<=_22){
var _24=0;
for(var i=0;i<this.children.length;i++){
if(this.children[i].sizeActual<this.children[i].sizeMin){
_24+=this.children[i].sizeMin-this.children[i].sizeActual;
this.children[i].sizeActual=this.children[i].sizeMin;
}
}
if(_24>0){
if(this.isDraggingLeft){
for(var i=this.children.length-1;i>=0;i--){
_24=this._growPane(_24,this.children[i]);
}
}else{
for(var i=0;i<this.children.length;i++){
_24=this._growPane(_24,this.children[i]);
}
}
}
}else{
for(var i=0;i<this.children.length;i++){
this.children[i].sizeActual=Math.round(_22*(this.children[i].sizeMin/_21));
}
}
},beginSizing:function(e,i){
this.paneBefore=this.children[i];
this.paneAfter=this.children[i+1];
this.isSizing=true;
this.sizingSplitter=this.sizers[i];
this.originPos=dojo.html.getAbsolutePosition(this.children[0].domNode,true,dojo.html.boxSizing.MARGIN_BOX);
if(this.isHorizontal){
var _27=(e.layerX?e.layerX:e.offsetX);
var _28=e.pageX;
this.originPos=this.originPos.x;
}else{
var _27=(e.layerY?e.layerY:e.offsetY);
var _28=e.pageY;
this.originPos=this.originPos.y;
}
this.startPoint=this.lastPoint=_28;
this.screenToClientOffset=_28-_27;
this.dragOffset=this.lastPoint-this.paneBefore.sizeActual-this.originPos-this.paneBefore.position;
if(!this.activeSizing){
this._showSizingLine();
}
dojo.event.connect(document.documentElement,"onmousemove",this,"changeSizing");
dojo.event.connect(document.documentElement,"onmouseup",this,"endSizing");
dojo.event.browser.stopEvent(e);
},changeSizing:function(e){
this.lastPoint=this.isHorizontal?e.pageX:e.pageY;
if(this.activeSizing){
this.movePoint();
this._updateSize();
}else{
this.movePoint();
this._moveSizingLine();
}
dojo.event.browser.stopEvent(e);
},endSizing:function(e){
if(!this.activeSizing){
this._hideSizingLine();
}
this._updateSize();
this.isSizing=false;
dojo.event.disconnect(document.documentElement,"onmousemove",this,"changeSizing");
dojo.event.disconnect(document.documentElement,"onmouseup",this,"endSizing");
if(this.persist){
this._saveState(this);
}
},movePoint:function(){
var p=this.lastPoint-this.screenToClientOffset;
var a=p-this.dragOffset;
a=this.legaliseSplitPoint(a);
p=a+this.dragOffset;
this.lastPoint=p+this.screenToClientOffset;
},legaliseSplitPoint:function(a){
a+=this.sizingSplitter.position;
this.isDraggingLeft=(a>0)?true:false;
if(!this.activeSizing){
if(a<this.paneBefore.position+this.paneBefore.sizeMin){
a=this.paneBefore.position+this.paneBefore.sizeMin;
}
if(a>this.paneAfter.position+(this.paneAfter.sizeActual-(this.sizerWidth+this.paneAfter.sizeMin))){
a=this.paneAfter.position+(this.paneAfter.sizeActual-(this.sizerWidth+this.paneAfter.sizeMin));
}
}
a-=this.sizingSplitter.position;
this._checkSizes();
return a;
},_updateSize:function(){
var pos=this.lastPoint-this.dragOffset-this.originPos;
var _2f=this.paneBefore.position;
var _30=this.paneAfter.position+this.paneAfter.sizeActual;
this.paneBefore.sizeActual=pos-_2f;
this.paneAfter.position=pos+this.sizerWidth;
this.paneAfter.sizeActual=_30-this.paneAfter.position;
for(var i=0;i<this.children.length;i++){
this.children[i].sizeShare=this.children[i].sizeActual;
}
this._layoutPanels();
},_showSizingLine:function(){
this._moveSizingLine();
if(this.isHorizontal){
dojo.html.setMarginBox(this.virtualSizer,{width:this.sizerWidth,height:this.paneHeight});
}else{
dojo.html.setMarginBox(this.virtualSizer,{width:this.paneWidth,height:this.sizerWidth});
}
this.virtualSizer.style.display="block";
},_hideSizingLine:function(){
this.virtualSizer.style.display="none";
},_moveSizingLine:function(){
var pos=this.lastPoint-this.startPoint+this.sizingSplitter.position;
if(this.isHorizontal){
this.virtualSizer.style.left=pos+"px";
}else{
var pos=(this.lastPoint-this.startPoint)+this.sizingSplitter.position;
this.virtualSizer.style.top=pos+"px";
}
},_getCookieName:function(i){
return this.widgetId+"_"+i;
},_restoreState:function(){
for(var i=0;i<this.children.length;i++){
var _35=this._getCookieName(i);
var _36=dojo.io.cookie.getCookie(_35);
if(_36!=null){
var pos=parseInt(_36);
if(typeof pos=="number"){
this.children[i].sizeShare=pos;
}
}
}
},_saveState:function(){
for(var i=0;i<this.children.length;i++){
var _39=this._getCookieName(i);
dojo.io.cookie.setCookie(_39,this.children[i].sizeShare,null,null,null,null);
}
}});
dojo.lang.extend(dojo.widget.Widget,{sizeMin:10,sizeShare:10});
dojo.widget.defineWidget("dojo.widget.SplitContainerPanel",dojo.widget.ContentPane,{});
