/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.TreeContextMenuV3");

dojo.require("dojo.event.*");
dojo.require("dojo.io.*");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.Menu2");
dojo.require("dojo.widget.TreeCommon");

dojo.widget.defineWidget(
	"dojo.widget.TreeContextMenuV3",
	[dojo.widget.PopupMenu2, dojo.widget.TreeCommon],
	function() {
		this.listenedTrees = {};
	},
{
	open: function(x, y, parentMenu, explodeSrc){

		var result = dojo.widget.PopupMenu2.prototype.open.apply(this, arguments);

		/* publish many events here about structural changes */
		dojo.event.topic.publish(this.eventNames.open, { menu:this });

		return result;
	},

	listenTreeEvents: ["afterChangeTree","beforeTreeDestroy"],
	listenNodeFilter: function(elem) { return elem instanceof dojo.widget.Widget},
	
	onBeforeTreeDestroy: function(message) {
		this.unlistenTree(message.source);
	},


	onAfterChangeTree: function(message) {
		//dojo.debugShallow(message);
				
		            
		if (!message.newTree || !this.listenedTrees[message.newTree.widgetId]) {
			// I got this message because node leaves me (oldtree)
			this.processDescendants(message.node, this.listenNodeFilter, this.unlistenNode);
		}		
		
		if (!message.oldTree || !this.listenedTrees[message.oldTree.widgetId]) {
			// we have new node
			this.processDescendants(message.node, this.listenNodeFilter, this.listenNode);
		}
		
		//dojo.profile.end("onTreeChange");
	},

	
	listenNode: function(node) {
		this.bindDomNode(node.labelNode);
	},


	unlistenNode: function(node) {
		this.unBindDomNode(node.labelNode);
	}
});


dojo.widget.defineWidget(
	"dojo.widget.TreeMenuItemV3",
	[dojo.widget.MenuItem2, dojo.widget.TreeCommon],
	function() {
		this.treeActions = [];
	},
{
	// treeActions menu item performs following actions (to be checked for permissions)
	treeActions: "",

	initialize: function(args, frag) {
		for(var i=0; i<this.treeActions.length; i++) {
			this.treeActions[i] = this.treeActions[i].toUpperCase();
		}
	},

	getTreeNode: function() {
		var menu = this;

		// FIXME: change to dojo.widget[this.widgetType]
		while (! (menu instanceof dojo.widget.TreeContextMenuV3) ) {
			menu = menu.parent;
		}

		var source = menu.getTopOpenEvent().target;

		var treeNode = this.domElement2TreeNode(source);
		
		return treeNode;
	},


	menuOpen: function(message) {
		var treeNode = this.getTreeNode();

		this.setDisabled(false); // enable by default

		var _this = this;
		dojo.lang.forEach(_this.treeActions,
			function(action) {
				_this.setDisabled( treeNode.actionIsDisabledNow(action) );
			}
		);

	},

	toString: function() {
		return "["+this.widgetType+" node "+this.getTreeNode()+"]";
	}
});
