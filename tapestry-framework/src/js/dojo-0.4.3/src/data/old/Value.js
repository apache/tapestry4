dojo.provide("dojo.data.old.Value");
dojo.require("dojo.lang.assert");
dojo.data.old.Value=function(_1){
this._value=_1;
this._type=null;
};
dojo.data.old.Value.prototype.toString=function(){
return this._value.toString();
};
dojo.data.old.Value.prototype.getValue=function(){
return this._value;
};
dojo.data.old.Value.prototype.getType=function(){
dojo.unimplemented("dojo.data.old.Value.prototype.getType");
return this._type;
};
dojo.data.old.Value.prototype.compare=function(){
dojo.unimplemented("dojo.data.old.Value.prototype.compare");
};
dojo.data.old.Value.prototype.isEqual=function(){
dojo.unimplemented("dojo.data.old.Value.prototype.isEqual");
};
