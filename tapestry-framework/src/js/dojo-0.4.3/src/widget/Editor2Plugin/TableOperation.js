dojo.provide("dojo.widget.Editor2Plugin.TableOperation");
dojo.require("dojo.widget.Editor2");
dojo.event.topic.subscribe("dojo.widget.RichText::init",function(_1){
if(dojo.render.html.ie){
_1.contentDomPreFilters.push(dojo.widget.Editor2Plugin.TableOperation.showIETableBorder);
_1.contentDomPostFilters.push(dojo.widget.Editor2Plugin.TableOperation.removeIEFakeClass);
}
_1.getCommand("toggletableborder");
});
dojo.lang.declare("dojo.widget.Editor2Plugin.deleteTableCommand",dojo.widget.Editor2Command,{execute:function(){
var _2=dojo.withGlobal(this._editor.window,"getAncestorElement",dojo.html.selection,["table"]);
if(_2){
dojo.withGlobal(this._editor.window,"selectElement",dojo.html.selection,[_2]);
this._editor.execCommand("inserthtml"," ");
}
},getState:function(){
if(this._editor._lastStateTimestamp>this._updateTime||this._state==undefined){
this._updateTime=this._editor._lastStateTimestamp;
var _3=dojo.withGlobal(this._editor.window,"hasAncestorElement",dojo.html.selection,["table"]);
this._state=_3?dojo.widget.Editor2Manager.commandState.Enabled:dojo.widget.Editor2Manager.commandState.Disabled;
}
return this._state;
},getText:function(){
return "Delete Table";
}});
dojo.lang.declare("dojo.widget.Editor2Plugin.toggleTableBorderCommand",dojo.widget.Editor2Command,function(){
this._showTableBorder=false;
dojo.event.connect(this._editor,"editorOnLoad",this,"execute");
},{execute:function(){
if(this._showTableBorder){
this._showTableBorder=false;
if(dojo.render.html.moz){
this._editor.removeStyleSheet(dojo.uri.moduleUri("dojo.widget","templates/Editor2/showtableborder_gecko.css"));
}else{
if(dojo.render.html.ie){
this._editor.removeStyleSheet(dojo.uri.moduleUri("dojo.widget","templates/Editor2/showtableborder_ie.css"));
}
}
}else{
this._showTableBorder=true;
if(dojo.render.html.moz){
this._editor.addStyleSheet(dojo.uri.moduleUri("dojo.widget","templates/Editor2/showtableborder_gecko.css"));
}else{
if(dojo.render.html.ie){
this._editor.addStyleSheet(dojo.uri.moduleUri("dojo.widget","templates/Editor2/showtableborder_ie.css"));
}
}
}
},getText:function(){
return "Toggle Table Border";
},getState:function(){
return this._showTableBorder?dojo.widget.Editor2Manager.commandState.Latched:dojo.widget.Editor2Manager.commandState.Enabled;
}});
dojo.widget.Editor2Plugin.TableOperation={getCommand:function(_4,_5){
switch(_5.toLowerCase()){
case "toggletableborder":
return new dojo.widget.Editor2Plugin.toggleTableBorderCommand(_4,_5);
case "inserttable":
return new dojo.widget.Editor2DialogCommand(_4,"inserttable",{contentFile:"dojo.widget.Editor2Plugin.InsertTableDialog",contentClass:"Editor2InsertTableDialog",title:"Insert/Edit Table",width:"450px",height:"250px"});
case "deletetable":
return new dojo.widget.Editor2Plugin.deleteTableCommand(_4,_5);
}
},getToolbarItem:function(_6){
var _6=_6.toLowerCase();
var _7;
switch(_6){
case "inserttable":
case "toggletableborder":
_7=new dojo.widget.Editor2ToolbarButton(_6);
}
return _7;
},getContextMenuGroup:function(_8,_9){
return new dojo.widget.Editor2Plugin.TableContextMenuGroup(_9);
},showIETableBorder:function(_a){
var _b=_a.getElementsByTagName("table");
dojo.lang.forEach(_b,function(t){
dojo.html.addClass(t,"dojoShowIETableBorders");
});
return _a;
},removeIEFakeClass:function(_d){
var _e=_d.getElementsByTagName("table");
dojo.lang.forEach(_e,function(t){
dojo.html.removeClass(t,"dojoShowIETableBorders");
});
return _d;
}};
dojo.widget.Editor2Manager.registerHandler(dojo.widget.Editor2Plugin.TableOperation.getCommand);
dojo.widget.Editor2ToolbarItemManager.registerHandler(dojo.widget.Editor2Plugin.TableOperation.getToolbarItem);
if(dojo.widget.Editor2Plugin.ContextMenuManager){
dojo.widget.Editor2Plugin.ContextMenuManager.registerGroup("Table",dojo.widget.Editor2Plugin.TableOperation.getContextMenuGroup);
dojo.declare("dojo.widget.Editor2Plugin.TableContextMenuGroup",dojo.widget.Editor2Plugin.SimpleContextMenuGroup,{createItems:function(){
this.items.push(dojo.widget.createWidget("Editor2ContextMenuItem",{caption:"Delete Table",command:"deletetable"}));
this.items.push(dojo.widget.createWidget("Editor2ContextMenuItem",{caption:"Table Property",command:"inserttable",iconClass:"TB_Button_Icon TB_Button_Table"}));
},checkVisibility:function(){
var _10=dojo.widget.Editor2Manager.getCurrentInstance();
var _11=dojo.withGlobal(_10.window,"hasAncestorElement",dojo.html.selection,["table"]);
if(dojo.withGlobal(_10.window,"hasAncestorElement",dojo.html.selection,["table"])){
this.items[0].show();
this.items[1].show();
return true;
}else{
this.items[0].hide();
this.items[1].hide();
return false;
}
}});
}
