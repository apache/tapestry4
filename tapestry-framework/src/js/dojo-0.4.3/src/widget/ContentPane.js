dojo.provide("dojo.widget.ContentPane");
dojo.require("dojo.widget.*");
dojo.require("dojo.io.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.string");
dojo.require("dojo.string.extras");
dojo.require("dojo.html.style");
dojo.widget.defineWidget("dojo.widget.ContentPane",dojo.widget.HtmlWidget,function(){
this._styleNodes=[];
this._onLoadStack=[];
this._onUnloadStack=[];
this._callOnUnload=false;
this._ioBindObj;
this.scriptScope;
this.bindArgs={};
},{isContainer:true,adjustPaths:true,href:"",extractContent:true,parseContent:true,cacheContent:true,preload:false,refreshOnShow:false,handler:"",executeScripts:false,scriptSeparation:true,loadingMessage:"Loading...",isLoaded:false,postCreate:function(_1,_2,_3){
if(this.handler!==""){
this.setHandler(this.handler);
}
if(this.isShowing()||this.preload){
this.loadContents();
}
},show:function(){
if(this.refreshOnShow){
this.refresh();
}else{
this.loadContents();
}
dojo.widget.ContentPane.superclass.show.call(this);
},refresh:function(){
this.isLoaded=false;
this.loadContents();
},loadContents:function(){
if(this.isLoaded){
return;
}
if(dojo.lang.isFunction(this.handler)){
this._runHandler();
}else{
if(this.href!=""){
this._downloadExternalContent(this.href,this.cacheContent&&!this.refreshOnShow);
}
}
},setUrl:function(_4){
this.href=_4;
this.isLoaded=false;
if(this.preload||this.isShowing()){
this.loadContents();
}
},abort:function(){
var _5=this._ioBindObj;
if(!_5||!_5.abort){
return;
}
_5.abort();
delete this._ioBindObj;
},_downloadExternalContent:function(_6,_7){
this.abort();
this._handleDefaults(this.loadingMessage,"onDownloadStart");
var _8=this;
this._ioBindObj=dojo.io.bind(this._cacheSetting({url:_6,mimetype:"text/html",handler:function(_9,_a,_b){
delete _8._ioBindObj;
if(_9=="load"){
_8.onDownloadEnd.call(_8,_6,_a);
}else{
var e={responseText:_b.responseText,status:_b.status,statusText:_b.statusText,responseHeaders:_b.getAllResponseHeaders(),text:"Error loading '"+_6+"' ("+_b.status+" "+_b.statusText+")"};
_8._handleDefaults.call(_8,e,"onDownloadError");
_8.onLoad();
}
}},_7));
},_cacheSetting:function(_d,_e){
for(var x in this.bindArgs){
if(dojo.lang.isUndefined(_d[x])){
_d[x]=this.bindArgs[x];
}
}
if(dojo.lang.isUndefined(_d.useCache)){
_d.useCache=_e;
}
if(dojo.lang.isUndefined(_d.preventCache)){
_d.preventCache=!_e;
}
if(dojo.lang.isUndefined(_d.mimetype)){
_d.mimetype="text/html";
}
return _d;
},onLoad:function(e){
this._runStack("_onLoadStack");
this.isLoaded=true;
},onUnLoad:function(e){
dojo.deprecated(this.widgetType+".onUnLoad, use .onUnload (lowercased load)",0.5);
},onUnload:function(e){
this._runStack("_onUnloadStack");
delete this.scriptScope;
if(this.onUnLoad!==dojo.widget.ContentPane.prototype.onUnLoad){
this.onUnLoad.apply(this,arguments);
}
},_runStack:function(_13){
var st=this[_13];
var err="";
var _16=this.scriptScope||window;
for(var i=0;i<st.length;i++){
try{
st[i].call(_16);
}
catch(e){
err+="\n"+st[i]+" failed: "+e.description;
}
}
this[_13]=[];
if(err.length){
var _18=(_13=="_onLoadStack")?"addOnLoad":"addOnUnLoad";
this._handleDefaults(_18+" failure\n "+err,"onExecError","debug");
}
},addOnLoad:function(obj,_1a){
this._pushOnStack(this._onLoadStack,obj,_1a);
},addOnUnload:function(obj,_1c){
this._pushOnStack(this._onUnloadStack,obj,_1c);
},addOnUnLoad:function(){
dojo.deprecated(this.widgetType+".addOnUnLoad, use addOnUnload instead. (lowercased Load)",0.5);
this.addOnUnload.apply(this,arguments);
},_pushOnStack:function(_1d,obj,_1f){
if(typeof _1f=="undefined"){
_1d.push(obj);
}else{
_1d.push(function(){
obj[_1f]();
});
}
},destroy:function(){
this.onUnload();
dojo.widget.ContentPane.superclass.destroy.call(this);
},onExecError:function(e){
},onContentError:function(e){
},onDownloadError:function(e){
},onDownloadStart:function(e){
},onDownloadEnd:function(url,_25){
_25=this.splitAndFixPaths(_25,url);
this.setContent(_25);
},_handleDefaults:function(e,_27,_28){
if(!_27){
_27="onContentError";
}
if(dojo.lang.isString(e)){
e={text:e};
}
if(!e.text){
e.text=e.toString();
}
e.toString=function(){
return this.text;
};
if(typeof e.returnValue!="boolean"){
e.returnValue=true;
}
if(typeof e.preventDefault!="function"){
e.preventDefault=function(){
this.returnValue=false;
};
}
this[_27](e);
if(e.returnValue){
switch(_28){
case true:
case "alert":
alert(e.toString());
break;
case "debug":
dojo.debug(e.toString());
break;
default:
if(this._callOnUnload){
this.onUnload();
}
this._callOnUnload=false;
if(arguments.callee._loopStop){
dojo.debug(e.toString());
}else{
arguments.callee._loopStop=true;
this._setContent(e.toString());
}
}
}
arguments.callee._loopStop=false;
},splitAndFixPaths:function(s,url){
var _2b=[],_2c=[],tmp=[];
var _2e=[],_2f=[],_30=[],_31=[];
var str="",_33="",fix="",_35="",tag="",_37="";
if(!url){
url="./";
}
if(s){
var _38=/<title[^>]*>([\s\S]*?)<\/title>/i;
while(_2e=_38.exec(s)){
_2b.push(_2e[1]);
s=s.substring(0,_2e.index)+s.substr(_2e.index+_2e[0].length);
}
if(this.adjustPaths){
var _39=/<[a-z][a-z0-9]*[^>]*\s(?:(?:src|href|style)=[^>])+[^>]*>/i;
var _3a=/\s(src|href|style)=(['"]?)([\w()\[\]\/.,\\'"-:;#=&?\s@]+?)\2/i;
var _3b=/^(?:[#]|(?:(?:https?|ftps?|file|javascript|mailto|news):))/;
while(tag=_39.exec(s)){
str+=s.substring(0,tag.index);
s=s.substring((tag.index+tag[0].length),s.length);
tag=tag[0];
_35="";
while(_30=_3a.exec(tag)){
_33="";
_37=_30[3];
switch(_30[1].toLowerCase()){
case "src":
case "href":
if(_3b.exec(_37)){
_33=_37;
}else{
_33=(new dojo.uri.Uri(url,_37).toString());
}
break;
case "style":
_33=dojo.html.fixPathsInCssText(_37,url);
break;
default:
_33=_37;
}
fix=" "+_30[1]+"="+_30[2]+_33+_30[2];
_35+=tag.substring(0,_30.index)+fix;
tag=tag.substring((_30.index+_30[0].length),tag.length);
}
str+=_35+tag;
}
s=str+s;
}
_38=/(?:<(style)[^>]*>([\s\S]*?)<\/style>|<link ([^>]*rel=['"]?stylesheet['"]?[^>]*)>)/i;
while(_2e=_38.exec(s)){
if(_2e[1]&&_2e[1].toLowerCase()=="style"){
_31.push(dojo.html.fixPathsInCssText(_2e[2],url));
}else{
if(_30=_2e[3].match(/href=(['"]?)([^'">]*)\1/i)){
_31.push({path:_30[2]});
}
}
s=s.substring(0,_2e.index)+s.substr(_2e.index+_2e[0].length);
}
var _38=/<script([^>]*)>([\s\S]*?)<\/script>/i;
var _3c=/src=(['"]?)([^"']*)\1/i;
var _3d=/.*(\bdojo\b\.js(?:\.uncompressed\.js)?)$/;
var _3e=/(?:var )?\bdjConfig\b(?:[\s]*=[\s]*\{[^}]+\}|\.[\w]*[\s]*=[\s]*[^;\n]*)?;?|dojo\.hostenv\.writeIncludes\(\s*\);?/g;
var _3f=/dojo\.(?:(?:require(?:After)?(?:If)?)|(?:widget\.(?:manager\.)?registerWidgetPackage)|(?:(?:hostenv\.)?setModulePrefix|registerModulePath)|defineNamespace)\((['"]).*?\1\)\s*;?/;
while(_2e=_38.exec(s)){
if(this.executeScripts&&_2e[1]){
if(_30=_3c.exec(_2e[1])){
if(_3d.exec(_30[2])){
dojo.debug("Security note! inhibit:"+_30[2]+" from  being loaded again.");
}else{
_2c.push({path:_30[2]});
}
}
}
if(_2e[2]){
var sc=_2e[2].replace(_3e,"");
if(!sc){
continue;
}
while(tmp=_3f.exec(sc)){
_2f.push(tmp[0]);
sc=sc.substring(0,tmp.index)+sc.substr(tmp.index+tmp[0].length);
}
if(this.executeScripts){
_2c.push(sc);
}
}
s=s.substr(0,_2e.index)+s.substr(_2e.index+_2e[0].length);
}
if(this.extractContent){
_2e=s.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
if(_2e){
s=_2e[1];
}
}
if(this.executeScripts&&this.scriptSeparation){
var _38=/(<[a-zA-Z][a-zA-Z0-9]*\s[^>]*?\S=)((['"])[^>]*scriptScope[^>]*>)/;
var _41=/([\s'";:\(])scriptScope(.*)/;
str="";
while(tag=_38.exec(s)){
tmp=((tag[3]=="'")?"\"":"'");
fix="";
str+=s.substring(0,tag.index)+tag[1];
while(_30=_41.exec(tag[2])){
tag[2]=tag[2].substring(0,_30.index)+_30[1]+"dojo.widget.byId("+tmp+this.widgetId+tmp+").scriptScope"+_30[2];
}
str+=tag[2];
s=s.substr(tag.index+tag[0].length);
}
s=str+s;
}
}
return {"xml":s,"styles":_31,"titles":_2b,"requires":_2f,"scripts":_2c,"url":url};
},_setContent:function(_42){
this.destroyChildren();
for(var i=0;i<this._styleNodes.length;i++){
if(this._styleNodes[i]&&this._styleNodes[i].parentNode){
this._styleNodes[i].parentNode.removeChild(this._styleNodes[i]);
}
}
this._styleNodes=[];
try{
var _44=this.containerNode||this.domNode;
while(_44.firstChild){
dojo.html.destroyNode(_44.firstChild);
}
if(typeof _42!="string"){
_44.appendChild(_42);
}else{
_44.innerHTML=_42;
}
}
catch(e){
e.text="Couldn't load content:"+e.description;
this._handleDefaults(e,"onContentError");
}
},setContent:function(_45){
this.abort();
if(this._callOnUnload){
this.onUnload();
}
this._callOnUnload=true;
if(!_45||dojo.html.isNode(_45)){
this._setContent(_45);
this.onResized();
this.onLoad();
}else{
if(typeof _45.xml!="string"){
this.href="";
_45=this.splitAndFixPaths(_45);
}
this._setContent(_45.xml);
for(var i=0;i<_45.styles.length;i++){
if(_45.styles[i].path){
this._styleNodes.push(dojo.html.insertCssFile(_45.styles[i].path,dojo.doc(),false,true));
}else{
this._styleNodes.push(dojo.html.insertCssText(_45.styles[i]));
}
}
if(this.parseContent){
for(var i=0;i<_45.requires.length;i++){
try{
eval(_45.requires[i]);
}
catch(e){
e.text="ContentPane: error in package loading calls, "+(e.description||e);
this._handleDefaults(e,"onContentError","debug");
}
}
}
var _47=this;
function asyncParse(){
if(_47.executeScripts){
_47._executeScripts(_45.scripts);
}
if(_47.parseContent){
var _48=_47.containerNode||_47.domNode;
var _49=new dojo.xml.Parse();
var _4a=_49.parseElement(_48,null,true);
dojo.widget.getParser().createSubComponents(_4a,_47);
}
_47.onResized();
_47.onLoad();
}
if(dojo.hostenv.isXDomain&&_45.requires.length){
dojo.addOnLoad(asyncParse);
}else{
asyncParse();
}
}
},setHandler:function(_4b){
var fcn=dojo.lang.isFunction(_4b)?_4b:window[_4b];
if(!dojo.lang.isFunction(fcn)){
this._handleDefaults("Unable to set handler, '"+_4b+"' not a function.","onExecError",true);
return;
}
this.handler=function(){
return fcn.apply(this,arguments);
};
},_runHandler:function(){
var ret=true;
if(dojo.lang.isFunction(this.handler)){
this.handler(this,this.domNode);
ret=false;
}
this.onLoad();
return ret;
},_executeScripts:function(_4e){
var _4f=this;
var tmp="",_51="";
for(var i=0;i<_4e.length;i++){
if(_4e[i].path){
dojo.io.bind(this._cacheSetting({"url":_4e[i].path,"load":function(_53,_54){
dojo.lang.hitch(_4f,tmp=";"+_54);
},"error":function(_55,_56){
_56.text=_55+" downloading remote script";
_4f._handleDefaults.call(_4f,_56,"onExecError","debug");
},"mimetype":"text/plain","sync":true},this.cacheContent));
_51+=tmp;
}else{
_51+=_4e[i];
}
}
try{
if(this.scriptSeparation){
delete this.scriptScope;
this.scriptScope=new (new Function("_container_",_51+"; return this;"))(_4f);
}else{
var djg=dojo.global();
if(djg.execScript){
djg.execScript(_51);
}else{
var djd=dojo.doc();
var sc=djd.createElement("script");
sc.appendChild(djd.createTextNode(_51));
(this.containerNode||this.domNode).appendChild(sc);
}
}
}
catch(e){
e.text="Error running scripts from content:\n"+e.description;
this._handleDefaults(e,"onExecError","debug");
}
}});
