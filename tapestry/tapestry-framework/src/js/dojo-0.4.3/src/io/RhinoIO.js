dojo.provide("dojo.io.RhinoIO");
dojo.require("dojo.io.common");
dojo.require("dojo.lang.func");
dojo.require("dojo.lang.array");
dojo.require("dojo.string.extras");
dojo.io.RhinoHTTPTransport=new function(){
this.canHandle=function(_1){
if(dojo.lang.find(["text/plain","text/html","text/xml","text/javascript","text/json","application/json"],(_1.mimetype.toLowerCase()||""))<0){
return false;
}
if(_1.url.substr(0,7)!="http://"){
return false;
}
return true;
};
function doLoad(_2,_3){
var _4;
if(_2.method.toLowerCase()=="head"){
}else{
var _5=_3.getContent();
var _6=new java.io.BufferedReader(new java.io.InputStreamReader(_5));
var _7="";
var _8=null;
while((_8=_6.readLine())!=null){
_7+=_8;
}
if(_2.mimetype=="text/javascript"){
try{
_4=dj_eval(_7);
}
catch(e){
dojo.debug(e);
dojo.debug(_7);
_4=null;
}
}else{
if(_2.mimetype=="text/json"||_2.mimetype=="application/json"){
try{
_4=dj_eval("("+_7+")");
}
catch(e){
dojo.debug(e);
dojo.debug(_7);
_4=false;
}
}else{
_4=_7;
}
}
}
_2.load("load",_4,_2);
}
function connect(_9){
var _a=_9.content||{};
var _b;
if(_9.sendTransport){
_a["dojo.transport"]="rhinohttp";
}
if(_9.postContent){
_b=_9.postContent;
}else{
_b=dojo.io.argsFromMap(_a,_9.encoding);
}
var _c=_9.url;
if(_9.method.toLowerCase()=="get"&&_b!=""){
_c=_c+"?"+_b;
}
var _d=new java.net.URL(_c);
var _e=_d.openConnection();
_e.setRequestMethod(_9.method.toUpperCase());
if(_9.headers){
for(var _f in _9.headers){
if(_f.toLowerCase()=="content-type"&&!_9.contentType){
_9.contentType=_9.headers[_f];
}else{
_e.setRequestProperty(_f,_9.headers[_f]);
}
}
}
if(_9.contentType){
_e.setRequestProperty("Content-Type",_9.contentType);
}
if(_9.method.toLowerCase()=="post"){
_e.setDoOutput(true);
var _10=_e.getOutputStream();
var _11=(new java.lang.String(_b)).getBytes();
_10.write(_11,0,_11.length);
}
_e.connect();
doLoad(_9,_e);
}
this.bind=function(req){
var _13=req["sync"]?false:true;
if(_13){
setTimeout(dojo.lang.hitch(this,function(){
connect(req);
}),1);
}else{
connect(req);
}
};
dojo.io.transports.addTransport("RhinoHTTPTransport");
};
