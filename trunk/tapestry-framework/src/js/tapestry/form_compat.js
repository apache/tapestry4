dojo.provide("tapestry.form_compat");
dojo.provide("Tapestry");

// package: tapestry.form_compat
// Backwards compatibility functions, to be removed in 4.1.1 js version. Contains all of 
// the functions found in the old Form.js provided by previous Tapestry releases.
// Should only be included by tapestry.form, don't dojo.require() this module.

// global used to deprecate old event connection methods
tapestry.form.deprecateConnect=function(formName, fnc, event, advice){
	dojo.deprecated("Tapestry.on"+event,
					"use dojo.event.connect instead",
					"4.1.1");
	
	if (advice) {
		dojo.event.connect(advice, dojo.byId(formName), event, fnc);
	} else {
		dojo.event.connect(dojo.byId(formName), event, fnc);
	}
}

// BEGIN old function definitions

Tapestry.default_invalid_field_handler=function(event, field, message){
	dojo.deprecated("Tapestry.default_invalid_field_handler", 
					"use tapestry.form.validation.validateForm instead", 
					"4.1.1");
	if (field.disabled) return;
    
    if (typeof window != "undefined"){
    	window.alert(message);
    } else {
    	dojo.debug("Invalid field : " + message);
    }
    tapestry.form.focusField(field);
}

Tapestry.invalid_field=function(field, message){
	Tapestry.default_invalid_field_handler(null, field, message);
}

Tapestry.find=function(id){
	dojo.deprecated("Tapestry.find",
					"use dojo.byId instead",
					"4.1.1");
	return dojo.byId(id);
}

Tapestry.register_form=function(formId){
	dojo.deprecated("Tapestry.register_form",
					"use tapestry.form.registerForm instead register_form(" + formId + ")",
					"4.1.1");
	tapestry.form.registerForm(formId);
}

Tapestry.onpresubmit=function(formName, fnc){ tapestry.form.deprecateConnect(formName, fnc, "onsubmit", "before"); };
Tapestry.onsubmit=function(formName, fnc){ tapestry.form.deprecateConnect(formName, fnc, "onsubmit"); };
Tapestry.onpostsubmit=function(formName, fnc){ tapestry.form.deprecateConnect(formName, fnc, "onsubmit", "after"); };
Tapestry.onreset=function(formName, fnc){ tapestry.form.deprecateConnect(formName, fnc, "onreset"); };
Tapestry.onrefresh=function(formName, fnc){ tapestry.form.deprecateConnect(formName, fnc, "onrefresh"); };
Tapestry.oncancel=function(formName, fnc){ tapestry.form.deprecateConnect(formName, fnc, "oncancel"); };

Tapestry.set_focus=function (field){
	dojo.deprecated("Tapestry.set_focus",
					"use tapestry.form.focusField instead",
					"4.1.1");
	tapestry.form.focusField(field);
}

Tapestry.trim_field_value = function(fieldId)
{
	dojo.deprecated("Tapestry.trim_field_value",
					"use dojo.html instead",
					"4.1.1");
	
	if (arguments.length < 1) return;
	
	var elm=dojo.byId(id);
	if (!elm) {return;}
	if ( elm.type != "text" && elm.type != "textarea"
		&& elm.type != "password" ) { return; }
		
	elm.value = elm.value.replace(/(^\s*|\s*$)/g, "");
}

Tapestry.require_field = function(event, field, message)
{
	dojo.deprecated("Tapestry.require_field",
					"use tapestry.form.validation.validateForm instead",
					"4.1.1");
	if (arguments.length < 1) return;
	
	var elem=dojo.byId(field);
	if (!elem) { return; }
	
	// Are textbox, textarea, or password fields blank.
	if ( (elem.type == "text" || elem.type == "textarea" || elem.type == "password") 
		&& /^\s*$/.test(elem.value) ) {
		Tapestry.default_invalid_field_handler(elem, message);
		return;
	}
	// Does drop-down box have option selected.
	else if ( (elem.type == "select-one" || elem.type == "select-multiple") 
			&& elem.selectedIndex == -1 ) {
		Tapestry.default_invalid_field_handler(elem, message);
		return;
	} else if ( elem instanceof Array )  {
		// Does radio button group (or check box group) have option checked.
		var checked = false;
		for (var j = 0; j < elem.length; j++) {
			if (elem[j].checked) { checked = true; }
		}
		if ( !checked ) {	
			Tapestry.default_invalid_field_handler(elem, message);
			return;
		}
	}
}

Tapestry.submit_form = function(form_id, field_name)
{
	dojo.deprecated("Tapestry.submit_form",
					"use tapestry.form.submit instead (" + form_id + ", " + field_name + ")",
					"4.1.1");
	tapestry.form.submit(form_id, field_name);
}
