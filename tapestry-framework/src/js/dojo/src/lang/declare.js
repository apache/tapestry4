
dojo.provide("dojo.lang.declare");dojo.require("dojo.lang.common");dojo.require("dojo.lang.extras");dojo.lang.declare = function( className,superclass,init,props){if((dojo.lang.isFunction(props))||((!props)&&(!dojo.lang.isFunction(init)))){if(dojo.lang.isFunction(props)){dojo.deprecated("dojo.lang.declare("+className+"...):", "use class, superclass, initializer, properties argument order", "0.6");}
var temp = props;props = init;init = temp;}
if(props && props.initializer){dojo.deprecated("dojo.lang.declare("+className+"...):", "specify initializer as third argument, not as an element in properties", "0.6");}
var mixins = [ ];if(dojo.lang.isArray(superclass)){mixins = superclass;superclass = mixins.shift();}
if(!init){init = dojo.evalObjPath(className, false);if((init)&&(!dojo.lang.isFunction(init))){ init = null };}
var ctor = dojo.lang.declare._makeConstructor();var scp = (superclass ? superclass.prototype : null);if(scp){scp.prototyping = true;ctor.prototype = new superclass();scp.prototyping = false;}
ctor.superclass = scp;ctor.mixins = mixins;for(var i=0,l=mixins.length; i<l; i++){dojo.lang.extend(ctor, mixins[i].prototype);}
ctor.prototype.initializer = null;ctor.prototype.declaredClass = className;if(dojo.lang.isArray(props)){dojo.lang.extend.apply(dojo.lang, [ctor].concat(props));}else{dojo.lang.extend(ctor, (props)||{});}
dojo.lang.extend(ctor, dojo.lang.declare._common);ctor.prototype.constructor = ctor;ctor.prototype.initializer = (ctor.prototype.initializer)||(init)||(function(){});var created = dojo.parseObjPath(className, null, true);created.obj[created.prop] = ctor;return ctor;}
dojo.lang.declare._makeConstructor = function(){return function(){var self = this._getPropContext();var s = self.constructor.superclass;if((s)&&(s.constructor)){if(s.constructor==arguments.callee){this._inherited("constructor", arguments);}else{this._contextMethod(s, "constructor", arguments);}}
var ms = (self.constructor.mixins)||([]);for(var i=0, m; (m=ms[i]); i++) {(((m.prototype)&&(m.prototype.initializer))||(m)).apply(this, arguments);}
if((!this.prototyping)&&(self.initializer)){self.initializer.apply(this, arguments);}}
}
dojo.lang.declare._common = {_getPropContext: function(){ return (this.___proto||this); },_contextMethod: function(ptype, method, args){var result, stack = this.___proto;this.___proto = ptype;try { result = ptype[method].apply(this,(args||[])); }
catch(e) { throw e; }
finally { this.___proto = stack; }
return result;},_inherited: function(prop, args){var p = this._getPropContext();do{if((!p.constructor)||(!p.constructor.superclass)){ return; }
p = p.constructor.superclass;}while(!(prop in p));return (dojo.lang.isFunction(p[prop]) ? this._contextMethod(p, prop, args) : p[prop]);}}
dojo.declare = dojo.lang.declare;