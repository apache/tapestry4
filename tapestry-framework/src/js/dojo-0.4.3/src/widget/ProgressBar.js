dojo.provide("dojo.widget.ProgressBar");
dojo.require("dojo.widget.*");
dojo.require("dojo.event");
dojo.require("dojo.dom");
dojo.require("dojo.html.style");
dojo.require("dojo.string.*");
dojo.require("dojo.lfx.*");
dojo.widget.defineWidget("dojo.widget.ProgressBar",dojo.widget.HtmlWidget,{progressValue:0,maxProgressValue:100,width:300,height:30,frontPercentClass:"frontPercent",backPercentClass:"backPercent",frontBarClass:"frontBar",backBarClass:"backBar",hasText:false,isVertical:false,showOnlyIntegers:false,dataSource:"",pollInterval:3000,duration:1000,templateString:"<div dojoAttachPoint=\"containerNode\" style=\"position:relative;overflow:hidden\">\n\t<div style=\"position:absolute;display:none;width:100%;text-align:center\" dojoAttachPoint=\"backPercentLabel\" class=\"dojoBackPercentLabel\"></div>\n\t<div style=\"position:absolute;overflow:hidden;width:100%;height:100%\" dojoAttachPoint=\"internalProgress\">\n\t<div style=\"position:absolute;display:none;width:100%;text-align:center\" dojoAttachPoint=\"frontPercentLabel\" class=\"dojoFrontPercentLabel\"></div></div>\n</div>\n",templateCssString:".backBar{\n\tborder:1px solid #84a3d1;\n}\n.frontBar{\n\tbackground:url(\"images/bar.gif\") repeat bottom left;\n\tbackground-attachment: fixed;\n}\n.h-frontBar{\n\tbackground:url(\"images/h-bar.gif\") repeat bottom left;\n\tbackground-attachment: fixed;\n}\n.simpleFrontBar{\n\tbackground: red;\n}\n.frontPercent,.backPercent{\n\tfont:bold 13px helvetica;\n}\n.backPercent{\n\tcolor:#293a4b;\n}\n.frontPercent{\n\tcolor:#fff;\n}\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/ProgressBar.css"),containerNode:null,internalProgress:null,_pixelUnitRatio:0,_pixelPercentRatio:0,_unitPercentRatio:0,_unitPixelRatio:0,_floatDimension:0,_intDimension:0,_progressPercentValue:"0%",_floatMaxProgressValue:0,_dimension:"width",_pixelValue:0,_oInterval:null,_animation:null,_animationStopped:true,_progressValueBak:false,_hasTextBak:false,fillInTemplate:function(_1,_2){
this.internalProgress.className=this.frontBarClass;
this.containerNode.className=this.backBarClass;
if(this.isVertical){
this.internalProgress.style.bottom="0px";
this.internalProgress.style.left="0px";
this._dimension="height";
}else{
this.internalProgress.style.top="0px";
this.internalProgress.style.left="0px";
this._dimension="width";
}
this.frontPercentLabel.className=this.frontPercentClass;
this.backPercentLabel.className=this.backPercentClass;
this.progressValue=""+this.progressValue;
this.domNode.style.height=this.height+"px";
this.domNode.style.width=this.width+"px";
this._intDimension=parseInt("0"+eval("this."+this._dimension));
this._floatDimension=parseFloat("0"+eval("this."+this._dimension));
this._pixelPercentRatio=this._floatDimension/100;
this.setMaxProgressValue(this.maxProgressValue,true);
this.setProgressValue(dojo.string.trim(this.progressValue),true);
dojo.debug("float dimension: "+this._floatDimension);
dojo.debug("this._unitPixelRatio: "+this._unitPixelRatio);
this.showText(this.hasText);
},showText:function(_3){
if(_3){
this.backPercentLabel.style.display="block";
this.frontPercentLabel.style.display="block";
}else{
this.backPercentLabel.style.display="none";
this.frontPercentLabel.style.display="none";
}
this.hasText=_3;
},postCreate:function(_4,_5){
this.render();
},_backupValues:function(){
this._progressValueBak=this.progressValue;
this._hasTextBak=this.hasText;
},_restoreValues:function(){
this.setProgressValue(this._progressValueBak);
this.showText(this._hasTextBak);
},_setupAnimation:function(){
var _6=this;
dojo.debug("internalProgress width: "+this.internalProgress.style.width);
this._animation=dojo.lfx.html.slideTo(this.internalProgress,{top:0,left:parseInt(this.width)-parseInt(this.internalProgress.style.width)},parseInt(this.duration),null,function(){
var _7=dojo.lfx.html.slideTo(_6.internalProgress,{top:0,left:0},parseInt(_6.duration));
dojo.event.connect(_7,"onEnd",function(){
if(!_6._animationStopped){
_6._animation.play();
}
});
if(!_6._animationStopped){
_7.play();
}
_7=null;
});
},getMaxProgressValue:function(){
return this.maxProgressValue;
},setMaxProgressValue:function(_8,_9){
if(!this._animationStopped){
return;
}
this.maxProgressValue=_8;
this._floatMaxProgressValue=parseFloat("0"+this.maxProgressValue);
this._pixelUnitRatio=this._floatDimension/this.maxProgressValue;
this._unitPercentRatio=this._floatMaxProgressValue/100;
this._unitPixelRatio=this._floatMaxProgressValue/this._floatDimension;
this.setProgressValue(this.progressValue,true);
if(!_9){
this.render();
}
},setProgressValue:function(_a,_b){
if(!this._animationStopped){
return;
}
this._progressPercentValue="0%";
var _c=dojo.string.trim(""+_a);
var _d=parseFloat("0"+_c);
var _e=parseInt("0"+_c);
var _f=0;
if(dojo.string.endsWith(_c,"%",false)){
this._progressPercentValue=Math.min(_d.toFixed(1),100)+"%";
_c=Math.min((_d)*this._unitPercentRatio,this.maxProgressValue);
_f=Math.min((_d)*this._pixelPercentRatio,eval("this."+this._dimension));
}else{
this.progressValue=Math.min(_d,this.maxProgressValue);
this._progressPercentValue=Math.min((_d/this._unitPercentRatio).toFixed(1),100)+"%";
_f=Math.min(_d/this._unitPixelRatio,eval("this."+this._dimension));
}
this.progressValue=dojo.string.trim(_c);
this._pixelValue=_f;
if(!_b){
this.render();
}
},getProgressValue:function(){
return this.progressValue;
},getProgressPercentValue:function(){
return this._progressPercentValue;
},setDataSource:function(_10){
this.dataSource=_10;
},setPollInterval:function(_11){
this.pollInterval=_11;
},start:function(){
var _12=dojo.lang.hitch(this,this._showRemoteProgress);
this._oInterval=setInterval(_12,this.pollInterval);
},startAnimation:function(){
if(this._animationStopped){
this._backupValues();
this.setProgressValue("10%");
this._animationStopped=false;
this._setupAnimation();
this.showText(false);
this.internalProgress.style.height="105%";
this._animation.play();
}
},stopAnimation:function(){
if(this._animation){
this._animationStopped=true;
this._animation.stop();
this.internalProgress.style.height="100%";
this.internalProgress.style.left="0px";
this._restoreValues();
this._setLabelPosition();
}
},_showRemoteProgress:function(){
var _13=this;
if((this.getMaxProgressValue()==this.getProgressValue())&&this._oInterval){
clearInterval(this._oInterval);
this._oInterval=null;
this.setProgressValue("100%");
return;
}
var _14={url:_13.dataSource,method:"POST",mimetype:"text/json",error:function(_15,_16){
dojo.debug("[ProgressBar] showRemoteProgress error");
},load:function(_17,_18,evt){
_13.setProgressValue((_13._oInterval?_18["progress"]:"100%"));
}};
dojo.io.bind(_14);
},render:function(){
this._setPercentLabel(dojo.string.trim(this._progressPercentValue));
this._setPixelValue(this._pixelValue);
this._setLabelPosition();
},_setLabelPosition:function(){
var _1a=dojo.html.getContentBox(this.frontPercentLabel).width;
var _1b=dojo.html.getContentBox(this.frontPercentLabel).height;
var _1c=dojo.html.getContentBox(this.backPercentLabel).width;
var _1d=dojo.html.getContentBox(this.backPercentLabel).height;
var _1e=(parseInt(this.width)-_1a)/2+"px";
var _1f=(parseInt(this.height)-parseInt(_1b))/2+"px";
var _20=(parseInt(this.width)-_1c)/2+"px";
var _21=(parseInt(this.height)-parseInt(_1d))/2+"px";
this.frontPercentLabel.style.left=_1e;
this.backPercentLabel.style.left=_20;
this.frontPercentLabel.style.bottom=_1f;
this.backPercentLabel.style.bottom=_21;
},_setPercentLabel:function(_22){
dojo.dom.removeChildren(this.frontPercentLabel);
dojo.dom.removeChildren(this.backPercentLabel);
var _23=this.showOnlyIntegers==false?_22:parseInt(_22)+"%";
this.frontPercentLabel.appendChild(document.createTextNode(_23));
this.backPercentLabel.appendChild(document.createTextNode(_23));
},_setPixelValue:function(_24){
eval("this.internalProgress.style."+this._dimension+" = "+_24+" + 'px'");
this.onChange();
},onChange:function(){
}});
