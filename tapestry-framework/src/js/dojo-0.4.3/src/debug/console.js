dojo.provide("dojo.debug.console");
dojo.require("dojo.logging.ConsoleLogger");
if(window.console){
if(console.info!=null){
dojo.hostenv.println=function(){
if(!djConfig.isDebug){
return;
}
console.info.apply(console,arguments);
};
dojo.debug=dojo.hostenv.println;
dojo.debugDeep=dojo.debug;
dojo.debugShallow=function(_1,_2,_3){
if(!djConfig.isDebug){
return;
}
_2=(_2!=false);
_3=(_3!=false);
if(_1==null||_1.constructor==null){
return dojo.debug(_1);
}
var _4=_1.declaredClass;
if(_4==null){
_4=_1.constructor.toString().match(/function\s*(.*)\(/);
if(_4){
_4=_4[1];
}
}
if(_4){
if(_4=="String"||_4=="Number"){
return dojo.debug(_4+": ",_1);
}
if(_2&&!_3){
var _5=_1;
}else{
var _6=[];
if(_2){
for(var _7 in _1){
_6.push(_7);
}
}else{
for(var _7 in _1){
if(typeof _1[_7]!="function"){
_6.push(_7);
}else{
dojo.debug(_7);
}
}
}
if(_3){
_6.sort();
}
var _5={};
dojo.lang.forEach(_6,function(_8){
_5[_8]=_1[_8];
});
}
return dojo.debug(_4+": %o\n%2.o",_1,_5);
}
return dojo.debug(_1.constructor+": ",_1);
};
}else{
if(console.log!=null){
dojo.hostenv.println=function(){
if(!djConfig.isDebug){
return;
}
var _9=dojo.lang.toArray(arguments);
console.log("DEBUG: "+_9.join(" "));
};
dojo.debug=dojo.hostenv.println;
}else{
dojo.debug("dojo.debug.console requires Firebug > 0.4");
}
}
}else{
if(dojo.render.html.opera){
if(opera&&opera.postError){
dojo.hostenv.println=opera.postError;
}else{
dojo.debug("dojo.debug.Opera requires Opera > 8.0");
}
}
}
