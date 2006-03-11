// Copyright 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// Define a Tapestry object to contain most functions.

var Tapestry = new Object();

Tapestry.default_invalid_field_handler = function(event, field, message)
{
  if (!event.abort && !field.disabled)
  {
    Tapestry.set_focus(field);
    
    window.alert(message);
    
    event.abort = true;
    event.cancel_handlers = true;
  }
}

Tapestry.find = function(elementId)
{
  return document.getElementById(elementId);
}

Tapestry.register_form = function(formId)
{
  var form = this.find(formId);
  
  form.events = new FormEventManager(form);
}

Tapestry.onpresubmit = function(formId, handler)
{
  var form = this.find(formId);
  
  form.events.add_presubmit_handler(handler);
}

Tapestry.onsubmit = function(formId, handler)
{
  var form = this.find(formId);
  
  form.events.add_submit_handler(handler);
}

Tapestry.onpostsubmit = function(formId, handler)
{
  var form = this.find(formId);
  
  form.events.add_postsubmit_handler(handler);
}

Tapestry.onreset = function(formId, handler)
{
  var form = this.find(formId);
  
  form.events.add_reset_handler(handler);
}

Tapestry.onrefresh  = function(formId, handler)
{
  var form = this.find(formId);
  
  form.events.add_refresh_handler(handler);
}

Tapestry.oncancel = function(formId, handler)
{
  var form = this.find(formId);
  
  form.events.add_cancel_handler(handler);
}

Tapestry.set_focus = function (field)
{
	if (typeof field == "string") {
	    field = this.find(field);
		
        if (field) {
            if (!field.disabled && field.clientWidth > 0) {
            	if (field.focus) {
	            	field.focus();
                }
                if (field.isContentEditable || field.isContentEditable == null) {
                    if (field.select) {
                        field.select();
                    }
                }
            }
        }
    }
}

Tapestry.trim_field_value = function(fieldId)
{
	var field = this.find(fieldId);
	
	field.value = field.value.replace(/^\s+/g, '').replace(/\s+$/g, '');
}

Tapestry.require_field = function(event, fieldId, message)
{
    var field = this.find(fieldId);
    
    if (field.value.length == 0)
      event.invalid_field(field, message);
}

// Used by LinkSubmit components to force the form to submit

Tapestry.submit_form = function(form_id, field_name)
{
	var form = this.find(form_id);
	
	form.events.submit(field_name);
}

// FormSubmitEvent
// 
// Event object that identifies the form that was submitted,
// the type of submission ("submit", "refresh", "reset" or "cancel")
// and two flags.
// A listener may set the abort flag to true, which will prevent
// the Form submit or reset from occuring, but will not prevent
// other listeners from being invoked.  A listener may also set the
// cancel_handlers flag, which will prevent further listeners from being 
// invoked.
// The invalid_field_handler is provided (by the FormEventManager)
// to handle any invalid fields.

function FormSubmitEvent(form, type, invalid_field_handler)
{
  this.form = form;
  this.type = type;
  this.abort = false;
  this.cancel_handlers = false;
  this.invalid_field_handler = invalid_field_handler;
}

FormSubmitEvent.prototype.invalid_field = function(field, message)
{
  this.invalid_field_handler.call(window, this, field, message);
}

FormSubmitEvent.prototype.toString = function()
{
  return "FormSubmitEvent[" + this.form.name + " " + this.type + " " + this.abort + "]";
}


// FormEventManager
//
// Manages listeners (as handler functions) for a given Form object. Installs 
// onsubmit and onreset listeners on the form.

function FormEventManager(form)
{
  this.form = form;
   
  // Key is handler type ("submit", "refresh", "reset" or "cancel"), value
  // is an array of functions.
  this.handlers = {};
  
  // Inside onsubmit and onreset, "this" will be the form (how convienient)
  // so we need to carefully bridge this back to the FormEventManager instance.
  
  form.onsubmit = function() { return this.events.onsubmit_handler(); };
  form.onreset = function() { return this.events.onreset_handler(); };
  
  // Override this property when doing something more ambitious than
  // the default (which invokes window.alert(), focuses the field, and aborts
  // the form submit and the rest of the listeners).  
  // The function should take three parameters:
  // the FormSubmitEvent, the field object, and the message
  
  this.invalid_field_handler = Tapestry.default_invalid_field_handler;
}

// add_handler(type, handler)
//
// type -- the handler type to add ("submit", "refresh", "reset" or "cancel"
// handler -- a function to execute

FormEventManager.prototype.add_handler = function(type, handler)
{
  var array = this.handlers[type];
  if (array == null)
  {
    array = [];
    this.handlers[type] = array;
  }
  
  array.push(handler);
}

// invoke_handlers(type, eventObject)
//
// type -- the type of handler to execute
// eventObj -- passed to the handler function

