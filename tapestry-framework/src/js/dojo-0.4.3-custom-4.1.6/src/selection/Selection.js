dojo.provide("dojo.selection.Selection");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.func");
dojo.require("dojo.lang.common");
dojo.require("dojo.math");
dojo.declare("dojo.selection.Selection",null,{initializer:function(_1,_2){
this.items=[];
this.selection=[];
this._pivotItems=[];
this.clearItems();
if(_1){
if(_2){
this.setItemsCollection(_1);
}else{
this.setItems(_1);
}
}
},items:null,selection:null,lastSelected:null,allowImplicit:true,length:0,isGrowable:true,_pivotItems:null,_pivotItem:null,onSelect:function(_3){
},onDeselect:function(_4){
},onSelectChange:function(_5,_6){
},_find:function(_7,_8){
if(_8){
return dojo.lang.find(this.selection,_7);
}else{
return dojo.lang.find(this.items,_7);
}
},isSelectable:function(_9){
return true;
},setItems:function(){
this.clearItems();
this.addItems.call(this,arguments);
},setItemsCollection:function(_a){
this.items=_a;
},addItems:function(){
var _b=dojo.lang.unnest(arguments);
for(var i=0;i<_b.length;i++){
this.items.push(_b[i]);
}
},addItemsAt:function(_d,_e){
if(this.items.length==0){
return this.addItems(dojo.lang.toArray(arguments,2));
}
if(!this.isItem(_d)){
_d=this.items[_d];
}
if(!_d){
throw new Error("addItemsAt: item doesn't exist");
}
var _f=this._find(_d);
if(_f>0&&_e){
_f--;
}
for(var i=2;i<arguments.length;i++){
if(!this.isItem(arguments[i])){
this.items.splice(_f++,0,arguments[i]);
}
}
},removeItem:function(_11){
var idx=this._find(_11);
if(idx>-1){
this.items.splice(idx,1);
}
idx=this._find(_11,true);
if(idx>-1){
this.selection.splice(idx,1);
}
},clearItems:function(){
this.items=[];
this.deselectAll();
},isItem:function(_13){
return this._find(_13)>-1;
},isSelected:function(_14){
return this._find(_14,true)>-1;
},selectFilter:function(_15,_16,add,_18){
return true;
},update:function(_19,add,_1b,_1c){
if(!this.isItem(_19)){
return false;
}
if(this.isGrowable&&_1b){
if((!this.isSelected(_19))&&this.selectFilter(_19,this.selection,false,true)){
this.grow(_19);
this.lastSelected=_19;
}
}else{
if(add){
if(this.selectFilter(_19,this.selection,true,false)){
if(_1c){
if(this.select(_19)){
this.lastSelected=_19;
}
}else{
if(this.toggleSelected(_19)){
this.lastSelected=_19;
}
}
}
}else{
this.deselectAll();
this.select(_19);
}
}
this.length=this.selection.length;
return true;
},grow:function(_1d,_1e){
if(!this.isGrowable){
return;
}
if(arguments.length==1){
_1e=this._pivotItem;
if(!_1e&&this.allowImplicit){
_1e=this.items[0];
}
}
if(!_1d||!_1e){
return false;
}
var _1f=this._find(_1e);
var _20={};
var _21=-1;
if(this.lastSelected){
_21=this._find(this.lastSelected);
var _22=_1f<_21?-1:1;
var _23=dojo.math.range(_21,_1f,_22);
for(var i=0;i<_23.length;i++){
_20[_23[i]]=true;
}
}
var _25=this._find(_1d);
var _22=_1f<_25?-1:1;
var _26=_21>=0&&_22==1?_21<_25:_21>_25;
var _23=dojo.math.range(_25,_1f,_22);
if(_23.length){
for(var i=_23.length-1;i>=0;i--){
var _27=this.items[_23[i]];
if(this.selectFilter(_27,this.selection,false,true)){
if(this.select(_27,true)||_26){
this.lastSelected=_27;
}
if(_23[i] in _20){
delete _20[_23[i]];
}
}
}
}else{
this.lastSelected=_1e;
}
for(var i in _20){
if(this.items[i]==this.lastSelected){
}
this.deselect(this.items[i]);
}
this._updatePivot();
},growUp:function(){
if(!this.isGrowable){
return;
}
var idx=this._find(this.lastSelected)-1;
while(idx>=0){
if(this.selectFilter(this.items[idx],this.selection,false,true)){
this.grow(this.items[idx]);
break;
}
idx--;
}
},growDown:function(){
if(!this.isGrowable){
return;
}
var idx=this._find(this.lastSelected);
if(idx<0&&this.allowImplicit){
this.select(this.items[0]);
idx=0;
}
idx++;
while(idx>0&&idx<this.items.length){
if(this.selectFilter(this.items[idx],this.selection,false,true)){
this.grow(this.items[idx]);
break;
}
idx++;
}
},toggleSelected:function(_2a,_2b){
if(this.isItem(_2a)){
if(this.select(_2a,_2b)){
return 1;
}
if(this.deselect(_2a)){
return -1;
}
}
return 0;
},select:function(_2c,_2d){
if(this.isItem(_2c)&&!this.isSelected(_2c)&&this.isSelectable(_2c)){
this.selection.push(_2c);
this.lastSelected=_2c;
this.onSelect(_2c);
this.onSelectChange(_2c,true);
if(!_2d){
this._addPivot(_2c);
}
this.length=this.selection.length;
return true;
}
return false;
},deselect:function(_2e){
var idx=this._find(_2e,true);
if(idx>-1){
this.selection.splice(idx,1);
this.onDeselect(_2e);
this.onSelectChange(_2e,false);
if(_2e==this.lastSelected){
this.lastSelected=null;
}
this._removePivot(_2e);
this.length=this.selection.length;
return true;
}
return false;
},selectAll:function(){
for(var i=0;i<this.items.length;i++){
this.select(this.items[i]);
}
},deselectAll:function(){
while(this.selection&&this.selection.length){
this.deselect(this.selection[0]);
}
},selectNext:function(){
var idx=this._find(this.lastSelected);
while(idx>-1&&++idx<this.items.length){
if(this.isSelectable(this.items[idx])){
this.deselectAll();
this.select(this.items[idx]);
return true;
}
}
return false;
},selectPrevious:function(){
var idx=this._find(this.lastSelected);
while(idx-->0){
if(this.isSelectable(this.items[idx])){
this.deselectAll();
this.select(this.items[idx]);
return true;
}
}
return false;
},selectFirst:function(){
this.deselectAll();
var idx=0;
while(this.items[idx]&&!this.select(this.items[idx])){
idx++;
}
return this.items[idx]?true:false;
},selectLast:function(){
this.deselectAll();
var idx=this.items.length-1;
while(this.items[idx]&&!this.select(this.items[idx])){
idx--;
}
return this.items[idx]?true:false;
},_addPivot:function(_35,_36){
this._pivotItem=_35;
if(_36){
this._pivotItems=[_35];
}else{
this._pivotItems.push(_35);
}
},_removePivot:function(_37){
var i=dojo.lang.find(this._pivotItems,_37);
if(i>-1){
this._pivotItems.splice(i,1);
this._pivotItem=this._pivotItems[this._pivotItems.length-1];
}
this._updatePivot();
},_updatePivot:function(){
if(this._pivotItems.length==0){
if(this.lastSelected){
this._addPivot(this.lastSelected);
}
}
},sorted:function(){
return dojo.lang.toArray(this.selection).sort(dojo.lang.hitch(this,function(a,b){
var A=this._find(a),B=this._find(b);
if(A>B){
return 1;
}else{
if(A<B){
return -1;
}else{
return 0;
}
}
}));
},updateSelected:function(){
for(var i=0;i<this.selection.length;i++){
if(this._find(this.selection[i])<0){
var _3e=this.selection.splice(i,1);
this._removePivot(_3e[0]);
}
}
this.length=this.selection.length;
}});
