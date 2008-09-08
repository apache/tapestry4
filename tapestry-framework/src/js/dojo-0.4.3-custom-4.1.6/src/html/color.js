dojo.require("dojo.html.style");
dojo.provide("dojo.html.color");
dojo.require("dojo.gfx.color");
dojo.require("dojo.lang.common");
dojo.html.getBackgroundColor=function(_1){
_1=dojo.byId(_1);
var _2;
do{
_2=dojo.html.getStyle(_1,"background-color");
if(_2.toLowerCase()=="rgba(0, 0, 0, 0)"){
_2="transparent";
}
if(_1==document.getElementsByTagName("body")[0]){
_1=null;
break;
}
_1=_1.parentNode;
}while(_1&&dojo.lang.inArray(["transparent",""],_2));
if(_2=="transparent"){
_2=[255,255,255,0];
}else{
_2=dojo.gfx.color.extractRGB(_2);
}
return _2;
};
