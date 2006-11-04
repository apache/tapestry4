dojo.registerModulePath("tapestry", "../tapestry");

dojo.require("dojo.widget.*");
dojo.require("tapestry.test");
dojo.require("tapestry.core");
dojo.require("tapestry.form");
dojo.require("tapestry.form.datetime");

function test_datetime_validDate(){
	var value = "08/15/1999";
	// jum.assertFalse(value, tapestry.form.datetime.isValidDate(value, {}));
	jum.assertTrue(value, tapestry.form.datetime.isValidDate(value, 
				{datePattern:"MM/dd/yyyy",selector:"dateOnly"}));
	
	// value = "12112/12/23434"; "08 Sep 2006"
	// jum.assertFalse(value, tapestry.form.datetime.isValidDate(value));
	// jum.assertFalse("null value", tapestry.form.datetime.isValidDate());
	jum.assertTrue("verbose date valid", tapestry.form.datetime.isValidDate("08 Sep 2006",
					{strict:true, datePattern: "d MMM yyyy", selector:"dateOnly"} ));
}

function test_datetime_maxDate(){
	var maxValue = "09/28/2020";
	
	var value = "08/15/2021";
	jum.assertFalse(value, tapestry.form.datetime.isValidDate(value, 
					{strict:true,max:maxValue,datePattern:"MM/dd/yyyy",selector:"dateOnly"}));
	
	jum.assertTrue("08/15/2020", tapestry.form.datetime.isValidDate("08/15/2020", 
						{strict:true, max:maxValue, datePattern:"MM/dd/yyyy",selector:"dateOnly"}));
	
	value = "08/15/2020";
	jum.assertTrue(value, tapestry.form.datetime.isValidDate(value, 
									{strict:true, max:maxValue, datePattern:"MM/dd/yyyy",selector:"dateOnly"}));
	
	jum.assertTrue("09/28/2020", tapestry.form.datetime.isValidDate(
			"09/28/2020",
			{strict:true, max:maxValue, datePattern:"MM/dd/yyyy",selector:"dateOnly"})
	);
}

function test_datetime_minDate(){
	var minValue = "09/28/2000";
	
	var value = "09/27/2000";
	jum.assertFalse(value, tapestry.form.datetime.isValidDate(value, 
				{strict:true, min:minValue,selector:"dateOnly",datePattern:"MM/dd/yyyy"}));
	
	jum.assertTrue("11/27/2000", tapestry.form.datetime.isValidDate("11/27/2000", 
									{strict:true,min:minValue, datePattern:"MM/dd/yyyy",selector:"dateOnly"}));
	
	value = "09/28/2000";
	jum.assertTrue(value, tapestry.form.datetime.isValidDate(value, 
									{strict:true,min:minValue, datePattern:"MM/dd/yyyy",selector:"dateOnly"}));
}


function test_datetime_LongFormat(){
	var value = "18 Aug 2006";
	
	jum.assertFalse(value, tapestry.form.datetime.isValidDate(value, 
			{strict:true,max:"06 Aug 2006",datePattern:"dd MMM yyyy",selector:"dateOnly"}));
			
	jum.assertTrue(value, tapestry.form.datetime.isValidDate(value, 
			{strict:true,max:"19 Aug 2006",datePattern:"dd MMM yyyy",selector:"dateOnly"}));
	
	value = "4 Nov 2006";
	jum.assertTrue(value, tapestry.form.datetime.isValidDate(value, 
			{max:"04 Nov 2006",datePattern:"dd MMM yyyy"}));
}
