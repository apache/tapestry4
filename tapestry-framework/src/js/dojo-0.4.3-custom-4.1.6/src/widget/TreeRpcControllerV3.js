dojo.provide("dojo.widget.TreeRpcControllerV3");
dojo.require("dojo.event.*");
dojo.require("dojo.json");
dojo.require("dojo.io.*");
dojo.require("dojo.widget.TreeLoadingControllerV3");
dojo.widget.defineWidget("dojo.widget.TreeRpcControllerV3",dojo.widget.TreeLoadingControllerV3,{extraRpcOnEdit:false,doMove:function(_1,_2,_3,_4){
var _5={child:this.getInfo(_1),childTree:this.getInfo(_1.tree),oldParent:this.getInfo(_1.parent),oldParentTree:this.getInfo(_1.parent.tree),newParent:this.getInfo(_2),newParentTree:this.getInfo(_2.tree),newIndex:_3};
var _6=this.runRpc({url:this.getRpcUrl("move"),sync:_4,params:_5});
var _7=this;
var _8=arguments;
_6.addCallback(function(){
dojo.widget.TreeBasicControllerV3.prototype.doMove.apply(_7,_8);
});
return _6;
},prepareDetach:function(_9,_a){
var _b=this.startProcessing(_9);
return _b;
},finalizeDetach:function(_c){
this.finishProcessing(_c);
},doDetach:function(_d,_e){
var _f={node:this.getInfo(_d),tree:this.getInfo(_d.tree)};
var _10=this.runRpc({url:this.getRpcUrl("detach"),sync:_e,params:_f});
var _11=this;
var _12=arguments;
_10.addCallback(function(){
dojo.widget.TreeBasicControllerV3.prototype.doDetach.apply(_11,_12);
});
return _10;
},requestEditConfirmation:function(_13,_14,_15){
if(!this.extraRpcOnEdit){
return dojo.Deferred.prototype.makeCalled();
}
var _16=this;
var _17=this.startProcessing(_13);
var _18={node:this.getInfo(_13),tree:this.getInfo(_13.tree)};
_17.addCallback(function(){
return _16.runRpc({url:_16.getRpcUrl(_14),sync:_15,params:_18});
});
_17.addBoth(function(r){
_16.finishProcessing(_13);
return r;
});
return _17;
},editLabelSave:function(_1a,_1b,_1c){
var _1d=this.startProcessing(_1a);
var _1e=this;
var _1f={node:this.getInfo(_1a),tree:this.getInfo(_1a.tree),newContent:_1b};
_1d.addCallback(function(){
return _1e.runRpc({url:_1e.getRpcUrl("editLabelSave"),sync:_1c,params:_1f});
});
_1d.addBoth(function(r){
_1e.finishProcessing(_1a);
return r;
});
return _1d;
},editLabelStart:function(_21,_22){
if(!this.canEditLabel(_21)){
return false;
}
var _23=this;
if(!this.editor.isClosed()){
var _24=this.editLabelFinish(this.editor.saveOnBlur,_22);
_24.addCallback(function(){
return _23.editLabelStart(_21,_22);
});
return _24;
}
var _24=this.requestEditConfirmation(_21,"editLabelStart",_22);
_24.addCallback(function(){
_23.doEditLabelStart(_21);
});
return _24;
},editLabelFinish:function(_25,_26){
var _27=this;
var _28=this.editor.node;
var _29=dojo.Deferred.prototype.makeCalled();
if(!_25&&!_28.isPhantom){
_29=this.requestEditConfirmation(this.editor.node,"editLabelFinishCancel",_26);
}
if(_25){
if(_28.isPhantom){
_29=this.sendCreateChildRequest(_28.parent,_28.getParentIndex(),{title:this.editor.getContents()},_26);
}else{
_29=this.editLabelSave(_28,this.editor.getContents(),_26);
}
}
_29.addCallback(function(_2a){
_27.doEditLabelFinish(_25,_2a);
});
_29.addErrback(function(r){
_27.doEditLabelFinish(false);
return false;
});
return _29;
},createAndEdit:function(_2c,_2d,_2e){
var _2f={title:_2c.tree.defaultChildTitle};
if(!this.canCreateChild(_2c,_2d,_2f)){
return false;
}
if(!this.editor.isClosed()){
var _30=this.editLabelFinish(this.editor.saveOnBlur,_2e);
_30.addCallback(function(){
return _31.createAndEdit(_2c,_2d,_2e);
});
return _30;
}
var _31=this;
var _30=this.prepareCreateChild(_2c,_2d,_2f,_2e);
_30.addCallback(function(){
var _32=_31.makeDefaultNode(_2c,_2d);
_32.isPhantom=true;
return _32;
});
_30.addBoth(function(r){
_31.finalizeCreateChild(_2c,_2d,_2f,_2e);
return r;
});
_30.addCallback(function(_34){
var d=_31.exposeCreateChild(_2c,_2d,_2f,_2e);
d.addCallback(function(){
return _34;
});
return d;
});
_30.addCallback(function(_36){
_31.doEditLabelStart(_36);
return _36;
});
return _30;
},prepareDestroyChild:function(_37,_38){
var _39=this.startProcessing(_37);
return _39;
},finalizeDestroyChild:function(_3a){
this.finishProcessing(_3a);
},doDestroyChild:function(_3b,_3c){
var _3d={node:this.getInfo(_3b),tree:this.getInfo(_3b.tree)};
var _3e=this.runRpc({url:this.getRpcUrl("destroyChild"),sync:_3c,params:_3d});
var _3f=this;
var _40=arguments;
_3e.addCallback(function(){
dojo.widget.TreeBasicControllerV3.prototype.doDestroyChild.apply(_3f,_40);
});
return _3e;
},sendCreateChildRequest:function(_41,_42,_43,_44){
var _45={tree:this.getInfo(_41.tree),parent:this.getInfo(_41),index:_42,data:_43};
var _46=this.runRpc({url:this.getRpcUrl("createChild"),sync:_44,params:_45});
return _46;
},doCreateChild:function(_47,_48,_49,_4a){
if(dojo.lang.isUndefined(_49.title)){
_49.title=_47.tree.defaultChildTitle;
}
var _4b=this.sendCreateChildRequest(_47,_48,_49,_4a);
var _4c=this;
var _4d=arguments;
_4b.addCallback(function(_4e){
dojo.lang.mixin(_49,_4e);
return dojo.widget.TreeBasicControllerV3.prototype.doCreateChild.call(_4c,_47,_48,_49);
});
return _4b;
},doClone:function(_4f,_50,_51,_52,_53){
var _54={child:this.getInfo(_4f),oldParent:this.getInfo(_4f.parent),oldParentTree:this.getInfo(_4f.parent.tree),newParent:this.getInfo(_50),newParentTree:this.getInfo(_50.tree),index:_51,deep:_52?true:false,tree:this.getInfo(_4f.tree)};
var _55=this.runRpc({url:this.getRpcUrl("clone"),sync:_53,params:_54});
var _56=this;
var _57=arguments;
_55.addCallback(function(){
dojo.widget.TreeBasicControllerV3.prototype.doClone.apply(_56,_57);
});
return _55;
}});
