dojo.provide("dojo.graphics.color.hsl");
dojo.require("dojo.gfx.color.hsl");
dojo.deprecated("dojo.graphics.color.hsl has been replaced with dojo.gfx.color.hsl","0.5");
dojo.graphics.color.rgb2hsl=function(r,g,b){
dojo.deprecated("dojo.graphics.color.rgb2hsl has been replaced with dojo.gfx.color.rgb2hsl","0.5");
return dojo.gfx.color.rgb2hsl(r,g,b);
};
dojo.graphics.color.hsl2rgb=function(h,s,l){
dojo.deprecated("dojo.graphics.color.hsl2rgb has been replaced with dojo.gfx.color.hsl2rgb","0.5");
return dojo.gfx.color.hsl2rgb(h,s,l);
};
dojo.graphics.color.hsl2hex=function(h,s,l){
dojo.deprecated("dojo.graphics.color.hsl2hex has been replaced with dojo.gfx.color.hsl2hex","0.5");
return dojo.gfx.color.hsl2hex(h,s,l);
};
dojo.graphics.color.hex2hsl=function(_a){
dojo.deprecated("dojo.graphics.color.hex2hsl has been replaced with dojo.gfx.color.hex2hsl","0.5");
return dojo.gfx.color.hex2hsl(_a);
};
