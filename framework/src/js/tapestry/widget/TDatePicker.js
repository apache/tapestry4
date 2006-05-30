dojo.provide("tapestry.widget.TDatePicker");

dojo.require("dojo.widget.DatePicker");
dojo.require("dojo.widget.html.DatePicker");

tapestry.widget.TDatePicker = function(){
	// this is just an interface that gets mixed in
	// mix in the DatePicker properties
	dojo.widget.html.DatePicker.call(this);
	this.widgetType = "TDatePicker";
	
	this.templateCssPath = "";
	
	this.onUpdateDate = function(evt) {
		this.formattedDate = this.storedDate;
	}
	
	dojo.event.connect(this, "onSetDate", this, "onUpdateDate");
	
	// Used by other classes to know when a calendar tr (ie date)
	// is double clicked
	this.onDoubleClick = function(evt) {
	}
	
	// Let's us override default DatePicker method so we can add event 
	// listeners to dom nodes that would otherwise swallow onClick events
	this.fillInTemplate = dojo.widget.html.DatePicker.prototype.fillInTemplate = function() {
		this.initData();
		this.initUI();
		
		var rows = this.calendarDatesContainerNode.getElementsByTagName("tr");
		if (rows) {
			for (var i = 0; i < rows.length; i++) {
				dojo.event.connect(rows[i], "ondblclick", this, "onDoubleClick");
			}
		}
	}
}

dojo.inherits(tapestry.widget.TDatePicker, dojo.widget.html.DatePicker);
dojo.widget.tags.addParseTreeHandler("dojo:tdatepicker");
