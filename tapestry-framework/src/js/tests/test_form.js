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
		Tapestry.register_form();
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
}

function test_form_requireTextField(){
	Tapestry.require_field(null, "bs", "invalid");
	
	var node = document.createElement("input");
	node.setAttribute("id", "testid");
	node.type="text";
	node.value="";
	
	var mockInvalid=new mock(node, "must have value");
	dojo.event.connect(Tapestry, "default_invalid_field_handler", mockInvalid, "intercept");
	
	Tapestry.require_field(null, "testid", "must have value");
	
	jum.assertTrue("invalidCalled", mockInvalid.called);
	dojo.event.disconnect(Tapestry, "default_invalid_field_handler", mockInvalid, "intercept");
}

function test_form_submit(){
	
	var submitCalled=false;
	var node = document.createElement("form");
	node.setAttribute("id", "form1");
	node.submit=function(){
		submitCalled=true;
	}
	node.submitname={value:""};
	node.elements=[];
	
	Tapestry.register_form("form1");
	Tapestry.submit_form("form1", "testSubmit");
	
	jum.assertTrue("submitCalled", submitCalled);
	jum.assertEquals("submitName", node.submitname.value, "testSubmit");
}

var bindCalled=false;

function test_submit_parms(){
	bindCalled=false;
	var node = document.createElement("form");
	node.setAttribute("id", "formparmtest");
	node.setAttribute("method", "post");
	node.setAttribute("action", "/default/url");
	node.submit=function(){}
	node.submitname={value:""};
	node.elements=[];
	document.body.appendChild(node);
	
	dojo.event.connect(dojo.io, "bind", this, checkSubmitParms);
	
	tapestry.form.registerForm("formparmtest");
	tapestry.form.submit("formparmtest", null, {async:true,url:"/new/url"});
	
	jum.assertTrue("bindCalled", bindCalled);
	
	dojo.event.disconnect(dojo.io, "bind", this, checkSubmitParms);
}

function checkSubmitParms(kwArgs){
	bindCalled=true;
	jum.assertEquals("submitParmUrl", kwArgs["url"], "/new/url");
}

function test_submit_defaultParms(){
	bindCalled=false;
	var node = document.createElement("form");
	node.setAttribute("id", "formasynctest");
	node.setAttribute("method", "post");
	node.setAttribute("action", "/default/url");
	node.submit=function(){}
	node.submitname={value:""};
	node.elements=[];
	document.body.appendChild(node);
	
	dojo.event.connect(dojo.io, "bind", this, checkDefaultParms);
	
	tapestry.form.registerForm("formasynctest", true);
	tapestry.form.submit("formasynctest");
	
	jum.assertTrue("bindCalled", bindCalled);
	
	dojo.event.disconnect(dojo.io, "bind", this, checkDefaultParms);
}

function checkDefaultParms(kwArgs){
	bindCalled=true;
	jum.assertTrue("submitParmUrl2", typeof kwArgs["url"] == "undefined");
}
