
dojo.provide("dojo.query");dojo.require("dojo.experimental");dojo.experimental("dojo.query");(function(){var h = dojo.render.html;var d = dojo;var _getIndexes = function(q){return [ q.indexOf("#"), q.indexOf("."), q.indexOf("["), q.indexOf(":") ];}
var getId = function(query){var i = _getIndexes(query);if(i[0] != -1){return query.substring(i[0]+1, getIdEnd(query));}else{return "";}}
var getIdEnd = function(query){return _lowestFromIndex(query, 1);}
var buildPath = function(query){var xpath = "";var qparts = query.split(" ");while(qparts.length){var tqp = qparts.shift();var prefix;if(tqp == ">"){prefix = "/";tqp = qparts.shift();}else{prefix = "//";}
var tagName = getTagName(tqp);xpath += prefix + tagName;var id = getId(tqp);if(id.length){xpath += "[@id='"+id+"']";}
var cn = getClassName(tqp);if(cn.length){var padding = " ";if(cn.charAt(cn.length-1) == "*"){padding = ""; cn = cn.substr(0, cn.length-1);}
xpath +=
"[contains(concat(' ',@class,' '), ' "+
cn + padding + "')]";}};return xpath;};var _xpathFuncCache = {};var getXPathFunc = function(path){if(_xpathFuncCache[path]){return _xpathFuncCache[path];}
var doc = dojo.doc();var parent = dojo.body();var xpath = buildPath(path);var tf = function(){var ret = [];var xpathResult = doc.evaluate(xpath, parent, null,XPathResult.ANY_TYPE, null);var result = xpathResult.iterateNext();while(result){ret.push(result);result = xpathResult.iterateNext();}
return ret;}
return _xpathFuncCache[path] = tf;};var _filtersCache = {};var _simpleFiltersCache = {};var agree = function(first, second){if(first && second){return function(){return first.apply(window, arguments) && second.apply(window, arguments);}}else if(first){return first;}else{return second;}}
var _filterDown = function(element, queryParts, matchArr, idx){var nidx = idx+1;var isFinal = (queryParts.length == nidx);var tqp = queryParts[idx];if(tqp == ">"){var ecn = element.childNodes;if(!ecn.length){return;}
nidx++;var isFinal = (queryParts.length == nidx);var tf = getFilterFunc(queryParts[idx+1]);for(var x=ecn.length-1, te; x>=0, te=ecn[x]; x--){if(tf(te)){if(isFinal){matchArr.push(te);}else{_filterDown(te, queryParts, matchArr, nidx);}}
if(x==0){break;}}
}
var candidates = getElements(tqp, element);if(isFinal){while(candidates.length){matchArr.push(candidates.shift());}}else{while(candidates.length){_filterDown(candidates.shift(), queryParts, matchArr, nidx);}}
}
var filterDown = function(elements, queryParts, matchArr){var ret = matchArr||[];for(var x=elements.length-1, te; x>=0, te=elements[x]; x--){_filterDown(te, queryParts, ret, 0);}
return ret;}
var _lowestFromIndex = function(query, index){var ql = query.length;var i = _getIndexes(query);var end = ql;for(var x=index; x<i.length; x++){if(i[x] >= 0){if(i[x] < end){end = i[x];}}
}
return (end < 0) ? ql : end;}
var getTagNameEnd = function(query){var i = _getIndexes(query);if((i[0] == 0)||(i[1] == 0)){return 0;}else{return _lowestFromIndex(query, 0);}}
var getTagName = function(query){var tagNameEnd = getTagNameEnd(query);return ((tagNameEnd > 0) ? query.substr(0, tagNameEnd).toLowerCase() : "*");}
var getFilterFunc = function(query){if(_filtersCache[query]){return _filtersCache[query];}
var ff = null;var tagName = getTagName(query);if(tagName != "*"){ff = agree(ff,function(elem){var isTn = (
(elem.nodeType == 1) &&
(tagName == elem.tagName.toLowerCase())
);return isTn;}
);}
var idComponent = getId(query);if(idComponent.length){ff = agree(ff,function(elem){return (
(elem.nodeType == 1) &&
(elem.id == idComponent)
);}
);}
if(Math.max.apply(this, _getIndexes(query).slice(1)) >= 0){ff = agree(ff, getSimpleFilterFunc(query));}
return _filtersCache[query] = ff;}
var smallest = function(arr){var ret = -1;for(var x=0; x<arr.length; x++){var ta = arr[x];if(ta >= 0){if((ta > ret)||(ret == -1)){ret = ta;}}
}
return ret;}
var getClassName = function(query){var i = _getIndexes(query);if(-1 == i[1]){ return ""; }
var di = i[1]+1;var othersStart = smallest(i.slice(2));if(di < othersStart){return query.substring(di, othersStart);}else if(-1 == othersStart){return query.substr(di);}else{return "";}}
var getNodeIndex = function(node){var pn = node.parentNode;var pnc = pn.childNodes;var nidx = -1;var child = pn.firstChild;if(!child){return nidx;}
var ci = node["__cachedIndex"];var cl = pn["__cachedLength"];if(((typeof cl == "number")&&(cl != pnc.length))||(typeof ci != "number")){pn["__cachedLength"] = pnc.length;var idx = 1;do{if(child === node){nidx = idx;}
if(child.nodeType == 1){child["__cachedIndex"] = idx;idx++;}
child = child.nextSibling;}while(child);}else{nidx = ci;}
return nidx;}
var firedCount = 0;var _getAttr = function(elem, attr){var blank = "";if(attr == "class"){return elem.className || blank;}
if(attr == "for"){return elem.htmlFor || blank;}
return elem.getAttribute(attr) || blank;}
var attrs = [
{key: "|=",getMatcher: function(attr, value){var valueDash = value+"-";return function(elem){var ea = elem.getAttribute(attr) || "";return (
(ea == value) ||
(ea.indexOf(valueDash)==0)
);}}
},{key: "^=",getMatcher: function(attr, value){return function(elem){return (_getAttr(elem, attr).indexOf(value)==0);}}
},{key: "*=",getMatcher: function(attr, value){return function(elem){return (_getAttr(elem, attr).indexOf(value)>=0);}}
},{key: "$=",getMatcher: function(attr, value){return function(elem){var ea = _getAttr(elem, attr);return (ea.lastIndexOf(value)==(ea.length-value.length));}}
},{key: "!=",getMatcher: function(attr, value){return function(elem){return (_getAttr(elem, attr) != value);}}
},{key: "=",getMatcher: function(attr, value){return function(elem){return (_getAttr(elem, attr) == value);}}
}
];var getSimpleFilterFunc = function(query){var fcHit = (_simpleFiltersCache[query]||_filtersCache[query]);if(fcHit){ return fcHit; }
var ff = null;var i = _getIndexes(query);if(i[0] >= 0){var tn = getTagName(query);if(tn != "*"){ff = agree(ff, function(elem){return (elem.tagName.toLowerCase() == tn);});}}
var className = getClassName(query);if(className.length){var isWildcard = className.charAt(className.length-1) == "*";if(isWildcard){className = className.substr(0, className.length-1);}
var cnl = className.length;var spc = " ";var re = new RegExp("(?:^|\\s)" + className + (isWildcard ? ".*" : "") + "(?:\\s|$)");ff = agree(ff, function(elem){return re.test(elem.className);});}
if(i[2] >= 0){var lBktIdx = query.lastIndexOf("]");var condition = query.substring(i[2]+1, lBktIdx);if(condition.charAt(0) == "@"){condition = condition.slice(1);}
var matcher = null;for(var x=0; x<attrs.length; x++){var ta = attrs[x];var tci = condition.indexOf(ta.key);if(tci >= 0){var attr = condition.substring(0, tci);var value = condition.substring(tci+ta.key.length);if((value.charAt(0) == "\"")||
(value.charAt(0) == "\'")){value = value.substring(1, value.length-1);}
matcher = ta.getMatcher(attr, value);break;}}
if((!matcher)&&(condition.length)){if(dojo.render.html.ie){matcher = function(elem){return elem[condition];}}else{matcher = function(elem){return elem.hasAttribute(condition);}}
}
if(matcher){ff = agree(ff, matcher);}}
if(i[3]>= 0){var pseudoName = query.substr(i[3]+1);var condition = "";var obi = pseudoName.indexOf("(");var cbi = pseudoName.lastIndexOf(")");if((0 <= obi)&&
(0 <= cbi)&&
(cbi > obi)){condition = pseudoName.substring(obi+1, cbi);pseudoName = pseudoName.substr(0, obi);}
if(pseudoName == "first-child"){ff = agree(ff,function(elem){if(elem.nodeType != 1){ return false; }
var fc = elem.previousSibling;while(fc && (fc.nodeType != 1)){fc = fc.previousSibling;}
return (!fc);}
);}else if(pseudoName == "last-child"){ff = agree(ff,function(elem){if(elem.nodeType != 1){ return false; }
var nc = elem.nextSibling;while(nc && (nc.nodeType != 1)){nc = nc.nextSibling;}
return (!nc);}
);}else if(pseudoName == "empty"){ff = agree(ff,function(elem){var cn = elem.childNodes;var cnl = elem.childNodes.length;for(var x=cnl-1; x >= 0; x--){var nt = cn[x].nodeType;if((nt == 1)||(nt == 3)){ return false; }}
return true;}
);}else if(pseudoName == "contains"){ff = agree(ff,function(elem){return (elem.innerHTML.indexOf(condition) >= 0);}
);}else if(pseudoName == "not"){var ntf = getFilterFunc(condition);ff = agree(ff,function(elem){return (!ntf(elem));}
);}else if(pseudoName == "nth-child"){if(condition == "odd"){ff = agree(ff,function(elem){return (
((getNodeIndex(elem)) % 2) == 1
);}
);}else if((condition == "2n")||
(condition == "even")){ff = agree(ff,function(elem){return ((getNodeIndex(elem) % 2) == 0);}
);}else if(condition.indexOf("0n+") == 0){var ncount = parseInt(condition.substr(3));ff = agree(ff,function(elem){return (elem.parentNode.childNodes[ncount-1] === elem);}
);}else if((condition.indexOf("n+") > 0) &&
(condition.length > 3) ){var tparts = condition.split("n+", 2);var pred = parseInt(tparts[0]);var idx = parseInt(tparts[1]);ff = agree(ff,function(elem){return ((getNodeIndex(elem) % pred) == idx);}
);}else if(condition.indexOf("n") == -1){var ncount = parseInt(condition);ff = agree(ff,function(elem){return (elem.parentNode.childNodes[ncount-1] === elem);}
);}}
}
if(!ff){ff = function(){ return true; };}
return _simpleFiltersCache[query] = ff;}
var isTagOnly = function(query){return (Math.max.apply(this, _getIndexes(query)) == -1);}
var _getElementsFuncCache = {};var getElementsFunc = function(query, root){var fHit = _getElementsFuncCache[query];if(fHit){ return fHit; }
var retFunc = null;var i = _getIndexes(query);var id = getId(query);if(i[0] == 0){return _getElementsFuncCache[query] = function(root){return [ d.byId(id) ];}}
var filterFunc = getSimpleFilterFunc(query);if(i[0] >= 0){retFunc = function(root){var te = d.byId(id);if(filterFunc(te)){return [ te ];}}
}else{var ret = [];var tret;var tn = getTagName(query);if(-1 != i[3]){var pseudoName = (0 <= i[3]) ? query.substr(i[3]+1) : "";switch(pseudoName){case "first":
retFunc = function(root){var tret = root.getElementsByTagName(tn);for(var x=0, te; te = tret[x]; x++){if(filterFunc(te)){return [ te ];}}
return [];}
break;case "last":
retFunc = function(root){var tret = root.getElementsByTagName(tn);for(var x=tret.length-1, te; te = tret[x]; x--){if(filterFunc(te)){return [ te ];}}
return [];}
break;default:
retFunc = function(root){var tret = root.getElementsByTagName(tn);var ret = [];for(var x=0, te; te = tret[x]; x++){if(filterFunc(te)){ret[ret.length] = te;}}
return ret;}
break;}}else if(isTagOnly(query)){retFunc = function(root){var ret = [];var tret = root.getElementsByTagName(tn);for(var x=0, te; te = tret[x]; x++){ret.push(te);}
return ret;}}else{retFunc = function(root){var tret = root.getElementsByTagName(tn);var ret = [];for(var x=0, te; te = tret[x]; x++){if(filterFunc(te)){ret.push(te);}}
return ret;}}
}
return _getElementsFuncCache[query] = retFunc;}
var getElements = function(query, root){if(!root){ root = document; }
return getElementsFunc(query)(root);}
var _partsCache = {};var _queryFuncCache = {};var getStepQueryFunc = function(query){if(_queryFuncCache[query]){ return _queryFuncCache[query]; }
if(0 > query.indexOf(" ")){_queryFuncCache[query] = function(root){return getElements(query, root);}
return _queryFuncCache[query];}
var sqf = function(root){var qparts = query.split(" ");var partIndex = 0;var lastRoot;while((partIndex < qparts.length)&&(0 <= qparts[partIndex].indexOf("#"))){lastRoot = root;root = getElements(qparts[partIndex])[0];if(!root){ root = lastRoot; break; }
partIndex++;}
if(qparts.length == partIndex){return [ root ];}
root = root || document;var candidates;if(isTagOnly(qparts[partIndex]) && (qparts[partIndex+1] != ">")){qparts = qparts.slice(partIndex);var searchParts = [];var idx = 0;while(qparts[idx] && isTagOnly(qparts[idx]) && (qparts[idx+1] != ">" )){searchParts.push(qparts[idx]);idx++;}
var curLevelItems = [ root ];var nextLevelItems = [];for(var x=0; x<searchParts.length; x++){var tsp = qparts.shift();for(var y=0; y<curLevelItems.length; y++){var tcol = curLevelItems[y].getElementsByTagName(tsp);for(var z=0; z<tcol.length; z++){nextLevelItems.push(tcol[z]);}}
curLevelItems = nextLevelItems;nextLevelItems = [];}
candidates = curLevelItems;if(!qparts.length){return candidates;}}else{candidates = getElements(qparts.shift(), root);}
return filterDown(candidates, qparts);}
_queryFuncCache[query] = sqf;return sqf;}
var _getQueryFunc = (
(document["evaluate"] && !dojo.render.html.safari) ?
function(query){if(_queryFuncCache[query]){ return _queryFuncCache[query]; }
var qparts = query.split(" ");if((document["evaluate"])&&
(query.indexOf(":") == -1) ){var gtIdx = query.indexOf(">")
if(
((qparts.length > 2)&&(query.indexOf(">") == -1))||
(qparts.length > 3)||
((0 > query.indexOf(" "))&&(0 == query.indexOf(".")))
){return _queryFuncCache[query] = getXPathFunc(query);}}
return getStepQueryFunc(query);} : getStepQueryFunc
);var getQueryFunc = function(query){if(_queryFuncCache[query]){ return _queryFuncCache[query]; }
if(0 > query.indexOf(",")){return _queryFuncCache[query] = _getQueryFunc(query);}else{var parts = query.split(", ");var tf = function(root){var pindex = 0;var ret = [];var tp;while(tp = parts[pindex++]){ret = ret.concat(_getQueryFunc(tp, tp.indexOf(" "))(root));}
return ret;}
return _queryFuncCache[query] = tf;}}
var _zipIdx = 0;var _zip = function(arr){if(!arr){ return []; }
var al = arr.length;if(al < 2){ return arr; }
_zipIdx++;var ret = [arr[0]];arr[0]["_zipIdx"] = _zipIdx;for(var x=1; x<arr.length; x++){if(arr[x]["_zipIdx"] != _zipIdx){ret.push(arr[x]);}
arr[x]["_zipIdx"] = _zipIdx;}
return ret;}
d.query = function(query, root){return _zip(getQueryFunc(query)(root));}})();