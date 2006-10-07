/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.charting.engine.Chart");
dojo.require("dojo.lang.common");
dojo.require("dojo.widget.charting.engine.PlotArea");

dojo.widget.charting.engine.Chart = function(
	/* HTMLElement? */node, 
	/* string? */title, 
	/* string? */description
){
	this.node = node || null;
	this.title = title || "Chart";			//	pure string.
	this.description = description || "";	//	HTML is allowed.
	this.plotAreas = [];
};

dojo.extend(dojo.widget.charting.engine.Chart, {
	//	methods
	addPlotArea: function(/* object */obj, /* bool? */doRender){
		//	we expect object to be: { plotArea, (x, y) or (top, left) }
		if(obj.x && !obj.left){ obj.left = obj.x; }
		if(obj.y && !obj.top){ obj.top = obj.y; }
		this.plotAreas.push(obj);
		if(doRender){ this.render(); }
	},
	
	//	events
	onInitialize:function(chart){ },
	onRender:function(chart){ },
	onDestroy:function(chart){ },

	//	standard build methods
	initialize: function(){
		if(!this.node){ 
			dojo.raise("dojo.widget.charting.engine.Chart.initialize: there must be a root node defined for the Chart."); 
		}
		this.destroy();
		this.render();
	},
	render:function(){
		if(this.node.style.position != "absolute"){
			this.node.style.position = "relative";
		}
		for(var i=0; i<this.plotAreas.length; i++){
			var area = this.plotAreas[i].plotArea;
			var node = area.initialize();
			node.style.position = "absolute";
			node.style.top = this.plotAreas[i].top + "px";
			node.style.left = this.plotAreas[i].left + "px";
			this.node.appendChild(node);
			area.render();
		}
	},
	destroy: function(){
		//	kill any existing plotAreas
		for(var i=0; i<this.plotAreas.length; i++){
			this.plotAreas[i].plotArea.destroy();
		};
		//	clean out any child nodes.
		while(this.node && this.node.childNodes && this.node.childNodes.length > 0){ 
			this.node.removeChild(this.node.childNodes[0]); 
		}
	}
});
