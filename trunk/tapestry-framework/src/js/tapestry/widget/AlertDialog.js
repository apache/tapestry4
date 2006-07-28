dojo.provide("tapestry.widget.AlertDialog");

dojo.require("dojo.widget.*");
dojo.require("dojo.widget.Dialog");
dojo.require("dojo.widget.Button");
dojo.require("dojo.event.*");
dojo.require("dojo.html");

dojo.widget.defineWidget(
	"tapestry.widget.AlertDialog",
	dojo.widget.html.Dialog,
	{
		bgColor: "white",
		bgOpacity: 0.5,
		okButton:null,
		messageNode:null,
		message:"",
		
		postCreate: function(args, frag, parentComp) {
			tapestry.widget.AlertDialog.superclass.postCreate.call(this, args, frag, parentComp);
			
			var content=document.createElement("div");
			this.containerNode.appendChild(content);
			dojo.html.addClass(this.containerNode, "alertDialog");
			
			this.messageNode=document.createElement("div");
			this.messageNode.innerHTML=this.message;
			content.appendChild(this.messageNode);
			
			var buttNode=document.createElement("div");
			buttNode.appendChild(document.createTextNode("ok"));
			content.appendChild(buttNode);
			this.show(); // to fix bug in button
			this.okButton=dojo.widget.createWidget("Button",{}, buttNode);
			dojo.event.connect(this.okButton, "onClick", this, "hide");
		},
		
		setMessage:function(str){
			this.messageNode.innerHTML=str;
		},
		
		hideDialog:function(e){
			this.hide();
			this.okButton.destroy();
			tapestry.widget.AlertDialog.superclass.destroy.call(this);
			dojo.dom.removeNode(this.bg);
		}
	},
	"html"
);

dojo.widget.tags.addParseTreeHandler("dojo:alertdialog");
