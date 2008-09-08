dojo.provide("dojo.widget.RichText");
dojo.require("dojo.widget.*");
dojo.require("dojo.html.*");
dojo.require("dojo.html.layout");
dojo.require("dojo.html.selection");
dojo.require("dojo.event.*");
dojo.require("dojo.string.extras");
dojo.require("dojo.uri.Uri");
dojo.require("dojo.Deferred");
if(!djConfig["useXDomain"]||djConfig["allowXdRichTextSave"]){
if(dojo.hostenv.post_load_){
(function(){
var _1=dojo.doc().createElement("textarea");
_1.id="dojo.widget.RichText.savedContent";
_1.style="display:none;position:absolute;top:-100px;left:-100px;height:3px;width:3px;overflow:hidden;";
dojo.body().appendChild(_1);
})();
}else{
try{
dojo.doc().write("<textarea id=\"dojo.widget.RichText.savedContent\" "+"style=\"display:none;position:absolute;top:-100px;left:-100px;height:3px;width:3px;overflow:hidden;\"></textarea>");
}
catch(e){
}
}
}
dojo.widget.defineWidget("dojo.widget.RichText",dojo.widget.HtmlWidget,function(){
this.contentPreFilters=[];
this.contentPostFilters=[];
this.contentDomPreFilters=[];
this.contentDomPostFilters=[];
this.editingAreaStyleSheets=[];
if(dojo.render.html.moz){
this.contentPreFilters.push(this._fixContentForMoz);
}
this._keyHandlers={};
if(dojo.Deferred){
this.onLoadDeferred=new dojo.Deferred();
}
},{inheritWidth:false,focusOnLoad:false,saveName:"",styleSheets:"",_content:"",height:"",minHeight:"1em",isClosed:true,isLoaded:false,useActiveX:false,relativeImageUrls:false,_SEPARATOR:"@@**%%__RICHTEXTBOUNDRY__%%**@@",onLoadDeferred:null,fillInTemplate:function(){
dojo.event.topic.publish("dojo.widget.RichText::init",this);
this.open();
dojo.event.connect(this,"onKeyPressed",this,"afterKeyPress");
dojo.event.connect(this,"onKeyPress",this,"keyPress");
dojo.event.connect(this,"onKeyDown",this,"keyDown");
dojo.event.connect(this,"onKeyUp",this,"keyUp");
this.setupDefaultShortcuts();
},setupDefaultShortcuts:function(){
var _2=this.KEY_CTRL;
var _3=function(_4,_5){
return arguments.length==1?function(){
this.execCommand(_4);
}:function(){
this.execCommand(_4,_5);
};
};
this.addKeyHandler("b",_2,_3("bold"));
this.addKeyHandler("i",_2,_3("italic"));
this.addKeyHandler("u",_2,_3("underline"));
this.addKeyHandler("a",_2,_3("selectall"));
this.addKeyHandler("s",_2,function(){
this.save(true);
});
this.addKeyHandler("1",_2,_3("formatblock","h1"));
this.addKeyHandler("2",_2,_3("formatblock","h2"));
this.addKeyHandler("3",_2,_3("formatblock","h3"));
this.addKeyHandler("4",_2,_3("formatblock","h4"));
this.addKeyHandler("\\",_2,_3("insertunorderedlist"));
if(!dojo.render.html.ie){
this.addKeyHandler("Z",_2,_3("redo"));
}
},events:["onBlur","onFocus","onKeyPress","onKeyDown","onKeyUp","onClick"],open:function(_6){
if(this.onLoadDeferred.fired>=0){
this.onLoadDeferred=new dojo.Deferred();
}
var h=dojo.render.html;
if(!this.isClosed){
this.close();
}
dojo.event.topic.publish("dojo.widget.RichText::open",this);
this._content="";
if((arguments.length==1)&&(_6["nodeName"])){
this.domNode=_6;
}
if((this.domNode["nodeName"])&&(this.domNode.nodeName.toLowerCase()=="textarea")){
this.textarea=this.domNode;
var _8=this._preFilterContent(this.textarea.value);
this.domNode=dojo.doc().createElement("div");
dojo.html.copyStyle(this.domNode,this.textarea);
var _9=dojo.lang.hitch(this,function(){
with(this.textarea.style){
display="block";
position="absolute";
left=top="-1000px";
if(h.ie){
this.__overflow=overflow;
overflow="hidden";
}
}
});
if(h.ie){
setTimeout(_9,10);
}else{
_9();
}
if(!h.safari){
dojo.html.insertBefore(this.domNode,this.textarea);
}
if(this.textarea.form){
dojo.event.connect("before",this.textarea.form,"onsubmit",dojo.lang.hitch(this,function(){
this.textarea.value=this.getEditorContent();
}));
}
var _a=this;
dojo.event.connect(this,"postCreate",function(){
dojo.html.insertAfter(_a.textarea,_a.domNode);
});
}else{
var _8=this._preFilterContent(dojo.string.trim(this.domNode.innerHTML));
}
if(_8==""){
_8="&nbsp;";
}
var _b=dojo.html.getContentBox(this.domNode);
this._oldHeight=_b.height;
this._oldWidth=_b.width;
this._firstChildContributingMargin=this._getContributingMargin(this.domNode,"top");
this._lastChildContributingMargin=this._getContributingMargin(this.domNode,"bottom");
this.savedContent=_8;
this.domNode.innerHTML="";
this.editingArea=dojo.doc().createElement("div");
this.domNode.appendChild(this.editingArea);
if((this.domNode["nodeName"])&&(this.domNode.nodeName=="LI")){
this.domNode.innerHTML=" <br>";
}
if(this.saveName!=""&&(!djConfig["useXDomain"]||djConfig["allowXdRichTextSave"])){
var _c=dojo.doc().getElementById("dojo.widget.RichText.savedContent");
if(_c.value!=""){
var _d=_c.value.split(this._SEPARATOR);
for(var i=0;i<_d.length;i++){
var _f=_d[i].split(":");
if(_f[0]==this.saveName){
_8=_f[1];
_d.splice(i,1);
break;
}
}
}
dojo.event.connect("before",window,"onunload",this,"_saveContent");
}
if(h.ie70&&this.useActiveX){
dojo.debug("activeX in ie70 is not currently supported, useActiveX is ignored for now.");
this.useActiveX=false;
}
if(this.useActiveX&&h.ie){
var _10=this;
setTimeout(function(){
_10._drawObject(_8);
},0);
}else{
if(h.ie||this._safariIsLeopard()||h.opera){
this.iframe=dojo.doc().createElement("iframe");
this.iframe.src="javascript:void(0)";
this.editorObject=this.iframe;
with(this.iframe.style){
border="0";
width="100%";
}
this.iframe.frameBorder=0;
this.editingArea.appendChild(this.iframe);
this.window=this.iframe.contentWindow;
this.document=this.window.document;
this.document.open();
this.document.write("<html><head><style>body{margin:0;padding:0;border:0;overflow:hidden;}</style></head><body><div></div></body></html>");
this.document.close();
this.editNode=this.document.body.firstChild;
this.editNode.contentEditable=true;
with(this.iframe.style){
if(h.ie70){
if(this.height){
height=this.height;
}
if(this.minHeight){
minHeight=this.minHeight;
}
}else{
height=this.height?this.height:this.minHeight;
}
}
var _11=["p","pre","address","h1","h2","h3","h4","h5","h6","ol","div","ul"];
var _12="";
for(var i in _11){
if(_11[i].charAt(1)!="l"){
_12+="<"+_11[i]+"><span>content</span></"+_11[i]+">";
}else{
_12+="<"+_11[i]+"><li>content</li></"+_11[i]+">";
}
}
with(this.editNode.style){
position="absolute";
left="-2000px";
top="-2000px";
}
this.editNode.innerHTML=_12;
var _13=this.editNode.firstChild;
while(_13){
dojo.withGlobal(this.window,"selectElement",dojo.html.selection,[_13.firstChild]);
var _14=_13.tagName.toLowerCase();
this._local2NativeFormatNames[_14]=this.queryCommandValue("formatblock");
this._native2LocalFormatNames[this._local2NativeFormatNames[_14]]=_14;
_13=_13.nextSibling;
}
with(this.editNode.style){
position="";
left="";
top="";
}
this.editNode.innerHTML=_8;
if(this.height){
this.document.body.style.overflowY="scroll";
}
dojo.lang.forEach(this.events,function(e){
dojo.event.connect(this.editNode,e.toLowerCase(),this,e);
},this);
this.onLoad();
}else{
this._drawIframe(_8);
this.editorObject=this.iframe;
}
}
if(this.domNode.nodeName=="LI"){
this.domNode.lastChild.style.marginTop="-1.2em";
}
dojo.html.addClass(this.domNode,"RichTextEditable");
this.isClosed=false;
},_hasCollapseableMargin:function(_16,_17){
if(dojo.html.getPixelValue(_16,"border-"+_17+"-width",false)){
return false;
}else{
if(dojo.html.getPixelValue(_16,"padding-"+_17,false)){
return false;
}else{
return true;
}
}
},_getContributingMargin:function(_18,_19){
if(_19=="top"){
var _1a="previousSibling";
var _1b="nextSibling";
var _1c="firstChild";
var _1d="margin-top";
var _1e="margin-bottom";
}else{
var _1a="nextSibling";
var _1b="previousSibling";
var _1c="lastChild";
var _1d="margin-bottom";
var _1e="margin-top";
}
var _1f=dojo.html.getPixelValue(_18,_1d,false);
function isSignificantNode(_20){
return !(_20.nodeType==3&&dojo.string.isBlank(_20.data))&&dojo.html.getStyle(_20,"display")!="none"&&!dojo.html.isPositionAbsolute(_20);
}
var _21=0;
var _22=_18[_1c];
while(_22){
while((!isSignificantNode(_22))&&_22[_1b]){
_22=_22[_1b];
}
_21=Math.max(_21,dojo.html.getPixelValue(_22,_1d,false));
if(!this._hasCollapseableMargin(_22,_19)){
break;
}
_22=_22[_1c];
}
if(!this._hasCollapseableMargin(_18,_19)){
return parseInt(_21);
}
var _23=0;
var _24=_18[_1a];
while(_24){
if(isSignificantNode(_24)){
_23=dojo.html.getPixelValue(_24,_1e,false);
break;
}
_24=_24[_1a];
}
if(!_24){
_23=dojo.html.getPixelValue(_18.parentNode,_1d,false);
}
if(_21>_1f){
return parseInt(Math.max((_21-_1f)-_23,0));
}else{
return 0;
}
},_drawIframe:function(_25){
var _26=Boolean(dojo.render.html.moz&&(typeof window.XML=="undefined"));
if(!this.iframe){
var _27=(new dojo.uri.Uri(dojo.doc().location)).host;
this.iframe=dojo.doc().createElement("iframe");
with(this.iframe){
style.border="none";
style.lineHeight="0";
style.verticalAlign="bottom";
scrolling=this.height?"auto":"no";
}
}
if(djConfig["useXDomain"]&&!djConfig["dojoRichTextFrameUrl"]){
dojo.debug("dojo.widget.RichText: When using cross-domain Dojo builds,"+" please save src/widget/templates/richtextframe.html to your domain and set djConfig.dojoRichTextFrameUrl"+" to the path on your domain to richtextframe.html");
}
this.iframe.src=(djConfig["dojoRichTextFrameUrl"]||dojo.uri.moduleUri("dojo.widget","templates/richtextframe.html"))+((dojo.doc().domain!=_27)?("#"+dojo.doc().domain):"");
this.iframe.width=this.inheritWidth?this._oldWidth:"100%";
if(this.height){
this.iframe.style.height=this.height;
}else{
var _28=this._oldHeight;
if(this._hasCollapseableMargin(this.domNode,"top")){
_28+=this._firstChildContributingMargin;
}
if(this._hasCollapseableMargin(this.domNode,"bottom")){
_28+=this._lastChildContributingMargin;
}
this.iframe.height=_28;
}
var _29=dojo.doc().createElement("div");
_29.innerHTML=_25;
this.editingArea.appendChild(_29);
if(this.relativeImageUrls){
var _2a=_29.getElementsByTagName("img");
for(var i=0;i<_2a.length;i++){
_2a[i].src=(new dojo.uri.Uri(dojo.global().location,_2a[i].src)).toString();
}
_25=_29.innerHTML;
}
var _2c=dojo.html.firstElement(_29);
var _2d=dojo.html.lastElement(_29);
if(_2c){
_2c.style.marginTop=this._firstChildContributingMargin+"px";
}
if(_2d){
_2d.style.marginBottom=this._lastChildContributingMargin+"px";
}
this.editingArea.appendChild(this.iframe);
if(dojo.render.html.safari){
this.iframe.src=this.iframe.src;
}
var _2e=false;
var _2f=dojo.lang.hitch(this,function(){
if(!_2e){
_2e=true;
}else{
return;
}
if(!this.editNode){
if(this.iframe.contentWindow){
this.window=this.iframe.contentWindow;
this.document=this.iframe.contentWindow.document;
}else{
if(this.iframe.contentDocument){
this.window=this.iframe.contentDocument.window;
this.document=this.iframe.contentDocument;
}
}
var _30=(function(_31){
return function(_32){
return dojo.html.getStyle(_31,_32);
};
})(this.domNode);
var _33=_30("font-weight")+" "+_30("font-size")+" "+_30("font-family");
var _34="1.0";
var _35=dojo.html.getUnitValue(this.domNode,"line-height");
if(_35.value&&_35.units==""){
_34=_35.value;
}
dojo.html.insertCssText("body,html{background:transparent;padding:0;margin:0;}"+"body{top:0;left:0;right:0;"+(((this.height)||(dojo.render.html.opera))?"":"position:fixed;")+"font:"+_33+";"+"min-height:"+this.minHeight+";"+"line-height:"+_34+"}"+"p{margin: 1em 0 !important;}"+"body > *:first-child{padding-top:0 !important;margin-top:"+this._firstChildContributingMargin+"px !important;}"+"body > *:last-child{padding-bottom:0 !important;margin-bottom:"+this._lastChildContributingMargin+"px !important;}"+"li > ul:-moz-first-node, li > ol:-moz-first-node{padding-top:1.2em;}\n"+"li{min-height:1.2em;}"+"",this.document);
dojo.html.removeNode(_29);
this.document.body.innerHTML=_25;
if(_26||dojo.render.html.safari){
this.document.designMode="on";
}
this.onLoad();
}else{
dojo.html.removeNode(_29);
this.editNode.innerHTML=_25;
this.onDisplayChanged();
}
});
if(this.editNode){
_2f();
}else{
if(dojo.render.html.moz){
this.iframe.onload=function(){
setTimeout(_2f,250);
};
}else{
this.iframe.onload=_2f;
}
}
},_applyEditingAreaStyleSheets:function(){
var _36=[];
if(this.styleSheets){
_36=this.styleSheets.split(";");
this.styleSheets="";
}
_36=_36.concat(this.editingAreaStyleSheets);
this.editingAreaStyleSheets=[];
if(_36.length>0){
for(var i=0;i<_36.length;i++){
var url=_36[i];
if(url){
this.addStyleSheet(dojo.uri.dojoUri(url));
}
}
}
},addStyleSheet:function(uri){
var url=uri.toString();
if(dojo.lang.find(this.editingAreaStyleSheets,url)>-1){
dojo.debug("dojo.widget.RichText.addStyleSheet: Style sheet "+url+" is already applied to the editing area!");
return;
}
if(url.charAt(0)=="."||(url.charAt(0)!="/"&&!uri.host)){
url=(new dojo.uri.Uri(dojo.global().location,url)).toString();
}
this.editingAreaStyleSheets.push(url);
if(this.document.createStyleSheet){
this.document.createStyleSheet(url);
}else{
var _3b=this.document.getElementsByTagName("head")[0];
var _3c=this.document.createElement("link");
with(_3c){
rel="stylesheet";
type="text/css";
href=url;
}
_3b.appendChild(_3c);
}
},removeStyleSheet:function(uri){
var url=uri.toString();
if(url.charAt(0)=="."||(url.charAt(0)!="/"&&!uri.host)){
url=(new dojo.uri.Uri(dojo.global().location,url)).toString();
}
var _3f=dojo.lang.find(this.editingAreaStyleSheets,url);
if(_3f==-1){
dojo.debug("dojo.widget.RichText.removeStyleSheet: Style sheet "+url+" is not applied to the editing area so it can not be removed!");
return;
}
delete this.editingAreaStyleSheets[_3f];
var _40=this.document.getElementsByTagName("link");
for(var i=0;i<_40.length;i++){
if(_40[i].href==url){
if(dojo.render.html.ie){
_40[i].href="";
}
dojo.html.removeNode(_40[i]);
break;
}
}
},_drawObject:function(_42){
this.object=dojo.html.createExternalElement(dojo.doc(),"object");
with(this.object){
classid="clsid:2D360201-FFF5-11D1-8D03-00A0C959BC0A";
width=this.inheritWidth?this._oldWidth:"100%";
style.height=this.height?this.height:(this._oldHeight+"px");
Scrollbars=this.height?true:false;
Appearance=this._activeX.appearance.flat;
}
this.editorObject=this.object;
this.editingArea.appendChild(this.object);
this.object.attachEvent("DocumentComplete",dojo.lang.hitch(this,"onLoad"));
dojo.lang.forEach(this.events,function(e){
this.object.attachEvent(e.toLowerCase(),dojo.lang.hitch(this,e));
},this);
this.object.DocumentHTML="<!doctype HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">"+"<html><title></title>"+"<style type=\"text/css\">"+"    body,html { padding: 0; margin: 0; }"+(this.height?"":"    body,  { overflow: hidden; }")+"</style>"+"<body><div>"+_42+"<div></body></html>";
this._cacheLocalBlockFormatNames();
},_local2NativeFormatNames:{},_native2LocalFormatNames:{},_cacheLocalBlockFormatNames:function(){
if(!this._native2LocalFormatNames["p"]){
var obj=this.object;
var _45=false;
if(!obj){
try{
obj=dojo.html.createExternalElement(dojo.doc(),"object");
obj.classid="clsid:2D360201-FFF5-11D1-8D03-00A0C959BC0A";
dojo.body().appendChild(obj);
obj.DocumentHTML="<html><head></head><body></body></html>";
}
catch(e){
_45=true;
}
}
try{
var _46=new ActiveXObject("DEGetBlockFmtNamesParam.DEGetBlockFmtNamesParam");
obj.ExecCommand(this._activeX.command["getblockformatnames"],0,_46);
var _47=new VBArray(_46.Names);
var _48=_47.toArray();
var _49=["p","pre","address","h1","h2","h3","h4","h5","h6","ol","ul","","","","","div"];
for(var i=0;i<_49.length;++i){
if(_49[i].length>0){
this._local2NativeFormatNames[_48[i]]=_49[i];
this._native2LocalFormatNames[_49[i]]=_48[i];
}
}
}
catch(e){
_45=true;
}
if(obj&&!this.object){
dojo.body().removeChild(obj);
}
}
return !_45;
},_isResized:function(){
return false;
},onLoad:function(e){
this.isLoaded=true;
if(this.object){
this.document=this.object.DOM;
this.window=this.document.parentWindow;
this.editNode=this.document.body.firstChild;
this.editingArea.style.height=this.height?this.height:this.minHeight;
if(!this.height){
this.connect(this,"onDisplayChanged","_updateHeight");
}
this.window._frameElement=this.object;
}else{
if(this.iframe&&!dojo.render.html.ie){
this.editNode=this.document.body;
if(!this.height){
this.connect(this,"onDisplayChanged","_updateHeight");
}
try{
this.document.execCommand("useCSS",false,true);
this.document.execCommand("styleWithCSS",false,false);
}
catch(e2){
}
if(dojo.render.html.safari){
this.connect(this.editNode,"onblur","onBlur");
this.connect(this.editNode,"onfocus","onFocus");
this.connect(this.editNode,"onclick","onFocus");
this.interval=setInterval(dojo.lang.hitch(this,"onDisplayChanged"),750);
}else{
if(dojo.render.html.mozilla||dojo.render.html.opera){
var doc=this.document;
var _4d=dojo.event.browser.addListener;
var _4e=this;
dojo.lang.forEach(this.events,function(e){
var l=_4d(_4e.document,e.substr(2).toLowerCase(),dojo.lang.hitch(_4e,e));
if(e=="onBlur"){
var _51={unBlur:function(e){
dojo.event.browser.removeListener(doc,"blur",l);
}};
dojo.event.connect("before",_4e,"close",_51,"unBlur");
}
});
}
}
}else{
if(dojo.render.html.ie){
if(!this.height){
this.connect(this,"onDisplayChanged","_updateHeight");
}
this.editNode.style.zoom=1;
}
}
}
this._applyEditingAreaStyleSheets();
if(this.focusOnLoad){
this.focus();
}
this.onDisplayChanged(e);
if(this.onLoadDeferred){
this.onLoadDeferred.callback(true);
}
},onKeyDown:function(e){
if((!e)&&(this.object)){
e=dojo.event.browser.fixEvent(this.window.event);
}
if((dojo.render.html.ie)&&(e.keyCode==e.KEY_TAB)){
e.preventDefault();
e.stopPropagation();
this.execCommand((e.shiftKey?"outdent":"indent"));
}else{
if(dojo.render.html.ie){
if((65<=e.keyCode)&&(e.keyCode<=90)){
e.charCode=e.keyCode;
this.onKeyPress(e);
}
}
}
},onKeyUp:function(e){
return;
},KEY_CTRL:1,onKeyPress:function(e){
if((!e)&&(this.object)){
e=dojo.event.browser.fixEvent(this.window.event);
}
var _56=e.ctrlKey?this.KEY_CTRL:0;
if(this._keyHandlers[e.key]){
var _57=this._keyHandlers[e.key],i=0,_59;
while(_59=_57[i++]){
if(_56==_59.modifiers){
e.preventDefault();
_59.handler.call(this);
break;
}
}
}
dojo.lang.setTimeout(this,this.onKeyPressed,1,e);
},addKeyHandler:function(key,_5b,_5c){
if(!(this._keyHandlers[key] instanceof Array)){
this._keyHandlers[key]=[];
}
this._keyHandlers[key].push({modifiers:_5b||0,handler:_5c});
},onKeyPressed:function(e){
this.onDisplayChanged();
},onClick:function(e){
this.onDisplayChanged(e);
},onBlur:function(e){
},_initialFocus:true,onFocus:function(e){
if((dojo.render.html.mozilla)&&(this._initialFocus)){
this._initialFocus=false;
if(dojo.string.trim(this.editNode.innerHTML)=="&nbsp;"){
this.placeCursorAtStart();
}
}
},blur:function(){
if(this.iframe){
this.window.blur();
}else{
if(this.object){
this.document.body.blur();
}else{
if(this.editNode){
this.editNode.blur();
}
}
}
},focus:function(){
if(this.iframe&&!dojo.render.html.ie){
this.window.focus();
}else{
if(this.object){
this.document.focus();
}else{
if(this.editNode&&this.editNode.focus){
this.editNode.focus();
}else{
dojo.debug("Have no idea how to focus into the editor!");
}
}
}
},onDisplayChanged:function(e){
},_activeX:{command:{bold:5000,italic:5023,underline:5048,justifycenter:5024,justifyleft:5025,justifyright:5026,cut:5003,copy:5002,paste:5032,"delete":5004,undo:5049,redo:5033,removeformat:5034,selectall:5035,unlink:5050,indent:5018,outdent:5031,insertorderedlist:5030,insertunorderedlist:5051,inserttable:5022,insertcell:5019,insertcol:5020,insertrow:5021,deletecells:5005,deletecols:5006,deleterows:5007,mergecells:5029,splitcell:5047,setblockformat:5043,getblockformat:5011,getblockformatnames:5012,setfontname:5044,getfontname:5013,setfontsize:5045,getfontsize:5014,setbackcolor:5042,getbackcolor:5010,setforecolor:5046,getforecolor:5015,findtext:5008,font:5009,hyperlink:5016,image:5017,lockelement:5027,makeabsolute:5028,sendbackward:5036,bringforward:5037,sendbelowtext:5038,bringabovetext:5039,sendtoback:5040,bringtofront:5041,properties:5052},ui:{"default":0,prompt:1,noprompt:2},status:{notsupported:0,disabled:1,enabled:3,latched:7,ninched:11},appearance:{flat:0,inset:1},state:{unchecked:0,checked:1,gray:2}},_normalizeCommand:function(cmd){
var drh=dojo.render.html;
var _64=cmd.toLowerCase();
if(_64=="formatblock"){
if(drh.safari){
_64="heading";
}
}else{
if(this.object){
switch(_64){
case "createlink":
_64="hyperlink";
break;
case "insertimage":
_64="image";
break;
}
}else{
if(_64=="hilitecolor"&&!drh.mozilla){
_64="backcolor";
}
}
}
return _64;
},_safariIsLeopard:function(){
var _65=false;
if(dojo.render.html.safari){
var tmp=dojo.render.html.UA.split("AppleWebKit/")[1];
var ver=parseFloat(tmp.split(" ")[0]);
if(ver>=420){
_65=true;
}
}
return _65;
},queryCommandAvailable:function(_68){
var ie=1;
var _6a=1<<1;
var _6b=1<<2;
var _6c=1<<3;
var _6d=1<<4;
var _6e=this._safariIsLeopard();
function isSupportedBy(_6f){
return {ie:Boolean(_6f&ie),mozilla:Boolean(_6f&_6a),safari:Boolean(_6f&_6b),safari420:Boolean(_6f&_6d),opera:Boolean(_6f&_6c)};
}
var _70=null;
switch(_68.toLowerCase()){
case "bold":
case "italic":
case "underline":
case "subscript":
case "superscript":
case "fontname":
case "fontsize":
case "forecolor":
case "hilitecolor":
case "justifycenter":
case "justifyfull":
case "justifyleft":
case "justifyright":
case "delete":
case "selectall":
_70=isSupportedBy(_6a|ie|_6b|_6c);
break;
case "createlink":
case "unlink":
case "removeformat":
case "inserthorizontalrule":
case "insertimage":
case "insertorderedlist":
case "insertunorderedlist":
case "indent":
case "outdent":
case "formatblock":
case "inserthtml":
case "undo":
case "redo":
case "strikethrough":
_70=isSupportedBy(_6a|ie|_6c|_6d);
break;
case "blockdirltr":
case "blockdirrtl":
case "dirltr":
case "dirrtl":
case "inlinedirltr":
case "inlinedirrtl":
_70=isSupportedBy(ie);
break;
case "cut":
case "copy":
case "paste":
_70=isSupportedBy(ie|_6a|_6d);
break;
case "inserttable":
_70=isSupportedBy(_6a|(this.object?ie:0));
break;
case "insertcell":
case "insertcol":
case "insertrow":
case "deletecells":
case "deletecols":
case "deleterows":
case "mergecells":
case "splitcell":
_70=isSupportedBy(this.object?ie:0);
break;
default:
return false;
}
return (dojo.render.html.ie&&_70.ie)||(dojo.render.html.mozilla&&_70.mozilla)||(dojo.render.html.safari&&_70.safari)||(_6e&&_70.safari420)||(dojo.render.html.opera&&_70.opera);
},execCommand:function(_71,_72){
var _73;
this.focus();
_71=this._normalizeCommand(_71);
if(_72!=undefined){
if(_71=="heading"){
throw new Error("unimplemented");
}else{
if(_71=="formatblock"){
if(this.object){
_72=this._native2LocalFormatNames[_72];
}else{
if(dojo.render.html.ie){
_72="<"+_72+">";
}
}
}
}
}
if(this.object){
switch(_71){
case "hilitecolor":
_71="setbackcolor";
break;
case "forecolor":
case "backcolor":
case "fontsize":
case "fontname":
_71="set"+_71;
break;
case "formatblock":
_71="setblockformat";
}
if(_71=="strikethrough"){
_71="inserthtml";
var _74=this.document.selection.createRange();
if(!_74.htmlText){
return;
}
_72=_74.htmlText.strike();
}else{
if(_71=="inserthorizontalrule"){
_71="inserthtml";
_72="<hr>";
}
}
if(_71=="inserthtml"){
var _74=this.document.selection.createRange();
if(this.document.selection.type.toUpperCase()=="CONTROL"){
for(var i=0;i<_74.length;i++){
_74.item(i).outerHTML=_72;
}
}else{
_74.pasteHTML(_72);
_74.select();
}
_73=true;
}else{
if(arguments.length==1){
_73=this.object.ExecCommand(this._activeX.command[_71],this._activeX.ui.noprompt);
}else{
_73=this.object.ExecCommand(this._activeX.command[_71],this._activeX.ui.noprompt,_72);
}
}
}else{
if(_71=="inserthtml"){
if(dojo.render.html.ie){
var _76=this.document.selection.createRange();
_76.pasteHTML(_72);
_76.select();
return true;
}else{
return this.document.execCommand(_71,false,_72);
}
}else{
if((_71=="unlink")&&(this.queryCommandEnabled("unlink"))&&(dojo.render.html.mozilla)){
var _77=this.window.getSelection();
var _78=_77.getRangeAt(0);
var _79=_78.startContainer;
var _7a=_78.startOffset;
var _7b=_78.endContainer;
var _7c=_78.endOffset;
var a=dojo.withGlobal(this.window,"getAncestorElement",dojo.html.selection,["a"]);
dojo.withGlobal(this.window,"selectElement",dojo.html.selection,[a]);
_73=this.document.execCommand("unlink",false,null);
var _78=this.document.createRange();
_78.setStart(_79,_7a);
_78.setEnd(_7b,_7c);
_77.removeAllRanges();
_77.addRange(_78);
return _73;
}else{
if((_71=="hilitecolor")&&(dojo.render.html.mozilla)){
this.document.execCommand("useCSS",false,false);
_73=this.document.execCommand(_71,false,_72);
this.document.execCommand("useCSS",false,true);
}else{
if((dojo.render.html.ie)&&((_71=="backcolor")||(_71=="forecolor"))){
_72=arguments.length>1?_72:null;
_73=this.document.execCommand(_71,false,_72);
}else{
_72=arguments.length>1?_72:null;
if(_72||_71!="createlink"){
_73=this.document.execCommand(_71,false,_72);
}
}
}
}
}
}
this.onDisplayChanged();
return _73;
},queryCommandEnabled:function(_7e){
_7e=this._normalizeCommand(_7e);
if(this.object){
switch(_7e){
case "hilitecolor":
_7e="setbackcolor";
break;
case "forecolor":
case "backcolor":
case "fontsize":
case "fontname":
_7e="set"+_7e;
break;
case "formatblock":
_7e="setblockformat";
break;
case "strikethrough":
_7e="bold";
break;
case "inserthorizontalrule":
return true;
}
if(typeof this._activeX.command[_7e]=="undefined"){
return false;
}
var _7f=this.object.QueryStatus(this._activeX.command[_7e]);
return ((_7f!=this._activeX.status.notsupported)&&(_7f!=this._activeX.status.disabled));
}else{
if(dojo.render.html.mozilla){
if(_7e=="unlink"){
return dojo.withGlobal(this.window,"hasAncestorElement",dojo.html.selection,["a"]);
}else{
if(_7e=="inserttable"){
return true;
}
}
}
var _80=(dojo.render.html.ie)?this.document.selection.createRange():this.document;
return _80.queryCommandEnabled(_7e);
}
},queryCommandState:function(_81){
_81=this._normalizeCommand(_81);
if(this.object){
if(_81=="forecolor"){
_81="setforecolor";
}else{
if(_81=="backcolor"){
_81="setbackcolor";
}else{
if(_81=="strikethrough"){
return dojo.withGlobal(this.window,"hasAncestorElement",dojo.html.selection,["strike"]);
}else{
if(_81=="inserthorizontalrule"){
return false;
}
}
}
}
if(typeof this._activeX.command[_81]=="undefined"){
return null;
}
var _82=this.object.QueryStatus(this._activeX.command[_81]);
return ((_82==this._activeX.status.latched)||(_82==this._activeX.status.ninched));
}else{
return this.document.queryCommandState(_81);
}
},queryCommandValue:function(_83){
_83=this._normalizeCommand(_83);
if(this.object){
switch(_83){
case "forecolor":
case "backcolor":
case "fontsize":
case "fontname":
_83="get"+_83;
return this.object.execCommand(this._activeX.command[_83],this._activeX.ui.noprompt);
case "formatblock":
var _84=this.object.execCommand(this._activeX.command["getblockformat"],this._activeX.ui.noprompt);
if(_84){
return this._local2NativeFormatNames[_84];
}
}
}else{
if(dojo.render.html.ie&&_83=="formatblock"){
return this._local2NativeFormatNames[this.document.queryCommandValue(_83)]||this.document.queryCommandValue(_83);
}
return this.document.queryCommandValue(_83);
}
},placeCursorAtStart:function(){
this.focus();
if(dojo.render.html.moz&&this.editNode.firstChild&&this.editNode.firstChild.nodeType!=dojo.dom.TEXT_NODE){
dojo.withGlobal(this.window,"selectElementChildren",dojo.html.selection,[this.editNode.firstChild]);
}else{
dojo.withGlobal(this.window,"selectElementChildren",dojo.html.selection,[this.editNode]);
}
dojo.withGlobal(this.window,"collapse",dojo.html.selection,[true]);
},placeCursorAtEnd:function(){
this.focus();
if(dojo.render.html.moz&&this.editNode.lastChild&&this.editNode.lastChild.nodeType!=dojo.dom.TEXT_NODE){
dojo.withGlobal(this.window,"selectElementChildren",dojo.html.selection,[this.editNode.lastChild]);
}else{
dojo.withGlobal(this.window,"selectElementChildren",dojo.html.selection,[this.editNode]);
}
dojo.withGlobal(this.window,"collapse",dojo.html.selection,[false]);
},replaceEditorContent:function(_85){
_85=this._preFilterContent(_85);
if(this.isClosed){
this.domNode.innerHTML=_85;
}else{
if(this.window&&this.window.getSelection&&!dojo.render.html.moz){
this.editNode.innerHTML=_85;
}else{
if((this.window&&this.window.getSelection)||(this.document&&this.document.selection)){
this.execCommand("selectall");
if(dojo.render.html.moz&&!_85){
_85="&nbsp;";
}
this.execCommand("inserthtml",_85);
}
}
}
},_preFilterContent:function(_86){
var ec=_86;
dojo.lang.forEach(this.contentPreFilters,function(ef){
ec=ef(ec);
});
if(this.contentDomPreFilters.length>0){
var dom=dojo.doc().createElement("div");
dom.style.display="none";
dojo.body().appendChild(dom);
dom.innerHTML=ec;
dojo.lang.forEach(this.contentDomPreFilters,function(ef){
dom=ef(dom);
});
ec=dom.innerHTML;
dojo.body().removeChild(dom);
}
return ec;
},_postFilterContent:function(_8b){
var ec=_8b;
if(this.contentDomPostFilters.length>0){
var dom=this.document.createElement("div");
dom.innerHTML=ec;
dojo.lang.forEach(this.contentDomPostFilters,function(ef){
dom=ef(dom);
});
ec=dom.innerHTML;
}
dojo.lang.forEach(this.contentPostFilters,function(ef){
ec=ef(ec);
});
return ec;
},_lastHeight:0,_updateHeight:function(){
if(!this.isLoaded){
return;
}
if(this.height){
return;
}
var _90=dojo.html.getBorderBox(this.editNode).height;
if(!_90){
_90=dojo.html.getBorderBox(this.document.body).height;
}
if(_90==0){
dojo.debug("Can not figure out the height of the editing area!");
return;
}
this._lastHeight=_90;
this.editorObject.style.height=this._lastHeight+"px";
this.window.scrollTo(0,0);
},_saveContent:function(e){
var _92=dojo.doc().getElementById("dojo.widget.RichText.savedContent");
_92.value+=this._SEPARATOR+this.saveName+":"+this.getEditorContent();
},getEditorContent:function(){
var ec="";
try{
ec=(this._content.length>0)?this._content:this.editNode.innerHTML;
if(dojo.string.trim(ec)=="&nbsp;"){
ec="";
}
}
catch(e){
}
if(dojo.render.html.ie&&!this.object){
var re=new RegExp("(?:<p>&nbsp;</p>[\n\r]*)+$","i");
ec=ec.replace(re,"");
}
ec=this._postFilterContent(ec);
if(this.relativeImageUrls){
var _95=dojo.global().location.protocol+"//"+dojo.global().location.host;
var _96=dojo.global().location.pathname;
if(_96.match(/\/$/)){
}else{
var _97=_96.split("/");
if(_97.length){
_97.pop();
}
_96=_97.join("/")+"/";
}
var _98=new RegExp("(<img[^>]* src=[\"'])("+_95+"("+_96+")?)","ig");
ec=ec.replace(_98,"$1");
}
return ec;
},close:function(_99,_9a){
if(this.isClosed){
return false;
}
if(arguments.length==0){
_99=true;
}
this._content=this._postFilterContent(this.editNode.innerHTML);
var _9b=(this.savedContent!=this._content);
if(this.interval){
clearInterval(this.interval);
}
if(dojo.render.html.ie&&!this.object){
dojo.event.browser.clean(this.editNode);
}
if(this.iframe){
delete this.iframe;
}
if(this.textarea){
with(this.textarea.style){
position="";
left=top="";
if(dojo.render.html.ie){
overflow=this.__overflow;
this.__overflow=null;
}
}
if(_99){
this.textarea.value=this._content;
}else{
this.textarea.value=this.savedContent;
}
dojo.html.removeNode(this.domNode);
this.domNode=this.textarea;
}else{
if(_99){
if(dojo.render.html.moz){
var nc=dojo.doc().createElement("span");
this.domNode.appendChild(nc);
nc.innerHTML=this.editNode.innerHTML;
}else{
this.domNode.innerHTML=this._content;
}
}else{
this.domNode.innerHTML=this.savedContent;
}
}
dojo.html.removeClass(this.domNode,"RichTextEditable");
this.isClosed=true;
this.isLoaded=false;
delete this.editNode;
if(this.window._frameElement){
this.window._frameElement=null;
}
this.window=null;
this.document=null;
this.object=null;
this.editingArea=null;
this.editorObject=null;
return _9b;
},destroyRendering:function(){
},destroy:function(){
this.destroyRendering();
if(!this.isClosed){
this.close(false);
}
dojo.widget.RichText.superclass.destroy.call(this);
},connect:function(_9d,_9e,_9f){
dojo.event.connect(_9d,_9e,this,_9f);
},disconnect:function(_a0,_a1,_a2){
dojo.event.disconnect(_a0,_a1,this,_a2);
},disconnectAllWithRoot:function(_a3){
dojo.deprecated("disconnectAllWithRoot","is deprecated. No need to disconnect manually","0.5");
},_fixContentForMoz:function(_a4){
_a4=_a4.replace(/<strong([ \>])/gi,"<b$1");
_a4=_a4.replace(/<\/strong>/gi,"</b>");
_a4=_a4.replace(/<em([ \>])/gi,"<i$1");
_a4=_a4.replace(/<\/em>/gi,"</i>");
return _a4;
}});
