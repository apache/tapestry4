dojo.registerModulePath("tapestry", "../tapestry");

dojo.require("tapestry.core");
dojo.require("tapestry.test");
dojo.require("tapestry.html");


function test_html_getContentAsString(){
    
        var node = _createTestNode();
        
        var data = tapestry.html.getContentAsString(node).toLowerCase();
        jum.assertEquals("<div id=\"testid2\">content</div>", data);
}

function test_html_textarea(){
    
        var node = _createTestNode("textarea", true);
        
        var data = tapestry.html.getContentAsString(node).toLowerCase();
        jum.assertEquals("<textarea id=\"testid2\"></textarea>", data);
        // cannot test Mozilla's getContentAsString from here... 
        // only browser based tests will show if this is working
}

function _createTestNode(element, empty){
	var node = document.createElement("div");
	node.setAttribute("id", "testid");
        
        if (!element)
            element="div";
        
	var node2 = document.createElement(element);
	node2.setAttribute("id", "testid2");
        
        if (!empty){
            var content = document.createTextNode("content");
            node2.appendChild(content);
        }
        
        node.appendChild(node2);
        
        return node;
}
