dojo.provide("dojo.date.common");
dojo.date.setDayOfYear=function(_1,_2){
_1.setMonth(0);
_1.setDate(_2);
return _1;
};
dojo.date.getDayOfYear=function(_3){
var _4=_3.getFullYear();
var _5=new Date(_4-1,11,31);
return Math.floor((_3.getTime()-_5.getTime())/86400000);
};
dojo.date.setWeekOfYear=function(_6,_7,_8){
if(arguments.length==1){
_8=0;
}
dojo.unimplemented("dojo.date.setWeekOfYear");
};
dojo.date.getWeekOfYear=function(_9,_a){
if(arguments.length==1){
_a=0;
}
var _b=new Date(_9.getFullYear(),0,1);
var _c=_b.getDay();
_b.setDate(_b.getDate()-_c+_a-(_c>_a?7:0));
return Math.floor((_9.getTime()-_b.getTime())/604800000);
};
dojo.date.setIsoWeekOfYear=function(_d,_e,_f){
if(arguments.length==1){
_f=1;
}
dojo.unimplemented("dojo.date.setIsoWeekOfYear");
};
dojo.date.getIsoWeekOfYear=function(_10,_11){
if(arguments.length==1){
_11=1;
}
dojo.unimplemented("dojo.date.getIsoWeekOfYear");
};
dojo.date.shortTimezones=["IDLW","BET","HST","MART","AKST","PST","MST","CST","EST","AST","NFT","BST","FST","AT","GMT","CET","EET","MSK","IRT","GST","AFT","AGTT","IST","NPT","ALMT","MMT","JT","AWST","JST","ACST","AEST","LHST","VUT","NFT","NZT","CHAST","PHOT","LINT"];
dojo.date.timezoneOffsets=[-720,-660,-600,-570,-540,-480,-420,-360,-300,-240,-210,-180,-120,-60,0,60,120,180,210,240,270,300,330,345,360,390,420,480,540,570,600,630,660,690,720,765,780,840];
dojo.date.getDaysInMonth=function(_12){
var _13=_12.getMonth();
var _14=[31,28,31,30,31,30,31,31,30,31,30,31];
if(_13==1&&dojo.date.isLeapYear(_12)){
return 29;
}else{
return _14[_13];
}
};
dojo.date.isLeapYear=function(_15){
var _16=_15.getFullYear();
return (_16%400==0)?true:(_16%100==0)?false:(_16%4==0)?true:false;
};
dojo.date.getTimezoneName=function(_17){
var str=_17.toString();
var tz="";
var _1a;
var pos=str.indexOf("(");
if(pos>-1){
pos++;
tz=str.substring(pos,str.indexOf(")"));
}else{
var pat=/([A-Z\/]+) \d{4}$/;
if((_1a=str.match(pat))){
tz=_1a[1];
}else{
str=_17.toLocaleString();
pat=/ ([A-Z\/]+)$/;
if((_1a=str.match(pat))){
tz=_1a[1];
}
}
}
return tz=="AM"||tz=="PM"?"":tz;
};
dojo.date.getOrdinal=function(_1d){
var _1e=_1d.getDate();
if(_1e%100!=11&&_1e%10==1){
return "st";
}else{
if(_1e%100!=12&&_1e%10==2){
return "nd";
}else{
if(_1e%100!=13&&_1e%10==3){
return "rd";
}else{
return "th";
}
}
}
};
dojo.date.compareTypes={DATE:1,TIME:2};
dojo.date.compare=function(_1f,_20,_21){
var dA=_1f;
var dB=_20||new Date();
var now=new Date();
with(dojo.date.compareTypes){
var opt=_21||(DATE|TIME);
var d1=new Date((opt&DATE)?dA.getFullYear():now.getFullYear(),(opt&DATE)?dA.getMonth():now.getMonth(),(opt&DATE)?dA.getDate():now.getDate(),(opt&TIME)?dA.getHours():0,(opt&TIME)?dA.getMinutes():0,(opt&TIME)?dA.getSeconds():0);
var d2=new Date((opt&DATE)?dB.getFullYear():now.getFullYear(),(opt&DATE)?dB.getMonth():now.getMonth(),(opt&DATE)?dB.getDate():now.getDate(),(opt&TIME)?dB.getHours():0,(opt&TIME)?dB.getMinutes():0,(opt&TIME)?dB.getSeconds():0);
}
if(d1.valueOf()>d2.valueOf()){
return 1;
}
if(d1.valueOf()<d2.valueOf()){
return -1;
}
return 0;
};
dojo.date.dateParts={YEAR:0,MONTH:1,DAY:2,HOUR:3,MINUTE:4,SECOND:5,MILLISECOND:6,QUARTER:7,WEEK:8,WEEKDAY:9};
dojo.date.add=function(dt,_29,_2a){
if(typeof dt=="number"){
dt=new Date(dt);
}
function fixOvershoot(){
if(sum.getDate()<dt.getDate()){
sum.setDate(0);
}
}
var sum=new Date(dt);
with(dojo.date.dateParts){
switch(_29){
case YEAR:
sum.setFullYear(dt.getFullYear()+_2a);
fixOvershoot();
break;
case QUARTER:
_2a*=3;
case MONTH:
sum.setMonth(dt.getMonth()+_2a);
fixOvershoot();
break;
case WEEK:
_2a*=7;
case DAY:
sum.setDate(dt.getDate()+_2a);
break;
case WEEKDAY:
var dat=dt.getDate();
var _2d=0;
var _2e=0;
var _2f=0;
var _30=0;
var adj=0;
var mod=_2a%5;
if(mod==0){
_2e=(_2a>0)?5:-5;
_2d=(_2a>0)?((_2a-5)/5):((_2a+5)/5);
}else{
_2e=mod;
_2d=parseInt(_2a/5);
}
_2f=dt.getDay();
if(_2f==6&&_2a>0){
adj=1;
}else{
if(_2f==0&&_2a<0){
adj=-1;
}
}
_30=(_2f+_2e);
if(_30==0||_30==6){
adj=(_2a>0)?2:-2;
}
sum.setDate(dat+(7*_2d)+_2e+adj);
break;
case HOUR:
sum.setHours(sum.getHours()+_2a);
break;
case MINUTE:
sum.setMinutes(sum.getMinutes()+_2a);
break;
case SECOND:
sum.setSeconds(sum.getSeconds()+_2a);
break;
case MILLISECOND:
sum.setMilliseconds(sum.getMilliseconds()+_2a);
break;
default:
break;
}
}
return sum;
};
dojo.date.diff=function(dtA,dtB,_35){
if(typeof dtA=="number"){
dtA=new Date(dtA);
}
if(typeof dtB=="number"){
dtB=new Date(dtB);
}
var _36=dtB.getFullYear()-dtA.getFullYear();
var _37=(dtB.getMonth()-dtA.getMonth())+(_36*12);
var _38=dtB.getTime()-dtA.getTime();
var _39=_38/1000;
var _3a=_39/60;
var _3b=_3a/60;
var _3c=_3b/24;
var _3d=_3c/7;
var _3e=0;
with(dojo.date.dateParts){
switch(_35){
case YEAR:
_3e=_36;
break;
case QUARTER:
var mA=dtA.getMonth();
var mB=dtB.getMonth();
var qA=Math.floor(mA/3)+1;
var qB=Math.floor(mB/3)+1;
qB+=(_36*4);
_3e=qB-qA;
break;
case MONTH:
_3e=_37;
break;
case WEEK:
_3e=parseInt(_3d);
break;
case DAY:
_3e=_3c;
break;
case WEEKDAY:
var _43=Math.round(_3c);
var _44=parseInt(_43/7);
var mod=_43%7;
if(mod==0){
_43=_44*5;
}else{
var adj=0;
var _47=dtA.getDay();
var _48=dtB.getDay();
_44=parseInt(_43/7);
mod=_43%7;
var _49=new Date(dtA);
_49.setDate(_49.getDate()+(_44*7));
var _4a=_49.getDay();
if(_3c>0){
switch(true){
case _47==6:
adj=-1;
break;
case _47==0:
adj=0;
break;
case _48==6:
adj=-1;
break;
case _48==0:
adj=-2;
break;
case (_4a+mod)>5:
adj=-2;
break;
default:
break;
}
}else{
if(_3c<0){
switch(true){
case _47==6:
adj=0;
break;
case _47==0:
adj=1;
break;
case _48==6:
adj=2;
break;
case _48==0:
adj=1;
break;
case (_4a+mod)<0:
adj=2;
break;
default:
break;
}
}
}
_43+=adj;
_43-=(_44*2);
}
_3e=_43;
break;
case HOUR:
_3e=_3b;
break;
case MINUTE:
_3e=_3a;
break;
case SECOND:
_3e=_39;
break;
case MILLISECOND:
_3e=_38;
break;
default:
break;
}
}
return Math.round(_3e);
};
