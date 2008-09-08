dojo.provide("dojo.widget.TreeLoadingController");
dojo.require("dojo.widget.TreeBasicController");
dojo.require("dojo.event.*");
dojo.require("dojo.json");
dojo.require("dojo.io.*");
dojo.widget.defineWidget("dojo.widget.TreeLoadingController",dojo.widget.TreeBasicController,{RPCUrl:"",RPCActionParam:"action",RPCErrorHandler:function(_1,_2,_3){
alert("RPC Error: "+(_2.message||"no message"));
},preventCache:true,getRPCUrl:function(_4){
if(this.RPCUrl=="local"){
var _5=document.location.href.substr(0,document.location.href.lastIndexOf("/"));
var _6=_5+"/"+_4;
return _6;
}
if(!this.RPCUrl){
dojo.raise("Empty RPCUrl: can't load");
}
return this.RPCUrl+(this.RPCUrl.indexOf("?")>-1?"&":"?")+this.RPCActionParam+"="+_4;
},loadProcessResponse:function(_7,_8,_9,_a){
if(!dojo.lang.isUndefined(_8.error)){
this.RPCErrorHandler("server",_8.error);
return false;
}
var _b=_8;
if(!dojo.lang.isArray(_b)){
dojo.raise("loadProcessResponse: Not array loaded: "+_b);
}
for(var i=0;i<_b.length;i++){
_b[i]=dojo.widget.createWidget(_7.widgetType,_b[i]);
_7.addChild(_b[i]);
}
_7.state=_7.loadStates.LOADED;
if(dojo.lang.isFunction(_a)){
_a.apply(dojo.lang.isUndefined(_9)?this:_9,[_7,_b]);
}
},getInfo:function(_d){
return _d.getInfo();
},runRPC:function(kw){
var _f=this;
var _10=function(_11,_12,evt){
if(kw.lock){
dojo.lang.forEach(kw.lock,function(t){
t.unlock();
});
}
if(_11=="load"){
kw.load.call(this,_12);
}else{
this.RPCErrorHandler(_11,_12,evt);
}
};
if(kw.lock){
dojo.lang.forEach(kw.lock,function(t){
t.lock();
});
}
dojo.io.bind({url:kw.url,handle:dojo.lang.hitch(this,_10),mimetype:"text/json",preventCache:_f.preventCache,sync:kw.sync,content:{data:dojo.json.serialize(kw.params)}});
},loadRemote:function(_16,_17,_18,_19){
var _1a=this;
var _1b={node:this.getInfo(_16),tree:this.getInfo(_16.tree)};
this.runRPC({url:this.getRPCUrl("getChildren"),load:function(_1c){
_1a.loadProcessResponse(_16,_1c,_18,_19);
},sync:_17,lock:[_16],params:_1b});
},expand:function(_1d,_1e,_1f,_20){
if(_1d.state==_1d.loadStates.UNCHECKED&&_1d.isFolder){
this.loadRemote(_1d,_1e,this,function(_21,_22){
this.expand(_21,_1e,_1f,_20);
});
return;
}
dojo.widget.TreeBasicController.prototype.expand.apply(this,arguments);
},doMove:function(_23,_24,_25){
if(_24.isTreeNode&&_24.state==_24.loadStates.UNCHECKED){
this.loadRemote(_24,true);
}
return dojo.widget.TreeBasicController.prototype.doMove.apply(this,arguments);
},doCreateChild:function(_26,_27,_28,_29,_2a){
if(_26.state==_26.loadStates.UNCHECKED){
this.loadRemote(_26,true);
}
return dojo.widget.TreeBasicController.prototype.doCreateChild.apply(this,arguments);
}});
