

dojo.provide("dojo.data.Write");
dojo.require("dojo.lang.declare");
dojo.require("dojo.data.Read");
dojo.require("dojo.experimental");


dojo.experimental("dojo.data.Write");

dojo.declare("dojo.data.Write", dojo.data.Read, {
newItem:
function( keywordArgs) {



var newItem;
dojo.unimplemented('dojo.data.Write.newItem');
return newItem; // item
},
deleteItem:
function( item) {



dojo.unimplemented('dojo.data.Write.deleteItem');
return false; // boolean
},
set:
function( item,  attribute,  value) {



dojo.unimplemented('dojo.data.Write.set');
return false; // boolean
},
setValues:
function( item,  attribute,  values) {



dojo.unimplemented('dojo.data.Write.setValues');
return false; // boolean
},
clear:
function( item,  attribute) {



dojo.unimplemented('dojo.data.Write.clear');
return false; // boolean
},
save:
function() {



dojo.unimplemented('dojo.data.Write.save');
return false; // boolean
},
revert:
function() {



dojo.unimplemented('dojo.data.Write.revert');
return false; // boolean
},
isDirty:
function( item) {



dojo.unimplemented('dojo.data.Write.isDirty');
return false; // boolean
}
});
