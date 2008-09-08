/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("layer.widget");
dojo.provide("dojo.namespaces.dojo");
dojo.require("dojo.ns");

(function(){
	// Mapping of all widget short names to their full package names
	// This is used for widget autoloading - no dojo.require() is necessary.
	// If you use a widget in markup or create one dynamically, then this
	// mapping is used to find and load any dependencies not already loaded.
	// You should use your own namespace for any custom widgets.
	// For extra widgets you use, dojo.declare() may be used to explicitly load them.
	// Experimental and deprecated widgets are not included in this table
	var map = {
		html: {
			"accordioncontainer": "dojo.widget.AccordionContainer",
			"animatedpng": "dojo.widget.AnimatedPng",
			"button": "dojo.widget.Button",
			"chart": "dojo.widget.Chart",
			"checkbox": "dojo.widget.Checkbox",
			"clock": "dojo.widget.Clock",
			"colorpalette": "dojo.widget.ColorPalette",
			"combobox": "dojo.widget.ComboBox",
			"combobutton": "dojo.widget.Button",
			"contentpane": "dojo.widget.ContentPane",
			"currencytextbox": "dojo.widget.CurrencyTextbox",
			"datepicker": "dojo.widget.DatePicker",
			"datetextbox": "dojo.widget.DateTextbox",
			"debugconsole": "dojo.widget.DebugConsole",
			"dialog": "dojo.widget.Dialog",
			"dropdownbutton": "dojo.widget.Button",
			"dropdowndatepicker": "dojo.widget.DropdownDatePicker",
			"dropdowntimepicker": "dojo.widget.DropdownTimePicker",
			"emaillisttextbox": "dojo.widget.InternetTextbox",
			"emailtextbox": "dojo.widget.InternetTextbox",
			"editor": "dojo.widget.Editor",
			"editor2": "dojo.widget.Editor2",
			"filteringtable": "dojo.widget.FilteringTable",
			"fisheyelist": "dojo.widget.FisheyeList",
			"fisheyelistitem": "dojo.widget.FisheyeList",
			"floatingpane": "dojo.widget.FloatingPane",
			"modalfloatingpane": "dojo.widget.FloatingPane",
			"form": "dojo.widget.Form",
			"googlemap": "dojo.widget.GoogleMap",
			"inlineeditbox": "dojo.widget.InlineEditBox",
			"integerspinner": "dojo.widget.Spinner",
			"integertextbox": "dojo.widget.IntegerTextbox",
			"ipaddresstextbox": "dojo.widget.InternetTextbox",
			"layoutcontainer": "dojo.widget.LayoutContainer",
			"linkpane": "dojo.widget.LinkPane",
			"popupmenu2": "dojo.widget.Menu2",
			"menuitem2": "dojo.widget.Menu2",
			"menuseparator2": "dojo.widget.Menu2",
			"menubar2": "dojo.widget.Menu2",
			"menubaritem2": "dojo.widget.Menu2",
			"pagecontainer": "dojo.widget.PageContainer",
			"pagecontroller": "dojo.widget.PageContainer",
			"popupcontainer": "dojo.widget.PopupContainer",
			"progressbar": "dojo.widget.ProgressBar",
			"radiogroup": "dojo.widget.RadioGroup",
			"realnumbertextbox": "dojo.widget.RealNumberTextbox",
			"regexptextbox": "dojo.widget.RegexpTextbox",
			"repeater": "dojo.widget.Repeater", 
			"resizabletextarea": "dojo.widget.ResizableTextarea",
			"richtext": "dojo.widget.RichText",
			"select": "dojo.widget.Select",
			"show": "dojo.widget.Show",
			"showaction": "dojo.widget.ShowAction",
			"showslide": "dojo.widget.ShowSlide",
			"slidervertical": "dojo.widget.Slider",
			"sliderhorizontal": "dojo.widget.Slider",
			"slider":"dojo.widget.Slider",
			"slideshow": "dojo.widget.SlideShow",
			"sortabletable": "dojo.widget.SortableTable",
			"splitcontainer": "dojo.widget.SplitContainer",
			"tabcontainer": "dojo.widget.TabContainer",
			"tabcontroller": "dojo.widget.TabContainer",
			"taskbar": "dojo.widget.TaskBar",
			"textbox": "dojo.widget.Textbox",
			"timepicker": "dojo.widget.TimePicker",
			"timetextbox": "dojo.widget.DateTextbox",
			"titlepane": "dojo.widget.TitlePane",
			"toaster": "dojo.widget.Toaster",
			"toggler": "dojo.widget.Toggler",
			"toolbar": "dojo.widget.Toolbar",
			"toolbarcontainer": "dojo.widget.Toolbar",
			"toolbaritem": "dojo.widget.Toolbar",
			"toolbarbuttongroup": "dojo.widget.Toolbar",
			"toolbarbutton": "dojo.widget.Toolbar",
			"toolbardialog": "dojo.widget.Toolbar",
			"toolbarmenu": "dojo.widget.Toolbar",
			"toolbarseparator": "dojo.widget.Toolbar",
			"toolbarspace": "dojo.widget.Toolbar",
			"toolbarselect": "dojo.widget.Toolbar",
			"toolbarcolordialog": "dojo.widget.Toolbar",
			"tooltip": "dojo.widget.Tooltip",
			"tree": "dojo.widget.Tree",
			"treebasiccontroller": "dojo.widget.TreeBasicController",
			"treecontextmenu": "dojo.widget.TreeContextMenu",
			"treedisablewrapextension": "dojo.widget.TreeDisableWrapExtension",
			"treedociconextension": "dojo.widget.TreeDocIconExtension",
			"treeeditor": "dojo.widget.TreeEditor",
			"treeemphasizeonselect": "dojo.widget.TreeEmphasizeOnSelect",
			"treeexpandtonodeonselect": "dojo.widget.TreeExpandToNodeOnSelect",
			"treelinkextension": "dojo.widget.TreeLinkExtension",
			"treeloadingcontroller": "dojo.widget.TreeLoadingController",
			"treemenuitem": "dojo.widget.TreeContextMenu",
			"treenode": "dojo.widget.TreeNode",
			"treerpccontroller": "dojo.widget.TreeRPCController",
			"treeselector": "dojo.widget.TreeSelector",
			"treetoggleonselect": "dojo.widget.TreeToggleOnSelect",
			"treev3": "dojo.widget.TreeV3",
			"treebasiccontrollerv3": "dojo.widget.TreeBasicControllerV3",
			"treecontextmenuv3": "dojo.widget.TreeContextMenuV3",
			"treedndcontrollerv3": "dojo.widget.TreeDndControllerV3",
			"treeloadingcontrollerv3": "dojo.widget.TreeLoadingControllerV3",
			"treemenuitemv3": "dojo.widget.TreeContextMenuV3",
			"treerpccontrollerv3": "dojo.widget.TreeRpcControllerV3",
			"treeselectorv3": "dojo.widget.TreeSelectorV3",
			"urltextbox": "dojo.widget.InternetTextbox",
			"usphonenumbertextbox": "dojo.widget.UsTextbox",
			"ussocialsecuritynumbertextbox": "dojo.widget.UsTextbox",
			"usstatetextbox": "dojo.widget.UsTextbox",
			"usziptextbox": "dojo.widget.UsTextbox",
			"validationtextbox": "dojo.widget.ValidationTextbox",
			"treeloadingcontroller": "dojo.widget.TreeLoadingController",
			"wizardcontainer": "dojo.widget.Wizard",
			"wizardpane": "dojo.widget.Wizard",
			"yahoomap": "dojo.widget.YahooMap"
		},
		svg: {
			"chart": "dojo.widget.svg.Chart"
		},
		vml: {
			"chart": "dojo.widget.vml.Chart"
		}
	};

	dojo.addDojoNamespaceMapping = function(/*String*/shortName, /*String*/packageName){
	// summary:
	//	Add an entry to the mapping table for the dojo: namespace
	//
	// shortName: the name to be used as the widget's tag name in the dojo: namespace
	// packageName: the path to the Javascript module in dotted package notation
		map[shortName]=packageName;    
	};
	
	function dojoNamespaceResolver(name, domain){
		if(!domain){ domain="html"; }
		if(!map[domain]){ return null; }
		return map[domain][name];    
	}

	dojo.registerNamespaceResolver("dojo", dojoNamespaceResolver);
})();

dojo.provide("dojo.xml.Parse");
dojo.require("dojo.dom");

//TODO: determine dependencies
// currently has dependency on dojo.xml.DomUtil nodeTypes constants...

// using documentFragment nomenclature to generalize in case we don't want to require passing a collection of nodes with a single parent

dojo.xml.Parse = function(){
	// summary:
	//		generic class for taking a DOM node and parsing it into an object
	//		based on the "dojo tag name" of that node.
	// 
	//		supported dojoTagName's:
	//			<prefix:tag> => prefix:tag
	//			<dojo:tag> => dojo:tag
	//			<dojoTag> => dojo:tag
	//			<tag dojoType="type"> => dojo:type
	//			<tag dojoType="prefix:type"> => prefix:type
	//			<tag dojo:type="type"> => dojo:type
	//			<tag class="classa dojo-type classb"> => dojo:type	

	var isIE = ((dojo.render.html.capable)&&(dojo.render.html.ie));

	// get normalized (lowercase) tagName
	// some browsers report tagNames in lowercase no matter what
	function getTagName(node){
		/*
		return ((node)&&(node["tagName"]) ? node.tagName.toLowerCase() : '');
		*/
		try{
			return node.tagName.toLowerCase();
		}catch(e){
			return "";
		}
	}

	// locate dojo qualified tag name
	function getDojoTagName(node){
		var tagName = getTagName(node);
		if (!tagName){
				return '';
		}
		// any registered tag
		if((dojo.widget)&&(dojo.widget.tags[tagName])){
			return tagName;
		}
		// <prefix:tag> => prefix:tag
		var p = tagName.indexOf(":");
		if(p>=0){
			return tagName;
		}
		// <dojo:tag> => dojo:tag
		if(tagName.substr(0,5) == "dojo:"){
			return tagName;
		}
		if(dojo.render.html.capable && dojo.render.html.ie && node.scopeName != 'HTML'){
			return node.scopeName.toLowerCase() + ':' + tagName;
		}
		// <dojoTag> => dojo:tag
		if(tagName.substr(0,4) == "dojo"){
			// FIXME: this assumes tag names are always lower case
			return "dojo:" + tagName.substring(4);
		}
		// <tag dojoType="prefix:type"> => prefix:type
		// <tag dojoType="type"> => dojo:type
		var djt = node.getAttribute("dojoType") || node.getAttribute("dojotype");
		if(djt){
			if (djt.indexOf(":")<0){
				djt = "dojo:"+djt;
			}
			return djt.toLowerCase();
		}
		// <tag dojo:type="type"> => dojo:type
		djt = node.getAttributeNS && node.getAttributeNS(dojo.dom.dojoml,"type");
		if(djt){
			return "dojo:" + djt.toLowerCase();
		}
		// <tag dojo:type="type"> => dojo:type
		try{
			// FIXME: IE really really doesn't like this, so we squelch errors for it
			djt = node.getAttribute("dojo:type");
		}catch(e){ 
			// FIXME: log?  
		}
		if(djt){ return "dojo:"+djt.toLowerCase(); }
		// <tag class="classa dojo-type classb"> => dojo:type	
		if((dj_global["djConfig"])&&(!djConfig["ignoreClassNames"])){ 
			// FIXME: should we make this optionally enabled via djConfig?
			var classes = node.className||node.getAttribute("class");
			// FIXME: following line, without check for existence of classes.indexOf
			// breaks firefox 1.5's svg widgets
			if((classes )&&(classes.indexOf)&&(classes.indexOf("dojo-")!=-1)){
				var aclasses = classes.split(" ");
				for(var x=0, c=aclasses.length; x<c; x++){
					if(aclasses[x].slice(0, 5) == "dojo-"){
						return "dojo:"+aclasses[x].substr(5).toLowerCase(); 
					}
				}
			}
		}
		// no dojo-qualified name
		return '';
	}


	this.parseElement = function(	/*DomNode*/node,
									/*Boolean*/hasParentNodeSet, 
									/*Boolean*/optimizeForDojoML, 
									/*Integer*/thisIdx	){
		// summary:
		//		recursively parse the passed node, returning a normalized data
		//		structure that represents the "attributes of interest" of said
		//		elements. If optimizeForDojoML is true, only nodes that contain
		//		a "dojo tag name" will be inspected for attributes.
		// node: the DomNode to be treated as the root of inspection
		// hasParentNodeSet: no-op, please pass "null"
		// optimizeForDojoML: should we ignore non-Dojo nodes? Defaults to false.
		// thisIdx:
		//		a way to specify a synthetic "index" property in the resulting
		//		data structure. Otherwise the index property of the top-level
		//		return element is always "0".

		// TODOC: document return structure of a non-trivial element set

		// run shortcuts to bail out of processing up front to save time and
		// object alloc if possible.
		var tagName = getTagName(node);
		//There's a weird bug in IE where it counts end tags, e.g. </dojo:button> as nodes that should be parsed.  Ignore these
		if(isIE && tagName.indexOf("/")==0){ return null; }

		try{
			var attr = node.getAttribute("parseWidgets");
			if(attr && attr.toLowerCase() == "false"){
				return {};
			}
		}catch(e){/*continue*/}

		
		// look for a dojoml qualified name
		// process dojoml only when optimizeForDojoML is true
		var process = true;
		if(optimizeForDojoML){
			var dojoTagName = getDojoTagName(node);
			tagName = dojoTagName || tagName;
			process = Boolean(dojoTagName);
		}

		var parsedNodeSet = {};
		parsedNodeSet[tagName] = [];
		var pos = tagName.indexOf(":");
		if(pos>0){
			var ns = tagName.substring(0,pos);
			parsedNodeSet["ns"] = ns;
			// honor user namespace filters
			if((dojo.ns)&&(!dojo.ns.allow(ns))){process=false;}
		}

		if(process){
			var attributeSet = this.parseAttributes(node);
			for(var attr in attributeSet){
				if((!parsedNodeSet[tagName][attr])||(typeof parsedNodeSet[tagName][attr] != "array")){
					parsedNodeSet[tagName][attr] = [];
				}
				parsedNodeSet[tagName][attr].push(attributeSet[attr]);
			}	
			// FIXME: we might want to make this optional or provide cloning instead of
			// referencing, but for now, we include a node reference to allow
			// instantiated components to figure out their "roots"
			parsedNodeSet[tagName].nodeRef = node;
			parsedNodeSet.tagName = tagName;
			parsedNodeSet.index = thisIdx||0;
		}

		var count = 0;
		for(var i = 0; i < node.childNodes.length; i++){
			var tcn = node.childNodes.item(i);
			switch(tcn.nodeType){
				case  dojo.dom.ELEMENT_NODE: // element nodes, call this function recursively
					var ctn = getDojoTagName(tcn) || getTagName(tcn);
					if(!parsedNodeSet[ctn]){
						parsedNodeSet[ctn] = [];
					}
					parsedNodeSet[ctn].push(this.parseElement(tcn, true, optimizeForDojoML, count));
					if(	(tcn.childNodes.length == 1)&&
						(tcn.childNodes.item(0).nodeType == dojo.dom.TEXT_NODE)){
						parsedNodeSet[ctn][parsedNodeSet[ctn].length-1].value = tcn.childNodes.item(0).nodeValue;
					}
					count++;
					break;
				case  dojo.dom.TEXT_NODE: // if a single text node is the child, treat it as an attribute
					if(node.childNodes.length == 1){
						parsedNodeSet[tagName].push({ value: node.childNodes.item(0).nodeValue });
					}
					break;
				default: break;
				/*
				case  dojo.dom.ATTRIBUTE_NODE: // attribute node... not meaningful here
					break;
				case  dojo.dom.CDATA_SECTION_NODE: // cdata section... not sure if this would ever be meaningful... might be...
					break;
				case  dojo.dom.ENTITY_REFERENCE_NODE: // entity reference node... not meaningful here
					break;
				case  dojo.dom.ENTITY_NODE: // entity node... not sure if this would ever be meaningful
					break;
				case  dojo.dom.PROCESSING_INSTRUCTION_NODE: // processing instruction node... not meaningful here
					break;
				case  dojo.dom.COMMENT_NODE: // comment node... not not sure if this would ever be meaningful 
					break;
				case  dojo.dom.DOCUMENT_NODE: // document node... not sure if this would ever be meaningful
					break;
				case  dojo.dom.DOCUMENT_TYPE_NODE: // document type node... not meaningful here
					break;
				case  dojo.dom.DOCUMENT_FRAGMENT_NODE: // document fragment node... not meaningful here
					break;
				case  dojo.dom.NOTATION_NODE:// notation node... not meaningful here
					break;
				*/
			}
		}
		//return (hasParentNodeSet) ? parsedNodeSet[node.tagName] : parsedNodeSet;
		//if(parsedNodeSet.tagName)dojo.debug("parseElement: RETURNING NODE WITH TAGNAME "+parsedNodeSet.tagName);
		return parsedNodeSet;
	};


	/* parses a set of attributes on a node into an object tree */
	this.parseAttributes = function(/*DomNode*/node){
		// summary:
		// 		creates an attribute object that maps attribute values for the
		// 		passed node. Note that this is similar to creating a JSON
		// 		representation of a DOM node.
		// usage:
		//		a node with the following serialization:
		//			<div foo="bar" baz="thud">...</div>	
		//		would yeild the following return structure when passed into this
		//		function:
		//			{
		//				"foo": {
		//					"value": "bar"
		//				},
		//				"baz": {
		//					"value": "thud"
		//				}
		//			}
		//
		var parsedAttributeSet = {};
		var atts = node.attributes;
		// TODO: should we allow for duplicate attributes at this point...
		// would any of the relevant dom implementations even allow this?
		var attnode, i=0;
		while((attnode=atts[i++])){
			if(isIE){
				if(!attnode){ continue; }
				if((typeof attnode == "object")&&
					(typeof attnode.nodeValue == 'undefined')||
					(attnode.nodeValue == null)||
					(attnode.nodeValue == '')){ 
					continue; 
				}
			}

			var nn = attnode.nodeName.split(":");
			nn = (nn.length == 2) ? nn[1] : attnode.nodeName;
						
			parsedAttributeSet[nn] = { 
				value: attnode.nodeValue 
			};
		}
		return parsedAttributeSet;
	};
};

dojo.provide("dojo.widget.Widget");

dojo.require("dojo.lang.func");
dojo.require("dojo.lang.array");
dojo.require("dojo.lang.extras");
dojo.require("dojo.lang.declare");
dojo.require("dojo.ns");
dojo.require("dojo.widget.Manager");
dojo.require("dojo.event.*");

dojo.declare("dojo.widget.Widget", null,
	function(){
		// these properties aren't primitives and need to be created on a per-item
		// basis.

		// children: Array
		//		a list of all of the widgets that have been added as children of
		//		this component. Should only have values if isContainer is true.
		this.children = [];

		// extraArgs: Object
		//		a map of properties which the widget system tried to assign from
		//		user input but did not correspond to any of the properties set on
		//		the class prototype. These names will also be available in all
		//		lower-case form in this map
		this.extraArgs = {};
	},
{
	// parent: Widget
	//		the parent of this widget
	parent: null, 

	// isTopLevel: Boolean
	//		should this widget eat all events that bubble up to it?
	//		obviously, top-level and modal widgets should set these appropriately
	isTopLevel:  false, 

	// disabled: Boolean
	//		should this widget respond to user input?
	//		in markup, this is specified as "disabled='disabled'", or just "disabled"
	disabled: false,

	// isContainer: Boolean
	//		can this widget contain other widgets?
	isContainer: false, 

	// widgetId: String
	//		a unique, opaque ID string that can be assigned by users or by the
	//		system. If the developer passes an ID which is known not to be
	//		unique, the specified ID is ignored and the system-generated ID is
	//		used instead.
	widgetId: "",

	// widgetType: String
	//		used for building generic widgets
	widgetType: "Widget",

	// ns: String
	//		defaults to 'dojo'.  "namespace" is a reserved word in JavaScript, so we abbreviate
	ns: "dojo",

	getNamespacedType: function(){ 
		// summary:
		//		get the "full" name of the widget. If the widget comes from the
		//		"dojo" namespace and is a Button, calling this method will
		//		return "dojo:button", all lower-case
		return (this.ns ? this.ns + ":" + this.widgetType : this.widgetType).toLowerCase(); // String
	},
	
	toString: function(){
		// summary:
		//		returns a string that represents the widget. When a widget is
		//		cast to a string, this method will be used to generate the
		//		output. Currently, it does not implement any sort of reversable
		//		serialization.
		return '[Widget ' + this.getNamespacedType() + ', ' + (this.widgetId || 'NO ID') + ']'; // String
	},

	repr: function(){
		// summary: returns the string representation of the widget.
		return this.toString(); // String
	},

	enable: function(){
		// summary:
		//		enables the widget, usually involving unmasking inputs and
		//		turning on event handlers. Not implemented here.
		this.disabled = false;
	},

	disable: function(){
		// summary:
		//		disables the widget, usually involves masking inputs and
		//		unsetting event handlers. Not implemented here.
		this.disabled = true;
	},

	// TODO:
	//	1) this would be better in HtmlWidget rather than here?
	//	2) since many widgets don't care if they've been resized, maybe this should be a mixin?
	onResized: function(){
		// summary:
		//		A signal that widgets will call when they have been resized.
		//		Can be connected to for determining if a layout needs to be
		//		reflowed. Clients should override this function to do special
		//		processing, then call this.notifyChildrenOfResize() to notify
		//		children of resize.
		this.notifyChildrenOfResize();
	},
	
	notifyChildrenOfResize: function(){
		// summary: dispatches resized events to all children of this widget
		for(var i=0; i<this.children.length; i++){
			var child = this.children[i];
			//dojo.debug(this.widgetId + " resizing child " + child.widgetId);
			if( child.onResized ){
				child.onResized();
			}
		}
	},

	create: function(args, fragment, parent, ns){
		// summary:
		//		'create' manages the initialization part of the widget
		//		lifecycle. It's called implicitly when any widget is created.
		//		All other initialization functions for widgets, except for the
		//		constructor, are called as a result of 'create' being fired.
		// args: Object
		//		a normalized view of the parameters that the widget should take
		// fragment: Object
		//		if the widget is being instantiated from markup, this object 
		// parent: Widget?
		//		the widget, if any, that this widget will be the child of.  If
		//		none is passed, the global default widget is used.
		// ns: String?
		//		what namespace the widget belongs to
		// description:
		//		to understand the process by which widgets are instantiated, it
		//		is critical to understand what other methods 'create' calls and
		//		which of them you'll want to over-ride. Of course, adventurous
		//		developers could over-ride 'create' entirely, but this should
		//		only be done as a last resort.
		//
		//		Below is a list of the methods that are called, in the order
		//		they are fired, along with notes about what they do and if/when
		//		you should over-ride them in your widget:
		//			
		//			mixInProperties:
		//				takes the args and does lightweight type introspection
		//				on pre-existing object properties to initialize widget
		//				values by casting the values that are passed in args
		//			postMixInProperties:
		//				a stub function that you can over-ride to modify
		//				variables that may have been naively assigned by
		//				mixInProperties
		//			# widget is added to manager object here
		//			buildRendering
		//				subclasses use this method to handle all UI initialization
		//			initialize:
		//				a stub function that you can over-ride.
		//			postInitialize:
		//				a stub function that you can over-ride.
		//			postCreate
		//				a stub function that you can over-ride to modify take
		//				actions once the widget has been placed in the UI
		//
		//		all of these functions are passed the same arguments as are
		//		passed to 'create'

		//dojo.profile.start(this.widgetType + " create");
		if(ns){
			this.ns = ns;
		}
		// dojo.debug(this.widgetType, "create");
		//dojo.profile.start(this.widgetType + " satisfyPropertySets");
		this.satisfyPropertySets(args, fragment, parent);
		//dojo.profile.end(this.widgetType + " satisfyPropertySets");
		// dojo.debug(this.widgetType, "-> mixInProperties");
		//dojo.profile.start(this.widgetType + " mixInProperties");
		this.mixInProperties(args, fragment, parent);
		//dojo.profile.end(this.widgetType + " mixInProperties");
		// dojo.debug(this.widgetType, "-> postMixInProperties");
		//dojo.profile.start(this.widgetType + " postMixInProperties");
		this.postMixInProperties(args, fragment, parent);
		//dojo.profile.end(this.widgetType + " postMixInProperties");
		// dojo.debug(this.widgetType, "-> dojo.widget.manager.add");
		dojo.widget.manager.add(this);
		// dojo.debug(this.widgetType, "-> buildRendering");
		//dojo.profile.start(this.widgetType + " buildRendering");
		this.buildRendering(args, fragment, parent);
		//dojo.profile.end(this.widgetType + " buildRendering");
		// dojo.debug(this.widgetType, "-> initialize");
		//dojo.profile.start(this.widgetType + " initialize");
		this.initialize(args, fragment, parent);
		//dojo.profile.end(this.widgetType + " initialize");
		// dojo.debug(this.widgetType, "-> postInitialize");
		// postinitialize includes subcomponent creation
		// profile is put directly to function
		this.postInitialize(args, fragment, parent);
		// dojo.debug(this.widgetType, "-> postCreate");
		//dojo.profile.start(this.widgetType + " postCreate");
		this.postCreate(args, fragment, parent);
		//dojo.profile.end(this.widgetType + " postCreate");
		// dojo.debug(this.widgetType, "done!");
		
		//dojo.profile.end(this.widgetType + " create");
		
		return this;
	},

	destroy: function(finalize){
		// summary:
		// 		Destroy this widget and it's descendants. This is the generic
		// 		"destructor" function that all widget users should call to
		// 		clealy discard with a widget. Once a widget is destroyed, it's
		// 		removed from the manager object.
		// finalize: Boolean
		//		is this function being called part of global environment
		//		tear-down?

		// FIXME: this is woefully incomplete
		if(this.parent){
			this.parent.removeChild(this);
		}
		this.destroyChildren();
		this.uninitialize();
		this.destroyRendering(finalize);
		dojo.widget.manager.removeById(this.widgetId);
	},

	destroyChildren: function(){
		// summary:
		//		Recursively destroy the children of this widget and their
		//		descendents.
		var widget;
		var i=0;
		while(this.children.length > i){
			widget = this.children[i];
			if (widget instanceof dojo.widget.Widget) { // find first widget
				this.removeChild(widget);
				widget.destroy();
				continue;
			}
			
			i++; // skip data object
		}
				
	},

	getChildrenOfType: function(/*String*/type, recurse){
		// summary: 
		//		return an array of descendant widgets who match the passed type
		// recurse: Boolean
		//		should we try to get all descendants that match? Defaults to
		//		false.
		var ret = [];
		var isFunc = dojo.lang.isFunction(type);
		if(!isFunc){
			type = type.toLowerCase();
		}
		for(var x=0; x<this.children.length; x++){
			if(isFunc){
				if(this.children[x] instanceof type){
					ret.push(this.children[x]);
				}
			}else{
				if(this.children[x].widgetType.toLowerCase() == type){
					ret.push(this.children[x]);
				}
			}
			if(recurse){
				ret = ret.concat(this.children[x].getChildrenOfType(type, recurse));
			}
		}
		return ret; // Array
	},

	getDescendants: function(){
		// returns: a flattened array of all direct descendants including self
		var result = [];
		var stack = [this];
		var elem;
		while ((elem = stack.pop())){
			result.push(elem);
			// a child may be data object without children field set (not widget)
			if (elem.children) {
				dojo.lang.forEach(elem.children, function(elem) { stack.push(elem); });
			}
		}
		return result; // Array
	},


	isFirstChild: function(){
		return this === this.parent.children[0]; // Boolean
	},

	isLastChild: function() {
		return this === this.parent.children[this.parent.children.length-1]; // Boolean
	},

	satisfyPropertySets: function(args){
		// summary: not implemented!

		// dojo.profile.start("satisfyPropertySets");
		// get the default propsets for our component type
		/*
		var typePropSets = []; // FIXME: need to pull these from somewhere!
		var localPropSets = []; // pull out propsets from the parser's return structure

		// for(var x=0; x<args.length; x++){
		// }

		for(var x=0; x<typePropSets.length; x++){
		}

		for(var x=0; x<localPropSets.length; x++){
		}
		*/
		// dojo.profile.end("satisfyPropertySets");
		
		return args;
	},

	mixInProperties: function(args, /*Object*/frag){
		// summary:
		// 		takes the list of properties listed in args and sets values of
		// 		the current object based on existence of properties with the
		// 		same name (case insensitive) and the type of the pre-existing
		// 		property. This is a lightweight conversion and is not intended
		// 		to capture custom type semantics.
		// args: Object
		//		A map of properties and values to set on the current object. By
		//		default it is assumed that properties in args are in string
		//		form and need to be converted. However, if there is a
		//		'fastMixIn' property with the value 'true' in the args param,
		//		this assumption is ignored and all values in args are copied
		//		directly to the current object without any form of type
		//		casting.
		// description:
		//		The mix-in code attempts to do some type-assignment based on
		//		PRE-EXISTING properties of the "this" object. When a named
		//		property of args is located, it is first tested to make
		//		sure that the current object already "has one". Properties
		//		which are undefined in the base widget are NOT settable here.
		//		The next step is to try to determine type of the pre-existing
		//		property. If it's a string, the property value is simply
		//		assigned. If a function, it is first cast using "new
		//		Function()" and the execution scope modified such that it
		//		always evaluates in the context of the current object. This
		//		listener is then added to the original function via
		//		dojo.event.connect(). If an Array, the system attempts to split
		//		the string value on ";" chars, and no further processing is
		//		attempted (conversion of array elements to a integers, for
		//		instance). If the property value is an Object
		//		(testObj.constructor === Object), the property is split first
		//		on ";" chars, secondly on ":" chars, and the resulting
		//		key/value pairs are assigned to an object in a map style. The
		//		onus is on the property user to ensure that all property values
		//		are converted to the expected type before usage. Properties
		//		which do not occur in the "this" object are assigned to the
		//		this.extraArgs map using both the original name and the
		//		lower-case name of the property. This allows for consistent
		//		access semantics regardless of the case preservation of the
		//		source of the property names.
		
		if((args["fastMixIn"])||(frag["fastMixIn"])){
			// dojo.profile.start("mixInProperties_fastMixIn");
			// fast mix in assumes case sensitivity, no type casting, etc...
			// dojo.lang.mixin(this, args);
			for(var x in args){
				this[x] = args[x];
			}
			// dojo.profile.end("mixInProperties_fastMixIn");
			return;
		}
		// dojo.profile.start("mixInProperties");

		var undef;

		// NOTE: we cannot assume that the passed properties are case-correct
		// (esp due to some browser bugs). Therefore, we attempt to locate
		// properties for assignment regardless of case. This may cause
		// problematic assignments and bugs in the future and will need to be
		// documented with big bright neon lights.

		// FIXME: fails miserably if a mixin property has a default value of null in 
		// a widget

		// NOTE: caching lower-cased args in the prototype is only 
		// acceptable if the properties are invariant.
		// if we have a name-cache, get it
		var lcArgs = dojo.widget.lcArgsCache[this.widgetType];
		if ( lcArgs == null ){
			// build a lower-case property name cache if we don't have one
			lcArgs = {};
			for(var y in this){
				lcArgs[((new String(y)).toLowerCase())] = y;
			}
			dojo.widget.lcArgsCache[this.widgetType] = lcArgs;
		}
		var visited = {};
		for(var x in args){
			if(!this[x]){ // check the cache for properties
				var y = lcArgs[(new String(x)).toLowerCase()];
				if(y){
					args[y] = args[x];
					x = y; 
				}
			}
			if(visited[x]){ continue; }
			visited[x] = true;
			if((typeof this[x]) != (typeof undef)){
				if(typeof args[x] != "string"){
					this[x] = args[x];
				}else{
					if(dojo.lang.isString(this[x])){
						this[x] = args[x];
					}else if(dojo.lang.isNumber(this[x])){
						this[x] = new Number(args[x]); // FIXME: what if NaN is the result?
					}else if(dojo.lang.isBoolean(this[x])){
						this[x] = (args[x].toLowerCase()=="false") ? false : true;
					}else if(dojo.lang.isFunction(this[x])){

						// FIXME: need to determine if always over-writing instead
						// of attaching here is appropriate. I suspect that we
						// might want to only allow attaching w/ action items.
						
						// RAR, 1/19/05: I'm going to attach instead of
						// over-write here. Perhaps function objects could have
						// some sort of flag set on them? Or mixed-into objects
						// could have some list of non-mutable properties
						// (although I'm not sure how that would alleviate this
						// particular problem)? 

						// this[x] = new Function(args[x]);

						// after an IRC discussion last week, it was decided
						// that these event handlers should execute in the
						// context of the widget, so that the "this" pointer
						// takes correctly.
						
						// argument that contains no punctuation other than . is 
						// considered a function spec, not code
						if(args[x].search(/[^\w\.]+/i) == -1){
							this[x] = dojo.evalObjPath(args[x], false);
						}else{
							var tn = dojo.lang.nameAnonFunc(new Function(args[x]), this);
							dojo.event.kwConnect({
								srcObj: this, 
								srcFunc: x, 
								adviceObj: this, 
								adviceFunc: tn
							});
						}
					}else if(dojo.lang.isArray(this[x])){ // typeof [] == "object"
						this[x] = args[x].split(";");
					} else if (this[x] instanceof Date) {
						this[x] = new Date(Number(args[x])); // assume timestamp
					}else if(typeof this[x] == "object"){ 
						// FIXME: should we be allowing extension here to handle
						// other object types intelligently?

						// if a plain string is passed to a property of type dojo.uri.Uri,
						// we assume it is relative to root of dojo
						if (this[x] instanceof dojo.uri.Uri){
							this[x] = dojo.uri.dojoUri(args[x]);
						}else{
							// FIXME: unlike all other types, we do not replace the
							// object with a new one here. Should we change that?
							var pairs = args[x].split(";");
							for(var y=0; y<pairs.length; y++){
								var si = pairs[y].indexOf(":");
								if((si != -1)&&(pairs[y].length>si)){
									this[x][pairs[y].substr(0, si).replace(/^\s+|\s+$/g, "")] = pairs[y].substr(si+1);
								}
							}
						}
					}else{
						// the default is straight-up string assignment. When would
						// we ever hit this?
						this[x] = args[x];
					}
				}
			}else{
				// collect any extra 'non mixed in' args
				this.extraArgs[x.toLowerCase()] = args[x];
			}
		}
		// dojo.profile.end("mixInProperties");
	},
	
	postMixInProperties: function(/*Object*/args, /*Object*/frag, /*Widget*/parent){
		// summary
		//	Called after the parameters to the widget have been read-in,
		//	but before the widget template is instantiated.
		//	Especially useful to set properties that are referenced in the widget template.
	},

	initialize: function(/*Object*/args, /*Object*/frag, /*Widget*/parent){
		// summary: stub function.
		return false;
		// dojo.unimplemented("dojo.widget.Widget.initialize");
	},

	postInitialize: function(/*Object*/args, /*Object*/frag, /*Widget*/parent){
		// summary: stub function.
		return false;
	},

	postCreate: function(/*Object*/args, /*Object*/frag, /*Widget*/parent){
		// summary: stub function.
		return false;
	},

	uninitialize: function(){
		// summary: 
		//		stub function. Over-ride to implement custom widget tear-down
		//		behavior.
		return false;
	},

	buildRendering: function(/*Object*/args, /*Object*/frag, /*Widget*/parent){
		// summary: stub function. SUBCLASSES MUST IMPLEMENT
		dojo.unimplemented("dojo.widget.Widget.buildRendering, on "+this.toString()+", ");
		return false;
	},

	destroyRendering: function(){
		// summary: stub function. SUBCLASSES MUST IMPLEMENT
		dojo.unimplemented("dojo.widget.Widget.destroyRendering");
		return false;
	},

	addedTo: function(parent){
		// summary:
		//		stub function this is just a signal that can be caught
		// parent: Widget
		//		instance of dojo.widget.Widget that we were added to
	},

	addChild: function(child){
		// summary: stub function. SUBCLASSES MUST IMPLEMENT
		dojo.unimplemented("dojo.widget.Widget.addChild");
		return false;
	},

	// Detach the given child widget from me, but don't destroy it
	removeChild: function(/*Widget*/widget){
		// summary: 
		//		removes the passed widget instance from this widget but does
		//		not destroy it
		for(var x=0; x<this.children.length; x++){
			if(this.children[x] === widget){
				this.children.splice(x, 1);
				widget.parent=null;
				break;
			}
		}
		return widget; // Widget
	},

	getPreviousSibling: function(){
		// summary:
		//		returns null if this is the first child of the parent,
		//		otherwise returns the next sibling to the "left".
		var idx = this.getParentIndex();
 
		 // first node is idx=0 not found is idx<0
		if (idx<=0) return null;
 
		return this.parent.children[idx-1]; // Widget
	},
 
	getSiblings: function(){
		// summary: gets an array of all children of our parent, including "this"
		return this.parent.children; // Array
	},
 
	getParentIndex: function(){
		// summary: what index are we at in the parent's children array?
		return dojo.lang.indexOf(this.parent.children, this, true); // int
	},
 
	getNextSibling: function(){
		// summary:
		//		returns null if this is the last child of the parent,
		//		otherwise returns the next sibling to the "right".
 
		var idx = this.getParentIndex();
 
		if (idx == this.parent.children.length-1){return null;} // last node
		if (idx < 0){return null;} // not found
 
		return this.parent.children[idx+1]; // Widget
	}
});

