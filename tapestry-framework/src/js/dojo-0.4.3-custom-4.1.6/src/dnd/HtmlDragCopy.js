dojo.provide("dojo.dnd.HtmlDragCopy");
dojo.require("dojo.dnd.*");
dojo.declare("dojo.dnd.HtmlDragCopySource",dojo.dnd.HtmlDragSource,function(_1,_2,_3){
this.copyOnce=_3;
this.makeCopy=true;
},{onDragStart:function(){
var _4=new dojo.dnd.HtmlDragCopyObject(this.dragObject,this.type,this);
if(this.dragClass){
_4.dragClass=this.dragClass;
}
if(this.constrainToContainer){
_4.constrainTo(this.constrainingContainer||this.domNode.parentNode);
}
return _4;
},onSelected:function(){
for(var i=0;i<this.dragObjects.length;i++){
dojo.dnd.dragManager.selectedSources.push(new dojo.dnd.HtmlDragCopySource(this.dragObjects[i]));
}
}});
dojo.declare("dojo.dnd.HtmlDragCopyObject",dojo.dnd.HtmlDragObject,function(_6,_7,_8){
this.copySource=_8;
},{onDragStart:function(e){
dojo.dnd.HtmlDragCopyObject.superclass.onDragStart.apply(this,arguments);
if(this.copySource.makeCopy){
this.sourceNode=this.domNode;
this.domNode=this.domNode.cloneNode(true);
}
},onDragEnd:function(e){
switch(e.dragStatus){
case "dropFailure":
var _b=dojo.html.getAbsolutePosition(this.dragClone,true);
var _c={left:this.dragStartPosition.x+1,top:this.dragStartPosition.y+1};
var _d=dojo.lfx.slideTo(this.dragClone,_c,500,dojo.lfx.easeOut);
var _e=this;
dojo.event.connect(_d,"onEnd",function(e){
dojo.lang.setTimeout(function(){
dojo.html.removeNode(_e.dragClone);
_e.dragClone=null;
if(_e.copySource.makeCopy){
dojo.html.removeNode(_e.domNode);
_e.domNode=_e.sourceNode;
_e.sourceNode=null;
}
},200);
});
_d.play();
dojo.event.topic.publish("dragEnd",{source:this});
return;
}
dojo.dnd.HtmlDragCopyObject.superclass.onDragEnd.apply(this,arguments);
this.copySource.dragObject=this.domNode;
if(this.copySource.copyOnce){
this.copySource.makeCopy=false;
}
new dojo.dnd.HtmlDragCopySource(this.sourceNode,this.type,this.copySource.copyOnce);
this.sourceNode=null;
}});
