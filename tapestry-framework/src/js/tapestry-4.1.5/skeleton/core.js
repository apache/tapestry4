/**
 * This is a skeleton file. It contains the
 * javascript objects and methods that tapestry requires.
 *
 * It should be used as a reference for building implementations
 * specific to existing javascript frameworks.  
 */
 
// dj_undef function that checks if an object is not defined in the window scope  
if (!window.dj_undef) window.dj_undef=function(name) {
	return (typeof this[name] == "undefined"); }
	
// the two required objects, dojo and tapestry
var dojo={
    // those methods should only be needed for the dojo implementations
    // others will probably leave those empty
    registerModulePath: function() {tapestry.log('dojo.registerModulePath', arguments);},
    require: function() {tapestry.log('dojo.require', arguments);}
};

var tapestry={
    // setup firebug logging - useful for finding unimplemented methods
    log: function() {                   
        if (window.console) console.log.apply(this, arguments);
    },
    /**
     * Executes the passed function when the document has done loading
     */
    addOnLoad: function(fn) {
        tapestry.log('t.addOnLoad');
    },
    /**
     * Returns the dom node with the given id
     */
    byId: document.getElementById,

    /**
     * Makes sure that the given namespace (passed as a string) exists
     * and is a valid javascript object.  
     */
    provide: function() {tapestry.log('t.provide', arguments);},

    /**
     * Connects the event of the given target node to the given function of
     * the global namespace "tapestry".
     * Users of this method usually attach custom methods to the tapestry namespace
     * before calling this.
     *
     * Parameters: target, event, funcName
     */
    connect: function() {tapestry.log('t.connect', arguments);},

    /**
     * Disconnects the event of the given target node from the given function of
     * the global namespace "tapestry"
     *
     * Parameters: target, event, funcName
     */
    cleanConnect: function() {tapestry.log('t.cleanConnect', arguments);},
    
    /**
     * Connects the event of the given widget with the given function of
     * the global namespace "tapestry"
     *
     * Parameters: widgetId, event, funcName
     */
    connectWidget: function() {tapestry.log('t.connectWidget', arguments);},    
    
    /**
     * Disconnects the event of the given widget from the given function of
     * the global namespace "tapestry"
     *
     * Parameters: widgetId, event, funcName
     */
    cleanConnectWidget: function() {tapestry.log('t.cleanConnectWidget', arguments);},    

	/**
	 * Perform an XHR.
	 * Implementation should set the mime-type to either "text/xml" or
	 * "text/json" and include the request headers described in the comments to the
	 * json parameter.
     * Implementations are also responsible for handling the responses.  
	 *
	 * Parameters:
	 * 	url     - The url to bind the request to.
	 * 	content - A properties map of optional extra content to send.
	 *  json    - (Optional) Boolean parameter specifying whether or not to create a
	 * 		    json request. If true, the request headers should include "json":true.
	 *          If false or unspecified, they should contain "dojo-ajax-request":true
	 */
    bind: function(url, content, json){tapestry.log('t.bind', arguments);},

    /**
     * Helper that builds the content from eventName and (triggered) id and then forwards
     * execution to tapestry.bind
     * 
     * @param url
     * @param id
     * @param json
     * @param eventName
     */
    linkOnClick: function(url, id, json, eventName) {tapestry.log('t.linkOnClick', arguments);}
};

tapestry.form = {
    /**
     * Submits the specified form.
     * Should check the value of form.submitmode to find out what type of
     * submission (cancel, refresh or normal) to do and whether to run client validation.
     *
     * Parameters:
	 * form			-	The form or form id to submit.
	 * submitName	- 	(Optional) Submit name to use when submitting. This is used
	 * 					to associate a form submission with a particular component.
	 *                  Implementations will typically just set form.submitname to this value.
	 * parms		-	(Optional) Extra set of parameters. Implementations can just look for
     *                  the async key and if that's set to true, they should perform an async
     *                  submission.
     */
    submit: function(formId, submitName, parms) {tapestry.log('t.f.submit', arguments);},

    /** Same as submit, but forces cancel submission */
    cancel: function(formId, submitName, parms) {tapestry.log('t.f.submit', arguments);},

    /** Same as submit, but forces refresh submission */
    refresh: function(formId, submitName, parms) {tapestry.log('t.f.submit', arguments);},

    /**
	 * Registers a form and allows definition of its properties.
	 * Implementation should keep track of such properties and
	 * use them later on, when the form is submitted.
	 *
	 * Parameters:
	 *	id		-	The form or form id to register.
	 *  async	-	Boolean, if true form submission should be asynchronous.
	 *  json	-	Boolean, if true form submission should be asyncrhronous json.
	 */
    registerForm: function(formId, async, json) {tapestry.log('t.f.registerForm', arguments);},

    /**
     * Registers a form validation/translation profile.
     * TODO: Describe profile structure.
     *
	 * Parameters:
	 *	formId		-	The form or form id to register profile with.
	 *	profile	    -	The object containing all of the validation/value constraints for the form.
     */    
    registerProfile: function(formId, profile) {tapestry.log('t.f.registerProfile', arguments);},

	/**
	 * Clears any previously registered validation profiles on the specified form.
	 *
	 * Parameters:
	 *	formId      -   The form id to clear profiles for.
	 */
    clearProfiles: function(formId) {tapestry.log('t.f.clearProfiles', arguments);},

    /**
     * Brings keyboard input focus to the specified field.
     */
    focusField: function(fieldId) {tapestry.log('t.f.focusField', arguments);},

    // TODO: Describe validation methods
    datetime: {
        isValidDate: function(date) {tapestry.log('t.f.d.isValidDate', arguments);return true;}        
    },
    
    validation: {
        isReal: function() {tapestry.log('t.f.v.isReal', arguments);return true;},
        greaterThanOrEqual: function() {tapestry.log('t.f.v.greaterThanOrEqual', arguments);return true;},
        lessThanOrEqual: function() {tapestry.log('t.f.v.lessThanOrEqual', arguments);return true;},
        isText: function() {tapestry.log('t.f.v.isText', arguments);return true;},
        isEmailAddress: function() {tapestry.log('t.f.v.isEmailAddress', arguments);return true;},
        isValidPattern: function() {tapestry.log('t.f.v.isValidPattern', arguments);return true;},
        validateForm: function() {tapestry.log('t.f.v.validateForm', arguments);return true;}
    }
};

tapestry.event = {
    /**
     * Takes an incoming browser generated event (like key/mouse events) and
     * creates a js object holding the basic values of the event in order for
     * it to be submitted to the server.
     *
     * Parameters:
     *	event - The javascript event method is based on, if it isn't a valid
     * 				browser event it will be ignored.
     *	props - The existing property object to set the values on, if it doesn't
     * 				exist one will be created.
     *  args  - The arguments from an method-call interception
     *
     * Returns:
     * The desired event properties bound to an object. Ie obj.target,obj.charCode, etc..
     */
    buildEventProperties:function(event, props, args){tapestry.log('t.e.buildEventProperties', arguments);},
    stopEvent: function() {tapestry.log('t.e.stopEvent', arguments);}
};

tapestry.widget = {
    synchronizeWidgetState: function() {tapestry.log('t.w.synchronizeWidgetState', arguments);}
};
