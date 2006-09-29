/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.validate.EmailListTextbox");

dojo.require("dojo.widget.validate.EmailTextbox");

/*
  ****** EmailListTextbox ******

  A subclass of EmailTextbox.
  Over-rides isValid to test for a list of email addresses.
  Can use all markup attributes/properties of EmailTextbox and ...

  @attr listSeparator  The character used to separate email addresses.  
    Default is ";", ",", "\n" or " ".
*/
dojo.widget.defineWidget(
	"dojo.widget.validate.EmailListTextbox",
	dojo.widget.validate.EmailTextbox,
	{
		mixInProperties: function(localProperties, frag) {
			// First initialize properties in super-class.
			dojo.widget.validate.EmailListTextbox.superclass.mixInProperties.apply(this, arguments);
	
			// Get properties from markup attributes, and assign to flags object.
			if ( localProperties.listseparator ) { 
				this.flags.listSeparator = localProperties.listseparator;
			}
		},

		// Over-ride for email address list validation
		isValid: function() { 
			return dojo.validate.isEmailAddressList(this.textbox.value, this.flags);
		}
	}
);
