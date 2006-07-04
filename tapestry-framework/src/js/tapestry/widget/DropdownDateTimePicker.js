dojo.provide("tapestry.widget.DropdownDateTimePicker");

dojo.require("dojo.widget.*");
dojo.require("dojo.widget.DropdownContainer");
dojo.require("dojo.event.*");
dojo.require("dojo.html");

dojo.require("tapestry.widget.TimePicker");

dojo.widget.defineWidget(
	"tapestry.widget.DropdownDateTimePicker",
	dojo.widget.DropdownContainer,
	{
		iconURL: dojo.uri.dojoUri("../tapestry/widget/templates/images/dateIcon.gif"),
		iconAlt: "Select a Date/Time",
		zIndex: "10",
		timePicker: null,
		
		dateFormat: "%I:%M %p",
		date: null,
		
		fillInTemplate: function(args, frag){
			tapestry.widget.DropdownDateTimePicker.superclass.fillInTemplate.call(this, args, frag);
			var source = this.getFragNodeRef(frag);
			
			if(args.date){ this.date = new Date(args.date); }
			
			var dpNode = document.createElement("div");
			this.containerNode.appendChild(dpNode);
			
			var dateProps = { widgetContainerId: this.widgetId };
			if(this.date){
				dateProps["date"] = this.date;
				dateProps["storedDate"] = dojo.widget.TimePicker.util.toRfcDateTime(this.date);
				this.inputNode.value = dojo.date.format(this.date, this.dateFormat);
			}
			this.timePicker = dojo.widget.createWidget("TimePicker", dateProps, dpNode);
			dojo.event.connect(this.timePicker, "onSetTime", this, "onSetTime");
			this.containerNode.style.zIndex = this.zIndex;
			this.containerNode.style.backgroundColor = "transparent";
		},
		
		onSetTime: function(){
			this.inputNode.value = dojo.date.format(this.timePicker.time, this.dateFormat);
			this.hideContainer();
		},
		
		onInputChange: function(){
			if (this.inputNode.value.length < 1) return;
			var tmp = new Date(this.inputNode.value);
			this.timePicker.time = tmp;
			this.timePicker.setDateTime(dojo.widget.TimePicker.util.toRfcDateTime(tmp));
			this.timePicker.initData();
			this.timePicker.initUI();
		}
	},
	"html"
);

dojo.widget.tags.addParseTreeHandler("dojo:dropdowndatetimepicker");
