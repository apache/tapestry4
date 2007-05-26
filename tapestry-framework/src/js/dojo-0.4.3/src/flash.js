dojo.provide("dojo.flash");
dojo.require("dojo.string.*");
dojo.require("dojo.uri.*");
dojo.require("dojo.html.common");
dojo.flash=function(){
};
dojo.flash={flash6_version:null,flash8_version:null,ready:false,_visible:true,_loadedListeners:new Array(),_installingListeners:new Array(),setSwf:function(_1){
if(_1==null||dojo.lang.isUndefined(_1)){
return;
}
if(_1.flash6!=null&&!dojo.lang.isUndefined(_1.flash6)){
this.flash6_version=_1.flash6;
}
if(_1.flash8!=null&&!dojo.lang.isUndefined(_1.flash8)){
this.flash8_version=_1.flash8;
}
if(!dojo.lang.isUndefined(_1.visible)){
this._visible=_1.visible;
}
this._initialize();
},useFlash6:function(){
if(this.flash6_version==null){
return false;
}else{
if(this.flash6_version!=null&&dojo.flash.info.commVersion==6){
return true;
}else{
return false;
}
}
},useFlash8:function(){
if(this.flash8_version==null){
return false;
}else{
if(this.flash8_version!=null&&dojo.flash.info.commVersion==8){
return true;
}else{
return false;
}
}
},addLoadedListener:function(_2){
this._loadedListeners.push(_2);
},addInstallingListener:function(_3){
this._installingListeners.push(_3);
},loaded:function(){
dojo.flash.ready=true;
if(dojo.flash._loadedListeners.length>0){
for(var i=0;i<dojo.flash._loadedListeners.length;i++){
dojo.flash._loadedListeners[i].call(null);
}
}
},installing:function(){
if(dojo.flash._installingListeners.length>0){
for(var i=0;i<dojo.flash._installingListeners.length;i++){
dojo.flash._installingListeners[i].call(null);
}
}
},_initialize:function(){
var _6=new dojo.flash.Install();
dojo.flash.installer=_6;
if(_6.needed()==true){
_6.install();
}else{
dojo.flash.obj=new dojo.flash.Embed(this._visible);
dojo.flash.obj.write(dojo.flash.info.commVersion);
dojo.flash.comm=new dojo.flash.Communicator();
}
}};
dojo.flash.Info=function(){
if(dojo.render.html.ie){
document.writeln("<script language=\"VBScript\" type=\"text/vbscript\">");
document.writeln("Function VBGetSwfVer(i)");
document.writeln("  on error resume next");
document.writeln("  Dim swControl, swVersion");
document.writeln("  swVersion = 0");
document.writeln("  set swControl = CreateObject(\"ShockwaveFlash.ShockwaveFlash.\" + CStr(i))");
document.writeln("  if (IsObject(swControl)) then");
document.writeln("    swVersion = swControl.GetVariable(\"$version\")");
document.writeln("  end if");
document.writeln("  VBGetSwfVer = swVersion");
document.writeln("End Function");
document.writeln("</script>");
}
this._detectVersion();
this._detectCommunicationVersion();
};
dojo.flash.Info.prototype={version:-1,versionMajor:-1,versionMinor:-1,versionRevision:-1,capable:false,commVersion:6,installing:false,isVersionOrAbove:function(_7,_8,_9){
_9=parseFloat("."+_9);
if(this.versionMajor>=_7&&this.versionMinor>=_8&&this.versionRevision>=_9){
return true;
}else{
return false;
}
},_detectVersion:function(){
var _a;
for(var _b=25;_b>0;_b--){
if(dojo.render.html.ie){
_a=VBGetSwfVer(_b);
}else{
_a=this._JSFlashInfo(_b);
}
if(_a==-1){
this.capable=false;
return;
}else{
if(_a!=0){
var _c;
if(dojo.render.html.ie){
var _d=_a.split(" ");
var _e=_d[1];
_c=_e.split(",");
}else{
_c=_a.split(".");
}
this.versionMajor=_c[0];
this.versionMinor=_c[1];
this.versionRevision=_c[2];
var _f=this.versionMajor+"."+this.versionRevision;
this.version=parseFloat(_f);
this.capable=true;
break;
}
}
}
},_JSFlashInfo:function(_10){
if(navigator.plugins!=null&&navigator.plugins.length>0){
if(navigator.plugins["Shockwave Flash 2.0"]||navigator.plugins["Shockwave Flash"]){
var _11=navigator.plugins["Shockwave Flash 2.0"]?" 2.0":"";
var _12=navigator.plugins["Shockwave Flash"+_11].description;
var _13=_12.split(" ");
var _14=_13[2].split(".");
var _15=_14[0];
var _16=_14[1];
if(_13[3]!=""){
var _17=_13[3].split("r");
}else{
var _17=_13[4].split("r");
}
var _18=_17[1]>0?_17[1]:0;
var _19=_15+"."+_16+"."+_18;
return _19;
}
}
return -1;
},_detectCommunicationVersion:function(){
if(this.capable==false){
this.commVersion=null;
return;
}
if(typeof djConfig["forceFlashComm"]!="undefined"&&typeof djConfig["forceFlashComm"]!=null){
this.commVersion=djConfig["forceFlashComm"];
return;
}
if(dojo.render.html.safari==true||dojo.render.html.opera==true){
this.commVersion=8;
}else{
this.commVersion=6;
}
}};
dojo.flash.Embed=function(_1a){
this._visible=_1a;
};
dojo.flash.Embed.prototype={width:215,height:138,id:"flashObject",_visible:true,protocol:function(){
switch(window.location.protocol){
case "https:":
return "https";
break;
default:
return "http";
break;
}
},write:function(_1b,_1c){
if(dojo.lang.isUndefined(_1c)){
_1c=false;
}
var _1d=new dojo.string.Builder();
_1d.append("width: "+this.width+"px; ");
_1d.append("height: "+this.height+"px; ");
if(this._visible==false){
_1d.append("position: absolute; ");
_1d.append("z-index: 10000; ");
_1d.append("top: -1000px; ");
_1d.append("left: -1000px; ");
}
_1d=_1d.toString();
var _1e;
var _1f;
if(_1b==6){
_1f=dojo.flash.flash6_version;
var _20=djConfig.baseRelativePath;
_1f=_1f+"?baseRelativePath="+escape(_20);
_1e="<embed id=\""+this.id+"\" src=\""+_1f+"\" "+"    quality=\"high\" bgcolor=\"#ffffff\" "+"    width=\""+this.width+"\" height=\""+this.height+"\" "+"    name=\""+this.id+"\" "+"    align=\"middle\" allowScriptAccess=\"sameDomain\" "+"    type=\"application/x-shockwave-flash\" swLiveConnect=\"true\" "+"    pluginspage=\""+this.protocol()+"://www.macromedia.com/go/getflashplayer\">";
}else{
_1f=dojo.flash.flash8_version;
var _21=_1f;
var _22=_1f;
var _20=djConfig.baseRelativePath;
if(_1c){
var _23=escape(window.location);
document.title=document.title.slice(0,47)+" - Flash Player Installation";
var _24=escape(document.title);
_21+="?MMredirectURL="+_23+"&MMplayerType=ActiveX"+"&MMdoctitle="+_24+"&baseRelativePath="+escape(_20);
_22+="?MMredirectURL="+_23+"&MMplayerType=PlugIn"+"&baseRelativePath="+escape(_20);
}
if(_22.indexOf("?")==-1){
_22+="?baseRelativePath="+escape(_20)+"' ";
}
_1e="<object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" "+"codebase=\""+this.protocol()+"://fpdownload.macromedia.com/pub/shockwave/cabs/flash/"+"swflash.cab#version=8,0,0,0\" "+"width=\""+this.width+"\" "+"height=\""+this.height+"\" "+"id=\""+this.id+"\" "+"align=\"middle\"> "+"<param name=\"allowScriptAccess\" value=\"sameDomain\" /> "+"<param name=\"movie\" value=\""+_21+"\" /> "+"<param name=\"quality\" value=\"high\" /> "+"<param name=\"bgcolor\" value=\"#ffffff\" /> "+"<embed src=\""+_22+"' "+"quality=\"high\" "+"bgcolor=\"#ffffff\" "+"width=\""+this.width+"\" "+"height=\""+this.height+"\" "+"id=\""+this.id+"\" "+"name=\""+this.id+"\" "+"swLiveConnect=\"true\" "+"align=\"middle\" "+"allowScriptAccess=\"sameDomain\" "+"type=\"application/x-shockwave-flash\" "+"pluginspage=\""+this.protocol()+"://www.macromedia.com/go/getflashplayer\" />"+"</object>";
}
_1e="<div id=\""+this.id+"Container\" style=\""+_1d+"\"> "+_1e+"</div>";
document.writeln(_1e);
},get:function(){
return document.getElementById(this.id);
},setVisible:function(_25){
var _26=dojo.byId(this.id+"Container");
if(_25==true){
_26.style.visibility="visible";
}else{
_26.style.position="absolute";
_26.style.x="-1000px";
_26.style.y="-1000px";
_26.style.visibility="hidden";
}
},center:function(){
var _27=this.width;
var _28=this.height;
var _29=dojo.html.getScroll().offset;
var _2a=dojo.html.getViewport();
var x=_29.x+(_2a.width-_27)/2;
var y=_29.y+(_2a.height-_28)/2;
var _2d=dojo.byId(this.id+"Container");
_2d.style.top=y+"px";
_2d.style.left=x+"px";
}};
dojo.flash.Communicator=function(){
if(dojo.flash.useFlash6()){
this._writeFlash6();
}else{
if(dojo.flash.useFlash8()){
this._writeFlash8();
}
}
};
dojo.flash.Communicator.prototype={_writeFlash6:function(){
var id=dojo.flash.obj.id;
document.writeln("<script language=\"JavaScript\">");
document.writeln("  function "+id+"_DoFSCommand(command, args){ ");
document.writeln("    dojo.flash.comm._handleFSCommand(command, args); ");
document.writeln("}");
document.writeln("</script>");
if(dojo.render.html.ie){
document.writeln("<SCRIPT LANGUAGE=VBScript> ");
document.writeln("on error resume next ");
document.writeln("Sub "+id+"_FSCommand(ByVal command, ByVal args)");
document.writeln(" call "+id+"_DoFSCommand(command, args)");
document.writeln("end sub");
document.writeln("</SCRIPT> ");
}
},_writeFlash8:function(){
},_handleFSCommand:function(_2f,_30){
if(_2f!=null&&!dojo.lang.isUndefined(_2f)&&/^FSCommand:(.*)/.test(_2f)==true){
_2f=_2f.match(/^FSCommand:(.*)/)[1];
}
if(_2f=="addCallback"){
this._fscommandAddCallback(_2f,_30);
}else{
if(_2f=="call"){
this._fscommandCall(_2f,_30);
}else{
if(_2f=="fscommandReady"){
this._fscommandReady();
}
}
}
},_fscommandAddCallback:function(_31,_32){
var _33=_32;
var _34=function(){
return dojo.flash.comm._call(_33,arguments);
};
dojo.flash.comm[_33]=_34;
dojo.flash.obj.get().SetVariable("_succeeded",true);
},_fscommandCall:function(_35,_36){
var _37=dojo.flash.obj.get();
var _38=_36;
var _39=parseInt(_37.GetVariable("_numArgs"));
var _3a=new Array();
for(var i=0;i<_39;i++){
var _3c=_37.GetVariable("_"+i);
_3a.push(_3c);
}
var _3d;
if(_38.indexOf(".")==-1){
_3d=window[_38];
}else{
_3d=eval(_38);
}
var _3e=null;
if(!dojo.lang.isUndefined(_3d)&&_3d!=null){
_3e=_3d.apply(null,_3a);
}
_37.SetVariable("_returnResult",_3e);
},_fscommandReady:function(){
var _3f=dojo.flash.obj.get();
_3f.SetVariable("fscommandReady","true");
},_call:function(_40,_41){
var _42=dojo.flash.obj.get();
_42.SetVariable("_functionName",_40);
_42.SetVariable("_numArgs",_41.length);
for(var i=0;i<_41.length;i++){
var _44=_41[i];
_44=_44.replace(/\0/g,"\\0");
_42.SetVariable("_"+i,_44);
}
_42.TCallLabel("/_flashRunner","execute");
var _45=_42.GetVariable("_returnResult");
_45=_45.replace(/\\0/g,"\x00");
return _45;
},_addExternalInterfaceCallback:function(_46){
var _47=function(){
var _48=new Array(arguments.length);
for(var i=0;i<arguments.length;i++){
_48[i]=arguments[i];
}
return dojo.flash.comm._execFlash(_46,_48);
};
dojo.flash.comm[_46]=_47;
},_encodeData:function(_4a){
var _4b=/\&([^;]*)\;/g;
_4a=_4a.replace(_4b,"&amp;$1;");
_4a=_4a.replace(/</g,"&lt;");
_4a=_4a.replace(/>/g,"&gt;");
_4a=_4a.replace("\\","&custom_backslash;&custom_backslash;");
_4a=_4a.replace(/\n/g,"\\n");
_4a=_4a.replace(/\r/g,"\\r");
_4a=_4a.replace(/\f/g,"\\f");
_4a=_4a.replace(/\0/g,"\\0");
_4a=_4a.replace(/\'/g,"\\'");
_4a=_4a.replace(/\"/g,"\\\"");
return _4a;
},_decodeData:function(_4c){
if(_4c==null||typeof _4c=="undefined"){
return _4c;
}
_4c=_4c.replace(/\&custom_lt\;/g,"<");
_4c=_4c.replace(/\&custom_gt\;/g,">");
_4c=eval("\""+_4c+"\"");
return _4c;
},_chunkArgumentData:function(_4d,_4e){
var _4f=dojo.flash.obj.get();
var _50=Math.ceil(_4d.length/1024);
for(var i=0;i<_50;i++){
var _52=i*1024;
var _53=i*1024+1024;
if(i==(_50-1)){
_53=i*1024+_4d.length;
}
var _54=_4d.substring(_52,_53);
_54=this._encodeData(_54);
_4f.CallFunction("<invoke name=\"chunkArgumentData\" "+"returntype=\"javascript\">"+"<arguments>"+"<string>"+_54+"</string>"+"<number>"+_4e+"</number>"+"</arguments>"+"</invoke>");
}
},_chunkReturnData:function(){
var _55=dojo.flash.obj.get();
var _56=_55.getReturnLength();
var _57=new Array();
for(var i=0;i<_56;i++){
var _59=_55.CallFunction("<invoke name=\"chunkReturnData\" "+"returntype=\"javascript\">"+"<arguments>"+"<number>"+i+"</number>"+"</arguments>"+"</invoke>");
if(_59=="\"\""||_59=="''"){
_59="";
}else{
_59=_59.substring(1,_59.length-1);
}
_57.push(_59);
}
var _5a=_57.join("");
return _5a;
},_execFlash:function(_5b,_5c){
var _5d=dojo.flash.obj.get();
_5d.startExec();
_5d.setNumberArguments(_5c.length);
for(var i=0;i<_5c.length;i++){
this._chunkArgumentData(_5c[i],i);
}
_5d.exec(_5b);
var _5f=this._chunkReturnData();
_5f=this._decodeData(_5f);
_5d.endExec();
return _5f;
}};
dojo.flash.Install=function(){
};
dojo.flash.Install.prototype={needed:function(){
if(dojo.flash.info.capable==false){
return true;
}
if(dojo.render.os.mac==true&&!dojo.flash.info.isVersionOrAbove(8,0,0)){
return true;
}
if(!dojo.flash.info.isVersionOrAbove(6,0,0)){
return true;
}
return false;
},install:function(){
dojo.flash.info.installing=true;
dojo.flash.installing();
if(dojo.flash.info.capable==false){
var _60=new dojo.flash.Embed(false);
_60.write(8);
}else{
if(dojo.flash.info.isVersionOrAbove(6,0,65)){
var _60=new dojo.flash.Embed(false);
_60.write(8,true);
_60.setVisible(true);
_60.center();
}else{
alert("This content requires a more recent version of the Macromedia "+" Flash Player.");
window.location.href=+dojo.flash.Embed.protocol()+"://www.macromedia.com/go/getflashplayer";
}
}
},_onInstallStatus:function(msg){
if(msg=="Download.Complete"){
dojo.flash._initialize();
}else{
if(msg=="Download.Cancelled"){
alert("This content requires a more recent version of the Macromedia "+" Flash Player.");
window.location.href=dojo.flash.Embed.protocol()+"://www.macromedia.com/go/getflashplayer";
}else{
if(msg=="Download.Failed"){
alert("There was an error downloading the Flash Player update. "+"Please try again later, or visit macromedia.com to download "+"the latest version of the Flash plugin.");
}
}
}
}};
dojo.flash.info=new dojo.flash.Info();
