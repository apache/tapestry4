
dojo.provide("dojo.widget.Editor2Toolbar");dojo.require("dojo.lang.*");dojo.require("dojo.widget.*");dojo.require("dojo.event.*");dojo.require("dojo.html.layout");dojo.require("dojo.html.display");dojo.require("dojo.widget.RichText");dojo.lang.declare("dojo.widget.HandlerManager", null,function(){this._registeredHandlers=[];},{registerHandler: function(obj, func){if(arguments.length == 2){this._registeredHandlers.push(function(){return obj[func].apply(obj, arguments);});}else{this._registeredHandlers.push(obj);}},removeHandler: function(func){for(var i=0;i<this._registeredHandlers.length;i++){if(func === this._registeredHandlers[i]){delete this._registeredHandlers[i];return;}}
dojo.debug("HandlerManager handler "+func+" is not registered, can not remove.");},destroy: function(){for(var i=0;i<this._registeredHandlers.length;i++){delete this._registeredHandlers[i];}}});dojo.widget.Editor2ToolbarItemManager = new dojo.widget.HandlerManager;dojo.lang.mixin(dojo.widget.Editor2ToolbarItemManager,{getToolbarItem: function(name){var item;name = name.toLowerCase();for(var i=0;i<this._registeredHandlers.length;i++){item = this._registeredHandlers[i](name);if(item){return item;}}
_deprecated = function(cmd, plugin){if(!dojo.widget.Editor2Plugin[plugin]){dojo.deprecated('Toolbar item '+name+" is now defined in plugin dojo.widget.Editor2Plugin."+plugin+". It shall be required explicitly", "0.6");dojo['require']("dojo.widget.Editor2Plugin."+plugin);}}
if(name == 'forecolor' || name == 'hilitecolor'){_deprecated(name, 'ColorPicker')}else if(name == 'formatblock' || name == 'fontsize' || name == 'fontname'){_deprecated(name, 'DropDownList')}
switch(name){case 'bold':
case 'copy':
case 'cut':
case 'delete':
case 'indent':
case 'inserthorizontalrule':
case 'insertorderedlist':
case 'insertunorderedlist':
case 'italic':
case 'justifycenter':
case 'justifyfull':
case 'justifyleft':
case 'justifyright':
case 'outdent':
case 'paste':
case 'redo':
case 'removeformat':
case 'selectall':
case 'strikethrough':
case 'subscript':
case 'superscript':
case 'underline':
case 'undo':
case 'unlink':
case 'createlink':
case 'insertimage':
case 'htmltoggle':
item = new dojo.widget.Editor2ToolbarButton(name);break;case 'forecolor':
case 'hilitecolor':
item = new dojo.widget.Editor2ToolbarColorPaletteButton(name);break;case 'plainformatblock':
item = new dojo.widget.Editor2ToolbarFormatBlockPlainSelect("formatblock");break;case 'formatblock':
item = new dojo.widget.Editor2ToolbarFormatBlockSelect("formatblock");break;case 'fontsize':
item = new dojo.widget.Editor2ToolbarFontSizeSelect("fontsize");break;case 'fontname':
item = new dojo.widget.Editor2ToolbarFontNameSelect("fontname");break;case 'inserthtml':
case 'blockdirltr':
case 'blockdirrtl':
case 'dirltr':
case 'dirrtl':
case 'inlinedirltr':
case 'inlinedirrtl':
dojo.debug("Not yet implemented toolbar item: "+name);break;default:
dojo.debug("dojo.widget.Editor2ToolbarItemManager.getToolbarItem: Unknown toolbar item: "+name);}
return item;}});dojo.addOnUnload(dojo.widget.Editor2ToolbarItemManager, "destroy");dojo.declare("dojo.widget.Editor2ToolbarButton", null,function(name){this._name = name;},{create: function(node, toolbar, nohover){this._domNode = node;var cmd = toolbar.parent.getCommand(this._name);if(cmd){this._domNode.title = cmd.getText();}
this.disableSelection(this._domNode);this._parentToolbar = toolbar;dojo.event.connect(this._domNode, 'onclick', this, 'onClick');if(!nohover){dojo.event.connect(this._domNode, 'onmouseover', this, 'onMouseOver');dojo.event.connect(this._domNode, 'onmouseout', this, 'onMouseOut');}},disableSelection: function(rootnode){dojo.html.disableSelection(rootnode);var nodes = rootnode.all || rootnode.getElementsByTagName("*");for(var x=0; x<nodes.length; x++){dojo.html.disableSelection(nodes[x]);}},onMouseOver: function(){var curInst = dojo.widget.Editor2Manager.getCurrentInstance();if(curInst){var _command = curInst.getCommand(this._name);if(_command && _command.getState() != dojo.widget.Editor2Manager.commandState.Disabled){this.highlightToolbarItem();}}},onMouseOut: function(){this.unhighlightToolbarItem();},destroy: function(){this._domNode = null;this._parentToolbar = null;},onClick: function(e){if(this._domNode && !this._domNode.disabled && this._parentToolbar.checkAvailability()){e.preventDefault();e.stopPropagation();var curInst = dojo.widget.Editor2Manager.getCurrentInstance();if(curInst){var _command = curInst.getCommand(this._name);if(_command){_command.execute();}}}},refreshState: function(){var curInst = dojo.widget.Editor2Manager.getCurrentInstance();var em = dojo.widget.Editor2Manager;if(curInst){var _command = curInst.getCommand(this._name);if(_command){var state = _command.getState();if(state != this._lastState){switch(state){case em.commandState.Latched:
this.latchToolbarItem();break;case em.commandState.Enabled:
this.enableToolbarItem();break;case em.commandState.Disabled:
default:
this.disableToolbarItem();}
this._lastState = state;}}}
return em.commandState.Enabled;},latchToolbarItem: function(){this._domNode.disabled = false;this.removeToolbarItemStyle(this._domNode);dojo.html.addClass(this._domNode, this._parentToolbar.ToolbarLatchedItemStyle);},enableToolbarItem: function(){this._domNode.disabled = false;this.removeToolbarItemStyle(this._domNode);dojo.html.addClass(this._domNode, this._parentToolbar.ToolbarEnabledItemStyle);},disableToolbarItem: function(){this._domNode.disabled = true;this.removeToolbarItemStyle(this._domNode);dojo.html.addClass(this._domNode, this._parentToolbar.ToolbarDisabledItemStyle);},highlightToolbarItem: function(){dojo.html.addClass(this._domNode, this._parentToolbar.ToolbarHighlightedItemStyle);},unhighlightToolbarItem: function(){dojo.html.removeClass(this._domNode, this._parentToolbar.ToolbarHighlightedItemStyle);},removeToolbarItemStyle: function(){dojo.html.removeClass(this._domNode, this._parentToolbar.ToolbarEnabledItemStyle);dojo.html.removeClass(this._domNode, this._parentToolbar.ToolbarLatchedItemStyle);dojo.html.removeClass(this._domNode, this._parentToolbar.ToolbarDisabledItemStyle);this.unhighlightToolbarItem();}});dojo.declare("dojo.widget.Editor2ToolbarFormatBlockPlainSelect", dojo.widget.Editor2ToolbarButton, {create: function(node, toolbar){this._domNode = node;this._parentToolbar = toolbar;this._domNode = node;this.disableSelection(this._domNode);dojo.event.connect(this._domNode, 'onchange', this, 'onChange');},destroy: function(){this._domNode = null;},onChange: function(){if(this._parentToolbar.checkAvailability()){var sv = this._domNode.value.toLowerCase();var curInst = dojo.widget.Editor2Manager.getCurrentInstance();if(curInst){var _command = curInst.getCommand(this._name);if(_command){_command.execute(sv);}}}},refreshState: function(){if(this._domNode){dojo.widget.Editor2ToolbarFormatBlockPlainSelect.superclass.refreshState.call(this);var curInst = dojo.widget.Editor2Manager.getCurrentInstance();if(curInst){var _command = curInst.getCommand(this._name);if(_command){var format = _command.getValue();if(!format){ format = ""; }
dojo.lang.forEach(this._domNode.options, function(item){if(item.value.toLowerCase() == format.toLowerCase()){item.selected = true;}});}}}}});dojo.widget.defineWidget(
"dojo.widget.Editor2Toolbar",dojo.widget.HtmlWidget,function(){dojo.event.connect(this, "fillInTemplate", dojo.lang.hitch(this, function(){if(dojo.render.html.ie){this.domNode.style.zoom = 1.0;}}));},{templatePath: dojo.uri.dojoUri("src/widget/templates/EditorToolbar.html"),templateCssPath: dojo.uri.dojoUri("src/widget/templates/EditorToolbar.css"),ToolbarLatchedItemStyle: "ToolbarButtonLatched",ToolbarEnabledItemStyle: "ToolbarButtonEnabled",ToolbarDisabledItemStyle: "ToolbarButtonDisabled",ToolbarHighlightedItemStyle: "ToolbarButtonHighlighted",ToolbarHighlightedSelectStyle: "ToolbarSelectHighlighted",ToolbarHighlightedSelectItemStyle: "ToolbarSelectHighlightedItem",postCreate: function(){var nodes = dojo.html.getElementsByClass("dojoEditorToolbarItem", this.domNode);this.items = {};for(var x=0; x<nodes.length; x++){var node = nodes[x];var itemname = node.getAttribute("dojoETItemName");if(itemname){var item = dojo.widget.Editor2ToolbarItemManager.getToolbarItem(itemname);if(item){item.create(node, this);this.items[itemname.toLowerCase()] = item;}else{node.style.display = "none";}}}},update: function(){for(var cmd in this.items){this.items[cmd].refreshState();}},shareGroup: '',checkAvailability: function(){if(!this.shareGroup){this.parent.focus();return true;}
var curInst = dojo.widget.Editor2Manager.getCurrentInstance();if(this.shareGroup == curInst.toolbarGroup){return true;}
return false;},destroy: function(){for(var it in this.items){this.items[it].destroy();delete this.items[it];}
dojo.widget.Editor2Toolbar.superclass.destroy.call(this);}}
);