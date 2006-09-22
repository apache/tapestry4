/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

﻿dojo.provide("dojo.gfx.common");

dojo.require("dojo.gfx.color");
dojo.require("dojo.lang.declare");
dojo.require("dojo.lang.extras");

//dojo.gfx.defaultRenderer.init();

dojo.lang.mixin(dojo.gfx, {
	defaultPath:     {type: "path",     path: "", absolute: true},
	defaultPolyline: {type: "polyline", points: []},
	defaultRect:     {type: "rect",     x: 0, y: 0, width: 100, height: 100, r: 0},
	defaultEllipse:  {type: "ellipse",  cx: 0, cy: 0, rx: 100, ry: 200},
	defaultCircle:   {type: "circle",   cx: 0, cy: 0, r: 100},
	defaultLine:     {type: "line",     x1: 0, y1: 0, x2: 100, y2: 100},
	defaultImage:    {type: "image",    width: 0, height: 0, src: ""},

	defaultStroke: {color: "black", width: 1, cap: "butt", join: 4},
	defaultLinearGradient: {type: "linear", x1: 0, y1: 0, x2: 100, y2: 100, 
		colors: [{offset: 0, color: "black"}, {offset: 1, color: "white"}]},
	defaultRadialGradient: {type: "radial", cx: 0, cy: 0, r: 100, 
		colors: [{offset: 0, color: "black"}, {offset: 1, color: "white"}]},
	defaultPattern: {type: "pattern", x: 0, y: 0, width: 0, height: 0, src: ""},

	normalizeColor: function(color){
		return (color instanceof dojo.gfx.color.Color) ? color : new dojo.gfx.color.Color(color);
	},
	normalizeParameters: function(existed, update){
		if(update){
			var empty = {};
			for(x in existed){
				if(x in update && !(x in empty)){
					existed[x] = update[x];
				}
			}
		}
		return existed;
	},
	makeParameters: function(defaults, update){
		if(!update) return dojo.lang.shallowCopy(defaults, true);
		var result = {};
		for(var i in defaults){
			if(!(i in result)){
				result[i] = dojo.lang.shallowCopy((i in update) ? update[i] : defaults[i], true);
			}
		}
		return result;
	},

	_GUID: 1,
	guid: function(){ return "dj_gfx_guid_" + dojo.gfx._GUID++; }
});

// this is a Shape object, which knows how to apply graphical attributes and a transformation
dojo.gfx.Shape = function(){
	// underlying node
	this.rawNode = null;
	// abstract shape object
	this.shape = null;
	// transformation matrix
	this.matrix  = null;
	// graphical attributes
	this.fillStyle   = null;
	this.strokeStyle = null;
	// virtual group structure
	this.parent = null;
	this.parentMatrix = null;
};

dojo.lang.extend(dojo.gfx.Shape, {
	// trivial getters
	getNode:      function(){ return this.rawNode; },
	getShape:     function(){ return this.shape; },
	getTransform: function(){ return this.matrix; },
	getFill:      function(){ return this.fillStyle; },
	getStroke:    function(){ return this.strokeStyle; },
	getParent:    function(){ return this.parent; },
	
	getEventSource: function(){ return this.rawNode; },
	
	// empty settings
	setShape:  function(shape) { return this; },	// ignore
	setStroke: function(stroke){ return this; },	// ignore
	setFill:   function(fill)  { return this; },	// ignore
	
	// z-index
	moveToFront: function(){ return this; },		// ignore
	moveToBack:  function(){ return this; },		// ignore

	// apply transformations
	setTransform: function(matrix){
		this.matrix = dojo.gfx.matrix.clone(matrix ? dojo.gfx.matrix.normalize(matrix) : dojo.gfx.identity, true);
		return this._applyTransform();
	},
	
	// apply left & right transformation
	applyRightTransform: function(matrix){
		return matrix ? this.setTransform([this.matrix, matrix]) : this;
	},
	applyLeftTransform: function(matrix){
		return matrix ? this.setTransform([matrix, this.matrix]) : this;
	},

	// a shortcut for apply-right
	applyTransform: function(matrix){
		return matrix ? this.setTransform([this.matrix, matrix]) : this;
	},
	
	// virtual group methods
	remove: function(silently){
		if(this.parent){
			this.parent.remove(this, silently);
		}
		return this;
	},
	_setParent: function(parent, matrix){
		this.parent = parent;
		return this._updateParentMatrix(matrix);
	},
	_updateParentMatrix: function(matrix){
		this.parentMatrix = matrix ? dojo.gfx.matrix.clone(matrix) : null;
		return this._applyTransform();
	},
	_getRealMatrix: function(){
		return this.parentMatrix ? new dojo.gfx.matrix.Matrix2D([this.parentMatrix, this.matrix]) : this.matrix;
	}
});

dojo.declare("dojo.gfx.VirtualGroup", dojo.gfx.Shape, {
	initializer: function() {
		this.children = [];
	},
	
	// group management
	add: function(shape){
		var oldParent = shape.getParent();
		if(oldParent){
			oldParent.remove(shape, true);
		}
		this.children.push(shape);
		return shape._setParent(this, this._getRealMatrix());
	},
	remove: function(shape, silently){
		var i = 0;
		for(; i < this.children.length; ++i){
			if(this.children[i] == shape){
				if(silently){
					// skip for now
				}else{
					shape._setParent(null, null);
				}
				this.children.splice(i, 1);
				break;
			}
		}
		return this;
	},
	
	// apply transformation
	_applyTransform: function(){
		var matrix = this._getRealMatrix();
		for(var i = 0; i < this.children.length; ++i){
			this.children[i]._updateParentMatrix(matrix);
		}
		return this;
	}
});

// this is a Surface object
dojo.gfx.Surface = function(){
	// underlying node
	this.rawNode = null;
};

dojo.lang.extend(dojo.gfx.Surface, {
	getEventSource: function(){ return this.rawNode; }
});

// this is a Path shape
dojo.declare("dojo.gfx.Path", dojo.gfx.Shape, {
	initializer: function(rawNode) {
		this.shape = dojo.lang.shallowCopy(dojo.gfx.defaultPath, true);
		this.lastPos = {x: 0, y: 0};
		this._extraInit(rawNode);
		this.attach(rawNode);
	},
	setAbsoluteMode: function(mode){
		this.shape.absolute = typeof(mode) == "string" ? (mode == "absolute") : mode;
		return this;
	},
	getAbsoluteMode: function(){
		return this.shape.absolute;
	},
	closePath: function(){
		return this._drawTo("z", []);
	},
	moveTo: function(x, y){
		this._update(x, y);
		return this._drawTo("m", [x, y]);
	},
	lineTo: function(x, y){
		this._update(x, y);
		return this._drawTo("l", [x, y]);
	},
	curveTo: function(x1, y1, x2, y2, x, y){
		this._update(x, y, x2, y2);
		return this._drawTo("c", [x1, y1, x2, y2, x, y]);
	},
	_2PI: Math.PI * 2,
	// these are meant to be overridden in derived classes
	_extraInit: function(rawNode) {},
	_drawTo: function(action) { return this; },
	_update: function(x, y, x2, y2) {}
});
