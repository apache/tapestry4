dojo.provide("dojo.widget.Editor2Plugin.FindReplace");
dojo.require("dojo.widget.Editor2");
dojo.declare("dojo.widget.Editor2Plugin.FindCommand",dojo.widget.Editor2DialogCommand,{SearchOption:{CaseSensitive:4,SearchBackwards:64,WholeWord:2,WrapSearch:128},find:function(_1,_2){
this._editor.focus();
if(window.find){
this._editor.window.find(_1,_2&this.SearchOption.CaseSensitive?true:false,_2&this.SearchOption.SearchBackwards?true:false,_2&this.SearchOption.WrapSearch?true:false,_2&this.SearchOption.WholeWord?true:false);
}else{
if(dojo.body().createTextRange){
var _3=this._editor.document.body.createTextRange();
var _4=_3.findText(_1,(_2&this.SearchOption.SearchBackwards)?1:-1,_2);
if(_4){
_3.scrollIntoView();
_3.select();
}else{
alert("Can not find "+_1+" in the document");
}
}else{
alert("No idea how to search in this browser. Please submit patch if you know.");
}
}
},getText:function(){
return "Find";
}});
dojo.widget.Editor2Plugin.FindReplace={getCommand:function(_5,_6){
var _6=_6.toLowerCase();
var _7;
if(_6=="find"){
_7=new dojo.widget.Editor2Plugin.FindCommand(_5,"find",{contentFile:"dojo.widget.Editor2Plugin.FindReplaceDialog",contentClass:"Editor2FindDialog",title:"Find",width:"350px",height:"150px",modal:false});
}else{
if(_6=="replace"){
_7=new dojo.widget.Editor2DialogCommand(_5,"replace",{contentFile:"dojo.widget.Editor2Plugin.FindReplaceDialog",contentClass:"Editor2ReplaceDialog",href:dojo.uri.cache.set(dojo.uri.moduleUri("dojo.widget","templates/Editor2/Dialog/replace.html"),"<table style=\"white-space: nowrap;\">\n<tr><td>Find: </td><td> <input type=\"text\" dojoAttachPoint=\"replace_text\" /></td></tr>\n<tr><td>Replace with: </td><td> <input type=\"text\" dojoAttachPoint=\"replace_text\" /></td></tr>\n<tr><td colspan='2'><table><tr><td><input type=\"checkbox\" dojoType=\"CheckBox\" dojoAttachPoint=\"replace_option_casesens\" id=\"dojo_replace_option_casesens\" />\n\t\t<label for=\"dojo_replace_option_casesens\">Case Sensitive</label></td>\n\t\t\t<td><input type=\"checkbox\" dojoType=\"CheckBox\" dojoAttachPoint=\"replace_option_backwards\" id=\"dojo_replace_option_backwards\" />\n\t\t<label for=\"dojo_replace_option_backwards\">Search Backwards</label></td></tr></table></td></tr>\n<tr><td colspan=2\">\n\t<table><tr>\n\t<td><button dojoType='Button' dojoAttachEvent='onClick:replace'>Replace</button></td>\n\t<td><button dojoType='Button' dojoAttachEvent='onClick:replaceAll'>Replace All</button></td>\n\t<td><button dojoType='Button' dojoAttachEvent='onClick:cancel'>Close</button></td>\n\t</tr></table>\n\t</td></tr>\n</table>\n"),title:"Replace",width:"350px",height:"200px",modal:false});
}
}
return _7;
},getToolbarItem:function(_8){
var _8=_8.toLowerCase();
var _9;
if(_8=="replace"){
_9=new dojo.widget.Editor2ToolbarButton("Replace");
}else{
if(_8=="find"){
_9=new dojo.widget.Editor2ToolbarButton("Find");
}
}
return _9;
}};
dojo.widget.Editor2Manager.registerHandler(dojo.widget.Editor2Plugin.FindReplace.getCommand);
dojo.widget.Editor2ToolbarItemManager.registerHandler(dojo.widget.Editor2Plugin.FindReplace.getToolbarItem);
