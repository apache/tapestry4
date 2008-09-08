dojo.provide("dojo.date.supplemental");
dojo.date.getFirstDayOfWeek=function(_1){
var _2={mv:5,ae:6,af:6,bh:6,dj:6,dz:6,eg:6,er:6,et:6,iq:6,ir:6,jo:6,ke:6,kw:6,lb:6,ly:6,ma:6,om:6,qa:6,sa:6,sd:6,so:6,tn:6,ye:6,as:0,au:0,az:0,bw:0,ca:0,cn:0,fo:0,ge:0,gl:0,gu:0,hk:0,ie:0,il:0,is:0,jm:0,jp:0,kg:0,kr:0,la:0,mh:0,mo:0,mp:0,mt:0,nz:0,ph:0,pk:0,sg:0,th:0,tt:0,tw:0,um:0,us:0,uz:0,vi:0,za:0,zw:0,et:0,mw:0,ng:0,tj:0,gb:0,sy:4};
_1=dojo.hostenv.normalizeLocale(_1);
var _3=_1.split("-")[1];
var _4=_2[_3];
return (typeof _4=="undefined")?1:_4;
};
dojo.date.getWeekend=function(_5){
var _6={eg:5,il:5,sy:5,"in":0,ae:4,bh:4,dz:4,iq:4,jo:4,kw:4,lb:4,ly:4,ma:4,om:4,qa:4,sa:4,sd:4,tn:4,ye:4};
var _7={ae:5,bh:5,dz:5,iq:5,jo:5,kw:5,lb:5,ly:5,ma:5,om:5,qa:5,sa:5,sd:5,tn:5,ye:5,af:5,ir:5,eg:6,il:6,sy:6};
_5=dojo.hostenv.normalizeLocale(_5);
var _8=_5.split("-")[1];
var _9=_6[_8];
var _a=_7[_8];
if(typeof _9=="undefined"){
_9=6;
}
if(typeof _a=="undefined"){
_a=0;
}
return {start:_9,end:_a};
};
dojo.date.isWeekend=function(_b,_c){
var _d=dojo.date.getWeekend(_c);
var _e=(_b||new Date()).getDay();
if(_d.end<_d.start){
_d.end+=7;
if(_e<_d.start){
_e+=7;
}
}
return _e>=_d.start&&_e<=_d.end;
};
