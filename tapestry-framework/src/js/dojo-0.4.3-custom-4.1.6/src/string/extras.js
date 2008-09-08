dojo.provide("dojo.string.extras");
dojo.require("dojo.string.common");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.array");
dojo.string.substituteParams=function(_1,_2){
var _3=(typeof _2=="object")?_2:dojo.lang.toArray(arguments,1);
return _1.replace(/\%\{(\w+)\}/g,function(_4,_5){
if(typeof (_3[_5])!="undefined"&&_3[_5]!=null){
return _3[_5];
}
dojo.raise("Substitution not found: "+_5);
});
};
dojo.string.capitalize=function(_6){
if(!dojo.lang.isString(_6)){
return "";
}
if(arguments.length==0){
_6=this;
}
var _7=_6.split(" ");
for(var i=0;i<_7.length;i++){
_7[i]=_7[i].charAt(0).toUpperCase()+_7[i].substring(1);
}
return _7.join(" ");
};
dojo.string.isBlank=function(_9){
if(!dojo.lang.isString(_9)){
return true;
}
return (dojo.string.trim(_9).length==0);
};
dojo.string.encodeAscii=function(_a){
if(!dojo.lang.isString(_a)){
return _a;
}
var _b="";
var _c=escape(_a);
var _d,re=/%u([0-9A-F]{4})/i;
while((_d=_c.match(re))){
var _f=Number("0x"+_d[1]);
var _10=escape("&#"+_f+";");
_b+=_c.substring(0,_d.index)+_10;
_c=_c.substring(_d.index+_d[0].length);
}
_b+=_c.replace(/\+/g,"%2B");
return _b;
};
dojo.string.escape=function(_11,str){
var _13=dojo.lang.toArray(arguments,1);
switch(_11.toLowerCase()){
case "xml":
case "html":
case "xhtml":
return dojo.string.escapeXml.apply(this,_13);
case "sql":
return dojo.string.escapeSql.apply(this,_13);
case "regexp":
case "regex":
return dojo.string.escapeRegExp.apply(this,_13);
case "javascript":
case "jscript":
case "js":
return dojo.string.escapeJavaScript.apply(this,_13);
case "ascii":
return dojo.string.encodeAscii.apply(this,_13);
default:
return str;
}
};
dojo.string.escapeXml=function(str,_15){
str=str.replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");
if(!_15){
str=str.replace(/'/gm,"&#39;");
}
return str;
};
dojo.string.escapeSql=function(str){
return str.replace(/'/gm,"''");
};
dojo.string.escapeRegExp=function(str){
return str.replace(/\\/gm,"\\\\").replace(/([\f\b\n\t\r[\^$|?*+(){}])/gm,"\\$1");
};
dojo.string.escapeJavaScript=function(str){
return str.replace(/(["'\f\b\n\t\r])/gm,"\\$1");
};
dojo.string.escapeString=function(str){
return ("\""+str.replace(/(["\\])/g,"\\$1")+"\"").replace(/[\f]/g,"\\f").replace(/[\b]/g,"\\b").replace(/[\n]/g,"\\n").replace(/[\t]/g,"\\t").replace(/[\r]/g,"\\r");
};
dojo.string.summary=function(str,len){
if(!len||str.length<=len){
return str;
}
return str.substring(0,len).replace(/\.+$/,"")+"...";
};
dojo.string.endsWith=function(str,end,_1e){
if(_1e){
str=str.toLowerCase();
end=end.toLowerCase();
}
if((str.length-end.length)<0){
return false;
}
return str.lastIndexOf(end)==str.length-end.length;
};
dojo.string.endsWithAny=function(str){
for(var i=1;i<arguments.length;i++){
if(dojo.string.endsWith(str,arguments[i])){
return true;
}
}
return false;
};
dojo.string.startsWith=function(str,_22,_23){
if(_23){
str=str.toLowerCase();
_22=_22.toLowerCase();
}
return str.indexOf(_22)==0;
};
dojo.string.startsWithAny=function(str){
for(var i=1;i<arguments.length;i++){
if(dojo.string.startsWith(str,arguments[i])){
return true;
}
}
return false;
};
dojo.string.has=function(str){
for(var i=1;i<arguments.length;i++){
if(str.indexOf(arguments[i])>-1){
return true;
}
}
return false;
};
dojo.string.normalizeNewlines=function(_28,_29){
if(_29=="\n"){
_28=_28.replace(/\r\n/g,"\n");
_28=_28.replace(/\r/g,"\n");
}else{
if(_29=="\r"){
_28=_28.replace(/\r\n/g,"\r");
_28=_28.replace(/\n/g,"\r");
}else{
_28=_28.replace(/([^\r])\n/g,"$1\r\n").replace(/\r([^\n])/g,"\r\n$1");
}
}
return _28;
};
dojo.string.splitEscaped=function(str,_2b){
var _2c=[];
for(var i=0,_2e=0;i<str.length;i++){
if(str.charAt(i)=="\\"){
i++;
continue;
}
if(str.charAt(i)==_2b){
_2c.push(str.substring(_2e,i));
_2e=i+1;
}
}
_2c.push(str.substr(_2e));
return _2c;
};
