dojo.provide("dojo.data.old.provider.Base");
dojo.require("dojo.lang.assert");
dojo.data.old.provider.Base=function(){
this._countOfNestedTransactions=0;
this._changesInCurrentTransaction=null;
};
dojo.data.old.provider.Base.prototype.beginTransaction=function(){
if(this._countOfNestedTransactions===0){
this._changesInCurrentTransaction=[];
}
this._countOfNestedTransactions+=1;
};
dojo.data.old.provider.Base.prototype.endTransaction=function(){
this._countOfNestedTransactions-=1;
dojo.lang.assert(this._countOfNestedTransactions>=0);
if(this._countOfNestedTransactions===0){
var _1=this._saveChanges();
this._changesInCurrentTransaction=null;
if(_1.length>0){
this._notifyObserversOfChanges(_1);
}
}
};
dojo.data.old.provider.Base.prototype.getNewItemToLoad=function(){
return this._newItem();
};
dojo.data.old.provider.Base.prototype.newItem=function(_2){
dojo.lang.assertType(_2,String,{optional:true});
var _3=this._newItem();
if(_2){
_3.set("name",_2);
}
return _3;
};
dojo.data.old.provider.Base.prototype.newAttribute=function(_4){
dojo.lang.assertType(_4,String,{optional:true});
var _5=this._newAttribute(_4);
return _5;
};
dojo.data.old.provider.Base.prototype.getAttribute=function(_6){
dojo.unimplemented("dojo.data.old.provider.Base");
var _7;
return _7;
};
dojo.data.old.provider.Base.prototype.getAttributes=function(){
dojo.unimplemented("dojo.data.old.provider.Base");
return this._arrayOfAttributes;
};
dojo.data.old.provider.Base.prototype.fetchArray=function(){
dojo.unimplemented("dojo.data.old.provider.Base");
return [];
};
dojo.data.old.provider.Base.prototype.fetchResultSet=function(){
dojo.unimplemented("dojo.data.old.provider.Base");
var _8;
return _8;
};
dojo.data.old.provider.Base.prototype.noteChange=function(_9,_a,_b){
var _c={item:_9,attribute:_a,value:_b};
if(this._countOfNestedTransactions===0){
this.beginTransaction();
this._changesInCurrentTransaction.push(_c);
this.endTransaction();
}else{
this._changesInCurrentTransaction.push(_c);
}
};
dojo.data.old.provider.Base.prototype.addItemObserver=function(_d,_e){
dojo.lang.assertType(_d,dojo.data.old.Item);
_d.addObserver(_e);
};
dojo.data.old.provider.Base.prototype.removeItemObserver=function(_f,_10){
dojo.lang.assertType(_f,dojo.data.old.Item);
_f.removeObserver(_10);
};
dojo.data.old.provider.Base.prototype._newItem=function(){
var _11=new dojo.data.old.Item(this);
return _11;
};
dojo.data.old.provider.Base.prototype._newAttribute=function(_12){
var _13=new dojo.data.old.Attribute(this);
return _13;
};
dojo.data.old.provider.Base.prototype._saveChanges=function(){
var _14=this._changesInCurrentTransaction;
return _14;
};
dojo.data.old.provider.Base.prototype._notifyObserversOfChanges=function(_15){
var _16=this._getResultSets();
for(var i in _15){
var _18=_15[i];
var _19=_18.item;
var _1a=_19.getObservers();
for(var j in _1a){
var _1c=_1a[j];
_1c.observedObjectHasChanged(_19,_18);
}
for(var k in _16){
var _1e=_16[k];
var _1f=_1e.getObservers();
for(var m in _1f){
_1c=_1f[m];
_1c.observedObjectHasChanged(_1e,_18);
}
}
}
};
dojo.data.old.provider.Base.prototype._getResultSets=function(){
dojo.unimplemented("dojo.data.old.provider.Base");
return [];
};
