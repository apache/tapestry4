dojo.provide("dojo.data.old.provider.FlatFile");
dojo.require("dojo.data.old.provider.Base");
dojo.require("dojo.data.old.Item");
dojo.require("dojo.data.old.Attribute");
dojo.require("dojo.data.old.ResultSet");
dojo.require("dojo.data.old.format.Json");
dojo.require("dojo.data.old.format.Csv");
dojo.require("dojo.lang.assert");
dojo.data.old.provider.FlatFile=function(_1){
dojo.lang.assertType(_1,"pureobject",{optional:true});
dojo.data.old.provider.Base.call(this);
this._arrayOfItems=[];
this._resultSet=null;
this._dictionaryOfAttributes={};
if(_1){
var _2=_1["jsonObjects"];
var _3=_1["jsonString"];
var _4=_1["url"];
if(_2){
dojo.data.old.format.Json.loadDataProviderFromArrayOfJsonData(this,_2);
}
if(_3){
dojo.data.old.format.Json.loadDataProviderFromFileContents(this,_3);
}
if(_4){
var _5=_4.split(".");
var _6=_5[(_5.length-1)];
var _7=null;
if(_6=="json"){
_7=dojo.data.old.format.Json;
}
if(_6=="csv"){
_7=dojo.data.old.format.Csv;
}
if(_7){
var _8=dojo.hostenv.getText(_4);
_7.loadDataProviderFromFileContents(this,_8);
}else{
dojo.lang.assert(false,"new dojo.data.old.provider.FlatFile({url: }) was passed a file without a .csv or .json suffix");
}
}
}
};
dojo.inherits(dojo.data.old.provider.FlatFile,dojo.data.old.provider.Base);
dojo.data.old.provider.FlatFile.prototype.getProviderCapabilities=function(_9){
dojo.lang.assertType(_9,String,{optional:true});
if(!this._ourCapabilities){
this._ourCapabilities={transactions:false,undo:false,login:false,versioning:false,anonymousRead:true,anonymousWrite:false,permissions:false,queries:false,strongTyping:false,datatypes:[String,Date,Number]};
}
if(_9){
return this._ourCapabilities[_9];
}else{
return this._ourCapabilities;
}
};
dojo.data.old.provider.FlatFile.prototype.registerAttribute=function(_a){
var _b=this.getAttribute(_a);
if(!_b){
var _c=new dojo.data.old.Attribute(this,_a);
this._dictionaryOfAttributes[_a]=_c;
_b=_c;
}
return _b;
};
dojo.data.old.provider.FlatFile.prototype.getAttribute=function(_d){
var _e=(this._dictionaryOfAttributes[_d]||null);
return _e;
};
dojo.data.old.provider.FlatFile.prototype.getAttributes=function(){
var _f=[];
for(var key in this._dictionaryOfAttributes){
var _11=this._dictionaryOfAttributes[key];
_f.push(_11);
}
return _f;
};
dojo.data.old.provider.FlatFile.prototype.fetchArray=function(_12){
return this._arrayOfItems;
};
dojo.data.old.provider.FlatFile.prototype.fetchResultSet=function(_13){
if(!this._resultSet){
this._resultSet=new dojo.data.old.ResultSet(this,this.fetchArray(_13));
}
return this._resultSet;
};
dojo.data.old.provider.FlatFile.prototype._newItem=function(){
var _14=new dojo.data.old.Item(this);
this._arrayOfItems.push(_14);
return _14;
};
dojo.data.old.provider.FlatFile.prototype._newAttribute=function(_15){
dojo.lang.assertType(_15,String);
dojo.lang.assert(this.getAttribute(_15)===null);
var _16=new dojo.data.old.Attribute(this,_15);
this._dictionaryOfAttributes[_15]=_16;
return _16;
};
dojo.data.old.provider.Base.prototype._getResultSets=function(){
return [this._resultSet];
};
