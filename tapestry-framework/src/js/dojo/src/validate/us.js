
dojo.provide("dojo.validate.us");dojo.require("dojo.validate.common");dojo.validate.us.isCurrency = function(value, flags){return dojo.validate.isCurrency(value, flags);}
dojo.validate.us.isState = function(value, flags){var re = new RegExp("^" + dojo.regexp.us.state(flags) + "$", "i");return re.test(value);}
dojo.validate.us.isPhoneNumber = function(value){var flags = {format: [
"###-###-####","(###) ###-####","(###) ### ####","###.###.####","###/###-####","### ### ####","###-###-#### x#???","(###) ###-#### x#???","(###) ### #### x#???","###.###.#### x#???","###/###-#### x#???","### ### #### x#???","##########"
]
};return dojo.validate.isNumberFormat(value, flags);}
dojo.validate.us.isSocialSecurityNumber = function(value){var flags = {format: [
"###-##-####","### ## ####","#########"
]
};return dojo.validate.isNumberFormat(value, flags);}
dojo.validate.us.isZipCode = function(value){var flags = {format: [
"#####-####","##### ####","#########","#####"
]
};return dojo.validate.isNumberFormat(value, flags);}
