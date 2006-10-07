/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.charting.engine.svg.PlotArea");
dojo.require("dojo.lang.common");
dojo.require("dojo.svg");

dojo.extend(dojo.widget.charting.engine.PlotArea, {
	initializePlot: function(plot){
		//	summary
		//	Initialize the plot node for data rendering.
		plot.destroy();
		plot.dataNode = document.createElementNS(dojo.svg.xmlns.svg, "g");
		plot.dataNode.setAttribute("id", plot.getId());
		return plot.dataNode;	//	SVGElement
	},
	initializeAxis: function(axisObject){
		//	summary
		//	Initialize the passed axis descriptor.  Note that this should always
		//	be the result of this.getAxes, and not the axis directly!
		function createLabel(label, x, y, anchor){
			var text = document.createElementNS(dojo.svg.xmlns.svg, "text");
			text.setAttribute("x", x);
			text.setAttribute("y", y);
			text.setAttribute("style", "text-anchor:"+anchor+";font-size:"+textSize+"px;fill:#000;");
			text.appendChild(document.createTextNode(label));
			return text;
		};

		var area = this.getArea();
		var stroke = 1;
		var style = "stroke:#000;stroke-width:"+stroke+"px;";
		var axis = axisObject.axis;
		var textSize=10;

		//	figure out the origin value.
		if(isNaN(axis.origin)){
			if(axis.origin.toLowerCase() == "max"){ 
				axis.origin = axisObject.drawAgainst.range[(axisObject.plane=="y")?"upper":"lower"]; 
			}
			else if (axis.origin.toLowerCase() == "min"){ 
				axis.origin = axisObject.drawAgainst.range[(axisObject.plane=="y")?"lower":"upper"]; 
			}
			else { axis.origin=0; }
		}

		//	get the origin plot point.
		var coord = axisObject.drawAgainst.getCoord(axis.origin, this, axisObject.plot);
		
		var group = document.createElementNS(dojo.svg.xmlns.svg, "g");
		group.setAttribute("id", axis.getId());	//	need a handle if we have to kill parts of the axis def.
		var line = document.createElementNS(dojo.svg.xmlns.svg, "line");
		if(axisObject.plane == "x"){
			line.setAttribute("y1", coord);
			line.setAttribute("y2", coord);
			line.setAttribute("x1", area.left-stroke);
			line.setAttribute("x2", area.right+stroke);
			line.setAttribute("style", style);

			//	set up the labels
			if(axis.showLabels){
				//	the way we treat nLabels is to include the min/max points of the axis.
				//	always do the max point.
				var y = coord+textSize+2;
				var g = document.createElementNS(dojo.svg.xmlns.svg, "g");
				g.setAttribute("id", axis.getId()+"-labels");
				g.appendChild(createLabel(Math.round(axis.range.upper),(area.right-(textSize/2)),y, "middle"));
				if(axis.nLabels > 1){
					g.appendChild(createLabel(Math.round(axis.range.lower), area.left, y, "middle"));
				}
				var n = axis.nLabels-2;
				if(n>0){
					var length = area.right-area.left;
					var step = length/(n+1);
					var axisRange = axis.range.upper-axis.range.lower;
					for(var i=1; i<=n; i++){
						g.appendChild(createLabel(Math.round(axis.range.lower+(i*(axisRange/(n+1)))), area.left+(i*step), y, "middle"));
						if(axis.showLines){
							var l=document.createElementNS(dojo.svg.xmlns.svg, "line");
							l.setAttribute("style","stroke:#999;stroke-width:0.5pt;stroke-dasharray:3,5;");
							l.setAttribute("y1",area.top);
							l.setAttribute("y2",area.bottom);
							l.setAttribute("x1",area.left+(i*step));
							l.setAttribute("x2",area.left+(i*step));
							g.appendChild(l);
						}
						if(axis.showTicks){
							var l=document.createElementNS(dojo.svg.xmlns.svg, "line");
							l.setAttribute("style","stroke:#000;stroke-width:1pt;");
							l.setAttribute("y1",coord);
							l.setAttribute("y2",coord+3);
							l.setAttribute("x1",area.left+(i*step));
							l.setAttribute("x2",area.left+(i*step));
							g.appendChild(l);
						}
					}
				}
				group.appendChild(g);
			}
		} else {
			line.setAttribute("x1", coord);
			line.setAttribute("x2", coord);
			line.setAttribute("y1", area.top);
			line.setAttribute("y2", area.bottom);
			line.setAttribute("style", style);

			//	set up the labels
			if(axis.showLabels){
				//	the way we treat nLabels is to include the min/max points of the axis.
				//	always do the max point.
				var isMax = axis.origin == axisObject.drawAgainst.range.upper;
				var x = coord + (isMax?4:-4);
				var anchor = isMax?"start":"end";
				var g = document.createElementNS(dojo.svg.xmlns.svg, "g");
				g.setAttribute("id", axis.getId()+"-labels");
				g.appendChild(createLabel(Math.round(axis.range.upper), x, (area.top+(textSize/2)), anchor));
				if(axis.nLabels > 1){
					g.appendChild(createLabel(Math.round(axis.range.lower), x, area.bottom, anchor));
				}
				var n = axis.nLabels-2;
				if(n>0){
					var length = area.bottom-area.top;
					var step = length/(n+1);
					var axisRange = axis.range.upper-axis.range.lower;
					for(var i=1; i<=n; i++){
						g.appendChild(createLabel(Math.round(axis.range.upper-(i*(axisRange/(n+1)))), x, area.top+(i*step)+(textSize/2)-2, anchor));
						if(axis.showLines){
							var l=document.createElementNS(dojo.svg.xmlns.svg, "line");
							l.setAttribute("style","stroke:#999;stroke-width:0.5pt;stroke-dasharray:3,5;");
							l.setAttribute("y1",area.top+(i*step));
							l.setAttribute("y2",area.top+(i*step));
							l.setAttribute("x1",area.left);
							l.setAttribute("x2",area.right);
							g.appendChild(l);
						}
						if(axis.showTicks){
							var l=document.createElementNS(dojo.svg.xmlns.svg, "line");
							l.setAttribute("style","stroke:#999;stroke-width:1");
							l.setAttribute("y1",area.top+(i*step));
							l.setAttribute("y2",area.top+(i*step));
							l.setAttribute("x1",coord-2);
							l.setAttribute("x2",coord+2);
							g.appendChild(l);
						}
					}
				}
				group.appendChild(g);
			}
		}
		group.appendChild(line);
		return group;
	},

	initialize: function(){
		this.destroy();	//	kill everything first.
		
		//	start with the background
		this.nodes.main = document.createElement("div");

		this.nodes.area = document.createElementNS(dojo.svg.xmlns.svg, "svg");
		this.nodes.area.setAttribute("id", this.getId());
		this.nodes.area.setAttribute("width", this.size.width);
		this.nodes.area.setAttribute("height", this.size.height);
		this.nodes.main.appendChild(this.nodes.area);

		var area=this.getArea();
		var defs = document.createElementNS(dojo.svg.xmlns.svg, "defs");
		var clip = document.createElementNS(dojo.svg.xmlns.svg, "clipPath");
		clip.setAttribute("id",this.getId()+"-clip");
		var rect = document.createElementNS(dojo.svg.xmlns.svg, "rect");		
		rect.setAttribute("x", area.left);
		rect.setAttribute("y", area.top);
		rect.setAttribute("width", area.right-area.left);
		rect.setAttribute("height", area.bottom-area.top);
		clip.appendChild(rect);
		defs.appendChild(clip);
		this.nodes.area.appendChild(defs);
		
		this.nodes.background = document.createElementNS(dojo.svg.xmlns.svg, "rect");
		this.nodes.background.setAttribute("id", this.getId()+"-background");
		this.nodes.background.setAttribute("width", this.size.width);
		this.nodes.background.setAttribute("height", this.size.height);
		this.nodes.background.setAttribute("fill", "#fff");
		this.nodes.area.appendChild(this.nodes.background);

		this.nodes.plots = document.createElementNS(dojo.svg.xmlns.svg, "g");
		this.nodes.plots.setAttribute("id", this.getId()+"-plots");
		this.nodes.plots.setAttribute("style","clip-path:url(#"+this.getId()+"-clip);");
		this.nodes.area.appendChild(this.nodes.plots);

		for(var i=0; i<this.plots.length; i++){
			this.nodes.plots.appendChild(this.initializePlot(this.plots[i]));
		}

		//	do the axes
		this.nodes.axes = document.createElementNS(dojo.svg.xmlns.svg, "g");
		this.nodes.axes.setAttribute("id", this.getId()+"-axes");
		this.nodes.area.appendChild(this.nodes.axes);
		var axes = this.getAxes();
		for(var p in axes){
			this.nodes.axes.appendChild(this.initializeAxis(axes[p]));
		}
		return this.nodes.main;
	}
});
