
dojo.provide("dojo.behavior.common");dojo.require("dojo.event.*");dojo.require("dojo.query");dojo.require("dojo.experimental");dojo.experimental("dojo.behavior");dojo.behavior = new function(){function arrIn(obj, name){if(!obj[name]){ obj[name] = []; }
return obj[name];}
var _inc = 0;function forIn(obj, scope, func){var tmpObj = {};for(var x in obj){if(typeof tmpObj[x] == "undefined"){if(!func){scope(obj[x], x);}else{func.call(scope, obj[x], x);}}
}}
this.behaviors = {};this.add = function(behaviorObj){var tmpObj = {};forIn(behaviorObj, this, function(behavior, name){var tBehavior = arrIn(this.behaviors, name);if(typeof tBehavior["id"] != "number"){tBehavior.id = _inc++;}
var cversion = [];tBehavior.push(cversion);if((dojo.lang.isString(behavior))||(dojo.lang.isFunction(behavior))){behavior = { found: behavior };}
forIn(behavior, function(rule, ruleName){arrIn(cversion, ruleName).push(rule);});});}
this.apply = function(){dojo.profile.start("dojo.behavior.apply");forIn(this.behaviors, function(tBehavior, id){var elems = dojo.query(id);dojo.lang.forEach(elems,function(elem){var runFrom = 0;var bid = "_dj_behavior_"+tBehavior.id;if(typeof elem[bid] == "number"){runFrom = elem[bid];if(runFrom == (tBehavior.length-1)){return;}}
for(var x=runFrom, tver; tver = tBehavior[x]; x++){forIn(tver, function(ruleSet, ruleSetName){if(dojo.lang.isArray(ruleSet)){dojo.lang.forEach(ruleSet, function(action){dojo.behavior.applyToNode(elem, action, ruleSetName);});}});}
elem[bid] = tBehavior.length-1;}
);});dojo.profile.end("dojo.behavior.apply");}
this.applyToNode = function(node, action, ruleSetName){if(typeof action == "string"){dojo.event.topic.registerPublisher(action, node, ruleSetName);}else if(typeof action == "function"){if(ruleSetName == "found"){action(node);}else{dojo.event.connect(node, ruleSetName, action);}}else{action.srcObj = node;action.srcFunc = ruleSetName;dojo.event.kwConnect(action);}}
}
dojo.addOnLoad(dojo.behavior, "apply");