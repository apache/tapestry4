













dojo.provide("dojo.logging.Logger");
dojo.require("dojo.lang.common");



dojo.logging.Record = function(lvl, msg){
this.level = lvl;
this.message = "";
this.msgArgs = [];
this.time = new Date();

if(dojo.lang.isArray(msg)){
if(msg.length > 0 && dojo.lang.isString(msg[0])){
this.message=msg.shift();
}
this.msgArgs=msg;
}else{
this.message=msg;
}

}


dojo.logging.LogFilter = function(loggerChain){
this.passChain = loggerChain || "";
this.filter = function(record){
// FIXME: need to figure out a way to enforce the loggerChain
// restriction
return true; // pass all records
}
}

dojo.logging.Logger = function(){
this.cutOffLevel = 0;
this.propagate = true;
this.parent = null;

this.data = [];
this.filters = [];
this.handlers = [];
}

dojo.extend(dojo.logging.Logger,{
argsToArr: function(args){
// utility function, reproduced from __util__ here to remove dependency
var ret = [];
for(var x=0; x<args.length; x++){
ret.push(args[x]);
}
return ret;
},

setLevel: function(lvl){
this.cutOffLevel = parseInt(lvl);
},

isEnabledFor: function(lvl){
return parseInt(lvl) >= this.cutOffLevel;
},

getEffectiveLevel: function(){
if((this.cutOffLevel==0)&&(this.parent)){
return this.parent.getEffectiveLevel();
}
return this.cutOffLevel;
},

addFilter: function(flt){
this.filters.push(flt);
return this.filters.length-1;
},

removeFilterByIndex: function(fltIndex){
if(this.filters[fltIndex]){
delete this.filters[fltIndex];
return true;
}
return false;
},

removeFilter: function(fltRef){
for(var x=0; x<this.filters.length; x++){
if(this.filters[x]===fltRef){
delete this.filters[x];
return true;
}
}
return false;
},

removeAllFilters: function(){
this.filters = []; // clobber all of them
},

filter: function(rec){
for(var x=0; x<this.filters.length; x++){
if((this.filters[x]["filter"])&&
(!this.filters[x].filter(rec))||
(rec.level<this.cutOffLevel)){
return false;
}
}
return true;
},

addHandler: function(hdlr){
this.handlers.push(hdlr);
return this.handlers.length-1;
},

handle: function(rec){
if((!this.filter(rec))||(rec.level<this.cutOffLevel)){ return false; }
for(var x=0; x<this.handlers.length; x++){
if(this.handlers[x]["handle"]){
this.handlers[x].handle(rec);
}
}
// FIXME: not sure what to do about records to be propagated that may have
// been modified by the handlers or the filters at this logger. Should
// parents always have pristine copies? or is passing the modified record
// OK?
// if((this.propagate)&&(this.parent)){ this.parent.handle(rec); }
return true;
},


log: function(lvl, msg){
if(	(this.propagate)&&(this.parent)&&
(this.parent.rec.level>=this.cutOffLevel)){
this.parent.log(lvl, msg);
return false;
}
// FIXME: need to call logging providers here!
this.handle(new dojo.logging.Record(lvl, msg));
return true;
},


debug:function(msg){
return this.logType("DEBUG", this.argsToArr(arguments));
},

info: function(msg){
return this.logType("INFO", this.argsToArr(arguments));
},

warning: function(msg){
return this.logType("WARNING", this.argsToArr(arguments));
},

error: function(msg){
return this.logType("ERROR", this.argsToArr(arguments));
},

critical: function(msg){
return this.logType("CRITICAL", this.argsToArr(arguments));
},

exception: function(msg, e, squelch){
// FIXME: this needs to be modified to put the exception in the msg
// if we're on Moz, we can get the following from the exception object:
//		lineNumber
//		message
//		fileName
//		stack
//		name
// on IE, we get:
//		name
//		message (from MDA?)
//		number
//		description (same as message!)
if(e){
var eparts = [e.name, (e.description||e.message)];
if(e.fileName){
eparts.push(e.fileName);
eparts.push("line "+e.lineNumber);
// eparts.push(e.stack);
}
msg += " "+eparts.join(" : ");
}

this.logType("ERROR", msg);
if(!squelch){
throw e;
}
},

logType: function(type, args){
return this.log.apply(this, [dojo.logging.log.getLevel(type),
args]);
},

warn:function(){
this.warning.apply(this,arguments);
},
err:function(){
this.error.apply(this,arguments);
},
crit:function(){
this.critical.apply(this,arguments);
}
});


