dojo.hostenv.name_="wsh";
if(typeof WScript=="undefined"){
dojo.raise("attempt to use WSH host environment when no WScript global");
}
dojo.hostenv.println=WScript.Echo;
dojo.hostenv.getCurrentScriptUri=function(){
return WScript.ScriptFullName();
};
dojo.hostenv.getText=function(_1){
var _2=new ActiveXObject("Scripting.FileSystemObject");
var _3=_2.OpenTextFile(_1,1);
if(!_3){
return null;
}
var _4=_3.ReadAll();
_3.Close();
return _4;
};
dojo.hostenv.exit=function(_5){
WScript.Quit(_5);
};
dojo.requireIf((djConfig["isDebug"]||djConfig["debugAtAllCosts"]),"dojo.debug");
