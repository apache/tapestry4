
dojo.provide("dojo.widget.IntegerTextbox");dojo.require("dojo.widget.ValidationTextbox");dojo.require("dojo.validate.common");dojo.widget.defineWidget(
"dojo.widget.IntegerTextbox",dojo.widget.ValidationTextbox,{mixInProperties: function(localProperties, frag){dojo.widget.IntegerTextbox.superclass.mixInProperties.apply(this, arguments);if((localProperties.signed == "true")||
(localProperties.signed == "always")){this.flags.signed = true;}else if((localProperties.signed == "false")||
(localProperties.signed == "never")){this.flags.signed = false;this.flags.min = 0;}else{this.flags.signed = [ true, false ];}
if(localProperties.separator){this.flags.separator = localProperties.separator;}
if(localProperties.min){this.flags.min = parseInt(localProperties.min);}
if(localProperties.max){this.flags.max = parseInt(localProperties.max);}},isValid: function(){return dojo.validate.isInteger(this.textbox.value, this.flags);},isInRange: function(){return dojo.validate.isInRange(this.textbox.value, this.flags);}}
);