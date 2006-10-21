

dojo.provide("dojo.date.common");




dojo.date.setDayOfYear = function(dateObject, dayOfYear){

dateObject.setMonth(0);
dateObject.setDate(dayOfYear);
return dateObject; // Date
}

dojo.date.getDayOfYear = function(dateObject){

var fullYear = dateObject.getFullYear();
var lastDayOfPrevYear = new Date(fullYear-1, 11, 31);
return Math.floor((dateObject.getTime() -
lastDayOfPrevYear.getTime()) / 86400000); // Number
}


dojo.date.setWeekOfYear = function(dateObject, week, firstDay){
if(arguments.length == 1){ firstDay = 0; } // Sunday
dojo.unimplemented("dojo.date.setWeekOfYear");
}

dojo.date.getWeekOfYear = function(dateObject, firstDay){
if(arguments.length == 1){ firstDay = 0; } // Sunday


var firstDayOfYear = new Date(dateObject.getFullYear(), 0, 1);
var day = firstDayOfYear.getDay();
firstDayOfYear.setDate(firstDayOfYear.getDate() -
day + firstDay - (day > firstDay ? 7 : 0));

return Math.floor((dateObject.getTime() -
firstDayOfYear.getTime()) / 604800000);
}

dojo.date.setIsoWeekOfYear = function(dateObject, week, firstDay){

if (arguments.length == 1) { firstDay = 1; } // Monday
dojo.unimplemented("dojo.date.setIsoWeekOfYear");
}

dojo.date.getIsoWeekOfYear = function(dateObject, firstDay) {

if (arguments.length == 1) { firstDay = 1; } // Monday
dojo.unimplemented("dojo.date.getIsoWeekOfYear");
}





dojo.date.shortTimezones = ["IDLW", "BET", "HST", "MART", "AKST", "PST", "MST",
"CST", "EST", "AST", "NFT", "BST", "FST", "AT", "GMT", "CET", "EET", "MSK",
"IRT", "GST", "AFT", "AGTT", "IST", "NPT", "ALMT", "MMT", "JT", "AWST",
"JST", "ACST", "AEST", "LHST", "VUT", "NFT", "NZT", "CHAST", "PHOT",
"LINT"];
dojo.date.timezoneOffsets = [-720, -660, -600, -570, -540, -480, -420, -360,
-300, -240, -210, -180, -120, -60, 0, 60, 120, 180, 210, 240, 270, 300,
330, 345, 360, 390, 420, 480, 540, 570, 600, 630, 660, 690, 720, 765, 780,
840];


dojo.date.getDaysInMonth = function(dateObject){

var month = dateObject.getMonth();
var days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
if (month == 1 && dojo.date.isLeapYear(dateObject)) { return 29; } // Number
else { return days[month]; } // Number
}

dojo.date.isLeapYear = function(dateObject){









var year = dateObject.getFullYear();
return (year%400 == 0) ? true : (year%100 == 0) ? false : (year%4 == 0) ? true : false; // Boolean
}


dojo.date.getTimezoneName = function(dateObject){











var str = dateObject.toString(); // Start looking in toString
var tz = ''; // The result -- return empty string if nothing found
var match;


var pos = str.indexOf('(');
if (pos > -1) {
pos++;
tz = str.substring(pos, str.indexOf(')'));
}

else{
// If IE knows about the TZ, it appears before the year
// Capital letters or slash before a 4-digit year
// at the end of string
var pat = /([A-Z\/]+) \d{4}$/;
if((match = str.match(pat))) {
tz = match[1];
}
// Some browsers (e.g. Safari) glue the TZ on the end
// of toLocaleString instead of putting it in toString
else{
str = dateObject.toLocaleString();
// Capital letters or slash -- end of string,
// after space
pat = / ([A-Z\/]+)$/;
if((match = str.match(pat))) {
tz = match[1];
}
}
}


return tz == 'AM' || tz == 'PM' ? '' : tz; //String
}



