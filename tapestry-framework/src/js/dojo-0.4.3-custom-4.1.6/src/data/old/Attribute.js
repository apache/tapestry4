dojo.provide("dojo.data.old.Attribute");
dojo.require("dojo.data.old.Item");
dojo.require("dojo.lang.assert");
dojo.data.old.Attribute=function(_1,_2){
dojo.lang.assertType(_1,dojo.data.old.provider.Base,{optional:true});
dojo.lang.assertType(_2,String);
dojo.data.old.Item.call(this,_1);
this._attributeId=_2;
};
dojo.inherits(dojo.data.old.Attribute,dojo.data.old.Item);
dojo.data.old.Attribute.prototype.toString=function(){
return this._attributeId;
};
dojo.data.old.Attribute.prototype.getAttributeId=function(){
return this._attributeId;
};
dojo.data.old.Attribute.prototype.getType=function(){
return this.get("type");
};
dojo.data.old.Attribute.prototype.setType=function(_3){
this.set("type",_3);
};
