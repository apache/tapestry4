/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.gfx.vml");

dojo.require("dojo.dom");
dojo.require("dojo.math");
dojo.require("dojo.lang.declare");
dojo.require("dojo.lang.extras");
dojo.require("dojo.string.*");

dojo.require("dojo.gfx.color");
dojo.require("dojo.gfx.common");

dojo.require("dojo.experimental");
dojo.experimental("dojo.gfx.vml");

dojo.gfx.vml.xmlns = "urn:schemas-microsoft-com:vml";

dojo.gfx.vml._parseFloat = function(str) {
	return str.match(/^\d+f$/i) ? parseInt(str) / 65536 : parseFloat(str);
};

dojo.gfx.vml.normalizedLength = function(len) {
	// FIXME: why 1pt = 0.75px ?
	return len.indexOf("pt") >= 0 ? parseFloat(len) / 0.75 : parseFloat(len);
};

dojo.lang.extend(dojo.gfx.Shape, {
	setStroke: function(stroke){
		if(!stroke){
			// don't stroke
			this.strokeStyle = null;
			this.rawNode.stroked = false;
			return this;
		}
		// normalize the stroke
		this.strokeStyle = dojo.gfx.makeParameters(dojo.gfx.defaultStroke, stroke);
		this.strokeStyle.color = dojo.gfx.normalizeColor(this.strokeStyle.color);
		// generate attributes
		var s = this.strokeStyle;
		this.rawNode.stroked = true;
		this.rawNode.strokecolor = s.color.toCss();
		this.rawNode.strokeweight = s.width + "px";	// TODO: should we assume that the width is always in pixels?
		if(this.rawNode.stroke) {
			this.rawNode.stroke.opacity = s.color.a;
			this.rawNode.stroke.endcap = this._translate(this._capMap, s.cap);
			if(typeof(s.join) == "number") {
				this.rawNode.stroke.joinstyle = "miter";
				this.rawNode.stroke.miterlimit = s.join;
			}else{
				this.rawNode.stroke.joinstyle = s.join;
				// this.rawNode.stroke.miterlimit = s.width;
			}
		}
		return this;
	},
	
	_capMap: { butt: 'flat' },
	_capMapReversed: { flat: 'butt' },
	
	_translate: function(dict, value) {
		return (value in dict) ? dict[value] : value;
	},
	
	setFill: function(fill){
		if(!fill){
			// don't fill
			this.fillStyle = null;
			this.rawNode.filled = false;
			return this;
		}
		if(typeof(fill) == "object" && "type" in fill){
			// gradient
			switch(fill.type){
				case "linear":
					var f = dojo.gfx.makeParameters(dojo.gfx.defaultLinearGradient, fill);
					this.fillStyle = f;
					var s = "";
					for(var i = 0; i < f.colors.length; ++i){
						f.colors[i].color = dojo.gfx.normalizeColor(f.colors[i].color);
						s += f.colors[i].offset.toFixed(8) + " " + f.colors[i].color.toHex() + ";";
					}
					var fo = this.rawNode.fill;
					fo.colors.value = s;
					fo.method = "sigma";
					fo.type = "gradient";
					fo.angle = (dojo.math.radToDeg(Math.atan2(f.x2 - f.x1, f.y2 - f.y1)) + 180) % 360;
					fo.on = true;
					break;
				case "radial":
					var f = dojo.gfx.makeParameters(dojo.gfx.defaultRadialGradient, fill);
					this.fillStyle = f;
					var w = parseFloat(this.rawNode.style.width);
					var h = parseFloat(this.rawNode.style.height);
					var c = isNaN(w) ? 1 : 2 * f.r / w;
					var i = f.colors.length - 1;
					f.colors[i].color = dojo.gfx.normalizeColor(f.colors[i].color);
					var s = "0 " + f.colors[i].color.toHex();
					for(; i >= 0; --i){
						f.colors[i].color = dojo.gfx.normalizeColor(f.colors[i].color);
						s += (1 - c * f.colors[i].offset).toFixed(8) + " " + f.colors[i].color.toHex() + ";";
					}
					var fo = this.rawNode.fill;
					fo.colors.value = s;
					fo.method = "sigma";
					fo.type = "gradientradial";
					if(isNaN(w) || isNaN(h)){
						fo.focusposition = "0.5 0.5";
					}else{
						fo.focusposition = (f.cx / w).toFixed(8) + " " + (f.cy / h).toFixed(8);
					}
					fo.focussize = "0 0";
					fo.on = true;
					break;
				case "pattern":
					var f = dojo.gfx.makeParameters(dojo.gfx.defaultPattern, fill);
					this.fillStyle = f;
					var fo = this.rawNode.fill;
					fo.type = "tile";
					fo.src = f.src;
					if(f.width && f.height){
						// in points
						fo.size.x = 0.75 * f.width;
						fo.size.y = 0.75 * f.height;
					}
					fo.alignShape = false;
					fo.position.x = 0;
					fo.position.y = 0;
					fo.origin.x = f.width  ? f.x / f.width  : 0;
					fo.origin.y = f.height ? f.y / f.height : 0;
					fo.on = true;
					break;
			}
			this.rawNode.fill.opacity = 1;
			return this;
		}
		// color object
		this.fillStyle = dojo.gfx.normalizeColor(fill);
		this.rawNode.fillcolor = this.fillStyle.toHex();
		this.rawNode.fill.opacity = this.fillStyle.a;
		this.rawNode.filled = true;
		return this;
	},

	_applyTransform: function() {
		var matrix = this._getRealMatrix();
		if(!matrix) return this;
		var skew = this.rawNode.skew;
		if(typeof(skew) == "undefined"){
			for(var i = 0; i < this.rawNode.childNodes.length; ++i){
				if(this.rawNode.childNodes[i].tagName == "skew"){
					skew = this.rawNode.childNodes[i];
					break;
				}
			}
		}
		if(skew){
			skew.on = false;
			var mt = matrix.xx.toFixed(8) + " " + matrix.xy.toFixed(8) + " " + 
				matrix.yx.toFixed(8) + " " + matrix.yy.toFixed(8) + " 0 0";
			var offset = Math.floor(matrix.dx).toFixed() + "px " + Math.floor(matrix.dy).toFixed() + "px";
			var l = parseFloat(this.rawNode.style.left);
			var t = parseFloat(this.rawNode.style.top);
			var w = parseFloat(this.rawNode.style.width);
			var h = parseFloat(this.rawNode.style.height);
			if(isNaN(l)) l = 0;
			if(isNaN(t)) t = 0;
			if(isNaN(w)) w = 1;
			if(isNaN(h)) h = 1;
			var origin = (-l / w - 0.5).toFixed(8) + " " + (-t / h - 0.5).toFixed(8);
			skew.matrix =  mt;
			skew.origin = origin;
			skew.offset = offset;
			skew.on = true;
		}
		return this;
	},

	setRawNode: function(rawNode){
		rawNode.stroked = false;
		rawNode.filled  = false;
		this.rawNode = rawNode;
	},

	// Attach family
	attachStroke: function(rawNode) {
		var strokeStyle = dojo.lang.shallowCopy(dojo.gfx.defaultStroke, true);
		if(rawNode && rawNode.stroked){
			strokeStyle.color = new dojo.gfx.color.Color(rawNode.strokecolor.value);
			dojo.debug("We are expecting an .75pt here, instead of strokeweight = " + rawNode.strokeweight );
			strokeStyle.width = dojo.gfx.vml.normalizedLength(rawNode.strokeweight+"");
			strokeStyle.color.a = rawNode.stroke.opacity;
			strokeStyle.cap = this._translate(this._capMapReversed, rawNode.stroke.endcap);
			strokeStyle.join = rawNode.stroke.joinstyle == "miter" ? rawNode.stroke.miterlimit : rawNode.stroke.joinstyle;
		}else{
			return null;
		}
		return strokeStyle;
	},

	attachFill: function(rawNode){
		var fillStyle = null;
		var fo = rawNode.fill;
		if(rawNode) {
			if(fo.on && fo.type == "gradient"){
				var fillStyle = dojo.lang.shallowCopy(dojo.gfx.defaultLinearGradient, true);
				var rad = dojo.math.degToRad(fo.angle);
				fillStyle.x2 = Math.cos(rad);
				fillStyle.y2 = Math.sin(rad);
				fillStyle.colors = [];
				var stops = fo.colors.value.split(";");
				for(var i = 0; i < stops.length; ++i){
					var t = stops[i].match(/\S+/g);
					if(!t || t.length != 2) continue;
					fillStyle.colors.push({offset: dojo.gfx.vml._parseFloat(t[0]), color: new dojo.gfx.color.Color(t[1])});
				}
			}else if(fo.on && fo.type == "gradientradial"){
				var fillStyle = dojo.lang.shallowCopy(dojo.gfx.defaultRadialGradient, true);
				var w = parseFloat(rawNode.style.width);
				var h = parseFloat(rawNode.style.height);
				fillStyle.cx = isNaN(w) ? 0 : fo.focusposition.x * w;
				fillStyle.cy = isNaN(h) ? 0 : fo.focusposition.y * h;
				fillStyle.r  = isNaN(w) ? 1 : w / 2;
				fillStyle.colors = [];
				var stops = fo.colors.value.split(";");
				for(var i = stops.length - 1; i >= 0; --i){
					var t = stops[i].match(/\S+/g);
					if(!t || t.length != 2) continue;
					fillStyle.colors.push({offset: dojo.gfx.vml._parseFloat(t[0]), color: new dojo.gfx.color.Color(t[1])});
				}
			}else if(fo.on && fo.type == "tile"){
				var fillStyle = dojo.lang.shallowCopy(dojo.gfx.defaultPattern, true);
				fillStyle.width  = fo.size.x / 0.75; // from pt
				fillStyle.height = fo.size.y / 0.75; // from pt
				fillStyle.x = fo.origin.x * fillStyle.width;
				fillStyle.y = fo.origin.y * fillStyle.height;
				fillStyle.src = fo.src;
			}else if(rawNode.fillcolor){
				// a color object !
				fillStyle = new dojo.gfx.color.Color(rawNode.fillcolor+"");
				fillStyle.a = fo.opacity;
			}
		}
		return fillStyle;
	},

	attachTransform: function(rawNode) {
		var matrix = {};
		if(rawNode){
			var s = rawNode.skew;
			matrix.xx = s.matrix.xtox;
			matrix.xy = s.matrix.xtoy;
			matrix.yx = s.matrix.ytox;
			matrix.yy = s.matrix.ytoy;
			// TODO: transform from pt to px
			matrix.dx = s.offset.x / 0.75;
			matrix.dy = s.offset.y / 0.75;
		}
		return dojo.gfx.matrix.normalize(matrix);
	},

	attach: function(rawNode){
		if(rawNode){
			this.rawNode = rawNode;
			this.shape = this.attachShape(rawNode);
			this.fillStyle = this.attachFill(rawNode);
			this.strokeStyle = this.attachStroke(rawNode);
			this.matrix = this.attachTransform(rawNode);
		}
	}
});

