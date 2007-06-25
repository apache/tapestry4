dojo.provide("dojo.dnd.HtmlDragAndDrop");
dojo.require("dojo.dnd.HtmlDragManager");
dojo.require("dojo.dnd.DragAndDrop");
dojo.require("dojo.html.*");
dojo.require("dojo.html.display");
dojo.require("dojo.html.util");
dojo.require("dojo.html.selection");
dojo.require("dojo.html.iframe");
dojo.require("dojo.lang.extras");
dojo.require("dojo.lfx.*");
dojo.require("dojo.event.*");
dojo.declare("dojo.dnd.HtmlDragSource",dojo.dnd.DragSource,{dragClass:"",onDragStart:function(){
var _1=new dojo.dnd.HtmlDragObject(this.dragObject,this.type);
if(this.dragClass){
_1.dragClass=this.dragClass;
}
if(this.constrainToContainer){
_1.constrainTo(this.constrainingContainer||this.domNode.parentNode);
}
return _1;
},setDragHandle:function(_2){
_2=dojo.byId(_2);
dojo.dnd.dragManager.unregisterDragSource(this);
this.domNode=_2;
dojo.dnd.dragManager.registerDragSource(this);
},setDragTarget:function(_3){
this.dragObject=_3;
},constrainTo:function(_4){
this.constrainToContainer=true;
if(_4){
this.constrainingContainer=_4;
}
},onSelected:function(){
for(var i=0;i<this.dragObjects.length;i++){
dojo.dnd.dragManager.selectedSources.push(new dojo.dnd.HtmlDragSource(this.dragObjects[i]));
}
},addDragObjects:function(el){
for(var i=0;i<arguments.length;i++){
this.dragObjects.push(dojo.byId(arguments[i]));
}
}},function(_8,_9){
_8=dojo.byId(_8);
this.dragObjects=[];
this.constrainToContainer=false;
if(_8){
this.domNode=_8;
this.dragObject=_8;
this.type=(_9)||(this.domNode.nodeName.toLowerCase());
dojo.dnd.DragSource.prototype.reregister.call(this);
}
});
dojo.declare("dojo.dnd.HtmlDragObject",dojo.dnd.DragObject,{dragClass:"",opacity:0.5,createIframe:true,disableX:false,disableY:false,createDragNode:function(){
var _a=this.domNode.cloneNode(true);
if(this.dragClass){
dojo.html.addClass(_a,this.dragClass);
}
if(this.opacity<1){
dojo.html.setOpacity(_a,this.opacity);
}
var _b=_a.tagName.toLowerCase();
var _c=(_b=="tr");
if((_c)||(_b=="tbody")){
var _d=this.domNode.ownerDocument;
var _e=_d.createElement("table");
if(_c){
var _f=_d.createElement("tbody");
_e.appendChild(_f);
_f.appendChild(_a);
}else{
_e.appendChild(_a);
}
var _10=((_c)?this.domNode:this.domNode.firstChild);
var _11=((_c)?_a:_a.firstChild);
var _12=_10.childNodes;
var _13=_11.childNodes;
for(var i=0;i<_12.length;i++){
if((_13[i])&&(_13[i].style)){
_13[i].style.width=dojo.html.getContentBox(_12[i]).width+"px";
}
}
_a=_e;
}
if((dojo.render.html.ie55||dojo.render.html.ie60)&&this.createIframe){
with(_a.style){
top="0px";
left="0px";
}
var _15=document.createElement("div");
_15.appendChild(_a);
this.bgIframe=new dojo.html.BackgroundIframe(_15);
_15.appendChild(this.bgIframe.iframe);
_a=_15;
}
_a.style.zIndex=999;
return _a;
},onDragStart:function(e){
dojo.html.clearSelection();
this.scrollOffset=dojo.html.getScroll().offset;
this.dragStartPosition=dojo.html.getAbsolutePosition(this.domNode,true);
this.dragOffset={y:this.dragStartPosition.y-e.pageY,x:this.dragStartPosition.x-e.pageX};
this.dragClone=this.createDragNode();
this.containingBlockPosition=this.domNode.offsetParent?dojo.html.getAbsolutePosition(this.domNode.offsetParent,true):{x:0,y:0};
if(this.constrainToContainer){
this.constraints=this.getConstraints();
}
with(this.dragClone.style){
position="absolute";
top=this.dragOffset.y+e.pageY+"px";
left=this.dragOffset.x+e.pageX+"px";
}
dojo.body().appendChild(this.dragClone);
dojo.event.topic.publish("dragStart",{source:this});
},getConstraints:function(){
if(this.constrainingContainer.nodeName.toLowerCase()=="body"){
var _17=dojo.html.getViewport();
var _18=_17.width;
var _19=_17.height;
var _1a=dojo.html.getScroll().offset;
var x=_1a.x;
var y=_1a.y;
}else{
var _1d=dojo.html.getContentBox(this.constrainingContainer);
_18=_1d.width;
_19=_1d.height;
x=this.containingBlockPosition.x+dojo.html.getPixelValue(this.constrainingContainer,"padding-left",true)+dojo.html.getBorderExtent(this.constrainingContainer,"left");
y=this.containingBlockPosition.y+dojo.html.getPixelValue(this.constrainingContainer,"padding-top",true)+dojo.html.getBorderExtent(this.constrainingContainer,"top");
}
var mb=dojo.html.getMarginBox(this.domNode);
return {minX:x,minY:y,maxX:x+_18-mb.width,maxY:y+_19-mb.height};
},updateDragOffset:function(){
var _1f=dojo.html.getScroll().offset;
if(_1f.y!=this.scrollOffset.y){
var _20=_1f.y-this.scrollOffset.y;
this.dragOffset.y+=_20;
this.scrollOffset.y=_1f.y;
}
if(_1f.x!=this.scrollOffset.x){
var _20=_1f.x-this.scrollOffset.x;
this.dragOffset.x+=_20;
this.scrollOffset.x=_1f.x;
}
},onDragMove:function(e){
this.updateDragOffset();
var x=this.dragOffset.x+e.pageX;
var y=this.dragOffset.y+e.pageY;
if(this.constrainToContainer){
if(x<this.constraints.minX){
x=this.constraints.minX;
}
if(y<this.constraints.minY){
y=this.constraints.minY;
}
if(x>this.constraints.maxX){
x=this.constraints.maxX;
}
if(y>this.constraints.maxY){
y=this.constraints.maxY;
}
}
this.setAbsolutePosition(x,y);
dojo.event.topic.publish("dragMove",{source:this});
},setAbsolutePosition:function(x,y){
if(!this.disableY){
this.dragClone.style.top=y+"px";
}
if(!this.disableX){
this.dragClone.style.left=x+"px";
}
},onDragEnd:function(e){
switch(e.dragStatus){
case "dropSuccess":
dojo.html.removeNode(this.dragClone);
this.dragClone=null;
break;
case "dropFailure":
var _27=dojo.html.getAbsolutePosition(this.dragClone,true);
var _28={left:this.dragStartPosition.x+1,top:this.dragStartPosition.y+1};
var _29=dojo.lfx.slideTo(this.dragClone,_28,300);
var _2a=this;
dojo.event.connect(_29,"onEnd",function(e){
dojo.html.removeNode(_2a.dragClone);
_2a.dragClone=null;
});
_29.play();
break;
}
dojo.event.topic.publish("dragEnd",{source:this});
},constrainTo:function(_2c){
this.constrainToContainer=true;
if(_2c){
this.constrainingContainer=_2c;
}else{
this.constrainingContainer=this.domNode.parentNode;
}
}},function(_2d,_2e){
this.domNode=dojo.byId(_2d);
this.type=_2e;
this.constrainToContainer=false;
this.dragSource=null;
dojo.dnd.DragObject.prototype.register.call(this);
});
dojo.declare("dojo.dnd.HtmlDropTarget",dojo.dnd.DropTarget,{vertical:false,onDragOver:function(e){
if(!this.accepts(e.dragObjects)){
return false;
}
this.childBoxes=[];
for(var i=0,_31;i<this.domNode.childNodes.length;i++){
_31=this.domNode.childNodes[i];
if(_31.nodeType!=dojo.html.ELEMENT_NODE){
continue;
}
var pos=dojo.html.getAbsolutePosition(_31,true);
var _33=dojo.html.getBorderBox(_31);
this.childBoxes.push({top:pos.y,bottom:pos.y+_33.height,left:pos.x,right:pos.x+_33.width,height:_33.height,width:_33.width,node:_31});
}
return true;
},_getNodeUnderMouse:function(e){
for(var i=0,_36;i<this.childBoxes.length;i++){
with(this.childBoxes[i]){
if(e.pageX>=left&&e.pageX<=right&&e.pageY>=top&&e.pageY<=bottom){
return i;
}
}
}
return -1;
},createDropIndicator:function(){
this.dropIndicator=document.createElement("div");
with(this.dropIndicator.style){
position="absolute";
zIndex=999;
if(this.vertical){
borderLeftWidth="1px";
borderLeftColor="black";
borderLeftStyle="solid";
height=dojo.html.getBorderBox(this.domNode).height+"px";
top=dojo.html.getAbsolutePosition(this.domNode,true).y+"px";
}else{
borderTopWidth="1px";
borderTopColor="black";
borderTopStyle="solid";
width=dojo.html.getBorderBox(this.domNode).width+"px";
left=dojo.html.getAbsolutePosition(this.domNode,true).x+"px";
}
}
},onDragMove:function(e,_38){
var i=this._getNodeUnderMouse(e);
if(!this.dropIndicator){
this.createDropIndicator();
}
var _3a=this.vertical?dojo.html.gravity.WEST:dojo.html.gravity.NORTH;
var _3b=false;
if(i<0){
if(this.childBoxes.length){
var _3c=(dojo.html.gravity(this.childBoxes[0].node,e)&_3a);
if(_3c){
_3b=true;
}
}else{
var _3c=true;
}
}else{
var _3d=this.childBoxes[i];
var _3c=(dojo.html.gravity(_3d.node,e)&_3a);
if(_3d.node===_38[0].dragSource.domNode){
_3b=true;
}else{
var _3e=_3c?(i>0?this.childBoxes[i-1]:_3d):(i<this.childBoxes.length-1?this.childBoxes[i+1]:_3d);
if(_3e.node===_38[0].dragSource.domNode){
_3b=true;
}
}
}
if(_3b){
this.dropIndicator.style.display="none";
return;
}else{
this.dropIndicator.style.display="";
}
this.placeIndicator(e,_38,i,_3c);
if(!dojo.html.hasParent(this.dropIndicator)){
dojo.body().appendChild(this.dropIndicator);
}
},placeIndicator:function(e,_40,_41,_42){
var _43=this.vertical?"left":"top";
var _44;
if(_41<0){
if(this.childBoxes.length){
_44=_42?this.childBoxes[0]:this.childBoxes[this.childBoxes.length-1];
}else{
this.dropIndicator.style[_43]=dojo.html.getAbsolutePosition(this.domNode,true)[this.vertical?"x":"y"]+"px";
}
}else{
_44=this.childBoxes[_41];
}
if(_44){
this.dropIndicator.style[_43]=(_42?_44[_43]:_44[this.vertical?"right":"bottom"])+"px";
if(this.vertical){
this.dropIndicator.style.height=_44.height+"px";
this.dropIndicator.style.top=_44.top+"px";
}else{
this.dropIndicator.style.width=_44.width+"px";
this.dropIndicator.style.left=_44.left+"px";
}
}
},onDragOut:function(e){
if(this.dropIndicator){
dojo.html.removeNode(this.dropIndicator);
delete this.dropIndicator;
}
},onDrop:function(e){
this.onDragOut(e);
var i=this._getNodeUnderMouse(e);
var _48=this.vertical?dojo.html.gravity.WEST:dojo.html.gravity.NORTH;
if(i<0){
if(this.childBoxes.length){
if(dojo.html.gravity(this.childBoxes[0].node,e)&_48){
return this.insert(e,this.childBoxes[0].node,"before");
}else{
return this.insert(e,this.childBoxes[this.childBoxes.length-1].node,"after");
}
}
return this.insert(e,this.domNode,"append");
}
var _49=this.childBoxes[i];
if(dojo.html.gravity(_49.node,e)&_48){
return this.insert(e,_49.node,"before");
}else{
return this.insert(e,_49.node,"after");
}
},insert:function(e,_4b,_4c){
var _4d=e.dragObject.domNode;
if(_4c=="before"){
return dojo.html.insertBefore(_4d,_4b);
}else{
if(_4c=="after"){
return dojo.html.insertAfter(_4d,_4b);
}else{
if(_4c=="append"){
_4b.appendChild(_4d);
return true;
}
}
}
return false;
}},function(_4e,_4f){
if(arguments.length==0){
return;
}
this.domNode=dojo.byId(_4e);
dojo.dnd.DropTarget.call(this);
if(_4f&&dojo.lang.isString(_4f)){
_4f=[_4f];
}
this.acceptedTypes=_4f||[];
dojo.dnd.dragManager.registerDropTarget(this);
});
