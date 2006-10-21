

dojo.provide("dojo.string.Builder");
dojo.require("dojo.string");
dojo.require("dojo.lang.common");





dojo.string.Builder = function(str){

this.arrConcat = (dojo.render.html.capable && dojo.render.html["ie"]);

var a = [];
var b = "";
var length = this.length = b.length;

if(this.arrConcat){
if(b.length > 0){
a.push(b);
}
b = "";
}

this.toString = this.valueOf = function(){
//	summary
//	Concatenate internal buffer and return as a string
return (this.arrConcat) ? a.join("") : b;	//	string
};

this.append = function(){
//	summary
//	Append all arguments to the end of the internal buffer
for(var x=0; x<arguments.length; x++){
var s = arguments[x];
if(dojo.lang.isArrayLike(s)){
this.append.apply(this, s);
} else {
if(this.arrConcat){
a.push(s);
}else{
b+=s;
}
length += s.length;
this.length = length;
}
}
return this;	//	dojo.string.Builder
};

this.clear = function(){
//	summary
//	Clear the internal buffer.
a = [];
b = "";
length = this.length = 0;
return this;	//	dojo.string.Builder
};

this.remove = function(f, l){
//	summary
//	Remove a section of string from the internal buffer.
var s = "";
if(this.arrConcat){
b = a.join("");
}
a=[];
if(f>0){
s = b.substring(0, (f-1));
}
b = s + b.substring(f + l);
length = this.length = b.length;
if(this.arrConcat){
a.push(b);
b="";
}
return this;	//	dojo.string.Builder
};

this.replace = function(o, n){
//	summary
//	replace phrase *o* with phrase *n*.
if(this.arrConcat){
b = a.join("");
}
a = [];
b = b.replace(o,n);
length = this.length = b.length;
if(this.arrConcat){
a.push(b);
b="";
}
return this;	//	dojo.string.Builder
};

this.insert = function(idx, s){
//	summary
//	Insert string s at index idx.
if(this.arrConcat){
b = a.join("");
}
a=[];
if(idx == 0){
b = s + b;
}else{
var t = b.split("");
t.splice(idx,0,s);
b = t.join("")
}
length = this.length = b.length;
if(this.arrConcat){
a.push(b);
b="";
}
return this;	//	dojo.string.Builder
};

this.append.apply(this, arguments);
};
