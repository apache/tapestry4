







dojo.provide("dojo.widget.Editor2Plugin.SimpleSignalCommands");

dojo.require("dojo.widget.Editor2");

dojo.declare("dojo.widget.Editor2Plugin.SimpleSignalCommand", dojo.widget.Editor2Command,
function(name){
if(dojo.widget.Editor2.prototype[name] == undefined){
dojo.widget.Editor2.prototype[name] = function(){ dojo.debug("Editor2::"+name); };
}
},
{
execute: function(){
var curInst = dojo.widget.Editor2Manager.getCurrentInstance();

if(curInst){
curInst[this._name]();
}
}
});

if(dojo.widget.Editor2Plugin['SimpleSignalCommands']){
dojo.widget.Editor2Plugin['_SimpleSignalCommands']=dojo.widget.Editor2Plugin['SimpleSignalCommands'];
}

dojo.widget.Editor2Plugin.SimpleSignalCommands = {
signals: ['save', 'insertImage'],
Handler: function(name){
if(name.toLowerCase() == 'save'){
return new dojo.widget.Editor2ToolbarButton('Save');
}else if(name.toLowerCase() == 'insertimage'){
return new dojo.widget.Editor2ToolbarButton('InsertImage');
}
},
registerAllSignalCommands: function(){
for(var i=0;i<this.signals.length;i++){
dojo.widget.Editor2Manager.registerCommand(this.signals[i],
new dojo.widget.Editor2Plugin.SimpleSignalCommand(this.signals[i]));
}
}
};

if(dojo.widget.Editor2Plugin['_SimpleSignalCommands']){
dojo.lang.mixin(dojo.widget.Editor2Plugin.SimpleSignalCommands, dojo.widget.Editor2Plugin['_SimpleSignalCommands']);
}

dojo.widget.Editor2Plugin.SimpleSignalCommands.registerAllSignalCommands();
dojo.widget.Editor2ToolbarItemManager.registerHandler(dojo.widget.Editor2Plugin.SimpleSignalCommands.Handler);