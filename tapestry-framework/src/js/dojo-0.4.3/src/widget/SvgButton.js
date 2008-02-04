dojo.provide("dojo.widget.SvgButton");
dojo.require("dojo.experimental");
dojo.experimental("dojo.widget.SvgButton");
dojo.widget.SvgButton=function(){
dojo.widget.DomButton.call(this);
dojo.widget.SvgWidget.call(this);
this.onFoo=function(){
alert("bar");
};
this.label="huzzah!";
this.setLabel=function(x,y,_3,_4,_5){
var _6=dojo.widget.SvgButton.prototype.coordinates(x,y,_3,_4,_5);
var _7="";
switch(_5){
case "ellipse":
_7="<text x='"+_6[6]+"' y='"+_6[7]+"'>"+_4+"</text>";
break;
case "rectangle":
_7="";
break;
case "circle":
_7="";
break;
}
return _7;
};
this.fillInTemplate=function(x,y,_a,_b,_c){
this.textSize=_a||12;
this.label=_b;
var _d=this.label.length*this.textSize;
};
};
dojo.inherits(dojo.widget.SvgButton,dojo.widget.DomButton);
dojo.widget.SvgButton.prototype.shapeString=function(x,y,_10,_11,_12){
switch(_12){
case "ellipse":
var _13=dojo.widget.SvgButton.prototype.coordinates(x,y,_10,_11,_12);
return "<ellipse cx='"+_13[4]+"' cy='"+_13[5]+"' rx='"+_13[2]+"' ry='"+_13[3]+"'/>";
break;
case "rect":
return "";
break;
case "circle":
return "";
break;
}
};
dojo.widget.SvgButton.prototype.coordinates=function(x,y,_16,_17,_18){
switch(_18){
case "ellipse":
var _19=_17.length*_16;
var _1a=_16*2.5;
var rx=_19/2;
var ry=_1a/2;
var cx=rx+x;
var cy=ry+y;
var _1f=cx-rx*_16/25;
var _20=cy*1.1;
return [_19,_1a,rx,ry,cx,cy,_1f,_20];
break;
case "rectangle":
return "";
break;
case "circle":
return "";
break;
}
};
dojo.widget.SvgButton.prototype.labelString=function(x,y,_23,_24,_25){
var _26="";
var _27=dojo.widget.SvgButton.prototype.coordinates(x,y,_23,_24,_25);
switch(_25){
case "ellipse":
_26="<text x='"+_27[6]+"' y='"+_27[7]+"'>"+_24+"</text>";
break;
case "rectangle":
_26="";
break;
case "circle":
_26="";
break;
}
return _26;
};
dojo.widget.SvgButton.prototype.templateString=function(x,y,_2a,_2b,_2c){
return "<g class='dojoButton' dojoAttachEvent='onClick; onMouseMove: onFoo;' dojoAttachPoint='labelNode'>"+dojo.widgets.SVGButton.prototype.shapeString(x,y,_2a,_2b,_2c)+dojo.widget.SVGButton.prototype.labelString(x,y,_2a,_2b,_2c)+"</g>";
};
