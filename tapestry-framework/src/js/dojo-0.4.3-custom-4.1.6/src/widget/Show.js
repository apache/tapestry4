dojo.provide("dojo.widget.Show");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.uri.Uri");
dojo.require("dojo.event.*");
dojo.require("dojo.lfx.*");
dojo.require("dojo.math.curves");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.func");
dojo.widget.defineWidget("dojo.widget.Show",dojo.widget.HtmlWidget,function(){
this._slides=[];
},{isContainer:true,_slide:-1,body:null,nav:null,hider:null,select:null,option:null,inNav:false,debugPane:null,noClick:false,templateString:"<div class=\"dojoShow\">\n\t<div dojoAttachPoint=\"contentNode\"></div>\n\t<div class=\"dojoShowNav\" dojoAttachPoint=\"nav\">\n\t\t<div class=\"dojoShowHider\" dojoAttachPoint=\"hider\"></div>\n\t\t<span unselectable=\"on\" style=\"cursor: default;\" dojoAttachEvent=\"onClick:previousSlide\">&lt;</span>\n\t\t<select dojoAttachEvent=\"onClick:gotoSlideByEvent\" dojoAttachPoint=\"select\">\n\t\t\t<option dojoAttachPoint=\"option\">Title</option>\n\t\t</select>\n\t\t<span unselectable=\"on\" style=\"cursor: default;\" dojoAttachEvent=\"onClick:nextSlide\">&gt;</span>\n\t</div>\n</div>\n",templateCssString:"@media screen {\n\thtml, body {\n\t\tmargin: 0px;\n\t\tpadding: 0px;\n\t\twidth: 100%;\n\t}\n\th1 {\n\t\tfont-size: 50px;\n\t}\n\tp, li {\n\t\tfont-size: 30px;\n\t}\n\t.dojoShowNav {\n\t\tbackground: #369;\n\t\toverflow: hidden;\n\t\tposition: absolute;\n\t\theight: 5px;\n\t\tbottom: 0px;\n\t\tleft: 0px;\n\t\twidth: 100%;\n\t\ttext-align: center;\n\t}\n\t.dojoShowNav input {\n\t\tmargin: 0px;\n\t}\n\t.dojoShowHider {\n\t\theight: 5px;\n\t\toverflow: hidden;\n\t\twidth: 100%;\n\t}\n\t.dojoShowPrint {\n\t\tposition: absolute;\n\t\tleft: 5px;\n\t\ttop: 0px;\n\t}\n\t.dojoShow {\n\t\tdisplay: none;\n\t}\n}\n@media print {\n\t.dojoShow {\n\t\tdisplay: none !important;\n\t}\n\t.dojoShowPrint {\n\t\tdisplay: block !important;\n\t}\n\t.dojoShowPrintSlide {\n\t\tborder: 1px solid #aaa;\n\t\tpadding: 10px;\n\t\tmargin-bottom: 15px;\n\t}\n\t.dojoShowPrintSlide, ul {\n\tpage-break-inside: avoid;\n\t}\n\th1 {\n\t\tmargin-top: 0;\n\t\tpage-break-after: avoid;\n\t}\n}\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/Show.css"),fillInTemplate:function(_1,_2){
if(_1.debugPane){
var dp=this.debugPane=dojo.widget.byId(_1.debugPane);
dp.hide();
dojo.event.connect(dp,"closeWindow",dojo.lang.hitch(this,function(){
this.debugPane=false;
}));
}
var _4=this.getFragNodeRef(_2);
this.sourceNode=dojo.body().appendChild(_4.cloneNode(true));
for(var i=0,_6;_6=this.sourceNode.childNodes[i];i++){
if(_6.tagName&&_6.getAttribute("dojotype").toLowerCase()=="showslide"){
_6.className="dojoShowPrintSlide";
_6.innerHTML="<h1>"+_6.title+"</h1>"+_6.innerHTML;
}
}
this.sourceNode.className="dojoShowPrint";
this.sourceNode.style.display="none";
dojo.event.connect(document,"onclick",this,"gotoSlideByEvent");
if(dojo.render.html.ie){
dojo.event.connect(document,"onkeydown",this,"gotoSlideByEvent");
}else{
dojo.event.connect(document,"onkeypress",this,"gotoSlideByEvent");
}
dojo.event.connect(window,"onresize",this,"resizeWindow");
dojo.event.connect(this.nav,"onmousemove",this,"popUpNav");
},postCreate:function(){
this._slides=[];
for(var i=0,_8;_8=this.children[i];i++){
if(_8.widgetType=="ShowSlide"){
this._slides.push(_8);
this.option.text=_8.title+" ("+(i+1)+")";
this.option.parentNode.insertBefore(this.option.cloneNode(true),this.option);
}
}
this.option.parentNode.removeChild(this.option);
this.domNode.style.display="block";
this.resizeWindow();
this.gotoSlide(0,true);
dojo.addOnLoad(dojo.lang.hitch(this,function(){
var th=window.location.hash;
if(th.length){
var _a=(""+window.location).split(this.widgetId+"_SlideNo_");
if(_a.length>1){
setTimeout(dojo.lang.hitch(this,function(){
this.gotoSlide(parseInt(_a[1]),true);
}),300);
}
}
}));
},gotoSlide:function(_b,_c){
if(_b==this._slide){
return;
}
if(!this._slides[_b]){
for(var i=0,_e;_e=this._slides[i];i++){
if(_e.title==_b){
_b=i;
break;
}
}
}
if(!this._slides[_b]){
return;
}
if(this.debugPane){
if(this._slides[_b].debug){
this.debugPane.show();
}else{
this.debugPane.hide();
}
}
if(this._slide!=-1){
while(this._slides[this._slide].previousAction()){
}
}
if(!_c){
window.location.href="#"+this.widgetId+"_SlideNo_"+_b;
}
if(this._slides[this._slide]){
this._slides[this._slide].hide();
}
this._slide=_b;
this.select.selectedIndex=_b;
var cn=this.contentNode;
while(cn.firstChild){
cn.removeChild(cn.firstChild);
}
cn.appendChild(this._slides[_b].domNode);
this._slides[_b].show();
},gotoSlideByEvent:function(_10){
var _11=_10.target;
var _12=_10.type;
if(_12=="click"){
if(_11.tagName=="OPTION"&&_11.parentNode==this.select){
this.gotoSlide(_11.index);
}else{
if(_11==this.select){
this.gotoSlide(_11.selectedIndex);
}else{
this.nextSlide(_10);
}
}
}else{
if(_12=="keydown"||_12=="keypress"){
var key=_10.keyCode;
var ch=_10.charCode;
if(key==63234||key==37){
this.previousSlide(_10);
}else{
if(key==63235||key==39||ch==32){
this.nextSlide(_10);
}
}
}
}
},nextSlide:function(_15){
if(!this.stopEvent(_15)){
return false;
}
if(!this._slides[this._slide].nextAction(_15)){
if((this._slide+1)!=this._slides.length){
this.gotoSlide(this._slide+1);
return true;
}
return false;
}
},previousSlide:function(_16){
if(!this.stopEvent(_16)){
return false;
}
if(!this._slides[this._slide].previousAction(_16)){
if((this._slide-1)!=-1){
this.gotoSlide(this._slide-1);
return true;
}
return false;
}
},stopEvent:function(ev){
if(!ev){
return true;
}
if(ev.type=="click"&&(this._slides[this._slide].noClick||this.noClick)){
return false;
}
var _18=ev.target;
while(_18!=null){
if(_18==this.domNode){
_18=ev.target;
break;
}
_18=_18.parentNode;
}
if(!dojo.dom.isDescendantOf(_18,this.nav)){
while(_18&&_18!=this.domNode){
if(_18.tagName=="A"||_18.tagName=="INPUT"||_18.tagName=="TEXTAREA"||_18.tagName=="SELECT"){
return false;
}
if(typeof _18.onclick=="function"||typeof _18.onkeypress=="function"){
return false;
}
_18=_18.parentNode;
}
}
if(window.event){
ev.returnValue=false;
ev.cancelBubble=true;
}else{
ev.preventDefault();
ev.stopPropagation();
}
return true;
},popUpNav:function(){
if(!this.inNav){
dojo.lfx.propertyAnimation(this.nav,{"height":{start:5,end:30}},250).play();
}
clearTimeout(this.inNav);
this.inNav=setTimeout(dojo.lang.hitch(this,"hideNav"),2000);
},hideNav:function(){
clearTimeout(this.inNav);
this.inNav=false;
dojo.lfx.propertyAnimation(this.nav,{"height":{start:30,end:5}},250).play();
},resizeWindow:function(ev){
dojo.body().style.height="auto";
var h=Math.max(document.documentElement.scrollHeight||dojo.body().scrollHeight,dojo.html.getViewport().height);
dojo.body().style.height=h+"px";
}});
