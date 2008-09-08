dojo.provide("dojo.io.XhrIframeProxy");
dojo.require("dojo.experimental");
dojo.experimental("dojo.io.XhrIframeProxy");
dojo.require("dojo.io.IframeIO");
dojo.require("dojo.dom");
dojo.require("dojo.uri.Uri");
dojo.io.XhrIframeProxy={xipClientUrl:djConfig["xipClientUrl"]||dojo.uri.moduleUri("dojo.io","xip_client.html"),_state:{},_stateIdCounter:0,needFrameRecursion:function(){
return (true==dojo.render.html.ie70);
},send:function(_1){
var _2="XhrIframeProxy"+(this._stateIdCounter++);
_1._stateId=_2;
var _3=this.xipClientUrl+"#0:init:id="+_2+"&server="+encodeURIComponent(_1._ifpServerUrl)+"&fr=false";
if(this.needFrameRecursion()){
var _4=window.location.href;
if((this.xipClientUrl+"").charAt(0)=="/"){
var _5=_4.indexOf("://");
_5=_4.indexOf("/",_5+1);
_4=_4.substring(0,_5);
}else{
_4=_4.substring(0,_4.lastIndexOf("/")+1);
}
_4+=this.xipClientUrl;
var _6=_1._ifpServerUrl+(_1._ifpServerUrl.indexOf("?")==-1?"?":"&")+"dojo.fr=1";
_3=_6+"#0:init:id="+_2+"&client="+encodeURIComponent(_4)+"&fr="+this.needFrameRecursion();
}
this._state[_2]={facade:_1,stateId:_2,clientFrame:dojo.io.createIFrame(_2,"",_3)};
return _2;
},receive:function(_7,_8){
var _9={};
var _a=_8.split("&");
for(var i=0;i<_a.length;i++){
if(_a[i]){
var _c=_a[i].split("=");
_9[decodeURIComponent(_c[0])]=decodeURIComponent(_c[1]);
}
}
var _d=this._state[_7];
var _e=_d.facade;
_e._setResponseHeaders(_9.responseHeaders);
if(_9.status==0||_9.status){
_e.status=parseInt(_9.status,10);
}
if(_9.statusText){
_e.statusText=_9.statusText;
}
if(_9.responseText){
_e.responseText=_9.responseText;
var _f=_e.getResponseHeader("Content-Type");
if(_f&&(_f=="application/xml"||_f=="text/xml")){
_e.responseXML=dojo.dom.createDocumentFromText(_9.responseText,_f);
}
}
_e.readyState=4;
this.destroyState(_7);
},clientFrameLoaded:function(_10){
var _11=this._state[_10];
var _12=_11.facade;
if(this.needFrameRecursion()){
var _13=window.open("",_11.stateId+"_clientEndPoint");
}else{
var _13=_11.clientFrame.contentWindow;
}
var _14=[];
for(var _15 in _12._requestHeaders){
_14.push(_15+": "+_12._requestHeaders[_15]);
}
var _16={uri:_12._uri};
if(_14.length>0){
_16.requestHeaders=_14.join("\r\n");
}
if(_12._method){
_16.method=_12._method;
}
if(_12._bodyData){
_16.data=_12._bodyData;
}
_13.send(dojo.io.argsFromMap(_16,"utf8"));
},destroyState:function(_17){
var _18=this._state[_17];
if(_18){
delete this._state[_17];
var _19=_18.clientFrame.parentNode;
_19.removeChild(_18.clientFrame);
_18.clientFrame=null;
_18=null;
}
},createFacade:function(){
if(arguments&&arguments[0]&&arguments[0]["iframeProxyUrl"]){
return new dojo.io.XhrIframeFacade(arguments[0]["iframeProxyUrl"]);
}else{
return dojo.io.XhrIframeProxy.oldGetXmlhttpObject.apply(dojo.hostenv,arguments);
}
}};
dojo.io.XhrIframeProxy.oldGetXmlhttpObject=dojo.hostenv.getXmlhttpObject;
dojo.hostenv.getXmlhttpObject=dojo.io.XhrIframeProxy.createFacade;
dojo.io.XhrIframeFacade=function(_1a){
this._requestHeaders={};
this._allResponseHeaders=null;
this._responseHeaders={};
this._method=null;
this._uri=null;
this._bodyData=null;
this.responseText=null;
this.responseXML=null;
this.status=null;
this.statusText=null;
this.readyState=0;
this._ifpServerUrl=_1a;
this._stateId=null;
};
dojo.lang.extend(dojo.io.XhrIframeFacade,{open:function(_1b,uri){
this._method=_1b;
this._uri=uri;
this.readyState=1;
},setRequestHeader:function(_1d,_1e){
this._requestHeaders[_1d]=_1e;
},send:function(_1f){
this._bodyData=_1f;
this._stateId=dojo.io.XhrIframeProxy.send(this);
this.readyState=2;
},abort:function(){
dojo.io.XhrIframeProxy.destroyState(this._stateId);
},getAllResponseHeaders:function(){
return this._allResponseHeaders;
},getResponseHeader:function(_20){
return this._responseHeaders[_20];
},_setResponseHeaders:function(_21){
if(_21){
this._allResponseHeaders=_21;
_21=_21.replace(/\r/g,"");
var _22=_21.split("\n");
for(var i=0;i<_22.length;i++){
if(_22[i]){
var _24=_22[i].split(": ");
this._responseHeaders[_24[0]]=_24[1];
}
}
}
}});
