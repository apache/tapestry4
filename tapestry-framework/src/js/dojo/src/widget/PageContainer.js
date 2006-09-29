/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.PageContainer");

dojo.require("dojo.lang.func");
dojo.require("dojo.widget.*");
dojo.require("dojo.event.*");
dojo.require("dojo.html.selection");

// A PageContainer is a container that has multiple panes, but shows only
// one pane at a time.
//
// Publishes topics <widgetId>-addPane, <widgetId>-removePane, and <widgetId>-selectPane
//
// Can be base class for container, Wizard, Show, etc.
dojo.widget.defineWidget("dojo.widget.PageContainer", dojo.widget.HtmlWidget, {
	isContainer: true,

	// Boolean
	//  if true, change the size of my currently displayed child to match my size
	doLayout: true,

	templateString: "<div dojoAttachPoint='containerNode'></div>",

	// String
	//   id of the initially shown page
	selectedPage: "",

	fillInTemplate: function(args, frag) {
		// Copy style info from input node to output node
		var source = this.getFragNodeRef(frag);
		dojo.html.copyStyle(this.domNode, source);
		dojo.widget.PageContainer.superclass.fillInTemplate.apply(this, arguments);
	},

	postCreate: function(args, frag) {
		// Setup each page panel
		dojo.lang.forEach(this.children, this._setupPage, this);

		// Display the selected page
		if(this.selectedPageWidget){
			this.selectPage(this.selectedPageWidget, true);
		}
	},

	addChild: function(child){
		this._setupPage(child);
		dojo.widget.PageContainer.superclass.addChild.apply(this, arguments);

		// in case the page labels have overflowed from one line to two lines
		this.onResized();
	},

	_setupPage: function(page){
		// Summary: Add the given pane to this page container
		page.domNode.style.display="none";

		if(!this.selectedPageWidget || this.selectedPage==page.widgetId || page.selected || (this.children.length==0)){
			// Deselect old page and select new one
			// We do this instead of calling selectPage in this case, because other wise other widgets
			// listening for addChild and selectPage can run into a race condition
			if(this.selectedPageWidget){
				this._hidePage(this.selectedPageWidget);
			}
			this.selectedPageWidget = page;
			this._showPage(page);

		} else {
			this._hidePage(page);
		}

		dojo.html.addClass(page.domNode, "selected");

		// publish the addPane event for panes added via addChild(), and the original panes too
		dojo.event.topic.publish(this.widgetId+"-addPane", page);
	},

	removeChild: function(/* Widget */page){
		dojo.widget.PageContainer.superclass.removeChild.apply(this, arguments);

		// If we are being destroyed than don't the code below (to select another pane), because we are deleting
		// every pane one by one
		if(this._beingDestroyed){ return; }

		// this will notify any tablists to remove a button; do this first because it may affect sizing
		dojo.event.topic.publish(this.widgetId+"-removePane", page);

		if (this.selectedPageWidget === page) {
			this.selectedPageWidget = undefined;
			if (this.children.length > 0) {
				this.selectPage(this.children[0], true);
			}
		}
	},

	selectPage: function(/* Widget */ page, /* Boolean */ _noRefresh, /* Widget */ callingWidget){
		// summary
		//	Show the given widget (which must be one of my children)
		page = dojo.widget.byId(page);
		this.correspondingPageButton = callingWidget;

		// Deselect old page and select new one
		if(this.selectedPageWidget){
			this._hidePage(this.selectedPageWidget);
		}
		this.selectedPageWidget = page;
		this._showPage(page, _noRefresh);
		page.isFirstPage = (page == this.children[0]);
		page.isLastPage = (page == this.children[this.children.length-1]);
		dojo.event.topic.publish(this.widgetId+"-selectPane", page);
	},

	nextPage: function(){
		// Summary: advance to next page
		var index = dojo.lang.find(this.children, this.selectedPageWidget);
		this.selectPage(this.children[index+1]);
	},

	previousPage: function(){
		// Summary: go back to previous page
		var index = dojo.lang.find(this.children, this.selectedPageWidget);
		this.selectPage(this.children[index-1]);
	},

	onResized: function(){
		// Summary: called when any page is shown, to make it fit the container correctly
		if(this.doLayout && this.selectedPageWidget){
			with(this.selectedPageWidget.domNode.style){
				top = dojo.html.getPixelValue(this.containerNode, "padding-top", true);
				left = dojo.html.getPixelValue(this.containerNode, "padding-left", true);
			}
			var content = dojo.html.getContentBox(this.containerNode);
			this.selectedPageWidget.resizeTo(content.width, content.height);
		}
	},

	_showPage: function(page, _noRefresh) {
		page.selected=true;

		// size the current page (in case this is the first time it's being shown, or I have been resized)
		if(this.doLayout){
			var content = dojo.html.getContentBox(this.containerNode);
			page.resizeTo(content.width, content.height);
		}

		// make sure we dont refresh onClose and on postCreate
		// speeds up things a bit when using refreshOnShow and fixes #646
		if(_noRefresh && page.refreshOnShow){
			var tmp = page.refreshOnShow;
			page.refreshOnShow = false;
			page.show();
			page.refreshOnShow = tmp;
		}else{
			page.show();
		}
	},

	_hidePage: function(page) {
		page.selected=false;
		page.hide();
	},

	closePage: function(page) {
		// summary
		//	callback when user clicks the [X] to remove a page
		//	if onClose() returns true then remove and destroy the childd
		var remove = page.onClose(this, page);
		if(remove) {
			this.removeChild(page);
			// makes sure we can clean up executeScripts in ContentPane onUnLoad
			page.destroy();
		}
	},

	destroy: function(){
		this._beingDestroyed = true;
		dojo.event.topic.destroy(this.widgetId+"-addPane");
		dojo.event.topic.destroy(this.widgetId+"-removePane");
		dojo.event.topic.destroy(this.widgetId+"-selectPane");
		dojo.widget.PageContainer.superclass.destroy.apply(this, arguments);
	}
});


