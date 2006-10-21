

dojo.provide("dojo.widget.Slider");


dojo.require("dojo.event.*");
dojo.require("dojo.dnd.*");

dojo.require("dojo.dnd.HtmlDragMove");
dojo.require("dojo.widget.*");
dojo.require("dojo.html.layout");






























dojo.widget.defineWidget (
"dojo.widget.Slider",
dojo.widget.HtmlWidget,
{
// Number
//	minimum value to be represented by slider in the horizontal direction
minimumX: 0,
// Number
//	minimum value to be represented by slider in the vertical direction
minimumY: 0,
// Number
//	maximum value to be represented by slider in the horizontal direction
maximumX: 10,
// Number
//	maximum value to be represented by slider in the vertical direction
maximumY: 10,
// Number
//	number of values to be represented by slider in the horizontal direction
//	=0 means no snapping
snapValuesX: 0,
// Number
//	number of values to be represented by slider in the vertical direction
//	=0 means no snapping
snapValuesY: 0,
// should the handle snap to the grid or remain where it was dragged to?
// FIXME: snapToGrid=false is logically in conflict with setting snapValuesX and snapValuesY
_snapToGrid: true,
// Boolean
//	enables (disables) sliding in the horizontal direction
isEnableX: true,
// Boolean
//	enables (disables) sliding in the vertical direction
isEnableY: true,
// value size (pixels) in the x dimension
_valueSizeX: 0.0,
// value size (pixels) in the y dimension
_valueSizeY: 0.0,
// left most edge of constraining container (pixels) in the X dimension
_minX: 0,
// top most edge of constraining container (pixels) in the Y dimension
_minY: 0,
// constrained slider size (pixels) in the x dimension
_constraintWidth: 0,
// constrained slider size (pixels) in the y dimension
_constraintHeight: 0,
// progress image right clip value (pixels) in the X dimension
_clipLeft: 0,
// progress image left clip value (pixels) in the X dimension
_clipRight: 0,
// progress image top clip value (pixels) in the Y dimension
_clipTop: 0,
// progress image bottom clip value (pixels) in the Y dimension
_clipBottom: 0,
// half the size of the slider handle (pixels) in the X dimension
_clipXdelta: 0,
// half the size of the slider handle (pixels) in the Y dimension
_clipYdelta: 0,
// Number
//	initial value in the x dimension
initialValueX: 0,
// Number
//	initial value in the y dimension
initialValueY: 0,
// Boolean
//	values decrease in the X dimension
flipX: false,
// Boolean
//	values decrease in the Y dimension
flipY: false,
// Boolean
//	enables (disables) the user to click on the slider to set the position
clickSelect: true,
// Boolean
//	disables (enables) the value to change while you are dragging, or just after drag finishes
activeDrag: false,

templateCssPath: dojo.uri.dojoUri ("src/widget/templates/Slider.css"),
templatePath: dojo.uri.dojoUri ("src/widget/templates/Slider.html"),

// This is set to true when a drag is started, so that it is not confused
// with a click
_isDragInProgress: false,

// default user style attributes
// String
//	down arrow graphic URL
bottomButtonSrc: dojo.uri.dojoUri("src/widget/templates/images/slider_down_arrow.png"),
// String
//	up arrow graphic URL
topButtonSrc: dojo.uri.dojoUri("src/widget/templates/images/slider_up_arrow.png"),
// String
//	left arrow graphic URL
leftButtonSrc: dojo.uri.dojoUri("src/widget/templates/images/slider_left_arrow.png"),
// String
//	right arrow graphic URL
rightButtonSrc: dojo.uri.dojoUri("src/widget/templates/images/slider_right_arrow.png"),
// String
//	slider background graphic URL
backgroundSrc: dojo.uri.dojoUri("src/widget/templates/images/blank.gif"),
// String
//	slider background graphic URL to overlay the normal background to show progress
progressBackgroundSrc: dojo.uri.dojoUri("src/widget/templates/images/blank.gif"),
// String
//	sizing style attributes for the background image
backgroundSize: "width:200px;height:200px;",
// String
//	style attributes (other than sizing) for the background image
backgroundStyle: "",
// String
//	style attributes for the left and right arrow images
buttonStyleX: "",
// String
//	style attributes for the up and down arrow images
buttonStyleY: "",
// String
//	style attributes for the moveable slider image
handleStyle: "",
// String
//	moveable slider graphic URL
handleSrc: dojo.uri.dojoUri("src/widget/templates/images/slider-button.png"),
// Boolean
//	show (don't show) the arrow buttons
showButtons: true,
_eventCount: 0,
_typamaticTimer: null,
_typamaticFunction: null,
// Number
//	number of milliseconds before a held key or button becomes typematic
defaultTimeout: 500,
// Number
//	fraction of time used to change the typematic timer between events
//	1.0 means that each typematic event fires at defaultTimeout intervals
//	< 1.0 means that each typematic event fires at an increasing faster rate
timeoutChangeRate: 0.90,
_currentTimeout: this.defaultTimeout,

// does the keyboard related stuff
_handleKeyEvents: function( evt){
if(!evt.key){ return; }

if(!evt.ctrlKey && !evt.altKey){
switch(evt.key){
case evt.KEY_LEFT_ARROW:
dojo.event.browser.stopEvent(evt);
this._leftButtonPressed(evt);
return;
case evt.KEY_RIGHT_ARROW:
dojo.event.browser.stopEvent(evt);
this._rightButtonPressed(evt);
return;
case evt.KEY_DOWN_ARROW:
dojo.event.browser.stopEvent(evt);
this._bottomButtonPressed(evt);
return;
case evt.KEY_UP_ARROW:
dojo.event.browser.stopEvent(evt);
this._topButtonPressed(evt);
return;
}
}
this._eventCount++;

},

_pressButton: function( buttonNode){
buttonNode.className = buttonNode.className.replace("Outset","Inset");
},

_releaseButton: function( buttonNode){
buttonNode.className = buttonNode.className.replace("Inset","Outset");
},

_buttonPressed: function( evt,  buttonNode){
this._setFocus();
if(typeof evt == "object"){
if(this._typamaticTimer != null){
if(this._typamaticNode == buttonNode){
return;
}
clearTimeout(this._typamaticTimer);
}
this._buttonReleased(null);
this._eventCount++;
this._typamaticTimer = null;
this._currentTimeout = this.defaultTimeout;
dojo.event.browser.stopEvent(evt);
}else if (evt != this._eventCount){
this._buttonReleased(null);
return false;
}
if (buttonNode == this.leftButtonNode && this.isEnableX){
this._snapX(dojo.html.getPixelValue (this.sliderHandleNode,"left") - this._valueSizeX);
}
else if (buttonNode == this.rightButtonNode && this.isEnableX){
this._snapX(dojo.html.getPixelValue (this.sliderHandleNode,"left") + this._valueSizeX);
}
else if (buttonNode == this.topButtonNode && this.isEnableY){
this._snapY(dojo.html.getPixelValue (this.sliderHandleNode,"top") - this._valueSizeY);
}
else if (buttonNode == this.bottomButtonNode && this.isEnableY){
this._snapY(dojo.html.getPixelValue (this.sliderHandleNode,"top") + this._valueSizeY);
}
else {
return false;
}
this._pressButton(buttonNode);
this.notifyListeners();
this._typamaticNode = buttonNode;
this._typamaticTimer = dojo.lang.setTimeout(this, "_buttonPressed", this._currentTimeout, this._eventCount, buttonNode);
this._currentTimeout = Math.round(this._currentTimeout * this.timeoutChangeRate);
return false;
},

_bottomButtonPressed: function( evt){
return this._buttonPressed(evt,this.bottomButtonNode);
},

// IE sends these events when rapid clicking, mimic an extra single click
_bottomButtonDoubleClicked: function( evt){
var rc = this._bottomButtonPressed(evt);
dojo.lang.setTimeout( this, "_buttonReleased", 50, null);
return rc;
},

_topButtonPressed: function( evt){
return this._buttonPressed(evt,this.topButtonNode);
},

// IE sends these events when rapid clicking, mimic an extra single click
_topButtonDoubleClicked: function( evt){
var rc = this._topButtonPressed(evt);
dojo.lang.setTimeout( this, "_buttonReleased", 50, null);
return rc;
},

_leftButtonPressed: function( evt){
return this._buttonPressed(evt,this.leftButtonNode);
},

// IE sends these events when rapid clicking, mimic an extra single click
_leftButtonDoubleClicked: function( evt){
var rc = this._leftButtonPressed(evt);
dojo.lang.setTimeout( this, "_buttonReleased", 50, null);
return rc;
},

_rightButtonPressed: function( evt){
return this._buttonPressed(evt,this.rightButtonNode);
},

// IE sends these events when rapid clicking, mimic an extra single click
_rightButtonDoubleClicked: function( evt){
var rc = this._rightButtonPressed(evt);
dojo.lang.setTimeout( this, "_buttonReleased", 50, null);
return rc;
},

_buttonReleased: function( evt){
if(typeof evt == "object" && evt != null && typeof evt.keyCode != "undefined" && evt.keyCode != null){
var keyCode = evt.keyCode;

switch(keyCode){
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
if(this._typamaticTimer != null){
clearTimeout(this._typamaticTimer);
}
this._typamaticTimer = null;
this._currentTimeout = this.defaultTimeout;
},

_mouseWheeled: function( evt){
var scrollAmount = 0;
if(typeof evt.wheelDelta == 'number'){ // IE
scrollAmount = evt.wheelDelta;
}else if (typeof evt.detail == 'number'){ // Mozilla+Firefox
scrollAmount = -evt.detail;
}
if (this.isEnableY){
if(scrollAmount > 0){
this._topButtonPressed(evt);
this._buttonReleased(evt);
}else if (scrollAmount < 0){
this._bottomButtonPressed(evt);
this._buttonReleased(evt);
}
} else if (this.isEnableX){
if(scrollAmount > 0){
this._rightButtonPressed(evt);
this._buttonReleased(evt);
}else if (scrollAmount < 0){
this._leftButtonPressed(evt);
this._buttonReleased(evt);
}
}
},

_discardEvent: function( evt){
dojo.event.browser.stopEvent(evt);
},

_setFocus: function(){
if (this.focusNode.focus){
this.focusNode.focus();
}
},

// This function is called when the template is loaded
fillInTemplate: function ( args,  frag)
{
var source = this.getFragNodeRef(frag);
dojo.html.copyStyle(this.domNode, source);
// the user's style for the widget might include border and padding
// unfortunately, border isn't supported for inline elements
// so I get to fake everyone out by setting the border and padding
// of the outer table cells
var padding = this.domNode.style.padding;
if (dojo.lang.isString(padding) && padding != "" && padding != "0px" && padding != "0px 0px 0px 0px"){
this.topBorderNode.style.padding =
this.bottomBorderNode.style.padding = padding;
this.topBorderNode.style.paddingBottom = "0px";
this.bottomBorderNode.style.paddingTop = "0px";
this.rightBorderNode.style.paddingRight = this.domNode.style.paddingRight;
this.leftBorderNode.style.paddingLeft= this.domNode.style.paddingLeft;
this.domNode.style.padding = "0px 0px 0px 0px";
}
var borderWidth = this.domNode.style.borderWidth;
if (dojo.lang.isString(borderWidth) && borderWidth != "" && borderWidth != "0px" && borderWidth != "0px 0px 0px 0px"){
this.topBorderNode.style.borderStyle =
this.rightBorderNode.style.borderStyle =
this.bottomBorderNode.style.borderStyle =
this.leftBorderNode.style.borderStyle =
this.domNode.style.borderStyle;
this.topBorderNode.style.borderColor =
this.rightBorderNode.style.borderColor =
this.bottomBorderNode.style.borderColor =
this.leftBorderNode.style.borderColor =
this.domNode.style.borderColor;
this.topBorderNode.style.borderWidth =
this.bottomBorderNode.style.borderWidth = borderWidth;
this.topBorderNode.style.borderBottomWidth = "0px";
this.bottomBorderNode.style.borderTopWidth = "0px";
this.rightBorderNode.style.borderRightWidth = this.domNode.style.borderRightWidth;
this.leftBorderNode.style.borderLeftWidth = this.domNode.style.borderLeftWidth;
this.domNode.style.borderWidth = "0px 0px 0px 0px";
}

// dojo.debug ("fillInTemplate - className = " + this.domNode.className);

// setup drag-n-drop for the sliderHandle
this._handleMove = new dojo.widget._SliderDragMoveSource (this.sliderHandleNode);
this._handleMove.setParent (this);

if (this.clickSelect){
dojo.event.connect (this.constrainingContainerNode, "onmousedown", this, "_onClick");
}

if (this.isEnableX){
this.setValueX (!isNaN(this.initialValueX) ? this.initialValueX : (!isNaN(this.minimumX) ? this.minimumX : 0));
}
if (!this.isEnableX || !this.showButtons){
this.rightButtonNode.style.width = "1px"; // allow the border to show
this.rightButtonNode.style.visibility = "hidden";
this.leftButtonNode.style.width = "1px"; // allow the border to show
this.leftButtonNode.style.visibility = "hidden";
}
if (this.isEnableY){
this.setValueY (!isNaN(this.initialValueY) ? this.initialValueY : (!isNaN(this.minimumY) ? this.minimumY : 0));
}
if (!this.isEnableY || !this.showButtons){
this.bottomButtonNode.style.width = "1px"; // allow the border to show
this.bottomButtonNode.style.visibility = "hidden";
this.topButtonNode.style.width = "1px"; // allow the border to show
this.topButtonNode.style.visibility = "hidden";
}
if(this.focusNode.addEventListener){
// dojo.event.connect() doesn't seem to work with DOMMouseScroll
this.focusNode.addEventListener('DOMMouseScroll', dojo.lang.hitch(this, "_mouseWheeled"), false); // Mozilla + Firefox + Netscape
}
},

// move the X value to the closest allowable value
_snapX: function( x){
if (x < 0){ x = 0; }
else if (x > this._constraintWidth){ x = this._constraintWidth; }
else {
var selectedValue = Math.round (x / this._valueSizeX);
x = Math.round (selectedValue * this._valueSizeX);
}
this.sliderHandleNode.style.left = x + "px";
if (this.flipX){
this._clipLeft = x + this._clipXdelta;
} else {
this._clipRight = x + this._clipXdelta;
}
this.progressBackgroundNode.style.clip = "rect("+this._clipTop+"px,"+this._clipRight+"px,"+this._clipBottom+"px,"+this._clipLeft+"px)";
},

// compute _valueSizeX & _constraintWidth & default snapValuesX
_calc_valueSizeX: function (){
var constrainingCtrBox = dojo.html.getContentBox(this.constrainingContainerNode);
var sliderHandleBox = dojo.html.getContentBox(this.sliderHandleNode);
if (isNaN(constrainingCtrBox.width) || isNaN(sliderHandleBox.width) || constrainingCtrBox.width <= 0 || sliderHandleBox.width <= 0){
return false;
}

this._constraintWidth = constrainingCtrBox.width
+ dojo.html.getPadding(this.constrainingContainerNode).width
- sliderHandleBox.width;

if (this.flipX){
this._clipLeft = this._clipRight = constrainingCtrBox.width;
} else {
this._clipLeft = this._clipRight = 0;
}
this._clipXdelta = sliderHandleBox.width >> 1;
if (!this.isEnableY){
this._clipTop = 0;
this._clipBottom = constrainingCtrBox.height;
}

if (this._constraintWidth <= 0){ return false; }
if (this.snapValuesX == 0){
this.snapValuesX = this._constraintWidth + 1;
}

this._valueSizeX = this._constraintWidth / (this.snapValuesX - 1);
return true;
},

// summary
//	move the handle horizontally to the specified value
setValueX: function ( value){
if (0.0 == this._valueSizeX){
if (this._calc_valueSizeX () == false){
dojo.lang.setTimeout(this, "setValueX", 100, value);
return;
}
}
if (isNaN(value)){
value = 0;
}
if (value > this.maximumX){
value = this.maximumX;
}
else if (value < this.minimumX){
value = this.minimumX;
}
var pixelPercent = (value-this.minimumX) / (this.maximumX-this.minimumX);
if (this.flipX){
pixelPercent = 1.0 - pixelPercent;
}
this._snapX (pixelPercent * this._constraintWidth);
this.notifyListeners();
},


// summary
//	return the X value that the matches the position of the handle
getValueX: function (){
var pixelPercent = dojo.html.getPixelValue (this.sliderHandleNode,"left") / this._constraintWidth;
if (this.flipX){
pixelPercent = 1.0 - pixelPercent;
}
return Math.round (pixelPercent * (this.snapValuesX-1)) * ((this.maximumX-this.minimumX) / (this.snapValuesX-1)) + this.minimumX;
},

// move the Y value to the closest allowable value
_snapY: function ( y){
if (y < 0){ y = 0; }
else if (y > this._constraintHeight){ y = this._constraintHeight; }
else {
var selectedValue = Math.round (y / this._valueSizeY);
y = Math.round (selectedValue * this._valueSizeY);
}
this.sliderHandleNode.style.top = y + "px";
if (this.flipY){
this._clipTop = y + this._clipYdelta;
} else {
this._clipBottom = y + this._clipYdelta;
}
this.progressBackgroundNode.style.clip = "rect("+this._clipTop+"px,"+this._clipRight+"px,"+this._clipBottom+"px,"+this._clipLeft+"px)";
},
// compute _valueSizeY & _constraintHeight & default snapValuesY
_calc_valueSizeY: function (){
var constrainingCtrBox = dojo.html.getContentBox(this.constrainingContainerNode);
var sliderHandleBox = dojo.html.getContentBox(this.sliderHandleNode);
if (isNaN(constrainingCtrBox.height) || isNaN(sliderHandleBox.height) || constrainingCtrBox.height <= 0 || sliderHandleBox.height <= 0){
return false;
}

this._constraintHeight = constrainingCtrBox.height
+ dojo.html.getPadding(this.constrainingContainerNode).height
- sliderHandleBox.height;

if (this.flipY){
this._clipTop = this._clipBottom = constrainingCtrBox.height;
} else {
this._clipTop = this._clipBottom = 0;
}
this._clipYdelta = sliderHandleBox.height >> 1;
if (!this.isEnableX){
this._clipLeft = 0;
this._clipRight = constrainingCtrBox.width;
}

if (this._constraintHeight <= 0){ return false; }
if (this.snapValuesY == 0){
this.snapValuesY = this._constraintHeight + 1;
}

this._valueSizeY = this._constraintHeight / (this.snapValuesY - 1);
return true;
},

// summary
//	move the handle vertically to the specified value
setValueY: function ( value){
if (0.0 == this._valueSizeY){
if (this._calc_valueSizeY () == false){
dojo.lang.setTimeout(this, "setValueY", 100, value);
return;
}
}
if (isNaN(value)){
value = 0;
}
if (value > this.maximumY){
value = this.maximumY;
}
else if (value < this.minimumY){
value = this.minimumY;
}
var pixelPercent = (value-this.minimumY) / (this.maximumY-this.minimumY);
if (this.flipY){
pixelPercent = 1.0 - pixelPercent;
}
this._snapY (pixelPercent * this._constraintHeight);
this.notifyListeners();
},

// summary
//	return the Y value that the matches the position of the handle
getValueY: function (){
var pixelPercent = dojo.html.getPixelValue (this.sliderHandleNode,"top") / this._constraintHeight;
if (this.flipY){
pixelPercent = 1.0 - pixelPercent;
}
return Math.round (pixelPercent * (this.snapValuesY-1)) * ((this.maximumY-this.minimumY) / (this.snapValuesY-1)) + this.minimumY;
},

// set the position of the handle
_onClick: function( evt){
if (this._isDragInProgress){
return;
}

var parent = dojo.html.getAbsolutePosition(this.constrainingContainerNode, true, dojo.html.boxSizing.MARGIN_BOX);
var content = dojo.html.getContentBox(this._handleMove.domNode);
if (this.isEnableX){
var x = evt.pageX - parent.x - (content.width >> 1);
this._snapX(x);
}
if (this.isEnableY){
var y = evt.pageY - parent.y - (content.height >> 1);
this._snapY(y);
}
this.notifyListeners();
},

// summary
//	method to invoke user's onValueChanged method
notifyListeners: function(){
this.onValueChanged(this.getValueX(), this.getValueY());
},

// summary
//	empty method to be overridden by user
onValueChanged: function( x,  y){
}
}
);





dojo.widget.defineWidget (
"dojo.widget.SliderHorizontal",
dojo.widget.Slider,
{
widgetType: "SliderHorizontal",

isEnableX: true,
isEnableY: false,
// Number
//	sets initialValueX
initialValue: "",
// Number
//	sets snapValuesX
snapValues: "",
// Number
//	sets minimumX
minimum: "",
// Number
//	sets maximumX
maximum: "",
// String
//	sets buttonStyleX
buttonStyle: "",
backgroundSize: "height:10px;width:200px;",
backgroundSrc: dojo.uri.dojoUri("src/widget/templates/images/slider-bg.gif"),
// Boolean
//	sets flipX
flip: false,

postMixInProperties: function(){
dojo.widget.SliderHorizontal.superclass.postMixInProperties.apply(this, arguments);
if (!isNaN(parseFloat(this.initialValue))){
this.initialValueX = parseFloat(this.initialValue);
}
if (!isNaN(parseFloat(this.minimum))){
this.minimumX = parseFloat(this.minimum);
}
if (!isNaN(parseFloat(this.maximum))){
this.maximumX = parseFloat(this.maximum);
}
if (!isNaN(parseInt(this.snapValues))){
this.snapValuesX = parseInt(this.snapValues);
}
if (dojo.lang.isString(this.buttonStyle) && this.buttonStyle != ""){
this.buttonStyleX = this.buttonStyle;
}
if (dojo.lang.isBoolean(this.flip)){
this.flipX = this.flip;
}
},

notifyListeners: function(){
this.onValueChanged(this.getValueX());
},

// summary
//	wrapper for getValueX()
getValue: function (){
return this.getValueX ();
},

// summary
//	wrapper for setValueX()
setValue: function ( value){
this.setValueX (value);
},

onValueChanged: function( value){
}
}
);







dojo.widget.defineWidget (
"dojo.widget.SliderVertical",
dojo.widget.Slider,
{
widgetType: "SliderVertical",

isEnableX: false,
isEnableY: true,
// Number
//	sets initialValueY
initialValue: "",
// Number
//	sets snapValuesY
snapValues: "",
// Number
//	sets minimumY
minimum: "",
// Number
//	sets maximumY
maximum: "",
// String
//	sets buttonStyleY
buttonStyle: "",
backgroundSize: "width:10px;height:200px;",
backgroundSrc: dojo.uri.dojoUri("src/widget/templates/images/slider-bg-vert.gif"),
// Boolean
//	sets flipY
flip: false,

postMixInProperties: function(){
dojo.widget.SliderVertical.superclass.postMixInProperties.apply(this, arguments);
if (!isNaN(parseFloat(this.initialValue))){
this.initialValueY = parseFloat(this.initialValue);
}
if (!isNaN(parseFloat(this.minimum))){
this.minimumY = parseFloat(this.minimum);
}
if (!isNaN(parseFloat(this.maximum))){
this.maximumY = parseFloat(this.maximum);
}
if (!isNaN(parseInt(this.snapValues))){
this.snapValuesY = parseInt(this.snapValues);
}
if (dojo.lang.isString(this.buttonStyle) && this.buttonStyle != ""){
this.buttonStyleY = this.buttonStyle;
}
if (dojo.lang.isBoolean(this.flip)){
this.flipY = this.flip;
}
},

notifyListeners: function(){
this.onValueChanged(this.getValueY());
},

// summary
//	wrapper for getValueY()
getValue: function (){
return this.getValueY ();
},

// summary
//	wrapper for setValueY()
setValue: function ( value){
this.setValueY (value);
},

onValueChanged: function( value){
}
}
);






dojo.declare (
"dojo.widget._SliderDragMoveSource",
dojo.dnd.HtmlDragMoveSource,
{
slider: null,


onDragStart: function( evt){
this.slider._isDragInProgress = true;
var pos = dojo.html.getAbsolutePosition(this.slider.constrainingContainerNode, true, dojo.html.boxSizing.MARGIN_BOX);
if (this.slider.isEnableX){
this.slider._minX = pos.x;
}
if (this.slider.isEnableY){
this.slider._minY = pos.y;
}

var dragObj = this.createDragMoveObject ();

this.slider.notifyListeners();
return dragObj;
},

onDragEnd: function( evt){
this.slider._isDragInProgress = false;
this.slider.notifyListeners();
},

createDragMoveObject: function (){
//dojo.debug ("SliderDragMoveSource#createDragMoveObject - " + this.slider);
var dragObj = new dojo.widget._SliderDragMoveObject (this.dragObject, this.type);
dragObj.slider = this.slider;

// this code copied from dojo.dnd.HtmlDragSource#onDragStart
if (this.dragClass){
dragObj.dragClass = this.dragClass;
}

return dragObj;
},


setParent: function ( slider){
this.slider = slider;
}
});






dojo.declare (
"dojo.widget._SliderDragMoveObject",
dojo.dnd.HtmlDragMoveObject,
{

slider: null,


onDragMove: function( evt){
this.updateDragOffset ();

if (this.slider.isEnableX){
var x = this.dragOffset.x + evt.pageX - this.slider._minX;
this.slider._snapX(x);
}

if (this.slider.isEnableY){
var y = this.dragOffset.y + evt.pageY - this.slider._minY;
this.slider._snapY(y);
}
if(this.slider.activeDrag){
this.slider.notifyListeners();
}
}
});
