dojo.provide("dojo.validate.creditCard");
dojo.require("dojo.lang.common");
dojo.require("dojo.validate.common");
dojo.validate.isValidCreditCard=function(_1,_2){
if(_1&&_2&&((_2.toLowerCase()=="er"||dojo.validate.isValidLuhn(_1))&&(dojo.validate.isValidCreditCardNumber(_1,_2.toLowerCase())))){
return true;
}
return false;
};
dojo.validate.isValidCreditCardNumber=function(_3,_4){
if(typeof _3!="string"){
_3=String(_3);
}
_3=_3.replace(/[- ]/g,"");
var _5=[];
var _6={"mc":"5[1-5][0-9]{14}","ec":"5[1-5][0-9]{14}","vi":"4([0-9]{12}|[0-9]{15})","ax":"3[47][0-9]{13}","dc":"3(0[0-5][0-9]{11}|[68][0-9]{12})","bl":"3(0[0-5][0-9]{11}|[68][0-9]{12})","di":"6011[0-9]{12}","jcb":"(3[0-9]{15}|(2131|1800)[0-9]{11})","er":"2(014|149)[0-9]{11}"};
if(_4&&dojo.lang.has(_6,_4.toLowerCase())){
return Boolean(_3.match(_6[_4.toLowerCase()]));
}else{
for(var p in _6){
if(_3.match("^"+_6[p]+"$")!=null){
_5.push(p);
}
}
return (_5.length)?_5.join("|"):false;
}
};
dojo.validate.isValidCvv=function(_8,_9){
if(typeof _8!="string"){
_8=String(_8);
}
var _a;
switch(_9.toLowerCase()){
case "mc":
case "ec":
case "vi":
case "di":
_a="###";
break;
case "ax":
_a="####";
break;
default:
return false;
}
var _b={format:_a};
if((_8.length==_a.length)&&(dojo.validate.isNumberFormat(_8,_b))){
return true;
}
return false;
};
