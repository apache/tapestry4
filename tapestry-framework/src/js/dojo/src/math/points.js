

dojo.provide("dojo.math.points");
dojo.require("dojo.math");

dojo.math.points = {
translate: function(a, b) {
//	summary
//	translate a by b, and return the result.
if( a.length != b.length ) {
dojo.raise("dojo.math.translate: points not same size (a:[" + a + "], b:[" + b + "])");
}
var c = new Array(a.length);
for(var i = 0; i < a.length; i++) {
c[i] = a[i] + b[i];
}
return c;	//	array
},

midpoint: function(a, b) {
//	summary
//	Find the point midway between a and b
if( a.length != b.length ) {
dojo.raise("dojo.math.midpoint: points not same size (a:[" + a + "], b:[" + b + "])");
}
var c = new Array(a.length);
for(var i = 0; i < a.length; i++) {
c[i] = (a[i] + b[i]) / 2;
}
return c;	//	array
},

invert: function(a) {
//	summary
//	invert the values in a and return it.
var b = new Array(a.length);
for(var i = 0; i < a.length; i++) { b[i] = -a[i]; }
return b;	//	array
},

distance: function(a, b) {
//	summary
//	Calculate the distance between point a and point b
return Math.sqrt(Math.pow(b[0]-a[0], 2) + Math.pow(b[1]-a[1], 2));	// 	float
}
};