FormEventManager.prototype.invoke_handlers = function(type, eventObj)
{
  if (eventObj.cancel_handlers) return;
  
  var array = this.handlers[type];
   
  if (array == null) return;
  
  var length = array.length;
  for (var i = 0; i < length; i++)
  {
    var handler = array[i];
    handler.call(window, eventObj);
    
    if (eventObj.cancel_handlers) return;
  }
}

// add_cancel_handler(handler)
//
// handler - receives notifications when the form is canceled

FormEventManager.prototype.add_cancel_handler = function(handler)
{
  this.add_handler("cancel", handler);
}

// cancel()
//
// Cancels the form.  Any cancel listeners are invoked; unless one of them sets the abort
// flag on the event, then the form will be submitted, and the mode set to "cancel".

FormEventManager.prototype.cancel = function()
{
	var event = new FormSubmitEvent(this.form, "cancel", this.invalid_field_handler);
	
	this.invoke_handlers("cancel", event);
	
	if (event.abort == false)
	{
	  this.form.submitmode.value = "cancel";
	  this.form.onsubmit = null;
	  this.form.submit();
	}
}

// add_presubmit_handler(handler)
//
// Typically used to setup state prior to the submit handlers being invoked.
// Pre-submit listeners are invoked before submit handlers are invoked.  If
// a pre-submit listener sets the cancelListeners flag on the event, the
// submit and post-submit listeners will not be invoked.

//
// form - the Form object for which a listener is added
// handler - recieves notification when the form is submitted

FormEventManager.prototype.add_presubmit_handler = function(handler)
{
  this.add_handler("presubmit", handler);
}

// add_submit_handler(handler)
//
// Typically used for input validations; normal submit listeners are skipped when
// a form is submitted to refresh some of its values.  If a handler sets
// the cancelListener flag on the event, then subsequent submit handlers, and all
// post-submit handlers, will not be invoked.
//
// form - the Form object for which a listener is added
// handler - receives notifications when the form is submitted

FormEventManager.prototype.add_submit_handler = function(handler)
{
  this.add_handler("submit", handler);
}

// add_postsubmit_handler(handler)
// 
// Used to perform final cleanup after all submit listeners have been invoked.
//
// form - the Form object for which a listener is added
// handler - receives notifications when the form is submitted

FormEventManager.prototype.add_postsubmit_handler = function(handler)
{
  this.add_handler("postsubmit", handler);
}

// submit()
//
// Submits a form programatically.
// 

FormEventManager.prototype.submit = function(name)
{
	if (this.onsubmit_handler())
	{
	  this.form.onsubmit = null;

      this.form.submitname.value = name;    
	  this.form.submit();
	}
}

// onsubmit_handler()
//
// The handler for the form's onsubmit event.  Invokes the presubmit, submit
// and postsubmit handlers and returns false if any handler sets the
// event's abort flag.  Otherwise, sets the form's submitmode to "submit"
// and return true.
// 

FormEventManager.prototype.onsubmit_handler = function()
{
	var event = new FormSubmitEvent(this.form, "submit", this.invalid_field_handler);

    this.invoke_handlers("presubmit", event);
	this.invoke_handlers("submit", event);	
	this.invoke_handlers("postsubmit", event);
		
	if (event.abort)
      return false;
	  
	this.form.submitmode.value = "submit";
	this.form.submitname.value = "";
	
	return true;
}

// add_refresh_handler(handler)
//
// Used for a limited number of situations where some logic is necessary even
// when a form is submitted for refresh.  Normal submit listeners are skipped, but
// refresh listeners are invoked.  The handler is added as a submit listener and
// a refresh listener.
//
// handler - receives notifications when the form is submitted (normally, or for refresh)

FormEventManager.prototype.add_refresh_handler = function(handler)
{
  this.add_handler("submit", handler);
  this.add_handler("refresh", handler);
}

// refresh()
//
// Refreshes the form, which is a special kind of submit that bypasses the normal
// submit listeners.  This is used, typically, when updating a field causes requires
// that another field be updated via a server round-trip.  Refresh listeners
// can prevent the form submission by setting the event's abort flag.

FormEventManager.prototype.refresh = function(name)
{
	var event = new FormSubmitEvent(this.form, "refresh", this.invalid_field_handler);
	
	this.invoke_handlers("refresh", event);
	
	if (event.abort)
	  return;
	  
    this.form.submitmode.value = "refresh";
    this.form.submitname.value = name;
	this.form.onsubmit = null;
	this.form.submit();
}

// add_reset_handler(handler)
//
// Allow for special behavior when a form is reset, intended as the
// form's onreset .  Listeners are invoked
// before the form is reset.

FormEventManager.prototype.add_reset_handler = function(handler)
{
  this.add_handler("reset", handler);
}

// onreset_handler()
//
// Handles a reset by invoking any "reset" handlers (which are rare).
// Returns true, unless a handler sets the event.abort flag.

FormEventManager.prototype.onreset_handler = function()
{
  var event = new FormSubmitEvent(this.form, "reset", this.invalid_field_handler);
  
  this.invoke_handlers("reset", event);
  
  return ! event.abort;
}

  
