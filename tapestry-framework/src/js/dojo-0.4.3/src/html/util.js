dojo.provide("dojo.html.util");
dojo.require("dojo.html.layout");
dojo.html.getElementWindow=function(_1){
return dojo.html.getDocumentWindow(_1.ownerDocument);
};
dojo.html.getDocumentWindow=function(_2){
if(dojo.render.html.safari&&!_2._parentWindow){
var _3=function(_4){
_4.document._parentWindow=_4;
for(var i=0;i<_4.frames.length;i++){
_3(_4.frames[i]);
}
};
_3(window.top);
}
if(dojo.render.html.ie&&window!==document.parentWindow&&!_2._parentWindow){
_2.parentWindow.execScript("document._parentWindow = window;","Javascript");
var _6=_2._parentWindow;
_2._parentWindow=null;
return _6;
}
return _2._parentWindow||_2.parentWindow||_2.defaultView;
};
dojo.html.gravity=function(_7,e){
_7=dojo.byId(_7);
var _9=dojo.html.getCursorPosition(e);
with(dojo.html){
var _a=getAbsolutePosition(_7,true);
var bb=getBorderBox(_7);
var _c=_a.x+(bb.width/2);
var _d=_a.y+(bb.height/2);
}
with(dojo.html.gravity){
return ((_9.x<_c?WEST:EAST)|(_9.y<_d?NORTH:SOUTH));
}
};
dojo.html.gravity.NORTH=1;
dojo.html.gravity.SOUTH=1<<1;
dojo.html.gravity.EAST=1<<2;
dojo.html.gravity.WEST=1<<3;
dojo.html.overElement=function(_e,e){
_e=dojo.byId(_e);
var _10=dojo.html.getCursorPosition(e);
var bb=dojo.html.getBorderBox(_e);
var _12=dojo.html.getAbsolutePosition(_e,true,dojo.html.boxSizing.BORDER_BOX);
var top=_12.y;
var _14=top+bb.height;
var _15=_12.x;
var _16=_15+bb.width;
return (_10.x>=_15&&_10.x<=_16&&_10.y>=top&&_10.y<=_14);
};
dojo.html.renderedTextContent=function(_17){
_17=dojo.byId(_17);
var _18="";
if(_17==null){
return _18;
}
for(var i=0;i<_17.childNodes.length;i++){
switch(_17.childNodes[i].nodeType){
case 1:
case 5:
var _1a="unknown";
try{
_1a=dojo.html.getStyle(_17.childNodes[i],"display");
}
catch(E){
}
switch(_1a){
case "block":
case "list-item":
case "run-in":
case "table":
case "table-row-group":
case "table-header-group":
case "table-footer-group":
case "table-row":
case "table-column-group":
case "table-column":
case "table-cell":
case "table-caption":
_18+="\n";
_18+=dojo.html.renderedTextContent(_17.childNodes[i]);
_18+="\n";
break;
case "none":
break;
default:
if(_17.childNodes[i].tagName&&_17.childNodes[i].tagName.toLowerCase()=="br"){
_18+="\n";
}else{
_18+=dojo.html.renderedTextContent(_17.childNodes[i]);
}
break;
}
break;
case 3:
case 2:
case 4:
var _1b=_17.childNodes[i].nodeValue;
var _1c="unknown";
try{
_1c=dojo.html.getStyle(_17,"text-transform");
}
catch(E){
}
switch(_1c){
case "capitalize":
var _1d=_1b.split(" ");
for(var i=0;i<_1d.length;i++){
_1d[i]=_1d[i].charAt(0).toUpperCase()+_1d[i].substring(1);
}
_1b=_1d.join(" ");
break;
case "uppercase":
_1b=_1b.toUpperCase();
break;
case "lowercase":
_1b=_1b.toLowerCase();
break;
default:
break;
}
switch(_1c){
case "nowrap":
break;
case "pre-wrap":
break;
case "pre-line":
break;
case "pre":
break;
default:
_1b=_1b.replace(/\s+/," ");
if(/\s$/.test(_18)){
_1b.replace(/^\s/,"");
}
break;
}
_18+=_1b;
break;
default:
break;
}
}
return _18;
};
dojo.html.createNodesFromText=function(txt,_1f){
if(_1f){
txt=txt.replace(/^\s+|\s+$/g,"");
}
var tn=dojo.doc().createElement("div");
tn.style.visibility="hidden";
dojo.body().appendChild(tn);
var _21="none";
if((/^<t[dh][\s\r\n>]/i).test(txt.replace(/^\s+/))){
txt="<table><tbody><tr>"+txt+"</tr></tbody></table>";
_21="cell";
}else{
if((/^<tr[\s\r\n>]/i).test(txt.replace(/^\s+/))){
txt="<table><tbody>"+txt+"</tbody></table>";
_21="row";
}else{
if((/^<(thead|tbody|tfoot)[\s\r\n>]/i).test(txt.replace(/^\s+/))){
txt="<table>"+txt+"</table>";
_21="section";
}
}
}
tn.innerHTML=txt;
if(tn["normalize"]){
tn.normalize();
}
var _22=null;
switch(_21){
case "cell":
_22=tn.getElementsByTagName("tr")[0];
break;
case "row":
_22=tn.getElementsByTagName("tbody")[0];
break;
case "section":
_22=tn.getElementsByTagName("table")[0];
break;
default:
_22=tn;
break;
}
var _23=[];
for(var x=0;x<_22.childNodes.length;x++){
_23.push(_22.childNodes[x].cloneNode(true));
}
tn.style.display="none";
dojo.html.destroyNode(tn);
return _23;
};
dojo.html.placeOnScreen=function(_25,_26,_27,_28,_29,_2a,_2b){
if(_26 instanceof Array||typeof _26=="array"){
_2b=_2a;
_2a=_29;
_29=_28;
_28=_27;
_27=_26[1];
_26=_26[0];
}
if(_2a instanceof String||typeof _2a=="string"){
_2a=_2a.split(",");
}
if(!isNaN(_28)){
_28=[Number(_28),Number(_28)];
}else{
if(!(_28 instanceof Array||typeof _28=="array")){
_28=[0,0];
}
}
var _2c=dojo.html.getScroll().offset;
var _2d=dojo.html.getViewport();
_25=dojo.byId(_25);
var _2e=_25.style.display;
_25.style.display="";
var bb=dojo.html.getBorderBox(_25);
var w=bb.width;
var h=bb.height;
_25.style.display=_2e;
if(!(_2a instanceof Array||typeof _2a=="array")){
_2a=["TL"];
}
var _32,_33,_34=Infinity,_35;
for(var _36=0;_36<_2a.length;++_36){
var _37=_2a[_36];
var _38=true;
var _39=_26-(_37.charAt(1)=="L"?0:w)+_28[0]*(_37.charAt(1)=="L"?1:-1);
var _3a=_27-(_37.charAt(0)=="T"?0:h)+_28[1]*(_37.charAt(0)=="T"?1:-1);
if(_29){
_39-=_2c.x;
_3a-=_2c.y;
}
if(_39<0){
_39=0;
_38=false;
}
if(_3a<0){
_3a=0;
_38=false;
}
var x=_39+w;
if(x>_2d.width){
x=_2d.width-w;
_38=false;
}else{
x=_39;
}
x=Math.max(_28[0],x)+_2c.x;
var y=_3a+h;
if(y>_2d.height){
y=_2d.height-h;
_38=false;
}else{
y=_3a;
}
y=Math.max(_28[1],y)+_2c.y;
if(_38){
_32=x;
_33=y;
_34=0;
_35=_37;
break;
}else{
var _3d=Math.pow(x-_39-_2c.x,2)+Math.pow(y-_3a-_2c.y,2);
if(_34>_3d){
_34=_3d;
_32=x;
_33=y;
_35=_37;
}
}
}
if(!_2b){
_25.style.left=_32+"px";
_25.style.top=_33+"px";
}
return {left:_32,top:_33,x:_32,y:_33,dist:_34,corner:_35};
};
dojo.html.placeOnScreenPoint=function(_3e,_3f,_40,_41,_42){
dojo.deprecated("dojo.html.placeOnScreenPoint","use dojo.html.placeOnScreen() instead","0.5");
return dojo.html.placeOnScreen(_3e,_3f,_40,_41,_42,["TL","TR","BL","BR"]);
};
dojo.html.placeOnScreenAroundElement=function(_43,_44,_45,_46,_47,_48){
var _49,_4a=Infinity;
_44=dojo.byId(_44);
var _4b=_44.style.display;
_44.style.display="";
var mb=dojo.html.getElementBox(_44,_46);
var _4d=mb.width;
var _4e=mb.height;
var _4f=dojo.html.getAbsolutePosition(_44,true,_46);
_44.style.display=_4b;
for(var _50 in _47){
var pos,_52,_53;
var _54=_47[_50];
_52=_4f.x+(_50.charAt(1)=="L"?0:_4d);
_53=_4f.y+(_50.charAt(0)=="T"?0:_4e);
pos=dojo.html.placeOnScreen(_43,_52,_53,_45,true,_54,true);
if(pos.dist==0){
_49=pos;
break;
}else{
if(_4a>pos.dist){
_4a=pos.dist;
_49=pos;
}
}
}
if(!_48){
_43.style.left=_49.left+"px";
_43.style.top=_49.top+"px";
}
return _49;
};
dojo.html.scrollIntoView=function(_55){
if(!_55){
return;
}
if(dojo.render.html.ie){
if(dojo.html.getBorderBox(_55.parentNode).height<=_55.parentNode.scrollHeight){
_55.scrollIntoView(false);
}
}else{
if(dojo.render.html.mozilla){
_55.scrollIntoView(false);
}else{
var _56=_55.parentNode;
var _57=_56.scrollTop+dojo.html.getBorderBox(_56).height;
var _58=_55.offsetTop+dojo.html.getMarginBox(_55).height;
if(_57<_58){
_56.scrollTop+=(_58-_57);
}else{
if(_56.scrollTop>_55.offsetTop){
_56.scrollTop-=(_56.scrollTop-_55.offsetTop);
}
}
}
}
};
