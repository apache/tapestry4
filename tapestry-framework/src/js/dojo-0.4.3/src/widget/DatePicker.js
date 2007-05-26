dojo.provide("dojo.widget.DatePicker");
dojo.require("dojo.date.common");
dojo.require("dojo.date.format");
dojo.require("dojo.date.serialize");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.event.*");
dojo.require("dojo.dom");
dojo.require("dojo.html.style");
dojo.widget.defineWidget("dojo.widget.DatePicker",dojo.widget.HtmlWidget,{value:"",name:"",displayWeeks:6,adjustWeeks:false,startDate:"1492-10-12",endDate:"2941-10-12",weekStartsOn:"",staticDisplay:false,dayWidth:"narrow",classNames:{previous:"previousMonth",disabledPrevious:"previousMonthDisabled",current:"currentMonth",disabledCurrent:"currentMonthDisabled",next:"nextMonth",disabledNext:"nextMonthDisabled",currentDate:"currentDate",selectedDate:"selectedDate"},templateString:"<div class=\"datePickerContainer\" dojoAttachPoint=\"datePickerContainerNode\">\n\t<table cellspacing=\"0\" cellpadding=\"0\" class=\"calendarContainer\">\n\t\t<thead>\n\t\t\t<tr>\n\t\t\t\t<td class=\"monthWrapper\" valign=\"top\">\n\t\t\t\t\t<table class=\"monthContainer\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\n\t\t\t\t\t\t<tr>\n\t\t\t\t\t\t\t<td class=\"monthCurve monthCurveTL\" valign=\"top\"></td>\n\t\t\t\t\t\t\t<td class=\"monthLabelContainer\" valign=\"top\">\n\t\t\t\t\t\t\t\t<span dojoAttachPoint=\"increaseWeekNode\" \n\t\t\t\t\t\t\t\t\tdojoAttachEvent=\"onClick: onIncrementWeek;\" \n\t\t\t\t\t\t\t\t\tclass=\"incrementControl increase\">\n\t\t\t\t\t\t\t\t\t<img src=\"${dojoWidgetModuleUri}templates/images/incrementMonth.png\" \n\t\t\t\t\t\t\t\t\talt=\"&darr;\" style=\"width:7px;height:5px;\" />\n\t\t\t\t\t\t\t\t</span>\n\t\t\t\t\t\t\t\t<span \n\t\t\t\t\t\t\t\t\tdojoAttachPoint=\"increaseMonthNode\" \n\t\t\t\t\t\t\t\t\tdojoAttachEvent=\"onClick: onIncrementMonth;\" class=\"incrementControl increase\">\n\t\t\t\t\t\t\t\t\t<img src=\"${dojoWidgetModuleUri}templates/images/incrementMonth.png\" \n\t\t\t\t\t\t\t\t\t\talt=\"&darr;\"  dojoAttachPoint=\"incrementMonthImageNode\">\n\t\t\t\t\t\t\t\t</span>\n\t\t\t\t\t\t\t\t<span \n\t\t\t\t\t\t\t\t\tdojoAttachPoint=\"decreaseWeekNode\" \n\t\t\t\t\t\t\t\t\tdojoAttachEvent=\"onClick: onIncrementWeek;\" \n\t\t\t\t\t\t\t\t\tclass=\"incrementControl decrease\">\n\t\t\t\t\t\t\t\t\t<img src=\"${dojoWidgetModuleUri}templates/images/decrementMonth.png\" alt=\"&uarr;\" style=\"width:7px;height:5px;\" />\n\t\t\t\t\t\t\t\t</span>\n\t\t\t\t\t\t\t\t<span \n\t\t\t\t\t\t\t\t\tdojoAttachPoint=\"decreaseMonthNode\" \n\t\t\t\t\t\t\t\t\tdojoAttachEvent=\"onClick: onIncrementMonth;\" class=\"incrementControl decrease\">\n\t\t\t\t\t\t\t\t\t<img src=\"${dojoWidgetModuleUri}templates/images/decrementMonth.png\" \n\t\t\t\t\t\t\t\t\t\talt=\"&uarr;\" dojoAttachPoint=\"decrementMonthImageNode\">\n\t\t\t\t\t\t\t\t</span>\n\t\t\t\t\t\t\t\t<span dojoAttachPoint=\"monthLabelNode\" class=\"month\"></span>\n\t\t\t\t\t\t\t</td>\n\t\t\t\t\t\t\t<td class=\"monthCurve monthCurveTR\" valign=\"top\"></td>\n\t\t\t\t\t\t</tr>\n\t\t\t\t\t</table>\n\t\t\t\t</td>\n\t\t\t</tr>\n\t\t</thead>\n\t\t<tbody>\n\t\t\t<tr>\n\t\t\t\t<td colspan=\"3\">\n\t\t\t\t\t<table class=\"calendarBodyContainer\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\n\t\t\t\t\t\t<thead>\n\t\t\t\t\t\t\t<tr dojoAttachPoint=\"dayLabelsRow\">\n\t\t\t\t\t\t\t\t<td></td>\n\t\t\t\t\t\t\t\t<td></td>\n\t\t\t\t\t\t\t\t<td></td>\n\t\t\t\t\t\t\t\t<td></td>\n\t\t\t\t\t\t\t\t<td></td>\n\t\t\t\t\t\t\t\t<td></td>\n\t\t\t\t\t\t\t\t<td></td>\n\t\t\t\t\t\t\t</tr>\n\t\t\t\t\t\t</thead>\n\t\t\t\t\t\t<tbody dojoAttachPoint=\"calendarDatesContainerNode\" \n\t\t\t\t\t\t\tdojoAttachEvent=\"onClick: _handleUiClick;\">\n\t\t\t\t\t\t\t<tr dojoAttachPoint=\"calendarWeekTemplate\">\n\t\t\t\t\t\t\t\t<td></td>\n\t\t\t\t\t\t\t\t<td></td>\n\t\t\t\t\t\t\t\t<td></td>\n\t\t\t\t\t\t\t\t<td></td>\n\t\t\t\t\t\t\t\t<td></td>\n\t\t\t\t\t\t\t\t<td></td>\n\t\t\t\t\t\t\t\t<td></td>\n\t\t\t\t\t\t\t</tr>\n\t\t\t\t\t\t</tbody>\n\t\t\t\t\t</table>\n\t\t\t\t</td>\n\t\t\t</tr>\n\t\t</tbody>\n\t\t<tfoot>\n\t\t\t<tr>\n\t\t\t\t<td colspan=\"3\" class=\"yearWrapper\">\n\t\t\t\t\t<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" class=\"yearContainer\">\n\t\t\t\t\t\t<tr>\n\t\t\t\t\t\t\t<td class=\"curveBL\" valign=\"top\"></td>\n\t\t\t\t\t\t\t<td valign=\"top\">\n\t\t\t\t\t\t\t\t<h3 class=\"yearLabel\">\n\t\t\t\t\t\t\t\t\t<span dojoAttachPoint=\"previousYearLabelNode\"\n\t\t\t\t\t\t\t\t\t\tdojoAttachEvent=\"onClick: onIncrementYear;\" class=\"previousYear\"></span>\n\t\t\t\t\t\t\t\t\t<span class=\"selectedYear\" dojoAttachPoint=\"currentYearLabelNode\"></span>\n\t\t\t\t\t\t\t\t\t<span dojoAttachPoint=\"nextYearLabelNode\" \n\t\t\t\t\t\t\t\t\t\tdojoAttachEvent=\"onClick: onIncrementYear;\" class=\"nextYear\"></span>\n\t\t\t\t\t\t\t\t</h3>\n\t\t\t\t\t\t\t</td>\n\t\t\t\t\t\t\t<td class=\"curveBR\" valign=\"top\"></td>\n\t\t\t\t\t\t</tr>\n\t\t\t\t\t</table>\n\t\t\t\t</td>\n\t\t\t</tr>\n\t\t</tfoot>\n\t</table>\n\t\n</div>\n",templateCssString:".datePickerContainer {\n\twidth:164px; /* needed for proper user styling */\n}\n\n.calendarContainer {\n/*\tborder:1px solid #566f8f;*/\n}\n\n.calendarBodyContainer {\n\twidth:100%; /* needed for the explode effect (explain?) */\n\tbackground: #7591bc url(\"images/dpBg.gif\") top left repeat-x;\n}\n\n.calendarBodyContainer thead tr td {\n\tcolor:#293a4b;\n\tfont:bold 0.75em Helvetica, Arial, Verdana, sans-serif;\n\ttext-align:center;\n\tpadding:0.25em;\n\tbackground: url(\"images/dpHorizLine.gif\") bottom left repeat-x;\n}\n\n.calendarBodyContainer tbody tr td {\n\tcolor:#fff;\n\tfont:bold 0.7em Helvetica, Arial, Verdana, sans-serif;\n\ttext-align:center;\n\tpadding:0.4em;\n\tbackground: url(\"images/dpVertLine.gif\") top right repeat-y;\n\tcursor:pointer;\n\tcursor:hand;\n}\n\n\n.monthWrapper {\n\tpadding-bottom:2px;\n\tbackground: url(\"images/dpHorizLine.gif\") bottom left repeat-x;\n}\n\n.monthContainer {\n\twidth:100%;\n}\n\n.monthLabelContainer {\n\ttext-align:center;\n\tfont:bold 0.75em Helvetica, Arial, Verdana, sans-serif;\n\tbackground: url(\"images/dpMonthBg.png\") repeat-x top left !important;\n\tcolor:#293a4b;\n\tpadding:0.25em;\n}\n\n.monthCurve {\n\twidth:12px;\n}\n\n.monthCurveTL {\n\tbackground: url(\"images/dpCurveTL.png\") no-repeat top left !important;\n}\n\n.monthCurveTR {\n\t\tbackground: url(\"images/dpCurveTR.png\") no-repeat top right !important;\n}\n\n\n.yearWrapper {\n\tbackground: url(\"images/dpHorizLineFoot.gif\") top left repeat-x;\n\tpadding-top:2px;\n}\n\n.yearContainer {\n\twidth:100%;\n}\n\n.yearContainer td {\n\tbackground:url(\"images/dpYearBg.png\") top left repeat-x;\n}\n\n.yearContainer .yearLabel {\n\tmargin:0;\n\tpadding:0.45em 0 0.45em 0;\n\tcolor:#fff;\n\tfont:bold 0.75em Helvetica, Arial, Verdana, sans-serif;\n\ttext-align:center;\n}\n\n.curveBL {\n\tbackground: url(\"images/dpCurveBL.png\") bottom left no-repeat !important;\n\twidth:9px !important;\n\tpadding:0;\n\tmargin:0;\n}\n\n.curveBR {\n\tbackground: url(\"images/dpCurveBR.png\") bottom right no-repeat !important;\n\twidth:9px !important;\n\tpadding:0;\n\tmargin:0;\n}\n\n\n.previousMonth {\n\tbackground-color:#6782a8 !important;\n}\n\n.previousMonthDisabled {\n\tbackground-color:#a4a5a6 !important;\n\tcursor:default !important\n}\n.currentMonth {\n}\n\n.currentMonthDisabled {\n\tbackground-color:#bbbbbc !important;\n\tcursor:default !important\n}\n.nextMonth {\n\tbackground-color:#6782a8 !important;\n}\n.nextMonthDisabled {\n\tbackground-color:#a4a5a6 !important;\n\tcursor:default !important;\n}\n\n.currentDate {\n\ttext-decoration:underline;\n\tfont-style:italic;\n}\n\n.selectedDate {\n\tbackground-color:#fff !important;\n\tcolor:#6782a8 !important;\n}\n\n.yearLabel .selectedYear {\n\tpadding:0.2em;\n\tbackground-color:#9ec3fb !important;\n}\n\n.nextYear, .previousYear {\n\tcursor:pointer;cursor:hand;\n\tpadding:0;\n}\n\n.nextYear {\n\tmargin:0 0 0 0.55em;\n}\n\n.previousYear {\n\tmargin:0 0.55em 0 0;\n}\n\n.incrementControl {\n\tcursor:pointer;cursor:hand;\n\twidth:1em;\n}\n\n.increase {\n\tfloat:right;\n}\n\n.decrease {\n\tfloat:left;\n}\n\n.lastColumn {\n\tbackground-image:none !important;\n}\n\n\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/DatePicker.css"),postMixInProperties:function(){
dojo.widget.DatePicker.superclass.postMixInProperties.apply(this,arguments);
if(!this.weekStartsOn){
this.weekStartsOn=dojo.date.getFirstDayOfWeek(this.lang);
}
this.today=new Date();
this.today.setHours(0,0,0,0);
if(typeof (this.value)=="string"&&this.value.toLowerCase()=="today"){
this.value=new Date();
}else{
if(this.value&&(typeof this.value=="string")&&(this.value.split("-").length>2)){
this.value=dojo.date.fromRfc3339(this.value);
this.value.setHours(0,0,0,0);
}
}
},fillInTemplate:function(_1,_2){
dojo.widget.DatePicker.superclass.fillInTemplate.apply(this,arguments);
var _3=this.getFragNodeRef(_2);
dojo.html.copyStyle(this.domNode,_3);
this.weekTemplate=dojo.dom.removeNode(this.calendarWeekTemplate);
this._preInitUI(this.value?this.value:this.today,false,true);
var _4=dojo.lang.unnest(dojo.date.getNames("days",this.dayWidth,"standAlone",this.lang));
if(this.weekStartsOn>0){
for(var i=0;i<this.weekStartsOn;i++){
_4.push(_4.shift());
}
}
var _6=this.dayLabelsRow.getElementsByTagName("td");
for(i=0;i<7;i++){
_6.item(i).innerHTML=_4[i];
}
if(this.value){
this.setValue(this.value);
}
},getValue:function(){
return dojo.date.toRfc3339(new Date(this.value),"dateOnly");
},getDate:function(){
return this.value;
},setValue:function(_7){
this.setDate(_7);
},setDate:function(_8){
var d=_8;
if(typeof (d)=="string"&&d!=""){
var t=dojo.date.fromRfc3339(d);
}else{
if(typeof (d)=="object"){
var t=new Date(d);
}else{
t="";
}
}
if(typeof (t)=="object"){
this.value=new Date(t);
this.value.setHours(0,0,0,0);
}else{
this.value="";
}
if(this.selectedNode!=null){
dojo.html.removeClass(this.selectedNode,this.classNames.selectedDate);
}
if(this.clickedNode!=null){
dojo.html.addClass(this.clickedNode,this.classNames.selectedDate);
this.selectedNode=this.clickedNode;
}else{
this._preInitUI((this.value=="")?this.curMonth:this.value,false,true);
}
this.clickedNode=null;
this.onValueChanged(this.value);
},_preInitUI:function(_b,_c,_d){
function checkDate(d,s){
if(typeof (d)=="string"){
var t=dojo.date.fromRfc3339(d);
if(t==null&&typeof (s)=="string"){
var t=dojo.date.fromRfc3339(s);
}
return t;
}
return d;
}
this.startDate=checkDate(this.startDate,"1492-10-12");
this.endDate=checkDate(this.endDate,"2941-10-12");
this.startDate.setHours(0,0,0,0);
this.endDate.setHours(24,0,0,-1);
if(_b<this.startDate||_b>this.endDate){
_b=new Date((_b<this.startDate)?this.startDate:this.endDate);
}
this.firstDay=this._initFirstDay(_b,_c);
this.selectedIsUsed=false;
this.currentIsUsed=false;
var _11=new Date(this.firstDay);
var _12=_11.getMonth();
this.curMonth=new Date(_11);
this.curMonth.setDate(_11.getDate()+6);
this.curMonth.setDate(1);
if(this.displayWeeks==""||this.adjustWeeks){
this.adjustWeeks=true;
this.displayWeeks=Math.ceil((dojo.date.getDaysInMonth(this.curMonth)+this._getAdjustedDay(this.curMonth))/7);
}
var _13=this.displayWeeks*7;
if(dojo.date.diff(this.startDate,this.endDate,dojo.date.dateParts.DAY)<_13){
this.staticDisplay=true;
if(dojo.date.diff(_11,this.endDate,dojo.date.dateParts.DAY)>_13){
this._preInitUI(this.startDate,true,false);
_11=new Date(this.firstDay);
}
this.curMonth=new Date(_11);
this.curMonth.setDate(_11.getDate()+6);
this.curMonth.setDate(1);
var _14=(_11.getMonth()==this.curMonth.getMonth())?"current":"previous";
}
if(_d){
this._initUI(_13);
}
},_initUI:function(_15){
dojo.dom.removeChildren(this.calendarDatesContainerNode);
for(var i=0;i<this.displayWeeks;i++){
this.calendarDatesContainerNode.appendChild(this.weekTemplate.cloneNode(true));
}
var _17=new Date(this.firstDay);
this._setMonthLabel(this.curMonth.getMonth());
this._setYearLabels(this.curMonth.getFullYear());
var _18=this.calendarDatesContainerNode.getElementsByTagName("td");
var _19=this.calendarDatesContainerNode.getElementsByTagName("tr");
var _1a;
for(i=0;i<_15;i++){
_1a=_18.item(i);
_1a.innerHTML=_17.getDate();
_1a.setAttribute("djDateValue",_17.valueOf());
var _1b=(_17.getMonth()!=this.curMonth.getMonth()&&Number(_17)<Number(this.curMonth))?"previous":(_17.getMonth()==this.curMonth.getMonth())?"current":"next";
var _1c=_1b;
if(this._isDisabledDate(_17)){
var _1d={previous:"disabledPrevious",current:"disabledCurrent",next:"disabledNext"};
_1c=_1d[_1b];
}
dojo.html.setClass(_1a,this._getDateClassName(_17,_1c));
if(dojo.html.hasClass(_1a,this.classNames.selectedDate)){
this.selectedNode=_1a;
}
_17=dojo.date.add(_17,dojo.date.dateParts.DAY,1);
}
this.lastDay=dojo.date.add(_17,dojo.date.dateParts.DAY,-1);
this._initControls();
},_initControls:function(){
var d=this.firstDay;
var d2=this.lastDay;
var _20,_21,_22,_23,_24,_25;
_20=_21=_22=_23=_24=_25=!this.staticDisplay;
with(dojo.date.dateParts){
var add=dojo.date.add;
if(_20&&add(d,DAY,(-1*(this._getAdjustedDay(d)+1)))<this.startDate){
_20=_22=_24=false;
}
if(_21&&d2>this.endDate){
_21=_23=_25=false;
}
if(_22&&add(d,DAY,-1)<this.startDate){
_22=_24=false;
}
if(_23&&add(d2,DAY,1)>this.endDate){
_23=_25=false;
}
if(_24&&add(d2,YEAR,-1)<this.startDate){
_24=false;
}
if(_25&&add(d,YEAR,1)>this.endDate){
_25=false;
}
}
function enableControl(_27,_28){
dojo.html.setVisibility(_27,_28?"":"hidden");
}
enableControl(this.decreaseWeekNode,_20);
enableControl(this.increaseWeekNode,_21);
enableControl(this.decreaseMonthNode,_22);
enableControl(this.increaseMonthNode,_23);
enableControl(this.previousYearLabelNode,_24);
enableControl(this.nextYearLabelNode,_25);
},_incrementWeek:function(evt){
var d=new Date(this.firstDay);
switch(evt.target){
case this.increaseWeekNode.getElementsByTagName("img").item(0):
case this.increaseWeekNode:
var _2b=dojo.date.add(d,dojo.date.dateParts.WEEK,1);
if(_2b<this.endDate){
d=dojo.date.add(d,dojo.date.dateParts.WEEK,1);
}
break;
case this.decreaseWeekNode.getElementsByTagName("img").item(0):
case this.decreaseWeekNode:
if(d>=this.startDate){
d=dojo.date.add(d,dojo.date.dateParts.WEEK,-1);
}
break;
}
this._preInitUI(d,true,true);
},_incrementMonth:function(evt){
var d=new Date(this.curMonth);
var _2e=new Date(this.firstDay);
switch(evt.currentTarget){
case this.increaseMonthNode.getElementsByTagName("img").item(0):
case this.increaseMonthNode:
_2e=dojo.date.add(_2e,dojo.date.dateParts.DAY,this.displayWeeks*7);
if(_2e<this.endDate){
d=dojo.date.add(d,dojo.date.dateParts.MONTH,1);
}else{
var _2f=true;
}
break;
case this.decreaseMonthNode.getElementsByTagName("img").item(0):
case this.decreaseMonthNode:
if(_2e>this.startDate){
d=dojo.date.add(d,dojo.date.dateParts.MONTH,-1);
}else{
var _30=true;
}
break;
}
if(_30){
d=new Date(this.startDate);
}else{
if(_2f){
d=new Date(this.endDate);
}
}
this._preInitUI(d,false,true);
},_incrementYear:function(evt){
var _32=this.curMonth.getFullYear();
var _33=new Date(this.firstDay);
switch(evt.target){
case this.nextYearLabelNode:
_33=dojo.date.add(_33,dojo.date.dateParts.YEAR,1);
if(_33<this.endDate){
_32++;
}else{
var _34=true;
}
break;
case this.previousYearLabelNode:
_33=dojo.date.add(_33,dojo.date.dateParts.YEAR,-1);
if(_33>this.startDate){
_32--;
}else{
var _35=true;
}
break;
}
var d;
if(_35){
d=new Date(this.startDate);
}else{
if(_34){
d=new Date(this.endDate);
}else{
d=new Date(_32,this.curMonth.getMonth(),1);
}
}
this._preInitUI(d,false,true);
},onIncrementWeek:function(evt){
evt.stopPropagation();
if(!this.staticDisplay){
this._incrementWeek(evt);
}
},onIncrementMonth:function(evt){
evt.stopPropagation();
if(!this.staticDisplay){
this._incrementMonth(evt);
}
},onIncrementYear:function(evt){
evt.stopPropagation();
if(!this.staticDisplay){
this._incrementYear(evt);
}
},_setMonthLabel:function(_3a){
this.monthLabelNode.innerHTML=dojo.date.getNames("months","wide","standAlone",this.lang)[_3a];
},_setYearLabels:function(_3b){
var y=_3b-1;
var _3d=this;
function f(n){
_3d[n+"YearLabelNode"].innerHTML=dojo.date.format(new Date(y++,0),{selector:"yearOnly",locale:_3d.lang});
}
f("previous");
f("current");
f("next");
},_getDateClassName:function(_3f,_40){
var _41=this.classNames[_40];
if((!this.selectedIsUsed&&this.value)&&(Number(_3f)==Number(this.value))){
_41=this.classNames.selectedDate+" "+_41;
this.selectedIsUsed=true;
}
if((!this.currentIsUsed)&&(Number(_3f)==Number(this.today))){
_41=_41+" "+this.classNames.currentDate;
this.currentIsUsed=true;
}
return _41;
},onClick:function(evt){
dojo.event.browser.stopEvent(evt);
},_handleUiClick:function(evt){
var _44=evt.target;
if(_44.nodeType!=dojo.dom.ELEMENT_NODE){
_44=_44.parentNode;
}
dojo.event.browser.stopEvent(evt);
this.selectedIsUsed=this.todayIsUsed=false;
if(dojo.html.hasClass(_44,this.classNames["disabledPrevious"])||dojo.html.hasClass(_44,this.classNames["disabledCurrent"])||dojo.html.hasClass(_44,this.classNames["disabledNext"])){
return;
}
this.clickedNode=_44;
this.setDate(new Date(Number(dojo.html.getAttribute(_44,"djDateValue"))));
},onValueChanged:function(_45){
},_isDisabledDate:function(_46){
if(_46<this.startDate||_46>this.endDate){
return true;
}
return this.isDisabledDate(_46,this.lang);
},isDisabledDate:function(_47,_48){
return false;
},_initFirstDay:function(_49,adj){
var d=new Date(_49);
if(!adj){
d.setDate(1);
}
d.setDate(d.getDate()-this._getAdjustedDay(d,this.weekStartsOn));
d.setHours(0,0,0,0);
return d;
},_getAdjustedDay:function(_4c){
var _4d=[0,1,2,3,4,5,6];
if(this.weekStartsOn>0){
for(var i=0;i<this.weekStartsOn;i++){
_4d.unshift(_4d.pop());
}
}
return _4d[_4c.getDay()];
},destroy:function(){
dojo.widget.DatePicker.superclass.destroy.apply(this,arguments);
dojo.html.destroyNode(this.weekTemplate);
}});
