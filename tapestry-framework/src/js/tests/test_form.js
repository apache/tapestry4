dojo.registerModulePath("tapestry", "../tapestry");

dojo.require("tapestry.core");
dojo.require("tapestry.test");
dojo.require("tapestry.form");

tapestry.form.invalidField=function(field, message){
}

function test_last_msg(){
	jum.assertFalse(lastMsgContains());
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

	dojo.event.connect(dojo.io, "queueBind", this, checkSubmitParms);

	tapestry.form.registerForm("formparmtest");
	tapestry.form.submit("formparmtest", null, {async:true,url:"/a/url"});

	jum.assertTrue("bindCalled", bindCalled);

	dojo.event.disconnect(dojo.io, "queueBind", this, checkSubmitParms);
}

function checkSubmitParms(kwArgs){
	bindCalled=true;
	jum.assertEquals("submitParmUrl", kwArgs["url"], "/a/url");
}

function test_submit_defaultParms(){
	bindCalled=false;
	var node = document.createElement("form");
	node.setAttribute("id", "formasynctest");
	node.setAttribute("method", "post");
	node.setAttribute("action", "/a/url");
	node.submit=function(){}
	node.submitname={value:""};
	node.elements=[];
	document.body.appendChild(node);

	dojo.event.connect(dojo.io, "queueBind", this, checkDefaultParms);

	tapestry.form.registerForm("formasynctest", true);
	tapestry.form.submit("formasynctest");

	jum.assertTrue("bindCalled", bindCalled);

	dojo.event.disconnect(dojo.io, "queueBind", this, checkDefaultParms);
}

function checkDefaultParms(kwArgs){
	bindCalled=true;
	jum.assertTrue("submitParmUrl2", typeof kwArgs["url"] == "undefined");
}
