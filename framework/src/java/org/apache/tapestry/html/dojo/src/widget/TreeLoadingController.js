/*
	Copyright (c) 2004-2005, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/


dojo.provide("dojo.widget.TreeLoadingController");

dojo.require("dojo.widget.TreeBasicController");
dojo.require("dojo.event.*");
dojo.require("dojo.json")
dojo.require("dojo.io.*");


dojo.widget.tags.addParseTreeHandler("dojo:TreeLoadingController");


dojo.widget.TreeLoadingController = function() {
	dojo.widget.TreeBasicController.call(this);
}

dojo.inherits(dojo.widget.TreeLoadingController, dojo.widget.TreeBasicController);


dojo.lang.extend(dojo.widget.TreeLoadingController, {
	widgetType: "TreeLoadingController",

	RPCUrl: "local",


	/**
	 * Common RPC error handler (dies)
	*/
	RPCErrorHandler: function(type, obj) {
		alert( "RPC Error: " + (obj.message||"no message"));
	},



	getRPCUrl: function(action) {

		if (this.RPCUrl == "local") { // for demo and tests. May lead to widgetId collisions
			var dir = document.location.href.substr(0, document.location.href.lastIndexOf('/'));
			var localUrl = dir+"/"+action;
			//dojo.debug(localUrl);
			return localUrl;
		}

		if (!this.RPCUrl) {
			dojo.raise("Empty RPCUrl: can't load");
		}

		return this.RPCUrl + ( this.RPCUrl.indexOf("?") > -1 ? "&" : "?") + "action="+action;
	},


	/**
	 * Add all loaded nodes from array obj as node children and expand it
	*/
	loadProcessResponse: function(type, node, result, callFunc, callObj) {

		if (!dojo.lang.isUndefined(result.error)) {
			this.RPCErrorHandler(result.error);
			return false;
		}

		var newChildren = result;

		if (!dojo.lang.isArray(newChildren)) {
			dojo.raise('Not array loaded: '+newChildren);
		}

		for(var i=0; i<newChildren.length; i++) {
			// looks like dojo.widget.manager needs no special "add" command
			newChildren[i] = dojo.widget.createWidget(node.widgetType, newChildren[i]);
			node.addChild(newChildren[i]);
		}


		//node.addAllChildren(newChildren);

		node.state = node.loadStates.LOADED;

		if (dojo.lang.isFunction(callFunc)) {
			callFunc.apply(dojo.lang.isUndefined(callObj) ? this : callObj, [node, newChildren]);
		}
		//this.expand(node);
	},

	getInfo: function(obj) {
		return obj.getInfo();
	},

	/**
	 * Load children of the node from server
	 * Synchroneous loading doesn't break control flow
	 * I need sync mode for DnD
	*/
	loadRemote: function(node, sync, callFunc, callObj){
		node.markLoading();


		var params = {
			node: this.getInfo(node),
			tree: this.getInfo(node.tree)
		};

		var requestUrl = this.getRPCUrl('getChildren');
		//dojo.debug(requestUrl)

		dojo.io.bind({
			url: requestUrl,
			/* I hitch to get this.loadOkHandler */
			load: dojo.lang.hitch(this,
				function(type, result) {
					this.loadProcessResponse(type, node, result, callFunc, callObj) ;
				}
			),
			error: this.RPCErrorHandler,
			mimetype: "text/json",
			preventCache: true,
			sync: sync,
			content: { data: dojo.json.serialize(params) }
		});


	},


	expand: function(node, sync, callObj, callFunc) {

		if (node.state == node.loadStates.UNCHECKED && node.isFolder) {

			this.loadRemote(node, sync,
				function(node, newChildren) {
					this.expand(node, sync, callObj, callFunc);
				}
			);

			return;
		}

		dojo.widget.TreeBasicController.prototype.expand.apply(this, arguments);

	},



	doMove: function(child, newParent, index) {
		/* load nodes into newParent in sync mode, if needed, first */
		if (newParent.isTreeNode && newParent.state == newParent.loadStates.UNCHECKED) {
			this.loadRemote(newParent, true);
		}

		return dojo.widget.TreeBasicController.prototype.doMove.apply(this, arguments);
	},


	doCreateChild: function(parent, index, data, callFunc, callObj) {

		/* load nodes into newParent in sync mode, if needed, first */
		if (parent.state == parent.loadStates.UNCHECKED) {
			this.loadRemote(parent, true);
		}

		return dojo.widget.TreeBasicController.prototype.doCreateChild.apply(this, arguments);
	}



});
