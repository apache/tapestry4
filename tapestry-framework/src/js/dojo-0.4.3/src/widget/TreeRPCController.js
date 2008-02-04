dojo.provide("dojo.widget.TreeRPCController");
dojo.require("dojo.event.*");
dojo.require("dojo.json");
dojo.require("dojo.io.*");
dojo.require("dojo.widget.TreeLoadingController");
dojo.widget.defineWidget("dojo.widget.TreeRPCController",dojo.widget.TreeLoadingController,{doMove:function(_1,_2,_3){
var _4={child:this.getInfo(_1),childTree:this.getInfo(_1.tree),newParent:this.getInfo(_2),newParentTree:this.getInfo(_2.tree),newIndex:_3};
var _5;
this.runRPC({url:this.getRPCUrl("move"),load:function(_6){
_5=this.doMoveProcessResponse(_6,_1,_2,_3);
},sync:true,lock:[_1,_2],params:_4});
return _5;
},doMoveProcessResponse:function(_7,_8,_9,_a){
if(!dojo.lang.isUndefined(_7.error)){
this.RPCErrorHandler("server",_7.error);
return false;
}
var _b=[_8,_9,_a];
return dojo.widget.TreeLoadingController.prototype.doMove.apply(this,_b);
},doRemoveNode:function(_c,_d,_e){
var _f={node:this.getInfo(_c),tree:this.getInfo(_c.tree)};
this.runRPC({url:this.getRPCUrl("removeNode"),load:function(_10){
this.doRemoveNodeProcessResponse(_10,_c,_d,_e);
},params:_f,lock:[_c]});
},doRemoveNodeProcessResponse:function(_11,_12,_13,_14){
if(!dojo.lang.isUndefined(_11.error)){
this.RPCErrorHandler("server",_11.error);
return false;
}
if(!_11){
return false;
}
if(_11==true){
var _15=[_12,_13,_14];
dojo.widget.TreeLoadingController.prototype.doRemoveNode.apply(this,_15);
return;
}else{
if(dojo.lang.isObject(_11)){
dojo.raise(_11.error);
}else{
dojo.raise("Invalid response "+_11);
}
}
},doCreateChild:function(_16,_17,_18,_19,_1a){
var _1b={tree:this.getInfo(_16.tree),parent:this.getInfo(_16),index:_17,data:_18};
this.runRPC({url:this.getRPCUrl("createChild"),load:function(_1c){
this.doCreateChildProcessResponse(_1c,_16,_17,_19,_1a);
},params:_1b,lock:[_16]});
},doCreateChildProcessResponse:function(_1d,_1e,_1f,_20,_21){
if(!dojo.lang.isUndefined(_1d.error)){
this.RPCErrorHandler("server",_1d.error);
return false;
}
if(!dojo.lang.isObject(_1d)){
dojo.raise("Invalid result "+_1d);
}
var _22=[_1e,_1f,_1d,_20,_21];
dojo.widget.TreeLoadingController.prototype.doCreateChild.apply(this,_22);
}});
