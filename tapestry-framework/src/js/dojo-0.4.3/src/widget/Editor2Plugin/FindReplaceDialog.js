dojo.provide("dojo.widget.Editor2Plugin.FindReplaceDialog");
dojo.widget.defineWidget("dojo.widget.Editor2FindDialog",dojo.widget.Editor2DialogContent,{templateString:"<table style=\"white-space: nowrap;\">\n<tr><td colspan='2'>Find: <input type=\"text\" dojoAttachPoint=\"find_text\" /></td></tr>\n<tr><td><input type=\"checkbox\" dojoType=\"CheckBox\" dojoAttachPoint=\"find_option_casesens\" />\n\t\t<label for=\"find_option_casesens\">Case Sensitive</label></td>\n\t\t\t<td><input type=\"checkbox\" dojoType=\"CheckBox\" dojoAttachPoint=\"find_option_backwards\" />\n\t\t<label for=\"find_option_backwards\">Search Backwards</label></td></tr>\n<tr><td style=\"display: none;\"><input type=\"checkbox\" dojoType=\"CheckBox\" dojoAttachPoint=\"find_option_wholeword\" />\n\t\t<label for=\"find_option_wholeword\">Whole Word</label></td>\n<tr><td colspan=\"1\">\n\t<table><tr>\n\t<td><button dojoType='Button' dojoAttachEvent='onClick:find'>Find</button></td>\n\t<td><button dojoType='Button' dojoAttachEvent='onClick:cancel'>Close</button></td>\n\t</tr></table>\n\t</td></tr>\n</table>\n",find:function(){
var _1=dojo.widget.Editor2Manager.getCurrentInstance();
var _2=_1.getCommand("find");
var _3=0;
if(this["find_option_casesens"].checked){
_3|=_2.SearchOption.CaseSensitive;
}
if(this["find_option_backwards"].checked){
_3|=_2.SearchOption.SearchBackwards;
}
if(this["find_option_wholeword"].checked){
_3|=_2.SearchOption.WholeWord;
}
_2.find(this["find_text"].value,_3);
}});
dojo.widget.defineWidget("dojo.widget.Editor2ReplaceDialog",dojo.widget.Editor2DialogContent,{templateString:"<table style=\"white-space: nowrap;\">\n<tr><td>Find: </td><td> <input type=\"text\" dojoAttachPoint=\"replace_text\" /></td></tr>\n<tr><td>Replace with: </td><td> <input type=\"text\" dojoAttachPoint=\"replace_text\" /></td></tr>\n<tr><td colspan='2'><table><tr><td><input type=\"checkbox\" dojoType=\"CheckBox\" dojoAttachPoint=\"replace_option_casesens\" id=\"dojo_replace_option_casesens\" />\n\t\t<label for=\"dojo_replace_option_casesens\">Case Sensitive</label></td>\n\t\t\t<td><input type=\"checkbox\" dojoType=\"CheckBox\" dojoAttachPoint=\"replace_option_backwards\" id=\"dojo_replace_option_backwards\" />\n\t\t<label for=\"dojo_replace_option_backwards\">Search Backwards</label></td></tr></table></td></tr>\n<tr><td colspan=2\">\n\t<table><tr>\n\t<td><button dojoType='Button' dojoAttachEvent='onClick:replace'>Replace</button></td>\n\t<td><button dojoType='Button' dojoAttachEvent='onClick:replaceAll'>Replace All</button></td>\n\t<td><button dojoType='Button' dojoAttachEvent='onClick:cancel'>Close</button></td>\n\t</tr></table>\n\t</td></tr>\n</table>\n",replace:function(){
alert("not implemented yet");
},replaceAll:function(){
alert("not implemented yet");
}});
