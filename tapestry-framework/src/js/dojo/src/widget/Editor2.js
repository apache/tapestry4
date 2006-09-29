/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

/* TODO:
 * - font selector
 * - test, bug fix, more features :)
*/
dojo.provide("dojo.widget.Editor2");
dojo.provide("dojo.widget.Editor2Manager");

dojo.require("dojo.io.*");
dojo.require("dojo.html.*");
dojo.require("dojo.html.layout");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.RichText");
dojo.require("dojo.widget.Editor2Toolbar");

// dojo.require("dojo.widget.ColorPalette");
// dojo.require("dojo.string.extras");

//API to manage current focused Editor2 Instance
dojo.widget.Editor2Manager = {
	//private variables
	_currentInstance: null,
	_loadedCommands: {},

	destroy: function(){
		this._currentInstance = null;
		for(var cmd in this._loadedCommands){
			this._loadedCommands[cmd].destory();
		}
	},

	commandState: {Disabled: 0, Latched: 1, Enabled: 2},
	//Public API
	getCurrentInstance: function(){
		return this._currentInstance;
	},
	setCurrentInstance: function(inst){
		this._currentInstance = inst;
	},
	registerCommand: function(name, cmd){
		name = name.toLowerCase();
		if(this._loadedCommands[name]){
			delete this._loadedCommands[name];
		}
		this._loadedCommands[name] = cmd;
	},
	getCommand: function(name){
		name = name.toLowerCase();
		var oCommand = this._loadedCommands[name];
		if(oCommand){
			return oCommand;
		}

		switch(name){
			case 'htmltoggle':
				//Editor2 natively provide the htmltoggle functionalitity
				//and it is treated as a builtin command 
				oCommand = new dojo.widget.Editor2BrowserCommand(name);
				break;
			case 'formatblock':
				oCommand = new dojo.widget.Editor2FormatBlockCommand(name);
				break;
			case 'anchor':
				oCommand = new dojo.widget.Editor2Command(name);
				break;

			//dialog command
			case 'createlink':
				oCommand = new dojo.widget.Editor2DialogCommand(name, 
						{contentFile: "dojo.widget.Editor2Plugin.CreateLinkDialog", 
							contentClass: "Editor2CreateLinkDialog",
							title: "Insert/Edit Link", width: "300px", height: "200px"});
				break;
			case 'insertimage':
				oCommand = new dojo.widget.Editor2DialogCommand(name, 
						{contentFile: "dojo.widget.Editor2Plugin.InsertImageDialog", 
							contentClass: "Editor2InsertImageDialog",
							title: "Insert/Edit Image", width: "400px", height: "270px"});
				break;
			// By default we assume that it is a builtin simple command.
			default:
				var curtInst = this.getCurrentInstance();
				if((curtInst && curtInst.queryCommandAvailable(name)) ||
					(!curtInst && dojo.widget.Editor2.prototype.queryCommandAvailable(name))){
					oCommand = new dojo.widget.Editor2BrowserCommand(name);
				}else{
					dojo.debug("dojo.widget.Editor2Manager.getCommand: Unknown command "+name);
					return;
				}
		}
		this._loadedCommands[name] = oCommand;
		return oCommand;
	}
};

dojo.addOnUnload(dojo.widget.Editor2Manager, "destroy");

/* base class for all command in Editor2 */
dojo.lang.declare("dojo.widget.Editor2Command",null,{
		initializer: function(name){
			this._name = name;
		},
		//this function should be re-implemented in subclass
		execute: function(para){
			alert("Please implement your own execute() function for subclass of Editor2Command.");
		},
		//default implemetation always returns Enabled
		getState: function(){
			return dojo.widget.Editor2Manager.commandState.Enabled;
		},
		destory: function(){}
	}
);

