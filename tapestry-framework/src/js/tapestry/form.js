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
	
	invalid_field:function(field, message){
		tapestry.form.invalidField(field, message); 
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
	
	getFormId:function(form){
		return (form.getAttribute("id") ) ? form.getAttribute("id") : form.getAttribute("name");
	},
	
	/**
	 * Registers the form with the local <code>forms</code> property so 
	 * that there is a central reference of all tapestry forms.
	 * 
	 * @param id The form(form id) to register.
	 * @param async Boolean, if true causes form submission to be asynchronous.
	 * @param json Boolean, if true causes form submission to be asyncrhronous with an 
	 * 			   expected JSON response.
	 */
	registerForm:function(id, async, json){
		var form=dojo.byId(id);
		if (!form) {
			dojo.raise("Form not found with id " + id);
			return;
		}
		
		// make sure id is correct just in case node passed in has only name
		id=this.getFormId(form);
		
		if (!this.forms[id]) {
			this.forms[id]={};
			this.forms[id].validateForm=true;
			this.forms[id].profiles=[];
			this.forms[id].async=(typeof async != "undefined") ? async : false;
			this.forms[id].json=(typeof json != "undefined") ? json : false;
			
			if (!this.forms[id].async) {
				dojo.event.connect(form, "onsubmit", this, "onFormSubmit");
			} else {
				for(var i = 0; i < form.elements.length; i++) {
					var node = form.elements[i];
					if(node && node.type && dojo.lang.inArray(node.type.toLowerCase(), ["submit", "button"])) {
						dojo.event.connect(node, "onclick", tapestry.form, "inputClicked");
					}
				}
				
				var inputs = form.getElementsByTagName("input");
				for(var i = 0; i < inputs.length; i++) {
					var input = inputs[i];
					if(input.type.toLowerCase() == "image" && input.form == form) {
						dojo.event.connect(input, "onclick", tapestry.form, "inputClicked");
					}
				}
				
				dojo.event.connect(form, "onsubmit", function(e) {
					if (dojo.event.browser.isEvent(e)){
						dojo.event.browser.stopEvent(e);
					}
					
					tapestry.form.submitAsync(form);
				});
			}
		} else {
			dojo.log.warn("registerForm(" + id + ") Form already registered.");
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
	
	inputClicked:function(e){
		var node = e.currentTarget;
		if(node.disabled || dj_undef("form", node)) { return; }
		this.forms[this.getFormId(node.form)].clickedButton = node;
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
		if (!evt || dj_undef("target", evt)) {
			dojo.raise("No valid form event found with argument: " + evt);
			return;
		}
		
		var id=this.getFormId(evt.target);
		if (!id) {
			dojo.log.warn("Form had no id attribute.");
			return;
		}
		
		var form = dojo.byId(id);
		if (!dj_undef("value", form.submitmode)
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
		
		// listeners trigerred then for some reason.
		// try to create a real event so any listeners get notified
		if (!dj_undef("createEvent", document)){
			var ev = document.createEvent("HTMLEvents");
			ev.initEvent("submit", true, true);
			form.dispatchEvent(ev);
		} else if (!dj_undef("createEventObject", document)) {
			form.fireEvent("onsubmit");
		} else {
			dojo.log.warn("tapestry.form.submit using default form.submit() call, no event objects found in browser.");
			form.submit();
			return;
		}
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
	 */
	submitAsync:function(form, content, submitName){
		var form=dojo.byId(form);
		if (!form) {
			dojo.raise("Form not found with id " + id);
			return;
		}
		var formId=this.getFormId(form);
		
		if (!tapestry.form.validation.validateForm(form, this.forms[formId])) {
			dojo.log.debug("Form validation failed for form with id " + formId);
			return;
		}
		
		if (submitName){
			form.submitname.value=submitName;
		}
		
		// handle submissions from input buttons
		if (!dj_undef("clickedButton", this.forms[formId])) {
			if (!content) { content={}; }
			content[this.forms[formId].clickedButton.getAttribute("name")]=this.forms[formId].clickedButton.getAttribute("value");
		}
		
		var parms={
			formNode:form,
			content:content,
            useCache:true,
            preventCache:true,
            error: (function(){tapestry.error.apply(this, arguments);}),
            encoding: "UTF-8"
		};
		
		if (this.forms[formId].json) {
			parms.headers={"json":true};
			parms.mimetype="text/json";
		} else {
			parms.headers={"dojo-ajax-request":true};
			parms.mimetype="text/xml";
			parms.load=(function(){tapestry.load.apply(this, arguments);});
		}
		
		dojo.io.bind(parms);
	}
}

dojo.require("tapestry.form.validation");
dojo.require("tapestry.form_compat");
