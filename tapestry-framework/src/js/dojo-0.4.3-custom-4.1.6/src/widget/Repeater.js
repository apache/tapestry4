dojo.provide("dojo.widget.Repeater");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.string");
dojo.require("dojo.event.*");
dojo.require("dojo.experimental");
dojo.experimental("dojo.widget.Repeater");
dojo.widget.defineWidget("dojo.widget.Repeater",dojo.widget.HtmlWidget,{name:"",rowTemplate:"",myObject:null,pattern:"",useDnd:false,isContainer:true,initialize:function(_1,_2){
var _3=this.getFragNodeRef(_2);
_3.removeAttribute("dojotype");
this.setRow(dojo.string.trim(_3.innerHTML),{});
_3.innerHTML="";
_2=null;
},postCreate:function(_4,_5){
if(this.useDnd){
dojo.require("dojo.dnd.*");
var _6=new dojo.dnd.HtmlDropTarget(this.domNode,[this.widgetId]);
}
},_reIndexRows:function(){
for(var i=0,_8=this.domNode.childNodes.length;i<_8;i++){
var _9=["INPUT","SELECT","TEXTAREA"];
for(var k=0;k<_9.length;k++){
var _b=this.domNode.childNodes[i].getElementsByTagName(_9[k]);
for(var j=0,_d=_b.length;j<_d;j++){
var _e=_b[j].name;
var _f=dojo.string.escape("regexp",this.pattern);
_f=_f.replace(/(%\\\{index\\\})/g,"%{index}");
var _10=dojo.string.substituteParams(_f,{"index":"[0-9]*"});
var _11=dojo.string.substituteParams(this.pattern,{"index":""+i});
var re=new RegExp(_10,"g");
_b[j].name=_e.replace(re,_11);
}
}
}
},onDeleteRow:function(e){
var _14=dojo.string.escape("regexp",this.pattern);
_14=_14.replace(/%\\\{index\\\}/g,"%{index}");
var _15=dojo.string.substituteParams(_14,{"index":"([0-9]*)"});
var re=new RegExp(_15,"g");
this.deleteRow(re.exec(e.target.name)[1]);
},hasRows:function(){
if(this.domNode.childNodes.length>0){
return true;
}
return false;
},getRowCount:function(){
return this.domNode.childNodes.length;
},deleteRow:function(idx){
this.domNode.removeChild(this.domNode.childNodes[idx]);
this._reIndexRows();
},_changeRowPosition:function(e){
if(e.dragStatus=="dropFailure"){
this.domNode.removeChild(e["dragSource"].domNode);
}else{
if(e.dragStatus=="dropSuccess"){
}
}
this._reIndexRows();
},setRow:function(_19,_1a){
_19=_19.replace(/\%\{(index)\}/g,"0");
this.rowTemplate=_19;
this.myObject=_1a;
},getRow:function(){
return this.rowTemplate;
},_initRow:function(_1b){
if(typeof (_1b)=="number"){
_1b=this.domNode.childNodes[_1b];
}
var _1c=["INPUT","SELECT","IMG"];
for(var k=0;k<_1c.length;k++){
var _1e=_1b.getElementsByTagName(_1c[k]);
for(var i=0,len=_1e.length;i<len;i++){
var _21=_1e[i];
if(_21.nodeType!=1){
continue;
}
if(_21.getAttribute("rowFunction")!=null){
if(typeof (this.myObject[_21.getAttribute("rowFunction")])=="undefined"){
dojo.debug("Function "+_21.getAttribute("rowFunction")+" not found");
}else{
this.myObject[_21.getAttribute("rowFunction")](_21);
}
}else{
if(_21.getAttribute("rowAction")!=null){
if(_21.getAttribute("rowAction")=="delete"){
_21.name=dojo.string.substituteParams(this.pattern,{"index":""+(this.getRowCount()-1)});
dojo.event.connect(_21,"onclick",this,"onDeleteRow");
}
}
}
}
}
},onAddRow:function(e){
},addRow:function(_23){
if(typeof (_23)=="undefined"){
_23=true;
}
var _24=document.createElement("span");
_24.innerHTML=this.getRow();
if(_24.childNodes.length==1){
_24=_24.childNodes[0];
}
this.domNode.appendChild(_24);
var _25=new dojo.xml.Parse();
var _26=_25.parseElement(_24,null,true);
dojo.widget.getParser().createSubComponents(_26,this);
this._reIndexRows();
if(_23){
this._initRow(_24);
}
if(this.useDnd){
_24=new dojo.dnd.HtmlDragSource(_24,this.widgetId);
dojo.event.connect(_24,"onDragEnd",this,"_changeRowPosition");
}
this.onAddRow(_24);
}});
