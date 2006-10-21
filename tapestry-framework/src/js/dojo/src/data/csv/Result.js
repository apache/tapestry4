

dojo.provide("dojo.data.csv.Result");
dojo.require("dojo.data.Result");
dojo.require("dojo.lang.assert");

dojo.declare("dojo.data.csv.Result", dojo.data.Result, {

initializer:
function( arrayOfItems,  dataStore) {
dojo.lang.assertType(arrayOfItems, Array);
this._arrayOfItems = arrayOfItems;
this._dataStore = dataStore;
this._cancel = false;
this._inProgress = false;
},
forEach:
function( callbackFunction,  callbackObject,  optionalKeywordArgs) {
// summary: See dojo.data.Result.forEach()
dojo.lang.assertType(callbackFunction, Function);
dojo.lang.assertType(callbackObject, Object, {optional:true});
dojo.lang.assertType(optionalKeywordArgs, "pureobject", {optional:true});
this._inProgress = true;
for (var i in this._arrayOfItems) {
var item = this._arrayOfItems[i];
if (!this._cancel) {
callbackFunction.call(callbackObject, item, i, this);
}
}
this._inProgress = false;
this._cancel = false;

return true; // boolean
},
getLength:
function() {
// summary: See dojo.data.Result.getLength()
return this._arrayOfItems.length; // integer
},
inProgress:
function() {
// summary: See dojo.data.Result.inProgress()
return this._inProgress; // boolean
},
cancel:
function() {
// summary: See dojo.data.Result.cancel()
if (this._inProgress) {
this._cancel = true;
}
},
setOnFindCompleted:
function( callbackFunction,  callbackObject) {
// summary: See dojo.data.Result.setOnFindCompleted()
dojo.unimplemented('dojo.data.csv.Result.setOnFindCompleted');
},
setOnError:
function( errorCallbackFunction,  callbackObject) {
// summary: See dojo.data.Result.setOnError()
dojo.unimplemented('dojo.data.csv.Result.setOnError');
},
getStore:
function() {
// summary: See dojo.data.Result.getStore()
return this._dataStore; // an object that implements dojo.data.Read
}
});

