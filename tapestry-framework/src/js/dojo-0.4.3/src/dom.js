dojo.provide("dojo.dom");
dojo.dom.ELEMENT_NODE=1;
dojo.dom.ATTRIBUTE_NODE=2;
dojo.dom.TEXT_NODE=3;
dojo.dom.CDATA_SECTION_NODE=4;
dojo.dom.ENTITY_REFERENCE_NODE=5;
dojo.dom.ENTITY_NODE=6;
dojo.dom.PROCESSING_INSTRUCTION_NODE=7;
dojo.dom.COMMENT_NODE=8;
dojo.dom.DOCUMENT_NODE=9;
dojo.dom.DOCUMENT_TYPE_NODE=10;
dojo.dom.DOCUMENT_FRAGMENT_NODE=11;
dojo.dom.NOTATION_NODE=12;
dojo.dom.dojoml="http://www.dojotoolkit.org/2004/dojoml";
dojo.dom.xmlns={svg:"http://www.w3.org/2000/svg",smil:"http://www.w3.org/2001/SMIL20/",mml:"http://www.w3.org/1998/Math/MathML",cml:"http://www.xml-cml.org",xlink:"http://www.w3.org/1999/xlink",xhtml:"http://www.w3.org/1999/xhtml",xul:"http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul",xbl:"http://www.mozilla.org/xbl",fo:"http://www.w3.org/1999/XSL/Format",xsl:"http://www.w3.org/1999/XSL/Transform",xslt:"http://www.w3.org/1999/XSL/Transform",xi:"http://www.w3.org/2001/XInclude",xforms:"http://www.w3.org/2002/01/xforms",saxon:"http://icl.com/saxon",xalan:"http://xml.apache.org/xslt",xsd:"http://www.w3.org/2001/XMLSchema",dt:"http://www.w3.org/2001/XMLSchema-datatypes",xsi:"http://www.w3.org/2001/XMLSchema-instance",rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#",rdfs:"http://www.w3.org/2000/01/rdf-schema#",dc:"http://purl.org/dc/elements/1.1/",dcq:"http://purl.org/dc/qualifiers/1.0","soap-env":"http://schemas.xmlsoap.org/soap/envelope/",wsdl:"http://schemas.xmlsoap.org/wsdl/",AdobeExtensions:"http://ns.adobe.com/AdobeSVGViewerExtensions/3.0/"};
dojo.dom.isNode=function(wh){
if(typeof Element=="function"){
try{
return wh instanceof Element;
}
catch(e){
}
}else{
return wh&&!isNaN(wh.nodeType);
}
};
dojo.dom.getUniqueId=function(){
var _2=dojo.doc();
do{
var id="dj_unique_"+(++arguments.callee._idIncrement);
}while(_2.getElementById(id));
return id;
};
dojo.dom.getUniqueId._idIncrement=0;
dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_4,_5){
var _6=_4.firstChild;
while(_6&&_6.nodeType!=dojo.dom.ELEMENT_NODE){
_6=_6.nextSibling;
}
if(_5&&_6&&_6.tagName&&_6.tagName.toLowerCase()!=_5.toLowerCase()){
_6=dojo.dom.nextElement(_6,_5);
}
return _6;
};
dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_7,_8){
var _9=_7.lastChild;
while(_9&&_9.nodeType!=dojo.dom.ELEMENT_NODE){
_9=_9.previousSibling;
}
if(_8&&_9&&_9.tagName&&_9.tagName.toLowerCase()!=_8.toLowerCase()){
_9=dojo.dom.prevElement(_9,_8);
}
return _9;
};
dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(_a,_b){
if(!_a){
return null;
}
do{
_a=_a.nextSibling;
}while(_a&&_a.nodeType!=dojo.dom.ELEMENT_NODE);
if(_a&&_b&&_b.toLowerCase()!=_a.tagName.toLowerCase()){
return dojo.dom.nextElement(_a,_b);
}
return _a;
};
dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(_c,_d){
if(!_c){
return null;
}
if(_d){
_d=_d.toLowerCase();
}
do{
_c=_c.previousSibling;
}while(_c&&_c.nodeType!=dojo.dom.ELEMENT_NODE);
if(_c&&_d&&_d.toLowerCase()!=_c.tagName.toLowerCase()){
return dojo.dom.prevElement(_c,_d);
}
return _c;
};
dojo.dom.moveChildren=function(_e,_f,_10){
var _11=0;
if(_10){
while(_e.hasChildNodes()&&_e.firstChild.nodeType==dojo.dom.TEXT_NODE){
_e.removeChild(_e.firstChild);
}
while(_e.hasChildNodes()&&_e.lastChild.nodeType==dojo.dom.TEXT_NODE){
_e.removeChild(_e.lastChild);
}
}
while(_e.hasChildNodes()){
_f.appendChild(_e.firstChild);
_11++;
}
return _11;
};
dojo.dom.copyChildren=function(_12,_13,_14){
var _15=_12.cloneNode(true);
return this.moveChildren(_15,_13,_14);
};
dojo.dom.replaceChildren=function(_16,_17){
var _18=[];
if(dojo.render.html.ie){
for(var i=0;i<_16.childNodes.length;i++){
_18.push(_16.childNodes[i]);
}
}
dojo.dom.removeChildren(_16);
_16.appendChild(_17);
for(var i=0;i<_18.length;i++){
dojo.dom.destroyNode(_18[i]);
}
};
dojo.dom.removeChildren=function(_1a){
var _1b=_1a.childNodes.length;
while(_1a.hasChildNodes()){
dojo.dom.removeNode(_1a.firstChild);
}
return _1b;
};
dojo.dom.replaceNode=function(_1c,_1d){
return _1c.parentNode.replaceChild(_1d,_1c);
};
dojo.dom.destroyNode=function(_1e){
if(_1e.parentNode){
_1e=dojo.dom.removeNode(_1e);
}
if(_1e.nodeType!=3){
if(dojo.evalObjPath("dojo.event.browser.clean",false)){
dojo.event.browser.clean(_1e);
}
if(dojo.render.html.ie){
_1e.outerHTML="";
}
}
};
dojo.dom.removeNode=function(_1f){
if(_1f&&_1f.parentNode){
return _1f.parentNode.removeChild(_1f);
}
};
dojo.dom.getAncestors=function(_20,_21,_22){
var _23=[];
var _24=(_21&&(_21 instanceof Function||typeof _21=="function"));
while(_20){
if(!_24||_21(_20)){
_23.push(_20);
}
if(_22&&_23.length>0){
return _23[0];
}
_20=_20.parentNode;
}
if(_22){
return null;
}
return _23;
};
dojo.dom.getAncestorsByTag=function(_25,tag,_27){
tag=tag.toLowerCase();
return dojo.dom.getAncestors(_25,function(el){
return ((el.tagName)&&(el.tagName.toLowerCase()==tag));
},_27);
};
dojo.dom.getFirstAncestorByTag=function(_29,tag){
return dojo.dom.getAncestorsByTag(_29,tag,true);
};
dojo.dom.isDescendantOf=function(_2b,_2c,_2d){
if(_2d&&_2b){
_2b=_2b.parentNode;
}
while(_2b){
if(_2b==_2c){
return true;
}
_2b=_2b.parentNode;
}
return false;
};
dojo.dom.innerXML=function(_2e){
if(_2e.innerXML){
return _2e.innerXML;
}else{
if(_2e.xml){
return _2e.xml;
}else{
if(typeof XMLSerializer!="undefined"){
return (new XMLSerializer()).serializeToString(_2e);
}
}
}
};
dojo.dom.createDocument=function(){
var doc=null;
var _30=dojo.doc();
if(!dj_undef("ActiveXObject")){
var _31=["MSXML2","Microsoft","MSXML","MSXML3"];
for(var i=0;i<_31.length;i++){
try{
doc=new ActiveXObject(_31[i]+".XMLDOM");
}
catch(e){
}
if(doc){
break;
}
}
}else{
if((_30.implementation)&&(_30.implementation.createDocument)){
doc=_30.implementation.createDocument("","",null);
}
}
return doc;
};
dojo.dom.createDocumentFromText=function(str,_34){
if(!_34){
_34="text/xml";
}
if(!dj_undef("DOMParser")){
var _35=new DOMParser();
return _35.parseFromString(str,_34);
}else{
if(!dj_undef("ActiveXObject")){
var _36=dojo.dom.createDocument();
if(_36){
_36.async=false;
_36.loadXML(str);
return _36;
}else{
dojo.debug("toXml didn't work?");
}
}else{
var _37=dojo.doc();
if(_37.createElement){
var tmp=_37.createElement("xml");
tmp.innerHTML=str;
if(_37.implementation&&_37.implementation.createDocument){
var _39=_37.implementation.createDocument("foo","",null);
for(var i=0;i<tmp.childNodes.length;i++){
_39.importNode(tmp.childNodes.item(i),true);
}
return _39;
}
return ((tmp.document)&&(tmp.document.firstChild?tmp.document.firstChild:tmp));
}
}
}
return null;
};
dojo.dom.prependChild=function(_3b,_3c){
if(_3c.firstChild){
_3c.insertBefore(_3b,_3c.firstChild);
}else{
_3c.appendChild(_3b);
}
return true;
};
dojo.dom.insertBefore=function(_3d,ref,_3f){
if((_3f!=true)&&(_3d===ref||_3d.nextSibling===ref)){
return false;
}
var _40=ref.parentNode;
_40.insertBefore(_3d,ref);
return true;
};
dojo.dom.insertAfter=function(_41,ref,_43){
var pn=ref.parentNode;
if(ref==pn.lastChild){
if((_43!=true)&&(_41===ref)){
return false;
}
pn.appendChild(_41);
}else{
return this.insertBefore(_41,ref.nextSibling,_43);
}
return true;
};
dojo.dom.insertAtPosition=function(_45,ref,_47){
if((!_45)||(!ref)||(!_47)){
return false;
}
switch(_47.toLowerCase()){
case "before":
return dojo.dom.insertBefore(_45,ref);
case "after":
return dojo.dom.insertAfter(_45,ref);
case "first":
if(ref.firstChild){
return dojo.dom.insertBefore(_45,ref.firstChild);
}else{
ref.appendChild(_45);
return true;
}
break;
default:
ref.appendChild(_45);
return true;
}
};
dojo.dom.insertAtIndex=function(_48,_49,_4a){
var _4b=_49.childNodes;
if(!_4b.length||_4b.length==_4a){
_49.appendChild(_48);
return true;
}
if(_4a==0){
return dojo.dom.prependChild(_48,_49);
}
return dojo.dom.insertAfter(_48,_4b[_4a-1]);
};
dojo.dom.textContent=function(_4c,_4d){
if(arguments.length>1){
var _4e=dojo.doc();
dojo.dom.replaceChildren(_4c,_4e.createTextNode(_4d));
return _4d;
}else{
if(_4c.textContent!=undefined){
return _4c.textContent;
}
var _4f="";
if(_4c==null){
return _4f;
}
for(var i=0;i<_4c.childNodes.length;i++){
switch(_4c.childNodes[i].nodeType){
case 1:
case 5:
_4f+=dojo.dom.textContent(_4c.childNodes[i]);
break;
case 3:
case 2:
case 4:
_4f+=_4c.childNodes[i].nodeValue;
break;
default:
break;
}
}
return _4f;
}
};
dojo.dom.hasParent=function(_51){
return Boolean(_51&&_51.parentNode&&dojo.dom.isNode(_51.parentNode));
};
dojo.dom.isTag=function(_52){
if(_52&&_52.tagName){
for(var i=1;i<arguments.length;i++){
if(_52.tagName==String(arguments[i])){
return String(arguments[i]);
}
}
}
return "";
};
dojo.dom.setAttributeNS=function(_54,_55,_56,_57){
if(_54==null||((_54==undefined)&&(typeof _54=="undefined"))){
dojo.raise("No element given to dojo.dom.setAttributeNS");
}
if(!((_54.setAttributeNS==undefined)&&(typeof _54.setAttributeNS=="undefined"))){
_54.setAttributeNS(_55,_56,_57);
}else{
var _58=_54.ownerDocument;
var _59=_58.createNode(2,_56,_55);
_59.nodeValue=_57;
_54.setAttributeNode(_59);
}
};
