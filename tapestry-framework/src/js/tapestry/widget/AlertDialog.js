dojo.provide("tapestry.widget.AlertDialog");

dojo.require("dojo.widget.*");
dojo.require("dojo.widget.Dialog");
dojo.require("dojo.event.common");
dojo.require("dojo.html.common");

/**
 * Script:  tapestry.widget.AlertDialog
 * 
 * The modal dialog used to display client side validation errors / informational
 * messages.
 * 
 * Inherits from:
 * 		<dojo.widget.Dialog>
 * 
 */
dojo.widget.defineWidget(
	"tapestry.widget.AlertDialog",
	dojo.widget.Dialog,
	{
		bgColor: "white",
		bgOpacity: 0.6,
		okButton:null,
		messageNode:null,
		message:"",
		
		dialogClass:"alertDialog",
		contentClass:"alertContent",
		buttonClass:"alertButton",
		buttonText:"OK",
		
		/**
		 * Function: postCreate
		 * 
		 * Called after widget constructed.
		 */
		postCreate: function(args, frag, parentComp) {
			dojo.widget.Dialog.prototype.postCreate.call(this, args, frag, parentComp);
			
			var content=document.createElement("div");
			dojo.html.setClass(content, this.contentClass);
			this.containerNode.appendChild(content);
			dojo.html.addClass(this.containerNode, this.dialogClass);
			
			this.messageNode=document.createElement("div");
			this.messageNode.innerHTML=this.message;
			content.appendChild(this.messageNode);
			
			var buttNode=document.createElement("button");
			dojo.html.setClass(buttNode, this.buttonClass);
			buttNode.setAttribute("id", "alertButton");
			buttNode.innerHTML = this.buttonText;
			content.appendChild(buttNode);
			
			this.okButton=buttNode;
			this.tabStart=this.okButton;
			this.tabEnd=this.okButton;
			this.show();
			dojo.event.connect(this.okButton, "onclick", this, "hideDialog");
			this.okButton.focus();
			dojo.event.connect(this.wrapper, 'onkeyup', this, 'dialogKeys');
			dojo.event.connect(document.body, 'onkeyup', this, 'bodyKeys'); 
		},
		
		dialogKeys:function(e) {
			if (e.keyCode == e.KEY_ESCAPE) {
				this.hideDialog(e);
			}
			// allow default behavior, but don't let the event keep bubbling/propagating
			if (e.stopPropagation) {
				e.stopPropagation();
			} else { 
				e.cancelBubble = true; 
			}
		},
		
		bodyKeys:function(e) {
			if (e.keyCode == e.KEY_ESCAPE) {
				this.hideDialog(e);
			} else if ( ! dojo.dom.isDescendantOf(e.target, this.wrapper, true) ) {
				dojo.event.browser.stopEvent(e);
				this.tabStart.focus();
			}
		},
		
		setMessage:function(str){
			this.messageNode.innerHTML=str;
		},
		
		hideDialog:function(e){
			dojo.event.disconnect(this.wrapper, 'onkeyup', this, 'dialogKeys');
			dojo.event.disconnect(document.body, 'onkeyup', this, 'bodyKeys');
			this.hideModalDialog();
			dojo.dom.removeNode(this.okButton);
			tapestry.widget.AlertDialog.prototype.destroy.call(this);
			dojo.dom.removeNode(this.bg); 
			tapestry.form._focusCurrentField();
		}
	},
	"html"
);
