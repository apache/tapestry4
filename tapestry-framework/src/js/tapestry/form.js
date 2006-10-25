dojo.provide("tapestry.form");

dojo.require("dojo.event.browser");
dojo.require("dojo.dom");
dojo.require("dojo.html.selection");

dojo.require("tapestry.core");

/**
 * package: tapestry.form
 * Provides central handling of all client side form related logic.
 */
tapestry.form={
	
	// property: forms
	// Contains a reference to all registered Tapestry forms in the current document.
	forms:{}, // registered form references
	// property: currentFocus 
	// Reference to form element/element id of field that should currently recieve focus, if any
	currentFocus:null,
	
	/**
	 * Function: focusField
	 * If possible, brings keyboard input focus
	 * to the specified field.
	 * 
	 * Parameters:
	 * 	field - The field(field id) of the field to focus.
	 * 
	 * Note:
	 * 	Function deprecated in favor of dojo equivalent, like 
	 *  dojo.html.selectInputText(node).
	 */
	focusField:function(field){
		if (arguments.length < 1) {return;}
		
		var f=dojo.widget.byId(field);
		if(f && !dj_undef("focus", f)){
			f.focus();
			return;
		} else {
			f = dojo.byId(field);
		}
		
		if (!f) { return; }
		
		if(!dj_undef("focus", f)){
			f.focus();
			return;
		}
		
		if (field.disabled || field.clientWidth < 1) {
			return;
		}
		
        dojo.html.selectInputText(field);
	},
	
	/**
	 * Used by AlertDialog to focus the highest priority form field 
	 * that failed validation. This happens because the field needs to 
	 * be focused ~after~ the dialog is hidden.
	 */
	_focusCurrentField:function(){
		if(!this.currentFocus){return;}
		
		this.focusField(this.currentFocus);
	},
	
	/**
	 * Function: registerForm
	 * 
	 * Registers the form with the local <forms> property so 
	 * that there is a central reference of all tapestry forms.
	 * 
	 * Parameters:
	 * 
	 *	id		-	The form(form id) to register.
	 *  async	-	Boolean, if true causes form submission to be asynchronous.
	 *  json	-	Boolean, if true causes form submission to be asyncrhronous with an 
	 * 				expected JSON response.
	 */
	registerForm:function(id, async, json){
		var form=dojo.byId(id);
		if (!form) {
			dojo.raise("Form not found with id " + id);
			return;
		}
		
		// make sure id is correct just in case node passed in has only name
		id=form.getAttribute("id");
		
		// if previously connected, cleanup and reconnect
		if (this.forms[id]) {
			dojo.event.disconnect(form, "onsubmit", this, "onFormSubmit");
			for(var i = 0; i < form.elements.length; i++) {
				var node = form.elements[i];
				if(node && node.type && dojo.lang.inArray(["submit", "button"],node.type.toLowerCase())) {
					dojo.event.disconnect(node, "onclick", tapestry.form, "inputClicked");
				}
			}
			
			var inputs = form.getElementsByTagName("input");
			for(var i = 0; i < inputs.length; i++) {
				var input = inputs[i];
				if(input.type.toLowerCase() == "image" && input.form == form) {
					dojo.event.disconnect(input, "onclick", tapestry.form, "inputClicked");
				}
			}
			
			dojo.event.disconnect(form, "onsubmit", this, "overrideSubmit");
			delete this.forms[id];
		}
		
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
				if(node && node.type && dojo.lang.inArray(["submit", "button"],node.type.toLowerCase())) {
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
			
			dojo.event.connect(form, "onsubmit", this, "overrideSubmit");
		}
	},
	
	overrideSubmit:function(e){
		dojo.event.browser.stopEvent(e);
		tapestry.form.submitAsync(e.target);
	},
	
	/**
	 * Function: registerProfile
	 * 
	 * Registers a form validation/translation profile. There
	 * can potentially be more than one profile registered with
	 * a form.
	 * 
	 * The profiles will be consulted at various points in the forms
	 * life, which currently only involves running the profile checks
	 * before form submission. (more points to be determined in the future)
	 * 
	 * See Also:
	 * 	<dojo.validate.check>
	 * 
	 * Parameters:
	 * 
	 *	id		-	The form(form id) to register profile with.
	 *	profile	-	The object containing all of the validation/value constraints for the form. 
	 */
	registerProfile:function(id, profile){
		if (!this.forms[id]) {
			dojo.raise("registerProfile(" + id + ") No form previously registered with that id.");
			return;
		}
		
		this.forms[id].profiles.push(eval(profile));
	},

	/**
	 * Function: clearProfiles
	 * 
	 * Clears any previously registered validation profiles 
	 * on the specified form. Normally called during XHR requests
	 * by returned JS response to ensure new validation logic coming
	 * in from potentially new form fields is accounted for.
	 * 
	 * Parameters:
	 * 
	 *	id - The form id to clear profiles for.
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
		this.forms[node.form.getAttribute("id")].clickedButton = node;
	},
	
	/**
	 * Function: setFormValidating
	 * 
	 * If a form registered with the specified formId
	 * exists a local property will be set that causes
	 * validation to be turned on/off depending on the argument.
	 * 
	 * Parameters:
	 * 
	 * formId - The id of the form to turn validation on/off for.
	 * validate - Boolean for whether or not to validate form, if not specified assumes true.
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
		if(!evt || dj_undef("target", evt)) {
			dojo.raise("No valid form event found with argument: " + evt);
			return;
		}
		
		var id=evt.target.getAttribute("id");
		if (!id) {
			dojo.raise("Form had no id attribute.");
			return;
		}
		var form = dojo.byId(id);
		
		if (!dj_undef("value", form.submitmode)
			&& (form.submitmode.value == "cancel" || form.submitmode.value == "refresh")) {
			return;
		}
		
		if (!tapestry.form.validation.validateForm(form, this.forms[id])) {
			dojo.event.browser.stopEvent(evt);
		}
	},
	
	/**
	 * Function: submit
	 * 
	 * Submits the form specified, optionally setting the submitname
	 * hidden input field to the value of submitName to let the Form 
	 * component on server know which button caused the submission. (For
	 * the case of submit button listeners).
	 * 
	 * Parameters:
	 * 
	 * form			-	The form(form id) to submit.
	 * submitName	- 	Optional submit name string to use when submitting. This is used
	 * 					to associate a form submission with a particular component, like a
	 * 					Submit/LinkSubmit/etc..
	 * parms		-	Optional extra set of arguments that can control the form submission semantics
	 * 					such as url/async/json/etc. 
	 */
	submit:function(form, submitName, parms){
		var form=dojo.byId(form);
		if (!form) {
			dojo.raise("Form not found with id " + form);
			return;
		}
		var id=form.getAttribute("id");
		
		if (submitName){
			form.submitname.value=submitName;
		}
		
		if (!dj_undef("value", form.submitmode)
			&& (form.submitmode.value == "cancel" || form.submitmode.value == "refresh")) {
			form.submit();
			return;
		}
		
		if (!tapestry.form.validation.validateForm(form, this.forms[id])) {
			return;
		}
		
		if (parms && !dj_undef("async", parms) && parms.async) {
			tapestry.form.submitAsync(form, null, submitName, parms);
			return;
		} else if(!dj_undef(id, this.forms) && this.forms[id].async){
			tapestry.form.submitAsync(form);
			return;
		}
		
		form.submit();
	},
	
	/**
	 * Function: cancel
	 * 
	 * Submits the form to the server in "cancel" mode, invoking any cancel listeners
	 * registered to the server side equivalent to the form passed in.
	 * 
	 * Parameters:
	 * 	
	 * 	form 		-	The form(form id) to cancel.
	 * 	submitName	- 	Optional submit name string to use when submitting. This is used
	 * 					to associate a form submission with a particular component, like a
	 * 					Submit/LinkSubmit/etc..
	 */
	cancel:function(form, submitName){
		var form=dojo.byId(form);
		if (!form){
			dojo.raise("Form not found with id " + form);
			return;
		}
		
		form.submitmode.value="cancel";
		
		this.submit(form, submitName);
	},
	
	/**
	 * Function: refresh
	 * 
	 * Submits the form to the server in "refresh" mode, invoking any refresh listeners
	 * registered to the server side equivalent to the form passed in.
	 * 
	 * Parameters:
	 * 	
	 * 	form 		-	The form(form id) to refresh.
	 * 	submitName	- 	Optional submit name string to use when submitting. This is used
	 * 					to associate a form submission with a particular component, like a
	 * 					Submit/LinkSubmit/etc..
	 */
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
	 * Function: submitAsync
	 * 
	 * Does almost the same thing as <tapestry.form.submit>, 
	 * but submits the request via XHR to the server asynchronously.
	 * 
	 * Parameters:
	 * 
	 *	form		-	The form(form id) to submit.
	 *	content		-	Optional content map, mainly used to pass in browser
	 * 					event parameters to form submission, but can be any
	 * 					typical form/value pair.
	 *	submitName	-	Optional submit name string to use when submitting.
	 *	parms		-	Optional set of extra parms that can override the defautls for 
	 * 					this specific form submission, like the url/async/json behaviour of 
	 * 					the submission.
	 */
	submitAsync:function(form, content, submitName, parms){
		var form=dojo.byId(form);
		if (!form) {
			dojo.raise("Form not found with id " + id);
			return;
		}
		var formId=form.getAttribute("id");
		
		if (!tapestry.form.validation.validateForm(form, this.forms[formId])) {
			dojo.log.debug("Form validation failed for form with id " + formId);
			return;
		}
		
		if (submitName){
			form.submitname.value=submitName;
			if(!content){ content={}; }
			if(form[submitName]){
				content[submitName]=form[submitName].value;
			}
		}
		
		// handle submissions from input buttons
		if (!dj_undef("clickedButton", this.forms[formId])) {
			if (!content) { content={}; }
			content[this.forms[formId].clickedButton.getAttribute("name")]=this.forms[formId].clickedButton.getAttribute("value");
		}
		
		var kwArgs={
			formNode:form,
			content:content,
            useCache:true,
            preventCache:true,
            error: (function(){tapestry.error.apply(this, arguments);}),
            encoding: "UTF-8"
		};
		
		// check for override
		if (parms){
			if (!dj_undef("url", parms)) { kwArgs.url=parms.url; }
		}
		
		if (this.forms[formId].json || parms && parms.json) {
			kwArgs.headers={"json":true};
			kwArgs.mimetype="text/json";
		} else {
			kwArgs.headers={"dojo-ajax-request":true};
			kwArgs.mimetype="text/xml";
			kwArgs.load=(function(){tapestry.load.apply(this, arguments);});
		}
		
		dojo.io.queueBind(kwArgs);
	}
}

dojo.require("tapestry.form.validation");
dojo.require("tapestry.form_compat");
