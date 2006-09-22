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
// Can be base class for TabContainer, Wizard, etc.
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
		dojo.widget.PageContainer.superclass.fillInTemplate.call(this, args, frag);
	},

	postCreate: function(args, frag) {
		// Setup each page panel
		dojo.lang.forEach(this.children, this._setupPage, this);

		// Display the selected page
		if(this.selectedPageWidget){
			this.selectPage(this.selectedPageWidget, true);
		}
	},

	addChild: function(child, overrideContainerNode, pos, ref, insertIndex){
		this._setupPage(child);
		dojo.widget.PageContainer.superclass.addChild.call(this,child, overrideContainerNode, pos, ref, insertIndex);

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
		dojo.widget.PageContainer.superclass.removeChild.call(this, page);

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
		this.onResized();

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
		// Summary:	callback when user tries to remove page from PageContainer
		var onc = page.extraArgs.onClose || page.extraArgs.onclose;
		var fcn = dojo.lang.isFunction(onc) ? onc : window[onc];
		var remove = dojo.lang.isFunction(fcn) ? fcn(this,page) : true;
		if(remove) {
			this.removeChild(page);
			// makes sure we can clean up executeScripts in ContentPane onUnLoad
			page.destroy();
		}
	},

	destroy: function(){
		dojo.event.topic.destroy(this.widgetId+"-addPane");
		dojo.event.topic.destroy(this.widgetId+"-removePane");
		dojo.event.topic.destroy(this.widgetId+"-selectPane");
		this.inherited("destroy");
	}
});
