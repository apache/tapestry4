


dojo.provide("dojo.widget.TreeLoadingController");

dojo.require("dojo.widget.TreeBasicController");
dojo.require("dojo.event.*");
dojo.require("dojo.json")
dojo.require("dojo.io.*");


dojo.widget.defineWidget("dojo.widget.TreeLoadingController", dojo.widget.TreeBasicController, {
RPCUrl: "",

RPCActionParam: "action", // used for GET for RPCUrl



RPCErrorHandler: function(type, obj, evt) {
alert( "RPC Error: " + (obj.message||"no message"));
},

preventCache: true,

getRPCUrl: function(action) {

// RPCUrl=local meant SOLELY for DEMO and LOCAL TESTS.
// May lead to widgetId collisions
if (this.RPCUrl == "local") {
var dir = document.location.href.substr(0, document.location.href.lastIndexOf('/'));
var localUrl = dir+"/"+action;
//dojo.debug(localUrl);
return localUrl;
}

if (!this.RPCUrl) {
dojo.raise("Empty RPCUrl: can't load");
}

return this.RPCUrl + ( this.RPCUrl.indexOf("?") > -1 ? "&" : "?") + this.RPCActionParam+"="+action;
},



loadProcessResponse: function(node, result, callObj, callFunc) {

if (!dojo.lang.isUndefined(result.error)) {
this.RPCErrorHandler("server", result.error);
return false;
}

//dojo.debugShallow(result);

var newChildren = result;

if (!dojo.lang.isArray(newChildren)) {
dojo.raise('loadProcessResponse: Not array loaded: '+newChildren);
}

for(var i=0; i<newChildren.length; i++) {
// looks like dojo.widget.manager needs no special "add" command
newChildren[i] = dojo.widget.createWidget(node.widgetType, newChildren[i]);
node.addChild(newChildren[i]);
}


//node.addAllChildren(newChildren);

node.state = node.loadStates.LOADED;

//dojo.debug(callFunc);

if (dojo.lang.isFunction(callFunc)) {
callFunc.apply(dojo.lang.isUndefined(callObj) ? this : callObj, [node, newChildren]);
}
//this.expand(node);
},

getInfo: function(obj) {
return obj.getInfo();
},

runRPC: function(kw) {
var _this = this;

var handle = function(type, data, evt) {
// unlock BEFORE any processing is done
// so errorHandler may apply locking
if (kw.lock) {
dojo.lang.forEach(kw.lock,
function(t) { t.unlock() }
);
}

if(type == "load"){
kw.load.call(this, data);
}else{
this.RPCErrorHandler(type, data, evt);
}

}

if (kw.lock) {
dojo.lang.forEach(kw.lock,
function(t) { t.lock() }
);
}


dojo.io.bind({
url: kw.url,

handle: dojo.lang.hitch(this, handle),
mimetype: "text/json",
preventCache: _this.preventCache,
sync: kw.sync,
content: { data: dojo.json.serialize(kw.params) }
});
},




loadRemote: function(node, sync, callObj, callFunc){
var _this = this;

var params = {
node: this.getInfo(node),
tree: this.getInfo(node.tree)
};

//dojo.debug(callFunc)

this.runRPC({
url: this.getRPCUrl('getChildren'),
load: function(result) {
_this.loadProcessResponse(node, result, callObj, callFunc) ;
},
sync: sync,
lock: [node],
params: params
});

},


expand: function(node, sync, callObj, callFunc) {

if (node.state == node.loadStates.UNCHECKED && node.isFolder) {

this.loadRemote(node, sync,
this,
function(node, newChildren) {
this.expand(node, sync, callObj, callFunc);
}
);

return;
}

dojo.widget.TreeBasicController.prototype.expand.apply(this, arguments);

},



doMove: function(child, newParent, index) {

if (newParent.isTreeNode && newParent.state == newParent.loadStates.UNCHECKED) {
this.loadRemote(newParent, true);
}

return dojo.widget.TreeBasicController.prototype.doMove.apply(this, arguments);
},


doCreateChild: function(parent, index, data, callObj, callFunc) {


if (parent.state == parent.loadStates.UNCHECKED) {
this.loadRemote(parent, true);
}

return dojo.widget.TreeBasicController.prototype.doCreateChild.apply(this, arguments);
}



});
