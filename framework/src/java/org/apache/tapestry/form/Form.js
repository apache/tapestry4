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

function default_invalid_field_handler(event, field, message)
{
  // Temporary, while all the event logic is getting munged together
  // inside one big handler.
  
  if (!event.abort && !field.disabled)
  {
    focus(field);
    
    window.alert(message);
    
    event.abort = true;
    event.cancelListeners = true;
  }
}

function focus(field)
{
	field.focus();
    
    if (field.select)
        field.select();
}

function trim(field)
{
	field.value = field.value.replace(/^\s+/g, '').replace(/\s+$/g, '');
}

function require(event, field, message)
{
    if (field.value.length == 0)
      event.invalid_field(field, message);
}

// FormSubmitEvent
// 
// Event object that identifies the form that was submitted,
// the type of submission ("submit", "refresh", "reset" or "cancel")
// and two flags.
// A listener may set the abort flag to true, which will prevent
// the Form submit or reset from occuring, but will not prevent
// other listeners from being invoked.  A listener may also set the
// cancelListeners flag, which will prevent further listeners from being 
// invoked.
// The invalid_field_handler is provided (by the FormEventManager)
// to handle any invalid fields.

function FormSubmitEvent(form, type, invalid_field_handler)
{
  this.form = form;
  this.type = type;
  this.abort = false;
  this.cancelListeners = false;
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
  
  // Add an events property to the form
  form.events = this;
   
  // Key is handler type ("submit", "refresh", "reset" or "cancel"), value
  // is an array of functions.
  this.handlers = {};
  
  // Inside onsubmit and onreset, "this" will be the form (how convienient)
  // so we need to carefully bridge this back to the FormEventManager instance.
  
  form.onsubmit = function() { return this.events.submit(); };
  form.onreset = function() { return this.events.reset(); };
  
  // Override this property when doing something more ambitious than
  // the default (which invokes window.alert(), focuses the field, and aborts
  // the form submit and the rest of the listeners).  
  // The function should take three parameters:
  // the FormSubmitEvent, the field object, and the message
  
  this.invalid_field_handler = default_invalid_field_handler;
}

// addListener(type, handler)
//
// type -- the handler type to add ("submit", "refresh", "reset" or "cancel"
// handler -- a function to execute

FormEventManager.prototype.addListener = function(type, handler)
{
  var array = this.handlers[type];
  if (array == null)
  {
    array = [];
    this.handlers[type] = array;
  }
  
  array.push(handler);
}

// invokeListeners(type, eventObject)
//
// type -- the type of handler to execute
// eventObject -- passed to the handler function

FormEventManager.prototype.invokeListeners = function(type, eventObj)
{
  if (eventObj.cancelListeners) return;
  
  var array = this.handlers[type];
   
  if (array == null) return;
  
  var length = array.length;
  for (var i = 0; i < length; i++)
  {
    var handler = array[i];
    handler.call(window, eventObj);
    
    if (eventObj.cancelListeners) return;
  }
}

// addCancelListener(handler)
//
// handler - receives notifications when the form is canceled

FormEventManager.prototype.addCancelListener= function(handler)
{
  this.addListener("cancel", handler);
}

// cancel()
//
// Cancels the form.  Any cancel listeners are invoked; unless one of them sets the abort
// flag on the event, then the form will be submitted, and the mode set to "cancel".

FormEventManager.prototype.cancel = function()
{
	var event = new FormSubmitEvent(this.form, "cancel", this.invalid_field_handler);
	
	this.invokeListeners("cancel", event);
	
	if (event.abort == false)
	{
	  this.form.submitmode.value = "cancel";
	  this.form.onsubmit = null;
	  this.form.submit();
	}
}

// addPreSubmitListener(handler)
//
// Typically used to setup state prior to the submit handlers being invoked.
// Pre-submit listeners are invoked before submit handlers are invoked.  If
// a pre-submit listener sets the cancelListeners flag on the event, the
// submit and post-submit listeners will not be invoked.

//
// form - the Form object for which a listener is added
// handler - recieves notification when the form is submitted

FormEventManager.prototype.addPreSubmitListener = function(handler)
{
  this.addListener("presubmit", handler);
}

// addSubmitListener(handler)
//
// Typically used for input validations; normal submit listeners are skipped when
// a form is submitted to refresh some of its values.  If a handler sets
// the cancelListener flag on the event, then subsequent submit handlers, and all
// post-submit handlers, will not be invoked.
//
// form - the Form object for which a listener is added
// handler - receives notifications when the form is submitted

FormEventManager.prototype.addSubmitListener = function(handler)
{
  this.addListener("submit", handler);
}

// addPostSubmitListener(handler)
// 
// Used to perform final cleanup after all submit listeners have been invoked.
//
// form - the Form object for which a listener is added
// handler - receives notifications when the form is submitted

FormEventManager.prototype.addPostSubmitListener = function(handler)
{
  this.addListener("postsubmit", handler);
}

// submit()
//
// Submits a form.  This is designed to be the form's onsubmit event handler, so it returns true
// to let the form submit, or false if any of the listeners set the abort flag on the event.
// Returns false if any listener set the event's abort flag, true otherwise.
//
// Invokes all pre-submit listeners, then all submit listeners, then all post-submit listeners.
// 

FormEventManager.prototype.submit = function()
{
	var event = new FormSubmitEvent(this.form, "submit", this.invalid_field_handler);

    this.invokeListeners("presubmit", event);
	this.invokeListeners("submit", event);	
	this.invokeListeners("postsubmit", event);
		
	if (event.abort)
      return false;
	  
	this.form.submitmode.value = "submit";
	
	return true;
}

// addRefreshListener(handler)
//
// Used for a limited number of situations where some logic is necessary even
// when a form is submitted for refresh.  Normal submit listeners are skipped, but
// refresh listeners are invoked.  The handler is added as a submit listener and
// a refresh listener.
//
// handler - receives notifications when the form is submitted (normally, or for refresh)

FormEventManager.prototype.addRefreshListener = function(handler)
{
  this.addListener("submit", handler);
  this.addListener("refresh", handler);
}

// refresh()
//
// Refreshes the form, which is a special kind of submit that bypasses the normal
// submit listeners.  This is used, typically, when updating a field causes requires
// that another field be updated via a server round-trip.  Refresh listeners
// can prevent the form submission by setting the event's abort flag.

FormEventManager.prototype.refresh = function()
{
	var event = new FormSubmitEvent(this.form, "refresh", this.invalid_field_handler);
	
	this.invokeListeners("refresh", event);
	
	if (event.abort)
	  return;
	  
    this.form.submitmode.value = "refresh";
	this.form.onsubmit = null;
	this.form.submit();
}

// addResetListener(handler)
//
// Allow for special behavior when a form is reset, intended as the
// form's onreset .  Listeners are invoked
// before the form is reset.

FormEventManager.prototype.addResetListener = function(handler)
{
  this.addListener("reset", handler);
}

// reset()
//
// Intended as an onreset event handler for a form.  Returns
// true normally, or false if the event's abort flag was
// set by a listener.

FormEventManager.prototype.reset = function()
{
  var event = new FormSubmitEvent(this.form, "reset", this.invalid_field_handler);
  
  this.invokeListeners("reset", event);
  
  return ! event.abort;
}

  
