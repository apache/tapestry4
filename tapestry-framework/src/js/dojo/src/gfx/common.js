

dojo.provide("dojo.gfx.common");

dojo.require("dojo.gfx.color");
dojo.require("dojo.lang.declare");
dojo.require("dojo.lang.extras");
dojo.require("dojo.dom");

dojo.lang.mixin(dojo.gfx, {



defaultPath:     {type: "path",     path: ""},
defaultPolyline: {type: "polyline", points: []},
defaultRect:     {type: "rect",     x: 0, y: 0, width: 100, height: 100, r: 0},
defaultEllipse:  {type: "ellipse",  cx: 0, cy: 0, rx: 200, ry: 100},
defaultCircle:   {type: "circle",   cx: 0, cy: 0, r: 100},
defaultLine:     {type: "line",     x1: 0, y1: 0, x2: 100, y2: 100},
defaultImage:    {type: "image",    width: 0, height: 0, src: ""},


defaultStroke: {color: "black", width: 1, cap: "butt", join: 4},
defaultLinearGradient: {type: "linear", x1: 0, y1: 0, x2: 100, y2: 100,
colors: [{offset: 0, color: "black"}, {offset: 1, color: "white"}]},
defaultRadialGradient: {type: "radial", cx: 0, cy: 0, r: 100,
colors: [{offset: 0, color: "black"}, {offset: 1, color: "white"}]},
defaultPattern: {type: "pattern", x: 0, y: 0, width: 0, height: 0, src: ""},

normalizeColor: function( color){
// summary: converts any legal color representation to normalized Color object
return (color instanceof dojo.gfx.color.Color) ? color : new dojo.gfx.color.Color(color); // dojo.gfx.color.Color
},
normalizeParameters: function( existed,  update){
// summary: updates an existing object with properties from the "update" object
// existed: the "target" object to be updated
// update: the "update" object, whose properties will be used to update the existed object
if(update){
var empty = {};
for(var x in existed){
if(x in update && !(x in empty)){
existed[x] = update[x];
}
}
}
return existed;	// Object
},
makeParameters: function( defaults,  update){
// summary: copies the original object, and all copied properties from the "update" object
// defaults: the object to be cloned before updating
// update: the object, which properties are to be cloned during updating
if(!update) return dojo.lang.shallowCopy(defaults, true);
var result = {};
for(var i in defaults){
if(!(i in result)){
result[i] = dojo.lang.shallowCopy((i in update) ? update[i] : defaults[i], true);
}
}
return result; // Object
},
formatNumber: function( x,  addSpace){
// summary: converts a number to a string using a fixed notation
// x: number to be converted
// addSpace: add a space before a positive number
var val = x.toString();
if(val.indexOf("e") >= 0){
val = x.toFixed(4);
}else{
var point = val.indexOf(".");
if(point >= 0 && val.length - point > 5){
val = x.toFixed(4);
}
}
if(x < 0){
return val; // String
}
return addSpace ? " " + val : val; // String
},

pathRegExp: /([A-Za-z]+)|(\d+(\.\d+)?)|(\.\d+)|(-\d+(\.\d+)?)|(-\.\d+)/g
});

dojo.declare("dojo.gfx.Surface", null, {

initializer: function(){
// summary: a constructor

// underlying node
this.rawNode = null;
},
getEventSource: function(){
// summary: returns a node, which can be used to attach event listeners
return this.rawNode; // Node
}
});
