dojo.provide("dojo.widget.Editor2Plugin.CreateLinkDialog");
dojo.widget.defineWidget("dojo.widget.Editor2CreateLinkDialog",dojo.widget.Editor2DialogContent,{templateString:"<table>\n<tr><td>URL</td><td> <input type=\"text\" dojoAttachPoint=\"link_href\" name=\"dojo_createLink_href\"/></td></tr>\n<tr><td>Target </td><td><select dojoAttachPoint=\"link_target\">\n\t<option value=\"\">Self</option>\n\t<option value=\"_blank\">New Window</option>\n\t<option value=\"_top\">Top Window</option>\n\t</select></td></tr>\n<tr><td>Class </td><td><input type=\"text\" dojoAttachPoint=\"link_class\" /></td></tr>\n<tr><td colspan=\"2\">\n\t<table><tr>\n\t<td><button dojoType='Button' dojoAttachEvent='onClick:ok'>OK</button></td>\n\t<td><button dojoType='Button' dojoAttachEvent='onClick:cancel'>Cancel</button></td>\n\t</tr></table>\n\t</td></tr>\n</table>\n",editableAttributes:["href","target","class"],loadContent:function(){
var _1=dojo.widget.Editor2Manager.getCurrentInstance();
_1.saveSelection();
this.linkNode=dojo.withGlobal(_1.window,"getAncestorElement",dojo.html.selection,["a"]);
var _2={};
this.extraAttribText="";
if(this.linkNode){
var _3=this.linkNode.attributes;
for(var i=0;i<_3.length;i++){
if(dojo.lang.find(this.editableAttributes,_3[i].name.toLowerCase())>-1){
_2[_3[i].name]=_3[i].value;
}else{
if(_3[i].specified==undefined||_3[i].specified){
this.extraAttribText+=_3[i].name+"=\""+_3[i].value+"\" ";
}
}
}
}else{
var _5=dojo.withGlobal(_1.window,"getSelectedText",dojo.html.selection);
if(_5==null||_5.length==0){
alert("Please select some text to create a link.");
return false;
}
}
for(var i=0;i<this.editableAttributes.length;++i){
name=this.editableAttributes[i];
this["link_"+name].value=(_2[name]==undefined)?"":_2[name];
}
return true;
},ok:function(){
var _6=dojo.widget.Editor2Manager.getCurrentInstance();
_6.restoreSelection();
if(!this.linkNode){
var _7=dojo.withGlobal(_6.window,"getSelectedHtml",dojo.html.selection);
}else{
var _7=this.linkNode.innerHTML;
dojo.withGlobal(_6.window,"selectElement",dojo.html.selection,[this.linkNode]);
}
var _8="";
for(var i=0;i<this.editableAttributes.length;++i){
name=this.editableAttributes[i];
var _a=this["link_"+name].value;
if(_a.length>0){
_8+=name+"=\""+_a+"\" ";
}
}
_6.execCommand("inserthtml","<a "+_8+this.extraAttribText+">"+_7+"</a>");
this.cancel();
}});
