

dojo.provide("dojo.data.old.Type");
dojo.require("dojo.data.old.Item");




dojo.data.old.Type = function( dataProvider) {

dojo.data.old.Item.call(this, dataProvider);
};
dojo.inherits(dojo.data.old.Type, dojo.data.old.Item);
