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
dojo.provide("dojo.widget.EditorTree");

dojo.require("dojo.event.*");
dojo.require("dojo.fx.html");
dojo.require("dojo.io.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.widget.EditorTreeNode");
dojo.require("dojo.widget.EditorTreeSelector");
dojo.require("dojo.widget.EditorTreeController");



// make it a tag
dojo.widget.tags.addParseTreeHandler("dojo:EditorTree");


dojo.widget.EditorTree = function() {
	dojo.widget.HtmlWidget.call(this);

	this.eventNames = {
		// new node built.. Well, just built
		nodeCreate: "",
		// expand icon clicked
		treeClick: "",
		// node icon clicked
		iconClick: "",
		// node title clicked
		titleClick: ""
	};

	this.tree = this;
	this.acceptDropSources = [];
	this.actionsDisabled = [];

}
dojo.inherits(dojo.widget.EditorTree, dojo.widget.HtmlWidget);

/* extend DOES NOT copy recursively */
dojo.lang.extend(dojo.widget.EditorTree, {
	widgetType: "EditorTree",

	isContainer: true,

	acceptDropSources: "",

	templateCssPath: dojo.uri.dojoUri("src/widget/templates/images/EditorTree/EditorTree.css"),

	templateString: '<div class="dojoTree"></div>',

	/* Model events */
	eventNames: null,
	eventNaming: "default",

	toggler: null,

	isExpanded: true, // consider this "root node" to be always expanded

	isTree: true,

	objectId: "",


	//
	// these icons control the grid and expando buttons for the whole tree
	//

	blankIconSrc: dojo.uri.dojoUri("src/widget/templates/images/EditorTree/treenode_blank.gif"),

	gridIconSrcT: dojo.uri.dojoUri("src/widget/templates/images/EditorTree/treenode_grid_t.gif"), // for non-last child grid
	gridIconSrcL: dojo.uri.dojoUri("src/widget/templates/images/EditorTree/treenode_grid_l.gif"), // for last child grid
	gridIconSrcV: dojo.uri.dojoUri("src/widget/templates/images/EditorTree/treenode_grid_v.gif"), // vertical line
	gridIconSrcP: dojo.uri.dojoUri("src/widget/templates/images/EditorTree/treenode_grid_p.gif"), // for under parent item child icons
	gridIconSrcC: dojo.uri.dojoUri("src/widget/templates/images/EditorTree/treenode_grid_c.gif"), // for under child item child icons
	gridIconSrcX: dojo.uri.dojoUri("src/widget/templates/images/EditorTree/treenode_grid_x.gif"), // grid for sole root item
	gridIconSrcY: dojo.uri.dojoUri("src/widget/templates/images/EditorTree/treenode_grid_y.gif"), // grid for last rrot item
	gridIconSrcZ: dojo.uri.dojoUri("src/widget/templates/images/EditorTree/treenode_grid_z.gif"), // for under root parent item child icon

	expandIconSrcPlus: dojo.uri.dojoUri("src/widget/templates/images/EditorTree/treenode_expand_plus.gif"),
	expandIconSrcMinus: dojo.uri.dojoUri("src/widget/templates/images/EditorTree/treenode_expand_minus.gif"),
	expandIconSrcLoading: dojo.uri.dojoUri("src/widget/templates/images/EditorTree/treenode_loading.gif"),


	iconWidth: 18,
	iconHeight: 18,


	//
	// tree options
	//

	showGrid: true,
	showRootGrid: true,

	toggle: "default",
	toggleDuration: 150,
	selector: null,


	actionIsDisabled: function(action) {
		var _this = this;
		return dojo.lang.inArray(_this.actionsDisabled, action)
	},


	actions: {
    	ADDCHILD: "ADDCHILD"
	},


	getInfo: function() {
		var _this = this;
		var info = {
			widgetId: _this.widgetId,
			objectId: _this.objectId
		}

		return info;
	},

	initialize: function(args, frag){

		var _this = this;
		//this.acceptDropSources = this.acceptDropSources.split(',');
		/*
		var sources;
		if ( (sources = args['acceptDropSources']) || (sources = args['acceptDropSources']) ) {

		}
		*/
		//this.actionsDisabled = this.actionsDisabled.split(",");
		for(var i=0; i<this.actionsDisabled.length; i++) {
			this.actionsDisabled[i] = this.actionsDisabled[i].toUpperCase();
		}


		switch (this.toggle) {

			case "fade": this.toggler = new dojo.widget.EditorTree.FadeToggle(); break;
			// buggy - try to open many nodes in FF (IE is ok)
			case "wipe": this.toggler = new dojo.widget.EditorTree.WipeToggle(); break;
			default    : this.toggler = new dojo.widget.EditorTree.DefaultToggle();
		}


		if (this.eventNaming == "default") { // IE || FF

			for (eventName in this.eventNames) {
				this.eventNames[eventName] = this.widgetId+"/"+eventName;
			}
			/*
			this.eventNames.watch("nodeCreate",
   				function (id,oldval,newval) {
      				alert("o." + id + " changed from " + oldval + " to " + newval);
      				return newval;
			   }
			  );
			 */

		//	alert(dojo.widget.manager.getWidgetById('firstTree').widgetId)
		//	alert(dojo.widget.manager.getWidgetById('firstTree').eventNames.nodeCreate);

		}


		if (args['controller']) {
			var controller = dojo.widget.manager.getWidgetById(args['controller']);

			controller.subscribeTree(this); // controller listens to my events
		}

		if (args['selector']) {
			this.selector = dojo.widget.manager.getWidgetById(args['selector']);
		} else {
			this.selector = new dojo.widget.createWidget("EditorTreeSelector");
		}

		this.containerNode = this.domNode;

	},



	postCreate: function() {
		this.buildTree();
	},

	buildTree: function() {

		dojo.html.disableSelection(this.domNode);

		for(var i=0; i<this.children.length; i++){

			this.children[i].isFirstNode = (i == 0) ? true : false;
			this.children[i].isLastNode = (i == this.children.length-1) ? true : false;
			this.children[i].parent = this; // root nodes have tree as parent

			var node = this.children[i].buildNode(this, 0);


			this.domNode.appendChild(node);
		}


		//
		// when we don't show root toggles, we need to auto-expand root nodes
		//

		if (!this.showRootGrid){
			for(var i=0; i<this.children.length; i++){
				this.children[i].expand();
			}
		}


	},



	/**
	 * Move child to newParent as last child
	 * redraw tree and update icons
	*/
	changeParent: function(child, newParent, index) {
//		dojo.debug("Move "+child+" to "+newParent+" index "+index)

		//dojo.debug(dojo.widget.manager.getWidgetById('1.3').containerNode.style.display);

		var destIdx = index;
/*
		if (child.parent === newParent && child.getParentIndex()<=index) {
			dojo.debug("shift dest index")
			destIdx--; // shift index cause child.length is less by 1 after removal
		}
	*/

		/* do actual parent change here. Write remove child first */
		child.parent.removeChild(child);

		newParent.addChild(child, destIdx);

	},

	removeChild: function(child) {

		var parent = child.parent;

		var children = parent.children;

		for(var i=0; i<children.length; i++){
			if(children[i] === child){
				if (children.length>1) {
					if (i==0) {
						children[i+1].isFirstNode = true;
					}
					if (i==children.length-1) {
						children[i-1].isLastNode = true;
					}
				}
				children.splice(i, 1);
				break;
			}
		}

		dojo.dom.removeNode(child.domNode);


		//dojo.debug("removeChild: "+child.title+" from "+parent.title);

		/*
		if (children.length == 0) {
			// toggle empty container off
			if (!parent.isTree) { // if has container
				parent.containerNode.style.display = 'none';
			}

		}
		*/

		parent.updateIconTree();



		return child;

	},


	// not called for initial tree building. See buildNode instead.
	// builds child html node if needed
	// index is "last node" by default
	addChild: function(child, index){


		dojo.profile.start("AddChild");

		if (dojo.lang.isUndefined(index)) {
			index = this.children.length;
		}


		//dojo.debug("This "+this+" Child "+child+" index "+index+" children.length "+this.children.length);

		//
		// this function gets called to add nodes to both trees and nodes, so it's a little confusing :)
		//

		if (!child.isTreeNode){
			dojo.raise("You can only add EditorTreeNode widgets to a "+this.widgetType+" widget!");
			return;
		}

		// set/clean isFirstNode and isLastNode
		if (this.children.length){
			if (index == 0) {
				this.children[0].isFirstNode = false;
				child.isFirstNode = true;
			} else {
				child.isFirstNode = false;
			}
			if (index == this.children.length) {
				this.children[index-1].isLastNode = false;
				child.isLastNode = true;
			} else {
				child.isLastNode = false;
			}
		} else {
			child.isLastNode = true;
			child.isFirstNode = true;
		}


		//dojo.debug("For new child set first:"+child.isFirstNode+" last:"+child.isLastNode);


		// usually it is impossible to change "isFolder" state, but if anyone wants to add a child to leaf,
		// it is possible program-way.
		if (this.isTreeNode){
			if (!this.isFolder) { // just became a folder.
				this.setFolder();
			}
		}

		// adjust tree
		var _this = this;
		dojo.lang.forEach(child.getDescendants(), function(elem) { elem.tree = _this.tree; });

		/*
		var stack = [child];
		var elem;
		// change tree for all subnodes
		while (elem = stack.pop()) {
			//dojo.debug("Tree for "+elem.title);
			elem.tree = tree;
			dojo.lang.forEach(elem.children, function(elem) { stack.push(elem); });
		}
		*/

		// fix parent
		child.parent = this;


		// no dynamic loading for those who are parents already
		if (this.isTreeNode) {
			this.state = this.loadStates.LOADED;
		}

		// if node exists - adjust its depth, otherwise build it
		if (child.domNodeInitialized) {
			//dojo.debug(this.widgetType)
			var d = this.isTreeNode ? this.depth : -1;
			//dojo.debug('Depth is '+this.depth);
			child.adjustDepth( d - child.depth + 1 );
		} else {
			child.depth = this.isTreeNode ? this.depth+1 : 0;
			child.buildNode(child.tree, child.depth);
		}


		//dojo.debug(child.domNode.outerHTML)

		if (index < this.children.length) {

			//dojo.debug('insert '+index)
			//dojo.debugShallow(child);

			// insert
			dojo.dom.insertBefore(child.domNode, this.children[index].domNode);
		} else {
			this.containerNode.appendChild(child.domNode);
		}

		/*
		if (index == this.children.length && this.children.length == 0) {
			this.containerNode.style.display = 'block';
		}
		*/

		this.children.splice(index, 0, child);

		//dojo.lang.forEach(this.children, function(child) { dojo.debug("Child "+child.title); } );

		//dojo.debugShallow(child);

		//this.expand();



		dojo.profile.end("AddChild");

		dojo.profile.start("updateIconTree");

		//this.updateIconTree();
		child.updateIcons();
		if (this.children.length>1) {
			if (child.isFirstNode) {
				this.children[1].updateIcons();
			}
			if (child.isLastNode) {
				this.children[this.children.length-2].updateIcons();
			}
		}

		dojo.profile.end("updateIconTree");


	},

	// adds an array of children in "batch mode" into empty node
	// optimized for fast loading of large collections into empty nodes
	addAllChildren: function(children) {

		if (children.length == 0) return;

		dojo.profile.start("addAllChildren");

		var _this = this;

		children[0].isFirstNode = true;
		children[children.length-1].isLastNode = true;

		if (this.isTreeNode){
			if (!this.isFolder) { // just became a folder.
				this.setFolder();
			}
		}

		dojo.lang.forEach(children,
			function(child) {
				child.parent = _this;
				child.buildNode(_this.tree, _this.isTreeNode ? _this.depth+1 : 0);
				_this.containerNode.appendChild(child.domNode);
			}

		);

		// no dynamic loading for those who are parents already
		if (this.isTreeNode) {
			this.state = this.loadStates.LOADED;
		}


		this.children = children;

		//this.updateIconTree();

		dojo.profile.end("addAllChildren");

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
	}





});



dojo.widget.EditorTree.DefaultToggle = function(){

	this.show = function(node){
		node.style.display = 'block';
	}

	this.hide = function(node){
		node.style.display = 'none';
	}
}

dojo.widget.EditorTree.FadeToggle = function(duration){
	this.toggleDuration = duration ? duration : 150;

	this.show = function(node){
		node.style.display = 'block';
		dojo.fx.html.fade(node, this.toggleDuration, 0, 1);
	}

	this.hide = function(node){
		dojo.fx.html.fadeOut(node, this.toggleDuration, function(node){ node.style.display = 'none'; });
	}
}

dojo.widget.EditorTree.WipeToggle = function(duration){
	this.toggleDuration = duration ? duration : 150;

	this.show = function(node){
		node.style.display = 'block';
		dojo.fx.html.wipeIn(node, this.toggleDuration);
	}

	this.hide = function(node){
		dojo.fx.html.wipeOut(node, this.toggleDuration, function(node){ node.style.display = 'none'; });
	}
}

