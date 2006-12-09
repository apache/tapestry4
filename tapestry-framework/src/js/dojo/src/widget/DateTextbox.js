
dojo.provide("dojo.widget.DateTextbox");dojo.require("dojo.widget.ValidationTextbox");dojo.require("dojo.date.format");dojo.require("dojo.validate.datetime");dojo.widget.defineWidget(
"dojo.widget.DateTextbox",dojo.widget.ValidationTextbox,{displayFormat: "",formatLength: "short",isValid: function(){return dojo.date.parse(this.textbox.value, {formatLength:this.formatLength, selector:'dateOnly', locale:this.lang, datePattern: this.displayFormat});}}
);dojo.widget.defineWidget(
"dojo.widget.TimeTextbox",dojo.widget.ValidationTextbox,{displayFormat: "",formatLength: "short",isValid: function(){return dojo.date.parse(this.textbox.value, {formatLength:this.formatLength, selector:'timeOnly', locale:this.lang, timePattern: this.displayFormat});}}
);