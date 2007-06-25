dojo.provide("dojo.lang.declare");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.extras");
dojo.lang.declare=function(_1,_2,_3,_4){
if((dojo.lang.isFunction(_4))||((!_4)&&(!dojo.lang.isFunction(_3)))){
var _5=_4;
_4=_3;
_3=_5;
}
var _6=[];
if(dojo.lang.isArray(_2)){
_6=_2;
_2=_6.shift();
}
if(!_3){
_3=dojo.evalObjPath(_1,false);
if((_3)&&(!dojo.lang.isFunction(_3))){
_3=null;
}
}
var _7=dojo.lang.declare._makeConstructor();
var _8=(_2?_2.prototype:null);
if(_8){
_8.prototyping=true;
_7.prototype=new _2();
_8.prototyping=false;
}
_7.superclass=_8;
_7.mixins=_6;
for(var i=0,l=_6.length;i<l;i++){
dojo.lang.extend(_7,_6[i].prototype);
}
_7.prototype.initializer=null;
_7.prototype.declaredClass=_1;
if(dojo.lang.isArray(_4)){
dojo.lang.extend.apply(dojo.lang,[_7].concat(_4));
}else{
dojo.lang.extend(_7,(_4)||{});
}
dojo.lang.extend(_7,dojo.lang.declare._common);
_7.prototype.constructor=_7;
_7.prototype.initializer=(_7.prototype.initializer)||(_3)||(function(){
});
var _b=dojo.parseObjPath(_1,null,true);
_b.obj[_b.prop]=_7;
return _7;
};
dojo.lang.declare._makeConstructor=function(){
return function(){
var _c=this._getPropContext();
var s=_c.constructor.superclass;
if((s)&&(s.constructor)){
if(s.constructor==arguments.callee){
this._inherited("constructor",arguments);
}else{
this._contextMethod(s,"constructor",arguments);
}
}
var ms=(_c.constructor.mixins)||([]);
for(var i=0,m;(m=ms[i]);i++){
(((m.prototype)&&(m.prototype.initializer))||(m)).apply(this,arguments);
}
if((!this.prototyping)&&(_c.initializer)){
_c.initializer.apply(this,arguments);
}
};
};
dojo.lang.declare._common={_getPropContext:function(){
return (this.___proto||this);
},_contextMethod:function(_11,_12,_13){
var _14,_15=this.___proto;
this.___proto=_11;
try{
_14=_11[_12].apply(this,(_13||[]));
}
catch(e){
throw e;
}
finally{
this.___proto=_15;
}
return _14;
},_inherited:function(_16,_17){
var p=this._getPropContext();
do{
if((!p.constructor)||(!p.constructor.superclass)){
return;
}
p=p.constructor.superclass;
}while(!(_16 in p));
return (dojo.lang.isFunction(p[_16])?this._contextMethod(p,_16,_17):p[_16]);
},inherited:function(_19,_1a){
dojo.deprecated("'inherited' method is dangerous, do not up-call! 'inherited' is slated for removal in 0.5; name your super class (or use superclass property) instead.","0.5");
this._inherited(_19,_1a);
}};
dojo.declare=dojo.lang.declare;
