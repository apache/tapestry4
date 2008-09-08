dojo.provide("dojo.ns");
dojo.ns={namespaces:{},failed:{},loading:{},loaded:{},register:function(_1,_2,_3,_4){
if(!_4||!this.namespaces[_1]){
this.namespaces[_1]=new dojo.ns.Ns(_1,_2,_3);
}
},allow:function(_5){
if(this.failed[_5]){
return false;
}
if((djConfig.excludeNamespace)&&(dojo.lang.inArray(djConfig.excludeNamespace,_5))){
return false;
}
return ((_5==this.dojo)||(!djConfig.includeNamespace)||(dojo.lang.inArray(djConfig.includeNamespace,_5)));
},get:function(_6){
return this.namespaces[_6];
},require:function(_7){
var ns=this.namespaces[_7];
if((ns)&&(this.loaded[_7])){
return ns;
}
if(!this.allow(_7)){
return false;
}
if(this.loading[_7]){
dojo.debug("dojo.namespace.require: re-entrant request to load namespace \""+_7+"\" must fail.");
return false;
}
var _9=dojo.require;
this.loading[_7]=true;
try{
if(_7=="dojo"){
_9("dojo.namespaces.dojo");
}else{
if(!dojo.hostenv.moduleHasPrefix(_7)){
dojo.registerModulePath(_7,"../"+_7);
}
_9([_7,"manifest"].join("."),false,true);
}
if(!this.namespaces[_7]){
this.failed[_7]=true;
}
}
finally{
this.loading[_7]=false;
}
return this.namespaces[_7];
}};
dojo.ns.Ns=function(_a,_b,_c){
this.name=_a;
this.module=_b;
this.resolver=_c;
this._loaded=[];
this._failed=[];
};
dojo.ns.Ns.prototype.resolve=function(_d,_e,_f){
if(!this.resolver||djConfig["skipAutoRequire"]){
return false;
}
var _10=this.resolver(_d,_e);
if((_10)&&(!this._loaded[_10])&&(!this._failed[_10])){
var req=dojo.require;
req(_10,false,true);
if(dojo.hostenv.findModule(_10,false)){
this._loaded[_10]=true;
}else{
if(!_f){
dojo.raise("dojo.ns.Ns.resolve: module '"+_10+"' not found after loading via namespace '"+this.name+"'");
}
this._failed[_10]=true;
}
}
return Boolean(this._loaded[_10]);
};
dojo.registerNamespace=function(_12,_13,_14){
dojo.ns.register.apply(dojo.ns,arguments);
};
dojo.registerNamespaceResolver=function(_15,_16){
var n=dojo.ns.namespaces[_15];
if(n){
n.resolver=_16;
}
};
dojo.registerNamespaceManifest=function(_18,_19,_1a,_1b,_1c){
dojo.registerModulePath(_1a,_19);
dojo.registerNamespace(_1a,_1b,_1c);
};
dojo.registerNamespace("dojo","dojo.widget");
