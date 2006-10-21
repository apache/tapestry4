


dojo.provide("dojo.widget.TreeDndControllerV3");

dojo.require("dojo.dnd.TreeDragAndDropV3");

dojo.widget.defineWidget(
"dojo.widget.TreeDndControllerV3",
[dojo.widget.HtmlWidget, dojo.widget.TreeCommon],
function() {
this.dragSources = {};
this.dropTargets = {};
this.listenedTrees = {};
},
{
listenTreeEvents: ["afterChangeTree","beforeTreeDestroy", "afterAddChild"],
listenNodeFilter: function(elem) { return elem instanceof dojo.widget.Widget},

initialize: function(args) {
this.treeController = dojo.lang.isString(args.controller) ? dojo.widget.byId(args.controller) : args.controller;

if (!this.treeController) {
dojo.raise("treeController must be declared");
}

},

onBeforeTreeDestroy: function(message) {
this.unlistenTree(message.source);
},




onAfterAddChild: function(message) {
//dojo.debug("Dnd addChild "+message.child);
this.listenNode(message.child);
},


onAfterChangeTree: function(message) {

if (!message.oldTree) return;

//dojo.debug("HERE");

if (!message.newTree || !this.listenedTrees[message.newTree.widgetId]) {
this.processDescendants(message.node, this.listenNodeFilter, this.unlistenNode);
}

if (!this.listenedTrees[message.oldTree.widgetId]) {
// we have new node
this.processDescendants(message.node, this.listenNodeFilter, this.listenNode);
}
//dojo.profile.end("onTreeChange");
},



listenNode: function(node) {

//dojo.debug("listen dnd "+node);
//dojo.debug((new Error()).stack)
//dojo.profile.start("Dnd listenNode "+node);
if (!node.tree.DndMode) return;
if (this.dragSources[node.widgetId] || this.dropTargets[node.widgetId]) return;




var source = null;
var target = null;


if (!node.actionIsDisabled(node.actions.MOVE)) {
//dojo.debug("reg source")

//dojo.profile.start("Dnd source "+node);
var source = this.makeDragSource(node);
//dojo.profile.end("Dnd source "+node);

this.dragSources[node.widgetId] = source;
}

//dojo.profile.start("Dnd target "+node);
//dojo.debug("reg target");
var target = this.makeDropTarget(node);
//dojo.profile.end("Dnd target "+node);

this.dropTargets[node.widgetId] = target;

//dojo.profile.end("Dnd listenNode "+node);


},


makeDragSource: function(node) {
return new dojo.dnd.TreeDragSourceV3(node.contentNode, this, node.tree.widgetId, node);
},



makeDropTarget: function(node) {
return new dojo.dnd.TreeDropTargetV3(node.contentNode, this.treeController, node.tree.DndAcceptTypes, node);
},

unlistenNode: function(node) {

if (this.dragSources[node.widgetId]) {
dojo.dnd.dragManager.unregisterDragSource(this.dragSources[node.widgetId]);
delete this.dragSources[node.widgetId];
}

if (this.dropTargets[node.widgetId]) {
dojo.dnd.dragManager.unregisterDropTarget(this.dropTargets[node.widgetId]);
delete this.dropTargets[node.widgetId];
}
}

});
