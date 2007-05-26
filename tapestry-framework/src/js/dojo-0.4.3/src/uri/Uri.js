dojo.provide("dojo.uri.Uri");
dojo.uri=new function(){
var _1=new RegExp("^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$");
var _2=new RegExp("(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$");
var _3=new RegExp("/(\\w+.css)");
this.dojoUri=function(_4){
return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri(),_4);
};
this.moduleUri=function(_5,_6){
var _7=dojo.hostenv.getModuleSymbols(_5).join("/");
if(!_7){
return null;
}
if(_7.lastIndexOf("/")!=_7.length-1){
_7+="/";
}
var _8=_7.indexOf(":");
var _9=_7.indexOf("/");
if(_7.charAt(0)!="/"&&(_8==-1||_8>_9)){
_7=dojo.hostenv.getBaseScriptUri()+_7;
}
return new dojo.uri.Uri(_7,_6);
};
this.Uri=function(){
var _a=arguments[0];
if(_a&&arguments.length>1){
var _b=_3.exec(_a);
if(_b){
_a=_a.toString().replace(_b[1],"");
}
}
for(var i=1;i<arguments.length;i++){
if(!arguments[i]){
continue;
}
var _d=new dojo.uri.Uri(arguments[i].toString());
var _e=new dojo.uri.Uri(_a.toString());
if((_d.path=="")&&(_d.scheme==null)&&(_d.authority==null)&&(_d.query==null)){
if(_d.fragment!=null){
_e.fragment=_d.fragment;
}
_d=_e;
}
if(_d.scheme!=null&&_d.authority!=null){
_a="";
}
if(_d.scheme!=null){
_a+=_d.scheme+":";
}
if(_d.authority!=null){
_a+="//"+_d.authority;
}
_a+=_d.path;
if(_d.query!=null){
_a+="?"+_d.query;
}
if(_d.fragment!=null){
_a+="#"+_d.fragment;
}
}
this.uri=_a.toString();
var r=this.uri.match(_2);
this.scheme=r[2]||(r[1]?"":null);
this.authority=r[4]||(r[3]?"":null);
this.path=r[5];
this.query=r[7]||(r[6]?"":null);
this.fragment=r[9]||(r[8]?"":null);
if(this.authority!=null){
r=this.authority.match(_1);
this.user=r[3]||null;
this.password=r[4]||null;
this.host=r[5];
this.port=r[7]||null;
}
this.toString=function(){
return this.uri;
};
};
};
