
dojo.provide("dojo.widget.Textbox");dojo.require("dojo.widget.*");dojo.require("dojo.widget.HtmlWidget");dojo.require("dojo.widget.Manager");dojo.require("dojo.widget.Parse");dojo.require("dojo.xml.Parse");dojo.require("dojo.lang.array");dojo.require("dojo.lang.common");dojo.require("dojo.string.extras");dojo.require("dojo.i18n.common");dojo.requireLocalization("dojo.widget", "validate", null, "zh-cn,de,ja,fr,it,ROOT");dojo.widget.defineWidget(
"dojo.widget.Textbox",dojo.widget.HtmlWidget,{className: "",name: "",value: "",type: "",trim: false,uppercase: false,lowercase: false,ucFirst: false,digit: false,htmlfloat: "none",templatePath: dojo.uri.moduleUri("dojo.widget", "templates/Textbox.html"),textbox: null,fillInTemplate: function() {this.textbox.value = this.value;this.filter();},filter: function() {if (this.trim) {this.textbox.value = this.textbox.value.replace(/(^\s*|\s*$)/g, "");}
if (this.uppercase) {this.textbox.value = this.textbox.value.toUpperCase();}
if (this.lowercase) {this.textbox.value = this.textbox.value.toLowerCase();}
if (this.ucFirst) {this.textbox.value = dojo.string.capitalize(this.textbox.value);}
if (this.digit) {this.textbox.value = this.textbox.value.replace(/\D/g, "");}},onfocus: function() {},onblur: function() { this.filter(); },mixInProperties: function(localProperties, frag) {dojo.widget.Textbox.superclass.mixInProperties.apply(this, arguments);if ( localProperties["class"] ) {this.className = localProperties["class"];}}
}
);