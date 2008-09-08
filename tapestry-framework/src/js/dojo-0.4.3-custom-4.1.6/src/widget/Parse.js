dojo.provide("dojo.widget.Parse");
dojo.require("dojo.widget.Manager");
dojo.require("dojo.dom");
dojo.widget.Parse=function(_1){
this.propertySetsList=[];
this.fragment=_1;
this.createComponents=function(_2,_3){
var _4=[];
var _5=false;
try{
if(_2&&_2.tagName&&(_2!=_2.nodeRef)){
var _6=dojo.widget.tags;
var _7=String(_2.tagName).split(";");
for(var x=0;x<_7.length;x++){
var _9=_7[x].replace(/^\s+|\s+$/g,"").toLowerCase();
_2.tagName=_9;
var _a;
if(_6[_9]){
_5=true;
_a=_6[_9](_2,this,_3,_2.index);
_4.push(_a);
}else{
if(_9.indexOf(":")==-1){
_9="dojo:"+_9;
}
_a=dojo.widget.buildWidgetFromParseTree(_9,_2,this,_3,_2.index);
if(_a){
_5=true;
_4.push(_a);
}
}
}
}
}
catch(e){
dojo.debug("dojo.widget.Parse: error:",e);
}
if(!_5){
_4=_4.concat(this.createSubComponents(_2,_3));
}
return _4;
};
this.createSubComponents=function(_b,_c){
var _d,_e=[];
for(var _f in _b){
_d=_b[_f];
if(_d&&typeof _d=="object"&&(_d!=_b.nodeRef)&&(_d!=_b.tagName)&&(!dojo.dom.isNode(_d))){
_e=_e.concat(this.createComponents(_d,_c));
}
}
return _e;
};
this.parsePropertySets=function(_10){
return [];
};
this.parseProperties=function(_11){
var _12={};
for(var _13 in _11){
if((_11[_13]==_11.tagName)||(_11[_13]==_11.nodeRef)){
}else{
var _14=_11[_13];
if(_14.tagName&&dojo.widget.tags[_14.tagName.toLowerCase()]){
}else{
if(_14[0]&&_14[0].value!=""&&_14[0].value!=null){
try{
if(_13.toLowerCase()=="dataprovider"){
var _15=this;
this.getDataProvider(_15,_14[0].value);
_12.dataProvider=this.dataProvider;
}
_12[_13]=_14[0].value;
var _16=this.parseProperties(_14);
for(var _17 in _16){
_12[_17]=_16[_17];
}
}
catch(e){
dojo.debug(e);
}
}
}
switch(_13.toLowerCase()){
case "checked":
case "disabled":
if(typeof _12[_13]!="boolean"){
_12[_13]=true;
}
break;
}
}
}
return _12;
};
this.getDataProvider=function(_18,_19){
dojo.io.bind({url:_19,load:function(_1a,_1b){
if(_1a=="load"){
_18.dataProvider=_1b;
}
},mimetype:"text/javascript",sync:true});
};
this.getPropertySetById=function(_1c){
for(var x=0;x<this.propertySetsList.length;x++){
if(_1c==this.propertySetsList[x]["id"][0].value){
return this.propertySetsList[x];
}
}
return "";
};
this.getPropertySetsByType=function(_1e){
var _1f=[];
for(var x=0;x<this.propertySetsList.length;x++){
var cpl=this.propertySetsList[x];
var _22=cpl.componentClass||cpl.componentType||null;
var _23=this.propertySetsList[x]["id"][0].value;
if(_22&&(_23==_22[0].value)){
_1f.push(cpl);
}
}
return _1f;
};
this.getPropertySets=function(_24){
var ppl="dojo:propertyproviderlist";
var _26=[];
var _27=_24.tagName;
if(_24[ppl]){
var _28=_24[ppl].value.split(" ");
for(var _29 in _28){
if((_29.indexOf("..")==-1)&&(_29.indexOf("://")==-1)){
var _2a=this.getPropertySetById(_29);
if(_2a!=""){
_26.push(_2a);
}
}else{
}
}
}
return this.getPropertySetsByType(_27).concat(_26);
};
this.createComponentFromScript=function(_2b,_2c,_2d,ns){
_2d.fastMixIn=true;
var ltn=(ns||"dojo")+":"+_2c.toLowerCase();
if(dojo.widget.tags[ltn]){
return [dojo.widget.tags[ltn](_2d,this,null,null,_2d)];
}
return [dojo.widget.buildWidgetFromParseTree(ltn,_2d,this,null,null,_2d)];
};
};
dojo.widget._parser_collection={"dojo":new dojo.widget.Parse()};
dojo.widget.getParser=function(_30){
if(!_30){
_30="dojo";
}
if(!this._parser_collection[_30]){
this._parser_collection[_30]=new dojo.widget.Parse();
}
return this._parser_collection[_30];
};
dojo.widget.createWidget=function(_31,_32,_33,_34){
var _35=false;
var _36=(typeof _31=="string");
if(_36){
var pos=_31.indexOf(":");
var ns=(pos>-1)?_31.substring(0,pos):"dojo";
if(pos>-1){
_31=_31.substring(pos+1);
}
var _39=_31.toLowerCase();
var _3a=ns+":"+_39;
_35=(dojo.byId(_31)&&!dojo.widget.tags[_3a]);
}
if((arguments.length==1)&&(_35||!_36)){
var xp=new dojo.xml.Parse();
var tn=_35?dojo.byId(_31):_31;
return dojo.widget.getParser().createComponents(xp.parseElement(tn,null,true))[0];
}
function fromScript(_3d,_3e,_3f,ns){
_3f[_3a]={dojotype:[{value:_39}],nodeRef:_3d,fastMixIn:true};
_3f.ns=ns;
return dojo.widget.getParser().createComponentFromScript(_3d,_3e,_3f,ns);
}
_32=_32||{};
var _41=false;
var tn=null;
var h=dojo.render.html.capable;
if(h){
tn=document.createElement("span");
}
if(!_33){
_41=true;
_33=tn;
if(h){
dojo.body().appendChild(_33);
}
}else{
if(_34){
dojo.dom.insertAtPosition(tn,_33,_34);
}else{
tn=_33;
}
}
var _43=fromScript(tn,_31.toLowerCase(),_32,ns);
if((!_43)||(!_43[0])||(typeof _43[0].widgetType=="undefined")){
throw new Error("createWidget: Creation of \""+_31+"\" widget failed.");
}
try{
if(_41&&_43[0].domNode.parentNode){
_43[0].domNode.parentNode.removeChild(_43[0].domNode);
}
}
catch(e){
dojo.debug(e);
}
return _43[0];
};