dojo.declare("dojo.gfx.Group", dojo.gfx.VirtualGroup, {
	attach: function(rawNode){
		if(rawNode){
			this.rawNode = rawNode;
			this.shape = null;
			this.fillStyle = null;
			this.strokeStyle = null;
			this.matrix = null;
		}
	},
	add: function(shape){
		if(this != shape.getParent()){
			this.rawNode.appendChild(shape.rawNode);
			this.inherited("add", [shape]);
		}
		return this;
	},
	remove: function(shape, silently){
		if(this == shape.getParent()){
			if(this.rawNode == shape.rawNode.parentNode){
				this.rawNode.removeChild(shape.rawNode);
			}
			this.inherited("remove", [shape, silently]);
		}
		return this;
	}
});
dojo.gfx.Group.nodeType = "group";

var zIndex = {
	moveToFront: function(){
		this.rawNode.parentNode.appendChild(this.rawNode);
		return this;
	},
	moveToBack: function(){
		this.rawNode.parentNode.insertBefore(this.rawNode, this.rawNode.parentNode.firstChild);
		return this;
	}
};
dojo.lang.extend(dojo.gfx.Shape, zIndex);
dojo.lang.extend(dojo.gfx.Group, zIndex);
delete zIndex;

dojo.declare("dojo.gfx.Rect", dojo.gfx.Shape, {
	initializer: function(rawNode) {
		this.shape = dojo.lang.shallowCopy(dojo.gfx.defaultRect, true);
		this.attach(rawNode);
	},
	setShape: function(newShape){
		var shape = this.shape = dojo.gfx.makeParameters(this.shape, newShape);
		var style = this.rawNode.style;
		style.left   = shape.x.toFixed();
		style.top    = shape.y.toFixed();
		style.width  = (typeof(shape.width) == "string" && shape.width.indexOf("%") >= 0)  ? shape.width  : shape.width.toFixed();
		style.height = (typeof(shape.width) == "string" && shape.height.indexOf("%") >= 0) ? shape.height : shape.height.toFixed();
		var r = Math.min(1, (shape.r / Math.min(parseFloat(shape.width), parseFloat(shape.height)))).toFixed(8);
		// a workaround for the VML's arcsize bug: cannot read arcsize of an instantiated node
		var parent = this.rawNode.parentNode;
		var before = null;
		if(parent){
			if(parent.lastChild != this.rawNode){
				for(var i = 0; i < parent.childNodes.length; ++i){
					if(parent.childNodes[i] == this.rawNode){
						before = parent.childNodes[i+1];
						break;
					}
				}
			}
			parent.removeChild(this.rawNode);
		}
		this.rawNode.arcsize = r;
		if(parent){
			if(before){
				parent.insertBefore(this.rawNode, before);
			}else{
				parent.appendChild(this.rawNode);
			}
		}
		return this.setTransform(this.matrix);
	},
	attachShape: function(rawNode){
		// a workaround for the VML's arcsize bug: cannot read arcsize of an instantiated node
		var arcsize = rawNode.outerHTML.match(/arcsize = \"(\d*\.?\d+[%f]?)\"/)[1];
		arcsize = (arcsize.indexOf("%") >= 0) ? parseFloat(arcsize) / 100 : dojo.gfx.vml._parseFloat(arcsize);
		var width  = parseFloat(rawNode.style.width);
		var height = parseFloat(rawNode.style.height);
		// make an object
		return dojo.gfx.makeParameters(dojo.gfx.defaultRect, {
			x: parseInt(rawNode.style.left),
			y: parseInt(rawNode.style.top),
			width:  width,
			height: height,
			r: Math.min(width, height) * arcsize
		});
	}
});
dojo.gfx.Rect.nodeType = "roundrect"; // use a roundrect so the stroke join type is respected

dojo.declare("dojo.gfx.Ellipse", dojo.gfx.Shape, {
	initializer: function(rawNode) {
		this.shape = dojo.lang.shallowCopy(dojo.gfx.defaultEllipse, true);
		this.attach(rawNode);
	},
	setShape: function(newShape){
		var ts = this.shape = dojo.gfx.makeParameters(this.shape, newShape);
		with(this.rawNode.style){
			left   = (ts.cx - ts.rx).toFixed();
			top    = (ts.cy - ts.ry).toFixed();
			width  = (ts.rx * 2).toFixed();
			height = (ts.ry * 2).toFixed();
		}
		return this.setTransform(this.matrix);
	},
	attachShape: function(rawNode){
		var rx = parseInt(rawNode.style.width ) / 2;
		var ry = parseInt(rawNode.style.height) / 2;
		return dojo.gfx.makeParameters(dojo.gfx.defaultEllipse, {
			cx: parseInt(rawNode.style.left) + rx,
			cy: parseInt(rawNode.style.top ) + ry,
			rx: rx,
			ry: ry
		});
	}
});
dojo.gfx.Ellipse.nodeType = "oval";

dojo.declare("dojo.gfx.Circle", dojo.gfx.Shape, {
	initializer: function(rawNode) {
		this.shape = dojo.lang.shallowCopy(dojo.gfx.defaultCircle, true);
		this.attach(rawNode);
	},
	setShape: function(newShape){
		this.shape = dojo.gfx.makeParameters(this.shape, newShape);
		this.rawNode.style.left   = (this.shape.cx - this.shape.r).toFixed();
		this.rawNode.style.top    = (this.shape.cy - this.shape.r).toFixed();
		this.rawNode.style.width  = (this.shape.r * 2).toFixed();
		this.rawNode.style.height = (this.shape.r * 2).toFixed();
		return this;
	},
	attachShape: function(rawNode){
		var r = parseInt(rawNode.style.width) / 2;
		return dojo.gfx.makeParameters(dojo.gfx.defaultCircle, {
			cx: parseInt(rawNode.style.left) + r,
			cy: parseInt(rawNode.style.top)  + r,
			r:  r
		});
	}
});
dojo.gfx.Circle.nodeType = "oval";

dojo.declare("dojo.gfx.Line", dojo.gfx.Shape, {
	initializer: function(rawNode) {
		this.shape = dojo.lang.shallowCopy(dojo.gfx.defaultLine, true);
		this.attach(rawNode);
	},
	setShape: function(newShape){
		this.shape = dojo.gfx.makeParameters(this.shape, newShape);
		this.rawNode.from = this.shape.x1.toFixed() + "," + this.shape.y1.toFixed();
		this.rawNode.to   = this.shape.x2.toFixed() + "," + this.shape.y2.toFixed();
		return this;
	},
	attachShape: function(rawNode){
		return dojo.gfx.makeParameters(dojo.gfx.defaultLine, {
			x1: this.rawNode.from.x,
			y1: this.rawNode.from.y,
			x2: this.rawNode.to.x,
			y2: this.rawNode.to.y
		});
	}
});
dojo.gfx.Line.nodeType = "line";

dojo.declare("dojo.gfx.Polyline", dojo.gfx.Shape, {
	initializer: function(rawNode) {
		this.shape = dojo.lang.shallowCopy(dojo.gfx.defaultPolyline, true);
		this.attach(rawNode);
	},
	setShape: function(points, closed){
		if(points && points instanceof Array){
			this.shape = dojo.gfx.makeParameters(this.shape, { points: points });
			if(closed && this.shape.points.length) this.shape.points.push(this.shape.points[0]);
		}else{
			this.shape = dojo.gfx.makeParameters(this.shape, points);
		}
		attr = "";
		for(var i = 0; i< this.shape.points.length; ++i){
			attr += this.shape.points[i].x.toFixed(8) + " " + this.shape.points[i].y.toFixed(8) + " ";
		}
		this.rawNode.points.value = attr;
		return this.setTransform(this.matrix);
	},
	attachShape: function(rawNode){
		var shape = dojo.lang.shallowCopy(dojo.gfx.defaultPolyline, true);
		var points = rawNode.points.value.match(/\d+/g);
		if(points){
			for(var i = 0; i < points.length; i += 2){
				shape.points.push({ x: parseFloat(points[i]), y: parseFloat(points[i + 1]) });
			}
		}
		return shape;
	}
});
dojo.gfx.Polyline.nodeType = "polyline";

dojo.gfx.Path._calcArc = function(alpha){
	var cosa  = Math.cos(alpha);
	var sina  = Math.sin(alpha);
	// return a start point, 1st and 2nd control points, and an end point
	var p2 = {x: cosa + (4 / 3) * (1 - cosa), y: sina - (4 / 3) * cosa * (1 - cosa) / sina};
	return {
		s:  {x: cosa, y: sina},
		c1: p2,
		c2: {x: p2.x, y: -p2.y},
		e:  {x: cosa, y: -sina}
	};
};

dojo.lang.extend(dojo.gfx.Path, {
	_pathVmlToSvgMap: { r: "l", l: "L", t: "m", m: "M", v: "c", c: "C", x: "z" },
	_pathSvgToVmlMap: { l: "r", L: "l", m: "t", M: "m", c: "v", C: "c", z: "x" },
	_extraInit: function(rawNode) {
		this.lastControl = {x: 0, y: 0};
		this.lastAction = "";
	},
	_drawTo: function(action, args){
		this.shape.path += this.shape.absolute ? action.toUpperCase() : action.toLowerCase();
		for(var i = 0; i < args.length; ++i){
			this.shape.path += args[i].toFixed() + " ";
		}
		this.lastAction = action; 
		this.setShape();
		return this;
	},
	_update: function(x, y, x2, y2){
		if(this.shape.absolute){
			this.lastPos = {x: x, y: y};
			if(typeof(y2) != "undefined"){
				this.lastControl.x = x2;
				this.lastControl.y = y2;
			} else {
				this.lastControl.x = this.lastPos.x;
				this.lastControl.y = this.lastPos.y;
			}
		} else {
			if(typeof(y2) != "undefined"){
				this.lastControl.x = this.lastPos.x + x2;
				this.lastControl.y = this.lastPos.y + y2;
			} else {
				this.lastControl.x = this.lastPos.x + x;
				this.lastControl.y = this.lastPos.y + y;
			}
			this.lastPos.x += x;
			this.lastPos.y += y;
		}
	},
	setShape: function(newShape){
		this.shape = dojo.gfx.makeParameters(this.shape, typeof(newShape) == "string" ? { path: newShape } : newShape);
		this.setAbsoluteMode(this.shape.absolute);
		// convert SVG path to VML path
		var path = this.shape.path;
		for(var i in this._pathSvgToVmlMap){
			path = path.replace( new RegExp(i, 'g'),  this._pathSvgToVmlMap[i] );
		}
		this.rawNode.path.v = path + " e";
		return this.setTransform(this.matrix);
	},
	_mirror: function(action){
		if(action != this.lastAction){
			return {x: this.lastPos.x, y: this.lastPos.y};
		}
		var x1 = 2 * this.lastPos.x - this.lastControl.x;
		var y1 = 2 * this.lastPos.y - this.lastControl.y;
		if(!this.shape.absolute){
			x1 -= this.lastPos.x;
			y1 -= this.lastPos.y;
		}
		return {x: x1, y: y1};
	},
	smoothCurveTo: function(x2, y2, x, y){
		var pos = this._mirror("c");
		var x1 = pos.x;
		var y1 = pos.y;
		return this.curveTo(x1, y1, x2, y2, x, y);
	},
	_PI4: Math.PI / 4,
	_curvePI4: dojo.gfx.Path._calcArc(Math.PI / 8),
	arcTo: function(endAngle, cx, cy, rx, ry, xRotate, isCCW){
		// start of our arc
		var startAngle = Math.atan2(cy - this.lastPos.y, this.lastPos.x - cx) - xRotate;
		// size of our arc in radians
		var theta = isCCW ? endAngle - startAngle : startAngle - endAngle;
		if(theta < 0){
			theta += this._2PI;
		}else if(theta > this._2PI){
			theta = this._2PI;
		}
		// calculate our elliptic transformation
		var elliptic_transform;
		with(dojo.gfx.matrix){
			elliptic_transform = normalize([
				translate(cx, cy),
				rotate(xRotate),
				scale(rx, ry)
			]);
		};
		// draw curve chunks
		var alpha = this._PI4 / 2;
		var curve = this._curvePI4;
		var step  = isCCW ? alpha : -alpha;
		for(var angle = theta; angle > 0; angle -= this._PI4){
			if(angle < this._PI4){
				alpha = angle / 2;
				curve = dojo.gfx.Path._calcArc(alpha);
				step  = isCCW ? alpha : -alpha;
			}
			var c1, c2, e;
			with(dojo.gfx.matrix){
				var m = normalize([elliptic_transform, rotate(startAngle + step)]);
				if(isCCW){
					c1 = multiplyPoint(m, curve.c1);
					c2 = multiplyPoint(m, curve.c2);
					e  = multiplyPoint(m, curve.e );
				}else{
					c1 = multiplyPoint(m, curve.c2);
					c2 = multiplyPoint(m, curve.c1);
					e  = multiplyPoint(m, curve.s );
				}
			};
			// draw the curve
			this.curveTo(c1.x, c1.y, c2.x, c2.y, e.x, e.y);
			startAngle += 2 * step;
		}
		return this;
	}
});
dojo.gfx.Path.nodeType = "shape";

dojo.declare("dojo.gfx.Image", dojo.gfx.Shape, {
	initializer: function(rawNode) {
		this.shape = dojo.lang.shallowCopy(dojo.gfx.defaultImage, true);
		this.attach(rawNode);
	},
	getEventSource: function() {
		return this.rawNode ? this.rawNode.firstChild : null;
	},
	setShape: function(newShape){
		var ts = this.shape = dojo.gfx.makeParameters(this.shape, newShape);
        this.rawNode.firstChild.src = ts.src;
        if(ts.width || ts.height){
			with(this.rawNode.firstChild.style){
				width  = ts.width;
				height = ts.height;
			}
        }
		return this.setTransform(this.matrix);
	},
	setStroke: function() { return this; },
	setFill:   function() { return this; },
	attachShape: function(rawNode){
		var shape = dojo.lang.shallowCopy(dojo.gfx.defaultImage, true);
		shape.src = rawNode.firstChild.src;
		return shape;
	},
	attachStroke: function(rawNode){ return null; },
	attachFill:   function(rawNode){ return null; },
	attachTransform: function(rawNode) {
		var matrix = {};
		if(rawNode){
			var m = rawNode.filters["DXImageTransform.Microsoft.Matrix"];
			matrix.xx = m.M11;
			matrix.xy = m.M12;
			matrix.yx = m.M21;
			matrix.yy = m.M22;
			matrix.dx = m.Dx;
			matrix.dy = m.Dy;
		}
		return dojo.gfx.matrix.normalize(matrix);
	},
	_applyTransform: function() {
		var matrix = this._getRealMatrix();
		if(!matrix) return this;
		with(this.rawNode.filters["DXImageTransform.Microsoft.Matrix"]){
			M11 = matrix.xx;
			M12 = matrix.xy;
			M21 = matrix.yx;
			M22 = matrix.yy;
			Dx  = matrix.dx;
			Dy  = matrix.dy;
		}
		return this;
	}
});
dojo.gfx.Image.nodeType = "image";

dojo.gfx._creators = {
	createRect: function(rect){
		return this.createObject(dojo.gfx.Rect, rect);
	},
	createEllipse: function(ellipse){
		return this.createObject(dojo.gfx.Ellipse, ellipse);
	},
	createCircle: function(circle){
		return this.createObject(dojo.gfx.Circle, circle);
	},
	createLine: function(line){
		return this.createObject(dojo.gfx.Line, line);
	},
	createPolyline: function(points){
		return this.createObject(dojo.gfx.Polyline, points);
	},
	createPath: function(path){
		return this.createObject(dojo.gfx.Path, path, true);
	},
	createGroup: function(path){
		return this.createObject(dojo.gfx.Group, null, true);
	},
	createImage: function(image){
		if(!this.rawNode) return null;
		var shape = new dojo.gfx.Image();
		var node = document.createElement('div');
		node.style.position = "absolute";
		node.style.width  = this.rawNode.style.width;
		node.style.height = this.rawNode.style.height;
		node.style.filter = "progid:DXImageTransform.Microsoft.Matrix(M11=1, M12=0, M21=0, M22=1, Dx=0, Dy=0)";
		var img  = document.createElement('img');
		node.appendChild(img);
		shape.setRawNode(node);
		this.rawNode.appendChild(node);
		shape.setShape(image);
		this.add(shape);
		return shape;
	},
	createObject: function(shapeType, rawShape, overrideSize) {
		if(!this.rawNode) return null;
		var shape = new shapeType();
		var node = document.createElement('v:' + shapeType.nodeType);
		shape.setRawNode(node);
		this.rawNode.appendChild(node);
		if(overrideSize) this._overrideSize(node);
		shape.setShape(rawShape);
		this.add(shape);
		return shape;
	},
	_overrideSize: function(node){
		node.style.width  = this.rawNode.style.width;
		node.style.height = this.rawNode.style.height;
		node.coordsize = parseFloat(node.style.width) + " " + parseFloat(node.style.height);
	}
};

dojo.lang.extend(dojo.gfx.Group, dojo.gfx._creators);
dojo.lang.extend(dojo.gfx.Surface, dojo.gfx._creators);

delete dojo.gfx._creators;

dojo.gfx.attachNode = function(node){
	if(!node) return null;
	var s = null;
	switch(node.tagName.toLowerCase()){
		case dojo.gfx.Rect.nodeType:
			s = new dojo.gfx.Rect();
			break;
		case dojo.gfx.Ellipse.nodeType:
			s = new dojo.gfx.Ellipse();
			break;
		case dojo.gfx.Polyline.nodeType:
			s = new dojo.gfx.Polyline();
			break;
		case dojo.gfx.Path.nodeType:
			s = new dojo.gfx.Path();
			break;
		case dojo.gfx.Circle.nodeType:
			s = new dojo.gfx.Circle();
			break;
		case dojo.gfx.Line.nodeType:
			s = new dojo.gfx.Line();
			break;
		case dojo.gfx.Image.nodeType:
			s = new dojo.gfx.Image();
			break;
		default:
			dojo.debug("FATAL ERROR! tagName = " + node.tagName);
	}
	s.attach(node);
	return s;
};

dojo.lang.extend(dojo.gfx.Surface, {
	setDimensions: function(width, height){
		if(!this.rawNode) return this;
		this.rawNode.style.width = width;
		this.rawNode.style.height = height;
		this.rawNode.coordsize = width + " " + height;
		return this;
	},
	getDimensions: function(){
		return this.rawNode ? { width: this.rawNode.style.width, height: this.rawNode.style.height } : null;
	},
	// group control
	add: function(shape){
		var oldParent = shape.getParent();
		if(this != oldParent){
			this.rawNode.appendChild(shape.rawNode);
			if(oldParent){
				oldParent.remove(shape, true);
			}
			shape._setParent(this, null);
		}
		return this;
	},
	remove: function(shape, silently){
		if(this == shape.getParent()){
			if(this.rawNode == shape.rawNode.parentNode){
				this.rawNode.removeChild(shape.rawNode);
			}
			shape._setParent(null, null);
		}
		return this;
	}
});

dojo.gfx.createSurface = function(parentNode, width, height){
   var s = new dojo.gfx.Surface();
   s.rawNode = document.createElement("v:group");
   s.rawNode.style.width  = (width) ? (width + "px") : "100%";
   s.rawNode.style.height = (height) ? (height + "px") : "100%";
   s.rawNode.coordsize = (width && height) ? (width + " " + height) : "100% 100%";
   s.rawNode.coordorigin = "0 0";
   dojo.byId(parentNode).appendChild(s.rawNode);
   return s;
};

dojo.gfx.attachSurface = function(node){
	var s = new dojo.gfx.Surface();
	s.rawNode = node;
	return s;
};
