dojo.provide("tapestry.widget.DropDownDatePicker");

dojo.require("dojo.widget");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("tapestry.widget.TDatePicker");
dojo.require("dojo.event");
dojo.require("dojo.html");
dojo.require("dojo.date");

dojo.widget.tags.addParseTreeHandler("dojo:dropdowndatepicker");

tapestry.widget.DropDownDatePicker = function(){
	
	dojo.widget.HtmlWidget.call(this);
	this.widgetType = "DropDownDatePicker";
	this.subWidgetType="";
	this.templateString = '<div><input type="text" value="" style="vertical-align:middle;" dojoAttachPoint="valueInputNode" /> <img src="" alt="" dojoAttachPoint="containerDropdownNode" dojoAttachEvent="onclick: onDropdown;" style="vertical-align:middle; cursor:pointer; cursor:hand;" /><div dojoAttachPoint="subWidgetContainerNode" class="dropDownContainer"><div dojoAttachPoint="subWidgetNode" class="subWidgetContainer"></div></div></div>';
	this.templateCssPath = "";
	
	this.fillInTemplate = function() {
		this.initUI();
	}
	
	this.initUI = function() {
		this.subWidgetContainerNode.style.left = "";
		this.subWidgetContainerNode.style.top = "";
		var properties = {
			widgetContainerId: this.widgetId
		}
		
		this.valueInputNode.style.width=this.inputWidth;
		this.containerDropdownNode.src = this.dateIconPath;
		this.containerDropdownNode.alt = "date";
		this.containerDropdownNode.title = "select a date";
		this.subWidgetRef = dojo.widget.createWidget("TDatePicker", properties, this.subWidgetNode);
		dojo.event.connect(this.subWidgetRef, "onUpdateDate", this, "onPopulate");
		dojo.event.connect(this.valueInputNode, "onchange", this, "onInputChange");
		this.onUpdateDate = function(evt) {
			this.storedDate = evt.storedDate;
		}
		dojo.event.connect(this.subWidgetRef, "onDoubleClick", this, "show");
	}
	
	this.onDropdown = function(evt) {
		this.show(this.subWidgetContainerNode.style.display == "block");
	}
	
	this.show = function(bool) {
		this.subWidgetContainerNode.style.display = (bool) ? "none" : "block";
	}
	
	this.onHide = function(evt) {
		this.show(false);
	}
	
	this.onPopulate = function() {
		this.valueInputNode.value = dojo.date.format(this.subWidgetRef.date, this.dateFormat);
	}
	
	this.onInputChange = function(){
		var test=new Date(this.valueInputNode.value);
		this.subWidgetRef.date=test;
		this.subWidgetRef.setDate(dojo.widget.DatePicker.util.toRfcDate(test));
		this.onPopulate();
	}
}

dojo.inherits(tapestry.widget.DropDownDatePicker, dojo.widget.HtmlWidget);

dojo.lang.extend(tapestry.widget.DropDownDatePicker, {
	//	default attributes
	dateFormat:"%m/%d/%Y", //see <http://www.opengroup.org/onlinepubs/007908799/xsh/strftime.html>
	dateIconPath:"templates/images/dateIcon.gif",
	inputWidth:"6em"
});
