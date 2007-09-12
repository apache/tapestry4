dojo.provide("dojo.widget.FilteringTable");
dojo.require("dojo.date.format");
dojo.require("dojo.math");
dojo.require("dojo.collections.Store");
dojo.require("dojo.html.*");
dojo.require("dojo.html.util");
dojo.require("dojo.html.style");
dojo.require("dojo.html.selection");
dojo.require("dojo.event.*");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.widget.defineWidget("dojo.widget.FilteringTable",dojo.widget.HtmlWidget,function(){
this.store=new dojo.collections.Store();
this.valueField="Id";
this.multiple=false;
this.maxSelect=0;
this.maxSortable=1;
this.minRows=0;
this.defaultDateFormat="%D";
this.isInitialized=false;
this.alternateRows=false;
this.columns=[];
this.sortInformation=[{index:0,direction:0}];
this.headClass="";
this.tbodyClass="";
this.headerClass="";
this.headerUpClass="selectedUp";
this.headerDownClass="selectedDown";
this.rowClass="";
this.rowAlternateClass="alt";
this.rowSelectedClass="selected";
this.columnSelected="sorted-column";
},{isContainer:false,templatePath:null,templateCssPath:null,getTypeFromString:function(s){
var _2=s.split("."),i=0,_4=dj_global;
do{
_4=_4[_2[i++]];
}while(i<_2.length&&_4);
return (_4!=dj_global)?_4:null;
},getByRow:function(_5){
return this.store.getByKey(dojo.html.getAttribute(_5,"value"));
},getDataByRow:function(_6){
return this.store.getDataByKey(dojo.html.getAttribute(_6,"value"));
},getRow:function(_7){
var _8=this.domNode.tBodies[0].rows;
for(var i=0;i<_8.length;i++){
if(this.store.getDataByKey(dojo.html.getAttribute(_8[i],"value"))==_7){
return _8[i];
}
}
return null;
},getColumnIndex:function(_a){
for(var i=0;i<this.columns.length;i++){
if(this.columns[i].getField()==_a){
return i;
}
}
return -1;
},getSelectedData:function(){
var _c=this.store.get();
var a=[];
for(var i=0;i<_c.length;i++){
if(_c[i].isSelected){
a.push(_c[i].src);
}
}
if(this.multiple){
return a;
}else{
return a[0];
}
},isSelected:function(_f){
var _10=this.store.get();
for(var i=0;i<_10.length;i++){
if(_10[i].src==_f){
return true;
}
}
return false;
},isValueSelected:function(val){
var v=this.store.getByKey(val);
if(v){
return v.isSelected;
}
return false;
},isIndexSelected:function(idx){
var v=this.store.getByIndex(idx);
if(v){
return v.isSelected;
}
return false;
},isRowSelected:function(row){
var v=this.getByRow(row);
if(v){
return v.isSelected;
}
return false;
},reset:function(){
this.store.clearData();
this.columns=[];
this.sortInformation=[{index:0,direction:0}];
this.resetSelections();
this.isInitialized=false;
this.onReset();
},resetSelections:function(){
this.store.forEach(function(_18){
_18.isSelected=false;
});
},onReset:function(){
},select:function(obj){
var _1a=this.store.get();
for(var i=0;i<_1a.length;i++){
if(_1a[i].src==obj){
_1a[i].isSelected=true;
break;
}
}
this.onDataSelect(obj);
},selectByValue:function(val){
this.select(this.store.getDataByKey(val));
},selectByIndex:function(idx){
this.select(this.store.getDataByIndex(idx));
},selectByRow:function(row){
this.select(this.getDataByRow(row));
},selectAll:function(){
this.store.forEach(function(_1f){
_1f.isSelected=true;
});
},onDataSelect:function(obj){
},toggleSelection:function(obj){
var _22=this.store.get();
for(var i=0;i<_22.length;i++){
if(_22[i].src==obj){
_22[i].isSelected=!_22[i].isSelected;
break;
}
}
this.onDataToggle(obj);
},toggleSelectionByValue:function(val){
this.toggleSelection(this.store.getDataByKey(val));
},toggleSelectionByIndex:function(idx){
this.toggleSelection(this.store.getDataByIndex(idx));
},toggleSelectionByRow:function(row){
this.toggleSelection(this.getDataByRow(row));
},toggleAll:function(){
this.store.forEach(function(_27){
_27.isSelected=!_27.isSelected;
});
},onDataToggle:function(obj){
},_meta:{field:null,format:null,filterer:null,noSort:false,sortType:"String",dataType:String,sortFunction:null,filterFunction:null,label:null,align:"left",valign:"middle",getField:function(){
return this.field||this.label;
},getType:function(){
return this.dataType;
}},createMetaData:function(obj){
for(var p in this._meta){
if(!obj[p]){
obj[p]=this._meta[p];
}
}
if(!obj.label){
obj.label=obj.field;
}
if(!obj.filterFunction){
obj.filterFunction=this._defaultFilter;
}
return obj;
},parseMetadata:function(_2b){
this.columns=[];
this.sortInformation=[];
var row=_2b.getElementsByTagName("tr")[0];
var _2d=row.getElementsByTagName("td");
if(_2d.length==0){
_2d=row.getElementsByTagName("th");
}
for(var i=0;i<_2d.length;i++){
var o=this.createMetaData({});
if(dojo.html.hasAttribute(_2d[i],"align")){
o.align=dojo.html.getAttribute(_2d[i],"align");
}
if(dojo.html.hasAttribute(_2d[i],"valign")){
o.valign=dojo.html.getAttribute(_2d[i],"valign");
}
if(dojo.html.hasAttribute(_2d[i],"nosort")){
o.noSort=(dojo.html.getAttribute(_2d[i],"nosort")=="true");
}
if(dojo.html.hasAttribute(_2d[i],"sortusing")){
var _30=dojo.html.getAttribute(_2d[i],"sortusing");
var f=this.getTypeFromString(_30);
if(f!=null&&f!=window&&typeof (f)=="function"){
o.sortFunction=f;
}
}
o.label=dojo.html.renderedTextContent(_2d[i]);
if(dojo.html.hasAttribute(_2d[i],"field")){
o.field=dojo.html.getAttribute(_2d[i],"field");
}else{
if(o.label.length>0){
o.field=o.label;
}else{
o.field="field"+i;
}
}
if(dojo.html.hasAttribute(_2d[i],"format")){
o.format=dojo.html.getAttribute(_2d[i],"format");
}
if(dojo.html.hasAttribute(_2d[i],"dataType")){
var _32=dojo.html.getAttribute(_2d[i],"dataType");
if(_32.toLowerCase()=="html"||_32.toLowerCase()=="markup"){
o.sortType="__markup__";
}else{
var _33=this.getTypeFromString(_32);
if(_33){
o.sortType=_32;
o.dataType=_33;
}
}
}
if(dojo.html.hasAttribute(_2d[i],"filterusing")){
var _30=dojo.html.getAttribute(_2d[i],"filterusing");
var f=this.getTypeFromString(_30);
if(f!=null&&f!=window&&typeof (f)=="function"){
o.filterFunction=f;
}
}
this.columns.push(o);
if(dojo.html.hasAttribute(_2d[i],"sort")){
var _34={index:i,direction:0};
var dir=dojo.html.getAttribute(_2d[i],"sort");
if(!isNaN(parseInt(dir))){
dir=parseInt(dir);
_34.direction=(dir!=0)?1:0;
}else{
_34.direction=(dir.toLowerCase()=="desc")?1:0;
}
this.sortInformation.push(_34);
}
}
if(this.sortInformation.length==0){
this.sortInformation.push({index:0,direction:0});
}else{
if(this.sortInformation.length>this.maxSortable){
this.sortInformation.length=this.maxSortable;
}
}
},parseData:function(_36){
if(_36.rows.length==0&&this.columns.length==0){
return;
}
var _37=this;
this["__selected__"]=[];
var arr=this.store.getFromHtml(this.columns,_36,function(obj,row){
if(typeof (obj[_37.valueField])=="undefined"||obj[_37.valueField]==null){
obj[_37.valueField]=dojo.html.getAttribute(row,"value");
}
if(dojo.html.getAttribute(row,"selected")=="true"){
_37["__selected__"].push(obj);
}
});
this.store.setData(arr,true);
this.render();
for(var i=0;i<this["__selected__"].length;i++){
this.select(this["__selected__"][i]);
}
this.renderSelections();
delete this["__selected__"];
this.isInitialized=true;
},onSelect:function(e){
var row=dojo.html.getParentByType(e.target,"tr");
if(dojo.html.hasAttribute(row,"emptyRow")){
return;
}
var _3e=dojo.html.getParentByType(row,"tbody");
if(this.multiple){
if(e.shiftKey){
var _3f;
var _40=_3e.rows;
for(var i=0;i<_40.length;i++){
if(_40[i]==row){
break;
}
if(this.isRowSelected(_40[i])){
_3f=_40[i];
}
}
if(!_3f){
_3f=row;
for(;i<_40.length;i++){
if(this.isRowSelected(_40[i])){
row=_40[i];
break;
}
}
}
this.resetSelections();
if(_3f==row){
this.toggleSelectionByRow(row);
}else{
var _42=false;
for(var i=0;i<_40.length;i++){
if(_40[i]==_3f){
_42=true;
}
if(_42){
this.selectByRow(_40[i]);
}
if(_40[i]==row){
_42=false;
}
}
}
}else{
this.toggleSelectionByRow(row);
}
}else{
this.resetSelections();
this.toggleSelectionByRow(row);
}
this.renderSelections();
},onSort:function(e){
var _44=this.sortIndex;
var _45=this.sortDirection;
var _46=e.target;
var row=dojo.html.getParentByType(_46,"tr");
var _48="td";
if(row.getElementsByTagName(_48).length==0){
_48="th";
}
var _49=row.getElementsByTagName(_48);
var _4a=dojo.html.getParentByType(_46,_48);
for(var i=0;i<_49.length;i++){
dojo.html.setClass(_49[i],this.headerClass);
if(_49[i]==_4a){
if(this.sortInformation[0].index!=i){
this.sortInformation.unshift({index:i,direction:0});
}else{
this.sortInformation[0]={index:i,direction:(~this.sortInformation[0].direction)&1};
}
}
}
this.sortInformation.length=Math.min(this.sortInformation.length,this.maxSortable);
for(var i=0;i<this.sortInformation.length;i++){
var idx=this.sortInformation[i].index;
var dir=(~this.sortInformation[i].direction)&1;
dojo.html.setClass(_49[idx],dir==0?this.headerDownClass:this.headerUpClass);
}
this.render();
},onFilter:function(){
},_defaultFilter:function(obj){
return true;
},setFilter:function(_4f,fn){
for(var i=0;i<this.columns.length;i++){
if(this.columns[i].getField()==_4f){
this.columns[i].filterFunction=fn;
break;
}
}
this.applyFilters();
},setFilterByIndex:function(idx,fn){
this.columns[idx].filterFunction=fn;
this.applyFilters();
},clearFilter:function(_54){
for(var i=0;i<this.columns.length;i++){
if(this.columns[i].getField()==_54){
this.columns[i].filterFunction=this._defaultFilter;
break;
}
}
this.applyFilters();
},clearFilterByIndex:function(idx){
this.columns[idx].filterFunction=this._defaultFilter;
this.applyFilters();
},clearFilters:function(){
for(var i=0;i<this.columns.length;i++){
this.columns[i].filterFunction=this._defaultFilter;
}
var _58=this.domNode.tBodies[0].rows;
for(var i=0;i<_58.length;i++){
_58[i].style.display="";
if(this.alternateRows){
dojo.html[((i%2==1)?"addClass":"removeClass")](_58[i],this.rowAlternateClass);
}
}
this.onFilter();
},applyFilters:function(){
var alt=0;
var _5a=this.domNode.tBodies[0].rows;
for(var i=0;i<_5a.length;i++){
var b=true;
var row=_5a[i];
for(var j=0;j<this.columns.length;j++){
var _5f=this.store.getField(this.getDataByRow(row),this.columns[j].getField());
if(this.columns[j].getType()==Date&&_5f!=null&&!_5f.getYear){
_5f=new Date(_5f);
}
if(!this.columns[j].filterFunction(_5f)){
b=false;
break;
}
}
row.style.display=(b?"":"none");
if(b&&this.alternateRows){
dojo.html[((alt++%2==1)?"addClass":"removeClass")](row,this.rowAlternateClass);
}
}
this.onFilter();
},createSorter:function(_60){
var _61=this;
var _62=[];
function createSortFunction(_63,dir){
var _65=_61.columns[_63];
var _66=_65.getField();
return function(_67,_68){
if(dojo.html.hasAttribute(_67,"emptyRow")){
return 1;
}
if(dojo.html.hasAttribute(_68,"emptyRow")){
return -1;
}
var a=_61.store.getField(_61.getDataByRow(_67),_66);
var b=_61.store.getField(_61.getDataByRow(_68),_66);
var ret=0;
if(a>b){
ret=1;
}
if(a<b){
ret=-1;
}
return dir*ret;
};
}
var _6c=0;
var max=Math.min(_60.length,this.maxSortable,this.columns.length);
while(_6c<max){
var _6e=(_60[_6c].direction==0)?1:-1;
_62.push(createSortFunction(_60[_6c].index,_6e));
_6c++;
}
return function(_6f,_70){
var idx=0;
while(idx<_62.length){
var ret=_62[idx++](_6f,_70);
if(ret!=0){
return ret;
}
}
return 0;
};
},createRow:function(obj){
var row=document.createElement("tr");
dojo.html.disableSelection(row);
if(obj.key!=null){
row.setAttribute("value",obj.key);
}
for(var j=0;j<this.columns.length;j++){
var _76=document.createElement("td");
_76.setAttribute("align",this.columns[j].align);
_76.setAttribute("valign",this.columns[j].valign);
dojo.html.disableSelection(_76);
var val=this.store.getField(obj.src,this.columns[j].getField());
if(typeof (val)=="undefined"){
val="";
}
this.fillCell(_76,this.columns[j],val);
row.appendChild(_76);
}
return row;
},fillCell:function(_78,_79,val){
if(_79.sortType=="__markup__"){
_78.innerHTML=val;
}else{
if(_79.getType()==Date){
val=new Date(val);
if(!isNaN(val)){
var _7b=this.defaultDateFormat;
if(_79.format){
_7b=_79.format;
}
_78.innerHTML=dojo.date.strftime(val,_7b);
}else{
_78.innerHTML=val;
}
}else{
if("Number number int Integer float Float".indexOf(_79.getType())>-1){
if(val.length==0){
val="0";
}
var n=parseFloat(val,10)+"";
if(n.indexOf(".")>-1){
n=dojo.math.round(parseFloat(val,10),2);
}
_78.innerHTML=n;
}else{
_78.innerHTML=val;
}
}
}
},prefill:function(){
this.isInitialized=false;
var _7d=this.domNode.tBodies[0];
while(_7d.childNodes.length>0){
_7d.removeChild(_7d.childNodes[0]);
}
if(this.minRows>0){
for(var i=0;i<this.minRows;i++){
var row=document.createElement("tr");
if(this.alternateRows){
dojo.html[((i%2==1)?"addClass":"removeClass")](row,this.rowAlternateClass);
}
row.setAttribute("emptyRow","true");
for(var j=0;j<this.columns.length;j++){
var _81=document.createElement("td");
_81.innerHTML="&nbsp;";
row.appendChild(_81);
}
_7d.appendChild(row);
}
}
},init:function(){
this.isInitialized=false;
var _82=this.domNode.getElementsByTagName("thead")[0];
if(_82.getElementsByTagName("tr").length==0){
var row=document.createElement("tr");
for(var i=0;i<this.columns.length;i++){
var _85=document.createElement("td");
_85.setAttribute("align",this.columns[i].align);
_85.setAttribute("valign",this.columns[i].valign);
dojo.html.disableSelection(_85);
_85.innerHTML=this.columns[i].label;
row.appendChild(_85);
if(!this.columns[i].noSort){
dojo.event.connect(_85,"onclick",this,"onSort");
}
}
dojo.html.prependChild(row,_82);
}
if(this.store.get().length==0){
return false;
}
var idx=this.domNode.tBodies[0].rows.length;
if(!idx||idx==0||this.domNode.tBodies[0].rows[0].getAttribute("emptyRow")=="true"){
idx=0;
var _87=this.domNode.tBodies[0];
while(_87.childNodes.length>0){
_87.removeChild(_87.childNodes[0]);
}
var _88=this.store.get();
for(var i=0;i<_88.length;i++){
var row=this.createRow(_88[i]);
_87.appendChild(row);
idx++;
}
}
if(this.minRows>0&&idx<this.minRows){
idx=this.minRows-idx;
for(var i=0;i<idx;i++){
row=document.createElement("tr");
row.setAttribute("emptyRow","true");
for(var j=0;j<this.columns.length;j++){
_85=document.createElement("td");
_85.innerHTML="&nbsp;";
row.appendChild(_85);
}
_87.appendChild(row);
}
}
var row=this.domNode.getElementsByTagName("thead")[0].rows[0];
var _8a="td";
if(row.getElementsByTagName(_8a).length==0){
_8a="th";
}
var _8b=row.getElementsByTagName(_8a);
for(var i=0;i<_8b.length;i++){
dojo.html.setClass(_8b[i],this.headerClass);
}
for(var i=0;i<this.sortInformation.length;i++){
var idx=this.sortInformation[i].index;
var dir=(~this.sortInformation[i].direction)&1;
dojo.html.setClass(_8b[idx],dir==0?this.headerDownClass:this.headerUpClass);
}
this.isInitialized=true;
return this.isInitialized;
},render:function(){
if(!this.isInitialized){
var b=this.init();
if(!b){
this.prefill();
return;
}
}
var _8e=[];
var _8f=this.domNode.tBodies[0];
var _90=-1;
for(var i=0;i<_8f.rows.length;i++){
_8e.push(_8f.rows[i]);
}
var _92=this.createSorter(this.sortInformation);
if(_92){
_8e.sort(_92);
}
for(var i=0;i<_8e.length;i++){
if(this.alternateRows){
dojo.html[((i%2==1)?"addClass":"removeClass")](_8e[i],this.rowAlternateClass);
}
dojo.html[(this.isRowSelected(_8f.rows[i])?"addClass":"removeClass")](_8f.rows[i],this.rowSelectedClass);
_8f.appendChild(_8e[i]);
}
},renderSelections:function(){
var _93=this.domNode.tBodies[0];
for(var i=0;i<_93.rows.length;i++){
dojo.html[(this.isRowSelected(_93.rows[i])?"addClass":"removeClass")](_93.rows[i],this.rowSelectedClass);
}
},initialize:function(){
var _95=this;
dojo.event.connect(this.store,"onSetData",function(){
_95.store.forEach(function(_96){
_96.isSelected=false;
});
_95.isInitialized=false;
var _97=_95.domNode.tBodies[0];
if(_97){
while(_97.childNodes.length>0){
_97.removeChild(_97.childNodes[0]);
}
}
_95.render();
});
dojo.event.connect(this.store,"onClearData",function(){
_95.isInitialized=false;
_95.render();
});
dojo.event.connect(this.store,"onAddData",function(_98){
var row=_95.createRow(_98);
_95.domNode.tBodies[0].appendChild(row);
_95.render();
});
dojo.event.connect(this.store,"onAddDataRange",function(arr){
for(var i=0;i<arr.length;i++){
arr[i].isSelected=false;
var row=_95.createRow(arr[i]);
_95.domNode.tBodies[0].appendChild(row);
}
_95.render();
});
dojo.event.connect(this.store,"onRemoveData",function(_9d){
var _9e=_95.domNode.tBodies[0].rows;
for(var i=0;i<_9e.length;i++){
if(_95.getDataByRow(_9e[i])==_9d.src){
_9e[i].parentNode.removeChild(_9e[i]);
break;
}
}
_95.render();
});
dojo.event.connect(this.store,"onUpdateField",function(obj,_a1,val){
var row=_95.getRow(obj);
var idx=_95.getColumnIndex(_a1);
if(row&&row.cells[idx]&&_95.columns[idx]){
_95.fillCell(row.cells[idx],_95.columns[idx],val);
}
});
},postCreate:function(){
this.store.keyField=this.valueField;
if(this.domNode){
if(this.domNode.nodeName.toLowerCase()!="table"){
}
if(this.domNode.getElementsByTagName("thead")[0]){
var _a5=this.domNode.getElementsByTagName("thead")[0];
if(this.headClass.length>0){
_a5.className=this.headClass;
}
dojo.html.disableSelection(this.domNode);
this.parseMetadata(_a5);
var _a6="td";
if(_a5.getElementsByTagName(_a6).length==0){
_a6="th";
}
var _a7=_a5.getElementsByTagName(_a6);
for(var i=0;i<_a7.length;i++){
if(!this.columns[i].noSort){
dojo.event.connect(_a7[i],"onclick",this,"onSort");
}
}
}else{
this.domNode.appendChild(document.createElement("thead"));
}
if(this.domNode.tBodies.length<1){
var _a9=document.createElement("tbody");
this.domNode.appendChild(_a9);
}else{
var _a9=this.domNode.tBodies[0];
}
if(this.tbodyClass.length>0){
_a9.className=this.tbodyClass;
}
dojo.event.connect(_a9,"onclick",this,"onSelect");
this.parseData(_a9);
}
}});
