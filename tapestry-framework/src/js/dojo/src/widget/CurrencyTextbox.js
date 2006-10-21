

dojo.provide("dojo.widget.CurrencyTextbox");

dojo.require("dojo.widget.IntegerTextbox");
dojo.require("dojo.validate.common");


dojo.widget.defineWidget(
"dojo.widget.CurrencyTextbox",
dojo.widget.IntegerTextbox,
{
mixInProperties: function(localProperties, frag){
// First initialize properties in super-class.
dojo.widget.CurrencyTextbox.superclass.mixInProperties.apply(this, arguments);

// Get properties from markup attributes, and assign to flags object.
if(localProperties.fractional){
this.flags.fractional = (localProperties.fractional == "true");
}else if(localProperties.cents){
dojo.deprecated("dojo.widget.IntegerTextbox", "use fractional attr instead of cents", "0.5");
this.flags.fractional = (localProperties.cents == "true");
}
if(localProperties.symbol){
this.flags.symbol = localProperties.symbol;
}
if(localProperties.min){
this.flags.min = parseFloat(localProperties.min);
}
if(localProperties.max){
this.flags.max = parseFloat(localProperties.max);
}
},

// Over-ride for currency validation
isValid: function(){
return dojo.validate.isCurrency(this.textbox.value, this.flags);
},
isInRange: function(){
return dojo.validate.isInRange(this.textbox.value, this.flags);
}
}
);
