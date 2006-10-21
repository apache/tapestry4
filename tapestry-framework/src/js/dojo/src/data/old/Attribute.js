

dojo.provide("dojo.data.old.Attribute");
dojo.require("dojo.data.old.Item");
dojo.require("dojo.lang.assert");




dojo.data.old.Attribute = function( dataProvider,  attributeId) {

dojo.lang.assertType(dataProvider, dojo.data.old.provider.Base, {optional: true});
dojo.lang.assertType(attributeId, String);
dojo.data.old.Item.call(this, dataProvider);
this._attributeId = attributeId;
};
dojo.inherits(dojo.data.old.Attribute, dojo.data.old.Item);




dojo.data.old.Attribute.prototype.toString = function() {
return this._attributeId; // string
};

dojo.data.old.Attribute.prototype.getAttributeId = function() {

return this._attributeId; // string
};

dojo.data.old.Attribute.prototype.getType = function() {

return this.get('type'); // dojo.data.old.Type or null
};

dojo.data.old.Attribute.prototype.setType = function( type) {

this.set('type', type);
};
