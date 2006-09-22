/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.data.Result");
dojo.require("dojo.lang.declare");
dojo.require("dojo.experimental");

/* summary:
 *   This is an abstract API used by data provider implementations.  
 *   This file defines methods signatures and intentionally leaves all the
 *   methods unimplemented.
 */
dojo.experimental("dojo.data.Result");

dojo.declare("dojo.data.Result", null, {
	forEach:
		function(/* function or object */ functionOrKeywordObject) {
		/* summary:
		 *   Loops through the result list, calling a callback function
		 *   for each item in the result list.
		 * description:
		 *   The forEach() method will pass two arguments to the callback
		 *   function: an item, and the result list object itself.
		 *   Returns true if the entire result list has been looped through.
		 *   After the forEach() operation has finished (or been cancelled)
		 *   result.forEach() can be called again on the same result object.
		 * functionOrKeywordObject:
		 *   The forEach() method always takes exactly one argument,
		 *   which is either a simple callback function or an object
		 *   with keyword parameters.  Conforming implementations must
		 *   accept 'object' and 'callback' as keyword parameters.  If
		 *   the caller provides 'object', then the value of 'callback'
		 *   must be a string with the name of a method available on that
		 *   object.  If the caller does not provide 'object', then the
		 *   'callback' value must be a function.  Different implementations
		 *   may support additional keywords, but implementations are not
		 *   required to support anything else.
		 * examples:
		 *   var results = store.find("recent books");            // synchronous
		 *   var results = store.find("all books", {async: true}); // asynchronous
		 *   someCallbackFunction = function(item, resultObject) {};
		 *   results.forEach(someCallbackFunction);
		 *   results.forEach({object:someHandlerObject, callback:"someCallbackMethod"});
		 * issues:
		 *   We haven't yet decided what other parameters we might allow to
		 *   support fancy features.  Here are some ideas:
		 *     results.forEach({callback:callbackFunction, onCompletion: finishedFunction});
		 *     results.forEach({callback:callbackFunction, first: 201, last: 300}); // partial loop
		 *     results.forEach({callback:callbackFunction, first: 200, numItems: 50}); // partial loop from 200 to 250
		 *   CCM: How to specify datastore-specific options to allow caching n
		 *   items before/after current window of items being viewed?
		 */
			dojo.unimplemented('dojo.data.Result.forEach');
			return false; // boolean
		},
	getLength:
		function() {
		/* summary:
		 *   Returns an integer -- the number of items in the result list.
		 *   Returns -1 if the length is not known when the method is called.
		 */
			dojo.unimplemented('dojo.data.Result.getLength');
			return -1; // integer
		},
	inProgress:
		function() {
		/* summary:
		 *   Returns true if a forEach() loop is in progress.
		 */
			dojo.unimplemented('dojo.data.Result.inProgress');
			return false; // boolean
		},
	cancel:
		function() {
		/* summary:
		 *   If a forEach() loop is in progress, calling cancel() stops
		 *   the loop.
		 */
			dojo.unimplemented('dojo.data.Result.cancel');
		},
	addCallback:
		function(/* function */ callbackFunction) {
		/* summary:
		 *   Allows you to register a callbackFunction that will
		 *   be called when all the results are available.
		 *   Patterned after dojo.Deferred.addCallback()
		 * issues:
		 *   I (Brian) am not clear on exactly how this shoul work:
		 *   what parameters it takes, what the return value should
		 *   be, whether the callback gets called before or after the
		 *   forEach() loop, whether you can chain callbacks (as with
		 *   a real Deferred.  I'm worried that doing everything that
		 *   dojo.Deferred does is too much to ask of basic data-store
		 *   implementations like a simple CSV store.  Maybe someone
		 *   else can suggest simple answers to how addCallback should
		 *   work.
		 */
			dojo.unimplemented('dojo.data.Result.addCallback');
		},
	addErrback:
		function(/* function */ errorCallbackFunction) {
		/* summary:
		 *   Allows you to register a errorCallbackFunction that
		 *   will be called if there is any sort of error.
		 * issues:
		 *   See the notes under addCallback(), above.
		 */
			dojo.unimplemented('dojo.data.Result.addErrback');
		},
	getStore:
		function() {
		/* summary:
		 *   Returns the datastore object that created this result list
		 */
			dojo.unimplemented('dojo.data.Result.getStore');
			return null; // an object that implements dojo.data.Read
		}
});