// PageController - set of buttons to select the page in a page list
// When intialized, the PageController monitors the container, and whenever a pane is
// added or deleted updates itself accordingly.
dojo.widget.defineWidget(
    "dojo.widget.PageController",
    dojo.widget.HtmlWidget,
	{
		templateString: "<span wairole='tablist' dojoAttachEvent='onKey'></span>",
		isContainer: true,

		// String
		//	the id of the page container that I point to
		containerId: "",

		// String
		//	the name of the button widget to create to correspond to each page
		buttonWidget: "PageButton",

		// String
		//	Class name to apply to the top dom node
		"class": "dojoPageController",

		fillInTemplate: function() {
			dojo.html.addClass(this.domNode, this["class"]);  // "class" is a reserved word in JS
			dojo.widget.wai.setAttr(this.domNode, "waiRole", "role", "tablist");
		},

		postCreate: function(){
			this.pane2button = {};		// mapping from panes to buttons

			// If children have already been added to the page container then create buttons for them
			var container = dojo.widget.byId(this.containerId);
			if(container){
				dojo.lang.forEach(container.children, this.onAddPage, this);
			}

			dojo.event.topic.subscribe(this.containerId+"-addPane", this, "onAddPage");
			dojo.event.topic.subscribe(this.containerId+"-removePane", this, "onRemovePage");
			dojo.event.topic.subscribe(this.containerId+"-selectPane", this, "onSelectPage");
		},

		destroy: function(){
			dojo.event.topic.unsubscribe(this.containerId+"-addPane", this, "onAddPage");
			dojo.event.topic.unsubscribe(this.containerId+"-removePane", this, "onRemovePage");
			dojo.event.topic.unsubscribe(this.containerId+"-selectPane", this, "onSelectPage");
			dojo.widget.PageController.superclass.destroy.apply(this, arguments);
		},

		onAddPage: function(/* Widget */ pane){
			// summary
			//   Called whenever a pane is added to the container.
			//   Create button corresponding to the pane.
			var button = dojo.widget.createWidget(this.buttonWidget,
				{
					label: pane.label,
					closable: pane.tabCloseButton
				});
			this.addChild(button);
			this.domNode.appendChild(button.domNode);
			this.pane2button[pane]=button;
			pane.tabButton = button;	// this value might be overwritten if two tabs point to same container

			var _this = this;
			dojo.event.connect(button, "onClick", function(){ _this.onButtonClick(pane); });
			dojo.event.connect(button, "onCloseButtonClick", function(){ _this.onCloseButtonClick(pane); });
		},

		onRemovePage: function(/* Widget */ pane){
			// summary
			//   Called whenever a pane is removed from the container.
			//   Remove the button corresponding to the pane.
			if(this.currentPane == pane){ this.currentPane = null; }
			var button = this.pane2button[pane];
			button.destroy();
			this.pane2button[pane] = null;
		},

		onSelectPage: function(/*Widget*/ page){
			// Summary
			//	Called when a page has been selected in the PageContainer, either by me or by someone another PageController
			if(this.currentPane){
				var oldButton=this.pane2button[this.currentPane];
				oldButton.clearSelected();
			}
			var newButton=this.pane2button[page];
			newButton.setSelected();
			this.currentPane=page;
		},

		onButtonClick: function(/*Widget*/ page){
			// summary
			//   Called whenever one of my child buttons is pressed in an attempt to select a pane
			var container = dojo.widget.byId(this.containerId);	// TODO: do this via topics?
			container.selectPage(page, false, this);
		},

		onCloseButtonClick: function(/*Widget*/ page){
			// summary
			//   Called whenever one of my child buttons [X] is pressed in an attempt to close a pane
			var container = dojo.widget.byId(this.containerId);
			container.closePage(page);
		},

		onKey: function(evt){
			// summary:
			//   Handle keystrokes on the page list, for advancing to next/previous button

			if( (evt.keyCode == evt.KEY_RIGHT_ARROW)||
				(evt.keyCode == evt.KEY_LEFT_ARROW) ){
				var current = 0;
				var next = null;	// the next button to focus on
				
				// find currently focused button in children array
				var current = dojo.lang.find(this.children, this._currentPage);
				
				// pick next button to focus on
				if(evt.keyCode == evt.KEY_RIGHT_ARROW){
					next = this.children[ (current+1) % this.children.length ]; 
				}else{ // is LEFT_ARROW
					next = this.children[ (current+ (this.children.length-1)) % this.children.length ];
				}
				
				dojo.event.browser.stopEvent(evt);
				next.onClick();
			}
		}
	}
);

