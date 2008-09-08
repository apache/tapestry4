dojo.provide("dojo.validate.de");
dojo.require("dojo.validate.common");
dojo.validate.isGermanCurrency=function(_1){
var _2={symbol:"\u20ac",placement:"after",signPlacement:"begin",decimal:",",separator:"."};
return dojo.validate.isCurrency(_1,_2);
};
