/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.data.csv.Result");
dojo.require("dojo.data.Result");
dojo.require("dojo.lang.assert");

dojo.declare("dojo.data.csv.Result", dojo.data.Result, {
	/* Summary:
	 *   dojo.data.csv.Result implements the dojo.data.Result API.  
	 */
	initializer: 
		function(/* array */ arrayOfItems, /* object */ dataStore) {
			dojo.lang.assertType(arrayOfItems, Array);
			this._arrayOfItems = arrayOfItems;
			this._dataStore = dataStore;
			this._cancel = false;
			this._inProgress = false;
		},
	forEach:
		function(/* function or object */ functionOrKeywordObject) {
			// Summary: See dojo.data.Result.forEach()
			dojo.lang.assertType(functionOrKeywordObject, [Function, "pureobject"]); 
			var callbackFunction = null;
			var callbackObject = null;
			var callbackFunctionName = null;
			if (dojo.lang.isFunction(functionOrKeywordObject)) {
				callbackFunction = functionOrKeywordObject;
			} else {
				var keywordObject = functionOrKeywordObject;
				dojo.lang.assertType(keywordObject, "pureobject");
				var callbackObject = keywordObject["object"];
				var callbackFunctionName = keywordObject["callback"];
			}
			this._inProgress = true;
			for (var i in this._arrayOfItems) {
				var item = this._arrayOfItems[i];
				if (!this._cancel) {
					if (callbackFunction) {
						callbackFunction(item, this);
					} else {
						callbackObject[callbackFunctionName](item, this);
					}
				}
			}
			this._inProgress = false;
			this._cancel = false;
			
			return true; // boolean
		},
	getLength:
		function() {
			// Summary: See dojo.data.Result.getLength()
			return this._arrayOfItems.length; // integer
		},
	inProgress:
		function() {
			// Summary: See dojo.data.Result.inProgress()
			return this._inProgress; // boolean
		},
	cancel:
		function() {
			// Summary: See dojo.data.Result.cancel()
			if (this._inProgress) {
				this._cancel = true;
			}
		},
	addCallback:
		function(/* function */ callbackFunction) {
			// Summary: See dojo.data.Result.addCallback()
			dojo.unimplemented('dojo.data.csv.Result.addCallback');
		},
	addErrback:
		function(/* function */ errorCallbackFunction) {
			// Summary: See dojo.data.Result.addErrback()
			dojo.unimplemented('dojo.data.csv.Result.addErrback');
		},
	getStore:
		function() {
			// Summary: See dojo.data.Result.getStore()
			return this._dataStore; // an object that implements dojo.data.Read
		}
});

