

dojo.provide("dojo.collections.Collections");

dojo.collections.DictionaryEntry=function(k, v){


this.key=k;
this.value=v;
this.valueOf=function(){
return this.value; 	//	object
};
this.toString=function(){
return String(this.value);	//	string
};
}


dojo.collections.Iterator=function(arr){


var a=arr;
var position=0;
this.element=a[position]||null;
this.atEnd=function(){
//	summary
//	Test to see if the internal cursor has reached the end of the internal collection.
return (position>=a.length);	//	bool
};
this.get=function(){
//	summary
//	Test to see if the internal cursor has reached the end of the internal collection.
if(this.atEnd()){
return null;		//	object
}
this.element=a[position++];
return this.element;	//	object
};
this.map=function(fn, scope){
//	summary
//	Functional iteration with optional scope.
var s=scope||dj_global;
if(Array.map){
return Array.map(a,fn,s);	//	array
}else{
var arr=[];
for(var i=0; i<a.length; i++){
arr.push(fn.call(s,a[i]));
}
return arr;		//	array
}
};
this.reset=function(){
//	summary
//	reset the internal cursor.
position=0;
this.element=a[position];
};
}


dojo.collections.DictionaryIterator=function(obj){


var a=[];	//	Create an indexing array
var testObject={};
for(var p in obj){
if(!testObject[p]){
a.push(obj[p]);	//	fill it up
}
}
var position=0;
this.element=a[position]||null;
this.atEnd=function(){
//	summary
//	Test to see if the internal cursor has reached the end of the internal collection.
return (position>=a.length);	//	bool
};
this.get=function(){
//	summary
//	Test to see if the internal cursor has reached the end of the internal collection.
if(this.atEnd()){
return null;		//	object
}
this.element=a[position++];
return this.element;	//	object
};
this.map=function(fn, scope){
//	summary
//	Functional iteration with optional scope.
var s=scope||dj_global;
if(Array.map){
return Array.map(a,fn,s);	//	array
}else{
var arr=[];
for(var i=0; i<a.length; i++){
arr.push(fn.call(s,a[i]));
}
return arr;		//	array
}
};
this.reset=function() {
//	summary
//	reset the internal cursor.
position=0;
this.element=a[position];
};
};
