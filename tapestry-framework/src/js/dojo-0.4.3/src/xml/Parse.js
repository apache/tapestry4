dojo.provide("dojo.xml.Parse");
dojo.require("dojo.dom");
dojo.xml.Parse=function(){
var _1=((dojo.render.html.capable)&&(dojo.render.html.ie));
function getTagName(_2){
try{
return _2.tagName.toLowerCase();
}
catch(e){
return "";
}
}
function getDojoTagName(_3){
var _4=getTagName(_3);
if(!_4){
return "";
}
if((dojo.widget)&&(dojo.widget.tags[_4])){
return _4;
}
var p=_4.indexOf(":");
if(p>=0){
return _4;
}
if(_4.substr(0,5)=="dojo:"){
return _4;
}
if(dojo.render.html.capable&&dojo.render.html.ie&&_3.scopeName!="HTML"){
return _3.scopeName.toLowerCase()+":"+_4;
}
if(_4.substr(0,4)=="dojo"){
return "dojo:"+_4.substring(4);
}
var _6=_3.getAttribute("dojoType")||_3.getAttribute("dojotype");
if(_6){
if(_6.indexOf(":")<0){
_6="dojo:"+_6;
}
return _6.toLowerCase();
}
_6=_3.getAttributeNS&&_3.getAttributeNS(dojo.dom.dojoml,"type");
if(_6){
return "dojo:"+_6.toLowerCase();
}
try{
_6=_3.getAttribute("dojo:type");
}
catch(e){
}
if(_6){
return "dojo:"+_6.toLowerCase();
}
if((dj_global["djConfig"])&&(!djConfig["ignoreClassNames"])){
var _7=_3.className||_3.getAttribute("class");
if((_7)&&(_7.indexOf)&&(_7.indexOf("dojo-")!=-1)){
var _8=_7.split(" ");
for(var x=0,c=_8.length;x<c;x++){
if(_8[x].slice(0,5)=="dojo-"){
return "dojo:"+_8[x].substr(5).toLowerCase();
}
}
}
}
return "";
}
this.parseElement=function(_b,_c,_d,_e){
var _f=getTagName(_b);
if(_1&&_f.indexOf("/")==0){
return null;
}
try{
var _10=_b.getAttribute("parseWidgets");
if(_10&&_10.toLowerCase()=="false"){
return {};
}
}
catch(e){
}
var _11=true;
if(_d){
var _12=getDojoTagName(_b);
_f=_12||_f;
_11=Boolean(_12);
}
var _13={};
_13[_f]=[];
var pos=_f.indexOf(":");
if(pos>0){
var ns=_f.substring(0,pos);
_13["ns"]=ns;
if((dojo.ns)&&(!dojo.ns.allow(ns))){
_11=false;
}
}
if(_11){
var _16=this.parseAttributes(_b);
for(var _10 in _16){
if((!_13[_f][_10])||(typeof _13[_f][_10]!="array")){
_13[_f][_10]=[];
}
_13[_f][_10].push(_16[_10]);
}
_13[_f].nodeRef=_b;
_13.tagName=_f;
_13.index=_e||0;
}
var _17=0;
for(var i=0;i<_b.childNodes.length;i++){
var tcn=_b.childNodes.item(i);
switch(tcn.nodeType){
case dojo.dom.ELEMENT_NODE:
var ctn=getDojoTagName(tcn)||getTagName(tcn);
if(!_13[ctn]){
_13[ctn]=[];
}
_13[ctn].push(this.parseElement(tcn,true,_d,_17));
if((tcn.childNodes.length==1)&&(tcn.childNodes.item(0).nodeType==dojo.dom.TEXT_NODE)){
_13[ctn][_13[ctn].length-1].value=tcn.childNodes.item(0).nodeValue;
}
_17++;
break;
case dojo.dom.TEXT_NODE:
if(_b.childNodes.length==1){
_13[_f].push({value:_b.childNodes.item(0).nodeValue});
}
break;
default:
break;
}
}
return _13;
};
this.parseAttributes=function(_1b){
var _1c={};
var _1d=_1b.attributes;
var _1e,i=0;
while((_1e=_1d[i++])){
if(_1){
if(!_1e){
continue;
}
if((typeof _1e=="object")&&(typeof _1e.nodeValue=="undefined")||(_1e.nodeValue==null)||(_1e.nodeValue=="")){
continue;
}
}
var nn=_1e.nodeName.split(":");
nn=(nn.length==2)?nn[1]:_1e.nodeName;
_1c[nn]={value:_1e.nodeValue};
}
return _1c;
};
};
