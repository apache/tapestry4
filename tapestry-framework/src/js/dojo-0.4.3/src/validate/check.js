dojo.provide("dojo.validate.check");
dojo.require("dojo.validate.common");
dojo.require("dojo.lang.common");
dojo.validate.check=function(_1,_2){
var _3=[];
var _4=[];
var _5={isSuccessful:function(){
return (!this.hasInvalid()&&!this.hasMissing());
},hasMissing:function(){
return (_3.length>0);
},getMissing:function(){
return _3;
},isMissing:function(_6){
for(var i=0;i<_3.length;i++){
if(_6==_3[i]){
return true;
}
}
return false;
},hasInvalid:function(){
return (_4.length>0);
},getInvalid:function(){
return _4;
},isInvalid:function(_8){
for(var i=0;i<_4.length;i++){
if(_8==_4[i]){
return true;
}
}
return false;
}};
if(_2.trim instanceof Array){
for(var i=0;i<_2.trim.length;i++){
var _b=_1[_2.trim[i]];
if(dj_undef("type",_b)||_b.type!="text"&&_b.type!="textarea"&&_b.type!="password"){
continue;
}
_b.value=_b.value.replace(/(^\s*|\s*$)/g,"");
}
}
if(_2.uppercase instanceof Array){
for(var i=0;i<_2.uppercase.length;i++){
var _b=_1[_2.uppercase[i]];
if(dj_undef("type",_b)||_b.type!="text"&&_b.type!="textarea"&&_b.type!="password"){
continue;
}
_b.value=_b.value.toUpperCase();
}
}
if(_2.lowercase instanceof Array){
for(var i=0;i<_2.lowercase.length;i++){
var _b=_1[_2.lowercase[i]];
if(dj_undef("type",_b)||_b.type!="text"&&_b.type!="textarea"&&_b.type!="password"){
continue;
}
_b.value=_b.value.toLowerCase();
}
}
if(_2.ucfirst instanceof Array){
for(var i=0;i<_2.ucfirst.length;i++){
var _b=_1[_2.ucfirst[i]];
if(dj_undef("type",_b)||_b.type!="text"&&_b.type!="textarea"&&_b.type!="password"){
continue;
}
_b.value=_b.value.replace(/\b\w+\b/g,function(_c){
return _c.substring(0,1).toUpperCase()+_c.substring(1).toLowerCase();
});
}
}
if(_2.digit instanceof Array){
for(var i=0;i<_2.digit.length;i++){
var _b=_1[_2.digit[i]];
if(dj_undef("type",_b)||_b.type!="text"&&_b.type!="textarea"&&_b.type!="password"){
continue;
}
_b.value=_b.value.replace(/\D/g,"");
}
}
if(_2.required instanceof Array){
for(var i=0;i<_2.required.length;i++){
if(!dojo.lang.isString(_2.required[i])){
continue;
}
var _b=_1[_2.required[i]];
if(!dj_undef("type",_b)&&(_b.type=="text"||_b.type=="textarea"||_b.type=="password"||_b.type=="file")&&/^\s*$/.test(_b.value)){
_3[_3.length]=_b.name;
}else{
if(!dj_undef("type",_b)&&(_b.type=="select-one"||_b.type=="select-multiple")&&(_b.selectedIndex==-1||/^\s*$/.test(_b.options[_b.selectedIndex].value))){
_3[_3.length]=_b.name;
}else{
if(dojo.lang.isArrayLike(_b)){
var _d=false;
for(var j=0;j<_b.length;j++){
if(_b[j].checked){
_d=true;
}
}
if(!_d){
_3[_3.length]=_b[0].name;
}
}
}
}
}
}
if(_2.required instanceof Array){
for(var i=0;i<_2.required.length;i++){
if(!dojo.lang.isObject(_2.required[i])){
continue;
}
var _b,_f;
for(var _10 in _2.required[i]){
_b=_1[_10];
_f=_2.required[i][_10];
}
if(dojo.lang.isArrayLike(_b)){
var _d=0;
for(var j=0;j<_b.length;j++){
if(_b[j].checked){
_d++;
}
}
if(_d<_f){
_3[_3.length]=_b[0].name;
}
}else{
if(!dj_undef("type",_b)&&_b.type=="select-multiple"){
var _11=0;
for(var j=0;j<_b.options.length;j++){
if(_b.options[j].selected&&!/^\s*$/.test(_b.options[j].value)){
_11++;
}
}
if(_11<_f){
_3[_3.length]=_b.name;
}
}
}
}
}
if(dojo.lang.isObject(_2.dependencies)||dojo.lang.isObject(_2.dependancies)){
if(_2["dependancies"]){
dojo.deprecated("dojo.validate.check","profile 'dependancies' is deprecated, please use "+"'dependencies'","0.5");
_2.dependencies=_2.dependancies;
}
for(_10 in _2.dependencies){
var _b=_1[_10];
if(dj_undef("type",_b)){
continue;
}
if(_b.type!="text"&&_b.type!="textarea"&&_b.type!="password"){
continue;
}
if(/\S+/.test(_b.value)){
continue;
}
if(_5.isMissing(_b.name)){
continue;
}
var _12=_1[_2.dependencies[_10]];
if(_12.type!="text"&&_12.type!="textarea"&&_12.type!="password"){
continue;
}
if(/^\s*$/.test(_12.value)){
continue;
}
_3[_3.length]=_b.name;
}
}
if(dojo.lang.isObject(_2.constraints)){
for(_10 in _2.constraints){
var _b=_1[_10];
if(!_b){
continue;
}
if(!dj_undef("tagName",_b)&&(_b.tagName.toLowerCase().indexOf("input")>=0||_b.tagName.toLowerCase().indexOf("textarea")>=0)&&/^\s*$/.test(_b.value)){
continue;
}
var _13=true;
if(dojo.lang.isFunction(_2.constraints[_10])){
_13=_2.constraints[_10](_b.value);
}else{
if(dojo.lang.isArray(_2.constraints[_10])){
if(dojo.lang.isArray(_2.constraints[_10][0])){
for(var i=0;i<_2.constraints[_10].length;i++){
_13=dojo.validate.evaluateConstraint(_2,_2.constraints[_10][i],_10,_b);
if(!_13){
break;
}
}
}else{
_13=dojo.validate.evaluateConstraint(_2,_2.constraints[_10],_10,_b);
}
}
}
if(!_13){
_4[_4.length]=_b.name;
}
}
}
if(dojo.lang.isObject(_2.confirm)){
for(_10 in _2.confirm){
var _b=_1[_10];
var _12=_1[_2.confirm[_10]];
if(dj_undef("type",_b)||dj_undef("type",_12)||(_b.type!="text"&&_b.type!="textarea"&&_b.type!="password")||(_12.type!=_b.type)||(_12.value==_b.value)||(_5.isInvalid(_b.name))||(/^\s*$/.test(_12.value))){
continue;
}
_4[_4.length]=_b.name;
}
}
return _5;
};
dojo.validate.evaluateConstraint=function(_14,_15,_16,_17){
var _18=_15[0];
var _19=_15.slice(1);
_19.unshift(_17.value);
if(typeof _18!="undefined"){
return _18.apply(null,_19);
}
return false;
};
