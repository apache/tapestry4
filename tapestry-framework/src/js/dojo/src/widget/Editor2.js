


dojo.provide("dojo.widget.Editor2");

dojo.require("dojo.io.*");
dojo.require("dojo.html.*");
dojo.require("dojo.html.layout");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.RichText");
dojo.require("dojo.widget.Editor2Toolbar");


dojo.widget.Editor2Manager = {
_currentInstance: null,
_loadedCommands: {},


commandState: {Disabled: 0, Latched: 1, Enabled: 2},

getCurrentInstance: function(){
// summary: Return the current focused Editor2 instance
return this._currentInstance;
},
setCurrentInstance: function(inst){
// summary: Set current focused Editor2 instance
this._currentInstance = inst;
},
registerCommand: function(name, cmd){
// summary: Register an Editor2 command
// name: name of the command (case insensitive)
// cmd: an object which implements interface dojo.widget.Editor2Command
name = name.toLowerCase();
if(this._loadedCommands[name]){
delete this._loadedCommands[name];
}
this._loadedCommands[name] = cmd;
},
getCommand: function(name){
// summary: Return Editor2 command with the given name
// name: name of the command (case insensitive)
name = name.toLowerCase();
var oCommand = this._loadedCommands[name];
if(oCommand){
return oCommand;
}

switch(name){
case 'htmltoggle':
//Editor2 natively provide the htmltoggle functionalitity
//and it is treated as a builtin command
oCommand = new dojo.widget.Editor2BrowserCommand(name);
break;
case 'formatblock':
oCommand = new dojo.widget.Editor2FormatBlockCommand(name);
break;
case 'anchor':
oCommand = new dojo.widget.Editor2Command(name);
break;

//dialog command
case 'createlink':
oCommand = new dojo.widget.Editor2DialogCommand(name,
{contentFile: "dojo.widget.Editor2Plugin.CreateLinkDialog",
contentClass: "Editor2CreateLinkDialog",
title: "Insert/Edit Link", width: "300px", height: "200px"});
break;
case 'insertimage':
oCommand = new dojo.widget.Editor2DialogCommand(name,
{contentFile: "dojo.widget.Editor2Plugin.InsertImageDialog",
contentClass: "Editor2InsertImageDialog",
title: "Insert/Edit Image", width: "400px", height: "270px"});
break;
// By default we assume that it is a builtin simple command.
default:
var curtInst = this.getCurrentInstance();
if((curtInst && curtInst.queryCommandAvailable(name)) ||
(!curtInst && dojo.widget.Editor2.prototype.queryCommandAvailable(name))){
oCommand = new dojo.widget.Editor2BrowserCommand(name);
}else{
dojo.debug("dojo.widget.Editor2Manager.getCommand: Unknown command "+name);
return;
}
}
this._loadedCommands[name] = oCommand;
return oCommand;
},
destroy: function(){
// summary: Cleaning up. This is called automatically on page unload.
this._currentInstance = null;
for(var cmd in this._loadedCommands){
this._loadedCommands[cmd].destory();
}
}
};

dojo.addOnUnload(dojo.widget.Editor2Manager, "destroy");



dojo.lang.declare("dojo.widget.Editor2Command",null,{
initializer: function(name){
// summary: Constructor of this class
this._name = name;
},
//this function should be re-implemented in subclass
execute: function(para){
// summary: Execute the command. should be implemented in subclass
dojo.unimplemented("dojo.widget.Editor2Command.execute");
},
//default implemetation always returns Enabled
getState: function(){
// summary:
//		Return the state of the command. The default behavior is
//		to always return Enabled
return dojo.widget.Editor2Manager.commandState.Enabled;
},
destory: function(){
// summary: Destructor
}
}
);




dojo.lang.declare("dojo.widget.Editor2BrowserCommand", dojo.widget.Editor2Command, {
execute: function(para){
var curInst = dojo.widget.Editor2Manager.getCurrentInstance();
if(curInst){
curInst.execCommand(this._name, para);
}
},
getState: function(){
var curInst = dojo.widget.Editor2Manager.getCurrentInstance();
if(curInst){
try{
if(curInst.queryCommandEnabled(this._name)){
if(curInst.queryCommandState(this._name)){
return dojo.widget.Editor2Manager.commandState.Latched;
}else{
return dojo.widget.Editor2Manager.commandState.Enabled;
}
}else{
return dojo.widget.Editor2Manager.commandState.Disabled;
}
}catch (e) {
//dojo.debug("exception when getting state for command "+this._name+": "+e);
return dojo.widget.Editor2Manager.commandState.Enabled;
}
}
return dojo.widget.Editor2Manager.commandState.Disabled;
},
getValue: function(){
var curInst = dojo.widget.Editor2Manager.getCurrentInstance();
if(curInst){
try{
return curInst.queryCommandValue(this._name);
}catch(e){}
}
}
}
);

