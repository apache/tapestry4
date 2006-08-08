dojo.setModulePrefix("tapestry", "../tapestry");

dojo.require("dojo.date");

dojo.require("tapestry.*");
dojo.require("tapestry.test");
dojo.require("tapestry.form.datetime");

function test_datetime_validDate(){
	var value = "08/15/1999";
	jum.assertTrue(value, tapestry.form.datetime.isValidDate(value));
	
	value = "12112/12/23434";
	jum.assertFalse(value, tapestry.form.datetime.isValidDate(value));
	jum.assertFalse("null value", tapestry.form.datetime.isValidDate());
}

function test_datetime_maxDate(){
	var maxValue = "09/28/2020";
	
	var value = "08/15/2021";
	jum.assertTrue(value, tapestry.form.datetime.isValidDate(value, {max:maxValue}));
	
	jum.assertFalse(value, tapestry.form.datetime.isValidDate(value, 
									{max:maxValue, format:"MM/DD/YYYY"}));
	
	value = "08/15/2020";
	jum.assertTrue(value, tapestry.form.datetime.isValidDate(value, 
									{max:maxValue, format:"MM/DD/YYYY"}));
				
	jum.assertTrue("09/28/2020", tapestry.form.datetime.isValidDate(
			"09/28/2020",
			{max:maxValue, format:"MM/DD/YYYY"})
	);
}

function test_datetime_minDate(){
	var minValue = "09/28/2000";
	
	var value = "09/27/2000";
	jum.assertTrue(value, tapestry.form.datetime.isValidDate(value, {min:minValue}));
	
	jum.assertFalse(value, tapestry.form.datetime.isValidDate(value, 
									{min:minValue, format:"MM/DD/YYYY"}));
	
	value = "09/28/2000";
	jum.assertTrue(value, tapestry.form.datetime.isValidDate(value, 
									{min:minValue, format:"MM/DD/YYYY"}));
}

function test_datetime_posixFormat(){
	var value = "18 Aug 2006";
	
	jum.assertFalse(value, tapestry.form.datetime.isValidDate(value, {max:"06 Aug 2006",format:"%d %h %Y"}));
	jum.assertTrue(value, tapestry.form.datetime.isValidDate(value, {max:"19 Aug 2006",format:"%d %h %Y"}));
}
