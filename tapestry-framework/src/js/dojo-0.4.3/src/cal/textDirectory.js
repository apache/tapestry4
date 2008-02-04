dojo.provide("dojo.cal.textDirectory");
dojo.require("dojo.string");
dojo.cal.textDirectory.Property=function(_1){
var _2=dojo.string.trim(_1.substring(0,_1.indexOf(":")));
var _3=dojo.string.trim(_1.substr(_1.indexOf(":")+1));
var _4=dojo.string.splitEscaped(_2,";");
this.name=_4[0];
_4.splice(0,1);
this.params=[];
var _5;
for(var i=0;i<_4.length;i++){
_5=_4[i].split("=");
var _7=dojo.string.trim(_5[0].toUpperCase());
if(_5.length==1){
this.params.push([_7]);
continue;
}
var _8=dojo.string.splitEscaped(_5[1],",");
for(var j=0;j<_8.length;j++){
if(dojo.string.trim(_8[j])!=""){
this.params.push([_7,dojo.string.trim(_8[j])]);
}
}
}
if(this.name.indexOf(".")>0){
_5=this.name.split(".");
this.group=_5[0];
this.name=_5[1];
}
this.value=_3;
};
dojo.cal.textDirectory.tokenise=function(_a){
var _b=dojo.string.normalizeNewlines(_a,"\n").replace(/\n[ \t]/g,"").replace(/\x00/g,"");
var _c=_b.split("\n");
var _d=[];
for(var i=0;i<_c.length;i++){
if(dojo.string.trim(_c[i])==""){
continue;
}
var _f=new dojo.cal.textDirectory.Property(_c[i]);
_d.push(_f);
}
return _d;
};
