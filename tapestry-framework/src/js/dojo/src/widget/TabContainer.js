
dojo.provide("dojo.widget.TabContainer");dojo.require("dojo.lang.func");dojo.require("dojo.widget.*");dojo.require("dojo.widget.PageContainer");dojo.require("dojo.event.*");dojo.require("dojo.html.selection");dojo.require("dojo.widget.html.layout");dojo.widget.defineWidget("dojo.widget.TabContainer", dojo.widget.PageContainer, {labelPosition: "top",closeButton: "none",templateString: null,templatePath: dojo.uri.dojoUri("src/widget/templates/TabContainer.html"),templateCssPath: dojo.uri.dojoUri("src/widget/templates/TabContainer.css"),fillInTemplate: function() {this.tablist = dojo.widget.createWidget("TabController",{id: this.widgetId + "_tablist",labelPosition: this.labelPosition,doLayout: this.doLayout,containerId: this.widgetId}, this.tablistNode);dojo.widget.TabContainer.superclass.fillInTemplate.apply(this, arguments);},postCreate: function(args, frag) {dojo.widget.TabContainer.superclass.postCreate.apply(this, arguments);this.onResized();},_setupChild: function(tab){if(this.closeButton=="tab" || this.closeButton=="pane"){tab.closable=true;}
dojo.html.addClass(tab.domNode, "dojoTabPane");dojo.widget.TabContainer.superclass._setupChild.apply(this, arguments);},onResized: function(){if(!this.doLayout){ return; }
var labelAlign=this.labelPosition.replace(/-h/,"");var children = [
{domNode: this.tablist.domNode, layoutAlign: labelAlign},{domNode: this.containerNode, layoutAlign: "client"}
];dojo.widget.html.layout(this.domNode, children);if(this.selectedChildWidget){var containerSize = dojo.html.getContentBox(this.containerNode);this.selectedChildWidget.resizeTo(containerSize.width, containerSize.height);}},onKey: function(e){if(e.keyCode == e.KEY_UP_ARROW && e.ctrlKey){var button = this.correspondingTabButton || this.selectedTabWidget.tabButton;button.focus();dojo.event.browser.stopEvent(e);}else if(e.keyCode == e.KEY_DELETE && e.altKey){if (this.selectedChildWidget.closable){this.closeChild(this.selectedChildWidget);dojo.event.browser.stopEvent(e);}}},destroy: function(){this.tablist.destroy();dojo.widget.TabContainer.superclass.destroy.apply(this, arguments);}});dojo.widget.defineWidget(
"dojo.widget.TabController",dojo.widget.PageController,{templateString: "<div wairole='tablist' dojoAttachEvent='onKey'></div>",labelPosition: "top",doLayout: true,"class": "",buttonWidget: "TabButton",postMixInProperties: function() {if(!this["class"]){this["class"] = "dojoTabLabels-" + this.labelPosition + (this.doLayout ? "" : " dojoTabNoLayout");}
dojo.widget.TabController.superclass.postMixInProperties.apply(this, arguments);}}
);dojo.widget.defineWidget("dojo.widget.TabButton", dojo.widget.PageButton,{templateString: "<div class='dojoTab' dojoAttachEvent='onClick'>"
+"<div dojoAttachPoint='innerDiv'>"
+"<span dojoAttachPoint='titleNode' tabIndex='-1' waiRole='tab'>${this.label}</span>"
+"<span dojoAttachPoint='closeButtonNode' class='close closeImage' style='${this.closeButtonStyle}'"
+"    dojoAttachEvent='onMouseOver:onCloseButtonMouseOver; onMouseOut:onCloseButtonMouseOut; onClick:onCloseButtonClick'></span>"
+"</div>"
+"</div>",postMixInProperties: function(){this.closeButtonStyle = this.closeButton ? "" : "display: none";dojo.widget.TabButton.superclass.postMixInProperties.apply(this, arguments);},fillInTemplate: function(){dojo.html.disableSelection(this.titleNode);dojo.widget.TabButton.superclass.fillInTemplate.apply(this, arguments);},onCloseButtonClick: function( evt){evt.stopPropagation();dojo.widget.TabButton.superclass.onCloseButtonClick.apply(this, arguments);}});dojo.widget.defineWidget(
"dojo.widget.a11y.TabButton",dojo.widget.TabButton,{imgPath: dojo.uri.dojoUri("src/widget/templates/images/tab_close.gif"),templateString: "<div class='dojoTab' dojoAttachEvent='onClick;onKey'>"
+"<div dojoAttachPoint='innerDiv'>"
+"<span dojoAttachPoint='titleNode' tabIndex='-1' waiRole='tab'>${this.label}</span>"
+"<img class='close' src='${this.imgPath}' alt='[x]' style='${this.closeButtonStyle}'"
+"    dojoAttachEvent='onClick:onCloseButtonClick'>"
+"</div>"
+"</div>"}
);