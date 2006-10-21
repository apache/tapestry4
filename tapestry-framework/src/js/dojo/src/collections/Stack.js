

dojo.provide("dojo.collections.Stack");
dojo.require("dojo.collections.Collections");

dojo.collections.Stack=function(arr){


var q=[];
if (arr) q=q.concat(arr);
this.count=q.length;
this.clear=function(){
//	summary
//	Clear the internal array and reset the count
q=[];
this.count=q.length;
};
this.clone=function(){
//	summary
//	Create and return a clone of this Stack
return new dojo.collections.Stack(q);
};
this.contains=function(o){
//	summary
//	check to see if the stack contains object o
for (var i=0; i<q.length; i++){
if (q[i] == o){
return true;	//	bool
}
}
return false;	//	bool
};
this.copyTo=function( arr,  i){
//	summary
//	copy the stack into array arr at index i
arr.splice(i,0,q);
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
this.getIterator=function(){
//	summary
//	get an iterator for this collection
return new dojo.collections.Iterator(q);	//	dojo.collections.Iterator
};
this.peek=function(){
//	summary
//	Return the next item without altering the stack itself.
return q[(q.length-1)];	//	object
};
this.pop=function(){
//	summary
//	pop and return the next item on the stack
var r=q.pop();
this.count=q.length;
return r;	//	object
};
this.push=function( o){
//	summary
//	Push object o onto the stack
this.count=q.push(o);
};
this.toArray=function(){
//	summary
//	create and return an array based on the internal collection
return [].concat(q);	//	array
};
}
