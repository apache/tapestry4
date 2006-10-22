// Copyright 2006 The Apache Software Foundation
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
dojo.provide("tapestry.widget");
dojo.provide("tapestry.widget.Widget");

dojo.require("dojo.widget.*");

// Define core widget management methods
tapestry.widget = {
	
	/**
	 * Ensures that the widget specified currently exists, 
	 * and if not creates a new widget via dojo.widget.createWidget().
	 * 
	 * @param widgetId 
	 * 			The unique widgetId to search for, will also be
	 * 			used to locate and create the widget via the corresponding
	 * 			dom node id if the widget doesn't already exist.
	 * @param type 
	 * 			The dojo widget type string. Ie "dialog" / "combobox" / etc..
	 * @param props 
	 * 			The js properties object to create the widget with.
	 */
	synchronizeWidgetState : function(widgetId, type, props, destroy){
		if(typeof destroy == "undefined"){
			destroy=true;
		}
		var widget = dojo.widget.byId(widgetId);
		
		if (!widget) {
			this.createWidget(widgetId, type, props);
		} else if (destroy){
			widget.destroy();
			this.createWidget(widgetId, type, props);
		} else {
			this.setWidgetProperties(widget, props);
		}
	},
	
	/**
	 * Creates a new widget (if possible) via dojo.widget.createWidget()
	 */
	createWidget : function(widgetId, type, props) {
		var node = dojo.byId(widgetId);
		if (!node) {
			dojo.raise("createWidget() Node not found with specified id of '" + widgetId + "'.");
			return;
		}
		
		if (!props["widgetId"]) {
			props["widgetId"]=widgetId;
		}
		
		// handle disabling widgets
		var w = dojo.widget.createWidget(type, props, node);
		this.setWidgetProperties(w, props);
	},
	
	setWidgetProperties: function(w, props){
		if (!dj_undef("disabled",props) && props.disabled == true 
			&& dojo.lang.isFunction(w["disable"])){
			w.disable();
		}
	}
}