dojo.date.getOrdinal = function(dateObject){

var date = dateObject.getDate();

if(date%100 != 11 && date%10 == 1){ return "st"; } // String
else if(date%100 != 12 && date%10 == 2){ return "nd"; } // String
else if(date%100 != 13 && date%10 == 3){ return "rd"; } // String
else{ return "th"; } // String
}



dojo.date.compareTypes={


DATE:1, TIME:2
};
dojo.date.compare=function( dateA,  dateB,  options){


var dA=dateA;
var dB=dateB||new Date();
var now=new Date();

with(dojo.date.compareTypes){
var opt=options||(DATE|TIME);
var d1=new Date(
(opt&DATE)?dA.getFullYear():now.getFullYear(),
(opt&DATE)?dA.getMonth():now.getMonth(),
(opt&DATE)?dA.getDate():now.getDate(),
(opt&TIME)?dA.getHours():0,
(opt&TIME)?dA.getMinutes():0,
(opt&TIME)?dA.getSeconds():0
);
var d2=new Date(
(opt&DATE)?dB.getFullYear():now.getFullYear(),
(opt&DATE)?dB.getMonth():now.getMonth(),
(opt&DATE)?dB.getDate():now.getDate(),
(opt&TIME)?dB.getHours():0,
(opt&TIME)?dB.getMinutes():0,
(opt&TIME)?dB.getSeconds():0
);
}
if(d1.valueOf()>d2.valueOf()){
return 1;	//	int
}
if(d1.valueOf()<d2.valueOf()){
return -1;	//	int
}
return 0;	//	int
}

dojo.date.dateParts={


YEAR:0, MONTH:1, DAY:2, HOUR:3, MINUTE:4, SECOND:5, MILLISECOND:6, QUARTER:7, WEEK:8, WEEKDAY:9
};

dojo.date.add = function( dt,  interv,  incr){












if(typeof dt == 'number'){dt = new Date(dt);} // Allow timestamps


function fixOvershoot(){
if (sum.getDate() < dt.getDate()){
sum.setDate(0);
}
}

var sum = new Date(dt);

with(dojo.date.dateParts){
switch(interv){
case YEAR:
sum.setFullYear(dt.getFullYear()+incr);
// Keep increment/decrement from 2/29 out of March
fixOvershoot();
break;
case QUARTER:
// Naive quarter is just three months
incr*=3;
// fallthrough...
case MONTH:
sum.setMonth(dt.getMonth()+incr);
// Reset to last day of month if you overshoot
fixOvershoot();
break;
case WEEK:
incr*=7;
// fallthrough...
case DAY:
sum.setDate(dt.getDate() + incr);
break;
case WEEKDAY:
//FIXME: assumes Saturday/Sunday weekend, but even this is not fixed.  There are CLDR entries to localize this.
var dat = dt.getDate();
var weeks = 0;
var days = 0;
var strt = 0;
var trgt = 0;
var adj = 0;
// Divide the increment time span into weekspans plus leftover days
// e.g., 8 days is one 5-day weekspan / and two leftover days
// Can't have zero leftover days, so numbers divisible by 5 get
// a days value of 5, and the remaining days make up the number of weeks
var mod = incr % 5;
if (mod == 0) {
days = (incr > 0) ? 5 : -5;
weeks = (incr > 0) ? ((incr-5)/5) : ((incr+5)/5);
}
else {
days = mod;
weeks = parseInt(incr/5);
}
// Get weekday value for orig date param
strt = dt.getDay();
// Orig date is Sat / positive incrementer
// Jump over Sun
if (strt == 6 && incr > 0) {
adj = 1;
}
// Orig date is Sun / negative incrementer
// Jump back over Sat
else if (strt == 0 && incr < 0) {
adj = -1;
}
// Get weekday val for the new date
trgt = (strt + days);
// New date is on Sat or Sun
if (trgt == 0 || trgt == 6) {
adj = (incr > 0) ? 2 : -2;
}
// Increment by number of weeks plus leftover days plus
// weekend adjustments
sum.setDate(dat + (7*weeks) + days + adj);
break;
case HOUR:
sum.setHours(sum.getHours()+incr);
break;
case MINUTE:
sum.setMinutes(sum.getMinutes()+incr);
break;
case SECOND:
sum.setSeconds(sum.getSeconds()+incr);
break;
case MILLISECOND:
sum.setMilliseconds(sum.getMilliseconds()+incr);
break;
default:
// Do nothing
break;
}
}

return sum; // Date
};

