

dojo.provide("dojo.widget.RegexpTextbox");

dojo.require("dojo.widget.ValidationTextbox");


dojo.widget.defineWidget(
"dojo.widget.RegexpTextbox",
dojo.widget.ValidationTextbox,
{
mixInProperties: function(localProperties, frag){
// First initialize properties in super-class.
dojo.widget.RegexpTextbox.superclass.mixInProperties.apply(this, arguments);

// Get properties from markup attibutes, and assign to flags object.
if(localProperties.regexp){
this.flags.regexp = localProperties.regexp;
}
if(localProperties.flags){
this.flags.flags = localProperties.flags;
}
},

// Over-ride for integer validation
isValid: function(){
var regexp = new RegExp(this.flags.regexp, this.flags.flags);
return regexp.test(this.textbox.value);
}
}
);
