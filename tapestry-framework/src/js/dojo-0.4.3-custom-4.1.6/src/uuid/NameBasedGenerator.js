dojo.provide("dojo.uuid.NameBasedGenerator");
dojo.uuid.NameBasedGenerator=new function(){
this.generate=function(_1){
dojo.unimplemented("dojo.uuid.NameBasedGenerator.generate");
var _2="00000000-0000-0000-0000-000000000000";
if(_1&&(_1!=String)){
_2=new _1(_2);
}
return _2;
};
}();
