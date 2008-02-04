dojo.provide("dojo.widget.SortableTable");
dojo.deprecated("SortableTable will be removed in favor of FilteringTable.","0.5");
dojo.require("dojo.lang.common");
dojo.require("dojo.date.format");
dojo.require("dojo.html.*");
dojo.require("dojo.html.selection");
dojo.require("dojo.html.util");
dojo.require("dojo.html.style");
dojo.require("dojo.event.*");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.widget.defineWidget("dojo.widget.SortableTable",dojo.widget.HtmlWidget,function(){
this.data=[];
this.selected=[];
this.columns=[];
},{enableMultipleSelect:false,maximumNumberOfSelections:0,enableAlternateRows:false,minRows:0,defaultDateFormat:"%D",sortIndex:0,sortDirection:0,valueField:"Id",headClass:"",tbodyClass:"",headerClass:"",headerSortUpClass:"selected",headerSortDownClass:"selected",rowClass:"",rowAlternateClass:"alt",rowSelectedClass:"selected",columnSelected:"sorted-column",isContainer:false,templatePath:null,templateCssPath:null,getTypeFromString:function(s){
var _2=s.split("."),i=0,_4=dj_global;
do{
_4=_4[_2[i++]];
}while(i<_2.length&&_4);
return (_4!=dj_global)?_4:null;
},compare:function(o1,o2){
for(var p in o1){
if(!(p in o2)){
return false;
}
if(o1[p].valueOf()!=o2[p].valueOf()){
return false;
}
}
return true;
},isSelected:function(o){
for(var i=0;i<this.selected.length;i++){
if(this.compare(this.selected[i],o)){
return true;
}
}
return false;
},removeFromSelected:function(o){
var _b=-1;
for(var i=0;i<this.selected.length;i++){
if(this.compare(this.selected[i],o)){
_b=i;
break;
}
}
if(_b>=0){
this.selected.splice(_b,1);
}
},getSelection:function(){
return this.selected;
},getValue:function(){
var a=[];
for(var i=0;i<this.selected.length;i++){
if(this.selected[i][this.valueField]){
a.push(this.selected[i][this.valueField]);
}
}
return a.join();
},reset:function(){
this.columns=[];
this.data=[];
this.resetSelections(this.domNode.getElementsByTagName("tbody")[0]);
},resetSelections:function(_f){
this.selected=[];
var idx=0;
var _11=_f.getElementsByTagName("tr");
for(var i=0;i<_11.length;i++){
if(_11[i].parentNode==_f){
_11[i].removeAttribute("selected");
if(this.enableAlternateRows&&idx%2==1){
_11[i].className=this.rowAlternateClass;
}else{
_11[i].className="";
}
idx++;
}
}
},getObjectFromRow:function(row){
var _14=row.getElementsByTagName("td");
var o={};
for(var i=0;i<this.columns.length;i++){
if(this.columns[i].sortType=="__markup__"){
o[this.columns[i].getField()]=_14[i].innerHTML;
}else{
var _17=dojo.html.renderedTextContent(_14[i]);
var val=_17;
if(this.columns[i].getType()!=String){
var val=new (this.columns[i].getType())(_17);
}
o[this.columns[i].getField()]=val;
}
}
if(dojo.html.hasAttribute(row,"value")){
o[this.valueField]=dojo.html.getAttribute(row,"value");
}
return o;
},setSelectionByRow:function(row){
var o=this.getObjectFromRow(row);
var b=false;
for(var i=0;i<this.selected.length;i++){
if(this.compare(this.selected[i],o)){
b=true;
break;
}
}
if(!b){
this.selected.push(o);
}
},parseColumns:function(_1d){
this.reset();
var row=_1d.getElementsByTagName("tr")[0];
var _1f=row.getElementsByTagName("td");
if(_1f.length==0){
_1f=row.getElementsByTagName("th");
}
for(var i=0;i<_1f.length;i++){
var o={field:null,format:null,noSort:false,sortType:"String",dataType:String,sortFunction:null,label:null,align:"left",valign:"middle",getField:function(){
return this.field||this.label;
},getType:function(){
return this.dataType;
}};
if(dojo.html.hasAttribute(_1f[i],"align")){
o.align=dojo.html.getAttribute(_1f[i],"align");
}
if(dojo.html.hasAttribute(_1f[i],"valign")){
o.valign=dojo.html.getAttribute(_1f[i],"valign");
}
if(dojo.html.hasAttribute(_1f[i],"nosort")){
o.noSort=dojo.html.getAttribute(_1f[i],"nosort")=="true";
}
if(dojo.html.hasAttribute(_1f[i],"sortusing")){
var _22=dojo.html.getAttribute(_1f[i],"sortusing");
var f=this.getTypeFromString(_22);
if(f!=null&&f!=window&&typeof (f)=="function"){
o.sortFunction=f;
}
}
if(dojo.html.hasAttribute(_1f[i],"field")){
o.field=dojo.html.getAttribute(_1f[i],"field");
}
if(dojo.html.hasAttribute(_1f[i],"format")){
o.format=dojo.html.getAttribute(_1f[i],"format");
}
if(dojo.html.hasAttribute(_1f[i],"dataType")){
var _24=dojo.html.getAttribute(_1f[i],"dataType");
if(_24.toLowerCase()=="html"||_24.toLowerCase()=="markup"){
o.sortType="__markup__";
o.noSort=true;
}else{
var _25=this.getTypeFromString(_24);
if(_25){
o.sortType=_24;
o.dataType=_25;
}
}
}
o.label=dojo.html.renderedTextContent(_1f[i]);
this.columns.push(o);
if(dojo.html.hasAttribute(_1f[i],"sort")){
this.sortIndex=i;
var dir=dojo.html.getAttribute(_1f[i],"sort");
if(!isNaN(parseInt(dir))){
dir=parseInt(dir);
this.sortDirection=(dir!=0)?1:0;
}else{
this.sortDirection=(dir.toLowerCase()=="desc")?1:0;
}
}
}
},parseData:function(_27){
this.data=[];
this.selected=[];
for(var i=0;i<_27.length;i++){
var o={};
for(var j=0;j<this.columns.length;j++){
var _2b=this.columns[j].getField();
if(this.columns[j].sortType=="__markup__"){
o[_2b]=String(_27[i][_2b]);
}else{
var _2c=this.columns[j].getType();
var val=_27[i][_2b];
var t=this.columns[j].sortType.toLowerCase();
if(_2c==String){
o[_2b]=val;
}else{
if(val!=null){
o[_2b]=new _2c(val);
}else{
o[_2b]=new _2c();
}
}
}
}
if(_27[i][this.valueField]&&!o[this.valueField]){
o[this.valueField]=_27[i][this.valueField];
}
this.data.push(o);
}
},parseDataFromTable:function(_2f){
this.data=[];
this.selected=[];
var _30=_2f.getElementsByTagName("tr");
for(var i=0;i<_30.length;i++){
if(dojo.html.getAttribute(_30[i],"ignoreIfParsed")=="true"){
continue;
}
var o={};
var _33=_30[i].getElementsByTagName("td");
for(var j=0;j<this.columns.length;j++){
var _35=this.columns[j].getField();
if(this.columns[j].sortType=="__markup__"){
o[_35]=_33[j].innerHTML;
}else{
var _36=this.columns[j].getType();
var val=dojo.html.renderedTextContent(_33[j]);
if(_36==String){
o[_35]=val;
}else{
if(val!=null){
o[_35]=new _36(val);
}else{
o[_35]=new _36();
}
}
}
}
if(dojo.html.hasAttribute(_30[i],"value")&&!o[this.valueField]){
o[this.valueField]=dojo.html.getAttribute(_30[i],"value");
}
this.data.push(o);
if(dojo.html.getAttribute(_30[i],"selected")=="true"){
this.selected.push(o);
}
}
},showSelections:function(){
var _38=this.domNode.getElementsByTagName("tbody")[0];
var _39=_38.getElementsByTagName("tr");
var idx=0;
for(var i=0;i<_39.length;i++){
if(_39[i].parentNode==_38){
if(dojo.html.getAttribute(_39[i],"selected")=="true"){
_39[i].className=this.rowSelectedClass;
}else{
if(this.enableAlternateRows&&idx%2==1){
_39[i].className=this.rowAlternateClass;
}else{
_39[i].className="";
}
}
idx++;
}
}
},render:function(_3c){
var _3d=[];
var _3e=this.domNode.getElementsByTagName("tbody")[0];
if(!_3c){
this.parseDataFromTable(_3e);
}
for(var i=0;i<this.data.length;i++){
_3d.push(this.data[i]);
}
var col=this.columns[this.sortIndex];
if(!col.noSort){
var _41=col.getField();
if(col.sortFunction){
var _42=col.sortFunction;
}else{
var _42=function(a,b){
if(a[_41]>b[_41]){
return 1;
}
if(a[_41]<b[_41]){
return -1;
}
return 0;
};
}
_3d.sort(_42);
if(this.sortDirection!=0){
_3d.reverse();
}
}
while(_3e.childNodes.length>0){
_3e.removeChild(_3e.childNodes[0]);
}
for(var i=0;i<_3d.length;i++){
var row=document.createElement("tr");
dojo.html.disableSelection(row);
if(_3d[i][this.valueField]){
row.setAttribute("value",_3d[i][this.valueField]);
}
if(this.isSelected(_3d[i])){
row.className=this.rowSelectedClass;
row.setAttribute("selected","true");
}else{
if(this.enableAlternateRows&&i%2==1){
row.className=this.rowAlternateClass;
}
}
for(var j=0;j<this.columns.length;j++){
var _47=document.createElement("td");
_47.setAttribute("align",this.columns[j].align);
_47.setAttribute("valign",this.columns[j].valign);
dojo.html.disableSelection(_47);
if(this.sortIndex==j){
_47.className=this.columnSelected;
}
if(this.columns[j].sortType=="__markup__"){
_47.innerHTML=_3d[i][this.columns[j].getField()];
for(var k=0;k<_47.childNodes.length;k++){
var _49=_47.childNodes[k];
if(_49&&_49.nodeType==dojo.html.ELEMENT_NODE){
dojo.html.disableSelection(_49);
}
}
}else{
if(this.columns[j].getType()==Date){
var _4a=this.defaultDateFormat;
if(this.columns[j].format){
_4a=this.columns[j].format;
}
_47.appendChild(document.createTextNode(dojo.date.strftime(_3d[i][this.columns[j].getField()],_4a)));
}else{
_47.appendChild(document.createTextNode(_3d[i][this.columns[j].getField()]));
}
}
row.appendChild(_47);
}
_3e.appendChild(row);
dojo.event.connect(row,"onclick",this,"onUISelect");
}
var _4b=parseInt(this.minRows);
if(!isNaN(_4b)&&_4b>0&&_3d.length<_4b){
var mod=0;
if(_3d.length%2==0){
mod=1;
}
var _4d=_4b-_3d.length;
for(var i=0;i<_4d;i++){
var row=document.createElement("tr");
row.setAttribute("ignoreIfParsed","true");
if(this.enableAlternateRows&&i%2==mod){
row.className=this.rowAlternateClass;
}
for(var j=0;j<this.columns.length;j++){
var _47=document.createElement("td");
_47.appendChild(document.createTextNode("\xa0"));
row.appendChild(_47);
}
_3e.appendChild(row);
}
}
},onSelect:function(e){
},onUISelect:function(e){
var row=dojo.html.getParentByType(e.target,"tr");
var _51=dojo.html.getParentByType(row,"tbody");
if(this.enableMultipleSelect){
if(e.metaKey||e.ctrlKey){
if(this.isSelected(this.getObjectFromRow(row))){
this.removeFromSelected(this.getObjectFromRow(row));
row.removeAttribute("selected");
}else{
this.setSelectionByRow(row);
row.setAttribute("selected","true");
}
}else{
if(e.shiftKey){
var _52;
var _53=_51.getElementsByTagName("tr");
for(var i=0;i<_53.length;i++){
if(_53[i].parentNode==_51){
if(_53[i]==row){
break;
}
if(dojo.html.getAttribute(_53[i],"selected")=="true"){
_52=_53[i];
}
}
}
if(!_52){
_52=row;
for(;i<_53.length;i++){
if(dojo.html.getAttribute(_53[i],"selected")=="true"){
row=_53[i];
break;
}
}
}
this.resetSelections(_51);
if(_52==row){
row.setAttribute("selected","true");
this.setSelectionByRow(row);
}else{
var _55=false;
for(var i=0;i<_53.length;i++){
if(_53[i].parentNode==_51){
_53[i].removeAttribute("selected");
if(_53[i]==_52){
_55=true;
}
if(_55){
this.setSelectionByRow(_53[i]);
_53[i].setAttribute("selected","true");
}
if(_53[i]==row){
_55=false;
}
}
}
}
}else{
this.resetSelections(_51);
row.setAttribute("selected","true");
this.setSelectionByRow(row);
}
}
}else{
this.resetSelections(_51);
row.setAttribute("selected","true");
this.setSelectionByRow(row);
}
this.showSelections();
this.onSelect(e);
e.stopPropagation();
e.preventDefault();
},onHeaderClick:function(e){
var _57=this.sortIndex;
var _58=this.sortDirection;
var _59=e.target;
var row=dojo.html.getParentByType(_59,"tr");
var _5b="td";
if(row.getElementsByTagName(_5b).length==0){
_5b="th";
}
var _5c=row.getElementsByTagName(_5b);
var _5d=dojo.html.getParentByType(_59,_5b);
for(var i=0;i<_5c.length;i++){
if(_5c[i]==_5d){
if(i!=_57){
this.sortIndex=i;
this.sortDirection=0;
_5c[i].className=this.headerSortDownClass;
}else{
this.sortDirection=(_58==0)?1:0;
if(this.sortDirection==0){
_5c[i].className=this.headerSortDownClass;
}else{
_5c[i].className=this.headerSortUpClass;
}
}
}else{
_5c[i].className=this.headerClass;
}
}
this.render();
},postCreate:function(){
var _5f=this.domNode.getElementsByTagName("thead")[0];
if(this.headClass.length>0){
_5f.className=this.headClass;
}
dojo.html.disableSelection(this.domNode);
this.parseColumns(_5f);
var _60="td";
if(_5f.getElementsByTagName(_60).length==0){
_60="th";
}
var _61=_5f.getElementsByTagName(_60);
for(var i=0;i<_61.length;i++){
if(!this.columns[i].noSort){
dojo.event.connect(_61[i],"onclick",this,"onHeaderClick");
}
if(this.sortIndex==i){
if(this.sortDirection==0){
_61[i].className=this.headerSortDownClass;
}else{
_61[i].className=this.headerSortUpClass;
}
}
}
var _63=this.domNode.getElementsByTagName("tbody")[0];
if(this.tbodyClass.length>0){
_63.className=this.tbodyClass;
}
this.parseDataFromTable(_63);
this.render(true);
}});
