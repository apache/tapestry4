/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.charting.engine.vml.Plotters");
dojo.require("dojo.lang.common");

dojo.mixin(dojo.widget.charting.engine.Plotters, {
	/*********************************************************
	 *	Grouped plotters: need all series on a plot at once.
	 *********************************************************/
	Bar: function(
		/* dojo.widget.charting.engine.PlotArea */plotarea,
		/* dojo.widget.charting.engine.Plot */plot,
		/* object? */kwArgs,
		/* function? */applyTo
	){
		var area = plotarea.getArea();
		var group = document.createElement("div");
		group.style.position="absolute";
		group.style.top="0px";
		group.style.left="0px";
		group.style.width=plotarea.size.width+"px";
		group.style.height=plotarea.size.height+"px";
		
		//	calculate the width of each bar.
		var space = 4;
		var n = plot.series.length;
		var width = Math.round(((area.right-area.left)-(space*(n-1)))/n);
		var yOrigin = plot.axisY.getCoord(plot.axisX.origin, plotarea, plot);
		for(var i=0; i<n; i++){
			var series = plot.series[i];
			var data = series.data.evaluate(kwArgs);
			var x = area.left+(width*i)+(space*i);
			var value = data[data.length-1].y;

			var yA = yOrigin;
			var y = plot.axisY.getCoord(value, plotarea, plot);
			var h = Math.abs(yA-y);
			if(value < plot.axisX.origin){
				yA = y;
				y = yOrigin;
			}
			
			var bar=document.createElement("v:rect");
			bar.style.position="absolute";
			bar.style.top=y+1+"px";
			bar.style.left=x+"px";
			bar.style.width=width+"px";
			bar.style.height=h+"px";
			bar.setAttribute("fillColor", data[data.length-1].series.color);
			bar.setAttribute("stroked", "false");
			bar.style.antialias="false";
			var fill=document.createElement("v:fill");
			fill.setAttribute("opacity", "0.65");
			bar.appendChild(fill);
			group.appendChild(bar);
		}
		return group;
	},

	/*********************************************************
	 *	Single plotters: one series at a time.
	 *********************************************************/
	Line: function(
		/* array */data, 
		/* dojo.widget.charting.engine.PlotArea */plotarea,
		/* dojo.widget.charting.engine.Plot */plot,
		/* function? */applyTo
	){
		var tension = 3;
		var area = plotarea.getArea();
		var group=document.createElement("div");
		group.style.position="absolute";
		group.style.top="0px";
		group.style.left="0px";
		group.style.width=plotarea.size.width+"px";
		group.style.height=plotarea.size.height+"px";

		var path=document.createElement("v:shape");
		path.setAttribute("strokeweight", "2px");
		path.setAttribute("strokecolor", data[0].series.color);
		path.setAttribute("fillcolor", "none");
		path.setAttribute("filled", "false");
		path.setAttribute("coordsize", (area.right-area.left) + "," + (area.bottom-area.top));
		path.style.position="absolute";
		path.style.top="0px";
		path.style.left="0px";
		path.style.width=area.right-area.left+"px";
		path.style.height=area.bottom-area.top+"px";
		var stroke=document.createElement("v:stroke");
		stroke.setAttribute("opacity", "0.8");
		path.appendChild(stroke);

		var cmd = [];
		var r=3;
		for(var i=0; i<data.length; i++){
			var x = Math.round(plot.axisX.getCoord(data[i].x, plotarea, plot));
			var y = Math.round(plot.axisY.getCoord(data[i].y, plotarea, plot));

			if (i==0){
				cmd.push("m");
				cmd.push(x+","+y);
			}else{
				var lastx = Math.round(plot.axisX.getCoord(data[i-1].x, plotarea, plot));
				var lasty = Math.round(plot.axisY.getCoord(data[i-1].y, plotarea, plot));
				var dx=x-lastx;
				var dy=y-lasty;
				
				cmd.push("c");
				var cx=Math.round((x-(tension-1)*(dx/tension)));
				cmd.push(cx+","+lasty);
				cx=Math.round((x-(dx/tension)));
				cmd.push(cx+","+y);
				cmd.push(x+","+y);
			}

			//	add the circle.
			var c = document.createElement("v:oval");
			c.setAttribute("strokeweight", "1px");
			c.setAttribute("strokecolor", data[i].series.color);
			c.setAttribute("fillcolor", data[i].series.color);
			var str=document.createElement("v:stroke");
			str.setAttribute("opacity","0.8");
			c.appendChild(str);
			str=document.createElement("v:fill");
			str.setAttribute("opacity","0.6");
			c.appendChild(str);
			var s=c.style;
			s.position="absolute";
			s.top=(y-r)+"px";
			s.left=(x-r)+"px";
			s.width=(r*2)+"px";
			s.height=(r*2)+"px";
			group.appendChild(c);
			if(applyTo){ applyTo(c, data[i].src); }
		}
		path.setAttribute("path", cmd.join(" ")+" e");
		group.appendChild(path);
		return group;
	},
	Area: function(
		/* array */data, 
		/* dojo.widget.charting.engine.PlotArea */plotarea,
		/* dojo.widget.charting.engine.Plot */plot,
		/* function? */applyTo
	){
		var tension = 3;
		var area = plotarea.getArea();
		var group=document.createElement("div");
		group.style.position="absolute";
		group.style.top="0px";
		group.style.left="0px";
		group.style.width=plotarea.size.width+"px";
		group.style.height=plotarea.size.height+"px";

		var path=document.createElement("v:shape");
		path.setAttribute("strokeweight", "1px");
		path.setAttribute("strokecolor", data[0].series.color);
		path.setAttribute("fillcolor", data[0].series.color);
		path.setAttribute("coordsize", (area.right-area.left) + "," + (area.bottom-area.top));
		path.style.position="absolute";
		path.style.top="0px";
		path.style.left="0px";
		path.style.width=area.right-area.left+"px";
		path.style.height=area.bottom-area.top+"px";
		var stroke=document.createElement("v:stroke");
		stroke.setAttribute("opacity", "0.8");
		path.appendChild(stroke);
		var fill=document.createElement("v:fill");
		fill.setAttribute("opacity", "0.4");
		path.appendChild(fill);

		var cmd = [];
		var r=3;
		for(var i=0; i<data.length; i++){
			var x = Math.round(plot.axisX.getCoord(data[i].x, plotarea, plot));
			var y = Math.round(plot.axisY.getCoord(data[i].y, plotarea, plot));

			if (i==0){
				cmd.push("m");
				cmd.push(x+","+y);
			}else{
				var lastx = Math.round(plot.axisX.getCoord(data[i-1].x, plotarea, plot));
				var lasty = Math.round(plot.axisY.getCoord(data[i-1].y, plotarea, plot));
				var dx=x-lastx;
				var dy=y-lasty;
				
				cmd.push("c");
				var cx=Math.round((x-(tension-1)*(dx/tension)));
				cmd.push(cx+","+lasty);
				cx=Math.round((x-(dx/tension)));
				cmd.push(cx+","+y);
				cmd.push(x+","+y);
			}

			//	add the circle.
			var c = document.createElement("v:oval");
			c.setAttribute("strokeweight", "1px");
			c.setAttribute("strokecolor", data[i].series.color);
			c.setAttribute("fillcolor", data[i].series.color);
			var str=document.createElement("v:stroke");
			str.setAttribute("opacity","0.8");
			c.appendChild(str);
			str=document.createElement("v:fill");
			str.setAttribute("opacity","0.6");
			c.appendChild(str);
			var s=c.style;
			s.position="absolute";
			s.top=(y-r)+"px";
			s.left=(x-r)+"px";
			s.width=(r*2)+"px";
			s.height=(r*2)+"px";
			group.appendChild(c);
			if(applyTo){ applyTo(c, data[i].src); }
		}
		cmd.push("l");
		cmd.push(x + "," + Math.round(plot.axisY.getCoord(plot.axisX.origin, plotarea, plot)));
		cmd.push("l");
		cmd.push(Math.round(plot.axisX.getCoord(data[0].x, plotarea, plot)) + "," +  Math.round(plot.axisY.getCoord(plot.axisX.origin, plotarea, plot)));
		path.setAttribute("path", cmd.join(" ")+" e");
		group.appendChild(path);
		return group;
	},
	Scatter: function(
		/* array */data, 
		/* dojo.widget.charting.engine.PlotArea */plotarea,
		/* dojo.widget.charting.engine.Plot */plot,
		/* function? */applyTo
	){
		var r=6;
		var mod=r/2;

		var area = plotarea.getArea();
		var group=document.createElement("div");
		group.style.position="absolute";
		group.style.top="0px";
		group.style.left="0px";
		group.style.width=plotarea.size.width+"px";
		group.style.height=plotarea.size.height+"px";

		for(var i=0; i<data.length; i++){
			var x = Math.round(plot.axisX.getCoord(data[i].x, plotarea, plot));
			var y = Math.round(plot.axisY.getCoord(data[i].y, plotarea, plot));

			var point = document.createElement("v:rect");
			point.setAttribute("strokecolor", data[i].series.color);
			point.setAttribute("fillcolor", data[i].series.color);
			var fill=document.createElement("v:fill");
			fill.setAttribute("opacity","0.6");
			point.appendChild(fill);

			var s=point.style;
			s.position="absolute";
			s.rotation="45";
			s.top=(y-mod)+"px";
			s.left=(x-mod)+"px";
			s.width=r+"px";
			s.height=r+"px";
			group.appendChild(point);
			if(applyTo){ applyTo(point, data[i].src); }
		}
		return group;
	},
	Bubble: function(
		/* array */data, 
		/* dojo.widget.charting.engine.PlotArea */plotarea,
		/* dojo.widget.charting.engine.Plot */plot,
		/* function? */applyTo
	){
		var sizeFactor=1;
		var area = plotarea.getArea();
		var group=document.createElement("div");
		group.style.position="absolute";
		group.style.top="0px";
		group.style.left="0px";
		group.style.width=plotarea.size.width+"px";
		group.style.height=plotarea.size.height+"px";

		for(var i=0; i<data.length; i++){
			var x = Math.round(plot.axisX.getCoord(data[i].x, plotarea, plot));
			var y = Math.round(plot.axisY.getCoord(data[i].y, plotarea, plot));
			if(i==0){
				//	figure out the size factor, start with the axis with the greater range.
				var raw = data[i].size;
				var dy = plot.axisY.getCoord(data[i].y + raw, plotarea, plot)-y;
				sizeFactor = dy/raw;
			}
			if(sizeFactor<1) { sizeFactor = 1; }
			var r = (data[i].size/2)*sizeFactor;

			var point = document.createElement("v:oval");
			point.setAttribute("strokecolor", data[i].series.color);
			point.setAttribute("fillcolor", data[i].series.color);
			var fill=document.createElement("v:fill");
			fill.setAttribute("opacity","0.6");
			point.appendChild(fill);

			var s=point.style;
			s.position="absolute";
			s.rotation="45";
			s.top=(y-r)+"px";
			s.left=(x-r)+"px";
			s.width=(r*2)+"px";
			s.height=(r*2)+"px";
			group.appendChild(point);
			if(applyTo){ applyTo(point, data[i].src); }
		}
		return group;
	}
});
dojo.widget.charting.engine.Plotters["Default"] = dojo.widget.charting.engine.Plotters.Line;