// Lower case name cache: listing of the lower case elements in each widget.
// We can't store the lcArgs in the widget itself because if B subclasses A,
// then B.prototype.lcArgs might return A.prototype.lcArgs, which is not what we
// want
dojo.widget.lcArgsCache = {};

// TODO: should have a more general way to add tags or tag libraries?
// TODO: need a default tags class to inherit from for things like getting propertySets
// TODO: parse properties/propertySets into component attributes
// TODO: parse subcomponents
// TODO: copy/clone raw markup fragments/nodes as appropriate
dojo.widget.tags = {};
dojo.widget.tags.addParseTreeHandler = function(/*String*/type){
	// summary: deprecated!
	dojo.deprecated("addParseTreeHandler", ". ParseTreeHandlers are now reserved for components. Any unfiltered DojoML tag without a ParseTreeHandler is assumed to be a widget", "0.5");
	/*
	var ltype = type.toLowerCase();
	this[ltype] = function(fragment, widgetParser, parentComp, insertionIndex, localProps){
		var _ltype = ltype;
		dojo.profile.start(_ltype);
		var n = dojo.widget.buildWidgetFromParseTree(ltype, fragment, widgetParser, parentComp, insertionIndex, localProps);
		dojo.profile.end(_ltype);
		return n;
	}
	*/
}

//dojo.widget.tags.addParseTreeHandler("dojo:widget");

dojo.widget.tags["dojo:propertyset"] = function(fragment, widgetParser, parentComp){
	// FIXME: Is this needed?
	// FIXME: Not sure that this parses into the structure that I want it to parse into...
	// FIXME: add support for nested propertySets
	var properties = widgetParser.parseProperties(fragment["dojo:propertyset"]);
}

// FIXME: need to add the <dojo:connect />
dojo.widget.tags["dojo:connect"] = function(fragment, widgetParser, parentComp){
	var properties = widgetParser.parseProperties(fragment["dojo:connect"]);
}

// FIXME: if we know the insertion point (to a reasonable location), why then do we:
//	- create a template node
//	- clone the template node
//	- render the clone and set properties
//	- remove the clone from the render tree
//	- place the clone
// this is quite dumb
dojo.widget.buildWidgetFromParseTree = function(/*String*/				type,
												/*Object*/				frag, 
												/*dojo.widget.Parse*/	parser,
												/*Widget, optional*/	parentComp, 
												/*int, optional*/		insertionIndex,
												/*Object*/				localProps){

	// summary: creates a tree of widgets from the data structure produced by the first-pass parser (frag)
	

	var stype = type.split(":");
	stype = (stype.length == 2) ? stype[1] : type;
	
	// FIXME: we don't seem to be doing anything with this!
	// var propertySets = parser.getPropertySets(frag);
	var localProperties = localProps || parser.parseProperties(frag[frag["ns"]+":"+stype]);
	var twidget = dojo.widget.manager.getImplementation(stype,null,null,frag["ns"]);
	if(!twidget){
		throw new Error('cannot find "' + type + '" widget');
	}else if (!twidget.create){
		throw new Error('"' + type + '" widget object has no "create" method and does not appear to implement *Widget');
	}
	localProperties["dojoinsertionindex"] = insertionIndex;
	// FIXME: we lose no less than 5ms in construction!
	var ret = twidget.create(localProperties, frag, parentComp, frag["ns"]);
	// dojo.profile.end("buildWidgetFromParseTree");
	return ret;
}

dojo.widget.defineWidget = function(widgetClass, renderer, superclasses, init, props){
	// summary: Create a widget constructor function (aka widgetClass)
	// widgetClass: String
	//		the location in the object hierarchy to place the new widget class constructor
	// renderer: String
	//		usually "html", determines when this delcaration will be used
	// superclasses: Function||Function[]
	//		can be either a single function or an array of functions to be
	//		mixed in as superclasses. If an array, only the first will be used
	//		to set prototype inheritance.
	// init: Function
	//		an optional constructor function. Will be called after superclasses are mixed in.
	// props: Object
	//		a map of properties and functions to extend the class prototype with

	// This meta-function does parameter juggling for backward compat and overloading
	// if 4th argument is a string, we are using the old syntax
	// old sig: widgetClass, superclasses, props (object), renderer (string), init (function)
	if(dojo.lang.isString(arguments[3])){
		dojo.widget._defineWidget(arguments[0], arguments[3], arguments[1], arguments[4], arguments[2]);
	}else{
		// widgetClass
		var args = [ arguments[0] ], p = 3;
		if(dojo.lang.isString(arguments[1])){
			// renderer, superclass
			args.push(arguments[1], arguments[2]);
		}else{
			// superclass
			args.push('', arguments[1]);
			p = 2;
		}
		if(dojo.lang.isFunction(arguments[p])){
			// init (function), props (object) 
			args.push(arguments[p], arguments[p+1]);
		}else{
			// props (object) 
			args.push(null, arguments[p]);
		}
		dojo.widget._defineWidget.apply(this, args);
	}
}

dojo.widget.defineWidget.renderers = "html|svg|vml";

dojo.widget._defineWidget = function(widgetClass /*string*/, renderer /*string*/, superclasses /*function||array*/, init /*function*/, props /*object*/){
	// FIXME: uncomment next line to test parameter juggling ... remove when confidence improves
	// dojo.debug('(c:)' + widgetClass + '\n\n(r:)' + renderer + '\n\n(i:)' + init + '\n\n(p:)' + props);
	// widgetClass takes the form foo.bar.baz<.renderer>.WidgetName (e.g. foo.bar.baz.WidgetName or foo.bar.baz.html.WidgetName)
	var module = widgetClass.split(".");
	var type = module.pop(); // type <= WidgetName, module <= foo.bar.baz<.renderer>
	var regx = "\\.(" + (renderer ? renderer + '|' : '') + dojo.widget.defineWidget.renderers + ")\\.";
	var r = widgetClass.search(new RegExp(regx));
	module = (r < 0 ? module.join(".") : widgetClass.substr(0, r));

	// deprecated in favor of namespace system, remove for 0.5
	dojo.widget.manager.registerWidgetPackage(module);
	
	var pos = module.indexOf(".");
	var nsName = (pos > -1) ? module.substring(0,pos) : module;

	// FIXME: hrm, this might make things simpler
	//dojo.widget.tags.addParseTreeHandler(nsName+":"+type.toLowerCase());
	
	props=(props)||{};
	props.widgetType = type;
	if((!init)&&(props["classConstructor"])){
		init = props.classConstructor;
		delete props.classConstructor;
	}
	dojo.declare(widgetClass, superclasses, init, props);
}

dojo.provide("dojo.widget.Parse");
dojo.require("dojo.widget.Manager");
dojo.require("dojo.dom");

//
// dojoML parser should be moved out of 'widget', codifying the difference between a 'component'
// and a 'widget'. A 'component' being anything that can be generated from a tag.
//
// a particular dojoML tag would be handled by a registered tagHandler with a hook for a default handler
// if the widget system is loaded, a widget builder would be attach itself as the default handler
// 
// widget tags are no longer registered themselves:
// they are now arbitrarily namespaced, so we cannot register them all, and the non-prefixed portions 
// are no longer guaranteed unique 
// 
// therefore dojo.widget.tags should go with this parser code out of the widget module
//

dojo.widget.Parse = function(/*Object*/fragment){
	this.propertySetsList = [];
	this.fragment = fragment;

	this.createComponents = function(/*Object*/frag, /*Object*/parentComp){
		var comps = [];
		var built = false;
		// if we have items to parse/create at this level, do it!
		try{
			if(frag && frag.tagName && (frag != frag.nodeRef)){
				
				// these are in fact, not ever for widgets per-se anymore, 
				// but for other markup elements (aka components)
				var djTags = dojo.widget.tags;
				
				// we split so that you can declare multiple 
				// non-destructive components from the same ctor node
				var tna = String(frag.tagName).split(";");
				for(var x=0; x<tna.length; x++){
					var ltn = tna[x].replace(/^\s+|\s+$/g, "").toLowerCase();
					// FIXME: unsure what this does
					frag.tagName = ltn;
					var ret;
					if(djTags[ltn]){
						built = true;
						ret = djTags[ltn](frag, this, parentComp, frag.index);
						comps.push(ret);
					}else{
						// we require a namespace prefix, default to dojo:
						if(ltn.indexOf(":") == -1){
							ltn = "dojo:"+ltn;
						}
						// FIXME: handling failure condition correctly?
						// ret = djTags[ltn](frag, this, parentComp, frag.index);
						ret = dojo.widget.buildWidgetFromParseTree(ltn, frag, this, parentComp, frag.index);
						if(ret){
							built = true;
							comps.push(ret);
						}
					}
				}
			}
		}catch(e){
			dojo.debug("dojo.widget.Parse: error:", e);
			// note, commenting out the next line is breaking several widgets for me
			// throw e;
			// IE is such a pain sometimes
		}
		// if there's a sub-frag, build widgets from that too
		if(!built){
			comps = comps.concat(this.createSubComponents(frag, parentComp));
		}
		return comps; // Array
	}

	this.createSubComponents = function(/*Object*/fragment, /*Object*/parentComp){
		// summary: recurses over a raw JavaScript object structure,
		// and calls the corresponding handler for its normalized tagName if it exists

		var frag, comps = [];
		for(var item in fragment){
			frag = fragment[item];
			if(frag && typeof frag == "object"
				&&(frag!=fragment.nodeRef)
				&&(frag!=fragment.tagName)
				&&(!dojo.dom.isNode(frag))){// needed in IE when we have event.connected to the domNode
				comps = comps.concat(this.createComponents(frag, parentComp));
			}
		}
		return comps; // Array
	}

	this.parsePropertySets = function(/*Object*/fragment){
		// summary: checks the top level of a raw JavaScript object
		//	structure for any propertySets.  It stores an array of references to 
		//	propertySets that it finds.
		return [];
		/*
		var propertySets = [];
		for(var item in fragment){
			if((fragment[item]["tagName"] == "dojo:propertyset")){
				propertySets.push(fragment[item]);
			}
		}
		// FIXME: should we store these propertySets somewhere for later retrieval
		this.propertySetsList.push(propertySets);
		return propertySets;
		*/
	}

	this.parseProperties = function(/*Object*/fragment){
		// summary: parseProperties checks a raw JavaScript object structure for
		//	properties, and returns a hash of properties that it finds.
		var properties = {};
		for(var item in fragment){
			// FIXME: need to check for undefined?
			// case: its a tagName or nodeRef
			if((fragment[item] == fragment.tagName)||(fragment[item] == fragment.nodeRef)){
				// do nothing
			}else{
				var frag = fragment[item];
				if(frag.tagName && dojo.widget.tags[frag.tagName.toLowerCase()]){
					// TODO: it isn't a property or property set, it's a fragment, 
					// so do something else
					// FIXME: needs to be a better/stricter check
					// TODO: handle xlink:href for external property sets
				}else if(frag[0] && frag[0].value!="" && frag[0].value!=null){
					try{
						// FIXME: need to allow more than one provider
						if(item.toLowerCase() == "dataprovider"){
							var _this = this;
							this.getDataProvider(_this, frag[0].value);
							properties.dataProvider = this.dataProvider;
						}
						properties[item] = frag[0].value;
						var nestedProperties = this.parseProperties(frag);
						// FIXME: this kind of copying is expensive and inefficient!
						for(var property in nestedProperties){
							properties[property] = nestedProperties[property];
						}
					}catch(e){ dojo.debug(e); }
				}
				switch(item.toLowerCase()){
				case "checked":
				case "disabled":
					if (typeof properties[item] != "boolean"){ 
						properties[item] = true;
					}
					break;
				}
			} 
		}
		return properties; // Object
	}

	this.getDataProvider = function(/*Object*/objRef, /*String*/dataUrl){
		// FIXME: this is currently sync.  To make this async, we made need to move 
		//this step into the widget ctor, so that it is loaded when it is needed 
		// to populate the widget
		dojo.io.bind({
			url: dataUrl,
			load: function(type, evaldObj){
				if(type=="load"){
					objRef.dataProvider = evaldObj;
				}
			},
			mimetype: "text/javascript",
			sync: true
		});
	}

	this.getPropertySetById = function(propertySetId){
		// summary: returns the propertySet that matches the provided id
		for(var x = 0; x < this.propertySetsList.length; x++){
			if(propertySetId == this.propertySetsList[x]["id"][0].value){
				return this.propertySetsList[x];
			}
		}
		return ""; // String
	}
	
	//FIXME: doesn't use the componentType param?
	this.getPropertySetsByType = function(componentType){
		// summary: returns the propertySet(s) that match(es) the
	 	// provided componentClass

		var propertySets = [];
		for(var x=0; x < this.propertySetsList.length; x++){
			var cpl = this.propertySetsList[x];
			var cpcc = cpl.componentClass || cpl.componentType || null; //FIXME: is componentType supposed to be an indirect reference?
			var propertySetId = this.propertySetsList[x]["id"][0].value;
			if(cpcc && (propertySetId == cpcc[0].value)){
				propertySets.push(cpl);
			}
		}
		return propertySets; // Array
	}

	this.getPropertySets = function(/*Object*/fragment){
		// summary: returns the propertySet for a given component fragment

		var ppl = "dojo:propertyproviderlist";
		var propertySets = [];
		var tagname = fragment.tagName;
		if(fragment[ppl]){ 
			var propertyProviderIds = fragment[ppl].value.split(" ");
			// FIXME: should the propertyProviderList attribute contain #
			// 		  syntax for reference to ids or not?
			// FIXME: need a better test to see if this is local or external
			// FIXME: doesn't handle nested propertySets, or propertySets that
			// 		  just contain information about css documents, etc.
			for(var propertySetId in propertyProviderIds){
				if((propertySetId.indexOf("..")==-1)&&(propertySetId.indexOf("://")==-1)){
					// get a reference to a propertySet within the current parsed structure
					var propertySet = this.getPropertySetById(propertySetId);
					if(propertySet != ""){
						propertySets.push(propertySet);
					}
				}else{
					// FIXME: add code to parse and return a propertySet from
					// another document
					// alex: is this even necessaray? Do we care? If so, why?
				}
			}
		}
		// we put the typed ones first so that the parsed ones override when
		// iteration happens.
		return this.getPropertySetsByType(tagname).concat(propertySets); // Array
	}

	this.createComponentFromScript = function(/*Node*/nodeRef, /*String*/componentName, /*Object*/properties, /*String?*/ns){
		// summary:
		// nodeRef: the node to be replaced... in the future, we might want to add 
		// an alternative way to specify an insertion point
		// componentName: the expected dojo widget name, i.e. Button of ContextMenu
		// properties: an object of name value pairs
		// ns: the namespace of the widget.  Defaults to "dojo"

		properties.fastMixIn = true;			
		// FIXME: we pulled it apart and now we put it back together ... 
		var ltn = (ns || "dojo") + ":" + componentName.toLowerCase();
		if(dojo.widget.tags[ltn]){
			return [dojo.widget.tags[ltn](properties, this, null, null, properties)]; // Array
		}
		return [dojo.widget.buildWidgetFromParseTree(ltn, properties, this, null, null, properties)]; // Array
	}
}

dojo.widget._parser_collection = {"dojo": new dojo.widget.Parse() };

dojo.widget.getParser = function(/*String?*/name){
	if(!name){ name = "dojo"; }
	if(!this._parser_collection[name]){
		this._parser_collection[name] = new dojo.widget.Parse();
	}
	return this._parser_collection[name];
}

dojo.widget.createWidget = function(/*String*/name, /*String*/props, /*Node*/refNode, /*String*/position){
	// summary: Creates widget
	// name: The name of the widget to create with optional namespace prefix,
	//	e.g."ns:widget", namespace defaults to "dojo".
	// props: Key-Value pairs of properties of the widget
	// refNode: If the position argument is specified, this node is used as
	//	a reference for inserting this node into a DOM tree; else
	//	the widget becomes the domNode
	// position: The position to insert this widget's node relative to the
	//	refNode argument

	var isNode = false;
	var isNameStr = (typeof name == "string");
	if(isNameStr){
		var pos = name.indexOf(":");
		var ns = (pos > -1) ? name.substring(0,pos) : "dojo";
		if(pos > -1){ name = name.substring(pos+1); }
		var lowerCaseName = name.toLowerCase();
		var namespacedName = ns + ":" + lowerCaseName;
		isNode = (dojo.byId(name) && !dojo.widget.tags[namespacedName]); 
	}

	if((arguments.length == 1) && (isNode || !isNameStr)){
		// we got a DOM node 
		var xp = new dojo.xml.Parse(); 
		// FIXME: we should try to find the parent! 
		var tn = isNode ? dojo.byId(name) : name; 
		return dojo.widget.getParser().createComponents(xp.parseElement(tn, null, true))[0]; 
	}

	function fromScript(placeKeeperNode, name, props, ns){
		props[namespacedName] = { 
			dojotype: [{value: lowerCaseName}],
			nodeRef: placeKeeperNode,
			fastMixIn: true
		};
		props.ns = ns;
		return dojo.widget.getParser().createComponentFromScript(placeKeeperNode, name, props, ns);
	}

	props = props||{};
	var notRef = false;
	var tn = null;
	var h = dojo.render.html.capable;
	if(h){
		tn = document.createElement("span");
	}
	if(!refNode){
		notRef = true;
		refNode = tn;
		if(h){
			dojo.body().appendChild(refNode);
		}
	}else if(position){
		dojo.dom.insertAtPosition(tn, refNode, position);
	}else{ // otherwise don't replace, but build in-place
		tn = refNode;
	}
	var widgetArray = fromScript(tn, name.toLowerCase(), props, ns);
	if(	(!widgetArray)||(!widgetArray[0])||
		(typeof widgetArray[0].widgetType == "undefined") ){
		throw new Error("createWidget: Creation of \"" + name + "\" widget failed.");
	}
	try{
		if(notRef && widgetArray[0].domNode.parentNode){
			widgetArray[0].domNode.parentNode.removeChild(widgetArray[0].domNode);
		}
	}catch(e){
		/* squelch for Safari */
		dojo.debug(e);
	}
	return widgetArray[0]; // Widget
}

dojo.kwCompoundRequire({
	common: [["dojo.uri.Uri", false, false]]
});
dojo.provide("dojo.uri.*");

dojo.provide("dojo.widget.DomWidget");

dojo.require("dojo.event.*");

dojo.require("dojo.dom");
dojo.require("dojo.html.style");


dojo.require("dojo.lang.func");
dojo.require("dojo.lang.extras");

dojo.widget._cssFiles = {};
dojo.widget._cssStrings = {};
dojo.widget._templateCache = {};

dojo.widget.defaultStrings = {
	// summary: a mapping of strings that are used in template variable replacement
	dojoRoot: dojo.hostenv.getBaseScriptUri(),
	dojoWidgetModuleUri: dojo.uri.moduleUri("dojo.widget"),
	baseScriptUri: dojo.hostenv.getBaseScriptUri()
};

dojo.widget.fillFromTemplateCache = function(obj, templatePath, templateString, avoidCache){
	// summary:
	//		static method to build from a template w/ or w/o a real widget in
	//		place
	// obj: DomWidget
	//		an instance of dojo.widget.DomWidget to initialize the template for
	// templatePath: String
	//		the URL to get the template from. dojo.uri.Uri is often passed as well.
	// templateString: String?
	//		a string to use in lieu of fetching the template from a URL
	// avoidCache: Boolean?
	//		should the template system not use whatever is in the cache and
	//		always use the passed templatePath or templateString?

	// dojo.debug("avoidCache:", avoidCache);
	var tpath = templatePath || obj.templatePath;

	var tmplts = dojo.widget._templateCache;
	if(!tpath && !obj["widgetType"]) { // don't have a real template here
		do {
			var dummyName = "__dummyTemplate__" + dojo.widget._templateCache.dummyCount++;
		} while(tmplts[dummyName]);
		obj.widgetType = dummyName;
	}
	var wt = tpath?tpath.toString():obj.widgetType;

	var ts = tmplts[wt];
	if(!ts){
		tmplts[wt] = {"string": null, "node": null};
		if(avoidCache){
			ts = {};
		}else{
			ts = tmplts[wt];
		}
	}

	if((!obj.templateString)&&(!avoidCache)){
		obj.templateString = templateString || ts["string"];
	}
	if(obj.templateString){
		obj.templateString = this._sanitizeTemplateString(obj.templateString);
	}

	if((!obj.templateNode)&&(!avoidCache)){
		obj.templateNode = ts["node"];
	}
	if((!obj.templateNode)&&(!obj.templateString)&&(tpath)){
		// fetch a text fragment and assign it to templateString
		// NOTE: we rely on blocking IO here!
		var tstring = this._sanitizeTemplateString(dojo.hostenv.getText(tpath));

		obj.templateString = tstring;
		if(!avoidCache){
			tmplts[wt]["string"] = tstring;
		}
	}
	if((!ts["string"])&&(!avoidCache)){
		ts.string = obj.templateString;
	}
}

dojo.widget._sanitizeTemplateString = function(/*String*/tString){
	//summary: Strips <?xml ...?> declarations so that external SVG and XML
	//documents can be added to a document without worry. Also, if the string
	//is an HTML document, only the part inside the body tag is returned.
	if(tString){
		tString = tString.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im, "");
		var matches = tString.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
		if(matches){
			tString = matches[1];
		}
	}else{
		tString = "";
	}
	return tString; //String
}

dojo.widget._templateCache.dummyCount = 0;

// Array: list of properties to search for node-to-property mappings
dojo.widget.attachProperties = ["dojoAttachPoint", "id"];

// String: name of the property to use for mapping DOM events to widget functions
dojo.widget.eventAttachProperty = "dojoAttachEvent";

// String: property name of code to evaluate when the widget is constructed
dojo.widget.onBuildProperty = "dojoOnBuild";

// Array:  possible accessibility values to set on widget elements - role or state
dojo.widget.waiNames  = ["waiRole", "waiState"];

dojo.widget.wai = {
	// summary: Contains functions to set accessibility roles and states
	//		onto widget elements
	waiRole: { 	
				// name: String:
				//		information for mapping accessibility role
				name: "waiRole", 
				// namespace: String:
				//		URI of the namespace for the set of roles
				"namespace": "http://www.w3.org/TR/xhtml2", 
				// alias: String:
				//		The alias to assign the namespace
				alias: "x2",
				// prefix: String:
				//		The prefix to assign to the role value
				prefix: "wairole:"
	},
	waiState: { 
				// name: String:
				//		information for mapping accessibility state
				name: "waiState", 
				// namespace: String:
				//		URI of the namespace for the set of states
				"namespace": "http://www.w3.org/2005/07/aaa", 
				// alias: String:
				//		The alias to assign the namespace
				alias: "aaa",
				// prefix: String:
				//		empty string - state value does not require prefix
				prefix: ""
	},
	setAttr: function(/*DomNode*/node, /*String*/ ns, /*String*/ attr, /*String|Boolean*/value){
		// summary: Use appropriate API to set the role or state attribute onto the element.
		// description: In IE use the generic setAttribute() api.  Append a namespace
		//   alias to the attribute name and appropriate prefix to the value. 
		//   Otherwise, use the setAttribueNS api to set the namespaced attribute. Also
		//   add the appropriate prefix to the attribute value.
		if(dojo.render.html.ie){
			node.setAttribute(this[ns].alias+":"+ attr, this[ns].prefix+value);
		}else{
			node.setAttributeNS(this[ns]["namespace"], attr, this[ns].prefix+value);
		}
	},

	getAttr: function(/*DomNode*/ node, /*String*/ ns, /*String|Boolena*/ attr){
		// Summary:  Use the appropriate API to retrieve the role or state value
		// Description: In IE use the generic getAttribute() api.  An alias value 
		// 	was added to the attribute name to simulate a namespace when the attribute
		//  was set.  Otherwise use the getAttributeNS() api to retrieve the state value
		if(dojo.render.html.ie){
			return node.getAttribute(this[ns].alias+":"+attr);
		}else{
			return node.getAttributeNS(this[ns]["namespace"], attr);
		}
	},
	removeAttr: function(/*DomNode*/ node, /*String*/ ns, /*String|Boolena*/ attr){
		// summary:  Use the appropriate API to remove the role or state value
		// description: In IE use the generic removeAttribute() api.  An alias value 
		// 	was added to the attribute name to simulate a namespace when the attribute
		//  was set.  Otherwise use the removeAttributeNS() api to remove the state value
		var success = true; //only IE returns a value
		if(dojo.render.html.ie){
			 success = node.removeAttribute(this[ns].alias+":"+attr);
		}else{
			node.removeAttributeNS(this[ns]["namespace"], attr);
		}
		return success;
	}
};

dojo.widget.attachTemplateNodes = function(rootNode, /*Widget*/ targetObj, events ){
	// summary:
	//		map widget properties and functions to the handlers specified in
	//		the dom node and it's descendants. This function iterates over all
	//		nodes and looks for these properties:
	//			* dojoAttachPoint
	//			* dojoAttachEvent	
	//			* waiRole
	//			* waiState
	//			* any "dojoOn*" proprties passed in the events array
	// rootNode: DomNode
	//		the node to search for properties. All children will be searched.
	// events: Array
	//		a list of properties generated from getDojoEventsFromStr.

	// FIXME: this method is still taking WAAAY too long. We need ways of optimizing:
	//	a.) what we are looking for on each node
	//	b.) the nodes that are subject to interrogation (use xpath instead?)
	//	c.) how expensive event assignment is (less eval(), more connect())
	// var start = new Date();
	var elementNodeType = dojo.dom.ELEMENT_NODE;

	function trim(str){
		return str.replace(/^\s+|\s+$/g, "");
	}

	if(!rootNode){ 
		rootNode = targetObj.domNode;
	}

	if(rootNode.nodeType != elementNodeType){
		return;
	}
	// alert(events.length);

	var nodes = rootNode.all || rootNode.getElementsByTagName("*");
	var _this = targetObj;
	for(var x=-1; x<nodes.length; x++){
		var baseNode = (x == -1) ? rootNode : nodes[x];
		// FIXME: is this going to have capitalization problems?  Could use getAttribute(name, 0); to get attributes case-insensitve
		var attachPoint = [];
		if(!targetObj.widgetsInTemplate || !baseNode.getAttribute('dojoType')){
			for(var y=0; y<this.attachProperties.length; y++){
				var tmpAttachPoint = baseNode.getAttribute(this.attachProperties[y]);
				if(tmpAttachPoint){
					attachPoint = tmpAttachPoint.split(";");
					for(var z=0; z<attachPoint.length; z++){
						if(dojo.lang.isArray(targetObj[attachPoint[z]])){
							targetObj[attachPoint[z]].push(baseNode);
						}else{
							targetObj[attachPoint[z]]=baseNode;
						}
					}
					break;
				}
			}

			var attachEvent = baseNode.getAttribute(this.eventAttachProperty);
			if(attachEvent){
				// NOTE: we want to support attributes that have the form
				// "domEvent: nativeEvent; ..."
				var evts = attachEvent.split(";");
				for(var y=0; y<evts.length; y++){
					if((!evts[y])||(!evts[y].length)){ continue; }
					var thisFunc = null;
					var tevt = trim(evts[y]);
					if(evts[y].indexOf(":") >= 0){
						// oh, if only JS had tuple assignment
						var funcNameArr = tevt.split(":");
						tevt = trim(funcNameArr[0]);
						thisFunc = trim(funcNameArr[1]);
					}
					if(!thisFunc){
						thisFunc = tevt;
					}
	
					var tf = function(){ 
						var ntf = new String(thisFunc);
						return function(evt){
							if(_this[ntf]){
								_this[ntf](dojo.event.browser.fixEvent(evt, this));
							}
						};
					}();
					dojo.event.browser.addListener(baseNode, tevt, tf, false, true);
					// dojo.event.browser.addListener(baseNode, tevt, dojo.lang.hitch(_this, thisFunc));
				}
			}
	
			for(var y=0; y<events.length; y++){
				//alert(events[x]);
				var evtVal = baseNode.getAttribute(events[y]);
				if((evtVal)&&(evtVal.length)){
					var thisFunc = null;
					var domEvt = events[y].substr(4); // clober the "dojo" prefix
					thisFunc = trim(evtVal);
					var funcs = [thisFunc];
					if(thisFunc.indexOf(";")>=0){
						funcs = dojo.lang.map(thisFunc.split(";"), trim);
					}
					for(var z=0; z<funcs.length; z++){
						if(!funcs[z].length){ continue; }
						var tf = function(){ 
							var ntf = new String(funcs[z]);
							return function(evt){
								if(_this[ntf]){
									_this[ntf](dojo.event.browser.fixEvent(evt, this));
								}
							}
						}();
						dojo.event.browser.addListener(baseNode, domEvt, tf, false, true);
						// dojo.event.browser.addListener(baseNode, domEvt, dojo.lang.hitch(_this, funcs[z]));
					}
				}
			}
		}
		// continue;

		// FIXME: we need to put this into some kind of lookup structure
		// instead of direct assignment
		var tmpltPoint = baseNode.getAttribute(this.templateProperty);
		if(tmpltPoint){
			targetObj[tmpltPoint]=baseNode;
		}

		dojo.lang.forEach(dojo.widget.waiNames, function(name){
			var wai = dojo.widget.wai[name];
			var val = baseNode.getAttribute(wai.name);
			if(val){
				if(val.indexOf('-') == -1){ 
					dojo.widget.wai.setAttr(baseNode, wai.name, "role", val);
				}else{
					// this is a state-value pair
					var statePair = val.split('-');
					dojo.widget.wai.setAttr(baseNode, wai.name, statePair[0], statePair[1]);
				}
			}
		}, this);

		var onBuild = baseNode.getAttribute(this.onBuildProperty);
		if(onBuild){
			eval("var node = baseNode; var widget = targetObj; "+onBuild);
		}
	}

}

dojo.widget.getDojoEventsFromStr = function(str){
	// summary:
	//		generates a list of properties with names that match the form
	//		dojoOn*
	// str: String
	//		the template string to search
	
	// var lstr = str.toLowerCase();
	var re = /(dojoOn([a-z]+)(\s?))=/gi;
	var evts = str ? str.match(re)||[] : [];
	var ret = [];
	var lem = {};
	for(var x=0; x<evts.length; x++){
		if(evts[x].length < 1){ continue; }
		var cm = evts[x].replace(/\s/, "");
		cm = (cm.slice(0, cm.length-1));
		if(!lem[cm]){
			lem[cm] = true;
			ret.push(cm);
		}
	}
	return ret; // Array
}

