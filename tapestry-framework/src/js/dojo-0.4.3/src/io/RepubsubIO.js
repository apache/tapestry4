dojo.require("dojo.event.*");
dojo.require("dojo.io.BrowserIO");
dojo.provide("dojo.io.RepubsubIO");
dojo.io.repubsubTranport=new function(){
var _1=dojo.io.repubsub;
this.canHandle=function(_2){
if((_2["mimetype"]=="text/javascript")&&(_2["method"]=="repubsub")){
return true;
}
return false;
};
this.bind=function(_3){
if(!_1.isInitialized){
_1.init();
}
if(!_1.topics[_3.url]){
_3.rpsLoad=function(_4){
_3.load("load",_4);
};
_1.subscribe(_3.url,_3,"rpsLoad");
}
if(_3["content"]){
var _5=dojo.io.repubsubEvent.initFromProperties(_3.content);
_1.publish(_3.url,_5);
}
};
dojo.io.transports.addTransport("repubsubTranport");
};
dojo.io.repubsub=new function(){
this.initDoc="init.html";
this.isInitialized=false;
this.subscriptionBacklog=[];
this.debug=true;
this.rcvNodeName=null;
this.sndNodeName=null;
this.rcvNode=null;
this.sndNode=null;
this.canRcv=false;
this.canSnd=false;
this.canLog=false;
this.sndTimer=null;
this.windowRef=window;
this.backlog=[];
this.tunnelInitCount=0;
this.tunnelFrameKey="tunnel_frame";
this.serverBaseURL=location.protocol+"//"+location.host+location.pathname;
this.logBacklog=[];
this.getRandStr=function(){
return Math.random().toString().substring(2,10);
};
this.userid="guest";
this.tunnelID=this.getRandStr();
this.attachPathList=[];
this.topics=[];
this.parseGetStr=function(){
var _6=document.location.toString();
var _7=_6.split("?",2);
if(_7.length>1){
var _8=_7[1];
var _9=_8.split("&");
var _a=[];
for(var x in _9){
var sp=_9[x].split("=");
try{
_a[sp[0]]=eval(sp[1]);
}
catch(e){
_a[sp[0]]=sp[1];
}
}
return _a;
}else{
return [];
}
};
var _d=this.parseGetStr();
for(var x in _d){
this[x]=_d[x];
}
if(!this["tunnelURI"]){
this.tunnelURI=["/who/",escape(this.userid),"/s/",this.getRandStr(),"/kn_journal"].join("");
}
if(window["repubsubOpts"]||window["rpsOpts"]){
var _f=window["repubsubOpts"]||window["rpsOpts"];
for(var x in _f){
this[x]=_f[x];
}
}
this.tunnelCloseCallback=function(){
dojo.io.setIFrameSrc(this.rcvNode,this.initDoc+"?callback=repubsub.rcvNodeReady&domain="+document.domain);
};
this.receiveEventFromTunnel=function(evt,_11){
if(!evt["elements"]){
this.log("bailing! event received without elements!","error");
return;
}
var e={};
for(var i=0;i<evt.elements.length;i++){
var ee=evt.elements[i];
e[ee.name||ee.nameU]=(ee.value||ee.valueU);
this.log("[event]: "+(ee.name||ee.nameU)+": "+e[ee.name||ee.nameU]);
}
this.dispatch(e);
};
this.widenDomain=function(_15){
var cd=_15||document.domain;
if(cd.indexOf(".")==-1){
return;
}
var dps=cd.split(".");
if(dps.length<=2){
return;
}
dps=dps.slice(dps.length-2);
document.domain=dps.join(".");
};
this.parseCookie=function(){
var cs=document.cookie;
var _19=cs.split(";");
for(var x=0;x<_19.length;x++){
_19[x]=_19[x].split("=");
if(x!=_19.length-1){
cs+=";";
}
}
return _19;
};
this.setCookie=function(_1b,_1c){
if((_1c)&&(_1c==true)){
document.cookie="";
}
var cs="";
for(var x=0;x<_1b.length;x++){
cs+=_1b[x][0]+"="+_1b[x][1];
if(x!=_1b.length-1){
cs+=";";
}
}
document.cookie=cs;
};
this.log=function(str,lvl){
if(!this.debug){
return;
}
while(this.logBacklog.length>0){
if(!this.canLog){
break;
}
var blo=this.logBacklog.shift();
this.writeLog("["+blo[0]+"]: "+blo[1],blo[2]);
}
this.writeLog(str,lvl);
};
this.writeLog=function(str,lvl){
dojo.debug(((new Date()).toLocaleTimeString())+": "+str);
};
this.init=function(){
this.widenDomain();
this.openTunnel();
this.isInitialized=true;
while(this.subscriptionBacklog.length){
this.subscribe.apply(this,this.subscriptionBacklog.shift());
}
};
this.clobber=function(){
if(this.rcvNode){
this.setCookie([[this.tunnelFrameKey,"closed"],["path","/"]],false);
}
};
this.openTunnel=function(){
this.rcvNodeName="rcvIFrame_"+this.getRandStr();
this.setCookie([[this.tunnelFrameKey,this.rcvNodeName],["path","/"]],false);
this.rcvNode=dojo.io.createIFrame(this.rcvNodeName);
dojo.io.setIFrameSrc(this.rcvNode,this.initDoc+"?callback=repubsub.rcvNodeReady&domain="+document.domain);
this.sndNodeName="sndIFrame_"+this.getRandStr();
this.sndNode=dojo.io.createIFrame(this.sndNodeName);
dojo.io.setIFrameSrc(this.sndNode,this.initDoc+"?callback=repubsub.sndNodeReady&domain="+document.domain);
};
this.rcvNodeReady=function(){
var _24=[this.tunnelURI,"/kn_status/",this.getRandStr(),"_",String(this.tunnelInitCount++)].join("");
this.log("rcvNodeReady");
var _25=[this.serverBaseURL,"/kn?kn_from=",escape(this.tunnelURI),"&kn_id=",escape(this.tunnelID),"&kn_status_from=",escape(_24)];
dojo.io.setIFrameSrc(this.rcvNode,_25.join(""));
this.subscribe(_24,this,"statusListener",true);
this.log(_25.join(""));
};
this.sndNodeReady=function(){
this.canSnd=true;
this.log("sndNodeReady");
this.log(this.backlog.length);
if(this.backlog.length>0){
this.dequeueEvent();
}
};
this.statusListener=function(evt){
this.log("status listener called");
this.log(evt.status,"info");
};
this.dispatch=function(evt){
if(evt["to"]||evt["kn_routed_from"]){
var rf=evt["to"]||evt["kn_routed_from"];
var _29=rf.split(this.serverBaseURL,2)[1];
if(!_29){
_29=rf;
}
this.log("[topic] "+_29);
if(_29.length>3){
if(_29.slice(0,3)=="/kn"){
_29=_29.slice(3);
}
}
if(this.attachPathList[_29]){
this.attachPathList[_29](evt);
}
}
};
this.subscribe=function(_2a,_2b,_2c,_2d){
if(!this.isInitialized){
this.subscriptionBacklog.push([_2a,_2b,_2c,_2d]);
return;
}
if(!this.attachPathList[_2a]){
this.attachPathList[_2a]=function(){
return true;
};
this.log("subscribing to: "+_2a);
this.topics.push(_2a);
}
var _2e=new dojo.io.repubsubEvent(this.tunnelURI,_2a,"route");
var _2f=[this.serverBaseURL+"/kn",_2e.toGetString()].join("");
dojo.event.kwConnect({once:true,srcObj:this.attachPathList,srcFunc:_2a,adviceObj:_2b,adviceFunc:_2c});
if(!this.rcvNode){
}
if(_2d){
return;
}
this.log("sending subscription to: "+_2a);
this.sendTopicSubToServer(_2a,_2f);
};
this.sendTopicSubToServer=function(_30,str){
if(!this.attachPathList[_30]["subscriptions"]){
this.enqueueEventStr(str);
this.attachPathList[_30].subscriptions=0;
}
this.attachPathList[_30].subscriptions++;
};
this.unSubscribe=function(_32,_33,_34){
dojo.event.kwDisconnect({srcObj:this.attachPathList,srcFunc:_32,adviceObj:_33,adviceFunc:_34});
};
this.publish=function(_35,_36){
var evt=dojo.io.repubsubEvent.initFromProperties(_36);
evt.to=_35;
var _38=[];
_38.push(this.serverBaseURL+"/kn");
_38.push(evt.toGetString());
this.enqueueEventStr(_38.join(""));
};
this.enqueueEventStr=function(_39){
this.log("enqueueEventStr");
this.backlog.push(_39);
this.dequeueEvent();
};
this.dequeueEvent=function(_3a){
this.log("dequeueEvent");
if(this.backlog.length<=0){
return;
}
if((this.canSnd)||(_3a)){
dojo.io.setIFrameSrc(this.sndNode,this.backlog.shift()+"&callback=repubsub.sndNodeReady");
this.canSnd=false;
}else{
this.log("sndNode not available yet!","debug");
}
};
};
dojo.io.repubsubEvent=function(to,_3c,_3d,id,_3f,_40,_41,uid){
this.to=to;
this.from=_3c;
this.method=_3d||"route";
this.id=id||repubsub.getRandStr();
this.uri=_3f;
this.displayname=_41||repubsub.displayname;
this.userid=uid||repubsub.userid;
this.payload=_40||"";
this.flushChars=4096;
this.initFromProperties=function(evt){
if(evt.constructor=dojo.io.repubsubEvent){
for(var x in evt){
this[x]=evt[x];
}
}else{
for(var x in evt){
if(typeof this.forwardPropertiesMap[x]=="string"){
this[this.forwardPropertiesMap[x]]=evt[x];
}else{
this[x]=evt[x];
}
}
}
};
this.toGetString=function(_45){
var qs=[((_45)?"":"?")];
for(var x=0;x<this.properties.length;x++){
var tp=this.properties[x];
if(this[tp[0]]){
qs.push(tp[1]+"="+encodeURIComponent(String(this[tp[0]])));
}
}
return qs.join("&");
};
};
dojo.io.repubsubEvent.prototype.properties=[["from","kn_from"],["to","kn_to"],["method","do_method"],["id","kn_id"],["uri","kn_uri"],["displayname","kn_displayname"],["userid","kn_userid"],["payload","kn_payload"],["flushChars","kn_response_flush"],["responseFormat","kn_response_format"]];
dojo.io.repubsubEvent.prototype.forwardPropertiesMap={};
dojo.io.repubsubEvent.prototype.reversePropertiesMap={};
for(var x=0;x<dojo.io.repubsubEvent.prototype.properties.length;x++){
var tp=dojo.io.repubsubEvent.prototype.properties[x];
dojo.io.repubsubEvent.prototype.reversePropertiesMap[tp[0]]=tp[1];
dojo.io.repubsubEvent.prototype.forwardPropertiesMap[tp[1]]=tp[0];
}
dojo.io.repubsubEvent.initFromProperties=function(evt){
var _4a=new dojo.io.repubsubEvent();
_4a.initFromProperties(evt);
return _4a;
};
