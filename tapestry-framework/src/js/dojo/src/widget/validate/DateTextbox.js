/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.validate.DateTextbox");

dojo.require("dojo.widget.validate.ValidationTextbox");
dojo.require("dojo.validate.datetime");

/*
  ****** DateTextbox ******

  A subclass of ValidationTextbox.
  Over-rides isValid to test if input is in a valid date format.

  @attr format  Described in dojo.validate.js.  Default is  "MM/DD/YYYY".
*/
dojo.widget.defineWidget(
	"dojo.widget.validate.DateTextbox",
	dojo.widget.validate.ValidationTextbox,
	{
		mixInProperties: function(localProperties, frag) {
			// First initialize properties in super-class.
			dojo.widget.validate.DateTextbox.superclass.mixInProperties.apply(this, arguments);
	
			// Get properties from markup attributes, and assign to flags object.
			if ( localProperties.format ) { 
				this.flags.format = localProperties.format;
			}
		},

		// Over-ride for date validation
		isValid: function() { 
			return dojo.validate.isValidDate(this.textbox.value, this.flags.format);
		}
	}
);
