dojo.provide("dojo.charting.vml.Plotters");
dojo.require("dojo.lang.common");
if(dojo.render.vml.capable){
dojo.mixin(dojo.charting.Plotters,{_group:function(_1){
var _2=document.createElement("div");
_2.style.position="absolute";
_2.style.top="0px";
_2.style.left="0px";
_2.style.width=_1.size.width+"px";
_2.style.height=_1.size.height+"px";
return _2;
},Bar:function(_3,_4,_5,_6){
var _7=_3.getArea();
var _8=dojo.charting.Plotters._group(_3);
var n=_4.series.length;
var _a=[];
for(var i=0;i<n;i++){
var _c=_4.series[i].data.evaluate(_5);
_a.push(_c);
}
var _d=8;
var _e=_a[0].length;
if(_e==0){
return _8;
}
var _f=((_7.right-_7.left)-(_d*(_e-1)))/_e;
var _10=Math.round(_f/n);
var _11=_4.axisY.getCoord(_4.axisX.origin,_3,_4);
for(var i=0;i<_e;i++){
var _12=_7.left+(_f*i)+(_d*i);
for(var j=0;j<n;j++){
var _14=_a[j][i].y;
var yA=_11;
var x=_12+(_10*j);
var y=_4.axisY.getCoord(_14,_3,_4);
var h=Math.abs(yA-y);
if(_14<_4.axisX.origin){
yA=y;
y=_11;
}
var bar=document.createElement("v:rect");
bar.style.position="absolute";
bar.style.top=y+1+"px";
bar.style.left=x+"px";
bar.style.width=_10+"px";
bar.style.height=h+"px";
bar.setAttribute("fillColor",_a[j][i].series.color);
bar.setAttribute("stroked","false");
bar.style.antialias="false";
var _1a=document.createElement("v:fill");
_1a.setAttribute("opacity","0.6");
bar.appendChild(_1a);
if(_6){
_6(bar,_a[j][i].src);
}
_8.appendChild(bar);
}
}
return _8;
},HorizontalBar:function(_1b,_1c,_1d,_1e){
var _1f=_1b.getArea();
var _20=dojo.charting.Plotters._group(_1b);
var n=_1c.series.length;
var _22=[];
for(var i=0;i<n;i++){
var tmp=_1c.series[i].data.evaluate(_1d);
_22.push(tmp);
}
var _25=6;
var _26=_22[0].length;
if(_26==0){
return _20;
}
var h=((_1f.bottom-_1f.top)-(_25*(_26-1)))/_26;
var _28=h/n;
var _29=_1c.axisX.getCoord(0,_1b,_1c);
for(var i=0;i<_26;i++){
var _2a=_1f.top+(h*i)+(_25*i);
for(var j=0;j<n;j++){
var _2c=_22[j][i].y;
var y=_2a+(_28*j);
var xA=_29;
var x=_1c.axisX.getCoord(_2c,_1b,_1c);
var w=Math.abs(x-xA);
if(_2c>0){
x=_29;
}
var bar=document.createElement("v:rect");
bar.style.position="absolute";
bar.style.top=y+1+"px";
bar.style.left=xA+"px";
bar.style.width=w+"px";
bar.style.height=_28+"px";
bar.setAttribute("fillColor",_22[j][i].series.color);
bar.setAttribute("stroked","false");
bar.style.antialias="false";
var _32=document.createElement("v:fill");
_32.setAttribute("opacity","0.6");
bar.appendChild(_32);
if(_1e){
_1e(bar,_22[j][i].src);
}
_20.appendChild(bar);
}
}
var _25=4;
var n=_1c.series.length;
var h=((_1f.bottom-_1f.top)-(_25*(n-1)))/n;
var _29=_1c.axisX.getCoord(0,_1b,_1c);
for(var i=0;i<n;i++){
var _33=_1c.series[i];
var _22=_33.data.evaluate(_1d);
var y=_1f.top+(h*i)+(_25*i);
var _2c=_22[_22.length-1].y;
var xA=_29;
var x=_1c.axisX.getCoord(_2c,_1b,_1c);
var w=Math.abs(xA-x);
if(_2c>0){
xA=x;
x=_29;
}
}
return _20;
},Gantt:function(_34,_35,_36,_37){
var _38=_34.getArea();
var _39=dojo.charting.Plotters._group(_34);
var n=_35.series.length;
var _3b=[];
for(var i=0;i<n;i++){
var tmp=_35.series[i].data.evaluate(_36);
_3b.push(tmp);
}
var _3e=2;
var _3f=_3b[0].length;
if(_3f==0){
return _39;
}
var h=((_38.bottom-_38.top)-(_3e*(_3f-1)))/_3f;
var _41=h/n;
for(var i=0;i<_3f;i++){
var _42=_38.top+(h*i)+(_3e*i);
for(var j=0;j<n;j++){
var _44=_3b[j][i].high;
var low=_3b[j][i].low;
if(low>_44){
var t=_44;
_44=low;
low=t;
}
var x=_35.axisX.getCoord(low,_34,_35);
var w=_35.axisX.getCoord(_44,_34,_35)-x;
var y=_42+(_41*j);
var bar=document.createElement("v:rect");
bar.style.position="absolute";
bar.style.top=y+1+"px";
bar.style.left=x+"px";
bar.style.width=w+"px";
bar.style.height=_41+"px";
bar.setAttribute("fillColor",_3b[j][i].series.color);
bar.setAttribute("stroked","false");
bar.style.antialias="false";
var _4b=document.createElement("v:fill");
_4b.setAttribute("opacity","0.6");
bar.appendChild(_4b);
if(_37){
_37(bar,_3b[j][i].src);
}
_39.appendChild(bar);
}
}
return _39;
},StackedArea:function(_4c,_4d,_4e,_4f){
var _50=_4c.getArea();
var _51=dojo.charting.Plotters._group(_4c);
var n=_4d.series.length;
var _53=[];
var _54=[];
for(var i=0;i<n;i++){
var tmp=_4d.series[i].data.evaluate(_4e);
for(var j=0;j<tmp.length;j++){
if(i==0){
_54.push(tmp[j].y);
}else{
_54[j]+=tmp[j].y;
}
tmp[j].y=_54[j];
}
_53.push(tmp);
}
for(var i=n-1;i>=0;i--){
var _58=document.createElement("v:shape");
_58.setAttribute("strokeweight","1px");
_58.setAttribute("strokecolor",_53[i][0].series.color);
_58.setAttribute("fillcolor",_53[i][0].series.color);
_58.setAttribute("coordsize",(_50.right-_50.left)+","+(_50.bottom-_50.top));
_58.style.position="absolute";
_58.style.top="0px";
_58.style.left="0px";
_58.style.width=_50.right-_50.left+"px";
_58.style.height=_50.bottom-_50.top+"px";
var _59=document.createElement("v:stroke");
_59.setAttribute("opacity","0.8");
_58.appendChild(_59);
var _5a=document.createElement("v:fill");
_5a.setAttribute("opacity","0.4");
_58.appendChild(_5a);
var cmd=[];
var r=3;
for(var j=0;j<_53[i].length;j++){
var _5d=_53[i];
var x=Math.round(_4d.axisX.getCoord(_5d[j].x,_4c,_4d));
var y=Math.round(_4d.axisY.getCoord(_5d[j].y,_4c,_4d));
if(j==0){
cmd.push("m");
cmd.push(x+","+y);
}else{
cmd.push("l");
cmd.push(x+","+y);
}
var c=document.createElement("v:oval");
c.setAttribute("strokeweight","1px");
c.setAttribute("strokecolor",_5d[j].series.color);
c.setAttribute("fillcolor",_5d[j].series.color);
var str=document.createElement("v:stroke");
str.setAttribute("opacity","0.8");
c.appendChild(str);
str=document.createElement("v:fill");
str.setAttribute("opacity","0.6");
c.appendChild(str);
var s=c.style;
s.position="absolute";
s.top=(y-r)+"px";
s.left=(x-r)+"px";
s.width=(r*2)+"px";
s.height=(r*2)+"px";
_51.appendChild(c);
if(_4f){
_4f(c,_53[j].src);
}
}
if(i==0){
cmd.push("l");
cmd.push(x+","+Math.round(_4d.axisY.getCoord(_4d.axisX.origin,_4c,_4d)));
cmd.push("l");
cmd.push(Math.round(_4d.axisX.getCoord(_53[0][0].x,_4c,_4d))+","+Math.round(_4d.axisY.getCoord(_4d.axisX.origin,_4c,_4d)));
}else{
var _5d=_53[i-1];
cmd.push("l");
cmd.push(x+","+Math.round(_4d.axisY.getCoord(_5d[_5d.length-1].y,_4c,_4d)));
for(var j=_5d.length-2;j>=0;j--){
var x=Math.round(_4d.axisX.getCoord(_5d[j].x,_4c,_4d));
var y=Math.round(_4d.axisY.getCoord(_5d[j].y,_4c,_4d));
cmd.push("l");
cmd.push(x+","+y);
}
}
_58.setAttribute("path",cmd.join(" ")+" x e");
_51.appendChild(_58);
}
return _51;
},StackedCurvedArea:function(_63,_64,_65,_66){
var _67=3;
var _68=_63.getArea();
var _69=dojo.charting.Plotters._group(_63);
var n=_64.series.length;
var _6b=[];
var _6c=[];
for(var i=0;i<n;i++){
var tmp=_64.series[i].data.evaluate(_65);
for(var j=0;j<tmp.length;j++){
if(i==0){
_6c.push(tmp[j].y);
}else{
_6c[j]+=tmp[j].y;
}
tmp[j].y=_6c[j];
}
_6b.push(tmp);
}
for(var i=n-1;i>=0;i--){
var _70=document.createElement("v:shape");
_70.setAttribute("strokeweight","1px");
_70.setAttribute("strokecolor",_6b[i][0].series.color);
_70.setAttribute("fillcolor",_6b[i][0].series.color);
_70.setAttribute("coordsize",(_68.right-_68.left)+","+(_68.bottom-_68.top));
_70.style.position="absolute";
_70.style.top="0px";
_70.style.left="0px";
_70.style.width=_68.right-_68.left+"px";
_70.style.height=_68.bottom-_68.top+"px";
var _71=document.createElement("v:stroke");
_71.setAttribute("opacity","0.8");
_70.appendChild(_71);
var _72=document.createElement("v:fill");
_72.setAttribute("opacity","0.4");
_70.appendChild(_72);
var cmd=[];
var r=3;
for(var j=0;j<_6b[i].length;j++){
var _75=_6b[i];
var x=Math.round(_64.axisX.getCoord(_75[j].x,_63,_64));
var y=Math.round(_64.axisY.getCoord(_75[j].y,_63,_64));
if(j==0){
cmd.push("m");
cmd.push(x+","+y);
}else{
var _78=Math.round(_64.axisX.getCoord(_75[j-1].x,_63,_64));
var _79=Math.round(_64.axisY.getCoord(_75[j-1].y,_63,_64));
var dx=x-_78;
var dy=y-_79;
cmd.push("c");
var cx=Math.round((x-(_67-1)*(dx/_67)));
cmd.push(cx+","+_79);
cx=Math.round((x-(dx/_67)));
cmd.push(cx+","+y);
cmd.push(x+","+y);
}
var c=document.createElement("v:oval");
c.setAttribute("strokeweight","1px");
c.setAttribute("strokecolor",_75[j].series.color);
c.setAttribute("fillcolor",_75[j].series.color);
var str=document.createElement("v:stroke");
str.setAttribute("opacity","0.8");
c.appendChild(str);
str=document.createElement("v:fill");
str.setAttribute("opacity","0.6");
c.appendChild(str);
var s=c.style;
s.position="absolute";
s.top=(y-r)+"px";
s.left=(x-r)+"px";
s.width=(r*2)+"px";
s.height=(r*2)+"px";
_69.appendChild(c);
if(_66){
_66(c,_6b[j].src);
}
}
if(i==0){
cmd.push("l");
cmd.push(x+","+Math.round(_64.axisY.getCoord(_64.axisX.origin,_63,_64)));
cmd.push("l");
cmd.push(Math.round(_64.axisX.getCoord(_6b[0][0].x,_63,_64))+","+Math.round(_64.axisY.getCoord(_64.axisX.origin,_63,_64)));
}else{
var _75=_6b[i-1];
cmd.push("l");
cmd.push(x+","+Math.round(_64.axisY.getCoord(_75[_75.length-1].y,_63,_64)));
for(var j=_75.length-2;j>=0;j--){
var x=Math.round(_64.axisX.getCoord(_75[j].x,_63,_64));
var y=Math.round(_64.axisY.getCoord(_75[j].y,_63,_64));
var _78=Math.round(_64.axisX.getCoord(_75[j+1].x,_63,_64));
var _79=Math.round(_64.axisY.getCoord(_75[j+1].y,_63,_64));
var dx=x-_78;
var dy=y-_79;
cmd.push("c");
var cx=Math.round((x-(_67-1)*(dx/_67)));
cmd.push(cx+","+_79);
cx=Math.round((x-(dx/_67)));
cmd.push(cx+","+y);
cmd.push(x+","+y);
}
}
_70.setAttribute("path",cmd.join(" ")+" x e");
_69.appendChild(_70);
}
return _69;
},DataBar:function(_80,_81,_82,_83){
var _84=_81.getArea();
var _85=dojo.charting.Plotters._group(_81);
var n=_80.length;
var w=(_84.right-_84.left)/(_82.axisX.range.upper-_82.axisX.range.lower);
var _88=_82.axisY.getCoord(_82.axisX.origin,_81,_82);
for(var i=0;i<n;i++){
var _8a=_80[i].y;
var yA=_88;
var x=_82.axisX.getCoord(_80[i].x,_81,_82)-(w/2)+1;
var y=_82.axisY.getCoord(_8a,_81,_82);
var h=Math.abs(yA-y);
if(_8a<_82.axisX.origin){
yA=y;
y=_88;
}
var bar=document.createElement("v:rect");
bar.style.position="absolute";
bar.style.top=y+1+"px";
bar.style.left=x+"px";
bar.style.width=w+"px";
bar.style.height=h+"px";
bar.setAttribute("fillColor",_80[i].series.color);
bar.setAttribute("stroked","false");
bar.style.antialias="false";
var _90=document.createElement("v:fill");
_90.setAttribute("opacity","0.6");
bar.appendChild(_90);
if(_83){
_83(bar,_80[i].src);
}
_85.appendChild(bar);
}
return _85;
},Line:function(_91,_92,_93,_94){
var _95=_92.getArea();
var _96=dojo.charting.Plotters._group(_92);
if(_91.length==0){
return _96;
}
var _97=document.createElement("v:shape");
_97.setAttribute("strokeweight","2px");
_97.setAttribute("strokecolor",_91[0].series.color);
_97.setAttribute("fillcolor","none");
_97.setAttribute("filled","false");
_97.setAttribute("coordsize",(_95.right-_95.left)+","+(_95.bottom-_95.top));
_97.style.position="absolute";
_97.style.top="0px";
_97.style.left="0px";
_97.style.width=_95.right-_95.left+"px";
_97.style.height=_95.bottom-_95.top+"px";
var _98=document.createElement("v:stroke");
_98.setAttribute("opacity","0.8");
_97.appendChild(_98);
var cmd=[];
var r=3;
for(var i=0;i<_91.length;i++){
var x=Math.round(_93.axisX.getCoord(_91[i].x,_92,_93));
var y=Math.round(_93.axisY.getCoord(_91[i].y,_92,_93));
if(i==0){
cmd.push("m");
cmd.push(x+","+y);
}else{
cmd.push("l");
cmd.push(x+","+y);
}
var c=document.createElement("v:oval");
c.setAttribute("strokeweight","1px");
c.setAttribute("strokecolor",_91[i].series.color);
c.setAttribute("fillcolor",_91[i].series.color);
var str=document.createElement("v:stroke");
str.setAttribute("opacity","0.8");
c.appendChild(str);
str=document.createElement("v:fill");
str.setAttribute("opacity","0.6");
c.appendChild(str);
var s=c.style;
s.position="absolute";
s.top=(y-r)+"px";
s.left=(x-r)+"px";
s.width=(r*2)+"px";
s.height=(r*2)+"px";
_96.appendChild(c);
if(_94){
_94(c,_91[i].src);
}
}
_97.setAttribute("path",cmd.join(" ")+" e");
_96.appendChild(_97);
return _96;
},CurvedLine:function(_a1,_a2,_a3,_a4){
var _a5=3;
var _a6=_a2.getArea();
var _a7=dojo.charting.Plotters._group(_a2);
if(_a1.length==0){
return _a7;
}
var _a8=document.createElement("v:shape");
_a8.setAttribute("strokeweight","2px");
_a8.setAttribute("strokecolor",_a1[0].series.color);
_a8.setAttribute("fillcolor","none");
_a8.setAttribute("filled","false");
_a8.setAttribute("coordsize",(_a6.right-_a6.left)+","+(_a6.bottom-_a6.top));
_a8.style.position="absolute";
_a8.style.top="0px";
_a8.style.left="0px";
_a8.style.width=_a6.right-_a6.left+"px";
_a8.style.height=_a6.bottom-_a6.top+"px";
var _a9=document.createElement("v:stroke");
_a9.setAttribute("opacity","0.8");
_a8.appendChild(_a9);
var cmd=[];
var r=3;
for(var i=0;i<_a1.length;i++){
var x=Math.round(_a3.axisX.getCoord(_a1[i].x,_a2,_a3));
var y=Math.round(_a3.axisY.getCoord(_a1[i].y,_a2,_a3));
if(i==0){
cmd.push("m");
cmd.push(x+","+y);
}else{
var _af=Math.round(_a3.axisX.getCoord(_a1[i-1].x,_a2,_a3));
var _b0=Math.round(_a3.axisY.getCoord(_a1[i-1].y,_a2,_a3));
var dx=x-_af;
var dy=y-_b0;
cmd.push("c");
var cx=Math.round((x-(_a5-1)*(dx/_a5)));
cmd.push(cx+","+_b0);
cx=Math.round((x-(dx/_a5)));
cmd.push(cx+","+y);
cmd.push(x+","+y);
}
var c=document.createElement("v:oval");
c.setAttribute("strokeweight","1px");
c.setAttribute("strokecolor",_a1[i].series.color);
c.setAttribute("fillcolor",_a1[i].series.color);
var str=document.createElement("v:stroke");
str.setAttribute("opacity","0.8");
c.appendChild(str);
str=document.createElement("v:fill");
str.setAttribute("opacity","0.6");
c.appendChild(str);
var s=c.style;
s.position="absolute";
s.top=(y-r)+"px";
s.left=(x-r)+"px";
s.width=(r*2)+"px";
s.height=(r*2)+"px";
_a7.appendChild(c);
if(_a4){
_a4(c,_a1[i].src);
}
}
_a8.setAttribute("path",cmd.join(" ")+" e");
_a7.appendChild(_a8);
return _a7;
},Area:function(_b7,_b8,_b9,_ba){
var _bb=_b8.getArea();
var _bc=dojo.charting.Plotters._group(_b8);
if(_b7.length==0){
return _bc;
}
var _bd=document.createElement("v:shape");
_bd.setAttribute("strokeweight","1px");
_bd.setAttribute("strokecolor",_b7[0].series.color);
_bd.setAttribute("fillcolor",_b7[0].series.color);
_bd.setAttribute("coordsize",(_bb.right-_bb.left)+","+(_bb.bottom-_bb.top));
_bd.style.position="absolute";
_bd.style.top="0px";
_bd.style.left="0px";
_bd.style.width=_bb.right-_bb.left+"px";
_bd.style.height=_bb.bottom-_bb.top+"px";
var _be=document.createElement("v:stroke");
_be.setAttribute("opacity","0.8");
_bd.appendChild(_be);
var _bf=document.createElement("v:fill");
_bf.setAttribute("opacity","0.4");
_bd.appendChild(_bf);
var cmd=[];
var r=3;
for(var i=0;i<_b7.length;i++){
var x=Math.round(_b9.axisX.getCoord(_b7[i].x,_b8,_b9));
var y=Math.round(_b9.axisY.getCoord(_b7[i].y,_b8,_b9));
if(i==0){
cmd.push("m");
cmd.push(x+","+y);
}else{
cmd.push("l");
cmd.push(x+","+y);
}
var c=document.createElement("v:oval");
c.setAttribute("strokeweight","1px");
c.setAttribute("strokecolor",_b7[i].series.color);
c.setAttribute("fillcolor",_b7[i].series.color);
var str=document.createElement("v:stroke");
str.setAttribute("opacity","0.8");
c.appendChild(str);
str=document.createElement("v:fill");
str.setAttribute("opacity","0.6");
c.appendChild(str);
var s=c.style;
s.position="absolute";
s.top=(y-r)+"px";
s.left=(x-r)+"px";
s.width=(r*2)+"px";
s.height=(r*2)+"px";
_bc.appendChild(c);
if(_ba){
_ba(c,_b7[i].src);
}
}
cmd.push("l");
cmd.push(x+","+Math.round(_b9.axisY.getCoord(_b9.axisX.origin,_b8,_b9)));
cmd.push("l");
cmd.push(Math.round(_b9.axisX.getCoord(_b7[0].x,_b8,_b9))+","+Math.round(_b9.axisY.getCoord(_b9.axisX.origin,_b8,_b9)));
_bd.setAttribute("path",cmd.join(" ")+" x e");
_bc.appendChild(_bd);
return _bc;
},CurvedArea:function(_c8,_c9,_ca,_cb){
var _cc=3;
var _cd=_c9.getArea();
var _ce=dojo.charting.Plotters._group(_c9);
if(_c8.length==0){
return _ce;
}
var _cf=document.createElement("v:shape");
_cf.setAttribute("strokeweight","1px");
_cf.setAttribute("strokecolor",_c8[0].series.color);
_cf.setAttribute("fillcolor",_c8[0].series.color);
_cf.setAttribute("coordsize",(_cd.right-_cd.left)+","+(_cd.bottom-_cd.top));
_cf.style.position="absolute";
_cf.style.top="0px";
_cf.style.left="0px";
_cf.style.width=_cd.right-_cd.left+"px";
_cf.style.height=_cd.bottom-_cd.top+"px";
var _d0=document.createElement("v:stroke");
_d0.setAttribute("opacity","0.8");
_cf.appendChild(_d0);
var _d1=document.createElement("v:fill");
_d1.setAttribute("opacity","0.4");
_cf.appendChild(_d1);
var cmd=[];
var r=3;
for(var i=0;i<_c8.length;i++){
var x=Math.round(_ca.axisX.getCoord(_c8[i].x,_c9,_ca));
var y=Math.round(_ca.axisY.getCoord(_c8[i].y,_c9,_ca));
if(i==0){
cmd.push("m");
cmd.push(x+","+y);
}else{
var _d7=Math.round(_ca.axisX.getCoord(_c8[i-1].x,_c9,_ca));
var _d8=Math.round(_ca.axisY.getCoord(_c8[i-1].y,_c9,_ca));
var dx=x-_d7;
var dy=y-_d8;
cmd.push("c");
var cx=Math.round((x-(_cc-1)*(dx/_cc)));
cmd.push(cx+","+_d8);
cx=Math.round((x-(dx/_cc)));
cmd.push(cx+","+y);
cmd.push(x+","+y);
}
var c=document.createElement("v:oval");
c.setAttribute("strokeweight","1px");
c.setAttribute("strokecolor",_c8[i].series.color);
c.setAttribute("fillcolor",_c8[i].series.color);
var str=document.createElement("v:stroke");
str.setAttribute("opacity","0.8");
c.appendChild(str);
str=document.createElement("v:fill");
str.setAttribute("opacity","0.6");
c.appendChild(str);
var s=c.style;
s.position="absolute";
s.top=(y-r)+"px";
s.left=(x-r)+"px";
s.width=(r*2)+"px";
s.height=(r*2)+"px";
_ce.appendChild(c);
if(_cb){
_cb(c,_c8[i].src);
}
}
cmd.push("l");
cmd.push(x+","+Math.round(_ca.axisY.getCoord(_ca.axisX.origin,_c9,_ca)));
cmd.push("l");
cmd.push(Math.round(_ca.axisX.getCoord(_c8[0].x,_c9,_ca))+","+Math.round(_ca.axisY.getCoord(_ca.axisX.origin,_c9,_ca)));
_cf.setAttribute("path",cmd.join(" ")+" x e");
_ce.appendChild(_cf);
return _ce;
},HighLow:function(_df,_e0,_e1,_e2){
var _e3=_e0.getArea();
var _e4=dojo.charting.Plotters._group(_e0);
var n=_df.length;
var _e6=((_e3.right-_e3.left)/(_e1.axisX.range.upper-_e1.axisX.range.lower))/4;
var w=_e6*2;
for(var i=0;i<n;i++){
var _e9=_df[i].high;
var low=_df[i].low;
if(low>_e9){
var t=low;
low=_e9;
_e9=t;
}
var x=_e1.axisX.getCoord(_df[i].x,_e0,_e1)-(w/2);
var y=_e1.axisY.getCoord(_e9,_e0,_e1);
var h=_e1.axisY.getCoord(low,_e0,_e1)-y;
var bar=document.createElement("v:rect");
bar.style.position="absolute";
bar.style.top=y+1+"px";
bar.style.left=x+"px";
bar.style.width=w+"px";
bar.style.height=h+"px";
bar.setAttribute("fillColor",_df[i].series.color);
bar.setAttribute("stroked","false");
bar.style.antialias="false";
var _f0=document.createElement("v:fill");
_f0.setAttribute("opacity","0.6");
bar.appendChild(_f0);
if(_e2){
_e2(bar,_df[i].src);
}
_e4.appendChild(bar);
}
return _e4;
},HighLowClose:function(_f1,_f2,_f3,_f4){
var _f5=_f2.getArea();
var _f6=dojo.charting.Plotters._group(_f2);
var n=_f1.length;
var _f8=((_f5.right-_f5.left)/(_f3.axisX.range.upper-_f3.axisX.range.lower))/4;
var w=_f8*2;
for(var i=0;i<n;i++){
var _fb=_f1[i].high;
var low=_f1[i].low;
if(low>_fb){
var t=low;
low=_fb;
_fb=t;
}
var c=_f1[i].close;
var x=_f3.axisX.getCoord(_f1[i].x,_f2,_f3)-(w/2);
var y=_f3.axisY.getCoord(_fb,_f2,_f3);
var h=_f3.axisY.getCoord(low,_f2,_f3)-y;
var _102=_f3.axisY.getCoord(c,_f2,_f3);
var g=document.createElement("div");
var bar=document.createElement("v:rect");
bar.style.position="absolute";
bar.style.top=y+1+"px";
bar.style.left=x+"px";
bar.style.width=w+"px";
bar.style.height=h+"px";
bar.setAttribute("fillColor",_f1[i].series.color);
bar.setAttribute("stroked","false");
bar.style.antialias="false";
var fill=document.createElement("v:fill");
fill.setAttribute("opacity","0.6");
bar.appendChild(fill);
g.appendChild(bar);
var line=document.createElement("v:line");
line.setAttribute("strokecolor",_f1[i].series.color);
line.setAttribute("strokeweight","1px");
line.setAttribute("from",x+"px,"+_102+"px");
line.setAttribute("to",(x+w+(_f8*2)-2)+"px,"+_102+"px");
var s=line.style;
s.position="absolute";
s.top="0px";
s.left="0px";
s.antialias="false";
var str=document.createElement("v:stroke");
str.setAttribute("opacity","0.6");
line.appendChild(str);
g.appendChild(line);
if(_f4){
_f4(g,_f1[i].src);
}
_f6.appendChild(g);
}
return _f6;
},HighLowOpenClose:function(data,_10a,plot,_10c){
var area=_10a.getArea();
var _10e=dojo.charting.Plotters._group(_10a);
var n=data.length;
var part=((area.right-area.left)/(plot.axisX.range.upper-plot.axisX.range.lower))/4;
var w=part*2;
for(var i=0;i<n;i++){
var high=data[i].high;
var low=data[i].low;
if(low>high){
var t=low;
low=high;
high=t;
}
var o=data[i].open;
var c=data[i].close;
var x=plot.axisX.getCoord(data[i].x,_10a,plot)-(w/2);
var y=plot.axisY.getCoord(high,_10a,plot);
var h=plot.axisY.getCoord(low,_10a,plot)-y;
var open=plot.axisY.getCoord(o,_10a,plot);
var _11c=plot.axisY.getCoord(c,_10a,plot);
var g=document.createElement("div");
var bar=document.createElement("v:rect");
bar.style.position="absolute";
bar.style.top=y+1+"px";
bar.style.left=x+"px";
bar.style.width=w+"px";
bar.style.height=h+"px";
bar.setAttribute("fillColor",data[i].series.color);
bar.setAttribute("stroked","false");
bar.style.antialias="false";
var fill=document.createElement("v:fill");
fill.setAttribute("opacity","0.6");
bar.appendChild(fill);
g.appendChild(bar);
var line=document.createElement("v:line");
line.setAttribute("strokecolor",data[i].series.color);
line.setAttribute("strokeweight","1px");
line.setAttribute("from",(x-(part*2))+"px,"+open+"px");
line.setAttribute("to",(x+w-2)+"px,"+open+"px");
var s=line.style;
s.position="absolute";
s.top="0px";
s.left="0px";
s.antialias="false";
var str=document.createElement("v:stroke");
str.setAttribute("opacity","0.6");
line.appendChild(str);
g.appendChild(line);
var line=document.createElement("v:line");
line.setAttribute("strokecolor",data[i].series.color);
line.setAttribute("strokeweight","1px");
line.setAttribute("from",x+"px,"+_11c+"px");
line.setAttribute("to",(x+w+(part*2)-2)+"px,"+_11c+"px");
var s=line.style;
s.position="absolute";
s.top="0px";
s.left="0px";
s.antialias="false";
var str=document.createElement("v:stroke");
str.setAttribute("opacity","0.6");
line.appendChild(str);
g.appendChild(line);
if(_10c){
_10c(g,data[i].src);
}
_10e.appendChild(g);
}
return _10e;
},Scatter:function(data,_124,plot,_126){
var r=6;
var mod=r/2;
var area=_124.getArea();
var _12a=dojo.charting.Plotters._group(_124);
for(var i=0;i<data.length;i++){
var x=Math.round(plot.axisX.getCoord(data[i].x,_124,plot));
var y=Math.round(plot.axisY.getCoord(data[i].y,_124,plot));
var _12e=document.createElement("v:rect");
_12e.setAttribute("strokecolor",data[i].series.color);
_12e.setAttribute("fillcolor",data[i].series.color);
var fill=document.createElement("v:fill");
fill.setAttribute("opacity","0.6");
_12e.appendChild(fill);
var s=_12e.style;
s.position="absolute";
s.rotation="45";
s.top=(y-mod)+"px";
s.left=(x-mod)+"px";
s.width=r+"px";
s.height=r+"px";
_12a.appendChild(_12e);
if(_126){
_126(_12e,data[i].src);
}
}
return _12a;
},Bubble:function(data,_132,plot,_134){
var _135=1;
var area=_132.getArea();
var _137=dojo.charting.Plotters._group(_132);
for(var i=0;i<data.length;i++){
var x=Math.round(plot.axisX.getCoord(data[i].x,_132,plot));
var y=Math.round(plot.axisY.getCoord(data[i].y,_132,plot));
if(i==0){
var raw=data[i].size;
var dy=plot.axisY.getCoord(data[i].y+raw,_132,plot)-y;
_135=dy/raw;
}
if(_135<1){
_135=1;
}
var r=(data[i].size/2)*_135;
var _13e=document.createElement("v:oval");
_13e.setAttribute("strokecolor",data[i].series.color);
_13e.setAttribute("fillcolor",data[i].series.color);
var fill=document.createElement("v:fill");
fill.setAttribute("opacity","0.6");
_13e.appendChild(fill);
var s=_13e.style;
s.position="absolute";
s.rotation="45";
s.top=(y-r)+"px";
s.left=(x-r)+"px";
s.width=(r*2)+"px";
s.height=(r*2)+"px";
_137.appendChild(_13e);
if(_134){
_134(_13e,data[i].src);
}
}
return _137;
}});
dojo.charting.Plotters["Default"]=dojo.charting.Plotters.Line;
}
