dojo.require("dojo.date");

dojo.setModulePrefix("tapestry", "../tapestry");

dojo.require("tapestry.*");
dojo.require("tapestry.test");

function test_time_format(){
	var dt = new Date();
	var value = dojo.date.format(dt, "%I:%M %p");
	jum.assertTrue(value, value.indexOf("NaN") < 0);
	
	dt = new Date("03:00 am");
	value = dojo.date.format(dt, "%I:%M %p");
	jum.assertFalse(value, value.indexOf("NaN") < 0);
}
