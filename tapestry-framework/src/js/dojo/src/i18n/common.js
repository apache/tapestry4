/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.i18n.common");

dojo.i18n.getLocalization = function(packageName, bundleName, locale /*optional*/){
//	summary:
//		Returns an Object containing the localization for a given resource bundle
//		in a package, matching the specified locale.
//
//	description:
//		Returns a hash containing name/value pairs.  Throws an exception if the bundle is not found.
//		Bundle must have already been loaded by dojo.requireLocalization() or by a build optimization step.
//
//	packageName: package which is associated with this resource
//	bundleName: the base filename of the resource bundle (without the ".js" suffix)
//	locale: the variant to load (optional).  By default, the locale defined by the
//		host environment: dojo.locale

	dojo.hostenv.preloadLocalizations();
	locale = dojo.hostenv.normalizeLocale(locale);

	// look for nearest locale match
	var elements = locale.split('-');
	var module = [packageName,"nls",bundleName].join('.');
	var bundle = dojo.hostenv.findModule(module, true);

	for(var i = elements.length; i > 0; i--){
		var loc = elements.slice(0, i).join('_');
		if(bundle[loc]){
			return bundle[loc]; // Object
		}
	}
	if(bundle.ROOT){
		return bundle.ROOT; // Object
	}

	dojo.raise("Bundle not found: " + bundleName + " in " + packageName+" , locale=" + locale);
};

dojo.i18n.isLTR = function(/*String?*/locale){
//	summary:
//		Is the language read left-to-right?  Most exceptions are for middle eastern languages.
//
//	locale: a string representing the locale.  By default, the locale defined by the
//		host environment: dojo.locale

	var lang = dojo.hostenv.normalizeLocale(locale).split('-')[0];
	var RTL = {ar:true,fa:true,he:true,ur:true,yi:true};
	return !RTL[lang]; // Boolean
};
