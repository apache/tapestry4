dojo.provide("dojo.widget.FisheyeList");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.html.style");
dojo.require("dojo.html.selection");
dojo.require("dojo.html.util");
dojo.require("dojo.event.*");
dojo.widget.defineWidget("dojo.widget.FisheyeList",dojo.widget.HtmlWidget,function(){
this.pos={x:-1,y:-1};
this.EDGE={CENTER:0,LEFT:1,RIGHT:2,TOP:3,BOTTOM:4};
this.timerScale=1;
},{templateString:"<div class=\"dojoHtmlFisheyeListBar\"></div>",templateCssString:".dojoHtmlFisheyeListItemLabel {\n\tfont-family: Arial, Helvetica, sans-serif;\n\tbackground-color: #eee;\n\tborder: 2px solid #666;\n\tpadding: 2px;\n\ttext-align: center;\n\tposition: absolute;\n\tdisplay: none;\n}\n\n.dojoHtmlFisheyeListItemLabel.selected {\n\tdisplay: block;\n}\n\n.dojoHtmlFisheyeListItemImage {\n\tborder: 0px;\n\tposition: absolute;\n}\n\n.dojoHtmlFisheyeListItem {\n\tposition: absolute;\n\tz-index: 2;\n}\n\n.dojoHtmlFisheyeListBar {\n\tposition: relative;\n}\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/FisheyeList.css"),isContainer:true,snarfChildDomOutput:true,itemWidth:40,itemHeight:40,itemMaxWidth:150,itemMaxHeight:150,orientation:"horizontal",conservativeTrigger:false,effectUnits:2,itemPadding:10,attachEdge:"center",labelEdge:"bottom",enableCrappySvgSupport:false,fillInTemplate:function(){
dojo.html.disableSelection(this.domNode);
this.isHorizontal=(this.orientation=="horizontal");
this.selectedNode=-1;
this.isOver=false;
this.hitX1=-1;
this.hitY1=-1;
this.hitX2=-1;
this.hitY2=-1;
this.anchorEdge=this._toEdge(this.attachEdge,this.EDGE.CENTER);
this.labelEdge=this._toEdge(this.labelEdge,this.EDGE.TOP);
if(this.isHorizontal&&(this.anchorEdge==this.EDGE.LEFT)){
this.anchorEdge=this.EDGE.CENTER;
}
if(this.isHorizontal&&(this.anchorEdge==this.EDGE.RIGHT)){
this.anchorEdge=this.EDGE.CENTER;
}
if(!this.isHorizontal&&(this.anchorEdge==this.EDGE.TOP)){
this.anchorEdge=this.EDGE.CENTER;
}
if(!this.isHorizontal&&(this.anchorEdge==this.EDGE.BOTTOM)){
this.anchorEdge=this.EDGE.CENTER;
}
if(this.labelEdge==this.EDGE.CENTER){
this.labelEdge=this.EDGE.TOP;
}
if(this.isHorizontal&&(this.labelEdge==this.EDGE.LEFT)){
this.labelEdge=this.EDGE.TOP;
}
if(this.isHorizontal&&(this.labelEdge==this.EDGE.RIGHT)){
this.labelEdge=this.EDGE.TOP;
}
if(!this.isHorizontal&&(this.labelEdge==this.EDGE.TOP)){
this.labelEdge=this.EDGE.LEFT;
}
if(!this.isHorizontal&&(this.labelEdge==this.EDGE.BOTTOM)){
this.labelEdge=this.EDGE.LEFT;
}
this.proximityLeft=this.itemWidth*(this.effectUnits-0.5);
this.proximityRight=this.itemWidth*(this.effectUnits-0.5);
this.proximityTop=this.itemHeight*(this.effectUnits-0.5);
this.proximityBottom=this.itemHeight*(this.effectUnits-0.5);
if(this.anchorEdge==this.EDGE.LEFT){
this.proximityLeft=0;
}
if(this.anchorEdge==this.EDGE.RIGHT){
this.proximityRight=0;
}
if(this.anchorEdge==this.EDGE.TOP){
this.proximityTop=0;
}
if(this.anchorEdge==this.EDGE.BOTTOM){
this.proximityBottom=0;
}
if(this.anchorEdge==this.EDGE.CENTER){
this.proximityLeft/=2;
this.proximityRight/=2;
this.proximityTop/=2;
this.proximityBottom/=2;
}
},postCreate:function(){
this._initializePositioning();
if(!this.conservativeTrigger){
dojo.event.connect(document.documentElement,"onmousemove",this,"_onMouseMove");
}
dojo.event.connect(document.documentElement,"onmouseout",this,"_onBodyOut");
dojo.event.connect(this,"addChild",this,"_initializePositioning");
},_initializePositioning:function(){
this.itemCount=this.children.length;
this.barWidth=(this.isHorizontal?this.itemCount:1)*this.itemWidth;
this.barHeight=(this.isHorizontal?1:this.itemCount)*this.itemHeight;
this.totalWidth=this.proximityLeft+this.proximityRight+this.barWidth;
this.totalHeight=this.proximityTop+this.proximityBottom+this.barHeight;
for(var i=0;i<this.children.length;i++){
this.children[i].posX=this.itemWidth*(this.isHorizontal?i:0);
this.children[i].posY=this.itemHeight*(this.isHorizontal?0:i);
this.children[i].cenX=this.children[i].posX+(this.itemWidth/2);
this.children[i].cenY=this.children[i].posY+(this.itemHeight/2);
var _2=this.isHorizontal?this.itemWidth:this.itemHeight;
var r=this.effectUnits*_2;
var c=this.isHorizontal?this.children[i].cenX:this.children[i].cenY;
var _5=this.isHorizontal?this.proximityLeft:this.proximityTop;
var _6=this.isHorizontal?this.proximityRight:this.proximityBottom;
var _7=this.isHorizontal?this.barWidth:this.barHeight;
var _8=r;
var _9=r;
if(_8>c+_5){
_8=c+_5;
}
if(_9>(_7-c+_6)){
_9=_7-c+_6;
}
this.children[i].effectRangeLeft=_8/_2;
this.children[i].effectRangeRght=_9/_2;
}
this.domNode.style.width=this.barWidth+"px";
this.domNode.style.height=this.barHeight+"px";
for(var i=0;i<this.children.length;i++){
var _a=this.children[i];
var _b=_a.domNode;
_b.style.left=_a.posX+"px";
_b.style.top=_a.posY+"px";
_b.style.width=this.itemWidth+"px";
_b.style.height=this.itemHeight+"px";
if(_a.svgNode){
_a.svgNode.style.position="absolute";
_a.svgNode.style.left=this.itemPadding+"%";
_a.svgNode.style.top=this.itemPadding+"%";
_a.svgNode.style.width=(100-2*this.itemPadding)+"%";
_a.svgNode.style.height=(100-2*this.itemPadding)+"%";
_a.svgNode.style.zIndex=1;
_a.svgNode.setSize(this.itemWidth,this.itemHeight);
}else{
_a.imgNode.style.left=this.itemPadding+"%";
_a.imgNode.style.top=this.itemPadding+"%";
_a.imgNode.style.width=(100-2*this.itemPadding)+"%";
_a.imgNode.style.height=(100-2*this.itemPadding)+"%";
}
}
this._calcHitGrid();
},_onBodyOut:function(e){
if(dojo.html.overElement(dojo.body(),e)){
return;
}
this._setDormant(e);
},_setDormant:function(e){
if(!this.isOver){
return;
}
this.isOver=false;
if(this.conservativeTrigger){
dojo.event.disconnect(document.documentElement,"onmousemove",this,"_onMouseMove");
}
this._onGridMouseMove(-1,-1);
},_setActive:function(e){
if(this.isOver){
return;
}
this.isOver=true;
if(this.conservativeTrigger){
dojo.event.connect(document.documentElement,"onmousemove",this,"_onMouseMove");
this.timerScale=0;
this._onMouseMove(e);
this._expandSlowly();
}
},_onMouseMove:function(e){
if((e.pageX>=this.hitX1)&&(e.pageX<=this.hitX2)&&(e.pageY>=this.hitY1)&&(e.pageY<=this.hitY2)){
if(!this.isOver){
this._setActive(e);
}
this._onGridMouseMove(e.pageX-this.hitX1,e.pageY-this.hitY1);
}else{
if(this.isOver){
this._setDormant(e);
}
}
},onResized:function(){
this._calcHitGrid();
},_onGridMouseMove:function(x,y){
this.pos={x:x,y:y};
this._paint();
},_paint:function(){
var x=this.pos.x;
var y=this.pos.y;
if(this.itemCount<=0){
return;
}
var pos=this.isHorizontal?x:y;
var prx=this.isHorizontal?this.proximityLeft:this.proximityTop;
var siz=this.isHorizontal?this.itemWidth:this.itemHeight;
var sim=this.isHorizontal?(1-this.timerScale)*this.itemWidth+this.timerScale*this.itemMaxWidth:(1-this.timerScale)*this.itemHeight+this.timerScale*this.itemMaxHeight;
var cen=((pos-prx)/siz)-0.5;
var _19=(sim/siz)-0.5;
if(_19>this.effectUnits){
_19=this.effectUnits;
}
var _1a=0;
if(this.anchorEdge==this.EDGE.BOTTOM){
var _1b=(y-this.proximityTop)/this.itemHeight;
_1a=(_1b>0.5)?1:y/(this.proximityTop+(this.itemHeight/2));
}
if(this.anchorEdge==this.EDGE.TOP){
var _1b=(y-this.proximityTop)/this.itemHeight;
_1a=(_1b<0.5)?1:(this.totalHeight-y)/(this.proximityBottom+(this.itemHeight/2));
}
if(this.anchorEdge==this.EDGE.RIGHT){
var _1b=(x-this.proximityLeft)/this.itemWidth;
_1a=(_1b>0.5)?1:x/(this.proximityLeft+(this.itemWidth/2));
}
if(this.anchorEdge==this.EDGE.LEFT){
var _1b=(x-this.proximityLeft)/this.itemWidth;
_1a=(_1b<0.5)?1:(this.totalWidth-x)/(this.proximityRight+(this.itemWidth/2));
}
if(this.anchorEdge==this.EDGE.CENTER){
if(this.isHorizontal){
_1a=y/(this.totalHeight);
}else{
_1a=x/(this.totalWidth);
}
if(_1a>0.5){
_1a=1-_1a;
}
_1a*=2;
}
for(var i=0;i<this.itemCount;i++){
var _1d=this._weighAt(cen,i);
if(_1d<0){
_1d=0;
}
this._setItemSize(i,_1d*_1a);
}
var _1e=Math.round(cen);
var _1f=0;
if(cen<0){
_1e=0;
}else{
if(cen>this.itemCount-1){
_1e=this.itemCount-1;
}else{
_1f=(cen-_1e)*((this.isHorizontal?this.itemWidth:this.itemHeight)-this.children[_1e].sizeMain);
}
}
this._positionElementsFrom(_1e,_1f);
},_weighAt:function(cen,i){
var _22=Math.abs(cen-i);
var _23=((cen-i)>0)?this.children[i].effectRangeRght:this.children[i].effectRangeLeft;
return (_22>_23)?0:(1-_22/_23);
},_setItemSize:function(p,_25){
_25*=this.timerScale;
var w=Math.round(this.itemWidth+((this.itemMaxWidth-this.itemWidth)*_25));
var h=Math.round(this.itemHeight+((this.itemMaxHeight-this.itemHeight)*_25));
if(this.isHorizontal){
this.children[p].sizeW=w;
this.children[p].sizeH=h;
this.children[p].sizeMain=w;
this.children[p].sizeOff=h;
var y=0;
if(this.anchorEdge==this.EDGE.TOP){
y=(this.children[p].cenY-(this.itemHeight/2));
}else{
if(this.anchorEdge==this.EDGE.BOTTOM){
y=(this.children[p].cenY-(h-(this.itemHeight/2)));
}else{
y=(this.children[p].cenY-(h/2));
}
}
this.children[p].usualX=Math.round(this.children[p].cenX-(w/2));
this.children[p].domNode.style.top=y+"px";
this.children[p].domNode.style.left=this.children[p].usualX+"px";
}else{
this.children[p].sizeW=w;
this.children[p].sizeH=h;
this.children[p].sizeOff=w;
this.children[p].sizeMain=h;
var x=0;
if(this.anchorEdge==this.EDGE.LEFT){
x=this.children[p].cenX-(this.itemWidth/2);
}else{
if(this.anchorEdge==this.EDGE.RIGHT){
x=this.children[p].cenX-(w-(this.itemWidth/2));
}else{
x=this.children[p].cenX-(w/2);
}
}
this.children[p].domNode.style.left=x+"px";
this.children[p].usualY=Math.round(this.children[p].cenY-(h/2));
this.children[p].domNode.style.top=this.children[p].usualY+"px";
}
this.children[p].domNode.style.width=w+"px";
this.children[p].domNode.style.height=h+"px";
if(this.children[p].svgNode){
this.children[p].svgNode.setSize(w,h);
}
},_positionElementsFrom:function(p,_2b){
var pos=0;
if(this.isHorizontal){
pos=Math.round(this.children[p].usualX+_2b);
this.children[p].domNode.style.left=pos+"px";
}else{
pos=Math.round(this.children[p].usualY+_2b);
this.children[p].domNode.style.top=pos+"px";
}
this._positionLabel(this.children[p]);
var _2d=pos;
for(var i=p-1;i>=0;i--){
_2d-=this.children[i].sizeMain;
if(this.isHorizontal){
this.children[i].domNode.style.left=_2d+"px";
}else{
this.children[i].domNode.style.top=_2d+"px";
}
this._positionLabel(this.children[i]);
}
var _2f=pos;
for(var i=p+1;i<this.itemCount;i++){
_2f+=this.children[i-1].sizeMain;
if(this.isHorizontal){
this.children[i].domNode.style.left=_2f+"px";
}else{
this.children[i].domNode.style.top=_2f+"px";
}
this._positionLabel(this.children[i]);
}
},_positionLabel:function(itm){
var x=0;
var y=0;
var mb=dojo.html.getMarginBox(itm.lblNode);
if(this.labelEdge==this.EDGE.TOP){
x=Math.round((itm.sizeW/2)-(mb.width/2));
y=-mb.height;
}
if(this.labelEdge==this.EDGE.BOTTOM){
x=Math.round((itm.sizeW/2)-(mb.width/2));
y=itm.sizeH;
}
if(this.labelEdge==this.EDGE.LEFT){
x=-mb.width;
y=Math.round((itm.sizeH/2)-(mb.height/2));
}
if(this.labelEdge==this.EDGE.RIGHT){
x=itm.sizeW;
y=Math.round((itm.sizeH/2)-(mb.height/2));
}
itm.lblNode.style.left=x+"px";
itm.lblNode.style.top=y+"px";
},_calcHitGrid:function(){
var pos=dojo.html.getAbsolutePosition(this.domNode,true);
this.hitX1=pos.x-this.proximityLeft;
this.hitY1=pos.y-this.proximityTop;
this.hitX2=this.hitX1+this.totalWidth;
this.hitY2=this.hitY1+this.totalHeight;
},_toEdge:function(inp,def){
return this.EDGE[inp.toUpperCase()]||def;
},_expandSlowly:function(){
if(!this.isOver){
return;
}
this.timerScale+=0.2;
this._paint();
if(this.timerScale<1){
dojo.lang.setTimeout(this,"_expandSlowly",10);
}
},destroy:function(){
dojo.event.disconnect(document.documentElement,"onmouseout",this,"_onBodyOut");
dojo.event.disconnect(document.documentElement,"onmousemove",this,"_onMouseMove");
dojo.widget.FisheyeList.superclass.destroy.call(this);
}});
dojo.widget.defineWidget("dojo.widget.FisheyeListItem",dojo.widget.HtmlWidget,{iconSrc:"",svgSrc:"",caption:"",id:"",_blankImgPath:dojo.uri.moduleUri("dojo.widget","templates/images/blank.gif"),templateString:"<div class=\"dojoHtmlFisheyeListItem\">"+"  <img class=\"dojoHtmlFisheyeListItemImage\" dojoAttachPoint=\"imgNode\" dojoAttachEvent=\"onMouseOver;onMouseOut;onClick\">"+"  <div class=\"dojoHtmlFisheyeListItemLabel\" dojoAttachPoint=\"lblNode\"></div>"+"</div>",fillInTemplate:function(){
if(this.svgSrc!=""){
this.svgNode=this._createSvgNode(this.svgSrc);
this.domNode.appendChild(this.svgNode);
this.imgNode.style.display="none";
}else{
if((this.iconSrc.toLowerCase().substring(this.iconSrc.length-4)==".png")&&(dojo.render.html.ie)&&(!dojo.render.html.ie70)){
if(dojo.dom.hasParent(this.imgNode)&&this.id!=""){
var _37=this.imgNode.parentNode;
_37.setAttribute("id",this.id);
}
this.imgNode.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+this.iconSrc+"', sizingMethod='scale')";
this.imgNode.src=this._blankImgPath.toString();
}else{
if(dojo.dom.hasParent(this.imgNode)&&this.id!=""){
var _37=this.imgNode.parentNode;
_37.setAttribute("id",this.id);
}
this.imgNode.src=this.iconSrc;
}
}
if(this.lblNode){
this.lblNode.appendChild(document.createTextNode(this.caption));
}
dojo.html.disableSelection(this.domNode);
},_createSvgNode:function(src){
var elm=document.createElement("embed");
elm.src=src;
elm.type="image/svg+xml";
elm.style.width="1px";
elm.style.height="1px";
elm.loaded=0;
elm.setSizeOnLoad=false;
elm.onload=function(){
this.svgRoot=this.getSVGDocument().rootElement;
this.svgDoc=this.getSVGDocument().documentElement;
this.zeroWidth=this.svgRoot.width.baseVal.value;
this.zeroHeight=this.svgRoot.height.baseVal.value;
this.loaded=true;
if(this.setSizeOnLoad){
this.setSize(this.setWidth,this.setHeight);
}
};
elm.setSize=function(w,h){
if(!this.loaded){
this.setWidth=w;
this.setHeight=h;
this.setSizeOnLoad=true;
return;
}
this.style.width=w+"px";
this.style.height=h+"px";
this.svgRoot.width.baseVal.value=w;
this.svgRoot.height.baseVal.value=h;
var _3c=w/this.zeroWidth;
var _3d=h/this.zeroHeight;
for(var i=0;i<this.svgDoc.childNodes.length;i++){
if(this.svgDoc.childNodes[i].setAttribute){
this.svgDoc.childNodes[i].setAttribute("transform","scale("+_3c+","+_3d+")");
}
}
};
return elm;
},onMouseOver:function(e){
if(!this.parent.isOver){
this.parent._setActive(e);
}
if(this.caption!=""){
dojo.html.addClass(this.lblNode,"selected");
this.parent._positionLabel(this);
}
},onMouseOut:function(e){
dojo.html.removeClass(this.lblNode,"selected");
},onClick:function(e){
}});
