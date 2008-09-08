dojo.provide("dojo.widget.MonthlyCalendar");
dojo.require("dojo.date.common");
dojo.require("dojo.date.format");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.DatePicker");
dojo.require("dojo.event.*");
dojo.require("dojo.html.*");
dojo.require("dojo.experimental");
dojo.experimental("dojo.widget.MonthlyCalendar");
dojo.widget.defineWidget("dojo.widget.MonthlyCalendar",dojo.widget.DatePicker,{dayWidth:"wide",templateString:"<div class=\"datePickerContainer\" dojoAttachPoint=\"datePickerContainerNode\">\n\t<h3 class=\"monthLabel\">\n\t<!--\n\t<span \n\t\tdojoAttachPoint=\"decreaseWeekNode\" \n\t\tdojoAttachEvent=\"onClick: onIncrementWeek;\" \n\t\tclass=\"incrementControl\">\n\t\t<img src=\"${dojoWidgetModuleUri}templates/decrementWeek.gif\" alt=\"&uarr;\" />\n\t</span>\n\t-->\n\t<span \n\t\tdojoAttachPoint=\"decreaseMonthNode\" \n\t\tdojoAttachEvent=\"onClick: onIncrementMonth;\" class=\"incrementControl\">\n\t\t<img src=\"${dojoWidgetModuleUri}templates/decrementMonth.gif\" \n\t\t\talt=\"&uarr;\" dojoAttachPoint=\"decrementMonthImageNode\">\n\t</span>\n\t<span dojoAttachPoint=\"monthLabelNode\" class=\"month\">July</span>\n\t<span \n\t\tdojoAttachPoint=\"increaseMonthNode\" \n\t\tdojoAttachEvent=\"onClick: onIncrementMonth;\" class=\"incrementControl\">\n\t\t<img src=\"${dojoWidgetModuleUri}templates/incrementMonth.gif\" \n\t\t\talt=\"&darr;\"  dojoAttachPoint=\"incrementMonthImageNode\">\n\t</span>\n\t<!--\n\t\t<span dojoAttachPoint=\"increaseWeekNode\" \n\t\t\tdojoAttachEvent=\"onClick: onIncrementWeek;\" \n\t\t\tclass=\"incrementControl\">\n\t\t\t<img src=\"${dojoWidgetModuleUri}templates/incrementWeek.gif\" \n\t\t\talt=\"&darr;\" />\n\t\t</span>\n\t-->\n\t</h3>\n\t<table class=\"calendarContainer\">\n\t\t<thead>\n\t\t\t<tr dojoAttachPoint=\"dayLabelsRow\">\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t</tr>\n\t\t</thead>\n\t\t<tbody dojoAttachPoint=\"calendarDatesContainerNode\" \n\t\t\tdojoAttachEvent=\"onClick: onSetDate;\">\n\t\t\t<tr dojoAttachPoint=\"calendarRow0\">\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t</tr>\n\t\t\t<tr dojoAttachPoint=\"calendarRow1\">\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t</tr>\n\t\t\t<tr dojoAttachPoint=\"calendarRow2\">\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t</tr>\n\t\t\t<tr dojoAttachPoint=\"calendarRow3\">\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t</tr>\n\t\t\t<tr dojoAttachPoint=\"calendarRow4\">\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t</tr>\n\t\t\t<tr dojoAttachPoint=\"calendarRow5\">\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t\t<td></td>\n\t\t\t</tr>\n\t\t</tbody>\n\t</table>\n\t<h3 class=\"yearLabel\">\n\t\t<span dojoAttachPoint=\"previousYearLabelNode\"\n\t\t\tdojoAttachEvent=\"onClick: onIncrementYear;\" class=\"previousYear\"></span>\n\t\t<span class=\"selectedYear\" dojoAttachPoint=\"currentYearLabelNode\"></span>\n\t\t<span dojoAttachPoint=\"nextYearLabelNode\" \n\t\t\tdojoAttachEvent=\"onClick: onIncrementYear;\" class=\"nextYear\"></span>\n\t</h3>\n</div>\n",templateCssString:".datePickerContainer {\n\tmargin:0.5em 2em 0.5em 0;\n\t/*width:10em;*/\n\tfloat:left;\n}\n\n.previousMonth {\n\tbackground-color:#bbbbbb;\n}\n\n.currentMonth {\n\tbackground-color:#8f8f8f;\n}\n\n.nextMonth {\n\tbackground-color:#eeeeee;\n}\n\n.currentDate {\n\ttext-decoration:underline;\n\tfont-style:italic;\n}\n\n.selectedItem {\n\tbackground-color:#3a3a3a;\n\tcolor:#ffffff;\n}\n\n.calendarContainer {\n\tborder-collapse:collapse;\n\tborder-spacing:0;\n\tborder-bottom:1px solid #e6e6e6;\n\toverflow: hidden;\n\ttext-align: right;\n}\n\n.calendarContainer thead{\n\tborder-bottom:1px solid #e6e6e6;\n}\n\n.calendarContainer tbody * td {\n        height: 100px;\n        border: 1px solid gray;\n}\n\n.calendarContainer td {\n        width: 100px;\n        padding: 2px;\n\tvertical-align: top;\n}\n\n.monthLabel {\n\tfont-size:0.9em;\n\tfont-weight:400;\n\tmargin:0;\n\ttext-align:center;\n}\n\n.monthLabel .month {\n\tpadding:0 0.4em 0 0.4em;\n}\n\n.yearLabel {\n\tfont-size:0.9em;\n\tfont-weight:400;\n\tmargin:0.25em 0 0 0;\n\ttext-align:right;\n\tcolor:#a3a3a3;\n}\n\n.yearLabel .selectedYear {\n\tcolor:#000;\n\tpadding:0 0.2em;\n}\n\n.nextYear, .previousYear {\n\tcursor:pointer;cursor:hand;\n}\n\n.incrementControl {\n\tcursor:pointer;cursor:hand;\n\twidth:1em;\n}\n\n.dojoMonthlyCalendarEvent {\n\tfont-size:0.7em;\n\toverflow: hidden;\n\tfont-color: grey;\n\twhite-space: nowrap;\n\ttext-align: left;\n}\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/MonthlyCalendar.css"),initializer:function(){
this.iCalendars=[];
},addCalendar:function(_1){
dojo.debug("Adding Calendar");
this.iCalendars.push(_1);
dojo.debug("Starting init");
this.initUI();
dojo.debug("done init");
},createDayContents:function(_2,_3){
dojo.html.removeChildren(_2);
_2.appendChild(document.createTextNode(_3.getDate()));
for(var x=0;x<this.iCalendars.length;x++){
var _5=this.iCalendars[x].getEvents(_3);
if((dojo.lang.isArray(_5))&&(_5.length>0)){
for(var y=0;y<_5.length;y++){
var el=document.createElement("div");
dojo.html.addClass(el,"dojoMonthlyCalendarEvent");
el.appendChild(document.createTextNode(_5[y].summary.value));
el.width=dojo.html.getContentBox(_2).width;
_2.appendChild(el);
}
}
}
},initUI:function(){
var _8=dojo.date.getNames("days",this.dayWidth,"standAlone",this.lang);
var _9=this.dayLabelsRow.getElementsByTagName("td");
for(var i=0;i<7;i++){
_9.item(i).innerHTML=_8[i];
}
this.selectedIsUsed=false;
this.currentIsUsed=false;
var _b="";
var _c=new Date();
var _d=this.calendarDatesContainerNode.getElementsByTagName("td");
var _e;
_c.setHours(8);
var _f=new Date(this.firstSaturday.year,this.firstSaturday.month,this.firstSaturday.date,8);
var _10=new Date(this.firstSaturday.year,this.firstSaturday.month,this.firstSaturday.date+42,8);
if(this.iCalendars.length>0){
for(var x=0;x<this.iCalendars.length;x++){
this.iCalendars[x].preComputeRecurringEvents(_10);
}
}
if(this.firstSaturday.date<7){
var _12=6;
for(var i=this.firstSaturday.date;i>0;i--){
_e=_d.item(_12);
this.createDayContents(_e,_f);
dojo.html.setClass(_e,this.getDateClassName(_f,"current"));
_12--;
_c=_f;
_f=this.incrementDate(_f,false);
}
for(var i=_12;i>-1;i--){
_e=_d.item(i);
this.createDayContents(_e,_f);
dojo.html.setClass(_e,this.getDateClassName(_f,"previous"));
_c=_f;
_f=this.incrementDate(_f,false);
}
}else{
_f.setDate(1);
for(var i=0;i<7;i++){
_e=_d.item(i);
this.createDayContents(_e,_f);
dojo.html.setClass(_e,this.getDateClassName(_f,"current"));
_c=_f;
_f=this.incrementDate(_f,true);
}
}
_c.setDate(this.firstSaturday.date);
_c.setMonth(this.firstSaturday.month);
_c.setFullYear(this.firstSaturday.year);
_f=this.incrementDate(_c,true);
var _13=7;
_e=_d.item(_13);
while((_f.getMonth()==_c.getMonth())&&(_13<42)){
this.createDayContents(_e,_f);
dojo.html.setClass(_e,this.getDateClassName(_f,"current"));
_e=_d.item(++_13);
_c=_f;
_f=this.incrementDate(_f,true);
}
while(_13<42){
this.createDayContents(_e,_f);
dojo.html.setClass(_e,this.getDateClassName(_f,"next"));
_e=_d.item(++_13);
_c=_f;
_f=this.incrementDate(_f,true);
}
this.setMonthLabel(this.firstSaturday.month);
this.setYearLabels(this.firstSaturday.year);
}});
dojo.widget.MonthlyCalendar.util=new function(){
this.toRfcDate=function(_14){
if(!_14){
_14=this.today;
}
var _15=_14.getFullYear();
var _16=_14.getMonth()+1;
if(_16<10){
_16="0"+_16.toString();
}
var _17=_14.getDate();
if(_17<10){
_17="0"+_17.toString();
}
return _15+"-"+_16+"-"+_17+"T00:00:00+00:00";
};
this.fromRfcDate=function(_18){
var _19=_18.split("-");
if(_19.length<3){
return new Date();
}
return new Date(parseInt(_19[0]),(parseInt(_19[1],10)-1),parseInt(_19[2].substr(0,2),10));
};
this.initFirstSaturday=function(_1a,_1b){
if(!_1a){
_1a=this.date.getMonth();
}
if(!_1b){
_1b=this.date.getFullYear();
}
var _1c=new Date(_1b,_1a,1);
return {year:_1b,month:_1a,date:7-_1c.getDay()};
};
};
