dojo.provide("dojo.validate.us");
dojo.require("dojo.validate.common");
dojo.validate.us.isCurrency=function(_1,_2){
return dojo.validate.isCurrency(_1,_2);
};
dojo.validate.us.isState=function(_3,_4){
var re=new RegExp("^"+dojo.regexp.us.state(_4)+"$","i");
return re.test(_3);
};
dojo.validate.us.isPhoneNumber=function(_6){
var _7={format:["###-###-####","(###) ###-####","(###) ### ####","###.###.####","###/###-####","### ### ####","###-###-#### x#???","(###) ###-#### x#???","(###) ### #### x#???","###.###.#### x#???","###/###-#### x#???","### ### #### x#???","##########"]};
return dojo.validate.isNumberFormat(_6,_7);
};
dojo.validate.us.isSocialSecurityNumber=function(_8){
var _9={format:["###-##-####","### ## ####","#########"]};
return dojo.validate.isNumberFormat(_8,_9);
};
dojo.validate.us.isZipCode=function(_a){
var _b={format:["#####-####","##### ####","#########","#####"]};
return dojo.validate.isNumberFormat(_a,_b);
};