dojo.declare("dojo.widget.DomWidget", 
	dojo.widget.Widget,
	function(){
		// summary:
		//		dojo.widget.DomWidget is the superclass that provides behavior for all
		//		DOM-based renderers, including HtmlWidget and SvgWidget. DomWidget
		//		implements the templating system that most widget authors use to define
		//		the UI for their widgets.
		if((arguments.length>0)&&(typeof arguments[0] == "object")){
			this.create(arguments[0]);
		}
	},
	{							 
		// templateNode: DomNode
		//		a node that represents the widget template. Pre-empts both templateString and templatePath.
		templateNode: null,

		// templateString String:
		//		a string that represents the widget template. Pre-empts the
		//		templatePath. In builds that have their strings "interned", the
		//		templatePath is converted to an inline templateString, thereby
		//		preventing a synchronous network call.
		templateString: null,

		// templateCssString String:
		//		a string that represents the CSS for the widgettemplate.
		//		Pre-empts the templateCssPath. In builds that have their
		//		strings "interned", the templateCssPath is converted to an
		//		inline templateCssString, thereby preventing a synchronous
		//		network call.
		templateCssString: null,

		// preventClobber Boolean:
		//		should the widget not replace the node from which it was
		//		constructed? Widgets that apply behaviors to pre-existing parts
		//		of a page can be implemented easily by setting this to "true".
		//		In these cases, the domNode property will point to the node
		//		which the widget was created from.
		preventClobber: false,

		// domNode DomNode:
		//		this is our visible representation of the widget! Other DOM
		//		Nodes may by assigned to other properties, usually through the
		//		template system's dojoAttachPonit syntax, but the domNode
		//		property is the canonical "top level" node in widget UI.
		domNode: null, 

		// containerNode DomNode:
		//		holds child elements. "containerNode" is generally set via a
		//		dojoAttachPoint assignment and it designates where widgets that
		//		are defined as "children" of the parent will be placed
		//		visually.
		containerNode: null,

		// widgetsInTemplate Boolean:
		//		should we parse the template to find widgets that might be
		//		declared in markup inside it? false by default.
		widgetsInTemplate: false,

		addChild: function(/*Widget*/	widget, overrideContainerNode, pos, ref, insertIndex){
			// summary:
			//		Process the given child widget, inserting it's dom node as
			//		a child of our dom node
			// overrideContainerNode: DomNode?
			//		a non-default container node for the widget
			// pos: String?
			//		can be one of "before", "after", "first", or "last". This
			//		has the same meaning as in dojo.dom.insertAtPosition()
			// ref: DomNode?
			//		a node to place the widget relative to
			// insertIndex: int?
			//		DOM index, same meaning as in dojo.dom.insertAtIndex()
			// returns: the widget that was inserted

			// FIXME: should we support addition at an index in the children arr and
			// order the display accordingly? Right now we always append.
			if(!this.isContainer){ // we aren't allowed to contain other widgets, it seems
				dojo.debug("dojo.widget.DomWidget.addChild() attempted on non-container widget");
				return null;
			}else{
				if(insertIndex == undefined){
					insertIndex = this.children.length;
				}
				this.addWidgetAsDirectChild(widget, overrideContainerNode, pos, ref, insertIndex);
				this.registerChild(widget, insertIndex);
			}
			return widget; // Widget
		},
		
		addWidgetAsDirectChild: function(/*Widget*/	widget, overrideContainerNode, pos, ref, insertIndex){
			// summary:
			//		Process the given child widget, inserting it's dom node as
			//		a child of our dom node
			// overrideContainerNode: DomNode
			//		a non-default container node for the widget
			// pos: String?
			//		can be one of "before", "after", "first", or "last". This
			//		has the same meaning as in dojo.dom.insertAtPosition()
			// ref: DomNode?
			//		a node to place the widget relative to
			// insertIndex: int?
			//		DOM index, same meaning as in dojo.dom.insertAtIndex()
			if((!this.containerNode)&&(!overrideContainerNode)){
				this.containerNode = this.domNode;
			}
			var cn = (overrideContainerNode) ? overrideContainerNode : this.containerNode;
			if(!pos){ pos = "after"; }
			if(!ref){ 
				if(!cn){ cn = dojo.body(); }
				ref = cn.lastChild; 
			}
			if(!insertIndex) { insertIndex = 0; }
			widget.domNode.setAttribute("dojoinsertionindex", insertIndex);

			// insert the child widget domNode directly underneath my domNode, in the
			// specified position (by default, append to end)
			if(!ref){
				cn.appendChild(widget.domNode);
			}else{
				// FIXME: was this meant to be the (ugly hack) way to support insert @ index?
				//dojo.dom[pos](widget.domNode, ref, insertIndex);

				// CAL: this appears to be the intended way to insert a node at a given position...
				if (pos == 'insertAtIndex'){
					// dojo.debug("idx:", insertIndex, "isLast:", ref === cn.lastChild);
					dojo.dom.insertAtIndex(widget.domNode, ref.parentNode, insertIndex);
				}else{
					// dojo.debug("pos:", pos, "isLast:", ref === cn.lastChild);
					if((pos == "after")&&(ref === cn.lastChild)){
						cn.appendChild(widget.domNode);
					}else{
						dojo.dom.insertAtPosition(widget.domNode, cn, pos);
					}
				}
			}
		},

		registerChild: function(widget, insertionIndex){
			// summary: record that given widget descends from me
			// widget: Widget
			//		the widget that is now a child
			// insertionIndex: int
			//		where in the children[] array to place it

			// we need to insert the child at the right point in the parent's 
			// 'children' array, based on the insertionIndex

			widget.dojoInsertionIndex = insertionIndex;

			var idx = -1;
			for(var i=0; i<this.children.length; i++){

				//This appears to fix an out of order issue in the case of mixed
				//markup and programmatically added children.  Previously, if a child
				//existed from markup, and another child was addChild()d without specifying
				//any additional parameters, it would end up first in the list, when in fact
				//it should be after.  I can't see cases where this would break things, but
				//I could see no other obvious solution. -dustin

				if (this.children[i].dojoInsertionIndex <= insertionIndex){
					idx = i;
				}
			}

			this.children.splice(idx+1, 0, widget);

			widget.parent = this;
			widget.addedTo(this, idx+1);
			
			// If this widget was created programatically, then it was erroneously added
			// to dojo.widget.manager.topWidgets.  Fix that here.
			delete dojo.widget.manager.topWidgets[widget.widgetId];
		},

		removeChild: function(/*Widget*/ widget){
			// summary: detach child domNode from parent domNode
			dojo.dom.removeNode(widget.domNode);

			// remove child widget from parent widget 
			return dojo.widget.DomWidget.superclass.removeChild.call(this, widget); // Widget
		},

		getFragNodeRef: function(frag){
			// summary:
			//		returns the source node, if any, that the widget was
			//		declared from
			// frag: Object
			//		an opaque data structure generated by the first-pass parser
			if(!frag){return null;} // null
			if(!frag[this.getNamespacedType()]){
				dojo.raise("Error: no frag for widget type " + this.getNamespacedType() 
					+ ", id " + this.widgetId
					+ " (maybe a widget has set it's type incorrectly)");
			}
			return frag[this.getNamespacedType()]["nodeRef"]; // DomNode
		},
		
		postInitialize: function(/*Object*/ args, /*Object*/ frag, /*Widget*/ parentComp){
			// summary:
			//		Replace the source domNode with the generated dom
			//		structure, and register the widget with its parent.
			//		This is an implementation of the stub function defined in
			//		dojo.widget.Widget.
			
			//dojo.profile.start(this.widgetType + " postInitialize");
			
			var sourceNodeRef = this.getFragNodeRef(frag);
			// Stick my generated dom into the output tree
			//alert(this.widgetId + ": replacing " + sourceNodeRef + " with " + this.domNode.innerHTML);
			if (parentComp && (parentComp.snarfChildDomOutput || !sourceNodeRef)){
				// Add my generated dom as a direct child of my parent widget
				// This is important for generated widgets, and also cases where I am generating an
				// <li> node that can't be inserted back into the original DOM tree
				parentComp.addWidgetAsDirectChild(this, "", "insertAtIndex", "",  args["dojoinsertionindex"], sourceNodeRef);
			} else if (sourceNodeRef){
				// Do in-place replacement of the my source node with my generated dom
				if(this.domNode && (this.domNode !== sourceNodeRef)){
					this._sourceNodeRef = dojo.dom.replaceNode(sourceNodeRef, this.domNode);
				}
			}

			// Register myself with my parent, or with the widget manager if
			// I have no parent
			// TODO: the code below erroneously adds all programatically generated widgets
			// to topWidgets (since we don't know who the parent is until after creation finishes)
			if ( parentComp ) {
				parentComp.registerChild(this, args.dojoinsertionindex);
			} else {
				dojo.widget.manager.topWidgets[this.widgetId]=this;
			}

			if(this.widgetsInTemplate){
				var parser = new dojo.xml.Parse();

				var subContainerNode;
				//TODO: use xpath here?
				var subnodes = this.domNode.getElementsByTagName("*");
				for(var i=0;i<subnodes.length;i++){
					if(subnodes[i].getAttribute('dojoAttachPoint') == 'subContainerWidget'){
						subContainerNode = subnodes[i];
//						break;
					}
					if(subnodes[i].getAttribute('dojoType')){
						subnodes[i].setAttribute('isSubWidget', true);
					}
				}
				if (this.isContainer && !this.containerNode){
					//no containerNode is available, which means a widget is used as a container. find it here and move
					//all dom nodes defined in the main html page as children of this.domNode into the actual container
					//widget's node (at this point, the subwidgets defined in the template file is not parsed yet)
					if(subContainerNode){
						var src = this.getFragNodeRef(frag);
						if (src){
							dojo.dom.moveChildren(src, subContainerNode);
							//do not need to follow children nodes in the main html page, as they
							//will be dealt with in the subContainerWidget
							frag['dojoDontFollow'] = true;
						}
					}else{
						dojo.debug("No subContainerWidget node can be found in template file for widget "+this);
					}
				}

				var templatefrag = parser.parseElement(this.domNode, null, true);
				// createSubComponents not createComponents because frag has already been created
				dojo.widget.getParser().createSubComponents(templatefrag, this);
	
				//find all the sub widgets defined in the template file of this widget
				var subwidgets = [];
				var stack = [this];
				var w;
				while((w = stack.pop())){
					for(var i = 0; i < w.children.length; i++){
						var cwidget = w.children[i];
						if(cwidget._processedSubWidgets || !cwidget.extraArgs['issubwidget']){ continue; }
						subwidgets.push(cwidget);
						if(cwidget.isContainer){
							stack.push(cwidget);
						}
					}
				}
	
				//connect event to this widget/attach dom node
				for(var i = 0; i < subwidgets.length; i++){
					var widget = subwidgets[i];
					if(widget._processedSubWidgets){
						dojo.debug("This should not happen: widget._processedSubWidgets is already true!");
						return;
					}
					widget._processedSubWidgets = true;
					if(widget.extraArgs['dojoattachevent']){
						var evts = widget.extraArgs['dojoattachevent'].split(";");
						for(var j=0; j<evts.length; j++){
							var thisFunc = null;
							var tevt = dojo.string.trim(evts[j]);
							if(tevt.indexOf(":") >= 0){
								// oh, if only JS had tuple assignment
								var funcNameArr = tevt.split(":");
								tevt = dojo.string.trim(funcNameArr[0]);
								thisFunc = dojo.string.trim(funcNameArr[1]);
							}
							if(!thisFunc){
								thisFunc = tevt;
							}
							if(dojo.lang.isFunction(widget[tevt])){
								dojo.event.kwConnect({
									srcObj: widget, 
									srcFunc: tevt, 
									targetObj: this, 
									targetFunc: thisFunc
								});
							}else{
								alert(tevt+" is not a function in widget "+widget);
							}
						}
					}
	
					if(widget.extraArgs['dojoattachpoint']){
						//don't attach widget.domNode here, as we do not know which
						//dom node we should connect to (in checkbox widget case, 
						//it is inputNode). So we make the widget itself available
						this[widget.extraArgs['dojoattachpoint']] = widget;
					}
				}
			}

			//dojo.profile.end(this.widgetType + " postInitialize");

			// Expand my children widgets
			/* dojoDontFollow is important for a very special case
			 * basically if you have a widget that you instantiate from script
			 * and that widget is a container, and it contains a reference to a parent
			 * instance, the parser will start recursively parsing until the browser
			 * complains.  So the solution is to set an initialization property of 
			 * dojoDontFollow: true and then it won't recurse where it shouldn't
			 */
			if(this.isContainer && !frag["dojoDontFollow"]){
				//alert("recurse from " + this.widgetId);
				// build any sub-components with us as the parent
				dojo.widget.getParser().createSubComponents(frag, this);
			}
		},

		// method over-ride
		buildRendering: function(/*Object*/ args, /*Object*/ frag){
			// summary:
			//		Construct the UI for this widget, generally from a
			//		template. This can be over-ridden for custom UI creation to
			//		to side-step the template system.  This is an
			//		implementation of the stub function defined in
			//		dojo.widget.Widget.

			// DOM widgets construct themselves from a template
			var ts = dojo.widget._templateCache[this.widgetType];
			
			// Handle style for this widget here, as even if templatePath
			// is not set, style specified by templateCssString or templateCssPath
			// should be applied. templateCssString has higher priority
			// than templateCssPath
			if(args["templatecsspath"]){
				args["templateCssPath"] = args["templatecsspath"];
			}
			var cpath = args["templateCssPath"] || this.templateCssPath;
			if(cpath && !dojo.widget._cssFiles[cpath.toString()]){
				if((!this.templateCssString)&&(cpath)){
					this.templateCssString = dojo.hostenv.getText(cpath);
					this.templateCssPath = null;
				}
				dojo.widget._cssFiles[cpath.toString()] = true;
			}
		
			if((this["templateCssString"])&&(!dojo.widget._cssStrings[this.templateCssString])){
				dojo.html.insertCssText(this.templateCssString, null, cpath);
				dojo.widget._cssStrings[this.templateCssString] = true;
			}
			if(	
				(!this.preventClobber)&&(
					(this.templatePath)||
					(this.templateNode)||
					(
						(this["templateString"])&&(this.templateString.length) 
					)||
					(
						(typeof ts != "undefined")&&( (ts["string"])||(ts["node"]) )
					)
				)
			){
				// if it looks like we can build the thing from a template, do it!
				this.buildFromTemplate(args, frag);
			}else{
				// otherwise, assign the DOM node that was the source of the widget
				// parsing to be the root node
				this.domNode = this.getFragNodeRef(frag);
			}
			this.fillInTemplate(args, frag); 	// this is where individual widgets
												// will handle population of data
												// from properties, remote data
												// sets, etc.
		},

		buildFromTemplate: function(/*Object*/ args, /*Object*/ frag){
			// summary:
			//		Called by buildRendering, creates the actual UI in a DomWidget.

			// var start = new Date();
			// copy template properties if they're already set in the templates object
			// dojo.debug("buildFromTemplate:", this);
			var avoidCache = false;
			if(args["templatepath"]){
//				avoidCache = true;
				args["templatePath"] = args["templatepath"];
			}
			dojo.widget.fillFromTemplateCache(	this, 
												args["templatePath"], 
												null,
												avoidCache);
			var ts = dojo.widget._templateCache[this.templatePath?this.templatePath.toString():this.widgetType];
			if((ts)&&(!avoidCache)){
				if(!this.templateString.length){
					this.templateString = ts["string"];
				}
				if(!this.templateNode){
					this.templateNode = ts["node"];
				}
			}
			var matches = false;
			var node = null;
			// var tstr = new String(this.templateString); 
			var tstr = this.templateString; 
			// attempt to clone a template node, if there is one
			if((!this.templateNode)&&(this.templateString)){
				matches = this.templateString.match(/\$\{([^\}]+)\}/g);
				if(matches) {
					// if we do property replacement, don't create a templateNode
					// to clone from.
					var hash = this.strings || {};
					// FIXME: should this hash of default replacements be cached in
					// templateString?
					for(var key in dojo.widget.defaultStrings) {
						if(dojo.lang.isUndefined(hash[key])) {
							hash[key] = dojo.widget.defaultStrings[key];
						}
					}
					// FIXME: this is a lot of string munging. Can we make it faster?
					for(var i = 0; i < matches.length; i++) {
						var key = matches[i];
						key = key.substring(2, key.length-1);
						var kval = (key.substring(0, 5) == "this.") ? dojo.lang.getObjPathValue(key.substring(5), this) : hash[key];
						var value;
						if((kval)||(dojo.lang.isString(kval))){
							value = new String((dojo.lang.isFunction(kval)) ? kval.call(this, key, this.templateString) : kval);
							// Safer substitution, see heading "Attribute values" in  
							// http://www.w3.org/TR/REC-html40/appendix/notes.html#h-B.3.2
							while (value.indexOf("\"") > -1) {
								value=value.replace("\"","&quot;");
							}
							tstr = tstr.replace(matches[i], value);
						}
					}
				}else{
					// otherwise, we are required to instantiate a copy of the template
					// string if one is provided.
					
					// FIXME: need to be able to distinguish here what should be done
					// or provide a generic interface across all DOM implementations
					// FIMXE: this breaks if the template has whitespace as its first 
					// characters
					// node = this.createNodesFromText(this.templateString, true);
					// this.templateNode = node[0].cloneNode(true); // we're optimistic here
					this.templateNode = this.createNodesFromText(this.templateString, true)[0];
					if(!avoidCache){
						ts.node = this.templateNode;
					}
				}
			}
			if((!this.templateNode)&&(!matches)){ 
				dojo.debug("DomWidget.buildFromTemplate: could not create template");
				return false;
			}else if(!matches){
				node = this.templateNode.cloneNode(true);
				if(!node){ return false; }
			}else{
				node = this.createNodesFromText(tstr, true)[0];
			}

			// recurse through the node, looking for, and attaching to, our
			// attachment points which should be defined on the template node.

			this.domNode = node;
			// dojo.profile.start("attachTemplateNodes");
			this.attachTemplateNodes();
			// dojo.profile.end("attachTemplateNodes");
		
			// relocate source contents to templated container node
			// this.containerNode must be able to receive children, or exceptions will be thrown
			if (this.isContainer && this.containerNode){
				var src = this.getFragNodeRef(frag);
				if (src){
					dojo.dom.moveChildren(src, this.containerNode);
				}
			}
		},

		attachTemplateNodes: function(baseNode, targetObj){
			// summary: 
			//		hooks up event handlers and property/node linkages. Calls
			//		dojo.widget.attachTemplateNodes to do all the hard work.
			// baseNode: DomNode
			//		defaults to "this.domNode"
			// targetObj: Widget
			//		defaults to "this"
			if(!baseNode){ baseNode = this.domNode; }
			if(!targetObj){ targetObj = this; }
			return dojo.widget.attachTemplateNodes(baseNode, targetObj, 
						dojo.widget.getDojoEventsFromStr(this.templateString));
		},

		fillInTemplate: function(){
			// summary:
			//		stub function! sub-classes may use as a default UI
			//		initializer function. The UI rendering will be available by
			//		the time this is called from buildRendering. If
			//		buildRendering is over-ridden, this function may not be
			//		fired!

			// dojo.unimplemented("dojo.widget.DomWidget.fillInTemplate");
		},
		
		// method over-ride
		destroyRendering: function(){
			// summary: UI destructor.  Destroy the dom nodes associated w/this widget.
			try{
				dojo.dom.destroyNode(this.domNode);
				delete this.domNode;
			}catch(e){ /* squelch! */ }
			if(this._sourceNodeRef){
				try{
					dojo.dom.destroyNode(this._sourceNodeRef);
				}catch(e){ /* squelch! */ }
			}
		},

		createNodesFromText: function(){
			// summary
			//	Attempts to create a set of nodes based on the structure of the passed text.
			//	Implemented in HtmlWidget and SvgWidget.
			dojo.unimplemented("dojo.widget.DomWidget.createNodesFromText");
		}
	}
);

dojo.provide("dojo.html.layout");

dojo.require("dojo.html.common");
dojo.require("dojo.html.style");
dojo.require("dojo.html.display");

dojo.html.sumAncestorProperties = function(/* HTMLElement */node, /* string */prop){
	//	summary
	//	Returns the sum of the passed property on all ancestors of node.
	node = dojo.byId(node);
	if(!node){ return 0; } // FIXME: throw an error?
	
	var retVal = 0;
	while(node){
		if(dojo.html.getComputedStyle(node, 'position') == 'fixed'){
			return 0;
		}
		var val = node[prop];
		if(val){
			retVal += val - 0;
			if(node==dojo.body()){ break; }// opera and khtml #body & #html has the same values, we only need one value
		}
		node = node.parentNode;
	}
	return retVal;	//	integer
}

dojo.html.setStyleAttributes = function(/* HTMLElement */node, /* string */attributes) { 
	//	summary
	//	allows a dev to pass a string similar to what you'd pass in style="", and apply it to a node.
	node = dojo.byId(node);
	var splittedAttribs=attributes.replace(/(;)?\s*$/, "").split(";"); 
	for(var i=0; i<splittedAttribs.length; i++){ 
		var nameValue=splittedAttribs[i].split(":"); 
		var name=nameValue[0].replace(/\s*$/, "").replace(/^\s*/, "").toLowerCase();
		var value=nameValue[1].replace(/\s*$/, "").replace(/^\s*/, "");
		switch(name){
			case "opacity":
				dojo.html.setOpacity(node, value); 
				break; 
			case "content-height":
				dojo.html.setContentBox(node, {height: value}); 
				break; 
			case "content-width":
				dojo.html.setContentBox(node, {width: value}); 
				break; 
			case "outer-height":
				dojo.html.setMarginBox(node, {height: value}); 
				break; 
			case "outer-width":
				dojo.html.setMarginBox(node, {width: value}); 
				break; 
			default:
				node.style[dojo.html.toCamelCase(name)]=value; 
		}
	} 
}

dojo.html.boxSizing = {
	MARGIN_BOX: "margin-box",
	BORDER_BOX: "border-box",
	PADDING_BOX: "padding-box",
	CONTENT_BOX: "content-box"
};

dojo.html.getAbsolutePosition = dojo.html.abs = function(/* HTMLElement */node, /* boolean? */includeScroll, /* string? */boxType){
	//	summary
	//	Gets the absolute position of the passed element based on the document itself.
	node = dojo.byId(node, node.ownerDocument);
	var ret = {
		x: 0,
		y: 0
	};

	var bs = dojo.html.boxSizing;
	if(!boxType) { boxType = bs.CONTENT_BOX; }
	var nativeBoxType = 2; //BORDER box
	var targetBoxType;
	switch(boxType){
		case bs.MARGIN_BOX:
			targetBoxType = 3;
			break;
		case bs.BORDER_BOX:
			targetBoxType = 2;
			break;
		case bs.PADDING_BOX:
		default:
			targetBoxType = 1;
			break;
		case bs.CONTENT_BOX:
			targetBoxType = 0;
			break;
	}

	var h = dojo.render.html;
	var db = document["body"]||document["documentElement"];

	if(h.ie){
		with(node.getBoundingClientRect()){
			ret.x = left-2;
			ret.y = top-2;
		}
	}else if(document.getBoxObjectFor){
		// mozilla
		nativeBoxType = 1; //getBoxObjectFor return padding box coordinate
		try{
			var bo = document.getBoxObjectFor(node);
			ret.x = bo.x - dojo.html.sumAncestorProperties(node, "scrollLeft");
			ret.y = bo.y - dojo.html.sumAncestorProperties(node, "scrollTop");
		}catch(e){
			// squelch
		}
	}else{
		if(node["offsetParent"]){
			var endNode;
			// in Safari, if the node is an absolutely positioned child of
			// the body and the body has a margin the offset of the child
			// and the body contain the body's margins, so we need to end
			// at the body
			if(	(h.safari)&&
				(node.style.getPropertyValue("position") == "absolute")&&
				(node.parentNode == db)){
				endNode = db;
			}else{
				endNode = db.parentNode;
			}

			//TODO: set correct nativeBoxType for safari/konqueror

			if(node.parentNode != db){
				var nd = node;
				if(dojo.render.html.opera){ nd = db; }
				ret.x -= dojo.html.sumAncestorProperties(nd, "scrollLeft");
				ret.y -= dojo.html.sumAncestorProperties(nd, "scrollTop");
			}
			var curnode = node;
			do{
				var n = curnode["offsetLeft"];
				//FIXME: ugly hack to workaround the submenu in 
				//popupmenu2 does not shown up correctly in opera. 
				//Someone have a better workaround?
				if(!h.opera || n>0){
					ret.x += isNaN(n) ? 0 : n;
				}
				var m = curnode["offsetTop"];
				ret.y += isNaN(m) ? 0 : m;
				curnode = curnode.offsetParent;
			}while((curnode != endNode)&&(curnode != null));
		}else if(node["x"]&&node["y"]){
			ret.x += isNaN(node.x) ? 0 : node.x;
			ret.y += isNaN(node.y) ? 0 : node.y;
		}
	}

	// account for document scrolling!
	if(includeScroll){
		var scroll = dojo.html.getScroll();
		ret.y += scroll.top;
		ret.x += scroll.left;
	}

	var extentFuncArray=[dojo.html.getPaddingExtent, dojo.html.getBorderExtent, dojo.html.getMarginExtent];
	if(nativeBoxType > targetBoxType){
		for(var i=targetBoxType;i<nativeBoxType;++i){
			ret.y += extentFuncArray[i](node, 'top');
			ret.x += extentFuncArray[i](node, 'left');
		}
	}else if(nativeBoxType < targetBoxType){
		for(var i=targetBoxType;i>nativeBoxType;--i){
			ret.y -= extentFuncArray[i-1](node, 'top');
			ret.x -= extentFuncArray[i-1](node, 'left');
		}
	}
	ret.top = ret.y;
	ret.left = ret.x;
	return ret;	//	object
}

dojo.html.isPositionAbsolute = function(/* HTMLElement */node){
	//	summary
	//	Returns true if the element is absolutely positioned.
	return (dojo.html.getComputedStyle(node, 'position') == 'absolute');	//	boolean
}

dojo.html._sumPixelValues = function(/* HTMLElement */node, selectors, autoIsZero){
	var total = 0;
	for(var x=0; x<selectors.length; x++){
		total += dojo.html.getPixelValue(node, selectors[x], autoIsZero);
	}
	return total;
}

dojo.html.getMargin = function(/* HTMLElement */node){
	//	summary
	//	Returns the width and height of the passed node's margin
	return {
		width: dojo.html._sumPixelValues(node, ["margin-left", "margin-right"], (dojo.html.getComputedStyle(node, 'position') == 'absolute')),
		height: dojo.html._sumPixelValues(node, ["margin-top", "margin-bottom"], (dojo.html.getComputedStyle(node, 'position') == 'absolute'))
	};	//	object
}

dojo.html.getBorder = function(/* HTMLElement */node){
	//	summary
	//	Returns the width and height of the passed node's border
	return {
		width: dojo.html.getBorderExtent(node, 'left') + dojo.html.getBorderExtent(node, 'right'),
		height: dojo.html.getBorderExtent(node, 'top') + dojo.html.getBorderExtent(node, 'bottom')
	};	//	object
}

dojo.html.getBorderExtent = function(/* HTMLElement */node, /* string */side){
	//	summary
	//	returns the width of the requested border
	return (dojo.html.getStyle(node, 'border-' + side + '-style') == 'none' ? 0 : dojo.html.getPixelValue(node, 'border-' + side + '-width'));	// integer
}

dojo.html.getMarginExtent = function(/* HTMLElement */node, /* string */side){
	//	summary
	//	returns the width of the requested margin
	return dojo.html._sumPixelValues(node, ["margin-" + side], dojo.html.isPositionAbsolute(node));	//	integer
}

dojo.html.getPaddingExtent = function(/* HTMLElement */node, /* string */side){
	//	summary
	//	Returns the width of the requested padding 
	return dojo.html._sumPixelValues(node, ["padding-" + side], true);	//	integer
}

dojo.html.getPadding = function(/* HTMLElement */node){
	//	summary
	//	Returns the width and height of the passed node's padding
	return {
		width: dojo.html._sumPixelValues(node, ["padding-left", "padding-right"], true),
		height: dojo.html._sumPixelValues(node, ["padding-top", "padding-bottom"], true)
	};	//	object
}

dojo.html.getPadBorder = function(/* HTMLElement */node){
	//	summary
	//	Returns the width and height of the passed node's padding and border
	var pad = dojo.html.getPadding(node);
	var border = dojo.html.getBorder(node);
	return { width: pad.width + border.width, height: pad.height + border.height };	//	object
}

dojo.html.getBoxSizing = function(/* HTMLElement */node){
	//	summary
	//	Returns which box model the passed element is working with
	var h = dojo.render.html;
	var bs = dojo.html.boxSizing;
	if(((h.ie)||(h.opera)) && node.nodeName.toLowerCase() != "img"){ 
		var cm = document["compatMode"];
		if((cm == "BackCompat")||(cm == "QuirksMode")){
			return bs.BORDER_BOX; 	//	string
		}else{
			return bs.CONTENT_BOX; 	//	string
		}
	}else{
		if(arguments.length == 0){ node = document.documentElement; }
		var sizing;
		if(!h.ie){
			sizing = dojo.html.getStyle(node, "-moz-box-sizing");
			if(!sizing){ 
				sizing = dojo.html.getStyle(node, "box-sizing");
			}
		}
		return (sizing ? sizing : bs.CONTENT_BOX);	//	string
	}
}

dojo.html.isBorderBox = function(/* HTMLElement */node){
	//	summary
	//	returns whether the passed element is using border box sizing or not.
	return (dojo.html.getBoxSizing(node) == dojo.html.boxSizing.BORDER_BOX);	//	boolean
}

dojo.html.getBorderBox = function(/* HTMLElement */node){
	//	summary
	//	Returns the dimensions of the passed element based on border-box sizing.
	node = dojo.byId(node);
	return { width: node.offsetWidth, height: node.offsetHeight };	//	object
}

dojo.html.getPaddingBox = function(/* HTMLElement */node){
	//	summary
	//	Returns the dimensions of the padding box (see http://www.w3.org/TR/CSS21/box.html)
	var box = dojo.html.getBorderBox(node);
	var border = dojo.html.getBorder(node);
	return {
		width: box.width - border.width,
		height:box.height - border.height
	};	//	object
}

dojo.html.getContentBox = function(/* HTMLElement */node){
	//	summary
	//	Returns the dimensions of the content box (see http://www.w3.org/TR/CSS21/box.html)
	node = dojo.byId(node);
	var padborder = dojo.html.getPadBorder(node);
	return {
		width: node.offsetWidth - padborder.width,
		height: node.offsetHeight - padborder.height
	};	//	object
}

dojo.html.setContentBox = function(/* HTMLElement */node, /* object */args){
	//	summary
	//	Sets the dimensions of the passed node according to content sizing.
	node = dojo.byId(node);
	var width = 0; var height = 0;
	var isbb = dojo.html.isBorderBox(node);
	var padborder = (isbb ? dojo.html.getPadBorder(node) : { width: 0, height: 0});
	var ret = {};
	if(typeof args.width != "undefined"){
		width = args.width + padborder.width;
		ret.width = dojo.html.setPositivePixelValue(node, "width", width);
	}
	if(typeof args.height != "undefined"){
		height = args.height + padborder.height;
		ret.height = dojo.html.setPositivePixelValue(node, "height", height);
	}
	return ret;	//	object
}

dojo.html.getMarginBox = function(/* HTMLElement */node){
	//	summary
	//	returns the dimensions of the passed node including any margins.
	var borderbox = dojo.html.getBorderBox(node);
	var margin = dojo.html.getMargin(node);
	return { width: borderbox.width + margin.width, height: borderbox.height + margin.height };	//	object
}

dojo.html.setMarginBox = function(/* HTMLElement */node, /* object */args){
	//	summary
	//	Sets the dimensions of the passed node using margin box calcs.
	node = dojo.byId(node);
	var width = 0; var height = 0;
	var isbb = dojo.html.isBorderBox(node);
	var padborder = (!isbb ? dojo.html.getPadBorder(node) : { width: 0, height: 0 });
	var margin = dojo.html.getMargin(node);
	var ret = {};
	if(typeof args.width != "undefined"){
		width = args.width - padborder.width;
		width -= margin.width;
		ret.width = dojo.html.setPositivePixelValue(node, "width", width);
	}
	if(typeof args.height != "undefined"){
		height = args.height - padborder.height;
		height -= margin.height;
		ret.height = dojo.html.setPositivePixelValue(node, "height", height);
	}
	return ret;	//	object
}

dojo.html.getElementBox = function(/* HTMLElement */node, /* string */type){
	//	summary
	//	return dimesions of a node based on the passed box model type.
	var bs = dojo.html.boxSizing;
	switch(type){
		case bs.MARGIN_BOX:
			return dojo.html.getMarginBox(node);	//	object
		case bs.BORDER_BOX:
			return dojo.html.getBorderBox(node);	//	object
		case bs.PADDING_BOX:
			return dojo.html.getPaddingBox(node);	//	object
		case bs.CONTENT_BOX:
		default:
			return dojo.html.getContentBox(node);	//	object
	}
}
// in: coordinate array [x,y,w,h] or dom node
// return: coordinate object
dojo.html.toCoordinateObject = dojo.html.toCoordinateArray = function(/* array */coords, /* boolean? */includeScroll, /* string? */boxtype) {
	//	summary
	//	Converts an array of coordinates into an object of named arguments.
	if(coords instanceof Array || typeof coords == "array"){
		dojo.deprecated("dojo.html.toCoordinateArray", "use dojo.html.toCoordinateObject({left: , top: , width: , height: }) instead", "0.5");
		// coords is already an array (of format [x,y,w,h]), just return it
		while ( coords.length < 4 ) { coords.push(0); }
		while ( coords.length > 4 ) { coords.pop(); }
		var ret = {
			left: coords[0],
			top: coords[1],
			width: coords[2],
			height: coords[3]
		};
	}else if(!coords.nodeType && !(coords instanceof String || typeof coords == "string") &&
			 ('width' in coords || 'height' in coords || 'left' in coords ||
			  'x' in coords || 'top' in coords || 'y' in coords)){
		// coords is a coordinate object or at least part of one
		var ret = {
			left: coords.left||coords.x||0,
			top: coords.top||coords.y||0,
			width: coords.width||0,
			height: coords.height||0
		};
	}else{
		// coords is an dom object (or dom object id); return it's coordinates
		var node = dojo.byId(coords);
		var pos = dojo.html.abs(node, includeScroll, boxtype);
		var marginbox = dojo.html.getMarginBox(node);
		var ret = {
			left: pos.left,
			top: pos.top,
			width: marginbox.width,
			height: marginbox.height
		};
	}
	ret.x = ret.left;
	ret.y = ret.top;
	return ret;	//	object
}

dojo.html.setMarginBoxWidth = dojo.html.setOuterWidth = function(node, width){
	return dojo.html._callDeprecated("setMarginBoxWidth", "setMarginBox", arguments, "width");
}
dojo.html.setMarginBoxHeight = dojo.html.setOuterHeight = function(){
	return dojo.html._callDeprecated("setMarginBoxHeight", "setMarginBox", arguments, "height");
}
dojo.html.getMarginBoxWidth = dojo.html.getOuterWidth = function(){
	return dojo.html._callDeprecated("getMarginBoxWidth", "getMarginBox", arguments, null, "width");
}
dojo.html.getMarginBoxHeight = dojo.html.getOuterHeight = function(){
	return dojo.html._callDeprecated("getMarginBoxHeight", "getMarginBox", arguments, null, "height");
}
dojo.html.getTotalOffset = function(node, type, includeScroll){
	return dojo.html._callDeprecated("getTotalOffset", "getAbsolutePosition", arguments, null, type);
}
dojo.html.getAbsoluteX = function(node, includeScroll){
	return dojo.html._callDeprecated("getAbsoluteX", "getAbsolutePosition", arguments, null, "x");
}
dojo.html.getAbsoluteY = function(node, includeScroll){
	return dojo.html._callDeprecated("getAbsoluteY", "getAbsolutePosition", arguments, null, "y");
}
dojo.html.totalOffsetLeft = function(node, includeScroll){
	return dojo.html._callDeprecated("totalOffsetLeft", "getAbsolutePosition", arguments, null, "left");
}
dojo.html.totalOffsetTop = function(node, includeScroll){
	return dojo.html._callDeprecated("totalOffsetTop", "getAbsolutePosition", arguments, null, "top");
}
dojo.html.getMarginWidth = function(node){
	return dojo.html._callDeprecated("getMarginWidth", "getMargin", arguments, null, "width");
}
dojo.html.getMarginHeight = function(node){
	return dojo.html._callDeprecated("getMarginHeight", "getMargin", arguments, null, "height");
}
dojo.html.getBorderWidth = function(node){
	return dojo.html._callDeprecated("getBorderWidth", "getBorder", arguments, null, "width");
}
dojo.html.getBorderHeight = function(node){
	return dojo.html._callDeprecated("getBorderHeight", "getBorder", arguments, null, "height");
}
dojo.html.getPaddingWidth = function(node){
	return dojo.html._callDeprecated("getPaddingWidth", "getPadding", arguments, null, "width");
}
dojo.html.getPaddingHeight = function(node){
	return dojo.html._callDeprecated("getPaddingHeight", "getPadding", arguments, null, "height");
}
dojo.html.getPadBorderWidth = function(node){
	return dojo.html._callDeprecated("getPadBorderWidth", "getPadBorder", arguments, null, "width");
}
dojo.html.getPadBorderHeight = function(node){
	return dojo.html._callDeprecated("getPadBorderHeight", "getPadBorder", arguments, null, "height");
}
dojo.html.getBorderBoxWidth = dojo.html.getInnerWidth = function(){
	return dojo.html._callDeprecated("getBorderBoxWidth", "getBorderBox", arguments, null, "width");
}
dojo.html.getBorderBoxHeight = dojo.html.getInnerHeight = function(){
	return dojo.html._callDeprecated("getBorderBoxHeight", "getBorderBox", arguments, null, "height");
}
dojo.html.getContentBoxWidth = dojo.html.getContentWidth = function(){
	return dojo.html._callDeprecated("getContentBoxWidth", "getContentBox", arguments, null, "width");
}
dojo.html.getContentBoxHeight = dojo.html.getContentHeight = function(){
	return dojo.html._callDeprecated("getContentBoxHeight", "getContentBox", arguments, null, "height");
}
dojo.html.setContentBoxWidth = dojo.html.setContentWidth = function(node, width){
	return dojo.html._callDeprecated("setContentBoxWidth", "setContentBox", arguments, "width");
}
dojo.html.setContentBoxHeight = dojo.html.setContentHeight = function(node, height){
	return dojo.html._callDeprecated("setContentBoxHeight", "setContentBox", arguments, "height");
}

dojo.provide("dojo.html.util");


dojo.html.getElementWindow = function(/* HTMLElement */element){
	//	summary
	// 	Get the window object where the element is placed in.
	return dojo.html.getDocumentWindow( element.ownerDocument );	//	Window
}

dojo.html.getDocumentWindow = function(doc){
	//	summary
	// 	Get window object associated with document doc

	// With Safari, there is not wa to retrieve the window from the document, so we must fix it.
	if(dojo.render.html.safari && !doc._parentWindow){
		/*
			This is a Safari specific function that fix the reference to the parent
			window from the document object.
		*/

		var fix=function(win){
			win.document._parentWindow=win;
			for(var i=0; i<win.frames.length; i++){
				fix(win.frames[i]);
			}
		}
		fix(window.top);
	}

	//In some IE versions (at least 6.0), document.parentWindow does not return a
	//reference to the real window object (maybe a copy), so we must fix it as well
	//We use IE specific execScript to attach the real window reference to
	//document._parentWindow for later use
	if(dojo.render.html.ie && window !== document.parentWindow && !doc._parentWindow){
		/*
		In IE 6, only the variable "window" can be used to connect events (others
		may be only copies).
		*/
		doc.parentWindow.execScript("document._parentWindow = window;", "Javascript");
		//to prevent memory leak, unset it after use
		//another possibility is to add an onUnload handler which seems overkill to me (liucougar)
		var win = doc._parentWindow;
		doc._parentWindow = null;
		return win;	//	Window
	}

	return doc._parentWindow || doc.parentWindow || doc.defaultView;	//	Window
}

