dojo.provide("dojo.rpc.JsonService");
dojo.require("dojo.rpc.RpcService");
dojo.require("dojo.io.*");
dojo.require("dojo.json");
dojo.require("dojo.lang.common");
dojo.rpc.JsonService=function(_1){
if(_1){
if(dojo.lang.isString(_1)){
this.connect(_1);
}else{
if(_1["smdUrl"]){
this.connect(_1.smdUrl);
}
if(_1["smdStr"]){
this.processSmd(dj_eval("("+_1.smdStr+")"));
}
if(_1["smdObj"]){
this.processSmd(_1.smdObj);
}
if(_1["serviceUrl"]){
this.serviceUrl=_1.serviceUrl;
}
if(typeof _1["strictArgChecks"]!="undefined"){
this.strictArgChecks=_1.strictArgChecks;
}
}
}
};
dojo.inherits(dojo.rpc.JsonService,dojo.rpc.RpcService);
dojo.extend(dojo.rpc.JsonService,{bustCache:false,contentType:"application/json-rpc",lastSubmissionId:0,callRemote:function(_2,_3){
var _4=new dojo.Deferred();
this.bind(_2,_3,_4);
return _4;
},bind:function(_5,_6,_7,_8){
dojo.io.bind({url:_8||this.serviceUrl,postContent:this.createRequest(_5,_6),method:"POST",contentType:this.contentType,mimetype:"text/json",load:this.resultCallback(_7),error:this.errorCallback(_7),preventCache:this.bustCache});
},createRequest:function(_9,_a){
var _b={"params":_a,"method":_9,"id":++this.lastSubmissionId};
var _c=dojo.json.serialize(_b);
dojo.debug("JsonService: JSON-RPC Request: "+_c);
return _c;
},parseResults:function(_d){
if(!_d){
return;
}
if(_d["Result"]!=null){
return _d["Result"];
}else{
if(_d["result"]!=null){
return _d["result"];
}else{
if(_d["ResultSet"]){
return _d["ResultSet"];
}else{
return _d;
}
}
}
}});
