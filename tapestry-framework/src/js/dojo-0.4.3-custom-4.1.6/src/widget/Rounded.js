dojo.provide("dojo.widget.Rounded");
dojo.widget.tags.addParseTreeHandler("dojo:rounded");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.ContentPane");
dojo.require("dojo.html.style");
dojo.require("dojo.html.display");
dojo.require("dojo.gfx.color");
dojo.deprecated("dojo.widget.Rounded will be removed in version 0.5; you can now apply rounded corners to any block element using dojo.lfx.rounded.","0.5");
dojo.widget.defineWidget("dojo.widget.Rounded",dojo.widget.ContentPane,{isSafari:dojo.render.html.safari,boxMargin:"50px",radius:14,domNode:"",corners:"TR,TL,BR,BL",antiAlias:true,fillInTemplate:function(_1,_2){
dojo.widget.Rounded.superclass.fillInTemplate.call(this,_1,_2);
dojo.html.insertCssFile(this.templateCssPath);
if(this.domNode.style.height<=0){
var _3=(this.radius*1)+this.domNode.clientHeight;
this.domNode.style.height=_3+"px";
}
if(this.domNode.style.width<=0){
var _4=(this.radius*1)+this.domNode.clientWidth;
this.domNode.style.width=_4+"px";
}
var _5=["TR","TL","BR","BL"];
var _6=this.corners.split(",");
this.settings={antiAlias:this.antiAlias};
var _7=function(_8){
var _9=_8.toLowerCase();
if(dojo.lang.inArray(_6,_8)){
this.settings[_9]={radius:this.radius,enabled:true};
}else{
this.settings[_9]={radius:0};
}
};
dojo.lang.forEach(_5,_7,this);
this.domNode.style.margin=this.boxMargin;
this.curvyCorners(this.settings);
this.applyCorners();
},curvyCorners:function(_a){
this.box=this.domNode;
this.topContainer=null;
this.bottomContainer=null;
this.masterCorners=[];
var _b=dojo.html.getStyle(this.box,"height");
if(_b==""){
_b="0px";
}
var _c=dojo.html.getStyle(this.box,"width");
var _d=dojo.html.getStyle(this.box,"borderTopWidth");
if(_d==""){
_d="0px";
}
var _e=dojo.html.getStyle(this.box,"borderTopColor");
if(_d>0){
this.antiAlias=true;
}
var _f=dojo.html.getStyle(this.box,"backgroundColor");
var _10=dojo.html.getStyle(this.box,"backgroundImage");
var _11=dojo.html.getStyle(this.box,"position");
this.boxHeight=parseInt(((_b!=""&&_b!="auto"&&_b.indexOf("%")==-1)?_b.substring(0,_b.indexOf("px")):this.box.scrollHeight));
this.boxWidth=parseInt(((_c!=""&&_c!="auto"&&_c.indexOf("%")==-1)?_c.substring(0,_c.indexOf("px")):this.box.scrollWidth));
this.borderWidth=parseInt(((_d!=""&&_d.indexOf("px")!==-1)?_d.slice(0,_d.indexOf("px")):0));
var _12=new dojo.gfx.color.Color(_f);
this.boxColour=((_f!=""&&_f!="transparent")?((_f.substr(0,3)=="rgb")?this.rgb2Hex(_f):_f):"#ffffff");
this.borderColour=((_e!=""&&_e!="transparent"&&this.borderWidth>0)?((_e.substr(0,3)=="rgb")?this.rgb2Hex(_e):_e):this.boxColour);
this.borderString=this.borderWidth+"px"+" solid "+this.borderColour;
this.backgroundImage=((_10!="none")?_10:"");
if(_11!="absolute"){
this.box.style.position="relative";
}
this.applyCorners=function(){
for(var t=0;t<2;t++){
switch(t){
case 0:
if(this.settings.tl.enabled||this.settings.tr.enabled){
var _14=document.createElement("DIV");
with(_14.style){
width="100%";
fontSize="1px";
overflow="hidden";
position="absolute";
paddingLeft=this.borderWidth+"px";
paddingRight=this.borderWidth+"px";
var _15=Math.max(this.settings.tl?this.settings.tl.radius:0,this.settings.tr?this.settings.tr.radius:0);
height=_15+"px";
top=0-_15+"px";
left=0-this.borderWidth+"px";
}
this.topContainer=this.box.appendChild(_14);
}
break;
case 1:
if(this.settings.bl.enabled||this.settings.br.enabled){
var _14=document.createElement("DIV");
with(_14.style){
width="100%";
fontSize="1px";
overflow="hidden";
position="absolute";
paddingLeft=this.borderWidth+"px";
paddingRight=this.borderWidth+"px";
var _16=Math.max(this.settings.bl?this.settings.bl.radius:0,this.settings.br?this.settings.br.radius:0);
height=_16+"px";
bottom=0-_16+"px";
left=0-this.borderWidth+"px";
}
this.bottomContainer=this.box.appendChild(_14);
}
break;
}
}
if(this.topContainer){
this.box.style.borderTopWidth="0px";
}
if(this.bottomContainer){
this.box.style.borderBottomWidth="0px";
}
var _17=["tr","tl","br","bl"];
for(var i in _17){
var cc=_17[i];
if(!this.settings[cc]){
if(((cc=="tr"||cc=="tl")&&this.topContainer!=null)||((cc=="br"||cc=="bl")&&this.bottomContainer!=null)){
var _1a=document.createElement("DIV");
_1a.style.position="relative";
_1a.style.fontSize="1px";
_1a.style.overflow="hidden";
if(this.backgroundImage==""){
_1a.style.backgroundColor=this.boxColour;
}else{
_1a.style.backgroundImage=this.backgroundImage;
}
switch(cc){
case "tl":
with(_1a.style){
height=_15-this.borderWidth+"px";
marginRight=this.settings.tr.radius-(this.borderWidth*2)+"px";
borderLeft=this.borderString;
borderTop=this.borderString;
left=-this.borderWidth+"px";
}
break;
case "tr":
with(_1a.style){
height=_15-this.borderWidth+"px";
marginLeft=this.settings.tl.radius-(this.borderWidth*2)+"px";
borderRight=this.borderString;
borderTop=this.borderString;
backgroundPosition="-"+this.boxWidth+"px 0px";
left=this.borderWidth+"px";
}
break;
case "bl":
with(_1a.style){
height=_16-this.borderWidth+"px";
marginRight=this.settings.br.radius-(this.borderWidth*2)+"px";
borderLeft=this.borderString;
borderBottom=this.borderString;
left=-this.borderWidth+"px";
}
break;
case "br":
with(_1a.style){
height=_16-this.borderWidth+"px";
marginLeft=this.settings.bl.radius-(this.borderWidth*2)+"px";
borderRight=this.borderString;
borderBottom=this.borderString;
left=this.borderWidth+"px";
}
break;
}
}
}else{
if(this.masterCorners[this.settings[cc].radius]){
var _1a=this.masterCorners[this.settings[cc].radius].cloneNode(true);
}else{
var _1a=document.createElement("DIV");
with(_1a.style){
height=this.settings[cc].radius+"px";
width=this.settings[cc].radius+"px";
position="absolute";
fontSize="1px";
overflow="hidden";
}
var _1b=parseInt(this.settings[cc].radius-this.borderWidth);
for(var _1c=0,j=this.settings[cc].radius;_1c<j;_1c++){
if((_1c+1)>=_1b){
var y1=-1;
}else{
var y1=(Math.floor(Math.sqrt(Math.pow(_1b,2)-Math.pow((_1c+1),2)))-1);
}
if(_1b!=j){
if((_1c)>=_1b){
var y2=-1;
}else{
var y2=Math.ceil(Math.sqrt(Math.pow(_1b,2)-Math.pow(_1c,2)));
}
if((_1c+1)>=j){
var y3=-1;
}else{
var y3=(Math.floor(Math.sqrt(Math.pow(j,2)-Math.pow((_1c+1),2)))-1);
}
}
if((_1c)>=j){
var y4=-1;
}else{
var y4=Math.ceil(Math.sqrt(Math.pow(j,2)-Math.pow(_1c,2)));
}
if(y1>-1){
this.drawPixel(_1c,0,this.boxColour,100,(y1+1),_1a,-1,this.settings[cc].radius);
}
if(_1b!=j){
if(this.antiAlias){
for(var _22=(y1+1);_22<y2;_22++){
if(this.backgroundImage!=""){
var _23=(this.pixelFraction(_1c,_22,_1b)*100);
if(_23<30){
this.drawPixel(_1c,_22,this.borderColour,100,1,_1a,0,this.settings[cc].radius);
}else{
this.drawPixel(_1c,_22,this.borderColour,100,1,_1a,-1,this.settings[cc].radius);
}
}else{
var _24=dojo.gfx.color.blend(this.boxColour,this.borderColour,this.pixelFraction(_1c,_22,_1b));
this.drawPixel(_1c,_22,_24,100,1,_1a,0,this.settings[cc].radius);
}
}
}
if(y3>=y2){
if(y1==-1){
y1=0;
}
this.drawPixel(_1c,y2,this.borderColour,100,(y3-y2+1),_1a,0,this.settings[cc].radius);
}
var _25=this.borderColour;
}else{
var _25=this.boxColour;
var y3=y1;
}
if(this.antiAlias){
for(var _22=(y3+1);_22<y4;_22++){
this.drawPixel(_1c,_22,_25,(this.pixelFraction(_1c,_22,j)*100),1,_1a,((this.borderWidth>0)?0:-1),this.settings[cc].radius);
}
}
}
this.masterCorners[this.settings[cc].radius]=_1a.cloneNode(true);
}
if(cc!="br"){
for(var t=0,k=_1a.childNodes.length;t<k;t++){
var _27=_1a.childNodes[t];
var _28=parseInt(_27.style.top.substring(0,_27.style.top.indexOf("px")));
var _29=parseInt(_27.style.left.substring(0,_27.style.left.indexOf("px")));
var _2a=parseInt(_27.style.height.substring(0,_27.style.height.indexOf("px")));
if(cc=="tl"||cc=="bl"){
_27.style.left=this.settings[cc].radius-_29-1+"px";
}
if(cc=="tr"||cc=="tl"){
_27.style.top=this.settings[cc].radius-_2a-_28+"px";
}
var _2b;
switch(cc){
case "tr":
_2b=(-1*(Math.abs((this.boxWidth-this.settings[cc].radius+this.borderWidth)+_29)-(Math.abs(this.settings[cc].radius-_2a-_28-this.borderWidth))));
_27.style.backgroundPosition=_2b+"px";
break;
case "tl":
_2b=(-1*(Math.abs((this.settings[cc].radius-_29-1)-this.borderWidth)-(Math.abs(this.settings[cc].radius-_2a-_28-this.borderWidth))));
_27.style.backgroundPosition=_2b+"px";
break;
case "bl":
_2b=(-1*(Math.abs((this.settings[cc].radius-_29-1)-this.borderWidth)-(Math.abs((this.boxHeight+this.settings[cc].radius+_28)-this.borderWidth))));
_27.style.backgroundPosition=_2b+"px";
break;
}
}
}
}
if(_1a){
switch(cc){
case "tl":
if(_1a.style.position=="absolute"){
_1a.style.top="0px";
}
if(_1a.style.position=="absolute"){
_1a.style.left="0px";
}
if(this.topContainer){
this.topContainer.appendChild(_1a);
}
break;
case "tr":
if(_1a.style.position=="absolute"){
_1a.style.top="0px";
}
if(_1a.style.position=="absolute"){
_1a.style.right="0px";
}
if(this.topContainer){
this.topContainer.appendChild(_1a);
}
break;
case "bl":
if(_1a.style.position=="absolute"){
_1a.style.bottom="0px";
}
if(_1a.style.position=="absolute"){
_1a.style.left="0px";
}
if(this.bottomContainer){
this.bottomContainer.appendChild(_1a);
}
break;
case "br":
if(_1a.style.position=="absolute"){
_1a.style.bottom="0px";
}
if(_1a.style.position=="absolute"){
_1a.style.right="0px";
}
if(this.bottomContainer){
this.bottomContainer.appendChild(_1a);
}
break;
}
}
}
var _2c=[];
_2c["t"]=this.settings.tl.enabled&&this.settings.tr.enabled?Math.abs(this.settings.tl.radius-this.settings.tr.radius):0;
_2c["b"]=this.settings.bl.enabled&&this.settings.br.enabled?Math.abs(this.settings.bl.radius-this.settings.br.radius):0;
for(var z in _2c){
if(_2c[z]){
var _2e=((this.settings[z+"l"].radius<this.settings[z+"r"].radius)?z+"l":z+"r");
var _2f=document.createElement("DIV");
with(_2f.style){
height=_2c[z]+"px";
width=this.settings[_2e].radius+"px";
position="absolute";
fontSize="1px";
overflow="hidden";
backgroundColor=this.boxColour;
}
switch(_2e){
case "tl":
with(_2f.style){
bottom="0px";
left="0px";
borderLeft=this.borderString;
}
this.topContainer.appendChild(_2f);
break;
case "tr":
with(_2f.style){
bottom="0px";
right="0px";
borderRight=this.borderString;
}
this.topContainer.appendChild(_2f);
break;
case "bl":
with(_2f.style){
top="0px";
left="0px";
borderLeft=this.borderString;
}
this.bottomContainer.appendChild(_2f);
break;
case "br":
with(_2f.style){
top="0px";
right="0px";
borderRight=this.borderString;
}
this.bottomContainer.appendChild(_2f);
break;
}
}
var _30=document.createElement("DIV");
with(_30.style){
position="relative";
fontSize="1px";
overflow="hidden";
backgroundColor=this.boxColour;
}
switch(z){
case "t":
if(this.topContainer){
with(_30.style){
height=_15-this.borderWidth+"px";
marginLeft=this.settings.tl.radius-this.borderWidth+"px";
marginRight=this.settings.tr.radius-this.borderWidth+"px";
borderTop=this.borderString;
}
this.topContainer.appendChild(_30);
}
break;
case "b":
if(this.bottomContainer){
with(_30.style){
height=_16-this.borderWidth+"px";
marginLeft=this.settings.bl.radius-this.borderWidth+"px";
marginRight=this.settings.br.radius-this.borderWidth+"px";
borderBottom=this.borderString;
}
this.bottomContainer.appendChild(_30);
}
break;
}
}
};
this.drawPixel=function(_31,_32,_33,_34,_35,_36,_37,_38){
var _39=document.createElement("DIV");
_39.style.height=_35+"px";
_39.style.width="1px";
_39.style.position="absolute";
_39.style.fontSize="1px";
_39.style.overflow="hidden";
if(_37==-1&&this.backgroundImage!=""){
_39.style.backgroundImage=this.backgroundImage;
_39.style.backgroundPosition="-"+(this.boxWidth-(_38-_31)+this.borderWidth)+"px -"+((this.boxHeight+_38+_32)-this.borderWidth)+"px";
}else{
_39.style.backgroundColor=_33;
}
if(_34!=100){
dojo.html.setOpacity(_39,_34);
}
_39.style.top=_32+"px";
_39.style.left=_31+"px";
_36.appendChild(_39);
};
},pixelFraction:function(x,y,r){
var _3d=0;
var _3e=[];
var _3f=[];
var _40=0;
var _41="";
var _42=Math.sqrt((Math.pow(r,2)-Math.pow(x,2)));
if((_42>=y)&&(_42<(y+1))){
_41="Left";
_3e[_40]=0;
_3f[_40]=_42-y;
_40=_40+1;
}
var _42=Math.sqrt((Math.pow(r,2)-Math.pow(y+1,2)));
if((_42>=x)&&(_42<(x+1))){
_41=_41+"Top";
_3e[_40]=_42-x;
_3f[_40]=1;
_40=_40+1;
}
var _42=Math.sqrt((Math.pow(r,2)-Math.pow(x+1,2)));
if((_42>=y)&&(_42<(y+1))){
_41=_41+"Right";
_3e[_40]=1;
_3f[_40]=_42-y;
_40=_40+1;
}
var _42=Math.sqrt((Math.pow(r,2)-Math.pow(y,2)));
if((_42>=x)&&(_42<(x+1))){
_41=_41+"Bottom";
_3e[_40]=_42-x;
_3f[_40]=0;
}
switch(_41){
case "LeftRight":
_3d=Math.min(_3f[0],_3f[1])+((Math.max(_3f[0],_3f[1])-Math.min(_3f[0],_3f[1]))/2);
break;
case "TopRight":
_3d=1-(((1-_3e[0])*(1-_3f[1]))/2);
break;
case "TopBottom":
_3d=Math.min(_3e[0],_3e[1])+((Math.max(_3e[0],_3e[1])-Math.min(_3e[0],_3e[1]))/2);
break;
case "LeftBottom":
_3d=(_3f[0]*_3e[1])/2;
break;
default:
_3d=1;
}
return _3d;
},rgb2Hex:function(_43){
try{
var _44=this.rgb2Array(_43);
var red=parseInt(_44[0]);
var _46=parseInt(_44[1]);
var _47=parseInt(_44[2]);
var _48="#"+this.intToHex(red)+this.intToHex(_46)+this.intToHex(_47);
}
catch(e){
alert("There was an error converting the RGB value to Hexadecimal in function rgb2Hex");
}
return _48;
},intToHex:function(_49){
var _4a=_49/16;
var rem=_49%16;
var _4a=_4a-(rem/16);
var _4c=this.makeHex(_4a);
var _4d=this.makeHex(rem);
return _4c+""+_4d;
},makeHex:function(x){
if((x>=0)&&(x<=9)){
return x;
}else{
switch(x){
case 10:
return "A";
case 11:
return "B";
case 12:
return "C";
case 13:
return "D";
case 14:
return "E";
case 15:
return "F";
}
}
},rgb2Array:function(_4f){
var _50=_4f.substring(4,_4f.indexOf(")"));
var _51=_50.split(", ");
return _51;
}});
