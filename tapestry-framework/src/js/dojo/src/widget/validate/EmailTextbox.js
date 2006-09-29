/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.validate.EmailTextbox");

dojo.require("dojo.widget.validate.UrlTextbox");

/*
  ****** EmailTextbox ******

  A subclass of UrlTextbox.
  Over-rides isValid to test for email addresses.
  Can use all markup attributes/properties of UrlTextbox except scheme.
  One new attribute available in the markup.

  @attr allowCruft  Allow address like <mailto:foo@yahoo.com>.  Default is false.
*/
dojo.widget.defineWidget(
	"dojo.widget.validate.EmailTextbox",
	dojo.widget.validate.UrlTextbox,
	{
		mixInProperties: function(localProperties, frag) {
			// First initialize properties in super-class.
			dojo.widget.validate.EmailTextbox.superclass.mixInProperties.apply(this, arguments);
	
			// Get properties from markup attributes, and assign to flags object.
			if ( localProperties.allowcruft ) { 
				this.flags.allowCruft = ( localProperties.allowcruft == "true" );
			}
		},

		// Over-ride for email address validation
		isValid: function() { 
			return dojo.validate.isEmailAddress(this.textbox.value, this.flags);
		}
	}
);
