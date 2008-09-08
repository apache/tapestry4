dojo.provide("dojo.math.curves");
dojo.require("dojo.math");
dojo.math.curves={Line:function(_1,_2){
this.start=_1;
this.end=_2;
this.dimensions=_1.length;
for(var i=0;i<_1.length;i++){
_1[i]=Number(_1[i]);
}
for(var i=0;i<_2.length;i++){
_2[i]=Number(_2[i]);
}
this.getValue=function(n){
var _5=new Array(this.dimensions);
for(var i=0;i<this.dimensions;i++){
_5[i]=((this.end[i]-this.start[i])*n)+this.start[i];
}
return _5;
};
return this;
},Bezier:function(_7){
this.getValue=function(_8){
if(_8>=1){
return this.p[this.p.length-1];
}
if(_8<=0){
return this.p[0];
}
var _9=new Array(this.p[0].length);
for(var k=0;j<this.p[0].length;k++){
_9[k]=0;
}
for(var j=0;j<this.p[0].length;j++){
var C=0;
var D=0;
for(var i=0;i<this.p.length;i++){
C+=this.p[i][j]*this.p[this.p.length-1][0]*dojo.math.bernstein(_8,this.p.length,i);
}
for(var l=0;l<this.p.length;l++){
D+=this.p[this.p.length-1][0]*dojo.math.bernstein(_8,this.p.length,l);
}
_9[j]=C/D;
}
return _9;
};
this.p=_7;
return this;
},CatmullRom:function(_10,c){
this.getValue=function(_12){
var _13=_12*(this.p.length-1);
var _14=Math.floor(_13);
var _15=_13-_14;
var i0=_14-1;
if(i0<0){
i0=0;
}
var i=_14;
var i1=_14+1;
if(i1>=this.p.length){
i1=this.p.length-1;
}
var i2=_14+2;
if(i2>=this.p.length){
i2=this.p.length-1;
}
var u=_15;
var u2=_15*_15;
var u3=_15*_15*_15;
var _1d=new Array(this.p[0].length);
for(var k=0;k<this.p[0].length;k++){
var x1=(-this.c*this.p[i0][k])+((2-this.c)*this.p[i][k])+((this.c-2)*this.p[i1][k])+(this.c*this.p[i2][k]);
var x2=(2*this.c*this.p[i0][k])+((this.c-3)*this.p[i][k])+((3-2*this.c)*this.p[i1][k])+(-this.c*this.p[i2][k]);
var x3=(-this.c*this.p[i0][k])+(this.c*this.p[i1][k]);
var x4=this.p[i][k];
_1d[k]=x1*u3+x2*u2+x3*u+x4;
}
return _1d;
};
if(!c){
this.c=0.7;
}else{
this.c=c;
}
this.p=_10;
return this;
},Arc:function(_23,end,ccw){
var _26=dojo.math.points.midpoint(_23,end);
var _27=dojo.math.points.translate(dojo.math.points.invert(_26),_23);
var rad=Math.sqrt(Math.pow(_27[0],2)+Math.pow(_27[1],2));
var _29=dojo.math.radToDeg(Math.atan(_27[1]/_27[0]));
if(_27[0]<0){
_29-=90;
}else{
_29+=90;
}
dojo.math.curves.CenteredArc.call(this,_26,rad,_29,_29+(ccw?-180:180));
},CenteredArc:function(_2a,_2b,_2c,end){
this.center=_2a;
this.radius=_2b;
this.start=_2c||0;
this.end=end;
this.getValue=function(n){
var _2f=new Array(2);
var _30=dojo.math.degToRad(this.start+((this.end-this.start)*n));
_2f[0]=this.center[0]+this.radius*Math.sin(_30);
_2f[1]=this.center[1]-this.radius*Math.cos(_30);
return _2f;
};
return this;
},Circle:function(_31,_32){
dojo.math.curves.CenteredArc.call(this,_31,_32,0,360);
return this;
},Path:function(){
var _33=[];
var _34=[];
var _35=[];
var _36=0;
this.add=function(_37,_38){
if(_38<0){
dojo.raise("dojo.math.curves.Path.add: weight cannot be less than 0");
}
_33.push(_37);
_34.push(_38);
_36+=_38;
computeRanges();
};
this.remove=function(_39){
for(var i=0;i<_33.length;i++){
if(_33[i]==_39){
_33.splice(i,1);
_36-=_34.splice(i,1)[0];
break;
}
}
computeRanges();
};
this.removeAll=function(){
_33=[];
_34=[];
_36=0;
};
this.getValue=function(n){
var _3c=false,_3d=0;
for(var i=0;i<_35.length;i++){
var r=_35[i];
if(n>=r[0]&&n<r[1]){
var _40=(n-r[0])/r[2];
_3d=_33[i].getValue(_40);
_3c=true;
break;
}
}
if(!_3c){
_3d=_33[_33.length-1].getValue(1);
}
for(var j=0;j<i;j++){
_3d=dojo.math.points.translate(_3d,_33[j].getValue(1));
}
return _3d;
};
function computeRanges(){
var _42=0;
for(var i=0;i<_34.length;i++){
var end=_42+_34[i]/_36;
var len=end-_42;
_35[i]=[_42,end,len];
_42=end;
}
}
return this;
}};
