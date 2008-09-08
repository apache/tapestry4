dojo.provide("dojo.validate.jp");
dojo.require("dojo.validate.common");
dojo.validate.isJapaneseCurrency=function(_1){
var _2={symbol:"\xa5",fractional:false};
return dojo.validate.isCurrency(_1,_2);
};
