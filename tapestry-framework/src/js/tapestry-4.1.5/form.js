dojo.provide("tapestry.form");
dojo.provide("tapestry.form.datetime");
dojo.provide("tapestry.form.validation");

dojo.require("dojo.event.browser");
dojo.require("dojo.dom");
dojo.require("dojo.html.selection");
dojo.require("tapestry.core");
dojo.require("dojo.date.format");
dojo.require("dojo.validate.datetime");
dojo.require("dojo.validate.check");
dojo.require("dojo.html.style");

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
	 * If possible, brings keyboard input focus to the specified field.
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
		try{
		var f=dojo.widget.byId(field);
		if(f && !dj_undef("focus", f)){
			if (dojo.html.isVisible(f)){
				f.focus();
			}
			return;
		} else {
			f = dojo.byId(field);
		}
		
		if (!f) { return; }
        if (field.disabled || field.clientWidth < 1) {
			return;
		}
        
        if(!dj_undef("focus", f) && dojo.html.isShowing(f)){
            f.focus();
			return;
		}
		
        dojo.html.selectInputText(field);} catch(e){dojo.log.debug(e);}
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
        var elm = e.target;
        if (!dj_undef("form", elm)){
            dojo.log.debug("Submit event was generated from element: ", elm);
            elm = elm.form;
        }
        tapestry.form.submitAsync(elm);
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
		
		this.forms[id].profiles.push(profile);
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
			this.forms[formId].validateForm = validate;
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
		form=dojo.byId(form);
		if (!form) {
			dojo.raise("Form not found with id " + form);
			return;
		}
		var id=form.getAttribute("id");
        if (submitName){
            form.submitname.value = submitName;
        }

        if (!dj_undef("value", form.submitmode)
                && (form.submitmode.value == "cancel" || form.submitmode.value == "refresh")
                && !parms) {
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
	 *  parms       -   Optional object parms passed through to tapestry.form.submit().
	 */
	cancel:function(form, submitName, parms){
		form=dojo.byId(form);
		if (!form){
			dojo.raise("Form not found with id " + form);
			return;
		}

        var formName=form.getAttribute("id");
        var validateState=tapestry.form.forms[formName].validateForm;
        tapestry.form.setFormValidating(formName, false);

        var previous = form.submitmode.value;
        form.submitmode.value="cancel";

        if (parms && !dj_undef("async", parms) && parms.async){
            this.submitAsync(form, null, submitName, parms);
            form.submitmode.value = previous;
            tapestry.form.setFormValidating(formName, validateState);
        } else {
            this.submit(form, submitName, parms);
        }
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
	 *  parms       -   Optional object parms passed through to tapestry.form.submit().
	 */
	refresh:function(form, submitName, parms){
		form=dojo.byId(form);
		if (!form){
			dojo.raise("Form not found with id " + form);
			return;
		}

        var formName=form.getAttribute("id");
        var validateState=tapestry.form.forms[formName].validateForm;
        tapestry.form.setFormValidating(formName, false);

        var previous = form.submitmode.value;
        form.submitmode.value="refresh";

        if (parms && !dj_undef("async", parms) && parms.async){
            this.submitAsync(form, null, submitName, parms);
            form.submitmode.value = previous;
            tapestry.form.setFormValidating(formName, validateState);
        } else {
            this.submit(form, submitName, parms);
        }
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
		form=dojo.byId(form);
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
            var previous = form.submitname.value;
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
            delete this.forms[formId].clickedButton;
        }

		var kwArgs={
			formNode:form,
			content:content,
            useCache:true,
            preventCache:true,
            error: (function(){tapestry.error.apply(this, arguments);}),
            encoding: tapestry.requestEncoding
		};
		
		// check for override
		if (parms){
			if (!dj_undef("url", parms)) { kwArgs.url=parms.url; }
		}

		if (this.forms[formId].json || parms && parms.json) {
			kwArgs.headers={"json":true};
			kwArgs.mimetype="text/json";
            kwArgs.load=(function(){tapestry.loadJson.apply(this, arguments);});
        } else {
			kwArgs.headers={"dojo-ajax-request":true};
			kwArgs.mimetype="text/xml";
			kwArgs.load=(function(){tapestry.load.apply(this, arguments);});
		}
		tapestry.requestsInFlight++;
        dojo.io.queueBind(kwArgs);

        if (submitName){
            form.submitname.value = previous;
        }
    }
}

