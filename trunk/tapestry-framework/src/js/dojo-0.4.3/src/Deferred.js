dojo.provide("dojo.Deferred");
dojo.require("dojo.lang.func");
dojo.Deferred=function(_1){
this.chain=[];
this.id=this._nextId();
this.fired=-1;
this.paused=0;
this.results=[null,null];
this.canceller=_1;
this.silentlyCancelled=false;
};
dojo.lang.extend(dojo.Deferred,{getFunctionFromArgs:function(){
var a=arguments;
if((a[0])&&(!a[1])){
if(dojo.lang.isFunction(a[0])){
return a[0];
}else{
if(dojo.lang.isString(a[0])){
return dj_global[a[0]];
}
}
}else{
if((a[0])&&(a[1])){
return dojo.lang.hitch(a[0],a[1]);
}
}
return null;
},makeCalled:function(){
var _3=new dojo.Deferred();
_3.callback();
return _3;
},repr:function(){
var _4;
if(this.fired==-1){
_4="unfired";
}else{
if(this.fired==0){
_4="success";
}else{
_4="error";
}
}
return "Deferred("+this.id+", "+_4+")";
},toString:dojo.lang.forward("repr"),_nextId:(function(){
var n=1;
return function(){
return n++;
};
})(),cancel:function(){
if(this.fired==-1){
if(this.canceller){
this.canceller(this);
}else{
this.silentlyCancelled=true;
}
if(this.fired==-1){
this.errback(new Error(this.repr()));
}
}else{
if((this.fired==0)&&(this.results[0] instanceof dojo.Deferred)){
this.results[0].cancel();
}
}
},_pause:function(){
this.paused++;
},_unpause:function(){
this.paused--;
if((this.paused==0)&&(this.fired>=0)){
this._fire();
}
},_continue:function(_6){
this._resback(_6);
this._unpause();
},_resback:function(_7){
this.fired=((_7 instanceof Error)?1:0);
this.results[this.fired]=_7;
this._fire();
},_check:function(){
if(this.fired!=-1){
if(!this.silentlyCancelled){
dojo.raise("already called!");
}
this.silentlyCancelled=false;
return;
}
},callback:function(_8){
this._check();
this._resback(_8);
},errback:function(_9){
this._check();
if(!(_9 instanceof Error)){
_9=new Error(_9);
}
this._resback(_9);
},addBoth:function(cb,_b){
var _c=this.getFunctionFromArgs(cb,_b);
if(arguments.length>2){
_c=dojo.lang.curryArguments(null,_c,arguments,2);
}
return this.addCallbacks(_c,_c);
},addCallback:function(cb,_e){
var _f=this.getFunctionFromArgs(cb,_e);
if(arguments.length>2){
_f=dojo.lang.curryArguments(null,_f,arguments,2);
}
return this.addCallbacks(_f,null);
},addErrback:function(cb,_11){
var _12=this.getFunctionFromArgs(cb,_11);
if(arguments.length>2){
_12=dojo.lang.curryArguments(null,_12,arguments,2);
}
return this.addCallbacks(null,_12);
return this.addCallbacks(null,_11);
},addCallbacks:function(cb,eb){
this.chain.push([cb,eb]);
if(this.fired>=0){
this._fire();
}
return this;
},_fire:function(){
var _15=this.chain;
var _16=this.fired;
var res=this.results[_16];
var _18=this;
var cb=null;
while(_15.length>0&&this.paused==0){
var _1a=_15.shift();
var f=_1a[_16];
if(f==null){
continue;
}
try{
res=f(res);
_16=((res instanceof Error)?1:0);
if(res instanceof dojo.Deferred){
cb=function(res){
_18._continue(res);
};
this._pause();
}
}
catch(err){
_16=1;
res=err;
}
}
this.fired=_16;
this.results[_16]=res;
if((cb)&&(this.paused)){
res.addBoth(cb);
}
}});
