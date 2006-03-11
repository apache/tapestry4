/*
	Copyright (c) 2004-2005, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/


dojo.provide("dojo.widget.TreeRPCController");

dojo.require("dojo.event.*");
dojo.require("dojo.json")
dojo.require("dojo.io.*");
dojo.require("dojo.widget.TreeLoadingController");


dojo.widget.tags.addParseTreeHandler("dojo:TreeRPCController");


dojo.widget.TreeRPCController = function() {
	dojo.widget.TreeLoadingController.call(this);
}

dojo.inherits(dojo.widget.TreeRPCController, dojo.widget.TreeLoadingController);


dojo.lang.extend(dojo.widget.TreeRPCController, {
	widgetType: "TreeRPCController",

	/**
	 * Make request to server about moving children.
	 *
	 * Request returns "true" if move succeeded,
	 * object with error field if failed
	 *
	 * I can't leave DragObject floating until async request returns, need to return false/true
	 * so making it sync way...
	 *
	 * Also, "loading" icon is not shown until function finishes execution, so no indication for remote request.
	*/
	doMove: function(child, newParent, index) {

		//if (newParent.isTreeNode) newParent.markLoading();

		var params = {
			// where from
			child: this.getInfo(child),
			childTree: this.getInfo(child.tree),
			// where to
			newParent: this.getInfo(newParent),
			newParentTree: this.getInfo(newParent.tree),
			newIndex: index
		};

		var success;

		dojo.io.bind({
			url: this.getRPCUrl('move'),
			/* I hitch to get this.loadOkHandler */
			load: dojo.lang.hitch(this,
				function(type, response) {
					success = this.doMoveProcessResponse(type, response, child, newParent, index) ;
				}
			),
			error: this.RPCErrorHandler,
			mimetype: "text/json",
			preventCache: true,
			sync: true,
			content: { data: dojo.json.serialize(params) }
		});


		return success;
	},

	doMoveProcessResponse: function(type, response, child, newParent, index) {

		if (!dojo.lang.isUndefined(response.error)) {
			this.RPCErrorHandler(response.error);
			return false;
		}

		var args = [child, newParent, index];
		return dojo.widget.TreeLoadingController.prototype.doMove.apply(this, args);

	},


	doRemoveChild: function(node, callFunc, callObj) {

		var params = {
			node: this.getInfo(node),
			tree: this.getInfo(node.tree)
		}

		dojo.io.bind({
				url: this.getRPCUrl('removeChild'),
				/* I hitch to get this.loadOkHandler */
				load: dojo.lang.hitch(this, function(type, response) {
					this.doRemoveChildProcessResponse(type, response, node, callFunc, callObj) }
				),
				error: this.RPCErrorHandler,
				mimetype: "text/json",
				preventCache: true,
				content: {data: dojo.json.serialize(params) }
		});

	},


	doRemoveChildProcessResponse: function(type, response, node, callFunc, callObj) {
		if (!dojo.lang.isUndefined(response.error)) {
			this.RPCErrorHandler(response.error);
			return false;
		}

		if (!response) return false;

		if (response == true) {
			/* change parent succeeded */
			var args = [ node, callFunc, callObj ];
			dojo.widget.TreeLoadingController.prototype.doRemoveChild.apply(this, args);

			return;
		} else if (dojo.lang.isObject(response)) {
			dojo.raise(response.error);
		} else {
			dojo.raise("Invalid response "+obj)
		}


	},



	// -----------------------------------------------------------------------------
	//                             Create node stuff
	// -----------------------------------------------------------------------------


	doCreateChild: function(parent, index, output, callFunc, callObj) {

			var params = {
				tree: this.getInfo(parent.tree),
				parent: this.getInfo(parent),
				index: index,
				data: dojo.json.serialize(output)
			}

			dojo.io.bind({
					url: this.getRPCUrl('createChild'),
					/* I hitch to get this.loadOkHandler */
					load: dojo.lang.hitch(this, function(type, response) {
						// data is dead, new data is used
						this.doCreateChildProcessResponse(type, response, parent, index, callFunc, callObj) }
					),
					error: this.RPCErrorHandler,
					mimetype: "text/json",
					preventCache: true,
					content: params
			});

	},

	doCreateChildProcessResponse: function(type, response, parent, index, callFunc, callObj) {

		if (!dojo.lang.isUndefined(response.error)) {
			this.RPCErrorHandler(response.error);
			return false;
		}

		if (!parent.isTreeNode) {
			dojo.raise("Can only add children to TreeNode")
		}

		if (!dojo.lang.isObject(response)) {
			dojo.raise("Invalid result "+response)
		}

		var args = [parent, index, response, callFunc, callObj];
		dojo.widget.TreeLoadingController.prototype.doCreateChild.apply(this, args);

	}

});
