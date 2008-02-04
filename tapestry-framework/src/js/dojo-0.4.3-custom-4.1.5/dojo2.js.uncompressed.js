/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("layer.validation");
dojo.provide("dojo.experimental");

dojo.experimental = function(/* String */ moduleName, /* String? */ extra){
	// summary: Marks code as experimental.
	// description: 
	//    This can be used to mark a function, file, or module as experimental.
	//    Experimental code is not ready to be used, and the APIs are subject
	//    to change without notice.  Experimental code may be completed deleted
	//    without going through the normal deprecation process.
	// moduleName: The name of a module, or the name of a module file or a specific function
	// extra: some additional message for the user
	
	// examples:
	//    dojo.experimental("dojo.data.Result");
	//    dojo.experimental("dojo.weather.toKelvin()", "PENDING approval from NOAA");
	var message = "EXPERIMENTAL: " + moduleName;
	message += " -- Not yet ready for use.  APIs subject to change without notice.";
	if(extra){ message += " " + extra; }
	dojo.debug(message);
}

dojo.provide("dojo.regexp");
dojo.evalObjPath("dojo.regexp.us", true);	// this file also defines stuff in the dojo.regexp.us module (TODO: move to separate file?)

// *** Regular Expression Generators ***

dojo.regexp.tld = function(/*Object?*/flags){
	// summary: Builds a RE that matches a top-level domain
	//
	// flags:
	//    flags.allowCC  Include 2 letter country code domains.  Default is true.
	//    flags.allowGeneric  Include the generic domains.  Default is true.
	//    flags.allowInfra  Include infrastructure domains.  Default is true.

	// assign default values to missing paramters
	flags = (typeof flags == "object") ? flags : {};
	if(typeof flags.allowCC != "boolean"){ flags.allowCC = true; }
	if(typeof flags.allowInfra != "boolean"){ flags.allowInfra = true; }
	if(typeof flags.allowGeneric != "boolean"){ flags.allowGeneric = true; }

	// Infrastructure top-level domain - only one at present
	var infraRE = "arpa";

	// Generic top-level domains RE.
	var genericRE = 
		"aero|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|xxx|jobs|mobi|post";
	
	// Country Code top-level domains RE
	var ccRE = 
		"ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|" +
		"bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cu|cv|cx|cy|cz|de|dj|dk|dm|do|dz|" +
		"ec|ee|eg|er|eu|es|et|fi|fj|fk|fm|fo|fr|ga|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|"
		+
		"gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kr|kw|ky|kz|" +
		"la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|" +
		"my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|" +
		"re|ro|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sk|sl|sm|sn|sr|st|su|sv|sy|sz|tc|td|tf|tg|th|tj|tk|tm|" +
		"tn|to|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|yu|za|zm|zw";

	// Build top-level domain RE
	var a = [];
	if(flags.allowInfra){ a.push(infraRE); }
	if(flags.allowGeneric){ a.push(genericRE); }
	if(flags.allowCC){ a.push(ccRE); }

	var tldRE = "";
	if (a.length > 0) {
		tldRE = "(" + a.join("|") + ")";
	}

	return tldRE; // String
}

dojo.regexp.ipAddress = function(/*Object?*/flags){
	// summary: Builds a RE that matches an IP Address
	//
	// description:
	//  Supports 5 formats for IPv4: dotted decimal, dotted hex, dotted octal, decimal and hexadecimal.
	//  Supports 2 formats for Ipv6.
	//
	// flags  An object.  All flags are boolean with default = true.
	//    flags.allowDottedDecimal  Example, 207.142.131.235.  No zero padding.
	//    flags.allowDottedHex  Example, 0x18.0x11.0x9b.0x28.  Case insensitive.  Zero padding allowed.
	//    flags.allowDottedOctal  Example, 0030.0021.0233.0050.  Zero padding allowed.
	//    flags.allowDecimal  Example, 3482223595.  A decimal number between 0-4294967295.
	//    flags.allowHex  Example, 0xCF8E83EB.  Hexadecimal number between 0x0-0xFFFFFFFF.
	//      Case insensitive.  Zero padding allowed.
	//    flags.allowIPv6   IPv6 address written as eight groups of four hexadecimal digits.
	//    flags.allowHybrid   IPv6 address written as six groups of four hexadecimal digits
	//      followed by the usual 4 dotted decimal digit notation of IPv4. x:x:x:x:x:x:d.d.d.d

	// assign default values to missing paramters
	flags = (typeof flags == "object") ? flags : {};
	if(typeof flags.allowDottedDecimal != "boolean"){ flags.allowDottedDecimal = true; }
	if(typeof flags.allowDottedHex != "boolean"){ flags.allowDottedHex = true; }
	if(typeof flags.allowDottedOctal != "boolean"){ flags.allowDottedOctal = true; }
	if(typeof flags.allowDecimal != "boolean"){ flags.allowDecimal = true; }
	if(typeof flags.allowHex != "boolean"){ flags.allowHex = true; }
	if(typeof flags.allowIPv6 != "boolean"){ flags.allowIPv6 = true; }
	if(typeof flags.allowHybrid != "boolean"){ flags.allowHybrid = true; }

	// decimal-dotted IP address RE.
	var dottedDecimalRE = 
		// Each number is between 0-255.  Zero padding is not allowed.
		"((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])";

	// dotted hex IP address RE.  Each number is between 0x0-0xff.  Zero padding is allowed, e.g. 0x00.
	var dottedHexRE = "(0[xX]0*[\\da-fA-F]?[\\da-fA-F]\\.){3}0[xX]0*[\\da-fA-F]?[\\da-fA-F]";

	// dotted octal IP address RE.  Each number is between 0000-0377.  
	// Zero padding is allowed, but each number must have at least 4 characters.
	var dottedOctalRE = "(0+[0-3][0-7][0-7]\\.){3}0+[0-3][0-7][0-7]";

	// decimal IP address RE.  A decimal number between 0-4294967295.  
	var decimalRE =  "(0|[1-9]\\d{0,8}|[1-3]\\d{9}|4[01]\\d{8}|42[0-8]\\d{7}|429[0-3]\\d{6}|" +
		"4294[0-8]\\d{5}|42949[0-5]\\d{4}|429496[0-6]\\d{3}|4294967[01]\\d{2}|42949672[0-8]\\d|429496729[0-5])";

	// hexadecimal IP address RE. 
	// A hexadecimal number between 0x0-0xFFFFFFFF. Case insensitive.  Zero padding is allowed.
	var hexRE = "0[xX]0*[\\da-fA-F]{1,8}";

	// IPv6 address RE. 
	// The format is written as eight groups of four hexadecimal digits, x:x:x:x:x:x:x:x,
	// where x is between 0000-ffff. Zero padding is optional. Case insensitive. 
	var ipv6RE = "([\\da-fA-F]{1,4}\\:){7}[\\da-fA-F]{1,4}";

	// IPv6/IPv4 Hybrid address RE. 
	// The format is written as six groups of four hexadecimal digits, 
	// followed by the 4 dotted decimal IPv4 format. x:x:x:x:x:x:d.d.d.d
	var hybridRE = "([\\da-fA-F]{1,4}\\:){6}" + 
		"((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])";

	// Build IP Address RE
	var a = [];
	if(flags.allowDottedDecimal){ a.push(dottedDecimalRE); }
	if(flags.allowDottedHex){ a.push(dottedHexRE); }
	if(flags.allowDottedOctal){ a.push(dottedOctalRE); }
	if(flags.allowDecimal){ a.push(decimalRE); }
	if(flags.allowHex){ a.push(hexRE); }
	if(flags.allowIPv6){ a.push(ipv6RE); }
	if(flags.allowHybrid){ a.push(hybridRE); }

	var ipAddressRE = "";
	if(a.length > 0){
		ipAddressRE = "(" + a.join("|") + ")";
	}

	return ipAddressRE; // String
}

dojo.regexp.host = function(/*Object?*/flags){
	// summary: Builds a RE that matches a host
	// description: A host is a domain name or an IP address, possibly followed by a port number.
	// flags: An object.
	//    flags.allowIP  Allow an IP address for hostname.  Default is true.
	//    flags.allowLocal  Allow the host to be "localhost".  Default is false.
	//    flags.allowPort  Allow a port number to be present.  Default is true.
	//    flags in regexp.ipAddress can be applied.
	//    flags in regexp.tld can be applied.

	// assign default values to missing paramters
	flags = (typeof flags == "object") ? flags : {};
	if(typeof flags.allowIP != "boolean"){ flags.allowIP = true; }
	if(typeof flags.allowLocal != "boolean"){ flags.allowLocal = false; }
	if(typeof flags.allowPort != "boolean"){ flags.allowPort = true; }

	// Domain names can not end with a dash.
	var domainNameRE = "([0-9a-zA-Z]([-0-9a-zA-Z]{0,61}[0-9a-zA-Z])?\\.)+" + dojo.regexp.tld(flags);

	// port number RE
	var portRE = ( flags.allowPort ) ? "(\\:" + dojo.regexp.integer({signed: false}) + ")?" : "";

	// build host RE
	var hostNameRE = domainNameRE;
	if(flags.allowIP){ hostNameRE += "|" +  dojo.regexp.ipAddress(flags); }
	if(flags.allowLocal){ hostNameRE += "|localhost"; }

	return "(" + hostNameRE + ")" + portRE; // String
}

dojo.regexp.url = function(/*Object?*/flags){
	// summary: Builds a regular expression that matches a URL
	//
	// flags: An object
	//    flags.scheme  Can be true, false, or [true, false]. 
	//      This means: required, not allowed, or match either one.
	//    flags in regexp.host can be applied.
	//    flags in regexp.ipAddress can be applied.
	//    flags in regexp.tld can be applied.

	// assign default values to missing paramters
	flags = (typeof flags == "object") ? flags : {};
	if(typeof flags.scheme == "undefined"){ flags.scheme = [true, false]; }

	// Scheme RE
	var protocolRE = dojo.regexp.buildGroupRE(flags.scheme,
		function(q){ if(q){ return "(https?|ftps?)\\://"; } return ""; }
	);

	// Path and query and anchor RE
	var pathRE = "(/([^?#\\s/]+/)*)?([^?#\\s/]+(\\?[^?#\\s/]*)?(#[A-Za-z][\\w.:-]*)?)?";

	return protocolRE + dojo.regexp.host(flags) + pathRE;
}


dojo.regexp.emailAddress = function(/*Object?*/flags){
	// summary: Builds a regular expression that matches an email address
	//
	//flags: An object
	//    flags.allowCruft  Allow address like <mailto:foo@yahoo.com>.  Default is false.
	//    flags in regexp.host can be applied.
	//    flags in regexp.ipAddress can be applied.
	//    flags in regexp.tld can be applied.

	// assign default values to missing paramters
	flags = (typeof flags == "object") ? flags : {};
	if (typeof flags.allowCruft != "boolean") { flags.allowCruft = false; }
	flags.allowPort = false; // invalid in email addresses

	// user name RE - apostrophes are valid if there's not 2 in a row
	var usernameRE = "([\\da-z]+[-._+&'])*[\\da-z]+";

	// build emailAddress RE
	var emailAddressRE = usernameRE + "@" + dojo.regexp.host(flags);

	// Allow email addresses with cruft
	if ( flags.allowCruft ) {
		emailAddressRE = "<?(mailto\\:)?" + emailAddressRE + ">?";
	}

	return emailAddressRE; // String
}

dojo.regexp.emailAddressList = function(/*Object?*/flags){
	// summary: Builds a regular expression that matches a list of email addresses.
	//
	// flags: An object.
	//    flags.listSeparator  The character used to separate email addresses.  Default is ";", ",", "\n" or " ".
	//    flags in regexp.emailAddress can be applied.
	//    flags in regexp.host can be applied.
	//    flags in regexp.ipAddress can be applied.
	//    flags in regexp.tld can be applied.

	// assign default values to missing paramters
	flags = (typeof flags == "object") ? flags : {};
	if(typeof flags.listSeparator != "string"){ flags.listSeparator = "\\s;,"; }

	// build a RE for an Email Address List
	var emailAddressRE = dojo.regexp.emailAddress(flags);
	var emailAddressListRE = "(" + emailAddressRE + "\\s*[" + flags.listSeparator + "]\\s*)*" + 
		emailAddressRE + "\\s*[" + flags.listSeparator + "]?\\s*";

	return emailAddressListRE; // String
}

dojo.regexp.integer = function(/*Object?*/flags){
	// summary: Builds a regular expression that matches an integer
	//
	// flags: An object
	//    flags.signed  The leading plus-or-minus sign.  Can be true, false, or [true, false].
	//      Default is [true, false], (i.e. will match if it is signed or unsigned).
	//    flags.separator  The character used as the thousands separator.  Default is no separator.
	//      For more than one symbol use an array, e.g. [",", ""], makes ',' optional.
	//	flags.groupSize group size between separators
	//	flags.groupSize2 second grouping (for India)

	// assign default values to missing paramters
	flags = (typeof flags == "object") ? flags : {};
	if(typeof flags.signed == "undefined"){ flags.signed = [true, false]; }
	if(typeof flags.separator == "undefined"){
		flags.separator = "";
	} else if(typeof flags.groupSize == "undefined"){
		flags.groupSize = 3;
	}
	// build sign RE
	var signRE = dojo.regexp.buildGroupRE(flags.signed,
		function(q) { return q ? "[-+]" : ""; }
	);

	// number RE
	var numberRE = dojo.regexp.buildGroupRE(flags.separator,
		function(sep){ 
			if(sep == ""){ 
				return "(0|[1-9]\\d*)";
			}
			var grp = flags.groupSize, grp2 = flags.groupSize2;
			if(typeof grp2 != "undefined"){
				var grp2RE = "(0|[1-9]\\d{0," + (grp2-1) + "}([" + sep + "]\\d{" + grp2 + "})*[" + sep + "]\\d{" + grp + "})";
				return ((grp-grp2) > 0) ? "(" + grp2RE + "|(0|[1-9]\\d{0," + (grp-1) + "}))" : grp2RE;
			}
			return  "(0|[1-9]\\d{0," + (grp-1) + "}([" + sep + "]\\d{" + grp + "})*)";
		}
	);

	// integer RE
	return signRE + numberRE; // String
}

dojo.regexp.realNumber = function(/*Object?*/flags){
	// summary: Builds a regular expression to match a real number in exponential notation
	//
	// flags:An object
	//    flags.places  The integer number of decimal places.
	//      If not given, the decimal part is optional and the number of places is unlimited.
	//    flags.decimal  A string for the character used as the decimal point.  Default is ".".
	//    flags.fractional  Whether decimal places are allowed.
	//      Can be true, false, or [true, false].  Default is [true, false]
	//    flags.exponent  Express in exponential notation.  Can be true, false, or [true, false].
	//      Default is [true, false], (i.e. will match if the exponential part is present are not).
	//    flags.eSigned  The leading plus-or-minus sign on the exponent.  Can be true, false, 
	//      or [true, false].  Default is [true, false], (i.e. will match if it is signed or unsigned).
	//    flags in regexp.integer can be applied.

	// assign default values to missing paramters
	flags = (typeof flags == "object") ? flags : {};
	if(typeof flags.places != "number"){ flags.places = Infinity; }
	if(typeof flags.decimal != "string"){ flags.decimal = "."; }
	if(typeof flags.fractional == "undefined"){ flags.fractional = [true, false]; }
	if(typeof flags.exponent == "undefined"){ flags.exponent = [true, false]; }
	if(typeof flags.eSigned == "undefined"){ flags.eSigned = [true, false]; }

	// integer RE
	var integerRE = dojo.regexp.integer(flags);

	// decimal RE
	var decimalRE = dojo.regexp.buildGroupRE(flags.fractional,
		function(q){
			var re = "";
			if(q && (flags.places > 0)){
				re = "\\" + flags.decimal;
				if(flags.places == Infinity){ 
					re = "(" + re + "\\d+)?"; 
				}else{ 
					re = re + "\\d{" + flags.places + "}"; 
				}
			}

			return re;
		}
	);

	// exponent RE
	var exponentRE = dojo.regexp.buildGroupRE(flags.exponent,
		function(q){ 
			if(q){ return "([eE]" + dojo.regexp.integer({ signed: flags.eSigned}) + ")"; }
			return ""; 
		}
	);

	// real number RE
	return integerRE + decimalRE + exponentRE; // String
}

