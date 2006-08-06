dojo.provide("tapestry.form");

dojo.require("dojo.event");
dojo.require("dojo.event.browser");

dojo.require("tapestry.core");

/**
 * Provides central handling of all client side form related logic.
 * 
 * Validation system to be replaced with {@link dojo.validate#check(form, profile)}.
 */
tapestry.form={
	
	forms:{}, // registered form references
	
	/**
	 * Generically displays a window alert for the 
	 * given field when in error.
	 * 
	 * @param field The field element
	 * @param message The message to display
	 */
	invalidField:function(field, message){
		if (field.disabled) return;
		
    	window.alert(message);
    	this.focusField(field);
	},
	
	invalid_field:function(field, message){tapestry.form.invalidField(field, message); },
	
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
        
        dojo.html.selectInputText(field);
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
		if ( elm.type != "text" && elm.type != "textarea"
			&& elm.type != "password" ) { return; }
		
		elm.value = elm.value.replace(/(^\s*|\s*$)/g, "");
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
	
	/**
	 * Registers the form with the local <code>forms</code> property so 
	 * that there is a central reference of all tapestry forms.
	 * @param id The form(form id) to register.
	 */
	registerForm:function(id){
		var form=dojo.byId(id);
		if (!form) {
			dojo.raise("Form not found with id " + id);
			return;
		}
		
		// make sure id is correct just in case node passed in
		id=(form.getAttribute("id") ) ? form.getAttribute("id") : form.getAttribute("name");
		
		if (!this.forms[id]) {
			this.forms[id]={};
			this.forms[id].validateForm=true;
			this.forms[id].profiles=[];
			
			dojo.event.connect(form, "onsubmit", this, "onFormSubmit");
		} else {
			dojo.log.warn("egisterForm(" + id + ") Form already registered.");
		}
	},
	
	/**
	 * Registers a form validation/translation profile. There
	 * can potentially be more than one profile registered with
	 * a form.
	 * 
	 * The profiles will be consulted at various points in the forms
	 * life, which currently only involves running the profile checks
	 * before form submission. (more points to be determined in the future)
	 * 
	 * @see {@link dojo.validate.check(form, profile)}.
	 * 
	 * @param id The form(form id) to register profile with.
	 * @param profile The object containing all of the validation/value
	 * 				  constraints for the form. 
	 */
	registerProfile:function(id, profile){
		if (!this.forms[id]) {
			dojo.raise("registerProfile(" + id + ") No form previously registered with that id.");
			return;
		}
		
		this.forms[id].profiles.push(eval(profile));
	},

	/**
	 * Clears any previously registered validation profiles 
	 * on the specified form. Normally called during XHR requests
	 * by returned JS response to ensure new validation logic coming
	 * in from potentially new form fields is accounted for.
	 * 
	 * @param id The form id to clear profiles for.
	 */
	clearProfiles:function(id){
		if (!this.forms[id]) return;
		
		for (var i=0; i < this.forms[id].profiles.length; i++) {
			delete this.forms[id].profiles[i];
		}
		this.forms[id].profiles=[];
	},

	/**
	 * If a form registered with the specified formId
	 * exists a local property will be set that causes
	 * validation to be turned on/off depending on the argument.
	 * 
	 * @param formId The id of the form to turn validation on/off for.
	 * @param validate Boolean for whether or not to validate form, if
	 * 				   not specified assumes true.
	 */
	setFormValidating:function(formId, validate){
		if (this.forms[formId]){
			this.forms[formId].validateForm = validate ? true : false;
		}
	},
	
	/**
	 * Event connected function that is invoked when a form
	 * is submitted.
	 */
	onFormSubmit:function(evt){
		if (!evt || !evt.target) {
			dojo.raise("No target for form event." + evt);
			return;
		}
		
		var id=evt.target.getAttribute("id");
		if (!id) return;
		
		var form = dojo.byId(id);
		if (form.submitmode.value 
			&& (form.submitmode.value == "cancel" || form.submitmode.value == "refresh")) {
			return;
		}
		
		if (!tapestry.form.validation.validateForm(evt.target, this.forms[id])) {
			dojo.event.browser.stopEvent(evt);
		}
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
	
	cancel:function(form, submitName){
		var form=dojo.byId(form);
		if (!form){
			dojo.raise("Form not found with id " + form);
			return;
		}
		
		form.submitmode.value="cancel";
		
		this.submit(form, submitName);
	},
	
	refresh:function(form, submitName){
		var form=dojo.byId(form);
		if (!form){
			dojo.raise("Form not found with id " + form);
			return;
		}
		
		form.submitmode.value="refresh";
		
		this.submit(form, submitName);
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
	 * @param validate Whether or not to validate form, default is false.
	 */
	submitAsync:function(form, content, submitName, validate){
		var form=dojo.byId(form);
		if (!form) {
			dojo.raise("Form not found with id " + id);
			return;
		}
		var formId=form.getAttribute("id");
		
		if (validate && !tapestry.form.validation.validateForm(form, this.forms[formId])) {
			dojo.log.debug("Form validation failed for form with id " + formId);
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

dojo.require("tapestry.form.validation");
dojo.require("tapestry.form_compat");
