dojo.provide("dojo.widget.Form");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.widget.defineWidget("dojo.widget.Form",dojo.widget.HtmlWidget,{isContainer:true,templateString:"<form dojoAttachPoint='containerNode' dojoAttachEvent='onSubmit:onSubmit'></form>",formElements:[],ignoreNullValues:false,postCreate:function(_1,_2){
for(var _3 in _1){
if(_3=="dojotype"){
continue;
}
var _4=document.createAttribute(_3);
_4.nodeValue=_1[_3];
this.containerNode.setAttributeNode(_4);
}
},_createRepeaters:function(_5,_6){
for(var i=0;i<_6.children.length;++i){
if(_6.children[i].widgetType=="RepeaterContainer"){
var _8=_6.children[i].index;
var _9=_8.indexOf("%{index}");
_8=_8.substr(0,_9-1);
var _a=this._getObject(_5,_8);
if(typeof (_a)=="object"&&_a.length==0){
_a=new Array();
}
var _b=_6.children[i].getRowCount();
for(var j=0,_d=_b;j<_d;++j){
_6.children[i].deleteRow(0);
}
for(var j=0;j<_a.length;j++){
_6.children[i].addRow(false);
}
}
if(_6.children[i].isContainer){
this._createRepeaters(_5,_6.children[i]);
}
}
},_createFormElements:function(){
if(dojo.render.html.safari){
this.formElements=[];
var _e=["INPUT","SELECT","TEXTAREA"];
for(var k=0;k<_e.length;k++){
var _10=this.containerNode.getElementsByTagName(_e[k]);
for(var j=0,_12=_10.length;j<_12;j++){
this.formElements.push(_10[j]);
}
}
}else{
this.formElements=this.containerNode.elements;
}
},onSubmit:function(e){
e.preventDefault();
},submit:function(){
this.containerNode.submit();
},_getFormElement:function(_14){
if(dojo.render.html.ie){
for(var i=0,len=this.formElements.length;i<len;i++){
var _17=this.formElements[i];
if(_17.name==_14){
return _17;
}
}
}else{
var _18=this.formElements[_14];
if(typeof (_18)!="undefined"){
return _18;
}
}
return null;
},_getObject:function(obj,_1a){
var _1b=[];
_1b=_1a.split(".");
var _1c=obj;
var _1d=_1b[_1b.length-1];
for(var j=0,len=_1b.length;j<len;++j){
var p=_1b[j];
if(typeof (_1c[p])=="undefined"){
_1c[p]={};
}
_1c=_1c[p];
}
return _1c;
},_setToContainers:function(obj,_22){
for(var i=0,len=_22.children.length;i<len;++i){
var _25=_22.children[i];
if(_25.widgetType=="Repeater"){
for(var j=0,len=_25.getRowCount();j<len;++j){
_25._initRow(j);
}
}
if(_25.isContainer){
this._setToContainers(obj,_25);
continue;
}
switch(_25.widgetType){
case "Checkbox":
_25.setValue(_25.inputNode.checked);
break;
case "DropdownDatePicker":
_25.setValue(_25.getValue());
break;
case "Select":
continue;
break;
case "ComboBox":
continue;
break;
default:
break;
}
}
},setValues:function(obj){
this._createFormElements();
this._createRepeaters(obj,this);
for(var i=0,len=this.formElements.length;i<len;i++){
var _2a=this.formElements[i];
if(_2a.name==""){
continue;
}
var _2b=new Array();
_2b=_2a.name.split(".");
var _2c=obj;
var _2d=_2b[_2b.length-1];
for(var j=1,_2f=_2b.length;j<_2f;++j){
var p=_2b[j-1];
if(typeof (_2c[p])=="undefined"){
_2c=undefined;
break;
}
_2c=_2c[p];
}
if(typeof (_2c)=="undefined"){
continue;
}
if(typeof (_2c[_2d])=="undefined"&&this.ignoreNullValues){
continue;
}
var _31=_2a.type;
if(_31=="hidden"||_31=="text"||_31=="textarea"||_31=="password"){
_31="text";
}
switch(_31){
case "checkbox":
_2a.checked=false;
if(typeof (_2c[_2d])=="undefined"){
continue;
}
for(var j=0,_2f=_2c[_2d].length;j<_2f;++j){
if(_2a.value==_2c[_2d][j]){
_2a.checked=true;
}
}
break;
case "radio":
_2a.checked=false;
if(typeof (_2c[_2d])=="undefined"){
continue;
}
if(_2c[_2d]==_2a.value){
_2a.checked=true;
}
break;
case "select-multiple":
_2a.selectedIndex=-1;
for(var j=0,_2f=_2a.options.length;j<_2f;++j){
for(var k=0,_33=_2c[_2d].length;k<_33;++k){
if(_2a.options[j].value==_2c[_2d][k]){
_2a.options[j].selected=true;
}
}
}
break;
case "select-one":
_2a.selectedIndex="0";
for(var j=0,_2f=_2a.options.length;j<_2f;++j){
if(_2a.options[j].value==_2c[_2d]){
_2a.options[j].selected=true;
}else{
}
}
break;
case "text":
var _34="";
if(typeof (_2c[_2d])!="undefined"){
_34=_2c[_2d];
}
_2a.value=_34;
break;
default:
dojo.debug("Not supported type ("+_31+")");
break;
}
}
this._setToContainers(obj,this);
},getValues:function(){
this._createFormElements();
var obj={};
for(var i=0,len=this.formElements.length;i<len;i++){
var elm=this.formElements[i];
var _39=[];
if(elm.name==""){
continue;
}
_39=elm.name.split(".");
var _3a=obj;
var _3b=_39[_39.length-1];
for(var j=1,_3d=_39.length;j<_3d;++j){
var _3e=null;
var p=_39[j-1];
var _40=p.split("[");
if(_40.length>1){
if(typeof (_3a[_40[0]])=="undefined"){
_3a[_40[0]]=[];
}
_3e=parseInt(_40[1]);
if(typeof (_3a[_40[0]][_3e])=="undefined"){
_3a[_40[0]][_3e]={};
}
}else{
if(typeof (_3a[_40[0]])=="undefined"){
_3a[_40[0]]={};
}
}
if(_40.length==1){
_3a=_3a[_40[0]];
}else{
_3a=_3a[_40[0]][_3e];
}
}
if((elm.type!="select-multiple"&&elm.type!="checkbox"&&elm.type!="radio")||(elm.type=="radio"&&elm.checked)){
if(_3b==_3b.split("[")[0]){
_3a[_3b]=elm.value;
}else{
}
}else{
if(elm.type=="checkbox"&&elm.checked){
if(typeof (_3a[_3b])=="undefined"){
_3a[_3b]=[];
}
_3a[_3b].push(elm.value);
}else{
if(elm.type=="select-multiple"){
if(typeof (_3a[_3b])=="undefined"){
_3a[_3b]=[];
}
for(var jdx=0,_42=elm.options.length;jdx<_42;++jdx){
if(elm.options[jdx].selected){
_3a[_3b].push(elm.options[jdx].value);
}
}
}
}
}
_3b=undefined;
}
return obj;
}});
