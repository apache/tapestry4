dojo.setModulePrefix("tapestry", "../tapestry");

dojo.require("tapestry.*");
dojo.require("tapestry.test");
dojo.require("tapestry.form");
dojo.require("dojo.lang.*");
dojo.require("dojo.validate.common");

function test_register_invalidform(){
	try {
		tapestry.form.registerForm("bsid");
		throw new JUMAssertFailure("Previous test should have failed.");
	} catch (e) { 
		jum.assertTrue("testFormRegisterInvalid", e instanceof Error); 
	}
}

function test_register_form(){
	var form = document.createElement("form");
	form.setAttribute("id", "regform");
	form.submit=function(){
		form.submitCalled=true;
	}
	form.submitname={value:""};
	
	tapestry.form.registerForm("regform");
	jum.assertTrue("formregForm", dojo.lang.isObject(tapestry.form.forms["regform"]));
	jum.assertTrue("formregProfiles", dojo.lang.isArray(tapestry.form.forms["regform"].profiles));
	jum.assertEquals("formregProfileLength", 0, tapestry.form.forms["regform"].profiles.length);
}

function test_validate_realNumber(){
	var value="a12";
	jum.assertFalse(value, dojo.validate.isRealNumber(value, {places:0,decimal:".",separator:","}));
}

/*
dojo.event.browser.stopEvent=function(e){
	if (e) {
		if (!e["preventDefault"]) { e.preventDefault(); }
		if (!e["stopPropogation"]) { e.stopPropagation(); }
	}
}

function test_cancel_form(){
	var form = document.createElement("form");
	form.setAttribute("id", "canform");
	form.submit=function(){
		form.submitCalled=true;
		this.onsubmit();
	}
	form.onsubmit=function(){}
	form.submitname={value:""};
	form.submitmode={value:""};
	
	jum.assertTrue("regform exists", dojo.byId("canform"));
	
	tapestry.form.registerForm(form);
	jum.assertTrue("formregForm", dojo.lang.isObject(tapestry.form.forms["canform"]));
	jum.assertTrue("formregProfiles", dojo.lang.isArray(tapestry.form.forms["canform"].profiles));
	jum.assertEquals("formregProfileLength", 0, tapestry.form.forms["canform"].profiles.length);
	
	tapestry.form.registerProfile("canform", {required:["field1"]});
	jum.assertEquals("formregProfileLength2", 1, tapestry.form.forms["canform"].profiles.length);
	
	var formValidated=false;
	var valObj={
		intercept:function(){this.formValidated=true;}
	};
	dojo.event.connect(tapestry.form.validation, "validateForm", valObj, "intercept");
	
	tapestry.form.cancel("canform");
	
	jum.assertTrue("canFormSubmitted", form.submitCalled);
	jum.assertFalse("canFormValidated", valObj.formValidated);
	
	dojo.event.disconnect(tapestry.form.validation, "validateForm", valObj, "intercept");
}

function test_validate_form(){
	var form = document.createElement("form");
	form.setAttribute("id", "valform");
	form.submit=function(){
		form.submitCalled=true;
		this.onsubmit();
	}
	form.onsubmit=function(){}
	
	form.submitname={value:""};
	form.submitmode={value:""};
	
	tapestry.form.registerForm(form);
	tapestry.form.registerProfile("valform", {required:["field1"]});
	
	jum.assertEquals("formValProfiles", 1, tapestry.form.forms["valform"].profiles.length);
	
	var formValidated=false;
	var valObj={
		intercept:function(){this.formValidated=true;}
	};
	dojo.event.connect(tapestry.form.validation, "validateForm", valObj, "intercept");
	
	tapestry.form.submit("valform");
	
	jum.assertTrue("valFormSubmitted", form.submitCalled);
	jum.assertTrue("valFormValidated", valObj.formValidated);
	
	dojo.event.disconnect(tapestry.form.validation, "validateForm", valObj, "intercept");
}
*/
