dojo.provide("dojo.logging.ConsoleLogger");
dojo.require("dojo.logging.Logger");
dojo.lang.extend(dojo.logging.MemoryLogHandler,{debug:function(){
dojo.hostenv.println.apply(this,arguments);
},info:function(){
dojo.hostenv.println.apply(this,arguments);
},warn:function(){
dojo.hostenv.println.apply(this,arguments);
},error:function(){
dojo.hostenv.println.apply(this,arguments);
},critical:function(){
dojo.hostenv.println.apply(this,arguments);
},emit:function(_1){
if(!djConfig.isDebug){
return;
}
var _2=null;
switch(_1.level){
case 1:
_2="debug";
break;
case 2:
_2="info";
break;
case 3:
_2="warn";
break;
case 4:
_2="error";
break;
case 5:
_2="critical";
break;
default:
_2="debug";
}
var _3=String(dojo.log.getLevelName(_1.level)+": "+_1.time.toLocaleTimeString())+": "+_1.message;
if(_1.msgArgs&&_1.msgArgs.length>0){
this[_2].call(this,_3,_1.msgArgs);
}else{
this[_2].call(this,_3);
}
this.data.push(_1);
if(this.numRecords!=-1){
while(this.data.length>this.numRecords){
this.data.shift();
}
}
}});
if(!dj_undef("console")&&!dj_undef("info",console)){
dojo.lang.extend(dojo.logging.MemoryLogHandler,{debug:function(){
console.debug.apply(this,arguments);
},info:function(){
console.info.apply(this,arguments);
},warn:function(){
console.warn.apply(this,arguments);
},error:function(){
console.error.apply(this,arguments);
},critical:function(){
console.error.apply(this,arguments);
}});
dojo.lang.extend(dojo.logging.Logger,{exception:function(_4,e,_6){
var _7=[_4];
if(e){
_4+=" : "+e.name+" "+(e.description||e.message);
_7.push(e);
}
this.logType("ERROR",_7);
if(!_6){
throw e;
}
}});
}
