

dojo.provide("dojo.lang.timing.Timer");
dojo.require("dojo.lang.func");

dojo.lang.timing.Timer = function( interval){



this.timer = null;
this.isRunning = false;
this.interval = interval;

this.onStart = null;
this.onStop = null;
};

dojo.extend(dojo.lang.timing.Timer, {
onTick : function(){
// summary: Method called every time the interval passes.  Override to do something useful.
},

setInterval : function(interval){
// summary: Reset the interval of a timer, whether running or not.
// interval: New interval, in milliseconds.
if (this.isRunning){
dj_global.clearInterval(this.timer);
}
this.interval = interval;
if (this.isRunning){
this.timer = dj_global.setInterval(dojo.lang.hitch(this, "onTick"), this.interval);
}
},

start : function(){
// summary: Start the timer ticking.
// description: Calls the "onStart()" handler, if defined.
// 				Note that the onTick() function is not called right away,
//				only after first interval passes.
if (typeof this.onStart == "function"){
this.onStart();
}
this.isRunning = true;
this.timer = dj_global.setInterval(dojo.lang.hitch(this, "onTick"), this.interval);
},

stop : function(){
// summary: Stop the timer.
// description: Calls the "onStop()" handler, if defined.
if (typeof this.onStop == "function"){
this.onStop();
}
this.isRunning = false;
dj_global.clearInterval(this.timer);
}
});
