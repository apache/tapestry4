dojo.provide("tapestry.html");

/**
 * package: tapestry.html
 * Provides functionality related to parsing and rendering dom nodes.
 */
tapestry.html={
    
    TextareaMatcher:'<textarea(.*?)/>', // regexp for compact textarea elements
    TextareaReplacer:'<textarea$1></textarea>', // replace pattern for compact textareas
	
    /**
	 * Function: getContentAsString
	 * 
	 * Takes a dom node and returns its contents rendered in a string.
     *
     * The resulting string does NOT contain any markup (or attributes) of
     * the given node - only child nodes are rendered and returned.Content
     *
     * Implementation Note: This function tries to make use of browser 
     * specific features (the xml attribute of nodes in IE and the XMLSerializer
     * object in Mozilla derivatives) - if those fails, a generic implementation
     * is used that is guaranteed to work in all platforms.
	 * 
	 * Parameters: 
	 * 
	 *	node - The dom node.
	 * Returns:
	 * 
	 * The string representation of the given node's contents.
	 */    
	getContentAsString:function(node){
		if (typeof node.xml != "undefined")
			return this._getContentAsStringIE(node);
		else if (typeof XMLSerializer != "undefined" )
			return this._getContentAsStringMozilla(node);
		else
			return this._getContentAsStringGeneric(node);
	},        
	
   /**
	 * Function: getElementAsString
	 * 
	 * Takes a dom node and returns itself and its contents rendered in a string.
     *
     * Implementation Note: This function uses a generic implementation in order
     * to generate the returned string.
	 * 
	 * Parameters: 
	 * 
	 *	node - The dom node.
	 * Returns:
	 * 
	 * The string representation of the given node.
	 */         
	getElementAsString:function(node){
		if (!node) { return ""; }
		
		var s='<' + node.nodeName;
		// add attributes
		if (node.attributes && node.attributes.length > 0) {
			for (var i=0; i < node.attributes.length; i++) {
				s += " " + node.attributes[i].name + "=\"" + node.attributes[i].value + "\"";	
			}
		}
		// close start tag
		s += '>';
		// content of tag
		s += this._getContentAsStringGeneric(node);
		// end tag
		s += '</' + node.nodeName + '>';
		return s;
	},        

	_getContentAsStringIE:function(node){
		var s="";
    	for (var i = 0; i < node.childNodes.length; i++)
        	s += node.childNodes[i].xml;
    	return s;
	},
	
	_getContentAsStringMozilla:function(node){
		var xmlSerializer = new XMLSerializer();
	    var s = "";
	    for (var i = 0; i < node.childNodes.length; i++) {
	        s += xmlSerializer.serializeToString(node.childNodes[i]);
	        if (s == "undefined")
		        return this._getContentAsStringGeneric(node);
	    }
	    
        s = this._processTextareas(s);
        
	    return s;
	},
	
	_getContentAsStringGeneric:function(node){
		var s="";
		if (node == null) { return s; }
		for (var i = 0; i < node.childNodes.length; i++) {
			switch (node.childNodes[i].nodeType) {
				case 1: // ELEMENT_NODE
				case 5: // ENTITY_REFERENCE_NODE
					s += this.getElementAsString(node.childNodes[i]);
					break;
				case 3: // TEXT_NODE
				case 2: // ATTRIBUTE_NODE
				case 4: // CDATA_SECTION_NODE
					s += node.childNodes[i].nodeValue;
					break;
				default:
					break;
			}
		}
		return s;	
	},

	_processTextareas:function(htmlData)
 	{
        var match = new RegExp(tapestry.html.TextareaMatcher);
        while (htmlData.match(match)){
            htmlData = htmlData.replace(match, tapestry.html.TextareaReplacer);
        }
        return htmlData;
 	}
}
