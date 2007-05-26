dojo.provide("dojo.charting.svg.Plotters");
dojo.require("dojo.lang.common");
if(dojo.render.svg.capable){
dojo.require("dojo.svg");
dojo.mixin(dojo.charting.Plotters,{Bar:function(_1,_2,_3,_4){
var _5=_1.getArea();
var _6=document.createElementNS(dojo.svg.xmlns.svg,"g");
var n=_2.series.length;
var _8=[];
for(var i=0;i<n;i++){
var _a=_2.series[i].data.evaluate(_3);
_8.push(_a);
}
var _b=8;
var _c=_8[0].length;
if(_c==0){
return _6;
}
var _d=((_5.right-_5.left)-(_b*(_c-1)))/_c;
var _e=_d/n;
var _f=_2.axisY.getCoord(_2.axisX.origin,_1,_2);
for(var i=0;i<_c;i++){
var _10=_5.left+(_d*i)+(_b*i);
for(var j=0;j<n;j++){
var _12=_8[j][i].y;
var yA=_f;
var x=_10+(_e*j);
var y=_2.axisY.getCoord(_12,_1,_2);
var h=Math.abs(yA-y);
if(_12<_2.axisX.origin){
yA=y;
y=_f;
}
var bar=document.createElementNS(dojo.svg.xmlns.svg,"rect");
bar.setAttribute("fill",_8[j][i].series.color);
bar.setAttribute("stroke-width","0");
bar.setAttribute("x",x);
bar.setAttribute("y",y);
bar.setAttribute("width",_e);
bar.setAttribute("height",h);
bar.setAttribute("fill-opacity","0.6");
if(_4){
_4(bar,_8[j][i].src);
}
_6.appendChild(bar);
}
}
return _6;
},HorizontalBar:function(_18,_19,_1a,_1b){
var _1c=_18.getArea();
var _1d=document.createElementNS(dojo.svg.xmlns.svg,"g");
var n=_19.series.length;
var _1f=[];
for(var i=0;i<n;i++){
var tmp=_19.series[i].data.evaluate(_1a);
_1f.push(tmp);
}
var _22=6;
var _23=_1f[0].length;
if(_23==0){
return _1d;
}
var h=((_1c.bottom-_1c.top)-(_22*(_23-1)))/_23;
var _25=h/n;
var _26=_19.axisX.getCoord(0,_18,_19);
for(var i=0;i<_23;i++){
var _27=_1c.top+(h*i)+(_22*i);
for(var j=0;j<n;j++){
var _29=_1f[j][i].y;
var y=_27+(_25*j);
var xA=_26;
var x=_19.axisX.getCoord(_29,_18,_19);
var w=Math.abs(x-xA);
if(_29>0){
x=_26;
}
var bar=document.createElementNS(dojo.svg.xmlns.svg,"rect");
bar.setAttribute("fill",_1f[j][i].series.color);
bar.setAttribute("stroke-width","0");
bar.setAttribute("x",xA);
bar.setAttribute("y",y);
bar.setAttribute("width",w);
bar.setAttribute("height",_25);
bar.setAttribute("fill-opacity","0.6");
if(_1b){
_1b(bar,_1f[j][i].src);
}
_1d.appendChild(bar);
}
}
return _1d;
},Gantt:function(_2f,_30,_31,_32){
var _33=_2f.getArea();
var _34=document.createElementNS(dojo.svg.xmlns.svg,"g");
var n=_30.series.length;
var _36=[];
for(var i=0;i<n;i++){
var tmp=_30.series[i].data.evaluate(_31);
_36.push(tmp);
}
var _39=2;
var _3a=_36[0].length;
if(_3a==0){
return _34;
}
var h=((_33.bottom-_33.top)-(_39*(_3a-1)))/_3a;
var _3c=h/n;
for(var i=0;i<_3a;i++){
var _3d=_33.top+(h*i)+(_39*i);
for(var j=0;j<n;j++){
var _3f=_36[j][i].high;
var low=_36[j][i].low;
if(low>_3f){
var t=_3f;
_3f=low;
low=t;
}
var x=_30.axisX.getCoord(low,_2f,_30);
var w=_30.axisX.getCoord(_3f,_2f,_30)-x;
var y=_3d+(_3c*j);
var bar=document.createElementNS(dojo.svg.xmlns.svg,"rect");
bar.setAttribute("fill",_36[j][i].series.color);
bar.setAttribute("stroke-width","0");
bar.setAttribute("x",x);
bar.setAttribute("y",y);
bar.setAttribute("width",w);
bar.setAttribute("height",_3c);
bar.setAttribute("fill-opacity","0.6");
if(_32){
_32(bar,_36[j][i].src);
}
_34.appendChild(bar);
}
}
return _34;
},StackedArea:function(_46,_47,_48,_49){
var _4a=_46.getArea();
var _4b=document.createElementNS(dojo.svg.xmlns.svg,"g");
var n=_47.series.length;
var _4d=[];
var _4e=[];
for(var i=0;i<n;i++){
var tmp=_47.series[i].data.evaluate(_48);
for(var j=0;j<tmp.length;j++){
if(i==0){
_4e.push(tmp[j].y);
}else{
_4e[j]+=tmp[j].y;
}
tmp[j].y=_4e[j];
}
_4d.push(tmp);
}
for(var i=n-1;i>=0;i--){
var _52=document.createElementNS(dojo.svg.xmlns.svg,"path");
_52.setAttribute("fill",_4d[i][0].series.color);
_52.setAttribute("fill-opacity","0.4");
_52.setAttribute("stroke",_4d[i][0].series.color);
_52.setAttribute("stroke-width","1");
_52.setAttribute("stroke-opacity","0.85");
var cmd=[];
var r=3;
for(var j=0;j<_4d[i].length;j++){
var _55=_4d[i];
var x=_47.axisX.getCoord(_55[j].x,_46,_47);
var y=_47.axisY.getCoord(_55[j].y,_46,_47);
if(j==0){
cmd.push("M");
}else{
cmd.push("L");
}
cmd.push(x+","+y);
var c=document.createElementNS(dojo.svg.xmlns.svg,"circle");
c.setAttribute("cx",x);
c.setAttribute("cy",y);
c.setAttribute("r","3");
c.setAttribute("fill",_55[j].series.color);
c.setAttribute("fill-opacity","0.6");
c.setAttribute("stroke-width","1");
c.setAttribute("stroke-opacity","0.85");
_4b.appendChild(c);
if(_49){
_49(c,_4d[i].src);
}
}
if(i==0){
cmd.push("L");
cmd.push(x+","+_47.axisY.getCoord(_47.axisX.origin,_46,_47));
cmd.push("L");
cmd.push(_47.axisX.getCoord(_4d[0][0].x,_46,_47)+","+_47.axisY.getCoord(_47.axisX.origin,_46,_47));
cmd.push("Z");
}else{
var _55=_4d[i-1];
cmd.push("L");
cmd.push(x+","+Math.round(_47.axisY.getCoord(_55[_55.length-1].y,_46,_47)));
for(var j=_55.length-2;j>=0;j--){
var x=_47.axisX.getCoord(_55[j].x,_46,_47);
var y=_47.axisY.getCoord(_55[j].y,_46,_47);
cmd.push("L");
cmd.push(x+","+y);
}
}
_52.setAttribute("d",cmd.join(" ")+" Z");
_4b.appendChild(_52);
}
return _4b;
},StackedCurvedArea:function(_59,_5a,_5b,_5c){
var _5d=3;
var _5e=_59.getArea();
var _5f=document.createElementNS(dojo.svg.xmlns.svg,"g");
var n=_5a.series.length;
var _61=[];
var _62=[];
for(var i=0;i<n;i++){
var tmp=_5a.series[i].data.evaluate(_5b);
for(var j=0;j<tmp.length;j++){
if(i==0){
_62.push(tmp[j].y);
}else{
_62[j]+=tmp[j].y;
}
tmp[j].y=_62[j];
}
_61.push(tmp);
}
for(var i=n-1;i>=0;i--){
var _66=document.createElementNS(dojo.svg.xmlns.svg,"path");
_66.setAttribute("fill",_61[i][0].series.color);
_66.setAttribute("fill-opacity","0.4");
_66.setAttribute("stroke",_61[i][0].series.color);
_66.setAttribute("stroke-width","1");
_66.setAttribute("stroke-opacity","0.85");
var cmd=[];
var r=3;
for(var j=0;j<_61[i].length;j++){
var _69=_61[i];
var x=_5a.axisX.getCoord(_69[j].x,_59,_5a);
var y=_5a.axisY.getCoord(_69[j].y,_59,_5a);
var dx=_5e.left+1;
var dy=_5e.bottom;
if(j>0){
dx=x-_5a.axisX.getCoord(_69[j-1].x,_59,_5a);
dy=_5a.axisY.getCoord(_69[j-1].y,_59,_5a);
}
if(j==0){
cmd.push("M");
}else{
cmd.push("C");
var cx=x-(_5d-1)*(dx/_5d);
cmd.push(cx+","+dy);
cx=x-(dx/_5d);
cmd.push(cx+","+y);
}
cmd.push(x+","+y);
var c=document.createElementNS(dojo.svg.xmlns.svg,"circle");
c.setAttribute("cx",x);
c.setAttribute("cy",y);
c.setAttribute("r","3");
c.setAttribute("fill",_69[j].series.color);
c.setAttribute("fill-opacity","0.6");
c.setAttribute("stroke-width","1");
c.setAttribute("stroke-opacity","0.85");
_5f.appendChild(c);
if(_5c){
_5c(c,_61[i].src);
}
}
if(i==0){
cmd.push("L");
cmd.push(x+","+_5a.axisY.getCoord(_5a.axisX.origin,_59,_5a));
cmd.push("L");
cmd.push(_5a.axisX.getCoord(_61[0][0].x,_59,_5a)+","+_5a.axisY.getCoord(_5a.axisX.origin,_59,_5a));
cmd.push("Z");
}else{
var _69=_61[i-1];
cmd.push("L");
cmd.push(x+","+Math.round(_5a.axisY.getCoord(_69[_69.length-1].y,_59,_5a)));
for(var j=_69.length-2;j>=0;j--){
var x=_5a.axisX.getCoord(_69[j].x,_59,_5a);
var y=_5a.axisY.getCoord(_69[j].y,_59,_5a);
var dx=x-_5a.axisX.getCoord(_69[j+1].x,_59,_5a);
var dy=_5a.axisY.getCoord(_69[j+1].y,_59,_5a);
cmd.push("C");
var cx=x-(_5d-1)*(dx/_5d);
cmd.push(cx+","+dy);
cx=x-(dx/_5d);
cmd.push(cx+","+y);
cmd.push(x+","+y);
}
}
_66.setAttribute("d",cmd.join(" ")+" Z");
_5f.appendChild(_66);
}
return _5f;
},DataBar:function(_70,_71,_72,_73){
var _74=_71.getArea();
var _75=document.createElementNS(dojo.svg.xmlns.svg,"g");
var n=_70.length;
var w=(_74.right-_74.left)/(_72.axisX.range.upper-_72.axisX.range.lower);
var _78=_72.axisY.getCoord(_72.axisX.origin,_71,_72);
for(var i=0;i<n;i++){
var _7a=_70[i].y;
var yA=_78;
var x=_72.axisX.getCoord(_70[i].x,_71,_72)-(w/2);
var y=_72.axisY.getCoord(_7a,_71,_72);
var h=Math.abs(yA-y);
if(_7a<_72.axisX.origin){
yA=y;
y=_78;
}
var bar=document.createElementNS(dojo.svg.xmlns.svg,"rect");
bar.setAttribute("fill",_70[i].series.color);
bar.setAttribute("stroke-width","0");
bar.setAttribute("x",x);
bar.setAttribute("y",y);
bar.setAttribute("width",w);
bar.setAttribute("height",h);
bar.setAttribute("fill-opacity","0.6");
if(_73){
_73(bar,_70[i].src);
}
_75.appendChild(bar);
}
return _75;
},Line:function(_80,_81,_82,_83){
var _84=_81.getArea();
var _85=document.createElementNS(dojo.svg.xmlns.svg,"g");
if(_80.length==0){
return _85;
}
var _86=document.createElementNS(dojo.svg.xmlns.svg,"path");
_85.appendChild(_86);
_86.setAttribute("fill","none");
_86.setAttribute("stroke",_80[0].series.color);
_86.setAttribute("stroke-width","2");
_86.setAttribute("stroke-opacity","0.85");
if(_80[0].series.label!=null){
_86.setAttribute("title",_80[0].series.label);
}
var cmd=[];
for(var i=0;i<_80.length;i++){
var x=_82.axisX.getCoord(_80[i].x,_81,_82);
var y=_82.axisY.getCoord(_80[i].y,_81,_82);
if(i==0){
cmd.push("M");
}else{
cmd.push("L");
}
cmd.push(x+","+y);
var c=document.createElementNS(dojo.svg.xmlns.svg,"circle");
c.setAttribute("cx",x);
c.setAttribute("cy",y);
c.setAttribute("r","3");
c.setAttribute("fill",_80[i].series.color);
c.setAttribute("fill-opacity","0.6");
c.setAttribute("stroke-width","1");
c.setAttribute("stroke-opacity","0.85");
_85.appendChild(c);
if(_83){
_83(c,_80[i].src);
}
}
_86.setAttribute("d",cmd.join(" "));
return _85;
},CurvedLine:function(_8c,_8d,_8e,_8f){
var _90=3;
var _91=_8d.getArea();
var _92=document.createElementNS(dojo.svg.xmlns.svg,"g");
if(_8c.length==0){
return _92;
}
var _93=document.createElementNS(dojo.svg.xmlns.svg,"path");
_92.appendChild(_93);
_93.setAttribute("fill","none");
_93.setAttribute("stroke",_8c[0].series.color);
_93.setAttribute("stroke-width","2");
_93.setAttribute("stroke-opacity","0.85");
if(_8c[0].series.label!=null){
_93.setAttribute("title",_8c[0].series.label);
}
var cmd=[];
for(var i=0;i<_8c.length;i++){
var x=_8e.axisX.getCoord(_8c[i].x,_8d,_8e);
var y=_8e.axisY.getCoord(_8c[i].y,_8d,_8e);
var dx=_91.left+1;
var dy=_91.bottom;
if(i>0){
dx=x-_8e.axisX.getCoord(_8c[i-1].x,_8d,_8e);
dy=_8e.axisY.getCoord(_8c[i-1].y,_8d,_8e);
}
if(i==0){
cmd.push("M");
}else{
cmd.push("C");
var cx=x-(_90-1)*(dx/_90);
cmd.push(cx+","+dy);
cx=x-(dx/_90);
cmd.push(cx+","+y);
}
cmd.push(x+","+y);
var c=document.createElementNS(dojo.svg.xmlns.svg,"circle");
c.setAttribute("cx",x);
c.setAttribute("cy",y);
c.setAttribute("r","3");
c.setAttribute("fill",_8c[i].series.color);
c.setAttribute("fill-opacity","0.6");
c.setAttribute("stroke-width","1");
c.setAttribute("stroke-opacity","0.85");
_92.appendChild(c);
if(_8f){
_8f(c,_8c[i].src);
}
}
_93.setAttribute("d",cmd.join(" "));
return _92;
},Area:function(_9c,_9d,_9e,_9f){
var _a0=_9d.getArea();
var _a1=document.createElementNS(dojo.svg.xmlns.svg,"g");
if(_9c.length==0){
return _a1;
}
var _a2=document.createElementNS(dojo.svg.xmlns.svg,"path");
_a1.appendChild(_a2);
_a2.setAttribute("fill",_9c[0].series.color);
_a2.setAttribute("fill-opacity","0.4");
_a2.setAttribute("stroke",_9c[0].series.color);
_a2.setAttribute("stroke-width","1");
_a2.setAttribute("stroke-opacity","0.85");
if(_9c[0].series.label!=null){
_a2.setAttribute("title",_9c[0].series.label);
}
var cmd=[];
for(var i=0;i<_9c.length;i++){
var x=_9e.axisX.getCoord(_9c[i].x,_9d,_9e);
var y=_9e.axisY.getCoord(_9c[i].y,_9d,_9e);
if(i==0){
cmd.push("M");
}else{
cmd.push("L");
}
cmd.push(x+","+y);
var c=document.createElementNS(dojo.svg.xmlns.svg,"circle");
c.setAttribute("cx",x);
c.setAttribute("cy",y);
c.setAttribute("r","3");
c.setAttribute("fill",_9c[i].series.color);
c.setAttribute("fill-opacity","0.6");
c.setAttribute("stroke-width","1");
c.setAttribute("stroke-opacity","0.85");
_a1.appendChild(c);
if(_9f){
_9f(c,_9c[i].src);
}
}
cmd.push("L");
cmd.push(x+","+_9e.axisY.getCoord(_9e.axisX.origin,_9d,_9e));
cmd.push("L");
cmd.push(_9e.axisX.getCoord(_9c[0].x,_9d,_9e)+","+_9e.axisY.getCoord(_9e.axisX.origin,_9d,_9e));
cmd.push("Z");
_a2.setAttribute("d",cmd.join(" "));
return _a1;
},CurvedArea:function(_a8,_a9,_aa,_ab){
var _ac=3;
var _ad=_a9.getArea();
var _ae=document.createElementNS(dojo.svg.xmlns.svg,"g");
if(_a8.length==0){
return _ae;
}
var _af=document.createElementNS(dojo.svg.xmlns.svg,"path");
_ae.appendChild(_af);
_af.setAttribute("fill",_a8[0].series.color);
_af.setAttribute("fill-opacity","0.4");
_af.setAttribute("stroke",_a8[0].series.color);
_af.setAttribute("stroke-width","1");
_af.setAttribute("stroke-opacity","0.85");
if(_a8[0].series.label!=null){
_af.setAttribute("title",_a8[0].series.label);
}
var cmd=[];
for(var i=0;i<_a8.length;i++){
var x=_aa.axisX.getCoord(_a8[i].x,_a9,_aa);
var y=_aa.axisY.getCoord(_a8[i].y,_a9,_aa);
var dx=_ad.left+1;
var dy=_ad.bottom;
if(i>0){
dx=x-_aa.axisX.getCoord(_a8[i-1].x,_a9,_aa);
dy=_aa.axisY.getCoord(_a8[i-1].y,_a9,_aa);
}
if(i==0){
cmd.push("M");
}else{
cmd.push("C");
var cx=x-(_ac-1)*(dx/_ac);
cmd.push(cx+","+dy);
cx=x-(dx/_ac);
cmd.push(cx+","+y);
}
cmd.push(x+","+y);
var c=document.createElementNS(dojo.svg.xmlns.svg,"circle");
c.setAttribute("cx",x);
c.setAttribute("cy",y);
c.setAttribute("r","3");
c.setAttribute("fill",_a8[i].series.color);
c.setAttribute("fill-opacity","0.6");
c.setAttribute("stroke-width","1");
c.setAttribute("stroke-opacity","0.85");
_ae.appendChild(c);
if(_ab){
_ab(c,_a8[i].src);
}
}
cmd.push("L");
cmd.push(x+","+_aa.axisY.getCoord(_aa.axisX.origin,_a9,_aa));
cmd.push("L");
cmd.push(_aa.axisX.getCoord(_a8[0].x,_a9,_aa)+","+_aa.axisY.getCoord(_aa.axisX.origin,_a9,_aa));
cmd.push("Z");
_af.setAttribute("d",cmd.join(" "));
return _ae;
},HighLow:function(_b8,_b9,_ba,_bb){
var _bc=_b9.getArea();
var _bd=document.createElementNS(dojo.svg.xmlns.svg,"g");
var n=_b8.length;
var _bf=((_bc.right-_bc.left)/(_ba.axisX.range.upper-_ba.axisX.range.lower))/4;
var w=_bf*2;
for(var i=0;i<n;i++){
var _c2=_b8[i].high;
var low=_b8[i].low;
if(low>_c2){
var t=low;
low=_c2;
_c2=t;
}
var x=_ba.axisX.getCoord(_b8[i].x,_b9,_ba)-(w/2);
var y=_ba.axisY.getCoord(_c2,_b9,_ba);
var h=_ba.axisY.getCoord(low,_b9,_ba)-y;
var bar=document.createElementNS(dojo.svg.xmlns.svg,"rect");
bar.setAttribute("fill",_b8[i].series.color);
bar.setAttribute("stroke-width","0");
bar.setAttribute("x",x);
bar.setAttribute("y",y);
bar.setAttribute("width",w);
bar.setAttribute("height",h);
bar.setAttribute("fill-opacity","0.6");
if(_bb){
_bb(bar,_b8[i].src);
}
_bd.appendChild(bar);
}
return _bd;
},HighLowClose:function(_c9,_ca,_cb,_cc){
var _cd=_ca.getArea();
var _ce=document.createElementNS(dojo.svg.xmlns.svg,"g");
var n=_c9.length;
var _d0=((_cd.right-_cd.left)/(_cb.axisX.range.upper-_cb.axisX.range.lower))/4;
var w=_d0*2;
for(var i=0;i<n;i++){
var _d3=_c9[i].high;
var low=_c9[i].low;
if(low>_d3){
var t=low;
low=_d3;
_d3=t;
}
var c=_c9[i].close;
var x=_cb.axisX.getCoord(_c9[i].x,_ca,_cb)-(w/2);
var y=_cb.axisY.getCoord(_d3,_ca,_cb);
var h=_cb.axisY.getCoord(low,_ca,_cb)-y;
var _da=_cb.axisY.getCoord(c,_ca,_cb);
var g=document.createElementNS(dojo.svg.xmlns.svg,"g");
var bar=document.createElementNS(dojo.svg.xmlns.svg,"rect");
bar.setAttribute("fill",_c9[i].series.color);
bar.setAttribute("stroke-width","0");
bar.setAttribute("x",x);
bar.setAttribute("y",y);
bar.setAttribute("width",w);
bar.setAttribute("height",h);
bar.setAttribute("fill-opacity","0.6");
g.appendChild(bar);
var _dd=document.createElementNS(dojo.svg.xmlns.svg,"line");
_dd.setAttribute("x1",x);
_dd.setAttribute("x2",x+w+(_d0*2));
_dd.setAttribute("y1",_da);
_dd.setAttribute("y2",_da);
_dd.setAttribute("style","stroke:"+_c9[i].series.color+";stroke-width:1px;stroke-opacity:0.6;");
g.appendChild(_dd);
if(_cc){
_cc(g,_c9[i].src);
}
_ce.appendChild(g);
}
return _ce;
},HighLowOpenClose:function(_de,_df,_e0,_e1){
var _e2=_df.getArea();
var _e3=document.createElementNS(dojo.svg.xmlns.svg,"g");
var n=_de.length;
var _e5=((_e2.right-_e2.left)/(_e0.axisX.range.upper-_e0.axisX.range.lower))/4;
var w=_e5*2;
for(var i=0;i<n;i++){
var _e8=_de[i].high;
var low=_de[i].low;
if(low>_e8){
var t=low;
low=_e8;
_e8=t;
}
var o=_de[i].open;
var c=_de[i].close;
var x=_e0.axisX.getCoord(_de[i].x,_df,_e0)-(w/2);
var y=_e0.axisY.getCoord(_e8,_df,_e0);
var h=_e0.axisY.getCoord(low,_df,_e0)-y;
var _f0=_e0.axisY.getCoord(o,_df,_e0);
var _f1=_e0.axisY.getCoord(c,_df,_e0);
var g=document.createElementNS(dojo.svg.xmlns.svg,"g");
var bar=document.createElementNS(dojo.svg.xmlns.svg,"rect");
bar.setAttribute("fill",_de[i].series.color);
bar.setAttribute("stroke-width","0");
bar.setAttribute("x",x);
bar.setAttribute("y",y);
bar.setAttribute("width",w);
bar.setAttribute("height",h);
bar.setAttribute("fill-opacity","0.6");
g.appendChild(bar);
var _f4=document.createElementNS(dojo.svg.xmlns.svg,"line");
_f4.setAttribute("x1",x-(_e5*2));
_f4.setAttribute("x2",x+w);
_f4.setAttribute("y1",_f0);
_f4.setAttribute("y2",_f0);
_f4.setAttribute("style","stroke:"+_de[i].series.color+";stroke-width:1px;stroke-opacity:0.6;");
g.appendChild(_f4);
var _f4=document.createElementNS(dojo.svg.xmlns.svg,"line");
_f4.setAttribute("x1",x);
_f4.setAttribute("x2",x+w+(_e5*2));
_f4.setAttribute("y1",_f1);
_f4.setAttribute("y2",_f1);
_f4.setAttribute("style","stroke:"+_de[i].series.color+";stroke-width:1px;stroke-opacity:0.6;");
g.appendChild(_f4);
if(_e1){
_e1(g,_de[i].src);
}
_e3.appendChild(g);
}
return _e3;
},Scatter:function(_f5,_f6,_f7,_f8){
var r=7;
var _fa=document.createElementNS(dojo.svg.xmlns.svg,"g");
for(var i=0;i<_f5.length;i++){
var x=_f7.axisX.getCoord(_f5[i].x,_f6,_f7);
var y=_f7.axisY.getCoord(_f5[i].y,_f6,_f7);
var _fe=document.createElementNS(dojo.svg.xmlns.svg,"path");
_fe.setAttribute("fill",_f5[i].series.color);
_fe.setAttribute("stroke-width","0");
_fe.setAttribute("d","M "+x+","+(y-r)+" "+"Q "+x+","+y+" "+(x+r)+","+y+" "+"Q "+x+","+y+" "+x+","+(y+r)+" "+"Q "+x+","+y+" "+(x-r)+","+y+" "+"Q "+x+","+y+" "+x+","+(y-r)+" "+"Z");
if(_f8){
_f8(_fe,_f5[i].src);
}
_fa.appendChild(_fe);
}
return _fa;
},Bubble:function(_ff,_100,plot,_102){
var _103=document.createElementNS(dojo.svg.xmlns.svg,"g");
var _104=1;
for(var i=0;i<_ff.length;i++){
var x=plot.axisX.getCoord(_ff[i].x,_100,plot);
var y=plot.axisY.getCoord(_ff[i].y,_100,plot);
if(i==0){
var raw=_ff[i].size;
var dy=plot.axisY.getCoord(_ff[i].y+raw,_100,plot)-y;
_104=dy/raw;
}
if(_104<1){
_104=1;
}
var _10a=document.createElementNS(dojo.svg.xmlns.svg,"circle");
_10a.setAttribute("fill",_ff[i].series.color);
_10a.setAttribute("fill-opacity","0.8");
_10a.setAttribute("stroke",_ff[i].series.color);
_10a.setAttribute("stroke-width","1");
_10a.setAttribute("cx",x);
_10a.setAttribute("cy",y);
_10a.setAttribute("r",(_ff[i].size/2)*_104);
if(_102){
_102(_10a,_ff[i].src);
}
_103.appendChild(_10a);
}
return _103;
}});
dojo.charting.Plotters["Default"]=dojo.charting.Plotters.Line;
}
