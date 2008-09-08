dojo.provide("dojo.widget.html.layout");
dojo.require("dojo.lang.common");
dojo.require("dojo.string.extras");
dojo.require("dojo.html.style");
dojo.require("dojo.html.layout");
dojo.widget.html.layout=function(_1,_2,_3){
dojo.html.addClass(_1,"dojoLayoutContainer");
_2=dojo.lang.filter(_2,function(_4,_5){
_4.idx=_5;
return dojo.lang.inArray(["top","bottom","left","right","client","flood"],_4.layoutAlign);
});
if(_3&&_3!="none"){
var _6=function(_7){
switch(_7.layoutAlign){
case "flood":
return 1;
case "left":
case "right":
return (_3=="left-right")?2:3;
case "top":
case "bottom":
return (_3=="left-right")?3:2;
default:
return 4;
}
};
_2.sort(function(a,b){
return (_6(a)-_6(b))||(a.idx-b.idx);
});
}
var f={top:dojo.html.getPixelValue(_1,"padding-top",true),left:dojo.html.getPixelValue(_1,"padding-left",true)};
dojo.lang.mixin(f,dojo.html.getContentBox(_1));
dojo.lang.forEach(_2,function(_b){
var _c=_b.domNode;
var _d=_b.layoutAlign;
with(_c.style){
left=f.left+"px";
top=f.top+"px";
bottom="auto";
right="auto";
}
dojo.html.addClass(_c,"dojoAlign"+dojo.string.capitalize(_d));
if((_d=="top")||(_d=="bottom")){
dojo.html.setMarginBox(_c,{width:f.width});
var h=dojo.html.getMarginBox(_c).height;
f.height-=h;
if(_d=="top"){
f.top+=h;
}else{
_c.style.top=f.top+f.height+"px";
}
if(_b.onResized){
_b.onResized();
}
}else{
if(_d=="left"||_d=="right"){
var w=dojo.html.getMarginBox(_c).width;
if(_b.resizeTo){
_b.resizeTo(w,f.height);
}else{
dojo.html.setMarginBox(_c,{width:w,height:f.height});
}
f.width-=w;
if(_d=="left"){
f.left+=w;
}else{
_c.style.left=f.left+f.width+"px";
}
}else{
if(_d=="flood"||_d=="client"){
if(_b.resizeTo){
_b.resizeTo(f.width,f.height);
}else{
dojo.html.setMarginBox(_c,{width:f.width,height:f.height});
}
}
}
}
});
};
dojo.html.insertCssText(".dojoLayoutContainer{ position: relative; display: block; overflow: hidden; }\n"+"body .dojoAlignTop, body .dojoAlignBottom, body .dojoAlignLeft, body .dojoAlignRight { position: absolute; overflow: hidden; }\n"+"body .dojoAlignClient { position: absolute }\n"+".dojoAlignClient { overflow: auto; }\n");
