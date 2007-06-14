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

function test_html_getElementAsString(){
    
        var node = _createTestNode();
        
        var data = tapestry.html.getElementAsString(node).toLowerCase();
        jum.assertEquals("<div id=\"testid\"><div id=\"testid2\">content</div></div>", data);
}

function test_html_processTextareas(){
    var initial = "start<textarea id='2' rows=4/>";
    var expected = "start<textarea id='2' rows=4></textarea>";
    
    jum.assertEquals(expected, tapestry.html._processCompactElements(initial));
    jum.assertEquals(expected + expected, 
        tapestry.html._processCompactElements(initial+initial));
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
