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
    var initial = "start<textarea id='2' rows=4/>22<input type='text'/><textarea/><div id=1/>";
    var expected = "start<textarea id='2' rows=4></textarea>22<input type='text'/><textarea></textarea><div id=1></div>";
    
    jum.assertEquals(expected, tapestry.html._processCompactElements(initial));
    jum.assertEquals(expected + expected, 
        tapestry.html._processCompactElements(initial+initial));
}

function test_html_processResponse(){
    var initial = '<div id="editTopic"><form method="post" action="Topics,topicList.$Form.sdirect" id="Form"> '
        +'<div style="display:none;" id="Formhidden"><input type="hidden" name="formids" value="topicName,shortDescriptiveText,descriptiveText,If,If_0,updateTopic" /> '
        +'<input type="hidden" name="updateParts" value="list" /> <input type="hidden" name="updateParts" value="edit" /> <input type="hidden" name="reservedids" '
        +'value="updateParts" /> <input type="hidden" name="submitmode" value="" /> <input type="hidden" name="submitname" value="" /> <input type="hidden" name="If" value="T" /> '
        +'<input type="hidden" name="If_0" value="F" /> </div> Make the desired changes to this topic. <fieldset> <div> <label for="topicName" class="required">Topic Name</label> '
        +'<input type="text" name="name" value="Uncategorized" id="tName" /> </div> <div> <label for="shortDescriptiveText" class="required">Short Description</label> '
        +'<textarea name="shortDescriptiveText" id="shortDescriptiveText" cols="40" rows="4">Information</textarea> </div> <div> <label for="descriptiveText">Additional '
        +'Description</label> <textarea name="descriptiveText" id="descriptiveText" cols="40" rows="4"/> </div> <div> <label for="pUrlField" class="required">Home '
        +'Url</label> <input type="text" name="pUrlField" value="ba.org" id="pUrlField" size="40" /> Verified </div> <div></div> </fieldset> <div> '
        +'<input type="submit" name="uTopic" id="uTopic" value="Save Changes"/> <!--input type="submit" jwcid="cancelTopic@Submit" '
        +'listener="listener:doCancel" value="message:label.cancel-changes" async="true"/--></div> </form> </div>';
    var expected = '<div id="editTopic"><form method="post" action="Topics,topicList.$Form.sdirect" id="Form"> '
        +'<div style="display:none;" id="Formhidden"><input type="hidden" name="formids" value="topicName,shortDescriptiveText,descriptiveText,If,If_0,updateTopic" /> '
        +'<input type="hidden" name="updateParts" value="list" /> <input type="hidden" name="updateParts" value="edit" /> <input type="hidden" name="reservedids" '
        +'value="updateParts" /> <input type="hidden" name="submitmode" value="" /> <input type="hidden" name="submitname" value="" /> <input type="hidden" name="If" value="T" /> '
        +'<input type="hidden" name="If_0" value="F" /> </div> Make the desired changes to this topic. <fieldset> <div> <label for="topicName" class="required">Topic Name</label> '
        +'<input type="text" name="name" value="Uncategorized" id="tName" /> </div> <div> <label for="shortDescriptiveText" class="required">Short Description</label> '
        +'<textarea name="shortDescriptiveText" id="shortDescriptiveText" cols="40" rows="4">Information</textarea> </div> <div> <label for="descriptiveText">Additional '
        +'Description</label> <textarea name="descriptiveText" id="descriptiveText" cols="40" rows="4"></textarea> </div> <div> <label for="pUrlField" class="required">Home '
        +'Url</label> <input type="text" name="pUrlField" value="ba.org" id="pUrlField" size="40" /> Verified </div> <div></div> </fieldset> <div> '
        +'<input type="submit" name="uTopic" id="uTopic" value="Save Changes"/> <!--input type="submit" jwcid="cancelTopic@Submit" '
        +'listener="listener:doCancel" value="message:label.cancel-changes" async="true"/--></div> </form> </div>';
    
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
