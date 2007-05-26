dojo.provide("dojo.lfx.extras");
dojo.require("dojo.lfx.html");
dojo.require("dojo.lfx.Animation");
dojo.lfx.html.fadeWipeIn=function(_1,_2,_3,_4){
_1=dojo.lfx.html._byId(_1);
var _5=dojo.lfx.combine(dojo.lfx.fadeIn(_1,_2,_3),dojo.lfx.wipeIn(_1,_2,_3));
if(_4){
_5.connect("onEnd",function(){
_4(_1,_5);
});
}
return _5;
};
dojo.lfx.html.fadeWipeOut=function(_6,_7,_8,_9){
_6=dojo.lfx.html._byId(_6);
var _a=dojo.lfx.combine(dojo.lfx.fadeOut(_6,_7,_8),dojo.lfx.wipeOut(_6,_7,_8));
if(_9){
_a.connect("onEnd",function(){
_9(_6,_a);
});
}
return _a;
};
dojo.lfx.html.scale=function(_b,_c,_d,_e,_f,_10,_11){
_b=dojo.lfx.html._byId(_b);
var _12=[];
dojo.lang.forEach(_b,function(_13){
var _14=dojo.html.getMarginBox(_13);
var _15=_c/100;
var _16=[{property:"width",start:_14.width,end:_14.width*_15},{property:"height",start:_14.height,end:_14.height*_15}];
if(_d){
var _17=dojo.html.getStyle(_13,"font-size");
var _18=null;
if(!_17){
_17=parseFloat("100%");
_18="%";
}else{
dojo.lang.some(["em","px","%"],function(_19,_1a,arr){
if(_17.indexOf(_19)>0){
_17=parseFloat(_17);
_18=_19;
return true;
}
});
}
_16.push({property:"font-size",start:_17,end:_17*_15,units:_18});
}
if(_e){
var _1c=dojo.html.getStyle(_13,"position");
var _1d=_13.offsetTop;
var _1e=_13.offsetLeft;
var _1f=((_14.height*_15)-_14.height)/2;
var _20=((_14.width*_15)-_14.width)/2;
_16.push({property:"top",start:_1d,end:(_1c=="absolute"?_1d-_1f:(-1*_1f))});
_16.push({property:"left",start:_1e,end:(_1c=="absolute"?_1e-_20:(-1*_20))});
}
var _21=dojo.lfx.propertyAnimation(_13,_16,_f,_10);
if(_11){
_21.connect("onEnd",function(){
_11(_13,_21);
});
}
_12.push(_21);
});
return dojo.lfx.combine(_12);
};
dojo.lang.mixin(dojo.lfx,dojo.lfx.html);
