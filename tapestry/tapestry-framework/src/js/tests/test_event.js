dojo.registerModulePath("tapestry", "../tapestry");

dojo.require("tapestry.test");
dojo.require("tapestry.lang");
dojo.require("tapestry.event");

function test_eventCapture_props(){
	var fevent=document.createEvent('UIEvents');
	fevent.type="testType";

	var tnode = document.createElement("div");
	tnode.setAttribute("id", "testid");
	fevent.target=tnode;

	var props = tapestry.event.buildEventProperties(fevent);

	jum.assertTrue("evType", dojo.event.browser.isEvent(fevent));
	jum.assertTrue("testNullProp", tapestry.event.buildEventProperties({}));
	jum.assertTrue("type", props.beventtype != "undefined");
	jum.assertEquals("targetprops", "testid", props["beventtarget.id"]);
}
