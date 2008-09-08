dojo.provide("dojo.widget.Manager");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.func");
dojo.require("dojo.event.*");
dojo.widget.manager=new function(){
this.widgets=[];
this.widgetIds=[];
this.topWidgets={};
var _1={};
var _2=[];
this.getUniqueId=function(_3){
var _4;
do{
_4=_3+"_"+(_1[_3]!=undefined?++_1[_3]:_1[_3]=0);
}while(this.getWidgetById(_4));
return _4;
};
this.add=function(_5){
this.widgets.push(_5);
if(!_5.extraArgs["id"]){
_5.extraArgs["id"]=_5.extraArgs["ID"];
}
if(_5.widgetId==""){
if(_5["id"]){
_5.widgetId=_5["id"];
}else{
if(_5.extraArgs["id"]){
_5.widgetId=_5.extraArgs["id"];
}else{
_5.widgetId=this.getUniqueId(_5.ns+"_"+_5.widgetType);
}
}
}
if(this.widgetIds[_5.widgetId]){
dojo.debug("widget ID collision on ID: "+_5.widgetId);
}
this.widgetIds[_5.widgetId]=_5;
};
this.destroyAll=function(){
for(var x=this.widgets.length-1;x>=0;x--){
try{
this.widgets[x].destroy(true);
delete this.widgets[x];
}
catch(e){
}
}
};
this.remove=function(_7){
if(dojo.lang.isNumber(_7)){
var tw=this.widgets[_7].widgetId;
delete this.topWidgets[tw];
delete this.widgetIds[tw];
this.widgets.splice(_7,1);
}else{
this.removeById(_7);
}
};
this.removeById=function(id){
if(!dojo.lang.isString(id)){
id=id["widgetId"];
if(!id){
dojo.debug("invalid widget or id passed to removeById");
return;
}
}
for(var i=0;i<this.widgets.length;i++){
if(this.widgets[i].widgetId==id){
this.remove(i);
break;
}
}
};
this.getWidgetById=function(id){
if(dojo.lang.isString(id)){
return this.widgetIds[id];
}
return id;
};
this.getWidgetsByType=function(_c){
var lt=_c.toLowerCase();
var _e=(_c.indexOf(":")<0?function(x){
return x.widgetType.toLowerCase();
}:function(x){
return x.getNamespacedType();
});
var ret=[];
dojo.lang.forEach(this.widgets,function(x){
if(_e(x)==lt){
ret.push(x);
}
});
return ret;
};
this.getWidgetsByFilter=function(_13,_14){
var ret=[];
dojo.lang.every(this.widgets,function(x){
if(_13(x)){
ret.push(x);
if(_14){
return false;
}
}
return true;
});
return (_14?ret[0]:ret);
};
this.getAllWidgets=function(){
return this.widgets.concat();
};
this.getWidgetByNode=function(_17){
var w=this.getAllWidgets();
_17=dojo.byId(_17);
for(var i=0;i<w.length;i++){
if(w[i].domNode==_17){
return w[i];
}
}
return null;
};
this.byId=this.getWidgetById;
this.byType=this.getWidgetsByType;
this.byFilter=this.getWidgetsByFilter;
this.byNode=this.getWidgetByNode;
var _1a={};
var _1b=["dojo.widget"];
for(var i=0;i<_1b.length;i++){
_1b[_1b[i]]=true;
}
this.registerWidgetPackage=function(_1d){
if(!_1b[_1d]){
_1b[_1d]=true;
_1b.push(_1d);
}
};
this.getWidgetPackageList=function(){
return dojo.lang.map(_1b,function(elt){
return (elt!==true?elt:undefined);
});
};
this.getImplementation=function(_1f,_20,_21,ns){
var _23=this.getImplementationName(_1f,ns);
if(_23){
var ret=_20?new _23(_20):new _23();
return ret;
}
};
function buildPrefixCache(){
for(var _25 in dojo.render){
if(dojo.render[_25]["capable"]===true){
var _26=dojo.render[_25].prefixes;
for(var i=0;i<_26.length;i++){
_2.push(_26[i].toLowerCase());
}
}
}
}
var _28=function(_29,_2a){
if(!_2a){
return null;
}
for(var i=0,l=_2.length,_2d;i<=l;i++){
_2d=(i<l?_2a[_2[i]]:_2a);
if(!_2d){
continue;
}
for(var _2e in _2d){
if(_2e.toLowerCase()==_29){
return _2d[_2e];
}
}
}
return null;
};
var _2f=function(_30,_31){
var _32=dojo.evalObjPath(_31,false);
return (_32?_28(_30,_32):null);
};
this.getImplementationName=function(_33,ns){
var _35=_33.toLowerCase();
ns=ns||"dojo";
var _36=_1a[ns]||(_1a[ns]={});
var _37=_36[_35];
if(_37){
return _37;
}
if(!_2.length){
buildPrefixCache();
}
var _38=dojo.ns.get(ns);
if(!_38){
dojo.ns.register(ns,ns+".widget");
_38=dojo.ns.get(ns);
}
if(_38){
_38.resolve(_33);
}
_37=_2f(_35,_38.module);
if(_37){
return (_36[_35]=_37);
}
_38=dojo.ns.require(ns);
if((_38)&&(_38.resolver)){
_38.resolve(_33);
_37=_2f(_35,_38.module);
if(_37){
return (_36[_35]=_37);
}
}
dojo.deprecated("dojo.widget.Manager.getImplementationName","Could not locate widget implementation for \""+_33+"\" in \""+_38.module+"\" registered to namespace \""+_38.name+"\". "+"Developers must specify correct namespaces for all non-Dojo widgets","0.5");
for(var i=0;i<_1b.length;i++){
_37=_2f(_35,_1b[i]);
if(_37){
return (_36[_35]=_37);
}
}
throw new Error("Could not locate widget implementation for \""+_33+"\" in \""+_38.module+"\" registered to namespace \""+_38.name+"\"");
};
this.resizing=false;
this.onWindowResized=function(){
if(this.resizing){
return;
}
try{
this.resizing=true;
for(var id in this.topWidgets){
var _3b=this.topWidgets[id];
if(_3b.checkSize){
_3b.checkSize();
}
}
}
catch(e){
}
finally{
this.resizing=false;
}
};
if(typeof window!="undefined"){
dojo.addOnLoad(this,"onWindowResized");
dojo.event.connect(window,"onresize",this,"onWindowResized");
}
};
(function(){
var dw=dojo.widget;
var dwm=dw.manager;
var h=dojo.lang.curry(dojo.lang,"hitch",dwm);
var g=function(_40,_41){
dw[(_41||_40)]=h(_40);
};
g("add","addWidget");
g("destroyAll","destroyAllWidgets");
g("remove","removeWidget");
g("removeById","removeWidgetById");
g("getWidgetById");
g("getWidgetById","byId");
g("getWidgetsByType");
g("getWidgetsByFilter");
g("getWidgetsByType","byType");
g("getWidgetsByFilter","byFilter");
g("getWidgetByNode","byNode");
dw.all=function(n){
var _43=dwm.getAllWidgets.apply(dwm,arguments);
if(arguments.length>0){
return _43[n];
}
return _43;
};
g("registerWidgetPackage");
g("getImplementation","getWidgetImplementation");
g("getImplementationName","getWidgetImplementationName");
dw.widgets=dwm.widgets;
dw.widgetIds=dwm.widgetIds;
dw.root=dwm.root;
})();
