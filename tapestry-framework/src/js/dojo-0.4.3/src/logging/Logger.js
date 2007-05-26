dojo.provide("dojo.logging.Logger");
dojo.provide("dojo.logging.LogFilter");
dojo.provide("dojo.logging.Record");
dojo.provide("dojo.log");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.declare");
dojo.logging.Record=function(_1,_2){
this.level=_1;
this.message="";
this.msgArgs=[];
this.time=new Date();
if(dojo.lang.isArray(_2)){
if(_2.length>0&&dojo.lang.isString(_2[0])){
this.message=_2.shift();
}
this.msgArgs=_2;
}else{
this.message=_2;
}
};
dojo.logging.LogFilter=function(_3){
this.passChain=_3||"";
this.filter=function(_4){
return true;
};
};
dojo.logging.Logger=function(){
this.cutOffLevel=0;
this.propagate=true;
this.parent=null;
this.data=[];
this.filters=[];
this.handlers=[];
};
dojo.extend(dojo.logging.Logger,{_argsToArr:function(_5){
var _6=[];
for(var x=0;x<_5.length;x++){
_6.push(_5[x]);
}
return _6;
},setLevel:function(_8){
this.cutOffLevel=parseInt(_8);
},isEnabledFor:function(_9){
return parseInt(_9)>=this.cutOffLevel;
},getEffectiveLevel:function(){
if((this.cutOffLevel==0)&&(this.parent)){
return this.parent.getEffectiveLevel();
}
return this.cutOffLevel;
},addFilter:function(_a){
this.filters.push(_a);
return this.filters.length-1;
},removeFilterByIndex:function(_b){
if(this.filters[_b]){
delete this.filters[_b];
return true;
}
return false;
},removeFilter:function(_c){
for(var x=0;x<this.filters.length;x++){
if(this.filters[x]===_c){
delete this.filters[x];
return true;
}
}
return false;
},removeAllFilters:function(){
this.filters=[];
},filter:function(_e){
for(var x=0;x<this.filters.length;x++){
if((this.filters[x]["filter"])&&(!this.filters[x].filter(_e))||(_e.level<this.cutOffLevel)){
return false;
}
}
return true;
},addHandler:function(_10){
this.handlers.push(_10);
return this.handlers.length-1;
},handle:function(rec){
if((!this.filter(rec))||(rec.level<this.cutOffLevel)){
return false;
}
for(var x=0;x<this.handlers.length;x++){
if(this.handlers[x]["handle"]){
this.handlers[x].handle(rec);
}
}
return true;
},log:function(lvl,msg){
if((this.propagate)&&(this.parent)&&(this.parent.rec.level>=this.cutOffLevel)){
this.parent.log(lvl,msg);
return false;
}
this.handle(new dojo.logging.Record(lvl,msg));
return true;
},debug:function(msg){
return this.logType("DEBUG",this._argsToArr(arguments));
},info:function(msg){
return this.logType("INFO",this._argsToArr(arguments));
},warning:function(msg){
return this.logType("WARNING",this._argsToArr(arguments));
},error:function(msg){
return this.logType("ERROR",this._argsToArr(arguments));
},critical:function(msg){
return this.logType("CRITICAL",this._argsToArr(arguments));
},exception:function(msg,e,_1c){
if(e){
var _1d=[e.name,(e.description||e.message)];
if(e.fileName){
_1d.push(e.fileName);
_1d.push("line "+e.lineNumber);
}
msg+=" "+_1d.join(" : ");
}
this.logType("ERROR",msg);
if(!_1c){
throw e;
}
},logType:function(_1e,_1f){
return this.log.apply(this,[dojo.logging.log.getLevel(_1e),_1f]);
},warn:function(){
this.warning.apply(this,arguments);
},err:function(){
this.error.apply(this,arguments);
},crit:function(){
this.critical.apply(this,arguments);
}});
dojo.logging.LogHandler=function(_20){
this.cutOffLevel=(_20)?_20:0;
this.formatter=null;
this.data=[];
this.filters=[];
};
dojo.lang.extend(dojo.logging.LogHandler,{setFormatter:function(_21){
dojo.unimplemented("setFormatter");
},flush:function(){
},close:function(){
},handleError:function(){
dojo.deprecated("dojo.logging.LogHandler.handleError","use handle()","0.6");
},handle:function(_22){
if((this.filter(_22))&&(_22.level>=this.cutOffLevel)){
this.emit(_22);
}
},emit:function(_23){
dojo.unimplemented("emit");
}});
void (function(){
var _24=["setLevel","addFilter","removeFilterByIndex","removeFilter","removeAllFilters","filter"];
var tgt=dojo.logging.LogHandler.prototype;
var src=dojo.logging.Logger.prototype;
for(var x=0;x<_24.length;x++){
tgt[_24[x]]=src[_24[x]];
}
})();
dojo.logging.log=new dojo.logging.Logger();
dojo.logging.log.levels=[{"name":"DEBUG","level":1},{"name":"INFO","level":2},{"name":"WARNING","level":3},{"name":"ERROR","level":4},{"name":"CRITICAL","level":5}];
dojo.logging.log.loggers={};
dojo.logging.log.getLogger=function(_28){
if(!this.loggers[_28]){
this.loggers[_28]=new dojo.logging.Logger();
this.loggers[_28].parent=this;
}
return this.loggers[_28];
};
dojo.logging.log.getLevelName=function(lvl){
for(var x=0;x<this.levels.length;x++){
if(this.levels[x].level==lvl){
return this.levels[x].name;
}
}
return null;
};
dojo.logging.log.getLevel=function(_2b){
for(var x=0;x<this.levels.length;x++){
if(this.levels[x].name.toUpperCase()==_2b.toUpperCase()){
return this.levels[x].level;
}
}
return null;
};
dojo.declare("dojo.logging.MemoryLogHandler",dojo.logging.LogHandler,{initializer:function(_2d,_2e,_2f,_30){
dojo.logging.LogHandler.call(this,_2d);
this.numRecords=(typeof djConfig["loggingNumRecords"]!="undefined")?djConfig["loggingNumRecords"]:((_2e)?_2e:-1);
this.postType=(typeof djConfig["loggingPostType"]!="undefined")?djConfig["loggingPostType"]:(_2f||-1);
this.postInterval=(typeof djConfig["loggingPostInterval"]!="undefined")?djConfig["loggingPostInterval"]:(_2f||-1);
},emit:function(_31){
if(!djConfig.isDebug){
return;
}
var _32=String(dojo.log.getLevelName(_31.level)+": "+_31.time.toLocaleTimeString())+": "+_31.message;
if(!dj_undef("println",dojo.hostenv)){
dojo.hostenv.println(_32,_31.msgArgs);
}
this.data.push(_31);
if(this.numRecords!=-1){
while(this.data.length>this.numRecords){
this.data.shift();
}
}
}});
dojo.logging.logQueueHandler=new dojo.logging.MemoryLogHandler(0,50,0,10000);
dojo.logging.log.addHandler(dojo.logging.logQueueHandler);
dojo.log=dojo.logging.log;
