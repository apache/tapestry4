dojo.provide("dojo.undo.Manager");
dojo.require("dojo.lang.common");
dojo.undo.Manager=function(_1){
this.clear();
this._parent=_1;
};
dojo.extend(dojo.undo.Manager,{_parent:null,_undoStack:null,_redoStack:null,_currentManager:null,canUndo:false,canRedo:false,isUndoing:false,isRedoing:false,onUndo:function(_2,_3){
},onRedo:function(_4,_5){
},onUndoAny:function(_6,_7){
},onRedoAny:function(_8,_9){
},_updateStatus:function(){
this.canUndo=this._undoStack.length>0;
this.canRedo=this._redoStack.length>0;
},clear:function(){
this._undoStack=[];
this._redoStack=[];
this._currentManager=this;
this.isUndoing=false;
this.isRedoing=false;
this._updateStatus();
},undo:function(){
if(!this.canUndo){
return false;
}
this.endAllTransactions();
this.isUndoing=true;
var _a=this._undoStack.pop();
if(_a instanceof dojo.undo.Manager){
_a.undoAll();
}else{
_a.undo();
}
if(_a.redo){
this._redoStack.push(_a);
}
this.isUndoing=false;
this._updateStatus();
this.onUndo(this,_a);
if(!(_a instanceof dojo.undo.Manager)){
this.getTop().onUndoAny(this,_a);
}
return true;
},redo:function(){
if(!this.canRedo){
return false;
}
this.isRedoing=true;
var _b=this._redoStack.pop();
if(_b instanceof dojo.undo.Manager){
_b.redoAll();
}else{
_b.redo();
}
this._undoStack.push(_b);
this.isRedoing=false;
this._updateStatus();
this.onRedo(this,_b);
if(!(_b instanceof dojo.undo.Manager)){
this.getTop().onRedoAny(this,_b);
}
return true;
},undoAll:function(){
while(this._undoStack.length>0){
this.undo();
}
},redoAll:function(){
while(this._redoStack.length>0){
this.redo();
}
},push:function(_c,_d,_e){
if(!_c){
return;
}
if(this._currentManager==this){
this._undoStack.push({undo:_c,redo:_d,description:_e});
}else{
this._currentManager.push.apply(this._currentManager,arguments);
}
this._redoStack=[];
this._updateStatus();
},concat:function(_f){
if(!_f){
return;
}
if(this._currentManager==this){
for(var x=0;x<_f._undoStack.length;x++){
this._undoStack.push(_f._undoStack[x]);
}
if(_f._undoStack.length>0){
this._redoStack=[];
}
this._updateStatus();
}else{
this._currentManager.concat.apply(this._currentManager,arguments);
}
},beginTransaction:function(_11){
if(this._currentManager==this){
var mgr=new dojo.undo.Manager(this);
mgr.description=_11?_11:"";
this._undoStack.push(mgr);
this._currentManager=mgr;
return mgr;
}else{
this._currentManager=this._currentManager.beginTransaction.apply(this._currentManager,arguments);
}
},endTransaction:function(_13){
if(this._currentManager==this){
if(this._parent){
this._parent._currentManager=this._parent;
if(this._undoStack.length==0||_13){
var idx=dojo.lang.find(this._parent._undoStack,this);
if(idx>=0){
this._parent._undoStack.splice(idx,1);
if(_13){
for(var x=0;x<this._undoStack.length;x++){
this._parent._undoStack.splice(idx++,0,this._undoStack[x]);
}
this._updateStatus();
}
}
}
return this._parent;
}
}else{
this._currentManager=this._currentManager.endTransaction.apply(this._currentManager,arguments);
}
},endAllTransactions:function(){
while(this._currentManager!=this){
this.endTransaction();
}
},getTop:function(){
if(this._parent){
return this._parent.getTop();
}else{
return this;
}
}});