dojo.html.gravity = function(/* HTMLElement */node, /* DOMEvent */e){
	//	summary
	//	Calculates the mouse's direction of gravity relative to the centre
	//	of the given node.
	//	<p>
	//	If you wanted to insert a node into a DOM tree based on the mouse
	//	position you might use the following code:
	//	<pre>
	//	if (gravity(node, e) & gravity.NORTH) { [insert before]; }
	//	else { [insert after]; }
	//	</pre>
	//
	//	@param node The node
	//	@param e		The event containing the mouse coordinates
	//	@return		 The directions, NORTH or SOUTH and EAST or WEST. These
	//						 are properties of the function.
	node = dojo.byId(node);
	var mouse = dojo.html.getCursorPosition(e);

	with (dojo.html) {
		var absolute = getAbsolutePosition(node, true);
		var bb = getBorderBox(node);
		var nodecenterx = absolute.x + (bb.width / 2);
		var nodecentery = absolute.y + (bb.height / 2);
	}

	with (dojo.html.gravity) {
		return ((mouse.x < nodecenterx ? WEST : EAST) | (mouse.y < nodecentery ? NORTH : SOUTH));	//	integer
	}
}

dojo.html.gravity.NORTH = 1;
dojo.html.gravity.SOUTH = 1 << 1;
dojo.html.gravity.EAST = 1 << 2;
dojo.html.gravity.WEST = 1 << 3;

dojo.html.overElement = function(/* HTMLElement */element, /* DOMEvent */e){
	//	summary
	//	Returns whether the mouse is over the passed element.
	//	Element must be display:block (ie, not a <span>)
	element = dojo.byId(element);
	var mouse = dojo.html.getCursorPosition(e);
	var bb = dojo.html.getBorderBox(element);
	var absolute = dojo.html.getAbsolutePosition(element, true, dojo.html.boxSizing.BORDER_BOX);
	var top = absolute.y;
	var bottom = top + bb.height;
	var left = absolute.x;
	var right = left + bb.width;

	return (mouse.x >= left
		&& mouse.x <= right
		&& mouse.y >= top
		&& mouse.y <= bottom
	);	//	boolean
}

dojo.html.renderedTextContent = function(/* HTMLElement */node){
	//	summary
	//	Attempts to return the text as it would be rendered, with the line breaks
	//	sorted out nicely. Unfinished.
	node = dojo.byId(node);
	var result = "";
	if (node == null) { return result; }
	for (var i = 0; i < node.childNodes.length; i++) {
		switch (node.childNodes[i].nodeType) {
			case 1: // ELEMENT_NODE
			case 5: // ENTITY_REFERENCE_NODE
				var display = "unknown";
				try {
					display = dojo.html.getStyle(node.childNodes[i], "display");
				} catch(E) {}
				switch (display) {
					case "block": case "list-item": case "run-in":
					case "table": case "table-row-group": case "table-header-group":
					case "table-footer-group": case "table-row": case "table-column-group":
					case "table-column": case "table-cell": case "table-caption":
						// TODO: this shouldn't insert double spaces on aligning blocks
						result += "\n";
						result += dojo.html.renderedTextContent(node.childNodes[i]);
						result += "\n";
						break;

					case "none": break;

					default:
						if(node.childNodes[i].tagName && node.childNodes[i].tagName.toLowerCase() == "br") {
							result += "\n";
						} else {
							result += dojo.html.renderedTextContent(node.childNodes[i]);
						}
						break;
				}
				break;
			case 3: // TEXT_NODE
			case 2: // ATTRIBUTE_NODE
			case 4: // CDATA_SECTION_NODE
				var text = node.childNodes[i].nodeValue;
				var textTransform = "unknown";
				try {
					textTransform = dojo.html.getStyle(node, "text-transform");
				} catch(E) {}
				switch (textTransform){
					case "capitalize":
						var words = text.split(' ');
						for(var i=0; i<words.length; i++){
							words[i] = words[i].charAt(0).toUpperCase() + words[i].substring(1);
						}
						text = words.join(" ");
						break;
					case "uppercase": text = text.toUpperCase(); break;
					case "lowercase": text = text.toLowerCase(); break;
					default: break; // leave as is
				}
				// TODO: implement
				switch (textTransform){
					case "nowrap": break;
					case "pre-wrap": break;
					case "pre-line": break;
					case "pre": break; // leave as is
					default:
						// remove whitespace and collapse first space
						text = text.replace(/\s+/, " ");
						if (/\s$/.test(result)) { text.replace(/^\s/, ""); }
						break;
				}
				result += text;
				break;
			default:
				break;
		}
	}
	return result;	//	string
}

dojo.html.createNodesFromText = function(/* string */txt, /* boolean? */trim){
	//	summary
	//	Attempts to create a set of nodes based on the structure of the passed text.
	if(trim) { txt = txt.replace(/^\s+|\s+$/g, ""); }

	var tn = dojo.doc().createElement("div");
	// tn.style.display = "none";
	tn.style.visibility= "hidden";
	dojo.body().appendChild(tn);
	var tableType = "none";
	if((/^<t[dh][\s\r\n>]/i).test(txt.replace(/^\s+/))) {
		txt = "<table><tbody><tr>" + txt + "</tr></tbody></table>";
		tableType = "cell";
	} else if((/^<tr[\s\r\n>]/i).test(txt.replace(/^\s+/))) {
		txt = "<table><tbody>" + txt + "</tbody></table>";
		tableType = "row";
	} else if((/^<(thead|tbody|tfoot)[\s\r\n>]/i).test(txt.replace(/^\s+/))) {
		txt = "<table>" + txt + "</table>";
		tableType = "section";
	}
	tn.innerHTML = txt;
	if(tn["normalize"]){
		tn.normalize();
	}

	var parent = null;
	switch(tableType) {
		case "cell":
			parent = tn.getElementsByTagName("tr")[0];
			break;
		case "row":
			parent = tn.getElementsByTagName("tbody")[0];
			break;
		case "section":
			parent = tn.getElementsByTagName("table")[0];
			break;
		default:
			parent = tn;
			break;
	}

	/* this doesn't make much sense, I'm assuming it just meant trim() so wrap was replaced with trim
	if(wrap){
		var ret = [];
		// start hack
		var fc = tn.firstChild;
		ret[0] = ((fc.nodeValue == " ")||(fc.nodeValue == "\t")) ? fc.nextSibling : fc;
		// end hack
		// tn.style.display = "none";
		dojo.body().removeChild(tn);
		return ret;
	}
	*/
	var nodes = [];
	for(var x=0; x<parent.childNodes.length; x++){
		nodes.push(parent.childNodes[x].cloneNode(true));
	}
	tn.style.display = "none"; // FIXME: why do we do this?
	dojo.html.destroyNode(tn);
	return nodes;	//	array
}

dojo.html.placeOnScreen = function(
	/* HTMLElement */node,
	/* integer */desiredX,
	/* integer */desiredY,
	/* integer */padding,
	/* boolean? */hasScroll,
	/* string? */corners,
	/* boolean? */tryOnly
){
	//	summary
	//	Keeps 'node' in the visible area of the screen while trying to
	//	place closest to desiredX, desiredY. The input coordinates are
	//	expected to be the desired screen position, not accounting for
	//	scrolling. If you already accounted for scrolling, set 'hasScroll'
	//	to true. Set padding to either a number or array for [paddingX, paddingY]
	//	to put some buffer around the element you want to position.
	//	Set which corner(s) you want to bind to, such as
	//
	//	placeOnScreen(node, desiredX, desiredY, padding, hasScroll, "TR")
	//	placeOnScreen(node, [desiredX, desiredY], padding, hasScroll, ["TR", "BL"])
	//
	//	The desiredX/desiredY will be treated as the topleft(TL)/topright(TR) or
	//	BottomLeft(BL)/BottomRight(BR) corner of the node. Each corner is tested
	//	and if a perfect match is found, it will be used. Otherwise, it goes through
	//	all of the specified corners, and choose the most appropriate one.
	//	By default, corner = ['TL'].
	//	If tryOnly is set to true, the node will not be moved to the place.
	//
	//	NOTE: node is assumed to be absolutely or relatively positioned.
	//
	//	Alternate call sig:
	//	 placeOnScreen(node, [x, y], padding, hasScroll)
	//
	//	Examples:
	//	 placeOnScreen(node, 100, 200)
	//	 placeOnScreen("myId", [800, 623], 5)
	//	 placeOnScreen(node, 234, 3284, [2, 5], true)

	// TODO: make this function have variable call sigs
	//	kes(node, ptArray, cornerArray, padding, hasScroll)
	//	kes(node, ptX, ptY, cornerA, cornerB, cornerC, paddingArray, hasScroll)
	if(desiredX instanceof Array || typeof desiredX == "array") {
		tryOnly = corners;
		corners = hasScroll;
		hasScroll = padding;
		padding = desiredY;
		desiredY = desiredX[1];
		desiredX = desiredX[0];
	}

	if(corners instanceof String || typeof corners == "string"){
		corners = corners.split(",");
	}

	if(!isNaN(padding)) {
		padding = [Number(padding), Number(padding)];
	} else if(!(padding instanceof Array || typeof padding == "array")) {
		padding = [0, 0];
	}

	var scroll = dojo.html.getScroll().offset;
	var view = dojo.html.getViewport();

	node = dojo.byId(node);
	var oldDisplay = node.style.display;
	node.style.display="";
	var bb = dojo.html.getBorderBox(node);
	var w = bb.width;
	var h = bb.height;
	node.style.display=oldDisplay;

	if(!(corners instanceof Array || typeof corners == "array")){
		corners = ['TL'];
	}

	var bestx, besty, bestDistance = Infinity, bestCorner;

	for(var cidex=0; cidex<corners.length; ++cidex){
		var corner = corners[cidex];
		var match = true;
		var tryX = desiredX - (corner.charAt(1)=='L' ? 0 : w) + padding[0]*(corner.charAt(1)=='L' ? 1 : -1);
		var tryY = desiredY - (corner.charAt(0)=='T' ? 0 : h) + padding[1]*(corner.charAt(0)=='T' ? 1 : -1);
		if(hasScroll) {
			tryX -= scroll.x;
			tryY -= scroll.y;
		}

		if(tryX < 0){
			tryX = 0;
			match = false;
		}

		if(tryY < 0){
			tryY = 0;
			match = false;
		}

		var x = tryX + w;
		if(x > view.width) {
			x = view.width - w;
			match = false;
		} else {
			x = tryX;
		}
		x = Math.max(padding[0], x) + scroll.x;

		var y = tryY + h;
		if(y > view.height) {
			y = view.height - h;
			match = false;
		} else {
			y = tryY;
		}
		y = Math.max(padding[1], y) + scroll.y;

		if(match){ //perfect match, return now
			bestx = x;
			besty = y;
			bestDistance = 0;
			bestCorner = corner;
			break;
		}else{
			//not perfect, find out whether it is better than the saved one
			var dist = Math.pow(x-tryX-scroll.x,2)+Math.pow(y-tryY-scroll.y,2);
			if(bestDistance > dist){
				bestDistance = dist;
				bestx = x;
				besty = y;
				bestCorner = corner;
			}
		}
	}

	if(!tryOnly){
		node.style.left = bestx + "px";
		node.style.top = besty + "px";
	}

	return { left: bestx, top: besty, x: bestx, y: besty, dist: bestDistance, corner:  bestCorner};	//	object
}

dojo.html.placeOnScreenPoint = function(node, desiredX, desiredY, padding, hasScroll) {
	dojo.deprecated("dojo.html.placeOnScreenPoint", "use dojo.html.placeOnScreen() instead", "0.5");
	return dojo.html.placeOnScreen(node, desiredX, desiredY, padding, hasScroll, ['TL', 'TR', 'BL', 'BR']);
}

dojo.html.placeOnScreenAroundElement = function(
	/* HTMLElement */node,
	/* HTMLElement */aroundNode,
	/* integer */padding,
	/* string? */aroundType,
	/* string? */aroundCorners,
	/* boolean? */tryOnly
){
	//	summary
	//	Like placeOnScreen, except it accepts aroundNode instead of x,y
	//	and attempts to place node around it. aroundType (see
	//	dojo.html.boxSizing in html/layout.js) determines which box of the
	//	aroundNode should be used to calculate the outer box.
	//	aroundCorners specify Which corner of aroundNode should be
	//	used to place the node => which corner(s) of node to use (see the
	//	corners parameter in dojo.html.placeOnScreen)
	//	aroundCorners: {'TL': 'BL', 'BL': 'TL'}

	var best, bestDistance=Infinity;
	aroundNode = dojo.byId(aroundNode);
	var oldDisplay = aroundNode.style.display;
	aroundNode.style.display="";
	var mb = dojo.html.getElementBox(aroundNode, aroundType);
	var aroundNodeW = mb.width;
	var aroundNodeH = mb.height;
	var aroundNodePos = dojo.html.getAbsolutePosition(aroundNode, true, aroundType);
	aroundNode.style.display=oldDisplay;

	for(var nodeCorner in aroundCorners){
		var pos, desiredX, desiredY;
		var corners = aroundCorners[nodeCorner];

		desiredX = aroundNodePos.x + (nodeCorner.charAt(1)=='L' ? 0 : aroundNodeW);
		desiredY = aroundNodePos.y + (nodeCorner.charAt(0)=='T' ? 0 : aroundNodeH);

		pos = dojo.html.placeOnScreen(node, desiredX, desiredY, padding, true, corners, true);
		if(pos.dist == 0){
			best = pos;
			break;
		}else{
			//not perfect, find out whether it is better than the saved one
			if(bestDistance > pos.dist){
				bestDistance = pos.dist;
				best = pos;
			}
		}
	}

	if(!tryOnly){
		node.style.left = best.left + "px";
		node.style.top = best.top + "px";
	}
	return best;	//	object
}

dojo.html.scrollIntoView = function(/* HTMLElement */node){
	//	summary
	//	Scroll the passed node into view, if it is not.
	if(!node){ return; }

	// don't rely on that node.scrollIntoView works just because the function is there
	// it doesnt work in Konqueror or Opera even though the function is there and probably
	// not safari either
	// dont like browser sniffs implementations but sometimes you have to use it
	if(dojo.render.html.ie){
		//only call scrollIntoView if there is a scrollbar for this menu,
		//otherwise, scrollIntoView will scroll the window scrollbar
		if(dojo.html.getBorderBox(node.parentNode).height <= node.parentNode.scrollHeight){
			node.scrollIntoView(false);
		}
	}else if(dojo.render.html.mozilla){
		// IE, mozilla
		node.scrollIntoView(false);
	}else{
		var parent = node.parentNode;
		var parentBottom = parent.scrollTop + dojo.html.getBorderBox(parent).height;
		var nodeBottom = node.offsetTop + dojo.html.getMarginBox(node).height;
		if(parentBottom < nodeBottom){
			parent.scrollTop += (nodeBottom - parentBottom);
		}else if(parent.scrollTop > node.offsetTop){
			parent.scrollTop -= (parent.scrollTop - node.offsetTop);
		}
	}
}

dojo.provide("dojo.gfx.color");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.array");

// TODO: rewrite the "x2y" methods to take advantage of the parsing
//       abilities of the Color object. Also, beef up the Color
//       object (as possible) to parse most common formats

