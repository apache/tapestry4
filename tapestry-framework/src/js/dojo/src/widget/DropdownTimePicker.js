/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.DropdownTimePicker");

dojo.require("dojo.widget.*");
dojo.require("dojo.widget.DropdownContainer");
dojo.require("dojo.widget.TimePicker");
dojo.require("dojo.event.*");
dojo.require("dojo.html.*");

dojo.require("dojo.i18n.common");
dojo.requireLocalization("dojo.widget", "DropdownTimePicker");

dojo.widget.defineWidget(
	"dojo.widget.DropdownTimePicker",
	dojo.widget.DropdownContainer,
	{
		iconURL: dojo.uri.dojoUri("src/widget/templates/images/timeIcon.gif"),
		zIndex: "10",
		timePicker: null,
		
		timeFormat: "%R",
		time: null,

		postMixInProperties: function(localProperties, frag) {
			dojo.widget.DropdownTimePicker.superclass.postMixInProperties.apply(this, arguments);
			var messages = dojo.i18n.getLocalization("dojo.widget", "DropdownTimePicker", this.lang);
			this.iconAlt = messages.selectTime;
		},

		fillInTemplate: function(args, frag){
			dojo.widget.DropdownTimePicker.superclass.fillInTemplate.call(this, args, frag);

			var timeProps = { widgetContainerId: this.widgetId };
			this.timePicker = dojo.widget.createWidget("TimePicker", timeProps, this.containerNode, "child");
			dojo.event.connect(this.timePicker, "onSetTime", this, "onSetTime");
			dojo.event.connect(this.inputNode,  "onchange",  this, "onInputChange");
			this.containerNode.style.zIndex = this.zIndex;
			this.containerNode.explodeClassName = "timeBorder";
			if(args.storedtime){
				this.timePicker.selectedTime.anyTime = false;
				this.timePicker.setDateTime("2005-01-01T" + args.storedtime);
				this.timePicker.initData();
				this.timePicker.initUI();
				this.onSetTime();
			}
		},
		
		onSetTime: function(){
			this.inputNode.value = this.timePicker.selectedTime.anyTime ? "" : dojo.date.strftime(this.timePicker.time, this.timeFormat);
			this.hideContainer();
		},
		
		onInputChange: function(){
			this.timePicker.time = "2005-01-01T" + this.inputNode.value;
			this.timePicker.setDateTime(this.timePicker.time);
			this.timePicker.initData();
			this.timePicker.initUI();
		},
		
		enable: function() {
			this.inputNode.disabled = false;
			this.timePicker.enable();
			this.inherited("enable", []);
		},
		
		disable: function() {
			this.inputNode.disabled = true;
			this.timePicker.disable();
			this.inherited("disable", []);
		}
	}
);
