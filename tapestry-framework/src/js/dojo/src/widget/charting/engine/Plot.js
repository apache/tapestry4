/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.charting.engine.Plot");
dojo.require("dojo.lang.common");
dojo.require("dojo.widget.charting.engine.Axis");
dojo.require("dojo.widget.charting.engine.Series");

dojo.widget.charting.engine.RenderPlotSeries = { Singly:"single", Grouped:"grouped" };

dojo.widget.charting.engine.Plot = function(
	/* dojo.widget.charting.engine.Axis? */xaxis, 
	/* dojo.widget.charting.engine.Axis? */yaxis, 
	/* dojo.widget.charting.engine.Series[]? */series
){
	var id = "dojo-charting-plot-"+dojo.widget.charting.engine.Plot.count++;
	this.getId=function(){ return id; };
	this.setId=function(key){ id = key; };
	this.axisX = null;
	this.axisY = null;
	this.series = [];
	this.dataNode = null;

	//	for bar charts, pie charts and stacked charts, change to Grouped.
	this.renderType = dojo.widget.charting.engine.RenderPlotSeries.Singly;
	if(xaxis){
		this.setAxis(xaxis,"x");
	}
	if(yaxis){
		this.setAxis(yaxis,"y");
	}
	if(series){
		for(var i=0; i<series.length; i++){ this.addSeries(series[i]); }
	}
}
dojo.widget.charting.engine.Plot.count=0;

dojo.extend(dojo.widget.charting.engine.Plot, {
	addSeries: function(
		/* dojo.widget.charting.engine.Series || object */series,
		/* function? */plotter
	){
		if(series.plotter){
			this.series.push(series);
		} else {
			this.series.push({
				data: series,
				plotter: plotter || dojo.widget.charting.engine.Plotters["Default"]
			});
		}
	},
	setAxis: function(/* dojo.widget.charting.engine.Axis */axis, /* string */which){
		if(which.toLowerCase()=="x"){ this.axisX = axis; }
		else if(which.toLowerCase()=="y"){ this.axisY = axis; }
	},
	getRanges: function(){
		//	summary
		//	set the ranges on these axes.
		var xmin, xmax, ymin, ymax;
		xmin=ymin=Number.MAX_VALUE;
		xmax=ymax=Number.MIN_VALUE;
		for(var i=0; i<this.series.length; i++){
			var values = this.series[i].data.evaluate();	//	full data range.
			for(var j=0; j<values.length; j++){
				var comp=values[j];
				xmin=Math.min(comp.x, xmin);
				ymin=Math.min(comp.y, ymin);
				xmax=Math.max(comp.x, xmax);
				ymax=Math.max(comp.y, ymax);
			}
		}
		return {
			x:{ upper: xmax, lower:xmin },
			y:{ upper: ymax, lower:ymin },
			toString:function(){
				return "[ x:"+xmax+" - "+xmin+", y:"+ymax+" - "+ymin+"]";
			}
		};	//	object
	},
	destroy: function(){
		var node=this.dataNode;
		while(node && node.childNodes && node.childNodes.length > 0){
			node.removeChild(node.childNodes[0]);
		}
		this.dataNode=null;
	}
});
