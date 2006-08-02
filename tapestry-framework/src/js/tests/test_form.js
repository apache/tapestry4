dojo.setModulePrefix("tapestry", "../tapestry");

dojo.require("tapestry.*");
dojo.require("tapestry.test");
dojo.require("tapestry.form");

tapestry.form.invalidField=function(field, message){
}

function test_form_find(){
	var node = document.createElement("div");
	node.setAttribute("id", "testid");
	
	jum.assertTrue("findwithNode", Tapestry.find(node));
	jum.assertTrue("findwithId", Tapestry.find("testid"));
}

function test_last_msg(){
	jum.assertFalse("lastMessage", lastMsgContains());
}

function test_form_deprecated(){
	try {
		Tapestry.register_form("null");
		throw new JUMAssertFailure("Previous test should have failed.");
	} catch (e) { jum.assertTrue("testFormDepre", e instanceof Error); return; }
	jum.assertTrue("deprecated", lastMsgContains("deprecated"));
	Tapestry.onpresubmit();
	jum.assertTrue("deprecated", lastMsgContains("deprecated"));
	Tapestry.onsubmit();
	jum.assertTrue("deprecated", lastMsgContains("deprecated"));
	Tapestry.onpostsubmit();
	jum.assertTrue("deprecated", lastMsgContains("deprecated"));
	Tapestry.onreset();
	jum.assertTrue("deprecated", lastMsgContains("deprecated"));
	Tapestry.onrefresh();
	jum.assertTrue("deprecated", lastMsgContains("deprecated"));
	Tapestry.oncancel();
	jum.assertTrue("deprecated", lastMsgContains("deprecated"));
}

function test_form_invalidHandler(){
	Tapestry.default_invalid_field_handler(null, "yo", "yo");
	tapestry.form.invalidField("this", "field");
}

function test_form_requireTextField(){
	Tapestry.require_field(null, "bs", "invalid");
	
	var node = document.createElement("input");
	node.setAttribute("id", "testid");
	node.type="text";
	node.value="";
	
	var mockInvalid=new mock(node, "must have value");
	dojo.event.connect(tapestry.form, "invalidField", mockInvalid, "intercept");
	
	tapestry.form.requireField("testid", "must have value");
	
	jum.assertTrue("invalidCalled", mockInvalid.called);
	dojo.event.disconnect(tapestry.form, "invalidField", mockInvalid, "intercept");
}

function test_form_submit(){
	
	var submitCalled=false;
	var node = document.createElement("form");
	node.setAttribute("id", "form1");
	node.submit=function(){
		submitCalled=true;
	}
	node.submitname={value:""};
	
	Tapestry.submit_form("form1", "testSubmit");
	
	jum.assertTrue("submitCalled", submitCalled);
	jum.assertEquals("submitName", node.submitname.value, "testSubmit");
}
