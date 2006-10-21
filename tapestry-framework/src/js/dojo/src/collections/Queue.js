

dojo.provide("dojo.collections.Queue");
dojo.require("dojo.collections.Collections");

dojo.collections.Queue=function(arr){


var q=[];
if (arr){
q=q.concat(arr);
}
this.count=q.length;
this.clear=function(){
//	summary
//	clears the internal collection
q=[];
this.count=q.length;
};
this.clone=function(){
//	summary
//	creates a new Queue based on this one
return new dojo.collections.Queue(q);	//	dojo.collections.Queue
};
this.contains=function( o){
//	summary
//	Check to see if the passed object is an element in this queue
for(var i=0; i<q.length; i++){
if (q[i]==o){
return true;	//	bool
}
}
return false;	//	bool
};
this.copyTo=function( arr,  i){
//	summary
//	Copy the contents of this queue into the passed array at index i.
arr.splice(i,0,q);
};
this.dequeue=function(){
//	summary
//	shift the first element off the queue and return it
var r=q.shift();
this.count=q.length;
return r;	//	object
};
this.enqueue=function( o){
//	summary
//	put the passed object at the end of the queue
this.count=q.push(o);
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
//	get an Iterator based on this queue.
return new dojo.collections.Iterator(q);	//	dojo.collections.Iterator
};
this.peek=function(){
//	summary
//	get the next element in the queue without altering the queue.
return q[0];
};
this.toArray=function(){
//	summary
//	return an array based on the internal array of the queue.
return [].concat(q);
};
};