dojo.regexp.currency = function(/*Object?*/flags){
	// summary: Builds a regular expression to match a monetary value
	//
	// flags: An object
	//    flags.symbol  A currency symbol such as Yen "�", Pound "�", or the Euro sign "�".  
	//      Default is "$".  For more than one symbol use an array, e.g. ["$", ""], makes $ optional.
	//    flags.placement  The symbol can come "before" the number or "after" the number.  Default is "before".
	//    flags.signPlacement  The sign can come "before" the number or "after" the sign,
	//      "around" to put parentheses around negative values, or "end" for the final char.  Default is "before".
	//    flags.cents  deprecated, in favor of flags.fractional
	//    flags in regexp.realNumber can be applied except exponent, eSigned.

	// assign default values to missing paramters
	flags = (typeof flags == "object") ? flags : {};
	if(typeof flags.signed == "undefined"){ flags.signed = [true, false]; }
	if(typeof flags.symbol == "undefined"){ flags.symbol = "$"; }
	if(typeof flags.placement != "string"){ flags.placement = "before"; }
	if(typeof flags.signPlacement != "string"){ flags.signPlacement = "before"; }
	if(typeof flags.separator == "undefined"){ flags.separator = ","; }
	if(typeof flags.fractional == "undefined" && typeof flags.cents != "undefined"){
		dojo.deprecated("dojo.regexp.currency: flags.cents", "use flags.fractional instead", "0.5");
		flags.fractional = flags.cents;
	}
	if(typeof flags.decimal != "string"){ flags.decimal = "."; }

	// build sign RE
	var signRE = dojo.regexp.buildGroupRE(flags.signed,
		function(q){ if (q){ return "[-+]"; } return ""; }
	);

	// build symbol RE
	var symbolRE = dojo.regexp.buildGroupRE(flags.symbol,
		function(symbol){ 
			// escape all special characters
			return "\\s?" + symbol.replace( /([.$?*!=:|\\\/^])/g, "\\$1") + "\\s?";
		}
	);

	switch (flags.signPlacement){
		case "before":
			symbolRE = signRE + symbolRE;
			break;
		case "after":
			symbolRE = symbolRE + signRE;
			break;
	}

	// number RE
	var flagsCopy = flags; //TODO: copy by value?
	flagsCopy.signed = false; flagsCopy.exponent = false;
	var numberRE = dojo.regexp.realNumber(flagsCopy);

	// build currency RE
	var currencyRE;
	switch (flags.placement){
		case "before":
			currencyRE = symbolRE + numberRE;
			break;
		case "after":
			currencyRE = numberRE + symbolRE;
			break;
	}

	switch (flags.signPlacement){
		case "around":
			currencyRE = "(" + currencyRE + "|" + "\\(" + currencyRE + "\\)" + ")";
			break;
		case "begin":
			currencyRE = signRE + currencyRE;
			break;
		case "end":
			currencyRE = currencyRE + signRE;
			break;
	}
	return currencyRE; // String
}


dojo.regexp.us.state = function(/*Object?*/flags){
	// summary: A regular expression to match US state and territory abbreviations
	//
	// flags  An object.
	//    flags.allowTerritories  Allow Guam, Puerto Rico, etc.  Default is true.
	//    flags.allowMilitary  Allow military 'states', e.g. Armed Forces Europe (AE).  Default is true.

	// assign default values to missing paramters
	flags = (typeof flags == "object") ? flags : {};
	if(typeof flags.allowTerritories != "boolean"){ flags.allowTerritories = true; }
	if(typeof flags.allowMilitary != "boolean"){ flags.allowMilitary = true; }

	// state RE
	var statesRE = 
		"AL|AK|AZ|AR|CA|CO|CT|DE|DC|FL|GA|HI|ID|IL|IN|IA|KS|KY|LA|ME|MD|MA|MI|MN|MS|MO|MT|" + 
		"NE|NV|NH|NJ|NM|NY|NC|ND|OH|OK|OR|PA|RI|SC|SD|TN|TX|UT|VT|VA|WA|WV|WI|WY";

	// territories RE
	var territoriesRE = "AS|FM|GU|MH|MP|PW|PR|VI";

	// military states RE
	var militaryRE = "AA|AE|AP";

	// Build states and territories RE
	if(flags.allowTerritories){ statesRE += "|" + territoriesRE; }
	if(flags.allowMilitary){ statesRE += "|" + militaryRE; }

	return "(" + statesRE + ")"; // String
}

dojo.regexp.time = function(/*Object?*/flags){
	// summary: Builds a regular expression to match any International format for time
	// description: The RE can match one format or one of multiple formats.
	//
	//  Format
	//  h        12 hour, no zero padding.
	//  hh       12 hour, has leading zero.
	//  H        24 hour, no zero padding.
	//  HH       24 hour, has leading zero.
	//  m        minutes, no zero padding.
	//  mm       minutes, has leading zero.
	//  s        seconds, no zero padding.
	//  ss       seconds, has leading zero.
	//  t        am or pm, case insensitive.
	//  All other characters must appear literally in the expression.
	//
	//  Example
	//    "h:m:s t"  ->   2:5:33 PM
	//    "HH:mm:ss" ->  14:05:33
	//
	// flags: An object
	//    flags.format  A string or an array of strings.  Default is "h:mm:ss t".
	//    flags.amSymbol  The symbol used for AM.  Default is "AM".
	//    flags.pmSymbol  The symbol used for PM.  Default is "PM".

	dojo.deprecated("dojo.regexp.time", "Use dojo.date.parse instead", "0.5");

	// assign default values to missing paramters
	flags = (typeof flags == "object") ? flags : {};
	if(typeof flags.format == "undefined"){ flags.format = "h:mm:ss t"; }
	if(typeof flags.amSymbol != "string"){ flags.amSymbol = "AM"; }
	if(typeof flags.pmSymbol != "string"){ flags.pmSymbol = "PM"; }

	// Converts a time format to a RE
	var timeRE = function(format){
		// escape all special characters
		format = format.replace( /([.$?*!=:|{}\(\)\[\]\\\/^])/g, "\\$1");
		var amRE = flags.amSymbol.replace( /([.$?*!=:|{}\(\)\[\]\\\/^])/g, "\\$1");
		var pmRE = flags.pmSymbol.replace( /([.$?*!=:|{}\(\)\[\]\\\/^])/g, "\\$1");

		// replace tokens with Regular Expressions
		format = format.replace("hh", "(0[1-9]|1[0-2])");
		format = format.replace("h", "([1-9]|1[0-2])");
		format = format.replace("HH", "([01][0-9]|2[0-3])");
		format = format.replace("H", "([0-9]|1[0-9]|2[0-3])");
		format = format.replace("mm", "([0-5][0-9])");
		format = format.replace("m", "([1-5][0-9]|[0-9])");
		format = format.replace("ss", "([0-5][0-9])");
		format = format.replace("s", "([1-5][0-9]|[0-9])");
		format = format.replace("t", "\\s?(" + amRE + "|" + pmRE + ")\\s?" );

		return format; // String
	};

	// build RE for multiple time formats
	return dojo.regexp.buildGroupRE(flags.format, timeRE); // String
}

