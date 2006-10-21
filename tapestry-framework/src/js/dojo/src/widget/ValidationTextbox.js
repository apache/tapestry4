

dojo.provide("dojo.widget.ValidationTextbox");

dojo.require("dojo.widget.Textbox");
dojo.require("dojo.i18n.common");


dojo.widget.defineWidget(
"dojo.widget.ValidationTextbox",
dojo.widget.Textbox,
function() {
// this property isn't a primitive and needs to be created on a per-item basis.
this.flags = {};
},
{
// default values for new subclass properties
required: false,
rangeClass: "range",
invalidClass: "invalid",
missingClass: "missing",
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
templateCssPath: dojo.uri.dojoUri("src/widget/templates/Validate.css"),

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

updateClass: function(className){
//summary: used to ensure that only 1 validation class is set at a time
var pre = this.classPrefix;
dojo.html.removeClass(this.textbox,pre+"Empty");
dojo.html.removeClass(this.textbox,pre+"Valid");
dojo.html.removeClass(this.textbox,pre+"Invalid");
dojo.html.addClass(this.textbox,pre+className);
},

// Called oninit, and onblur.
highlight: function() {
// highlight textbox background
if (this.isEmpty()) {
this.updateClass("Empty");
}else if (this.isValid() && this.isInRange() ){
this.updateClass("Valid");
}else if(this.textbox.value != this.promptMessage){
this.updateClass("Invalid");
}else{
this.updateClass("Empty");
}
},

onfocus: function(evt) {
if ( !this.listenOnKeyPress) {
this.updateClass("Empty");

}
},

onblur: function(evt) {
this.filter();
this.update();
},

onkeyup: function(evt){
if(this.listenOnKeyPress){
//this.filter();  trim is problem if you have to type two words
this.update();
}else if (this.textbox.value != this.lastCheckedValue){
this.updateClass("Empty");

}
},

postMixInProperties: function(localProperties, frag) {
dojo.widget.ValidationTextbox.superclass.postMixInProperties.apply(this, arguments);
this.messages = dojo.i18n.getLocalization("dojo.widget", "validate", this.lang);
dojo.lang.forEach(["invalidMessage", "missingMessage", "rangeMessage"], function(prop) {
if(this[prop]){ this.messages[prop] = this[prop]; }
}, this);
},

// FIXME: why are there to fillInTemplate methods defined here?
fillInTemplate: function() {
dojo.widget.ValidationTextbox.superclass.fillInTemplate.apply(this, arguments);

// Attach isMissing and isValid methods to the textbox.
// We may use them later in connection with a submit button widget.
// TODO: this is unorthodox; it seems better to do it another way -- Bill
this.textbox.isValid = function() { this.isValid.call(this); };
this.textbox.isMissing = function() { this.isMissing.call(this); };
this.textbox.isInRange = function() { this.isInRange.call(this); };
dojo.html.setClass(this.invalidSpan,this.invalidClass);
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
