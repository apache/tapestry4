
﻿dojo.provide("dojo.html.units");dojo.require("dojo.html.style");dojo.html.units.constants = {cm_in_pt: 2.54 / 72,mm_in_pt: 25.4 / 72
};dojo.html._getLength = function(len, passAuto){if(typeof len != "string"){ return len; }
if(len.length == 0){ return 0; }
if(len == "auto"){ return passAuto ? len : 0; }
var val  = parseFloat(len);var unit = len.match(/%|[a-zA-Z]{2}\s*$/);if(unit){switch(unit[0]){case "px": return val;case "pt": return val * this.px_in_pt;case "in": return val * 72 * this.px_in_pt;case "pc": return val * 12 * this.px_in_pt;case "mm": return val / dojo.html.units.constants.mm_in_pt * this.px_in_pt;case "cm": return val / dojo.html.units.constants.cm_in_pt * this.px_in_pt;case "em": return val * this["1em"];case "ex": return val * this["1ex"];case "%":  return val / 100 * this["100%"];}}
return val;};dojo.html._measureSettings = function(node){var s = node.style;s.position="absolute";s.left="-100px";s.top="0";s.width="30px";s.height="1000em";s.border="0";s.margin="0";s.padding="0";s.outline="0";s.lineHeight="1";s.overflow="hidden";};dojo.html._measure = function(parent, sibling, callback){var heights = {'1em':0, '1ex':0, '100%':0, '12pt':0, '16px':0, 'xx-small':0, 'x-small':0,'small':0, 'medium':0, 'large':0, 'x-large':0, 'xx-large':0
};if(dojo.render.html.ie){document.documentElement.style.fontSize="100%";}
var node = document.createElement("div");if(callback){ callback(node); }
dojo.html._measureSettings(node);if(sibling){parent.insertBefore(node, sibling);}else{parent.appendChild(node);}
var s = node.style;for(var p in heights){s.fontSize = p;heights[p] = Math.round(node.offsetHeight * 12/16) * 16/12 / 1000;}
parent.removeChild(node);node = null;heights.px_in_pt = heights["12pt"] / 12;heights.getLength = dojo.html._getLength;return heights;};dojo.html.measure = function(){var parent = null, base = 0, args = [];if(arguments.length > 0 && typeof arguments[0] != "string"){parent = arguments[0];base = 1;}
if(!parent){parent = dojo.body();}
for(var i = base; i < arguments.length; ++i){args.push(arguments[i]);}
if(args.length == 0){returnnull;}
var f = function(node){var s = node.style;s.fontFamily = args[0];if(args.length > 1){ s.fontWeight  = args[1]; }
if(args.length > 2){ s.fontStyle   = args[2]; }
if(args.length > 3){ s.fontVariant = args[3]; }};if(args.length == 2){switch(args[1]){case "class":
f = function(div){ div.className = args[0]; };break;case "style":
f = function(div){ div.style.cssText = args[0]; };break;}}
var node = document.createElement("div");f(node);dojo.html._measureSettings(node);parent.appendChild(node);var heights = dojo.html._measure(node);parent.removeChild(node);node = null;return heights;};dojo.html._measureNode = function(node){node = dojo.byId(node);return dojo.html._measure(node.parentNode, node, function(div){div.style.cssText = node.style.cssText;div.className     = node.className;});};dojo.html._defaultFontMeasurements = null;dojo.html.measureDefaults = function(recalc){if(recalc || !dojo.html._defaultFontMeasurements){dojo.html._defaultFontMeasurements = dojo.html._measure(dojo.body());}
return dojo.html._defaultFontMeasurements;};dojo.html._getDefaultLength = function(len, passAuto){if(typeof len != "string"){ return len; }
if(len.length == 0){ return 0; }
if(len == "auto"){ return passAuto ? len : 0; }
var val = parseFloat(len);if(len.length > 2){switch(len.slice(-2)){case "px": return val;case "pt": return val * this.px_in_pt;case "in": return val * 72 * this.px_in_pt;case "pc": return val * 12 * this.px_in_pt;case "mm": return val / dojo.html.units.constants.mm_in_pt * this.px_in_pt;case "cm": return val / dojo.html.units.constants.cm_in_pt * this.px_in_pt;case "em":
case "ex":
case "%":
var t = dojo.html._measureNode(this.node);var empty = {};for(var i in t){if(!(i in empty) && typeof t[i] == "number"){this[i] = t[i];}}
this.getLength = dojo.html._getLength;return this.getLength(len, passAuto);}}
return val;};dojo.html.measureNode = function(node){var t = dojo.html.measureDefaults();t.node = dojo.byId(node);t.getLength = dojo.html._getDefaultLength;return t;};dojo.html.getStyleValue = function(node, cssSelector){var f;node = dojo.byId(node);if(!node || !node.style){return;}
if (document.defaultView && dojo.html.isDescendantOf(node, node.ownerDocument)){if(node.style.getPropertyValue){f = dojo.html._getStyleValueW3;}else{f = dojo.html._getStyleValueGeckoKhtml;}} else if(node.currentStyle){f = dojo.html._getStyleValueIe;}
dojo.html.getStyleValue = function(node, cssSelector){return dojo.html._getStyleValueWrapper(node, cssSelector, f);}
return dojo.html.getStyleValue(node, cssSelector);};dojo.html._getStyleValueWrapper = function(node, cssSelector, f){node = dojo.byId(node);if(!node || !node.style){return;}
var cssSelector = dojo.html.toSelectorCase(cssSelector);return f(node, cssSelector);};dojo.html._getStyleValueW3 = function(node, cssSelector){try{var cs = document.defaultView.getComputedStyle(node, "");if(cs){return cs.getPropertyValue(cssSelector);}}catch(e){}
return node.style.getPropertyValue(cssSelector);};dojo.html._getStyleValueGeckoKhtml = function(node, cssSelector){try{var cs = document.defaultView.getComputedStyle(node, "");if(cs){return cs.getPropertyValue(cssSelector);}}catch(e){}
return;};dojo.html._getStyleValueIe = function(node, cssSelector){var property = dojo.html.toCamelCase(cssSelector);return node.currentStyle[property];};