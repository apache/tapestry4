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
