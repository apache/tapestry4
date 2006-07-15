dojo.provide("tapestry.html");

tapestry.html={
	
	getContentAsString:function(node){
		if (typeof node.xml != "undefined")
			return this.getContentAsStringIE(node);
		else if (typeof XMLSerializer != "undefined" )
			return this.getContentAsStringMozilla(node);
		else
			return this.getContentAsStringGeneric(node);
	},
	
	getContentAsStringIE:function(node){
		var s="";
    	for (var i = 0; i < node.childNodes.length; i++)
        	s += node.childNodes[i].xml;
    	return s;
	},
	
	getContentAsStringMozilla:function(node){
		var xmlSerializer = new XMLSerializer();
	    var s = "";
	    for (var i = 0; i < node.childNodes.length; i++) {
	        s += xmlSerializer.serializeToString(node.childNodes[i]);
	        if (s == "undefined")
		        return this.getContentAsStringGeneric(node);
	    }
	    return s;
	},
	
	getContentAsStringGeneric:function(node){
		var s="";
		if (node == null) { return s; }
		for (var i = 0; i < node.childNodes.length; i++) {
			switch (node.childNodes[i].nodeType) {
				case 1: // ELEMENT_NODE
				case 5: // ENTITY_REFERENCE_NODE
					s += this.getElementAsStringGeneric(node.childNodes[i]);
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
	
	getElementAsStringGeneric:function(node){
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
		s += this.getContentAsStringGeneric(node);
		// end tag
		s += '</' + node.nodeName + '>';
		return s;
	}
}