dojo.provide("dojo.html.style");
dojo.require("dojo.html.common");
dojo.require("dojo.uri.Uri");
dojo.html.getClass=function(_1){
_1=dojo.byId(_1);
if(!_1){
return "";
}
var cs="";
if(_1.className){
cs=_1.className;
}else{
if(dojo.html.hasAttribute(_1,"class")){
cs=dojo.html.getAttribute(_1,"class");
}
}
return cs.replace(/^\s+|\s+$/g,"");
};
dojo.html.getClasses=function(_3){
var c=dojo.html.getClass(_3);
return (c=="")?[]:c.split(/\s+/g);
};
dojo.html.hasClass=function(_5,_6){
return (new RegExp("(^|\\s+)"+_6+"(\\s+|$)")).test(dojo.html.getClass(_5));
};
dojo.html.prependClass=function(_7,_8){
_8+=" "+dojo.html.getClass(_7);
return dojo.html.setClass(_7,_8);
};
dojo.html.addClass=function(_9,_a){
if(dojo.html.hasClass(_9,_a)){
return false;
}
_a=(dojo.html.getClass(_9)+" "+_a).replace(/^\s+|\s+$/g,"");
return dojo.html.setClass(_9,_a);
};
dojo.html.setClass=function(_b,_c){
_b=dojo.byId(_b);
var cs=new String(_c);
try{
if(typeof _b.className=="string"){
_b.className=cs;
}else{
if(_b.setAttribute){
_b.setAttribute("class",_c);
_b.className=cs;
}else{
return false;
}
}
}
catch(e){
dojo.debug("dojo.html.setClass() failed",e);
}
return true;
};
dojo.html.removeClass=function(_e,_f,_10){
try{
if(!_10){
var _11=dojo.html.getClass(_e).replace(new RegExp("(^|\\s+)"+_f+"(\\s+|$)"),"$1$2");
}else{
var _11=dojo.html.getClass(_e).replace(_f,"");
}
dojo.html.setClass(_e,_11);
}
catch(e){
dojo.debug("dojo.html.removeClass() failed",e);
}
return true;
};
dojo.html.replaceClass=function(_12,_13,_14){
dojo.html.removeClass(_12,_14);
dojo.html.addClass(_12,_13);
};
dojo.html.classMatchType={ContainsAll:0,ContainsAny:1,IsOnly:2};
dojo.html.getElementsByClass=function(_15,_16,_17,_18,_19){
_19=false;
var _1a=dojo.doc();
_16=dojo.byId(_16)||_1a;
var _1b=_15.split(/\s+/g);
var _1c=[];
if(_18!=1&&_18!=2){
_18=0;
}
var _1d=new RegExp("(\\s|^)(("+_1b.join(")|(")+"))(\\s|$)");
var _1e=_1b.join(" ").length;
var _1f=[];
if(!_19&&_1a.evaluate){
var _20=".//"+(_17||"*")+"[contains(";
if(_18!=dojo.html.classMatchType.ContainsAny){
_20+="concat(' ',@class,' '), ' "+_1b.join(" ') and contains(concat(' ',@class,' '), ' ")+" ')";
if(_18==2){
_20+=" and string-length(@class)="+_1e+"]";
}else{
_20+="]";
}
}else{
_20+="concat(' ',@class,' '), ' "+_1b.join(" ') or contains(concat(' ',@class,' '), ' ")+" ')]";
}
var _21=_1a.evaluate(_20,_16,null,XPathResult.ANY_TYPE,null);
var _22=_21.iterateNext();
while(_22){
try{
_1f.push(_22);
_22=_21.iterateNext();
}
catch(e){
break;
}
}
return _1f;
}else{
if(!_17){
_17="*";
}
_1f=_16.getElementsByTagName(_17);
var _23,i=0;
outer:
while(_23=_1f[i++]){
var _25=dojo.html.getClasses(_23);
if(_25.length==0){
continue outer;
}
var _26=0;
for(var j=0;j<_25.length;j++){
if(_1d.test(_25[j])){
if(_18==dojo.html.classMatchType.ContainsAny){
_1c.push(_23);
continue outer;
}else{
_26++;
}
}else{
if(_18==dojo.html.classMatchType.IsOnly){
continue outer;
}
}
}
if(_26==_1b.length){
if((_18==dojo.html.classMatchType.IsOnly)&&(_26==_25.length)){
_1c.push(_23);
}else{
if(_18==dojo.html.classMatchType.ContainsAll){
_1c.push(_23);
}
}
}
}
return _1c;
}
};
dojo.html.getElementsByClassName=dojo.html.getElementsByClass;
dojo.html.toCamelCase=function(_28){
var arr=_28.split("-"),cc=arr[0];
for(var i=1;i<arr.length;i++){
cc+=arr[i].charAt(0).toUpperCase()+arr[i].substring(1);
}
return cc;
};
dojo.html.toSelectorCase=function(_2c){
return _2c.replace(/([A-Z])/g,"-$1").toLowerCase();
};
if(dojo.render.html.ie){
dojo.html.getComputedStyle=function(_2d,_2e,_2f){
_2d=dojo.byId(_2d);
if(!_2d||!_2d.currentStyle){
return _2f;
}
return _2d.currentStyle[dojo.html.toCamelCase(_2e)];
};
dojo.html.getComputedStyles=function(_30){
return _30.currentStyle;
};
}else{
dojo.html.getComputedStyle=function(_31,_32,_33){
_31=dojo.byId(_31);
if(!_31||!_31.style){
return _33;
}
var s=document.defaultView.getComputedStyle(_31,null);
return (s&&s[dojo.html.toCamelCase(_32)])||"";
};
dojo.html.getComputedStyles=function(_35){
return document.defaultView.getComputedStyle(_35,null);
};
}
dojo.html.getStyleProperty=function(_36,_37){
_36=dojo.byId(_36);
return (_36&&_36.style?_36.style[dojo.html.toCamelCase(_37)]:undefined);
};
dojo.html.getStyle=function(_38,_39){
var _3a=dojo.html.getStyleProperty(_38,_39);
return (_3a?_3a:dojo.html.getComputedStyle(_38,_39));
};
dojo.html.setStyle=function(_3b,_3c,_3d){
_3b=dojo.byId(_3b);
if(_3b&&_3b.style){
var _3e=dojo.html.toCamelCase(_3c);
_3b.style[_3e]=_3d;
}
};
dojo.html.setStyleText=function(_3f,_40){
try{
_3f.style.cssText=_40;
}
catch(e){
_3f.setAttribute("style",_40);
}
};
dojo.html.copyStyle=function(_41,_42){
if(!_42.style.cssText){
_41.setAttribute("style",_42.getAttribute("style"));
}else{
_41.style.cssText=_42.style.cssText;
}
dojo.html.addClass(_41,dojo.html.getClass(_42));
};
dojo.html.getUnitValue=function(_43,_44,_45){
var s=dojo.html.getComputedStyle(_43,_44);
if((!s)||((s=="auto")&&(_45))){
return {value:0,units:"px"};
}
var _47=s.match(/(\-?[\d.]+)([a-z%]*)/i);
if(!_47){
return dojo.html.getUnitValue.bad;
}
return {value:Number(_47[1]),units:_47[2].toLowerCase()};
};
dojo.html.getUnitValue.bad={value:NaN,units:""};
if(dojo.render.html.ie){
dojo.html.toPixelValue=function(_48,_49){
if(!_49){
return 0;
}
if(_49.slice(-2)=="px"){
return parseFloat(_49);
}
var _4a=0;
with(_48){
var _4b=style.left;
var _4c=runtimeStyle.left;
runtimeStyle.left=currentStyle.left;
try{
style.left=_49||0;
_4a=style.pixelLeft;
style.left=_4b;
runtimeStyle.left=_4c;
}
catch(e){
}
}
return _4a;
};
}else{
dojo.html.toPixelValue=function(_4d,_4e){
return (_4e&&(_4e.slice(-2)=="px")?parseFloat(_4e):0);
};
}
dojo.html.getPixelValue=function(_4f,_50,_51){
return dojo.html.toPixelValue(_4f,dojo.html.getComputedStyle(_4f,_50));
};
dojo.html.setPositivePixelValue=function(_52,_53,_54){
if(isNaN(_54)){
return false;
}
_52.style[_53]=Math.max(0,_54)+"px";
return true;
};
dojo.html.styleSheet=null;
dojo.html.insertCssRule=function(_55,_56,_57){
if(!dojo.html.styleSheet){
if(document.createStyleSheet){
dojo.html.styleSheet=document.createStyleSheet();
}else{
if(document.styleSheets[0]){
dojo.html.styleSheet=document.styleSheets[0];
}else{
return null;
}
}
}
if(arguments.length<3){
if(dojo.html.styleSheet.cssRules){
_57=dojo.html.styleSheet.cssRules.length;
}else{
if(dojo.html.styleSheet.rules){
_57=dojo.html.styleSheet.rules.length;
}else{
return null;
}
}
}
if(dojo.html.styleSheet.insertRule){
var _58=_55+" { "+_56+" }";
return dojo.html.styleSheet.insertRule(_58,_57);
}else{
if(dojo.html.styleSheet.addRule){
return dojo.html.styleSheet.addRule(_55,_56,_57);
}else{
return null;
}
}
};
dojo.html.removeCssRule=function(_59){
if(!dojo.html.styleSheet){
dojo.debug("no stylesheet defined for removing rules");
return false;
}
if(dojo.render.html.ie){
if(!_59){
_59=dojo.html.styleSheet.rules.length;
dojo.html.styleSheet.removeRule(_59);
}
}else{
if(document.styleSheets[0]){
if(!_59){
_59=dojo.html.styleSheet.cssRules.length;
}
dojo.html.styleSheet.deleteRule(_59);
}
}
return true;
};
dojo.html._insertedCssFiles=[];
dojo.html.insertCssFile=function(URI,doc,_5c,_5d){
if(!URI){
return;
}
if(!doc){
doc=document;
}
var _5e=dojo.hostenv.getText(URI,false,_5d);
if(_5e===null){
return;
}
_5e=dojo.html.fixPathsInCssText(_5e,URI);
if(_5c){
var idx=-1,_60,ent=dojo.html._insertedCssFiles;
for(var i=0;i<ent.length;i++){
if((ent[i].doc==doc)&&(ent[i].cssText==_5e)){
idx=i;
_60=ent[i].nodeRef;
break;
}
}
if(_60){
var _63=doc.getElementsByTagName("style");
for(var i=0;i<_63.length;i++){
if(_63[i]==_60){
return;
}
}
dojo.html._insertedCssFiles.shift(idx,1);
}
}
var _64=dojo.html.insertCssText(_5e,doc);
dojo.html._insertedCssFiles.push({"doc":doc,"cssText":_5e,"nodeRef":_64});
if(_64&&djConfig.isDebug){
_64.setAttribute("dbgHref",URI);
}
return _64;
};
dojo.html.insertCssText=function(_65,doc,URI){
if(!_65){
return;
}
if(!doc){
doc=document;
}
if(URI){
_65=dojo.html.fixPathsInCssText(_65,URI);
}
var _68=doc.createElement("style");
_68.setAttribute("type","text/css");
var _69=doc.getElementsByTagName("head")[0];
if(!_69){
dojo.debug("No head tag in document, aborting styles");
return;
}else{
_69.appendChild(_68);
}
if(_68.styleSheet){
var _6a=function(){
try{
_68.styleSheet.cssText=_65;
}
catch(e){
dojo.debug(e);
}
};
if(_68.styleSheet.disabled){
setTimeout(_6a,10);
}else{
_6a();
}
}else{
var _6b=doc.createTextNode(_65);
_68.appendChild(_6b);
}
return _68;
};
dojo.html.fixPathsInCssText=function(_6c,URI){
if(!_6c||!URI){
return;
}
var _6e,str="",url="",_71="[\\t\\s\\w\\(\\)\\/\\.\\\\'\"-:#=&?~]+";
var _72=new RegExp("url\\(\\s*("+_71+")\\s*\\)");
var _73=/(file|https?|ftps?):\/\//;
regexTrim=new RegExp("^[\\s]*(['\"]?)("+_71+")\\1[\\s]*?$");
if(dojo.render.html.ie55||dojo.render.html.ie60){
var _74=new RegExp("AlphaImageLoader\\((.*)src=['\"]("+_71+")['\"]");
while(_6e=_74.exec(_6c)){
url=_6e[2].replace(regexTrim,"$2");
if(!_73.exec(url)){
url=(new dojo.uri.Uri(URI,url).toString());
}
str+=_6c.substring(0,_6e.index)+"AlphaImageLoader("+_6e[1]+"src='"+url+"'";
_6c=_6c.substr(_6e.index+_6e[0].length);
}
_6c=str+_6c;
str="";
}
while(_6e=_72.exec(_6c)){
url=_6e[1].replace(regexTrim,"$2");
if(!_73.exec(url)){
url=(new dojo.uri.Uri(URI,url).toString());
}
str+=_6c.substring(0,_6e.index)+"url("+url+")";
_6c=_6c.substr(_6e.index+_6e[0].length);
}
return str+_6c;
};
dojo.html.setActiveStyleSheet=function(_75){
var i=0,a,els=dojo.doc().getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")){
a.disabled=true;
if(a.getAttribute("title")==_75){
a.disabled=false;
}
}
}
};
dojo.html.getActiveStyleSheet=function(){
var i=0,a,els=dojo.doc().getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")&&!a.disabled){
return a.getAttribute("title");
}
}
return null;
};
dojo.html.getPreferredStyleSheet=function(){
var i=0,a,els=dojo.doc().getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("rel").indexOf("alt")==-1&&a.getAttribute("title")){
return a.getAttribute("title");
}
}
return null;
};
dojo.html.applyBrowserClass=function(_7f){
var drh=dojo.render.html;
var _81={dj_ie:drh.ie,dj_ie55:drh.ie55,dj_ie6:drh.ie60,dj_ie7:drh.ie70,dj_iequirks:drh.ie&&drh.quirks,dj_opera:drh.opera,dj_opera8:drh.opera&&(Math.floor(dojo.render.version)==8),dj_opera9:drh.opera&&(Math.floor(dojo.render.version)==9),dj_khtml:drh.khtml,dj_safari:drh.safari,dj_gecko:drh.mozilla};
for(var p in _81){
if(_81[p]){
dojo.html.addClass(_7f,p);
}
}
};
