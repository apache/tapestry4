

dojo.provide("dojo.data.Result");
dojo.require("dojo.lang.declare");
dojo.require("dojo.experimental");


dojo.experimental("dojo.data.Result");

dojo.declare("dojo.data.Result", null, {
forEach:
function( callbackFunction,  callbackObject,  optionalKeywordArgs) {



dojo.unimplemented('dojo.data.Result.forEach');
return false; // boolean
},
getLength:
function() {

dojo.unimplemented('dojo.data.Result.getLength');
return -1; // integer
},
inProgress:
function() {

dojo.unimplemented('dojo.data.Result.inProgress');
return false; // boolean
},
cancel:
function() {

dojo.unimplemented('dojo.data.Result.cancel');
},
setOnFindCompleted:
function( callbackFunction,  callbackObject) {

dojo.unimplemented('dojo.data.Result.setOnFindCompleted');
},
setOnError:
function( errorCallbackFunction,  callbackObject) {

dojo.unimplemented('dojo.data.Result.setOnError');
},
getStore:
function() {

dojo.unimplemented('dojo.data.Result.getStore');
return null; // an object that implements dojo.data.Read
}
});
