dojo.provide("dojo.data.core.RemoteStore");
dojo.require("dojo.data.core.Read");
dojo.require("dojo.data.core.Write");
dojo.require("dojo.data.core.Result");
dojo.require("dojo.experimental");
dojo.require("dojo.Deferred");
dojo.require("dojo.lang.declare");
dojo.require("dojo.json");
dojo.require("dojo.io.*");
dojo.experimental("dojo.data.core.RemoteStore");
dojo.lang.declare("dojo.data.core.RemoteStore",[dojo.data.core.Read,dojo.data.core.Write],{_datatypeMap:{},_jsonRegistry:dojo.json.jsonRegistry,initializer:function(_1){
if(!_1){
_1={};
}
this._serverQueryUrl=_1.queryUrl||"";
this._serverSaveUrl=_1.saveUrl||"";
this._deleted={};
this._changed={};
this._added={};
this._results={};
this._data={};
this._numItems=0;
},_setupQueryRequest:function(_2,_3){
_2.query=_2.query||"";
_3.url=this._serverQueryUrl+encodeURIComponent(_2.query);
_3.method="get";
_3.mimetype="text/json";
},_resultToQueryMetadata:function(_4){
return _4;
},_resultToQueryData:function(_5){
return _5.data;
},_remoteToLocalValues:function(_6){
for(var _7 in _6){
var _8=_6[_7];
for(var i=0;i<_8.length;i++){
var _a=_8[i];
var _b=_a.datatype||_a.type;
if(_b){
var _c=_a.value;
if(this._datatypeMap[_b]){
_c=this._datatypeMap[_b](_a);
}
_8[i]=_c;
}
}
}
return _6;
},_queryToQueryKey:function(_d){
if(typeof _d=="string"){
return _d;
}else{
return dojo.json.serialize(_d);
}
},_assertIsItem:function(_e){
if(!this.isItem(_e)){
throw new Error("dojo.data.RemoteStore: a function was passed an item argument that was not an item");
}
},get:function(_f,_10,_11){
var _12=this.getValues(_f,_10);
if(_12.length==0){
return _11;
}
return _12[0];
},getValues:function(_13,_14){
var _15=this.getIdentity(_13);
this._assertIsItem(_15);
var _16=this._changed[_15];
if(_16){
var _17=_16[_14];
if(_17!==undefined){
return _17;
}else{
return [];
}
}
return this._data[_15][0][_14];
},getAttributes:function(_18){
var _19=this.getIdentity(_18);
if(!_19){
return undefined;
}
var _1a=[];
var _1b=this._data[_19][0];
for(var att in _1b){
_1a.push(att);
}
return _1a;
},hasAttribute:function(_1d,_1e){
var _1f=this.getValues(_1d,_1e);
return _1f.length?true:false;
},containsValue:function(_20,_21,_22){
var _23=this.getValues(_20,_21);
for(var i=0;i<_23.length;i++){
if(_23[i]==_22){
return true;
}
}
return false;
},isItem:function(_25){
if(!_25){
return false;
}
var _26=_25;
if(this._deleted[_26]){
return false;
}
if(this._data[_26]){
return true;
}
if(this._added[_26]){
return true;
}
return false;
},find:function(_27){
var _28=null;
if(_27 instanceof dojo.data.core.Result){
_28=_27;
_28.store=this;
}else{
_28=new dojo.data.core.Result(_27,this);
}
var _29=_28.query;
var _2a=this;
var _2b=function(_2c,_2d,evt){
var _2f=_28.scope||dj_global;
if(_2c=="load"){
_28.resultMetadata=_2a._resultToQueryMetadata(_2d);
var _30=_2a._resultToQueryData(_2d);
if(_28.onbegin){
_28.onbegin.call(_2f,_28);
}
var _31=0;
var _32=[];
var _33=0;
for(var key in _30){
if(_28._aborted){
break;
}
if(!_2a._deleted[key]){
var _35=_30[key];
var _36=_2a._remoteToLocalValues(_35);
var _37=_2a._data[key];
var _38=1;
if(_37){
_38=++_37[1];
}else{
_33++;
}
_2a._data[key]=[_36,_38];
_32.push(key);
_31++;
if(_28.onnext){
_28.onnext.call(_2f,key,_28);
}
}
}
_2a._results[_2a._queryToQueryKey(_29)]=_32;
_2a._numItems+=_33;
_28.length=_31;
if(_28.saveResult){
_28.items=_32;
}
if(!_28._aborted&&_28.oncompleted){
_28.oncompleted.call(_2f,_28);
}
}else{
if(_2c=="error"||_2c=="timeout"){
dojo.debug("find error: "+dojo.json.serialize(_2d));
if(_28.onerror){
_28.onerror.call(_2f,_2d);
}
}
}
};
var _39=_27.bindArgs||{};
_39.sync=_28.sync;
_39.handle=_2b;
this._setupQueryRequest(_28,_39);
var _3a=dojo.io.bind(_39);
_28._abortFunc=_3a.abort;
return _28;
},getIdentity:function(_3b){
if(!this.isItem(_3b)){
return null;
}
return (_3b.id?_3b.id:_3b);
},newItem:function(_3c,_3d){
var _3e=_3d["identity"];
if(this._deleted[_3e]){
delete this._deleted[_3e];
}else{
this._added[_3e]=1;
}
if(_3c){
for(var _3f in _3c){
var _40=_3c[_3f];
if(dojo.lang.isArray(_40)){
this.setValues(_3e,_3f,_40);
}else{
this.set(_3e,_3f,_40);
}
}
}
return {id:_3e};
},deleteItem:function(_41){
var _42=this.getIdentity(_41);
if(!_42){
return false;
}
if(this._added[_42]){
delete this._added[_42];
}else{
this._deleted[_42]=1;
}
if(this._changed[_42]){
delete this._changed[_42];
}
return true;
},setValues:function(_43,_44,_45){
var _46=this.getIdentity(_43);
if(!_46){
return undefined;
}
var _47=this._changed[_46];
if(!_47){
_47={};
this._changed[_46]=_47;
}
_47[_44]=_45;
return true;
},set:function(_48,_49,_4a){
return this.setValues(_48,_49,[_4a]);
},unsetAttribute:function(_4b,_4c){
return this.setValues(_4b,_4c,[]);
},_initChanges:function(){
this._deleted={};
this._changed={};
this._added={};
},_setupSaveRequest:function(_4d,_4e){
_4e.url=this._serverSaveUrl;
_4e.method="post";
_4e.mimetype="text/plain";
var _4f=[];
for(var key in this._deleted){
_4f.push(key);
}
var _51={"changed":this._changed,"deleted":_4f};
var _52=dojo.json.jsonRegistry;
dojo.json.jsonRegistry=this._jsonRegistry;
var _53=dojo.json.serialize(_51);
dojo.json.jsonRegistry=_52;
_4e.postContent=_53;
},save:function(_54){
_54=_54||{};
var _55=new dojo.Deferred();
var _56=this;
var _57=function(_58,_59,evt){
if(_58=="load"){
if(_55.fired==1){
return;
}
var key=null;
for(key in _56._added){
if(!_56._data[key]){
_56._data[key]=[{},1];
}
}
for(key in _56._changed){
var _5c=_56._data[key];
var _5d=_56._changed[key];
if(_5c){
_5c[0]=_5d;
}else{
_56._data[key]=[_5d,1];
}
}
for(key in _56._deleted){
if(_56._data[key]){
delete _56._data[key];
}
}
_56._initChanges();
_55.callback(true);
}else{
if(_58=="error"||_58=="timeout"){
_55.errback(_59);
}
}
};
var _5e={sync:_54["sync"],handle:_57};
this._setupSaveRequest(_54,_5e);
var _5f=dojo.io.bind(_5e);
_55.canceller=function(_60){
_5f.abort();
};
return _55;
},revert:function(){
this._initChanges();
return true;
},isDirty:function(_61){
if(_61){
var _62=_61.id||_61;
return this._deleted[_62]||this._changed[_62];
}else{
var key=null;
for(key in this._changed){
return true;
}
for(key in this._deleted){
return true;
}
for(key in this._added){
return true;
}
return false;
}
},createReference:function(_64){
return {id:_64};
},getSize:function(){
return this._numItems;
},forgetResults:function(_65){
var _66=this._queryToQueryKey(_65);
var _67=this._results[_66];
if(!_67){
return false;
}
var _68=0;
for(var i=0;i<_67.length;i++){
var key=_67[i];
var _6b=this._data[key];
if(_6b[1]<=1){
delete this._data[key];
_68++;
}else{
_6b[1]=--_6b[1];
}
}
delete this._results[_66];
this._numItems-=_68;
return true;
}});
