dojo.provide("dojo.io.cookie");
dojo.io.cookie.setCookie=function(_1,_2,_3,_4,_5,_6){
var _7=-1;
if((typeof _3=="number")&&(_3>=0)){
var d=new Date();
d.setTime(d.getTime()+(_3*24*60*60*1000));
_7=d.toGMTString();
}
_2=escape(_2);
document.cookie=_1+"="+_2+";"+(_7!=-1?" expires="+_7+";":"")+(_4?"path="+_4:"")+(_5?"; domain="+_5:"")+(_6?"; secure":"");
};
dojo.io.cookie.set=dojo.io.cookie.setCookie;
dojo.io.cookie.getCookie=function(_9){
var _a=document.cookie.lastIndexOf(_9+"=");
if(_a==-1){
return null;
}
var _b=document.cookie.substring(_a+_9.length+1);
var _c=_b.indexOf(";");
if(_c==-1){
_c=_b.length;
}
_b=_b.substring(0,_c);
_b=unescape(_b);
return _b;
};
dojo.io.cookie.get=dojo.io.cookie.getCookie;
dojo.io.cookie.deleteCookie=function(_d){
dojo.io.cookie.setCookie(_d,"-",0);
};
dojo.io.cookie.setObjectCookie=function(_e,_f,_10,_11,_12,_13,_14){
if(arguments.length==5){
_14=_12;
_12=null;
_13=null;
}
var _15=[],_16,_17="";
if(!_14){
_16=dojo.io.cookie.getObjectCookie(_e);
}
if(_10>=0){
if(!_16){
_16={};
}
for(var _18 in _f){
if(_f[_18]==null){
delete _16[_18];
}else{
if((typeof _f[_18]=="string")||(typeof _f[_18]=="number")){
_16[_18]=_f[_18];
}
}
}
_18=null;
for(var _18 in _16){
_15.push(escape(_18)+"="+escape(_16[_18]));
}
_17=_15.join("&");
}
dojo.io.cookie.setCookie(_e,_17,_10,_11,_12,_13);
};
dojo.io.cookie.getObjectCookie=function(_19){
var _1a=null,_1b=dojo.io.cookie.getCookie(_19);
if(_1b){
_1a={};
var _1c=_1b.split("&");
for(var i=0;i<_1c.length;i++){
var _1e=_1c[i].split("=");
var _1f=_1e[1];
if(isNaN(_1f)){
_1f=unescape(_1e[1]);
}
_1a[unescape(_1e[0])]=_1f;
}
}
return _1a;
};
dojo.io.cookie.isSupported=function(){
if(typeof navigator.cookieEnabled!="boolean"){
dojo.io.cookie.setCookie("__TestingYourBrowserForCookieSupport__","CookiesAllowed",90,null);
var _20=dojo.io.cookie.getCookie("__TestingYourBrowserForCookieSupport__");
navigator.cookieEnabled=(_20=="CookiesAllowed");
if(navigator.cookieEnabled){
this.deleteCookie("__TestingYourBrowserForCookieSupport__");
}
}
return navigator.cookieEnabled;
};
if(!dojo.io.cookies){
dojo.io.cookies=dojo.io.cookie;
}
