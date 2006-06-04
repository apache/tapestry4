dojo.provide("tapestry.form");
dojo.provide("Tapestry");

dojo.require("tapestry.core");

tapestry.form={
	
	invalidField:function(field, message){
		if (field.disabled) return;
		
    	this.focusField(field);
    	window.alert(message);
	},
	
	focusField:function(field){
		if (arguments.length < 1) return;
		
		field = dojo.byId(field);
		if (!field) return;
		if (field.disabled || field.clientWidth < 1) return;
		
        if (typeof field.focus != "undefined") { field.focus(); }
        
        if (field.isContentEditable 
        	|| field.isContentEditable == null
        	&& typeof field.select != "undefined") {
        	field.select();
        }
	},
	
	trimField:function(id){
		if (arguments.length < 1) return;
		
		var elm=dojo.byId(id);
		if (!elm) return;
		if ( elem.type != "text" && elem.type != "textarea" 
			&& elem.type != "password" ) { return; }
		
		elem.value = elem.value.replace(/(^\s*|\s*$)/g, "");
	},
	
	requireField:function(fieldId, message){
		if (arguments.length < 1) return;
		
		var elem=dojo.byId(fieldId);
		if (!elem) return;
		
		// Are textbox, textarea, or password fields blank.
		if ( (elem.type == "text" || elem.type == "textarea" || elem.type == "password") 
			&& /^\s*$/.test(elem.value) ) {
			this.invalidField(elem, message);
			return;
		}
		// Does drop-down box have option selected.
		else if ( (elem.type == "select-one" || elem.type == "select-multiple") 
				&& elem.selectedIndex == -1 ) {
			this.invalidField(elem, message);
		}
		// Does radio button group (or check box group) have option checked.
		else if ( elem instanceof Array )  {
			var checked = false;
			for (var j = 0; j < elem.length; j++) {
				if (elem[j].checked) { checked = true; }
			}
			if ( !checked ) {	
				this.invalidField(elem, message);
				return;
			}
		}
	},
	
	submit:function(id, submitName){
		var form=dojo.byId(id);
		if (!form) {
			dojo.raise("Form not found with id " + id);
			return;
		}
		
		form.submitname.value=submitName;
		form.submit();
	},
	
	submitAsync:function(id, submitName){
		var form=dojo.byId(id);
		if (!form) {
			dojo.raise("Form not found with id " + id);
			return;
		}
		
		dojo.io.bind({
			formNode:form,
            headers:{"dojo-ajax-request":true},
            useCache:true,
            preventCache:true,
            load: (function(){tapestry.load.apply(this, arguments);}),
            error: (function(){tapestry.error.apply(this, arguments);}),
            mimetype: "text/xml",
            encoding: "UTF-8"
        });
	}
}

// Backwards compatibility functions, to be removed in 4.1.1 js version

// global used to deprecate old event connection methods
tapestry.form.deprecateConnect=function(){
	dojo.deprecated("Tapestry.on<event>",
					"use dojo.event.connect instead",
					"4.1.1");
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
}

Tapestry.onpresubmit=tapestry.form.deprecateConnect;
Tapestry.onsubmit=tapestry.form.deprecateConnect;
Tapestry.onpostsubmit=tapestry.form.deprecateConnect;
Tapestry.onreset=tapestry.form.deprecateConnect;
Tapestry.onrefresh=tapestry.form.deprecateConnect;
Tapestry.oncancel=tapestry.form.deprecateConnect;

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