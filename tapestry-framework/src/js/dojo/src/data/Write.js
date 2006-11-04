
dojo.provide("dojo.data.Write");dojo.require("dojo.lang.declare");dojo.require("dojo.data.Read");dojo.require("dojo.experimental");dojo.experimental("dojo.data.Write");dojo.declare("dojo.data.Write", dojo.data.Read, {newItem:
function( keywordArgs) {var newItem;dojo.unimplemented('dojo.data.Write.newItem');return newItem;},deleteItem:
function( item) {dojo.unimplemented('dojo.data.Write.deleteItem');return false;},set:
function( item,  attribute,  value) {dojo.unimplemented('dojo.data.Write.set');return false;},setValues:
function( item,  attribute,  values) {dojo.unimplemented('dojo.data.Write.setValues');return false;},clear:
function( item,  attribute) {dojo.unimplemented('dojo.data.Write.clear');return false;},save:
function() {dojo.unimplemented('dojo.data.Write.save');return false;},revert:
function() {dojo.unimplemented('dojo.data.Write.revert');return false;},isDirty:
function( item) {dojo.unimplemented('dojo.data.Write.isDirty');return false;}});