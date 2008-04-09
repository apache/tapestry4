// pull in the dependency list and define it in the var "dependencies". This
// over-rides the default built into getDependencyList.js. The bootstrap and
// hostenv files are included by default and don't need to be included here,
// but you can change the hostenv file that's included by setting the value of
// the variable "hostenvType" (defaults to "browser").
var dependencies = [
	"dojo.event.browser",
    "dojo.io.common",
    "dojo.io.BrowserIO",
    "dojo.io.cookie",
    "dojo.lang.common",
    "dojo.lang.declare",
    "dojo.html.*",
    "dojo.html.display",
    "dojo.ns",
    "dojo.widget.Manager",
    "dojo.tlocale"
];

dependencies.layers = [
	{
		name: "dojo2.js",
		resourceName: "layer.validation",
		layerDependencies: [
			"dojo.js"
		],
		dependencies: [
            "dojo.i18n.number",
            "dojo.validate.check",
            "dojo.string.extras",
            "dojo.html.style",
            "dojo.regexp",
            "dojo.date.format",
            "dojo.date.serialize",
            "dojo.validate.datetime",
            "dojo.validate.web",
            "dojo.validate.creditCard",
            "dojo.validate.us"
        ]
	},
	{
		name: "dojo3.js",
		resourceName: "layer.widget",
		layerDependencies: [
			"dojo2.js"
		],
		dependencies: [
            "dojo.namespaces.dojo",
            "dojo.widget.*",
            "dojo.widget.ContentPane",
            "dojo.widget.PopupContainer",
            "dojo.widget.DropdownContainer",
            "dojo.widget.Manager",
            "dojo.widget.html.stabile",
            "dojo.widget.Dialog",
            "dojo.widget.Select"
        ]
	}
];

// NOTE: this MUST be included or a list of files must be output via print()
// manually.
load("getDependencyList.js");
