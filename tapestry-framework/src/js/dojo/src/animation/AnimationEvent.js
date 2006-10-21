

dojo.provide("dojo.animation.AnimationEvent");
dojo.require("dojo.lang.common");

dojo.deprecated("dojo.animation.AnimationEvent is slated for removal in 0.5; use dojo.lfx.* instead.", "0.5");

dojo.animation.AnimationEvent = function(
animation,
type,
coords,
startTime,
currentTime,
endTime,
duration,
percent,
fps) {













this.type = type; // "animate", "begin", "end", "play", "pause", "stop"
this.animation = animation;

this.coords = coords;
this.x = coords[0];
this.y = coords[1];
this.z = coords[2];

this.startTime = startTime;
this.currentTime = currentTime;
this.endTime = endTime;

this.duration = duration;
this.percent = percent;
this.fps = fps;
};
dojo.extend(dojo.animation.AnimationEvent, {
coordsAsInts: function() {
// summary: Coerce the coordinates into integers.
var cints = new Array(this.coords.length);
for(var i = 0; i < this.coords.length; i++) {
cints[i] = Math.round(this.coords[i]);
}
return cints;
}
});
