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

dojo.widget.defineWidget(
	"dojo.widget.DropdownDatePicker",
	dojo.widget.DropdownContainer,
	{
		iconURL: dojo.uri.dojoUri("src/widget/templates/images/dateIcon.gif"),
		zIndex: "10",

		// pattern used in display of formatted date.  See dojo.date.format.
		displayFormat: "",
		dateFormat: "", // deprecated in 0.5
		// formatting used when submitting form.  A pattern string like display format or one of the following:
		// rfc|iso|posix|unix  By default, uses rfc3339 style date formatting.
		saveFormat: "",
		// type of format appropriate to locale.  see dojo.date.format
		formatLength: "short", // only parsing of short is supported at this time
		date: "", //if =='today' will default to todays date
		// name of the form element
		name: "",

		postMixInProperties: function(localProperties, frag){
			// summary: see dojo.widget.DomWidget

			dojo.widget.DropdownDatePicker.superclass.postMixInProperties.apply(this, arguments);
			var messages = dojo.i18n.getLocalization("dojo.widget", "DropdownDatePicker", this.lang);
			this.iconAlt = messages.selectDate;
			
			if(typeof(this.date)=='string'&&this.date.toLowerCase()=='today'){
				this.date = new Date();
			}
			if(this.date && isNaN(this.date)){
				var orig = this.date;
				this.date = dojo.date.fromRfc3339(this.date);
				if(!this.date){this.date = new Date(orig); dojo.deprecated("dojo.widget.DropdownDatePicker", "date attributes must be passed in Rfc3339 format", "0.5");}
			}
			if(this.date && !isNaN(this.date)){
				this.date = new Date(this.date);
			}
		},

		fillInTemplate: function(args, frag){
			// summary: see dojo.widget.DomWidget

			dojo.widget.DropdownDatePicker.superclass.fillInTemplate.call(this, args, frag);

			this.datePicker = dojo.widget.createWidget("DatePicker",
				{ widgetContainerId: this.widgetId, lang: this.lang, date: this.date }, this.containerNode, "child");
			dojo.event.connect(this.datePicker, "onSetDate", this, "onSetDate");
			
			if(this.date){
				this.onSetDate();
			}
			this.containerNode.style.zIndex = this.zIndex;
			this.containerNode.explodeClassName = "calendarBodyContainer";
			this.valueNode.name=this.name;
		},

		onSetDate: function(){
			if(this.dateFormat){
				dojo.deprecated("dojo.widget.DropdownDatePicker",
				"Must use displayFormat attribute instead of dateFormat.  See dojo.date.format for specification.", "0.5");
				this.inputNode.value = dojo.date.strftime(this.datePicker.date, this.dateFormat, this.lang);
			}else{
				this.inputNode.value = dojo.date.format(this.datePicker.date,
					{formatLength:this.formatLength, datePattern:this.displayFormat, selector:'dateOnly', locale:this.lang});
			}
			this._synchValueNode();
			this.hideContainer();
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
						this.datePicker.setDate(inputDate);
						this._synchValueNode();
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
			var date = this.datePicker.date;
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
