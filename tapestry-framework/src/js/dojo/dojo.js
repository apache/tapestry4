
if(typeof dojo=="undefined"){var dj_global=this;var dj_currentContext=this;function dj_undef(_1,_2){return (typeof (_2||dj_currentContext)[_1]=="undefined");}
if(dj_undef("djConfig",this)){var djConfig={};}
if(dj_undef("dojo",this)){var dojo={};}
dojo.global=function(){return dj_currentContext;};dojo.locale=djConfig.locale;dojo.version={major:0,minor:0,patch:0,flag:"dev",revision:Number("$Rev: 6986 $".match(/[0-9]+/)[0]),toString:function(){with(dojo.version){return major+"."+minor+"."+patch+flag+" ("+revision+")";}}};dojo.getObject=function(_3,_4,_5,_6){var _7,_8;if(typeof _3!="string"){return undefined;}
_7=_5;if(!_7){_7=dojo.global();}
var _9=_3.split("."),i=0,_b,_c,_d;do{_b=_7;_d=_9[i];_c=_7[_9[i]];if((_4)&&(!_c)){_c=_7[_9[i]]={};}
_7=_c;i++;}while(i<_9.length&&_7);_8=_7;_7=_b;return (_6)?{obj:_7,prop:_d}:_8;};dojo.exists=function(_e,_f){if(typeof _f=="string"){dojo.deprecated("dojo.exists(obj, name)","use dojo.exists(name, obj, create)","0.6");var tmp=_e;_e=_f;_f=tmp;}
return (!!dojo.getObject(_e,false,_f));};dojo.evalProp=function(_11,_12,_13){dojo.deprecated("dojo.evalProp","just use hash syntax. Sheesh.","0.6");return _12[_11]||(_13?(_12[_11]={}):undefined);};dojo.parseObjPath=function(_14,_15,_16){dojo.deprecated("dojo.parseObjPath","use dojo.getObject(path, create, context, true)","0.6");return dojo.getObject(_14,_16,_15,true);};dojo.evalObjPath=function(_17,_18){dojo.deprecated("dojo.evalObjPath","use dojo.getObject(path, create)","0.6");return dojo.getObject(_17,_18);};dojo.errorToString=function(_19){return (_19["message"]||_19["description"]||_19);};dojo.raise=function(_1a,_1b){if(_1b){_1a=_1a+": "+dojo.errorToString(_1b);}else{_1a=dojo.errorToString(_1a);}
try{if(djConfig.isDebug){dojo.hostenv.println("FATAL exception raised: "+_1a);}}
catch(e){}
throw _1b||Error(_1a);};dojo.debug=function(){};dojo.debugShallow=function(obj){};dojo.profile={start:function(){},end:function(){},stop:function(){},dump:function(){}};function dj_eval(_1d){return dj_global.eval?dj_global.eval(_1d):eval(_1d);}
dojo.unimplemented=function(_1e,_1f){var _20="'"+_1e+"' not implemented";if(_1f!=null){_20+=" "+_1f;}
dojo.raise(_20);};dojo.deprecated=function(_21,_22,_23){var _24="DEPRECATED: "+_21;if(_22){_24+=" "+_22;}
if(_23){_24+=" -- will be removed in version: "+_23;}
dojo.debug(_24);};dojo.render=(function(){function vscaffold(_25,_26){var tmp={capable:false,support:{builtin:false,plugin:false},prefixes:_25};for(var i=0;i<_26.length;i++){tmp[_26[i]]=false;}
return tmp;}
return {name:"",ver:dojo.version,os:{win:false,linux:false,osx:false},html:vscaffold(["html"],["ie","opera","khtml","safari","moz"]),svg:vscaffold(["svg"],["corel","adobe","batik"]),vml:vscaffold(["vml"],["ie"]),swf:vscaffold(["Swf","Flash","Mm"],["mm"]),swt:vscaffold(["Swt"],["ibm"])};})();dojo.hostenv=(function(){var _29={isDebug:false,allowQueryConfig:false,baseScriptUri:"",baseRelativePath:"",libraryScriptUri:"",iePreventClobber:false,ieClobberMinimal:true,preventBackButtonFix:true,delayMozLoadingFix:false,searchIds:[],parseWidgets:true};if(typeof djConfig=="undefined"){djConfig=_29;}else{for(var _2a in _29){if(typeof djConfig[_2a]=="undefined"){djConfig[_2a]=_29[_2a];}}
}
return {name_:"(unset)",version_:"(unset)",getName:function(){return this.name_;},getVersion:function(){return this.version_;},getText:function(uri){dojo.unimplemented("getText","uri="+uri);}};})();dojo.hostenv.getBaseScriptUri=function(){if(djConfig.baseScriptUri.length){return djConfig.baseScriptUri;}
var uri=new String(djConfig.libraryScriptUri||djConfig.baseRelativePath);if(!uri){dojo.raise("Nothing returned by getLibraryScriptUri(): "+uri);}
djConfig.baseScriptUri=djConfig.baseRelativePath;return djConfig.baseScriptUri;};(function(){var _2d={pkgFileName:"__package__",loading_modules_:{},loaded_modules_:{},addedToLoadingCount:[],removedFromLoadingCount:[],inFlightCount:0,modulePrefixes_:{dojo:{name:"dojo",value:"src"}},registerModulePath:function(_2e,_2f){this.modulePrefixes_[_2e]={name:_2e,value:_2f};},moduleHasPrefix:function(_30){var mp=this.modulePrefixes_;return Boolean(mp[_30]&&mp[_30].value);},getModulePrefix:function(_32){if(this.moduleHasPrefix(_32)){return this.modulePrefixes_[_32].value;}
return _32;},getTextStack:[],loadUriStack:[],loadedUris:[],post_load_:false,modulesLoadedListeners:[],unloadListeners:[],loadNotifying:false};for(var _33 in _2d){dojo.hostenv[_33]=_2d[_33];}})();dojo.hostenv.loadPath=function(_34,_35,cb){var uri;if(_34.charAt(0)=="/"||_34.match(/^\w+:/)){uri=_34;}else{uri=this.getBaseScriptUri()+_34;}
if(djConfig.cacheBust&&dojo.render.html.capable){uri+="?"+String(djConfig.cacheBust).replace(/\W+/g,"");}
try{return !_35?this.loadUri(uri,cb):this.loadUriAndCheck(uri,_35,cb);}
catch(e){dojo.debug(e);return false;}};dojo.hostenv.loadUri=function(uri,cb){if(this.loadedUris[uri]){return true;}
var _3a=this.getText(uri,null,true);if(!_3a){return false;}
this.loadedUris[uri]=true;if(cb){_3a="("+_3a+")";}
var _3b=dj_eval(_3a);if(cb){cb(_3b);}
return true;};dojo.hostenv.loadUriAndCheck=function(uri,_3d,cb){var ok=true;try{ok=this.loadUri(uri,cb);}
catch(e){dojo.debug("failed loading ",uri," with error: ",e);}
return Boolean(ok&&this.findModule(_3d,false));};dojo.loaded=function(){};dojo.unloaded=function(){};dojo.hostenv.loaded=function(){this.loadNotifying=true;this.post_load_=true;var mll=this.modulesLoadedListeners;for(var x=0;x<mll.length;x++){mll[x]();}
this.modulesLoadedListeners=[];this.loadNotifying=false;dojo.loaded();};dojo.hostenv.unloaded=function(){var mll=this.unloadListeners;while(mll.length){(mll.pop())();}
dojo.unloaded();};dojo.addOnLoad=function(obj,_44){var dh=dojo.hostenv;if(arguments.length==1){dh.modulesLoadedListeners.push(obj);}else{if(arguments.length>1){dh.modulesLoadedListeners.push(function(){obj[_44]();});}}
if(dh.post_load_&&dh.inFlightCount==0&&!dh.loadNotifying){dh.callLoaded();}};dojo.addOnUnload=function(obj,_47){var dh=dojo.hostenv;if(arguments.length==1){dh.unloadListeners.push(obj);}else{if(arguments.length>1){dh.unloadListeners.push(function(){obj[_47]();});}}
};dojo.hostenv.modulesLoaded=function(){if(this.post_load_){return;}
if(this.loadUriStack.length==0&&this.getTextStack.length==0){if(this.inFlightCount>0){dojo.debug("files still in flight!");return;}
dojo.hostenv.callLoaded();}};dojo.hostenv.callLoaded=function(){if(typeof setTimeout=="object"){setTimeout("dojo.hostenv.loaded();",0);}else{dojo.hostenv.loaded();}};dojo.hostenv.getModuleSymbols=function(_49){var _4a=_49.split(".");for(var i=_4a.length;i>0;i--){var _4c=_4a.slice(0,i).join(".");if((i==1)&&!this.moduleHasPrefix(_4c)){_4a[0]="../"+_4a[0];}else{var _4d=this.getModulePrefix(_4c);if(_4d!=_4c){_4a.splice(0,i,_4d);break;}}
}
return _4a;};dojo.hostenv._global_omit_module_check=false;dojo.hostenv.loadModule=function(_4e,_4f,_50){if(!_4e){return;}
_50=this._global_omit_module_check||_50;var _51=this.findModule(_4e,false);if(_51){return _51;}
if(dj_undef(_4e,this.loading_modules_)){this.addedToLoadingCount.push(_4e);}
this.loading_modules_[_4e]=1;var _52=_4e.replace(/\./g,"/")+".js";var _53=_4e.split(".");var _54=this.getModuleSymbols(_4e);var _55=((_54[0].charAt(0)!="/")&&!_54[0].match(/^\w+:/));var _56=_54[_54.length-1];var ok;if(_56=="*"){_4e=_53.slice(0,-1).join(".");while(_54.length){_54.pop();_54.push(this.pkgFileName);_52=_54.join("/")+".js";if(_55&&_52.charAt(0)=="/"){_52=_52.slice(1);}
ok=this.loadPath(_52,!_50?_4e:null);if(ok){break;}
_54.pop();}}else{_52=_54.join("/")+".js";_4e=_53.join(".");var _58=!_50?_4e:null;ok=this.loadPath(_52,_58);if(!ok&&!_4f){_54.pop();while(_54.length){_52=_54.join("/")+".js";ok=this.loadPath(_52,_58);if(ok){break;}
_54.pop();_52=_54.join("/")+"/"+this.pkgFileName+".js";if(_55&&_52.charAt(0)=="/"){_52=_52.slice(1);}
ok=this.loadPath(_52,_58);if(ok){break;}}
}
if(!ok&&!_50){dojo.raise("Could not load '"+_4e+"'; last tried '"+_52+"'");}}
if(!_50&&!this["isXDomain"]){_51=this.findModule(_4e,false);if(!_51){dojo.raise("symbol '"+_4e+"' is not defined after loading '"+_52+"'");}}
return _51;};dojo.hostenv.startPackage=function(_59){var _5a=String(_59);var _5b=_5a;var _5c=_59.split(/\./);if(_5c[_5c.length-1]=="*"){_5c.pop();_5b=_5c.join(".");}
var _5d=dojo.getObject(_5b,true);this.loaded_modules_[_5a]=_5d;this.loaded_modules_[_5b]=_5d;return _5d;};dojo.hostenv.findModule=function(_5e,_5f){var lmn=String(_5e);if(this.loaded_modules_[lmn]){return this.loaded_modules_[lmn];}
if(_5f){dojo.raise("no loaded module named '"+_5e+"'");}
return null;};dojo.kwCompoundRequire=function(_61){var _62=_61["common"]||[];var _63=_61[dojo.hostenv.name_]?_62.concat(_61[dojo.hostenv.name_]||[]):_62.concat(_61["default"]||[]);for(var x=0;x<_63.length;x++){var _65=_63[x];if(_65.constructor==Array){dojo.hostenv.loadModule.apply(dojo.hostenv,_65);}else{dojo.hostenv.loadModule(_65);}}
};dojo.require=function(_66){dojo.hostenv.loadModule.apply(dojo.hostenv,arguments);};dojo.requireIf=function(_67,_68){var _69=arguments[0];if((_69===true)||(_69=="common")||(_69&&dojo.render[_69].capable)){var _6a=[];for(var i=1;i<arguments.length;i++){_6a.push(arguments[i]);}
dojo.require.apply(dojo,_6a);}};dojo.requireAfterIf=dojo.requireIf;dojo.provide=function(_6c){return dojo.hostenv.startPackage.apply(dojo.hostenv,arguments);};dojo.registerModulePath=function(_6d,_6e){return dojo.hostenv.registerModulePath(_6d,_6e);};if(djConfig["modulePaths"]){for(var param in djConfig["modulePaths"]){dojo.registerModulePath(param,djConfig["modulePaths"][param]);}}
dojo.hostenv.normalizeLocale=function(_6f){var _70=_6f?_6f.toLowerCase():dojo.locale;if(_70=="root"){_70="ROOT";}
return _70;};dojo.hostenv.searchLocalePath=function(_71,_72,_73){_71=dojo.hostenv.normalizeLocale(_71);var _74=_71.split("-");var _75=[];for(var i=_74.length;i>0;i--){_75.push(_74.slice(0,i).join("-"));}
_75.push(false);if(_72){_75.reverse();}
for(var j=_75.length-1;j>=0;j--){var loc=_75[j]||"ROOT";var _79=_73(loc);if(_79){break;}}
};dojo.hostenv.localesGenerated;dojo.hostenv.registerNlsPrefix=function(){dojo.registerModulePath("nls","nls");};dojo.hostenv.preloadLocalizations=function(){if(dojo.hostenv.localesGenerated){dojo.hostenv.registerNlsPrefix();function preload(_7a){_7a=dojo.hostenv.normalizeLocale(_7a);dojo.hostenv.searchLocalePath(_7a,true,function(loc){for(var i=0;i<dojo.hostenv.localesGenerated.length;i++){if(dojo.hostenv.localesGenerated[i]==loc){dojo["require"]("nls.dojo_"+loc);return true;}}
return false;});}
preload();var _7d=djConfig.extraLocale||[];for(var i=0;i<_7d.length;i++){preload(_7d[i]);}}
dojo.hostenv.preloadLocalizations=function(){};};dojo.requireLocalization=function(_7f,_80,_81,_82){dojo.hostenv.preloadLocalizations();var _83=dojo.hostenv.normalizeLocale(_81);var _84=[_7f,"nls",_80].join(".");var _85="";if(_82){var _86=_82.split(",");for(var i=0;i<_86.length;i++){if(_83.indexOf(_86[i])==0){if(_86[i].length>_85.length){_85=_86[i];}}
}
if(!_85){_85="ROOT";}}
var _88=_82?_85:_83;var _89=dojo.hostenv.findModule(_84);var _8a=null;if(_89){if(djConfig.localizationComplete&&_89._built){return;}
var _8b=_88.replace("-","_");var _8c=_84+"."+_8b;_8a=dojo.hostenv.findModule(_8c);}
if(!_8a){_89=dojo.hostenv.startPackage(_84);var _8d=dojo.hostenv.getModuleSymbols(_7f);var _8e=_8d.concat("nls").join("/");var _8f;dojo.hostenv.searchLocalePath(_88,_82,function(loc){var _91=loc.replace("-","_");var _92=_84+"."+_91;var _93=false;if(!dojo.hostenv.findModule(_92)){dojo.hostenv.startPackage(_92);var _94=[_8e];if(loc!="ROOT"){_94.push(loc);}
_94.push(_80);var _95=_94.join("/")+".js";_93=dojo.hostenv.loadPath(_95,null,function(_96){var _97=function(){};_97.prototype=_8f;_89[_91]=new _97();for(var j in _96){_89[_91][j]=_96[j];}});}else{_93=true;}
if(_93&&_89[_91]){_8f=_89[_91];}else{_89[_91]=_8f;}
if(_82){return true;}});}
if(_82&&_83!=_85){_89[_83.replace("-","_")]=_89[_85.replace("-","_")];}};(function(){var _99=djConfig.extraLocale;if(_99){if(!_99 instanceof Array){_99=[_99];}
var req=dojo.requireLocalization;dojo.requireLocalization=function(m,b,_9d,_9e){req(m,b,_9d,_9e);if(_9d){return;}
for(var i=0;i<_99.length;i++){req(m,b,_99[i],_9e);}};}})();}
if(typeof window!="undefined"){(function(){if(djConfig.allowQueryConfig){var _a0=document.location.toString();var _a1=_a0.split("?",2);if(_a1.length>1){var _a2=_a1[1];var _a3=_a2.split("&");for(var x in _a3){var sp=_a3[x].split("=");if((sp[0].length>9)&&(sp[0].substr(0,9)=="djConfig.")){var opt=sp[0].substr(9);try{djConfig[opt]=eval(sp[1]);}
catch(e){djConfig[opt]=sp[1];}}
}}
}
if(((djConfig["baseScriptUri"]=="")||(djConfig["baseRelativePath"]==""))&&(document&&document.getElementsByTagName)){var _a7=document.getElementsByTagName("script");var _a8=/(__package__|dojo|bootstrap1)\.js([\?\.]|$)/i;for(var i=0;i<_a7.length;i++){var src=_a7[i].getAttribute("src");if(!src){continue;}
var m=src.match(_a8);if(m){var _ac=src.substring(0,m.index);if(src.indexOf("bootstrap1")>-1){_ac+="../";}
if(!this["djConfig"]){djConfig={};}
if(djConfig["baseScriptUri"]==""){djConfig["baseScriptUri"]=_ac;}
if(djConfig["baseRelativePath"]==""){djConfig["baseRelativePath"]=_ac;}
break;}}
}
var dr=dojo.render;var drh=dojo.render.html;var drs=dojo.render.svg;var dua=(drh.UA=navigator.userAgent);var dav=(drh.AV=navigator.appVersion);var t=true;var f=false;drh.capable=t;drh.support.builtin=t;dr.ver=parseFloat(drh.AV);dr.os.mac=dav.indexOf("Macintosh")>=0;dr.os.win=dav.indexOf("Windows")>=0;dr.os.linux=dav.indexOf("X11")>=0;drh.opera=dua.indexOf("Opera")>=0;drh.khtml=(dav.indexOf("Konqueror")>=0)||(dav.indexOf("Safari")>=0);drh.safari=dav.indexOf("Safari")>=0;var _b4=dua.indexOf("Gecko");drh.mozilla=drh.moz=(_b4>=0)&&(!drh.khtml);if(drh.mozilla){drh.geckoVersion=dua.substring(_b4+6,_b4+14);}
drh.ie=(document.all)&&(!drh.opera);drh.ie50=drh.ie&&dav.indexOf("MSIE 5.0")>=0;drh.ie55=drh.ie&&dav.indexOf("MSIE 5.5")>=0;drh.ie60=drh.ie&&dav.indexOf("MSIE 6.0")>=0;drh.ie70=drh.ie&&dav.indexOf("MSIE 7.0")>=0;var cm=document["compatMode"];drh.quirks=(cm=="BackCompat")||(cm=="QuirksMode")||drh.ie55||drh.ie50;dojo.locale=dojo.locale||(drh.ie?navigator.userLanguage:navigator.language).toLowerCase();dr.vml.capable=drh.ie;drs.capable=f;drs.support.plugin=f;drs.support.builtin=f;var _b6=window["document"];var tdi=_b6["implementation"];if((tdi)&&(tdi["hasFeature"])&&(tdi.hasFeature("org.w3c.dom.svg","1.0"))){drs.capable=t;drs.support.builtin=t;drs.support.plugin=f;}
if(drh.safari){var tmp=dua.split("AppleWebKit/")[1];var ver=parseFloat(tmp.split(" ")[0]);if(ver>=420){drs.capable=t;drs.support.builtin=t;drs.support.plugin=f;}}else{}})();dojo.hostenv.startPackage("dojo.hostenv");dojo.render.name=dojo.hostenv.name_="browser";dojo.hostenv.searchIds=[];dojo.hostenv._XMLHTTP_PROGIDS=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"];dojo.hostenv.getXmlhttpObject=function(){var _ba=null;var _bb=null;try{_ba=new XMLHttpRequest();}
catch(e){}
if(!_ba){for(var i=0;i<3;++i){var _bd=dojo.hostenv._XMLHTTP_PROGIDS[i];try{_ba=new ActiveXObject(_bd);}
catch(e){_bb=e;}
if(_ba){dojo.hostenv._XMLHTTP_PROGIDS=[_bd];break;}}
}
if(!_ba){return dojo.raise("XMLHTTP not available",_bb);}
return _ba;};dojo.hostenv._blockAsync=false;dojo.hostenv.getText=function(uri,_bf,_c0){if(!_bf){this._blockAsync=true;}
var _c1=this.getXmlhttpObject();function isDocumentOk(_c2){var _c3=_c2["status"];return Boolean((!_c3)||((200<=_c3)&&(300>_c3))||(_c3==304));}
if(_bf){var _c4=this,_c5=null,gbl=dojo.global();var xhr=dojo.getObject("dojo.io.XMLHTTPTransport");_c1.onreadystatechange=function(){if(_c5){gbl.clearTimeout(_c5);_c5=null;}
if(_c4._blockAsync||(xhr&&xhr._blockAsync)){_c5=gbl.setTimeout(function(){_c1.onreadystatechange.apply(this);},10);}else{if(4==_c1.readyState){if(isDocumentOk(_c1)){_bf(_c1.responseText);}}
}};}
_c1.open("GET",uri,_bf?true:false);try{_c1.send(null);if(_bf){return null;}
if(!isDocumentOk(_c1)){var err=Error("Unable to load "+uri+" status:"+_c1.status);err.status=_c1.status;err.responseText=_c1.responseText;throw err;}}
catch(e){this._blockAsync=false;if((_c0)&&(!_bf)){return null;}else{throw e;}}
this._blockAsync=false;return _c1.responseText;};dojo.hostenv.defaultDebugContainerId="dojoDebug";dojo.hostenv._println_buffer=[];dojo.hostenv._println_safe=false;dojo.hostenv.println=function(_c9){if(!dojo.hostenv._println_safe){dojo.hostenv._println_buffer.push(_c9);}else{try{var _ca=document.getElementById(djConfig.debugContainerId?djConfig.debugContainerId:dojo.hostenv.defaultDebugContainerId);if(!_ca){_ca=dojo.body();}
var div=document.createElement("div");div.appendChild(document.createTextNode(_c9));_ca.appendChild(div);}
catch(e){try{document.write("<div>"+_c9+"</div>");}
catch(e2){window.status=_c9;}}
}};dojo.addOnLoad(function(){dojo.hostenv._println_safe=true;while(dojo.hostenv._println_buffer.length>0){dojo.hostenv.println(dojo.hostenv._println_buffer.shift());}});function dj_addNodeEvtHdlr(_cc,_cd,fp){var _cf=_cc["on"+_cd]||function(){};_cc["on"+_cd]=function(){fp.apply(_cc,arguments);_cf.apply(_cc,arguments);};return true;}
function dj_load_init(e){var _d1=(e&&e.type)?e.type.toLowerCase():"load";if(arguments.callee.initialized||(_d1!="domcontentloaded"&&_d1!="load")){return;}
arguments.callee.initialized=true;if(typeof (_timer)!="undefined"){clearInterval(_timer);delete _timer;}
var _d2=function(){if(dojo.render.html.ie){dojo.hostenv.makeWidgets();}};if(dojo.hostenv.inFlightCount==0){_d2();dojo.hostenv.modulesLoaded();}else{dojo.hostenv.modulesLoadedListeners.unshift(_d2);}}
if(document.addEventListener){if(dojo.render.html.opera||(dojo.render.html.moz&&(djConfig["enableMozDomContentLoaded"]===true))){document.addEventListener("DOMContentLoaded",dj_load_init,null);}
window.addEventListener("load",dj_load_init,null);}
if(dojo.render.html.ie&&dojo.render.os.win){document.write("<scr"+"ipt defer src=\"//:\" "+"onreadystatechange=\"if(this.readyState=='complete'){dj_load_init();}\">"+"</scr"+"ipt>");}
if(/(WebKit|khtml)/i.test(navigator.userAgent)){var _timer=setInterval(function(){if(/loaded|complete/.test(document.readyState)){dj_load_init();}},10);}
if(dojo.render.html.ie){dj_addNodeEvtHdlr(window,"beforeunload",function(){dojo.hostenv._unloading=true;window.setTimeout(function(){dojo.hostenv._unloading=false;},0);});}
dj_addNodeEvtHdlr(window,"unload",function(){if((!dojo.render.html.ie)||(dojo.render.html.ie&&dojo.hostenv._unloading)){dojo.hostenv.unloaded();}});dojo.hostenv.makeWidgets=function(){var _d3=[];if(djConfig.searchIds&&djConfig.searchIds.length>0){_d3=_d3.concat(djConfig.searchIds);}
if(dojo.hostenv.searchIds&&dojo.hostenv.searchIds.length>0){_d3=_d3.concat(dojo.hostenv.searchIds);}
if((djConfig.parseWidgets)||(_d3.length>0)){if(dojo.getObject("dojo.widget.Parse")){var _d4=new dojo.xml.Parse();if(_d3.length>0){for(var x=0;x<_d3.length;x++){var _d6=document.getElementById(_d3[x]);if(!_d6){continue;}
var _d7=_d4.parseElement(_d6,null,true);dojo.widget.getParser().createComponents(_d7);}}else{if(djConfig.parseWidgets){var _d7=_d4.parseElement(dojo.body(),null,true);dojo.widget.getParser().createComponents(_d7);}}
}}
};dojo.addOnLoad(function(){if(!dojo.render.html.ie){dojo.hostenv.makeWidgets();}});try{if(dojo.render.html.ie){document.namespaces.add("v","urn:schemas-microsoft-com:vml");document.createStyleSheet().addRule("v\\:*","behavior:url(#default#VML)");}}
catch(e){}
dojo.hostenv.writeIncludes=function(){};if(!dj_undef("document",this)){dj_currentDocument=this.document;}
dojo.doc=function(){return dj_currentDocument;};dojo.body=function(){return dojo.doc().body||dojo.doc().getElementsByTagName("body")[0];};dojo.byId=function(id,doc){if((id)&&((typeof id=="string")||(id instanceof String))){if(!doc){doc=dj_currentDocument;}
var ele=doc.getElementById(id);if(ele&&(ele.id!=id)&&doc.all){ele=null;eles=doc.all[id];if(eles){if(eles.length){for(var i=0;i<eles.length;i++){if(eles[i].id==id){ele=eles[i];break;}}
}else{ele=eles;}}
}
return ele;}
return id;};dojo.setContext=function(_dc,_dd){dj_currentContext=_dc;dj_currentDocument=_dd;};dojo._fireCallback=function(_de,_df,_e0){if((_df)&&((typeof _de=="string")||(_de instanceof String))){_de=_df[_de];}
return (_df?_de.apply(_df,_e0||[]):_de());};dojo.withGlobal=function(_e1,_e2,_e3,_e4){var _e5;var _e6=dj_currentContext;var _e7=dj_currentDocument;try{dojo.setContext(_e1,_e1.document);_e5=dojo._fireCallback(_e2,_e3,_e4);}
finally{dojo.setContext(_e6,_e7);}
return _e5;};dojo.withDoc=function(_e8,_e9,_ea,_eb){var _ec;var _ed=dj_currentDocument;try{dj_currentDocument=_e8;_ec=dojo._fireCallback(_e9,_ea,_eb);}
finally{dj_currentDocument=_ed;}
return _ec;};}
dojo.requireIf((djConfig["isDebug"]||djConfig["debugAtAllCosts"]),"dojo.debug");dojo.requireIf(djConfig["debugAtAllCosts"]&&!window.widget&&!djConfig["useXDomain"],"dojo.browser_debug");dojo.requireIf(djConfig["debugAtAllCosts"]&&!window.widget&&djConfig["useXDomain"],"dojo.browser_debug_xd");dojo.provide("dojo.a11y");dojo.provide("dojo.dom");dojo.provide("dojo.event.*");dojo.provide("dojo.event.browser");dojo.provide("dojo.event.common");dojo.provide("dojo.event.topic");dojo.provide("dojo.gfx.color");dojo.provide("dojo.html.color");dojo.provide("dojo.html.common");dojo.provide("dojo.html.display");dojo.provide("dojo.html.layout");dojo.provide("dojo.html.style");dojo.provide("dojo.html.util");dojo.provide("dojo.io.BrowserIO");dojo.provide("dojo.io.common");dojo.provide("dojo.lang.array");dojo.provide("dojo.lang.common");dojo.provide("dojo.lang.declare");dojo.provide("dojo.lang.extras");dojo.provide("dojo.lang.func");dojo.provide("dojo.lfx.*");dojo.provide("dojo.lfx.Animation");dojo.provide("dojo.lfx.html");dojo.provide("dojo.lfx.toggler");dojo.provide("dojo.ns");dojo.provide("dojo.string");dojo.provide("dojo.string.common");dojo.provide("dojo.string.extras");dojo.provide("dojo.undo.browser");dojo.provide("dojo.uri.*");dojo.provide("dojo.uri.Uri");dojo.provide("dojo.widget.*");dojo.provide("dojo.widget.DomWidget");dojo.provide("dojo.widget.HtmlWidget");dojo.provide("dojo.widget.Manager");dojo.provide("dojo.widget.Parse");dojo.provide("dojo.widget.Widget");dojo.provide("dojo.xml.Parse");dojo.lang.inherits=function(_ee,_ef){if(!dojo.lang.isFunction(_ef)){dojo.raise("dojo.inherits: superclass argument ["+_ef+"] must be a function (subclass: ["+_ee+"']");}
_ee.prototype=new _ef();_ee.prototype.constructor=_ee;_ee.superclass=_ef.prototype;_ee["super"]=_ef.prototype;};dojo.lang._mixin=function(obj,_f1){var _f2={};for(var x in _f1){if((typeof _f2[x]=="undefined")||(_f2[x]!=_f1[x])){obj[x]=_f1[x];}}
if(dojo.render.html.ie&&(typeof (_f1["toString"])=="function")&&(_f1["toString"]!=obj["toString"])&&(_f1["toString"]!=_f2["toString"])){obj.toString=_f1.toString;}
return obj;};dojo.lang.mixin=function(obj,_f5){for(var i=1,l=arguments.length;i<l;i++){dojo.lang._mixin(obj,arguments[i]);}
return obj;};dojo.lang.extend=function(_f8,_f9){for(var i=1,l=arguments.length;i<l;i++){dojo.lang._mixin(_f8.prototype,arguments[i]);}
return _f8;};dojo.lang._delegate=function(obj,_fd){function TMP(){}
TMP.prototype=obj;var tmp=new TMP();if(_fd){dojo.lang.mixin(tmp,_fd);}
return tmp;};dojo.inherits=dojo.lang.inherits;dojo.mixin=dojo.lang.mixin;dojo.extend=dojo.lang.extend;dojo.lang.find=function(_ff,_100,_101,_102){var _103=dojo.lang.isString(_ff);if(_103){_ff=_ff.split("");}
if(_102){var step=-1;var i=_ff.length-1;var end=-1;}else{var step=1;var i=0;var end=_ff.length;}
if(_101){while(i!=end){if(_ff[i]===_100){return i;}
i+=step;}}else{while(i!=end){if(_ff[i]==_100){return i;}
i+=step;}}
return -1;};dojo.lang.indexOf=dojo.lang.find;dojo.lang.findLast=function(_107,_108,_109){return dojo.lang.find(_107,_108,_109,true);};dojo.lang.lastIndexOf=dojo.lang.findLast;dojo.lang.inArray=function(_10a,_10b){return dojo.lang.find(_10a,_10b)>-1;};dojo.lang.isObject=function(it){if(typeof it=="undefined"){return false;}
return (typeof it=="object"||it===null||dojo.lang.isArray(it)||dojo.lang.isFunction(it));};dojo.lang.isArray=function(it){return (it&&it instanceof Array||typeof it=="array");};dojo.lang.isArrayLike=function(it){if((!it)||(dojo.lang.isUndefined(it))){return false;}
if(dojo.lang.isString(it)){return false;}
if(dojo.lang.isFunction(it)){return false;}
if(dojo.lang.isArray(it)){return true;}
if((it.tagName)&&(it.tagName.toLowerCase()=="form")){return false;}
if(dojo.lang.isNumber(it.length)&&isFinite(it.length)){return true;}
return false;};dojo.lang.isFunction=function(it){return (it instanceof Function||typeof it=="function");};(function(){if((dojo.render.html.capable)&&(dojo.render.html["safari"])){dojo.lang.isFunction=function(it){if((typeof (it)=="function")&&(it=="[object NodeList]")){return false;}
return (it instanceof Function||typeof it=="function");};}})();dojo.lang.isString=function(it){return (typeof it=="string"||it instanceof String);};dojo.lang.isAlien=function(it){if(!it){return false;}
return !dojo.lang.isFunction(it)&&/\{\s*\[native code\]\s*\}/.test(String(it));};dojo.lang.isBoolean=function(it){return (it instanceof Boolean||typeof it=="boolean");};dojo.lang.isNumber=function(it){return (it instanceof Number||typeof it=="number");};dojo.lang.isUndefined=function(it){return ((typeof (it)=="undefined")&&(it==undefined));};dojo.lang.mixin(dojo.lang,{has:function(obj,name){try{return typeof obj[name]!="undefined";}
catch(e){return false;}},isEmpty:function(obj){if(dojo.lang.isArrayLike(obj)||dojo.lang.isString(obj)){return obj.length===0;}else{if(dojo.lang.isObject(obj)){var tmp={};for(var x in obj){if(obj[x]&&(!tmp[x])){return false;}}
return true;}}
},map:function(arr,obj,_11d){var _11e=dojo.lang.isString(arr);if(_11e){arr=arr.split("");}
if(dojo.lang.isFunction(obj)&&(!_11d)){_11d=obj;obj=dj_global;}else{if(dojo.lang.isFunction(obj)&&_11d){var _11f=obj;obj=_11d;_11d=_11f;}}
if(Array.map){var _120=Array.map(arr,_11d,obj);}else{var _120=[];for(var i=0;i<arr.length;++i){_120.push(_11d.call(obj,arr[i]));}}
if(_11e){return _120.join("");}else{return _120;}},reduce:function(arr,_123,obj,_125){var _126=_123;if(arguments.length==1){dojo.debug("dojo.lang.reduce called with too few arguments!");return false;}else{if(arguments.length==2){_125=_123;_126=arr.shift();}else{if(arguments.lenght==3){if(dojo.lang.isFunction(obj)){_125=obj;obj=null;}}else{if(dojo.lang.isFunction(obj)){var tmp=_125;_125=obj;obj=tmp;}}
}}
var ob=obj?obj:dj_global;dojo.lang.map(arr,function(val){_126=_125.call(ob,_126,val);});return _126;},forEach:function(_12a,_12b,_12c){if(dojo.lang.isString(_12a)){_12a=_12a.split("");}
if(Array.forEach){Array.forEach(_12a,_12b,_12c);}else{if(!_12c){_12c=dj_global;}
for(var i=0,l=_12a.length;i<l;i++){_12b.call(_12c,_12a[i],i,_12a);}}
},_everyOrSome:function(_12f,arr,_131,_132){if(dojo.lang.isString(arr)){arr=arr.split("");}
if(Array.every){return Array[_12f?"every":"some"](arr,_131,_132);}else{if(!_132){_132=dj_global;}
for(var i=0,l=arr.length;i<l;i++){var _135=_131.call(_132,arr[i],i,arr);if(_12f&&!_135){return false;}else{if((!_12f)&&(_135)){return true;}}
}
return Boolean(_12f);}},every:function(arr,_137,_138){return this._everyOrSome(true,arr,_137,_138);},some:function(arr,_13a,_13b){return this._everyOrSome(false,arr,_13a,_13b);},filter:function(arr,_13d,_13e){var _13f=dojo.lang.isString(arr);if(_13f){arr=arr.split("");}
var _140;if(Array.filter){_140=Array.filter(arr,_13d,_13e);}else{if(!_13e){if(arguments.length>=3){dojo.raise("thisObject doesn't exist!");}
_13e=dj_global;}
_140=[];for(var i=0;i<arr.length;i++){if(_13d.call(_13e,arr[i],i,arr)){_140.push(arr[i]);}}
}
if(_13f){return _140.join("");}else{return _140;}},unnest:function(){var out=[];for(var i=0;i<arguments.length;i++){if(dojo.lang.isArrayLike(arguments[i])){var add=dojo.lang.unnest.apply(this,arguments[i]);out=out.concat(add);}else{out.push(arguments[i]);}}
return out;},toArray:function(_145,_146){var _147=[];for(var i=_146||0;i<_145.length;i++){_147.push(_145[i]);}
return _147;}});dojo.lang.setTimeout=function(func,_14a){var _14b=window,_14c=2;if(!dojo.lang.isFunction(func)){_14b=func;func=_14a;_14a=arguments[2];_14c++;}
if(dojo.lang.isString(func)){func=_14b[func];}
var args=[];for(var i=_14c;i<arguments.length;i++){args.push(arguments[i]);}
return dojo.global().setTimeout(function(){func.apply(_14b,args);},_14a);};dojo.lang.clearTimeout=function(_14f){dojo.global().clearTimeout(_14f);};dojo.lang.getNameInObj=function(ns,item){if(!ns){ns=dj_global;}
for(var x in ns){if(ns[x]===item){return new String(x);}}
return null;};dojo.lang.shallowCopy=function(obj,deep){var i,ret;if(obj===null){return null;}
if(dojo.lang.isObject(obj)){ret=new obj.constructor();for(i in obj){if(dojo.lang.isUndefined(ret[i])){ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];}}
}else{if(dojo.lang.isArray(obj)){ret=[];for(i=0;i<obj.length;i++){ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];}}else{ret=obj;}}
return ret;};dojo.lang.firstValued=function(){for(var i=0;i<arguments.length;i++){if(typeof arguments[i]!="undefined"){return arguments[i];}}
return undefined;};dojo.lang.getObjPathValue=function(_158,_159,_15a){dojo.deprecated("dojo.lang.getObjPathValue","use dojo.getObject","0.6");with(dojo.parseObjPath(_158,_159,_15a)){return dojo.evalProp(prop,obj,_15a);}};dojo.lang.setObjPathValue=function(_15b,_15c,_15d,_15e){dojo.deprecated("dojo.lang.setObjPathValue","use dojo.parseObjPath and the '=' operator","0.6");if(arguments.length<4){_15e=true;}
with(dojo.parseObjPath(_15b,_15d,_15e)){if(obj&&(_15e||(prop in obj))){obj[prop]=_15c;}}
};dojo.lang.hitch=function(_15f,_160){var args=[];for(var x=2;x<arguments.length;x++){args.push(arguments[x]);}
var fcn=(dojo.lang.isString(_160)?_15f[_160]:_160)||function(){};return function(){var ta=args.concat([]);for(var x=0;x<arguments.length;x++){ta.push(arguments[x]);}
return fcn.apply(_15f,ta);};};dojo.lang.anonCtr=0;dojo.lang.anon={};dojo.lang.nameAnonFunc=function(_166,_167,_168){var isIE=(dojo.render.html.capable&&dojo.render.html["ie"]);var jpn="$joinpoint";var nso=(_167||dojo.lang.anon);if(isIE){var cn=_166["__dojoNameCache"];if(cn&&nso[cn]===_166){return _166["__dojoNameCache"];}else{if(cn){var _16d=cn.indexOf(jpn);if(_16d!=-1){return cn.substring(0,_16d);}}
}}
if((_168)||((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true))){for(var x in nso){try{if(nso[x]===_166){if(isIE){_166["__dojoNameCache"]=x;var _16d=x.indexOf(jpn);if(_16d!=-1){x=x.substring(0,_16d);}}
return x;}}
catch(e){}}
}
var ret="__"+dojo.lang.anonCtr++;while(typeof nso[ret]!="undefined"){ret="__"+dojo.lang.anonCtr++;}
nso[ret]=_166;return ret;};dojo.lang.forward=function(_170){return function(){return this[_170].apply(this,arguments);};};dojo.lang.curry=function(_171,func){var _173=[];_171=_171||dj_global;if(dojo.lang.isString(func)){func=_171[func];}
for(var x=2;x<arguments.length;x++){_173.push(arguments[x]);}
var _175=(func["__preJoinArity"]||func.length)-_173.length;function gather(_176,_177,_178){var _179=_178;var _17a=_177.slice(0);for(var x=0;x<_176.length;x++){_17a.push(_176[x]);}
_178=_178-_176.length;if(_178<=0){var res=func.apply(_171,_17a);_178=_179;return res;}else{return function(){return gather(arguments,_17a,_178);};}}
return gather([],_173,_175);};dojo.lang.curryArguments=function(_17d,func,args,_180){var _181=[];var x=_180||0;for(x=_180;x<args.length;x++){_181.push(args[x]);}
return dojo.lang.curry.apply(dojo.lang,[_17d,func].concat(_181));};dojo.lang.tryThese=function(){for(var x=0;x<arguments.length;x++){try{if(typeof arguments[x]=="function"){var ret=(arguments[x]());if(ret){return ret;}}
}
catch(e){dojo.debug(e);}}
};dojo.lang.delayThese=function(farr,cb,_187,_188){if(!farr.length){if(typeof _188=="function"){_188();}
return;}
if((typeof _187=="undefined")&&(typeof cb=="number")){_187=cb;cb=function(){};}else{if(!cb){cb=function(){};if(!_187){_187=0;}}
}
setTimeout(function(){(farr.shift())();cb();dojo.lang.delayThese(farr,cb,_187,_188);},_187);};dojo.event=new function(){this._canTimeout=dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);function interpolateArgs(args,_18a){var dl=dojo.lang;var ao={srcObj:dj_global,srcFunc:null,adviceObj:dj_global,adviceFunc:null,aroundObj:null,aroundFunc:null,adviceType:(args.length>2)?args[0]:"after",precedence:"last",once:false,delay:null,rate:0,adviceMsg:false,maxCalls:-1};switch(args.length){case 0:
return;case 1:
return;case 2:
ao.srcFunc=args[0];ao.adviceFunc=args[1];break;case 3:
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isString(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];ao.adviceFunc=args[2];}else{if((dl.isString(args[1]))&&(dl.isString(args[2]))){ao.srcFunc=args[1];ao.adviceFunc=args[2];}else{if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isFunction(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];var _18d=dl.nameAnonFunc(args[2],ao.adviceObj,_18a);ao.adviceFunc=_18d;}else{if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){ao.adviceType="after";ao.srcObj=dj_global;var _18d=dl.nameAnonFunc(args[0],ao.srcObj,_18a);ao.srcFunc=_18d;ao.adviceObj=args[1];ao.adviceFunc=args[2];}}
}}
break;case 4:
if((dl.isObject(args[0]))&&(dl.isObject(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isString(args[1]))&&(dl.isObject(args[2]))){ao.adviceType=args[0];ao.srcObj=dj_global;ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isFunction(args[1]))&&(dl.isObject(args[2]))){ao.adviceType=args[0];ao.srcObj=dj_global;var _18d=dl.nameAnonFunc(args[1],dj_global,_18a);ao.srcFunc=_18d;ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){ao.srcObj=args[1];ao.srcFunc=args[2];var _18d=dl.nameAnonFunc(args[3],dj_global,_18a);ao.adviceObj=dj_global;ao.adviceFunc=_18d;}else{if(dl.isObject(args[1])){ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=dj_global;ao.adviceFunc=args[3];}else{if(dl.isObject(args[2])){ao.srcObj=dj_global;ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{ao.srcObj=ao.adviceObj=ao.aroundObj=dj_global;ao.srcFunc=args[1];ao.adviceFunc=args[2];ao.aroundFunc=args[3];}}
}}
}}
break;case 6:
ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=args[3];ao.adviceFunc=args[4];ao.aroundFunc=args[5];ao.aroundObj=dj_global;break;default:
ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=args[3];ao.adviceFunc=args[4];ao.aroundObj=args[5];ao.aroundFunc=args[6];ao.once=args[7];ao.delay=args[8];ao.rate=args[9];ao.adviceMsg=args[10];ao.maxCalls=(!isNaN(parseInt(args[11])))?args[11]:-1;break;}
if(dl.isFunction(ao.aroundFunc)){var _18d=dl.nameAnonFunc(ao.aroundFunc,ao.aroundObj,_18a);ao.aroundFunc=_18d;}
if(dl.isFunction(ao.srcFunc)){ao.srcFunc=dl.getNameInObj(ao.srcObj,ao.srcFunc);}
if(dl.isFunction(ao.adviceFunc)){ao.adviceFunc=dl.getNameInObj(ao.adviceObj,ao.adviceFunc);}
if((ao.aroundObj)&&(dl.isFunction(ao.aroundFunc))){ao.aroundFunc=dl.getNameInObj(ao.aroundObj,ao.aroundFunc);}
if(!ao.srcObj){dojo.raise("bad srcObj for srcFunc: "+ao.srcFunc);}
if(!ao.adviceObj){dojo.raise("bad adviceObj for adviceFunc: "+ao.adviceFunc);}
if(!ao.adviceFunc){dojo.debug("bad adviceFunc for srcFunc: "+ao.srcFunc);dojo.debugShallow(ao);}
return ao;}
this.connect=function(){if(arguments.length==1){var ao=arguments[0];}else{var ao=interpolateArgs(arguments,true);}
if(dojo.lang.isString(ao.srcFunc)&&(ao.srcFunc.toLowerCase()=="onkey")){if(dojo.render.html.ie){ao.srcFunc="onkeydown";this.connect(ao);}
ao.srcFunc="onkeypress";}
if(dojo.lang.isArray(ao.srcObj)&&ao.srcObj!=""){var _18f={};for(var x in ao){_18f[x]=ao[x];}
var mjps=[];dojo.lang.forEach(ao.srcObj,function(src){if((dojo.render.html.capable)&&(dojo.lang.isString(src))){src=dojo.byId(src);}
_18f.srcObj=src;mjps.push(dojo.event.connect.call(dojo.event,_18f));});return mjps;}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);if(ao.adviceFunc){var mjp2=dojo.event.MethodJoinPoint.getForMethod(ao.adviceObj,ao.adviceFunc);}
mjp.kwAddAdvice(ao);return mjp;};this.log=function(a1,a2){var _197;if((arguments.length==1)&&(typeof a1=="object")){_197=a1;}else{_197={srcObj:a1,srcFunc:a2};}
_197.adviceFunc=function(){var _198=[];for(var x=0;x<arguments.length;x++){_198.push(arguments[x]);}
dojo.debug("("+_197.srcObj+")."+_197.srcFunc,":",_198.join(", "));};this.kwConnect(_197);};this.connectBefore=function(){var args=["before"];for(var i=0;i<arguments.length;i++){args.push(arguments[i]);}
return this.connect.apply(this,args);};this.connectAround=function(){var args=["around"];for(var i=0;i<arguments.length;i++){args.push(arguments[i]);}
return this.connect.apply(this,args);};this.connectOnce=function(){var ao=interpolateArgs(arguments,true);ao.once=true;return this.connect(ao);};this.connectRunOnce=function(){var ao=interpolateArgs(arguments,true);ao.maxCalls=1;return this.connect(ao);};this._kwConnectImpl=function(_1a0,_1a1){var fn=(_1a1)?"disconnect":"connect";if(typeof _1a0["srcFunc"]=="function"){_1a0.srcObj=_1a0["srcObj"]||dj_global;var _1a3=dojo.lang.nameAnonFunc(_1a0.srcFunc,_1a0.srcObj,true);_1a0.srcFunc=_1a3;}
if(typeof _1a0["adviceFunc"]=="function"){_1a0.adviceObj=_1a0["adviceObj"]||dj_global;var _1a3=dojo.lang.nameAnonFunc(_1a0.adviceFunc,_1a0.adviceObj,true);_1a0.adviceFunc=_1a3;}
_1a0.srcObj=_1a0["srcObj"]||dj_global;_1a0.adviceObj=_1a0["adviceObj"]||_1a0["targetObj"]||dj_global;_1a0.adviceFunc=_1a0["adviceFunc"]||_1a0["targetFunc"];return dojo.event[fn](_1a0);};this.kwConnect=function(_1a4){return this._kwConnectImpl(_1a4,false);};this.disconnect=function(){if(arguments.length==1){var ao=arguments[0];}else{var ao=interpolateArgs(arguments,true);}
if(!ao.adviceFunc){return;}
if(dojo.lang.isString(ao.srcFunc)&&(ao.srcFunc.toLowerCase()=="onkey")){if(dojo.render.html.ie){ao.srcFunc="onkeydown";this.disconnect(ao);}
ao.srcFunc="onkeypress";}
if(!ao.srcObj[ao.srcFunc]){return null;}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc,true);mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);return mjp;};this.kwDisconnect=function(_1a7){return this._kwConnectImpl(_1a7,true);};};dojo.event.MethodInvocation=function(_1a8,obj,args){this.jp_=_1a8;this.object=obj;this.args=[];for(var x=0;x<args.length;x++){this.args[x]=args[x];}
this.around_index=-1;};dojo.event.MethodInvocation.prototype.proceed=function(){this.around_index++;if(this.around_index>=this.jp_.around.length){return this.jp_.object[this.jp_.methodname].apply(this.jp_.object,this.args);}else{var ti=this.jp_.around[this.around_index];var mobj=ti[0]||dj_global;var meth=ti[1];return mobj[meth].call(mobj,this);}};dojo.event.MethodJoinPoint=function(obj,_1b0){this.object=obj||dj_global;this.methodname=_1b0;this.methodfunc=this.object[_1b0];};dojo.event.MethodJoinPoint.getForMethod=function(obj,_1b2){if(!obj){obj=dj_global;}
var ofn=obj[_1b2];if(!ofn){ofn=obj[_1b2]=function(){};if(!obj[_1b2]){dojo.raise("Cannot set do-nothing method on that object "+_1b2);}}else{if((typeof ofn!="function")&&(!dojo.lang.isFunction(ofn))&&(!dojo.lang.isAlien(ofn))){return null;}}
var _1b4=_1b2+"$joinpoint";var _1b5=_1b2+"$joinpoint$method";var _1b6=obj[_1b4];if(!_1b6){var _1b7=false;if(dojo.event["browser"]){if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){_1b7=true;dojo.event.browser.addClobberNodeAttrs(obj,[_1b4,_1b5,_1b2]);}}
var _1b8=ofn.length;obj[_1b5]=ofn;_1b6=obj[_1b4]=new dojo.event.MethodJoinPoint(obj,_1b5);if(!_1b7){obj[_1b2]=function(){return _1b6.run.apply(_1b6,arguments);};}else{obj[_1b2]=function(){var args=[];if(!arguments.length){var evt=null;try{if(obj.ownerDocument){evt=obj.ownerDocument.parentWindow.event;}else{if(obj.documentElement){evt=obj.documentElement.ownerDocument.parentWindow.event;}else{if(obj.event){evt=obj.event;}else{evt=window.event;}}
}}
catch(e){evt=window.event;}
if(evt){args.push(dojo.event.browser.fixEvent(evt,this));}}else{for(var x=0;x<arguments.length;x++){if((x==0)&&(dojo.event.browser.isEvent(arguments[x]))){args.push(dojo.event.browser.fixEvent(arguments[x],this));}else{args.push(arguments[x]);}}
}
return _1b6.run.apply(_1b6,args);};}
obj[_1b2].__preJoinArity=_1b8;}
return _1b6;};dojo.lang.extend(dojo.event.MethodJoinPoint,{squelch:false,unintercept:function(){this.object[this.methodname]=this.methodfunc;this.before=[];this.after=[];this.around=[];},disconnect:dojo.lang.forward("unintercept"),run:function(){var obj=this.object||dj_global;var args=arguments;var _1be=[];for(var x=0;x<args.length;x++){_1be[x]=args[x];}
var _1c0=function(marr){if(!marr){dojo.debug("Null argument to unrollAdvice()");return;}
var _1c2=marr[0]||dj_global;var _1c3=marr[1];if(!_1c2[_1c3]){dojo.raise("function \""+_1c3+"\" does not exist on \""+_1c2+"\"");}
var _1c4=marr[2]||dj_global;var _1c5=marr[3];var msg=marr[6];var _1c7=marr[7];if(_1c7>-1){if(_1c7==0){return;}
marr[7]--;}
var _1c8;var to={args:[],jp_:this,object:obj,proceed:function(){return _1c2[_1c3].apply(_1c2,to.args);}};to.args=_1be;var _1ca=parseInt(marr[4]);var _1cb=((!isNaN(_1ca))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));if(marr[5]){var rate=parseInt(marr[5]);var cur=new Date();var _1ce=false;if((marr["last"])&&((cur-marr.last)<=rate)){if(dojo.event._canTimeout){if(marr["delayTimer"]){clearTimeout(marr.delayTimer);}
var tod=parseInt(rate*2);var mcpy=dojo.lang.shallowCopy(marr);marr.delayTimer=setTimeout(function(){mcpy[5]=0;_1c0(mcpy);},tod);}
return;}else{marr.last=cur;}}
if(_1c5){_1c4[_1c5].call(_1c4,to);}else{if((_1cb)&&((dojo.render.html)||(dojo.render.svg))){dj_global["setTimeout"](function(){if(msg){_1c2[_1c3].call(_1c2,to);}else{_1c2[_1c3].apply(_1c2,args);}},_1ca);}else{if(msg){_1c2[_1c3].call(_1c2,to);}else{_1c2[_1c3].apply(_1c2,args);}}
}};var _1d1=function(){if(this.squelch){try{return _1c0.apply(this,arguments);}
catch(e){dojo.debug(e);}}else{return _1c0.apply(this,arguments);}};if((this["before"])&&(this.before.length>0)){dojo.lang.forEach(this.before.concat(new Array()),_1d1);}
var _1d2;try{if((this["around"])&&(this.around.length>0)){var mi=new dojo.event.MethodInvocation(this,obj,args);_1d2=mi.proceed();}else{if(this.methodfunc){_1d2=this.object[this.methodname].apply(this.object,args);}}
}
catch(e){if(!this.squelch){dojo.debug(e,"when calling",this.methodname,"on",this.object,"with arguments",args);dojo.raise(e);}}
if((this["after"])&&(this.after.length>0)){dojo.lang.forEach(this.after.concat(new Array()),_1d1);}
return (this.methodfunc)?_1d2:null;},getArr:function(kind){var type="after";if((typeof kind=="string")&&(kind.indexOf("before")!=-1)){type="before";}else{if(kind=="around"){type="around";}}
if(!this[type]){this[type]=[];}
return this[type];},kwAddAdvice:function(args){this.addAdvice(args["adviceObj"],args["adviceFunc"],args["aroundObj"],args["aroundFunc"],args["adviceType"],args["precedence"],args["once"],args["delay"],args["rate"],args["adviceMsg"],args["maxCalls"]);},addAdvice:function(_1d7,_1d8,_1d9,_1da,_1db,_1dc,once,_1de,rate,_1e0,_1e1){var arr=this.getArr(_1db);if(!arr){dojo.raise("bad this: "+this);}
var ao=[_1d7,_1d8,_1d9,_1da,_1de,rate,_1e0,_1e1];if(once){if(this.hasAdvice(_1d7,_1d8,_1db,arr)>=0){return;}}
if(_1dc=="first"){arr.unshift(ao);}else{arr.push(ao);}},hasAdvice:function(_1e4,_1e5,_1e6,arr){if(!arr){arr=this.getArr(_1e6);}
var ind=-1;for(var x=0;x<arr.length;x++){var aao=(typeof _1e5=="object")?(new String(_1e5)).toString():_1e5;var a1o=(typeof arr[x][1]=="object")?(new String(arr[x][1])).toString():arr[x][1];if((arr[x][0]==_1e4)&&(a1o==aao)){ind=x;}}
return ind;},removeAdvice:function(_1ec,_1ed,_1ee,once){var arr=this.getArr(_1ee);var ind=this.hasAdvice(_1ec,_1ed,_1ee,arr);if(ind==-1){return false;}
while(ind!=-1){arr.splice(ind,1);if(once){break;}
ind=this.hasAdvice(_1ec,_1ed,_1ee,arr);}
return true;}});dojo._ie_clobber=new function(){this.clobberNodes=[];function nukeProp(node,prop){try{node[prop]=null;}
catch(e){}
try{delete node[prop];}
catch(e){}
try{node.removeAttribute(prop);}
catch(e){}}
this.clobber=function(_1f4){var na;var tna;if(_1f4){tna=_1f4.all||_1f4.getElementsByTagName("*");na=[_1f4];for(var x=0;x<tna.length;x++){if(tna[x]["__doClobber__"]){na.push(tna[x]);}}
}else{try{window.onload=null;}
catch(e){}
na=(this.clobberNodes.length)?this.clobberNodes:document.all;}
tna=null;var _1f8={};for(var i=na.length-1;i>=0;i=i-1){var el=na[i];try{if(el&&el["__clobberAttrs__"]){for(var j=0;j<el.__clobberAttrs__.length;j++){nukeProp(el,el.__clobberAttrs__[j]);}
nukeProp(el,"__clobberAttrs__");nukeProp(el,"__doClobber__");}}
catch(e){}}
na=null;};};if(dojo.render.html.ie){dojo.addOnUnload(function(){dojo._ie_clobber.clobber();try{if((dojo["widget"])&&(dojo.widget["manager"])){dojo.widget.manager.destroyAll();}}
catch(e){}
if(dojo.widget){for(var name in dojo.widget._templateCache){if(dojo.widget._templateCache[name].node){dojo.dom.destroyNode(dojo.widget._templateCache[name].node);dojo.widget._templateCache[name].node=null;delete dojo.widget._templateCache[name].node;}}
}
try{window.onload=null;}
catch(e){}
try{window.onunload=null;}
catch(e){}
dojo._ie_clobber.clobberNodes=[];});}
dojo.event.browser=new function(){var _1fd=0;this.normalizedEventName=function(_1fe){switch(_1fe){case "CheckboxStateChange":
case "DOMAttrModified":
case "DOMMenuItemActive":
case "DOMMenuItemInactive":
case "DOMMouseScroll":
case "DOMNodeInserted":
case "DOMNodeRemoved":
case "RadioStateChange":
return _1fe;break;default:
var lcn=_1fe.toLowerCase();return (lcn.indexOf("on")==0)?lcn.substr(2):lcn;break;}};this.clean=function(node){if(dojo.render.html.ie){dojo._ie_clobber.clobber(node);}};this.addClobberNode=function(node){if(!dojo.render.html.ie){return;}
if(!node["__doClobber__"]){node.__doClobber__=true;dojo._ie_clobber.clobberNodes.push(node);node.__clobberAttrs__=[];}};this.addClobberNodeAttrs=function(node,_203){if(!dojo.render.html.ie){return;}
this.addClobberNode(node);for(var x=0;x<_203.length;x++){node.__clobberAttrs__.push(_203[x]);}};this.removeListener=function(node,_206,fp,_208){if(!_208){var _208=false;}
_206=dojo.event.browser.normalizedEventName(_206);if(_206=="key"){if(dojo.render.html.ie){this.removeListener(node,"onkeydown",fp,_208);}
_206="keypress";}
if(node.removeEventListener){node.removeEventListener(_206,fp,_208);}};this.addListener=function(node,_20a,fp,_20c,_20d){if(!node){return;}
if(!_20c){var _20c=false;}
_20a=dojo.event.browser.normalizedEventName(_20a);if(_20a=="key"){if(dojo.render.html.ie){this.addListener(node,"onkeydown",fp,_20c,_20d);}
_20a="keypress";}
if(!_20d){var _20e=function(evt){if(!evt){evt=window.event;}
var ret=fp(dojo.event.browser.fixEvent(evt,this));if(_20c){dojo.event.browser.stopEvent(evt);}
return ret;};}else{_20e=fp;}
if(node.addEventListener){node.addEventListener(_20a,_20e,_20c);return _20e;}else{_20a="on"+_20a;if(typeof node[_20a]=="function"){var _211=node[_20a];node[_20a]=function(e){_211(e);return _20e(e);};}else{node[_20a]=_20e;}
if(dojo.render.html.ie){this.addClobberNodeAttrs(node,[_20a]);}
return _20e;}};this.isEvent=function(obj){return (typeof obj!="undefined")&&(obj)&&(typeof Event!="undefined")&&(obj.eventPhase);};this.currentEvent=null;this.callListener=function(_214,_215){if(typeof _214!="function"){dojo.raise("listener not a function: "+_214);}
dojo.event.browser.currentEvent.currentTarget=_215;return _214.call(_215,dojo.event.browser.currentEvent);};this._stopPropagation=function(){dojo.event.browser.currentEvent.cancelBubble=true;};this._preventDefault=function(){dojo.event.browser.currentEvent.returnValue=false;};this.keys={KEY_BACKSPACE:8,KEY_TAB:9,KEY_CLEAR:12,KEY_ENTER:13,KEY_SHIFT:16,KEY_CTRL:17,KEY_ALT:18,KEY_PAUSE:19,KEY_CAPS_LOCK:20,KEY_ESCAPE:27,KEY_SPACE:32,KEY_PAGE_UP:33,KEY_PAGE_DOWN:34,KEY_END:35,KEY_HOME:36,KEY_LEFT_ARROW:37,KEY_UP_ARROW:38,KEY_RIGHT_ARROW:39,KEY_DOWN_ARROW:40,KEY_INSERT:45,KEY_DELETE:46,KEY_HELP:47,KEY_LEFT_WINDOW:91,KEY_RIGHT_WINDOW:92,KEY_SELECT:93,KEY_NUMPAD_0:96,KEY_NUMPAD_1:97,KEY_NUMPAD_2:98,KEY_NUMPAD_3:99,KEY_NUMPAD_4:100,KEY_NUMPAD_5:101,KEY_NUMPAD_6:102,KEY_NUMPAD_7:103,KEY_NUMPAD_8:104,KEY_NUMPAD_9:105,KEY_NUMPAD_MULTIPLY:106,KEY_NUMPAD_PLUS:107,KEY_NUMPAD_ENTER:108,KEY_NUMPAD_MINUS:109,KEY_NUMPAD_PERIOD:110,KEY_NUMPAD_DIVIDE:111,KEY_F1:112,KEY_F2:113,KEY_F3:114,KEY_F4:115,KEY_F5:116,KEY_F6:117,KEY_F7:118,KEY_F8:119,KEY_F9:120,KEY_F10:121,KEY_F11:122,KEY_F12:123,KEY_F13:124,KEY_F14:125,KEY_F15:126,KEY_NUM_LOCK:144,KEY_SCROLL_LOCK:145};this.revKeys=[];for(var key in this.keys){this.revKeys[this.keys[key]]=key;}
this.fixEvent=function(evt,_218){if(!evt){if(window["event"]){evt=window.event;}}
if((evt["type"])&&(evt["type"].indexOf("key")==0)){evt.keys=this.revKeys;for(var key in this.keys){evt[key]=this.keys[key];}
if(evt["type"]=="keydown"&&dojo.render.html.ie){switch(evt.keyCode){case evt.KEY_SHIFT:
case evt.KEY_CTRL:
case evt.KEY_ALT:
case evt.KEY_CAPS_LOCK:
case evt.KEY_LEFT_WINDOW:
case evt.KEY_RIGHT_WINDOW:
case evt.KEY_SELECT:
case evt.KEY_NUM_LOCK:
case evt.KEY_SCROLL_LOCK:
case evt.KEY_NUMPAD_0:
case evt.KEY_NUMPAD_1:
case evt.KEY_NUMPAD_2:
case evt.KEY_NUMPAD_3:
case evt.KEY_NUMPAD_4:
case evt.KEY_NUMPAD_5:
case evt.KEY_NUMPAD_6:
case evt.KEY_NUMPAD_7:
case evt.KEY_NUMPAD_8:
case evt.KEY_NUMPAD_9:
case evt.KEY_NUMPAD_PERIOD:
break;case evt.KEY_NUMPAD_MULTIPLY:
case evt.KEY_NUMPAD_PLUS:
case evt.KEY_NUMPAD_ENTER:
case evt.KEY_NUMPAD_MINUS:
case evt.KEY_NUMPAD_DIVIDE:
break;case evt.KEY_PAUSE:
case evt.KEY_TAB:
case evt.KEY_BACKSPACE:
case evt.KEY_ENTER:
case evt.KEY_ESCAPE:
case evt.KEY_PAGE_UP:
case evt.KEY_PAGE_DOWN:
case evt.KEY_END:
case evt.KEY_HOME:
case evt.KEY_LEFT_ARROW:
case evt.KEY_UP_ARROW:
case evt.KEY_RIGHT_ARROW:
case evt.KEY_DOWN_ARROW:
case evt.KEY_INSERT:
case evt.KEY_DELETE:
case evt.KEY_F1:
case evt.KEY_F2:
case evt.KEY_F3:
case evt.KEY_F4:
case evt.KEY_F5:
case evt.KEY_F6:
case evt.KEY_F7:
case evt.KEY_F8:
case evt.KEY_F9:
case evt.KEY_F10:
case evt.KEY_F11:
case evt.KEY_F12:
case evt.KEY_F12:
case evt.KEY_F13:
case evt.KEY_F14:
case evt.KEY_F15:
case evt.KEY_CLEAR:
case evt.KEY_HELP:
evt.key=evt.keyCode;break;default:
if(evt.ctrlKey||evt.altKey){var _21a=evt.keyCode;if(_21a>=65&&_21a<=90&&evt.shiftKey==false){_21a+=32;}
if(_21a>=1&&_21a<=26&&evt.ctrlKey){_21a+=96;}
evt.key=String.fromCharCode(_21a);}}
}else{if(evt["type"]=="keypress"){if(dojo.render.html.opera){if(evt.which==0){evt.key=evt.keyCode;}else{if(evt.which>0){switch(evt.which){case evt.KEY_SHIFT:
case evt.KEY_CTRL:
case evt.KEY_ALT:
case evt.KEY_CAPS_LOCK:
case evt.KEY_NUM_LOCK:
case evt.KEY_SCROLL_LOCK:
break;case evt.KEY_PAUSE:
case evt.KEY_TAB:
case evt.KEY_BACKSPACE:
case evt.KEY_ENTER:
case evt.KEY_ESCAPE:
evt.key=evt.which;break;default:
var _21a=evt.which;if((evt.ctrlKey||evt.altKey||evt.metaKey)&&(evt.which>=65&&evt.which<=90&&evt.shiftKey==false)){_21a+=32;}
evt.key=String.fromCharCode(_21a);}}
}}else{if(dojo.render.html.ie){if(!evt.ctrlKey&&!evt.altKey&&evt.keyCode>=evt.KEY_SPACE){evt.key=String.fromCharCode(evt.keyCode);}}else{if(dojo.render.html.safari){switch(evt.keyCode){case 25:
evt.key=evt.KEY_TAB;evt.shift=true;break;case 63232:
evt.key=evt.KEY_UP_ARROW;break;case 63233:
evt.key=evt.KEY_DOWN_ARROW;break;case 63234:
evt.key=evt.KEY_LEFT_ARROW;break;case 63235:
evt.key=evt.KEY_RIGHT_ARROW;break;case 63236:
evt.key=evt.KEY_F1;break;case 63237:
evt.key=evt.KEY_F2;break;case 63238:
evt.key=evt.KEY_F3;break;case 63239:
evt.key=evt.KEY_F4;break;case 63240:
evt.key=evt.KEY_F5;break;case 63241:
evt.key=evt.KEY_F6;break;case 63242:
evt.key=evt.KEY_F7;break;case 63243:
evt.key=evt.KEY_F8;break;case 63244:
evt.key=evt.KEY_F9;break;case 63245:
evt.key=evt.KEY_F10;break;case 63246:
evt.key=evt.KEY_F11;break;case 63247:
evt.key=evt.KEY_F12;break;case 63250:
evt.key=evt.KEY_PAUSE;break;case 63272:
evt.key=evt.KEY_DELETE;break;case 63273:
evt.key=evt.KEY_HOME;break;case 63275:
evt.key=evt.KEY_END;break;case 63276:
evt.key=evt.KEY_PAGE_UP;break;case 63277:
evt.key=evt.KEY_PAGE_DOWN;break;case 63302:
evt.key=evt.KEY_INSERT;break;case 63248:
case 63249:
case 63289:
break;default:
evt.key=evt.charCode>=evt.KEY_SPACE?String.fromCharCode(evt.charCode):evt.keyCode;}}else{evt.key=evt.charCode>0?String.fromCharCode(evt.charCode):evt.keyCode;}}
}}
}}
if(dojo.render.html.ie){if(!evt.target){evt.target=evt.srcElement;}
if(!evt.currentTarget){evt.currentTarget=(_218?_218:evt.srcElement);}
if(!evt.layerX){evt.layerX=evt.offsetX;}
if(!evt.layerY){evt.layerY=evt.offsetY;}
var doc=(evt.srcElement&&evt.srcElement.ownerDocument)?evt.srcElement.ownerDocument:document;var _21c=((dojo.render.html.ie55)||(doc["compatMode"]=="BackCompat"))?doc.body:doc.documentElement;if(!evt.pageX){evt.pageX=evt.clientX+(_21c.scrollLeft||0);}
if(!evt.pageY){evt.pageY=evt.clientY+(_21c.scrollTop||0);}
if(evt.type=="mouseover"){evt.relatedTarget=evt.fromElement;}
if(evt.type=="mouseout"){evt.relatedTarget=evt.toElement;}
this.currentEvent=evt;evt.callListener=this.callListener;evt.stopPropagation=this._stopPropagation;evt.preventDefault=this._preventDefault;}
return evt;};this.stopEvent=function(evt){if(window.event){evt.cancelBubble=true;evt.returnValue=false;}else{evt.preventDefault();evt.stopPropagation();}};};dojo.string.trim=function(str,wh){if(!str.replace){return str;}
if(!str.length){return str;}
var re=(wh>0)?(/^\s+/):(wh<0)?(/\s+$/):(/^\s+|\s+$/g);return str.replace(re,"");};dojo.string.trimStart=function(str){return dojo.string.trim(str,1);};dojo.string.trimEnd=function(str){return dojo.string.trim(str,-1);};dojo.string.repeat=function(str,_224,_225){var out="";for(var i=0;i<_224;i++){out+=str;if(_225&&i<_224-1){out+=_225;}}
return out;};dojo.string.pad=function(str,len,c,dir){var out=String(str);if(!c){c="0";}
if(!dir){dir=1;}
while(out.length<len){if(dir>0){out=c+out;}else{out+=c;}}
return out;};dojo.string.padLeft=function(str,len,c){return dojo.string.pad(str,len,c,1);};dojo.string.padRight=function(str,len,c){return dojo.string.pad(str,len,c,-1);};dojo.io.transports=[];dojo.io.hdlrFuncNames=["load","error","timeout"];dojo.io.Request=function(url,_234,_235,_236){if((arguments.length==1)&&(arguments[0].constructor==Object)){this.fromKwArgs(arguments[0]);}else{this.url=url;if(_234){this.mimetype=_234;}
if(_235){this.transport=_235;}
if(arguments.length>=4){this.changeUrl=_236;}}
};dojo.lang.extend(dojo.io.Request,{url:"",mimetype:"text/plain",method:"GET",content:undefined,transport:undefined,changeUrl:undefined,formNode:undefined,sync:false,bindSuccess:false,useCache:false,preventCache:false,load:function(type,data,_239,_23a){},error:function(type,_23c,_23d,_23e){},timeout:function(type,_240,_241,_242){},handle:function(type,data,_245,_246){},timeoutSeconds:0,abort:function(){},fromKwArgs:function(_247){if(_247["url"]){_247.url=_247.url.toString();}
if(_247["formNode"]){_247.formNode=dojo.byId(_247.formNode);}
if(!_247["method"]&&_247["formNode"]&&_247["formNode"].method){_247.method=_247["formNode"].method;}
if(!_247["handle"]&&_247["handler"]){_247.handle=_247.handler;}
if(!_247["load"]&&_247["loaded"]){_247.load=_247.loaded;}
if(!_247["changeUrl"]&&_247["changeURL"]){_247.changeUrl=_247.changeURL;}
_247.encoding=dojo.lang.firstValued(_247["encoding"],djConfig["bindEncoding"],"");_247.sendTransport=dojo.lang.firstValued(_247["sendTransport"],djConfig["ioSendTransport"],false);var _248=dojo.lang.isFunction;for(var x=0;x<dojo.io.hdlrFuncNames.length;x++){var fn=dojo.io.hdlrFuncNames[x];if(_247[fn]&&_248(_247[fn])){continue;}
if(_247["handle"]&&_248(_247["handle"])){_247[fn]=_247.handle;}}
dojo.lang.mixin(this,_247);}});dojo.io.Error=function(msg,type,num){this.message=msg;this.type=type||"unknown";this.number=num||0;};dojo.io.transports.addTransport=function(name){this.push(name);this[name]=dojo.io[name];};dojo.io.bind=function(_24f){if(!(_24f instanceof dojo.io.Request)){try{_24f=new dojo.io.Request(_24f);}
catch(e){dojo.debug(e);}}
var _250="";if(_24f["transport"]){_250=_24f["transport"];if(!this[_250]){dojo.io.sendBindError(_24f,"No dojo.io.bind() transport with name '"+_24f["transport"]+"'.");return _24f;}
if(!this[_250].canHandle(_24f)){dojo.io.sendBindError(_24f,"dojo.io.bind() transport with name '"+_24f["transport"]+"' cannot handle this type of request.");return _24f;}}else{for(var x=0;x<dojo.io.transports.length;x++){var tmp=dojo.io.transports[x];if((this[tmp])&&(this[tmp].canHandle(_24f))){_250=tmp;break;}}
if(_250==""){dojo.io.sendBindError(_24f,"None of the loaded transports for dojo.io.bind()"+" can handle the request.");return _24f;}}
this[_250].bind(_24f);_24f.bindSuccess=true;return _24f;};dojo.io.sendBindError=function(_253,_254){if((typeof _253.error=="function"||typeof _253.handle=="function")&&(typeof setTimeout=="function"||typeof setTimeout=="object")){var _255=new dojo.io.Error(_254);setTimeout(function(){_253[(typeof _253.error=="function")?"error":"handle"]("error",_255,null,_253);},50);}else{dojo.raise(_254);}};dojo.io.queueBind=function(_256){if(!(_256 instanceof dojo.io.Request)){try{_256=new dojo.io.Request(_256);}
catch(e){dojo.debug(e);}}
var _257=_256.load;_256.load=function(){dojo.io._queueBindInFlight=false;var ret=_257.apply(this,arguments);dojo.io._dispatchNextQueueBind();return ret;};var _259=_256.error;_256.error=function(){dojo.io._queueBindInFlight=false;var ret=_259.apply(this,arguments);dojo.io._dispatchNextQueueBind();return ret;};dojo.io._bindQueue.push(_256);dojo.io._dispatchNextQueueBind();return _256;};dojo.io._dispatchNextQueueBind=function(){if(!dojo.io._queueBindInFlight){dojo.io._queueBindInFlight=true;if(dojo.io._bindQueue.length>0){dojo.io.bind(dojo.io._bindQueue.shift());}else{dojo.io._queueBindInFlight=false;}}
};dojo.io._bindQueue=[];dojo.io._queueBindInFlight=false;dojo.io.argsFromMap=function(map,_25c,last){var enc=/utf/i.test(_25c||"")?encodeURIComponent:dojo.string.encodeAscii;var _25f=[];var _260=new Object();for(var name in map){var _262=function(elt){var val=enc(name)+"="+enc(elt);_25f[(last==name)?"push":"unshift"](val);};if(!_260[name]){var _265=map[name];if(dojo.lang.isArray(_265)){dojo.lang.forEach(_265,_262);}else{_262(_265);}}
}
return _25f.join("&");};dojo.io.setIFrameSrc=function(_266,src,_268){try{var r=dojo.render.html;if(!_268){if(r.safari){_266.location=src;}else{frames[_266.name].location=src;}}else{var idoc;if(r.ie){idoc=_266.contentWindow.document;}else{if(r.safari){idoc=_266.document;}else{idoc=_266.contentWindow;}}
if(!idoc){_266.location=src;return;}else{idoc.location.replace(src);}}
}
catch(e){dojo.debug(e);dojo.debug("setIFrameSrc: "+e);}};dojo.string.substituteParams=function(_26b,hash){var map=(typeof hash=="object")?hash:dojo.lang.toArray(arguments,1);return _26b.replace(/\%\{(\w+)\}/g,function(_26e,key){if(typeof (map[key])!="undefined"&&map[key]!=null){return map[key];}
dojo.raise("Substitution not found: "+key);});};dojo.string.capitalize=function(str){if(!dojo.lang.isString(str)){return "";}
return str.replace(/[^\s]+/g,function(word){return word.substring(0,1).toUpperCase()+word.substring(1);});};dojo.string.isBlank=function(str){if(!dojo.lang.isString(str)){return true;}
return (dojo.string.trim(str).length==0);};dojo.string.encodeAscii=function(str){if(!dojo.lang.isString(str)){return str;}
var ret="";var _275=escape(str);var _276,re=/%u([0-9A-F]{4})/i;while((_276=_275.match(re))){var num=Number("0x"+_276[1]);var _279=escape("&#"+num+";");ret+=_275.substring(0,_276.index)+_279;_275=_275.substring(_276.index+_276[0].length);}
ret+=_275.replace(/\+/g,"%2B");return ret;};dojo.string.escape=function(type,str){var args=dojo.lang.toArray(arguments,1);switch(type.toLowerCase()){case "xml":
case "html":
case "xhtml":
return dojo.string.escapeXml.apply(this,args);case "sql":
return dojo.string.escapeSql.apply(this,args);case "regexp":
case "regex":
return dojo.string.escapeRegExp.apply(this,args);case "javascript":
case "jscript":
case "js":
return dojo.string.escapeJavaScript.apply(this,args);case "ascii":
return dojo.string.encodeAscii.apply(this,args);default:
return str;}};dojo.string.escapeXml=function(str,_27e){str=str.replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");if(!_27e){str=str.replace(/'/gm,"&#39;");}
return str;};dojo.string.escapeSql=function(str){return str.replace(/'/gm,"''");};dojo.string.escapeRegExp=function(str,_281){return str.replace(/([\.$?*!=:|{}\(\)\[\]\\\/^])/g,function(ch){if(_281&&_281.indexOf(ch)!=-1){return ch;}
return "\\"+ch;});};dojo.string.escapeJavaScript=function(str){return str.replace(/(["'\f\b\n\t\r])/gm,"\\$1");};dojo.string.escapeString=function(str){return ("\""+str.replace(/(["\\])/g,"\\$1")+"\"").replace(/[\f]/g,"\\f").replace(/[\b]/g,"\\b").replace(/[\n]/g,"\\n").replace(/[\t]/g,"\\t").replace(/[\r]/g,"\\r");};dojo.string.summary=function(str,len){if(!len||str.length<=len){return str;}
return str.substring(0,len).replace(/\.+$/,"")+"...";};dojo.string.endsWith=function(str,end,_289){if(_289){str=str.toLowerCase();end=end.toLowerCase();}
if((str.length-end.length)<0){return false;}
return str.lastIndexOf(end)==str.length-end.length;};dojo.string.endsWithAny=function(str){for(var i=1;i<arguments.length;i++){if(dojo.string.endsWith(str,arguments[i])){return true;}}
return false;};dojo.string.startsWith=function(str,_28d,_28e){if(_28e){str=str.toLowerCase();_28d=_28d.toLowerCase();}
return str.indexOf(_28d)==0;};dojo.string.startsWithAny=function(str){for(var i=1;i<arguments.length;i++){if(dojo.string.startsWith(str,arguments[i])){return true;}}
return false;};dojo.string.has=function(str){for(var i=1;i<arguments.length;i++){if(str.indexOf(arguments[i])>-1){return true;}}
return false;};dojo.string.normalizeNewlines=function(text,_294){if(_294=="\n"){text=text.replace(/\r\n/g,"\n");text=text.replace(/\r/g,"\n");}else{if(_294=="\r"){text=text.replace(/\r\n/g,"\r");text=text.replace(/\n/g,"\r");}else{text=text.replace(/([^\r])\n/g,"$1\r\n").replace(/\r([^\n])/g,"\r\n$1");}}
return text;};dojo.string.splitEscaped=function(str,_296){var _297=[];for(var i=0,_299=0;i<str.length;i++){if(str.charAt(i)=="\\"){i++;continue;}
if(str.charAt(i)==_296){_297.push(str.substring(_299,i));_299=i+1;}}
_297.push(str.substr(_299));return _297;};dojo.dom.ELEMENT_NODE=1;dojo.dom.ATTRIBUTE_NODE=2;dojo.dom.TEXT_NODE=3;dojo.dom.CDATA_SECTION_NODE=4;dojo.dom.ENTITY_REFERENCE_NODE=5;dojo.dom.ENTITY_NODE=6;dojo.dom.PROCESSING_INSTRUCTION_NODE=7;dojo.dom.COMMENT_NODE=8;dojo.dom.DOCUMENT_NODE=9;dojo.dom.DOCUMENT_TYPE_NODE=10;dojo.dom.DOCUMENT_FRAGMENT_NODE=11;dojo.dom.NOTATION_NODE=12;dojo.dom.dojoml="http://www.dojotoolkit.org/2004/dojoml";dojo.dom.xmlns={svg:"http://www.w3.org/2000/svg",smil:"http://www.w3.org/2001/SMIL20/",mml:"http://www.w3.org/1998/Math/MathML",cml:"http://www.xml-cml.org",xlink:"http://www.w3.org/1999/xlink",xhtml:"http://www.w3.org/1999/xhtml",xul:"http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul",xbl:"http://www.mozilla.org/xbl",fo:"http://www.w3.org/1999/XSL/Format",xsl:"http://www.w3.org/1999/XSL/Transform",xslt:"http://www.w3.org/1999/XSL/Transform",xi:"http://www.w3.org/2001/XInclude",xforms:"http://www.w3.org/2002/01/xforms",saxon:"http://icl.com/saxon",xalan:"http://xml.apache.org/xslt",xsd:"http://www.w3.org/2001/XMLSchema",dt:"http://www.w3.org/2001/XMLSchema-datatypes",xsi:"http://www.w3.org/2001/XMLSchema-instance",rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#",rdfs:"http://www.w3.org/2000/01/rdf-schema#",dc:"http://purl.org/dc/elements/1.1/",dcq:"http://purl.org/dc/qualifiers/1.0","soap-env":"http://schemas.xmlsoap.org/soap/envelope/",wsdl:"http://schemas.xmlsoap.org/wsdl/",AdobeExtensions:"http://ns.adobe.com/AdobeSVGViewerExtensions/3.0/"};dojo.dom.isNode=function(wh){if(typeof Element=="function"){try{return wh instanceof Element;}
catch(e){}}else{return wh&&!isNaN(wh.nodeType);}};dojo.dom.getUniqueId=function(){var _29b=dojo.doc();do{var id="dj_unique_"+(++arguments.callee._idIncrement);}while(_29b.getElementById(id));return id;};dojo.dom.getUniqueId._idIncrement=0;dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_29d,_29e){var node=_29d.firstChild;while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){node=node.nextSibling;}
if(_29e&&node&&node.tagName&&node.tagName.toLowerCase()!=_29e.toLowerCase()){node=dojo.dom.nextElement(node,_29e);}
return node;};dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_2a0,_2a1){var node=_2a0.lastChild;while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){node=node.previousSibling;}
if(_2a1&&node&&node.tagName&&node.tagName.toLowerCase()!=_2a1.toLowerCase()){node=dojo.dom.prevElement(node,_2a1);}
return node;};dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(node,_2a4){if(!node){return null;}
do{node=node.nextSibling;}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);if(node&&_2a4&&_2a4.toLowerCase()!=node.tagName.toLowerCase()){return dojo.dom.nextElement(node,_2a4);}
return node;};dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(node,_2a6){if(!node){return null;}
if(_2a6){_2a6=_2a6.toLowerCase();}
do{node=node.previousSibling;}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);if(node&&_2a6&&_2a6.toLowerCase()!=node.tagName.toLowerCase()){return dojo.dom.prevElement(node,_2a6);}
return node;};dojo.dom.moveChildren=function(_2a7,_2a8,trim){var _2aa=0;if(trim){while(_2a7.hasChildNodes()&&_2a7.firstChild.nodeType==dojo.dom.TEXT_NODE){_2a7.removeChild(_2a7.firstChild);}
while(_2a7.hasChildNodes()&&_2a7.lastChild.nodeType==dojo.dom.TEXT_NODE){_2a7.removeChild(_2a7.lastChild);}}
while(_2a7.hasChildNodes()){_2a8.appendChild(_2a7.firstChild);_2aa++;}
return _2aa;};dojo.dom.copyChildren=function(_2ab,_2ac,trim){var _2ae=_2ab.cloneNode(true);return this.moveChildren(_2ae,_2ac,trim);};dojo.dom.replaceChildren=function(node,_2b0){var _2b1=[];if(dojo.render.html.ie){for(var i=0;i<node.childNodes.length;i++){_2b1.push(node.childNodes[i]);}}
dojo.dom.removeChildren(node);node.appendChild(_2b0);for(var i=0;i<_2b1.length;i++){dojo.dom.destroyNode(_2b1[i]);}};dojo.dom.removeChildren=function(node){var _2b4=node.childNodes.length;while(node.hasChildNodes()){dojo.dom.removeNode(node.firstChild);}
return _2b4;};dojo.dom.replaceNode=function(node,_2b6){return node.parentNode.replaceChild(_2b6,node);};dojo.dom.destroyNode=function(node){if(node.parentNode){node=dojo.dom.removeNode(node);}
if(node.nodeType!=3){if(dojo.exists("dojo.event.browser.clean")){dojo.event.browser.clean(node);}
if(dojo.render.html.ie){node.outerHTML="";}}
};dojo.dom.removeNode=function(node){if(node&&node.parentNode){return node.parentNode.removeChild(node);}};dojo.dom.getAncestors=function(node,_2ba,_2bb){var _2bc=[];var _2bd=(_2ba&&(_2ba instanceof Function||typeof _2ba=="function"));while(node){if(!_2bd||_2ba(node)){_2bc.push(node);}
if(_2bb&&_2bc.length>0){return _2bc[0];}
node=node.parentNode;}
if(_2bb){return null;}
return _2bc;};dojo.dom.getAncestorsByTag=function(node,tag,_2c0){tag=tag.toLowerCase();return dojo.dom.getAncestors(node,function(el){return ((el.tagName)&&(el.tagName.toLowerCase()==tag));},_2c0);};dojo.dom.getFirstAncestorByTag=function(node,tag){return dojo.dom.getAncestorsByTag(node,tag,true);};dojo.dom.isDescendantOf=function(node,_2c5,_2c6){if(_2c6&&node){node=node.parentNode;}
while(node){if(node==_2c5){return true;}
node=node.parentNode;}
return false;};dojo.dom.innerXML=function(node){if(node.innerXML){return node.innerXML;}else{if(node.xml){return node.xml;}else{if(typeof XMLSerializer!="undefined"){return (new XMLSerializer()).serializeToString(node);}}
}};dojo.dom.createDocument=function(){var doc=null;var _2c9=dojo.doc();if(!dj_undef("ActiveXObject")){var _2ca=["MSXML2","Microsoft","MSXML","MSXML3"];for(var i=0;i<_2ca.length;i++){try{doc=new ActiveXObject(_2ca[i]+".XMLDOM");}
catch(e){}
if(doc){break;}}
}else{if((_2c9.implementation)&&(_2c9.implementation.createDocument)){doc=_2c9.implementation.createDocument("","",null);}}
return doc;};dojo.dom.createDocumentFromText=function(str,_2cd){if(!_2cd){_2cd="text/xml";}
if(!dj_undef("DOMParser")){var _2ce=new DOMParser();return _2ce.parseFromString(str,_2cd);}else{if(!dj_undef("ActiveXObject")){var _2cf=dojo.dom.createDocument();if(_2cf){_2cf.async=false;_2cf.loadXML(str);return _2cf;}else{dojo.debug("toXml didn't work?");}}else{var _2d0=dojo.doc();if(_2d0.createElement){var tmp=_2d0.createElement("xml");tmp.innerHTML=str;if(_2d0.implementation&&_2d0.implementation.createDocument){var _2d2=_2d0.implementation.createDocument("foo","",null);for(var i=0;i<tmp.childNodes.length;i++){_2d2.importNode(tmp.childNodes.item(i),true);}
return _2d2;}
return ((tmp.document)&&(tmp.document.firstChild?tmp.document.firstChild:tmp));}}
}
return null;};dojo.dom.prependChild=function(node,_2d5){if(_2d5.firstChild){_2d5.insertBefore(node,_2d5.firstChild);}else{_2d5.appendChild(node);}
return true;};dojo.dom.insertBefore=function(node,ref,_2d8){if((_2d8!=true)&&(node===ref||node.nextSibling===ref)){return false;}
var _2d9=ref.parentNode;_2d9.insertBefore(node,ref);return true;};dojo.dom.insertAfter=function(node,ref,_2dc){var pn=ref.parentNode;if(ref==pn.lastChild){if((_2dc!=true)&&(node===ref)){return false;}
pn.appendChild(node);}else{return this.insertBefore(node,ref.nextSibling,_2dc);}
return true;};dojo.dom.insertAtPosition=function(node,ref,_2e0){if((!node)||(!ref)||(!_2e0)){return false;}
switch(_2e0.toLowerCase()){case "before":
return dojo.dom.insertBefore(node,ref);case "after":
return dojo.dom.insertAfter(node,ref);case "first":
if(ref.firstChild){return dojo.dom.insertBefore(node,ref.firstChild);}else{ref.appendChild(node);return true;}
break;default:
ref.appendChild(node);return true;}};dojo.dom.insertAtIndex=function(node,_2e2,_2e3){var _2e4=_2e2.childNodes;if(!_2e4.length||_2e4.length==_2e3){_2e2.appendChild(node);return true;}
if(_2e3==0){return dojo.dom.prependChild(node,_2e2);}
return dojo.dom.insertAfter(node,_2e4[_2e3-1]);};dojo.dom.textContent=function(node,text){if(arguments.length>1){var _2e7=dojo.doc();dojo.dom.replaceChildren(node,_2e7.createTextNode(text));return text;}else{if(node.textContent!=undefined){return node.textContent;}
var _2e8="";if(node==null){return _2e8;}
for(var i=0;i<node.childNodes.length;i++){switch(node.childNodes[i].nodeType){case 1:
case 5:
_2e8+=dojo.dom.textContent(node.childNodes[i]);break;case 3:
case 2:
case 4:
_2e8+=node.childNodes[i].nodeValue;break;default:
break;}}
return _2e8;}};dojo.dom.hasParent=function(node){return Boolean(node&&node.parentNode&&dojo.dom.isNode(node.parentNode));};dojo.dom.isTag=function(node){if(node&&node.tagName){for(var i=1;i<arguments.length;i++){if(node.tagName==String(arguments[i])){return String(arguments[i]);}}
}
return "";};dojo.dom.setAttributeNS=function(elem,_2ee,_2ef,_2f0){if(elem==null||((elem==undefined)&&(typeof elem=="undefined"))){dojo.raise("No element given to dojo.dom.setAttributeNS");}
if(!((elem.setAttributeNS==undefined)&&(typeof elem.setAttributeNS=="undefined"))){elem.setAttributeNS(_2ee,_2ef,_2f0);}else{var _2f1=elem.ownerDocument;var _2f2=_2f1.createNode(2,_2ef,_2ee);_2f2.nodeValue=_2f0;elem.setAttributeNode(_2f2);}};try{if((!djConfig["preventBackButtonFix"])&&(!dojo.hostenv.post_load_)){document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(djConfig["dojoIframeHistoryUrl"]||dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"'></iframe>");}}
catch(e){}
if(dojo.render.html.opera){dojo.debug("Opera is not supported with dojo.undo.browser, so back/forward detection will not work.");}
dojo.undo.browser={initialHref:(!dj_undef("window"))?window.location.href:"",initialHash:(!dj_undef("window"))?window.location.hash:"",moveForward:false,historyStack:[],forwardStack:[],historyIframe:null,bookmarkAnchor:null,locationTimer:null,setInitialState:function(args){this.initialState=this._createState(this.initialHref,args,this.initialHash);},addToHistory:function(args){this.forwardStack=[];var hash=null;var url=null;if(!this.historyIframe){if(djConfig["useXDomain"]&&!djConfig["dojoIframeHistoryUrl"]){dojo.debug("dojo.undo.browser: When using cross-domain Dojo builds,"+" please save iframe_history.html to your domain and set djConfig.dojoIframeHistoryUrl"+" to the path on your domain to iframe_history.html");}
this.historyIframe=window.frames["djhistory"];}
if(!this.bookmarkAnchor){this.bookmarkAnchor=document.createElement("a");dojo.body().appendChild(this.bookmarkAnchor);this.bookmarkAnchor.style.display="none";}
if(args["changeUrl"]){hash="#"+((args["changeUrl"]!==true)?args["changeUrl"]:(new Date()).getTime());if(this.historyStack.length==0&&this.initialState.urlHash==hash){this.initialState=this._createState(url,args,hash);return;}else{if(this.historyStack.length>0&&this.historyStack[this.historyStack.length-1].urlHash==hash){this.historyStack[this.historyStack.length-1]=this._createState(url,args,hash);return;}}
this.changingUrl=true;setTimeout("window.location.href = '"+hash+"'; dojo.undo.browser.changingUrl = false;",1);this.bookmarkAnchor.href=hash;if(dojo.render.html.ie){url=this._loadIframeHistory();var _2f7=args["back"]||args["backButton"]||args["handle"];var tcb=function(_2f9){if(window.location.hash!=""){setTimeout("window.location.href = '"+hash+"';",1);}
_2f7.apply(this,[_2f9]);};if(args["back"]){args.back=tcb;}else{if(args["backButton"]){args.backButton=tcb;}else{if(args["handle"]){args.handle=tcb;}}
}
var _2fa=args["forward"]||args["forwardButton"]||args["handle"];var tfw=function(_2fc){if(window.location.hash!=""){window.location.href=hash;}
if(_2fa){_2fa.apply(this,[_2fc]);}};if(args["forward"]){args.forward=tfw;}else{if(args["forwardButton"]){args.forwardButton=tfw;}else{if(args["handle"]){args.handle=tfw;}}
}}else{if(dojo.render.html.moz){if(!this.locationTimer){this.locationTimer=setInterval("dojo.undo.browser.checkLocation();",200);}}
}}else{url=this._loadIframeHistory();}
this.historyStack.push(this._createState(url,args,hash));},checkLocation:function(){if(!this.changingUrl){var hsl=this.historyStack.length;if((window.location.hash==this.initialHash||window.location.href==this.initialHref)&&(hsl==1)){this.handleBackButton();return;}
if(this.forwardStack.length>0){if(this.forwardStack[this.forwardStack.length-1].urlHash==window.location.hash){this.handleForwardButton();return;}}
if((hsl>=2)&&(this.historyStack[hsl-2])){if(this.historyStack[hsl-2].urlHash==window.location.hash){this.handleBackButton();return;}}
}},iframeLoaded:function(evt,_2ff){if(!dojo.render.html.opera){var _300=this._getUrlQuery(_2ff.href);if(_300==null){if(this.historyStack.length==1){this.handleBackButton();}
return;}
if(this.moveForward){this.moveForward=false;return;}
if(this.historyStack.length>=2&&_300==this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){this.handleBackButton();}else{if(this.forwardStack.length>0&&_300==this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){this.handleForwardButton();}}
}},handleBackButton:function(){var _301=this.historyStack.pop();if(!_301){return;}
var last=this.historyStack[this.historyStack.length-1];if(!last&&this.historyStack.length==0){last=this.initialState;}
if(last){if(last.kwArgs["back"]){last.kwArgs["back"]();}else{if(last.kwArgs["backButton"]){last.kwArgs["backButton"]();}else{if(last.kwArgs["handle"]){last.kwArgs.handle("back");}}
}}
this.forwardStack.push(_301);},handleForwardButton:function(){var last=this.forwardStack.pop();if(!last){return;}
if(last.kwArgs["forward"]){last.kwArgs.forward();}else{if(last.kwArgs["forwardButton"]){last.kwArgs.forwardButton();}else{if(last.kwArgs["handle"]){last.kwArgs.handle("forward");}}
}
this.historyStack.push(last);},_createState:function(url,args,hash){return {"url":url,"kwArgs":args,"urlHash":hash};},_getUrlQuery:function(url){var _308=url.split("?");if(_308.length<2){return null;}else{return _308[1];}},_loadIframeHistory:function(){var url=(djConfig["dojoIframeHistoryUrl"]||dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"?"+(new Date()).getTime();this.moveForward=true;dojo.io.setIFrameSrc(this.historyIframe,url,false);return url;}};if(!dj_undef("window")){dojo.io.checkChildrenForFile=function(node){var _30b=false;var _30c=node.getElementsByTagName("input");dojo.lang.forEach(_30c,function(_30d){if(_30b){return;}
if(_30d.getAttribute("type")=="file"){_30b=true;}});return _30b;};dojo.io.formHasFile=function(_30e){return dojo.io.checkChildrenForFile(_30e);};dojo.io.updateNode=function(node,_310){node=dojo.byId(node);var args=_310;if(dojo.lang.isString(_310)){args={url:_310};}
args.mimetype="text/html";args.load=function(t,d,e){while(node.firstChild){dojo.dom.destroyNode(node.firstChild);}
node.innerHTML=d;};dojo.io.bind(args);};dojo.io.formFilter=function(node){var type=(node.type||"").toLowerCase();return !node.disabled&&node.name&&!dojo.lang.inArray(["file","submit","image","reset","button"],type);};dojo.io.encodeForm=function(_317,_318,_319){if((!_317)||(!_317.tagName)||(!_317.tagName.toLowerCase()=="form")){dojo.raise("Attempted to encode a non-form element.");}
if(!_319){_319=dojo.io.formFilter;}
var enc=/utf/i.test(_318||"")?encodeURIComponent:dojo.string.encodeAscii;var _31b=[];for(var i=0;i<_317.elements.length;i++){var elm=_317.elements[i];if(!elm||elm.tagName.toLowerCase()=="fieldset"||!_319(elm)){continue;}
var name=enc(elm.name);var type=elm.type.toLowerCase();if(type=="select-multiple"){for(var j=0;j<elm.options.length;j++){if(elm.options[j].selected){_31b.push(name+"="+enc(elm.options[j].value));}}
}else{if(dojo.lang.inArray(["radio","checkbox"],type)){if(elm.checked){_31b.push(name+"="+enc(elm.value));}}else{_31b.push(name+"="+enc(elm.value));}}
}
var _321=_317.getElementsByTagName("input");for(var i=0;i<_321.length;i++){var _322=_321[i];if(_322.type.toLowerCase()=="image"&&_322.form==_317&&_319(_322)){var name=enc(_322.name);_31b.push(name+"="+enc(_322.value));_31b.push(name+".x=0");_31b.push(name+".y=0");}}
return _31b.join("&")+"&";};dojo.io.FormBind=function(args){this.bindArgs={};if(args&&args.formNode){this.init(args);}else{if(args){this.init({formNode:args});}}
};dojo.lang.extend(dojo.io.FormBind,{form:null,bindArgs:null,clickedButton:null,init:function(args){var form=dojo.byId(args.formNode);if(!form||!form.tagName||form.tagName.toLowerCase()!="form"){throw new Error("FormBind: Couldn't apply, invalid form");}else{if(this.form==form){return;}else{if(this.form){throw new Error("FormBind: Already applied to a form");}}
}
dojo.lang.mixin(this.bindArgs,args);this.form=form;this.connect(form,"onsubmit","submit");for(var i=0;i<form.elements.length;i++){var node=form.elements[i];if(node&&node.type&&dojo.lang.inArray(["submit","button"],node.type.toLowerCase())){this.connect(node,"onclick","click");}}
var _328=form.getElementsByTagName("input");for(var i=0;i<_328.length;i++){var _329=_328[i];if(_329.type.toLowerCase()=="image"&&_329.form==form){this.connect(_329,"onclick","click");}}
},onSubmit:function(form){return true;},submit:function(e){e.preventDefault();if(this.onSubmit(this.form)){dojo.io.bind(dojo.lang.mixin(this.bindArgs,{formFilter:dojo.lang.hitch(this,"formFilter")}));}},click:function(e){var node=e.currentTarget;if(node.disabled){return;}
this.clickedButton=node;},formFilter:function(node){var type=(node.type||"").toLowerCase();var _330=false;if(node.disabled||!node.name){_330=false;}else{if(dojo.lang.inArray(["submit","button","image"],type)){if(!this.clickedButton){this.clickedButton=node;}
_330=node==this.clickedButton;}else{_330=!dojo.lang.inArray(["file","submit","reset","button"],type);}}
return _330;},connect:function(_331,_332,_333){if(dojo.getObject("dojo.event.connect")){dojo.event.connect(_331,_332,this,_333);}else{var fcn=dojo.lang.hitch(this,_333);_331[_332]=function(e){if(!e){e=window.event;}
if(!e.currentTarget){e.currentTarget=e.srcElement;}
if(!e.preventDefault){e.preventDefault=function(){window.event.returnValue=false;};}
fcn(e);};}}});dojo.io.XMLHTTPTransport=new function(){var _336=this;var _337={};this.useCache=false;this.preventCache=false;function getCacheKey(url,_339,_33a){return url+"|"+_339+"|"+_33a.toLowerCase();}
function addToCache(url,_33c,_33d,http){_337[getCacheKey(url,_33c,_33d)]=http;}
function getFromCache(url,_340,_341){return _337[getCacheKey(url,_340,_341)];}
this.clearCache=function(){_337={};};function doLoad(_342,http,url,_345,_346){if(((http.status>=200)&&(http.status<300))||(http.status==304)||(location.protocol=="file:"&&(http.status==0||http.status==undefined))||(location.protocol=="chrome:"&&(http.status==0||http.status==undefined))){var ret;if(_342.method.toLowerCase()=="head"){var _348=http.getAllResponseHeaders();ret={};ret.toString=function(){return _348;};var _349=_348.split(/[\r\n]+/g);for(var i=0;i<_349.length;i++){var pair=_349[i].match(/^([^:]+)\s*:\s*(.+)$/i);if(pair){ret[pair[1]]=pair[2];}}
}else{if(_342.mimetype=="text/javascript"){try{ret=dj_eval(http.responseText);}
catch(e){dojo.debug(e);dojo.debug(http.responseText);ret=null;}}else{if(_342.mimetype=="text/json"||_342.mimetype=="application/json"){try{ret=dj_eval("("+http.responseText+")");}
catch(e){dojo.debug(e);dojo.debug(http.responseText);ret=false;}}else{if((_342.mimetype=="application/xml")||(_342.mimetype=="text/xml")){ret=http.responseXML;if(!ret||typeof ret=="string"||!http.getResponseHeader("Content-Type")){ret=dojo.dom.createDocumentFromText(http.responseText);}}else{ret=http.responseText;}}
}}
if(_346){addToCache(url,_345,_342.method,http);}
_342[(typeof _342.load=="function")?"load":"handle"]("load",ret,http,_342);}else{var _34c=new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);_342[(typeof _342.error=="function")?"error":"handle"]("error",_34c,http,_342);}}
function setHeaders(http,_34e){if(_34e["headers"]){for(var _34f in _34e["headers"]){if(_34f.toLowerCase()=="content-type"&&!_34e["contentType"]){_34e["contentType"]=_34e["headers"][_34f];}else{http.setRequestHeader(_34f,_34e["headers"][_34f]);}}
}}
this.inFlight=[];this.inFlightTimer=null;this.startWatchingInFlight=function(){if(!this.inFlightTimer){this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);}};this.watchInFlight=function(){var now=null;if(!dojo.hostenv._blockAsync&&!_336._blockAsync){for(var x=this.inFlight.length-1;x>=0;x--){try{var tif=this.inFlight[x];if(!tif||tif.http._aborted||!tif.http.readyState){this.inFlight.splice(x,1);continue;}
if(4==tif.http.readyState){this.inFlight.splice(x,1);doLoad(tif.req,tif.http,tif.url,tif.query,tif.useCache);}else{if(tif.startTime){if(!now){now=(new Date()).getTime();}
if(tif.startTime+(tif.req.timeoutSeconds*1000)<now){if(typeof tif.http.abort=="function"){tif.http.abort();}
this.inFlight.splice(x,1);tif.req[(typeof tif.req.timeout=="function")?"timeout":"handle"]("timeout",null,tif.http,tif.req);}}
}}
catch(e){try{var _353=new dojo.io.Error("XMLHttpTransport.watchInFlight Error: "+e);tif.req[(typeof tif.req.error=="function")?"error":"handle"]("error",_353,tif.http,tif.req);}
catch(e2){dojo.debug("XMLHttpTransport error callback failed: "+e2);}}
}}
clearTimeout(this.inFlightTimer);if(this.inFlight.length==0){this.inFlightTimer=null;return;}
this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);};var _354=dojo.hostenv.getXmlhttpObject()?true:false;this.canHandle=function(_355){return _354&&dojo.lang.inArray(["text/plain","text/html","application/xml","text/xml","text/javascript","text/json","application/json"],(_355["mimetype"].toLowerCase()||""))&&!(_355["formNode"]&&dojo.io.formHasFile(_355["formNode"]));};this.multipartBoundary="45309FFF-BD65-4d50-99C9-36986896A96F";this.bind=function(_356){var url=_356.url;var _358="";if(_356["formNode"]){var ta=_356.formNode.getAttribute("action");if(typeof (ta)!="string"){ta=_356.formNode.attributes.action.value;}
if((ta)&&(!_356["url"])){url=ta;}
var tp=_356.formNode.getAttribute("method");if((tp)&&(!_356["method"])){_356.method=tp;}
_358+=dojo.io.encodeForm(_356.formNode,_356.encoding,_356["formFilter"]);}
if(url.indexOf("#")>-1){dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);url=url.split("#")[0];}
if(_356["file"]){_356.method="post";}
if(!_356["method"]){_356.method="get";}
if(_356.method.toLowerCase()=="get"){_356.multipart=false;}else{if(_356["file"]){_356.multipart=true;}else{if(!_356["multipart"]){_356.multipart=false;}}
}
if(_356["backButton"]||_356["back"]||_356["changeUrl"]){dojo.undo.browser.addToHistory(_356);}
var _35b=_356["content"]||{};if(_356.sendTransport){_35b["dojo.transport"]="xmlhttp";}
do{if(_356.postContent){_358=_356.postContent;break;}
if(_35b){_358+=dojo.io.argsFromMap(_35b,_356.encoding);}
if(_356.method.toLowerCase()=="get"||!_356.multipart){break;}
var t=[];if(_358.length){var q=_358.split("&");for(var i=0;i<q.length;++i){if(q[i].length){var p=q[i].split("=");t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+p[0]+"\"","",p[1]);}}
}
if(_356.file){if(dojo.lang.isArray(_356.file)){for(var i=0;i<_356.file.length;++i){var o=_356.file[i];t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);}}else{var o=_356.file;t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);}}
if(t.length){t.push("--"+this.multipartBoundary+"--","");_358=t.join("\r\n");}}while(false);var _361=_356["sync"]?false:true;var _362=_356["preventCache"]||(this.preventCache==true&&_356["preventCache"]!=false);var _363=_356["useCache"]==true||(this.useCache==true&&_356["useCache"]!=false);if(!_362&&_363){var _364=getFromCache(url,_358,_356.method);if(_364){doLoad(_356,_364,url,_358,false);return;}}
var http=dojo.hostenv.getXmlhttpObject(_356);var _366=false;if(_361){var _367=this.inFlight.push({"req":_356,"http":http,"url":url,"query":_358,"useCache":_363,"startTime":_356.timeoutSeconds?(new Date()).getTime():0});this.startWatchingInFlight();}else{_336._blockAsync=true;}
if(_356.method.toLowerCase()=="post"){if(!_356.user){http.open("POST",url,_361);}else{http.open("POST",url,_361,_356.user,_356.password);}
setHeaders(http,_356);http.setRequestHeader("Content-Type",_356.multipart?("multipart/form-data; boundary="+this.multipartBoundary):(_356.contentType||"application/x-www-form-urlencoded"));try{http.send(_358);}
catch(e){if(typeof http.abort=="function"){http.abort();}
doLoad(_356,{status:404},url,_358,_363);}}else{var _368=url;if(_358!=""){_368+=(_368.indexOf("?")>-1?"&":"?")+_358;}
if(_362){_368+=(dojo.string.endsWithAny(_368,"?","&")?"":(_368.indexOf("?")>-1?"&":"?"))+"dojo.preventCache="+new Date().valueOf();}
if(!_356.user){http.open(_356.method.toUpperCase(),_368,_361);}else{http.open(_356.method.toUpperCase(),_368,_361,_356.user,_356.password);}
setHeaders(http,_356);try{http.send(null);}
catch(e){if(typeof http.abort=="function"){http.abort();}
doLoad(_356,{status:404},url,_358,_363);}}
if(!_361){doLoad(_356,http,url,_358,_363);_336._blockAsync=false;}
_356.abort=function(){try{http._aborted=true;}
catch(e){}
return http.abort();};return;};dojo.io.transports.addTransport("XMLHTTPTransport");};}
dojo.xml.Parse=function(){var isIE=((dojo.render.html.capable)&&(dojo.render.html.ie));function getTagName(node){try{return node.tagName.toLowerCase();}
catch(e){return "";}}
function getDojoTagName(node){var _36c=getTagName(node);if(!_36c){return "";}
if((dojo.widget)&&(dojo.widget.tags[_36c])){return _36c;}
var p=_36c.indexOf(":");if(p>=0){return _36c;}
if(_36c.substr(0,5)=="dojo:"){return _36c;}
if(dojo.render.html.capable&&dojo.render.html.ie&&node.scopeName&&node.scopeName!="HTML"){return node.scopeName.toLowerCase()+":"+_36c;}
if(_36c.substr(0,4)=="dojo"){return "dojo:"+_36c.substring(4);}
var djt=node.getAttribute("dojoType")||node.getAttribute("dojotype");if(djt){if(djt.indexOf(":")<0){djt="dojo:"+djt;}
return djt.toLowerCase();}
djt=node.getAttributeNS&&node.getAttributeNS(dojo.dom.dojoml,"type");if(djt){return "dojo:"+djt.toLowerCase();}
try{djt=node.getAttribute("dojo:type");}
catch(e){}
if(djt){return "dojo:"+djt.toLowerCase();}
if((dj_global["djConfig"])&&(!djConfig["ignoreClassNames"])){var _36f=node.className||node.getAttribute("class");if((_36f)&&(_36f.indexOf)&&(_36f.indexOf("dojo-")!=-1)){var _370=_36f.split(" ");for(var x=0,c=_370.length;x<c;x++){if(_370[x].slice(0,5)=="dojo-"){return "dojo:"+_370[x].substr(5).toLowerCase();}}
}}
return "";}
this.parseElement=function(node,_374,_375,_376){var _377=getTagName(node);if(isIE&&_377.indexOf("/")==0){return null;}
try{var attr=node.getAttribute("parseWidgets");if(attr&&attr.toLowerCase()=="false"){return {};}}
catch(e){}
var _379=true;if(_375){var _37a=getDojoTagName(node);_377=_37a||_377;_379=Boolean(_37a);}
var _37b={};_37b[_377]=[];var pos=_377.indexOf(":");if(pos>0){var ns=_377.substring(0,pos);_37b["ns"]=ns;if((dojo.ns)&&(!dojo.ns.allow(ns))){_379=false;}}
if(_379){var _37e=this.parseAttributes(node);for(var attr in _37e){if((!_37b[_377][attr])||(typeof _37b[_377][attr]!="array")){_37b[_377][attr]=[];}
_37b[_377][attr].push(_37e[attr]);}
_37b[_377].nodeRef=node;_37b.tagName=_377;_37b.index=_376||0;}
var _37f=0;for(var i=0;i<node.childNodes.length;i++){var tcn=node.childNodes.item(i);switch(tcn.nodeType){case dojo.dom.ELEMENT_NODE:
var ctn=getDojoTagName(tcn)||getTagName(tcn);if(!_37b[ctn]){_37b[ctn]=[];}
_37b[ctn].push(this.parseElement(tcn,true,_375,_37f));if((tcn.childNodes.length==1)&&(tcn.childNodes.item(0).nodeType==dojo.dom.TEXT_NODE)){_37b[ctn][_37b[ctn].length-1].value=tcn.childNodes.item(0).nodeValue;}
_37f++;break;case dojo.dom.TEXT_NODE:
if(node.childNodes.length==1){_37b[_377].push({value:node.childNodes.item(0).nodeValue});}
break;default:
break;}}
return _37b;};this.parseAttributes=function(node){var _384={};var atts=node.attributes;var _386,i=0;while((_386=atts[i++])){if(isIE){if(!_386){continue;}
if((typeof _386=="object")&&(typeof _386.nodeValue=="undefined")||(_386.nodeValue==null)||(_386.nodeValue=="")){continue;}}
var nn=_386.nodeName.split(":");nn=(nn.length==2)?nn[1]:_386.nodeName;_384[nn]={value:_386.nodeValue};}
return _384;};};dojo.lang.declare=function(_389,_38a,init,_38c){if((dojo.lang.isFunction(_38c))||((!_38c)&&(!dojo.lang.isFunction(init)))){if(dojo.lang.isFunction(_38c)){dojo.deprecated("dojo.lang.declare("+_389+"...):","use class, superclass, initializer, properties argument order","0.6");}
var temp=_38c;_38c=init;init=temp;}
if(_38c&&_38c.initializer){dojo.deprecated("dojo.lang.declare("+_389+"...):","specify initializer as third argument, not as an element in properties","0.6");}
var _38e=[];if(dojo.lang.isArray(_38a)){_38e=_38a;_38a=_38e.shift();}
if(!init){init=dojo.getObject(_389,false);if((init)&&(!dojo.lang.isFunction(init))){init=null;}}
var ctor=dojo.lang.declare._makeConstructor();var scp=(_38a?_38a.prototype:null);if(scp){scp.prototyping=true;ctor.prototype=new _38a();scp.prototyping=false;}
ctor.superclass=scp;ctor.mixins=_38e;for(var i=0,l=_38e.length;i<l;i++){dojo.lang.extend(ctor,_38e[i].prototype);}
ctor.prototype.initializer=null;ctor.prototype.declaredClass=_389;if(dojo.lang.isArray(_38c)){dojo.lang.extend.apply(dojo.lang,[ctor].concat(_38c));}else{dojo.lang.extend(ctor,(_38c)||{});}
dojo.lang.extend(ctor,dojo.lang.declare._common);ctor.prototype.constructor=ctor;ctor.prototype.initializer=(ctor.prototype.initializer)||(init)||(function(){});var _393=dojo.getObject(_389,true,null,true);_393.obj[_393.prop]=ctor;return ctor;};dojo.lang.declare._makeConstructor=function(){return function(){var self=this._getPropContext();var s=self.constructor.superclass;if((s)&&(s.constructor)){if(s.constructor==arguments.callee){this._inherited("constructor",arguments);}else{this._contextMethod(s,"constructor",arguments);}}
var ms=(self.constructor.mixins)||([]);for(var i=0,m;(m=ms[i]);i++){(((m.prototype)&&(m.prototype.initializer))||(m)).apply(this,arguments);}
if((!this.prototyping)&&(self.initializer)){self.initializer.apply(this,arguments);}};};dojo.lang.declare._common={_getPropContext:function(){return (this.___proto||this);},_contextMethod:function(_399,_39a,args){var _39c,_39d=this.___proto;this.___proto=_399;try{_39c=_399[_39a].apply(this,(args||[]));}
catch(e){throw e;}
finally{this.___proto=_39d;}
return _39c;},_inherited:function(prop,args){var p=this._getPropContext();do{if((!p.constructor)||(!p.constructor.superclass)){return;}
p=p.constructor.superclass;}while(!(prop in p));return (dojo.lang.isFunction(p[prop])?this._contextMethod(p,prop,args):p[prop]);}};dojo.declare=dojo.lang.declare;dojo.ns={namespaces:{},failed:{},loading:{},loaded:{},register:function(name,_3a2,_3a3,_3a4){if(!_3a4||!this.namespaces[name]){this.namespaces[name]=new dojo.ns.Ns(name,_3a2,_3a3);}},allow:function(name){if(this.failed[name]){return false;}
if((djConfig.excludeNamespace)&&(dojo.lang.inArray(djConfig.excludeNamespace,name))){return false;}
return ((name==this.dojo)||(!djConfig.includeNamespace)||(dojo.lang.inArray(djConfig.includeNamespace,name)));},get:function(name){return this.namespaces[name];},require:function(name){var ns=this.namespaces[name];if((ns)&&(this.loaded[name])){return ns;}
if(!this.allow(name)){return false;}
if(this.loading[name]){dojo.debug("dojo.namespace.require: re-entrant request to load namespace \""+name+"\" must fail.");return false;}
var req=dojo.require;this.loading[name]=true;try{if(name=="dojo"){req("dojo.namespaces.dojo");}else{if(!dojo.hostenv.moduleHasPrefix(name)){dojo.registerModulePath(name,"../"+name);}
req([name,"manifest"].join("."),false,true);}
if(!this.namespaces[name]){this.failed[name]=true;}}
finally{this.loading[name]=false;}
return this.namespaces[name];}};dojo.ns.Ns=function(name,_3ab,_3ac){this.name=name;this.module=_3ab;this.resolver=_3ac;this._loaded=[];this._failed=[];};dojo.ns.Ns.prototype.resolve=function(name,_3ae,_3af){if(!this.resolver||djConfig["skipAutoRequire"]){return false;}
var _3b0=this.resolver(name,_3ae);if((_3b0)&&(!this._loaded[_3b0])&&(!this._failed[_3b0])){var req=dojo.require;req(_3b0,false,true);if(dojo.hostenv.findModule(_3b0,false)){this._loaded[_3b0]=true;}else{if(!_3af){dojo.raise("dojo.ns.Ns.resolve: module '"+_3b0+"' not found after loading via namespace '"+this.name+"'");}
this._failed[_3b0]=true;}}
return Boolean(this._loaded[_3b0]);};dojo.registerNamespace=function(name,_3b3,_3b4){dojo.ns.register.apply(dojo.ns,arguments);};dojo.registerNamespaceResolver=function(name,_3b6){var n=dojo.ns.namespaces[name];if(n){n.resolver=_3b6;}};dojo.registerNamespaceManifest=function(_3b8,path,name,_3bb,_3bc){dojo.registerModulePath(name,path);dojo.registerNamespace(name,_3bb,_3bc);};dojo.registerNamespace("dojo","dojo.widget");dojo.event.topic=new function(){this.topics={};this.getTopic=function(_3bd){if(!this.topics[_3bd]){this.topics[_3bd]=new this.TopicImpl(_3bd);}
return this.topics[_3bd];};this.registerPublisher=function(_3be,obj,_3c0){var _3be=this.getTopic(_3be);_3be.registerPublisher(obj,_3c0);};this.subscribe=function(_3c1,obj,_3c3){var _3c1=this.getTopic(_3c1);_3c1.subscribe(obj,_3c3);};this.unsubscribe=function(_3c4,obj,_3c6){var _3c4=this.getTopic(_3c4);_3c4.unsubscribe(obj,_3c6);};this.destroy=function(_3c7){this.getTopic(_3c7).destroy();delete this.topics[_3c7];};this.publishApply=function(_3c8,args){var _3c8=this.getTopic(_3c8);_3c8.sendMessage.apply(_3c8,args);};this.publish=function(_3ca,_3cb){var _3ca=this.getTopic(_3ca);var args=[];for(var x=1;x<arguments.length;x++){args.push(arguments[x]);}
_3ca.sendMessage.apply(_3ca,args);};};dojo.event.topic.TopicImpl=function(_3ce){this.topicName=_3ce;this.subscribe=function(_3cf,_3d0){var tf=_3d0||_3cf;var to=(!_3d0)?dj_global:_3cf;return dojo.event.kwConnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});};this.unsubscribe=function(_3d3,_3d4){var tf=(!_3d4)?_3d3:_3d4;var to=(!_3d4)?null:_3d3;return dojo.event.kwDisconnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});};this._getJoinPoint=function(){return dojo.event.MethodJoinPoint.getForMethod(this,"sendMessage");};this.setSquelch=function(_3d7){this._getJoinPoint().squelch=_3d7;};this.destroy=function(){this._getJoinPoint().disconnect();};this.registerPublisher=function(_3d8,_3d9){dojo.event.connect(_3d8,_3d9,this,"sendMessage");};this.sendMessage=function(_3da){};};dojo.kwCompoundRequire({common:["dojo.event.common","dojo.event.topic"],browser:["dojo.event.browser"],dashboard:["dojo.event.browser"]});dojo.widget.manager=new function(){this.widgets=[];this.widgetIds=[];this.topWidgets={};var _3db={};var _3dc=[];this.getUniqueId=function(_3dd){var _3de;do{_3de=_3dd+"_"+(_3db[_3dd]!=undefined?++_3db[_3dd]:_3db[_3dd]=0);}while(this.getWidgetById(_3de));return _3de;};this.add=function(_3df){this.widgets.push(_3df);if(!_3df.extraArgs["id"]){_3df.extraArgs["id"]=_3df.extraArgs["ID"];}
if(_3df.widgetId==""){if(_3df["id"]){_3df.widgetId=_3df["id"];}else{if(_3df.extraArgs["id"]){_3df.widgetId=_3df.extraArgs["id"];}else{_3df.widgetId=this.getUniqueId(_3df.ns+"_"+_3df.widgetType);}}
}
if(this.widgetIds[_3df.widgetId]){dojo.debug("widget ID collision on ID: "+_3df.widgetId);}
this.widgetIds[_3df.widgetId]=_3df;};this.destroyAll=function(){for(var x=this.widgets.length-1;x>=0;x--){try{this.widgets[x].destroy(true);delete this.widgets[x];}
catch(e){}}
};this.remove=function(_3e1){if(dojo.lang.isNumber(_3e1)){var tw=this.widgets[_3e1].widgetId;delete this.widgetIds[tw];this.widgets.splice(_3e1,1);}else{this.removeById(_3e1);}};this.removeById=function(id){if(!dojo.lang.isString(id)){id=id["widgetId"];if(!id){dojo.debug("invalid widget or id passed to removeById");return;}}
for(var i=0;i<this.widgets.length;i++){if(this.widgets[i].widgetId==id){this.remove(i);break;}}
};this.getWidgetById=function(id){if(dojo.lang.isString(id)){return this.widgetIds[id];}
return id;};this.getWidgetsByType=function(type){var lt=type.toLowerCase();var _3e8=(type.indexOf(":")<0?function(x){return x.widgetType.toLowerCase();}:function(x){return x.getNamespacedType();});var ret=[];dojo.lang.forEach(this.widgets,function(x){if(_3e8(x)==lt){ret.push(x);}});return ret;};this.getWidgetsByFilter=function(_3ed,_3ee){var ret=[];dojo.lang.every(this.widgets,function(x){if(_3ed(x)){ret.push(x);if(_3ee){return false;}}
return true;});return (_3ee?ret[0]:ret);};this.getAllWidgets=function(){return this.widgets.concat();};this.getWidgetByNode=function(node){var w=this.getAllWidgets();node=dojo.byId(node);for(var i=0;i<w.length;i++){if(w[i].domNode==node){return w[i];}}
return null;};this.byId=this.getWidgetById;this.byType=this.getWidgetsByType;this.byFilter=this.getWidgetsByFilter;this.byNode=this.getWidgetByNode;var _3f4={};var _3f5=["dojo.widget"];for(var i=0;i<_3f5.length;i++){_3f5[_3f5[i]]=true;}
this.registerWidgetPackage=function(_3f7){if(!_3f5[_3f7]){_3f5[_3f7]=true;_3f5.push(_3f7);}};this.getWidgetPackageList=function(){return dojo.lang.map(_3f5,function(elt){return (elt!==true?elt:undefined);});};this.getImplementation=function(_3f9,_3fa,_3fb,ns){var impl=this.getImplementationName(_3f9,ns);if(impl){var ret=_3fa?new impl(_3fa):new impl();return ret;}};function buildPrefixCache(){for(var _3ff in dojo.render){if(dojo.render[_3ff]["capable"]===true){var _400=dojo.render[_3ff].prefixes;for(var i=0;i<_400.length;i++){_3dc.push(_400[i].toLowerCase());}}
}}
var _402=function(_403,_404){if(!_404){return null;}
for(var i=0,l=_3dc.length,_407;i<=l;i++){_407=(i<l?_404[_3dc[i]]:_404);if(!_407){continue;}
for(var name in _407){if(name.toLowerCase()==_403){return _407[name];}}
}
return null;};var _409=function(_40a,_40b){var _40c=dojo.getObject(_40b,false);return (_40c?_402(_40a,_40c):null);};this.getImplementationName=function(_40d,ns){var _40f=_40d.toLowerCase();ns=ns||"dojo";var imps=_3f4[ns]||(_3f4[ns]={});var impl=imps[_40f];if(impl){return impl;}
if(!_3dc.length){buildPrefixCache();}
var _412=dojo.ns.get(ns);if(!_412){dojo.ns.register(ns,ns+".widget");_412=dojo.ns.get(ns);}
if(_412){_412.resolve(_40d);}
impl=_409(_40f,_412.module);if(impl){return (imps[_40f]=impl);}
_412=dojo.ns.require(ns);if((_412)&&(_412.resolver)){_412.resolve(_40d);impl=_409(_40f,_412.module);if(impl){return (imps[_40f]=impl);}}
throw new Error("Could not locate widget implementation for \""+_40d+"\" in \""+_412.module+"\" registered to namespace \""+_412.name+"\"");};this.resizing=false;this.onWindowResized=function(){if(this.resizing){return;}
try{this.resizing=true;for(var id in this.topWidgets){var _414=this.topWidgets[id];if(_414.checkSize){_414.checkSize();}}
}
catch(e){}
finally{this.resizing=false;}};if(typeof window!="undefined"){dojo.addOnLoad(this,"onWindowResized");dojo.event.connect(window,"onresize",this,"onWindowResized");}};(function(){var dw=dojo.widget;var dwm=dw.manager;var h=dojo.lang.curry(dojo.lang,"hitch",dwm);var g=function(_419,_41a){dw[(_41a||_419)]=h(_419);};g("add","addWidget");g("destroyAll","destroyAllWidgets");g("remove","removeWidget");g("removeById","removeWidgetById");g("getWidgetById");g("getWidgetById","byId");g("getWidgetsByType");g("getWidgetsByFilter");g("getWidgetsByType","byType");g("getWidgetsByFilter","byFilter");g("getWidgetByNode","byNode");dw.all=function(n){var _41c=dwm.getAllWidgets.apply(dwm,arguments);if(arguments.length>0){return _41c[n];}
return _41c;};g("registerWidgetPackage");g("getImplementation","getWidgetImplementation");g("getImplementationName","getWidgetImplementationName");dw.widgets=dwm.widgets;dw.widgetIds=dwm.widgetIds;dw.root=dwm.root;})();dojo.uri=new function(){var _41d=new RegExp("^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$");var _41e=new RegExp("(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$");var _41f=new RegExp("/(\\w+.css)");this.dojoUri=function(uri){return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri(),uri);};this.moduleUri=function(_421,uri){var loc=dojo.hostenv.getModuleSymbols(_421).join("/");if(!loc){return null;}
if(loc.lastIndexOf("/")!=loc.length-1){loc+="/";}
var _424=loc.indexOf(":");var _425=loc.indexOf("/");if(loc.charAt(0)!="/"&&(_424==-1||_424>_425)){loc=dojo.hostenv.getBaseScriptUri()+loc;}
return new dojo.uri.Uri(loc,uri);};this.Uri=function(){var uri=arguments[0];if(uri&&arguments.length>1){var _427=_41f.exec(uri);if(_427){uri=uri.toString().replace(_427[1],"");}}
for(var i=1;i<arguments.length;i++){if(!arguments[i]){continue;}
var _429=new dojo.uri.Uri(arguments[i].toString());var _42a=new dojo.uri.Uri(uri.toString());if((_429.path=="")&&(_429.scheme==null)&&(_429.authority==null)&&(_429.query==null)){if(_429.fragment!=null){_42a.fragment=_429.fragment;}
_429=_42a;}
if(_429.scheme!=null&&_429.authority!=null){uri="";}
if(_429.scheme!=null){uri+=_429.scheme+":";}
if(_429.authority!=null){uri+="//"+_429.authority;}
uri+=_429.path;if(_429.query!=null){uri+="?"+_429.query;}
if(_429.fragment!=null){uri+="#"+_429.fragment;}}
this.uri=uri.toString();var r=this.uri.match(_41e);this.scheme=r[2]||(r[1]?"":null);this.authority=r[4]||(r[3]?"":null);this.path=r[5];this.query=r[7]||(r[6]?"":null);this.fragment=r[9]||(r[8]?"":null);if(this.authority!=null){r=this.authority.match(_41d);this.user=r[3]||null;this.password=r[4]||null;this.host=r[5];this.port=r[7]||null;}
this.toString=function(){return this.uri;};};};dojo.kwCompoundRequire({common:[["dojo.uri.Uri",false,false]]});dojo.lang.mixin(dojo.html,dojo.dom);dojo.html.getEventTarget=function(evt){if(!evt){evt=dojo.global().event||{};}
var t=(evt.srcElement?evt.srcElement:(evt.target?evt.target:null));while((t)&&(t.nodeType!=1)){t=t.parentNode;}
return t;};dojo.html.getViewport=function(){var _42e=dojo.global();var _42f=dojo.doc();var w=0;var h=0;if(dojo.render.html.mozilla){w=_42f.documentElement.clientWidth;h=_42e.innerHeight;}else{if(!dojo.render.html.opera&&_42e.innerWidth){w=_42e.innerWidth;h=_42e.innerHeight;}else{if(!dojo.render.html.opera&&dojo.exists("documentElement.clientWidth",_42f)){var w2=_42f.documentElement.clientWidth;if(!w||w2&&w2<w){w=w2;}
h=_42f.documentElement.clientHeight;}else{if(dojo.body().clientWidth){w=dojo.body().clientWidth;h=dojo.body().clientHeight;}}
}}
return {width:w,height:h};};dojo.html.getScroll=function(){var _433=dojo.global();var _434=dojo.doc();var top=_433.pageYOffset||_434.documentElement.scrollTop||dojo.body().scrollTop||0;var left=_433.pageXOffset||_434.documentElement.scrollLeft||dojo.body().scrollLeft||0;return {top:top,left:left,offset:{x:left,y:top}};};dojo.html.getParentByType=function(node,type){var _439=dojo.doc();var _43a=dojo.byId(node);type=type.toLowerCase();while((_43a)&&(_43a.nodeName.toLowerCase()!=type)){if(_43a==(_439["body"]||_439["documentElement"])){return null;}
_43a=_43a.parentNode;}
return _43a;};dojo.html.getAttribute=function(node,attr){node=dojo.byId(node);if((!node)||(!node.getAttribute)){return null;}
var ta=typeof attr=="string"?attr:new String(attr);var v=node.getAttribute(ta.toUpperCase());if((v)&&(typeof v=="string")&&(v!="")){return v;}
if(v&&v.value){return v.value;}
if((node.getAttributeNode)&&(node.getAttributeNode(ta))){return (node.getAttributeNode(ta)).value;}else{if(node.getAttribute(ta)){return node.getAttribute(ta);}else{if(node.getAttribute(ta.toLowerCase())){return node.getAttribute(ta.toLowerCase());}}
}
return null;};dojo.html.hasAttribute=function(node,attr){return dojo.html.getAttribute(dojo.byId(node),attr)?true:false;};dojo.html.getCursorPosition=function(e){e=e||dojo.global().event;var _442={x:0,y:0};if(e.pageX||e.pageY){_442.x=e.pageX;_442.y=e.pageY;}else{var de=dojo.doc().documentElement;var db=dojo.body();_442.x=e.clientX+((de||db)["scrollLeft"])-((de||db)["clientLeft"]);_442.y=e.clientY+((de||db)["scrollTop"])-((de||db)["clientTop"]);}
return _442;};dojo.html.isTag=function(node){node=dojo.byId(node);if(node&&node.tagName){for(var i=1;i<arguments.length;i++){if(node.tagName.toLowerCase()==String(arguments[i]).toLowerCase()){return String(arguments[i]).toLowerCase();}}
}
return "";};if(dojo.render.html.ie&&!dojo.render.html.ie70){if(window.location.href.substr(0,6).toLowerCase()!="https:"){(function(){var _447=dojo.doc().createElement("script");_447.src="javascript:'dojo.html.createExternalElement=function(doc, tag){ return doc.createElement(tag); }'";dojo.doc().getElementsByTagName("head")[0].appendChild(_447);})();}}else{dojo.html.createExternalElement=function(doc,tag){return doc.createElement(tag);};}
dojo.a11y={imgPath:dojo.uri.moduleUri("dojo.widget","templates/images"),doAccessibleCheck:true,accessible:null,checkAccessible:function(){if(this.accessible===null){this.accessible=false;if(this.doAccessibleCheck==true){this.accessible=this.testAccessible();}}
return this.accessible;},testAccessible:function(){this.accessible=false;if(dojo.render.html.ie||dojo.render.html.mozilla){var div=document.createElement("div");div.style.backgroundImage="url(\""+this.imgPath+"/tab_close.gif\")";dojo.body().appendChild(div);var _44b=null;if(window.getComputedStyle){var _44c=getComputedStyle(div,"");_44b=_44c.getPropertyValue("background-image");}else{_44b=div.currentStyle.backgroundImage;}
var _44d=false;if(_44b!=null&&(_44b=="none"||_44b=="url(invalid-url:)")){this.accessible=true;}
dojo.body().removeChild(div);}
return this.accessible;},setAccessible:function(_44e){this.accessible=_44e;},setCheckAccessible:function(_44f){this.doAccessibleCheck=_44f;},setAccessibleMode:function(){if(this.accessible===null){if(this.checkAccessible()){dojo.render.html.prefixes.unshift("a11y");}}
return this.accessible;}};dojo.declare("dojo.widget.Widget",null,function(){this.children=[];this.extraArgs={};},{parent:null,isTopLevel:false,disabled:false,isContainer:false,widgetId:"",widgetType:"Widget",ns:"dojo",getNamespacedType:function(){return (this.ns?this.ns+":"+this.widgetType:this.widgetType).toLowerCase();},toString:function(){return "[Widget "+this.getNamespacedType()+", "+(this.widgetId||"NO ID")+"]";},repr:function(){return this.toString();},enable:function(){this.disabled=false;},disable:function(){this.disabled=true;},onResized:function(){this.notifyChildrenOfResize();},notifyChildrenOfResize:function(){for(var i=0;i<this.children.length;i++){var _451=this.children[i];if(_451.onResized){_451.onResized();}}
},create:function(args,_453,_454,ns){if(ns){this.ns=ns;}
this.satisfyPropertySets(args,_453,_454);this.mixInProperties(args,_453,_454);this.postMixInProperties(args,_453,_454);dojo.widget.manager.add(this);this.buildRendering(args,_453,_454);this.initialize(args,_453,_454);this.postInitialize(args,_453,_454);this.postCreate(args,_453,_454);return this;},destroy:function(_456){if(this.parent){this.parent.removeChild(this);}
this.destroyChildren();this.uninitialize();this.destroyRendering(_456);dojo.widget.manager.removeById(this.widgetId);},destroyChildren:function(){var _457;var i=0;while(this.children.length>i){_457=this.children[i];if(_457 instanceof dojo.widget.Widget){this.removeChild(_457);_457.destroy();continue;}
i++;}},getChildrenOfType:function(type,_45a){var ret=[];var _45c=dojo.lang.isFunction(type);if(!_45c){type=type.toLowerCase();}
for(var x=0;x<this.children.length;x++){if(_45c){if(this.children[x] instanceof type){ret.push(this.children[x]);}}else{if(this.children[x].widgetType.toLowerCase()==type){ret.push(this.children[x]);}}
if(_45a){ret=ret.concat(this.children[x].getChildrenOfType(type,_45a));}}
return ret;},getDescendants:function(){var _45e=[];var _45f=[this];var elem;while((elem=_45f.pop())){_45e.push(elem);if(elem.children){dojo.lang.forEach(elem.children,function(elem){_45f.push(elem);});}}
return _45e;},isFirstChild:function(){return this===this.parent.children[0];},isLastChild:function(){return this===this.parent.children[this.parent.children.length-1];},satisfyPropertySets:function(args){return args;},mixInProperties:function(args,frag){if((args["fastMixIn"])||(frag["fastMixIn"])){for(var x in args){this[x]=args[x];}
return;}
var _466;var _467=dojo.widget.lcArgsCache[this.widgetType];if(_467==null){_467={};for(var y in this){_467[((new String(y)).toLowerCase())]=y;}
dojo.widget.lcArgsCache[this.widgetType]=_467;}
var _469={};for(var x in args){if(!this[x]){var y=_467[(new String(x)).toLowerCase()];if(y){args[y]=args[x];x=y;}}
if(_469[x]){continue;}
_469[x]=true;if((typeof this[x])!=(typeof _466)){if(typeof args[x]!="string"){this[x]=args[x];}else{if(dojo.lang.isString(this[x])){this[x]=args[x];}else{if(dojo.lang.isNumber(this[x])){this[x]=new Number(args[x]);}else{if(dojo.lang.isBoolean(this[x])){this[x]=(args[x].toLowerCase()=="false")?false:true;}else{if(dojo.lang.isFunction(this[x])){if(args[x].search(/[^\w\.]+/i)==-1){this[x]=dojo.getObject(args[x],false);}else{var tn=dojo.lang.nameAnonFunc(new Function(args[x]),this);dojo.event.kwConnect({srcObj:this,srcFunc:x,adviceObj:this,adviceFunc:tn});}}else{if(dojo.lang.isArray(this[x])){this[x]=args[x].split(";");}else{if(this[x] instanceof Date){this[x]=new Date(Number(args[x]));}else{if(typeof this[x]=="object"){if(this[x] instanceof dojo.uri.Uri){this[x]=dojo.uri.dojoUri(args[x]);}else{var _46b=args[x].split(";");for(var y=0;y<_46b.length;y++){var si=_46b[y].indexOf(":");if((si!=-1)&&(_46b[y].length>si)){this[x][_46b[y].substr(0,si).replace(/^\s+|\s+$/g,"")]=_46b[y].substr(si+1);}}
}}else{this[x]=args[x];}}
}}
}}
}}
}else{this.extraArgs[x.toLowerCase()]=args[x];}}
},postMixInProperties:function(args,frag,_46f){},initialize:function(args,frag,_472){return false;},postInitialize:function(args,frag,_475){return false;},postCreate:function(args,frag,_478){return false;},uninitialize:function(){return false;},buildRendering:function(args,frag,_47b){dojo.unimplemented("dojo.widget.Widget.buildRendering, on "+this.toString()+", ");return false;},destroyRendering:function(){dojo.unimplemented("dojo.widget.Widget.destroyRendering");return false;},addedTo:function(_47c){},addChild:function(_47d){dojo.unimplemented("dojo.widget.Widget.addChild");return false;},removeChild:function(_47e){for(var x=0;x<this.children.length;x++){if(this.children[x]===_47e){this.children.splice(x,1);_47e.parent=null;break;}}
return _47e;},getPreviousSibling:function(){var idx=this.getParentIndex();if(idx<=0){return null;}
return this.parent.children[idx-1];},getSiblings:function(){return this.parent.children;},getParentIndex:function(){return dojo.lang.indexOf(this.parent.children,this,true);},getNextSibling:function(){var idx=this.getParentIndex();if(idx==this.parent.children.length-1){return null;}
if(idx<0){return null;}
return this.parent.children[idx+1];}});dojo.widget.lcArgsCache={};dojo.widget.tags={};dojo.widget.tags["dojo:propertyset"]=function(_482,_483,_484){var _485=_483.parseProperties(_482["dojo:propertyset"]);};dojo.widget.tags["dojo:connect"]=function(_486,_487,_488){var _489=_487.parseProperties(_486["dojo:connect"]);};dojo.widget.buildWidgetFromParseTree=function(type,frag,_48c,_48d,_48e,_48f){var _490=type.split(":");_490=(_490.length==2)?_490[1]:type;var _491=_48f||_48c.parseProperties(frag[frag["ns"]+":"+_490]);var _492=dojo.widget.manager.getImplementation(_490,null,null,frag["ns"]);if(!_492){throw new Error("cannot find \""+type+"\" widget");}else{if(!_492.create){throw new Error("\""+type+"\" widget object has no \"create\" method and does not appear to implement *Widget");}}
_491["dojoinsertionindex"]=_48e;var ret=_492.create(_491,frag,_48d,frag["ns"]);return ret;};dojo.widget.defineWidget=function(_494,_495,_496,init,_498){if(dojo.lang.isString(arguments[3])){dojo.widget._defineWidget(arguments[0],arguments[3],arguments[1],arguments[4],arguments[2]);}else{var args=[arguments[0]],p=3;if(dojo.lang.isString(arguments[1])){args.push(arguments[1],arguments[2]);}else{args.push("",arguments[1]);p=2;}
if(dojo.lang.isFunction(arguments[p])){args.push(arguments[p],arguments[p+1]);}else{args.push(null,arguments[p]);}
dojo.widget._defineWidget.apply(this,args);}};dojo.widget.defineWidget.renderers="html|svg|vml";dojo.widget._defineWidget=function(_49b,_49c,_49d,init,_49f){var _4a0=_49b.split(".");var type=_4a0.pop();var regx="\\.("+(_49c?_49c+"|":"")+dojo.widget.defineWidget.renderers+")\\.";var r=_49b.search(new RegExp(regx));_4a0=(r<0?_4a0.join("."):_49b.substr(0,r));dojo.widget.manager.registerWidgetPackage(_4a0);var pos=_4a0.indexOf(".");var _4a5=(pos>-1)?_4a0.substring(0,pos):_4a0;_49f=(_49f)||{};_49f.widgetType=type;if((!init)&&(_49f["classConstructor"])){init=_49f.classConstructor;delete _49f.classConstructor;}
dojo.declare(_49b,_49d,init,_49f);};dojo.widget.Parse=function(_4a6){this.propertySetsList=[];this.fragment=_4a6;this.createComponents=function(frag,_4a8){var _4a9=[];var _4aa=false;try{if(frag&&frag.tagName&&(frag!=frag.nodeRef)){var _4ab=dojo.widget.tags;var tna=String(frag.tagName).split(";");for(var x=0;x<tna.length;x++){var ltn=tna[x].replace(/^\s+|\s+$/g,"").toLowerCase();frag.tagName=ltn;var ret;if(_4ab[ltn]){_4aa=true;ret=_4ab[ltn](frag,this,_4a8,frag.index);_4a9.push(ret);}else{if(ltn.indexOf(":")==-1){ltn="dojo:"+ltn;}
ret=dojo.widget.buildWidgetFromParseTree(ltn,frag,this,_4a8,frag.index);if(ret){_4aa=true;_4a9.push(ret);}}
}}
}
catch(e){dojo.debug("dojo.widget.Parse: error:",e);}
if(!_4aa){_4a9=_4a9.concat(this.createSubComponents(frag,_4a8));}
return _4a9;};this.createSubComponents=function(_4b0,_4b1){var frag,_4b3=[];for(var item in _4b0){frag=_4b0[item];if(frag&&typeof frag=="object"&&(frag!=_4b0.nodeRef)&&(frag!=_4b0.tagName)&&(item.indexOf("$")==-1)){_4b3=_4b3.concat(this.createComponents(frag,_4b1));}}
return _4b3;};this.parsePropertySets=function(_4b5){return [];};this.parseProperties=function(_4b6){var _4b7={};for(var item in _4b6){if((_4b6[item]==_4b6.tagName)||(_4b6[item]==_4b6.nodeRef)){}else{var frag=_4b6[item];if(frag.tagName&&dojo.widget.tags[frag.tagName.toLowerCase()]){}else{if(frag[0]&&frag[0].value!=""&&frag[0].value!=null){try{if(item.toLowerCase()=="dataprovider"){var _4ba=this;this.getDataProvider(_4ba,frag[0].value);_4b7.dataProvider=this.dataProvider;}
_4b7[item]=frag[0].value;var _4bb=this.parseProperties(frag);for(var _4bc in _4bb){_4b7[_4bc]=_4bb[_4bc];}}
catch(e){dojo.debug(e);}}
}
switch(item.toLowerCase()){case "checked":
case "disabled":
if(typeof _4b7[item]!="boolean"){_4b7[item]=true;}
break;}}
}
return _4b7;};this.getDataProvider=function(_4bd,_4be){dojo.io.bind({url:_4be,load:function(type,_4c0){if(type=="load"){_4bd.dataProvider=_4c0;}},mimetype:"text/javascript",sync:true});};this.getPropertySetById=function(_4c1){for(var x=0;x<this.propertySetsList.length;x++){if(_4c1==this.propertySetsList[x]["id"][0].value){return this.propertySetsList[x];}}
return "";};this.getPropertySetsByType=function(_4c3){var _4c4=[];for(var x=0;x<this.propertySetsList.length;x++){var cpl=this.propertySetsList[x];var cpcc=cpl.componentClass||cpl.componentType||null;var _4c8=this.propertySetsList[x]["id"][0].value;if(cpcc&&(_4c8==cpcc[0].value)){_4c4.push(cpl);}}
return _4c4;};this.getPropertySets=function(_4c9){var ppl="dojo:propertyproviderlist";var _4cb=[];var _4cc=_4c9.tagName;if(_4c9[ppl]){var _4cd=_4c9[ppl].value.split(" ");for(var _4ce in _4cd){if((_4ce.indexOf("..")==-1)&&(_4ce.indexOf("://")==-1)){var _4cf=this.getPropertySetById(_4ce);if(_4cf!=""){_4cb.push(_4cf);}}else{}}
}
return this.getPropertySetsByType(_4cc).concat(_4cb);};this.createComponentFromScript=function(_4d0,_4d1,_4d2,ns){_4d2.fastMixIn=true;var ltn=(ns||"dojo")+":"+_4d1.toLowerCase();if(dojo.widget.tags[ltn]){return [dojo.widget.tags[ltn](_4d2,this,null,null,_4d2)];}
return [dojo.widget.buildWidgetFromParseTree(ltn,_4d2,this,null,null,_4d2)];};};dojo.widget._parser_collection={"dojo":new dojo.widget.Parse()};dojo.widget.getParser=function(name){if(!name){name="dojo";}
if(!this._parser_collection[name]){this._parser_collection[name]=new dojo.widget.Parse();}
return this._parser_collection[name];};dojo.widget.createWidget=function(name,_4d7,_4d8,_4d9){var _4da=false;var _4db=(typeof name=="string");if(_4db){var pos=name.indexOf(":");var ns=(pos>-1)?name.substring(0,pos):"dojo";if(pos>-1){name=name.substring(pos+1);}
var _4de=name.toLowerCase();var _4df=ns+":"+_4de;_4da=(dojo.byId(name)&&!dojo.widget.tags[_4df]);}
if((arguments.length==1)&&(_4da||!_4db)){var xp=new dojo.xml.Parse();var tn=_4da?dojo.byId(name):name;return dojo.widget.getParser().createComponents(xp.parseElement(tn,null,true))[0];}
function fromScript(_4e2,name,_4e4,ns){_4e4[_4df]={dojotype:[{value:_4de}],nodeRef:_4e2,fastMixIn:true};_4e4.ns=ns;return dojo.widget.getParser().createComponentFromScript(_4e2,name,_4e4,ns);}
_4d7=_4d7||{};var _4e6=false;var tn=null;var h=dojo.render.html.capable;if(h){tn=document.createElement("span");}
if(!_4d8){_4e6=true;_4d8=tn;if(h){dojo.body().appendChild(_4d8);}}else{if(_4d9){dojo.dom.insertAtPosition(tn,_4d8,_4d9);}else{tn=_4d8;}}
var _4e8=fromScript(tn,name.toLowerCase(),_4d7,ns);if((!_4e8)||(!_4e8[0])||(typeof _4e8[0].widgetType=="undefined")){throw new Error("createWidget: Creation of \""+name+"\" widget failed.");}
try{if(_4e6&&_4e8[0].domNode.parentNode){_4e8[0].domNode.parentNode.removeChild(_4e8[0].domNode);}}
catch(e){dojo.debug(e);}
return _4e8[0];};dojo.html.getClass=function(node){node=dojo.byId(node);if(!node){return "";}
var cs="";if(node.className){cs=node.className;}else{if(dojo.html.hasAttribute(node,"class")){cs=dojo.html.getAttribute(node,"class");}}
return cs.replace(/^\s+|\s+$/g,"");};dojo.html.getClasses=function(node){var c=dojo.html.getClass(node);return (c=="")?[]:c.split(/\s+/g);};dojo.html.hasClass=function(node,_4ee){return (new RegExp("(^|\\s+)"+_4ee+"(\\s+|$)")).test(dojo.html.getClass(node));};dojo.html.prependClass=function(node,_4f0){_4f0+=" "+dojo.html.getClass(node);return dojo.html.setClass(node,_4f0);};dojo.html.addClass=function(node,_4f2){if(dojo.html.hasClass(node,_4f2)){return false;}
_4f2=(dojo.html.getClass(node)+" "+_4f2).replace(/^\s+|\s+$/g,"");return dojo.html.setClass(node,_4f2);};dojo.html.setClass=function(node,_4f4){node=dojo.byId(node);var cs=new String(_4f4);try{if(typeof node.className=="string"){node.className=cs;}else{if(node.setAttribute){node.setAttribute("class",_4f4);node.className=cs;}else{return false;}}
}
catch(e){dojo.debug("dojo.html.setClass() failed",e);}
return true;};dojo.html.removeClass=function(node,_4f7,_4f8){try{if(!_4f8){var _4f9=dojo.html.getClass(node).replace(new RegExp("(^|\\s+)"+_4f7+"(\\s+|$)"),"$1$2");}else{var _4f9=dojo.html.getClass(node).replace(_4f7,"");}
dojo.html.setClass(node,_4f9);}
catch(e){dojo.debug("dojo.html.removeClass() failed",e);}
return true;};dojo.html.replaceClass=function(node,_4fb,_4fc){dojo.html.removeClass(node,_4fc);dojo.html.addClass(node,_4fb);};dojo.html.classMatchType={ContainsAll:0,ContainsAny:1,IsOnly:2};dojo.html.getElementsByClass=function(_4fd,_4fe,_4ff,_500,_501){_501=false;var _502=dojo.doc();_4fe=dojo.byId(_4fe)||_502;var _503=_4fd.split(/\s+/g);var _504=[];if(_500!=1&&_500!=2){_500=0;}
var _505=new RegExp("(\\s|^)(("+_503.join(")|(")+"))(\\s|$)");var _506=_503.join(" ").length;var _507=[];if(!_501&&_502.evaluate){var _508=".//"+(_4ff||"*")+"[contains(";if(_500!=dojo.html.classMatchType.ContainsAny){_508+="concat(' ',@class,' '), ' "+_503.join(" ') and contains(concat(' ',@class,' '), ' ")+" ')";if(_500==2){_508+=" and string-length(@class)="+_506+"]";}else{_508+="]";}}else{_508+="concat(' ',@class,' '), ' "+_503.join(" ') or contains(concat(' ',@class,' '), ' ")+" ')]";}
var _509=_502.evaluate(_508,_4fe,null,XPathResult.ANY_TYPE,null);var _50a=_509.iterateNext();while(_50a){try{_507.push(_50a);_50a=_509.iterateNext();}
catch(e){break;}}
return _507;}else{if(!_4ff){_4ff="*";}
_507=_4fe.getElementsByTagName(_4ff);var node,i=0;outer:
while(node=_507[i++]){var _50d=dojo.html.getClasses(node);if(_50d.length==0){continue outer;}
var _50e=0;for(var j=0;j<_50d.length;j++){if(_505.test(_50d[j])){if(_500==dojo.html.classMatchType.ContainsAny){_504.push(node);continue outer;}else{_50e++;}}else{if(_500==dojo.html.classMatchType.IsOnly){continue outer;}}
}
if(_50e==_503.length){if((_500==dojo.html.classMatchType.IsOnly)&&(_50e==_50d.length)){_504.push(node);}else{if(_500==dojo.html.classMatchType.ContainsAll){_504.push(node);}}
}}
return _504;}};dojo.html.getElementsByClassName=dojo.html.getElementsByClass;dojo.html.toCamelCase=function(_510){var arr=_510.split("-"),cc=arr[0];for(var i=1;i<arr.length;i++){cc+=arr[i].charAt(0).toUpperCase()+arr[i].substring(1);}
return cc;};dojo.html.toSelectorCase=function(_514){return _514.replace(/([A-Z])/g,"-$1").toLowerCase();};dojo.html.getComputedStyle=function(node,_516,_517){node=dojo.byId(node);var _516=dojo.html.toSelectorCase(_516);var _518=dojo.html.toCamelCase(_516);if(!node||!node.style){return _517;}else{if(document.defaultView&&dojo.html.isDescendantOf(node,node.ownerDocument)){try{var cs=document.defaultView.getComputedStyle(node,"");if(cs){return cs.getPropertyValue(_516);}}
catch(e){if(node.style.getPropertyValue){return node.style.getPropertyValue(_516);}else{return _517;}}
}else{if(node.currentStyle){return node.currentStyle[_518];}}
}
if(node.style.getPropertyValue){return node.style.getPropertyValue(_516);}else{return _517;}};dojo.html.getStyleProperty=function(node,_51b){node=dojo.byId(node);return (node&&node.style?node.style[dojo.html.toCamelCase(_51b)]:undefined);};dojo.html.getStyle=function(node,_51d){var _51e=dojo.html.getStyleProperty(node,_51d);return (_51e?_51e:dojo.html.getComputedStyle(node,_51d));};dojo.html.setStyle=function(node,_520,_521){node=dojo.byId(node);if(node&&node.style){var _522=dojo.html.toCamelCase(_520);node.style[_522]=_521;}};dojo.html.setStyleText=function(_523,text){try{_523.style.cssText=text;}
catch(e){_523.setAttribute("style",text);}};dojo.html.copyStyle=function(_525,_526){if(!_526.style.cssText){_525.setAttribute("style",_526.getAttribute("style"));}else{_525.style.cssText=_526.style.cssText;}
dojo.html.addClass(_525,dojo.html.getClass(_526));};dojo.html.getUnitValue=function(node,_528,_529){var s=dojo.html.getComputedStyle(node,_528);if((!s)||((s=="auto")&&(_529))){return {value:0,units:"px"};}
var _52b=s.match(/(\-?[\d.]+)([a-z%]*)/i);if(!_52b){return dojo.html.getUnitValue.bad;}
return {value:Number(_52b[1]),units:_52b[2].toLowerCase()};};dojo.html.getUnitValue.bad={value:NaN,units:""};dojo.html.getPixelValue=function(node,_52d,_52e){var _52f=dojo.html.getUnitValue(node,_52d,_52e);if(isNaN(_52f.value)){return 0;}
if((_52f.value)&&(_52f.units!="px")){return NaN;}
return _52f.value;};dojo.html.setPositivePixelValue=function(node,_531,_532){if(isNaN(_532)){return false;}
node.style[_531]=Math.max(0,_532)+"px";return true;};dojo.html.styleSheet=null;dojo.html.insertCssRule=function(_533,_534,_535){if(!dojo.html.styleSheet){if(document.createStyleSheet){dojo.html.styleSheet=document.createStyleSheet();}else{if(document.styleSheets[0]){dojo.html.styleSheet=document.styleSheets[0];}else{return null;}}
}
if(arguments.length<3){if(dojo.html.styleSheet.cssRules){_535=dojo.html.styleSheet.cssRules.length;}else{if(dojo.html.styleSheet.rules){_535=dojo.html.styleSheet.rules.length;}else{return null;}}
}
if(dojo.html.styleSheet.insertRule){var rule=_533+" { "+_534+" }";return dojo.html.styleSheet.insertRule(rule,_535);}else{if(dojo.html.styleSheet.addRule){return dojo.html.styleSheet.addRule(_533,_534,_535);}else{return null;}}
};dojo.html.removeCssRule=function(_537){if(!dojo.html.styleSheet){dojo.debug("no stylesheet defined for removing rules");return false;}
if(dojo.render.html.ie){if(!_537){_537=dojo.html.styleSheet.rules.length;dojo.html.styleSheet.removeRule(_537);}}else{if(document.styleSheets[0]){if(!_537){_537=dojo.html.styleSheet.cssRules.length;}
dojo.html.styleSheet.deleteRule(_537);}}
return true;};dojo.html._insertedCssFiles=[];dojo.html.insertCssFile=function(URI,doc,_53a,_53b){if(!URI){return;}
if(!doc){doc=document;}
var _53c=dojo.hostenv.getText(URI,false,_53b);if(_53c===null){return;}
_53c=dojo.html.fixPathsInCssText(_53c,URI);if(_53a){var idx=-1,node,ent=dojo.html._insertedCssFiles;for(var i=0;i<ent.length;i++){if((ent[i].doc==doc)&&(ent[i].cssText==_53c)){idx=i;node=ent[i].nodeRef;break;}}
if(node){var _541=doc.getElementsByTagName("style");for(var i=0;i<_541.length;i++){if(_541[i]==node){return;}}
dojo.html._insertedCssFiles.shift(idx,1);}}
var _542=dojo.html.insertCssText(_53c,doc);dojo.html._insertedCssFiles.push({"doc":doc,"cssText":_53c,"nodeRef":_542});if(_542&&djConfig.isDebug){_542.setAttribute("dbgHref",URI);}
return _542;};dojo.html.insertCssText=function(_543,doc,URI){if(!_543){return;}
if(!doc){doc=document;}
if(URI){_543=dojo.html.fixPathsInCssText(_543,URI);}
var _546=doc.createElement("style");_546.setAttribute("type","text/css");var head=doc.getElementsByTagName("head")[0];if(!head){dojo.debug("No head tag in document, aborting styles");return;}else{head.appendChild(_546);}
if(_546.styleSheet){var _548=function(){try{_546.styleSheet.cssText=_543;}
catch(e){dojo.debug(e);}};if(_546.styleSheet.disabled){setTimeout(_548,10);}else{_548();}}else{var _549=doc.createTextNode(_543);_546.appendChild(_549);}
return _546;};dojo.html.fixPathsInCssText=function(_54a,URI){if(!_54a||!URI){return;}
var _54c,str="",url="",_54f="[\\t\\s\\w\\(\\)\\/\\.\\\\'\"-:#=&?~]+";var _550=new RegExp("url\\(\\s*("+_54f+")\\s*\\)");var _551=/(file|https?|ftps?):\/\//;regexTrim=new RegExp("^[\\s]*(['\"]?)("+_54f+")\\1[\\s]*?$");if(dojo.render.html.ie55||dojo.render.html.ie60){var _552=new RegExp("AlphaImageLoader\\((.*)src=['\"]("+_54f+")['\"]");while(_54c=_552.exec(_54a)){url=_54c[2].replace(regexTrim,"$2");if(!_551.exec(url)){url=(new dojo.uri.Uri(URI,url).toString());}
str+=_54a.substring(0,_54c.index)+"AlphaImageLoader("+_54c[1]+"src='"+url+"'";_54a=_54a.substr(_54c.index+_54c[0].length);}
_54a=str+_54a;str="";}
while(_54c=_550.exec(_54a)){url=_54c[1].replace(regexTrim,"$2");if(!_551.exec(url)){url=(new dojo.uri.Uri(URI,url).toString());}
str+=_54a.substring(0,_54c.index)+"url("+url+")";_54a=_54a.substr(_54c.index+_54c[0].length);}
return str+_54a;};dojo.html.setActiveStyleSheet=function(_553){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")){a.disabled=true;if(a.getAttribute("title")==_553){a.disabled=false;}}
}};dojo.html.getActiveStyleSheet=function(){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")&&!a.disabled){return a.getAttribute("title");}}
return null;};dojo.html.getPreferredStyleSheet=function(){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("rel").indexOf("alt")==-1&&a.getAttribute("title")){return a.getAttribute("title");}}
return null;};dojo.html.applyBrowserClass=function(node){var drh=dojo.render.html;var _55f={dj_ie:drh.ie,dj_ie55:drh.ie55,dj_ie6:drh.ie60,dj_ie7:drh.ie70,dj_iequirks:drh.ie&&drh.quirks,dj_opera:drh.opera,dj_opera8:drh.opera&&(Math.floor(dojo.render.version)==8),dj_opera9:drh.opera&&(Math.floor(dojo.render.version)==9),dj_khtml:drh.khtml,dj_safari:drh.safari,dj_gecko:drh.mozilla};for(var p in _55f){if(_55f[p]){dojo.html.addClass(node,p);}}
};dojo.widget._cssFiles={};dojo.widget._cssStrings={};dojo.widget._templateCache={};dojo.widget.defaultStrings={dojoRoot:dojo.hostenv.getBaseScriptUri(),dojoWidgetModuleUri:dojo.uri.moduleUri("dojo.widget"),baseScriptUri:dojo.hostenv.getBaseScriptUri()};dojo.widget.fillFromTemplateCache=function(obj,_562,_563,_564){var _565=_562||obj.templatePath;var _566=dojo.widget._templateCache;if(!_565&&!obj["widgetType"]){do{var _567="__dummyTemplate__"+dojo.widget._templateCache.dummyCount++;}while(_566[_567]);obj.widgetType=_567;}
var wt=_565?_565.toString():obj.widgetType;var ts=_566[wt];if(!ts){_566[wt]={"string":null,"node":null};if(_564){ts={};}else{ts=_566[wt];}}
if((!obj.templateString)&&(!_564)){obj.templateString=_563||ts["string"];}
if(obj.templateString){obj.templateString=this._sanitizeTemplateString(obj.templateString);}
if((!obj.templateNode)&&(!_564)){obj.templateNode=ts["node"];}
if((!obj.templateNode)&&(!obj.templateString)&&(_565)){var _56a=this._sanitizeTemplateString(dojo.hostenv.getText(_565));obj.templateString=_56a;if(!_564){_566[wt]["string"]=_56a;}}
if((!ts["string"])&&(!_564)){ts.string=obj.templateString;}};dojo.widget._sanitizeTemplateString=function(_56b){if(_56b){_56b=_56b.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im,"");var _56c=_56b.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);if(_56c){_56b=_56c[1];}}else{_56b="";}
return _56b;};dojo.widget._templateCache.dummyCount=0;dojo.widget.attachProperties=["dojoAttachPoint","id"];dojo.widget.eventAttachProperty="dojoAttachEvent";dojo.widget.onBuildProperty="dojoOnBuild";dojo.widget.waiNames=["waiRole","waiState"];dojo.widget.wai={waiRole:{name:"waiRole","namespace":"http://www.w3.org/TR/xhtml2",alias:"x2",prefix:"wairole:"},waiState:{name:"waiState","namespace":"http://www.w3.org/2005/07/aaa",alias:"aaa",prefix:""},setAttr:function(node,ns,attr,_570){if(dojo.render.html.ie){node.setAttribute(this[ns].alias+":"+attr,this[ns].prefix+_570);}else{node.setAttributeNS(this[ns]["namespace"],attr,this[ns].prefix+_570);}},getAttr:function(node,ns,attr){if(dojo.render.html.ie){return node.getAttribute(this[ns].alias+":"+attr);}else{return node.getAttributeNS(this[ns]["namespace"],attr);}},removeAttr:function(node,ns,attr){var _577=true;if(dojo.render.html.ie){_577=node.removeAttribute(this[ns].alias+":"+attr);}else{node.removeAttributeNS(this[ns]["namespace"],attr);}
return _577;}};dojo.widget.attachTemplateNodes=function(_578,_579,_57a){var _57b=dojo.dom.ELEMENT_NODE;function trim(str){return str.replace(/^\s+|\s+$/g,"");}
if(!_578){_578=_579.domNode;}
if(_578.nodeType!=_57b){return;}
var _57d=_578.all||_578.getElementsByTagName("*");var _57e=_579;for(var x=-1;x<_57d.length;x++){var _580=(x==-1)?_578:_57d[x];var _581=[];if(!_579.widgetsInTemplate||!_580.getAttribute("dojoType")){for(var y=0;y<this.attachProperties.length;y++){var _583=_580.getAttribute(this.attachProperties[y]);if(_583){_581=_583.split(";");for(var z=0;z<_581.length;z++){if(dojo.lang.isArray(_579[_581[z]])){_579[_581[z]].push(_580);}else{_579[_581[z]]=_580;}}
break;}}
var _585=_580.getAttribute(this.eventAttachProperty);if(_585){var evts=_585.split(";");for(var y=0;y<evts.length;y++){if((!evts[y])||(!evts[y].length)){continue;}
var _587=null;var tevt=trim(evts[y]);if(evts[y].indexOf(":")>=0){var _589=tevt.split(":");tevt=trim(_589[0]);_587=trim(_589[1]);}
if(!_587){_587=tevt;}
var tf=function(){var ntf=new String(_587);return function(evt){if(_57e[ntf]){_57e[ntf](dojo.event.browser.fixEvent(evt,this));}};}();dojo.event.browser.addListener(_580,tevt,tf,false,true);}}
for(var y=0;y<_57a.length;y++){var _58d=_580.getAttribute(_57a[y]);if((_58d)&&(_58d.length)){var _587=null;var _58e=_57a[y].substr(4);_587=trim(_58d);var _58f=[_587];if(_587.indexOf(";")>=0){_58f=dojo.lang.map(_587.split(";"),trim);}
for(var z=0;z<_58f.length;z++){if(!_58f[z].length){continue;}
var tf=function(){var ntf=new String(_58f[z]);return function(evt){if(_57e[ntf]){_57e[ntf](dojo.event.browser.fixEvent(evt,this));}};}();dojo.event.browser.addListener(_580,_58e,tf,false,true);}}
}}
var _592=_580.getAttribute(this.templateProperty);if(_592){_579[_592]=_580;}
dojo.lang.forEach(dojo.widget.waiNames,function(name){var wai=dojo.widget.wai[name];var val=_580.getAttribute(wai.name);if(val){if(val.indexOf("-")==-1){dojo.widget.wai.setAttr(_580,wai.name,"role",val);}else{var _596=val.split("-");dojo.widget.wai.setAttr(_580,wai.name,_596[0],_596[1]);}}
},this);var _597=_580.getAttribute(this.onBuildProperty);if(_597){eval("var node = baseNode; var widget = targetObj; "+_597);}}
};dojo.widget.getDojoEventsFromStr=function(str){var re=/(dojoOn([a-z]+)(\s?))=/gi;var evts=str?str.match(re)||[]:[];var ret=[];var lem={};for(var x=0;x<evts.length;x++){if(evts[x].length<1){continue;}
var cm=evts[x].replace(/\s/,"");cm=(cm.slice(0,cm.length-1));if(!lem[cm]){lem[cm]=true;ret.push(cm);}}
return ret;};dojo.declare("dojo.widget.DomWidget",dojo.widget.Widget,function(){if((arguments.length>0)&&(typeof arguments[0]=="object")){this.create(arguments[0]);}},{templateNode:null,templateString:null,templateCssString:null,preventClobber:false,domNode:null,containerNode:null,widgetsInTemplate:false,addChild:function(_59f,_5a0,pos,ref,_5a3){if(typeof _5a3=="undefined"){_5a3=this.children.length;}
this.addWidgetAsDirectChild(_59f,_5a0,pos,ref,_5a3);this.registerChild(_59f,_5a3);return _59f;},addWidgetAsDirectChild:function(_5a4,_5a5,pos,ref,_5a8){if((!this.containerNode)&&(!_5a5)){this.containerNode=this.domNode;}
var cn=(_5a5)?_5a5:this.containerNode;if(!pos){pos="after";}
if(!ref){if(!cn){cn=dojo.body();}
ref=cn.lastChild;}
if(!_5a8){_5a8=0;}
_5a4.domNode.setAttribute("dojoinsertionindex",_5a8);if(!ref){cn.appendChild(_5a4.domNode);}else{if(pos=="insertAtIndex"){dojo.dom.insertAtIndex(_5a4.domNode,ref.parentNode,_5a8);}else{if((pos=="after")&&(ref===cn.lastChild)){cn.appendChild(_5a4.domNode);}else{dojo.dom.insertAtPosition(_5a4.domNode,cn,pos);}}
}},registerChild:function(_5aa,_5ab){_5aa.dojoInsertionIndex=_5ab;var idx=-1;for(var i=0;i<this.children.length;i++){if(this.children[i].dojoInsertionIndex<=_5ab){idx=i;}}
this.children.splice(idx+1,0,_5aa);_5aa.parent=this;_5aa.addedTo(this,idx+1);delete dojo.widget.manager.topWidgets[_5aa.widgetId];},removeChild:function(_5ae){dojo.dom.removeNode(_5ae.domNode);return dojo.widget.DomWidget.superclass.removeChild.call(this,_5ae);},getFragNodeRef:function(frag){if(!frag){return null;}
if(!frag[this.getNamespacedType()]){dojo.raise("Error: no frag for widget type "+this.getNamespacedType()+", id "+this.widgetId+" (maybe a widget has set it's type incorrectly)");}
return frag[this.getNamespacedType()]["nodeRef"];},postInitialize:function(args,frag,_5b2){var _5b3=this.getFragNodeRef(frag);if(_5b2&&(_5b2.snarfChildDomOutput||!_5b3)){_5b2.addWidgetAsDirectChild(this,"","insertAtIndex","",args["dojoinsertionindex"],_5b3);}else{if(_5b3){if(this.domNode&&(this.domNode!==_5b3)){this._sourceNodeRef=dojo.dom.replaceNode(_5b3,this.domNode);}}
}
if(_5b2){_5b2.registerChild(this,args.dojoinsertionindex);}else{dojo.widget.manager.topWidgets[this.widgetId]=this;}
if(this.widgetsInTemplate){var _5b4=new dojo.xml.Parse();var _5b5;var _5b6=this.domNode.getElementsByTagName("*");for(var i=0;i<_5b6.length;i++){if(_5b6[i].getAttribute("dojoAttachPoint")=="subContainerWidget"){_5b5=_5b6[i];}
if(_5b6[i].getAttribute("dojoType")){_5b6[i].setAttribute("isSubWidget",true);}}
if(this.isContainer&&!this.containerNode){if(_5b5){var src=this.getFragNodeRef(frag);if(src){dojo.dom.moveChildren(src,_5b5);frag["dojoDontFollow"]=true;}}else{dojo.debug("No subContainerWidget node can be found in template file for widget "+this);}}
var _5b9=_5b4.parseElement(this.domNode,null,true);dojo.widget.getParser().createSubComponents(_5b9,this);var _5ba=[];var _5bb=[this];var w;while((w=_5bb.pop())){for(var i=0;i<w.children.length;i++){var _5bd=w.children[i];if(_5bd._processedSubWidgets||!_5bd.extraArgs["issubwidget"]){continue;}
_5ba.push(_5bd);if(_5bd.isContainer){_5bb.push(_5bd);}}
}
for(var i=0;i<_5ba.length;i++){var _5be=_5ba[i];if(_5be._processedSubWidgets){dojo.debug("This should not happen: widget._processedSubWidgets is already true!");return;}
_5be._processedSubWidgets=true;if(_5be.extraArgs["dojoattachevent"]){var evts=_5be.extraArgs["dojoattachevent"].split(";");for(var j=0;j<evts.length;j++){var _5c1=null;var tevt=dojo.string.trim(evts[j]);if(tevt.indexOf(":")>=0){var _5c3=tevt.split(":");tevt=dojo.string.trim(_5c3[0]);_5c1=dojo.string.trim(_5c3[1]);}
if(!_5c1){_5c1=tevt;}
if(dojo.lang.isFunction(_5be[tevt])){dojo.event.kwConnect({srcObj:_5be,srcFunc:tevt,targetObj:this,targetFunc:_5c1});}else{alert(tevt+" is not a function in widget "+_5be);}}
}
if(_5be.extraArgs["dojoattachpoint"]){this[_5be.extraArgs["dojoattachpoint"]]=_5be;}}
}
if(this.isContainer&&!frag["dojoDontFollow"]){dojo.widget.getParser().createSubComponents(frag,this);}},buildRendering:function(args,frag){var ts=dojo.widget._templateCache[this.widgetType];if(args["templatecsspath"]){args["templateCssPath"]=args["templatecsspath"];}
var _5c7=args["templateCssPath"]||this.templateCssPath;if(_5c7&&!dojo.widget._cssFiles[_5c7.toString()]){if((!this.templateCssString)&&(_5c7)){this.templateCssString=dojo.hostenv.getText(_5c7);this.templateCssPath=null;}
dojo.widget._cssFiles[_5c7.toString()]=true;}
if((this["templateCssString"])&&(!dojo.widget._cssStrings[this.templateCssString])){dojo.html.insertCssText(this.templateCssString,null,_5c7);dojo.widget._cssStrings[this.templateCssString]=true;}
if((!this.preventClobber)&&((this.templatePath)||(this.templateNode)||((this["templateString"])&&(this.templateString.length))||((typeof ts!="undefined")&&((ts["string"])||(ts["node"]))))){this.buildFromTemplate(args,frag);}else{this.domNode=this.getFragNodeRef(frag);}
this.fillInTemplate(args,frag);},buildFromTemplate:function(args,frag){var _5ca=false;if(args["templatepath"]){args["templatePath"]=args["templatepath"];}
dojo.widget.fillFromTemplateCache(this,args["templatePath"],null,_5ca);var ts=dojo.widget._templateCache[this.templatePath?this.templatePath.toString():this.widgetType];if((ts)&&(!_5ca)){if(!this.templateString.length){this.templateString=ts["string"];}
if(!this.templateNode){this.templateNode=ts["node"];}}
var _5cc=false;var node=null;var tstr=this.templateString;if((!this.templateNode)&&(this.templateString)){_5cc=this.templateString.match(/\$\{([^\}]+)\}/g);if(_5cc){var hash=this.strings||{};for(var key in dojo.widget.defaultStrings){if(dojo.lang.isUndefined(hash[key])){hash[key]=dojo.widget.defaultStrings[key];}}
for(var i=0;i<_5cc.length;i++){var key=_5cc[i];key=key.substring(2,key.length-1);var kval=(key.substring(0,5)=="this.")?dojo.getObject(key.substring(5),false,this):hash[key];var _5d3;if((kval)||(dojo.lang.isString(kval))){_5d3=new String((dojo.lang.isFunction(kval))?kval.call(this,key,this.templateString):kval);while(_5d3.indexOf("\"")>-1){_5d3=_5d3.replace("\"","&quot;");}
tstr=tstr.replace(_5cc[i],_5d3);}}
}else{this.templateNode=this.createNodesFromText(this.templateString,true)[0];if(!_5ca){ts.node=this.templateNode;}}
}
if((!this.templateNode)&&(!_5cc)){dojo.debug("DomWidget.buildFromTemplate: could not create template");return false;}else{if(!_5cc){node=this.templateNode.cloneNode(true);if(!node){return false;}}else{node=this.createNodesFromText(tstr,true)[0];}}
this.domNode=node;this.attachTemplateNodes();if(this.isContainer&&this.containerNode){var src=this.getFragNodeRef(frag);if(src){dojo.dom.moveChildren(src,this.containerNode);}}
},attachTemplateNodes:function(_5d5,_5d6){if(!_5d5){_5d5=this.domNode;}
if(!_5d6){_5d6=this;}
return dojo.widget.attachTemplateNodes(_5d5,_5d6,dojo.widget.getDojoEventsFromStr(this.templateString));},fillInTemplate:function(){},destroyRendering:function(){try{dojo.dom.destroyNode(this.domNode);delete this.domNode;}
catch(e){}
if(this._sourceNodeRef){try{dojo.dom.destroyNode(this._sourceNodeRef);}
catch(e){}}
},createNodesFromText:function(){dojo.unimplemented("dojo.widget.DomWidget.createNodesFromText");}});dojo.html._toggle=function(node,_5d8,_5d9){node=dojo.byId(node);_5d9(node,!_5d8(node));return _5d8(node);};dojo.html.show=function(node){node=dojo.byId(node);if(dojo.html.getStyleProperty(node,"display")=="none"){var _5db=dojo.html.getAttribute("djDisplayCache");dojo.html.setStyle(node,"display",(_5db||""));node.removeAttribute("djDisplayCache");}};dojo.html.hide=function(node){node=dojo.byId(node);var _5dd=dojo.html.getAttribute("djDisplayCache");if(_5dd==null){var d=dojo.html.getStyleProperty(node,"display");if(d!="none"){node.setAttribute("djDisplayCache",d);}}
dojo.html.setStyle(node,"display","none");};dojo.html.setShowing=function(node,_5e0){dojo.html[(_5e0?"show":"hide")](node);};dojo.html.isShowing=function(node){return (dojo.html.getStyleProperty(node,"display")!="none");};dojo.html.toggleShowing=function(node){return dojo.html._toggle(node,dojo.html.isShowing,dojo.html.setShowing);};dojo.html.displayMap={tr:"",td:"",th:"",img:"inline",span:"inline",input:"inline",button:"inline"};dojo.html.suggestDisplayByTagName=function(node){node=dojo.byId(node);if(node&&node.tagName){var tag=node.tagName.toLowerCase();return (tag in dojo.html.displayMap?dojo.html.displayMap[tag]:"block");}};dojo.html.setDisplay=function(node,_5e6){dojo.html.setStyle(node,"display",((_5e6 instanceof String||typeof _5e6=="string")?_5e6:(_5e6?dojo.html.suggestDisplayByTagName(node):"none")));};dojo.html.isDisplayed=function(node){return (dojo.html.getComputedStyle(node,"display")!="none");};dojo.html.toggleDisplay=function(node){return dojo.html._toggle(node,dojo.html.isDisplayed,dojo.html.setDisplay);};dojo.html.setVisibility=function(node,_5ea){dojo.html.setStyle(node,"visibility",((_5ea instanceof String||typeof _5ea=="string")?_5ea:(_5ea?"visible":"hidden")));};dojo.html.isVisible=function(node){return (dojo.html.getComputedStyle(node,"visibility")!="hidden");};dojo.html.toggleVisibility=function(node){return dojo.html._toggle(node,dojo.html.isVisible,dojo.html.setVisibility);};dojo.html.setOpacity=function(node,_5ee,_5ef){node=dojo.byId(node);var h=dojo.render.html;if(!_5ef){if(_5ee>=1){if(h.ie){dojo.html.clearOpacity(node);return;}else{_5ee=0.999999;}}else{if(_5ee<0){_5ee=0;}}
}
if(h.ie){if(node.nodeName.toLowerCase()=="tr"){var tds=node.getElementsByTagName("td");for(var x=0;x<tds.length;x++){tds[x].style.filter="Alpha(Opacity="+_5ee*100+")";}}
node.style.filter="Alpha(Opacity="+_5ee*100+")";}else{if(h.moz){node.style.opacity=_5ee;node.style.MozOpacity=_5ee;}else{if(h.safari){node.style.opacity=_5ee;node.style.KhtmlOpacity=_5ee;}else{node.style.opacity=_5ee;}}
}};dojo.html.clearOpacity=function(node){node=dojo.byId(node);var ns=node.style;var h=dojo.render.html;if(h.ie){try{if(node.filters&&node.filters.alpha){ns.filter="";}}
catch(e){}}else{if(h.moz){ns.opacity=1;ns.MozOpacity=1;}else{if(h.safari){ns.opacity=1;ns.KhtmlOpacity=1;}else{ns.opacity=1;}}
}};dojo.html.getOpacity=function(node){node=dojo.byId(node);var h=dojo.render.html;if(h.ie){var opac=(node.filters&&node.filters.alpha&&typeof node.filters.alpha.opacity=="number"?node.filters.alpha.opacity:100)/100;}else{var opac=node.style.opacity||node.style.MozOpacity||node.style.KhtmlOpacity||1;}
return opac>=0.999999?1:Number(opac);};dojo.html.sumAncestorProperties=function(node,prop){node=dojo.byId(node);if(!node){return 0;}
var _5fb=0;while(node){if(dojo.html.getComputedStyle(node,"position")=="fixed"){return 0;}
var val=node[prop];if(val){_5fb+=val-0;if(node==dojo.body()){break;}}
node=node.parentNode;}
return _5fb;};dojo.html.setStyleAttributes=function(node,_5fe){node=dojo.byId(node);var _5ff=_5fe.replace(/(;)?\s*$/,"").split(";");for(var i=0;i<_5ff.length;i++){var _601=_5ff[i].split(":");var name=_601[0].replace(/\s*$/,"").replace(/^\s*/,"").toLowerCase();var _603=_601[1].replace(/\s*$/,"").replace(/^\s*/,"");switch(name){case "opacity":
dojo.html.setOpacity(node,_603);break;case "content-height":
dojo.html.setContentBox(node,{height:_603});break;case "content-width":
dojo.html.setContentBox(node,{width:_603});break;case "outer-height":
dojo.html.setMarginBox(node,{height:_603});break;case "outer-width":
dojo.html.setMarginBox(node,{width:_603});break;default:
node.style[dojo.html.toCamelCase(name)]=_603;}}
};dojo.html.boxSizing={MARGIN_BOX:"margin-box",BORDER_BOX:"border-box",PADDING_BOX:"padding-box",CONTENT_BOX:"content-box"};dojo.html.getAbsolutePosition=dojo.html.abs=function(node,_605,_606){node=dojo.byId(node);var _607=dojo.doc();var ret={x:0,y:0};var bs=dojo.html.boxSizing;if(!_606){_606=bs.CONTENT_BOX;}
var _60a=2;var _60b;switch(_606){case bs.MARGIN_BOX:
_60b=3;break;case bs.BORDER_BOX:
_60b=2;break;case bs.PADDING_BOX:
default:
_60b=1;break;case bs.CONTENT_BOX:
_60b=0;break;}
var h=dojo.render.html;var db=_607["body"]||_607["documentElement"];if(h.ie){with(node.getBoundingClientRect()){ret.x=left-2;ret.y=top-2;}}else{if(_607["getBoxObjectFor"]){_60a=1;try{var bo=_607.getBoxObjectFor(node);ret.x=bo.x-dojo.html.sumAncestorProperties(node,"scrollLeft");ret.y=bo.y-dojo.html.sumAncestorProperties(node,"scrollTop");}
catch(e){}}else{if(node["offsetParent"]){var _60f;if((h.safari)&&(node.style.getPropertyValue("position")=="absolute")&&(node.parentNode==db)){_60f=db;}else{_60f=db.parentNode;}
if(node.parentNode!=db){var nd=node;if(dojo.render.html.opera){nd=db;}
ret.x-=dojo.html.sumAncestorProperties(nd,"scrollLeft");ret.y-=dojo.html.sumAncestorProperties(nd,"scrollTop");}
var _611=node;do{var n=_611["offsetLeft"];if(!h.opera||n>0){ret.x+=isNaN(n)?0:n;}
var m=_611["offsetTop"];ret.y+=isNaN(m)?0:m;_611=_611.offsetParent;}while((_611!=_60f)&&(_611!=null));}else{if(node["x"]&&node["y"]){ret.x+=isNaN(node.x)?0:node.x;ret.y+=isNaN(node.y)?0:node.y;}}
}}
if(_605){var _614=dojo.html.getScroll();ret.y+=_614.top;ret.x+=_614.left;}
var _615=[dojo.html.getPaddingExtent,dojo.html.getBorderExtent,dojo.html.getMarginExtent];if(_60a>_60b){for(var i=_60b;i<_60a;++i){ret.y+=_615[i](node,"top");ret.x+=_615[i](node,"left");}}else{if(_60a<_60b){for(var i=_60b;i>_60a;--i){ret.y-=_615[i-1](node,"top");ret.x-=_615[i-1](node,"left");}}
}
ret.top=ret.y;ret.left=ret.x;return ret;};dojo.html.isPositionAbsolute=function(node){return (dojo.html.getComputedStyle(node,"position")=="absolute");};dojo.html._getComponentPixelValues=function(node,_619,_61a,_61b){var _61c=["top","bottom","left","right"];var obj={};for(var i in _61c){side=_61c[i];obj[side]=_61a(node,_619+side,_61b);}
obj.width=obj.left+obj.right;obj.height=obj.top+obj.bottom;return obj;};dojo.html.getMargin=function(node){return dojo.html._getComponentPixelValues(node,"margin-",dojo.html.getPixelValue,dojo.html.isPositionAbsolute(node));};dojo.html.getBorder=function(node){return dojo.html._getComponentPixelValues(node,"",dojo.html.getBorderExtent);};dojo.html.getBorderExtent=function(node,side){return (dojo.html.getStyle(node,"border-"+side+"-style")=="none"?0:dojo.html.getPixelValue(node,"border-"+side+"-width"));};dojo.html.getMarginExtent=function(node,side){return dojo.html.getPixelValue(node,"margin-"+side,dojo.html.isPositionAbsolute(node));};dojo.html.getPaddingExtent=function(node,side){return dojo.html.getPixelValue(node,"padding-"+side,true);};dojo.html.getPadding=function(node){return dojo.html._getComponentPixelValues(node,"padding-",dojo.html.getPixelValue,true);};dojo.html.getPadBorder=function(node){var pad=dojo.html.getPadding(node);var _62a=dojo.html.getBorder(node);return {width:pad.width+_62a.width,height:pad.height+_62a.height};};dojo.html.getBoxSizing=function(node){var h=dojo.render.html;var bs=dojo.html.boxSizing;if(((h.ie)||(h.opera))&&node.nodeName!="IMG"){var cm=document["compatMode"];if((cm=="BackCompat")||(cm=="QuirksMode")){return bs.BORDER_BOX;}else{return bs.CONTENT_BOX;}}else{if(arguments.length==0){node=document.documentElement;}
var _62f=dojo.html.getStyle(node,"-moz-box-sizing");if(!_62f){_62f=dojo.html.getStyle(node,"box-sizing");}
return (_62f?_62f:bs.CONTENT_BOX);}};dojo.html.isBorderBox=function(node){return (dojo.html.getBoxSizing(node)==dojo.html.boxSizing.BORDER_BOX);};dojo.html.getBorderBox=function(node){node=dojo.byId(node);return {width:node.offsetWidth,height:node.offsetHeight};};dojo.html.getPaddingBox=function(node){var box=dojo.html.getBorderBox(node);var _634=dojo.html.getBorder(node);return {width:box.width-_634.width,height:box.height-_634.height};};dojo.html.getContentBox=function(node){node=dojo.byId(node);var _636=dojo.html.getPadBorder(node);return {width:node.offsetWidth-_636.width,height:node.offsetHeight-_636.height};};dojo.html.setContentBox=function(node,args){node=dojo.byId(node);var _639=0;var _63a=0;var isbb=dojo.html.isBorderBox(node);var _63c=(isbb?dojo.html.getPadBorder(node):{width:0,height:0});var ret={};if(typeof args.width!="undefined"){_639=args.width+_63c.width;ret.width=dojo.html.setPositivePixelValue(node,"width",_639);}
if(typeof args.height!="undefined"){_63a=args.height+_63c.height;ret.height=dojo.html.setPositivePixelValue(node,"height",_63a);}
return ret;};dojo.html.getMarginBox=function(node){var _63f=dojo.html.getBorderBox(node);var _640=dojo.html.getMargin(node);return {width:_63f.width+_640.width,height:_63f.height+_640.height};};dojo.html.setMarginBox=function(node,args){node=dojo.byId(node);var _643=0;var _644=0;var isbb=dojo.html.isBorderBox(node);var _646=(!isbb?dojo.html.getPadBorder(node):{width:0,height:0});var _647=dojo.html.getMargin(node);var ret={};if(typeof args.width!="undefined"){_643=args.width-_646.width;_643-=_647.width;ret.width=dojo.html.setPositivePixelValue(node,"width",_643);}
if(typeof args.height!="undefined"){_644=args.height-_646.height;_644-=_647.height;ret.height=dojo.html.setPositivePixelValue(node,"height",_644);}
return ret;};dojo.html.getElementBox=function(node,type){var bs=dojo.html.boxSizing;switch(type){case bs.MARGIN_BOX:
return dojo.html.getMarginBox(node);case bs.BORDER_BOX:
return dojo.html.getBorderBox(node);case bs.PADDING_BOX:
return dojo.html.getPaddingBox(node);case bs.CONTENT_BOX:
default:
return dojo.html.getContentBox(node);}};dojo.html.toCoordinateObject=dojo.html.toCoordinateArray=function(_64c,_64d,_64e){if(!_64c.nodeType&&!(_64c instanceof String||typeof _64c=="string")&&("width" in _64c||"height" in _64c||"left" in _64c||"x" in _64c||"top" in _64c||"y" in _64c)){var ret={left:_64c.left||_64c.x||0,top:_64c.top||_64c.y||0,width:_64c.width||0,height:_64c.height||0};}else{var node=dojo.byId(_64c);var pos=dojo.html.abs(node,_64d,_64e);var _652=dojo.html.getMarginBox(node);var ret={left:pos.left,top:pos.top,width:_652.width,height:_652.height};}
ret.x=ret.left;ret.y=ret.top;return ret;};dojo.html.setMarginBoxWidth=dojo.html.setOuterWidth=function(node,_654){return dojo.html._callDeprecated("setMarginBoxWidth","setMarginBox",arguments,"width");};dojo.html.setMarginBoxHeight=dojo.html.setOuterHeight=function(){return dojo.html._callDeprecated("setMarginBoxHeight","setMarginBox",arguments,"height");};dojo.html.getMarginBoxWidth=dojo.html.getOuterWidth=function(){return dojo.html._callDeprecated("getMarginBoxWidth","getMarginBox",arguments,null,"width");};dojo.html.getMarginBoxHeight=dojo.html.getOuterHeight=function(){return dojo.html._callDeprecated("getMarginBoxHeight","getMarginBox",arguments,null,"height");};dojo.html.getTotalOffset=function(node,type,_657){return dojo.html._callDeprecated("getTotalOffset","getAbsolutePosition",arguments,null,type);};dojo.html.getAbsoluteX=function(node,_659){return dojo.html._callDeprecated("getAbsoluteX","getAbsolutePosition",arguments,null,"x");};dojo.html.getAbsoluteY=function(node,_65b){return dojo.html._callDeprecated("getAbsoluteY","getAbsolutePosition",arguments,null,"y");};dojo.html.totalOffsetLeft=function(node,_65d){return dojo.html._callDeprecated("totalOffsetLeft","getAbsolutePosition",arguments,null,"left");};dojo.html.totalOffsetTop=function(node,_65f){return dojo.html._callDeprecated("totalOffsetTop","getAbsolutePosition",arguments,null,"top");};dojo.html.getMarginWidth=function(node){return dojo.html._callDeprecated("getMarginWidth","getMargin",arguments,null,"width");};dojo.html.getMarginHeight=function(node){return dojo.html._callDeprecated("getMarginHeight","getMargin",arguments,null,"height");};dojo.html.getBorderWidth=function(node){return dojo.html._callDeprecated("getBorderWidth","getBorder",arguments,null,"width");};dojo.html.getBorderHeight=function(node){return dojo.html._callDeprecated("getBorderHeight","getBorder",arguments,null,"height");};dojo.html.getPaddingWidth=function(node){return dojo.html._callDeprecated("getPaddingWidth","getPadding",arguments,null,"width");};dojo.html.getPaddingHeight=function(node){return dojo.html._callDeprecated("getPaddingHeight","getPadding",arguments,null,"height");};dojo.html.getPadBorderWidth=function(node){return dojo.html._callDeprecated("getPadBorderWidth","getPadBorder",arguments,null,"width");};dojo.html.getPadBorderHeight=function(node){return dojo.html._callDeprecated("getPadBorderHeight","getPadBorder",arguments,null,"height");};dojo.html.getBorderBoxWidth=dojo.html.getInnerWidth=function(){return dojo.html._callDeprecated("getBorderBoxWidth","getBorderBox",arguments,null,"width");};dojo.html.getBorderBoxHeight=dojo.html.getInnerHeight=function(){return dojo.html._callDeprecated("getBorderBoxHeight","getBorderBox",arguments,null,"height");};dojo.html.getContentBoxWidth=dojo.html.getContentWidth=function(){return dojo.html._callDeprecated("getContentBoxWidth","getContentBox",arguments,null,"width");};dojo.html.getContentBoxHeight=dojo.html.getContentHeight=function(){return dojo.html._callDeprecated("getContentBoxHeight","getContentBox",arguments,null,"height");};dojo.html.setContentBoxWidth=dojo.html.setContentWidth=function(node,_669){return dojo.html._callDeprecated("setContentBoxWidth","setContentBox",arguments,"width");};dojo.html.setContentBoxHeight=dojo.html.setContentHeight=function(node,_66b){return dojo.html._callDeprecated("setContentBoxHeight","setContentBox",arguments,"height");};dojo.html.getElementWindow=function(_66c){return dojo.html.getDocumentWindow(_66c.ownerDocument);};dojo.html.getDocumentWindow=function(doc){if(dojo.render.html.safari&&!doc._parentWindow){var fix=function(win){win.document._parentWindow=win;for(var i=0;i<win.frames.length;i++){fix(win.frames[i]);}};fix(window.top);}
if(dojo.render.html.ie&&window!==document.parentWindow&&!doc._parentWindow){doc.parentWindow.execScript("document._parentWindow = window;","Javascript");var win=doc._parentWindow;doc._parentWindow=null;return win;}
return doc._parentWindow||doc.parentWindow||doc.defaultView;};dojo.html.getAbsolutePositionExt=function(node,_673,_674,_675){var _676=dojo.html.getElementWindow(node);var ret=dojo.withGlobal(_676,"getAbsolutePosition",dojo.html,arguments);var win=dojo.html.getElementWindow(node);if(_675!=win&&win.frameElement){var ext=dojo.html.getAbsolutePositionExt(win.frameElement,_673,_674,_675);ret.x+=ext.x;ret.y+=ext.y;}
ret.top=ret.y;ret.left=ret.x;return ret;};dojo.html.gravity=function(node,e){node=dojo.byId(node);var _67c=dojo.html.getCursorPosition(e);with(dojo.html){var _67d=getAbsolutePosition(node,true);var bb=getBorderBox(node);var _67f=_67d.x+(bb.width/2);var _680=_67d.y+(bb.height/2);}
with(dojo.html.gravity){return ((_67c.x<_67f?WEST:EAST)|(_67c.y<_680?NORTH:SOUTH));}};dojo.html.gravity.NORTH=1;dojo.html.gravity.SOUTH=1<<1;dojo.html.gravity.EAST=1<<2;dojo.html.gravity.WEST=1<<3;dojo.html.overElement=function(_681,e){_681=dojo.byId(_681);var _683=dojo.html.getCursorPosition(e);var bb=dojo.html.getBorderBox(_681);var _685=dojo.html.getAbsolutePosition(_681,true,dojo.html.boxSizing.BORDER_BOX);var top=_685.y;var _687=top+bb.height;var left=_685.x;var _689=left+bb.width;return (_683.x>=left&&_683.x<=_689&&_683.y>=top&&_683.y<=_687);};dojo.html.renderedTextContent=function(node){node=dojo.byId(node);var _68b="";if(node==null){return _68b;}
for(var i=0;i<node.childNodes.length;i++){switch(node.childNodes[i].nodeType){case 1:
case 5:
var _68d="unknown";try{_68d=dojo.html.getStyle(node.childNodes[i],"display");}
catch(E){}
switch(_68d){case "block":
case "list-item":
case "run-in":
case "table":
case "table-row-group":
case "table-header-group":
case "table-footer-group":
case "table-row":
case "table-column-group":
case "table-column":
case "table-cell":
case "table-caption":
_68b+="\n";_68b+=dojo.html.renderedTextContent(node.childNodes[i]);_68b+="\n";break;case "none":
break;default:
if(node.childNodes[i].tagName&&node.childNodes[i].tagName.toLowerCase()=="br"){_68b+="\n";}else{_68b+=dojo.html.renderedTextContent(node.childNodes[i]);}
break;}
break;case 3:
case 2:
case 4:
var text=node.childNodes[i].nodeValue;var _68f="unknown";try{_68f=dojo.html.getStyle(node,"text-transform");}
catch(E){}
switch(_68f){case "capitalize":
var _690=text.split(" ");for(var i=0;i<_690.length;i++){_690[i]=_690[i].charAt(0).toUpperCase()+_690[i].substring(1);}
text=_690.join(" ");break;case "uppercase":
text=text.toUpperCase();break;case "lowercase":
text=text.toLowerCase();break;default:
break;}
switch(_68f){case "nowrap":
break;case "pre-wrap":
break;case "pre-line":
break;case "pre":
break;default:
text=text.replace(/\s+/," ");if(/\s$/.test(_68b)){text.replace(/^\s/,"");}
break;}
_68b+=text;break;default:
break;}}
return _68b;};dojo.html.createNodesFromText=function(txt,trim){if(trim){txt=txt.replace(/^\s+|\s+$/g,"");}
var tn=dojo.doc().createElement("div");tn.style.visibility="hidden";dojo.body().appendChild(tn);var _694="none";if((/^<t[dh][\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table><tbody><tr>"+txt+"</tr></tbody></table>";_694="cell";}else{if((/^<tr[\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table><tbody>"+txt+"</tbody></table>";_694="row";}else{if((/^<(thead|tbody|tfoot)[\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table>"+txt+"</table>";_694="section";}}
}
tn.innerHTML=txt;if(tn["normalize"]){tn.normalize();}
var _695=null;switch(_694){case "cell":
_695=tn.getElementsByTagName("tr")[0];break;case "row":
_695=tn.getElementsByTagName("tbody")[0];break;case "section":
_695=tn.getElementsByTagName("table")[0];break;default:
_695=tn;break;}
var _696=[];for(var x=0;x<_695.childNodes.length;x++){_696.push(_695.childNodes[x].cloneNode(true));}
tn.style.display="none";dojo.html.destroyNode(tn);return _696;};dojo.html.placeOnScreen=function(node,_699,_69a,_69b,_69c,_69d,_69e){if(_699 instanceof Array||typeof _699=="array"){_69e=_69d;_69d=_69c;_69c=_69b;_69b=_69a;_69a=_699[1];_699=_699[0];}
if(_69d instanceof String||typeof _69d=="string"){_69d=_69d.split(",");}
if(!isNaN(_69b)){_69b=[Number(_69b),Number(_69b)];}else{if(!(_69b instanceof Array||typeof _69b=="array")){_69b=[0,0];}}
var _69f=dojo.html.getScroll().offset;var view=dojo.html.getViewport();node=dojo.byId(node);var _6a1=node.style.display;node.style.display="";var bb=dojo.html.getBorderBox(node);var w=bb.width;var h=bb.height;node.style.display=_6a1;if(!(_69d instanceof Array||typeof _69d=="array")){_69d=["TL"];}
var _6a5,_6a6,_6a7=Infinity,_6a8;for(var _6a9=0;_6a9<_69d.length;++_6a9){var _6aa=_69d[_6a9];var _6ab=true;var tryX=_699-(_6aa.charAt(1)=="L"?0:w)+_69b[0]*(_6aa.charAt(1)=="L"?1:-1);var tryY=_69a-(_6aa.charAt(0)=="T"?0:h)+_69b[1]*(_6aa.charAt(0)=="T"?1:-1);if(_69c){tryX-=_69f.x;tryY-=_69f.y;}
if(tryX<0){tryX=0;_6ab=false;}
if(tryY<0){tryY=0;_6ab=false;}
var x=tryX+w;if(x>view.width){x=view.width-w;_6ab=false;}else{x=tryX;}
x=Math.max(_69b[0],x)+_69f.x;var y=tryY+h;if(y>view.height){y=view.height-h;_6ab=false;}else{y=tryY;}
y=Math.max(_69b[1],y)+_69f.y;if(_6ab){_6a5=x;_6a6=y;_6a7=0;_6a8=_6aa;break;}else{var dist=Math.pow(x-tryX-_69f.x,2)+Math.pow(y-tryY-_69f.y,2);if(_6a7>dist){_6a7=dist;_6a5=x;_6a6=y;_6a8=_6aa;}}
}
if(!_69e){node.style.left=_6a5+"px";node.style.top=_6a6+"px";}
return {left:_6a5,top:_6a6,x:_6a5,y:_6a6,dist:_6a7,corner:_6a8};};dojo.html.placeOnScreenAroundElement=function(node,_6b2,_6b3,_6b4,_6b5,_6b6){var best,_6b8=Infinity;_6b2=dojo.byId(_6b2);var _6b9=_6b2.style.display;_6b2.style.display="";var mb=dojo.html.getElementBox(_6b2,_6b4);var _6bb=mb.width;var _6bc=mb.height;var _6bd=dojo.html.getAbsolutePosition(_6b2,true,_6b4);_6b2.style.display=_6b9;for(var _6be in _6b5){var pos,_6c0,_6c1;var _6c2=_6b5[_6be];_6c0=_6bd.x+(_6be.charAt(1)=="L"?0:_6bb);_6c1=_6bd.y+(_6be.charAt(0)=="T"?0:_6bc);pos=dojo.html.placeOnScreen(node,_6c0,_6c1,_6b3,true,_6c2,true);if(pos.dist==0){best=pos;break;}else{if(_6b8>pos.dist){_6b8=pos.dist;best=pos;}}
}
if(!_6b6){node.style.left=best.left+"px";node.style.top=best.top+"px";}
return best;};dojo.html.scrollIntoView=function(node){if(!node){return;}
if(dojo.render.html.ie){if(dojo.html.getBorderBox(node.parentNode).height<=node.parentNode.scrollHeight){node.scrollIntoView(false);}}else{if(dojo.render.html.mozilla){node.scrollIntoView(false);}else{var _6c4=node.parentNode;var _6c5=_6c4.scrollTop+dojo.html.getBorderBox(_6c4).height;var _6c6=node.offsetTop+dojo.html.getMarginBox(node).height;if(_6c5<_6c6){_6c4.scrollTop+=(_6c6-_6c5);}else{if(_6c4.scrollTop>node.offsetTop){_6c4.scrollTop-=(_6c4.scrollTop-node.offsetTop);}}
}}
};dojo.gfx.color.Color=function(r,g,b,a){if(dojo.lang.isArray(r)){this.r=r[0];this.g=r[1];this.b=r[2];this.a=r[3]||1;}else{if(dojo.lang.isString(r)){var rgb=dojo.gfx.color.extractRGB(r);this.r=rgb[0];this.g=rgb[1];this.b=rgb[2];this.a=g||1;}else{if(r instanceof dojo.gfx.color.Color){this.r=r.r;this.b=r.b;this.g=r.g;this.a=r.a;}else{this.r=r;this.g=g;this.b=b;this.a=a;}}
}};dojo.gfx.color.Color.fromArray=function(arr){return new dojo.gfx.color.Color(arr[0],arr[1],arr[2],arr[3]);};dojo.extend(dojo.gfx.color.Color,{toRgb:function(_6cd){if(_6cd){return this.toRgba();}else{return [this.r,this.g,this.b];}},toRgba:function(){return [this.r,this.g,this.b,this.a];},toHex:function(){return dojo.gfx.color.rgb2hex(this.toRgb());},toCss:function(){return "rgb("+this.toRgb().join()+")";},toString:function(){return this.toHex();},blend:function(_6ce,_6cf){var rgb=null;if(dojo.lang.isArray(_6ce)){rgb=_6ce;}else{if(_6ce instanceof dojo.gfx.color.Color){rgb=_6ce.toRgb();}else{rgb=new dojo.gfx.color.Color(_6ce).toRgb();}}
return dojo.gfx.color.blend(this.toRgb(),rgb,_6cf);}});dojo.gfx.color.named={white:[255,255,255],black:[0,0,0],red:[255,0,0],green:[0,255,0],lime:[0,255,0],blue:[0,0,255],navy:[0,0,128],gray:[128,128,128],silver:[192,192,192]};dojo.gfx.color.blend=function(a,b,_6d3){if(typeof a=="string"){return dojo.gfx.color.blendHex(a,b,_6d3);}
if(!_6d3){_6d3=0;}
_6d3=Math.min(Math.max(-1,_6d3),1);_6d3=((_6d3+1)/2);var c=[];for(var x=0;x<3;x++){c[x]=parseInt(b[x]+((a[x]-b[x])*_6d3));}
return c;};dojo.gfx.color.blendHex=function(a,b,_6d8){return dojo.gfx.color.rgb2hex(dojo.gfx.color.blend(dojo.gfx.color.hex2rgb(a),dojo.gfx.color.hex2rgb(b),_6d8));};dojo.gfx.color.extractRGB=function(_6d9){_6d9=_6d9.toLowerCase();if(_6d9.indexOf("rgb")==0){var _6da=_6d9.match(/rgba*\((\d+), *(\d+), *(\d+)/i);var ret=_6da.splice(1,3);return ret;}else{var _6dc=dojo.gfx.color.hex2rgb(_6d9);if(_6dc){return _6dc;}else{return dojo.gfx.color.named[_6d9]||[255,255,255];}}
};dojo.gfx.color.hex2rgb=function(hex){var _6de="0123456789ABCDEF";var rgb=new Array(3);if(hex.indexOf("#")==0){hex=hex.substring(1);}
hex=hex.toUpperCase();if(hex.replace(new RegExp("["+_6de+"]","g"),"")!=""){return null;}
if(hex.length==3){rgb[0]=hex.charAt(0)+hex.charAt(0);rgb[1]=hex.charAt(1)+hex.charAt(1);rgb[2]=hex.charAt(2)+hex.charAt(2);}else{rgb[0]=hex.substring(0,2);rgb[1]=hex.substring(2,4);rgb[2]=hex.substring(4);}
for(var i=0;i<rgb.length;i++){rgb[i]=_6de.indexOf(rgb[i].charAt(0))*16+_6de.indexOf(rgb[i].charAt(1));}
return rgb;};dojo.gfx.color.rgb2hex=function(r,g,b){if(dojo.lang.isArray(r)){g=r[1]||0;b=r[2]||0;r=r[0]||0;}
var ret=dojo.lang.map([r,g,b],function(x){x=new Number(x);var s=x.toString(16);while(s.length<2){s="0"+s;}
return s;});ret.unshift("#");return ret.join("");};dojo.lfx.Line=function(_6e7,end){this.start=_6e7;this.end=end;if(dojo.lang.isArray(_6e7)){var diff=[];dojo.lang.forEach(this.start,function(s,i){diff[i]=this.end[i]-s;},this);this.getValue=function(n){var res=[];dojo.lang.forEach(this.start,function(s,i){res[i]=(diff[i]*n)+s;},this);return res;};}else{var diff=end-_6e7;this.getValue=function(n){return (diff*n)+this.start;};}};dojo.lfx.easeDefault=function(n){if(dojo.render.html.khtml){return (parseFloat("0.5")+((Math.sin((n+parseFloat("1.5"))*Math.PI))/2));}else{return (0.5+((Math.sin((n+1.5)*Math.PI))/2));}};dojo.lfx.easeIn=function(n){return Math.pow(n,3);};dojo.lfx.easeOut=function(n){return (1-Math.pow(1-n,3));};dojo.lfx.easeInOut=function(n){return ((3*Math.pow(n,2))-(2*Math.pow(n,3)));};dojo.lfx.IAnimation=function(){};dojo.lang.extend(dojo.lfx.IAnimation,{curve:null,duration:1000,easing:null,repeatCount:0,rate:25,handler:null,beforeBegin:null,onBegin:null,onAnimate:null,onEnd:null,onPlay:null,onPause:null,onStop:null,play:null,pause:null,stop:null,connect:function(evt,_6f6,_6f7){if(!_6f7){_6f7=_6f6;_6f6=this;}
_6f7=dojo.lang.hitch(_6f6,_6f7);var _6f8=this[evt]||function(){};this[evt]=function(){var ret=_6f8.apply(this,arguments);_6f7.apply(this,arguments);return ret;};return this;},fire:function(evt,args){if(this[evt]){this[evt].apply(this,(args||[]));}
return this;},repeat:function(_6fc){this.repeatCount=_6fc;return this;},_active:false,_paused:false});dojo.lfx.Animation=function(_6fd,_6fe,_6ff,_700,_701,rate){dojo.lfx.IAnimation.call(this);if(dojo.lang.isNumber(_6fd)||(!_6fd&&_6fe.getValue)){rate=_701;_701=_700;_700=_6ff;_6ff=_6fe;_6fe=_6fd;_6fd=null;}else{if(_6fd.getValue||dojo.lang.isArray(_6fd)){rate=_700;_701=_6ff;_700=_6fe;_6ff=_6fd;_6fe=null;_6fd=null;}}
if(dojo.lang.isArray(_6ff)){this.curve=new dojo.lfx.Line(_6ff[0],_6ff[1]);}else{this.curve=_6ff;}
if(_6fe!=null&&_6fe>0){this.duration=_6fe;}
if(_701){this.repeatCount=_701;}
if(rate){this.rate=rate;}
if(_6fd){dojo.lang.forEach(["handler","beforeBegin","onBegin","onEnd","onPlay","onStop","onAnimate"],function(item){if(_6fd[item]){this.connect(item,_6fd[item]);}},this);}
if(_700&&dojo.lang.isFunction(_700)){this.easing=_700;}};dojo.inherits(dojo.lfx.Animation,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Animation,{_startTime:null,_endTime:null,_timer:null,_percent:0,_startRepeatCount:0,play:function(_704,_705){if(_705){clearTimeout(this._timer);this._active=false;this._paused=false;this._percent=0;}else{if(this._active&&!this._paused){return this;}}
this.fire("handler",["beforeBegin"]);this.fire("beforeBegin");if(_704>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_705);}),_704);return this;}
this._startTime=new Date().valueOf();if(this._paused){this._startTime-=(this.duration*this._percent/100);}
this._endTime=this._startTime+this.duration;this._active=true;this._paused=false;var step=this._percent/100;var _707=this.curve.getValue(step);if(this._percent==0){if(!this._startRepeatCount){this._startRepeatCount=this.repeatCount;}
this.fire("handler",["begin",_707]);this.fire("onBegin",[_707]);}
this.fire("handler",["play",_707]);this.fire("onPlay",[_707]);this._cycle();return this;},pause:function(){clearTimeout(this._timer);if(!this._active){return this;}
this._paused=true;var _708=this.curve.getValue(this._percent/100);this.fire("handler",["pause",_708]);this.fire("onPause",[_708]);return this;},gotoPercent:function(pct,_70a){clearTimeout(this._timer);this._active=true;this._paused=true;this._percent=pct;if(_70a){this.play();}
return this;},stop:function(_70b){clearTimeout(this._timer);var step=this._percent/100;if(_70b){step=1;}
var _70d=this.curve.getValue(step);this.fire("handler",["stop",_70d]);this.fire("onStop",[_70d]);this._active=false;this._paused=false;return this;},status:function(){if(this._active){return this._paused?"paused":"playing";}else{return "stopped";}
return this;},_cycle:function(){clearTimeout(this._timer);if(this._active){var curr=new Date().valueOf();var step=(curr-this._startTime)/(this._endTime-this._startTime);if(step>=1){step=1;this._percent=100;}else{this._percent=step*100;}
if((this.easing)&&(dojo.lang.isFunction(this.easing))){step=this.easing(step);}
var _710=this.curve.getValue(step);this.fire("handler",["animate",_710]);this.fire("onAnimate",[_710]);if(step<1){this._timer=setTimeout(dojo.lang.hitch(this,"_cycle"),this.rate);}else{this._active=false;this.fire("handler",["end"]);this.fire("onEnd");if(this.repeatCount>0){this.repeatCount--;this.play(null,true);}else{if(this.repeatCount==-1){this.play(null,true);}else{if(this._startRepeatCount){this.repeatCount=this._startRepeatCount;this._startRepeatCount=0;}}
}}
}
return this;}});dojo.lfx.Combine=function(_711){dojo.lfx.IAnimation.call(this);this._anims=[];this._animsEnded=0;var _712=arguments;if(_712.length==1&&(dojo.lang.isArray(_712[0])||dojo.lang.isArrayLike(_712[0]))){_712=_712[0];}
dojo.lang.forEach(_712,function(anim){this._anims.push(anim);anim.connect("onEnd",dojo.lang.hitch(this,"_onAnimsEnded"));},this);};dojo.inherits(dojo.lfx.Combine,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Combine,{_animsEnded:0,play:function(_714,_715){if(!this._anims.length){return this;}
this.fire("beforeBegin");if(_714>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_715);}),_714);return this;}
if(_715||this._anims[0].percent==0){this.fire("onBegin");}
this.fire("onPlay");this._animsCall("play",null,_715);return this;},pause:function(){this.fire("onPause");this._animsCall("pause");return this;},stop:function(_716){this.fire("onStop");this._animsCall("stop",_716);return this;},_onAnimsEnded:function(){this._animsEnded++;if(this._animsEnded>=this._anims.length){this.fire("onEnd");}
return this;},_animsCall:function(_717){var args=[];if(arguments.length>1){for(var i=1;i<arguments.length;i++){args.push(arguments[i]);}}
var _71a=this;dojo.lang.forEach(this._anims,function(anim){anim[_717](args);},_71a);return this;}});dojo.lfx.Chain=function(_71c){dojo.lfx.IAnimation.call(this);this._anims=[];this._currAnim=-1;var _71d=arguments;if(_71d.length==1&&(dojo.lang.isArray(_71d[0])||dojo.lang.isArrayLike(_71d[0]))){_71d=_71d[0];}
var _71e=this;dojo.lang.forEach(_71d,function(anim,i,_721){this._anims.push(anim);if(i<_721.length-1){anim.connect("onEnd",dojo.lang.hitch(this,"_playNext"));}else{anim.connect("onEnd",dojo.lang.hitch(this,function(){this.fire("onEnd");}));}},this);};dojo.inherits(dojo.lfx.Chain,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Chain,{_currAnim:-1,play:function(_722,_723){if(!this._anims.length){return this;}
if(_723||!this._anims[this._currAnim]){this._currAnim=0;}
var _724=this._anims[this._currAnim];this.fire("beforeBegin");if(_722>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_723);}),_722);return this;}
if(_724){if(this._currAnim==0){this.fire("handler",["begin",this._currAnim]);this.fire("onBegin",[this._currAnim]);}
this.fire("onPlay",[this._currAnim]);_724.play(null,_723);}
return this;},pause:function(){if(this._anims[this._currAnim]){this._anims[this._currAnim].pause();this.fire("onPause",[this._currAnim]);}
return this;},playPause:function(){if(this._anims.length==0){return this;}
if(this._currAnim==-1){this._currAnim=0;}
var _725=this._anims[this._currAnim];if(_725){if(!_725._active||_725._paused){this.play();}else{this.pause();}}
return this;},stop:function(){var _726=this._anims[this._currAnim];if(_726){_726.stop();this.fire("onStop",[this._currAnim]);}
return _726;},_playNext:function(){if(this._currAnim==-1||this._anims.length==0){return this;}
this._currAnim++;if(this._anims[this._currAnim]){this._anims[this._currAnim].play(null,true);}
return this;}});dojo.lfx.combine=function(_727){var _728=arguments;if(dojo.lang.isArray(arguments[0])){_728=arguments[0];}
if(_728.length==1){return _728[0];}
return new dojo.lfx.Combine(_728);};dojo.lfx.chain=function(_729){var _72a=arguments;if(dojo.lang.isArray(arguments[0])){_72a=arguments[0];}
if(_72a.length==1){return _72a[0];}
return new dojo.lfx.Chain(_72a);};dojo.html.getBackgroundColor=function(node){node=dojo.byId(node);var _72c;do{_72c=dojo.html.getStyle(node,"background-color");if(_72c.toLowerCase()=="rgba(0, 0, 0, 0)"){_72c="transparent";}
if(node==document.getElementsByTagName("body")[0]){node=null;break;}
node=node.parentNode;}while(node&&dojo.lang.inArray(["transparent",""],_72c));if(_72c=="transparent"){_72c=[255,255,255,0];}else{_72c=dojo.gfx.color.extractRGB(_72c);}
return _72c;};dojo.lfx.html._byId=function(_72d){if(!_72d){return [];}
if(dojo.lang.isArrayLike(_72d)){if(!_72d.alreadyChecked){var n=[];dojo.lang.forEach(_72d,function(node){n.push(dojo.byId(node));});n.alreadyChecked=true;return n;}else{return _72d;}}else{var n=[];n.push(dojo.byId(_72d));n.alreadyChecked=true;return n;}};dojo.lfx.html.propertyAnimation=function(_730,_731,_732,_733,_734){_730=dojo.lfx.html._byId(_730);var _735={"propertyMap":_731,"nodes":_730,"duration":_732,"easing":_733||dojo.lfx.easeDefault};var _736=function(args){if(args.nodes.length==1){var pm=args.propertyMap;if(!dojo.lang.isArray(args.propertyMap)){var parr=[];for(var _73a in pm){pm[_73a].property=_73a;parr.push(pm[_73a]);}
pm=args.propertyMap=parr;}
dojo.lang.forEach(pm,function(prop){if(dj_undef("start",prop)){if(prop.property!="opacity"){prop.start=parseInt(dojo.html.getComputedStyle(args.nodes[0],prop.property));}else{prop.start=dojo.html.getOpacity(args.nodes[0]);}}
});}};var _73c=function(_73d){var _73e=[];dojo.lang.forEach(_73d,function(c){_73e.push(Math.round(c));});return _73e;};var _740=function(n,_742){n=dojo.byId(n);if(!n||!n.style){return;}
for(var s in _742){try{if(s=="opacity"){dojo.html.setOpacity(n,_742[s]);}else{n.style[s]=_742[s];}}
catch(e){dojo.debug(e);}}
};var _744=function(_745){this._properties=_745;this.diffs=new Array(_745.length);dojo.lang.forEach(_745,function(prop,i){if(dojo.lang.isFunction(prop.start)){prop.start=prop.start(prop,i);}
if(dojo.lang.isFunction(prop.end)){prop.end=prop.end(prop,i);}
if(dojo.lang.isArray(prop.start)){this.diffs[i]=null;}else{if(prop.start instanceof dojo.gfx.color.Color){prop.startRgb=prop.start.toRgb();prop.endRgb=prop.end.toRgb();}else{this.diffs[i]=prop.end-prop.start;}}
},this);this.getValue=function(n){var ret={};dojo.lang.forEach(this._properties,function(prop,i){var _74c=null;if(dojo.lang.isArray(prop.start)){}else{if(prop.start instanceof dojo.gfx.color.Color){_74c=(prop.units||"rgb")+"(";for(var j=0;j<prop.startRgb.length;j++){_74c+=Math.round(((prop.endRgb[j]-prop.startRgb[j])*n)+prop.startRgb[j])+(j<prop.startRgb.length-1?",":"");}
_74c+=")";}else{_74c=((this.diffs[i])*n)+prop.start+(prop.property!="opacity"?prop.units||"px":"");}}
ret[dojo.html.toCamelCase(prop.property)]=_74c;},this);return ret;};};var anim=new dojo.lfx.Animation({beforeBegin:function(){_736(_735);anim.curve=new _744(_735.propertyMap);},onAnimate:function(_74f){dojo.lang.forEach(_735.nodes,function(node){_740(node,_74f);});}},_735.duration,null,_735.easing);if(_734){for(var x in _734){if(dojo.lang.isFunction(_734[x])){anim.connect(x,anim,_734[x]);}}
}
return anim;};dojo.lfx.html._makeFadeable=function(_752){var _753=function(node){if(dojo.render.html.ie){if((node.style.zoom.length==0)&&(dojo.html.getStyle(node,"zoom")=="normal")){node.style.zoom="1";}
if((node.style.width.length==0)&&(dojo.html.getStyle(node,"width")=="auto")){node.style.width="auto";}}
};if(dojo.lang.isArrayLike(_752)){dojo.lang.forEach(_752,_753);}else{_753(_752);}};dojo.lfx.html.fade=function(_755,_756,_757,_758,_759){_755=dojo.lfx.html._byId(_755);var _75a={property:"opacity"};if(!dj_undef("start",_756)){_75a.start=_756.start;}else{_75a.start=function(){return dojo.html.getOpacity(_755[0]);};}
if(!dj_undef("end",_756)){_75a.end=_756.end;}else{dojo.raise("dojo.lfx.html.fade needs an end value");}
var anim=dojo.lfx.propertyAnimation(_755,[_75a],_757,_758);anim.connect("beforeBegin",function(){dojo.lfx.html._makeFadeable(_755);});if(_759){anim.connect("onEnd",function(){_759(_755,anim);});}
return anim;};dojo.lfx.html.fadeIn=function(_75c,_75d,_75e,_75f){return dojo.lfx.html.fade(_75c,{end:1},_75d,_75e,_75f);};dojo.lfx.html.fadeOut=function(_760,_761,_762,_763){return dojo.lfx.html.fade(_760,{end:0},_761,_762,_763);};dojo.lfx.html.fadeShow=function(_764,_765,_766,_767){_764=dojo.lfx.html._byId(_764);dojo.lang.forEach(_764,function(node){dojo.html.setOpacity(node,0);});var anim=dojo.lfx.html.fadeIn(_764,_765,_766,_767);anim.connect("beforeBegin",function(){if(dojo.lang.isArrayLike(_764)){dojo.lang.forEach(_764,dojo.html.show);}else{dojo.html.show(_764);}});return anim;};dojo.lfx.html.fadeHide=function(_76a,_76b,_76c,_76d){var anim=dojo.lfx.html.fadeOut(_76a,_76b,_76c,function(){if(dojo.lang.isArrayLike(_76a)){dojo.lang.forEach(_76a,dojo.html.hide);}else{dojo.html.hide(_76a);}
if(_76d){_76d(_76a,anim);}});return anim;};dojo.lfx.html.wipeIn=function(_76f,_770,_771,_772){_76f=dojo.lfx.html._byId(_76f);var _773=[];dojo.lang.forEach(_76f,function(node){var _775={};with(node.style){visibility="hidden";display="";}
var _776=dojo.html.getBorderBox(node).height;with(node.style){visibility="";display="none";}
var anim=dojo.lfx.propertyAnimation(node,{"height":{start:1,end:function(){return _776;}}},_770,_771);anim.connect("beforeBegin",function(){_775.overflow=node.style.overflow;_775.height=node.style.height;with(node.style){overflow="hidden";_776="1px";}
dojo.html.show(node);});anim.connect("onEnd",function(){with(node.style){overflow=_775.overflow;_776=_775.height;}
if(_772){_772(node,anim);}});_773.push(anim);});return dojo.lfx.combine(_773);};dojo.lfx.html.wipeOut=function(_778,_779,_77a,_77b){_778=dojo.lfx.html._byId(_778);var _77c=[];dojo.lang.forEach(_778,function(node){var _77e={};var anim=dojo.lfx.propertyAnimation(node,{"height":{start:function(){return dojo.html.getContentBox(node).height;},end:1}},_779,_77a,{"beforeBegin":function(){_77e.overflow=node.style.overflow;_77e.height=node.style.height;with(node.style){overflow="hidden";}
dojo.html.show(node);},"onEnd":function(){dojo.html.hide(node);with(node.style){overflow=_77e.overflow;height=_77e.height;}
if(_77b){_77b(node,anim);}}});_77c.push(anim);});return dojo.lfx.combine(_77c);};dojo.lfx.html.slideTo=function(_780,_781,_782,_783,_784){_780=dojo.lfx.html._byId(_780);var _785=[];var _786=dojo.html.getComputedStyle;dojo.lang.forEach(_780,function(node){var top=null;var left=null;var init=(function(){var _78b=node;return function(){var pos=_786(_78b,"position");top=(pos=="absolute"?node.offsetTop:parseInt(_786(node,"top"))||0);left=(pos=="absolute"?node.offsetLeft:parseInt(_786(node,"left"))||0);if(!dojo.lang.inArray(["absolute","relative"],pos)){var ret=dojo.html.abs(_78b,true);dojo.html.setStyleAttributes(_78b,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");top=ret.y;left=ret.x;}};})();init();var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:(_781.top||0)},"left":{start:left,end:(_781.left||0)}},_782,_783,{"beforeBegin":init});if(_784){anim.connect("onEnd",function(){_784(_780,anim);});}
_785.push(anim);});return dojo.lfx.combine(_785);};dojo.lfx.html.slideBy=function(_78f,_790,_791,_792,_793){_78f=dojo.lfx.html._byId(_78f);var _794=[];var _795=dojo.html.getComputedStyle;dojo.lang.forEach(_78f,function(node){var top=null;var left=null;var init=(function(){var _79a=node;return function(){var pos=_795(_79a,"position");top=(pos=="absolute"?node.offsetTop:parseInt(_795(node,"top"))||0);left=(pos=="absolute"?node.offsetLeft:parseInt(_795(node,"left"))||0);if(!dojo.lang.inArray(["absolute","relative"],pos)){var ret=dojo.html.abs(_79a,true);dojo.html.setStyleAttributes(_79a,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");top=ret.y;left=ret.x;}};})();init();var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:top+(_790.top||0)},"left":{start:left,end:left+(_790.left||0)}},_791,_792).connect("beforeBegin",init);if(_793){anim.connect("onEnd",function(){_793(_78f,anim);});}
_794.push(anim);});return dojo.lfx.combine(_794);};dojo.lfx.html.explode=function(_79e,_79f,_7a0,_7a1,_7a2){var h=dojo.html;_79e=dojo.byId(_79e);_79f=dojo.byId(_79f);var _7a4=h.toCoordinateObject(_79e,true);var _7a5=document.createElement("div");h.copyStyle(_7a5,_79f);if(_79f.explodeClassName){_7a5.className=_79f.explodeClassName;}
with(_7a5.style){position="absolute";display="none";var _7a6=h.getStyle(_79e,"background-color");backgroundColor=_7a6?_7a6.toLowerCase():"transparent";backgroundColor=(backgroundColor=="transparent")?"rgb(221, 221, 221)":backgroundColor;}
dojo.body().appendChild(_7a5);with(_79f.style){visibility="hidden";display="block";}
var _7a7=h.toCoordinateObject(_79f,true);with(_79f.style){display="none";visibility="visible";}
var _7a8={opacity:{start:0.5,end:1}};dojo.lang.forEach(["height","width","top","left"],function(type){_7a8[type]={start:_7a4[type],end:_7a7[type]};});var anim=new dojo.lfx.propertyAnimation(_7a5,_7a8,_7a0,_7a1,{"beforeBegin":function(){h.setDisplay(_7a5,"block");},"onEnd":function(){h.setDisplay(_79f,"block");_7a5.parentNode.removeChild(_7a5);}});if(_7a2){anim.connect("onEnd",function(){_7a2(_79f,anim);});}
return anim;};dojo.lfx.html.implode=function(_7ab,end,_7ad,_7ae,_7af){var h=dojo.html;_7ab=dojo.byId(_7ab);end=dojo.byId(end);var _7b1=dojo.html.toCoordinateObject(_7ab,true);var _7b2=dojo.html.toCoordinateObject(end,true);var _7b3=document.createElement("div");dojo.html.copyStyle(_7b3,_7ab);if(_7ab.explodeClassName){_7b3.className=_7ab.explodeClassName;}
dojo.html.setOpacity(_7b3,0.3);with(_7b3.style){position="absolute";display="none";backgroundColor=h.getStyle(_7ab,"background-color").toLowerCase();}
dojo.body().appendChild(_7b3);var _7b4={opacity:{start:1,end:0.5}};dojo.lang.forEach(["height","width","top","left"],function(type){_7b4[type]={start:_7b1[type],end:_7b2[type]};});var anim=new dojo.lfx.propertyAnimation(_7b3,_7b4,_7ad,_7ae,{"beforeBegin":function(){dojo.html.hide(_7ab);dojo.html.show(_7b3);},"onEnd":function(){_7b3.parentNode.removeChild(_7b3);}});if(_7af){anim.connect("onEnd",function(){_7af(_7ab,anim);});}
return anim;};dojo.lfx.html.highlight=function(_7b7,_7b8,_7b9,_7ba,_7bb){_7b7=dojo.lfx.html._byId(_7b7);var _7bc=[];dojo.lang.forEach(_7b7,function(node){var _7be=dojo.html.getBackgroundColor(node);var bg=dojo.html.getStyle(node,"background-color").toLowerCase();var _7c0=dojo.html.getStyle(node,"background-image");var _7c1=(bg=="transparent"||bg=="rgba(0, 0, 0, 0)");while(_7be.length>3){_7be.pop();}
var rgb=new dojo.gfx.color.Color(_7b8);var _7c3=new dojo.gfx.color.Color(_7be);var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:rgb,end:_7c3}},_7b9,_7ba,{"beforeBegin":function(){if(_7c0){node.style.backgroundImage="none";}
node.style.backgroundColor="rgb("+rgb.toRgb().join(",")+")";},"onEnd":function(){if(_7c0){node.style.backgroundImage=_7c0;}
if(_7c1){node.style.backgroundColor="transparent";}
if(_7bb){_7bb(node,anim);}}});_7bc.push(anim);});return dojo.lfx.combine(_7bc);};dojo.lfx.html.unhighlight=function(_7c5,_7c6,_7c7,_7c8,_7c9){_7c5=dojo.lfx.html._byId(_7c5);var _7ca=[];dojo.lang.forEach(_7c5,function(node){var _7cc=new dojo.gfx.color.Color(dojo.html.getBackgroundColor(node));var rgb=new dojo.gfx.color.Color(_7c6);var _7ce=dojo.html.getStyle(node,"background-image");var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:_7cc,end:rgb}},_7c7,_7c8,{"beforeBegin":function(){if(_7ce){node.style.backgroundImage="none";}
node.style.backgroundColor="rgb("+_7cc.toRgb().join(",")+")";},"onEnd":function(){if(_7c9){_7c9(node,anim);}}});_7ca.push(anim);});return dojo.lfx.combine(_7ca);};dojo.lang.mixin(dojo.lfx,dojo.lfx.html);dojo.kwCompoundRequire({browser:["dojo.lfx.html"],dashboard:["dojo.lfx.html"]});dojo.lfx.toggler.plain=function(){this.stop=function(){};this.show=function(node,_7d1,_7d2,_7d3){dojo.html.show(node);if(dojo.lang.isFunction(_7d3)){_7d3();}};this.hide=function(node,_7d5,_7d6,_7d7){dojo.html.hide(node);if(dojo.lang.isFunction(_7d7)){_7d7();}};};dojo.lfx.toggler.common={stop:function(){if(this.anim&&this.anim.status()!="stopped"){this.anim.stop();}},_act:function(_7d8,node,_7da,_7db,_7dc,_7dd){this.stop();this.anim=dojo.lfx[_7d8](node,_7da,_7db,_7dc).play();},show:function(node,_7df,_7e0,_7e1,_7e2){this._act(this.show_action,node,_7df,_7e0,_7e1,_7e2);},hide:function(node,_7e4,_7e5,_7e6,_7e7){this._act(this.hide_action,node,_7e4,_7e5,_7e6,_7e7);}};dojo.lfx.toggler.fade=function(){this.anim=null;this.show_action="fadeShow";this.hide_action="fadeHide";};dojo.extend(dojo.lfx.toggler.fade,dojo.lfx.toggler.common);dojo.lfx.toggler.wipe=function(){this.anim=null;this.show_action="wipeIn";this.hide_action="wipeOut";};dojo.extend(dojo.lfx.toggler.wipe,dojo.lfx.toggler.common);dojo.lfx.toggler.explode=function(){this.anim=null;this.show_action="explode";this.hide_action="implode";this.show=function(node,_7e9,_7ea,_7eb,_7ec){this.stop();this.anim=dojo.lfx.explode(_7ec||{x:0,y:0,width:0,height:0},node,_7e9,_7ea,_7eb).play();};this.hide=function(node,_7ee,_7ef,_7f0,_7f1){this.stop();this.anim=dojo.lfx.implode(node,_7f1||{x:0,y:0,width:0,height:0},_7ee,_7ef,_7f0).play();};};dojo.extend(dojo.lfx.toggler.explode,dojo.lfx.toggler.common);dojo.declare("dojo.widget.HtmlWidget",dojo.widget.DomWidget,{templateCssPath:null,templatePath:null,lang:"",toggle:"plain",toggleDuration:150,initialize:function(args,frag){},postMixInProperties:function(args,frag){if(this.lang===""){this.lang=null;}
this.toggleObj=new (dojo.lfx.toggler[this.toggle.toLowerCase()]||dojo.lfx.toggler.plain);},createNodesFromText:function(txt,wrap){return dojo.html.createNodesFromText(txt,wrap);},destroyRendering:function(_7f8){try{if(this.bgIframe){this.bgIframe.remove();delete this.bgIframe;}
if(!_7f8&&this.domNode){dojo.event.browser.clean(this.domNode);}
dojo.widget.HtmlWidget.superclass.destroyRendering.call(this);}
catch(e){}},isShowing:function(){return dojo.html.isShowing(this.domNode);},toggleShowing:function(){if(this.isShowing()){this.hide();}else{this.show();}},show:function(){if(this.isShowing()){return;}
this.animationInProgress=true;this.toggleObj.show(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onShow),this.explodeSrc);},onShow:function(){this.animationInProgress=false;this.checkSize();},hide:function(){if(!this.isShowing()){return;}
this.animationInProgress=true;this.toggleObj.hide(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onHide),this.explodeSrc);},onHide:function(){this.animationInProgress=false;},_isResized:function(w,h){if(!this.isShowing()){return false;}
var wh=dojo.html.getMarginBox(this.domNode);var _7fc=w||wh.width;var _7fd=h||wh.height;if(this.width==_7fc&&this.height==_7fd){return false;}
this.width=_7fc;this.height=_7fd;return true;},checkSize:function(){if(!this._isResized()){return;}
this.onResized();},resizeTo:function(w,h){dojo.html.setMarginBox(this.domNode,{width:w,height:h});if(this.isShowing()){this.onResized();}},resizeSoon:function(){if(this.isShowing()){dojo.lang.setTimeout(this,this.onResized,0);}},onResized:function(){dojo.lang.forEach(this.children,function(_800){if(_800.checkSize){_800.checkSize();}});}});dojo.kwCompoundRequire({common:["dojo.xml.Parse","dojo.widget.Widget","dojo.widget.Parse","dojo.widget.Manager"],browser:["dojo.widget.DomWidget","dojo.widget.HtmlWidget"],dashboard:["dojo.widget.DomWidget","dojo.widget.HtmlWidget"],svg:["dojo.widget.SvgWidget"],rhino:["dojo.widget.SwtWidget"]});