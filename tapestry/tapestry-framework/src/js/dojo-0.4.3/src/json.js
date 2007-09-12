dojo.provide("dojo.json");
dojo.require("dojo.lang.func");
dojo.require("dojo.string.extras");
dojo.require("dojo.AdapterRegistry");
dojo.json={jsonRegistry:new dojo.AdapterRegistry(),register:function(_1,_2,_3,_4){
dojo.json.jsonRegistry.register(_1,_2,_3,_4);
},evalJson:function(_5){
try{
return eval("("+_5+")");
}
catch(e){
dojo.debug(e);
return _5;
}
},serialize:function(o){
var _7=typeof (o);
if(_7=="undefined"){
return "undefined";
}else{
if((_7=="number")||(_7=="boolean")){
return o+"";
}else{
if(o===null){
return "null";
}
}
}
if(_7=="string"){
return dojo.string.escapeString(o);
}
var me=arguments.callee;
var _9;
if(typeof (o.__json__)=="function"){
_9=o.__json__();
if(o!==_9){
return me(_9);
}
}
if(typeof (o.json)=="function"){
_9=o.json();
if(o!==_9){
return me(_9);
}
}
if(_7!="function"&&typeof (o.length)=="number"){
var _a=[];
for(var i=0;i<o.length;i++){
var _c=me(o[i]);
if(typeof (_c)!="string"){
_c="undefined";
}
_a.push(_c);
}
return "["+_a.join(",")+"]";
}
try{
window.o=o;
_9=dojo.json.jsonRegistry.match(o);
return me(_9);
}
catch(e){
}
if(_7=="function"){
return null;
}
_a=[];
for(var k in o){
var _e;
if(typeof (k)=="number"){
_e="\""+k+"\"";
}else{
if(typeof (k)=="string"){
_e=dojo.string.escapeString(k);
}else{
continue;
}
}
_c=me(o[k]);
if(typeof (_c)!="string"){
continue;
}
_a.push(_e+":"+_c);
}
return "{"+_a.join(",")+"}";
}};
