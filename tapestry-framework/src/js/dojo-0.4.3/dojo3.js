/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("layer.widget");
dojo.provide("dojo.namespaces.dojo");
dojo.require("dojo.ns");
(function(){
var _1={html:{"accordioncontainer":"dojo.widget.AccordionContainer","animatedpng":"dojo.widget.AnimatedPng","button":"dojo.widget.Button","chart":"dojo.widget.Chart","checkbox":"dojo.widget.Checkbox","clock":"dojo.widget.Clock","colorpalette":"dojo.widget.ColorPalette","combobox":"dojo.widget.ComboBox","combobutton":"dojo.widget.Button","contentpane":"dojo.widget.ContentPane","currencytextbox":"dojo.widget.CurrencyTextbox","datepicker":"dojo.widget.DatePicker","datetextbox":"dojo.widget.DateTextbox","debugconsole":"dojo.widget.DebugConsole","dialog":"dojo.widget.Dialog","dropdownbutton":"dojo.widget.Button","dropdowndatepicker":"dojo.widget.DropdownDatePicker","dropdowntimepicker":"dojo.widget.DropdownTimePicker","emaillisttextbox":"dojo.widget.InternetTextbox","emailtextbox":"dojo.widget.InternetTextbox","editor":"dojo.widget.Editor","editor2":"dojo.widget.Editor2","filteringtable":"dojo.widget.FilteringTable","fisheyelist":"dojo.widget.FisheyeList","fisheyelistitem":"dojo.widget.FisheyeList","floatingpane":"dojo.widget.FloatingPane","modalfloatingpane":"dojo.widget.FloatingPane","form":"dojo.widget.Form","googlemap":"dojo.widget.GoogleMap","inlineeditbox":"dojo.widget.InlineEditBox","integerspinner":"dojo.widget.Spinner","integertextbox":"dojo.widget.IntegerTextbox","ipaddresstextbox":"dojo.widget.InternetTextbox","layoutcontainer":"dojo.widget.LayoutContainer","linkpane":"dojo.widget.LinkPane","popupmenu2":"dojo.widget.Menu2","menuitem2":"dojo.widget.Menu2","menuseparator2":"dojo.widget.Menu2","menubar2":"dojo.widget.Menu2","menubaritem2":"dojo.widget.Menu2","pagecontainer":"dojo.widget.PageContainer","pagecontroller":"dojo.widget.PageContainer","popupcontainer":"dojo.widget.PopupContainer","progressbar":"dojo.widget.ProgressBar","radiogroup":"dojo.widget.RadioGroup","realnumbertextbox":"dojo.widget.RealNumberTextbox","regexptextbox":"dojo.widget.RegexpTextbox","repeater":"dojo.widget.Repeater","resizabletextarea":"dojo.widget.ResizableTextarea","richtext":"dojo.widget.RichText","select":"dojo.widget.Select","show":"dojo.widget.Show","showaction":"dojo.widget.ShowAction","showslide":"dojo.widget.ShowSlide","slidervertical":"dojo.widget.Slider","sliderhorizontal":"dojo.widget.Slider","slider":"dojo.widget.Slider","slideshow":"dojo.widget.SlideShow","sortabletable":"dojo.widget.SortableTable","splitcontainer":"dojo.widget.SplitContainer","tabcontainer":"dojo.widget.TabContainer","tabcontroller":"dojo.widget.TabContainer","taskbar":"dojo.widget.TaskBar","textbox":"dojo.widget.Textbox","timepicker":"dojo.widget.TimePicker","timetextbox":"dojo.widget.DateTextbox","titlepane":"dojo.widget.TitlePane","toaster":"dojo.widget.Toaster","toggler":"dojo.widget.Toggler","toolbar":"dojo.widget.Toolbar","toolbarcontainer":"dojo.widget.Toolbar","toolbaritem":"dojo.widget.Toolbar","toolbarbuttongroup":"dojo.widget.Toolbar","toolbarbutton":"dojo.widget.Toolbar","toolbardialog":"dojo.widget.Toolbar","toolbarmenu":"dojo.widget.Toolbar","toolbarseparator":"dojo.widget.Toolbar","toolbarspace":"dojo.widget.Toolbar","toolbarselect":"dojo.widget.Toolbar","toolbarcolordialog":"dojo.widget.Toolbar","tooltip":"dojo.widget.Tooltip","tree":"dojo.widget.Tree","treebasiccontroller":"dojo.widget.TreeBasicController","treecontextmenu":"dojo.widget.TreeContextMenu","treedisablewrapextension":"dojo.widget.TreeDisableWrapExtension","treedociconextension":"dojo.widget.TreeDocIconExtension","treeeditor":"dojo.widget.TreeEditor","treeemphasizeonselect":"dojo.widget.TreeEmphasizeOnSelect","treeexpandtonodeonselect":"dojo.widget.TreeExpandToNodeOnSelect","treelinkextension":"dojo.widget.TreeLinkExtension","treeloadingcontroller":"dojo.widget.TreeLoadingController","treemenuitem":"dojo.widget.TreeContextMenu","treenode":"dojo.widget.TreeNode","treerpccontroller":"dojo.widget.TreeRPCController","treeselector":"dojo.widget.TreeSelector","treetoggleonselect":"dojo.widget.TreeToggleOnSelect","treev3":"dojo.widget.TreeV3","treebasiccontrollerv3":"dojo.widget.TreeBasicControllerV3","treecontextmenuv3":"dojo.widget.TreeContextMenuV3","treedndcontrollerv3":"dojo.widget.TreeDndControllerV3","treeloadingcontrollerv3":"dojo.widget.TreeLoadingControllerV3","treemenuitemv3":"dojo.widget.TreeContextMenuV3","treerpccontrollerv3":"dojo.widget.TreeRpcControllerV3","treeselectorv3":"dojo.widget.TreeSelectorV3","urltextbox":"dojo.widget.InternetTextbox","usphonenumbertextbox":"dojo.widget.UsTextbox","ussocialsecuritynumbertextbox":"dojo.widget.UsTextbox","usstatetextbox":"dojo.widget.UsTextbox","usziptextbox":"dojo.widget.UsTextbox","validationtextbox":"dojo.widget.ValidationTextbox","treeloadingcontroller":"dojo.widget.TreeLoadingController","wizardcontainer":"dojo.widget.Wizard","wizardpane":"dojo.widget.Wizard","yahoomap":"dojo.widget.YahooMap"},svg:{"chart":"dojo.widget.svg.Chart"},vml:{"chart":"dojo.widget.vml.Chart"}};
dojo.addDojoNamespaceMapping=function(_2,_3){
_1[_2]=_3;
};
function dojoNamespaceResolver(_4,_5){
if(!_5){
_5="html";
}
if(!_1[_5]){
return null;
}
return _1[_5][_4];
}
dojo.registerNamespaceResolver("dojo",dojoNamespaceResolver);
})();
dojo.provide("dojo.xml.Parse");
dojo.require("dojo.dom");
dojo.xml.Parse=function(){
var _6=((dojo.render.html.capable)&&(dojo.render.html.ie));
function getTagName(_7){
try{
return _7.tagName.toLowerCase();
}
catch(e){
return "";
}
}
function getDojoTagName(_8){
var _9=getTagName(_8);
if(!_9){
return "";
}
if((dojo.widget)&&(dojo.widget.tags[_9])){
return _9;
}
var p=_9.indexOf(":");
if(p>=0){
return _9;
}
if(_9.substr(0,5)=="dojo:"){
return _9;
}
if(dojo.render.html.capable&&dojo.render.html.ie&&_8.scopeName!="HTML"){
return _8.scopeName.toLowerCase()+":"+_9;
}
if(_9.substr(0,4)=="dojo"){
return "dojo:"+_9.substring(4);
}
var _b=_8.getAttribute("dojoType")||_8.getAttribute("dojotype");
if(_b){
if(_b.indexOf(":")<0){
_b="dojo:"+_b;
}
return _b.toLowerCase();
}
_b=_8.getAttributeNS&&_8.getAttributeNS(dojo.dom.dojoml,"type");
if(_b){
return "dojo:"+_b.toLowerCase();
}
try{
_b=_8.getAttribute("dojo:type");
}
catch(e){
}
if(_b){
return "dojo:"+_b.toLowerCase();
}
if((dj_global["djConfig"])&&(!djConfig["ignoreClassNames"])){
var _c=_8.className||_8.getAttribute("class");
if((_c)&&(_c.indexOf)&&(_c.indexOf("dojo-")!=-1)){
var _d=_c.split(" ");
for(var x=0,c=_d.length;x<c;x++){
if(_d[x].slice(0,5)=="dojo-"){
return "dojo:"+_d[x].substr(5).toLowerCase();
}
}
}
}
return "";
}
this.parseElement=function(_10,_11,_12,_13){
var _14=getTagName(_10);
if(_6&&_14.indexOf("/")==0){
return null;
}
try{
var _15=_10.getAttribute("parseWidgets");
if(_15&&_15.toLowerCase()=="false"){
return {};
}
}
catch(e){
}
var _16=true;
if(_12){
var _17=getDojoTagName(_10);
_14=_17||_14;
_16=Boolean(_17);
}
var _18={};
_18[_14]=[];
var pos=_14.indexOf(":");
if(pos>0){
var ns=_14.substring(0,pos);
_18["ns"]=ns;
if((dojo.ns)&&(!dojo.ns.allow(ns))){
_16=false;
}
}
if(_16){
var _1b=this.parseAttributes(_10);
for(var _15 in _1b){
if((!_18[_14][_15])||(typeof _18[_14][_15]!="array")){
_18[_14][_15]=[];
}
_18[_14][_15].push(_1b[_15]);
}
_18[_14].nodeRef=_10;
_18.tagName=_14;
_18.index=_13||0;
}
var _1c=0;
for(var i=0;i<_10.childNodes.length;i++){
var tcn=_10.childNodes.item(i);
switch(tcn.nodeType){
case dojo.dom.ELEMENT_NODE:
var ctn=getDojoTagName(tcn)||getTagName(tcn);
if(!_18[ctn]){
_18[ctn]=[];
}
_18[ctn].push(this.parseElement(tcn,true,_12,_1c));
if((tcn.childNodes.length==1)&&(tcn.childNodes.item(0).nodeType==dojo.dom.TEXT_NODE)){
_18[ctn][_18[ctn].length-1].value=tcn.childNodes.item(0).nodeValue;
}
_1c++;
break;
case dojo.dom.TEXT_NODE:
if(_10.childNodes.length==1){
_18[_14].push({value:_10.childNodes.item(0).nodeValue});
}
break;
default:
break;
}
}
return _18;
};
this.parseAttributes=function(_20){
var _21={};
var _22=_20.attributes;
var _23,i=0;
while((_23=_22[i++])){
if(_6){
if(!_23){
continue;
}
if((typeof _23=="object")&&(typeof _23.nodeValue=="undefined")||(_23.nodeValue==null)||(_23.nodeValue=="")){
continue;
}
}
var nn=_23.nodeName.split(":");
nn=(nn.length==2)?nn[1]:_23.nodeName;
_21[nn]={value:_23.nodeValue};
}
return _21;
};
};
dojo.provide("dojo.widget.Widget");
dojo.require("dojo.lang.func");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.extras");
dojo.require("dojo.lang.declare");
dojo.require("dojo.ns");
dojo.require("dojo.widget.Manager");
dojo.require("dojo.event.*");
dojo.declare("dojo.widget.Widget",null,function(){
this.children=[];
this.extraArgs={};
},{parent:null,isTopLevel:false,disabled:false,isContainer:false,widgetId:"",widgetType:"Widget",ns:"dojo",getNamespacedType:function(){
return (this.ns?this.ns+":"+this.widgetType:this.widgetType).toLowerCase();
},toString:function(){
return "[Widget "+this.getNamespacedType()+", "+(this.widgetId||"NO ID")+"]";
},repr:function(){
return this.toString();
},enable:function(){
this.disabled=false;
},disable:function(){
this.disabled=true;
},onResized:function(){
this.notifyChildrenOfResize();
},notifyChildrenOfResize:function(){
for(var i=0;i<this.children.length;i++){
var _27=this.children[i];
if(_27.onResized){
_27.onResized();
}
}
},create:function(_28,_29,_2a,ns){
if(ns){
this.ns=ns;
}
this.satisfyPropertySets(_28,_29,_2a);
this.mixInProperties(_28,_29,_2a);
this.postMixInProperties(_28,_29,_2a);
dojo.widget.manager.add(this);
this.buildRendering(_28,_29,_2a);
this.initialize(_28,_29,_2a);
this.postInitialize(_28,_29,_2a);
this.postCreate(_28,_29,_2a);
return this;
},destroy:function(_2c){
if(this.parent){
this.parent.removeChild(this);
}
this.destroyChildren();
this.uninitialize();
this.destroyRendering(_2c);
dojo.widget.manager.removeById(this.widgetId);
},destroyChildren:function(){
var _2d;
var i=0;
while(this.children.length>i){
_2d=this.children[i];
if(_2d instanceof dojo.widget.Widget){
this.removeChild(_2d);
_2d.destroy();
continue;
}
i++;
}
},getChildrenOfType:function(_2f,_30){
var ret=[];
var _32=dojo.lang.isFunction(_2f);
if(!_32){
_2f=_2f.toLowerCase();
}
for(var x=0;x<this.children.length;x++){
if(_32){
if(this.children[x] instanceof _2f){
ret.push(this.children[x]);
}
}else{
if(this.children[x].widgetType.toLowerCase()==_2f){
ret.push(this.children[x]);
}
}
if(_30){
ret=ret.concat(this.children[x].getChildrenOfType(_2f,_30));
}
}
return ret;
},getDescendants:function(){
var _34=[];
var _35=[this];
var _36;
while((_36=_35.pop())){
_34.push(_36);
if(_36.children){
dojo.lang.forEach(_36.children,function(_37){
_35.push(_37);
});
}
}
return _34;
},isFirstChild:function(){
return this===this.parent.children[0];
},isLastChild:function(){
return this===this.parent.children[this.parent.children.length-1];
},satisfyPropertySets:function(_38){
return _38;
},mixInProperties:function(_39,_3a){
if((_39["fastMixIn"])||(_3a["fastMixIn"])){
for(var x in _39){
this[x]=_39[x];
}
return;
}
var _3c;
var _3d=dojo.widget.lcArgsCache[this.widgetType];
if(_3d==null){
_3d={};
for(var y in this){
_3d[((new String(y)).toLowerCase())]=y;
}
dojo.widget.lcArgsCache[this.widgetType]=_3d;
}
var _3f={};
for(var x in _39){
if(!this[x]){
var y=_3d[(new String(x)).toLowerCase()];
if(y){
_39[y]=_39[x];
x=y;
}
}
if(_3f[x]){
continue;
}
_3f[x]=true;
if((typeof this[x])!=(typeof _3c)){
if(typeof _39[x]!="string"){
this[x]=_39[x];
}else{
if(dojo.lang.isString(this[x])){
this[x]=_39[x];
}else{
if(dojo.lang.isNumber(this[x])){
this[x]=new Number(_39[x]);
}else{
if(dojo.lang.isBoolean(this[x])){
this[x]=(_39[x].toLowerCase()=="false")?false:true;
}else{
if(dojo.lang.isFunction(this[x])){
if(_39[x].search(/[^\w\.]+/i)==-1){
this[x]=dojo.evalObjPath(_39[x],false);
}else{
var tn=dojo.lang.nameAnonFunc(new Function(_39[x]),this);
dojo.event.kwConnect({srcObj:this,srcFunc:x,adviceObj:this,adviceFunc:tn});
}
}else{
if(dojo.lang.isArray(this[x])){
this[x]=_39[x].split(";");
}else{
if(this[x] instanceof Date){
this[x]=new Date(Number(_39[x]));
}else{
if(typeof this[x]=="object"){
if(this[x] instanceof dojo.uri.Uri){
this[x]=dojo.uri.dojoUri(_39[x]);
}else{
var _41=_39[x].split(";");
for(var y=0;y<_41.length;y++){
var si=_41[y].indexOf(":");
if((si!=-1)&&(_41[y].length>si)){
this[x][_41[y].substr(0,si).replace(/^\s+|\s+$/g,"")]=_41[y].substr(si+1);
}
}
}
}else{
this[x]=_39[x];
}
}
}
}
}
}
}
}
}else{
this.extraArgs[x.toLowerCase()]=_39[x];
}
}
},postMixInProperties:function(_43,_44,_45){
},initialize:function(_46,_47,_48){
return false;
},postInitialize:function(_49,_4a,_4b){
return false;
},postCreate:function(_4c,_4d,_4e){
return false;
},uninitialize:function(){
return false;
},buildRendering:function(_4f,_50,_51){
dojo.unimplemented("dojo.widget.Widget.buildRendering, on "+this.toString()+", ");
return false;
},destroyRendering:function(){
dojo.unimplemented("dojo.widget.Widget.destroyRendering");
return false;
},addedTo:function(_52){
},addChild:function(_53){
dojo.unimplemented("dojo.widget.Widget.addChild");
return false;
},removeChild:function(_54){
for(var x=0;x<this.children.length;x++){
if(this.children[x]===_54){
this.children.splice(x,1);
_54.parent=null;
break;
}
}
return _54;
},getPreviousSibling:function(){
var idx=this.getParentIndex();
if(idx<=0){
return null;
}
return this.parent.children[idx-1];
},getSiblings:function(){
return this.parent.children;
},getParentIndex:function(){
return dojo.lang.indexOf(this.parent.children,this,true);
},getNextSibling:function(){
var idx=this.getParentIndex();
if(idx==this.parent.children.length-1){
return null;
}
if(idx<0){
return null;
}
return this.parent.children[idx+1];
}});
dojo.widget.lcArgsCache={};
dojo.widget.tags={};
dojo.widget.tags.addParseTreeHandler=function(_58){
dojo.deprecated("addParseTreeHandler",". ParseTreeHandlers are now reserved for components. Any unfiltered DojoML tag without a ParseTreeHandler is assumed to be a widget","0.5");
};
dojo.widget.tags["dojo:propertyset"]=function(_59,_5a,_5b){
var _5c=_5a.parseProperties(_59["dojo:propertyset"]);
};
dojo.widget.tags["dojo:connect"]=function(_5d,_5e,_5f){
var _60=_5e.parseProperties(_5d["dojo:connect"]);
};
dojo.widget.buildWidgetFromParseTree=function(_61,_62,_63,_64,_65,_66){
var _67=_61.split(":");
_67=(_67.length==2)?_67[1]:_61;
var _68=_66||_63.parseProperties(_62[_62["ns"]+":"+_67]);
var _69=dojo.widget.manager.getImplementation(_67,null,null,_62["ns"]);
if(!_69){
throw new Error("cannot find \""+_61+"\" widget");
}else{
if(!_69.create){
throw new Error("\""+_61+"\" widget object has no \"create\" method and does not appear to implement *Widget");
}
}
_68["dojoinsertionindex"]=_65;
var ret=_69.create(_68,_62,_64,_62["ns"]);
return ret;
};
dojo.widget.defineWidget=function(_6b,_6c,_6d,_6e,_6f){
if(dojo.lang.isString(arguments[3])){
dojo.widget._defineWidget(arguments[0],arguments[3],arguments[1],arguments[4],arguments[2]);
}else{
var _70=[arguments[0]],p=3;
if(dojo.lang.isString(arguments[1])){
_70.push(arguments[1],arguments[2]);
}else{
_70.push("",arguments[1]);
p=2;
}
if(dojo.lang.isFunction(arguments[p])){
_70.push(arguments[p],arguments[p+1]);
}else{
_70.push(null,arguments[p]);
}
dojo.widget._defineWidget.apply(this,_70);
}
};
dojo.widget.defineWidget.renderers="html|svg|vml";
dojo.widget._defineWidget=function(_72,_73,_74,_75,_76){
var _77=_72.split(".");
var _78=_77.pop();
var _79="\\.("+(_73?_73+"|":"")+dojo.widget.defineWidget.renderers+")\\.";
var r=_72.search(new RegExp(_79));
_77=(r<0?_77.join("."):_72.substr(0,r));
dojo.widget.manager.registerWidgetPackage(_77);
var pos=_77.indexOf(".");
var _7c=(pos>-1)?_77.substring(0,pos):_77;
_76=(_76)||{};
_76.widgetType=_78;
if((!_75)&&(_76["classConstructor"])){
_75=_76.classConstructor;
delete _76.classConstructor;
}
dojo.declare(_72,_74,_75,_76);
};
dojo.provide("dojo.widget.Parse");
dojo.require("dojo.widget.Manager");
dojo.require("dojo.dom");
dojo.widget.Parse=function(_7d){
this.propertySetsList=[];
this.fragment=_7d;
this.createComponents=function(_7e,_7f){
var _80=[];
var _81=false;
try{
if(_7e&&_7e.tagName&&(_7e!=_7e.nodeRef)){
var _82=dojo.widget.tags;
var tna=String(_7e.tagName).split(";");
for(var x=0;x<tna.length;x++){
var ltn=tna[x].replace(/^\s+|\s+$/g,"").toLowerCase();
_7e.tagName=ltn;
var ret;
if(_82[ltn]){
_81=true;
ret=_82[ltn](_7e,this,_7f,_7e.index);
_80.push(ret);
}else{
if(ltn.indexOf(":")==-1){
ltn="dojo:"+ltn;
}
ret=dojo.widget.buildWidgetFromParseTree(ltn,_7e,this,_7f,_7e.index);
if(ret){
_81=true;
_80.push(ret);
}
}
}
}
}
catch(e){
dojo.debug("dojo.widget.Parse: error:",e);
}
if(!_81){
_80=_80.concat(this.createSubComponents(_7e,_7f));
}
return _80;
};
this.createSubComponents=function(_87,_88){
var _89,_8a=[];
for(var _8b in _87){
_89=_87[_8b];
if(_89&&typeof _89=="object"&&(_89!=_87.nodeRef)&&(_89!=_87.tagName)&&(!dojo.dom.isNode(_89))){
_8a=_8a.concat(this.createComponents(_89,_88));
}
}
return _8a;
};
this.parsePropertySets=function(_8c){
return [];
};
this.parseProperties=function(_8d){
var _8e={};
for(var _8f in _8d){
if((_8d[_8f]==_8d.tagName)||(_8d[_8f]==_8d.nodeRef)){
}else{
var _90=_8d[_8f];
if(_90.tagName&&dojo.widget.tags[_90.tagName.toLowerCase()]){
}else{
if(_90[0]&&_90[0].value!=""&&_90[0].value!=null){
try{
if(_8f.toLowerCase()=="dataprovider"){
var _91=this;
this.getDataProvider(_91,_90[0].value);
_8e.dataProvider=this.dataProvider;
}
_8e[_8f]=_90[0].value;
var _92=this.parseProperties(_90);
for(var _93 in _92){
_8e[_93]=_92[_93];
}
}
catch(e){
dojo.debug(e);
}
}
}
switch(_8f.toLowerCase()){
case "checked":
case "disabled":
if(typeof _8e[_8f]!="boolean"){
_8e[_8f]=true;
}
break;
}
}
}
return _8e;
};
this.getDataProvider=function(_94,_95){
dojo.io.bind({url:_95,load:function(_96,_97){
if(_96=="load"){
_94.dataProvider=_97;
}
},mimetype:"text/javascript",sync:true});
};
this.getPropertySetById=function(_98){
for(var x=0;x<this.propertySetsList.length;x++){
if(_98==this.propertySetsList[x]["id"][0].value){
return this.propertySetsList[x];
}
}
return "";
};
this.getPropertySetsByType=function(_9a){
var _9b=[];
for(var x=0;x<this.propertySetsList.length;x++){
var cpl=this.propertySetsList[x];
var _9e=cpl.componentClass||cpl.componentType||null;
var _9f=this.propertySetsList[x]["id"][0].value;
if(_9e&&(_9f==_9e[0].value)){
_9b.push(cpl);
}
}
return _9b;
};
this.getPropertySets=function(_a0){
var ppl="dojo:propertyproviderlist";
var _a2=[];
var _a3=_a0.tagName;
if(_a0[ppl]){
var _a4=_a0[ppl].value.split(" ");
for(var _a5 in _a4){
if((_a5.indexOf("..")==-1)&&(_a5.indexOf("://")==-1)){
var _a6=this.getPropertySetById(_a5);
if(_a6!=""){
_a2.push(_a6);
}
}else{
}
}
}
return this.getPropertySetsByType(_a3).concat(_a2);
};
this.createComponentFromScript=function(_a7,_a8,_a9,ns){
_a9.fastMixIn=true;
var ltn=(ns||"dojo")+":"+_a8.toLowerCase();
if(dojo.widget.tags[ltn]){
return [dojo.widget.tags[ltn](_a9,this,null,null,_a9)];
}
return [dojo.widget.buildWidgetFromParseTree(ltn,_a9,this,null,null,_a9)];
};
};
dojo.widget._parser_collection={"dojo":new dojo.widget.Parse()};
dojo.widget.getParser=function(_ac){
if(!_ac){
_ac="dojo";
}
if(!this._parser_collection[_ac]){
this._parser_collection[_ac]=new dojo.widget.Parse();
}
return this._parser_collection[_ac];
};
dojo.widget.createWidget=function(_ad,_ae,_af,_b0){
var _b1=false;
var _b2=(typeof _ad=="string");
if(_b2){
var pos=_ad.indexOf(":");
var ns=(pos>-1)?_ad.substring(0,pos):"dojo";
if(pos>-1){
_ad=_ad.substring(pos+1);
}
var _b5=_ad.toLowerCase();
var _b6=ns+":"+_b5;
_b1=(dojo.byId(_ad)&&!dojo.widget.tags[_b6]);
}
if((arguments.length==1)&&(_b1||!_b2)){
var xp=new dojo.xml.Parse();
var tn=_b1?dojo.byId(_ad):_ad;
return dojo.widget.getParser().createComponents(xp.parseElement(tn,null,true))[0];
}
function fromScript(_b9,_ba,_bb,ns){
_bb[_b6]={dojotype:[{value:_b5}],nodeRef:_b9,fastMixIn:true};
_bb.ns=ns;
return dojo.widget.getParser().createComponentFromScript(_b9,_ba,_bb,ns);
}
_ae=_ae||{};
var _bd=false;
var tn=null;
var h=dojo.render.html.capable;
if(h){
tn=document.createElement("span");
}
if(!_af){
_bd=true;
_af=tn;
if(h){
dojo.body().appendChild(_af);
}
}else{
if(_b0){
dojo.dom.insertAtPosition(tn,_af,_b0);
}else{
tn=_af;
}
}
var _bf=fromScript(tn,_ad.toLowerCase(),_ae,ns);
if((!_bf)||(!_bf[0])||(typeof _bf[0].widgetType=="undefined")){
throw new Error("createWidget: Creation of \""+_ad+"\" widget failed.");
}
try{
if(_bd&&_bf[0].domNode.parentNode){
_bf[0].domNode.parentNode.removeChild(_bf[0].domNode);
}
}
catch(e){
dojo.debug(e);
}
return _bf[0];
};
dojo.kwCompoundRequire({common:[["dojo.uri.Uri",false,false]]});
dojo.provide("dojo.uri.*");
dojo.provide("dojo.widget.DomWidget");
dojo.require("dojo.event.*");
dojo.require("dojo.dom");
dojo.require("dojo.html.style");
dojo.require("dojo.lang.func");
dojo.require("dojo.lang.extras");
dojo.widget._cssFiles={};
dojo.widget._cssStrings={};
dojo.widget._templateCache={};
dojo.widget.defaultStrings={dojoRoot:dojo.hostenv.getBaseScriptUri(),dojoWidgetModuleUri:dojo.uri.moduleUri("dojo.widget"),baseScriptUri:dojo.hostenv.getBaseScriptUri()};
dojo.widget.fillFromTemplateCache=function(obj,_c1,_c2,_c3){
var _c4=_c1||obj.templatePath;
var _c5=dojo.widget._templateCache;
if(!_c4&&!obj["widgetType"]){
do{
var _c6="__dummyTemplate__"+dojo.widget._templateCache.dummyCount++;
}while(_c5[_c6]);
obj.widgetType=_c6;
}
var wt=_c4?_c4.toString():obj.widgetType;
var ts=_c5[wt];
if(!ts){
_c5[wt]={"string":null,"node":null};
if(_c3){
ts={};
}else{
ts=_c5[wt];
}
}
if((!obj.templateString)&&(!_c3)){
obj.templateString=_c2||ts["string"];
}
if(obj.templateString){
obj.templateString=this._sanitizeTemplateString(obj.templateString);
}
if((!obj.templateNode)&&(!_c3)){
obj.templateNode=ts["node"];
}
if((!obj.templateNode)&&(!obj.templateString)&&(_c4)){
var _c9=this._sanitizeTemplateString(dojo.hostenv.getText(_c4));
obj.templateString=_c9;
if(!_c3){
_c5[wt]["string"]=_c9;
}
}
if((!ts["string"])&&(!_c3)){
ts.string=obj.templateString;
}
};
dojo.widget._sanitizeTemplateString=function(_ca){
if(_ca){
_ca=_ca.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im,"");
var _cb=_ca.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
if(_cb){
_ca=_cb[1];
}
}else{
_ca="";
}
return _ca;
};
dojo.widget._templateCache.dummyCount=0;
dojo.widget.attachProperties=["dojoAttachPoint","id"];
dojo.widget.eventAttachProperty="dojoAttachEvent";
dojo.widget.onBuildProperty="dojoOnBuild";
dojo.widget.waiNames=["waiRole","waiState"];
dojo.widget.wai={waiRole:{name:"waiRole","namespace":"http://www.w3.org/TR/xhtml2",alias:"x2",prefix:"wairole:"},waiState:{name:"waiState","namespace":"http://www.w3.org/2005/07/aaa",alias:"aaa",prefix:""},setAttr:function(_cc,ns,_ce,_cf){
if(dojo.render.html.ie){
_cc.setAttribute(this[ns].alias+":"+_ce,this[ns].prefix+_cf);
}else{
_cc.setAttributeNS(this[ns]["namespace"],_ce,this[ns].prefix+_cf);
}
},getAttr:function(_d0,ns,_d2){
if(dojo.render.html.ie){
return _d0.getAttribute(this[ns].alias+":"+_d2);
}else{
return _d0.getAttributeNS(this[ns]["namespace"],_d2);
}
},removeAttr:function(_d3,ns,_d5){
var _d6=true;
if(dojo.render.html.ie){
_d6=_d3.removeAttribute(this[ns].alias+":"+_d5);
}else{
_d3.removeAttributeNS(this[ns]["namespace"],_d5);
}
return _d6;
}};
dojo.widget.attachTemplateNodes=function(_d7,_d8,_d9){
var _da=dojo.dom.ELEMENT_NODE;
function trim(str){
return str.replace(/^\s+|\s+$/g,"");
}
if(!_d7){
_d7=_d8.domNode;
}
if(_d7.nodeType!=_da){
return;
}
var _dc=_d7.all||_d7.getElementsByTagName("*");
var _dd=_d8;
for(var x=-1;x<_dc.length;x++){
var _df=(x==-1)?_d7:_dc[x];
var _e0=[];
if(!_d8.widgetsInTemplate||!_df.getAttribute("dojoType")){
for(var y=0;y<this.attachProperties.length;y++){
var _e2=_df.getAttribute(this.attachProperties[y]);
if(_e2){
_e0=_e2.split(";");
for(var z=0;z<_e0.length;z++){
if(dojo.lang.isArray(_d8[_e0[z]])){
_d8[_e0[z]].push(_df);
}else{
_d8[_e0[z]]=_df;
}
}
break;
}
}
var _e4=_df.getAttribute(this.eventAttachProperty);
if(_e4){
var _e5=_e4.split(";");
for(var y=0;y<_e5.length;y++){
if((!_e5[y])||(!_e5[y].length)){
continue;
}
var _e6=null;
var _e7=trim(_e5[y]);
if(_e5[y].indexOf(":")>=0){
var _e8=_e7.split(":");
_e7=trim(_e8[0]);
_e6=trim(_e8[1]);
}
if(!_e6){
_e6=_e7;
}
var tf=function(){
var ntf=new String(_e6);
return function(evt){
if(_dd[ntf]){
_dd[ntf](dojo.event.browser.fixEvent(evt,this));
}
};
}();
dojo.event.browser.addListener(_df,_e7,tf,false,true);
}
}
for(var y=0;y<_d9.length;y++){
var _ec=_df.getAttribute(_d9[y]);
if((_ec)&&(_ec.length)){
var _e6=null;
var _ed=_d9[y].substr(4);
_e6=trim(_ec);
var _ee=[_e6];
if(_e6.indexOf(";")>=0){
_ee=dojo.lang.map(_e6.split(";"),trim);
}
for(var z=0;z<_ee.length;z++){
if(!_ee[z].length){
continue;
}
var tf=function(){
var ntf=new String(_ee[z]);
return function(evt){
if(_dd[ntf]){
_dd[ntf](dojo.event.browser.fixEvent(evt,this));
}
};
}();
dojo.event.browser.addListener(_df,_ed,tf,false,true);
}
}
}
}
var _f1=_df.getAttribute(this.templateProperty);
if(_f1){
_d8[_f1]=_df;
}
dojo.lang.forEach(dojo.widget.waiNames,function(_f2){
var wai=dojo.widget.wai[_f2];
var val=_df.getAttribute(wai.name);
if(val){
if(val.indexOf("-")==-1){
dojo.widget.wai.setAttr(_df,wai.name,"role",val);
}else{
var _f5=val.split("-");
dojo.widget.wai.setAttr(_df,wai.name,_f5[0],_f5[1]);
}
}
},this);
var _f6=_df.getAttribute(this.onBuildProperty);
if(_f6){
eval("var node = baseNode; var widget = targetObj; "+_f6);
}
}
};
dojo.widget.getDojoEventsFromStr=function(str){
var re=/(dojoOn([a-z]+)(\s?))=/gi;
var _f9=str?str.match(re)||[]:[];
var ret=[];
var lem={};
for(var x=0;x<_f9.length;x++){
if(_f9[x].length<1){
continue;
}
var cm=_f9[x].replace(/\s/,"");
cm=(cm.slice(0,cm.length-1));
if(!lem[cm]){
lem[cm]=true;
ret.push(cm);
}
}
return ret;
};
dojo.declare("dojo.widget.DomWidget",dojo.widget.Widget,function(){
if((arguments.length>0)&&(typeof arguments[0]=="object")){
this.create(arguments[0]);
}
},{templateNode:null,templateString:null,templateCssString:null,preventClobber:false,domNode:null,containerNode:null,widgetsInTemplate:false,addChild:function(_fe,_ff,pos,ref,_102){
if(!this.isContainer){
dojo.debug("dojo.widget.DomWidget.addChild() attempted on non-container widget");
return null;
}else{
if(_102==undefined){
_102=this.children.length;
}
this.addWidgetAsDirectChild(_fe,_ff,pos,ref,_102);
this.registerChild(_fe,_102);
}
return _fe;
},addWidgetAsDirectChild:function(_103,_104,pos,ref,_107){
if((!this.containerNode)&&(!_104)){
this.containerNode=this.domNode;
}
var cn=(_104)?_104:this.containerNode;
if(!pos){
pos="after";
}
if(!ref){
if(!cn){
cn=dojo.body();
}
ref=cn.lastChild;
}
if(!_107){
_107=0;
}
_103.domNode.setAttribute("dojoinsertionindex",_107);
if(!ref){
cn.appendChild(_103.domNode);
}else{
if(pos=="insertAtIndex"){
dojo.dom.insertAtIndex(_103.domNode,ref.parentNode,_107);
}else{
if((pos=="after")&&(ref===cn.lastChild)){
cn.appendChild(_103.domNode);
}else{
dojo.dom.insertAtPosition(_103.domNode,cn,pos);
}
}
}
},registerChild:function(_109,_10a){
_109.dojoInsertionIndex=_10a;
var idx=-1;
for(var i=0;i<this.children.length;i++){
if(this.children[i].dojoInsertionIndex<=_10a){
idx=i;
}
}
this.children.splice(idx+1,0,_109);
_109.parent=this;
_109.addedTo(this,idx+1);
delete dojo.widget.manager.topWidgets[_109.widgetId];
},removeChild:function(_10d){
dojo.dom.removeNode(_10d.domNode);
return dojo.widget.DomWidget.superclass.removeChild.call(this,_10d);
},getFragNodeRef:function(frag){
if(!frag){
return null;
}
if(!frag[this.getNamespacedType()]){
dojo.raise("Error: no frag for widget type "+this.getNamespacedType()+", id "+this.widgetId+" (maybe a widget has set it's type incorrectly)");
}
return frag[this.getNamespacedType()]["nodeRef"];
},postInitialize:function(args,frag,_111){
var _112=this.getFragNodeRef(frag);
if(_111&&(_111.snarfChildDomOutput||!_112)){
_111.addWidgetAsDirectChild(this,"","insertAtIndex","",args["dojoinsertionindex"],_112);
}else{
if(_112){
if(this.domNode&&(this.domNode!==_112)){
this._sourceNodeRef=dojo.dom.replaceNode(_112,this.domNode);
}
}
}
if(_111){
_111.registerChild(this,args.dojoinsertionindex);
}else{
dojo.widget.manager.topWidgets[this.widgetId]=this;
}
if(this.widgetsInTemplate){
var _113=new dojo.xml.Parse();
var _114;
var _115=this.domNode.getElementsByTagName("*");
for(var i=0;i<_115.length;i++){
if(_115[i].getAttribute("dojoAttachPoint")=="subContainerWidget"){
_114=_115[i];
}
if(_115[i].getAttribute("dojoType")){
_115[i].setAttribute("isSubWidget",true);
}
}
if(this.isContainer&&!this.containerNode){
if(_114){
var src=this.getFragNodeRef(frag);
if(src){
dojo.dom.moveChildren(src,_114);
frag["dojoDontFollow"]=true;
}
}else{
dojo.debug("No subContainerWidget node can be found in template file for widget "+this);
}
}
var _118=_113.parseElement(this.domNode,null,true);
dojo.widget.getParser().createSubComponents(_118,this);
var _119=[];
var _11a=[this];
var w;
while((w=_11a.pop())){
for(var i=0;i<w.children.length;i++){
var _11c=w.children[i];
if(_11c._processedSubWidgets||!_11c.extraArgs["issubwidget"]){
continue;
}
_119.push(_11c);
if(_11c.isContainer){
_11a.push(_11c);
}
}
}
for(var i=0;i<_119.length;i++){
var _11d=_119[i];
if(_11d._processedSubWidgets){
dojo.debug("This should not happen: widget._processedSubWidgets is already true!");
return;
}
_11d._processedSubWidgets=true;
if(_11d.extraArgs["dojoattachevent"]){
var evts=_11d.extraArgs["dojoattachevent"].split(";");
for(var j=0;j<evts.length;j++){
var _120=null;
var tevt=dojo.string.trim(evts[j]);
if(tevt.indexOf(":")>=0){
var _122=tevt.split(":");
tevt=dojo.string.trim(_122[0]);
_120=dojo.string.trim(_122[1]);
}
if(!_120){
_120=tevt;
}
if(dojo.lang.isFunction(_11d[tevt])){
dojo.event.kwConnect({srcObj:_11d,srcFunc:tevt,targetObj:this,targetFunc:_120});
}else{
alert(tevt+" is not a function in widget "+_11d);
}
}
}
if(_11d.extraArgs["dojoattachpoint"]){
this[_11d.extraArgs["dojoattachpoint"]]=_11d;
}
}
}
if(this.isContainer&&!frag["dojoDontFollow"]){
dojo.widget.getParser().createSubComponents(frag,this);
}
},buildRendering:function(args,frag){
var ts=dojo.widget._templateCache[this.widgetType];
if(args["templatecsspath"]){
args["templateCssPath"]=args["templatecsspath"];
}
var _126=args["templateCssPath"]||this.templateCssPath;
if(_126&&!dojo.widget._cssFiles[_126.toString()]){
if((!this.templateCssString)&&(_126)){
this.templateCssString=dojo.hostenv.getText(_126);
this.templateCssPath=null;
}
dojo.widget._cssFiles[_126.toString()]=true;
}
if((this["templateCssString"])&&(!dojo.widget._cssStrings[this.templateCssString])){
dojo.html.insertCssText(this.templateCssString,null,_126);
dojo.widget._cssStrings[this.templateCssString]=true;
}
if((!this.preventClobber)&&((this.templatePath)||(this.templateNode)||((this["templateString"])&&(this.templateString.length))||((typeof ts!="undefined")&&((ts["string"])||(ts["node"]))))){
this.buildFromTemplate(args,frag);
}else{
this.domNode=this.getFragNodeRef(frag);
}
this.fillInTemplate(args,frag);
},buildFromTemplate:function(args,frag){
var _129=false;
if(args["templatepath"]){
args["templatePath"]=args["templatepath"];
}
dojo.widget.fillFromTemplateCache(this,args["templatePath"],null,_129);
var ts=dojo.widget._templateCache[this.templatePath?this.templatePath.toString():this.widgetType];
if((ts)&&(!_129)){
if(!this.templateString.length){
this.templateString=ts["string"];
}
if(!this.templateNode){
this.templateNode=ts["node"];
}
}
var _12b=false;
var node=null;
var tstr=this.templateString;
if((!this.templateNode)&&(this.templateString)){
_12b=this.templateString.match(/\$\{([^\}]+)\}/g);
if(_12b){
var hash=this.strings||{};
for(var key in dojo.widget.defaultStrings){
if(dojo.lang.isUndefined(hash[key])){
hash[key]=dojo.widget.defaultStrings[key];
}
}
for(var i=0;i<_12b.length;i++){
var key=_12b[i];
key=key.substring(2,key.length-1);
var kval=(key.substring(0,5)=="this.")?dojo.lang.getObjPathValue(key.substring(5),this):hash[key];
var _132;
if((kval)||(dojo.lang.isString(kval))){
_132=new String((dojo.lang.isFunction(kval))?kval.call(this,key,this.templateString):kval);
while(_132.indexOf("\"")>-1){
_132=_132.replace("\"","&quot;");
}
tstr=tstr.replace(_12b[i],_132);
}
}
}else{
this.templateNode=this.createNodesFromText(this.templateString,true)[0];
if(!_129){
ts.node=this.templateNode;
}
}
}
if((!this.templateNode)&&(!_12b)){
dojo.debug("DomWidget.buildFromTemplate: could not create template");
return false;
}else{
if(!_12b){
node=this.templateNode.cloneNode(true);
if(!node){
return false;
}
}else{
node=this.createNodesFromText(tstr,true)[0];
}
}
this.domNode=node;
this.attachTemplateNodes();
if(this.isContainer&&this.containerNode){
var src=this.getFragNodeRef(frag);
if(src){
dojo.dom.moveChildren(src,this.containerNode);
}
}
},attachTemplateNodes:function(_134,_135){
if(!_134){
_134=this.domNode;
}
if(!_135){
_135=this;
}
return dojo.widget.attachTemplateNodes(_134,_135,dojo.widget.getDojoEventsFromStr(this.templateString));
},fillInTemplate:function(){
},destroyRendering:function(){
try{
dojo.dom.destroyNode(this.domNode);
delete this.domNode;
}
catch(e){
}
if(this._sourceNodeRef){
try{
dojo.dom.destroyNode(this._sourceNodeRef);
}
catch(e){
}
}
},createNodesFromText:function(){
dojo.unimplemented("dojo.widget.DomWidget.createNodesFromText");
}});
dojo.provide("dojo.html.layout");
dojo.require("dojo.html.common");
dojo.require("dojo.html.style");
dojo.require("dojo.html.display");
dojo.html.sumAncestorProperties=function(node,prop){
node=dojo.byId(node);
if(!node){
return 0;
}
var _138=0;
while(node){
if(dojo.html.getComputedStyle(node,"position")=="fixed"){
return 0;
}
var val=node[prop];
if(val){
_138+=val-0;
if(node==dojo.body()){
break;
}
}
node=node.parentNode;
}
return _138;
};
dojo.html.setStyleAttributes=function(node,_13b){
node=dojo.byId(node);
var _13c=_13b.replace(/(;)?\s*$/,"").split(";");
for(var i=0;i<_13c.length;i++){
var _13e=_13c[i].split(":");
var name=_13e[0].replace(/\s*$/,"").replace(/^\s*/,"").toLowerCase();
var _140=_13e[1].replace(/\s*$/,"").replace(/^\s*/,"");
switch(name){
case "opacity":
dojo.html.setOpacity(node,_140);
break;
case "content-height":
dojo.html.setContentBox(node,{height:_140});
break;
case "content-width":
dojo.html.setContentBox(node,{width:_140});
break;
case "outer-height":
dojo.html.setMarginBox(node,{height:_140});
break;
case "outer-width":
dojo.html.setMarginBox(node,{width:_140});
break;
default:
node.style[dojo.html.toCamelCase(name)]=_140;
}
}
};
dojo.html.boxSizing={MARGIN_BOX:"margin-box",BORDER_BOX:"border-box",PADDING_BOX:"padding-box",CONTENT_BOX:"content-box"};
dojo.html.getAbsolutePosition=dojo.html.abs=function(node,_142,_143){
node=dojo.byId(node,node.ownerDocument);
var ret={x:0,y:0};
var bs=dojo.html.boxSizing;
if(!_143){
_143=bs.CONTENT_BOX;
}
var _146=2;
var _147;
switch(_143){
case bs.MARGIN_BOX:
_147=3;
break;
case bs.BORDER_BOX:
_147=2;
break;
case bs.PADDING_BOX:
default:
_147=1;
break;
case bs.CONTENT_BOX:
_147=0;
break;
}
var h=dojo.render.html;
var db=document["body"]||document["documentElement"];
if(h.ie){
with(node.getBoundingClientRect()){
ret.x=left-2;
ret.y=top-2;
}
}else{
if(document.getBoxObjectFor){
_146=1;
try{
var bo=document.getBoxObjectFor(node);
ret.x=bo.x-dojo.html.sumAncestorProperties(node,"scrollLeft");
ret.y=bo.y-dojo.html.sumAncestorProperties(node,"scrollTop");
}
catch(e){
}
}else{
if(node["offsetParent"]){
var _14b;
if((h.safari)&&(node.style.getPropertyValue("position")=="absolute")&&(node.parentNode==db)){
_14b=db;
}else{
_14b=db.parentNode;
}
if(node.parentNode!=db){
var nd=node;
if(dojo.render.html.opera){
nd=db;
}
ret.x-=dojo.html.sumAncestorProperties(nd,"scrollLeft");
ret.y-=dojo.html.sumAncestorProperties(nd,"scrollTop");
}
var _14d=node;
do{
var n=_14d["offsetLeft"];
if(!h.opera||n>0){
ret.x+=isNaN(n)?0:n;
}
var m=_14d["offsetTop"];
ret.y+=isNaN(m)?0:m;
_14d=_14d.offsetParent;
}while((_14d!=_14b)&&(_14d!=null));
}else{
if(node["x"]&&node["y"]){
ret.x+=isNaN(node.x)?0:node.x;
ret.y+=isNaN(node.y)?0:node.y;
}
}
}
}
if(_142){
var _150=dojo.html.getScroll();
ret.y+=_150.top;
ret.x+=_150.left;
}
var _151=[dojo.html.getPaddingExtent,dojo.html.getBorderExtent,dojo.html.getMarginExtent];
if(_146>_147){
for(var i=_147;i<_146;++i){
ret.y+=_151[i](node,"top");
ret.x+=_151[i](node,"left");
}
}else{
if(_146<_147){
for(var i=_147;i>_146;--i){
ret.y-=_151[i-1](node,"top");
ret.x-=_151[i-1](node,"left");
}
}
}
ret.top=ret.y;
ret.left=ret.x;
return ret;
};
dojo.html.isPositionAbsolute=function(node){
return (dojo.html.getComputedStyle(node,"position")=="absolute");
};
dojo.html._sumPixelValues=function(node,_155,_156){
var _157=0;
for(var x=0;x<_155.length;x++){
_157+=dojo.html.getPixelValue(node,_155[x],_156);
}
return _157;
};
dojo.html.getMargin=function(node){
return {width:dojo.html._sumPixelValues(node,["margin-left","margin-right"],(dojo.html.getComputedStyle(node,"position")=="absolute")),height:dojo.html._sumPixelValues(node,["margin-top","margin-bottom"],(dojo.html.getComputedStyle(node,"position")=="absolute"))};
};
dojo.html.getBorder=function(node){
return {width:dojo.html.getBorderExtent(node,"left")+dojo.html.getBorderExtent(node,"right"),height:dojo.html.getBorderExtent(node,"top")+dojo.html.getBorderExtent(node,"bottom")};
};
dojo.html.getBorderExtent=function(node,side){
return (dojo.html.getStyle(node,"border-"+side+"-style")=="none"?0:dojo.html.getPixelValue(node,"border-"+side+"-width"));
};
dojo.html.getMarginExtent=function(node,side){
return dojo.html._sumPixelValues(node,["margin-"+side],dojo.html.isPositionAbsolute(node));
};
dojo.html.getPaddingExtent=function(node,side){
return dojo.html._sumPixelValues(node,["padding-"+side],true);
};
dojo.html.getPadding=function(node){
return {width:dojo.html._sumPixelValues(node,["padding-left","padding-right"],true),height:dojo.html._sumPixelValues(node,["padding-top","padding-bottom"],true)};
};
dojo.html.getPadBorder=function(node){
var pad=dojo.html.getPadding(node);
var _164=dojo.html.getBorder(node);
return {width:pad.width+_164.width,height:pad.height+_164.height};
};
dojo.html.getBoxSizing=function(node){
var h=dojo.render.html;
var bs=dojo.html.boxSizing;
if(((h.ie)||(h.opera))&&node.nodeName.toLowerCase()!="img"){
var cm=document["compatMode"];
if((cm=="BackCompat")||(cm=="QuirksMode")){
return bs.BORDER_BOX;
}else{
return bs.CONTENT_BOX;
}
}else{
if(arguments.length==0){
node=document.documentElement;
}
var _169;
if(!h.ie){
_169=dojo.html.getStyle(node,"-moz-box-sizing");
if(!_169){
_169=dojo.html.getStyle(node,"box-sizing");
}
}
return (_169?_169:bs.CONTENT_BOX);
}
};
dojo.html.isBorderBox=function(node){
return (dojo.html.getBoxSizing(node)==dojo.html.boxSizing.BORDER_BOX);
};
dojo.html.getBorderBox=function(node){
node=dojo.byId(node);
return {width:node.offsetWidth,height:node.offsetHeight};
};
dojo.html.getPaddingBox=function(node){
var box=dojo.html.getBorderBox(node);
var _16e=dojo.html.getBorder(node);
return {width:box.width-_16e.width,height:box.height-_16e.height};
};
dojo.html.getContentBox=function(node){
node=dojo.byId(node);
var _170=dojo.html.getPadBorder(node);
return {width:node.offsetWidth-_170.width,height:node.offsetHeight-_170.height};
};
dojo.html.setContentBox=function(node,args){
node=dojo.byId(node);
var _173=0;
var _174=0;
var isbb=dojo.html.isBorderBox(node);
var _176=(isbb?dojo.html.getPadBorder(node):{width:0,height:0});
var ret={};
if(typeof args.width!="undefined"){
_173=args.width+_176.width;
ret.width=dojo.html.setPositivePixelValue(node,"width",_173);
}
if(typeof args.height!="undefined"){
_174=args.height+_176.height;
ret.height=dojo.html.setPositivePixelValue(node,"height",_174);
}
return ret;
};
dojo.html.getMarginBox=function(node){
var _179=dojo.html.getBorderBox(node);
var _17a=dojo.html.getMargin(node);
return {width:_179.width+_17a.width,height:_179.height+_17a.height};
};
dojo.html.setMarginBox=function(node,args){
node=dojo.byId(node);
var _17d=0;
var _17e=0;
var isbb=dojo.html.isBorderBox(node);
var _180=(!isbb?dojo.html.getPadBorder(node):{width:0,height:0});
var _181=dojo.html.getMargin(node);
var ret={};
if(typeof args.width!="undefined"){
_17d=args.width-_180.width;
_17d-=_181.width;
ret.width=dojo.html.setPositivePixelValue(node,"width",_17d);
}
if(typeof args.height!="undefined"){
_17e=args.height-_180.height;
_17e-=_181.height;
ret.height=dojo.html.setPositivePixelValue(node,"height",_17e);
}
return ret;
};
dojo.html.getElementBox=function(node,type){
var bs=dojo.html.boxSizing;
switch(type){
case bs.MARGIN_BOX:
return dojo.html.getMarginBox(node);
case bs.BORDER_BOX:
return dojo.html.getBorderBox(node);
case bs.PADDING_BOX:
return dojo.html.getPaddingBox(node);
case bs.CONTENT_BOX:
default:
return dojo.html.getContentBox(node);
}
};
dojo.html.toCoordinateObject=dojo.html.toCoordinateArray=function(_186,_187,_188){
if(_186 instanceof Array||typeof _186=="array"){
dojo.deprecated("dojo.html.toCoordinateArray","use dojo.html.toCoordinateObject({left: , top: , width: , height: }) instead","0.5");
while(_186.length<4){
_186.push(0);
}
while(_186.length>4){
_186.pop();
}
var ret={left:_186[0],top:_186[1],width:_186[2],height:_186[3]};
}else{
if(!_186.nodeType&&!(_186 instanceof String||typeof _186=="string")&&("width" in _186||"height" in _186||"left" in _186||"x" in _186||"top" in _186||"y" in _186)){
var ret={left:_186.left||_186.x||0,top:_186.top||_186.y||0,width:_186.width||0,height:_186.height||0};
}else{
var node=dojo.byId(_186);
var pos=dojo.html.abs(node,_187,_188);
var _18c=dojo.html.getMarginBox(node);
var ret={left:pos.left,top:pos.top,width:_18c.width,height:_18c.height};
}
}
ret.x=ret.left;
ret.y=ret.top;
return ret;
};
dojo.html.setMarginBoxWidth=dojo.html.setOuterWidth=function(node,_18e){
return dojo.html._callDeprecated("setMarginBoxWidth","setMarginBox",arguments,"width");
};
dojo.html.setMarginBoxHeight=dojo.html.setOuterHeight=function(){
return dojo.html._callDeprecated("setMarginBoxHeight","setMarginBox",arguments,"height");
};
dojo.html.getMarginBoxWidth=dojo.html.getOuterWidth=function(){
return dojo.html._callDeprecated("getMarginBoxWidth","getMarginBox",arguments,null,"width");
};
dojo.html.getMarginBoxHeight=dojo.html.getOuterHeight=function(){
return dojo.html._callDeprecated("getMarginBoxHeight","getMarginBox",arguments,null,"height");
};
dojo.html.getTotalOffset=function(node,type,_191){
return dojo.html._callDeprecated("getTotalOffset","getAbsolutePosition",arguments,null,type);
};
dojo.html.getAbsoluteX=function(node,_193){
return dojo.html._callDeprecated("getAbsoluteX","getAbsolutePosition",arguments,null,"x");
};
dojo.html.getAbsoluteY=function(node,_195){
return dojo.html._callDeprecated("getAbsoluteY","getAbsolutePosition",arguments,null,"y");
};
dojo.html.totalOffsetLeft=function(node,_197){
return dojo.html._callDeprecated("totalOffsetLeft","getAbsolutePosition",arguments,null,"left");
};
dojo.html.totalOffsetTop=function(node,_199){
return dojo.html._callDeprecated("totalOffsetTop","getAbsolutePosition",arguments,null,"top");
};
dojo.html.getMarginWidth=function(node){
return dojo.html._callDeprecated("getMarginWidth","getMargin",arguments,null,"width");
};
dojo.html.getMarginHeight=function(node){
return dojo.html._callDeprecated("getMarginHeight","getMargin",arguments,null,"height");
};
dojo.html.getBorderWidth=function(node){
return dojo.html._callDeprecated("getBorderWidth","getBorder",arguments,null,"width");
};
dojo.html.getBorderHeight=function(node){
return dojo.html._callDeprecated("getBorderHeight","getBorder",arguments,null,"height");
};
dojo.html.getPaddingWidth=function(node){
return dojo.html._callDeprecated("getPaddingWidth","getPadding",arguments,null,"width");
};
dojo.html.getPaddingHeight=function(node){
return dojo.html._callDeprecated("getPaddingHeight","getPadding",arguments,null,"height");
};
dojo.html.getPadBorderWidth=function(node){
return dojo.html._callDeprecated("getPadBorderWidth","getPadBorder",arguments,null,"width");
};
dojo.html.getPadBorderHeight=function(node){
return dojo.html._callDeprecated("getPadBorderHeight","getPadBorder",arguments,null,"height");
};
dojo.html.getBorderBoxWidth=dojo.html.getInnerWidth=function(){
return dojo.html._callDeprecated("getBorderBoxWidth","getBorderBox",arguments,null,"width");
};
dojo.html.getBorderBoxHeight=dojo.html.getInnerHeight=function(){
return dojo.html._callDeprecated("getBorderBoxHeight","getBorderBox",arguments,null,"height");
};
dojo.html.getContentBoxWidth=dojo.html.getContentWidth=function(){
return dojo.html._callDeprecated("getContentBoxWidth","getContentBox",arguments,null,"width");
};
dojo.html.getContentBoxHeight=dojo.html.getContentHeight=function(){
return dojo.html._callDeprecated("getContentBoxHeight","getContentBox",arguments,null,"height");
};
dojo.html.setContentBoxWidth=dojo.html.setContentWidth=function(node,_1a3){
return dojo.html._callDeprecated("setContentBoxWidth","setContentBox",arguments,"width");
};
dojo.html.setContentBoxHeight=dojo.html.setContentHeight=function(node,_1a5){
return dojo.html._callDeprecated("setContentBoxHeight","setContentBox",arguments,"height");
};
dojo.provide("dojo.html.util");
dojo.html.getElementWindow=function(_1a6){
return dojo.html.getDocumentWindow(_1a6.ownerDocument);
};
dojo.html.getDocumentWindow=function(doc){
if(dojo.render.html.safari&&!doc._parentWindow){
var fix=function(win){
win.document._parentWindow=win;
for(var i=0;i<win.frames.length;i++){
fix(win.frames[i]);
}
};
fix(window.top);
}
if(dojo.render.html.ie&&window!==document.parentWindow&&!doc._parentWindow){
doc.parentWindow.execScript("document._parentWindow = window;","Javascript");
var win=doc._parentWindow;
doc._parentWindow=null;
return win;
}
return doc._parentWindow||doc.parentWindow||doc.defaultView;
};
dojo.html.gravity=function(node,e){
node=dojo.byId(node);
var _1ae=dojo.html.getCursorPosition(e);
with(dojo.html){
var _1af=getAbsolutePosition(node,true);
var bb=getBorderBox(node);
var _1b1=_1af.x+(bb.width/2);
var _1b2=_1af.y+(bb.height/2);
}
with(dojo.html.gravity){
return ((_1ae.x<_1b1?WEST:EAST)|(_1ae.y<_1b2?NORTH:SOUTH));
}
};
dojo.html.gravity.NORTH=1;
dojo.html.gravity.SOUTH=1<<1;
dojo.html.gravity.EAST=1<<2;
dojo.html.gravity.WEST=1<<3;
dojo.html.overElement=function(_1b3,e){
_1b3=dojo.byId(_1b3);
var _1b5=dojo.html.getCursorPosition(e);
var bb=dojo.html.getBorderBox(_1b3);
var _1b7=dojo.html.getAbsolutePosition(_1b3,true,dojo.html.boxSizing.BORDER_BOX);
var top=_1b7.y;
var _1b9=top+bb.height;
var left=_1b7.x;
var _1bb=left+bb.width;
return (_1b5.x>=left&&_1b5.x<=_1bb&&_1b5.y>=top&&_1b5.y<=_1b9);
};
dojo.html.renderedTextContent=function(node){
node=dojo.byId(node);
var _1bd="";
if(node==null){
return _1bd;
}
for(var i=0;i<node.childNodes.length;i++){
switch(node.childNodes[i].nodeType){
case 1:
case 5:
var _1bf="unknown";
try{
_1bf=dojo.html.getStyle(node.childNodes[i],"display");
}
catch(E){
}
switch(_1bf){
case "block":
case "list-item":
case "run-in":
case "table":
case "table-row-group":
case "table-header-group":
case "table-footer-group":
case "table-row":
case "table-column-group":
case "table-column":
case "table-cell":
case "table-caption":
_1bd+="\n";
_1bd+=dojo.html.renderedTextContent(node.childNodes[i]);
_1bd+="\n";
break;
case "none":
break;
default:
if(node.childNodes[i].tagName&&node.childNodes[i].tagName.toLowerCase()=="br"){
_1bd+="\n";
}else{
_1bd+=dojo.html.renderedTextContent(node.childNodes[i]);
}
break;
}
break;
case 3:
case 2:
case 4:
var text=node.childNodes[i].nodeValue;
var _1c1="unknown";
try{
_1c1=dojo.html.getStyle(node,"text-transform");
}
catch(E){
}
switch(_1c1){
case "capitalize":
var _1c2=text.split(" ");
for(var i=0;i<_1c2.length;i++){
_1c2[i]=_1c2[i].charAt(0).toUpperCase()+_1c2[i].substring(1);
}
text=_1c2.join(" ");
break;
case "uppercase":
text=text.toUpperCase();
break;
case "lowercase":
text=text.toLowerCase();
break;
default:
break;
}
switch(_1c1){
case "nowrap":
break;
case "pre-wrap":
break;
case "pre-line":
break;
case "pre":
break;
default:
text=text.replace(/\s+/," ");
if(/\s$/.test(_1bd)){
text.replace(/^\s/,"");
}
break;
}
_1bd+=text;
break;
default:
break;
}
}
return _1bd;
};
dojo.html.createNodesFromText=function(txt,trim){
if(trim){
txt=txt.replace(/^\s+|\s+$/g,"");
}
var tn=dojo.doc().createElement("div");
tn.style.visibility="hidden";
dojo.body().appendChild(tn);
var _1c6="none";
if((/^<t[dh][\s\r\n>]/i).test(txt.replace(/^\s+/))){
txt="<table><tbody><tr>"+txt+"</tr></tbody></table>";
_1c6="cell";
}else{
if((/^<tr[\s\r\n>]/i).test(txt.replace(/^\s+/))){
txt="<table><tbody>"+txt+"</tbody></table>";
_1c6="row";
}else{
if((/^<(thead|tbody|tfoot)[\s\r\n>]/i).test(txt.replace(/^\s+/))){
txt="<table>"+txt+"</table>";
_1c6="section";
}
}
}
tn.innerHTML=txt;
if(tn["normalize"]){
tn.normalize();
}
var _1c7=null;
switch(_1c6){
case "cell":
_1c7=tn.getElementsByTagName("tr")[0];
break;
case "row":
_1c7=tn.getElementsByTagName("tbody")[0];
break;
case "section":
_1c7=tn.getElementsByTagName("table")[0];
break;
default:
_1c7=tn;
break;
}
var _1c8=[];
for(var x=0;x<_1c7.childNodes.length;x++){
_1c8.push(_1c7.childNodes[x].cloneNode(true));
}
tn.style.display="none";
dojo.html.destroyNode(tn);
return _1c8;
};
dojo.html.placeOnScreen=function(node,_1cb,_1cc,_1cd,_1ce,_1cf,_1d0){
if(_1cb instanceof Array||typeof _1cb=="array"){
_1d0=_1cf;
_1cf=_1ce;
_1ce=_1cd;
_1cd=_1cc;
_1cc=_1cb[1];
_1cb=_1cb[0];
}
if(_1cf instanceof String||typeof _1cf=="string"){
_1cf=_1cf.split(",");
}
if(!isNaN(_1cd)){
_1cd=[Number(_1cd),Number(_1cd)];
}else{
if(!(_1cd instanceof Array||typeof _1cd=="array")){
_1cd=[0,0];
}
}
var _1d1=dojo.html.getScroll().offset;
var view=dojo.html.getViewport();
node=dojo.byId(node);
var _1d3=node.style.display;
node.style.display="";
var bb=dojo.html.getBorderBox(node);
var w=bb.width;
var h=bb.height;
node.style.display=_1d3;
if(!(_1cf instanceof Array||typeof _1cf=="array")){
_1cf=["TL"];
}
var _1d7,_1d8,_1d9=Infinity,_1da;
for(var _1db=0;_1db<_1cf.length;++_1db){
var _1dc=_1cf[_1db];
var _1dd=true;
var tryX=_1cb-(_1dc.charAt(1)=="L"?0:w)+_1cd[0]*(_1dc.charAt(1)=="L"?1:-1);
var tryY=_1cc-(_1dc.charAt(0)=="T"?0:h)+_1cd[1]*(_1dc.charAt(0)=="T"?1:-1);
if(_1ce){
tryX-=_1d1.x;
tryY-=_1d1.y;
}
if(tryX<0){
tryX=0;
_1dd=false;
}
if(tryY<0){
tryY=0;
_1dd=false;
}
var x=tryX+w;
if(x>view.width){
x=view.width-w;
_1dd=false;
}else{
x=tryX;
}
x=Math.max(_1cd[0],x)+_1d1.x;
var y=tryY+h;
if(y>view.height){
y=view.height-h;
_1dd=false;
}else{
y=tryY;
}
y=Math.max(_1cd[1],y)+_1d1.y;
if(_1dd){
_1d7=x;
_1d8=y;
_1d9=0;
_1da=_1dc;
break;
}else{
var dist=Math.pow(x-tryX-_1d1.x,2)+Math.pow(y-tryY-_1d1.y,2);
if(_1d9>dist){
_1d9=dist;
_1d7=x;
_1d8=y;
_1da=_1dc;
}
}
}
if(!_1d0){
node.style.left=_1d7+"px";
node.style.top=_1d8+"px";
}
return {left:_1d7,top:_1d8,x:_1d7,y:_1d8,dist:_1d9,corner:_1da};
};
dojo.html.placeOnScreenPoint=function(node,_1e4,_1e5,_1e6,_1e7){
dojo.deprecated("dojo.html.placeOnScreenPoint","use dojo.html.placeOnScreen() instead","0.5");
return dojo.html.placeOnScreen(node,_1e4,_1e5,_1e6,_1e7,["TL","TR","BL","BR"]);
};
dojo.html.placeOnScreenAroundElement=function(node,_1e9,_1ea,_1eb,_1ec,_1ed){
var best,_1ef=Infinity;
_1e9=dojo.byId(_1e9);
var _1f0=_1e9.style.display;
_1e9.style.display="";
var mb=dojo.html.getElementBox(_1e9,_1eb);
var _1f2=mb.width;
var _1f3=mb.height;
var _1f4=dojo.html.getAbsolutePosition(_1e9,true,_1eb);
_1e9.style.display=_1f0;
for(var _1f5 in _1ec){
var pos,_1f7,_1f8;
var _1f9=_1ec[_1f5];
_1f7=_1f4.x+(_1f5.charAt(1)=="L"?0:_1f2);
_1f8=_1f4.y+(_1f5.charAt(0)=="T"?0:_1f3);
pos=dojo.html.placeOnScreen(node,_1f7,_1f8,_1ea,true,_1f9,true);
if(pos.dist==0){
best=pos;
break;
}else{
if(_1ef>pos.dist){
_1ef=pos.dist;
best=pos;
}
}
}
if(!_1ed){
node.style.left=best.left+"px";
node.style.top=best.top+"px";
}
return best;
};
dojo.html.scrollIntoView=function(node){
if(!node){
return;
}
if(dojo.render.html.ie){
if(dojo.html.getBorderBox(node.parentNode).height<=node.parentNode.scrollHeight){
node.scrollIntoView(false);
}
}else{
if(dojo.render.html.mozilla){
node.scrollIntoView(false);
}else{
var _1fb=node.parentNode;
var _1fc=_1fb.scrollTop+dojo.html.getBorderBox(_1fb).height;
var _1fd=node.offsetTop+dojo.html.getMarginBox(node).height;
if(_1fc<_1fd){
_1fb.scrollTop+=(_1fd-_1fc);
}else{
if(_1fb.scrollTop>node.offsetTop){
_1fb.scrollTop-=(_1fb.scrollTop-node.offsetTop);
}
}
}
}
};
dojo.provide("dojo.gfx.color");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.array");
dojo.gfx.color.Color=function(r,g,b,a){
if(dojo.lang.isArray(r)){
this.r=r[0];
this.g=r[1];
this.b=r[2];
this.a=r[3]||1;
}else{
if(dojo.lang.isString(r)){
var rgb=dojo.gfx.color.extractRGB(r);
this.r=rgb[0];
this.g=rgb[1];
this.b=rgb[2];
this.a=g||1;
}else{
if(r instanceof dojo.gfx.color.Color){
this.r=r.r;
this.b=r.b;
this.g=r.g;
this.a=r.a;
}else{
this.r=r;
this.g=g;
this.b=b;
this.a=a;
}
}
}
};
dojo.gfx.color.Color.fromArray=function(arr){
return new dojo.gfx.color.Color(arr[0],arr[1],arr[2],arr[3]);
};
dojo.extend(dojo.gfx.color.Color,{toRgb:function(_204){
if(_204){
return this.toRgba();
}else{
return [this.r,this.g,this.b];
}
},toRgba:function(){
return [this.r,this.g,this.b,this.a];
},toHex:function(){
return dojo.gfx.color.rgb2hex(this.toRgb());
},toCss:function(){
return "rgb("+this.toRgb().join()+")";
},toString:function(){
return this.toHex();
},blend:function(_205,_206){
var rgb=null;
if(dojo.lang.isArray(_205)){
rgb=_205;
}else{
if(_205 instanceof dojo.gfx.color.Color){
rgb=_205.toRgb();
}else{
rgb=new dojo.gfx.color.Color(_205).toRgb();
}
}
return dojo.gfx.color.blend(this.toRgb(),rgb,_206);
}});
dojo.gfx.color.named={white:[255,255,255],black:[0,0,0],red:[255,0,0],green:[0,255,0],lime:[0,255,0],blue:[0,0,255],navy:[0,0,128],gray:[128,128,128],silver:[192,192,192]};
dojo.gfx.color.blend=function(a,b,_20a){
if(typeof a=="string"){
return dojo.gfx.color.blendHex(a,b,_20a);
}
if(!_20a){
_20a=0;
}
_20a=Math.min(Math.max(-1,_20a),1);
_20a=((_20a+1)/2);
var c=[];
for(var x=0;x<3;x++){
c[x]=parseInt(b[x]+((a[x]-b[x])*_20a));
}
return c;
};
dojo.gfx.color.blendHex=function(a,b,_20f){
return dojo.gfx.color.rgb2hex(dojo.gfx.color.blend(dojo.gfx.color.hex2rgb(a),dojo.gfx.color.hex2rgb(b),_20f));
};
dojo.gfx.color.extractRGB=function(_210){
var hex="0123456789abcdef";
_210=_210.toLowerCase();
if(_210.indexOf("rgb")==0){
var _212=_210.match(/rgba*\((\d+), *(\d+), *(\d+)/i);
var ret=_212.splice(1,3);
return ret;
}else{
var _214=dojo.gfx.color.hex2rgb(_210);
if(_214){
return _214;
}else{
return dojo.gfx.color.named[_210]||[255,255,255];
}
}
};
dojo.gfx.color.hex2rgb=function(hex){
var _216="0123456789ABCDEF";
var rgb=new Array(3);
if(hex.indexOf("#")==0){
hex=hex.substring(1);
}
hex=hex.toUpperCase();
if(hex.replace(new RegExp("["+_216+"]","g"),"")!=""){
return null;
}
if(hex.length==3){
rgb[0]=hex.charAt(0)+hex.charAt(0);
rgb[1]=hex.charAt(1)+hex.charAt(1);
rgb[2]=hex.charAt(2)+hex.charAt(2);
}else{
rgb[0]=hex.substring(0,2);
rgb[1]=hex.substring(2,4);
rgb[2]=hex.substring(4);
}
for(var i=0;i<rgb.length;i++){
rgb[i]=_216.indexOf(rgb[i].charAt(0))*16+_216.indexOf(rgb[i].charAt(1));
}
return rgb;
};
dojo.gfx.color.rgb2hex=function(r,g,b){
if(dojo.lang.isArray(r)){
g=r[1]||0;
b=r[2]||0;
r=r[0]||0;
}
var ret=dojo.lang.map([r,g,b],function(x){
x=new Number(x);
var s=x.toString(16);
while(s.length<2){
s="0"+s;
}
return s;
});
ret.unshift("#");
return ret.join("");
};
dojo.provide("dojo.lfx.Animation");
dojo.require("dojo.lang.func");
dojo.lfx.Line=function(_21f,end){
this.start=_21f;
this.end=end;
if(dojo.lang.isArray(_21f)){
var diff=[];
dojo.lang.forEach(this.start,function(s,i){
diff[i]=this.end[i]-s;
},this);
this.getValue=function(n){
var res=[];
dojo.lang.forEach(this.start,function(s,i){
res[i]=(diff[i]*n)+s;
},this);
return res;
};
}else{
var diff=end-_21f;
this.getValue=function(n){
return (diff*n)+this.start;
};
}
};
if((dojo.render.html.khtml)&&(!dojo.render.html.safari)){
dojo.lfx.easeDefault=function(n){
return (parseFloat("0.5")+((Math.sin((n+parseFloat("1.5"))*Math.PI))/2));
};
}else{
dojo.lfx.easeDefault=function(n){
return (0.5+((Math.sin((n+1.5)*Math.PI))/2));
};
}
dojo.lfx.easeIn=function(n){
return Math.pow(n,3);
};
dojo.lfx.easeOut=function(n){
return (1-Math.pow(1-n,3));
};
dojo.lfx.easeInOut=function(n){
return ((3*Math.pow(n,2))-(2*Math.pow(n,3)));
};
dojo.lfx.IAnimation=function(){
};
dojo.lang.extend(dojo.lfx.IAnimation,{curve:null,duration:1000,easing:null,repeatCount:0,rate:10,handler:null,beforeBegin:null,onBegin:null,onAnimate:null,onEnd:null,onPlay:null,onPause:null,onStop:null,play:null,pause:null,stop:null,connect:function(evt,_22f,_230){
if(!_230){
_230=_22f;
_22f=this;
}
_230=dojo.lang.hitch(_22f,_230);
var _231=this[evt]||function(){
};
this[evt]=function(){
var ret=_231.apply(this,arguments);
_230.apply(this,arguments);
return ret;
};
return this;
},fire:function(evt,args){
if(this[evt]){
this[evt].apply(this,(args||[]));
}
return this;
},repeat:function(_235){
this.repeatCount=_235;
return this;
},_active:false,_paused:false});
dojo.lfx.Animation=function(_236,_237,_238,_239,_23a,rate){
dojo.lfx.IAnimation.call(this);
if(dojo.lang.isNumber(_236)||(!_236&&_237.getValue)){
rate=_23a;
_23a=_239;
_239=_238;
_238=_237;
_237=_236;
_236=null;
}else{
if(_236.getValue||dojo.lang.isArray(_236)){
rate=_239;
_23a=_238;
_239=_237;
_238=_236;
_237=null;
_236=null;
}
}
if(dojo.lang.isArray(_238)){
this.curve=new dojo.lfx.Line(_238[0],_238[1]);
}else{
this.curve=_238;
}
if(_237!=null&&_237>0){
this.duration=_237;
}
if(_23a){
this.repeatCount=_23a;
}
if(rate){
this.rate=rate;
}
if(_236){
dojo.lang.forEach(["handler","beforeBegin","onBegin","onEnd","onPlay","onStop","onAnimate"],function(item){
if(_236[item]){
this.connect(item,_236[item]);
}
},this);
}
if(_239&&dojo.lang.isFunction(_239)){
this.easing=_239;
}
};
dojo.inherits(dojo.lfx.Animation,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Animation,{_startTime:null,_endTime:null,_timer:null,_percent:0,_startRepeatCount:0,play:function(_23d,_23e){
if(_23e){
clearTimeout(this._timer);
this._active=false;
this._paused=false;
this._percent=0;
}else{
if(this._active&&!this._paused){
return this;
}
}
this.fire("handler",["beforeBegin"]);
this.fire("beforeBegin");
if(_23d>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_23e);
}),_23d);
return this;
}
this._startTime=new Date().valueOf();
if(this._paused){
this._startTime-=(this.duration*this._percent/100);
}
this._endTime=this._startTime+this.duration;
this._active=true;
this._paused=false;
var step=this._percent/100;
var _240=this.curve.getValue(step);
if(this._percent==0){
if(!this._startRepeatCount){
this._startRepeatCount=this.repeatCount;
}
this.fire("handler",["begin",_240]);
this.fire("onBegin",[_240]);
}
this.fire("handler",["play",_240]);
this.fire("onPlay",[_240]);
this._cycle();
return this;
},pause:function(){
clearTimeout(this._timer);
if(!this._active){
return this;
}
this._paused=true;
var _241=this.curve.getValue(this._percent/100);
this.fire("handler",["pause",_241]);
this.fire("onPause",[_241]);
return this;
},gotoPercent:function(pct,_243){
clearTimeout(this._timer);
this._active=true;
this._paused=true;
this._percent=pct;
if(_243){
this.play();
}
return this;
},stop:function(_244){
clearTimeout(this._timer);
var step=this._percent/100;
if(_244){
step=1;
}
var _246=this.curve.getValue(step);
this.fire("handler",["stop",_246]);
this.fire("onStop",[_246]);
this._active=false;
this._paused=false;
return this;
},status:function(){
if(this._active){
return this._paused?"paused":"playing";
}else{
return "stopped";
}
return this;
},_cycle:function(){
clearTimeout(this._timer);
if(this._active){
var curr=new Date().valueOf();
var step=(curr-this._startTime)/(this._endTime-this._startTime);
if(step>=1){
step=1;
this._percent=100;
}else{
this._percent=step*100;
}
if((this.easing)&&(dojo.lang.isFunction(this.easing))){
step=this.easing(step);
}
var _249=this.curve.getValue(step);
this.fire("handler",["animate",_249]);
this.fire("onAnimate",[_249]);
if(step<1){
this._timer=setTimeout(dojo.lang.hitch(this,"_cycle"),this.rate);
}else{
this._active=false;
this.fire("handler",["end"]);
this.fire("onEnd");
if(this.repeatCount>0){
this.repeatCount--;
this.play(null,true);
}else{
if(this.repeatCount==-1){
this.play(null,true);
}else{
if(this._startRepeatCount){
this.repeatCount=this._startRepeatCount;
this._startRepeatCount=0;
}
}
}
}
}
return this;
}});
dojo.lfx.Combine=function(_24a){
dojo.lfx.IAnimation.call(this);
this._anims=[];
this._animsEnded=0;
var _24b=arguments;
if(_24b.length==1&&(dojo.lang.isArray(_24b[0])||dojo.lang.isArrayLike(_24b[0]))){
_24b=_24b[0];
}
dojo.lang.forEach(_24b,function(anim){
this._anims.push(anim);
anim.connect("onEnd",dojo.lang.hitch(this,"_onAnimsEnded"));
},this);
};
dojo.inherits(dojo.lfx.Combine,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Combine,{_animsEnded:0,play:function(_24d,_24e){
if(!this._anims.length){
return this;
}
this.fire("beforeBegin");
if(_24d>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_24e);
}),_24d);
return this;
}
if(_24e||this._anims[0].percent==0){
this.fire("onBegin");
}
this.fire("onPlay");
this._animsCall("play",null,_24e);
return this;
},pause:function(){
this.fire("onPause");
this._animsCall("pause");
return this;
},stop:function(_24f){
this.fire("onStop");
this._animsCall("stop",_24f);
return this;
},_onAnimsEnded:function(){
this._animsEnded++;
if(this._animsEnded>=this._anims.length){
this.fire("onEnd");
}
return this;
},_animsCall:function(_250){
var args=[];
if(arguments.length>1){
for(var i=1;i<arguments.length;i++){
args.push(arguments[i]);
}
}
var _253=this;
dojo.lang.forEach(this._anims,function(anim){
anim[_250](args);
},_253);
return this;
}});
dojo.lfx.Chain=function(_255){
dojo.lfx.IAnimation.call(this);
this._anims=[];
this._currAnim=-1;
var _256=arguments;
if(_256.length==1&&(dojo.lang.isArray(_256[0])||dojo.lang.isArrayLike(_256[0]))){
_256=_256[0];
}
var _257=this;
dojo.lang.forEach(_256,function(anim,i,_25a){
this._anims.push(anim);
if(i<_25a.length-1){
anim.connect("onEnd",dojo.lang.hitch(this,"_playNext"));
}else{
anim.connect("onEnd",dojo.lang.hitch(this,function(){
this.fire("onEnd");
}));
}
},this);
};
dojo.inherits(dojo.lfx.Chain,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Chain,{_currAnim:-1,play:function(_25b,_25c){
if(!this._anims.length){
return this;
}
if(_25c||!this._anims[this._currAnim]){
this._currAnim=0;
}
var _25d=this._anims[this._currAnim];
this.fire("beforeBegin");
if(_25b>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_25c);
}),_25b);
return this;
}
if(_25d){
if(this._currAnim==0){
this.fire("handler",["begin",this._currAnim]);
this.fire("onBegin",[this._currAnim]);
}
this.fire("onPlay",[this._currAnim]);
_25d.play(null,_25c);
}
return this;
},pause:function(){
if(this._anims[this._currAnim]){
this._anims[this._currAnim].pause();
this.fire("onPause",[this._currAnim]);
}
return this;
},playPause:function(){
if(this._anims.length==0){
return this;
}
if(this._currAnim==-1){
this._currAnim=0;
}
var _25e=this._anims[this._currAnim];
if(_25e){
if(!_25e._active||_25e._paused){
this.play();
}else{
this.pause();
}
}
return this;
},stop:function(){
var _25f=this._anims[this._currAnim];
if(_25f){
_25f.stop();
this.fire("onStop",[this._currAnim]);
}
return _25f;
},_playNext:function(){
if(this._currAnim==-1||this._anims.length==0){
return this;
}
this._currAnim++;
if(this._anims[this._currAnim]){
this._anims[this._currAnim].play(null,true);
}
return this;
}});
dojo.lfx.combine=function(_260){
var _261=arguments;
if(dojo.lang.isArray(arguments[0])){
_261=arguments[0];
}
if(_261.length==1){
return _261[0];
}
return new dojo.lfx.Combine(_261);
};
dojo.lfx.chain=function(_262){
var _263=arguments;
if(dojo.lang.isArray(arguments[0])){
_263=arguments[0];
}
if(_263.length==1){
return _263[0];
}
return new dojo.lfx.Chain(_263);
};
dojo.require("dojo.html.style");
dojo.provide("dojo.html.color");
dojo.require("dojo.lang.common");
dojo.html.getBackgroundColor=function(node){
node=dojo.byId(node);
var _265;
do{
_265=dojo.html.getStyle(node,"background-color");
if(_265.toLowerCase()=="rgba(0, 0, 0, 0)"){
_265="transparent";
}
if(node==document.getElementsByTagName("body")[0]){
node=null;
break;
}
node=node.parentNode;
}while(node&&dojo.lang.inArray(["transparent",""],_265));
if(_265=="transparent"){
_265=[255,255,255,0];
}else{
_265=dojo.gfx.color.extractRGB(_265);
}
return _265;
};
dojo.provide("dojo.lfx.html");
dojo.require("dojo.lang.array");
dojo.require("dojo.html.display");
dojo.lfx.html._byId=function(_266){
if(!_266){
return [];
}
if(dojo.lang.isArrayLike(_266)){
if(!_266.alreadyChecked){
var n=[];
dojo.lang.forEach(_266,function(node){
n.push(dojo.byId(node));
});
n.alreadyChecked=true;
return n;
}else{
return _266;
}
}else{
var n=[];
n.push(dojo.byId(_266));
n.alreadyChecked=true;
return n;
}
};
dojo.lfx.html.propertyAnimation=function(_269,_26a,_26b,_26c,_26d){
_269=dojo.lfx.html._byId(_269);
var _26e={"propertyMap":_26a,"nodes":_269,"duration":_26b,"easing":_26c||dojo.lfx.easeDefault};
var _26f=function(args){
if(args.nodes.length==1){
var pm=args.propertyMap;
if(!dojo.lang.isArray(args.propertyMap)){
var parr=[];
for(var _273 in pm){
pm[_273].property=_273;
parr.push(pm[_273]);
}
pm=args.propertyMap=parr;
}
dojo.lang.forEach(pm,function(prop){
if(dj_undef("start",prop)){
if(prop.property!="opacity"){
prop.start=parseInt(dojo.html.getComputedStyle(args.nodes[0],prop.property));
}else{
prop.start=dojo.html.getOpacity(args.nodes[0]);
}
}
});
}
};
var _275=function(_276){
var _277=[];
dojo.lang.forEach(_276,function(c){
_277.push(Math.round(c));
});
return _277;
};
var _279=function(n,_27b){
n=dojo.byId(n);
if(!n||!n.style){
return;
}
for(var s in _27b){
try{
if(s=="opacity"){
dojo.html.setOpacity(n,_27b[s]);
}else{
n.style[s]=_27b[s];
}
}
catch(e){
dojo.debug(e);
}
}
};
var _27d=function(_27e){
this._properties=_27e;
this.diffs=new Array(_27e.length);
dojo.lang.forEach(_27e,function(prop,i){
if(dojo.lang.isFunction(prop.start)){
prop.start=prop.start(prop,i);
}
if(dojo.lang.isFunction(prop.end)){
prop.end=prop.end(prop,i);
}
if(dojo.lang.isArray(prop.start)){
this.diffs[i]=null;
}else{
if(prop.start instanceof dojo.gfx.color.Color){
prop.startRgb=prop.start.toRgb();
prop.endRgb=prop.end.toRgb();
}else{
this.diffs[i]=prop.end-prop.start;
}
}
},this);
this.getValue=function(n){
var ret={};
dojo.lang.forEach(this._properties,function(prop,i){
var _285=null;
if(dojo.lang.isArray(prop.start)){
}else{
if(prop.start instanceof dojo.gfx.color.Color){
_285=(prop.units||"rgb")+"(";
for(var j=0;j<prop.startRgb.length;j++){
_285+=Math.round(((prop.endRgb[j]-prop.startRgb[j])*n)+prop.startRgb[j])+(j<prop.startRgb.length-1?",":"");
}
_285+=")";
}else{
_285=((this.diffs[i])*n)+prop.start+(prop.property!="opacity"?prop.units||"px":"");
}
}
ret[dojo.html.toCamelCase(prop.property)]=_285;
},this);
return ret;
};
};
var anim=new dojo.lfx.Animation({beforeBegin:function(){
_26f(_26e);
anim.curve=new _27d(_26e.propertyMap);
},onAnimate:function(_288){
dojo.lang.forEach(_26e.nodes,function(node){
_279(node,_288);
});
}},_26e.duration,null,_26e.easing);
if(_26d){
for(var x in _26d){
if(dojo.lang.isFunction(_26d[x])){
anim.connect(x,anim,_26d[x]);
}
}
}
return anim;
};
dojo.lfx.html._makeFadeable=function(_28b){
var _28c=function(node){
if(dojo.render.html.ie){
if((node.style.zoom.length==0)&&(dojo.html.getStyle(node,"zoom")=="normal")){
node.style.zoom="1";
}
if((node.style.width.length==0)&&(dojo.html.getStyle(node,"width")=="auto")){
node.style.width="auto";
}
}
};
if(dojo.lang.isArrayLike(_28b)){
dojo.lang.forEach(_28b,_28c);
}else{
_28c(_28b);
}
};
dojo.lfx.html.fade=function(_28e,_28f,_290,_291,_292){
_28e=dojo.lfx.html._byId(_28e);
var _293={property:"opacity"};
if(!dj_undef("start",_28f)){
_293.start=_28f.start;
}else{
_293.start=function(){
return dojo.html.getOpacity(_28e[0]);
};
}
if(!dj_undef("end",_28f)){
_293.end=_28f.end;
}else{
dojo.raise("dojo.lfx.html.fade needs an end value");
}
var anim=dojo.lfx.propertyAnimation(_28e,[_293],_290,_291);
anim.connect("beforeBegin",function(){
dojo.lfx.html._makeFadeable(_28e);
});
if(_292){
anim.connect("onEnd",function(){
_292(_28e,anim);
});
}
return anim;
};
dojo.lfx.html.fadeIn=function(_295,_296,_297,_298){
return dojo.lfx.html.fade(_295,{end:1},_296,_297,_298);
};
dojo.lfx.html.fadeOut=function(_299,_29a,_29b,_29c){
return dojo.lfx.html.fade(_299,{end:0},_29a,_29b,_29c);
};
dojo.lfx.html.fadeShow=function(_29d,_29e,_29f,_2a0){
_29d=dojo.lfx.html._byId(_29d);
dojo.lang.forEach(_29d,function(node){
dojo.html.setOpacity(node,0);
});
var anim=dojo.lfx.html.fadeIn(_29d,_29e,_29f,_2a0);
anim.connect("beforeBegin",function(){
if(dojo.lang.isArrayLike(_29d)){
dojo.lang.forEach(_29d,dojo.html.show);
}else{
dojo.html.show(_29d);
}
});
return anim;
};
dojo.lfx.html.fadeHide=function(_2a3,_2a4,_2a5,_2a6){
var anim=dojo.lfx.html.fadeOut(_2a3,_2a4,_2a5,function(){
if(dojo.lang.isArrayLike(_2a3)){
dojo.lang.forEach(_2a3,dojo.html.hide);
}else{
dojo.html.hide(_2a3);
}
if(_2a6){
_2a6(_2a3,anim);
}
});
return anim;
};
dojo.lfx.html.wipeIn=function(_2a8,_2a9,_2aa,_2ab){
_2a8=dojo.lfx.html._byId(_2a8);
var _2ac=[];
dojo.lang.forEach(_2a8,function(node){
var _2ae={};
var _2af,_2b0,_2b1;
with(node.style){
_2af=top;
_2b0=left;
_2b1=position;
top="-9999px";
left="-9999px";
position="absolute";
display="";
}
var _2b2=dojo.html.getBorderBox(node).height;
with(node.style){
top=_2af;
left=_2b0;
position=_2b1;
display="none";
}
var anim=dojo.lfx.propertyAnimation(node,{"height":{start:1,end:function(){
return _2b2;
}}},_2a9,_2aa);
anim.connect("beforeBegin",function(){
_2ae.overflow=node.style.overflow;
_2ae.height=node.style.height;
with(node.style){
overflow="hidden";
height="1px";
}
dojo.html.show(node);
});
anim.connect("onEnd",function(){
with(node.style){
overflow=_2ae.overflow;
height=_2ae.height;
}
if(_2ab){
_2ab(node,anim);
}
});
_2ac.push(anim);
});
return dojo.lfx.combine(_2ac);
};
dojo.lfx.html.wipeOut=function(_2b4,_2b5,_2b6,_2b7){
_2b4=dojo.lfx.html._byId(_2b4);
var _2b8=[];
dojo.lang.forEach(_2b4,function(node){
var _2ba={};
var anim=dojo.lfx.propertyAnimation(node,{"height":{start:function(){
return dojo.html.getContentBox(node).height;
},end:1}},_2b5,_2b6,{"beforeBegin":function(){
_2ba.overflow=node.style.overflow;
_2ba.height=node.style.height;
with(node.style){
overflow="hidden";
}
dojo.html.show(node);
},"onEnd":function(){
dojo.html.hide(node);
with(node.style){
overflow=_2ba.overflow;
height=_2ba.height;
}
if(_2b7){
_2b7(node,anim);
}
}});
_2b8.push(anim);
});
return dojo.lfx.combine(_2b8);
};
dojo.lfx.html.slideTo=function(_2bc,_2bd,_2be,_2bf,_2c0){
_2bc=dojo.lfx.html._byId(_2bc);
var _2c1=[];
var _2c2=dojo.html.getComputedStyle;
if(dojo.lang.isArray(_2bd)){
dojo.deprecated("dojo.lfx.html.slideTo(node, array)","use dojo.lfx.html.slideTo(node, {top: value, left: value});","0.5");
_2bd={top:_2bd[0],left:_2bd[1]};
}
dojo.lang.forEach(_2bc,function(node){
var top=null;
var left=null;
var init=(function(){
var _2c7=node;
return function(){
var pos=_2c2(_2c7,"position");
top=(pos=="absolute"?node.offsetTop:parseInt(_2c2(node,"top"))||0);
left=(pos=="absolute"?node.offsetLeft:parseInt(_2c2(node,"left"))||0);
if(!dojo.lang.inArray(["absolute","relative"],pos)){
var ret=dojo.html.abs(_2c7,true);
dojo.html.setStyleAttributes(_2c7,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");
top=ret.y;
left=ret.x;
}
};
})();
init();
var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:(_2bd.top||0)},"left":{start:left,end:(_2bd.left||0)}},_2be,_2bf,{"beforeBegin":init});
if(_2c0){
anim.connect("onEnd",function(){
_2c0(_2bc,anim);
});
}
_2c1.push(anim);
});
return dojo.lfx.combine(_2c1);
};
dojo.lfx.html.slideBy=function(_2cb,_2cc,_2cd,_2ce,_2cf){
_2cb=dojo.lfx.html._byId(_2cb);
var _2d0=[];
var _2d1=dojo.html.getComputedStyle;
if(dojo.lang.isArray(_2cc)){
dojo.deprecated("dojo.lfx.html.slideBy(node, array)","use dojo.lfx.html.slideBy(node, {top: value, left: value});","0.5");
_2cc={top:_2cc[0],left:_2cc[1]};
}
dojo.lang.forEach(_2cb,function(node){
var top=null;
var left=null;
var init=(function(){
var _2d6=node;
return function(){
var pos=_2d1(_2d6,"position");
top=(pos=="absolute"?node.offsetTop:parseInt(_2d1(node,"top"))||0);
left=(pos=="absolute"?node.offsetLeft:parseInt(_2d1(node,"left"))||0);
if(!dojo.lang.inArray(["absolute","relative"],pos)){
var ret=dojo.html.abs(_2d6,true);
dojo.html.setStyleAttributes(_2d6,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");
top=ret.y;
left=ret.x;
}
};
})();
init();
var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:top+(_2cc.top||0)},"left":{start:left,end:left+(_2cc.left||0)}},_2cd,_2ce).connect("beforeBegin",init);
if(_2cf){
anim.connect("onEnd",function(){
_2cf(_2cb,anim);
});
}
_2d0.push(anim);
});
return dojo.lfx.combine(_2d0);
};
dojo.lfx.html.explode=function(_2da,_2db,_2dc,_2dd,_2de){
var h=dojo.html;
_2da=dojo.byId(_2da);
_2db=dojo.byId(_2db);
var _2e0=h.toCoordinateObject(_2da,true);
var _2e1=document.createElement("div");
h.copyStyle(_2e1,_2db);
if(_2db.explodeClassName){
_2e1.className=_2db.explodeClassName;
}
with(_2e1.style){
position="absolute";
display="none";
var _2e2=h.getStyle(_2da,"background-color");
backgroundColor=_2e2?_2e2.toLowerCase():"transparent";
backgroundColor=(backgroundColor=="transparent")?"rgb(221, 221, 221)":backgroundColor;
}
dojo.body().appendChild(_2e1);
with(_2db.style){
visibility="hidden";
display="block";
}
var _2e3=h.toCoordinateObject(_2db,true);
with(_2db.style){
display="none";
visibility="visible";
}
var _2e4={opacity:{start:0.5,end:1}};
dojo.lang.forEach(["height","width","top","left"],function(type){
_2e4[type]={start:_2e0[type],end:_2e3[type]};
});
var anim=new dojo.lfx.propertyAnimation(_2e1,_2e4,_2dc,_2dd,{"beforeBegin":function(){
h.setDisplay(_2e1,"block");
},"onEnd":function(){
h.setDisplay(_2db,"block");
_2e1.parentNode.removeChild(_2e1);
}});
if(_2de){
anim.connect("onEnd",function(){
_2de(_2db,anim);
});
}
return anim;
};
dojo.lfx.html.implode=function(_2e7,end,_2e9,_2ea,_2eb){
var h=dojo.html;
_2e7=dojo.byId(_2e7);
end=dojo.byId(end);
var _2ed=dojo.html.toCoordinateObject(_2e7,true);
var _2ee=dojo.html.toCoordinateObject(end,true);
var _2ef=document.createElement("div");
dojo.html.copyStyle(_2ef,_2e7);
if(_2e7.explodeClassName){
_2ef.className=_2e7.explodeClassName;
}
dojo.html.setOpacity(_2ef,0.3);
with(_2ef.style){
position="absolute";
display="none";
backgroundColor=h.getStyle(_2e7,"background-color").toLowerCase();
}
dojo.body().appendChild(_2ef);
var _2f0={opacity:{start:1,end:0.5}};
dojo.lang.forEach(["height","width","top","left"],function(type){
_2f0[type]={start:_2ed[type],end:_2ee[type]};
});
var anim=new dojo.lfx.propertyAnimation(_2ef,_2f0,_2e9,_2ea,{"beforeBegin":function(){
dojo.html.hide(_2e7);
dojo.html.show(_2ef);
},"onEnd":function(){
_2ef.parentNode.removeChild(_2ef);
}});
if(_2eb){
anim.connect("onEnd",function(){
_2eb(_2e7,anim);
});
}
return anim;
};
dojo.lfx.html.highlight=function(_2f3,_2f4,_2f5,_2f6,_2f7){
_2f3=dojo.lfx.html._byId(_2f3);
var _2f8=[];
dojo.lang.forEach(_2f3,function(node){
var _2fa=dojo.html.getBackgroundColor(node);
var bg=dojo.html.getStyle(node,"background-color").toLowerCase();
var _2fc=dojo.html.getStyle(node,"background-image");
var _2fd=(bg=="transparent"||bg=="rgba(0, 0, 0, 0)");
while(_2fa.length>3){
_2fa.pop();
}
var rgb=new dojo.gfx.color.Color(_2f4);
var _2ff=new dojo.gfx.color.Color(_2fa);
var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:rgb,end:_2ff}},_2f5,_2f6,{"beforeBegin":function(){
if(_2fc){
node.style.backgroundImage="none";
}
node.style.backgroundColor="rgb("+rgb.toRgb().join(",")+")";
},"onEnd":function(){
if(_2fc){
node.style.backgroundImage=_2fc;
}
if(_2fd){
node.style.backgroundColor="transparent";
}
if(_2f7){
_2f7(node,anim);
}
}});
_2f8.push(anim);
});
return dojo.lfx.combine(_2f8);
};
dojo.lfx.html.unhighlight=function(_301,_302,_303,_304,_305){
_301=dojo.lfx.html._byId(_301);
var _306=[];
dojo.lang.forEach(_301,function(node){
var _308=new dojo.gfx.color.Color(dojo.html.getBackgroundColor(node));
var rgb=new dojo.gfx.color.Color(_302);
var _30a=dojo.html.getStyle(node,"background-image");
var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:_308,end:rgb}},_303,_304,{"beforeBegin":function(){
if(_30a){
node.style.backgroundImage="none";
}
node.style.backgroundColor="rgb("+_308.toRgb().join(",")+")";
},"onEnd":function(){
if(_305){
_305(node,anim);
}
}});
_306.push(anim);
});
return dojo.lfx.combine(_306);
};
dojo.lang.mixin(dojo.lfx,dojo.lfx.html);
dojo.kwCompoundRequire({browser:["dojo.lfx.html"],dashboard:["dojo.lfx.html"]});
dojo.provide("dojo.lfx.*");
dojo.provide("dojo.lfx.toggle");
dojo.lfx.toggle.plain={show:function(node,_30d,_30e,_30f){
dojo.html.show(node);
if(dojo.lang.isFunction(_30f)){
_30f();
}
},hide:function(node,_311,_312,_313){
dojo.html.hide(node);
if(dojo.lang.isFunction(_313)){
_313();
}
}};
dojo.lfx.toggle.fade={show:function(node,_315,_316,_317){
dojo.lfx.fadeShow(node,_315,_316,_317).play();
},hide:function(node,_319,_31a,_31b){
dojo.lfx.fadeHide(node,_319,_31a,_31b).play();
}};
dojo.lfx.toggle.wipe={show:function(node,_31d,_31e,_31f){
dojo.lfx.wipeIn(node,_31d,_31e,_31f).play();
},hide:function(node,_321,_322,_323){
dojo.lfx.wipeOut(node,_321,_322,_323).play();
}};
dojo.lfx.toggle.explode={show:function(node,_325,_326,_327,_328){
dojo.lfx.explode(_328||{x:0,y:0,width:0,height:0},node,_325,_326,_327).play();
},hide:function(node,_32a,_32b,_32c,_32d){
dojo.lfx.implode(node,_32d||{x:0,y:0,width:0,height:0},_32a,_32b,_32c).play();
}};
dojo.provide("dojo.widget.HtmlWidget");
dojo.require("dojo.html.display");
dojo.require("dojo.lang.extras");
dojo.require("dojo.lang.func");
dojo.declare("dojo.widget.HtmlWidget",dojo.widget.DomWidget,{templateCssPath:null,templatePath:null,lang:"",toggle:"plain",toggleDuration:150,initialize:function(args,frag){
},postMixInProperties:function(args,frag){
if(this.lang===""){
this.lang=null;
}
this.toggleObj=dojo.lfx.toggle[this.toggle.toLowerCase()]||dojo.lfx.toggle.plain;
},createNodesFromText:function(txt,wrap){
return dojo.html.createNodesFromText(txt,wrap);
},destroyRendering:function(_334){
try{
if(this.bgIframe){
this.bgIframe.remove();
delete this.bgIframe;
}
if(!_334&&this.domNode){
dojo.event.browser.clean(this.domNode);
}
dojo.widget.HtmlWidget.superclass.destroyRendering.call(this);
}
catch(e){
}
},isShowing:function(){
return dojo.html.isShowing(this.domNode);
},toggleShowing:function(){
if(this.isShowing()){
this.hide();
}else{
this.show();
}
},show:function(){
if(this.isShowing()){
return;
}
this.animationInProgress=true;
this.toggleObj.show(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onShow),this.explodeSrc);
},onShow:function(){
this.animationInProgress=false;
this.checkSize();
},hide:function(){
if(!this.isShowing()){
return;
}
this.animationInProgress=true;
this.toggleObj.hide(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onHide),this.explodeSrc);
},onHide:function(){
this.animationInProgress=false;
},_isResized:function(w,h){
if(!this.isShowing()){
return false;
}
var wh=dojo.html.getMarginBox(this.domNode);
var _338=w||wh.width;
var _339=h||wh.height;
if(this.width==_338&&this.height==_339){
return false;
}
this.width=_338;
this.height=_339;
return true;
},checkSize:function(){
if(!this._isResized()){
return;
}
this.onResized();
},resizeTo:function(w,h){
dojo.html.setMarginBox(this.domNode,{width:w,height:h});
if(this.isShowing()){
this.onResized();
}
},resizeSoon:function(){
if(this.isShowing()){
dojo.lang.setTimeout(this,this.onResized,0);
}
},onResized:function(){
dojo.lang.forEach(this.children,function(_33c){
if(_33c.checkSize){
_33c.checkSize();
}
});
}});
dojo.kwCompoundRequire({common:["dojo.xml.Parse","dojo.widget.Widget","dojo.widget.Parse","dojo.widget.Manager"],browser:["dojo.widget.DomWidget","dojo.widget.HtmlWidget"],dashboard:["dojo.widget.DomWidget","dojo.widget.HtmlWidget"],svg:["dojo.widget.SvgWidget"],rhino:["dojo.widget.SwtWidget"]});
dojo.provide("dojo.widget.*");
dojo.kwCompoundRequire({common:["dojo.io.common"],rhino:["dojo.io.RhinoIO"],browser:["dojo.io.BrowserIO","dojo.io.cookie"],dashboard:["dojo.io.BrowserIO","dojo.io.cookie"]});
dojo.provide("dojo.io.*");
dojo.provide("dojo.widget.ContentPane");
dojo.require("dojo.string");
dojo.require("dojo.string.extras");
dojo.require("dojo.html.style");
dojo.widget.defineWidget("dojo.widget.ContentPane",dojo.widget.HtmlWidget,function(){
this._styleNodes=[];
this._onLoadStack=[];
this._onUnloadStack=[];
this._callOnUnload=false;
this._ioBindObj;
this.scriptScope;
this.bindArgs={};
},{isContainer:true,adjustPaths:true,href:"",extractContent:true,parseContent:true,cacheContent:true,preload:false,refreshOnShow:false,handler:"",executeScripts:false,scriptSeparation:true,loadingMessage:"Loading...",isLoaded:false,postCreate:function(args,frag,_33f){
if(this.handler!==""){
this.setHandler(this.handler);
}
if(this.isShowing()||this.preload){
this.loadContents();
}
},show:function(){
if(this.refreshOnShow){
this.refresh();
}else{
this.loadContents();
}
dojo.widget.ContentPane.superclass.show.call(this);
},refresh:function(){
this.isLoaded=false;
this.loadContents();
},loadContents:function(){
if(this.isLoaded){
return;
}
if(dojo.lang.isFunction(this.handler)){
this._runHandler();
}else{
if(this.href!=""){
this._downloadExternalContent(this.href,this.cacheContent&&!this.refreshOnShow);
}
}
},setUrl:function(url){
this.href=url;
this.isLoaded=false;
if(this.preload||this.isShowing()){
this.loadContents();
}
},abort:function(){
var bind=this._ioBindObj;
if(!bind||!bind.abort){
return;
}
bind.abort();
delete this._ioBindObj;
},_downloadExternalContent:function(url,_343){
this.abort();
this._handleDefaults(this.loadingMessage,"onDownloadStart");
var self=this;
this._ioBindObj=dojo.io.bind(this._cacheSetting({url:url,mimetype:"text/html",handler:function(type,data,xhr){
delete self._ioBindObj;
if(type=="load"){
self.onDownloadEnd.call(self,url,data);
}else{
var e={responseText:xhr.responseText,status:xhr.status,statusText:xhr.statusText,responseHeaders:xhr.getAllResponseHeaders(),text:"Error loading '"+url+"' ("+xhr.status+" "+xhr.statusText+")"};
self._handleDefaults.call(self,e,"onDownloadError");
self.onLoad();
}
}},_343));
},_cacheSetting:function(_349,_34a){
for(var x in this.bindArgs){
if(dojo.lang.isUndefined(_349[x])){
_349[x]=this.bindArgs[x];
}
}
if(dojo.lang.isUndefined(_349.useCache)){
_349.useCache=_34a;
}
if(dojo.lang.isUndefined(_349.preventCache)){
_349.preventCache=!_34a;
}
if(dojo.lang.isUndefined(_349.mimetype)){
_349.mimetype="text/html";
}
return _349;
},onLoad:function(e){
this._runStack("_onLoadStack");
this.isLoaded=true;
},onUnLoad:function(e){
dojo.deprecated(this.widgetType+".onUnLoad, use .onUnload (lowercased load)",0.5);
},onUnload:function(e){
this._runStack("_onUnloadStack");
delete this.scriptScope;
if(this.onUnLoad!==dojo.widget.ContentPane.prototype.onUnLoad){
this.onUnLoad.apply(this,arguments);
}
},_runStack:function(_34f){
var st=this[_34f];
var err="";
var _352=this.scriptScope||window;
for(var i=0;i<st.length;i++){
try{
st[i].call(_352);
}
catch(e){
err+="\n"+st[i]+" failed: "+e.description;
}
}
this[_34f]=[];
if(err.length){
var name=(_34f=="_onLoadStack")?"addOnLoad":"addOnUnLoad";
this._handleDefaults(name+" failure\n "+err,"onExecError","debug");
}
},addOnLoad:function(obj,func){
this._pushOnStack(this._onLoadStack,obj,func);
},addOnUnload:function(obj,func){
this._pushOnStack(this._onUnloadStack,obj,func);
},addOnUnLoad:function(){
dojo.deprecated(this.widgetType+".addOnUnLoad, use addOnUnload instead. (lowercased Load)",0.5);
this.addOnUnload.apply(this,arguments);
},_pushOnStack:function(_359,obj,func){
if(typeof func=="undefined"){
_359.push(obj);
}else{
_359.push(function(){
obj[func]();
});
}
},destroy:function(){
this.onUnload();
dojo.widget.ContentPane.superclass.destroy.call(this);
},onExecError:function(e){
},onContentError:function(e){
},onDownloadError:function(e){
},onDownloadStart:function(e){
},onDownloadEnd:function(url,data){
data=this.splitAndFixPaths(data,url);
this.setContent(data);
},_handleDefaults:function(e,_363,_364){
if(!_363){
_363="onContentError";
}
if(dojo.lang.isString(e)){
e={text:e};
}
if(!e.text){
e.text=e.toString();
}
e.toString=function(){
return this.text;
};
if(typeof e.returnValue!="boolean"){
e.returnValue=true;
}
if(typeof e.preventDefault!="function"){
e.preventDefault=function(){
this.returnValue=false;
};
}
this[_363](e);
if(e.returnValue){
switch(_364){
case true:
case "alert":
alert(e.toString());
break;
case "debug":
dojo.debug(e.toString());
break;
default:
if(this._callOnUnload){
this.onUnload();
}
this._callOnUnload=false;
if(arguments.callee._loopStop){
dojo.debug(e.toString());
}else{
arguments.callee._loopStop=true;
this._setContent(e.toString());
}
}
}
arguments.callee._loopStop=false;
},splitAndFixPaths:function(s,url){
var _367=[],_368=[],tmp=[];
var _36a=[],_36b=[],attr=[],_36d=[];
var str="",path="",fix="",_371="",tag="",_373="";
if(!url){
url="./";
}
if(s){
var _374=/<title[^>]*>([\s\S]*?)<\/title>/i;
while(_36a=_374.exec(s)){
_367.push(_36a[1]);
s=s.substring(0,_36a.index)+s.substr(_36a.index+_36a[0].length);
}
if(this.adjustPaths){
var _375=/<[a-z][a-z0-9]*[^>]*\s(?:(?:src|href|style)=[^>])+[^>]*>/i;
var _376=/\s(src|href|style)=(['"]?)([\w()\[\]\/.,\\'"-:;#=&?\s@]+?)\2/i;
var _377=/^(?:[#]|(?:(?:https?|ftps?|file|javascript|mailto|news):))/;
while(tag=_375.exec(s)){
str+=s.substring(0,tag.index);
s=s.substring((tag.index+tag[0].length),s.length);
tag=tag[0];
_371="";
while(attr=_376.exec(tag)){
path="";
_373=attr[3];
switch(attr[1].toLowerCase()){
case "src":
case "href":
if(_377.exec(_373)){
path=_373;
}else{
path=(new dojo.uri.Uri(url,_373).toString());
}
break;
case "style":
path=dojo.html.fixPathsInCssText(_373,url);
break;
default:
path=_373;
}
fix=" "+attr[1]+"="+attr[2]+path+attr[2];
_371+=tag.substring(0,attr.index)+fix;
tag=tag.substring((attr.index+attr[0].length),tag.length);
}
str+=_371+tag;
}
s=str+s;
}
_374=/(?:<(style)[^>]*>([\s\S]*?)<\/style>|<link ([^>]*rel=['"]?stylesheet['"]?[^>]*)>)/i;
while(_36a=_374.exec(s)){
if(_36a[1]&&_36a[1].toLowerCase()=="style"){
_36d.push(dojo.html.fixPathsInCssText(_36a[2],url));
}else{
if(attr=_36a[3].match(/href=(['"]?)([^'">]*)\1/i)){
_36d.push({path:attr[2]});
}
}
s=s.substring(0,_36a.index)+s.substr(_36a.index+_36a[0].length);
}
var _374=/<script([^>]*)>([\s\S]*?)<\/script>/i;
var _378=/src=(['"]?)([^"']*)\1/i;
var _379=/.*(\bdojo\b\.js(?:\.uncompressed\.js)?)$/;
var _37a=/(?:var )?\bdjConfig\b(?:[\s]*=[\s]*\{[^}]+\}|\.[\w]*[\s]*=[\s]*[^;\n]*)?;?|dojo\.hostenv\.writeIncludes\(\s*\);?/g;
var _37b=/dojo\.(?:(?:require(?:After)?(?:If)?)|(?:widget\.(?:manager\.)?registerWidgetPackage)|(?:(?:hostenv\.)?setModulePrefix|registerModulePath)|defineNamespace)\((['"]).*?\1\)\s*;?/;
while(_36a=_374.exec(s)){
if(this.executeScripts&&_36a[1]){
if(attr=_378.exec(_36a[1])){
if(_379.exec(attr[2])){
dojo.debug("Security note! inhibit:"+attr[2]+" from  being loaded again.");
}else{
_368.push({path:attr[2]});
}
}
}
if(_36a[2]){
var sc=_36a[2].replace(_37a,"");
if(!sc){
continue;
}
while(tmp=_37b.exec(sc)){
_36b.push(tmp[0]);
sc=sc.substring(0,tmp.index)+sc.substr(tmp.index+tmp[0].length);
}
if(this.executeScripts){
_368.push(sc);
}
}
s=s.substr(0,_36a.index)+s.substr(_36a.index+_36a[0].length);
}
if(this.extractContent){
_36a=s.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
if(_36a){
s=_36a[1];
}
}
if(this.executeScripts&&this.scriptSeparation){
var _374=/(<[a-zA-Z][a-zA-Z0-9]*\s[^>]*?\S=)((['"])[^>]*scriptScope[^>]*>)/;
var _37d=/([\s'";:\(])scriptScope(.*)/;
str="";
while(tag=_374.exec(s)){
tmp=((tag[3]=="'")?"\"":"'");
fix="";
str+=s.substring(0,tag.index)+tag[1];
while(attr=_37d.exec(tag[2])){
tag[2]=tag[2].substring(0,attr.index)+attr[1]+"dojo.widget.byId("+tmp+this.widgetId+tmp+").scriptScope"+attr[2];
}
str+=tag[2];
s=s.substr(tag.index+tag[0].length);
}
s=str+s;
}
}
return {"xml":s,"styles":_36d,"titles":_367,"requires":_36b,"scripts":_368,"url":url};
},_setContent:function(cont){
this.destroyChildren();
for(var i=0;i<this._styleNodes.length;i++){
if(this._styleNodes[i]&&this._styleNodes[i].parentNode){
this._styleNodes[i].parentNode.removeChild(this._styleNodes[i]);
}
}
this._styleNodes=[];
try{
var node=this.containerNode||this.domNode;
while(node.firstChild){
dojo.html.destroyNode(node.firstChild);
}
if(typeof cont!="string"){
node.appendChild(cont);
}else{
node.innerHTML=cont;
}
}
catch(e){
e.text="Couldn't load content:"+e.description;
this._handleDefaults(e,"onContentError");
}
},setContent:function(data){
this.abort();
if(this._callOnUnload){
this.onUnload();
}
this._callOnUnload=true;
if(!data||dojo.html.isNode(data)){
this._setContent(data);
this.onResized();
this.onLoad();
}else{
if(typeof data.xml!="string"){
this.href="";
data=this.splitAndFixPaths(data);
}
this._setContent(data.xml);
for(var i=0;i<data.styles.length;i++){
if(data.styles[i].path){
this._styleNodes.push(dojo.html.insertCssFile(data.styles[i].path,dojo.doc(),false,true));
}else{
this._styleNodes.push(dojo.html.insertCssText(data.styles[i]));
}
}
if(this.parseContent){
for(var i=0;i<data.requires.length;i++){
try{
eval(data.requires[i]);
}
catch(e){
e.text="ContentPane: error in package loading calls, "+(e.description||e);
this._handleDefaults(e,"onContentError","debug");
}
}
}
var _383=this;
function asyncParse(){
if(_383.executeScripts){
_383._executeScripts(data.scripts);
}
if(_383.parseContent){
var node=_383.containerNode||_383.domNode;
var _385=new dojo.xml.Parse();
var frag=_385.parseElement(node,null,true);
dojo.widget.getParser().createSubComponents(frag,_383);
}
_383.onResized();
_383.onLoad();
}
if(dojo.hostenv.isXDomain&&data.requires.length){
dojo.addOnLoad(asyncParse);
}else{
asyncParse();
}
}
},setHandler:function(_387){
var fcn=dojo.lang.isFunction(_387)?_387:window[_387];
if(!dojo.lang.isFunction(fcn)){
this._handleDefaults("Unable to set handler, '"+_387+"' not a function.","onExecError",true);
return;
}
this.handler=function(){
return fcn.apply(this,arguments);
};
},_runHandler:function(){
var ret=true;
if(dojo.lang.isFunction(this.handler)){
this.handler(this,this.domNode);
ret=false;
}
this.onLoad();
return ret;
},_executeScripts:function(_38a){
var self=this;
var tmp="",code="";
for(var i=0;i<_38a.length;i++){
if(_38a[i].path){
dojo.io.bind(this._cacheSetting({"url":_38a[i].path,"load":function(type,_390){
dojo.lang.hitch(self,tmp=";"+_390);
},"error":function(type,_392){
_392.text=type+" downloading remote script";
self._handleDefaults.call(self,_392,"onExecError","debug");
},"mimetype":"text/plain","sync":true},this.cacheContent));
code+=tmp;
}else{
code+=_38a[i];
}
}
try{
if(this.scriptSeparation){
delete this.scriptScope;
this.scriptScope=new (new Function("_container_",code+"; return this;"))(self);
}else{
var djg=dojo.global();
if(djg.execScript){
djg.execScript(code);
}else{
var djd=dojo.doc();
var sc=djd.createElement("script");
sc.appendChild(djd.createTextNode(code));
(this.containerNode||this.domNode).appendChild(sc);
}
}
}
catch(e){
e.text="Error running scripts from content:\n"+e.description;
this._handleDefaults(e,"onExecError","debug");
}
}});
dojo.require("dojo.html.common");
dojo.provide("dojo.html.selection");
dojo.require("dojo.dom");
dojo.require("dojo.lang.common");
dojo.html.selectionType={NONE:0,TEXT:1,CONTROL:2};
dojo.html.clearSelection=function(){
var _396=dojo.global();
var _397=dojo.doc();
try{
if(_396["getSelection"]){
if(dojo.render.html.safari){
_396.getSelection().collapse();
}else{
_396.getSelection().removeAllRanges();
}
}else{
if(_397.selection){
if(_397.selection.empty){
_397.selection.empty();
}else{
if(_397.selection.clear){
_397.selection.clear();
}
}
}
}
return true;
}
catch(e){
dojo.debug(e);
return false;
}
};
dojo.html.disableSelection=function(_398){
_398=dojo.byId(_398)||dojo.body();
var h=dojo.render.html;
if(h.mozilla){
_398.style.MozUserSelect="none";
}else{
if(h.safari){
_398.style.KhtmlUserSelect="none";
}else{
if(h.ie){
_398.unselectable="on";
}else{
return false;
}
}
}
return true;
};
dojo.html.enableSelection=function(_39a){
_39a=dojo.byId(_39a)||dojo.body();
var h=dojo.render.html;
if(h.mozilla){
_39a.style.MozUserSelect="";
}else{
if(h.safari){
_39a.style.KhtmlUserSelect="";
}else{
if(h.ie){
_39a.unselectable="off";
}else{
return false;
}
}
}
return true;
};
dojo.html.selectElement=function(_39c){
dojo.deprecated("dojo.html.selectElement","replaced by dojo.html.selection.selectElementChildren",0.5);
};
dojo.html.selectInputText=function(_39d){
var _39e=dojo.global();
var _39f=dojo.doc();
_39d=dojo.byId(_39d);
if(_39f["selection"]&&dojo.body()["createTextRange"]){
var _3a0=_39d.createTextRange();
_3a0.moveStart("character",0);
_3a0.moveEnd("character",_39d.value.length);
_3a0.select();
}else{
if(_39e["getSelection"]){
var _3a1=_39e.getSelection();
_39d.setSelectionRange(0,_39d.value.length);
}
}
_39d.focus();
};
dojo.html.isSelectionCollapsed=function(){
dojo.deprecated("dojo.html.isSelectionCollapsed","replaced by dojo.html.selection.isCollapsed",0.5);
return dojo.html.selection.isCollapsed();
};
dojo.lang.mixin(dojo.html.selection,{getType:function(){
if(dojo.doc()["selection"]){
return dojo.html.selectionType[dojo.doc().selection.type.toUpperCase()];
}else{
var _3a2=dojo.html.selectionType.TEXT;
var oSel;
try{
oSel=dojo.global().getSelection();
}
catch(e){
}
if(oSel&&oSel.rangeCount==1){
var _3a4=oSel.getRangeAt(0);
if(_3a4.startContainer==_3a4.endContainer&&(_3a4.endOffset-_3a4.startOffset)==1&&_3a4.startContainer.nodeType!=dojo.dom.TEXT_NODE){
_3a2=dojo.html.selectionType.CONTROL;
}
}
return _3a2;
}
},isCollapsed:function(){
var _3a5=dojo.global();
var _3a6=dojo.doc();
if(_3a6["selection"]){
return _3a6.selection.createRange().text=="";
}else{
if(_3a5["getSelection"]){
var _3a7=_3a5.getSelection();
if(dojo.lang.isString(_3a7)){
return _3a7=="";
}else{
return _3a7.isCollapsed||_3a7.toString()=="";
}
}
}
},getSelectedElement:function(){
if(dojo.html.selection.getType()==dojo.html.selectionType.CONTROL){
if(dojo.doc()["selection"]){
var _3a8=dojo.doc().selection.createRange();
if(_3a8&&_3a8.item){
return dojo.doc().selection.createRange().item(0);
}
}else{
var _3a9=dojo.global().getSelection();
return _3a9.anchorNode.childNodes[_3a9.anchorOffset];
}
}
},getParentElement:function(){
if(dojo.html.selection.getType()==dojo.html.selectionType.CONTROL){
var p=dojo.html.selection.getSelectedElement();
if(p){
return p.parentNode;
}
}else{
if(dojo.doc()["selection"]){
return dojo.doc().selection.createRange().parentElement();
}else{
var _3ab=dojo.global().getSelection();
if(_3ab){
var node=_3ab.anchorNode;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.parentNode;
}
return node;
}
}
}
},getSelectedText:function(){
if(dojo.doc()["selection"]){
if(dojo.html.selection.getType()==dojo.html.selectionType.CONTROL){
return null;
}
return dojo.doc().selection.createRange().text;
}else{
var _3ad=dojo.global().getSelection();
if(_3ad){
return _3ad.toString();
}
}
},getSelectedHtml:function(){
if(dojo.doc()["selection"]){
if(dojo.html.selection.getType()==dojo.html.selectionType.CONTROL){
return null;
}
return dojo.doc().selection.createRange().htmlText;
}else{
var _3ae=dojo.global().getSelection();
if(_3ae&&_3ae.rangeCount){
var frag=_3ae.getRangeAt(0).cloneContents();
var div=document.createElement("div");
div.appendChild(frag);
return div.innerHTML;
}
return null;
}
},hasAncestorElement:function(_3b1){
return (dojo.html.selection.getAncestorElement.apply(this,arguments)!=null);
},getAncestorElement:function(_3b2){
var node=dojo.html.selection.getSelectedElement()||dojo.html.selection.getParentElement();
while(node){
if(dojo.html.selection.isTag(node,arguments).length>0){
return node;
}
node=node.parentNode;
}
return null;
},isTag:function(node,tags){
if(node&&node.tagName){
for(var i=0;i<tags.length;i++){
if(node.tagName.toLowerCase()==String(tags[i]).toLowerCase()){
return String(tags[i]).toLowerCase();
}
}
}
return "";
},selectElement:function(_3b7){
var _3b8=dojo.global();
var _3b9=dojo.doc();
_3b7=dojo.byId(_3b7);
if(_3b9.selection&&dojo.body().createTextRange){
try{
var _3ba=dojo.body().createControlRange();
_3ba.addElement(_3b7);
_3ba.select();
}
catch(e){
dojo.html.selection.selectElementChildren(_3b7);
}
}else{
if(_3b8["getSelection"]){
var _3bb=_3b8.getSelection();
if(_3bb["removeAllRanges"]){
var _3ba=_3b9.createRange();
_3ba.selectNode(_3b7);
_3bb.removeAllRanges();
_3bb.addRange(_3ba);
}
}
}
},selectElementChildren:function(_3bc){
var _3bd=dojo.global();
var _3be=dojo.doc();
_3bc=dojo.byId(_3bc);
if(_3be.selection&&dojo.body().createTextRange){
var _3bf=dojo.body().createTextRange();
_3bf.moveToElementText(_3bc);
_3bf.select();
}else{
if(_3bd["getSelection"]){
var _3c0=_3bd.getSelection();
if(_3c0["setBaseAndExtent"]){
_3c0.setBaseAndExtent(_3bc,0,_3bc,_3bc.innerText.length-1);
}else{
if(_3c0["selectAllChildren"]){
_3c0.selectAllChildren(_3bc);
}
}
}
}
},getBookmark:function(){
var _3c1;
var _3c2=dojo.doc();
if(_3c2["selection"]){
var _3c3=_3c2.selection.createRange();
_3c1=_3c3.getBookmark();
}else{
var _3c4;
try{
_3c4=dojo.global().getSelection();
}
catch(e){
}
if(_3c4){
var _3c3=_3c4.getRangeAt(0);
_3c1=_3c3.cloneRange();
}else{
dojo.debug("No idea how to store the current selection for this browser!");
}
}
return _3c1;
},moveToBookmark:function(_3c5){
var _3c6=dojo.doc();
if(_3c6["selection"]){
var _3c7=_3c6.selection.createRange();
_3c7.moveToBookmark(_3c5);
_3c7.select();
}else{
var _3c8;
try{
_3c8=dojo.global().getSelection();
}
catch(e){
}
if(_3c8&&_3c8["removeAllRanges"]){
_3c8.removeAllRanges();
_3c8.addRange(_3c5);
}else{
dojo.debug("No idea how to restore selection for this browser!");
}
}
},collapse:function(_3c9){
if(dojo.global()["getSelection"]){
var _3ca=dojo.global().getSelection();
if(_3ca.removeAllRanges){
if(_3c9){
_3ca.collapseToStart();
}else{
_3ca.collapseToEnd();
}
}else{
dojo.global().getSelection().collapse(_3c9);
}
}else{
if(dojo.doc().selection){
var _3cb=dojo.doc().selection.createRange();
_3cb.collapse(_3c9);
_3cb.select();
}
}
},remove:function(){
if(dojo.doc().selection){
var _3cc=dojo.doc().selection;
if(_3cc.type.toUpperCase()!="NONE"){
_3cc.clear();
}
return _3cc;
}else{
var _3cc=dojo.global().getSelection();
for(var i=0;i<_3cc.rangeCount;i++){
_3cc.getRangeAt(i).deleteContents();
}
return _3cc;
}
}});
dojo.provide("dojo.html.iframe");
dojo.html.iframeContentWindow=function(_3ce){
var win=dojo.html.getDocumentWindow(dojo.html.iframeContentDocument(_3ce))||dojo.html.iframeContentDocument(_3ce).__parent__||(_3ce.name&&document.frames[_3ce.name])||null;
return win;
};
dojo.html.iframeContentDocument=function(_3d0){
var doc=_3d0.contentDocument||((_3d0.contentWindow)&&(_3d0.contentWindow.document))||((_3d0.name)&&(document.frames[_3d0.name])&&(document.frames[_3d0.name].document))||null;
return doc;
};
dojo.html.BackgroundIframe=function(node){
if(dojo.render.html.ie55||dojo.render.html.ie60){
var html="<iframe src='javascript:false'"+" style='position: absolute; left: 0px; top: 0px; width: 100%; height: 100%;"+"z-index: -1; filter:Alpha(Opacity=\"0\");' "+">";
this.iframe=dojo.doc().createElement(html);
this.iframe.tabIndex=-1;
if(node){
node.appendChild(this.iframe);
this.domNode=node;
}else{
dojo.body().appendChild(this.iframe);
this.iframe.style.display="none";
}
}
};
dojo.lang.extend(dojo.html.BackgroundIframe,{iframe:null,onResized:function(){
if(this.iframe&&this.domNode&&this.domNode.parentNode){
var _3d4=dojo.html.getMarginBox(this.domNode);
if(_3d4.width==0||_3d4.height==0){
dojo.lang.setTimeout(this,this.onResized,100);
return;
}
this.iframe.style.width=_3d4.width+"px";
this.iframe.style.height=_3d4.height+"px";
}
},size:function(node){
if(!this.iframe){
return;
}
var _3d6=dojo.html.toCoordinateObject(node,true,dojo.html.boxSizing.BORDER_BOX);
with(this.iframe.style){
width=_3d6.width+"px";
height=_3d6.height+"px";
left=_3d6.left+"px";
top=_3d6.top+"px";
}
},setZIndex:function(node){
if(!this.iframe){
return;
}
if(dojo.dom.isNode(node)){
this.iframe.style.zIndex=dojo.html.getStyle(node,"z-index")-1;
}else{
if(!isNaN(node)){
this.iframe.style.zIndex=node;
}
}
},show:function(){
if(this.iframe){
this.iframe.style.display="block";
}
},hide:function(){
if(this.iframe){
this.iframe.style.display="none";
}
},remove:function(){
if(this.iframe){
dojo.html.removeNode(this.iframe,true);
delete this.iframe;
this.iframe=null;
}
}});
dojo.provide("dojo.widget.PopupContainer");
dojo.require("dojo.html.style");
dojo.require("dojo.event.*");
dojo.declare("dojo.widget.PopupContainerBase",null,function(){
this.queueOnAnimationFinish=[];
},{isShowingNow:false,currentSubpopup:null,beginZIndex:1000,parentPopup:null,parent:null,popupIndex:0,aroundBox:dojo.html.boxSizing.BORDER_BOX,openedForWindow:null,processKey:function(evt){
return false;
},applyPopupBasicStyle:function(){
with(this.domNode.style){
display="none";
position="absolute";
}
},aboutToShow:function(){
},open:function(x,y,_3db,_3dc,_3dd,_3de){
if(this.isShowingNow){
return;
}
if(this.animationInProgress){
this.queueOnAnimationFinish.push(this.open,arguments);
return;
}
this.aboutToShow();
var _3df=false,node,_3e1;
if(typeof x=="object"){
node=x;
_3e1=_3dc;
_3dc=_3db;
_3db=y;
_3df=true;
}
this.parent=_3db;
dojo.body().appendChild(this.domNode);
_3dc=_3dc||_3db["domNode"]||[];
var _3e2=null;
this.isTopLevel=true;
while(_3db){
if(_3db!==this&&(_3db.setOpenedSubpopup!=undefined&&_3db.applyPopupBasicStyle!=undefined)){
_3e2=_3db;
this.isTopLevel=false;
_3e2.setOpenedSubpopup(this);
break;
}
_3db=_3db.parent;
}
this.parentPopup=_3e2;
this.popupIndex=_3e2?_3e2.popupIndex+1:1;
if(this.isTopLevel){
var _3e3=dojo.html.isNode(_3dc)?_3dc:null;
dojo.widget.PopupManager.opened(this,_3e3);
}
if(this.isTopLevel&&!dojo.withGlobal(this.openedForWindow||dojo.global(),dojo.html.selection.isCollapsed)){
this._bookmark=dojo.withGlobal(this.openedForWindow||dojo.global(),dojo.html.selection.getBookmark);
}else{
this._bookmark=null;
}
if(_3dc instanceof Array){
_3dc={left:_3dc[0],top:_3dc[1],width:0,height:0};
}
with(this.domNode.style){
display="";
zIndex=this.beginZIndex+this.popupIndex;
}
if(_3df){
this.move(node,_3de,_3e1);
}else{
this.move(x,y,_3de,_3dd);
}
this.domNode.style.display="none";
this.explodeSrc=_3dc;
this.show();
this.isShowingNow=true;
},move:function(x,y,_3e6,_3e7){
var _3e8=(typeof x=="object");
if(_3e8){
var _3e9=_3e6;
var node=x;
_3e6=y;
if(!_3e9){
_3e9={"BL":"TL","TL":"BL"};
}
dojo.html.placeOnScreenAroundElement(this.domNode,node,_3e6,this.aroundBox,_3e9);
}else{
if(!_3e7){
_3e7="TL,TR,BL,BR";
}
dojo.html.placeOnScreen(this.domNode,x,y,_3e6,true,_3e7);
}
},close:function(_3eb){
if(_3eb){
this.domNode.style.display="none";
}
if(this.animationInProgress){
this.queueOnAnimationFinish.push(this.close,[]);
return;
}
this.closeSubpopup(_3eb);
this.hide();
if(this.bgIframe){
this.bgIframe.hide();
this.bgIframe.size({left:0,top:0,width:0,height:0});
}
if(this.isTopLevel){
dojo.widget.PopupManager.closed(this);
}
this.isShowingNow=false;
if(this.parent){
setTimeout(dojo.lang.hitch(this,function(){
try{
if(this.parent["focus"]){
this.parent.focus();
}else{
this.parent.domNode.focus();
}
}
catch(e){
dojo.debug("No idea how to focus to parent",e);
}
}),10);
}
if(this._bookmark&&dojo.withGlobal(this.openedForWindow||dojo.global(),dojo.html.selection.isCollapsed)){
if(this.openedForWindow){
this.openedForWindow.focus();
}
try{
dojo.withGlobal(this.openedForWindow||dojo.global(),"moveToBookmark",dojo.html.selection,[this._bookmark]);
}
catch(e){
}
}
this._bookmark=null;
},closeAll:function(_3ec){
if(this.parentPopup){
this.parentPopup.closeAll(_3ec);
}else{
this.close(_3ec);
}
},setOpenedSubpopup:function(_3ed){
this.currentSubpopup=_3ed;
},closeSubpopup:function(_3ee){
if(this.currentSubpopup==null){
return;
}
this.currentSubpopup.close(_3ee);
this.currentSubpopup=null;
},onShow:function(){
dojo.widget.PopupContainer.superclass.onShow.apply(this,arguments);
this.openedSize={w:this.domNode.style.width,h:this.domNode.style.height};
if(dojo.render.html.ie){
if(!this.bgIframe){
this.bgIframe=new dojo.html.BackgroundIframe();
this.bgIframe.setZIndex(this.domNode);
}
this.bgIframe.size(this.domNode);
this.bgIframe.show();
}
this.processQueue();
},processQueue:function(){
if(!this.queueOnAnimationFinish.length){
return;
}
var func=this.queueOnAnimationFinish.shift();
var args=this.queueOnAnimationFinish.shift();
func.apply(this,args);
},onHide:function(){
dojo.widget.HtmlWidget.prototype.onHide.call(this);
if(this.openedSize){
with(this.domNode.style){
width=this.openedSize.w;
height=this.openedSize.h;
}
}
this.processQueue();
}});
dojo.widget.defineWidget("dojo.widget.PopupContainer",[dojo.widget.HtmlWidget,dojo.widget.PopupContainerBase],{isContainer:true,fillInTemplate:function(){
this.applyPopupBasicStyle();
dojo.widget.PopupContainer.superclass.fillInTemplate.apply(this,arguments);
}});
dojo.widget.PopupManager=new function(){
this.currentMenu=null;
this.currentButton=null;
this.currentFocusMenu=null;
this.focusNode=null;
this.registeredWindows=[];
this.registerWin=function(win){
if(!win.__PopupManagerRegistered){
dojo.event.connect(win.document,"onmousedown",this,"onClick");
dojo.event.connect(win,"onscroll",this,"onClick");
dojo.event.connect(win.document,"onkey",this,"onKey");
win.__PopupManagerRegistered=true;
this.registeredWindows.push(win);
}
};
this.registerAllWindows=function(_3f2){
if(!_3f2){
_3f2=dojo.html.getDocumentWindow(window.top&&window.top.document||window.document);
}
this.registerWin(_3f2);
for(var i=0;i<_3f2.frames.length;i++){
try{
var win=dojo.html.getDocumentWindow(_3f2.frames[i].document);
if(win){
this.registerAllWindows(win);
}
}
catch(e){
}
}
};
this.unRegisterWin=function(win){
if(win.__PopupManagerRegistered){
dojo.event.disconnect(win.document,"onmousedown",this,"onClick");
dojo.event.disconnect(win,"onscroll",this,"onClick");
dojo.event.disconnect(win.document,"onkey",this,"onKey");
win.__PopupManagerRegistered=false;
}
};
this.unRegisterAllWindows=function(){
for(var i=0;i<this.registeredWindows.length;++i){
this.unRegisterWin(this.registeredWindows[i]);
}
this.registeredWindows=[];
};
dojo.addOnLoad(this,"registerAllWindows");
dojo.addOnUnload(this,"unRegisterAllWindows");
this.closed=function(menu){
if(this.currentMenu==menu){
this.currentMenu=null;
this.currentButton=null;
this.currentFocusMenu=null;
}
};
this.opened=function(menu,_3f9){
if(menu==this.currentMenu){
return;
}
if(this.currentMenu){
this.currentMenu.close();
}
this.currentMenu=menu;
this.currentFocusMenu=menu;
this.currentButton=_3f9;
};
this.setFocusedMenu=function(menu){
this.currentFocusMenu=menu;
};
this.onKey=function(e){
if(!e.key){
return;
}
if(!this.currentMenu||!this.currentMenu.isShowingNow){
return;
}
var m=this.currentFocusMenu;
while(m){
if(m.processKey(e)){
e.preventDefault();
e.stopPropagation();
break;
}
m=m.parentPopup||m.parentMenu;
}
},this.onClick=function(e){
if(!this.currentMenu){
return;
}
var _3fe=dojo.html.getScroll().offset;
var m=this.currentMenu;
while(m){
if(dojo.html.overElement(m.domNode,e)||dojo.html.isDescendantOf(e.target,m.domNode)){
return;
}
m=m.currentSubpopup;
}
if(this.currentButton&&dojo.html.overElement(this.currentButton,e)){
return;
}
this.currentMenu.closeAll(true);
};
};
dojo.provide("dojo.widget.DropdownContainer");
dojo.require("dojo.event.*");
dojo.require("dojo.html.display");
dojo.widget.defineWidget("dojo.widget.DropdownContainer",dojo.widget.HtmlWidget,{inputWidth:"7em",id:"",inputId:"",inputName:"",iconURL:dojo.uri.moduleUri("dojo.widget","templates/images/combo_box_arrow.png"),copyClasses:false,iconAlt:"",containerToggle:"plain",containerToggleDuration:150,templateString:"<span style=\"white-space:nowrap\"><input type=\"hidden\" name=\"\" value=\"\" dojoAttachPoint=\"valueNode\" /><input name=\"\" type=\"text\" value=\"\" style=\"vertical-align:middle;\" dojoAttachPoint=\"inputNode\" autocomplete=\"off\" /> <img src=\"${this.iconURL}\" alt=\"${this.iconAlt}\" dojoAttachEvent=\"onclick:onIconClick\" dojoAttachPoint=\"buttonNode\" style=\"vertical-align:middle; cursor:pointer; cursor:hand\" /></span>",templateCssPath:"",isContainer:true,attachTemplateNodes:function(){
dojo.widget.DropdownContainer.superclass.attachTemplateNodes.apply(this,arguments);
this.popup=dojo.widget.createWidget("PopupContainer",{toggle:this.containerToggle,toggleDuration:this.containerToggleDuration});
this.containerNode=this.popup.domNode;
},fillInTemplate:function(args,frag){
this.domNode.appendChild(this.popup.domNode);
if(this.id){
this.domNode.id=this.id;
}
if(this.inputId){
this.inputNode.id=this.inputId;
}
if(this.inputName){
this.inputNode.name=this.inputName;
}
this.inputNode.style.width=this.inputWidth;
this.inputNode.disabled=this.disabled;
if(this.copyClasses){
this.inputNode.style="";
this.inputNode.className=this.getFragNodeRef(frag).className;
}
dojo.event.connect(this.inputNode,"onchange",this,"onInputChange");
},onIconClick:function(evt){
if(this.disabled){
return;
}
if(!this.popup.isShowingNow){
this.popup.open(this.inputNode,this,this.buttonNode);
}else{
this.popup.close();
}
},hideContainer:function(){
if(this.popup.isShowingNow){
this.popup.close();
}
},onInputChange:function(){
},enable:function(){
this.inputNode.disabled=false;
dojo.widget.DropdownContainer.superclass.enable.apply(this,arguments);
},disable:function(){
this.inputNode.disabled=true;
dojo.widget.DropdownContainer.superclass.disable.apply(this,arguments);
}});
dojo.provide("dojo.widget.html.stabile");
dojo.widget.html.stabile={_sqQuotables:new RegExp("([\\\\'])","g"),_depth:0,_recur:false,depthLimit:2};
dojo.widget.html.stabile.getState=function(id){
dojo.widget.html.stabile.setup();
return dojo.widget.html.stabile.widgetState[id];
};
dojo.widget.html.stabile.setState=function(id,_405,_406){
dojo.widget.html.stabile.setup();
dojo.widget.html.stabile.widgetState[id]=_405;
if(_406){
dojo.widget.html.stabile.commit(dojo.widget.html.stabile.widgetState);
}
};
dojo.widget.html.stabile.setup=function(){
if(!dojo.widget.html.stabile.widgetState){
var text=dojo.widget.html.stabile._getStorage().value;
dojo.widget.html.stabile.widgetState=text?dj_eval("("+text+")"):{};
}
};
dojo.widget.html.stabile.commit=function(_408){
dojo.widget.html.stabile._getStorage().value=dojo.widget.html.stabile.description(_408);
};
dojo.widget.html.stabile.description=function(v,_40a){
var _40b=dojo.widget.html.stabile._depth;
var _40c=function(){
return this.description(this,true);
};
try{
if(v===void (0)){
return "undefined";
}
if(v===null){
return "null";
}
if(typeof (v)=="boolean"||typeof (v)=="number"||v instanceof Boolean||v instanceof Number){
return v.toString();
}
if(typeof (v)=="string"||v instanceof String){
var v1=v.replace(dojo.widget.html.stabile._sqQuotables,"\\$1");
v1=v1.replace(/\n/g,"\\n");
v1=v1.replace(/\r/g,"\\r");
return "'"+v1+"'";
}
if(v instanceof Date){
return "new Date("+d.getFullYear+","+d.getMonth()+","+d.getDate()+")";
}
var d;
if(v instanceof Array||v.push){
if(_40b>=dojo.widget.html.stabile.depthLimit){
return "[ ... ]";
}
d="[";
var _40f=true;
dojo.widget.html.stabile._depth++;
for(var i=0;i<v.length;i++){
if(_40f){
_40f=false;
}else{
d+=",";
}
d+=arguments.callee(v[i],_40a);
}
return d+"]";
}
if(v.constructor==Object||v.toString==_40c){
if(_40b>=dojo.widget.html.stabile.depthLimit){
return "{ ... }";
}
if(typeof (v.hasOwnProperty)!="function"&&v.prototype){
throw new Error("description: "+v+" not supported by script engine");
}
var _40f=true;
d="{";
dojo.widget.html.stabile._depth++;
for(var key in v){
if(v[key]==void (0)||typeof (v[key])=="function"){
continue;
}
if(_40f){
_40f=false;
}else{
d+=", ";
}
var kd=key;
if(!kd.match(/^[a-zA-Z_][a-zA-Z0-9_]*$/)){
kd=arguments.callee(key,_40a);
}
d+=kd+": "+arguments.callee(v[key],_40a);
}
return d+"}";
}
if(_40a){
if(dojo.widget.html.stabile._recur){
var _413=Object.prototype.toString;
return _413.apply(v,[]);
}else{
dojo.widget.html.stabile._recur=true;
return v.toString();
}
}else{
throw new Error("Unknown type: "+v);
return "'unknown'";
}
}
finally{
dojo.widget.html.stabile._depth=_40b;
}
};
dojo.widget.html.stabile._getStorage=function(){
if(dojo.widget.html.stabile.dataField){
return dojo.widget.html.stabile.dataField;
}
var form=document.forms._dojo_form;
return dojo.widget.html.stabile.dataField=form?form.stabile:{value:""};
};
dojo.provide("dojo.widget.Dialog");
dojo.require("dojo.event.*");
dojo.require("dojo.html.display");
dojo.declare("dojo.widget.ModalDialogBase",null,{isContainer:true,focusElement:"",bgColor:"black",bgOpacity:0.4,followScroll:true,closeOnBackgroundClick:false,trapTabs:function(e){
if(e.target==this.tabStartOuter){
if(this._fromTrap){
this.tabStart.focus();
this._fromTrap=false;
}else{
this._fromTrap=true;
this.tabEnd.focus();
}
}else{
if(e.target==this.tabStart){
if(this._fromTrap){
this._fromTrap=false;
}else{
this._fromTrap=true;
this.tabEnd.focus();
}
}else{
if(e.target==this.tabEndOuter){
if(this._fromTrap){
this.tabEnd.focus();
this._fromTrap=false;
}else{
this._fromTrap=true;
this.tabStart.focus();
}
}else{
if(e.target==this.tabEnd){
if(this._fromTrap){
this._fromTrap=false;
}else{
this._fromTrap=true;
this.tabStart.focus();
}
}
}
}
}
},clearTrap:function(e){
var _417=this;
setTimeout(function(){
_417._fromTrap=false;
},100);
},postCreate:function(){
with(this.domNode.style){
position="absolute";
zIndex=999;
display="none";
overflow="visible";
}
var b=dojo.body();
b.appendChild(this.domNode);
this.bg=document.createElement("div");
this.bg.className="dialogUnderlay";
with(this.bg.style){
position="absolute";
left=top="0px";
zIndex=998;
display="none";
}
b.appendChild(this.bg);
this.setBackgroundColor(this.bgColor);
this.bgIframe=new dojo.html.BackgroundIframe();
if(this.bgIframe.iframe){
with(this.bgIframe.iframe.style){
position="absolute";
left=top="0px";
zIndex=90;
display="none";
}
}
if(this.closeOnBackgroundClick){
dojo.event.kwConnect({srcObj:this.bg,srcFunc:"onclick",adviceObj:this,adviceFunc:"onBackgroundClick",once:true});
}
},uninitialize:function(){
this.bgIframe.remove();
dojo.html.removeNode(this.bg,true);
dojo.event.disconnect(window, "onscroll", this, "_onScroll");
dojo.event.disconnect(document.documentElement, "onkey", this, "_onKey");
},setBackgroundColor:function(_419){
if(arguments.length>=3){
_419=new dojo.gfx.color.Color(arguments[0],arguments[1],arguments[2]);
}else{
_419=new dojo.gfx.color.Color(_419);
}
this.bg.style.backgroundColor=_419.toString();
return this.bgColor=_419;
},setBackgroundOpacity:function(op){
if(arguments.length==0){
op=this.bgOpacity;
}
dojo.html.setOpacity(this.bg,op);
try{
this.bgOpacity=dojo.html.getOpacity(this.bg);
}
catch(e){
this.bgOpacity=op;
}
return this.bgOpacity;
},_sizeBackground:function(){
if(this.bgOpacity>0){
var _41b=dojo.html.getViewport();
var h=_41b.height;
var w=_41b.width;
with(this.bg.style){
width=w+"px";
height=h+"px";
}
var _41e=dojo.html.getScroll().offset;
this.bg.style.top=_41e.y+"px";
this.bg.style.left=_41e.x+"px";
var _41b=dojo.html.getViewport();
if(_41b.width!=w){
this.bg.style.width=_41b.width+"px";
}
if(_41b.height!=h){
this.bg.style.height=_41b.height+"px";
}
}
this.bgIframe.size(this.bg);
},_showBackground:function(){
if(this.bgOpacity>0){
this.bg.style.display="block";
}
if(this.bgIframe.iframe){
this.bgIframe.iframe.style.display="block";
}
},placeModalDialog:function(){
var _41f=dojo.html.getScroll().offset;
var _420=dojo.html.getViewport();
var mb;
if(this.isShowing()){
mb=dojo.html.getMarginBox(this.domNode);
}else{
dojo.html.setVisibility(this.domNode,false);
dojo.html.show(this.domNode);
mb=dojo.html.getMarginBox(this.domNode);
dojo.html.hide(this.domNode);
dojo.html.setVisibility(this.domNode,true);
}
var x=_41f.x+(_420.width-mb.width)/2;
var y=_41f.y+(_420.height-mb.height)/2;
with(this.domNode.style){
left=x+"px";
top=y+"px";
}
},_onKey:function(evt){
if(evt.key){
var node=evt.target;
while(node!=null){
if(node==this.domNode){
return;
}
node=node.parentNode;
}
if(evt.key!=evt.KEY_TAB){
dojo.event.browser.stopEvent(evt);
}else{
if(!dojo.render.html.opera){
try{
this.tabStart.focus();
}
catch(e){
}
}
}
}
},showModalDialog:function(){
if(this.followScroll&&!this._scrollConnected){
this._scrollConnected=true;
dojo.event.connect(window,"onscroll",this,"_onScroll");
}
dojo.event.connect(document.documentElement,"onkey",this,"_onKey");
this.placeModalDialog();
this.setBackgroundOpacity();
this._sizeBackground();
this._showBackground();
this._fromTrap=true;
setTimeout(dojo.lang.hitch(this,function(){
try{
this.tabStart.focus();
}
catch(e){
}
}),50);
},hideModalDialog:function(){
if(this.focusElement){
dojo.byId(this.focusElement).focus();
dojo.byId(this.focusElement).blur();
}
this.bg.style.display="none";
this.bg.style.width=this.bg.style.height="1px";
if(this.bgIframe.iframe){
this.bgIframe.iframe.style.display="none";
}
dojo.event.disconnect(document.documentElement,"onkey",this,"_onKey");
if(this._scrollConnected){
this._scrollConnected=false;
dojo.event.disconnect(window,"onscroll",this,"_onScroll");
}
},_onScroll:function(){
var _426=dojo.html.getScroll().offset;
this.bg.style.top=_426.y+"px";
this.bg.style.left=_426.x+"px";
this.placeModalDialog();
},checkSize:function(){
if(this.isShowing()){
this._sizeBackground();
this.placeModalDialog();
this.onResized();
}
},onBackgroundClick:function(){
if(this.lifetime-this.timeRemaining>=this.blockDuration){
return;
}
this.hide();
}});
dojo.widget.defineWidget("dojo.widget.Dialog",[dojo.widget.ContentPane,dojo.widget.ModalDialogBase],{templateString:"<div id=\"${this.widgetId}\" class=\"dojoDialog\" dojoattachpoint=\"wrapper\">\n\t<span dojoattachpoint=\"tabStartOuter\" dojoonfocus=\"trapTabs\" dojoonblur=\"clearTrap\"\ttabindex=\"0\"></span>\n\t<span dojoattachpoint=\"tabStart\" dojoonfocus=\"trapTabs\" dojoonblur=\"clearTrap\" tabindex=\"0\"></span>\n\t<div dojoattachpoint=\"containerNode\" style=\"position: relative; z-index: 2;\"></div>\n\t<span dojoattachpoint=\"tabEnd\" dojoonfocus=\"trapTabs\" dojoonblur=\"clearTrap\" tabindex=\"0\"></span>\n\t<span dojoattachpoint=\"tabEndOuter\" dojoonfocus=\"trapTabs\" dojoonblur=\"clearTrap\" tabindex=\"0\"></span>\n</div>\n",blockDuration:0,lifetime:0,closeNode:"",postMixInProperties:function(){
dojo.widget.Dialog.superclass.postMixInProperties.apply(this,arguments);
if(this.closeNode){
this.setCloseControl(this.closeNode);
}
},postCreate:function(){
dojo.widget.Dialog.superclass.postCreate.apply(this,arguments);
dojo.widget.ModalDialogBase.prototype.postCreate.apply(this,arguments);
},show:function(){
if(this.lifetime){
this.timeRemaining=this.lifetime;
if(this.timerNode){
this.timerNode.innerHTML=Math.ceil(this.timeRemaining/1000);
}
if(this.blockDuration&&this.closeNode){
if(this.lifetime>this.blockDuration){
this.closeNode.style.visibility="hidden";
}else{
this.closeNode.style.display="none";
}
}
if(this.timer){
clearInterval(this.timer);
}
this.timer=setInterval(dojo.lang.hitch(this,"_onTick"),100);
}
this.showModalDialog();
dojo.widget.Dialog.superclass.show.call(this);
},onLoad:function(){
this.placeModalDialog();
dojo.widget.Dialog.superclass.onLoad.call(this);
},fillInTemplate:function(){
},hide:function(){
this.hideModalDialog();
dojo.widget.Dialog.superclass.hide.call(this);
if(this.timer){
clearInterval(this.timer);
}
},setTimerNode:function(node){
this.timerNode=node;
},setCloseControl:function(node){
this.closeNode=dojo.byId(node);
dojo.event.connect(this.closeNode,"onclick",this,"hide");
},setShowControl:function(node){
node=dojo.byId(node);
dojo.event.connect(node,"onclick",this,"show");
},_onTick:function(){
if(this.timer){
this.timeRemaining-=100;
if(this.lifetime-this.timeRemaining>=this.blockDuration){
if(this.closeNode){
this.closeNode.style.visibility="visible";
}
}
if(!this.timeRemaining){
clearInterval(this.timer);
this.hide();
}else{
if(this.timerNode){
this.timerNode.innerHTML=Math.ceil(this.timeRemaining/1000);
}
}
}
}});
dojo.provide("dojo.widget.ComboBox");
dojo.require("dojo.event.*");
dojo.require("dojo.html.*");
dojo.require("dojo.string");
dojo.declare("dojo.widget.incrementalComboBoxDataProvider",null,function(_42a){
this.searchUrl=_42a.dataUrl;
this._cache={};
this._inFlight=false;
this._lastRequest=null;
this.allowCache=false;
},{_addToCache:function(_42b,data){
if(this.allowCache){
this._cache[_42b]=data;
}
},startSearch:function(_42d,_42e){
if(this._inFlight){
}
var tss=encodeURIComponent(_42d);
var _430=dojo.string.substituteParams(this.searchUrl,{"searchString":tss});
var _431=this;
var _432=this._lastRequest=dojo.io.bind({url:_430,method:"get",mimetype:"text/json",load:function(type,data,evt){
_431._inFlight=false;
if(!dojo.lang.isArray(data)){
var _436=[];
for(var key in data){
_436.push([data[key],key]);
}
data=_436;
}
_431._addToCache(_42d,data);
if(_432==_431._lastRequest){
_42e(data);
}
}});
this._inFlight=true;
}});
dojo.declare("dojo.widget.basicComboBoxDataProvider",null,function(_438,node){
this._data=[];
this.searchLimit=30;
this.searchType="STARTSTRING";
this.caseSensitive=false;
if(!dj_undef("dataUrl",_438)&&!dojo.string.isBlank(_438.dataUrl)){
this._getData(_438.dataUrl);
}else{
if((node)&&(node.nodeName.toLowerCase()=="select")){
var opts=node.getElementsByTagName("option");
var ol=opts.length;
var data=[];
for(var x=0;x<ol;x++){
var text=opts[x].textContent||opts[x].innerText||opts[x].innerHTML;
var _43f=[String(text),String(opts[x].value)];
data.push(_43f);
if(opts[x].selected){
_438.setAllValues(_43f[0],_43f[1]);
}
}
this.setData(data);
}
}
},{_getData:function(url){
dojo.io.bind({url:url,load:dojo.lang.hitch(this,function(type,data,evt){
if(!dojo.lang.isArray(data)){
var _444=[];
for(var key in data){
_444.push([data[key],key]);
}
data=_444;
}
this.setData(data);
}),mimetype:"text/json"});
},startSearch:function(_446,_447){
this._performSearch(_446,_447);
},_performSearch:function(_448,_449){
var st=this.searchType;
var ret=[];
if(!this.caseSensitive){
_448=_448.toLowerCase();
}
for(var x=0;x<this._data.length;x++){
if((this.searchLimit>0)&&(ret.length>=this.searchLimit)){
break;
}
var _44d=new String((!this.caseSensitive)?this._data[x][0].toLowerCase():this._data[x][0]);
if(_44d.length<_448.length){
continue;
}
if(st=="STARTSTRING"){
if(_448==_44d.substr(0,_448.length)){
ret.push(this._data[x]);
}
}else{
if(st=="SUBSTRING"){
if(_44d.indexOf(_448)>=0){
ret.push(this._data[x]);
}
}else{
if(st=="STARTWORD"){
var idx=_44d.indexOf(_448);
if(idx==0){
ret.push(this._data[x]);
}
if(idx<=0){
continue;
}
var _44f=false;
while(idx!=-1){
if(" ,/(".indexOf(_44d.charAt(idx-1))!=-1){
_44f=true;
break;
}
idx=_44d.indexOf(_448,idx+1);
}
if(!_44f){
continue;
}else{
ret.push(this._data[x]);
}
}
}
}
}
_449(ret);
},setData:function(_450){
this._data=_450;
}});
dojo.widget.defineWidget("dojo.widget.ComboBox",dojo.widget.HtmlWidget,{forceValidOption:false,searchType:"stringstart",dataProvider:null,autoComplete:true,searchDelay:100,dataUrl:"",fadeTime:200,maxListLength:8,mode:"local",selectedResult:null,dataProviderClass:"",buttonSrc:dojo.uri.moduleUri("dojo.widget","templates/images/combo_box_arrow.png"),dropdownToggle:"fade",templateString:"<span _=\"whitespace and CR's between tags adds &nbsp; in FF\"\n\tclass=\"dojoComboBoxOuter\"\n\t><input style=\"display:none\"  tabindex=\"-1\" name=\"\" value=\"\" \n\t\tdojoAttachPoint=\"comboBoxValue\"\n\t><input style=\"display:none\"  tabindex=\"-1\" name=\"\" value=\"\" \n\t\tdojoAttachPoint=\"comboBoxSelectionValue\"\n\t><input type=\"text\" autocomplete=\"off\" class=\"dojoComboBox\"\n\t\tdojoAttachEvent=\"key:_handleKeyEvents; keyUp: onKeyUp; compositionEnd; onResize;\"\n\t\tdojoAttachPoint=\"textInputNode\"\n\t><img hspace=\"0\"\n\t\tvspace=\"0\"\n\t\tclass=\"dojoComboBox\"\n\t\tdojoAttachPoint=\"downArrowNode\"\n\t\tdojoAttachEvent=\"onMouseUp: handleArrowClick; onResize;\"\n\t\tsrc=\"${this.buttonSrc}\"\n></span>\n",templateCssString:".dojoComboBoxOuter {\n\tborder: 0px !important;\n\tmargin: 0px !important;\n\tpadding: 0px !important;\n\tbackground: transparent !important;\n\twhite-space: nowrap !important;\n}\n\n.dojoComboBox {\n\tborder: 1px inset #afafaf;\n\tmargin: 0px;\n\tpadding: 0px;\n\tvertical-align: middle !important;\n\tfloat: none !important;\n\tposition: static !important;\n\tdisplay: inline !important;\n}\n\n/* the input box */\ninput.dojoComboBox {\n\tborder-right-width: 0px !important; \n\tmargin-right: 0px !important;\n\tpadding-right: 0px !important;\n}\n\n/* the down arrow */\nimg.dojoComboBox {\n\tborder-left-width: 0px !important;\n\tpadding-left: 0px !important;\n\tmargin-left: 0px !important;\n}\n\n/* IE vertical-alignment calculations can be off by +-1 but these margins are collapsed away */\n.dj_ie img.dojoComboBox {\n\tmargin-top: 1px; \n\tmargin-bottom: 1px; \n}\n\n/* the drop down */\n.dojoComboBoxOptions {\n\tfont-family: Verdana, Helvetica, Garamond, sans-serif;\n\t/* font-size: 0.7em; */\n\tbackground-color: white;\n\tborder: 1px solid #afafaf;\n\tposition: absolute;\n\tz-index: 1000; \n\toverflow: auto;\n\tcursor: default;\n}\n\n.dojoComboBoxItem {\n\tpadding-left: 2px;\n\tpadding-top: 2px;\n\tmargin: 0px;\n}\n\n.dojoComboBoxItemEven {\n\tbackground-color: #f4f4f4;\n}\n\n.dojoComboBoxItemOdd {\n\tbackground-color: white;\n}\n\n.dojoComboBoxItemHighlight {\n\tbackground-color: #63709A;\n\tcolor: white;\n}\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/ComboBox.css"),setValue:function(_451){
this.comboBoxValue.value=_451;
if(this.textInputNode.value!=_451){
this.textInputNode.value=_451;
dojo.widget.html.stabile.setState(this.widgetId,this.getState(),true);
this.onValueChanged(_451);
}
},onValueChanged:function(_452){
},getValue:function(){
return this.comboBoxValue.value;
},getState:function(){
return {value:this.getValue()};
},setState:function(_453){
this.setValue(_453.value);
},enable:function(){
this.disabled=false;
this.textInputNode.removeAttribute("disabled");
},disable:function(){
this.disabled=true;
this.textInputNode.setAttribute("disabled",true);
},_getCaretPos:function(_454){
if(dojo.lang.isNumber(_454.selectionStart)){
return _454.selectionStart;
}else{
if(dojo.render.html.ie){
var tr=document.selection.createRange().duplicate();
var ntr=_454.createTextRange();
tr.move("character",0);
ntr.move("character",0);
try{
ntr.setEndPoint("EndToEnd",tr);
return String(ntr.text).replace(/\r/g,"").length;
}
catch(e){
return 0;
}
}
}
},_setCaretPos:function(_457,_458){
_458=parseInt(_458);
this._setSelectedRange(_457,_458,_458);
},_setSelectedRange:function(_459,_45a,end){
if(!end){
end=_459.value.length;
}
if(_459.setSelectionRange){
_459.focus();
_459.setSelectionRange(_45a,end);
}else{
if(_459.createTextRange){
var _45c=_459.createTextRange();
with(_45c){
collapse(true);
moveEnd("character",end);
moveStart("character",_45a);
select();
}
}else{
_459.value=_459.value;
_459.blur();
_459.focus();
var dist=parseInt(_459.value.length)-end;
var _45e=String.fromCharCode(37);
var tcc=_45e.charCodeAt(0);
for(var x=0;x<dist;x++){
var te=document.createEvent("KeyEvents");
te.initKeyEvent("keypress",true,true,null,false,false,false,false,tcc,tcc);
_459.dispatchEvent(te);
}
}
}
},_handleKeyEvents:function(evt){
if(evt.ctrlKey||evt.altKey||!evt.key){
return;
}
this._prev_key_backspace=false;
this._prev_key_esc=false;
var k=dojo.event.browser.keys;
var _464=true;
switch(evt.key){
case k.KEY_DOWN_ARROW:
if(!this.popupWidget.isShowingNow){
this._startSearchFromInput();
}
this._highlightNextOption();
dojo.event.browser.stopEvent(evt);
return;
case k.KEY_UP_ARROW:
this._highlightPrevOption();
dojo.event.browser.stopEvent(evt);
return;
case k.KEY_TAB:
if(!this.autoComplete&&this.popupWidget.isShowingNow&&this._highlighted_option){
dojo.event.browser.stopEvent(evt);
this._selectOption({"target":this._highlighted_option,"noHide":false});
this._setSelectedRange(this.textInputNode,this.textInputNode.value.length,null);
}else{
this._selectOption();
return;
}
break;
case k.KEY_ENTER:
if(this.popupWidget.isShowingNow){
dojo.event.browser.stopEvent(evt);
}
if(this.autoComplete){
this._selectOption();
return;
}
case " ":
if(this.popupWidget.isShowingNow&&this._highlighted_option){
dojo.event.browser.stopEvent(evt);
this._selectOption();
this._hideResultList();
return;
}
break;
case k.KEY_ESCAPE:
this._hideResultList();
this._prev_key_esc=true;
return;
case k.KEY_BACKSPACE:
this._prev_key_backspace=true;
if(!this.textInputNode.value.length){
this.setAllValues("","");
this._hideResultList();
_464=false;
}
break;
case k.KEY_RIGHT_ARROW:
case k.KEY_LEFT_ARROW:
_464=false;
break;
default:
if(evt.charCode==0){
_464=false;
}
}
if(this.searchTimer){
clearTimeout(this.searchTimer);
}
if(_464){
this._blurOptionNode();
this.searchTimer=setTimeout(dojo.lang.hitch(this,this._startSearchFromInput),this.searchDelay);
}
},compositionEnd:function(evt){
evt.key=evt.keyCode;
this._handleKeyEvents(evt);
},onKeyUp:function(evt){
this.setValue(this.textInputNode.value);
},setSelectedValue:function(_467){
this.comboBoxSelectionValue.value=_467;
},setAllValues:function(_468,_469){
this.setSelectedValue(_469);
this.setValue(_468);
},_focusOptionNode:function(node){
if(this._highlighted_option!=node){
this._blurOptionNode();
this._highlighted_option=node;
dojo.html.addClass(this._highlighted_option,"dojoComboBoxItemHighlight");
}
},_blurOptionNode:function(){
if(this._highlighted_option){
dojo.html.removeClass(this._highlighted_option,"dojoComboBoxItemHighlight");
this._highlighted_option=null;
}
},_highlightNextOption:function(){
if((!this._highlighted_option)||!this._highlighted_option.parentNode){
this._focusOptionNode(this.optionsListNode.firstChild);
}else{
if(this._highlighted_option.nextSibling){
this._focusOptionNode(this._highlighted_option.nextSibling);
}
}
dojo.html.scrollIntoView(this._highlighted_option);
},_highlightPrevOption:function(){
if(this._highlighted_option&&this._highlighted_option.previousSibling){
this._focusOptionNode(this._highlighted_option.previousSibling);
}else{
this._highlighted_option=null;
this._hideResultList();
return;
}
dojo.html.scrollIntoView(this._highlighted_option);
},_itemMouseOver:function(evt){
if(evt.target===this.optionsListNode){
return;
}
this._focusOptionNode(evt.target);
dojo.html.addClass(this._highlighted_option,"dojoComboBoxItemHighlight");
},_itemMouseOut:function(evt){
if(evt.target===this.optionsListNode){
return;
}
this._blurOptionNode();
},onResize:function(){
var _46d=dojo.html.getContentBox(this.textInputNode);
if(_46d.height<=0){
dojo.lang.setTimeout(this,"onResize",100);
return;
}
var _46e={width:_46d.height,height:_46d.height};
dojo.html.setContentBox(this.downArrowNode,_46e);
},fillInTemplate:function(args,frag){
dojo.html.applyBrowserClass(this.domNode);
var _471=this.getFragNodeRef(frag);
if(!this.name&&_471.name){
this.name=_471.name;
}
this.comboBoxValue.name=this.name;
this.comboBoxSelectionValue.name=this.name+"_selected";
dojo.html.copyStyle(this.domNode,_471);
dojo.html.copyStyle(this.textInputNode,_471);
dojo.html.copyStyle(this.downArrowNode,_471);
with(this.downArrowNode.style){
width="0px";
height="0px";
}
var _472;
if(this.dataProviderClass){
if(typeof this.dataProviderClass=="string"){
_472=dojo.evalObjPath(this.dataProviderClass);
}else{
_472=this.dataProviderClass;
}
}else{
if(this.mode=="remote"){
_472=dojo.widget.incrementalComboBoxDataProvider;
}else{
_472=dojo.widget.basicComboBoxDataProvider;
}
}
this.dataProvider=new _472(this,this.getFragNodeRef(frag));
this.popupWidget=new dojo.widget.createWidget("PopupContainer",{toggle:this.dropdownToggle,toggleDuration:this.toggleDuration});
dojo.event.connect(this,"destroy",this.popupWidget,"destroy");
this.optionsListNode=this.popupWidget.domNode;
this.domNode.appendChild(this.optionsListNode);
dojo.html.addClass(this.optionsListNode,"dojoComboBoxOptions");
dojo.event.connect(this.optionsListNode,"onclick",this,"_selectOption");
dojo.event.connect(this.optionsListNode,"onmouseover",this,"_onMouseOver");
dojo.event.connect(this.optionsListNode,"onmouseout",this,"_onMouseOut");
dojo.event.connect(this.optionsListNode,"onmouseover",this,"_itemMouseOver");
dojo.event.connect(this.optionsListNode,"onmouseout",this,"_itemMouseOut");
},_openResultList:function(_473){
if(this.disabled){
return;
}
this._clearResultList();
if(!_473.length){
this._hideResultList();
}
if((this.autoComplete)&&(_473.length)&&(!this._prev_key_backspace)&&(this.textInputNode.value.length>0)){
var cpos=this._getCaretPos(this.textInputNode);
if((cpos+1)>this.textInputNode.value.length){
this.textInputNode.value+=_473[0][0].substr(cpos);
this._setSelectedRange(this.textInputNode,cpos,this.textInputNode.value.length);
}
}
var even=true;
while(_473.length){
var tr=_473.shift();
if(tr){
var td=document.createElement("div");
td.appendChild(document.createTextNode(tr[0]));
td.setAttribute("resultName",tr[0]);
td.setAttribute("resultValue",tr[1]);
td.className="dojoComboBoxItem "+((even)?"dojoComboBoxItemEven":"dojoComboBoxItemOdd");
even=(!even);
this.optionsListNode.appendChild(td);
}
}
this._showResultList();
},_onFocusInput:function(){
this._hasFocus=true;
},_onBlurInput:function(){
this._hasFocus=false;
this._handleBlurTimer(true,500);
},_handleBlurTimer:function(_478,_479){
if(this.blurTimer&&(_478||_479)){
clearTimeout(this.blurTimer);
}
if(_479){
this.blurTimer=dojo.lang.setTimeout(this,"_checkBlurred",_479);
}
},_onMouseOver:function(evt){
if(!this._mouseover_list){
this._handleBlurTimer(true,0);
this._mouseover_list=true;
}
},_onMouseOut:function(evt){
var _47c=evt.relatedTarget;
try{
if(!_47c||_47c.parentNode!=this.optionsListNode){
this._mouseover_list=false;
this._handleBlurTimer(true,100);
this._tryFocus();
}
}
catch(e){
}
},_isInputEqualToResult:function(_47d){
var _47e=this.textInputNode.value;
if(!this.dataProvider.caseSensitive){
_47e=_47e.toLowerCase();
_47d=_47d.toLowerCase();
}
return (_47e==_47d);
},_isValidOption:function(){
var tgt=dojo.html.firstElement(this.optionsListNode);
var _480=false;
while(!_480&&tgt){
if(this._isInputEqualToResult(tgt.getAttribute("resultName"))){
_480=true;
}else{
tgt=dojo.html.nextElement(tgt);
}
}
return _480;
},_checkBlurred:function(){
if(!this._hasFocus&&!this._mouseover_list){
this._hideResultList();
if(!this.textInputNode.value.length){
this.setAllValues("","");
return;
}
var _481=this._isValidOption();
if(this.forceValidOption&&!_481){
this.setAllValues("","");
return;
}
if(!_481){
this.setSelectedValue("");
}
}
},_selectOption:function(evt){
var tgt=null;
if(!evt){
evt={target:this._highlighted_option};
}
if(!dojo.html.isDescendantOf(evt.target,this.optionsListNode)){
if(!this.textInputNode.value.length){
return;
}
tgt=dojo.html.firstElement(this.optionsListNode);
if(!tgt||!this._isInputEqualToResult(tgt.getAttribute("resultName"))){
return;
}
}else{
tgt=evt.target;
}
while((tgt.nodeType!=1)||(!tgt.getAttribute("resultName"))){
tgt=tgt.parentNode;
if(tgt===dojo.body()){
return false;
}
}
this.selectedResult=[tgt.getAttribute("resultName"),tgt.getAttribute("resultValue")];
this.setAllValues(tgt.getAttribute("resultName"),tgt.getAttribute("resultValue"));
if(!evt.noHide){
this._hideResultList();
this._setSelectedRange(this.textInputNode,0,null);
}
this._tryFocus();
},_clearResultList:function(){
if(this.optionsListNode.innerHTML){
this.optionsListNode.innerHTML="";
}
},_hideResultList:function(){
this.popupWidget.close();
},_showResultList:function(){
var _484=this.optionsListNode.childNodes;
if(_484.length){
var _485=Math.min(_484.length,this.maxListLength);
with(this.optionsListNode.style){
display="";
if(_485==_484.length){
height="";
}else{
height=_485*dojo.html.getMarginBox(_484[0]).height+"px";
}
width=(dojo.html.getMarginBox(this.domNode).width-2)+"px";
}
this.popupWidget.open(this.domNode,this,this.downArrowNode);
}else{
this._hideResultList();
}
},handleArrowClick:function(){
this._handleBlurTimer(true,0);
this._tryFocus();
if(this.popupWidget.isShowingNow){
this._hideResultList();
}else{
this._startSearch("");
}
},_tryFocus:function(){
try{
this.textInputNode.focus();
}
catch(e){
}
},_startSearchFromInput:function(){
this._startSearch(this.textInputNode.value);
},_startSearch:function(key){
this.dataProvider.startSearch(key,dojo.lang.hitch(this,"_openResultList"));
},postCreate:function(){
this.onResize();
dojo.event.connect(this.textInputNode,"onblur",this,"_onBlurInput");
dojo.event.connect(this.textInputNode,"onfocus",this,"_onFocusInput");
if(this.disabled){
this.disable();
}
var s=dojo.widget.html.stabile.getState(this.widgetId);
if(s){
this.setState(s);
}
}});
dojo.provide("dojo.widget.Select");
dojo.widget.defineWidget("dojo.widget.Select",dojo.widget.ComboBox,{forceValidOption:true,setValue:function(_488){
this.comboBoxValue.value=_488;
dojo.widget.html.stabile.setState(this.widgetId,this.getState(),true);
this.onValueChanged(_488);
},setLabel:function(_489){
this.comboBoxSelectionValue.value=_489;
if(this.textInputNode.value!=_489){
this.textInputNode.value=_489;
}
},getLabel:function(){
return this.comboBoxSelectionValue.value;
},getState:function(){
return {value:this.getValue(),label:this.getLabel()};
},onKeyUp:function(evt){
this.setLabel(this.textInputNode.value);
},setState:function(_48b){
this.setValue(_48b.value);
this.setLabel(_48b.label);
},setAllValues:function(_48c,_48d){
this.setLabel(_48c);
this.setValue(_48d);
}});
