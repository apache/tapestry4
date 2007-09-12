dojo.provide("dojo.uuid.NilGenerator");
dojo.uuid.NilGenerator=new function(){
this.generate=function(_1){
var _2="00000000-0000-0000-0000-000000000000";
if(_1&&(_1!=String)){
_2=new _1(_2);
}
return _2;
};
}();
