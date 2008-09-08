dojo.provide("dojo.browser_debug");
dojo.hostenv.loadedUris.push("../src/bootstrap1.js");
dojo.hostenv.loadedUris.push("../src/loader.js");
dojo.hostenv.loadedUris.push("../src/hostenv_browser.js");
dojo.hostenv._loadedUrisListStart=dojo.hostenv.loadedUris.length;
function removeComments(_1){
_1=new String((!_1)?"":_1);
_1=_1.replace(/^(.*?)\/\/(.*)$/mg,"$1");
_1=_1.replace(/(\n)/mg,"__DOJONEWLINE");
_1=_1.replace(/\/\*(.*?)\*\//g,"");
return _1.replace(/__DOJONEWLINE/mg,"\n");
}
dojo.hostenv.getRequiresAndProvides=function(_2){
if(!_2){
return [];
}
var _3=[];
var _4;
RegExp.lastIndex=0;
var _5=/dojo.(hostenv.loadModule|hostenv.require|require|requireIf|kwCompoundRequire|hostenv.conditionalLoadModule|hostenv.startPackage|provide)\([\w\W]*?\)/mg;
while((_4=_5.exec(_2))!=null){
_3.push(_4[0]);
}
return _3;
};
dojo.hostenv.getDelayRequiresAndProvides=function(_6){
if(!_6){
return [];
}
var _7=[];
var _8;
RegExp.lastIndex=0;
var _9=/dojo.(requireAfterIf)\([\w\W]*?\)/mg;
while((_8=_9.exec(_6))!=null){
_7.push(_8[0]);
}
return _7;
};
dojo.clobberLastObject=function(_a){
if(_a.indexOf(".")==-1){
if(!dj_undef(_a,dj_global)){
delete dj_global[_a];
}
return true;
}
var _b=_a.split(/\./);
var _c=dojo.evalObjPath(_b.slice(0,-1).join("."),false);
var _d=_b[_b.length-1];
if(!dj_undef(_d,_c)){
delete _c[_d];
return true;
}
return false;
};
var removals=[];
function zip(_e){
var _f=[];
var _10={};
for(var x=0;x<_e.length;x++){
if(!_10[_e[x]]){
_f.push(_e[x]);
_10[_e[x]]=true;
}
}
return _f;
}
var old_dj_eval=dj_eval;
dj_eval=function(){
return true;
};
dojo.hostenv.oldLoadUri=dojo.hostenv.loadUri;
dojo.hostenv.loadUri=function(uri,cb){
if(dojo.hostenv.loadedUris[uri]){
return true;
}
try{
var _14=this.getText(uri,null,true);
if(!_14){
return false;
}
if(cb){
var _15=old_dj_eval("("+_14+")");
cb(_15);
}else{
var _16=dojo.hostenv.getRequiresAndProvides(_14);
eval(_16.join(";"));
dojo.hostenv.loadedUris.push(uri);
dojo.hostenv.loadedUris[uri]=true;
var _17=dojo.hostenv.getDelayRequiresAndProvides(_14);
eval(_17.join(";"));
}
}
catch(e){
alert(e);
}
return true;
};
dojo.hostenv._writtenIncludes={};
dojo.hostenv.writeIncludes=function(_18){
for(var x=removals.length-1;x>=0;x--){
dojo.clobberLastObject(removals[x]);
}
var _1a=[];
var _1b=dojo.hostenv._writtenIncludes;
for(var x=0;x<dojo.hostenv.loadedUris.length;x++){
var _1c=dojo.hostenv.loadedUris[x];
if(!_1b[_1c]){
_1b[_1c]=true;
_1a.push(_1c);
}
}
dojo.hostenv._global_omit_module_check=true;
for(var x=dojo.hostenv._loadedUrisListStart;x<_1a.length;x++){
document.write("<script type='text/javascript' src='"+_1a[x]+"'></script>");
}
document.write("<script type='text/javascript'>dojo.hostenv._global_omit_module_check = false;</script>");
dojo.hostenv._loadedUrisListStart=0;
if(!_18){
dj_eval=old_dj_eval;
dojo.hostenv.loadUri=dojo.hostenv.oldLoadUri;
}
};
