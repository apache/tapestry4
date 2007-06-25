dojo.provide("dojo.widget.DropdownTimePicker");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.DropdownContainer");
dojo.require("dojo.widget.TimePicker");
dojo.require("dojo.event.*");
dojo.require("dojo.html.*");
dojo.require("dojo.date.format");
dojo.require("dojo.date.serialize");
dojo.require("dojo.i18n.common");
dojo.requireLocalization("dojo.widget","DropdownTimePicker",null,"ROOT");
dojo.widget.defineWidget("dojo.widget.DropdownTimePicker",dojo.widget.DropdownContainer,{iconURL:dojo.uri.moduleUri("dojo.widget","templates/images/timeIcon.gif"),formatLength:"short",displayFormat:"",timeFormat:"",saveFormat:"",value:"",name:"",postMixInProperties:function(){
dojo.widget.DropdownTimePicker.superclass.postMixInProperties.apply(this,arguments);
var _1=dojo.i18n.getLocalization("dojo.widget","DropdownTimePicker",this.lang);
this.iconAlt=_1.selectTime;
if(typeof (this.value)=="string"&&this.value.toLowerCase()=="today"){
this.value=new Date();
}
if(this.value&&isNaN(this.value)){
var _2=this.value;
this.value=dojo.date.fromRfc3339(this.value);
if(!this.value){
var d=dojo.date.format(new Date(),{selector:"dateOnly",datePattern:"yyyy-MM-dd"});
var c=_2.split(":");
for(var i=0;i<c.length;++i){
if(c[i].length==1){
c[i]="0"+c[i];
}
}
_2=c.join(":");
this.value=dojo.date.fromRfc3339(d+"T"+_2);
dojo.deprecated("dojo.widget.DropdownTimePicker","time attributes must be passed in Rfc3339 format","0.5");
}
}
if(this.value&&!isNaN(this.value)){
this.value=new Date(this.value);
}
},fillInTemplate:function(){
dojo.widget.DropdownTimePicker.superclass.fillInTemplate.apply(this,arguments);
var _6="";
if(this.value instanceof Date){
_6=this.value;
}else{
if(this.value){
var _7=this.value;
var d=dojo.date.format(new Date(),{selector:"dateOnly",datePattern:"yyyy-MM-dd"});
var c=_7.split(":");
for(var i=0;i<c.length;++i){
if(c[i].length==1){
c[i]="0"+c[i];
}
}
_7=c.join(":");
_6=dojo.date.fromRfc3339(d+"T"+_7);
}
}
var _b={widgetContainerId:this.widgetId,lang:this.lang,value:_6};
this.timePicker=dojo.widget.createWidget("TimePicker",_b,this.containerNode,"child");
dojo.event.connect(this.timePicker,"onValueChanged",this,"_updateText");
if(this.value){
this._updateText();
}
this.containerNode.style.zIndex=this.zIndex;
this.containerNode.explodeClassName="timeContainer";
this.valueNode.name=this.name;
},getValue:function(){
return this.valueNode.value;
},getTime:function(){
return this.timePicker.storedTime;
},setValue:function(_c){
this.setTime(_c);
},setTime:function(_d){
var _e="";
if(_d instanceof Date){
_e=_d;
}else{
if(this.value){
var _f=this.value;
var d=dojo.date.format(new Date(),{selector:"dateOnly",datePattern:"yyyy-MM-dd"});
var c=_f.split(":");
for(var i=0;i<c.length;++i){
if(c[i].length==1){
c[i]="0"+c[i];
}
}
_f=c.join(":");
_e=dojo.date.fromRfc3339(d+"T"+_f);
}
}
this.timePicker.setTime(_e);
this._syncValueNode();
},_updateText:function(){
if(this.timePicker.selectedTime.anyTime){
this.inputNode.value="";
}else{
if(this.timeFormat){
dojo.deprecated("dojo.widget.DropdownTimePicker","Must use displayFormat attribute instead of timeFormat.  See dojo.date.format for specification.","0.5");
this.inputNode.value=dojo.date.strftime(this.timePicker.time,this.timeFormat,this.lang);
}else{
this.inputNode.value=dojo.date.format(this.timePicker.time,{formatLength:this.formatLength,timePattern:this.displayFormat,selector:"timeOnly",locale:this.lang});
}
}
this._syncValueNode();
this.onValueChanged(this.getTime());
this.hideContainer();
},onValueChanged:function(_13){
},onInputChange:function(){
if(this.dateFormat){
dojo.deprecated("dojo.widget.DropdownTimePicker","Cannot parse user input.  Must use displayFormat attribute instead of dateFormat.  See dojo.date.format for specification.","0.5");
}else{
var _14=dojo.string.trim(this.inputNode.value);
if(_14){
var _15=dojo.date.parse(_14,{formatLength:this.formatLength,timePattern:this.displayFormat,selector:"timeOnly",locale:this.lang});
if(_15){
this.setTime(_15);
}
}else{
this.valueNode.value=_14;
}
}
if(_14){
this._updateText();
}
},_syncValueNode:function(){
var _16=this.timePicker.time;
var _17;
switch(this.saveFormat.toLowerCase()){
case "rfc":
case "iso":
case "":
_17=dojo.date.toRfc3339(_16,"timeOnly");
break;
case "posix":
case "unix":
_17=Number(_16);
break;
default:
_17=dojo.date.format(_16,{datePattern:this.saveFormat,selector:"timeOnly",locale:this.lang});
}
this.valueNode.value=_17;
},destroy:function(_18){
this.timePicker.destroy(_18);
dojo.widget.DropdownTimePicker.superclass.destroy.apply(this,arguments);
}});
