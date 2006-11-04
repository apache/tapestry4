
dojo.provide("dojo.data.Read");dojo.require("dojo.lang.declare");dojo.require("dojo.data.Result");dojo.require("dojo.experimental");dojo.experimental("dojo.data.Read");dojo.declare("dojo.data.Read", null, {get:
function( item,  attribute,  defaultValue) {dojo.unimplemented('dojo.data.Read.get');var attributeValue = null;return attributeValue;},getValues:
function( item,  attribute) {dojo.unimplemented('dojo.data.Read.getValues');var array = null;return array;},getAttributes:
function( item) {dojo.unimplemented('dojo.data.Read.getAttributes');var array = null;return array;},hasAttribute:
function( item,  attribute) {dojo.unimplemented('dojo.data.Read.hasAttribute');return false;},hasAttributeValue:
function( item,  attribute,  value) {dojo.unimplemented('dojo.data.Read.hasAttributeValue');return false;},isItem:
function( something) {dojo.unimplemented('dojo.data.Read.isItem');return false;},find:
function( query,  optionalKeywordArgs ) {dojo.unimplemented('dojo.data.Read.find');var result = null;return result;},getIdentity:
function( item) {dojo.unimplemented('dojo.data.Read.getIdentity');var itemIdentifyString = null;return itemIdentifyString;},getByIdentity:
function( id) {dojo.unimplemented('dojo.data.Read.getByIdentity');var item = null;return item;}});