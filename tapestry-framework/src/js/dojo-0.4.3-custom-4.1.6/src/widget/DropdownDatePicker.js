dojo.provide("dojo.widget.DropdownDatePicker");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.DropdownContainer");
dojo.require("dojo.widget.DatePicker");
dojo.require("dojo.event.*");
dojo.require("dojo.html.*");
dojo.require("dojo.date.format");
dojo.require("dojo.date.serialize");
dojo.require("dojo.string.common");
dojo.require("dojo.i18n.common");
dojo.requireLocalization("dojo.widget","DropdownDatePicker",null,"ROOT");
dojo.widget.defineWidget("dojo.widget.DropdownDatePicker",dojo.widget.DropdownContainer,{iconURL:dojo.uri.moduleUri("dojo.widget","templates/images/dateIcon.gif"),formatLength:"short",displayFormat:"",saveFormat:"",value:"",name:"",displayWeeks:6,adjustWeeks:false,startDate:"1492-10-12",endDate:"2941-10-12",weekStartsOn:"",staticDisplay:false,postMixInProperties:function(_1,_2){
dojo.widget.DropdownDatePicker.superclass.postMixInProperties.apply(this,arguments);
var _3=dojo.i18n.getLocalization("dojo.widget","DropdownDatePicker",this.lang);
this.iconAlt=_3.selectDate;
if(typeof (this.value)=="string"&&this.value.toLowerCase()=="today"){
this.value=new Date();
}else{
if(this.value&&isNaN(this.value)){
var _4=this.value;
this.value=dojo.date.fromRfc3339(this.value);
}else{
if(this.value&&!isNaN(this.value)){
this.value=new Date(this.value);
}
}
}
},fillInTemplate:function(_5,_6){
dojo.widget.DropdownDatePicker.superclass.fillInTemplate.call(this,_5,_6);
var _7={widgetContainerId:this.widgetId,lang:this.lang,value:this.value,startDate:this.startDate,endDate:this.endDate,displayWeeks:this.displayWeeks,weekStartsOn:this.weekStartsOn,adjustWeeks:this.adjustWeeks,staticDisplay:this.staticDisplay};
this.datePicker=dojo.widget.createWidget("DatePicker",_7,this.containerNode,"child");
dojo.event.connect(this.datePicker,"onValueChanged",this,"_updateText");
dojo.event.connect(this.inputNode,"onChange",this,"_updateText");
if(this.value){
this._updateText();
}
this.containerNode.explodeClassName="calendarBodyContainer";
this.valueNode.name=this.name;
},getValue:function(){
return this.valueNode.value;
},getDate:function(){
return this.datePicker.value;
},setValue:function(_8){
this.setDate(_8);
},setDate:function(_9){
this.datePicker.setDate(_9);
this._syncValueNode();
},_updateText:function(){
this.inputNode.value=this.datePicker.value?dojo.date.format(this.datePicker.value,{formatLength:this.formatLength,datePattern:this.displayFormat,selector:"dateOnly",locale:this.lang}):"";
if(this.datePicker.value<this.datePicker.startDate||this.value>this.datePicker.endDate){
this.inputNode.value="";
}
this._syncValueNode();
this.onValueChanged(this.getDate());
this.hideContainer();
},onValueChanged:function(_a){
},onInputChange:function(){
var _b=dojo.string.trim(this.inputNode.value);
if(_b){
var _c=dojo.date.parse(_b,{formatLength:this.formatLength,datePattern:this.displayFormat,selector:"dateOnly",locale:this.lang});
if(!this.datePicker._isDisabledDate(_c)){
this.setDate(_c);
}
}else{
if(_b==""){
this.datePicker.setDate("");
}
this.valueNode.value=_b;
}
if(_b){
this._updateText();
}
},_syncValueNode:function(){
var _d=this.datePicker.value;
var _e="";
switch(this.saveFormat.toLowerCase()){
case "rfc":
case "iso":
case "":
_e=dojo.date.toRfc3339(_d,"dateOnly");
break;
case "posix":
case "unix":
_e=Number(_d);
break;
default:
if(_d){
_e=dojo.date.format(_d,{datePattern:this.saveFormat,selector:"dateOnly",locale:this.lang});
}
}
this.valueNode.value=_e;
},destroy:function(_f){
this.datePicker.destroy(_f);
dojo.widget.DropdownDatePicker.superclass.destroy.apply(this,arguments);
}});
