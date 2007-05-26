dojo.provide("dojo.html.display");
dojo.require("dojo.html.style");
dojo.html._toggle=function(_1,_2,_3){
_1=dojo.byId(_1);
_3(_1,!_2(_1));
return _2(_1);
};
dojo.html.show=function(_4){
_4=dojo.byId(_4);
if(dojo.html.getStyleProperty(_4,"display")=="none"){
dojo.html.setStyle(_4,"display",(_4.dojoDisplayCache||""));
_4.dojoDisplayCache=undefined;
}
};
dojo.html.hide=function(_5){
_5=dojo.byId(_5);
if(typeof _5["dojoDisplayCache"]=="undefined"){
var d=dojo.html.getStyleProperty(_5,"display");
if(d!="none"){
_5.dojoDisplayCache=d;
}
}
dojo.html.setStyle(_5,"display","none");
};
dojo.html.setShowing=function(_7,_8){
dojo.html[(_8?"show":"hide")](_7);
};
dojo.html.isShowing=function(_9){
return (dojo.html.getStyleProperty(_9,"display")!="none");
};
dojo.html.toggleShowing=function(_a){
return dojo.html._toggle(_a,dojo.html.isShowing,dojo.html.setShowing);
};
dojo.html.displayMap={tr:"",td:"",th:"",img:"inline",span:"inline",input:"inline",button:"inline"};
dojo.html.suggestDisplayByTagName=function(_b){
_b=dojo.byId(_b);
if(_b&&_b.tagName){
var _c=_b.tagName.toLowerCase();
return (_c in dojo.html.displayMap?dojo.html.displayMap[_c]:"block");
}
};
dojo.html.setDisplay=function(_d,_e){
dojo.html.setStyle(_d,"display",((_e instanceof String||typeof _e=="string")?_e:(_e?dojo.html.suggestDisplayByTagName(_d):"none")));
};
dojo.html.isDisplayed=function(_f){
return (dojo.html.getComputedStyle(_f,"display")!="none");
};
dojo.html.toggleDisplay=function(_10){
return dojo.html._toggle(_10,dojo.html.isDisplayed,dojo.html.setDisplay);
};
dojo.html.setVisibility=function(_11,_12){
dojo.html.setStyle(_11,"visibility",((_12 instanceof String||typeof _12=="string")?_12:(_12?"visible":"hidden")));
};
dojo.html.isVisible=function(_13){
return (dojo.html.getComputedStyle(_13,"visibility")!="hidden");
};
dojo.html.toggleVisibility=function(_14){
return dojo.html._toggle(_14,dojo.html.isVisible,dojo.html.setVisibility);
};
dojo.html.setOpacity=function(_15,_16,_17){
_15=dojo.byId(_15);
var h=dojo.render.html;
if(!_17){
if(_16>=1){
if(h.ie){
dojo.html.clearOpacity(_15);
return;
}else{
_16=0.999999;
}
}else{
if(_16<0){
_16=0;
}
}
}
if(h.ie){
if(_15.nodeName.toLowerCase()=="tr"){
var tds=_15.getElementsByTagName("td");
for(var x=0;x<tds.length;x++){
tds[x].style.filter="Alpha(Opacity="+_16*100+")";
}
}
_15.style.filter="Alpha(Opacity="+_16*100+")";
}else{
if(h.moz){
_15.style.opacity=_16;
_15.style.MozOpacity=_16;
}else{
if(h.safari){
_15.style.opacity=_16;
_15.style.KhtmlOpacity=_16;
}else{
_15.style.opacity=_16;
}
}
}
};
dojo.html.clearOpacity=function(_1b){
_1b=dojo.byId(_1b);
var ns=_1b.style;
var h=dojo.render.html;
if(h.ie){
try{
if(_1b.filters&&_1b.filters.alpha){
ns.filter="";
}
}
catch(e){
}
}else{
if(h.moz){
ns.opacity=1;
ns.MozOpacity=1;
}else{
if(h.safari){
ns.opacity=1;
ns.KhtmlOpacity=1;
}else{
ns.opacity=1;
}
}
}
};
dojo.html.getOpacity=function(_1e){
_1e=dojo.byId(_1e);
var h=dojo.render.html;
if(h.ie){
var _20=(_1e.filters&&_1e.filters.alpha&&typeof _1e.filters.alpha.opacity=="number"?_1e.filters.alpha.opacity:100)/100;
}else{
var _20=_1e.style.opacity||_1e.style.MozOpacity||_1e.style.KhtmlOpacity||1;
}
return _20>=0.999999?1:Number(_20);
};
