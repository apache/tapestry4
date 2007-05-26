dojo.provide("dojo.widget.UsTextbox");
dojo.require("dojo.widget.ValidationTextbox");
dojo.require("dojo.validate.us");
dojo.widget.defineWidget("dojo.widget.UsStateTextbox",dojo.widget.ValidationTextbox,{mixInProperties:function(_1){
dojo.widget.UsStateTextbox.superclass.mixInProperties.apply(this,arguments);
if(_1.allowterritories){
this.flags.allowTerritories=(_1.allowterritories=="true");
}
if(_1.allowmilitary){
this.flags.allowMilitary=(_1.allowmilitary=="true");
}
},isValid:function(){
return dojo.validate.us.isState(this.textbox.value,this.flags);
}});
dojo.widget.defineWidget("dojo.widget.UsZipTextbox",dojo.widget.ValidationTextbox,{isValid:function(){
return dojo.validate.us.isZipCode(this.textbox.value);
}});
dojo.widget.defineWidget("dojo.widget.UsSocialSecurityNumberTextbox",dojo.widget.ValidationTextbox,{isValid:function(){
return dojo.validate.us.isSocialSecurityNumber(this.textbox.value);
}});
dojo.widget.defineWidget("dojo.widget.UsPhoneNumberTextbox",dojo.widget.ValidationTextbox,{isValid:function(){
return dojo.validate.us.isPhoneNumber(this.textbox.value);
}});