dojo.regexp.numberFormat = function(/*Object?*/flags){
	// summary: Builds a regular expression to match any sort of number based format
	// description:
	//  Use this method for phone numbers, social security numbers, zip-codes, etc.
	//  The RE can match one format or one of multiple formats.
	//
	//  Format
	//    #        Stands for a digit, 0-9.
	//    ?        Stands for an optional digit, 0-9 or nothing.
	//    All other characters must appear literally in the expression.
	//
	//  Example   
	//    "(###) ###-####"       ->   (510) 542-9742
	//    "(###) ###-#### x#???" ->   (510) 542-9742 x153
	//    "###-##-####"          ->   506-82-1089       i.e. social security number
	//    "#####-####"           ->   98225-1649        i.e. zip code
	//
	// flags:  An object
	//    flags.format  A string or an Array of strings for multiple formats.

	// assign default values to missing paramters
	flags = (typeof flags == "object") ? flags : {};
	if(typeof flags.format == "undefined"){ flags.format = "###-###-####"; }

	// Converts a number format to RE.
	var digitRE = function(format){
		// escape all special characters, except '?'
		format = format.replace( /([.$*!=:|{}\(\)\[\]\\\/^])/g, "\\$1");

		// Now replace '?' with Regular Expression
		format = format.replace(/\?/g, "\\d?");

		// replace # with Regular Expression
		format = format.replace(/#/g, "\\d");

		return format; // String
	};

	// build RE for multiple number formats
	return dojo.regexp.buildGroupRE(flags.format, digitRE); //String
}


dojo.regexp.buildGroupRE = function(/*value or Array of values*/a, /*Function(x) returns a regular expression as a String*/re){
	// summary: Builds a regular expression that groups subexpressions
	// description: A utility function used by some of the RE generators.
	//  The subexpressions are constructed by the function, re, in the second parameter.
	//  re builds one subexpression for each elem in the array a, in the first parameter.
	//  Returns a string for a regular expression that groups all the subexpressions.
	//
	// a:  A single value or an array of values.
	// re:  A function.  Takes one parameter and converts it to a regular expression. 

	// case 1: a is a single value.
	if(!(a instanceof Array)){
		return re(a); // String
	}

	// case 2: a is an array
	var b = [];
	for (var i = 0; i < a.length; i++){
		// convert each elem to a RE
		b.push(re(a[i]));
	}

	 // join the REs as alternatives in a RE group.
	return "(" + b.join("|") + ")"; // String
}

dojo.provide("dojo.i18n.number");




dojo.require("dojo.i18n.common");
dojo.require("dojo.lang.common");

/**
* Method to Format and validate a given number
*
* @param Number value
*	The number to be formatted and validated.
* @param Object flags
*   flags.places number of decimal places to show, default is 0 (cannot be Infinity)
*   flags.round true to round the number, false to truncate
* @param String locale
*	The locale used to determine the number format.
* @return String
* 	the formatted number of type String if successful
*   or null if an unsupported locale value was provided
**/
dojo.i18n.number.format = function(value, flags /*optional*/, locale /*optional*/){
	flags = (typeof flags == "object") ? flags : {};

	var formatData = dojo.i18n.number._mapToLocalizedFormatData(dojo.i18n.number.FORMAT_TABLE, locale);
	if (typeof flags.separator == "undefined") {flags.separator = formatData[1];}
	if (typeof flags.decimal == "undefined") {flags.decimal = formatData[2];}
	if (typeof flags.groupSize == "undefined") {flags.groupSize = formatData[3];}
	if (typeof flags.groupSize2 == "undefined") {flags.groupSize2 = formatData[4];}
	if (typeof flags.round == "undefined") {flags.round = true;}
	if (typeof flags.signed == "undefined") {flags.signed = true;}

	var output = (flags.signed && (value < 0)) ? "-" : "";
	value = Math.abs(value);
	var whole = String((((flags.places > 0) || !flags.round) ? Math.floor : Math.round)(value));

	// Splits str into substrings of size count, starting from right to left.  Is there a more clever way to do this in JS?
	function splitSubstrings(str, count){
		for(var subs = []; str.length >= count; str = str.substr(0, str.length - count)){
			subs.push(str.substr(-count));
		}
		if (str.length > 0){subs.push(str);}
		return subs.reverse();
	}

	if (flags.groupSize2 && (whole.length > flags.groupSize)){
		var groups = splitSubstrings(whole.substr(0, whole.length - flags.groupSize), flags.groupSize2);
		groups.push(whole.substr(-flags.groupSize));
		output = output + groups.join(flags.separator);
	}else if (flags.groupSize){
		output = output + splitSubstrings(whole, flags.groupSize).join(flags.separator);
	}else{
		output = output + whole;
	}

//TODO: what if flags.places is Infinity?
	if (flags.places > 0){
	//Q: Is it safe to convert to a string and split on ".", or might that be locale dependent?  Use Math for now.
		var fract = value - Math.floor(value);
		fract = (flags.round ? Math.round : Math.floor)(fract * Math.pow(10, flags.places));
		output = output + flags.decimal + fract;
	}

//TODO: exp

	return output;
};

/**
* Method to convert a properly formatted int string to a primative numeric value.
*
* @param String value
*	The int string to be convertted
* @param string locale
*	The locale used to convert the number string
* @param Object flags
*   flags.validate true to check the string for strict adherence to the locale settings for separator, sign, etc.
*     Default is true
* @return Number
* 	Returns a value of type Number, Number.NaN if not a number, or null if locale is not supported.
**/
dojo.i18n.number.parse = function(value, locale /*optional*/, flags /*optional*/){
	flags = (typeof flags == "object") ? flags : {};

	var formatData = dojo.i18n.number._mapToLocalizedFormatData(dojo.i18n.number.FORMAT_TABLE, locale);
	if (typeof flags.separator == "undefined") {flags.separator = formatData[1];}
	if (typeof flags.decimal == "undefined") {flags.decimal = formatData[2];}
	if (typeof flags.groupSize == "undefined") {flags.groupSize = formatData[3];}
	if (typeof flags.groupSize2 == "undefined") {flags.groupSize2 = formatData[4];}
	if (typeof flags.validate == "undefined") {flags.validate = true;}

	if (flags.validate && !dojo.i18n.number.isReal(value, locale, flags)) {
		return Number.NaN;
	}

	var numbers = value.split(flags.decimal);
	if (numbers.length > 2){return Number.NaN; }
    var whole;
    if (flags.separator != ""){
        whole = Number(numbers[0].replace(new RegExp("\\" + flags.separator, "g"), ""));
    } else {
        whole = Number(numbers[0]);
    }
	var fract = (numbers.length == 1) ? 0 : Number(numbers[1]) / Math.pow(10, String(numbers[1]).length);

//TODO: exp

	return whole + fract;
};

/**
  Validates whether a string is in an integer format. 

  @param value  A string.
  @param locale the locale to determine formatting used.  By default, the locale defined by the
    host environment: dojo.locale
  @param flags  An object.
    flags.signed  The leading plus-or-minus sign.  Can be true, false, or [true, false].
      Default is [true, false], (i.e. sign is optional).
    flags.separator  The character used as the thousands separator.  Default is specified by the locale.
      For more than one symbol use an array, e.g. [",", ""], makes ',' optional.
      The empty array [] makes the default separator optional.   
  @return  true or false.
*/
dojo.i18n.number.isInteger = function(value, locale /*optional*/, flags /*optional*/) {
	flags = (typeof flags == "object") ? flags : {};

	var formatData = dojo.i18n.number._mapToLocalizedFormatData(dojo.i18n.number.FORMAT_TABLE, locale);
	if (typeof flags.separator == "undefined") {flags.separator = formatData[1];}
	else if (dojo.lang.isArray(flags.separator) && flags.separator.length ===0){
		flags.separator = [formatData[1],""];
	}
	if (typeof flags.groupSize == "undefined") {flags.groupSize = formatData[3];}
	if (typeof flags.groupSize2 == "undefined") {flags.groupSize2 = formatData[4];}

	var re = new RegExp("^" + dojo.regexp.integer(flags) + "$");
	return re.test(value);
};

/**
  Validates whether a string is a real valued number. 
  Format is the usual exponential notation.

  @param value  A string.
  @param locale the locale to determine formatting used.  By default, the locale defined by the
    host environment: dojo.locale
  @param flags  An object.
    flags.places  The integer number of decimal places.
      If not given, the decimal part is optional and the number of places is unlimited.
    flags.decimal  The character used for the decimal point.  The default is specified by the locale.
    flags.exponent  Express in exponential notation.  Can be true, false, or [true, false].
      Default is [true, false], (i.e. the exponential part is optional).
    flags.eSigned  The leading plus-or-minus sign on the exponent.  Can be true, false, 
      or [true, false].  Default is [true, false], (i.e. sign is optional).
    flags in regexp.integer can be applied.
  @return  true or false.
*/
dojo.i18n.number.isReal = function(value, locale /*optional*/, flags /*optional*/) {
	flags = (typeof flags == "object") ? flags : {};

	var formatData = dojo.i18n.number._mapToLocalizedFormatData(dojo.i18n.number.FORMAT_TABLE, locale);
	if (typeof flags.separator == "undefined") {flags.separator = formatData[1];}
	else if (dojo.lang.isArray(flags.separator) && flags.separator.length ===0){
		flags.separator = [formatData[1],""];
	}
	if (typeof flags.decimal == "undefined") {flags.decimal = formatData[2];}
	if (typeof flags.groupSize == "undefined") {flags.groupSize = formatData[3];}
	if (typeof flags.groupSize2 == "undefined") {flags.groupSize2 = formatData[4];}

	var re = new RegExp("^" + dojo.regexp.realNumber(flags) + "$");
	return re.test(value);
};

//TODO: hide in a closure?
//TODO: change to use hashes and mixins, rather than arrays
//Q: fallback algorithm/how to structure table:
// does it make sense to look by country code most of the time (wildcard match on
// language, except where it's relevant) and provide default country when only
// a language is given?
(function() {

dojo.i18n.number.FORMAT_TABLE = {
	//0: thousand seperator for monetary, 1: thousand seperator for number, 2: decimal seperator, 3: group size, 4: second group size because of india
	'ar-ae': ["","", ",", 1],
	'ar-bh': ["","",",", 1],
	'ar-dz': ["","",",", 1],
	'ar-eg': ["","", ",", 1],
	'ar-jo': ["","",",", 1],
	'ar-kw': ["","", ",", 1],
	'ar-lb': ["","", ",", 1],
	'ar-ma': ["","", ",", 1],
	'ar-om': ["","", ",", 1],
	'ar-qa': ["","", ",", 1],
	'ar-sa': ["","", ",", 1],
	'ar-sy': ["","", ",", 1],
	'ar-tn': ["","", ",", 1],
	'ar-ye': ["","", ",", 1],
	'cs-cz': [".",".", ",", 3],
	'da-dk': [".",".", ",", 3],
	'de-at': [".",".", ",", 3],
	'de-de': [".",".", ",", 3],
	'de-lu': [".",".", ",", 3],
	//IBM JSL defect 51278. right now we have problem with single quote. //IBM: explain?
	'de-ch': ["'","'", ".", 3], //Q: comma as decimal separator for currency??
	//'de-ch': [".",".", ",", 3],
	'el-gr': [".",".", ",", 3],
	'en-au': [",",",", ".", 3],
	'en-ca': [",",",", ".", 3],
	'en-gb': [",",",", ".", 3],
	'en-hk': [",",",", ".", 3],
	'en-ie': [",",",", ".", 3],
	'en-in': [",",",", ".", 3,2],//india-english, 1,23,456.78
	'en-nz': [",",",", ".", 3],
	'en-us': [",",",", ".", 3],
	'en-za': [",",",", ".", 3],
	
	'es-ar': [".",".", ",", 3],
	'es-bo': [".",".", ",", 3],
	'es-cl': [".",".", ",", 3],
	'es-co': [".",".", ",", 3],
	'es-cr': [".",".", ",", 3],
	'es-do': [".",".", ",", 3],
	'es-ec': [".",".", ",", 3],
	'es-es': [".",".", ",", 3],
	'es-gt': [",",",", ".", 3],
	'es-hn': [",",",", ".", 3],
	'es-mx': [",",",", ".", 3],
	'es-ni': [",",",", ".", 3],
	'es-pa': [",",",", ".", 3],
	'es-pe': [",",",", ".", 3],
	'es-pr': [",",",", ".", 3],
	'es-py': [".",".",",", 3],
	'es-sv': [",", ",",".", 3],
	'es-uy': [".",".",",", 3],
	'es-ve': [".",".", ",", 3],
	
	'fi-fi': [" "," ", ",", 3],
	
	'fr-be': [".",".",",", 3],
	'fr-ca': [" ", " ", ",", 3],
	
	'fr-ch': [" ", " ",".", 3],
	
	'fr-fr': [" "," ", ",", 3],
	'fr-lu': [".",".", ",", 3],
	
	'he-il': [",",",", ".", 3],
	
	'hu-hu': [" ", " ",",", 3],
	
	'it-ch': [" "," ", ".", 3],
	
	'it-it': [".",".", ",", 3],
	'ja-jp': [",",",", ".", 3],
	'ko-kr': [",", ",",".", 3],
	
	'no-no': [".",".", ",", 3],
	
	'nl-be': [" "," ", ",", 3],
	'nl-nl': [".",".", ",", 3],
	'pl-pl': [".", ".",",", 3],
	
	'pt-br': [".",".", ",", 3],
	'pt-pt': [".",".", "$", 3],
	'ru-ru': [" ", " ",",", 3],
	
	'sv-se': ["."," ", ",", 3],
	
	'tr-tr': [".",".", ",", 3],
	
	'zh-cn': [",",",", ".", 3],
	'zh-hk': [",",",",".", 3],
	'zh-tw': [",", ",",".", 3],
	'*': [",",",", ".", 3]
};
})();

dojo.i18n.number._mapToLocalizedFormatData = function(table, locale){
	locale = dojo.hostenv.normalizeLocale(locale);
//TODO: most- to least-specific search? search by country code?
//TODO: implement aliases to simplify and shorten tables
	var data = table[locale];
	if (typeof data == 'undefined'){data = table['*'];}
	return data;
}

dojo.provide("dojo.validate.common");




dojo.validate.isText = function(/*String*/value, /*Object?*/flags){
// summary:
//	Checks if a string has non whitespace characters. 
//	Parameters allow you to constrain the length.
//
// value: A string
// flags: {length: Number, minlength: Number, maxlength: Number}
//    flags.length  If set, checks if there are exactly flags.length number of characters.
//    flags.minlength  If set, checks if there are at least flags.minlength number of characters.
//    flags.maxlength  If set, checks if there are at most flags.maxlength number of characters.

	flags = (typeof flags == "object") ? flags : {};

	// test for text
	if(/^\s*$/.test(value)){ return false; } // Boolean

	// length tests
	if(typeof flags.length == "number" && flags.length != value.length){ return false; } // Boolean
	if(typeof flags.minlength == "number" && flags.minlength > value.length){ return false; } // Boolean
	if(typeof flags.maxlength == "number" && flags.maxlength < value.length){ return false; } // Boolean

	return true; // Boolean
}

dojo.validate.isInteger = function(/*String*/value, /*Object?*/flags){
// summary:
//	Validates whether a string is in an integer format
//
// value  A string
// flags  {signed: Boolean|[true,false], separator: String}
//    flags.signed  The leading plus-or-minus sign.  Can be true, false, or [true, false].
//      Default is [true, false], (i.e. sign is optional).
//    flags.separator  The character used as the thousands separator.  Default is no separator.
//      For more than one symbol use an array, e.g. [",", ""], makes ',' optional.

	var re = new RegExp("^" + dojo.regexp.integer(flags) + "$");
	return re.test(value); // Boolean
}

dojo.validate.isRealNumber = function(/*String*/value, /*Object?*/flags){
// summary:
//	Validates whether a string is a real valued number. 
//	Format is the usual exponential notation.
//
// value: A string
// flags: {places: Number, decimal: String, exponent: Boolean|[true,false], eSigned: Boolean|[true,false], ...}
//    flags.places  The integer number of decimal places.
//      If not given, the decimal part is optional and the number of places is unlimited.
//    flags.decimal  The character used for the decimal point.  Default is ".".
//    flags.exponent  Express in exponential notation.  Can be true, false, or [true, false].
//      Default is [true, false], (i.e. the exponential part is optional).
//    flags.eSigned  The leading plus-or-minus sign on the exponent.  Can be true, false, 
//      or [true, false].  Default is [true, false], (i.e. sign is optional).
//    flags in regexp.integer can be applied.

	var re = new RegExp("^" + dojo.regexp.realNumber(flags) + "$");
	return re.test(value); // Boolean
}

dojo.validate.isCurrency = function(/*String*/value, /*Object?*/flags){
// summary:
//	Validates whether a string denotes a monetary value. 
// value: A string
// flags: {signed:Boolean|[true,false], symbol:String, placement:String, separator:String,
//	fractional:Boolean|[true,false], decimal:String}
//    flags.signed  The leading plus-or-minus sign.  Can be true, false, or [true, false].
//      Default is [true, false], (i.e. sign is optional).
//    flags.symbol  A currency symbol such as Yen "�", Pound "�", or the Euro sign "�".  
//      Default is "$".  For more than one symbol use an array, e.g. ["$", ""], makes $ optional.
//    flags.placement  The symbol can come "before" the number or "after".  Default is "before".
//    flags.separator  The character used as the thousands separator. The default is ",".
//    flags.fractional  The appropriate number of decimal places for fractional currency (e.g. cents)
//      Can be true, false, or [true, false].  Default is [true, false], (i.e. cents are optional).
//    flags.decimal  The character used for the decimal point.  Default is ".".

	var re = new RegExp("^" + dojo.regexp.currency(flags) + "$");
	return re.test(value); // Boolean
}

dojo.validate._isInRangeCache = {};

dojo.validate.isInRange = function(/*String*/value, /*Object?*/flags){
//summary:
//	Validates whether a string denoting an integer, 
//	real number, or monetary value is between a max and min. 
//
// value: A string
// flags: {max:Number, min:Number, decimal:String}
//    flags.max  A number, which the value must be less than or equal to for the validation to be true.
//    flags.min  A number, which the value must be greater than or equal to for the validation to be true.
//    flags.decimal  The character used for the decimal point.  Default is ".".


	//stripping the separator allows NaN to perform as expected, if no separator, we assume ','
	//once i18n support is ready for this, instead of assuming, we default to i18n's recommended value
	value = value.replace(dojo.lang.has(flags,'separator')?flags.separator:',', '', 'g').
		replace(dojo.lang.has(flags,'symbol')?flags.symbol:'$', '');
	if(isNaN(value)){
		return false; // Boolean
	}
	// assign default values to missing paramters
	flags = (typeof flags == "object") ? flags : {};
	var max = (typeof flags.max == "number") ? flags.max : Infinity;
	var min = (typeof flags.min == "number") ? flags.min : -Infinity;
	var dec = (typeof flags.decimal == "string") ? flags.decimal : ".";
	
	var cache = dojo.validate._isInRangeCache;
	var cacheIdx = value+"max"+max+"min"+min+"dec"+dec;
	if(typeof cache[cacheIdx] != "undefined"){
		return cache[cacheIdx];
	}

	// splice out anything not part of a number
	var pattern = "[^" + dec + "\\deE+-]";
	value = value.replace(RegExp(pattern, "g"), "");

	// trim ends of things like e, E, or the decimal character
	value = value.replace(/^([+-]?)(\D*)/, "$1");
	value = value.replace(/(\D*)$/, "");

	// replace decimal with ".". The minus sign '-' could be the decimal!
	pattern = "(\\d)[" + dec + "](\\d)";
	value = value.replace(RegExp(pattern, "g"), "$1.$2");

	value = Number(value);
	if ( value < min || value > max ) { cache[cacheIdx] = false; return false; } // Boolean

	cache[cacheIdx] = true; return true; // Boolean
}

dojo.validate.isNumberFormat = function(/*String*/value, /*Object?*/flags){
// summary:
//	Validates any sort of number based format
//
// description:
//	Use it for phone numbers, social security numbers, zip-codes, etc.
//	The value can be validated against one format or one of multiple formats.
//
//  Format
//    #        Stands for a digit, 0-9.
//    ?        Stands for an optional digit, 0-9 or nothing.
//    All other characters must appear literally in the expression.
//
//  Example   
//    "(###) ###-####"       ->   (510) 542-9742
//    "(###) ###-#### x#???" ->   (510) 542-9742 x153
//    "###-##-####"          ->   506-82-1089       i.e. social security number
//    "#####-####"           ->   98225-1649        i.e. zip code
//
// value: A string
// flags: {format:String}
//    flags.format  A string or an Array of strings for multiple formats.

	var re = new RegExp("^" + dojo.regexp.numberFormat(flags) + "$", "i");
	return re.test(value); // Boolean
}

dojo.validate.isValidLuhn = function(/*String*/value){
//summary: Compares value against the Luhn algorithm to verify its integrity
	var sum, parity, curDigit;
	if(typeof value!='string'){
		value = String(value);
	}
	value = value.replace(/[- ]/g,''); //ignore dashes and whitespaces
	parity = value.length%2;
	sum=0;
	for(var i=0;i<value.length;i++){
		curDigit = parseInt(value.charAt(i));
		if(i%2==parity){
			curDigit*=2;
		}
		if(curDigit>9){
			curDigit-=9;
		}
		sum+=curDigit;
	}
	return !(sum%10); //Boolean
}

/**
	Procedural API Description

		The main aim is to make input validation expressible in a simple format.
		You define profiles which declare the required and optional fields and any constraints they might have.
		The results are provided as an object that makes it easy to handle missing and invalid input.

	Usage

		var results = dojo.validate.check(form, profile);

	Profile Object

		var profile = {
			// filters change the field value and are applied before validation.
			trim: ["tx1", "tx2"],
			uppercase: ["tx9"],
			lowercase: ["tx5", "tx6", "tx7"],
			ucfirst: ["tx10"],
			digit: ["tx11"],

			// required input fields that are blank will be reported missing.
			// required radio button groups and drop-down lists with no selection will be reported missing.
			// checkbox groups and selectboxes can be required to have more than one value selected.
			// List required fields by name and use this notation to require more than one value: {checkboxgroup: 2}, {selectboxname: 3}.
			required: ["tx7", "tx8", "pw1", "ta1", "rb1", "rb2", "cb3", "s1", {"doubledip":2}, {"tripledip":3}],

			// dependant/conditional fields are required if the target field is present and not blank.
			// At present only textbox, password, and textarea fields are supported.
			dependencies:	{
				cc_exp: "cc_no",	
				cc_type: "cc_no",	
			},

			// Fields can be validated using any boolean valued function.  
			// Use arrays to specify parameters in addition to the field value.
			constraints: {
				field_name1: myValidationFunction,
				field_name2: dojo.validate.isInteger,
				field_name3: [myValidationFunction, additional parameters],
				field_name4: [dojo.validate.isValidDate, "YYYY.MM.DD"],
				field_name5: [dojo.validate.isEmailAddress, false, true],
			},

			// Confirm is a sort of conditional validation.
			// It associates each field in its property list with another field whose value should be equal.
			// If the values are not equal, the field in the property list is reported as Invalid. Unless the target field is blank.
			confirm: {
				email_confirm: "email",	
				pw2: "pw1",	
			}
		};

	Results Object

		isSuccessful(): Returns true if there were no invalid or missing fields, else it returns false.
		hasMissing():  Returns true if the results contain any missing fields.
		getMissing():  Returns a list of required fields that have values missing.
		isMissing(field):  Returns true if the field is required and the value is missing.
		hasInvalid():  Returns true if the results contain fields with invalid data.
		getInvalid():  Returns a list of fields that have invalid values.
		isInvalid(field):  Returns true if the field has an invalid value.

*/

dojo.provide("dojo.validate.check");

dojo.require("dojo.lang.common");

dojo.validate.check = function(/*HTMLFormElement*/form, /*Object*/profile){
	// summary: validates user input of an HTML form based on input profile
	//
	// description:
	//	returns an object that contains several methods summarizing the results of the validation
	//
	// form: form to be validated
	// profile: specifies how the form fields are to be validated
	// {trim:Array, uppercase:Array, lowercase:Array, ucfirst:Array, digit:Array,
	//	required:Array, dependencies:Object, constraints:Object, confirm:Object}

	// Essentially private properties of results object
	var missing = [];
	var invalid = [];

	// results object summarizes the validation
	var results = {
		isSuccessful: function() {return ( !this.hasInvalid() && !this.hasMissing() );},
		hasMissing: function() {return ( missing.length > 0 );},
		getMissing: function() {return missing;},
		isMissing: function(elemname) {
			for(var i = 0; i < missing.length; i++){
				if(elemname == missing[i]){ return true; }
			}
			return false;
		},
		hasInvalid: function() {return ( invalid.length > 0 );},
		getInvalid: function() {return invalid;},
		isInvalid: function(elemname){
			for(var i = 0; i < invalid.length; i++){
				if(elemname == invalid[i]){ return true; }
			}
			return false;
		}
	};

	// Filters are applied before fields are validated.
	// Trim removes white space at the front and end of the fields.
	if(profile.trim instanceof Array){
		for(var i = 0; i < profile.trim.length; i++){
			var elem = form[profile.trim[i]];
			if(dj_undef("type", elem) || elem.type != "text" && elem.type != "textarea" && elem.type != "password"){ continue; }
			elem.value = elem.value.replace(/(^\s*|\s*$)/g, "");
		}
	}
	// Convert to uppercase
	if(profile.uppercase instanceof Array){
		for(var i = 0; i < profile.uppercase.length; i++){
			var elem = form[profile.uppercase[i]];
			if(dj_undef("type", elem) || elem.type != "text" && elem.type != "textarea" && elem.type != "password"){ continue; }
			elem.value = elem.value.toUpperCase();
		}
	}
	// Convert to lowercase
	if(profile.lowercase instanceof Array){
		for (var i = 0; i < profile.lowercase.length; i++){
			var elem = form[profile.lowercase[i]];
			if(dj_undef("type", elem) || elem.type != "text" && elem.type != "textarea" && elem.type != "password"){ continue; }
			elem.value = elem.value.toLowerCase();
		}
	}
	// Uppercase first letter
	if(profile.ucfirst instanceof Array){
		for(var i = 0; i < profile.ucfirst.length; i++){
			var elem = form[profile.ucfirst[i]];
			if(dj_undef("type", elem) || elem.type != "text" && elem.type != "textarea" && elem.type != "password"){ continue; }
			elem.value = elem.value.replace(/\b\w+\b/g, function(word) { return word.substring(0,1).toUpperCase() + word.substring(1).toLowerCase(); });
		}
	}
	// Remove non digits characters from the input.
	if(profile.digit instanceof Array){
		for(var i = 0; i < profile.digit.length; i++){
			var elem = form[profile.digit[i]];
			if(dj_undef("type", elem) || elem.type != "text" && elem.type != "textarea" && elem.type != "password"){ continue; }
			elem.value = elem.value.replace(/\D/g, "");
		}
	}

	// See if required input fields have values missing.
	if(profile.required instanceof Array){
		for(var i = 0; i < profile.required.length; i++){ 
			if(!dojo.lang.isString(profile.required[i])){ continue; }
			var elem = form[profile.required[i]];
			// Are textbox, textarea, or password fields blank.
			if(!dj_undef("type", elem) && (elem.type == "text" || elem.type == "textarea" || elem.type == "password" || elem.type == "file") && /^\s*$/.test(elem.value)){
				missing[missing.length] = elem.name;
			}
			// Does drop-down box have option selected.
			else if(!dj_undef("type", elem) && (elem.type == "select-one" || elem.type == "select-multiple") 
						&& (elem.selectedIndex == -1 
						|| /^\s*$/.test(elem.options[elem.selectedIndex].value))){
				missing[missing.length] = elem.name;
			}
			// Does radio button group (or check box group) have option checked.
			else if(elem instanceof Array){
				var checked = false;
				for(var j = 0; j < elem.length; j++){
					if (elem[j].checked) { checked = true; }
				}
				if(!checked){	
					missing[missing.length] = elem[0].name;
				}
			}
		}
	}

	// See if checkbox groups and select boxes have x number of required values.
	if(profile.required instanceof Array){
		for (var i = 0; i < profile.required.length; i++){ 
			if(!dojo.lang.isObject(profile.required[i])){ continue; }
			var elem, numRequired;
			for(var name in profile.required[i]){ 
				elem = form[name]; 
				numRequired = profile.required[i][name];
			}
			// case 1: elem is a check box group
			if(elem instanceof Array){
				var checked = 0;
				for(var j = 0; j < elem.length; j++){
					if(elem[j].checked){ checked++; }
				}
				if(checked < numRequired){	
					missing[missing.length] = elem[0].name;
				}
			}
			// case 2: elem is a select box
			else if(!dj_undef("type", elem) && elem.type == "select-multiple" ){
				var selected = 0;
				for(var j = 0; j < elem.options.length; j++){
					if (elem.options[j].selected && !/^\s*$/.test(elem.options[j].value)) { selected++; }
				}
				if(selected < numRequired){	
					missing[missing.length] = elem.name;
				}
			}
		}
	}

	// Dependent fields are required when the target field is present (not blank).
	// Todo: Support dependent and target fields that are radio button groups, or select drop-down lists.
	// Todo: Make the dependency based on a specific value of the target field.
	// Todo: allow dependent fields to have several required values, like {checkboxgroup: 3}.
	if(dojo.lang.isObject(profile.dependencies) || dojo.lang.isObject(profile.dependancies)){
		if(profile["dependancies"]){
			dojo.deprecated("dojo.validate.check", "profile 'dependancies' is deprecated, please use "
							+ "'dependencies'", "0.5");
			profile.dependencies=profile.dependancies;
		}
		// properties of dependencies object are the names of dependent fields to be checked
		for(name in profile.dependencies){
			var elem = form[name];	// the dependent element
			if(dj_undef("type", elem)){continue;}
			if(elem.type != "text" && elem.type != "textarea" && elem.type != "password"){ continue; } // limited support
			if(/\S+/.test(elem.value)){ continue; }	// has a value already
			if(results.isMissing(elem.name)){ continue; }	// already listed as missing
			var target = form[profile.dependencies[name]];
			if(target.type != "text" && target.type != "textarea" && target.type != "password"){ continue; }	// limited support
			if(/^\s*$/.test(target.value)){ continue; }	// skip if blank
			missing[missing.length] = elem.name;	// ok the dependent field is missing
		}
	}

	// Find invalid input fields.
	if(dojo.lang.isObject(profile.constraints)){
		// constraint properties are the names of fields to bevalidated
		for(name in profile.constraints){
			var elem = form[name];
			if(!elem) {continue;}
			
			// skip if blank - its optional unless required, in which case it
			// is already listed as missing.
			if(!dj_undef("tagName",elem) 
				&& (elem.tagName.toLowerCase().indexOf("input") >= 0
					|| elem.tagName.toLowerCase().indexOf("textarea") >= 0) 
				&& /^\s*$/.test(elem.value)){ 
				continue; 
			}
			
			var isValid = true;
			// case 1: constraint value is validation function
			if(dojo.lang.isFunction(profile.constraints[name])){
				isValid = profile.constraints[name](elem.value);
			}else if(dojo.lang.isArray(profile.constraints[name])){
				
				// handle nested arrays for multiple constraints
				if(dojo.lang.isArray(profile.constraints[name][0])){
					for (var i=0; i<profile.constraints[name].length; i++){
						isValid = dojo.validate.evaluateConstraint(profile, profile.constraints[name][i], name, elem);
						if(!isValid){ break; }
					}
				}else{
					// case 2: constraint value is array, first elem is function,
					// tail is parameters
					isValid = dojo.validate.evaluateConstraint(profile, profile.constraints[name], name, elem);
				}
			}
			
			if(!isValid){	
				invalid[invalid.length] = elem.name;
			}
		}
	}

	// Find unequal confirm fields and report them as Invalid.
	if(dojo.lang.isObject(profile.confirm)){
		for(name in profile.confirm){
			var elem = form[name];	// the confirm element
			var target = form[profile.confirm[name]];
			if (dj_undef("type", elem) || dj_undef("type", target) || (elem.type != "text" && elem.type != "textarea" && elem.type != "password") 
				||(target.type != elem.type)
				||(target.value == elem.value)	// it's valid
				||(results.isInvalid(elem.name))// already listed as invalid
				||(/^\s*$/.test(target.value)))	// skip if blank - only confirm if target has a value
			{
				continue; 
			}
			invalid[invalid.length] = elem.name;
		}
	}

	return results; // Object
};

//TODO: evaluateConstraint doesn't use profile or fieldName args?
dojo.validate.evaluateConstraint=function(profile, /*Array*/constraint, fieldName, elem){
	// summary:
	//	Evaluates dojo.validate.check() constraints that are specified as array
	//	arguments
	//
	// description: The arrays are expected to be in the format of:
	//      constraints:{
	//              fieldName: [functionToCall, param1, param2, etc.],
	//              fieldName: [[functionToCallFirst, param1],[functionToCallSecond,param2]]
	//      }
	// 
	//  This function evaluates a single array function in the format of:
	//      [functionName, argument1, argument2, etc]
	// 
	//  The function will be parsed out and evaluated against the incoming parameters.
	//
	// profile: The dojo.validate.check() profile that this evaluation is against.
	// constraint: The single [] array of function and arguments for the function.
	// fieldName: The form dom name of the field being validated.
	// elem: The form element field.
	
 	var isValidSomething = constraint[0];
	var params = constraint.slice(1);
	params.unshift(elem.value);
	if(typeof isValidSomething != "undefined"){
		return isValidSomething.apply(null, params);
	}
	return false; // Boolean
}

dojo.provide("dojo.date.common");


/* Supplementary Date Functions
 *******************************/

dojo.date.setDayOfYear = function(/*Date*/dateObject, /*Number*/dayOfYear){
	// summary: sets dateObject according to day of the year (1..366)
	dateObject.setMonth(0);
	dateObject.setDate(dayOfYear);
	return dateObject; // Date
}

dojo.date.getDayOfYear = function(/*Date*/dateObject){
	// summary: gets the day of the year as represented by dateObject
	var fullYear = dateObject.getFullYear();
	var lastDayOfPrevYear = new Date(fullYear-1, 11, 31);
	return Math.floor((dateObject.getTime() -
		lastDayOfPrevYear.getTime()) / 86400000); // Number
}


dojo.date.setWeekOfYear = function(/*Date*/dateObject, /*Number*/week, /*Number*/firstDay){
	if(arguments.length == 1){ firstDay = 0; } // Sunday
	dojo.unimplemented("dojo.date.setWeekOfYear");
}

dojo.date.getWeekOfYear = function(/*Date*/dateObject, /*Number*/firstDay){
	if(arguments.length == 1){ firstDay = 0; } // Sunday

	// work out the first day of the year corresponding to the week
	var firstDayOfYear = new Date(dateObject.getFullYear(), 0, 1);
	var day = firstDayOfYear.getDay();
	firstDayOfYear.setDate(firstDayOfYear.getDate() -
			day + firstDay - (day > firstDay ? 7 : 0));

	return Math.floor((dateObject.getTime() -
		firstDayOfYear.getTime()) / 604800000);
}

dojo.date.setIsoWeekOfYear = function(/*Date*/dateObject, /*Number*/week, /*Number*/firstDay){
	// summary: unimplemented
	if (arguments.length == 1) { firstDay = 1; } // Monday
	dojo.unimplemented("dojo.date.setIsoWeekOfYear");
}

dojo.date.getIsoWeekOfYear = function(/*Date*/dateObject, /*Number*/firstDay) {
	// summary: unimplemented
	if (arguments.length == 1) { firstDay = 1; } // Monday
	dojo.unimplemented("dojo.date.getIsoWeekOfYear");
}


/* Informational Functions
 **************************/

//DEPRECATED: These timezone arrays will be deprecated in 0.5
dojo.date.shortTimezones = ["IDLW", "BET", "HST", "MART", "AKST", "PST", "MST",
	"CST", "EST", "AST", "NFT", "BST", "FST", "AT", "GMT", "CET", "EET", "MSK",
	"IRT", "GST", "AFT", "AGTT", "IST", "NPT", "ALMT", "MMT", "JT", "AWST",
	"JST", "ACST", "AEST", "LHST", "VUT", "NFT", "NZT", "CHAST", "PHOT",
	"LINT"];
dojo.date.timezoneOffsets = [-720, -660, -600, -570, -540, -480, -420, -360,
	-300, -240, -210, -180, -120, -60, 0, 60, 120, 180, 210, 240, 270, 300,
	330, 345, 360, 390, 420, 480, 540, 570, 600, 630, 660, 690, 720, 765, 780,
	840];
/*
dojo.date.timezones = ["International Date Line West", "Bering Standard Time",
	"Hawaiian Standard Time", "Marquesas Time", "Alaska Standard Time",
	"Pacific Standard Time (USA)", "Mountain Standard Time",
	"Central Standard Time (USA)", "Eastern Standard Time (USA)",
	"Atlantic Standard Time", "Newfoundland Time", "Brazil Standard Time",
	"Fernando de Noronha Standard Time (Brazil)", "Azores Time",
	"Greenwich Mean Time", "Central Europe Time", "Eastern Europe Time",
	"Moscow Time", "Iran Standard Time", "Gulf Standard Time",
	"Afghanistan Time", "Aqtobe Time", "Indian Standard Time", "Nepal Time",
	"Almaty Time", "Myanmar Time", "Java Time",
	"Australian Western Standard Time", "Japan Standard Time",
	"Australian Central Standard Time", "Lord Hove Standard Time (Australia)",
	"Vanuata Time", "Norfolk Time (Australia)", "New Zealand Standard Time",
	"Chatham Standard Time (New Zealand)", "Phoenix Islands Time (Kribati)",
	"Line Islands Time (Kribati)"];
*/

dojo.date.getDaysInMonth = function(/*Date*/dateObject){
	// summary: returns the number of days in the month used by dateObject
	var month = dateObject.getMonth();
	var days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
	if (month == 1 && dojo.date.isLeapYear(dateObject)) { return 29; } // Number
	else { return days[month]; } // Number
}

dojo.date.isLeapYear = function(/*Date*/dateObject){
// summary:
//	Determines if the year of the dateObject is a leap year
//
// description:
//	Leap years are years with an additional day YYYY-02-29, where the year
//	number is a multiple of four with the following exception: If a year
//	is a multiple of 100, then it is only a leap year if it is also a
//	multiple of 400. For example, 1900 was not a leap year, but 2000 is one.

	var year = dateObject.getFullYear();
	return (year%400 == 0) ? true : (year%100 == 0) ? false : (year%4 == 0) ? true : false; // Boolean
}

// FIXME: This is not localized
dojo.date.getTimezoneName = function(/*Date*/dateObject){
// summary:
//	Get the user's time zone as provided by the browser
//
// dateObject: needed because the timezone may vary with time (daylight savings)
//
// description:
//	Try to get time zone info from toString or toLocaleString
//	method of the Date object -- UTC offset is not a time zone.
//	See http://www.twinsun.com/tz/tz-link.htm
//	Note: results may be inconsistent across browsers.

	var str = dateObject.toString(); // Start looking in toString
	var tz = ''; // The result -- return empty string if nothing found
	var match;

	// First look for something in parentheses -- fast lookup, no regex
	var pos = str.indexOf('(');
	if (pos > -1) {
		pos++;
		tz = str.substring(pos, str.indexOf(')'));
	}
	// If at first you don't succeed ...
	else{
		// If IE knows about the TZ, it appears before the year
		// Capital letters or slash before a 4-digit year 
		// at the end of string
		var pat = /([A-Z\/]+) \d{4}$/;
		if((match = str.match(pat))) {
			tz = match[1];
		}
		// Some browsers (e.g. Safari) glue the TZ on the end
		// of toLocaleString instead of putting it in toString
		else{
			str = dateObject.toLocaleString();
			// Capital letters or slash -- end of string, 
			// after space
			pat = / ([A-Z\/]+)$/;
			if((match = str.match(pat))) {
				tz = match[1];
			}
		}
	}

	// Make sure it doesn't somehow end up return AM or PM
	return tz == 'AM' || tz == 'PM' ? '' : tz; //String
}


//FIXME: not localized
dojo.date.getOrdinal = function(dateObject){
	// summary: returns the appropriate suffix (English only) for the day of the month, e.g. 'st' for 1, 'nd' for 2, etc.)
	var date = dateObject.getDate();

	if(date%100 != 11 && date%10 == 1){ return "st"; } // String
	else if(date%100 != 12 && date%10 == 2){ return "nd"; } // String
	else if(date%100 != 13 && date%10 == 3){ return "rd"; } // String
	else{ return "th"; } // String
}


/* compare and add
 ******************/
dojo.date.compareTypes={
	// 	summary
	//	bitmask for comparison operations.
	DATE:1, TIME:2 
};
dojo.date.compare=function(/* Date */ dateA, /* Date */ dateB, /* dojo.date.compareTypes */ options){
	//	summary
	//	Compare two date objects by date, time, or both.  Returns 0 if equal, positive if a > b, else negative.
	var dA=dateA;
	var dB=dateB||new Date();
	var now=new Date();
	//FIXME: shorten this code
	with(dojo.date.compareTypes){
		var opt=options||(DATE|TIME);
		var d1=new Date(
			(opt&DATE)?dA.getFullYear():now.getFullYear(), 
			(opt&DATE)?dA.getMonth():now.getMonth(),
			(opt&DATE)?dA.getDate():now.getDate(),
			(opt&TIME)?dA.getHours():0,
			(opt&TIME)?dA.getMinutes():0,
			(opt&TIME)?dA.getSeconds():0
		);
		var d2=new Date(
			(opt&DATE)?dB.getFullYear():now.getFullYear(),
			(opt&DATE)?dB.getMonth():now.getMonth(),
			(opt&DATE)?dB.getDate():now.getDate(),
			(opt&TIME)?dB.getHours():0,
			(opt&TIME)?dB.getMinutes():0,
			(opt&TIME)?dB.getSeconds():0
		);
	}
	if(d1.valueOf()>d2.valueOf()){
		return 1;	//	int
	}
	if(d1.valueOf()<d2.valueOf()){
		return -1;	//	int
	}
	return 0;	//	int
}

dojo.date.dateParts={ 
	//	summary
	//	constants for use in dojo.date.add
	YEAR:0, MONTH:1, DAY:2, HOUR:3, MINUTE:4, SECOND:5, MILLISECOND:6, QUARTER:7, WEEK:8, WEEKDAY:9
};

dojo.date.add = function(/* Date */ dt, /* dojo.date.dateParts */ interv, /* int */ incr){
//	summary:
//		Add to a Date in intervals of different size, from milliseconds to years
//
//	dt:
//		A Javascript Date object to start with
//
//	interv:
//		A constant representing the interval, e.g. YEAR, MONTH, DAY.  See dojo.date.dateParts.
//
//	incr:
//		How much to add to the date

	if(typeof dt == 'number'){dt = new Date(dt);} // Allow timestamps
//FIXME: what's the reason behind this?	incr = incr || 1;

	function fixOvershoot(){
		if (sum.getDate() < dt.getDate()){
			sum.setDate(0);
		}
	}
	
	var sum = new Date(dt);

	with(dojo.date.dateParts){
		switch(interv){
			case YEAR:
				sum.setFullYear(dt.getFullYear()+incr);
				// Keep increment/decrement from 2/29 out of March
				fixOvershoot();
				break;
			case QUARTER:
				// Naive quarter is just three months
				incr*=3;
				// fallthrough...
			case MONTH:
				sum.setMonth(dt.getMonth()+incr);
				// Reset to last day of month if you overshoot
				fixOvershoot();
				break;
			case WEEK:
				incr*=7;
				// fallthrough...
			case DAY:
				sum.setDate(dt.getDate() + incr);
				break;
			case WEEKDAY:
				//FIXME: assumes Saturday/Sunday weekend, but even this is not fixed.  There are CLDR entries to localize this.
				var dat = dt.getDate();
				var weeks = 0;
				var days = 0;
				var strt = 0;
				var trgt = 0;
				var adj = 0;
				// Divide the increment time span into weekspans plus leftover days
				// e.g., 8 days is one 5-day weekspan / and two leftover days
				// Can't have zero leftover days, so numbers divisible by 5 get
				// a days value of 5, and the remaining days make up the number of weeks
				var mod = incr % 5;
				if (mod == 0) {
					days = (incr > 0) ? 5 : -5;
					weeks = (incr > 0) ? ((incr-5)/5) : ((incr+5)/5);
				}
				else {
					days = mod;
					weeks = parseInt(incr/5);
				}
				// Get weekday value for orig date param
				strt = dt.getDay();
				// Orig date is Sat / positive incrementer
				// Jump over Sun
				if (strt == 6 && incr > 0) {
					adj = 1;
				}
				// Orig date is Sun / negative incrementer
				// Jump back over Sat
				else if (strt == 0 && incr < 0) {
					adj = -1;
				}
				// Get weekday val for the new date
				trgt = (strt + days);
				// New date is on Sat or Sun
				if (trgt == 0 || trgt == 6) {
					adj = (incr > 0) ? 2 : -2;
				}
				// Increment by number of weeks plus leftover days plus
				// weekend adjustments
				sum.setDate(dat + (7*weeks) + days + adj);
				break;
			case HOUR:
				sum.setHours(sum.getHours()+incr);
				break;
			case MINUTE:
				sum.setMinutes(sum.getMinutes()+incr);
				break;
			case SECOND:
				sum.setSeconds(sum.getSeconds()+incr);
				break;
			case MILLISECOND:
				sum.setMilliseconds(sum.getMilliseconds()+incr);
				break;
			default:
				// Do nothing
				break;
		}
	}

	return sum; // Date
};

dojo.date.diff = function(/* Date */ dtA, /* Date */ dtB, /* dojo.date.dateParts */ interv){
//	summary:
//		Get the difference in a specific unit of time (e.g., number of months, weeks,
//		days, etc.) between two dates.
//
//	dtA:
//		A Javascript Date object
//
//	dtB:
//		A Javascript Date object
//
//	interv:
//		A constant representing the interval, e.g. YEAR, MONTH, DAY.  See dojo.date.dateParts.

	// Accept timestamp input
	if(typeof dtA == 'number'){dtA = new Date(dtA);}
	if(typeof dtB == 'number'){dtB = new Date(dtB);}
	var yeaDiff = dtB.getFullYear() - dtA.getFullYear();
	var monDiff = (dtB.getMonth() - dtA.getMonth()) + (yeaDiff * 12);
	var msDiff = dtB.getTime() - dtA.getTime(); // Millisecs
	var secDiff = msDiff/1000;
	var minDiff = secDiff/60;
	var houDiff = minDiff/60;
	var dayDiff = houDiff/24;
	var weeDiff = dayDiff/7;
	var delta = 0; // Integer return value

	with(dojo.date.dateParts){
		switch(interv){
			case YEAR:
				delta = yeaDiff;
				break;
			case QUARTER:
				var mA = dtA.getMonth();
				var mB = dtB.getMonth();
				// Figure out which quarter the months are in
				var qA = Math.floor(mA/3) + 1;
				var qB = Math.floor(mB/3) + 1;
				// Add quarters for any year difference between the dates
				qB += (yeaDiff * 4);
				delta = qB - qA;
				break;
			case MONTH:
				delta = monDiff;
				break;
			case WEEK:
				// Truncate instead of rounding
				// Don't use Math.floor -- value may be negative
				delta = parseInt(weeDiff);
				break;
			case DAY:
				delta = dayDiff;
				break;
			case WEEKDAY:
				var days = Math.round(dayDiff);
				var weeks = parseInt(days/7);
				var mod = days % 7;

				// Even number of weeks
				if (mod == 0) {
					days = weeks*5;
				}
				// Weeks plus spare change (< 7 days)
				else {
					var adj = 0;
					var aDay = dtA.getDay();
					var bDay = dtB.getDay();
	
					weeks = parseInt(days/7);
					mod = days % 7;
					// Mark the date advanced by the number of
					// round weeks (may be zero)
					var dtMark = new Date(dtA);
					dtMark.setDate(dtMark.getDate()+(weeks*7));
					var dayMark = dtMark.getDay();
					// Spare change days -- 6 or less
					// ----------
					// Positive diff
					if (dayDiff > 0) {
						switch (true) {
							// Range starts on Sat
							case aDay == 6:
								adj = -1;
								break;
							// Range starts on Sun
							case aDay == 0:
								adj = 0;
								break;
							// Range ends on Sat
							case bDay == 6:
								adj = -1;
								break;
							// Range ends on Sun
							case bDay == 0:
								adj = -2;
								break;
							// Range contains weekend
							case (dayMark + mod) > 5:
								adj = -2;
								break;
							default:
								// Do nothing
								break;
						}
					}
					// Negative diff
					else if (dayDiff < 0) {
						switch (true) {
							// Range starts on Sat
							case aDay == 6:
								adj = 0;
								break;
							// Range starts on Sun
							case aDay == 0:
								adj = 1;
								break;
							// Range ends on Sat
							case bDay == 6:
								adj = 2;
								break;
							// Range ends on Sun
							case bDay == 0:
								adj = 1;
								break;
							// Range contains weekend
							case (dayMark + mod) < 0:
								adj = 2;
								break;
							default:
								// Do nothing
								break;
						}
					}
					days += adj;
					days -= (weeks*2);
				}
				delta = days;

				break;
			case HOUR:
				delta = houDiff;
				break;
			case MINUTE:
				delta = minDiff;
				break;
			case SECOND:
				delta = secDiff;
				break;
			case MILLISECOND:
				delta = msDiff;
				break;
			default:
				// Do nothing
				break;
		}
	}

	// Round for fractional values and DST leaps
	return Math.round(delta); // Number (integer)
};

dojo.provide("dojo.date.supplemental");

dojo.date.getFirstDayOfWeek = function(/*String?*/locale){
// summary: Returns a zero-based index for first day of the week
// description:
//		Returns a zero-based index for first day of the week, as used by the local (Gregorian) calendar.
//		e.g. Sunday (returns 0), or Monday (returns 1)

	// from http://www.unicode.org/cldr/data/common/supplemental/supplementalData.xml:supplementalData/weekData/firstDay
	var firstDay = {/*default is 1=Monday*/
		mv:5,
		ae:6,af:6,bh:6,dj:6,dz:6,eg:6,er:6,et:6,iq:6,ir:6,jo:6,ke:6,kw:6,lb:6,ly:6,ma:6,om:6,qa:6,sa:6,
		sd:6,so:6,tn:6,ye:6,
		as:0,au:0,az:0,bw:0,ca:0,cn:0,fo:0,ge:0,gl:0,gu:0,hk:0,ie:0,il:0,is:0,jm:0,jp:0,kg:0,kr:0,la:0,
		mh:0,mo:0,mp:0,mt:0,nz:0,ph:0,pk:0,sg:0,th:0,tt:0,tw:0,um:0,us:0,uz:0,vi:0,za:0,zw:0,
		et:0,mw:0,ng:0,tj:0,
		gb:0,
		sy:4
	};

	locale = dojo.hostenv.normalizeLocale(locale);
	var country = locale.split("-")[1];
	var dow = firstDay[country];
	return (typeof dow == 'undefined') ? 1 : dow; /*Number*/
};

dojo.date.getWeekend = function(/*String?*/locale){
// summary: Returns a hash containing the start and end days of the weekend
// description:
//		Returns a hash containing the start and end days of the weekend according to local custom using locale,
//		or by default in the user's locale.
//		e.g. {start:6, end:0}

	// from http://www.unicode.org/cldr/data/common/supplemental/supplementalData.xml:supplementalData/weekData/weekend{Start,End}
	var weekendStart = {/*default is 6=Saturday*/
		eg:5,il:5,sy:5,
		'in':0,
		ae:4,bh:4,dz:4,iq:4,jo:4,kw:4,lb:4,ly:4,ma:4,om:4,qa:4,sa:4,sd:4,tn:4,ye:4		
	};

	var weekendEnd = {/*default is 0=Sunday*/
		ae:5,bh:5,dz:5,iq:5,jo:5,kw:5,lb:5,ly:5,ma:5,om:5,qa:5,sa:5,sd:5,tn:5,ye:5,af:5,ir:5,
		eg:6,il:6,sy:6
	};

	locale = dojo.hostenv.normalizeLocale(locale);
	var country = locale.split("-")[1];
	var start = weekendStart[country];
	var end = weekendEnd[country];
	if(typeof start == 'undefined'){start=6;}
	if(typeof end == 'undefined'){end=0;}
	return {start:start, end:end}; /*Object {start,end}*/
};

dojo.date.isWeekend = function(/*Date?*/dateObj, /*String?*/locale){
// summary:
//	Determines if the date falls on a weekend, according to local custom.

	var weekend = dojo.date.getWeekend(locale);
	var day = (dateObj || new Date()).getDay();
	if(weekend.end<weekend.start){
		weekend.end+=7;
		if(day<weekend.start){ day+=7; }
	}
	return day >= weekend.start && day <= weekend.end; // Boolean
};

dojo.provide("dojo.date.format");



dojo.require("dojo.lang.array");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.func");
dojo.require("dojo.string.common");
dojo.require("dojo.i18n.common");

// Load the bundles containing localization information for
// names and formats
dojo.requireLocalization("dojo.i18n.calendar", "gregorian");
dojo.requireLocalization("dojo.i18n.calendar", "gregorianExtras");

//NOTE: Everything in this module assumes Gregorian calendars.
// Other calendars will be implemented in separate modules.

(function(){
dojo.date.format = function(/*Date*/dateObject, /*Object?*/options){
//
// summary:
//		Format a Date object as a String, using locale-specific settings.
//
// description:
//		Create a string from a Date object using a known localized pattern.
//		By default, this method formats both date and time from dateObject.
//		Formatting patterns are chosen appropriate to the locale.  Different
//		formatting lengths may be chosen, with "full" used by default.
//		Custom patterns may be used or registered with translations using
//		the addCustomBundle method.
//		Formatting patterns are implemented using the syntax described at
//		http://www.unicode.org/reports/tr35/tr35-4.html#Date_Format_Patterns
//
// dateObject:
//		the date and/or time to be formatted.  If a time only is formatted,
//		the values in the year, month, and day fields are irrelevant.  The
//		opposite is true when formatting only dates.
//
// options: object {selector: string, formatLength: string, datePattern: string, timePattern: string, locale: string}
//		selector- choice of timeOnly,dateOnly (default: date and time)
//		formatLength- choice of long, short, medium or full (plus any custom additions).  Defaults to 'full'
//		datePattern,timePattern- override pattern with this string
//		am,pm- override strings for am/pm in times
//		locale- override the locale used to determine formatting rules
//

	if(typeof options == "string"){
		dojo.deprecated("dojo.date.format", "To format dates with POSIX-style strings, please use dojo.date.strftime instead", "0.5");
		return dojo.date.strftime(dateObject, options);
	}

	// Format a pattern without literals
	function formatPattern(dateObject, pattern){
		return pattern.replace(/([a-z])\1*/ig, function(match){
			var s;
			var c = match.charAt(0);
			var l = match.length;
			var pad;
			var widthList = ["abbr", "wide", "narrow"];
			switch(c){
				case 'G':
					if(l>3){dojo.unimplemented("Era format not implemented");}
					s = bundle.eras[dateObject.getFullYear() < 0 ? 1 : 0];
					break;
				case 'y':
					s = dateObject.getFullYear();
					switch(l){
						case 1:
							break;
						case 2:
							s = String(s); s = s.substr(s.length - 2);
							break;
						default:
							pad = true;
					}
					break;
				case 'Q':
				case 'q':
					s = Math.ceil((dateObject.getMonth()+1)/3);
					switch(l){
						case 1: case 2:
							pad = true;
							break;
						case 3:
						case 4:
							dojo.unimplemented("Quarter format not implemented");
					}
					break;
				case 'M':
				case 'L':
					var m = dateObject.getMonth();
					var width;
					switch(l){
						case 1: case 2:
							s = m+1; pad = true;
							break;
						case 3: case 4: case 5:
							width = widthList[l-3];
							break;
					}
					if(width){
						var type = (c == "L") ? "standalone" : "format";
						var prop = ["months",type,width].join("-");
						s = bundle[prop][m];
					}
					break;
				case 'w':
					var firstDay = 0;
					s = dojo.date.getWeekOfYear(dateObject, firstDay); pad = true;
					break;
				case 'd':
					s = dateObject.getDate(); pad = true;
					break;
				case 'D':
					s = dojo.date.getDayOfYear(dateObject); pad = true;
					break;
				case 'E':
				case 'e':
				case 'c': // REVIEW: don't see this in the spec?
					var d = dateObject.getDay();
					var width;
					switch(l){
						case 1: case 2:
							if(c == 'e'){
								var first = dojo.date.getFirstDayOfWeek(options.locale);
								d = (d-first+7)%7;
							}
							if(c != 'c'){
								s = d+1; pad = true;
								break;
							}
							// else fallthrough...
						case 3: case 4: case 5:
							width = widthList[l-3];
							break;
					}
					if(width){
						var type = (c == "c") ? "standalone" : "format";
						var prop = ["days",type,width].join("-");
						s = bundle[prop][d];
					}
					break;
				case 'a':
					var timePeriod = (dateObject.getHours() < 12) ? 'am' : 'pm';
					s = bundle[timePeriod];
					break;
				case 'h':
				case 'H':
				case 'K':
				case 'k':
					var h = dateObject.getHours();
					// strange choices in the date format make it impossible to write this succinctly
					switch (c) {
						case 'h': // 1-12
							s = (h % 12) || 12;
							break;
						case 'H': // 0-23
							s = h;
							break;
						case 'K': // 0-11
							s = (h % 12);
							break;
						case 'k': // 1-24
							s = h || 24;
							break;
					}
					pad = true;
					break;
				case 'm':
					s = dateObject.getMinutes(); pad = true;
					break;
				case 's':
					s = dateObject.getSeconds(); pad = true;
					break;
				case 'S':
					s = Math.round(dateObject.getMilliseconds() * Math.pow(10, l-3));
					break;
				case 'v': // FIXME: don't know what this is. seems to be same as z?
				case 'z':
					// We only have one timezone to offer; the one from the browser
					s = dojo.date.getTimezoneName(dateObject);
					if(s){break;}
					l=4;
					// fallthrough... use GMT if tz not available
				case 'Z':
					var offset = dateObject.getTimezoneOffset();
					var tz = [
						(offset<=0 ? "+" : "-"),
						dojo.string.pad(Math.floor(Math.abs(offset)/60), 2),
						dojo.string.pad(Math.abs(offset)% 60, 2)
					];
					if(l==4){
						tz.splice(0, 0, "GMT");
						tz.splice(3, 0, ":");
					}
					s = tz.join("");
					break;
				case 'Y':
				case 'u':
				case 'W':
				case 'F':
				case 'g':
				case 'A':
//					dojo.debug(match+" modifier not yet implemented");
					s = "?";
					break;
				default:
					dojo.raise("dojo.date.format: invalid pattern char: "+pattern);
			}
			if(pad){ s = dojo.string.pad(s, l); }
			return s;
		});
	}

	options = options || {};

	var locale = dojo.hostenv.normalizeLocale(options.locale);
	var formatLength = options.formatLength || 'short';
	var bundle = dojo.date._getGregorianBundle(locale);
	var str = [];
	var sauce = dojo.lang.curry(this, formatPattern, dateObject);
	if(options.selector == "yearOnly"){
		// Special case as this is not yet driven by CLDR data
		var year = dateObject.getFullYear();
		if(locale.match(/^zh|^ja/)){
			year += "\u5E74";
		}
		return year;
	}
	if(options.selector != "timeOnly"){
		var datePattern = options.datePattern || bundle["dateFormat-"+formatLength];
		if(datePattern){str.push(_processPattern(datePattern, sauce));}
	}
	if(options.selector != "dateOnly"){
		var timePattern = options.timePattern || bundle["timeFormat-"+formatLength];
		if(timePattern){str.push(_processPattern(timePattern, sauce));}
	}
	var result = str.join(" "); //TODO: use locale-specific pattern to assemble date + time
	return result; /*String*/
};

dojo.date.parse = function(/*String*/value, /*Object?*/options){
//
// summary:
//		Convert a properly formatted string to a primitive Date object,
//		using locale-specific settings.
//
// description:
//		Create a Date object from a string using a known localized pattern.
//		By default, this method parses looking for both date and time in the string.
//		Formatting patterns are chosen appropriate to the locale.  Different
//		formatting lengths may be chosen, with "full" used by default.
//		Custom patterns may be used or registered with translations using
//		the addCustomBundle method.
//		Formatting patterns are implemented using the syntax described at
//		http://www.unicode.org/reports/tr35/#Date_Format_Patterns
//
// value:
//		A string representation of a date
//
// options: object {selector: string, formatLength: string, datePattern: string, timePattern: string, locale: string, strict: boolean}
//		selector- choice of timeOnly, dateOnly, dateTime (default: dateOnly)
//		formatLength- choice of long, short, medium or full (plus any custom additions).  Defaults to 'full'
//		datePattern,timePattern- override pattern with this string
//		am,pm- override strings for am/pm in times
//		locale- override the locale used to determine formatting rules
//		strict- strict parsing, off by default
//

	options = options || {};
	var locale = dojo.hostenv.normalizeLocale(options.locale);
	var info = dojo.date._getGregorianBundle(locale);
	var formatLength = options.formatLength || 'full';
	if(!options.selector){ options.selector = 'dateOnly'; }
	var datePattern = options.datePattern || info["dateFormat-" + formatLength];
	var timePattern = options.timePattern || info["timeFormat-" + formatLength];

	var pattern;
	if(options.selector == 'dateOnly'){
		pattern = datePattern;
	}
	else if(options.selector == 'timeOnly'){
		pattern = timePattern;
	}else if(options.selector == 'dateTime'){
		pattern = datePattern + ' ' + timePattern; //TODO: use locale-specific pattern to assemble date + time
	}else{
		var msg = "dojo.date.parse: Unknown selector param passed: '" + options.selector + "'.";
		msg += " Defaulting to date pattern.";
		dojo.debug(msg);
		pattern = datePattern;
	}

	var groups = [];
	var dateREString = _processPattern(pattern, dojo.lang.curry(this, _buildDateTimeRE, groups, info, options));
	var dateRE = new RegExp("^" + dateREString + "$");

	var match = dateRE.exec(value);
	if(!match){
		return null;
	}

	var widthList = ['abbr', 'wide', 'narrow'];
	//1972 is a leap year.  We want to avoid Feb 29 rolling over into Mar 1,
	//in the cases where the year is parsed after the month and day.
	var result = new Date(1972, 0);
	var expected = {};
	for(var i=1; i<match.length; i++){
		var grp=groups[i-1];
		var l=grp.length;
		var v=match[i];
		switch(grp.charAt(0)){
			case 'y':
				if(l != 2){
					//interpret year literally, so '5' would be 5 A.D.
					result.setFullYear(v);
					expected.year = v;
				}else{
					if(v<100){
						v = Number(v);
						//choose century to apply, according to a sliding window
						//of 80 years before and 20 years after present year
						var year = '' + new Date().getFullYear();
						var century = year.substring(0, 2) * 100;
						var yearPart = Number(year.substring(2, 4));
						var cutoff = Math.min(yearPart + 20, 99);
						var num = (v < cutoff) ? century + v : century - 100 + v;
						result.setFullYear(num);
						expected.year = num;
					}else{
						//we expected 2 digits and got more...
						if(options.strict){
							return null;
						}
						//interpret literally, so '150' would be 150 A.D.
						//also tolerate '1950', if 'yyyy' input passed to 'yy' format
						result.setFullYear(v);
						expected.year = v;
					}
				}
				break;
			case 'M':
				if (l>2) {
					if(!options.strict){
						//Tolerate abbreviating period in month part
						v = v.replace(/\./g,'');
						//Case-insensitive
						v = v.toLowerCase();
					}
					var months = info['months-format-' + widthList[l-3]].concat();
					for (var j=0; j<months.length; j++){
						if(!options.strict){
							//Case-insensitive
							months[j] = months[j].toLowerCase();
						}
						if(v == months[j]){
							result.setMonth(j);
							expected.month = j;
							break;
						}
					}
					if(j==months.length){
						dojo.debug("dojo.date.parse: Could not parse month name: '" + v + "'.");
						return null;
					}
				}else{
					result.setMonth(v-1);
					expected.month = v-1;
				}
				break;
			case 'E':
			case 'e':
				if(!options.strict){
					//Case-insensitive
					v = v.toLowerCase();
				}
				var days = info['days-format-' + widthList[l-3]].concat();
				for (var j=0; j<days.length; j++){
					if(!options.strict){
						//Case-insensitive
						days[j] = days[j].toLowerCase();
					}
					if(v == days[j]){
						//TODO: not sure what to actually do with this input,
						//in terms of setting something on the Date obj...?
						//without more context, can't affect the actual date
						break;
					}
				}
				if(j==days.length){
					dojo.debug("dojo.date.parse: Could not parse weekday name: '" + v + "'.");
					return null;
				}
				break;	
			case 'd':
				result.setDate(v);
				expected.date = v;
				break;
			case 'a': //am/pm
				var am = options.am || info.am;
				var pm = options.pm || info.pm;
				if(!options.strict){
					v = v.replace(/\./g,'').toLowerCase();
					am = am.replace(/\./g,'').toLowerCase();
					pm = pm.replace(/\./g,'').toLowerCase();
				}
				if(options.strict && v != am && v != pm){
					dojo.debug("dojo.date.parse: Could not parse am/pm part.");
					return null;
				}
				var hours = result.getHours();
				if(v == pm && hours < 12){
					result.setHours(hours + 12); //e.g., 3pm -> 15
				} else if(v == am && hours == 12){
					result.setHours(0); //12am -> 0
				}
				break;
			case 'K': //hour (1-24)
				if(v==24){v=0;}
				// fallthrough...
			case 'h': //hour (1-12)
			case 'H': //hour (0-23)
			case 'k': //hour (0-11)
				//TODO: strict bounds checking, padding
				if(v>23){
					dojo.debug("dojo.date.parse: Illegal hours value");
					return null;
				}

				//in the 12-hour case, adjusting for am/pm requires the 'a' part
				//which for now we will assume always comes after the 'h' part
				result.setHours(v);
				break;
			case 'm': //minutes
				result.setMinutes(v);
				break;
			case 's': //seconds
				result.setSeconds(v);
				break;
			case 'S': //milliseconds
				result.setMilliseconds(v);
				break;
			default:
				dojo.unimplemented("dojo.date.parse: unsupported pattern char=" + grp.charAt(0));
		}
	}

	//validate parse date fields versus input date fields
	if(expected.year && result.getFullYear() != expected.year){
		dojo.debug("Parsed year: '" + result.getFullYear() + "' did not match input year: '" + expected.year + "'.");
		return null;
	}
	if(expected.month && result.getMonth() != expected.month){
		dojo.debug("Parsed month: '" + result.getMonth() + "' did not match input month: '" + expected.month + "'.");
		return null;
	}
	if(expected.date && result.getDate() != expected.date){
		dojo.debug("Parsed day of month: '" + result.getDate() + "' did not match input day of month: '" + expected.date + "'.");
		return null;
	}

	//TODO: implement a getWeekday() method in order to test 
	//validity of input strings containing 'EEE' or 'EEEE'...

	return result; /*Date*/
};

function _processPattern(pattern, applyPattern, applyLiteral, applyAll){
	// Process a pattern with literals in it
	// Break up on single quotes, treat every other one as a literal, except '' which becomes '
	var identity = function(x){return x;};
	applyPattern = applyPattern || identity;
	applyLiteral = applyLiteral || identity;
	applyAll = applyAll || identity;

	//split on single quotes (which escape literals in date format strings) 
	//but preserve escaped single quotes (e.g., o''clock)
	var chunks = pattern.match(/(''|[^'])+/g); 
	var literal = false;

	for(var i=0; i<chunks.length; i++){
		if(!chunks[i]){
			chunks[i]='';
		} else {
			chunks[i]=(literal ? applyLiteral : applyPattern)(chunks[i]);
			literal = !literal;
		}
	}
	return applyAll(chunks.join(''));
}

function _buildDateTimeRE(groups, info, options, pattern){
	return pattern.replace(/([a-z])\1*/ig, function(match){
		// Build a simple regexp without parenthesis, which would ruin the match list
		var s = '';
		var c = match.charAt(0);
		var l = match.length;
		switch(c){
			case 'y':
				s = '\\d' + ((l==2) ? '{2,4}' : '+');
				break;
			case 'M':
				s = (l>2) ? '\\S+' : '\\d{1,2}';
				break;
			case 'd':
				s = '\\d{1,2}';
				break;
		    case 'E':
				s = '\\S+';
				break;
			case 'h': 
			case 'H': 
			case 'K': 
			case 'k':
				s = '\\d{1,2}';
				break;
			case 'm':
			case 's':
				s = '[0-5]\\d';
				break;
			case 'S':
				s = '\\d{1,3}';
				break;
			case 'a':
				var am = options.am || info.am || 'AM';
				var pm = options.pm || info.pm || 'PM';
				if(options.strict){
					s = am + '|' + pm;
				}else{
					for (var i=0; i < am.length; i++){
                        s += '[' + am.charAt(i).toLowerCase() + '|' + am.charAt(i).toUpperCase() + ']';
                    }
                    s += '|';
                    for (var i=0; i < pm.length; i++){
                        s += '[' + pm.charAt(i).toLowerCase() + '|' + pm.charAt(i).toUpperCase() + ']';
                    }
				}
				break;
			default:
				dojo.unimplemented("parse of date format, pattern=" + pattern);
		}

		if(groups){ groups.push(match); }

//FIXME: replace whitespace within final regexp with more flexible whitespace match instead?
		//tolerate whitespace
		return '\\s*(' + s + ')\\s*';
	});
}
})();

//TODO: try to common strftime and format code somehow?

dojo.date.strftime = function(/*Date*/dateObject, /*String*/format, /*String?*/locale){
//
// summary:
//		Formats the date object using the specifications of the POSIX strftime function
//
// description:
//		see <http://www.opengroup.org/onlinepubs/007908799/xsh/strftime.html>

	// zero pad
	var padChar = null;
	function _(s, n){
		return dojo.string.pad(s, n || 2, padChar || "0");
	}

	var info = dojo.date._getGregorianBundle(locale);

	function $(property){
		switch (property){
			case "a": // abbreviated weekday name according to the current locale
				return dojo.date.getDayShortName(dateObject, locale);

			case "A": // full weekday name according to the current locale
				return dojo.date.getDayName(dateObject, locale);

			case "b":
			case "h": // abbreviated month name according to the current locale
				return dojo.date.getMonthShortName(dateObject, locale);
				
			case "B": // full month name according to the current locale
				return dojo.date.getMonthName(dateObject, locale);
				
			case "c": // preferred date and time representation for the current
				      // locale
				return dojo.date.format(dateObject, {locale: locale});

			case "C": // century number (the year divided by 100 and truncated
				      // to an integer, range 00 to 99)
				return _(Math.floor(dateObject.getFullYear()/100));
				
			case "d": // day of the month as a decimal number (range 01 to 31)
				return _(dateObject.getDate());
				
			case "D": // same as %m/%d/%y
				return $("m") + "/" + $("d") + "/" + $("y");
					
			case "e": // day of the month as a decimal number, a single digit is
				      // preceded by a space (range ' 1' to '31')
				if(padChar == null){ padChar = " "; }
				return _(dateObject.getDate());
			
			case "f": // month as a decimal number, a single digit is
							// preceded by a space (range ' 1' to '12')
				if(padChar == null){ padChar = " "; }
				return _(dateObject.getMonth()+1);				
			
			case "g": // like %G, but without the century.
				break;
			
			case "G": // The 4-digit year corresponding to the ISO week number
				      // (see %V).  This has the same format and value as %Y,
				      // except that if the ISO week number belongs to the
				      // previous or next year, that year is used instead.
				dojo.unimplemented("unimplemented modifier 'G'");
				break;
			
			case "F": // same as %Y-%m-%d
				return $("Y") + "-" + $("m") + "-" + $("d");
				
			case "H": // hour as a decimal number using a 24-hour clock (range
				      // 00 to 23)
				return _(dateObject.getHours());
				
			case "I": // hour as a decimal number using a 12-hour clock (range
				      // 01 to 12)
				return _(dateObject.getHours() % 12 || 12);
				
			case "j": // day of the year as a decimal number (range 001 to 366)
				return _(dojo.date.getDayOfYear(dateObject), 3);
				
			case "k": // Hour as a decimal number using a 24-hour clock (range
					  // 0 to 23 (space-padded))
				if (padChar == null) { padChar = " "; }
				return _(dateObject.getHours());

			case "l": // Hour as a decimal number using a 12-hour clock (range
					  // 1 to 12 (space-padded))
				if (padChar == null) { padChar = " "; }
				return _(dateObject.getHours() % 12 || 12);
			
			case "m": // month as a decimal number (range 01 to 12)
				return _(dateObject.getMonth() + 1);
				
			case "M": // minute as a decimal number
				return _(dateObject.getMinutes());
			
			case "n":
				return "\n";

			case "p": // either `am' or `pm' according to the given time value,
				      // or the corresponding strings for the current locale
				return info[dateObject.getHours() < 12 ? "am" : "pm"];
				
			case "r": // time in a.m. and p.m. notation
				return $("I") + ":" + $("M") + ":" + $("S") + " " + $("p");
				
			case "R": // time in 24 hour notation
				return $("H") + ":" + $("M");
				
			case "S": // second as a decimal number
				return _(dateObject.getSeconds());

			case "t":
				return "\t";

			case "T": // current time, equal to %H:%M:%S
				return $("H") + ":" + $("M") + ":" + $("S");
				
			case "u": // weekday as a decimal number [1,7], with 1 representing
				      // Monday
				return String(dateObject.getDay() || 7);
				
			case "U": // week number of the current year as a decimal number,
				      // starting with the first Sunday as the first day of the
				      // first week
				return _(dojo.date.getWeekOfYear(dateObject));

			case "V": // week number of the year (Monday as the first day of the
				      // week) as a decimal number [01,53]. If the week containing
				      // 1 January has four or more days in the new year, then it 
				      // is considered week 1. Otherwise, it is the last week of 
				      // the previous year, and the next week is week 1.
				return _(dojo.date.getIsoWeekOfYear(dateObject));
				
			case "W": // week number of the current year as a decimal number,
				      // starting with the first Monday as the first day of the
				      // first week
				return _(dojo.date.getWeekOfYear(dateObject, 1));
				
			case "w": // day of the week as a decimal, Sunday being 0
				return String(dateObject.getDay());

			case "x": // preferred date representation for the current locale
				      // without the time
				return dojo.date.format(dateObject, {selector:'dateOnly', locale:locale});

			case "X": // preferred time representation for the current locale
				      // without the date
				return dojo.date.format(dateObject, {selector:'timeOnly', locale:locale});

			case "y": // year as a decimal number without a century (range 00 to
				      // 99)
				return _(dateObject.getFullYear()%100);
				
			case "Y": // year as a decimal number including the century
				return String(dateObject.getFullYear());
			
			case "z": // time zone or name or abbreviation
				var timezoneOffset = dateObject.getTimezoneOffset();
				return (timezoneOffset > 0 ? "-" : "+") + 
					_(Math.floor(Math.abs(timezoneOffset)/60)) + ":" +
					_(Math.abs(timezoneOffset)%60);

			case "Z": // time zone or name or abbreviation
				return dojo.date.getTimezoneName(dateObject);
			
			case "%":
				return "%";
		}
	}

	// parse the formatting string and construct the resulting string
	var string = "";
	var i = 0;
	var index = 0;
	var switchCase = null;
	while ((index = format.indexOf("%", i)) != -1){
		string += format.substring(i, index++);
		
		// inspect modifier flag
		switch (format.charAt(index++)) {
			case "_": // Pad a numeric result string with spaces.
				padChar = " "; break;
			case "-": // Do not pad a numeric result string.
				padChar = ""; break;
			case "0": // Pad a numeric result string with zeros.
				padChar = "0"; break;
			case "^": // Convert characters in result string to uppercase.
				switchCase = "upper"; break;
			case "*": // Convert characters in result string to lowercase
				switchCase = "lower"; break;
			case "#": // Swap the case of the result string.
				switchCase = "swap"; break;
			default: // no modifier flag so decrement the index
				padChar = null; index--; break;
		}

		// toggle case if a flag is set
		var property = $(format.charAt(index++));
		switch (switchCase){
			case "upper":
				property = property.toUpperCase();
				break;
			case "lower":
				property = property.toLowerCase();
				break;
			case "swap": // Upper to lower, and versey-vicea
				var compareString = property.toLowerCase();
				var swapString = '';
				var j = 0;
				var ch = '';
				while (j < property.length){
					ch = property.charAt(j);
					swapString += (ch == compareString.charAt(j)) ?
						ch.toUpperCase() : ch.toLowerCase();
					j++;
				}
				property = swapString;
				break;
			default:
				break;
		}
		switchCase = null;
		
		string += property;
		i = index;
	}
	string += format.substring(i);
	
	return string; // String
};

(function(){
var _customFormats = [];
dojo.date.addCustomFormats = function(/*String*/packageName, /*String*/bundleName){
//
// summary:
//		Add a reference to a bundle containing localized custom formats to be
//		used by date/time formatting and parsing routines.
//
// description:
//		The user may add custom localized formats where the bundle has properties following the
//		same naming convention used by dojo for the CLDR data: dateFormat-xxxx / timeFormat-xxxx
//		The pattern string should match the format used by the CLDR.
//		See dojo.date.format for details.
//		The resources must be loaded by dojo.requireLocalization() prior to use

	_customFormats.push({pkg:packageName,name:bundleName});
};

dojo.date._getGregorianBundle = function(/*String*/locale){
	var gregorian = {};
	dojo.lang.forEach(_customFormats, function(desc){
		var bundle = dojo.i18n.getLocalization(desc.pkg, desc.name, locale);
		gregorian = dojo.lang.mixin(gregorian, bundle);
	}, this);
	return gregorian; /*Object*/
};
})();

dojo.date.addCustomFormats("dojo.i18n.calendar","gregorian");
dojo.date.addCustomFormats("dojo.i18n.calendar","gregorianExtras");

dojo.date.getNames = function(/*String*/item, /*String*/type, /*String?*/use, /*String?*/locale){
//
// summary:
//		Used to get localized strings for day or month names.
//
// item: 'months' || 'days'
// type: 'wide' || 'narrow' || 'abbr' (e.g. "Monday", "Mon", or "M" respectively, in English)
// use: 'standAlone' || 'format' (default)
// locale: override locale used to find the names

	var label;
	var lookup = dojo.date._getGregorianBundle(locale);
	var props = [item, use, type];
	if(use == 'standAlone'){
		label = lookup[props.join('-')];
	}
	props[1] = 'format';

	// return by copy so changes won't be made accidentally to the in-memory model
	return (label || lookup[props.join('-')]).concat(); /*Array*/
};

// Convenience methods

dojo.date.getDayName = function(/*Date*/dateObject, /*String?*/locale){
// summary: gets the full localized day of the week corresponding to the date object
	return dojo.date.getNames('days', 'wide', 'format', locale)[dateObject.getDay()]; /*String*/
};

dojo.date.getDayShortName = function(/*Date*/dateObject, /*String?*/locale){
// summary: gets the abbreviated localized day of the week corresponding to the date object
	return dojo.date.getNames('days', 'abbr', 'format', locale)[dateObject.getDay()]; /*String*/
};

dojo.date.getMonthName = function(/*Date*/dateObject, /*String?*/locale){
// summary: gets the full localized month name corresponding to the date object
	return dojo.date.getNames('months', 'wide', 'format', locale)[dateObject.getMonth()]; /*String*/
};

dojo.date.getMonthShortName = function(/*Date*/dateObject, /*String?*/locale){
// summary: gets the abbreviated localized month name corresponding to the date object
	return dojo.date.getNames('months', 'abbr', 'format', locale)[dateObject.getMonth()]; /*String*/
};

//FIXME: not localized
dojo.date.toRelativeString = function(/*Date*/dateObject){
// summary:
//	Returns an description in English of the date relative to the current date.  Note: this is not localized yet.  English only.
//
// description: Example returns:
//	 - "1 minute ago"
//	 - "4 minutes ago"
//	 - "Yesterday"
//	 - "2 days ago"

	var now = new Date();
	var diff = (now - dateObject) / 1000;
	var end = " ago";
	var future = false;
	if(diff < 0){
		future = true;
		end = " from now";
		diff = -diff;
	}

	if(diff < 60){
		diff = Math.round(diff);
		return diff + " second" + (diff == 1 ? "" : "s") + end;
	}
	if(diff < 60*60){
		diff = Math.round(diff/60);
		return diff + " minute" + (diff == 1 ? "" : "s") + end;
	}
	if(diff < 60*60*24){
		diff = Math.round(diff/3600);
		return diff + " hour" + (diff == 1 ? "" : "s") + end;
	}
	if(diff < 60*60*24*7){
		diff = Math.round(diff/(3600*24));
		if(diff == 1){
			return future ? "Tomorrow" : "Yesterday";
		}else{
			return diff + " days" + end;
		}
	}
	return dojo.date.format(dateObject); // String
};

//FIXME: SQL methods can probably be moved to a different module without i18n deps

dojo.date.toSql = function(/*Date*/dateObject, /*Boolean?*/noTime){
// summary:
//	Convert a Date to a SQL string
// noTime: whether to ignore the time portion of the Date.  Defaults to false.

	return dojo.date.strftime(dateObject, "%F" + !noTime ? " %T" : ""); // String
};

dojo.date.fromSql = function(/*String*/sqlDate){
// summary:
//	Convert a SQL date string to a JavaScript Date object

	var parts = sqlDate.split(/[\- :]/g);
	while(parts.length < 6){
		parts.push(0);
	}
	return new Date(parts[0], (parseInt(parts[1],10)-1), parts[2], parts[3], parts[4], parts[5]); // Date
};

dojo.provide("dojo.date.serialize");

dojo.require("dojo.string.common");

/* ISO 8601 Functions
 *********************/

dojo.date.setIso8601 = function(/*Date*/dateObject, /*String*/formattedString){
	// summary: sets a Date object based on an ISO 8601 formatted string (uses date and time)
	var comps = (formattedString.indexOf("T") == -1) ? formattedString.split(" ") : formattedString.split("T");
	dateObject = dojo.date.setIso8601Date(dateObject, comps[0]);
	if(comps.length == 2){ dateObject = dojo.date.setIso8601Time(dateObject, comps[1]); }
	return dateObject; /* Date or null */
};

dojo.date.fromIso8601 = function(/*String*/formattedString){
	// summary: returns a Date object based on an ISO 8601 formatted string (uses date and time)
	return dojo.date.setIso8601(new Date(0, 0), formattedString);
};

dojo.date.setIso8601Date = function(/*String*/dateObject, /*String*/formattedString){
	// summary: sets a Date object based on an ISO 8601 formatted string (date only)
	var regexp = "^([0-9]{4})((-?([0-9]{2})(-?([0-9]{2}))?)|" +
			"(-?([0-9]{3}))|(-?W([0-9]{2})(-?([1-7]))?))?$";
	var d = formattedString.match(new RegExp(regexp));
	if(!d){
		dojo.debug("invalid date string: " + formattedString);
		return null; // null
	}
	var year = d[1];
	var month = d[4];
	var date = d[6];
	var dayofyear = d[8];
	var week = d[10];
	var dayofweek = d[12] ? d[12] : 1;

	dateObject.setFullYear(year);

	if(dayofyear){
		dateObject.setMonth(0);
		dateObject.setDate(Number(dayofyear));
	}
	else if(week){
		dateObject.setMonth(0);
		dateObject.setDate(1);
		var gd = dateObject.getDay();
		var day =  gd ? gd : 7;
		var offset = Number(dayofweek) + (7 * Number(week));
		
		if(day <= 4){ dateObject.setDate(offset + 1 - day); }
		else{ dateObject.setDate(offset + 8 - day); }
	} else{
		if(month){
			dateObject.setDate(1);
			dateObject.setMonth(month - 1); 
		}
		if(date){ dateObject.setDate(date); }
	}
	
	return dateObject; // Date
};

dojo.date.fromIso8601Date = function(/*String*/formattedString){
	// summary: returns a Date object based on an ISO 8601 formatted string (date only)
	return dojo.date.setIso8601Date(new Date(0, 0), formattedString);
};

dojo.date.setIso8601Time = function(/*Date*/dateObject, /*String*/formattedString){
	// summary: sets a Date object based on an ISO 8601 formatted string (time only)

	// first strip timezone info from the end
	var timezone = "Z|(([-+])([0-9]{2})(:?([0-9]{2}))?)$";
	var d = formattedString.match(new RegExp(timezone));

	var offset = 0; // local time if no tz info
	if(d){
		if(d[0] != 'Z'){
			offset = (Number(d[3]) * 60) + Number(d[5]);
			offset *= ((d[2] == '-') ? 1 : -1);
		}
		offset -= dateObject.getTimezoneOffset();
		formattedString = formattedString.substr(0, formattedString.length - d[0].length);
	}

	// then work out the time
	var regexp = "^([0-9]{2})(:?([0-9]{2})(:?([0-9]{2})(\.([0-9]+))?)?)?$";
	d = formattedString.match(new RegExp(regexp));
	if(!d){
		dojo.debug("invalid time string: " + formattedString);
		return null; // null
	}
	var hours = d[1];
	var mins = Number((d[3]) ? d[3] : 0);
	var secs = (d[5]) ? d[5] : 0;
	var ms = d[7] ? (Number("0." + d[7]) * 1000) : 0;

	dateObject.setHours(hours);
	dateObject.setMinutes(mins);
	dateObject.setSeconds(secs);
	dateObject.setMilliseconds(ms);

	if(offset !== 0){
		dateObject.setTime(dateObject.getTime() + offset * 60000);
	}	
	return dateObject; // Date
};

dojo.date.fromIso8601Time = function(/*String*/formattedString){
	// summary: returns a Date object based on an ISO 8601 formatted string (date only)
	return dojo.date.setIso8601Time(new Date(0, 0), formattedString);
};


/* RFC-3339 Date Functions
 *************************/

dojo.date.toRfc3339 = function(/*Date?*/dateObject, /*String?*/selector){
//	summary:
//		Format a JavaScript Date object as a string according to RFC 3339
//
//	dateObject:
//		A JavaScript date, or the current date and time, by default
//
//	selector:
//		"dateOnly" or "timeOnly" to format selected portions of the Date object.
//		Date and time will be formatted by default.

//FIXME: tolerate Number, string input?
	if(!dateObject){
		dateObject = new Date();
	}

	var _ = dojo.string.pad;
	var formattedDate = [];
	if(selector != "timeOnly"){
		var date = [_(dateObject.getFullYear(),4), _(dateObject.getMonth()+1,2), _(dateObject.getDate(),2)].join('-');
		formattedDate.push(date);
	}
	if(selector != "dateOnly"){
		var time = [_(dateObject.getHours(),2), _(dateObject.getMinutes(),2), _(dateObject.getSeconds(),2)].join(':');
		var timezoneOffset = dateObject.getTimezoneOffset();
		time += (timezoneOffset > 0 ? "-" : "+") + 
					_(Math.floor(Math.abs(timezoneOffset)/60),2) + ":" +
					_(Math.abs(timezoneOffset)%60,2);
		formattedDate.push(time);
	}
	return formattedDate.join('T'); // String
};

dojo.date.fromRfc3339 = function(/*String*/rfcDate){
//	summary:
//		Create a JavaScript Date object from a string formatted according to RFC 3339
//
//	rfcDate:
//		A string such as 2005-06-30T08:05:00-07:00
//		"any" is also supported in place of a time.

	// backwards compatible support for use of "any" instead of just not 
	// including the time
	if(rfcDate.indexOf("Tany")!=-1){
		rfcDate = rfcDate.replace("Tany","");
	}
	var dateObject = new Date();
	return dojo.date.setIso8601(dateObject, rfcDate); // Date or null
};

dojo.provide("dojo.validate.datetime");


/**
  Validates a time value in any International format.
  The value can be validated against one format or one of multiple formats.

  Format
  h        12 hour, no zero padding.
  hh       12 hour, has leading zero.
  H        24 hour, no zero padding.
  HH       24 hour, has leading zero.
  m        minutes, no zero padding.
  mm       minutes, has leading zero.
  s        seconds, no zero padding.
  ss       seconds, has leading zero.
  All other characters must appear literally in the expression.

  Example
    "h:m:s t"  ->   2:5:33 PM
    "HH:mm:ss" ->  14:05:33

  @param value  A string.
  @param flags  An object.
    flags.format  A string or an array of strings.  Default is "h:mm:ss t".
    flags.amSymbol  The symbol used for AM.  Default is "AM".
    flags.pmSymbol  The symbol used for PM.  Default is "PM".
  @return  true or false
*/
dojo.validate.isValidTime = function(value, flags) {
	dojo.deprecated("dojo.validate.datetime", "use dojo.date.parse instead", "0.5");
	var re = new RegExp("^" + dojo.regexp.time(flags) + "$", "i");
	return re.test(value);
}

/**
  Validates 12-hour time format.
  Zero-padding is not allowed for hours, required for minutes and seconds.
  Seconds are optional.

  @param value  A string.
  @return  true or false
*/
dojo.validate.is12HourTime = function(value) {
	dojo.deprecated("dojo.validate.datetime", "use dojo.date.parse instead", "0.5");
	return dojo.validate.isValidTime(value, {format: ["h:mm:ss t", "h:mm t"]});
}

/**
  Validates 24-hour military time format.
  Zero-padding is required for hours, minutes, and seconds.
  Seconds are optional.

  @param value  A string.
  @return  true or false
*/
dojo.validate.is24HourTime = function(value) {
	dojo.deprecated("dojo.validate.datetime", "use dojo.date.parse instead", "0.5");
	return dojo.validate.isValidTime(value, {format: ["HH:mm:ss", "HH:mm"]} );
}

/**
  Returns true if the date conforms to the format given and is a valid date. Otherwise returns false.

  @param dateValue  A string for the date.
  @param format  A string, default is  "MM/DD/YYYY".
  @return  true or false

  Accepts any type of format, including ISO8601.
  All characters in the format string are treated literally except the following tokens:

  YYYY - matches a 4 digit year
  M - matches a non zero-padded month
  MM - matches a zero-padded month
  D -  matches a non zero-padded date
  DD -  matches a zero-padded date
  DDD -  matches an ordinal date, 001-365, and 366 on leapyear
  ww - matches week of year, 01-53
  d - matches day of week, 1-7

  Examples: These are all today's date.

  Date          Format
  2005-W42-3    YYYY-Www-d
  2005-292      YYYY-DDD
  20051019      YYYYMMDD
  10/19/2005    M/D/YYYY
  19.10.2005    D.M.YYYY
*/
dojo.validate.isValidDate = function(dateValue, format) {
	dojo.deprecated("dojo.validate.datetime", "use dojo.date.parse instead", "0.5");
	// Default is the American format
	if (typeof format == "object" && typeof format.format == "string"){ format = format.format; }
	if (typeof format != "string") { format = "MM/DD/YYYY"; }

	// Create a literal regular expression based on format
	var reLiteral = format.replace(/([$^.*+?=!:|\/\\\(\)\[\]\{\}])/g, "\\$1");

	// Convert all the tokens to RE elements
	reLiteral = reLiteral.replace( "YYYY", "([0-9]{4})" );
	reLiteral = reLiteral.replace( "MM", "(0[1-9]|10|11|12)" );
	reLiteral = reLiteral.replace( "M", "([1-9]|10|11|12)" );
	reLiteral = reLiteral.replace( "DDD", "(00[1-9]|0[1-9][0-9]|[12][0-9][0-9]|3[0-5][0-9]|36[0-6])" );
	reLiteral = reLiteral.replace( "DD", "(0[1-9]|[12][0-9]|30|31)" );
	reLiteral = reLiteral.replace( "D", "([1-9]|[12][0-9]|30|31)" );
	reLiteral = reLiteral.replace( "ww", "(0[1-9]|[1-4][0-9]|5[0-3])" );
	reLiteral = reLiteral.replace( "d", "([1-7])" );

	// Anchor pattern to begining and end of string
	reLiteral = "^" + reLiteral + "$";

	// Dynamic RE that parses the original format given
	var re = new RegExp(reLiteral);
	
	// Test if date is in a valid format
	if (!re.test(dateValue))  return false;

	// Parse date to get elements and check if date is valid
	// Assume valid values for date elements not given.
	var year = 0, month = 1, date = 1, dayofyear = 1, week = 1, day = 1;

	// Capture tokens
	var tokens = format.match( /(YYYY|MM|M|DDD|DD|D|ww|d)/g );

	// Capture date values
	var values = re.exec(dateValue);

	// Match up tokens with date values
	for (var i = 0; i < tokens.length; i++) {
		switch (tokens[i]) {
		case "YYYY":
			year = Number(values[i+1]); break;
		case "M":
		case "MM":
			month = Number(values[i+1]); break;
		case "D":
		case "DD":
			date = Number(values[i+1]); break;
		case "DDD":
			dayofyear = Number(values[i+1]); break;
		case "ww":
			week = Number(values[i+1]); break;
		case "d":
			day = Number(values[i+1]); break;
		}
	}

	// Leap years are divisible by 4, but not by 100, unless by 400
	var leapyear = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));

	// 31st of a month with 30 days
	if (date == 31 && (month == 4 || month == 6 || month == 9 || month == 11)) return false; 

	// February 30th or 31st
	if (date >= 30 && month == 2) return false; 

	// February 29th outside a leap year
	if (date == 29 && month == 2 && !leapyear) return false; 
	if (dayofyear == 366 && !leapyear)  return false;

	return true;
}

dojo.provide("dojo.validate.web");


dojo.validate.isIpAddress = function(/*String*/value, /*Object?*/flags) {
	// summary: Validates an IP address
	//
	// description:
	//  Supports 5 formats for IPv4: dotted decimal, dotted hex, dotted octal, decimal and hexadecimal.
	//  Supports 2 formats for Ipv6.
	//
	// value  A string.
	// flags  An object.  All flags are boolean with default = true.
	//    flags.allowDottedDecimal  Example, 207.142.131.235.  No zero padding.
	//    flags.allowDottedHex  Example, 0x18.0x11.0x9b.0x28.  Case insensitive.  Zero padding allowed.
	//    flags.allowDottedOctal  Example, 0030.0021.0233.0050.  Zero padding allowed.
	//    flags.allowDecimal  Example, 3482223595.  A decimal number between 0-4294967295.
	//    flags.allowHex  Example, 0xCF8E83EB.  Hexadecimal number between 0x0-0xFFFFFFFF.
	//      Case insensitive.  Zero padding allowed.
	//    flags.allowIPv6   IPv6 address written as eight groups of four hexadecimal digits.
	//    flags.allowHybrid   IPv6 address written as six groups of four hexadecimal digits
	//      followed by the usual 4 dotted decimal digit notation of IPv4. x:x:x:x:x:x:d.d.d.d

	var re = new RegExp("^" + dojo.regexp.ipAddress(flags) + "$", "i");
	return re.test(value); // Boolean
}


dojo.validate.isUrl = function(/*String*/value, /*Object?*/flags) {
	// summary: Checks if a string could be a valid URL
	// value: A string
	// flags: An object
	//    flags.scheme  Can be true, false, or [true, false]. 
	//      This means: required, not allowed, or either.
	//    flags in regexp.host can be applied.
	//    flags in regexp.ipAddress can be applied.
	//    flags in regexp.tld can be applied.

	var re = new RegExp("^" + dojo.regexp.url(flags) + "$", "i");
	return re.test(value); // Boolean
}

dojo.validate.isEmailAddress = function(/*String*/value, /*Object?*/flags) {
	// summary: Checks if a string could be a valid email address
	//
	// value: A string
	// flags: An object
	//    flags.allowCruft  Allow address like <mailto:foo@yahoo.com>.  Default is false.
	//    flags in regexp.host can be applied.
	//    flags in regexp.ipAddress can be applied.
	//    flags in regexp.tld can be applied.

	var re = new RegExp("^" + dojo.regexp.emailAddress(flags) + "$", "i");
	return re.test(value); // Boolean
}

dojo.validate.isEmailAddressList = function(/*String*/value, /*Object?*/flags) {
	// summary: Checks if a string could be a valid email address list.
	//
	// value  A string.
	// flags  An object.
	//    flags.listSeparator  The character used to separate email addresses.  Default is ";", ",", "\n" or " ".
	//    flags in regexp.emailAddress can be applied.
	//    flags in regexp.host can be applied.
	//    flags in regexp.ipAddress can be applied.
	//    flags in regexp.tld can be applied.

	var re = new RegExp("^" + dojo.regexp.emailAddressList(flags) + "$", "i");
	return re.test(value); // Boolean
}

dojo.validate.getEmailAddressList = function(/*String*/value, /*Object?*/flags) {
	// summary: Check if value is an email address list. If an empty list
	//  is returned, the value didn't pass the test or it was empty.
	//
	// value: A string
	// flags: An object (same as dojo.validate.isEmailAddressList)

	if(!flags) { flags = {}; }
	if(!flags.listSeparator) { flags.listSeparator = "\\s;,"; }

	if ( dojo.validate.isEmailAddressList(value, flags) ) {
		return value.split(new RegExp("\\s*[" + flags.listSeparator + "]\\s*")); // Array
	}
	return []; // Array
}

dojo.provide('dojo.validate.creditCard');

dojo.require("dojo.lang.common");


/*
	Validates Credit Cards using account number rules in conjunction with the Luhn algorigthm
	
 */

dojo.validate.isValidCreditCard = function(/*String|Int*/value, /*String*/ccType){
	//Summary:
	//  checks if type matches the # scheme, and if Luhn checksum is accurate (unless its an Enroute card, the checkSum is skipped)
	
	//Value: Boolean
	if(value&&ccType&&((ccType.toLowerCase()=='er'||dojo.validate.isValidLuhn(value))&&(dojo.validate.isValidCreditCardNumber(value,ccType.toLowerCase())))){
			return true; //Boolean
	}
	return false; //Boolean
}
dojo.validate.isValidCreditCardNumber = function(/*String|Int*/value,/*String?*/ccType) {
	//Summary:
	//  checks if the # matches the pattern for that card or any card types if none is specified
	//  value == CC #, white spaces and dashes are ignored
	//  ccType is of the values in cardinfo -- if Omitted it it returns a | delimited string of matching card types, or false if no matches found
	
	//Value: Boolean
	
	if(typeof value!='string'){
		value = String(value);
	}
	value = value.replace(/[- ]/g,''); //ignore dashes and whitespaces
	/* 	FIXME: not sure on all the abbreviations for credit cards,below is what each stands for atleast to my knowledge
		mc: Mastercard
		ec: Eurocard
		vi: Visa
		ax: American Express
		dc: Diners Club
		bl: Carte Blanch
		di: Discover
		jcb: JCB
		er: Enroute
	 */
	var results=[];
	var cardinfo = {
		'mc':'5[1-5][0-9]{14}','ec':'5[1-5][0-9]{14}','vi':'4([0-9]{12}|[0-9]{15})',
		'ax':'3[47][0-9]{13}', 'dc':'3(0[0-5][0-9]{11}|[68][0-9]{12})',
		'bl':'3(0[0-5][0-9]{11}|[68][0-9]{12})','di':'6011[0-9]{12}',
		'jcb':'(3[0-9]{15}|(2131|1800)[0-9]{11})','er':'2(014|149)[0-9]{11}'
	};
	if(ccType&&dojo.lang.has(cardinfo,ccType.toLowerCase())){
		return Boolean(value.match(cardinfo[ccType.toLowerCase()])); // boolean
	}else{
		for(var p in cardinfo){
			if(value.match('^'+cardinfo[p]+'$')!=null){
				results.push(p);
			}
		}
		return (results.length)?results.join('|'):false; // string | boolean
	}	
}

dojo.validate.isValidCvv = function(/*String|Int*/value, /*String*/ccType) {
	//Summary:
	//  returns true if the security code (CCV) matches the correct format for supplied ccType
	
	//Value: Boolean
	
	if(typeof value!='string'){
		value=String(value);
	}
	var format;
	switch (ccType.toLowerCase()){
		case 'mc':
		case 'ec':
		case 'vi':
		case 'di':
			format = '###';
			break;
		case 'ax':
			format = '####';
			break;
		default:
			return false; //Boolean
	}
	var flags = {format:format};
	//FIXME? Why does isNumberFormat take an object for flags when its only parameter is either a string or an array inside the object?
	if ((value.length == format.length)&&(dojo.validate.isNumberFormat(value, flags))){
		return true; //Boolean
	}
	return false; //Boolean
}

dojo.provide("dojo.validate.us");


dojo.validate.us.isCurrency = function(/*String*/value, /*Object?*/flags){
	// summary: Validates U.S. currency
	// value: the representation to check
	// flags: flags in validate.isCurrency can be applied.
	return dojo.validate.isCurrency(value, flags); // Boolean
}


dojo.validate.us.isState = function(/*String*/value, /*Object?*/flags){
	// summary: Validates US state and territory abbreviations.
	//
	// value: A two character string
	// flags: An object
	//    flags.allowTerritories  Allow Guam, Puerto Rico, etc.  Default is true.
	//    flags.allowMilitary  Allow military 'states', e.g. Armed Forces Europe (AE).  Default is true.

	var re = new RegExp("^" + dojo.regexp.us.state(flags) + "$", "i");
	return re.test(value); // Boolean
}

dojo.validate.us.isPhoneNumber = function(/*String*/value){
	// summary: Validates 10 US digit phone number for several common formats
	// value: The telephone number string

	var flags = {
		format: [
			"###-###-####",
			"(###) ###-####",
			"(###) ### ####",
			"###.###.####",
			"###/###-####",
			"### ### ####",
			"###-###-#### x#???",
			"(###) ###-#### x#???",
			"(###) ### #### x#???",
			"###.###.#### x#???",
			"###/###-#### x#???",
			"### ### #### x#???",
			"##########"
		]
	};

	return dojo.validate.isNumberFormat(value, flags); // Boolean
}

dojo.validate.us.isSocialSecurityNumber = function(/*String*/value){
// summary: Validates social security number
	var flags = {
		format: [
			"###-##-####",
			"### ## ####",
			"#########"
		]
	};

	return dojo.validate.isNumberFormat(value, flags); // Boolean
}

dojo.validate.us.isZipCode = function(/*String*/value){
// summary: Validates U.S. zip-code
	var flags = {
		format: [
			"#####-####",
			"##### ####",
			"#########",
			"#####"
		]
	};

	return dojo.validate.isNumberFormat(value, flags); // Boolean
}

