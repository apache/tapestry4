dojo.provide("dojo.widget.RegexpTextbox");
dojo.require("dojo.widget.ValidationTextbox");
dojo.widget.defineWidget("dojo.widget.RegexpTextbox",dojo.widget.ValidationTextbox,{mixInProperties:function(_1,_2){
dojo.widget.RegexpTextbox.superclass.mixInProperties.apply(this,arguments);
if(_1.regexp){
this.flags.regexp=_1.regexp;
}
if(_1.flags){
this.flags.flags=_1.flags;
}
},isValid:function(){
var _3=new RegExp(this.flags.regexp,this.flags.flags);
return _3.test(this.textbox.value);
}});
