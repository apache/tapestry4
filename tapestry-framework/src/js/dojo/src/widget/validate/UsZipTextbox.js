/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.validate.UsZipTextbox");

dojo.require("dojo.widget.validate.ValidationTextbox");
dojo.require("dojo.validate.us");

/*
  ****** UsZipTextbox ******

  A subclass of ValidationTextbox.
  Over-rides isValid to test if input is a US zip code.
  Validates zip-5 and zip-5 plus 4.
*/
dojo.widget.defineWidget(
	"dojo.widget.validate.UsZipTextbox",
	dojo.widget.validate.ValidationTextbox,
	{
		isValid: function() { 
			return dojo.validate.us.isZipCode(this.textbox.value);
		}
	}
);
