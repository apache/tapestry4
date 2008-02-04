dojo.provide("dojo.widget.html.stabile");
dojo.widget.html.stabile={_sqQuotables:new RegExp("([\\\\'])","g"),_depth:0,_recur:false,depthLimit:2};
dojo.widget.html.stabile.getState=function(id){
dojo.widget.html.stabile.setup();
return dojo.widget.html.stabile.widgetState[id];
};
dojo.widget.html.stabile.setState=function(id,_3,_4){
dojo.widget.html.stabile.setup();
dojo.widget.html.stabile.widgetState[id]=_3;
if(_4){
dojo.widget.html.stabile.commit(dojo.widget.html.stabile.widgetState);
}
};
dojo.widget.html.stabile.setup=function(){
if(!dojo.widget.html.stabile.widgetState){
var _5=dojo.widget.html.stabile._getStorage().value;
dojo.widget.html.stabile.widgetState=_5?dj_eval("("+_5+")"):{};
}
};
dojo.widget.html.stabile.commit=function(_6){
dojo.widget.html.stabile._getStorage().value=dojo.widget.html.stabile.description(_6);
};
dojo.widget.html.stabile.description=function(v,_8){
var _9=dojo.widget.html.stabile._depth;
var _a=function(){
return this.description(this,true);
};
try{
if(v===void (0)){
return "undefined";
}
if(v===null){
return "null";
}
if(typeof (v)=="boolean"||typeof (v)=="number"||v instanceof Boolean||v instanceof Number){
return v.toString();
}
if(typeof (v)=="string"||v instanceof String){
var v1=v.replace(dojo.widget.html.stabile._sqQuotables,"\\$1");
v1=v1.replace(/\n/g,"\\n");
v1=v1.replace(/\r/g,"\\r");
return "'"+v1+"'";
}
if(v instanceof Date){
return "new Date("+d.getFullYear+","+d.getMonth()+","+d.getDate()+")";
}
var d;
if(v instanceof Array||v.push){
if(_9>=dojo.widget.html.stabile.depthLimit){
return "[ ... ]";
}
d="[";
var _d=true;
dojo.widget.html.stabile._depth++;
for(var i=0;i<v.length;i++){
if(_d){
_d=false;
}else{
d+=",";
}
d+=arguments.callee(v[i],_8);
}
return d+"]";
}
if(v.constructor==Object||v.toString==_a){
if(_9>=dojo.widget.html.stabile.depthLimit){
return "{ ... }";
}
if(typeof (v.hasOwnProperty)!="function"&&v.prototype){
throw new Error("description: "+v+" not supported by script engine");
}
var _d=true;
d="{";
dojo.widget.html.stabile._depth++;
for(var _f in v){
if(v[_f]==void (0)||typeof (v[_f])=="function"){
continue;
}
if(_d){
_d=false;
}else{
d+=", ";
}
var kd=_f;
if(!kd.match(/^[a-zA-Z_][a-zA-Z0-9_]*$/)){
kd=arguments.callee(_f,_8);
}
d+=kd+": "+arguments.callee(v[_f],_8);
}
return d+"}";
}
if(_8){
if(dojo.widget.html.stabile._recur){
var _11=Object.prototype.toString;
return _11.apply(v,[]);
}else{
dojo.widget.html.stabile._recur=true;
return v.toString();
}
}else{
throw new Error("Unknown type: "+v);
return "'unknown'";
}
}
finally{
dojo.widget.html.stabile._depth=_9;
}
};
dojo.widget.html.stabile._getStorage=function(){
if(dojo.widget.html.stabile.dataField){
return dojo.widget.html.stabile.dataField;
}
var _12=document.forms._dojo_form;
return dojo.widget.html.stabile.dataField=_12?_12.stabile:{value:""};
};
