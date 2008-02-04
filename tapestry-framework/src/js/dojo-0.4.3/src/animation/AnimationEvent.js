dojo.provide("dojo.animation.AnimationEvent");
dojo.require("dojo.lang.common");
dojo.deprecated("dojo.animation.AnimationEvent is slated for removal in 0.5; use dojo.lfx.* instead.","0.5");
dojo.animation.AnimationEvent=function(_1,_2,_3,_4,_5,_6,_7,_8,_9){
this.type=_2;
this.animation=_1;
this.coords=_3;
this.x=_3[0];
this.y=_3[1];
this.z=_3[2];
this.startTime=_4;
this.currentTime=_5;
this.endTime=_6;
this.duration=_7;
this.percent=_8;
this.fps=_9;
};
dojo.extend(dojo.animation.AnimationEvent,{coordsAsInts:function(){
var _a=new Array(this.coords.length);
for(var i=0;i<this.coords.length;i++){
_a[i]=Math.round(this.coords[i]);
}
return _a;
}});
