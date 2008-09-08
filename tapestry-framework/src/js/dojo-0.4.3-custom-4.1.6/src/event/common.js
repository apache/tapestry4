dojo.provide("dojo.event.common");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.extras");
dojo.require("dojo.lang.func");
dojo.event=new function(){
this._canTimeout=dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);
function interpolateArgs(_1,_2){
var dl=dojo.lang;
var ao={srcObj:dj_global,srcFunc:null,adviceObj:dj_global,adviceFunc:null,aroundObj:null,aroundFunc:null,adviceType:(_1.length>2)?_1[0]:"after",precedence:"last",once:false,delay:null,rate:0,adviceMsg:false,maxCalls:-1};
switch(_1.length){
case 0:
return;
case 1:
return;
case 2:
ao.srcFunc=_1[0];
ao.adviceFunc=_1[1];
break;
case 3:
if((dl.isObject(_1[0]))&&(dl.isString(_1[1]))&&(dl.isString(_1[2]))){
ao.adviceType="after";
ao.srcObj=_1[0];
ao.srcFunc=_1[1];
ao.adviceFunc=_1[2];
}else{
if((dl.isString(_1[1]))&&(dl.isString(_1[2]))){
ao.srcFunc=_1[1];
ao.adviceFunc=_1[2];
}else{
if((dl.isObject(_1[0]))&&(dl.isString(_1[1]))&&(dl.isFunction(_1[2]))){
ao.adviceType="after";
ao.srcObj=_1[0];
ao.srcFunc=_1[1];
var _5=dl.nameAnonFunc(_1[2],ao.adviceObj,_2);
ao.adviceFunc=_5;
}else{
if((dl.isFunction(_1[0]))&&(dl.isObject(_1[1]))&&(dl.isString(_1[2]))){
ao.adviceType="after";
ao.srcObj=dj_global;
var _5=dl.nameAnonFunc(_1[0],ao.srcObj,_2);
ao.srcFunc=_5;
ao.adviceObj=_1[1];
ao.adviceFunc=_1[2];
}
}
}
}
break;
case 4:
if((dl.isObject(_1[0]))&&(dl.isObject(_1[2]))){
ao.adviceType="after";
ao.srcObj=_1[0];
ao.srcFunc=_1[1];
ao.adviceObj=_1[2];
ao.adviceFunc=_1[3];
}else{
if((dl.isString(_1[0]))&&(dl.isString(_1[1]))&&(dl.isObject(_1[2]))){
ao.adviceType=_1[0];
ao.srcObj=dj_global;
ao.srcFunc=_1[1];
ao.adviceObj=_1[2];
ao.adviceFunc=_1[3];
}else{
if((dl.isString(_1[0]))&&(dl.isFunction(_1[1]))&&(dl.isObject(_1[2]))){
ao.adviceType=_1[0];
ao.srcObj=dj_global;
var _5=dl.nameAnonFunc(_1[1],dj_global,_2);
ao.srcFunc=_5;
ao.adviceObj=_1[2];
ao.adviceFunc=_1[3];
}else{
if((dl.isString(_1[0]))&&(dl.isObject(_1[1]))&&(dl.isString(_1[2]))&&(dl.isFunction(_1[3]))){
ao.srcObj=_1[1];
ao.srcFunc=_1[2];
var _5=dl.nameAnonFunc(_1[3],dj_global,_2);
ao.adviceObj=dj_global;
ao.adviceFunc=_5;
}else{
if(dl.isObject(_1[1])){
ao.srcObj=_1[1];
ao.srcFunc=_1[2];
ao.adviceObj=dj_global;
ao.adviceFunc=_1[3];
}else{
if(dl.isObject(_1[2])){
ao.srcObj=dj_global;
ao.srcFunc=_1[1];
ao.adviceObj=_1[2];
ao.adviceFunc=_1[3];
}else{
ao.srcObj=ao.adviceObj=ao.aroundObj=dj_global;
ao.srcFunc=_1[1];
ao.adviceFunc=_1[2];
ao.aroundFunc=_1[3];
}
}
}
}
}
}
break;
case 6:
ao.srcObj=_1[1];
ao.srcFunc=_1[2];
ao.adviceObj=_1[3];
ao.adviceFunc=_1[4];
ao.aroundFunc=_1[5];
ao.aroundObj=dj_global;
break;
default:
ao.srcObj=_1[1];
ao.srcFunc=_1[2];
ao.adviceObj=_1[3];
ao.adviceFunc=_1[4];
ao.aroundObj=_1[5];
ao.aroundFunc=_1[6];
ao.once=_1[7];
ao.delay=_1[8];
ao.rate=_1[9];
ao.adviceMsg=_1[10];
ao.maxCalls=(!isNaN(parseInt(_1[11])))?_1[11]:-1;
break;
}
if(dl.isFunction(ao.aroundFunc)){
var _5=dl.nameAnonFunc(ao.aroundFunc,ao.aroundObj,_2);
ao.aroundFunc=_5;
}
if(dl.isFunction(ao.srcFunc)){
ao.srcFunc=dl.getNameInObj(ao.srcObj,ao.srcFunc);
}
if(dl.isFunction(ao.adviceFunc)){
ao.adviceFunc=dl.getNameInObj(ao.adviceObj,ao.adviceFunc);
}
if((ao.aroundObj)&&(dl.isFunction(ao.aroundFunc))){
ao.aroundFunc=dl.getNameInObj(ao.aroundObj,ao.aroundFunc);
}
if(!ao.srcObj){
dojo.raise("bad srcObj for srcFunc: "+ao.srcFunc);
}
if(!ao.adviceObj){
dojo.raise("bad adviceObj for adviceFunc: "+ao.adviceFunc);
}
if(!ao.adviceFunc){
dojo.debug("bad adviceFunc for srcFunc: "+ao.srcFunc);
dojo.debugShallow(ao);
}
return ao;
}
this.connect=function(){
if(arguments.length==1){
var ao=arguments[0];
}else{
var ao=interpolateArgs(arguments,true);
}
if(dojo.lang.isString(ao.srcFunc)&&(ao.srcFunc.toLowerCase()=="onkey")){
if(dojo.render.html.ie){
ao.srcFunc="onkeydown";
this.connect(ao);
}
ao.srcFunc="onkeypress";
}
if(dojo.lang.isArray(ao.srcObj)&&ao.srcObj!=""){
var _7={};
for(var x in ao){
_7[x]=ao[x];
}
var _9=[];
dojo.lang.forEach(ao.srcObj,function(_a){
if((dojo.render.html.capable)&&(dojo.lang.isString(_a))){
_a=dojo.byId(_a);
}
_7.srcObj=_a;
_9.push(dojo.event.connect.call(dojo.event,_7));
});
return _9;
}
var _b=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);
if(ao.adviceFunc){
var _c=dojo.event.MethodJoinPoint.getForMethod(ao.adviceObj,ao.adviceFunc);
}
_b.kwAddAdvice(ao);
return _b;
};
this.log=function(a1,a2){
var _f;
if((arguments.length==1)&&(typeof a1=="object")){
_f=a1;
}else{
_f={srcObj:a1,srcFunc:a2};
}
_f.adviceFunc=function(){
var _10=[];
for(var x=0;x<arguments.length;x++){
_10.push(arguments[x]);
}
dojo.debug("("+_f.srcObj+")."+_f.srcFunc,":",_10.join(", "));
};
this.kwConnect(_f);
};
this.connectBefore=function(){
var _12=["before"];
for(var i=0;i<arguments.length;i++){
_12.push(arguments[i]);
}
return this.connect.apply(this,_12);
};
this.connectAround=function(){
var _14=["around"];
for(var i=0;i<arguments.length;i++){
_14.push(arguments[i]);
}
return this.connect.apply(this,_14);
};
this.connectOnce=function(){
var ao=interpolateArgs(arguments,true);
ao.once=true;
return this.connect(ao);
};
this.connectRunOnce=function(){
var ao=interpolateArgs(arguments,true);
ao.maxCalls=1;
return this.connect(ao);
};
this._kwConnectImpl=function(_18,_19){
var fn=(_19)?"disconnect":"connect";
if(typeof _18["srcFunc"]=="function"){
_18.srcObj=_18["srcObj"]||dj_global;
var _1b=dojo.lang.nameAnonFunc(_18.srcFunc,_18.srcObj,true);
_18.srcFunc=_1b;
}
if(typeof _18["adviceFunc"]=="function"){
_18.adviceObj=_18["adviceObj"]||dj_global;
var _1b=dojo.lang.nameAnonFunc(_18.adviceFunc,_18.adviceObj,true);
_18.adviceFunc=_1b;
}
_18.srcObj=_18["srcObj"]||dj_global;
_18.adviceObj=_18["adviceObj"]||_18["targetObj"]||dj_global;
_18.adviceFunc=_18["adviceFunc"]||_18["targetFunc"];
return dojo.event[fn](_18);
};
this.kwConnect=function(_1c){
return this._kwConnectImpl(_1c,false);
};
this.disconnect=function(){
if(arguments.length==1){
var ao=arguments[0];
}else{
var ao=interpolateArgs(arguments,true);
}
if(!ao.adviceFunc){
return;
}
if(dojo.lang.isString(ao.srcFunc)&&(ao.srcFunc.toLowerCase()=="onkey")){
if(dojo.render.html.ie){
ao.srcFunc="onkeydown";
this.disconnect(ao);
}
ao.srcFunc="onkeypress";
}
if(!ao.srcObj[ao.srcFunc]){
return null;
}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc,true);
mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);
return mjp;
};
this.kwDisconnect=function(_1f){
return this._kwConnectImpl(_1f,true);
};
};
dojo.event.MethodInvocation=function(_20,obj,_22){
this.jp_=_20;
this.object=obj;
this.args=[];
for(var x=0;x<_22.length;x++){
this.args[x]=_22[x];
}
this.around_index=-1;
};
dojo.event.MethodInvocation.prototype.proceed=function(){
this.around_index++;
if(this.around_index>=this.jp_.around.length){
return this.jp_.object[this.jp_.methodname].apply(this.jp_.object,this.args);
}else{
var ti=this.jp_.around[this.around_index];
var _25=ti[0]||dj_global;
var _26=ti[1];
return _25[_26].call(_25,this);
}
};
dojo.event.MethodJoinPoint=function(obj,_28){
this.object=obj||dj_global;
this.methodname=_28;
this.methodfunc=this.object[_28];
this.squelch=false;
};
dojo.event.MethodJoinPoint.getForMethod=function(obj,_2a){
if(!obj){
obj=dj_global;
}
var ofn=obj[_2a];
if(!ofn){
ofn=obj[_2a]=function(){
};
if(!obj[_2a]){
dojo.raise("Cannot set do-nothing method on that object "+_2a);
}
}else{
if((typeof ofn!="function")&&(!dojo.lang.isFunction(ofn))&&(!dojo.lang.isAlien(ofn))){
return null;
}
}
var _2c=_2a+"$joinpoint";
var _2d=_2a+"$joinpoint$method";
var _2e=obj[_2c];
if(!_2e){
var _2f=false;
if(dojo.event["browser"]){
if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){
_2f=true;
dojo.event.browser.addClobberNodeAttrs(obj,[_2c,_2d,_2a]);
}
}
var _30=ofn.length;
obj[_2d]=ofn;
_2e=obj[_2c]=new dojo.event.MethodJoinPoint(obj,_2d);
if(!_2f){
obj[_2a]=function(){
return _2e.run.apply(_2e,arguments);
};
}else{
obj[_2a]=function(){
var _31=[];
if(!arguments.length){
var evt=null;
try{
if(obj.ownerDocument){
evt=obj.ownerDocument.parentWindow.event;
}else{
if(obj.documentElement){
evt=obj.documentElement.ownerDocument.parentWindow.event;
}else{
if(obj.event){
evt=obj.event;
}else{
evt=window.event;
}
}
}
}
catch(e){
evt=window.event;
}
if(evt){
_31.push(dojo.event.browser.fixEvent(evt,this));
}
}else{
for(var x=0;x<arguments.length;x++){
if((x==0)&&(dojo.event.browser.isEvent(arguments[x]))){
_31.push(dojo.event.browser.fixEvent(arguments[x],this));
}else{
_31.push(arguments[x]);
}
}
}
return _2e.run.apply(_2e,_31);
};
}
obj[_2a].__preJoinArity=_30;
}
return _2e;
};
dojo.lang.extend(dojo.event.MethodJoinPoint,{squelch:false,unintercept:function(){
this.object[this.methodname]=this.methodfunc;
this.before=[];
this.after=[];
this.around=[];
},disconnect:dojo.lang.forward("unintercept"),run:function(){
var obj=this.object||dj_global;
var _35=arguments;
var _36=[];
for(var x=0;x<_35.length;x++){
_36[x]=_35[x];
}
var _38=function(_39){
if(!_39){
dojo.debug("Null argument to unrollAdvice()");
return;
}
var _3a=_39[0]||dj_global;
var _3b=_39[1];
if(!_3a[_3b]){
dojo.raise("function \""+_3b+"\" does not exist on \""+_3a+"\"");
}
var _3c=_39[2]||dj_global;
var _3d=_39[3];
var msg=_39[6];
var _3f=_39[7];
if(_3f>-1){
if(_3f==0){
return;
}
_39[7]--;
}
var _40;
var to={args:[],jp_:this,object:obj,proceed:function(){
return _3a[_3b].apply(_3a,to.args);
}};
to.args=_36;
var _42=parseInt(_39[4]);
var _43=((!isNaN(_42))&&(_39[4]!==null)&&(typeof _39[4]!="undefined"));
if(_39[5]){
var _44=parseInt(_39[5]);
var cur=new Date();
var _46=false;
if((_39["last"])&&((cur-_39.last)<=_44)){
if(dojo.event._canTimeout){
if(_39["delayTimer"]){
clearTimeout(_39.delayTimer);
}
var tod=parseInt(_44*2);
var _48=dojo.lang.shallowCopy(_39);
_39.delayTimer=setTimeout(function(){
_48[5]=0;
_38(_48);
},tod);
}
return;
}else{
_39.last=cur;
}
}
if(_3d){
_3c[_3d].call(_3c,to);
}else{
if((_43)&&((dojo.render.html)||(dojo.render.svg))){
dj_global["setTimeout"](function(){
if(msg){
_3a[_3b].call(_3a,to);
}else{
_3a[_3b].apply(_3a,_35);
}
},_42);
}else{
if(msg){
_3a[_3b].call(_3a,to);
}else{
_3a[_3b].apply(_3a,_35);
}
}
}
};
var _49=function(){
if(this.squelch){
try{
return _38.apply(this,arguments);
}
catch(e){
dojo.debug(e);
}
}else{
return _38.apply(this,arguments);
}
};
if((this["before"])&&(this.before.length>0)){
dojo.lang.forEach(this.before.concat(new Array()),_49);
}
var _4a;
try{
if((this["around"])&&(this.around.length>0)){
var mi=new dojo.event.MethodInvocation(this,obj,_35);
_4a=mi.proceed();
}else{
if(this.methodfunc){
_4a=this.object[this.methodname].apply(this.object,_35);
}
}
}
catch(e){
if(!this.squelch){
dojo.debug(e,"when calling",this.methodname,"on",this.object,"with arguments",_35);
dojo.raise(e);
}
}
if((this["after"])&&(this.after.length>0)){
dojo.lang.forEach(this.after.concat(new Array()),_49);
}
return (this.methodfunc)?_4a:null;
},getArr:function(_4c){
var _4d="after";
if((typeof _4c=="string")&&(_4c.indexOf("before")!=-1)){
_4d="before";
}else{
if(_4c=="around"){
_4d="around";
}
}
if(!this[_4d]){
this[_4d]=[];
}
return this[_4d];
},kwAddAdvice:function(_4e){
this.addAdvice(_4e["adviceObj"],_4e["adviceFunc"],_4e["aroundObj"],_4e["aroundFunc"],_4e["adviceType"],_4e["precedence"],_4e["once"],_4e["delay"],_4e["rate"],_4e["adviceMsg"],_4e["maxCalls"]);
},addAdvice:function(_4f,_50,_51,_52,_53,_54,_55,_56,_57,_58,_59){
var arr=this.getArr(_53);
if(!arr){
dojo.raise("bad this: "+this);
}
var ao=[_4f,_50,_51,_52,_56,_57,_58,_59];
if(_55){
if(this.hasAdvice(_4f,_50,_53,arr)>=0){
return;
}
}
if(_54=="first"){
arr.unshift(ao);
}else{
arr.push(ao);
}
},hasAdvice:function(_5c,_5d,_5e,arr){
if(!arr){
arr=this.getArr(_5e);
}
var ind=-1;
for(var x=0;x<arr.length;x++){
var aao=(typeof _5d=="object")?(new String(_5d)).toString():_5d;
var a1o=(typeof arr[x][1]=="object")?(new String(arr[x][1])).toString():arr[x][1];
if((arr[x][0]==_5c)&&(a1o==aao)){
ind=x;
}
}
return ind;
},removeAdvice:function(_64,_65,_66,_67){
var arr=this.getArr(_66);
var ind=this.hasAdvice(_64,_65,_66,arr);
if(ind==-1){
return false;
}
while(ind!=-1){
arr.splice(ind,1);
if(_67){
break;
}
ind=this.hasAdvice(_64,_65,_66,arr);
}
return true;
}});
