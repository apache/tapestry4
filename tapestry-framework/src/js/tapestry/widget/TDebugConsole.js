dojo.provide("tapestry.widget.TDebugConsole");

dojo.require("dojo.widget.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.widget.Menu2");
dojo.require("dojo.lang");
dojo.require("dojo.html");
dojo.require("dojo.event.*");
dojo.widget.tags.addParseTreeHandler("dojo:tdebugConsole");

tapestry.widget.TDebugConsole=function(){
	dojo.widget.HtmlWidget.call(this);
	this.widgetType="TDebugConsole";
	this.isContainer=false;
};

dojo.inherits(tapestry.widget.TDebugConsole, dojo.widget.HtmlWidget);

dojo.lang.extend(tapestry.widget.TDebugConsole, {
	templatePath:dojo.uri.dojoUri("../tapestry/widget/templates/TDebugConsole.html"),
	templateCssPath:null,
	
	fillInTemplate:function(){
		document.getElementById("debug").appendChild(this.domNode);
		
		var mbar = dojo.widget.createWidget("MenuBar2", 
											{widgetId:"mbar"}, 
											this.menuBar);
		var mbaritem1 = dojo.widget.createWidget("MenuBarItem2", 
												{submenuId:"submenu1",
												caption:"View",widgetId:"mainFile"}, 
												this.menuBarItem1);
		mbar.addChild(mbaritem1);
		
		var popup = dojo.widget.createWidget("PopupMenu2", 
											{widgetId:"submenu1"}, 
											this.popupMenu2);
		var mitem = dojo.widget.createWidget("MenuItem2", 
											{widgetId:"mitem1",
											caption:"INFO"}, 
											this.popupMenuItem1);
		popup.addChild(mitem);
		
		dojo.event.connect(mitem, "onClick", function(e){alert('hello world');});
	},
	
	postCreate:function(){
	}
	
});