
dojo.provide("dojo.widget.Editor2Plugin.ColorPicker")
dojo.require("dojo.widget.Editor2Plugin.DropDownList");dojo.require("dojo.widget.ColorPalette");dojo.declare("dojo.widget.Editor2ToolbarColorPaletteButton", dojo.widget.Editor2ToolbarDropDownButton, {onDropDownShown: function(){if(!this._colorpalette){this._colorpalette = dojo.widget.createWidget("ColorPalette", {});this._dropdown.addChild(this._colorpalette);this.disableSelection(this._dropdown.domNode);this.disableSelection(this._colorpalette.domNode);dojo.event.connect(this._colorpalette, "onColorSelect", this, 'setColor');dojo.event.connect(this._dropdown, "open", this, 'latchToolbarItem');dojo.event.connect(this._dropdown, "close", this, 'enableToolbarItem');}},enableToolbarItem: function(){dojo.widget.Editor2ToolbarButton.prototype.enableToolbarItem.call(this);},disableToolbarItem: function(){dojo.widget.Editor2ToolbarButton.prototype.disableToolbarItem.call(this);},setColor: function(color){this._dropdown.close();var curInst = dojo.widget.Editor2Manager.getCurrentInstance();if(curInst){var _command = curInst.getCommand(this._name);if(_command){_command.execute(color);}}
}});