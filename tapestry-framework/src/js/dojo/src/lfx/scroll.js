
dojo.provide("dojo.lfx.scroll");dojo.require("dojo.html.util");dojo.require("dojo.html.iframe");dojo.require("dojo.lfx.Animation");dojo.lfx.smoothScroll = function( node,win,offset,duration,easing,handlers){var targs = {"window": win,"offset": offset || {x:0,y:0},"target": dojo.html.getAbsolutePositionExt(node,true,dojo.html.boxSizing.BORDER_BOX, win),"duration": duration,"easing": easing||dojo.lfx.easeOut
};var anim = new dojo.lfx.Animation({beforeBegin: function(){var current = dojo.withGlobal(targs.window,dojo.html.getScroll).offset;delete this.curve;anim.curve = new dojo.lfx.Line([current.x,current.y],[targs.target.x+targs.offset.x,targs.target.y+targs.offset.y]);},onAnimate: function(value){targs.window.scrollTo(value[0],value[1]);}},duration,null,easing||dojo.lfx.easeOut
);if(handlers){for(var x in handlers){if(dojo.lang.isFunction(handlers[x])){anim.connect(x, anim, handlers[x]);}}
}
return anim;}