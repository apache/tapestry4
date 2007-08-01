dojo.provide("dojo.date.format");
dojo.require("dojo.date.common");
dojo.require("dojo.date.supplemental");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.func");
dojo.require("dojo.string.common");
dojo.require("dojo.i18n.common");
dojo.requireLocalization("dojo.i18n.calendar","gregorian",null,"zh-cn,zh-hk,de,ko,zh-tw,zh,ja,fi,pt-br,fr,es,ROOT,hu,en,it,sv,nl,pt");
dojo.requireLocalization("dojo.i18n.calendar","gregorianExtras",null,"zh,ja,ROOT");
(function(){
dojo.date.format=function(_1,_2){
if(typeof _2=="string"){
dojo.deprecated("dojo.date.format","To format dates with POSIX-style strings, please use dojo.date.strftime instead","0.5");
return dojo.date.strftime(_1,_2);
}
function formatPattern(_3,_4){
return _4.replace(/([a-z])\1*/ig,function(_5){
var s;
var c=_5.charAt(0);
var l=_5.length;
var _9;
var _a=["abbr","wide","narrow"];
switch(c){
case "G":
if(l>3){
dojo.unimplemented("Era format not implemented");
}
s=_b.eras[_3.getFullYear()<0?1:0];
break;
case "y":
s=_3.getFullYear();
switch(l){
case 1:
break;
case 2:
s=String(s);
s=s.substr(s.length-2);
break;
default:
_9=true;
}
break;
case "Q":
case "q":
s=Math.ceil((_3.getMonth()+1)/3);
switch(l){
case 1:
case 2:
_9=true;
break;
case 3:
case 4:
dojo.unimplemented("Quarter format not implemented");
}
break;
case "M":
case "L":
var m=_3.getMonth();
var _d;
switch(l){
case 1:
case 2:
s=m+1;
_9=true;
break;
case 3:
case 4:
case 5:
_d=_a[l-3];
break;
}
if(_d){
var _e=(c=="L")?"standalone":"format";
var _f=["months",_e,_d].join("-");
s=_b[_f][m];
}
break;
case "w":
var _10=0;
s=dojo.date.getWeekOfYear(_3,_10);
_9=true;
break;
case "d":
s=_3.getDate();
_9=true;
break;
case "D":
s=dojo.date.getDayOfYear(_3);
_9=true;
break;
case "E":
case "e":
case "c":
var d=_3.getDay();
var _d;
switch(l){
case 1:
case 2:
if(c=="e"){
var _12=dojo.date.getFirstDayOfWeek(_2.locale);
d=(d-_12+7)%7;
}
if(c!="c"){
s=d+1;
_9=true;
break;
}
case 3:
case 4:
case 5:
_d=_a[l-3];
break;
}
if(_d){
var _e=(c=="c")?"standalone":"format";
var _f=["days",_e,_d].join("-");
s=_b[_f][d];
}
break;
case "a":
var _13=(_3.getHours()<12)?"am":"pm";
s=_b[_13];
break;
case "h":
case "H":
case "K":
case "k":
var h=_3.getHours();
switch(c){
case "h":
s=(h%12)||12;
break;
case "H":
s=h;
break;
case "K":
s=(h%12);
break;
case "k":
s=h||24;
break;
}
_9=true;
break;
case "m":
s=_3.getMinutes();
_9=true;
break;
case "s":
s=_3.getSeconds();
_9=true;
break;
case "S":
s=Math.round(_3.getMilliseconds()*Math.pow(10,l-3));
break;
case "v":
case "z":
s=dojo.date.getTimezoneName(_3);
if(s){
break;
}
l=4;
case "Z":
var _15=_3.getTimezoneOffset();
var tz=[(_15<=0?"+":"-"),dojo.string.pad(Math.floor(Math.abs(_15)/60),2),dojo.string.pad(Math.abs(_15)%60,2)];
if(l==4){
tz.splice(0,0,"GMT");
tz.splice(3,0,":");
}
s=tz.join("");
break;
case "Y":
case "u":
case "W":
case "F":
case "g":
case "A":
s="?";
break;
default:
dojo.raise("dojo.date.format: invalid pattern char: "+_4);
}
if(_9){
s=dojo.string.pad(s,l);
}
return s;
});
}
_2=_2||{};
var _17=dojo.hostenv.normalizeLocale(_2.locale);
var _18=_2.formatLength||"short";
var _b=dojo.date._getGregorianBundle(_17);
var str=[];
var _1a=dojo.lang.curry(this,formatPattern,_1);
if(_2.selector=="yearOnly"){
var _1b=_1.getFullYear();
if(_17.match(/^zh|^ja/)){
_1b+="\u5e74";
}
return _1b;
}
if(_2.selector!="timeOnly"){
var _1c=_2.datePattern||_b["dateFormat-"+_18];
if(_1c){
str.push(_processPattern(_1c,_1a));
}
}
if(_2.selector!="dateOnly"){
var _1d=_2.timePattern||_b["timeFormat-"+_18];
if(_1d){
str.push(_processPattern(_1d,_1a));
}
}
var _1e=str.join(" ");
return _1e;
};
dojo.date.parse=function(_1f,_20){
_20=_20||{};
var _21=dojo.hostenv.normalizeLocale(_20.locale);
var _22=dojo.date._getGregorianBundle(_21);
var _23=_20.formatLength||"full";
if(!_20.selector){
_20.selector="dateOnly";
}
var _24=_20.datePattern||_22["dateFormat-"+_23];
var _25=_20.timePattern||_22["timeFormat-"+_23];
var _26;
if(_20.selector=="dateOnly"){
_26=_24;
}else{
if(_20.selector=="timeOnly"){
_26=_25;
}else{
if(_20.selector=="dateTime"){
_26=_24+" "+_25;
}else{
var msg="dojo.date.parse: Unknown selector param passed: '"+_20.selector+"'.";
msg+=" Defaulting to date pattern.";
dojo.debug(msg);
_26=_24;
}
}
}
var _28=[];
var _29=_processPattern(_26,dojo.lang.curry(this,_buildDateTimeRE,_28,_22,_20));
var _2a=new RegExp("^"+_29+"$");
var _2b=_2a.exec(_1f);
if(!_2b){
return null;
}
var _2c=["abbr","wide","narrow"];
var _2d=new Date(1972,0);
var _2e={};
for(var i=1;i<_2b.length;i++){
var grp=_28[i-1];
var l=grp.length;
var v=_2b[i];
switch(grp.charAt(0)){
case "y":
if(l!=2){
_2d.setFullYear(v);
_2e.year=v;
}else{
if(v<100){
v=Number(v);
var _33=""+new Date().getFullYear();
var _34=_33.substring(0,2)*100;
var _35=Number(_33.substring(2,4));
var _36=Math.min(_35+20,99);
var num=(v<_36)?_34+v:_34-100+v;
_2d.setFullYear(num);
_2e.year=num;
}else{
if(_20.strict){
return null;
}
_2d.setFullYear(v);
_2e.year=v;
}
}
break;
case "M":
if(l>2){
if(!_20.strict){
v=v.replace(/\./g,"");
v=v.toLowerCase();
}
var _38=_22["months-format-"+_2c[l-3]].concat();
for(var j=0;j<_38.length;j++){
if(!_20.strict){
_38[j]=_38[j].toLowerCase();
}
if(v==_38[j]){
_2d.setMonth(j);
_2e.month=j;
break;
}
}
if(j==_38.length){
dojo.debug("dojo.date.parse: Could not parse month name: '"+v+"'.");
return null;
}
}else{
_2d.setMonth(v-1);
_2e.month=v-1;
}
break;
case "E":
case "e":
if(!_20.strict){
v=v.toLowerCase();
}
var _3a=_22["days-format-"+_2c[l-3]].concat();
for(var j=0;j<_3a.length;j++){
if(!_20.strict){
_3a[j]=_3a[j].toLowerCase();
}
if(v==_3a[j]){
break;
}
}
if(j==_3a.length){
dojo.debug("dojo.date.parse: Could not parse weekday name: '"+v+"'.");
return null;
}
break;
case "d":
_2d.setDate(v);
_2e.date=v;
break;
case "a":
var am=_20.am||_22.am;
var pm=_20.pm||_22.pm;
if(!_20.strict){
v=v.replace(/\./g,"").toLowerCase();
am=am.replace(/\./g,"").toLowerCase();
pm=pm.replace(/\./g,"").toLowerCase();
}
if(_20.strict&&v!=am&&v!=pm){
dojo.debug("dojo.date.parse: Could not parse am/pm part.");
return null;
}
var _3d=_2d.getHours();
if(v==pm&&_3d<12){
_2d.setHours(_3d+12);
}else{
if(v==am&&_3d==12){
_2d.setHours(0);
}
}
break;
case "K":
if(v==24){
v=0;
}
case "h":
case "H":
case "k":
if(v>23){
dojo.debug("dojo.date.parse: Illegal hours value");
return null;
}
_2d.setHours(v);
break;
case "m":
_2d.setMinutes(v);
break;
case "s":
_2d.setSeconds(v);
break;
case "S":
_2d.setMilliseconds(v);
break;
default:
dojo.unimplemented("dojo.date.parse: unsupported pattern char="+grp.charAt(0));
}
}
if(_2e.year&&_2d.getFullYear()!=_2e.year){
dojo.debug("Parsed year: '"+_2d.getFullYear()+"' did not match input year: '"+_2e.year+"'.");
return null;
}
if(_2e.month&&_2d.getMonth()!=_2e.month){
dojo.debug("Parsed month: '"+_2d.getMonth()+"' did not match input month: '"+_2e.month+"'.");
return null;
}
if(_2e.date&&_2d.getDate()!=_2e.date){
dojo.debug("Parsed day of month: '"+_2d.getDate()+"' did not match input day of month: '"+_2e.date+"'.");
return null;
}
return _2d;
};
function _processPattern(_3e,_3f,_40,_41){
var _42=function(x){
return x;
};
_3f=_3f||_42;
_40=_40||_42;
_41=_41||_42;
var _44=_3e.match(/(''|[^'])+/g);
var _45=false;
for(var i=0;i<_44.length;i++){
if(!_44[i]){
_44[i]="";
}else{
_44[i]=(_45?_40:_3f)(_44[i]);
_45=!_45;
}
}
return _41(_44.join(""));
}
function _buildDateTimeRE(_47,_48,_49,_4a){
return _4a.replace(/([a-z])\1*/ig,function(_4b){
var s;
var c=_4b.charAt(0);
var l=_4b.length;
switch(c){
case "y":
s="\\d"+((l==2)?"{2,4}":"+");
break;
case "M":
s=(l>2)?"\\S+":"\\d{1,2}";
break;
case "d":
s="\\d{1,2}";
break;
case "E":
s="\\S+";
break;
case "h":
case "H":
case "K":
case "k":
s="\\d{1,2}";
break;
case "m":
case "s":
s="[0-5]\\d";
break;
case "S":
s="\\d{1,3}";
break;
case "a":
var am=_49.am||_48.am||"AM";
var pm=_49.pm||_48.pm||"PM";
if(_49.strict){
s=am+"|"+pm;
}else{
s=am;
s+=(am!=am.toLowerCase())?"|"+am.toLowerCase():"";
s+="|";
s+=(pm!=pm.toLowerCase())?pm+"|"+pm.toLowerCase():pm;
}
break;
default:
dojo.unimplemented("parse of date format, pattern="+_4a);
}
if(_47){
_47.push(_4b);
}
return "\\s*("+s+")\\s*";
});
}
})();
dojo.date.strftime=function(_51,_52,_53){
var _54=null;
function _(s,n){
return dojo.string.pad(s,n||2,_54||"0");
}
var _57=dojo.date._getGregorianBundle(_53);
function $(_58){
switch(_58){
case "a":
return dojo.date.getDayShortName(_51,_53);
case "A":
return dojo.date.getDayName(_51,_53);
case "b":
case "h":
return dojo.date.getMonthShortName(_51,_53);
case "B":
return dojo.date.getMonthName(_51,_53);
case "c":
return dojo.date.format(_51,{locale:_53});
case "C":
return _(Math.floor(_51.getFullYear()/100));
case "d":
return _(_51.getDate());
case "D":
return $("m")+"/"+$("d")+"/"+$("y");
case "e":
if(_54==null){
_54=" ";
}
return _(_51.getDate());
case "f":
if(_54==null){
_54=" ";
}
return _(_51.getMonth()+1);
case "g":
break;
case "G":
dojo.unimplemented("unimplemented modifier 'G'");
break;
case "F":
return $("Y")+"-"+$("m")+"-"+$("d");
case "H":
return _(_51.getHours());
case "I":
return _(_51.getHours()%12||12);
case "j":
return _(dojo.date.getDayOfYear(_51),3);
case "k":
if(_54==null){
_54=" ";
}
return _(_51.getHours());
case "l":
if(_54==null){
_54=" ";
}
return _(_51.getHours()%12||12);
case "m":
return _(_51.getMonth()+1);
case "M":
return _(_51.getMinutes());
case "n":
return "\n";
case "p":
return _57[_51.getHours()<12?"am":"pm"];
case "r":
return $("I")+":"+$("M")+":"+$("S")+" "+$("p");
case "R":
return $("H")+":"+$("M");
case "S":
return _(_51.getSeconds());
case "t":
return "\t";
case "T":
return $("H")+":"+$("M")+":"+$("S");
case "u":
return String(_51.getDay()||7);
case "U":
return _(dojo.date.getWeekOfYear(_51));
case "V":
return _(dojo.date.getIsoWeekOfYear(_51));
case "W":
return _(dojo.date.getWeekOfYear(_51,1));
case "w":
return String(_51.getDay());
case "x":
return dojo.date.format(_51,{selector:"dateOnly",locale:_53});
case "X":
return dojo.date.format(_51,{selector:"timeOnly",locale:_53});
case "y":
return _(_51.getFullYear()%100);
case "Y":
return String(_51.getFullYear());
case "z":
var _59=_51.getTimezoneOffset();
return (_59>0?"-":"+")+_(Math.floor(Math.abs(_59)/60))+":"+_(Math.abs(_59)%60);
case "Z":
return dojo.date.getTimezoneName(_51);
case "%":
return "%";
}
}
var _5a="";
var i=0;
var _5c=0;
var _5d=null;
while((_5c=_52.indexOf("%",i))!=-1){
_5a+=_52.substring(i,_5c++);
switch(_52.charAt(_5c++)){
case "_":
_54=" ";
break;
case "-":
_54="";
break;
case "0":
_54="0";
break;
case "^":
_5d="upper";
break;
case "*":
_5d="lower";
break;
case "#":
_5d="swap";
break;
default:
_54=null;
_5c--;
break;
}
var _5e=$(_52.charAt(_5c++));
switch(_5d){
case "upper":
_5e=_5e.toUpperCase();
break;
case "lower":
_5e=_5e.toLowerCase();
break;
case "swap":
var _5f=_5e.toLowerCase();
var _60="";
var j=0;
var ch="";
while(j<_5e.length){
ch=_5e.charAt(j);
_60+=(ch==_5f.charAt(j))?ch.toUpperCase():ch.toLowerCase();
j++;
}
_5e=_60;
break;
default:
break;
}
_5d=null;
_5a+=_5e;
i=_5c;
}
_5a+=_52.substring(i);
return _5a;
};
(function(){
var _63=[];
dojo.date.addCustomFormats=function(_64,_65){
_63.push({pkg:_64,name:_65});
};
dojo.date._getGregorianBundle=function(_66){
var _67={};
dojo.lang.forEach(_63,function(_68){
var _69=dojo.i18n.getLocalization(_68.pkg,_68.name,_66);
_67=dojo.lang.mixin(_67,_69);
},this);
return _67;
};
})();
dojo.date.addCustomFormats("dojo.i18n.calendar","gregorian");
dojo.date.addCustomFormats("dojo.i18n.calendar","gregorianExtras");
dojo.date.getNames=function(_6a,_6b,use,_6d){
var _6e;
var _6f=dojo.date._getGregorianBundle(_6d);
var _70=[_6a,use,_6b];
if(use=="standAlone"){
_6e=_6f[_70.join("-")];
}
_70[1]="format";
return (_6e||_6f[_70.join("-")]).concat();
};
dojo.date.getDayName=function(_71,_72){
return dojo.date.getNames("days","wide","format",_72)[_71.getDay()];
};
dojo.date.getDayShortName=function(_73,_74){
return dojo.date.getNames("days","abbr","format",_74)[_73.getDay()];
};
dojo.date.getMonthName=function(_75,_76){
return dojo.date.getNames("months","wide","format",_76)[_75.getMonth()];
};
dojo.date.getMonthShortName=function(_77,_78){
return dojo.date.getNames("months","abbr","format",_78)[_77.getMonth()];
};
dojo.date.toRelativeString=function(_79){
var now=new Date();
var _7b=(now-_79)/1000;
var end=" ago";
var _7d=false;
if(_7b<0){
_7d=true;
end=" from now";
_7b=-_7b;
}
if(_7b<60){
_7b=Math.round(_7b);
return _7b+" second"+(_7b==1?"":"s")+end;
}
if(_7b<60*60){
_7b=Math.round(_7b/60);
return _7b+" minute"+(_7b==1?"":"s")+end;
}
if(_7b<60*60*24){
_7b=Math.round(_7b/3600);
return _7b+" hour"+(_7b==1?"":"s")+end;
}
if(_7b<60*60*24*7){
_7b=Math.round(_7b/(3600*24));
if(_7b==1){
return _7d?"Tomorrow":"Yesterday";
}else{
return _7b+" days"+end;
}
}
return dojo.date.format(_79);
};
dojo.date.toSql=function(_7e,_7f){
return dojo.date.strftime(_7e,"%F"+!_7f?" %T":"");
};
dojo.date.fromSql=function(_80){
var _81=_80.split(/[\- :]/g);
while(_81.length<6){
_81.push(0);
}
return new Date(_81[0],(parseInt(_81[1],10)-1),_81[2],_81[3],_81[4],_81[5]);
};
