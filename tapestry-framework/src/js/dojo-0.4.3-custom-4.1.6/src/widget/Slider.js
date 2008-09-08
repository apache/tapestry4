dojo.provide("dojo.widget.Slider");
dojo.require("dojo.event.*");
dojo.require("dojo.dnd.*");
dojo.require("dojo.dnd.HtmlDragMove");
dojo.require("dojo.widget.*");
dojo.require("dojo.html.layout");
dojo.widget.defineWidget("dojo.widget.Slider",dojo.widget.HtmlWidget,{minimumX:0,minimumY:0,maximumX:10,maximumY:10,snapValuesX:0,snapValuesY:0,_snapToGrid:true,isEnableX:true,isEnableY:true,_valueSizeX:0,_valueSizeY:0,_minX:0,_minY:0,_constraintWidth:0,_constraintHeight:0,_clipLeft:0,_clipRight:0,_clipTop:0,_clipBottom:0,_clipXdelta:0,_clipYdelta:0,initialValueX:0,initialValueY:0,flipX:false,flipY:false,clickSelect:true,activeDrag:false,templateCssString:".sliderMain {\n  border: 0px !important;\n  border-spacing: 0px !important;\n  line-height: 0px !important;\n  padding: 0px !important;\n  display: -moz-inline-table !important;\n  display: inline !important;\n  -moz-user-focus: normal !important;\n}\n\n.sliderComponent {\n  border: 0px;\n  padding: 0px;\n  margin: 0px;\n}\n\n.sliderHandle { \n  top: 0px;\n  left: 0px;\n  z-index: 1000;\n  position: absolute !important;\n}\n\n.sliderOutsetButton { \n  border-style: outset;\n  border-width: 0px 1px 1px 0px;\n  border-color: black;\n}\n\n.sliderInsetButton { \n  border-style: inset;\n  border-width: 1px 0px 0px 1px;\n  border-color: black;\n}\n\n.sliderButtonY {\n  margin: 0px;\n  padding: 0px;\n  z-index: 900;\n}\n\n.sliderButtonX {\n  margin: 0px;\n  padding: 0px;\n  z-index: 900;\n}\n\n.sliderBackground { \n  z-index: 0;\n  display: block !important;\n  position: relative !important;\n}\n\n.sliderProgressBackground { \n  z-index: 800;\n  position: absolute !important;\n  clip: rect(0px,0px,0px,0px);\n}\n\n.sliderBackgroundSizeOnly { \n}\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/Slider.css"),templateString:"<table _=\"weird end tag formatting is to prevent whitespace from becoming &nbsp;\" \n\tclass=\"sliderMain\" \n\tdojoAttachPoint=\"focusNode\" \n\tdojoAttachEvent=\"onmousedown:_setFocus; onkey:_handleKeyEvents; onkeyup:_buttonReleased; onmouseup:_buttonReleased; onmousewheel:_mouseWheeled;\"\n\ttabindex=\"0\" cols=3 cellpadding=0 cellspacing=0 style=\"\">\n\t<tr valign=middle align=center>\n\t\t<td class=\"sliderComponent\" colspan=3 dojoAttachPoint=topBorderNode style=\"\"\n\t\t\t><img class=\"sliderOutsetButton sliderButtonY\"\n\t\t\t\tdojoAttachPoint=topButtonNode \n\t\t\t\tdojoAttachEvent=\"onmousedown:_topButtonPressed; onmousemove:_discardEvent; ondblclick:_topButtonDoubleClicked;\"\n\t\t\t\tsrc=\"${this.topButtonSrc}\" \n\t\t\t\tstyle=\"${this.buttonStyleY}\"\n\t\t></td>\n\t</tr>\n\t<tr valign=middle align=center>\n\t\t<td class=\"sliderComponent\" dojoAttachPoint=leftBorderNode style=\"\"\n\t\t\t><img class=\"sliderOutsetButton sliderButtonX\"\n\t\t\t\tdojoAttachPoint=leftButtonNode\n\t\t\t\tdojoAttachEvent=\"onmousedown:_leftButtonPressed; onmousemove:_discardEvent; ondblclick:_leftButtonDoubleClicked;\"\n\t\t\t\tsrc=\"${this.leftButtonSrc}\" \n\t\t\t\tstyle=\"${this.buttonStyleX}\"\n\t\t></td>\n\t\t<td dojoAttachPoint=constrainingContainerNode \n\t\t\tclass=\"sliderComponent sliderBackground\"\n\t\t\tstyle=\"${this.backgroundStyle}\"\n\t\t\t><img src=\"${this.handleSrc}\" \n\t\t\t\tclass=sliderHandle\n\t\t\t\tdojoAttachPoint=sliderHandleNode\n\t\t\t\tstyle=\"${this.handleStyle}\"\n\t\t\t><img src=\"${this.progressBackgroundSrc}\"\n\t\t\t\tclass=\"sliderBackgroundSizeOnly sliderProgressBackground\"\n\t\t\t\tdojoAttachPoint=progressBackgroundNode\n\t\t\t\tstyle=\"${this.backgroundSize}\"\n\t\t\t><img src=\"${this.backgroundSrc}\" \n\t\t\t\tclass=sliderBackgroundSizeOnly\n\t\t\t\tdojoAttachPoint=sliderBackgroundNode\n\t\t\t\tstyle=\"${this.backgroundSize}\"\n\t\t></td>\n\t\t<td class=\"sliderComponent\" dojoAttachPoint=rightBorderNode style=\"\"\n\t\t\t><img class=\"sliderOutsetButton sliderButtonX\"\n\t\t\t\tdojoAttachPoint=rightButtonNode\n\t\t\t\tdojoAttachEvent=\"onmousedown:_rightButtonPressed; onmousemove:_discardEvent; ondblclick:_rightButtonDoubleClicked;\"\n\t\t\t\tsrc=\"${this.rightButtonSrc}\" \n\t\t\t\tstyle=\"${this.buttonStyleX}\"\n\t\t></td>\n\t</tr>\n\t<tr valign=middle align=center>\n\t\t<td class=\"sliderComponent\" colspan=3 dojoAttachPoint=bottomBorderNode style=\"\"\n\t\t\t><img class=\"sliderOutsetButton sliderButtonY\"\n\t\t\t\tdojoAttachPoint=bottomButtonNode \n\t\t\t\tdojoAttachEvent=\"onmousedown:_bottomButtonPressed; onmousemove:_discardEvent; ondblclick:_bottomButtonDoubleClicked;\"\n\t\t\t\tsrc=\"${this.bottomButtonSrc}\" \n\t\t\t\tstyle=\"${this.buttonStyleY}\"\n\t\t></td>\n\t</tr>\n</table>\n",_isDragInProgress:false,bottomButtonSrc:dojo.uri.moduleUri("dojo.widget","templates/images/slider_down_arrow.png"),topButtonSrc:dojo.uri.moduleUri("dojo.widget","templates/images/slider_up_arrow.png"),leftButtonSrc:dojo.uri.moduleUri("dojo.widget","templates/images/slider_left_arrow.png"),rightButtonSrc:dojo.uri.moduleUri("dojo.widget","templates/images/slider_right_arrow.png"),backgroundSrc:dojo.uri.moduleUri("dojo.widget","templates/images/blank.gif"),progressBackgroundSrc:dojo.uri.moduleUri("dojo.widget","templates/images/blank.gif"),backgroundSize:"width:200px;height:200px;",backgroundStyle:"",buttonStyleX:"",buttonStyleY:"",handleStyle:"",handleSrc:dojo.uri.moduleUri("dojo.widget","templates/images/slider-button.png"),showButtons:true,_eventCount:0,_typamaticTimer:null,_typamaticFunction:null,defaultTimeout:500,timeoutChangeRate:0.9,_currentTimeout:this.defaultTimeout,_handleKeyEvents:function(_1){
if(!_1.key){
return;
}
if(!_1.ctrlKey&&!_1.altKey){
switch(_1.key){
case _1.KEY_LEFT_ARROW:
dojo.event.browser.stopEvent(_1);
this._leftButtonPressed(_1);
return;
case _1.KEY_RIGHT_ARROW:
dojo.event.browser.stopEvent(_1);
this._rightButtonPressed(_1);
return;
case _1.KEY_DOWN_ARROW:
dojo.event.browser.stopEvent(_1);
this._bottomButtonPressed(_1);
return;
case _1.KEY_UP_ARROW:
dojo.event.browser.stopEvent(_1);
this._topButtonPressed(_1);
return;
}
}
this._eventCount++;
},_pressButton:function(_2){
_2.className=_2.className.replace("Outset","Inset");
},_releaseButton:function(_3){
_3.className=_3.className.replace("Inset","Outset");
},_buttonPressed:function(_4,_5){
this._setFocus();
if(typeof _4=="object"){
if(this._typamaticTimer!=null){
if(this._typamaticNode==_5){
return;
}
clearTimeout(this._typamaticTimer);
}
this._buttonReleased(null);
this._eventCount++;
this._typamaticTimer=null;
this._currentTimeout=this.defaultTimeout;
dojo.event.browser.stopEvent(_4);
}else{
if(_4!=this._eventCount){
this._buttonReleased(null);
return false;
}
}
if(_5==this.leftButtonNode&&this.isEnableX){
this._snapX(dojo.html.getPixelValue(this.sliderHandleNode,"left")-this._valueSizeX);
}else{
if(_5==this.rightButtonNode&&this.isEnableX){
this._snapX(dojo.html.getPixelValue(this.sliderHandleNode,"left")+this._valueSizeX);
}else{
if(_5==this.topButtonNode&&this.isEnableY){
this._snapY(dojo.html.getPixelValue(this.sliderHandleNode,"top")-this._valueSizeY);
}else{
if(_5==this.bottomButtonNode&&this.isEnableY){
this._snapY(dojo.html.getPixelValue(this.sliderHandleNode,"top")+this._valueSizeY);
}else{
return false;
}
}
}
}
this._pressButton(_5);
this.notifyListeners();
this._typamaticNode=_5;
this._typamaticTimer=dojo.lang.setTimeout(this,"_buttonPressed",this._currentTimeout,this._eventCount,_5);
this._currentTimeout=Math.round(this._currentTimeout*this.timeoutChangeRate);
return false;
},_bottomButtonPressed:function(_6){
return this._buttonPressed(_6,this.bottomButtonNode);
},_bottomButtonDoubleClicked:function(_7){
var rc=this._bottomButtonPressed(_7);
dojo.lang.setTimeout(this,"_buttonReleased",50,null);
return rc;
},_topButtonPressed:function(_9){
return this._buttonPressed(_9,this.topButtonNode);
},_topButtonDoubleClicked:function(_a){
var rc=this._topButtonPressed(_a);
dojo.lang.setTimeout(this,"_buttonReleased",50,null);
return rc;
},_leftButtonPressed:function(_c){
return this._buttonPressed(_c,this.leftButtonNode);
},_leftButtonDoubleClicked:function(_d){
var rc=this._leftButtonPressed(_d);
dojo.lang.setTimeout(this,"_buttonReleased",50,null);
return rc;
},_rightButtonPressed:function(_f){
return this._buttonPressed(_f,this.rightButtonNode);
},_rightButtonDoubleClicked:function(evt){
var rc=this._rightButtonPressed(evt);
dojo.lang.setTimeout(this,"_buttonReleased",50,null);
return rc;
},_buttonReleased:function(evt){
if(typeof evt=="object"&&evt!=null&&typeof evt.keyCode!="undefined"&&evt.keyCode!=null){
var _13=evt.keyCode;
switch(_13){
case evt.KEY_LEFT_ARROW:
case evt.KEY_RIGHT_ARROW:
case evt.KEY_DOWN_ARROW:
case evt.KEY_UP_ARROW:
dojo.event.browser.stopEvent(evt);
break;
}
}
this._releaseButton(this.topButtonNode);
this._releaseButton(this.bottomButtonNode);
this._releaseButton(this.leftButtonNode);
this._releaseButton(this.rightButtonNode);
this._eventCount++;
if(this._typamaticTimer!=null){
clearTimeout(this._typamaticTimer);
}
this._typamaticTimer=null;
this._currentTimeout=this.defaultTimeout;
},_mouseWheeled:function(evt){
var _15=0;
if(typeof evt.wheelDelta=="number"){
_15=evt.wheelDelta;
}else{
if(typeof evt.detail=="number"){
_15=-evt.detail;
}
}
if(this.isEnableY){
if(_15>0){
this._topButtonPressed(evt);
this._buttonReleased(evt);
}else{
if(_15<0){
this._bottomButtonPressed(evt);
this._buttonReleased(evt);
}
}
}else{
if(this.isEnableX){
if(_15>0){
this._rightButtonPressed(evt);
this._buttonReleased(evt);
}else{
if(_15<0){
this._leftButtonPressed(evt);
this._buttonReleased(evt);
}
}
}
}
},_discardEvent:function(evt){
dojo.event.browser.stopEvent(evt);
},_setFocus:function(){
if(this.focusNode.focus){
this.focusNode.focus();
}
},fillInTemplate:function(_17,_18){
var _19=this.getFragNodeRef(_18);
dojo.html.copyStyle(this.domNode,_19);
var _1a=this.domNode.style.padding;
if(dojo.lang.isString(_1a)&&_1a!=""&&_1a!="0px"&&_1a!="0px 0px 0px 0px"){
this.topBorderNode.style.padding=this.bottomBorderNode.style.padding=_1a;
this.topBorderNode.style.paddingBottom="0px";
this.bottomBorderNode.style.paddingTop="0px";
this.rightBorderNode.style.paddingRight=this.domNode.style.paddingRight;
this.leftBorderNode.style.paddingLeft=this.domNode.style.paddingLeft;
this.domNode.style.padding="0px 0px 0px 0px";
}
var _1b=this.domNode.style.borderWidth;
if(dojo.lang.isString(_1b)&&_1b!=""&&_1b!="0px"&&_1b!="0px 0px 0px 0px"){
this.topBorderNode.style.borderStyle=this.rightBorderNode.style.borderStyle=this.bottomBorderNode.style.borderStyle=this.leftBorderNode.style.borderStyle=this.domNode.style.borderStyle;
this.topBorderNode.style.borderColor=this.rightBorderNode.style.borderColor=this.bottomBorderNode.style.borderColor=this.leftBorderNode.style.borderColor=this.domNode.style.borderColor;
this.topBorderNode.style.borderWidth=this.bottomBorderNode.style.borderWidth=_1b;
this.topBorderNode.style.borderBottomWidth="0px";
this.bottomBorderNode.style.borderTopWidth="0px";
this.rightBorderNode.style.borderRightWidth=this.domNode.style.borderRightWidth;
this.leftBorderNode.style.borderLeftWidth=this.domNode.style.borderLeftWidth;
this.domNode.style.borderWidth="0px 0px 0px 0px";
}
this._handleMove=new dojo.widget._SliderDragMoveSource(this.sliderHandleNode);
this._handleMove.setParent(this);
if(this.clickSelect){
dojo.event.connect(this.constrainingContainerNode,"onmousedown",this,"_onClick");
}
if(this.isEnableX){
this.setValueX(!isNaN(this.initialValueX)?this.initialValueX:(!isNaN(this.minimumX)?this.minimumX:0));
}
if(!this.isEnableX||!this.showButtons){
this.rightButtonNode.style.width="1px";
this.rightButtonNode.style.visibility="hidden";
this.leftButtonNode.style.width="1px";
this.leftButtonNode.style.visibility="hidden";
}
if(this.isEnableY){
this.setValueY(!isNaN(this.initialValueY)?this.initialValueY:(!isNaN(this.minimumY)?this.minimumY:0));
}
if(!this.isEnableY||!this.showButtons){
this.bottomButtonNode.style.width="1px";
this.bottomButtonNode.style.visibility="hidden";
this.topButtonNode.style.width="1px";
this.topButtonNode.style.visibility="hidden";
}
if(this.focusNode.addEventListener){
this.focusNode.addEventListener("DOMMouseScroll",dojo.lang.hitch(this,"_mouseWheeled"),false);
}
},_snapX:function(x){
if(x<0){
x=0;
}else{
if(x>this._constraintWidth){
x=this._constraintWidth;
}else{
var _1d=Math.round(x/this._valueSizeX);
x=Math.round(_1d*this._valueSizeX);
}
}
this.sliderHandleNode.style.left=x+"px";
if(this.flipX){
this._clipLeft=x+this._clipXdelta;
}else{
this._clipRight=x+this._clipXdelta;
}
this.progressBackgroundNode.style.clip="rect("+this._clipTop+"px,"+this._clipRight+"px,"+this._clipBottom+"px,"+this._clipLeft+"px)";
},_calc_valueSizeX:function(){
var _1e=dojo.html.getContentBox(this.constrainingContainerNode);
var _1f=dojo.html.getContentBox(this.sliderHandleNode);
if(isNaN(_1e.width)||isNaN(_1f.width)||_1e.width<=0||_1f.width<=0){
return false;
}
this._constraintWidth=_1e.width+dojo.html.getPadding(this.constrainingContainerNode).width-_1f.width;
if(this.flipX){
this._clipLeft=this._clipRight=_1e.width;
}else{
this._clipLeft=this._clipRight=0;
}
this._clipXdelta=_1f.width>>1;
if(!this.isEnableY){
this._clipTop=0;
this._clipBottom=_1e.height;
}
if(this._constraintWidth<=0){
return false;
}
if(this.snapValuesX==0){
this.snapValuesX=this._constraintWidth+1;
}
this._valueSizeX=this._constraintWidth/(this.snapValuesX-1);
return true;
},setValueX:function(_20){
if(0==this._valueSizeX){
if(this._calc_valueSizeX()==false){
dojo.lang.setTimeout(this,"setValueX",100,_20);
return;
}
}
if(isNaN(_20)){
_20=0;
}
if(_20>this.maximumX){
_20=this.maximumX;
}else{
if(_20<this.minimumX){
_20=this.minimumX;
}
}
var _21=(_20-this.minimumX)/(this.maximumX-this.minimumX);
if(this.flipX){
_21=1-_21;
}
this._snapX(_21*this._constraintWidth);
this.notifyListeners();
},getValueX:function(){
var _22=dojo.html.getPixelValue(this.sliderHandleNode,"left")/this._constraintWidth;
if(this.flipX){
_22=1-_22;
}
return Math.round(_22*(this.snapValuesX-1))*((this.maximumX-this.minimumX)/(this.snapValuesX-1))+this.minimumX;
},_snapY:function(y){
if(y<0){
y=0;
}else{
if(y>this._constraintHeight){
y=this._constraintHeight;
}else{
var _24=Math.round(y/this._valueSizeY);
y=Math.round(_24*this._valueSizeY);
}
}
this.sliderHandleNode.style.top=y+"px";
if(this.flipY){
this._clipTop=y+this._clipYdelta;
}else{
this._clipBottom=y+this._clipYdelta;
}
this.progressBackgroundNode.style.clip="rect("+this._clipTop+"px,"+this._clipRight+"px,"+this._clipBottom+"px,"+this._clipLeft+"px)";
},_calc_valueSizeY:function(){
var _25=dojo.html.getContentBox(this.constrainingContainerNode);
var _26=dojo.html.getContentBox(this.sliderHandleNode);
if(isNaN(_25.height)||isNaN(_26.height)||_25.height<=0||_26.height<=0){
return false;
}
this._constraintHeight=_25.height+dojo.html.getPadding(this.constrainingContainerNode).height-_26.height;
if(this.flipY){
this._clipTop=this._clipBottom=_25.height;
}else{
this._clipTop=this._clipBottom=0;
}
this._clipYdelta=_26.height>>1;
if(!this.isEnableX){
this._clipLeft=0;
this._clipRight=_25.width;
}
if(this._constraintHeight<=0){
return false;
}
if(this.snapValuesY==0){
this.snapValuesY=this._constraintHeight+1;
}
this._valueSizeY=this._constraintHeight/(this.snapValuesY-1);
return true;
},setValueY:function(_27){
if(0==this._valueSizeY){
if(this._calc_valueSizeY()==false){
dojo.lang.setTimeout(this,"setValueY",100,_27);
return;
}
}
if(isNaN(_27)){
_27=0;
}
if(_27>this.maximumY){
_27=this.maximumY;
}else{
if(_27<this.minimumY){
_27=this.minimumY;
}
}
var _28=(_27-this.minimumY)/(this.maximumY-this.minimumY);
if(this.flipY){
_28=1-_28;
}
this._snapY(_28*this._constraintHeight);
this.notifyListeners();
},getValueY:function(){
var _29=dojo.html.getPixelValue(this.sliderHandleNode,"top")/this._constraintHeight;
if(this.flipY){
_29=1-_29;
}
return Math.round(_29*(this.snapValuesY-1))*((this.maximumY-this.minimumY)/(this.snapValuesY-1))+this.minimumY;
},_onClick:function(evt){
if(this._isDragInProgress){
return;
}
var _2b=dojo.html.getAbsolutePosition(this.constrainingContainerNode,true,dojo.html.boxSizing.MARGIN_BOX);
var _2c=dojo.html.getContentBox(this._handleMove.domNode);
if(this.isEnableX){
var x=evt.pageX-_2b.x-(_2c.width>>1);
this._snapX(x);
}
if(this.isEnableY){
var y=evt.pageY-_2b.y-(_2c.height>>1);
this._snapY(y);
}
this.notifyListeners();
},notifyListeners:function(){
this.onValueChanged(this.getValueX(),this.getValueY());
},onValueChanged:function(x,y){
}});
dojo.widget.defineWidget("dojo.widget.SliderHorizontal",dojo.widget.Slider,{isEnableX:true,isEnableY:false,initialValue:"",snapValues:"",minimum:"",maximum:"",buttonStyle:"",backgroundSize:"height:10px;width:200px;",backgroundSrc:dojo.uri.moduleUri("dojo.widget","templates/images/slider-bg.gif"),flip:false,postMixInProperties:function(){
dojo.widget.SliderHorizontal.superclass.postMixInProperties.apply(this,arguments);
if(!isNaN(parseFloat(this.initialValue))){
this.initialValueX=parseFloat(this.initialValue);
}
if(!isNaN(parseFloat(this.minimum))){
this.minimumX=parseFloat(this.minimum);
}
if(!isNaN(parseFloat(this.maximum))){
this.maximumX=parseFloat(this.maximum);
}
if(!isNaN(parseInt(this.snapValues))){
this.snapValuesX=parseInt(this.snapValues);
}
if(dojo.lang.isString(this.buttonStyle)&&this.buttonStyle!=""){
this.buttonStyleX=this.buttonStyle;
}
if(dojo.lang.isBoolean(this.flip)){
this.flipX=this.flip;
}
},notifyListeners:function(){
this.onValueChanged(this.getValueX());
},getValue:function(){
return this.getValueX();
},setValue:function(_31){
this.setValueX(_31);
},onValueChanged:function(_32){
}});
dojo.widget.defineWidget("dojo.widget.SliderVertical",dojo.widget.Slider,{isEnableX:false,isEnableY:true,initialValue:"",snapValues:"",minimum:"",maximum:"",buttonStyle:"",backgroundSize:"width:10px;height:200px;",backgroundSrc:dojo.uri.moduleUri("dojo.widget","templates/images/slider-bg-vert.gif"),flip:false,postMixInProperties:function(){
dojo.widget.SliderVertical.superclass.postMixInProperties.apply(this,arguments);
if(!isNaN(parseFloat(this.initialValue))){
this.initialValueY=parseFloat(this.initialValue);
}
if(!isNaN(parseFloat(this.minimum))){
this.minimumY=parseFloat(this.minimum);
}
if(!isNaN(parseFloat(this.maximum))){
this.maximumY=parseFloat(this.maximum);
}
if(!isNaN(parseInt(this.snapValues))){
this.snapValuesY=parseInt(this.snapValues);
}
if(dojo.lang.isString(this.buttonStyle)&&this.buttonStyle!=""){
this.buttonStyleY=this.buttonStyle;
}
if(dojo.lang.isBoolean(this.flip)){
this.flipY=this.flip;
}
},notifyListeners:function(){
this.onValueChanged(this.getValueY());
},getValue:function(){
return this.getValueY();
},setValue:function(_33){
this.setValueY(_33);
},onValueChanged:function(_34){
}});
dojo.declare("dojo.widget._SliderDragMoveSource",dojo.dnd.HtmlDragMoveSource,{slider:null,onDragStart:function(evt){
this.slider._isDragInProgress=true;
var _36=this.createDragMoveObject();
this.slider.notifyListeners();
return _36;
},onDragEnd:function(evt){
this.slider._isDragInProgress=false;
this.slider.notifyListeners();
},createDragMoveObject:function(){
var _38=new dojo.widget._SliderDragMoveObject(this.dragObject,this.type);
_38.slider=this.slider;
if(this.dragClass){
_38.dragClass=this.dragClass;
}
return _38;
},setParent:function(_39){
this.slider=_39;
}});
dojo.declare("dojo.widget._SliderDragMoveObject",dojo.dnd.HtmlDragMoveObject,{slider:null,onDragMove:function(evt){
this.updateDragOffset();
if(this.slider.isEnableX){
var x=this.dragOffset.x+evt.pageX;
this.slider._snapX(x);
}
if(this.slider.isEnableY){
var y=this.dragOffset.y+evt.pageY;
this.slider._snapY(y);
}
if(this.slider.activeDrag){
this.slider.notifyListeners();
}
}});
