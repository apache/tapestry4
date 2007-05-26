dojo.provide("dojo.widget.DateTextbox");
dojo.require("dojo.widget.ValidationTextbox");
dojo.require("dojo.date.format");
dojo.require("dojo.validate.datetime");
dojo.widget.defineWidget("dojo.widget.DateTextbox",dojo.widget.ValidationTextbox,{displayFormat:"",formatLength:"short",mixInProperties:function(_1){
dojo.widget.DateTextbox.superclass.mixInProperties.apply(this,arguments);
if(_1.format){
this.flags.format=_1.format;
}
},isValid:function(){
if(this.flags.format){
dojo.deprecated("dojo.widget.DateTextbox","format attribute is deprecated; use displayFormat or formatLength instead","0.5");
return dojo.validate.isValidDate(this.textbox.value,this.flags.format);
}
return dojo.date.parse(this.textbox.value,{formatLength:this.formatLength,selector:"dateOnly",locale:this.lang,datePattern:this.displayFormat});
}});
dojo.widget.defineWidget("dojo.widget.TimeTextbox",dojo.widget.ValidationTextbox,{displayFormat:"",formatLength:"short",mixInProperties:function(_2){
dojo.widget.TimeTextbox.superclass.mixInProperties.apply(this,arguments);
if(_2.format){
this.flags.format=_2.format;
}
if(_2.amsymbol){
this.flags.amSymbol=_2.amsymbol;
}
if(_2.pmsymbol){
this.flags.pmSymbol=_2.pmsymbol;
}
},isValid:function(){
if(this.flags.format){
dojo.deprecated("dojo.widget.TimeTextbox","format attribute is deprecated; use displayFormat or formatLength instead","0.5");
return dojo.validate.isValidTime(this.textbox.value,this.flags);
}
return dojo.date.parse(this.textbox.value,{formatLength:this.formatLength,selector:"timeOnly",locale:this.lang,timePattern:this.displayFormat});
}});
