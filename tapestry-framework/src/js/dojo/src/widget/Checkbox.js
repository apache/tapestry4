/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.Checkbox");

dojo.require("dojo.widget.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.event.*");
dojo.require("dojo.html.style");

dojo.widget.defineWidget(
	"dojo.widget.Checkbox",
	dojo.widget.HtmlWidget,
	{
		templatePath: dojo.uri.dojoUri('src/widget/templates/Checkbox.html'),
		templateCssPath: dojo.uri.dojoUri('src/widget/templates/Checkbox.css'),

		// attributes
		disabled: "enabled",
		name: "",
		checked: "",
		tabIndex: "",
		id: "",
		value: "on",

		postMixInProperties: function(){
			dojo.widget.Checkbox.superclass.postMixInProperties.apply(this, arguments);
			// set the variables referenced by the template
			// valid HTML 4.01 and XHTML use disabled="disabled" - convert to boolean 
			//NOTE: this doesn't catch disabled with no value if FF
			this.disabled = (this.disabled == "disabled" || this.disabled == true);
			// valid HTML 4.01 and XHTML require checked="checked"
			// convert to boolean NOTE: this doesn't catch checked with no value in FF
			this.checked = (this.checked == "checked" || this.checked == true);

			// output valid checked and disabled attributes
			this.disabledStr = this.disabled ? "disabled=\"disabled\"" : "";
			this.checkedStr = this.checked ? "checked=\"checked\"" : "";

			// set tabIndex="0" because if tabIndex=="" user won't be able to tab to the field


			if(!this.disabled && this.tabIndex==""){ this.tabIndex="0"; }
		},

		postCreate: function(args, frag){
			// find any associated label and create a labelled-by relationship
			// assumes <label for="inputId">label text </label> rather than
			// <label><input type="xyzzy">label text</label> 
			if(this.id != ""){
				var labels = document.getElementsByTagName("label");
				if (labels != null && labels.length > 0){
					for(var i=0; i<labels.length; i++){
						if (labels[i].htmlFor == this.id){
							labels[i].id = (labels[i].htmlFor + "label"); 
							dojo.widget.wai.setAttr(this.domNode, "waiState", "labelledby", labels[i].id);
							break;
						}
					}
				}
			}
		},

		fillInTemplate: function(){
			this._setInfo();
		},

		_onClick: function(e){
			if(this.disabled == false){
				this.checked = !this.checked;
				this._setInfo();
			}
			e.preventDefault();
			this.onClick();
		},

		// user overridable function
		onClick: function(){ },

		onKey: function(e){
			var k = dojo.event.browser.keys;
			if(e.key == " "){
	 			this._onClick(e);
	 		}
		},
		
		mouseOver: function(e){
			this.hover(e, true);
		},
		
		mouseOut: function(e){
			this.hover(e, false);
		},
		
		hover: function(e, isOver){
			if (this.disabled == false){
				var state = this.checked ? "On" : "Off";
				var style = "dojoHtmlCheckbox" + state + "Hover";
				if (isOver){
					dojo.html.addClass(this.domNode, style);
				}else{
					dojo.html.removeClass(this.domNode,style);
				}
			}
		},

		// set CSS class string according to checked/unchecked and disabled/enabled state
		_setInfo: function(){
			var state = "dojoHtmlCheckbox" + (this.disabled ? "Disabled" : "") + (this.checked ? "On" : "Off");
			dojo.html.setClass(this.domNode, "dojoHtmlCheckbox " + state);
			this.inputNode.checked = this.checked;
			dojo.widget.wai.setAttr(this.domNode, "waiState", "checked", this.checked);
		}
	}
);
dojo.widget.defineWidget(
	"dojo.widget.a11y.Checkbox",
	dojo.widget.Checkbox,
	{	
		templatePath: dojo.uri.dojoUri('src/widget/templates/CheckboxA11y.html'),
		
		postCreate: function(args, frag){
			// nothing to do but don't want Checkbox version to run
		},
		
		fillInTemplate: function(){
		},
		_onClick: function(){
			this.onClick();
		}
	}
);

