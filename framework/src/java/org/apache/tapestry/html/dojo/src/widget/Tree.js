/*
	Copyright (c) 2004-2005, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

/**
 * Tree model does all the drawing, visual node management etc.
 * Throws events about clicks on it, so someone may catch them and process
 * Tree knows nothing about DnD stuff, covered in TreeDragAndDrop and (if enabled) attached by controller
*/
dojo.provide("dojo.widget.Tree");

dojo.require("dojo.event.*");
dojo.require("dojo.io.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.widget.TreeNode");



// make it a tag
dojo.widget.tags.addParseTreeHandler("dojo:Tree");


dojo.widget.Tree = function() {
	dojo.widget.HtmlWidget.call(this);

	this.eventNames = {};

	this.tree = this;
	this.DNDAcceptTypes = [];
	this.actionsDisabled = [];

}
dojo.inherits(dojo.widget.Tree, dojo.widget.HtmlWidget);

dojo.lang.extend(dojo.widget.Tree, {
	widgetType: "Tree",

	eventNamesDefault: {
		// new child does not get domNode filled in (only template draft)
		// until addChild->createDOMNode is called(program way) OR createDOMNode (html-way)
		// hook events to operate on new DOMNode, create dropTargets etc
		createDOMNode: "createDOMNode",
		// tree created.. Perform tree-wide actions if needed
		treeCreate: "treeCreate",
		// expand icon clicked
		treeClick: "treeClick",
		// node icon clicked
		iconClick: "iconClick",
		// node title clicked
		titleClick: "titleClick",

		moveFrom: "moveFrom",
		moveTo: "moveTo",
		addChild: "addChild",
		removeChild: "removeChild",
		expand: "expand",
		collapse: "collapse"
	},

	isContainer: true,

	DNDMode: "off",

	DNDModes: {
		BETWEEN: 1,
		ONTO: 2
	},

	DNDAcceptTypes: "",

	templateCssPath: dojo.uri.dojoUri("src/widget/templates/images/Tree/Tree.css"),

	templateString: '<div class="dojoTree"></div>',

	isExpanded: true, // consider this "root node" to be always expanded

	isTree: true,

	objectId: "",

	// autoCreate if not "off"
	// used to get the autocreated controller ONLY.
	// generally, tree DOES NOT KNOW about its CONTROLLER, it just doesn't care
	// controller gets messages via dojo.event
	controller: "",

	// autoCreate if not "off"
	// used to get the autocreated selector ONLY.
	// generally, tree DOES NOT KNOW its SELECTOR
	// binding is made with dojo.event
	selector: "",

	// used ONLY at initialization time
	menu: "", // autobind menu if menu's widgetId is set here

	expandLevel: "", // expand to level automatically

	//
	// these icons control the grid and expando buttons for the whole tree
	//

	blankIconSrc: dojo.uri.dojoUri("src/widget/templates/images/Tree/treenode_blank.gif"),

	gridIconSrcT: dojo.uri.dojoUri("src/widget/templates/images/Tree/treenode_grid_t.gif"), // for non-last child grid
	gridIconSrcL: dojo.uri.dojoUri("src/widget/templates/images/Tree/treenode_grid_l.gif"), // for last child grid
	gridIconSrcV: dojo.uri.dojoUri("src/widget/templates/images/Tree/treenode_grid_v.gif"), // vertical line
	gridIconSrcP: dojo.uri.dojoUri("src/widget/templates/images/Tree/treenode_grid_p.gif"), // for under parent item child icons
	gridIconSrcC: dojo.uri.dojoUri("src/widget/templates/images/Tree/treenode_grid_c.gif"), // for under child item child icons
	gridIconSrcX: dojo.uri.dojoUri("src/widget/templates/images/Tree/treenode_grid_x.gif"), // grid for sole root item
	gridIconSrcY: dojo.uri.dojoUri("src/widget/templates/images/Tree/treenode_grid_y.gif"), // grid for last rrot item
	gridIconSrcZ: dojo.uri.dojoUri("src/widget/templates/images/Tree/treenode_grid_z.gif"), // for under root parent item child icon

	expandIconSrcPlus: dojo.uri.dojoUri("src/widget/templates/images/Tree/treenode_expand_plus.gif"),
	expandIconSrcMinus: dojo.uri.dojoUri("src/widget/templates/images/Tree/treenode_expand_minus.gif"),
	expandIconSrcLoading: dojo.uri.dojoUri("src/widget/templates/images/Tree/treenode_loading.gif"),


	iconWidth: 18,
	iconHeight: 18,


	//
	// tree options
	//

	showGrid: true,
	showRootGrid: true,

	actionIsDisabled: function(action) {
		var _this = this;
		return dojo.lang.inArray(_this.actionsDisabled, action)
	},


	actions: {
    	ADDCHILD: "ADDCHILD"
	},


	getInfo: function() {
		var info = {
			widgetId: this.widgetId,
			objectId: this.objectId
		}

		return info;
	},

	initializeController: function() {
		if (this.controller != "off") {
			if (this.controller) {
				this.controller = dojo.widget.byId(this.controller);
			}
			else {
				// create default controller here
				dojo.require("dojo.widget.TreeBasicController");
				this.controller = dojo.widget.createWidget("TreeBasicController",
					{ DNDController: this.DNDMode ? "create" : "" }
				 );

			}
			this.controller.listenTree(this); // controller listens to my events

		} else {
			this.controller = null;
		}
	},

	initializeSelector: function() {

		if (this.selector != "off") {
			if (this.selector) {
				this.selector = dojo.widget.byId(this.selector);
			}
			else {
				// create default controller here
				dojo.require("dojo.widget.TreeSelector");
				this.selector = dojo.widget.createWidget("TreeSelector");
			}

			this.selector.listenTree(this);
		} else {
			this.selector = null;
		}
	},

	initialize: function(args, frag){

		var _this = this;

		for(name in this.eventNamesDefault) {
			if (dojo.lang.isUndefined(this.eventNames[name])) {
				this.eventNames[name] = this.widgetId+"/"+this.eventNamesDefault[name];
			}
		}

		for(var i=0; i<this.actionsDisabled.length; i++) {
			this.actionsDisabled[i] = this.actionsDisabled[i].toUpperCase();
		}

		if (this.DNDMode == "off") {
			this.DNDMode = 0;
		} else if (this.DNDMode == "between") {
			this.DNDMode = this.DNDModes.ONTO | this.DNDModes.BETWEEN;
		} else if (this.DNDMode == "onto") {
			this.DNDMode = this.DNDModes.ONTO;
		}

		this.expandLevel = parseInt(this.expandLevel);

		this.initializeController();
		this.initializeSelector();

		if (this.menu) {
			this.menu = dojo.widget.byId(this.menu);
			this.menu.listenTree(this);
		}


		this.containerNode = this.domNode;

	},


	postCreate: function() {
		this.createDOMNode();
	},


	createDOMNode: function() {

		dojo.html.disableSelection(this.domNode);

		for(var i=0; i<this.children.length; i++){
			this.children[i].parent = this; // root nodes have tree as parent

			var node = this.children[i].createDOMNode(this, 0);


			this.domNode.appendChild(node);
		}


		if (!this.showRootGrid){
			for(var i=0; i<this.children.length; i++){
				this.children[i].expand();
			}
		}

		dojo.event.topic.publish(this.tree.eventNames.treeCreate, { source: this } );

	},


	addChild: function(child, index) {
		var message = {
			child: child,
			index: index,
			parent: this,
			// remember if dom was already initialized
			// initialized => no createDOMNode => no createDOMNode event
			domNodeInitialized: child.domNodeInitialized
		}

		this.doAddChild.apply(this, arguments);

		dojo.event.topic.publish(this.tree.eventNames.addChild, message);
	},


	// not called for initial tree building. See createDOMNode instead.
	// builds child html node if needed
	// index is "last node" by default
	/**
	 * FIXME: Is it possible that removeNode from the tree will cause leaks cause of attached events ?
	 * if yes, then only attach events in addChild and detach in remove.. Seems all ok yet.
	*/
	doAddChild: function(child, index){

		//dojo.debug("doAddChild "+index+" called for "+child);


		if (dojo.lang.isUndefined(index)) {
			index = this.children.length;
		}

		if (!child.isTreeNode){
			dojo.raise("You can only add TreeNode widgets to a "+this.widgetType+" widget!");
			return;
		}

		// usually it is impossible to change "isFolder" state, but if anyone wants to add a child to leaf,
		// it is possible program-way.
		if (this.isTreeNode){
			if (!this.isFolder) { // just became a folder.
				//dojo.debug("becoming folder "+this);
				this.setFolder();
			}
		}

		// adjust tree
		var _this = this;
		dojo.lang.forEach(child.getDescendants(), function(elem) { elem.tree = _this.tree; });

		// fix parent
		child.parent = this;


		// no dynamic loading for those who become parents
		if (this.isTreeNode) {
			this.state = this.loadStates.LOADED;
		}


		// add new child into DOM after it was added into children
		if (index < this.children.length) { // children[] already has child
			//dojo.debug("Inserting before "+this.children[index].title);
			dojo.dom.insertBefore(child.domNode, this.children[index].domNode);
		} else {
			this.containerNode.appendChild(child.domNode);
			if (this.isExpanded && this.isTreeNode) {
				/* When I add children to hidden containerNode => show container w/ them */
				this.showChildren();
			}
		}


		this.children.splice(index, 0, child);

		//dojo.debugShallow(this.children);


		// if node exists - adjust its depth, otherwise build it
		if (child.domNodeInitialized) {
			var d = this.isTreeNode ? this.depth : -1;
			child.adjustDepth( d - child.depth + 1 );


			// update icons to link generated dom with Tree => updateParentGrid
			// if I moved child from LastNode inside the tree => need to link it up'n'down =>
			// updateExpandGridColumn
			// if I change depth => need to update all grid..
			child.updateIconTree();
		} else {
			//dojo.debug("Create domnode ");
			child.depth = this.isTreeNode ? this.depth+1 : 0;
			child.createDOMNode(child.tree, child.depth);
		}


		// Use-case:
		// When previous sibling was created => it was last, no children after it
		// so it did not create link down => let's add it for all descendants
		// Use-case:
		// a child was moved down under the last node so last node should be updated
		var prevSibling = child.getPreviousSibling();
		if (child.isLastNode() && prevSibling) {
			prevSibling.updateExpandGridColumn();
		}


		//dojo.debug("Added child "+child);



	},




	makeBlankImg: function() {
		var img = document.createElement('img');

		img.style.width = this.iconWidth + 'px';
		img.style.height = this.iconHeight + 'px';
		img.src = this.blankIconSrc;
		img.style.verticalAlign = 'middle';

		return img;
	},


	updateIconTree: function(){

		//dojo.debug("Update icons for "+this)
		if (!this.isTree) {
			this.updateIcons();
		}

		for(var i=0; i<this.children.length; i++){
			this.children[i].updateIconTree();
		}

	},

	toString: function() {
		return "["+this.widgetType+" ID:"+this.widgetId+"]"
	},




	/**
	 * Move child to newParent as last child
	 * redraw tree and update icons.
	 *
	 * Called by target, saves source in event.
	 * events are published for BOTH trees AFTER update.
	*/
	move: function(child, newParent, index) {

		//dojo.debug(child+" "+newParent+" at "+index);

		var oldParent = child.parent;
		var oldTree = child.tree;

		this.doMove.apply(this, arguments);

		var newParent = child.parent;
		var newTree = child.tree;

		var message = {
				oldParent: oldParent, oldTree: oldTree,
				newParent: newParent, newTree: newTree,
				child: child
		};

		/* publish events here about structural changes for both source and target trees */
		dojo.event.topic.publish(oldTree.eventNames.moveFrom, message);
		dojo.event.topic.publish(newTree.eventNames.moveTo, message);

	},


	/* do actual parent change here. Write remove child first */
	doMove: function(child, newParent, index) {
		//var parent = child.parent;
		child.parent.doRemoveChild(child);

		newParent.doAddChild(child, index);
	},



// ================================ removeChild ===================================

	removeChild: function(child) {
		if (!child.parent) return;

		var oldTree = child.tree;
		var oldParent = child.parent;

		var removedChild = this.doRemoveChild.apply(this, arguments);


		dojo.event.topic.publish(this.tree.eventNames.removeChild,
			{ child: removedChild, tree: oldTree, parent: oldParent }
		);

		return removedChild;
	},


	doRemoveChild: function(child) {
		if (!child.parent) return;

		var parent = child.parent;

		var children = parent.children;


		var index = child.getParentIndex();
		if (index < 0) {
			dojo.raise("Couldn't find node "+child+" for removal");
		}


		children.splice(index,1);
		dojo.dom.removeNode(child.domNode);

		// if WAS last node (children.length decreased already) and has prevSibling
		if (index == children.length && index>0) {
			children[index-1].updateExpandGridColumn();
		}

		//parent.updateIconTree();


		child.parent = child.tree = null;

		return child;
	},

// ================================ destroyChild ===================================


	destroyChild: function(child) {
		dojo.debug("destroyed");
		//dojo.debugShallow(child);
		child.parent.removeChild(child);
		child.cleanUp();
		delete child;
	}


});


