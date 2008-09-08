dojo.provide("dojo.widget.svg.HslColorPicker");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.widget.HslColorPicker");
dojo.require("dojo.math");
dojo.require("dojo.svg");
dojo.require("dojo.gfx.color");
dojo.require("dojo.gfx.color.hsl");
dojo.require("dojo.experimental");
dojo.experimental("dojo.widget.svg.HslColorPicker");
dojo.widget.defineWidget("dojo.widget.svg.HslColorPicker",dojo.widget.HtmlWidget,function(){
dojo.debug("warning: the HslColorPicker is not a finished widget, and is not yet ready for general use");
this.filterObject={};
},{hue:"0",saturation:"0",light:"0",storedColor:"#0054aa",templateString:"<svg xmlns=\"http://www.w3.org/2000/svg\"\n\txmlns:xlink=\"http://www.w3.org/1999/xlink\"\n\tversion=\"1.1\" baseProfile=\"full\" width=\"170\" height=\"131\" xmlns:html=\"http://www.w3.org/1999/xhtml\">\n\t<defs>\n\t<linearGradient id=\"colorGradient\" dojoAttachPoint=\"colorGradientNode\" x1=\"0\" x2=\"131\" y1=\"0\" y2=\"0\" gradientUnits=\"userSpaceOnUse\">\n\t\t<stop id=\"leftGradientColor\" dojoAttachPoint=\"leftGradientColorNode\" offset=\"0%\" stop-color=\"#828282\"/>\n\t\t<stop id=\"rightGradientColor\" dojoAttachPoint=\"rightGradientColorNode\" offset=\"100%\" stop-color=\"#053fff\"/>\n\t</linearGradient>\n\t<linearGradient id=\"verticalGradient\" x1=\"0\" x2=\"0\" y1=\"0\" y2=\"131\" gradientUnits=\"userSpaceOnUse\">\n\t\t<stop offset=\"0%\" style=\"stop-color:#000000;\"/>\n\t\t<stop offset=\"50%\" style=\"stop-color:#000000;stop-opacity:0;\"/>\n\t\t<stop offset=\"50%\" style=\"stop-color:#ffffff;stop-opacity:0;\"/>\n\t\t<stop offset=\"100%\" style=\"stop-color:#ffffff;\"/>\n\t</linearGradient>\n\t<linearGradient id=\"sliderGradient\">\n\t\t<stop offset=\"0%\" style=\"stop-color:#000000;\"/>\n\t\t<stop offset=\"15%\" style=\"stop-color:#ffffff;\"/>\n\t\t<stop offset=\"30%\" style=\"stop-color:#000000;\"/>\n\t\t<stop offset=\"45%\" style=\"stop-color:#ffffff;\"/>\n\t\t<stop offset=\"60%\" style=\"stop-color:#000000;\"/>\n\t\t<stop offset=\"75%\" style=\"stop-color:#ffffff;\"/>\n\t\t<stop offset=\"90%\" style=\"stop-color:#000000;\"/>\n\t</linearGradient>\n</defs>\n\t<rect x=\"0\" y=\"0\" width=\"131px\" height=\"131px\" fill=\"url(#colorGradient)\"/>\n\t<rect x=\"0\" y=\"0\" width=\"131px\" height=\"131px\" style=\"fill:url(#verticalGradient);\"/>\n\t<rect id=\"saturationLightSlider\" dojoAttachPoint=\"saturationLightSliderNode\" x=\"100\" y=\"100\" width=\"5px\" height=\"5px\" style=\"stroke:url(#sliderGradient);stroke-width:1px;fill-opacity:0;\"/>\n\t<image xlink:href=\"images/hue.png\" dojoAttachPoint=\"hueNode\" x=\"140px\" y=\"0px\" width=\"21px\" height=\"131px\" dojoAttachEvent=\"onclick: onHueClick;\"/>\n\t<rect dojoAttachPoint=\"hueSliderNode\" x=\"139px\" y=\"40px\" width=\"24px\" height=\"4px\" style=\"stroke-opacity:1;fill-opacity:0;stroke:black;\"/>\n</svg>\n",fillInTemplate:function(){
this.height="131px";
this.svgDoc=this.hueNode.ownerDocument;
this.leftGradientColorNode=this.hueNode.ownerDocument.getElementById("leftGradientColor");
this.rightGradientColorNode=this.hueNode.ownerDocument.getElementById("rightGradientColor");
this.hueNode.setAttributeNS(dojo.dom.xmlns.xlink,"href",dojo.uri.moduleUri("dojo.widget","templates/images/hue.png"));
var _1=dojo.gfx.color.hex2hsl(this.storedColor);
this.hue=_1[0];
this.saturation=_1[1];
this.light=_1[2];
this.setSaturationStopColors();
},setSaturationStopColors:function(){
this.leftGradientStopColor=dojo.gfx.color.rgb2hex(this.hsl2rgb(this.hue,0,50));
this.rightGradientStopColor=dojo.gfx.color.rgb2hex(this.hsl2rgb(this.hue,100,50));
this.leftGradientColorNode.setAttributeNS(null,"stop-color",this.leftGradientStopColor);
this.rightGradientColorNode.setAttributeNS(null,"stop-color",this.rightGradientStopColor);
},setHue:function(_2){
this.hue=_2;
},setHueSlider:function(){
this.hueSliderNode.setAttribute("y",parseInt((this.hue/360)*parseInt(this.height)-2)+"px");
},setSaturationLight:function(_3,_4){
this.saturation=_3;
this.light=_4;
},setSaturationLightSlider:function(){
},onHueClick:function(_5){
var _6=parseInt(_5.clientY)-parseInt(_5.target.getAttribute("y"));
this.setHue(360-parseInt(_6*(360/parseInt(this.height))));
this.setSaturationStopColors();
this.setStoredColor(dojo.gfx.color.hsl2hex(this.hue,this.saturation,this.light));
},onHueDrag:function(_7){
},onSaturationLightClick:function(_8){
var _9=parseInt(_8.clientX)-parseInt(_8.target.getAttribute("y"));
var _a=parseInt(_8.clientY)-parseInt(_8.target.getAttribute("y"));
var _b=parseInt(parseInt(_9)*(101/106));
var _c=parseInt(parseInt(_a)*(101/106));
this.setSaturationLight(_b,_c);
this.setStoredColor(dojo.gfx.color.hsl2hex(this.hue,this.saturation,this.light));
},onSaturationLightDrag:function(_d){
},getStoredColor:function(){
return this.storedColor;
},setStoredColor:function(_e){
this.storedColor=_e;
dojo.event.topic.publish("/"+this.widgetId+"/setStoredColor",this.filterObject);
},hsl2rgb:function(_f,_10,_11){
function rgb(q1,q2,hue){
if(hue>360){
hue=hue-360;
}
if(hue<0){
hue=hue+360;
}
if(hue<60){
return (q1+(q2-q1)*hue/60);
}else{
if(hue<180){
return (q2);
}else{
if(hue<240){
return (q1+(q2-q1)*(240-hue)/60);
}else{
return (q1);
}
}
}
}
this.rgb=rgb;
if(_10==0){
return [Math.round(_11*255/100),Math.round(_11*255/100),Math.round(_11*255/100)];
}else{
_11=_11/100;
_10=_10/100;
if((_11)<0.5){
var _15=(_11)*(1+_10);
}else{
var _15=(_11+_10-(_11*_10));
}
var _16=2*_11-_15;
var _17=[];
_17[0]=Math.round(rgb(_16,_15,parseInt(_f)+120)*255);
_17[1]=Math.round(rgb(_16,_15,_f)*255);
_17[2]=Math.round(rgb(_16,_15,parseInt(_f)-120)*255);
return _17;
}
}});
