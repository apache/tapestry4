dojo.provide("dojo.i18n.currency");
dojo.require("dojo.experimental");
dojo.experimental("dojo.i18n.currency");
dojo.require("dojo.regexp");
dojo.require("dojo.i18n.common");
dojo.require("dojo.i18n.number");
dojo.require("dojo.lang.common");
dojo.i18n.currency.format=function(_1,_2,_3,_4){
_3=(typeof _3=="object")?_3:{};
var _5=dojo.i18n.currency._mapToLocalizedFormatData(dojo.i18n.currency.FORMAT_TABLE,_2,_4);
if(typeof _3.places=="undefined"){
_3.places=_5.places;
}
if(typeof _3.places=="undefined"){
_3.places=2;
}
_3.signed=false;
var _6=dojo.i18n.number.format(_1,_3,_4);
var _7=_5.symbol;
if(_5.adjSpace=="symbol"){
if(_5.placement=="after"){
_7=" "+_7;
}else{
_7=_7+" ";
}
}
if(_1<0){
if(_5.signPlacement=="before"){
_7="-"+_7;
}else{
if(_5.signPlacement=="after"){
_7=_7+"-";
}
}
}
var _8=(_5.adjSpace=="number")?" ":"";
if(_5.placement=="after"){
_6=_6+_8+_7;
}else{
_6=_7+_8+_6;
}
if(_1<0){
if(_5.signPlacement=="around"){
_6="("+_6+")";
}else{
if(_5.signPlacement=="end"){
_6=_6+"-";
}else{
if(!_5.signPlacement||_5.signPlacement=="begin"){
_6="-"+_6;
}
}
}
}
return _6;
};
dojo.i18n.currency.parse=function(_9,_a,_b,_c){
if(typeof _c.validate=="undefined"){
_c.validate=true;
}
if(_c.validate&&!dojo.i18n.number.isCurrency(_9,_a,_b,_c)){
return Number.NaN;
}
var _d=(_9.indexOf("-")!=-1);
var _e=_e.replace(/\-/,"");
var _f=dojo.i18n.currency._mapToLocalizedFormatData(dojo.i18n.currency.FORMAT_TABLE,_a,_b);
_e=_e.replace(new RegExp("\\"+_f.symbol),"");
var _10=dojo.i18n.number.parse(_e,_b,_c);
if(_d){
_10=_10*-1;
}
return _10;
};
dojo.i18n.currency.isCurrency=function(_11,iso,_13,_14){
_14=(typeof _14=="object")?_14:{};
var _15=dojo.i18n.number._mapToLocalizedFormatData(dojo.i18n.number.FORMAT_TABLE,_13);
if(typeof _14.separator=="undefined"){
_14.separator=_15[0];
}else{
if(dojo.lang.isArray(_14.separator)&&_14.separator.length==0){
_14.separator=[_15[0],""];
}
}
if(typeof _14.decimal=="undefined"){
_14.decimal=_15[2];
}
if(typeof _14.groupSize=="undefined"){
_14.groupSize=_15[3];
}
if(typeof _14.groupSize2=="undefined"){
_14.groupSize2=_15[4];
}
var _16=dojo.i18n.currency._mapToLocalizedFormatData(dojo.i18n.currency.FORMAT_TABLE,iso,_13);
if(typeof _14.places=="undefined"){
_14.places=_16.places;
}
if(typeof _14.places=="undefined"){
_14.places=2;
}
if(typeof _14.symbol=="undefined"){
_14.symbol=_16.symbol;
}else{
if(dojo.lang.isArray(_14.symbol)&&_14.symbol.length==0){
_14.symbol=[_16.symbol,""];
}
}
if(typeof _14.placement=="undefined"){
_14.placement=_16.placement;
}
var re=new RegExp("^"+dojo.regexp.currency(_14)+"$");
return re.test(_11);
};
dojo.i18n.currency._mapToLocalizedFormatData=function(_18,iso,_1a){
var _1b=dojo.i18n.currency.FORMAT_TABLE[iso];
if(!dojo.lang.isArray(_1b)){
return _1b;
}
return dojo.i18n.number._mapToLocalizedFormatData(_1b[0],_1a);
};
(function(){
var _1c={symbol:"\u062c",placement:"after",htmlSymbol:"?"};
var _1d={symbol:"\u20ac",placement:"before",adjSpace:"symbol",htmlSymbol:"&euro;"};
var _1e={symbol:"\u20ac",placement:"after",htmlSymbol:"&euro;"};
dojo.i18n.currency.FORMAT_TABLE={AED:{symbol:"\u062c",placement:"after"},ARS:{symbol:"$",signPlacement:"after"},ATS:{symbol:"\u20ac",adjSpace:"number",signPlacement:"after",htmlSymbol:"&euro;"},AUD:{symbol:"$"},BOB:{symbol:"$b"},BRL:{symbol:"R$",adjSpace:"symbol"},BEF:_1e,BHD:_1c,CAD:[{"*":{symbol:"$"},"fr-ca":{symbol:"$",placement:"after",signPlacement:"around"}}],CHF:{symbol:"CHF",adjSpace:"symbol",signPlacement:"after"},CLP:{symbol:"$"},COP:{symbol:"$",signPlacement:"around"},CNY:{symbol:"\xa5",htmlSymbol:"&yen;"},CRC:{symbol:"\u20a1",signPlacement:"after",htmlSymbol:"?"},CZK:{symbol:"Kc",adjSpace:"symbol",signPlacement:"after"},DEM:_1e,DKK:{symbol:"kr.",adjSpace:"symbol",signPlacement:"after"},DOP:{symbol:"$"},DZD:_1c,ECS:{symbol:"$",signPlacement:"after"},EGP:_1c,ESP:_1e,EUR:_1d,FIM:_1e,FRF:_1e,GBP:{symbol:"\xa3",htmlSymbol:"&pound;"},GRD:{symbol:"\u20ac",signPlacement:"end",htmlSymbol:"&euro;"},GTQ:{symbol:"Q",signPlacement:"after"},HKD:{symbol:"HK$"},HNL:{symbol:"L.",signPlacement:"end"},HUF:{symbol:"Ft",placement:"after",adjSpace:"symbol"},IEP:{symbol:"\u20ac",htmlSymbol:"&euro;"},ILS:{symbol:"\u05e9\"\u05d7",placement:"after",htmlSymbol:"?"},INR:{symbol:"Rs."},ITL:{symbol:"\u20ac",signPlacement:"after",htmlSymbol:"&euro;"},JOD:_1c,JPY:{symbol:"\xa5",places:0,htmlSymbol:"&yen;"},KRW:{symbol:"\u20a9",places:0,htmlSymbol:"?"},KWD:_1c,LBP:_1c,LUF:_1e,MAD:_1c,MXN:{symbol:"$",signPlacement:"around"},NIO:{symbol:"C$",adjSpace:"symbol",signPlacement:"after"},NLG:{symbol:"\u20ac",signPlacement:"end",htmlSymbol:"&euro;"},NOK:{symbol:"kr",adjSpace:"symbol",signPlacement:"after"},NZD:{symbol:"$"},OMR:_1c,PAB:{symbol:"B/",adjSpace:"symbol",signPlacement:"after"},PEN:{symbol:"S/",signPlacement:"after"},PLN:{symbol:"z",placement:"after"},PTE:_1e,PYG:{symbol:"Gs.",signPlacement:"after"},QAR:_1c,RUR:{symbol:"rub.",placement:"after"},SAR:_1c,SEK:{symbol:"kr",placement:"after",adjSpace:"symbol"},SGD:{symbol:"$"},SVC:{symbol:"\u20a1",signPlacement:"after",adjSpace:"symbol"},SYP:_1c,TND:_1c,TRL:{symbol:"TL",placement:"after"},TWD:{symbol:"NT$"},USD:{symbol:"$"},UYU:{symbol:"$U",signplacement:"after",adjSpace:"symbol"},VEB:{symbol:"Bs",signplacement:"after",adjSpace:"symbol"},YER:_1c,ZAR:{symbol:"R",signPlacement:"around"}};
})();
