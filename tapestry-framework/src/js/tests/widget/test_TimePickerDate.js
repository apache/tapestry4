dojo.registerModulePath("tapestry", "../tapestry-4.1.6");

dojo.require("tapestry.test");
dojo.require("tapestry.core");
dojo.require("dojo.date.format");

function test_time_format(){
	var dt = new Date();
	var value = dojo.date.format(dt, {timePattern:"h:mm a"});
	jum.assertTrue(value, value.indexOf("NaN") < 0);
	
	dt = new Date("03:00 am");
	value = dojo.date.format(dt, {timePattern:"h:mm a"});
	jum.assertFalse(value, value.indexOf("NaN") < 0);
}
