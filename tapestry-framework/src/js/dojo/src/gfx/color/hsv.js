/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.gfx.color.hsv");
dojo.require("dojo.lang.array");
dojo.require("dojo.math");

dojo.lang.extend(dojo.gfx.color.Color, {
	toHsv: function() {
		return dojo.gfx.color.rgb2hsv(this.toRgb());
	}

});

dojo.gfx.color.rgb2hsv = function(r, g, b){
	if (dojo.lang.isArray(r)) {
		b = r[2] || 0;
		g = r[1] || 0;
		r = r[0] || 0;
	}

	// r,g,b, each 0 to 255, to HSV.
	// h = 0.0 to 360.0 (corresponding to 0..360.0 degrees around hexcone)
	// s = 0.0 (shade of gray) to 1.0 (pure color)
	// v = 0.0 (black) to 1.0 {white)
	//
	// Based on C Code in "Computer Graphics -- Principles and Practice,"
	// Foley et al, 1996, p. 592. 
	//
	// our calculatuions are based on 'regular' values (0-360, 0-1, 0-1) 
	// but we return bytes values (0-255, 0-255, 0-255)

	var h = null;
	var s = null;
	var v = null;

	var min = Math.min(r, g, b);
	v = Math.max(r, g, b);

	var delta = v - min;

	// calculate saturation (0 if r, g and b are all 0)

	s = (v == 0) ? 0 : delta/v;
	if (s == 0){
		// achromatic: when saturation is, hue is undefined
		h = 0;
	}else{
		// chromatic
		if (r == v){
			// between yellow and magenta
			h = 60 * (g - b) / delta;
		}else{
			if (g == v){
				// between cyan and yellow
				h = 120 + 60 * (b - r) / delta;
			}else{
				if (b == v){
					// between magenta and cyan
					h = 240 + 60 * (r - g) / delta;
				}
			}
		}
		if (h < 0){
			h += 360;
		}
	}

	h = (h == 0) ? 360 : Math.ceil((h / 360) * 255);
	s = Math.ceil(s * 255);
	return [h, s, v];
}

// Based on C Code in "Computer Graphics -- Principles and Practice,"
// Foley et al, 1996, p. 593.
//
// H = 0.0 to 360.0 (corresponding to 0..360 degrees around hexcone) 0 for S = 0
// S = 0.0 (shade of gray) to 1.0 (pure color)
// V = 0.0 (black) to 1.0 (white)
dojo.gfx.color.hsv2rgb = function(/* int || Array */h, /* int */s, /* int */v, /* Object? */options){
	//	summary
	//	converts an HSV value set to RGB, ranges depending on optional options object.
	//	patch for options by Matthew Eernisse 	
	if (dojo.lang.isArray(h)) {
		if(s){
			options = s;
		}
		v = h[2] || 0;
		s = h[1] || 0;
		h = h[0] || 0;
	}

	var opt = {
		inputRange:  (options && options.inputRange)  ? options.inputRange : [255, 255, 255],
		outputRange: (options && options.outputRange) ? options.outputRange : 255
	};

    switch(opt.inputRange[0]) { 
		// 0.0-1.0 
		case 1: h = h * 360; break; 
		// 0-100 
		case 100: h = (h / 100) * 360; break; 
		// 0-360 
		case 360: h = h; break; 
		// 0-255 
		default: h = (h / 255) * 360; 
	} 
	if (h == 360){ h = 0;}

	//	no need to alter if inputRange[1] = 1
	switch(opt.inputRange[1]){
		case 100: s /= 100; break;
		case 255: s /= 255;
	}

	//	no need to alter if inputRange[1] = 1
	switch(opt.inputRange[2]){
		case 100: v /= 100; break;
		case 255: v /= 255;
	}

	var r = null;
	var g = null;
	var b = null;

	if (s == 0){
		// color is on black-and-white center line
		// achromatic: shades of gray
		r = v;
		g = v;
		b = v;
	}else{
		// chromatic color
		var hTemp = h / 60;		// h is now IN [0,6]
		var i = Math.floor(hTemp);	// largest integer <= h
		var f = hTemp - i;		// fractional part of h

		var p = v * (1 - s);
		var q = v * (1 - (s * f));
		var t = v * (1 - (s * (1 - f)));

		switch(i){
			case 0: r = v; g = t; b = p; break;
			case 1: r = q; g = v; b = p; break;
			case 2: r = p; g = v; b = t; break;
			case 3: r = p; g = q; b = v; break;
			case 4: r = t; g = p; b = v; break;
			case 5: r = v; g = p; b = q; break;
		}
	}

	switch(opt.outputRange){
		case 1:
			r = dojo.math.round(r, 2);
			g = dojo.math.round(g, 2);
			b = dojo.math.round(b, 2);
			break;
		case 100:
			r = Math.ceil(r * 100);
			g = Math.ceil(g * 100);
			b = Math.ceil(b * 100);
			break;
		default:
			r = Math.ceil(r * 255);
			g = Math.ceil(g * 255);
			b = Math.ceil(b * 255);
	}

	return [r, g, b];
}
