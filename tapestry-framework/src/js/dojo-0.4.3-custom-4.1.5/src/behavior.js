dojo.provide("dojo.behavior");
dojo.require("dojo.event.*");
dojo.require("dojo.experimental");
dojo.experimental("dojo.behavior");
dojo.behavior=new function(){
function arrIn(_1,_2){
if(!_1[_2]){
_1[_2]=[];
}
return _1[_2];
}
function forIn(_3,_4,_5){
var _6={};
for(var x in _3){
if(typeof _6[x]=="undefined"){
if(!_5){
_4(_3[x],x);
}else{
_5.call(_4,_3[x],x);
}
}
}
}
this.behaviors={};
this.add=function(_8){
var _9={};
forIn(_8,this,function(_a,_b){
var _c=arrIn(this.behaviors,_b);
if((dojo.lang.isString(_a))||(dojo.lang.isFunction(_a))){
_a={found:_a};
}
forIn(_a,function(_d,_e){
arrIn(_c,_e).push(_d);
});
});
};
this.apply=function(){
dojo.profile.start("dojo.behavior.apply");
var r=dojo.render.html;
var _10=(!r.safari);
if(r.safari){
var uas=r.UA.split("AppleWebKit/")[1];
if(parseInt(uas.match(/[0-9.]{3,}/))>=420){
_10=true;
}
}
if((dj_undef("behaviorFastParse",djConfig)?(_10):djConfig["behaviorFastParse"])){
this.applyFast();
}else{
this.applySlow();
}
dojo.profile.end("dojo.behavior.apply");
};
this.matchCache={};
this.elementsById=function(id,_13){
var _14=[];
var _15=[];
arrIn(this.matchCache,id);
if(_13){
var _16=this.matchCache[id];
for(var x=0;x<_16.length;x++){
if(_16[x].id!=""){
_14.push(_16[x]);
_16.splice(x,1);
x--;
}
}
}
var _18=dojo.byId(id);
while(_18){
if(!_18["idcached"]){
_15.push(_18);
}
_18.id="";
_18=dojo.byId(id);
}
this.matchCache[id]=this.matchCache[id].concat(_15);
dojo.lang.forEach(this.matchCache[id],function(_19){
_19.id=id;
_19.idcached=true;
});
return {"removed":_14,"added":_15,"match":this.matchCache[id]};
};
this.applyToNode=function(_1a,_1b,_1c){
if(typeof _1b=="string"){
dojo.event.topic.registerPublisher(_1b,_1a,_1c);
}else{
if(typeof _1b=="function"){
if(_1c=="found"){
_1b(_1a);
}else{
dojo.event.connect(_1a,_1c,_1b);
}
}else{
_1b.srcObj=_1a;
_1b.srcFunc=_1c;
dojo.event.kwConnect(_1b);
}
}
};
this.applyFast=function(){
dojo.profile.start("dojo.behavior.applyFast");
forIn(this.behaviors,function(_1d,id){
var _1f=dojo.behavior.elementsById(id);
dojo.lang.forEach(_1f.added,function(_20){
forIn(_1d,function(_21,_22){
if(dojo.lang.isArray(_21)){
dojo.lang.forEach(_21,function(_23){
dojo.behavior.applyToNode(_20,_23,_22);
});
}
});
});
});
dojo.profile.end("dojo.behavior.applyFast");
};
this.applySlow=function(){
dojo.profile.start("dojo.behavior.applySlow");
var all=document.getElementsByTagName("*");
var _25=all.length;
for(var x=0;x<_25;x++){
var _27=all[x];
if((_27.id)&&(!_27["behaviorAdded"])&&(this.behaviors[_27.id])){
_27["behaviorAdded"]=true;
forIn(this.behaviors[_27.id],function(_28,_29){
if(dojo.lang.isArray(_28)){
dojo.lang.forEach(_28,function(_2a){
dojo.behavior.applyToNode(_27,_2a,_29);
});
}
});
}
}
dojo.profile.end("dojo.behavior.applySlow");
};
};
dojo.addOnLoad(dojo.behavior,"apply");
