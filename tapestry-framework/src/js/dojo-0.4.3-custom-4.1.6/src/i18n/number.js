dojo.provide("dojo.i18n.number");
dojo.require("dojo.experimental");
dojo.require("dojo.regexp");
dojo.require("dojo.i18n.common");
dojo.require("dojo.lang.common");
dojo.i18n.number.format=function(_1,_2,_3){
_2=(typeof _2=="object")?_2:{};
var _4=dojo.i18n.number._mapToLocalizedFormatData(dojo.i18n.number.FORMAT_TABLE,_3);
if(typeof _2.separator=="undefined"){
_2.separator=_4[1];
}
if(typeof _2.decimal=="undefined"){
_2.decimal=_4[2];
}
if(typeof _2.groupSize=="undefined"){
_2.groupSize=_4[3];
}
if(typeof _2.groupSize2=="undefined"){
_2.groupSize2=_4[4];
}
if(typeof _2.round=="undefined"){
_2.round=true;
}
if(typeof _2.signed=="undefined"){
_2.signed=true;
}
var _5=(_2.signed&&(_1<0))?"-":"";
_1=Math.abs(_1);
var _6=String((((_2.places>0)||!_2.round)?Math.floor:Math.round)(_1));
function splitSubstrings(_7,_8){
for(var _9=[];_7.length>=_8;_7=_7.substr(0,_7.length-_8)){
_9.push(_7.substr(-_8));
}
if(_7.length>0){
_9.push(_7);
}
return _9.reverse();
}
if(_2.groupSize2&&(_6.length>_2.groupSize)){
var _a=splitSubstrings(_6.substr(0,_6.length-_2.groupSize),_2.groupSize2);
_a.push(_6.substr(-_2.groupSize));
_5=_5+_a.join(_2.separator);
}else{
if(_2.groupSize){
_5=_5+splitSubstrings(_6,_2.groupSize).join(_2.separator);
}else{
_5=_5+_6;
}
}
if(_2.places>0){
var _b=_1-Math.floor(_1);
_b=(_2.round?Math.round:Math.floor)(_b*Math.pow(10,_2.places));
_5=_5+_2.decimal+_b;
}
return _5;
};
dojo.i18n.number.parse=function(_c,_d,_e){
_e=(typeof _e=="object")?_e:{};
var _f=dojo.i18n.number._mapToLocalizedFormatData(dojo.i18n.number.FORMAT_TABLE,_d);
if(typeof _e.separator=="undefined"){
_e.separator=_f[1];
}
if(typeof _e.decimal=="undefined"){
_e.decimal=_f[2];
}
if(typeof _e.groupSize=="undefined"){
_e.groupSize=_f[3];
}
if(typeof _e.groupSize2=="undefined"){
_e.groupSize2=_f[4];
}
if(typeof _e.validate=="undefined"){
_e.validate=true;
}
if(_e.validate&&!dojo.i18n.number.isReal(_c,_d,_e)){
return Number.NaN;
}
var _10=_c.split(_e.decimal);
if(_10.length>2){
return Number.NaN;
}
var _11;
if(_e.separator!=""){
_11=Number(_10[0].replace(new RegExp("\\"+_e.separator,"g"),""));
}else{
_11=Number(_10[0]);
}
var _12=(_10.length==1)?0:Number(_10[1])/Math.pow(10,String(_10[1]).length);
return _11+_12;
};
dojo.i18n.number.isInteger=function(_13,_14,_15){
_15=(typeof _15=="object")?_15:{};
var _16=dojo.i18n.number._mapToLocalizedFormatData(dojo.i18n.number.FORMAT_TABLE,_14);
if(typeof _15.separator=="undefined"){
_15.separator=_16[1];
}else{
if(dojo.lang.isArray(_15.separator)&&_15.separator.length===0){
_15.separator=[_16[1],""];
}
}
if(typeof _15.groupSize=="undefined"){
_15.groupSize=_16[3];
}
if(typeof _15.groupSize2=="undefined"){
_15.groupSize2=_16[4];
}
var re=new RegExp("^"+dojo.regexp.integer(_15)+"$");
return re.test(_13);
};
dojo.i18n.number.isReal=function(_18,_19,_1a){
_1a=(typeof _1a=="object")?_1a:{};
var _1b=dojo.i18n.number._mapToLocalizedFormatData(dojo.i18n.number.FORMAT_TABLE,_19);
if(typeof _1a.separator=="undefined"){
_1a.separator=_1b[1];
}else{
if(dojo.lang.isArray(_1a.separator)&&_1a.separator.length===0){
_1a.separator=[_1b[1],""];
}
}
if(typeof _1a.decimal=="undefined"){
_1a.decimal=_1b[2];
}
if(typeof _1a.groupSize=="undefined"){
_1a.groupSize=_1b[3];
}
if(typeof _1a.groupSize2=="undefined"){
_1a.groupSize2=_1b[4];
}
var re=new RegExp("^"+dojo.regexp.realNumber(_1a)+"$");
return re.test(_18);
};
(function(){
dojo.i18n.number.FORMAT_TABLE={"ar-ae":["","",",",1],"ar-bh":["","",",",1],"ar-dz":["","",",",1],"ar-eg":["","",",",1],"ar-jo":["","",",",1],"ar-kw":["","",",",1],"ar-lb":["","",",",1],"ar-ma":["","",",",1],"ar-om":["","",",",1],"ar-qa":["","",",",1],"ar-sa":["","",",",1],"ar-sy":["","",",",1],"ar-tn":["","",",",1],"ar-ye":["","",",",1],"cs-cz":[".",".",",",3],"da-dk":[".",".",",",3],"de-at":[".",".",",",3],"de-de":[".",".",",",3],"de-lu":[".",".",",",3],"de-ch":["'","'",".",3],"el-gr":[".",".",",",3],"en-au":[",",",",".",3],"en-ca":[",",",",".",3],"en-gb":[",",",",".",3],"en-hk":[",",",",".",3],"en-ie":[",",",",".",3],"en-in":[",",",",".",3,2],"en-nz":[",",",",".",3],"en-us":[",",",",".",3],"en-za":[",",",",".",3],"es-ar":[".",".",",",3],"es-bo":[".",".",",",3],"es-cl":[".",".",",",3],"es-co":[".",".",",",3],"es-cr":[".",".",",",3],"es-do":[".",".",",",3],"es-ec":[".",".",",",3],"es-es":[".",".",",",3],"es-gt":[",",",",".",3],"es-hn":[",",",",".",3],"es-mx":[",",",",".",3],"es-ni":[",",",",".",3],"es-pa":[",",",",".",3],"es-pe":[",",",",".",3],"es-pr":[",",",",".",3],"es-py":[".",".",",",3],"es-sv":[",",",",".",3],"es-uy":[".",".",",",3],"es-ve":[".",".",",",3],"fi-fi":[" "," ",",",3],"fr-be":[".",".",",",3],"fr-ca":[" "," ",",",3],"fr-ch":[" "," ",".",3],"fr-fr":[" "," ",",",3],"fr-lu":[".",".",",",3],"he-il":[",",",",".",3],"hu-hu":[" "," ",",",3],"it-ch":[" "," ",".",3],"it-it":[".",".",",",3],"ja-jp":[",",",",".",3],"ko-kr":[",",",",".",3],"no-no":[".",".",",",3],"nl-be":[" "," ",",",3],"nl-nl":[".",".",",",3],"pl-pl":[".",".",",",3],"pt-br":[".",".",",",3],"pt-pt":[".",".","$",3],"ru-ru":[" "," ",",",3],"sv-se":["."," ",",",3],"tr-tr":[".",".",",",3],"zh-cn":[",",",",".",3],"zh-hk":[",",",",".",3],"zh-tw":[",",",",".",3],"*":[",",",",".",3]};
})();
dojo.i18n.number._mapToLocalizedFormatData=function(_1d,_1e){
_1e=dojo.hostenv.normalizeLocale(_1e);
var _1f=_1d[_1e];
if(typeof _1f=="undefined"){
_1f=_1d["*"];
}
return _1f;
};
