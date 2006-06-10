dojo.provide("tapestry.form");

dojo.require("tapestry.core");

/**
 * Provides central handling of all client side form related logic.
 * 
 * Validation system to be replaced with {@link dojo.validate#check(form, profile)}.
 */
tapestry.form={
	
	/**
	 * Generically displays a window alert for the 
	 * given field when in error.
	 * 
	 * @param field The field element
	 * @param message The message to display
	 */
	invalidField:function(field, message){
		if (field.disabled) return;
		
    	this.focusField(field);
    	window.alert(message);
	},
	
	/**
	 * If possible, brings keyboard input focus
	 * to the specified field.
	 * 
	 * @param field The field(field id) of the field to focus.
	 */
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
	
	/**
	 * Trims whitespace from before/after field.
	 * @param id The field(field id) of the field to trim
	 * 			 whitespace input from.
	 */
	trimField:function(id){
		if (arguments.length < 1) return;
		
		var elm=dojo.byId(id);
		if (!elm) return;
		if ( elem.type != "text" && elem.type != "textarea" 
			&& elem.type != "password" ) { return; }
		
		elem.value = elem.value.replace(/(^\s*|\s*$)/g, "");
	},
	
	/**
	 * Checks if the field specified has a non-null value
	 * selected. This covers input fields/checkboxes/radio groups/etc..
	 * 
	 * @param field The field(field id) of the field to check for input.
	 * @param message The message to be displayed if no value has been input/selected
	 * 				  for the field.
	 */
	requireField:function(field, message){
		if (arguments.length < 1) return;
		
		var elem=dojo.byId(field);
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
	
	registerForm:function(id){
		var form=dojo.byId(id);
		if (!form) {
			dojo.raise("Form not found with id " + id);
			return;
		}
		
		dojo.log.warn("registerForm() not implemented yet.");
	},
	
	/**
	 * Submits the form specified, optionally setting the submitname
	 * hidden input field to the value of submitName to let the Form 
	 * component on server know which button caused the submission. (For
	 * the case of submit button listeners).
	 * 
	 * @param form The form(form id) to submit.
	 * @param submitName Optional submit name string to use when submitting.
	 */
	submit:function(form, submitName){
		var form=dojo.byId(form);
		if (!form) {
			dojo.raise("Form not found with id " + form);
			return;
		}
		
		if (submitName){
			form.submitname.value=submitName;
		}
		form.submit();
	},
	
	/**
	 * Does almost the same thing as {@link tapestry.form#submit(form, submitName)}, 
	 * but submits the request via XHR to the server asynchronously.
	 * 
	 * @param form The form(form id) to submit.
	 * @param content Optional content map, mainly used to pass in browser
	 * 				  event parameters to form submission, but can be any
	 * 				  typical form/value pair.
	 * @param submitName Optional submit name string to use when submitting.
	 */
	submitAsync:function(form, content, submitName){
		var form=dojo.byId(form);
		if (!form) {
			dojo.raise("Form not found with id " + id);
			return;
		}
		
		if (submitName){
			form.submitname.value=submitName;
		}
		dojo.io.bind({
			formNode:form,
			content:content,
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

dojo.require("tapestry.form_compat");
