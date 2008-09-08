dojo.provide("dojo.date.serialize");
dojo.require("dojo.string.common");
dojo.date.setIso8601=function(_1,_2){
var _3=(_2.indexOf("T")==-1)?_2.split(" "):_2.split("T");
_1=dojo.date.setIso8601Date(_1,_3[0]);
if(_3.length==2){
_1=dojo.date.setIso8601Time(_1,_3[1]);
}
return _1;
};
dojo.date.fromIso8601=function(_4){
return dojo.date.setIso8601(new Date(0,0),_4);
};
dojo.date.setIso8601Date=function(_5,_6){
var _7="^([0-9]{4})((-?([0-9]{2})(-?([0-9]{2}))?)|"+"(-?([0-9]{3}))|(-?W([0-9]{2})(-?([1-7]))?))?$";
var d=_6.match(new RegExp(_7));
if(!d){
dojo.debug("invalid date string: "+_6);
return null;
}
var _9=d[1];
var _a=d[4];
var _b=d[6];
var _c=d[8];
var _d=d[10];
var _e=d[12]?d[12]:1;
_5.setFullYear(_9);
if(_c){
_5.setMonth(0);
_5.setDate(Number(_c));
}else{
if(_d){
_5.setMonth(0);
_5.setDate(1);
var gd=_5.getDay();
var day=gd?gd:7;
var _11=Number(_e)+(7*Number(_d));
if(day<=4){
_5.setDate(_11+1-day);
}else{
_5.setDate(_11+8-day);
}
}else{
if(_a){
_5.setDate(1);
_5.setMonth(_a-1);
}
if(_b){
_5.setDate(_b);
}
}
}
return _5;
};
dojo.date.fromIso8601Date=function(_12){
return dojo.date.setIso8601Date(new Date(0,0),_12);
};
dojo.date.setIso8601Time=function(_13,_14){
var _15="Z|(([-+])([0-9]{2})(:?([0-9]{2}))?)$";
var d=_14.match(new RegExp(_15));
var _17=0;
if(d){
if(d[0]!="Z"){
_17=(Number(d[3])*60)+Number(d[5]);
_17*=((d[2]=="-")?1:-1);
}
_17-=_13.getTimezoneOffset();
_14=_14.substr(0,_14.length-d[0].length);
}
var _18="^([0-9]{2})(:?([0-9]{2})(:?([0-9]{2})(.([0-9]+))?)?)?$";
d=_14.match(new RegExp(_18));
if(!d){
dojo.debug("invalid time string: "+_14);
return null;
}
var _19=d[1];
var _1a=Number((d[3])?d[3]:0);
var _1b=(d[5])?d[5]:0;
var ms=d[7]?(Number("0."+d[7])*1000):0;
_13.setHours(_19);
_13.setMinutes(_1a);
_13.setSeconds(_1b);
_13.setMilliseconds(ms);
if(_17!==0){
_13.setTime(_13.getTime()+_17*60000);
}
return _13;
};
dojo.date.fromIso8601Time=function(_1d){
return dojo.date.setIso8601Time(new Date(0,0),_1d);
};
dojo.date.toRfc3339=function(_1e,_1f){
if(!_1e){
_1e=new Date();
}
var _=dojo.string.pad;
var _21=[];
if(_1f!="timeOnly"){
var _22=[_(_1e.getFullYear(),4),_(_1e.getMonth()+1,2),_(_1e.getDate(),2)].join("-");
_21.push(_22);
}
if(_1f!="dateOnly"){
var _23=[_(_1e.getHours(),2),_(_1e.getMinutes(),2),_(_1e.getSeconds(),2)].join(":");
var _24=_1e.getTimezoneOffset();
_23+=(_24>0?"-":"+")+_(Math.floor(Math.abs(_24)/60),2)+":"+_(Math.abs(_24)%60,2);
_21.push(_23);
}
return _21.join("T");
};
dojo.date.fromRfc3339=function(_25){
if(_25.indexOf("Tany")!=-1){
_25=_25.replace("Tany","");
}
var _26=new Date();
return dojo.date.setIso8601(_26,_25);
};
