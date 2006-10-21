

dojo.provide("dojo.string.common");

dojo.string.trim = function(str, wh){


if(!str.replace){ return str; }
if(!str.length){ return str; }
var re = (wh > 0) ? (/^\s+/) : (wh < 0) ? (/\s+$/) : (/^\s+|\s+$/g);
return str.replace(re, "");	//	string
}

dojo.string.trimStart = function(str) {


return dojo.string.trim(str, 1);	//	string
}

dojo.string.trimEnd = function(str) {


return dojo.string.trim(str, -1);
}

dojo.string.repeat = function(str, count, separator) {


var out = "";
for(var i = 0; i < count; i++) {
out += str;
if(separator && i < count - 1) {
out += separator;
}
}
return out;	//	string
}

dojo.string.pad = function(str, len,  c, dir) {



var out = String(str);
if(!c) {
c = '0';
}
if(!dir) {
dir = 1;
}
while(out.length < len) {
if(dir > 0) {
out = c + out;
} else {
out += c;
}
}
return out;	//	string
}

dojo.string.padLeft = function(str, len, c) {


return dojo.string.pad(str, len, c, 1);	//	string
}

dojo.string.padRight = function(str, len, c) {


return dojo.string.pad(str, len, c, -1);	//	string
}
