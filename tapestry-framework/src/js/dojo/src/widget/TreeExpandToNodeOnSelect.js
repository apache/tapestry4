
dojo.provide("dojo.widget.TreeExpandToNodeOnSelect");dojo.require("dojo.widget.HtmlWidget");dojo.widget.defineWidget(
"dojo.widget.TreeExpandToNodeOnSelect",dojo.widget.HtmlWidget,{selector: "",controller: "",withSelected: false,initialize: function() {this.selector = dojo.widget.byId(this.selector);this.controller = dojo.widget.byId(this.controller);dojo.event.topic.subscribe(this.selector.eventNames.select, this, "onSelectEvent");},onSelectEvent: function(message) {this.controller.expandToNode(message.node, this.withSelected)
}});