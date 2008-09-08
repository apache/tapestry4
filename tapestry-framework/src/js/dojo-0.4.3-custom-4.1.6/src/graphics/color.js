dojo.provide("dojo.graphics.color");
dojo.require("dojo.gfx.color");
dojo.deprecated("dojo.graphics.color.Color is now dojo.gfx.color.Color.","0.5");
dojo.graphics.color.Color=dojo.gfx.color.Color;
dojo.graphics.color.named=dojo.gfx.color.named;
dojo.graphics.color.blend=function(a,b,_3){
dojo.deprecated("dojo.graphics.color.blend is now dojo.gfx.color.blend","0.5");
return dojo.gfx.color.blend(a,b,_3);
};
dojo.graphics.color.blendHex=function(a,b,_6){
dojo.deprecated("dojo.graphics.color.blendHex is now dojo.gfx.color.blendHex","0.5");
return dojo.gfx.color.rgb2hex(dojo.gfx.color.blend(dojo.gfx.color.hex2rgb(a),dojo.gfx.color.hex2rgb(b),_6));
};
dojo.graphics.color.extractRGB=function(_7){
dojo.deprecated("dojo.graphics.color.extractRGB is now dojo.gfx.color.extractRGB","0.5");
return dojo.gfx.color.extractRGB(_7);
};
dojo.graphics.color.hex2rgb=function(_8){
dojo.deprecated("dojo.graphics.color.hex2rgb is now dojo.gfx.color.hex2rgb","0.5");
return dojo.gfx.color.hex2rgb(_8);
};
dojo.graphics.color.rgb2hex=function(r,g,b){
dojo.deprecated("dojo.graphics.color.rgb2hex is now dojo.gfx.color.rgb2hex","0.5");
return dojo.gfx.color.rgb2hex;
};
