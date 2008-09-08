dojo.provide("dojo.collections.Store");
dojo.require("dojo.lang.common");
dojo.collections.Store=function(_1){
var _2=[];
var _3={};
this.keyField="Id";
this.get=function(){
return _2;
};
this.getByKey=function(_4){
return _3[_4];
};
this.getByIndex=function(_5){
return _2[_5];
};
this.getIndexOf=function(_6){
for(var i=0;i<_2.length;i++){
if(_2[i].key==_6){
return i;
}
}
return -1;
};
this.getData=function(){
var _8=[];
for(var i=0;i<_2.length;i++){
_8.push(_2[i].src);
}
return _8;
};
this.getDataByKey=function(_a){
if(_3[_a]!=null){
return _3[_a].src;
}
return null;
};
this.getIndexOfData=function(_b){
for(var i=0;i<_2.length;i++){
if(_2[i].src==_b){
return i;
}
}
return -1;
};
this.getDataByIndex=function(_d){
if(_2[_d]){
return _2[_d].src;
}
return null;
};
this.update=function(_e,_f,val,_11){
var _12=_f.split("."),i=0,o=_e,_15;
if(_12.length>1){
_15=_12.pop();
do{
if(_12[i].indexOf("()")>-1){
var _16=_12[i++].split("()")[0];
if(!o[_16]){
dojo.raise("dojo.collections.Store.getField(obj, '"+_15+"'): '"+_16+"' is not a property of the passed object.");
}else{
o=o[_16]();
}
}else{
o=o[_12[i++]];
}
}while(i<_12.length&&o!=null);
}else{
_15=_12[0];
}
_e[_15]=val;
if(!_11){
this.onUpdateField(_e,_f,val);
}
};
this.forEach=function(fn){
if(Array.forEach){
Array.forEach(_2,fn,this);
}else{
for(var i=0;i<_2.length;i++){
fn.call(this,_2[i]);
}
}
};
this.forEachData=function(fn){
if(Array.forEach){
Array.forEach(this.getData(),fn,this);
}else{
var a=this.getData();
for(var i=0;i<a.length;i++){
fn.call(this,a[i]);
}
}
};
this.setData=function(arr,_1d){
_2=[];
for(var i=0;i<arr.length;i++){
var o={key:arr[i][this.keyField],src:arr[i]};
_2.push(o);
_3[o.key]=o;
}
if(!_1d){
this.onSetData();
}
};
this.clearData=function(_20){
_2=[];
_3={};
if(!_20){
this.onClearData();
}
};
this.addData=function(obj,key,_23){
var k=key||obj[this.keyField];
if(_3[k]!=null){
var o=_3[k];
o.src=obj;
}else{
var o={key:k,src:obj};
_2.push(o);
_3[o.key]=o;
}
if(!_23){
this.onAddData(o);
}
};
this.addDataRange=function(arr,_27){
var _28=[];
for(var i=0;i<arr.length;i++){
var k=arr[i][this.keyField];
if(_3[k]!=null){
var o=_3[k];
o.src=arr[i];
}else{
var o={key:k,src:arr[i]};
_2.push(o);
_3[k]=o;
}
_28.push(o);
}
if(!_27){
this.onAddDataRange(_28);
}
};
this.addDataByIndex=function(obj,idx,key,_2f){
var k=key||obj[this.keyField];
if(_3[k]!=null){
var i=this.getIndexOf(k);
var o=_2.splice(i,1);
o.src=obj;
}else{
var o={key:k,src:obj};
_3[k]=o;
}
_2.splice(idx,0,o);
if(!_2f){
this.onAddData(o);
}
};
this.addDataRangeByIndex=function(arr,idx,_35){
var _36=[];
for(var i=0;i<arr.length;i++){
var k=arr[i][this.keyField];
if(_3[k]!=null){
var j=this.getIndexOf(k);
var o=_2.splice(j,1);
o.src=arr[i];
}else{
var o={key:k,src:arr[i]};
_3[k]=o;
}
_36.push(o);
}
_2.splice(idx,0,_36);
if(!_35){
this.onAddDataRange(_36);
}
};
this.removeData=function(obj,_3c){
var idx=-1;
var o=null;
for(var i=0;i<_2.length;i++){
if(_2[i].src==obj){
idx=i;
o=_2[i];
break;
}
}
if(!_3c){
this.onRemoveData(o);
}
if(idx>-1){
_2.splice(idx,1);
delete _3[o.key];
}
};
this.removeDataRange=function(idx,_41,_42){
var ret=_2.splice(idx,_41);
for(var i=0;i<ret.length;i++){
delete _3[ret[i].key];
}
if(!_42){
this.onRemoveDataRange(ret);
}
return ret;
};
this.removeDataByKey=function(key,_46){
this.removeData(this.getDataByKey(key),_46);
};
this.removeDataByIndex=function(idx,_48){
this.removeData(this.getDataByIndex(idx),_48);
};
if(_1&&_1.length&&_1[0]){
this.setData(_1,true);
}
};
dojo.extend(dojo.collections.Store,{getField:function(obj,_4a){
var _4b=_4a.split("."),i=0,o=obj;
do{
if(_4b[i].indexOf("()")>-1){
var _4e=_4b[i++].split("()")[0];
if(!o[_4e]){
dojo.raise("dojo.collections.Store.getField(obj, '"+_4a+"'): '"+_4e+"' is not a property of the passed object.");
}else{
o=o[_4e]();
}
}else{
o=o[_4b[i++]];
}
}while(i<_4b.length&&o!=null);
if(i<_4b.length){
dojo.raise("dojo.collections.Store.getField(obj, '"+_4a+"'): '"+_4a+"' is not a property of the passed object.");
}
return o;
},getFromHtml:function(_4f,_50,_51){
var _52=_50.rows;
var _53=function(row){
var obj={};
for(var i=0;i<_4f.length;i++){
var o=obj;
var _58=row.cells[i].innerHTML;
var p=_4f[i].getField();
if(p.indexOf(".")>-1){
p=p.split(".");
while(p.length>1){
var pr=p.shift();
o[pr]={};
o=o[pr];
}
p=p[0];
}
var _5b=_4f[i].getType();
if(_5b==String){
o[p]=_58;
}else{
if(_58){
o[p]=new _5b(_58);
}else{
o[p]=new _5b();
}
}
}
return obj;
};
var arr=[];
for(var i=0;i<_52.length;i++){
var o=_53(_52[i]);
if(_51){
_51(o,_52[i]);
}
arr.push(o);
}
return arr;
},onSetData:function(){
},onClearData:function(){
},onAddData:function(obj){
},onAddDataRange:function(arr){
},onRemoveData:function(obj){
},onRemoveDataRange:function(arr){
},onUpdateField:function(obj,_64,val){
}});
