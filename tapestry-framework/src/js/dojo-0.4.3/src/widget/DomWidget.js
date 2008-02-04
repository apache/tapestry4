dojo.provide("dojo.widget.DomWidget");
dojo.require("dojo.event.*");
dojo.require("dojo.widget.Widget");
dojo.require("dojo.dom");
dojo.require("dojo.html.style");
dojo.require("dojo.xml.Parse");
dojo.require("dojo.uri.*");
dojo.require("dojo.lang.func");
dojo.require("dojo.lang.extras");
dojo.widget._cssFiles={};
dojo.widget._cssStrings={};
dojo.widget._templateCache={};
dojo.widget.defaultStrings={dojoRoot:dojo.hostenv.getBaseScriptUri(),dojoWidgetModuleUri:dojo.uri.moduleUri("dojo.widget"),baseScriptUri:dojo.hostenv.getBaseScriptUri()};
dojo.widget.fillFromTemplateCache=function(_1,_2,_3,_4){
var _5=_2||_1.templatePath;
var _6=dojo.widget._templateCache;
if(!_5&&!_1["widgetType"]){
do{
var _7="__dummyTemplate__"+dojo.widget._templateCache.dummyCount++;
}while(_6[_7]);
_1.widgetType=_7;
}
var wt=_5?_5.toString():_1.widgetType;
var ts=_6[wt];
if(!ts){
_6[wt]={"string":null,"node":null};
if(_4){
ts={};
}else{
ts=_6[wt];
}
}
if((!_1.templateString)&&(!_4)){
_1.templateString=_3||ts["string"];
}
if(_1.templateString){
_1.templateString=this._sanitizeTemplateString(_1.templateString);
}
if((!_1.templateNode)&&(!_4)){
_1.templateNode=ts["node"];
}
if((!_1.templateNode)&&(!_1.templateString)&&(_5)){
var _a=this._sanitizeTemplateString(dojo.hostenv.getText(_5));
_1.templateString=_a;
if(!_4){
_6[wt]["string"]=_a;
}
}
if((!ts["string"])&&(!_4)){
ts.string=_1.templateString;
}
};
dojo.widget._sanitizeTemplateString=function(_b){
if(_b){
_b=_b.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im,"");
var _c=_b.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
if(_c){
_b=_c[1];
}
}else{
_b="";
}
return _b;
};
dojo.widget._templateCache.dummyCount=0;
dojo.widget.attachProperties=["dojoAttachPoint","id"];
dojo.widget.eventAttachProperty="dojoAttachEvent";
dojo.widget.onBuildProperty="dojoOnBuild";
dojo.widget.waiNames=["waiRole","waiState"];
dojo.widget.wai={waiRole:{name:"waiRole","namespace":"http://www.w3.org/TR/xhtml2",alias:"x2",prefix:"wairole:"},waiState:{name:"waiState","namespace":"http://www.w3.org/2005/07/aaa",alias:"aaa",prefix:""},setAttr:function(_d,ns,_f,_10){
if(dojo.render.html.ie){
_d.setAttribute(this[ns].alias+":"+_f,this[ns].prefix+_10);
}else{
_d.setAttributeNS(this[ns]["namespace"],_f,this[ns].prefix+_10);
}
},getAttr:function(_11,ns,_13){
if(dojo.render.html.ie){
return _11.getAttribute(this[ns].alias+":"+_13);
}else{
return _11.getAttributeNS(this[ns]["namespace"],_13);
}
},removeAttr:function(_14,ns,_16){
var _17=true;
if(dojo.render.html.ie){
_17=_14.removeAttribute(this[ns].alias+":"+_16);
}else{
_14.removeAttributeNS(this[ns]["namespace"],_16);
}
return _17;
}};
dojo.widget.attachTemplateNodes=function(_18,_19,_1a){
var _1b=dojo.dom.ELEMENT_NODE;
function trim(str){
return str.replace(/^\s+|\s+$/g,"");
}
if(!_18){
_18=_19.domNode;
}
if(_18.nodeType!=_1b){
return;
}
var _1d=_18.all||_18.getElementsByTagName("*");
var _1e=_19;
for(var x=-1;x<_1d.length;x++){
var _20=(x==-1)?_18:_1d[x];
var _21=[];
if(!_19.widgetsInTemplate||!_20.getAttribute("dojoType")){
for(var y=0;y<this.attachProperties.length;y++){
var _23=_20.getAttribute(this.attachProperties[y]);
if(_23){
_21=_23.split(";");
for(var z=0;z<_21.length;z++){
if(dojo.lang.isArray(_19[_21[z]])){
_19[_21[z]].push(_20);
}else{
_19[_21[z]]=_20;
}
}
break;
}
}
var _25=_20.getAttribute(this.eventAttachProperty);
if(_25){
var _26=_25.split(";");
for(var y=0;y<_26.length;y++){
if((!_26[y])||(!_26[y].length)){
continue;
}
var _27=null;
var _28=trim(_26[y]);
if(_26[y].indexOf(":")>=0){
var _29=_28.split(":");
_28=trim(_29[0]);
_27=trim(_29[1]);
}
if(!_27){
_27=_28;
}
var tf=function(){
var ntf=new String(_27);
return function(evt){
if(_1e[ntf]){
_1e[ntf](dojo.event.browser.fixEvent(evt,this));
}
};
}();
dojo.event.browser.addListener(_20,_28,tf,false,true);
}
}
for(var y=0;y<_1a.length;y++){
var _2d=_20.getAttribute(_1a[y]);
if((_2d)&&(_2d.length)){
var _27=null;
var _2e=_1a[y].substr(4);
_27=trim(_2d);
var _2f=[_27];
if(_27.indexOf(";")>=0){
_2f=dojo.lang.map(_27.split(";"),trim);
}
for(var z=0;z<_2f.length;z++){
if(!_2f[z].length){
continue;
}
var tf=function(){
var ntf=new String(_2f[z]);
return function(evt){
if(_1e[ntf]){
_1e[ntf](dojo.event.browser.fixEvent(evt,this));
}
};
}();
dojo.event.browser.addListener(_20,_2e,tf,false,true);
}
}
}
}
var _32=_20.getAttribute(this.templateProperty);
if(_32){
_19[_32]=_20;
}
dojo.lang.forEach(dojo.widget.waiNames,function(_33){
var wai=dojo.widget.wai[_33];
var val=_20.getAttribute(wai.name);
if(val){
if(val.indexOf("-")==-1){
dojo.widget.wai.setAttr(_20,wai.name,"role",val);
}else{
var _36=val.split("-");
dojo.widget.wai.setAttr(_20,wai.name,_36[0],_36[1]);
}
}
},this);
var _37=_20.getAttribute(this.onBuildProperty);
if(_37){
eval("var node = baseNode; var widget = targetObj; "+_37);
}
}
};
dojo.widget.getDojoEventsFromStr=function(str){
var re=/(dojoOn([a-z]+)(\s?))=/gi;
var _3a=str?str.match(re)||[]:[];
var ret=[];
var lem={};
for(var x=0;x<_3a.length;x++){
if(_3a[x].length<1){
continue;
}
var cm=_3a[x].replace(/\s/,"");
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
},{templateNode:null,templateString:null,templateCssString:null,preventClobber:false,domNode:null,containerNode:null,widgetsInTemplate:false,addChild:function(_3f,_40,pos,ref,_43){
if(!this.isContainer){
dojo.debug("dojo.widget.DomWidget.addChild() attempted on non-container widget");
return null;
}else{
if(_43==undefined){
_43=this.children.length;
}
this.addWidgetAsDirectChild(_3f,_40,pos,ref,_43);
this.registerChild(_3f,_43);
}
return _3f;
},addWidgetAsDirectChild:function(_44,_45,pos,ref,_48){
if((!this.containerNode)&&(!_45)){
this.containerNode=this.domNode;
}
var cn=(_45)?_45:this.containerNode;
if(!pos){
pos="after";
}
if(!ref){
if(!cn){
cn=dojo.body();
}
ref=cn.lastChild;
}
if(!_48){
_48=0;
}
_44.domNode.setAttribute("dojoinsertionindex",_48);
if(!ref){
cn.appendChild(_44.domNode);
}else{
if(pos=="insertAtIndex"){
dojo.dom.insertAtIndex(_44.domNode,ref.parentNode,_48);
}else{
if((pos=="after")&&(ref===cn.lastChild)){
cn.appendChild(_44.domNode);
}else{
dojo.dom.insertAtPosition(_44.domNode,cn,pos);
}
}
}
},registerChild:function(_4a,_4b){
_4a.dojoInsertionIndex=_4b;
var idx=-1;
for(var i=0;i<this.children.length;i++){
if(this.children[i].dojoInsertionIndex<=_4b){
idx=i;
}
}
this.children.splice(idx+1,0,_4a);
_4a.parent=this;
_4a.addedTo(this,idx+1);
delete dojo.widget.manager.topWidgets[_4a.widgetId];
},removeChild:function(_4e){
dojo.dom.removeNode(_4e.domNode);
return dojo.widget.DomWidget.superclass.removeChild.call(this,_4e);
},getFragNodeRef:function(_4f){
if(!_4f){
return null;
}
if(!_4f[this.getNamespacedType()]){
dojo.raise("Error: no frag for widget type "+this.getNamespacedType()+", id "+this.widgetId+" (maybe a widget has set it's type incorrectly)");
}
return _4f[this.getNamespacedType()]["nodeRef"];
},postInitialize:function(_50,_51,_52){
var _53=this.getFragNodeRef(_51);
if(_52&&(_52.snarfChildDomOutput||!_53)){
_52.addWidgetAsDirectChild(this,"","insertAtIndex","",_50["dojoinsertionindex"],_53);
}else{
if(_53){
if(this.domNode&&(this.domNode!==_53)){
this._sourceNodeRef=dojo.dom.replaceNode(_53,this.domNode);
}
}
}
if(_52){
_52.registerChild(this,_50.dojoinsertionindex);
}else{
dojo.widget.manager.topWidgets[this.widgetId]=this;
}
if(this.widgetsInTemplate){
var _54=new dojo.xml.Parse();
var _55;
var _56=this.domNode.getElementsByTagName("*");
for(var i=0;i<_56.length;i++){
if(_56[i].getAttribute("dojoAttachPoint")=="subContainerWidget"){
_55=_56[i];
}
if(_56[i].getAttribute("dojoType")){
_56[i].setAttribute("isSubWidget",true);
}
}
if(this.isContainer&&!this.containerNode){
if(_55){
var src=this.getFragNodeRef(_51);
if(src){
dojo.dom.moveChildren(src,_55);
_51["dojoDontFollow"]=true;
}
}else{
dojo.debug("No subContainerWidget node can be found in template file for widget "+this);
}
}
var _59=_54.parseElement(this.domNode,null,true);
dojo.widget.getParser().createSubComponents(_59,this);
var _5a=[];
var _5b=[this];
var w;
while((w=_5b.pop())){
for(var i=0;i<w.children.length;i++){
var _5d=w.children[i];
if(_5d._processedSubWidgets||!_5d.extraArgs["issubwidget"]){
continue;
}
_5a.push(_5d);
if(_5d.isContainer){
_5b.push(_5d);
}
}
}
for(var i=0;i<_5a.length;i++){
var _5e=_5a[i];
if(_5e._processedSubWidgets){
dojo.debug("This should not happen: widget._processedSubWidgets is already true!");
return;
}
_5e._processedSubWidgets=true;
if(_5e.extraArgs["dojoattachevent"]){
var _5f=_5e.extraArgs["dojoattachevent"].split(";");
for(var j=0;j<_5f.length;j++){
var _61=null;
var _62=dojo.string.trim(_5f[j]);
if(_62.indexOf(":")>=0){
var _63=_62.split(":");
_62=dojo.string.trim(_63[0]);
_61=dojo.string.trim(_63[1]);
}
if(!_61){
_61=_62;
}
if(dojo.lang.isFunction(_5e[_62])){
dojo.event.kwConnect({srcObj:_5e,srcFunc:_62,targetObj:this,targetFunc:_61});
}else{
alert(_62+" is not a function in widget "+_5e);
}
}
}
if(_5e.extraArgs["dojoattachpoint"]){
this[_5e.extraArgs["dojoattachpoint"]]=_5e;
}
}
}
if(this.isContainer&&!_51["dojoDontFollow"]){
dojo.widget.getParser().createSubComponents(_51,this);
}
},buildRendering:function(_64,_65){
var ts=dojo.widget._templateCache[this.widgetType];
if(_64["templatecsspath"]){
_64["templateCssPath"]=_64["templatecsspath"];
}
var _67=_64["templateCssPath"]||this.templateCssPath;
if(_67&&!dojo.widget._cssFiles[_67.toString()]){
if((!this.templateCssString)&&(_67)){
this.templateCssString=dojo.hostenv.getText(_67);
this.templateCssPath=null;
}
dojo.widget._cssFiles[_67.toString()]=true;
}
if((this["templateCssString"])&&(!dojo.widget._cssStrings[this.templateCssString])){
dojo.html.insertCssText(this.templateCssString,null,_67);
dojo.widget._cssStrings[this.templateCssString]=true;
}
if((!this.preventClobber)&&((this.templatePath)||(this.templateNode)||((this["templateString"])&&(this.templateString.length))||((typeof ts!="undefined")&&((ts["string"])||(ts["node"]))))){
this.buildFromTemplate(_64,_65);
}else{
this.domNode=this.getFragNodeRef(_65);
}
this.fillInTemplate(_64,_65);
},buildFromTemplate:function(_68,_69){
var _6a=false;
if(_68["templatepath"]){
_68["templatePath"]=_68["templatepath"];
}
dojo.widget.fillFromTemplateCache(this,_68["templatePath"],null,_6a);
var ts=dojo.widget._templateCache[this.templatePath?this.templatePath.toString():this.widgetType];
if((ts)&&(!_6a)){
if(!this.templateString.length){
this.templateString=ts["string"];
}
if(!this.templateNode){
this.templateNode=ts["node"];
}
}
var _6c=false;
var _6d=null;
var _6e=this.templateString;
if((!this.templateNode)&&(this.templateString)){
_6c=this.templateString.match(/\$\{([^\}]+)\}/g);
if(_6c){
var _6f=this.strings||{};
for(var key in dojo.widget.defaultStrings){
if(dojo.lang.isUndefined(_6f[key])){
_6f[key]=dojo.widget.defaultStrings[key];
}
}
for(var i=0;i<_6c.length;i++){
var key=_6c[i];
key=key.substring(2,key.length-1);
var _72=(key.substring(0,5)=="this.")?dojo.lang.getObjPathValue(key.substring(5),this):_6f[key];
var _73;
if((_72)||(dojo.lang.isString(_72))){
_73=new String((dojo.lang.isFunction(_72))?_72.call(this,key,this.templateString):_72);
while(_73.indexOf("\"")>-1){
_73=_73.replace("\"","&quot;");
}
_6e=_6e.replace(_6c[i],_73);
}
}
}else{
this.templateNode=this.createNodesFromText(this.templateString,true)[0];
if(!_6a){
ts.node=this.templateNode;
}
}
}
if((!this.templateNode)&&(!_6c)){
dojo.debug("DomWidget.buildFromTemplate: could not create template");
return false;
}else{
if(!_6c){
_6d=this.templateNode.cloneNode(true);
if(!_6d){
return false;
}
}else{
_6d=this.createNodesFromText(_6e,true)[0];
}
}
this.domNode=_6d;
this.attachTemplateNodes();
if(this.isContainer&&this.containerNode){
var src=this.getFragNodeRef(_69);
if(src){
dojo.dom.moveChildren(src,this.containerNode);
}
}
},attachTemplateNodes:function(_75,_76){
if(!_75){
_75=this.domNode;
}
if(!_76){
_76=this;
}
return dojo.widget.attachTemplateNodes(_75,_76,dojo.widget.getDojoEventsFromStr(this.templateString));
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
