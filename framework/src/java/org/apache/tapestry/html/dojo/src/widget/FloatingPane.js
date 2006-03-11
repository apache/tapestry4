/*
	Copyright (c) 2004-2005, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.FloatingPane");
dojo.provide("dojo.widget.html.FloatingPane");

//
// this widget provides a window-like floating pane
//

dojo.require("dojo.widget.*");
dojo.require("dojo.widget.Manager");
dojo.require("dojo.html");
dojo.require("dojo.html.shadow");
dojo.require("dojo.style");
dojo.require("dojo.dom");
dojo.require("dojo.widget.ContentPane");
dojo.require("dojo.dnd.HtmlDragMove");
dojo.require("dojo.dnd.HtmlDragMoveSource");
dojo.require("dojo.dnd.HtmlDragMoveObject");
dojo.require("dojo.widget.ResizeHandle");

dojo.widget.html.FloatingPane = function(){
	dojo.widget.html.ContentPane.call(this);
}

dojo.inherits(dojo.widget.html.FloatingPane, dojo.widget.html.ContentPane);

dojo.lang.extend(dojo.widget.html.FloatingPane, {
	widgetType: "FloatingPane",

	// Constructor arguments
	title: '',
	iconSrc: '',
	hasShadow: false,
	constrainToContainer: false,
	taskBarId: "",
	resizable: true,
	titleBarDisplay: "fancy",

	windowState: "normal",
	displayCloseAction: false,
	displayMinimizeAction: false,
	displayMaximizeAction: false,

	maxTaskBarConnectAttempts: 5,
	taskBarConnectAttempts: 0,

	templatePath: dojo.uri.dojoUri("src/widget/templates/HtmlFloatingPane.html"),
	templateCssPath: dojo.uri.dojoUri("src/widget/templates/HtmlFloatingPane.css"),

	fillInTemplate: function(args, frag){
		// Copy style info and id from input node to output node
		var source = this.getFragNodeRef(frag);
		this.domNode.style.cssText = source.style.cssText;
		dojo.html.addClass(this.domNode, dojo.html.getClass(source));
		if(dojo.render.html.safari){
			document.body.appendChild(this.domNode);
		}

		if(this.titleBarDisplay!="none"){	
			this.titleBar.style.display="";
			dojo.html.disableSelection(this.titleBar);

			this.titleBarIcon.style.display = (this.iconSrc=="" ? "none" : "");

			this.minimizeAction.style.display= (this.displayMinimizeAction ? "" : "none");
			this.maximizeAction.style.display= 
				(this.displayMaximizeAction && this.windowState!="maximized" ? "" : "none");
			this.restoreAction.style.display= 
				(this.displayMaximizeAction && this.windowState!="normal" ? "" : "none");
			this.closeAction.style.display= (this.displayCloseAction ? "" : "none");

			var drag = new dojo.dnd.HtmlDragMoveSource(this.domNode);	
			if (this.constrainToContainer) {
				drag.constrainTo();
			}
			drag.setDragHandle(this.titleBar);
		}

		if(this.resizable){
			this.resizeBar.style.display="";
			var rh = dojo.widget.createWidget("ResizeHandle", {targetElmId: this.widgetId, id:this.widgetId+"_resize"});
			this.resizeBar.appendChild(rh.domNode);
		}

		// make the content pane take all the remaining space
		this._setPadding();

		// add a drop shadow
		if(this.hasShadow){
			this.shadow=new dojo.html.shadow(this.domNode);
		}

		// Prevent IE bleed-through problem
		this.bgIframe = new dojo.html.BackgroundIframe(this.domNode);

		if( this.taskBarId ){
			this.taskBarSetup();
		}

		if (dojo.hostenv.post_load_) {
			this.setInitialWindowState();
		} else {
			dojo.addOnLoad(this, "setInitialWindowState");
		}
		if(dojo.render.html.safari){
			document.body.removeChild(this.domNode);
		}

		dojo.widget.html.FloatingPane.superclass.fillInTemplate.call(this, args, frag);
	},

	// Configure the content pane to take up all the space between the title bar and the resize bar
	_setPadding: function(){
		var t=dojo.style.getOuterHeight(this.titleBar);
		var b=dojo.style.getOuterHeight(this.resizeBar);
		if(t==0||b==0){
			// browser needs more time to compute sizes (maybe CSS hasn't downloaded yet)
			dojo.lang.setTimeout(this, this._setPadding, 50);
			return;
		}

		with(this.domNode.style){
			paddingTop=t+"px";
			paddingBottom=b+"px";
		}
	},

	maximizeWindow: function(evt) {
		this.previous={};
		var self=this;
		dojo.lang.forEach(["width", "height", "left", "top", "bottom", "right"],
			function(attr){ self.previous[attr] = self.domNode.style[attr]; });
		dojo.debugShallow(this.previous);

		this.domNode.style.left =
			dojo.style.getPixelValue(this.domNode.parentNode, "padding-left", true) + "px";
		this.domNode.style.top =
			dojo.style.getPixelValue(this.domNode.parentNode, "padding-top", true) + "px";

		if ((this.domNode.parentNode.nodeName.toLowerCase() == 'body')) {
			dojo.style.setOuterWidth(this.domNode, dojo.html.getViewportWidth()-dojo.style.getPaddingWidth(document.body));
			dojo.style.setOuterHeight(this.domNode, dojo.html.getViewportHeight()-dojo.style.getPaddingHeight(document.body));
		} else {
			dojo.style.setOuterWidth(this.domNode, dojo.style.getContentWidth(this.domNode.parentNode));
			dojo.style.setOuterHeight(this.domNode, dojo.style.getContentHeight(this.domNode.parentNode));
		}
		this.maximizeAction.style.display="none";
		this.restoreAction.style.display="";
		this.windowState="maximized";
	},

	minimizeWindow: function(evt) {
		this.hide();
		this.windowState = "minimized";
	},

	restoreWindow: function(evt) {
		for(var attr in this.previous){
			this.domNode.style[attr]=this.previous[attr];
		}
		this.previous=null;

		this.restoreAction.style.display="none";
		this.maximizeAction.style.display=this.displayMaximizeAction ? "" : "none";

		this.windowState="normal";
	},

	closeWindow: function(evt) {
		dojo.dom.removeNode(this.domNode);
		this.destroy();
	},

	onMouseDown: function(evt) {
		this.bringToTop();
	},

	bringToTop: function() {
		var floatingPaneStartingZ = 100;
		var floatingPanes= dojo.widget.manager.getWidgetsByType(this.widgetType);
		var windows = [];
		var y=0;
		for (var x=0; x<floatingPanes.length; x++) {
			if (this.widgetId != floatingPanes[x].widgetId) {
					windows.push(floatingPanes[x]);
			}
		}

		windows.sort(function(a,b) {
			return a.domNode.style.zIndex - b.domNode.style.zIndex;
		});
		
		windows.push(this);

		for (x=0; x<windows.length;x++) {
			windows[x].domNode.style.zIndex = floatingPaneStartingZ + x;
		}
	},

	setInitialWindowState: function() {
		if (this.windowState == "maximized") {
			this.maximizeWindow();
			this.show();
			return;
		}

		if (this.windowState=="normal") {
			dojo.lang.setTimeout(this, this.onResized, 50);
			this.show();
			return;
		}

		if (this.windowState=="minimized") {
			this.hide();
			return;
		}

		this.windowState="minimized";
	},

	// add icon to task bar, connected to me
	taskBarSetup: function() {
		var taskbar = dojo.widget.getWidgetById(this.taskBarId);
		if (!taskbar){
			if (this.taskBarConnectAttempts <  this.maxTaskBarConnectAttempts) {
				dojo.lang.setTimeout(this, this.taskBarSetup, 50);
				this.taskBarConnectAttempts++;
			} else {
				dojo.debug("Unable to connect to the taskBar");
			}
			return;
		}
		taskbar.addChild(this);
	},

	show: function(){
		if(!this.isVisible()){
			dojo.widget.html.FloatingPane.superclass.show.apply(this, arguments);
		}
		this.bringToTop();
	},

	onShow: function(){
		dojo.widget.html.FloatingPane.superclass.onShow.call(this);
		this.onResized();
	}
});

dojo.widget.tags.addParseTreeHandler("dojo:FloatingPane");
