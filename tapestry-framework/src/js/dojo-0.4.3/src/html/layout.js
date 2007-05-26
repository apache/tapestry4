dojo.provide("dojo.html.layout");
dojo.require("dojo.html.common");
dojo.require("dojo.html.style");
dojo.require("dojo.html.display");
dojo.html.sumAncestorProperties=function(_1,_2){
_1=dojo.byId(_1);
if(!_1){
return 0;
}
var _3=0;
while(_1){
if(dojo.html.getComputedStyle(_1,"position")=="fixed"){
return 0;
}
var _4=_1[_2];
if(_4){
_3+=_4-0;
if(_1==dojo.body()){
break;
}
}
_1=_1.parentNode;
}
return _3;
};
dojo.html.setStyleAttributes=function(_5,_6){
_5=dojo.byId(_5);
var _7=_6.replace(/(;)?\s*$/,"").split(";");
for(var i=0;i<_7.length;i++){
var _9=_7[i].split(":");
var _a=_9[0].replace(/\s*$/,"").replace(/^\s*/,"").toLowerCase();
var _b=_9[1].replace(/\s*$/,"").replace(/^\s*/,"");
switch(_a){
case "opacity":
dojo.html.setOpacity(_5,_b);
break;
case "content-height":
dojo.html.setContentBox(_5,{height:_b});
break;
case "content-width":
dojo.html.setContentBox(_5,{width:_b});
break;
case "outer-height":
dojo.html.setMarginBox(_5,{height:_b});
break;
case "outer-width":
dojo.html.setMarginBox(_5,{width:_b});
break;
default:
_5.style[dojo.html.toCamelCase(_a)]=_b;
}
}
};
dojo.html.boxSizing={MARGIN_BOX:"margin-box",BORDER_BOX:"border-box",PADDING_BOX:"padding-box",CONTENT_BOX:"content-box"};
dojo.html.getAbsolutePosition=dojo.html.abs=function(_c,_d,_e){
_c=dojo.byId(_c,_c.ownerDocument);
var _f={x:0,y:0};
var bs=dojo.html.boxSizing;
if(!_e){
_e=bs.CONTENT_BOX;
}
var _11=2;
var _12;
switch(_e){
case bs.MARGIN_BOX:
_12=3;
break;
case bs.BORDER_BOX:
_12=2;
break;
case bs.PADDING_BOX:
default:
_12=1;
break;
case bs.CONTENT_BOX:
_12=0;
break;
}
var h=dojo.render.html;
var db=document["body"]||document["documentElement"];
if(h.ie){
with(_c.getBoundingClientRect()){
_f.x=left-2;
_f.y=top-2;
}
}else{
if(document.getBoxObjectFor){
_11=1;
try{
var bo=document.getBoxObjectFor(_c);
_f.x=bo.x-dojo.html.sumAncestorProperties(_c,"scrollLeft");
_f.y=bo.y-dojo.html.sumAncestorProperties(_c,"scrollTop");
}
catch(e){
}
}else{
if(_c["offsetParent"]){
var _16;
if((h.safari)&&(_c.style.getPropertyValue("position")=="absolute")&&(_c.parentNode==db)){
_16=db;
}else{
_16=db.parentNode;
}
if(_c.parentNode!=db){
var nd=_c;
if(dojo.render.html.opera){
nd=db;
}
_f.x-=dojo.html.sumAncestorProperties(nd,"scrollLeft");
_f.y-=dojo.html.sumAncestorProperties(nd,"scrollTop");
}
var _18=_c;
do{
var n=_18["offsetLeft"];
if(!h.opera||n>0){
_f.x+=isNaN(n)?0:n;
}
var m=_18["offsetTop"];
_f.y+=isNaN(m)?0:m;
_18=_18.offsetParent;
}while((_18!=_16)&&(_18!=null));
}else{
if(_c["x"]&&_c["y"]){
_f.x+=isNaN(_c.x)?0:_c.x;
_f.y+=isNaN(_c.y)?0:_c.y;
}
}
}
}
if(_d){
var _1b=dojo.html.getScroll();
_f.y+=_1b.top;
_f.x+=_1b.left;
}
var _1c=[dojo.html.getPaddingExtent,dojo.html.getBorderExtent,dojo.html.getMarginExtent];
if(_11>_12){
for(var i=_12;i<_11;++i){
_f.y+=_1c[i](_c,"top");
_f.x+=_1c[i](_c,"left");
}
}else{
if(_11<_12){
for(var i=_12;i>_11;--i){
_f.y-=_1c[i-1](_c,"top");
_f.x-=_1c[i-1](_c,"left");
}
}
}
_f.top=_f.y;
_f.left=_f.x;
return _f;
};
dojo.html.isPositionAbsolute=function(_1e){
return (dojo.html.getComputedStyle(_1e,"position")=="absolute");
};
dojo.html._sumPixelValues=function(_1f,_20,_21){
var _22=0;
for(var x=0;x<_20.length;x++){
_22+=dojo.html.getPixelValue(_1f,_20[x],_21);
}
return _22;
};
dojo.html.getMargin=function(_24){
return {width:dojo.html._sumPixelValues(_24,["margin-left","margin-right"],(dojo.html.getComputedStyle(_24,"position")=="absolute")),height:dojo.html._sumPixelValues(_24,["margin-top","margin-bottom"],(dojo.html.getComputedStyle(_24,"position")=="absolute"))};
};
dojo.html.getBorder=function(_25){
return {width:dojo.html.getBorderExtent(_25,"left")+dojo.html.getBorderExtent(_25,"right"),height:dojo.html.getBorderExtent(_25,"top")+dojo.html.getBorderExtent(_25,"bottom")};
};
dojo.html.getBorderExtent=function(_26,_27){
return (dojo.html.getStyle(_26,"border-"+_27+"-style")=="none"?0:dojo.html.getPixelValue(_26,"border-"+_27+"-width"));
};
dojo.html.getMarginExtent=function(_28,_29){
return dojo.html._sumPixelValues(_28,["margin-"+_29],dojo.html.isPositionAbsolute(_28));
};
dojo.html.getPaddingExtent=function(_2a,_2b){
return dojo.html._sumPixelValues(_2a,["padding-"+_2b],true);
};
dojo.html.getPadding=function(_2c){
return {width:dojo.html._sumPixelValues(_2c,["padding-left","padding-right"],true),height:dojo.html._sumPixelValues(_2c,["padding-top","padding-bottom"],true)};
};
dojo.html.getPadBorder=function(_2d){
var pad=dojo.html.getPadding(_2d);
var _2f=dojo.html.getBorder(_2d);
return {width:pad.width+_2f.width,height:pad.height+_2f.height};
};
dojo.html.getBoxSizing=function(_30){
var h=dojo.render.html;
var bs=dojo.html.boxSizing;
if(((h.ie)||(h.opera))&&_30.nodeName.toLowerCase()!="img"){
var cm=document["compatMode"];
if((cm=="BackCompat")||(cm=="QuirksMode")){
return bs.BORDER_BOX;
}else{
return bs.CONTENT_BOX;
}
}else{
if(arguments.length==0){
_30=document.documentElement;
}
var _34;
if(!h.ie){
_34=dojo.html.getStyle(_30,"-moz-box-sizing");
if(!_34){
_34=dojo.html.getStyle(_30,"box-sizing");
}
}
return (_34?_34:bs.CONTENT_BOX);
}
};
dojo.html.isBorderBox=function(_35){
return (dojo.html.getBoxSizing(_35)==dojo.html.boxSizing.BORDER_BOX);
};
dojo.html.getBorderBox=function(_36){
_36=dojo.byId(_36);
return {width:_36.offsetWidth,height:_36.offsetHeight};
};
dojo.html.getPaddingBox=function(_37){
var box=dojo.html.getBorderBox(_37);
var _39=dojo.html.getBorder(_37);
return {width:box.width-_39.width,height:box.height-_39.height};
};
dojo.html.getContentBox=function(_3a){
_3a=dojo.byId(_3a);
var _3b=dojo.html.getPadBorder(_3a);
return {width:_3a.offsetWidth-_3b.width,height:_3a.offsetHeight-_3b.height};
};
dojo.html.setContentBox=function(_3c,_3d){
_3c=dojo.byId(_3c);
var _3e=0;
var _3f=0;
var _40=dojo.html.isBorderBox(_3c);
var _41=(_40?dojo.html.getPadBorder(_3c):{width:0,height:0});
var ret={};
if(typeof _3d.width!="undefined"){
_3e=_3d.width+_41.width;
ret.width=dojo.html.setPositivePixelValue(_3c,"width",_3e);
}
if(typeof _3d.height!="undefined"){
_3f=_3d.height+_41.height;
ret.height=dojo.html.setPositivePixelValue(_3c,"height",_3f);
}
return ret;
};
dojo.html.getMarginBox=function(_43){
var _44=dojo.html.getBorderBox(_43);
var _45=dojo.html.getMargin(_43);
return {width:_44.width+_45.width,height:_44.height+_45.height};
};
dojo.html.setMarginBox=function(_46,_47){
_46=dojo.byId(_46);
var _48=0;
var _49=0;
var _4a=dojo.html.isBorderBox(_46);
var _4b=(!_4a?dojo.html.getPadBorder(_46):{width:0,height:0});
var _4c=dojo.html.getMargin(_46);
var ret={};
if(typeof _47.width!="undefined"){
_48=_47.width-_4b.width;
_48-=_4c.width;
ret.width=dojo.html.setPositivePixelValue(_46,"width",_48);
}
if(typeof _47.height!="undefined"){
_49=_47.height-_4b.height;
_49-=_4c.height;
ret.height=dojo.html.setPositivePixelValue(_46,"height",_49);
}
return ret;
};
dojo.html.getElementBox=function(_4e,_4f){
var bs=dojo.html.boxSizing;
switch(_4f){
case bs.MARGIN_BOX:
return dojo.html.getMarginBox(_4e);
case bs.BORDER_BOX:
return dojo.html.getBorderBox(_4e);
case bs.PADDING_BOX:
return dojo.html.getPaddingBox(_4e);
case bs.CONTENT_BOX:
default:
return dojo.html.getContentBox(_4e);
}
};
dojo.html.toCoordinateObject=dojo.html.toCoordinateArray=function(_51,_52,_53){
if(_51 instanceof Array||typeof _51=="array"){
dojo.deprecated("dojo.html.toCoordinateArray","use dojo.html.toCoordinateObject({left: , top: , width: , height: }) instead","0.5");
while(_51.length<4){
_51.push(0);
}
while(_51.length>4){
_51.pop();
}
var ret={left:_51[0],top:_51[1],width:_51[2],height:_51[3]};
}else{
if(!_51.nodeType&&!(_51 instanceof String||typeof _51=="string")&&("width" in _51||"height" in _51||"left" in _51||"x" in _51||"top" in _51||"y" in _51)){
var ret={left:_51.left||_51.x||0,top:_51.top||_51.y||0,width:_51.width||0,height:_51.height||0};
}else{
var _55=dojo.byId(_51);
var pos=dojo.html.abs(_55,_52,_53);
var _57=dojo.html.getMarginBox(_55);
var ret={left:pos.left,top:pos.top,width:_57.width,height:_57.height};
}
}
ret.x=ret.left;
ret.y=ret.top;
return ret;
};
dojo.html.setMarginBoxWidth=dojo.html.setOuterWidth=function(_58,_59){
return dojo.html._callDeprecated("setMarginBoxWidth","setMarginBox",arguments,"width");
};
dojo.html.setMarginBoxHeight=dojo.html.setOuterHeight=function(){
return dojo.html._callDeprecated("setMarginBoxHeight","setMarginBox",arguments,"height");
};
dojo.html.getMarginBoxWidth=dojo.html.getOuterWidth=function(){
return dojo.html._callDeprecated("getMarginBoxWidth","getMarginBox",arguments,null,"width");
};
dojo.html.getMarginBoxHeight=dojo.html.getOuterHeight=function(){
return dojo.html._callDeprecated("getMarginBoxHeight","getMarginBox",arguments,null,"height");
};
dojo.html.getTotalOffset=function(_5a,_5b,_5c){
return dojo.html._callDeprecated("getTotalOffset","getAbsolutePosition",arguments,null,_5b);
};
dojo.html.getAbsoluteX=function(_5d,_5e){
return dojo.html._callDeprecated("getAbsoluteX","getAbsolutePosition",arguments,null,"x");
};
dojo.html.getAbsoluteY=function(_5f,_60){
return dojo.html._callDeprecated("getAbsoluteY","getAbsolutePosition",arguments,null,"y");
};
dojo.html.totalOffsetLeft=function(_61,_62){
return dojo.html._callDeprecated("totalOffsetLeft","getAbsolutePosition",arguments,null,"left");
};
dojo.html.totalOffsetTop=function(_63,_64){
return dojo.html._callDeprecated("totalOffsetTop","getAbsolutePosition",arguments,null,"top");
};
dojo.html.getMarginWidth=function(_65){
return dojo.html._callDeprecated("getMarginWidth","getMargin",arguments,null,"width");
};
dojo.html.getMarginHeight=function(_66){
return dojo.html._callDeprecated("getMarginHeight","getMargin",arguments,null,"height");
};
dojo.html.getBorderWidth=function(_67){
return dojo.html._callDeprecated("getBorderWidth","getBorder",arguments,null,"width");
};
dojo.html.getBorderHeight=function(_68){
return dojo.html._callDeprecated("getBorderHeight","getBorder",arguments,null,"height");
};
dojo.html.getPaddingWidth=function(_69){
return dojo.html._callDeprecated("getPaddingWidth","getPadding",arguments,null,"width");
};
dojo.html.getPaddingHeight=function(_6a){
return dojo.html._callDeprecated("getPaddingHeight","getPadding",arguments,null,"height");
};
dojo.html.getPadBorderWidth=function(_6b){
return dojo.html._callDeprecated("getPadBorderWidth","getPadBorder",arguments,null,"width");
};
dojo.html.getPadBorderHeight=function(_6c){
return dojo.html._callDeprecated("getPadBorderHeight","getPadBorder",arguments,null,"height");
};
dojo.html.getBorderBoxWidth=dojo.html.getInnerWidth=function(){
return dojo.html._callDeprecated("getBorderBoxWidth","getBorderBox",arguments,null,"width");
};
dojo.html.getBorderBoxHeight=dojo.html.getInnerHeight=function(){
return dojo.html._callDeprecated("getBorderBoxHeight","getBorderBox",arguments,null,"height");
};
dojo.html.getContentBoxWidth=dojo.html.getContentWidth=function(){
return dojo.html._callDeprecated("getContentBoxWidth","getContentBox",arguments,null,"width");
};
dojo.html.getContentBoxHeight=dojo.html.getContentHeight=function(){
return dojo.html._callDeprecated("getContentBoxHeight","getContentBox",arguments,null,"height");
};
dojo.html.setContentBoxWidth=dojo.html.setContentWidth=function(_6d,_6e){
return dojo.html._callDeprecated("setContentBoxWidth","setContentBox",arguments,"width");
};
dojo.html.setContentBoxHeight=dojo.html.setContentHeight=function(_6f,_70){
return dojo.html._callDeprecated("setContentBoxHeight","setContentBox",arguments,"height");
};
