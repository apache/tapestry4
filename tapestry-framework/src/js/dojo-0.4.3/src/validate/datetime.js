dojo.provide("dojo.validate.datetime");
dojo.require("dojo.validate.common");
dojo.validate.isValidTime=function(_1,_2){
dojo.deprecated("dojo.validate.datetime","use dojo.date.parse instead","0.5");
var re=new RegExp("^"+dojo.regexp.time(_2)+"$","i");
return re.test(_1);
};
dojo.validate.is12HourTime=function(_4){
dojo.deprecated("dojo.validate.datetime","use dojo.date.parse instead","0.5");
return dojo.validate.isValidTime(_4,{format:["h:mm:ss t","h:mm t"]});
};
dojo.validate.is24HourTime=function(_5){
dojo.deprecated("dojo.validate.datetime","use dojo.date.parse instead","0.5");
return dojo.validate.isValidTime(_5,{format:["HH:mm:ss","HH:mm"]});
};
dojo.validate.isValidDate=function(_6,_7){
dojo.deprecated("dojo.validate.datetime","use dojo.date.parse instead","0.5");
if(typeof _7=="object"&&typeof _7.format=="string"){
_7=_7.format;
}
if(typeof _7!="string"){
_7="MM/DD/YYYY";
}
var _8=_7.replace(/([$^.*+?=!:|\/\\\(\)\[\]\{\}])/g,"\\$1");
_8=_8.replace("YYYY","([0-9]{4})");
_8=_8.replace("MM","(0[1-9]|10|11|12)");
_8=_8.replace("M","([1-9]|10|11|12)");
_8=_8.replace("DDD","(00[1-9]|0[1-9][0-9]|[12][0-9][0-9]|3[0-5][0-9]|36[0-6])");
_8=_8.replace("DD","(0[1-9]|[12][0-9]|30|31)");
_8=_8.replace("D","([1-9]|[12][0-9]|30|31)");
_8=_8.replace("ww","(0[1-9]|[1-4][0-9]|5[0-3])");
_8=_8.replace("d","([1-7])");
_8="^"+_8+"$";
var re=new RegExp(_8);
if(!re.test(_6)){
return false;
}
var _a=0,_b=1,_c=1,_d=1,_e=1,_f=1;
var _10=_7.match(/(YYYY|MM|M|DDD|DD|D|ww|d)/g);
var _11=re.exec(_6);
for(var i=0;i<_10.length;i++){
switch(_10[i]){
case "YYYY":
_a=Number(_11[i+1]);
break;
case "M":
case "MM":
_b=Number(_11[i+1]);
break;
case "D":
case "DD":
_c=Number(_11[i+1]);
break;
case "DDD":
_d=Number(_11[i+1]);
break;
case "ww":
_e=Number(_11[i+1]);
break;
case "d":
_f=Number(_11[i+1]);
break;
}
}
var _13=(_a%4==0&&(_a%100!=0||_a%400==0));
if(_c==31&&(_b==4||_b==6||_b==9||_b==11)){
return false;
}
if(_c>=30&&_b==2){
return false;
}
if(_c==29&&_b==2&&!_13){
return false;
}
if(_d==366&&!_13){
return false;
}
return true;
};
