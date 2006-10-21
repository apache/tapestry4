


dojo.require("dojo.lang.declare");
dojo.provide("dojo.widget.TreeWithNode");

dojo.declare(
"dojo.widget.TreeWithNode",
null,
function(){ },
{

loadStates: {
UNCHECKED: "UNCHECKED",
LOADING: "LOADING",
LOADED: "LOADED"
},

state: "UNCHECKED",  // after creation will change to loadStates: "loaded/loading/unchecked"

//RpcUrl: "", // user can override rpc url for specific nodes

objectId: "", // the widget represents an object



isContainer: true,

lockLevel: 0, // lock ++ unlock --, so nested locking works fine

lock: function() {
this.lockLevel++;
},
unlock: function() {
if (!this.lockLevel) {
//dojo.debug((new Error()).stack);
dojo.raise(this.widgetType+" unlock: not locked");
}
this.lockLevel--;
},


expandLevel: 0, // expand to level automatically
loadLevel: 0, // load to level automatically

hasLock: function() {
return this.lockLevel>0;
},

isLocked: function() {
var node = this;
while (true) {
if (node.lockLevel) {
return true;
}
if (!node.parent || node.isTree) {
break;
}

node = node.parent;

}

return false;
},


flushLock: function() {
this.lockLevel = 0;
//this.unMarkLoading();
},


actionIsDisabled: function(action) {
var disabled = false;

if (dojo.lang.inArray(this.actionsDisabled, action)) {
disabled = true;
}


//dojo.debug("Check "+this+" "+disabled)


if (this.isTreeNode) {
if (!this.tree.allowAddChildToLeaf && action == this.actions.ADDCHILD && !this.isFolder) {
disabled = true;
}
}
return disabled;
},

actionIsDisabledNow: function(action) {
return this.actionIsDisabled(action) || this.isLocked();
},



setChildren: function(childrenArray) {
//dojo.profile.start("setChildren "+this);
//dojo.debug("setChildren in "+this);


if (this.isTreeNode && !this.isFolder) {
//dojo.debug("folder parent "+parent+ " isfolder "+parent.isFolder);
this.setFolder();
} else if (this.isTreeNode) {
this.state = this.loadStates.LOADED;
}

var hadChildren = this.children.length > 0;

if (hadChildren && childrenArray){
// perf: most of time setChildren used for empty nodes, so save function call
this.destroyChildren()
}

if (childrenArray) {
this.children = childrenArray;
}



var hasChildren = this.children.length > 0;
if (this.isTreeNode && hasChildren != hadChildren) {
// call only when hasChildren state changes
this.viewSetHasChildren();
}



for(var i=0; i<this.children.length; i++) {
var child = this.children[i];

//dojo.profile.start("setChildren - create "+this);

if (!(child instanceof dojo.widget.Widget)) {

child = this.children[i] = this.tree.createNode(child);
var childWidgetCreated = true;
//dojo.debugShallow(child)

//dojo.debug("setChildren creates node "+child);
} else {
var childWidgetCreated = false;
}

//dojo.profile.end("setChildren - create "+this);

//dojo.profile.start("setChildren - attach "+this);

if (!child.parent) { // detached child

//dojo.debug("detached child "+child);

child.parent = this;

//dojo.profile.start("setChildren - updateTree "+this);

if (this.tree !== child.tree) {
child.updateTree(this.tree);
}
//dojo.profile.end("setChildren - updateTree "+this);


//dojo.debug("Add layout for "+child);
child.viewAddLayout();
this.containerNode.appendChild(child.domNode);

var message = {
child: child,
index: i,
parent: this,
childWidgetCreated: childWidgetCreated
}

delete dojo.widget.manager.topWidgets[child.widgetId];


//dojo.profile.start("setChildren - event "+this);
//dojo.debug("publish "+this.tree.eventNames.afterAddChild)
dojo.event.topic.publish(this.tree.eventNames.afterAddChild, message);

//dojo.profile.end("setChildren - event "+this);

}

if (this.tree.eagerWidgetInstantiation) {
dojo.lang.forEach(this.children, function(child) {
child.setChildren();
});
}

//dojo.profile.end("setChildren - attach "+this);


}



//dojo.profile.end("setChildren "+this);

},


doAddChild: function(child, index) {
return this.addChild(child, index, true);
},

addChild: function(child, index, dontPublishEvent) {
if (dojo.lang.isUndefined(index)) {
index = this.children.length;
}

//dojo.debug("doAddChild "+index+" called for "+this+" child "+child+" existing children "+(this.children.length ? this.children : "<no children>"));

if (!child.isTreeNode){
dojo.raise("You can only add TreeNode widgets to a "+this.widgetType+" widget!");
return;
}

this.children.splice(index, 0, child);
child.parent = this;

child.addedTo(this, index, dontPublishEvent);

// taken from DomWidget.registerChild
// delete from widget list that are notified on resize etc (no parent)
delete dojo.widget.manager.topWidgets[child.widgetId];

},


onShow: function() {
this.animationInProgress=false;
},

onHide: function() {
this.animationInProgress=false;
}

});
