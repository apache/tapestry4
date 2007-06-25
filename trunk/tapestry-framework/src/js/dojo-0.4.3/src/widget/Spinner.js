dojo.provide("dojo.widget.Spinner");
dojo.require("dojo.io.*");
dojo.require("dojo.lfx.*");
dojo.require("dojo.html.*");
dojo.require("dojo.html.layout");
dojo.require("dojo.string");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.IntegerTextbox");
dojo.require("dojo.widget.RealNumberTextbox");
dojo.require("dojo.widget.DateTextbox");
dojo.require("dojo.experimental");
dojo.declare("dojo.widget.Spinner",null,{_typamaticTimer:null,_typamaticFunction:null,_currentTimeout:this.defaultTimeout,_eventCount:0,defaultTimeout:500,timeoutChangeRate:0.9,templateString:"<span _=\"weird end tag formatting is to prevent whitespace from becoming &nbsp;\"\n\tstyle='float:${this.htmlfloat};'\n\t><table cellpadding=0 cellspacing=0 class=\"dojoSpinner\">\n\t\t<tr>\n\t\t\t<td\n\t\t\t\t><input\n\t\t\t\t\tdojoAttachPoint='textbox' type='${this.type}'\n\t\t\t\t\tdojoAttachEvent='onblur;onfocus;onkey:_handleKeyEvents;onKeyUp:_onSpinnerKeyUp;onresize:_resize'\n\t\t\t\t\tid='${this.widgetId}' name='${this.name}' size='${this.size}' maxlength='${this.maxlength}'\n\t\t\t\t\tvalue='${this.value}' class='${this.className}' autocomplete=\"off\"\n\t\t\t></td>\n\t\t\t<td\n\t\t\t\t><img dojoAttachPoint=\"upArrowNode\"\n\t\t\t\t\tdojoAttachEvent=\"onDblClick: _upArrowDoubleClicked;  onMouseDown: _upArrowPressed; onMouseUp: _arrowReleased; onMouseOut: _arrowReleased; onMouseMove: _discardEvent;\"\n\t\t\t\t\tsrc=\"${this.incrementSrc}\" style=\"width: ${this.buttonSize.width}px; height: ${this.buttonSize.height}px;\"\n\t\t\t\t><img dojoAttachPoint=\"downArrowNode\"\n\t\t\t\t\tdojoAttachEvent=\"onDblClick: _downArrowDoubleClicked;  onMouseDown: _downArrowPressed; onMouseUp: _arrowReleased; onMouseOut: _arrowReleased; onMouseMove: _discardEvent;\"\n\t\t\t\t\tsrc=\"${this.decrementSrc}\" style=\"width: ${this.buttonSize.width}px; height: ${this.buttonSize.height}px;\"\n\t\t\t></td>\n\t\t</tr>\n\t</table\n\t><span dojoAttachPoint='invalidSpan' class='${this.invalidClass}'>${this.messages.invalidMessage}</span\n\t><span dojoAttachPoint='missingSpan' class='${this.missingClass}'>${this.messages.missingMessage}</span\n\t><span dojoAttachPoint='rangeSpan' class='${this.rangeClass}'>${this.messages.rangeMessage}</span\n></span>\n",templateCssString:"/* inline the table holding the <input> and buttons (method varies by browser) */\n.ie .dojoSpinner, .safari .dojoSpinner {\n\tdisplay: inline;\n}\n\n.moz .dojoSpinner {\n\tdisplay: -moz-inline-box;\n}\n\n.opera .dojoSpinner {\n\tdisplay: inline-table;\n}\n\n/* generic stuff for the table */\n.dojoSpinner td {\n\tpadding:0px;\n\tmargin:0px;\n\tvertical-align: middle;\n}\ntable.dojoSpinner {\n\tborder:0px;\n\tborder-spacing:0px;\n\tline-height:0px;\n\tpadding:0px;\n\tmargin: 0px;\n\tvertical-align: middle;\n}\n\n/* the buttons */\n.dojoSpinner img {\n\tdisplay: block;\n\tborder-width:0px 1px 1px 0px;\n\tborder-style:outset;\n}\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/Spinner.css"),incrementSrc:dojo.uri.moduleUri("dojo.widget","templates/images/spinnerIncrement.gif"),decrementSrc:dojo.uri.moduleUri("dojo.widget","templates/images/spinnerDecrement.gif"),_handleKeyEvents:function(_1){
if(!_1.key){
return;
}
if(!_1.ctrlKey&&!_1.altKey){
switch(_1.key){
case _1.KEY_DOWN_ARROW:
dojo.event.browser.stopEvent(_1);
this._downArrowPressed(_1);
return;
case _1.KEY_UP_ARROW:
dojo.event.browser.stopEvent(_1);
this._upArrowPressed(_1);
return;
}
}
this._eventCount++;
},_onSpinnerKeyUp:function(_2){
this._arrowReleased(_2);
this.onkeyup(_2);
},_resize:function(){
var _3=dojo.html.getBorderBox(this.textbox);
this.buttonSize={width:_3.height/2,height:_3.height/2};
if(this.upArrowNode){
dojo.html.setMarginBox(this.upArrowNode,this.buttonSize);
dojo.html.setMarginBox(this.downArrowNode,this.buttonSize);
}
},_pressButton:function(_4){
_4.style.borderWidth="1px 0px 0px 1px";
_4.style.borderStyle="inset";
},_releaseButton:function(_5){
_5.style.borderWidth="0px 1px 1px 0px";
_5.style.borderStyle="outset";
},_arrowPressed:function(_6,_7){
var _8=(_7==-1)?this.downArrowNode:this.upArrowNode;
var _9=(_7==+1)?this.downArrowNode:this.upArrowNode;
if(typeof _6!="number"){
if(this._typamaticTimer!=null){
if(this._typamaticNode==_8){
return;
}
dojo.lang.clearTimeout(this._typamaticTimer);
}
this._releaseButton(_9);
this._eventCount++;
this._typamaticTimer=null;
this._currentTimeout=this.defaultTimeout;
}else{
if(_6!=this._eventCount){
this._releaseButton(_8);
return;
}
}
this._pressButton(_8);
this._setCursorX(this.adjustValue(_7,this._getCursorX()));
this._typamaticNode=_8;
this._typamaticTimer=dojo.lang.setTimeout(this,"_arrowPressed",this._currentTimeout,this._eventCount,_7);
this._currentTimeout=Math.round(this._currentTimeout*this.timeoutChangeRate);
},_downArrowPressed:function(_a){
return this._arrowPressed(_a,-1);
},_downArrowDoubleClicked:function(_b){
var rc=this._downArrowPressed(_b);
dojo.lang.setTimeout(this,"_arrowReleased",50,null);
return rc;
},_upArrowPressed:function(_d){
return this._arrowPressed(_d,+1);
},_upArrowDoubleClicked:function(_e){
var rc=this._upArrowPressed(_e);
dojo.lang.setTimeout(this,"_arrowReleased",50,null);
return rc;
},_arrowReleased:function(evt){
this.textbox.focus();
if(evt!=null&&typeof evt=="object"&&evt.keyCode&&evt.keyCode!=null){
var _11=evt.keyCode;
var k=dojo.event.browser.keys;
switch(_11){
case k.KEY_DOWN_ARROW:
case k.KEY_UP_ARROW:
dojo.event.browser.stopEvent(evt);
break;
}
}
this._releaseButton(this.upArrowNode);
this._releaseButton(this.downArrowNode);
this._eventCount++;
if(this._typamaticTimer!=null){
dojo.lang.clearTimeout(this._typamaticTimer);
}
this._typamaticTimer=null;
this._currentTimeout=this.defaultTimeout;
},_mouseWheeled:function(evt){
var _14=0;
if(typeof evt.wheelDelta=="number"){
_14=evt.wheelDelta;
}else{
if(typeof evt.detail=="number"){
_14=-evt.detail;
}
}
if(_14>0){
this._upArrowPressed(evt);
this._arrowReleased(evt);
}else{
if(_14<0){
this._downArrowPressed(evt);
this._arrowReleased(evt);
}
}
},_discardEvent:function(evt){
dojo.event.browser.stopEvent(evt);
},_getCursorX:function(){
var x=-1;
try{
this.textbox.focus();
if(typeof this.textbox.selectionEnd=="number"){
x=this.textbox.selectionEnd;
}else{
if(document.selection&&document.selection.createRange){
var _17=document.selection.createRange().duplicate();
if(_17.parentElement()==this.textbox){
_17.moveStart("textedit",-1);
x=_17.text.length;
}
}
}
}
catch(e){
}
return x;
},_setCursorX:function(x){
try{
this.textbox.focus();
if(!x){
x=0;
}
if(typeof this.textbox.selectionEnd=="number"){
this.textbox.selectionEnd=x;
}else{
if(this.textbox.createTextRange){
var _19=this.textbox.createTextRange();
_19.collapse(true);
_19.moveEnd("character",x);
_19.moveStart("character",x);
_19.select();
}
}
}
catch(e){
}
},_spinnerPostMixInProperties:function(_1a,_1b){
var _1c=this.getFragNodeRef(_1b);
var _1d=dojo.html.getBorderBox(_1c);
this.buttonSize={width:_1d.height/2-1,height:_1d.height/2-1};
},_spinnerPostCreate:function(_1e,_1f){
if(this.textbox.addEventListener){
this.textbox.addEventListener("DOMMouseScroll",dojo.lang.hitch(this,"_mouseWheeled"),false);
}else{
dojo.event.connect(this.textbox,"onmousewheel",this,"_mouseWheeled");
}
}});
dojo.widget.defineWidget("dojo.widget.IntegerSpinner",[dojo.widget.IntegerTextbox,dojo.widget.Spinner],{delta:"1",postMixInProperties:function(_20,_21){
dojo.widget.IntegerSpinner.superclass.postMixInProperties.apply(this,arguments);
this._spinnerPostMixInProperties(_20,_21);
},postCreate:function(_22,_23){
dojo.widget.IntegerSpinner.superclass.postCreate.apply(this,arguments);
this._spinnerPostCreate(_22,_23);
},adjustValue:function(_24,x){
var val=this.getValue().replace(/[^\-+\d]/g,"");
if(val.length==0){
return;
}
var num=Math.min(Math.max((parseInt(val)+(parseInt(this.delta)*_24)),(this.flags.min?this.flags.min:-Infinity)),(this.flags.max?this.flags.max:+Infinity));
val=num.toString();
if(num>=0){
val=((this.flags.signed==true)?"+":" ")+val;
}
if(this.flags.separator.length>0){
for(var i=val.length-3;i>1;i-=3){
val=val.substr(0,i)+this.flags.separator+val.substr(i);
}
}
if(val.substr(0,1)==" "){
val=val.substr(1);
}
this.setValue(val);
return val.length;
}});
dojo.widget.defineWidget("dojo.widget.RealNumberSpinner",[dojo.widget.RealNumberTextbox,dojo.widget.Spinner],function(){
dojo.experimental("dojo.widget.RealNumberSpinner");
},{delta:"1e1",postMixInProperties:function(_29,_2a){
dojo.widget.RealNumberSpinner.superclass.postMixInProperties.apply(this,arguments);
this._spinnerPostMixInProperties(_29,_2a);
},postCreate:function(_2b,_2c){
dojo.widget.RealNumberSpinner.superclass.postCreate.apply(this,arguments);
this._spinnerPostCreate(_2b,_2c);
},adjustValue:function(_2d,x){
var val=this.getValue().replace(/[^\-+\.eE\d]/g,"");
if(!val.length){
return;
}
var num=parseFloat(val);
if(isNaN(num)){
return;
}
var _31=this.delta.split(/[eE]/);
if(!_31.length){
_31=[1,1];
}else{
_31[0]=parseFloat(_31[0].replace(/[^\-+\.\d]/g,""));
if(isNaN(_31[0])){
_31[0]=1;
}
if(_31.length>1){
_31[1]=parseInt(_31[1]);
}
if(isNaN(_31[1])){
_31[1]=1;
}
}
val=this.getValue().split(/[eE]/);
if(!val.length){
return;
}
var _32=parseFloat(val[0].replace(/[^\-+\.\d]/g,""));
if(val.length==1){
var _33=0;
}else{
var _33=parseInt(val[1].replace(/[^\-+\d]/g,""));
}
if(x<=val[0].length){
x=0;
_32+=_31[0]*_2d;
}else{
x=Number.MAX_VALUE;
_33+=_31[1]*_2d;
if(this.flags.eSigned==false&&_33<0){
_33=0;
}
}
num=Math.min(Math.max((_32*Math.pow(10,_33)),(this.flags.min?this.flags.min:-Infinity)),(this.flags.max?this.flags.max:+Infinity));
if((this.flags.exponent==true||(this.flags.exponent!=false&&x!=0))&&num.toExponential){
if(isNaN(this.flags.places)||this.flags.places==Infinity){
val=num.toExponential();
}else{
val=num.toExponential(this.flags.places);
}
}else{
if(num.toFixed&&num.toPrecision){
if(isNaN(this.flags.places)||this.flags.places==Infinity){
val=num.toPrecision((1/3).toString().length-1);
}else{
val=num.toFixed(this.flags.places);
}
}else{
val=num.toString();
}
}
if(num>=0){
if(this.flags.signed==true){
val="+"+val;
}
}
val=val.split(/[eE]/);
if(this.flags.separator.length>0){
if(num>=0&&val[0].substr(0,1)!="+"){
val[0]=" "+val[0];
}
var i=val[0].lastIndexOf(".");
if(i>=0){
i-=3;
}else{
i=val[0].length-3;
}
for(;i>1;i-=3){
val[0]=val[0].substr(0,i)+this.flags.separator+val[0].substr(i);
}
if(val[0].substr(0,1)==" "){
val[0]=val[0].substr(1);
}
}
if(val.length>1){
if((this.flags.eSigned==true)&&(val[1].substr(0,1)!="+")){
val[1]="+"+val[1];
}else{
if((!this.flags.eSigned)&&(val[1].substr(0,1)=="+")){
val[1]=val[1].substr(1);
}else{
if((!this.flags.eSigned)&&(val[1].substr(0,1)=="-")&&(num.toFixed&&num.toPrecision)){
if(isNaN(this.flags.places)){
val[0]=num.toPrecision((1/3).toString().length-1);
}else{
val[0]=num.toFixed(this.flags.places).toString();
}
val[1]="0";
}
}
}
val[0]+="e"+val[1];
}
this.setValue(val[0]);
if(x>val[0].length){
x=val[0].length;
}
return x;
}});
dojo.widget.defineWidget("dojo.widget.TimeSpinner",[dojo.widget.TimeTextbox,dojo.widget.Spinner],function(){
dojo.experimental("dojo.widget.TimeSpinner");
},{postMixInProperties:function(_35,_36){
dojo.widget.TimeSpinner.superclass.postMixInProperties.apply(this,arguments);
this._spinnerPostMixInProperties(_35,_36);
},postCreate:function(_37,_38){
dojo.widget.TimeSpinner.superclass.postCreate.apply(this,arguments);
this._spinnerPostCreate(_37,_38);
},adjustValue:function(_39,x){
var val=this.getValue();
var _3c=(this.flags.format&&this.flags.format.search(/[Hhmst]/)>=0)?this.flags.format:"hh:mm:ss t";
if(_39==0||!val.length||!this.isValid()){
return;
}
if(!this.flags.amSymbol){
this.flags.amSymbol="AM";
}
if(!this.flags.pmSymbol){
this.flags.pmSymbol="PM";
}
var re=dojo.regexp.time(this.flags);
var _3e=_3c.replace(/H/g,"h").replace(/[^hmst]/g,"").replace(/([hmst])\1/g,"$1");
var _3f=_3e.indexOf("h")+1;
var _40=_3e.indexOf("m")+1;
var _41=_3e.indexOf("s")+1;
var _42=_3e.indexOf("t")+1;
var _43=_3c;
var _44="";
if(_42>0){
_44=val.replace(new RegExp(re),"$"+_42);
_43=_43.replace(/t+/,_44.replace(/./g,"t"));
}
var _45=0;
var _46=1;
if(_3f>0){
_45=val.replace(new RegExp(re),"$"+_3f);
if(dojo.lang.isString(this.delta)){
_46=this.delta.replace(new RegExp(re),"$"+_3f);
}
if(isNaN(_46)){
_46=1;
}else{
_46=parseInt(_46);
}
if(_45.length==2){
_43=_43.replace(/([Hh])+/,"$1$1");
}else{
_43=_43.replace(/([Hh])+/,"$1");
}
if(isNaN(_45)){
_45=0;
}else{
_45=parseInt(_45.replace(/^0(\d)/,"$1"));
}
}
var min=0;
var _48=1;
if(_40>0){
min=val.replace(new RegExp(re),"$"+_40);
if(dojo.lang.isString(this.delta)){
_48=this.delta.replace(new RegExp(re),"$"+_40);
}
if(isNaN(_48)){
_48=1;
}else{
_48=parseInt(_48);
}
_43=_43.replace(/m+/,min.replace(/./g,"m"));
if(isNaN(min)){
min=0;
}else{
min=parseInt(min.replace(/^0(\d)/,"$1"));
}
}
var sec=0;
var _4a=1;
if(_41>0){
sec=val.replace(new RegExp(re),"$"+_41);
if(dojo.lang.isString(this.delta)){
_4a=this.delta.replace(new RegExp(re),"$"+_41);
}
if(isNaN(_4a)){
_4a=1;
}else{
_4a=parseInt(_4a);
}
_43=_43.replace(/s+/,sec.replace(/./g,"s"));
if(isNaN(sec)){
sec=0;
}else{
sec=parseInt(sec.replace(/^0(\d)/,"$1"));
}
}
if(isNaN(x)||x>=_43.length){
x=_43.length-1;
}
var _4b=_43.charAt(x);
switch(_4b){
case "t":
if(_44==this.flags.amSymbol){
_44=this.flags.pmSymbol;
}else{
if(_44==this.flags.pmSymbol){
_44=this.flags.amSymbol;
}
}
break;
default:
if(_45>=1&&_45<12&&_44==this.flags.pmSymbol){
_45+=12;
}
if(_45==12&&_44==this.flags.amSymbol){
_45=0;
}
switch(_4b){
case "s":
sec+=_4a*_39;
while(sec<0){
min--;
sec+=60;
}
while(sec>=60){
min++;
sec-=60;
}
case "m":
if(_4b=="m"){
min+=_48*_39;
}
while(min<0){
_45--;
min+=60;
}
while(min>=60){
_45++;
min-=60;
}
case "h":
case "H":
if(_4b=="h"||_4b=="H"){
_45+=_46*_39;
}
while(_45<0){
_45+=24;
}
while(_45>=24){
_45-=24;
}
break;
default:
return;
}
if(_45>=12){
_44=this.flags.pmSymbol;
if(_3c.indexOf("h")>=0&&_45>=13){
_45-=12;
}
}else{
_44=this.flags.amSymbol;
if(_3c.indexOf("h")>=0&&_45==0){
_45=12;
}
}
}
_43=_3c;
if(_45>=0&&_45<10&&_3c.search(/[hH]{2}/)>=0){
_45="0"+_45.toString();
}
if(_45>=10&&_43.search(/[hH]{2}/)<0){
_43=_43.replace(/(h|H)/,"$1$1");
}
if(min>=0&&min<10&&_43.search(/mm/)>=0){
min="0"+min.toString();
}
if(min>=10&&_43.search(/mm/)<0){
_43=_43.replace(/m/,"$1$1");
}
if(sec>=0&&sec<10&&_43.search(/ss/)>=0){
sec="0"+sec.toString();
}
if(sec>=10&&_43.search(/ss/)<0){
_43=_43.replace(/s/,"$1$1");
}
x=_43.indexOf(_4b);
if(x==-1){
x=_3c.length;
}
_3c=_3c.replace(/[hH]+/,_45);
_3c=_3c.replace(/m+/,min);
_3c=_3c.replace(/s+/,sec);
_3c=_3c.replace(/t/,_44);
this.setValue(_3c);
if(x>_3c.length){
x=_3c.length;
}
return x;
}});
