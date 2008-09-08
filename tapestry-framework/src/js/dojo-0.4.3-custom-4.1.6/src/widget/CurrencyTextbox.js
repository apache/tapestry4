dojo.provide("dojo.widget.CurrencyTextbox");
dojo.require("dojo.widget.IntegerTextbox");
dojo.require("dojo.validate.common");
dojo.widget.defineWidget("dojo.widget.CurrencyTextbox",dojo.widget.IntegerTextbox,{mixInProperties:function(_1,_2){
dojo.widget.CurrencyTextbox.superclass.mixInProperties.apply(this,arguments);
if(_1.fractional){
this.flags.fractional=(_1.fractional=="true");
}else{
if(_1.cents){
dojo.deprecated("dojo.widget.IntegerTextbox","use fractional attr instead of cents","0.5");
this.flags.fractional=(_1.cents=="true");
}
}
if(_1.symbol){
this.flags.symbol=_1.symbol;
}
if(_1.min){
this.flags.min=parseFloat(_1.min);
}
if(_1.max){
this.flags.max=parseFloat(_1.max);
}
},isValid:function(){
return dojo.validate.isCurrency(this.textbox.value,this.flags);
},isInRange:function(){
return dojo.validate.isInRange(this.textbox.value,this.flags);
}});
