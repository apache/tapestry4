dojo.provide("dojo.widget.RadioGroup");
dojo.require("dojo.lang.common");
dojo.require("dojo.event.browser");
dojo.require("dojo.html.selection");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.widget.defineWidget("dojo.widget.RadioGroup",dojo.widget.HtmlWidget,function(){
this.selectedItem=null;
this.items=[];
this.selected=[];
this.groupCssClass="radioGroup";
this.selectedCssClass="selected";
this.itemContentCssClass="itemContent";
},{isContainer:false,templatePath:null,templateCssPath:null,postCreate:function(){
this._parseStructure();
dojo.html.addClass(this.domNode,this.groupCssClass);
this._setupChildren();
dojo.event.browser.addListener(this.domNode,"onclick",dojo.lang.hitch(this,"onSelect"));
if(this.selectedItem){
this._selectItem(this.selectedItem);
}
},_parseStructure:function(){
if(this.domNode.tagName.toLowerCase()!="ul"&&this.domNode.tagName.toLowerCase()!="ol"){
dojo.raise("RadioGroup: Expected ul or ol content.");
return;
}
this.items=[];
var nl=this.domNode.getElementsByTagName("li");
for(var i=0;i<nl.length;i++){
if(nl[i].parentNode==this.domNode){
this.items.push(nl[i]);
}
}
},add:function(_3){
if(_3.parentNode!=this.domNode){
this.domNode.appendChild(_3);
}
this.items.push(_3);
this._setup(_3);
},remove:function(_4){
var _5=-1;
for(var i=0;i<this.items.length;i++){
if(this.items[i]==_4){
_5=i;
break;
}
}
if(_5<0){
return;
}
this.items.splice(_5,1);
_4.parentNode.removeChild(_4);
},clear:function(){
for(var i=0;i<this.items.length;i++){
this.domNode.removeChild(this.items[i]);
}
this.items=[];
},clearSelections:function(){
for(var i=0;i<this.items.length;i++){
dojo.html.removeClass(this.items[i],this.selectedCssClass);
}
this.selectedItem=null;
},_setup:function(_9){
var _a=document.createElement("span");
dojo.html.disableSelection(_a);
dojo.html.addClass(_a,this.itemContentCssClass);
dojo.dom.moveChildren(_9,_a);
_9.appendChild(_a);
if(this.selected.length>0){
var _b=dojo.html.getAttribute(_9,"id");
if(_b&&_b==this.selected){
this.selectedItem=_9;
}
}
dojo.event.browser.addListener(_9,"onclick",dojo.lang.hitch(this,"onItemSelect"));
if(dojo.html.hasAttribute(_9,"onitemselect")){
var tn=dojo.lang.nameAnonFunc(new Function(dojo.html.getAttribute(_9,"onitemselect")),this);
dojo.event.browser.addListener(_9,"onclick",dojo.lang.hitch(this,tn));
}
},_setupChildren:function(){
for(var i=0;i<this.items.length;i++){
this._setup(this.items[i]);
}
},_selectItem:function(_e,_f,_10){
if(this.selectedItem){
dojo.html.removeClass(this.selectedItem,this.selectedCssClass);
}
this.selectedItem=_e;
dojo.html.addClass(this.selectedItem,this.selectedCssClass);
if(!dj_undef("currentTarget",_f)){
return;
}
if(!_10){
if(dojo.render.html.ie){
this.selectedItem.fireEvent("onclick");
}else{
var e=document.createEvent("MouseEvents");
e.initEvent("click",true,false);
this.selectedItem.dispatchEvent(e);
}
}
},getValue:function(){
return this.selectedItem;
},onSelect:function(e){
},onItemSelect:function(e){
if(!dj_undef("currentTarget",e)){
this._selectItem(e.currentTarget,e);
}
}});
