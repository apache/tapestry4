/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.DropdownDatePicker");

dojo.require("dojo.widget.*");
dojo.require("dojo.widget.DropdownContainer");
dojo.require("dojo.widget.DatePicker");
dojo.require("dojo.event.*");
dojo.require("dojo.html.*");
dojo.require("dojo.date.format");
dojo.require("dojo.date.serialize");
dojo.require("dojo.string.common");
dojo.require("dojo.i18n.common");
dojo.requireLocalization("dojo.widget", "DropdownDatePicker");

		/*
		summary: 
			DatePicker form input that allows for selecting a Date from the UI or typing of date in an inputbox
	 	description: 
			Basically this is DatePicker in a DropdownContainer, it supports all features of DatePicker as well
			as wrapping its value inside of a form element to be submitted.
	 	 usage: 
	 	              var ddp = dojo.widget.createWidget("DropdownDatePicker", {},   
	 	              dojo.byId("DropdownDatePickerNode")); 
	 	 
	 	              <input dojoType="DropdownDatePicker">
		*/

dojo.widget.defineWidget(
	"dojo.widget.DropdownDatePicker",
	dojo.widget.DropdownContainer,
	{
		iconURL: dojo.uri.dojoUri("src/widget/templates/images/dateIcon.gif"),
		zIndex: "10",
		
		//String
		// 	pattern used in display of formatted date.  See dojo.date.format.
		displayFormat: "",
		dateFormat: "", // deprecated in 0.5
		//String
		// 	formatting used when submitting form.  A pattern string like display format or one of the following:
		// 	rfc|iso|posix|unix  By default, uses rfc3339 style date formatting.
		saveFormat: "",
		//String
		// 	type of format appropriate to locale.  see dojo.date.format
		formatLength: "short", // only parsing of short is supported at this time
		//String|Date
		//	form value property if =='today' will default to todays date
		value: "", 
		//String
		// 	name of the form element
		name: "",
		//Integer
		//	total weeks to display default 
		displayWeeks: 6, 
		//Boolean
		//	if true, weekly size of calendar changes to acomodate the month if false, 42 day format is used
		adjustWeeks: false,
		//String|Date
		//	first available date in the calendar set
		startDate: "1492-10-12",
		//String|Date
		//	last available date in the calendar set
		endDate: "2941-10-12",
		//Integer
		//	adjusts the first day of the week 0==Sunday..6==Saturday
		weekStartsOn: "",
		//String
		//	deprecated use value instead
		storedDate: "",
		//Boolean
		//d	isable all incremental controls, must pick a date in the current display
		staticDisplay: false,
		
		postMixInProperties: function(localProperties, frag){
			// summary: see dojo.widget.DomWidget

			dojo.widget.DropdownDatePicker.superclass.postMixInProperties.apply(this, arguments);
			var messages = dojo.i18n.getLocalization("dojo.widget", "DropdownDatePicker", this.lang);
			this.iconAlt = messages.selectDate;
			
			if(typeof(this.value)=='string'&&this.value.toLowerCase()=='today'){
				this.value = new Date();
			}
			if(this.value && isNaN(this.value)){
				var orig = this.value;
				this.value = dojo.date.fromRfc3339(this.value);
				if(!this.value){this.value = new Date(orig); dojo.deprecated("dojo.widget.DropdownDatePicker", "date attributes must be passed in Rfc3339 format", "0.5");}
			}
			if(this.value && !isNaN(this.value)){
				this.value = new Date(this.value);
			}
		},

		fillInTemplate: function(args, frag){
			// summary: see dojo.widget.DomWidget
			dojo.widget.DropdownDatePicker.superclass.fillInTemplate.call(this, args, frag);
			//attributes to be passed on to DatePicker
			var dpArgs = {widgetContainerId: this.widgetId};
			if(this.value){ dpArgs.value = this.value; }
			if(this.startDate){ dpArgs.startDate = this.startDate; }
			if(this.endDate){ dpArgs.endDate = this.endDate; }
			if(this.displayWeeks){ dpArgs.displayWeeks = this.displayWeeks; }
			if(this.weekStartsOn){ dpArgs.weekStartsOn = this.weekStartsOn; }
			if(this.adjustWeeks){ dpArgs.adjustWeeks = this.adjustWeeks; }
			if(this.staticDisplay){ dpArgs.staticDisplay = this.staticDisplay; }
			if(this.value){ dpArgs.date = this.value; }
			if(this.storedDate){ dpArgs.storedDate = this.storedDate; } //deprecated in 0.5
			//build the args for DatePicker based on the public attributes of DropdownDatePicker
			this.datePicker = dojo.widget.createWidget("DatePicker", dpArgs, this.containerNode, "child");
			dojo.event.connect(this.datePicker, "onValueChanged", this, "onSetDate");
			
			if(this.value){
				this.onSetDate();
			}
			this.containerNode.style.zIndex = this.zIndex;
			this.containerNode.explodeClassName = "calendarBodyContainer";
			this.valueNode.name=this.name;
		},

		getValue: function(/*Boolean*/displayFormat){
			// summary: return current date in RFC 3339 format if displayFormat is false otherwise, uses displayFormat that inputNode uses
			return (displayFormat)?dojo.date.format(this.datePicker.value,{formatLength:this.formatLength, datePattern:this.displayFormat, selector:'dateOnly', locale:this.lang}):dojo.date.toRfc3339(new Date(this.datePicker.value),'dateOnly'); /*String*/
		},

		getDate: function(){
			// summary: return current date as a Date object
			return this.datePicker.value; /*Date*/
		},

		setValue: function(/*Date|String*/rfcDate){
			//summary: set the current date from RFC 3339 formatted string or a date object, synonymous with setDate
			this.setDate(rfcDate);
		},			
			
		setDate: function(/*Date|String*/rfcDate){
		//summary: set the current date and update the UI
			this.datePicker.setDate(rfcDate);
			this._synchValueNode();
		},
		
		onSetDate: function(){
			if(this.dateFormat){
				dojo.deprecated("dojo.widget.DropdownDatePicker",
				"Must use displayFormat attribute instead of dateFormat.  See dojo.date.format for specification.", "0.5");
				this.inputNode.value = dojo.date.strftime(this.datePicker.value, this.dateFormat, this.lang);
			}else{
				this.inputNode.value = dojo.date.format(this.datePicker.value,
					{formatLength:this.formatLength, datePattern:this.displayFormat, selector:'dateOnly', locale:this.lang});
			}
			this._synchValueNode();
			this.onValueChanged(this.getDate());
			this.hideContainer();
		},

		onValueChanged: function(/*Date*/date){
		//summary: triggered when this.value is changed
		},
		
		onInputChange: function(){
			if(this.dateFormat){
				dojo.deprecated("dojo.widget.DropdownDatePicker",
				"Cannot parse user input.  Must use displayFormat attribute instead of dateFormat.  See dojo.date.format for specification.", "0.5");
			}else{
				var input = dojo.string.trim(this.inputNode.value);
				if(input){
					var inputDate = dojo.date.parse(input,
							{formatLength:this.formatLength, datePattern:this.displayFormat, selector:'dateOnly', locale:this.lang});			
					if(inputDate){
						this.setDate(inputDate);
					}
				} else {
					this.valueNode.value = input;
				}
			}
			// If the date entered didn't parse, reset to the old date.  KISS, for now.
			//TODO: usability?  should we provide more feedback somehow? an error notice?
			// seems redundant to do this if the parse failed, but at least until we have validation,
			// this will fix up the display of entries like 01/32/2006
			if(input){ this.onSetDate(); }
		},

		_synchValueNode: function(){
			var date = this.datePicker.value;
			var value;
			switch(this.saveFormat.toLowerCase()){
				case "rfc": case "iso": case "":
					value = dojo.date.toRfc3339(date, 'dateOnly');
					break;
				case "posix": case "unix":
					value = Number(date);
					break;
				default:
					value = dojo.date.format(date, {datePattern:this.saveFormat, selector:'dateOnly', locale:this.lang});
			}
			this.valueNode.value = value;
		},
		
		enable: function() {
			this.inputNode.disabled = false;
			this.datePicker.enable();
			this.inherited("enable", []);
		},
		
		disable: function() {
			this.inputNode.disabled = true;
			this.datePicker.disable();
			this.inherited("disable", []);
		}
	}
);
