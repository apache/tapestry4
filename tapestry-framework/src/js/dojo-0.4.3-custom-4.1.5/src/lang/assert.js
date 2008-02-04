dojo.provide("dojo.lang.assert");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.type");
dojo.lang.assert=function(_1,_2){
if(!_1){
var _3="An assert statement failed.\n"+"The method dojo.lang.assert() was called with a 'false' value.\n";
if(_2){
_3+="Here's the assert message:\n"+_2+"\n";
}
throw new Error(_3);
}
};
dojo.lang.assertType=function(_4,_5,_6){
if(dojo.lang.isString(_6)){
dojo.deprecated("dojo.lang.assertType(value, type, \"message\")","use dojo.lang.assertType(value, type) instead","0.5");
}
if(!dojo.lang.isOfType(_4,_5,_6)){
if(!dojo.lang.assertType._errorMessage){
dojo.lang.assertType._errorMessage="Type mismatch: dojo.lang.assertType() failed.";
}
dojo.lang.assert(false,dojo.lang.assertType._errorMessage);
}
};
dojo.lang.assertValidKeywords=function(_7,_8,_9){
var _a;
if(!_9){
if(!dojo.lang.assertValidKeywords._errorMessage){
dojo.lang.assertValidKeywords._errorMessage="In dojo.lang.assertValidKeywords(), found invalid keyword:";
}
_9=dojo.lang.assertValidKeywords._errorMessage;
}
if(dojo.lang.isArray(_8)){
for(_a in _7){
if(!dojo.lang.inArray(_8,_a)){
dojo.lang.assert(false,_9+" "+_a);
}
}
}else{
for(_a in _7){
if(!(_a in _8)){
dojo.lang.assert(false,_9+" "+_a);
}
}
}
};
