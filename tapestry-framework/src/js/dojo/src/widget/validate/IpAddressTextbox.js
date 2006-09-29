/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.validate.IpAddressTextbox");

dojo.require("dojo.widget.validate.ValidationTextbox");
dojo.require("dojo.validate.web");

/*
  ****** IpAddressTextbox ******

  A subclass of ValidationTextbox.
  Over-rides isValid to test for IP addresses.
  Can specify formats for ipv4 or ipv6 as attributes in the markup.

  @attr allowDottedDecimal  true or false, default is true.
  @attr allowDottedHex      true or false, default is true.
  @attr allowDottedOctal    true or false, default is true.
  @attr allowDecimal        true or false, default is true.
  @attr allowHex            true or false, default is true.
  @attr allowIPv6           true or false, default is true.
  @attr allowHybrid         true or false, default is true.
*/
dojo.widget.defineWidget(
	"dojo.widget.validate.IpAddressTextbox",
	dojo.widget.validate.ValidationTextbox,
	{
		mixInProperties: function(localProperties, frag) {
			// First initialize properties in super-class.
			dojo.widget.validate.IpAddressTextbox.superclass.mixInProperties.apply(this, arguments);
	
			// Get properties from markup attributes, and assign to flags object.
			if ( localProperties.allowdotteddecimal ) { 
				this.flags.allowDottedDecimal = ( localProperties.allowdotteddecimal == "true" );
			}
			if ( localProperties.allowdottedhex ) { 
				this.flags.allowDottedHex = ( localProperties.allowdottedhex == "true" );
			}
			if ( localProperties.allowdottedoctal ) { 
				this.flags.allowDottedOctal = ( localProperties.allowdottedoctal == "true" );
			}
			if ( localProperties.allowdecimal ) { 
				this.flags.allowDecimal = ( localProperties.allowdecimal == "true" );
			}
			if ( localProperties.allowhex ) { 
				this.flags.allowHex = ( localProperties.allowhex == "true" );
			}
			if ( localProperties.allowipv6 ) { 
				this.flags.allowIPv6 = ( localProperties.allowipv6 == "true" );
			}
			if ( localProperties.allowhybrid ) { 
				this.flags.allowHybrid = ( localProperties.allowhybrid == "true" );
			}
		},

		// Over-ride for IP address validation
		isValid: function() { 
			return dojo.validate.isIpAddress(this.textbox.value, this.flags);
		}
	}
);
