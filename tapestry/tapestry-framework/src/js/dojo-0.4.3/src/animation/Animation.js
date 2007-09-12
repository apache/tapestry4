dojo.provide("dojo.animation.Animation");
dojo.require("dojo.animation.AnimationEvent");
dojo.require("dojo.lang.func");
dojo.require("dojo.math");
dojo.require("dojo.math.curves");
dojo.deprecated("dojo.animation.Animation is slated for removal in 0.5; use dojo.lfx.* instead.","0.5");
dojo.animation.Animation=function(_1,_2,_3,_4,_5){
if(dojo.lang.isArray(_1)){
_1=new dojo.math.curves.Line(_1[0],_1[1]);
}
this.curve=_1;
this.duration=_2;
this.repeatCount=_4||0;
this.rate=_5||25;
if(_3){
if(dojo.lang.isFunction(_3.getValue)){
this.accel=_3;
}else{
var i=0.35*_3+0.5;
this.accel=new dojo.math.curves.CatmullRom([[0],[i],[1]],0.45);
}
}
};
dojo.lang.extend(dojo.animation.Animation,{curve:null,duration:0,repeatCount:0,accel:null,onBegin:null,onAnimate:null,onEnd:null,onPlay:null,onPause:null,onStop:null,handler:null,_animSequence:null,_startTime:null,_endTime:null,_lastFrame:null,_timer:null,_percent:0,_active:false,_paused:false,_startRepeatCount:0,play:function(_7){
if(_7){
clearTimeout(this._timer);
this._active=false;
this._paused=false;
this._percent=0;
}else{
if(this._active&&!this._paused){
return;
}
}
this._startTime=new Date().valueOf();
if(this._paused){
this._startTime-=(this.duration*this._percent/100);
}
this._endTime=this._startTime+this.duration;
this._lastFrame=this._startTime;
var e=new dojo.animation.AnimationEvent(this,null,this.curve.getValue(this._percent),this._startTime,this._startTime,this._endTime,this.duration,this._percent,0);
this._active=true;
this._paused=false;
if(this._percent==0){
if(!this._startRepeatCount){
this._startRepeatCount=this.repeatCount;
}
e.type="begin";
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onBegin=="function"){
this.onBegin(e);
}
}
e.type="play";
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onPlay=="function"){
this.onPlay(e);
}
if(this._animSequence){
this._animSequence._setCurrent(this);
}
this._cycle();
},pause:function(){
clearTimeout(this._timer);
if(!this._active){
return;
}
this._paused=true;
var e=new dojo.animation.AnimationEvent(this,"pause",this.curve.getValue(this._percent),this._startTime,new Date().valueOf(),this._endTime,this.duration,this._percent,0);
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onPause=="function"){
this.onPause(e);
}
},playPause:function(){
if(!this._active||this._paused){
this.play();
}else{
this.pause();
}
},gotoPercent:function(_a,_b){
clearTimeout(this._timer);
this._active=true;
this._paused=true;
this._percent=_a;
if(_b){
this.play();
}
},stop:function(_c){
clearTimeout(this._timer);
var _d=this._percent/100;
if(_c){
_d=1;
}
var e=new dojo.animation.AnimationEvent(this,"stop",this.curve.getValue(_d),this._startTime,new Date().valueOf(),this._endTime,this.duration,this._percent);
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onStop=="function"){
this.onStop(e);
}
this._active=false;
this._paused=false;
},status:function(){
if(this._active){
return this._paused?"paused":"playing";
}else{
return "stopped";
}
},_cycle:function(){
clearTimeout(this._timer);
if(this._active){
var _f=new Date().valueOf();
var _10=(_f-this._startTime)/(this._endTime-this._startTime);
var fps=1000/(_f-this._lastFrame);
this._lastFrame=_f;
if(_10>=1){
_10=1;
this._percent=100;
}else{
this._percent=_10*100;
}
if(this.accel&&this.accel.getValue){
_10=this.accel.getValue(_10);
}
var e=new dojo.animation.AnimationEvent(this,"animate",this.curve.getValue(_10),this._startTime,_f,this._endTime,this.duration,this._percent,Math.round(fps));
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onAnimate=="function"){
this.onAnimate(e);
}
if(_10<1){
this._timer=setTimeout(dojo.lang.hitch(this,"_cycle"),this.rate);
}else{
e.type="end";
this._active=false;
if(typeof this.handler=="function"){
this.handler(e);
}
if(typeof this.onEnd=="function"){
this.onEnd(e);
}
if(this.repeatCount>0){
this.repeatCount--;
this.play(true);
}else{
if(this.repeatCount==-1){
this.play(true);
}else{
if(this._startRepeatCount){
this.repeatCount=this._startRepeatCount;
this._startRepeatCount=0;
}
if(this._animSequence){
this._animSequence._playNext();
}
}
}
}
}
}});
