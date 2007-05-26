dojo.render.name=dojo.hostenv.name_="dashboard";
dojo.hostenv.println=function(_1){
return alert(_1);
};
dojo.hostenv.getXmlhttpObject=function(_2){
if(widget.system&&_2){
if((_2.contentType&&_2.contentType.indexOf("text/")!=0)||(_2.headers&&_2.headers["content-type"]&&_2.headers["content-type"].indexOf("text/")!=0)){
var _3=new dojo.hostenv.CurlRequest;
_3._save=true;
return _3;
}else{
if(_2.method&&_2.method.toUpperCase()=="HEAD"){
return new dojo.hostenv.CurlRequest;
}else{
if(_2.headers&&_2.header.referer){
return new dojo.hostenv.CurlRequest;
}
}
}
}
return new XMLHttpRequest;
};
dojo.hostenv.CurlRequest=function(){
this.onreadystatechange=null;
this.readyState=0;
this.responseText="";
this.responseXML=null;
this.status=0;
this.statusText="";
this._method="";
this._url="";
this._async=true;
this._referrer="";
this._headers=[];
this._save=false;
this._responseHeader="";
this._responseHeaders={};
this._fileName="";
this._username="";
this._password="";
};
dojo.hostenv.CurlRequest.prototype.open=function(_4,_5,_6,_7,_8){
this._method=_4;
this._url=_5;
if(_6){
this._async=_6;
}
if(_7){
this._username=_7;
}
if(_8){
this._password=_8;
}
};
dojo.hostenv.CurlRequest.prototype.setRequestHeader=function(_9,_a){
switch(_9){
case "Referer":
this._referrer=_a;
break;
case "content-type":
break;
default:
this._headers.push(_9+"="+_a);
break;
}
};
dojo.hostenv.CurlRequest.prototype.getAllResponseHeaders=function(){
return this._responseHeader;
};
dojo.hostenv.CurlRequest.prototype.getResponseHeader=function(_b){
return this._responseHeaders[_b];
};
dojo.hostenv.CurlRequest.prototype.send=function(_c){
this.readyState=1;
if(this.onreadystatechange){
this.onreadystatechange.call(this);
}
var _d={sS:""};
if(this._referrer){
_d.e=this._referrer;
}
if(this._headers.length){
_d.H=this._headers.join("&");
}
if(this._username){
if(this._password){
_d.u=this._username+":"+this._password;
}else{
_d.u=this._username;
}
}
if(_c){
_d.d=this.content;
if(this._method!="POST"){
_d.G="";
}
}
if(this._method=="HEAD"){
_d.I="";
}else{
if(this._save){
_d.I="";
}else{
_d.i="";
}
}
var _e=widget.system(dojo.hostenv.CurlRequest._formatCall(_d,this._url),null);
this.readyState=2;
if(this.onreadystatechange){
this.onreadystatechange.call(this);
}
if(_e.errorString){
this.responseText=_e.errorString;
this.status=0;
}else{
if(this._save){
this._responseHeader=_e.outputString;
}else{
var _f=_e.outputString.replace(/\r/g,"").split("\n\n",2);
this._responseHeader=_f[0];
this.responseText=_f[1];
}
_f=this._responseHeader.split("\n");
this.statusText=_f.shift();
this.status=this.statusText.split(" ")[1];
for(var i=0,_11;_11=_f[i];i++){
var _12=_11.split(": ",2);
this._responseHeaders[_12[0]]=_12[1];
}
if(this._save){
widget.system("/bin/mkdir cache",null);
this._fileName=this._url.split("/").pop().replace(/\W/g,"");
this._fileName+="."+this._responseHeaders["Content-Type"].replace(/[\r\n]/g,"").split("/").pop();
delete _d.I;
_d.o="cache/"+this._fileName;
_e=widget.system(dojo.hostenv.CurlRequest._formatCall(_d,this._url),null);
if(!_e.errorString){
this.responseText="cache/"+this._fileName;
}
}else{
if(this._method=="HEAD"){
this.responseText=this._responseHeader;
}
}
}
this.readyState=4;
if(this.onreadystatechange){
this.onreadystatechange.call(this);
}
};
dojo.hostenv.CurlRequest._formatCall=function(_13,url){
var _15=["/usr/bin/curl"];
for(var key in _13){
if(_13[key]!=""){
_15.push("-"+key+" '"+_13[key].replace(/'/g,"'")+"'");
}else{
_15.push("-"+key);
}
}
_15.push("'"+url.replace(/'/g,"'")+"'");
return _15.join(" ");
};
dojo.hostenv.exit=function(){
if(widget.system){
widget.system("/bin/rm -rf cache/*",null);
}
};
