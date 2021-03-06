dojo.registerModulePath("tapestry", "../tapestry-4.1.6");

dojo.require("tapestry.core");
dojo.require("tapestry.event");
dojo.require("tapestry.test");
dojo.require("tapestry.form");
dojo.require("dojo.lang.*");
dojo.require("dojo.validate.check");

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

function test_validate_realNumber_Dojo(){//fails from rhino
	var value="a12";dojo.require("dojo.i18n.number");
	jum.assertFalse(value, dojo.i18n.number.isReal(value, {places:0,decimal:".",separator:","}));
}

function test_validate_realNumber(){//fails from rhino
	var value="a12";
	jum.assertFalse(value, tapestry.form.validation.isReal(value, {places:0,decimal:".",separator:","}));
}

function test_validate_email(){
	var value="test@apache.org";
	jum.assertTrue(value, tapestry.form.validation.isEmailAddress(value));
}

function test_validate_text(){
	var value="small";
	jum.assertTrue(value, tapestry.form.validation.isText(value, {minlength:4}));
}

function test_validate_decimals(){
	var input = "1,124.12";
	jum.assertTrue(dojo.validate.isRealNumber(input, {decimal:".",separator:","}));
	jum.assertTrue(dojo.validate.isInRange(input, {min:2.0,decimal:".",separator:",",symbol:"�"}));
	jum.assertTrue(dojo.validate.isInRange(input, {max:1000000001,decimal:".",separator:",",symbol:"�"}));
}

function test_validate_required(){
	// A generic form
	var f = {

		tx1: {type: "text", value: " 1001 ",  name: "tx1"},
		tx2: {type: "text", value: " ",  name: "tx2"},
		tx3: {type: "text", value: "10/19/2005",  name: "tx3"},

	};

	// Profile for form input
	var profile = {
	// required fields
		required: ["tx2"]
	};

	// results object
	var results = dojo.validate.check(f, profile);

	jum.assertTrue("missing_tx2", results.isMissing("tx2"));
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
