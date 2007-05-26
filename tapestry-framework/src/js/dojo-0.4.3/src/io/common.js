dojo.provide("dojo.io.common");
dojo.require("dojo.string");
dojo.require("dojo.lang.extras");
dojo.io.transports=[];
dojo.io.hdlrFuncNames=["load","error","timeout"];
dojo.io.Request=function(_1,_2,_3,_4){
if((arguments.length==1)&&(arguments[0].constructor==Object)){
this.fromKwArgs(arguments[0]);
}else{
this.url=_1;
if(_2){
this.mimetype=_2;
}
if(_3){
this.transport=_3;
}
if(arguments.length>=4){
this.changeUrl=_4;
}
}
};
dojo.lang.extend(dojo.io.Request,{url:"",mimetype:"text/plain",method:"GET",content:undefined,transport:undefined,changeUrl:undefined,formNode:undefined,sync:false,bindSuccess:false,useCache:false,preventCache:false,jsonFilter:function(_5){
if((this.mimetype=="text/json-comment-filtered")||(this.mimetype=="application/json-comment-filtered")){
var _6=_5.indexOf("/*");
var _7=_5.lastIndexOf("*/");
if((_6==-1)||(_7==-1)){
dojo.debug("your JSON wasn't comment filtered!");
return "";
}
return _5.substring(_6+2,_7);
}
dojo.debug("please consider using a mimetype of text/json-comment-filtered to avoid potential security issues with JSON endpoints");
return _5;
},load:function(_8,_9,_a,_b){
},error:function(_c,_d,_e,_f){
},timeout:function(_10,_11,_12,_13){
},handle:function(_14,_15,_16,_17){
},timeoutSeconds:0,abort:function(){
},fromKwArgs:function(_18){
if(_18["url"]){
_18.url=_18.url.toString();
}
if(_18["formNode"]){
_18.formNode=dojo.byId(_18.formNode);
}
if(!_18["method"]&&_18["formNode"]&&_18["formNode"].method){
_18.method=_18["formNode"].method;
}
if(!_18["handle"]&&_18["handler"]){
_18.handle=_18.handler;
}
if(!_18["load"]&&_18["loaded"]){
_18.load=_18.loaded;
}
if(!_18["changeUrl"]&&_18["changeURL"]){
_18.changeUrl=_18.changeURL;
}
_18.encoding=dojo.lang.firstValued(_18["encoding"],djConfig["bindEncoding"],"");
_18.sendTransport=dojo.lang.firstValued(_18["sendTransport"],djConfig["ioSendTransport"],false);
var _19=dojo.lang.isFunction;
for(var x=0;x<dojo.io.hdlrFuncNames.length;x++){
var fn=dojo.io.hdlrFuncNames[x];
if(_18[fn]&&_19(_18[fn])){
continue;
}
if(_18["handle"]&&_19(_18["handle"])){
_18[fn]=_18.handle;
}
}
dojo.lang.mixin(this,_18);
}});
dojo.io.Error=function(msg,_1d,num){
this.message=msg;
this.type=_1d||"unknown";
this.number=num||0;
};
dojo.io.transports.addTransport=function(_1f){
this.push(_1f);
this[_1f]=dojo.io[_1f];
};
dojo.io.bind=function(_20){
if(!(_20 instanceof dojo.io.Request)){
try{
_20=new dojo.io.Request(_20);
}
catch(e){
dojo.debug(e);
}
}
var _21="";
if(_20["transport"]){
_21=_20["transport"];
if(!this[_21]){
dojo.io.sendBindError(_20,"No dojo.io.bind() transport with name '"+_20["transport"]+"'.");
return _20;
}
if(!this[_21].canHandle(_20)){
dojo.io.sendBindError(_20,"dojo.io.bind() transport with name '"+_20["transport"]+"' cannot handle this type of request.");
return _20;
}
}else{
for(var x=0;x<dojo.io.transports.length;x++){
var tmp=dojo.io.transports[x];
if((this[tmp])&&(this[tmp].canHandle(_20))){
_21=tmp;
break;
}
}
if(_21==""){
dojo.io.sendBindError(_20,"None of the loaded transports for dojo.io.bind()"+" can handle the request.");
return _20;
}
}
this[_21].bind(_20);
_20.bindSuccess=true;
return _20;
};
dojo.io.sendBindError=function(_24,_25){
if((typeof _24.error=="function"||typeof _24.handle=="function")&&(typeof setTimeout=="function"||typeof setTimeout=="object")){
var _26=new dojo.io.Error(_25);
setTimeout(function(){
_24[(typeof _24.error=="function")?"error":"handle"]("error",_26,null,_24);
},50);
}else{
dojo.raise(_25);
}
};
dojo.io.queueBind=function(_27){
if(!(_27 instanceof dojo.io.Request)){
try{
_27=new dojo.io.Request(_27);
}
catch(e){
dojo.debug(e);
}
}
var _28=_27.load;
_27.load=function(){
dojo.io._queueBindInFlight=false;
var ret=_28.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
var _2a=_27.error;
_27.error=function(){
dojo.io._queueBindInFlight=false;
var ret=_2a.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
dojo.io._bindQueue.push(_27);
dojo.io._dispatchNextQueueBind();
return _27;
};
dojo.io._dispatchNextQueueBind=function(){
if(!dojo.io._queueBindInFlight){
dojo.io._queueBindInFlight=true;
if(dojo.io._bindQueue.length>0){
dojo.io.bind(dojo.io._bindQueue.shift());
}else{
dojo.io._queueBindInFlight=false;
}
}
};
dojo.io._bindQueue=[];
dojo.io._queueBindInFlight=false;
dojo.io.argsFromMap=function(map,_2d,_2e){
var enc=/utf/i.test(_2d||"")?encodeURIComponent:dojo.string.encodeAscii;
var _30=[];
var _31=new Object();
for(var _32 in map){
var _33=function(elt){
var val=enc(_32)+"="+enc(elt);
_30[(_2e==_32)?"push":"unshift"](val);
};
if(!_31[_32]){
var _36=map[_32];
if(dojo.lang.isArray(_36)){
dojo.lang.forEach(_36,_33);
}else{
_33(_36);
}
}
}
return _30.join("&");
};
dojo.io.setIFrameSrc=function(_37,src,_39){
try{
var r=dojo.render.html;
if(!_39){
if(r.safari){
_37.location=src;
}else{
frames[_37.name].location=src;
}
}else{
var _3b;
if(r.ie){
_3b=_37.contentWindow.document;
}else{
if(r.safari){
_3b=_37.document;
}else{
_3b=_37.contentWindow;
}
}
if(!_3b){
_37.location=src;
return;
}else{
_3b.location.replace(src);
}
}
}
catch(e){
dojo.debug(e);
dojo.debug("setIFrameSrc: "+e);
}
};
