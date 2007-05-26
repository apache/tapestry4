dojo.require("dojo.html.common");
dojo.provide("dojo.html.selection");
dojo.require("dojo.dom");
dojo.require("dojo.lang.common");
dojo.html.selectionType={NONE:0,TEXT:1,CONTROL:2};
dojo.html.clearSelection=function(){
var _1=dojo.global();
var _2=dojo.doc();
try{
if(_1["getSelection"]){
if(dojo.render.html.safari){
_1.getSelection().collapse();
}else{
_1.getSelection().removeAllRanges();
}
}else{
if(_2.selection){
if(_2.selection.empty){
_2.selection.empty();
}else{
if(_2.selection.clear){
_2.selection.clear();
}
}
}
}
return true;
}
catch(e){
dojo.debug(e);
return false;
}
};
dojo.html.disableSelection=function(_3){
_3=dojo.byId(_3)||dojo.body();
var h=dojo.render.html;
if(h.mozilla){
_3.style.MozUserSelect="none";
}else{
if(h.safari){
_3.style.KhtmlUserSelect="none";
}else{
if(h.ie){
_3.unselectable="on";
}else{
return false;
}
}
}
return true;
};
dojo.html.enableSelection=function(_5){
_5=dojo.byId(_5)||dojo.body();
var h=dojo.render.html;
if(h.mozilla){
_5.style.MozUserSelect="";
}else{
if(h.safari){
_5.style.KhtmlUserSelect="";
}else{
if(h.ie){
_5.unselectable="off";
}else{
return false;
}
}
}
return true;
};
dojo.html.selectElement=function(_7){
dojo.deprecated("dojo.html.selectElement","replaced by dojo.html.selection.selectElementChildren",0.5);
};
dojo.html.selectInputText=function(_8){
var _9=dojo.global();
var _a=dojo.doc();
_8=dojo.byId(_8);
if(_a["selection"]&&dojo.body()["createTextRange"]){
var _b=_8.createTextRange();
_b.moveStart("character",0);
_b.moveEnd("character",_8.value.length);
_b.select();
}else{
if(_9["getSelection"]){
var _c=_9.getSelection();
_8.setSelectionRange(0,_8.value.length);
}
}
_8.focus();
};
dojo.html.isSelectionCollapsed=function(){
dojo.deprecated("dojo.html.isSelectionCollapsed","replaced by dojo.html.selection.isCollapsed",0.5);
return dojo.html.selection.isCollapsed();
};
dojo.lang.mixin(dojo.html.selection,{getType:function(){
if(dojo.doc()["selection"]){
return dojo.html.selectionType[dojo.doc().selection.type.toUpperCase()];
}else{
var _d=dojo.html.selectionType.TEXT;
var _e;
try{
_e=dojo.global().getSelection();
}
catch(e){
}
if(_e&&_e.rangeCount==1){
var _f=_e.getRangeAt(0);
if(_f.startContainer==_f.endContainer&&(_f.endOffset-_f.startOffset)==1&&_f.startContainer.nodeType!=dojo.dom.TEXT_NODE){
_d=dojo.html.selectionType.CONTROL;
}
}
return _d;
}
},isCollapsed:function(){
var _10=dojo.global();
var _11=dojo.doc();
if(_11["selection"]){
return _11.selection.createRange().text=="";
}else{
if(_10["getSelection"]){
var _12=_10.getSelection();
if(dojo.lang.isString(_12)){
return _12=="";
}else{
return _12.isCollapsed||_12.toString()=="";
}
}
}
},getSelectedElement:function(){
if(dojo.html.selection.getType()==dojo.html.selectionType.CONTROL){
if(dojo.doc()["selection"]){
var _13=dojo.doc().selection.createRange();
if(_13&&_13.item){
return dojo.doc().selection.createRange().item(0);
}
}else{
var _14=dojo.global().getSelection();
return _14.anchorNode.childNodes[_14.anchorOffset];
}
}
},getParentElement:function(){
if(dojo.html.selection.getType()==dojo.html.selectionType.CONTROL){
var p=dojo.html.selection.getSelectedElement();
if(p){
return p.parentNode;
}
}else{
if(dojo.doc()["selection"]){
return dojo.doc().selection.createRange().parentElement();
}else{
var _16=dojo.global().getSelection();
if(_16){
var _17=_16.anchorNode;
while(_17&&_17.nodeType!=dojo.dom.ELEMENT_NODE){
_17=_17.parentNode;
}
return _17;
}
}
}
},getSelectedText:function(){
if(dojo.doc()["selection"]){
if(dojo.html.selection.getType()==dojo.html.selectionType.CONTROL){
return null;
}
return dojo.doc().selection.createRange().text;
}else{
var _18=dojo.global().getSelection();
if(_18){
return _18.toString();
}
}
},getSelectedHtml:function(){
if(dojo.doc()["selection"]){
if(dojo.html.selection.getType()==dojo.html.selectionType.CONTROL){
return null;
}
return dojo.doc().selection.createRange().htmlText;
}else{
var _19=dojo.global().getSelection();
if(_19&&_19.rangeCount){
var _1a=_19.getRangeAt(0).cloneContents();
var div=document.createElement("div");
div.appendChild(_1a);
return div.innerHTML;
}
return null;
}
},hasAncestorElement:function(_1c){
return (dojo.html.selection.getAncestorElement.apply(this,arguments)!=null);
},getAncestorElement:function(_1d){
var _1e=dojo.html.selection.getSelectedElement()||dojo.html.selection.getParentElement();
while(_1e){
if(dojo.html.selection.isTag(_1e,arguments).length>0){
return _1e;
}
_1e=_1e.parentNode;
}
return null;
},isTag:function(_1f,_20){
if(_1f&&_1f.tagName){
for(var i=0;i<_20.length;i++){
if(_1f.tagName.toLowerCase()==String(_20[i]).toLowerCase()){
return String(_20[i]).toLowerCase();
}
}
}
return "";
},selectElement:function(_22){
var _23=dojo.global();
var _24=dojo.doc();
_22=dojo.byId(_22);
if(_24.selection&&dojo.body().createTextRange){
try{
var _25=dojo.body().createControlRange();
_25.addElement(_22);
_25.select();
}
catch(e){
dojo.html.selection.selectElementChildren(_22);
}
}else{
if(_23["getSelection"]){
var _26=_23.getSelection();
if(_26["removeAllRanges"]){
var _25=_24.createRange();
_25.selectNode(_22);
_26.removeAllRanges();
_26.addRange(_25);
}
}
}
},selectElementChildren:function(_27){
var _28=dojo.global();
var _29=dojo.doc();
_27=dojo.byId(_27);
if(_29.selection&&dojo.body().createTextRange){
var _2a=dojo.body().createTextRange();
_2a.moveToElementText(_27);
_2a.select();
}else{
if(_28["getSelection"]){
var _2b=_28.getSelection();
if(_2b["setBaseAndExtent"]){
_2b.setBaseAndExtent(_27,0,_27,_27.innerText.length-1);
}else{
if(_2b["selectAllChildren"]){
_2b.selectAllChildren(_27);
}
}
}
}
},getBookmark:function(){
var _2c;
var _2d=dojo.doc();
if(_2d["selection"]){
var _2e=_2d.selection.createRange();
_2c=_2e.getBookmark();
}else{
var _2f;
try{
_2f=dojo.global().getSelection();
}
catch(e){
}
if(_2f){
var _2e=_2f.getRangeAt(0);
_2c=_2e.cloneRange();
}else{
dojo.debug("No idea how to store the current selection for this browser!");
}
}
return _2c;
},moveToBookmark:function(_30){
var _31=dojo.doc();
if(_31["selection"]){
var _32=_31.selection.createRange();
_32.moveToBookmark(_30);
_32.select();
}else{
var _33;
try{
_33=dojo.global().getSelection();
}
catch(e){
}
if(_33&&_33["removeAllRanges"]){
_33.removeAllRanges();
_33.addRange(_30);
}else{
dojo.debug("No idea how to restore selection for this browser!");
}
}
},collapse:function(_34){
if(dojo.global()["getSelection"]){
var _35=dojo.global().getSelection();
if(_35.removeAllRanges){
if(_34){
_35.collapseToStart();
}else{
_35.collapseToEnd();
}
}else{
dojo.global().getSelection().collapse(_34);
}
}else{
if(dojo.doc().selection){
var _36=dojo.doc().selection.createRange();
_36.collapse(_34);
_36.select();
}
}
},remove:function(){
if(dojo.doc().selection){
var _37=dojo.doc().selection;
if(_37.type.toUpperCase()!="NONE"){
_37.clear();
}
return _37;
}else{
var _37=dojo.global().getSelection();
for(var i=0;i<_37.rangeCount;i++){
_37.getRangeAt(i).deleteContents();
}
return _37;
}
}});