dojo.lang.declare("dojo.widget.Editor2FormatBlockCommand", dojo.widget.Editor2BrowserCommand, {

}
);

dojo.require("dojo.widget.FloatingPane");


dojo.widget.defineWidget(
"dojo.widget.Editor2Dialog",
[dojo.widget.HtmlWidget, dojo.widget.FloatingPaneBase, dojo.widget.ModalDialogBase],
{
templatePath: dojo.uri.dojoUri("src/widget/templates/Editor2/EditorDialog.html"),
// Boolean: Whether this is a modal dialog. True by default.
modal: true,


// String: Wwidth of the dialog. None by default
width: false,
// String: Height of the dialog. None by default
height: false,

// String: startup state of the dialog
windowState: "minimized",

displayCloseAction: true,

contentFile: "",
contentClass: "",

fillInTemplate: function(args, frag){
this.fillInFloatingPaneTemplate(args, frag);
dojo.widget.Editor2Dialog.superclass.fillInTemplate.call(this, args, frag);
},
postCreate: function(){
if(this.contentFile){
dojo.require(this.contentFile);
}
if(this.modal){
dojo.widget.ModalDialogBase.prototype.postCreate.call(this);
}else{
with(this.domNode.style) {
zIndex = 999;
display = "none";
}
}
dojo.widget.FloatingPaneBase.prototype.postCreate.apply(this, arguments);
dojo.widget.Editor2Dialog.superclass.postCreate.call(this);
if(this.width && this.height){
with(this.domNode.style){
width = this.width;
height = this.height;
}
}
},
createContent: function(){
if(!this.contentWidget && this.contentClass){
this.contentWidget = dojo.widget.createWidget(this.contentClass);
this.addChild(this.contentWidget);
}
},
show: function(){
if(!this.contentWidget){
//buggy IE: if the dialog is hidden, the button widgets
//in the dialog can not be shown, so show it temporary (as the
//dialog may decide not to show it in loadContent() later)
dojo.widget.Editor2Dialog.superclass.show.apply(this, arguments);
this.createContent();
dojo.widget.Editor2Dialog.superclass.hide.call(this);
}

if(!this.contentWidget || !this.contentWidget.loadContent()){
return;
}
this.showFloatingPane();
dojo.widget.Editor2Dialog.superclass.show.apply(this, arguments);
if(this.modal){
this.showModalDialog();
}
this.placeModalDialog();
if(this.modal){
//place the background div under this modal pane
this.shared.bg.style.zIndex = this.domNode.style.zIndex-1;
}
},
onShow: function(){
dojo.widget.Editor2Dialog.superclass.onShow.call(this);
this.onFloatingPaneShow();
},
closeWindow: function(){
this.hide();
dojo.widget.Editor2Dialog.superclass.closeWindow.apply(this, arguments);
},
hide: function(){
if(this.modal){
this.hideModalDialog();
}
dojo.widget.Editor2Dialog.superclass.hide.call(this);
}
}
);




dojo.widget.defineWidget(
"dojo.widget.Editor2DialogContent",
dojo.widget.HtmlWidget,
{
widgetsInTemplate: true,

loadContent:function(){
// summary: Load the content. Called by Editor2Dialog when first shown
return true;
},
cancel: function(){
// summary: Default handler when cancel button is clicked.
this.parent.hide();
}
});




dojo.lang.declare("dojo.widget.Editor2DialogCommand", dojo.widget.Editor2BrowserCommand,
function(name, dialogParas){
this.dialogParas = dialogParas;
},
{
execute: function(){
if(!this.dialog){
if(!this.dialogParas.contentFile || !this.dialogParas.contentClass){
alert("contentFile and contentClass should be set for dojo.widget.Editor2DialogCommand.dialogParas!");
return;
}
this.dialog = dojo.widget.createWidget("Editor2Dialog", this.dialogParas);

dojo.body().appendChild(this.dialog.domNode);

dojo.event.connect(this, "destroy", this.dialog, "destroy");
}
this.dialog.show();
}
});








