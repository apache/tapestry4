dojo.provide("dojo.data.old.format.Json");
dojo.require("dojo.lang.assert");
dojo.data.old.format.Json=new function(){
this.loadDataProviderFromFileContents=function(_1,_2){
dojo.lang.assertType(_1,dojo.data.old.provider.Base);
dojo.lang.assertType(_2,String);
var _3=eval("("+_2+")");
this.loadDataProviderFromArrayOfJsonData(_1,_3);
};
this.loadDataProviderFromArrayOfJsonData=function(_4,_5){
dojo.lang.assertType(_5,Array,{optional:true});
if(_5&&(_5.length>0)){
var _6=_5[0];
dojo.lang.assertType(_6,[Array,"pureobject"]);
if(dojo.lang.isArray(_6)){
_loadDataProviderFromArrayOfArrays(_4,_5);
}else{
dojo.lang.assertType(_6,"pureobject");
_loadDataProviderFromArrayOfObjects(_4,_5);
}
}
};
this.getJsonStringFromResultSet=function(_7){
dojo.unimplemented("dojo.data.old.format.Json.getJsonStringFromResultSet");
var _8=null;
return _8;
};
function _loadDataProviderFromArrayOfArrays(_9,_a){
var _b=_a[0];
for(var i=1;i<_a.length;++i){
var _d=_a[i];
var _e=_9.getNewItemToLoad();
for(var j in _d){
var _10=_d[j];
var key=_b[j];
_e.load(key,_10);
}
}
}
function _loadDataProviderFromArrayOfObjects(_12,_13){
for(var i in _13){
var row=_13[i];
var _16=_12.getNewItemToLoad();
for(var key in row){
var _18=row[key];
if(dojo.lang.isArray(_18)){
var _19=_18;
for(var j in _19){
_18=_19[j];
_16.load(key,_18);
}
}else{
_16.load(key,_18);
}
}
}
}
}();
