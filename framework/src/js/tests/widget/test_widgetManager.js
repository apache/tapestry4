dojo.require("dojo.widget.Checkbox");

dojo.setModulePrefix("tapestry", "../tapestry");

dojo.require("tapestry.*");
dojo.require("tapestry.test");
dojo.require("tapestry.widget.*");

function test_syncfailure_widget(){
	try {
		tapestry.widget.synchronizeWidgetState("bs", "NonExistant", {});
	} catch (e) { jum.assertTrue("test2", e instanceof Error); return; }
	throw new JUMAssertFailure("Previous test should have failed.");
}

function test_create_widget(){
	var dp = new dojo.widget.Checkbox();
	jum.assertFalse("test1", !dp);
}
