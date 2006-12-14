
dojo.provide("dojo.lfx.toggle");dojo.require("dojo.lfx.*");dojo.lfx.toggle.plain = {show: function(node, duration, easing, callback){dojo.html.show(node);if(dojo.lang.isFunction(callback)){ callback(); }},hide: function(node, duration, easing, callback){dojo.html.hide(node);if(dojo.lang.isFunction(callback)){ callback(); }}
}
dojo.lfx.toggle.fade = {anim: null,show: function(node, duration, easing, callback){if(this.anim && this.anim.status() != "stopped"){this.anim.stop();}
this.anim = dojo.lfx.fadeShow(node, duration, easing, callback).play();},hide: function(node, duration, easing, callback){if(this.anim && this.anim.status() != "stopped"){this.anim.stop();}
this.anim = dojo.lfx.fadeHide(node, duration, easing, callback).play();}}
dojo.lfx.toggle.wipe = {anim: null,show: function(node, duration, easing, callback){if(this.anim && this.anim.status() != "stopped"){this.anim.stop();}
this.anim = dojo.lfx.wipeIn(node, duration, easing, callback).play();},hide: function(node, duration, easing, callback){if(this.anim && this.anim.status() != "stopped"){this.anim.stop();}
this.anim = dojo.lfx.wipeOut(node, duration, easing, callback).play();}}
dojo.lfx.toggle.explode = {anim: null,show: function(node, duration, easing, callback, explodeSrc){if(this.anim && this.anim.status() != "stopped"){this.anim.stop();}
this.anim = dojo.lfx.explode(explodeSrc||{x:0,y:0,width:0,height:0}, node, duration, easing, callback).play();},hide: function(node, duration, easing, callback, explodeSrc){if(this.anim && this.anim.status() != "stopped"){this.anim.stop();}
this.anim = dojo.lfx.implode(node, explodeSrc||{x:0,y:0,width:0,height:0}, duration, easing, callback).play();}}
