/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.validate.UrlTextbox");

dojo.require("dojo.widget.validate.IpAddressTextbox");

/*
  ****** UrlTextbox ******

  A subclass of IpAddressTextbox.
  Over-rides isValid to test for URL's.
  Can specify 5 additional attributes in the markup.

  @attr scheme        Can be true or false.  If omitted the scheme is optional.
  @attr allowIP       Allow an IP address for hostname.  Default is true.
  @attr allowLocal    Allow the host to be "localhost".  Default is false.
  @attr allowCC       Allow 2 letter country code domains.  Default is true.
  @attr allowGeneric  Allow generic domains.  Can be true or false, default is true.
*/
dojo.widget.defineWidget(
	"dojo.widget.validate.UrlTextbox",
	dojo.widget.validate.IpAddressTextbox,
	{
		mixInProperties: function(localProperties, frag) {
			// First initialize properties in super-class.
			dojo.widget.validate.UrlTextbox.superclass.mixInProperties.apply(this, arguments);

			// Get properties from markup attributes, and assign to flags object.
			if ( localProperties.scheme ) { 
				this.flags.scheme = ( localProperties.scheme == "true" );
			}
			if ( localProperties.allowip ) { 
				this.flags.allowIP = ( localProperties.allowip == "true" );
			}
			if ( localProperties.allowlocal ) { 
				this.flags.allowLocal = ( localProperties.allowlocal == "true" );
			}
			if ( localProperties.allowcc ) { 
				this.flags.allowCC = ( localProperties.allowcc == "true" );
			}
			if ( localProperties.allowgeneric ) { 
				this.flags.allowGeneric = ( localProperties.allowgeneric == "true" );
			}
		},

		// Over-ride for URL validation
		isValid: function() { 
			return dojo.validate.isUrl(this.textbox.value, this.flags);
		}
	}
);
