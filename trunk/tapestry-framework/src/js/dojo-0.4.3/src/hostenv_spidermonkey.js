dojo.hostenv.name_="spidermonkey";
dojo.hostenv.println=print;
dojo.hostenv.exit=function(_1){
quit(_1);
};
dojo.hostenv.getVersion=function(){
return version();
};
if(typeof line2pc=="undefined"){
dojo.raise("attempt to use SpiderMonkey host environment when no 'line2pc' global");
}
function dj_spidermonkey_current_file(_2){
var s="";
try{
throw Error("whatever");
}
catch(e){
s=e.stack;
}
var _4=s.match(/[^@]*\.js/gi);
if(!_4){
dojo.raise("could not parse stack string: '"+s+"'");
}
var _5=(typeof _2!="undefined"&&_2)?_4[_2+1]:_4[_4.length-1];
if(!_5){
dojo.raise("could not find file name in stack string '"+s+"'");
}
return _5;
}
if(!dojo.hostenv.library_script_uri_){
dojo.hostenv.library_script_uri_=dj_spidermonkey_current_file(0);
}
dojo.hostenv.loadUri=function(_6){
var ok=load(_6);
return 1;
};
dojo.requireIf((djConfig["isDebug"]||djConfig["debugAtAllCosts"]),"dojo.debug");
