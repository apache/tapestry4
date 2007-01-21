
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
dojo.require.apply(dojo,_6a);}};dojo.requireAfterIf=dojo.requireIf;dojo.provide=function(_6c){return dojo.hostenv.startPackage.apply(dojo.hostenv,arguments);};dojo.registerModulePath=function(_6d,_6e){return dojo.hostenv.registerModulePath(_6d,_6e);};dojo.hostenv.normalizeLocale=function(_6f){var _70=_6f?_6f.toLowerCase():dojo.locale;if(_70=="root"){_70="ROOT";}
return _70;};dojo.hostenv.searchLocalePath=function(_71,_72,_73){_71=dojo.hostenv.normalizeLocale(_71);var _74=_71.split("-");var _75=[];for(var i=_74.length;i>0;i--){_75.push(_74.slice(0,i).join("-"));}
_75.push(false);if(_72){_75.reverse();}
for(var j=_75.length-1;j>=0;j--){var loc=_75[j]||"ROOT";var _79=_73(loc);if(_79){break;}}
};dojo.hostenv.localesGenerated=["ROOT","es-es","es","it-it","pt-br","de","fr-fr","zh-cn","pt","en-us","zh","fr","zh-tw","it","en-gb","xx","de-de","ko-kr","ja-jp","ko","en","ja"];dojo.hostenv.registerNlsPrefix=function(){dojo.registerModulePath("nls","nls");};dojo.hostenv.preloadLocalizations=function(){if(dojo.hostenv.localesGenerated){dojo.hostenv.registerNlsPrefix();function preload(_7a){_7a=dojo.hostenv.normalizeLocale(_7a);dojo.hostenv.searchLocalePath(_7a,true,function(loc){for(var i=0;i<dojo.hostenv.localesGenerated.length;i++){if(dojo.hostenv.localesGenerated[i]==loc){dojo["require"]("nls.dojo_"+loc);return true;}}
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
(function(){if(typeof dj_usingBootstrap!="undefined"){return;}
var _ee=false;var _ef=false;var _f0=false;if((typeof this["load"]=="function")&&((typeof this["Packages"]=="function")||(typeof this["Packages"]=="object"))){_ee=true;}else{if(typeof this["load"]=="function"){_ef=true;}else{if(window.widget){_f0=true;}}
}
var _f1=[];if((this["djConfig"])&&((djConfig["isDebug"])||(djConfig["debugAtAllCosts"]))){_f1.push("debug.js");}
if((this["djConfig"])&&(djConfig["debugAtAllCosts"])&&(!_ee)&&(!_f0)){_f1.push("browser_debug.js");}
var _f2=djConfig["baseScriptUri"];if((this["djConfig"])&&(djConfig["baseLoaderUri"])){_f2=djConfig["baseLoaderUri"];}
for(var x=0;x<_f1.length;x++){var _f4=_f2+"src/"+_f1[x];if(_ee||_ef){load(_f4);}else{try{document.write("<scr"+"ipt type='text/javascript' src='"+_f4+"'></scr"+"ipt>");}
catch(e){var _f5=document.createElement("script");_f5.src=_f4;document.getElementsByTagName("head")[0].appendChild(_f5);}}
}})();dojo.provide("dojo.a11y");dojo.provide("dojo.date.common");dojo.provide("dojo.date.format");dojo.provide("dojo.date.supplemental");dojo.provide("dojo.dom");dojo.provide("dojo.event.*");dojo.provide("dojo.event.browser");dojo.provide("dojo.event.common");dojo.provide("dojo.event.topic");dojo.provide("dojo.gfx.color");dojo.provide("dojo.html.color");dojo.provide("dojo.html.common");dojo.provide("dojo.html.display");dojo.provide("dojo.html.layout");dojo.provide("dojo.html.style");dojo.provide("dojo.html.util");dojo.provide("dojo.i18n.common");dojo.provide("dojo.io.BrowserIO");dojo.provide("dojo.io.common");dojo.provide("dojo.io.cookie");dojo.provide("dojo.lang.array");dojo.provide("dojo.lang.common");dojo.provide("dojo.lang.declare");dojo.provide("dojo.lang.extras");dojo.provide("dojo.lang.func");dojo.provide("dojo.lfx.*");dojo.provide("dojo.lfx.Animation");dojo.provide("dojo.lfx.html");dojo.provide("dojo.lfx.toggler");dojo.provide("dojo.ns");dojo.provide("dojo.string");dojo.provide("dojo.string.common");dojo.provide("dojo.string.extras");dojo.provide("dojo.undo.browser");dojo.provide("dojo.uri.*");dojo.provide("dojo.uri.Uri");dojo.provide("dojo.widget.*");dojo.provide("dojo.widget.DomWidget");dojo.provide("dojo.widget.HtmlWidget");dojo.provide("dojo.widget.Manager");dojo.provide("dojo.widget.Parse");dojo.provide("dojo.widget.Widget");dojo.provide("dojo.xml.Parse");dojo.lang.inherits=function(_f6,_f7){if(!dojo.lang.isFunction(_f7)){dojo.raise("dojo.inherits: superclass argument ["+_f7+"] must be a function (subclass: ["+_f6+"']");}
_f6.prototype=new _f7();_f6.prototype.constructor=_f6;_f6.superclass=_f7.prototype;_f6["super"]=_f7.prototype;};dojo.lang._mixin=function(obj,_f9){var _fa={};for(var x in _f9){if((typeof _fa[x]=="undefined")||(_fa[x]!=_f9[x])){obj[x]=_f9[x];}}
if(dojo.render.html.ie&&(typeof (_f9["toString"])=="function")&&(_f9["toString"]!=obj["toString"])&&(_f9["toString"]!=_fa["toString"])){obj.toString=_f9.toString;}
return obj;};dojo.lang.mixin=function(obj,_fd){for(var i=1,l=arguments.length;i<l;i++){dojo.lang._mixin(obj,arguments[i]);}
return obj;};dojo.lang.extend=function(_100,_101){for(var i=1,l=arguments.length;i<l;i++){dojo.lang._mixin(_100.prototype,arguments[i]);}
return _100;};dojo.lang._delegate=function(obj,_105){function TMP(){}
TMP.prototype=obj;var tmp=new TMP();if(_105){dojo.lang.mixin(tmp,_105);}
return tmp;};dojo.inherits=dojo.lang.inherits;dojo.mixin=dojo.lang.mixin;dojo.extend=dojo.lang.extend;dojo.lang.find=function(_107,_108,_109,_10a){var _10b=dojo.lang.isString(_107);if(_10b){_107=_107.split("");}
if(_10a){var step=-1;var i=_107.length-1;var end=-1;}else{var step=1;var i=0;var end=_107.length;}
if(_109){while(i!=end){if(_107[i]===_108){return i;}
i+=step;}}else{while(i!=end){if(_107[i]==_108){return i;}
i+=step;}}
return -1;};dojo.lang.indexOf=dojo.lang.find;dojo.lang.findLast=function(_10f,_110,_111){return dojo.lang.find(_10f,_110,_111,true);};dojo.lang.lastIndexOf=dojo.lang.findLast;dojo.lang.inArray=function(_112,_113){return dojo.lang.find(_112,_113)>-1;};dojo.lang.isObject=function(it){if(typeof it=="undefined"){return false;}
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
},map:function(arr,obj,_125){var _126=dojo.lang.isString(arr);if(_126){arr=arr.split("");}
if(dojo.lang.isFunction(obj)&&(!_125)){_125=obj;obj=dj_global;}else{if(dojo.lang.isFunction(obj)&&_125){var _127=obj;obj=_125;_125=_127;}}
if(Array.map){var _128=Array.map(arr,_125,obj);}else{var _128=[];for(var i=0;i<arr.length;++i){_128.push(_125.call(obj,arr[i]));}}
if(_126){return _128.join("");}else{return _128;}},reduce:function(arr,_12b,obj,_12d){var _12e=_12b;if(arguments.length==1){dojo.debug("dojo.lang.reduce called with too few arguments!");return false;}else{if(arguments.length==2){_12d=_12b;_12e=arr.shift();}else{if(arguments.lenght==3){if(dojo.lang.isFunction(obj)){_12d=obj;obj=null;}}else{if(dojo.lang.isFunction(obj)){var tmp=_12d;_12d=obj;obj=tmp;}}
}}
var ob=obj?obj:dj_global;dojo.lang.map(arr,function(val){_12e=_12d.call(ob,_12e,val);});return _12e;},forEach:function(_132,_133,_134){if(dojo.lang.isString(_132)){_132=_132.split("");}
if(Array.forEach){Array.forEach(_132,_133,_134);}else{if(!_134){_134=dj_global;}
for(var i=0,l=_132.length;i<l;i++){_133.call(_134,_132[i],i,_132);}}
},_everyOrSome:function(_137,arr,_139,_13a){if(dojo.lang.isString(arr)){arr=arr.split("");}
if(Array.every){return Array[_137?"every":"some"](arr,_139,_13a);}else{if(!_13a){_13a=dj_global;}
for(var i=0,l=arr.length;i<l;i++){var _13d=_139.call(_13a,arr[i],i,arr);if(_137&&!_13d){return false;}else{if((!_137)&&(_13d)){return true;}}
}
return Boolean(_137);}},every:function(arr,_13f,_140){return this._everyOrSome(true,arr,_13f,_140);},some:function(arr,_142,_143){return this._everyOrSome(false,arr,_142,_143);},filter:function(arr,_145,_146){var _147=dojo.lang.isString(arr);if(_147){arr=arr.split("");}
var _148;if(Array.filter){_148=Array.filter(arr,_145,_146);}else{if(!_146){if(arguments.length>=3){dojo.raise("thisObject doesn't exist!");}
_146=dj_global;}
_148=[];for(var i=0;i<arr.length;i++){if(_145.call(_146,arr[i],i,arr)){_148.push(arr[i]);}}
}
if(_147){return _148.join("");}else{return _148;}},unnest:function(){var out=[];for(var i=0;i<arguments.length;i++){if(dojo.lang.isArrayLike(arguments[i])){var add=dojo.lang.unnest.apply(this,arguments[i]);out=out.concat(add);}else{out.push(arguments[i]);}}
return out;},toArray:function(_14d,_14e){var _14f=[];for(var i=_14e||0;i<_14d.length;i++){_14f.push(_14d[i]);}
return _14f;}});dojo.lang.setTimeout=function(func,_152){var _153=window,_154=2;if(!dojo.lang.isFunction(func)){_153=func;func=_152;_152=arguments[2];_154++;}
if(dojo.lang.isString(func)){func=_153[func];}
var args=[];for(var i=_154;i<arguments.length;i++){args.push(arguments[i]);}
return dojo.global().setTimeout(function(){func.apply(_153,args);},_152);};dojo.lang.clearTimeout=function(_157){dojo.global().clearTimeout(_157);};dojo.lang.getNameInObj=function(ns,item){if(!ns){ns=dj_global;}
for(var x in ns){if(ns[x]===item){return new String(x);}}
return null;};dojo.lang.shallowCopy=function(obj,deep){var i,ret;if(obj===null){return null;}
if(dojo.lang.isObject(obj)){ret=new obj.constructor();for(i in obj){if(dojo.lang.isUndefined(ret[i])){ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];}}
}else{if(dojo.lang.isArray(obj)){ret=[];for(i=0;i<obj.length;i++){ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];}}else{ret=obj;}}
return ret;};dojo.lang.firstValued=function(){for(var i=0;i<arguments.length;i++){if(typeof arguments[i]!="undefined"){return arguments[i];}}
return undefined;};dojo.lang.getObjPathValue=function(_160,_161,_162){dojo.deprecated("dojo.lang.getObjPathValue","use dojo.getObject","0.6");with(dojo.parseObjPath(_160,_161,_162)){return dojo.evalProp(prop,obj,_162);}};dojo.lang.setObjPathValue=function(_163,_164,_165,_166){dojo.deprecated("dojo.lang.setObjPathValue","use dojo.parseObjPath and the '=' operator","0.6");if(arguments.length<4){_166=true;}
with(dojo.parseObjPath(_163,_165,_166)){if(obj&&(_166||(prop in obj))){obj[prop]=_164;}}
};dojo.lang.hitch=function(_167,_168){var args=[];for(var x=2;x<arguments.length;x++){args.push(arguments[x]);}
var fcn=(dojo.lang.isString(_168)?_167[_168]:_168)||function(){};return function(){var ta=args.concat([]);for(var x=0;x<arguments.length;x++){ta.push(arguments[x]);}
return fcn.apply(_167,ta);};};dojo.lang.anonCtr=0;dojo.lang.anon={};dojo.lang.nameAnonFunc=function(_16e,_16f,_170){var isIE=(dojo.render.html.capable&&dojo.render.html["ie"]);var jpn="$joinpoint";var nso=(_16f||dojo.lang.anon);if(isIE){var cn=_16e["__dojoNameCache"];if(cn&&nso[cn]===_16e){return _16e["__dojoNameCache"];}else{if(cn){var _175=cn.indexOf(jpn);if(_175!=-1){return cn.substring(0,_175);}}
}}
if((_170)||((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true))){for(var x in nso){try{if(nso[x]===_16e){if(isIE){_16e["__dojoNameCache"]=x;var _175=x.indexOf(jpn);if(_175!=-1){x=x.substring(0,_175);}}
return x;}}
catch(e){}}
}
var ret="__"+dojo.lang.anonCtr++;while(typeof nso[ret]!="undefined"){ret="__"+dojo.lang.anonCtr++;}
nso[ret]=_16e;return ret;};dojo.lang.forward=function(_178){return function(){return this[_178].apply(this,arguments);};};dojo.lang.curry=function(_179,func){var _17b=[];_179=_179||dj_global;if(dojo.lang.isString(func)){func=_179[func];}
for(var x=2;x<arguments.length;x++){_17b.push(arguments[x]);}
var _17d=(func["__preJoinArity"]||func.length)-_17b.length;function gather(_17e,_17f,_180){var _181=_180;var _182=_17f.slice(0);for(var x=0;x<_17e.length;x++){_182.push(_17e[x]);}
_180=_180-_17e.length;if(_180<=0){var res=func.apply(_179,_182);_180=_181;return res;}else{return function(){return gather(arguments,_182,_180);};}}
return gather([],_17b,_17d);};dojo.lang.curryArguments=function(_185,func,args,_188){var _189=[];var x=_188||0;for(x=_188;x<args.length;x++){_189.push(args[x]);}
return dojo.lang.curry.apply(dojo.lang,[_185,func].concat(_189));};dojo.lang.tryThese=function(){for(var x=0;x<arguments.length;x++){try{if(typeof arguments[x]=="function"){var ret=(arguments[x]());if(ret){return ret;}}
}
catch(e){dojo.debug(e);}}
};dojo.lang.delayThese=function(farr,cb,_18f,_190){if(!farr.length){if(typeof _190=="function"){_190();}
return;}
if((typeof _18f=="undefined")&&(typeof cb=="number")){_18f=cb;cb=function(){};}else{if(!cb){cb=function(){};if(!_18f){_18f=0;}}
}
setTimeout(function(){(farr.shift())();cb();dojo.lang.delayThese(farr,cb,_18f,_190);},_18f);};dojo.event=new function(){this._canTimeout=dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);function interpolateArgs(args,_192){var dl=dojo.lang;var ao={srcObj:dj_global,srcFunc:null,adviceObj:dj_global,adviceFunc:null,aroundObj:null,aroundFunc:null,adviceType:(args.length>2)?args[0]:"after",precedence:"last",once:false,delay:null,rate:0,adviceMsg:false,maxCalls:-1};switch(args.length){case 0:
return;case 1:
return;case 2:
ao.srcFunc=args[0];ao.adviceFunc=args[1];break;case 3:
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isString(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];ao.adviceFunc=args[2];}else{if((dl.isString(args[1]))&&(dl.isString(args[2]))){ao.srcFunc=args[1];ao.adviceFunc=args[2];}else{if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isFunction(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];var _195=dl.nameAnonFunc(args[2],ao.adviceObj,_192);ao.adviceFunc=_195;}else{if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){ao.adviceType="after";ao.srcObj=dj_global;var _195=dl.nameAnonFunc(args[0],ao.srcObj,_192);ao.srcFunc=_195;ao.adviceObj=args[1];ao.adviceFunc=args[2];}}
}}
break;case 4:
if((dl.isObject(args[0]))&&(dl.isObject(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isString(args[1]))&&(dl.isObject(args[2]))){ao.adviceType=args[0];ao.srcObj=dj_global;ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isFunction(args[1]))&&(dl.isObject(args[2]))){ao.adviceType=args[0];ao.srcObj=dj_global;var _195=dl.nameAnonFunc(args[1],dj_global,_192);ao.srcFunc=_195;ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){ao.srcObj=args[1];ao.srcFunc=args[2];var _195=dl.nameAnonFunc(args[3],dj_global,_192);ao.adviceObj=dj_global;ao.adviceFunc=_195;}else{if(dl.isObject(args[1])){ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=dj_global;ao.adviceFunc=args[3];}else{if(dl.isObject(args[2])){ao.srcObj=dj_global;ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{ao.srcObj=ao.adviceObj=ao.aroundObj=dj_global;ao.srcFunc=args[1];ao.adviceFunc=args[2];ao.aroundFunc=args[3];}}
}}
}}
break;case 6:
ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=args[3];ao.adviceFunc=args[4];ao.aroundFunc=args[5];ao.aroundObj=dj_global;break;default:
ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=args[3];ao.adviceFunc=args[4];ao.aroundObj=args[5];ao.aroundFunc=args[6];ao.once=args[7];ao.delay=args[8];ao.rate=args[9];ao.adviceMsg=args[10];ao.maxCalls=(!isNaN(parseInt(args[11])))?args[11]:-1;break;}
if(dl.isFunction(ao.aroundFunc)){var _195=dl.nameAnonFunc(ao.aroundFunc,ao.aroundObj,_192);ao.aroundFunc=_195;}
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
if(dojo.lang.isArray(ao.srcObj)&&ao.srcObj!=""){var _197={};for(var x in ao){_197[x]=ao[x];}
var mjps=[];dojo.lang.forEach(ao.srcObj,function(src){if((dojo.render.html.capable)&&(dojo.lang.isString(src))){src=dojo.byId(src);}
_197.srcObj=src;mjps.push(dojo.event.connect.call(dojo.event,_197));});return mjps;}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);if(ao.adviceFunc){var mjp2=dojo.event.MethodJoinPoint.getForMethod(ao.adviceObj,ao.adviceFunc);}
mjp.kwAddAdvice(ao);return mjp;};this.log=function(a1,a2){var _19f;if((arguments.length==1)&&(typeof a1=="object")){_19f=a1;}else{_19f={srcObj:a1,srcFunc:a2};}
_19f.adviceFunc=function(){var _1a0=[];for(var x=0;x<arguments.length;x++){_1a0.push(arguments[x]);}
dojo.debug("("+_19f.srcObj+")."+_19f.srcFunc,":",_1a0.join(", "));};this.kwConnect(_19f);};this.connectBefore=function(){var args=["before"];for(var i=0;i<arguments.length;i++){args.push(arguments[i]);}
return this.connect.apply(this,args);};this.connectAround=function(){var args=["around"];for(var i=0;i<arguments.length;i++){args.push(arguments[i]);}
return this.connect.apply(this,args);};this.connectOnce=function(){var ao=interpolateArgs(arguments,true);ao.once=true;return this.connect(ao);};this.connectRunOnce=function(){var ao=interpolateArgs(arguments,true);ao.maxCalls=1;return this.connect(ao);};this._kwConnectImpl=function(_1a8,_1a9){var fn=(_1a9)?"disconnect":"connect";if(typeof _1a8["srcFunc"]=="function"){_1a8.srcObj=_1a8["srcObj"]||dj_global;var _1ab=dojo.lang.nameAnonFunc(_1a8.srcFunc,_1a8.srcObj,true);_1a8.srcFunc=_1ab;}
if(typeof _1a8["adviceFunc"]=="function"){_1a8.adviceObj=_1a8["adviceObj"]||dj_global;var _1ab=dojo.lang.nameAnonFunc(_1a8.adviceFunc,_1a8.adviceObj,true);_1a8.adviceFunc=_1ab;}
_1a8.srcObj=_1a8["srcObj"]||dj_global;_1a8.adviceObj=_1a8["adviceObj"]||_1a8["targetObj"]||dj_global;_1a8.adviceFunc=_1a8["adviceFunc"]||_1a8["targetFunc"];return dojo.event[fn](_1a8);};this.kwConnect=function(_1ac){return this._kwConnectImpl(_1ac,false);};this.disconnect=function(){if(arguments.length==1){var ao=arguments[0];}else{var ao=interpolateArgs(arguments,true);}
if(!ao.adviceFunc){return;}
if(dojo.lang.isString(ao.srcFunc)&&(ao.srcFunc.toLowerCase()=="onkey")){if(dojo.render.html.ie){ao.srcFunc="onkeydown";this.disconnect(ao);}
ao.srcFunc="onkeypress";}
if(!ao.srcObj[ao.srcFunc]){return null;}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc,true);mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);return mjp;};this.kwDisconnect=function(_1af){return this._kwConnectImpl(_1af,true);};};dojo.event.MethodInvocation=function(_1b0,obj,args){this.jp_=_1b0;this.object=obj;this.args=[];for(var x=0;x<args.length;x++){this.args[x]=args[x];}
this.around_index=-1;};dojo.event.MethodInvocation.prototype.proceed=function(){this.around_index++;if(this.around_index>=this.jp_.around.length){return this.jp_.object[this.jp_.methodname].apply(this.jp_.object,this.args);}else{var ti=this.jp_.around[this.around_index];var mobj=ti[0]||dj_global;var meth=ti[1];return mobj[meth].call(mobj,this);}};dojo.event.MethodJoinPoint=function(obj,_1b8){this.object=obj||dj_global;this.methodname=_1b8;this.methodfunc=this.object[_1b8];};dojo.event.MethodJoinPoint.getForMethod=function(obj,_1ba){if(!obj){obj=dj_global;}
var ofn=obj[_1ba];if(!ofn){ofn=obj[_1ba]=function(){};if(!obj[_1ba]){dojo.raise("Cannot set do-nothing method on that object "+_1ba);}}else{if((typeof ofn!="function")&&(!dojo.lang.isFunction(ofn))&&(!dojo.lang.isAlien(ofn))){return null;}}
var _1bc=_1ba+"$joinpoint";var _1bd=_1ba+"$joinpoint$method";var _1be=obj[_1bc];if(!_1be){var _1bf=false;if(dojo.event["browser"]){if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){_1bf=true;dojo.event.browser.addClobberNodeAttrs(obj,[_1bc,_1bd,_1ba]);}}
var _1c0=ofn.length;obj[_1bd]=ofn;_1be=obj[_1bc]=new dojo.event.MethodJoinPoint(obj,_1bd);if(!_1bf){obj[_1ba]=function(){return _1be.run.apply(_1be,arguments);};}else{obj[_1ba]=function(){var args=[];if(!arguments.length){var evt=null;try{if(obj.ownerDocument){evt=obj.ownerDocument.parentWindow.event;}else{if(obj.documentElement){evt=obj.documentElement.ownerDocument.parentWindow.event;}else{if(obj.event){evt=obj.event;}else{evt=window.event;}}
}}
catch(e){evt=window.event;}
if(evt){args.push(dojo.event.browser.fixEvent(evt,this));}}else{for(var x=0;x<arguments.length;x++){if((x==0)&&(dojo.event.browser.isEvent(arguments[x]))){args.push(dojo.event.browser.fixEvent(arguments[x],this));}else{args.push(arguments[x]);}}
}
return _1be.run.apply(_1be,args);};}
obj[_1ba].__preJoinArity=_1c0;}
return _1be;};dojo.lang.extend(dojo.event.MethodJoinPoint,{squelch:false,unintercept:function(){this.object[this.methodname]=this.methodfunc;this.before=[];this.after=[];this.around=[];},disconnect:dojo.lang.forward("unintercept"),run:function(){var obj=this.object||dj_global;var args=arguments;var _1c6=[];for(var x=0;x<args.length;x++){_1c6[x]=args[x];}
var _1c8=function(marr){if(!marr){dojo.debug("Null argument to unrollAdvice()");return;}
var _1ca=marr[0]||dj_global;var _1cb=marr[1];if(!_1ca[_1cb]){dojo.raise("function \""+_1cb+"\" does not exist on \""+_1ca+"\"");}
var _1cc=marr[2]||dj_global;var _1cd=marr[3];var msg=marr[6];var _1cf=marr[7];if(_1cf>-1){if(_1cf==0){return;}
marr[7]--;}
var _1d0;var to={args:[],jp_:this,object:obj,proceed:function(){return _1ca[_1cb].apply(_1ca,to.args);}};to.args=_1c6;var _1d2=parseInt(marr[4]);var _1d3=((!isNaN(_1d2))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));if(marr[5]){var rate=parseInt(marr[5]);var cur=new Date();var _1d6=false;if((marr["last"])&&((cur-marr.last)<=rate)){if(dojo.event._canTimeout){if(marr["delayTimer"]){clearTimeout(marr.delayTimer);}
var tod=parseInt(rate*2);var mcpy=dojo.lang.shallowCopy(marr);marr.delayTimer=setTimeout(function(){mcpy[5]=0;_1c8(mcpy);},tod);}
return;}else{marr.last=cur;}}
if(_1cd){_1cc[_1cd].call(_1cc,to);}else{if((_1d3)&&((dojo.render.html)||(dojo.render.svg))){dj_global["setTimeout"](function(){if(msg){_1ca[_1cb].call(_1ca,to);}else{_1ca[_1cb].apply(_1ca,args);}},_1d2);}else{if(msg){_1ca[_1cb].call(_1ca,to);}else{_1ca[_1cb].apply(_1ca,args);}}
}};var _1d9=function(){if(this.squelch){try{return _1c8.apply(this,arguments);}
catch(e){dojo.debug(e);}}else{return _1c8.apply(this,arguments);}};if((this["before"])&&(this.before.length>0)){dojo.lang.forEach(this.before.concat(new Array()),_1d9);}
var _1da;try{if((this["around"])&&(this.around.length>0)){var mi=new dojo.event.MethodInvocation(this,obj,args);_1da=mi.proceed();}else{if(this.methodfunc){_1da=this.object[this.methodname].apply(this.object,args);}}
}
catch(e){if(!this.squelch){dojo.debug(e,"when calling",this.methodname,"on",this.object,"with arguments",args);dojo.raise(e);}}
if((this["after"])&&(this.after.length>0)){dojo.lang.forEach(this.after.concat(new Array()),_1d9);}
return (this.methodfunc)?_1da:null;},getArr:function(kind){var type="after";if((typeof kind=="string")&&(kind.indexOf("before")!=-1)){type="before";}else{if(kind=="around"){type="around";}}
if(!this[type]){this[type]=[];}
return this[type];},kwAddAdvice:function(args){this.addAdvice(args["adviceObj"],args["adviceFunc"],args["aroundObj"],args["aroundFunc"],args["adviceType"],args["precedence"],args["once"],args["delay"],args["rate"],args["adviceMsg"],args["maxCalls"]);},addAdvice:function(_1df,_1e0,_1e1,_1e2,_1e3,_1e4,once,_1e6,rate,_1e8,_1e9){var arr=this.getArr(_1e3);if(!arr){dojo.raise("bad this: "+this);}
var ao=[_1df,_1e0,_1e1,_1e2,_1e6,rate,_1e8,_1e9];if(once){if(this.hasAdvice(_1df,_1e0,_1e3,arr)>=0){return;}}
if(_1e4=="first"){arr.unshift(ao);}else{arr.push(ao);}},hasAdvice:function(_1ec,_1ed,_1ee,arr){if(!arr){arr=this.getArr(_1ee);}
var ind=-1;for(var x=0;x<arr.length;x++){var aao=(typeof _1ed=="object")?(new String(_1ed)).toString():_1ed;var a1o=(typeof arr[x][1]=="object")?(new String(arr[x][1])).toString():arr[x][1];if((arr[x][0]==_1ec)&&(a1o==aao)){ind=x;}}
return ind;},removeAdvice:function(_1f4,_1f5,_1f6,once){var arr=this.getArr(_1f6);var ind=this.hasAdvice(_1f4,_1f5,_1f6,arr);if(ind==-1){return false;}
while(ind!=-1){arr.splice(ind,1);if(once){break;}
ind=this.hasAdvice(_1f4,_1f5,_1f6,arr);}
return true;}});dojo._ie_clobber=new function(){this.clobberNodes=[];function nukeProp(node,prop){try{node[prop]=null;}
catch(e){}
try{delete node[prop];}
catch(e){}
try{node.removeAttribute(prop);}
catch(e){}}
this.clobber=function(_1fc){var na;var tna;if(_1fc){tna=_1fc.all||_1fc.getElementsByTagName("*");na=[_1fc];for(var x=0;x<tna.length;x++){if(tna[x]["__doClobber__"]){na.push(tna[x]);}}
}else{try{window.onload=null;}
catch(e){}
na=(this.clobberNodes.length)?this.clobberNodes:document.all;}
tna=null;var _200={};for(var i=na.length-1;i>=0;i=i-1){var el=na[i];try{if(el&&el["__clobberAttrs__"]){for(var j=0;j<el.__clobberAttrs__.length;j++){nukeProp(el,el.__clobberAttrs__[j]);}
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
dojo.event.browser=new function(){var _205=0;this.normalizedEventName=function(_206){switch(_206){case "CheckboxStateChange":
case "DOMAttrModified":
case "DOMMenuItemActive":
case "DOMMenuItemInactive":
case "DOMMouseScroll":
case "DOMNodeInserted":
case "DOMNodeRemoved":
case "RadioStateChange":
return _206;break;default:
var lcn=_206.toLowerCase();return (lcn.indexOf("on")==0)?lcn.substr(2):lcn;break;}};this.clean=function(node){if(dojo.render.html.ie){dojo._ie_clobber.clobber(node);}};this.addClobberNode=function(node){if(!dojo.render.html.ie){return;}
if(!node["__doClobber__"]){node.__doClobber__=true;dojo._ie_clobber.clobberNodes.push(node);node.__clobberAttrs__=[];}};this.addClobberNodeAttrs=function(node,_20b){if(!dojo.render.html.ie){return;}
this.addClobberNode(node);for(var x=0;x<_20b.length;x++){node.__clobberAttrs__.push(_20b[x]);}};this.removeListener=function(node,_20e,fp,_210){if(!_210){var _210=false;}
_20e=dojo.event.browser.normalizedEventName(_20e);if(_20e=="key"){if(dojo.render.html.ie){this.removeListener(node,"onkeydown",fp,_210);}
_20e="keypress";}
if(node.removeEventListener){node.removeEventListener(_20e,fp,_210);}};this.addListener=function(node,_212,fp,_214,_215){if(!node){return;}
if(!_214){var _214=false;}
_212=dojo.event.browser.normalizedEventName(_212);if(_212=="key"){if(dojo.render.html.ie){this.addListener(node,"onkeydown",fp,_214,_215);}
_212="onkeypress";}
if(!_215){var _216=function(evt){if(!evt){evt=window.event;}
var ret=fp(dojo.event.browser.fixEvent(evt,this));if(_214){dojo.event.browser.stopEvent(evt);}
return ret;};}else{_216=fp;}
if(node.addEventListener){node.addEventListener(_212,_216,_214);return _216;}else{_212="on"+_212;if(typeof node[_212]=="function"){var _219=node[_212];node[_212]=function(e){_219(e);return _216(e);};}else{node[_212]=_216;}
if(dojo.render.html.ie){this.addClobberNodeAttrs(node,[_212]);}
return _216;}};this.isEvent=function(obj){return (typeof obj!="undefined")&&(obj)&&(typeof Event!="undefined")&&(obj.eventPhase);};this.currentEvent=null;this.callListener=function(_21c,_21d){if(typeof _21c!="function"){dojo.raise("listener not a function: "+_21c);}
dojo.event.browser.currentEvent.currentTarget=_21d;return _21c.call(_21d,dojo.event.browser.currentEvent);};this._stopPropagation=function(){dojo.event.browser.currentEvent.cancelBubble=true;};this._preventDefault=function(){dojo.event.browser.currentEvent.returnValue=false;};this.keys={KEY_BACKSPACE:8,KEY_TAB:9,KEY_CLEAR:12,KEY_ENTER:13,KEY_SHIFT:16,KEY_CTRL:17,KEY_ALT:18,KEY_PAUSE:19,KEY_CAPS_LOCK:20,KEY_ESCAPE:27,KEY_SPACE:32,KEY_PAGE_UP:33,KEY_PAGE_DOWN:34,KEY_END:35,KEY_HOME:36,KEY_LEFT_ARROW:37,KEY_UP_ARROW:38,KEY_RIGHT_ARROW:39,KEY_DOWN_ARROW:40,KEY_INSERT:45,KEY_DELETE:46,KEY_HELP:47,KEY_LEFT_WINDOW:91,KEY_RIGHT_WINDOW:92,KEY_SELECT:93,KEY_NUMPAD_0:96,KEY_NUMPAD_1:97,KEY_NUMPAD_2:98,KEY_NUMPAD_3:99,KEY_NUMPAD_4:100,KEY_NUMPAD_5:101,KEY_NUMPAD_6:102,KEY_NUMPAD_7:103,KEY_NUMPAD_8:104,KEY_NUMPAD_9:105,KEY_NUMPAD_MULTIPLY:106,KEY_NUMPAD_PLUS:107,KEY_NUMPAD_ENTER:108,KEY_NUMPAD_MINUS:109,KEY_NUMPAD_PERIOD:110,KEY_NUMPAD_DIVIDE:111,KEY_F1:112,KEY_F2:113,KEY_F3:114,KEY_F4:115,KEY_F5:116,KEY_F6:117,KEY_F7:118,KEY_F8:119,KEY_F9:120,KEY_F10:121,KEY_F11:122,KEY_F12:123,KEY_F13:124,KEY_F14:125,KEY_F15:126,KEY_NUM_LOCK:144,KEY_SCROLL_LOCK:145};this.revKeys=[];for(var key in this.keys){this.revKeys[this.keys[key]]=key;}
this.fixEvent=function(evt,_220){if(!evt){if(window["event"]){evt=window.event;}}
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
if(evt.ctrlKey||evt.altKey){var _222=evt.keyCode;if(_222>=65&&_222<=90&&evt.shiftKey==false){_222+=32;}
if(_222>=1&&_222<=26&&evt.ctrlKey){_222+=96;}
evt.key=String.fromCharCode(_222);}}
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
var _222=evt.which;if((evt.ctrlKey||evt.altKey||evt.metaKey)&&(evt.which>=65&&evt.which<=90&&evt.shiftKey==false)){_222+=32;}
evt.key=String.fromCharCode(_222);}}
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
if(!evt.currentTarget){evt.currentTarget=(_220?_220:evt.srcElement);}
if(!evt.layerX){evt.layerX=evt.offsetX;}
if(!evt.layerY){evt.layerY=evt.offsetY;}
var doc=(evt.srcElement&&evt.srcElement.ownerDocument)?evt.srcElement.ownerDocument:document;var _224=((dojo.render.html.ie55)||(doc["compatMode"]=="BackCompat"))?doc.body:doc.documentElement;if(!evt.pageX){evt.pageX=evt.clientX+(_224.scrollLeft||0);}
if(!evt.pageY){evt.pageY=evt.clientY+(_224.scrollTop||0);}
if(evt.type=="mouseover"){evt.relatedTarget=evt.fromElement;}
if(evt.type=="mouseout"){evt.relatedTarget=evt.toElement;}
this.currentEvent=evt;evt.callListener=this.callListener;evt.stopPropagation=this._stopPropagation;evt.preventDefault=this._preventDefault;}
return evt;};this.stopEvent=function(evt){if(window.event){evt.cancelBubble=true;evt.returnValue=false;}else{evt.preventDefault();evt.stopPropagation();}};};dojo.dom.ELEMENT_NODE=1;dojo.dom.ATTRIBUTE_NODE=2;dojo.dom.TEXT_NODE=3;dojo.dom.CDATA_SECTION_NODE=4;dojo.dom.ENTITY_REFERENCE_NODE=5;dojo.dom.ENTITY_NODE=6;dojo.dom.PROCESSING_INSTRUCTION_NODE=7;dojo.dom.COMMENT_NODE=8;dojo.dom.DOCUMENT_NODE=9;dojo.dom.DOCUMENT_TYPE_NODE=10;dojo.dom.DOCUMENT_FRAGMENT_NODE=11;dojo.dom.NOTATION_NODE=12;dojo.dom.dojoml="http://www.dojotoolkit.org/2004/dojoml";dojo.dom.xmlns={svg:"http://www.w3.org/2000/svg",smil:"http://www.w3.org/2001/SMIL20/",mml:"http://www.w3.org/1998/Math/MathML",cml:"http://www.xml-cml.org",xlink:"http://www.w3.org/1999/xlink",xhtml:"http://www.w3.org/1999/xhtml",xul:"http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul",xbl:"http://www.mozilla.org/xbl",fo:"http://www.w3.org/1999/XSL/Format",xsl:"http://www.w3.org/1999/XSL/Transform",xslt:"http://www.w3.org/1999/XSL/Transform",xi:"http://www.w3.org/2001/XInclude",xforms:"http://www.w3.org/2002/01/xforms",saxon:"http://icl.com/saxon",xalan:"http://xml.apache.org/xslt",xsd:"http://www.w3.org/2001/XMLSchema",dt:"http://www.w3.org/2001/XMLSchema-datatypes",xsi:"http://www.w3.org/2001/XMLSchema-instance",rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#",rdfs:"http://www.w3.org/2000/01/rdf-schema#",dc:"http://purl.org/dc/elements/1.1/",dcq:"http://purl.org/dc/qualifiers/1.0","soap-env":"http://schemas.xmlsoap.org/soap/envelope/",wsdl:"http://schemas.xmlsoap.org/wsdl/",AdobeExtensions:"http://ns.adobe.com/AdobeSVGViewerExtensions/3.0/"};dojo.dom.isNode=function(wh){if(typeof Element=="function"){try{return wh instanceof Element;}
catch(e){}}else{return wh&&!isNaN(wh.nodeType);}};dojo.dom.getUniqueId=function(){var _227=dojo.doc();do{var id="dj_unique_"+(++arguments.callee._idIncrement);}while(_227.getElementById(id));return id;};dojo.dom.getUniqueId._idIncrement=0;dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_229,_22a){var node=_229.firstChild;while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){node=node.nextSibling;}
if(_22a&&node&&node.tagName&&node.tagName.toLowerCase()!=_22a.toLowerCase()){node=dojo.dom.nextElement(node,_22a);}
return node;};dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_22c,_22d){var node=_22c.lastChild;while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){node=node.previousSibling;}
if(_22d&&node&&node.tagName&&node.tagName.toLowerCase()!=_22d.toLowerCase()){node=dojo.dom.prevElement(node,_22d);}
return node;};dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(node,_230){if(!node){return null;}
do{node=node.nextSibling;}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);if(node&&_230&&_230.toLowerCase()!=node.tagName.toLowerCase()){return dojo.dom.nextElement(node,_230);}
return node;};dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(node,_232){if(!node){return null;}
if(_232){_232=_232.toLowerCase();}
do{node=node.previousSibling;}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);if(node&&_232&&_232.toLowerCase()!=node.tagName.toLowerCase()){return dojo.dom.prevElement(node,_232);}
return node;};dojo.dom.moveChildren=function(_233,_234,trim){var _236=0;if(trim){while(_233.hasChildNodes()&&_233.firstChild.nodeType==dojo.dom.TEXT_NODE){_233.removeChild(_233.firstChild);}
while(_233.hasChildNodes()&&_233.lastChild.nodeType==dojo.dom.TEXT_NODE){_233.removeChild(_233.lastChild);}}
while(_233.hasChildNodes()){_234.appendChild(_233.firstChild);_236++;}
return _236;};dojo.dom.copyChildren=function(_237,_238,trim){var _23a=_237.cloneNode(true);return this.moveChildren(_23a,_238,trim);};dojo.dom.replaceChildren=function(node,_23c){var _23d=[];if(dojo.render.html.ie){for(var i=0;i<node.childNodes.length;i++){_23d.push(node.childNodes[i]);}}
dojo.dom.removeChildren(node);node.appendChild(_23c);for(var i=0;i<_23d.length;i++){dojo.dom.destroyNode(_23d[i]);}};dojo.dom.removeChildren=function(node){var _240=node.childNodes.length;while(node.hasChildNodes()){dojo.dom.removeNode(node.firstChild);}
return _240;};dojo.dom.replaceNode=function(node,_242){return node.parentNode.replaceChild(_242,node);};dojo.dom.destroyNode=function(node){if(node.parentNode){node=dojo.dom.removeNode(node);}
if(node.nodeType!=3){if(dojo.exists("dojo.event.browser.clean")){dojo.event.browser.clean(node);}
if(dojo.render.html.ie){node.outerHTML="";}}
};dojo.dom.removeNode=function(node){if(node&&node.parentNode){return node.parentNode.removeChild(node);}};dojo.dom.getAncestors=function(node,_246,_247){var _248=[];var _249=(_246&&(_246 instanceof Function||typeof _246=="function"));while(node){if(!_249||_246(node)){_248.push(node);}
if(_247&&_248.length>0){return _248[0];}
node=node.parentNode;}
if(_247){return null;}
return _248;};dojo.dom.getAncestorsByTag=function(node,tag,_24c){tag=tag.toLowerCase();return dojo.dom.getAncestors(node,function(el){return ((el.tagName)&&(el.tagName.toLowerCase()==tag));},_24c);};dojo.dom.getFirstAncestorByTag=function(node,tag){return dojo.dom.getAncestorsByTag(node,tag,true);};dojo.dom.isDescendantOf=function(node,_251,_252){if(_252&&node){node=node.parentNode;}
while(node){if(node==_251){return true;}
node=node.parentNode;}
return false;};dojo.dom.innerXML=function(node){if(node.innerXML){return node.innerXML;}else{if(node.xml){return node.xml;}else{if(typeof XMLSerializer!="undefined"){return (new XMLSerializer()).serializeToString(node);}}
}};dojo.dom.createDocument=function(){var doc=null;var _255=dojo.doc();if(!dj_undef("ActiveXObject")){var _256=["MSXML2","Microsoft","MSXML","MSXML3"];for(var i=0;i<_256.length;i++){try{doc=new ActiveXObject(_256[i]+".XMLDOM");}
catch(e){}
if(doc){break;}}
}else{if((_255.implementation)&&(_255.implementation.createDocument)){doc=_255.implementation.createDocument("","",null);}}
return doc;};dojo.dom.createDocumentFromText=function(str,_259){if(!_259){_259="text/xml";}
if(!dj_undef("DOMParser")){var _25a=new DOMParser();return _25a.parseFromString(str,_259);}else{if(!dj_undef("ActiveXObject")){var _25b=dojo.dom.createDocument();if(_25b){_25b.async=false;_25b.loadXML(str);return _25b;}else{dojo.debug("toXml didn't work?");}}else{var _25c=dojo.doc();if(_25c.createElement){var tmp=_25c.createElement("xml");tmp.innerHTML=str;if(_25c.implementation&&_25c.implementation.createDocument){var _25e=_25c.implementation.createDocument("foo","",null);for(var i=0;i<tmp.childNodes.length;i++){_25e.importNode(tmp.childNodes.item(i),true);}
return _25e;}
return ((tmp.document)&&(tmp.document.firstChild?tmp.document.firstChild:tmp));}}
}
return null;};dojo.dom.prependChild=function(node,_261){if(_261.firstChild){_261.insertBefore(node,_261.firstChild);}else{_261.appendChild(node);}
return true;};dojo.dom.insertBefore=function(node,ref,_264){if((_264!=true)&&(node===ref||node.nextSibling===ref)){return false;}
var _265=ref.parentNode;_265.insertBefore(node,ref);return true;};dojo.dom.insertAfter=function(node,ref,_268){var pn=ref.parentNode;if(ref==pn.lastChild){if((_268!=true)&&(node===ref)){return false;}
pn.appendChild(node);}else{return this.insertBefore(node,ref.nextSibling,_268);}
return true;};dojo.dom.insertAtPosition=function(node,ref,_26c){if((!node)||(!ref)||(!_26c)){return false;}
switch(_26c.toLowerCase()){case "before":
return dojo.dom.insertBefore(node,ref);case "after":
return dojo.dom.insertAfter(node,ref);case "first":
if(ref.firstChild){return dojo.dom.insertBefore(node,ref.firstChild);}else{ref.appendChild(node);return true;}
break;default:
ref.appendChild(node);return true;}};dojo.dom.insertAtIndex=function(node,_26e,_26f){var _270=_26e.childNodes;if(!_270.length||_270.length==_26f){_26e.appendChild(node);return true;}
if(_26f==0){return dojo.dom.prependChild(node,_26e);}
return dojo.dom.insertAfter(node,_270[_26f-1]);};dojo.dom.textContent=function(node,text){if(arguments.length>1){var _273=dojo.doc();dojo.dom.replaceChildren(node,_273.createTextNode(text));return text;}else{if(node.textContent!=undefined){return node.textContent;}
var _274="";if(node==null){return _274;}
for(var i=0;i<node.childNodes.length;i++){switch(node.childNodes[i].nodeType){case 1:
case 5:
_274+=dojo.dom.textContent(node.childNodes[i]);break;case 3:
case 2:
case 4:
_274+=node.childNodes[i].nodeValue;break;default:
break;}}
return _274;}};dojo.dom.hasParent=function(node){return Boolean(node&&node.parentNode&&dojo.dom.isNode(node.parentNode));};dojo.dom.isTag=function(node){if(node&&node.tagName){for(var i=1;i<arguments.length;i++){if(node.tagName==String(arguments[i])){return String(arguments[i]);}}
}
return "";};dojo.dom.setAttributeNS=function(elem,_27a,_27b,_27c){if(elem==null||((elem==undefined)&&(typeof elem=="undefined"))){dojo.raise("No element given to dojo.dom.setAttributeNS");}
if(!((elem.setAttributeNS==undefined)&&(typeof elem.setAttributeNS=="undefined"))){elem.setAttributeNS(_27a,_27b,_27c);}else{var _27d=elem.ownerDocument;var _27e=_27d.createNode(2,_27b,_27a);_27e.nodeValue=_27c;elem.setAttributeNode(_27e);}};dojo.string.trim=function(str,wh){if(!str.replace){return str;}
if(!str.length){return str;}
var re=(wh>0)?(/^\s+/):(wh<0)?(/\s+$/):(/^\s+|\s+$/g);return str.replace(re,"");};dojo.string.trimStart=function(str){return dojo.string.trim(str,1);};dojo.string.trimEnd=function(str){return dojo.string.trim(str,-1);};dojo.string.repeat=function(str,_285,_286){var out="";for(var i=0;i<_285;i++){out+=str;if(_286&&i<_285-1){out+=_286;}}
return out;};dojo.string.pad=function(str,len,c,dir){var out=String(str);if(!c){c="0";}
if(!dir){dir=1;}
while(out.length<len){if(dir>0){out=c+out;}else{out+=c;}}
return out;};dojo.string.padLeft=function(str,len,c){return dojo.string.pad(str,len,c,1);};dojo.string.padRight=function(str,len,c){return dojo.string.pad(str,len,c,-1);};dojo.io.transports=[];dojo.io.hdlrFuncNames=["load","error","timeout"];dojo.io.Request=function(url,_295,_296,_297){if((arguments.length==1)&&(arguments[0].constructor==Object)){this.fromKwArgs(arguments[0]);}else{this.url=url;if(_295){this.mimetype=_295;}
if(_296){this.transport=_296;}
if(arguments.length>=4){this.changeUrl=_297;}}
};dojo.lang.extend(dojo.io.Request,{url:"",mimetype:"text/plain",method:"GET",content:undefined,transport:undefined,changeUrl:undefined,formNode:undefined,sync:false,bindSuccess:false,useCache:false,preventCache:false,load:function(type,data,_29a,_29b){},error:function(type,_29d,_29e,_29f){},timeout:function(type,_2a1,_2a2,_2a3){},handle:function(type,data,_2a6,_2a7){},timeoutSeconds:0,abort:function(){},fromKwArgs:function(_2a8){if(_2a8["url"]){_2a8.url=_2a8.url.toString();}
if(_2a8["formNode"]){_2a8.formNode=dojo.byId(_2a8.formNode);}
if(!_2a8["method"]&&_2a8["formNode"]&&_2a8["formNode"].method){_2a8.method=_2a8["formNode"].method;}
if(!_2a8["handle"]&&_2a8["handler"]){_2a8.handle=_2a8.handler;}
if(!_2a8["load"]&&_2a8["loaded"]){_2a8.load=_2a8.loaded;}
if(!_2a8["changeUrl"]&&_2a8["changeURL"]){_2a8.changeUrl=_2a8.changeURL;}
_2a8.encoding=dojo.lang.firstValued(_2a8["encoding"],djConfig["bindEncoding"],"");_2a8.sendTransport=dojo.lang.firstValued(_2a8["sendTransport"],djConfig["ioSendTransport"],false);var _2a9=dojo.lang.isFunction;for(var x=0;x<dojo.io.hdlrFuncNames.length;x++){var fn=dojo.io.hdlrFuncNames[x];if(_2a8[fn]&&_2a9(_2a8[fn])){continue;}
if(_2a8["handle"]&&_2a9(_2a8["handle"])){_2a8[fn]=_2a8.handle;}}
dojo.lang.mixin(this,_2a8);}});dojo.io.Error=function(msg,type,num){this.message=msg;this.type=type||"unknown";this.number=num||0;};dojo.io.transports.addTransport=function(name){this.push(name);this[name]=dojo.io[name];};dojo.io.bind=function(_2b0){if(!(_2b0 instanceof dojo.io.Request)){try{_2b0=new dojo.io.Request(_2b0);}
catch(e){dojo.debug(e);}}
var _2b1="";if(_2b0["transport"]){_2b1=_2b0["transport"];if(!this[_2b1]){dojo.io.sendBindError(_2b0,"No dojo.io.bind() transport with name '"+_2b0["transport"]+"'.");return _2b0;}
if(!this[_2b1].canHandle(_2b0)){dojo.io.sendBindError(_2b0,"dojo.io.bind() transport with name '"+_2b0["transport"]+"' cannot handle this type of request.");return _2b0;}}else{for(var x=0;x<dojo.io.transports.length;x++){var tmp=dojo.io.transports[x];if((this[tmp])&&(this[tmp].canHandle(_2b0))){_2b1=tmp;break;}}
if(_2b1==""){dojo.io.sendBindError(_2b0,"None of the loaded transports for dojo.io.bind()"+" can handle the request.");return _2b0;}}
this[_2b1].bind(_2b0);_2b0.bindSuccess=true;return _2b0;};dojo.io.sendBindError=function(_2b4,_2b5){if((typeof _2b4.error=="function"||typeof _2b4.handle=="function")&&(typeof setTimeout=="function"||typeof setTimeout=="object")){var _2b6=new dojo.io.Error(_2b5);setTimeout(function(){_2b4[(typeof _2b4.error=="function")?"error":"handle"]("error",_2b6,null,_2b4);},50);}else{dojo.raise(_2b5);}};dojo.io.queueBind=function(_2b7){if(!(_2b7 instanceof dojo.io.Request)){try{_2b7=new dojo.io.Request(_2b7);}
catch(e){dojo.debug(e);}}
var _2b8=_2b7.load;_2b7.load=function(){dojo.io._queueBindInFlight=false;var ret=_2b8.apply(this,arguments);dojo.io._dispatchNextQueueBind();return ret;};var _2ba=_2b7.error;_2b7.error=function(){dojo.io._queueBindInFlight=false;var ret=_2ba.apply(this,arguments);dojo.io._dispatchNextQueueBind();return ret;};dojo.io._bindQueue.push(_2b7);dojo.io._dispatchNextQueueBind();return _2b7;};dojo.io._dispatchNextQueueBind=function(){if(!dojo.io._queueBindInFlight){dojo.io._queueBindInFlight=true;if(dojo.io._bindQueue.length>0){dojo.io.bind(dojo.io._bindQueue.shift());}else{dojo.io._queueBindInFlight=false;}}
};dojo.io._bindQueue=[];dojo.io._queueBindInFlight=false;dojo.io.argsFromMap=function(map,_2bd,last){var enc=/utf/i.test(_2bd||"")?encodeURIComponent:dojo.string.encodeAscii;var _2c0=[];var _2c1=new Object();for(var name in map){var _2c3=function(elt){var val=enc(name)+"="+enc(elt);_2c0[(last==name)?"push":"unshift"](val);};if(!_2c1[name]){var _2c6=map[name];if(dojo.lang.isArray(_2c6)){dojo.lang.forEach(_2c6,_2c3);}else{_2c3(_2c6);}}
}
return _2c0.join("&");};dojo.io.setIFrameSrc=function(_2c7,src,_2c9){try{var r=dojo.render.html;if(!_2c9){if(r.safari){_2c7.location=src;}else{frames[_2c7.name].location=src;}}else{var idoc;if(r.ie){idoc=_2c7.contentWindow.document;}else{if(r.safari){idoc=_2c7.document;}else{idoc=_2c7.contentWindow;}}
if(!idoc){_2c7.location=src;return;}else{idoc.location.replace(src);}}
}
catch(e){dojo.debug(e);dojo.debug("setIFrameSrc: "+e);}};dojo.string.substituteParams=function(_2cc,hash){var map=(typeof hash=="object")?hash:dojo.lang.toArray(arguments,1);return _2cc.replace(/\%\{(\w+)\}/g,function(_2cf,key){if(typeof (map[key])!="undefined"&&map[key]!=null){return map[key];}
dojo.raise("Substitution not found: "+key);});};dojo.string.capitalize=function(str){if(!dojo.lang.isString(str)){return "";}
return str.replace(/[^\s]+/g,function(word){return word.substring(0,1).toUpperCase()+word.substring(1);});};dojo.string.isBlank=function(str){if(!dojo.lang.isString(str)){return true;}
return (dojo.string.trim(str).length==0);};dojo.string.encodeAscii=function(str){if(!dojo.lang.isString(str)){return str;}
var ret="";var _2d6=escape(str);var _2d7,re=/%u([0-9A-F]{4})/i;while((_2d7=_2d6.match(re))){var num=Number("0x"+_2d7[1]);var _2da=escape("&#"+num+";");ret+=_2d6.substring(0,_2d7.index)+_2da;_2d6=_2d6.substring(_2d7.index+_2d7[0].length);}
ret+=_2d6.replace(/\+/g,"%2B");return ret;};dojo.string.escape=function(type,str){var args=dojo.lang.toArray(arguments,1);switch(type.toLowerCase()){case "xml":
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
return str;}};dojo.string.escapeXml=function(str,_2df){str=str.replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");if(!_2df){str=str.replace(/'/gm,"&#39;");}
return str;};dojo.string.escapeSql=function(str){return str.replace(/'/gm,"''");};dojo.string.escapeRegExp=function(str,_2e2){return str.replace(/([\.$?*!=:|{}\(\)\[\]\\\/^])/g,function(ch){if(_2e2&&_2e2.indexOf(ch)!=-1){return ch;}
return "\\"+ch;});};dojo.string.escapeJavaScript=function(str){return str.replace(/(["'\f\b\n\t\r])/gm,"\\$1");};dojo.string.escapeString=function(str){return ("\""+str.replace(/(["\\])/g,"\\$1")+"\"").replace(/[\f]/g,"\\f").replace(/[\b]/g,"\\b").replace(/[\n]/g,"\\n").replace(/[\t]/g,"\\t").replace(/[\r]/g,"\\r");};dojo.string.summary=function(str,len){if(!len||str.length<=len){return str;}
return str.substring(0,len).replace(/\.+$/,"")+"...";};dojo.string.endsWith=function(str,end,_2ea){if(_2ea){str=str.toLowerCase();end=end.toLowerCase();}
if((str.length-end.length)<0){return false;}
return str.lastIndexOf(end)==str.length-end.length;};dojo.string.endsWithAny=function(str){for(var i=1;i<arguments.length;i++){if(dojo.string.endsWith(str,arguments[i])){return true;}}
return false;};dojo.string.startsWith=function(str,_2ee,_2ef){if(_2ef){str=str.toLowerCase();_2ee=_2ee.toLowerCase();}
return str.indexOf(_2ee)==0;};dojo.string.startsWithAny=function(str){for(var i=1;i<arguments.length;i++){if(dojo.string.startsWith(str,arguments[i])){return true;}}
return false;};dojo.string.has=function(str){for(var i=1;i<arguments.length;i++){if(str.indexOf(arguments[i])>-1){return true;}}
return false;};dojo.string.normalizeNewlines=function(text,_2f5){if(_2f5=="\n"){text=text.replace(/\r\n/g,"\n");text=text.replace(/\r/g,"\n");}else{if(_2f5=="\r"){text=text.replace(/\r\n/g,"\r");text=text.replace(/\n/g,"\r");}else{text=text.replace(/([^\r])\n/g,"$1\r\n").replace(/\r([^\n])/g,"\r\n$1");}}
return text;};dojo.string.splitEscaped=function(str,_2f7){var _2f8=[];for(var i=0,_2fa=0;i<str.length;i++){if(str.charAt(i)=="\\"){i++;continue;}
if(str.charAt(i)==_2f7){_2f8.push(str.substring(_2fa,i));_2fa=i+1;}}
_2f8.push(str.substr(_2fa));return _2f8;};try{if((!djConfig["preventBackButtonFix"])&&(!dojo.hostenv.post_load_)){document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(djConfig["dojoIframeHistoryUrl"]||dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"'></iframe>");}}
catch(e){}
if(dojo.render.html.opera){dojo.debug("Opera is not supported with dojo.undo.browser, so back/forward detection will not work.");}
dojo.undo.browser={initialHref:(!dj_undef("window"))?window.location.href:"",initialHash:(!dj_undef("window"))?window.location.hash:"",moveForward:false,historyStack:[],forwardStack:[],historyIframe:null,bookmarkAnchor:null,locationTimer:null,setInitialState:function(args){this.initialState=this._createState(this.initialHref,args,this.initialHash);},addToHistory:function(args){this.forwardStack=[];var hash=null;var url=null;if(!this.historyIframe){if(djConfig["useXDomain"]&&!djConfig["dojoIframeHistoryUrl"]){dojo.debug("dojo.undo.browser: When using cross-domain Dojo builds,"+" please save iframe_history.html to your domain and set djConfig.dojoIframeHistoryUrl"+" to the path on your domain to iframe_history.html");}
this.historyIframe=window.frames["djhistory"];}
if(!this.bookmarkAnchor){this.bookmarkAnchor=document.createElement("a");dojo.body().appendChild(this.bookmarkAnchor);this.bookmarkAnchor.style.display="none";}
if(args["changeUrl"]){hash="#"+((args["changeUrl"]!==true)?args["changeUrl"]:(new Date()).getTime());if(this.historyStack.length==0&&this.initialState.urlHash==hash){this.initialState=this._createState(url,args,hash);return;}else{if(this.historyStack.length>0&&this.historyStack[this.historyStack.length-1].urlHash==hash){this.historyStack[this.historyStack.length-1]=this._createState(url,args,hash);return;}}
this.changingUrl=true;setTimeout("window.location.href = '"+hash+"'; dojo.undo.browser.changingUrl = false;",1);this.bookmarkAnchor.href=hash;if(dojo.render.html.ie){url=this._loadIframeHistory();var _2ff=args["back"]||args["backButton"]||args["handle"];var tcb=function(_301){if(window.location.hash!=""){setTimeout("window.location.href = '"+hash+"';",1);}
_2ff.apply(this,[_301]);};if(args["back"]){args.back=tcb;}else{if(args["backButton"]){args.backButton=tcb;}else{if(args["handle"]){args.handle=tcb;}}
}
var _302=args["forward"]||args["forwardButton"]||args["handle"];var tfw=function(_304){if(window.location.hash!=""){window.location.href=hash;}
if(_302){_302.apply(this,[_304]);}};if(args["forward"]){args.forward=tfw;}else{if(args["forwardButton"]){args.forwardButton=tfw;}else{if(args["handle"]){args.handle=tfw;}}
}}else{if(dojo.render.html.moz){if(!this.locationTimer){this.locationTimer=setInterval("dojo.undo.browser.checkLocation();",200);}}
}}else{url=this._loadIframeHistory();}
this.historyStack.push(this._createState(url,args,hash));},checkLocation:function(){if(!this.changingUrl){var hsl=this.historyStack.length;if((window.location.hash==this.initialHash||window.location.href==this.initialHref)&&(hsl==1)){this.handleBackButton();return;}
if(this.forwardStack.length>0){if(this.forwardStack[this.forwardStack.length-1].urlHash==window.location.hash){this.handleForwardButton();return;}}
if((hsl>=2)&&(this.historyStack[hsl-2])){if(this.historyStack[hsl-2].urlHash==window.location.hash){this.handleBackButton();return;}}
}},iframeLoaded:function(evt,_307){if(!dojo.render.html.opera){var _308=this._getUrlQuery(_307.href);if(_308==null){if(this.historyStack.length==1){this.handleBackButton();}
return;}
if(this.moveForward){this.moveForward=false;return;}
if(this.historyStack.length>=2&&_308==this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){this.handleBackButton();}else{if(this.forwardStack.length>0&&_308==this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){this.handleForwardButton();}}
}},handleBackButton:function(){var _309=this.historyStack.pop();if(!_309){return;}
var last=this.historyStack[this.historyStack.length-1];if(!last&&this.historyStack.length==0){last=this.initialState;}
if(last){if(last.kwArgs["back"]){last.kwArgs["back"]();}else{if(last.kwArgs["backButton"]){last.kwArgs["backButton"]();}else{if(last.kwArgs["handle"]){last.kwArgs.handle("back");}}
}}
this.forwardStack.push(_309);},handleForwardButton:function(){var last=this.forwardStack.pop();if(!last){return;}
if(last.kwArgs["forward"]){last.kwArgs.forward();}else{if(last.kwArgs["forwardButton"]){last.kwArgs.forwardButton();}else{if(last.kwArgs["handle"]){last.kwArgs.handle("forward");}}
}
this.historyStack.push(last);},_createState:function(url,args,hash){return {"url":url,"kwArgs":args,"urlHash":hash};},_getUrlQuery:function(url){var _310=url.split("?");if(_310.length<2){return null;}else{return _310[1];}},_loadIframeHistory:function(){var url=(djConfig["dojoIframeHistoryUrl"]||dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"?"+(new Date()).getTime();this.moveForward=true;dojo.io.setIFrameSrc(this.historyIframe,url,false);return url;}};if(!dj_undef("window")){dojo.io.checkChildrenForFile=function(node){var _313=false;var _314=node.getElementsByTagName("input");dojo.lang.forEach(_314,function(_315){if(_313){return;}
if(_315.getAttribute("type")=="file"){_313=true;}});return _313;};dojo.io.formHasFile=function(_316){return dojo.io.checkChildrenForFile(_316);};dojo.io.updateNode=function(node,_318){node=dojo.byId(node);var args=_318;if(dojo.lang.isString(_318)){args={url:_318};}
args.mimetype="text/html";args.load=function(t,d,e){while(node.firstChild){dojo.dom.destroyNode(node.firstChild);}
node.innerHTML=d;};dojo.io.bind(args);};dojo.io.formFilter=function(node){var type=(node.type||"").toLowerCase();return !node.disabled&&node.name&&!dojo.lang.inArray(["file","submit","image","reset","button"],type);};dojo.io.encodeForm=function(_31f,_320,_321){if((!_31f)||(!_31f.tagName)||(!_31f.tagName.toLowerCase()=="form")){dojo.raise("Attempted to encode a non-form element.");}
if(!_321){_321=dojo.io.formFilter;}
var enc=/utf/i.test(_320||"")?encodeURIComponent:dojo.string.encodeAscii;var _323=[];for(var i=0;i<_31f.elements.length;i++){var elm=_31f.elements[i];if(!elm||elm.tagName.toLowerCase()=="fieldset"||!_321(elm)){continue;}
var name=enc(elm.name);var type=elm.type.toLowerCase();if(type=="select-multiple"){for(var j=0;j<elm.options.length;j++){if(elm.options[j].selected){_323.push(name+"="+enc(elm.options[j].value));}}
}else{if(dojo.lang.inArray(["radio","checkbox"],type)){if(elm.checked){_323.push(name+"="+enc(elm.value));}}else{_323.push(name+"="+enc(elm.value));}}
}
var _329=_31f.getElementsByTagName("input");for(var i=0;i<_329.length;i++){var _32a=_329[i];if(_32a.type.toLowerCase()=="image"&&_32a.form==_31f&&_321(_32a)){var name=enc(_32a.name);_323.push(name+"="+enc(_32a.value));_323.push(name+".x=0");_323.push(name+".y=0");}}
return _323.join("&")+"&";};dojo.io.FormBind=function(args){this.bindArgs={};if(args&&args.formNode){this.init(args);}else{if(args){this.init({formNode:args});}}
};dojo.lang.extend(dojo.io.FormBind,{form:null,bindArgs:null,clickedButton:null,init:function(args){var form=dojo.byId(args.formNode);if(!form||!form.tagName||form.tagName.toLowerCase()!="form"){throw new Error("FormBind: Couldn't apply, invalid form");}else{if(this.form==form){return;}else{if(this.form){throw new Error("FormBind: Already applied to a form");}}
}
dojo.lang.mixin(this.bindArgs,args);this.form=form;this.connect(form,"onsubmit","submit");for(var i=0;i<form.elements.length;i++){var node=form.elements[i];if(node&&node.type&&dojo.lang.inArray(["submit","button"],node.type.toLowerCase())){this.connect(node,"onclick","click");}}
var _330=form.getElementsByTagName("input");for(var i=0;i<_330.length;i++){var _331=_330[i];if(_331.type.toLowerCase()=="image"&&_331.form==form){this.connect(_331,"onclick","click");}}
},onSubmit:function(form){return true;},submit:function(e){e.preventDefault();if(this.onSubmit(this.form)){dojo.io.bind(dojo.lang.mixin(this.bindArgs,{formFilter:dojo.lang.hitch(this,"formFilter")}));}},click:function(e){var node=e.currentTarget;if(node.disabled){return;}
this.clickedButton=node;},formFilter:function(node){var type=(node.type||"").toLowerCase();var _338=false;if(node.disabled||!node.name){_338=false;}else{if(dojo.lang.inArray(["submit","button","image"],type)){if(!this.clickedButton){this.clickedButton=node;}
_338=node==this.clickedButton;}else{_338=!dojo.lang.inArray(["file","submit","reset","button"],type);}}
return _338;},connect:function(_339,_33a,_33b){if(dojo.evalObjPath("dojo.event.connect")){dojo.event.connect(_339,_33a,this,_33b);}else{var fcn=dojo.lang.hitch(this,_33b);_339[_33a]=function(e){if(!e){e=window.event;}
if(!e.currentTarget){e.currentTarget=e.srcElement;}
if(!e.preventDefault){e.preventDefault=function(){window.event.returnValue=false;};}
fcn(e);};}}});dojo.io.XMLHTTPTransport=new function(){var _33e=this;var _33f={};this.useCache=false;this.preventCache=false;function getCacheKey(url,_341,_342){return url+"|"+_341+"|"+_342.toLowerCase();}
function addToCache(url,_344,_345,http){_33f[getCacheKey(url,_344,_345)]=http;}
function getFromCache(url,_348,_349){return _33f[getCacheKey(url,_348,_349)];}
this.clearCache=function(){_33f={};};function doLoad(_34a,http,url,_34d,_34e){if(((http.status>=200)&&(http.status<300))||(http.status==304)||(location.protocol=="file:"&&(http.status==0||http.status==undefined))||(location.protocol=="chrome:"&&(http.status==0||http.status==undefined))){var ret;if(_34a.method.toLowerCase()=="head"){var _350=http.getAllResponseHeaders();ret={};ret.toString=function(){return _350;};var _351=_350.split(/[\r\n]+/g);for(var i=0;i<_351.length;i++){var pair=_351[i].match(/^([^:]+)\s*:\s*(.+)$/i);if(pair){ret[pair[1]]=pair[2];}}
}else{if(_34a.mimetype=="text/javascript"){try{ret=dj_eval(http.responseText);}
catch(e){dojo.debug(e);dojo.debug(http.responseText);ret=null;}}else{if(_34a.mimetype=="text/json"||_34a.mimetype=="application/json"){try{ret=dj_eval("("+http.responseText+")");}
catch(e){dojo.debug(e);dojo.debug(http.responseText);ret=false;}}else{if((_34a.mimetype=="application/xml")||(_34a.mimetype=="text/xml")){ret=http.responseXML;if(!ret||typeof ret=="string"||!http.getResponseHeader("Content-Type")){ret=dojo.dom.createDocumentFromText(http.responseText);}}else{ret=http.responseText;}}
}}
if(_34e){addToCache(url,_34d,_34a.method,http);}
_34a[(typeof _34a.load=="function")?"load":"handle"]("load",ret,http,_34a);}else{var _354=new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);_34a[(typeof _34a.error=="function")?"error":"handle"]("error",_354,http,_34a);}}
function setHeaders(http,_356){if(_356["headers"]){for(var _357 in _356["headers"]){if(_357.toLowerCase()=="content-type"&&!_356["contentType"]){_356["contentType"]=_356["headers"][_357];}else{http.setRequestHeader(_357,_356["headers"][_357]);}}
}}
this.inFlight=[];this.inFlightTimer=null;this.startWatchingInFlight=function(){if(!this.inFlightTimer){this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);}};this.watchInFlight=function(){var now=null;if(!dojo.hostenv._blockAsync&&!_33e._blockAsync){for(var x=this.inFlight.length-1;x>=0;x--){try{var tif=this.inFlight[x];if(!tif||tif.http._aborted||!tif.http.readyState){this.inFlight.splice(x,1);continue;}
if(4==tif.http.readyState){this.inFlight.splice(x,1);doLoad(tif.req,tif.http,tif.url,tif.query,tif.useCache);}else{if(tif.startTime){if(!now){now=(new Date()).getTime();}
if(tif.startTime+(tif.req.timeoutSeconds*1000)<now){if(typeof tif.http.abort=="function"){tif.http.abort();}
this.inFlight.splice(x,1);tif.req[(typeof tif.req.timeout=="function")?"timeout":"handle"]("timeout",null,tif.http,tif.req);}}
}}
catch(e){try{var _35b=new dojo.io.Error("XMLHttpTransport.watchInFlight Error: "+e);tif.req[(typeof tif.req.error=="function")?"error":"handle"]("error",_35b,tif.http,tif.req);}
catch(e2){dojo.debug("XMLHttpTransport error callback failed: "+e2);}}
}}
clearTimeout(this.inFlightTimer);if(this.inFlight.length==0){this.inFlightTimer=null;return;}
this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);};var _35c=dojo.hostenv.getXmlhttpObject()?true:false;this.canHandle=function(_35d){return _35c&&dojo.lang.inArray(["text/plain","text/html","application/xml","text/xml","text/javascript","text/json","application/json"],(_35d["mimetype"].toLowerCase()||""))&&!(_35d["formNode"]&&dojo.io.formHasFile(_35d["formNode"]));};this.multipartBoundary="45309FFF-BD65-4d50-99C9-36986896A96F";this.bind=function(_35e){var url=_35e.url;var _360="";if(_35e["formNode"]){var ta=_35e.formNode.getAttribute("action");if((ta)&&(!_35e["url"])){url=ta;}
var tp=_35e.formNode.getAttribute("method");if((tp)&&(!_35e["method"])){_35e.method=tp;}
_360+=dojo.io.encodeForm(_35e.formNode,_35e.encoding,_35e["formFilter"]);}
if(url.indexOf("#")>-1){dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);url=url.split("#")[0];}
if(_35e["file"]){_35e.method="post";}
if(!_35e["method"]){_35e.method="get";}
if(_35e.method.toLowerCase()=="get"){_35e.multipart=false;}else{if(_35e["file"]){_35e.multipart=true;}else{if(!_35e["multipart"]){_35e.multipart=false;}}
}
if(_35e["backButton"]||_35e["back"]||_35e["changeUrl"]){dojo.undo.browser.addToHistory(_35e);}
var _363=_35e["content"]||{};if(_35e.sendTransport){_363["dojo.transport"]="xmlhttp";}
do{if(_35e.postContent){_360=_35e.postContent;break;}
if(_363){_360+=dojo.io.argsFromMap(_363,_35e.encoding);}
if(_35e.method.toLowerCase()=="get"||!_35e.multipart){break;}
var t=[];if(_360.length){var q=_360.split("&");for(var i=0;i<q.length;++i){if(q[i].length){var p=q[i].split("=");t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+p[0]+"\"","",p[1]);}}
}
if(_35e.file){if(dojo.lang.isArray(_35e.file)){for(var i=0;i<_35e.file.length;++i){var o=_35e.file[i];t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);}}else{var o=_35e.file;t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);}}
if(t.length){t.push("--"+this.multipartBoundary+"--","");_360=t.join("\r\n");}}while(false);var _369=_35e["sync"]?false:true;var _36a=_35e["preventCache"]||(this.preventCache==true&&_35e["preventCache"]!=false);var _36b=_35e["useCache"]==true||(this.useCache==true&&_35e["useCache"]!=false);if(!_36a&&_36b){var _36c=getFromCache(url,_360,_35e.method);if(_36c){doLoad(_35e,_36c,url,_360,false);return;}}
var http=dojo.hostenv.getXmlhttpObject(_35e);var _36e=false;if(_369){var _36f=this.inFlight.push({"req":_35e,"http":http,"url":url,"query":_360,"useCache":_36b,"startTime":_35e.timeoutSeconds?(new Date()).getTime():0});this.startWatchingInFlight();}else{_33e._blockAsync=true;}
if(_35e.method.toLowerCase()=="post"){if(!_35e.user){http.open("POST",url,_369);}else{http.open("POST",url,_369,_35e.user,_35e.password);}
setHeaders(http,_35e);http.setRequestHeader("Content-Type",_35e.multipart?("multipart/form-data; boundary="+this.multipartBoundary):(_35e.contentType||"application/x-www-form-urlencoded"));try{http.send(_360);}
catch(e){if(typeof http.abort=="function"){http.abort();}
doLoad(_35e,{status:404},url,_360,_36b);}}else{var _370=url;if(_360!=""){_370+=(_370.indexOf("?")>-1?"&":"?")+_360;}
if(_36a){_370+=(dojo.string.endsWithAny(_370,"?","&")?"":(_370.indexOf("?")>-1?"&":"?"))+"dojo.preventCache="+new Date().valueOf();}
if(!_35e.user){http.open(_35e.method.toUpperCase(),_370,_369);}else{http.open(_35e.method.toUpperCase(),_370,_369,_35e.user,_35e.password);}
setHeaders(http,_35e);try{http.send(null);}
catch(e){if(typeof http.abort=="function"){http.abort();}
doLoad(_35e,{status:404},url,_360,_36b);}}
if(!_369){doLoad(_35e,http,url,_360,_36b);_33e._blockAsync=false;}
_35e.abort=function(){try{http._aborted=true;}
catch(e){}
return http.abort();};return;};dojo.io.transports.addTransport("XMLHTTPTransport");};}
dojo.io.cookie.setCookie=function(name,_372,days,path,_375,_376){var _377=-1;if((typeof days=="number")&&(days>=0)){var d=new Date();d.setTime(d.getTime()+(days*24*60*60*1000));_377=d.toGMTString();}
_372=escape(_372);document.cookie=name+"="+_372+";"+(_377!=-1?" expires="+_377+";":"")+(path?"path="+path:"")+(_375?"; domain="+_375:"")+(_376?"; secure":"");};dojo.io.cookie.set=dojo.io.cookie.setCookie;dojo.io.cookie.getCookie=function(name){var idx=document.cookie.lastIndexOf(name+"=");if(idx==-1){return null;}
var _37b=document.cookie.substring(idx+name.length+1);var end=_37b.indexOf(";");if(end==-1){end=_37b.length;}
_37b=_37b.substring(0,end);_37b=unescape(_37b);return _37b;};dojo.io.cookie.get=dojo.io.cookie.getCookie;dojo.io.cookie.deleteCookie=function(name){dojo.io.cookie.setCookie(name,"-",0);};dojo.io.cookie.setObjectCookie=function(name,obj,days,path,_382,_383,_384){if(arguments.length==5){_384=_382;_382=null;_383=null;}
var _385=[],_386,_387="";if(!_384){_386=dojo.io.cookie.getObjectCookie(name);}
if(days>=0){if(!_386){_386={};}
for(var prop in obj){if(obj[prop]==null){delete _386[prop];}else{if((typeof obj[prop]=="string")||(typeof obj[prop]=="number")){_386[prop]=obj[prop];}}
}
prop=null;for(var prop in _386){_385.push(escape(prop)+"="+escape(_386[prop]));}
_387=_385.join("&");}
dojo.io.cookie.setCookie(name,_387,days,path,_382,_383);};dojo.io.cookie.getObjectCookie=function(name){var _38a=null,_38b=dojo.io.cookie.getCookie(name);if(_38b){_38a={};var _38c=_38b.split("&");for(var i=0;i<_38c.length;i++){var pair=_38c[i].split("=");var _38f=pair[1];if(isNaN(_38f)){_38f=unescape(pair[1]);}
_38a[unescape(pair[0])]=_38f;}}
return _38a;};dojo.io.cookie.isSupported=function(){if(typeof navigator.cookieEnabled!="boolean"){dojo.io.cookie.setCookie("__TestingYourBrowserForCookieSupport__","CookiesAllowed",90,null);var _390=dojo.io.cookie.getCookie("__TestingYourBrowserForCookieSupport__");navigator.cookieEnabled=(_390=="CookiesAllowed");if(navigator.cookieEnabled){this.deleteCookie("__TestingYourBrowserForCookieSupport__");}}
return navigator.cookieEnabled;};if(!dojo.io.cookies){dojo.io.cookies=dojo.io.cookie;}
dojo.date.setDayOfYear=function(_391,_392){_391.setMonth(0);_391.setDate(_392);return _391;};dojo.date.getDayOfYear=function(_393){var _394=_393.getFullYear();var _395=new Date(_394-1,11,31);return Math.floor((_393.getTime()-_395.getTime())/86400000);};dojo.date.setWeekOfYear=function(_396,week,_398){if(arguments.length==2){_398=0;}
dojo.unimplemented("dojo.date.setWeekOfYear");};dojo.date.getWeekOfYear=function(_399,_39a){if(arguments.length==1){_39a=0;}
var _39b=new Date(_399.getFullYear(),0,1);var day=_39b.getDay();_39b.setDate(_39b.getDate()-day+_39a-(day>_39a?7:0));return Math.floor((_399.getTime()-_39b.getTime())/604800000);};dojo.date.setIsoWeekOfYear=function(_39d,week,_39f){if(arguments.length==2){_39f=1;}
dojo.unimplemented("dojo.date.setIsoWeekOfYear");};dojo.date.getIsoWeekOfYear=function(_3a0,_3a1){if(arguments.length==1){_3a1=1;}
dojo.unimplemented("dojo.date.getIsoWeekOfYear");};dojo.date.shortTimezones=["IDLW","BET","HST","MART","AKST","PST","MST","CST","EST","AST","NFT","BST","FST","AT","GMT","CET","EET","MSK","IRT","GST","AFT","AGTT","IST","NPT","ALMT","MMT","JT","AWST","JST","ACST","AEST","LHST","VUT","NFT","NZT","CHAST","PHOT","LINT"];dojo.date.timezoneOffsets=[-720,-660,-600,-570,-540,-480,-420,-360,-300,-240,-210,-180,-120,-60,0,60,120,180,210,240,270,300,330,345,360,390,420,480,540,570,600,630,660,690,720,765,780,840];dojo.date.getDaysInMonth=function(_3a2){var _3a3=_3a2.getMonth();var days=[31,28,31,30,31,30,31,31,30,31,30,31];if(_3a3==1&&dojo.date.isLeapYear(_3a2)){return 29;}else{return days[_3a3];}};dojo.date.isLeapYear=function(_3a5){var year=_3a5.getFullYear();return (year%400==0)?true:(year%100==0)?false:(year%4==0)?true:false;};dojo.date.getTimezoneName=function(_3a7){var str=_3a7.toString();var tz="";var _3aa;var pos=str.indexOf("(");if(pos>-1){pos++;tz=str.substring(pos,str.indexOf(")"));}else{var pat=/([A-Z\/]+) \d{4}$/;if((_3aa=str.match(pat))){tz=_3aa[1];}else{str=_3a7.toLocaleString();pat=/ ([A-Z\/]+)$/;if((_3aa=str.match(pat))){tz=_3aa[1];}}
}
return tz=="AM"||tz=="PM"?"":tz;};dojo.date.getOrdinal=function(_3ad){var date=_3ad.getDate();if(date%100!=11&&date%10==1){return "st";}else{if(date%100!=12&&date%10==2){return "nd";}else{if(date%100!=13&&date%10==3){return "rd";}else{return "th";}}
}};dojo.date.compareTypes={DATE:1,TIME:2};dojo.date.compare=function(_3af,_3b0,_3b1){var dA=_3af;var dB=_3b0||new Date();var now=new Date();with(dojo.date.compareTypes){var opt=_3b1||(DATE|TIME);var d1=new Date((opt&DATE)?dA.getFullYear():now.getFullYear(),(opt&DATE)?dA.getMonth():now.getMonth(),(opt&DATE)?dA.getDate():now.getDate(),(opt&TIME)?dA.getHours():0,(opt&TIME)?dA.getMinutes():0,(opt&TIME)?dA.getSeconds():0);var d2=new Date((opt&DATE)?dB.getFullYear():now.getFullYear(),(opt&DATE)?dB.getMonth():now.getMonth(),(opt&DATE)?dB.getDate():now.getDate(),(opt&TIME)?dB.getHours():0,(opt&TIME)?dB.getMinutes():0,(opt&TIME)?dB.getSeconds():0);}
if(d1.valueOf()>d2.valueOf()){return 1;}
if(d1.valueOf()<d2.valueOf()){return -1;}
return 0;};dojo.date.dateParts={YEAR:0,MONTH:1,DAY:2,HOUR:3,MINUTE:4,SECOND:5,MILLISECOND:6,QUARTER:7,WEEK:8,WEEKDAY:9};dojo.date.add=function(dt,_3b9,incr){if(typeof dt=="number"){dt=new Date(dt);}
function fixOvershoot(){if(sum.getDate()<dt.getDate()){sum.setDate(0);}}
var sum=new Date(dt);with(dojo.date.dateParts){switch(_3b9){case YEAR:
sum.setFullYear(dt.getFullYear()+incr);fixOvershoot();break;case QUARTER:
incr*=3;case MONTH:
sum.setMonth(dt.getMonth()+incr);fixOvershoot();break;case WEEK:
incr*=7;case DAY:
sum.setDate(dt.getDate()+incr);break;case WEEKDAY:
var dat=dt.getDate();var _3bd=0;var days=0;var strt=0;var trgt=0;var adj=0;var mod=incr%5;if(mod==0){days=(incr>0)?5:-5;_3bd=(incr>0)?((incr-5)/5):((incr+5)/5);}else{days=mod;_3bd=parseInt(incr/5);}
strt=dt.getDay();if(strt==6&&incr>0){adj=1;}else{if(strt==0&&incr<0){adj=-1;}}
trgt=(strt+days);if(trgt==0||trgt==6){adj=(incr>0)?2:-2;}
sum.setDate(dat+(7*_3bd)+days+adj);break;case HOUR:
sum.setHours(sum.getHours()+incr);break;case MINUTE:
sum.setMinutes(sum.getMinutes()+incr);break;case SECOND:
sum.setSeconds(sum.getSeconds()+incr);break;case MILLISECOND:
sum.setMilliseconds(sum.getMilliseconds()+incr);break;default:
break;}}
return sum;};dojo.date.diff=function(dtA,dtB,_3c5){if(typeof dtA=="number"){dtA=new Date(dtA);}
if(typeof dtB=="number"){dtB=new Date(dtB);}
var _3c6=dtB.getFullYear()-dtA.getFullYear();var _3c7=(dtB.getMonth()-dtA.getMonth())+(_3c6*12);var _3c8=dtB.getTime()-dtA.getTime();var _3c9=_3c8/1000;var _3ca=_3c9/60;var _3cb=_3ca/60;var _3cc=_3cb/24;var _3cd=_3cc/7;var _3ce=0;with(dojo.date.dateParts){switch(_3c5){case YEAR:
_3ce=_3c6;break;case QUARTER:
var mA=dtA.getMonth();var mB=dtB.getMonth();var qA=Math.floor(mA/3)+1;var qB=Math.floor(mB/3)+1;qB+=(_3c6*4);_3ce=qB-qA;break;case MONTH:
_3ce=_3c7;break;case WEEK:
_3ce=parseInt(_3cd);break;case DAY:
_3ce=_3cc;break;case WEEKDAY:
var days=Math.round(_3cc);var _3d4=parseInt(days/7);var mod=days%7;if(mod==0){days=_3d4*5;}else{var adj=0;var aDay=dtA.getDay();var bDay=dtB.getDay();_3d4=parseInt(days/7);mod=days%7;var _3d9=new Date(dtA);_3d9.setDate(_3d9.getDate()+(_3d4*7));var _3da=_3d9.getDay();if(_3cc>0){switch(true){case aDay==6:
adj=-1;break;case aDay==0:
adj=0;break;case bDay==6:
adj=-1;break;case bDay==0:
adj=-2;break;case (_3da+mod)>5:
adj=-2;break;default:
break;}}else{if(_3cc<0){switch(true){case aDay==6:
adj=0;break;case aDay==0:
adj=1;break;case bDay==6:
adj=2;break;case bDay==0:
adj=1;break;case (_3da+mod)<0:
adj=2;break;default:
break;}}
}
days+=adj;days-=(_3d4*2);}
_3ce=days;break;case HOUR:
_3ce=_3cb;break;case MINUTE:
_3ce=_3ca;break;case SECOND:
_3ce=_3c9;break;case MILLISECOND:
_3ce=_3c8;break;default:
break;}}
return Math.round(_3ce);};dojo.date.getFirstDayOfWeek=function(_3db){var _3dc={mv:5,ae:6,af:6,bh:6,dj:6,dz:6,eg:6,er:6,et:6,iq:6,ir:6,jo:6,ke:6,kw:6,lb:6,ly:6,ma:6,om:6,qa:6,sa:6,sd:6,so:6,tn:6,ye:6,as:0,au:0,az:0,bw:0,ca:0,cn:0,fo:0,ge:0,gl:0,gu:0,hk:0,ie:0,il:0,is:0,jm:0,jp:0,kg:0,kr:0,la:0,mh:0,mo:0,mp:0,mt:0,nz:0,ph:0,pk:0,sg:0,th:0,tt:0,tw:0,um:0,us:0,uz:0,vi:0,za:0,zw:0,et:0,mw:0,ng:0,tj:0,gb:0,sy:4};_3db=dojo.hostenv.normalizeLocale(_3db);var _3dd=_3db.split("-")[1];var dow=_3dc[_3dd];return (typeof dow=="undefined")?1:dow;};dojo.date.getWeekend=function(_3df){var _3e0={eg:5,il:5,sy:5,"in":0,ae:4,bh:4,dz:4,iq:4,jo:4,kw:4,lb:4,ly:4,ma:4,om:4,qa:4,sa:4,sd:4,tn:4,ye:4};var _3e1={ae:5,bh:5,dz:5,iq:5,jo:5,kw:5,lb:5,ly:5,ma:5,om:5,qa:5,sa:5,sd:5,tn:5,ye:5,af:5,ir:5,eg:6,il:6,sy:6};_3df=dojo.hostenv.normalizeLocale(_3df);var _3e2=_3df.split("-")[1];var _3e3=_3e0[_3e2];var end=_3e1[_3e2];if(typeof _3e3=="undefined"){_3e3=6;}
if(typeof end=="undefined"){end=0;}
return {start:_3e3,end:end};};dojo.date.isWeekend=function(_3e5,_3e6){var _3e7=dojo.date.getWeekend(_3e6);var day=(_3e5||new Date()).getDay();if(_3e7.end<_3e7.start){_3e7.end+=7;if(day<_3e7.start){day+=7;}}
return day>=_3e7.start&&day<=_3e7.end;};dojo.i18n.getLocalization=function(_3e9,_3ea,_3eb){dojo.hostenv.preloadLocalizations();_3eb=dojo.hostenv.normalizeLocale(_3eb);var _3ec=_3eb.split("-");var _3ed=[_3e9,"nls",_3ea].join(".");var _3ee=dojo.hostenv.findModule(_3ed,true);var _3ef;for(var i=_3ec.length;i>0;i--){var loc=_3ec.slice(0,i).join("_");if(_3ee[loc]){_3ef=_3ee[loc];break;}}
if(!_3ef){_3ef=_3ee.ROOT;}
if(_3ef){var _3f2=function(){};_3f2.prototype=_3ef;return new _3f2();}
dojo.raise("Bundle not found: "+_3ea+" in "+_3e9+" , locale="+_3eb);};dojo.i18n.isLTR=function(_3f3){var lang=dojo.hostenv.normalizeLocale(_3f3).split("-")[0];var RTL={ar:true,fa:true,he:true,ur:true,yi:true};return !RTL[lang];};(function(){dojo.date.format=function(_3f6,_3f7){function formatPattern(_3f8,_3f9){return _3f9.replace(/([a-z])\1*/ig,function(_3fa){var s;var c=_3fa.charAt(0);var l=_3fa.length;var pad;var _3ff=["abbr","wide","narrow"];switch(c){case "G":
if(l>3){dojo.unimplemented("Era format not implemented");}
s=info.eras[_3f8.getFullYear()<0?1:0];break;case "y":
s=_3f8.getFullYear();switch(l){case 1:
break;case 2:
s=String(s);s=s.substr(s.length-2);break;default:
pad=true;}
break;case "Q":
case "q":
s=Math.ceil((_3f8.getMonth()+1)/3);switch(l){case 1:
case 2:
pad=true;break;case 3:
case 4:
dojo.unimplemented("Quarter format not implemented");}
break;case "M":
case "L":
var m=_3f8.getMonth();var _402;switch(l){case 1:
case 2:
s=m+1;pad=true;break;case 3:
case 4:
case 5:
_402=_3ff[l-3];break;}
if(_402){var type=(c=="L")?"standalone":"format";var prop=["months",type,_402].join("-");s=info[prop][m];}
break;case "w":
var _405=0;s=dojo.date.getWeekOfYear(_3f8,_405);pad=true;break;case "d":
s=_3f8.getDate();pad=true;break;case "D":
s=dojo.date.getDayOfYear(_3f8);pad=true;break;case "E":
case "e":
case "c":
var d=_3f8.getDay();var _402;switch(l){case 1:
case 2:
if(c=="e"){var _407=dojo.date.getFirstDayOfWeek(_3f7.locale);d=(d-_407+7)%7;}
if(c!="c"){s=d+1;pad=true;break;}
case 3:
case 4:
case 5:
_402=_3ff[l-3];break;}
if(_402){var type=(c=="c")?"standalone":"format";var prop=["days",type,_402].join("-");s=info[prop][d];}
break;case "a":
var _408=(_3f8.getHours()<12)?"am":"pm";s=info[_408];break;case "h":
case "H":
case "K":
case "k":
var h=_3f8.getHours();switch(c){case "h":
s=(h%12)||12;break;case "H":
s=h;break;case "K":
s=(h%12);break;case "k":
s=h||24;break;}
pad=true;break;case "m":
s=_3f8.getMinutes();pad=true;break;case "s":
s=_3f8.getSeconds();pad=true;break;case "S":
s=Math.round(_3f8.getMilliseconds()*Math.pow(10,l-3));break;case "v":
case "z":
s=dojo.date.getTimezoneName(_3f8);if(s){break;}
l=4;case "Z":
var _40a=_3f8.getTimezoneOffset();var tz=[(_40a<=0?"+":"-"),dojo.string.pad(Math.floor(Math.abs(_40a)/60),2),dojo.string.pad(Math.abs(_40a)%60,2)];if(l==4){tz.splice(0,0,"GMT");tz.splice(3,0,":");}
s=tz.join("");break;case "Y":
case "u":
case "W":
case "F":
case "g":
case "A":
s="?";break;default:
dojo.raise("dojo.date.format: invalid pattern char: "+_3f9);}
if(pad){s=dojo.string.pad(s,l);}
return s;});}
_3f7=_3f7||{};var _40c=dojo.hostenv.normalizeLocale(_3f7.locale);var _40d=_3f7.formatLength||"full";var info=dojo.date._getGregorianBundle(_40c);var str=[];var _40f=dojo.lang.curry(this,formatPattern,_3f6);if(_3f7.selector!="timeOnly"){var _410=_3f7.datePattern||info["dateFormat-"+_40d];if(_410){str.push(_processPattern(_410,_40f));}}
if(_3f7.selector!="dateOnly"){var _411=_3f7.timePattern||info["timeFormat-"+_40d];if(_411){str.push(_processPattern(_411,_40f));}}
var _412=str.join(" ");return _412;};dojo.date.parse=function(_413,_414){_414=_414||{};var _415=dojo.hostenv.normalizeLocale(_414.locale);var info=dojo.date._getGregorianBundle(_415);var _417=_414.formatLength||"full";if(!_414.selector){_414.selector="dateOnly";}
var _418=_414.datePattern||info["dateFormat-"+_417];var _419=_414.timePattern||info["timeFormat-"+_417];var _41a;if(_414.selector=="dateOnly"){_41a=_418;}else{if(_414.selector=="timeOnly"){_41a=_419;}else{if(_414.selector=="dateTime"){_41a=_418+" "+_419;}else{var msg="dojo.date.parse: Unknown selector param passed: '"+_414.selector+"'.";msg+=" Defaulting to date pattern.";dojo.debug(msg);_41a=_418;}}
}
var _41c=[];var _41d=_processPattern(_41a,dojo.lang.curry(this,_buildDateTimeRE,_41c,info,_414));var _41e=new RegExp("^"+_41d+"$");var _41f=_41e.exec(_413);if(!_41f){return null;}
var _420=["abbr","wide","narrow"];var _421=new Date(1972,0);var _422={};for(var i=1;i<_41f.length;i++){var grp=_41c[i-1];var l=grp.length;var v=_41f[i];switch(grp.charAt(0)){case "y":
if(l!=2){_421.setFullYear(v);_422.year=v;}else{if(v<100){v=Number(v);var year=""+new Date().getFullYear();var _428=year.substring(0,2)*100;var _429=Number(year.substring(2,4));var _42a=Math.min(_429+20,99);var num=(v<_42a)?_428+v:_428-100+v;_421.setFullYear(num);_422.year=num;}else{if(_414.strict){return null;}
_421.setFullYear(v);_422.year=v;}}
break;case "M":
if(l>2){if(!_414.strict){v=v.replace(/\./g,"");v=v.toLowerCase();}
var _42c=info["months-format-"+_420[l-3]].concat();for(var j=0;j<_42c.length;j++){if(!_414.strict){_42c[j]=_42c[j].toLowerCase();}
if(v==_42c[j]){_421.setMonth(j);_422.month=j;break;}}
if(j==_42c.length){return null;}}else{_421.setMonth(v-1);_422.month=v-1;}
break;case "E":
case "e":
if(!_414.strict){v=v.toLowerCase();}
var days=info["days-format-"+_420[l-3]].concat();for(var j=0;j<days.length;j++){if(!_414.strict){days[j]=days[j].toLowerCase();}
if(v==days[j]){break;}}
if(j==days.length){return null;}
break;case "d":
_421.setDate(v);_422.date=v;break;case "D":
dojo.date.setDayOfYear(_421,v);break;case "w":
var _42f=0;dojo.date.setWeekOfYear(_421,v,_42f);break;case "a":
var am=_414.am||info.am;var pm=_414.pm||info.pm;if(!_414.strict){v=v.replace(/\./g,"").toLowerCase();am=am.replace(/\./g,"").toLowerCase();pm=pm.replace(/\./g,"").toLowerCase();}
if(_414.strict&&v!=am&&v!=pm){return null;}
var _432=_421.getHours();if(v==pm&&_432<12){_421.setHours(_432+12);}else{if(v==am&&_432==12){_421.setHours(0);}}
break;case "K":
if(v==24){v=0;}
case "h":
case "H":
case "k":
if(v>23){return null;}
_421.setHours(v);break;case "m":
_421.setMinutes(v);break;case "s":
_421.setSeconds(v);break;case "S":
_421.setMilliseconds(v);break;default:
dojo.unimplemented("dojo.date.parse: unsupported pattern char="+grp.charAt(0));}}
if(_422.year&&_421.getFullYear()!=_422.year){return null;}
if(_422.month&&_421.getMonth()!=_422.month){return null;}
if(_422.date&&_421.getDate()!=_422.date){return null;}
return _421;};function _processPattern(_433,_434,_435,_436){var _437=function(x){return x;};_434=_434||_437;_435=_435||_437;_436=_436||_437;var _439=_433.match(/(''|[^'])+/g);var _43a=false;for(var i=0;i<_439.length;i++){if(!_439[i]){_439[i]="";}else{_439[i]=(_43a?_435:_434)(_439[i]);_43a=!_43a;}}
return _436(_439.join(""));}
function _buildDateTimeRE(_43c,info,_43e,_43f){return _43f.replace(/([a-z])\1*/ig,function(_440){var s;var c=_440.charAt(0);var l=_440.length;switch(c){case "y":
s="\\d"+((l==2)?"{2,4}":"+");break;case "M":
s=(l>2)?"\\S+":"\\d{1,2}";break;case "D":
s="\\d{1,3}";break;case "d":
case "w":
s="\\d{1,2}";break;case "E":
s="\\S+";break;case "h":
case "H":
case "K":
case "k":
s="\\d{1,2}";break;case "m":
case "s":
s="[0-5]\\d";break;case "S":
s="\\d{1,3}";break;case "a":
var am=_43e.am||info.am||"AM";var pm=_43e.pm||info.pm||"PM";if(_43e.strict){s=am+"|"+pm;}else{s=am;s+=(am!=am.toLowerCase())?"|"+am.toLowerCase():"";s+="|";s+=(pm!=pm.toLowerCase())?pm+"|"+pm.toLowerCase():pm;}
break;default:
dojo.unimplemented("parse of date format, pattern="+_43f);}
if(_43c){_43c.push(_440);}
return "\\s*("+s+")\\s*";});}})();dojo.date.strftime=function(_446,_447,_448){var _449=null;function _(s,n){return dojo.string.pad(s,n||2,_449||"0");}
var info=dojo.date._getGregorianBundle(_448);function $(_44d){switch(_44d){case "a":
return dojo.date.getDayShortName(_446,_448);case "A":
return dojo.date.getDayName(_446,_448);case "b":
case "h":
return dojo.date.getMonthShortName(_446,_448);case "B":
return dojo.date.getMonthName(_446,_448);case "c":
return dojo.date.format(_446,{locale:_448});case "C":
return _(Math.floor(_446.getFullYear()/100));case "d":
return _(_446.getDate());case "D":
return $("m")+"/"+$("d")+"/"+$("y");case "e":
if(_449==null){_449=" ";}
return _(_446.getDate());case "f":
if(_449==null){_449=" ";}
return _(_446.getMonth()+1);case "g":
break;case "G":
dojo.unimplemented("unimplemented modifier 'G'");break;case "F":
return $("Y")+"-"+$("m")+"-"+$("d");case "H":
return _(_446.getHours());case "I":
return _(_446.getHours()%12||12);case "j":
return _(dojo.date.getDayOfYear(_446),3);case "k":
if(_449==null){_449=" ";}
return _(_446.getHours());case "l":
if(_449==null){_449=" ";}
return _(_446.getHours()%12||12);case "m":
return _(_446.getMonth()+1);case "M":
return _(_446.getMinutes());case "n":
return "\n";case "p":
return info[_446.getHours()<12?"am":"pm"];case "r":
return $("I")+":"+$("M")+":"+$("S")+" "+$("p");case "R":
return $("H")+":"+$("M");case "S":
return _(_446.getSeconds());case "t":
return "\t";case "T":
return $("H")+":"+$("M")+":"+$("S");case "u":
return String(_446.getDay()||7);case "U":
return _(dojo.date.getWeekOfYear(_446));case "V":
return _(dojo.date.getIsoWeekOfYear(_446));case "W":
return _(dojo.date.getWeekOfYear(_446,1));case "w":
return String(_446.getDay());case "x":
return dojo.date.format(_446,{selector:"dateOnly",locale:_448});case "X":
return dojo.date.format(_446,{selector:"timeOnly",locale:_448});case "y":
return _(_446.getFullYear()%100);case "Y":
return String(_446.getFullYear());case "z":
var _44e=_446.getTimezoneOffset();return (_44e>0?"-":"+")+_(Math.floor(Math.abs(_44e)/60))+":"+_(Math.abs(_44e)%60);case "Z":
return dojo.date.getTimezoneName(_446);case "%":
return "%";}}
var _44f="";var i=0;var _451=0;var _452=null;while((_451=_447.indexOf("%",i))!=-1){_44f+=_447.substring(i,_451++);switch(_447.charAt(_451++)){case "_":
_449=" ";break;case "-":
_449="";break;case "0":
_449="0";break;case "^":
_452="upper";break;case "*":
_452="lower";break;case "#":
_452="swap";break;default:
_449=null;_451--;break;}
var _453=$(_447.charAt(_451++));switch(_452){case "upper":
_453=_453.toUpperCase();break;case "lower":
_453=_453.toLowerCase();break;case "swap":
var _454=_453.toLowerCase();var _455="";var j=0;var ch="";while(j<_453.length){ch=_453.charAt(j);_455+=(ch==_454.charAt(j))?ch.toUpperCase():ch.toLowerCase();j++;}
_453=_455;break;default:
break;}
_452=null;_44f+=_453;i=_451;}
_44f+=_447.substring(i);return _44f;};(function(){var _458=[];dojo.date.addCustomFormats=function(_459,_45a){_458.push({pkg:_459,name:_45a});};dojo.date._getGregorianBundle=function(_45b){var _45c={};dojo.lang.forEach(_458,function(desc){var _45e=dojo.i18n.getLocalization(desc.pkg,desc.name,_45b);_45c=dojo.lang.mixin(_45c,_45e);},this);return _45c;};})();dojo.date.addCustomFormats("dojo.i18n.cldr","gregorian");dojo.date.addCustomFormats("dojo.i18n.cldr","gregorianExtras");dojo.date.getNames=function(item,type,use,_462){var _463;var _464=dojo.date._getGregorianBundle(_462);var _465=[item,use,type];if(use=="standAlone"){_463=_464[_465.join("-")];}
_465[1]="format";return (_463||_464[_465.join("-")]).concat();};dojo.date.getDayName=function(_466,_467){return dojo.date.getNames("days","wide","format",_467)[_466.getDay()];};dojo.date.getDayShortName=function(_468,_469){return dojo.date.getNames("days","abbr","format",_469)[_468.getDay()];};dojo.date.getMonthName=function(_46a,_46b){return dojo.date.getNames("months","wide","format",_46b)[_46a.getMonth()];};dojo.date.getMonthShortName=function(_46c,_46d){return dojo.date.getNames("months","abbr","format",_46d)[_46c.getMonth()];};dojo.date.toRelativeString=function(_46e){var now=new Date();var diff=(now-_46e)/1000;var end=" ago";var _472=false;if(diff<0){_472=true;end=" from now";diff=-diff;}
if(diff<60){diff=Math.round(diff);return diff+" second"+(diff==1?"":"s")+end;}
if(diff<60*60){diff=Math.round(diff/60);return diff+" minute"+(diff==1?"":"s")+end;}
if(diff<60*60*24){diff=Math.round(diff/3600);return diff+" hour"+(diff==1?"":"s")+end;}
if(diff<60*60*24*7){diff=Math.round(diff/(3600*24));if(diff==1){return _472?"Tomorrow":"Yesterday";}else{return diff+" days"+end;}}
return dojo.date.format(_46e);};dojo.date.toSql=function(_473,_474){return dojo.date.strftime(_473,"%F"+(_474?"":" %T"));};dojo.date.fromSql=function(_475){var _476=_475.split(/[\- :]/g);while(_476.length<6){_476.push(0);}
return new Date(_476[0],(parseInt(_476[1],10)-1),_476[2],_476[3],_476[4],_476[5]);};dojo.xml.Parse=function(){var isIE=((dojo.render.html.capable)&&(dojo.render.html.ie));function getTagName(node){try{return node.tagName.toLowerCase();}
catch(e){return "";}}
function getDojoTagName(node){var _47a=getTagName(node);if(!_47a){return "";}
if((dojo.widget)&&(dojo.widget.tags[_47a])){return _47a;}
var p=_47a.indexOf(":");if(p>=0){return _47a;}
if(_47a.substr(0,5)=="dojo:"){return _47a;}
if(dojo.render.html.capable&&dojo.render.html.ie&&node.scopeName&&node.scopeName!="HTML"){return node.scopeName.toLowerCase()+":"+_47a;}
if(_47a.substr(0,4)=="dojo"){return "dojo:"+_47a.substring(4);}
var djt=node.getAttribute("dojoType")||node.getAttribute("dojotype");if(djt){if(djt.indexOf(":")<0){djt="dojo:"+djt;}
return djt.toLowerCase();}
djt=node.getAttributeNS&&node.getAttributeNS(dojo.dom.dojoml,"type");if(djt){return "dojo:"+djt.toLowerCase();}
try{djt=node.getAttribute("dojo:type");}
catch(e){}
if(djt){return "dojo:"+djt.toLowerCase();}
if((dj_global["djConfig"])&&(!djConfig["ignoreClassNames"])){var _47d=node.className||node.getAttribute("class");if((_47d)&&(_47d.indexOf)&&(_47d.indexOf("dojo-")!=-1)){var _47e=_47d.split(" ");for(var x=0,c=_47e.length;x<c;x++){if(_47e[x].slice(0,5)=="dojo-"){return "dojo:"+_47e[x].substr(5).toLowerCase();}}
}}
return "";}
this.parseElement=function(node,_482,_483,_484){var _485=getTagName(node);if(isIE&&_485.indexOf("/")==0){return null;}
try{var attr=node.getAttribute("parseWidgets");if(attr&&attr.toLowerCase()=="false"){return {};}}
catch(e){}
var _487=true;if(_483){var _488=getDojoTagName(node);_485=_488||_485;_487=Boolean(_488);}
var _489={};_489[_485]=[];var pos=_485.indexOf(":");if(pos>0){var ns=_485.substring(0,pos);_489["ns"]=ns;if((dojo.ns)&&(!dojo.ns.allow(ns))){_487=false;}}
if(_487){var _48c=this.parseAttributes(node);for(var attr in _48c){if((!_489[_485][attr])||(typeof _489[_485][attr]!="array")){_489[_485][attr]=[];}
_489[_485][attr].push(_48c[attr]);}
_489[_485].nodeRef=node;_489.tagName=_485;_489.index=_484||0;}
var _48d=0;for(var i=0;i<node.childNodes.length;i++){var tcn=node.childNodes.item(i);switch(tcn.nodeType){case dojo.dom.ELEMENT_NODE:
var ctn=getDojoTagName(tcn)||getTagName(tcn);if(!_489[ctn]){_489[ctn]=[];}
_489[ctn].push(this.parseElement(tcn,true,_483,_48d));if((tcn.childNodes.length==1)&&(tcn.childNodes.item(0).nodeType==dojo.dom.TEXT_NODE)){_489[ctn][_489[ctn].length-1].value=tcn.childNodes.item(0).nodeValue;}
_48d++;break;case dojo.dom.TEXT_NODE:
if(node.childNodes.length==1){_489[_485].push({value:node.childNodes.item(0).nodeValue});}
break;default:
break;}}
return _489;};this.parseAttributes=function(node){var _492={};var atts=node.attributes;var _494,i=0;while((_494=atts[i++])){if(isIE){if(!_494){continue;}
if((typeof _494=="object")&&(typeof _494.nodeValue=="undefined")||(_494.nodeValue==null)||(_494.nodeValue=="")){continue;}}
var nn=_494.nodeName.split(":");nn=(nn.length==2)?nn[1]:_494.nodeName;_492[nn]={value:_494.nodeValue};}
return _492;};};dojo.lang.declare=function(_497,_498,init,_49a){if((dojo.lang.isFunction(_49a))||((!_49a)&&(!dojo.lang.isFunction(init)))){if(dojo.lang.isFunction(_49a)){dojo.deprecated("dojo.lang.declare("+_497+"...):","use class, superclass, initializer, properties argument order","0.6");}
var temp=_49a;_49a=init;init=temp;}
if(_49a&&_49a.initializer){dojo.deprecated("dojo.lang.declare("+_497+"...):","specify initializer as third argument, not as an element in properties","0.6");}
var _49c=[];if(dojo.lang.isArray(_498)){_49c=_498;_498=_49c.shift();}
if(!init){init=dojo.getObject(_497,false);if((init)&&(!dojo.lang.isFunction(init))){init=null;}}
var ctor=dojo.lang.declare._makeConstructor();var scp=(_498?_498.prototype:null);if(scp){scp.prototyping=true;ctor.prototype=new _498();scp.prototyping=false;}
ctor.superclass=scp;ctor.mixins=_49c;for(var i=0,l=_49c.length;i<l;i++){dojo.lang.extend(ctor,_49c[i].prototype);}
ctor.prototype.initializer=null;ctor.prototype.declaredClass=_497;if(dojo.lang.isArray(_49a)){dojo.lang.extend.apply(dojo.lang,[ctor].concat(_49a));}else{dojo.lang.extend(ctor,(_49a)||{});}
dojo.lang.extend(ctor,dojo.lang.declare._common);ctor.prototype.constructor=ctor;ctor.prototype.initializer=(ctor.prototype.initializer)||(init)||(function(){});var _4a1=dojo.getObject(_497,true,null,true);_4a1.obj[_4a1.prop]=ctor;return ctor;};dojo.lang.declare._makeConstructor=function(){return function(){var self=this._getPropContext();var s=self.constructor.superclass;if((s)&&(s.constructor)){if(s.constructor==arguments.callee){this._inherited("constructor",arguments);}else{this._contextMethod(s,"constructor",arguments);}}
var ms=(self.constructor.mixins)||([]);for(var i=0,m;(m=ms[i]);i++){(((m.prototype)&&(m.prototype.initializer))||(m)).apply(this,arguments);}
if((!this.prototyping)&&(self.initializer)){self.initializer.apply(this,arguments);}};};dojo.lang.declare._common={_getPropContext:function(){return (this.___proto||this);},_contextMethod:function(_4a7,_4a8,args){var _4aa,_4ab=this.___proto;this.___proto=_4a7;try{_4aa=_4a7[_4a8].apply(this,(args||[]));}
catch(e){throw e;}
finally{this.___proto=_4ab;}
return _4aa;},_inherited:function(prop,args){var p=this._getPropContext();do{if((!p.constructor)||(!p.constructor.superclass)){return;}
p=p.constructor.superclass;}while(!(prop in p));return (dojo.lang.isFunction(p[prop])?this._contextMethod(p,prop,args):p[prop]);}};dojo.declare=dojo.lang.declare;dojo.ns={namespaces:{},failed:{},loading:{},loaded:{},register:function(name,_4b0,_4b1,_4b2){if(!_4b2||!this.namespaces[name]){this.namespaces[name]=new dojo.ns.Ns(name,_4b0,_4b1);}},allow:function(name){if(this.failed[name]){return false;}
if((djConfig.excludeNamespace)&&(dojo.lang.inArray(djConfig.excludeNamespace,name))){return false;}
return ((name==this.dojo)||(!djConfig.includeNamespace)||(dojo.lang.inArray(djConfig.includeNamespace,name)));},get:function(name){return this.namespaces[name];},require:function(name){var ns=this.namespaces[name];if((ns)&&(this.loaded[name])){return ns;}
if(!this.allow(name)){return false;}
if(this.loading[name]){dojo.debug("dojo.namespace.require: re-entrant request to load namespace \""+name+"\" must fail.");return false;}
var req=dojo.require;this.loading[name]=true;try{if(name=="dojo"){req("dojo.namespaces.dojo");}else{if(!dojo.hostenv.moduleHasPrefix(name)){dojo.registerModulePath(name,"../"+name);}
req([name,"manifest"].join("."),false,true);}
if(!this.namespaces[name]){this.failed[name]=true;}}
finally{this.loading[name]=false;}
return this.namespaces[name];}};dojo.ns.Ns=function(name,_4b9,_4ba){this.name=name;this.module=_4b9;this.resolver=_4ba;this._loaded=[];this._failed=[];};dojo.ns.Ns.prototype.resolve=function(name,_4bc,_4bd){if(!this.resolver||djConfig["skipAutoRequire"]){return false;}
var _4be=this.resolver(name,_4bc);if((_4be)&&(!this._loaded[_4be])&&(!this._failed[_4be])){var req=dojo.require;req(_4be,false,true);if(dojo.hostenv.findModule(_4be,false)){this._loaded[_4be]=true;}else{if(!_4bd){dojo.raise("dojo.ns.Ns.resolve: module '"+_4be+"' not found after loading via namespace '"+this.name+"'");}
this._failed[_4be]=true;}}
return Boolean(this._loaded[_4be]);};dojo.registerNamespace=function(name,_4c1,_4c2){dojo.ns.register.apply(dojo.ns,arguments);};dojo.registerNamespaceResolver=function(name,_4c4){var n=dojo.ns.namespaces[name];if(n){n.resolver=_4c4;}};dojo.registerNamespaceManifest=function(_4c6,path,name,_4c9,_4ca){dojo.registerModulePath(name,path);dojo.registerNamespace(name,_4c9,_4ca);};dojo.registerNamespace("dojo","dojo.widget");dojo.event.topic=new function(){this.topics={};this.getTopic=function(_4cb){if(!this.topics[_4cb]){this.topics[_4cb]=new this.TopicImpl(_4cb);}
return this.topics[_4cb];};this.registerPublisher=function(_4cc,obj,_4ce){var _4cc=this.getTopic(_4cc);_4cc.registerPublisher(obj,_4ce);};this.subscribe=function(_4cf,obj,_4d1){var _4cf=this.getTopic(_4cf);_4cf.subscribe(obj,_4d1);};this.unsubscribe=function(_4d2,obj,_4d4){var _4d2=this.getTopic(_4d2);_4d2.unsubscribe(obj,_4d4);};this.destroy=function(_4d5){this.getTopic(_4d5).destroy();delete this.topics[_4d5];};this.publishApply=function(_4d6,args){var _4d6=this.getTopic(_4d6);_4d6.sendMessage.apply(_4d6,args);};this.publish=function(_4d8,_4d9){var _4d8=this.getTopic(_4d8);var args=[];for(var x=1;x<arguments.length;x++){args.push(arguments[x]);}
_4d8.sendMessage.apply(_4d8,args);};};dojo.event.topic.TopicImpl=function(_4dc){this.topicName=_4dc;this.subscribe=function(_4dd,_4de){var tf=_4de||_4dd;var to=(!_4de)?dj_global:_4dd;return dojo.event.kwConnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});};this.unsubscribe=function(_4e1,_4e2){var tf=(!_4e2)?_4e1:_4e2;var to=(!_4e2)?null:_4e1;return dojo.event.kwDisconnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});};this._getJoinPoint=function(){return dojo.event.MethodJoinPoint.getForMethod(this,"sendMessage");};this.setSquelch=function(_4e5){this._getJoinPoint().squelch=_4e5;};this.destroy=function(){this._getJoinPoint().disconnect();};this.registerPublisher=function(_4e6,_4e7){dojo.event.connect(_4e6,_4e7,this,"sendMessage");};this.sendMessage=function(_4e8){};};dojo.kwCompoundRequire({common:["dojo.event.common","dojo.event.topic"],browser:["dojo.event.browser"],dashboard:["dojo.event.browser"]});dojo.widget.manager=new function(){this.widgets=[];this.widgetIds=[];this.topWidgets={};var _4e9={};var _4ea=[];this.getUniqueId=function(_4eb){var _4ec;do{_4ec=_4eb+"_"+(_4e9[_4eb]!=undefined?++_4e9[_4eb]:_4e9[_4eb]=0);}while(this.getWidgetById(_4ec));return _4ec;};this.add=function(_4ed){this.widgets.push(_4ed);if(!_4ed.extraArgs["id"]){_4ed.extraArgs["id"]=_4ed.extraArgs["ID"];}
if(_4ed.widgetId==""){if(_4ed["id"]){_4ed.widgetId=_4ed["id"];}else{if(_4ed.extraArgs["id"]){_4ed.widgetId=_4ed.extraArgs["id"];}else{_4ed.widgetId=this.getUniqueId(_4ed.ns+"_"+_4ed.widgetType);}}
}
if(this.widgetIds[_4ed.widgetId]){dojo.debug("widget ID collision on ID: "+_4ed.widgetId);}
this.widgetIds[_4ed.widgetId]=_4ed;};this.destroyAll=function(){for(var x=this.widgets.length-1;x>=0;x--){try{this.widgets[x].destroy(true);delete this.widgets[x];}
catch(e){}}
};this.remove=function(_4ef){if(dojo.lang.isNumber(_4ef)){var tw=this.widgets[_4ef].widgetId;delete this.widgetIds[tw];this.widgets.splice(_4ef,1);}else{this.removeById(_4ef);}};this.removeById=function(id){if(!dojo.lang.isString(id)){id=id["widgetId"];if(!id){dojo.debug("invalid widget or id passed to removeById");return;}}
for(var i=0;i<this.widgets.length;i++){if(this.widgets[i].widgetId==id){this.remove(i);break;}}
};this.getWidgetById=function(id){if(dojo.lang.isString(id)){return this.widgetIds[id];}
return id;};this.getWidgetsByType=function(type){var lt=type.toLowerCase();var _4f6=(type.indexOf(":")<0?function(x){return x.widgetType.toLowerCase();}:function(x){return x.getNamespacedType();});var ret=[];dojo.lang.forEach(this.widgets,function(x){if(_4f6(x)==lt){ret.push(x);}});return ret;};this.getWidgetsByFilter=function(_4fb,_4fc){var ret=[];dojo.lang.every(this.widgets,function(x){if(_4fb(x)){ret.push(x);if(_4fc){return false;}}
return true;});return (_4fc?ret[0]:ret);};this.getAllWidgets=function(){return this.widgets.concat();};this.getWidgetByNode=function(node){var w=this.getAllWidgets();node=dojo.byId(node);for(var i=0;i<w.length;i++){if(w[i].domNode==node){return w[i];}}
return null;};this.byId=this.getWidgetById;this.byType=this.getWidgetsByType;this.byFilter=this.getWidgetsByFilter;this.byNode=this.getWidgetByNode;var _502={};var _503=["dojo.widget"];for(var i=0;i<_503.length;i++){_503[_503[i]]=true;}
this.registerWidgetPackage=function(_505){if(!_503[_505]){_503[_505]=true;_503.push(_505);}};this.getWidgetPackageList=function(){return dojo.lang.map(_503,function(elt){return (elt!==true?elt:undefined);});};this.getImplementation=function(_507,_508,_509,ns){var impl=this.getImplementationName(_507,ns);if(impl){var ret=_508?new impl(_508):new impl();return ret;}};function buildPrefixCache(){for(var _50d in dojo.render){if(dojo.render[_50d]["capable"]===true){var _50e=dojo.render[_50d].prefixes;for(var i=0;i<_50e.length;i++){_4ea.push(_50e[i].toLowerCase());}}
}}
var _510=function(_511,_512){if(!_512){return null;}
for(var i=0,l=_4ea.length,_515;i<=l;i++){_515=(i<l?_512[_4ea[i]]:_512);if(!_515){continue;}
for(var name in _515){if(name.toLowerCase()==_511){return _515[name];}}
}
return null;};var _517=function(_518,_519){var _51a=dojo.getObject(_519,false);return (_51a?_510(_518,_51a):null);};this.getImplementationName=function(_51b,ns){var _51d=_51b.toLowerCase();ns=ns||"dojo";var imps=_502[ns]||(_502[ns]={});var impl=imps[_51d];if(impl){return impl;}
if(!_4ea.length){buildPrefixCache();}
var _520=dojo.ns.get(ns);if(!_520){dojo.ns.register(ns,ns+".widget");_520=dojo.ns.get(ns);}
if(_520){_520.resolve(_51b);}
impl=_517(_51d,_520.module);if(impl){return (imps[_51d]=impl);}
_520=dojo.ns.require(ns);if((_520)&&(_520.resolver)){_520.resolve(_51b);impl=_517(_51d,_520.module);if(impl){return (imps[_51d]=impl);}}
throw new Error("Could not locate widget implementation for \""+_51b+"\" in \""+_520.module+"\" registered to namespace \""+_520.name+"\"");};this.resizing=false;this.onWindowResized=function(){if(this.resizing){return;}
try{this.resizing=true;for(var id in this.topWidgets){var _522=this.topWidgets[id];if(_522.checkSize){_522.checkSize();}}
}
catch(e){}
finally{this.resizing=false;}};if(typeof window!="undefined"){dojo.addOnLoad(this,"onWindowResized");dojo.event.connect(window,"onresize",this,"onWindowResized");}};(function(){var dw=dojo.widget;var dwm=dw.manager;var h=dojo.lang.curry(dojo.lang,"hitch",dwm);var g=function(_527,_528){dw[(_528||_527)]=h(_527);};g("add","addWidget");g("destroyAll","destroyAllWidgets");g("remove","removeWidget");g("removeById","removeWidgetById");g("getWidgetById");g("getWidgetById","byId");g("getWidgetsByType");g("getWidgetsByFilter");g("getWidgetsByType","byType");g("getWidgetsByFilter","byFilter");g("getWidgetByNode","byNode");dw.all=function(n){var _52a=dwm.getAllWidgets.apply(dwm,arguments);if(arguments.length>0){return _52a[n];}
return _52a;};g("registerWidgetPackage");g("getImplementation","getWidgetImplementation");g("getImplementationName","getWidgetImplementationName");dw.widgets=dwm.widgets;dw.widgetIds=dwm.widgetIds;dw.root=dwm.root;})();dojo.uri=new function(){var _52b=new RegExp("^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$");var _52c=new RegExp("(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$");var _52d=new RegExp("/(\\w+.css)");this.dojoUri=function(uri){return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri(),uri);};this.moduleUri=function(_52f,uri){var loc=dojo.hostenv.getModuleSymbols(_52f).join("/");if(!loc){return null;}
if(loc.lastIndexOf("/")!=loc.length-1){loc+="/";}
var _532=loc.indexOf(":");var _533=loc.indexOf("/");if(loc.charAt(0)!="/"&&(_532==-1||_532>_533)){loc=dojo.hostenv.getBaseScriptUri()+loc;}
return new dojo.uri.Uri(loc,uri);};this.Uri=function(){var uri=arguments[0];if(uri&&arguments.length>1){var _535=_52d.exec(uri);if(_535){uri=uri.toString().replace(_535[1],"");}}
for(var i=1;i<arguments.length;i++){if(!arguments[i]){continue;}
var _537=new dojo.uri.Uri(arguments[i].toString());var _538=new dojo.uri.Uri(uri.toString());if((_537.path=="")&&(_537.scheme==null)&&(_537.authority==null)&&(_537.query==null)){if(_537.fragment!=null){_538.fragment=_537.fragment;}
_537=_538;}
if(_537.scheme!=null&&_537.authority!=null){uri="";}
if(_537.scheme!=null){uri+=_537.scheme+":";}
if(_537.authority!=null){uri+="//"+_537.authority;}
uri+=_537.path;if(_537.query!=null){uri+="?"+_537.query;}
if(_537.fragment!=null){uri+="#"+_537.fragment;}}
this.uri=uri.toString();var r=this.uri.match(_52c);this.scheme=r[2]||(r[1]?"":null);this.authority=r[4]||(r[3]?"":null);this.path=r[5];this.query=r[7]||(r[6]?"":null);this.fragment=r[9]||(r[8]?"":null);if(this.authority!=null){r=this.authority.match(_52b);this.user=r[3]||null;this.password=r[4]||null;this.host=r[5];this.port=r[7]||null;}
this.toString=function(){return this.uri;};};};dojo.kwCompoundRequire({common:[["dojo.uri.Uri",false,false]]});dojo.lang.mixin(dojo.html,dojo.dom);dojo.html.getEventTarget=function(evt){if(!evt){evt=dojo.global().event||{};}
var t=(evt.srcElement?evt.srcElement:(evt.target?evt.target:null));while((t)&&(t.nodeType!=1)){t=t.parentNode;}
return t;};dojo.html.getViewport=function(){var _53c=dojo.global();var _53d=dojo.doc();var w=0;var h=0;if(dojo.render.html.mozilla){w=_53d.documentElement.clientWidth;h=_53c.innerHeight;}else{if(!dojo.render.html.opera&&_53c.innerWidth){w=_53c.innerWidth;h=_53c.innerHeight;}else{if(!dojo.render.html.opera&&dojo.exists("documentElement.clientWidth",_53d)){var w2=_53d.documentElement.clientWidth;if(!w||w2&&w2<w){w=w2;}
h=_53d.documentElement.clientHeight;}else{if(dojo.body().clientWidth){w=dojo.body().clientWidth;h=dojo.body().clientHeight;}}
}}
return {width:w,height:h};};dojo.html.getScroll=function(){var _541=dojo.global();var _542=dojo.doc();var top=_541.pageYOffset||_542.documentElement.scrollTop||dojo.body().scrollTop||0;var left=_541.pageXOffset||_542.documentElement.scrollLeft||dojo.body().scrollLeft||0;return {top:top,left:left,offset:{x:left,y:top}};};dojo.html.getParentByType=function(node,type){var _547=dojo.doc();var _548=dojo.byId(node);type=type.toLowerCase();while((_548)&&(_548.nodeName.toLowerCase()!=type)){if(_548==(_547["body"]||_547["documentElement"])){return null;}
_548=_548.parentNode;}
return _548;};dojo.html.getAttribute=function(node,attr){node=dojo.byId(node);if((!node)||(!node.getAttribute)){return null;}
var ta=typeof attr=="string"?attr:new String(attr);var v=node.getAttribute(ta.toUpperCase());if((v)&&(typeof v=="string")&&(v!="")){return v;}
if(v&&v.value){return v.value;}
if((node.getAttributeNode)&&(node.getAttributeNode(ta))){return (node.getAttributeNode(ta)).value;}else{if(node.getAttribute(ta)){return node.getAttribute(ta);}else{if(node.getAttribute(ta.toLowerCase())){return node.getAttribute(ta.toLowerCase());}}
}
return null;};dojo.html.hasAttribute=function(node,attr){return dojo.html.getAttribute(dojo.byId(node),attr)?true:false;};dojo.html.getCursorPosition=function(e){e=e||dojo.global().event;var _550={x:0,y:0};if(e.pageX||e.pageY){_550.x=e.pageX;_550.y=e.pageY;}else{var de=dojo.doc().documentElement;var db=dojo.body();_550.x=e.clientX+((de||db)["scrollLeft"])-((de||db)["clientLeft"]);_550.y=e.clientY+((de||db)["scrollTop"])-((de||db)["clientTop"]);}
return _550;};dojo.html.isTag=function(node){node=dojo.byId(node);if(node&&node.tagName){for(var i=1;i<arguments.length;i++){if(node.tagName.toLowerCase()==String(arguments[i]).toLowerCase()){return String(arguments[i]).toLowerCase();}}
}
return "";};if(dojo.render.html.ie&&!dojo.render.html.ie70){if(window.location.href.substr(0,6).toLowerCase()!="https:"){(function(){var _555=dojo.doc().createElement("script");_555.src="javascript:'dojo.html.createExternalElement=function(doc, tag){ return doc.createElement(tag); }'";dojo.doc().getElementsByTagName("head")[0].appendChild(_555);})();}}else{dojo.html.createExternalElement=function(doc,tag){return doc.createElement(tag);};}
dojo.a11y={imgPath:dojo.uri.moduleUri("dojo","widget/templates/images"),doAccessibleCheck:true,accessible:null,checkAccessible:function(){if(this.accessible===null){this.accessible=false;if(this.doAccessibleCheck==true){this.accessible=this.testAccessible();}}
return this.accessible;},testAccessible:function(){this.accessible=false;if(dojo.render.html.ie||dojo.render.html.mozilla){var div=document.createElement("div");div.style.backgroundImage="url(\""+this.imgPath+"/tab_close.gif\")";dojo.body().appendChild(div);var _559=null;if(window.getComputedStyle){var _55a=getComputedStyle(div,"");_559=_55a.getPropertyValue("background-image");}else{_559=div.currentStyle.backgroundImage;}
var _55b=false;if(_559!=null&&(_559=="none"||_559=="url(invalid-url:)")){this.accessible=true;}
dojo.body().removeChild(div);}
return this.accessible;},setAccessible:function(_55c){this.accessible=_55c;},setCheckAccessible:function(_55d){this.doAccessibleCheck=_55d;},setAccessibleMode:function(){if(this.accessible===null){if(this.checkAccessible()){dojo.render.html.prefixes.unshift("a11y");}}
return this.accessible;}};dojo.declare("dojo.widget.Widget",null,function(){this.children=[];this.extraArgs={};},{parent:null,isTopLevel:false,disabled:false,isContainer:false,widgetId:"",widgetType:"Widget",ns:"dojo",getNamespacedType:function(){return (this.ns?this.ns+":"+this.widgetType:this.widgetType).toLowerCase();},toString:function(){return "[Widget "+this.getNamespacedType()+", "+(this.widgetId||"NO ID")+"]";},repr:function(){return this.toString();},enable:function(){this.disabled=false;},disable:function(){this.disabled=true;},onResized:function(){this.notifyChildrenOfResize();},notifyChildrenOfResize:function(){for(var i=0;i<this.children.length;i++){var _55f=this.children[i];if(_55f.onResized){_55f.onResized();}}
},create:function(args,_561,_562,ns){if(ns){this.ns=ns;}
this.satisfyPropertySets(args,_561,_562);this.mixInProperties(args,_561,_562);this.postMixInProperties(args,_561,_562);dojo.widget.manager.add(this);this.buildRendering(args,_561,_562);this.initialize(args,_561,_562);this.postInitialize(args,_561,_562);this.postCreate(args,_561,_562);return this;},destroy:function(_564){if(this.parent){this.parent.removeChild(this);}
this.destroyChildren();this.uninitialize();this.destroyRendering(_564);dojo.widget.manager.removeById(this.widgetId);},destroyChildren:function(){var _565;var i=0;while(this.children.length>i){_565=this.children[i];if(_565 instanceof dojo.widget.Widget){this.removeChild(_565);_565.destroy();continue;}
i++;}},getChildrenOfType:function(type,_568){var ret=[];var _56a=dojo.lang.isFunction(type);if(!_56a){type=type.toLowerCase();}
for(var x=0;x<this.children.length;x++){if(_56a){if(this.children[x] instanceof type){ret.push(this.children[x]);}}else{if(this.children[x].widgetType.toLowerCase()==type){ret.push(this.children[x]);}}
if(_568){ret=ret.concat(this.children[x].getChildrenOfType(type,_568));}}
return ret;},getDescendants:function(){var _56c=[];var _56d=[this];var elem;while((elem=_56d.pop())){_56c.push(elem);if(elem.children){dojo.lang.forEach(elem.children,function(elem){_56d.push(elem);});}}
return _56c;},isFirstChild:function(){return this===this.parent.children[0];},isLastChild:function(){return this===this.parent.children[this.parent.children.length-1];},satisfyPropertySets:function(args){return args;},mixInProperties:function(args,frag){if((args["fastMixIn"])||(frag["fastMixIn"])){for(var x in args){this[x]=args[x];}
return;}
var _574;var _575=dojo.widget.lcArgsCache[this.widgetType];if(_575==null){_575={};for(var y in this){_575[((new String(y)).toLowerCase())]=y;}
dojo.widget.lcArgsCache[this.widgetType]=_575;}
var _577={};for(var x in args){if(!this[x]){var y=_575[(new String(x)).toLowerCase()];if(y){args[y]=args[x];x=y;}}
if(_577[x]){continue;}
_577[x]=true;if((typeof this[x])!=(typeof _574)){if(typeof args[x]!="string"){this[x]=args[x];}else{if(dojo.lang.isString(this[x])){this[x]=args[x];}else{if(dojo.lang.isNumber(this[x])){this[x]=new Number(args[x]);}else{if(dojo.lang.isBoolean(this[x])){this[x]=(args[x].toLowerCase()=="false")?false:true;}else{if(dojo.lang.isFunction(this[x])){if(args[x].search(/[^\w\.]+/i)==-1){this[x]=dojo.getObject(args[x],false);}else{var tn=dojo.lang.nameAnonFunc(new Function(args[x]),this);dojo.event.kwConnect({srcObj:this,srcFunc:x,adviceObj:this,adviceFunc:tn});}}else{if(dojo.lang.isArray(this[x])){this[x]=args[x].split(";");}else{if(this[x] instanceof Date){this[x]=new Date(Number(args[x]));}else{if(typeof this[x]=="object"){if(this[x] instanceof dojo.uri.Uri){this[x]=dojo.uri.dojoUri(args[x]);}else{var _579=args[x].split(";");for(var y=0;y<_579.length;y++){var si=_579[y].indexOf(":");if((si!=-1)&&(_579[y].length>si)){this[x][_579[y].substr(0,si).replace(/^\s+|\s+$/g,"")]=_579[y].substr(si+1);}}
}}else{this[x]=args[x];}}
}}
}}
}}
}else{this.extraArgs[x.toLowerCase()]=args[x];}}
},postMixInProperties:function(args,frag,_57d){},initialize:function(args,frag,_580){return false;},postInitialize:function(args,frag,_583){return false;},postCreate:function(args,frag,_586){return false;},uninitialize:function(){return false;},buildRendering:function(args,frag,_589){dojo.unimplemented("dojo.widget.Widget.buildRendering, on "+this.toString()+", ");return false;},destroyRendering:function(){dojo.unimplemented("dojo.widget.Widget.destroyRendering");return false;},addedTo:function(_58a){},addChild:function(_58b){dojo.unimplemented("dojo.widget.Widget.addChild");return false;},removeChild:function(_58c){for(var x=0;x<this.children.length;x++){if(this.children[x]===_58c){this.children.splice(x,1);_58c.parent=null;break;}}
return _58c;},getPreviousSibling:function(){var idx=this.getParentIndex();if(idx<=0){return null;}
return this.parent.children[idx-1];},getSiblings:function(){return this.parent.children;},getParentIndex:function(){return dojo.lang.indexOf(this.parent.children,this,true);},getNextSibling:function(){var idx=this.getParentIndex();if(idx==this.parent.children.length-1){return null;}
if(idx<0){return null;}
return this.parent.children[idx+1];}});dojo.widget.lcArgsCache={};dojo.widget.tags={};dojo.widget.tags["dojo:propertyset"]=function(_590,_591,_592){var _593=_591.parseProperties(_590["dojo:propertyset"]);};dojo.widget.tags["dojo:connect"]=function(_594,_595,_596){var _597=_595.parseProperties(_594["dojo:connect"]);};dojo.widget.buildWidgetFromParseTree=function(type,frag,_59a,_59b,_59c,_59d){dojo.a11y.setAccessibleMode();var _59e=type.split(":");_59e=(_59e.length==2)?_59e[1]:type;var _59f=_59d||_59a.parseProperties(frag[frag["ns"]+":"+_59e]);var _5a0=dojo.widget.manager.getImplementation(_59e,null,null,frag["ns"]);if(!_5a0){throw new Error("cannot find \""+type+"\" widget");}else{if(!_5a0.create){throw new Error("\""+type+"\" widget object has no \"create\" method and does not appear to implement *Widget");}}
_59f["dojoinsertionindex"]=_59c;var ret=_5a0.create(_59f,frag,_59b,frag["ns"]);return ret;};dojo.widget.defineWidget=function(_5a2,_5a3,_5a4,init,_5a6){if(dojo.lang.isString(arguments[3])){dojo.widget._defineWidget(arguments[0],arguments[3],arguments[1],arguments[4],arguments[2]);}else{var args=[arguments[0]],p=3;if(dojo.lang.isString(arguments[1])){args.push(arguments[1],arguments[2]);}else{args.push("",arguments[1]);p=2;}
if(dojo.lang.isFunction(arguments[p])){args.push(arguments[p],arguments[p+1]);}else{args.push(null,arguments[p]);}
dojo.widget._defineWidget.apply(this,args);}};dojo.widget.defineWidget.renderers="html|svg|vml";dojo.widget._defineWidget=function(_5a9,_5aa,_5ab,init,_5ad){var _5ae=_5a9.split(".");var type=_5ae.pop();var regx="\\.("+(_5aa?_5aa+"|":"")+dojo.widget.defineWidget.renderers+")\\.";var r=_5a9.search(new RegExp(regx));_5ae=(r<0?_5ae.join("."):_5a9.substr(0,r));dojo.widget.manager.registerWidgetPackage(_5ae);var pos=_5ae.indexOf(".");var _5b3=(pos>-1)?_5ae.substring(0,pos):_5ae;_5ad=(_5ad)||{};_5ad.widgetType=type;if((!init)&&(_5ad["classConstructor"])){init=_5ad.classConstructor;delete _5ad.classConstructor;}
dojo.declare(_5a9,_5ab,init,_5ad);};dojo.widget.Parse=function(_5b4){this.propertySetsList=[];this.fragment=_5b4;this.createComponents=function(frag,_5b6){var _5b7=[];var _5b8=false;try{if(frag&&frag.tagName&&(frag!=frag.nodeRef)){var _5b9=dojo.widget.tags;var tna=String(frag.tagName).split(";");for(var x=0;x<tna.length;x++){var ltn=tna[x].replace(/^\s+|\s+$/g,"").toLowerCase();frag.tagName=ltn;var ret;if(_5b9[ltn]){_5b8=true;ret=_5b9[ltn](frag,this,_5b6,frag.index);_5b7.push(ret);}else{if(ltn.indexOf(":")==-1){ltn="dojo:"+ltn;}
ret=dojo.widget.buildWidgetFromParseTree(ltn,frag,this,_5b6,frag.index);if(ret){_5b8=true;_5b7.push(ret);}}
}}
}
catch(e){dojo.debug("dojo.widget.Parse: error:",e);}
if(!_5b8){_5b7=_5b7.concat(this.createSubComponents(frag,_5b6));}
return _5b7;};this.createSubComponents=function(_5be,_5bf){var frag,_5c1=[];for(var item in _5be){frag=_5be[item];if(frag&&typeof frag=="object"&&(frag!=_5be.nodeRef)&&(frag!=_5be.tagName)&&(item.indexOf("$")==-1)){_5c1=_5c1.concat(this.createComponents(frag,_5bf));}}
return _5c1;};this.parsePropertySets=function(_5c3){return [];};this.parseProperties=function(_5c4){var _5c5={};for(var item in _5c4){if((_5c4[item]==_5c4.tagName)||(_5c4[item]==_5c4.nodeRef)){}else{var frag=_5c4[item];if(frag.tagName&&dojo.widget.tags[frag.tagName.toLowerCase()]){}else{if(frag[0]&&frag[0].value!=""&&frag[0].value!=null){try{if(item.toLowerCase()=="dataprovider"){var _5c8=this;this.getDataProvider(_5c8,frag[0].value);_5c5.dataProvider=this.dataProvider;}
_5c5[item]=frag[0].value;var _5c9=this.parseProperties(frag);for(var _5ca in _5c9){_5c5[_5ca]=_5c9[_5ca];}}
catch(e){dojo.debug(e);}}
}
switch(item.toLowerCase()){case "checked":
case "disabled":
if(typeof _5c5[item]!="boolean"){_5c5[item]=true;}
break;}}
}
return _5c5;};this.getDataProvider=function(_5cb,_5cc){dojo.io.bind({url:_5cc,load:function(type,_5ce){if(type=="load"){_5cb.dataProvider=_5ce;}},mimetype:"text/javascript",sync:true});};this.getPropertySetById=function(_5cf){for(var x=0;x<this.propertySetsList.length;x++){if(_5cf==this.propertySetsList[x]["id"][0].value){return this.propertySetsList[x];}}
return "";};this.getPropertySetsByType=function(_5d1){var _5d2=[];for(var x=0;x<this.propertySetsList.length;x++){var cpl=this.propertySetsList[x];var cpcc=cpl.componentClass||cpl.componentType||null;var _5d6=this.propertySetsList[x]["id"][0].value;if(cpcc&&(_5d6==cpcc[0].value)){_5d2.push(cpl);}}
return _5d2;};this.getPropertySets=function(_5d7){var ppl="dojo:propertyproviderlist";var _5d9=[];var _5da=_5d7.tagName;if(_5d7[ppl]){var _5db=_5d7[ppl].value.split(" ");for(var _5dc in _5db){if((_5dc.indexOf("..")==-1)&&(_5dc.indexOf("://")==-1)){var _5dd=this.getPropertySetById(_5dc);if(_5dd!=""){_5d9.push(_5dd);}}else{}}
}
return this.getPropertySetsByType(_5da).concat(_5d9);};this.createComponentFromScript=function(_5de,_5df,_5e0,ns){_5e0.fastMixIn=true;var ltn=(ns||"dojo")+":"+_5df.toLowerCase();if(dojo.widget.tags[ltn]){return [dojo.widget.tags[ltn](_5e0,this,null,null,_5e0)];}
return [dojo.widget.buildWidgetFromParseTree(ltn,_5e0,this,null,null,_5e0)];};};dojo.widget._parser_collection={"dojo":new dojo.widget.Parse()};dojo.widget.getParser=function(name){if(!name){name="dojo";}
if(!this._parser_collection[name]){this._parser_collection[name]=new dojo.widget.Parse();}
return this._parser_collection[name];};dojo.widget.createWidget=function(name,_5e5,_5e6,_5e7){var _5e8=false;var _5e9=(typeof name=="string");if(_5e9){var pos=name.indexOf(":");var ns=(pos>-1)?name.substring(0,pos):"dojo";if(pos>-1){name=name.substring(pos+1);}
var _5ec=name.toLowerCase();var _5ed=ns+":"+_5ec;_5e8=(dojo.byId(name)&&!dojo.widget.tags[_5ed]);}
if((arguments.length==1)&&(_5e8||!_5e9)){var xp=new dojo.xml.Parse();var tn=_5e8?dojo.byId(name):name;return dojo.widget.getParser().createComponents(xp.parseElement(tn,null,true))[0];}
function fromScript(_5f0,name,_5f2,ns){_5f2[_5ed]={dojotype:[{value:_5ec}],nodeRef:_5f0,fastMixIn:true};_5f2.ns=ns;return dojo.widget.getParser().createComponentFromScript(_5f0,name,_5f2,ns);}
_5e5=_5e5||{};var _5f4=false;var tn=null;var h=dojo.render.html.capable;if(h){tn=document.createElement("span");}
if(!_5e6){_5f4=true;_5e6=tn;if(h){dojo.body().appendChild(_5e6);}}else{if(_5e7){dojo.dom.insertAtPosition(tn,_5e6,_5e7);}else{tn=_5e6;}}
var _5f6=fromScript(tn,name.toLowerCase(),_5e5,ns);if((!_5f6)||(!_5f6[0])||(typeof _5f6[0].widgetType=="undefined")){throw new Error("createWidget: Creation of \""+name+"\" widget failed.");}
try{if(_5f4&&_5f6[0].domNode.parentNode){_5f6[0].domNode.parentNode.removeChild(_5f6[0].domNode);}}
catch(e){dojo.debug(e);}
return _5f6[0];};dojo.html.getClass=function(node){node=dojo.byId(node);if(!node){return "";}
var cs="";if(node.className){cs=node.className;}else{if(dojo.html.hasAttribute(node,"class")){cs=dojo.html.getAttribute(node,"class");}}
return cs.replace(/^\s+|\s+$/g,"");};dojo.html.getClasses=function(node){var c=dojo.html.getClass(node);return (c=="")?[]:c.split(/\s+/g);};dojo.html.hasClass=function(node,_5fc){return (new RegExp("(^|\\s+)"+_5fc+"(\\s+|$)")).test(dojo.html.getClass(node));};dojo.html.prependClass=function(node,_5fe){_5fe+=" "+dojo.html.getClass(node);return dojo.html.setClass(node,_5fe);};dojo.html.addClass=function(node,_600){if(dojo.html.hasClass(node,_600)){return false;}
_600=(dojo.html.getClass(node)+" "+_600).replace(/^\s+|\s+$/g,"");return dojo.html.setClass(node,_600);};dojo.html.setClass=function(node,_602){node=dojo.byId(node);var cs=new String(_602);try{if(typeof node.className=="string"){node.className=cs;}else{if(node.setAttribute){node.setAttribute("class",_602);node.className=cs;}else{return false;}}
}
catch(e){dojo.debug("dojo.html.setClass() failed",e);}
return true;};dojo.html.removeClass=function(node,_605,_606){try{if(!_606){var _607=dojo.html.getClass(node).replace(new RegExp("(^|\\s+)"+_605+"(\\s+|$)"),"$1$2");}else{var _607=dojo.html.getClass(node).replace(_605,"");}
dojo.html.setClass(node,_607);}
catch(e){dojo.debug("dojo.html.removeClass() failed",e);}
return true;};dojo.html.replaceClass=function(node,_609,_60a){dojo.html.removeClass(node,_60a);dojo.html.addClass(node,_609);};dojo.html.classMatchType={ContainsAll:0,ContainsAny:1,IsOnly:2};dojo.html.getElementsByClass=function(_60b,_60c,_60d,_60e,_60f){_60f=false;var _610=dojo.doc();_60c=dojo.byId(_60c)||_610;var _611=_60b.split(/\s+/g);var _612=[];if(_60e!=1&&_60e!=2){_60e=0;}
var _613=new RegExp("(\\s|^)(("+_611.join(")|(")+"))(\\s|$)");var _614=_611.join(" ").length;var _615=[];if(!_60f&&_610.evaluate){var _616=".//"+(_60d||"*")+"[contains(";if(_60e!=dojo.html.classMatchType.ContainsAny){_616+="concat(' ',@class,' '), ' "+_611.join(" ') and contains(concat(' ',@class,' '), ' ")+" ')";if(_60e==2){_616+=" and string-length(@class)="+_614+"]";}else{_616+="]";}}else{_616+="concat(' ',@class,' '), ' "+_611.join(" ') or contains(concat(' ',@class,' '), ' ")+" ')]";}
var _617=_610.evaluate(_616,_60c,null,XPathResult.ANY_TYPE,null);var _618=_617.iterateNext();while(_618){try{_615.push(_618);_618=_617.iterateNext();}
catch(e){break;}}
return _615;}else{if(!_60d){_60d="*";}
_615=_60c.getElementsByTagName(_60d);var node,i=0;outer:
while(node=_615[i++]){var _61b=dojo.html.getClasses(node);if(_61b.length==0){continue outer;}
var _61c=0;for(var j=0;j<_61b.length;j++){if(_613.test(_61b[j])){if(_60e==dojo.html.classMatchType.ContainsAny){_612.push(node);continue outer;}else{_61c++;}}else{if(_60e==dojo.html.classMatchType.IsOnly){continue outer;}}
}
if(_61c==_611.length){if((_60e==dojo.html.classMatchType.IsOnly)&&(_61c==_61b.length)){_612.push(node);}else{if(_60e==dojo.html.classMatchType.ContainsAll){_612.push(node);}}
}}
return _612;}};dojo.html.getElementsByClassName=dojo.html.getElementsByClass;dojo.html.toCamelCase=function(_61e){var arr=_61e.split("-"),cc=arr[0];for(var i=1;i<arr.length;i++){cc+=arr[i].charAt(0).toUpperCase()+arr[i].substring(1);}
return cc;};dojo.html.toSelectorCase=function(_622){return _622.replace(/([A-Z])/g,"-$1").toLowerCase();};dojo.html.getComputedStyle=function(node,_624,_625){node=dojo.byId(node);var _624=dojo.html.toSelectorCase(_624);var _626=dojo.html.toCamelCase(_624);if(!node||!node.style){return _625;}else{if(document.defaultView&&dojo.html.isDescendantOf(node,node.ownerDocument)){try{var cs=document.defaultView.getComputedStyle(node,"");if(cs){return cs.getPropertyValue(_624);}}
catch(e){if(node.style.getPropertyValue){return node.style.getPropertyValue(_624);}else{return _625;}}
}else{if(node.currentStyle){return node.currentStyle[_626];}}
}
if(node.style.getPropertyValue){return node.style.getPropertyValue(_624);}else{return _625;}};dojo.html.getStyleProperty=function(node,_629){node=dojo.byId(node);return (node&&node.style?node.style[dojo.html.toCamelCase(_629)]:undefined);};dojo.html.getStyle=function(node,_62b){var _62c=dojo.html.getStyleProperty(node,_62b);return (_62c?_62c:dojo.html.getComputedStyle(node,_62b));};dojo.html.setStyle=function(node,_62e,_62f){node=dojo.byId(node);if(node&&node.style){var _630=dojo.html.toCamelCase(_62e);node.style[_630]=_62f;}};dojo.html.setStyleText=function(_631,text){try{_631.style.cssText=text;}
catch(e){_631.setAttribute("style",text);}};dojo.html.copyStyle=function(_633,_634){if(!_634.style.cssText){_633.setAttribute("style",_634.getAttribute("style"));}else{_633.style.cssText=_634.style.cssText;}
dojo.html.addClass(_633,dojo.html.getClass(_634));};dojo.html.getUnitValue=function(node,_636,_637){var s=dojo.html.getComputedStyle(node,_636);if((!s)||((s=="auto")&&(_637))){return {value:0,units:"px"};}
var _639=s.match(/(\-?[\d.]+)([a-z%]*)/i);if(!_639){return dojo.html.getUnitValue.bad;}
return {value:Number(_639[1]),units:_639[2].toLowerCase()};};dojo.html.getUnitValue.bad={value:NaN,units:""};dojo.html.getPixelValue=function(node,_63b,_63c){var _63d=dojo.html.getUnitValue(node,_63b,_63c);if(isNaN(_63d.value)){return 0;}
if((_63d.value)&&(_63d.units!="px")){return NaN;}
return _63d.value;};dojo.html.setPositivePixelValue=function(node,_63f,_640){if(isNaN(_640)){return false;}
node.style[_63f]=Math.max(0,_640)+"px";return true;};dojo.html.styleSheet=null;dojo.html.insertCssRule=function(_641,_642,_643){if(!dojo.html.styleSheet){if(document.createStyleSheet){dojo.html.styleSheet=document.createStyleSheet();}else{if(document.styleSheets[0]){dojo.html.styleSheet=document.styleSheets[0];}else{return null;}}
}
if(arguments.length<3){if(dojo.html.styleSheet.cssRules){_643=dojo.html.styleSheet.cssRules.length;}else{if(dojo.html.styleSheet.rules){_643=dojo.html.styleSheet.rules.length;}else{return null;}}
}
if(dojo.html.styleSheet.insertRule){var rule=_641+" { "+_642+" }";return dojo.html.styleSheet.insertRule(rule,_643);}else{if(dojo.html.styleSheet.addRule){return dojo.html.styleSheet.addRule(_641,_642,_643);}else{return null;}}
};dojo.html.removeCssRule=function(_645){if(!dojo.html.styleSheet){dojo.debug("no stylesheet defined for removing rules");return false;}
if(dojo.render.html.ie){if(!_645){_645=dojo.html.styleSheet.rules.length;dojo.html.styleSheet.removeRule(_645);}}else{if(document.styleSheets[0]){if(!_645){_645=dojo.html.styleSheet.cssRules.length;}
dojo.html.styleSheet.deleteRule(_645);}}
return true;};dojo.html._insertedCssFiles=[];dojo.html.insertCssFile=function(URI,doc,_648,_649){if(!URI){return;}
if(!doc){doc=document;}
var _64a=dojo.hostenv.getText(URI,false,_649);if(_64a===null){return;}
_64a=dojo.html.fixPathsInCssText(_64a,URI);if(_648){var idx=-1,node,ent=dojo.html._insertedCssFiles;for(var i=0;i<ent.length;i++){if((ent[i].doc==doc)&&(ent[i].cssText==_64a)){idx=i;node=ent[i].nodeRef;break;}}
if(node){var _64f=doc.getElementsByTagName("style");for(var i=0;i<_64f.length;i++){if(_64f[i]==node){return;}}
dojo.html._insertedCssFiles.shift(idx,1);}}
var _650=dojo.html.insertCssText(_64a,doc);dojo.html._insertedCssFiles.push({"doc":doc,"cssText":_64a,"nodeRef":_650});if(_650&&djConfig.isDebug){_650.setAttribute("dbgHref",URI);}
return _650;};dojo.html.insertCssText=function(_651,doc,URI){if(!_651){return;}
if(!doc){doc=document;}
if(URI){_651=dojo.html.fixPathsInCssText(_651,URI);}
var _654=doc.createElement("style");_654.setAttribute("type","text/css");var head=doc.getElementsByTagName("head")[0];if(!head){dojo.debug("No head tag in document, aborting styles");return;}else{head.appendChild(_654);}
if(_654.styleSheet){var _656=function(){try{_654.styleSheet.cssText=_651;}
catch(e){dojo.debug(e);}};if(_654.styleSheet.disabled){setTimeout(_656,10);}else{_656();}}else{var _657=doc.createTextNode(_651);_654.appendChild(_657);}
return _654;};dojo.html.fixPathsInCssText=function(_658,URI){if(!_658||!URI){return;}
var _65a,str="",url="",_65d="[\\t\\s\\w\\(\\)\\/\\.\\\\'\"-:#=&?~]+";var _65e=new RegExp("url\\(\\s*("+_65d+")\\s*\\)");var _65f=/(file|https?|ftps?):\/\//;regexTrim=new RegExp("^[\\s]*(['\"]?)("+_65d+")\\1[\\s]*?$");if(dojo.render.html.ie55||dojo.render.html.ie60){var _660=new RegExp("AlphaImageLoader\\((.*)src=['\"]("+_65d+")['\"]");while(_65a=_660.exec(_658)){url=_65a[2].replace(regexTrim,"$2");if(!_65f.exec(url)){url=(new dojo.uri.Uri(URI,url).toString());}
str+=_658.substring(0,_65a.index)+"AlphaImageLoader("+_65a[1]+"src='"+url+"'";_658=_658.substr(_65a.index+_65a[0].length);}
_658=str+_658;str="";}
while(_65a=_65e.exec(_658)){url=_65a[1].replace(regexTrim,"$2");if(!_65f.exec(url)){url=(new dojo.uri.Uri(URI,url).toString());}
str+=_658.substring(0,_65a.index)+"url("+url+")";_658=_658.substr(_65a.index+_65a[0].length);}
return str+_658;};dojo.html.setActiveStyleSheet=function(_661){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")){a.disabled=true;if(a.getAttribute("title")==_661){a.disabled=false;}}
}};dojo.html.getActiveStyleSheet=function(){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")&&!a.disabled){return a.getAttribute("title");}}
return null;};dojo.html.getPreferredStyleSheet=function(){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("rel").indexOf("alt")==-1&&a.getAttribute("title")){return a.getAttribute("title");}}
return null;};dojo.html.applyBrowserClass=function(node){var drh=dojo.render.html;var _66d={dj_ie:drh.ie,dj_ie55:drh.ie55,dj_ie6:drh.ie60,dj_ie7:drh.ie70,dj_iequirks:drh.ie&&drh.quirks,dj_opera:drh.opera,dj_opera8:drh.opera&&(Math.floor(dojo.render.version)==8),dj_opera9:drh.opera&&(Math.floor(dojo.render.version)==9),dj_khtml:drh.khtml,dj_safari:drh.safari,dj_gecko:drh.mozilla};for(var p in _66d){if(_66d[p]){dojo.html.addClass(node,p);}}
};dojo.widget._cssFiles={};dojo.widget._cssStrings={};dojo.widget._templateCache={};dojo.widget.defaultStrings={dojoRoot:dojo.hostenv.getBaseScriptUri(),dojoModuleUri:dojo.uri.moduleUri("dojo"),baseScriptUri:dojo.hostenv.getBaseScriptUri()};dojo.widget.fillFromTemplateCache=function(obj,_670,_671,_672){var _673=_670||obj.templatePath;var _674=dojo.widget._templateCache;if(!_673&&!obj["widgetType"]){do{var _675="__dummyTemplate__"+dojo.widget._templateCache.dummyCount++;}while(_674[_675]);obj.widgetType=_675;}
var wt=_673?_673.toString():obj.widgetType;var ts=_674[wt];if(!ts){_674[wt]={"string":null,"node":null};if(_672){ts={};}else{ts=_674[wt];}}
if((!obj.templateString)&&(!_672)){obj.templateString=_671||ts["string"];}
if(obj.templateString){obj.templateString=this._sanitizeTemplateString(obj.templateString);}
if((!obj.templateNode)&&(!_672)){obj.templateNode=ts["node"];}
if((!obj.templateNode)&&(!obj.templateString)&&(_673)){var _678=this._sanitizeTemplateString(dojo.hostenv.getText(_673));obj.templateString=_678;if(!_672){_674[wt]["string"]=_678;}}
if((!ts["string"])&&(!_672)){ts.string=obj.templateString;}};dojo.widget._sanitizeTemplateString=function(_679){if(_679){_679=_679.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im,"");var _67a=_679.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);if(_67a){_679=_67a[1];}}else{_679="";}
return _679;};dojo.widget._templateCache.dummyCount=0;dojo.widget.attachProperties=["dojoAttachPoint","id"];dojo.widget.eventAttachProperty="dojoAttachEvent";dojo.widget.onBuildProperty="dojoOnBuild";dojo.widget.waiNames=["waiRole","waiState"];dojo.widget.wai={waiRole:{name:"waiRole","namespace":"http://www.w3.org/TR/xhtml2",alias:"x2",prefix:"wairole:"},waiState:{name:"waiState","namespace":"http://www.w3.org/2005/07/aaa",alias:"aaa",prefix:""},setAttr:function(node,ns,attr,_67e){if(dojo.render.html.ie){node.setAttribute(this[ns].alias+":"+attr,this[ns].prefix+_67e);}else{node.setAttributeNS(this[ns]["namespace"],attr,this[ns].prefix+_67e);}},getAttr:function(node,ns,attr){if(dojo.render.html.ie){return node.getAttribute(this[ns].alias+":"+attr);}else{return node.getAttributeNS(this[ns]["namespace"],attr);}},removeAttr:function(node,ns,attr){var _685=true;if(dojo.render.html.ie){_685=node.removeAttribute(this[ns].alias+":"+attr);}else{node.removeAttributeNS(this[ns]["namespace"],attr);}
return _685;}};dojo.widget.attachTemplateNodes=function(_686,_687,_688){var _689=dojo.dom.ELEMENT_NODE;function trim(str){return str.replace(/^\s+|\s+$/g,"");}
if(!_686){_686=_687.domNode;}
if(_686.nodeType!=_689){return;}
var _68b=_686.all||_686.getElementsByTagName("*");var _68c=_687;for(var x=-1;x<_68b.length;x++){var _68e=(x==-1)?_686:_68b[x];var _68f=[];if(!_687.widgetsInTemplate||!_68e.getAttribute("dojoType")){for(var y=0;y<this.attachProperties.length;y++){var _691=_68e.getAttribute(this.attachProperties[y]);if(_691){_68f=_691.split(";");for(var z=0;z<_68f.length;z++){if(dojo.lang.isArray(_687[_68f[z]])){_687[_68f[z]].push(_68e);}else{_687[_68f[z]]=_68e;}}
break;}}
var _693=_68e.getAttribute(this.eventAttachProperty);if(_693){var evts=_693.split(";");for(var y=0;y<evts.length;y++){if((!evts[y])||(!evts[y].length)){continue;}
var _695=null;var tevt=trim(evts[y]);if(evts[y].indexOf(":")>=0){var _697=tevt.split(":");tevt=trim(_697[0]);_695=trim(_697[1]);}
if(!_695){_695=tevt;}
var tf=function(){var ntf=new String(_695);return function(evt){if(_68c[ntf]){_68c[ntf](dojo.event.browser.fixEvent(evt,this));}};}();dojo.event.browser.addListener(_68e,tevt,tf,false,true);}}
for(var y=0;y<_688.length;y++){var _69b=_68e.getAttribute(_688[y]);if((_69b)&&(_69b.length)){var _695=null;var _69c=_688[y].substr(4);_695=trim(_69b);var _69d=[_695];if(_695.indexOf(";")>=0){_69d=dojo.lang.map(_695.split(";"),trim);}
for(var z=0;z<_69d.length;z++){if(!_69d[z].length){continue;}
var tf=function(){var ntf=new String(_69d[z]);return function(evt){if(_68c[ntf]){_68c[ntf](dojo.event.browser.fixEvent(evt,this));}};}();dojo.event.browser.addListener(_68e,_69c,tf,false,true);}}
}}
var _6a0=_68e.getAttribute(this.templateProperty);if(_6a0){_687[_6a0]=_68e;}
dojo.lang.forEach(dojo.widget.waiNames,function(name){var wai=dojo.widget.wai[name];var val=_68e.getAttribute(wai.name);if(val){if(val.indexOf("-")==-1){dojo.widget.wai.setAttr(_68e,wai.name,"role",val);}else{var _6a4=val.split("-");dojo.widget.wai.setAttr(_68e,wai.name,_6a4[0],_6a4[1]);}}
},this);var _6a5=_68e.getAttribute(this.onBuildProperty);if(_6a5){eval("var node = baseNode; var widget = targetObj; "+_6a5);}}
};dojo.widget.getDojoEventsFromStr=function(str){var re=/(dojoOn([a-z]+)(\s?))=/gi;var evts=str?str.match(re)||[]:[];var ret=[];var lem={};for(var x=0;x<evts.length;x++){if(evts[x].length<1){continue;}
var cm=evts[x].replace(/\s/,"");cm=(cm.slice(0,cm.length-1));if(!lem[cm]){lem[cm]=true;ret.push(cm);}}
return ret;};dojo.declare("dojo.widget.DomWidget",dojo.widget.Widget,function(){if((arguments.length>0)&&(typeof arguments[0]=="object")){this.create(arguments[0]);}},{templateNode:null,templateString:null,templateCssString:null,preventClobber:false,domNode:null,containerNode:null,widgetsInTemplate:false,addChild:function(_6ad,_6ae,pos,ref,_6b1){if(typeof _6b1=="undefined"){_6b1=this.children.length;}
this.addWidgetAsDirectChild(_6ad,_6ae,pos,ref,_6b1);this.registerChild(_6ad,_6b1);return _6ad;},addWidgetAsDirectChild:function(_6b2,_6b3,pos,ref,_6b6){if((!this.containerNode)&&(!_6b3)){this.containerNode=this.domNode;}
var cn=(_6b3)?_6b3:this.containerNode;if(!pos){pos="after";}
if(!ref){if(!cn){cn=dojo.body();}
ref=cn.lastChild;}
if(!_6b6){_6b6=0;}
_6b2.domNode.setAttribute("dojoinsertionindex",_6b6);if(!ref){cn.appendChild(_6b2.domNode);}else{if(pos=="insertAtIndex"){dojo.dom.insertAtIndex(_6b2.domNode,ref.parentNode,_6b6);}else{if((pos=="after")&&(ref===cn.lastChild)){cn.appendChild(_6b2.domNode);}else{dojo.dom.insertAtPosition(_6b2.domNode,cn,pos);}}
}},registerChild:function(_6b8,_6b9){_6b8.dojoInsertionIndex=_6b9;var idx=-1;for(var i=0;i<this.children.length;i++){if(this.children[i].dojoInsertionIndex<=_6b9){idx=i;}}
this.children.splice(idx+1,0,_6b8);_6b8.parent=this;_6b8.addedTo(this,idx+1);delete dojo.widget.manager.topWidgets[_6b8.widgetId];},removeChild:function(_6bc){dojo.dom.removeNode(_6bc.domNode);return dojo.widget.DomWidget.superclass.removeChild.call(this,_6bc);},getFragNodeRef:function(frag){if(!frag){return null;}
if(!frag[this.getNamespacedType()]){dojo.raise("Error: no frag for widget type "+this.getNamespacedType()+", id "+this.widgetId+" (maybe a widget has set it's type incorrectly)");}
return frag[this.getNamespacedType()]["nodeRef"];},postInitialize:function(args,frag,_6c0){var _6c1=this.getFragNodeRef(frag);if(_6c0&&(_6c0.snarfChildDomOutput||!_6c1)){_6c0.addWidgetAsDirectChild(this,"","insertAtIndex","",args["dojoinsertionindex"],_6c1);}else{if(_6c1){if(this.domNode&&(this.domNode!==_6c1)){this._sourceNodeRef=dojo.dom.replaceNode(_6c1,this.domNode);}}
}
if(_6c0){_6c0.registerChild(this,args.dojoinsertionindex);}else{dojo.widget.manager.topWidgets[this.widgetId]=this;}
if(this.widgetsInTemplate){var _6c2=new dojo.xml.Parse();var _6c3;var _6c4=this.domNode.getElementsByTagName("*");for(var i=0;i<_6c4.length;i++){if(_6c4[i].getAttribute("dojoAttachPoint")=="subContainerWidget"){_6c3=_6c4[i];}
if(_6c4[i].getAttribute("dojoType")){_6c4[i].setAttribute("isSubWidget",true);}}
if(this.isContainer&&!this.containerNode){if(_6c3){var src=this.getFragNodeRef(frag);if(src){dojo.dom.moveChildren(src,_6c3);frag["dojoDontFollow"]=true;}}else{dojo.debug("No subContainerWidget node can be found in template file for widget "+this);}}
var _6c7=_6c2.parseElement(this.domNode,null,true);dojo.widget.getParser().createSubComponents(_6c7,this);var _6c8=[];var _6c9=[this];var w;while((w=_6c9.pop())){for(var i=0;i<w.children.length;i++){var _6cb=w.children[i];if(_6cb._processedSubWidgets||!_6cb.extraArgs["issubwidget"]){continue;}
_6c8.push(_6cb);if(_6cb.isContainer){_6c9.push(_6cb);}}
}
for(var i=0;i<_6c8.length;i++){var _6cc=_6c8[i];if(_6cc._processedSubWidgets){dojo.debug("This should not happen: widget._processedSubWidgets is already true!");return;}
_6cc._processedSubWidgets=true;if(_6cc.extraArgs["dojoattachevent"]){var evts=_6cc.extraArgs["dojoattachevent"].split(";");for(var j=0;j<evts.length;j++){var _6cf=null;var tevt=dojo.string.trim(evts[j]);if(tevt.indexOf(":")>=0){var _6d1=tevt.split(":");tevt=dojo.string.trim(_6d1[0]);_6cf=dojo.string.trim(_6d1[1]);}
if(!_6cf){_6cf=tevt;}
if(dojo.lang.isFunction(_6cc[tevt])){dojo.event.kwConnect({srcObj:_6cc,srcFunc:tevt,targetObj:this,targetFunc:_6cf});}else{alert(tevt+" is not a function in widget "+_6cc);}}
}
if(_6cc.extraArgs["dojoattachpoint"]){this[_6cc.extraArgs["dojoattachpoint"]]=_6cc;}}
}
if(this.isContainer&&!frag["dojoDontFollow"]){dojo.widget.getParser().createSubComponents(frag,this);}},buildRendering:function(args,frag){var ts=dojo.widget._templateCache[this.widgetType];if(args["templatecsspath"]){args["templateCssPath"]=args["templatecsspath"];}
var _6d5=args["templateCssPath"]||this.templateCssPath;if(_6d5&&!dojo.widget._cssFiles[_6d5.toString()]){if((!this.templateCssString)&&(_6d5)){this.templateCssString=dojo.hostenv.getText(_6d5);this.templateCssPath=null;}
dojo.widget._cssFiles[_6d5.toString()]=true;}
if((this["templateCssString"])&&(!dojo.widget._cssStrings[this.templateCssString])){dojo.html.insertCssText(this.templateCssString,null,_6d5);dojo.widget._cssStrings[this.templateCssString]=true;}
if((!this.preventClobber)&&((this.templatePath)||(this.templateNode)||((this["templateString"])&&(this.templateString.length))||((typeof ts!="undefined")&&((ts["string"])||(ts["node"]))))){this.buildFromTemplate(args,frag);}else{this.domNode=this.getFragNodeRef(frag);}
this.fillInTemplate(args,frag);},buildFromTemplate:function(args,frag){var _6d8=false;if(args["templatepath"]){args["templatePath"]=args["templatepath"];}
dojo.widget.fillFromTemplateCache(this,args["templatePath"],null,_6d8);var ts=dojo.widget._templateCache[this.templatePath?this.templatePath.toString():this.widgetType];if((ts)&&(!_6d8)){if(!this.templateString.length){this.templateString=ts["string"];}
if(!this.templateNode){this.templateNode=ts["node"];}}
var _6da=false;var node=null;var tstr=this.templateString;if((!this.templateNode)&&(this.templateString)){_6da=this.templateString.match(/\$\{([^\}]+)\}/g);if(_6da){var hash=this.strings||{};for(var key in dojo.widget.defaultStrings){if(dojo.lang.isUndefined(hash[key])){hash[key]=dojo.widget.defaultStrings[key];}}
for(var i=0;i<_6da.length;i++){var key=_6da[i];key=key.substring(2,key.length-1);var kval=(key.substring(0,5)=="this.")?dojo.getObject(key.substring(5),false,this):hash[key];var _6e1;if((kval)||(dojo.lang.isString(kval))){_6e1=new String((dojo.lang.isFunction(kval))?kval.call(this,key,this.templateString):kval);while(_6e1.indexOf("\"")>-1){_6e1=_6e1.replace("\"","&quot;");}
tstr=tstr.replace(_6da[i],_6e1);}}
}else{this.templateNode=this.createNodesFromText(this.templateString,true)[0];if(!_6d8){ts.node=this.templateNode;}}
}
if((!this.templateNode)&&(!_6da)){dojo.debug("DomWidget.buildFromTemplate: could not create template");return false;}else{if(!_6da){node=this.templateNode.cloneNode(true);if(!node){return false;}}else{node=this.createNodesFromText(tstr,true)[0];}}
this.domNode=node;this.attachTemplateNodes();if(this.isContainer&&this.containerNode){var src=this.getFragNodeRef(frag);if(src){dojo.dom.moveChildren(src,this.containerNode);}}
},attachTemplateNodes:function(_6e3,_6e4){if(!_6e3){_6e3=this.domNode;}
if(!_6e4){_6e4=this;}
return dojo.widget.attachTemplateNodes(_6e3,_6e4,dojo.widget.getDojoEventsFromStr(this.templateString));},fillInTemplate:function(){},destroyRendering:function(){try{dojo.dom.destroyNode(this.domNode);delete this.domNode;}
catch(e){}
if(this._sourceNodeRef){try{dojo.dom.destroyNode(this._sourceNodeRef);}
catch(e){}}
},createNodesFromText:function(){dojo.unimplemented("dojo.widget.DomWidget.createNodesFromText");}});dojo.html._toggle=function(node,_6e6,_6e7){node=dojo.byId(node);_6e7(node,!_6e6(node));return _6e6(node);};dojo.html.show=function(node){node=dojo.byId(node);if(dojo.html.getStyleProperty(node,"display")=="none"){var _6e9=dojo.html.getAttribute("djDisplayCache");dojo.html.setStyle(node,"display",(_6e9||""));node.removeAttribute("djDisplayCache");}};dojo.html.hide=function(node){node=dojo.byId(node);var _6eb=dojo.html.getAttribute("djDisplayCache");if(_6eb==null){var d=dojo.html.getStyleProperty(node,"display");if(d!="none"){node.setAttribute("djDisplayCache",d);}}
dojo.html.setStyle(node,"display","none");};dojo.html.setShowing=function(node,_6ee){dojo.html[(_6ee?"show":"hide")](node);};dojo.html.isShowing=function(node){return (dojo.html.getStyleProperty(node,"display")!="none");};dojo.html.toggleShowing=function(node){return dojo.html._toggle(node,dojo.html.isShowing,dojo.html.setShowing);};dojo.html.displayMap={tr:"",td:"",th:"",img:"inline",span:"inline",input:"inline",button:"inline"};dojo.html.suggestDisplayByTagName=function(node){node=dojo.byId(node);if(node&&node.tagName){var tag=node.tagName.toLowerCase();return (tag in dojo.html.displayMap?dojo.html.displayMap[tag]:"block");}};dojo.html.setDisplay=function(node,_6f4){dojo.html.setStyle(node,"display",((_6f4 instanceof String||typeof _6f4=="string")?_6f4:(_6f4?dojo.html.suggestDisplayByTagName(node):"none")));};dojo.html.isDisplayed=function(node){return (dojo.html.getComputedStyle(node,"display")!="none");};dojo.html.toggleDisplay=function(node){return dojo.html._toggle(node,dojo.html.isDisplayed,dojo.html.setDisplay);};dojo.html.setVisibility=function(node,_6f8){dojo.html.setStyle(node,"visibility",((_6f8 instanceof String||typeof _6f8=="string")?_6f8:(_6f8?"visible":"hidden")));};dojo.html.isVisible=function(node){return (dojo.html.getComputedStyle(node,"visibility")!="hidden");};dojo.html.toggleVisibility=function(node){return dojo.html._toggle(node,dojo.html.isVisible,dojo.html.setVisibility);};dojo.html.setOpacity=function(node,_6fc,_6fd){node=dojo.byId(node);var h=dojo.render.html;if(!_6fd){if(_6fc>=1){if(h.ie){dojo.html.clearOpacity(node);return;}else{_6fc=0.999999;}}else{if(_6fc<0){_6fc=0;}}
}
if(h.ie){if(node.nodeName.toLowerCase()=="tr"){var tds=node.getElementsByTagName("td");for(var x=0;x<tds.length;x++){tds[x].style.filter="Alpha(Opacity="+_6fc*100+")";}}
node.style.filter="Alpha(Opacity="+_6fc*100+")";}else{if(h.moz){node.style.opacity=_6fc;node.style.MozOpacity=_6fc;}else{if(h.safari){node.style.opacity=_6fc;node.style.KhtmlOpacity=_6fc;}else{node.style.opacity=_6fc;}}
}};dojo.html.clearOpacity=function(node){node=dojo.byId(node);var ns=node.style;var h=dojo.render.html;if(h.ie){try{if(node.filters&&node.filters.alpha){ns.filter="";}}
catch(e){}}else{if(h.moz){ns.opacity=1;ns.MozOpacity=1;}else{if(h.safari){ns.opacity=1;ns.KhtmlOpacity=1;}else{ns.opacity=1;}}
}};dojo.html.getOpacity=function(node){node=dojo.byId(node);var h=dojo.render.html;if(h.ie){var opac=(node.filters&&node.filters.alpha&&typeof node.filters.alpha.opacity=="number"?node.filters.alpha.opacity:100)/100;}else{var opac=node.style.opacity||node.style.MozOpacity||node.style.KhtmlOpacity||1;}
return opac>=0.999999?1:Number(opac);};dojo.html.sumAncestorProperties=function(node,prop){node=dojo.byId(node);if(!node){return 0;}
var _709=0;while(node){if(dojo.html.getComputedStyle(node,"position")=="fixed"){return 0;}
var val=node[prop];if(val){_709+=val-0;if(node==dojo.body()){break;}}
node=node.parentNode;}
return _709;};dojo.html.setStyleAttributes=function(node,_70c){node=dojo.byId(node);var _70d=_70c.replace(/(;)?\s*$/,"").split(";");for(var i=0;i<_70d.length;i++){var _70f=_70d[i].split(":");var name=_70f[0].replace(/\s*$/,"").replace(/^\s*/,"").toLowerCase();var _711=_70f[1].replace(/\s*$/,"").replace(/^\s*/,"");switch(name){case "opacity":
dojo.html.setOpacity(node,_711);break;case "content-height":
dojo.html.setContentBox(node,{height:_711});break;case "content-width":
dojo.html.setContentBox(node,{width:_711});break;case "outer-height":
dojo.html.setMarginBox(node,{height:_711});break;case "outer-width":
dojo.html.setMarginBox(node,{width:_711});break;default:
node.style[dojo.html.toCamelCase(name)]=_711;}}
};dojo.html.boxSizing={MARGIN_BOX:"margin-box",BORDER_BOX:"border-box",PADDING_BOX:"padding-box",CONTENT_BOX:"content-box"};dojo.html.getAbsolutePosition=dojo.html.abs=function(node,_713,_714){node=dojo.byId(node);var _715=dojo.doc();var ret={x:0,y:0};var bs=dojo.html.boxSizing;if(!_714){_714=bs.CONTENT_BOX;}
var _718=2;var _719;switch(_714){case bs.MARGIN_BOX:
_719=3;break;case bs.BORDER_BOX:
_719=2;break;case bs.PADDING_BOX:
default:
_719=1;break;case bs.CONTENT_BOX:
_719=0;break;}
var h=dojo.render.html;var db=_715["body"]||_715["documentElement"];if(h.ie){with(node.getBoundingClientRect()){ret.x=left-2;ret.y=top-2;}}else{if(_715["getBoxObjectFor"]){_718=1;try{var bo=_715.getBoxObjectFor(node);ret.x=bo.x-dojo.html.sumAncestorProperties(node,"scrollLeft");ret.y=bo.y-dojo.html.sumAncestorProperties(node,"scrollTop");}
catch(e){}}else{if(node["offsetParent"]){var _71d;if((h.safari)&&(node.style.getPropertyValue("position")=="absolute")&&(node.parentNode==db)){_71d=db;}else{_71d=db.parentNode;}
if(node.parentNode!=db){var nd=node;if(dojo.render.html.opera){nd=db;}
ret.x-=dojo.html.sumAncestorProperties(nd,"scrollLeft");ret.y-=dojo.html.sumAncestorProperties(nd,"scrollTop");}
var _71f=node;do{var n=_71f["offsetLeft"];if(!h.opera||n>0){ret.x+=isNaN(n)?0:n;}
var m=_71f["offsetTop"];ret.y+=isNaN(m)?0:m;_71f=_71f.offsetParent;}while((_71f!=_71d)&&(_71f!=null));}else{if(node["x"]&&node["y"]){ret.x+=isNaN(node.x)?0:node.x;ret.y+=isNaN(node.y)?0:node.y;}}
}}
if(_713){var _722=dojo.html.getScroll();ret.y+=_722.top;ret.x+=_722.left;}
var _723=[dojo.html.getPaddingExtent,dojo.html.getBorderExtent,dojo.html.getMarginExtent];if(_718>_719){for(var i=_719;i<_718;++i){ret.y+=_723[i](node,"top");ret.x+=_723[i](node,"left");}}else{if(_718<_719){for(var i=_719;i>_718;--i){ret.y-=_723[i-1](node,"top");ret.x-=_723[i-1](node,"left");}}
}
ret.top=ret.y;ret.left=ret.x;return ret;};dojo.html.isPositionAbsolute=function(node){return (dojo.html.getComputedStyle(node,"position")=="absolute");};dojo.html._getComponentPixelValues=function(node,_727,_728,_729){var _72a=["top","bottom","left","right"];var obj={};for(var i in _72a){side=_72a[i];obj[side]=_728(node,_727+side,_729);}
obj.width=obj.left+obj.right;obj.height=obj.top+obj.bottom;return obj;};dojo.html.getMargin=function(node){return dojo.html._getComponentPixelValues(node,"margin-",dojo.html.getPixelValue,dojo.html.isPositionAbsolute(node));};dojo.html.getBorder=function(node){return dojo.html._getComponentPixelValues(node,"",dojo.html.getBorderExtent);};dojo.html.getBorderExtent=function(node,side){return (dojo.html.getStyle(node,"border-"+side+"-style")=="none"?0:dojo.html.getPixelValue(node,"border-"+side+"-width"));};dojo.html.getMarginExtent=function(node,side){return dojo.html.getPixelValue(node,"margin-"+side,dojo.html.isPositionAbsolute(node));};dojo.html.getPaddingExtent=function(node,side){return dojo.html.getPixelValue(node,"padding-"+side,true);};dojo.html.getPadding=function(node){return dojo.html._getComponentPixelValues(node,"padding-",dojo.html.getPixelValue,true);};dojo.html.getPadBorder=function(node){var pad=dojo.html.getPadding(node);var _738=dojo.html.getBorder(node);return {width:pad.width+_738.width,height:pad.height+_738.height};};dojo.html.getBoxSizing=function(node){var h=dojo.render.html;var bs=dojo.html.boxSizing;if(((h.ie)||(h.opera))&&node.nodeName!="IMG"){var cm=document["compatMode"];if((cm=="BackCompat")||(cm=="QuirksMode")){return bs.BORDER_BOX;}else{return bs.CONTENT_BOX;}}else{if(arguments.length==0){node=document.documentElement;}
var _73d=dojo.html.getStyle(node,"-moz-box-sizing");if(!_73d){_73d=dojo.html.getStyle(node,"box-sizing");}
return (_73d?_73d:bs.CONTENT_BOX);}};dojo.html.isBorderBox=function(node){return (dojo.html.getBoxSizing(node)==dojo.html.boxSizing.BORDER_BOX);};dojo.html.getBorderBox=function(node){node=dojo.byId(node);return {width:node.offsetWidth,height:node.offsetHeight};};dojo.html.getPaddingBox=function(node){var box=dojo.html.getBorderBox(node);var _742=dojo.html.getBorder(node);return {width:box.width-_742.width,height:box.height-_742.height};};dojo.html.getContentBox=function(node){node=dojo.byId(node);var _744=dojo.html.getPadBorder(node);return {width:node.offsetWidth-_744.width,height:node.offsetHeight-_744.height};};dojo.html.setContentBox=function(node,args){node=dojo.byId(node);var _747=0;var _748=0;var isbb=dojo.html.isBorderBox(node);var _74a=(isbb?dojo.html.getPadBorder(node):{width:0,height:0});var ret={};if(typeof args.width!="undefined"){_747=args.width+_74a.width;ret.width=dojo.html.setPositivePixelValue(node,"width",_747);}
if(typeof args.height!="undefined"){_748=args.height+_74a.height;ret.height=dojo.html.setPositivePixelValue(node,"height",_748);}
return ret;};dojo.html.getMarginBox=function(node){var _74d=dojo.html.getBorderBox(node);var _74e=dojo.html.getMargin(node);return {width:_74d.width+_74e.width,height:_74d.height+_74e.height};};dojo.html.setMarginBox=function(node,args){node=dojo.byId(node);var _751=0;var _752=0;var isbb=dojo.html.isBorderBox(node);var _754=(!isbb?dojo.html.getPadBorder(node):{width:0,height:0});var _755=dojo.html.getMargin(node);var ret={};if(typeof args.width!="undefined"){_751=args.width-_754.width;_751-=_755.width;ret.width=dojo.html.setPositivePixelValue(node,"width",_751);}
if(typeof args.height!="undefined"){_752=args.height-_754.height;_752-=_755.height;ret.height=dojo.html.setPositivePixelValue(node,"height",_752);}
return ret;};dojo.html.getElementBox=function(node,type){var bs=dojo.html.boxSizing;switch(type){case bs.MARGIN_BOX:
return dojo.html.getMarginBox(node);case bs.BORDER_BOX:
return dojo.html.getBorderBox(node);case bs.PADDING_BOX:
return dojo.html.getPaddingBox(node);case bs.CONTENT_BOX:
default:
return dojo.html.getContentBox(node);}};dojo.html.toCoordinateObject=dojo.html.toCoordinateArray=function(_75a,_75b,_75c){if(!_75a.nodeType&&!(_75a instanceof String||typeof _75a=="string")&&("width" in _75a||"height" in _75a||"left" in _75a||"x" in _75a||"top" in _75a||"y" in _75a)){var ret={left:_75a.left||_75a.x||0,top:_75a.top||_75a.y||0,width:_75a.width||0,height:_75a.height||0};}else{var node=dojo.byId(_75a);var pos=dojo.html.abs(node,_75b,_75c);var _760=dojo.html.getMarginBox(node);var ret={left:pos.left,top:pos.top,width:_760.width,height:_760.height};}
ret.x=ret.left;ret.y=ret.top;return ret;};dojo.html.setMarginBoxWidth=dojo.html.setOuterWidth=function(node,_762){return dojo.html._callDeprecated("setMarginBoxWidth","setMarginBox",arguments,"width");};dojo.html.setMarginBoxHeight=dojo.html.setOuterHeight=function(){return dojo.html._callDeprecated("setMarginBoxHeight","setMarginBox",arguments,"height");};dojo.html.getMarginBoxWidth=dojo.html.getOuterWidth=function(){return dojo.html._callDeprecated("getMarginBoxWidth","getMarginBox",arguments,null,"width");};dojo.html.getMarginBoxHeight=dojo.html.getOuterHeight=function(){return dojo.html._callDeprecated("getMarginBoxHeight","getMarginBox",arguments,null,"height");};dojo.html.getTotalOffset=function(node,type,_765){return dojo.html._callDeprecated("getTotalOffset","getAbsolutePosition",arguments,null,type);};dojo.html.getAbsoluteX=function(node,_767){return dojo.html._callDeprecated("getAbsoluteX","getAbsolutePosition",arguments,null,"x");};dojo.html.getAbsoluteY=function(node,_769){return dojo.html._callDeprecated("getAbsoluteY","getAbsolutePosition",arguments,null,"y");};dojo.html.totalOffsetLeft=function(node,_76b){return dojo.html._callDeprecated("totalOffsetLeft","getAbsolutePosition",arguments,null,"left");};dojo.html.totalOffsetTop=function(node,_76d){return dojo.html._callDeprecated("totalOffsetTop","getAbsolutePosition",arguments,null,"top");};dojo.html.getMarginWidth=function(node){return dojo.html._callDeprecated("getMarginWidth","getMargin",arguments,null,"width");};dojo.html.getMarginHeight=function(node){return dojo.html._callDeprecated("getMarginHeight","getMargin",arguments,null,"height");};dojo.html.getBorderWidth=function(node){return dojo.html._callDeprecated("getBorderWidth","getBorder",arguments,null,"width");};dojo.html.getBorderHeight=function(node){return dojo.html._callDeprecated("getBorderHeight","getBorder",arguments,null,"height");};dojo.html.getPaddingWidth=function(node){return dojo.html._callDeprecated("getPaddingWidth","getPadding",arguments,null,"width");};dojo.html.getPaddingHeight=function(node){return dojo.html._callDeprecated("getPaddingHeight","getPadding",arguments,null,"height");};dojo.html.getPadBorderWidth=function(node){return dojo.html._callDeprecated("getPadBorderWidth","getPadBorder",arguments,null,"width");};dojo.html.getPadBorderHeight=function(node){return dojo.html._callDeprecated("getPadBorderHeight","getPadBorder",arguments,null,"height");};dojo.html.getBorderBoxWidth=dojo.html.getInnerWidth=function(){return dojo.html._callDeprecated("getBorderBoxWidth","getBorderBox",arguments,null,"width");};dojo.html.getBorderBoxHeight=dojo.html.getInnerHeight=function(){return dojo.html._callDeprecated("getBorderBoxHeight","getBorderBox",arguments,null,"height");};dojo.html.getContentBoxWidth=dojo.html.getContentWidth=function(){return dojo.html._callDeprecated("getContentBoxWidth","getContentBox",arguments,null,"width");};dojo.html.getContentBoxHeight=dojo.html.getContentHeight=function(){return dojo.html._callDeprecated("getContentBoxHeight","getContentBox",arguments,null,"height");};dojo.html.setContentBoxWidth=dojo.html.setContentWidth=function(node,_777){return dojo.html._callDeprecated("setContentBoxWidth","setContentBox",arguments,"width");};dojo.html.setContentBoxHeight=dojo.html.setContentHeight=function(node,_779){return dojo.html._callDeprecated("setContentBoxHeight","setContentBox",arguments,"height");};dojo.html.getElementWindow=function(_77a){return dojo.html.getDocumentWindow(_77a.ownerDocument);};dojo.html.getDocumentWindow=function(doc){if(dojo.render.html.safari&&!doc._parentWindow){var fix=function(win){win.document._parentWindow=win;for(var i=0;i<win.frames.length;i++){fix(win.frames[i]);}};fix(window.top);}
if(dojo.render.html.ie&&window!==document.parentWindow&&!doc._parentWindow){doc.parentWindow.execScript("document._parentWindow = window;","Javascript");var win=doc._parentWindow;doc._parentWindow=null;return win;}
return doc._parentWindow||doc.parentWindow||doc.defaultView;};dojo.html.getAbsolutePositionExt=function(node,_781,_782,_783){var _784=dojo.html.getElementWindow(node);var ret=dojo.withGlobal(_784,"getAbsolutePosition",dojo.html,arguments);var win=dojo.html.getElementWindow(node);if(_783!=win&&win.frameElement){var ext=dojo.html.getAbsolutePositionExt(win.frameElement,_781,_782,_783);ret.x+=ext.x;ret.y+=ext.y;}
ret.top=ret.y;ret.left=ret.x;return ret;};dojo.html.gravity=function(node,e){node=dojo.byId(node);var _78a=dojo.html.getCursorPosition(e);with(dojo.html){var _78b=getAbsolutePosition(node,true);var bb=getBorderBox(node);var _78d=_78b.x+(bb.width/2);var _78e=_78b.y+(bb.height/2);}
with(dojo.html.gravity){return ((_78a.x<_78d?WEST:EAST)|(_78a.y<_78e?NORTH:SOUTH));}};dojo.html.gravity.NORTH=1;dojo.html.gravity.SOUTH=1<<1;dojo.html.gravity.EAST=1<<2;dojo.html.gravity.WEST=1<<3;dojo.html.overElement=function(_78f,e){_78f=dojo.byId(_78f);var _791=dojo.html.getCursorPosition(e);var bb=dojo.html.getBorderBox(_78f);var _793=dojo.html.getAbsolutePosition(_78f,true,dojo.html.boxSizing.BORDER_BOX);var top=_793.y;var _795=top+bb.height;var left=_793.x;var _797=left+bb.width;return (_791.x>=left&&_791.x<=_797&&_791.y>=top&&_791.y<=_795);};dojo.html.renderedTextContent=function(node){node=dojo.byId(node);var _799="";if(node==null){return _799;}
for(var i=0;i<node.childNodes.length;i++){switch(node.childNodes[i].nodeType){case 1:
case 5:
var _79b="unknown";try{_79b=dojo.html.getStyle(node.childNodes[i],"display");}
catch(E){}
switch(_79b){case "block":
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
_799+="\n";_799+=dojo.html.renderedTextContent(node.childNodes[i]);_799+="\n";break;case "none":
break;default:
if(node.childNodes[i].tagName&&node.childNodes[i].tagName.toLowerCase()=="br"){_799+="\n";}else{_799+=dojo.html.renderedTextContent(node.childNodes[i]);}
break;}
break;case 3:
case 2:
case 4:
var text=node.childNodes[i].nodeValue;var _79d="unknown";try{_79d=dojo.html.getStyle(node,"text-transform");}
catch(E){}
switch(_79d){case "capitalize":
var _79e=text.split(" ");for(var i=0;i<_79e.length;i++){_79e[i]=_79e[i].charAt(0).toUpperCase()+_79e[i].substring(1);}
text=_79e.join(" ");break;case "uppercase":
text=text.toUpperCase();break;case "lowercase":
text=text.toLowerCase();break;default:
break;}
switch(_79d){case "nowrap":
break;case "pre-wrap":
break;case "pre-line":
break;case "pre":
break;default:
text=text.replace(/\s+/," ");if(/\s$/.test(_799)){text.replace(/^\s/,"");}
break;}
_799+=text;break;default:
break;}}
return _799;};dojo.html.createNodesFromText=function(txt,trim){if(trim){txt=txt.replace(/^\s+|\s+$/g,"");}
var tn=dojo.doc().createElement("div");tn.style.visibility="hidden";dojo.body().appendChild(tn);var _7a2="none";if((/^<t[dh][\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table><tbody><tr>"+txt+"</tr></tbody></table>";_7a2="cell";}else{if((/^<tr[\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table><tbody>"+txt+"</tbody></table>";_7a2="row";}else{if((/^<(thead|tbody|tfoot)[\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table>"+txt+"</table>";_7a2="section";}}
}
tn.innerHTML=txt;if(tn["normalize"]){tn.normalize();}
var _7a3=null;switch(_7a2){case "cell":
_7a3=tn.getElementsByTagName("tr")[0];break;case "row":
_7a3=tn.getElementsByTagName("tbody")[0];break;case "section":
_7a3=tn.getElementsByTagName("table")[0];break;default:
_7a3=tn;break;}
var _7a4=[];for(var x=0;x<_7a3.childNodes.length;x++){_7a4.push(_7a3.childNodes[x].cloneNode(true));}
tn.style.display="none";dojo.html.destroyNode(tn);return _7a4;};dojo.html.placeOnScreen=function(node,_7a7,_7a8,_7a9,_7aa,_7ab,_7ac){if(_7a7 instanceof Array||typeof _7a7=="array"){_7ac=_7ab;_7ab=_7aa;_7aa=_7a9;_7a9=_7a8;_7a8=_7a7[1];_7a7=_7a7[0];}
if(_7ab instanceof String||typeof _7ab=="string"){_7ab=_7ab.split(",");}
if(!isNaN(_7a9)){_7a9=[Number(_7a9),Number(_7a9)];}else{if(!(_7a9 instanceof Array||typeof _7a9=="array")){_7a9=[0,0];}}
var _7ad=dojo.html.getScroll().offset;var view=dojo.html.getViewport();node=dojo.byId(node);var _7af=node.style.display;node.style.display="";var bb=dojo.html.getBorderBox(node);var w=bb.width;var h=bb.height;node.style.display=_7af;if(!(_7ab instanceof Array||typeof _7ab=="array")){_7ab=["TL"];}
var _7b3,_7b4,_7b5=Infinity,_7b6;for(var _7b7=0;_7b7<_7ab.length;++_7b7){var _7b8=_7ab[_7b7];var _7b9=true;var tryX=_7a7-(_7b8.charAt(1)=="L"?0:w)+_7a9[0]*(_7b8.charAt(1)=="L"?1:-1);var tryY=_7a8-(_7b8.charAt(0)=="T"?0:h)+_7a9[1]*(_7b8.charAt(0)=="T"?1:-1);if(_7aa){tryX-=_7ad.x;tryY-=_7ad.y;}
if(tryX<0){tryX=0;_7b9=false;}
if(tryY<0){tryY=0;_7b9=false;}
var x=tryX+w;if(x>view.width){x=view.width-w;_7b9=false;}else{x=tryX;}
x=Math.max(_7a9[0],x)+_7ad.x;var y=tryY+h;if(y>view.height){y=view.height-h;_7b9=false;}else{y=tryY;}
y=Math.max(_7a9[1],y)+_7ad.y;if(_7b9){_7b3=x;_7b4=y;_7b5=0;_7b6=_7b8;break;}else{var dist=Math.pow(x-tryX-_7ad.x,2)+Math.pow(y-tryY-_7ad.y,2);if(_7b5>dist){_7b5=dist;_7b3=x;_7b4=y;_7b6=_7b8;}}
}
if(!_7ac){node.style.left=_7b3+"px";node.style.top=_7b4+"px";}
return {left:_7b3,top:_7b4,x:_7b3,y:_7b4,dist:_7b5,corner:_7b6};};dojo.html.placeOnScreenAroundElement=function(node,_7c0,_7c1,_7c2,_7c3,_7c4){var best,_7c6=Infinity;_7c0=dojo.byId(_7c0);var _7c7=_7c0.style.display;_7c0.style.display="";var mb=dojo.html.getElementBox(_7c0,_7c2);var _7c9=mb.width;var _7ca=mb.height;var _7cb=dojo.html.getAbsolutePosition(_7c0,true,_7c2);_7c0.style.display=_7c7;for(var _7cc in _7c3){var pos,_7ce,_7cf;var _7d0=_7c3[_7cc];_7ce=_7cb.x+(_7cc.charAt(1)=="L"?0:_7c9);_7cf=_7cb.y+(_7cc.charAt(0)=="T"?0:_7ca);pos=dojo.html.placeOnScreen(node,_7ce,_7cf,_7c1,true,_7d0,true);if(pos.dist==0){best=pos;break;}else{if(_7c6>pos.dist){_7c6=pos.dist;best=pos;}}
}
if(!_7c4){node.style.left=best.left+"px";node.style.top=best.top+"px";}
return best;};dojo.html.scrollIntoView=function(node){if(!node){return;}
if(dojo.render.html.ie){if(dojo.html.getBorderBox(node.parentNode).height<=node.parentNode.scrollHeight){node.scrollIntoView(false);}}else{if(dojo.render.html.mozilla){node.scrollIntoView(false);}else{var _7d2=node.parentNode;var _7d3=_7d2.scrollTop+dojo.html.getBorderBox(_7d2).height;var _7d4=node.offsetTop+dojo.html.getMarginBox(node).height;if(_7d3<_7d4){_7d2.scrollTop+=(_7d4-_7d3);}else{if(_7d2.scrollTop>node.offsetTop){_7d2.scrollTop-=(_7d2.scrollTop-node.offsetTop);}}
}}
};dojo.gfx.color.Color=function(r,g,b,a){if(dojo.lang.isArray(r)){this.r=r[0];this.g=r[1];this.b=r[2];this.a=r[3]||1;}else{if(dojo.lang.isString(r)){var rgb=dojo.gfx.color.extractRGB(r);this.r=rgb[0];this.g=rgb[1];this.b=rgb[2];this.a=g||1;}else{if(r instanceof dojo.gfx.color.Color){this.r=r.r;this.b=r.b;this.g=r.g;this.a=r.a;}else{this.r=r;this.g=g;this.b=b;this.a=a;}}
}};dojo.gfx.color.Color.fromArray=function(arr){return new dojo.gfx.color.Color(arr[0],arr[1],arr[2],arr[3]);};dojo.extend(dojo.gfx.color.Color,{toRgb:function(_7db){if(_7db){return this.toRgba();}else{return [this.r,this.g,this.b];}},toRgba:function(){return [this.r,this.g,this.b,this.a];},toHex:function(){return dojo.gfx.color.rgb2hex(this.toRgb());},toCss:function(){return "rgb("+this.toRgb().join()+")";},toString:function(){return this.toHex();},blend:function(_7dc,_7dd){var rgb=null;if(dojo.lang.isArray(_7dc)){rgb=_7dc;}else{if(_7dc instanceof dojo.gfx.color.Color){rgb=_7dc.toRgb();}else{rgb=new dojo.gfx.color.Color(_7dc).toRgb();}}
return dojo.gfx.color.blend(this.toRgb(),rgb,_7dd);}});dojo.gfx.color.named={white:[255,255,255],black:[0,0,0],red:[255,0,0],green:[0,255,0],lime:[0,255,0],blue:[0,0,255],navy:[0,0,128],gray:[128,128,128],silver:[192,192,192]};dojo.gfx.color.blend=function(a,b,_7e1){if(typeof a=="string"){return dojo.gfx.color.blendHex(a,b,_7e1);}
if(!_7e1){_7e1=0;}
_7e1=Math.min(Math.max(-1,_7e1),1);_7e1=((_7e1+1)/2);var c=[];for(var x=0;x<3;x++){c[x]=parseInt(b[x]+((a[x]-b[x])*_7e1));}
return c;};dojo.gfx.color.blendHex=function(a,b,_7e6){return dojo.gfx.color.rgb2hex(dojo.gfx.color.blend(dojo.gfx.color.hex2rgb(a),dojo.gfx.color.hex2rgb(b),_7e6));};dojo.gfx.color.extractRGB=function(_7e7){_7e7=_7e7.toLowerCase();if(_7e7.indexOf("rgb")==0){var _7e8=_7e7.match(/rgba*\((\d+), *(\d+), *(\d+)/i);var ret=_7e8.splice(1,3);return ret;}else{var _7ea=dojo.gfx.color.hex2rgb(_7e7);if(_7ea){return _7ea;}else{return dojo.gfx.color.named[_7e7]||[255,255,255];}}
};dojo.gfx.color.hex2rgb=function(hex){var _7ec="0123456789ABCDEF";var rgb=new Array(3);if(hex.indexOf("#")==0){hex=hex.substring(1);}
hex=hex.toUpperCase();if(hex.replace(new RegExp("["+_7ec+"]","g"),"")!=""){return null;}
if(hex.length==3){rgb[0]=hex.charAt(0)+hex.charAt(0);rgb[1]=hex.charAt(1)+hex.charAt(1);rgb[2]=hex.charAt(2)+hex.charAt(2);}else{rgb[0]=hex.substring(0,2);rgb[1]=hex.substring(2,4);rgb[2]=hex.substring(4);}
for(var i=0;i<rgb.length;i++){rgb[i]=_7ec.indexOf(rgb[i].charAt(0))*16+_7ec.indexOf(rgb[i].charAt(1));}
return rgb;};dojo.gfx.color.rgb2hex=function(r,g,b){if(dojo.lang.isArray(r)){g=r[1]||0;b=r[2]||0;r=r[0]||0;}
var ret=dojo.lang.map([r,g,b],function(x){x=new Number(x);var s=x.toString(16);while(s.length<2){s="0"+s;}
return s;});ret.unshift("#");return ret.join("");};dojo.lfx.Line=function(_7f5,end){this.start=_7f5;this.end=end;if(dojo.lang.isArray(_7f5)){var diff=[];dojo.lang.forEach(this.start,function(s,i){diff[i]=this.end[i]-s;},this);this.getValue=function(n){var res=[];dojo.lang.forEach(this.start,function(s,i){res[i]=(diff[i]*n)+s;},this);return res;};}else{var diff=end-_7f5;this.getValue=function(n){return (diff*n)+this.start;};}};dojo.lfx.easeDefault=function(n){if(dojo.render.html.khtml){return (parseFloat("0.5")+((Math.sin((n+parseFloat("1.5"))*Math.PI))/2));}else{return (0.5+((Math.sin((n+1.5)*Math.PI))/2));}};dojo.lfx.easeIn=function(n){return Math.pow(n,3);};dojo.lfx.easeOut=function(n){return (1-Math.pow(1-n,3));};dojo.lfx.easeInOut=function(n){return ((3*Math.pow(n,2))-(2*Math.pow(n,3)));};dojo.lfx.IAnimation=function(){};dojo.lang.extend(dojo.lfx.IAnimation,{curve:null,duration:1000,easing:null,repeatCount:0,rate:25,handler:null,beforeBegin:null,onBegin:null,onAnimate:null,onEnd:null,onPlay:null,onPause:null,onStop:null,play:null,pause:null,stop:null,connect:function(evt,_804,_805){if(!_805){_805=_804;_804=this;}
_805=dojo.lang.hitch(_804,_805);var _806=this[evt]||function(){};this[evt]=function(){var ret=_806.apply(this,arguments);_805.apply(this,arguments);return ret;};return this;},fire:function(evt,args){if(this[evt]){this[evt].apply(this,(args||[]));}
return this;},repeat:function(_80a){this.repeatCount=_80a;return this;},_active:false,_paused:false});dojo.lfx.Animation=function(_80b,_80c,_80d,_80e,_80f,rate){dojo.lfx.IAnimation.call(this);if(dojo.lang.isNumber(_80b)||(!_80b&&_80c.getValue)){rate=_80f;_80f=_80e;_80e=_80d;_80d=_80c;_80c=_80b;_80b=null;}else{if(_80b.getValue||dojo.lang.isArray(_80b)){rate=_80e;_80f=_80d;_80e=_80c;_80d=_80b;_80c=null;_80b=null;}}
if(dojo.lang.isArray(_80d)){this.curve=new dojo.lfx.Line(_80d[0],_80d[1]);}else{this.curve=_80d;}
if(_80c!=null&&_80c>0){this.duration=_80c;}
if(_80f){this.repeatCount=_80f;}
if(rate){this.rate=rate;}
if(_80b){dojo.lang.forEach(["handler","beforeBegin","onBegin","onEnd","onPlay","onStop","onAnimate"],function(item){if(_80b[item]){this.connect(item,_80b[item]);}},this);}
if(_80e&&dojo.lang.isFunction(_80e)){this.easing=_80e;}};dojo.inherits(dojo.lfx.Animation,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Animation,{_startTime:null,_endTime:null,_timer:null,_percent:0,_startRepeatCount:0,play:function(_812,_813){if(_813){clearTimeout(this._timer);this._active=false;this._paused=false;this._percent=0;}else{if(this._active&&!this._paused){return this;}}
this.fire("handler",["beforeBegin"]);this.fire("beforeBegin");if(_812>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_813);}),_812);return this;}
this._startTime=new Date().valueOf();if(this._paused){this._startTime-=(this.duration*this._percent/100);}
this._endTime=this._startTime+this.duration;this._active=true;this._paused=false;var step=this._percent/100;var _815=this.curve.getValue(step);if(this._percent==0){if(!this._startRepeatCount){this._startRepeatCount=this.repeatCount;}
this.fire("handler",["begin",_815]);this.fire("onBegin",[_815]);}
this.fire("handler",["play",_815]);this.fire("onPlay",[_815]);this._cycle();return this;},pause:function(){clearTimeout(this._timer);if(!this._active){return this;}
this._paused=true;var _816=this.curve.getValue(this._percent/100);this.fire("handler",["pause",_816]);this.fire("onPause",[_816]);return this;},gotoPercent:function(pct,_818){clearTimeout(this._timer);this._active=true;this._paused=true;this._percent=pct;if(_818){this.play();}
return this;},stop:function(_819){clearTimeout(this._timer);var step=this._percent/100;if(_819){step=1;}
var _81b=this.curve.getValue(step);this.fire("handler",["stop",_81b]);this.fire("onStop",[_81b]);this._active=false;this._paused=false;return this;},status:function(){if(this._active){return this._paused?"paused":"playing";}else{return "stopped";}
return this;},_cycle:function(){clearTimeout(this._timer);if(this._active){var curr=new Date().valueOf();var step=(curr-this._startTime)/(this._endTime-this._startTime);if(step>=1){step=1;this._percent=100;}else{this._percent=step*100;}
if((this.easing)&&(dojo.lang.isFunction(this.easing))){step=this.easing(step);}
var _81e=this.curve.getValue(step);this.fire("handler",["animate",_81e]);this.fire("onAnimate",[_81e]);if(step<1){this._timer=setTimeout(dojo.lang.hitch(this,"_cycle"),this.rate);}else{this._active=false;this.fire("handler",["end"]);this.fire("onEnd");if(this.repeatCount>0){this.repeatCount--;this.play(null,true);}else{if(this.repeatCount==-1){this.play(null,true);}else{if(this._startRepeatCount){this.repeatCount=this._startRepeatCount;this._startRepeatCount=0;}}
}}
}
return this;}});dojo.lfx.Combine=function(_81f){dojo.lfx.IAnimation.call(this);this._anims=[];this._animsEnded=0;var _820=arguments;if(_820.length==1&&(dojo.lang.isArray(_820[0])||dojo.lang.isArrayLike(_820[0]))){_820=_820[0];}
dojo.lang.forEach(_820,function(anim){this._anims.push(anim);anim.connect("onEnd",dojo.lang.hitch(this,"_onAnimsEnded"));},this);};dojo.inherits(dojo.lfx.Combine,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Combine,{_animsEnded:0,play:function(_822,_823){if(!this._anims.length){return this;}
this.fire("beforeBegin");if(_822>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_823);}),_822);return this;}
if(_823||this._anims[0].percent==0){this.fire("onBegin");}
this.fire("onPlay");this._animsCall("play",null,_823);return this;},pause:function(){this.fire("onPause");this._animsCall("pause");return this;},stop:function(_824){this.fire("onStop");this._animsCall("stop",_824);return this;},_onAnimsEnded:function(){this._animsEnded++;if(this._animsEnded>=this._anims.length){this.fire("onEnd");}
return this;},_animsCall:function(_825){var args=[];if(arguments.length>1){for(var i=1;i<arguments.length;i++){args.push(arguments[i]);}}
var _828=this;dojo.lang.forEach(this._anims,function(anim){anim[_825](args);},_828);return this;}});dojo.lfx.Chain=function(_82a){dojo.lfx.IAnimation.call(this);this._anims=[];this._currAnim=-1;var _82b=arguments;if(_82b.length==1&&(dojo.lang.isArray(_82b[0])||dojo.lang.isArrayLike(_82b[0]))){_82b=_82b[0];}
var _82c=this;dojo.lang.forEach(_82b,function(anim,i,_82f){this._anims.push(anim);if(i<_82f.length-1){anim.connect("onEnd",dojo.lang.hitch(this,"_playNext"));}else{anim.connect("onEnd",dojo.lang.hitch(this,function(){this.fire("onEnd");}));}},this);};dojo.inherits(dojo.lfx.Chain,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Chain,{_currAnim:-1,play:function(_830,_831){if(!this._anims.length){return this;}
if(_831||!this._anims[this._currAnim]){this._currAnim=0;}
var _832=this._anims[this._currAnim];this.fire("beforeBegin");if(_830>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_831);}),_830);return this;}
if(_832){if(this._currAnim==0){this.fire("handler",["begin",this._currAnim]);this.fire("onBegin",[this._currAnim]);}
this.fire("onPlay",[this._currAnim]);_832.play(null,_831);}
return this;},pause:function(){if(this._anims[this._currAnim]){this._anims[this._currAnim].pause();this.fire("onPause",[this._currAnim]);}
return this;},playPause:function(){if(this._anims.length==0){return this;}
if(this._currAnim==-1){this._currAnim=0;}
var _833=this._anims[this._currAnim];if(_833){if(!_833._active||_833._paused){this.play();}else{this.pause();}}
return this;},stop:function(){var _834=this._anims[this._currAnim];if(_834){_834.stop();this.fire("onStop",[this._currAnim]);}
return _834;},_playNext:function(){if(this._currAnim==-1||this._anims.length==0){return this;}
this._currAnim++;if(this._anims[this._currAnim]){this._anims[this._currAnim].play(null,true);}
return this;}});dojo.lfx.combine=function(_835){var _836=arguments;if(dojo.lang.isArray(arguments[0])){_836=arguments[0];}
if(_836.length==1){return _836[0];}
return new dojo.lfx.Combine(_836);};dojo.lfx.chain=function(_837){var _838=arguments;if(dojo.lang.isArray(arguments[0])){_838=arguments[0];}
if(_838.length==1){return _838[0];}
return new dojo.lfx.Chain(_838);};dojo.html.getBackgroundColor=function(node){node=dojo.byId(node);var _83a;do{_83a=dojo.html.getStyle(node,"background-color");if(_83a.toLowerCase()=="rgba(0, 0, 0, 0)"){_83a="transparent";}
if(node==document.getElementsByTagName("body")[0]){node=null;break;}
node=node.parentNode;}while(node&&dojo.lang.inArray(["transparent",""],_83a));if(_83a=="transparent"){_83a=[255,255,255,0];}else{_83a=dojo.gfx.color.extractRGB(_83a);}
return _83a;};dojo.lfx.html._byId=function(_83b){if(!_83b){return [];}
if(dojo.lang.isArrayLike(_83b)){if(!_83b.alreadyChecked){var n=[];dojo.lang.forEach(_83b,function(node){n.push(dojo.byId(node));});n.alreadyChecked=true;return n;}else{return _83b;}}else{var n=[];n.push(dojo.byId(_83b));n.alreadyChecked=true;return n;}};dojo.lfx.html.propertyAnimation=function(_83e,_83f,_840,_841,_842){_83e=dojo.lfx.html._byId(_83e);var _843={"propertyMap":_83f,"nodes":_83e,"duration":_840,"easing":_841||dojo.lfx.easeDefault};var _844=function(args){if(args.nodes.length==1){var pm=args.propertyMap;if(!dojo.lang.isArray(args.propertyMap)){var parr=[];for(var _848 in pm){pm[_848].property=_848;parr.push(pm[_848]);}
pm=args.propertyMap=parr;}
dojo.lang.forEach(pm,function(prop){if(dj_undef("start",prop)){if(prop.property!="opacity"){prop.start=parseInt(dojo.html.getComputedStyle(args.nodes[0],prop.property));}else{prop.start=dojo.html.getOpacity(args.nodes[0]);}}
});}};var _84a=function(_84b){var _84c=[];dojo.lang.forEach(_84b,function(c){_84c.push(Math.round(c));});return _84c;};var _84e=function(n,_850){n=dojo.byId(n);if(!n||!n.style){return;}
for(var s in _850){try{if(s=="opacity"){dojo.html.setOpacity(n,_850[s]);}else{n.style[s]=_850[s];}}
catch(e){dojo.debug(e);}}
};var _852=function(_853){this._properties=_853;this.diffs=new Array(_853.length);dojo.lang.forEach(_853,function(prop,i){if(dojo.lang.isFunction(prop.start)){prop.start=prop.start(prop,i);}
if(dojo.lang.isFunction(prop.end)){prop.end=prop.end(prop,i);}
if(dojo.lang.isArray(prop.start)){this.diffs[i]=null;}else{if(prop.start instanceof dojo.gfx.color.Color){prop.startRgb=prop.start.toRgb();prop.endRgb=prop.end.toRgb();}else{this.diffs[i]=prop.end-prop.start;}}
},this);this.getValue=function(n){var ret={};dojo.lang.forEach(this._properties,function(prop,i){var _85a=null;if(dojo.lang.isArray(prop.start)){}else{if(prop.start instanceof dojo.gfx.color.Color){_85a=(prop.units||"rgb")+"(";for(var j=0;j<prop.startRgb.length;j++){_85a+=Math.round(((prop.endRgb[j]-prop.startRgb[j])*n)+prop.startRgb[j])+(j<prop.startRgb.length-1?",":"");}
_85a+=")";}else{_85a=((this.diffs[i])*n)+prop.start+(prop.property!="opacity"?prop.units||"px":"");}}
ret[dojo.html.toCamelCase(prop.property)]=_85a;},this);return ret;};};var anim=new dojo.lfx.Animation({beforeBegin:function(){_844(_843);anim.curve=new _852(_843.propertyMap);},onAnimate:function(_85d){dojo.lang.forEach(_843.nodes,function(node){_84e(node,_85d);});}},_843.duration,null,_843.easing);if(_842){for(var x in _842){if(dojo.lang.isFunction(_842[x])){anim.connect(x,anim,_842[x]);}}
}
return anim;};dojo.lfx.html._makeFadeable=function(_860){var _861=function(node){if(dojo.render.html.ie){if((node.style.zoom.length==0)&&(dojo.html.getStyle(node,"zoom")=="normal")){node.style.zoom="1";}
if((node.style.width.length==0)&&(dojo.html.getStyle(node,"width")=="auto")){node.style.width="auto";}}
};if(dojo.lang.isArrayLike(_860)){dojo.lang.forEach(_860,_861);}else{_861(_860);}};dojo.lfx.html.fade=function(_863,_864,_865,_866,_867){_863=dojo.lfx.html._byId(_863);var _868={property:"opacity"};if(!dj_undef("start",_864)){_868.start=_864.start;}else{_868.start=function(){return dojo.html.getOpacity(_863[0]);};}
if(!dj_undef("end",_864)){_868.end=_864.end;}else{dojo.raise("dojo.lfx.html.fade needs an end value");}
var anim=dojo.lfx.propertyAnimation(_863,[_868],_865,_866);anim.connect("beforeBegin",function(){dojo.lfx.html._makeFadeable(_863);});if(_867){anim.connect("onEnd",function(){_867(_863,anim);});}
return anim;};dojo.lfx.html.fadeIn=function(_86a,_86b,_86c,_86d){return dojo.lfx.html.fade(_86a,{end:1},_86b,_86c,_86d);};dojo.lfx.html.fadeOut=function(_86e,_86f,_870,_871){return dojo.lfx.html.fade(_86e,{end:0},_86f,_870,_871);};dojo.lfx.html.fadeShow=function(_872,_873,_874,_875){_872=dojo.lfx.html._byId(_872);dojo.lang.forEach(_872,function(node){dojo.html.setOpacity(node,0);});var anim=dojo.lfx.html.fadeIn(_872,_873,_874,_875);anim.connect("beforeBegin",function(){if(dojo.lang.isArrayLike(_872)){dojo.lang.forEach(_872,dojo.html.show);}else{dojo.html.show(_872);}});return anim;};dojo.lfx.html.fadeHide=function(_878,_879,_87a,_87b){var anim=dojo.lfx.html.fadeOut(_878,_879,_87a,function(){if(dojo.lang.isArrayLike(_878)){dojo.lang.forEach(_878,dojo.html.hide);}else{dojo.html.hide(_878);}
if(_87b){_87b(_878,anim);}});return anim;};dojo.lfx.html.wipeIn=function(_87d,_87e,_87f,_880){_87d=dojo.lfx.html._byId(_87d);var _881=[];dojo.lang.forEach(_87d,function(node){var _883={};with(node.style){visibility="hidden";display="";}
var _884=dojo.html.getBorderBox(node).height;with(node.style){visibility="";display="none";}
var anim=dojo.lfx.propertyAnimation(node,{"height":{start:1,end:function(){return _884;}}},_87e,_87f);anim.connect("beforeBegin",function(){_883.overflow=node.style.overflow;_883.height=node.style.height;with(node.style){overflow="hidden";_884="1px";}
dojo.html.show(node);});anim.connect("onEnd",function(){with(node.style){overflow=_883.overflow;_884=_883.height;}
if(_880){_880(node,anim);}});_881.push(anim);});return dojo.lfx.combine(_881);};dojo.lfx.html.wipeOut=function(_886,_887,_888,_889){_886=dojo.lfx.html._byId(_886);var _88a=[];dojo.lang.forEach(_886,function(node){var _88c={};var anim=dojo.lfx.propertyAnimation(node,{"height":{start:function(){return dojo.html.getContentBox(node).height;},end:1}},_887,_888,{"beforeBegin":function(){_88c.overflow=node.style.overflow;_88c.height=node.style.height;with(node.style){overflow="hidden";}
dojo.html.show(node);},"onEnd":function(){dojo.html.hide(node);with(node.style){overflow=_88c.overflow;height=_88c.height;}
if(_889){_889(node,anim);}}});_88a.push(anim);});return dojo.lfx.combine(_88a);};dojo.lfx.html.slideTo=function(_88e,_88f,_890,_891,_892){_88e=dojo.lfx.html._byId(_88e);var _893=[];var _894=dojo.html.getComputedStyle;dojo.lang.forEach(_88e,function(node){var top=null;var left=null;var init=(function(){var _899=node;return function(){var pos=_894(_899,"position");top=(pos=="absolute"?node.offsetTop:parseInt(_894(node,"top"))||0);left=(pos=="absolute"?node.offsetLeft:parseInt(_894(node,"left"))||0);if(!dojo.lang.inArray(["absolute","relative"],pos)){var ret=dojo.html.abs(_899,true);dojo.html.setStyleAttributes(_899,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");top=ret.y;left=ret.x;}};})();init();var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:(_88f.top||0)},"left":{start:left,end:(_88f.left||0)}},_890,_891,{"beforeBegin":init});if(_892){anim.connect("onEnd",function(){_892(_88e,anim);});}
_893.push(anim);});return dojo.lfx.combine(_893);};dojo.lfx.html.slideBy=function(_89d,_89e,_89f,_8a0,_8a1){_89d=dojo.lfx.html._byId(_89d);var _8a2=[];var _8a3=dojo.html.getComputedStyle;dojo.lang.forEach(_89d,function(node){var top=null;var left=null;var init=(function(){var _8a8=node;return function(){var pos=_8a3(_8a8,"position");top=(pos=="absolute"?node.offsetTop:parseInt(_8a3(node,"top"))||0);left=(pos=="absolute"?node.offsetLeft:parseInt(_8a3(node,"left"))||0);if(!dojo.lang.inArray(["absolute","relative"],pos)){var ret=dojo.html.abs(_8a8,true);dojo.html.setStyleAttributes(_8a8,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");top=ret.y;left=ret.x;}};})();init();var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:top+(_89e.top||0)},"left":{start:left,end:left+(_89e.left||0)}},_89f,_8a0).connect("beforeBegin",init);if(_8a1){anim.connect("onEnd",function(){_8a1(_89d,anim);});}
_8a2.push(anim);});return dojo.lfx.combine(_8a2);};dojo.lfx.html.explode=function(_8ac,_8ad,_8ae,_8af,_8b0){var h=dojo.html;_8ac=dojo.byId(_8ac);_8ad=dojo.byId(_8ad);var _8b2=h.toCoordinateObject(_8ac,true);var _8b3=document.createElement("div");h.copyStyle(_8b3,_8ad);if(_8ad.explodeClassName){_8b3.className=_8ad.explodeClassName;}
with(_8b3.style){position="absolute";display="none";var _8b4=h.getStyle(_8ac,"background-color");backgroundColor=_8b4?_8b4.toLowerCase():"transparent";backgroundColor=(backgroundColor=="transparent")?"rgb(221, 221, 221)":backgroundColor;}
dojo.body().appendChild(_8b3);with(_8ad.style){visibility="hidden";display="block";}
var _8b5=h.toCoordinateObject(_8ad,true);with(_8ad.style){display="none";visibility="visible";}
var _8b6={opacity:{start:0.5,end:1}};dojo.lang.forEach(["height","width","top","left"],function(type){_8b6[type]={start:_8b2[type],end:_8b5[type]};});var anim=new dojo.lfx.propertyAnimation(_8b3,_8b6,_8ae,_8af,{"beforeBegin":function(){h.setDisplay(_8b3,"block");},"onEnd":function(){h.setDisplay(_8ad,"block");_8b3.parentNode.removeChild(_8b3);}});if(_8b0){anim.connect("onEnd",function(){_8b0(_8ad,anim);});}
return anim;};dojo.lfx.html.implode=function(_8b9,end,_8bb,_8bc,_8bd){var h=dojo.html;_8b9=dojo.byId(_8b9);end=dojo.byId(end);var _8bf=dojo.html.toCoordinateObject(_8b9,true);var _8c0=dojo.html.toCoordinateObject(end,true);var _8c1=document.createElement("div");dojo.html.copyStyle(_8c1,_8b9);if(_8b9.explodeClassName){_8c1.className=_8b9.explodeClassName;}
dojo.html.setOpacity(_8c1,0.3);with(_8c1.style){position="absolute";display="none";backgroundColor=h.getStyle(_8b9,"background-color").toLowerCase();}
dojo.body().appendChild(_8c1);var _8c2={opacity:{start:1,end:0.5}};dojo.lang.forEach(["height","width","top","left"],function(type){_8c2[type]={start:_8bf[type],end:_8c0[type]};});var anim=new dojo.lfx.propertyAnimation(_8c1,_8c2,_8bb,_8bc,{"beforeBegin":function(){dojo.html.hide(_8b9);dojo.html.show(_8c1);},"onEnd":function(){_8c1.parentNode.removeChild(_8c1);}});if(_8bd){anim.connect("onEnd",function(){_8bd(_8b9,anim);});}
return anim;};dojo.lfx.html.highlight=function(_8c5,_8c6,_8c7,_8c8,_8c9){_8c5=dojo.lfx.html._byId(_8c5);var _8ca=[];dojo.lang.forEach(_8c5,function(node){var _8cc=dojo.html.getBackgroundColor(node);var bg=dojo.html.getStyle(node,"background-color").toLowerCase();var _8ce=dojo.html.getStyle(node,"background-image");var _8cf=(bg=="transparent"||bg=="rgba(0, 0, 0, 0)");while(_8cc.length>3){_8cc.pop();}
var rgb=new dojo.gfx.color.Color(_8c6);var _8d1=new dojo.gfx.color.Color(_8cc);var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:rgb,end:_8d1}},_8c7,_8c8,{"beforeBegin":function(){if(_8ce){node.style.backgroundImage="none";}
node.style.backgroundColor="rgb("+rgb.toRgb().join(",")+")";},"onEnd":function(){if(_8ce){node.style.backgroundImage=_8ce;}
if(_8cf){node.style.backgroundColor="transparent";}
if(_8c9){_8c9(node,anim);}}});_8ca.push(anim);});return dojo.lfx.combine(_8ca);};dojo.lfx.html.unhighlight=function(_8d3,_8d4,_8d5,_8d6,_8d7){_8d3=dojo.lfx.html._byId(_8d3);var _8d8=[];dojo.lang.forEach(_8d3,function(node){var _8da=new dojo.gfx.color.Color(dojo.html.getBackgroundColor(node));var rgb=new dojo.gfx.color.Color(_8d4);var _8dc=dojo.html.getStyle(node,"background-image");var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:_8da,end:rgb}},_8d5,_8d6,{"beforeBegin":function(){if(_8dc){node.style.backgroundImage="none";}
node.style.backgroundColor="rgb("+_8da.toRgb().join(",")+")";},"onEnd":function(){if(_8d7){_8d7(node,anim);}}});_8d8.push(anim);});return dojo.lfx.combine(_8d8);};dojo.lang.mixin(dojo.lfx,dojo.lfx.html);dojo.kwCompoundRequire({browser:["dojo.lfx.html"],dashboard:["dojo.lfx.html"]});dojo.lfx.toggler.plain=function(){this.stop=function(){};this.show=function(node,_8df,_8e0,_8e1){dojo.html.show(node);if(dojo.lang.isFunction(_8e1)){_8e1();}};this.hide=function(node,_8e3,_8e4,_8e5){dojo.html.hide(node);if(dojo.lang.isFunction(_8e5)){_8e5();}};};dojo.lfx.toggler.common={stop:function(){if(this.anim&&this.anim.status()!="stopped"){this.anim.stop();}},_act:function(_8e6,node,_8e8,_8e9,_8ea,_8eb){this.stop();this.anim=dojo.lfx[_8e6](node,_8e8,_8e9,_8ea).play();},show:function(node,_8ed,_8ee,_8ef,_8f0){this._act(this.show_action,node,_8ed,_8ee,_8ef,_8f0);},hide:function(node,_8f2,_8f3,_8f4,_8f5){this._act(this.hide_action,node,_8f2,_8f3,_8f4,_8f5);}};dojo.lfx.toggler.fade=function(){this.anim=null;this.show_action="fadeShow";this.hide_action="fadeHide";};dojo.extend(dojo.lfx.toggler.fade,dojo.lfx.toggler.common);dojo.lfx.toggler.wipe=function(){this.anim=null;this.show_action="wipeIn";this.hide_action="wipeOut";};dojo.extend(dojo.lfx.toggler.wipe,dojo.lfx.toggler.common);dojo.lfx.toggler.explode=function(){this.anim=null;this.show_action="explode";this.hide_action="implode";this.show=function(node,_8f7,_8f8,_8f9,_8fa){this.stop();this.anim=dojo.lfx.explode(_8fa||{x:0,y:0,width:0,height:0},node,_8f7,_8f8,_8f9).play();};this.hide=function(node,_8fc,_8fd,_8fe,_8ff){this.stop();this.anim=dojo.lfx.implode(node,_8ff||{x:0,y:0,width:0,height:0},_8fc,_8fd,_8fe).play();};};dojo.extend(dojo.lfx.toggler.explode,dojo.lfx.toggler.common);dojo.declare("dojo.widget.HtmlWidget",dojo.widget.DomWidget,{templateCssPath:null,templatePath:null,lang:"",toggle:"plain",toggleDuration:150,initialize:function(args,frag){},postMixInProperties:function(args,frag){if(this.lang===""){this.lang=null;}
this.toggleObj=new (dojo.lfx.toggler[this.toggle.toLowerCase()]||dojo.lfx.toggler.plain);},createNodesFromText:function(txt,wrap){return dojo.html.createNodesFromText(txt,wrap);},destroyRendering:function(_906){try{if(this.bgIframe){this.bgIframe.remove();delete this.bgIframe;}
if(!_906&&this.domNode){dojo.event.browser.clean(this.domNode);}
dojo.widget.HtmlWidget.superclass.destroyRendering.call(this);}
catch(e){}},isShowing:function(){return dojo.html.isShowing(this.domNode);},toggleShowing:function(){if(this.isShowing()){this.hide();}else{this.show();}},show:function(){if(this.isShowing()){return;}
this.animationInProgress=true;this.toggleObj.show(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onShow),this.explodeSrc);},onShow:function(){this.animationInProgress=false;this.checkSize();},hide:function(){if(!this.isShowing()){return;}
this.animationInProgress=true;this.toggleObj.hide(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onHide),this.explodeSrc);},onHide:function(){this.animationInProgress=false;},_isResized:function(w,h){if(!this.isShowing()){return false;}
var wh=dojo.html.getMarginBox(this.domNode);var _90a=w||wh.width;var _90b=h||wh.height;if(this.width==_90a&&this.height==_90b){return false;}
this.width=_90a;this.height=_90b;return true;},checkSize:function(){if(!this._isResized()){return;}
this.onResized();},resizeTo:function(w,h){dojo.html.setMarginBox(this.domNode,{width:w,height:h});if(this.isShowing()){this.onResized();}},resizeSoon:function(){if(this.isShowing()){dojo.lang.setTimeout(this,this.onResized,0);}},onResized:function(){dojo.lang.forEach(this.children,function(_90e){if(_90e.checkSize){_90e.checkSize();}});}});dojo.kwCompoundRequire({common:["dojo.xml.Parse","dojo.widget.Widget","dojo.widget.Parse","dojo.widget.Manager"],browser:["dojo.widget.DomWidget","dojo.widget.HtmlWidget"],dashboard:["dojo.widget.DomWidget","dojo.widget.HtmlWidget"],svg:["dojo.widget.SvgWidget"],rhino:["dojo.widget.SwtWidget"]});