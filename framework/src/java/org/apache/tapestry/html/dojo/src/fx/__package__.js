/*
	Copyright (c) 2004-2005, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.hostenv.conditionalLoadModule({
	browser: ["dojo.fx.html"],
	dashboard: ["dojo.fx.html"]
});
dojo.hostenv.moduleLoaded("dojo.fx.*");
