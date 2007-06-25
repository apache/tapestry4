dojo.provide("dojo.html.common");
dojo.require("dojo.lang.common");
dojo.require("dojo.dom");
dojo.lang.mixin(dojo.html,dojo.dom);
dojo.html.body=function(){
dojo.deprecated("dojo.html.body() moved to dojo.body()","0.5");
return dojo.body();
};
dojo.html.getEventTarget=function(_1){
if(!_1){
_1=dojo.global().event||{};
}
var t=(_1.srcElement?_1.srcElement:(_1.target?_1.target:null));
while((t)&&(t.nodeType!=1)){
t=t.parentNode;
}
return t;
};
dojo.html.getViewport=function(){
var _3=dojo.global();
var _4=dojo.doc();
var w=0;
var h=0;
if(dojo.render.html.mozilla){
w=_4.documentElement.clientWidth;
h=_3.innerHeight;
}else{
if(!dojo.render.html.opera&&_3.innerWidth){
w=_3.innerWidth;
h=_3.innerHeight;
}else{
if(!dojo.render.html.opera&&dojo.exists(_4,"documentElement.clientWidth")){
var w2=_4.documentElement.clientWidth;
if(!w||w2&&w2<w){
w=w2;
}
h=_4.documentElement.clientHeight;
}else{
if(dojo.body().clientWidth){
w=dojo.body().clientWidth;
h=dojo.body().clientHeight;
}
}
}
}
return {width:w,height:h};
};
dojo.html.getScroll=function(){
var _8=dojo.global();
var _9=dojo.doc();
var _a=_8.pageYOffset||_9.documentElement.scrollTop||dojo.body().scrollTop||0;
var _b=_8.pageXOffset||_9.documentElement.scrollLeft||dojo.body().scrollLeft||0;
return {top:_a,left:_b,offset:{x:_b,y:_a}};
};
dojo.html.getParentByType=function(_c,_d){
var _e=dojo.doc();
var _f=dojo.byId(_c);
_d=_d.toLowerCase();
while((_f)&&(_f.nodeName.toLowerCase()!=_d)){
if(_f==(_e["body"]||_e["documentElement"])){
return null;
}
_f=_f.parentNode;
}
return _f;
};
dojo.html.getAttribute=function(_10,_11){
_10=dojo.byId(_10);
if((!_10)||(!_10.getAttribute)){
return null;
}
var ta=typeof _11=="string"?_11:new String(_11);
var v=_10.getAttribute(ta.toUpperCase());
if((v)&&(typeof v=="string")&&(v!="")){
return v;
}
if(v&&v.value){
return v.value;
}
if((_10.getAttributeNode)&&(_10.getAttributeNode(ta))){
return (_10.getAttributeNode(ta)).value;
}else{
if(_10.getAttribute(ta)){
return _10.getAttribute(ta);
}else{
if(_10.getAttribute(ta.toLowerCase())){
return _10.getAttribute(ta.toLowerCase());
}
}
}
return null;
};
dojo.html.hasAttribute=function(_14,_15){
return dojo.html.getAttribute(dojo.byId(_14),_15)?true:false;
};
dojo.html.getCursorPosition=function(e){
e=e||dojo.global().event;
var _17={x:0,y:0};
if(e.pageX||e.pageY){
_17.x=e.pageX;
_17.y=e.pageY;
}else{
var de=dojo.doc().documentElement;
var db=dojo.body();
_17.x=e.clientX+((de||db)["scrollLeft"])-((de||db)["clientLeft"]);
_17.y=e.clientY+((de||db)["scrollTop"])-((de||db)["clientTop"]);
}
return _17;
};
dojo.html.isTag=function(_1a){
_1a=dojo.byId(_1a);
if(_1a&&_1a.tagName){
for(var i=1;i<arguments.length;i++){
if(_1a.tagName.toLowerCase()==String(arguments[i]).toLowerCase()){
return String(arguments[i]).toLowerCase();
}
}
}
return "";
};
if(dojo.render.html.ie&&!dojo.render.html.ie70){
if(window.location.href.substr(0,6).toLowerCase()!="https:"){
(function(){
var _1c=dojo.doc().createElement("script");
_1c.src="javascript:'dojo.html.createExternalElement=function(doc, tag){ return doc.createElement(tag); }'";
dojo.doc().getElementsByTagName("head")[0].appendChild(_1c);
})();
}
}else{
dojo.html.createExternalElement=function(doc,tag){
return doc.createElement(tag);
};
}
dojo.html._callDeprecated=function(_1f,_20,_21,_22,_23){
dojo.deprecated("dojo.html."+_1f,"replaced by dojo.html."+_20+"("+(_22?"node, {"+_22+": "+_22+"}":"")+")"+(_23?"."+_23:""),"0.5");
var _24=[];
if(_22){
var _25={};
_25[_22]=_21[1];
_24.push(_21[0]);
_24.push(_25);
}else{
_24=_21;
}
var ret=dojo.html[_20].apply(dojo.html,_21);
if(_23){
return ret[_23];
}else{
return ret;
}
};
dojo.html.getViewportWidth=function(){
return dojo.html._callDeprecated("getViewportWidth","getViewport",arguments,null,"width");
};
dojo.html.getViewportHeight=function(){
return dojo.html._callDeprecated("getViewportHeight","getViewport",arguments,null,"height");
};
dojo.html.getViewportSize=function(){
return dojo.html._callDeprecated("getViewportSize","getViewport",arguments);
};
dojo.html.getScrollTop=function(){
return dojo.html._callDeprecated("getScrollTop","getScroll",arguments,null,"top");
};
dojo.html.getScrollLeft=function(){
return dojo.html._callDeprecated("getScrollLeft","getScroll",arguments,null,"left");
};
dojo.html.getScrollOffset=function(){
return dojo.html._callDeprecated("getScrollOffset","getScroll",arguments,null,"offset");
};