// takes an r, g, b, a(lpha) value, [r, g, b, a] array, "rgb(...)" string, hex string (#aaa, #aaaaaa, aaaaaaa)
dojo.gfx.color.Color = function(r, g, b, a) {
	// dojo.debug("r:", r[0], "g:", r[1], "b:", r[2]);
	if(dojo.lang.isArray(r)){
		this.r = r[0];
		this.g = r[1];
		this.b = r[2];
		this.a = r[3]||1.0;
	}else if(dojo.lang.isString(r)){
		var rgb = dojo.gfx.color.extractRGB(r);
		this.r = rgb[0];
		this.g = rgb[1];
		this.b = rgb[2];
		this.a = g||1.0;
	}else if(r instanceof dojo.gfx.color.Color){
		// why does this create a new instance if we were passed one?
		this.r = r.r;
		this.b = r.b;
		this.g = r.g;
		this.a = r.a;
	}else{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
}

dojo.gfx.color.Color.fromArray = function(arr) {
	return new dojo.gfx.color.Color(arr[0], arr[1], arr[2], arr[3]);
}

dojo.extend(dojo.gfx.color.Color, {
	toRgb: function(includeAlpha) {
		if(includeAlpha) {
			return this.toRgba();
		} else {
			return [this.r, this.g, this.b];
		}
	},
	toRgba: function() {
		return [this.r, this.g, this.b, this.a];
	},
	toHex: function() {
		return dojo.gfx.color.rgb2hex(this.toRgb());
	},
	toCss: function() {
		return "rgb(" + this.toRgb().join() + ")";
	},
	toString: function() {
		return this.toHex(); // decent default?
	},
	blend: function(color, weight){
		var rgb = null;
		if(dojo.lang.isArray(color)){
			rgb = color;
		}else if(color instanceof dojo.gfx.color.Color){
			rgb = color.toRgb();
		}else{
			rgb = new dojo.gfx.color.Color(color).toRgb();
		}
		return dojo.gfx.color.blend(this.toRgb(), rgb, weight);
	}
});

dojo.gfx.color.named = {
	white:      [255,255,255],
	black:      [0,0,0],
	red:        [255,0,0],
	green:	    [0,255,0],
	lime:	    [0,255,0],
	blue:       [0,0,255],
	navy:       [0,0,128],
	gray:       [128,128,128],
	silver:     [192,192,192]
};

dojo.gfx.color.blend = function(a, b, weight){
	// summary: 
	//		blend colors a and b (both as RGB array or hex strings) with weight
	//		from -1 to +1, 0 being a 50/50 blend
	if(typeof a == "string"){
		return dojo.gfx.color.blendHex(a, b, weight);
	}
	if(!weight){
		weight = 0;
	}
	weight = Math.min(Math.max(-1, weight), 1);

	// alex: this interface blows.
	// map -1 to 1 to the range 0 to 1
	weight = ((weight + 1)/2);
	
	var c = [];

	// var stop = (1000*weight);
	for(var x = 0; x < 3; x++){
		c[x] = parseInt( b[x] + ( (a[x] - b[x]) * weight) );
	}
	return c;
}

// very convenient blend that takes and returns hex values
// (will get called automatically by blend when blend gets strings)
dojo.gfx.color.blendHex = function(a, b, weight) {
	return dojo.gfx.color.rgb2hex(dojo.gfx.color.blend(dojo.gfx.color.hex2rgb(a), dojo.gfx.color.hex2rgb(b), weight));
}

// get RGB array from css-style color declarations
dojo.gfx.color.extractRGB = function(color) {
	var hex = "0123456789abcdef";
	color = color.toLowerCase();
	if( color.indexOf("rgb") == 0 ) {
		var matches = color.match(/rgba*\((\d+), *(\d+), *(\d+)/i);
		var ret = matches.splice(1, 3);
		return ret;
	} else {
		var colors = dojo.gfx.color.hex2rgb(color);
		if(colors) {
			return colors;
		} else {
			// named color (how many do we support?)
			return dojo.gfx.color.named[color] || [255, 255, 255];
		}
	}
}

dojo.gfx.color.hex2rgb = function(hex) {
	var hexNum = "0123456789ABCDEF";
	var rgb = new Array(3);
	if( hex.indexOf("#") == 0 ) { hex = hex.substring(1); }
	hex = hex.toUpperCase();
	if(hex.replace(new RegExp("["+hexNum+"]", "g"), "") != "") {
		return null;
	}
	if( hex.length == 3 ) {
		rgb[0] = hex.charAt(0) + hex.charAt(0)
		rgb[1] = hex.charAt(1) + hex.charAt(1)
		rgb[2] = hex.charAt(2) + hex.charAt(2);
	} else {
		rgb[0] = hex.substring(0, 2);
		rgb[1] = hex.substring(2, 4);
		rgb[2] = hex.substring(4);
	}
	for(var i = 0; i < rgb.length; i++) {
		rgb[i] = hexNum.indexOf(rgb[i].charAt(0)) * 16 + hexNum.indexOf(rgb[i].charAt(1));
	}
	return rgb;
}

dojo.gfx.color.rgb2hex = function(r, g, b) {
	if(dojo.lang.isArray(r)) {
		g = r[1] || 0;
		b = r[2] || 0;
		r = r[0] || 0;
	}
	var ret = dojo.lang.map([r, g, b], function(x) {
		x = new Number(x);
		var s = x.toString(16);
		while(s.length < 2) { s = "0" + s; }
		return s;
	});
	ret.unshift("#");
	return ret.join("");
}

dojo.provide("dojo.lfx.Animation");

dojo.require("dojo.lang.func");

/*
	Animation package based on Dan Pupius' work: http://pupius.co.uk/js/Toolkit.Drawing.js
*/
dojo.lfx.Line = function(/*int*/ start, /*int*/ end){
	// summary: dojo.lfx.Line is the object used to generate values
	//			from a start value to an end value
	this.start = start;
	this.end = end;
	if(dojo.lang.isArray(start)){
		/* start: Array
		   end: Array
		   pId: a */
		var diff = [];
		dojo.lang.forEach(this.start, function(s,i){
			diff[i] = this.end[i] - s;
		}, this);
		
		this.getValue = function(/*float*/ n){
			var res = [];
			dojo.lang.forEach(this.start, function(s, i){
				res[i] = (diff[i] * n) + s;
			}, this);
			return res; // Array
		}
	}else{
		var diff = end - start;
			
		this.getValue = function(/*float*/ n){
			//	summary: returns the point on the line
			//	n: a floating point number greater than 0 and less than 1
			return (diff * n) + this.start; // Decimal
		}
	}
}

if((dojo.render.html.khtml)&&(!dojo.render.html.safari)){
	// the cool kids are obviously not using konqueror...
	// found a very wierd bug in floats constants, 1.5 evals as 1
	// seems somebody mixed up ints and floats in 3.5.4 ??
	// FIXME: investigate more and post a KDE bug (Fredrik)
	dojo.lfx.easeDefault = function(/*Decimal?*/ n){
		//	summary: Returns the point for point n on a sin wave.
		return (parseFloat("0.5")+((Math.sin( (n+parseFloat("1.5")) * Math.PI))/2));
	}
}else{
	dojo.lfx.easeDefault = function(/*Decimal?*/ n){
		return (0.5+((Math.sin( (n+1.5) * Math.PI))/2));
	}
}

dojo.lfx.easeIn = function(/*Decimal?*/ n){
	//	summary: returns the point on an easing curve
	//	n: a floating point number greater than 0 and less than 1
	return Math.pow(n, 3);
}

dojo.lfx.easeOut = function(/*Decimal?*/ n){
	//	summary: returns the point on the line
	//	n: a floating point number greater than 0 and less than 1
	return ( 1 - Math.pow(1 - n, 3) );
}

dojo.lfx.easeInOut = function(/*Decimal?*/ n){
	//	summary: returns the point on the line
	//	n: a floating point number greater than 0 and less than 1
	return ( (3 * Math.pow(n, 2)) - (2 * Math.pow(n, 3)) );
}

dojo.lfx.IAnimation = function(){
	// summary: dojo.lfx.IAnimation is an interface that implements
	//			commonly used functions of animation objects
}
dojo.lang.extend(dojo.lfx.IAnimation, {
	// public properties
	curve: null,
	duration: 1000,
	easing: null,
	repeatCount: 0,
	rate: 10,
	
	// events
	handler: null,
	beforeBegin: null,
	onBegin: null,
	onAnimate: null,
	onEnd: null,
	onPlay: null,
	onPause: null,
	onStop: null,
	
	// public methods
	play: null,
	pause: null,
	stop: null,
	
	connect: function(/*Event*/ evt, /*Object*/ scope, /*Function*/ newFunc){
		// summary: Convenience function.  Quickly connect to an event
		//			of this object and save the old functions connected to it.
		// evt: The name of the event to connect to.
		// scope: the scope in which to run newFunc.
		// newFunc: the function to run when evt is fired.
		if(!newFunc){
			/* scope: Function
			   newFunc: null
			   pId: f */
			newFunc = scope;
			scope = this;
		}
		newFunc = dojo.lang.hitch(scope, newFunc);
		var oldFunc = this[evt]||function(){};
		this[evt] = function(){
			var ret = oldFunc.apply(this, arguments);
			newFunc.apply(this, arguments);
			return ret;
		}
		return this; // dojo.lfx.IAnimation
	},

	fire: function(/*Event*/ evt, /*Array*/ args){
		// summary: Convenience function.  Fire event "evt" and pass it
		//			the arguments specified in "args".
		// evt: The event to fire.
		// args: The arguments to pass to the event.
		if(this[evt]){
			this[evt].apply(this, (args||[]));
		}
		return this; // dojo.lfx.IAnimation
	},
	
	repeat: function(/*int*/ count){
		// summary: Set the repeat count of this object.
		// count: How many times to repeat the animation.
		this.repeatCount = count;
		return this; // dojo.lfx.IAnimation
	},

	// private properties
	_active: false,
	_paused: false
});

dojo.lfx.Animation = function(	/*Object*/ handlers, 
								/*int*/ duration, 
								/*dojo.lfx.Line*/ curve, 
								/*function*/ easing, 
								/*int*/ repeatCount, 
								/*int*/ rate){
	//	summary
	//		a generic animation object that fires callbacks into it's handlers
	//		object at various states
	//	handlers: { handler: Function?, onstart: Function?, onstop: Function?, onanimate: Function? }
	dojo.lfx.IAnimation.call(this);
	if(dojo.lang.isNumber(handlers)||(!handlers && duration.getValue)){
		// no handlers argument:
		rate = repeatCount;
		repeatCount = easing;
		easing = curve;
		curve = duration;
		duration = handlers;
		handlers = null;
	}else if(handlers.getValue||dojo.lang.isArray(handlers)){
		// no handlers or duration:
		rate = easing;
		repeatCount = curve;
		easing = duration;
		curve = handlers;
		duration = null;
		handlers = null;
	}
	if(dojo.lang.isArray(curve)){
		/* curve: Array
		   pId: a */
		this.curve = new dojo.lfx.Line(curve[0], curve[1]);
	}else{
		this.curve = curve;
	}
	if(duration != null && duration > 0){ this.duration = duration; }
	if(repeatCount){ this.repeatCount = repeatCount; }
	if(rate){ this.rate = rate; }
	if(handlers){
		dojo.lang.forEach([
				"handler", "beforeBegin", "onBegin", 
				"onEnd", "onPlay", "onStop", "onAnimate"
			], function(item){
				if(handlers[item]){
					this.connect(item, handlers[item]);
				}
			}, this);
	}
	if(easing && dojo.lang.isFunction(easing)){
		this.easing=easing;
	}
}
dojo.inherits(dojo.lfx.Animation, dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Animation, {
	// "private" properties
	_startTime: null,
	_endTime: null,
	_timer: null,
	_percent: 0,
	_startRepeatCount: 0,

	// public methods
	play: function(/*int?*/ delay, /*bool?*/ gotoStart){
		// summary: Start the animation.
		// delay: How many milliseconds to delay before starting.
		// gotoStart: If true, starts the animation from the beginning; otherwise,
		//            starts it from its current position.
		if(gotoStart){
			clearTimeout(this._timer);
			this._active = false;
			this._paused = false;
			this._percent = 0;
		}else if(this._active && !this._paused){
			return this; // dojo.lfx.Animation
		}
		
		this.fire("handler", ["beforeBegin"]);
		this.fire("beforeBegin");

		if(delay > 0){
			setTimeout(dojo.lang.hitch(this, function(){ this.play(null, gotoStart); }), delay);
			return this; // dojo.lfx.Animation
		}
		
		this._startTime = new Date().valueOf();
		if(this._paused){
			this._startTime -= (this.duration * this._percent / 100);
		}
		this._endTime = this._startTime + this.duration;

		this._active = true;
		this._paused = false;
		
		var step = this._percent / 100;
		var value = this.curve.getValue(step);
		if(this._percent == 0 ){
			if(!this._startRepeatCount){
				this._startRepeatCount = this.repeatCount;
			}
			this.fire("handler", ["begin", value]);
			this.fire("onBegin", [value]);
		}

		this.fire("handler", ["play", value]);
		this.fire("onPlay", [value]);

		this._cycle();
		return this; // dojo.lfx.Animation
	},

	pause: function(){
		// summary: Pauses a running animation.
		clearTimeout(this._timer);
		if(!this._active){ return this; /*dojo.lfx.Animation*/}
		this._paused = true;
		var value = this.curve.getValue(this._percent / 100);
		this.fire("handler", ["pause", value]);
		this.fire("onPause", [value]);
		return this; // dojo.lfx.Animation
	},

	gotoPercent: function(/*Decimal*/ pct, /*bool?*/ andPlay){
		// summary: Sets the progress of the animation.
		// pct: A percentage in decimal notation (between and including 0.0 and 1.0).
		// andPlay: If true, play the animation after setting the progress.
		clearTimeout(this._timer);
		this._active = true;
		this._paused = true;
		this._percent = pct;
		if(andPlay){ this.play(); }
		return this; // dojo.lfx.Animation
	},

	stop: function(/*bool?*/ gotoEnd){
		// summary: Stops a running animation.
		// gotoEnd: If true, the animation will end.
		clearTimeout(this._timer);
		var step = this._percent / 100;
		if(gotoEnd){
			step = 1;
		}
		var value = this.curve.getValue(step);
		this.fire("handler", ["stop", value]);
		this.fire("onStop", [value]);
		this._active = false;
		this._paused = false;
		return this; // dojo.lfx.Animation
	},

	status: function(){
		// summary: Returns a string representation of the status of
		//			the animation.
		if(this._active){
			return this._paused ? "paused" : "playing"; // String
		}else{
			return "stopped"; // String
		}
		return this;
	},

	// "private" methods
	_cycle: function(){
		clearTimeout(this._timer);
		if(this._active){
			var curr = new Date().valueOf();
			var step = (curr - this._startTime) / (this._endTime - this._startTime);

			if(step >= 1){
				step = 1;
				this._percent = 100;
			}else{
				this._percent = step * 100;
			}
			
			// Perform easing
			if((this.easing)&&(dojo.lang.isFunction(this.easing))){
				step = this.easing(step);
			}

			var value = this.curve.getValue(step);
			this.fire("handler", ["animate", value]);
			this.fire("onAnimate", [value]);

			if( step < 1 ){
				this._timer = setTimeout(dojo.lang.hitch(this, "_cycle"), this.rate);
			}else{
				this._active = false;
				this.fire("handler", ["end"]);
				this.fire("onEnd");

				if(this.repeatCount > 0){
					this.repeatCount--;
					this.play(null, true);
				}else if(this.repeatCount == -1){
					this.play(null, true);
				}else{
					if(this._startRepeatCount){
						this.repeatCount = this._startRepeatCount;
						this._startRepeatCount = 0;
					}
				}
			}
		}
		return this; // dojo.lfx.Animation
	}
});

dojo.lfx.Combine = function(/*dojo.lfx.IAnimation...*/ animations){
	// summary: An animation object to play animations passed to it at the same time.
	dojo.lfx.IAnimation.call(this);
	this._anims = [];
	this._animsEnded = 0;
	
	var anims = arguments;
	if(anims.length == 1 && (dojo.lang.isArray(anims[0]) || dojo.lang.isArrayLike(anims[0]))){
		/* animations: dojo.lfx.IAnimation[]
		   pId: a */
		anims = anims[0];
	}
	
	dojo.lang.forEach(anims, function(anim){
		this._anims.push(anim);
		anim.connect("onEnd", dojo.lang.hitch(this, "_onAnimsEnded"));
	}, this);
}
dojo.inherits(dojo.lfx.Combine, dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Combine, {
	// private members
	_animsEnded: 0,
	
	// public methods
	play: function(/*int?*/ delay, /*bool?*/ gotoStart){
		// summary: Start the animations.
		// delay: How many milliseconds to delay before starting.
		// gotoStart: If true, starts the animations from the beginning; otherwise,
		//            starts them from their current position.
		if( !this._anims.length ){ return this; /*dojo.lfx.Combine*/}

		this.fire("beforeBegin");

		if(delay > 0){
			setTimeout(dojo.lang.hitch(this, function(){ this.play(null, gotoStart); }), delay);
			return this; // dojo.lfx.Combine
		}
		
		if(gotoStart || this._anims[0].percent == 0){
			this.fire("onBegin");
		}
		this.fire("onPlay");
		this._animsCall("play", null, gotoStart);
		return this; // dojo.lfx.Combine
	},
	
	pause: function(){
		// summary: Pauses the running animations.
		this.fire("onPause");
		this._animsCall("pause"); 
		return this; // dojo.lfx.Combine
	},
	
	stop: function(/*bool?*/ gotoEnd){
		// summary: Stops the running animations.
		// gotoEnd: If true, the animations will end.
		this.fire("onStop");
		this._animsCall("stop", gotoEnd);
		return this; // dojo.lfx.Combine
	},
	
	// private methods
	_onAnimsEnded: function(){
		this._animsEnded++;
		if(this._animsEnded >= this._anims.length){
			this.fire("onEnd");
		}
		return this; // dojo.lfx.Combine
	},
	
	_animsCall: function(/*String*/ funcName){
		var args = [];
		if(arguments.length > 1){
			for(var i = 1 ; i < arguments.length ; i++){
				args.push(arguments[i]);
			}
		}
		var _this = this;
		dojo.lang.forEach(this._anims, function(anim){
			anim[funcName](args);
		}, _this);
		return this; // dojo.lfx.Combine
	}
});

dojo.lfx.Chain = function(/*dojo.lfx.IAnimation...*/ animations) {
	// summary: An animation object to play animations passed to it
	//			one after another.
	dojo.lfx.IAnimation.call(this);
	this._anims = [];
	this._currAnim = -1;
	
	var anims = arguments;
	if(anims.length == 1 && (dojo.lang.isArray(anims[0]) || dojo.lang.isArrayLike(anims[0]))){
		/* animations: dojo.lfx.IAnimation[]
		   pId: a */
		anims = anims[0];
	}
	
	var _this = this;
	dojo.lang.forEach(anims, function(anim, i, anims_arr){
		this._anims.push(anim);
		if(i < anims_arr.length - 1){
			anim.connect("onEnd", dojo.lang.hitch(this, "_playNext") );
		}else{
			anim.connect("onEnd", dojo.lang.hitch(this, function(){ this.fire("onEnd"); }) );
		}
	}, this);
}
dojo.inherits(dojo.lfx.Chain, dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Chain, {
	// private members
	_currAnim: -1,
	
	// public methods
	play: function(/*int?*/ delay, /*bool?*/ gotoStart){
		// summary: Start the animation sequence.
		// delay: How many milliseconds to delay before starting.
		// gotoStart: If true, starts the sequence from the beginning; otherwise,
		//            starts it from its current position.
		if( !this._anims.length ) { return this; /*dojo.lfx.Chain*/}
		if( gotoStart || !this._anims[this._currAnim] ) {
			this._currAnim = 0;
		}

		var currentAnimation = this._anims[this._currAnim];

		this.fire("beforeBegin");
		if(delay > 0){
			setTimeout(dojo.lang.hitch(this, function(){ this.play(null, gotoStart); }), delay);
			return this; // dojo.lfx.Chain
		}
		
		if(currentAnimation){
			if(this._currAnim == 0){
				this.fire("handler", ["begin", this._currAnim]);
				this.fire("onBegin", [this._currAnim]);
			}
			this.fire("onPlay", [this._currAnim]);
			currentAnimation.play(null, gotoStart);
		}
		return this; // dojo.lfx.Chain
	},
	
	pause: function(){
		// summary: Pauses the running animation sequence.
		if( this._anims[this._currAnim] ) {
			this._anims[this._currAnim].pause();
			this.fire("onPause", [this._currAnim]);
		}
		return this; // dojo.lfx.Chain
	},
	
	playPause: function(){
		// summary: If the animation sequence is playing, pause it; otherwise,
		//			play it.
		if(this._anims.length == 0){ return this; }
		if(this._currAnim == -1){ this._currAnim = 0; }
		var currAnim = this._anims[this._currAnim];
		if( currAnim ) {
			if( !currAnim._active || currAnim._paused ) {
				this.play();
			} else {
				this.pause();
			}
		}
		return this; // dojo.lfx.Chain
	},
	
	stop: function(){
		// summary: Stops the running animations.
		var currAnim = this._anims[this._currAnim];
		if(currAnim){
			currAnim.stop();
			this.fire("onStop", [this._currAnim]);
		}
		return currAnim; // dojo.lfx.IAnimation
	},
	
	// private methods
	_playNext: function(){
		if( this._currAnim == -1 || this._anims.length == 0 ) { return this; }
		this._currAnim++;
		if( this._anims[this._currAnim] ){
			this._anims[this._currAnim].play(null, true);
		}
		return this; // dojo.lfx.Chain
	}
});

dojo.lfx.combine = function(/*dojo.lfx.IAnimation...*/ animations){
	// summary: Convenience function.  Returns a dojo.lfx.Combine created
	//			using the animations passed in.
	var anims = arguments;
	if(dojo.lang.isArray(arguments[0])){
		/* animations: dojo.lfx.IAnimation[]
		   pId: a */
		anims = arguments[0];
	}
	if(anims.length == 1){ return anims[0]; }
	return new dojo.lfx.Combine(anims); // dojo.lfx.Combine
}

dojo.lfx.chain = function(/*dojo.lfx.IAnimation...*/ animations){
	// summary: Convenience function.  Returns a dojo.lfx.Chain created
	//			using the animations passed in.
	var anims = arguments;
	if(dojo.lang.isArray(arguments[0])){
		/* animations: dojo.lfx.IAnimation[]
		   pId: a */
		anims = arguments[0];
	}
	if(anims.length == 1){ return anims[0]; }
	return new dojo.lfx.Chain(anims); // dojo.lfx.Combine
}

dojo.require("dojo.html.style");
dojo.provide("dojo.html.color");


dojo.require("dojo.lang.common");

dojo.html.getBackgroundColor = function(/* HTMLElement */node){
	//	summary
	//	returns the background color of the passed node as a 32-bit color (RGBA)
	node = dojo.byId(node);
	var color;
	do{
		color = dojo.html.getStyle(node, "background-color");
		// Safari doesn't say "transparent"
		if(color.toLowerCase() == "rgba(0, 0, 0, 0)") { color = "transparent"; }
		if(node == document.getElementsByTagName("body")[0]) { node = null; break; }
		node = node.parentNode;
	}while(node && dojo.lang.inArray(["transparent", ""], color));
	if(color == "transparent"){
		color = [255, 255, 255, 0];
	}else{
		color = dojo.gfx.color.extractRGB(color);
	}
	return color;	//	array
}

dojo.provide("dojo.lfx.html");



dojo.require("dojo.lang.array");
dojo.require("dojo.html.display");



dojo.lfx.html._byId = function(nodes){
	if(!nodes){ return []; }
	if(dojo.lang.isArrayLike(nodes)){
		if(!nodes.alreadyChecked){
			var n = [];
			dojo.lang.forEach(nodes, function(node){
				n.push(dojo.byId(node));
			});
			n.alreadyChecked = true;
			return n;
		}else{
			return nodes;
		}
	}else{
		var n = [];
		n.push(dojo.byId(nodes));
		n.alreadyChecked = true;
		return n;
	}
}

dojo.lfx.html.propertyAnimation = function(	/*DOMNode[]*/ nodes, 
											/*Object[]*/ propertyMap, 
											/*int*/ duration,
											/*function*/ easing,
											/*Object*/ handlers){
	// summary: Returns an animation that will transition the properties of "nodes"
	//			depending how they are defined in "propertyMap".
	// nodes: An array of DOMNodes or one DOMNode.
	// propertyMap: { property: String, start: Decimal?, end: Decimal?, units: String? }
	//				An array of objects defining properties to change.
	// duration: Duration of the animation in milliseconds.
	// easing: An easing function.
	// handlers: { handler: Function?, onstart: Function?, onstop: Function?, onanimate: Function? }
	nodes = dojo.lfx.html._byId(nodes);

	var targs = {
		"propertyMap": propertyMap,
		"nodes": nodes,
		"duration": duration,
		"easing": easing||dojo.lfx.easeDefault
	};
	
	var setEmUp = function(args){
		if(args.nodes.length==1){
			// FIXME: we're only supporting start-value filling when one node is
			// passed
			
			var pm = args.propertyMap;
			if(!dojo.lang.isArray(args.propertyMap)){
				// it's stupid to have to pack an array with a set of objects
				// when you can just pass in an object list
				var parr = [];
				for(var pname in pm){
					pm[pname].property = pname;
					parr.push(pm[pname]);
				}
				pm = args.propertyMap = parr;
			}
			dojo.lang.forEach(pm, function(prop){
				if(dj_undef("start", prop)){
					if(prop.property != "opacity"){
						prop.start = parseInt(dojo.html.getComputedStyle(args.nodes[0], prop.property));
					}else{
						prop.start = dojo.html.getOpacity(args.nodes[0]);
					}
				}
			});
		}
	}

	var coordsAsInts = function(coords){
		var cints = [];
		dojo.lang.forEach(coords, function(c){ 
			cints.push(Math.round(c));
		});
		return cints;
	}

	var setStyle = function(n, style){
		n = dojo.byId(n);
		if(!n || !n.style){ return; }
		for(var s in style){
			try{
				if(s == "opacity"){
					dojo.html.setOpacity(n, style[s]);
				}else{
						n.style[s] = style[s];
				}
			}catch(e){ dojo.debug(e); }
		}
	}

	var propLine = function(properties){
		this._properties = properties;
		this.diffs = new Array(properties.length);
		dojo.lang.forEach(properties, function(prop, i){
			// calculate the end - start to optimize a bit
			if(dojo.lang.isFunction(prop.start)){
				prop.start = prop.start(prop, i);
			}
			if(dojo.lang.isFunction(prop.end)){
				prop.end = prop.end(prop, i);
			}
			if(dojo.lang.isArray(prop.start)){
				// don't loop through the arrays
				this.diffs[i] = null;
			}else if(prop.start instanceof dojo.gfx.color.Color){
				// save these so we don't have to call toRgb() every getValue() call
				prop.startRgb = prop.start.toRgb();
				prop.endRgb = prop.end.toRgb();
			}else{
				this.diffs[i] = prop.end - prop.start;
			}
		}, this);

		this.getValue = function(n){
			var ret = {};
			dojo.lang.forEach(this._properties, function(prop, i){
				var value = null;
				if(dojo.lang.isArray(prop.start)){
					// FIXME: what to do here?
				}else if(prop.start instanceof dojo.gfx.color.Color){
					value = (prop.units||"rgb") + "(";
					for(var j = 0 ; j < prop.startRgb.length ; j++){
						value += Math.round(((prop.endRgb[j] - prop.startRgb[j]) * n) + prop.startRgb[j]) + (j < prop.startRgb.length - 1 ? "," : "");
					}
					value += ")";
				}else{
					value = ((this.diffs[i]) * n) + prop.start + (prop.property != "opacity" ? prop.units||"px" : "");
				}
				ret[dojo.html.toCamelCase(prop.property)] = value;
			}, this);
			return ret;
		}
	}
	
	var anim = new dojo.lfx.Animation({
			beforeBegin: function(){ 
				setEmUp(targs); 
				anim.curve = new propLine(targs.propertyMap);
			},
			onAnimate: function(propValues){
				dojo.lang.forEach(targs.nodes, function(node){
					setStyle(node, propValues);
				});
			}
		},
		targs.duration, 
		null,
		targs.easing
	);
	if(handlers){
		for(var x in handlers){
			if(dojo.lang.isFunction(handlers[x])){
				anim.connect(x, anim, handlers[x]);
			}
		}
	}
	
	return anim; // dojo.lfx.Animation
}

dojo.lfx.html._makeFadeable = function(nodes){
	var makeFade = function(node){
		if(dojo.render.html.ie){
			// only set the zoom if the "tickle" value would be the same as the
			// default
			if( (node.style.zoom.length == 0) &&
				(dojo.html.getStyle(node, "zoom") == "normal") ){
				// make sure the node "hasLayout"
				// NOTE: this has been tested with larger and smaller user-set text
				// sizes and works fine
				node.style.zoom = "1";
				// node.style.zoom = "normal";
			}
			// don't set the width to auto if it didn't already cascade that way.
			// We don't want to f anyones designs
			if(	(node.style.width.length == 0) &&
				(dojo.html.getStyle(node, "width") == "auto") ){
				node.style.width = "auto";
			}
		}
	}
	if(dojo.lang.isArrayLike(nodes)){
		dojo.lang.forEach(nodes, makeFade);
	}else{
		makeFade(nodes);
	}
}

dojo.lfx.html.fade = function(/*DOMNode[]*/ nodes,
							  /*Object*/values,
							  /*int?*/ duration,
							  /*Function?*/ easing,
							  /*Function?*/ callback){
	// summary:Returns an animation that will fade the "nodes" from the start to end values passed.
	// nodes: An array of DOMNodes or one DOMNode.
	// values: { start: Decimal?, end: Decimal? }
	// duration: Duration of the animation in milliseconds.
	// easing: An easing function.
	// callback: Function to run at the end of the animation.
	nodes = dojo.lfx.html._byId(nodes);
	var props = { property: "opacity" };
	if(!dj_undef("start", values)){
		props.start = values.start;
	}else{
		props.start = function(){ return dojo.html.getOpacity(nodes[0]); };
	}

	if(!dj_undef("end", values)){
		props.end = values.end;
	}else{
		dojo.raise("dojo.lfx.html.fade needs an end value");
	}

	var anim = dojo.lfx.propertyAnimation(nodes, [ props ], duration, easing);
	anim.connect("beforeBegin", function(){
		dojo.lfx.html._makeFadeable(nodes);
	});
	if(callback){
		anim.connect("onEnd", function(){ callback(nodes, anim); });
	}

	return anim; // dojo.lfx.Animation
}

dojo.lfx.html.fadeIn = function(/*DOMNode[]*/ nodes, /*int?*/ duration, /*Function?*/ easing, /*Function?*/ callback){
	// summary: Returns an animation that will fade "nodes" from its current opacity to fully opaque.
	// nodes: An array of DOMNodes or one DOMNode.
	// duration: Duration of the animation in milliseconds.
	// easing: An easing function.
	// callback: Function to run at the end of the animation.
	return dojo.lfx.html.fade(nodes, { end: 1 }, duration, easing, callback); // dojo.lfx.Animation
}

dojo.lfx.html.fadeOut = function(/*DOMNode[]*/ nodes, /*int?*/ duration, /*Function?*/ easing, /*Function?*/ callback){
	// summary: Returns an animation that will fade "nodes" from its current opacity to fully transparent.
	// nodes: An array of DOMNodes or one DOMNode.
	// duration: Duration of the animation in milliseconds.
	// easing: An easing function.
	// callback: Function to run at the end of the animation.	
	return dojo.lfx.html.fade(nodes, { end: 0 }, duration, easing, callback); // dojo.lfx.Animation
}

dojo.lfx.html.fadeShow = function(/*DOMNode[]*/ nodes, /*int?*/ duration, /*Function?*/ easing, /*Function?*/ callback){
	// summary: Returns an animation that will fade "nodes" from transparent to opaque and shows
	//			"nodes" at the end if it is hidden.
	// nodes: An array of DOMNodes or one DOMNode.
	// duration: Duration of the animation in milliseconds.
	// easing: An easing function.
	// callback: Function to run at the end of the animation.	
	nodes=dojo.lfx.html._byId(nodes);
	dojo.lang.forEach(nodes, function(node){
		dojo.html.setOpacity(node, 0.0);
	});

	var anim = dojo.lfx.html.fadeIn(nodes, duration, easing, callback);
	anim.connect("beforeBegin", function(){ 
		if(dojo.lang.isArrayLike(nodes)){
			dojo.lang.forEach(nodes, dojo.html.show);
		}else{
			dojo.html.show(nodes);
		}
	});

	return anim; // dojo.lfx.Animation
}

dojo.lfx.html.fadeHide = function(/*DOMNode[]*/ nodes, /*int?*/ duration, /*Function?*/ easing, /*Function?*/ callback){
	// summary: Returns an animation that will fade "nodes" from its current opacity to opaque and hides
	//			"nodes" at the end.
	// nodes: An array of DOMNodes or one DOMNode.
	// duration: Duration of the animation in milliseconds.
	// easing: An easing function.
	// callback: Function to run at the end of the animation.
	var anim = dojo.lfx.html.fadeOut(nodes, duration, easing, function(){
		if(dojo.lang.isArrayLike(nodes)){
			dojo.lang.forEach(nodes, dojo.html.hide);
		}else{
			dojo.html.hide(nodes);
		}
		if(callback){ callback(nodes, anim); }
	});
	
	return anim; // dojo.lfx.Animation
}

dojo.lfx.html.wipeIn = function(/*DOMNode[]*/ nodes, /*int?*/ duration, /*Function?*/ easing, /*Function?*/ callback){
	// summary: Returns an animation that will show and wipe in "nodes".
	// nodes: An array of DOMNodes or one DOMNode.
	// duration: Duration of the animation in milliseconds.
	// easing: An easing function.
	// callback: Function to run at the end of the animation.
	nodes = dojo.lfx.html._byId(nodes);
	var anims = [];

	dojo.lang.forEach(nodes, function(node){
		var oprop = {  };	// old properties of node (before we mucked w/them)
		
		// get node height, either it's natural height or it's height specified via style or class attributes
		// (for FF, the node has to be (temporarily) rendered to measure height)
		// TODO: should this offscreen code be part of dojo.html, so that getBorderBox() works on hidden nodes?
		var origTop, origLeft, origPosition;
		with(node.style){
			origTop=top; origLeft=left; origPosition=position;
			top="-9999px"; left="-9999px"; position="absolute";
			display="";
		}
		var nodeHeight = dojo.html.getBorderBox(node).height;
		with(node.style){
			top=origTop; left=origLeft; position=origPosition;
			display="none";
		}

		var anim = dojo.lfx.propertyAnimation(node,
			{	"height": {
					start: 1, // 0 causes IE to display the whole panel
					end: function(){ return nodeHeight; } 
				}
			}, 
			duration, 
			easing);
	
		anim.connect("beforeBegin", function(){
			oprop.overflow = node.style.overflow;
			oprop.height = node.style.height;
			with(node.style){
				overflow = "hidden";
				height = "1px"; // 0 causes IE to display the whole panel
			}
			dojo.html.show(node);
		});
		
		anim.connect("onEnd", function(){ 
			with(node.style){
				overflow = oprop.overflow;
				height = oprop.height;
			}
			if(callback){ callback(node, anim); }
		});
		anims.push(anim);
	});
	
	return dojo.lfx.combine(anims); // dojo.lfx.Combine
}

dojo.lfx.html.wipeOut = function(/*DOMNode[]*/ nodes, /*int?*/ duration, /*Function?*/ easing, /*Function?*/ callback){
	// summary: Returns an animation that will wipe out and hide "nodes".
	// nodes: An array of DOMNodes or one DOMNode.
	// duration: Duration of the animation in milliseconds.
	// easing: An easing function.
	// callback: Function to run at the end of the animation.
	nodes = dojo.lfx.html._byId(nodes);
	var anims = [];
	
	dojo.lang.forEach(nodes, function(node){
		var oprop = {  };	// old properties of node (before we mucked w/them)
		var anim = dojo.lfx.propertyAnimation(node,
			{	"height": {
					start: function(){ return dojo.html.getContentBox(node).height; },
					end: 1 // 0 causes IE to display the whole panel
				} 
			},
			duration,
			easing,
			{
				"beforeBegin": function(){
					oprop.overflow = node.style.overflow;
					oprop.height = node.style.height;
					with(node.style){
						overflow = "hidden";
					}
					dojo.html.show(node);
				},
				
				"onEnd": function(){ 
					dojo.html.hide(node);
					with(node.style){
						overflow = oprop.overflow;
						height = oprop.height;
					}
					if(callback){ callback(node, anim); }
				}
			}
		);
		anims.push(anim);
	});

	return dojo.lfx.combine(anims); // dojo.lfx.Combine
}

dojo.lfx.html.slideTo = function(/*DOMNode*/ nodes,
								 /*Object*/ coords,
								 /*int?*/ duration,
								 /*Function?*/ easing,
								 /*Function?*/ callback){
	// summary: Returns an animation that will slide "nodes" from its current position to
	//			the position defined in "coords".
	// nodes: An array of DOMNodes or one DOMNode.
	// coords: { top: Decimal?, left: Decimal? }
	// duration: Duration of the animation in milliseconds.
	// easing: An easing function.
	// callback: Function to run at the end of the animation.
	nodes = dojo.lfx.html._byId(nodes);
	var anims = [];
	var compute = dojo.html.getComputedStyle;
	
	if(dojo.lang.isArray(coords)){
		/* coords: Array
		   pId: a */
		dojo.deprecated('dojo.lfx.html.slideTo(node, array)', 'use dojo.lfx.html.slideTo(node, {top: value, left: value});', '0.5');
		coords = { top: coords[0], left: coords[1] };
	}
	dojo.lang.forEach(nodes, function(node){
		var top = null;
		var left = null;
		
		var init = (function(){
			var innerNode = node;
			return function(){
				var pos = compute(innerNode, 'position');
				top = (pos == 'absolute' ? node.offsetTop : parseInt(compute(node, 'top')) || 0);
				left = (pos == 'absolute' ? node.offsetLeft : parseInt(compute(node, 'left')) || 0);

				if (!dojo.lang.inArray(['absolute', 'relative'], pos)) {
					var ret = dojo.html.abs(innerNode, true);
					dojo.html.setStyleAttributes(innerNode, "position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");
					top = ret.y;
					left = ret.x;
				}
			}
		})();
		init();
		
		var anim = dojo.lfx.propertyAnimation(node,
			{	"top": { start: top, end: (coords.top||0) },
				"left": { start: left, end: (coords.left||0)  }
			},
			duration,
			easing,
			{ "beforeBegin": init }
		);

		if(callback){
			anim.connect("onEnd", function(){ callback(nodes, anim); });
		}

		anims.push(anim);
	});
	
	return dojo.lfx.combine(anims); // dojo.lfx.Combine
}

dojo.lfx.html.slideBy = function(/*DOMNode*/ nodes, /*Object*/ coords, /*int?*/ duration, /*Function?*/ easing, /*Function?*/ callback){
	// summary: Returns an animation that will slide "nodes" from its current position
	//			to its current position plus the numbers defined in "coords".
	// nodes: An array of DOMNodes or one DOMNode.
	// coords: { top: Decimal?, left: Decimal? }
	// duration: Duration of the animation in milliseconds.
	// easing: An easing function.
	// callback: Function to run at the end of the animation.
	nodes = dojo.lfx.html._byId(nodes);
	var anims = [];
	var compute = dojo.html.getComputedStyle;

	if(dojo.lang.isArray(coords)){
		/* coords: Array
		   pId: a */
		dojo.deprecated('dojo.lfx.html.slideBy(node, array)', 'use dojo.lfx.html.slideBy(node, {top: value, left: value});', '0.5');
		coords = { top: coords[0], left: coords[1] };
	}

	dojo.lang.forEach(nodes, function(node){
		var top = null;
		var left = null;
		
		var init = (function(){
			var innerNode = node;
			return function(){
				var pos = compute(innerNode, 'position');
				top = (pos == 'absolute' ? node.offsetTop : parseInt(compute(node, 'top')) || 0);
				left = (pos == 'absolute' ? node.offsetLeft : parseInt(compute(node, 'left')) || 0);

				if (!dojo.lang.inArray(['absolute', 'relative'], pos)) {
					var ret = dojo.html.abs(innerNode, true);
					dojo.html.setStyleAttributes(innerNode, "position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");
					top = ret.y;
					left = ret.x;
				}
			}
		})();
		init();
		
		var anim = dojo.lfx.propertyAnimation(node,
			{
				"top": { start: top, end: top+(coords.top||0) },
				"left": { start: left, end: left+(coords.left||0) }
			},
			duration,
			easing).connect("beforeBegin", init);

		if(callback){
			anim.connect("onEnd", function(){ callback(nodes, anim); });
		}

		anims.push(anim);
	});

	return dojo.lfx.combine(anims); // dojo.lfx.Combine
}

dojo.lfx.html.explode = function(/*DOMNode*/ start,
								 /*DOMNode*/ endNode,
								 /*int?*/ duration,
								 /*Function?*/ easing,
								 /*Function?*/ callback){
	// summary: Returns an animation that will 
	// start:
	// endNode:
	// duration: Duration of the animation in milliseconds.
	// easing: An easing function.
	// callback: Function to run at the end of the animation.
	var h = dojo.html;
	start = dojo.byId(start);
	endNode = dojo.byId(endNode);
	var startCoords = h.toCoordinateObject(start, true);
	var outline = document.createElement("div");
	h.copyStyle(outline, endNode);
	if(endNode.explodeClassName){ outline.className = endNode.explodeClassName; }
	with(outline.style){
		position = "absolute";
		display = "none";
		// border = "1px solid black";
		var backgroundStyle = h.getStyle(start, "background-color");
		backgroundColor = backgroundStyle ? backgroundStyle.toLowerCase() : "transparent";
		backgroundColor = (backgroundColor == "transparent") ? "rgb(221, 221, 221)" : backgroundColor;
	}
	dojo.body().appendChild(outline);

	with(endNode.style){
		visibility = "hidden";
		display = "block";
	}
	var endCoords = h.toCoordinateObject(endNode, true);
	with(endNode.style){
		display = "none";
		visibility = "visible";
	}

	var props = { opacity: { start: 0.5, end: 1.0 } };
	dojo.lang.forEach(["height", "width", "top", "left"], function(type){
		props[type] = { start: startCoords[type], end: endCoords[type] }
	});
	
	var anim = new dojo.lfx.propertyAnimation(outline, 
		props,
		duration,
		easing,
		{
			"beforeBegin": function(){
				h.setDisplay(outline, "block");
			},
			"onEnd": function(){
				h.setDisplay(endNode, "block");
				outline.parentNode.removeChild(outline);
			}
		}
	);

	if(callback){
		anim.connect("onEnd", function(){ callback(endNode, anim); });
	}
	return anim; // dojo.lfx.Animation
}

dojo.lfx.html.implode = function(/*DOMNode*/ startNode,
								 /*DOMNode*/ end,
								 /*int?*/ duration,
								 /*Function?*/ easing,
								 /*Function?*/ callback){
	// summary: Returns an animation that will 
	// startNode:
	// end:
	// duration: Duration of the animation in milliseconds.
	// easing: An easing function.
	// callback: Function to run at the end of the animation.
	var h = dojo.html;
	startNode = dojo.byId(startNode);
	end = dojo.byId(end);
	var startCoords = dojo.html.toCoordinateObject(startNode, true);
	var endCoords = dojo.html.toCoordinateObject(end, true);

	var outline = document.createElement("div");
	dojo.html.copyStyle(outline, startNode);
	if (startNode.explodeClassName) { outline.className = startNode.explodeClassName; }
	dojo.html.setOpacity(outline, 0.3);
	with(outline.style){
		position = "absolute";
		display = "none";
		backgroundColor = h.getStyle(startNode, "background-color").toLowerCase();
	}
	dojo.body().appendChild(outline);

	var props = { opacity: { start: 1.0, end: 0.5 } };
	dojo.lang.forEach(["height", "width", "top", "left"], function(type){
		props[type] = { start: startCoords[type], end: endCoords[type] }
	});
	
	var anim = new dojo.lfx.propertyAnimation(outline,
		props,
		duration,
		easing,
		{
			"beforeBegin": function(){
				dojo.html.hide(startNode);
				dojo.html.show(outline);
			},
			"onEnd": function(){
				outline.parentNode.removeChild(outline);
			}
		}
	);

	if(callback){
		anim.connect("onEnd", function(){ callback(startNode, anim); });
	}
	return anim; // dojo.lfx.Animation
}

dojo.lfx.html.highlight = function(/*DOMNode[]*/ nodes,
								   /*dojo.gfx.color.Color*/ startColor,
								   /*int?*/ duration,
								   /*Function?*/ easing,
								   /*Function?*/ callback){
	// summary: Returns an animation that will set the background color
	//			of "nodes" to startColor and transition it to "nodes"
	//			original color.
	// startColor: Color to transition from.
	// duration: Duration of the animation in milliseconds.
	// easing: An easing function.
	// callback: Function to run at the end of the animation.
	nodes = dojo.lfx.html._byId(nodes);
	var anims = [];

	dojo.lang.forEach(nodes, function(node){
		var color = dojo.html.getBackgroundColor(node);
		var bg = dojo.html.getStyle(node, "background-color").toLowerCase();
		var bgImage = dojo.html.getStyle(node, "background-image");
		var wasTransparent = (bg == "transparent" || bg == "rgba(0, 0, 0, 0)");
		while(color.length > 3) { color.pop(); }

		var rgb = new dojo.gfx.color.Color(startColor);
		var endRgb = new dojo.gfx.color.Color(color);

		var anim = dojo.lfx.propertyAnimation(node, 
			{ "background-color": { start: rgb, end: endRgb } }, 
			duration, 
			easing,
			{
				"beforeBegin": function(){ 
					if(bgImage){
						node.style.backgroundImage = "none";
					}
					node.style.backgroundColor = "rgb(" + rgb.toRgb().join(",") + ")";
				},
				"onEnd": function(){ 
					if(bgImage){
						node.style.backgroundImage = bgImage;
					}
					if(wasTransparent){
						node.style.backgroundColor = "transparent";
					}
					if(callback){
						callback(node, anim);
					}
				}
			}
		);

		anims.push(anim);
	});
	return dojo.lfx.combine(anims); // dojo.lfx.Combine
}

dojo.lfx.html.unhighlight = function(/*DOMNode[]*/ nodes,
									 /*dojo.gfx.color.Color*/ endColor,
									 /*int?*/ duration,
									 /*Function?*/ easing,
									 /*Function?*/ callback){
	// summary: Returns an animation that will transition "nodes" background color
	//			from its current color to "endColor".
	// endColor: Color to transition to.
	// duration: Duration of the animation in milliseconds.
	// easing: An easing function.
	// callback: Function to run at the end of the animation.
	nodes = dojo.lfx.html._byId(nodes);
	var anims = [];

	dojo.lang.forEach(nodes, function(node){
		var color = new dojo.gfx.color.Color(dojo.html.getBackgroundColor(node));
		var rgb = new dojo.gfx.color.Color(endColor);

		var bgImage = dojo.html.getStyle(node, "background-image");
		
		var anim = dojo.lfx.propertyAnimation(node, 
			{ "background-color": { start: color, end: rgb } },
			duration, 
			easing,
			{
				"beforeBegin": function(){ 
					if(bgImage){
						node.style.backgroundImage = "none";
					}
					node.style.backgroundColor = "rgb(" + color.toRgb().join(",") + ")";
				},
				"onEnd": function(){ 
					if(callback){
						callback(node, anim);
					}
				}
			}
		);
		anims.push(anim);
	});
	return dojo.lfx.combine(anims); // dojo.lfx.Combine
}

dojo.lang.mixin(dojo.lfx, dojo.lfx.html);

dojo.kwCompoundRequire({
	browser: ["dojo.lfx.html"],
	dashboard: ["dojo.lfx.html"]
});
dojo.provide("dojo.lfx.*");

dojo.provide("dojo.lfx.toggle");


dojo.lfx.toggle.plain = {
	show: function(node, duration, easing, callback){
		dojo.html.show(node);
		if(dojo.lang.isFunction(callback)){ callback(); }
	},
	
	hide: function(node, duration, easing, callback){
		dojo.html.hide(node);
		if(dojo.lang.isFunction(callback)){ callback(); }
	}
}

dojo.lfx.toggle.fade = {
	show: function(node, duration, easing, callback){
		dojo.lfx.fadeShow(node, duration, easing, callback).play();
	},

	hide: function(node, duration, easing, callback){
		dojo.lfx.fadeHide(node, duration, easing, callback).play();
	}
}

dojo.lfx.toggle.wipe = {
	show: function(node, duration, easing, callback){
		dojo.lfx.wipeIn(node, duration, easing, callback).play();
	},

	hide: function(node, duration, easing, callback){
		dojo.lfx.wipeOut(node, duration, easing, callback).play();
	}
}

dojo.lfx.toggle.explode = {
	show: function(node, duration, easing, callback, explodeSrc){
		dojo.lfx.explode(explodeSrc||{x:0,y:0,width:0,height:0}, node, duration, easing, callback).play();
	},

	hide: function(node, duration, easing, callback, explodeSrc){
		dojo.lfx.implode(node, explodeSrc||{x:0,y:0,width:0,height:0}, duration, easing, callback).play();
	}
}

dojo.provide("dojo.widget.HtmlWidget");


dojo.require("dojo.html.display");

dojo.require("dojo.lang.extras");
dojo.require("dojo.lang.func");


dojo.declare("dojo.widget.HtmlWidget", dojo.widget.DomWidget, {								 
	// summary
	//	Base class for all browser based widgets, or at least "html" widgets.
	//	The meaning of "html" has become unclear; in practice, all widgets derive from this class.
	
	// templateCssPath: String
	//	Path to CSS file for this widget
	templateCssPath: null,
	
	// templatePath: String
	//	Path to template (HTML file) for this widget
	templatePath: null,

	// lang: String
	//	Language to display this widget in (like en-us).
	//	Defaults to brower's specified preferred language (typically the language of the OS)
	lang: "",

	// toggle: String
	//	Controls animation effect for when show() and hide() (or toggle()) are called.
	//	Possible values: "plain", "wipe", "fade", "explode"
	toggle: "plain",

	// toggleDuration: Integer
	//	Number of milliseconds for toggle animation effect to complete
	toggleDuration: 150,

	initialize: function(args, frag){
		// summary: called after the widget is rendered; most subclasses won't override or call this function
	},

	postMixInProperties: function(args, frag){
		if(this.lang === ""){this.lang = null;}
		// now that we know the setting for toggle, get toggle object
		// (default to plain toggler if user specified toggler not present)
		this.toggleObj =
			dojo.lfx.toggle[this.toggle.toLowerCase()] || dojo.lfx.toggle.plain;
	},

	createNodesFromText: function(txt, wrap){
		return dojo.html.createNodesFromText(txt, wrap);
	},

	destroyRendering: function(finalize){
		try{
			if(this.bgIframe){
				this.bgIframe.remove();
				delete this.bgIframe;
			}
			if(!finalize && this.domNode){
				dojo.event.browser.clean(this.domNode);
			}
			dojo.widget.HtmlWidget.superclass.destroyRendering.call(this);
		}catch(e){ /* squelch! */ }
	},

	/////////////////////////////////////////////////////////
	// Displaying/hiding the widget
	/////////////////////////////////////////////////////////
	isShowing: function(){
		// summary
		//	Tests whether widget is set to show-mode or hide-mode (see show() and 
		//	hide() methods)
		//
		//	This function is poorly named.  Even if widget is in show-mode,
		//	if it's inside a container that's hidden
		//	(either a container widget, or just a domnode with display:none),
		//	then it won't be displayed
		return dojo.html.isShowing(this.domNode);	// Boolean
	},

	toggleShowing: function(){
		// summary: show or hide the widget, to switch it's state
		if(this.isShowing()){
			this.hide();
		}else{
			this.show();
		}
	},

	show: function(){
		// summary: show the widget
		if(this.isShowing()){ return; }
		this.animationInProgress=true;
		this.toggleObj.show(this.domNode, this.toggleDuration, null,
			dojo.lang.hitch(this, this.onShow), this.explodeSrc);
	},

	onShow: function(){
		// summary: called after the show() animation has completed
		this.animationInProgress=false;
		this.checkSize();
	},

	hide: function(){
		// summary: hide the widget (ending up with display:none)
		if(!this.isShowing()){ return; }
		this.animationInProgress = true;
		this.toggleObj.hide(this.domNode, this.toggleDuration, null,
			dojo.lang.hitch(this, this.onHide), this.explodeSrc);
	},

	onHide: function(){
		// summary: called after the hide() animation has completed
		this.animationInProgress=false;
	},

	//////////////////////////////////////////////////////////////////////////////
	// Sizing related methods
	//  If the parent changes size then for each child it should call either
	//   - resizeTo(): size the child explicitly
	//   - or checkSize(): notify the child the the parent has changed size
	//////////////////////////////////////////////////////////////////////////////

	_isResized: function(w, h){
		// summary
		//	Test if my size has changed.
		//	If width & height are specified then that's my new size; otherwise,
		//	query outerWidth/outerHeight of my domNode

		// If I'm not being displayed then disregard (show() must
		// check if the size has changed)
		if(!this.isShowing()){ return false; }

		// If my parent has been resized and I have style="height: 100%"
		// or something similar then my size has changed too.
		var wh = dojo.html.getMarginBox(this.domNode);
		var width=w||wh.width;
		var height=h||wh.height;
		if(this.width == width && this.height == height){ return false; }

		this.width=width;
		this.height=height;
		return true;
	},

	checkSize: function(){
		// summary
		//	Called when my parent has changed size, but my parent won't call resizeTo().
		//	This is useful if my size is height:100% or something similar.
		//	Also called whenever I am shown, because the first time I am shown I may need
		//	to do size calculations.
		if(!this._isResized()){ return; }
		this.onResized();
	},

	resizeTo: function(w, h){
		// summary: explicitly set this widget's size (in pixels).
		dojo.html.setMarginBox(this.domNode, { width: w, height: h });
		
		// can't do sizing if widget is hidden because referencing node.offsetWidth/node.offsetHeight returns 0.
		// do sizing on show() instead.
		if(this.isShowing()){
			this.onResized();
		}
	},

	resizeSoon: function(){
		// summary
		//	schedule onResized() to be called soon, after browser has had
		//	a little more time to calculate the sizes
		if(this.isShowing()){
			dojo.lang.setTimeout(this, this.onResized, 0);
		}
	},

	onResized: function(){
		// summary
		//	Called when my size has changed.
		//	Must notify children if their size has (possibly) changed.
		dojo.lang.forEach(this.children, function(child){ if(child.checkSize){child.checkSize();} });
	}
});

dojo.kwCompoundRequire({
	common: ["dojo.xml.Parse", 
			 "dojo.widget.Widget", 
			 "dojo.widget.Parse", 
			 "dojo.widget.Manager"],
	browser: ["dojo.widget.DomWidget",
			  "dojo.widget.HtmlWidget"],
	dashboard: ["dojo.widget.DomWidget",
			  "dojo.widget.HtmlWidget"],
	svg: 	 ["dojo.widget.SvgWidget"],
	rhino: 	 ["dojo.widget.SwtWidget"]
});
dojo.provide("dojo.widget.*");

dojo.kwCompoundRequire({
	common: ["dojo.io.common"],
	rhino: ["dojo.io.RhinoIO"],
	browser: ["dojo.io.BrowserIO", "dojo.io.cookie"],
	dashboard: ["dojo.io.BrowserIO", "dojo.io.cookie"]
});
dojo.provide("dojo.io.*");

dojo.provide("dojo.widget.ContentPane");




dojo.require("dojo.string");
dojo.require("dojo.string.extras");
dojo.require("dojo.html.style");


dojo.widget.defineWidget(
	"dojo.widget.ContentPane",
	dojo.widget.HtmlWidget,
	function(){
		// summary:
		//		A widget that can be used as a standalone widget 
		//		or as a baseclass for other widgets
		//		Handles replacement of document fragment using either external uri or javascript/java 
		//		generated markup or DomNode content, instanciating widgets within content and runs scripts.
		//		Dont confuse it with an iframe, it only needs document fragments.
		//		It's useful as a child of LayoutContainer, SplitContainer, or TabContainer.
		//		But note that those classes can contain any widget as a child.
		// scriptScope: Function
		//		reference holder to the inline scripts container, if scriptSeparation is true
		// bindArgs: String[]
		//		Send in extra args to the dojo.io.bind call
		
		// per widgetImpl variables
		this._styleNodes =  [];
		this._onLoadStack = [];
		this._onUnloadStack = [];
		this._callOnUnload = false;
		this._ioBindObj;
		//	Note:
		//		dont change this value externally
		this.scriptScope; // undefined for now

		// loading option
		//	example:
		//		bindArgs="preventCache:false;" overrides cacheContent
		this.bindArgs = {};

	
	}, {
		isContainer: true,

		// loading options
		// adjustPaths: Boolean
		//		adjust relative paths in markup to fit this page
		adjustPaths: true,

		// href: String
		//		The href of the content that displays now
		//		Set this at construction if you want to load externally,
		//		changing href after creation doesnt have any effect, see setUrl
		href: "",

		// extractContent Boolean: Extract visible content from inside of <body> .... </body>
		extractContent: true,

		// parseContent Boolean: Construct all widgets that is in content
		parseContent:	true,

		// cacheContent Boolean: Cache content retreived externally
		cacheContent:	true,

		// preload: Boolean
		//		Force load of data even if pane is hidden.
		// Note:
		//		In order to delay download you need to initially hide the node it constructs from
		preload: false,

		// refreshOnShow: Boolean
		//		Refresh (re-download) content when pane goes from hidden to shown
		refreshOnShow: false,

		// handler: String||Function
		//		Generate pane content from a java function
		//		The name of the java proxy function
		handler: "",

		// executeScripts: Boolean
		//		Run scripts within content, extractContent has NO effect on this.
		// Note:
		//		if true scripts in content will be evaled after content is innerHTML'ed
		executeScripts: false,

		// scriptSeparation: Boolean
		//		Run scripts in a separate scope, unique for each ContentPane
		scriptSeparation: true,

		// loadingMessage: String
		//		Message that shows while downloading
		loadingMessage: "Loading...",

		// isLoaded: Boolean
		//		Tells loading status
		isLoaded: false,

		postCreate: function(args, frag, parentComp){
			if (this.handler!==""){
				this.setHandler(this.handler);
			}
			if(this.isShowing() || this.preload){
				this.loadContents(); 
			}
		},
	
		show: function(){
			// if refreshOnShow is true, reload the contents every time; otherwise, load only the first time
			if(this.refreshOnShow){
				this.refresh();
			}else{
				this.loadContents();
			}
			dojo.widget.ContentPane.superclass.show.call(this);
		},
	
		refresh: function(){
			// summary:
			//		Force a refresh (re-download) of content, be sure to turn of cache
			this.isLoaded=false;
			this.loadContents();
		},
	
		loadContents: function() {
			// summary:
			//		Download if isLoaded is false, else ignore
			if ( this.isLoaded ){
				return;
			}
			if ( dojo.lang.isFunction(this.handler)) {
				this._runHandler();
			} else if ( this.href != "" ) {
				this._downloadExternalContent(this.href, this.cacheContent && !this.refreshOnShow);
			}
		},
		
		setUrl: function(/*String||dojo.uri.Uri*/ url) {
			// summary:
			//		Reset the (external defined) content of this pane and replace with new url

			//	Note:
			//		It delays the download until widget is shown if preload is false
			this.href = url;
			this.isLoaded = false;
			if ( this.preload || this.isShowing() ){
				this.loadContents();
			}
		},

		abort: function(){
			// summary
			//		Aborts a inflight download of content
			var bind = this._ioBindObj;
			if(!bind || !bind.abort){ return; }
			bind.abort();
			delete this._ioBindObj;
		},
	
		_downloadExternalContent: function(url, useCache) {
			this.abort();
			this._handleDefaults(this.loadingMessage, "onDownloadStart");
			var self = this;
			this._ioBindObj = dojo.io.bind(
				this._cacheSetting({
					url: url,
					mimetype: "text/html",
					handler: function(type, data, xhr){
						delete self._ioBindObj; // makes sure abort doesnt clear cache
						if(type=="load"){
							self.onDownloadEnd.call(self, url, data);
						}else{
							// XHR isnt a normal JS object, IE doesnt have prototype on XHR so we cant extend it or shallowCopy it
							var e = {
								responseText: xhr.responseText,
								status: xhr.status,
								statusText: xhr.statusText,
								responseHeaders: xhr.getAllResponseHeaders(),
								text: "Error loading '" + url + "' (" + xhr.status + " "+  xhr.statusText + ")"
							};
							self._handleDefaults.call(self, e, "onDownloadError");
							self.onLoad();
						}
					}
				}, useCache)
			);
		},
	
		_cacheSetting: function(bindObj, useCache){
			for(var x in this.bindArgs){
				if(dojo.lang.isUndefined(bindObj[x])){
					bindObj[x] = this.bindArgs[x];
				}
			}

			if(dojo.lang.isUndefined(bindObj.useCache)){ bindObj.useCache = useCache; }
			if(dojo.lang.isUndefined(bindObj.preventCache)){ bindObj.preventCache = !useCache; }
			if(dojo.lang.isUndefined(bindObj.mimetype)){ bindObj.mimetype = "text/html"; }
			return bindObj;
		},

		onLoad: function(e){
			// summary:
			//		Event hook, is called after everything is loaded and widgetified 
			this._runStack("_onLoadStack");
			this.isLoaded=true;
		},
	
		onUnLoad: function(e){
			// summary:
			//		Deprecated, use onUnload (lowercased load)
			dojo.deprecated(this.widgetType+".onUnLoad, use .onUnload (lowercased load)", 0.5);
		},

		onUnload: function(e){
			// summary:
			//		Event hook, is called before old content is cleared
			this._runStack("_onUnloadStack");
			delete this.scriptScope;
			// FIXME: remove for 0.5 along with onUnLoad
			if(this.onUnLoad !== dojo.widget.ContentPane.prototype.onUnLoad){
				this.onUnLoad.apply(this, arguments);
			}
		},
	
		_runStack: function(stName){
			var st = this[stName]; var err = "";
			var scope = this.scriptScope || window;
			for(var i = 0;i < st.length; i++){
				try{
					st[i].call(scope);
				}catch(e){ 
					err += "\n"+st[i]+" failed: "+e.description;
				}
			}
			this[stName] = [];
	
			if(err.length){
				var name = (stName== "_onLoadStack") ? "addOnLoad" : "addOnUnLoad";
				this._handleDefaults(name+" failure\n "+err, "onExecError", "debug");
			}
		},
	
		addOnLoad: function(obj, func){
			// summary
			//		Stores function refs and calls them one by one in the order they came in
			//		when load event occurs.
			//	obj: Function||Object?
			//		holder object
			//	func: Function
			//		function that will be called 
			this._pushOnStack(this._onLoadStack, obj, func);
		},
	
		addOnUnload: function(obj, func){
			// summary
			//		Stores function refs and calls them one by one in the order they came in
			//		when unload event occurs.
			//	obj: Function||Object
			//		holder object
			//	func: Function
			//		function that will be called 
			this._pushOnStack(this._onUnloadStack, obj, func);
		},

		addOnUnLoad: function(){
			// summary:
			//		Deprecated use addOnUnload (lower cased load)
			dojo.deprecated(this.widgetType + ".addOnUnLoad, use addOnUnload instead. (lowercased Load)", 0.5);
			this.addOnUnload.apply(this, arguments);
		},
	
		_pushOnStack: function(stack, obj, func){
			if(typeof func == 'undefined') {
				stack.push(obj);
			}else{
				stack.push(function(){ obj[func](); });
			}
		},
	
		destroy: function(){
			// make sure we call onUnload
			this.onUnload();
			dojo.widget.ContentPane.superclass.destroy.call(this);
		},
 
		onExecError: function(/*Object*/e){
			// summary:
			//		called when content script eval error or Java error occurs, preventDefault-able
			//		default is to debug not alert as in 0.3.1
		},
	
		onContentError: function(/*Object*/e){
			// summary: 
			//		called on DOM faults, require fault etc in content, preventDefault-able
			//		default is to display errormessage inside pane
		},
	
		onDownloadError: function(/*Object*/e){
			// summary: 
			//		called when download error occurs, preventDefault-able
			//		default is to display errormessage inside pane
		},
	
		onDownloadStart: function(/*Object*/e){
			// summary:
			//		called before download starts, preventDefault-able
			//		default is to display loadingMessage inside pane
			//		by changing e.text in your event handler you can change loading message
		},
	
		// 
		onDownloadEnd: function(url, data){
			// summary:
			//		called when download is finished
			//
			//	url String: url that downloaded data
			//	data String: the markup that was downloaded
			data = this.splitAndFixPaths(data, url);
			this.setContent(data);
		},
	
		// useful if user wants to prevent default behaviour ie: _setContent("Error...")
		_handleDefaults: function(e, handler, messType){
			if(!handler){ handler = "onContentError"; }

			if(dojo.lang.isString(e)){ e = {text: e}; }

			if(!e.text){ e.text = e.toString(); }

			e.toString = function(){ return this.text; };

			if(typeof e.returnValue != "boolean"){
				e.returnValue = true; 
			}
			if(typeof e.preventDefault != "function"){
				e.preventDefault = function(){ this.returnValue = false; };
			}
			// call our handler
			this[handler](e);
			if(e.returnValue){
				switch(messType){
					case true: // fallthrough, old compat
					case "alert":
						alert(e.toString()); break;
					case "debug":
						dojo.debug(e.toString()); break;
					default:
					// makes sure scripts can clean up after themselves, before we setContent
					if(this._callOnUnload){ this.onUnload(); } 
					// makes sure we dont try to call onUnLoad again on this event,
					// ie onUnLoad before 'Loading...' but not before clearing 'Loading...'
					this._callOnUnload = false;

					// we might end up in a endless recursion here if domNode cant append content
					if(arguments.callee._loopStop){
						dojo.debug(e.toString());
					}else{
						arguments.callee._loopStop = true;
						this._setContent(e.toString());
					}
				}
			}
			arguments.callee._loopStop = false;
		},
	
		// pathfixes, require calls, css stuff and neccesary content clean
		splitAndFixPaths: function(s, url){
			// summary:
			// 		adjusts all relative paths in (hopefully) all cases, images, remote scripts, links etc.
			// 		splits up content in different pieces, scripts, title, style, link and whats left becomes .xml
			//	s String:	The markup in string
			//	url (String||dojo.uri.Uri?) url that pulled in markup

			var titles = [], scripts = [],tmp = [];// init vars
			var match = [], requires = [], attr = [], styles = [];
			var str = '', path = '', fix = '', tagFix = '', tag = '', origPath = '';
	
			if(!url) { url = "./"; } // point to this page if not set

			if(s){ // make sure we dont run regexes on empty content

				/************** <title> ***********/
				// khtml is picky about dom faults, you can't attach a <style> or <title> node as child of body
				// must go into head, so we need to cut out those tags
				var regex = /<title[^>]*>([\s\S]*?)<\/title>/i;
				while(match = regex.exec(s)){
					titles.push(match[1]);
					s = s.substring(0, match.index) + s.substr(match.index + match[0].length);
				};
		
				/************** adjust paths *****************/
				if(this.adjustPaths){
					// attributepaths one tag can have multiple paths example:
					// <input src="..." style="url(..)"/> or <a style="url(..)" href="..">
					// strip out the tag and run fix on that.
					// this guarantees that we won't run replace on another tag's attribute + it was easier do
					var regexFindTag = /<[a-z][a-z0-9]*[^>]*\s(?:(?:src|href|style)=[^>])+[^>]*>/i;
					var regexFindAttr = /\s(src|href|style)=(['"]?)([\w()\[\]\/.,\\'"-:;#=&?\s@]+?)\2/i;
					// these are the supported protocols, all other is considered relative
					var regexProtocols = /^(?:[#]|(?:(?:https?|ftps?|file|javascript|mailto|news):))/;
		
					while(tag = regexFindTag.exec(s)){
						str += s.substring(0, tag.index);
						s = s.substring((tag.index + tag[0].length), s.length);
						tag = tag[0];
			
						// loop through attributes
						tagFix = '';
						while(attr = regexFindAttr.exec(tag)){
							path = ""; origPath = attr[3];
							switch(attr[1].toLowerCase()){
								case "src":// falltrough
								case "href":
									if(regexProtocols.exec(origPath)){
										path = origPath;
									} else {
										path = (new dojo.uri.Uri(url, origPath).toString());
									}
									break;
								case "style":// style
									path = dojo.html.fixPathsInCssText(origPath, url);
									break;
								default:
									path = origPath;
							}
							fix = " " + attr[1] + "=" + attr[2] + path + attr[2];
							// slices up tag before next attribute check
							tagFix += tag.substring(0, attr.index) + fix;
							tag = tag.substring((attr.index + attr[0].length), tag.length);
						}
						str += tagFix + tag; //dojo.debug(tagFix + tag);
					}
					s = str+s;
				}

				/****************  cut out all <style> and <link rel="stylesheet" href=".."> **************/
				regex = /(?:<(style)[^>]*>([\s\S]*?)<\/style>|<link ([^>]*rel=['"]?stylesheet['"]?[^>]*)>)/i;
				while(match = regex.exec(s)){
					if(match[1] && match[1].toLowerCase() == "style"){
						styles.push(dojo.html.fixPathsInCssText(match[2],url));
					}else if(attr = match[3].match(/href=(['"]?)([^'">]*)\1/i)){
						styles.push({path: attr[2]});
					}
					s = s.substring(0, match.index) + s.substr(match.index + match[0].length);
				};

				/***************** cut out all <script> tags, push them into scripts array ***************/
				var regex = /<script([^>]*)>([\s\S]*?)<\/script>/i;
				var regexSrc = /src=(['"]?)([^"']*)\1/i;
				var regexDojoJs = /.*(\bdojo\b\.js(?:\.uncompressed\.js)?)$/;
				var regexInvalid = /(?:var )?\bdjConfig\b(?:[\s]*=[\s]*\{[^}]+\}|\.[\w]*[\s]*=[\s]*[^;\n]*)?;?|dojo\.hostenv\.writeIncludes\(\s*\);?/g;
				var regexRequires = /dojo\.(?:(?:require(?:After)?(?:If)?)|(?:widget\.(?:manager\.)?registerWidgetPackage)|(?:(?:hostenv\.)?setModulePrefix|registerModulePath)|defineNamespace)\((['"]).*?\1\)\s*;?/;

				while(match = regex.exec(s)){
					if(this.executeScripts && match[1]){
						if(attr = regexSrc.exec(match[1])){
							// remove a dojo.js or dojo.js.uncompressed.js from remoteScripts
							// we declare all files named dojo.js as bad, regardless of path
							if(regexDojoJs.exec(attr[2])){
								dojo.debug("Security note! inhibit:"+attr[2]+" from  being loaded again.");
							}else{
								scripts.push({path: attr[2]});
							}
						}
					}
					if(match[2]){
						// remove all invalid variables etc like djConfig and dojo.hostenv.writeIncludes()
						var sc = match[2].replace(regexInvalid, "");
						if(!sc){ continue; }
		
						// cut out all dojo.require (...) calls, if we have execute 
						// scripts false widgets dont get there require calls
						// takes out possible widgetpackage registration as well
						while(tmp = regexRequires.exec(sc)){
							requires.push(tmp[0]);
							sc = sc.substring(0, tmp.index) + sc.substr(tmp.index + tmp[0].length);
						}
						if(this.executeScripts){
							scripts.push(sc);
						}
					}
					s = s.substr(0, match.index) + s.substr(match.index + match[0].length);
				}

				/********* extract content *********/
				if(this.extractContent){
					match = s.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
					if(match) { s = match[1]; }
				}
	
				/*** replace scriptScope prefix in html Event handler
				* working order: find tags with scriptScope in a tag attribute
				* then replace all standalone scriptScope occurencies with reference to to this widget
				* valid onClick="scriptScope.func()" or onClick="scriptScope['func']();scriptScope.i++"
				* not valid onClick="var.scriptScope.ref" nor onClick="var['scriptScope'].ref" */
				if(this.executeScripts && this.scriptSeparation){
					var regex = /(<[a-zA-Z][a-zA-Z0-9]*\s[^>]*?\S=)((['"])[^>]*scriptScope[^>]*>)/;
					var regexAttr = /([\s'";:\(])scriptScope(.*)/; // we rely on that attribute begins ' or "
					str = ""; 
					while(tag = regex.exec(s)){
						tmp = ((tag[3]=="'") ? '"': "'");fix= "";
						str += s.substring(0, tag.index) + tag[1];
						while(attr = regexAttr.exec(tag[2])){
							tag[2] = tag[2].substring(0, attr.index) + attr[1] + "dojo.widget.byId("+ tmp + this.widgetId + tmp + ").scriptScope" + attr[2];
						}
						str += tag[2];
						s = s.substr(tag.index + tag[0].length);
					}
					s = str + s;
				}
	 		}

			return {"xml": 		s, // Object
				"styles":		styles,
				"titles": 		titles,
				"requires": 	requires,
				"scripts": 		scripts,
				"url": 			url};
		},
	
		
		_setContent: function(cont){
			this.destroyChildren();
	
			// remove old stylenodes from HEAD
			for(var i = 0; i < this._styleNodes.length; i++){
				if(this._styleNodes[i] && this._styleNodes[i].parentNode){
					this._styleNodes[i].parentNode.removeChild(this._styleNodes[i]);
				}
			}
			this._styleNodes = [];

			try{
				var node = this.containerNode || this.domNode;
				while(node.firstChild){
					dojo.html.destroyNode(node.firstChild);
				}
				if(typeof cont != "string"){
					node.appendChild(cont);
				}else{
					node.innerHTML = cont;
				}
			}catch(e){
				e.text = "Couldn't load content:"+e.description;
				this._handleDefaults(e, "onContentError");
			}
		},
	
		setContent: function(data){
			// summary:
			//		Replaces old content with data content, include style classes from old content
			//	data String||DomNode:	new content, be it Document fragment or a DomNode chain
			//			If data contains style tags, link rel=stylesheet it inserts those styles into DOM
			this.abort();
			if(this._callOnUnload){ this.onUnload(); }// this tells a remote script clean up after itself
			this._callOnUnload = true;
	
			if(!data || dojo.html.isNode(data)){
				// if we do a clean using setContent(""); or setContent(#node) bypass all parsing, extractContent etc
				this._setContent(data);
				this.onResized();
				this.onLoad();
			}else{
				// need to run splitAndFixPaths? ie. manually setting content
				// adjustPaths is taken care of inside splitAndFixPaths
				if(typeof data.xml != "string"){ 
					this.href = ""; // so we can refresh safely
					data = this.splitAndFixPaths(data); 
				}

				this._setContent(data.xml);

				// insert styles from content (in same order they came in)
				for(var i = 0; i < data.styles.length; i++){
					if(data.styles[i].path){
						this._styleNodes.push(dojo.html.insertCssFile(data.styles[i].path, dojo.doc(), false, true));
					}else{
						this._styleNodes.push(dojo.html.insertCssText(data.styles[i]));
					}
				}
	
				if(this.parseContent){
					for(var i = 0; i < data.requires.length; i++){
						try{
							eval(data.requires[i]);
						} catch(e){
							e.text = "ContentPane: error in package loading calls, " + (e.description||e);
							this._handleDefaults(e, "onContentError", "debug");
						}
					}
				}
				// need to allow async load, Xdomain uses it
				// is inline function because we cant send args to dojo.addOnLoad
				var _self = this;
				function asyncParse(){
					if(_self.executeScripts){
						_self._executeScripts(data.scripts);
					}
	
					if(_self.parseContent){
						var node = _self.containerNode || _self.domNode;
						var parser = new dojo.xml.Parse();
						var frag = parser.parseElement(node, null, true);
						// createSubComponents not createComponents because frag has already been created
						dojo.widget.getParser().createSubComponents(frag, _self);
					}
	
					_self.onResized();
					_self.onLoad();
				}
				// try as long as possible to make setContent sync call
				if(dojo.hostenv.isXDomain && data.requires.length){
					dojo.addOnLoad(asyncParse);
				}else{
					asyncParse();
				}
			}
		},

		setHandler: function(/*Function*/ handler) {
			// summary:
			//		Generate pane content from given java function
			var fcn = dojo.lang.isFunction(handler) ? handler : window[handler];
			if(!dojo.lang.isFunction(fcn)) {
				// FIXME: needs testing! somebody with java knowledge needs to try this
				this._handleDefaults("Unable to set handler, '" + handler + "' not a function.", "onExecError", true);
				return;
			}
			this.handler = function() {
				return fcn.apply(this, arguments);
			}
		},
	
		_runHandler: function() {
			var ret = true;
			if(dojo.lang.isFunction(this.handler)) {
				this.handler(this, this.domNode);
				ret = false;
			}
			this.onLoad();
			return ret;
		},
	
		_executeScripts: function(scripts) {
			// loop through the scripts in the order they came in
			var self = this;
			var tmp = "", code = "";
			for(var i = 0; i < scripts.length; i++){
				if(scripts[i].path){ // remotescript
					dojo.io.bind(this._cacheSetting({
						"url": 		scripts[i].path,
						"load":     function(type, scriptStr){
								dojo.lang.hitch(self, tmp = ";"+scriptStr);
						},
						"error":    function(type, error){
								error.text = type + " downloading remote script";
								self._handleDefaults.call(self, error, "onExecError", "debug");
						},
						"mimetype": "text/plain",
						"sync":     true
					}, this.cacheContent));
					code += tmp;
				}else{
					code += scripts[i];
				}
			}


			try{
				if(this.scriptSeparation){
					// initialize a new anonymous container for our script, dont make it part of this widgets scope chain
					// instead send in a variable that points to this widget, useful to connect events to onLoad, onUnload etc..
					delete this.scriptScope;
					this.scriptScope = new (new Function('_container_', code+'; return this;'))(self);
				}else{
					// exec in global, lose the _container_ feature
					var djg = dojo.global();
					if(djg.execScript){
						djg.execScript(code);
					}else{
						var djd = dojo.doc();
						var sc = djd.createElement("script");
						sc.appendChild(djd.createTextNode(code));
						(this.containerNode||this.domNode).appendChild(sc);
					}
				}
			}catch(e){
				e.text = "Error running scripts from content:\n"+e.description;
				this._handleDefaults(e, "onExecError", "debug");
			}
		}
	}
);

dojo.require("dojo.html.common");
dojo.provide("dojo.html.selection");

dojo.require("dojo.dom");
dojo.require("dojo.lang.common");

/**
 * type of selection
**/
dojo.html.selectionType = {
	NONE : 0, //selection is empty
	TEXT : 1, //selection contains text (may also contains CONTROL objects)
	CONTROL : 2 //only one element is selected (such as img, table etc)
};

dojo.html.clearSelection = function(){
	// summary: deselect the current selection to make it empty
	var _window = dojo.global();
	var _document = dojo.doc();
	try{
		if(_window["getSelection"]){ 
			if(dojo.render.html.safari){
				// pulled from WebCore/ecma/kjs_window.cpp, line 2536
				_window.getSelection().collapse();
			}else{
				_window.getSelection().removeAllRanges();
			}
		}else if(_document.selection){
			if(_document.selection.empty){
				_document.selection.empty();
			}else if(_document.selection.clear){
				_document.selection.clear();
			}
		}
		return true;
	}catch(e){
		dojo.debug(e);
		return false;
	}
}

dojo.html.disableSelection = function(/*DomNode*/element){
	// summary: disable selection on a node
	element = dojo.byId(element)||dojo.body();
	var h = dojo.render.html;
	
	if(h.mozilla){
		element.style.MozUserSelect = "none";
	}else if(h.safari){
		element.style.KhtmlUserSelect = "none"; 
	}else if(h.ie){
		element.unselectable = "on";
	}else{
		return false;
	}
	return true;
}

dojo.html.enableSelection = function(/*DomNode*/element){
	// summary: enable selection on a node
	element = dojo.byId(element)||dojo.body();
	
	var h = dojo.render.html;
	if(h.mozilla){ 
		element.style.MozUserSelect = ""; 
	}else if(h.safari){
		element.style.KhtmlUserSelect = "";
	}else if(h.ie){
		element.unselectable = "off";
	}else{
		return false;
	}
	return true;
}

dojo.html.selectElement = function(/*DomNode*/element){
	dojo.deprecated("dojo.html.selectElement", "replaced by dojo.html.selection.selectElementChildren", 0.5);
}

dojo.html.selectInputText = function(/*DomNode*/element){
	// summary: select all the text in an input element
	var _window = dojo.global();
	var _document = dojo.doc();
	element = dojo.byId(element);
	if(_document["selection"] && dojo.body()["createTextRange"]){ // IE
		var range = element.createTextRange();
		range.moveStart("character", 0);
		range.moveEnd("character", element.value.length);
		range.select();
	}else if(_window["getSelection"]){
		var selection = _window.getSelection();
		// FIXME: does this work on Safari?
		element.setSelectionRange(0, element.value.length);
	}
	element.focus();
}


dojo.html.isSelectionCollapsed = function(){
	dojo.deprecated("dojo.html.isSelectionCollapsed", "replaced by dojo.html.selection.isCollapsed", 0.5);
	return dojo.html.selection.isCollapsed();
}

dojo.lang.mixin(dojo.html.selection, {
	getType: function() {
		// summary: Get the selection type (like document.select.type in IE).
		if(dojo.doc()["selection"]){ //IE
			return dojo.html.selectionType[dojo.doc().selection.type.toUpperCase()];
		}else{
			var stype = dojo.html.selectionType.TEXT;
	
			// Check if the actual selection is a CONTROL (IMG, TABLE, HR, etc...).
			var oSel;
			try {oSel = dojo.global().getSelection();}
			catch (e) {}
			
			if(oSel && oSel.rangeCount==1){
				var oRange = oSel.getRangeAt(0);
				if (oRange.startContainer == oRange.endContainer && (oRange.endOffset - oRange.startOffset) == 1
					&& oRange.startContainer.nodeType != dojo.dom.TEXT_NODE) {
					stype = dojo.html.selectionType.CONTROL;
				}
			}
			return stype;
		}
	},
	isCollapsed: function() {
		// summary: return whether the current selection is empty
		var _window = dojo.global();
		var _document = dojo.doc();
		if(_document["selection"]){ // IE
			return _document.selection.createRange().text == "";
		}else if(_window["getSelection"]){
			var selection = _window.getSelection();
			if(dojo.lang.isString(selection)){ // Safari
				return selection == "";
			}else{ // Mozilla/W3
				return selection.isCollapsed || selection.toString() == "";
			}
		}
	},
	getSelectedElement: function() {
		// summary: 
		//		Retrieves the selected element (if any), just in the case that a single
		//		element (object like and image or a table) is selected.
		if ( dojo.html.selection.getType() == dojo.html.selectionType.CONTROL ){
			if(dojo.doc()["selection"]){ //IE
				var range = dojo.doc().selection.createRange();
		
				if ( range && range.item ){
					return dojo.doc().selection.createRange().item(0);
				}
			}else{
				var selection = dojo.global().getSelection();
				return selection.anchorNode.childNodes[ selection.anchorOffset ];
			}
		}
	},
	getParentElement: function() {
		// summary: 
		//		Get the parent element of the current selection
		if(dojo.html.selection.getType() == dojo.html.selectionType.CONTROL){
			var p = dojo.html.selection.getSelectedElement();
			if(p){ return p.parentNode; }
		}else{
			if(dojo.doc()["selection"]){ //IE
				return dojo.doc().selection.createRange().parentElement();
			}else{
				var selection = dojo.global().getSelection();
				if(selection){
					var node = selection.anchorNode;
		
					while ( node && node.nodeType != dojo.dom.ELEMENT_NODE ){
						node = node.parentNode;
					}
		
					return node;
				}
			}
		}
	},
	getSelectedText: function(){
		// summary:
		//		Return the text (no html tags) included in the current selection or null if no text is selected
		if(dojo.doc()["selection"]){ //IE
			if(dojo.html.selection.getType() == dojo.html.selectionType.CONTROL){
				return null;
			}
			return dojo.doc().selection.createRange().text;
		}else{
			var selection = dojo.global().getSelection();
			if(selection){
				return selection.toString();
			}
		}
	},
	getSelectedHtml: function(){
		// summary:
		//		Return the html of the current selection or null if unavailable
		if(dojo.doc()["selection"]){ //IE
			if(dojo.html.selection.getType() == dojo.html.selectionType.CONTROL){
				return null;
			}
			return dojo.doc().selection.createRange().htmlText;
		}else{
			var selection = dojo.global().getSelection();
			if(selection && selection.rangeCount){
				var frag = selection.getRangeAt(0).cloneContents();
				var div = document.createElement("div");
				div.appendChild(frag);
				return div.innerHTML;
			}
			return null;
		}
	},
	hasAncestorElement: function(/*String*/tagName /* ... */){
		// summary: 
		// 		Check whether current selection has a  parent element which is of type tagName (or one of the other specified tagName)
		return (dojo.html.selection.getAncestorElement.apply(this, arguments) != null);
	},
	getAncestorElement: function(/*String*/tagName /* ... */){
		// summary:
		//		Return the parent element of the current selection which is of type tagName (or one of the other specified tagName)
		var node = dojo.html.selection.getSelectedElement() || dojo.html.selection.getParentElement();
		while(node /*&& node.tagName.toLowerCase() != 'body'*/){
			if(dojo.html.selection.isTag(node, arguments).length>0){
				return node;
			}
			node = node.parentNode;
		}
		return null;
	},
	//modified from dojo.html.isTag to take an array as second parameter
	isTag: function(/*DomNode*/node, /*Array*/tags) {
		if(node && node.tagName) {
			for (var i=0; i<tags.length; i++){
				if (node.tagName.toLowerCase()==String(tags[i]).toLowerCase()){
					return String(tags[i]).toLowerCase();
				}
			}
		}
		return "";
	},
	selectElement: function(/*DomNode*/element) {
		// summary: clear previous selection and select element (including all its children)
		var _window = dojo.global();
		var _document = dojo.doc();
		element = dojo.byId(element);
		if(_document.selection && dojo.body().createTextRange){ // IE
			try{
				var range = dojo.body().createControlRange();
				range.addElement(element);
				range.select();
			}catch(e){
				dojo.html.selection.selectElementChildren(element);
			}
		}else if(_window["getSelection"]){
			var selection = _window.getSelection();
			// FIXME: does this work on Safari?
			if(selection["removeAllRanges"]){ // Mozilla
				var range = _document.createRange() ;
				range.selectNode(element) ;
				selection.removeAllRanges() ;
				selection.addRange(range) ;
			}
		}
	},
	selectElementChildren: function(/*DomNode*/element){
		// summary: clear previous selection and select the content of the node (excluding the node itself)
		var _window = dojo.global();
		var _document = dojo.doc();
		element = dojo.byId(element);
		if(_document.selection && dojo.body().createTextRange){ // IE
			var range = dojo.body().createTextRange();
			range.moveToElementText(element);
			range.select();
		}else if(_window["getSelection"]){
			var selection = _window.getSelection();
			if(selection["setBaseAndExtent"]){ // Safari
				selection.setBaseAndExtent(element, 0, element, element.innerText.length - 1);
			} else if(selection["selectAllChildren"]){ // Mozilla
				selection.selectAllChildren(element);
			}
		}
	},
	getBookmark: function(){
		// summary: Retrieves a bookmark that can be used with moveToBookmark to return to the same range
		var bookmark;
		var _document = dojo.doc();
		if(_document["selection"]){ // IE
			var range = _document.selection.createRange();
			bookmark = range.getBookmark();
		}else{
			var selection;
			try {selection = dojo.global().getSelection();}
			catch (e) {}
			if(selection){
				var range = selection.getRangeAt(0);
				bookmark = range.cloneRange();
			}else{
				dojo.debug("No idea how to store the current selection for this browser!");
			}
		}
		return bookmark;
	},
	moveToBookmark: function(/*Object*/bookmark){
		// summary: Moves current selection to a bookmark
		// bookmark: this should be a returned object from dojo.html.selection.getBookmark()
		var _document = dojo.doc();
		if(_document["selection"]){ // IE
			var range = _document.selection.createRange();
			 range.moveToBookmark(bookmark);
			 range.select();
		}else{ //Moz/W3C
			var selection;
			try {selection = dojo.global().getSelection();}
			catch (e) {}
			if(selection && selection['removeAllRanges']){
				selection.removeAllRanges() ;
				selection.addRange(bookmark) ;
			}else{
				dojo.debug("No idea how to restore selection for this browser!");
			}
		}
	},
	collapse: function(/*Boolean*/beginning) {
		// summary: clear current selection
		if(dojo.global()['getSelection']){
			var selection = dojo.global().getSelection();
			if(selection.removeAllRanges){ // Mozilla
				if(beginning){
					selection.collapseToStart();
				}else{
					selection.collapseToEnd();
				}
			}else{ // Safari
				// pulled from WebCore/ecma/kjs_window.cpp, line 2536
				 dojo.global().getSelection().collapse(beginning);
			}
		}else if(dojo.doc().selection){ // IE
			var range = dojo.doc().selection.createRange();
			range.collapse(beginning);
			range.select();
		}
	},
	remove: function() {
		// summary: delete current selection
		if(dojo.doc().selection) { //IE
			var selection = dojo.doc().selection;

			if ( selection.type.toUpperCase() != "NONE" ){
				selection.clear();
			}
		
			return selection;
		}else{
			var selection = dojo.global().getSelection();

			for ( var i = 0; i < selection.rangeCount; i++ ){
				selection.getRangeAt(i).deleteContents();
			}
		
			return selection;
		}
	}
});

dojo.provide("dojo.html.iframe");


// thanks burstlib!
dojo.html.iframeContentWindow = function(/* HTMLIFrameElement */iframe_el) {
	//	summary
	//	returns the window reference of the passed iframe
	var win = dojo.html.getDocumentWindow(dojo.html.iframeContentDocument(iframe_el)) ||
		// Moz. TODO: is this available when defaultView isn't?
		dojo.html.iframeContentDocument(iframe_el).__parent__ ||
		(iframe_el.name && document.frames[iframe_el.name]) || null;
	return win;	//	Window
}

dojo.html.iframeContentDocument = function(/* HTMLIFrameElement */iframe_el){
	//	summary
	//	returns a reference to the document object inside iframe_el
	var doc = iframe_el.contentDocument // W3
		|| ((iframe_el.contentWindow)&&(iframe_el.contentWindow.document))	// IE
		|| ((iframe_el.name)&&(document.frames[iframe_el.name])&&(document.frames[iframe_el.name].document)) 
		|| null;
	return doc;	//	HTMLDocument
}

dojo.html.BackgroundIframe = function(/* HTMLElement */node) {
	//	summary
	//	For IE z-index schenanigans
	//	Two possible uses:
	//	1. new dojo.html.BackgroundIframe(node)
	//		Makes a background iframe as a child of node, that fills area (and position) of node
	//	2. new dojo.html.BackgroundIframe()
	//		Attaches frame to dojo.body().  User must call size() to set size.
	if(dojo.render.html.ie55 || dojo.render.html.ie60) {
		var html="<iframe src='javascript:false'"
			+ " style='position: absolute; left: 0px; top: 0px; width: 100%; height: 100%;"
			+ "z-index: -1; filter:Alpha(Opacity=\"0\");' "
			+ ">";
		this.iframe = dojo.doc().createElement(html);
		this.iframe.tabIndex = -1; // Magic to prevent iframe from getting focus on tab keypress - as style didnt work.
		if(node){
			node.appendChild(this.iframe);
			this.domNode=node;
		}else{
			dojo.body().appendChild(this.iframe);
			this.iframe.style.display="none";
		}
	}
}
dojo.lang.extend(dojo.html.BackgroundIframe, {
	iframe: null,
	onResized: function(){
		//	summary
		//	Resize event handler.
		// TODO: this function shouldn't be necessary but setting width=height=100% doesn't work!
		if(this.iframe && this.domNode && this.domNode.parentNode){ // No parentElement if onResized() timeout event occurs on a removed domnode
			var outer = dojo.html.getMarginBox(this.domNode);
			if (outer.width  == 0 || outer.height == 0 ){
				dojo.lang.setTimeout(this, this.onResized, 100);
				return;
			}
			this.iframe.style.width = outer.width + "px";
			this.iframe.style.height = outer.height + "px";
		}
	},

	size: function(/* HTMLElement */node) {
		// summary:
		//		Call this function if the iframe is connected to dojo.body()
		//		rather than the node being shadowed 

		//	(TODO: erase)
		if(!this.iframe){ return; }
		var coords = dojo.html.toCoordinateObject(node, true, dojo.html.boxSizing.BORDER_BOX);
		with(this.iframe.style){
			width = coords.width + "px";
			height = coords.height + "px";
			left = coords.left + "px";
			top = coords.top + "px";
		}
	},

	setZIndex: function(/* HTMLElement */node){
		//	summary
		//	Sets the z-index of the background iframe.
		if(!this.iframe){ return; }
		if(dojo.dom.isNode(node)){
			this.iframe.style.zIndex = dojo.html.getStyle(node, "z-index") - 1;
		}else if(!isNaN(node)){
			this.iframe.style.zIndex = node;
		}
	},

	show: function(){
		//	summary:
		//		show the iframe
		if(this.iframe){ 
			this.iframe.style.display = "block";
		}
	},

	hide: function(){
		//	summary:
		//		hide the iframe
		if(this.iframe){ 
			this.iframe.style.display = "none";
		}
	},

	remove: function(){
		//	summary:
		//		remove the iframe
		if(this.iframe){
			dojo.html.removeNode(this.iframe, true);
			delete this.iframe;
			this.iframe=null;
		}
	}
});

dojo.provide("dojo.widget.PopupContainer");

dojo.require("dojo.html.style");



dojo.require("dojo.event.*");



dojo.declare(
	"dojo.widget.PopupContainerBase",
	null,
	function(){
		this.queueOnAnimationFinish = [];
	},
{
	// summary:
	//		PopupContainerBase is the mixin class which provide popup behaviors:
	//		it can open in a given position x,y or around a given node.
	//		In addition, it handles animation and IE bleed through workaround.
	// description:
	//		This class can not be used standalone: it should be mixed-in to a
	//		dojo.widget.HtmlWidget. Use PopupContainer instead if you want a
	//		a standalone popup widget

	// isShowingNow: Boolean: whether this popup is shown
	isShowingNow: false,

	// currentSubpopup: Widget: the shown sub popup if any
	currentSubpopup: null,

	// beginZIndex: Integer: the minimal popup zIndex
	beginZIndex: 1000,

	// parentPopup: Widget: parent popup widget
	parentPopup: null,

	// parent: Widget: the widget that caused me to be displayed; the logical parent.
	parent: null,

	// popupIndex: Integer: level of sub popup
	popupIndex: 0,

	// aroundBox: dojo.html.boxSizing: which bounding box to use for open aroundNode. By default use BORDER box of the aroundNode
	aroundBox: dojo.html.boxSizing.BORDER_BOX,

	// openedForWindow: Object: in which window the open() is triggered
	openedForWindow: null,

	processKey: function(/*Event*/evt){
		// summary: key event handler
		return false;
	},

	applyPopupBasicStyle: function(){
		// summary: apply necessary css rules to the top domNode
		// description:
		//		this function should be called in sub class where a custom
		//		templateString/templateStringPath is used (see Tooltip widget)
		with(this.domNode.style){
			display = 'none';
			position = 'absolute';
		}
	},

	aboutToShow: function() {
		// summary: connect to this stub to modify the content of the popup
	},

	open: function(/*Integer*/x, /*Integer*/y, /*DomNode*/parent, /*Object*/explodeSrc, /*String?*/orient, /*Array?*/padding){
		// summary:
		//		Open the popup at position (x,y), relative to dojo.body()
	 	//		Or open(node, parent, explodeSrc, aroundOrient) to open
	 	//		around node
		if (this.isShowingNow){ return; }

		// if I click right button and menu is opened, then it gets 2 commands: close -> open
		// so close enables animation and next "open" is put to queue to occur at new location
		if(this.animationInProgress){
			this.queueOnAnimationFinish.push(this.open, arguments);
			return;
		}

		this.aboutToShow();

		var around = false, node, aroundOrient;
		if(typeof x == 'object'){
			node = x;
			aroundOrient = explodeSrc;
			explodeSrc = parent;
			parent = y;
			around = true;
		}

		// save this so that the focus can be returned
		this.parent = parent;

		// for unknown reasons even if the domNode is attached to the body in postCreate(),
		// it's not attached here, so have to attach it here.
		dojo.body().appendChild(this.domNode);

		// if explodeSrc isn't specified then explode from my parent widget
		explodeSrc = explodeSrc || parent["domNode"] || [];

		//keep track of parent popup to decided whether this is a top level popup
		var parentPopup = null;
		this.isTopLevel = true;
		while(parent){
			if(parent !== this && (parent.setOpenedSubpopup != undefined && parent.applyPopupBasicStyle != undefined)){
				parentPopup = parent;
				this.isTopLevel = false;
				parentPopup.setOpenedSubpopup(this);
				break;
			}
			parent = parent.parent;
		}

		this.parentPopup = parentPopup;
		this.popupIndex = parentPopup ? parentPopup.popupIndex + 1 : 1;

		if(this.isTopLevel){
			var button = dojo.html.isNode(explodeSrc) ? explodeSrc : null;
			dojo.widget.PopupManager.opened(this, button);
		}

		//Store the current selection and restore it before the action for a menu item
		//is executed. This is required as clicking on an menu item deselects current selection
		if(this.isTopLevel && !dojo.withGlobal(this.openedForWindow||dojo.global(), dojo.html.selection.isCollapsed)){
			this._bookmark = dojo.withGlobal(this.openedForWindow||dojo.global(), dojo.html.selection.getBookmark);
		}else{
			this._bookmark = null;
		}

		//convert explodeSrc from format [x, y] to
		//{left: x, top: y, width: 0, height: 0} which is the new
		//format required by dojo.html.toCoordinateObject
		if(explodeSrc instanceof Array){
			explodeSrc = {left: explodeSrc[0], top: explodeSrc[1], width: 0, height: 0};
		}

		// display temporarily, and move into position, then hide again
		with(this.domNode.style){
			display="";
			zIndex = this.beginZIndex + this.popupIndex;
		}

		if(around){
			this.move(node, padding, aroundOrient);
		}else{
			this.move(x, y, padding, orient);
		}
		this.domNode.style.display="none";

		this.explodeSrc = explodeSrc;

		// then use the user defined method to display it
		this.show();

		this.isShowingNow = true;
	},

	// TODOC: move(node, padding, aroundOrient) how to do this?
	move: function(/*Int*/x, /*Int*/y, /*Integer?*/padding, /*String?*/orient){
		// summary: calculate where to place the popup

		var around = (typeof x == "object");
		if(around){
			var aroundOrient=padding;
			var node=x;
			padding=y;
			if(!aroundOrient){ //By default, attempt to open above the aroundNode, or below
				aroundOrient = {'BL': 'TL', 'TL': 'BL'};
			}
			dojo.html.placeOnScreenAroundElement(this.domNode, node, padding, this.aroundBox, aroundOrient);
		}else{
			if(!orient){ orient = 'TL,TR,BL,BR';}
			dojo.html.placeOnScreen(this.domNode, x, y, padding, true, orient);
		}
	},

	close: function(/*Boolean?*/force){
		// summary: hide the popup
		if(force){
			this.domNode.style.display="none";
		}

		// If we are in the process of opening the menu and we are asked to close it
		if(this.animationInProgress){
			this.queueOnAnimationFinish.push(this.close, []);
			return;
		}

		this.closeSubpopup(force);
		this.hide();
		if(this.bgIframe){
			this.bgIframe.hide();
			this.bgIframe.size({left: 0, top: 0, width: 0, height: 0});
		}
		if(this.isTopLevel){
			dojo.widget.PopupManager.closed(this);
		}
		this.isShowingNow = false;
		// return focus to the widget that opened the menu

		if(this.parent){
			setTimeout(
				dojo.lang.hitch(this, 
					function(){
						try{
							if(this.parent['focus']){
								this.parent.focus();
							}else{
								this.parent.domNode.focus(); 
							}
						}catch(e){dojo.debug("No idea how to focus to parent", e);}
					}
				),
				10
			);
		}


		//do not need to restore if current selection is not empty
		//(use keyboard to select a menu item)
		if(this._bookmark && dojo.withGlobal(this.openedForWindow||dojo.global(), dojo.html.selection.isCollapsed)){
			if(this.openedForWindow){
				this.openedForWindow.focus()
			}
			try{
				dojo.withGlobal(this.openedForWindow||dojo.global(), "moveToBookmark", dojo.html.selection, [this._bookmark]);
			}catch(e){
				/*squelch IE internal error, see http://trac.dojotoolkit.org/ticket/1984 */
			}
		}
		this._bookmark = null;
	},

	closeAll: function(/*Boolean?*/force){
		// summary: hide all popups including sub ones
		if (this.parentPopup){
			this.parentPopup.closeAll(force);
		}else{
			this.close(force);
		}
	},

	setOpenedSubpopup: function(/*Widget*/popup) {
		// summary: used by sub popup to set currentSubpopup in the parent popup
		this.currentSubpopup = popup;
	},

	closeSubpopup: function(/*Boolean?*/force) {
		// summary: close opened sub popup
		if(this.currentSubpopup == null){ return; }

		this.currentSubpopup.close(force);
		this.currentSubpopup = null;
	},

	onShow: function() {
		dojo.widget.PopupContainer.superclass.onShow.apply(this, arguments);
		// With some animation (wipe), after close, the size of the domnode is 0
		// and next time when shown, the open() function can not determine
		// the correct place to popup, so we store the opened size here and
		// set it after close (in function onHide())
		this.openedSize={w: this.domNode.style.width, h: this.domNode.style.height};
		// prevent IE bleed through
		if(dojo.render.html.ie){
			if(!this.bgIframe){
				this.bgIframe = new dojo.html.BackgroundIframe();
				this.bgIframe.setZIndex(this.domNode);
			}

			this.bgIframe.size(this.domNode);
			this.bgIframe.show();
		}
		this.processQueue();
	},

	processQueue: function() {
		// summary: do events from queue
		if (!this.queueOnAnimationFinish.length) return;

		var func = this.queueOnAnimationFinish.shift();
		var args = this.queueOnAnimationFinish.shift();

		func.apply(this, args);
	},

	onHide: function() {
		dojo.widget.HtmlWidget.prototype.onHide.call(this);

		//restore size of the domnode, see comment in
		//function onShow()
		if(this.openedSize){
			with(this.domNode.style){
				width=this.openedSize.w;
				height=this.openedSize.h;
			}
		}

		this.processQueue();
	}
});

dojo.widget.defineWidget(
	"dojo.widget.PopupContainer",
	[dojo.widget.HtmlWidget, dojo.widget.PopupContainerBase], {
		// summary: dojo.widget.PopupContainer is the widget version of dojo.widget.PopupContainerBase
		isContainer: true,
		fillInTemplate: function(){
			this.applyPopupBasicStyle();
			dojo.widget.PopupContainer.superclass.fillInTemplate.apply(this, arguments);
		}
	});


dojo.widget.PopupManager = new function(){
	// summary:
	//		the popup manager makes sure we don't have several popups
	//		open at once. the root popup in an opening sequence calls
	//		opened(). when a root menu closes it calls closed(). then
	//		everything works. lovely.

	this.currentMenu = null;
	this.currentButton = null;		// button that opened current menu (if any)
	this.currentFocusMenu = null;	// the (sub)menu which receives key events
	this.focusNode = null;
	this.registeredWindows = [];

	this.registerWin = function(/*Window*/win){
		// summary: register a window so that when clicks/scroll in it, the popup can be closed automatically
		if(!win.__PopupManagerRegistered)
		{
			dojo.event.connect(win.document, 'onmousedown', this, 'onClick');
			dojo.event.connect(win, "onscroll", this, "onClick");
			dojo.event.connect(win.document, "onkey", this, 'onKey');
			win.__PopupManagerRegistered = true;
			this.registeredWindows.push(win);
		}
	};

	/*

	*/
	this.registerAllWindows = function(/*Window*/targetWindow){
		// summary:
		//		This function register all the iframes and the top window,
		//		so that whereever the user clicks in the page, the popup
		//		menu will be closed
		//		In case you add an iframe after onload event, please call
		//		dojo.widget.PopupManager.registerWin manually

		//starting from window.top, clicking everywhere in this page
		//should close popup menus
		if(!targetWindow) { //see comment below
			targetWindow = dojo.html.getDocumentWindow(window.top && window.top.document || window.document);
		}

		this.registerWin(targetWindow);

		for (var i = 0; i < targetWindow.frames.length; i++){
			try{
				//do not remove  dojo.html.getDocumentWindow, see comment in it
				var win = dojo.html.getDocumentWindow(targetWindow.frames[i].document);
				if(win){
					this.registerAllWindows(win);
				}
			}catch(e){ /* squelch error for cross domain iframes */ }
		}
	};

	this.unRegisterWin = function(/*Window*/win){
		// summary: remove listeners on the registered window
		if(win.__PopupManagerRegistered)
		{
			dojo.event.disconnect(win.document, 'onmousedown', this, 'onClick');
			dojo.event.disconnect(win, "onscroll", this, "onClick");
			dojo.event.disconnect(win.document, "onkey", this, 'onKey');
			win.__PopupManagerRegistered = false;
		}
	};

	this.unRegisterAllWindows = function(){
		// summary: remove listeners on all the registered windows
		for(var i=0;i<this.registeredWindows.length;++i){
			this.unRegisterWin(this.registeredWindows[i]);
		}
		this.registeredWindows = [];
	};

	dojo.addOnLoad(this, "registerAllWindows");
	dojo.addOnUnload(this, "unRegisterAllWindows");

	this.closed = function(/*Widget*/menu){
		// summary: notify the manager that menu is closed
		if (this.currentMenu == menu){
			this.currentMenu = null;
			this.currentButton = null;
			this.currentFocusMenu = null;
		}
	};

	this.opened = function(/*Widget*/menu, /*DomNode*/button){
		// summary: sets the current opened popup
		if (menu == this.currentMenu){ return; }

		if (this.currentMenu){
			this.currentMenu.close();
		}

		this.currentMenu = menu;
		this.currentFocusMenu = menu;
		this.currentButton = button;
	};

	this.setFocusedMenu = function(/*Widget*/menu){
		// summary:
		// 		Set the current focused popup, This is used by popups which supports keyboard navigation
		this.currentFocusMenu = menu;
	};

	this.onKey = function(/*Event*/e){
		if (!e.key) { return; }
		if(!this.currentMenu || !this.currentMenu.isShowingNow){ return; }

		// loop from child menu up ancestor chain, ending at button that spawned the menu
		var m = this.currentFocusMenu;
		while (m){
			if(m.processKey(e)){
				e.preventDefault();
				e.stopPropagation();
				break;
			}
			m = m.parentPopup || m.parentMenu;
		}		
	},

	this.onClick = function(/*Event*/e){
		if (!this.currentMenu){ return; }

		var scrolloffset = dojo.html.getScroll().offset;

		// starting from the base menu, perform a hit test
		// and exit when one succeeds

		var m = this.currentMenu;

		while (m){
			if(dojo.html.overElement(m.domNode, e) || dojo.html.isDescendantOf(e.target, m.domNode)){
				return;
			}
			m = m.currentSubpopup;
		}

		// Also, if user clicked the button that opened this menu, then
		// that button will send the menu a close() command, so this code
		// shouldn't try to close the menu.  Closing twice messes up animation.
		if (this.currentButton && dojo.html.overElement(this.currentButton, e)){
			return;
		}

		// the click didn't fall within the open menu tree
		// so close it

		this.currentMenu.closeAll(true);
	};
}

dojo.provide("dojo.widget.DropdownContainer");



dojo.require("dojo.event.*");

dojo.require("dojo.html.display");



dojo.widget.defineWidget(
	"dojo.widget.DropdownContainer",
	dojo.widget.HtmlWidget,
	{
		// summary:
		//		provides an input box and a button for a dropdown.
		//		In subclass, the dropdown can be specified.

		// inputWidth: String: width of the input box
		inputWidth: "7em",

		// id: String: id of this widget
		id: "",

		// inputId: String: id of the input box
		inputId: "",

		// inputName: String: name of the input box
		inputName: "",

		// iconURL: dojo.uri.Uri: icon for the dropdown button
		iconURL: dojo.uri.moduleUri("dojo.widget", "templates/images/combo_box_arrow.png"),

		// copyClass:
		//		should we use the class properties on the source node instead
		//		of our own styles?
		copyClasses: false,

		// iconAlt: dojo.uri.Uri: alt text for the dropdown button icon
		iconAlt: "",

		// containerToggle: String: toggle property of the dropdown
		containerToggle: "plain",

		// containerToggleDuration: Integer: toggle duration property of the dropdown
		containerToggleDuration: 150,

		templateString: '<span style="white-space:nowrap"><input type="hidden" name="" value="" dojoAttachPoint="valueNode" /><input name="" type="text" value="" style="vertical-align:middle;" dojoAttachPoint="inputNode" autocomplete="off" /> <img src="${this.iconURL}" alt="${this.iconAlt}" dojoAttachEvent="onclick:onIconClick" dojoAttachPoint="buttonNode" style="vertical-align:middle; cursor:pointer; cursor:hand" /></span>',
		templateCssPath: "",
		isContainer: true,

		attachTemplateNodes: function(){
			// summary: use attachTemplateNodes to specify containerNode, as fillInTemplate is too late for this
			dojo.widget.DropdownContainer.superclass.attachTemplateNodes.apply(this, arguments);
			this.popup = dojo.widget.createWidget("PopupContainer", {toggle: this.containerToggle, toggleDuration: this.containerToggleDuration});
			this.containerNode = this.popup.domNode;
		},

		fillInTemplate: function(args, frag){
			this.domNode.appendChild(this.popup.domNode);
			if(this.id) { this.domNode.id = this.id; }
			if(this.inputId){ this.inputNode.id = this.inputId; }
			if(this.inputName){ this.inputNode.name = this.inputName; }
			this.inputNode.style.width = this.inputWidth;
			this.inputNode.disabled = this.disabled;

			if(this.copyClasses){
				this.inputNode.style = "";
				this.inputNode.className = this.getFragNodeRef(frag).className;
			}


			dojo.event.connect(this.inputNode, "onchange", this, "onInputChange");
		},

		onIconClick: function(/*Event*/ evt){
			if(this.disabled) return;
			if(!this.popup.isShowingNow){
				this.popup.open(this.inputNode, this, this.buttonNode);
			}else{
				this.popup.close();
			}
		},

		hideContainer: function(){
			// summary: hide the dropdown
			if(this.popup.isShowingNow){
				this.popup.close();
			}
		},

		onInputChange: function(){
			// summary: signal for changes in the input box
		},
		
		enable: function() {
			// summary: enable this widget to accept user input
			this.inputNode.disabled = false;
			dojo.widget.DropdownContainer.superclass.enable.apply(this, arguments);
		},
		
		disable: function() {
			// summary: lock this widget so that the user can't change the value
			this.inputNode.disabled = true;
			dojo.widget.DropdownContainer.superclass.disable.apply(this, arguments);
		}
	}
);

dojo.provide("dojo.widget.html.stabile");

dojo.widget.html.stabile = {
	// summary: Maintain state of widgets when user hits back/forward button

	// Characters to quote in single-quoted regexprs
	_sqQuotables: new RegExp("([\\\\'])", "g"),

	// Current depth.
	_depth: 0,

	// Set to true when calling v.toString, to sniff for infinite
	// recursion.
	_recur: false,

	// Levels of nesting of Array and object displays.
	// If when >= depth, no display or array or object internals.
	depthLimit: 2
};

//// PUBLIC METHODS

dojo.widget.html.stabile.getState = function(id){
	// summary
	//	Get the state stored for the widget with the given ID, or undefined
	//	if none.

	dojo.widget.html.stabile.setup();
	return dojo.widget.html.stabile.widgetState[id];
}

dojo.widget.html.stabile.setState = function(id, state, isCommit){
	// summary
	//		Set the state stored for the widget with the given ID.  If isCommit
	//		is true, commits all widget state to more stable storage.

	dojo.widget.html.stabile.setup();
	dojo.widget.html.stabile.widgetState[id] = state;
	if(isCommit){
		dojo.widget.html.stabile.commit(dojo.widget.html.stabile.widgetState);
	}
}

dojo.widget.html.stabile.setup = function(){
	// summary
	//		Sets up widgetState: a hash keyed by widgetId, maps to an object
	//		or array writable with "describe".  If there is data in the widget
	//		storage area, use it, otherwise initialize an empty object.

	if(!dojo.widget.html.stabile.widgetState){
		var text = dojo.widget.html.stabile._getStorage().value;
		dojo.widget.html.stabile.widgetState = text ? dj_eval("("+text+")") : {};
	}
}

dojo.widget.html.stabile.commit = function(state){
	// summary
	//		Commits all widget state to more stable storage, so if the user
	//		navigates away and returns, it can be restored.

	dojo.widget.html.stabile._getStorage().value = dojo.widget.html.stabile.description(state);
}

dojo.widget.html.stabile.description = function(v, showAll){
	// summary
	//		Return a JSON "description string" for the given value.
	//		Supports only core JavaScript types with literals, plus Date,
	//		and cyclic structures are unsupported.
	//		showAll defaults to false -- if true, this becomes a simple symbolic
	//		object dumper, but you cannot "eval" the output.

	// Save and later restore dojo.widget.html.stabile._depth;
	var depth = dojo.widget.html.stabile._depth;

	var describeThis = function() {
		 return this.description(this, true);
	} 
	
	try {

		if(v===void(0)){
			return "undefined";
		}
		if(v===null){
			return "null";
		}
		if(typeof(v)=="boolean" || typeof(v)=="number"
		    || v instanceof Boolean || v instanceof Number){
			return v.toString();
		}

		if(typeof(v)=="string" || v instanceof String){
			// Quote strings and their contents as required.
			// Replacing by $& fails in IE 5.0
			var v1 = v.replace(dojo.widget.html.stabile._sqQuotables, "\\$1"); 
			v1 = v1.replace(/\n/g, "\\n");
			v1 = v1.replace(/\r/g, "\\r");
			// Any other important special cases?
			return "'"+v1+"'";
		}

		if(v instanceof Date){
			// Create a data constructor.
			return "new Date("+d.getFullYear+","+d.getMonth()+","+d.getDate()+")";
		}

		var d;
		if(v instanceof Array || v.push){
			// "push" test needed for KHTML/Safari, don't know why -cp

			if(depth>=dojo.widget.html.stabile.depthLimit)
			  return "[ ... ]";

			d = "[";
			var first = true;
			dojo.widget.html.stabile._depth++;
			for(var i=0; i<v.length; i++){
				// Skip functions and undefined values
				// if(v[i]==undef || typeof(v[i])=="function")
				//   continue;
				if(first){
					first = false;
				}else{
					d += ",";
				}
				d+=arguments.callee(v[i], showAll);
			}
			return d+"]";
		}

		if(v.constructor==Object
		    || v.toString==describeThis){
			if(depth>=dojo.widget.html.stabile.depthLimit)
			  return "{ ... }";

			// Instanceof Hash is good, or if we just use Objects,
			// we can say v.constructor==Object.
			// IE (5?) lacks hasOwnProperty, but perhaps objects do not always
			// have prototypes??
			if(typeof(v.hasOwnProperty)!="function" && v.prototype){
				throw new Error("description: "+v+" not supported by script engine");
			}
			var first = true;
			d = "{";
			dojo.widget.html.stabile._depth++;
			for(var key in v){
				// Skip values that are functions or undefined.
				if(v[key]==void(0) || typeof(v[key])=="function")
					continue;
				if(first){
					first = false;
				}else{
					d += ", ";
				}
				var kd = key;
				// If the key is not a legal identifier, use its description.
				// For strings this will quote the stirng.
				if(!kd.match(/^[a-zA-Z_][a-zA-Z0-9_]*$/)){
					kd = arguments.callee(key, showAll);
				}
				d += kd+": "+arguments.callee(v[key], showAll);
			}
			return d+"}";
		}

		if(showAll){
			if(dojo.widget.html.stabile._recur){
				// Save the original definitions of toString;
				var objectToString = Object.prototype.toString;
				return objectToString.apply(v, []);
			}else{
				dojo.widget.html.stabile._recur = true;
				return v.toString();
			}
		}else{
			// log("Description? "+v.toString()+", "+typeof(v));
			throw new Error("Unknown type: "+v);
			return "'unknown'";
		}

	} finally {
		// Always restore the global current depth.
		dojo.widget.html.stabile._depth = depth;
	}

}



//// PRIVATE TO MODULE

dojo.widget.html.stabile._getStorage = function(){
	// summary
	//	Gets an object (form field) with a read/write "value" property.

	if (dojo.widget.html.stabile.dataField) {
		return dojo.widget.html.stabile.dataField;
	}
	var form = document.forms._dojo_form;
	return dojo.widget.html.stabile.dataField = form ? form.stabile : {value: ""};
}


dojo.provide("dojo.widget.Dialog");



dojo.require("dojo.event.*");


dojo.require("dojo.html.display");


dojo.declare(
	"dojo.widget.ModalDialogBase", 
	null,
	{
		// summary
		//	Mixin for widgets implementing a modal dialog

		isContainer: true,

		// focusElement: String
		//	provide a focusable element or element id if you need to
		//	work around FF's tendency to send focus into outer space on hide
		focusElement: "",

		// bgColor: String
		//	color of viewport when displaying a dialog
		bgColor: "black",
		
		// bgOpacity: Number
		//	opacity (0~1) of viewport color (see bgColor attribute)
		bgOpacity: 0.4,

		// followScroll: Boolean
		//	if true, readjusts the dialog (and dialog background) when the user moves the scrollbar
		followScroll: true,

		// closeOnBackgroundClick: Boolean
		//	clicking anywhere on the background will close the dialog
		closeOnBackgroundClick: false,

		trapTabs: function(/*Event*/ e){
			// summary
			//	callback on focus
			if(e.target == this.tabStartOuter) {
				if(this._fromTrap) {
					this.tabStart.focus();
					this._fromTrap = false;
				} else {
					this._fromTrap = true;
					this.tabEnd.focus();
				}
			} else if (e.target == this.tabStart) {
				if(this._fromTrap) {
					this._fromTrap = false;
				} else {
					this._fromTrap = true;
					this.tabEnd.focus();
				}
			} else if(e.target == this.tabEndOuter) {
				if(this._fromTrap) {
					this.tabEnd.focus();
					this._fromTrap = false;
				} else {
					this._fromTrap = true;
					this.tabStart.focus();
				}
			} else if(e.target == this.tabEnd) {
				if(this._fromTrap) {
					this._fromTrap = false;
				} else {
					this._fromTrap = true;
					this.tabStart.focus();
				}
			}
		},

		clearTrap: function(/*Event*/ e) {
			// summary
			//	callback on blur
			var _this = this;
			setTimeout(function() {
				_this._fromTrap = false;
			}, 100);
		},

		postCreate: function() {
			// summary
			//	if the target mixin class already defined postCreate,
			//	dojo.widget.ModalDialogBase.prototype.postCreate.call(this)
			//	should be called in its postCreate()
			with(this.domNode.style){
				position = "absolute";
				zIndex = 999;
				display = "none";
				overflow = "visible";
			}
			var b = dojo.body();
			b.appendChild(this.domNode);

			// make background (which sits behind the dialog but above the normal text)
			this.bg = document.createElement("div");
			this.bg.className = "dialogUnderlay";
			with(this.bg.style){
				position = "absolute";
				left = top = "0px";
				zIndex = 998;
				display = "none";
			}
			b.appendChild(this.bg);
			this.setBackgroundColor(this.bgColor);

			this.bgIframe = new dojo.html.BackgroundIframe();
            if(this.bgIframe.iframe){
				with(this.bgIframe.iframe.style){
					position = "absolute";
					left = top = "0px";
					zIndex = 90;
					display = "none";
				}
			}

			if(this.closeOnBackgroundClick){
				dojo.event.kwConnect({srcObj: this.bg, srcFunc: "onclick",
					adviceObj: this, adviceFunc: "onBackgroundClick", once: true});
			}
		},

		uninitialize: function(){
			this.bgIframe.remove();
			dojo.html.removeNode(this.bg, true);
		},

		setBackgroundColor: function(/*String*/ color) {
			// summary
			//	changes background color specified by "bgColor" parameter
			//	usage:
			//		setBackgroundColor("black");
			//		setBackgroundColor(0xff, 0xff, 0xff);
			if(arguments.length >= 3) {
				color = new dojo.gfx.color.Color(arguments[0], arguments[1], arguments[2]);
			} else {
				color = new dojo.gfx.color.Color(color);
			}
			this.bg.style.backgroundColor = color.toString();
			return this.bgColor = color;	// String: the color
		},

		setBackgroundOpacity: function(/*Number*/ op) {
			// summary
			//	changes background opacity set by "bgOpacity" parameter
			if(arguments.length == 0) { op = this.bgOpacity; }
			dojo.html.setOpacity(this.bg, op);
			try {
				this.bgOpacity = dojo.html.getOpacity(this.bg);
			} catch (e) {
				this.bgOpacity = op;
			}
			return this.bgOpacity;	// Number: the opacity
		},

		_sizeBackground: function() {
			if(this.bgOpacity > 0) {
				
				var viewport = dojo.html.getViewport();
				var h = viewport.height;
				var w = viewport.width;
				with(this.bg.style){
					width = w + "px";
					height = h + "px";
				}
				var scroll_offset = dojo.html.getScroll().offset;
				this.bg.style.top = scroll_offset.y + "px";
				this.bg.style.left = scroll_offset.x + "px";
				// process twice since the scroll bar may have been removed
				// by the previous resizing
				var viewport = dojo.html.getViewport();
				if (viewport.width != w) { this.bg.style.width = viewport.width + "px"; }
				if (viewport.height != h) { this.bg.style.height = viewport.height + "px"; }
			}
			this.bgIframe.size(this.bg);
		},

		_showBackground: function() {
			if(this.bgOpacity > 0) {
				this.bg.style.display = "block";
			}
			if(this.bgIframe.iframe){
				this.bgIframe.iframe.style.display = "block";
			}
		},

		placeModalDialog: function() {
			// summary: position modal dialog in center of screen

			var scroll_offset = dojo.html.getScroll().offset;
			var viewport_size = dojo.html.getViewport();
			
			// find the size of the dialog (dialog needs to be showing to get the size)
			var mb;
			if(this.isShowing()){
				mb = dojo.html.getMarginBox(this.domNode);
			}else{
				dojo.html.setVisibility(this.domNode, false);
				dojo.html.show(this.domNode);
				mb = dojo.html.getMarginBox(this.domNode);
				dojo.html.hide(this.domNode);
				dojo.html.setVisibility(this.domNode, true);
			}
			
			var x = scroll_offset.x + (viewport_size.width - mb.width)/2;
			var y = scroll_offset.y + (viewport_size.height - mb.height)/2;
			with(this.domNode.style){
				left = x + "px";
				top = y + "px";
			}
		},

		_onKey: function(/*Event*/ evt){
			if (evt.key){
				// see if the key is for the dialog
				var node = evt.target;
				while (node != null){
					if (node == this.domNode){
						return; // yes, so just let it go
					}
					node = node.parentNode;
				}
				// this key is for the disabled document window
				if (evt.key != evt.KEY_TAB){ // allow tabbing into the dialog for a11y
					dojo.event.browser.stopEvent(evt);
				// opera won't tab to a div
				}else if (!dojo.render.html.opera){
					try {
						this.tabStart.focus(); 
					} catch(e){}
				}
			}
		},

		showModalDialog: function() {
			// summary
			//	call this function in show() of subclass before calling superclass.show()
			if (this.followScroll && !this._scrollConnected){
				this._scrollConnected = true;
				dojo.event.connect(window, "onscroll", this, "_onScroll");
			}
			dojo.event.connect(document.documentElement, "onkey", this, "_onKey");

			this.placeModalDialog();
			this.setBackgroundOpacity();
			this._sizeBackground();
			this._showBackground();
			this._fromTrap = true; 

			// set timeout to allow the browser to render dialog 
			setTimeout(dojo.lang.hitch(this, function(){
				try{
					this.tabStart.focus();
				}catch(e){}
			}), 50);

		},

		hideModalDialog: function(){
			// summary
			//	call this function in hide() of subclass

			// workaround for FF focus going into outer space
			if (this.focusElement) {
				dojo.byId(this.focusElement).focus(); 
				dojo.byId(this.focusElement).blur();
			}

			this.bg.style.display = "none";
			this.bg.style.width = this.bg.style.height = "1px";
            if(this.bgIframe.iframe){
				this.bgIframe.iframe.style.display = "none";
			}

			dojo.event.disconnect(document.documentElement, "onkey", this, "_onKey");
			if (this._scrollConnected){
				this._scrollConnected = false;
				dojo.event.disconnect(window, "onscroll", this, "_onScroll");
			}
		},

		_onScroll: function(){
			var scroll_offset = dojo.html.getScroll().offset;
			this.bg.style.top = scroll_offset.y + "px";
			this.bg.style.left = scroll_offset.x + "px";
			this.placeModalDialog();
		},

		checkSize: function() {
			if(this.isShowing()){
				this._sizeBackground();
				this.placeModalDialog();
				this.onResized();
			}
		},
		
		onBackgroundClick: function(){
			// summary
			//		Callback on background click.
			//		Clicking anywhere on the background will close the dialog, but only
			//		if the dialog doesn't have an explicit close button, and only if
			//		the dialog doesn't have a blockDuration.
			if(this.lifetime - this.timeRemaining >= this.blockDuration){ return; }
			this.hide();
		}
	});

dojo.widget.defineWidget(
	"dojo.widget.Dialog",
	[dojo.widget.ContentPane, dojo.widget.ModalDialogBase],
	{
		// summary
		//	Pops up a modal dialog window, blocking access to the screen and also graying out the screen
		//	Dialog is extended from ContentPane so it supports all the same parameters (href, etc.)

		templateString:"<div id=\"${this.widgetId}\" class=\"dojoDialog\" dojoattachpoint=\"wrapper\">\n\t<span dojoattachpoint=\"tabStartOuter\" dojoonfocus=\"trapTabs\" dojoonblur=\"clearTrap\"\ttabindex=\"0\"></span>\n\t<span dojoattachpoint=\"tabStart\" dojoonfocus=\"trapTabs\" dojoonblur=\"clearTrap\" tabindex=\"0\"></span>\n\t<div dojoattachpoint=\"containerNode\" style=\"position: relative; z-index: 2;\"></div>\n\t<span dojoattachpoint=\"tabEnd\" dojoonfocus=\"trapTabs\" dojoonblur=\"clearTrap\" tabindex=\"0\"></span>\n\t<span dojoattachpoint=\"tabEndOuter\" dojoonfocus=\"trapTabs\" dojoonblur=\"clearTrap\" tabindex=\"0\"></span>\n</div>\n",

		// blockDuration: Integer
		//	number of seconds for which the user cannot dismiss the dialog
		blockDuration: 0,
		
		// lifetime: Integer
		//	if set, this controls the number of seconds the dialog will be displayed before automatically disappearing
		lifetime: 0,

		// closeNode: String
		//	Id of button or other dom node to click to close this dialog
		closeNode: "",

		postMixInProperties: function(){
			dojo.widget.Dialog.superclass.postMixInProperties.apply(this, arguments);
			if(this.closeNode){
				this.setCloseControl(this.closeNode);
			}
		},

		postCreate: function(){
			dojo.widget.Dialog.superclass.postCreate.apply(this, arguments);
			dojo.widget.ModalDialogBase.prototype.postCreate.apply(this, arguments);
		},

		show: function() {
			if(this.lifetime){
				this.timeRemaining = this.lifetime;
				if(this.timerNode){
					this.timerNode.innerHTML = Math.ceil(this.timeRemaining/1000);
				}
				if(this.blockDuration && this.closeNode){
					if(this.lifetime > this.blockDuration){
						this.closeNode.style.visibility = "hidden";
					}else{
						this.closeNode.style.display = "none";
					}
				}
				if (this.timer) {
					clearInterval(this.timer);
				}
				this.timer = setInterval(dojo.lang.hitch(this, "_onTick"), 100);
			}

			this.showModalDialog();
			dojo.widget.Dialog.superclass.show.call(this);
		},

		onLoad: function(){
			// when href is specified we need to reposition
			// the dialog after the data is loaded
			this.placeModalDialog();
			dojo.widget.Dialog.superclass.onLoad.call(this);
		},
		
		fillInTemplate: function(){
			// dojo.event.connect(this.domNode, "onclick", this, "killEvent");
		},

		hide: function(){
			this.hideModalDialog();
			dojo.widget.Dialog.superclass.hide.call(this);

			if(this.timer){
				clearInterval(this.timer);
			}
		},
		
		setTimerNode: function(node){
			// summary
			//	specify into which node to write the remaining # of seconds
			// TODO: make this a parameter too
			this.timerNode = node;
		},

		setCloseControl: function(/*String|DomNode*/ node) {
			// summary
			//	Specify which node is the close button for this dialog.
			//	If no close node is specified then clicking anywhere on the screen will close the dialog.
			this.closeNode = dojo.byId(node);
			dojo.event.connect(this.closeNode, "onclick", this, "hide");
		},

		setShowControl: function(/*String|DomNode*/ node) {
			// summary
			//	when specified node is clicked, show this dialog
			// TODO: make this a parameter too
			node = dojo.byId(node);
			dojo.event.connect(node, "onclick", this, "show");
		},

		_onTick: function(){
			// summary
			//	callback every second that the timer clicks
			if(this.timer){
				this.timeRemaining -= 100;
				if(this.lifetime - this.timeRemaining >= this.blockDuration){
					// TODO: this block of code is executing over and over again, rather than just once
					if(this.closeNode){
						this.closeNode.style.visibility = "visible";
					}
				}
				if(!this.timeRemaining){
					clearInterval(this.timer);
					this.hide();
				}else if(this.timerNode){
					this.timerNode.innerHTML = Math.ceil(this.timeRemaining/1000);
				}
			}
		}
	}
);

dojo.provide("dojo.widget.ComboBox");


dojo.require("dojo.event.*");

dojo.require("dojo.html.*");
dojo.require("dojo.string");



dojo.declare(
	"dojo.widget.incrementalComboBoxDataProvider",
	null,
	function(options){
		// summary:
		//		Reference implementation / interface for Combobox incremental data provider.
		//		This class takes a search string and returns values that match
		//		that search string.  The filtering of values (to find values matching given
		//		search string) is done on the server.
		//
		// options:
		//		Structure containing {dataUrl: "foo.js?search={searchString}"} or similar data.
		//		dataUrl is a URL that is passed the search string a returns a JSON structure
		//		showing the matching values, like [ ["Alabama","AL"], ["Alaska","AK"], ["American Samoa","AS"] ]

		this.searchUrl = options.dataUrl;

		// TODO: cache doesn't work
		this._cache = {};

		this._inFlight = false;
		this._lastRequest = null;

		// allowCache: Boolean
		//	Setting to use/not use cache for previously seen values
		//	TODO: caching doesn't work.
		//	TODO: read the setting for this value from the widget parameters
		this.allowCache = false;
	},
	{
		_addToCache: function(/*String*/ keyword, /*Array*/ data){
			if(this.allowCache){
				this._cache[keyword] = data;
			}
		},

		startSearch: function(/*String*/ searchStr, /*Function*/ callback){
			// summary:
			//		Start the search for patterns that match searchStr, and call
			//		specified callback functions with the results
			// searchStr:
			//		The characters the user has typed into the <input>.
			// callback:
			//		This function will be called with the result, as an
			//		array of label/value pairs (the value is used for the Select widget).  Example:
			//		[ ["Alabama","AL"], ["Alaska","AK"], ["American Samoa","AS"] ]

			if(this._inFlight){
				// FIXME: implement backoff!
			}
			var tss = encodeURIComponent(searchStr);
			var realUrl = dojo.string.substituteParams(this.searchUrl, {"searchString": tss});
			var _this = this;
			var request = this._lastRequest = dojo.io.bind({
				url: realUrl,
				method: "get",
				mimetype: "text/json",
				load: function(type, data, evt){
					_this._inFlight = false;
					if(!dojo.lang.isArray(data)){
						var arrData = [];
						for(var key in data){
							arrData.push([data[key], key]);
						}
						data = arrData;
					}
					_this._addToCache(searchStr, data);
					if (request == _this._lastRequest){
						callback(data);
					}
				}
			});
			this._inFlight = true;
		}
	}
);

dojo.declare(
	"dojo.widget.basicComboBoxDataProvider",
	null,
	function(/*Object*/ options, /*DomNode*/ node){
		// summary:
		//		Reference implementation / interface for Combobox data provider.
		//		This class takes a search string and returns values that match
		//		that search string.    All possible values for the combobox are downloaded
		//		on initialization, and then startSearch() runs locally,
		//		merely filting that downloaded list, to find values matching search string
		//
		//		NOTE: this data provider is designed as a naive reference
		//		implementation, and as such it is written more for readability than
		//		speed. A deployable data provider would implement lookups, search
		//		caching (and invalidation), and a significantly less naive data
		//		structure for storage of items.
		//
		//	options: Object
		//		Options object.  Example:
		//		{
		//			dataUrl: String (URL to query to get list of possible drop down values),
		//			setAllValues: Function (callback for setting initially selected value)
		//		}
		//		The return format for dataURL is (for example)
		//			[ ["Alabama","AL"], ["Alaska","AK"], ["American Samoa","AS"] ... ]
		//
		// node:
		//		Pointer to the domNode in the original markup.
		//		This is needed in the case when the list of values is embedded
		//		in the html like <select> <option>Alabama</option> <option>Arkansas</option> ...
		//		rather than specified as a URL.

		// _data: Array
		//		List of every possible value for the drop down list
		//		startSearch() simply searches this array and returns matching values.
		this._data = [];

		// searchLimit: Integer
		//		Maximum number of results to return.
		//		TODO: need to read this value from the widget parameters
		this.searchLimit = 30;

		// searchType: String
		//		Defines what values match the search string; see searchType parameter
		//		of ComboBox for details
		//		TODO: need to read this value from the widget parameters; the setting in ComboBox is being ignored.
		this.searchType = "STARTSTRING";

		// caseSensitive: Boolean
		//		Should search be case sensitive?
		//		TODO: this should be a parameter to combobox?
		this.caseSensitive = false;

		if(!dj_undef("dataUrl", options) && !dojo.string.isBlank(options.dataUrl)){
			this._getData(options.dataUrl);
		}else{
			// check to see if we can populate the list from <option> elements
			if((node)&&(node.nodeName.toLowerCase() == "select")){
				// NOTE: we're not handling <optgroup> here yet
				var opts = node.getElementsByTagName("option");
				var ol = opts.length;
				var data = [];
				for(var x=0; x<ol; x++){
					var text = opts[x].textContent || opts[x].innerText || opts[x].innerHTML;
					var keyValArr = [String(text), String(opts[x].value)];
					data.push(keyValArr);
					if(opts[x].selected){
						options.setAllValues(keyValArr[0], keyValArr[1]);
					}
				}
				this.setData(data);
			}
		}
	},
	{
		_getData: function(/*String*/ url){
			dojo.io.bind({
				url: url,
				load: dojo.lang.hitch(this, function(type, data, evt){
					if(!dojo.lang.isArray(data)){
						var arrData = [];
						for(var key in data){
							arrData.push([data[key], key]);
						}
						data = arrData;
					}
					this.setData(data);
				}),
				mimetype: "text/json"
			});
		},

		startSearch: function(/*String*/ searchStr, /*Function*/ callback){
			// summary:
			//		Start the search for patterns that match searchStr.
			// searchStr:
			//		The characters the user has typed into the <input>.
			// callback:
			//		This function will be called with the result, as an
			//		array of label/value pairs (the value is used for the Select widget).  Example:
			//		[ ["Alabama","AL"], ["Alaska","AK"], ["American Samoa","AS"] ]

			// FIXME: need to add timeout handling here!!
			this._performSearch(searchStr, callback);
		},

		_performSearch: function(/*String*/ searchStr, /*Function*/ callback){
			//
			//	NOTE: this search is LINEAR, which means that it exhibits perhaps
			//	the worst possible speed characteristics of any search type. It's
			//	written this way to outline the responsibilities and interfaces for
			//	a search.
			//
			var st = this.searchType;
			// FIXME: this is just an example search, which means that we implement
			// only a linear search without any of the attendant (useful!) optimizations
			var ret = [];
			if(!this.caseSensitive){
				searchStr = searchStr.toLowerCase();
			}
			for(var x=0; x<this._data.length; x++){
				if((this.searchLimit > 0)&&(ret.length >= this.searchLimit)){
					break;
				}
				// FIXME: we should avoid copies if possible!
				var dataLabel = new String((!this.caseSensitive) ? this._data[x][0].toLowerCase() : this._data[x][0]);
				if(dataLabel.length < searchStr.length){
					// this won't ever be a good search, will it? What if we start
					// to support regex search?
					continue;
				}

				if(st == "STARTSTRING"){
					if(searchStr == dataLabel.substr(0, searchStr.length)){
						ret.push(this._data[x]);
					}
				}else if(st == "SUBSTRING"){
					// this one is a gimmie
					if(dataLabel.indexOf(searchStr) >= 0){
						ret.push(this._data[x]);
					}
				}else if(st == "STARTWORD"){
					// do a substring search and then attempt to determine if the
					// preceeding char was the beginning of the string or a
					// whitespace char.
					var idx = dataLabel.indexOf(searchStr);
					if(idx == 0){
						// implicit match
						ret.push(this._data[x]);
					}
					if(idx <= 0){
						// if we didn't match or implicily matched, march onward
						continue;
					}
					// otherwise, we have to go figure out if the match was at the
					// start of a word...
					// this code is taken almost directy from nWidgets
					var matches = false;
					while(idx!=-1){
						// make sure the match either starts whole string, or
						// follows a space, or follows some punctuation
						if(" ,/(".indexOf(dataLabel.charAt(idx-1)) != -1){
							// FIXME: what about tab chars?
							matches = true; break;
						}
						idx = dataLabel.indexOf(searchStr, idx+1);
					}
					if(!matches){
						continue;
					}else{
						ret.push(this._data[x]);
					}
				}
			}
			callback(ret);
		},

		setData: function(/*Array*/ pdata){
			// summary: set (or reset) the data and initialize lookup structures
			this._data = pdata;
		}
	}
);

dojo.widget.defineWidget(
	"dojo.widget.ComboBox",
	dojo.widget.HtmlWidget,
	{
		// summary:
		//		Auto-completing text box, and base class for Select widget.
		//
		//		The drop down box's values are populated from an class called
		//		a data provider, which returns a list of values based on the characters
		//		that the user has typed into the input box.
		//
		//		Some of the options to the ComboBox are actually arguments to the data
		//		provider.

		// forceValidOption: Boolean
		//		If true, only allow selection of strings in drop down list.
		//		If false, user can select a value from the drop down, or just type in
		//		any random value.
		forceValidOption: false,

		// searchType: String
		//		Argument to data provider.
		//		Specifies rule for matching typed in string w/list of available auto-completions.
		//			startString - look for auto-completions that start w/the specified string.
		//			subString - look for auto-completions containing the typed in string.
		//			startWord - look for auto-completions where any word starts w/the typed in string.
		searchType: "stringstart",

		// dataProvider: Object
		//		(Read only) reference to data provider object created for this combobox
		//		according to "dataProviderClass" argument.
		dataProvider: null,

		// autoComplete: Boolean
		//		If you type in a partial string, and then tab out of the <input> box,
		//		automatically copy the first entry displayed in the drop down list to
		//		the <input> field
		autoComplete: true,

		// searchDelay: Integer
		//		Delay in milliseconds between when user types something and we start
		//		searching based on that value
		searchDelay: 100,

		// dataUrl: String
		//		URL argument passed to data provider object (class name specified in "dataProviderClass")
		//		An example of the URL format for the default data provider is
		//		"remoteComboBoxData.js?search=%{searchString}"
		dataUrl: "",

		// fadeTime: Integer
		//		Milliseconds duration of fadeout for drop down box
		fadeTime: 200,

		// maxListLength: Integer
		//		 Limits list to X visible rows, scroll on rest
		maxListLength: 8,

		// mode: String
		//		Mode must be specified unless dataProviderClass is specified.
		//		"local" to inline search string, "remote" for JSON-returning live search
		//		or "html" for dumber live search.
		mode: "local",

		// selectedResult: Array
		//		(Read only) array specifying the value/label that the user selected
		selectedResult: null,

		// dataProviderClass: String
		//		Name of data provider class (code that maps a search string to a list of values)
		//		The class must match the interface demonstrated by dojo.widget.incrementalComboBoxDataProvider
		dataProviderClass: "",

		// buttonSrc: URI
		//		URI for the down arrow icon to the right of the input box.
		buttonSrc: dojo.uri.moduleUri("dojo.widget", "templates/images/combo_box_arrow.png"),

		// dropdownToggle: String
		//		Animation effect for showing/displaying drop down box
		dropdownToggle: "fade",

		templateString:"<span _=\"whitespace and CR's between tags adds &nbsp; in FF\"\n\tclass=\"dojoComboBoxOuter\"\n\t><input style=\"display:none\"  tabindex=\"-1\" name=\"\" value=\"\" \n\t\tdojoAttachPoint=\"comboBoxValue\"\n\t><input style=\"display:none\"  tabindex=\"-1\" name=\"\" value=\"\" \n\t\tdojoAttachPoint=\"comboBoxSelectionValue\"\n\t><input type=\"text\" autocomplete=\"off\" class=\"dojoComboBox\"\n\t\tdojoAttachEvent=\"key:_handleKeyEvents; keyUp: onKeyUp; compositionEnd; onResize;\"\n\t\tdojoAttachPoint=\"textInputNode\"\n\t><img hspace=\"0\"\n\t\tvspace=\"0\"\n\t\tclass=\"dojoComboBox\"\n\t\tdojoAttachPoint=\"downArrowNode\"\n\t\tdojoAttachEvent=\"onMouseUp: handleArrowClick; onResize;\"\n\t\tsrc=\"${this.buttonSrc}\"\n></span>\n",
		templateCssString:".dojoComboBoxOuter {\n\tborder: 0px !important;\n\tmargin: 0px !important;\n\tpadding: 0px !important;\n\tbackground: transparent !important;\n\twhite-space: nowrap !important;\n}\n\n.dojoComboBox {\n\tborder: 1px inset #afafaf;\n\tmargin: 0px;\n\tpadding: 0px;\n\tvertical-align: middle !important;\n\tfloat: none !important;\n\tposition: static !important;\n\tdisplay: inline !important;\n}\n\n/* the input box */\ninput.dojoComboBox {\n\tborder-right-width: 0px !important; \n\tmargin-right: 0px !important;\n\tpadding-right: 0px !important;\n}\n\n/* the down arrow */\nimg.dojoComboBox {\n\tborder-left-width: 0px !important;\n\tpadding-left: 0px !important;\n\tmargin-left: 0px !important;\n}\n\n/* IE vertical-alignment calculations can be off by +-1 but these margins are collapsed away */\n.dj_ie img.dojoComboBox {\n\tmargin-top: 1px; \n\tmargin-bottom: 1px; \n}\n\n/* the drop down */\n.dojoComboBoxOptions {\n\tfont-family: Verdana, Helvetica, Garamond, sans-serif;\n\t/* font-size: 0.7em; */\n\tbackground-color: white;\n\tborder: 1px solid #afafaf;\n\tposition: absolute;\n\tz-index: 1000; \n\toverflow: auto;\n\tcursor: default;\n}\n\n.dojoComboBoxItem {\n\tpadding-left: 2px;\n\tpadding-top: 2px;\n\tmargin: 0px;\n}\n\n.dojoComboBoxItemEven {\n\tbackground-color: #f4f4f4;\n}\n\n.dojoComboBoxItemOdd {\n\tbackground-color: white;\n}\n\n.dojoComboBoxItemHighlight {\n\tbackground-color: #63709A;\n\tcolor: white;\n}\n",templateCssPath: dojo.uri.moduleUri("dojo.widget", "templates/ComboBox.css"),

		setValue: function(/*String*/ value){
			// summary: Sets the value of the combobox
			this.comboBoxValue.value = value;
			if (this.textInputNode.value != value){ // prevent mucking up of selection
				this.textInputNode.value = value;
				// only change state and value if a new value is set
				dojo.widget.html.stabile.setState(this.widgetId, this.getState(), true);
				this.onValueChanged(value);
			}
		},

		onValueChanged: function(/*String*/ value){
			// summary: callback when value changes, for user to attach to
		},

		getValue: function(){
			// summary: Rerturns combo box value
			return this.comboBoxValue.value;
		},

		getState: function(){
			// summary:
			//	Used for saving state of ComboBox when navigates to a new
			//	page, in case they then hit the browser's "Back" button.
			return {value: this.getValue()};
		},

		setState: function(/*Object*/ state){
			// summary:
			//	Used for restoring state of ComboBox when has navigated to a new
			//	page but then hits browser's "Back" button.
			this.setValue(state.value);
		},

		enable:function(){
			this.disabled=false;
			this.textInputNode.removeAttribute("disabled");
		},

		disable: function(){
			this.disabled = true;
			this.textInputNode.setAttribute("disabled",true);
		},

		_getCaretPos: function(/*DomNode*/ element){
			// khtml 3.5.2 has selection* methods as does webkit nightlies from 2005-06-22
			if(dojo.lang.isNumber(element.selectionStart)){
				// FIXME: this is totally borked on Moz < 1.3. Any recourse?
				return element.selectionStart;
			}else if(dojo.render.html.ie){
				// in the case of a mouse click in a popup being handled,
				// then the document.selection is not the textarea, but the popup
				// var r = document.selection.createRange();
				// hack to get IE 6 to play nice. What a POS browser.
				var tr = document.selection.createRange().duplicate();
				var ntr = element.createTextRange();
				tr.move("character",0);
				ntr.move("character",0);
				try {
					// If control doesnt have focus, you get an exception.
					// Seems to happen on reverse-tab, but can also happen on tab (seems to be a race condition - only happens sometimes).
					// There appears to be no workaround for this - googled for quite a while.
					ntr.setEndPoint("EndToEnd", tr);
					return String(ntr.text).replace(/\r/g,"").length;
				} catch (e){
					return 0; // If focus has shifted, 0 is fine for caret pos.
				}

			}
		},

		_setCaretPos: function(/*DomNode*/ element, /*Number*/ location){
			location = parseInt(location);
			this._setSelectedRange(element, location, location);
		},

		_setSelectedRange: function(/*DomNode*/ element, /*Number*/ start, /*Number*/ end){
			if(!end){ end = element.value.length; }  // NOTE: Strange - should be able to put caret at start of text?
			// Mozilla
			// parts borrowed from http://www.faqts.com/knowledge_base/view.phtml/aid/13562/fid/130
			if(element.setSelectionRange){
				element.focus();
				element.setSelectionRange(start, end);
			}else if(element.createTextRange){ // IE
				var range = element.createTextRange();
				with(range){
					collapse(true);
					moveEnd('character', end);
					moveStart('character', start);
					select();
				}
			}else{ //otherwise try the event-creation hack (our own invention)
				// do we need these?
				element.value = element.value;
				element.blur();
				element.focus();
				// figure out how far back to go
				var dist = parseInt(element.value.length)-end;
				var tchar = String.fromCharCode(37);
				var tcc = tchar.charCodeAt(0);
				for(var x = 0; x < dist; x++){
					var te = document.createEvent("KeyEvents");
					te.initKeyEvent("keypress", true, true, null, false, false, false, false, tcc, tcc);
					element.dispatchEvent(te);
				}
			}
		},

		_handleKeyEvents: function(/*Event*/ evt){
			// summary: handles keyboard events
			if(evt.ctrlKey || evt.altKey || !evt.key){ return; }

			// reset these
			this._prev_key_backspace = false;
			this._prev_key_esc = false;

			var k = dojo.event.browser.keys;
			var doSearch = true;

			switch(evt.key){
	 			case k.KEY_DOWN_ARROW:
					if(!this.popupWidget.isShowingNow){
						this._startSearchFromInput();
					}
					this._highlightNextOption();
					dojo.event.browser.stopEvent(evt);
					return;
				case k.KEY_UP_ARROW:
					this._highlightPrevOption();
					dojo.event.browser.stopEvent(evt);
					return;
				case k.KEY_TAB:
					// using linux alike tab for autocomplete
					if(!this.autoComplete && this.popupWidget.isShowingNow && this._highlighted_option){
						dojo.event.browser.stopEvent(evt);
						this._selectOption({ 'target': this._highlighted_option, 'noHide': false});

						// put caret last
						this._setSelectedRange(this.textInputNode, this.textInputNode.value.length, null);
					}else{
						this._selectOption();
						return;
					}
					break;
				case k.KEY_ENTER:
					// prevent submitting form if we press enter with list open
					if(this.popupWidget.isShowingNow){
						dojo.event.browser.stopEvent(evt);
					}
					if(this.autoComplete){
						this._selectOption();
						return;
					}
					// fallthrough
				case " ":
					if(this.popupWidget.isShowingNow && this._highlighted_option){
						dojo.event.browser.stopEvent(evt);
						this._selectOption();
						this._hideResultList();
						return;
					}
					break;
				case k.KEY_ESCAPE:
					this._hideResultList();
					this._prev_key_esc = true;
					return;
				case k.KEY_BACKSPACE:
					this._prev_key_backspace = true;
					if(!this.textInputNode.value.length){
						this.setAllValues("", "");
						this._hideResultList();
						doSearch = false;
					}
					break;
				case k.KEY_RIGHT_ARROW: // fall through
				case k.KEY_LEFT_ARROW: // fall through
					doSearch = false;
					break;
				default:// non char keys (F1-F12 etc..)  shouldn't open list
					if(evt.charCode==0){
						doSearch = false;
					}
			}

			if(this.searchTimer){
				clearTimeout(this.searchTimer);
			}
			if(doSearch){
				// if we have gotten this far we dont want to keep our highlight
				this._blurOptionNode();

				// need to wait a tad before start search so that the event bubbles through DOM and we have value visible
				this.searchTimer = setTimeout(dojo.lang.hitch(this, this._startSearchFromInput), this.searchDelay);
			}
		},

		compositionEnd: function(/*Event*/ evt){
			// summary: When inputting characters using an input method, such as Asian
			// languages, it will generate this event instead of onKeyDown event
			evt.key = evt.keyCode;
			this._handleKeyEvents(evt);
		},

		onKeyUp: function(/*Event*/ evt){
			// summary: callback on key up event
			this.setValue(this.textInputNode.value);
		},

		setSelectedValue: function(/*String*/ value){
			// summary:
			//		This sets a hidden value associated w/the displayed value.
			//		The hidden value (and this function) shouldn't be used; if
			//		you need a hidden value then use Select widget instead of ComboBox.
			// TODO: remove?
			// FIXME, not sure what to do here!
			this.comboBoxSelectionValue.value = value;
		},

		setAllValues: function(/*String*/ value1, /*String*/ value2){
			// summary:
			//		This sets the displayed value and hidden value.
			//		The hidden value (and this function) shouldn't be used; if
			//		you need a hidden value then use Select widget instead of ComboBox.
			this.setSelectedValue(value2);
			this.setValue(value1);
		},

		_focusOptionNode: function(/*DomNode*/ node){
			// summary: does the actual highlight
			if(this._highlighted_option != node){
				this._blurOptionNode();
				this._highlighted_option = node;
				dojo.html.addClass(this._highlighted_option, "dojoComboBoxItemHighlight");
			}
		},

		_blurOptionNode: function(){
			// sumary: removes highlight on highlighted
			if(this._highlighted_option){
				dojo.html.removeClass(this._highlighted_option, "dojoComboBoxItemHighlight");
				this._highlighted_option = null;
			}
		},

		_highlightNextOption: function(){
			if((!this._highlighted_option) || !this._highlighted_option.parentNode){
				this._focusOptionNode(this.optionsListNode.firstChild);
			}else if(this._highlighted_option.nextSibling){
				this._focusOptionNode(this._highlighted_option.nextSibling);
			}
			dojo.html.scrollIntoView(this._highlighted_option);
		},

		_highlightPrevOption: function(){
			if(this._highlighted_option && this._highlighted_option.previousSibling){
				this._focusOptionNode(this._highlighted_option.previousSibling);
			}else{
				this._highlighted_option = null;
				this._hideResultList();
				return;
			}
			dojo.html.scrollIntoView(this._highlighted_option);
		},

		_itemMouseOver: function(/*Event*/ evt){
			if (evt.target === this.optionsListNode){ return; }
			this._focusOptionNode(evt.target);
			dojo.html.addClass(this._highlighted_option, "dojoComboBoxItemHighlight");
		},

		_itemMouseOut: function(/*Event*/ evt){
			if (evt.target === this.optionsListNode){ return; }
			this._blurOptionNode();
		},

		onResize: function(){
			// summary: this function is called when the input area has changed size
			var inputSize = dojo.html.getContentBox(this.textInputNode);
			if( inputSize.height <= 0 ){
				// need more time to calculate size
				dojo.lang.setTimeout(this, "onResize", 100);
				return;
			}
			var buttonSize = { width: inputSize.height, height: inputSize.height};
			dojo.html.setContentBox(this.downArrowNode, buttonSize);
		},

		fillInTemplate: function(/*Object*/ args, /*Object*/ frag){
			// there's some browser specific CSS in ComboBox.css
			dojo.html.applyBrowserClass(this.domNode);

			var source = this.getFragNodeRef(frag);
			if (! this.name && source.name){ this.name = source.name; }
			this.comboBoxValue.name = this.name;
			this.comboBoxSelectionValue.name = this.name+"_selected";

			/* different nodes get different parts of the style */
			dojo.html.copyStyle(this.domNode, source);
			dojo.html.copyStyle(this.textInputNode, source);
			dojo.html.copyStyle(this.downArrowNode, source);
			with (this.downArrowNode.style){ // calculate these later
				width = "0px";
				height = "0px";
			}

			// Use specified data provider class; if no class is specified
			// then use comboboxDataProvider or incrmentalComboBoxDataProvider
			// depending on setting of mode
			var dpClass;
			if(this.dataProviderClass){
				if(typeof this.dataProviderClass == "string"){
					dpClass = dojo.evalObjPath(this.dataProviderClass)
				}else{
					dpClass = this.dataProviderClass;
				}
			}else{
				if(this.mode == "remote"){
					dpClass = dojo.widget.incrementalComboBoxDataProvider;
				}else{
					dpClass = dojo.widget.basicComboBoxDataProvider;
				}
			}
			this.dataProvider = new dpClass(this, this.getFragNodeRef(frag));

			this.popupWidget = new dojo.widget.createWidget("PopupContainer",
				{toggle: this.dropdownToggle, toggleDuration: this.toggleDuration});
			dojo.event.connect(this, 'destroy', this.popupWidget, 'destroy');
			this.optionsListNode = this.popupWidget.domNode;
			this.domNode.appendChild(this.optionsListNode);
			dojo.html.addClass(this.optionsListNode, 'dojoComboBoxOptions');
			dojo.event.connect(this.optionsListNode, 'onclick', this, '_selectOption');
			dojo.event.connect(this.optionsListNode, 'onmouseover', this, '_onMouseOver');
			dojo.event.connect(this.optionsListNode, 'onmouseout', this, '_onMouseOut');

			// TODO: why does onmouseover and onmouseout connect to two separate handlers???
			dojo.event.connect(this.optionsListNode, "onmouseover", this, "_itemMouseOver");
			dojo.event.connect(this.optionsListNode, "onmouseout", this, "_itemMouseOut");
		},

		_openResultList: function(/*Array*/ results){
			if (this.disabled){
				return;
			}
			this._clearResultList();
			if(!results.length){
				this._hideResultList();
			}

			if(	(this.autoComplete)&&
				(results.length)&&
				(!this._prev_key_backspace)&&
				(this.textInputNode.value.length > 0)){
				var cpos = this._getCaretPos(this.textInputNode);
				// only try to extend if we added the last character at the end of the input
				if((cpos+1) > this.textInputNode.value.length){
					// only add to input node as we would overwrite Capitalisation of chars
					this.textInputNode.value += results[0][0].substr(cpos);
					// build a new range that has the distance from the earlier
					// caret position to the end of the first string selected
					this._setSelectedRange(this.textInputNode, cpos, this.textInputNode.value.length);
				}
			}

			var even = true;
			while(results.length){
				var tr = results.shift();
				if(tr){
					var td = document.createElement("div");
					td.appendChild(document.createTextNode(tr[0]));
					td.setAttribute("resultName", tr[0]);
					td.setAttribute("resultValue", tr[1]);
					td.className = "dojoComboBoxItem "+((even) ? "dojoComboBoxItemEven" : "dojoComboBoxItemOdd");
					even = (!even);
					this.optionsListNode.appendChild(td);
				}
			}

			// show our list (only if we have content, else nothing)
			this._showResultList();
		},

		_onFocusInput: function(){
			this._hasFocus = true;
		},

		_onBlurInput: function(){
			this._hasFocus = false;
			this._handleBlurTimer(true, 500);
		},

		_handleBlurTimer: function(/*Boolean*/clear, /*Number*/ millisec){
			// summary: collect all blur timers issues here
			if(this.blurTimer && (clear || millisec)){
				clearTimeout(this.blurTimer);
			}
			if(millisec){ // we ignore that zero is false and never sets as that never happens in this widget
				this.blurTimer = dojo.lang.setTimeout(this, "_checkBlurred", millisec);
			}
		},

		_onMouseOver: function(/*Event*/ evt){
			// summary: needed in IE and Safari as inputTextNode loses focus when scrolling optionslist
			if(!this._mouseover_list){
				this._handleBlurTimer(true, 0);
				this._mouseover_list = true;
			}
		},

		_onMouseOut:function(/*Event*/ evt){
			// summary: needed in IE and Safari as inputTextNode loses focus when scrolling optionslist
			var relTarget = evt.relatedTarget;
			try { // fixes #1807
				if(!relTarget || relTarget.parentNode != this.optionsListNode){
					this._mouseover_list = false;
					this._handleBlurTimer(true, 100);
					this._tryFocus();
				}
			}catch(e){}
		},

		_isInputEqualToResult: function(/*String*/ result){
			var input = this.textInputNode.value;
			if(!this.dataProvider.caseSensitive){
				input = input.toLowerCase();
				result = result.toLowerCase();
			}
			return (input == result);
		},

		_isValidOption: function(){
			var tgt = dojo.html.firstElement(this.optionsListNode);
			var isValidOption = false;
			while(!isValidOption && tgt){
				if(this._isInputEqualToResult(tgt.getAttribute("resultName"))){
					isValidOption = true;
				}else{
					tgt = dojo.html.nextElement(tgt);
				}
			}
			return isValidOption;
		},

		_checkBlurred: function(){
			if(!this._hasFocus && !this._mouseover_list){
				this._hideResultList();
				// clear the list if the user empties field and moves away.
				if(!this.textInputNode.value.length){
					this.setAllValues("", "");
					return;
				}

				var isValidOption = this._isValidOption();
				// enforce selection from option list
				if(this.forceValidOption && !isValidOption){
					this.setAllValues("", "");
					return;
				}
				if(!isValidOption){// clear
					this.setSelectedValue("");
				}
			}
		},

		_selectOption: function(/*Event*/ evt){
			var tgt = null;
			if(!evt){
				evt = { target: this._highlighted_option };
			}

			if(!dojo.html.isDescendantOf(evt.target, this.optionsListNode)){
				// handle autocompletion where the the user has hit ENTER or TAB

				// if the input is empty do nothing
				if(!this.textInputNode.value.length){
					return;
				}
				tgt = dojo.html.firstElement(this.optionsListNode);

				// user has input value not in option list
				if(!tgt || !this._isInputEqualToResult(tgt.getAttribute("resultName"))){
					return;
				}
				// otherwise the user has accepted the autocompleted value
			}else{
				tgt = evt.target;
			}

			while((tgt.nodeType!=1)||(!tgt.getAttribute("resultName"))){
				tgt = tgt.parentNode;
				if(tgt === dojo.body()){
					return false;
				}
			}

			this.selectedResult = [tgt.getAttribute("resultName"), tgt.getAttribute("resultValue")];
			this.setAllValues(tgt.getAttribute("resultName"), tgt.getAttribute("resultValue"));
			if(!evt.noHide){
				this._hideResultList();
				this._setSelectedRange(this.textInputNode, 0, null);
			}
			this._tryFocus();
		},

		_clearResultList: function(){
			if(this.optionsListNode.innerHTML){
				this.optionsListNode.innerHTML = "";  // browser natively knows how to collect this memory
			}
		},

		_hideResultList: function(){
			this.popupWidget.close();
		},

		_showResultList: function(){
			// Our dear friend IE doesnt take max-height so we need to calculate that on our own every time
			var childs = this.optionsListNode.childNodes;
			if(childs.length){
				var visibleCount = Math.min(childs.length,this.maxListLength);

				with(this.optionsListNode.style)
				{
					display = "";
					if(visibleCount == childs.length){
						//no scrollbar is required, so unset height to let browser calcuate it,
						//as in css, overflow is already set to auto
						height = "";
					}else{
						//show it first to get the correct dojo.style.getOuterHeight(childs[0])
						//FIXME: shall we cache the height of the item?
						height = visibleCount * dojo.html.getMarginBox(childs[0]).height +"px";
					}
					width = (dojo.html.getMarginBox(this.domNode).width-2)+"px";
				}
				this.popupWidget.open(this.domNode, this, this.downArrowNode);
			}else{
				this._hideResultList();
			}
		},

		handleArrowClick: function(){
			// summary: callback when arrow is clicked
			this._handleBlurTimer(true, 0);
			this._tryFocus();
			if(this.popupWidget.isShowingNow){
				this._hideResultList();
			}else{
				// forces full population of results, if they click
				// on the arrow it means they want to see more options
				this._startSearch("");
			}
		},

		_tryFocus: function(){
			try {
				this.textInputNode.focus();
			} catch (e){
				// element isn't focusable if disabled, or not visible etc - not easy to test for.
	 		};
		},

		_startSearchFromInput: function(){
			this._startSearch(this.textInputNode.value);
		},

		_startSearch: function(/*String*/ key){
			this.dataProvider.startSearch(key, dojo.lang.hitch(this, "_openResultList"));
		},

		postCreate: function(){
			this.onResize();

			// TODO: add these attach events to template
			dojo.event.connect(this.textInputNode, "onblur", this, "_onBlurInput");
			dojo.event.connect(this.textInputNode, "onfocus", this, "_onFocusInput");

			if (this.disabled){
				this.disable();
			}
			var s = dojo.widget.html.stabile.getState(this.widgetId);
			if (s){
				this.setState(s);
			}
		}
	}
);

dojo.provide("dojo.widget.Select");





dojo.widget.defineWidget(
	"dojo.widget.Select",
	dojo.widget.ComboBox,
	{
		/*
		 * summary
		 *	Enhanced version of HTML's <select> tag.
		 *
		 *	Similar features:
		 *	  - There is a drop down list of possible values.
		 *    - You can only enter a value from the drop down list.  (You can't enter an arbitrary value.)
		 *    - The value submitted with the form is the hidden value (ex: CA),
		 *      not the displayed value a.k.a. label (ex: California)
		 *
		 *	Enhancements over plain HTML version:
		 *    - If you type in some text then it will filter down the list of possible values in the drop down list.
		 *    - List can be specified either as a static list or via a javascript function (that can get the list from a server)
		 */

		//	This value should not be changed by the user
		forceValidOption: true,

		setValue: function(value) {
			// summary
			//	Sets the value of the combobox.
			//	TODO: this doesn't work correctly when a URL is specified, because we can't
			//	set the label automatically (based on the specified value)
			this.comboBoxValue.value = value;
			dojo.widget.html.stabile.setState(this.widgetId, this.getState(), true);
			this.onValueChanged(value);
		},

		setLabel: function(value){
			// summary
			//	FIXME, not sure what to do here!
			//	Users shouldn't call this function; they should be calling setValue() instead
			this.comboBoxSelectionValue.value = value;
			if (this.textInputNode.value != value) { // prevent mucking up of selection
				this.textInputNode.value = value;
			}
		},	  

		getLabel: function(){
			// summary: returns current label
			return this.comboBoxSelectionValue.value;	// String
		},

		getState: function() {
			// summary: returns current value and label
			return {
				value: this.getValue(),
				label: this.getLabel()
			};	// Object
		},

		onKeyUp: function(/*Event*/ evt){
			// summary: internal function
			this.setLabel(this.textInputNode.value);
		},

		setState: function(/*Object*/ state) {
			// summary: internal function to set both value and label
			this.setValue(state.value);
			this.setLabel(state.label);
		},

		setAllValues: function(/*String*/ value1, /*String*/ value2){
			// summary: internal function to set both value and label
			this.setLabel(value1);
			this.setValue(value2);
		}
	}
);

