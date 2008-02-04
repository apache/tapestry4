dojo.provide("dojo.storage");
dojo.require("dojo.lang.*");
dojo.require("dojo.event.*");
dojo.storage=new function(){
};
dojo.declare("dojo.storage",null,{SUCCESS:"success",FAILED:"failed",PENDING:"pending",SIZE_NOT_AVAILABLE:"Size not available",SIZE_NO_LIMIT:"No size limit",namespace:"default",onHideSettingsUI:null,initialize:function(){
dojo.unimplemented("dojo.storage.initialize");
},isAvailable:function(){
dojo.unimplemented("dojo.storage.isAvailable");
},put:function(_1,_2,_3){
dojo.unimplemented("dojo.storage.put");
},get:function(_4){
dojo.unimplemented("dojo.storage.get");
},hasKey:function(_5){
return (this.get(_5)!=null);
},getKeys:function(){
dojo.unimplemented("dojo.storage.getKeys");
},clear:function(){
dojo.unimplemented("dojo.storage.clear");
},remove:function(_6){
dojo.unimplemented("dojo.storage.remove");
},isPermanent:function(){
dojo.unimplemented("dojo.storage.isPermanent");
},getMaximumSize:function(){
dojo.unimplemented("dojo.storage.getMaximumSize");
},hasSettingsUI:function(){
return false;
},showSettingsUI:function(){
dojo.unimplemented("dojo.storage.showSettingsUI");
},hideSettingsUI:function(){
dojo.unimplemented("dojo.storage.hideSettingsUI");
},getType:function(){
dojo.unimplemented("dojo.storage.getType");
},isValidKey:function(_7){
if((_7==null)||(typeof _7=="undefined")){
return false;
}
return /^[0-9A-Za-z_]*$/.test(_7);
}});
dojo.storage.manager=new function(){
this.currentProvider=null;
this.available=false;
this._initialized=false;
this._providers=[];
this.namespace="default";
this.initialize=function(){
this.autodetect();
};
this.register=function(_8,_9){
this._providers[this._providers.length]=_9;
this._providers[_8]=_9;
};
this.setProvider=function(_a){
};
this.autodetect=function(){
if(this._initialized==true){
return;
}
var _b=null;
for(var i=0;i<this._providers.length;i++){
_b=this._providers[i];
if(dojo.lang.isUndefined(djConfig["forceStorageProvider"])==false&&_b.getType()==djConfig["forceStorageProvider"]){
_b.isAvailable();
break;
}else{
if(dojo.lang.isUndefined(djConfig["forceStorageProvider"])==true&&_b.isAvailable()){
break;
}
}
}
if(_b==null){
this._initialized=true;
this.available=false;
this.currentProvider=null;
dojo.raise("No storage provider found for this platform");
}
this.currentProvider=_b;
for(var i in _b){
dojo.storage[i]=_b[i];
}
dojo.storage.manager=this;
dojo.storage.initialize();
this._initialized=true;
this.available=true;
};
this.isAvailable=function(){
return this.available;
};
this.isInitialized=function(){
if(this.currentProvider.getType()=="dojo.storage.browser.FlashStorageProvider"&&dojo.flash.ready==false){
return false;
}else{
return this._initialized;
}
};
this.supportsProvider=function(_d){
try{
var _e=eval("new "+_d+"()");
var _f=_e.isAvailable();
if(_f==null||typeof _f=="undefined"){
return false;
}
return _f;
}
catch(exception){
return false;
}
};
this.getProvider=function(){
return this.currentProvider;
};
this.loaded=function(){
};
};
