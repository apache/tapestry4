dojo.provide("dojo.widget.html.loader");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.io.*");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.extras");
dojo.require("dojo.experimental");
dojo.experimental("dojo.widget.html.loader");
dojo.widget.html.loader=new (function(){
this.toString=function(){
return "dojo.widget.html.loader";
};
var _1=this;
dojo.addOnLoad(function(){
dojo.experimental(_1.toString());
var _2=dojo.evalObjPath("dojo.undo.browser");
if(djConfig["preventBackButtonFix"]&&_2&&!_2.initialState){
_2.setInitialState(new _3);
}
});
var _4={};
var _3=function(id,_6){
this.id=id;
this.data=_6;
};
_3.prototype.handle=function(_7){
if(typeof dojo=="undefined"){
return;
}
var wg=dojo.widget.byId(this.id);
if(wg){
wg.setContent(this.data,true);
}
};
this._log=function(_9,_a){
if(_9.trackHistory){
if(!_4[_9.widgetId]){
_4[_9.widgetId]={childrenIds:[],stack:[_a]};
}
var _b=_4[_9.widgetId].childrenIds;
while(_b&&_b.length){
delete _4[_b.pop()];
}
for(var _c in _9.children){
_4[_9.widgetId].childrenIds=_c.widgetId;
}
dojo.undo.browser.addToHistory(new _3(_9.widgetId,dojo.lang.shallowCopy(_a,true)));
}
};
var _d=dojo.lang.isUndefined;
var _e=dojo.lang.isFunction;
function handleDefaults(e,_10,_11){
if(!_10){
_10="onContentError";
}
if(dojo.lang.isString(e)){
e={_text:e};
}
if(!e._text){
e._text=e.toString();
}
e.toString=function(){
return this._text;
};
if(typeof e.returnValue!="boolean"){
e.returnValue=true;
}
if(typeof e.preventDefault!="function"){
e.preventDefault=function(){
this.returnValue=false;
};
}
this[_10](e);
if(e.returnValue){
if(_11){
alert(e.toString());
}else{
this.loader.callOnUnLoad.call(this,false);
this.onSetContent(e.toString());
}
}
}
function downloader(_12){
for(var x in this.bindArgs){
_12[x]=(_d(_12[x])?this.bindArgs[x]:undefined);
}
var _14=this.cacheContent;
if(_d(_12.useCache)){
_12.useCache=_14;
}
if(_d(_12.preventCache)){
_12.preventCache=!_14;
}
if(_d(_12.mimetype)){
_12.mimetype="text/html";
}
this.loader.bindObj=dojo.io.bind(_12);
}
function stackRunner(st){
var err="",_17=null;
var _18=this.scriptScope||dojo.global();
while(st.length){
_17=st.shift();
try{
_17.call(_18);
}
catch(e){
err+="\n"+_17+" failed: "+e;
}
}
if(err.length){
var _19=(st==this.loader.addOnLoads)?"addOnLoad":"addOnUnLoad";
handleDefaults.call(this,_19+" failure\n "+err,"onExecError",true);
}
}
function stackPusher(st,obj,_1c){
if(typeof _1c=="undefined"){
st.push(obj);
}else{
st.push(function(){
obj[_1c]();
});
}
}
function refreshed(){
this.onResized();
this.onLoad();
this.isLoaded=true;
}
function asyncParse(_1d){
if(this.executeScripts){
this.onExecScript.call(this,_1d.scripts);
}
if(this.parseContent){
this.onContentParse.call(this);
}
refreshed.call(this);
}
function runHandler(){
if(dojo.lang.isFunction(this.handler)){
this.handler(this,this.containerNode||this.domNode);
refreshed.call(this);
return false;
}
return true;
}
this.htmlContentBasicFix=function(s,url){
var _20=[],_21=[];
var _22=/<title[^>]*>([\s\S]*?)<\/title>/i;
var _23,_24;
while(_23=_22.exec(s)){
_20.push(_23[1]);
s=s.substring(0,_23.index)+s.substr(_23.index+_23[0].length);
}
_22=/(?:<(style)[^>]*>([\s\S]*?)<\/style>|<link ([^>]*rel=['"]?stylesheet['"]?[^>]*)>)/i;
while(_23=_22.exec(s)){
if(_23[1]&&_23[1].toLowerCase()=="style"){
_21.push(dojo.html.fixPathsInCssText(_23[2],url));
}else{
if(_24=_23[3].match(/href=(['"]?)([^'">]*)\1/i)){
_21.push({path:_24[2]});
}
}
s=s.substring(0,_23.index)+s.substr(_23.index+_23[0].length);
}
return {"s":s,"titles":_20,"styles":_21};
};
this.htmlContentAdjustPaths=function(s,url){
var tag="",str="",_29="",_2a="";
var _2b=[],_2c="",fix="";
var _2e=/<[a-z][a-z0-9]*[^>]*\s(?:(?:src|href|style)=[^>])+[^>]*>/i;
var _2f=/\s(src|href|style)=(['"]?)([\w()\[\]\/.,\\'"-:;#=&?\s@]+?)\2/i;
var _30=/^(?:[#]|(?:(?:https?|ftps?|file|javascript|mailto|news):))/;
while(tag=_2e.exec(s)){
str+=s.substring(0,tag.index);
s=s.substring((tag.index+tag[0].length),s.length);
tag=tag[0];
_29="";
while(_2b=_2f.exec(tag)){
_2a="";
_2c=_2b[3];
switch(_2b[1].toLowerCase()){
case "src":
case "href":
if(_30.exec(_2c)){
_2a=_2c;
}else{
_2a=(new dojo.uri.Uri(url,_2c).toString());
}
break;
case "style":
_2a=dojo.html.fixPathsInCssText(_2c,url);
break;
default:
_2a=_2c;
}
fix=" "+_2b[1]+"="+_2b[2]+_2a+_2b[2];
_29+=tag.substring(0,_2b.index)+fix;
tag=tag.substring((_2b.index+_2b[0].length),tag.length);
}
str+=_29+tag;
}
return str+s;
};
this.htmlContentScripts=function(s,_32){
var _33=[],_34=[],_35=[];
var _36="",tmp=null,tag="",sc="",str="";
var _3b=/<script([^>]*)>([\s\S]*?)<\/script>/i;
var _3c=/src=(['"]?)([^"']*)\1/i;
var _3d=/.*(\bdojo\b\.js(?:\.uncompressed\.js)?)$/;
var _3e=/(?:var )?\bdjConfig\b(?:[\s]*=[\s]*\{[^}]+\}|\.[\w]*[\s]*=[\s]*[^;\n]*)?;?|dojo\.hostenv\.writeIncludes\(\s*\);?/g;
var _3f=/dojo\.(?:(?:require(?:After)?(?:If)?)|(?:widget\.(?:manager\.)?registerWidgetPackage)|(?:(?:hostenv\.)?setModulePrefix)|defineNamespace)\((['"]).*?\1\)\s*;?/;
while(_35=_3b.exec(s)){
if(this.executeScripts&&_35[1]){
if(_36=_3c.exec(_35[1])){
if(_3d.exec(_36[2])){
dojo.debug("Security note! inhibit:"+_36[2]+" from  beeing loaded again.");
}else{
_33.push({path:_36[2]});
}
}
}
if(_35[2]){
sc=_35[2].replace(_3e,"");
if(!sc){
continue;
}
while(tmp=_3f.exec(sc)){
_34.push(tmp[0]);
sc=sc.substring(0,tmp.index)+sc.substr(tmp.index+tmp[0].length);
}
if(_32){
_33.push(sc);
}
}
s=s.substr(0,_35.index)+s.substr(_35.index+_35[0].length);
}
if(_32){
var _3b=/(<[a-zA-Z][a-zA-Z0-9]*\s[^>]*\S=(['"])[^>]*[^\.\]])scriptScope([^>]*>)/;
str="";
while(tag=_3b.exec(s)){
tmp=((tag[2]=="'")?"\"":"'");
str+=s.substring(0,tag.index);
s=s.substr(tag.index).replace(_3b,"$1dojo.widget.byId("+tmp+this.widgetId+tmp+").scriptScope$3");
}
s=str+s;
}
return {"s":s,"requires":_34,"scripts":_33};
};
this.splitAndFixPaths=function(_40){
if(!_40.url){
_40.url="./";
}
url=new dojo.uri.Uri(location,_40.url).toString();
var ret={"xml":"","styles":[],"titles":[],"requires":[],"scripts":[],"url":url};
if(_40.content){
var tmp=null,_43=_40.content;
if(_40.adjustPaths){
_43=_1.htmlContentAdjustPaths.call(this,_43,url);
}
tmp=_1.htmlContentBasicFix.call(this,_43,url);
_43=tmp.s;
ret.styles=tmp.styles;
ret.titles=tmp.titles;
if(_40.collectRequires||_40.collectScripts){
tmp=_1.htmlContentScripts.call(this,_43,_40.collectScripts);
_43=tmp.s;
ret.requires=tmp.requires;
ret.scripts=tmp.scripts;
}
var _44=[];
if(_40.bodyExtract){
_44=_43.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
if(_44){
_43=_44[1];
}
}
ret.xml=_43;
}
return ret;
};
this.hookUp=function(_45){
var _46=_45.widget;
if(dojo.lang.isString(_46)){
if(_45.mixin){
dojo.raise(this.toString()+", cant use mixin when widget is a string");
}
_46=dojo.evalObjPath(_46);
}
if(!_46||!(_46 instanceof dojo.widget.HtmlWidget)){
dojo.raise(this.toString()+" Widget isn't defined or isn't a HtmlWidget instance");
}
if(_46.loader&&_46.setUrl){
return;
}
var _47=(_45.mixin)?_46:_46.constructor.prototype;
_46.loader={isLoaded:false,styleNodes:[],addOnLoads:[],addOnUnLoads:[],callOnUnLoad:(function(_48){
return function(_49){
this.abort();
if(_48){
this.onUnLoad();
}
_48=_49;
};
})(false),bindObj:null,unHook:(function(w,wg){
var _4c={isContainer:w.isContainer,adjustPats:w.adjustPaths,href:w.href,extractContent:w.extractContent,parseContent:w.parseContent,cacheContent:w.cacheContent,bindArgs:w.bindArgs,preload:w.preload,refreshOnShow:w.refreshOnShow,handler:w.handler,trackHistory:w.trackHistory,executeScripts:w.executeScripts,scriptScope:w.scriptScope,postCreate:w.postCreate,show:w.show,refresh:w.refresh,loadContents:w.loadContents,abort:w.abort,destroy:w.destroy,onLoad:w.onLoad,onUnLoad:w.onUnLoad,addOnLoad:w.addOnLoad,addOnUnLoad:w.addOnUnLoad,onDownloadStart:w.onDownloadStart,onDownloadEnd:w.onDownloadEnd,onDownloadError:w.onDownloadError,onContentError:w.onContentError,onExecError:w.onExecError,onSetContent:w.onSetContent,setUrl:w.setUrl,setContent:w.setContent,onContentParse:w.onContentParse,onExecScript:w.onExecScript,setHandler:w.setHandler};
return function(){
if(wg.abort){
wg.abort();
}
if((w!=wg)&&(dojo.widget.byType(wg.widgetType).length>1)){
return;
}
for(var x in _4c){
if(_4c[x]===undefined){
delete w[x];
continue;
}
w[x]=_4c[x];
}
delete wg._loader_defined;
delete wg.loader;
};
})(_47,_46)};
if(_47._loader_defined||_46._loader_defined){
return;
}
dojo.mixin(_47,{isContainer:true,adjustPaths:_d(_47.adjustPaths)?true:_47.adjustPaths,href:_d(_47.href)?"":_47.href,extractContent:_d(_47.extractContent)?true:_47.extractContent,parseContent:_d(_47.parseContent)?true:_47.parseContent,cacheContent:_d(_47.cacheContent)?true:_47.cacheContent,bindArgs:_d(_47.bindArgs)?{}:_47.bindArgs,preload:_d(_47.preload)?false:_47.preload,refreshOnShow:_d(_47.refreshOnShow)?false:_47.refreshOnShow,handler:_d(_47.handler)?"":_47.handler,executeScripts:_d(_47.executeScripts)?false:_47.executeScripts,trackHistory:_d(_47.tracHistory)?false:_47.trackHistory,scriptScope:null});
_47.postCreate=(function(_4e){
return function(){
if(_47.constructor.superclass.postCreate!=_4e){
_4e.apply(this,arguments);
}else{
_47.constructor.superclass.postCreate.apply(this,arguments);
}
if(this.handler!==""){
this.setHandler(this.handler);
}
if(this.isShowing()||this.preload){
this.loadContents();
if(!this.href){
_1._log(this,(this.domNode||this.containerNode).innerHTML);
}
}
};
})(_47.postCreate);
_47.show=(function(_4f){
return function(){
if(this.refreshOnShow){
this.refresh();
}else{
this.loadContents();
}
if((_47.constructor.superclass.show==_4f)||!_e(_4f)){
_47.constructor.superclass.show.apply(this,arguments);
}else{
_4f.apply(this,arguments);
}
};
})(_47.show);
_47.destroy=(function(_50){
return function(_51){
this.onUnLoad();
this.abort();
this.loader.unHook();
if((_47.constructor.superclass.destroy!=_51)&&_e(_51)){
_51.apply(this,arguments);
}else{
_47.constructor.superclass.destroy.apply(this,arguments);
}
};
})(_47.destroy);
if(!_47.refresh){
_47.refresh=function(){
this.loader.isLoaded=false;
this.loadContents();
};
}
if(!_47.loadContents){
_47.loadContents=function(){
if(this.loader.isLoaded){
return;
}
if(_e(this.handler)){
runHandler.call(this);
}else{
if(this.href!==""){
handleDefaults.call(this,"Loading...","onDownloadStart");
var _52=this,url=this.href;
downloader.call(this,{url:url,load:function(_54,_55,xhr){
_52.onDownloadEnd.call(_52,url,_55);
},error:function(_57,err,xhr){
var e={responseText:xhr.responseText,status:xhr.status,statusText:xhr.statusText,responseHeaders:(xhr.getAllResponseHeaders)?xhr.getAllResponseHeaders():[],_text:"Error loading '"+url+"' ("+xhr.status+" "+xhr.statusText+")"};
handleDefaults.call(_52,e,"onDownloadError");
_52.onLoad();
}});
}
}
};
}
if(!_47.abort){
_47.abort=function(){
if(!this.loader||!this.loader.bindObj||!this.loader.bindObj.abort){
return;
}
this.loader.bindObj.abort();
this.loader.bindObj=null;
};
}
if(!_47.onLoad){
_47.onLoad=function(){
stackRunner.call(this,this.loader.addOnLoads);
this.loader.isLoaded=true;
};
}
if(!_47.onUnLoad){
_47.onUnLoad=function(){
stackRunner.call(this,this.loader.addOnUnLoads);
delete this.scriptScope;
};
}
if(!_47.addOnLoad){
_47.addOnLoad=function(obj,_5c){
stackPusher.call(this,this.loader.addOnLoads,obj,_5c);
};
}
if(!_47.addOnUnLoad){
_47.addOnUnLoad=function(obj,_5e){
stackPusher.call(this,this.loader.addOnUnLoads,obj,_5e);
};
}
if(!_47.onExecError){
_47.onExecError=function(){
};
}
if(!_47.onContentError){
_47.onContentError=function(){
};
}
if(!_47.onDownloadError){
_47.onDownloadError=function(){
};
}
if(!_47.onDownloadStart){
_47.onDownloadStart=function(_5f){
};
}
if(!_47.onDownloadEnd){
_47.onDownloadEnd=function(url,_61){
var _62={content:_61,url:url,adjustPaths:this.adjustPaths,collectScripts:this.executeScripts,collectRequires:this.parseContent,bodyExtract:this.extractContent};
_61=_1.splitAndFixPaths.call(this,_62);
this.setContent(_61);
};
}
if(!_47.onSetContent){
_47.onSetContent=function(_63){
this.destroyChildren();
var _64=this.loader.styleNodes;
while(_64.length){
var st=_64.pop();
if(st&&st.parentNode){
st.parentNode.removeChild(st);
}
}
var _66=this.containerNode||this.domNode;
while(_66.firstChild){
try{
dojo.event.browser.clean(_66.firstChild);
}
catch(e){
}
_66.removeChild(_66.firstChild);
}
try{
if(typeof _63!="string"){
_66.appendChild(_63);
}else{
try{
_66.innerHTML=_63;
}
catch(e){
var tmp;
(tmp=dojo.doc().createElement("div")).innerHTML=_63;
while(tmp.firstChild){
_66.appendChild(tmp.removeChild(tmp.firstChild));
}
}
}
}
catch(e){
e._text="Could'nt load content: "+e;
var _68=(this.loader._onSetContent_err==e._text);
this.loader._onSetContent_err=e._text;
handleDefaults.call(this,e,"onContentError",_68);
}
};
}
if(!_47.setUrl){
_47.setUrl=function(url){
this.href=url;
this.loader.isLoaded=false;
if(this.preload||this.isShowing()){
this.loadContents();
}
};
}
if(!_47.setContent){
_47.setContent=function(_6a,_6b){
this.loader.callOnUnLoad.call(this,true);
if(!_6a||dojo.html.isNode(_6a)){
this.onSetContent(_6a);
refreshed.call(this);
}else{
if(typeof _6a.xml!="string"){
this.href="";
var _6c={content:_6a,url:this.href,adjustPaths:this.adjustPaths,collectScripts:this.executeScripts,collectRequires:this.parseContent,bodyExtract:this.extractContent};
_6a=_1.splitAndFixPaths.call(this,_6c);
}else{
if(_6a.url!="./"){
this.url=_6a.url;
}
}
this.onSetContent(_6a.xml);
for(var i=0,_6e=_6a.styles;i<_6e.length;i++){
if(_6e[i].path){
this.loader.styleNodes.push(dojo.html.insertCssFile(_6e[i].path));
}else{
this.loader.styleNodes.push(dojo.html.insertCssText(_6e[i]));
}
}
if(this.parseContent){
for(var i=0,_6f=_6a.requires;i<_6f.length;i++){
try{
eval(_6f[i]);
}
catch(e){
e._text="dojo.widget.html.loader.hookUp: error in package loading calls, "+(e.description||e);
handleDefaults.call(this,e,"onContentError",true);
}
}
}
if(dojo.hostenv.isXDomain&&_6a.requires.length){
dojo.addOnLoad(function(){
asyncParse.call(this,_6a);
if(!_6b){
_1._log(this,_6a);
}
});
_6b=true;
}else{
asyncParse.call(this,_6a);
}
}
if(!_6b){
}
};
}
if(!_47.onContentParse){
_47.onContentParse=function(){
var _70=this.containerNode||this.domNode;
var _71=new dojo.xml.Parse();
var _72=_71.parseElement(_70,null,true);
dojo.widget.getParser().createSubComponents(_72,this);
};
}
if(!_47.onExecScript){
_47.onExecScript=function(_73){
var _74=this,tmp="",_76="";
for(var i=0;i<_73.length;i++){
if(_73[i].path){
var url=_73[i].path;
downloader.call(this,{"url":url,"load":function(_79,_7a){
(function(){
tmp=_7a;
_73[i]=_7a;
}).call(_74);
},"error":function(_7b,_7c){
_7c._text=_7b+" downloading remote script";
handleDefaults.call(_74,_7c,"onExecError",true);
},"mimetype":"text/plain","sync":true});
_76+=tmp;
}else{
_76+=_73[i];
}
}
try{
delete this.scriptScope;
this.scriptScope=new (new Function("_container_",_76+"; return this;"))(_74);
}
catch(e){
e._text="Error running scripts from content:\n"+(e.description||e.toString());
handleDefaults.call(this,e,"onExecError",true);
}
};
}
if(!_47.setHandler){
_47.setHandler=function(_7d){
var fcn=dojo.lang.isFunction(_7d)?_7d:window[_7d];
if(!_e(fcn)){
handleDefaults.call(this,"Unable to set handler, '"+_7d+"' not a function.","onExecError",true);
return;
}
this.handler=function(){
return fcn.apply(this,arguments);
};
};
}
_47._loader_defined=true;
};
})();
