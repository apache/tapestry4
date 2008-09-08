dojo.provide("dojo.data.CsvStore");
dojo.require("dojo.data.core.RemoteStore");
dojo.require("dojo.lang.assert");
dojo.declare("dojo.data.CsvStore",dojo.data.core.RemoteStore,{_setupQueryRequest:function(_1,_2){
var _3=this._serverQueryUrl?this._serverQueryUrl:"";
var _4=_1.query?_1.query:"";
_2.url=_3+_4;
_2.method="get";
},_resultToQueryData:function(_5){
var _6=_5;
var _7=this._getArrayOfArraysFromCsvFileContents(_6);
var _8=this._getArrayOfObjectsFromArrayOfArrays(_7);
var _9=this._getRemoteStoreDataFromArrayOfObjects(_8);
return _9;
},_setupSaveRequest:function(_a,_b){
},_getArrayOfArraysFromCsvFileContents:function(_c){
dojo.lang.assertType(_c,String);
var _d=new RegExp("\r\n|\n|\r");
var _e=new RegExp("^\\s+","g");
var _f=new RegExp("\\s+$","g");
var _10=new RegExp("\"\"","g");
var _11=[];
var _12=_c.split(_d);
for(var i in _12){
var _14=_12[i];
if(_14.length>0){
var _15=_14.split(",");
var j=0;
while(j<_15.length){
var _17=_15[j];
var _18=_17.replace(_e,"");
var _19=_18.replace(_f,"");
var _1a=_19.charAt(0);
var _1b=_19.charAt(_19.length-1);
var _1c=_19.charAt(_19.length-2);
var _1d=_19.charAt(_19.length-3);
if((_1a=="\"")&&((_1b!="\"")||((_1b=="\"")&&(_1c=="\"")&&(_1d!="\"")))){
if(j+1===_15.length){
return null;
}
var _1e=_15[j+1];
_15[j]=_18+","+_1e;
_15.splice(j+1,1);
}else{
if((_1a=="\"")&&(_1b=="\"")){
_19=_19.slice(1,(_19.length-1));
_19=_19.replace(_10,"\"");
}
_15[j]=_19;
j+=1;
}
}
_11.push(_15);
}
}
return _11;
},_getArrayOfObjectsFromArrayOfArrays:function(_1f){
dojo.lang.assertType(_1f,Array);
var _20=[];
if(_1f.length>1){
var _21=_1f[0];
for(var i=1;i<_1f.length;++i){
var row=_1f[i];
var _24={};
for(var j in row){
var _26=row[j];
var key=_21[j];
_24[key]=_26;
}
_20.push(_24);
}
}
return _20;
},_getRemoteStoreDataFromArrayOfObjects:function(_28){
dojo.lang.assertType(_28,Array);
var _29={};
for(var i=0;i<_28.length;++i){
var _2b=_28[i];
for(var key in _2b){
var _2d=_2b[key];
_2b[key]=[_2d];
}
_29[i]=_2b;
}
return _29;
},newItem:function(_2e,_2f){
dojo.unimplemented("dojo.data.CsvStore.newItem");
},deleteItem:function(_30){
dojo.unimplemented("dojo.data.CsvStore.deleteItem");
},setValues:function(_31,_32,_33){
dojo.unimplemented("dojo.data.CsvStore.setValues");
},set:function(_34,_35,_36){
dojo.unimplemented("dojo.data.CsvStore.set");
},unsetAttribute:function(_37,_38){
dojo.unimplemented("dojo.data.CsvStore.unsetAttribute");
},save:function(_39){
dojo.unimplemented("dojo.data.CsvStore.save");
},revert:function(){
dojo.unimplemented("dojo.data.CsvStore.revert");
},isDirty:function(_3a){
dojo.unimplemented("dojo.data.CsvStore.isDirty");
}});
