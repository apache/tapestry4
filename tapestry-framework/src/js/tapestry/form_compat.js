dojo.provide("tapestry.form_compat");
dojo.provide("Tapestry");

// Backwards compatibility functions, to be removed in 4.1.1 js version.
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
					"use tapestry.form.invalidField instead", 
					"4.1.1");
	tapestry.form.invalidField(field, message);
}

Tapestry.find=function(id){
	dojo.deprecated("Tapestry.find",
					"use dojo.byId instead",
					"4.1.1");
	return dojo.byId(id);
}

Tapestry.register_form=function(formId){
	dojo.deprecated("Tapestry.register_form",
					"use tapestry.form.registerForm instead",
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
					"use tapestry.form.trimField instead",
					"4.1.1");
	tapestry.form.trimField(fieldId);
}

Tapestry.require_field = function(event, fieldId, message)
{
	dojo.deprecated("Tapestry.require_field",
					"use tapestry.form.requireField instead",
					"4.1.1");
	tapestry.form.requireField(fieldId, message);
}

Tapestry.submit_form = function(form_id, field_name)
{
	dojo.deprecated("Tapestry.submit_form",
					"use tapestry.form.submit instead",
					"4.1.1");
	tapestry.form.submit(form_id, field_name);
}
