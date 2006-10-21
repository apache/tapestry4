

dojo.provide("dojo.widget.RealNumberTextbox");

dojo.require("dojo.widget.IntegerTextbox");
dojo.require("dojo.validate.common");


dojo.widget.defineWidget(
"dojo.widget.RealNumberTextbox",
dojo.widget.IntegerTextbox,
{
mixInProperties: function(localProperties, frag){
// First initialize properties in super-class.
dojo.widget.RealNumberTextbox.superclass.mixInProperties.apply(this, arguments);

// Get properties from markup attributes, and assign to flags object.
if (localProperties.places){
this.flags.places = Number(localProperties.places);
}
if((localProperties.exponent == "true")||
(localProperties.exponent == "always")){
this.flags.exponent = true;
}else if((localProperties.exponent == "false")||(localProperties.exponent == "never")){
this.flags.exponent = false;
}else{
this.flags.exponent = [ true, false ]; // optional
}
if((localProperties.esigned == "true")||(localProperties.esigned == "always")){
this.flags.eSigned = true;
}else if((localProperties.esigned == "false")||(localProperties.esigned == "never")){
this.flags.eSigned = false;
}else{
this.flags.eSigned = [ true, false ]; // optional
}
if(localProperties.min){
this.flags.min = parseFloat(localProperties.min);
}
if(localProperties.max){
this.flags.max = parseFloat(localProperties.max);
}
},

// Over-ride for real number validation
isValid: function(){
return dojo.validate.isRealNumber(this.textbox.value, this.flags);
},
isInRange: function(){
return dojo.validate.isInRange(this.textbox.value, this.flags);
}

}
);
