
if(typeof dojo=="undefined"){var dj_global=this;var dj_currentContext=this;function dj_undef(_1,_2){return (typeof (_2||dj_currentContext)[_1]=="undefined");}
if(dj_undef("djConfig",this)){var djConfig={};}
if(dj_undef("dojo",this)){var dojo={};}
dojo.global=function(){return dj_currentContext;};dojo.locale=djConfig.locale;dojo.version={major:0,minor:0,patch:0,flag:"dev",revision:Number("$Rev: 6425 $".match(/[0-9]+/)[0]),toString:function(){with(dojo.version){return major+"."+minor+"."+patch+flag+" ("+revision+")";}}};dojo.evalProp=function(_3,_4,_5){if((!_4)||(!_3)){return undefined;}
if(!dj_undef(_3,_4)){return _4[_3];}
return (_5?(_4[_3]={}):undefined);};dojo.parseObjPath=function(_6,_7,_8){var _9=(_7||dojo.global());var _a=_6.split(".");var _b=_a.pop();for(var i=0,l=_a.length;i<l&&_9;i++){_9=dojo.evalProp(_a[i],_9,_8);}
return {obj:_9,prop:_b};};dojo.evalObjPath=function(_e,_f){if(typeof _e!="string"){return dojo.global();}
if(_e.indexOf(".")==-1){return dojo.evalProp(_e,dojo.global(),_f);}
var ref=dojo.parseObjPath(_e,dojo.global(),_f);if(ref){return dojo.evalProp(ref.prop,ref.obj,_f);}
return null;};dojo.errorToString=function(_11){if(!dj_undef("message",_11)){return _11.message;}else{if(!dj_undef("description",_11)){return _11.description;}else{return _11;}}
};dojo.raise=function(_12,_13){if(_13){_12=_12+": "+dojo.errorToString(_13);}else{_12=dojo.errorToString(_12);}
try{if(djConfig.isDebug){dojo.hostenv.println("FATAL exception raised: "+_12);}}
catch(e){}
throw _13||Error(_12);};dojo.debug=function(){};dojo.debugShallow=function(obj){};dojo.profile={start:function(){},end:function(){},stop:function(){},dump:function(){}};function dj_eval(_15){return dj_global.eval?dj_global.eval(_15):eval(_15);}
dojo.unimplemented=function(_16,_17){var _18="'"+_16+"' not implemented";if(_17!=null){_18+=" "+_17;}
dojo.raise(_18);};dojo.deprecated=function(_19,_1a,_1b){var _1c="DEPRECATED: "+_19;if(_1a){_1c+=" "+_1a;}
if(_1b){_1c+=" -- will be removed in version: "+_1b;}
dojo.debug(_1c);};dojo.render=(function(){function vscaffold(_1d,_1e){var tmp={capable:false,support:{builtin:false,plugin:false},prefixes:_1d};for(var i=0;i<_1e.length;i++){tmp[_1e[i]]=false;}
return tmp;}
return {name:"",ver:dojo.version,os:{win:false,linux:false,osx:false},html:vscaffold(["html"],["ie","opera","khtml","safari","moz"]),svg:vscaffold(["svg"],["corel","adobe","batik"]),vml:vscaffold(["vml"],["ie"]),swf:vscaffold(["Swf","Flash","Mm"],["mm"]),swt:vscaffold(["Swt"],["ibm"])};})();dojo.hostenv=(function(){var _21={isDebug:false,allowQueryConfig:false,baseScriptUri:"",baseRelativePath:"",libraryScriptUri:"",iePreventClobber:false,ieClobberMinimal:true,preventBackButtonFix:true,delayMozLoadingFix:false,searchIds:[],parseWidgets:true};if(typeof djConfig=="undefined"){djConfig=_21;}else{for(var _22 in _21){if(typeof djConfig[_22]=="undefined"){djConfig[_22]=_21[_22];}}
}
return {name_:"(unset)",version_:"(unset)",getName:function(){return this.name_;},getVersion:function(){return this.version_;},getText:function(uri){dojo.unimplemented("getText","uri="+uri);}};})();dojo.hostenv.getBaseScriptUri=function(){if(djConfig.baseScriptUri.length){return djConfig.baseScriptUri;}
var uri=new String(djConfig.libraryScriptUri||djConfig.baseRelativePath);if(!uri){dojo.raise("Nothing returned by getLibraryScriptUri(): "+uri);}
var _25=uri.lastIndexOf("/");djConfig.baseScriptUri=djConfig.baseRelativePath;return djConfig.baseScriptUri;};(function(){var _26={pkgFileName:"__package__",loading_modules_:{},loaded_modules_:{},addedToLoadingCount:[],removedFromLoadingCount:[],inFlightCount:0,modulePrefixes_:{dojo:{name:"dojo",value:"src"}},registerModulePath:function(_27,_28){this.modulePrefixes_[_27]={name:_27,value:_28};},moduleHasPrefix:function(_29){var mp=this.modulePrefixes_;return Boolean(mp[_29]&&mp[_29].value);},getModulePrefix:function(_2b){if(this.moduleHasPrefix(_2b)){return this.modulePrefixes_[_2b].value;}
return _2b;},getTextStack:[],loadUriStack:[],loadedUris:[],post_load_:false,modulesLoadedListeners:[],unloadListeners:[],loadNotifying:false};for(var _2c in _26){dojo.hostenv[_2c]=_26[_2c];}})();dojo.hostenv.loadPath=function(_2d,_2e,cb){var uri;if(_2d.charAt(0)=="/"||_2d.match(/^\w+:/)){uri=_2d;}else{uri=this.getBaseScriptUri()+_2d;}
if(djConfig.cacheBust&&dojo.render.html.capable){uri+="?"+String(djConfig.cacheBust).replace(/\W+/g,"");}
try{return !_2e?this.loadUri(uri,cb):this.loadUriAndCheck(uri,_2e,cb);}
catch(e){dojo.debug(e);return false;}};dojo.hostenv.loadUri=function(uri,cb){if(this.loadedUris[uri]){return true;}
var _33=this.getText(uri,null,true);if(!_33){return false;}
this.loadedUris[uri]=true;if(cb){_33="("+_33+")";}
var _34=dj_eval(_33);if(cb){cb(_34);}
return true;};dojo.hostenv.loadUriAndCheck=function(uri,_36,cb){var ok=true;try{ok=this.loadUri(uri,cb);}
catch(e){dojo.debug("failed loading ",uri," with error: ",e);}
return Boolean(ok&&this.findModule(_36,false));};dojo.loaded=function(){};dojo.unloaded=function(){};dojo.hostenv.loaded=function(){this.loadNotifying=true;this.post_load_=true;var mll=this.modulesLoadedListeners;for(var x=0;x<mll.length;x++){mll[x]();}
this.modulesLoadedListeners=[];this.loadNotifying=false;dojo.loaded();};dojo.hostenv.unloaded=function(){var mll=this.unloadListeners;while(mll.length){(mll.pop())();}
dojo.unloaded();};dojo.addOnLoad=function(obj,_3d){var dh=dojo.hostenv;if(arguments.length==1){dh.modulesLoadedListeners.push(obj);}else{if(arguments.length>1){dh.modulesLoadedListeners.push(function(){obj[_3d]();});}}
if(dh.post_load_&&dh.inFlightCount==0&&!dh.loadNotifying){dh.callLoaded();}};dojo.addOnUnload=function(obj,_40){var dh=dojo.hostenv;if(arguments.length==1){dh.unloadListeners.push(obj);}else{if(arguments.length>1){dh.unloadListeners.push(function(){obj[_40]();});}}
};dojo.hostenv.modulesLoaded=function(){if(this.post_load_){return;}
if(this.loadUriStack.length==0&&this.getTextStack.length==0){if(this.inFlightCount>0){dojo.debug("files still in flight!");return;}
dojo.hostenv.callLoaded();}};dojo.hostenv.callLoaded=function(){if(typeof setTimeout=="object"){setTimeout("dojo.hostenv.loaded();",0);}else{dojo.hostenv.loaded();}};dojo.hostenv.getModuleSymbols=function(_42){var _43=_42.split(".");for(var i=_43.length;i>0;i--){var _45=_43.slice(0,i).join(".");if((i==1)&&!this.moduleHasPrefix(_45)){_43[0]="../"+_43[0];}else{var _46=this.getModulePrefix(_45);if(_46!=_45){_43.splice(0,i,_46);break;}}
}
return _43;};dojo.hostenv._global_omit_module_check=false;dojo.hostenv.loadModule=function(_47,_48,_49){if(!_47){return;}
_49=this._global_omit_module_check||_49;var _4a=this.findModule(_47,false);if(_4a){return _4a;}
if(dj_undef(_47,this.loading_modules_)){this.addedToLoadingCount.push(_47);}
this.loading_modules_[_47]=1;var _4b=_47.replace(/\./g,"/")+".js";var _4c=_47.split(".");var _4d=this.getModuleSymbols(_47);var _4e=((_4d[0].charAt(0)!="/")&&!_4d[0].match(/^\w+:/));var _4f=_4d[_4d.length-1];var ok;if(_4f=="*"){_47=_4c.slice(0,-1).join(".");while(_4d.length){_4d.pop();_4d.push(this.pkgFileName);_4b=_4d.join("/")+".js";if(_4e&&_4b.charAt(0)=="/"){_4b=_4b.slice(1);}
ok=this.loadPath(_4b,!_49?_47:null);if(ok){break;}
_4d.pop();}}else{_4b=_4d.join("/")+".js";_47=_4c.join(".");var _51=!_49?_47:null;ok=this.loadPath(_4b,_51);if(!ok&&!_48){_4d.pop();while(_4d.length){_4b=_4d.join("/")+".js";ok=this.loadPath(_4b,_51);if(ok){break;}
_4d.pop();_4b=_4d.join("/")+"/"+this.pkgFileName+".js";if(_4e&&_4b.charAt(0)=="/"){_4b=_4b.slice(1);}
ok=this.loadPath(_4b,_51);if(ok){break;}}
}
if(!ok&&!_49){dojo.raise("Could not load '"+_47+"'; last tried '"+_4b+"'");}}
if(!_49&&!this["isXDomain"]){_4a=this.findModule(_47,false);if(!_4a){dojo.raise("symbol '"+_47+"' is not defined after loading '"+_4b+"'");}}
return _4a;};dojo.hostenv.startPackage=function(_52){var _53=String(_52);var _54=_53;var _55=_52.split(/\./);if(_55[_55.length-1]=="*"){_55.pop();_54=_55.join(".");}
var _56=dojo.evalObjPath(_54,true);this.loaded_modules_[_53]=_56;this.loaded_modules_[_54]=_56;return _56;};dojo.hostenv.findModule=function(_57,_58){var lmn=String(_57);if(this.loaded_modules_[lmn]){return this.loaded_modules_[lmn];}
if(_58){dojo.raise("no loaded module named '"+_57+"'");}
return null;};dojo.kwCompoundRequire=function(_5a){var _5b=_5a["common"]||[];var _5c=_5a[dojo.hostenv.name_]?_5b.concat(_5a[dojo.hostenv.name_]||[]):_5b.concat(_5a["default"]||[]);for(var x=0;x<_5c.length;x++){var _5e=_5c[x];if(_5e.constructor==Array){dojo.hostenv.loadModule.apply(dojo.hostenv,_5e);}else{dojo.hostenv.loadModule(_5e);}}
};dojo.require=function(_5f){dojo.hostenv.loadModule.apply(dojo.hostenv,arguments);};dojo.requireIf=function(_60,_61){var _62=arguments[0];if((_62===true)||(_62=="common")||(_62&&dojo.render[_62].capable)){var _63=[];for(var i=1;i<arguments.length;i++){_63.push(arguments[i]);}
dojo.require.apply(dojo,_63);}};dojo.requireAfterIf=dojo.requireIf;dojo.provide=function(_65){return dojo.hostenv.startPackage.apply(dojo.hostenv,arguments);};dojo.registerModulePath=function(_66,_67){return dojo.hostenv.registerModulePath(_66,_67);};dojo.exists=function(obj,_69){var p=_69.split(".");for(var i=0;i<p.length;i++){if(!obj[p[i]]){return false;}
obj=obj[p[i]];}
return true;};dojo.hostenv.normalizeLocale=function(_6c){var _6d=_6c?_6c.toLowerCase():dojo.locale;if(_6d=="root"){_6d="ROOT";}
return _6d;};dojo.hostenv.searchLocalePath=function(_6e,_6f,_70){_6e=dojo.hostenv.normalizeLocale(_6e);var _71=_6e.split("-");var _72=[];for(var i=_71.length;i>0;i--){_72.push(_71.slice(0,i).join("-"));}
_72.push(false);if(_6f){_72.reverse();}
for(var j=_72.length-1;j>=0;j--){var loc=_72[j]||"ROOT";var _76=_70(loc);if(_76){break;}}
};dojo.hostenv.localesGenerated=["ROOT","es-es","es","it-it","pt-br","de","fr-fr","zh-cn","pt","en-us","zh","fr","zh-tw","it","en-gb","xx","de-de","ko-kr","ja-jp","ko","en","ja"];dojo.hostenv.registerNlsPrefix=function(){dojo.registerModulePath("nls","nls");};dojo.hostenv.preloadLocalizations=function(){if(dojo.hostenv.localesGenerated){dojo.hostenv.registerNlsPrefix();function preload(_77){_77=dojo.hostenv.normalizeLocale(_77);dojo.hostenv.searchLocalePath(_77,true,function(loc){for(var i=0;i<dojo.hostenv.localesGenerated.length;i++){if(dojo.hostenv.localesGenerated[i]==loc){dojo["require"]("nls.dojo_"+loc);return true;}}
return false;});}
preload();var _7a=djConfig.extraLocale||[];for(var i=0;i<_7a.length;i++){preload(_7a[i]);}}
dojo.hostenv.preloadLocalizations=function(){};};dojo.requireLocalization=function(_7c,_7d,_7e,_7f){dojo.hostenv.preloadLocalizations();var _80=dojo.hostenv.normalizeLocale(_7e);var _81=[_7c,"nls",_7d].join(".");var _82="";if(_7f){var _83=_7f.split(",");for(var i=0;i<_83.length;i++){if(_80.indexOf(_83[i])==0){if(_83[i].length>_82.length){_82=_83[i];}}
}
if(!_82){_82="ROOT";}}
var _85=_7f?_82:_80;var _86=dojo.hostenv.findModule(_81);var _87=null;if(_86){if(djConfig.localizationComplete&&_86._built){return;}
var _88=_85.replace("-","_");var _89=_81+"."+_88;_87=dojo.hostenv.findModule(_89);}
if(!_87){_86=dojo.hostenv.startPackage(_81);var _8a=dojo.hostenv.getModuleSymbols(_7c);var _8b=_8a.concat("nls").join("/");var _8c;dojo.hostenv.searchLocalePath(_85,_7f,function(loc){var _8e=loc.replace("-","_");var _8f=_81+"."+_8e;var _90=false;if(!dojo.hostenv.findModule(_8f)){dojo.hostenv.startPackage(_8f);var _91=[_8b];if(loc!="ROOT"){_91.push(loc);}
_91.push(_7d);var _92=_91.join("/")+".js";_90=dojo.hostenv.loadPath(_92,null,function(_93){var _94=function(){};_94.prototype=_8c;_86[_8e]=new _94();for(var j in _93){_86[_8e][j]=_93[j];}});}else{_90=true;}
if(_90&&_86[_8e]){_8c=_86[_8e];}else{_86[_8e]=_8c;}
if(_7f){return true;}});}
if(_7f&&_80!=_82){_86[_80.replace("-","_")]=_86[_82.replace("-","_")];}};(function(){var _96=djConfig.extraLocale;if(_96){if(!_96 instanceof Array){_96=[_96];}
var req=dojo.requireLocalization;dojo.requireLocalization=function(m,b,_9a,_9b){req(m,b,_9a,_9b);if(_9a){return;}
for(var i=0;i<_96.length;i++){req(m,b,_96[i],_9b);}};}})();}
if(typeof window!="undefined"){(function(){if(djConfig.allowQueryConfig){var _9d=document.location.toString();var _9e=_9d.split("?",2);if(_9e.length>1){var _9f=_9e[1];var _a0=_9f.split("&");for(var x in _a0){var sp=_a0[x].split("=");if((sp[0].length>9)&&(sp[0].substr(0,9)=="djConfig.")){var opt=sp[0].substr(9);try{djConfig[opt]=eval(sp[1]);}
catch(e){djConfig[opt]=sp[1];}}
}}
}
if(((djConfig["baseScriptUri"]=="")||(djConfig["baseRelativePath"]==""))&&(document&&document.getElementsByTagName)){var _a4=document.getElementsByTagName("script");var _a5=/(__package__|dojo|bootstrap1)\.js([\?\.]|$)/i;for(var i=0;i<_a4.length;i++){var src=_a4[i].getAttribute("src");if(!src){continue;}
var m=src.match(_a5);if(m){var _a9=src.substring(0,m.index);if(src.indexOf("bootstrap1")>-1){_a9+="../";}
if(!this["djConfig"]){djConfig={};}
if(djConfig["baseScriptUri"]==""){djConfig["baseScriptUri"]=_a9;}
if(djConfig["baseRelativePath"]==""){djConfig["baseRelativePath"]=_a9;}
break;}}
}
var dr=dojo.render;var drh=dojo.render.html;var drs=dojo.render.svg;var dua=(drh.UA=navigator.userAgent);var dav=(drh.AV=navigator.appVersion);var t=true;var f=false;drh.capable=t;drh.support.builtin=t;dr.ver=parseFloat(drh.AV);dr.os.mac=dav.indexOf("Macintosh")>=0;dr.os.win=dav.indexOf("Windows")>=0;dr.os.linux=dav.indexOf("X11")>=0;drh.opera=dua.indexOf("Opera")>=0;drh.khtml=(dav.indexOf("Konqueror")>=0)||(dav.indexOf("Safari")>=0);drh.safari=dav.indexOf("Safari")>=0;var _b1=dua.indexOf("Gecko");drh.mozilla=drh.moz=(_b1>=0)&&(!drh.khtml);if(drh.mozilla){drh.geckoVersion=dua.substring(_b1+6,_b1+14);}
drh.ie=(document.all)&&(!drh.opera);drh.ie50=drh.ie&&dav.indexOf("MSIE 5.0")>=0;drh.ie55=drh.ie&&dav.indexOf("MSIE 5.5")>=0;drh.ie60=drh.ie&&dav.indexOf("MSIE 6.0")>=0;drh.ie70=drh.ie&&dav.indexOf("MSIE 7.0")>=0;var cm=document["compatMode"];drh.quirks=(cm=="BackCompat")||(cm=="QuirksMode")||drh.ie55||drh.ie50;dojo.locale=dojo.locale||(drh.ie?navigator.userLanguage:navigator.language).toLowerCase();dr.vml.capable=drh.ie;drs.capable=f;drs.support.plugin=f;drs.support.builtin=f;var _b3=window["document"];var tdi=_b3["implementation"];if((tdi)&&(tdi["hasFeature"])&&(tdi.hasFeature("org.w3c.dom.svg","1.0"))){drs.capable=t;drs.support.builtin=t;drs.support.plugin=f;}
if(drh.safari){var tmp=dua.split("AppleWebKit/")[1];var ver=parseFloat(tmp.split(" ")[0]);if(ver>=420){drs.capable=t;drs.support.builtin=t;drs.support.plugin=f;}}else{}})();dojo.hostenv.startPackage("dojo.hostenv");dojo.render.name=dojo.hostenv.name_="browser";dojo.hostenv.searchIds=[];dojo.hostenv._XMLHTTP_PROGIDS=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"];dojo.hostenv.getXmlhttpObject=function(){var _b7=null;var _b8=null;try{_b7=new XMLHttpRequest();}
catch(e){}
if(!_b7){for(var i=0;i<3;++i){var _ba=dojo.hostenv._XMLHTTP_PROGIDS[i];try{_b7=new ActiveXObject(_ba);}
catch(e){_b8=e;}
if(_b7){dojo.hostenv._XMLHTTP_PROGIDS=[_ba];break;}}
}
if(!_b7){return dojo.raise("XMLHTTP not available",_b8);}
return _b7;};dojo.hostenv._blockAsync=false;dojo.hostenv.getText=function(uri,_bc,_bd){if(!_bc){this._blockAsync=true;}
var _be=this.getXmlhttpObject();function isDocumentOk(_bf){var _c0=_bf["status"];return Boolean((!_c0)||((200<=_c0)&&(300>_c0))||(_c0==304));}
if(_bc){var _c1=this,_c2=null,gbl=dojo.global();var xhr=dojo.evalObjPath("dojo.io.XMLHTTPTransport");_be.onreadystatechange=function(){if(_c2){gbl.clearTimeout(_c2);_c2=null;}
if(_c1._blockAsync||(xhr&&xhr._blockAsync)){_c2=gbl.setTimeout(function(){_be.onreadystatechange.apply(this);},10);}else{if(4==_be.readyState){if(isDocumentOk(_be)){_bc(_be.responseText);}}
}};}
_be.open("GET",uri,_bc?true:false);try{_be.send(null);if(_bc){return null;}
if(!isDocumentOk(_be)){var err=Error("Unable to load "+uri+" status:"+_be.status);err.status=_be.status;err.responseText=_be.responseText;throw err;}}
catch(e){this._blockAsync=false;if((_bd)&&(!_bc)){return null;}else{throw e;}}
this._blockAsync=false;return _be.responseText;};dojo.hostenv.defaultDebugContainerId="dojoDebug";dojo.hostenv._println_buffer=[];dojo.hostenv._println_safe=false;dojo.hostenv.println=function(_c6){if(!dojo.hostenv._println_safe){dojo.hostenv._println_buffer.push(_c6);}else{try{var _c7=document.getElementById(djConfig.debugContainerId?djConfig.debugContainerId:dojo.hostenv.defaultDebugContainerId);if(!_c7){_c7=dojo.body();}
var div=document.createElement("div");div.appendChild(document.createTextNode(_c6));_c7.appendChild(div);}
catch(e){try{document.write("<div>"+_c6+"</div>");}
catch(e2){window.status=_c6;}}
}};dojo.addOnLoad(function(){dojo.hostenv._println_safe=true;while(dojo.hostenv._println_buffer.length>0){dojo.hostenv.println(dojo.hostenv._println_buffer.shift());}});function dj_addNodeEvtHdlr(_c9,_ca,fp){var _cc=_c9["on"+_ca]||function(){};_c9["on"+_ca]=function(){fp.apply(_c9,arguments);_cc.apply(_c9,arguments);};return true;}
function dj_load_init(e){var _ce=(e&&e.type)?e.type.toLowerCase():"load";if(arguments.callee.initialized||(_ce!="domcontentloaded"&&_ce!="load")){return;}
arguments.callee.initialized=true;if(typeof (_timer)!="undefined"){clearInterval(_timer);delete _timer;}
var _cf=function(){if(dojo.render.html.ie){dojo.hostenv.makeWidgets();}};if(dojo.hostenv.inFlightCount==0){_cf();dojo.hostenv.modulesLoaded();}else{dojo.hostenv.modulesLoadedListeners.unshift(_cf);}}
if(document.addEventListener){if(dojo.render.html.opera||(dojo.render.html.moz&&(djConfig["enableMozDomContentLoaded"]===true))){document.addEventListener("DOMContentLoaded",dj_load_init,null);}
window.addEventListener("load",dj_load_init,null);}
if(dojo.render.html.ie&&dojo.render.os.win){document.write("<scr"+"ipt defer src=\"//:\" "+"onreadystatechange=\"if(this.readyState=='complete'){dj_load_init();}\">"+"</scr"+"ipt>");}
if(/(WebKit|khtml)/i.test(navigator.userAgent)){var _timer=setInterval(function(){if(/loaded|complete/.test(document.readyState)){dj_load_init();}},10);}
if(dojo.render.html.ie){dj_addNodeEvtHdlr(window,"beforeunload",function(){dojo.hostenv._unloading=true;window.setTimeout(function(){dojo.hostenv._unloading=false;},0);});}
dj_addNodeEvtHdlr(window,"unload",function(){dojo.hostenv.unloaded();if((!dojo.render.html.ie)||(dojo.render.html.ie&&dojo.hostenv._unloading)){dojo.hostenv.unloaded();}});dojo.hostenv.makeWidgets=function(){var _d0=[];if(djConfig.searchIds&&djConfig.searchIds.length>0){_d0=_d0.concat(djConfig.searchIds);}
if(dojo.hostenv.searchIds&&dojo.hostenv.searchIds.length>0){_d0=_d0.concat(dojo.hostenv.searchIds);}
if((djConfig.parseWidgets)||(_d0.length>0)){if(dojo.evalObjPath("dojo.widget.Parse")){var _d1=new dojo.xml.Parse();if(_d0.length>0){for(var x=0;x<_d0.length;x++){var _d3=document.getElementById(_d0[x]);if(!_d3){continue;}
var _d4=_d1.parseElement(_d3,null,true);dojo.widget.getParser().createComponents(_d4);}}else{if(djConfig.parseWidgets){var _d4=_d1.parseElement(dojo.body(),null,true);dojo.widget.getParser().createComponents(_d4);}}
}}
};dojo.addOnLoad(function(){if(!dojo.render.html.ie){dojo.hostenv.makeWidgets();}});try{if(dojo.render.html.ie){document.namespaces.add("v","urn:schemas-microsoft-com:vml");document.createStyleSheet().addRule("v\\:*","behavior:url(#default#VML)");}}
catch(e){}
dojo.hostenv.writeIncludes=function(){};if(!dj_undef("document",this)){dj_currentDocument=this.document;}
dojo.doc=function(){return dj_currentDocument;};dojo.body=function(){return dojo.doc().body||dojo.doc().getElementsByTagName("body")[0];};dojo.byId=function(id,doc){if((id)&&((typeof id=="string")||(id instanceof String))){if(!doc){doc=dj_currentDocument;}
var ele=doc.getElementById(id);if(ele&&(ele.id!=id)&&doc.all){ele=null;eles=doc.all[id];if(eles){if(eles.length){for(var i=0;i<eles.length;i++){if(eles[i].id==id){ele=eles[i];break;}}
}else{ele=eles;}}
}
return ele;}
return id;};dojo.setContext=function(_d9,_da){dj_currentContext=_d9;dj_currentDocument=_da;};dojo._fireCallback=function(_db,_dc,_dd){if((_dc)&&((typeof _db=="string")||(_db instanceof String))){_db=_dc[_db];}
return (_dc?_db.apply(_dc,_dd||[]):_db());};dojo.withGlobal=function(_de,_df,_e0,_e1){var _e2;var _e3=dj_currentContext;var _e4=dj_currentDocument;try{dojo.setContext(_de,_de.document);_e2=dojo._fireCallback(_df,_e0,_e1);}
finally{dojo.setContext(_e3,_e4);}
return _e2;};dojo.withDoc=function(_e5,_e6,_e7,_e8){var _e9;var _ea=dj_currentDocument;try{dj_currentDocument=_e5;_e9=dojo._fireCallback(_e6,_e7,_e8);}
finally{dj_currentDocument=_ea;}
return _e9;};}
(function(){if(typeof dj_usingBootstrap!="undefined"){return;}
var _eb=false;var _ec=false;var _ed=false;if((typeof this["load"]=="function")&&((typeof this["Packages"]=="function")||(typeof this["Packages"]=="object"))){_eb=true;}else{if(typeof this["load"]=="function"){_ec=true;}else{if(window.widget){_ed=true;}}
}
var _ee=[];if((this["djConfig"])&&((djConfig["isDebug"])||(djConfig["debugAtAllCosts"]))){_ee.push("debug.js");}
if((this["djConfig"])&&(djConfig["debugAtAllCosts"])&&(!_eb)&&(!_ed)){_ee.push("browser_debug.js");}
var _ef=djConfig["baseScriptUri"];if((this["djConfig"])&&(djConfig["baseLoaderUri"])){_ef=djConfig["baseLoaderUri"];}
for(var x=0;x<_ee.length;x++){var _f1=_ef+"src/"+_ee[x];if(_eb||_ec){load(_f1);}else{try{document.write("<scr"+"ipt type='text/javascript' src='"+_f1+"'></scr"+"ipt>");}
catch(e){var _f2=document.createElement("script");_f2.src=_f1;document.getElementsByTagName("head")[0].appendChild(_f2);}}
}})();dojo.provide("dojo.lang.common");dojo.lang.inherits=function(_f3,_f4){if(!dojo.lang.isFunction(_f4)){dojo.raise("dojo.inherits: superclass argument ["+_f4+"] must be a function (subclass: ["+_f3+"']");}
_f3.prototype=new _f4();_f3.prototype.constructor=_f3;_f3.superclass=_f4.prototype;_f3["super"]=_f4.prototype;};dojo.lang._mixin=function(obj,_f6){var _f7={};for(var x in _f6){if((typeof _f7[x]=="undefined")||(_f7[x]!=_f6[x])){obj[x]=_f6[x];}}
if(dojo.render.html.ie&&(typeof (_f6["toString"])=="function")&&(_f6["toString"]!=obj["toString"])&&(_f6["toString"]!=_f7["toString"])){obj.toString=_f6.toString;}
return obj;};dojo.lang.mixin=function(obj,_fa){for(var i=1,l=arguments.length;i<l;i++){dojo.lang._mixin(obj,arguments[i]);}
return obj;};dojo.lang.extend=function(_fd,_fe){for(var i=1,l=arguments.length;i<l;i++){dojo.lang._mixin(_fd.prototype,arguments[i]);}
return _fd;};dojo.lang._delegate=function(obj,_102){function TMP(){}
TMP.prototype=obj;var tmp=new TMP();if(_102){dojo.lang.mixin(tmp,_102);}
return tmp;};dojo.inherits=dojo.lang.inherits;dojo.mixin=dojo.lang.mixin;dojo.extend=dojo.lang.extend;dojo.lang.find=function(_104,_105,_106,_107){var _108=dojo.lang.isString(_104);if(_108){_104=_104.split("");}
if(_107){var step=-1;var i=_104.length-1;var end=-1;}else{var step=1;var i=0;var end=_104.length;}
if(_106){while(i!=end){if(_104[i]===_105){return i;}
i+=step;}}else{while(i!=end){if(_104[i]==_105){return i;}
i+=step;}}
return -1;};dojo.lang.indexOf=dojo.lang.find;dojo.lang.findLast=function(_10c,_10d,_10e){return dojo.lang.find(_10c,_10d,_10e,true);};dojo.lang.lastIndexOf=dojo.lang.findLast;dojo.lang.inArray=function(_10f,_110){return dojo.lang.find(_10f,_110)>-1;};dojo.lang.isObject=function(it){if(typeof it=="undefined"){return false;}
return (typeof it=="object"||it===null||dojo.lang.isArray(it)||dojo.lang.isFunction(it));};dojo.lang.isArray=function(it){return (it&&it instanceof Array||typeof it=="array");};dojo.lang.isArrayLike=function(it){if((!it)||(dojo.lang.isUndefined(it))){return false;}
if(dojo.lang.isString(it)){return false;}
if(dojo.lang.isFunction(it)){return false;}
if(dojo.lang.isArray(it)){return true;}
if((it.tagName)&&(it.tagName.toLowerCase()=="form")){return false;}
if(dojo.lang.isNumber(it.length)&&isFinite(it.length)){return true;}
return false;};dojo.lang.isFunction=function(it){return (it instanceof Function||typeof it=="function");};(function(){if((dojo.render.html.capable)&&(dojo.render.html["safari"])){dojo.lang.isFunction=function(it){if((typeof (it)=="function")&&(it=="[object NodeList]")){return false;}
return (it instanceof Function||typeof it=="function");};}})();dojo.lang.isString=function(it){return (typeof it=="string"||it instanceof String);};dojo.lang.isAlien=function(it){if(!it){return false;}
return !dojo.lang.isFunction(it)&&/\{\s*\[native code\]\s*\}/.test(String(it));};dojo.lang.isBoolean=function(it){return (it instanceof Boolean||typeof it=="boolean");};dojo.lang.isNumber=function(it){return (it instanceof Number||typeof it=="number");};dojo.lang.isUndefined=function(it){return ((typeof (it)=="undefined")&&(it==undefined));};dojo.provide("dojo.lang.array");dojo.lang.mixin(dojo.lang,{has:function(obj,name){try{return typeof obj[name]!="undefined";}
catch(e){return false;}},isEmpty:function(obj){if(dojo.lang.isArrayLike(obj)||dojo.lang.isString(obj)){return obj.length===0;}else{if(dojo.lang.isObject(obj)){var tmp={};for(var x in obj){if(obj[x]&&(!tmp[x])){return false;}}
return true;}}
},map:function(arr,obj,_122){var _123=dojo.lang.isString(arr);if(_123){arr=arr.split("");}
if(dojo.lang.isFunction(obj)&&(!_122)){_122=obj;obj=dj_global;}else{if(dojo.lang.isFunction(obj)&&_122){var _124=obj;obj=_122;_122=_124;}}
if(Array.map){var _125=Array.map(arr,_122,obj);}else{var _125=[];for(var i=0;i<arr.length;++i){_125.push(_122.call(obj,arr[i]));}}
if(_123){return _125.join("");}else{return _125;}},reduce:function(arr,_128,obj,_12a){var _12b=_128;if(arguments.length==1){dojo.debug("dojo.lang.reduce called with too few arguments!");return false;}else{if(arguments.length==2){_12a=_128;_12b=arr.shift();}else{if(arguments.lenght==3){if(dojo.lang.isFunction(obj)){_12a=obj;obj=null;}}else{if(dojo.lang.isFunction(obj)){var tmp=_12a;_12a=obj;obj=tmp;}}
}}
var ob=obj?obj:dj_global;dojo.lang.map(arr,function(val){_12b=_12a.call(ob,_12b,val);});return _12b;},forEach:function(_12f,_130,_131){if(dojo.lang.isString(_12f)){_12f=_12f.split("");}
if(Array.forEach){Array.forEach(_12f,_130,_131);}else{if(!_131){_131=dj_global;}
for(var i=0,l=_12f.length;i<l;i++){_130.call(_131,_12f[i],i,_12f);}}
},_everyOrSome:function(_134,arr,_136,_137){if(dojo.lang.isString(arr)){arr=arr.split("");}
if(Array.every){return Array[_134?"every":"some"](arr,_136,_137);}else{if(!_137){_137=dj_global;}
for(var i=0,l=arr.length;i<l;i++){var _13a=_136.call(_137,arr[i],i,arr);if(_134&&!_13a){return false;}else{if((!_134)&&(_13a)){return true;}}
}
return Boolean(_134);}},every:function(arr,_13c,_13d){return this._everyOrSome(true,arr,_13c,_13d);},some:function(arr,_13f,_140){return this._everyOrSome(false,arr,_13f,_140);},filter:function(arr,_142,_143){var _144=dojo.lang.isString(arr);if(_144){arr=arr.split("");}
var _145;if(Array.filter){_145=Array.filter(arr,_142,_143);}else{if(!_143){if(arguments.length>=3){dojo.raise("thisObject doesn't exist!");}
_143=dj_global;}
_145=[];for(var i=0;i<arr.length;i++){if(_142.call(_143,arr[i],i,arr)){_145.push(arr[i]);}}
}
if(_144){return _145.join("");}else{return _145;}},unnest:function(){var out=[];for(var i=0;i<arguments.length;i++){if(dojo.lang.isArrayLike(arguments[i])){var add=dojo.lang.unnest.apply(this,arguments[i]);out=out.concat(add);}else{out.push(arguments[i]);}}
return out;},toArray:function(_14a,_14b){var _14c=[];for(var i=_14b||0;i<_14a.length;i++){_14c.push(_14a[i]);}
return _14c;}});dojo.provide("dojo.lang.extras");dojo.lang.setTimeout=function(func,_14f){var _150=window,_151=2;if(!dojo.lang.isFunction(func)){_150=func;func=_14f;_14f=arguments[2];_151++;}
if(dojo.lang.isString(func)){func=_150[func];}
var args=[];for(var i=_151;i<arguments.length;i++){args.push(arguments[i]);}
return dojo.global().setTimeout(function(){func.apply(_150,args);},_14f);};dojo.lang.clearTimeout=function(_154){dojo.global().clearTimeout(_154);};dojo.lang.getNameInObj=function(ns,item){if(!ns){ns=dj_global;}
for(var x in ns){if(ns[x]===item){return new String(x);}}
return null;};dojo.lang.shallowCopy=function(obj,deep){var i,ret;if(obj===null){return null;}
if(dojo.lang.isObject(obj)){ret=new obj.constructor();for(i in obj){if(dojo.lang.isUndefined(ret[i])){ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];}}
}else{if(dojo.lang.isArray(obj)){ret=[];for(i=0;i<obj.length;i++){ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];}}else{ret=obj;}}
return ret;};dojo.lang.firstValued=function(){for(var i=0;i<arguments.length;i++){if(typeof arguments[i]!="undefined"){return arguments[i];}}
return undefined;};dojo.lang.getObjPathValue=function(_15d,_15e,_15f){with(dojo.parseObjPath(_15d,_15e,_15f)){return dojo.evalProp(prop,obj,_15f);}};dojo.lang.setObjPathValue=function(_160,_161,_162,_163){dojo.deprecated("dojo.lang.setObjPathValue","use dojo.parseObjPath and the '=' operator","0.6");if(arguments.length<4){_163=true;}
with(dojo.parseObjPath(_160,_162,_163)){if(obj&&(_163||(prop in obj))){obj[prop]=_161;}}
};dojo.provide("dojo.lang.func");dojo.lang.hitch=function(_164,_165){var args=[];for(var x=2;x<arguments.length;x++){args.push(arguments[x]);}
var fcn=(dojo.lang.isString(_165)?_164[_165]:_165)||function(){};return function(){var ta=args.concat([]);for(var x=0;x<arguments.length;x++){ta.push(arguments[x]);}
return fcn.apply(_164,ta);};};dojo.lang.anonCtr=0;dojo.lang.anon={};dojo.lang.nameAnonFunc=function(_16b,_16c,_16d){var nso=(_16c||dojo.lang.anon);if((_16d)||((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true))){for(var x in nso){try{if(nso[x]===_16b){return x;}}
catch(e){}}
}
var ret="__"+dojo.lang.anonCtr++;while(typeof nso[ret]!="undefined"){ret="__"+dojo.lang.anonCtr++;}
nso[ret]=_16b;return ret;};dojo.lang.forward=function(_171){return function(){return this[_171].apply(this,arguments);};};dojo.lang.curry=function(_172,func){var _174=[];_172=_172||dj_global;if(dojo.lang.isString(func)){func=_172[func];}
for(var x=2;x<arguments.length;x++){_174.push(arguments[x]);}
var _176=(func["__preJoinArity"]||func.length)-_174.length;function gather(_177,_178,_179){var _17a=_179;var _17b=_178.slice(0);for(var x=0;x<_177.length;x++){_17b.push(_177[x]);}
_179=_179-_177.length;if(_179<=0){var res=func.apply(_172,_17b);_179=_17a;return res;}else{return function(){return gather(arguments,_17b,_179);};}}
return gather([],_174,_176);};dojo.lang.curryArguments=function(_17e,func,args,_181){var _182=[];var x=_181||0;for(x=_181;x<args.length;x++){_182.push(args[x]);}
return dojo.lang.curry.apply(dojo.lang,[_17e,func].concat(_182));};dojo.lang.tryThese=function(){for(var x=0;x<arguments.length;x++){try{if(typeof arguments[x]=="function"){var ret=(arguments[x]());if(ret){return ret;}}
}
catch(e){dojo.debug(e);}}
};dojo.lang.delayThese=function(farr,cb,_188,_189){if(!farr.length){if(typeof _189=="function"){_189();}
return;}
if((typeof _188=="undefined")&&(typeof cb=="number")){_188=cb;cb=function(){};}else{if(!cb){cb=function(){};if(!_188){_188=0;}}
}
setTimeout(function(){(farr.shift())();cb();dojo.lang.delayThese(farr,cb,_188,_189);},_188);};dojo.provide("dojo.event.common");dojo.event=new function(){this._canTimeout=dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);function interpolateArgs(args,_18b){var dl=dojo.lang;var ao={srcObj:dj_global,srcFunc:null,adviceObj:dj_global,adviceFunc:null,aroundObj:null,aroundFunc:null,adviceType:(args.length>2)?args[0]:"after",precedence:"last",once:false,delay:null,rate:0,adviceMsg:false,maxCalls:-1};switch(args.length){case 0:
return;case 1:
return;case 2:
ao.srcFunc=args[0];ao.adviceFunc=args[1];break;case 3:
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isString(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];ao.adviceFunc=args[2];}else{if((dl.isString(args[1]))&&(dl.isString(args[2]))){ao.srcFunc=args[1];ao.adviceFunc=args[2];}else{if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isFunction(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];var _18e=dl.nameAnonFunc(args[2],ao.adviceObj,_18b);ao.adviceFunc=_18e;}else{if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){ao.adviceType="after";ao.srcObj=dj_global;var _18e=dl.nameAnonFunc(args[0],ao.srcObj,_18b);ao.srcFunc=_18e;ao.adviceObj=args[1];ao.adviceFunc=args[2];}}
}}
break;case 4:
if((dl.isObject(args[0]))&&(dl.isObject(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isString(args[1]))&&(dl.isObject(args[2]))){ao.adviceType=args[0];ao.srcObj=dj_global;ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isFunction(args[1]))&&(dl.isObject(args[2]))){ao.adviceType=args[0];ao.srcObj=dj_global;var _18e=dl.nameAnonFunc(args[1],dj_global,_18b);ao.srcFunc=_18e;ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){ao.srcObj=args[1];ao.srcFunc=args[2];var _18e=dl.nameAnonFunc(args[3],dj_global,_18b);ao.adviceObj=dj_global;ao.adviceFunc=_18e;}else{if(dl.isObject(args[1])){ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=dj_global;ao.adviceFunc=args[3];}else{if(dl.isObject(args[2])){ao.srcObj=dj_global;ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{ao.srcObj=ao.adviceObj=ao.aroundObj=dj_global;ao.srcFunc=args[1];ao.adviceFunc=args[2];ao.aroundFunc=args[3];}}
}}
}}
break;case 6:
ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=args[3];ao.adviceFunc=args[4];ao.aroundFunc=args[5];ao.aroundObj=dj_global;break;default:
ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=args[3];ao.adviceFunc=args[4];ao.aroundObj=args[5];ao.aroundFunc=args[6];ao.once=args[7];ao.delay=args[8];ao.rate=args[9];ao.adviceMsg=args[10];ao.maxCalls=(!isNaN(parseInt(args[11])))?args[11]:-1;break;}
if(dl.isFunction(ao.aroundFunc)){var _18e=dl.nameAnonFunc(ao.aroundFunc,ao.aroundObj,_18b);ao.aroundFunc=_18e;}
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
if(dojo.lang.isArray(ao.srcObj)&&ao.srcObj!=""){var _190={};for(var x in ao){_190[x]=ao[x];}
var mjps=[];dojo.lang.forEach(ao.srcObj,function(src){if((dojo.render.html.capable)&&(dojo.lang.isString(src))){src=dojo.byId(src);}
_190.srcObj=src;mjps.push(dojo.event.connect.call(dojo.event,_190));});return mjps;}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);if(ao.adviceFunc){var mjp2=dojo.event.MethodJoinPoint.getForMethod(ao.adviceObj,ao.adviceFunc);}
mjp.kwAddAdvice(ao);return mjp;};this.log=function(a1,a2){var _198;if((arguments.length==1)&&(typeof a1=="object")){_198=a1;}else{_198={srcObj:a1,srcFunc:a2};}
_198.adviceFunc=function(){var _199=[];for(var x=0;x<arguments.length;x++){_199.push(arguments[x]);}
dojo.debug("("+_198.srcObj+")."+_198.srcFunc,":",_199.join(", "));};this.kwConnect(_198);};this.connectBefore=function(){var args=["before"];for(var i=0;i<arguments.length;i++){args.push(arguments[i]);}
return this.connect.apply(this,args);};this.connectAround=function(){var args=["around"];for(var i=0;i<arguments.length;i++){args.push(arguments[i]);}
return this.connect.apply(this,args);};this.connectOnce=function(){var ao=interpolateArgs(arguments,true);ao.once=true;return this.connect(ao);};this.connectRunOnce=function(){var ao=interpolateArgs(arguments,true);ao.maxCalls=1;return this.connect(ao);};this._kwConnectImpl=function(_1a1,_1a2){var fn=(_1a2)?"disconnect":"connect";if(typeof _1a1["srcFunc"]=="function"){_1a1.srcObj=_1a1["srcObj"]||dj_global;var _1a4=dojo.lang.nameAnonFunc(_1a1.srcFunc,_1a1.srcObj,true);_1a1.srcFunc=_1a4;}
if(typeof _1a1["adviceFunc"]=="function"){_1a1.adviceObj=_1a1["adviceObj"]||dj_global;var _1a4=dojo.lang.nameAnonFunc(_1a1.adviceFunc,_1a1.adviceObj,true);_1a1.adviceFunc=_1a4;}
_1a1.srcObj=_1a1["srcObj"]||dj_global;_1a1.adviceObj=_1a1["adviceObj"]||_1a1["targetObj"]||dj_global;_1a1.adviceFunc=_1a1["adviceFunc"]||_1a1["targetFunc"];return dojo.event[fn](_1a1);};this.kwConnect=function(_1a5){return this._kwConnectImpl(_1a5,false);};this.disconnect=function(){if(arguments.length==1){var ao=arguments[0];}else{var ao=interpolateArgs(arguments,true);}
if(!ao.adviceFunc){return;}
if(dojo.lang.isString(ao.srcFunc)&&(ao.srcFunc.toLowerCase()=="onkey")){if(dojo.render.html.ie){ao.srcFunc="onkeydown";this.disconnect(ao);}
ao.srcFunc="onkeypress";}
if(!ao.srcObj[ao.srcFunc]){return null;}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc,true);mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);return mjp;};this.kwDisconnect=function(_1a8){return this._kwConnectImpl(_1a8,true);};};dojo.event.MethodInvocation=function(_1a9,obj,args){this.jp_=_1a9;this.object=obj;this.args=[];for(var x=0;x<args.length;x++){this.args[x]=args[x];}
this.around_index=-1;};dojo.event.MethodInvocation.prototype.proceed=function(){this.around_index++;if(this.around_index>=this.jp_.around.length){return this.jp_.object[this.jp_.methodname].apply(this.jp_.object,this.args);}else{var ti=this.jp_.around[this.around_index];var mobj=ti[0]||dj_global;var meth=ti[1];return mobj[meth].call(mobj,this);}};dojo.event.MethodJoinPoint=function(obj,_1b1){this.object=obj||dj_global;this.methodname=_1b1;this.methodfunc=this.object[_1b1];};dojo.event.MethodJoinPoint.getForMethod=function(obj,_1b3){if(!obj){obj=dj_global;}
var ofn=obj[_1b3];if(!ofn){ofn=obj[_1b3]=function(){};if(!obj[_1b3]){dojo.raise("Cannot set do-nothing method on that object "+_1b3);}}else{if((typeof ofn!="function")&&(!dojo.lang.isFunction(ofn))&&(!dojo.lang.isAlien(ofn))){return null;}}
var _1b5=_1b3+"$joinpoint";var _1b6=_1b3+"$joinpoint$method";var _1b7=obj[_1b5];if(!_1b7){var _1b8=false;if(dojo.event["browser"]){if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){_1b8=true;dojo.event.browser.addClobberNodeAttrs(obj,[_1b5,_1b6,_1b3]);}}
var _1b9=ofn.length;obj[_1b6]=ofn;_1b7=obj[_1b5]=new dojo.event.MethodJoinPoint(obj,_1b6);if(!_1b8){obj[_1b3]=function(){return _1b7.run.apply(_1b7,arguments);};}else{obj[_1b3]=function(){var args=[];if(!arguments.length){var evt=null;try{if(obj.ownerDocument){evt=obj.ownerDocument.parentWindow.event;}else{if(obj.documentElement){evt=obj.documentElement.ownerDocument.parentWindow.event;}else{if(obj.event){evt=obj.event;}else{evt=window.event;}}
}}
catch(e){evt=window.event;}
if(evt){args.push(dojo.event.browser.fixEvent(evt,this));}}else{for(var x=0;x<arguments.length;x++){if((x==0)&&(dojo.event.browser.isEvent(arguments[x]))){args.push(dojo.event.browser.fixEvent(arguments[x],this));}else{args.push(arguments[x]);}}
}
return _1b7.run.apply(_1b7,args);};}
obj[_1b3].__preJoinArity=_1b9;}
return _1b7;};dojo.lang.extend(dojo.event.MethodJoinPoint,{squelch:false,unintercept:function(){this.object[this.methodname]=this.methodfunc;this.before=[];this.after=[];this.around=[];},disconnect:dojo.lang.forward("unintercept"),run:function(){var obj=this.object||dj_global;var args=arguments;var _1bf=[];for(var x=0;x<args.length;x++){_1bf[x]=args[x];}
var _1c1=function(marr){if(!marr){dojo.debug("Null argument to unrollAdvice()");return;}
var _1c3=marr[0]||dj_global;var _1c4=marr[1];if(!_1c3[_1c4]){dojo.raise("function \""+_1c4+"\" does not exist on \""+_1c3+"\"");}
var _1c5=marr[2]||dj_global;var _1c6=marr[3];var msg=marr[6];var _1c8=marr[7];if(_1c8>-1){if(_1c8==0){return;}
marr[7]--;}
var _1c9;var to={args:[],jp_:this,object:obj,proceed:function(){return _1c3[_1c4].apply(_1c3,to.args);}};to.args=_1bf;var _1cb=parseInt(marr[4]);var _1cc=((!isNaN(_1cb))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));if(marr[5]){var rate=parseInt(marr[5]);var cur=new Date();var _1cf=false;if((marr["last"])&&((cur-marr.last)<=rate)){if(dojo.event._canTimeout){if(marr["delayTimer"]){clearTimeout(marr.delayTimer);}
var tod=parseInt(rate*2);var mcpy=dojo.lang.shallowCopy(marr);marr.delayTimer=setTimeout(function(){mcpy[5]=0;_1c1(mcpy);},tod);}
return;}else{marr.last=cur;}}
if(_1c6){_1c5[_1c6].call(_1c5,to);}else{if((_1cc)&&((dojo.render.html)||(dojo.render.svg))){dj_global["setTimeout"](function(){if(msg){_1c3[_1c4].call(_1c3,to);}else{_1c3[_1c4].apply(_1c3,args);}},_1cb);}else{if(msg){_1c3[_1c4].call(_1c3,to);}else{_1c3[_1c4].apply(_1c3,args);}}
}};var _1d2=function(){if(this.squelch){try{return _1c1.apply(this,arguments);}
catch(e){dojo.debug(e);}}else{return _1c1.apply(this,arguments);}};if((this["before"])&&(this.before.length>0)){dojo.lang.forEach(this.before.concat(new Array()),_1d2);}
var _1d3;try{if((this["around"])&&(this.around.length>0)){var mi=new dojo.event.MethodInvocation(this,obj,args);_1d3=mi.proceed();}else{if(this.methodfunc){_1d3=this.object[this.methodname].apply(this.object,args);}}
}
catch(e){if(!this.squelch){dojo.debug(e,"when calling",this.methodname,"on",this.object,"with arguments",args);dojo.raise(e);}}
if((this["after"])&&(this.after.length>0)){dojo.lang.forEach(this.after.concat(new Array()),_1d2);}
return (this.methodfunc)?_1d3:null;},getArr:function(kind){var type="after";if((typeof kind=="string")&&(kind.indexOf("before")!=-1)){type="before";}else{if(kind=="around"){type="around";}}
if(!this[type]){this[type]=[];}
return this[type];},kwAddAdvice:function(args){this.addAdvice(args["adviceObj"],args["adviceFunc"],args["aroundObj"],args["aroundFunc"],args["adviceType"],args["precedence"],args["once"],args["delay"],args["rate"],args["adviceMsg"],args["maxCalls"]);},addAdvice:function(_1d8,_1d9,_1da,_1db,_1dc,_1dd,once,_1df,rate,_1e1,_1e2){var arr=this.getArr(_1dc);if(!arr){dojo.raise("bad this: "+this);}
var ao=[_1d8,_1d9,_1da,_1db,_1df,rate,_1e1,_1e2];if(once){if(this.hasAdvice(_1d8,_1d9,_1dc,arr)>=0){return;}}
if(_1dd=="first"){arr.unshift(ao);}else{arr.push(ao);}},hasAdvice:function(_1e5,_1e6,_1e7,arr){if(!arr){arr=this.getArr(_1e7);}
var ind=-1;for(var x=0;x<arr.length;x++){var aao=(typeof _1e6=="object")?(new String(_1e6)).toString():_1e6;var a1o=(typeof arr[x][1]=="object")?(new String(arr[x][1])).toString():arr[x][1];if((arr[x][0]==_1e5)&&(a1o==aao)){ind=x;}}
return ind;},removeAdvice:function(_1ed,_1ee,_1ef,once){var arr=this.getArr(_1ef);var ind=this.hasAdvice(_1ed,_1ee,_1ef,arr);if(ind==-1){return false;}
while(ind!=-1){arr.splice(ind,1);if(once){break;}
ind=this.hasAdvice(_1ed,_1ee,_1ef,arr);}
return true;}});dojo.provide("dojo.event.browser");dojo._ie_clobber=new function(){this.clobberNodes=[];function nukeProp(node,prop){try{node[prop]=null;}
catch(e){}
try{delete node[prop];}
catch(e){}
try{node.removeAttribute(prop);}
catch(e){}}
this.clobber=function(_1f5){var na;var tna;if(_1f5){tna=_1f5.all||_1f5.getElementsByTagName("*");na=[_1f5];for(var x=0;x<tna.length;x++){if(tna[x]["__doClobber__"]){na.push(tna[x]);}}
}else{try{window.onload=null;}
catch(e){}
na=(this.clobberNodes.length)?this.clobberNodes:document.all;}
tna=null;var _1f9={};for(var i=na.length-1;i>=0;i=i-1){var el=na[i];try{if(el&&el["__clobberAttrs__"]){for(var j=0;j<el.__clobberAttrs__.length;j++){nukeProp(el,el.__clobberAttrs__[j]);}
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
dojo.event.browser=new function(){var _1fe=0;this.normalizedEventName=function(_1ff){switch(_1ff){case "CheckboxStateChange":
case "DOMAttrModified":
case "DOMMenuItemActive":
case "DOMMenuItemInactive":
case "DOMMouseScroll":
case "DOMNodeInserted":
case "DOMNodeRemoved":
case "RadioStateChange":
return _1ff;break;default:
return _1ff.toLowerCase();break;}};this.clean=function(node){if(dojo.render.html.ie){dojo._ie_clobber.clobber(node);}};this.addClobberNode=function(node){if(!dojo.render.html.ie){return;}
if(!node["__doClobber__"]){node.__doClobber__=true;dojo._ie_clobber.clobberNodes.push(node);node.__clobberAttrs__=[];}};this.addClobberNodeAttrs=function(node,_203){if(!dojo.render.html.ie){return;}
this.addClobberNode(node);for(var x=0;x<_203.length;x++){node.__clobberAttrs__.push(_203[x]);}};this.removeListener=function(node,_206,fp,_208){if(!_208){var _208=false;}
_206=dojo.event.browser.normalizedEventName(_206);if((_206=="onkey")||(_206=="key")){if(dojo.render.html.ie){this.removeListener(node,"onkeydown",fp,_208);}
_206="onkeypress";}
if(_206.substr(0,2)=="on"){_206=_206.substr(2);}
if(node.removeEventListener){node.removeEventListener(_206,fp,_208);}};this.addListener=function(node,_20a,fp,_20c,_20d){if(!node){return;}
if(!_20c){var _20c=false;}
_20a=dojo.event.browser.normalizedEventName(_20a);if((_20a=="onkey")||(_20a=="key")){if(dojo.render.html.ie){this.addListener(node,"onkeydown",fp,_20c,_20d);}
_20a="onkeypress";}
if(_20a.substr(0,2)!="on"){_20a="on"+_20a;}
if(!_20d){var _20e=function(evt){if(!evt){evt=window.event;}
var ret=fp(dojo.event.browser.fixEvent(evt,this));if(_20c){dojo.event.browser.stopEvent(evt);}
return ret;};}else{_20e=fp;}
if(node.addEventListener){node.addEventListener(_20a.substr(2),_20e,_20c);return _20e;}else{if(typeof node[_20a]=="function"){var _211=node[_20a];node[_20a]=function(e){_211(e);return _20e(e);};}else{node[_20a]=_20e;}
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
return evt;};this.stopEvent=function(evt){if(window.event){evt.cancelBubble=true;evt.returnValue=false;}else{evt.preventDefault();evt.stopPropagation();}};};dojo.provide("dojo.dom");dojo.dom.ELEMENT_NODE=1;dojo.dom.ATTRIBUTE_NODE=2;dojo.dom.TEXT_NODE=3;dojo.dom.CDATA_SECTION_NODE=4;dojo.dom.ENTITY_REFERENCE_NODE=5;dojo.dom.ENTITY_NODE=6;dojo.dom.PROCESSING_INSTRUCTION_NODE=7;dojo.dom.COMMENT_NODE=8;dojo.dom.DOCUMENT_NODE=9;dojo.dom.DOCUMENT_TYPE_NODE=10;dojo.dom.DOCUMENT_FRAGMENT_NODE=11;dojo.dom.NOTATION_NODE=12;dojo.dom.dojoml="http://www.dojotoolkit.org/2004/dojoml";dojo.dom.xmlns={svg:"http://www.w3.org/2000/svg",smil:"http://www.w3.org/2001/SMIL20/",mml:"http://www.w3.org/1998/Math/MathML",cml:"http://www.xml-cml.org",xlink:"http://www.w3.org/1999/xlink",xhtml:"http://www.w3.org/1999/xhtml",xul:"http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul",xbl:"http://www.mozilla.org/xbl",fo:"http://www.w3.org/1999/XSL/Format",xsl:"http://www.w3.org/1999/XSL/Transform",xslt:"http://www.w3.org/1999/XSL/Transform",xi:"http://www.w3.org/2001/XInclude",xforms:"http://www.w3.org/2002/01/xforms",saxon:"http://icl.com/saxon",xalan:"http://xml.apache.org/xslt",xsd:"http://www.w3.org/2001/XMLSchema",dt:"http://www.w3.org/2001/XMLSchema-datatypes",xsi:"http://www.w3.org/2001/XMLSchema-instance",rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#",rdfs:"http://www.w3.org/2000/01/rdf-schema#",dc:"http://purl.org/dc/elements/1.1/",dcq:"http://purl.org/dc/qualifiers/1.0","soap-env":"http://schemas.xmlsoap.org/soap/envelope/",wsdl:"http://schemas.xmlsoap.org/wsdl/",AdobeExtensions:"http://ns.adobe.com/AdobeSVGViewerExtensions/3.0/"};dojo.dom.isNode=function(wh){if(typeof Element=="function"){try{return wh instanceof Element;}
catch(e){}}else{return wh&&!isNaN(wh.nodeType);}};dojo.dom.getUniqueId=function(){var _21f=dojo.doc();do{var id="dj_unique_"+(++arguments.callee._idIncrement);}while(_21f.getElementById(id));return id;};dojo.dom.getUniqueId._idIncrement=0;dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_221,_222){var node=_221.firstChild;while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){node=node.nextSibling;}
if(_222&&node&&node.tagName&&node.tagName.toLowerCase()!=_222.toLowerCase()){node=dojo.dom.nextElement(node,_222);}
return node;};dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_224,_225){var node=_224.lastChild;while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){node=node.previousSibling;}
if(_225&&node&&node.tagName&&node.tagName.toLowerCase()!=_225.toLowerCase()){node=dojo.dom.prevElement(node,_225);}
return node;};dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(node,_228){if(!node){return null;}
do{node=node.nextSibling;}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);if(node&&_228&&_228.toLowerCase()!=node.tagName.toLowerCase()){return dojo.dom.nextElement(node,_228);}
return node;};dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(node,_22a){if(!node){return null;}
if(_22a){_22a=_22a.toLowerCase();}
do{node=node.previousSibling;}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);if(node&&_22a&&_22a.toLowerCase()!=node.tagName.toLowerCase()){return dojo.dom.prevElement(node,_22a);}
return node;};dojo.dom.moveChildren=function(_22b,_22c,trim){var _22e=0;if(trim){while(_22b.hasChildNodes()&&_22b.firstChild.nodeType==dojo.dom.TEXT_NODE){_22b.removeChild(_22b.firstChild);}
while(_22b.hasChildNodes()&&_22b.lastChild.nodeType==dojo.dom.TEXT_NODE){_22b.removeChild(_22b.lastChild);}}
while(_22b.hasChildNodes()){_22c.appendChild(_22b.firstChild);_22e++;}
return _22e;};dojo.dom.copyChildren=function(_22f,_230,trim){var _232=_22f.cloneNode(true);return this.moveChildren(_232,_230,trim);};dojo.dom.replaceChildren=function(node,_234){var _235=[];if(dojo.render.html.ie){for(var i=0;i<node.childNodes.length;i++){_235.push(node.childNodes[i]);}}
dojo.dom.removeChildren(node);node.appendChild(_234);for(var i=0;i<_235.length;i++){dojo.dom.destroyNode(_235[i]);}};dojo.dom.removeChildren=function(node){var _238=node.childNodes.length;while(node.hasChildNodes()){dojo.dom.removeNode(node.firstChild);}
return _238;};dojo.dom.replaceNode=function(node,_23a){return node.parentNode.replaceChild(_23a,node);};dojo.dom.destroyNode=function(node){if(node.parentNode){node=dojo.dom.removeNode(node);}
if(node.nodeType!=3){if(dojo.evalObjPath("dojo.event.browser.clean",false)){dojo.event.browser.clean(node);}
if(dojo.render.html.ie){node.outerHTML="";}}
};dojo.dom.removeNode=function(node){if(node&&node.parentNode){return node.parentNode.removeChild(node);}};dojo.dom.getAncestors=function(node,_23e,_23f){var _240=[];var _241=(_23e&&(_23e instanceof Function||typeof _23e=="function"));while(node){if(!_241||_23e(node)){_240.push(node);}
if(_23f&&_240.length>0){return _240[0];}
node=node.parentNode;}
if(_23f){return null;}
return _240;};dojo.dom.getAncestorsByTag=function(node,tag,_244){tag=tag.toLowerCase();return dojo.dom.getAncestors(node,function(el){return ((el.tagName)&&(el.tagName.toLowerCase()==tag));},_244);};dojo.dom.getFirstAncestorByTag=function(node,tag){return dojo.dom.getAncestorsByTag(node,tag,true);};dojo.dom.isDescendantOf=function(node,_249,_24a){if(_24a&&node){node=node.parentNode;}
while(node){if(node==_249){return true;}
node=node.parentNode;}
return false;};dojo.dom.innerXML=function(node){if(node.innerXML){return node.innerXML;}else{if(node.xml){return node.xml;}else{if(typeof XMLSerializer!="undefined"){return (new XMLSerializer()).serializeToString(node);}}
}};dojo.dom.createDocument=function(){var doc=null;var _24d=dojo.doc();if(!dj_undef("ActiveXObject")){var _24e=["MSXML2","Microsoft","MSXML","MSXML3"];for(var i=0;i<_24e.length;i++){try{doc=new ActiveXObject(_24e[i]+".XMLDOM");}
catch(e){}
if(doc){break;}}
}else{if((_24d.implementation)&&(_24d.implementation.createDocument)){doc=_24d.implementation.createDocument("","",null);}}
return doc;};dojo.dom.createDocumentFromText=function(str,_251){if(!_251){_251="text/xml";}
if(!dj_undef("DOMParser")){var _252=new DOMParser();return _252.parseFromString(str,_251);}else{if(!dj_undef("ActiveXObject")){var _253=dojo.dom.createDocument();if(_253){_253.async=false;_253.loadXML(str);return _253;}else{dojo.debug("toXml didn't work?");}}else{var _254=dojo.doc();if(_254.createElement){var tmp=_254.createElement("xml");tmp.innerHTML=str;if(_254.implementation&&_254.implementation.createDocument){var _256=_254.implementation.createDocument("foo","",null);for(var i=0;i<tmp.childNodes.length;i++){_256.importNode(tmp.childNodes.item(i),true);}
return _256;}
return ((tmp.document)&&(tmp.document.firstChild?tmp.document.firstChild:tmp));}}
}
return null;};dojo.dom.prependChild=function(node,_259){if(_259.firstChild){_259.insertBefore(node,_259.firstChild);}else{_259.appendChild(node);}
return true;};dojo.dom.insertBefore=function(node,ref,_25c){if((_25c!=true)&&(node===ref||node.nextSibling===ref)){return false;}
var _25d=ref.parentNode;_25d.insertBefore(node,ref);return true;};dojo.dom.insertAfter=function(node,ref,_260){var pn=ref.parentNode;if(ref==pn.lastChild){if((_260!=true)&&(node===ref)){return false;}
pn.appendChild(node);}else{return this.insertBefore(node,ref.nextSibling,_260);}
return true;};dojo.dom.insertAtPosition=function(node,ref,_264){if((!node)||(!ref)||(!_264)){return false;}
switch(_264.toLowerCase()){case "before":
return dojo.dom.insertBefore(node,ref);case "after":
return dojo.dom.insertAfter(node,ref);case "first":
if(ref.firstChild){return dojo.dom.insertBefore(node,ref.firstChild);}else{ref.appendChild(node);return true;}
break;default:
ref.appendChild(node);return true;}};dojo.dom.insertAtIndex=function(node,_266,_267){var _268=_266.childNodes;if(!_268.length||_268.length==_267){_266.appendChild(node);return true;}
if(_267==0){return dojo.dom.prependChild(node,_266);}
return dojo.dom.insertAfter(node,_268[_267-1]);};dojo.dom.textContent=function(node,text){if(arguments.length>1){var _26b=dojo.doc();dojo.dom.replaceChildren(node,_26b.createTextNode(text));return text;}else{if(node.textContent!=undefined){return node.textContent;}
var _26c="";if(node==null){return _26c;}
for(var i=0;i<node.childNodes.length;i++){switch(node.childNodes[i].nodeType){case 1:
case 5:
_26c+=dojo.dom.textContent(node.childNodes[i]);break;case 3:
case 2:
case 4:
_26c+=node.childNodes[i].nodeValue;break;default:
break;}}
return _26c;}};dojo.dom.hasParent=function(node){return Boolean(node&&node.parentNode&&dojo.dom.isNode(node.parentNode));};dojo.dom.isTag=function(node){if(node&&node.tagName){for(var i=1;i<arguments.length;i++){if(node.tagName==String(arguments[i])){return String(arguments[i]);}}
}
return "";};dojo.dom.setAttributeNS=function(elem,_272,_273,_274){if(elem==null||((elem==undefined)&&(typeof elem=="undefined"))){dojo.raise("No element given to dojo.dom.setAttributeNS");}
if(!((elem.setAttributeNS==undefined)&&(typeof elem.setAttributeNS=="undefined"))){elem.setAttributeNS(_272,_273,_274);}else{var _275=elem.ownerDocument;var _276=_275.createNode(2,_273,_272);_276.nodeValue=_274;elem.setAttributeNode(_276);}};dojo.provide("dojo.string.common");dojo.string.trim=function(str,wh){if(!str.replace){return str;}
if(!str.length){return str;}
var re=(wh>0)?(/^\s+/):(wh<0)?(/\s+$/):(/^\s+|\s+$/g);return str.replace(re,"");};dojo.string.trimStart=function(str){return dojo.string.trim(str,1);};dojo.string.trimEnd=function(str){return dojo.string.trim(str,-1);};dojo.string.repeat=function(str,_27d,_27e){var out="";for(var i=0;i<_27d;i++){out+=str;if(_27e&&i<_27d-1){out+=_27e;}}
return out;};dojo.string.pad=function(str,len,c,dir){var out=String(str);if(!c){c="0";}
if(!dir){dir=1;}
while(out.length<len){if(dir>0){out=c+out;}else{out+=c;}}
return out;};dojo.string.padLeft=function(str,len,c){return dojo.string.pad(str,len,c,1);};dojo.string.padRight=function(str,len,c){return dojo.string.pad(str,len,c,-1);};dojo.provide("dojo.string");dojo.provide("dojo.io.common");dojo.io.transports=[];dojo.io.hdlrFuncNames=["load","error","timeout"];dojo.io.Request=function(url,_28d,_28e,_28f){if((arguments.length==1)&&(arguments[0].constructor==Object)){this.fromKwArgs(arguments[0]);}else{this.url=url;if(_28d){this.mimetype=_28d;}
if(_28e){this.transport=_28e;}
if(arguments.length>=4){this.changeUrl=_28f;}}
};dojo.lang.extend(dojo.io.Request,{url:"",mimetype:"text/plain",method:"GET",content:undefined,transport:undefined,changeUrl:undefined,formNode:undefined,sync:false,bindSuccess:false,useCache:false,preventCache:false,load:function(type,data,_292,_293){},error:function(type,_295,_296,_297){},timeout:function(type,_299,_29a,_29b){},handle:function(type,data,_29e,_29f){},timeoutSeconds:0,abort:function(){},fromKwArgs:function(_2a0){if(_2a0["url"]){_2a0.url=_2a0.url.toString();}
if(_2a0["formNode"]){_2a0.formNode=dojo.byId(_2a0.formNode);}
if(!_2a0["method"]&&_2a0["formNode"]&&_2a0["formNode"].method){_2a0.method=_2a0["formNode"].method;}
if(!_2a0["handle"]&&_2a0["handler"]){_2a0.handle=_2a0.handler;}
if(!_2a0["load"]&&_2a0["loaded"]){_2a0.load=_2a0.loaded;}
if(!_2a0["changeUrl"]&&_2a0["changeURL"]){_2a0.changeUrl=_2a0.changeURL;}
_2a0.encoding=dojo.lang.firstValued(_2a0["encoding"],djConfig["bindEncoding"],"");_2a0.sendTransport=dojo.lang.firstValued(_2a0["sendTransport"],djConfig["ioSendTransport"],false);var _2a1=dojo.lang.isFunction;for(var x=0;x<dojo.io.hdlrFuncNames.length;x++){var fn=dojo.io.hdlrFuncNames[x];if(_2a0[fn]&&_2a1(_2a0[fn])){continue;}
if(_2a0["handle"]&&_2a1(_2a0["handle"])){_2a0[fn]=_2a0.handle;}}
dojo.lang.mixin(this,_2a0);}});dojo.io.Error=function(msg,type,num){this.message=msg;this.type=type||"unknown";this.number=num||0;};dojo.io.transports.addTransport=function(name){this.push(name);this[name]=dojo.io[name];};dojo.io.bind=function(_2a8){if(!(_2a8 instanceof dojo.io.Request)){try{_2a8=new dojo.io.Request(_2a8);}
catch(e){dojo.debug(e);}}
var _2a9="";if(_2a8["transport"]){_2a9=_2a8["transport"];if(!this[_2a9]){dojo.io.sendBindError(_2a8,"No dojo.io.bind() transport with name '"+_2a8["transport"]+"'.");return _2a8;}
if(!this[_2a9].canHandle(_2a8)){dojo.io.sendBindError(_2a8,"dojo.io.bind() transport with name '"+_2a8["transport"]+"' cannot handle this type of request.");return _2a8;}}else{for(var x=0;x<dojo.io.transports.length;x++){var tmp=dojo.io.transports[x];if((this[tmp])&&(this[tmp].canHandle(_2a8))){_2a9=tmp;break;}}
if(_2a9==""){dojo.io.sendBindError(_2a8,"None of the loaded transports for dojo.io.bind()"+" can handle the request.");return _2a8;}}
this[_2a9].bind(_2a8);_2a8.bindSuccess=true;return _2a8;};dojo.io.sendBindError=function(_2ac,_2ad){if((typeof _2ac.error=="function"||typeof _2ac.handle=="function")&&(typeof setTimeout=="function"||typeof setTimeout=="object")){var _2ae=new dojo.io.Error(_2ad);setTimeout(function(){_2ac[(typeof _2ac.error=="function")?"error":"handle"]("error",_2ae,null,_2ac);},50);}else{dojo.raise(_2ad);}};dojo.io.queueBind=function(_2af){if(!(_2af instanceof dojo.io.Request)){try{_2af=new dojo.io.Request(_2af);}
catch(e){dojo.debug(e);}}
var _2b0=_2af.load;_2af.load=function(){dojo.io._queueBindInFlight=false;var ret=_2b0.apply(this,arguments);dojo.io._dispatchNextQueueBind();return ret;};var _2b2=_2af.error;_2af.error=function(){dojo.io._queueBindInFlight=false;var ret=_2b2.apply(this,arguments);dojo.io._dispatchNextQueueBind();return ret;};dojo.io._bindQueue.push(_2af);dojo.io._dispatchNextQueueBind();return _2af;};dojo.io._dispatchNextQueueBind=function(){if(!dojo.io._queueBindInFlight){dojo.io._queueBindInFlight=true;if(dojo.io._bindQueue.length>0){dojo.io.bind(dojo.io._bindQueue.shift());}else{dojo.io._queueBindInFlight=false;}}
};dojo.io._bindQueue=[];dojo.io._queueBindInFlight=false;dojo.io.argsFromMap=function(map,_2b5,last){var enc=/utf/i.test(_2b5||"")?encodeURIComponent:dojo.string.encodeAscii;var _2b8=[];var _2b9=new Object();for(var name in map){var _2bb=function(elt){var val=enc(name)+"="+enc(elt);_2b8[(last==name)?"push":"unshift"](val);};if(!_2b9[name]){var _2be=map[name];if(dojo.lang.isArray(_2be)){dojo.lang.forEach(_2be,_2bb);}else{_2bb(_2be);}}
}
return _2b8.join("&");};dojo.io.setIFrameSrc=function(_2bf,src,_2c1){try{var r=dojo.render.html;if(!_2c1){if(r.safari){_2bf.location=src;}else{frames[_2bf.name].location=src;}}else{var idoc;if(r.ie){idoc=_2bf.contentWindow.document;}else{if(r.safari){idoc=_2bf.document;}else{idoc=_2bf.contentWindow;}}
if(!idoc){_2bf.location=src;return;}else{idoc.location.replace(src);}}
}
catch(e){dojo.debug(e);dojo.debug("setIFrameSrc: "+e);}};dojo.provide("dojo.string.extras");dojo.string.substituteParams=function(_2c4,hash){var map=(typeof hash=="object")?hash:dojo.lang.toArray(arguments,1);return _2c4.replace(/\%\{(\w+)\}/g,function(_2c7,key){if(typeof (map[key])!="undefined"&&map[key]!=null){return map[key];}
dojo.raise("Substitution not found: "+key);});};dojo.string.capitalize=function(str){if(!dojo.lang.isString(str)){return "";}
if(arguments.length==0){str=this;}
var _2ca=str.split(" ");for(var i=0;i<_2ca.length;i++){_2ca[i]=_2ca[i].charAt(0).toUpperCase()+_2ca[i].substring(1);}
return _2ca.join(" ");};dojo.string.isBlank=function(str){if(!dojo.lang.isString(str)){return true;}
return (dojo.string.trim(str).length==0);};dojo.string.encodeAscii=function(str){if(!dojo.lang.isString(str)){return str;}
var ret="";var _2cf=escape(str);var _2d0,re=/%u([0-9A-F]{4})/i;while((_2d0=_2cf.match(re))){var num=Number("0x"+_2d0[1]);var _2d3=escape("&#"+num+";");ret+=_2cf.substring(0,_2d0.index)+_2d3;_2cf=_2cf.substring(_2d0.index+_2d0[0].length);}
ret+=_2cf.replace(/\+/g,"%2B");return ret;};dojo.string.escape=function(type,str){var args=dojo.lang.toArray(arguments,1);switch(type.toLowerCase()){case "xml":
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
return str;}};dojo.string.escapeXml=function(str,_2d8){str=str.replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");if(!_2d8){str=str.replace(/'/gm,"&#39;");}
return str;};dojo.string.escapeSql=function(str){return str.replace(/'/gm,"''");};dojo.string.escapeRegExp=function(str){return str.replace(/\\/gm,"\\\\").replace(/([\f\b\n\t\r[\^$|?*+(){}])/gm,"\\$1");};dojo.string.escapeJavaScript=function(str){return str.replace(/(["'\f\b\n\t\r])/gm,"\\$1");};dojo.string.escapeString=function(str){return ("\""+str.replace(/(["\\])/g,"\\$1")+"\"").replace(/[\f]/g,"\\f").replace(/[\b]/g,"\\b").replace(/[\n]/g,"\\n").replace(/[\t]/g,"\\t").replace(/[\r]/g,"\\r");};dojo.string.summary=function(str,len){if(!len||str.length<=len){return str;}
return str.substring(0,len).replace(/\.+$/,"")+"...";};dojo.string.endsWith=function(str,end,_2e1){if(_2e1){str=str.toLowerCase();end=end.toLowerCase();}
if((str.length-end.length)<0){return false;}
return str.lastIndexOf(end)==str.length-end.length;};dojo.string.endsWithAny=function(str){for(var i=1;i<arguments.length;i++){if(dojo.string.endsWith(str,arguments[i])){return true;}}
return false;};dojo.string.startsWith=function(str,_2e5,_2e6){if(_2e6){str=str.toLowerCase();_2e5=_2e5.toLowerCase();}
return str.indexOf(_2e5)==0;};dojo.string.startsWithAny=function(str){for(var i=1;i<arguments.length;i++){if(dojo.string.startsWith(str,arguments[i])){return true;}}
return false;};dojo.string.has=function(str){for(var i=1;i<arguments.length;i++){if(str.indexOf(arguments[i])>-1){return true;}}
return false;};dojo.string.normalizeNewlines=function(text,_2ec){if(_2ec=="\n"){text=text.replace(/\r\n/g,"\n");text=text.replace(/\r/g,"\n");}else{if(_2ec=="\r"){text=text.replace(/\r\n/g,"\r");text=text.replace(/\n/g,"\r");}else{text=text.replace(/([^\r])\n/g,"$1\r\n").replace(/\r([^\n])/g,"\r\n$1");}}
return text;};dojo.string.splitEscaped=function(str,_2ee){var _2ef=[];for(var i=0,_2f1=0;i<str.length;i++){if(str.charAt(i)=="\\"){i++;continue;}
if(str.charAt(i)==_2ee){_2ef.push(str.substring(_2f1,i));_2f1=i+1;}}
_2ef.push(str.substr(_2f1));return _2ef;};dojo.provide("dojo.undo.browser");try{if((!djConfig["preventBackButtonFix"])&&(!dojo.hostenv.post_load_)){document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"'></iframe>");}}
catch(e){}
if(dojo.render.html.opera){dojo.debug("Opera is not supported with dojo.undo.browser, so back/forward detection will not work.");}
dojo.undo.browser={initialHref:(!dj_undef("window"))?window.location.href:"",initialHash:(!dj_undef("window"))?window.location.hash:"",moveForward:false,historyStack:[],forwardStack:[],historyIframe:null,bookmarkAnchor:null,locationTimer:null,setInitialState:function(args){this.initialState=this._createState(this.initialHref,args,this.initialHash);},addToHistory:function(args){this.forwardStack=[];var hash=null;var url=null;if(!this.historyIframe){this.historyIframe=window.frames["djhistory"];}
if(!this.bookmarkAnchor){this.bookmarkAnchor=document.createElement("a");dojo.body().appendChild(this.bookmarkAnchor);this.bookmarkAnchor.style.display="none";}
if(args["changeUrl"]){hash="#"+((args["changeUrl"]!==true)?args["changeUrl"]:(new Date()).getTime());if(this.historyStack.length==0&&this.initialState.urlHash==hash){this.initialState=this._createState(url,args,hash);return;}else{if(this.historyStack.length>0&&this.historyStack[this.historyStack.length-1].urlHash==hash){this.historyStack[this.historyStack.length-1]=this._createState(url,args,hash);return;}}
this.changingUrl=true;setTimeout("window.location.href = '"+hash+"'; dojo.undo.browser.changingUrl = false;",1);this.bookmarkAnchor.href=hash;if(dojo.render.html.ie){url=this._loadIframeHistory();var _2f6=args["back"]||args["backButton"]||args["handle"];var tcb=function(_2f8){if(window.location.hash!=""){setTimeout("window.location.href = '"+hash+"';",1);}
_2f6.apply(this,[_2f8]);};if(args["back"]){args.back=tcb;}else{if(args["backButton"]){args.backButton=tcb;}else{if(args["handle"]){args.handle=tcb;}}
}
var _2f9=args["forward"]||args["forwardButton"]||args["handle"];var tfw=function(_2fb){if(window.location.hash!=""){window.location.href=hash;}
if(_2f9){_2f9.apply(this,[_2fb]);}};if(args["forward"]){args.forward=tfw;}else{if(args["forwardButton"]){args.forwardButton=tfw;}else{if(args["handle"]){args.handle=tfw;}}
}}else{if(dojo.render.html.moz){if(!this.locationTimer){this.locationTimer=setInterval("dojo.undo.browser.checkLocation();",200);}}
}}else{url=this._loadIframeHistory();}
this.historyStack.push(this._createState(url,args,hash));},checkLocation:function(){if(!this.changingUrl){var hsl=this.historyStack.length;if((window.location.hash==this.initialHash||window.location.href==this.initialHref)&&(hsl==1)){this.handleBackButton();return;}
if(this.forwardStack.length>0){if(this.forwardStack[this.forwardStack.length-1].urlHash==window.location.hash){this.handleForwardButton();return;}}
if((hsl>=2)&&(this.historyStack[hsl-2])){if(this.historyStack[hsl-2].urlHash==window.location.hash){this.handleBackButton();return;}}
}},iframeLoaded:function(evt,_2fe){if(!dojo.render.html.opera){var _2ff=this._getUrlQuery(_2fe.href);if(_2ff==null){if(this.historyStack.length==1){this.handleBackButton();}
return;}
if(this.moveForward){this.moveForward=false;return;}
if(this.historyStack.length>=2&&_2ff==this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){this.handleBackButton();}else{if(this.forwardStack.length>0&&_2ff==this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){this.handleForwardButton();}}
}},handleBackButton:function(){var _300=this.historyStack.pop();if(!_300){return;}
var last=this.historyStack[this.historyStack.length-1];if(!last&&this.historyStack.length==0){last=this.initialState;}
if(last){if(last.kwArgs["back"]){last.kwArgs["back"]();}else{if(last.kwArgs["backButton"]){last.kwArgs["backButton"]();}else{if(last.kwArgs["handle"]){last.kwArgs.handle("back");}}
}}
this.forwardStack.push(_300);},handleForwardButton:function(){var last=this.forwardStack.pop();if(!last){return;}
if(last.kwArgs["forward"]){last.kwArgs.forward();}else{if(last.kwArgs["forwardButton"]){last.kwArgs.forwardButton();}else{if(last.kwArgs["handle"]){last.kwArgs.handle("forward");}}
}
this.historyStack.push(last);},_createState:function(url,args,hash){return {"url":url,"kwArgs":args,"urlHash":hash};},_getUrlQuery:function(url){var _307=url.split("?");if(_307.length<2){return null;}else{return _307[1];}},_loadIframeHistory:function(){var url=dojo.hostenv.getBaseScriptUri()+"iframe_history.html?"+(new Date()).getTime();this.moveForward=true;dojo.io.setIFrameSrc(this.historyIframe,url,false);return url;}};dojo.provide("dojo.io.BrowserIO");if(!dj_undef("window")){dojo.io.checkChildrenForFile=function(node){var _30a=false;var _30b=node.getElementsByTagName("input");dojo.lang.forEach(_30b,function(_30c){if(_30a){return;}
if(_30c.getAttribute("type")=="file"){_30a=true;}});return _30a;};dojo.io.formHasFile=function(_30d){return dojo.io.checkChildrenForFile(_30d);};dojo.io.updateNode=function(node,_30f){node=dojo.byId(node);var args=_30f;if(dojo.lang.isString(_30f)){args={url:_30f};}
args.mimetype="text/html";args.load=function(t,d,e){while(node.firstChild){dojo.dom.destroyNode(node.firstChild);}
node.innerHTML=d;};dojo.io.bind(args);};dojo.io.formFilter=function(node){var type=(node.type||"").toLowerCase();return !node.disabled&&node.name&&!dojo.lang.inArray(["file","submit","image","reset","button"],type);};dojo.io.encodeForm=function(_316,_317,_318){if((!_316)||(!_316.tagName)||(!_316.tagName.toLowerCase()=="form")){dojo.raise("Attempted to encode a non-form element.");}
if(!_318){_318=dojo.io.formFilter;}
var enc=/utf/i.test(_317||"")?encodeURIComponent:dojo.string.encodeAscii;var _31a=[];for(var i=0;i<_316.elements.length;i++){var elm=_316.elements[i];if(!elm||elm.tagName.toLowerCase()=="fieldset"||!_318(elm)){continue;}
var name=enc(elm.name);var type=elm.type.toLowerCase();if(type=="select-multiple"){for(var j=0;j<elm.options.length;j++){if(elm.options[j].selected){_31a.push(name+"="+enc(elm.options[j].value));}}
}else{if(dojo.lang.inArray(["radio","checkbox"],type)){if(elm.checked){_31a.push(name+"="+enc(elm.value));}}else{_31a.push(name+"="+enc(elm.value));}}
}
var _320=_316.getElementsByTagName("input");for(var i=0;i<_320.length;i++){var _321=_320[i];if(_321.type.toLowerCase()=="image"&&_321.form==_316&&_318(_321)){var name=enc(_321.name);_31a.push(name+"="+enc(_321.value));_31a.push(name+".x=0");_31a.push(name+".y=0");}}
return _31a.join("&")+"&";};dojo.io.FormBind=function(args){this.bindArgs={};if(args&&args.formNode){this.init(args);}else{if(args){this.init({formNode:args});}}
};dojo.lang.extend(dojo.io.FormBind,{form:null,bindArgs:null,clickedButton:null,init:function(args){var form=dojo.byId(args.formNode);if(!form||!form.tagName||form.tagName.toLowerCase()!="form"){throw new Error("FormBind: Couldn't apply, invalid form");}else{if(this.form==form){return;}else{if(this.form){throw new Error("FormBind: Already applied to a form");}}
}
dojo.lang.mixin(this.bindArgs,args);this.form=form;this.connect(form,"onsubmit","submit");for(var i=0;i<form.elements.length;i++){var node=form.elements[i];if(node&&node.type&&dojo.lang.inArray(["submit","button"],node.type.toLowerCase())){this.connect(node,"onclick","click");}}
var _327=form.getElementsByTagName("input");for(var i=0;i<_327.length;i++){var _328=_327[i];if(_328.type.toLowerCase()=="image"&&_328.form==form){this.connect(_328,"onclick","click");}}
},onSubmit:function(form){return true;},submit:function(e){e.preventDefault();if(this.onSubmit(this.form)){dojo.io.bind(dojo.lang.mixin(this.bindArgs,{formFilter:dojo.lang.hitch(this,"formFilter")}));}},click:function(e){var node=e.currentTarget;if(node.disabled){return;}
this.clickedButton=node;},formFilter:function(node){var type=(node.type||"").toLowerCase();var _32f=false;if(node.disabled||!node.name){_32f=false;}else{if(dojo.lang.inArray(["submit","button","image"],type)){if(!this.clickedButton){this.clickedButton=node;}
_32f=node==this.clickedButton;}else{_32f=!dojo.lang.inArray(["file","submit","reset","button"],type);}}
return _32f;},connect:function(_330,_331,_332){if(dojo.evalObjPath("dojo.event.connect")){dojo.event.connect(_330,_331,this,_332);}else{var fcn=dojo.lang.hitch(this,_332);_330[_331]=function(e){if(!e){e=window.event;}
if(!e.currentTarget){e.currentTarget=e.srcElement;}
if(!e.preventDefault){e.preventDefault=function(){window.event.returnValue=false;};}
fcn(e);};}}});dojo.io.XMLHTTPTransport=new function(){var _335=this;var _336={};this.useCache=false;this.preventCache=false;function getCacheKey(url,_338,_339){return url+"|"+_338+"|"+_339.toLowerCase();}
function addToCache(url,_33b,_33c,http){_336[getCacheKey(url,_33b,_33c)]=http;}
function getFromCache(url,_33f,_340){return _336[getCacheKey(url,_33f,_340)];}
this.clearCache=function(){_336={};};function doLoad(_341,http,url,_344,_345){if(((http.status>=200)&&(http.status<300))||(http.status==304)||(location.protocol=="file:"&&(http.status==0||http.status==undefined))||(location.protocol=="chrome:"&&(http.status==0||http.status==undefined))){var ret;if(_341.method.toLowerCase()=="head"){var _347=http.getAllResponseHeaders();ret={};ret.toString=function(){return _347;};var _348=_347.split(/[\r\n]+/g);for(var i=0;i<_348.length;i++){var pair=_348[i].match(/^([^:]+)\s*:\s*(.+)$/i);if(pair){ret[pair[1]]=pair[2];}}
}else{if(_341.mimetype=="text/javascript"){try{ret=dj_eval(http.responseText);}
catch(e){dojo.debug(e);dojo.debug(http.responseText);ret=null;}}else{if(_341.mimetype=="text/json"||_341.mimetype=="application/json"){try{ret=dj_eval("("+http.responseText+")");}
catch(e){dojo.debug(e);dojo.debug(http.responseText);ret=false;}}else{if((_341.mimetype=="application/xml")||(_341.mimetype=="text/xml")){ret=http.responseXML;if(!ret||typeof ret=="string"||!http.getResponseHeader("Content-Type")){ret=dojo.dom.createDocumentFromText(http.responseText);}}else{ret=http.responseText;}}
}}
if(_345){addToCache(url,_344,_341.method,http);}
_341[(typeof _341.load=="function")?"load":"handle"]("load",ret,http,_341);}else{var _34b=new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);_341[(typeof _341.error=="function")?"error":"handle"]("error",_34b,http,_341);}}
function setHeaders(http,_34d){if(_34d["headers"]){for(var _34e in _34d["headers"]){if(_34e.toLowerCase()=="content-type"&&!_34d["contentType"]){_34d["contentType"]=_34d["headers"][_34e];}else{http.setRequestHeader(_34e,_34d["headers"][_34e]);}}
}}
this.inFlight=[];this.inFlightTimer=null;this.startWatchingInFlight=function(){if(!this.inFlightTimer){this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);}};this.watchInFlight=function(){var now=null;if(!dojo.hostenv._blockAsync&&!_335._blockAsync){for(var x=this.inFlight.length-1;x>=0;x--){try{var tif=this.inFlight[x];if(!tif||tif.http._aborted||!tif.http.readyState){this.inFlight.splice(x,1);continue;}
if(4==tif.http.readyState){this.inFlight.splice(x,1);doLoad(tif.req,tif.http,tif.url,tif.query,tif.useCache);}else{if(tif.startTime){if(!now){now=(new Date()).getTime();}
if(tif.startTime+(tif.req.timeoutSeconds*1000)<now){if(typeof tif.http.abort=="function"){tif.http.abort();}
this.inFlight.splice(x,1);tif.req[(typeof tif.req.timeout=="function")?"timeout":"handle"]("timeout",null,tif.http,tif.req);}}
}}
catch(e){try{var _352=new dojo.io.Error("XMLHttpTransport.watchInFlight Error: "+e);tif.req[(typeof tif.req.error=="function")?"error":"handle"]("error",_352,tif.http,tif.req);}
catch(e2){dojo.debug("XMLHttpTransport error callback failed: "+e2);}}
}}
clearTimeout(this.inFlightTimer);if(this.inFlight.length==0){this.inFlightTimer=null;return;}
this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);};var _353=dojo.hostenv.getXmlhttpObject()?true:false;this.canHandle=function(_354){return _353&&dojo.lang.inArray(["text/plain","text/html","application/xml","text/xml","text/javascript","text/json","application/json"],(_354["mimetype"].toLowerCase()||""))&&!(_354["formNode"]&&dojo.io.formHasFile(_354["formNode"]));};this.multipartBoundary="45309FFF-BD65-4d50-99C9-36986896A96F";this.bind=function(_355){var url=_355.url;var _357="";if(_355["formNode"]){var ta=_355.formNode.getAttribute("action");if((ta)&&(!_355["url"])){url=ta;}
var tp=_355.formNode.getAttribute("method");if((tp)&&(!_355["method"])){_355.method=tp;}
_357+=dojo.io.encodeForm(_355.formNode,_355.encoding,_355["formFilter"]);}
if(url.indexOf("#")>-1){dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);url=url.split("#")[0];}
if(_355["file"]){_355.method="post";}
if(!_355["method"]){_355.method="get";}
if(_355.method.toLowerCase()=="get"){_355.multipart=false;}else{if(_355["file"]){_355.multipart=true;}else{if(!_355["multipart"]){_355.multipart=false;}}
}
if(_355["backButton"]||_355["back"]||_355["changeUrl"]){dojo.undo.browser.addToHistory(_355);}
var _35a=_355["content"]||{};if(_355.sendTransport){_35a["dojo.transport"]="xmlhttp";}
do{if(_355.postContent){_357=_355.postContent;break;}
if(_35a){_357+=dojo.io.argsFromMap(_35a,_355.encoding);}
if(_355.method.toLowerCase()=="get"||!_355.multipart){break;}
var t=[];if(_357.length){var q=_357.split("&");for(var i=0;i<q.length;++i){if(q[i].length){var p=q[i].split("=");t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+p[0]+"\"","",p[1]);}}
}
if(_355.file){if(dojo.lang.isArray(_355.file)){for(var i=0;i<_355.file.length;++i){var o=_355.file[i];t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);}}else{var o=_355.file;t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);}}
if(t.length){t.push("--"+this.multipartBoundary+"--","");_357=t.join("\r\n");}}while(false);var _360=_355["sync"]?false:true;var _361=_355["preventCache"]||(this.preventCache==true&&_355["preventCache"]!=false);var _362=_355["useCache"]==true||(this.useCache==true&&_355["useCache"]!=false);if(!_361&&_362){var _363=getFromCache(url,_357,_355.method);if(_363){doLoad(_355,_363,url,_357,false);return;}}
var http=dojo.hostenv.getXmlhttpObject(_355);var _365=false;if(_360){var _366=this.inFlight.push({"req":_355,"http":http,"url":url,"query":_357,"useCache":_362,"startTime":_355.timeoutSeconds?(new Date()).getTime():0});this.startWatchingInFlight();}else{_335._blockAsync=true;}
if(_355.method.toLowerCase()=="post"){if(!_355.user){http.open("POST",url,_360);}else{http.open("POST",url,_360,_355.user,_355.password);}
setHeaders(http,_355);http.setRequestHeader("Content-Type",_355.multipart?("multipart/form-data; boundary="+this.multipartBoundary):(_355.contentType||"application/x-www-form-urlencoded"));try{http.send(_357);}
catch(e){if(typeof http.abort=="function"){http.abort();}
doLoad(_355,{status:404},url,_357,_362);}}else{var _367=url;if(_357!=""){_367+=(_367.indexOf("?")>-1?"&":"?")+_357;}
if(_361){_367+=(dojo.string.endsWithAny(_367,"?","&")?"":(_367.indexOf("?")>-1?"&":"?"))+"dojo.preventCache="+new Date().valueOf();}
if(!_355.user){http.open(_355.method.toUpperCase(),_367,_360);}else{http.open(_355.method.toUpperCase(),_367,_360,_355.user,_355.password);}
setHeaders(http,_355);try{http.send(null);}
catch(e){if(typeof http.abort=="function"){http.abort();}
doLoad(_355,{status:404},url,_357,_362);}}
if(!_360){doLoad(_355,http,url,_357,_362);_335._blockAsync=false;}
_355.abort=function(){try{http._aborted=true;}
catch(e){}
return http.abort();};return;};dojo.io.transports.addTransport("XMLHTTPTransport");};}
dojo.provide("dojo.io.cookie");dojo.io.cookie.setCookie=function(name,_369,days,path,_36c,_36d){var _36e=-1;if((typeof days=="number")&&(days>=0)){var d=new Date();d.setTime(d.getTime()+(days*24*60*60*1000));_36e=d.toGMTString();}
_369=escape(_369);document.cookie=name+"="+_369+";"+(_36e!=-1?" expires="+_36e+";":"")+(path?"path="+path:"")+(_36c?"; domain="+_36c:"")+(_36d?"; secure":"");};dojo.io.cookie.set=dojo.io.cookie.setCookie;dojo.io.cookie.getCookie=function(name){var idx=document.cookie.lastIndexOf(name+"=");if(idx==-1){return null;}
var _372=document.cookie.substring(idx+name.length+1);var end=_372.indexOf(";");if(end==-1){end=_372.length;}
_372=_372.substring(0,end);_372=unescape(_372);return _372;};dojo.io.cookie.get=dojo.io.cookie.getCookie;dojo.io.cookie.deleteCookie=function(name){dojo.io.cookie.setCookie(name,"-",0);};dojo.io.cookie.setObjectCookie=function(name,obj,days,path,_379,_37a,_37b){if(arguments.length==5){_37b=_379;_379=null;_37a=null;}
var _37c=[],_37d,_37e="";if(!_37b){_37d=dojo.io.cookie.getObjectCookie(name);}
if(days>=0){if(!_37d){_37d={};}
for(var prop in obj){if(obj[prop]==null){delete _37d[prop];}else{if((typeof obj[prop]=="string")||(typeof obj[prop]=="number")){_37d[prop]=obj[prop];}}
}
prop=null;for(var prop in _37d){_37c.push(escape(prop)+"="+escape(_37d[prop]));}
_37e=_37c.join("&");}
dojo.io.cookie.setCookie(name,_37e,days,path,_379,_37a);};dojo.io.cookie.getObjectCookie=function(name){var _381=null,_382=dojo.io.cookie.getCookie(name);if(_382){_381={};var _383=_382.split("&");for(var i=0;i<_383.length;i++){var pair=_383[i].split("=");var _386=pair[1];if(isNaN(_386)){_386=unescape(pair[1]);}
_381[unescape(pair[0])]=_386;}}
return _381;};dojo.io.cookie.isSupported=function(){if(typeof navigator.cookieEnabled!="boolean"){dojo.io.cookie.setCookie("__TestingYourBrowserForCookieSupport__","CookiesAllowed",90,null);var _387=dojo.io.cookie.getCookie("__TestingYourBrowserForCookieSupport__");navigator.cookieEnabled=(_387=="CookiesAllowed");if(navigator.cookieEnabled){this.deleteCookie("__TestingYourBrowserForCookieSupport__");}}
return navigator.cookieEnabled;};if(!dojo.io.cookies){dojo.io.cookies=dojo.io.cookie;}
dojo.provide("dojo.date.common");dojo.date.setDayOfYear=function(_388,_389){_388.setMonth(0);_388.setDate(_389);return _388;};dojo.date.getDayOfYear=function(_38a){var _38b=_38a.getFullYear();var _38c=new Date(_38b-1,11,31);return Math.floor((_38a.getTime()-_38c.getTime())/86400000);};dojo.date.setWeekOfYear=function(_38d,week,_38f){if(arguments.length==2){_38f=0;}
dojo.unimplemented("dojo.date.setWeekOfYear");};dojo.date.getWeekOfYear=function(_390,_391){if(arguments.length==1){_391=0;}
var _392=new Date(_390.getFullYear(),0,1);var day=_392.getDay();_392.setDate(_392.getDate()-day+_391-(day>_391?7:0));return Math.floor((_390.getTime()-_392.getTime())/604800000);};dojo.date.setIsoWeekOfYear=function(_394,week,_396){if(arguments.length==2){_396=1;}
dojo.unimplemented("dojo.date.setIsoWeekOfYear");};dojo.date.getIsoWeekOfYear=function(_397,_398){if(arguments.length==1){_398=1;}
dojo.unimplemented("dojo.date.getIsoWeekOfYear");};dojo.date.shortTimezones=["IDLW","BET","HST","MART","AKST","PST","MST","CST","EST","AST","NFT","BST","FST","AT","GMT","CET","EET","MSK","IRT","GST","AFT","AGTT","IST","NPT","ALMT","MMT","JT","AWST","JST","ACST","AEST","LHST","VUT","NFT","NZT","CHAST","PHOT","LINT"];dojo.date.timezoneOffsets=[-720,-660,-600,-570,-540,-480,-420,-360,-300,-240,-210,-180,-120,-60,0,60,120,180,210,240,270,300,330,345,360,390,420,480,540,570,600,630,660,690,720,765,780,840];dojo.date.getDaysInMonth=function(_399){var _39a=_399.getMonth();var days=[31,28,31,30,31,30,31,31,30,31,30,31];if(_39a==1&&dojo.date.isLeapYear(_399)){return 29;}else{return days[_39a];}};dojo.date.isLeapYear=function(_39c){var year=_39c.getFullYear();return (year%400==0)?true:(year%100==0)?false:(year%4==0)?true:false;};dojo.date.getTimezoneName=function(_39e){var str=_39e.toString();var tz="";var _3a1;var pos=str.indexOf("(");if(pos>-1){pos++;tz=str.substring(pos,str.indexOf(")"));}else{var pat=/([A-Z\/]+) \d{4}$/;if((_3a1=str.match(pat))){tz=_3a1[1];}else{str=_39e.toLocaleString();pat=/ ([A-Z\/]+)$/;if((_3a1=str.match(pat))){tz=_3a1[1];}}
}
return tz=="AM"||tz=="PM"?"":tz;};dojo.date.getOrdinal=function(_3a4){var date=_3a4.getDate();if(date%100!=11&&date%10==1){return "st";}else{if(date%100!=12&&date%10==2){return "nd";}else{if(date%100!=13&&date%10==3){return "rd";}else{return "th";}}
}};dojo.date.compareTypes={DATE:1,TIME:2};dojo.date.compare=function(_3a6,_3a7,_3a8){var dA=_3a6;var dB=_3a7||new Date();var now=new Date();with(dojo.date.compareTypes){var opt=_3a8||(DATE|TIME);var d1=new Date((opt&DATE)?dA.getFullYear():now.getFullYear(),(opt&DATE)?dA.getMonth():now.getMonth(),(opt&DATE)?dA.getDate():now.getDate(),(opt&TIME)?dA.getHours():0,(opt&TIME)?dA.getMinutes():0,(opt&TIME)?dA.getSeconds():0);var d2=new Date((opt&DATE)?dB.getFullYear():now.getFullYear(),(opt&DATE)?dB.getMonth():now.getMonth(),(opt&DATE)?dB.getDate():now.getDate(),(opt&TIME)?dB.getHours():0,(opt&TIME)?dB.getMinutes():0,(opt&TIME)?dB.getSeconds():0);}
if(d1.valueOf()>d2.valueOf()){return 1;}
if(d1.valueOf()<d2.valueOf()){return -1;}
return 0;};dojo.date.dateParts={YEAR:0,MONTH:1,DAY:2,HOUR:3,MINUTE:4,SECOND:5,MILLISECOND:6,QUARTER:7,WEEK:8,WEEKDAY:9};dojo.date.add=function(dt,_3b0,incr){if(typeof dt=="number"){dt=new Date(dt);}
function fixOvershoot(){if(sum.getDate()<dt.getDate()){sum.setDate(0);}}
var sum=new Date(dt);with(dojo.date.dateParts){switch(_3b0){case YEAR:
sum.setFullYear(dt.getFullYear()+incr);fixOvershoot();break;case QUARTER:
incr*=3;case MONTH:
sum.setMonth(dt.getMonth()+incr);fixOvershoot();break;case WEEK:
incr*=7;case DAY:
sum.setDate(dt.getDate()+incr);break;case WEEKDAY:
var dat=dt.getDate();var _3b4=0;var days=0;var strt=0;var trgt=0;var adj=0;var mod=incr%5;if(mod==0){days=(incr>0)?5:-5;_3b4=(incr>0)?((incr-5)/5):((incr+5)/5);}else{days=mod;_3b4=parseInt(incr/5);}
strt=dt.getDay();if(strt==6&&incr>0){adj=1;}else{if(strt==0&&incr<0){adj=-1;}}
trgt=(strt+days);if(trgt==0||trgt==6){adj=(incr>0)?2:-2;}
sum.setDate(dat+(7*_3b4)+days+adj);break;case HOUR:
sum.setHours(sum.getHours()+incr);break;case MINUTE:
sum.setMinutes(sum.getMinutes()+incr);break;case SECOND:
sum.setSeconds(sum.getSeconds()+incr);break;case MILLISECOND:
sum.setMilliseconds(sum.getMilliseconds()+incr);break;default:
break;}}
return sum;};dojo.date.diff=function(dtA,dtB,_3bc){if(typeof dtA=="number"){dtA=new Date(dtA);}
if(typeof dtB=="number"){dtB=new Date(dtB);}
var _3bd=dtB.getFullYear()-dtA.getFullYear();var _3be=(dtB.getMonth()-dtA.getMonth())+(_3bd*12);var _3bf=dtB.getTime()-dtA.getTime();var _3c0=_3bf/1000;var _3c1=_3c0/60;var _3c2=_3c1/60;var _3c3=_3c2/24;var _3c4=_3c3/7;var _3c5=0;with(dojo.date.dateParts){switch(_3bc){case YEAR:
_3c5=_3bd;break;case QUARTER:
var mA=dtA.getMonth();var mB=dtB.getMonth();var qA=Math.floor(mA/3)+1;var qB=Math.floor(mB/3)+1;qB+=(_3bd*4);_3c5=qB-qA;break;case MONTH:
_3c5=_3be;break;case WEEK:
_3c5=parseInt(_3c4);break;case DAY:
_3c5=_3c3;break;case WEEKDAY:
var days=Math.round(_3c3);var _3cb=parseInt(days/7);var mod=days%7;if(mod==0){days=_3cb*5;}else{var adj=0;var aDay=dtA.getDay();var bDay=dtB.getDay();_3cb=parseInt(days/7);mod=days%7;var _3d0=new Date(dtA);_3d0.setDate(_3d0.getDate()+(_3cb*7));var _3d1=_3d0.getDay();if(_3c3>0){switch(true){case aDay==6:
adj=-1;break;case aDay==0:
adj=0;break;case bDay==6:
adj=-1;break;case bDay==0:
adj=-2;break;case (_3d1+mod)>5:
adj=-2;break;default:
break;}}else{if(_3c3<0){switch(true){case aDay==6:
adj=0;break;case aDay==0:
adj=1;break;case bDay==6:
adj=2;break;case bDay==0:
adj=1;break;case (_3d1+mod)<0:
adj=2;break;default:
break;}}
}
days+=adj;days-=(_3cb*2);}
_3c5=days;break;case HOUR:
_3c5=_3c2;break;case MINUTE:
_3c5=_3c1;break;case SECOND:
_3c5=_3c0;break;case MILLISECOND:
_3c5=_3bf;break;default:
break;}}
return Math.round(_3c5);};dojo.provide("dojo.date.supplemental");dojo.date.getFirstDayOfWeek=function(_3d2){var _3d3={mv:5,ae:6,af:6,bh:6,dj:6,dz:6,eg:6,er:6,et:6,iq:6,ir:6,jo:6,ke:6,kw:6,lb:6,ly:6,ma:6,om:6,qa:6,sa:6,sd:6,so:6,tn:6,ye:6,as:0,au:0,az:0,bw:0,ca:0,cn:0,fo:0,ge:0,gl:0,gu:0,hk:0,ie:0,il:0,is:0,jm:0,jp:0,kg:0,kr:0,la:0,mh:0,mo:0,mp:0,mt:0,nz:0,ph:0,pk:0,sg:0,th:0,tt:0,tw:0,um:0,us:0,uz:0,vi:0,za:0,zw:0,et:0,mw:0,ng:0,tj:0,gb:0,sy:4};_3d2=dojo.hostenv.normalizeLocale(_3d2);var _3d4=_3d2.split("-")[1];var dow=_3d3[_3d4];return (typeof dow=="undefined")?1:dow;};dojo.date.getWeekend=function(_3d6){var _3d7={eg:5,il:5,sy:5,"in":0,ae:4,bh:4,dz:4,iq:4,jo:4,kw:4,lb:4,ly:4,ma:4,om:4,qa:4,sa:4,sd:4,tn:4,ye:4};var _3d8={ae:5,bh:5,dz:5,iq:5,jo:5,kw:5,lb:5,ly:5,ma:5,om:5,qa:5,sa:5,sd:5,tn:5,ye:5,af:5,ir:5,eg:6,il:6,sy:6};_3d6=dojo.hostenv.normalizeLocale(_3d6);var _3d9=_3d6.split("-")[1];var _3da=_3d7[_3d9];var end=_3d8[_3d9];if(typeof _3da=="undefined"){_3da=6;}
if(typeof end=="undefined"){end=0;}
return {start:_3da,end:end};};dojo.date.isWeekend=function(_3dc,_3dd){var _3de=dojo.date.getWeekend(_3dd);var day=(_3dc||new Date()).getDay();if(_3de.end<_3de.start){_3de.end+=7;if(day<_3de.start){day+=7;}}
return day>=_3de.start&&day<=_3de.end;};dojo.provide("dojo.i18n.common");dojo.i18n.getLocalization=function(_3e0,_3e1,_3e2){dojo.hostenv.preloadLocalizations();_3e2=dojo.hostenv.normalizeLocale(_3e2);var _3e3=_3e2.split("-");var _3e4=[_3e0,"nls",_3e1].join(".");var _3e5=dojo.hostenv.findModule(_3e4,true);var _3e6;for(var i=_3e3.length;i>0;i--){var loc=_3e3.slice(0,i).join("_");if(_3e5[loc]){_3e6=_3e5[loc];break;}}
if(!_3e6){_3e6=_3e5.ROOT;}
if(_3e6){var _3e9=function(){};_3e9.prototype=_3e6;return new _3e9();}
dojo.raise("Bundle not found: "+_3e1+" in "+_3e0+" , locale="+_3e2);};dojo.i18n.isLTR=function(_3ea){var lang=dojo.hostenv.normalizeLocale(_3ea).split("-")[0];var RTL={ar:true,fa:true,he:true,ur:true,yi:true};return !RTL[lang];};dojo.provide("dojo.date.format");(function(){dojo.date.format=function(_3ed,_3ee){function formatPattern(_3ef,_3f0){return _3f0.replace(/([a-z])\1*/ig,function(_3f1){var s;var c=_3f1.charAt(0);var l=_3f1.length;var pad;var _3f6=["abbr","wide","narrow"];switch(c){case "G":
if(l>3){dojo.unimplemented("Era format not implemented");}
s=info.eras[_3ef.getFullYear()<0?1:0];break;case "y":
s=_3ef.getFullYear();switch(l){case 1:
break;case 2:
s=String(s);s=s.substr(s.length-2);break;default:
pad=true;}
break;case "Q":
case "q":
s=Math.ceil((_3ef.getMonth()+1)/3);switch(l){case 1:
case 2:
pad=true;break;case 3:
case 4:
dojo.unimplemented("Quarter format not implemented");}
break;case "M":
case "L":
var m=_3ef.getMonth();var _3f9;switch(l){case 1:
case 2:
s=m+1;pad=true;break;case 3:
case 4:
case 5:
_3f9=_3f6[l-3];break;}
if(_3f9){var type=(c=="L")?"standalone":"format";var prop=["months",type,_3f9].join("-");s=info[prop][m];}
break;case "w":
var _3fc=0;s=dojo.date.getWeekOfYear(_3ef,_3fc);pad=true;break;case "d":
s=_3ef.getDate();pad=true;break;case "D":
s=dojo.date.getDayOfYear(_3ef);pad=true;break;case "E":
case "e":
case "c":
var d=_3ef.getDay();var _3f9;switch(l){case 1:
case 2:
if(c=="e"){var _3fe=dojo.date.getFirstDayOfWeek(_3ee.locale);d=(d-_3fe+7)%7;}
if(c!="c"){s=d+1;pad=true;break;}
case 3:
case 4:
case 5:
_3f9=_3f6[l-3];break;}
if(_3f9){var type=(c=="c")?"standalone":"format";var prop=["days",type,_3f9].join("-");s=info[prop][d];}
break;case "a":
var _3ff=(_3ef.getHours()<12)?"am":"pm";s=info[_3ff];break;case "h":
case "H":
case "K":
case "k":
var h=_3ef.getHours();switch(c){case "h":
s=(h%12)||12;break;case "H":
s=h;break;case "K":
s=(h%12);break;case "k":
s=h||24;break;}
pad=true;break;case "m":
s=_3ef.getMinutes();pad=true;break;case "s":
s=_3ef.getSeconds();pad=true;break;case "S":
s=Math.round(_3ef.getMilliseconds()*Math.pow(10,l-3));break;case "v":
case "z":
s=dojo.date.getTimezoneName(_3ef);if(s){break;}
l=4;case "Z":
var _401=_3ef.getTimezoneOffset();var tz=[(_401<=0?"+":"-"),dojo.string.pad(Math.floor(Math.abs(_401)/60),2),dojo.string.pad(Math.abs(_401)%60,2)];if(l==4){tz.splice(0,0,"GMT");tz.splice(3,0,":");}
s=tz.join("");break;case "Y":
case "u":
case "W":
case "F":
case "g":
case "A":
dojo.debug(_3f1+" modifier not yet implemented");s="?";break;default:
dojo.raise("dojo.date.format: invalid pattern char: "+_3f0);}
if(pad){s=dojo.string.pad(s,l);}
return s;});}
_3ee=_3ee||{};var _403=dojo.hostenv.normalizeLocale(_3ee.locale);var _404=_3ee.formatLength||"full";var info=dojo.date._getGregorianBundle(_403);var str=[];var _406=dojo.lang.curry(this,formatPattern,_3ed);if(_3ee.selector!="timeOnly"){var _407=_3ee.datePattern||info["dateFormat-"+_404];if(_407){str.push(_processPattern(_407,_406));}}
if(_3ee.selector!="dateOnly"){var _408=_3ee.timePattern||info["timeFormat-"+_404];if(_408){str.push(_processPattern(_408,_406));}}
var _409=str.join(" ");return _409;};dojo.date.parse=function(_40a,_40b){_40b=_40b||{};var _40c=dojo.hostenv.normalizeLocale(_40b.locale);var info=dojo.date._getGregorianBundle(_40c);var _40e=_40b.formatLength||"full";if(!_40b.selector){_40b.selector="dateOnly";}
var _40f=_40b.datePattern||info["dateFormat-"+_40e];var _410=_40b.timePattern||info["timeFormat-"+_40e];var _411;if(_40b.selector=="dateOnly"){_411=_40f;}else{if(_40b.selector=="timeOnly"){_411=_410;}else{if(_40b.selector=="dateTime"){_411=_40f+" "+_410;}else{var msg="dojo.date.parse: Unknown selector param passed: '"+_40b.selector+"'.";msg+=" Defaulting to date pattern.";dojo.debug(msg);_411=_40f;}}
}
var _413=[];var _414=_processPattern(_411,dojo.lang.curry(this,_buildDateTimeRE,_413,info,_40b));var _415=new RegExp("^"+_414+"$");var _416=_415.exec(_40a);if(!_416){return null;}
var _417=["abbr","wide","narrow"];var _418=new Date(1972,0);var _419={};for(var i=1;i<_416.length;i++){var grp=_413[i-1];var l=grp.length;var v=_416[i];switch(grp.charAt(0)){case "y":
if(l!=2){_418.setFullYear(v);_419.year=v;}else{if(v<100){v=Number(v);var year=""+new Date().getFullYear();var _41f=year.substring(0,2)*100;var _420=Number(year.substring(2,4));var _421=Math.min(_420+20,99);var num=(v<_421)?_41f+v:_41f-100+v;_418.setFullYear(num);_419.year=num;}else{if(_40b.strict){return null;}
_418.setFullYear(v);_419.year=v;}}
break;case "M":
if(l>2){if(!_40b.strict){v=v.replace(/\./g,"");v=v.toLowerCase();}
var _423=info["months-format-"+_417[l-3]].concat();for(var j=0;j<_423.length;j++){if(!_40b.strict){_423[j]=_423[j].toLowerCase();}
if(v==_423[j]){_418.setMonth(j);_419.month=j;break;}}
if(j==_423.length){dojo.debug("dojo.date.parse: Could not parse month name: '"+v+"'.");return null;}}else{_418.setMonth(v-1);_419.month=v-1;}
break;case "E":
case "e":
if(!_40b.strict){v=v.toLowerCase();}
var days=info["days-format-"+_417[l-3]].concat();for(var j=0;j<days.length;j++){if(!_40b.strict){days[j]=days[j].toLowerCase();}
if(v==days[j]){break;}}
if(j==days.length){dojo.debug("dojo.date.parse: Could not parse weekday name: '"+v+"'.");return null;}
break;case "d":
_418.setDate(v);_419.date=v;break;case "D":
dojo.date.setDayOfYear(_418,v);break;case "w":
var _426=0;dojo.date.setWeekOfYear(_418,v,_426);break;case "a":
var am=_40b.am||info.am;var pm=_40b.pm||info.pm;if(!_40b.strict){v=v.replace(/\./g,"").toLowerCase();am=am.replace(/\./g,"").toLowerCase();pm=pm.replace(/\./g,"").toLowerCase();}
if(_40b.strict&&v!=am&&v!=pm){dojo.debug("dojo.date.parse: Could not parse am/pm part.");return null;}
var _429=_418.getHours();if(v==pm&&_429<12){_418.setHours(_429+12);}else{if(v==am&&_429==12){_418.setHours(0);}}
break;case "K":
if(v==24){v=0;}
case "h":
case "H":
case "k":
if(v>23){dojo.debug("dojo.date.parse: Illegal hours value");return null;}
_418.setHours(v);break;case "m":
_418.setMinutes(v);break;case "s":
_418.setSeconds(v);break;case "S":
_418.setMilliseconds(v);break;default:
dojo.unimplemented("dojo.date.parse: unsupported pattern char="+grp.charAt(0));}}
if(_419.year&&_418.getFullYear()!=_419.year){dojo.debug("dojo.date.parse: Parsed year: '"+_418.getFullYear()+"' did not match input year: '"+_419.year+"'.");return null;}
if(_419.month&&_418.getMonth()!=_419.month){dojo.debug("dojo.date.parse: Parsed month: '"+_418.getMonth()+"' did not match input month: '"+_419.month+"'.");return null;}
if(_419.date&&_418.getDate()!=_419.date){dojo.debug("dojo.date.parse: Parsed day of month: '"+_418.getDate()+"' did not match input day of month: '"+_419.date+"'.");return null;}
return _418;};function _processPattern(_42a,_42b,_42c,_42d){var _42e=function(x){return x;};_42b=_42b||_42e;_42c=_42c||_42e;_42d=_42d||_42e;var _430=_42a.match(/(''|[^'])+/g);var _431=false;for(var i=0;i<_430.length;i++){if(!_430[i]){_430[i]="";}else{_430[i]=(_431?_42c:_42b)(_430[i]);_431=!_431;}}
return _42d(_430.join(""));}
function _buildDateTimeRE(_433,info,_435,_436){return _436.replace(/([a-z])\1*/ig,function(_437){var s;var c=_437.charAt(0);var l=_437.length;switch(c){case "y":
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
var am=_435.am||info.am||"AM";var pm=_435.pm||info.pm||"PM";if(_435.strict){s=am+"|"+pm;}else{s=am;s+=(am!=am.toLowerCase())?"|"+am.toLowerCase():"";s+="|";s+=(pm!=pm.toLowerCase())?pm+"|"+pm.toLowerCase():pm;}
break;default:
dojo.unimplemented("parse of date format, pattern="+_436);}
if(_433){_433.push(_437);}
return "\\s*("+s+")\\s*";});}})();dojo.date.strftime=function(_43d,_43e,_43f){var _440=null;function _(s,n){return dojo.string.pad(s,n||2,_440||"0");}
var info=dojo.date._getGregorianBundle(_43f);function $(_444){switch(_444){case "a":
return dojo.date.getDayShortName(_43d,_43f);case "A":
return dojo.date.getDayName(_43d,_43f);case "b":
case "h":
return dojo.date.getMonthShortName(_43d,_43f);case "B":
return dojo.date.getMonthName(_43d,_43f);case "c":
return dojo.date.format(_43d,{locale:_43f});case "C":
return _(Math.floor(_43d.getFullYear()/100));case "d":
return _(_43d.getDate());case "D":
return $("m")+"/"+$("d")+"/"+$("y");case "e":
if(_440==null){_440=" ";}
return _(_43d.getDate());case "f":
if(_440==null){_440=" ";}
return _(_43d.getMonth()+1);case "g":
break;case "G":
dojo.unimplemented("unimplemented modifier 'G'");break;case "F":
return $("Y")+"-"+$("m")+"-"+$("d");case "H":
return _(_43d.getHours());case "I":
return _(_43d.getHours()%12||12);case "j":
return _(dojo.date.getDayOfYear(_43d),3);case "k":
if(_440==null){_440=" ";}
return _(_43d.getHours());case "l":
if(_440==null){_440=" ";}
return _(_43d.getHours()%12||12);case "m":
return _(_43d.getMonth()+1);case "M":
return _(_43d.getMinutes());case "n":
return "\n";case "p":
return info[_43d.getHours()<12?"am":"pm"];case "r":
return $("I")+":"+$("M")+":"+$("S")+" "+$("p");case "R":
return $("H")+":"+$("M");case "S":
return _(_43d.getSeconds());case "t":
return "\t";case "T":
return $("H")+":"+$("M")+":"+$("S");case "u":
return String(_43d.getDay()||7);case "U":
return _(dojo.date.getWeekOfYear(_43d));case "V":
return _(dojo.date.getIsoWeekOfYear(_43d));case "W":
return _(dojo.date.getWeekOfYear(_43d,1));case "w":
return String(_43d.getDay());case "x":
return dojo.date.format(_43d,{selector:"dateOnly",locale:_43f});case "X":
return dojo.date.format(_43d,{selector:"timeOnly",locale:_43f});case "y":
return _(_43d.getFullYear()%100);case "Y":
return String(_43d.getFullYear());case "z":
var _445=_43d.getTimezoneOffset();return (_445>0?"-":"+")+_(Math.floor(Math.abs(_445)/60))+":"+_(Math.abs(_445)%60);case "Z":
return dojo.date.getTimezoneName(_43d);case "%":
return "%";}}
var _446="";var i=0;var _448=0;var _449=null;while((_448=_43e.indexOf("%",i))!=-1){_446+=_43e.substring(i,_448++);switch(_43e.charAt(_448++)){case "_":
_440=" ";break;case "-":
_440="";break;case "0":
_440="0";break;case "^":
_449="upper";break;case "*":
_449="lower";break;case "#":
_449="swap";break;default:
_440=null;_448--;break;}
var _44a=$(_43e.charAt(_448++));switch(_449){case "upper":
_44a=_44a.toUpperCase();break;case "lower":
_44a=_44a.toLowerCase();break;case "swap":
var _44b=_44a.toLowerCase();var _44c="";var j=0;var ch="";while(j<_44a.length){ch=_44a.charAt(j);_44c+=(ch==_44b.charAt(j))?ch.toUpperCase():ch.toLowerCase();j++;}
_44a=_44c;break;default:
break;}
_449=null;_446+=_44a;i=_448;}
_446+=_43e.substring(i);return _446;};(function(){var _44f=[];dojo.date.addCustomFormats=function(_450,_451){_44f.push({pkg:_450,name:_451});};dojo.date._getGregorianBundle=function(_452){var _453={};dojo.lang.forEach(_44f,function(desc){var _455=dojo.i18n.getLocalization(desc.pkg,desc.name,_452);_453=dojo.lang.mixin(_453,_455);},this);return _453;};})();dojo.date.addCustomFormats("dojo.i18n.cldr","gregorian");dojo.date.addCustomFormats("dojo.i18n.cldr","gregorianExtras");dojo.date.getNames=function(item,type,use,_459){var _45a;var _45b=dojo.date._getGregorianBundle(_459);var _45c=[item,use,type];if(use=="standAlone"){_45a=_45b[_45c.join("-")];}
_45c[1]="format";return (_45a||_45b[_45c.join("-")]).concat();};dojo.date.getDayName=function(_45d,_45e){return dojo.date.getNames("days","wide","format",_45e)[_45d.getDay()];};dojo.date.getDayShortName=function(_45f,_460){return dojo.date.getNames("days","abbr","format",_460)[_45f.getDay()];};dojo.date.getMonthName=function(_461,_462){return dojo.date.getNames("months","wide","format",_462)[_461.getMonth()];};dojo.date.getMonthShortName=function(_463,_464){return dojo.date.getNames("months","abbr","format",_464)[_463.getMonth()];};dojo.date.toRelativeString=function(_465){var now=new Date();var diff=(now-_465)/1000;var end=" ago";var _469=false;if(diff<0){_469=true;end=" from now";diff=-diff;}
if(diff<60){diff=Math.round(diff);return diff+" second"+(diff==1?"":"s")+end;}
if(diff<60*60){diff=Math.round(diff/60);return diff+" minute"+(diff==1?"":"s")+end;}
if(diff<60*60*24){diff=Math.round(diff/3600);return diff+" hour"+(diff==1?"":"s")+end;}
if(diff<60*60*24*7){diff=Math.round(diff/(3600*24));if(diff==1){return _469?"Tomorrow":"Yesterday";}else{return diff+" days"+end;}}
return dojo.date.format(_465);};dojo.date.toSql=function(_46a,_46b){return dojo.date.strftime(_46a,"%F"+!_46b?" %T":"");};dojo.date.fromSql=function(_46c){var _46d=_46c.split(/[\- :]/g);while(_46d.length<6){_46d.push(0);}
return new Date(_46d[0],(parseInt(_46d[1],10)-1),_46d[2],_46d[3],_46d[4],_46d[5]);};dojo.provide("dojo.xml.Parse");dojo.xml.Parse=function(){var isIE=((dojo.render.html.capable)&&(dojo.render.html.ie));function getTagName(node){try{return node.tagName.toLowerCase();}
catch(e){return "";}}
function getDojoTagName(node){var _471=getTagName(node);if(!_471){return "";}
if((dojo.widget)&&(dojo.widget.tags[_471])){return _471;}
var p=_471.indexOf(":");if(p>=0){return _471;}
if(_471.substr(0,5)=="dojo:"){return _471;}
if(dojo.render.html.capable&&dojo.render.html.ie&&node.scopeName&&node.scopeName!="HTML"){return node.scopeName.toLowerCase()+":"+_471;}
if(_471.substr(0,4)=="dojo"){return "dojo:"+_471.substring(4);}
var djt=node.getAttribute("dojoType")||node.getAttribute("dojotype");if(djt){if(djt.indexOf(":")<0){djt="dojo:"+djt;}
return djt.toLowerCase();}
djt=node.getAttributeNS&&node.getAttributeNS(dojo.dom.dojoml,"type");if(djt){return "dojo:"+djt.toLowerCase();}
try{djt=node.getAttribute("dojo:type");}
catch(e){}
if(djt){return "dojo:"+djt.toLowerCase();}
if((dj_global["djConfig"])&&(!djConfig["ignoreClassNames"])){var _474=node.className||node.getAttribute("class");if((_474)&&(_474.indexOf)&&(_474.indexOf("dojo-")!=-1)){var _475=_474.split(" ");for(var x=0,c=_475.length;x<c;x++){if(_475[x].slice(0,5)=="dojo-"){return "dojo:"+_475[x].substr(5).toLowerCase();}}
}}
return "";}
this.parseElement=function(node,_479,_47a,_47b){var _47c=getTagName(node);if(isIE&&_47c.indexOf("/")==0){return null;}
try{var attr=node.getAttribute("parseWidgets");if(attr&&attr.toLowerCase()=="false"){return {};}}
catch(e){}
var _47e=true;if(_47a){var _47f=getDojoTagName(node);_47c=_47f||_47c;_47e=Boolean(_47f);}
var _480={};_480[_47c]=[];var pos=_47c.indexOf(":");if(pos>0){var ns=_47c.substring(0,pos);_480["ns"]=ns;if((dojo.ns)&&(!dojo.ns.allow(ns))){_47e=false;}}
if(_47e){var _483=this.parseAttributes(node);for(var attr in _483){if((!_480[_47c][attr])||(typeof _480[_47c][attr]!="array")){_480[_47c][attr]=[];}
_480[_47c][attr].push(_483[attr]);}
_480[_47c].nodeRef=node;_480.tagName=_47c;_480.index=_47b||0;}
var _484=0;for(var i=0;i<node.childNodes.length;i++){var tcn=node.childNodes.item(i);switch(tcn.nodeType){case dojo.dom.ELEMENT_NODE:
var ctn=getDojoTagName(tcn)||getTagName(tcn);if(!_480[ctn]){_480[ctn]=[];}
_480[ctn].push(this.parseElement(tcn,true,_47a,_484));if((tcn.childNodes.length==1)&&(tcn.childNodes.item(0).nodeType==dojo.dom.TEXT_NODE)){_480[ctn][_480[ctn].length-1].value=tcn.childNodes.item(0).nodeValue;}
_484++;break;case dojo.dom.TEXT_NODE:
if(node.childNodes.length==1){_480[_47c].push({value:node.childNodes.item(0).nodeValue});}
break;default:
break;}}
return _480;};this.parseAttributes=function(node){var _489={};var atts=node.attributes;var _48b,i=0;while((_48b=atts[i++])){if(isIE){if(!_48b){continue;}
if((typeof _48b=="object")&&(typeof _48b.nodeValue=="undefined")||(_48b.nodeValue==null)||(_48b.nodeValue=="")){continue;}}
var nn=_48b.nodeName.split(":");nn=(nn.length==2)?nn[1]:_48b.nodeName;_489[nn]={value:_48b.nodeValue};}
return _489;};};dojo.provide("dojo.lang.declare");dojo.lang.declare=function(_48e,_48f,init,_491){if((dojo.lang.isFunction(_491))||((!_491)&&(!dojo.lang.isFunction(init)))){if(dojo.lang.isFunction(_491)){dojo.deprecated("dojo.lang.declare("+_48e+"...):","use class, superclass, initializer, properties argument order","0.6");}
var temp=_491;_491=init;init=temp;}
if(_491&&_491.initializer){dojo.deprecated("dojo.lang.declare("+_48e+"...):","specify initializer as third argument, not as an element in properties","0.6");}
var _493=[];if(dojo.lang.isArray(_48f)){_493=_48f;_48f=_493.shift();}
if(!init){init=dojo.evalObjPath(_48e,false);if((init)&&(!dojo.lang.isFunction(init))){init=null;}}
var ctor=dojo.lang.declare._makeConstructor();var scp=(_48f?_48f.prototype:null);if(scp){scp.prototyping=true;ctor.prototype=new _48f();scp.prototyping=false;}
ctor.superclass=scp;ctor.mixins=_493;for(var i=0,l=_493.length;i<l;i++){dojo.lang.extend(ctor,_493[i].prototype);}
ctor.prototype.initializer=null;ctor.prototype.declaredClass=_48e;if(dojo.lang.isArray(_491)){dojo.lang.extend.apply(dojo.lang,[ctor].concat(_491));}else{dojo.lang.extend(ctor,(_491)||{});}
dojo.lang.extend(ctor,dojo.lang.declare._common);ctor.prototype.constructor=ctor;ctor.prototype.initializer=(ctor.prototype.initializer)||(init)||(function(){});var _498=dojo.parseObjPath(_48e,null,true);_498.obj[_498.prop]=ctor;return ctor;};dojo.lang.declare._makeConstructor=function(){return function(){var self=this._getPropContext();var s=self.constructor.superclass;if((s)&&(s.constructor)){if(s.constructor==arguments.callee){this._inherited("constructor",arguments);}else{this._contextMethod(s,"constructor",arguments);}}
var ms=(self.constructor.mixins)||([]);for(var i=0,m;(m=ms[i]);i++){(((m.prototype)&&(m.prototype.initializer))||(m)).apply(this,arguments);}
if((!this.prototyping)&&(self.initializer)){self.initializer.apply(this,arguments);}};};dojo.lang.declare._common={_getPropContext:function(){return (this.___proto||this);},_contextMethod:function(_49e,_49f,args){var _4a1,_4a2=this.___proto;this.___proto=_49e;try{_4a1=_49e[_49f].apply(this,(args||[]));}
catch(e){throw e;}
finally{this.___proto=_4a2;}
return _4a1;},_inherited:function(prop,args){var p=this._getPropContext();do{if((!p.constructor)||(!p.constructor.superclass)){return;}
p=p.constructor.superclass;}while(!(prop in p));return (dojo.lang.isFunction(p[prop])?this._contextMethod(p,prop,args):p[prop]);}};dojo.declare=dojo.lang.declare;dojo.provide("dojo.ns");dojo.ns={namespaces:{},failed:{},loading:{},loaded:{},register:function(name,_4a7,_4a8,_4a9){if(!_4a9||!this.namespaces[name]){this.namespaces[name]=new dojo.ns.Ns(name,_4a7,_4a8);}},allow:function(name){if(this.failed[name]){return false;}
if((djConfig.excludeNamespace)&&(dojo.lang.inArray(djConfig.excludeNamespace,name))){return false;}
return ((name==this.dojo)||(!djConfig.includeNamespace)||(dojo.lang.inArray(djConfig.includeNamespace,name)));},get:function(name){return this.namespaces[name];},require:function(name){var ns=this.namespaces[name];if((ns)&&(this.loaded[name])){return ns;}
if(!this.allow(name)){return false;}
if(this.loading[name]){dojo.debug("dojo.namespace.require: re-entrant request to load namespace \""+name+"\" must fail.");return false;}
var req=dojo.require;this.loading[name]=true;try{if(name=="dojo"){req("dojo.namespaces.dojo");}else{if(!dojo.hostenv.moduleHasPrefix(name)){dojo.registerModulePath(name,"../"+name);}
req([name,"manifest"].join("."),false,true);}
if(!this.namespaces[name]){this.failed[name]=true;}}
finally{this.loading[name]=false;}
return this.namespaces[name];}};dojo.ns.Ns=function(name,_4b0,_4b1){this.name=name;this.module=_4b0;this.resolver=_4b1;this._loaded=[];this._failed=[];};dojo.ns.Ns.prototype.resolve=function(name,_4b3,_4b4){if(!this.resolver||djConfig["skipAutoRequire"]){return false;}
var _4b5=this.resolver(name,_4b3);if((_4b5)&&(!this._loaded[_4b5])&&(!this._failed[_4b5])){var req=dojo.require;req(_4b5,false,true);if(dojo.hostenv.findModule(_4b5,false)){this._loaded[_4b5]=true;}else{if(!_4b4){dojo.raise("dojo.ns.Ns.resolve: module '"+_4b5+"' not found after loading via namespace '"+this.name+"'");}
this._failed[_4b5]=true;}}
return Boolean(this._loaded[_4b5]);};dojo.registerNamespace=function(name,_4b8,_4b9){dojo.ns.register.apply(dojo.ns,arguments);};dojo.registerNamespaceResolver=function(name,_4bb){var n=dojo.ns.namespaces[name];if(n){n.resolver=_4bb;}};dojo.registerNamespaceManifest=function(_4bd,path,name,_4c0,_4c1){dojo.registerModulePath(name,path);dojo.registerNamespace(name,_4c0,_4c1);};dojo.registerNamespace("dojo","dojo.widget");dojo.provide("dojo.event.topic");dojo.event.topic=new function(){this.topics={};this.getTopic=function(_4c2){if(!this.topics[_4c2]){this.topics[_4c2]=new this.TopicImpl(_4c2);}
return this.topics[_4c2];};this.registerPublisher=function(_4c3,obj,_4c5){var _4c3=this.getTopic(_4c3);_4c3.registerPublisher(obj,_4c5);};this.subscribe=function(_4c6,obj,_4c8){var _4c6=this.getTopic(_4c6);_4c6.subscribe(obj,_4c8);};this.unsubscribe=function(_4c9,obj,_4cb){var _4c9=this.getTopic(_4c9);_4c9.unsubscribe(obj,_4cb);};this.destroy=function(_4cc){this.getTopic(_4cc).destroy();delete this.topics[_4cc];};this.publishApply=function(_4cd,args){var _4cd=this.getTopic(_4cd);_4cd.sendMessage.apply(_4cd,args);};this.publish=function(_4cf,_4d0){var _4cf=this.getTopic(_4cf);var args=[];for(var x=1;x<arguments.length;x++){args.push(arguments[x]);}
_4cf.sendMessage.apply(_4cf,args);};};dojo.event.topic.TopicImpl=function(_4d3){this.topicName=_4d3;this.subscribe=function(_4d4,_4d5){var tf=_4d5||_4d4;var to=(!_4d5)?dj_global:_4d4;return dojo.event.kwConnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});};this.unsubscribe=function(_4d8,_4d9){var tf=(!_4d9)?_4d8:_4d9;var to=(!_4d9)?null:_4d8;return dojo.event.kwDisconnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});};this._getJoinPoint=function(){return dojo.event.MethodJoinPoint.getForMethod(this,"sendMessage");};this.setSquelch=function(_4dc){this._getJoinPoint().squelch=_4dc;};this.destroy=function(){this._getJoinPoint().disconnect();};this.registerPublisher=function(_4dd,_4de){dojo.event.connect(_4dd,_4de,this,"sendMessage");};this.sendMessage=function(_4df){};};dojo.provide("dojo.event.*");dojo.provide("dojo.widget.Manager");dojo.widget.manager=new function(){this.widgets=[];this.widgetIds=[];this.topWidgets={};var _4e0={};var _4e1=[];this.getUniqueId=function(_4e2){var _4e3;do{_4e3=_4e2+"_"+(_4e0[_4e2]!=undefined?++_4e0[_4e2]:_4e0[_4e2]=0);}while(this.getWidgetById(_4e3));return _4e3;};this.add=function(_4e4){this.widgets.push(_4e4);if(!_4e4.extraArgs["id"]){_4e4.extraArgs["id"]=_4e4.extraArgs["ID"];}
if(_4e4.widgetId==""){if(_4e4["id"]){_4e4.widgetId=_4e4["id"];}else{if(_4e4.extraArgs["id"]){_4e4.widgetId=_4e4.extraArgs["id"];}else{_4e4.widgetId=this.getUniqueId(_4e4.ns+"_"+_4e4.widgetType);}}
}
if(this.widgetIds[_4e4.widgetId]){dojo.debug("widget ID collision on ID: "+_4e4.widgetId);}
this.widgetIds[_4e4.widgetId]=_4e4;};this.destroyAll=function(){for(var x=this.widgets.length-1;x>=0;x--){try{this.widgets[x].destroy(true);delete this.widgets[x];}
catch(e){}}
};this.remove=function(_4e6){if(dojo.lang.isNumber(_4e6)){var tw=this.widgets[_4e6].widgetId;delete this.widgetIds[tw];this.widgets.splice(_4e6,1);}else{this.removeById(_4e6);}};this.removeById=function(id){if(!dojo.lang.isString(id)){id=id["widgetId"];if(!id){dojo.debug("invalid widget or id passed to removeById");return;}}
for(var i=0;i<this.widgets.length;i++){if(this.widgets[i].widgetId==id){this.remove(i);break;}}
};this.getWidgetById=function(id){if(dojo.lang.isString(id)){return this.widgetIds[id];}
return id;};this.getWidgetsByType=function(type){var lt=type.toLowerCase();var _4ed=(type.indexOf(":")<0?function(x){return x.widgetType.toLowerCase();}:function(x){return x.getNamespacedType();});var ret=[];dojo.lang.forEach(this.widgets,function(x){if(_4ed(x)==lt){ret.push(x);}});return ret;};this.getWidgetsByFilter=function(_4f2,_4f3){var ret=[];dojo.lang.every(this.widgets,function(x){if(_4f2(x)){ret.push(x);if(_4f3){return false;}}
return true;});return (_4f3?ret[0]:ret);};this.getAllWidgets=function(){return this.widgets.concat();};this.getWidgetByNode=function(node){var w=this.getAllWidgets();node=dojo.byId(node);for(var i=0;i<w.length;i++){if(w[i].domNode==node){return w[i];}}
return null;};this.byId=this.getWidgetById;this.byType=this.getWidgetsByType;this.byFilter=this.getWidgetsByFilter;this.byNode=this.getWidgetByNode;var _4f9={};var _4fa=["dojo.widget"];for(var i=0;i<_4fa.length;i++){_4fa[_4fa[i]]=true;}
this.registerWidgetPackage=function(_4fc){if(!_4fa[_4fc]){_4fa[_4fc]=true;_4fa.push(_4fc);}};this.getWidgetPackageList=function(){return dojo.lang.map(_4fa,function(elt){return (elt!==true?elt:undefined);});};this.getImplementation=function(_4fe,_4ff,_500,ns){var impl=this.getImplementationName(_4fe,ns);if(impl){var ret=_4ff?new impl(_4ff):new impl();return ret;}};function buildPrefixCache(){for(var _504 in dojo.render){if(dojo.render[_504]["capable"]===true){var _505=dojo.render[_504].prefixes;for(var i=0;i<_505.length;i++){_4e1.push(_505[i].toLowerCase());}}
}}
var _507=function(_508,_509){if(!_509){return null;}
for(var i=0,l=_4e1.length,_50c;i<=l;i++){_50c=(i<l?_509[_4e1[i]]:_509);if(!_50c){continue;}
for(var name in _50c){if(name.toLowerCase()==_508){return _50c[name];}}
}
return null;};var _50e=function(_50f,_510){var _511=dojo.evalObjPath(_510,false);return (_511?_507(_50f,_511):null);};this.getImplementationName=function(_512,ns){var _514=_512.toLowerCase();ns=ns||"dojo";var imps=_4f9[ns]||(_4f9[ns]={});var impl=imps[_514];if(impl){return impl;}
if(!_4e1.length){buildPrefixCache();}
var _517=dojo.ns.get(ns);if(!_517){dojo.ns.register(ns,ns+".widget");_517=dojo.ns.get(ns);}
if(_517){_517.resolve(_512);}
impl=_50e(_514,_517.module);if(impl){return (imps[_514]=impl);}
_517=dojo.ns.require(ns);if((_517)&&(_517.resolver)){_517.resolve(_512);impl=_50e(_514,_517.module);if(impl){return (imps[_514]=impl);}}
throw new Error("Could not locate widget implementation for \""+_512+"\" in \""+_517.module+"\" registered to namespace \""+_517.name+"\"");};this.resizing=false;this.onWindowResized=function(){if(this.resizing){return;}
try{this.resizing=true;for(var id in this.topWidgets){var _519=this.topWidgets[id];if(_519.checkSize){_519.checkSize();}}
}
catch(e){}
finally{this.resizing=false;}};if(typeof window!="undefined"){dojo.addOnLoad(this,"onWindowResized");dojo.event.connect(window,"onresize",this,"onWindowResized");}};(function(){var dw=dojo.widget;var dwm=dw.manager;var h=dojo.lang.curry(dojo.lang,"hitch",dwm);var g=function(_51e,_51f){dw[(_51f||_51e)]=h(_51e);};g("add","addWidget");g("destroyAll","destroyAllWidgets");g("remove","removeWidget");g("removeById","removeWidgetById");g("getWidgetById");g("getWidgetById","byId");g("getWidgetsByType");g("getWidgetsByFilter");g("getWidgetsByType","byType");g("getWidgetsByFilter","byFilter");g("getWidgetByNode","byNode");dw.all=function(n){var _521=dwm.getAllWidgets.apply(dwm,arguments);if(arguments.length>0){return _521[n];}
return _521;};g("registerWidgetPackage");g("getImplementation","getWidgetImplementation");g("getImplementationName","getWidgetImplementationName");dw.widgets=dwm.widgets;dw.widgetIds=dwm.widgetIds;dw.root=dwm.root;})();dojo.provide("dojo.uri.Uri");dojo.uri=new function(){var _522=new RegExp("^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$");var _523=new RegExp("(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$");this.dojoUri=function(uri){return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri(),uri);};this.moduleUri=function(_525,uri){var loc=dojo.hostenv.getModuleSymbols(_525).join("/");if(!loc){return null;}
if(loc.lastIndexOf("/")!=loc.length-1){loc+="/";}
return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri()+loc,uri);};this.Uri=function(){var uri=arguments[0];for(var i=1;i<arguments.length;i++){if(!arguments[i]){continue;}
var _52a=new dojo.uri.Uri(arguments[i].toString());var _52b=new dojo.uri.Uri(uri.toString());if((_52a.path=="")&&(_52a.scheme==null)&&(_52a.authority==null)&&(_52a.query==null)){if(_52a.fragment!=null){_52b.fragment=_52a.fragment;}
_52a=_52b;}
if(_52a.scheme!=null&&_52a.authority!=null){uri="";}
if(_52a.scheme!=null){uri+=_52a.scheme+":";}
if(_52a.authority!=null){uri+="//"+_52a.authority;}
uri+=_52a.path;if(_52a.query!=null){uri+="?"+_52a.query;}
if(_52a.fragment!=null){uri+="#"+_52a.fragment;}}
this.uri=uri.toString();var r=this.uri.match(_523);this.scheme=r[2]||(r[1]?"":null);this.authority=r[4]||(r[3]?"":null);this.path=r[5];this.query=r[7]||(r[6]?"":null);this.fragment=r[9]||(r[8]?"":null);if(this.authority!=null){r=this.authority.match(_522);this.user=r[3]||null;this.password=r[4]||null;this.host=r[5];this.port=r[7]||null;}
this.toString=function(){return this.uri;};};};dojo.provide("dojo.uri.*");dojo.provide("dojo.html.common");dojo.lang.mixin(dojo.html,dojo.dom);dojo.html.getEventTarget=function(evt){if(!evt){evt=dojo.global().event||{};}
var t=(evt.srcElement?evt.srcElement:(evt.target?evt.target:null));while((t)&&(t.nodeType!=1)){t=t.parentNode;}
return t;};dojo.html.getViewport=function(){var _52f=dojo.global();var _530=dojo.doc();var w=0;var h=0;if(dojo.render.html.mozilla){w=_530.documentElement.clientWidth;h=_52f.innerHeight;}else{if(!dojo.render.html.opera&&_52f.innerWidth){w=_52f.innerWidth;h=_52f.innerHeight;}else{if(!dojo.render.html.opera&&dojo.exists(_530,"documentElement.clientWidth")){var w2=_530.documentElement.clientWidth;if(!w||w2&&w2<w){w=w2;}
h=_530.documentElement.clientHeight;}else{if(dojo.body().clientWidth){w=dojo.body().clientWidth;h=dojo.body().clientHeight;}}
}}
return {width:w,height:h};};dojo.html.getScroll=function(){var _534=dojo.global();var _535=dojo.doc();var top=_534.pageYOffset||_535.documentElement.scrollTop||dojo.body().scrollTop||0;var left=_534.pageXOffset||_535.documentElement.scrollLeft||dojo.body().scrollLeft||0;return {top:top,left:left,offset:{x:left,y:top}};};dojo.html.getParentByType=function(node,type){var _53a=dojo.doc();var _53b=dojo.byId(node);type=type.toLowerCase();while((_53b)&&(_53b.nodeName.toLowerCase()!=type)){if(_53b==(_53a["body"]||_53a["documentElement"])){return null;}
_53b=_53b.parentNode;}
return _53b;};dojo.html.getAttribute=function(node,attr){node=dojo.byId(node);if((!node)||(!node.getAttribute)){return null;}
var ta=typeof attr=="string"?attr:new String(attr);var v=node.getAttribute(ta.toUpperCase());if((v)&&(typeof v=="string")&&(v!="")){return v;}
if(v&&v.value){return v.value;}
if((node.getAttributeNode)&&(node.getAttributeNode(ta))){return (node.getAttributeNode(ta)).value;}else{if(node.getAttribute(ta)){return node.getAttribute(ta);}else{if(node.getAttribute(ta.toLowerCase())){return node.getAttribute(ta.toLowerCase());}}
}
return null;};dojo.html.hasAttribute=function(node,attr){return dojo.html.getAttribute(dojo.byId(node),attr)?true:false;};dojo.html.getCursorPosition=function(e){e=e||dojo.global().event;var _543={x:0,y:0};if(e.pageX||e.pageY){_543.x=e.pageX;_543.y=e.pageY;}else{var de=dojo.doc().documentElement;var db=dojo.body();_543.x=e.clientX+((de||db)["scrollLeft"])-((de||db)["clientLeft"]);_543.y=e.clientY+((de||db)["scrollTop"])-((de||db)["clientTop"]);}
return _543;};dojo.html.isTag=function(node){node=dojo.byId(node);if(node&&node.tagName){for(var i=1;i<arguments.length;i++){if(node.tagName.toLowerCase()==String(arguments[i]).toLowerCase()){return String(arguments[i]).toLowerCase();}}
}
return "";};if(dojo.render.html.ie&&!dojo.render.html.ie70){if(window.location.href.substr(0,6).toLowerCase()!="https:"){(function(){var _548=dojo.doc().createElement("script");_548.src="javascript:'dojo.html.createExternalElement=function(doc, tag){ return doc.createElement(tag); }'";dojo.doc().getElementsByTagName("head")[0].appendChild(_548);})();}}else{dojo.html.createExternalElement=function(doc,tag){return doc.createElement(tag);};}
dojo.provide("dojo.a11y");dojo.a11y={imgPath:dojo.uri.dojoUri("src/widget/templates/images"),doAccessibleCheck:true,accessible:null,checkAccessible:function(){if(this.accessible===null){this.accessible=false;if(this.doAccessibleCheck==true){this.accessible=this.testAccessible();}}
return this.accessible;},testAccessible:function(){this.accessible=false;if(dojo.render.html.ie||dojo.render.html.mozilla){var div=document.createElement("div");div.style.backgroundImage="url(\""+this.imgPath+"/tab_close.gif\")";dojo.body().appendChild(div);var _54c=null;if(window.getComputedStyle){var _54d=getComputedStyle(div,"");_54c=_54d.getPropertyValue("background-image");}else{_54c=div.currentStyle.backgroundImage;}
var _54e=false;if(_54c!=null&&(_54c=="none"||_54c=="url(invalid-url:)")){this.accessible=true;}
dojo.body().removeChild(div);}
return this.accessible;},setAccessible:function(_54f){this.accessible=_54f;},setCheckAccessible:function(_550){this.doAccessibleCheck=_550;},setAccessibleMode:function(){if(this.accessible===null){if(this.checkAccessible()){dojo.render.html.prefixes.unshift("a11y");}}
return this.accessible;}};dojo.provide("dojo.widget.Widget");dojo.declare("dojo.widget.Widget",null,function(){this.children=[];this.extraArgs={};},{parent:null,isTopLevel:false,disabled:false,isContainer:false,widgetId:"",widgetType:"Widget",ns:"dojo",getNamespacedType:function(){return (this.ns?this.ns+":"+this.widgetType:this.widgetType).toLowerCase();},toString:function(){return "[Widget "+this.getNamespacedType()+", "+(this.widgetId||"NO ID")+"]";},repr:function(){return this.toString();},enable:function(){this.disabled=false;},disable:function(){this.disabled=true;},onResized:function(){this.notifyChildrenOfResize();},notifyChildrenOfResize:function(){for(var i=0;i<this.children.length;i++){var _552=this.children[i];if(_552.onResized){_552.onResized();}}
},create:function(args,_554,_555,ns){if(ns){this.ns=ns;}
this.satisfyPropertySets(args,_554,_555);this.mixInProperties(args,_554,_555);this.postMixInProperties(args,_554,_555);dojo.widget.manager.add(this);this.buildRendering(args,_554,_555);this.initialize(args,_554,_555);this.postInitialize(args,_554,_555);this.postCreate(args,_554,_555);return this;},destroy:function(_557){if(this.parent){this.parent.removeChild(this);}
this.destroyChildren();this.uninitialize();this.destroyRendering(_557);dojo.widget.manager.removeById(this.widgetId);},destroyChildren:function(){var _558;var i=0;while(this.children.length>i){_558=this.children[i];if(_558 instanceof dojo.widget.Widget){this.removeChild(_558);_558.destroy();continue;}
i++;}},getChildrenOfType:function(type,_55b){var ret=[];var _55d=dojo.lang.isFunction(type);if(!_55d){type=type.toLowerCase();}
for(var x=0;x<this.children.length;x++){if(_55d){if(this.children[x] instanceof type){ret.push(this.children[x]);}}else{if(this.children[x].widgetType.toLowerCase()==type){ret.push(this.children[x]);}}
if(_55b){ret=ret.concat(this.children[x].getChildrenOfType(type,_55b));}}
return ret;},getDescendants:function(){var _55f=[];var _560=[this];var elem;while((elem=_560.pop())){_55f.push(elem);if(elem.children){dojo.lang.forEach(elem.children,function(elem){_560.push(elem);});}}
return _55f;},isFirstChild:function(){return this===this.parent.children[0];},isLastChild:function(){return this===this.parent.children[this.parent.children.length-1];},satisfyPropertySets:function(args){return args;},mixInProperties:function(args,frag){if((args["fastMixIn"])||(frag["fastMixIn"])){for(var x in args){this[x]=args[x];}
return;}
var _567;var _568=dojo.widget.lcArgsCache[this.widgetType];if(_568==null){_568={};for(var y in this){_568[((new String(y)).toLowerCase())]=y;}
dojo.widget.lcArgsCache[this.widgetType]=_568;}
var _56a={};for(var x in args){if(!this[x]){var y=_568[(new String(x)).toLowerCase()];if(y){args[y]=args[x];x=y;}}
if(_56a[x]){continue;}
_56a[x]=true;if((typeof this[x])!=(typeof _567)){if(typeof args[x]!="string"){this[x]=args[x];}else{if(dojo.lang.isString(this[x])){this[x]=args[x];}else{if(dojo.lang.isNumber(this[x])){this[x]=new Number(args[x]);}else{if(dojo.lang.isBoolean(this[x])){this[x]=(args[x].toLowerCase()=="false")?false:true;}else{if(dojo.lang.isFunction(this[x])){if(args[x].search(/[^\w\.]+/i)==-1){this[x]=dojo.evalObjPath(args[x],false);}else{var tn=dojo.lang.nameAnonFunc(new Function(args[x]),this);dojo.event.kwConnect({srcObj:this,srcFunc:x,adviceObj:this,adviceFunc:tn});}}else{if(dojo.lang.isArray(this[x])){this[x]=args[x].split(";");}else{if(this[x] instanceof Date){this[x]=new Date(Number(args[x]));}else{if(typeof this[x]=="object"){if(this[x] instanceof dojo.uri.Uri){this[x]=dojo.uri.dojoUri(args[x]);}else{var _56c=args[x].split(";");for(var y=0;y<_56c.length;y++){var si=_56c[y].indexOf(":");if((si!=-1)&&(_56c[y].length>si)){this[x][_56c[y].substr(0,si).replace(/^\s+|\s+$/g,"")]=_56c[y].substr(si+1);}}
}}else{this[x]=args[x];}}
}}
}}
}}
}else{this.extraArgs[x.toLowerCase()]=args[x];}}
},postMixInProperties:function(args,frag,_570){},initialize:function(args,frag,_573){return false;},postInitialize:function(args,frag,_576){return false;},postCreate:function(args,frag,_579){return false;},uninitialize:function(){return false;},buildRendering:function(args,frag,_57c){dojo.unimplemented("dojo.widget.Widget.buildRendering, on "+this.toString()+", ");return false;},destroyRendering:function(){dojo.unimplemented("dojo.widget.Widget.destroyRendering");return false;},addedTo:function(_57d){},addChild:function(_57e){dojo.unimplemented("dojo.widget.Widget.addChild");return false;},removeChild:function(_57f){for(var x=0;x<this.children.length;x++){if(this.children[x]===_57f){this.children.splice(x,1);_57f.parent=null;break;}}
return _57f;},getPreviousSibling:function(){var idx=this.getParentIndex();if(idx<=0){return null;}
return this.parent.children[idx-1];},getSiblings:function(){return this.parent.children;},getParentIndex:function(){return dojo.lang.indexOf(this.parent.children,this,true);},getNextSibling:function(){var idx=this.getParentIndex();if(idx==this.parent.children.length-1){return null;}
if(idx<0){return null;}
return this.parent.children[idx+1];}});dojo.widget.lcArgsCache={};dojo.widget.tags={};dojo.widget.tags["dojo:propertyset"]=function(_583,_584,_585){var _586=_584.parseProperties(_583["dojo:propertyset"]);};dojo.widget.tags["dojo:connect"]=function(_587,_588,_589){var _58a=_588.parseProperties(_587["dojo:connect"]);};dojo.widget.buildWidgetFromParseTree=function(type,frag,_58d,_58e,_58f,_590){dojo.a11y.setAccessibleMode();var _591=type.split(":");_591=(_591.length==2)?_591[1]:type;var _592=_590||_58d.parseProperties(frag[frag["ns"]+":"+_591]);var _593=dojo.widget.manager.getImplementation(_591,null,null,frag["ns"]);if(!_593){throw new Error("cannot find \""+type+"\" widget");}else{if(!_593.create){throw new Error("\""+type+"\" widget object has no \"create\" method and does not appear to implement *Widget");}}
_592["dojoinsertionindex"]=_58f;var ret=_593.create(_592,frag,_58e,frag["ns"]);return ret;};dojo.widget.defineWidget=function(_595,_596,_597,init,_599){if(dojo.lang.isString(arguments[3])){dojo.widget._defineWidget(arguments[0],arguments[3],arguments[1],arguments[4],arguments[2]);}else{var args=[arguments[0]],p=3;if(dojo.lang.isString(arguments[1])){args.push(arguments[1],arguments[2]);}else{args.push("",arguments[1]);p=2;}
if(dojo.lang.isFunction(arguments[p])){args.push(arguments[p],arguments[p+1]);}else{args.push(null,arguments[p]);}
dojo.widget._defineWidget.apply(this,args);}};dojo.widget.defineWidget.renderers="html|svg|vml";dojo.widget._defineWidget=function(_59c,_59d,_59e,init,_5a0){var _5a1=_59c.split(".");var type=_5a1.pop();var regx="\\.("+(_59d?_59d+"|":"")+dojo.widget.defineWidget.renderers+")\\.";var r=_59c.search(new RegExp(regx));_5a1=(r<0?_5a1.join("."):_59c.substr(0,r));dojo.widget.manager.registerWidgetPackage(_5a1);var pos=_5a1.indexOf(".");var _5a6=(pos>-1)?_5a1.substring(0,pos):_5a1;_5a0=(_5a0)||{};_5a0.widgetType=type;if((!init)&&(_5a0["classConstructor"])){init=_5a0.classConstructor;delete _5a0.classConstructor;}
dojo.declare(_59c,_59e,init,_5a0);};dojo.provide("dojo.widget.Parse");dojo.widget.Parse=function(_5a7){this.propertySetsList=[];this.fragment=_5a7;this.createComponents=function(frag,_5a9){var _5aa=[];var _5ab=false;try{if(frag&&frag.tagName&&(frag!=frag.nodeRef)){var _5ac=dojo.widget.tags;var tna=String(frag.tagName).split(";");for(var x=0;x<tna.length;x++){var ltn=tna[x].replace(/^\s+|\s+$/g,"").toLowerCase();frag.tagName=ltn;var ret;if(_5ac[ltn]){_5ab=true;ret=_5ac[ltn](frag,this,_5a9,frag.index);_5aa.push(ret);}else{if(ltn.indexOf(":")==-1){ltn="dojo:"+ltn;}
ret=dojo.widget.buildWidgetFromParseTree(ltn,frag,this,_5a9,frag.index);if(ret){_5ab=true;_5aa.push(ret);}}
}}
}
catch(e){dojo.debug("dojo.widget.Parse: error:",e);}
if(!_5ab){_5aa=_5aa.concat(this.createSubComponents(frag,_5a9));}
return _5aa;};this.createSubComponents=function(_5b1,_5b2){var frag,_5b4=[];for(var item in _5b1){frag=_5b1[item];if(frag&&typeof frag=="object"&&(frag!=_5b1.nodeRef)&&(frag!=_5b1.tagName)&&(item.indexOf("$")==-1)){_5b4=_5b4.concat(this.createComponents(frag,_5b2));}}
return _5b4;};this.parsePropertySets=function(_5b6){return [];};this.parseProperties=function(_5b7){var _5b8={};for(var item in _5b7){if((_5b7[item]==_5b7.tagName)||(_5b7[item]==_5b7.nodeRef)){}else{var frag=_5b7[item];if(frag.tagName&&dojo.widget.tags[frag.tagName.toLowerCase()]){}else{if(frag[0]&&frag[0].value!=""&&frag[0].value!=null){try{if(item.toLowerCase()=="dataprovider"){var _5bb=this;this.getDataProvider(_5bb,frag[0].value);_5b8.dataProvider=this.dataProvider;}
_5b8[item]=frag[0].value;var _5bc=this.parseProperties(frag);for(var _5bd in _5bc){_5b8[_5bd]=_5bc[_5bd];}}
catch(e){dojo.debug(e);}}
}
switch(item.toLowerCase()){case "checked":
case "disabled":
if(typeof _5b8[item]!="boolean"){_5b8[item]=true;}
break;}}
}
return _5b8;};this.getDataProvider=function(_5be,_5bf){dojo.io.bind({url:_5bf,load:function(type,_5c1){if(type=="load"){_5be.dataProvider=_5c1;}},mimetype:"text/javascript",sync:true});};this.getPropertySetById=function(_5c2){for(var x=0;x<this.propertySetsList.length;x++){if(_5c2==this.propertySetsList[x]["id"][0].value){return this.propertySetsList[x];}}
return "";};this.getPropertySetsByType=function(_5c4){var _5c5=[];for(var x=0;x<this.propertySetsList.length;x++){var cpl=this.propertySetsList[x];var cpcc=cpl.componentClass||cpl.componentType||null;var _5c9=this.propertySetsList[x]["id"][0].value;if(cpcc&&(_5c9==cpcc[0].value)){_5c5.push(cpl);}}
return _5c5;};this.getPropertySets=function(_5ca){var ppl="dojo:propertyproviderlist";var _5cc=[];var _5cd=_5ca.tagName;if(_5ca[ppl]){var _5ce=_5ca[ppl].value.split(" ");for(var _5cf in _5ce){if((_5cf.indexOf("..")==-1)&&(_5cf.indexOf("://")==-1)){var _5d0=this.getPropertySetById(_5cf);if(_5d0!=""){_5cc.push(_5d0);}}else{}}
}
return this.getPropertySetsByType(_5cd).concat(_5cc);};this.createComponentFromScript=function(_5d1,_5d2,_5d3,ns){_5d3.fastMixIn=true;var ltn=(ns||"dojo")+":"+_5d2.toLowerCase();if(dojo.widget.tags[ltn]){return [dojo.widget.tags[ltn](_5d3,this,null,null,_5d3)];}
return [dojo.widget.buildWidgetFromParseTree(ltn,_5d3,this,null,null,_5d3)];};};dojo.widget._parser_collection={"dojo":new dojo.widget.Parse()};dojo.widget.getParser=function(name){if(!name){name="dojo";}
if(!this._parser_collection[name]){this._parser_collection[name]=new dojo.widget.Parse();}
return this._parser_collection[name];};dojo.widget.createWidget=function(name,_5d8,_5d9,_5da){var _5db=false;var _5dc=(typeof name=="string");if(_5dc){var pos=name.indexOf(":");var ns=(pos>-1)?name.substring(0,pos):"dojo";if(pos>-1){name=name.substring(pos+1);}
var _5df=name.toLowerCase();var _5e0=ns+":"+_5df;_5db=(dojo.byId(name)&&!dojo.widget.tags[_5e0]);}
if((arguments.length==1)&&(_5db||!_5dc)){var xp=new dojo.xml.Parse();var tn=_5db?dojo.byId(name):name;return dojo.widget.getParser().createComponents(xp.parseElement(tn,null,true))[0];}
function fromScript(_5e3,name,_5e5,ns){_5e5[_5e0]={dojotype:[{value:_5df}],nodeRef:_5e3,fastMixIn:true};_5e5.ns=ns;return dojo.widget.getParser().createComponentFromScript(_5e3,name,_5e5,ns);}
_5d8=_5d8||{};var _5e7=false;var tn=null;var h=dojo.render.html.capable;if(h){tn=document.createElement("span");}
if(!_5d9){_5e7=true;_5d9=tn;if(h){dojo.body().appendChild(_5d9);}}else{if(_5da){dojo.dom.insertAtPosition(tn,_5d9,_5da);}else{tn=_5d9;}}
var _5e9=fromScript(tn,name.toLowerCase(),_5d8,ns);if((!_5e9)||(!_5e9[0])||(typeof _5e9[0].widgetType=="undefined")){throw new Error("createWidget: Creation of \""+name+"\" widget failed.");}
try{if(_5e7&&_5e9[0].domNode.parentNode){_5e9[0].domNode.parentNode.removeChild(_5e9[0].domNode);}}
catch(e){dojo.debug(e);}
return _5e9[0];};dojo.provide("dojo.html.style");dojo.html.getClass=function(node){node=dojo.byId(node);if(!node){return "";}
var cs="";if(node.className){cs=node.className;}else{if(dojo.html.hasAttribute(node,"class")){cs=dojo.html.getAttribute(node,"class");}}
return cs.replace(/^\s+|\s+$/g,"");};dojo.html.getClasses=function(node){var c=dojo.html.getClass(node);return (c=="")?[]:c.split(/\s+/g);};dojo.html.hasClass=function(node,_5ef){return (new RegExp("(^|\\s+)"+_5ef+"(\\s+|$)")).test(dojo.html.getClass(node));};dojo.html.prependClass=function(node,_5f1){_5f1+=" "+dojo.html.getClass(node);return dojo.html.setClass(node,_5f1);};dojo.html.addClass=function(node,_5f3){if(dojo.html.hasClass(node,_5f3)){return false;}
_5f3=(dojo.html.getClass(node)+" "+_5f3).replace(/^\s+|\s+$/g,"");return dojo.html.setClass(node,_5f3);};dojo.html.setClass=function(node,_5f5){node=dojo.byId(node);var cs=new String(_5f5);try{if(typeof node.className=="string"){node.className=cs;}else{if(node.setAttribute){node.setAttribute("class",_5f5);node.className=cs;}else{return false;}}
}
catch(e){dojo.debug("dojo.html.setClass() failed",e);}
return true;};dojo.html.removeClass=function(node,_5f8,_5f9){try{if(!_5f9){var _5fa=dojo.html.getClass(node).replace(new RegExp("(^|\\s+)"+_5f8+"(\\s+|$)"),"$1$2");}else{var _5fa=dojo.html.getClass(node).replace(_5f8,"");}
dojo.html.setClass(node,_5fa);}
catch(e){dojo.debug("dojo.html.removeClass() failed",e);}
return true;};dojo.html.replaceClass=function(node,_5fc,_5fd){dojo.html.removeClass(node,_5fd);dojo.html.addClass(node,_5fc);};dojo.html.classMatchType={ContainsAll:0,ContainsAny:1,IsOnly:2};dojo.html.getElementsByClass=function(_5fe,_5ff,_600,_601,_602){_602=false;var _603=dojo.doc();_5ff=dojo.byId(_5ff)||_603;var _604=_5fe.split(/\s+/g);var _605=[];if(_601!=1&&_601!=2){_601=0;}
var _606=new RegExp("(\\s|^)(("+_604.join(")|(")+"))(\\s|$)");var _607=_604.join(" ").length;var _608=[];if(!_602&&_603.evaluate){var _609=".//"+(_600||"*")+"[contains(";if(_601!=dojo.html.classMatchType.ContainsAny){_609+="concat(' ',@class,' '), ' "+_604.join(" ') and contains(concat(' ',@class,' '), ' ")+" ')";if(_601==2){_609+=" and string-length(@class)="+_607+"]";}else{_609+="]";}}else{_609+="concat(' ',@class,' '), ' "+_604.join(" ') or contains(concat(' ',@class,' '), ' ")+" ')]";}
var _60a=_603.evaluate(_609,_5ff,null,XPathResult.ANY_TYPE,null);var _60b=_60a.iterateNext();while(_60b){try{_608.push(_60b);_60b=_60a.iterateNext();}
catch(e){break;}}
return _608;}else{if(!_600){_600="*";}
_608=_5ff.getElementsByTagName(_600);var node,i=0;outer:
while(node=_608[i++]){var _60e=dojo.html.getClasses(node);if(_60e.length==0){continue outer;}
var _60f=0;for(var j=0;j<_60e.length;j++){if(_606.test(_60e[j])){if(_601==dojo.html.classMatchType.ContainsAny){_605.push(node);continue outer;}else{_60f++;}}else{if(_601==dojo.html.classMatchType.IsOnly){continue outer;}}
}
if(_60f==_604.length){if((_601==dojo.html.classMatchType.IsOnly)&&(_60f==_60e.length)){_605.push(node);}else{if(_601==dojo.html.classMatchType.ContainsAll){_605.push(node);}}
}}
return _605;}};dojo.html.getElementsByClassName=dojo.html.getElementsByClass;dojo.html.toCamelCase=function(_611){var arr=_611.split("-"),cc=arr[0];for(var i=1;i<arr.length;i++){cc+=arr[i].charAt(0).toUpperCase()+arr[i].substring(1);}
return cc;};dojo.html.toSelectorCase=function(_615){return _615.replace(/([A-Z])/g,"-$1").toLowerCase();};dojo.html.getComputedStyle=function(node,_617,_618){node=dojo.byId(node);var _617=dojo.html.toSelectorCase(_617);var _619=dojo.html.toCamelCase(_617);if(!node||!node.style){return _618;}else{if(document.defaultView&&dojo.html.isDescendantOf(node,node.ownerDocument)){try{var cs=document.defaultView.getComputedStyle(node,"");if(cs){return cs.getPropertyValue(_617);}}
catch(e){if(node.style.getPropertyValue){return node.style.getPropertyValue(_617);}else{return _618;}}
}else{if(node.currentStyle){return node.currentStyle[_619];}}
}
if(node.style.getPropertyValue){return node.style.getPropertyValue(_617);}else{return _618;}};dojo.html.getStyleProperty=function(node,_61c){node=dojo.byId(node);return (node&&node.style?node.style[dojo.html.toCamelCase(_61c)]:undefined);};dojo.html.getStyle=function(node,_61e){var _61f=dojo.html.getStyleProperty(node,_61e);return (_61f?_61f:dojo.html.getComputedStyle(node,_61e));};dojo.html.setStyle=function(node,_621,_622){node=dojo.byId(node);if(node&&node.style){var _623=dojo.html.toCamelCase(_621);node.style[_623]=_622;}};dojo.html.setStyleText=function(_624,text){try{_624.style.cssText=text;}
catch(e){_624.setAttribute("style",text);}};dojo.html.copyStyle=function(_626,_627){if(!_627.style.cssText){_626.setAttribute("style",_627.getAttribute("style"));}else{_626.style.cssText=_627.style.cssText;}
dojo.html.addClass(_626,dojo.html.getClass(_627));};dojo.html.getUnitValue=function(node,_629,_62a){var s=dojo.html.getComputedStyle(node,_629);if((!s)||((s=="auto")&&(_62a))){return {value:0,units:"px"};}
var _62c=s.match(/(\-?[\d.]+)([a-z%]*)/i);if(!_62c){return dojo.html.getUnitValue.bad;}
return {value:Number(_62c[1]),units:_62c[2].toLowerCase()};};dojo.html.getUnitValue.bad={value:NaN,units:""};dojo.html.getPixelValue=function(node,_62e,_62f){var _630=dojo.html.getUnitValue(node,_62e,_62f);if(isNaN(_630.value)){return 0;}
if((_630.value)&&(_630.units!="px")){return NaN;}
return _630.value;};dojo.html.setPositivePixelValue=function(node,_632,_633){if(isNaN(_633)){return false;}
node.style[_632]=Math.max(0,_633)+"px";return true;};dojo.html.styleSheet=null;dojo.html.insertCssRule=function(_634,_635,_636){if(!dojo.html.styleSheet){if(document.createStyleSheet){dojo.html.styleSheet=document.createStyleSheet();}else{if(document.styleSheets[0]){dojo.html.styleSheet=document.styleSheets[0];}else{return null;}}
}
if(arguments.length<3){if(dojo.html.styleSheet.cssRules){_636=dojo.html.styleSheet.cssRules.length;}else{if(dojo.html.styleSheet.rules){_636=dojo.html.styleSheet.rules.length;}else{return null;}}
}
if(dojo.html.styleSheet.insertRule){var rule=_634+" { "+_635+" }";return dojo.html.styleSheet.insertRule(rule,_636);}else{if(dojo.html.styleSheet.addRule){return dojo.html.styleSheet.addRule(_634,_635,_636);}else{return null;}}
};dojo.html.removeCssRule=function(_638){if(!dojo.html.styleSheet){dojo.debug("no stylesheet defined for removing rules");return false;}
if(dojo.render.html.ie){if(!_638){_638=dojo.html.styleSheet.rules.length;dojo.html.styleSheet.removeRule(_638);}}else{if(document.styleSheets[0]){if(!_638){_638=dojo.html.styleSheet.cssRules.length;}
dojo.html.styleSheet.deleteRule(_638);}}
return true;};dojo.html._insertedCssFiles=[];dojo.html.insertCssFile=function(URI,doc,_63b,_63c){if(!URI){return;}
if(!doc){doc=document;}
var _63d=dojo.hostenv.getText(URI,false,_63c);if(_63d===null){return;}
_63d=dojo.html.fixPathsInCssText(_63d,URI);if(_63b){var idx=-1,node,ent=dojo.html._insertedCssFiles;for(var i=0;i<ent.length;i++){if((ent[i].doc==doc)&&(ent[i].cssText==_63d)){idx=i;node=ent[i].nodeRef;break;}}
if(node){var _642=doc.getElementsByTagName("style");for(var i=0;i<_642.length;i++){if(_642[i]==node){return;}}
dojo.html._insertedCssFiles.shift(idx,1);}}
var _643=dojo.html.insertCssText(_63d,doc);dojo.html._insertedCssFiles.push({"doc":doc,"cssText":_63d,"nodeRef":_643});if(_643&&djConfig.isDebug){_643.setAttribute("dbgHref",URI);}
return _643;};dojo.html.insertCssText=function(_644,doc,URI){if(!_644){return;}
if(!doc){doc=document;}
if(URI){_644=dojo.html.fixPathsInCssText(_644,URI);}
var _647=doc.createElement("style");_647.setAttribute("type","text/css");var head=doc.getElementsByTagName("head")[0];if(!head){dojo.debug("No head tag in document, aborting styles");return;}else{head.appendChild(_647);}
if(_647.styleSheet){var _649=function(){try{_647.styleSheet.cssText=_644;}
catch(e){dojo.debug(e);}};if(_647.styleSheet.disabled){setTimeout(_649,10);}else{_649();}}else{var _64a=doc.createTextNode(_644);_647.appendChild(_64a);}
return _647;};dojo.html.fixPathsInCssText=function(_64b,URI){if(!_64b||!URI){return;}
var _64d,str="",url="",_650="[\\t\\s\\w\\(\\)\\/\\.\\\\'\"-:#=&?~]+";var _651=new RegExp("url\\(\\s*("+_650+")\\s*\\)");var _652=/(file|https?|ftps?):\/\//;regexTrim=new RegExp("^[\\s]*(['\"]?)("+_650+")\\1[\\s]*?$");if(dojo.render.html.ie55||dojo.render.html.ie60){var _653=new RegExp("AlphaImageLoader\\((.*)src=['\"]("+_650+")['\"]");while(_64d=_653.exec(_64b)){url=_64d[2].replace(regexTrim,"$2");if(!_652.exec(url)){url=(new dojo.uri.Uri(URI,url).toString());}
str+=_64b.substring(0,_64d.index)+"AlphaImageLoader("+_64d[1]+"src='"+url+"'";_64b=_64b.substr(_64d.index+_64d[0].length);}
_64b=str+_64b;str="";}
while(_64d=_651.exec(_64b)){url=_64d[1].replace(regexTrim,"$2");if(!_652.exec(url)){url=(new dojo.uri.Uri(URI,url).toString());}
str+=_64b.substring(0,_64d.index)+"url("+url+")";_64b=_64b.substr(_64d.index+_64d[0].length);}
return str+_64b;};dojo.html.setActiveStyleSheet=function(_654){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")){a.disabled=true;if(a.getAttribute("title")==_654){a.disabled=false;}}
}};dojo.html.getActiveStyleSheet=function(){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")&&!a.disabled){return a.getAttribute("title");}}
return null;};dojo.html.getPreferredStyleSheet=function(){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("rel").indexOf("alt")==-1&&a.getAttribute("title")){return a.getAttribute("title");}}
return null;};dojo.html.applyBrowserClass=function(node){var drh=dojo.render.html;var _660={dj_ie:drh.ie,dj_ie55:drh.ie55,dj_ie6:drh.ie60,dj_ie7:drh.ie70,dj_iequirks:drh.ie&&drh.quirks,dj_opera:drh.opera,dj_opera8:drh.opera&&(Math.floor(dojo.render.version)==8),dj_opera9:drh.opera&&(Math.floor(dojo.render.version)==9),dj_khtml:drh.khtml,dj_safari:drh.safari,dj_gecko:drh.mozilla};for(var p in _660){if(_660[p]){dojo.html.addClass(node,p);}}
};dojo.provide("dojo.widget.DomWidget");dojo.widget._cssFiles={};dojo.widget._cssStrings={};dojo.widget._templateCache={};dojo.widget.defaultStrings={dojoRoot:dojo.hostenv.getBaseScriptUri(),baseScriptUri:dojo.hostenv.getBaseScriptUri()};dojo.widget.fillFromTemplateCache=function(obj,_663,_664,_665){var _666=_663||obj.templatePath;var _667=dojo.widget._templateCache;if(!_666&&!obj["widgetType"]){do{var _668="__dummyTemplate__"+dojo.widget._templateCache.dummyCount++;}while(_667[_668]);obj.widgetType=_668;}
var wt=_666?_666.toString():obj.widgetType;var ts=_667[wt];if(!ts){_667[wt]={"string":null,"node":null};if(_665){ts={};}else{ts=_667[wt];}}
if((!obj.templateString)&&(!_665)){obj.templateString=_664||ts["string"];}
if((!obj.templateNode)&&(!_665)){obj.templateNode=ts["node"];}
if((!obj.templateNode)&&(!obj.templateString)&&(_666)){var _66b=dojo.hostenv.getText(_666);if(_66b){_66b=_66b.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im,"");var _66c=_66b.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);if(_66c){_66b=_66c[1];}}else{_66b="";}
obj.templateString=_66b;if(!_665){_667[wt]["string"]=_66b;}}
if((!ts["string"])&&(!_665)){ts.string=obj.templateString;}};dojo.widget._templateCache.dummyCount=0;dojo.widget.attachProperties=["dojoAttachPoint","id"];dojo.widget.eventAttachProperty="dojoAttachEvent";dojo.widget.onBuildProperty="dojoOnBuild";dojo.widget.waiNames=["waiRole","waiState"];dojo.widget.wai={waiRole:{name:"waiRole","namespace":"http://www.w3.org/TR/xhtml2",alias:"x2",prefix:"wairole:"},waiState:{name:"waiState","namespace":"http://www.w3.org/2005/07/aaa",alias:"aaa",prefix:""},setAttr:function(node,ns,attr,_670){if(dojo.render.html.ie){node.setAttribute(this[ns].alias+":"+attr,this[ns].prefix+_670);}else{node.setAttributeNS(this[ns]["namespace"],attr,this[ns].prefix+_670);}},getAttr:function(node,ns,attr){if(dojo.render.html.ie){return node.getAttribute(this[ns].alias+":"+attr);}else{return node.getAttributeNS(this[ns]["namespace"],attr);}},removeAttr:function(node,ns,attr){var _677=true;if(dojo.render.html.ie){_677=node.removeAttribute(this[ns].alias+":"+attr);}else{node.removeAttributeNS(this[ns]["namespace"],attr);}
return _677;}};dojo.widget.attachTemplateNodes=function(_678,_679,_67a){var _67b=dojo.dom.ELEMENT_NODE;function trim(str){return str.replace(/^\s+|\s+$/g,"");}
if(!_678){_678=_679.domNode;}
if(_678.nodeType!=_67b){return;}
var _67d=_678.all||_678.getElementsByTagName("*");var _67e=_679;for(var x=-1;x<_67d.length;x++){var _680=(x==-1)?_678:_67d[x];var _681=[];if(!_679.widgetsInTemplate||!_680.getAttribute("dojoType")){for(var y=0;y<this.attachProperties.length;y++){var _683=_680.getAttribute(this.attachProperties[y]);if(_683){_681=_683.split(";");for(var z=0;z<_681.length;z++){if(dojo.lang.isArray(_679[_681[z]])){_679[_681[z]].push(_680);}else{_679[_681[z]]=_680;}}
break;}}
var _685=_680.getAttribute(this.eventAttachProperty);if(_685){var evts=_685.split(";");for(var y=0;y<evts.length;y++){if((!evts[y])||(!evts[y].length)){continue;}
var _687=null;var tevt=trim(evts[y]);if(evts[y].indexOf(":")>=0){var _689=tevt.split(":");tevt=trim(_689[0]);_687=trim(_689[1]);}
if(!_687){_687=tevt;}
var tf=function(){var ntf=new String(_687);return function(evt){if(_67e[ntf]){_67e[ntf](dojo.event.browser.fixEvent(evt,this));}};}();dojo.event.browser.addListener(_680,tevt,tf,false,true);}}
for(var y=0;y<_67a.length;y++){var _68d=_680.getAttribute(_67a[y]);if((_68d)&&(_68d.length)){var _687=null;var _68e=_67a[y].substr(4);_687=trim(_68d);var _68f=[_687];if(_687.indexOf(";")>=0){_68f=dojo.lang.map(_687.split(";"),trim);}
for(var z=0;z<_68f.length;z++){if(!_68f[z].length){continue;}
var tf=function(){var ntf=new String(_68f[z]);return function(evt){if(_67e[ntf]){_67e[ntf](dojo.event.browser.fixEvent(evt,this));}};}();dojo.event.browser.addListener(_680,_68e,tf,false,true);}}
}}
var _692=_680.getAttribute(this.templateProperty);if(_692){_679[_692]=_680;}
dojo.lang.forEach(dojo.widget.waiNames,function(name){var wai=dojo.widget.wai[name];var val=_680.getAttribute(wai.name);if(val){if(val.indexOf("-")==-1){dojo.widget.wai.setAttr(_680,wai.name,"role",val);}else{var _696=val.split("-");dojo.widget.wai.setAttr(_680,wai.name,_696[0],_696[1]);}}
},this);var _697=_680.getAttribute(this.onBuildProperty);if(_697){eval("var node = baseNode; var widget = targetObj; "+_697);}}
};dojo.widget.getDojoEventsFromStr=function(str){var re=/(dojoOn([a-z]+)(\s?))=/gi;var evts=str?str.match(re)||[]:[];var ret=[];var lem={};for(var x=0;x<evts.length;x++){if(evts[x].length<1){continue;}
var cm=evts[x].replace(/\s/,"");cm=(cm.slice(0,cm.length-1));if(!lem[cm]){lem[cm]=true;ret.push(cm);}}
return ret;};dojo.declare("dojo.widget.DomWidget",dojo.widget.Widget,function(){if((arguments.length>0)&&(typeof arguments[0]=="object")){this.create(arguments[0]);}},{templateNode:null,templateString:null,templateCssString:null,preventClobber:false,domNode:null,containerNode:null,widgetsInTemplate:false,addChild:function(_69f,_6a0,pos,ref,_6a3){if(!this.isContainer){dojo.debug("dojo.widget.DomWidget.addChild() attempted on non-container widget");return null;}else{if(_6a3==undefined){_6a3=this.children.length;}
this.addWidgetAsDirectChild(_69f,_6a0,pos,ref,_6a3);this.registerChild(_69f,_6a3);}
return _69f;},addWidgetAsDirectChild:function(_6a4,_6a5,pos,ref,_6a8){if((!this.containerNode)&&(!_6a5)){this.containerNode=this.domNode;}
var cn=(_6a5)?_6a5:this.containerNode;if(!pos){pos="after";}
if(!ref){if(!cn){cn=dojo.body();}
ref=cn.lastChild;}
if(!_6a8){_6a8=0;}
_6a4.domNode.setAttribute("dojoinsertionindex",_6a8);if(!ref){cn.appendChild(_6a4.domNode);}else{if(pos=="insertAtIndex"){dojo.dom.insertAtIndex(_6a4.domNode,ref.parentNode,_6a8);}else{if((pos=="after")&&(ref===cn.lastChild)){cn.appendChild(_6a4.domNode);}else{dojo.dom.insertAtPosition(_6a4.domNode,cn,pos);}}
}},registerChild:function(_6aa,_6ab){_6aa.dojoInsertionIndex=_6ab;var idx=-1;for(var i=0;i<this.children.length;i++){if(this.children[i].dojoInsertionIndex<=_6ab){idx=i;}}
this.children.splice(idx+1,0,_6aa);_6aa.parent=this;_6aa.addedTo(this,idx+1);delete dojo.widget.manager.topWidgets[_6aa.widgetId];},removeChild:function(_6ae){dojo.dom.removeNode(_6ae.domNode);return dojo.widget.DomWidget.superclass.removeChild.call(this,_6ae);},getFragNodeRef:function(frag){if(!frag){return null;}
if(!frag[this.getNamespacedType()]){dojo.raise("Error: no frag for widget type "+this.getNamespacedType()+", id "+this.widgetId+" (maybe a widget has set it's type incorrectly)");}
return frag[this.getNamespacedType()]["nodeRef"];},postInitialize:function(args,frag,_6b2){var _6b3=this.getFragNodeRef(frag);if(_6b2&&(_6b2.snarfChildDomOutput||!_6b3)){_6b2.addWidgetAsDirectChild(this,"","insertAtIndex","",args["dojoinsertionindex"],_6b3);}else{if(_6b3){if(this.domNode&&(this.domNode!==_6b3)){this._sourceNodeRef=dojo.dom.replaceNode(_6b3,this.domNode);}}
}
if(_6b2){_6b2.registerChild(this,args.dojoinsertionindex);}else{dojo.widget.manager.topWidgets[this.widgetId]=this;}
if(this.widgetsInTemplate){var _6b4=new dojo.xml.Parse();var _6b5;var _6b6=this.domNode.getElementsByTagName("*");for(var i=0;i<_6b6.length;i++){if(_6b6[i].getAttribute("dojoAttachPoint")=="subContainerWidget"){_6b5=_6b6[i];}
if(_6b6[i].getAttribute("dojoType")){_6b6[i].setAttribute("isSubWidget",true);}}
if(this.isContainer&&!this.containerNode){if(_6b5){var src=this.getFragNodeRef(frag);if(src){dojo.dom.moveChildren(src,_6b5);frag["dojoDontFollow"]=true;}}else{dojo.debug("No subContainerWidget node can be found in template file for widget "+this);}}
var _6b9=_6b4.parseElement(this.domNode,null,true);dojo.widget.getParser().createSubComponents(_6b9,this);var _6ba=[];var _6bb=[this];var w;while((w=_6bb.pop())){for(var i=0;i<w.children.length;i++){var _6bd=w.children[i];if(_6bd._processedSubWidgets||!_6bd.extraArgs["issubwidget"]){continue;}
_6ba.push(_6bd);if(_6bd.isContainer){_6bb.push(_6bd);}}
}
for(var i=0;i<_6ba.length;i++){var _6be=_6ba[i];if(_6be._processedSubWidgets){dojo.debug("This should not happen: widget._processedSubWidgets is already true!");return;}
_6be._processedSubWidgets=true;if(_6be.extraArgs["dojoattachevent"]){var evts=_6be.extraArgs["dojoattachevent"].split(";");for(var j=0;j<evts.length;j++){var _6c1=null;var tevt=dojo.string.trim(evts[j]);if(tevt.indexOf(":")>=0){var _6c3=tevt.split(":");tevt=dojo.string.trim(_6c3[0]);_6c1=dojo.string.trim(_6c3[1]);}
if(!_6c1){_6c1=tevt;}
if(dojo.lang.isFunction(_6be[tevt])){dojo.event.kwConnect({srcObj:_6be,srcFunc:tevt,targetObj:this,targetFunc:_6c1});}else{alert(tevt+" is not a function in widget "+_6be);}}
}
if(_6be.extraArgs["dojoattachpoint"]){this[_6be.extraArgs["dojoattachpoint"]]=_6be;}}
}
if(this.isContainer&&!frag["dojoDontFollow"]){dojo.widget.getParser().createSubComponents(frag,this);}},buildRendering:function(args,frag){var ts=dojo.widget._templateCache[this.widgetType];if(args["templatecsspath"]){args["templateCssPath"]=args["templatecsspath"];}
var _6c7=args["templateCssPath"]||this.templateCssPath;if(_6c7&&!dojo.widget._cssFiles[_6c7.toString()]){if((!this.templateCssString)&&(_6c7)){this.templateCssString=dojo.hostenv.getText(_6c7);this.templateCssPath=null;}
dojo.widget._cssFiles[_6c7.toString()]=true;}
if((this["templateCssString"])&&(!dojo.widget._cssStrings[this.templateCssString])){dojo.html.insertCssText(this.templateCssString,null,_6c7);dojo.widget._cssStrings[this.templateCssString]=true;}
if((!this.preventClobber)&&((this.templatePath)||(this.templateNode)||((this["templateString"])&&(this.templateString.length))||((typeof ts!="undefined")&&((ts["string"])||(ts["node"]))))){this.buildFromTemplate(args,frag);}else{this.domNode=this.getFragNodeRef(frag);}
this.fillInTemplate(args,frag);},buildFromTemplate:function(args,frag){var _6ca=false;if(args["templatepath"]){args["templatePath"]=args["templatepath"];}
dojo.widget.fillFromTemplateCache(this,args["templatePath"],null,_6ca);var ts=dojo.widget._templateCache[this.templatePath?this.templatePath.toString():this.widgetType];if((ts)&&(!_6ca)){if(!this.templateString.length){this.templateString=ts["string"];}
if(!this.templateNode){this.templateNode=ts["node"];}}
var _6cc=false;var node=null;var tstr=this.templateString;if((!this.templateNode)&&(this.templateString)){_6cc=this.templateString.match(/\$\{([^\}]+)\}/g);if(_6cc){var hash=this.strings||{};for(var key in dojo.widget.defaultStrings){if(dojo.lang.isUndefined(hash[key])){hash[key]=dojo.widget.defaultStrings[key];}}
for(var i=0;i<_6cc.length;i++){var key=_6cc[i];key=key.substring(2,key.length-1);var kval=(key.substring(0,5)=="this.")?dojo.lang.getObjPathValue(key.substring(5),this):hash[key];var _6d3;if((kval)||(dojo.lang.isString(kval))){_6d3=new String((dojo.lang.isFunction(kval))?kval.call(this,key,this.templateString):kval);while(_6d3.indexOf("\"")>-1){_6d3=_6d3.replace("\"","&quot;");}
tstr=tstr.replace(_6cc[i],_6d3);}}
}else{this.templateNode=this.createNodesFromText(this.templateString,true)[0];if(!_6ca){ts.node=this.templateNode;}}
}
if((!this.templateNode)&&(!_6cc)){dojo.debug("DomWidget.buildFromTemplate: could not create template");return false;}else{if(!_6cc){node=this.templateNode.cloneNode(true);if(!node){return false;}}else{node=this.createNodesFromText(tstr,true)[0];}}
this.domNode=node;this.attachTemplateNodes();if(this.isContainer&&this.containerNode){var src=this.getFragNodeRef(frag);if(src){dojo.dom.moveChildren(src,this.containerNode);}}
},attachTemplateNodes:function(_6d5,_6d6){if(!_6d5){_6d5=this.domNode;}
if(!_6d6){_6d6=this;}
return dojo.widget.attachTemplateNodes(_6d5,_6d6,dojo.widget.getDojoEventsFromStr(this.templateString));},fillInTemplate:function(){},destroyRendering:function(){try{dojo.dom.destroyNode(this.domNode);delete this.domNode;}
catch(e){}
if(this._sourceNodeRef){try{dojo.dom.destroyNode(this._sourceNodeRef);}
catch(e){}}
},createNodesFromText:function(){dojo.unimplemented("dojo.widget.DomWidget.createNodesFromText");}});dojo.provide("dojo.html.display");dojo.html._toggle=function(node,_6d8,_6d9){node=dojo.byId(node);_6d9(node,!_6d8(node));return _6d8(node);};dojo.html.show=function(node){node=dojo.byId(node);if(dojo.html.getStyleProperty(node,"display")=="none"){dojo.html.setStyle(node,"display",(node.dojoDisplayCache||""));node.dojoDisplayCache=undefined;}};dojo.html.hide=function(node){node=dojo.byId(node);if(typeof node["dojoDisplayCache"]=="undefined"){var d=dojo.html.getStyleProperty(node,"display");if(d!="none"){node.dojoDisplayCache=d;}}
dojo.html.setStyle(node,"display","none");};dojo.html.setShowing=function(node,_6de){dojo.html[(_6de?"show":"hide")](node);};dojo.html.isShowing=function(node){return (dojo.html.getStyleProperty(node,"display")!="none");};dojo.html.toggleShowing=function(node){return dojo.html._toggle(node,dojo.html.isShowing,dojo.html.setShowing);};dojo.html.displayMap={tr:"",td:"",th:"",img:"inline",span:"inline",input:"inline",button:"inline"};dojo.html.suggestDisplayByTagName=function(node){node=dojo.byId(node);if(node&&node.tagName){var tag=node.tagName.toLowerCase();return (tag in dojo.html.displayMap?dojo.html.displayMap[tag]:"block");}};dojo.html.setDisplay=function(node,_6e4){dojo.html.setStyle(node,"display",((_6e4 instanceof String||typeof _6e4=="string")?_6e4:(_6e4?dojo.html.suggestDisplayByTagName(node):"none")));};dojo.html.isDisplayed=function(node){return (dojo.html.getComputedStyle(node,"display")!="none");};dojo.html.toggleDisplay=function(node){return dojo.html._toggle(node,dojo.html.isDisplayed,dojo.html.setDisplay);};dojo.html.setVisibility=function(node,_6e8){dojo.html.setStyle(node,"visibility",((_6e8 instanceof String||typeof _6e8=="string")?_6e8:(_6e8?"visible":"hidden")));};dojo.html.isVisible=function(node){return (dojo.html.getComputedStyle(node,"visibility")!="hidden");};dojo.html.toggleVisibility=function(node){return dojo.html._toggle(node,dojo.html.isVisible,dojo.html.setVisibility);};dojo.html.setOpacity=function(node,_6ec,_6ed){node=dojo.byId(node);var h=dojo.render.html;if(!_6ed){if(_6ec>=1){if(h.ie){dojo.html.clearOpacity(node);return;}else{_6ec=0.999999;}}else{if(_6ec<0){_6ec=0;}}
}
if(h.ie){if(node.nodeName.toLowerCase()=="tr"){var tds=node.getElementsByTagName("td");for(var x=0;x<tds.length;x++){tds[x].style.filter="Alpha(Opacity="+_6ec*100+")";}}
node.style.filter="Alpha(Opacity="+_6ec*100+")";}else{if(h.moz){node.style.opacity=_6ec;node.style.MozOpacity=_6ec;}else{if(h.safari){node.style.opacity=_6ec;node.style.KhtmlOpacity=_6ec;}else{node.style.opacity=_6ec;}}
}};dojo.html.clearOpacity=function(node){node=dojo.byId(node);var ns=node.style;var h=dojo.render.html;if(h.ie){try{if(node.filters&&node.filters.alpha){ns.filter="";}}
catch(e){}}else{if(h.moz){ns.opacity=1;ns.MozOpacity=1;}else{if(h.safari){ns.opacity=1;ns.KhtmlOpacity=1;}else{ns.opacity=1;}}
}};dojo.html.getOpacity=function(node){node=dojo.byId(node);var h=dojo.render.html;if(h.ie){var opac=(node.filters&&node.filters.alpha&&typeof node.filters.alpha.opacity=="number"?node.filters.alpha.opacity:100)/100;}else{var opac=node.style.opacity||node.style.MozOpacity||node.style.KhtmlOpacity||1;}
return opac>=0.999999?1:Number(opac);};dojo.provide("dojo.html.layout");dojo.html.sumAncestorProperties=function(node,prop){node=dojo.byId(node);if(!node){return 0;}
var _6f9=0;while(node){if(dojo.html.getComputedStyle(node,"position")=="fixed"){return 0;}
var val=node[prop];if(val){_6f9+=val-0;if(node==dojo.body()){break;}}
node=node.parentNode;}
return _6f9;};dojo.html.setStyleAttributes=function(node,_6fc){node=dojo.byId(node);var _6fd=_6fc.replace(/(;)?\s*$/,"").split(";");for(var i=0;i<_6fd.length;i++){var _6ff=_6fd[i].split(":");var name=_6ff[0].replace(/\s*$/,"").replace(/^\s*/,"").toLowerCase();var _701=_6ff[1].replace(/\s*$/,"").replace(/^\s*/,"");switch(name){case "opacity":
dojo.html.setOpacity(node,_701);break;case "content-height":
dojo.html.setContentBox(node,{height:_701});break;case "content-width":
dojo.html.setContentBox(node,{width:_701});break;case "outer-height":
dojo.html.setMarginBox(node,{height:_701});break;case "outer-width":
dojo.html.setMarginBox(node,{width:_701});break;default:
node.style[dojo.html.toCamelCase(name)]=_701;}}
};dojo.html.boxSizing={MARGIN_BOX:"margin-box",BORDER_BOX:"border-box",PADDING_BOX:"padding-box",CONTENT_BOX:"content-box"};dojo.html.getAbsolutePosition=dojo.html.abs=function(node,_703,_704){node=dojo.byId(node);var _705=dojo.doc();var ret={x:0,y:0};var bs=dojo.html.boxSizing;if(!_704){_704=bs.CONTENT_BOX;}
var _708=2;var _709;switch(_704){case bs.MARGIN_BOX:
_709=3;break;case bs.BORDER_BOX:
_709=2;break;case bs.PADDING_BOX:
default:
_709=1;break;case bs.CONTENT_BOX:
_709=0;break;}
var h=dojo.render.html;var db=_705["body"]||_705["documentElement"];if(h.ie){with(node.getBoundingClientRect()){ret.x=left-2;ret.y=top-2;}}else{if(_705["getBoxObjectFor"]){_708=1;try{var bo=_705.getBoxObjectFor(node);ret.x=bo.x-dojo.html.sumAncestorProperties(node,"scrollLeft");ret.y=bo.y-dojo.html.sumAncestorProperties(node,"scrollTop");}
catch(e){}}else{if(node["offsetParent"]){var _70d;if((h.safari)&&(node.style.getPropertyValue("position")=="absolute")&&(node.parentNode==db)){_70d=db;}else{_70d=db.parentNode;}
if(node.parentNode!=db){var nd=node;if(dojo.render.html.opera){nd=db;}
ret.x-=dojo.html.sumAncestorProperties(nd,"scrollLeft");ret.y-=dojo.html.sumAncestorProperties(nd,"scrollTop");}
var _70f=node;do{var n=_70f["offsetLeft"];if(!h.opera||n>0){ret.x+=isNaN(n)?0:n;}
var m=_70f["offsetTop"];ret.y+=isNaN(m)?0:m;_70f=_70f.offsetParent;}while((_70f!=_70d)&&(_70f!=null));}else{if(node["x"]&&node["y"]){ret.x+=isNaN(node.x)?0:node.x;ret.y+=isNaN(node.y)?0:node.y;}}
}}
if(_703){var _712=dojo.html.getScroll();ret.y+=_712.top;ret.x+=_712.left;}
var _713=[dojo.html.getPaddingExtent,dojo.html.getBorderExtent,dojo.html.getMarginExtent];if(_708>_709){for(var i=_709;i<_708;++i){ret.y+=_713[i](node,"top");ret.x+=_713[i](node,"left");}}else{if(_708<_709){for(var i=_709;i>_708;--i){ret.y-=_713[i-1](node,"top");ret.x-=_713[i-1](node,"left");}}
}
ret.top=ret.y;ret.left=ret.x;return ret;};dojo.html.isPositionAbsolute=function(node){return (dojo.html.getComputedStyle(node,"position")=="absolute");};dojo.html._getComponentPixelValues=function(node,_717,_718,_719){var _71a=["top","bottom","left","right"];var obj={};for(var i in _71a){side=_71a[i];obj[side]=_718(node,_717+side,_719);}
obj.width=obj.left+obj.right;obj.height=obj.top+obj.bottom;return obj;};dojo.html.getMargin=function(node){return dojo.html._getComponentPixelValues(node,"margin-",dojo.html.getPixelValue,dojo.html.isPositionAbsolute(node));};dojo.html.getBorder=function(node){return dojo.html._getComponentPixelValues(node,"",dojo.html.getBorderExtent);};dojo.html.getBorderExtent=function(node,side){return (dojo.html.getStyle(node,"border-"+side+"-style")=="none"?0:dojo.html.getPixelValue(node,"border-"+side+"-width"));};dojo.html.getMarginExtent=function(node,side){return dojo.html.getPixelValue(node,"margin-"+side,dojo.html.isPositionAbsolute(node));};dojo.html.getPaddingExtent=function(node,side){return dojo.html.getPixelValue(node,"padding-"+side,true);};dojo.html.getPadding=function(node){return dojo.html._getComponentPixelValues(node,"padding-",dojo.html.getPixelValue,true);};dojo.html.getPadBorder=function(node){var pad=dojo.html.getPadding(node);var _728=dojo.html.getBorder(node);return {width:pad.width+_728.width,height:pad.height+_728.height};};dojo.html.getBoxSizing=function(node){var h=dojo.render.html;var bs=dojo.html.boxSizing;if(((h.ie)||(h.opera))&&node.nodeName!="IMG"){var cm=document["compatMode"];if((cm=="BackCompat")||(cm=="QuirksMode")){return bs.BORDER_BOX;}else{return bs.CONTENT_BOX;}}else{if(arguments.length==0){node=document.documentElement;}
var _72d=dojo.html.getStyle(node,"-moz-box-sizing");if(!_72d){_72d=dojo.html.getStyle(node,"box-sizing");}
return (_72d?_72d:bs.CONTENT_BOX);}};dojo.html.isBorderBox=function(node){return (dojo.html.getBoxSizing(node)==dojo.html.boxSizing.BORDER_BOX);};dojo.html.getBorderBox=function(node){node=dojo.byId(node);return {width:node.offsetWidth,height:node.offsetHeight};};dojo.html.getPaddingBox=function(node){var box=dojo.html.getBorderBox(node);var _732=dojo.html.getBorder(node);return {width:box.width-_732.width,height:box.height-_732.height};};dojo.html.getContentBox=function(node){node=dojo.byId(node);var _734=dojo.html.getPadBorder(node);return {width:node.offsetWidth-_734.width,height:node.offsetHeight-_734.height};};dojo.html.setContentBox=function(node,args){node=dojo.byId(node);var _737=0;var _738=0;var isbb=dojo.html.isBorderBox(node);var _73a=(isbb?dojo.html.getPadBorder(node):{width:0,height:0});var ret={};if(typeof args.width!="undefined"){_737=args.width+_73a.width;ret.width=dojo.html.setPositivePixelValue(node,"width",_737);}
if(typeof args.height!="undefined"){_738=args.height+_73a.height;ret.height=dojo.html.setPositivePixelValue(node,"height",_738);}
return ret;};dojo.html.getMarginBox=function(node){var _73d=dojo.html.getBorderBox(node);var _73e=dojo.html.getMargin(node);return {width:_73d.width+_73e.width,height:_73d.height+_73e.height};};dojo.html.setMarginBox=function(node,args){node=dojo.byId(node);var _741=0;var _742=0;var isbb=dojo.html.isBorderBox(node);var _744=(!isbb?dojo.html.getPadBorder(node):{width:0,height:0});var _745=dojo.html.getMargin(node);var ret={};if(typeof args.width!="undefined"){_741=args.width-_744.width;_741-=_745.width;ret.width=dojo.html.setPositivePixelValue(node,"width",_741);}
if(typeof args.height!="undefined"){_742=args.height-_744.height;_742-=_745.height;ret.height=dojo.html.setPositivePixelValue(node,"height",_742);}
return ret;};dojo.html.getElementBox=function(node,type){var bs=dojo.html.boxSizing;switch(type){case bs.MARGIN_BOX:
return dojo.html.getMarginBox(node);case bs.BORDER_BOX:
return dojo.html.getBorderBox(node);case bs.PADDING_BOX:
return dojo.html.getPaddingBox(node);case bs.CONTENT_BOX:
default:
return dojo.html.getContentBox(node);}};dojo.html.toCoordinateObject=dojo.html.toCoordinateArray=function(_74a,_74b,_74c){if(!_74a.nodeType&&!(_74a instanceof String||typeof _74a=="string")&&("width" in _74a||"height" in _74a||"left" in _74a||"x" in _74a||"top" in _74a||"y" in _74a)){var ret={left:_74a.left||_74a.x||0,top:_74a.top||_74a.y||0,width:_74a.width||0,height:_74a.height||0};}else{var node=dojo.byId(_74a);var pos=dojo.html.abs(node,_74b,_74c);var _750=dojo.html.getMarginBox(node);var ret={left:pos.left,top:pos.top,width:_750.width,height:_750.height};}
ret.x=ret.left;ret.y=ret.top;return ret;};dojo.html.setMarginBoxWidth=dojo.html.setOuterWidth=function(node,_752){return dojo.html._callDeprecated("setMarginBoxWidth","setMarginBox",arguments,"width");};dojo.html.setMarginBoxHeight=dojo.html.setOuterHeight=function(){return dojo.html._callDeprecated("setMarginBoxHeight","setMarginBox",arguments,"height");};dojo.html.getMarginBoxWidth=dojo.html.getOuterWidth=function(){return dojo.html._callDeprecated("getMarginBoxWidth","getMarginBox",arguments,null,"width");};dojo.html.getMarginBoxHeight=dojo.html.getOuterHeight=function(){return dojo.html._callDeprecated("getMarginBoxHeight","getMarginBox",arguments,null,"height");};dojo.html.getTotalOffset=function(node,type,_755){return dojo.html._callDeprecated("getTotalOffset","getAbsolutePosition",arguments,null,type);};dojo.html.getAbsoluteX=function(node,_757){return dojo.html._callDeprecated("getAbsoluteX","getAbsolutePosition",arguments,null,"x");};dojo.html.getAbsoluteY=function(node,_759){return dojo.html._callDeprecated("getAbsoluteY","getAbsolutePosition",arguments,null,"y");};dojo.html.totalOffsetLeft=function(node,_75b){return dojo.html._callDeprecated("totalOffsetLeft","getAbsolutePosition",arguments,null,"left");};dojo.html.totalOffsetTop=function(node,_75d){return dojo.html._callDeprecated("totalOffsetTop","getAbsolutePosition",arguments,null,"top");};dojo.html.getMarginWidth=function(node){return dojo.html._callDeprecated("getMarginWidth","getMargin",arguments,null,"width");};dojo.html.getMarginHeight=function(node){return dojo.html._callDeprecated("getMarginHeight","getMargin",arguments,null,"height");};dojo.html.getBorderWidth=function(node){return dojo.html._callDeprecated("getBorderWidth","getBorder",arguments,null,"width");};dojo.html.getBorderHeight=function(node){return dojo.html._callDeprecated("getBorderHeight","getBorder",arguments,null,"height");};dojo.html.getPaddingWidth=function(node){return dojo.html._callDeprecated("getPaddingWidth","getPadding",arguments,null,"width");};dojo.html.getPaddingHeight=function(node){return dojo.html._callDeprecated("getPaddingHeight","getPadding",arguments,null,"height");};dojo.html.getPadBorderWidth=function(node){return dojo.html._callDeprecated("getPadBorderWidth","getPadBorder",arguments,null,"width");};dojo.html.getPadBorderHeight=function(node){return dojo.html._callDeprecated("getPadBorderHeight","getPadBorder",arguments,null,"height");};dojo.html.getBorderBoxWidth=dojo.html.getInnerWidth=function(){return dojo.html._callDeprecated("getBorderBoxWidth","getBorderBox",arguments,null,"width");};dojo.html.getBorderBoxHeight=dojo.html.getInnerHeight=function(){return dojo.html._callDeprecated("getBorderBoxHeight","getBorderBox",arguments,null,"height");};dojo.html.getContentBoxWidth=dojo.html.getContentWidth=function(){return dojo.html._callDeprecated("getContentBoxWidth","getContentBox",arguments,null,"width");};dojo.html.getContentBoxHeight=dojo.html.getContentHeight=function(){return dojo.html._callDeprecated("getContentBoxHeight","getContentBox",arguments,null,"height");};dojo.html.setContentBoxWidth=dojo.html.setContentWidth=function(node,_767){return dojo.html._callDeprecated("setContentBoxWidth","setContentBox",arguments,"width");};dojo.html.setContentBoxHeight=dojo.html.setContentHeight=function(node,_769){return dojo.html._callDeprecated("setContentBoxHeight","setContentBox",arguments,"height");};dojo.provide("dojo.html.util");dojo.html.getElementWindow=function(_76a){return dojo.html.getDocumentWindow(_76a.ownerDocument);};dojo.html.getDocumentWindow=function(doc){if(dojo.render.html.safari&&!doc._parentWindow){var fix=function(win){win.document._parentWindow=win;for(var i=0;i<win.frames.length;i++){fix(win.frames[i]);}};fix(window.top);}
if(dojo.render.html.ie&&window!==document.parentWindow&&!doc._parentWindow){doc.parentWindow.execScript("document._parentWindow = window;","Javascript");var win=doc._parentWindow;doc._parentWindow=null;return win;}
return doc._parentWindow||doc.parentWindow||doc.defaultView;};dojo.html.getAbsolutePositionExt=function(node,_771,_772,_773){var _774=dojo.html.getElementWindow(node);var ret=dojo.withGlobal(_774,"getAbsolutePosition",dojo.html,arguments);var win=dojo.html.getElementWindow(node);if(_773!=win&&win.frameElement){var ext=dojo.html.getAbsolutePositionExt(win.frameElement,_771,_772,_773);ret.x+=ext.x;ret.y+=ext.y;}
ret.top=ret.y;ret.left=ret.x;return ret;};dojo.html.gravity=function(node,e){node=dojo.byId(node);var _77a=dojo.html.getCursorPosition(e);with(dojo.html){var _77b=getAbsolutePosition(node,true);var bb=getBorderBox(node);var _77d=_77b.x+(bb.width/2);var _77e=_77b.y+(bb.height/2);}
with(dojo.html.gravity){return ((_77a.x<_77d?WEST:EAST)|(_77a.y<_77e?NORTH:SOUTH));}};dojo.html.gravity.NORTH=1;dojo.html.gravity.SOUTH=1<<1;dojo.html.gravity.EAST=1<<2;dojo.html.gravity.WEST=1<<3;dojo.html.overElement=function(_77f,e){_77f=dojo.byId(_77f);var _781=dojo.html.getCursorPosition(e);var bb=dojo.html.getBorderBox(_77f);var _783=dojo.html.getAbsolutePosition(_77f,true,dojo.html.boxSizing.BORDER_BOX);var top=_783.y;var _785=top+bb.height;var left=_783.x;var _787=left+bb.width;return (_781.x>=left&&_781.x<=_787&&_781.y>=top&&_781.y<=_785);};dojo.html.renderedTextContent=function(node){node=dojo.byId(node);var _789="";if(node==null){return _789;}
for(var i=0;i<node.childNodes.length;i++){switch(node.childNodes[i].nodeType){case 1:
case 5:
var _78b="unknown";try{_78b=dojo.html.getStyle(node.childNodes[i],"display");}
catch(E){}
switch(_78b){case "block":
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
_789+="\n";_789+=dojo.html.renderedTextContent(node.childNodes[i]);_789+="\n";break;case "none":
break;default:
if(node.childNodes[i].tagName&&node.childNodes[i].tagName.toLowerCase()=="br"){_789+="\n";}else{_789+=dojo.html.renderedTextContent(node.childNodes[i]);}
break;}
break;case 3:
case 2:
case 4:
var text=node.childNodes[i].nodeValue;var _78d="unknown";try{_78d=dojo.html.getStyle(node,"text-transform");}
catch(E){}
switch(_78d){case "capitalize":
var _78e=text.split(" ");for(var i=0;i<_78e.length;i++){_78e[i]=_78e[i].charAt(0).toUpperCase()+_78e[i].substring(1);}
text=_78e.join(" ");break;case "uppercase":
text=text.toUpperCase();break;case "lowercase":
text=text.toLowerCase();break;default:
break;}
switch(_78d){case "nowrap":
break;case "pre-wrap":
break;case "pre-line":
break;case "pre":
break;default:
text=text.replace(/\s+/," ");if(/\s$/.test(_789)){text.replace(/^\s/,"");}
break;}
_789+=text;break;default:
break;}}
return _789;};dojo.html.createNodesFromText=function(txt,trim){if(trim){txt=txt.replace(/^\s+|\s+$/g,"");}
var tn=dojo.doc().createElement("div");tn.style.visibility="hidden";dojo.body().appendChild(tn);var _792="none";if((/^<t[dh][\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table><tbody><tr>"+txt+"</tr></tbody></table>";_792="cell";}else{if((/^<tr[\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table><tbody>"+txt+"</tbody></table>";_792="row";}else{if((/^<(thead|tbody|tfoot)[\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table>"+txt+"</table>";_792="section";}}
}
tn.innerHTML=txt;if(tn["normalize"]){tn.normalize();}
var _793=null;switch(_792){case "cell":
_793=tn.getElementsByTagName("tr")[0];break;case "row":
_793=tn.getElementsByTagName("tbody")[0];break;case "section":
_793=tn.getElementsByTagName("table")[0];break;default:
_793=tn;break;}
var _794=[];for(var x=0;x<_793.childNodes.length;x++){_794.push(_793.childNodes[x].cloneNode(true));}
tn.style.display="none";dojo.html.destroyNode(tn);return _794;};dojo.html.placeOnScreen=function(node,_797,_798,_799,_79a,_79b,_79c){if(_797 instanceof Array||typeof _797=="array"){_79c=_79b;_79b=_79a;_79a=_799;_799=_798;_798=_797[1];_797=_797[0];}
if(_79b instanceof String||typeof _79b=="string"){_79b=_79b.split(",");}
if(!isNaN(_799)){_799=[Number(_799),Number(_799)];}else{if(!(_799 instanceof Array||typeof _799=="array")){_799=[0,0];}}
var _79d=dojo.html.getScroll().offset;var view=dojo.html.getViewport();node=dojo.byId(node);var _79f=node.style.display;node.style.display="";var bb=dojo.html.getBorderBox(node);var w=bb.width;var h=bb.height;node.style.display=_79f;if(!(_79b instanceof Array||typeof _79b=="array")){_79b=["TL"];}
var _7a3,_7a4,_7a5=Infinity,_7a6;for(var _7a7=0;_7a7<_79b.length;++_7a7){var _7a8=_79b[_7a7];var _7a9=true;var tryX=_797-(_7a8.charAt(1)=="L"?0:w)+_799[0]*(_7a8.charAt(1)=="L"?1:-1);var tryY=_798-(_7a8.charAt(0)=="T"?0:h)+_799[1]*(_7a8.charAt(0)=="T"?1:-1);if(_79a){tryX-=_79d.x;tryY-=_79d.y;}
if(tryX<0){tryX=0;_7a9=false;}
if(tryY<0){tryY=0;_7a9=false;}
var x=tryX+w;if(x>view.width){x=view.width-w;_7a9=false;}else{x=tryX;}
x=Math.max(_799[0],x)+_79d.x;var y=tryY+h;if(y>view.height){y=view.height-h;_7a9=false;}else{y=tryY;}
y=Math.max(_799[1],y)+_79d.y;if(_7a9){_7a3=x;_7a4=y;_7a5=0;_7a6=_7a8;break;}else{var dist=Math.pow(x-tryX-_79d.x,2)+Math.pow(y-tryY-_79d.y,2);if(_7a5>dist){_7a5=dist;_7a3=x;_7a4=y;_7a6=_7a8;}}
}
if(!_79c){node.style.left=_7a3+"px";node.style.top=_7a4+"px";}
return {left:_7a3,top:_7a4,x:_7a3,y:_7a4,dist:_7a5,corner:_7a6};};dojo.html.placeOnScreenAroundElement=function(node,_7b0,_7b1,_7b2,_7b3,_7b4){var best,_7b6=Infinity;_7b0=dojo.byId(_7b0);var _7b7=_7b0.style.display;_7b0.style.display="";var mb=dojo.html.getElementBox(_7b0,_7b2);var _7b9=mb.width;var _7ba=mb.height;var _7bb=dojo.html.getAbsolutePosition(_7b0,true,_7b2);_7b0.style.display=_7b7;for(var _7bc in _7b3){var pos,_7be,_7bf;var _7c0=_7b3[_7bc];_7be=_7bb.x+(_7bc.charAt(1)=="L"?0:_7b9);_7bf=_7bb.y+(_7bc.charAt(0)=="T"?0:_7ba);pos=dojo.html.placeOnScreen(node,_7be,_7bf,_7b1,true,_7c0,true);if(pos.dist==0){best=pos;break;}else{if(_7b6>pos.dist){_7b6=pos.dist;best=pos;}}
}
if(!_7b4){node.style.left=best.left+"px";node.style.top=best.top+"px";}
return best;};dojo.html.scrollIntoView=function(node){if(!node){return;}
if(dojo.render.html.ie){if(dojo.html.getBorderBox(node.parentNode).height<=node.parentNode.scrollHeight){node.scrollIntoView(false);}}else{if(dojo.render.html.mozilla){node.scrollIntoView(false);}else{var _7c2=node.parentNode;var _7c3=_7c2.scrollTop+dojo.html.getBorderBox(_7c2).height;var _7c4=node.offsetTop+dojo.html.getMarginBox(node).height;if(_7c3<_7c4){_7c2.scrollTop+=(_7c4-_7c3);}else{if(_7c2.scrollTop>node.offsetTop){_7c2.scrollTop-=(_7c2.scrollTop-node.offsetTop);}}
}}
};dojo.provide("dojo.gfx.color");dojo.gfx.color.Color=function(r,g,b,a){if(dojo.lang.isArray(r)){this.r=r[0];this.g=r[1];this.b=r[2];this.a=r[3]||1;}else{if(dojo.lang.isString(r)){var rgb=dojo.gfx.color.extractRGB(r);this.r=rgb[0];this.g=rgb[1];this.b=rgb[2];this.a=g||1;}else{if(r instanceof dojo.gfx.color.Color){this.r=r.r;this.b=r.b;this.g=r.g;this.a=r.a;}else{this.r=r;this.g=g;this.b=b;this.a=a;}}
}};dojo.gfx.color.Color.fromArray=function(arr){return new dojo.gfx.color.Color(arr[0],arr[1],arr[2],arr[3]);};dojo.extend(dojo.gfx.color.Color,{toRgb:function(_7cb){if(_7cb){return this.toRgba();}else{return [this.r,this.g,this.b];}},toRgba:function(){return [this.r,this.g,this.b,this.a];},toHex:function(){return dojo.gfx.color.rgb2hex(this.toRgb());},toCss:function(){return "rgb("+this.toRgb().join()+")";},toString:function(){return this.toHex();},blend:function(_7cc,_7cd){var rgb=null;if(dojo.lang.isArray(_7cc)){rgb=_7cc;}else{if(_7cc instanceof dojo.gfx.color.Color){rgb=_7cc.toRgb();}else{rgb=new dojo.gfx.color.Color(_7cc).toRgb();}}
return dojo.gfx.color.blend(this.toRgb(),rgb,_7cd);}});dojo.gfx.color.named={white:[255,255,255],black:[0,0,0],red:[255,0,0],green:[0,255,0],lime:[0,255,0],blue:[0,0,255],navy:[0,0,128],gray:[128,128,128],silver:[192,192,192]};dojo.gfx.color.blend=function(a,b,_7d1){if(typeof a=="string"){return dojo.gfx.color.blendHex(a,b,_7d1);}
if(!_7d1){_7d1=0;}
_7d1=Math.min(Math.max(-1,_7d1),1);_7d1=((_7d1+1)/2);var c=[];for(var x=0;x<3;x++){c[x]=parseInt(b[x]+((a[x]-b[x])*_7d1));}
return c;};dojo.gfx.color.blendHex=function(a,b,_7d6){return dojo.gfx.color.rgb2hex(dojo.gfx.color.blend(dojo.gfx.color.hex2rgb(a),dojo.gfx.color.hex2rgb(b),_7d6));};dojo.gfx.color.extractRGB=function(_7d7){_7d7=_7d7.toLowerCase();if(_7d7.indexOf("rgb")==0){var _7d8=_7d7.match(/rgba*\((\d+), *(\d+), *(\d+)/i);var ret=_7d8.splice(1,3);return ret;}else{var _7da=dojo.gfx.color.hex2rgb(_7d7);if(_7da){return _7da;}else{return dojo.gfx.color.named[_7d7]||[255,255,255];}}
};dojo.gfx.color.hex2rgb=function(hex){var _7dc="0123456789ABCDEF";var rgb=new Array(3);if(hex.indexOf("#")==0){hex=hex.substring(1);}
hex=hex.toUpperCase();if(hex.replace(new RegExp("["+_7dc+"]","g"),"")!=""){return null;}
if(hex.length==3){rgb[0]=hex.charAt(0)+hex.charAt(0);rgb[1]=hex.charAt(1)+hex.charAt(1);rgb[2]=hex.charAt(2)+hex.charAt(2);}else{rgb[0]=hex.substring(0,2);rgb[1]=hex.substring(2,4);rgb[2]=hex.substring(4);}
for(var i=0;i<rgb.length;i++){rgb[i]=_7dc.indexOf(rgb[i].charAt(0))*16+_7dc.indexOf(rgb[i].charAt(1));}
return rgb;};dojo.gfx.color.rgb2hex=function(r,g,b){if(dojo.lang.isArray(r)){g=r[1]||0;b=r[2]||0;r=r[0]||0;}
var ret=dojo.lang.map([r,g,b],function(x){x=new Number(x);var s=x.toString(16);while(s.length<2){s="0"+s;}
return s;});ret.unshift("#");return ret.join("");};dojo.provide("dojo.lfx.Animation");dojo.lfx.Line=function(_7e5,end){this.start=_7e5;this.end=end;if(dojo.lang.isArray(_7e5)){var diff=[];dojo.lang.forEach(this.start,function(s,i){diff[i]=this.end[i]-s;},this);this.getValue=function(n){var res=[];dojo.lang.forEach(this.start,function(s,i){res[i]=(diff[i]*n)+s;},this);return res;};}else{var diff=end-_7e5;this.getValue=function(n){return (diff*n)+this.start;};}};dojo.lfx.easeDefault=function(n){if(dojo.render.html.khtml){return (parseFloat("0.5")+((Math.sin((n+parseFloat("1.5"))*Math.PI))/2));}else{return (0.5+((Math.sin((n+1.5)*Math.PI))/2));}};dojo.lfx.easeIn=function(n){return Math.pow(n,3);};dojo.lfx.easeOut=function(n){return (1-Math.pow(1-n,3));};dojo.lfx.easeInOut=function(n){return ((3*Math.pow(n,2))-(2*Math.pow(n,3)));};dojo.lfx.IAnimation=function(){};dojo.lang.extend(dojo.lfx.IAnimation,{curve:null,duration:1000,easing:null,repeatCount:0,rate:25,handler:null,beforeBegin:null,onBegin:null,onAnimate:null,onEnd:null,onPlay:null,onPause:null,onStop:null,play:null,pause:null,stop:null,connect:function(evt,_7f4,_7f5){if(!_7f5){_7f5=_7f4;_7f4=this;}
_7f5=dojo.lang.hitch(_7f4,_7f5);var _7f6=this[evt]||function(){};this[evt]=function(){var ret=_7f6.apply(this,arguments);_7f5.apply(this,arguments);return ret;};return this;},fire:function(evt,args){if(this[evt]){this[evt].apply(this,(args||[]));}
return this;},repeat:function(_7fa){this.repeatCount=_7fa;return this;},_active:false,_paused:false});dojo.lfx.Animation=function(_7fb,_7fc,_7fd,_7fe,_7ff,rate){dojo.lfx.IAnimation.call(this);if(dojo.lang.isNumber(_7fb)||(!_7fb&&_7fc.getValue)){rate=_7ff;_7ff=_7fe;_7fe=_7fd;_7fd=_7fc;_7fc=_7fb;_7fb=null;}else{if(_7fb.getValue||dojo.lang.isArray(_7fb)){rate=_7fe;_7ff=_7fd;_7fe=_7fc;_7fd=_7fb;_7fc=null;_7fb=null;}}
if(dojo.lang.isArray(_7fd)){this.curve=new dojo.lfx.Line(_7fd[0],_7fd[1]);}else{this.curve=_7fd;}
if(_7fc!=null&&_7fc>0){this.duration=_7fc;}
if(_7ff){this.repeatCount=_7ff;}
if(rate){this.rate=rate;}
if(_7fb){dojo.lang.forEach(["handler","beforeBegin","onBegin","onEnd","onPlay","onStop","onAnimate"],function(item){if(_7fb[item]){this.connect(item,_7fb[item]);}},this);}
if(_7fe&&dojo.lang.isFunction(_7fe)){this.easing=_7fe;}};dojo.inherits(dojo.lfx.Animation,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Animation,{_startTime:null,_endTime:null,_timer:null,_percent:0,_startRepeatCount:0,play:function(_802,_803){if(_803){clearTimeout(this._timer);this._active=false;this._paused=false;this._percent=0;}else{if(this._active&&!this._paused){return this;}}
this.fire("handler",["beforeBegin"]);this.fire("beforeBegin");if(_802>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_803);}),_802);return this;}
this._startTime=new Date().valueOf();if(this._paused){this._startTime-=(this.duration*this._percent/100);}
this._endTime=this._startTime+this.duration;this._active=true;this._paused=false;var step=this._percent/100;var _805=this.curve.getValue(step);if(this._percent==0){if(!this._startRepeatCount){this._startRepeatCount=this.repeatCount;}
this.fire("handler",["begin",_805]);this.fire("onBegin",[_805]);}
this.fire("handler",["play",_805]);this.fire("onPlay",[_805]);this._cycle();return this;},pause:function(){clearTimeout(this._timer);if(!this._active){return this;}
this._paused=true;var _806=this.curve.getValue(this._percent/100);this.fire("handler",["pause",_806]);this.fire("onPause",[_806]);return this;},gotoPercent:function(pct,_808){clearTimeout(this._timer);this._active=true;this._paused=true;this._percent=pct;if(_808){this.play();}
return this;},stop:function(_809){clearTimeout(this._timer);var step=this._percent/100;if(_809){step=1;}
var _80b=this.curve.getValue(step);this.fire("handler",["stop",_80b]);this.fire("onStop",[_80b]);this._active=false;this._paused=false;return this;},status:function(){if(this._active){return this._paused?"paused":"playing";}else{return "stopped";}
return this;},_cycle:function(){clearTimeout(this._timer);if(this._active){var curr=new Date().valueOf();var step=(curr-this._startTime)/(this._endTime-this._startTime);if(step>=1){step=1;this._percent=100;}else{this._percent=step*100;}
if((this.easing)&&(dojo.lang.isFunction(this.easing))){step=this.easing(step);}
var _80e=this.curve.getValue(step);this.fire("handler",["animate",_80e]);this.fire("onAnimate",[_80e]);if(step<1){this._timer=setTimeout(dojo.lang.hitch(this,"_cycle"),this.rate);}else{this._active=false;this.fire("handler",["end"]);this.fire("onEnd");if(this.repeatCount>0){this.repeatCount--;this.play(null,true);}else{if(this.repeatCount==-1){this.play(null,true);}else{if(this._startRepeatCount){this.repeatCount=this._startRepeatCount;this._startRepeatCount=0;}}
}}
}
return this;}});dojo.lfx.Combine=function(_80f){dojo.lfx.IAnimation.call(this);this._anims=[];this._animsEnded=0;var _810=arguments;if(_810.length==1&&(dojo.lang.isArray(_810[0])||dojo.lang.isArrayLike(_810[0]))){_810=_810[0];}
dojo.lang.forEach(_810,function(anim){this._anims.push(anim);anim.connect("onEnd",dojo.lang.hitch(this,"_onAnimsEnded"));},this);};dojo.inherits(dojo.lfx.Combine,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Combine,{_animsEnded:0,play:function(_812,_813){if(!this._anims.length){return this;}
this.fire("beforeBegin");if(_812>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_813);}),_812);return this;}
if(_813||this._anims[0].percent==0){this.fire("onBegin");}
this.fire("onPlay");this._animsCall("play",null,_813);return this;},pause:function(){this.fire("onPause");this._animsCall("pause");return this;},stop:function(_814){this.fire("onStop");this._animsCall("stop",_814);return this;},_onAnimsEnded:function(){this._animsEnded++;if(this._animsEnded>=this._anims.length){this.fire("onEnd");}
return this;},_animsCall:function(_815){var args=[];if(arguments.length>1){for(var i=1;i<arguments.length;i++){args.push(arguments[i]);}}
var _818=this;dojo.lang.forEach(this._anims,function(anim){anim[_815](args);},_818);return this;}});dojo.lfx.Chain=function(_81a){dojo.lfx.IAnimation.call(this);this._anims=[];this._currAnim=-1;var _81b=arguments;if(_81b.length==1&&(dojo.lang.isArray(_81b[0])||dojo.lang.isArrayLike(_81b[0]))){_81b=_81b[0];}
var _81c=this;dojo.lang.forEach(_81b,function(anim,i,_81f){this._anims.push(anim);if(i<_81f.length-1){anim.connect("onEnd",dojo.lang.hitch(this,"_playNext"));}else{anim.connect("onEnd",dojo.lang.hitch(this,function(){this.fire("onEnd");}));}},this);};dojo.inherits(dojo.lfx.Chain,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Chain,{_currAnim:-1,play:function(_820,_821){if(!this._anims.length){return this;}
if(_821||!this._anims[this._currAnim]){this._currAnim=0;}
var _822=this._anims[this._currAnim];this.fire("beforeBegin");if(_820>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_821);}),_820);return this;}
if(_822){if(this._currAnim==0){this.fire("handler",["begin",this._currAnim]);this.fire("onBegin",[this._currAnim]);}
this.fire("onPlay",[this._currAnim]);_822.play(null,_821);}
return this;},pause:function(){if(this._anims[this._currAnim]){this._anims[this._currAnim].pause();this.fire("onPause",[this._currAnim]);}
return this;},playPause:function(){if(this._anims.length==0){return this;}
if(this._currAnim==-1){this._currAnim=0;}
var _823=this._anims[this._currAnim];if(_823){if(!_823._active||_823._paused){this.play();}else{this.pause();}}
return this;},stop:function(){var _824=this._anims[this._currAnim];if(_824){_824.stop();this.fire("onStop",[this._currAnim]);}
return _824;},_playNext:function(){if(this._currAnim==-1||this._anims.length==0){return this;}
this._currAnim++;if(this._anims[this._currAnim]){this._anims[this._currAnim].play(null,true);}
return this;}});dojo.lfx.combine=function(_825){var _826=arguments;if(dojo.lang.isArray(arguments[0])){_826=arguments[0];}
if(_826.length==1){return _826[0];}
return new dojo.lfx.Combine(_826);};dojo.lfx.chain=function(_827){var _828=arguments;if(dojo.lang.isArray(arguments[0])){_828=arguments[0];}
if(_828.length==1){return _828[0];}
return new dojo.lfx.Chain(_828);};dojo.provide("dojo.html.color");dojo.html.getBackgroundColor=function(node){node=dojo.byId(node);var _82a;do{_82a=dojo.html.getStyle(node,"background-color");if(_82a.toLowerCase()=="rgba(0, 0, 0, 0)"){_82a="transparent";}
if(node==document.getElementsByTagName("body")[0]){node=null;break;}
node=node.parentNode;}while(node&&dojo.lang.inArray(["transparent",""],_82a));if(_82a=="transparent"){_82a=[255,255,255,0];}else{_82a=dojo.gfx.color.extractRGB(_82a);}
return _82a;};dojo.provide("dojo.lfx.html");dojo.lfx.html._byId=function(_82b){if(!_82b){return [];}
if(dojo.lang.isArrayLike(_82b)){if(!_82b.alreadyChecked){var n=[];dojo.lang.forEach(_82b,function(node){n.push(dojo.byId(node));});n.alreadyChecked=true;return n;}else{return _82b;}}else{var n=[];n.push(dojo.byId(_82b));n.alreadyChecked=true;return n;}};dojo.lfx.html.propertyAnimation=function(_82e,_82f,_830,_831,_832){_82e=dojo.lfx.html._byId(_82e);var _833={"propertyMap":_82f,"nodes":_82e,"duration":_830,"easing":_831||dojo.lfx.easeDefault};var _834=function(args){if(args.nodes.length==1){var pm=args.propertyMap;if(!dojo.lang.isArray(args.propertyMap)){var parr=[];for(var _838 in pm){pm[_838].property=_838;parr.push(pm[_838]);}
pm=args.propertyMap=parr;}
dojo.lang.forEach(pm,function(prop){if(dj_undef("start",prop)){if(prop.property!="opacity"){prop.start=parseInt(dojo.html.getComputedStyle(args.nodes[0],prop.property));}else{prop.start=dojo.html.getOpacity(args.nodes[0]);}}
});}};var _83a=function(_83b){var _83c=[];dojo.lang.forEach(_83b,function(c){_83c.push(Math.round(c));});return _83c;};var _83e=function(n,_840){n=dojo.byId(n);if(!n||!n.style){return;}
for(var s in _840){try{if(s=="opacity"){dojo.html.setOpacity(n,_840[s]);}else{n.style[s]=_840[s];}}
catch(e){dojo.debug(e);}}
};var _842=function(_843){this._properties=_843;this.diffs=new Array(_843.length);dojo.lang.forEach(_843,function(prop,i){if(dojo.lang.isFunction(prop.start)){prop.start=prop.start(prop,i);}
if(dojo.lang.isFunction(prop.end)){prop.end=prop.end(prop,i);}
if(dojo.lang.isArray(prop.start)){this.diffs[i]=null;}else{if(prop.start instanceof dojo.gfx.color.Color){prop.startRgb=prop.start.toRgb();prop.endRgb=prop.end.toRgb();}else{this.diffs[i]=prop.end-prop.start;}}
},this);this.getValue=function(n){var ret={};dojo.lang.forEach(this._properties,function(prop,i){var _84a=null;if(dojo.lang.isArray(prop.start)){}else{if(prop.start instanceof dojo.gfx.color.Color){_84a=(prop.units||"rgb")+"(";for(var j=0;j<prop.startRgb.length;j++){_84a+=Math.round(((prop.endRgb[j]-prop.startRgb[j])*n)+prop.startRgb[j])+(j<prop.startRgb.length-1?",":"");}
_84a+=")";}else{_84a=((this.diffs[i])*n)+prop.start+(prop.property!="opacity"?prop.units||"px":"");}}
ret[dojo.html.toCamelCase(prop.property)]=_84a;},this);return ret;};};var anim=new dojo.lfx.Animation({beforeBegin:function(){_834(_833);anim.curve=new _842(_833.propertyMap);},onAnimate:function(_84d){dojo.lang.forEach(_833.nodes,function(node){_83e(node,_84d);});}},_833.duration,null,_833.easing);if(_832){for(var x in _832){if(dojo.lang.isFunction(_832[x])){anim.connect(x,anim,_832[x]);}}
}
return anim;};dojo.lfx.html._makeFadeable=function(_850){var _851=function(node){if(dojo.render.html.ie){if((node.style.zoom.length==0)&&(dojo.html.getStyle(node,"zoom")=="normal")){node.style.zoom="1";}
if((node.style.width.length==0)&&(dojo.html.getStyle(node,"width")=="auto")){node.style.width="auto";}}
};if(dojo.lang.isArrayLike(_850)){dojo.lang.forEach(_850,_851);}else{_851(_850);}};dojo.lfx.html.fade=function(_853,_854,_855,_856,_857){_853=dojo.lfx.html._byId(_853);var _858={property:"opacity"};if(!dj_undef("start",_854)){_858.start=_854.start;}else{_858.start=function(){return dojo.html.getOpacity(_853[0]);};}
if(!dj_undef("end",_854)){_858.end=_854.end;}else{dojo.raise("dojo.lfx.html.fade needs an end value");}
var anim=dojo.lfx.propertyAnimation(_853,[_858],_855,_856);anim.connect("beforeBegin",function(){dojo.lfx.html._makeFadeable(_853);});if(_857){anim.connect("onEnd",function(){_857(_853,anim);});}
return anim;};dojo.lfx.html.fadeIn=function(_85a,_85b,_85c,_85d){return dojo.lfx.html.fade(_85a,{end:1},_85b,_85c,_85d);};dojo.lfx.html.fadeOut=function(_85e,_85f,_860,_861){return dojo.lfx.html.fade(_85e,{end:0},_85f,_860,_861);};dojo.lfx.html.fadeShow=function(_862,_863,_864,_865){_862=dojo.lfx.html._byId(_862);dojo.lang.forEach(_862,function(node){dojo.html.setOpacity(node,0);});var anim=dojo.lfx.html.fadeIn(_862,_863,_864,_865);anim.connect("beforeBegin",function(){if(dojo.lang.isArrayLike(_862)){dojo.lang.forEach(_862,dojo.html.show);}else{dojo.html.show(_862);}});return anim;};dojo.lfx.html.fadeHide=function(_868,_869,_86a,_86b){var anim=dojo.lfx.html.fadeOut(_868,_869,_86a,function(){if(dojo.lang.isArrayLike(_868)){dojo.lang.forEach(_868,dojo.html.hide);}else{dojo.html.hide(_868);}
if(_86b){_86b(_868,anim);}});return anim;};dojo.lfx.html.wipeIn=function(_86d,_86e,_86f,_870){_86d=dojo.lfx.html._byId(_86d);var _871=[];dojo.lang.forEach(_86d,function(node){var _873={};var _874,_875,_876;with(node.style){_874=top;_875=left;_876=position;top="-9999px";left="-9999px";position="absolute";display="";}
var _877=dojo.html.getBorderBox(node).height;with(node.style){top=_874;left=_875;position=_876;display="none";}
var anim=dojo.lfx.propertyAnimation(node,{"height":{start:1,end:function(){return _877;}}},_86e,_86f);anim.connect("beforeBegin",function(){_873.overflow=node.style.overflow;_873.height=node.style.height;with(node.style){overflow="hidden";_877="1px";}
dojo.html.show(node);});anim.connect("onEnd",function(){with(node.style){overflow=_873.overflow;_877=_873.height;}
if(_870){_870(node,anim);}});_871.push(anim);});return dojo.lfx.combine(_871);};dojo.lfx.html.wipeOut=function(_879,_87a,_87b,_87c){_879=dojo.lfx.html._byId(_879);var _87d=[];dojo.lang.forEach(_879,function(node){var _87f={};var anim=dojo.lfx.propertyAnimation(node,{"height":{start:function(){return dojo.html.getContentBox(node).height;},end:1}},_87a,_87b,{"beforeBegin":function(){_87f.overflow=node.style.overflow;_87f.height=node.style.height;with(node.style){overflow="hidden";}
dojo.html.show(node);},"onEnd":function(){dojo.html.hide(node);with(node.style){overflow=_87f.overflow;height=_87f.height;}
if(_87c){_87c(node,anim);}}});_87d.push(anim);});return dojo.lfx.combine(_87d);};dojo.lfx.html.slideTo=function(_881,_882,_883,_884,_885){_881=dojo.lfx.html._byId(_881);var _886=[];var _887=dojo.html.getComputedStyle;dojo.lang.forEach(_881,function(node){var top=null;var left=null;var init=(function(){var _88c=node;return function(){var pos=_887(_88c,"position");top=(pos=="absolute"?node.offsetTop:parseInt(_887(node,"top"))||0);left=(pos=="absolute"?node.offsetLeft:parseInt(_887(node,"left"))||0);if(!dojo.lang.inArray(["absolute","relative"],pos)){var ret=dojo.html.abs(_88c,true);dojo.html.setStyleAttributes(_88c,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");top=ret.y;left=ret.x;}};})();init();var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:(_882.top||0)},"left":{start:left,end:(_882.left||0)}},_883,_884,{"beforeBegin":init});if(_885){anim.connect("onEnd",function(){_885(_881,anim);});}
_886.push(anim);});return dojo.lfx.combine(_886);};dojo.lfx.html.slideBy=function(_890,_891,_892,_893,_894){_890=dojo.lfx.html._byId(_890);var _895=[];var _896=dojo.html.getComputedStyle;dojo.lang.forEach(_890,function(node){var top=null;var left=null;var init=(function(){var _89b=node;return function(){var pos=_896(_89b,"position");top=(pos=="absolute"?node.offsetTop:parseInt(_896(node,"top"))||0);left=(pos=="absolute"?node.offsetLeft:parseInt(_896(node,"left"))||0);if(!dojo.lang.inArray(["absolute","relative"],pos)){var ret=dojo.html.abs(_89b,true);dojo.html.setStyleAttributes(_89b,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");top=ret.y;left=ret.x;}};})();init();var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:top+(_891.top||0)},"left":{start:left,end:left+(_891.left||0)}},_892,_893).connect("beforeBegin",init);if(_894){anim.connect("onEnd",function(){_894(_890,anim);});}
_895.push(anim);});return dojo.lfx.combine(_895);};dojo.lfx.html.explode=function(_89f,_8a0,_8a1,_8a2,_8a3){var h=dojo.html;_89f=dojo.byId(_89f);_8a0=dojo.byId(_8a0);var _8a5=h.toCoordinateObject(_89f,true);var _8a6=document.createElement("div");h.copyStyle(_8a6,_8a0);if(_8a0.explodeClassName){_8a6.className=_8a0.explodeClassName;}
with(_8a6.style){position="absolute";display="none";var _8a7=h.getStyle(_89f,"background-color");backgroundColor=_8a7?_8a7.toLowerCase():"transparent";backgroundColor=(backgroundColor=="transparent")?"rgb(221, 221, 221)":backgroundColor;}
dojo.body().appendChild(_8a6);with(_8a0.style){visibility="hidden";display="block";}
var _8a8=h.toCoordinateObject(_8a0,true);with(_8a0.style){display="none";visibility="visible";}
var _8a9={opacity:{start:0.5,end:1}};dojo.lang.forEach(["height","width","top","left"],function(type){_8a9[type]={start:_8a5[type],end:_8a8[type]};});var anim=new dojo.lfx.propertyAnimation(_8a6,_8a9,_8a1,_8a2,{"beforeBegin":function(){h.setDisplay(_8a6,"block");},"onEnd":function(){h.setDisplay(_8a0,"block");_8a6.parentNode.removeChild(_8a6);}});if(_8a3){anim.connect("onEnd",function(){_8a3(_8a0,anim);});}
return anim;};dojo.lfx.html.implode=function(_8ac,end,_8ae,_8af,_8b0){var h=dojo.html;_8ac=dojo.byId(_8ac);end=dojo.byId(end);var _8b2=dojo.html.toCoordinateObject(_8ac,true);var _8b3=dojo.html.toCoordinateObject(end,true);var _8b4=document.createElement("div");dojo.html.copyStyle(_8b4,_8ac);if(_8ac.explodeClassName){_8b4.className=_8ac.explodeClassName;}
dojo.html.setOpacity(_8b4,0.3);with(_8b4.style){position="absolute";display="none";backgroundColor=h.getStyle(_8ac,"background-color").toLowerCase();}
dojo.body().appendChild(_8b4);var _8b5={opacity:{start:1,end:0.5}};dojo.lang.forEach(["height","width","top","left"],function(type){_8b5[type]={start:_8b2[type],end:_8b3[type]};});var anim=new dojo.lfx.propertyAnimation(_8b4,_8b5,_8ae,_8af,{"beforeBegin":function(){dojo.html.hide(_8ac);dojo.html.show(_8b4);},"onEnd":function(){_8b4.parentNode.removeChild(_8b4);}});if(_8b0){anim.connect("onEnd",function(){_8b0(_8ac,anim);});}
return anim;};dojo.lfx.html.highlight=function(_8b8,_8b9,_8ba,_8bb,_8bc){_8b8=dojo.lfx.html._byId(_8b8);var _8bd=[];dojo.lang.forEach(_8b8,function(node){var _8bf=dojo.html.getBackgroundColor(node);var bg=dojo.html.getStyle(node,"background-color").toLowerCase();var _8c1=dojo.html.getStyle(node,"background-image");var _8c2=(bg=="transparent"||bg=="rgba(0, 0, 0, 0)");while(_8bf.length>3){_8bf.pop();}
var rgb=new dojo.gfx.color.Color(_8b9);var _8c4=new dojo.gfx.color.Color(_8bf);var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:rgb,end:_8c4}},_8ba,_8bb,{"beforeBegin":function(){if(_8c1){node.style.backgroundImage="none";}
node.style.backgroundColor="rgb("+rgb.toRgb().join(",")+")";},"onEnd":function(){if(_8c1){node.style.backgroundImage=_8c1;}
if(_8c2){node.style.backgroundColor="transparent";}
if(_8bc){_8bc(node,anim);}}});_8bd.push(anim);});return dojo.lfx.combine(_8bd);};dojo.lfx.html.unhighlight=function(_8c6,_8c7,_8c8,_8c9,_8ca){_8c6=dojo.lfx.html._byId(_8c6);var _8cb=[];dojo.lang.forEach(_8c6,function(node){var _8cd=new dojo.gfx.color.Color(dojo.html.getBackgroundColor(node));var rgb=new dojo.gfx.color.Color(_8c7);var _8cf=dojo.html.getStyle(node,"background-image");var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:_8cd,end:rgb}},_8c8,_8c9,{"beforeBegin":function(){if(_8cf){node.style.backgroundImage="none";}
node.style.backgroundColor="rgb("+_8cd.toRgb().join(",")+")";},"onEnd":function(){if(_8ca){_8ca(node,anim);}}});_8cb.push(anim);});return dojo.lfx.combine(_8cb);};dojo.lang.mixin(dojo.lfx,dojo.lfx.html);dojo.provide("dojo.lfx.*");dojo.provide("dojo.lfx.toggle");dojo.lfx.toggle.plain={show:function(node,_8d2,_8d3,_8d4){dojo.html.show(node);if(dojo.lang.isFunction(_8d4)){_8d4();}},hide:function(node,_8d6,_8d7,_8d8){dojo.html.hide(node);if(dojo.lang.isFunction(_8d8)){_8d8();}}};dojo.lfx.toggle.fade={anim:null,show:function(node,_8da,_8db,_8dc){if(this.anim&&this.anim.status()!="stopped"){this.anim.stop();}
this.anim=dojo.lfx.fadeShow(node,_8da,_8db,_8dc).play();},hide:function(node,_8de,_8df,_8e0){if(this.anim&&this.anim.status()!="stopped"){this.anim.stop();}
this.anim=dojo.lfx.fadeHide(node,_8de,_8df,_8e0).play();}};dojo.lfx.toggle.wipe={anim:null,show:function(node,_8e2,_8e3,_8e4){if(this.anim&&this.anim.status()!="stopped"){this.anim.stop();}
this.anim=dojo.lfx.wipeIn(node,_8e2,_8e3,_8e4).play();},hide:function(node,_8e6,_8e7,_8e8){if(this.anim&&this.anim.status()!="stopped"){this.anim.stop();}
this.anim=dojo.lfx.wipeOut(node,_8e6,_8e7,_8e8).play();}};dojo.lfx.toggle.explode={anim:null,show:function(node,_8ea,_8eb,_8ec,_8ed){if(this.anim&&this.anim.status()!="stopped"){this.anim.stop();}
this.anim=dojo.lfx.explode(_8ed||{x:0,y:0,width:0,height:0},node,_8ea,_8eb,_8ec).play();},hide:function(node,_8ef,_8f0,_8f1,_8f2){if(this.anim&&this.anim.status()!="stopped"){this.anim.stop();}
this.anim=dojo.lfx.implode(node,_8f2||{x:0,y:0,width:0,height:0},_8ef,_8f0,_8f1).play();}};dojo.provide("dojo.widget.HtmlWidget");dojo.declare("dojo.widget.HtmlWidget",dojo.widget.DomWidget,{templateCssPath:null,templatePath:null,lang:"",toggle:"plain",toggleDuration:150,initialize:function(args,frag){},postMixInProperties:function(args,frag){if(this.lang===""){this.lang=null;}
this.toggleObj=dojo.lfx.toggle[this.toggle.toLowerCase()]||dojo.lfx.toggle.plain;},createNodesFromText:function(txt,wrap){return dojo.html.createNodesFromText(txt,wrap);},destroyRendering:function(_8f9){try{if(this.bgIframe){this.bgIframe.remove();delete this.bgIframe;}
if(!_8f9&&this.domNode){dojo.event.browser.clean(this.domNode);}
dojo.widget.HtmlWidget.superclass.destroyRendering.call(this);}
catch(e){}},isShowing:function(){return dojo.html.isShowing(this.domNode);},toggleShowing:function(){if(this.isShowing()){this.hide();}else{this.show();}},show:function(){if(this.isShowing()){return;}
this.animationInProgress=true;this.toggleObj.show(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onShow),this.explodeSrc);},onShow:function(){this.animationInProgress=false;this.checkSize();},hide:function(){if(!this.isShowing()){return;}
this.animationInProgress=true;this.toggleObj.hide(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onHide),this.explodeSrc);},onHide:function(){this.animationInProgress=false;},_isResized:function(w,h){if(!this.isShowing()){return false;}
var wh=dojo.html.getMarginBox(this.domNode);var _8fd=w||wh.width;var _8fe=h||wh.height;if(this.width==_8fd&&this.height==_8fe){return false;}
this.width=_8fd;this.height=_8fe;return true;},checkSize:function(){if(!this._isResized()){return;}
this.onResized();},resizeTo:function(w,h){dojo.html.setMarginBox(this.domNode,{width:w,height:h});if(this.isShowing()){this.onResized();}},resizeSoon:function(){if(this.isShowing()){dojo.lang.setTimeout(this,this.onResized,0);}},onResized:function(){dojo.lang.forEach(this.children,function(_901){if(_901.checkSize){_901.checkSize();}});}});dojo.provide("dojo.widget.*");