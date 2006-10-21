

dojo.provide("dojo.lang.func");

dojo.require("dojo.lang.common");


dojo.lang.hitch = function(thisObject, method){
var fcn = (dojo.lang.isString(method) ? thisObject[method] : method) || function(){};

return function() {
return fcn.apply(thisObject, arguments);
};
}

dojo.lang.anonCtr = 0;
dojo.lang.anon = {};
dojo.lang.nameAnonFunc = function(anonFuncPtr, namespaceObj, searchForNames){
var nso = (namespaceObj || dojo.lang.anon);
if( (searchForNames) ||
((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"] == true)) ){
for(var x in nso){
try{
if(nso[x] === anonFuncPtr){
return x;
}
}catch(e){} // window.external fails in IE embedded in Eclipse (Eclipse bug #151165)
}
}
var ret = "__"+dojo.lang.anonCtr++;
while(typeof nso[ret] != "undefined"){
ret = "__"+dojo.lang.anonCtr++;
}
nso[ret] = anonFuncPtr;
return ret;
}

dojo.lang.forward = function(funcName){

return function(){
return this[funcName].apply(this, arguments);
};
}

dojo.lang.curry = function(ns, func ){
var outerArgs = [];
ns = ns||dj_global;
if(dojo.lang.isString(func)){
func = ns[func];
}
for(var x=2; x<arguments.length; x++){
outerArgs.push(arguments[x]);
}




var ecount = (func["__preJoinArity"]||func.length) - outerArgs.length;

function gather(nextArgs, innerArgs, expected){
var texpected = expected;
var totalArgs = innerArgs.slice(0); // copy
for(var x=0; x<nextArgs.length; x++){
totalArgs.push(nextArgs[x]);
}
// check the list of provided nextArgs to see if it, plus the
// number of innerArgs already supplied, meets the total
// expected.
expected = expected-nextArgs.length;
if(expected<=0){
var res = func.apply(ns, totalArgs);
expected = texpected;
return res;
}else{
return function(){
return gather(arguments,// check to see if we've been run
// with enough args
totalArgs,	// a copy
expected);	// how many more do we need to run?;
};
}
}
return gather([], outerArgs, ecount);
}

dojo.lang.curryArguments = function(ns, func, args, offset){
var targs = [];
var x = offset||0;
for(x=offset; x<args.length; x++){
targs.push(args[x]); // ensure that it's an arr
}
return dojo.lang.curry.apply(dojo.lang, [ns, func].concat(targs));
}

dojo.lang.tryThese = function(){
for(var x=0; x<arguments.length; x++){
try{
if(typeof arguments[x] == "function"){
var ret = (arguments[x]());
if(ret){
return ret;
}
}
}catch(e){
dojo.debug(e);
}
}
}

dojo.lang.delayThese = function(farr, cb, delay, onend){

if(!farr.length){
if(typeof onend == "function"){
onend();
}
return;
}
if((typeof delay == "undefined")&&(typeof cb == "number")){
delay = cb;
cb = function(){};
}else if(!cb){
cb = function(){};
if(!delay){ delay = 0; }
}
setTimeout(function(){
(farr.shift())();
cb();
dojo.lang.delayThese(farr, cb, delay, onend);
}, delay);
}
