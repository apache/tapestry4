dojo.require("dojo.Deferred");
dojo.provide("dojo.DeferredList");
dojo.DeferredList=function(_1,_2,_3,_4,_5){
this.list=_1;
this.resultList=new Array(this.list.length);
this.chain=[];
this.id=this._nextId();
this.fired=-1;
this.paused=0;
this.results=[null,null];
this.canceller=_5;
this.silentlyCancelled=false;
if(this.list.length===0&&!_2){
this.callback(this.resultList);
}
this.finishedCount=0;
this.fireOnOneCallback=_2;
this.fireOnOneErrback=_3;
this.consumeErrors=_4;
var _6=0;
var _7=this;
dojo.lang.forEach(this.list,function(d){
var _9=_6;
d.addCallback(function(r){
_7._cbDeferred(_9,true,r);
});
d.addErrback(function(r){
_7._cbDeferred(_9,false,r);
});
_6++;
});
};
dojo.inherits(dojo.DeferredList,dojo.Deferred);
dojo.lang.extend(dojo.DeferredList,{_cbDeferred:function(_c,_d,_e){
this.resultList[_c]=[_d,_e];
this.finishedCount+=1;
if(this.fired!==0){
if(_d&&this.fireOnOneCallback){
this.callback([_c,_e]);
}else{
if(!_d&&this.fireOnOneErrback){
this.errback(_e);
}else{
if(this.finishedCount==this.list.length){
this.callback(this.resultList);
}
}
}
}
if(!_d&&this.consumeErrors){
_e=null;
}
return _e;
},gatherResults:function(_f){
var d=new dojo.DeferredList(_f,false,true,false);
d.addCallback(function(_11){
var ret=[];
for(var i=0;i<_11.length;i++){
ret.push(_11[i][1]);
}
return ret;
});
return d;
}});
