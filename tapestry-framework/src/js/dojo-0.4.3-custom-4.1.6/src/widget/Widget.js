dojo.provide("dojo.widget.Widget");
dojo.require("dojo.lang.func");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.extras");
dojo.require("dojo.lang.declare");
dojo.require("dojo.ns");
dojo.require("dojo.widget.Manager");
dojo.require("dojo.event.*");
dojo.declare("dojo.widget.Widget",null,function(){
this.children=[];
this.extraArgs={};
},{parent:null,isTopLevel:false,disabled:false,isContainer:false,widgetId:"",widgetType:"Widget",ns:"dojo",getNamespacedType:function(){
return (this.ns?this.ns+":"+this.widgetType:this.widgetType).toLowerCase();
},toString:function(){
return "[Widget "+this.getNamespacedType()+", "+(this.widgetId||"NO ID")+"]";
},repr:function(){
return this.toString();
},enable:function(){
this.disabled=false;
},disable:function(){
this.disabled=true;
},onResized:function(){
this.notifyChildrenOfResize();
},notifyChildrenOfResize:function(){
for(var i=0;i<this.children.length;i++){
var _2=this.children[i];
if(_2.onResized){
_2.onResized();
}
}
},create:function(_3,_4,_5,ns){
if(ns){
this.ns=ns;
}
this.satisfyPropertySets(_3,_4,_5);
this.mixInProperties(_3,_4,_5);
this.postMixInProperties(_3,_4,_5);
dojo.widget.manager.add(this);
this.buildRendering(_3,_4,_5);
this.initialize(_3,_4,_5);
this.postInitialize(_3,_4,_5);
this.postCreate(_3,_4,_5);
return this;
},destroy:function(_7){
if(this.parent){
this.parent.removeChild(this);
}
this.destroyChildren();
this.uninitialize();
this.destroyRendering(_7);
dojo.widget.manager.removeById(this.widgetId);
},destroyChildren:function(){
var _8;
var i=0;
while(this.children.length>i){
_8=this.children[i];
if(_8 instanceof dojo.widget.Widget){
this.removeChild(_8);
_8.destroy();
continue;
}
i++;
}
},getChildrenOfType:function(_a,_b){
var _c=[];
var _d=dojo.lang.isFunction(_a);
if(!_d){
_a=_a.toLowerCase();
}
for(var x=0;x<this.children.length;x++){
if(_d){
if(this.children[x] instanceof _a){
_c.push(this.children[x]);
}
}else{
if(this.children[x].widgetType.toLowerCase()==_a){
_c.push(this.children[x]);
}
}
if(_b){
_c=_c.concat(this.children[x].getChildrenOfType(_a,_b));
}
}
return _c;
},getDescendants:function(){
var _f=[];
var _10=[this];
var _11;
while((_11=_10.pop())){
_f.push(_11);
if(_11.children){
dojo.lang.forEach(_11.children,function(_12){
_10.push(_12);
});
}
}
return _f;
},isFirstChild:function(){
return this===this.parent.children[0];
},isLastChild:function(){
return this===this.parent.children[this.parent.children.length-1];
},satisfyPropertySets:function(_13){
return _13;
},mixInProperties:function(_14,_15){
if((_14["fastMixIn"])||(_15["fastMixIn"])){
for(var x in _14){
this[x]=_14[x];
}
return;
}
var _17;
var _18=dojo.widget.lcArgsCache[this.widgetType];
if(_18==null){
_18={};
for(var y in this){
_18[((new String(y)).toLowerCase())]=y;
}
dojo.widget.lcArgsCache[this.widgetType]=_18;
}
var _1a={};
for(var x in _14){
if(!this[x]){
var y=_18[(new String(x)).toLowerCase()];
if(y){
_14[y]=_14[x];
x=y;
}
}
if(_1a[x]){
continue;
}
_1a[x]=true;
if((typeof this[x])!=(typeof _17)){
if(typeof _14[x]!="string"){
this[x]=_14[x];
}else{
if(dojo.lang.isString(this[x])){
this[x]=_14[x];
}else{
if(dojo.lang.isNumber(this[x])){
this[x]=new Number(_14[x]);
}else{
if(dojo.lang.isBoolean(this[x])){
this[x]=(_14[x].toLowerCase()=="false")?false:true;
}else{
if(dojo.lang.isFunction(this[x])){
if(_14[x].search(/[^\w\.]+/i)==-1){
this[x]=dojo.evalObjPath(_14[x],false);
}else{
var tn=dojo.lang.nameAnonFunc(new Function(_14[x]),this);
dojo.event.kwConnect({srcObj:this,srcFunc:x,adviceObj:this,adviceFunc:tn});
}
}else{
if(dojo.lang.isArray(this[x])){
this[x]=_14[x].split(";");
}else{
if(this[x] instanceof Date){
this[x]=new Date(Number(_14[x]));
}else{
if(typeof this[x]=="object"){
if(this[x] instanceof dojo.uri.Uri){
this[x]=dojo.uri.dojoUri(_14[x]);
}else{
var _1c=_14[x].split(";");
for(var y=0;y<_1c.length;y++){
var si=_1c[y].indexOf(":");
if((si!=-1)&&(_1c[y].length>si)){
this[x][_1c[y].substr(0,si).replace(/^\s+|\s+$/g,"")]=_1c[y].substr(si+1);
}
}
}
}else{
this[x]=_14[x];
}
}
}
}
}
}
}
}
}else{
this.extraArgs[x.toLowerCase()]=_14[x];
}
}
},postMixInProperties:function(_1e,_1f,_20){
},initialize:function(_21,_22,_23){
return false;
},postInitialize:function(_24,_25,_26){
return false;
},postCreate:function(_27,_28,_29){
return false;
},uninitialize:function(){
return false;
},buildRendering:function(_2a,_2b,_2c){
dojo.unimplemented("dojo.widget.Widget.buildRendering, on "+this.toString()+", ");
return false;
},destroyRendering:function(){
dojo.unimplemented("dojo.widget.Widget.destroyRendering");
return false;
},addedTo:function(_2d){
},addChild:function(_2e){
dojo.unimplemented("dojo.widget.Widget.addChild");
return false;
},removeChild:function(_2f){
for(var x=0;x<this.children.length;x++){
if(this.children[x]===_2f){
this.children.splice(x,1);
_2f.parent=null;
break;
}
}
return _2f;
},getPreviousSibling:function(){
var idx=this.getParentIndex();
if(idx<=0){
return null;
}
return this.parent.children[idx-1];
},getSiblings:function(){
return this.parent.children;
},getParentIndex:function(){
return dojo.lang.indexOf(this.parent.children,this,true);
},getNextSibling:function(){
var idx=this.getParentIndex();
if(idx==this.parent.children.length-1){
return null;
}
if(idx<0){
return null;
}
return this.parent.children[idx+1];
}});
dojo.widget.lcArgsCache={};
dojo.widget.tags={};
dojo.widget.tags.addParseTreeHandler=function(_33){
dojo.deprecated("addParseTreeHandler",". ParseTreeHandlers are now reserved for components. Any unfiltered DojoML tag without a ParseTreeHandler is assumed to be a widget","0.5");
};
dojo.widget.tags["dojo:propertyset"]=function(_34,_35,_36){
var _37=_35.parseProperties(_34["dojo:propertyset"]);
};
dojo.widget.tags["dojo:connect"]=function(_38,_39,_3a){
var _3b=_39.parseProperties(_38["dojo:connect"]);
};
dojo.widget.buildWidgetFromParseTree=function(_3c,_3d,_3e,_3f,_40,_41){
var _42=_3c.split(":");
_42=(_42.length==2)?_42[1]:_3c;
var _43=_41||_3e.parseProperties(_3d[_3d["ns"]+":"+_42]);
var _44=dojo.widget.manager.getImplementation(_42,null,null,_3d["ns"]);
if(!_44){
throw new Error("cannot find \""+_3c+"\" widget");
}else{
if(!_44.create){
throw new Error("\""+_3c+"\" widget object has no \"create\" method and does not appear to implement *Widget");
}
}
_43["dojoinsertionindex"]=_40;
var ret=_44.create(_43,_3d,_3f,_3d["ns"]);
return ret;
};
dojo.widget.defineWidget=function(_46,_47,_48,_49,_4a){
if(dojo.lang.isString(arguments[3])){
dojo.widget._defineWidget(arguments[0],arguments[3],arguments[1],arguments[4],arguments[2]);
}else{
var _4b=[arguments[0]],p=3;
if(dojo.lang.isString(arguments[1])){
_4b.push(arguments[1],arguments[2]);
}else{
_4b.push("",arguments[1]);
p=2;
}
if(dojo.lang.isFunction(arguments[p])){
_4b.push(arguments[p],arguments[p+1]);
}else{
_4b.push(null,arguments[p]);
}
dojo.widget._defineWidget.apply(this,_4b);
}
};
dojo.widget.defineWidget.renderers="html|svg|vml";
dojo.widget._defineWidget=function(_4d,_4e,_4f,_50,_51){
var _52=_4d.split(".");
var _53=_52.pop();
var _54="\\.("+(_4e?_4e+"|":"")+dojo.widget.defineWidget.renderers+")\\.";
var r=_4d.search(new RegExp(_54));
_52=(r<0?_52.join("."):_4d.substr(0,r));
dojo.widget.manager.registerWidgetPackage(_52);
var pos=_52.indexOf(".");
var _57=(pos>-1)?_52.substring(0,pos):_52;
_51=(_51)||{};
_51.widgetType=_53;
if((!_50)&&(_51["classConstructor"])){
_50=_51.classConstructor;
delete _51.classConstructor;
}
dojo.declare(_4d,_4f,_50,_51);
};
