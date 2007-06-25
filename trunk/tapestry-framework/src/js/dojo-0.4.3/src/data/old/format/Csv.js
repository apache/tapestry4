dojo.provide("dojo.data.old.format.Csv");
dojo.require("dojo.lang.assert");
dojo.data.old.format.Csv=new function(){
this.getArrayStructureFromCsvFileContents=function(_1){
dojo.lang.assertType(_1,String);
var _2=new RegExp("\r\n|\n|\r");
var _3=new RegExp("^\\s+","g");
var _4=new RegExp("\\s+$","g");
var _5=new RegExp("\"\"","g");
var _6=[];
var _7=_1.split(_2);
for(var i in _7){
var _9=_7[i];
if(_9.length>0){
var _a=_9.split(",");
var j=0;
while(j<_a.length){
var _c=_a[j];
var _d=_c.replace(_3,"");
var _e=_d.replace(_4,"");
var _f=_e.charAt(0);
var _10=_e.charAt(_e.length-1);
var _11=_e.charAt(_e.length-2);
var _12=_e.charAt(_e.length-3);
if((_f=="\"")&&((_10!="\"")||((_10=="\"")&&(_11=="\"")&&(_12!="\"")))){
if(j+1===_a.length){
return null;
}
var _13=_a[j+1];
_a[j]=_d+","+_13;
_a.splice(j+1,1);
}else{
if((_f=="\"")&&(_10=="\"")){
_e=_e.slice(1,(_e.length-1));
_e=_e.replace(_5,"\"");
}
_a[j]=_e;
j+=1;
}
}
_6.push(_a);
}
}
return _6;
};
this.loadDataProviderFromFileContents=function(_14,_15){
dojo.lang.assertType(_14,dojo.data.old.provider.Base);
dojo.lang.assertType(_15,String);
var _16=this.getArrayStructureFromCsvFileContents(_15);
if(_16){
var _17=_16[0];
for(var i=1;i<_16.length;++i){
var row=_16[i];
var _1a=_14.getNewItemToLoad();
for(var j in row){
var _1c=row[j];
var key=_17[j];
_1a.load(key,_1c);
}
}
}
};
this.getCsvStringFromResultSet=function(_1e){
dojo.unimplemented("dojo.data.old.format.Csv.getCsvStringFromResultSet");
var _1f=null;
return _1f;
};
}();
