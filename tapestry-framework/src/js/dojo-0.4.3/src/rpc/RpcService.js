dojo.provide("dojo.rpc.RpcService");
dojo.require("dojo.io.*");
dojo.require("dojo.json");
dojo.require("dojo.lang.func");
dojo.require("dojo.Deferred");
dojo.rpc.RpcService=function(_1){
if(_1){
this.connect(_1);
}
};
dojo.lang.extend(dojo.rpc.RpcService,{strictArgChecks:true,serviceUrl:"",parseResults:function(_2){
return _2;
},errorCallback:function(_3){
return function(_4,e){
_3.errback(new Error(e.message));
};
},resultCallback:function(_6){
var tf=dojo.lang.hitch(this,function(_8,_9,e){
if(_9["error"]!=null){
var _b=new Error(_9.error);
_b.id=_9.id;
_6.errback(_b);
}else{
var _c=this.parseResults(_9);
_6.callback(_c);
}
});
return tf;
},generateMethod:function(_d,_e,_f){
return dojo.lang.hitch(this,function(){
var _10=new dojo.Deferred();
if((this.strictArgChecks)&&(_e!=null)&&(arguments.length!=_e.length)){
dojo.raise("Invalid number of parameters for remote method.");
}else{
this.bind(_d,arguments,_10,_f);
}
return _10;
});
},processSmd:function(_11){
dojo.debug("RpcService: Processing returned SMD.");
if(_11.methods){
dojo.lang.forEach(_11.methods,function(m){
if(m&&m["name"]){
dojo.debug("RpcService: Creating Method: this.",m.name,"()");
this[m.name]=this.generateMethod(m.name,m.parameters,m["url"]||m["serviceUrl"]||m["serviceURL"]);
if(dojo.lang.isFunction(this[m.name])){
dojo.debug("RpcService: Successfully created",m.name,"()");
}else{
dojo.debug("RpcService: Failed to create",m.name,"()");
}
}
},this);
}
this.serviceUrl=_11.serviceUrl||_11.serviceURL;
dojo.debug("RpcService: Dojo RpcService is ready for use.");
},connect:function(_13){
dojo.debug("RpcService: Attempting to load SMD document from:",_13);
dojo.io.bind({url:_13,mimetype:"text/json",load:dojo.lang.hitch(this,function(_14,_15,e){
return this.processSmd(_15);
}),sync:true});
}});
