dojo.provide("dojo.cal.iCalendar");
dojo.require("dojo.lang.common");
dojo.require("dojo.cal.textDirectory");
dojo.require("dojo.date.common");
dojo.require("dojo.date.serialize");
dojo.cal.iCalendar.fromText=function(_1){
var _2=dojo.cal.textDirectory.tokenise(_1);
var _3=[];
for(var i=0,_5=false;i<_2.length;i++){
var _6=_2[i];
if(!_5){
if(_6.name=="BEGIN"&&_6.value=="VCALENDAR"){
_5=true;
var _7=[];
}
}else{
if(_6.name=="END"&&_6.value=="VCALENDAR"){
_3.push(new dojo.cal.iCalendar.VCalendar(_7));
_5=false;
}else{
_7.push(_6);
}
}
}
return _3;
};
dojo.cal.iCalendar.Component=function(_8){
if(!this.name){
this.name="COMPONENT";
}
this.properties=[];
this.components=[];
if(_8){
for(var i=0,_a="";i<_8.length;i++){
if(_a==""){
if(_8[i].name=="BEGIN"){
_a=_8[i].value;
var _b=[];
}else{
this.addProperty(new dojo.cal.iCalendar.Property(_8[i]));
}
}else{
if(_8[i].name=="END"&&_8[i].value==_a){
if(_a=="VEVENT"){
this.addComponent(new dojo.cal.iCalendar.VEvent(_b));
}else{
if(_a=="VTIMEZONE"){
this.addComponent(new dojo.cal.iCalendar.VTimeZone(_b));
}else{
if(_a=="VTODO"){
this.addComponent(new dojo.cal.iCalendar.VTodo(_b));
}else{
if(_a=="VJOURNAL"){
this.addComponent(new dojo.cal.iCalendar.VJournal(_b));
}else{
if(_a=="VFREEBUSY"){
this.addComponent(new dojo.cal.iCalendar.VFreeBusy(_b));
}else{
if(_a=="STANDARD"){
this.addComponent(new dojo.cal.iCalendar.Standard(_b));
}else{
if(_a=="DAYLIGHT"){
this.addComponent(new dojo.cal.iCalendar.Daylight(_b));
}else{
if(_a=="VALARM"){
this.addComponent(new dojo.cal.iCalendar.VAlarm(_b));
}else{
dojo.unimplemented("dojo.cal.iCalendar."+_a);
}
}
}
}
}
}
}
}
_a="";
}else{
_b.push(_8[i]);
}
}
}
if(this._ValidProperties){
this.postCreate();
}
}
};
dojo.extend(dojo.cal.iCalendar.Component,{addProperty:function(_c){
this.properties.push(_c);
this[_c.name.toLowerCase()]=_c;
},addComponent:function(_d){
this.components.push(_d);
},postCreate:function(){
for(var x=0;x<this._ValidProperties.length;x++){
var _f=this._ValidProperties[x];
var _10=false;
for(var y=0;y<this.properties.length;y++){
var _12=this.properties[y];
var _13=_12.name.toLowerCase();
if(dojo.lang.isArray(_f)){
var _14=false;
for(var z=0;z<_f.length;z++){
var _16=_f[z].name.toLowerCase();
if((this[_16])&&(_16!=_13)){
_14=true;
}
}
if(!_14){
this[_13]=_12;
}
}else{
if(_13==_f.name.toLowerCase()){
_10=true;
if(_f.occurance==1){
this[_13]=_12;
}else{
_10=true;
if(!dojo.lang.isArray(this[_13])){
this[_13]=[];
}
this[_13].push(_12);
}
}
}
}
if(_f.required&&!_10){
dojo.debug("iCalendar - "+this.name+": Required Property not found: "+_f.name);
}
}
if(dojo.lang.isArray(this.rrule)){
for(var x=0;x<this.rrule.length;x++){
var _17=this.rrule[x].value;
this.rrule[x].cache=function(){
};
var _18=_17.split(";");
for(var y=0;y<_18.length;y++){
var _19=_18[y].split("=");
var key=_19[0].toLowerCase();
var val=_19[1];
if((key=="freq")||(key=="interval")||(key=="until")){
this.rrule[x][key]=val;
}else{
var _1c=val.split(",");
this.rrule[x][key]=_1c;
}
}
}
this.recurring=true;
}
},toString:function(){
return "[iCalendar.Component; "+this.name+", "+this.properties.length+" properties, "+this.components.length+" components]";
}});
dojo.cal.iCalendar.Property=function(_1d){
this.name=_1d.name;
this.group=_1d.group;
this.params=_1d.params;
this.value=_1d.value;
};
dojo.extend(dojo.cal.iCalendar.Property,{toString:function(){
return "[iCalenday.Property; "+this.name+": "+this.value+"]";
}});
var _P=function(n,oc,req){
return {name:n,required:(req)?true:false,occurance:(oc=="*"||!oc)?-1:oc};
};
dojo.cal.iCalendar.VCalendar=function(_21){
this.name="VCALENDAR";
this.recurring=[];
this.nonRecurringEvents=function(){
};
dojo.cal.iCalendar.Component.call(this,_21);
};
dojo.inherits(dojo.cal.iCalendar.VCalendar,dojo.cal.iCalendar.Component);
dojo.extend(dojo.cal.iCalendar.VCalendar,{addComponent:function(_22){
this.components.push(_22);
if(_22.name.toLowerCase()=="vevent"){
if(_22.rrule){
this.recurring.push(_22);
}else{
var _23=_22.getDate();
var _24=_23.getMonth()+1;
var _25=_24+"-"+_23.getDate()+"-"+_23.getFullYear();
if(!dojo.lang.isArray(this[_25])){
this.nonRecurringEvents[_25]=[];
}
this.nonRecurringEvents[_25].push(_22);
}
}
},preComputeRecurringEvents:function(_26){
var _27=function(){
};
for(var x=0;x<this.recurring.length;x++){
var _29=this.recurring[x].getDates(_26);
for(var y=0;y<_29.length;y++){
var _2b=_29[y].getMonth()+1;
var _2c=_2b+"-"+_29[y].getDate()+"-"+_29[y].getFullYear();
if(!dojo.lang.isArray(_27[_2c])){
_27[_2c]=[];
}
if(!dojo.lang.inArray(_27[_2c],this.recurring[x])){
_27[_2c].push(this.recurring[x]);
}
}
}
this.recurringEvents=_27;
},getEvents:function(_2d){
var _2e=[];
var _2f=[];
var _30=[];
var _31=_2d.getMonth()+1;
var _32=_31+"-"+_2d.getDate()+"-"+_2d.getFullYear();
if(dojo.lang.isArray(this.nonRecurringEvents[_32])){
_30=this.nonRecurringEvents[_32];
dojo.debug("Number of nonRecurring Events: "+_30.length);
}
if(dojo.lang.isArray(this.recurringEvents[_32])){
_2f=this.recurringEvents[_32];
}
_2e=_2f.concat(_30);
if(_2e.length>0){
return _2e;
}
return null;
}});
var StandardProperties=[_P("dtstart",1,true),_P("tzoffsetto",1,true),_P("tzoffsetfrom",1,true),_P("comment"),_P("rdate"),_P("rrule"),_P("tzname")];
dojo.cal.iCalendar.Standard=function(_33){
this.name="STANDARD";
this._ValidProperties=StandardProperties;
dojo.cal.iCalendar.Component.call(this,_33);
};
dojo.inherits(dojo.cal.iCalendar.Standard,dojo.cal.iCalendar.Component);
var DaylightProperties=[_P("dtstart",1,true),_P("tzoffsetto",1,true),_P("tzoffsetfrom",1,true),_P("comment"),_P("rdate"),_P("rrule"),_P("tzname")];
dojo.cal.iCalendar.Daylight=function(_34){
this.name="DAYLIGHT";
this._ValidProperties=DaylightProperties;
dojo.cal.iCalendar.Component.call(this,_34);
};
dojo.inherits(dojo.cal.iCalendar.Daylight,dojo.cal.iCalendar.Component);
var VEventProperties=[_P("class",1),_P("created",1),_P("description",1),_P("dtstart",1),_P("geo",1),_P("last-mod",1),_P("location",1),_P("organizer",1),_P("priority",1),_P("dtstamp",1),_P("seq",1),_P("status",1),_P("summary",1),_P("transp",1),_P("uid",1),_P("url",1),_P("recurid",1),[_P("dtend",1),_P("duration",1)],_P("attach"),_P("attendee"),_P("categories"),_P("comment"),_P("contact"),_P("exdate"),_P("exrule"),_P("rstatus"),_P("related"),_P("resources"),_P("rdate"),_P("rrule")];
dojo.cal.iCalendar.VEvent=function(_35){
this._ValidProperties=VEventProperties;
this.name="VEVENT";
dojo.cal.iCalendar.Component.call(this,_35);
this.recurring=false;
this.startDate=dojo.date.fromIso8601(this.dtstart.value);
};
dojo.inherits(dojo.cal.iCalendar.VEvent,dojo.cal.iCalendar.Component);
dojo.extend(dojo.cal.iCalendar.VEvent,{getDates:function(_36){
var _37=this.getDate();
var _38=[];
var _39=["su","mo","tu","we","th","fr","sa"];
var _3a={"daily":1,"weekly":2,"monthly":3,"yearly":4,"byday":1,"bymonthday":1,"byweekno":2,"bymonth":3,"byyearday":4};
for(var x=0;x<this.rrule.length;x++){
var _3c=this.rrule[x];
var _3d=_3c.freq.toLowerCase();
var _3e=1;
if(_3c.interval>_3e){
_3e=_3c.interval;
}
var set=[];
var _40=_3a[_3d];
if(_3c.until){
var _41=dojo.date.fromIso8601(_3c.until);
}else{
var _41=_36;
}
if(_41>_36){
_41=_36;
}
if(_37<_41){
var _42=function(){
};
var _43=function(){
};
_42.length=0;
_43.length=0;
switch(_3d){
case "yearly":
var _44=new Date(_37);
set.push(_44);
while(_44<_41){
_44.setYear(_44.getFullYear()+_3e);
_45=new Date(_44);
if(_45<_41){
set.push(_45);
}
}
break;
case "monthly":
_44=new Date(_37);
set.push(_44);
while(_44<_41){
_44.setMonth(_44.getMonth()+_3e);
var _45=new Date(_44);
if(_45<_41){
set.push(_45);
}
}
break;
case "weekly":
_44=new Date(_37);
set.push(_44);
while(_44<_41){
_44.setDate(_44.getDate()+(7*_3e));
var _45=new Date(_44);
if(_45<_41){
set.push(_45);
}
}
break;
case "daily":
_44=new Date(_37);
set.push(_44);
while(_44<_41){
_44.setDate(_44.getDate()+_3e);
var _45=new Date(_44);
if(_45<_41){
set.push(_45);
}
}
break;
}
if((_3c["bymonth"])&&(_3a["bymonth"]<_40)){
for(var z=0;z<_3c["bymonth"].length;z++){
if(z==0){
for(var zz=0;zz<set.length;zz++){
set[zz].setMonth(_3c["bymonth"][z]-1);
}
}else{
var _48=[];
for(var zz=0;zz<set.length;zz++){
var _49=new Date(set[zz]);
_49.setMonth(_3c[z]);
_48.push(_49);
}
tmp=set.concat(_48);
set=tmp;
}
}
}
if(_3c["byweekno"]&&!_3c["bymonth"]){
dojo.debug("TODO: no support for byweekno yet");
}
if(_3c["byyearday"]&&!_3c["bymonth"]&&!_3c["byweekno"]){
if(_3c["byyearday"].length>1){
var _4b="([+-]?)([0-9]{1,3})";
for(var z=1;x<_3c["byyearday"].length;z++){
var _4c=_3c["byyearday"][z].match(_4b);
if(z==1){
for(var zz=0;zz<set.length;zz++){
if(_4c[1]=="-"){
dojo.date.setDayOfYear(set[zz],366-_4c[2]);
}else{
dojo.date.setDayOfYear(set[zz],_4c[2]);
}
}
}else{
var _48=[];
for(var zz=0;zz<set.length;zz++){
var _49=new Date(set[zz]);
if(_4c[1]=="-"){
dojo.date.setDayOfYear(_49,366-_4c[2]);
}else{
dojo.date.setDayOfYear(_49,_4c[2]);
}
_48.push(_49);
}
tmp=set.concat(_48);
set=tmp;
}
}
}
}
if(_3c["bymonthday"]&&(_3a["bymonthday"]<_40)){
if(_3c["bymonthday"].length>0){
var _4b="([+-]?)([0-9]{1,3})";
for(var z=0;z<_3c["bymonthday"].length;z++){
var _4c=_3c["bymonthday"][z].match(_4b);
if(z==0){
for(var zz=0;zz<set.length;zz++){
if(_4c[1]=="-"){
if(_4c[2]<dojo.date.getDaysInMonth(set[zz])){
set[zz].setDate(dojo.date.getDaysInMonth(set[zz])-_4c[2]);
}
}else{
if(_4c[2]<dojo.date.getDaysInMonth(set[zz])){
set[zz].setDate(_4c[2]);
}
}
}
}else{
var _48=[];
for(var zz=0;zz<set.length;zz++){
var _49=new Date(set[zz]);
if(_4c[1]=="-"){
if(_4c[2]<dojo.date.getDaysInMonth(set[zz])){
_49.setDate(dojo.date.getDaysInMonth(set[zz])-_4c[2]);
}
}else{
if(_4c[2]<dojo.date.getDaysInMonth(set[zz])){
_49.setDate(_4c[2]);
}
}
_48.push(_49);
}
tmp=set.concat(_48);
set=tmp;
}
}
}
}
if(_3c["byday"]&&(_3a["byday"]<_40)){
if(_3c["bymonth"]){
if(_3c["byday"].length>0){
var _4b="([+-]?)([0-9]{0,1}?)([A-Za-z]{1,2})";
for(var z=0;z<_3c["byday"].length;z++){
var _4c=_3c["byday"][z].match(_4b);
var _4d=_4c[2];
var day=_4c[3].toLowerCase();
if(z==0){
for(var zz=0;zz<set.length;zz++){
if(_4c[1]=="-"){
var _4f=0;
var _50=dojo.date.getDaysInMonth(set[zz]);
var _51=1;
set[zz].setDate(_50);
if(_39[set[zz].getDay()]==day){
_4f++;
_51=7;
}
_51=1;
while(_4f<_4d){
set[zz].setDate(set[zz].getDate()-_51);
if(_39[set[zz].getDay()]==day){
_4f++;
_51=7;
}
}
}else{
if(_4d){
var _4f=0;
set[zz].setDate(1);
var _52=1;
if(_39[set[zz].getDay()]==day){
_4f++;
_52=7;
}
while(_4f<_4d){
set[zz].setDate(set[zz].getDate()+_52);
if(_39[set[zz].getDay()]==day){
_4f++;
_52=7;
}
}
}else{
var _4f=0;
var _48=[];
_50=new Date(set[zz]);
var _53=dojo.date.getDaysInMonth(set[zz]);
_50.setDate(_53);
set[zz].setDate(1);
if(_39[set[zz].getDay()]==day){
_4f++;
}
var _45=new Date(set[zz]);
_52=1;
while(_45.getDate()<_50){
if(_39[_45.getDay()]==day){
_4f++;
if(_4f==1){
set[zz]=_45;
}else{
_48.push(_45);
_45=new Date(_45);
_52=7;
_45.setDate(_45.getDate()+_52);
}
}else{
_45.setDate(_45.getDate()+_52);
}
}
var t=set.concat(_48);
set=t;
}
}
}
}else{
var _48=[];
for(var zz=0;zz<set.length;zz++){
var _49=new Date(set[zz]);
if(_4c[1]=="-"){
if(_4c[2]<dojo.date.getDaysInMonth(set[zz])){
_49.setDate(dojo.date.getDaysInMonth(set[zz])-_4c[2]);
}
}else{
if(_4c[2]<dojo.date.getDaysInMonth(set[zz])){
_49.setDate(_4c[2]);
}
}
_48.push(_49);
}
tmp=set.concat(_48);
set=tmp;
}
}
}
}else{
dojo.debug("TODO: byday within a yearly rule without a bymonth");
}
}
dojo.debug("TODO: Process BYrules for units larger than frequency");
var tmp=_38.concat(set);
_38=tmp;
}
}
_38.push(_37);
return _38;
},getDate:function(){
return dojo.date.fromIso8601(this.dtstart.value);
}});
var VTimeZoneProperties=[_P("tzid",1,true),_P("last-mod",1),_P("tzurl",1)];
dojo.cal.iCalendar.VTimeZone=function(_55){
this.name="VTIMEZONE";
this._ValidProperties=VTimeZoneProperties;
dojo.cal.iCalendar.Component.call(this,_55);
};
dojo.inherits(dojo.cal.iCalendar.VTimeZone,dojo.cal.iCalendar.Component);
var VTodoProperties=[_P("class",1),_P("completed",1),_P("created",1),_P("description",1),_P("dtstart",1),_P("geo",1),_P("last-mod",1),_P("location",1),_P("organizer",1),_P("percent",1),_P("priority",1),_P("dtstamp",1),_P("seq",1),_P("status",1),_P("summary",1),_P("uid",1),_P("url",1),_P("recurid",1),[_P("due",1),_P("duration",1)],_P("attach"),_P("attendee"),_P("categories"),_P("comment"),_P("contact"),_P("exdate"),_P("exrule"),_P("rstatus"),_P("related"),_P("resources"),_P("rdate"),_P("rrule")];
dojo.cal.iCalendar.VTodo=function(_56){
this.name="VTODO";
this._ValidProperties=VTodoProperties;
dojo.cal.iCalendar.Component.call(this,_56);
};
dojo.inherits(dojo.cal.iCalendar.VTodo,dojo.cal.iCalendar.Component);
var VJournalProperties=[_P("class",1),_P("created",1),_P("description",1),_P("dtstart",1),_P("last-mod",1),_P("organizer",1),_P("dtstamp",1),_P("seq",1),_P("status",1),_P("summary",1),_P("uid",1),_P("url",1),_P("recurid",1),_P("attach"),_P("attendee"),_P("categories"),_P("comment"),_P("contact"),_P("exdate"),_P("exrule"),_P("related"),_P("rstatus"),_P("rdate"),_P("rrule")];
dojo.cal.iCalendar.VJournal=function(_57){
this.name="VJOURNAL";
this._ValidProperties=VJournalProperties;
dojo.cal.iCalendar.Component.call(this,_57);
};
dojo.inherits(dojo.cal.iCalendar.VJournal,dojo.cal.iCalendar.Component);
var VFreeBusyProperties=[_P("contact"),_P("dtstart",1),_P("dtend"),_P("duration"),_P("organizer",1),_P("dtstamp",1),_P("uid",1),_P("url",1),_P("attendee"),_P("comment"),_P("freebusy"),_P("rstatus")];
dojo.cal.iCalendar.VFreeBusy=function(_58){
this.name="VFREEBUSY";
this._ValidProperties=VFreeBusyProperties;
dojo.cal.iCalendar.Component.call(this,_58);
};
dojo.inherits(dojo.cal.iCalendar.VFreeBusy,dojo.cal.iCalendar.Component);
var VAlarmProperties=[[_P("action",1,true),_P("trigger",1,true),[_P("duration",1),_P("repeat",1)],_P("attach",1)],[_P("action",1,true),_P("description",1,true),_P("trigger",1,true),[_P("duration",1),_P("repeat",1)]],[_P("action",1,true),_P("description",1,true),_P("trigger",1,true),_P("summary",1,true),_P("attendee","*",true),[_P("duration",1),_P("repeat",1)],_P("attach",1)],[_P("action",1,true),_P("attach",1,true),_P("trigger",1,true),[_P("duration",1),_P("repeat",1)],_P("description",1)]];
dojo.cal.iCalendar.VAlarm=function(_59){
this.name="VALARM";
this._ValidProperties=VAlarmProperties;
dojo.cal.iCalendar.Component.call(this,_59);
};
dojo.inherits(dojo.cal.iCalendar.VAlarm,dojo.cal.iCalendar.Component);
