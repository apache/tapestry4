dojo.require("dojo.event.common");
dojo.provide("dojo.event.topic");
dojo.event.topic=new function(){
this.topics={};
this.getTopic=function(_1){
if(!this.topics[_1]){
this.topics[_1]=new this.TopicImpl(_1);
}
return this.topics[_1];
};
this.registerPublisher=function(_2,_3,_4){
var _2=this.getTopic(_2);
_2.registerPublisher(_3,_4);
};
this.subscribe=function(_5,_6,_7){
var _5=this.getTopic(_5);
_5.subscribe(_6,_7);
};
this.unsubscribe=function(_8,_9,_a){
var _8=this.getTopic(_8);
_8.unsubscribe(_9,_a);
};
this.destroy=function(_b){
this.getTopic(_b).destroy();
delete this.topics[_b];
};
this.publishApply=function(_c,_d){
var _c=this.getTopic(_c);
_c.sendMessage.apply(_c,_d);
};
this.publish=function(_e,_f){
var _e=this.getTopic(_e);
var _10=[];
for(var x=1;x<arguments.length;x++){
_10.push(arguments[x]);
}
_e.sendMessage.apply(_e,_10);
};
};
dojo.event.topic.TopicImpl=function(_12){
this.topicName=_12;
this.subscribe=function(_13,_14){
var tf=_14||_13;
var to=(!_14)?dj_global:_13;
return dojo.event.kwConnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
this.unsubscribe=function(_17,_18){
var tf=(!_18)?_17:_18;
var to=(!_18)?null:_17;
return dojo.event.kwDisconnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
this._getJoinPoint=function(){
return dojo.event.MethodJoinPoint.getForMethod(this,"sendMessage");
};
this.setSquelch=function(_1b){
this._getJoinPoint().squelch=_1b;
};
this.destroy=function(){
this._getJoinPoint().disconnect();
};
this.registerPublisher=function(_1c,_1d){
dojo.event.connect(_1c,_1d,this,"sendMessage");
};
this.sendMessage=function(_1e){
};
};
