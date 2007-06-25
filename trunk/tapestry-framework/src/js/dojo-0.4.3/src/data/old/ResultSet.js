dojo.provide("dojo.data.old.ResultSet");
dojo.require("dojo.lang.assert");
dojo.require("dojo.collections.Collections");
dojo.data.old.ResultSet=function(_1,_2){
dojo.lang.assertType(_1,dojo.data.old.provider.Base,{optional:true});
dojo.lang.assertType(_2,Array,{optional:true});
dojo.data.old.Observable.call(this);
this._dataProvider=_1;
this._arrayOfItems=[];
if(_2){
this._arrayOfItems=_2;
}
};
dojo.inherits(dojo.data.old.ResultSet,dojo.data.old.Observable);
dojo.data.old.ResultSet.prototype.toString=function(){
var _3=this._arrayOfItems.join(", ");
return _3;
};
dojo.data.old.ResultSet.prototype.toArray=function(){
return this._arrayOfItems;
};
dojo.data.old.ResultSet.prototype.getIterator=function(){
return new dojo.collections.Iterator(this._arrayOfItems);
};
dojo.data.old.ResultSet.prototype.getLength=function(){
return this._arrayOfItems.length;
};
dojo.data.old.ResultSet.prototype.getItemAt=function(_4){
return this._arrayOfItems[_4];
};
dojo.data.old.ResultSet.prototype.indexOf=function(_5){
return dojo.lang.indexOf(this._arrayOfItems,_5);
};
dojo.data.old.ResultSet.prototype.contains=function(_6){
return dojo.lang.inArray(this._arrayOfItems,_6);
};
dojo.data.old.ResultSet.prototype.getDataProvider=function(){
return this._dataProvider;
};