dojo.lang.declare("dojo.widget.Editor2BrowserCommand", dojo.widget.Editor2Command, {
		execute: function(para){
			var curInst = dojo.widget.Editor2Manager.getCurrentInstance();
			if(curInst){
				curInst.execCommand(this._name, para);
			}
		},
		getState: function(){
			var curInst = dojo.widget.Editor2Manager.getCurrentInstance();
			if(curInst){
				try{
					if(curInst.queryCommandEnabled(this._name)){
						if(curInst.queryCommandState(this._name)){
							return dojo.widget.Editor2Manager.commandState.Latched;
						}else{
							return dojo.widget.Editor2Manager.commandState.Enabled;
						}
					}else{
						return dojo.widget.Editor2Manager.commandState.Disabled;
					}
				}catch (e) {
					//dojo.debug("exception when getting state for command "+this._name+": "+e);
					return dojo.widget.Editor2Manager.commandState.Enabled;
				}
			}
			return dojo.widget.Editor2Manager.commandState.Disabled;
		},
		getValue: function(){
			var curInst = dojo.widget.Editor2Manager.getCurrentInstance();
			if(curInst){
				try{
					return curInst.queryCommandValue(this._name);
				}catch(e){}
			}
		}
	}
);

dojo.lang.declare("dojo.widget.Editor2FormatBlockCommand", dojo.widget.Editor2BrowserCommand, {
		/* In none-ActiveX mode under IE, <p> and no <p> text can not be distinguished
		getCurrentValue: function(){
			var curInst = dojo.widget.Editor2Manager.getCurrentInstance();
			if(!curInst){ return ''; }

			var h = dojo.render.html;
			
			// safari f's us for selection primitives
			if(h.safari){ return ''; }

			var selectedNode = (h.ie) ? curInst.document.selection.createRange().parentElement() : curInst.window.getSelection().anchorNode;
			// make sure we actuall have an element
			while((selectedNode)&&(selectedNode.nodeType != 1)){
				selectedNode = selectedNode.parentNode;
			}
			if(!selectedNode){ return ''; }

			var formats = ["p", "pre", "h1", "h2", "h3", "h4", "h5", "h6", "address"];
			// gotta run some specialized updates for the various
			// formatting options
			var type = formats[dojo.lang.find(formats, selectedNode.nodeName.toLowerCase())];
			while((selectedNode!=curInst.editNode)&&(!type)){
				selectedNode = selectedNode.parentNode;
				if(!selectedNode){ break; }
				type = formats[dojo.lang.find(formats, selectedNode.nodeName.toLowerCase())];
			}
			if(!type){
				type = "";
			}
			return type;
		}*/
	}
);

dojo.require("dojo.widget.FloatingPane");
dojo.widget.defineWidget(
	"dojo.widget.Editor2Dialog",
	[dojo.widget.HtmlWidget, dojo.widget.FloatingPaneBase, dojo.widget.ModalDialogBase],
	{
		modal: true,
		templatePath: dojo.uri.dojoUri("src/widget/templates/Editor2/EditorDialog.html"),
		executeScripts: true,
		refreshOnShow: true, //for debug for now

		width: false,
		height: false,

		windowState: "minimized",
		displayCloseAction: true,

		contentFile: "",
		contentClass: "",

		fillInTemplate: function(args, frag){	
			this.fillInFloatingPaneTemplate(args, frag);
			dojo.widget.Editor2Dialog.superclass.fillInTemplate.call(this, args, frag);
		},
		postCreate: function(){
			if(this.contentFile){
				dojo.require(this.contentFile);
			}
			if(this.modal){
				dojo.widget.ModalDialogBase.prototype.postCreate.call(this);
			}else{
				with(this.domNode.style) {
					zIndex = 999;
					display = "none";
				}
			}
			dojo.widget.FloatingPaneBase.prototype.postCreate.apply(this, arguments);
			dojo.widget.Editor2Dialog.superclass.postCreate.call(this);
			if(this.width && this.height){
				with(this.domNode.style){
					width = this.width;
					height = this.height;
				}
			}
		},
		createContent: function(){
			if(!this.contentWidget && this.contentClass){
				this.contentWidget = dojo.widget.createWidget(this.contentClass);
				this.addChild(this.contentWidget);
			}
		},
		show: function(){
			if(!this.contentWidget){
				//buggy IE: if the dialog is hidden, the button widgets
				//in the dialog can not be shown, so show it temporary (as the
				//dialog may decide not to show it in loadContent() later)
				dojo.widget.Editor2Dialog.superclass.show.apply(this, arguments);
				this.createContent();
				dojo.widget.Editor2Dialog.superclass.hide.call(this);
			}

			if(!this.contentWidget || !this.contentWidget.loadContent()){
				return;
			}
			this.showFloatingPane();
			dojo.widget.Editor2Dialog.superclass.show.apply(this, arguments);
			if(this.modal){
				this.showModalDialog();
			}
			this.placeModalDialog();
			if(this.modal){
				//place the background div under this modal pane
				this.shared.bg.style.zIndex = this.domNode.style.zIndex-1;
			}
		},
		onShow: function(){
			dojo.widget.Editor2Dialog.superclass.onShow.call(this);
			this.onFloatingPaneShow();
		},
		closeWindow: function(){
			this.hide();
			dojo.widget.Editor2Dialog.superclass.closeWindow.apply(this, arguments);
		},
		hide: function(){
			if(this.modal){
				this.hideModalDialog();
			}
			dojo.widget.Editor2Dialog.superclass.hide.call(this);
		}
	}
);

