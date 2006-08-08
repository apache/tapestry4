dojo.provide("tapestry.form.datetime");

dojo.require("dojo.validate.datetime");
dojo.require("dojo.date");

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
		// default generic validation if no flags specified
		if (!flags || typeof flags.format != "string") 
			return dojo.validate.isValidDate(value);
		
		// parse date value
		var dateValue=null;
		try {
			dateValue = new Date(value);
		} catch (e) {
			dojo.log.exception("Error parsing input date.", e, true);
			return false;
		}
		
		// convert to format that is validatable
		value = dojo.date.format(dateValue, flags.format);
		
		// TODO: This is totally useless right now, doesn't even accept formats with string equivs
		// See a better method http://www.mattkruse.com/javascript/date/source.html 
		// basic format validation
		// if (!dojo.validate.isValidDate(value, flags.format)) 
			//return false;
		
		// max date
		if (typeof flags.max == "string") {
			var max = new Date(flags.max);
			if (dojo.date.compare(dateValue, max, dojo.date.compareTypes.DATE) > 0)
				return false;
		}
		
		// min date
		if (typeof flags.min == "string") {
			var min = new Date(flags.min);
			if (dojo.date.compare(dateValue, min, dojo.date.compareTypes.DATE) < 0)
				return false;
		}
		
		return true;
	}
		
}
