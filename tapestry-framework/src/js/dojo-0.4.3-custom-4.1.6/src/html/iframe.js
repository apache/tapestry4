dojo.provide("dojo.html.iframe");
dojo.require("dojo.html.util");
dojo.html.iframeContentWindow=function(_1){
var _2=dojo.html.getDocumentWindow(dojo.html.iframeContentDocument(_1))||dojo.html.iframeContentDocument(_1).__parent__||(_1.name&&document.frames[_1.name])||null;
return _2;
};
dojo.html.iframeContentDocument=function(_3){
var _4=_3.contentDocument||((_3.contentWindow)&&(_3.contentWindow.document))||((_3.name)&&(document.frames[_3.name])&&(document.frames[_3.name].document))||null;
return _4;
};
dojo.html.BackgroundIframe=function(_5){
if(dojo.render.html.ie55||dojo.render.html.ie60){
var _6="<iframe src='javascript:false'"+" style='position: absolute; left: 0px; top: 0px; width: 100%; height: 100%;"+"z-index: -1; filter:Alpha(Opacity=\"0\");' "+">";
this.iframe=dojo.doc().createElement(_6);
this.iframe.tabIndex=-1;
if(_5){
_5.appendChild(this.iframe);
this.domNode=_5;
}else{
dojo.body().appendChild(this.iframe);
this.iframe.style.display="none";
}
}
};
dojo.lang.extend(dojo.html.BackgroundIframe,{iframe:null,onResized:function(){
if(this.iframe&&this.domNode&&this.domNode.parentNode){
var _7=dojo.html.getMarginBox(this.domNode);
if(_7.width==0||_7.height==0){
dojo.lang.setTimeout(this,this.onResized,100);
return;
}
this.iframe.style.width=_7.width+"px";
this.iframe.style.height=_7.height+"px";
}
},size:function(_8){
if(!this.iframe){
return;
}
var _9=dojo.html.toCoordinateObject(_8,true,dojo.html.boxSizing.BORDER_BOX);
with(this.iframe.style){
width=_9.width+"px";
height=_9.height+"px";
left=_9.left+"px";
top=_9.top+"px";
}
},setZIndex:function(_a){
if(!this.iframe){
return;
}
if(dojo.dom.isNode(_a)){
this.iframe.style.zIndex=dojo.html.getStyle(_a,"z-index")-1;
}else{
if(!isNaN(_a)){
this.iframe.style.zIndex=_a;
}
}
},show:function(){
if(this.iframe){
this.iframe.style.display="block";
}
},hide:function(){
if(this.iframe){
this.iframe.style.display="none";
}
},remove:function(){
if(this.iframe){
dojo.html.removeNode(this.iframe,true);
delete this.iframe;
this.iframe=null;
}
}});
