dojo.provide("dojo.widget.TreeLoadingControllerV3");
dojo.require("dojo.widget.TreeBasicControllerV3");
dojo.require("dojo.event.*");
dojo.require("dojo.json");
dojo.require("dojo.io.*");
dojo.require("dojo.Deferred");
dojo.require("dojo.DeferredList");
dojo.declare("dojo.Error",Error,function(_1,_2){
this.message=_1;
this.extra=_2;
this.stack=(new Error()).stack;
});
dojo.declare("dojo.CommunicationError",dojo.Error,function(){
this.name="CommunicationError";
});
dojo.declare("dojo.LockedError",dojo.Error,function(){
this.name="LockedError";
});
dojo.declare("dojo.FormatError",dojo.Error,function(){
this.name="FormatError";
});
dojo.declare("dojo.RpcError",dojo.Error,function(){
this.name="RpcError";
});
dojo.widget.defineWidget("dojo.widget.TreeLoadingControllerV3",dojo.widget.TreeBasicControllerV3,{RpcUrl:"",RpcActionParam:"action",preventCache:true,checkValidRpcResponse:function(_3,_4){
if(_3!="load"){
var _5={};
for(var i=1;i<arguments.length;i++){
dojo.lang.mixin(_5,arguments[i]);
}
return new dojo.CommunicationError(_4,_5);
}
if(typeof _4!="object"){
return new dojo.FormatError("Wrong server answer format "+(_4&&_4.toSource?_4.toSource():_4)+" type "+(typeof _4),_4);
}
if(!dojo.lang.isUndefined(_4.error)){
return new dojo.RpcError(_4.error,_4);
}
return false;
},getDeferredBindHandler:function(_7){
return dojo.lang.hitch(this,function(_8,_9){
var _a=this.checkValidRpcResponse.apply(this,arguments);
if(_a){
_7.errback(_a);
return;
}
_7.callback(_9);
});
},getRpcUrl:function(_b){
if(this.RpcUrl=="local"){
var _c=document.location.href.substr(0,document.location.href.lastIndexOf("/"));
var _d=_c+"/local/"+_b;
return _d;
}
if(!this.RpcUrl){
dojo.raise("Empty RpcUrl: can't load");
}
var _e=this.RpcUrl;
if(_e.indexOf("/")!=0){
var _f=document.location.href.replace(/:\/\/.*/,"");
var _10=document.location.href.substring(_f.length+3);
if(_10.lastIndexOf("/")!=_10.length-1){
_10=_10.replace(/\/[^\/]+$/,"/");
}
if(_10.lastIndexOf("/")!=_10.length-1){
_10=_10+"/";
}
_e=_f+"://"+_10+_e;
}
return _e+(_e.indexOf("?")>-1?"&":"?")+this.RpcActionParam+"="+_b;
},loadProcessResponse:function(_11,_12){
if(!dojo.lang.isArray(_12)){
throw new dojo.FormatError("loadProcessResponse: Not array loaded: "+_12);
}
_11.setChildren(_12);
},runRpc:function(kw){
var _14=this;
var _15=new dojo.Deferred();
dojo.io.bind({url:kw.url,handle:this.getDeferredBindHandler(_15),mimetype:"text/javascript",preventCache:this.preventCache,sync:kw.sync,content:{data:dojo.json.serialize(kw.params)}});
return _15;
},loadRemote:function(_16,_17){
var _18=this;
var _19={node:this.getInfo(_16),tree:this.getInfo(_16.tree)};
var _1a=this.runRpc({url:this.getRpcUrl("getChildren"),sync:_17,params:_19});
_1a.addCallback(function(res){
return _18.loadProcessResponse(_16,res);
});
return _1a;
},batchExpandTimeout:0,recurseToLevel:function(_1c,_1d,_1e,_1f,_20,_21){
if(_1d==0){
return;
}
if(!_20){
var _22=_1e.call(_1f,_1c,_21);
}else{
var _22=dojo.Deferred.prototype.makeCalled();
}
var _23=this;
var _24=function(){
var _25=_1c.children;
var _26=[];
for(var i=0;i<_25.length;i++){
_26.push(_23.recurseToLevel(_25[i],_1d-1,_1e,_1f,_21));
}
return new dojo.DeferredList(_26);
};
_22.addCallback(_24);
return _22;
},expandToLevel:function(_28,_29,_2a){
return this.recurseToLevel(_28,_28.isTree?_29+1:_29,this.expand,this,_28.isTree,_2a);
},loadToLevel:function(_2b,_2c,_2d){
return this.recurseToLevel(_2b,_2b.isTree?_2c+1:_2c,this.loadIfNeeded,this,_2b.isTree,_2d);
},loadAll:function(_2e,_2f){
return this.loadToLevel(_2e,Number.POSITIVE_INFINITY,_2f);
},expand:function(_30,_31){
var _32=this;
var _33=this.startProcessing(_30);
_33.addCallback(function(){
return _32.loadIfNeeded(_30,_31);
});
_33.addCallback(function(res){
dojo.widget.TreeBasicControllerV3.prototype.expand(_30);
return res;
});
_33.addBoth(function(res){
_32.finishProcessing(_30);
return res;
});
return _33;
},loadIfNeeded:function(_36,_37){
var _38;
if(_36.state==_36.loadStates.UNCHECKED&&_36.isFolder&&!_36.children.length){
_38=this.loadRemote(_36,_37);
}else{
_38=new dojo.Deferred();
_38.callback();
}
return _38;
},runStages:function(_39,_3a,_3b,_3c,_3d,_3e){
var _3f=this;
if(_39&&!_39.apply(this,_3e)){
return false;
}
var _40=dojo.Deferred.prototype.makeCalled();
if(_3a){
_40.addCallback(function(){
return _3a.apply(_3f,_3e);
});
}
if(_3b){
_40.addCallback(function(){
var res=_3b.apply(_3f,_3e);
return res;
});
}
if(_3c){
_40.addBoth(function(res){
_3c.apply(_3f,_3e);
return res;
});
}
if(_3d){
_40.addCallback(function(res){
_3d.apply(_3f,_3e);
return res;
});
}
return _40;
},startProcessing:function(_44){
var _45=new dojo.Deferred();
var _46=dojo.lang.isArray(_44)?_44:arguments;
for(var i=0;i<_46.length;i++){
if(_46[i].isLocked()){
_45.errback(new dojo.LockedError("item locked "+_46[i],_46[i]));
return _45;
}
if(_46[i].isTreeNode){
_46[i].markProcessing();
}
_46[i].lock();
}
_45.callback();
return _45;
},finishProcessing:function(_48){
var _49=dojo.lang.isArray(_48)?_48:arguments;
for(var i=0;i<_49.length;i++){
if(!_49[i].hasLock()){
continue;
}
_49[i].unlock();
if(_49[i].isTreeNode){
_49[i].unmarkProcessing();
}
}
},refreshChildren:function(_4b,_4c){
return this.runStages(null,this.prepareRefreshChildren,this.doRefreshChildren,this.finalizeRefreshChildren,this.exposeRefreshChildren,arguments);
},prepareRefreshChildren:function(_4d,_4e){
var _4f=this.startProcessing(_4d);
_4d.destroyChildren();
_4d.state=_4d.loadStates.UNCHECKED;
return _4f;
},doRefreshChildren:function(_50,_51){
return this.loadRemote(_50,_51);
},finalizeRefreshChildren:function(_52,_53){
this.finishProcessing(_52);
},exposeRefreshChildren:function(_54,_55){
_54.expand();
},move:function(_56,_57,_58){
return this.runStages(this.canMove,this.prepareMove,this.doMove,this.finalizeMove,this.exposeMove,arguments);
},doMove:function(_59,_5a,_5b){
_59.tree.move(_59,_5a,_5b);
return true;
},prepareMove:function(_5c,_5d,_5e,_5f){
var _60=this.startProcessing(_5d);
_60.addCallback(dojo.lang.hitch(this,function(){
return this.loadIfNeeded(_5d,_5f);
}));
return _60;
},finalizeMove:function(_61,_62){
this.finishProcessing(_62);
},prepareCreateChild:function(_63,_64,_65,_66){
var _67=this.startProcessing(_63);
_67.addCallback(dojo.lang.hitch(this,function(){
return this.loadIfNeeded(_63,_66);
}));
return _67;
},finalizeCreateChild:function(_68){
this.finishProcessing(_68);
},prepareClone:function(_69,_6a,_6b,_6c,_6d){
var _6e=this.startProcessing(_69,_6a);
_6e.addCallback(dojo.lang.hitch(this,function(){
return this.loadIfNeeded(_6a,_6d);
}));
return _6e;
},finalizeClone:function(_6f,_70){
this.finishProcessing(_6f,_70);
}});
