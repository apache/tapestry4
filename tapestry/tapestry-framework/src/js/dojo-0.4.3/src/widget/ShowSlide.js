dojo.provide("dojo.widget.ShowSlide");
dojo.require("dojo.widget.*");
dojo.require("dojo.lang.common");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.lfx.html");
dojo.require("dojo.html.display");
dojo.require("dojo.html.layout");
dojo.require("dojo.animation.Animation");
dojo.require("dojo.gfx.color");
dojo.widget.defineWidget("dojo.widget.ShowSlide",dojo.widget.HtmlWidget,{title:"",_action:-1,isContainer:true,_components:{},_actions:[],gotoAction:function(_1){
this._action=_1;
},_nextAction:function(_2){
if((this._action+1)!=this._actions.length){
++this._action;
return true;
}
return false;
},_previousAction:function(_3){
if((this._action-1)!=-1){
--this._action;
return true;
}
return false;
},htmlTitle:null,debug:false,noClick:false,templateString:"<div class=\"dojoShowSlide\">\n\t<div class=\"dojoShowSlideTitle\">\n\t\t<h1 dojoAttachPoint=\"htmlTitle\">Title</h1>\n\t</div>\n\t<div class=\"dojoShowSlideBody\" dojoAttachPoint=\"containerNode\"></div>\n</div>\n",templateCssString:".dojoShowSlideTitle {\n\theight: 100px;\n\tbackground: #369;\n}\n.dojoShowSlideTitle h1 {\n\tmargin-top: 0;\n\tline-height: 100px;\n\tmargin-left: 30px;\n}\n.dojoShowSlideBody {\n\tmargin: 15px;\n}\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/ShowSlide.css"),postCreate:function(){
this.htmlTitle.innerHTML=this.title;
var _4=this.getChildrenOfType("ShowAction",false);
var _5={};
dojo.lang.forEach(_4,function(_6){
_5[_6.on]=true;
});
this._components={};
var cn=this.containerNode;
var _8=dojo.render.html.ie?cn.all:cn.getElementsByTagName("*");
dojo.lang.forEach(_8,function(_9){
var as=_9.getAttribute("as");
if(as){
if(!this._components[as]){
this._components[as]=[];
}
this._components[as].push(_9);
if(!_5[as]){
var _b=dojo.widget.createWidget("ShowAction",{on:as});
this.addChild(_b);
_5[as]=true;
}
}
},this);
this._actions=[];
_4=this.getChildrenOfType("ShowAction",false);
dojo.lang.forEach(_4,function(_c){
this._actions.push(_c);
var _d=this._components[_c.on];
for(var j=0,_f;_f=_d[j];j++){
if(_c["action"]&&((_c.action!="remove")&&(_c.action!="fadeout")&&(_c.action!="wipeout"))){
this.hideComponent(_f);
}
}
},this);
},previousAction:function(_10){
if(!this.parent.stopEvent(_10)){
return false;
}
var _11=this._actions[this._action];
if(!_11){
return false;
}
var on=_11.on;
while(_11.on==on){
var _13=this._components[on];
for(var i=0,_15;_15=_13[i];i++){
if((_11.action=="remove")||(_11.action=="fadeout")||(_11.action=="wipeout")){
if(_15.style.display=="none"){
_15.style.display="";
_15.style.visibility="visible";
var _16=true;
}
dojo.html.setOpacity(_15,1);
}else{
if(_11.action){
this.hideComponent(_15);
}
}
}
--this._action;
if(_16){
return true;
}
if(_11.auto=="true"){
on=this._actions[this._action].on;
}
_11=this._actions[this._action];
if(!_11){
return false;
}
}
return true;
},hideComponent:function(_17){
_17.style.visibility="hidden";
_17.style.backgroundColor="transparent";
var _18=_17.parentNode;
if((_18)&&(_18.tagName.toLowerCase()=="li")){
_18.oldType=_18.style.listStyleType;
_18.style.listStyleType="none";
}
},nextAction:function(_19){
if(!this.parent.stopEvent(_19)){
return false;
}
if(!this._nextAction(this)){
return false;
}
var _1a=this._actions[this._action];
if(!_1a){
return false;
}
var _1b=_1a["action"];
var _1c=this._components[_1a.on];
for(var i=0,_1e;_1e=_1c[i];i++){
if(_1b){
var _1f=_1a.duration||1000;
if((_1b=="fade")||(_1b=="fadeIn")){
dojo.html.setOpacity(_1e,0);
dojo.lfx.html.fadeShow(_1e,_1f).play(true);
}else{
if(_1b=="fadeout"){
dojo.lfx.html.fadeHide(_1e,_1f).play(true);
}else{
if(_1b=="fly"){
var _20=dojo.html.getMarginBox(_1e).width;
var _21=dojo.html.getAbsolutePosition(_1e);
_1e.style.position="relative";
_1e.style.left=-(_20+_21.x)+"px";
dojo.lfx.html.slideBy(_1e,{top:0,left:(_20+_21.x)},_1f,-1,this.callWith).play(true);
}else{
if((_1b=="wipe")||(_1b=="wipein")){
dojo.lfx.html.wipeIn(_1e,_1f).play();
}else{
if(_1b=="wipeout"){
dojo.lfx.html.wipeOut(_1e,_1f).play();
}else{
if(_1b=="color"){
var _22=new dojo.gfx.color.Color(_1a.from).toRgb();
var to=new dojo.gfx.color.Color(_1a.to).toRgb();
var _24=new dojo.animation.Animation(new dojo.math.curves.Line(_22,to),_1f,0);
var _25=_1e;
dojo.event.connect(_24,"onAnimate",function(e){
_25.style.color="rgb("+e.coordsAsInts().join(",")+")";
});
_24.play(true);
}else{
if(_1b=="bgcolor"){
dojo.lfx.html.unhighlight(_1e,_1a.to,_1f).play();
}else{
if(_1b=="remove"){
_1e.style.display="none";
}
}
}
}
}
}
}
}
if(_1b=="hide"){
_1e.style.visibility="hidden";
}else{
_1e.style.visibility="visible";
}
}
}
_1a=this._actions[this._action+1];
if(_1a&&_1a.auto=="true"){
this.nextAction();
}
return true;
},callWith:function(_27){
if(!_27){
return;
}
if(dojo.lang.isArray(_27)){
dojo.lang.forEach(_27,arguments.callee);
return;
}
var _28=_27.parentNode;
if((_28)&&(_28.tagName.toLowerCase()=="li")){
_28.style.listStyleType=_28.oldType;
}
}});
