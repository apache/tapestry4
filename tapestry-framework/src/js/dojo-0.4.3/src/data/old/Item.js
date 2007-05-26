dojo.provide("dojo.data.old.Item");
dojo.require("dojo.data.old.Observable");
dojo.require("dojo.data.old.Value");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.assert");
dojo.data.old.Item=function(_1){
dojo.lang.assertType(_1,dojo.data.old.provider.Base,{optional:true});
dojo.data.old.Observable.call(this);
this._dataProvider=_1;
this._dictionaryOfAttributeValues={};
};
dojo.inherits(dojo.data.old.Item,dojo.data.old.Observable);
dojo.data.old.Item.compare=function(_2,_3){
dojo.lang.assertType(_2,dojo.data.old.Item);
if(!dojo.lang.isOfType(_3,dojo.data.old.Item)){
return -1;
}
var _4=_2.getName();
var _5=_3.getName();
if(_4==_5){
var _6=_2.getAttributes();
var _7=_3.getAttributes();
if(_6.length!=_7.length){
if(_6.length>_7.length){
return 1;
}else{
return -1;
}
}
for(var i in _6){
var _9=_6[i];
var _a=_2.getValues(_9);
var _b=_3.getValues(_9);
dojo.lang.assert(_a&&(_a.length>0));
if(!_b){
return 1;
}
if(_a.length!=_b.length){
if(_a.length>_b.length){
return 1;
}else{
return -1;
}
}
for(var j in _a){
var _d=_a[j];
if(!_3.hasAttributeValue(_d)){
return 1;
}
}
return 0;
}
}else{
if(_4>_5){
return 1;
}else{
return -1;
}
}
};
dojo.data.old.Item.prototype.toString=function(){
var _e=[];
var _f=this.getAttributes();
for(var i in _f){
var _11=_f[i];
var _12=this.getValues(_11);
var _13;
if(_12.length==1){
_13=_12[0];
}else{
_13="[";
_13+=_12.join(", ");
_13+="]";
}
_e.push("  "+_11+": "+_13);
}
var _14="{ ";
_14+=_e.join(",\n");
_14+=" }";
return _14;
};
dojo.data.old.Item.prototype.compare=function(_15){
return dojo.data.old.Item.compare(this,_15);
};
dojo.data.old.Item.prototype.isEqual=function(_16){
return (this.compare(_16)==0);
};
dojo.data.old.Item.prototype.getName=function(){
return this.get("name");
};
dojo.data.old.Item.prototype.get=function(_17){
var _18=this._dictionaryOfAttributeValues[_17];
if(dojo.lang.isUndefined(_18)){
return null;
}
if(_18 instanceof dojo.data.old.Value){
return _18.getValue();
}
if(dojo.lang.isArray(_18)){
var _19=_18[0];
return _19.getValue();
}
return _18;
};
dojo.data.old.Item.prototype.getValue=function(_1a){
var _1b=this._dictionaryOfAttributeValues[_1a];
if(dojo.lang.isUndefined(_1b)){
return null;
}
if(_1b instanceof dojo.data.old.Value){
return _1b;
}
if(dojo.lang.isArray(_1b)){
var _1c=_1b[0];
return _1c;
}
var _1d=_1b;
_1c=new dojo.data.old.Value(_1d);
this._dictionaryOfAttributeValues[_1a]=_1c;
return _1c;
};
dojo.data.old.Item.prototype.getValues=function(_1e){
var _1f=this._dictionaryOfAttributeValues[_1e];
if(dojo.lang.isUndefined(_1f)){
return null;
}
if(_1f instanceof dojo.data.old.Value){
var _20=[_1f];
this._dictionaryOfAttributeValues[_1e]=_20;
return _20;
}
if(dojo.lang.isArray(_1f)){
return _1f;
}
var _21=_1f;
var _22=new dojo.data.old.Value(_21);
_20=[_22];
this._dictionaryOfAttributeValues[_1e]=_20;
return _20;
};
dojo.data.old.Item.prototype.load=function(_23,_24){
this._dataProvider.registerAttribute(_23);
var _25=this._dictionaryOfAttributeValues[_23];
if(dojo.lang.isUndefined(_25)){
this._dictionaryOfAttributeValues[_23]=_24;
return;
}
if(!(_24 instanceof dojo.data.old.Value)){
_24=new dojo.data.old.Value(_24);
}
if(_25 instanceof dojo.data.old.Value){
var _26=[_25,_24];
this._dictionaryOfAttributeValues[_23]=_26;
return;
}
if(dojo.lang.isArray(_25)){
_25.push(_24);
return;
}
var _27=_25;
var _28=new dojo.data.old.Value(_27);
_26=[_28,_24];
this._dictionaryOfAttributeValues[_23]=_26;
};
dojo.data.old.Item.prototype.set=function(_29,_2a){
this._dataProvider.registerAttribute(_29);
this._dictionaryOfAttributeValues[_29]=_2a;
this._dataProvider.noteChange(this,_29,_2a);
};
dojo.data.old.Item.prototype.setValue=function(_2b,_2c){
this.set(_2b,_2c);
};
dojo.data.old.Item.prototype.addValue=function(_2d,_2e){
this.load(_2d,_2e);
this._dataProvider.noteChange(this,_2d,_2e);
};
dojo.data.old.Item.prototype.setValues=function(_2f,_30){
dojo.lang.assertType(_30,Array);
this._dataProvider.registerAttribute(_2f);
var _31=[];
this._dictionaryOfAttributeValues[_2f]=_31;
for(var i in _30){
var _33=_30[i];
if(!(_33 instanceof dojo.data.old.Value)){
_33=new dojo.data.old.Value(_33);
}
_31.push(_33);
this._dataProvider.noteChange(this,_2f,_33);
}
};
dojo.data.old.Item.prototype.getAttributes=function(){
var _34=[];
for(var key in this._dictionaryOfAttributeValues){
_34.push(this._dataProvider.getAttribute(key));
}
return _34;
};
dojo.data.old.Item.prototype.hasAttribute=function(_36){
return (_36 in this._dictionaryOfAttributeValues);
};
dojo.data.old.Item.prototype.hasAttributeValue=function(_37,_38){
var _39=this.getValues(_37);
for(var i in _39){
var _3b=_39[i];
if(_3b.isEqual(_38)){
return true;
}
}
return false;
};
