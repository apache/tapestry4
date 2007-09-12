dojo.provide("dojo.undo.browser");
dojo.require("dojo.io.common");
try{
if((!djConfig["preventBackButtonFix"])&&(!dojo.hostenv.post_load_)){
document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(djConfig["dojoIframeHistoryUrl"]||dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"'></iframe>");
}
}
catch(e){
}
if(dojo.render.html.opera){
dojo.debug("Opera is not supported with dojo.undo.browser, so back/forward detection will not work.");
}
dojo.undo.browser={initialHref:(!dj_undef("window"))?window.location.href:"",initialHash:(!dj_undef("window"))?window.location.hash:"",moveForward:false,historyStack:[],forwardStack:[],historyIframe:null,bookmarkAnchor:null,locationTimer:null,setInitialState:function(_1){
this.initialState=this._createState(this.initialHref,_1,this.initialHash);
},addToHistory:function(_2){
this.forwardStack=[];
var _3=null;
var _4=null;
if(!this.historyIframe){
if(djConfig["useXDomain"]&&!djConfig["dojoIframeHistoryUrl"]){
dojo.debug("dojo.undo.browser: When using cross-domain Dojo builds,"+" please save iframe_history.html to your domain and set djConfig.dojoIframeHistoryUrl"+" to the path on your domain to iframe_history.html");
}
this.historyIframe=window.frames["djhistory"];
}
if(!this.bookmarkAnchor){
this.bookmarkAnchor=document.createElement("a");
dojo.body().appendChild(this.bookmarkAnchor);
this.bookmarkAnchor.style.display="none";
}
if(_2["changeUrl"]){
_3="#"+((_2["changeUrl"]!==true)?_2["changeUrl"]:(new Date()).getTime());
if(this.historyStack.length==0&&this.initialState.urlHash==_3){
this.initialState=this._createState(_4,_2,_3);
return;
}else{
if(this.historyStack.length>0&&this.historyStack[this.historyStack.length-1].urlHash==_3){
this.historyStack[this.historyStack.length-1]=this._createState(_4,_2,_3);
return;
}
}
this.changingUrl=true;
setTimeout("window.location.href = '"+_3+"'; dojo.undo.browser.changingUrl = false;",1);
this.bookmarkAnchor.href=_3;
if(dojo.render.html.ie){
_4=this._loadIframeHistory();
var _5=_2["back"]||_2["backButton"]||_2["handle"];
var _6=function(_7){
if(window.location.hash!=""){
setTimeout("window.location.href = '"+_3+"';",1);
}
_5.apply(this,[_7]);
};
if(_2["back"]){
_2.back=_6;
}else{
if(_2["backButton"]){
_2.backButton=_6;
}else{
if(_2["handle"]){
_2.handle=_6;
}
}
}
var _8=_2["forward"]||_2["forwardButton"]||_2["handle"];
var _9=function(_a){
if(window.location.hash!=""){
window.location.href=_3;
}
if(_8){
_8.apply(this,[_a]);
}
};
if(_2["forward"]){
_2.forward=_9;
}else{
if(_2["forwardButton"]){
_2.forwardButton=_9;
}else{
if(_2["handle"]){
_2.handle=_9;
}
}
}
}else{
if(dojo.render.html.moz){
if(!this.locationTimer){
this.locationTimer=setInterval("dojo.undo.browser.checkLocation();",200);
}
}
}
}else{
_4=this._loadIframeHistory();
}
this.historyStack.push(this._createState(_4,_2,_3));
},checkLocation:function(){
if(!this.changingUrl){
var _b=this.historyStack.length;
if((window.location.hash==this.initialHash||window.location.href==this.initialHref)&&(_b==1)){
this.handleBackButton();
return;
}
if(this.forwardStack.length>0){
if(this.forwardStack[this.forwardStack.length-1].urlHash==window.location.hash){
this.handleForwardButton();
return;
}
}
if((_b>=2)&&(this.historyStack[_b-2])){
if(this.historyStack[_b-2].urlHash==window.location.hash){
this.handleBackButton();
return;
}
}
}
},iframeLoaded:function(_c,_d){
if(!dojo.render.html.opera){
var _e=this._getUrlQuery(_d.href);
if(_e==null){
if(this.historyStack.length==1){
this.handleBackButton();
}
return;
}
if(this.moveForward){
this.moveForward=false;
return;
}
if(this.historyStack.length>=2&&_e==this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){
this.handleBackButton();
}else{
if(this.forwardStack.length>0&&_e==this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){
this.handleForwardButton();
}
}
}
},handleBackButton:function(){
var _f=this.historyStack.pop();
if(!_f){
return;
}
var _10=this.historyStack[this.historyStack.length-1];
if(!_10&&this.historyStack.length==0){
_10=this.initialState;
}
if(_10){
if(_10.kwArgs["back"]){
_10.kwArgs["back"]();
}else{
if(_10.kwArgs["backButton"]){
_10.kwArgs["backButton"]();
}else{
if(_10.kwArgs["handle"]){
_10.kwArgs.handle("back");
}
}
}
}
this.forwardStack.push(_f);
},handleForwardButton:function(){
var _11=this.forwardStack.pop();
if(!_11){
return;
}
if(_11.kwArgs["forward"]){
_11.kwArgs.forward();
}else{
if(_11.kwArgs["forwardButton"]){
_11.kwArgs.forwardButton();
}else{
if(_11.kwArgs["handle"]){
_11.kwArgs.handle("forward");
}
}
}
this.historyStack.push(_11);
},_createState:function(url,_13,_14){
return {"url":url,"kwArgs":_13,"urlHash":_14};
},_getUrlQuery:function(url){
var _16=url.split("?");
if(_16.length<2){
return null;
}else{
return _16[1];
}
},_loadIframeHistory:function(){
var url=(djConfig["dojoIframeHistoryUrl"]||dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"?"+(new Date()).getTime();
this.moveForward=true;
dojo.io.setIFrameSrc(this.historyIframe,url,false);
return url;
}};
