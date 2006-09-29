/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.validate.ValidationTextbox");

dojo.require("dojo.widget.validate.Textbox");
dojo.require("dojo.validate.common");

/*
  ****** ValidationTextbox ******

  A subclass of Textbox.
  Over-ride isValid in subclasses to perform specific kinds of validation.
  Has several new properties that can be specified as attributes in the markup.

  @attr type          		Basic input tag type declaration.
  @attr size          		Basic input tag size declaration.
  @attr type          		Basic input tag maxlength declaration.	
  @attr required          	Can be true or false, default is false.
  @attr validColor        	The color textbox is highlighted for valid input. Default is #cfc.
  @attr invalidColor      	The color textbox is highlighted for invalid input. Default is #fcc.
  @attr invalidClass		Class used to format displayed text in page if necessary to override default class
  @attr invalidMessage    	The message to display if value is invalid.
  @attr missingMessage    	The message to display if value is missing.
  @attr missingClass		Override default class used for missing input data
  @attr listenOnKeyPress	Updates messages on each key press.  Default is true.
  @attr promptMessage		Will not issue invalid message if field is populated with default user-prompt text
*/
dojo.widget.defineWidget(
	"dojo.widget.validate.ValidationTextbox",
	dojo.widget.validate.Textbox,
	function() {
		// this property isn't a primitive and needs to be created on a per-item basis.
		this.flags = {};
	},
	{
		// default values for new subclass properties
		required: false,
		rangeClass: "range",
		classPrefix: "dojoValidate",
		size: "",
		maxlength: "",
		promptMessage: "",
		invalidMessage: "",
		missingMessage: "",
		rangeMessage: "",
		listenOnKeyPress: true,
		htmlfloat: "none",
		lastCheckedValue: null,
	
		templatePath: dojo.uri.dojoUri("src/widget/templates/ValidationTextbox.html"),
	
		// new DOM nodes
		invalidSpan: null,
		missingSpan: null,
		rangeSpan: null,
	
		getValue: function() {
			return this.textbox.value;
		},
	
		setValue: function(value) {
			this.textbox.value = value;
			this.update();
		},
	
		// Need to over-ride with your own validation code in subclasses
		isValid: function() { return true; },
	
		// Need to over-ride with your own validation code in subclasses
		isInRange: function() { return true; },
	
		// Returns true if value is all whitespace
		isEmpty: function() { 
			return ( /^\s*$/.test(this.textbox.value) );
		},
	
		// Returns true if value is required and it is all whitespace.
		isMissing: function() { 
			return ( this.required && this.isEmpty() );
		},
	
		// Called oninit, onblur, and onkeypress.
		// Show missing or invalid messages if appropriate, and highlight textbox field.
		update: function() {
			this.lastCheckedValue = this.textbox.value;
			this.missingSpan.style.display = "none";
			this.invalidSpan.style.display = "none";
			this.rangeSpan.style.display = "none";
	
			var empty = this.isEmpty();
			var valid = true;
			if(this.promptMessage != this.textbox.value){ 
				valid = this.isValid(); 
			}
			var missing = this.isMissing();
	
			// Display at most one error message
			if(missing){
				this.missingSpan.style.display = "";
			}else if( !empty && !valid ){
				this.invalidSpan.style.display = "";
			}else if( !empty && !this.isInRange() ){
				this.rangeSpan.style.display = "";
			}
			this.highlight();
		},
	
		// Called oninit, and onblur.
		highlight: function() {
			// highlight textbox background 
			if ( this.isEmpty() ) {
				dojo.html.setClass(this.textbox,this.classPrefix+"Empty");
//				this.textbox.style.backgroundColor = "";
			}else if ( this.isValid() && this.isInRange() ){
				dojo.html.setClass(this.textbox,this.classPrefix+"Valid");
//				this.textbox.style.backgroundColor = this.validColor;
			}else if( this.textbox.value != this.promptMessage){ 
				dojo.html.setClass(this.textbox,this.classPrefix+"Invalid");
//				this.textbox.style.backgroundColor = this.invalidColor;
			}
		},
	
		onfocus: function() {
			if ( !this.listenOnKeyPress) {
				dojo.html.setClass(this.textbox,this.classPrefix+"Empty");
//			    this.textbox.style.backgroundColor = "";
			}
		},
	
		onblur: function() { 
			this.filter();
			this.update(); 
		},
	
		onkeyup: function(){ 
			if(this.listenOnKeyPress){ 
				//this.filter();  trim is problem if you have to type two words
				this.update(); 
			}else if (this.textbox.value != this.lastCheckedValue){
				dojo.html.setClass(this.textbox,this.classPrefix+"Empty");
//			    this.textbox.style.backgroundColor = "";
			}
		},

		postMixInProperties: function(localProperties, frag) {
			dojo.widget.validate.ValidationTextbox.superclass.postMixInProperties.apply(this, arguments);
			this.messages = dojo.i18n.getLocalization("dojo.widget", "validate", this.lang);
			dojo.lang.forEach(["invalidMessage", "missingMessage", "rangeMessage"], function(prop) {
				if(this[prop]){ this.messages[prop] = this[prop]; }
			}, this);
		},
	
		// FIXME: why are there to fillInTemplate methods defined here?
		fillInTemplate: function() {
			dojo.widget.validate.ValidationTextbox.superclass.fillInTemplate.apply(this, arguments);

			// Attach isMissing and isValid methods to the textbox.
			// We may use them later in connection with a submit button widget.
			// TODO: this is unorthodox; it seems better to do it another way -- Bill
			this.textbox.isValid = function() { this.isValid.call(this); };
			this.textbox.isMissing = function() { this.isMissing.call(this); };
			this.textbox.isInRange = function() { this.isInRange.call(this); };
			this.update(); 
			
			// apply any filters to initial value
			this.filter();

			// set table to be inlined (technique varies by browser)
			if(dojo.render.html.ie){ dojo.html.addClass(this.domNode, "ie"); }
			if(dojo.render.html.moz){ dojo.html.addClass(this.domNode, "moz"); }
			if(dojo.render.html.opera){ dojo.html.addClass(this.domNode, "opera"); }
			if(dojo.render.html.safari){ dojo.html.addClass(this.domNode, "safari"); }
		}
	}
);
