dojo.provide("dojo.lfx.rounded");
dojo.require("dojo.lang.common");
dojo.require("dojo.html.common");
dojo.require("dojo.html.style");
dojo.require("dojo.html.display");
dojo.require("dojo.html.layout");
dojo.lfx.rounded=function(_1){
var _2={validTags:_1.validTags||["div"],autoPad:_1.autoPad!=null?_1.autoPad:true,antiAlias:_1.antiAlias!=null?_1.antiAlias:true,radii:{tl:(_1.tl&&_1.tl.radius!=null)?_1.tl.radius:5,tr:(_1.tr&&_1.tr.radius!=null)?_1.tr.radius:5,bl:(_1.bl&&_1.bl.radius!=null)?_1.bl.radius:5,br:(_1.br&&_1.br.radius!=null)?_1.br.radius:5}};
var _3;
if(typeof (arguments[1])=="string"){
_3=dojo.html.getElementsByClass(arguments[1]);
}else{
if(dojo.lang.isArrayLike(arguments[1])){
_3=arguments[1];
for(var i=0;i<_3.length;i++){
_3[i]=dojo.byId(_3[i]);
}
}
}
if(_3.length==0){
return;
}
for(var i=0;i<_3.length;i++){
dojo.lfx.rounded.applyCorners(_2,_3[i]);
}
};
dojo.lfx.rounded.applyCorners=function(_5,_6){
var _7=null;
var _8=null;
var _9=null;
var _a=dojo.lfx.rounded._fns;
var _b=_6.offsetWidth;
var _c=_6.offsetHeight;
var _d=parseInt(dojo.html.getComputedStyle(_6,"border-top-width"));
var _e=dojo.html.getComputedStyle(_6,"border-top-color");
var _f=dojo.html.getComputedStyle(_6,"background-color");
var _10=dojo.html.getComputedStyle(_6,"background-image");
var _11=dojo.html.getComputedStyle(_6,"position");
var _12=parseInt(dojo.html.getComputedStyle(_6,"padding-top"));
var _13={height:_c,width:_b,borderWidth:_d,color:_a.getRGB(_f),padding:_12,borderColor:_a.getRGB(_e),borderString:_d+"px"+" solid "+_a.getRGB(_e),bgImage:((_10!="none")?_10:""),content:_6.innerHTML};
if(!dojo.html.isPositionAbsolute(_6)){
_6.style.position="relative";
}
_6.style.padding="0px";
if(dojo.render.html.ie&&_b=="auto"&&_c=="auto"){
_6.style.width="100%";
}
if(_5.autoPad&&_13.padding>0){
_6.innerHTML="";
}
var _14=Math.max(_5.radii.tl,_5.radii.tr);
var _15=Math.max(_5.radii.bl,_5.radii.br);
if(_5.radii.tl||_5.radii.tr){
_7=document.createElement("div");
_7.style.width="100%";
_7.style.fontSize="1px";
_7.style.overflow="hidden";
_7.style.position="absolute";
_7.style.paddingLeft=_13.borderWidth+"px";
_7.style.paddingRight=_13.borderWidth+"px";
_7.style.height=_14+"px";
_7.style.top=(0-_14)+"px";
_7.style.left=(0-_13.borderWidth)+"px";
_6.appendChild(_7);
}
if(_5.radii.bl||_5.radii.br){
_8=document.createElement("div");
_8.style.width="100%";
_8.style.fontSize="1px";
_8.style.overflow="hidden";
_8.style.position="absolute";
_8.style.paddingLeft=_13.borderWidth+"px";
_8.style.paddingRight=_13.borderWidth+"px";
_8.style.height=_15+"px";
_8.style.bottom=(0-_15)+"px";
_8.style.left=(0-_13.borderWidth)+"px";
_6.appendChild(_8);
}
if(_7){
_6.style.borderTopWidth="0px";
}
if(_8){
_6.style.borderBottomWidth="0px";
}
var _16=["tr","tl","br","bl"];
for(var i=0;i<_16.length;i++){
var cc=_16[i];
if(_5.radii[cc]==0){
if((cc.charAt(0)=="t"&&_7)||(cc.charAt(0)=="b"&&_8)){
var _19=document.createElement("div");
_19.style.position="relative";
_19.style.fontSize="1px;";
_19.style.overflow="hidden";
if(_13.bgImage==""){
_19.style.backgroundColor=_13.color;
}else{
_19.style.backgroundImage=_13.bgImage;
}
switch(cc){
case "tl":
_19.style.height=_14-_13.borderWidth+"px";
_19.style.marginRight=_5.radii[cc]-(_13.borderWidth*2)+"px";
_19.style.borderLeft=_13.borderString;
_19.style.borderTop=_13.borderString;
_19.style.left=-_13.borderWidth+"px";
break;
case "tr":
_19.style.height=_14-_13.borderWidth+"px";
_19.style.marginLeft=_5.radii[cc]-(_13.borderWidth*2)+"px";
_19.style.borderRight=_13.borderString;
_19.style.borderTop=_13.borderString;
_19.style.backgroundPosition="-"+(_14-_13.borderWidth)+"px 0px";
_19.style.left=_13.borderWidth+"px";
break;
case "bl":
_19.style.height=_15-_13.borderWidth+"px";
_19.style.marginRight=_5.radii[cc]-(_13.borderWidth*2)+"px";
_19.style.borderLeft=_13.borderString;
_19.style.borderBottom=_13.borderString;
_19.style.left=_13.borderWidth+"px";
_19.style.backgroundPosition="-"+_13.borderWidth+"px -"+(_13.height+(_15+_13.borderWidth))+"px";
break;
case "br":
_19.style.height=_15-_13.borderWidth+"px";
_19.style.marginLeft=_5.radii[cc]-(_13.borderWidth*2)+"px";
_19.style.borderRight=_13.borderString;
_19.style.borderBottom=_13.borderString;
_19.style.left=_13.borderWidth+"px";
_19.style.backgroundPosition="-"+(_15+_13.borderWidth)+"px -"+(_13.height+(_15+_13.borderWidth))+"px";
break;
}
}
}else{
var _19=document.createElement("div");
_19.style.height=_5.radii[cc]+"px";
_19.style.width=_5.radii[cc]+"px";
_19.style.position="absolute";
_19.style.fontSize="1px";
_19.style.overflow="hidden";
var _1a=Math.floor(_5.radii[cc]-_13.borderWidth);
for(var x=0,j=_5.radii[cc];x<j;x++){
var y1=Math.floor(Math.sqrt(Math.pow(_1a,2)-Math.pow((x+1),2)))-1;
if((x+1)>=_1a){
var y1=-1;
}
var y2=Math.ceil(Math.sqrt(Math.pow(_1a,2)-Math.pow(x,2)));
if(x>=_1a){
y2=-1;
}
var y3=Math.floor(Math.sqrt(Math.pow(j,2)-Math.pow((x+1),2)))-1;
if((x+1)>=j){
y3=-1;
}
var y4=Math.ceil(Math.sqrt(Math.pow(j,2)-Math.pow(x,2)));
if(x>=j){
y4=-1;
}
if(y1>-1){
_a.draw(x,0,_13.color,100,(y1+1),_19,-1,j,_14,_13);
}
for(var y=(y1+1);y<y2;y++){
if(_5.antiAlias){
if(_13.bgImage!=""){
var _22=_a.fraction(x,y,_1a)*100;
if(_22<30){
_a.draw(x,y,_13.borderColor,100,1,_19,0,_5.radii[cc],_14,_13);
}else{
_a.draw(x,y,_13.borderColor,100,1,_19,-1,_5.radii[cc],_14,_13);
}
}else{
var clr=_a.blend(_13.color,_13.borderColor,_a.fraction(x,y,_1a));
_a.draw(x,y,clr,100,1,_19,0,_5.radii[cc],_14,_13);
}
}
}
if(_5.antiAlias){
if(y3>=y2){
if(y2==-1){
y2=0;
}
_a.draw(x,y2,_13.borderColor,100,(y3-y2+1),_19,0,0,_14,_13);
}else{
if(y3>=y1){
_a.draw(x,(y1+1),_13.borderColor,100,(y3-y1),_19,0,0,_14,_13);
}
}
for(var y=(y3+1);y<y4;y++){
_a.draw(x,y,_13.borderColor,(_a.fraction(x,y,j)*100),1,_19,(_13.borderWidth>0?0:-1),_5.radii[cc],_14,_13);
}
}else{
y3=y1;
}
}
if(cc!="br"){
for(var t=0,k=_19.childNodes.length;t<k;t++){
var bar=_19.childNodes[t];
var _27=parseInt(dojo.html.getComputedStyle(bar,"top"));
var _28=parseInt(dojo.html.getComputedStyle(bar,"left"));
var _29=parseInt(dojo.html.getComputedStyle(bar,"height"));
if(cc.charAt(1)=="l"){
bar.style.left=(_5.radii[cc]-_28-1)+"px";
}
if(cc=="tr"){
bar.style.top=(_5.radii[cc]-_29-_27)+"px";
bar.style.backgroundPosition="-"+Math.abs((_13.width-_5.radii[cc]+_13.borderWidth)+_28)+"px -"+Math.abs(_5.radii[cc]-_29-_27-_13.borderWidth)+"px";
}else{
if(cc=="tl"){
bar.style.top=(_5.radii[cc]-_29-_27)+"px";
bar.style.backgroundPosition="-"+Math.abs((_5.radii[cc]-_28-1)-_13.borderWidth)+"px -"+Math.abs(_5.radii[cc]-_29-_27-_13.borderWidth)+"px";
}else{
bar.style.backgroundPosition="-"+Math.abs((_5.radii[cc]+_28)+_13.borderWidth)+"px -"+Math.abs((_13.height+_5.radii[cc]+_27)-_13.borderWidth)+"px";
}
}
}
}
}
if(_19){
var psn=[];
if(cc.charAt(0)=="t"){
psn.push("top");
}else{
psn.push("bottom");
}
if(cc.charAt(1)=="l"){
psn.push("left");
}else{
psn.push("right");
}
if(_19.style.position=="absolute"){
for(var z=0;z<psn.length;z++){
_19.style[psn[z]]="0px";
}
}
if(psn[0]=="top"){
if(_7){
_7.appendChild(_19);
}
}else{
if(_8){
_8.appendChild(_19);
}
}
}
}
var _2c={t:Math.abs(_5.radii.tl-_5.radii.tr),b:Math.abs(_5.radii.bl-_5.radii.br)};
for(var z in _2c){
var _2d=(_5.radii[z+"l"]<_5.radii[z+"r"]?z+"l":z+"r");
var _2e=document.createElement("div");
_2e.style.height=_2c[z]+"px";
_2e.style.width=_5.radii[_2d]+"px";
_2e.style.position="absolute";
_2e.style.fontSize="1px";
_2e.style.overflow="hidden";
_2e.style.backgroundColor=_13.color;
switch(_2d){
case "tl":
_2e.style.bottom="0px";
_2e.style.left="0px";
_2e.style.borderLeft=_13.borderString;
_7.appendChild(_2e);
break;
case "tr":
_2e.style.bottom="0px";
_2e.style.right="0px";
_2e.style.borderRight=_13.borderString;
_7.appendChild(_2e);
break;
case "bl":
_2e.style.top="0px";
_2e.style.left="0px";
_2e.style.borderLeft=_13.borderString;
_8.appendChild(_2e);
break;
case "br":
_2e.style.top="0px";
_2e.style.right="0px";
_2e.style.borderRight=_13.borderString;
_8.appendChild(_2e);
break;
}
var _2f=document.createElement("div");
_2f.style.position="relative";
_2f.style.fontSize="1px";
_2f.style.overflow="hidden";
_2f.style.backgroundColor=_13.color;
_2f.style.backgroundImage=_13.bgImage;
if(z=="t"){
if(_7){
if(_5.radii.tl&&_5.radii.tr){
_2f.style.height=(_14-_13.borderWidth)+"px";
_2f.style.marginLeft=(_5.radii.tl-_13.borderWidth)+"px";
_2f.style.marginRight=(_5.radii.tr-_13.borderWidth)+"px";
_2f.style.borderTop=_13.borderString;
if(_13.bgImage!=""){
_2f.style.backgroundPosition="-"+(_14+_13.borderWidth)+"px 0px";
}
}
_7.appendChild(_2f);
}
}else{
if(_8){
if(_5.radii.bl&&_5.radii.br){
_2f.style.height=(_15-_13.borderWidth)+"px";
_2f.style.marginLeft=(_5.radii.bl-_13.borderWidth)+"px";
_2f.style.marginRight=(_5.radii.br-_13.borderWidth)+"px";
_2f.style.borderBottom=_13.borderString;
if(_13.bgImage!=""){
_2f.style.backgroundPosition="-"+(_15+_13.borderWidth)+"px -"+(_13.height+(_14+_13.borderWidth))+"px";
}
}
_8.appendChild(_2f);
}
}
}
if(_5.autoPad&&_13.padding>0){
var _30=document.createElement("div");
_30.style.position="relative";
_30.innerHTML=_13.content;
_30.className="autoPadDiv";
if(_14<_13.padding){
_30.style.paddingTop=Math.abs(_14-_13.padding)+"px";
}
if(_15<_13.padding){
_30.style.paddingBottom=Math.abs(_15-_13.padding)+"px";
}
_30.style.paddingLeft=_13.padding+"px";
_30.style.paddingRight=_13.padding+"px";
_6.appendChild(_30);
}
};
var count=0;
dojo.lfx.rounded._fns={blend:function(_31,_32,_33){
var c1={r:parseInt(_31.substr(1,2),16),g:parseInt(_31.substr(3,2),16),b:parseInt(_31.substr(5,2),16)};
var c2={r:parseInt(_32.substr(1,2),16),g:parseInt(_32.substr(3,2),16),b:parseInt(_32.substr(5,2),16)};
if(_33>1||_33<0){
_33=1;
}
var ret=[Math.min(Math.max(Math.round((c1.r*_33)+(c2.r*(1-_33))),0),255),Math.min(Math.max(Math.round((c1.g*_33)+(c2.g*(1-_33))),0),255),Math.min(Math.max(Math.round((c1.b*_33)+(c2.b*(1-_33))),0),255)];
for(var i=0;i<ret.length;i++){
var n=ret[i].toString(16);
if(n.length<2){
n="0"+n;
}
ret[i]=n;
}
return "#"+ret.join("");
},fraction:function(x,y,r){
var _3c=0;
var _3d=[];
var _3e=[];
var _3f=0;
var _40="";
var _41=Math.sqrt((Math.pow(r,2)-Math.pow(x,2)));
if(_41>=y&&_41<(y+1)){
_40="Left";
_3d[_3f]=0;
_3e[_3f++]=_41-y;
}
_41=Math.sqrt((Math.pow(r,2)-Math.pow(y+1,2)));
if(_41>=x&&_41<(x+1)){
_40+="Top";
_3d[_3f]=_41-x;
_3e[_3f++]=1;
}
_41=Math.sqrt((Math.pow(r,2)-Math.pow(x+1,2)));
if(_41>=y&&_41<(y+1)){
_40+="Right";
_3d[_3f]=1;
_3e[_3f++]=_41-y;
}
_41=Math.sqrt((Math.pow(r,2)-Math.pow(y,2)));
if(_41>=x&&_41<(x+1)){
_40+="Bottom";
_3d[_3f]=_41-x;
_3e[_3f]=1;
}
switch(_40){
case "LeftRight":
return Math.min(_3e[0],_3e[1])+((Math.max(_3e[0],_3e[1])-Math.min(_3e[0],_3e[1]))/2);
case "TopRight":
return 1-(((1-_3d[0])*(1-_3e[1]))/2);
case "TopBottom":
return Math.min(_3d[0],_3d[1])+((Math.max(_3d[0],_3d[1])-Math.min(_3d[0],_3d[1]))/2);
case "LeftBottom":
return (_3e[0]*_3d[1])/2;
default:
return 1;
}
},draw:function(x,y,_44,_45,_46,_47,_48,_49,top,_4b){
var px=document.createElement("div");
px.style.height=_46+"px";
px.style.width="1px";
px.style.position="absolute";
px.style.fontSize="1px";
px.style.overflow="hidden";
if(_48==-1&&_4b.bgImage!=""){
px.style.backgroundImage=_4b.bgImage;
px.style.backgroundPosition="-"+(_4b.width-(_49-x)+_4b.borderWidth)+"px -"+((_4b.height+top+y)-_4b.borderWidth)+"px";
}else{
px.style.backgroundColor=_44;
}
if(_45!=100){
dojo.html.setOpacity(px,(_45/100));
}
px.style.top=y+"px";
px.style.left=x+"px";
_47.appendChild(px);
},getRGB:function(clr){
var ret="#ffffff";
if(clr!=""&&clr!="transparent"){
if(clr.substr(0,3)=="rgb"){
var t=clr.substring(4,clr.indexOf(")"));
t=t.split(",");
for(var i=0;i<t.length;i++){
var n=parseInt(t[i]).toString(16);
if(n.length<2){
n="0"+n;
}
t[i]=n;
}
ret="#"+t.join("");
}else{
if(clr.length==4){
ret="#"+clr.substring(1,2)+clr.substring(1,2)+clr.substring(2,3)+clr.substring(2,3)+clr.substring(3,4)+clr.substring(3,4);
}else{
ret=clr;
}
}
}
return ret;
}};
