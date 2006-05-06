dojo.setModulePrefix("tapestry", "../tapestry/framework/src/js/tapestry");

dojo.require("dojo.widget.*");
dojo.require("dojo.widget.DatePicker");

dojo.require("tapestry.test");
dojo.require("tapestry.widget.*");

function test_syncfailure_widget(){
	
	jum.assertEquals("test1", dojo.hostenv.getBaseScriptUri(), "/home/jkuhnert/projects/dojo/");
	
	try {
		tapestry.widget.synchronizeWidgetState("bs", "NonExistant", {});
	} catch (e) { jum.assertTrue("test2", e instanceof Error); return; }
	throw new JUMAssertFailure("Previous test should have failed.");
}

function test_create_widget(){
	var dp = new dojo.widget.DatePicker();
	jum.assertFalse("test1", !dp);
}