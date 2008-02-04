dojo.provide("dojo.widget.Editor2Plugin.SimpleSignalCommands");
dojo.require("dojo.widget.Editor2");
dojo.declare("dojo.widget.Editor2Plugin.SimpleSignalCommand",dojo.widget.Editor2Command,function(_1,_2){
if(dojo.widget.Editor2.prototype[_2]==undefined){
dojo.widget.Editor2.prototype[_2]=function(){
};
}
},{execute:function(){
this._editor[this._name]();
}});
if(dojo.widget.Editor2Plugin["SimpleSignalCommands"]){
dojo.widget.Editor2Plugin["_SimpleSignalCommands"]=dojo.widget.Editor2Plugin["SimpleSignalCommands"];
}
dojo.widget.Editor2Plugin.SimpleSignalCommands={signals:["save","insertImage"],Handler:function(_3){
if(_3.toLowerCase()=="save"){
return new dojo.widget.Editor2ToolbarButton("Save");
}else{
if(_3.toLowerCase()=="insertimage"){
return new dojo.widget.Editor2ToolbarButton("InsertImage");
}
}
},getCommand:function(_4,_5){
var _6;
dojo.lang.every(this.signals,function(s){
if(s.toLowerCase()==_5.toLowerCase()){
_6=s;
return false;
}
return true;
});
if(_6){
return new dojo.widget.Editor2Plugin.SimpleSignalCommand(_4,_6);
}
}};
if(dojo.widget.Editor2Plugin["_SimpleSignalCommands"]){
dojo.lang.mixin(dojo.widget.Editor2Plugin.SimpleSignalCommands,dojo.widget.Editor2Plugin["_SimpleSignalCommands"]);
}
dojo.widget.Editor2Manager.registerHandler(dojo.widget.Editor2Plugin.SimpleSignalCommands,"getCommand");
dojo.widget.Editor2ToolbarItemManager.registerHandler(dojo.widget.Editor2Plugin.SimpleSignalCommands.Handler);
