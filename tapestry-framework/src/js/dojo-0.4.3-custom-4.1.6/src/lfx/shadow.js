dojo.provide("dojo.lfx.shadow");
dojo.require("dojo.lang.common");
dojo.require("dojo.uri.Uri");
dojo.lfx.shadow=function(_1){
this.shadowPng=dojo.uri.moduleUri("dojo.html","images/shadow");
this.shadowThickness=8;
this.shadowOffset=15;
this.init(_1);
};
dojo.extend(dojo.lfx.shadow,{init:function(_2){
this.node=_2;
this.pieces={};
var x1=-1*this.shadowThickness;
var y0=this.shadowOffset;
var y1=this.shadowOffset+this.shadowThickness;
this._makePiece("tl","top",y0,"left",x1);
this._makePiece("l","top",y1,"left",x1,"scale");
this._makePiece("tr","top",y0,"left",0);
this._makePiece("r","top",y1,"left",0,"scale");
this._makePiece("bl","top",0,"left",x1);
this._makePiece("b","top",0,"left",0,"crop");
this._makePiece("br","top",0,"left",0);
},_makePiece:function(_6,_7,_8,_9,_a,_b){
var _c;
var _d=this.shadowPng+_6.toUpperCase()+".png";
if(dojo.render.html.ie55||dojo.render.html.ie60){
_c=dojo.doc().createElement("div");
_c.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+_d+"'"+(_b?", sizingMethod='"+_b+"'":"")+")";
}else{
_c=dojo.doc().createElement("img");
_c.src=_d;
}
_c.style.position="absolute";
_c.style[_7]=_8+"px";
_c.style[_9]=_a+"px";
_c.style.width=this.shadowThickness+"px";
_c.style.height=this.shadowThickness+"px";
this.pieces[_6]=_c;
this.node.appendChild(_c);
},size:function(_e,_f){
var _10=_f-(this.shadowOffset+this.shadowThickness+1);
if(_10<0){
_10=0;
}
if(_f<1){
_f=1;
}
if(_e<1){
_e=1;
}
with(this.pieces){
l.style.height=_10+"px";
r.style.height=_10+"px";
b.style.width=(_e-1)+"px";
bl.style.top=(_f-1)+"px";
b.style.top=(_f-1)+"px";
br.style.top=(_f-1)+"px";
tr.style.left=(_e-1)+"px";
r.style.left=(_e-1)+"px";
br.style.left=(_e-1)+"px";
}
}});
