


dojo.provide("dojo.widget.TreeCommon");
dojo.require("dojo.widget.*"); // for dojo.widget.manager

dojo.declare(
"dojo.widget.TreeCommon",
null,
{
listenTreeEvents: [],
listenedTrees: {},


listenNodeFilter: null,

listenTree: function(tree) {

//dojo.debug("listenTree in "+this+" tree "+tree);

var _this = this;

if (this.listenedTrees[tree.widgetId]) {
return; // already listening
}

dojo.lang.forEach(this.listenTreeEvents, function(event) {
var eventHandler =  "on" + event.charAt(0).toUpperCase() + event.substr(1);
//dojo.debug("subscribe: event "+tree.eventNames[event]+" widget "+_this+" handler "+eventHandler);
dojo.event.topic.subscribe(tree.eventNames[event], _this, eventHandler);
});


var filter;

if (this.listenNodeFilter) {
this.processDescendants(tree, this.listenNodeFilter, this.listenNode, true);
}


this.listenedTrees[tree.widgetId] = true;

},


listenNode: function() {},
unlistenNode: function() {},

unlistenTree: function(tree, nodeFilter) {

var _this = this;

if (!this.listenedTrees[tree.widgetId]) {
return;
}

dojo.lang.forEach(this.listenTreeEvents, function(event) {
var eventHandler =  "on" + event.charAt(0).toUpperCase() + event.substr(1);
dojo.event.topic.unsubscribe(tree.eventNames[event], _this, eventHandler);
});


if (this.listenNodeFilter) {
this.processDescendants(tree, this.listenNodeFilter, this.unlistenNode, true);
}

delete this.listenedTrees[tree.widgetId];

},


domElement2TreeNode: function(domElement) {
while (domElement && !domElement.widgetId) {
domElement = domElement.parentNode;
}

if (!domElement) {
return null;
}

var widget = dojo.widget.byId(domElement.widgetId);
if (!widget.isTreeNode) {
return null;
}

return widget;
},


processDescendants: function(elem, filter, func, skipFirst) {

var _this = this;

if (!skipFirst) {
if (!filter.call(_this,elem)) {
return;
}
func.call(_this,elem);
}


var stack = [elem];
while (elem = stack.pop()) {
dojo.lang.forEach(elem.children, function(elem) {
if (filter.call(_this, elem)) {
func.call(_this, elem);
stack.push(elem);
}
});
}
}
});
