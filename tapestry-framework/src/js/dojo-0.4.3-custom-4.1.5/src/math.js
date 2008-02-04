dojo.provide("dojo.math");
dojo.math.degToRad=function(x){
return (x*Math.PI)/180;
};
dojo.math.radToDeg=function(x){
return (x*180)/Math.PI;
};
dojo.math.factorial=function(n){
if(n<1){
return 0;
}
var _4=1;
for(var i=1;i<=n;i++){
_4*=i;
}
return _4;
};
dojo.math.permutations=function(n,k){
if(n==0||k==0){
return 1;
}
return (dojo.math.factorial(n)/dojo.math.factorial(n-k));
};
dojo.math.combinations=function(n,r){
if(n==0||r==0){
return 1;
}
return (dojo.math.factorial(n)/(dojo.math.factorial(n-r)*dojo.math.factorial(r)));
};
dojo.math.bernstein=function(t,n,i){
return (dojo.math.combinations(n,i)*Math.pow(t,i)*Math.pow(1-t,n-i));
};
dojo.math.gaussianRandom=function(){
var k=2;
do{
var i=2*Math.random()-1;
var j=2*Math.random()-1;
k=i*i+j*j;
}while(k>=1);
k=Math.sqrt((-2*Math.log(k))/k);
return i*k;
};
dojo.math.mean=function(){
var _10=dojo.lang.isArray(arguments[0])?arguments[0]:arguments;
var _11=0;
for(var i=0;i<_10.length;i++){
_11+=_10[i];
}
return _11/_10.length;
};
dojo.math.round=function(_13,_14){
if(!_14){
var _15=1;
}else{
var _15=Math.pow(10,_14);
}
return Math.round(_13*_15)/_15;
};
dojo.math.sd=dojo.math.standardDeviation=function(){
var _16=dojo.lang.isArray(arguments[0])?arguments[0]:arguments;
return Math.sqrt(dojo.math.variance(_16));
};
dojo.math.variance=function(){
var _17=dojo.lang.isArray(arguments[0])?arguments[0]:arguments;
var _18=0,_19=0;
for(var i=0;i<_17.length;i++){
_18+=_17[i];
_19+=Math.pow(_17[i],2);
}
return (_19/_17.length)-Math.pow(_18/_17.length,2);
};
dojo.math.range=function(a,b,_1d){
if(arguments.length<2){
b=a;
a=0;
}
if(arguments.length<3){
_1d=1;
}
var _1e=[];
if(_1d>0){
for(var i=a;i<b;i+=_1d){
_1e.push(i);
}
}else{
if(_1d<0){
for(var i=a;i>b;i+=_1d){
_1e.push(i);
}
}else{
throw new Error("dojo.math.range: step must be non-zero");
}
}
return _1e;
};
