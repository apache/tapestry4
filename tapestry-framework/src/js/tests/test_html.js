dojo.registerModulePath("tapestry", "../tapestry");

dojo.require("tapestry.core");
dojo.require("tapestry.test");
dojo.require("tapestry.html");


function test_html_getContentAsString(){
	var node = document.createElement("div");
	node.setAttribute("id", "testid");
        
	var node2 = document.createElement("div");
	node2.setAttribute("id", "testid2");
        var content = document.createTextNode("content");
        node2.appendChild(content);
        
        node.appendChild(node2);
        
        var data = tapestry.html.getContentAsString(node).toLowerCase();
        jum.assertEquals("<div id=\"testid2\">content</div>", data);
}
