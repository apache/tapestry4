dojo.provide("dojo.debug");
dojo.debug=function(){
if(!djConfig.isDebug){
return;
}
var _1=arguments;
if(dj_undef("println",dojo.hostenv)){
dojo.raise("dojo.debug not available (yet?)");
}
var _2=dj_global["jum"]&&!dj_global["jum"].isBrowser;
var s=[(_2?"":"DEBUG: ")];
for(var i=0;i<_1.length;++i){
if(!false&&_1[i]&&_1[i] instanceof Error){
var _5="["+_1[i].name+": "+dojo.errorToString(_1[i])+(_1[i].fileName?", file: "+_1[i].fileName:"")+(_1[i].lineNumber?", line: "+_1[i].lineNumber:"")+"]";
}else{
try{
var _5=String(_1[i]);
}
catch(e){
if(dojo.render.html.ie){
var _5="[ActiveXObject]";
}else{
var _5="[unknown]";
}
}
}
s.push(_5);
}
dojo.hostenv.println(s.join(" "));
};
dojo.debugShallow=function(_6){
if(!djConfig.isDebug){
return;
}
dojo.debug("------------------------------------------------------------");
dojo.debug("Object: "+_6);
var _7=[];
for(var _8 in _6){
try{
_7.push(_8+": "+_6[_8]);
}
catch(E){
_7.push(_8+": ERROR - "+E.message);
}
}
_7.sort();
for(var i=0;i<_7.length;i++){
dojo.debug(_7[i]);
}
dojo.debug("------------------------------------------------------------");
};
dojo.debugDeep=function(_a){
if(!djConfig.isDebug){
return;
}
if(!dojo.uri||!dojo.uri.dojoUri){
return dojo.debug("You'll need to load dojo.uri.* for deep debugging - sorry!");
}
if(!window.open){
return dojo.debug("Deep debugging is only supported in host environments with window.open");
}
var _b=dojo.debugDeep.debugVars.length;
dojo.debugDeep.debugVars.push(_a);
var _c=(djConfig["dojoDebugDeepHtmlUrl"]||new dojo.uri.Uri(location,dojo.uri.moduleUri("dojo.debug","deep.html")).toString())+"?var="+_b;
var _d=window.open(_c,"_blank","width=600, height=400, resizable=yes, scrollbars=yes, status=yes");
try{
_d.debugVar=_a;
}
catch(e){
}
};
dojo.debugDeep.debugVars=[];
