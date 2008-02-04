dojo.provide("dojo.lfx.Animation");
dojo.require("dojo.lang.func");
dojo.lfx.Line=function(_1,_2){
this.start=_1;
this.end=_2;
if(dojo.lang.isArray(_1)){
var _3=[];
dojo.lang.forEach(this.start,function(s,i){
_3[i]=this.end[i]-s;
},this);
this.getValue=function(n){
var _7=[];
dojo.lang.forEach(this.start,function(s,i){
_7[i]=(_3[i]*n)+s;
},this);
return _7;
};
}else{
var _3=_2-_1;
this.getValue=function(n){
return (_3*n)+this.start;
};
}
};
if((dojo.render.html.khtml)&&(!dojo.render.html.safari)){
dojo.lfx.easeDefault=function(n){
return (parseFloat("0.5")+((Math.sin((n+parseFloat("1.5"))*Math.PI))/2));
};
}else{
dojo.lfx.easeDefault=function(n){
return (0.5+((Math.sin((n+1.5)*Math.PI))/2));
};
}
dojo.lfx.easeIn=function(n){
return Math.pow(n,3);
};
dojo.lfx.easeOut=function(n){
return (1-Math.pow(1-n,3));
};
dojo.lfx.easeInOut=function(n){
return ((3*Math.pow(n,2))-(2*Math.pow(n,3)));
};
dojo.lfx.IAnimation=function(){
};
dojo.lang.extend(dojo.lfx.IAnimation,{curve:null,duration:1000,easing:null,repeatCount:0,rate:10,handler:null,beforeBegin:null,onBegin:null,onAnimate:null,onEnd:null,onPlay:null,onPause:null,onStop:null,play:null,pause:null,stop:null,connect:function(evt,_11,_12){
if(!_12){
_12=_11;
_11=this;
}
_12=dojo.lang.hitch(_11,_12);
var _13=this[evt]||function(){
};
this[evt]=function(){
var ret=_13.apply(this,arguments);
_12.apply(this,arguments);
return ret;
};
return this;
},fire:function(evt,_16){
if(this[evt]){
this[evt].apply(this,(_16||[]));
}
return this;
},repeat:function(_17){
this.repeatCount=_17;
return this;
},_active:false,_paused:false});
dojo.lfx.Animation=function(_18,_19,_1a,_1b,_1c,_1d){
dojo.lfx.IAnimation.call(this);
if(dojo.lang.isNumber(_18)||(!_18&&_19.getValue)){
_1d=_1c;
_1c=_1b;
_1b=_1a;
_1a=_19;
_19=_18;
_18=null;
}else{
if(_18.getValue||dojo.lang.isArray(_18)){
_1d=_1b;
_1c=_1a;
_1b=_19;
_1a=_18;
_19=null;
_18=null;
}
}
if(dojo.lang.isArray(_1a)){
this.curve=new dojo.lfx.Line(_1a[0],_1a[1]);
}else{
this.curve=_1a;
}
if(_19!=null&&_19>0){
this.duration=_19;
}
if(_1c){
this.repeatCount=_1c;
}
if(_1d){
this.rate=_1d;
}
if(_18){
dojo.lang.forEach(["handler","beforeBegin","onBegin","onEnd","onPlay","onStop","onAnimate"],function(_1e){
if(_18[_1e]){
this.connect(_1e,_18[_1e]);
}
},this);
}
if(_1b&&dojo.lang.isFunction(_1b)){
this.easing=_1b;
}
};
dojo.inherits(dojo.lfx.Animation,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Animation,{_startTime:null,_endTime:null,_timer:null,_percent:0,_startRepeatCount:0,play:function(_1f,_20){
if(_20){
clearTimeout(this._timer);
this._active=false;
this._paused=false;
this._percent=0;
}else{
if(this._active&&!this._paused){
return this;
}
}
this.fire("handler",["beforeBegin"]);
this.fire("beforeBegin");
if(_1f>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_20);
}),_1f);
return this;
}
this._startTime=new Date().valueOf();
if(this._paused){
this._startTime-=(this.duration*this._percent/100);
}
this._endTime=this._startTime+this.duration;
this._active=true;
this._paused=false;
var _21=this._percent/100;
var _22=this.curve.getValue(_21);
if(this._percent==0){
if(!this._startRepeatCount){
this._startRepeatCount=this.repeatCount;
}
this.fire("handler",["begin",_22]);
this.fire("onBegin",[_22]);
}
this.fire("handler",["play",_22]);
this.fire("onPlay",[_22]);
this._cycle();
return this;
},pause:function(){
clearTimeout(this._timer);
if(!this._active){
return this;
}
this._paused=true;
var _23=this.curve.getValue(this._percent/100);
this.fire("handler",["pause",_23]);
this.fire("onPause",[_23]);
return this;
},gotoPercent:function(pct,_25){
clearTimeout(this._timer);
this._active=true;
this._paused=true;
this._percent=pct;
if(_25){
this.play();
}
return this;
},stop:function(_26){
clearTimeout(this._timer);
var _27=this._percent/100;
if(_26){
_27=1;
}
var _28=this.curve.getValue(_27);
this.fire("handler",["stop",_28]);
this.fire("onStop",[_28]);
this._active=false;
this._paused=false;
return this;
},status:function(){
if(this._active){
return this._paused?"paused":"playing";
}else{
return "stopped";
}
return this;
},_cycle:function(){
clearTimeout(this._timer);
if(this._active){
var _29=new Date().valueOf();
var _2a=(_29-this._startTime)/(this._endTime-this._startTime);
if(_2a>=1){
_2a=1;
this._percent=100;
}else{
this._percent=_2a*100;
}
if((this.easing)&&(dojo.lang.isFunction(this.easing))){
_2a=this.easing(_2a);
}
var _2b=this.curve.getValue(_2a);
this.fire("handler",["animate",_2b]);
this.fire("onAnimate",[_2b]);
if(_2a<1){
this._timer=setTimeout(dojo.lang.hitch(this,"_cycle"),this.rate);
}else{
this._active=false;
this.fire("handler",["end"]);
this.fire("onEnd");
if(this.repeatCount>0){
this.repeatCount--;
this.play(null,true);
}else{
if(this.repeatCount==-1){
this.play(null,true);
}else{
if(this._startRepeatCount){
this.repeatCount=this._startRepeatCount;
this._startRepeatCount=0;
}
}
}
}
}
return this;
}});
dojo.lfx.Combine=function(_2c){
dojo.lfx.IAnimation.call(this);
this._anims=[];
this._animsEnded=0;
var _2d=arguments;
if(_2d.length==1&&(dojo.lang.isArray(_2d[0])||dojo.lang.isArrayLike(_2d[0]))){
_2d=_2d[0];
}
dojo.lang.forEach(_2d,function(_2e){
this._anims.push(_2e);
_2e.connect("onEnd",dojo.lang.hitch(this,"_onAnimsEnded"));
},this);
};
dojo.inherits(dojo.lfx.Combine,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Combine,{_animsEnded:0,play:function(_2f,_30){
if(!this._anims.length){
return this;
}
this.fire("beforeBegin");
if(_2f>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_30);
}),_2f);
return this;
}
if(_30||this._anims[0].percent==0){
this.fire("onBegin");
}
this.fire("onPlay");
this._animsCall("play",null,_30);
return this;
},pause:function(){
this.fire("onPause");
this._animsCall("pause");
return this;
},stop:function(_31){
this.fire("onStop");
this._animsCall("stop",_31);
return this;
},_onAnimsEnded:function(){
this._animsEnded++;
if(this._animsEnded>=this._anims.length){
this.fire("onEnd");
}
return this;
},_animsCall:function(_32){
var _33=[];
if(arguments.length>1){
for(var i=1;i<arguments.length;i++){
_33.push(arguments[i]);
}
}
var _35=this;
dojo.lang.forEach(this._anims,function(_36){
_36[_32](_33);
},_35);
return this;
}});
dojo.lfx.Chain=function(_37){
dojo.lfx.IAnimation.call(this);
this._anims=[];
this._currAnim=-1;
var _38=arguments;
if(_38.length==1&&(dojo.lang.isArray(_38[0])||dojo.lang.isArrayLike(_38[0]))){
_38=_38[0];
}
var _39=this;
dojo.lang.forEach(_38,function(_3a,i,_3c){
this._anims.push(_3a);
if(i<_3c.length-1){
_3a.connect("onEnd",dojo.lang.hitch(this,"_playNext"));
}else{
_3a.connect("onEnd",dojo.lang.hitch(this,function(){
this.fire("onEnd");
}));
}
},this);
};
dojo.inherits(dojo.lfx.Chain,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Chain,{_currAnim:-1,play:function(_3d,_3e){
if(!this._anims.length){
return this;
}
if(_3e||!this._anims[this._currAnim]){
this._currAnim=0;
}
var _3f=this._anims[this._currAnim];
this.fire("beforeBegin");
if(_3d>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_3e);
}),_3d);
return this;
}
if(_3f){
if(this._currAnim==0){
this.fire("handler",["begin",this._currAnim]);
this.fire("onBegin",[this._currAnim]);
}
this.fire("onPlay",[this._currAnim]);
_3f.play(null,_3e);
}
return this;
},pause:function(){
if(this._anims[this._currAnim]){
this._anims[this._currAnim].pause();
this.fire("onPause",[this._currAnim]);
}
return this;
},playPause:function(){
if(this._anims.length==0){
return this;
}
if(this._currAnim==-1){
this._currAnim=0;
}
var _40=this._anims[this._currAnim];
if(_40){
if(!_40._active||_40._paused){
this.play();
}else{
this.pause();
}
}
return this;
},stop:function(){
var _41=this._anims[this._currAnim];
if(_41){
_41.stop();
this.fire("onStop",[this._currAnim]);
}
return _41;
},_playNext:function(){
if(this._currAnim==-1||this._anims.length==0){
return this;
}
this._currAnim++;
if(this._anims[this._currAnim]){
this._anims[this._currAnim].play(null,true);
}
return this;
}});
dojo.lfx.combine=function(_42){
var _43=arguments;
if(dojo.lang.isArray(arguments[0])){
_43=arguments[0];
}
if(_43.length==1){
return _43[0];
}
return new dojo.lfx.Combine(_43);
};
dojo.lfx.chain=function(_44){
var _45=arguments;
if(dojo.lang.isArray(arguments[0])){
_45=arguments[0];
}
if(_45.length==1){
return _45[0];
}
return new dojo.lfx.Chain(_45);
};
