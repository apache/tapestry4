/*
	Copyright (c) 2004-2005, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/


dojo.provide("dojo.widget.TreeSelector");

dojo.require("dojo.widget.HtmlWidget");


dojo.widget.tags.addParseTreeHandler("dojo:TreeSelector");


dojo.widget.TreeSelector = function() {
	dojo.widget.HtmlWidget.call(this);


	this.eventNames = {};

	this.listenedTrees = [];

}

dojo.inherits(dojo.widget.TreeSelector, dojo.widget.HtmlWidget);


dojo.lang.extend(dojo.widget.TreeSelector, {
	widgetType: "TreeSelector",
	selectedNode: null,


	eventNamesDefault: {
		select : "select",
		deselect : "deselect",
		dblselect: "dblselect" // select already selected node.. Edit or whatever
	},

	initialize: function() {

		for(name in this.eventNamesDefault) {
			if (dojo.lang.isUndefined(this.eventNames[name])) {
				this.eventNames[name] = this.widgetId+"/"+this.eventNamesDefault[name];
			}
		}

	},


	listenTree: function(tree) {
		dojo.event.topic.subscribe(tree.eventNames.titleClick, this, "select");
		dojo.event.topic.subscribe(tree.eventNames.iconClick, this, "select");
		dojo.event.topic.subscribe(tree.eventNames.collapse, this, "onCollapse");
		dojo.event.topic.subscribe(tree.eventNames.moveFrom, this, "onMoveFrom");
		dojo.event.topic.subscribe(tree.eventNames.removeChild, this, "onRemoveChild");
		/* remember all my trees to deselect when element is movedFrom them */
		this.listenedTrees.push(tree);
	},

	// deselect node if parent is collapsed
	onCollapse: function(message) {
		if (!this.selectedNode) return;

		var node = message.source;
		var parent = this.selectedNode.parent;
		while (parent !== node && parent.isTreeNode) {
			parent = parent.parent;
		}
		if (parent.isTreeNode) {
			this.deselect();
		}
	},



	select: function(message) {
		var node = message.source;
		var e = message.event;

		if (this.selectedNode === node) {
			dojo.event.topic.publish(this.eventNames.dblselect, { node: node });
			return;
		}

		if (this.selectedNode) {
			this.deselect();
		}

		this.doSelect(node);

		dojo.event.topic.publish(this.eventNames.select, {node: node} );
	},

	/**
	 * Deselect node if target tree is out of our concern
	 */
	onMoveFrom: function(message) {
		if (message.child !== this.selectedNode) {
			return;
		}

		if (!dojo.lang.inArray(this.listenedTrees, message.newTree)) {
			this.deselect();
		}
	},

	onRemoveChild: function(message) {
		if (message.child !== this.selectedNode) {
			return;
		}

		this.deselect();
	},

	doSelect: function(node){

		node.markSelected();

		this.selectedNode = node;
	},

	deselect: function(){

		var node = this.selectedNode;

		this.selectedNode = null;
		node.unMarkSelected();
		dojo.event.topic.publish(this.eventNames.deselect, {node: node} );

	}

});



