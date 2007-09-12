dojo.provide("dojo.uri.Uri");

dojo.uri = new function() {
	
	var authorityPattern = new RegExp("^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$");
	var uriPattern = new RegExp("(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$");
	var cssPattern = new RegExp("/(\\w+.css)");
	
	this.dojoUri = function (/*dojo.uri.Uri||String*/uri) {
		// summary: returns a Uri object resolved relative to the dojo root
		return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri(), uri);
	}

	this.moduleUri = function(/*String*/module, /*dojo.uri.Uri||String*/uri){
		// summary: returns a Uri object relative to a module
		// description: Examples: dojo.uri.moduleUri("dojo","Editor"), or dojo.uri.moduleUri("acme","someWidget")
		var loc = dojo.hostenv.getModuleSymbols(module).join('/');
		if(!loc){
			return null;
		}
		if(loc.lastIndexOf("/") != loc.length-1){
			loc += "/";
		}
		
		//If the path is an absolute path (starts with a / or is on another domain/xdomain)
		//then don't add the baseScriptUri.
		var colonIndex = loc.indexOf(":");
		var slashIndex = loc.indexOf("/");
		if(loc.charAt(0) != "/" && (colonIndex == -1 || colonIndex > slashIndex)){
			loc = dojo.hostenv.getBaseScriptUri() + loc;
		}

		return new dojo.uri.Uri(loc,uri);
	}

	this.Uri = function (/*dojo.uri.Uri||String...*/) {
		// summary: Constructor to create an object representing a URI.
		// description: 
		//  Each argument is evaluated in order relative to the next until
		//  a canonical uri is produced. To get an absolute Uri relative
		//  to the current document use
		//      new dojo.uri.Uri(document.baseURI, uri)

		// TODO: support for IPv6, see RFC 2732

		// resolve uri components relative to each other
		var uri = arguments[0];
		if (uri && arguments.length > 1) {
			var cssMatch = cssPattern.exec(uri);
			if (cssMatch){
				uri = uri.toString().replace(cssMatch[1], "");
			}
		}
	
		for (var i = 1; i < arguments.length; i++) {
			if(!arguments[i]) { continue; }

			// Safari doesn't support this.constructor so we have to be explicit
			var relobj = new dojo.uri.Uri(arguments[i].toString());
			var uriobj = new dojo.uri.Uri(uri.toString());

			if ((relobj.path=="")&&(relobj.scheme==null)&&(relobj.authority==null)&&(relobj.query==null)) {
				if (relobj.fragment != null) { uriobj.fragment = relobj.fragment; }
				relobj = uriobj;
			}
			
			if (relobj.scheme != null && relobj.authority != null)
				uri = "";
			if (relobj.scheme != null) { uri += relobj.scheme + ":"; }
			if (relobj.authority != null) { uri += "//" + relobj.authority; }
			uri += relobj.path;
			if (relobj.query != null) { uri += "?" + relobj.query; }
			if (relobj.fragment != null) { uri += "#" + relobj.fragment; }
		}
		
		this.uri = uri.toString();

		// break the uri into its main components
	    var r = this.uri.match(uriPattern);
	    
		this.scheme = r[2] || (r[1] ? "" : null);
		this.authority = r[4] || (r[3] ? "" : null);
		this.path = r[5]; // can never be undefined
		this.query = r[7] || (r[6] ? "" : null);
		this.fragment  = r[9] || (r[8] ? "" : null);
		
		if (this.authority != null) {
			// server based naming authority
			r = this.authority.match(authorityPattern);
			
			this.user = r[3] || null;
			this.password = r[4] || null;
			this.host = r[5];
			this.port = r[7] || null;
		}
	
		this.toString = function(){ return this.uri; }
	}
};
