dojo.provide("dojo.lfx.html");
dojo.require("dojo.gfx.color");
dojo.require("dojo.lfx.Animation");
dojo.require("dojo.lang.array");
dojo.require("dojo.html.display");
dojo.require("dojo.html.color");
dojo.require("dojo.html.layout");
dojo.lfx.html._byId=function(_1){
if(!_1){
return [];
}
if(dojo.lang.isArrayLike(_1)){
if(!_1.alreadyChecked){
var n=[];
dojo.lang.forEach(_1,function(_3){
n.push(dojo.byId(_3));
});
n.alreadyChecked=true;
return n;
}else{
return _1;
}
}else{
var n=[];
n.push(dojo.byId(_1));
n.alreadyChecked=true;
return n;
}
};
dojo.lfx.html.propertyAnimation=function(_4,_5,_6,_7,_8){
_4=dojo.lfx.html._byId(_4);
var _9={"propertyMap":_5,"nodes":_4,"duration":_6,"easing":_7||dojo.lfx.easeDefault};
var _a=function(_b){
if(_b.nodes.length==1){
var pm=_b.propertyMap;
if(!dojo.lang.isArray(_b.propertyMap)){
var _d=[];
for(var _e in pm){
pm[_e].property=_e;
_d.push(pm[_e]);
}
pm=_b.propertyMap=_d;
}
dojo.lang.forEach(pm,function(_f){
if(dj_undef("start",_f)){
if(_f.property!="opacity"){
_f.start=parseInt(dojo.html.getComputedStyle(_b.nodes[0],_f.property));
}else{
_f.start=dojo.html.getOpacity(_b.nodes[0]);
}
}
});
}
};
var _10=function(_11){
var _12=[];
dojo.lang.forEach(_11,function(c){
_12.push(Math.round(c));
});
return _12;
};
var _14=function(n,_16){
n=dojo.byId(n);
if(!n||!n.style){
return;
}
for(var s in _16){
try{
if(s=="opacity"){
dojo.html.setOpacity(n,_16[s]);
}else{
n.style[s]=_16[s];
}
}
catch(e){
dojo.debug(e);
}
}
};
var _18=function(_19){
this._properties=_19;
this.diffs=new Array(_19.length);
dojo.lang.forEach(_19,function(_1a,i){
if(dojo.lang.isFunction(_1a.start)){
_1a.start=_1a.start(_1a,i);
}
if(dojo.lang.isFunction(_1a.end)){
_1a.end=_1a.end(_1a,i);
}
if(dojo.lang.isArray(_1a.start)){
this.diffs[i]=null;
}else{
if(_1a.start instanceof dojo.gfx.color.Color){
_1a.startRgb=_1a.start.toRgb();
_1a.endRgb=_1a.end.toRgb();
}else{
this.diffs[i]=_1a.end-_1a.start;
}
}
},this);
this.getValue=function(n){
var ret={};
dojo.lang.forEach(this._properties,function(_1e,i){
var _20=null;
if(dojo.lang.isArray(_1e.start)){
}else{
if(_1e.start instanceof dojo.gfx.color.Color){
_20=(_1e.units||"rgb")+"(";
for(var j=0;j<_1e.startRgb.length;j++){
_20+=Math.round(((_1e.endRgb[j]-_1e.startRgb[j])*n)+_1e.startRgb[j])+(j<_1e.startRgb.length-1?",":"");
}
_20+=")";
}else{
_20=((this.diffs[i])*n)+_1e.start+(_1e.property!="opacity"?_1e.units||"px":"");
}
}
ret[dojo.html.toCamelCase(_1e.property)]=_20;
},this);
return ret;
};
};
var _22=new dojo.lfx.Animation({beforeBegin:function(){
_a(_9);
_22.curve=new _18(_9.propertyMap);
},onAnimate:function(_23){
dojo.lang.forEach(_9.nodes,function(_24){
_14(_24,_23);
});
}},_9.duration,null,_9.easing);
if(_8){
for(var x in _8){
if(dojo.lang.isFunction(_8[x])){
_22.connect(x,_22,_8[x]);
}
}
}
return _22;
};
dojo.lfx.html._makeFadeable=function(_26){
var _27=function(_28){
if(dojo.render.html.ie){
if((_28.style.zoom.length==0)&&(dojo.html.getStyle(_28,"zoom")=="normal")){
_28.style.zoom="1";
}
if((_28.style.width.length==0)&&(dojo.html.getStyle(_28,"width")=="auto")){
_28.style.width="auto";
}
}
};
if(dojo.lang.isArrayLike(_26)){
dojo.lang.forEach(_26,_27);
}else{
_27(_26);
}
};
dojo.lfx.html.fade=function(_29,_2a,_2b,_2c,_2d){
_29=dojo.lfx.html._byId(_29);
var _2e={property:"opacity"};
if(!dj_undef("start",_2a)){
_2e.start=_2a.start;
}else{
_2e.start=function(){
return dojo.html.getOpacity(_29[0]);
};
}
if(!dj_undef("end",_2a)){
_2e.end=_2a.end;
}else{
dojo.raise("dojo.lfx.html.fade needs an end value");
}
var _2f=dojo.lfx.propertyAnimation(_29,[_2e],_2b,_2c);
_2f.connect("beforeBegin",function(){
dojo.lfx.html._makeFadeable(_29);
});
if(_2d){
_2f.connect("onEnd",function(){
_2d(_29,_2f);
});
}
return _2f;
};
dojo.lfx.html.fadeIn=function(_30,_31,_32,_33){
return dojo.lfx.html.fade(_30,{end:1},_31,_32,_33);
};
dojo.lfx.html.fadeOut=function(_34,_35,_36,_37){
return dojo.lfx.html.fade(_34,{end:0},_35,_36,_37);
};
dojo.lfx.html.fadeShow=function(_38,_39,_3a,_3b){
_38=dojo.lfx.html._byId(_38);
dojo.lang.forEach(_38,function(_3c){
dojo.html.setOpacity(_3c,0);
});
var _3d=dojo.lfx.html.fadeIn(_38,_39,_3a,_3b);
_3d.connect("beforeBegin",function(){
if(dojo.lang.isArrayLike(_38)){
dojo.lang.forEach(_38,dojo.html.show);
}else{
dojo.html.show(_38);
}
});
return _3d;
};
dojo.lfx.html.fadeHide=function(_3e,_3f,_40,_41){
var _42=dojo.lfx.html.fadeOut(_3e,_3f,_40,function(){
if(dojo.lang.isArrayLike(_3e)){
dojo.lang.forEach(_3e,dojo.html.hide);
}else{
dojo.html.hide(_3e);
}
if(_41){
_41(_3e,_42);
}
});
return _42;
};
dojo.lfx.html.wipeIn=function(_43,_44,_45,_46){
_43=dojo.lfx.html._byId(_43);
var _47=[];
dojo.lang.forEach(_43,function(_48){
var _49={};
var _4a,_4b,_4c;
with(_48.style){
_4a=top;
_4b=left;
_4c=position;
top="-9999px";
left="-9999px";
position="absolute";
display="";
}
var _4d=dojo.html.getBorderBox(_48).height;
with(_48.style){
top=_4a;
left=_4b;
position=_4c;
display="none";
}
var _4e=dojo.lfx.propertyAnimation(_48,{"height":{start:1,end:function(){
return _4d;
}}},_44,_45);
_4e.connect("beforeBegin",function(){
_49.overflow=_48.style.overflow;
_49.height=_48.style.height;
with(_48.style){
overflow="hidden";
height="1px";
}
dojo.html.show(_48);
});
_4e.connect("onEnd",function(){
with(_48.style){
overflow=_49.overflow;
height=_49.height;
}
if(_46){
_46(_48,_4e);
}
});
_47.push(_4e);
});
return dojo.lfx.combine(_47);
};
dojo.lfx.html.wipeOut=function(_4f,_50,_51,_52){
_4f=dojo.lfx.html._byId(_4f);
var _53=[];
dojo.lang.forEach(_4f,function(_54){
var _55={};
var _56=dojo.lfx.propertyAnimation(_54,{"height":{start:function(){
return dojo.html.getContentBox(_54).height;
},end:1}},_50,_51,{"beforeBegin":function(){
_55.overflow=_54.style.overflow;
_55.height=_54.style.height;
with(_54.style){
overflow="hidden";
}
dojo.html.show(_54);
},"onEnd":function(){
dojo.html.hide(_54);
with(_54.style){
overflow=_55.overflow;
height=_55.height;
}
if(_52){
_52(_54,_56);
}
}});
_53.push(_56);
});
return dojo.lfx.combine(_53);
};
dojo.lfx.html.slideTo=function(_57,_58,_59,_5a,_5b){
_57=dojo.lfx.html._byId(_57);
var _5c=[];
var _5d=dojo.html.getComputedStyle;
if(dojo.lang.isArray(_58)){
dojo.deprecated("dojo.lfx.html.slideTo(node, array)","use dojo.lfx.html.slideTo(node, {top: value, left: value});","0.5");
_58={top:_58[0],left:_58[1]};
}
dojo.lang.forEach(_57,function(_5e){
var top=null;
var _60=null;
var _61=(function(){
var _62=_5e;
return function(){
var pos=_5d(_62,"position");
top=(pos=="absolute"?_5e.offsetTop:parseInt(_5d(_5e,"top"))||0);
_60=(pos=="absolute"?_5e.offsetLeft:parseInt(_5d(_5e,"left"))||0);
if(!dojo.lang.inArray(["absolute","relative"],pos)){
var ret=dojo.html.abs(_62,true);
dojo.html.setStyleAttributes(_62,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");
top=ret.y;
_60=ret.x;
}
};
})();
_61();
var _65=dojo.lfx.propertyAnimation(_5e,{"top":{start:top,end:(_58.top||0)},"left":{start:_60,end:(_58.left||0)}},_59,_5a,{"beforeBegin":_61});
if(_5b){
_65.connect("onEnd",function(){
_5b(_57,_65);
});
}
_5c.push(_65);
});
return dojo.lfx.combine(_5c);
};
dojo.lfx.html.slideBy=function(_66,_67,_68,_69,_6a){
_66=dojo.lfx.html._byId(_66);
var _6b=[];
var _6c=dojo.html.getComputedStyle;
if(dojo.lang.isArray(_67)){
dojo.deprecated("dojo.lfx.html.slideBy(node, array)","use dojo.lfx.html.slideBy(node, {top: value, left: value});","0.5");
_67={top:_67[0],left:_67[1]};
}
dojo.lang.forEach(_66,function(_6d){
var top=null;
var _6f=null;
var _70=(function(){
var _71=_6d;
return function(){
var pos=_6c(_71,"position");
top=(pos=="absolute"?_6d.offsetTop:parseInt(_6c(_6d,"top"))||0);
_6f=(pos=="absolute"?_6d.offsetLeft:parseInt(_6c(_6d,"left"))||0);
if(!dojo.lang.inArray(["absolute","relative"],pos)){
var ret=dojo.html.abs(_71,true);
dojo.html.setStyleAttributes(_71,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");
top=ret.y;
_6f=ret.x;
}
};
})();
_70();
var _74=dojo.lfx.propertyAnimation(_6d,{"top":{start:top,end:top+(_67.top||0)},"left":{start:_6f,end:_6f+(_67.left||0)}},_68,_69).connect("beforeBegin",_70);
if(_6a){
_74.connect("onEnd",function(){
_6a(_66,_74);
});
}
_6b.push(_74);
});
return dojo.lfx.combine(_6b);
};
dojo.lfx.html.explode=function(_75,_76,_77,_78,_79){
var h=dojo.html;
_75=dojo.byId(_75);
_76=dojo.byId(_76);
var _7b=h.toCoordinateObject(_75,true);
var _7c=document.createElement("div");
h.copyStyle(_7c,_76);
if(_76.explodeClassName){
_7c.className=_76.explodeClassName;
}
with(_7c.style){
position="absolute";
display="none";
var _7d=h.getStyle(_75,"background-color");
backgroundColor=_7d?_7d.toLowerCase():"transparent";
backgroundColor=(backgroundColor=="transparent")?"rgb(221, 221, 221)":backgroundColor;
}
dojo.body().appendChild(_7c);
with(_76.style){
visibility="hidden";
display="block";
}
var _7e=h.toCoordinateObject(_76,true);
with(_76.style){
display="none";
visibility="visible";
}
var _7f={opacity:{start:0.5,end:1}};
dojo.lang.forEach(["height","width","top","left"],function(_80){
_7f[_80]={start:_7b[_80],end:_7e[_80]};
});
var _81=new dojo.lfx.propertyAnimation(_7c,_7f,_77,_78,{"beforeBegin":function(){
h.setDisplay(_7c,"block");
},"onEnd":function(){
h.setDisplay(_76,"block");
_7c.parentNode.removeChild(_7c);
}});
if(_79){
_81.connect("onEnd",function(){
_79(_76,_81);
});
}
return _81;
};
dojo.lfx.html.implode=function(_82,end,_84,_85,_86){
var h=dojo.html;
_82=dojo.byId(_82);
end=dojo.byId(end);
var _88=dojo.html.toCoordinateObject(_82,true);
var _89=dojo.html.toCoordinateObject(end,true);
var _8a=document.createElement("div");
dojo.html.copyStyle(_8a,_82);
if(_82.explodeClassName){
_8a.className=_82.explodeClassName;
}
dojo.html.setOpacity(_8a,0.3);
with(_8a.style){
position="absolute";
display="none";
backgroundColor=h.getStyle(_82,"background-color").toLowerCase();
}
dojo.body().appendChild(_8a);
var _8b={opacity:{start:1,end:0.5}};
dojo.lang.forEach(["height","width","top","left"],function(_8c){
_8b[_8c]={start:_88[_8c],end:_89[_8c]};
});
var _8d=new dojo.lfx.propertyAnimation(_8a,_8b,_84,_85,{"beforeBegin":function(){
dojo.html.hide(_82);
dojo.html.show(_8a);
},"onEnd":function(){
_8a.parentNode.removeChild(_8a);
}});
if(_86){
_8d.connect("onEnd",function(){
_86(_82,_8d);
});
}
return _8d;
};
dojo.lfx.html.highlight=function(_8e,_8f,_90,_91,_92){
_8e=dojo.lfx.html._byId(_8e);
var _93=[];
dojo.lang.forEach(_8e,function(_94){
var _95=dojo.html.getBackgroundColor(_94);
var bg=dojo.html.getStyle(_94,"background-color").toLowerCase();
var _97=dojo.html.getStyle(_94,"background-image");
var _98=(bg=="transparent"||bg=="rgba(0, 0, 0, 0)");
while(_95.length>3){
_95.pop();
}
var rgb=new dojo.gfx.color.Color(_8f);
var _9a=new dojo.gfx.color.Color(_95);
var _9b=dojo.lfx.propertyAnimation(_94,{"background-color":{start:rgb,end:_9a}},_90,_91,{"beforeBegin":function(){
if(_97){
_94.style.backgroundImage="none";
}
_94.style.backgroundColor="rgb("+rgb.toRgb().join(",")+")";
},"onEnd":function(){
if(_97){
_94.style.backgroundImage=_97;
}
if(_98){
_94.style.backgroundColor="transparent";
}
if(_92){
_92(_94,_9b);
}
}});
_93.push(_9b);
});
return dojo.lfx.combine(_93);
};
dojo.lfx.html.unhighlight=function(_9c,_9d,_9e,_9f,_a0){
_9c=dojo.lfx.html._byId(_9c);
var _a1=[];
dojo.lang.forEach(_9c,function(_a2){
var _a3=new dojo.gfx.color.Color(dojo.html.getBackgroundColor(_a2));
var rgb=new dojo.gfx.color.Color(_9d);
var _a5=dojo.html.getStyle(_a2,"background-image");
var _a6=dojo.lfx.propertyAnimation(_a2,{"background-color":{start:_a3,end:rgb}},_9e,_9f,{"beforeBegin":function(){
if(_a5){
_a2.style.backgroundImage="none";
}
_a2.style.backgroundColor="rgb("+_a3.toRgb().join(",")+")";
},"onEnd":function(){
if(_a0){
_a0(_a2,_a6);
}
}});
_a1.push(_a6);
});
return dojo.lfx.combine(_a1);
};
dojo.lang.mixin(dojo.lfx,dojo.lfx.html);
