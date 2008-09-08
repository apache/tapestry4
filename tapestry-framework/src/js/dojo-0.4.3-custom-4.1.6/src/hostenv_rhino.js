dojo.hostenv.println=function(_1){
if(arguments.length>0){
print(arguments[0]);
for(var i=1;i<arguments.length;i++){
var _3=false;
for(var p in arguments[i]){
_3=true;
break;
}
if(_3){
dojo.debugShallow(arguments[i]);
}
}
}else{
print(_1);
}
};
dojo.locale=dojo.locale||java.util.Locale.getDefault().toString().replace("_","-").toLowerCase();
dojo.render.name=dojo.hostenv.name_="rhino";
dojo.hostenv.getVersion=function(){
return version();
};
if(dj_undef("byId")){
dojo.byId=function(id,_6){
if(id&&(typeof id=="string"||id instanceof String)){
if(!_6){
_6=document;
}
return _6.getElementById(id);
}
return id;
};
}
dojo.hostenv.loadUri=function(_7,cb){
try{
var _9=(new java.io.File(_7)).exists();
if(!_9){
try{
var _a=(new java.net.URL(_7)).openStream();
_a.close();
}
catch(e){
return false;
}
}
if(cb){
var _b=(_9?readText:readUri)(_7,"UTF-8");
cb(eval("("+_b+")"));
}else{
load(_7);
}
return true;
}
catch(e){
dojo.debug("rhino load('"+_7+"') failed. Exception: "+e);
return false;
}
};
dojo.hostenv.exit=function(_c){
quit(_c);
};
function dj_rhino_current_script_via_java(_d){
var _e=Packages.org.mozilla.javascript.Context.getCurrentContext().getOptimizationLevel();
var _f=new java.io.CharArrayWriter();
var pw=new java.io.PrintWriter(_f);
var exc=new java.lang.Exception();
var s=_f.toString();
var _13=s.match(/[^\(]*\.js\)/gi);
if(!_13){
throw Error("cannot parse printStackTrace output: "+s);
}
var _14=((typeof _d!="undefined")&&(_d))?_13[_d+1]:_13[_13.length-1];
var _14=_13[3];
if(!_14){
_14=_13[1];
}
if(!_14){
throw Error("could not find js file in printStackTrace output: "+s);
}
return _14;
}
function readText(_15,_16){
_16=_16||"utf-8";
var jf=new java.io.File(_15);
var is=new java.io.FileInputStream(jf);
return dj_readInputStream(is,_16);
}
function readUri(uri,_1a){
var _1b=(new java.net.URL(uri)).openConnection();
_1a=_1a||_1b.getContentEncoding()||"utf-8";
var is=_1b.getInputStream();
return dj_readInputStream(is,_1a);
}
function dj_readInputStream(is,_1e){
var _1f=new java.io.BufferedReader(new java.io.InputStreamReader(is,_1e));
try{
var sb=new java.lang.StringBuffer();
var _21="";
while((_21=_1f.readLine())!==null){
sb.append(_21);
sb.append(java.lang.System.getProperty("line.separator"));
}
return sb.toString();
}
finally{
_1f.close();
}
}
if(!djConfig.libraryScriptUri.length){
try{
djConfig.libraryScriptUri=dj_rhino_current_script_via_java(1);
}
catch(e){
if(djConfig["isDebug"]){
print("\n");
print("we have no idea where Dojo is located.");
print("Please try loading rhino in a non-interpreted mode or set a");
print("\n\tdjConfig.libraryScriptUri\n");
print("Setting the dojo path to './'");
print("This is probably wrong!");
print("\n");
print("Dojo will try to load anyway");
}
djConfig.libraryScriptUri="./";
}
}
dojo.doc=function(){
return document;
};
dojo.body=function(){
return document.body;
};
function setTimeout(_22,_23){
var def={sleepTime:_23,hasSlept:false,run:function(){
if(!this.hasSlept){
this.hasSlept=true;
java.lang.Thread.currentThread().sleep(this.sleepTime);
}
try{
_22();
}
catch(e){
dojo.debug("Error running setTimeout thread:"+e);
}
}};
var _25=new java.lang.Runnable(def);
var _26=new java.lang.Thread(_25);
_26.start();
}
dojo.requireIf((djConfig["isDebug"]||djConfig["debugAtAllCosts"]),"dojo.debug");