tapestry.form.validation={

	missingClass:"fieldMissing", // default css class that will be applied to fields missing a value
	invalidClass:"fieldInvalid", // default css class applied to fields with invalid data

	dialogName:"tapestry:AlertDialog",

	/**
	 * Main entry point for running form validation. The
	 * props object passed in contains a number of fields that
	 * are managed by tapestry.form:
	 *
	 * 		props = {
	 * 			validateForm:[true|false] // whether to run validation at all
	 * 			profiles:[profile1, profile2] // set of dojo.validate.check() style profiles
	 * 										  // that may have been registered with form
	 * 		}
	 *
	 * The individual profiles will contain any of the data described by the dojo documentation
	 * for dojo.validate.check(). In addition to that, each profile will also have a corresponding
	 * string message to display if the specified condition has been met. For example, if you have
	 * specified that a select field named "select1" was required your profile would look something
	 * like:
	 *
	 * 		profile = {
	 * 			"required":["select1"], // normal dojo.validate.check data
	 * 			"select1":{ // tapestry field/error type specific data
	 * 				"required":"You must select a value for select1."
	 * 			}
	 * 		}
	 *
	 * It is intended for you to call dojo.validate.check(form, profile) for each profile
	 * stored in the "profiles" field, as well as deciding how to display errors / warnings.
	 *
	 * @return Boolean indicating if form submission should continue. If false the form
	 * 			will ~not~ be submitted.
	 */
	validateForm:function(form, props){
		if (typeof form == "undefined") {return false;}
		if (typeof props == "undefined") {return true;} // form exists but no profile? just submit I guess..
        if (!props.validateForm) {return true;}

		try {
			this.clearValidationDecorations(form, props);

			for (var i=0; i < props.profiles.length; i++) {
				var results=dojo.validate.check(form, props.profiles[i]);
                if (!this.processResults(form, results, props.profiles[i])) {
					this.summarizeErrors(form, results, props.profiles[i]);
					return false;
				}
			}
		} catch (e) {
			// since so many dynamic function calls may happen in here it's best that we
			// catch all of them and log them or else peoples forms might still get submitted
			// and they'd never be able to figure out what was wrong
			dojo.log.exception("Error validating", e, true);
			return false;
		}

		return true;
	},

	/**
	 * Called for each registered profile on a form after
	 * dojo.validate.check() has been called. This function is
	 * expected to do UI related notifications of fields in error.
	 *
	 * @param form The form that was validated.
	 * @param results The result of calling dojo.validate.check(form,profile)
	 * @param profile The original profile used to validate form, also holds
	 * 				  validation error messages to be used for each field.
	 *
	 * @return Boolean, if false form should not be submitted and all validation
	 * 		   should be stopped. If true validation will continue and eventually
	 * 		   form will be submitted.
	 */
	processResults:function(form, results, profile){
		if (results.isSuccessful()) { return true; }

        var formValid=true;
		if (results.hasMissing()) {
			var missing=results.getMissing();
            for (var i=0; i < missing.length; i++) {
				this.handleMissingField(missing[i], profile);
			}

			formValid=false;
		}

		if (results.hasInvalid()) {
			var invalid=results.getInvalid();
            for (var i=0; i < invalid.length; i++) {
				this.handleInvalidField(invalid[i], profile);
			}

			formValid=false;
		}

		return formValid; // if got past successful everything is invalid
	},

	/**
	 * Default field decorator for missing fields.
	 *
	 * @param field The field element that was missing data.
	 * @param profile The form validation profile.
	 */
	handleMissingField:function(field, profile){
		field=dojo.byId(field);
		if (dj_undef("type", field)) {return;}
		dojo.html.removeClass(field, this.invalidClass);

		if (!dojo.html.hasClass(field, this.missingClass)){
			dojo.html.prependClass(field, this.missingClass);
		}
	},

	/**
	 * Default field decorator for invalid fields.
	 *
	 * @param field The field element that had invalid data.
	 * @param profile The form validation profile.
	 */
	handleInvalidField:function(field, profile){
		field=dojo.byId(field);
		if (dj_undef("type", field)) {return;}
		dojo.html.removeClass(field, this.missingClass);

		if (!dojo.html.hasClass(field, this.invalidClass)){
			dojo.html.prependClass(field, this.invalidClass);
		}
	},

	/**
	 * Clears out previous css classes set on fields
	 * in error.
	 */
	clearValidationDecorations:function(form, props){

        for (var i=0; i < props.profiles.length; i++) {

            for (var fieldName in props.profiles[i]) {
                if (dj_undef("type", form.elements[fieldName]) || typeof form.elements[fieldName].type == "undefined"
                        || form.elements[fieldName].type == "submit"
                        || form.elements[fieldName].type == "hidden") { continue; }

                dojo.html.removeClass(form.elements[fieldName], this.missingClass);
                dojo.html.removeClass(form.elements[fieldName], this.invalidClass);
            }
        }
	},

	/**
	 * Optionally allows an alert dialog/dhtml dialog/etc to
	 * be displayed to user to alert them to the invalid state
	 * of their form if validation errors have occurred.
	 *
	 * @param form The form being validated.
	 * @param results Returned value of dojo.validate.check(form, profile)
	 * @param profile Validation profile definition
	 */
	summarizeErrors:function(form, results, profile){
		var merrs=[];
		var ierrs=[];
		tapestry.form.currentFocus=null;

		if (results.hasMissing()){
			var fields=results.getMissing();
			for (var i=0; i<fields.length; i++){
				if(i==0 && !tapestry.form.currentFocus){
					tapestry.form.currentFocus=fields[i];
				}
				if (profile[fields[i]] && profile[fields[i]]["required"]){
					if (dojo.lang.isArray(profile[fields[i]]["required"])) {
						for (var z=0; z < profile[fields[i]]["required"].length; z++)
							merrs.push(profile[fields[i]]["required"][z]);
					} else
						merrs.push(profile[fields[i]]["required"]);
				}
			}
		}
		if (results.hasInvalid()){
			var fields=results.getInvalid();
			for (var i=0; i<fields.length; i++){
				if(i==0 && !tapestry.form.currentFocus){
					tapestry.form.currentFocus=fields[i];
				}
				if (profile[fields[i]] && profile[fields[i]]["constraints"]){
					if (dojo.lang.isArray(profile[fields[i]]["constraints"])) {
						for (var z=0; z < profile[fields[i]]["constraints"].length; z++)
							ierrs.push(profile[fields[i]]["constraints"][z]);
					} else
						ierrs.push(profile[fields[i]]["constraints"]);
				}
			}
		}

		var msg="";
		if (merrs.length > 0) {
			msg+='<ul class="missingList">';
			for (var i=0; i<merrs.length;i++) {
				msg+="<li>"+merrs[i]+"</li>";
			}
			msg+="</ul>";
		}
		if (ierrs.length > 0) {
			msg+='<ul class="invalidList">';
			for (var i=0; i<ierrs.length;i++) {
				msg+="<li>"+ierrs[i]+"</li>";
			}
			msg+="</ul>";
		}

        dojo.require("dojo.widget.*");
        dojo.require("tapestry.widget.AlertDialog");

        var ad=dojo.widget.byId("validationDialog");
		if (ad) {
			ad.setMessage(msg);
			ad.show();
			return;
		}

		var node=document.createElement("span");
		document.body.appendChild(node);
		var dialog=dojo.widget.createWidget(this.dialogName,
						{
							widgetId:"validationDialog",
							message:msg
						}, node);
		dialog.show();
	},

	/**
	 * Validates that the input value matches the given
	 * regexp pattern.
	 *
	 * @param value The string value to be evaluated.
	 * @param pattern The regexp pattern used to match against value.
	 */
	isValidPattern:function(value, pattern){
		if (typeof value != "string" || typeof pattern != "string") { return false; }

		var re = new RegExp(pattern);
		return re.test(value);
	},

	isPalleteSelected:function(elem){
		return elem.length > 0;
	},

   /**
    * Validates that the input value is equal with the value of the given input control.
    */
    isEqual:function(value, other){
        var otherValue = dojo.byId(other).value;
        return value == otherValue;
    },

   /**
    * Validates that the input value is not equal with the value of the given input control.
    */
    isNotEqual:function(value, other){
        return !tapestry.form.validation.isEqual(value, other);
    },

   /**
    *  Checks that the value given is greater than or equal to the value of
    *  minString. Uses dojo.i18n.number.parse() to parse out the values using
    *  the locale settings configured for the current page.
    */
    greaterThanOrEqual:function(value, minString, flags){
        dojo.require("dojo.i18n.number");
        flags.validate=false;
        var min = dojo.i18n.number.parse(minString, null, flags);
        var num = dojo.i18n.number.parse(value, null, flags);
        if (Number.NaN == num) { return false; }

        return num >= min;
    },

   /**
    *  Checks that the value given is less than or equal to the value of
    *  maxString. Uses dojo.i18n.number.parse() to parse out the values using
    *  the locale settings configured for the current page.
    */
    lessThanOrEqual:function(value, maxString, flags){
        dojo.require("dojo.i18n.number");
        flags.validate=false;
        var max = dojo.i18n.number.parse(maxString, null, flags);
        var num = dojo.i18n.number.parse(value, null, flags);
        if (Number.NaN == num) { return false; }

        return num <= max;
    },
    
    isText:dojo.validate.isText,
    
    isEmailAddress:function(){
        dojo.require("dojo.validate.web");
        return dojo.validate.isEmailAddress.apply(dojo.validate, arguments);
    },
    
    isReal:function(){
        dojo.require("dojo.i18n.number");
        return dojo.i18n.number.isReal.apply(dojo.i18n.number, arguments);        
    }    
    
}

