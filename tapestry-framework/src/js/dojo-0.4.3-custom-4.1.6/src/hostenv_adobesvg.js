if(typeof window=="undefined"){
dojo.raise("attempt to use adobe svg hostenv when no window object");
}
with(dojo.render){
name=navigator.appName;
ver=parseFloat(navigator.appVersion,10);
switch(navigator.platform){
case "MacOS":
os.osx=true;
break;
case "Linux":
os.linux=true;
break;
case "Windows":
os.win=true;
break;
default:
os.linux=true;
break;
}
svg.capable=true;
svg.support.builtin=true;
svg.adobe=true;
}
dojo.hostenv.println=function(s){
try{
var ti=document.createElement("text");
ti.setAttribute("x","50");
var _3=25+15*document.getElementsByTagName("text").length;
ti.setAttribute("y",_3);
var tn=document.createTextNode(s);
ti.appendChild(tn);
document.documentElement.appendChild(ti);
}
catch(e){
}
};
dojo.debug=function(){
if(!djConfig.isDebug){
return;
}
var _5=arguments;
if(typeof dojo.hostenv.println!="function"){
dojo.raise("attempt to call dojo.debug when there is no dojo.hostenv println implementation (yet?)");
}
var _6=dj_global["jum"];
var s=_6?"":"DEBUG: ";
for(var i=0;i<_5.length;++i){
s+=_5[i];
}
if(_6){
jum.debug(s);
}else{
dojo.hostenv.println(s);
}
};
dojo.hostenv.startPackage("dojo.hostenv");
dojo.hostenv.name_="adobesvg";
dojo.hostenv.anonCtr=0;
dojo.hostenv.anon={};
dojo.hostenv.nameAnonFunc=function(_9,_a){
var _b="_"+this.anonCtr++;
var _c=(_a||this.anon);
while(typeof _c[_b]!="undefined"){
_b="_"+this.anonCtr++;
}
_c[_b]=_9;
return _b;
};
dojo.hostenv.modulesLoadedFired=false;
dojo.hostenv.modulesLoadedListeners=[];
dojo.hostenv.getTextStack=[];
dojo.hostenv.loadUriStack=[];
dojo.hostenv.loadedUris=[];
dojo.hostenv.modulesLoaded=function(){
if(this.modulesLoadedFired){
return;
}
if((this.loadUriStack.length==0)&&(this.getTextStack.length==0)){
if(this.inFlightCount>0){
dojo.debug("couldn't initialize, there are files still in flight");
return;
}
this.modulesLoadedFired=true;
var _d=this.modulesLoadedListeners;
for(var x=0;x<_d.length;x++){
_d[x]();
}
}
};
dojo.hostenv.getNewAnonFunc=function(){
var _f="_"+this.anonCtr++;
while(typeof this.anon[_f]!="undefined"){
_f="_"+this.anonCtr++;
}
eval("dojo.nostenv.anon."+_f+" = function(){};");
return [_f,this.anon[_f]];
};
dojo.hostenv.displayStack=function(){
var oa=[];
var _11=this.loadUriStack;
for(var x=0;x<_11.length;x++){
oa.unshift([_11[x][0],(typeof _11[x][2])]);
}
dojo.debug("<pre>"+oa.join("\n")+"</pre>");
};
dojo.hostenv.unwindUriStack=function(){
var _13=this.loadUriStack;
for(var x in dojo.hostenv.loadedUris){
for(var y=_13.length-1;y>=0;y--){
if(_13[y][0]==x){
_13.splice(y,1);
}
}
}
var _16=_13.pop();
if((!_16)&&(_13.length==0)){
return;
}
for(var x=0;x<_13.length;x++){
if((_13[x][0]==_16[0])&&(_13[x][2])){
_16[2]==_13[x][2];
}
}
var _17=_16;
while(dojo.hostenv.loadedUris[_16[0]]){
_17=_16;
_16=_13.pop();
}
while(typeof _16[2]=="string"){
try{
dj_eval(_16[2]);
_16[1](true);
}
catch(e){
dojo.debug("we got an error when loading "+_16[0]);
dojo.debug("error: "+e);
}
dojo.hostenv.loadedUris[_16[0]]=true;
dojo.hostenv.loadedUris.push(_16[0]);
_17=_16;
_16=_13.pop();
if((!_16)&&(_13.length==0)){
break;
}
while(dojo.hostenv.loadedUris[_16[0]]){
_17=_16;
_16=_13.pop();
}
}
if(_16){
_13.push(_16);
dojo.debug("### CHOKED ON: "+_16[0]);
}
};
dojo.hostenv.loadUri=function(uri,cb){
if(dojo.hostenv.loadedUris[uri]){
return;
}
var _1a=this.loadUriStack;
_1a.push([uri,cb,null]);
var tcb=function(_1c){
if(_1c.content){
_1c=_1c.content;
}
var _1d=_1a.pop();
if((!_1d)&&(_1a.length==0)){
dojo.hostenv.modulesLoaded();
return;
}
if(typeof _1c=="string"){
_1a.push(_1d);
for(var x=0;x<_1a.length;x++){
if(_1a[x][0]==uri){
_1a[x][2]=_1c;
}
}
_1d=_1a.pop();
}
if(dojo.hostenv.loadedUris[_1d[0]]){
dojo.hostenv.unwindUriStack();
return;
}
_1a.push(_1d);
if(_1d[0]!=uri){
if(typeof _1d[2]=="string"){
dojo.hostenv.unwindUriStack();
}
}else{
if(!_1c){
_1d[1](false);
}else{
var _1f=dojo.hostenv.getDepsForEval(_1d[2]);
if(_1f.length>0){
eval(_1f.join(";"));
}else{
dojo.hostenv.unwindUriStack();
}
}
}
};
this.getText(uri,tcb,true);
};
dojo.hostenv.loadModule=function(_20,_21,_22){
var _23=this.findModule(_20,0);
if(_23){
return _23;
}
if(typeof this.loading_modules_[_20]!=="undefined"){
dojo.debug("recursive attempt to load module '"+_20+"'");
}else{
this.addedToLoadingCount.push(_20);
}
this.loading_modules_[_20]=1;
var _24=_20.replace(/\./g,"/")+".js";
var _25=_20.split(".");
var _26=_20.split(".");
if(_25[0]=="dojo"){
_25[0]="src";
}
var _27=_25.pop();
_25.push(_27);
var _28=this;
var pfn=this.pkgFileName;
if(_27=="*"){
_20=(_26.slice(0,-1)).join(".");
var _23=this.findModule(_20,0);
if(_23){
_28.removedFromLoadingCount.push(_20);
return _23;
}
var _2a=function(_2b){
if(_2b){
_23=_28.findModule(_20,false);
if((!_23)&&(_25[_25.length-1]!=pfn)){
dojo.raise("Module symbol '"+_20+"' is not defined after loading '"+_24+"'");
}
if(_23){
_28.removedFromLoadingCount.push(_20);
dojo.hostenv.modulesLoaded();
return;
}
}
_25.pop();
_25.push(pfn);
_24=_25.join("/")+".js";
if(_24.charAt(0)=="/"){
_24=_24.slice(1);
}
_28.loadPath(_24,((!_22)?_20:null),_2a);
};
_2a();
}else{
_24=_25.join("/")+".js";
_20=_26.join(".");
var _2a=function(_2c){
if(_2c){
_23=_28.findModule(_20,false);
if((!_23)&&(_25[_25.length-1]!=pfn)){
dojo.raise("Module symbol '"+_20+"' is not defined after loading '"+_24+"'");
}
if(_23){
_28.removedFromLoadingCount.push(_20);
dojo.hostenv.modulesLoaded();
return;
}
}
var _2d=(_25[_25.length-1]==pfn)?false:true;
_25.pop();
if(_2d){
_25.push(pfn);
}
_24=_25.join("/")+".js";
if(_24.charAt(0)=="/"){
_24=_24.slice(1);
}
_28.loadPath(_24,((!_22)?_20:null),_2a);
};
this.loadPath(_24,((!_22)?_20:null),_2a);
}
return;
};
dojo.hostenv.async_cb=null;
dojo.hostenv.unWindGetTextStack=function(){
if(dojo.hostenv.inFlightCount>0){
setTimeout("dojo.hostenv.unWindGetTextStack()",100);
return;
}
dojo.hostenv.inFlightCount++;
var _2e=dojo.hostenv.getTextStack.pop();
if((!_2e)&&(dojo.hostenv.getTextStack.length==0)){
dojo.hostenv.inFlightCount--;
dojo.hostenv.async_cb=function(){
};
return;
}
dojo.hostenv.async_cb=_2e[1];
window.getURL(_2e[0],function(_2f){
dojo.hostenv.inFlightCount--;
dojo.hostenv.async_cb(_2f.content);
dojo.hostenv.unWindGetTextStack();
});
};
dojo.hostenv.getText=function(uri,_31,_32){
try{
if(_31){
dojo.hostenv.getTextStack.push([uri,_31,_32]);
dojo.hostenv.unWindGetTextStack();
}else{
return dojo.raise("No synchronous XMLHTTP implementation available, for uri "+uri);
}
}
catch(e){
return dojo.raise("No XMLHTTP implementation available, for uri "+uri);
}
};
dojo.hostenv.postText=function(uri,_34,_35,_36,_37,_38){
var _39=null;
var _3a=function(_3b){
if(!_3b.success){
dojo.raise("Request for uri '"+uri+"' resulted in "+_3b.status);
}
if(!_3b.content){
if(!_36){
dojo.raise("Request for uri '"+uri+"' resulted in no content");
}
return null;
}
_34(_3b.content);
};
try{
if(_34){
_39=window.postURL(uri,_35,_3a,mimeType,_38);
}else{
return dojo.raise("No synchronous XMLHTTP post implementation available, for uri "+uri);
}
}
catch(e){
return dojo.raise("No XMLHTTP post implementation available, for uri "+uri);
}
};
function dj_last_script_src(){
var _3c=window.document.getElementsByTagName("script");
if(_3c.length<1){
dojo.raise("No script elements in window.document, so can't figure out my script src");
}
var li=_3c.length-1;
var _3e="http://www.w3.org/1999/xlink";
var src=null;
var _40=null;
while(!src){
_40=_3c.item(li);
src=_40.getAttributeNS(_3e,"href");
li--;
if(li<0){
break;
}
}
if(!src){
dojo.raise("Last script element (out of "+_3c.length+") has no src");
}
return src;
}
if(!dojo.hostenv["library_script_uri_"]){
dojo.hostenv.library_script_uri_=dj_last_script_src();
}
dojo.requireIf((djConfig["isDebug"]||djConfig["debugAtAllCosts"]),"dojo.debug");
