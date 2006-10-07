/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.charting.engine.Axis");
dojo.require("dojo.lang.common");

dojo.widget.charting.engine.Axis = function(/* string? */label, /* string? */scale){
	var id = "dojo-charting-axis-"+dojo.widget.charting.engine.Axis.count++;
	this.getId=function(){ return id; };
	this.setId=function(key){ id = key; };
	this.scale = scale || "linear";		//	linear || log
	this.label = label || "";
	this.showLabels = true;		//	show interval ticks.
	this.nLabels = 3;			//	max ticks over range.
	this.showLines = false;		//	if you want lines over the range of the plot area
	this.showTicks = false;		//	if you want tick marks on the axis.
	this.range = { upper : 0, lower : 0 };	//	range of individual axis.
	this.origin = "min"; 			//	this can be any number, "min" or "max". min/max is translated on init.
};
dojo.widget.charting.engine.Axis.count = 0;

dojo.extend(dojo.widget.charting.engine.Axis, {
	//	TODO: implement log scaling.
	getCoord: function(
		/* float */val, 
		/* dojo.widget.charting.engine.PlotArea */plotArea, 
		/* dojo.widget.charting.engine.Plot */plot
	){
		//	summary
		//	returns the coordinate of val based on this axis range, plot area and plot.
		val = parseFloat(val, 10);
		var area = plotArea.getArea();
		if(plot.axisX == this){
			var offset = 0 - this.range.lower;
			var min = this.range.lower + offset;	//	FIXME: check this.
			var max = this.range.upper + offset;
			val += offset;
			return (val*((area.right-area.left)/max))+area.left;	//	float
		} else {
			var max = this.range.upper;
			var min = this.range.lower;
			var offset = 0;
			if(min<0){
				offset += Math.abs(min);
			}
			max += offset; min += offset; val += offset;
			var pmin = area.bottom;
			var pmax = area.top;
			return (((pmin-pmax)/(max-min))*(max-val))+pmax;
		}
	}
});
