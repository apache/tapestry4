dojo.provide("dojo.debug.Firebug");
dojo.deprecated("dojo.debug.Firebug is slated for removal in 0.5; use dojo.debug.console instead.","0.5");
if(dojo.render.html.moz){
if(console&&console.log){
var consoleLog=function(){
if(!djConfig.isDebug){
return;
}
var _1=dojo.lang.toArray(arguments);
_1.splice(0,0,"DEBUG: ");
console.log.apply(console,_1);
};
dojo.debug=consoleLog;
dojo.debugDeep=consoleLog;
dojo.debugShallow=function(_2){
if(!djConfig.isDebug){
return;
}
if(dojo.lang.isArray(_2)){
console.log("Array: ",_2);
for(var i=0;x<_2.length;i++){
console.log("    ","["+i+"]",_2[i]);
}
}else{
console.log("Object: ",_2);
var _4=[];
for(var _5 in _2){
_4.push(_5);
}
_4.sort();
dojo.lang.forEach(_4,function(_6){
try{
console.log("    ",_6,_2[_6]);
}
catch(e){
console.log("    ",_6,"ERROR",e.message,e);
}
});
}
};
}else{
dojo.debug("dojo.debug.Firebug requires Firebug > 0.4");
}
}
