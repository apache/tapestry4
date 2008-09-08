dojo.provide("dojo.validate.common");
dojo.require("dojo.regexp");
dojo.validate.isText=function(_1,_2){
_2=(typeof _2=="object")?_2:{};
if(/^\s*$/.test(_1)){
return false;
}
if(typeof _2.length=="number"&&_2.length!=_1.length){
return false;
}
if(typeof _2.minlength=="number"&&_2.minlength>_1.length){
return false;
}
if(typeof _2.maxlength=="number"&&_2.maxlength<_1.length){
return false;
}
return true;
};
dojo.validate.isInteger=function(_3,_4){
var re=new RegExp("^"+dojo.regexp.integer(_4)+"$");
return re.test(_3);
};
dojo.validate.isRealNumber=function(_6,_7){
var re=new RegExp("^"+dojo.regexp.realNumber(_7)+"$");
return re.test(_6);
};
dojo.validate.isCurrency=function(_9,_a){
var re=new RegExp("^"+dojo.regexp.currency(_a)+"$");
return re.test(_9);
};
dojo.validate._isInRangeCache={};
dojo.validate.isInRange=function(_c,_d){
_c=_c.replace(dojo.lang.has(_d,"separator")?_d.separator:",","","g").replace(dojo.lang.has(_d,"symbol")?_d.symbol:"$","");
if(isNaN(_c)){
return false;
}
_d=(typeof _d=="object")?_d:{};
var _e=(typeof _d.max=="number")?_d.max:Infinity;
var _f=(typeof _d.min=="number")?_d.min:-Infinity;
var dec=(typeof _d.decimal=="string")?_d.decimal:".";
var _11=dojo.validate._isInRangeCache;
var _12=_c+"max"+_e+"min"+_f+"dec"+dec;
if(typeof _11[_12]!="undefined"){
return _11[_12];
}
var _13="[^"+dec+"\\deE+-]";
_c=_c.replace(RegExp(_13,"g"),"");
_c=_c.replace(/^([+-]?)(\D*)/,"$1");
_c=_c.replace(/(\D*)$/,"");
_13="(\\d)["+dec+"](\\d)";
_c=_c.replace(RegExp(_13,"g"),"$1.$2");
_c=Number(_c);
if(_c<_f||_c>_e){
_11[_12]=false;
return false;
}
_11[_12]=true;
return true;
};
dojo.validate.isNumberFormat=function(_14,_15){
var re=new RegExp("^"+dojo.regexp.numberFormat(_15)+"$","i");
return re.test(_14);
};
dojo.validate.isValidLuhn=function(_17){
var sum,_19,_1a;
if(typeof _17!="string"){
_17=String(_17);
}
_17=_17.replace(/[- ]/g,"");
_19=_17.length%2;
sum=0;
for(var i=0;i<_17.length;i++){
_1a=parseInt(_17.charAt(i));
if(i%2==_19){
_1a*=2;
}
if(_1a>9){
_1a-=9;
}
sum+=_1a;
}
return !(sum%10);
};