dojo.date.diff = function( dtA,  dtB,  interv){














if(typeof dtA == 'number'){dtA = new Date(dtA);}
if(typeof dtB == 'number'){dtB = new Date(dtB);}
var yeaDiff = dtB.getFullYear() - dtA.getFullYear();
var monDiff = (dtB.getMonth() - dtA.getMonth()) + (yeaDiff * 12);
var msDiff = dtB.getTime() - dtA.getTime(); // Millisecs
var secDiff = msDiff/1000;
var minDiff = secDiff/60;
var houDiff = minDiff/60;
var dayDiff = houDiff/24;
var weeDiff = dayDiff/7;
var delta = 0; // Integer return value

with(dojo.date.dateParts){
switch(interv){
case YEAR:
delta = yeaDiff;
break;
case QUARTER:
var mA = dtA.getMonth();
var mB = dtB.getMonth();
// Figure out which quarter the months are in
var qA = Math.floor(mA/3) + 1;
var qB = Math.floor(mB/3) + 1;
// Add quarters for any year difference between the dates
qB += (yeaDiff * 4);
delta = qB - qA;
break;
case MONTH:
delta = monDiff;
break;
case WEEK:
// Truncate instead of rounding
// Don't use Math.floor -- value may be negative
delta = parseInt(weeDiff);
break;
case DAY:
delta = dayDiff;
break;
case WEEKDAY:
var days = Math.round(dayDiff);
var weeks = parseInt(days/7);
var mod = days % 7;

// Even number of weeks
if (mod == 0) {
days = weeks*5;
}
// Weeks plus spare change (< 7 days)
else {
var adj = 0;
var aDay = dtA.getDay();
var bDay = dtB.getDay();

weeks = parseInt(days/7);
mod = days % 7;
// Mark the date advanced by the number of
// round weeks (may be zero)
var dtMark = new Date(dtA);
dtMark.setDate(dtMark.getDate()+(weeks*7));
var dayMark = dtMark.getDay();
// Spare change days -- 6 or less
// ----------
// Positive diff
if (dayDiff > 0) {
switch (true) {
// Range starts on Sat
case aDay == 6:
adj = -1;
break;
// Range starts on Sun
case aDay == 0:
adj = 0;
break;
// Range ends on Sat
case bDay == 6:
adj = -1;
break;
// Range ends on Sun
case bDay == 0:
adj = -2;
break;
// Range contains weekend
case (dayMark + mod) > 5:
adj = -2;
break;
default:
// Do nothing
break;
}
}
// Negative diff
else if (dayDiff < 0) {
switch (true) {
// Range starts on Sat
case aDay == 6:
adj = 0;
break;
// Range starts on Sun
case aDay == 0:
adj = 1;
break;
// Range ends on Sat
case bDay == 6:
adj = 2;
break;
// Range ends on Sun
case bDay == 0:
adj = 1;
break;
// Range contains weekend
case (dayMark + mod) < 0:
adj = 2;
break;
default:
// Do nothing
break;
}
}
days += adj;
days -= (weeks*2);
}
delta = days;

break;
case HOUR:
delta = houDiff;
break;
case MINUTE:
delta = minDiff;
break;
case SECOND:
delta = secDiff;
break;
case MILLISECOND:
delta = msDiff;
break;
default:
// Do nothing
break;
}
}


return Math.round(delta); // Number (integer)
};
