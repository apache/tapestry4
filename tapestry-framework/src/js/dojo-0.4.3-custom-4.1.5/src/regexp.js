dojo.provide("dojo.regexp");
dojo.evalObjPath("dojo.regexp.us",true);
dojo.regexp.tld=function(_1){
_1=(typeof _1=="object")?_1:{};
if(typeof _1.allowCC!="boolean"){
_1.allowCC=true;
}
if(typeof _1.allowInfra!="boolean"){
_1.allowInfra=true;
}
if(typeof _1.allowGeneric!="boolean"){
_1.allowGeneric=true;
}
var _2="arpa";
var _3="aero|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|xxx|jobs|mobi|post";
var _4="ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|"+"bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cu|cv|cx|cy|cz|de|dj|dk|dm|do|dz|"+"ec|ee|eg|er|eu|es|et|fi|fj|fk|fm|fo|fr|ga|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|"+"gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kr|kw|ky|kz|"+"la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|"+"my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|"+"re|ro|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sk|sl|sm|sn|sr|st|su|sv|sy|sz|tc|td|tf|tg|th|tj|tk|tm|"+"tn|to|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|yu|za|zm|zw";
var a=[];
if(_1.allowInfra){
a.push(_2);
}
if(_1.allowGeneric){
a.push(_3);
}
if(_1.allowCC){
a.push(_4);
}
var _6="";
if(a.length>0){
_6="("+a.join("|")+")";
}
return _6;
};
dojo.regexp.ipAddress=function(_7){
_7=(typeof _7=="object")?_7:{};
if(typeof _7.allowDottedDecimal!="boolean"){
_7.allowDottedDecimal=true;
}
if(typeof _7.allowDottedHex!="boolean"){
_7.allowDottedHex=true;
}
if(typeof _7.allowDottedOctal!="boolean"){
_7.allowDottedOctal=true;
}
if(typeof _7.allowDecimal!="boolean"){
_7.allowDecimal=true;
}
if(typeof _7.allowHex!="boolean"){
_7.allowHex=true;
}
if(typeof _7.allowIPv6!="boolean"){
_7.allowIPv6=true;
}
if(typeof _7.allowHybrid!="boolean"){
_7.allowHybrid=true;
}
var _8="((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])";
var _9="(0[xX]0*[\\da-fA-F]?[\\da-fA-F]\\.){3}0[xX]0*[\\da-fA-F]?[\\da-fA-F]";
var _a="(0+[0-3][0-7][0-7]\\.){3}0+[0-3][0-7][0-7]";
var _b="(0|[1-9]\\d{0,8}|[1-3]\\d{9}|4[01]\\d{8}|42[0-8]\\d{7}|429[0-3]\\d{6}|"+"4294[0-8]\\d{5}|42949[0-5]\\d{4}|429496[0-6]\\d{3}|4294967[01]\\d{2}|42949672[0-8]\\d|429496729[0-5])";
var _c="0[xX]0*[\\da-fA-F]{1,8}";
var _d="([\\da-fA-F]{1,4}\\:){7}[\\da-fA-F]{1,4}";
var _e="([\\da-fA-F]{1,4}\\:){6}"+"((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])";
var a=[];
if(_7.allowDottedDecimal){
a.push(_8);
}
if(_7.allowDottedHex){
a.push(_9);
}
if(_7.allowDottedOctal){
a.push(_a);
}
if(_7.allowDecimal){
a.push(_b);
}
if(_7.allowHex){
a.push(_c);
}
if(_7.allowIPv6){
a.push(_d);
}
if(_7.allowHybrid){
a.push(_e);
}
var _10="";
if(a.length>0){
_10="("+a.join("|")+")";
}
return _10;
};
dojo.regexp.host=function(_11){
_11=(typeof _11=="object")?_11:{};
if(typeof _11.allowIP!="boolean"){
_11.allowIP=true;
}
if(typeof _11.allowLocal!="boolean"){
_11.allowLocal=false;
}
if(typeof _11.allowPort!="boolean"){
_11.allowPort=true;
}
var _12="([0-9a-zA-Z]([-0-9a-zA-Z]{0,61}[0-9a-zA-Z])?\\.)+"+dojo.regexp.tld(_11);
var _13=(_11.allowPort)?"(\\:"+dojo.regexp.integer({signed:false})+")?":"";
var _14=_12;
if(_11.allowIP){
_14+="|"+dojo.regexp.ipAddress(_11);
}
if(_11.allowLocal){
_14+="|localhost";
}
return "("+_14+")"+_13;
};
dojo.regexp.url=function(_15){
_15=(typeof _15=="object")?_15:{};
if(typeof _15.scheme=="undefined"){
_15.scheme=[true,false];
}
var _16=dojo.regexp.buildGroupRE(_15.scheme,function(q){
if(q){
return "(https?|ftps?)\\://";
}
return "";
});
var _18="(/([^?#\\s/]+/)*)?([^?#\\s/]+(\\?[^?#\\s/]*)?(#[A-Za-z][\\w.:-]*)?)?";
return _16+dojo.regexp.host(_15)+_18;
};
dojo.regexp.emailAddress=function(_19){
_19=(typeof _19=="object")?_19:{};
if(typeof _19.allowCruft!="boolean"){
_19.allowCruft=false;
}
_19.allowPort=false;
var _1a="([\\da-z]+[-._+&'])*[\\da-z]+";
var _1b=_1a+"@"+dojo.regexp.host(_19);
if(_19.allowCruft){
_1b="<?(mailto\\:)?"+_1b+">?";
}
return _1b;
};
dojo.regexp.emailAddressList=function(_1c){
_1c=(typeof _1c=="object")?_1c:{};
if(typeof _1c.listSeparator!="string"){
_1c.listSeparator="\\s;,";
}
var _1d=dojo.regexp.emailAddress(_1c);
var _1e="("+_1d+"\\s*["+_1c.listSeparator+"]\\s*)*"+_1d+"\\s*["+_1c.listSeparator+"]?\\s*";
return _1e;
};
dojo.regexp.integer=function(_1f){
_1f=(typeof _1f=="object")?_1f:{};
if(typeof _1f.signed=="undefined"){
_1f.signed=[true,false];
}
if(typeof _1f.separator=="undefined"){
_1f.separator="";
}else{
if(typeof _1f.groupSize=="undefined"){
_1f.groupSize=3;
}
}
var _20=dojo.regexp.buildGroupRE(_1f.signed,function(q){
return q?"[-+]":"";
});
var _22=dojo.regexp.buildGroupRE(_1f.separator,function(sep){
if(sep==""){
return "(0|[1-9]\\d*)";
}
var grp=_1f.groupSize,_25=_1f.groupSize2;
if(typeof _25!="undefined"){
var _26="(0|[1-9]\\d{0,"+(_25-1)+"}(["+sep+"]\\d{"+_25+"})*["+sep+"]\\d{"+grp+"})";
return ((grp-_25)>0)?"("+_26+"|(0|[1-9]\\d{0,"+(grp-1)+"}))":_26;
}
return "(0|[1-9]\\d{0,"+(grp-1)+"}(["+sep+"]\\d{"+grp+"})*)";
});
return _20+_22;
};
dojo.regexp.realNumber=function(_27){
_27=(typeof _27=="object")?_27:{};
if(typeof _27.places!="number"){
_27.places=Infinity;
}
if(typeof _27.decimal!="string"){
_27.decimal=".";
}
if(typeof _27.fractional=="undefined"){
_27.fractional=[true,false];
}
if(typeof _27.exponent=="undefined"){
_27.exponent=[true,false];
}
if(typeof _27.eSigned=="undefined"){
_27.eSigned=[true,false];
}
var _28=dojo.regexp.integer(_27);
var _29=dojo.regexp.buildGroupRE(_27.fractional,function(q){
var re="";
if(q&&(_27.places>0)){
re="\\"+_27.decimal;
if(_27.places==Infinity){
re="("+re+"\\d+)?";
}else{
re=re+"\\d{"+_27.places+"}";
}
}
return re;
});
var _2c=dojo.regexp.buildGroupRE(_27.exponent,function(q){
if(q){
return "([eE]"+dojo.regexp.integer({signed:_27.eSigned})+")";
}
return "";
});
return _28+_29+_2c;
};
dojo.regexp.currency=function(_2e){
_2e=(typeof _2e=="object")?_2e:{};
if(typeof _2e.signed=="undefined"){
_2e.signed=[true,false];
}
if(typeof _2e.symbol=="undefined"){
_2e.symbol="$";
}
if(typeof _2e.placement!="string"){
_2e.placement="before";
}
if(typeof _2e.signPlacement!="string"){
_2e.signPlacement="before";
}
if(typeof _2e.separator=="undefined"){
_2e.separator=",";
}
if(typeof _2e.fractional=="undefined"&&typeof _2e.cents!="undefined"){
dojo.deprecated("dojo.regexp.currency: flags.cents","use flags.fractional instead","0.5");
_2e.fractional=_2e.cents;
}
if(typeof _2e.decimal!="string"){
_2e.decimal=".";
}
var _2f=dojo.regexp.buildGroupRE(_2e.signed,function(q){
if(q){
return "[-+]";
}
return "";
});
var _31=dojo.regexp.buildGroupRE(_2e.symbol,function(_32){
return "\\s?"+_32.replace(/([.$?*!=:|\\\/^])/g,"\\$1")+"\\s?";
});
switch(_2e.signPlacement){
case "before":
_31=_2f+_31;
break;
case "after":
_31=_31+_2f;
break;
}
var _33=_2e;
_33.signed=false;
_33.exponent=false;
var _34=dojo.regexp.realNumber(_33);
var _35;
switch(_2e.placement){
case "before":
_35=_31+_34;
break;
case "after":
_35=_34+_31;
break;
}
switch(_2e.signPlacement){
case "around":
_35="("+_35+"|"+"\\("+_35+"\\)"+")";
break;
case "begin":
_35=_2f+_35;
break;
case "end":
_35=_35+_2f;
break;
}
return _35;
};
dojo.regexp.us.state=function(_36){
_36=(typeof _36=="object")?_36:{};
if(typeof _36.allowTerritories!="boolean"){
_36.allowTerritories=true;
}
if(typeof _36.allowMilitary!="boolean"){
_36.allowMilitary=true;
}
var _37="AL|AK|AZ|AR|CA|CO|CT|DE|DC|FL|GA|HI|ID|IL|IN|IA|KS|KY|LA|ME|MD|MA|MI|MN|MS|MO|MT|"+"NE|NV|NH|NJ|NM|NY|NC|ND|OH|OK|OR|PA|RI|SC|SD|TN|TX|UT|VT|VA|WA|WV|WI|WY";
var _38="AS|FM|GU|MH|MP|PW|PR|VI";
var _39="AA|AE|AP";
if(_36.allowTerritories){
_37+="|"+_38;
}
if(_36.allowMilitary){
_37+="|"+_39;
}
return "("+_37+")";
};
dojo.regexp.time=function(_3a){
dojo.deprecated("dojo.regexp.time","Use dojo.date.parse instead","0.5");
_3a=(typeof _3a=="object")?_3a:{};
if(typeof _3a.format=="undefined"){
_3a.format="h:mm:ss t";
}
if(typeof _3a.amSymbol!="string"){
_3a.amSymbol="AM";
}
if(typeof _3a.pmSymbol!="string"){
_3a.pmSymbol="PM";
}
var _3b=function(_3c){
_3c=_3c.replace(/([.$?*!=:|{}\(\)\[\]\\\/^])/g,"\\$1");
var _3d=_3a.amSymbol.replace(/([.$?*!=:|{}\(\)\[\]\\\/^])/g,"\\$1");
var _3e=_3a.pmSymbol.replace(/([.$?*!=:|{}\(\)\[\]\\\/^])/g,"\\$1");
_3c=_3c.replace("hh","(0[1-9]|1[0-2])");
_3c=_3c.replace("h","([1-9]|1[0-2])");
_3c=_3c.replace("HH","([01][0-9]|2[0-3])");
_3c=_3c.replace("H","([0-9]|1[0-9]|2[0-3])");
_3c=_3c.replace("mm","([0-5][0-9])");
_3c=_3c.replace("m","([1-5][0-9]|[0-9])");
_3c=_3c.replace("ss","([0-5][0-9])");
_3c=_3c.replace("s","([1-5][0-9]|[0-9])");
_3c=_3c.replace("t","\\s?("+_3d+"|"+_3e+")\\s?");
return _3c;
};
return dojo.regexp.buildGroupRE(_3a.format,_3b);
};
dojo.regexp.numberFormat=function(_3f){
_3f=(typeof _3f=="object")?_3f:{};
if(typeof _3f.format=="undefined"){
_3f.format="###-###-####";
}
var _40=function(_41){
_41=_41.replace(/([.$*!=:|{}\(\)\[\]\\\/^])/g,"\\$1");
_41=_41.replace(/\?/g,"\\d?");
_41=_41.replace(/#/g,"\\d");
return _41;
};
return dojo.regexp.buildGroupRE(_3f.format,_40);
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