// PageButton (the thing you click to select or delete a page)
dojo.widget.defineWidget("dojo.widget.PageButton", dojo.widget.HtmlWidget,
{
	templateString: "<span class='item'>" +
						"<span dojoAttachEvent='onClick' dojoAttachPoint='titleNode' class='selectButton'>${this.label}</span>" +
						"<span dojoAttachEvent='onClick:onCloseButtonClick' class='closeButton'>[X]</span>" +
					"</span>",

	// String
	//  Name to print on the button
	label: "foo",
	
	// Boolean
	//	true iff we should also print a close icon to destroy corresponding pane
	closable: false,

	onClick: function(){
		// summary
		//  Basically this is the attach point PageController listens to, to select the page
		this.focus();
	},

	onCloseButtonMouseOver: function(){
		// summary
		//	The close button changes color a bit when you mouse over	
		dojo.html.addClass(this.closeButtonNode, "closeHover");
	},

	onCloseButtonMouseOut: function(){
		// summary
		// 	Revert close button to normal color on mouse out
		dojo.html.removeClass(this.closeButtonNode, "closeHover");
	},

	onCloseButtonClick: function(evt){
		// summary
		//	Handle clicking the close button for this tab
	},
	
	setSelected: function(){
		// summary
		//	This is run whenever the pane corresponding to this button has been selected
		dojo.html.addClass(this.domNode, "current");
		this.titleNode.setAttribute("tabIndex","0");
	},
	
	clearSelected: function(){
		// summary
		//	This function is run whenever the pane corresponding to this button has been deselected (and another pane has been shown)
		dojo.html.removeClass(this.domNode, "current");
		this.titleNode.setAttribute("tabIndex","-1");
	},

	focus: function(){
		// summary
		//	This will focus on the this button (for accessibility you need to do this when the button is selected)
		if(this.titleNode.focus){	// mozilla 1.7 doesn't have focus() func
			this.titleNode.focus();
		}
		this.parent._currentPage = this;
	}
});

// These arguments can be specified for the children of a PageContainer.
// Since any widget can be specified as a PageContainer child, mix them
// into the base widget class.  (This is a hack, but it's effective.)
dojo.lang.extend(dojo.widget.Widget, {
	label: "",
	selected: false,	// is this tab currently selected?
	closeButton: false,
	onClose: function(){ return true; }	// callback if someone tries to close the child, child will be closed if func returns true
});
