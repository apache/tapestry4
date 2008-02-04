dojo.provide("dojo.data.OpmlStore");
dojo.require("dojo.data.core.Read");
dojo.require("dojo.data.core.Result");
dojo.require("dojo.lang.assert");
dojo.require("dojo.json");
dojo.require("dojo.experimental");
dojo.experimental("dojo.data.OpmlStore");
dojo.declare("dojo.data.OpmlStore",dojo.data.core.Read,{initializer:function(_1){
this._arrayOfTopLevelItems=[];
this._metadataNodes=null;
this._loadFinished=false;
this._opmlFileUrl=_1["url"];
},_assertIsItem:function(_2){
if(!this.isItem(_2)){
throw new Error("dojo.data.OpmlStore: a function was passed an item argument that was not an item");
}
},_removeChildNodesThatAreNotElementNodes:function(_3,_4){
var _5=_3.childNodes;
if(_5.length==0){
return;
}
var _6=[];
var i,_8;
for(i=0;i<_5.length;++i){
_8=_5[i];
if(_8.nodeType!=Node.ELEMENT_NODE){
_6.push(_8);
}
}
for(i=0;i<_6.length;++i){
_8=_6[i];
_3.removeChild(_8);
}
if(_4){
for(i=0;i<_5.length;++i){
_8=_5[i];
this._removeChildNodesThatAreNotElementNodes(_8,_4);
}
}
},_processRawXmlTree:function(_9){
var _a=_9.getElementsByTagName("head");
var _b=_a[0];
this._removeChildNodesThatAreNotElementNodes(_b);
this._metadataNodes=_b.childNodes;
var _c=_9.getElementsByTagName("body");
var _d=_c[0];
this._removeChildNodesThatAreNotElementNodes(_d,true);
var _e=_c[0].childNodes;
for(var i=0;i<_e.length;++i){
var _10=_e[i];
if(_10.tagName=="outline"){
this._arrayOfTopLevelItems.push(_10);
}
}
},get:function(_11,_12,_13){
this._assertIsItem(_11);
if(_12=="children"){
return (_11.firstChild||_13);
}else{
var _14=_11.getAttribute(_12);
_14=(_14!=undefined)?_14:_13;
return _14;
}
},getValues:function(_15,_16){
this._assertIsItem(_15);
if(_16=="children"){
var _17=[];
for(var i=0;i<_15.childNodes.length;++i){
_17.push(_15.childNodes[i]);
}
return _17;
}else{
return [_15.getAttribute(_16)];
}
},getAttributes:function(_19){
this._assertIsItem(_19);
var _1a=[];
var _1b=_19;
var _1c=_1b.attributes;
for(var i=0;i<_1c.length;++i){
var _1e=_1c.item(i);
_1a.push(_1e.nodeName);
}
if(_1b.childNodes.length>0){
_1a.push("children");
}
return _1a;
},hasAttribute:function(_1f,_20){
return (this.getValues(_1f,_20).length>0);
},containsValue:function(_21,_22,_23){
var _24=this.getValues(_21,_22);
for(var i=0;i<_24.length;++i){
var _26=_24[i];
if(_23==_26){
return true;
}
}
return false;
},isItem:function(_27){
return (_27&&_27.nodeType==Node.ELEMENT_NODE&&_27.tagName=="outline");
},isItemAvailable:function(_28){
return this.isItem(_28);
},find:function(_29){
var _2a=null;
if(_29 instanceof dojo.data.core.Result){
_2a=_29;
_2a.store=this;
}else{
_2a=new dojo.data.core.Result(_29,this);
}
var _2b=this;
var _2c=function(_2d,_2e,evt){
var _30=_2a.scope||dj_global;
if(_2d=="load"){
_2b._processRawXmlTree(_2e);
if(_2a.saveResult){
_2a.items=_2b._arrayOfTopLevelItems;
}
if(_2a.onbegin){
_2a.onbegin.call(_30,_2a);
}
for(var i=0;i<_2b._arrayOfTopLevelItems.length;i++){
var _32=_2b._arrayOfTopLevelItems[i];
if(_2a.onnext&&!_2a._aborted){
_2a.onnext.call(_30,_32,_2a);
}
}
if(_2a.oncompleted&&!_2a._aborted){
_2a.oncompleted.call(_30,_2a);
}
}else{
if(_2d=="error"||_2d=="timeout"){
var _33=_2e;
if(_2a.onerror){
_2a.onerror.call(_30,_2e);
}
}
}
};
if(!this._loadFinished){
if(this._opmlFileUrl){
var _34=dojo.io.bind({url:this._opmlFileUrl,handle:_2c,mimetype:"text/xml",sync:(_2a.sync||false)});
_2a._abortFunc=_34.abort;
}
}
return _2a;
},getIdentity:function(_35){
dojo.unimplemented("dojo.data.OpmlStore.getIdentity()");
return null;
},findByIdentity:function(_36){
dojo.unimplemented("dojo.data.OpmlStore.findByIdentity()");
return null;
}});
