dojo.provide("dojo.data.old.Observable");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.assert");
dojo.data.old.Observable=function(){
};
dojo.data.old.Observable.prototype.addObserver=function(_1){
dojo.lang.assertType(_1,Object);
dojo.lang.assertType(_1.observedObjectHasChanged,Function);
if(!this._arrayOfObservers){
this._arrayOfObservers=[];
}
if(!dojo.lang.inArray(this._arrayOfObservers,_1)){
this._arrayOfObservers.push(_1);
}
};
dojo.data.old.Observable.prototype.removeObserver=function(_2){
if(!this._arrayOfObservers){
return;
}
var _3=dojo.lang.indexOf(this._arrayOfObservers,_2);
if(_3!=-1){
this._arrayOfObservers.splice(_3,1);
}
};
dojo.data.old.Observable.prototype.getObservers=function(){
return this._arrayOfObservers;
};
