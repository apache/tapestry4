
dojo.provide("dojo.widget.TreeToggleOnSelect");dojo.require("dojo.widget.HtmlWidget");dojo.widget.defineWidget(
"dojo.widget.TreeToggleOnSelect",dojo.widget.HtmlWidget,{selector: "",controller: "",selectEvent: "select",initialize: function() {this.selector = dojo.widget.byId(this.selector);this.controller = dojo.widget.byId(this.controller);dojo.event.topic.subscribe(this.selector.eventNames[this.selectEvent], this, "onSelectEvent");},onSelectEvent: function(message) {var node = message.node
node.isExpanded ? this.controller.collapse(node) : this.controller.expand(node)}});