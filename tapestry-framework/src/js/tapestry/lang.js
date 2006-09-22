dojo.provide("tapestry.lang");

dojo.require("dojo.lang.common");

tapestry.lang = {

	/**
	 * Searches the specified list for an object with a matching propertyName/value pair. 
	 * @param list 			The array of objects to search.
	 * @param properyName	The object property key to match on. (ie object[propertyName]) 
	 * 			Can also be a template object to match in the form of {key:{key:value}} nested
	 * 			as deeply as you like. 
	 * @param value 		The value to be matched against
	 * @return The matching array object found, or null.
	 */
	find:function(list, property, value){
		if (!list || !property || list.length < 1) return null;
		
		// if not propMatch then template object was passed in
		var propMatch=dojo.lang.isString(property);
		if (propMatch && !value) return null; //if doing string/other non template match and no value
		
		for (var i=0; i < list.length; i++) {
			if (!list[i]) continue;
			if (propMatch) {
				if (list[i] && list[i][property] && list[i][property] == value) return list[i];
			} else {
				if (this.matchProperty(property, list[i])) return list[i];
			}
		}
		return null;
	},
	
	// called recursively to match object properties
	// partially stolen logic from dojo.widget.html.SortableTable.sort
	matchProperty:function(template, object){
		if(!dojo.lang.isObject(template) || !dojo.lang.isObject(object))
			return template.valueOf() == object.valueOf();
		
		for(var p in template){
			if(!(p in object)) return false;	//	boolean
			if (!this.matchProperty(template[p], object[p])) return false;
		}
		return true;
	}
}
