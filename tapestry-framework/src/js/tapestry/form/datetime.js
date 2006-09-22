dojo.provide("tapestry.form.datetime");

dojo.require("dojo.date.format");
dojo.require("dojo.validate.datetime");
dojo.require("dojo.logging.Logger");

tapestry.form.datetime={
	
	/**
	 * Checks if the specified value is a valid date, according to
	 * the flags passed in.
	 * 
	 * @param value The string value of the date being validated.
	 * @param flags An object.
	 * 		flags.format 	A string format pattern that will be used to validate
	 * 						the incoming value via @link dojo.validate.isValidDate(value, format).
	 * 		flags.max		A string date value representing the maximum date that can be selected.
	 * 		flags.min		A string date value representing the minimum date that can be selected.
	 * @return Boolean. True if valid, false otherwise.
	 */
	isValidDate:function(value, flags){
		if (!value || !flags){
			dojo.raise("isValidDate: value and flags must be specified");
			return;
		}
		
		// parse date value
		var dateValue=null;
		try {
			dateValue = dojo.date.parse(value, flags);
		} catch (e) {
			dojo.log.exception("Error parsing input date.", e, true);
			return false;
		}
		
		if(dateValue == null) { return false; }
		
		// convert to format that is validatable
		value=dojo.date.format(dateValue, flags);
		
		// TODO: This is totally useless right now, doesn't even accept formats with string equivs
		// See a better method http://www.mattkruse.com/javascript/date/source.html 
		// basic format validation
		// if (!dojo.validate.isValidDate(value, flags.format)) 
		//	return false;
		
		// max date
		if (!dj_undef("max", flags)){
			if (typeof flags.max == "string"){
				flags.max=dojo.date.parse(flags.max, flags);
			}
			if (dojo.date.compare(dateValue, flags.max, dojo.date.compareTypes.DATE) > 0)
				return false;
		}
		
		// min date
		if (!dj_undef("min", flags)){
			if (typeof flags.min == "string"){
				flags.min=dojo.date.parse(flags.min, flags);
			}
			if (dojo.date.compare(dateValue, flags.min, dojo.date.compareTypes.DATE) < 0)
				return false;
		}
		
		return true;
	}
		
}
