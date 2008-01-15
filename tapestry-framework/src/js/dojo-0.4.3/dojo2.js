/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("layer.validation");
dojo.provide("dojo.experimental");
dojo.experimental=function(_1,_2){
var _3="EXPERIMENTAL: "+_1;
_3+=" -- Not yet ready for use.  APIs subject to change without notice.";
if(_2){
_3+=" "+_2;
}
dojo.debug(_3);
};
dojo.provide("dojo.regexp");
dojo.evalObjPath("dojo.regexp.us",true);
dojo.regexp.tld=function(_4){
_4=(typeof _4=="object")?_4:{};
if(typeof _4.allowCC!="boolean"){
_4.allowCC=true;
}
if(typeof _4.allowInfra!="boolean"){
_4.allowInfra=true;
}
if(typeof _4.allowGeneric!="boolean"){
_4.allowGeneric=true;
}
var _5="arpa";
var _6="aero|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|xxx|jobs|mobi|post";
var _7="ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|"+"bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cu|cv|cx|cy|cz|de|dj|dk|dm|do|dz|"+"ec|ee|eg|er|eu|es|et|fi|fj|fk|fm|fo|fr|ga|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|"+"gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kr|kw|ky|kz|"+"la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|"+"my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|"+"re|ro|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sk|sl|sm|sn|sr|st|su|sv|sy|sz|tc|td|tf|tg|th|tj|tk|tm|"+"tn|to|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|yu|za|zm|zw";
var a=[];
if(_4.allowInfra){
a.push(_5);
}
if(_4.allowGeneric){
a.push(_6);
}
if(_4.allowCC){
a.push(_7);
}
var _9="";
if(a.length>0){
_9="("+a.join("|")+")";
}
return _9;
};
dojo.regexp.ipAddress=function(_a){
_a=(typeof _a=="object")?_a:{};
if(typeof _a.allowDottedDecimal!="boolean"){
_a.allowDottedDecimal=true;
}
if(typeof _a.allowDottedHex!="boolean"){
_a.allowDottedHex=true;
}
if(typeof _a.allowDottedOctal!="boolean"){
_a.allowDottedOctal=true;
}
if(typeof _a.allowDecimal!="boolean"){
_a.allowDecimal=true;
}
if(typeof _a.allowHex!="boolean"){
_a.allowHex=true;
}
if(typeof _a.allowIPv6!="boolean"){
_a.allowIPv6=true;
}
if(typeof _a.allowHybrid!="boolean"){
_a.allowHybrid=true;
}
var _b="((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])";
var _c="(0[xX]0*[\\da-fA-F]?[\\da-fA-F]\\.){3}0[xX]0*[\\da-fA-F]?[\\da-fA-F]";
var _d="(0+[0-3][0-7][0-7]\\.){3}0+[0-3][0-7][0-7]";
var _e="(0|[1-9]\\d{0,8}|[1-3]\\d{9}|4[01]\\d{8}|42[0-8]\\d{7}|429[0-3]\\d{6}|"+"4294[0-8]\\d{5}|42949[0-5]\\d{4}|429496[0-6]\\d{3}|4294967[01]\\d{2}|42949672[0-8]\\d|429496729[0-5])";
var _f="0[xX]0*[\\da-fA-F]{1,8}";
var _10="([\\da-fA-F]{1,4}\\:){7}[\\da-fA-F]{1,4}";
var _11="([\\da-fA-F]{1,4}\\:){6}"+"((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])";
var a=[];
if(_a.allowDottedDecimal){
a.push(_b);
}
if(_a.allowDottedHex){
a.push(_c);
}
if(_a.allowDottedOctal){
a.push(_d);
}
if(_a.allowDecimal){
a.push(_e);
}
if(_a.allowHex){
a.push(_f);
}
if(_a.allowIPv6){
a.push(_10);
}
if(_a.allowHybrid){
a.push(_11);
}
var _13="";
if(a.length>0){
_13="("+a.join("|")+")";
}
return _13;
};
dojo.regexp.host=function(_14){
_14=(typeof _14=="object")?_14:{};
if(typeof _14.allowIP!="boolean"){
_14.allowIP=true;
}
if(typeof _14.allowLocal!="boolean"){
_14.allowLocal=false;
}
if(typeof _14.allowPort!="boolean"){
_14.allowPort=true;
}
var _15="([0-9a-zA-Z]([-0-9a-zA-Z]{0,61}[0-9a-zA-Z])?\\.)+"+dojo.regexp.tld(_14);
var _16=(_14.allowPort)?"(\\:"+dojo.regexp.integer({signed:false})+")?":"";
var _17=_15;
if(_14.allowIP){
_17+="|"+dojo.regexp.ipAddress(_14);
}
if(_14.allowLocal){
_17+="|localhost";
}
return "("+_17+")"+_16;
};
dojo.regexp.url=function(_18){
_18=(typeof _18=="object")?_18:{};
if(typeof _18.scheme=="undefined"){
_18.scheme=[true,false];
}
var _19=dojo.regexp.buildGroupRE(_18.scheme,function(q){
if(q){
return "(https?|ftps?)\\://";
}
return "";
});
var _1b="(/([^?#\\s/]+/)*)?([^?#\\s/]+(\\?[^?#\\s/]*)?(#[A-Za-z][\\w.:-]*)?)?";
return _19+dojo.regexp.host(_18)+_1b;
};
dojo.regexp.emailAddress=function(_1c){
_1c=(typeof _1c=="object")?_1c:{};
if(typeof _1c.allowCruft!="boolean"){
_1c.allowCruft=false;
}
_1c.allowPort=false;
var _1d="([\\da-z]+[-._+&'])*[\\da-z]+";
var _1e=_1d+"@"+dojo.regexp.host(_1c);
if(_1c.allowCruft){
_1e="<?(mailto\\:)?"+_1e+">?";
}
return _1e;
};
dojo.regexp.emailAddressList=function(_1f){
_1f=(typeof _1f=="object")?_1f:{};
if(typeof _1f.listSeparator!="string"){
_1f.listSeparator="\\s;,";
}
var _20=dojo.regexp.emailAddress(_1f);
var _21="("+_20+"\\s*["+_1f.listSeparator+"]\\s*)*"+_20+"\\s*["+_1f.listSeparator+"]?\\s*";
return _21;
};
dojo.regexp.integer=function(_22){
_22=(typeof _22=="object")?_22:{};
if(typeof _22.signed=="undefined"){
_22.signed=[true,false];
}
if(typeof _22.separator=="undefined"){
_22.separator="";
}else{
if(typeof _22.groupSize=="undefined"){
_22.groupSize=3;
}
}
var _23=dojo.regexp.buildGroupRE(_22.signed,function(q){
return q?"[-+]":"";
});
var _25=dojo.regexp.buildGroupRE(_22.separator,function(sep){
if(sep==""){
return "(0|[1-9]\\d*)";
}
var grp=_22.groupSize,_28=_22.groupSize2;
if(typeof _28!="undefined"){
var _29="(0|[1-9]\\d{0,"+(_28-1)+"}(["+sep+"]\\d{"+_28+"})*["+sep+"]\\d{"+grp+"})";
return ((grp-_28)>0)?"("+_29+"|(0|[1-9]\\d{0,"+(grp-1)+"}))":_29;
}
return "(0|[1-9]\\d{0,"+(grp-1)+"}(["+sep+"]\\d{"+grp+"})*)";
});
return _23+_25;
};
dojo.regexp.realNumber=function(_2a){
_2a=(typeof _2a=="object")?_2a:{};
if(typeof _2a.places!="number"){
_2a.places=Infinity;
}
if(typeof _2a.decimal!="string"){
_2a.decimal=".";
}
if(typeof _2a.fractional=="undefined"){
_2a.fractional=[true,false];
}
if(typeof _2a.exponent=="undefined"){
_2a.exponent=[true,false];
}
if(typeof _2a.eSigned=="undefined"){
_2a.eSigned=[true,false];
}
var _2b=dojo.regexp.integer(_2a);
var _2c=dojo.regexp.buildGroupRE(_2a.fractional,function(q){
var re="";
if(q&&(_2a.places>0)){
re="\\"+_2a.decimal;
if(_2a.places==Infinity){
re="("+re+"\\d+)?";
}else{
re=re+"\\d{"+_2a.places+"}";
}
}
return re;
});
var _2f=dojo.regexp.buildGroupRE(_2a.exponent,function(q){
if(q){
return "([eE]"+dojo.regexp.integer({signed:_2a.eSigned})+")";
}
return "";
});
return _2b+_2c+_2f;
};
dojo.regexp.currency=function(_31){
_31=(typeof _31=="object")?_31:{};
if(typeof _31.signed=="undefined"){
_31.signed=[true,false];
}
if(typeof _31.symbol=="undefined"){
_31.symbol="$";
}
if(typeof _31.placement!="string"){
_31.placement="before";
}
if(typeof _31.signPlacement!="string"){
_31.signPlacement="before";
}
if(typeof _31.separator=="undefined"){
_31.separator=",";
}
if(typeof _31.fractional=="undefined"&&typeof _31.cents!="undefined"){
dojo.deprecated("dojo.regexp.currency: flags.cents","use flags.fractional instead","0.5");
_31.fractional=_31.cents;
}
if(typeof _31.decimal!="string"){
_31.decimal=".";
}
var _32=dojo.regexp.buildGroupRE(_31.signed,function(q){
if(q){
return "[-+]";
}
return "";
});
var _34=dojo.regexp.buildGroupRE(_31.symbol,function(_35){
return "\\s?"+_35.replace(/([.$?*!=:|\\\/^])/g,"\\$1")+"\\s?";
});
switch(_31.signPlacement){
case "before":
_34=_32+_34;
break;
case "after":
_34=_34+_32;
break;
}
var _36=_31;
_36.signed=false;
_36.exponent=false;
var _37=dojo.regexp.realNumber(_36);
var _38;
switch(_31.placement){
case "before":
_38=_34+_37;
break;
case "after":
_38=_37+_34;
break;
}
switch(_31.signPlacement){
case "around":
_38="("+_38+"|"+"\\("+_38+"\\)"+")";
break;
case "begin":
_38=_32+_38;
break;
case "end":
_38=_38+_32;
break;
}
return _38;
};
dojo.regexp.us.state=function(_39){
_39=(typeof _39=="object")?_39:{};
if(typeof _39.allowTerritories!="boolean"){
_39.allowTerritories=true;
}
if(typeof _39.allowMilitary!="boolean"){
_39.allowMilitary=true;
}
var _3a="AL|AK|AZ|AR|CA|CO|CT|DE|DC|FL|GA|HI|ID|IL|IN|IA|KS|KY|LA|ME|MD|MA|MI|MN|MS|MO|MT|"+"NE|NV|NH|NJ|NM|NY|NC|ND|OH|OK|OR|PA|RI|SC|SD|TN|TX|UT|VT|VA|WA|WV|WI|WY";
var _3b="AS|FM|GU|MH|MP|PW|PR|VI";
var _3c="AA|AE|AP";
if(_39.allowTerritories){
_3a+="|"+_3b;
}
if(_39.allowMilitary){
_3a+="|"+_3c;
}
return "("+_3a+")";
};
dojo.regexp.time=function(_3d){
dojo.deprecated("dojo.regexp.time","Use dojo.date.parse instead","0.5");
_3d=(typeof _3d=="object")?_3d:{};
if(typeof _3d.format=="undefined"){
_3d.format="h:mm:ss t";
}
if(typeof _3d.amSymbol!="string"){
_3d.amSymbol="AM";
}
if(typeof _3d.pmSymbol!="string"){
_3d.pmSymbol="PM";
}
var _3e=function(_3f){
_3f=_3f.replace(/([.$?*!=:|{}\(\)\[\]\\\/^])/g,"\\$1");
var _40=_3d.amSymbol.replace(/([.$?*!=:|{}\(\)\[\]\\\/^])/g,"\\$1");
var _41=_3d.pmSymbol.replace(/([.$?*!=:|{}\(\)\[\]\\\/^])/g,"\\$1");
_3f=_3f.replace("hh","(0[1-9]|1[0-2])");
_3f=_3f.replace("h","([1-9]|1[0-2])");
_3f=_3f.replace("HH","([01][0-9]|2[0-3])");
_3f=_3f.replace("H","([0-9]|1[0-9]|2[0-3])");
_3f=_3f.replace("mm","([0-5][0-9])");
_3f=_3f.replace("m","([1-5][0-9]|[0-9])");
_3f=_3f.replace("ss","([0-5][0-9])");
_3f=_3f.replace("s","([1-5][0-9]|[0-9])");
_3f=_3f.replace("t","\\s?("+_40+"|"+_41+")\\s?");
return _3f;
};
return dojo.regexp.buildGroupRE(_3d.format,_3e);
};
dojo.regexp.numberFormat=function(_42){
_42=(typeof _42=="object")?_42:{};
if(typeof _42.format=="undefined"){
_42.format="###-###-####";
}
var _43=function(_44){
_44=_44.replace(/([.$*!=:|{}\(\)\[\]\\\/^])/g,"\\$1");
_44=_44.replace(/\?/g,"\\d?");
_44=_44.replace(/#/g,"\\d");
return _44;
};
return dojo.regexp.buildGroupRE(_42.format,_43);
};
dojo.regexp.buildGroupRE=function(a,re){
if(!(a instanceof Array)){
return re(a);
}
var b=[];
for(var i=0;i<a.length;i++){
b.push(re(a[i]));
}
return "("+b.join("|")+")";
};
dojo.provide("dojo.i18n.number");
dojo.require("dojo.i18n.common");
dojo.require("dojo.lang.common");
dojo.i18n.number.format=function(_49,_4a,_4b){
_4a=(typeof _4a=="object")?_4a:{};
var _4c=dojo.i18n.number._mapToLocalizedFormatData(dojo.i18n.number.FORMAT_TABLE,_4b);
if(typeof _4a.separator=="undefined"){
_4a.separator=_4c[1];
}
if(typeof _4a.decimal=="undefined"){
_4a.decimal=_4c[2];
}
if(typeof _4a.groupSize=="undefined"){
_4a.groupSize=_4c[3];
}
if(typeof _4a.groupSize2=="undefined"){
_4a.groupSize2=_4c[4];
}
if(typeof _4a.round=="undefined"){
_4a.round=true;
}
if(typeof _4a.signed=="undefined"){
_4a.signed=true;
}
var _4d=(_4a.signed&&(_49<0))?"-":"";
_49=Math.abs(_49);
var _4e=String((((_4a.places>0)||!_4a.round)?Math.floor:Math.round)(_49));
function splitSubstrings(str,_50){
for(var _51=[];str.length>=_50;str=str.substr(0,str.length-_50)){
_51.push(str.substr(-_50));
}
if(str.length>0){
_51.push(str);
}
return _51.reverse();
}
if(_4a.groupSize2&&(_4e.length>_4a.groupSize)){
var _52=splitSubstrings(_4e.substr(0,_4e.length-_4a.groupSize),_4a.groupSize2);
_52.push(_4e.substr(-_4a.groupSize));
_4d=_4d+_52.join(_4a.separator);
}else{
if(_4a.groupSize){
_4d=_4d+splitSubstrings(_4e,_4a.groupSize).join(_4a.separator);
}else{
_4d=_4d+_4e;
}
}
if(_4a.places>0){
var _53=_49-Math.floor(_49);
_53=(_4a.round?Math.round:Math.floor)(_53*Math.pow(10,_4a.places));
_4d=_4d+_4a.decimal+_53;
}
return _4d;
};
dojo.i18n.number.parse=function(_54,_55,_56){
_56=(typeof _56=="object")?_56:{};
var _57=dojo.i18n.number._mapToLocalizedFormatData(dojo.i18n.number.FORMAT_TABLE,_55);
if(typeof _56.separator=="undefined"){
_56.separator=_57[1];
}
if(typeof _56.decimal=="undefined"){
_56.decimal=_57[2];
}
if(typeof _56.groupSize=="undefined"){
_56.groupSize=_57[3];
}
if(typeof _56.groupSize2=="undefined"){
_56.groupSize2=_57[4];
}
if(typeof _56.validate=="undefined"){
_56.validate=true;
}
if(_56.validate&&!dojo.i18n.number.isReal(_54,_55,_56)){
return Number.NaN;
}
var _58=_54.split(_56.decimal);
if(_58.length>2){
return Number.NaN;
}
var _59;
if(_56.separator!=""){
_59=Number(_58[0].replace(new RegExp("\\"+_56.separator,"g"),""));
}else{
_59=Number(_58[0]);
}
var _5a=(_58.length==1)?0:Number(_58[1])/Math.pow(10,String(_58[1]).length);
return _59+_5a;
};
dojo.i18n.number.isInteger=function(_5b,_5c,_5d){
_5d=(typeof _5d=="object")?_5d:{};
var _5e=dojo.i18n.number._mapToLocalizedFormatData(dojo.i18n.number.FORMAT_TABLE,_5c);
if(typeof _5d.separator=="undefined"){
_5d.separator=_5e[1];
}else{
if(dojo.lang.isArray(_5d.separator)&&_5d.separator.length===0){
_5d.separator=[_5e[1],""];
}
}
if(typeof _5d.groupSize=="undefined"){
_5d.groupSize=_5e[3];
}
if(typeof _5d.groupSize2=="undefined"){
_5d.groupSize2=_5e[4];
}
var re=new RegExp("^"+dojo.regexp.integer(_5d)+"$");
return re.test(_5b);
};
dojo.i18n.number.isReal=function(_60,_61,_62){
_62=(typeof _62=="object")?_62:{};
var _63=dojo.i18n.number._mapToLocalizedFormatData(dojo.i18n.number.FORMAT_TABLE,_61);
if(typeof _62.separator=="undefined"){
_62.separator=_63[1];
}else{
if(dojo.lang.isArray(_62.separator)&&_62.separator.length===0){
_62.separator=[_63[1],""];
}
}
if(typeof _62.decimal=="undefined"){
_62.decimal=_63[2];
}
if(typeof _62.groupSize=="undefined"){
_62.groupSize=_63[3];
}
if(typeof _62.groupSize2=="undefined"){
_62.groupSize2=_63[4];
}
var re=new RegExp("^"+dojo.regexp.realNumber(_62)+"$");
return re.test(_60);
};
(function(){
dojo.i18n.number.FORMAT_TABLE={"ar-ae":["","",",",1],"ar-bh":["","",",",1],"ar-dz":["","",",",1],"ar-eg":["","",",",1],"ar-jo":["","",",",1],"ar-kw":["","",",",1],"ar-lb":["","",",",1],"ar-ma":["","",",",1],"ar-om":["","",",",1],"ar-qa":["","",",",1],"ar-sa":["","",",",1],"ar-sy":["","",",",1],"ar-tn":["","",",",1],"ar-ye":["","",",",1],"cs-cz":[".",".",",",3],"da-dk":[".",".",",",3],"de-at":[".",".",",",3],"de-de":[".",".",",",3],"de-lu":[".",".",",",3],"de-ch":["'","'",".",3],"el-gr":[".",".",",",3],"en-au":[",",",",".",3],"en-ca":[",",",",".",3],"en-gb":[",",",",".",3],"en-hk":[",",",",".",3],"en-ie":[",",",",".",3],"en-in":[",",",",".",3,2],"en-nz":[",",",",".",3],"en-us":[",",",",".",3],"en-za":[",",",",".",3],"es-ar":[".",".",",",3],"es-bo":[".",".",",",3],"es-cl":[".",".",",",3],"es-co":[".",".",",",3],"es-cr":[".",".",",",3],"es-do":[".",".",",",3],"es-ec":[".",".",",",3],"es-es":[".",".",",",3],"es-gt":[",",",",".",3],"es-hn":[",",",",".",3],"es-mx":[",",",",".",3],"es-ni":[",",",",".",3],"es-pa":[",",",",".",3],"es-pe":[",",",",".",3],"es-pr":[",",",",".",3],"es-py":[".",".",",",3],"es-sv":[",",",",".",3],"es-uy":[".",".",",",3],"es-ve":[".",".",",",3],"fi-fi":[" "," ",",",3],"fr-be":[".",".",",",3],"fr-ca":[" "," ",",",3],"fr-ch":[" "," ",".",3],"fr-fr":[" "," ",",",3],"fr-lu":[".",".",",",3],"he-il":[",",",",".",3],"hu-hu":[" "," ",",",3],"it-ch":[" "," ",".",3],"it-it":[".",".",",",3],"ja-jp":[",",",",".",3],"ko-kr":[",",",",".",3],"no-no":[".",".",",",3],"nl-be":[" "," ",",",3],"nl-nl":[".",".",",",3],"pl-pl":[".",".",",",3],"pt-br":[".",".",",",3],"pt-pt":[".",".","$",3],"ru-ru":[" "," ",",",3],"sv-se":["."," ",",",3],"tr-tr":[".",".",",",3],"zh-cn":[",",",",".",3],"zh-hk":[",",",",".",3],"zh-tw":[",",",",".",3],"*":[",",",",".",3]};
})();
dojo.i18n.number._mapToLocalizedFormatData=function(_65,_66){
_66=dojo.hostenv.normalizeLocale(_66);
var _67=_65[_66];
if(typeof _67=="undefined"){
_67=_65["*"];
}
return _67;
};
dojo.provide("dojo.validate.common");
dojo.validate.isText=function(_68,_69){
_69=(typeof _69=="object")?_69:{};
if(/^\s*$/.test(_68)){
return false;
}
if(typeof _69.length=="number"&&_69.length!=_68.length){
return false;
}
if(typeof _69.minlength=="number"&&_69.minlength>_68.length){
return false;
}
if(typeof _69.maxlength=="number"&&_69.maxlength<_68.length){
return false;
}
return true;
};
dojo.validate.isInteger=function(_6a,_6b){
var re=new RegExp("^"+dojo.regexp.integer(_6b)+"$");
return re.test(_6a);
};
dojo.validate.isRealNumber=function(_6d,_6e){
var re=new RegExp("^"+dojo.regexp.realNumber(_6e)+"$");
return re.test(_6d);
};
dojo.validate.isCurrency=function(_70,_71){
var re=new RegExp("^"+dojo.regexp.currency(_71)+"$");
return re.test(_70);
};
dojo.validate._isInRangeCache={};
dojo.validate.isInRange=function(_73,_74){
_73=_73.replace(dojo.lang.has(_74,"separator")?_74.separator:",","","g").replace(dojo.lang.has(_74,"symbol")?_74.symbol:"$","");
if(isNaN(_73)){
return false;
}
_74=(typeof _74=="object")?_74:{};
var max=(typeof _74.max=="number")?_74.max:Infinity;
var min=(typeof _74.min=="number")?_74.min:-Infinity;
var dec=(typeof _74.decimal=="string")?_74.decimal:".";
var _78=dojo.validate._isInRangeCache;
var _79=_73+"max"+max+"min"+min+"dec"+dec;
if(typeof _78[_79]!="undefined"){
return _78[_79];
}
var _7a="[^"+dec+"\\deE+-]";
_73=_73.replace(RegExp(_7a,"g"),"");
_73=_73.replace(/^([+-]?)(\D*)/,"$1");
_73=_73.replace(/(\D*)$/,"");
_7a="(\\d)["+dec+"](\\d)";
_73=_73.replace(RegExp(_7a,"g"),"$1.$2");
_73=Number(_73);
if(_73<min||_73>max){
_78[_79]=false;
return false;
}
_78[_79]=true;
return true;
};
dojo.validate.isNumberFormat=function(_7b,_7c){
var re=new RegExp("^"+dojo.regexp.numberFormat(_7c)+"$","i");
return re.test(_7b);
};
dojo.validate.isValidLuhn=function(_7e){
var sum,_80,_81;
if(typeof _7e!="string"){
_7e=String(_7e);
}
_7e=_7e.replace(/[- ]/g,"");
_80=_7e.length%2;
sum=0;
for(var i=0;i<_7e.length;i++){
_81=parseInt(_7e.charAt(i));
if(i%2==_80){
_81*=2;
}
if(_81>9){
_81-=9;
}
sum+=_81;
}
return !(sum%10);
};
dojo.provide("dojo.validate.check");
dojo.require("dojo.lang.common");
dojo.validate.check=function(_83,_84){
var _85=[];
var _86=[];
var _87={isSuccessful:function(){
return (!this.hasInvalid()&&!this.hasMissing());
},hasMissing:function(){
return (_85.length>0);
},getMissing:function(){
return _85;
},isMissing:function(_88){
for(var i=0;i<_85.length;i++){
if(_88==_85[i]){
return true;
}
}
return false;
},hasInvalid:function(){
return (_86.length>0);
},getInvalid:function(){
return _86;
},isInvalid:function(_8a){
for(var i=0;i<_86.length;i++){
if(_8a==_86[i]){
return true;
}
}
return false;
}};
if(_84.trim instanceof Array){
for(var i=0;i<_84.trim.length;i++){
var _8d=_83[_84.trim[i]];
if(dj_undef("type",_8d)||_8d.type!="text"&&_8d.type!="textarea"&&_8d.type!="password"){
continue;
}
_8d.value=_8d.value.replace(/(^\s*|\s*$)/g,"");
}
}
if(_84.uppercase instanceof Array){
for(var i=0;i<_84.uppercase.length;i++){
var _8d=_83[_84.uppercase[i]];
if(dj_undef("type",_8d)||_8d.type!="text"&&_8d.type!="textarea"&&_8d.type!="password"){
continue;
}
_8d.value=_8d.value.toUpperCase();
}
}
if(_84.lowercase instanceof Array){
for(var i=0;i<_84.lowercase.length;i++){
var _8d=_83[_84.lowercase[i]];
if(dj_undef("type",_8d)||_8d.type!="text"&&_8d.type!="textarea"&&_8d.type!="password"){
continue;
}
_8d.value=_8d.value.toLowerCase();
}
}
if(_84.ucfirst instanceof Array){
for(var i=0;i<_84.ucfirst.length;i++){
var _8d=_83[_84.ucfirst[i]];
if(dj_undef("type",_8d)||_8d.type!="text"&&_8d.type!="textarea"&&_8d.type!="password"){
continue;
}
_8d.value=_8d.value.replace(/\b\w+\b/g,function(_8e){
return _8e.substring(0,1).toUpperCase()+_8e.substring(1).toLowerCase();
});
}
}
if(_84.digit instanceof Array){
for(var i=0;i<_84.digit.length;i++){
var _8d=_83[_84.digit[i]];
if(dj_undef("type",_8d)||_8d.type!="text"&&_8d.type!="textarea"&&_8d.type!="password"){
continue;
}
_8d.value=_8d.value.replace(/\D/g,"");
}
}
if(_84.required instanceof Array){
for(var i=0;i<_84.required.length;i++){
if(!dojo.lang.isString(_84.required[i])){continue;}
var _8d=_83[_84.required[i]];
if(!dj_undef("type",_8d)&&(_8d.type=="text"||_8d.type=="textarea"||_8d.type=="password"||_8d.type=="file")){
if (/^\s*$/.test(_8d.value))
_85[_85.length]=_8d.name;
}else{
if(!dj_undef("type",_8d)&&(_8d.type=="select-one"||_8d.type=="select-multiple")){
if (_8d.selectedIndex==-1||/^\s*$/.test(_8d.options[_8d.selectedIndex].value))
_85[_85.length]=_8d.name;
}else{
if( dojo.lang.isArrayLike(_8d)){
var _8f=false;
for(var j=0;j<_8d.length;j++){
if(_8d[j].checked){
_8f=true;
}
}
if(!_8f){
_85[_85.length]=_8d[0].name;
}}}}}}
if(_84.required instanceof Array){
for(var i=0;i<_84.required.length;i++){
if(!dojo.lang.isObject(_84.required[i])){continue;}
var _8d,_91, _92;
for(var _92 in _84.required[i]){
_8d=_83[_92];
_91=_84.required[i][_92];
}
if(dojo.lang.isArrayLike(_8d)){
var _8f=0;
for(var j=0;j<_8d.length;j++){
if(_8d[j].checked){
_8f++;
}
}
if(_8f<_91){
_85[_85.length]=_8d[0].name;
}
}else{
if(!dj_undef("type",_8d)&&_8d.type=="select-multiple"){
var _93=0;
for(var j=0;j<_8d.options.length;j++){
if(_8d.options[j].selected&&!/^\s*$/.test(_8d.options[j].value)){
_93++;
}
}
if(_93<_91){
_85[_85.length]=_8d.name;
}}}}}
if(dojo.lang.isObject(_84.dependencies)||dojo.lang.isObject(_84.dependancies)){
if(_84["dependancies"]){
dojo.deprecated("dojo.validate.check","profile 'dependancies' is deprecated, please use "+"'dependencies'","0.5");
_84.dependencies=_84.dependancies;
}
for(_92 in _84.dependencies){
var _8d=_83[_92];
if(dj_undef("type",_8d)){
continue;
}
if(_8d.type!="text"&&_8d.type!="textarea"&&_8d.type!="password"){
continue;
}
if(/\S+/.test(_8d.value)){
continue;
}
if(_87.isMissing(_8d.name)){
continue;
}
var _94=_83[_84.dependencies[_92]];
if(_94.type!="text"&&_94.type!="textarea"&&_94.type!="password"){
continue;
}
if(/^\s*$/.test(_94.value)){
continue;
}
_85[_85.length]=_8d.name;
}
}
if(dojo.lang.isObject(_84.constraints)){
for(_92 in _84.constraints){
var _8d=_83[_92];
if(!_8d){
continue;
}
if(!dj_undef("tagName",_8d)&&(_8d.tagName.toLowerCase().indexOf("input")>=0||_8d.tagName.toLowerCase().indexOf("textarea")>=0)&&/^\s*$/.test(_8d.value)){
continue;
}
var _95=true;
if(dojo.lang.isFunction(_84.constraints[_92])){
_95=_84.constraints[_92](_8d.value);
}else{
if(dojo.lang.isArray(_84.constraints[_92])){
if(dojo.lang.isArray(_84.constraints[_92][0])){
for(var i=0;i<_84.constraints[_92].length;i++){
_95=dojo.validate.evaluateConstraint(_84,_84.constraints[_92][i],_92,_8d);
if(!_95){
break;
}
}
}else{
_95=dojo.validate.evaluateConstraint(_84,_84.constraints[_92],_92,_8d);
}
}
}
if(!_95){
_86[_86.length]=_8d.name;
}
}
}
if(dojo.lang.isObject(_84.confirm)){
for(_92 in _84.confirm){
var _8d=_83[_92];
var _94=_83[_84.confirm[_92]];
if(dj_undef("type",_8d)||dj_undef("type",_94)||(_8d.type!="text"&&_8d.type!="textarea"&&_8d.type!="password")||(_94.type!=_8d.type)||(_94.value==_8d.value)||(_87.isInvalid(_8d.name))||(/^\s*$/.test(_94.value))){
continue;
}
_86[_86.length]=_8d.name;
}
}
return _87;
};
dojo.validate.evaluateConstraint=function(_96,_97,_98,_99){
var _9a=_97[0];
var _9b=_97.slice(1);
_9b.unshift(_99.value);
if(typeof _9a!="undefined"){
return _9a.apply(null,_9b);
}
return false;
};
dojo.provide("dojo.date.common");
dojo.date.setDayOfYear=function(_9c,_9d){
_9c.setMonth(0);
_9c.setDate(_9d);
return _9c;
};
dojo.date.getDayOfYear=function(_9e){
var _9f=_9e.getFullYear();
var _a0=new Date(_9f-1,11,31);
return Math.floor((_9e.getTime()-_a0.getTime())/86400000);
};
dojo.date.setWeekOfYear=function(_a1,_a2,_a3){
if(arguments.length==1){
_a3=0;
}
dojo.unimplemented("dojo.date.setWeekOfYear");
};
dojo.date.getWeekOfYear=function(_a4,_a5){
if(arguments.length==1){
_a5=0;
}
var _a6=new Date(_a4.getFullYear(),0,1);
var day=_a6.getDay();
_a6.setDate(_a6.getDate()-day+_a5-(day>_a5?7:0));
return Math.floor((_a4.getTime()-_a6.getTime())/604800000);
};
dojo.date.setIsoWeekOfYear=function(_a8,_a9,_aa){
if(arguments.length==1){
_aa=1;
}
dojo.unimplemented("dojo.date.setIsoWeekOfYear");
};
dojo.date.getIsoWeekOfYear=function(_ab,_ac){
if(arguments.length==1){
_ac=1;
}
dojo.unimplemented("dojo.date.getIsoWeekOfYear");
};
dojo.date.shortTimezones=["IDLW","BET","HST","MART","AKST","PST","MST","CST","EST","AST","NFT","BST","FST","AT","GMT","CET","EET","MSK","IRT","GST","AFT","AGTT","IST","NPT","ALMT","MMT","JT","AWST","JST","ACST","AEST","LHST","VUT","NFT","NZT","CHAST","PHOT","LINT"];
dojo.date.timezoneOffsets=[-720,-660,-600,-570,-540,-480,-420,-360,-300,-240,-210,-180,-120,-60,0,60,120,180,210,240,270,300,330,345,360,390,420,480,540,570,600,630,660,690,720,765,780,840];
dojo.date.getDaysInMonth=function(_ad){
var _ae=_ad.getMonth();
var _af=[31,28,31,30,31,30,31,31,30,31,30,31];
if(_ae==1&&dojo.date.isLeapYear(_ad)){
return 29;
}else{
return _af[_ae];
}
};
dojo.date.isLeapYear=function(_b0){
var _b1=_b0.getFullYear();
return (_b1%400==0)?true:(_b1%100==0)?false:(_b1%4==0)?true:false;
};
dojo.date.getTimezoneName=function(_b2){
var str=_b2.toString();
var tz="";
var _b5;
var pos=str.indexOf("(");
if(pos>-1){
pos++;
tz=str.substring(pos,str.indexOf(")"));
}else{
var pat=/([A-Z\/]+) \d{4}$/;
if((_b5=str.match(pat))){
tz=_b5[1];
}else{
str=_b2.toLocaleString();
pat=/ ([A-Z\/]+)$/;
if((_b5=str.match(pat))){
tz=_b5[1];
}
}
}
return tz=="AM"||tz=="PM"?"":tz;
};
dojo.date.getOrdinal=function(_b8){
var _b9=_b8.getDate();
if(_b9%100!=11&&_b9%10==1){
return "st";
}else{
if(_b9%100!=12&&_b9%10==2){
return "nd";
}else{
if(_b9%100!=13&&_b9%10==3){
return "rd";
}else{
return "th";
}
}
}
};
dojo.date.compareTypes={DATE:1,TIME:2};
dojo.date.compare=function(_ba,_bb,_bc){
var dA=_ba;
var dB=_bb||new Date();
var now=new Date();
with(dojo.date.compareTypes){
var opt=_bc||(DATE|TIME);
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
dojo.date.add=function(dt,_c4,_c5){
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
switch(_c4){
case YEAR:
sum.setFullYear(dt.getFullYear()+_c5);
fixOvershoot();
break;
case QUARTER:
_c5*=3;
case MONTH:
sum.setMonth(dt.getMonth()+_c5);
fixOvershoot();
break;
case WEEK:
_c5*=7;
case DAY:
sum.setDate(dt.getDate()+_c5);
break;
case WEEKDAY:
var dat=dt.getDate();
var _c8=0;
var _c9=0;
var _ca=0;
var _cb=0;
var adj=0;
var mod=_c5%5;
if(mod==0){
_c9=(_c5>0)?5:-5;
_c8=(_c5>0)?((_c5-5)/5):((_c5+5)/5);
}else{
_c9=mod;
_c8=parseInt(_c5/5);
}
_ca=dt.getDay();
if(_ca==6&&_c5>0){
adj=1;
}else{
if(_ca==0&&_c5<0){
adj=-1;
}
}
_cb=(_ca+_c9);
if(_cb==0||_cb==6){
adj=(_c5>0)?2:-2;
}
sum.setDate(dat+(7*_c8)+_c9+adj);
break;
case HOUR:
sum.setHours(sum.getHours()+_c5);
break;
case MINUTE:
sum.setMinutes(sum.getMinutes()+_c5);
break;
case SECOND:
sum.setSeconds(sum.getSeconds()+_c5);
break;
case MILLISECOND:
sum.setMilliseconds(sum.getMilliseconds()+_c5);
break;
default:
break;
}
}
return sum;
};
dojo.date.diff=function(dtA,dtB,_d0){
if(typeof dtA=="number"){
dtA=new Date(dtA);
}
if(typeof dtB=="number"){
dtB=new Date(dtB);
}
var _d1=dtB.getFullYear()-dtA.getFullYear();
var _d2=(dtB.getMonth()-dtA.getMonth())+(_d1*12);
var _d3=dtB.getTime()-dtA.getTime();
var _d4=_d3/1000;
var _d5=_d4/60;
var _d6=_d5/60;
var _d7=_d6/24;
var _d8=_d7/7;
var _d9=0;
with(dojo.date.dateParts){
switch(_d0){
case YEAR:
_d9=_d1;
break;
case QUARTER:
var mA=dtA.getMonth();
var mB=dtB.getMonth();
var qA=Math.floor(mA/3)+1;
var qB=Math.floor(mB/3)+1;
qB+=(_d1*4);
_d9=qB-qA;
break;
case MONTH:
_d9=_d2;
break;
case WEEK:
_d9=parseInt(_d8);
break;
case DAY:
_d9=_d7;
break;
case WEEKDAY:
var _de=Math.round(_d7);
var _df=parseInt(_de/7);
var mod=_de%7;
if(mod==0){
_de=_df*5;
}else{
var adj=0;
var _e2=dtA.getDay();
var _e3=dtB.getDay();
_df=parseInt(_de/7);
mod=_de%7;
var _e4=new Date(dtA);
_e4.setDate(_e4.getDate()+(_df*7));
var _e5=_e4.getDay();
if(_d7>0){
switch(true){
case _e2==6:
adj=-1;
break;
case _e2==0:
adj=0;
break;
case _e3==6:
adj=-1;
break;
case _e3==0:
adj=-2;
break;
case (_e5+mod)>5:
adj=-2;
break;
default:
break;
}
}else{
if(_d7<0){
switch(true){
case _e2==6:
adj=0;
break;
case _e2==0:
adj=1;
break;
case _e3==6:
adj=2;
break;
case _e3==0:
adj=1;
break;
case (_e5+mod)<0:
adj=2;
break;
default:
break;
}
}
}
_de+=adj;
_de-=(_df*2);
}
_d9=_de;
break;
case HOUR:
_d9=_d6;
break;
case MINUTE:
_d9=_d5;
break;
case SECOND:
_d9=_d4;
break;
case MILLISECOND:
_d9=_d3;
break;
default:
break;
}
}
return Math.round(_d9);
};
dojo.provide("dojo.date.supplemental");
dojo.date.getFirstDayOfWeek=function(_e6){
var _e7={mv:5,ae:6,af:6,bh:6,dj:6,dz:6,eg:6,er:6,et:6,iq:6,ir:6,jo:6,ke:6,kw:6,lb:6,ly:6,ma:6,om:6,qa:6,sa:6,sd:6,so:6,tn:6,ye:6,as:0,au:0,az:0,bw:0,ca:0,cn:0,fo:0,ge:0,gl:0,gu:0,hk:0,ie:0,il:0,is:0,jm:0,jp:0,kg:0,kr:0,la:0,mh:0,mo:0,mp:0,mt:0,nz:0,ph:0,pk:0,sg:0,th:0,tt:0,tw:0,um:0,us:0,uz:0,vi:0,za:0,zw:0,et:0,mw:0,ng:0,tj:0,gb:0,sy:4};
_e6=dojo.hostenv.normalizeLocale(_e6);
var _e8=_e6.split("-")[1];
var dow=_e7[_e8];
return (typeof dow=="undefined")?1:dow;
};
dojo.date.getWeekend=function(_ea){
var _eb={eg:5,il:5,sy:5,"in":0,ae:4,bh:4,dz:4,iq:4,jo:4,kw:4,lb:4,ly:4,ma:4,om:4,qa:4,sa:4,sd:4,tn:4,ye:4};
var _ec={ae:5,bh:5,dz:5,iq:5,jo:5,kw:5,lb:5,ly:5,ma:5,om:5,qa:5,sa:5,sd:5,tn:5,ye:5,af:5,ir:5,eg:6,il:6,sy:6};
_ea=dojo.hostenv.normalizeLocale(_ea);
var _ed=_ea.split("-")[1];
var _ee=_eb[_ed];
var end=_ec[_ed];
if(typeof _ee=="undefined"){
_ee=6;
}
if(typeof end=="undefined"){
end=0;
}
return {start:_ee,end:end};
};
dojo.date.isWeekend=function(_f0,_f1){
var _f2=dojo.date.getWeekend(_f1);
var day=(_f0||new Date()).getDay();
if(_f2.end<_f2.start){
_f2.end+=7;
if(day<_f2.start){
day+=7;
}
}
return day>=_f2.start&&day<=_f2.end;
};
dojo.provide("dojo.date.format");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.func");
dojo.require("dojo.string.common");
dojo.require("dojo.i18n.common");
dojo.requireLocalization("dojo.i18n.calendar","gregorian",null,"zh-cn,zh-hk,de,ko,zh-tw,zh,ja,fi,pt-br,fr,es,ROOT,hu,en,it,sv,nl,pt");
dojo.requireLocalization("dojo.i18n.calendar","gregorianExtras",null,"zh,ja,ROOT");
(function(){
dojo.date.format=function(_f4,_f5){
if(typeof _f5=="string"){
dojo.deprecated("dojo.date.format","To format dates with POSIX-style strings, please use dojo.date.strftime instead","0.5");
return dojo.date.strftime(_f4,_f5);
}
function formatPattern(_f6,_f7){
return _f7.replace(/([a-z])\1*/ig,function(_f8){
var s;
var c=_f8.charAt(0);
var l=_f8.length;
var pad;
var _fd=["abbr","wide","narrow"];
switch(c){
case "G":
if(l>3){
dojo.unimplemented("Era format not implemented");
}
s=_fe.eras[_f6.getFullYear()<0?1:0];
break;
case "y":
s=_f6.getFullYear();
switch(l){
case 1:
break;
case 2:
s=String(s);
s=s.substr(s.length-2);
break;
default:
pad=true;
}
break;
case "Q":
case "q":
s=Math.ceil((_f6.getMonth()+1)/3);
switch(l){
case 1:
case 2:
pad=true;
break;
case 3:
case 4:
dojo.unimplemented("Quarter format not implemented");
}
break;
case "M":
case "L":
var m=_f6.getMonth();
var _100;
switch(l){
case 1:
case 2:
s=m+1;
pad=true;
break;
case 3:
case 4:
case 5:
_100=_fd[l-3];
break;
}
if(_100){
var type=(c=="L")?"standalone":"format";
var prop=["months",type,_100].join("-");
s=_fe[prop][m];
}
break;
case "w":
var _103=0;
s=dojo.date.getWeekOfYear(_f6,_103);
pad=true;
break;
case "d":
s=_f6.getDate();
pad=true;
break;
case "D":
s=dojo.date.getDayOfYear(_f6);
pad=true;
break;
case "E":
case "e":
case "c":
var d=_f6.getDay();
var _100;
switch(l){
case 1:
case 2:
if(c=="e"){
var _105=dojo.date.getFirstDayOfWeek(_f5.locale);
d=(d-_105+7)%7;
}
if(c!="c"){
s=d+1;
pad=true;
break;
}
case 3:
case 4:
case 5:
_100=_fd[l-3];
break;
}
if(_100){
var type=(c=="c")?"standalone":"format";
var prop=["days",type,_100].join("-");
s=_fe[prop][d];
}
break;
case "a":
var _106=(_f6.getHours()<12)?"am":"pm";
s=_fe[_106];
break;
case "h":
case "H":
case "K":
case "k":
var h=_f6.getHours();
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
pad=true;
break;
case "m":
s=_f6.getMinutes();
pad=true;
break;
case "s":
s=_f6.getSeconds();
pad=true;
break;
case "S":
s=Math.round(_f6.getMilliseconds()*Math.pow(10,l-3));
break;
case "v":
case "z":
s=dojo.date.getTimezoneName(_f6);
if(s){
break;
}
l=4;
case "Z":
var _108=_f6.getTimezoneOffset();
var tz=[(_108<=0?"+":"-"),dojo.string.pad(Math.floor(Math.abs(_108)/60),2),dojo.string.pad(Math.abs(_108)%60,2)];
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
dojo.raise("dojo.date.format: invalid pattern char: "+_f7);
}
if(pad){
s=dojo.string.pad(s,l);
}
return s;
});
}
_f5=_f5||{};
var _10a=dojo.hostenv.normalizeLocale(_f5.locale);
var _10b=_f5.formatLength||"short";
var _fe=dojo.date._getGregorianBundle(_10a);
var str=[];
var _10d=dojo.lang.curry(this,formatPattern,_f4);
if(_f5.selector=="yearOnly"){
var year=_f4.getFullYear();
if(_10a.match(/^zh|^ja/)){
year+="\u5e74";
}
return year;
}
if(_f5.selector!="timeOnly"){
var _10f=_f5.datePattern||_fe["dateFormat-"+_10b];
if(_10f){
str.push(_processPattern(_10f,_10d));
}
}
if(_f5.selector!="dateOnly"){
var _110=_f5.timePattern||_fe["timeFormat-"+_10b];
if(_110){
str.push(_processPattern(_110,_10d));
}
}
var _111=str.join(" ");
return _111;
};
dojo.date.parse=function(_112,_113){
_113=_113||{};
var _114=dojo.hostenv.normalizeLocale(_113.locale);
var info=dojo.date._getGregorianBundle(_114);
var _116=_113.formatLength||"full";
if(!_113.selector){
_113.selector="dateOnly";
}
var _117=_113.datePattern||info["dateFormat-"+_116];
var _118=_113.timePattern||info["timeFormat-"+_116];
var _119;
if(_113.selector=="dateOnly"){
_119=_117;
}else{
if(_113.selector=="timeOnly"){
_119=_118;
}else{
if(_113.selector=="dateTime"){
_119=_117+" "+_118;
}else{
var msg="dojo.date.parse: Unknown selector param passed: '"+_113.selector+"'.";
msg+=" Defaulting to date pattern.";
dojo.debug(msg);
_119=_117;
}
}
}
var _11b=[];
var _11c=_processPattern(_119,dojo.lang.curry(this,_buildDateTimeRE,_11b,info,_113));
var _11d=new RegExp("^"+_11c+"$");
var _11e=_11d.exec(_112);
if(!_11e){
return null;
}
var _11f=["abbr","wide","narrow"];
var _120=new Date(1972,0);
var _121={};
for(var i=1;i<_11e.length;i++){
var grp=_11b[i-1];
var l=grp.length;
var v=_11e[i];
switch(grp.charAt(0)){
case "y":
if(l!=2){
_120.setFullYear(v);
_121.year=v;
}else{
if(v<100){
v=Number(v);
var year=""+new Date().getFullYear();
var _127=year.substring(0,2)*100;
var _128=Number(year.substring(2,4));
var _129=Math.min(_128+20,99);
var num=(v<_129)?_127+v:_127-100+v;
_120.setFullYear(num);
_121.year=num;
}else{
if(_113.strict){
return null;
}
_120.setFullYear(v);
_121.year=v;
}
}
break;
case "M":
if(l>2){
if(!_113.strict){
v=v.replace(/\./g,"");
v=v.toLowerCase();
}
var _12b=info["months-format-"+_11f[l-3]].concat();
for(var j=0;j<_12b.length;j++){
if(!_113.strict){
_12b[j]=_12b[j].toLowerCase();
}
if(v==_12b[j]){
_120.setMonth(j);
_121.month=j;
break;
}
}
if(j==_12b.length){
dojo.debug("dojo.date.parse: Could not parse month name: '"+v+"'.");
return null;
}
}else{
_120.setMonth(v-1);
_121.month=v-1;
}
break;
case "E":
case "e":
if(!_113.strict){
v=v.toLowerCase();
}
var days=info["days-format-"+_11f[l-3]].concat();
for(var j=0;j<days.length;j++){
if(!_113.strict){
days[j]=days[j].toLowerCase();
}
if(v==days[j]){
break;
}
}
if(j==days.length){
dojo.debug("dojo.date.parse: Could not parse weekday name: '"+v+"'.");
return null;
}
break;
case "d":
_120.setDate(v);
_121.date=v;
break;
case "a":
var am=_113.am||info.am;
var pm=_113.pm||info.pm;
if(!_113.strict){
v=v.replace(/\./g,"").toLowerCase();
am=am.replace(/\./g,"").toLowerCase();
pm=pm.replace(/\./g,"").toLowerCase();
}
if(_113.strict&&v!=am&&v!=pm){
dojo.debug("dojo.date.parse: Could not parse am/pm part.");
return null;
}
var _130=_120.getHours();
if(v==pm&&_130<12){
_120.setHours(_130+12);
}else{
if(v==am&&_130==12){
_120.setHours(0);
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
_120.setHours(v);
break;
case "m":
_120.setMinutes(v);
break;
case "s":
_120.setSeconds(v);
break;
case "S":
_120.setMilliseconds(v);
break;
default:
dojo.unimplemented("dojo.date.parse: unsupported pattern char="+grp.charAt(0));
}
}
if(_121.year&&_120.getFullYear()!=_121.year){
dojo.debug("Parsed year: '"+_120.getFullYear()+"' did not match input year: '"+_121.year+"'.");
return null;
}
if(_121.month&&_120.getMonth()!=_121.month){
dojo.debug("Parsed month: '"+_120.getMonth()+"' did not match input month: '"+_121.month+"'.");
return null;
}
if(_121.date&&_120.getDate()!=_121.date){
dojo.debug("Parsed day of month: '"+_120.getDate()+"' did not match input day of month: '"+_121.date+"'.");
return null;
}
return _120;
};
function _processPattern(_131,_132,_133,_134){
var _135=function(x){
return x;
};
_132=_132||_135;
_133=_133||_135;
_134=_134||_135;
var _137=_131.match(/(''|[^'])+/g);
var _138=false;
for(var i=0;i<_137.length;i++){
if(!_137[i]){
_137[i]="";
}else{
_137[i]=(_138?_133:_132)(_137[i]);
_138=!_138;
}
}
return _134(_137.join(""));
}
function _buildDateTimeRE(_13a,info,_13c,_13d){
return _13d.replace(/([a-z])\1*/ig,function(_13e){
var s='';
var c=_13e.charAt(0);
var l=_13e.length;
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
var am=_13c.am||info.am||"AM";
var pm=_13c.pm||info.pm||"PM";
if(_13c.strict){
s=am+"|"+pm;
}else{
for (var i=0; i < am.length; i++){s += '[' + am.charAt(i).toLowerCase() + '|' + am.charAt(i).toUpperCase() + ']';}
s += '|';
for (var i=0; i < pm.length; i++){ s += '[' + pm.charAt(i).toLowerCase() + '|' + pm.charAt(i).toUpperCase() + ']';}
}
break;
default:
dojo.unimplemented("parse of date format, pattern="+_13d);
}
if(_13a){
_13a.push(_13e);
}
return "\\s*("+s+")\\s*";
});
}
})();
dojo.date.strftime=function(_144,_145,_146){
var _147=null;
function _(s,n){
return dojo.string.pad(s,n||2,_147||"0");
}
var info=dojo.date._getGregorianBundle(_146);
function $(_14b){
switch(_14b){
case "a":
return dojo.date.getDayShortName(_144,_146);
case "A":
return dojo.date.getDayName(_144,_146);
case "b":
case "h":
return dojo.date.getMonthShortName(_144,_146);
case "B":
return dojo.date.getMonthName(_144,_146);
case "c":
return dojo.date.format(_144,{locale:_146});
case "C":
return _(Math.floor(_144.getFullYear()/100));
case "d":
return _(_144.getDate());
case "D":
return $("m")+"/"+$("d")+"/"+$("y");
case "e":
if(_147==null){
_147=" ";
}
return _(_144.getDate());
case "f":
if(_147==null){
_147=" ";
}
return _(_144.getMonth()+1);
case "g":
break;
case "G":
dojo.unimplemented("unimplemented modifier 'G'");
break;
case "F":
return $("Y")+"-"+$("m")+"-"+$("d");
case "H":
return _(_144.getHours());
case "I":
return _(_144.getHours()%12||12);
case "j":
return _(dojo.date.getDayOfYear(_144),3);
case "k":
if(_147==null){
_147=" ";
}
return _(_144.getHours());
case "l":
if(_147==null){
_147=" ";
}
return _(_144.getHours()%12||12);
case "m":
return _(_144.getMonth()+1);
case "M":
return _(_144.getMinutes());
case "n":
return "\n";
case "p":
return info[_144.getHours()<12?"am":"pm"];
case "r":
return $("I")+":"+$("M")+":"+$("S")+" "+$("p");
case "R":
return $("H")+":"+$("M");
case "S":
return _(_144.getSeconds());
case "t":
return "\t";
case "T":
return $("H")+":"+$("M")+":"+$("S");
case "u":
return String(_144.getDay()||7);
case "U":
return _(dojo.date.getWeekOfYear(_144));
case "V":
return _(dojo.date.getIsoWeekOfYear(_144));
case "W":
return _(dojo.date.getWeekOfYear(_144,1));
case "w":
return String(_144.getDay());
case "x":
return dojo.date.format(_144,{selector:"dateOnly",locale:_146});
case "X":
return dojo.date.format(_144,{selector:"timeOnly",locale:_146});
case "y":
return _(_144.getFullYear()%100);
case "Y":
return String(_144.getFullYear());
case "z":
var _14c=_144.getTimezoneOffset();
return (_14c>0?"-":"+")+_(Math.floor(Math.abs(_14c)/60))+":"+_(Math.abs(_14c)%60);
case "Z":
return dojo.date.getTimezoneName(_144);
case "%":
return "%";
}
}
var _14d="";
var i=0;
var _14f=0;
var _150=null;
while((_14f=_145.indexOf("%",i))!=-1){
_14d+=_145.substring(i,_14f++);
switch(_145.charAt(_14f++)){
case "_":
_147=" ";
break;
case "-":
_147="";
break;
case "0":
_147="0";
break;
case "^":
_150="upper";
break;
case "*":
_150="lower";
break;
case "#":
_150="swap";
break;
default:
_147=null;
_14f--;
break;
}
var _151=$(_145.charAt(_14f++));
switch(_150){
case "upper":
_151=_151.toUpperCase();
break;
case "lower":
_151=_151.toLowerCase();
break;
case "swap":
var _152=_151.toLowerCase();
var _153="";
var j=0;
var ch="";
while(j<_151.length){
ch=_151.charAt(j);
_153+=(ch==_152.charAt(j))?ch.toUpperCase():ch.toLowerCase();
j++;
}
_151=_153;
break;
default:
break;
}
_150=null;
_14d+=_151;
i=_14f;
}
_14d+=_145.substring(i);
return _14d;
};
(function(){
var _156=[];
dojo.date.addCustomFormats=function(_157,_158){
_156.push({pkg:_157,name:_158});
};
dojo.date._getGregorianBundle=function(_159){
var _15a={};
dojo.lang.forEach(_156,function(desc){
var _15c=dojo.i18n.getLocalization(desc.pkg,desc.name,_159);
_15a=dojo.lang.mixin(_15a,_15c);
},this);
return _15a;
};
})();
dojo.date.addCustomFormats("dojo.i18n.calendar","gregorian");
dojo.date.addCustomFormats("dojo.i18n.calendar","gregorianExtras");
dojo.date.getNames=function(item,type,use,_160){
var _161;
var _162=dojo.date._getGregorianBundle(_160);
var _163=[item,use,type];
if(use=="standAlone"){
_161=_162[_163.join("-")];
}
_163[1]="format";
return (_161||_162[_163.join("-")]).concat();
};
dojo.date.getDayName=function(_164,_165){
return dojo.date.getNames("days","wide","format",_165)[_164.getDay()];
};
dojo.date.getDayShortName=function(_166,_167){
return dojo.date.getNames("days","abbr","format",_167)[_166.getDay()];
};
dojo.date.getMonthName=function(_168,_169){
return dojo.date.getNames("months","wide","format",_169)[_168.getMonth()];
};
dojo.date.getMonthShortName=function(_16a,_16b){
return dojo.date.getNames("months","abbr","format",_16b)[_16a.getMonth()];
};
dojo.date.toRelativeString=function(_16c){
var now=new Date();
var diff=(now-_16c)/1000;
var end=" ago";
var _170=false;
if(diff<0){
_170=true;
end=" from now";
diff=-diff;
}
if(diff<60){
diff=Math.round(diff);
return diff+" second"+(diff==1?"":"s")+end;
}
if(diff<60*60){
diff=Math.round(diff/60);
return diff+" minute"+(diff==1?"":"s")+end;
}
if(diff<60*60*24){
diff=Math.round(diff/3600);
return diff+" hour"+(diff==1?"":"s")+end;
}
if(diff<60*60*24*7){
diff=Math.round(diff/(3600*24));
if(diff==1){
return _170?"Tomorrow":"Yesterday";
}else{
return diff+" days"+end;
}
}
return dojo.date.format(_16c);
};
dojo.date.toSql=function(_171,_172){
return dojo.date.strftime(_171,"%F"+!_172?" %T":"");
};
dojo.date.fromSql=function(_173){
var _174=_173.split(/[\- :]/g);
while(_174.length<6){
_174.push(0);
}
return new Date(_174[0],(parseInt(_174[1],10)-1),_174[2],_174[3],_174[4],_174[5]);
};
dojo.provide("dojo.date.serialize");
dojo.require("dojo.string.common");
dojo.date.setIso8601=function(_175,_176){
var _177=(_176.indexOf("T")==-1)?_176.split(" "):_176.split("T");
_175=dojo.date.setIso8601Date(_175,_177[0]);
if(_177.length==2){
_175=dojo.date.setIso8601Time(_175,_177[1]);
}
return _175;
};
dojo.date.fromIso8601=function(_178){
return dojo.date.setIso8601(new Date(0,0),_178);
};
dojo.date.setIso8601Date=function(_179,_17a){
var _17b="^([0-9]{4})((-?([0-9]{2})(-?([0-9]{2}))?)|"+"(-?([0-9]{3}))|(-?W([0-9]{2})(-?([1-7]))?))?$";
var d=_17a.match(new RegExp(_17b));
if(!d){
dojo.debug("invalid date string: "+_17a);
return null;
}
var year=d[1];
var _17e=d[4];
var date=d[6];
var _180=d[8];
var week=d[10];
var _182=d[12]?d[12]:1;
_179.setFullYear(year);
if(_180){
_179.setMonth(0);
_179.setDate(Number(_180));
}else{
if(week){
_179.setMonth(0);
_179.setDate(1);
var gd=_179.getDay();
var day=gd?gd:7;
var _185=Number(_182)+(7*Number(week));
if(day<=4){
_179.setDate(_185+1-day);
}else{
_179.setDate(_185+8-day);
}
}else{
if(_17e){
_179.setDate(1);
_179.setMonth(_17e-1);
}
if(date){
_179.setDate(date);
}
}
}
return _179;
};
dojo.date.fromIso8601Date=function(_186){
return dojo.date.setIso8601Date(new Date(0,0),_186);
};
dojo.date.setIso8601Time=function(_187,_188){
var _189="Z|(([-+])([0-9]{2})(:?([0-9]{2}))?)$";
var d=_188.match(new RegExp(_189));
var _18b=0;
if(d){
if(d[0]!="Z"){
_18b=(Number(d[3])*60)+Number(d[5]);
_18b*=((d[2]=="-")?1:-1);
}
_18b-=_187.getTimezoneOffset();
_188=_188.substr(0,_188.length-d[0].length);
}
var _18c="^([0-9]{2})(:?([0-9]{2})(:?([0-9]{2})(.([0-9]+))?)?)?$";
d=_188.match(new RegExp(_18c));
if(!d){
dojo.debug("invalid time string: "+_188);
return null;
}
var _18d=d[1];
var mins=Number((d[3])?d[3]:0);
var secs=(d[5])?d[5]:0;
var ms=d[7]?(Number("0."+d[7])*1000):0;
_187.setHours(_18d);
_187.setMinutes(mins);
_187.setSeconds(secs);
_187.setMilliseconds(ms);
if(_18b!==0){
_187.setTime(_187.getTime()+_18b*60000);
}
return _187;
};
dojo.date.fromIso8601Time=function(_191){
return dojo.date.setIso8601Time(new Date(0,0),_191);
};
dojo.date.toRfc3339=function(_192,_193){
if(!_192){
_192=new Date();
}
var _=dojo.string.pad;
var _195=[];
if(_193!="timeOnly"){
var date=[_(_192.getFullYear(),4),_(_192.getMonth()+1,2),_(_192.getDate(),2)].join("-");
_195.push(date);
}
if(_193!="dateOnly"){
var time=[_(_192.getHours(),2),_(_192.getMinutes(),2),_(_192.getSeconds(),2)].join(":");
var _198=_192.getTimezoneOffset();
time+=(_198>0?"-":"+")+_(Math.floor(Math.abs(_198)/60),2)+":"+_(Math.abs(_198)%60,2);
_195.push(time);
}
return _195.join("T");
};
dojo.date.fromRfc3339=function(_199){
if(_199.indexOf("Tany")!=-1){
_199=_199.replace("Tany","");
}
var _19a=new Date();
return dojo.date.setIso8601(_19a,_199);
};
dojo.provide("dojo.validate.datetime");
dojo.validate.isValidTime=function(_19b,_19c){
dojo.deprecated("dojo.validate.datetime","use dojo.date.parse instead","0.5");
var re=new RegExp("^"+dojo.regexp.time(_19c)+"$","i");
return re.test(_19b);
};
dojo.validate.is12HourTime=function(_19e){
dojo.deprecated("dojo.validate.datetime","use dojo.date.parse instead","0.5");
return dojo.validate.isValidTime(_19e,{format:["h:mm:ss t","h:mm t"]});
};
dojo.validate.is24HourTime=function(_19f){
dojo.deprecated("dojo.validate.datetime","use dojo.date.parse instead","0.5");
return dojo.validate.isValidTime(_19f,{format:["HH:mm:ss","HH:mm"]});
};
dojo.validate.isValidDate=function(_1a0,_1a1){
dojo.deprecated("dojo.validate.datetime","use dojo.date.parse instead","0.5");
if(typeof _1a1=="object"&&typeof _1a1.format=="string"){
_1a1=_1a1.format;
}
if(typeof _1a1!="string"){
_1a1="MM/DD/YYYY";
}
var _1a2=_1a1.replace(/([$^.*+?=!:|\/\\\(\)\[\]\{\}])/g,"\\$1");
_1a2=_1a2.replace("YYYY","([0-9]{4})");
_1a2=_1a2.replace("MM","(0[1-9]|10|11|12)");
_1a2=_1a2.replace("M","([1-9]|10|11|12)");
_1a2=_1a2.replace("DDD","(00[1-9]|0[1-9][0-9]|[12][0-9][0-9]|3[0-5][0-9]|36[0-6])");
_1a2=_1a2.replace("DD","(0[1-9]|[12][0-9]|30|31)");
_1a2=_1a2.replace("D","([1-9]|[12][0-9]|30|31)");
_1a2=_1a2.replace("ww","(0[1-9]|[1-4][0-9]|5[0-3])");
_1a2=_1a2.replace("d","([1-7])");
_1a2="^"+_1a2+"$";
var re=new RegExp(_1a2);
if(!re.test(_1a0)){
return false;
}
var year=0,_1a5=1,date=1,_1a7=1,week=1,day=1;
var _1aa=_1a1.match(/(YYYY|MM|M|DDD|DD|D|ww|d)/g);
var _1ab=re.exec(_1a0);
for(var i=0;i<_1aa.length;i++){
switch(_1aa[i]){
case "YYYY":
year=Number(_1ab[i+1]);
break;
case "M":
case "MM":
_1a5=Number(_1ab[i+1]);
break;
case "D":
case "DD":
date=Number(_1ab[i+1]);
break;
case "DDD":
_1a7=Number(_1ab[i+1]);
break;
case "ww":
week=Number(_1ab[i+1]);
break;
case "d":
day=Number(_1ab[i+1]);
break;
}
}
var _1ad=(year%4==0&&(year%100!=0||year%400==0));
if(date==31&&(_1a5==4||_1a5==6||_1a5==9||_1a5==11)){
return false;
}
if(date>=30&&_1a5==2){
return false;
}
if(date==29&&_1a5==2&&!_1ad){
return false;
}
if(_1a7==366&&!_1ad){
return false;
}
return true;
};
dojo.provide("dojo.validate.web");
dojo.validate.isIpAddress=function(_1ae,_1af){
var re=new RegExp("^"+dojo.regexp.ipAddress(_1af)+"$","i");
return re.test(_1ae);
};
dojo.validate.isUrl=function(_1b1,_1b2){
var re=new RegExp("^"+dojo.regexp.url(_1b2)+"$","i");
return re.test(_1b1);
};
dojo.validate.isEmailAddress=function(_1b4,_1b5){
var re=new RegExp("^"+dojo.regexp.emailAddress(_1b5)+"$","i");
return re.test(_1b4);
};
dojo.validate.isEmailAddressList=function(_1b7,_1b8){
var re=new RegExp("^"+dojo.regexp.emailAddressList(_1b8)+"$","i");
return re.test(_1b7);
};
dojo.validate.getEmailAddressList=function(_1ba,_1bb){
if(!_1bb){
_1bb={};
}
if(!_1bb.listSeparator){
_1bb.listSeparator="\\s;,";
}
if(dojo.validate.isEmailAddressList(_1ba,_1bb)){
return _1ba.split(new RegExp("\\s*["+_1bb.listSeparator+"]\\s*"));
}
return [];
};
dojo.provide("dojo.validate.creditCard");
dojo.require("dojo.lang.common");
dojo.validate.isValidCreditCard=function(_1bc,_1bd){
if(_1bc&&_1bd&&((_1bd.toLowerCase()=="er"||dojo.validate.isValidLuhn(_1bc))&&(dojo.validate.isValidCreditCardNumber(_1bc,_1bd.toLowerCase())))){
return true;
}
return false;
};
dojo.validate.isValidCreditCardNumber=function(_1be,_1bf){
if(typeof _1be!="string"){
_1be=String(_1be);
}
_1be=_1be.replace(/[- ]/g,"");
var _1c0=[];
var _1c1={"mc":"5[1-5][0-9]{14}","ec":"5[1-5][0-9]{14}","vi":"4([0-9]{12}|[0-9]{15})","ax":"3[47][0-9]{13}","dc":"3(0[0-5][0-9]{11}|[68][0-9]{12})","bl":"3(0[0-5][0-9]{11}|[68][0-9]{12})","di":"6011[0-9]{12}","jcb":"(3[0-9]{15}|(2131|1800)[0-9]{11})","er":"2(014|149)[0-9]{11}"};
if(_1bf&&dojo.lang.has(_1c1,_1bf.toLowerCase())){
return Boolean(_1be.match(_1c1[_1bf.toLowerCase()]));
}else{
for(var p in _1c1){
if(_1be.match("^"+_1c1[p]+"$")!=null){
_1c0.push(p);
}
}
return (_1c0.length)?_1c0.join("|"):false;
}
};
dojo.validate.isValidCvv=function(_1c3,_1c4){
if(typeof _1c3!="string"){
_1c3=String(_1c3);
}
var _1c5;
switch(_1c4.toLowerCase()){
case "mc":
case "ec":
case "vi":
case "di":
_1c5="###";
break;
case "ax":
_1c5="####";
break;
default:
return false;
}
var _1c6={format:_1c5};
if((_1c3.length==_1c5.length)&&(dojo.validate.isNumberFormat(_1c3,_1c6))){
return true;
}
return false;
};
dojo.provide("dojo.validate.us");
dojo.validate.us.isCurrency=function(_1c7,_1c8){
return dojo.validate.isCurrency(_1c7,_1c8);
};
dojo.validate.us.isState=function(_1c9,_1ca){
var re=new RegExp("^"+dojo.regexp.us.state(_1ca)+"$","i");
return re.test(_1c9);
};
dojo.validate.us.isPhoneNumber=function(_1cc){
var _1cd={format:["###-###-####","(###) ###-####","(###) ### ####","###.###.####","###/###-####","### ### ####","###-###-#### x#???","(###) ###-#### x#???","(###) ### #### x#???","###.###.#### x#???","###/###-#### x#???","### ### #### x#???","##########"]};
return dojo.validate.isNumberFormat(_1cc,_1cd);
};
dojo.validate.us.isSocialSecurityNumber=function(_1ce){
var _1cf={format:["###-##-####","### ## ####","#########"]};
return dojo.validate.isNumberFormat(_1ce,_1cf);
};
dojo.validate.us.isZipCode=function(_1d0){
var _1d1={format:["#####-####","##### ####","#########","#####"]};
return dojo.validate.isNumberFormat(_1d0,_1d1);
};