dojo.widget.defineWidget(
"dojo.widget.Editor2",
dojo.widget.RichText,
{







// Boolean: Whether to share toolbar with other instances of Editor2
shareToolbar: false,
// Boolean: Whether the toolbar should scroll to keep it in the view
toolbarAlwaysVisible: false,



toolbarWidget: null,
scrollInterval: null,

// Object: dojo.uri.Uri object to specify the template file for the toolbar
toolbarTemplatePath: dojo.uri.dojoUri("src/widget/templates/EditorToolbarOneline.html"),
// Object: dojo.uri.Uri object to specify the css file for the toolbar
toolbarTemplateCssPath: null,



_inSourceMode: false,
_htmlEditNode: null,

editorOnLoad: function(){
// summary:
//		Create toolbar and other initialization routines. This is called after
//		the finish of the loading of document in the editing element


dojo.event.topic.publish("dojo.widget.Editor2::preLoadingToolbar", this);
if(this.toolbarAlwaysVisible){
dojo.require("dojo.widget.Editor2Plugin.AlwaysShowToolbar");
}

var toolbars = dojo.widget.byType("Editor2Toolbar");
if((!toolbars.length)||(!this.shareToolbar)){
if(this.toolbarWidget){
this.toolbarWidget.show();
//re-add the toolbar to the new domNode (caused by open() on another element)
dojo.html.insertBefore(this.toolbarWidget.domNode, this.domNode.firstChild);
}else{
var tbOpts = {};
tbOpts.templatePath = this.toolbarTemplatePath;
if(this.toolbarTemplateCssPath){
tbOpts.templateCssPath = this.toolbarTemplateCssPath;
}
this.toolbarWidget = dojo.widget.createWidget("Editor2Toolbar", tbOpts, this.domNode.firstChild, "before");

dojo.event.connect(this, "close", this.toolbarWidget, "hide");

this.toolbarLoaded();
}
}else{
// FIXME: 	selecting in one shared toolbar doesn't clobber
// 			selection in the others. This is problematic.
this.toolbarWidget = toolbars[0];
}

dojo.event.topic.registerPublisher("Editor2.clobberFocus", this, "clobberFocus");
dojo.event.topic.subscribe("Editor2.clobberFocus", this, "setBlur");

dojo.event.topic.publish("dojo.widget.Editor2::onLoad", this);

},

//event for plugins to use
toolbarLoaded: function(){
// summary:
//		Fired when the toolbar for this editor is created.
//		This event is for plugins to use
},

//TODO: provide a query mechanism about loaded plugins?
registerLoadedPlugin: function(obj){
// summary: Register a plugin which is loaded for this instance
if(!this.loadedPlugins){
this.loadedPlugins = [];
}
this.loadedPlugins.push(obj);
},
unregisterLoadedPlugin: function(obj){
// summery: Delete a loaded plugin for this instance
for(var i in this.loadedPlugins){
if(this.loadedPlugins[i] === obj){
delete this.loadedPlugins[i];
return;
}
}
dojo.debug("dojo.widget.Editor2.unregisterLoadedPlugin: unknow plugin object: "+obj);
},

//overload the original ones to provide extra commands
execCommand: function(command, argument){
switch(command.toLowerCase()){
case 'htmltoggle':
this.toggleHtmlEditing();
break;
default:
dojo.widget.Editor2.superclass.execCommand.apply(this, arguments);
}
},
queryCommandEnabled: function(command, argument){
switch(command.toLowerCase()){
case 'htmltoggle':
return true;
default:
if(this._inSourceMode){ return false;}
return dojo.widget.Editor2.superclass.queryCommandEnabled.apply(this, arguments);
}
},
queryCommandState: function(command, argument){
switch(command.toLowerCase()){
case 'htmltoggle':
return this._inSourceMode;
default:
return dojo.widget.Editor2.superclass.queryCommandState.apply(this, arguments);
}
},

onClick: function(e){
dojo.widget.Editor2.superclass.onClick.call(this, e);
//if Popup is used, call dojo.widget.PopupManager.onClick
//manually when click in the editing area to close all
//open popups (dropdowns)
if(dojo.widget.PopupManager){
if(!e){ //IE
e = this.window.event;
}
dojo.widget.PopupManager.onClick(e);
}
},

clobberFocus: function(){
// summary: stub to signal other instances to clobber focus
},
toggleHtmlEditing: function(){
// summary: toggle between WYSIWYG mode and HTML source mode
if(this===dojo.widget.Editor2Manager.getCurrentInstance()){
if(!this._inSourceMode){
this._inSourceMode = true;

if(!this._htmlEditNode){
this._htmlEditNode = dojo.doc().createElement("textarea");
dojo.html.insertAfter(this._htmlEditNode, this.editorObject);
}
this._htmlEditNode.style.display = "";
this._htmlEditNode.style.width = "100%";
this._htmlEditNode.style.height = dojo.html.getBorderBox(this.editNode).height+"px";
this._htmlEditNode.value = this.editNode.innerHTML;

//activeX object (IE) doesn't like to be hidden, so move it outside of screen instead
with(this.editorObject.style){
position = "absolute";
left = "-2000px";
top = "-2000px";
}
}else{
this._inSourceMode = false;

//In IE activeX mode, if _htmlEditNode is focused,
//when toggling, an error would occur, so unfocus it
this._htmlEditNode.blur();

with(this.editorObject.style){
position = "";
left = "";
top = "";
}

dojo.lang.setTimeout(this, "replaceEditorContent", 1, this._htmlEditNode.value);
this._htmlEditNode.style.display = "none";
this.focus();
}
this.updateToolbar(true);
}
},

setFocus: function(){
// summary: focus is set on this instance

if(dojo.widget.Editor2Manager.getCurrentInstance() === this){ return; }

this.clobberFocus();

dojo.widget.Editor2Manager.setCurrentInstance(this);
},

setBlur: function(){
// summary: focus on this instance is lost

//dojo.event.disconnect(this.toolbarWidget, "exec", this, "execCommand");
},

saveSelection: function(){
// summary: save the current selection for restoring it
this._bookmark = null;
this._bookmark = dojo.withGlobal(this.window, dojo.html.selection.getBookmark);
},
restoreSelection: function(){
// summary: restore the last saved selection
if(this._bookmark){
this.focus(); //require for none-activeX IE
dojo.withGlobal(this.window, "moveToBookmark", dojo.html.selection, [this._bookmark]);
this._bookmark = null;
}else{
dojo.debug("restoreSelection: no saved selection is found!");
}
},

_updateToolbarLastRan: null,
_updateToolbarTimer: null,
_updateToolbarFrequency: 500,

updateToolbar: function(force){
// summary: update the associated toolbar of this Editor2
if((!this.isLoaded)||(!this.toolbarWidget)){ return; }

// keeps the toolbar from updating too frequently
// TODO: generalize this functionality?
var diff = new Date() - this._updateToolbarLastRan;
if( (!force)&&(this._updateToolbarLastRan)&&
((diff < this._updateToolbarFrequency)) ){

clearTimeout(this._updateToolbarTimer);
var _this = this;
this._updateToolbarTimer = setTimeout(function() {
_this.updateToolbar();
}, this._updateToolbarFrequency/2);
return;

}else{
this._updateToolbarLastRan = new Date();
}
// end frequency checker

//IE has the habit of generating events even when this editor is blurred, prevent this
if(dojo.widget.Editor2Manager.getCurrentInstance() !== this){ return; }

this.toolbarWidget.update();
},

destroy: function(finalize){
this._htmlEditNode = null;
dojo.event.disconnect(this, "close", this.toolbarWidget, "hide");
if(!finalize){
this.toolbarWidget.destroy();
}
dojo.widget.Editor2.superclass.destroy.call(this);
},

onDisplayChanged: function(e){
dojo.widget.Editor2.superclass.onDisplayChanged.call(this,e);
this.updateToolbar();
},

onLoad: function(){
try{
dojo.widget.Editor2.superclass.onLoad.call(this);
}catch(e){ // FIXME: debug why this is throwing errors in IE!
dojo.debug(e);
}
this.editorOnLoad();
},

onFocus: function(){
dojo.widget.Editor2.superclass.onFocus.call(this);
this.setFocus();
},

//overload to support source editing mode
getEditorContent: function(){
if(this._inSourceMode){
this.replaceEditorContent(this._htmlEditNode.value);
}
return dojo.widget.Editor2.superclass.getEditorContent.call(this);
}

},
"html"
);