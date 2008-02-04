dojo.provide("dojo.widget.TreeDemo");
dojo.require("dojo.Deferred");
dojo.widget.TreeDemo={reportIfDefered:function(_1){
if(_1 instanceof dojo.Deferred){
_1.addCallbacks(function(_2){
return _2;
},function(_3){
dojo.debug("Error");
dojo.debugShallow(_3);
});
}
},resetRandomChildren:function(_4){
this.randomChildrenMaxCount=_4;
this.randomChildrenCount=0;
this.randomChildrenDepth=0;
},makeRandomChildren:function(_5){
this.randomChildrenDepth++;
var _6=[];
for(var i=1;i<=5;i++){
var t=_5+(this.randomChildrenDepth==1?"":".")+i;
var _9={title:t};
_6.push(_9);
this.randomChildrenCount++;
if(this.randomChildrenCount>=this.randomChildrenMaxCount){
break;
}
}
var i=1;
var _a=this;
dojo.lang.forEach(_6,function(_b){
var t=_5+(_a.randomChildrenDepth==1?"":".")+i;
i++;
if(_a.randomChildrenCount<_a.randomChildrenMaxCount&&(_a.randomChildrenDepth==1&&_b===_6[0]||_a.randomChildrenDepth<5&&Math.random()>0.3)){
_b.children=_a.makeRandomChildren(t);
}
});
this.randomChildrenDepth--;
return _6;
},bindDemoMenu:function(_d){
var _t=this;
dojo.event.topic.subscribe("treeContextMenuDestroy/engage",function(_f){
var _10=_f.getTreeNode();
_t.reportIfDefered(_d.destroyChild(_10));
});
dojo.event.topic.subscribe("treeContextMenuRefresh/engage",function(_11){
var _12=_11.getTreeNode();
_t.reportIfDefered(_d.refreshChildren(_12));
});
dojo.event.topic.subscribe("treeContextMenuCreate/engage",function(_13){
var _14=_13.getTreeNode();
var d=_d.createAndEdit(_14,0);
_t.reportIfDefered(d);
});
dojo.event.topic.subscribe("treeContextMenuUp/engage",function(_16){
var _17=_16.getTreeNode();
if(_17.isFirstChild()){
return;
}
_t.reportIfDefered(_d.move(_17,_17.parent,_17.getParentIndex()-1));
});
dojo.event.topic.subscribe("treeContextMenuDown/engage",function(_18){
var _19=_18.getTreeNode();
if(_19.isLastChild()){
return;
}
_t.reportIfDefered(_d.move(_19,_19.parent,_19.getParentIndex()+1));
});
dojo.event.topic.subscribe("treeContextMenuEdit/engage",function(_1a){
var _1b=_1a.getTreeNode();
_t.reportIfDefered(_d.editLabelStart(_1b));
});
}};
