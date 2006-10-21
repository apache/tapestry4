

dojo.provide("dojo.widget.IntegerTextbox");

dojo.require("dojo.widget.ValidationTextbox");
dojo.require("dojo.validate.common");


dojo.widget.defineWidget(
"dojo.widget.IntegerTextbox",
dojo.widget.ValidationTextbox,
{
mixInProperties: function(localProperties, frag){
// First initialize properties in super-class.
dojo.widget.IntegerTextbox.superclass.mixInProperties.apply(this, arguments);

// Get properties from markup attributes, and assign to flags object.
if((localProperties.signed == "true")||
(localProperties.signed == "always")){
this.flags.signed = true;
}else if((localProperties.signed == "false")||
(localProperties.signed == "never")){
this.flags.signed = false;
this.flags.min = 0;
}else{
this.flags.signed = [ true, false ]; // optional
}
if(localProperties.separator){
this.flags.separator = localProperties.separator;
}
if(localProperties.min){
this.flags.min = parseInt(localProperties.min);
}
if(localProperties.max){
this.flags.max = parseInt(localProperties.max);
}
},

// Over-ride for integer validation
isValid: function(){
return dojo.validate.isInteger(this.textbox.value, this.flags);
},
isInRange: function(){
return dojo.validate.isInRange(this.textbox.value, this.flags);
}
}
);
