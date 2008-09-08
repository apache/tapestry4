dojo.hostenv.resetXd=function(){
this.isXDomain=djConfig.useXDomain||false;
this.xdTimer=0;
this.xdInFlight={};
this.xdOrderedReqs=[];
this.xdDepMap={};
this.xdContents=[];
this.xdDefList=[];
};
dojo.hostenv.resetXd();
dojo.hostenv.createXdPackage=function(_1,_2,_3){
var _4=[];
var _5=/dojo.(requireLocalization|require|requireIf|requireAll|provide|requireAfterIf|requireAfter|kwCompoundRequire|conditionalRequire|hostenv\.conditionalLoadModule|.hostenv\.loadModule|hostenv\.moduleLoaded)\(([\w\W]*?)\)/mg;
var _6;
while((_6=_5.exec(_1))!=null){
if(_6[1]=="requireLocalization"){
eval(_6[0]);
}else{
_4.push("\""+_6[1]+"\", "+_6[2]);
}
}
var _7=[];
_7.push("dojo.hostenv.packageLoaded({\n");
if(_4.length>0){
_7.push("depends: [");
for(var i=0;i<_4.length;i++){
if(i>0){
_7.push(",\n");
}
_7.push("["+_4[i]+"]");
}
_7.push("],");
}
_7.push("\ndefinePackage: function(dojo){");
_7.push(_1);
_7.push("\n}, resourceName: '"+_2+"', resourcePath: '"+_3+"'});");
return _7.join("");
};
dojo.hostenv.loadPath=function(_9,_a,cb){
var _c=_9.indexOf(":");
var _d=_9.indexOf("/");
var _e;
var _f=false;
if(_c>0&&_c<_d){
_e=_9;
this.isXDomain=_f=true;
}else{
_e=this.getBaseScriptUri()+_9;
_c=_e.indexOf(":");
_d=_e.indexOf("/");
if(_c>0&&_c<_d&&(!location.host||_e.indexOf("http://"+location.host)!=0)){
this.isXDomain=_f=true;
}
}
if(djConfig.cacheBust&&dojo.render.html.capable){
_e+="?"+String(djConfig.cacheBust).replace(/\W+/g,"");
}
try{
return ((!_a||this.isXDomain)?this.loadUri(_e,cb,_f,_a):this.loadUriAndCheck(_e,_a,cb));
}
catch(e){
dojo.debug(e);
return false;
}
};
dojo.hostenv.loadUri=function(uri,cb,_12,_13){
if(this.loadedUris[uri]){
return 1;
}
if(this.isXDomain&&_13){
if(uri.indexOf("__package__")!=-1){
_13+=".*";
}
this.xdOrderedReqs.push(_13);
if(_12||uri.indexOf("/nls/")==-1){
this.xdInFlight[_13]=true;
this.inFlightCount++;
}
if(!this.xdTimer){
this.xdTimer=setInterval("dojo.hostenv.watchInFlightXDomain();",100);
}
this.xdStartTime=(new Date()).getTime();
}
if(_12){
var _14=uri.lastIndexOf(".");
if(_14<=0){
_14=uri.length-1;
}
var _15=uri.substring(0,_14)+".xd";
if(_14!=uri.length-1){
_15+=uri.substring(_14,uri.length);
}
var _16=document.createElement("script");
_16.type="text/javascript";
_16.src=_15;
if(!this.headElement){
this.headElement=document.getElementsByTagName("head")[0];
if(!this.headElement){
this.headElement=document.getElementsByTagName("html")[0];
}
}
this.headElement.appendChild(_16);
}else{
var _17=this.getText(uri,null,true);
if(_17==null){
return 0;
}
if(this.isXDomain&&uri.indexOf("/nls/")==-1){
var pkg=this.createXdPackage(_17,_13,uri);
dj_eval(pkg);
}else{
if(cb){
_17="("+_17+")";
}
var _19=dj_eval(_17);
if(cb){
cb(_19);
}
}
}
this.loadedUris[uri]=true;
return 1;
};
dojo.hostenv.packageLoaded=function(pkg){
var _1b=pkg.depends;
var _1c=null;
var _1d=null;
var _1e=[];
if(_1b&&_1b.length>0){
var dep=null;
var _20=0;
var _21=false;
for(var i=0;i<_1b.length;i++){
dep=_1b[i];
if(dep[0]=="provide"||dep[0]=="hostenv.moduleLoaded"){
_1e.push(dep[1]);
}else{
if(!_1c){
_1c=[];
}
if(!_1d){
_1d=[];
}
var _23=this.unpackXdDependency(dep);
if(_23.requires){
_1c=_1c.concat(_23.requires);
}
if(_23.requiresAfter){
_1d=_1d.concat(_23.requiresAfter);
}
}
var _24=dep[0];
var _25=_24.split(".");
if(_25.length==2){
dojo[_25[0]][_25[1]].apply(dojo[_25[0]],dep.slice(1));
}else{
dojo[_24].apply(dojo,dep.slice(1));
}
}
var _26=this.xdContents.push({content:pkg.definePackage,resourceName:pkg["resourceName"],resourcePath:pkg["resourcePath"],isDefined:false})-1;
for(var i=0;i<_1e.length;i++){
this.xdDepMap[_1e[i]]={requires:_1c,requiresAfter:_1d,contentIndex:_26};
}
for(var i=0;i<_1e.length;i++){
this.xdInFlight[_1e[i]]=false;
}
}
};
dojo.hostenv.xdLoadFlattenedBundle=function(_27,_28,_29,_2a){
_29=_29||"root";
var _2b=dojo.hostenv.normalizeLocale(_29).replace("-","_");
var _2c=[_27,"nls",_28].join(".");
var _2d=dojo.hostenv.startPackage(_2c);
_2d[_2b]=_2a;
var _2e=[_27,_2b,_28].join(".");
var _2f=dojo.hostenv.xdBundleMap[_2e];
if(_2f){
for(var _30 in _2f){
_2d[_30]=_2a;
}
}
};
dojo.hostenv.xdBundleMap={};
dojo.xdRequireLocalization=function(_31,_32,_33,_34){
var _35=_34.split(",");
var _36=dojo.hostenv.normalizeLocale(_33);
var _37="";
for(var i=0;i<_35.length;i++){
if(_36.indexOf(_35[i])==0){
if(_35[i].length>_37.length){
_37=_35[i];
}
}
}
var _39=_37.replace("-","_");
var _3a=dojo.evalObjPath([_31,"nls",_32].join("."));
if(_3a&&_3a[_39]){
bundle[_36.replace("-","_")]=_3a[_39];
}else{
var _3b=[_31,(_39||"root"),_32].join(".");
var _3c=dojo.hostenv.xdBundleMap[_3b];
if(!_3c){
_3c=dojo.hostenv.xdBundleMap[_3b]={};
}
_3c[_36.replace("-","_")]=true;
dojo.require(_31+".nls"+(_37?"."+_37:"")+"."+_32);
}
};
(function(){
var _3d=djConfig.extraLocale;
if(_3d){
if(!_3d instanceof Array){
_3d=[_3d];
}
dojo._xdReqLoc=dojo.xdRequireLocalization;
dojo.xdRequireLocalization=function(m,b,_40,_41){
dojo._xdReqLoc(m,b,_40,_41);
if(_40){
return;
}
for(var i=0;i<_3d.length;i++){
dojo._xdReqLoc(m,b,_3d[i],_41);
}
};
}
})();
dojo.hostenv.unpackXdDependency=function(dep){
var _44=null;
var _45=null;
switch(dep[0]){
case "requireIf":
case "requireAfterIf":
case "conditionalRequire":
if((dep[1]===true)||(dep[1]=="common")||(dep[1]&&dojo.render[dep[1]].capable)){
_44=[{name:dep[2],content:null}];
}
break;
case "requireAll":
dep.shift();
_44=dep;
dojo.hostenv.flattenRequireArray(_44);
break;
case "kwCompoundRequire":
case "hostenv.conditionalLoadModule":
var _46=dep[1];
var _47=_46["common"]||[];
var _44=(_46[dojo.hostenv.name_])?_47.concat(_46[dojo.hostenv.name_]||[]):_47.concat(_46["default"]||[]);
dojo.hostenv.flattenRequireArray(_44);
break;
case "require":
case "requireAfter":
case "hostenv.loadModule":
_44=[{name:dep[1],content:null}];
break;
}
if(dep[0]=="requireAfterIf"||dep[0]=="requireIf"){
_45=_44;
_44=null;
}
return {requires:_44,requiresAfter:_45};
};
dojo.hostenv.xdWalkReqs=function(){
var _48=null;
var req;
for(var i=0;i<this.xdOrderedReqs.length;i++){
req=this.xdOrderedReqs[i];
if(this.xdDepMap[req]){
_48=[req];
_48[req]=true;
this.xdEvalReqs(_48);
}
}
};
dojo.hostenv.xdEvalReqs=function(_4b){
while(_4b.length>0){
var req=_4b[_4b.length-1];
var pkg=this.xdDepMap[req];
if(pkg){
var _4e=pkg.requires;
if(_4e&&_4e.length>0){
var _4f;
for(var i=0;i<_4e.length;i++){
_4f=_4e[i].name;
if(_4f&&!_4b[_4f]){
_4b.push(_4f);
_4b[_4f]=true;
this.xdEvalReqs(_4b);
}
}
}
var _51=this.xdContents[pkg.contentIndex];
if(!_51.isDefined){
var _52=_51.content;
_52["resourceName"]=_51["resourceName"];
_52["resourcePath"]=_51["resourcePath"];
this.xdDefList.push(_52);
_51.isDefined=true;
}
this.xdDepMap[req]=null;
var _4e=pkg.requiresAfter;
if(_4e&&_4e.length>0){
var _4f;
for(var i=0;i<_4e.length;i++){
_4f=_4e[i].name;
if(_4f&&!_4b[_4f]){
_4b.push(_4f);
_4b[_4f]=true;
this.xdEvalReqs(_4b);
}
}
}
}
_4b.pop();
}
};
dojo.hostenv.clearXdInterval=function(){
clearInterval(this.xdTimer);
this.xdTimer=0;
};
dojo.hostenv.watchInFlightXDomain=function(){
var _53=(djConfig.xdWaitSeconds||15)*1000;
if(this.xdStartTime+_53<(new Date()).getTime()){
this.clearXdInterval();
var _54="";
for(var _55 in this.xdInFlight){
if(this.xdInFlight[_55]){
_54+=_55+" ";
}
}
dojo.raise("Could not load cross-domain packages: "+_54);
}
for(var _55 in this.xdInFlight){
if(this.xdInFlight[_55]){
return;
}
}
this.clearXdInterval();
this.xdWalkReqs();
var _56=this.xdDefList.length;
for(var i=0;i<_56;i++){
var _58=dojo.hostenv.xdDefList[i];
if(djConfig["debugAtAllCosts"]&&_58["resourceName"]){
if(!this["xdDebugQueue"]){
this.xdDebugQueue=[];
}
this.xdDebugQueue.push({resourceName:_58.resourceName,resourcePath:_58.resourcePath});
}else{
_58(dojo);
}
}
for(var i=0;i<this.xdContents.length;i++){
var _59=this.xdContents[i];
if(_59.content&&!_59.isDefined){
_59.content(dojo);
}
}
this.resetXd();
if(this["xdDebugQueue"]&&this.xdDebugQueue.length>0){
this.xdDebugFileLoaded();
}else{
this.xdNotifyLoaded();
}
};
dojo.hostenv.xdNotifyLoaded=function(){
this.inFlightCount=0;
if(this._djInitFired&&!this.loadNotifying){
this.callLoaded();
}
};
dojo.hostenv.flattenRequireArray=function(_5a){
if(_5a){
for(var i=0;i<_5a.length;i++){
if(_5a[i] instanceof Array){
_5a[i]={name:_5a[i][0],content:null};
}else{
_5a[i]={name:_5a[i],content:null};
}
}
}
};
dojo.hostenv.xdHasCalledPreload=false;
dojo.hostenv.xdRealCallLoaded=dojo.hostenv.callLoaded;
dojo.hostenv.callLoaded=function(){
if(this.xdHasCalledPreload||dojo.hostenv.getModulePrefix("dojo")=="src"||!this.localesGenerated){
this.xdRealCallLoaded();
}else{
if(this.localesGenerated){
this.registerNlsPrefix=function(){
dojo.registerModulePath("nls",dojo.hostenv.getModulePrefix("dojo")+"/../nls");
};
this.preloadLocalizations();
}
}
this.xdHasCalledPreload=true;
};
