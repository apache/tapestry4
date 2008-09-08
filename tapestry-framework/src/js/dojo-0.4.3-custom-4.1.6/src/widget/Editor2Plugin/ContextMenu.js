dojo.provide("dojo.widget.Editor2Plugin.ContextMenu");
dojo.require("dojo.widget.Menu2");
dojo.event.topic.subscribe("dojo.widget.Editor2::onLoad",function(_1){
dojo.widget.Editor2Plugin.ContextMenuManager.getContextMenu(_1);
});
dojo.widget.Editor2Plugin.ContextMenuManager={menuGroups:["Generic","Link","Anchor","Image","List","Table"],_contextMenuGroupSets:{},_registeredGroups:{},_menus:{},registerGroup:function(_2,_3){
if(this._registeredGroups[_2]){
alert("dojo.widget.Editor2Plugin.ContextMenuManager.registerGroup: menu group "+_2+"is already registered. Ignored.");
return;
}
this._registeredGroups[_2]=_3;
},removeGroup:function(_4){
delete this._registeredGroups[_4];
},getGroup:function(_5,_6){
if(this._registeredGroups[_5]){
var _7=this._registeredGroups[_5](_5,_6);
if(_7){
return _7;
}
}
switch(_5){
case "Generic":
case "Link":
case "Image":
return new dojo.widget.Editor2Plugin[_5+"ContextMenuGroup"](_6);
case "Anchor":
case "List":
}
},registerGroupSet:function(_8,_9){
this._contextMenuGroupSets[_8]=_9;
},removeGroupSet:function(_a){
var _b=this._contextMenuGroupSets[_a];
delete this._contextMenuGroupSets[_a];
return _b;
},getContextMenu:function(_c){
var _d=_c.contextMenuGroupSet||"defaultDojoEditor2MenuGroupSet";
if(this._menus[_d]){
this._menus[_d].bindEditor(_c);
return this._menus[_d];
}
var gs=(_c.contextMenuGroupSet&&this._contextMenuGroupSets[_c.contextMenuGroupSet])||this.menuGroups;
var _f=new dojo.widget.Editor2Plugin.ContextMenu(_c,gs);
this._menus[_d]=_f;
return _f;
}};
dojo.declare("dojo.widget.Editor2Plugin.ContextMenu",null,function(_10,gs){
this.groups=[];
this.separators=[];
this.editor=_10;
this.editor.registerLoadedPlugin(this);
this.contextMenu=dojo.widget.createWidget("PopupMenu2",{});
dojo.body().appendChild(this.contextMenu.domNode);
this.bindEditor(this.editor);
dojo.event.connect(this.contextMenu,"aboutToShow",this,"aboutToShow");
dojo.event.connect(this.editor,"destroy",this,"destroy");
this.setup(gs);
},{bindEditor:function(_12){
this.contextMenu.bindDomNode(_12.document.body);
},setup:function(gs){
for(var i in gs){
var g=dojo.widget.Editor2Plugin.ContextMenuManager.getGroup(gs[i],this);
if(g){
this.groups.push(g);
}
}
},aboutToShow:function(){
var _16=true;
for(var i in this.groups){
if(i>0&&this.separators.length!=this.groups.length-1){
this.separators.push(dojo.widget.createWidget("MenuSeparator2",{}));
this.contextMenu.addChild(this.separators[this.separators.length-1]);
}
if(this.groups[i].refresh()){
if(i>0){
if(_16){
this.separators[i-1].hide();
}else{
this.separators[i-1].show();
}
}
if(_16){
_16=false;
}
}else{
if(i>0){
this.separators[i-1].hide();
}
}
}
},destroy:function(){
this.editor.unregisterLoadedPlugin(this);
delete this.groups;
delete this.separators;
this.contextMenu.destroy();
delete this.contextMenu;
}});
dojo.widget.defineWidget("dojo.widget.Editor2ContextMenuItem",dojo.widget.MenuItem2,{command:"",buildRendering:function(){
var _18=dojo.widget.Editor2Manager.getCurrentInstance();
this.caption=_18.getCommand(this.command).getText();
dojo.widget.Editor2ContextMenuItem.superclass.buildRendering.apply(this,arguments);
},onClick:function(){
var _19=dojo.widget.Editor2Manager.getCurrentInstance();
if(_19){
var _1a=_19.getCommand(this.command);
if(_1a){
_1a.execute();
}
}
},refresh:function(){
var _1b=dojo.widget.Editor2Manager.getCurrentInstance();
if(_1b){
var _1c=_1b.getCommand(this.command);
if(_1c){
if(_1c.getState()==dojo.widget.Editor2Manager.commandState.Disabled){
this.disable();
return false;
}else{
this.enable();
return true;
}
}
}
},hide:function(){
this.domNode.style.display="none";
},show:function(){
this.domNode.style.display="";
}});
dojo.declare("dojo.widget.Editor2Plugin.SimpleContextMenuGroup",null,function(_1d){
this.contextMenu=_1d.contextMenu;
this.items=[];
dojo.event.connect(_1d,"destroy",this,"destroy");
},{refresh:function(){
if(!this.items.length){
this.createItems();
for(var i in this.items){
this.contextMenu.addChild(this.items[i]);
}
}
return this.checkVisibility();
},destroy:function(){
this.contextmenu=null;
delete this.items;
delete this.contextMenu;
},createItems:function(){
},checkVisibility:function(){
var _1f=false;
for(var i in this.items){
_1f=_1f||this.items[i].refresh();
}
var _21=_1f?"show":"hide";
for(var i in this.items){
this.items[i][_21]();
}
return _1f;
}});
dojo.declare("dojo.widget.Editor2Plugin.GenericContextMenuGroup",dojo.widget.Editor2Plugin.SimpleContextMenuGroup,{createItems:function(){
this.items.push(dojo.widget.createWidget("Editor2ContextMenuItem",{command:"cut",iconClass:"dojoE2TBIcon dojoE2TBIcon_Cut"}));
this.items.push(dojo.widget.createWidget("Editor2ContextMenuItem",{command:"copy",iconClass:"dojoE2TBIcon dojoE2TBIcon_Copy"}));
this.items.push(dojo.widget.createWidget("Editor2ContextMenuItem",{command:"paste",iconClass:"dojoE2TBIcon dojoE2TBIcon_Paste"}));
}});
dojo.declare("dojo.widget.Editor2Plugin.LinkContextMenuGroup",dojo.widget.Editor2Plugin.SimpleContextMenuGroup,{createItems:function(){
this.items.push(dojo.widget.createWidget("Editor2ContextMenuItem",{command:"createlink",iconClass:"dojoE2TBIcon dojoE2TBIcon_Link"}));
this.items.push(dojo.widget.createWidget("Editor2ContextMenuItem",{command:"unlink",iconClass:"dojoE2TBIcon dojoE2TBIcon_UnLink"}));
},checkVisibility:function(){
var _22=this.items[1].refresh();
if(_22){
this.items[0].refresh();
for(var i in this.items){
this.items[i].show();
}
}else{
for(var i in this.items){
this.items[i].hide();
}
}
return _22;
}});
dojo.declare("dojo.widget.Editor2Plugin.ImageContextMenuGroup",dojo.widget.Editor2Plugin.SimpleContextMenuGroup,{createItems:function(){
this.items.push(dojo.widget.createWidget("Editor2ContextMenuItem",{command:"insertimage",iconClass:"dojoE2TBIcon dojoE2TBIcon_Image"}));
},checkVisibility:function(){
var _24=dojo.widget.Editor2Manager.getCurrentInstance();
var img=dojo.withGlobal(_24.window,"getSelectedElement",dojo.html.selection);
if(img&&img.tagName.toLowerCase()=="img"){
this.items[0].show();
return true;
}else{
this.items[0].hide();
return false;
}
}});
