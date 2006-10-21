

dojo.provide("dojo.collections.SortedList");
dojo.require("dojo.collections.Collections");

dojo.collections.SortedList=function( dictionary){



var _this=this;
var items={};
var q=[];
var sorter=function(a,b){
if (a.key > b.key) return 1;
if (a.key < b.key) return -1;
return 0;
};
var build=function(){
q=[];
var e=_this.getIterator();
while (!e.atEnd()){
q.push(e.get());
}
q.sort(sorter);
};
var testObject={};

this.count=q.length;
this.add=function( k,v){
//	summary
//	add the passed value to the dictionary at location k
if (!items[k]) {
items[k]=new dojo.collections.DictionaryEntry(k,v);
this.count=q.push(items[k]);
q.sort(sorter);
}
};
this.clear=function(){
//	summary
//	clear the internal collections
items={};
q=[];
this.count=q.length;
};
this.clone=function(){
//	summary
//	create a clone of this sorted list
return new dojo.collections.SortedList(this);	//	dojo.collections.SortedList
};
this.contains=this.containsKey=function( k){
//	summary
//	Check to see if the list has a location k
if(testObject[k]){
return false;			//	bool
}
return (items[k]!=null);	//	bool
};
this.containsValue=function( o){
//	summary
//	Check to see if this list contains the passed object
var e=this.getIterator();
while (!e.atEnd()){
var item=e.get();
if(item.value==o){
return true;	//	bool
}
}
return false;	//	bool
};
this.copyTo=function( arr,  i){
//	summary
//	copy the contents of the list into array arr at index i
var e=this.getIterator();
var idx=i;
while(!e.atEnd()){
arr.splice(idx,0,e.get());
idx++;
}
};
this.entry=function( k){
//	summary
//	return the object at location k
return items[k];	//	dojo.collections.DictionaryEntry
};
this.forEach=function( fn,  scope){
//	summary
//	functional iterator, following the mozilla spec.
var s=scope||dj_global;
if(Array.forEach){
Array.forEach(q, fn, s);
}else{
for(var i=0; i<q.length; i++){
fn.call(s, q[i], i, q);
}
}
};
this.getByIndex=function( i){
//	summary
//	return the item at index i
return q[i].valueOf();	//	object
};
this.getIterator=function(){
//	summary
//	get an iterator for this object
return new dojo.collections.DictionaryIterator(items);	//	dojo.collections.DictionaryIterator
};
this.getKey=function( i){
//	summary
//	return the key of the item at index i
return q[i].key;
};
this.getKeyList=function(){
//	summary
//	return an array of the keys set in this list
var arr=[];
var e=this.getIterator();
while (!e.atEnd()){
arr.push(e.get().key);
}
return arr;	//	array
};
this.getValueList=function(){
//	summary
//	return an array of values in this list
var arr=[];
var e=this.getIterator();
while (!e.atEnd()){
arr.push(e.get().value);
}
return arr;	//	array
};
this.indexOfKey=function( k){
//	summary
//	return the index of the passed key.
for (var i=0; i<q.length; i++){
if (q[i].key==k){
return i;	//	int
}
}
return -1;	//	int
};
this.indexOfValue=function( o){
//	summary
//	return the first index of object o
for (var i=0; i<q.length; i++){
if (q[i].value==o){
return i;	//	int
}
}
return -1;	//	int
};
this.item=function( k){
// 	summary
//	return the value of the object at location k.
if(k in items && !testObject[k]){
return items[k].valueOf();	//	object
}
return undefined;	//	object
};
this.remove=function(k){
// 	summary
//	remove the item at location k and rebuild the internal collections.
delete items[k];
build();
this.count=q.length;
};
this.removeAt=function( i){
//	summary
//	remove the item at index i, and rebuild the internal collections.
delete items[q[i].key];
build();
this.count=q.length;
};
this.replace=function( k,  v){
//	summary
//	Replace an existing item if it's there, and add a new one if not.
if (!items[k]){
//	we're adding a new object, return false
this.add(k,v);
return false; // bool
}else{
//	we're replacing an object, return true
items[k]=new dojo.collections.DictionaryEntry(k,v);
q.sort(sorter);
return true; // bool
}
};
this.setByIndex=function( i,  o){
//	summary
//	set an item by index
items[q[i].key].value=o;
build();
this.count=q.length;
};
if (dictionary){
var e=dictionary.getIterator();
while (!e.atEnd()){
var item=e.get();
q[q.length]=items[item.key]=new dojo.collections.DictionaryEntry(item.key,item.value);
}
q.sort(sorter);
}
}
