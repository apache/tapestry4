dojo.provide("dojo.string.common");
dojo.string.trim=function(_1,wh){
if(!_1.replace){
return _1;
}
if(!_1.length){
return _1;
}
var re=(wh>0)?(/^\s+/):(wh<0)?(/\s+$/):(/^\s+|\s+$/g);
return _1.replace(re,"");
};
dojo.string.trimStart=function(_4){
return dojo.string.trim(_4,1);
};
dojo.string.trimEnd=function(_5){
return dojo.string.trim(_5,-1);
};
dojo.string.repeat=function(_6,_7,_8){
var _9="";
for(var i=0;i<_7;i++){
_9+=_6;
if(_8&&i<_7-1){
_9+=_8;
}
}
return _9;
};
dojo.string.pad=function(_b,_c,c,_e){
var _f=String(_b);
if(!c){
c="0";
}
if(!_e){
_e=1;
}
while(_f.length<_c){
if(_e>0){
_f=c+_f;
}else{
_f+=c;
}
}
return _f;
};
dojo.string.padLeft=function(str,len,c){
return dojo.string.pad(str,len,c,1);
};
dojo.string.padRight=function(str,len,c){
return dojo.string.pad(str,len,c,-1);
};
