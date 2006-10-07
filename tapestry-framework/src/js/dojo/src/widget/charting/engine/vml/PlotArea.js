/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.charting.engine.vml.PlotArea");
dojo.require("dojo.lang.common");

dojo.extend(dojo.widget.charting.engine.PlotArea, {
	initializePlot: function(plot){
		//	summary
		//	Initialize the plot node for data rendering.
		plot.destroy();
		plot.dataNode = document.createElement("div");
		plot.dataNode.id  = plot.getId();
		return plot.dataNode;	//	HTMLElement
	},
	initializeAxis: function(axisObject){
		function createLabel(label, x, y, anchor){
			var text = document.createElement("div");
			var s=text.style;
			text.innerHTML=label;
			s.fontSize=textSize+"px";
			s.fontFamily="sans-serif";
			s.position="absolute";
			s.top = y+"px";
			if(anchor == "center"){
				s.left = x + "px";
				s.textAlign="center";
			} else if (anchor == "left"){
				s.left = x + "px";
				s.textAlign="left";
			} else if (anchor == "right"){
				s.right = x + "px";
				s.textAlign="right";
			}
			return text;
		}

		var area = this.getArea();
		var axis = axisObject.axis;
		var stroke = 1;
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
		
		var group = document.createElement("div");
		group.id = axis.getId();	//	need a handle if we have to kill parts of the axis def.
		var line = document.createElement("v:line");
		if(axisObject.plane == "x"){
			line.setAttribute("from", area.left+"px,"+coord+"px");
			line.setAttribute("to", area.right+"px,"+coord+"px");
			line.setAttribute("strokecolor", "#000");
			line.setAttribute("strokeweight", stroke+"px");
			var s=line.style;
			s.position="absolute";
			s.top="0px";
			s.left="0px";
			s.antialias="false";

			//	set up the labels
			if(axis.showLabels){
				//	the way we treat nLabels is to include the min/max points of the axis.
				//	always do the max point.
				var y = coord + Math.floor(textSize/2);
				var g = document.createElement("div");
				g.setAttribute("id", axis.getId()+"-labels");
				g.appendChild(createLabel(Math.round(axis.range.upper), area.right - textSize, y, "center"));
				if(axis.nLabels > 1){
					g.appendChild(createLabel(Math.round(axis.range.lower), area.left, y, "center"));
				}
				var n = axis.nLabels-2;
				if(n>0){
					var length = area.right-area.left;
					var step = length/(n+1);
					var axisRange = axis.range.upper-axis.range.lower;
					for(var i=1; i<=n; i++){
						var lb=Math.round(axis.range.lower+(i*(axisRange/(n+1))))+"";
						g.appendChild(createLabel(lb, area.left+(i*step)-Math.floor(textSize/2), y, "center"));
						if(axis.showLines){
							var l=document.createElement("v:line");
							var str=document.createElement("v:stroke");
							str.dashstyle="dot";
							l.appendChild(str);
							l.setAttribute("from", (area.left+(i*step))+"px,"+area.top+"px");
							l.setAttribute("to", (area.left+(i*step))+"px,"+area.bottom+"px");
							l.setAttribute("strokecolor", "#666");
							l.setAttribute("strokeweight", stroke+"px");
							var s=line.style;
							s.position="absolute";
							s.top="0px";
							s.left="0px";
							s.antialias="false";
							g.appendChild(l);
						}
						if(axis.showTicks){
							var l=document.createElement("v:line");
							l.setAttribute("from", (area.left+(i*step))+"px,"+coord+"px");
							l.setAttribute("to", (area.left+(i*step))+"px,"+(coord+3)+"px");
							l.setAttribute("strokecolor", "#000");
							l.setAttribute("strokeweight", stroke+"px");
							var s=line.style;
							s.position="absolute";
							s.top="0px";
							s.left="0px";
							s.antialias="false";
							g.appendChild(l);
						}
					}
				}
				group.appendChild(g);
			}
		} else {
			line.setAttribute("from", coord+"px,"+area.top+"px");
			line.setAttribute("to", coord+"px,"+area.bottom+"px");
			line.setAttribute("strokecolor", "#000");
			line.setAttribute("strokeweight", stroke+"px");
			var s=line.style;
			s.position="absolute";
			s.top="0px";
			s.left="0px";
			s.antialias="false";
			
			//	set up the labels
			if(axis.showLabels){
				//	the way we treat nLabels is to include the min/max points of the axis.
				//	always do the max point.
				var isMax = axis.origin == axisObject.drawAgainst.range.upper;
				var x = coord+4;
				var anchor = "left";
				if(!isMax){
					x = area.right-coord+textSize+2;
					if(coord == area.left) x+=textSize*2;
					anchor="right";
				}
				var g = document.createElement("div");
				g.setAttribute("id", axis.getId()+"-labels");
				g.appendChild(createLabel(Math.round(axis.range.upper), x, (area.top-(textSize/2)), anchor));
				if(axis.nLabels > 1){
					g.appendChild(createLabel(Math.round(axis.range.lower), x, (area.bottom-textSize), anchor));
				}
				var n = axis.nLabels-2;
				if(n>0){
					var length = area.bottom-area.top;
					var step = length/(n+1);
					var axisRange = axis.range.upper-axis.range.lower;
					for(var i=1; i<=n; i++){
						var lb = Math.round(axis.range.upper-(i*(axisRange/(n+1))));
						g.appendChild(createLabel(lb, x, area.top+(i*step)-Math.ceil(textSize/2), anchor));
						if(axis.showLines){
							var l=document.createElement("v:line");
							var str=document.createElement("v:stroke");
							str.dashstyle="dot";
							l.appendChild(str);
							l.setAttribute("from", area.left+"px,"+(area.top+(i*step))+"px");
							l.setAttribute("to", area.right+"px,"+(area.top+(i*step))+"px");
							l.setAttribute("strokecolor", "#666");
							l.setAttribute("strokeweight", stroke+"px");
							var s=line.style;
							s.position="absolute";
							s.top="0px";
							s.left="0px";
							s.antialias="false";
							g.appendChild(l);
						}
						if(axis.showTicks){
							var l=document.createElement("v:line");
							l.setAttribute("from", (coord-2)+"px,"+(area.top+(i*step))+"px");
							l.setAttribute("to", (coord+2)+"px,"+(area.top+(i*step))+"px");
							l.setAttribute("strokecolor", "#000");
							l.setAttribute("strokeweight", stroke+"px");
							var s=line.style;
							s.position="absolute";
							s.top="0px";
							s.left="0px";
							s.antialias="false";
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
	initialize:function(){
		this.destroy();	//	kill everything first.
		var main = this.nodes.main = document.createElement("div");
		
		//	start with the background
		var area = this.nodes.area = document.createElement("div");
		area.id = this.getId();
		area.style.width=this.size.width+"px";
		area.style.height=this.size.height+"px";
		area.style.position="absolute";
		main.appendChild(area);
	
		var bg = this.nodes.background = document.createElement("div");
		bg.id = this.getId()+"-background";
		bg.style.width=this.size.width+"px";
		bg.style.height=this.size.height+"px";
		bg.style.position="absolute";
		bg.style.top="0px";
		bg.style.left="0px";
		bg.style.backgroundColor="#fff";
		area.appendChild(bg);

		//	the plot group
		var a=this.getArea();
		var plots = this.nodes.plots = document.createElement("div");
		plots.id = this.getId()+"-plots";
		plots.style.width=this.size.width+"px";
		plots.style.height=this.size.height+"px";
		plots.style.position="absolute";
		plots.style.top="0px";
		plots.style.left="0px";
		plots.style.clip="rect("
			+ a.top+" "
			+ a.right+" "
			+ a.bottom+" "
			+ a.left
			+")";
		area.appendChild(plots);
		for(var i=0; i<this.plots.length; i++){
			plots.appendChild(this.initializePlot(this.plots[i]));
		}

		var axes = this.nodes.axes = document.createElement("div");
		axes.id = this.getId() + "-axes";
		area.appendChild(axes);
		var ax = this.getAxes();
		for(var p in ax){
			axes.appendChild(this.initializeAxis(ax[p]));
		}
		return main;
	}
});
