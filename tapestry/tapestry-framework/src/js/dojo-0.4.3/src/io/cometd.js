dojo.require("dojo.io.common");
dojo.provide("dojo.io.cometd");
dojo.require("dojo.AdapterRegistry");
dojo.require("dojo.json");
dojo.require("dojo.io.BrowserIO");
dojo.require("dojo.io.IframeIO");
dojo.require("dojo.io.ScriptSrcIO");
dojo.require("dojo.io.cookie");
dojo.require("dojo.event.*");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.func");
cometd=new function(){
this.initialized=false;
this.connected=false;
this.connectionTypes=new dojo.AdapterRegistry(true);
this.version=0.1;
this.minimumVersion=0.1;
this.clientId=null;
this._isXD=false;
this.handshakeReturn=null;
this.currentTransport=null;
this.url=null;
this.lastMessage=null;
this.globalTopicChannels={};
this.backlog=[];
this.tunnelInit=function(_1,_2){
};
this.tunnelCollapse=function(){
dojo.debug("tunnel collapsed!");
};
this.init=function(_3,_4,_5){
_3=_3||{};
_3.version=this.version;
_3.minimumVersion=this.minimumVersion;
_3.channel="/meta/handshake";
this.url=_4||djConfig["cometdRoot"];
if(!this.url){
dojo.debug("no cometd root specified in djConfig and no root passed");
return;
}
var _6={url:this.url,method:"POST",mimetype:"text/json",load:dojo.lang.hitch(this,"finishInit"),content:{"message":dojo.json.serialize([_3])}};
var _7="^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$";
var r=(""+window.location).match(new RegExp(_7));
if(r[4]){
var _9=r[4].split(":");
var _a=_9[0];
var _b=_9[1]||"80";
r=this.url.match(new RegExp(_7));
if(r[4]){
_9=r[4].split(":");
var _c=_9[0];
var _d=_9[1]||"80";
if((_c!=_a)||(_d!=_b)){
dojo.debug(_a,_c);
dojo.debug(_b,_d);
this._isXD=true;
_6.transport="ScriptSrcTransport";
_6.jsonParamName="jsonp";
_6.method="GET";
}
}
}
if(_5){
dojo.lang.mixin(_6,_5);
}
return dojo.io.bind(_6);
};
this.finishInit=function(_e,_f,evt,_11){
_f=_f[0];
this.handshakeReturn=_f;
if(_f["authSuccessful"]==false){
dojo.debug("cometd authentication failed");
return;
}
if(_f.version<this.minimumVersion){
dojo.debug("cometd protocol version mismatch. We wanted",this.minimumVersion,"but got",_f.version);
return;
}
this.currentTransport=this.connectionTypes.match(_f.supportedConnectionTypes,_f.version,this._isXD);
this.currentTransport.version=_f.version;
this.clientId=_f.clientId;
this.tunnelInit=dojo.lang.hitch(this.currentTransport,"tunnelInit");
this.tunnelCollapse=dojo.lang.hitch(this.currentTransport,"tunnelCollapse");
this.initialized=true;
this.currentTransport.startup(_f);
while(this.backlog.length!=0){
var cur=this.backlog.shift();
var fn=cur.shift();
this[fn].apply(this,cur);
}
};
this._getRandStr=function(){
return Math.random().toString().substring(2,10);
};
this.deliver=function(_14){
dojo.lang.forEach(_14,this._deliver,this);
};
this._deliver=function(_15){
if(!_15["channel"]){
dojo.debug("cometd error: no channel for message!");
return;
}
if(!this.currentTransport){
this.backlog.push(["deliver",_15]);
return;
}
this.lastMessage=_15;
if((_15.channel.length>5)&&(_15.channel.substr(0,5)=="/meta")){
switch(_15.channel){
case "/meta/subscribe":
if(!_15.successful){
dojo.debug("cometd subscription error for channel",_15.channel,":",_15.error);
return;
}
this.subscribed(_15.subscription,_15);
break;
case "/meta/unsubscribe":
if(!_15.successful){
dojo.debug("cometd unsubscription error for channel",_15.channel,":",_15.error);
return;
}
this.unsubscribed(_15.subscription,_15);
break;
}
}
this.currentTransport.deliver(_15);
if(_15.data){
var _16=(this.globalTopicChannels[_15.channel])?_15.channel:"/cometd"+_15.channel;
dojo.event.topic.publish(_16,_15);
}
};
this.disconnect=function(){
if(!this.currentTransport){
dojo.debug("no current transport to disconnect from");
return;
}
this.currentTransport.disconnect();
};
this.publish=function(_17,_18,_19){
if(!this.currentTransport){
this.backlog.push(["publish",_17,_18,_19]);
return;
}
var _1a={data:_18,channel:_17};
if(_19){
dojo.lang.mixin(_1a,_19);
}
return this.currentTransport.sendMessage(_1a);
};
this.subscribe=function(_1b,_1c,_1d,_1e){
if(!this.currentTransport){
this.backlog.push(["subscribe",_1b,_1c,_1d,_1e]);
return;
}
if(_1d){
var _1f=(_1c)?_1b:"/cometd"+_1b;
if(_1c){
this.globalTopicChannels[_1b]=true;
}
dojo.event.topic.subscribe(_1f,_1d,_1e);
}
return this.currentTransport.sendMessage({channel:"/meta/subscribe",subscription:_1b});
};
this.subscribed=function(_20,_21){
dojo.debug(_20);
dojo.debugShallow(_21);
};
this.unsubscribe=function(_22,_23,_24,_25){
if(!this.currentTransport){
this.backlog.push(["unsubscribe",_22,_23,_24,_25]);
return;
}
if(_24){
var _26=(_23)?_22:"/cometd"+_22;
dojo.event.topic.unsubscribe(_26,_24,_25);
}
return this.currentTransport.sendMessage({channel:"/meta/unsubscribe",subscription:_22});
};
this.unsubscribed=function(_27,_28){
dojo.debug(_27);
dojo.debugShallow(_28);
};
};
cometd.iframeTransport=new function(){
this.connected=false;
this.connectionId=null;
this.rcvNode=null;
this.rcvNodeName="";
this.phonyForm=null;
this.authToken=null;
this.lastTimestamp=null;
this.lastId=null;
this.backlog=[];
this.check=function(_29,_2a,_2b){
return ((!_2b)&&(!dojo.render.html.safari)&&(dojo.lang.inArray(_29,"iframe")));
};
this.tunnelInit=function(){
this.postToIframe({message:dojo.json.serialize([{channel:"/meta/connect",clientId:cometd.clientId,connectionType:"iframe"}])});
};
this.tunnelCollapse=function(){
if(this.connected){
this.connected=false;
this.postToIframe({message:dojo.json.serialize([{channel:"/meta/reconnect",clientId:cometd.clientId,connectionId:this.connectionId,timestamp:this.lastTimestamp,id:this.lastId}])});
}
};
this.deliver=function(_2c){
if(_2c["timestamp"]){
this.lastTimestamp=_2c.timestamp;
}
if(_2c["id"]){
this.lastId=_2c.id;
}
if((_2c.channel.length>5)&&(_2c.channel.substr(0,5)=="/meta")){
switch(_2c.channel){
case "/meta/connect":
if(!_2c.successful){
dojo.debug("cometd connection error:",_2c.error);
return;
}
this.connectionId=_2c.connectionId;
this.connected=true;
this.processBacklog();
break;
case "/meta/reconnect":
if(!_2c.successful){
dojo.debug("cometd reconnection error:",_2c.error);
return;
}
this.connected=true;
break;
case "/meta/subscribe":
if(!_2c.successful){
dojo.debug("cometd subscription error for channel",_2c.channel,":",_2c.error);
return;
}
dojo.debug(_2c.channel);
break;
}
}
};
this.widenDomain=function(_2d){
var cd=_2d||document.domain;
if(cd.indexOf(".")==-1){
return;
}
var dps=cd.split(".");
if(dps.length<=2){
return;
}
dps=dps.slice(dps.length-2);
document.domain=dps.join(".");
return document.domain;
};
this.postToIframe=function(_30,url){
if(!this.phonyForm){
if(dojo.render.html.ie){
this.phonyForm=document.createElement("<form enctype='application/x-www-form-urlencoded' method='POST' style='display: none;'>");
dojo.body().appendChild(this.phonyForm);
}else{
this.phonyForm=document.createElement("form");
this.phonyForm.style.display="none";
dojo.body().appendChild(this.phonyForm);
this.phonyForm.enctype="application/x-www-form-urlencoded";
this.phonyForm.method="POST";
}
}
this.phonyForm.action=url||cometd.url;
this.phonyForm.target=this.rcvNodeName;
this.phonyForm.setAttribute("target",this.rcvNodeName);
while(this.phonyForm.firstChild){
this.phonyForm.removeChild(this.phonyForm.firstChild);
}
for(var x in _30){
var tn;
if(dojo.render.html.ie){
tn=document.createElement("<input type='hidden' name='"+x+"' value='"+_30[x]+"'>");
this.phonyForm.appendChild(tn);
}else{
tn=document.createElement("input");
this.phonyForm.appendChild(tn);
tn.type="hidden";
tn.name=x;
tn.value=_30[x];
}
}
this.phonyForm.submit();
};
this.processBacklog=function(){
while(this.backlog.length>0){
this.sendMessage(this.backlog.shift(),true);
}
};
this.sendMessage=function(_34,_35){
if((_35)||(this.connected)){
_34.connectionId=this.connectionId;
_34.clientId=cometd.clientId;
var _36={url:cometd.url||djConfig["cometdRoot"],method:"POST",mimetype:"text/json",content:{message:dojo.json.serialize([_34])}};
return dojo.io.bind(_36);
}else{
this.backlog.push(_34);
}
};
this.startup=function(_37){
dojo.debug("startup!");
dojo.debug(dojo.json.serialize(_37));
if(this.connected){
return;
}
this.rcvNodeName="cometdRcv_"+cometd._getRandStr();
var _38=cometd.url+"/?tunnelInit=iframe";
if(false&&dojo.render.html.ie){
this.rcvNode=new ActiveXObject("htmlfile");
this.rcvNode.open();
this.rcvNode.write("<html>");
this.rcvNode.write("<script>document.domain = '"+document.domain+"'");
this.rcvNode.write("</html>");
this.rcvNode.close();
var _39=this.rcvNode.createElement("div");
this.rcvNode.appendChild(_39);
this.rcvNode.parentWindow.dojo=dojo;
_39.innerHTML="<iframe src='"+_38+"'></iframe>";
}else{
this.rcvNode=dojo.io.createIFrame(this.rcvNodeName,"",_38);
}
};
};
cometd.mimeReplaceTransport=new function(){
this.connected=false;
this.connectionId=null;
this.xhr=null;
this.authToken=null;
this.lastTimestamp=null;
this.lastId=null;
this.backlog=[];
this.check=function(_3a,_3b,_3c){
return ((!_3c)&&(dojo.render.html.mozilla)&&(dojo.lang.inArray(_3a,"mime-message-block")));
};
this.tunnelInit=function(){
if(this.connected){
return;
}
this.openTunnelWith({message:dojo.json.serialize([{channel:"/meta/connect",clientId:cometd.clientId,connectionType:"mime-message-block"}])});
this.connected=true;
};
this.tunnelCollapse=function(){
if(this.connected){
this.connected=false;
this.openTunnelWith({message:dojo.json.serialize([{channel:"/meta/reconnect",clientId:cometd.clientId,connectionId:this.connectionId,timestamp:this.lastTimestamp,id:this.lastId}])});
}
};
this.deliver=cometd.iframeTransport.deliver;
this.handleOnLoad=function(_3d){
cometd.deliver(dojo.json.evalJson(this.xhr.responseText));
};
this.openTunnelWith=function(_3e,url){
this.xhr=dojo.hostenv.getXmlhttpObject();
this.xhr.multipart=true;
if(dojo.render.html.mozilla){
this.xhr.addEventListener("load",dojo.lang.hitch(this,"handleOnLoad"),false);
}else{
if(dojo.render.html.safari){
dojo.debug("Webkit is broken with multipart responses over XHR = (");
this.xhr.onreadystatechange=dojo.lang.hitch(this,"handleOnLoad");
}else{
this.xhr.onload=dojo.lang.hitch(this,"handleOnLoad");
}
}
this.xhr.open("POST",(url||cometd.url),true);
this.xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
dojo.debug(dojo.json.serialize(_3e));
this.xhr.send(dojo.io.argsFromMap(_3e,"utf8"));
};
this.processBacklog=function(){
while(this.backlog.length>0){
this.sendMessage(this.backlog.shift(),true);
}
};
this.sendMessage=function(_40,_41){
if((_41)||(this.connected)){
_40.connectionId=this.connectionId;
_40.clientId=cometd.clientId;
var _42={url:cometd.url||djConfig["cometdRoot"],method:"POST",mimetype:"text/json",content:{message:dojo.json.serialize([_40])}};
return dojo.io.bind(_42);
}else{
this.backlog.push(_40);
}
};
this.startup=function(_43){
dojo.debugShallow(_43);
if(this.connected){
return;
}
this.tunnelInit();
};
};
cometd.longPollTransport=new function(){
this.connected=false;
this.connectionId=null;
this.authToken=null;
this.lastTimestamp=null;
this.lastId=null;
this.backlog=[];
this.check=function(_44,_45,_46){
return ((!_46)&&(dojo.lang.inArray(_44,"long-polling")));
};
this.tunnelInit=function(){
if(this.connected){
return;
}
this.openTunnelWith({message:dojo.json.serialize([{channel:"/meta/connect",clientId:cometd.clientId,connectionType:"long-polling"}])});
this.connected=true;
};
this.tunnelCollapse=function(){
if(!this.connected){
this.connected=false;
dojo.debug("clientId:",cometd.clientId);
this.openTunnelWith({message:dojo.json.serialize([{channel:"/meta/reconnect",connectionType:"long-polling",clientId:cometd.clientId,connectionId:this.connectionId,timestamp:this.lastTimestamp,id:this.lastId}])});
}
};
this.deliver=cometd.iframeTransport.deliver;
this.openTunnelWith=function(_47,url){
dojo.io.bind({url:(url||cometd.url),method:"post",content:_47,mimetype:"text/json",load:dojo.lang.hitch(this,function(_49,_4a,evt,_4c){
cometd.deliver(_4a);
this.connected=false;
this.tunnelCollapse();
}),error:function(){
dojo.debug("tunnel opening failed");
}});
this.connected=true;
};
this.processBacklog=function(){
while(this.backlog.length>0){
this.sendMessage(this.backlog.shift(),true);
}
};
this.sendMessage=function(_4d,_4e){
if((_4e)||(this.connected)){
_4d.connectionId=this.connectionId;
_4d.clientId=cometd.clientId;
var _4f={url:cometd.url||djConfig["cometdRoot"],method:"post",mimetype:"text/json",content:{message:dojo.json.serialize([_4d])},load:dojo.lang.hitch(this,function(_50,_51,evt,_53){
cometd.deliver(_51);
})};
return dojo.io.bind(_4f);
}else{
this.backlog.push(_4d);
}
};
this.startup=function(_54){
if(this.connected){
return;
}
this.tunnelInit();
};
};
cometd.callbackPollTransport=new function(){
this.connected=false;
this.connectionId=null;
this.authToken=null;
this.lastTimestamp=null;
this.lastId=null;
this.backlog=[];
this.check=function(_55,_56,_57){
return dojo.lang.inArray(_55,"callback-polling");
};
this.tunnelInit=function(){
if(this.connected){
return;
}
this.openTunnelWith({message:dojo.json.serialize([{channel:"/meta/connect",clientId:cometd.clientId,connectionType:"callback-polling"}])});
this.connected=true;
};
this.tunnelCollapse=function(){
if(!this.connected){
this.connected=false;
this.openTunnelWith({message:dojo.json.serialize([{channel:"/meta/reconnect",connectionType:"long-polling",clientId:cometd.clientId,connectionId:this.connectionId,timestamp:this.lastTimestamp,id:this.lastId}])});
}
};
this.deliver=cometd.iframeTransport.deliver;
this.openTunnelWith=function(_58,url){
var req=dojo.io.bind({url:(url||cometd.url),content:_58,mimetype:"text/json",transport:"ScriptSrcTransport",jsonParamName:"jsonp",load:dojo.lang.hitch(this,function(_5b,_5c,evt,_5e){
cometd.deliver(_5c);
this.connected=false;
this.tunnelCollapse();
}),error:function(){
dojo.debug("tunnel opening failed");
}});
this.connected=true;
};
this.processBacklog=function(){
while(this.backlog.length>0){
this.sendMessage(this.backlog.shift(),true);
}
};
this.sendMessage=function(_5f,_60){
if((_60)||(this.connected)){
_5f.connectionId=this.connectionId;
_5f.clientId=cometd.clientId;
var _61={url:cometd.url||djConfig["cometdRoot"],mimetype:"text/json",transport:"ScriptSrcTransport",jsonParamName:"jsonp",content:{message:dojo.json.serialize([_5f])},load:dojo.lang.hitch(this,function(_62,_63,evt,_65){
cometd.deliver(_63);
})};
return dojo.io.bind(_61);
}else{
this.backlog.push(_5f);
}
};
this.startup=function(_66){
if(this.connected){
return;
}
this.tunnelInit();
};
};
cometd.connectionTypes.register("mime-message-block",cometd.mimeReplaceTransport.check,cometd.mimeReplaceTransport);
cometd.connectionTypes.register("long-polling",cometd.longPollTransport.check,cometd.longPollTransport);
cometd.connectionTypes.register("callback-polling",cometd.callbackPollTransport.check,cometd.callbackPollTransport);
cometd.connectionTypes.register("iframe",cometd.iframeTransport.check,cometd.iframeTransport);
dojo.io.cometd=cometd;
