dojo.provide("dojo.storage.browser");
dojo.require("dojo.storage");
dojo.require("dojo.flash");
dojo.require("dojo.json");
dojo.require("dojo.uri.*");
dojo.storage.browser.FileStorageProvider=function(){
};
dojo.inherits(dojo.storage.browser.FileStorageProvider,dojo.storage);
dojo.storage.browser.FileStorageProvider._KEY_INDEX_FILENAME="__dojoAllKeys";
dojo.storage.browser.FileStorageProvider._APPLET_ID="__dojoFileJavaObj";
dojo.lang.extend(dojo.storage.browser.FileStorageProvider,{namespace:"default",initialized:false,_available:null,_statusHandler:null,_keyIndex:new Array(),initialize:function(){
if(djConfig["disableFileStorage"]==true){
return;
}
this._loadKeyIndex();
this.initialized=true;
dojo.storage.manager.loaded();
},isAvailable:function(){
this._available=false;
var _1=window.location.protocol;
if(_1.indexOf("file")!=-1||_1.indexOf("chrome")!=-1){
this._available=this._isAvailableXPCOM();
if(this._available==false){
this._available=this._isAvailableActiveX();
}
}
return this._available;
},put:function(_2,_3,_4){
if(this.isValidKey(_2)==false){
dojo.raise("Invalid key given: "+_2);
}
this._statusHandler=_4;
try{
this._save(_2,_3);
_4.call(null,dojo.storage.SUCCESS,_2);
}
catch(e){
this._statusHandler.call(null,dojo.storage.FAILED,_2,e.toString());
}
},get:function(_5){
if(this.isValidKey(_5)==false){
dojo.raise("Invalid key given: "+_5);
}
var _6=this._load(_5);
return _6;
},getKeys:function(){
return this._keyIndex;
},hasKey:function(_7){
if(this.isValidKey(_7)==false){
dojo.raise("Invalid key given: "+_7);
}
this._loadKeyIndex();
var _8=false;
for(var i=0;i<this._keyIndex.length;i++){
if(this._keyIndex[i]==_7){
_8=true;
}
}
return _8;
},clear:function(){
this._loadKeyIndex();
var _a=new Array();
for(var i=0;i<this._keyIndex.length;i++){
_a[_a.length]=new String(this._keyIndex[i]);
}
for(var i=0;i<_a.length;i++){
this.remove(_a[i]);
}
},remove:function(_c){
if(this.isValidKey(_c)==false){
dojo.raise("Invalid key given: "+_c);
}
this._loadKeyIndex();
for(var i=0;i<this._keyIndex.length;i++){
if(this._keyIndex[i]==_c){
this._keyIndex.splice(i,1);
break;
}
}
this._save(dojo.storage.browser.FileStorageProvider._KEY_INDEX_FILENAME,this._keyIndex,false);
var _e=this._getPagePath()+_c+".txt";
if(this._isAvailableXPCOM()){
this._removeXPCOM(_e);
}else{
if(this._isAvailableActiveX()){
this._removeActiveX(_e);
}
}
},isPermanent:function(){
return true;
},getMaximumSize:function(){
return dojo.storage.SIZE_NO_LIMIT;
},hasSettingsUI:function(){
return false;
},showSettingsUI:function(){
dojo.raise(this.getType()+" does not support a storage settings user-interface");
},hideSettingsUI:function(){
dojo.raise(this.getType()+" does not support a storage settings user-interface");
},getType:function(){
return "dojo.storage.browser.FileStorageProvider";
},_save:function(_f,_10,_11){
if(typeof _11=="undefined"){
_11=true;
}
if(dojo.lang.isString(_10)==false){
_10=dojo.json.serialize(_10);
_10="/* JavaScript */\n"+_10+"\n\n";
}
var _12=this._getPagePath()+_f+".txt";
if(this._isAvailableXPCOM()){
this._saveFileXPCOM(_12,_10);
}else{
if(this._isAvailableActiveX()){
this._saveFileActiveX(_12,_10);
}
}
if(_11){
this._updateKeyIndex(_f);
}
},_load:function(key){
var _14=this._getPagePath()+key+".txt";
var _15=null;
if(this._isAvailableXPCOM()){
_15=this._loadFileXPCOM(_14);
}else{
if(this._isAvailableActiveX()){
_15=this._loadFileActiveX(_14);
}else{
if(this._isAvailableJava()){
_15=this._loadFileJava(_14);
}
}
}
if(_15==null){
return null;
}
if(!dojo.lang.isUndefined(_15)&&_15!=null&&/^\/\* JavaScript \*\//.test(_15)){
_15=dojo.json.evalJson(_15);
}
return _15;
},_updateKeyIndex:function(key){
this._loadKeyIndex();
var _17=false;
for(var i=0;i<this._keyIndex.length;i++){
if(this._keyIndex[i]==key){
_17=true;
break;
}
}
if(_17==false){
this._keyIndex[this._keyIndex.length]=key;
}
this._save(dojo.storage.browser.FileStorageProvider._KEY_INDEX_FILENAME,this._keyIndex,false);
},_loadKeyIndex:function(){
var _19=this._load(dojo.storage.browser.FileStorageProvider._KEY_INDEX_FILENAME);
if(_19==null){
this._keyIndex=new Array();
}else{
this._keyIndex=_19;
}
},_saveFileXPCOM:function(_1a,_1b){
try{
netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
var f=Components.classes["@mozilla.org/file/local;1"].createInstance(Components.interfaces.nsILocalFile);
f.initWithPath(_1a);
var _1d=Components.classes["@mozilla.org/network/file-output-stream;1"].createInstance(Components.interfaces.nsIFileOutputStream);
_1d.init(f,32|4|8,256+128,null);
_1d.write(_1b,_1b.length);
_1d.close();
}
catch(e){
var msg=e.toString();
if(e.name&&e.message){
msg=e.name+": "+e.message;
}
dojo.raise("dojo.storage.browser.FileStorageProvider._saveFileXPCOM(): "+msg);
}
},_loadFileXPCOM:function(_1f){
try{
netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
var f=Components.classes["@mozilla.org/file/local;1"].createInstance(Components.interfaces.nsILocalFile);
f.initWithPath(_1f);
if(f.exists()==false){
return null;
}
var inp=Components.classes["@mozilla.org/network/file-input-stream;1"].createInstance(Components.interfaces.nsIFileInputStream);
inp.init(f,1,4,null);
var _22=Components.classes["@mozilla.org/scriptableinputstream;1"].createInstance(Components.interfaces.nsIScriptableInputStream);
_22.init(inp);
var _23=_22.read(_22.available());
return _23;
}
catch(e){
var msg=e.toString();
if(e.name&&e.message){
msg=e.name+": "+e.message;
}
dojo.raise("dojo.storage.browser.FileStorageProvider._loadFileXPCOM(): "+msg);
}
return null;
},_saveFileActiveX:function(_25,_26){
try{
var _27=new ActiveXObject("Scripting.FileSystemObject");
var f=_27.OpenTextFile(_25,2,true);
f.Write(_26);
f.Close();
}
catch(e){
var msg=e.toString();
if(e.name&&e.message){
msg=e.name+": "+e.message;
}
dojo.raise("dojo.storage.browser.FileStorageProvider._saveFileActiveX(): "+msg);
}
},_loadFileActiveX:function(_2a){
try{
var _2b=new ActiveXObject("Scripting.FileSystemObject");
if(_2b.FileExists(_2a)==false){
return null;
}
var f=_2b.OpenTextFile(_2a,1);
var _2d=f.ReadAll();
f.Close();
return _2d;
}
catch(e){
var msg=e.toString();
if(e.name&&e.message){
msg=e.name+": "+e.message;
}
dojo.raise("dojo.storage.browser.FileStorageProvider._loadFileActiveX(): "+msg);
}
},_saveFileJava:function(_2f,_30){
try{
var _31=dojo.byId(dojo.storage.browser.FileStorageProvider._APPLET_ID);
_31.save(_2f,_30);
}
catch(e){
var msg=e.toString();
if(e.name&&e.message){
msg=e.name+": "+e.message;
}
dojo.raise("dojo.storage.browser.FileStorageProvider._saveFileJava(): "+msg);
}
},_loadFileJava:function(_33){
try{
var _34=dojo.byId(dojo.storage.browser.FileStorageProvider._APPLET_ID);
var _35=_34.load(_33);
return _35;
}
catch(e){
var msg=e.toString();
if(e.name&&e.message){
msg=e.name+": "+e.message;
}
dojo.raise("dojo.storage.browser.FileStorageProvider._loadFileJava(): "+msg);
}
},_isAvailableActiveX:function(){
try{
if(window.ActiveXObject){
var _37=new window.ActiveXObject("Scripting.FileSystemObject");
return true;
}
}
catch(e){
dojo.debug(e);
}
return false;
},_isAvailableXPCOM:function(){
try{
if(window.Components){
netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
Components.classes["@mozilla.org/file/local;1"].createInstance(Components.interfaces.nsILocalFile);
return true;
}
}
catch(e){
dojo.debug(e);
}
return false;
},_isAvailableJava:function(){
try{
if(dojo.render.html.safari==true||dojo.render.html.opera==true()){
if(navigator.javaEnabled()==true){
return true;
}
}
}
catch(e){
dojo.debug(e);
}
return false;
},_getPagePath:function(){
var _38=window.location.pathname;
if(/\.html?$/i.test(_38)){
_38=_38.replace(/(?:\/|\\)?[^\.\/\\]*\.html?$/,"");
}
if(/^\/?[a-z]+\:/i.test(_38)){
_38=_38.replace(/^\/?/,"");
_38=_38.replace(/\//g,"\\");
}else{
if(/^[\/\\]{2,3}[^\/]/.test(_38)){
_38=_38.replace(/^[\/\\]{2,3}/,"");
_38=_38.replace(/\//g,"\\");
_38="\\\\"+_38;
}
}
if(/\/$/.test(_38)==false&&/\\$/.test(_38)==false){
if(/\//.test(_38)){
_38+="/";
}else{
_38+="\\";
}
}
_38=unescape(_38);
return _38;
},_removeXPCOM:function(_39){
try{
netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
var f=Components.classes["@mozilla.org/file/local;1"].createInstance(Components.interfaces.nsILocalFile);
f.initWithPath(_39);
if(f.exists()==false||f.isDirectory()){
return;
}
if(f.isFile()){
f.remove(false);
}
}
catch(e){
dojo.raise("dojo.storage.browser.FileStorageProvider.remove(): "+e.toString());
}
},_removeActiveX:function(_3b){
try{
var _3c=new ActiveXObject("Scripting.FileSystemObject");
_3c.DeleteFile(_3b);
}
catch(e){
dojo.raise("dojo.storage.browser.FileStorageProvider.remove(): "+e.toString());
}
},_removeJava:function(_3d){
try{
var _3e=dojo.byId(dojo.storage.browser.FileStorageProvider._APPLET_ID);
_3e.remove(_3d);
}
catch(e){
var msg=e.toString();
if(e.name&&e.message){
msg=e.name+": "+e.message;
}
dojo.raise("dojo.storage.browser.FileStorageProvider._removeJava(): "+msg);
}
},_writeApplet:function(){
var _40=dojo.uri.moduleUri("dojo","../DojoFileStorageProvider.jar").toString();
var tag="<applet "+"id='"+dojo.storage.browser.FileStorageProvider._APPLET_ID+"' "+"style='position: absolute; top: -500px; left: -500px; width: 1px; height: 1px;' "+"code='DojoFileStorageProvider.class' "+"archive='"+_40+"' "+"width='1' "+"height='1' "+">"+"</applet>";
document.writeln(tag);
}});
dojo.storage.browser.WhatWGStorageProvider=function(){
};
dojo.inherits(dojo.storage.browser.WhatWGStorageProvider,dojo.storage);
dojo.lang.extend(dojo.storage.browser.WhatWGStorageProvider,{namespace:"default",initialized:false,_domain:null,_available:null,_statusHandler:null,initialize:function(){
if(djConfig["disableWhatWGStorage"]==true){
return;
}
this._domain=location.hostname;
this.initialized=true;
dojo.storage.manager.loaded();
},isAvailable:function(){
try{
var _42=globalStorage[location.hostname];
}
catch(e){
this._available=false;
return this._available;
}
this._available=true;
return this._available;
},put:function(key,_44,_45){
if(this.isValidKey(key)==false){
dojo.raise("Invalid key given: "+key);
}
this._statusHandler=_45;
if(dojo.lang.isString(_44)){
_44="string:"+_44;
}else{
_44=dojo.json.serialize(_44);
}
window.addEventListener("storage",function(evt){
_45.call(null,dojo.storage.SUCCESS,key);
},false);
try{
var _47=globalStorage[this._domain];
_47.setItem(key,_44);
}
catch(e){
this._statusHandler.call(null,dojo.storage.FAILED,key,e.toString());
}
},get:function(key){
if(this.isValidKey(key)==false){
dojo.raise("Invalid key given: "+key);
}
var _49=globalStorage[this._domain];
var _4a=_49.getItem(key);
if(_4a==null){
return null;
}
_4a=_4a.value;
if(!dojo.lang.isUndefined(_4a)&&_4a!=null&&/^string:/.test(_4a)){
_4a=_4a.substring("string:".length);
}else{
_4a=dojo.json.evalJson(_4a);
}
return _4a;
},getKeys:function(){
var _4b=globalStorage[this._domain];
var _4c=new Array();
for(i=0;i<_4b.length;i++){
_4c[i]=_4b.key(i);
}
return _4c;
},clear:function(){
var _4d=globalStorage[this._domain];
var _4e=new Array();
for(var i=0;i<_4d.length;i++){
_4e[_4e.length]=_4d.key(i);
}
for(var i=0;i<_4e.length;i++){
_4d.removeItem(_4e[i]);
}
},remove:function(key){
var _51=globalStorage[this._domain];
_51.removeItem(key);
},isPermanent:function(){
return true;
},getMaximumSize:function(){
return dojo.storage.SIZE_NO_LIMIT;
},hasSettingsUI:function(){
return false;
},showSettingsUI:function(){
dojo.raise(this.getType()+" does not support a storage settings user-interface");
},hideSettingsUI:function(){
dojo.raise(this.getType()+" does not support a storage settings user-interface");
},getType:function(){
return "dojo.storage.browser.WhatWGProvider";
}});
dojo.storage.browser.FlashStorageProvider=function(){
};
dojo.inherits(dojo.storage.browser.FlashStorageProvider,dojo.storage);
dojo.lang.extend(dojo.storage.browser.FlashStorageProvider,{namespace:"default",initialized:false,_available:null,_statusHandler:null,initialize:function(){
if(djConfig["disableFlashStorage"]==true){
return;
}
var _52=function(){
dojo.storage._flashLoaded();
};
dojo.flash.addLoadedListener(_52);
var _53=dojo.uri.moduleUri("dojo","../Storage_version6.swf").toString();
var _54=dojo.uri.moduleUri("dojo","../Storage_version8.swf").toString();
dojo.flash.setSwf({flash6:_53,flash8:_54,visible:false});
},isAvailable:function(){
if(djConfig["disableFlashStorage"]==true){
this._available=false;
}else{
this._available=true;
}
return this._available;
},put:function(key,_56,_57){
if(this.isValidKey(key)==false){
dojo.raise("Invalid key given: "+key);
}
this._statusHandler=_57;
if(dojo.lang.isString(_56)){
_56="string:"+_56;
}else{
_56=dojo.json.serialize(_56);
}
dojo.flash.comm.put(key,_56,this.namespace);
},get:function(key){
if(this.isValidKey(key)==false){
dojo.raise("Invalid key given: "+key);
}
var _59=dojo.flash.comm.get(key,this.namespace);
if(_59==""){
return null;
}
if(!dojo.lang.isUndefined(_59)&&_59!=null&&/^string:/.test(_59)){
_59=_59.substring("string:".length);
}else{
_59=dojo.json.evalJson(_59);
}
return _59;
},getKeys:function(){
var _5a=dojo.flash.comm.getKeys(this.namespace);
if(_5a==""){
return [];
}
return _5a.split(",");
},clear:function(){
dojo.flash.comm.clear(this.namespace);
},remove:function(key){
dojo.unimplemented("dojo.storage.browser.FlashStorageProvider.remove");
},isPermanent:function(){
return true;
},getMaximumSize:function(){
return dojo.storage.SIZE_NO_LIMIT;
},hasSettingsUI:function(){
return true;
},showSettingsUI:function(){
dojo.flash.comm.showSettings();
dojo.flash.obj.setVisible(true);
dojo.flash.obj.center();
},hideSettingsUI:function(){
dojo.flash.obj.setVisible(false);
if(dojo.storage.onHideSettingsUI!=null&&!dojo.lang.isUndefined(dojo.storage.onHideSettingsUI)){
dojo.storage.onHideSettingsUI.call(null);
}
},getType:function(){
return "dojo.storage.browser.FlashStorageProvider";
},_flashLoaded:function(){
this._initialized=true;
dojo.storage.manager.loaded();
},_onStatus:function(_5c,key){
var ds=dojo.storage;
var dfo=dojo.flash.obj;
if(_5c==ds.PENDING){
dfo.center();
dfo.setVisible(true);
}else{
dfo.setVisible(false);
}
if((!dj_undef("_statusHandler",ds))&&(ds._statusHandler!=null)){
ds._statusHandler.call(null,_5c,key);
}
}});
dojo.storage.manager.register("dojo.storage.browser.FileStorageProvider",new dojo.storage.browser.FileStorageProvider());
dojo.storage.manager.register("dojo.storage.browser.WhatWGStorageProvider",new dojo.storage.browser.WhatWGStorageProvider());
dojo.storage.manager.register("dojo.storage.browser.FlashStorageProvider",new dojo.storage.browser.FlashStorageProvider());
dojo.storage.manager.initialize();