tapestry.form.datetime={

	/**
	 * Checks if the specified value is a valid date, according to
	 * the flags passed in.
	 *
	 * @param value The string value of the date being validated.
	 * @param flags An object.
	 * 		flags.format 	A string format pattern that will be used to validate
	 * 						the incoming value via @link dojo.validate.isValidDate(value, format).
	 * 		flags.max		A string date value representing the maximum date that can be selected.
	 * 		flags.min		A string date value representing the minimum date that can be selected.
	 * @return Boolean. True if valid, false otherwise.
	 */
	isValidDate:function(value, flags){
		if(!value){return false;}

		if (!flags){
			dojo.raise("isValidDate: value and flags must be specified");
			return;
		}

		// parse date value
		var dateValue=null;
		try {
			dateValue = dojo.date.parse(value, flags);
		} catch (e) {
			dojo.log.exception("Error parsing input date.", e, true);
			return false;
		}

		if(dateValue == null) { return false; }

		// convert to format that is validatable
		value=dojo.date.format(dateValue, flags);

		// TODO: This is totally useless right now, doesn't even accept formats with string equivs
		// See a better method http://www.mattkruse.com/javascript/date/source.html
		// basic format validation
		// if (!dojo.validate.isValidDate(value, flags.format))
		//	return false;

		// max date
		if (!dj_undef("max", flags)){
			if (typeof flags.max == "string"){
				flags.max=dojo.date.parse(flags.max, flags);
			}
			if (dojo.date.compare(dateValue, flags.max, dojo.date.compareTypes.DATE) > 0)
				return false;
		}

		// min date
		if (!dj_undef("min", flags)){
			if (typeof flags.min == "string"){
				flags.min=dojo.date.parse(flags.min, flags);
			}
			if (dojo.date.compare(dateValue, flags.min, dojo.date.compareTypes.DATE) < 0)
				return false;
		}

		return true;
	}

}
