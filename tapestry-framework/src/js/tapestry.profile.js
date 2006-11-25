// pull in the dependency list and define it in the var "dependencies". This
// over-rides the default built into getDependencyList.js. The bootstrap and
// hostenv files are included by default and don't need to be included here,
// but you can change the hostenv file that's included by setting the value of
// the variable "hostenvType" (defaults to "browser").
var dependencies = [ 
	"dojo.event.browser",
    "dojo.dom",
    "dojo.lang.common",
    "dojo.io.common",
    "dojo.io.BrowserIO",
    "dojo.io.cookie",
    "dojo.date.common",
    "dojo.date.format",
	"dojo.widget.*"
];

// NOTE: this MUST be included or a list of files must be output via print()
// manually.
load("getDependencyList.js");
