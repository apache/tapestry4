

dojo.provide("dojo.AdapterRegistry");
dojo.require("dojo.lang.func");

dojo.AdapterRegistry = function(returnWrappers){







this.pairs = [];
this.returnWrappers = returnWrappers || false;
}

dojo.lang.extend(dojo.AdapterRegistry, {
register: function(	 name,  check,  wrap,
directReturn,
override){
// summary:
//		register a check function to determine if the wrap function or
//		object gets selected
// name: a way to identify this matcher.
// check:
//		a function that arguments are passed to from the adapter's
//		match() function.  The check function should return true if the
//		given arguments are appropriate for the wrap function.
// directReturn:
//		If directReturn is true, the value passed in for wrap will be
//		returned instead of being called. Alternately, the
//		AdapterRegistry can be set globally to "return not call" using
//		the returnWrappers property. Either way, this behavior allows
//		the registry to act as a "search" function instead of a
//		function interception library.
// override:
//		If override is given and true, the check function will be given
//		highest priority. Otherwise, it will be the lowest priority
//		adapter.

var type = (override) ? "unshift" : "push";
this.pairs[type]([name, check, wrap, directReturn]);
},

match: function(){
// summary:
//		Find an adapter for the given arguments. If no suitable adapter
//		is found, throws an exception. match() accepts any number of
//		arguments, all of which are passed to all matching functions
//		from the registered pairs.
for(var i = 0; i < this.pairs.length; i++){
var pair = this.pairs[i];
if(pair[1].apply(this, arguments)){
if((pair[3])||(this.returnWrappers)){
return pair[2];
}else{
return pair[2].apply(this, arguments);
}
}
}
throw new Error("No match found");
// dojo.raise("No match found");
},

unregister: function(name){
// summary: Remove a named adapter from the registry

// FIXME: this is kind of a dumb way to handle this. On a large
// registry this will be slow-ish and we can use the name as a lookup
// should we choose to trade memory for speed.
for(var i = 0; i < this.pairs.length; i++){
var pair = this.pairs[i];
if(pair[0] == name){
this.pairs.splice(i, 1);
return true;
}
}
return false;
}
});
