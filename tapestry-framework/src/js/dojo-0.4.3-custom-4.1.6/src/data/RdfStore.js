dojo.provide("dojo.data.RdfStore");
dojo.provide("dojo.data.RhizomeStore");
dojo.require("dojo.lang.declare");
dojo.require("dojo.data.core.RemoteStore");
dojo.require("dojo.experimental");
dojo.data.RdfDatatypeSerializer=function(_1,_2,_3){
this.type=_1;
this._converter=_2;
this.uri=_3;
this.serialize=function(_4){
return this._converter.call(_4,_4);
};
};
dojo.declare("dojo.data.RdfStore",dojo.data.core.RemoteStore,{_datatypeMap:{literal:function(_5){
var _6=_5.value;
if(_5["xml:lang"]){
_6.lang=_5["xml:lang"];
}
return _6;
},uri:function(_7){
return {id:_7.value};
},bnode:function(_8){
return {id:"_:"+_8.value};
},"http://www.w3.org/2001/XMLSchema#int":function(_9){
return parseInt(_9.value);
},"http://www.w3.org/2001/XMLSchema#integer":function(_a){
return parseInt(_a.value);
},"http://www.w3.org/2001/XMLSchema#long":function(_b){
return parseInt(_b.value);
},"http://www.w3.org/2001/XMLSchema#float":function(_c){
return parseFloat(_c.value);
},"http://www.w3.org/2001/XMLSchema#double":function(_d){
return parseFloat(_d.value);
},"http://www.w3.org/2001/XMLSchema#boolean":function(_e){
return !_e||_e=="false"||_e=="0"?false:true;
}},_datatypeSerializers:[new dojo.data.RdfDatatypeSerializer(Number,Number.toString,"http://www.w3.org/2001/XMLSchema#float"),new dojo.data.RdfDatatypeSerializer(Boolean,Boolean.toString,"http://www.w3.org/2001/XMLSchema#boolean")],_findDatatypeSerializer:function(_f){
var _10=this._datatypeSerializers.length;
for(var i=0;i<_10;i++){
var _12=this._datatypeSerializers[i];
if(_f instanceof _12.type){
return _12;
}
}
},_toRDFValue:function(_13){
var _14={};
if(_13.id){
if(_13.id.slice(0,2)=="_:"){
_14.type="bnode";
_14.value=_13.id.substring(2);
}else{
_14.type="uri";
_14.value=_13.id;
}
}else{
if(typeof _13=="string"||_13 instanceof String){
_14.type="literal";
_14.value=_13;
if(_13.lang){
_14["xml:lang"]=_13.lang;
}
}else{
if(typeof _13=="number"){
_13=new Number(_13);
}else{
if(typeof _13=="boolean"){
_13=new Boolean(_13);
}
}
var _15=this._findDatatypeSerializer(_13);
if(_15){
_14={"type":"typed-literal","datatype":_15.uri,"value":_13.toString()};
}else{
_14={"type":"literal","value":_13.toString()};
}
}
}
return _14;
},_setupSaveRequest:function(_16,_17){
var _18={"head":{"vars":["s","p","o"]},"results":{"bindings":[]}};
var _19=[];
for(var key in this._deleted){
_19.push(key);
}
_18.results.deleted=_19;
for(key in this._changed){
var _1b=this._toRDFValue(this.getIdentity(key));
var _1c=this._changed[key];
for(var _1d in _1c){
var _1e={type:"uri",value:_1d};
var _1f=_1c[_1d];
if(!_1f.length){
continue;
}
var _20=[];
for(var i=0;i<_1f.length;i++){
var _22=this._toRDFValue(_1f[i]);
_18.results.bindings.push({s:_1b,p:_1e,o:_22});
}
}
}
var _23=dojo.json.jsonRegistry;
dojo.json.jsonRegistry=this._jsonRegistry;
var _24=dojo.json.serialize(_18);
dojo.json.jsonRegistry=_23;
_17.postContent=_24;
},_resultToQueryMetadata:function(_25){
return _25.head;
},_resultToQueryData:function(_26){
var _27={};
var _28=_26.results.bindings;
for(var i=0;i<_28.length;i++){
var _2a=_28[i];
var _2b=_2a.s.value;
if(_2a.s.type=="bnode"){
_2b="_:"+_2b;
}
var _2c=data[_2b];
if(!_2c){
_2c={};
data[_2a.s]=_2c;
}
var _2d=_2c[_2a.p.value];
if(!_2d){
_2c[_2a.p.value]=[_2a.o];
}else{
_2d.push(_2a.o);
}
}
return _27;
}});
dojo.declare("dojo.data.RhizomeStore",dojo.data.RdfStore,{initializer:function(_2e){
this._serverQueryUrl=_2e.baseUrl+"search?view=json&searchType=RxPath&search=";
this._serverSaveUrl=_2e.baseUrl+"save-metadata";
},_resultToQueryMetadata:function(_2f){
return _2f;
},_resultToQueryData:function(_30){
return _30;
},_setupSaveRequest:function(_31,_32){
_32.url=this._serverSaveUrl;
_32.method="post";
_32.mimetype="text/plain";
var _33=[];
for(var key in this._deleted){
_33.push(key);
}
var _35={};
for(key in this._changed){
if(!this._added[key]){
_33.push(key);
}
var _36=this._changed[key];
var _37={};
for(var _38 in _36){
var _39=_36[_38];
if(!_39.length){
continue;
}
var _3a=[];
for(var i=0;i<_39.length;i++){
var _3c=this._toRDFValue(_39[i]);
_3a.push(_3c);
}
_37[_38]=_3a;
}
_35[key]=_37;
}
var _3d=dojo.json.jsonRegistry;
dojo.json.jsonRegistry=this._jsonRegistry;
var _3e=dojo.json.serialize(_35);
dojo.json.jsonRegistry=_3d;
_32.content={rdfFormat:"json",resource:_33,metadata:_3e};
}});
