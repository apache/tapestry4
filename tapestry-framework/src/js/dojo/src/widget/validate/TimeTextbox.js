/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.validate.TimeTextbox");

dojo.require("dojo.widget.validate.ValidationTextbox");
dojo.require("dojo.validate.datetime");

/*
  ****** TimeTextbox ******

  A subclass of ValidationTextbox.
  Over-rides isValid to test if input is in a valid time format.

  @attr format    Described in dojo.validate.js.  Default is  "h:mm:ss t".
  @attr amSymbol  The symbol used for AM.  Default is "AM" or "am".
  @attr pmSymbol  The symbol used for PM.  Default is "PM" or "pm".
*/
dojo.widget.defineWidget(
	"dojo.widget.validate.TimeTextbox",
	dojo.widget.validate.ValidationTextbox,
	{
		mixInProperties: function(localProperties, frag) {
			// First initialize properties in super-class.
			dojo.widget.validate.TimeTextbox.superclass.mixInProperties.apply(this, arguments);
	
			// Get properties from markup attributes, and assign to flags object.
			if ( localProperties.format ) { 
				this.flags.format = localProperties.format;
			}
			if ( localProperties.amsymbol ) { 
				this.flags.amSymbol = localProperties.amsymbol;
			}
			if ( localProperties.pmsymbol ) { 
				this.flags.pmSymbol = localProperties.pmsymbol;
			}
		},

		// Over-ride for time validation
		isValid: function() { 
			return dojo.validate.isValidTime(this.textbox.value, this.flags);
		}
	}
);
