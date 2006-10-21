

dojo.provide("dojo.data.Read");
dojo.require("dojo.lang.declare");
dojo.require("dojo.data.Result");
dojo.require("dojo.experimental");


dojo.experimental("dojo.data.Read");

dojo.declare("dojo.data.Read", null, {
get:
function( item,  attribute,  defaultValue) {



dojo.unimplemented('dojo.data.Read.get');
var attributeValue = null;
return attributeValue; // a literal, an item, null, or undefined (never an array)
},
getValues:
function( item,  attribute) {



dojo.unimplemented('dojo.data.Read.getValues');
var array = null;
return array; // an array that may contain literals and items
},
getAttributes:
function( item) {



dojo.unimplemented('dojo.data.Read.getAttributes');
var array = null;
return array; // array
},
hasAttribute:
function( item,  attribute) {



dojo.unimplemented('dojo.data.Read.hasAttribute');
return false; // boolean
},
hasAttributeValue:
function( item,  attribute,  value) {



dojo.unimplemented('dojo.data.Read.hasAttributeValue');
return false; // boolean
},
isItem:
function( something) {



dojo.unimplemented('dojo.data.Read.isItem');
return false; // boolean
},
find:
function( query,  optionalKeywordArgs ) {



dojo.unimplemented('dojo.data.Read.find');
var result = null; // new dojo.data.Result().
return result; // an object that implements dojo.data.Result
},
getIdentity:
function( item) {



dojo.unimplemented('dojo.data.Read.getIdentity');
var itemIdentifyString = null;
return itemIdentifyString; // string
},
getByIdentity:
function( id) {



dojo.unimplemented('dojo.data.Read.getByIdentity');
var item = null;
return item; // item
}
});