dojo.logging.LogHandler = function(level){
this.cutOffLevel = (level) ? level : 0;
this.formatter = null; // FIXME: default formatter?
this.data = [];
this.filters = [];
}
dojo.lang.extend(dojo.logging.LogHandler,{

setFormatter:function(formatter){
dojo.unimplemented("setFormatter");
},

flush:function(){},
close:function(){},
handleError:function(){},

handle:function(record){
if((this.filter(record))&&(record.level>=this.cutOffLevel)){
this.emit(record);
}
},

emit:function(record){
dojo.unimplemented("emit");
}
});


void(function(){ // begin globals protection closure
var names = [
"setLevel", "addFilter", "removeFilterByIndex", "removeFilter",
"removeAllFilters", "filter"
];
var tgt = dojo.logging.LogHandler.prototype;
var src = dojo.logging.Logger.prototype;
for(var x=0; x<names.length; x++){
tgt[names[x]] = src[names[x]];
}
})(); // end globals protection closure

dojo.logging.log = new dojo.logging.Logger();



dojo.logging.log.levels = [ {"name": "DEBUG", "level": 1},
{"name": "INFO", "level": 2},
{"name": "WARNING", "level": 3},
{"name": "ERROR", "level": 4},
{"name": "CRITICAL", "level": 5} ];

dojo.logging.log.loggers = {};

dojo.logging.log.getLogger = function(name){
if(!this.loggers[name]){
this.loggers[name] = new dojo.logging.Logger();
this.loggers[name].parent = this;
}
return this.loggers[name];
}

dojo.logging.log.getLevelName = function(lvl){
for(var x=0; x<this.levels.length; x++){
if(this.levels[x].level == lvl){
return this.levels[x].name;
}
}
return null;
}

dojo.logging.log.addLevelName = function(name, lvl){
if(this.getLevelName(name)){
this.err("could not add log level "+name+" because a level with that name already exists");
return false;
}
this.levels.append({"name": name, "level": parseInt(lvl)});
return true;
}

dojo.logging.log.getLevel = function(name){
for(var x=0; x<this.levels.length; x++){
if(this.levels[x].name.toUpperCase() == name.toUpperCase()){
return this.levels[x].level;
}
}
return null;
}



dojo.logging.MemoryLogHandler = function(level, recordsToKeep, postType, postInterval){

dojo.logging.LogHandler.call(this, level);

this.numRecords = (typeof djConfig['loggingNumRecords'] != 'undefined') ? djConfig['loggingNumRecords'] : ((recordsToKeep) ? recordsToKeep : -1);

this.postType = (typeof djConfig['loggingPostType'] != 'undefined') ? djConfig['loggingPostType'] : ( postType || -1);

this.postInterval = (typeof djConfig['loggingPostInterval'] != 'undefined') ? djConfig['loggingPostInterval'] : ( postType || -1);
}

dojo.lang.inherits(dojo.logging.MemoryLogHandler, dojo.logging.LogHandler);
dojo.lang.extend(dojo.logging.MemoryLogHandler,{

emit:function(record){
if (!djConfig.isDebug) { return; }

var logStr = String(dojo.log.getLevelName(record.level)+": "
+record.time.toLocaleTimeString())+": "+record.message;
if(!dj_undef("println", dojo.hostenv)){
dojo.hostenv.println(logStr);
}

this.data.push(record);
if(this.numRecords != -1){
while(this.data.length>this.numRecords){
this.data.shift();
}
}
}
});

dojo.logging.logQueueHandler = new dojo.logging.MemoryLogHandler(0,50,0,10000);

dojo.logging.log.addHandler(dojo.logging.logQueueHandler);
dojo.log = dojo.logging.log;
