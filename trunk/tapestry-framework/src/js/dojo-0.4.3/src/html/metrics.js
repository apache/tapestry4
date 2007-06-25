dojo.provide("dojo.html.metrics");
dojo.require("dojo.html.layout");
dojo.html.getScrollbar=function(){
var _1=document.createElement("div");
_1.style.width="100px";
_1.style.height="100px";
_1.style.overflow="scroll";
_1.style.position="absolute";
_1.style.top="-300px";
_1.style.left="0px";
var _2=document.createElement("div");
_2.style.width="400px";
_2.style.height="400px";
_1.appendChild(_2);
dojo.body().appendChild(_1);
var _3=_1.offsetWidth-_1.clientWidth;
dojo.body().removeChild(_1);
_1.removeChild(_2);
_1=_2=null;
return {width:_3};
};
dojo.html.getFontMeasurements=function(){
var _4={"1em":0,"1ex":0,"100%":0,"12pt":0,"16px":0,"xx-small":0,"x-small":0,"small":0,"medium":0,"large":0,"x-large":0,"xx-large":0};
if(dojo.render.html.ie){
document.documentElement.style.fontSize="100%";
}
var _5=document.createElement("div");
_5.style.position="absolute";
_5.style.left="-100px";
_5.style.top="0";
_5.style.width="30px";
_5.style.height="1000em";
_5.style.border="0";
_5.style.margin="0";
_5.style.padding="0";
_5.style.outline="0";
_5.style.lineHeight="1";
_5.style.overflow="hidden";
dojo.body().appendChild(_5);
for(var p in _4){
_5.style.fontSize=p;
_4[p]=Math.round(_5.offsetHeight*12/16)*16/12/1000;
}
dojo.body().removeChild(_5);
_5=null;
return _4;
};
dojo.html._fontMeasurements=null;
dojo.html.getCachedFontMeasurements=function(_7){
if(_7||!dojo.html._fontMeasurements){
dojo.html._fontMeasurements=dojo.html.getFontMeasurements();
}
return dojo.html._fontMeasurements;
};
dojo.html.measureFragment=function(_8,_9,_a){
var _b=_8.cloneNode(true);
_b.innerHTML=_9;
_8.parentNode.appendChild(_b);
var _c=dojo.html.getElementBox(_b,_a);
_8.parentNode.removeChild(_b);
_b=null;
return _c;
};
dojo.html.getFittedFragment=function(_d,_e){
function cl(_f){
var _10=document.createElement(_f.tagName);
_10.id=_f.id+"-clone";
_10.className=_f.className;
for(var j=0;j<_f.attributes.length;j++){
if(_f.attributes[j].specified){
if(_f.attributes[j].nodeName.toLowerCase()!="style"&&_f.attributes[j].nodeName.toLowerCase()!="edited"&&_f.attributes[j].nodeName.toLowerCase()!="contenteditable"&&_f.attributes[j].nodeName.toLowerCase()!="id"&&_f.attributes[j].nodeName.toLowerCase()!="class"){
_10.setAttribute(_f.attributes[j].nodeName.toLowerCase(),_f.attributes[j].nodeValue);
}
}
}
return _10;
}
var _12=dojo.html.getFontMeasurements()["16px"];
var n=cl(_d);
n.style.width=dojo.html.getBorderBox(_d).width+"px";
n.style.height=(_12+4)+"px";
_d.parentNode.appendChild(n);
var rem=dojo.html.fitToElement(n,_e);
var ret=n.innerHTML;
n.parentNode.removeChild(n);
return ret;
};
dojo.html.fitToElement=function(_16,_17){
function cl(_18){
var _19=document.createElement(_18.tagName);
_19.id=_18.id+"-clone";
_19.className=_18.className;
for(var j=0;j<_18.attributes.length;j++){
if(_18.attributes[j].specified){
if(_18.attributes[j].nodeName.toLowerCase()!="style"&&_18.attributes[j].nodeName.toLowerCase()!="edited"&&_18.attributes[j].nodeName.toLowerCase()!="contenteditable"&&_18.attributes[j].nodeName.toLowerCase()!="id"&&_18.attributes[j].nodeName.toLowerCase()!="class"){
_19.setAttribute(_18.attributes[j].nodeName.toLowerCase(),_18.attributes[j].nodeValue);
}
}
}
return _19;
}
var _1b=cl(_16);
_16.parentNode.appendChild(_1b);
var t=dojo.html.getBorderBox(_16);
_1b.style.width=t.width+"px";
var _1d=["br","img","hr","input","!--"];
var _1e=["<BR>","<br>","<br/>","<br />","<p></p>","<P></P>"];
var _1f=[];
var str=_17;
var i=0;
var _22=str.length;
var add=0;
var _24=true;
_1b.innerHTML=str;
while(_24){
add=Math.round((_22-i)/2);
if(add<=1){
_24=false;
}
i+=add;
_1b.innerHTML=str.substr(0,i);
if(_1b.offsetHeight>t.height){
_22=i;
i-=add;
}
}
if(str.substr(0,i)!=str){
var _25=str.substr(0,i).lastIndexOf(" ");
var _26=str.substr(0,i).lastIndexOf("\n");
var _27=str.substr(0,i).lastIndexOf(">");
var _28=str.substr(0,i).lastIndexOf("<");
if(_28<=_27&&_26==i-1){
i=i;
}else{
if(_25!=-1&&_25>_27&&_27>_28){
i=_25+1;
}else{
if(_28>_27){
i=_28;
}else{
if(_27!=-1){
i=_27+1;
}
}
}
}
}
str=str.substr(0,i);
var ret=_17.substr(str.length);
var _2a=true;
var _2b=str.split("<");
_2b.shift();
for(var j=0;j<_2b.length;j++){
_2b[j]=_2b[j].split(">")[0];
if(_2b[j].charAt(_2b[j].length-1)=="/"){
continue;
}
if(_2b[j].charAt(0)!="/"){
for(var k=0;k<_1d.length;k++){
if(_2b[j].split(" ")[0].toLowerCase()==_1d[k]){
_2a=false;
}
}
if(_2a){
_1f.push(_2b[j]);
}
_2a=true;
}else{
_1f.pop();
}
}
for(var j=0;j<_1e.length;j++){
if(ret.charAt(0)=="\n"){
ret=ret.substr(1);
}
while(ret.indexOf(_1e[j])==0){
ret=ret.substr(_1e[j].length);
}
}
for(var j=_1f.length-1;j>=0;j--){
if(str.lastIndexOf(_1f[j])==(str.length-_1f[j].length-1)){
str=str.substring(0,str.lastIndexOf(_1f[j]));
}else{
str+="</"+_1f[j]+">";
}
if(ret.length>0){
ret="<"+_1f[j]+">"+ret;
}
}
for(var j=0;j<_1e.length;j++){
if(ret.charAt(0)=="\n"){
ret=ret.substr(1);
}
while(ret.indexOf(_1e[j])==0){
ret=ret.substr(_1e[j].length);
}
}
_16.innerHTML=str;
_1b.parentNode.removeChild(_1b);
_1b=null;
return ret;
};
