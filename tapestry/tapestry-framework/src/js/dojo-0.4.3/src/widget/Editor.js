dojo.provide("dojo.widget.Editor");
dojo.deprecated("dojo.widget.Editor","is replaced by dojo.widget.Editor2","0.5");
dojo.require("dojo.io.*");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.Toolbar");
dojo.require("dojo.widget.RichText");
dojo.require("dojo.widget.ColorPalette");
dojo.require("dojo.string.extras");
dojo.widget.tags.addParseTreeHandler("dojo:Editor");
dojo.widget.Editor=function(){
dojo.widget.HtmlWidget.call(this);
this.contentFilters=[];
this._toolbars=[];
};
dojo.inherits(dojo.widget.Editor,dojo.widget.HtmlWidget);
dojo.widget.Editor.itemGroups={textGroup:["bold","italic","underline","strikethrough"],blockGroup:["formatBlock","fontName","fontSize"],justifyGroup:["justifyleft","justifycenter","justifyright"],commandGroup:["save","cancel"],colorGroup:["forecolor","hilitecolor"],listGroup:["insertorderedlist","insertunorderedlist"],indentGroup:["outdent","indent"],linkGroup:["createlink","insertimage","inserthorizontalrule"]};
dojo.widget.Editor.formatBlockValues={"Normal":"p","Main heading":"h2","Sub heading":"h3","Sub sub heading":"h4","Preformatted":"pre"};
dojo.widget.Editor.fontNameValues={"Arial":"Arial, Helvetica, sans-serif","Verdana":"Verdana, sans-serif","Times New Roman":"Times New Roman, serif","Courier":"Courier New, monospace"};
dojo.widget.Editor.fontSizeValues={"1 (8 pt)":"1","2 (10 pt)":"2","3 (12 pt)":"3","4 (14 pt)":"4","5 (18 pt)":"5","6 (24 pt)":"6","7 (36 pt)":"7"};
dojo.widget.Editor.defaultItems=["commandGroup","|","blockGroup","|","textGroup","|","colorGroup","|","justifyGroup","|","listGroup","indentGroup","|","linkGroup"];
dojo.widget.Editor.supportedCommands=["save","cancel","|","-","/"," "];
dojo.lang.extend(dojo.widget.Editor,{widgetType:"Editor",saveUrl:"",saveMethod:"post",saveArgName:"editorContent",closeOnSave:false,items:dojo.widget.Editor.defaultItems,formatBlockItems:dojo.lang.shallowCopy(dojo.widget.Editor.formatBlockValues),fontNameItems:dojo.lang.shallowCopy(dojo.widget.Editor.fontNameValues),fontSizeItems:dojo.lang.shallowCopy(dojo.widget.Editor.fontSizeValues),getItemProperties:function(_1){
var _2={};
switch(_1.toLowerCase()){
case "bold":
case "italic":
case "underline":
case "strikethrough":
_2.toggleItem=true;
break;
case "justifygroup":
_2.defaultButton="justifyleft";
_2.preventDeselect=true;
_2.buttonGroup=true;
break;
case "listgroup":
_2.buttonGroup=true;
break;
case "save":
case "cancel":
_2.label=dojo.string.capitalize(_1);
break;
case "forecolor":
case "hilitecolor":
_2.name=_1;
_2.toggleItem=true;
_2.icon=this.getCommandImage(_1);
break;
case "formatblock":
_2.name="formatBlock";
_2.values=this.formatBlockItems;
break;
case "fontname":
_2.name="fontName";
_2.values=this.fontNameItems;
case "fontsize":
_2.name="fontSize";
_2.values=this.fontSizeItems;
}
return _2;
},validateItems:true,focusOnLoad:true,minHeight:"1em",_richText:null,_richTextType:"RichText",_toolbarContainer:null,_toolbarContainerType:"ToolbarContainer",_toolbars:[],_toolbarType:"Toolbar",_toolbarItemType:"ToolbarItem",buildRendering:function(_3,_4){
var _5=_4["dojo:"+this.widgetType.toLowerCase()]["nodeRef"];
var _6=dojo.widget.createWidget(this._richTextType,{focusOnLoad:this.focusOnLoad,minHeight:this.minHeight},_5);
var _7=this;
setTimeout(function(){
_7.setRichText(_6);
_7.initToolbar();
_7.fillInTemplate(_3,_4);
},0);
},setRichText:function(_8){
if(this._richText&&this._richText==_8){
dojo.debug("Already set the richText to this richText!");
return;
}
if(this._richText&&!this._richText.isClosed){
dojo.debug("You are switching richTexts yet you haven't closed the current one. Losing reference!");
}
this._richText=_8;
dojo.event.connect(this._richText,"close",this,"onClose");
dojo.event.connect(this._richText,"onLoad",this,"onLoad");
dojo.event.connect(this._richText,"onDisplayChanged",this,"updateToolbar");
if(this._toolbarContainer){
this._toolbarContainer.enable();
this.updateToolbar(true);
}
},initToolbar:function(){
if(this._toolbarContainer){
return;
}
this._toolbarContainer=dojo.widget.createWidget(this._toolbarContainerType);
var tb=this.addToolbar();
var _a=true;
for(var i=0;i<this.items.length;i++){
if(this.items[i]=="\n"){
tb=this.addToolbar();
}else{
if((this.items[i]=="|")&&(!_a)){
_a=true;
}else{
_a=this.addItem(this.items[i],tb);
}
}
}
this.insertToolbar(this._toolbarContainer.domNode,this._richText.domNode);
},insertToolbar:function(_c,_d){
dojo.html.insertBefore(_c,_d);
},addToolbar:function(_e){
this.initToolbar();
if(!(_e instanceof dojo.widget.Toolbar)){
_e=dojo.widget.createWidget(this._toolbarType);
}
this._toolbarContainer.addChild(_e);
this._toolbars.push(_e);
return _e;
},addItem:function(_f,tb,_11){
if(!tb){
tb=this._toolbars[0];
}
var cmd=((_f)&&(!dojo.lang.isUndefined(_f["getValue"])))?cmd=_f["getValue"]():_f;
var _13=dojo.widget.Editor.itemGroups;
if(_f instanceof dojo.widget.ToolbarItem){
tb.addChild(_f);
}else{
if(_13[cmd]){
var _14=_13[cmd];
var _15=true;
if(cmd=="justifyGroup"||cmd=="listGroup"){
var _16=[cmd];
for(var i=0;i<_14.length;i++){
if(_11||this.isSupportedCommand(_14[i])){
_16.push(this.getCommandImage(_14[i]));
}else{
_15=false;
}
}
if(_16.length){
var btn=tb.addChild(_16,null,this.getItemProperties(cmd));
dojo.event.connect(btn,"onClick",this,"_action");
dojo.event.connect(btn,"onChangeSelect",this,"_action");
}
return _15;
}else{
for(var i=0;i<_14.length;i++){
if(!this.addItem(_14[i],tb)){
_15=false;
}
}
return _15;
}
}else{
if((!_11)&&(!this.isSupportedCommand(cmd))){
return false;
}
if(_11||this.isSupportedCommand(cmd)){
cmd=cmd.toLowerCase();
if(cmd=="formatblock"){
var _19=dojo.widget.createWidget("ToolbarSelect",{name:"formatBlock",values:this.formatBlockItems});
tb.addChild(_19);
var _1a=this;
dojo.event.connect(_19,"onSetValue",function(_1b,_1c){
_1a.onAction("formatBlock",_1c);
});
}else{
if(cmd=="fontname"){
var _19=dojo.widget.createWidget("ToolbarSelect",{name:"fontName",values:this.fontNameItems});
tb.addChild(_19);
dojo.event.connect(_19,"onSetValue",dojo.lang.hitch(this,function(_1d,_1e){
this.onAction("fontName",_1e);
}));
}else{
if(cmd=="fontsize"){
var _19=dojo.widget.createWidget("ToolbarSelect",{name:"fontSize",values:this.fontSizeItems});
tb.addChild(_19);
dojo.event.connect(_19,"onSetValue",dojo.lang.hitch(this,function(_1f,_20){
this.onAction("fontSize",_20);
}));
}else{
if(dojo.lang.inArray(cmd,["forecolor","hilitecolor"])){
var btn=tb.addChild(dojo.widget.createWidget("ToolbarColorDialog",this.getItemProperties(cmd)));
dojo.event.connect(btn,"onSetValue",this,"_setValue");
}else{
var btn=tb.addChild(this.getCommandImage(cmd),null,this.getItemProperties(cmd));
if(cmd=="save"){
dojo.event.connect(btn,"onClick",this,"_save");
}else{
if(cmd=="cancel"){
dojo.event.connect(btn,"onClick",this,"_close");
}else{
dojo.event.connect(btn,"onClick",this,"_action");
dojo.event.connect(btn,"onChangeSelect",this,"_action");
}
}
}
}
}
}
}
}
}
return true;
},enableToolbar:function(){
if(this._toolbarContainer){
this._toolbarContainer.domNode.style.display="";
this._toolbarContainer.enable();
}
},disableToolbar:function(_21){
if(_21){
if(this._toolbarContainer){
this._toolbarContainer.domNode.style.display="none";
}
}else{
if(this._toolbarContainer){
this._toolbarContainer.disable();
}
}
},_updateToolbarLastRan:null,_updateToolbarTimer:null,_updateToolbarFrequency:500,updateToolbar:function(_22){
if(!this._toolbarContainer){
return;
}
var _23=new Date()-this._updateToolbarLastRan;
if(!_22&&this._updateToolbarLastRan&&(_23<this._updateToolbarFrequency)){
clearTimeout(this._updateToolbarTimer);
var _24=this;
this._updateToolbarTimer=setTimeout(function(){
_24.updateToolbar();
},this._updateToolbarFrequency/2);
return;
}else{
this._updateToolbarLastRan=new Date();
}
var _25=this._toolbarContainer.getItems();
for(var i=0;i<_25.length;i++){
var _27=_25[i];
if(_27 instanceof dojo.widget.ToolbarSeparator){
continue;
}
var cmd=_27._name;
if(cmd=="save"||cmd=="cancel"){
continue;
}else{
if(cmd=="justifyGroup"){
try{
if(!this._richText.queryCommandEnabled("justifyleft")){
_27.disable(false,true);
}else{
_27.enable(false,true);
var _29=_27.getItems();
for(var j=0;j<_29.length;j++){
var _2b=_29[j]._name;
var _2c=this._richText.queryCommandValue(_2b);
if(typeof _2c=="boolean"&&_2c){
_2c=_2b;
break;
}else{
if(typeof _2c=="string"){
_2c="justify"+_2c;
}else{
_2c=null;
}
}
}
if(!_2c){
_2c="justifyleft";
}
_27.setValue(_2c,false,true);
}
}
catch(err){
}
}else{
if(cmd=="listGroup"){
var _2d=_27.getItems();
for(var j=0;j<_2d.length;j++){
this.updateItem(_2d[j]);
}
}else{
this.updateItem(_27);
}
}
}
}
},updateItem:function(_2e){
try{
var cmd=_2e._name;
var _30=this._richText.queryCommandEnabled(cmd);
_2e.setEnabled(_30,false,true);
var _31=this._richText.queryCommandState(cmd);
if(_31&&cmd=="underline"){
_31=!this._richText.queryCommandEnabled("unlink");
}
_2e.setSelected(_31,false,true);
return true;
}
catch(err){
return false;
}
},supportedCommands:dojo.widget.Editor.supportedCommands.concat(),isSupportedCommand:function(cmd){
var yes=dojo.lang.inArray(cmd,this.supportedCommands);
if(!yes){
try{
var _34=this._richText||dojo.widget.HtmlRichText.prototype;
yes=_34.queryCommandAvailable(cmd);
}
catch(E){
}
}
return yes;
},getCommandImage:function(cmd){
if(cmd=="|"){
return cmd;
}else{
return dojo.uri.moduleUri("dojo.widget","templates/buttons/"+cmd+".gif");
}
},_action:function(e){
this._fire("onAction",e.getValue());
},_setValue:function(a,b){
this._fire("onAction",a.getValue(),b);
},_save:function(e){
if(!this._richText.isClosed){
if(this.saveUrl.length){
var _3a={};
_3a[this.saveArgName]=this.getHtml();
dojo.io.bind({method:this.saveMethod,url:this.saveUrl,content:_3a});
}else{
dojo.debug("please set a saveUrl for the editor");
}
if(this.closeOnSave){
this._richText.close(e.getName().toLowerCase()=="save");
}
}
},_close:function(e){
if(!this._richText.isClosed){
this._richText.close(e.getName().toLowerCase()=="save");
}
},onAction:function(cmd,_3d){
switch(cmd){
case "createlink":
if(!(_3d=prompt("Please enter the URL of the link:","http://"))){
return;
}
break;
case "insertimage":
if(!(_3d=prompt("Please enter the URL of the image:","http://"))){
return;
}
break;
}
this._richText.execCommand(cmd,_3d);
},fillInTemplate:function(_3e,_3f){
},_fire:function(_40){
if(dojo.lang.isFunction(this[_40])){
var _41=[];
if(arguments.length==1){
_41.push(this);
}else{
for(var i=1;i<arguments.length;i++){
_41.push(arguments[i]);
}
}
this[_40].apply(this,_41);
}
},getHtml:function(){
this._richText.contentFilters=this._richText.contentFilters.concat(this.contentFilters);
return this._richText.getEditorContent();
},getEditorContent:function(){
return this.getHtml();
},onClose:function(_43,_44){
this.disableToolbar(_44);
if(_43){
this._fire("onSave");
}else{
this._fire("onCancel");
}
},onLoad:function(){
},onSave:function(){
},onCancel:function(){
}});
