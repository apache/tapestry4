dojo.provide("dojo.i18n.common");
dojo.i18n.getLocalization=function(_1,_2,_3){
dojo.hostenv.preloadLocalizations();
_3=dojo.hostenv.normalizeLocale(_3);
var _4=_3.split("-");
var _5=[_1,"nls",_2].join(".");
var _6=dojo.hostenv.findModule(_5,true);
var _7;
for(var i=_4.length;i>0;i--){
var _9=_4.slice(0,i).join("_");
if(_6[_9]){
_7=_6[_9];
break;
}
}
if(!_7){
_7=_6.ROOT;
}
if(_7){
var _a=function(){
};
_a.prototype=_7;
return new _a();
}
dojo.raise("Bundle not found: "+_2+" in "+_1+" , locale="+_3);
};
dojo.i18n.isLTR=function(_b){
var _c=dojo.hostenv.normalizeLocale(_b).split("-")[0];
var _d={ar:true,fa:true,he:true,ur:true,yi:true};
return !_d[_c];
};