dojo.widget.defineWidget(
	"dojo.widget.Editor2DialogContent",
	dojo.widget.HtmlWidget,
{
	widgetsInTemplate: true,

	loadContent:function(){
		return true;
	},
	cancel: function(){
		this.parent.hide();
	}
});

dojo.lang.declare("dojo.widget.Editor2DialogCommand", dojo.widget.Editor2BrowserCommand, 
	function(name, dialogParas){
		this.dialogParas = dialogParas;
	},
{
	execute: function(){
		if(!this.dialog){
			if(!this.dialogParas.contentFile || !this.dialogParas.contentClass){
				alert("contentFile and contentClass should be set for dojo.widget.Editor2DialogCommand.dialogParas!");
				return;
			}
			this.dialog = dojo.widget.createWidget("Editor2Dialog", this.dialogParas);

			dojo.body().appendChild(this.dialog.domNode);

			dojo.event.connect(this, "destroy", this.dialog, "destroy");
		}
		this.dialog.show();
	}
});

dojo.widget.defineWidget(
	"dojo.widget.Editor2",
	dojo.widget.RichText,
	{
		saveUrl: "",
		saveMethod: "post",
		saveArgName: "editorContent",
		closeOnSave: false,
		shareToolbar: false,
		toolbarAlwaysVisible: false,
		htmlEditing: false,
		_inSourceMode: false,
		_htmlEditNode: null,

		toolbarWidget: null,
		scrollInterval: null,
		toolbarTemplatePath: dojo.uri.dojoUri("src/widget/templates/EditorToolbarOneline.html"),
//		toolbarTemplatePath: dojo.uri.dojoUri("src/widget/templates/Editor2/EditorToolbarFCKStyle.html"),
//		toolbarTemplateCssPath: dojo.uri.dojoUri("src/widget/templates/Editor2/FCKDefault/EditorToolbarFCKStyle.css"),

		plugins: "",

		editorOnLoad: function(){
//			dojo.profile.start("dojo.widget.Editor2::editorOnLoad");

			dojo.event.topic.publish("dojo.widget.Editor2::preLoadingToolbar", this);
			if(this.toolbarAlwaysVisible){
				dojo.require("dojo.widget.Editor2Plugin.AlwaysShowToolbar");
			}

			var toolbars = dojo.widget.byType("Editor2Toolbar");
			if((!toolbars.length)||(!this.shareToolbar)){
				if(this.toolbarWidget){
					this.toolbarWidget.show();
					//re-add the toolbar to the new domNode (caused by open() on another element)
					dojo.html.insertBefore(this.toolbarWidget.domNode, this.domNode.firstChild);
				}else{
					var tbOpts = {};
					tbOpts.templatePath = this.toolbarTemplatePath;
					if(this.toolbarTemplateCssPath){
						tbOpts.templateCssPath = this.toolbarTemplateCssPath;
					}
					this.toolbarWidget = dojo.widget.createWidget("Editor2Toolbar", tbOpts, this.domNode.firstChild, "before");

					dojo.event.connect(this, "close", this.toolbarWidget, "hide");

					this.toolbarLoaded();
				}
			}else{
				// FIXME: 	selecting in one shared toolbar doesn't clobber
				// 			selection in the others. This is problematic.
				this.toolbarWidget = toolbars[0];
			}

			dojo.event.topic.registerPublisher("Editor2.clobberFocus", this, "clobberFocus");
			dojo.event.topic.subscribe("Editor2.clobberFocus", this, "setBlur");

			dojo.event.topic.publish("dojo.widget.Editor2::onLoad", this);
//			dojo.profile.end("dojo.widget.Editor2::editorOnLoad");
		},

		//event for plugins to use
		toolbarLoaded: function(){},

		//TODO: provide a query mechanism about loaded plugins?
		registerLoadedPlugin: function(/*Object*/obj){
			if(!this.loadedPlugins){
				this.loadedPlugins = [];
			}
			this.loadedPlugins.push(obj);
		},
		unregisterLoadedPlugin: function(/*Object*/obj){
			for(var i in this.loadedPlugins){
				if(this.loadedPlugins[i] === obj){
					delete this.loadedPlugins[i];
					return;
				}
			}
			dojo.debug("dojo.widget.Editor2.unregisterLoadedPlugin: unknow plugin object: "+obj);
		},

		//overload the default one to provide extra commands
		execCommand: function(command, argument){
			switch(command.toLowerCase()){
				case 'htmltoggle':
					this.toggleHtmlEditing();
					break;
				default:
					dojo.widget.Editor2.superclass.execCommand.call(this, command, argument);
			}
		},
		queryCommandEnabled: function(command, argument){
			switch(command.toLowerCase()){
				case 'htmltoggle':
					return true;
				default:
					if(this._inSourceMode){ return false;}
					return dojo.widget.Editor2.superclass.queryCommandEnabled.call(this, command, argument);
			}
		},
		queryCommandState: function(command, argument){
			switch(command.toLowerCase()){
				case 'htmltoggle':
					return this._inSourceMode;
				default:
					return dojo.widget.Editor2.superclass.queryCommandState.call(this, command, argument);
			}
		},

		onClick: function(e){
			dojo.widget.Editor2.superclass.onClick.call(this, e);
			//if Popup is used, call dojo.widget.PopupManager.onClick
			//manually when click in the editing area to close all
			//open popups (dropdowns) 
			if(dojo.widget.PopupManager){
				if(!e){ //IE
					e = this.window.event;
				}
				dojo.widget.PopupManager.onClick(e);
			}
		},

		clobberFocus: function(){},
		toggleHtmlEditing: function(){
			if(this===dojo.widget.Editor2Manager.getCurrentInstance()){
				if(!this._inSourceMode){
					this._inSourceMode = true;

					if(!this._htmlEditNode){
						this._htmlEditNode = dojo.doc().createElement("textarea");
						dojo.html.insertAfter(this._htmlEditNode, this.editorObject);
					}
					this._htmlEditNode.style.display = "";
					this._htmlEditNode.style.width = "100%";
					this._htmlEditNode.style.height = dojo.html.getBorderBox(this.editNode).height+"px";
					this._htmlEditNode.value = this.editNode.innerHTML;

					//activeX object (IE) doesn't like to be hidden, so move it outside of screen instead
					with(this.editorObject.style){
						position = "absolute";
						left = "-2000px";
						top = "-2000px";
					}
				}else{
					this._inSourceMode = false;

					//In IE activeX mode, if _htmlEditNode is focused,
					//when toggling, an error would occur, so unfocus it
					this._htmlEditNode.blur();

					with(this.editorObject.style){
						position = "";
						left = "";
						top = "";
					}

					dojo.lang.setTimeout(this, "replaceEditorContent", 1, this._htmlEditNode.value);
					this._htmlEditNode.style.display = "none";
					this.focus();
				}
				this.updateToolbar(true);
			}
		},

		setFocus: function(){
			dojo.debug("setFocus: start "+this.widgetId);
			if(dojo.widget.Editor2Manager.getCurrentInstance() === this){ return; }

			this.clobberFocus();
			 dojo.debug("setFocus:", this);
			dojo.widget.Editor2Manager.setCurrentInstance(this);
		},

		setBlur: function(){
			 dojo.debug("setBlur:", this);
			//dojo.event.disconnect(this.toolbarWidget, "exec", this, "execCommand");
		},

		_updateToolbarLastRan: null,
		_updateToolbarTimer: null,
		_updateToolbarFrequency: 500,

		updateToolbar: function(force){
			if((!this.isLoaded)||(!this.toolbarWidget)){ return; }

			// keeps the toolbar from updating too frequently
			// TODO: generalize this functionality?
			var diff = new Date() - this._updateToolbarLastRan;
			if( (!force)&&(this._updateToolbarLastRan)&&
				((diff < this._updateToolbarFrequency)) ){

				clearTimeout(this._updateToolbarTimer);
				var _this = this;
				this._updateToolbarTimer = setTimeout(function() {
					_this.updateToolbar();
				}, this._updateToolbarFrequency/2);
				return;

			}else{
				this._updateToolbarLastRan = new Date();
			}
			// end frequency checker

			//TODO
			//if((cmd == "inserthtml") || (cmd == "save")){ return; }

			//IE has the habit of generating events even when this editor is blurred, prevent this
			if(dojo.widget.Editor2Manager.getCurrentInstance() !== this){ return; }

			this.toolbarWidget.update();
		},

		destroy: function(finalize){
			this._htmlEditNode = null;
			dojo.event.disconnect(this, "close", this.toolbarWidget, "hide");
			if(!finalize){
				this.toolbarWidget.destroy();
			}
			dojo.widget.Editor2.superclass.destroy.call(this);
		},

		onDisplayChanged: function(e){
			dojo.widget.Editor2.superclass.onDisplayChanged.call(this,e);
			this.updateToolbar();
		},

		onLoad: function(){
			try{
				dojo.widget.Editor2.superclass.onLoad.call(this);
			}catch(e){ // FIXME: debug why this is throwing errors in IE!
				dojo.debug(e);
			}
			this.editorOnLoad();
		},

		onFocus: function(){
			dojo.widget.Editor2.superclass.onFocus.call(this);
			this.setFocus();
		},

		//overload to support source editing mode
		getEditorContent: function(){
			if(this._inSourceMode){
				this.replaceEditorContent(this._htmlEditNode.value);
			}
			return dojo.widget.Editor2.superclass.getEditorContent.call(this);
		}// FIXME: probably not needed any more with new design, but need to verify
		/*,

		_save: function(e){
			// FIXME: how should this behave when there's a larger form in play?
			if(!this.isClosed){
				dojo.debug("save attempt");
				if(this.saveUrl.length){
					var content = {};
					content[this.saveArgName] = this.getEditorContent();
					dojo.io.bind({
						method: this.saveMethod,
						url: this.saveUrl,
						content: content
					});
				}else{
					dojo.debug("please set a saveUrl for the editor");
				}
				if(this.closeOnSave){
					this.close(e.getName().toLowerCase() == "save");
				}
			}
		}*/
	},
	"html"
);

//ContextMenu plugin should come before all other plugins which support
//contextmenu, otherwise the menu for that plugin won't be shown
dojo.require("dojo.widget.Editor2Plugin.ContextMenu");

// plugins are available using dojo's require syntax:
// dojo.widget.Editor2Plugin.FindReplace
//dojo.widget.Editor2Plugin.TableOperation
//dojo.widget.Editor2Plugin.ToolbarDndSupport
//dojo.widget.Editor2Plugin.ContextMenu
//use this plugin to have the old save/insertImage stub called when the
//corresponding button is clicked.
//Attention: this plugin overwrites the new builtin insertImage dialog
//see comments in the plugin file
//dojo.widget.Editor2Plugin.SimpleSignalCommands