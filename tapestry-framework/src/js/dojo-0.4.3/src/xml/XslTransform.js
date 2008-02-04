dojo.provide("dojo.xml.XslTransform");
dojo.xml.XslTransform=function(_1){
dojo.debug("XslTransform is supported by Internet Explorer and Mozilla, with limited support in Opera 9 (no document function support).");
var _2=dojo.render.html.ie;
var _3=["Msxml2.DOMDocument.5.0","Msxml2.DOMDocument.4.0","Msxml2.DOMDocument.3.0","MSXML2.DOMDocument","MSXML.DOMDocument","Microsoft.XMLDOM"];
var _4=["Msxml2.FreeThreadedDOMDocument.5.0","MSXML2.FreeThreadedDOMDocument.4.0","MSXML2.FreeThreadedDOMDocument.3.0"];
var _5=["Msxml2.XSLTemplate.5.0","Msxml2.XSLTemplate.4.0","MSXML2.XSLTemplate.3.0"];
function getActiveXImpl(_6){
for(var i=0;i<_6.length;i++){
try{
var _8=new ActiveXObject(_6[i]);
if(_8){
return _6[i];
}
}
catch(e){
}
}
dojo.raise("Could not find an ActiveX implementation in:\n\n "+_6);
}
if(_1==null||_1==undefined){
dojo.raise("You must pass the URI String for the XSL file to be used!");
return false;
}
var _9=null;
var _a=null;
if(_2){
_9=new ActiveXObject(getActiveXImpl(_4));
_9.async=false;
}else{
_a=new XSLTProcessor();
_9=document.implementation.createDocument("","",null);
_9.addEventListener("load",onXslLoad,false);
}
_9.load(_1);
if(_2){
var _b=new ActiveXObject(getActiveXImpl(_5));
_b.stylesheet=_9;
_a=_b.createProcessor();
}
function onXslLoad(){
_a.importStylesheet(_9);
}
function getResultDom(_c,_d){
if(_2){
addIeParams(_d);
var _e=getIeResultDom(_c);
removeIeParams(_d);
return _e;
}else{
return getMozillaResultDom(_c,_d);
}
}
function addIeParams(_f){
if(!_f){
return;
}
for(var i=0;i<_f.length;i++){
_a.addParameter(_f[i][0],_f[i][1]);
}
}
function removeIeParams(_11){
if(!_11){
return;
}
for(var i=0;i<_11.length;i++){
_a.addParameter(_11[i][0],"");
}
}
function getIeResultDom(_13){
_a.input=_13;
var _14=new ActiveXObject(getActiveXImpl(_3));
_14.async=false;
_14.validateOnParse=false;
_a.output=_14;
_a.transform();
if(_14.parseError.errorCode!=0){
var err=_14.parseError;
dojo.raise("err.errorCode: "+err.errorCode+"\n\nerr.reason: "+err.reason+"\n\nerr.url: "+err.url+"\n\nerr.srcText: "+err.srcText);
}
return _14;
}
function getIeResultStr(_16,_17){
_a.input=_16;
_a.transform();
return _a.output;
}
function addMozillaParams(_18){
if(!_18){
return;
}
for(var i=0;i<_18.length;i++){
_a.setParameter(null,_18[i][0],_18[i][1]);
}
}
function getMozillaResultDom(_1a,_1b){
addMozillaParams(_1b);
var _1c=_a.transformToDocument(_1a);
_a.clearParameters();
return _1c;
}
function getMozillaResultStr(_1d,_1e,_1f){
addMozillaParams(_1e);
var _20=_a.transformToFragment(_1d,_1f);
var _21=new XMLSerializer();
_a.clearParameters();
return _21.serializeToString(_20);
}
this.getResultString=function(_22,_23,_24){
var _25=null;
if(_2){
addIeParams(_23);
_25=getIeResultStr(_22,_23);
removeIeParams(_23);
}else{
_25=getMozillaResultStr(_22,_23,_24);
}
return _25;
};
this.transformToContentPane=function(_26,_27,_28,_29){
var _2a=this.getResultString(_26,_27,_29);
_28.setContent(_2a);
};
this.transformToRegion=function(_2b,_2c,_2d,_2e){
try{
var _2f=this.getResultString(_2b,_2c,_2e);
_2d.innerHTML=_2f;
}
catch(e){
dojo.raise(e.message+"\n\n xsltUri: "+_1);
}
};
this.transformToDocument=function(_30,_31){
return getResultDom(_30,_31);
};
this.transformToWindow=function(_32,_33,_34,_35){
try{
_34.open();
_34.write(this.getResultString(_32,_33,_35));
_34.close();
}
catch(e){
dojo.raise(e.message+"\n\n xsltUri: "+_1);
}
};
};
