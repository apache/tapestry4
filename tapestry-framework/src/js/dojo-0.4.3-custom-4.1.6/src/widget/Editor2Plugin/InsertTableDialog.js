dojo.provide("dojo.widget.Editor2Plugin.InsertTableDialog");
dojo.widget.defineWidget("dojo.widget.Editor2InsertTableDialog",dojo.widget.Editor2DialogContent,{templateString:"<div>\n<table cellSpacing=\"1\" cellPadding=\"1\" width=\"100%\" border=\"0\">\n\t<tr>\n\t\t<td valign=\"top\">\n\t\t\t<table cellSpacing=\"0\" cellPadding=\"0\" border=\"0\">\n\t\t\t\t<tr>\n\n\t\t\t\t\t<td><span>Rows</span>:</td>\n\t\t\t\t\t<td>&nbsp;<input dojoAttachPoint=\"table_rows\" type=\"text\" maxLength=\"3\" size=\"2\" value=\"3\"></td>\n\t\t\t\t</tr>\n\t\t\t\t<tr>\n\t\t\t\t\t<td><span>Columns</span>:</td>\n\t\t\t\t\t<td>&nbsp;<input dojoAttachPoint=\"table_cols\" type=\"text\" maxLength=\"2\" size=\"2\" value=\"2\"></td>\n\t\t\t\t</tr>\n\n\t\t\t\t<tr>\n\t\t\t\t\t<td>&nbsp;</td>\n\t\t\t\t\t<td>&nbsp;</td>\n\t\t\t\t</tr>\n\t\t\t\t<tr>\n\t\t\t\t\t<td><span>Border size</span>:</td>\n\t\t\t\t\t<td>&nbsp;<INPUT dojoAttachPoint=\"table_border\" type=\"text\" maxLength=\"2\" size=\"2\" value=\"1\"></td>\n\t\t\t\t</tr>\n\n\t\t\t\t<tr>\n\t\t\t\t\t<td><span>Alignment</span>:</td>\n\t\t\t\t\t<td>&nbsp;<select dojoAttachPoint=\"table_align\">\n\t\t\t\t\t\t\t<option value=\"\" selected>&lt;Not set&gt;</option>\n\t\t\t\t\t\t\t<option value=\"left\">Left</option>\n\t\t\t\t\t\t\t<option value=\"center\">Center</option>\n\t\t\t\t\t\t\t<option value=\"right\">Right</option>\n\t\t\t\t\t\t</select></td>\n\t\t\t\t</tr>\n\t\t\t</table>\n\t\t</td>\n\t\t<td>&nbsp;&nbsp;&nbsp;</td>\n\t\t<td align=\"right\" valign=\"top\">\n\t\t\t<table cellSpacing=\"0\" cellPadding=\"0\" border=\"0\">\n\t\t\t\t<tr>\n\t\t\t\t\t<td><span>Width</span>:</td>\n\t\t\t\t\t<td>&nbsp;<input dojoAttachPoint=\"table_width\" type=\"text\" maxLength=\"4\" size=\"3\"></td>\n\t\t\t\t\t<td>&nbsp;<select dojoAttachPoint=\"table_widthtype\">\n\t\t\t\t\t\t\t<option value=\"percent\" selected>percent</option>\n\t\t\t\t\t\t\t<option value=\"pixels\">pixels</option>\n\t\t\t\t\t\t</select></td>\n\n\t\t\t\t</tr>\n\t\t\t\t<tr>\n\t\t\t\t\t<td><span>Height</span>:</td>\n\t\t\t\t\t<td>&nbsp;<INPUT dojoAttachPoint=\"table_height\" type=\"text\" maxLength=\"4\" size=\"3\"></td>\n\t\t\t\t\t<td>&nbsp;<span>pixels</span></td>\n\t\t\t\t</tr>\n\t\t\t\t<tr>\n\t\t\t\t\t<td>&nbsp;</td>\n\t\t\t\t\t<td>&nbsp;</td>\n\t\t\t\t\t<td>&nbsp;</td>\n\t\t\t\t</tr>\n\t\t\t\t<tr>\n\t\t\t\t\t<td nowrap><span>Cell spacing</span>:</td>\n\t\t\t\t\t<td>&nbsp;<input dojoAttachPoint=\"table_cellspacing\" type=\"text\" maxLength=\"2\" size=\"2\" value=\"1\"></td>\n\t\t\t\t\t<td>&nbsp;</td>\n\n\t\t\t\t</tr>\n\t\t\t\t<tr>\n\t\t\t\t\t<td nowrap><span>Cell padding</span>:</td>\n\t\t\t\t\t<td>&nbsp;<input dojoAttachPoint=\"table_cellpadding\" type=\"text\" maxLength=\"2\" size=\"2\" value=\"1\"></td>\n\t\t\t\t\t<td>&nbsp;</td>\n\t\t\t\t</tr>\n\t\t\t</table>\n\t\t</td>\n\t</tr>\n</table>\n<table cellSpacing=\"0\" cellPadding=\"0\" width=\"100%\" border=\"0\">\n\t<tr>\n\t\t<td nowrap><span>Caption</span>:</td>\n\t\t<td>&nbsp;</td>\n\t\t<td width=\"100%\" nowrap>&nbsp;\n\t\t\t<input dojoAttachPoint=\"table_caption\" type=\"text\" style=\"WIDTH: 90%\"></td>\n\t</tr>\n\t<tr>\n\t\t<td nowrap><span>Summary</span>:</td>\n\t\t<td>&nbsp;</td>\n\t\t<td width=\"100%\" nowrap>&nbsp;\n\t\t\t<input dojoAttachPoint=\"table_summary\" type=\"text\" style=\"WIDTH: 90%\"></td>\n\t</tr>\n</table>\n<table><tr>\n<td><button dojoType='Button' dojoAttachEvent='onClick:ok'>Ok</button></td>\n<td><button dojoType='Button' dojoAttachEvent='onClick:cancel'>Cancel</button></td>\n</tr></table>\n</div>\n",editableAttributes:["summary","height","cellspacing","cellpadding","border","align"],loadContent:function(){
var _1=dojo.widget.Editor2Manager.getCurrentInstance();
_1.saveSelection();
this.tableNode=dojo.withGlobal(_1.window,"getSelectedElement",dojo.html.selection);
if(!this.tableNode||this.tableNode.tagName.toLowerCase()!="table"){
this.tableNode=dojo.withGlobal(_1.window,"getAncestorElement",dojo.html.selection,["table"]);
}
var _2={};
this.extraAttribText="";
if(this.tableNode){
this["table_rows"].value=this.tableNode.rows.length;
this["table_rows"].disabled=true;
this["table_cols"].value=this.tableNode.rows[0].cells.length;
this["table_cols"].disabled=true;
if(this.tableNode.caption){
this["table_caption"].value=this.tableNode.caption.innerHTML;
}else{
this["table_caption"].value="";
}
var _3=this.tableNode.style.width||this.tableNode.width;
if(_3){
this["table_width"].value=parseInt(_3);
if(_3.indexOf("%")>-1){
this["table_widthtype"].value="percent";
}else{
this["table_widthtype"].value="pixels";
}
}else{
this["table_width"].value="100";
}
var _4=this.tableNode.style.height||this.tableNode.height;
if(_4){
this["table_height"].value=parseInt(_3);
}else{
this["table_height"].value="";
}
var _5=this.tableNode.attributes;
for(var i=0;i<_5.length;i++){
if(dojo.lang.find(this.editableAttributes,_5[i].name.toLowerCase())>-1){
_2[_5[i].name]=_5[i].value;
}else{
this.extraAttribText+=_5[i].name+"=\""+_5[i].value+"\" ";
}
}
}else{
this["table_rows"].value=3;
this["table_rows"].disabled=false;
this["table_cols"].value=2;
this["table_cols"].disabled=false;
this["table_width"].value=100;
this["table_widthtype"].value="percent";
this["table_height"].value="";
}
for(var i=0;i<this.editableAttributes.length;++i){
name=this.editableAttributes[i];
this["table_"+name].value=(_2[name]==undefined)?"":_2[name];
if(name=="height"&&_2[name]!=undefined){
this["table_"+name].value=_2[name];
}
}
return true;
},ok:function(){
var _7=dojo.widget.Editor2Manager.getCurrentInstance();
var _8={};
_8["rows"]=this["table_rows"].value;
_8["cols"]=this["table_cols"].value;
_8["caption"]=this["table_caption"].value;
_8["tableattrs"]="";
if(this["table_widthtype"].value=="percent"){
_8["tableattrs"]+="width=\""+this["table_width"].value+"%\" ";
}else{
_8["tableattrs"]+="width=\""+this["table_width"].value+"px\" ";
}
for(var i=0;i<this.editableAttributes.length;++i){
var _a=this.editableAttributes[i];
var _b=this["table_"+_a].value;
if(_b.length>0){
_8["tableattrs"]+=_a+"=\""+_b+"\" ";
}
}
if(!_8["tableattrs"]){
_8["tableattrs"]="";
}
if(dojo.render.html.ie&&!this["table_border"].value){
_8["tableattrs"]+="class=\"dojoShowIETableBorders\" ";
}
var _c="<table "+_8["tableattrs"]+">";
if(_8["caption"]){
_c+="<caption>"+_8["caption"]+"</caption>";
}
var _d="<tbody>";
if(this.tableNode){
var _e=this.tableNode.getElementsByTagName("tbody")[0];
_d=_e.outerHTML;
if(!_d){
var _f=_e.cloneNode(true);
var _10=_e.ownerDocument.createElement("div");
_10.appendChild(_f);
_d=_10.innerHTML;
}
dojo.withGlobal(_7.window,"selectElement",dojo.html.selection,[this.tableNode]);
}else{
var _11="<tr>";
for(var i=0;i<+_8.cols;i++){
_11+="<td></td>";
}
_11+="</tr>";
for(var i=0;i<_8.rows;i++){
_d+=_11;
}
_d+="</tbody>";
}
_c+=_d+"</table>";
_7.restoreSelection();
_7.execCommand("inserthtml",_c);
this.cancel();
}});
