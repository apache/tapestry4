dojo.provide("dojo.a11y");
dojo.require("dojo.uri.*");
dojo.require("dojo.html.common");
dojo.a11y={imgPath:dojo.uri.moduleUri("dojo.widget","templates/images"),doAccessibleCheck:true,accessible:null,checkAccessible:function(){
if(this.accessible===null){
this.accessible=false;
if(this.doAccessibleCheck==true){
this.accessible=this.testAccessible();
}
}
return this.accessible;
},testAccessible:function(){
this.accessible=false;
if(dojo.render.html.ie||dojo.render.html.mozilla){
var _1=document.createElement("div");
_1.style.backgroundImage="url(\""+this.imgPath+"/tab_close.gif\")";
dojo.body().appendChild(_1);
var _2=null;
if(window.getComputedStyle){
var _3=getComputedStyle(_1,"");
_2=_3.getPropertyValue("background-image");
}else{
_2=_1.currentStyle.backgroundImage;
}
var _4=false;
if(_2!=null&&(_2=="none"||_2=="url(invalid-url:)")){
this.accessible=true;
}
dojo.body().removeChild(_1);
}
return this.accessible;
},setCheckAccessible:function(_5){
this.doAccessibleCheck=_5;
},setAccessibleMode:function(){
if(this.accessible===null){
if(this.checkAccessible()){
dojo.render.html.prefixes.unshift("a11y");
}
}
return this.accessible;
}};
