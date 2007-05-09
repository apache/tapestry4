
if(typeof dojo=="undefined"){var dj_global=this;var dj_currentContext=this;function dj_undef(_1,_2){return (typeof (_2||dj_currentContext)[_1]=="undefined");}
if(dj_undef("djConfig",this)){var djConfig={};}
if(dj_undef("dojo",this)){var dojo={};}
dojo.global=function(){return dj_currentContext;};dojo.locale=djConfig.locale;dojo.version={major:0,minor:0,patch:0,flag:"dev",revision:Number("$Rev$".match(/[0-9]+/)[0]),toString:function(){with(dojo.version){return major+"."+minor+"."+patch+flag+" ("+revision+")";}}};dojo.evalProp=function(_3,_4,_5){if((!_4)||(!_3)){return undefined;}
if(!dj_undef(_3,_4)){return _4[_3];}
return (_5?(_4[_3]={}):undefined);};dojo.parseObjPath=function(_6,_7,_8){var _9=(_7||dojo.global());var _a=_6.split(".");var _b=_a.pop();for(var i=0,l=_a.length;i<l&&_9;i++){_9=dojo.evalProp(_a[i],_9,_8);}
return {obj:_9,prop:_b};};dojo.evalObjPath=function(_e,_f){if(typeof _e!="string"){return dojo.global();}
if(_e.indexOf(".")==-1){return dojo.evalProp(_e,dojo.global(),_f);}
var ref=dojo.parseObjPath(_e,dojo.global(),_f);if(ref){return dojo.evalProp(ref.prop,ref.obj,_f);}
return null;};dojo.errorToString=function(_11){if(!dj_undef("message",_11)){return _11.message;}else{if(!dj_undef("description",_11)){return _11.description;}else{return _11;}}};dojo.raise=function(_12,_13){if(_13){_12=_12+": "+dojo.errorToString(_13);}else{_12=dojo.errorToString(_12);}
try{if(djConfig.isDebug){dojo.hostenv.println("FATAL exception raised: "+_12);}}
catch(e){}
throw _13||Error(_12);};dojo.debug=function(){};dojo.debugShallow=function(obj){};dojo.profile={start:function(){},end:function(){},stop:function(){},dump:function(){}};function dj_eval(_15){return dj_global.eval?dj_global.eval(_15):eval(_15);}
dojo.unimplemented=function(_16,_17){var _18="'"+_16+"' not implemented";if(_17!=null){_18+=" "+_17;}
dojo.raise(_18);};dojo.deprecated=function(_19,_1a,_1b){var _1c="DEPRECATED: "+_19;if(_1a){_1c+=" "+_1a;}
if(_1b){_1c+=" -- will be removed in version: "+_1b;}
dojo.debug(_1c);};dojo.render=(function(){function vscaffold(_1d,_1e){var tmp={capable:false,support:{builtin:false,plugin:false},prefixes:_1d};for(var i=0;i<_1e.length;i++){tmp[_1e[i]]=false;}
return tmp;}
return {name:"",ver:dojo.version,os:{win:false,linux:false,osx:false},html:vscaffold(["html"],["ie","opera","khtml","safari","moz"]),svg:vscaffold(["svg"],["corel","adobe","batik"]),vml:vscaffold(["vml"],["ie"]),swf:vscaffold(["Swf","Flash","Mm"],["mm"]),swt:vscaffold(["Swt"],["ibm"])};})();dojo.hostenv=(function(){var _21={isDebug:false,allowQueryConfig:false,baseScriptUri:"",baseRelativePath:"",libraryScriptUri:"",iePreventClobber:false,ieClobberMinimal:true,preventBackButtonFix:true,delayMozLoadingFix:false,searchIds:[],parseWidgets:true};if(typeof djConfig=="undefined"){djConfig=_21;}else{for(var _22 in _21){if(typeof djConfig[_22]=="undefined"){djConfig[_22]=_21[_22];}}}
return {name_:"(unset)",version_:"(unset)",getName:function(){return this.name_;},getVersion:function(){return this.version_;},getText:function(uri){dojo.unimplemented("getText","uri="+uri);}};})();dojo.hostenv.getBaseScriptUri=function(){if(djConfig.baseScriptUri.length){return djConfig.baseScriptUri;}
var uri=new String(djConfig.libraryScriptUri||djConfig.baseRelativePath);if(!uri){dojo.raise("Nothing returned by getLibraryScriptUri(): "+uri);}
var _25=uri.lastIndexOf("/");djConfig.baseScriptUri=djConfig.baseRelativePath;return djConfig.baseScriptUri;};(function(){var _26={pkgFileName:"__package__",loading_modules_:{},loaded_modules_:{},addedToLoadingCount:[],removedFromLoadingCount:[],inFlightCount:0,modulePrefixes_:{dojo:{name:"dojo",value:"src"}},setModulePrefix:function(_27,_28){this.modulePrefixes_[_27]={name:_27,value:_28};},moduleHasPrefix:function(_29){var mp=this.modulePrefixes_;return Boolean(mp[_29]&&mp[_29].value);},getModulePrefix:function(_2b){if(this.moduleHasPrefix(_2b)){return this.modulePrefixes_[_2b].value;}
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
if(dh.post_load_&&dh.inFlightCount==0&&!dh.loadNotifying){dh.callLoaded();}};dojo.addOnUnload=function(obj,_40){var dh=dojo.hostenv;if(arguments.length==1){dh.unloadListeners.push(obj);}else{if(arguments.length>1){dh.unloadListeners.push(function(){obj[_40]();});}}};dojo.hostenv.modulesLoaded=function(){if(this.post_load_){return;}
if(this.loadUriStack.length==0&&this.getTextStack.length==0){if(this.inFlightCount>0){dojo.debug("files still in flight!");return;}
dojo.hostenv.callLoaded();}};dojo.hostenv.callLoaded=function(){if(typeof setTimeout=="object"||(djConfig["useXDomain"]&&dojo.render.html.opera)){setTimeout("dojo.hostenv.loaded();",0);}else{dojo.hostenv.loaded();}};dojo.hostenv.getModuleSymbols=function(_42){var _43=_42.split(".");for(var i=_43.length;i>0;i--){var _45=_43.slice(0,i).join(".");if((i==1)&&!this.moduleHasPrefix(_45)){_43[0]="../"+_43[0];}else{var _46=this.getModulePrefix(_45);if(_46!=_45){_43.splice(0,i,_46);break;}}}
return _43;};dojo.hostenv._global_omit_module_check=false;dojo.hostenv.loadModule=function(_47,_48,_49){if(!_47){return;}
_49=this._global_omit_module_check||_49;var _4a=this.findModule(_47,false);if(_4a){return _4a;}
if(dj_undef(_47,this.loading_modules_)){this.addedToLoadingCount.push(_47);}
this.loading_modules_[_47]=1;var _4b=_47.replace(/\./g,"/")+".js";var _4c=_47.split(".");var _4d=this.getModuleSymbols(_47);var _4e=((_4d[0].charAt(0)!="/")&&!_4d[0].match(/^\w+:/));var _4f=_4d[_4d.length-1];var ok;if(_4f=="*"){_47=_4c.slice(0,-1).join(".");while(_4d.length){_4d.pop();_4d.push(this.pkgFileName);_4b=_4d.join("/")+".js";if(_4e&&_4b.charAt(0)=="/"){_4b=_4b.slice(1);}
ok=this.loadPath(_4b,!_49?_47:null);if(ok){break;}
_4d.pop();}}else{_4b=_4d.join("/")+".js";_47=_4c.join(".");var _51=!_49?_47:null;ok=this.loadPath(_4b,_51);if(!ok&&!_48){_4d.pop();while(_4d.length){_4b=_4d.join("/")+".js";ok=this.loadPath(_4b,_51);if(ok){break;}
_4d.pop();_4b=_4d.join("/")+"/"+this.pkgFileName+".js";if(_4e&&_4b.charAt(0)=="/"){_4b=_4b.slice(1);}
ok=this.loadPath(_4b,_51);if(ok){break;}}}
if(!ok&&!_49){dojo.raise("Could not load '"+_47+"'; last tried '"+_4b+"'");}}
if(!_49&&!this["isXDomain"]){_4a=this.findModule(_47,false);if(!_4a){dojo.raise("symbol '"+_47+"' is not defined after loading '"+_4b+"'");}}
return _4a;};dojo.hostenv.startPackage=function(_52){var _53=String(_52);var _54=_53;var _55=_52.split(/\./);if(_55[_55.length-1]=="*"){_55.pop();_54=_55.join(".");}
var _56=dojo.evalObjPath(_54,true);this.loaded_modules_[_53]=_56;this.loaded_modules_[_54]=_56;return _56;};dojo.hostenv.findModule=function(_57,_58){var lmn=String(_57);if(this.loaded_modules_[lmn]){return this.loaded_modules_[lmn];}
if(_58){dojo.raise("no loaded module named '"+_57+"'");}
return null;};dojo.kwCompoundRequire=function(_5a){var _5b=_5a["common"]||[];var _5c=_5a[dojo.hostenv.name_]?_5b.concat(_5a[dojo.hostenv.name_]||[]):_5b.concat(_5a["default"]||[]);for(var x=0;x<_5c.length;x++){var _5e=_5c[x];if(_5e.constructor==Array){dojo.hostenv.loadModule.apply(dojo.hostenv,_5e);}else{dojo.hostenv.loadModule(_5e);}}};dojo.require=function(_5f){dojo.hostenv.loadModule.apply(dojo.hostenv,arguments);};dojo.requireIf=function(_60,_61){var _62=arguments[0];if((_62===true)||(_62=="common")||(_62&&dojo.render[_62].capable)){var _63=[];for(var i=1;i<arguments.length;i++){_63.push(arguments[i]);}
dojo.require.apply(dojo,_63);}};dojo.requireAfterIf=dojo.requireIf;dojo.provide=function(_65){return dojo.hostenv.startPackage.apply(dojo.hostenv,arguments);};dojo.registerModulePath=function(_66,_67){return dojo.hostenv.setModulePrefix(_66,_67);};if(djConfig["modulePaths"]){for(var param in djConfig["modulePaths"]){dojo.registerModulePath(param,djConfig["modulePaths"][param]);}}
dojo.setModulePrefix=function(_68,_69){dojo.deprecated("dojo.setModulePrefix(\""+_68+"\", \""+_69+"\")","replaced by dojo.registerModulePath","0.5");return dojo.registerModulePath(_68,_69);};dojo.exists=function(obj,_6b){var p=_6b.split(".");for(var i=0;i<p.length;i++){if(!obj[p[i]]){return false;}
obj=obj[p[i]];}
return true;};dojo.hostenv.normalizeLocale=function(_6e){var _6f=_6e?_6e.toLowerCase():dojo.locale;if(_6f=="root"){_6f="ROOT";}
return _6f;};dojo.hostenv.searchLocalePath=function(_70,_71,_72){_70=dojo.hostenv.normalizeLocale(_70);var _73=_70.split("-");var _74=[];for(var i=_73.length;i>0;i--){_74.push(_73.slice(0,i).join("-"));}
_74.push(false);if(_71){_74.reverse();}
for(var j=_74.length-1;j>=0;j--){var loc=_74[j]||"ROOT";var _78=_72(loc);if(_78){break;}}};dojo.hostenv.localesGenerated;dojo.hostenv.registerNlsPrefix=function(){dojo.registerModulePath("nls","nls");};dojo.hostenv.preloadLocalizations=function(){if(dojo.hostenv.localesGenerated){dojo.hostenv.registerNlsPrefix();function preload(_79){_79=dojo.hostenv.normalizeLocale(_79);dojo.hostenv.searchLocalePath(_79,true,function(loc){for(var i=0;i<dojo.hostenv.localesGenerated.length;i++){if(dojo.hostenv.localesGenerated[i]==loc){dojo["require"]("nls.dojo_"+loc);return true;}}
return false;});}
preload();var _7c=djConfig.extraLocale||[];for(var i=0;i<_7c.length;i++){preload(_7c[i]);}}
dojo.hostenv.preloadLocalizations=function(){};};dojo.requireLocalization=function(_7e,_7f,_80,_81){dojo.hostenv.preloadLocalizations();var _82=dojo.hostenv.normalizeLocale(_80);var _83=[_7e,"nls",_7f].join(".");var _84="";if(_81){var _85=_81.split(",");for(var i=0;i<_85.length;i++){if(_82.indexOf(_85[i])==0){if(_85[i].length>_84.length){_84=_85[i];}}}
if(!_84){_84="ROOT";}}
var _87=_81?_84:_82;var _88=dojo.hostenv.findModule(_83);var _89=null;if(_88){if(djConfig.localizationComplete&&_88._built){return;}
var _8a=_87.replace("-","_");var _8b=_83+"."+_8a;_89=dojo.hostenv.findModule(_8b);}
if(!_89){_88=dojo.hostenv.startPackage(_83);var _8c=dojo.hostenv.getModuleSymbols(_7e);var _8d=_8c.concat("nls").join("/");var _8e;dojo.hostenv.searchLocalePath(_87,_81,function(loc){var _90=loc.replace("-","_");var _91=_83+"."+_90;var _92=false;if(!dojo.hostenv.findModule(_91)){dojo.hostenv.startPackage(_91);var _93=[_8d];if(loc!="ROOT"){_93.push(loc);}
_93.push(_7f);var _94=_93.join("/")+".js";_92=dojo.hostenv.loadPath(_94,null,function(_95){var _96=function(){};_96.prototype=_8e;_88[_90]=new _96();for(var j in _95){_88[_90][j]=_95[j];}});}else{_92=true;}
if(_92&&_88[_90]){_8e=_88[_90];}else{_88[_90]=_8e;}
if(_81){return true;}});}
if(_81&&_82!=_84){_88[_82.replace("-","_")]=_88[_84.replace("-","_")];}};(function(){var _98=djConfig.extraLocale;if(_98){if(!_98 instanceof Array){_98=[_98];}
var req=dojo.requireLocalization;dojo.requireLocalization=function(m,b,_9c,_9d){req(m,b,_9c,_9d);if(_9c){return;}
for(var i=0;i<_98.length;i++){req(m,b,_98[i],_9d);}};}})();}
if(typeof window!="undefined"){(function(){if(djConfig.allowQueryConfig){var _9f=document.location.toString();var _a0=_9f.split("?",2);if(_a0.length>1){var _a1=_a0[1];var _a2=_a1.split("&");for(var x in _a2){var sp=_a2[x].split("=");if((sp[0].length>9)&&(sp[0].substr(0,9)=="djConfig.")){var opt=sp[0].substr(9);try{djConfig[opt]=eval(sp[1]);}
catch(e){djConfig[opt]=sp[1];}}}}}
if(((djConfig["baseScriptUri"]=="")||(djConfig["baseRelativePath"]==""))&&(document&&document.getElementsByTagName)){var _a6=document.getElementsByTagName("script");var _a7=/(__package__|dojo|bootstrap1)\.js([\?\.]|$)/i;for(var i=0;i<_a6.length;i++){var src=_a6[i].getAttribute("src");if(!src){continue;}
var m=src.match(_a7);if(m){var _ab=src.substring(0,m.index);if(src.indexOf("bootstrap1")>-1){_ab+="../";}
if(!this["djConfig"]){djConfig={};}
if(djConfig["baseScriptUri"]==""){djConfig["baseScriptUri"]=_ab;}
if(djConfig["baseRelativePath"]==""){djConfig["baseRelativePath"]=_ab;}
break;}}}
var dr=dojo.render;var drh=dojo.render.html;var drs=dojo.render.svg;var dua=(drh.UA=navigator.userAgent);var dav=(drh.AV=navigator.appVersion);var t=true;var f=false;drh.capable=t;drh.support.builtin=t;dr.ver=parseFloat(drh.AV);dr.os.mac=dav.indexOf("Macintosh")>=0;dr.os.win=dav.indexOf("Windows")>=0;dr.os.linux=dav.indexOf("X11")>=0;drh.opera=dua.indexOf("Opera")>=0;drh.khtml=(dav.indexOf("Konqueror")>=0)||(dav.indexOf("Safari")>=0);drh.safari=dav.indexOf("Safari")>=0;var _b3=dua.indexOf("Gecko");drh.mozilla=drh.moz=(_b3>=0)&&(!drh.khtml);if(drh.mozilla){drh.geckoVersion=dua.substring(_b3+6,_b3+14);}
drh.ie=(document.all)&&(!drh.opera);drh.ie50=drh.ie&&dav.indexOf("MSIE 5.0")>=0;drh.ie55=drh.ie&&dav.indexOf("MSIE 5.5")>=0;drh.ie60=drh.ie&&dav.indexOf("MSIE 6.0")>=0;drh.ie70=drh.ie&&dav.indexOf("MSIE 7.0")>=0;var cm=document["compatMode"];drh.quirks=(cm=="BackCompat")||(cm=="QuirksMode")||drh.ie55||drh.ie50;dojo.locale=dojo.locale||(drh.ie?navigator.userLanguage:navigator.language).toLowerCase();dr.vml.capable=drh.ie;drs.capable=f;drs.support.plugin=f;drs.support.builtin=f;var _b5=window["document"];var tdi=_b5["implementation"];if((tdi)&&(tdi["hasFeature"])&&(tdi.hasFeature("org.w3c.dom.svg","1.0"))){drs.capable=t;drs.support.builtin=t;drs.support.plugin=f;}
if(drh.safari){var tmp=dua.split("AppleWebKit/")[1];var ver=parseFloat(tmp.split(" ")[0]);if(ver>=420){drs.capable=t;drs.support.builtin=t;drs.support.plugin=f;}}else{}})();dojo.hostenv.startPackage("dojo.hostenv");dojo.render.name=dojo.hostenv.name_="browser";dojo.hostenv.searchIds=[];dojo.hostenv._XMLHTTP_PROGIDS=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"];dojo.hostenv.getXmlhttpObject=function(){var _b9=null;var _ba=null;try{_b9=new XMLHttpRequest();}
catch(e){}
if(!_b9){for(var i=0;i<3;++i){var _bc=dojo.hostenv._XMLHTTP_PROGIDS[i];try{_b9=new ActiveXObject(_bc);}
catch(e){_ba=e;}
if(_b9){dojo.hostenv._XMLHTTP_PROGIDS=[_bc];break;}}}
if(!_b9){return dojo.raise("XMLHTTP not available",_ba);}
return _b9;};dojo.hostenv._blockAsync=false;dojo.hostenv.getText=function(uri,_be,_bf){if(!_be){this._blockAsync=true;}
var _c0=this.getXmlhttpObject();function isDocumentOk(_c1){var _c2=_c1["status"];return Boolean((!_c2)||((200<=_c2)&&(300>_c2))||(_c2==304));}
if(_be){var _c3=this,_c4=null,gbl=dojo.global();var xhr=dojo.evalObjPath("dojo.io.XMLHTTPTransport");_c0.onreadystatechange=function(){if(_c4){gbl.clearTimeout(_c4);_c4=null;}
if(_c3._blockAsync||(xhr&&xhr._blockAsync)){_c4=gbl.setTimeout(function(){_c0.onreadystatechange.apply(this);},10);}else{if(4==_c0.readyState){if(isDocumentOk(_c0)){_be(_c0.responseText);}}}};}
_c0.open("GET",uri,_be?true:false);try{_c0.send(null);if(_be){return null;}
if(!isDocumentOk(_c0)){var err=Error("Unable to load "+uri+" status:"+_c0.status);err.status=_c0.status;err.responseText=_c0.responseText;throw err;}}
catch(e){this._blockAsync=false;if((_bf)&&(!_be)){return null;}else{throw e;}}
this._blockAsync=false;return _c0.responseText;};dojo.hostenv.defaultDebugContainerId="dojoDebug";dojo.hostenv._println_buffer=[];dojo.hostenv._println_safe=false;dojo.hostenv.println=function(_c8){if(!dojo.hostenv._println_safe){dojo.hostenv._println_buffer.push(_c8);}else{try{var _c9=document.getElementById(djConfig.debugContainerId?djConfig.debugContainerId:dojo.hostenv.defaultDebugContainerId);if(!_c9){_c9=dojo.body();}
var div=document.createElement("div");div.appendChild(document.createTextNode(_c8));_c9.appendChild(div);}
catch(e){try{document.write("<div>"+_c8+"</div>");}
catch(e2){window.status=_c8;}}}};dojo.addOnLoad(function(){dojo.hostenv._println_safe=true;while(dojo.hostenv._println_buffer.length>0){dojo.hostenv.println(dojo.hostenv._println_buffer.shift());}});function dj_addNodeEvtHdlr(_cb,_cc,fp){var _ce=_cb["on"+_cc]||function(){};_cb["on"+_cc]=function(){fp.apply(_cb,arguments);_ce.apply(_cb,arguments);};return true;}
function dj_load_init(e){var _d0=(e&&e.type)?e.type.toLowerCase():"load";if(arguments.callee.initialized||(_d0!="domcontentloaded"&&_d0!="load")){return;}
arguments.callee.initialized=true;if(typeof (_timer)!="undefined"){clearInterval(_timer);delete _timer;}
var _d1=function(){if(dojo.render.html.ie){dojo.hostenv.makeWidgets();}};if(dojo.hostenv.inFlightCount==0){_d1();dojo.hostenv.modulesLoaded();}else{dojo.hostenv.modulesLoadedListeners.unshift(_d1);}}
if(document.addEventListener){if(dojo.render.html.opera||(dojo.render.html.moz&&(djConfig["enableMozDomContentLoaded"]===true))){document.addEventListener("DOMContentLoaded",dj_load_init,null);}
window.addEventListener("load",dj_load_init,null);}
if(dojo.render.html.ie&&dojo.render.os.win){document.attachEvent("onreadystatechange",function(e){if(document.readyState=="complete"){dj_load_init();}});}
if(/(WebKit|khtml)/i.test(navigator.userAgent)){var _timer=setInterval(function(){if(/loaded|complete/.test(document.readyState)){dj_load_init();}},10);}
if(dojo.render.html.ie){dj_addNodeEvtHdlr(window,"beforeunload",function(){dojo.hostenv._unloading=true;window.setTimeout(function(){dojo.hostenv._unloading=false;},0);});}
dj_addNodeEvtHdlr(window,"unload",function(){dojo.hostenv.unloaded();if((!dojo.render.html.ie)||(dojo.render.html.ie&&dojo.hostenv._unloading)){dojo.hostenv.unloaded();}});dojo.hostenv.makeWidgets=function(){var _d3=[];if(djConfig.searchIds&&djConfig.searchIds.length>0){_d3=_d3.concat(djConfig.searchIds);}
if(dojo.hostenv.searchIds&&dojo.hostenv.searchIds.length>0){_d3=_d3.concat(dojo.hostenv.searchIds);}
if((djConfig.parseWidgets)||(_d3.length>0)){if(dojo.evalObjPath("dojo.widget.Parse")){var _d4=new dojo.xml.Parse();if(_d3.length>0){for(var x=0;x<_d3.length;x++){var _d6=document.getElementById(_d3[x]);if(!_d6){continue;}
var _d7=_d4.parseElement(_d6,null,true);dojo.widget.getParser().createComponents(_d7);}}else{if(djConfig.parseWidgets){var _d7=_d4.parseElement(dojo.body(),null,true);dojo.widget.getParser().createComponents(_d7);}}}}};dojo.addOnLoad(function(){if(!dojo.render.html.ie){dojo.hostenv.makeWidgets();}});try{if(dojo.render.html.ie){document.namespaces.add("v","urn:schemas-microsoft-com:vml");document.createStyleSheet().addRule("v\\:*","behavior:url(#default#VML)");}}
catch(e){}
dojo.hostenv.writeIncludes=function(){};if(!dj_undef("document",this)){dj_currentDocument=this.document;}
dojo.doc=function(){return dj_currentDocument;};dojo.body=function(){return dojo.doc().body||dojo.doc().getElementsByTagName("body")[0];};dojo.byId=function(id,doc){if((id)&&((typeof id=="string")||(id instanceof String))){if(!doc){doc=dj_currentDocument;}
var ele=doc.getElementById(id);if(ele&&(ele.id!=id)&&doc.all){ele=null;eles=doc.all[id];if(eles){if(eles.length){for(var i=0;i<eles.length;i++){if(eles[i].id==id){ele=eles[i];break;}}}else{ele=eles;}}}
return ele;}
return id;};dojo.setContext=function(_dc,_dd){dj_currentContext=_dc;dj_currentDocument=_dd;};dojo._fireCallback=function(_de,_df,_e0){if((_df)&&((typeof _de=="string")||(_de instanceof String))){_de=_df[_de];}
return (_df?_de.apply(_df,_e0||[]):_de());};dojo.withGlobal=function(_e1,_e2,_e3,_e4){var _e5;var _e6=dj_currentContext;var _e7=dj_currentDocument;try{dojo.setContext(_e1,_e1.document);_e5=dojo._fireCallback(_e2,_e3,_e4);}
finally{dojo.setContext(_e6,_e7);}
return _e5;};dojo.withDoc=function(_e8,_e9,_ea,_eb){var _ec;var _ed=dj_currentDocument;try{dj_currentDocument=_e8;_ec=dojo._fireCallback(_e9,_ea,_eb);}
finally{dj_currentDocument=_ed;}
return _ec;};}
dojo.requireIf((djConfig["isDebug"]||djConfig["debugAtAllCosts"]),"dojo.debug");dojo.requireIf(djConfig["debugAtAllCosts"]&&!window.widget&&!djConfig["useXDomain"],"dojo.browser_debug");dojo.requireIf(djConfig["debugAtAllCosts"]&&!window.widget&&djConfig["useXDomain"],"dojo.browser_debug_xd");dojo.provide("dojo.lang.common");dojo.lang.inherits=function(_ee,_ef){if(!dojo.lang.isFunction(_ef)){dojo.raise("dojo.inherits: superclass argument ["+_ef+"] must be a function (subclass: ["+_ee+"']");}
_ee.prototype=new _ef();_ee.prototype.constructor=_ee;_ee.superclass=_ef.prototype;_ee["super"]=_ef.prototype;};dojo.lang._mixin=function(obj,_f1){var _f2={};for(var x in _f1){if((typeof _f2[x]=="undefined")||(_f2[x]!=_f1[x])){obj[x]=_f1[x];}}
if(dojo.render.html.ie&&(typeof (_f1["toString"])=="function")&&(_f1["toString"]!=obj["toString"])&&(_f1["toString"]!=_f2["toString"])){obj.toString=_f1.toString;}
return obj;};dojo.lang.mixin=function(obj,_f5){for(var i=1,l=arguments.length;i<l;i++){dojo.lang._mixin(obj,arguments[i]);}
return obj;};dojo.lang.extend=function(_f8,_f9){for(var i=1,l=arguments.length;i<l;i++){dojo.lang._mixin(_f8.prototype,arguments[i]);}
return _f8;};dojo.inherits=dojo.lang.inherits;dojo.mixin=dojo.lang.mixin;dojo.extend=dojo.lang.extend;dojo.lang.find=function(_fc,_fd,_fe,_ff){if(!dojo.lang.isArrayLike(_fc)&&dojo.lang.isArrayLike(_fd)){dojo.deprecated("dojo.lang.find(value, array)","use dojo.lang.find(array, value) instead","0.5");var temp=_fc;_fc=_fd;_fd=temp;}
var _101=dojo.lang.isString(_fc);if(_101){_fc=_fc.split("");}
if(_ff){var step=-1;var i=_fc.length-1;var end=-1;}else{var step=1;var i=0;var end=_fc.length;}
if(_fe){while(i!=end){if(_fc[i]===_fd){return i;}
i+=step;}}else{while(i!=end){if(_fc[i]==_fd){return i;}
i+=step;}}
return -1;};dojo.lang.indexOf=dojo.lang.find;dojo.lang.findLast=function(_105,_106,_107){return dojo.lang.find(_105,_106,_107,true);};dojo.lang.lastIndexOf=dojo.lang.findLast;dojo.lang.inArray=function(_108,_109){return dojo.lang.find(_108,_109)>-1;};dojo.lang.isObject=function(it){if(typeof it=="undefined"){return false;}
return (typeof it=="object"||it===null||dojo.lang.isArray(it)||dojo.lang.isFunction(it));};dojo.lang.isArray=function(it){return (it&&it instanceof Array||typeof it=="array");};dojo.lang.isArrayLike=function(it){if((!it)||(dojo.lang.isUndefined(it))){return false;}
if(dojo.lang.isString(it)){return false;}
if(dojo.lang.isFunction(it)){return false;}
if(dojo.lang.isArray(it)){return true;}
if((it.tagName)&&(it.tagName.toLowerCase()=="form")){return false;}
if(dojo.lang.isNumber(it.length)&&isFinite(it.length)){return true;}
return false;};dojo.lang.isFunction=function(it){return (it instanceof Function||typeof it=="function");};(function(){if((dojo.render.html.capable)&&(dojo.render.html["safari"])){dojo.lang.isFunction=function(it){if((typeof (it)=="function")&&(it=="[object NodeList]")){return false;}
return (it instanceof Function||typeof it=="function");};}})();dojo.lang.isString=function(it){return (typeof it=="string"||it instanceof String);};dojo.lang.isAlien=function(it){if(!it){return false;}
return !dojo.lang.isFunction(it)&&/\{\s*\[native code\]\s*\}/.test(String(it));};dojo.lang.isBoolean=function(it){return (it instanceof Boolean||typeof it=="boolean");};dojo.lang.isNumber=function(it){return (it instanceof Number||typeof it=="number");};dojo.lang.isUndefined=function(it){return ((typeof (it)=="undefined")&&(it==undefined));};dojo.provide("dojo.lang.array");dojo.lang.mixin(dojo.lang,{has:function(obj,name){try{return typeof obj[name]!="undefined";}
catch(e){return false;}},isEmpty:function(obj){if(dojo.lang.isObject(obj)){var tmp={};var _118=0;for(var x in obj){if(obj[x]&&(!tmp[x])){_118++;break;}}
return _118==0;}else{if(dojo.lang.isArrayLike(obj)||dojo.lang.isString(obj)){return obj.length==0;}}},map:function(arr,obj,_11c){var _11d=dojo.lang.isString(arr);if(_11d){arr=arr.split("");}
if(dojo.lang.isFunction(obj)&&(!_11c)){_11c=obj;obj=dj_global;}else{if(dojo.lang.isFunction(obj)&&_11c){var _11e=obj;obj=_11c;_11c=_11e;}}
if(Array.map){var _11f=Array.map(arr,_11c,obj);}else{var _11f=[];for(var i=0;i<arr.length;++i){_11f.push(_11c.call(obj,arr[i]));}}
if(_11d){return _11f.join("");}else{return _11f;}},reduce:function(arr,_122,obj,_124){var _125=_122;if(arguments.length==2){_124=_122;_125=arr[0];arr=arr.slice(1);}else{if(arguments.length==3){if(dojo.lang.isFunction(obj)){_124=obj;obj=null;}}else{if(dojo.lang.isFunction(obj)){var tmp=_124;_124=obj;obj=tmp;}}}
var ob=obj||dj_global;dojo.lang.map(arr,function(val){_125=_124.call(ob,_125,val);});return _125;},forEach:function(_129,_12a,_12b){if(dojo.lang.isString(_129)){_129=_129.split("");}
if(Array.forEach){Array.forEach(_129,_12a,_12b);}else{if(!_12b){_12b=dj_global;}
for(var i=0,l=_129.length;i<l;i++){_12a.call(_12b,_129[i],i,_129);}}},_everyOrSome:function(_12e,arr,_130,_131){if(dojo.lang.isString(arr)){arr=arr.split("");}
if(Array.every){return Array[_12e?"every":"some"](arr,_130,_131);}else{if(!_131){_131=dj_global;}
for(var i=0,l=arr.length;i<l;i++){var _134=_130.call(_131,arr[i],i,arr);if(_12e&&!_134){return false;}else{if((!_12e)&&(_134)){return true;}}}
return Boolean(_12e);}},every:function(arr,_136,_137){return this._everyOrSome(true,arr,_136,_137);},some:function(arr,_139,_13a){return this._everyOrSome(false,arr,_139,_13a);},filter:function(arr,_13c,_13d){var _13e=dojo.lang.isString(arr);if(_13e){arr=arr.split("");}
var _13f;if(Array.filter){_13f=Array.filter(arr,_13c,_13d);}else{if(!_13d){if(arguments.length>=3){dojo.raise("thisObject doesn't exist!");}
_13d=dj_global;}
_13f=[];for(var i=0;i<arr.length;i++){if(_13c.call(_13d,arr[i],i,arr)){_13f.push(arr[i]);}}}
if(_13e){return _13f.join("");}else{return _13f;}},unnest:function(){var out=[];for(var i=0;i<arguments.length;i++){if(dojo.lang.isArrayLike(arguments[i])){var add=dojo.lang.unnest.apply(this,arguments[i]);out=out.concat(add);}else{out.push(arguments[i]);}}
return out;},toArray:function(_144,_145){var _146=[];for(var i=_145||0;i<_144.length;i++){_146.push(_144[i]);}
return _146;}});dojo.provide("dojo.lang.extras");dojo.lang.setTimeout=function(func,_149){var _14a=window,_14b=2;if(!dojo.lang.isFunction(func)){_14a=func;func=_149;_149=arguments[2];_14b++;}
if(dojo.lang.isString(func)){func=_14a[func];}
var args=[];for(var i=_14b;i<arguments.length;i++){args.push(arguments[i]);}
return dojo.global().setTimeout(function(){func.apply(_14a,args);},_149);};dojo.lang.clearTimeout=function(_14e){dojo.global().clearTimeout(_14e);};dojo.lang.getNameInObj=function(ns,item){if(!ns){ns=dj_global;}
for(var x in ns){if(ns[x]===item){return new String(x);}}
return null;};dojo.lang.shallowCopy=function(obj,deep){var i,ret;if(obj===null){return null;}
if(dojo.lang.isObject(obj)){ret=new obj.constructor();for(i in obj){if(dojo.lang.isUndefined(ret[i])){ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];}}}else{if(dojo.lang.isArray(obj)){ret=[];for(i=0;i<obj.length;i++){ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];}}else{ret=obj;}}
return ret;};dojo.lang.firstValued=function(){for(var i=0;i<arguments.length;i++){if(typeof arguments[i]!="undefined"){return arguments[i];}}
return undefined;};dojo.lang.getObjPathValue=function(_157,_158,_159){with(dojo.parseObjPath(_157,_158,_159)){return dojo.evalProp(prop,obj,_159);}};dojo.lang.setObjPathValue=function(_15a,_15b,_15c,_15d){dojo.deprecated("dojo.lang.setObjPathValue","use dojo.parseObjPath and the '=' operator","0.6");if(arguments.length<4){_15d=true;}
with(dojo.parseObjPath(_15a,_15c,_15d)){if(obj&&(_15d||(prop in obj))){obj[prop]=_15b;}}};dojo.provide("dojo.lang.func");dojo.lang.hitch=function(_15e,_15f){var fcn=(dojo.lang.isString(_15f)?_15e[_15f]:_15f)||function(){};return function(){return fcn.apply(_15e,arguments);};};dojo.lang.anonCtr=0;dojo.lang.anon={};dojo.lang.nameAnonFunc=function(_161,_162,_163){var nso=(_162||dojo.lang.anon);if((_163)||((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true))){for(var x in nso){try{if(nso[x]===_161){return x;}}
catch(e){}}}
var ret="__"+dojo.lang.anonCtr++;while(typeof nso[ret]!="undefined"){ret="__"+dojo.lang.anonCtr++;}
nso[ret]=_161;return ret;};dojo.lang.forward=function(_167){return function(){return this[_167].apply(this,arguments);};};dojo.lang.curry=function(_168,func){var _16a=[];_168=_168||dj_global;if(dojo.lang.isString(func)){func=_168[func];}
for(var x=2;x<arguments.length;x++){_16a.push(arguments[x]);}
var _16c=(func["__preJoinArity"]||func.length)-_16a.length;function gather(_16d,_16e,_16f){var _170=_16f;var _171=_16e.slice(0);for(var x=0;x<_16d.length;x++){_171.push(_16d[x]);}
_16f=_16f-_16d.length;if(_16f<=0){var res=func.apply(_168,_171);_16f=_170;return res;}else{return function(){return gather(arguments,_171,_16f);};}}
return gather([],_16a,_16c);};dojo.lang.curryArguments=function(_174,func,args,_177){var _178=[];var x=_177||0;for(x=_177;x<args.length;x++){_178.push(args[x]);}
return dojo.lang.curry.apply(dojo.lang,[_174,func].concat(_178));};dojo.lang.tryThese=function(){for(var x=0;x<arguments.length;x++){try{if(typeof arguments[x]=="function"){var ret=(arguments[x]());if(ret){return ret;}}}
catch(e){dojo.debug(e);}}};dojo.lang.delayThese=function(farr,cb,_17e,_17f){if(!farr.length){if(typeof _17f=="function"){_17f();}
return;}
if((typeof _17e=="undefined")&&(typeof cb=="number")){_17e=cb;cb=function(){};}else{if(!cb){cb=function(){};if(!_17e){_17e=0;}}}
setTimeout(function(){(farr.shift())();cb();dojo.lang.delayThese(farr,cb,_17e,_17f);},_17e);};dojo.provide("dojo.event.common");dojo.event=new function(){this._canTimeout=dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);function interpolateArgs(args,_181){var dl=dojo.lang;var ao={srcObj:dj_global,srcFunc:null,adviceObj:dj_global,adviceFunc:null,aroundObj:null,aroundFunc:null,adviceType:(args.length>2)?args[0]:"after",precedence:"last",once:false,delay:null,rate:0,adviceMsg:false,maxCalls:-1};switch(args.length){case 0:
return;case 1:
return;case 2:
ao.srcFunc=args[0];ao.adviceFunc=args[1];break;case 3:
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isString(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];ao.adviceFunc=args[2];}else{if((dl.isString(args[1]))&&(dl.isString(args[2]))){ao.srcFunc=args[1];ao.adviceFunc=args[2];}else{if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isFunction(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];var _184=dl.nameAnonFunc(args[2],ao.adviceObj,_181);ao.adviceFunc=_184;}else{if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){ao.adviceType="after";ao.srcObj=dj_global;var _184=dl.nameAnonFunc(args[0],ao.srcObj,_181);ao.srcFunc=_184;ao.adviceObj=args[1];ao.adviceFunc=args[2];}}}}
break;case 4:
if((dl.isObject(args[0]))&&(dl.isObject(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isString(args[1]))&&(dl.isObject(args[2]))){ao.adviceType=args[0];ao.srcObj=dj_global;ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isFunction(args[1]))&&(dl.isObject(args[2]))){ao.adviceType=args[0];ao.srcObj=dj_global;var _184=dl.nameAnonFunc(args[1],dj_global,_181);ao.srcFunc=_184;ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){ao.srcObj=args[1];ao.srcFunc=args[2];var _184=dl.nameAnonFunc(args[3],dj_global,_181);ao.adviceObj=dj_global;ao.adviceFunc=_184;}else{if(dl.isObject(args[1])){ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=dj_global;ao.adviceFunc=args[3];}else{if(dl.isObject(args[2])){ao.srcObj=dj_global;ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{ao.srcObj=ao.adviceObj=ao.aroundObj=dj_global;ao.srcFunc=args[1];ao.adviceFunc=args[2];ao.aroundFunc=args[3];}}}}}}
break;case 6:
ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=args[3];ao.adviceFunc=args[4];ao.aroundFunc=args[5];ao.aroundObj=dj_global;break;default:
ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=args[3];ao.adviceFunc=args[4];ao.aroundObj=args[5];ao.aroundFunc=args[6];ao.once=args[7];ao.delay=args[8];ao.rate=args[9];ao.adviceMsg=args[10];ao.maxCalls=(!isNaN(parseInt(args[11])))?args[11]:-1;break;}
if(dl.isFunction(ao.aroundFunc)){var _184=dl.nameAnonFunc(ao.aroundFunc,ao.aroundObj,_181);ao.aroundFunc=_184;}
if(dl.isFunction(ao.srcFunc)){ao.srcFunc=dl.getNameInObj(ao.srcObj,ao.srcFunc);}
if(dl.isFunction(ao.adviceFunc)){ao.adviceFunc=dl.getNameInObj(ao.adviceObj,ao.adviceFunc);}
if((ao.aroundObj)&&(dl.isFunction(ao.aroundFunc))){ao.aroundFunc=dl.getNameInObj(ao.aroundObj,ao.aroundFunc);}
if(!ao.srcObj){dojo.raise("bad srcObj for srcFunc: "+ao.srcFunc);}
if(!ao.adviceObj){dojo.raise("bad adviceObj for adviceFunc: "+ao.adviceFunc);}
if(!ao.adviceFunc){dojo.debug("bad adviceFunc for srcFunc: "+ao.srcFunc);dojo.debugShallow(ao);}
return ao;}
this.connect=function(){if(arguments.length==1){var ao=arguments[0];}else{var ao=interpolateArgs(arguments,true);}
if(dojo.lang.isArray(ao.srcObj)&&ao.srcObj!=""){var _186={};for(var x in ao){_186[x]=ao[x];}
var mjps=[];dojo.lang.forEach(ao.srcObj,function(src){if((dojo.render.html.capable)&&(dojo.lang.isString(src))){src=dojo.byId(src);}
_186.srcObj=src;mjps.push(dojo.event.connect.call(dojo.event,_186));});return mjps;}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);if(ao.adviceFunc){var mjp2=dojo.event.MethodJoinPoint.getForMethod(ao.adviceObj,ao.adviceFunc);}
mjp.kwAddAdvice(ao);return mjp;};this.log=function(a1,a2){var _18e;if((arguments.length==1)&&(typeof a1=="object")){_18e=a1;}else{_18e={srcObj:a1,srcFunc:a2};}
_18e.adviceFunc=function(){var _18f=[];for(var x=0;x<arguments.length;x++){_18f.push(arguments[x]);}
dojo.debug("("+_18e.srcObj+")."+_18e.srcFunc,":",_18f.join(", "));};this.kwConnect(_18e);};this.connectBefore=function(){var args=["before"];for(var i=0;i<arguments.length;i++){args.push(arguments[i]);}
return this.connect.apply(this,args);};this.connectAround=function(){var args=["around"];for(var i=0;i<arguments.length;i++){args.push(arguments[i]);}
return this.connect.apply(this,args);};this.connectOnce=function(){var ao=interpolateArgs(arguments,true);ao.once=true;return this.connect(ao);};this.connectRunOnce=function(){var ao=interpolateArgs(arguments,true);ao.maxCalls=1;return this.connect(ao);};this._kwConnectImpl=function(_197,_198){var fn=(_198)?"disconnect":"connect";if(typeof _197["srcFunc"]=="function"){_197.srcObj=_197["srcObj"]||dj_global;var _19a=dojo.lang.nameAnonFunc(_197.srcFunc,_197.srcObj,true);_197.srcFunc=_19a;}
if(typeof _197["adviceFunc"]=="function"){_197.adviceObj=_197["adviceObj"]||dj_global;var _19a=dojo.lang.nameAnonFunc(_197.adviceFunc,_197.adviceObj,true);_197.adviceFunc=_19a;}
_197.srcObj=_197["srcObj"]||dj_global;_197.adviceObj=_197["adviceObj"]||_197["targetObj"]||dj_global;_197.adviceFunc=_197["adviceFunc"]||_197["targetFunc"];return dojo.event[fn](_197);};this.kwConnect=function(_19b){return this._kwConnectImpl(_19b,false);};this.disconnect=function(){if(arguments.length==1){var ao=arguments[0];}else{var ao=interpolateArgs(arguments,true);}
if(!ao.adviceFunc){return;}
if(dojo.lang.isString(ao.srcFunc)&&(ao.srcFunc.toLowerCase()=="onkey")){if(dojo.render.html.ie){ao.srcFunc="onkeydown";this.disconnect(ao);}
ao.srcFunc="onkeypress";}
if(!ao.srcObj[ao.srcFunc]){return null;}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc,true);mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);return mjp;};this.kwDisconnect=function(_19e){return this._kwConnectImpl(_19e,true);};};dojo.event.MethodInvocation=function(_19f,obj,args){this.jp_=_19f;this.object=obj;this.args=[];for(var x=0;x<args.length;x++){this.args[x]=args[x];}
this.around_index=-1;};dojo.event.MethodInvocation.prototype.proceed=function(){this.around_index++;if(this.around_index>=this.jp_.around.length){return this.jp_.object[this.jp_.methodname].apply(this.jp_.object,this.args);}else{var ti=this.jp_.around[this.around_index];var mobj=ti[0]||dj_global;var meth=ti[1];return mobj[meth].call(mobj,this);}};dojo.event.MethodJoinPoint=function(obj,_1a7){this.object=obj||dj_global;this.methodname=_1a7;this.methodfunc=this.object[_1a7];this.squelch=false;};dojo.event.MethodJoinPoint.getForMethod=function(obj,_1a9){if(!obj){obj=dj_global;}
var ofn=obj[_1a9];if(!ofn){ofn=obj[_1a9]=function(){};if(!obj[_1a9]){dojo.raise("Cannot set do-nothing method on that object "+_1a9);}}else{if((typeof ofn!="function")&&(!dojo.lang.isFunction(ofn))&&(!dojo.lang.isAlien(ofn))){return null;}}
var _1ab=_1a9+"$joinpoint";var _1ac=_1a9+"$joinpoint$method";var _1ad=obj[_1ab];if(!_1ad){var _1ae=false;if(dojo.event["browser"]){if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){_1ae=true;dojo.event.browser.addClobberNodeAttrs(obj,[_1ab,_1ac,_1a9]);}}
var _1af=ofn.length;obj[_1ac]=ofn;_1ad=obj[_1ab]=new dojo.event.MethodJoinPoint(obj,_1ac);if(!_1ae){obj[_1a9]=function(){return _1ad.run.apply(_1ad,arguments);};}else{obj[_1a9]=function(){var args=[];if(!arguments.length){var evt=null;try{if(obj.ownerDocument){evt=obj.ownerDocument.parentWindow.event;}else{if(obj.documentElement){evt=obj.documentElement.ownerDocument.parentWindow.event;}else{if(obj.event){evt=obj.event;}else{evt=window.event;}}}}
catch(e){evt=window.event;}
if(evt){args.push(dojo.event.browser.fixEvent(evt,this));}}else{for(var x=0;x<arguments.length;x++){if((x==0)&&(dojo.event.browser.isEvent(arguments[x]))){args.push(dojo.event.browser.fixEvent(arguments[x],this));}else{args.push(arguments[x]);}}}
return _1ad.run.apply(_1ad,args);};}
obj[_1a9].__preJoinArity=_1af;}
return _1ad;};dojo.lang.extend(dojo.event.MethodJoinPoint,{squelch:false,unintercept:function(){this.object[this.methodname]=this.methodfunc;this.before=[];this.after=[];this.around=[];},disconnect:dojo.lang.forward("unintercept"),run:function(){var obj=this.object||dj_global;var args=arguments;var _1b5=[];for(var x=0;x<args.length;x++){_1b5[x]=args[x];}
var _1b7=function(marr){if(!marr){dojo.debug("Null argument to unrollAdvice()");return;}
var _1b9=marr[0]||dj_global;var _1ba=marr[1];if(!_1b9[_1ba]){dojo.raise("function \""+_1ba+"\" does not exist on \""+_1b9+"\"");}
var _1bb=marr[2]||dj_global;var _1bc=marr[3];var msg=marr[6];var _1be=marr[7];if(_1be>-1){if(_1be==0){return;}
marr[7]--;}
var _1bf;var to={args:[],jp_:this,object:obj,proceed:function(){return _1b9[_1ba].apply(_1b9,to.args);}};to.args=_1b5;var _1c1=parseInt(marr[4]);var _1c2=((!isNaN(_1c1))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));if(marr[5]){var rate=parseInt(marr[5]);var cur=new Date();var _1c5=false;if((marr["last"])&&((cur-marr.last)<=rate)){if(dojo.event._canTimeout){if(marr["delayTimer"]){clearTimeout(marr.delayTimer);}
var tod=parseInt(rate*2);var mcpy=dojo.lang.shallowCopy(marr);marr.delayTimer=setTimeout(function(){mcpy[5]=0;_1b7(mcpy);},tod);}
return;}else{marr.last=cur;}}
if(_1bc){_1bb[_1bc].call(_1bb,to);}else{if((_1c2)&&((dojo.render.html)||(dojo.render.svg))){dj_global["setTimeout"](function(){if(msg){_1b9[_1ba].call(_1b9,to);}else{_1b9[_1ba].apply(_1b9,args);}},_1c1);}else{if(msg){_1b9[_1ba].call(_1b9,to);}else{_1b9[_1ba].apply(_1b9,args);}}}};var _1c8=function(){if(this.squelch){try{return _1b7.apply(this,arguments);}
catch(e){dojo.debug(e);}}else{return _1b7.apply(this,arguments);}};if((this["before"])&&(this.before.length>0)){dojo.lang.forEach(this.before.concat(new Array()),_1c8);}
var _1c9;try{if((this["around"])&&(this.around.length>0)){var mi=new dojo.event.MethodInvocation(this,obj,args);_1c9=mi.proceed();}else{if(this.methodfunc){_1c9=this.object[this.methodname].apply(this.object,args);}}}
catch(e){if(!this.squelch){dojo.debug(e,"when calling",this.methodname,"on",this.object,"with arguments",args);dojo.raise(e);}}
if((this["after"])&&(this.after.length>0)){dojo.lang.forEach(this.after.concat(new Array()),_1c8);}
return (this.methodfunc)?_1c9:null;},getArr:function(kind){var type="after";if((typeof kind=="string")&&(kind.indexOf("before")!=-1)){type="before";}else{if(kind=="around"){type="around";}}
if(!this[type]){this[type]=[];}
return this[type];},kwAddAdvice:function(args){this.addAdvice(args["adviceObj"],args["adviceFunc"],args["aroundObj"],args["aroundFunc"],args["adviceType"],args["precedence"],args["once"],args["delay"],args["rate"],args["adviceMsg"],args["maxCalls"]);},addAdvice:function(_1ce,_1cf,_1d0,_1d1,_1d2,_1d3,once,_1d5,rate,_1d7,_1d8){var arr=this.getArr(_1d2);if(!arr){dojo.raise("bad this: "+this);}
var ao=[_1ce,_1cf,_1d0,_1d1,_1d5,rate,_1d7,_1d8];if(once){if(this.hasAdvice(_1ce,_1cf,_1d2,arr)>=0){return;}}
if(_1d3=="first"){arr.unshift(ao);}else{arr.push(ao);}},hasAdvice:function(_1db,_1dc,_1dd,arr){if(!arr){arr=this.getArr(_1dd);}
var ind=-1;for(var x=0;x<arr.length;x++){var aao=(typeof _1dc=="object")?(new String(_1dc)).toString():_1dc;var a1o=(typeof arr[x][1]=="object")?(new String(arr[x][1])).toString():arr[x][1];if((arr[x][0]==_1db)&&(a1o==aao)){ind=x;}}
return ind;},removeAdvice:function(_1e3,_1e4,_1e5,once){var arr=this.getArr(_1e5);var ind=this.hasAdvice(_1e3,_1e4,_1e5,arr);if(ind==-1){return false;}
while(ind!=-1){arr.splice(ind,1);if(once){break;}
ind=this.hasAdvice(_1e3,_1e4,_1e5,arr);}
return true;}});dojo.provide("dojo.event.browser");dojo._ie_clobber=new function(){this.clobberNodes=[];function nukeProp(node,prop){try{node[prop]=null;}
catch(e){}
try{delete node[prop];}
catch(e){}
try{node.removeAttribute(prop);}
catch(e){}}
this.clobber=function(_1eb){var na;var tna;if(_1eb){tna=_1eb.all||_1eb.getElementsByTagName("*");na=[_1eb];for(var x=0;x<tna.length;x++){if(tna[x]["__doClobber__"]){na.push(tna[x]);}}}else{try{window.onload=null;}
catch(e){}
na=(this.clobberNodes.length)?this.clobberNodes:document.all;}
tna=null;var _1ef={};for(var i=na.length-1;i>=0;i=i-1){var el=na[i];try{if(el&&el["__clobberAttrs__"]){for(var j=0;j<el.__clobberAttrs__.length;j++){nukeProp(el,el.__clobberAttrs__[j]);}
nukeProp(el,"__clobberAttrs__");nukeProp(el,"__doClobber__");}}
catch(e){}}
na=null;};};if(dojo.render.html.ie){dojo.addOnUnload(function(){dojo._ie_clobber.clobber();try{if((dojo["widget"])&&(dojo.widget["manager"])){dojo.widget.manager.destroyAll();}}
catch(e){}
if(dojo.widget){for(var name in dojo.widget._templateCache){if(dojo.widget._templateCache[name].node){dojo.dom.destroyNode(dojo.widget._templateCache[name].node);dojo.widget._templateCache[name].node=null;delete dojo.widget._templateCache[name].node;}}}
try{window.onload=null;}
catch(e){}
try{window.onunload=null;}
catch(e){}
dojo._ie_clobber.clobberNodes=[];});}
dojo.event.browser=new function(){var _1f4=0;this.normalizedEventName=function(_1f5){switch(_1f5){case "CheckboxStateChange":
case "DOMAttrModified":
case "DOMMenuItemActive":
case "DOMMenuItemInactive":
case "DOMMouseScroll":
case "DOMNodeInserted":
case "DOMNodeRemoved":
case "RadioStateChange":
return _1f5;break;default:
var lcn=_1f5.toLowerCase();return (lcn.indexOf("on")==0)?lcn.substr(2):lcn;break;}};this.clean=function(node){if(dojo.render.html.ie){dojo._ie_clobber.clobber(node);}};this.addClobberNode=function(node){if(!dojo.render.html.ie){return;}
if(!node["__doClobber__"]){node.__doClobber__=true;dojo._ie_clobber.clobberNodes.push(node);node.__clobberAttrs__=[];}};this.addClobberNodeAttrs=function(node,_1fa){if(!dojo.render.html.ie){return;}
this.addClobberNode(node);for(var x=0;x<_1fa.length;x++){node.__clobberAttrs__.push(_1fa[x]);}};this.removeListener=function(node,_1fd,fp,_1ff){if(!_1ff){var _1ff=false;}
_1fd=dojo.event.browser.normalizedEventName(_1fd);if(_1fd=="key"){if(dojo.render.html.ie){this.removeListener(node,"onkeydown",fp,_1ff);}
_1fd="keypress";}
if(node.removeEventListener){node.removeEventListener(_1fd,fp,_1ff);}};this.addListener=function(node,_201,fp,_203,_204){if(!node){return;}
if(!_203){var _203=false;}
_201=dojo.event.browser.normalizedEventName(_201);if(_201=="key"){if(dojo.render.html.ie){this.addListener(node,"onkeydown",fp,_203,_204);}
_201="keypress";}
if(!_204){var _205=function(evt){if(!evt){evt=window.event;}
var ret=fp(dojo.event.browser.fixEvent(evt,this));if(_203){dojo.event.browser.stopEvent(evt);}
return ret;};}else{_205=fp;}
if(node.addEventListener){node.addEventListener(_201,_205,_203);return _205;}else{_201="on"+_201;if(typeof node[_201]=="function"){var _208=node[_201];node[_201]=function(e){_208(e);return _205(e);};}else{node[_201]=_205;}
if(dojo.render.html.ie){this.addClobberNodeAttrs(node,[_201]);}
return _205;}};this.isEvent=function(obj){return (typeof obj!="undefined")&&(obj)&&(typeof Event!="undefined")&&(obj.eventPhase);};this.currentEvent=null;this.callListener=function(_20b,_20c){if(typeof _20b!="function"){dojo.raise("listener not a function: "+_20b);}
dojo.event.browser.currentEvent.currentTarget=_20c;return _20b.call(_20c,dojo.event.browser.currentEvent);};this._stopPropagation=function(){dojo.event.browser.currentEvent.cancelBubble=true;};this._preventDefault=function(){dojo.event.browser.currentEvent.returnValue=false;};this.keys={KEY_BACKSPACE:8,KEY_TAB:9,KEY_CLEAR:12,KEY_ENTER:13,KEY_SHIFT:16,KEY_CTRL:17,KEY_ALT:18,KEY_PAUSE:19,KEY_CAPS_LOCK:20,KEY_ESCAPE:27,KEY_SPACE:32,KEY_PAGE_UP:33,KEY_PAGE_DOWN:34,KEY_END:35,KEY_HOME:36,KEY_LEFT_ARROW:37,KEY_UP_ARROW:38,KEY_RIGHT_ARROW:39,KEY_DOWN_ARROW:40,KEY_INSERT:45,KEY_DELETE:46,KEY_HELP:47,KEY_LEFT_WINDOW:91,KEY_RIGHT_WINDOW:92,KEY_SELECT:93,KEY_NUMPAD_0:96,KEY_NUMPAD_1:97,KEY_NUMPAD_2:98,KEY_NUMPAD_3:99,KEY_NUMPAD_4:100,KEY_NUMPAD_5:101,KEY_NUMPAD_6:102,KEY_NUMPAD_7:103,KEY_NUMPAD_8:104,KEY_NUMPAD_9:105,KEY_NUMPAD_MULTIPLY:106,KEY_NUMPAD_PLUS:107,KEY_NUMPAD_ENTER:108,KEY_NUMPAD_MINUS:109,KEY_NUMPAD_PERIOD:110,KEY_NUMPAD_DIVIDE:111,KEY_F1:112,KEY_F2:113,KEY_F3:114,KEY_F4:115,KEY_F5:116,KEY_F6:117,KEY_F7:118,KEY_F8:119,KEY_F9:120,KEY_F10:121,KEY_F11:122,KEY_F12:123,KEY_F13:124,KEY_F14:125,KEY_F15:126,KEY_NUM_LOCK:144,KEY_SCROLL_LOCK:145};this.revKeys=[];for(var key in this.keys){this.revKeys[this.keys[key]]=key;}
this.fixEvent=function(evt,_20f){if(!evt){if(window["event"]){evt=window.event;}}
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
if(evt.ctrlKey||evt.altKey){var _211=evt.keyCode;if(_211>=65&&_211<=90&&evt.shiftKey==false){_211+=32;}
if(_211>=1&&_211<=26&&evt.ctrlKey){_211+=96;}
evt.key=String.fromCharCode(_211);}}}else{if(evt["type"]=="keypress"){if(dojo.render.html.opera){if(evt.which==0){evt.key=evt.keyCode;}else{if(evt.which>0){switch(evt.which){case evt.KEY_SHIFT:
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
var _211=evt.which;if((evt.ctrlKey||evt.altKey||evt.metaKey)&&(evt.which>=65&&evt.which<=90&&evt.shiftKey==false)){_211+=32;}
evt.key=String.fromCharCode(_211);}}}}else{if(dojo.render.html.ie){if(!evt.ctrlKey&&!evt.altKey&&evt.keyCode>=evt.KEY_SPACE){evt.key=String.fromCharCode(evt.keyCode);}}else{if(dojo.render.html.safari){switch(evt.keyCode){case 25:
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
evt.key=evt.charCode>=evt.KEY_SPACE?String.fromCharCode(evt.charCode):evt.keyCode;}}else{evt.key=evt.charCode>0?String.fromCharCode(evt.charCode):evt.keyCode;}}}}}}
if(dojo.render.html.ie){if(!evt.target){evt.target=evt.srcElement;}
if(!evt.currentTarget){evt.currentTarget=(_20f?_20f:evt.srcElement);}
if(!evt.layerX){evt.layerX=evt.offsetX;}
if(!evt.layerY){evt.layerY=evt.offsetY;}
var doc=(evt.srcElement&&evt.srcElement.ownerDocument)?evt.srcElement.ownerDocument:document;var _213=((dojo.render.html.ie55)||(doc["compatMode"]=="BackCompat"))?doc.body:doc.documentElement;if(!evt.pageX){evt.pageX=evt.clientX+(_213.scrollLeft||0);}
if(!evt.pageY){evt.pageY=evt.clientY+(_213.scrollTop||0);}
if(evt.type=="mouseover"){evt.relatedTarget=evt.fromElement;}
if(evt.type=="mouseout"){evt.relatedTarget=evt.toElement;}
this.currentEvent=evt;evt.callListener=this.callListener;evt.stopPropagation=this._stopPropagation;evt.preventDefault=this._preventDefault;}
return evt;};this.stopEvent=function(evt){if(window.event){evt.cancelBubble=true;evt.returnValue=false;}else{evt.preventDefault();evt.stopPropagation();}};};dojo.provide("dojo.string.common");dojo.string.trim=function(str,wh){if(!str.replace){return str;}
if(!str.length){return str;}
var re=(wh>0)?(/^\s+/):(wh<0)?(/\s+$/):(/^\s+|\s+$/g);return str.replace(re,"");};dojo.string.trimStart=function(str){return dojo.string.trim(str,1);};dojo.string.trimEnd=function(str){return dojo.string.trim(str,-1);};dojo.string.repeat=function(str,_21b,_21c){var out="";for(var i=0;i<_21b;i++){out+=str;if(_21c&&i<_21b-1){out+=_21c;}}
return out;};dojo.string.pad=function(str,len,c,dir){var out=String(str);if(!c){c="0";}
if(!dir){dir=1;}
while(out.length<len){if(dir>0){out=c+out;}else{out+=c;}}
return out;};dojo.string.padLeft=function(str,len,c){return dojo.string.pad(str,len,c,1);};dojo.string.padRight=function(str,len,c){return dojo.string.pad(str,len,c,-1);};dojo.provide("dojo.string");dojo.provide("dojo.io.common");dojo.io.transports=[];dojo.io.hdlrFuncNames=["load","error","timeout"];dojo.io.Request=function(url,_22b,_22c,_22d){if((arguments.length==1)&&(arguments[0].constructor==Object)){this.fromKwArgs(arguments[0]);}else{this.url=url;if(_22b){this.mimetype=_22b;}
if(_22c){this.transport=_22c;}
if(arguments.length>=4){this.changeUrl=_22d;}}};dojo.lang.extend(dojo.io.Request,{url:"",mimetype:"text/plain",method:"GET",content:undefined,transport:undefined,changeUrl:undefined,formNode:undefined,sync:false,bindSuccess:false,useCache:false,preventCache:false,load:function(type,data,_230,_231){},error:function(type,_233,_234,_235){},timeout:function(type,_237,_238,_239){},handle:function(type,data,_23c,_23d){},timeoutSeconds:0,abort:function(){},fromKwArgs:function(_23e){if(_23e["url"]){_23e.url=_23e.url.toString();}
if(_23e["formNode"]){_23e.formNode=dojo.byId(_23e.formNode);}
if(!_23e["method"]&&_23e["formNode"]&&_23e["formNode"].method){_23e.method=_23e["formNode"].method;}
if(!_23e["handle"]&&_23e["handler"]){_23e.handle=_23e.handler;}
if(!_23e["load"]&&_23e["loaded"]){_23e.load=_23e.loaded;}
if(!_23e["changeUrl"]&&_23e["changeURL"]){_23e.changeUrl=_23e.changeURL;}
_23e.encoding=dojo.lang.firstValued(_23e["encoding"],djConfig["bindEncoding"],"");_23e.sendTransport=dojo.lang.firstValued(_23e["sendTransport"],djConfig["ioSendTransport"],false);var _23f=dojo.lang.isFunction;for(var x=0;x<dojo.io.hdlrFuncNames.length;x++){var fn=dojo.io.hdlrFuncNames[x];if(_23e[fn]&&_23f(_23e[fn])){continue;}
if(_23e["handle"]&&_23f(_23e["handle"])){_23e[fn]=_23e.handle;}}
dojo.lang.mixin(this,_23e);}});dojo.io.Error=function(msg,type,num){this.message=msg;this.type=type||"unknown";this.number=num||0;};dojo.io.transports.addTransport=function(name){this.push(name);this[name]=dojo.io[name];};dojo.io.bind=function(_246){if(!(_246 instanceof dojo.io.Request)){try{_246=new dojo.io.Request(_246);}
catch(e){dojo.debug(e);}}
var _247="";if(_246["transport"]){_247=_246["transport"];if(!this[_247]){dojo.io.sendBindError(_246,"No dojo.io.bind() transport with name '"+_246["transport"]+"'.");return _246;}
if(!this[_247].canHandle(_246)){dojo.io.sendBindError(_246,"dojo.io.bind() transport with name '"+_246["transport"]+"' cannot handle this type of request.");return _246;}}else{for(var x=0;x<dojo.io.transports.length;x++){var tmp=dojo.io.transports[x];if((this[tmp])&&(this[tmp].canHandle(_246))){_247=tmp;break;}}
if(_247==""){dojo.io.sendBindError(_246,"None of the loaded transports for dojo.io.bind()"+" can handle the request.");return _246;}}
this[_247].bind(_246);_246.bindSuccess=true;return _246;};dojo.io.sendBindError=function(_24a,_24b){if((typeof _24a.error=="function"||typeof _24a.handle=="function")&&(typeof setTimeout=="function"||typeof setTimeout=="object")){var _24c=new dojo.io.Error(_24b);setTimeout(function(){_24a[(typeof _24a.error=="function")?"error":"handle"]("error",_24c,null,_24a);},50);}else{dojo.raise(_24b);}};dojo.io.queueBind=function(_24d){if(!(_24d instanceof dojo.io.Request)){try{_24d=new dojo.io.Request(_24d);}
catch(e){dojo.debug(e);}}
var _24e=_24d.load;_24d.load=function(){dojo.io._queueBindInFlight=false;var ret=_24e.apply(this,arguments);dojo.io._dispatchNextQueueBind();return ret;};var _250=_24d.error;_24d.error=function(){dojo.io._queueBindInFlight=false;var ret=_250.apply(this,arguments);dojo.io._dispatchNextQueueBind();return ret;};dojo.io._bindQueue.push(_24d);dojo.io._dispatchNextQueueBind();return _24d;};dojo.io._dispatchNextQueueBind=function(){if(!dojo.io._queueBindInFlight){dojo.io._queueBindInFlight=true;if(dojo.io._bindQueue.length>0){dojo.io.bind(dojo.io._bindQueue.shift());}else{dojo.io._queueBindInFlight=false;}}};dojo.io._bindQueue=[];dojo.io._queueBindInFlight=false;dojo.io.argsFromMap=function(map,_253,last){var enc=/utf/i.test(_253||"")?encodeURIComponent:dojo.string.encodeAscii;var _256=[];var _257=new Object();for(var name in map){var _259=function(elt){var val=enc(name)+"="+enc(elt);_256[(last==name)?"push":"unshift"](val);};if(!_257[name]){var _25c=map[name];if(dojo.lang.isArray(_25c)){dojo.lang.forEach(_25c,_259);}else{_259(_25c);}}}
return _256.join("&");};dojo.io.setIFrameSrc=function(_25d,src,_25f){try{var r=dojo.render.html;if(!_25f){if(r.safari){_25d.location=src;}else{frames[_25d.name].location=src;}}else{var idoc;if(r.ie){idoc=_25d.contentWindow.document;}else{if(r.safari){idoc=_25d.document;}else{idoc=_25d.contentWindow;}}
if(!idoc){_25d.location=src;return;}else{idoc.location.replace(src);}}}
catch(e){dojo.debug(e);dojo.debug("setIFrameSrc: "+e);}};dojo.provide("dojo.string.extras");dojo.string.substituteParams=function(_262,hash){var map=(typeof hash=="object")?hash:dojo.lang.toArray(arguments,1);return _262.replace(/\%\{(\w+)\}/g,function(_265,key){if(typeof (map[key])!="undefined"&&map[key]!=null){return map[key];}
dojo.raise("Substitution not found: "+key);});};dojo.string.capitalize=function(str){if(!dojo.lang.isString(str)){return "";}
if(arguments.length==0){str=this;}
var _268=str.split(" ");for(var i=0;i<_268.length;i++){_268[i]=_268[i].charAt(0).toUpperCase()+_268[i].substring(1);}
return _268.join(" ");};dojo.string.isBlank=function(str){if(!dojo.lang.isString(str)){return true;}
return (dojo.string.trim(str).length==0);};dojo.string.encodeAscii=function(str){if(!dojo.lang.isString(str)){return str;}
var ret="";var _26d=escape(str);var _26e,re=/%u([0-9A-F]{4})/i;while((_26e=_26d.match(re))){var num=Number("0x"+_26e[1]);var _271=escape("&#"+num+";");ret+=_26d.substring(0,_26e.index)+_271;_26d=_26d.substring(_26e.index+_26e[0].length);}
ret+=_26d.replace(/\+/g,"%2B");return ret;};dojo.string.escape=function(type,str){var args=dojo.lang.toArray(arguments,1);switch(type.toLowerCase()){case "xml":
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
return str;}};dojo.string.escapeXml=function(str,_276){str=str.replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");if(!_276){str=str.replace(/'/gm,"&#39;");}
return str;};dojo.string.escapeSql=function(str){return str.replace(/'/gm,"''");};dojo.string.escapeRegExp=function(str){return str.replace(/\\/gm,"\\\\").replace(/([\f\b\n\t\r[\^$|?*+(){}])/gm,"\\$1");};dojo.string.escapeJavaScript=function(str){return str.replace(/(["'\f\b\n\t\r])/gm,"\\$1");};dojo.string.escapeString=function(str){return ("\""+str.replace(/(["\\])/g,"\\$1")+"\"").replace(/[\f]/g,"\\f").replace(/[\b]/g,"\\b").replace(/[\n]/g,"\\n").replace(/[\t]/g,"\\t").replace(/[\r]/g,"\\r");};dojo.string.summary=function(str,len){if(!len||str.length<=len){return str;}
return str.substring(0,len).replace(/\.+$/,"")+"...";};dojo.string.endsWith=function(str,end,_27f){if(_27f){str=str.toLowerCase();end=end.toLowerCase();}
if((str.length-end.length)<0){return false;}
return str.lastIndexOf(end)==str.length-end.length;};dojo.string.endsWithAny=function(str){for(var i=1;i<arguments.length;i++){if(dojo.string.endsWith(str,arguments[i])){return true;}}
return false;};dojo.string.startsWith=function(str,_283,_284){if(_284){str=str.toLowerCase();_283=_283.toLowerCase();}
return str.indexOf(_283)==0;};dojo.string.startsWithAny=function(str){for(var i=1;i<arguments.length;i++){if(dojo.string.startsWith(str,arguments[i])){return true;}}
return false;};dojo.string.has=function(str){for(var i=1;i<arguments.length;i++){if(str.indexOf(arguments[i])>-1){return true;}}
return false;};dojo.string.normalizeNewlines=function(text,_28a){if(_28a=="\n"){text=text.replace(/\r\n/g,"\n");text=text.replace(/\r/g,"\n");}else{if(_28a=="\r"){text=text.replace(/\r\n/g,"\r");text=text.replace(/\n/g,"\r");}else{text=text.replace(/([^\r])\n/g,"$1\r\n").replace(/\r([^\n])/g,"\r\n$1");}}
return text;};dojo.string.splitEscaped=function(str,_28c){var _28d=[];for(var i=0,_28f=0;i<str.length;i++){if(str.charAt(i)=="\\"){i++;continue;}
if(str.charAt(i)==_28c){_28d.push(str.substring(_28f,i));_28f=i+1;}}
_28d.push(str.substr(_28f));return _28d;};dojo.provide("dojo.dom");dojo.dom.ELEMENT_NODE=1;dojo.dom.ATTRIBUTE_NODE=2;dojo.dom.TEXT_NODE=3;dojo.dom.CDATA_SECTION_NODE=4;dojo.dom.ENTITY_REFERENCE_NODE=5;dojo.dom.ENTITY_NODE=6;dojo.dom.PROCESSING_INSTRUCTION_NODE=7;dojo.dom.COMMENT_NODE=8;dojo.dom.DOCUMENT_NODE=9;dojo.dom.DOCUMENT_TYPE_NODE=10;dojo.dom.DOCUMENT_FRAGMENT_NODE=11;dojo.dom.NOTATION_NODE=12;dojo.dom.dojoml="http://www.dojotoolkit.org/2004/dojoml";dojo.dom.xmlns={svg:"http://www.w3.org/2000/svg",smil:"http://www.w3.org/2001/SMIL20/",mml:"http://www.w3.org/1998/Math/MathML",cml:"http://www.xml-cml.org",xlink:"http://www.w3.org/1999/xlink",xhtml:"http://www.w3.org/1999/xhtml",xul:"http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul",xbl:"http://www.mozilla.org/xbl",fo:"http://www.w3.org/1999/XSL/Format",xsl:"http://www.w3.org/1999/XSL/Transform",xslt:"http://www.w3.org/1999/XSL/Transform",xi:"http://www.w3.org/2001/XInclude",xforms:"http://www.w3.org/2002/01/xforms",saxon:"http://icl.com/saxon",xalan:"http://xml.apache.org/xslt",xsd:"http://www.w3.org/2001/XMLSchema",dt:"http://www.w3.org/2001/XMLSchema-datatypes",xsi:"http://www.w3.org/2001/XMLSchema-instance",rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#",rdfs:"http://www.w3.org/2000/01/rdf-schema#",dc:"http://purl.org/dc/elements/1.1/",dcq:"http://purl.org/dc/qualifiers/1.0","soap-env":"http://schemas.xmlsoap.org/soap/envelope/",wsdl:"http://schemas.xmlsoap.org/wsdl/",AdobeExtensions:"http://ns.adobe.com/AdobeSVGViewerExtensions/3.0/"};dojo.dom.isNode=function(wh){if(typeof Element=="function"){try{return wh instanceof Element;}
catch(e){}}else{return wh&&!isNaN(wh.nodeType);}};dojo.dom.getUniqueId=function(){var _291=dojo.doc();do{var id="dj_unique_"+(++arguments.callee._idIncrement);}while(_291.getElementById(id));return id;};dojo.dom.getUniqueId._idIncrement=0;dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_293,_294){var node=_293.firstChild;while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){node=node.nextSibling;}
if(_294&&node&&node.tagName&&node.tagName.toLowerCase()!=_294.toLowerCase()){node=dojo.dom.nextElement(node,_294);}
return node;};dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_296,_297){var node=_296.lastChild;while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){node=node.previousSibling;}
if(_297&&node&&node.tagName&&node.tagName.toLowerCase()!=_297.toLowerCase()){node=dojo.dom.prevElement(node,_297);}
return node;};dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(node,_29a){if(!node){return null;}
do{node=node.nextSibling;}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);if(node&&_29a&&_29a.toLowerCase()!=node.tagName.toLowerCase()){return dojo.dom.nextElement(node,_29a);}
return node;};dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(node,_29c){if(!node){return null;}
if(_29c){_29c=_29c.toLowerCase();}
do{node=node.previousSibling;}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);if(node&&_29c&&_29c.toLowerCase()!=node.tagName.toLowerCase()){return dojo.dom.prevElement(node,_29c);}
return node;};dojo.dom.moveChildren=function(_29d,_29e,trim){var _2a0=0;if(trim){while(_29d.hasChildNodes()&&_29d.firstChild.nodeType==dojo.dom.TEXT_NODE){_29d.removeChild(_29d.firstChild);}
while(_29d.hasChildNodes()&&_29d.lastChild.nodeType==dojo.dom.TEXT_NODE){_29d.removeChild(_29d.lastChild);}}
while(_29d.hasChildNodes()){_29e.appendChild(_29d.firstChild);_2a0++;}
return _2a0;};dojo.dom.copyChildren=function(_2a1,_2a2,trim){var _2a4=_2a1.cloneNode(true);return this.moveChildren(_2a4,_2a2,trim);};dojo.dom.replaceChildren=function(node,_2a6){var _2a7=[];if(dojo.render.html.ie){for(var i=0;i<node.childNodes.length;i++){_2a7.push(node.childNodes[i]);}}
dojo.dom.removeChildren(node);node.appendChild(_2a6);for(var i=0;i<_2a7.length;i++){dojo.dom.destroyNode(_2a7[i]);}};dojo.dom.removeChildren=function(node){var _2aa=node.childNodes.length;while(node.hasChildNodes()){dojo.dom.removeNode(node.firstChild);}
return _2aa;};dojo.dom.replaceNode=function(node,_2ac){return node.parentNode.replaceChild(_2ac,node);};dojo.dom.destroyNode=function(node){if(node.parentNode){node=dojo.dom.removeNode(node);}
if(node.nodeType!=3){if(dojo.evalObjPath("dojo.event.browser.clean",false)){dojo.event.browser.clean(node);}
if(dojo.render.html.ie){node.outerHTML="";}}};dojo.dom.removeNode=function(node){if(node&&node.parentNode){return node.parentNode.removeChild(node);}};dojo.dom.getAncestors=function(node,_2b0,_2b1){var _2b2=[];var _2b3=(_2b0&&(_2b0 instanceof Function||typeof _2b0=="function"));while(node){if(!_2b3||_2b0(node)){_2b2.push(node);}
if(_2b1&&_2b2.length>0){return _2b2[0];}
node=node.parentNode;}
if(_2b1){return null;}
return _2b2;};dojo.dom.getAncestorsByTag=function(node,tag,_2b6){tag=tag.toLowerCase();return dojo.dom.getAncestors(node,function(el){return ((el.tagName)&&(el.tagName.toLowerCase()==tag));},_2b6);};dojo.dom.getFirstAncestorByTag=function(node,tag){return dojo.dom.getAncestorsByTag(node,tag,true);};dojo.dom.isDescendantOf=function(node,_2bb,_2bc){if(_2bc&&node){node=node.parentNode;}
while(node){if(node==_2bb){return true;}
node=node.parentNode;}
return false;};dojo.dom.innerXML=function(node){if(node.innerXML){return node.innerXML;}else{if(node.xml){return node.xml;}else{if(typeof XMLSerializer!="undefined"){return (new XMLSerializer()).serializeToString(node);}}}};dojo.dom.createDocument=function(){var doc=null;var _2bf=dojo.doc();if(!dj_undef("ActiveXObject")){var _2c0=["MSXML2","Microsoft","MSXML","MSXML3"];for(var i=0;i<_2c0.length;i++){try{doc=new ActiveXObject(_2c0[i]+".XMLDOM");}
catch(e){}
if(doc){break;}}}else{if((_2bf.implementation)&&(_2bf.implementation.createDocument)){doc=_2bf.implementation.createDocument("","",null);}}
return doc;};dojo.dom.createDocumentFromText=function(str,_2c3){if(!_2c3){_2c3="text/xml";}
if(!dj_undef("DOMParser")){var _2c4=new DOMParser();return _2c4.parseFromString(str,_2c3);}else{if(!dj_undef("ActiveXObject")){var _2c5=dojo.dom.createDocument();if(_2c5){_2c5.async=false;_2c5.loadXML(str);return _2c5;}else{dojo.debug("toXml didn't work?");}}else{var _2c6=dojo.doc();if(_2c6.createElement){var tmp=_2c6.createElement("xml");tmp.innerHTML=str;if(_2c6.implementation&&_2c6.implementation.createDocument){var _2c8=_2c6.implementation.createDocument("foo","",null);for(var i=0;i<tmp.childNodes.length;i++){_2c8.importNode(tmp.childNodes.item(i),true);}
return _2c8;}
return ((tmp.document)&&(tmp.document.firstChild?tmp.document.firstChild:tmp));}}}
return null;};dojo.dom.prependChild=function(node,_2cb){if(_2cb.firstChild){_2cb.insertBefore(node,_2cb.firstChild);}else{_2cb.appendChild(node);}
return true;};dojo.dom.insertBefore=function(node,ref,_2ce){if((_2ce!=true)&&(node===ref||node.nextSibling===ref)){return false;}
var _2cf=ref.parentNode;_2cf.insertBefore(node,ref);return true;};dojo.dom.insertAfter=function(node,ref,_2d2){var pn=ref.parentNode;if(ref==pn.lastChild){if((_2d2!=true)&&(node===ref)){return false;}
pn.appendChild(node);}else{return this.insertBefore(node,ref.nextSibling,_2d2);}
return true;};dojo.dom.insertAtPosition=function(node,ref,_2d6){if((!node)||(!ref)||(!_2d6)){return false;}
switch(_2d6.toLowerCase()){case "before":
return dojo.dom.insertBefore(node,ref);case "after":
return dojo.dom.insertAfter(node,ref);case "first":
if(ref.firstChild){return dojo.dom.insertBefore(node,ref.firstChild);}else{ref.appendChild(node);return true;}
break;default:
ref.appendChild(node);return true;}};dojo.dom.insertAtIndex=function(node,_2d8,_2d9){var _2da=_2d8.childNodes;if(!_2da.length||_2da.length==_2d9){_2d8.appendChild(node);return true;}
if(_2d9==0){return dojo.dom.prependChild(node,_2d8);}
return dojo.dom.insertAfter(node,_2da[_2d9-1]);};dojo.dom.textContent=function(node,text){if(arguments.length>1){var _2dd=dojo.doc();dojo.dom.replaceChildren(node,_2dd.createTextNode(text));return text;}else{if(node.textContent!=undefined){return node.textContent;}
var _2de="";if(node==null){return _2de;}
for(var i=0;i<node.childNodes.length;i++){switch(node.childNodes[i].nodeType){case 1:
case 5:
_2de+=dojo.dom.textContent(node.childNodes[i]);break;case 3:
case 2:
case 4:
_2de+=node.childNodes[i].nodeValue;break;default:
break;}}
return _2de;}};dojo.dom.hasParent=function(node){return Boolean(node&&node.parentNode&&dojo.dom.isNode(node.parentNode));};dojo.dom.isTag=function(node){if(node&&node.tagName){for(var i=1;i<arguments.length;i++){if(node.tagName==String(arguments[i])){return String(arguments[i]);}}}
return "";};dojo.dom.setAttributeNS=function(elem,_2e4,_2e5,_2e6){if(elem==null||((elem==undefined)&&(typeof elem=="undefined"))){dojo.raise("No element given to dojo.dom.setAttributeNS");}
if(!((elem.setAttributeNS==undefined)&&(typeof elem.setAttributeNS=="undefined"))){elem.setAttributeNS(_2e4,_2e5,_2e6);}else{var _2e7=elem.ownerDocument;var _2e8=_2e7.createNode(2,_2e5,_2e4);_2e8.nodeValue=_2e6;elem.setAttributeNode(_2e8);}};dojo.provide("dojo.undo.browser");try{if((!djConfig["preventBackButtonFix"])&&(!dojo.hostenv.post_load_)){document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(djConfig["dojoIframeHistoryUrl"]||dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"'></iframe>");}}
catch(e){}
if(dojo.render.html.opera){dojo.debug("Opera is not supported with dojo.undo.browser, so back/forward detection will not work.");}
dojo.undo.browser={initialHref:(!dj_undef("window"))?window.location.href:"",initialHash:(!dj_undef("window"))?window.location.hash:"",moveForward:false,historyStack:[],forwardStack:[],historyIframe:null,bookmarkAnchor:null,locationTimer:null,setInitialState:function(args){this.initialState=this._createState(this.initialHref,args,this.initialHash);},addToHistory:function(args){this.forwardStack=[];var hash=null;var url=null;if(!this.historyIframe){if(djConfig["useXDomain"]&&!djConfig["dojoIframeHistoryUrl"]){dojo.debug("dojo.undo.browser: When using cross-domain Dojo builds,"+" please save iframe_history.html to your domain and set djConfig.dojoIframeHistoryUrl"+" to the path on your domain to iframe_history.html");}
this.historyIframe=window.frames["djhistory"];}
if(!this.bookmarkAnchor){this.bookmarkAnchor=document.createElement("a");dojo.body().appendChild(this.bookmarkAnchor);this.bookmarkAnchor.style.display="none";}
if(args["changeUrl"]){hash="#"+((args["changeUrl"]!==true)?args["changeUrl"]:(new Date()).getTime());if(this.historyStack.length==0&&this.initialState.urlHash==hash){this.initialState=this._createState(url,args,hash);return;}else{if(this.historyStack.length>0&&this.historyStack[this.historyStack.length-1].urlHash==hash){this.historyStack[this.historyStack.length-1]=this._createState(url,args,hash);return;}}
this.changingUrl=true;setTimeout("window.location.href = '"+hash+"'; dojo.undo.browser.changingUrl = false;",1);this.bookmarkAnchor.href=hash;if(dojo.render.html.ie){url=this._loadIframeHistory();var _2ed=args["back"]||args["backButton"]||args["handle"];var tcb=function(_2ef){if(window.location.hash!=""){setTimeout("window.location.href = '"+hash+"';",1);}
_2ed.apply(this,[_2ef]);};if(args["back"]){args.back=tcb;}else{if(args["backButton"]){args.backButton=tcb;}else{if(args["handle"]){args.handle=tcb;}}}
var _2f0=args["forward"]||args["forwardButton"]||args["handle"];var tfw=function(_2f2){if(window.location.hash!=""){window.location.href=hash;}
if(_2f0){_2f0.apply(this,[_2f2]);}};if(args["forward"]){args.forward=tfw;}else{if(args["forwardButton"]){args.forwardButton=tfw;}else{if(args["handle"]){args.handle=tfw;}}}}else{if(dojo.render.html.moz){if(!this.locationTimer){this.locationTimer=setInterval("dojo.undo.browser.checkLocation();",200);}}}}else{url=this._loadIframeHistory();}
this.historyStack.push(this._createState(url,args,hash));},checkLocation:function(){if(!this.changingUrl){var hsl=this.historyStack.length;if((window.location.hash==this.initialHash||window.location.href==this.initialHref)&&(hsl==1)){this.handleBackButton();return;}
if(this.forwardStack.length>0){if(this.forwardStack[this.forwardStack.length-1].urlHash==window.location.hash){this.handleForwardButton();return;}}
if((hsl>=2)&&(this.historyStack[hsl-2])){if(this.historyStack[hsl-2].urlHash==window.location.hash){this.handleBackButton();return;}}}},iframeLoaded:function(evt,_2f5){if(!dojo.render.html.opera){var _2f6=this._getUrlQuery(_2f5.href);if(_2f6==null){if(this.historyStack.length==1){this.handleBackButton();}
return;}
if(this.moveForward){this.moveForward=false;return;}
if(this.historyStack.length>=2&&_2f6==this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){this.handleBackButton();}else{if(this.forwardStack.length>0&&_2f6==this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){this.handleForwardButton();}}}},handleBackButton:function(){var _2f7=this.historyStack.pop();if(!_2f7){return;}
var last=this.historyStack[this.historyStack.length-1];if(!last&&this.historyStack.length==0){last=this.initialState;}
if(last){if(last.kwArgs["back"]){last.kwArgs["back"]();}else{if(last.kwArgs["backButton"]){last.kwArgs["backButton"]();}else{if(last.kwArgs["handle"]){last.kwArgs.handle("back");}}}}
this.forwardStack.push(_2f7);},handleForwardButton:function(){var last=this.forwardStack.pop();if(!last){return;}
if(last.kwArgs["forward"]){last.kwArgs.forward();}else{if(last.kwArgs["forwardButton"]){last.kwArgs.forwardButton();}else{if(last.kwArgs["handle"]){last.kwArgs.handle("forward");}}}
this.historyStack.push(last);},_createState:function(url,args,hash){return {"url":url,"kwArgs":args,"urlHash":hash};},_getUrlQuery:function(url){var _2fe=url.split("?");if(_2fe.length<2){return null;}else{return _2fe[1];}},_loadIframeHistory:function(){var url=(djConfig["dojoIframeHistoryUrl"]||dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"?"+(new Date()).getTime();this.moveForward=true;dojo.io.setIFrameSrc(this.historyIframe,url,false);return url;}};dojo.provide("dojo.io.BrowserIO");if(!dj_undef("window")){dojo.io.checkChildrenForFile=function(node){var _301=false;var _302=node.getElementsByTagName("input");dojo.lang.forEach(_302,function(_303){if(_301){return;}
if(_303.getAttribute("type")=="file"){_301=true;}});return _301;};dojo.io.formHasFile=function(_304){return dojo.io.checkChildrenForFile(_304);};dojo.io.updateNode=function(node,_306){node=dojo.byId(node);var args=_306;if(dojo.lang.isString(_306)){args={url:_306};}
args.mimetype="text/html";args.load=function(t,d,e){while(node.firstChild){dojo.dom.destroyNode(node.firstChild);}
node.innerHTML=d;};dojo.io.bind(args);};dojo.io.formFilter=function(node){var type=(node.type||"").toLowerCase();return !node.disabled&&node.name&&!dojo.lang.inArray(["file","submit","image","reset","button"],type);};dojo.io.encodeForm=function(_30d,_30e,_30f){if((!_30d)||(!_30d.tagName)||(!_30d.tagName.toLowerCase()=="form")){dojo.raise("Attempted to encode a non-form element.");}
if(!_30f){_30f=dojo.io.formFilter;}
var enc=/utf/i.test(_30e||"")?encodeURIComponent:dojo.string.encodeAscii;var _311=[];for(var i=0;i<_30d.elements.length;i++){var elm=_30d.elements[i];if(!elm||elm.tagName.toLowerCase()=="fieldset"||!_30f(elm)){continue;}
var name=enc(elm.name);var type=elm.type.toLowerCase();if(type=="select-multiple"){for(var j=0;j<elm.options.length;j++){if(elm.options[j].selected){_311.push(name+"="+enc(elm.options[j].value));}}}else{if(dojo.lang.inArray(["radio","checkbox"],type)){if(elm.checked){_311.push(name+"="+enc(elm.value));}}else{_311.push(name+"="+enc(elm.value));}}}
var _317=_30d.getElementsByTagName("input");for(var i=0;i<_317.length;i++){var _318=_317[i];if(_318.type.toLowerCase()=="image"&&_318.form==_30d&&_30f(_318)){var name=enc(_318.name);_311.push(name+"="+enc(_318.value));_311.push(name+".x=0");_311.push(name+".y=0");}}
return _311.join("&")+"&";};dojo.io.FormBind=function(args){this.bindArgs={};if(args&&args.formNode){this.init(args);}else{if(args){this.init({formNode:args});}}};dojo.lang.extend(dojo.io.FormBind,{form:null,bindArgs:null,clickedButton:null,init:function(args){var form=dojo.byId(args.formNode);if(!form||!form.tagName||form.tagName.toLowerCase()!="form"){throw new Error("FormBind: Couldn't apply, invalid form");}else{if(this.form==form){return;}else{if(this.form){throw new Error("FormBind: Already applied to a form");}}}
dojo.lang.mixin(this.bindArgs,args);this.form=form;this.connect(form,"onsubmit","submit");for(var i=0;i<form.elements.length;i++){var node=form.elements[i];if(node&&node.type&&dojo.lang.inArray(["submit","button"],node.type.toLowerCase())){this.connect(node,"onclick","click");}}
var _31e=form.getElementsByTagName("input");for(var i=0;i<_31e.length;i++){var _31f=_31e[i];if(_31f.type.toLowerCase()=="image"&&_31f.form==form){this.connect(_31f,"onclick","click");}}},onSubmit:function(form){return true;},submit:function(e){e.preventDefault();if(this.onSubmit(this.form)){dojo.io.bind(dojo.lang.mixin(this.bindArgs,{formFilter:dojo.lang.hitch(this,"formFilter")}));}},click:function(e){var node=e.currentTarget;if(node.disabled){return;}
this.clickedButton=node;},formFilter:function(node){var type=(node.type||"").toLowerCase();var _326=false;if(node.disabled||!node.name){_326=false;}else{if(dojo.lang.inArray(["submit","button","image"],type)){if(!this.clickedButton){this.clickedButton=node;}
_326=node==this.clickedButton;}else{_326=!dojo.lang.inArray(["file","submit","reset","button"],type);}}
return _326;},connect:function(_327,_328,_329){if(dojo.evalObjPath("dojo.event.connect")){dojo.event.connect(_327,_328,this,_329);}else{var fcn=dojo.lang.hitch(this,_329);_327[_328]=function(e){if(!e){e=window.event;}
if(!e.currentTarget){e.currentTarget=e.srcElement;}
if(!e.preventDefault){e.preventDefault=function(){window.event.returnValue=false;};}
fcn(e);};}}});dojo.io.XMLHTTPTransport=new function(){var _32c=this;var _32d={};this.useCache=false;this.preventCache=false;function getCacheKey(url,_32f,_330){return url+"|"+_32f+"|"+_330.toLowerCase();}
function addToCache(url,_332,_333,http){_32d[getCacheKey(url,_332,_333)]=http;}
function getFromCache(url,_336,_337){return _32d[getCacheKey(url,_336,_337)];}
this.clearCache=function(){_32d={};};function doLoad(_338,http,url,_33b,_33c){if(((http.status>=200)&&(http.status<300))||(http.status==304)||(location.protocol=="file:"&&(http.status==0||http.status==undefined))||(location.protocol=="chrome:"&&(http.status==0||http.status==undefined))){var ret;if(_338.method.toLowerCase()=="head"){var _33e=http.getAllResponseHeaders();ret={};ret.toString=function(){return _33e;};var _33f=_33e.split(/[\r\n]+/g);for(var i=0;i<_33f.length;i++){var pair=_33f[i].match(/^([^:]+)\s*:\s*(.+)$/i);if(pair){ret[pair[1]]=pair[2];}}}else{if(_338.mimetype=="text/javascript"){try{ret=dj_eval(http.responseText);}
catch(e){dojo.debug(e);dojo.debug(http.responseText);ret=null;}}else{if(_338.mimetype=="text/json"||_338.mimetype=="application/json"){try{ret=dj_eval("("+http.responseText+")");}
catch(e){dojo.debug(e);dojo.debug(http.responseText);ret=false;}}else{if((_338.mimetype=="application/xml")||(_338.mimetype=="text/xml")){ret=http.responseXML;if(!ret||typeof ret=="string"||!http.getResponseHeader("Content-Type")){ret=dojo.dom.createDocumentFromText(http.responseText);}}else{ret=http.responseText;}}}}
if(_33c){addToCache(url,_33b,_338.method,http);}
_338[(typeof _338.load=="function")?"load":"handle"]("load",ret,http,_338);}else{var _342=new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);_338[(typeof _338.error=="function")?"error":"handle"]("error",_342,http,_338);}}
function setHeaders(http,_344){if(_344["headers"]){for(var _345 in _344["headers"]){if(_345.toLowerCase()=="content-type"&&!_344["contentType"]){_344["contentType"]=_344["headers"][_345];}else{http.setRequestHeader(_345,_344["headers"][_345]);}}}}
this.inFlight=[];this.inFlightTimer=null;this.startWatchingInFlight=function(){if(!this.inFlightTimer){this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);}};this.watchInFlight=function(){var now=null;if(!dojo.hostenv._blockAsync&&!_32c._blockAsync){for(var x=this.inFlight.length-1;x>=0;x--){try{var tif=this.inFlight[x];if(!tif||tif.http._aborted||!tif.http.readyState){this.inFlight.splice(x,1);continue;}
if(4==tif.http.readyState){this.inFlight.splice(x,1);doLoad(tif.req,tif.http,tif.url,tif.query,tif.useCache);}else{if(tif.startTime){if(!now){now=(new Date()).getTime();}
if(tif.startTime+(tif.req.timeoutSeconds*1000)<now){if(typeof tif.http.abort=="function"){tif.http.abort();}
this.inFlight.splice(x,1);tif.req[(typeof tif.req.timeout=="function")?"timeout":"handle"]("timeout",null,tif.http,tif.req);}}}}
catch(e){try{var _349=new dojo.io.Error("XMLHttpTransport.watchInFlight Error: "+e);tif.req[(typeof tif.req.error=="function")?"error":"handle"]("error",_349,tif.http,tif.req);}
catch(e2){dojo.debug("XMLHttpTransport error callback failed: "+e2);}}}}
clearTimeout(this.inFlightTimer);if(this.inFlight.length==0){this.inFlightTimer=null;return;}
this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);};var _34a=dojo.hostenv.getXmlhttpObject()?true:false;this.canHandle=function(_34b){return _34a&&dojo.lang.inArray(["text/plain","text/html","application/xml","text/xml","text/javascript","text/json","application/json"],(_34b["mimetype"].toLowerCase()||""))&&!(_34b["formNode"]&&dojo.io.formHasFile(_34b["formNode"]));};this.multipartBoundary="45309FFF-BD65-4d50-99C9-36986896A96F";this.bind=function(_34c){if(!_34c["url"]){if(!_34c["formNode"]&&(_34c["backButton"]||_34c["back"]||_34c["changeUrl"]||_34c["watchForURL"])&&(!djConfig.preventBackButtonFix)){dojo.deprecated("Using dojo.io.XMLHTTPTransport.bind() to add to browser history without doing an IO request","Use dojo.undo.browser.addToHistory() instead.","0.4");dojo.undo.browser.addToHistory(_34c);return true;}}
var url=_34c.url;var _34e="";if(_34c["formNode"]){var ta=_34c.formNode.getAttribute("action");if((ta)&&(!_34c["url"])){url=ta;}
var tp=_34c.formNode.getAttribute("method");if((tp)&&(!_34c["method"])){_34c.method=tp;}
_34e+=dojo.io.encodeForm(_34c.formNode,_34c.encoding,_34c["formFilter"]);}
if(url.indexOf("#")>-1){dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);url=url.split("#")[0];}
if(_34c["file"]){_34c.method="post";}
if(!_34c["method"]){_34c.method="get";}
if(_34c.method.toLowerCase()=="get"){_34c.multipart=false;}else{if(_34c["file"]){_34c.multipart=true;}else{if(!_34c["multipart"]){_34c.multipart=false;}}}
if(_34c["backButton"]||_34c["back"]||_34c["changeUrl"]){dojo.undo.browser.addToHistory(_34c);}
var _351=_34c["content"]||{};if(_34c.sendTransport){_351["dojo.transport"]="xmlhttp";}
do{if(_34c.postContent){_34e=_34c.postContent;break;}
if(_351){_34e+=dojo.io.argsFromMap(_351,_34c.encoding);}
if(_34c.method.toLowerCase()=="get"||!_34c.multipart){break;}
var t=[];if(_34e.length){var q=_34e.split("&");for(var i=0;i<q.length;++i){if(q[i].length){var p=q[i].split("=");t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+p[0]+"\"","",p[1]);}}}
if(_34c.file){if(dojo.lang.isArray(_34c.file)){for(var i=0;i<_34c.file.length;++i){var o=_34c.file[i];t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);}}else{var o=_34c.file;t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);}}
if(t.length){t.push("--"+this.multipartBoundary+"--","");_34e=t.join("\r\n");}}while(false);var _357=_34c["sync"]?false:true;var _358=_34c["preventCache"]||(this.preventCache==true&&_34c["preventCache"]!=false);var _359=_34c["useCache"]==true||(this.useCache==true&&_34c["useCache"]!=false);if(!_358&&_359){var _35a=getFromCache(url,_34e,_34c.method);if(_35a){doLoad(_34c,_35a,url,_34e,false);return;}}
var http=dojo.hostenv.getXmlhttpObject(_34c);var _35c=false;if(_357){var _35d=this.inFlight.push({"req":_34c,"http":http,"url":url,"query":_34e,"useCache":_359,"startTime":_34c.timeoutSeconds?(new Date()).getTime():0});this.startWatchingInFlight();}else{_32c._blockAsync=true;}
if(_34c.method.toLowerCase()=="post"){if(!_34c.user){http.open("POST",url,_357);}else{http.open("POST",url,_357,_34c.user,_34c.password);}
setHeaders(http,_34c);http.setRequestHeader("Content-Type",_34c.multipart?("multipart/form-data; boundary="+this.multipartBoundary):(_34c.contentType||"application/x-www-form-urlencoded"));try{http.send(_34e);}
catch(e){if(typeof http.abort=="function"){http.abort();}
doLoad(_34c,{status:404},url,_34e,_359);}}else{var _35e=url;if(_34e!=""){_35e+=(_35e.indexOf("?")>-1?"&":"?")+_34e;}
if(_358){_35e+=(dojo.string.endsWithAny(_35e,"?","&")?"":(_35e.indexOf("?")>-1?"&":"?"))+"dojo.preventCache="+new Date().valueOf();}
if(!_34c.user){http.open(_34c.method.toUpperCase(),_35e,_357);}else{http.open(_34c.method.toUpperCase(),_35e,_357,_34c.user,_34c.password);}
setHeaders(http,_34c);try{http.send(null);}
catch(e){if(typeof http.abort=="function"){http.abort();}
doLoad(_34c,{status:404},url,_34e,_359);}}
if(!_357){doLoad(_34c,http,url,_34e,_359);_32c._blockAsync=false;}
_34c.abort=function(){try{http._aborted=true;}
catch(e){}
return http.abort();};return;};dojo.io.transports.addTransport("XMLHTTPTransport");};}
dojo.provide("dojo.xml.Parse");dojo.xml.Parse=function(){var isIE=((dojo.render.html.capable)&&(dojo.render.html.ie));function getTagName(node){try{return node.tagName.toLowerCase();}
catch(e){return "";}}
function getDojoTagName(node){var _362=getTagName(node);if(!_362){return "";}
if((dojo.widget)&&(dojo.widget.tags[_362])){return _362;}
var p=_362.indexOf(":");if(p>=0){return _362;}
if(_362.substr(0,5)=="dojo:"){return _362;}
if(dojo.render.html.capable&&dojo.render.html.ie&&node.scopeName!="HTML"){return node.scopeName.toLowerCase()+":"+_362;}
if(_362.substr(0,4)=="dojo"){return "dojo:"+_362.substring(4);}
var djt=node.getAttribute("dojoType")||node.getAttribute("dojotype");if(djt){if(djt.indexOf(":")<0){djt="dojo:"+djt;}
return djt.toLowerCase();}
djt=node.getAttributeNS&&node.getAttributeNS(dojo.dom.dojoml,"type");if(djt){return "dojo:"+djt.toLowerCase();}
try{djt=node.getAttribute("dojo:type");}
catch(e){}
if(djt){return "dojo:"+djt.toLowerCase();}
if((dj_global["djConfig"])&&(!djConfig["ignoreClassNames"])){var _365=node.className||node.getAttribute("class");if((_365)&&(_365.indexOf)&&(_365.indexOf("dojo-")!=-1)){var _366=_365.split(" ");for(var x=0,c=_366.length;x<c;x++){if(_366[x].slice(0,5)=="dojo-"){return "dojo:"+_366[x].substr(5).toLowerCase();}}}}
return "";}
this.parseElement=function(node,_36a,_36b,_36c){var _36d=getTagName(node);if(isIE&&_36d.indexOf("/")==0){return null;}
try{var attr=node.getAttribute("parseWidgets");if(attr&&attr.toLowerCase()=="false"){return {};}}
catch(e){}
var _36f=true;if(_36b){var _370=getDojoTagName(node);_36d=_370||_36d;_36f=Boolean(_370);}
var _371={};_371[_36d]=[];var pos=_36d.indexOf(":");if(pos>0){var ns=_36d.substring(0,pos);_371["ns"]=ns;if((dojo.ns)&&(!dojo.ns.allow(ns))){_36f=false;}}
if(_36f){var _374=this.parseAttributes(node);for(var attr in _374){if((!_371[_36d][attr])||(typeof _371[_36d][attr]!="array")){_371[_36d][attr]=[];}
_371[_36d][attr].push(_374[attr]);}
_371[_36d].nodeRef=node;_371.tagName=_36d;_371.index=_36c||0;}
var _375=0;for(var i=0;i<node.childNodes.length;i++){var tcn=node.childNodes.item(i);switch(tcn.nodeType){case dojo.dom.ELEMENT_NODE:
var ctn=getDojoTagName(tcn)||getTagName(tcn);if(!_371[ctn]){_371[ctn]=[];}
_371[ctn].push(this.parseElement(tcn,true,_36b,_375));if((tcn.childNodes.length==1)&&(tcn.childNodes.item(0).nodeType==dojo.dom.TEXT_NODE)){_371[ctn][_371[ctn].length-1].value=tcn.childNodes.item(0).nodeValue;}
_375++;break;case dojo.dom.TEXT_NODE:
if(node.childNodes.length==1){_371[_36d].push({value:node.childNodes.item(0).nodeValue});}
break;default:
break;}}
return _371;};this.parseAttributes=function(node){var _37a={};var atts=node.attributes;var _37c,i=0;while((_37c=atts[i++])){if(isIE){if(!_37c){continue;}
if((typeof _37c=="object")&&(typeof _37c.nodeValue=="undefined")||(_37c.nodeValue==null)||(_37c.nodeValue=="")){continue;}}
var nn=_37c.nodeName.split(":");nn=(nn.length==2)?nn[1]:_37c.nodeName;_37a[nn]={value:_37c.nodeValue};}
return _37a;};};dojo.provide("dojo.lang.declare");dojo.lang.declare=function(_37f,_380,init,_382){if((dojo.lang.isFunction(_382))||((!_382)&&(!dojo.lang.isFunction(init)))){var temp=_382;_382=init;init=temp;}
var _384=[];if(dojo.lang.isArray(_380)){_384=_380;_380=_384.shift();}
if(!init){init=dojo.evalObjPath(_37f,false);if((init)&&(!dojo.lang.isFunction(init))){init=null;}}
var ctor=dojo.lang.declare._makeConstructor();var scp=(_380?_380.prototype:null);if(scp){scp.prototyping=true;ctor.prototype=new _380();scp.prototyping=false;}
ctor.superclass=scp;ctor.mixins=_384;for(var i=0,l=_384.length;i<l;i++){dojo.lang.extend(ctor,_384[i].prototype);}
ctor.prototype.initializer=null;ctor.prototype.declaredClass=_37f;if(dojo.lang.isArray(_382)){dojo.lang.extend.apply(dojo.lang,[ctor].concat(_382));}else{dojo.lang.extend(ctor,(_382)||{});}
dojo.lang.extend(ctor,dojo.lang.declare._common);ctor.prototype.constructor=ctor;ctor.prototype.initializer=(ctor.prototype.initializer)||(init)||(function(){});var _389=dojo.parseObjPath(_37f,null,true);_389.obj[_389.prop]=ctor;return ctor;};dojo.lang.declare._makeConstructor=function(){return function(){var self=this._getPropContext();var s=self.constructor.superclass;if((s)&&(s.constructor)){if(s.constructor==arguments.callee){this._inherited("constructor",arguments);}else{this._contextMethod(s,"constructor",arguments);}}
var ms=(self.constructor.mixins)||([]);for(var i=0,m;(m=ms[i]);i++){(((m.prototype)&&(m.prototype.initializer))||(m)).apply(this,arguments);}
if((!this.prototyping)&&(self.initializer)){self.initializer.apply(this,arguments);}};};dojo.lang.declare._common={_getPropContext:function(){return (this.___proto||this);},_contextMethod:function(_38f,_390,args){var _392,_393=this.___proto;this.___proto=_38f;try{_392=_38f[_390].apply(this,(args||[]));}
catch(e){throw e;}
finally{this.___proto=_393;}
return _392;},_inherited:function(prop,args){var p=this._getPropContext();do{if((!p.constructor)||(!p.constructor.superclass)){return;}
p=p.constructor.superclass;}while(!(prop in p));return (dojo.lang.isFunction(p[prop])?this._contextMethod(p,prop,args):p[prop]);},inherited:function(prop,args){dojo.deprecated("'inherited' method is dangerous, do not up-call! 'inherited' is slated for removal in 0.5; name your super class (or use superclass property) instead.","0.5");this._inherited(prop,args);}};dojo.declare=dojo.lang.declare;dojo.provide("dojo.ns");dojo.ns={namespaces:{},failed:{},loading:{},loaded:{},register:function(name,_39a,_39b,_39c){if(!_39c||!this.namespaces[name]){this.namespaces[name]=new dojo.ns.Ns(name,_39a,_39b);}},allow:function(name){if(this.failed[name]){return false;}
if((djConfig.excludeNamespace)&&(dojo.lang.inArray(djConfig.excludeNamespace,name))){return false;}
return ((name==this.dojo)||(!djConfig.includeNamespace)||(dojo.lang.inArray(djConfig.includeNamespace,name)));},get:function(name){return this.namespaces[name];},require:function(name){var ns=this.namespaces[name];if((ns)&&(this.loaded[name])){return ns;}
if(!this.allow(name)){return false;}
if(this.loading[name]){dojo.debug("dojo.namespace.require: re-entrant request to load namespace \""+name+"\" must fail.");return false;}
var req=dojo.require;this.loading[name]=true;try{if(name=="dojo"){req("dojo.namespaces.dojo");}else{if(!dojo.hostenv.moduleHasPrefix(name)){dojo.registerModulePath(name,"../"+name);}
req([name,"manifest"].join("."),false,true);}
if(!this.namespaces[name]){this.failed[name]=true;}}
finally{this.loading[name]=false;}
return this.namespaces[name];}};dojo.ns.Ns=function(name,_3a3,_3a4){this.name=name;this.module=_3a3;this.resolver=_3a4;this._loaded=[];this._failed=[];};dojo.ns.Ns.prototype.resolve=function(name,_3a6,_3a7){if(!this.resolver||djConfig["skipAutoRequire"]){return false;}
var _3a8=this.resolver(name,_3a6);if((_3a8)&&(!this._loaded[_3a8])&&(!this._failed[_3a8])){var req=dojo.require;req(_3a8,false,true);if(dojo.hostenv.findModule(_3a8,false)){this._loaded[_3a8]=true;}else{if(!_3a7){dojo.raise("dojo.ns.Ns.resolve: module '"+_3a8+"' not found after loading via namespace '"+this.name+"'");}
this._failed[_3a8]=true;}}
return Boolean(this._loaded[_3a8]);};dojo.registerNamespace=function(name,_3ab,_3ac){dojo.ns.register.apply(dojo.ns,arguments);};dojo.registerNamespaceResolver=function(name,_3ae){var n=dojo.ns.namespaces[name];if(n){n.resolver=_3ae;}};dojo.registerNamespaceManifest=function(_3b0,path,name,_3b3,_3b4){dojo.registerModulePath(name,path);dojo.registerNamespace(name,_3b3,_3b4);};dojo.registerNamespace("dojo","dojo.widget");dojo.provide("dojo.event.topic");dojo.event.topic=new function(){this.topics={};this.getTopic=function(_3b5){if(!this.topics[_3b5]){this.topics[_3b5]=new this.TopicImpl(_3b5);}
return this.topics[_3b5];};this.registerPublisher=function(_3b6,obj,_3b8){var _3b6=this.getTopic(_3b6);_3b6.registerPublisher(obj,_3b8);};this.subscribe=function(_3b9,obj,_3bb){var _3b9=this.getTopic(_3b9);_3b9.subscribe(obj,_3bb);};this.unsubscribe=function(_3bc,obj,_3be){var _3bc=this.getTopic(_3bc);_3bc.unsubscribe(obj,_3be);};this.destroy=function(_3bf){this.getTopic(_3bf).destroy();delete this.topics[_3bf];};this.publishApply=function(_3c0,args){var _3c0=this.getTopic(_3c0);_3c0.sendMessage.apply(_3c0,args);};this.publish=function(_3c2,_3c3){var _3c2=this.getTopic(_3c2);var args=[];for(var x=1;x<arguments.length;x++){args.push(arguments[x]);}
_3c2.sendMessage.apply(_3c2,args);};};dojo.event.topic.TopicImpl=function(_3c6){this.topicName=_3c6;this.subscribe=function(_3c7,_3c8){var tf=_3c8||_3c7;var to=(!_3c8)?dj_global:_3c7;return dojo.event.kwConnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});};this.unsubscribe=function(_3cb,_3cc){var tf=(!_3cc)?_3cb:_3cc;var to=(!_3cc)?null:_3cb;return dojo.event.kwDisconnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});};this._getJoinPoint=function(){return dojo.event.MethodJoinPoint.getForMethod(this,"sendMessage");};this.setSquelch=function(_3cf){this._getJoinPoint().squelch=_3cf;};this.destroy=function(){this._getJoinPoint().disconnect();};this.registerPublisher=function(_3d0,_3d1){dojo.event.connect(_3d0,_3d1,this,"sendMessage");};this.sendMessage=function(_3d2){};};dojo.kwCompoundRequire({common:["dojo.event.common","dojo.event.topic"],browser:["dojo.event.browser"],dashboard:["dojo.event.browser"]});dojo.provide("dojo.event.*");dojo.provide("dojo.widget.Manager");dojo.widget.manager=new function(){this.widgets=[];this.widgetIds=[];this.topWidgets={};var _3d3={};var _3d4=[];this.getUniqueId=function(_3d5){var _3d6;do{_3d6=_3d5+"_"+(_3d3[_3d5]!=undefined?++_3d3[_3d5]:_3d3[_3d5]=0);}while(this.getWidgetById(_3d6));return _3d6;};this.add=function(_3d7){this.widgets.push(_3d7);if(!_3d7.extraArgs["id"]){_3d7.extraArgs["id"]=_3d7.extraArgs["ID"];}
if(_3d7.widgetId==""){if(_3d7["id"]){_3d7.widgetId=_3d7["id"];}else{if(_3d7.extraArgs["id"]){_3d7.widgetId=_3d7.extraArgs["id"];}else{_3d7.widgetId=this.getUniqueId(_3d7.ns+"_"+_3d7.widgetType);}}}
if(this.widgetIds[_3d7.widgetId]){dojo.debug("widget ID collision on ID: "+_3d7.widgetId);}
this.widgetIds[_3d7.widgetId]=_3d7;};this.destroyAll=function(){for(var x=this.widgets.length-1;x>=0;x--){try{this.widgets[x].destroy(true);delete this.widgets[x];}
catch(e){}}};this.remove=function(_3d9){if(dojo.lang.isNumber(_3d9)){var tw=this.widgets[_3d9].widgetId;delete this.topWidgets[tw];delete this.widgetIds[tw];this.widgets.splice(_3d9,1);}else{this.removeById(_3d9);}};this.removeById=function(id){if(!dojo.lang.isString(id)){id=id["widgetId"];if(!id){dojo.debug("invalid widget or id passed to removeById");return;}}
for(var i=0;i<this.widgets.length;i++){if(this.widgets[i].widgetId==id){this.remove(i);break;}}};this.getWidgetById=function(id){if(dojo.lang.isString(id)){return this.widgetIds[id];}
return id;};this.getWidgetsByType=function(type){var lt=type.toLowerCase();var _3e0=(type.indexOf(":")<0?function(x){return x.widgetType.toLowerCase();}:function(x){return x.getNamespacedType();});var ret=[];dojo.lang.forEach(this.widgets,function(x){if(_3e0(x)==lt){ret.push(x);}});return ret;};this.getWidgetsByFilter=function(_3e5,_3e6){var ret=[];dojo.lang.every(this.widgets,function(x){if(_3e5(x)){ret.push(x);if(_3e6){return false;}}
return true;});return (_3e6?ret[0]:ret);};this.getAllWidgets=function(){return this.widgets.concat();};this.getWidgetByNode=function(node){var w=this.getAllWidgets();node=dojo.byId(node);for(var i=0;i<w.length;i++){if(w[i].domNode==node){return w[i];}}
return null;};this.byId=this.getWidgetById;this.byType=this.getWidgetsByType;this.byFilter=this.getWidgetsByFilter;this.byNode=this.getWidgetByNode;var _3ec={};var _3ed=["dojo.widget"];for(var i=0;i<_3ed.length;i++){_3ed[_3ed[i]]=true;}
this.registerWidgetPackage=function(_3ef){if(!_3ed[_3ef]){_3ed[_3ef]=true;_3ed.push(_3ef);}};this.getWidgetPackageList=function(){return dojo.lang.map(_3ed,function(elt){return (elt!==true?elt:undefined);});};this.getImplementation=function(_3f1,_3f2,_3f3,ns){var impl=this.getImplementationName(_3f1,ns);if(impl){var ret=_3f2?new impl(_3f2):new impl();return ret;}};function buildPrefixCache(){for(var _3f7 in dojo.render){if(dojo.render[_3f7]["capable"]===true){var _3f8=dojo.render[_3f7].prefixes;for(var i=0;i<_3f8.length;i++){_3d4.push(_3f8[i].toLowerCase());}}}}
var _3fa=function(_3fb,_3fc){if(!_3fc){return null;}
for(var i=0,l=_3d4.length,_3ff;i<=l;i++){_3ff=(i<l?_3fc[_3d4[i]]:_3fc);if(!_3ff){continue;}
for(var name in _3ff){if(name.toLowerCase()==_3fb){return _3ff[name];}}}
return null;};var _401=function(_402,_403){var _404=dojo.evalObjPath(_403,false);return (_404?_3fa(_402,_404):null);};this.getImplementationName=function(_405,ns){var _407=_405.toLowerCase();ns=ns||"dojo";var imps=_3ec[ns]||(_3ec[ns]={});var impl=imps[_407];if(impl){return impl;}
if(!_3d4.length){buildPrefixCache();}
var _40a=dojo.ns.get(ns);if(!_40a){dojo.ns.register(ns,ns+".widget");_40a=dojo.ns.get(ns);}
if(_40a){_40a.resolve(_405);}
impl=_401(_407,_40a.module);if(impl){return (imps[_407]=impl);}
_40a=dojo.ns.require(ns);if((_40a)&&(_40a.resolver)){_40a.resolve(_405);impl=_401(_407,_40a.module);if(impl){return (imps[_407]=impl);}}
dojo.deprecated("dojo.widget.Manager.getImplementationName","Could not locate widget implementation for \""+_405+"\" in \""+_40a.module+"\" registered to namespace \""+_40a.name+"\". "+"Developers must specify correct namespaces for all non-Dojo widgets","0.5");for(var i=0;i<_3ed.length;i++){impl=_401(_407,_3ed[i]);if(impl){return (imps[_407]=impl);}}
throw new Error("Could not locate widget implementation for \""+_405+"\" in \""+_40a.module+"\" registered to namespace \""+_40a.name+"\"");};this.resizing=false;this.onWindowResized=function(){if(this.resizing){return;}
try{this.resizing=true;for(var id in this.topWidgets){var _40d=this.topWidgets[id];if(_40d.checkSize){_40d.checkSize();}}}
catch(e){}
finally{this.resizing=false;}};if(typeof window!="undefined"){dojo.addOnLoad(this,"onWindowResized");dojo.event.connect(window,"onresize",this,"onWindowResized");}};(function(){var dw=dojo.widget;var dwm=dw.manager;var h=dojo.lang.curry(dojo.lang,"hitch",dwm);var g=function(_412,_413){dw[(_413||_412)]=h(_412);};g("add","addWidget");g("destroyAll","destroyAllWidgets");g("remove","removeWidget");g("removeById","removeWidgetById");g("getWidgetById");g("getWidgetById","byId");g("getWidgetsByType");g("getWidgetsByFilter");g("getWidgetsByType","byType");g("getWidgetsByFilter","byFilter");g("getWidgetByNode","byNode");dw.all=function(n){var _415=dwm.getAllWidgets.apply(dwm,arguments);if(arguments.length>0){return _415[n];}
return _415;};g("registerWidgetPackage");g("getImplementation","getWidgetImplementation");g("getImplementationName","getWidgetImplementationName");dw.widgets=dwm.widgets;dw.widgetIds=dwm.widgetIds;dw.root=dwm.root;})();dojo.provide("dojo.uri.Uri");dojo.uri=new function(){var _416=new RegExp("^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$");var _417=new RegExp("(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$");var _418=new RegExp("/(\\w+.css)");this.dojoUri=function(uri){return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri(),uri);};this.moduleUri=function(_41a,uri){var loc=dojo.hostenv.getModuleSymbols(_41a).join("/");if(!loc){return null;}
if(loc.lastIndexOf("/")!=loc.length-1){loc+="/";}
var _41d=loc.indexOf(":");var _41e=loc.indexOf("/");if(loc.charAt(0)!="/"&&(_41d==-1||_41d>_41e)){loc=dojo.hostenv.getBaseScriptUri()+loc;}
return new dojo.uri.Uri(loc,uri);};this.Uri=function(){var uri=arguments[0];if(uri&&arguments.length>1){var _420=_418.exec(uri);if(_420){uri=uri.toString().replace(_420[1],"");}}
for(var i=1;i<arguments.length;i++){if(!arguments[i]){continue;}
var _422=new dojo.uri.Uri(arguments[i].toString());var _423=new dojo.uri.Uri(uri.toString());if((_422.path=="")&&(_422.scheme==null)&&(_422.authority==null)&&(_422.query==null)){if(_422.fragment!=null){_423.fragment=_422.fragment;}
_422=_423;}
if(_422.scheme!=null&&_422.authority!=null){uri="";}
if(_422.scheme!=null){uri+=_422.scheme+":";}
if(_422.authority!=null){uri+="//"+_422.authority;}
uri+=_422.path;if(_422.query!=null){uri+="?"+_422.query;}
if(_422.fragment!=null){uri+="#"+_422.fragment;}}
this.uri=uri.toString();var r=this.uri.match(_417);this.scheme=r[2]||(r[1]?"":null);this.authority=r[4]||(r[3]?"":null);this.path=r[5];this.query=r[7]||(r[6]?"":null);this.fragment=r[9]||(r[8]?"":null);if(this.authority!=null){r=this.authority.match(_416);this.user=r[3]||null;this.password=r[4]||null;this.host=r[5];this.port=r[7]||null;}
this.toString=function(){return this.uri;};};};dojo.kwCompoundRequire({common:[["dojo.uri.Uri",false,false]]});dojo.provide("dojo.uri.*");dojo.provide("dojo.html.common");dojo.lang.mixin(dojo.html,dojo.dom);dojo.html.body=function(){dojo.deprecated("dojo.html.body() moved to dojo.body()","0.5");return dojo.body();};dojo.html.getEventTarget=function(evt){if(!evt){evt=dojo.global().event||{};}
var t=(evt.srcElement?evt.srcElement:(evt.target?evt.target:null));while((t)&&(t.nodeType!=1)){t=t.parentNode;}
return t;};dojo.html.getViewport=function(){var _427=dojo.global();var _428=dojo.doc();var w=0;var h=0;if(dojo.render.html.mozilla){w=_428.documentElement.clientWidth;h=_427.innerHeight;}else{if(!dojo.render.html.opera&&_427.innerWidth){w=_427.innerWidth;h=_427.innerHeight;}else{if(!dojo.render.html.opera&&dojo.exists(_428,"documentElement.clientWidth")){var w2=_428.documentElement.clientWidth;if(!w||w2&&w2<w){w=w2;}
h=_428.documentElement.clientHeight;}else{if(dojo.body().clientWidth){w=dojo.body().clientWidth;h=dojo.body().clientHeight;}}}}
return {width:w,height:h};};dojo.html.getScroll=function(){var _42c=dojo.global();var _42d=dojo.doc();var top=_42c.pageYOffset||_42d.documentElement.scrollTop||dojo.body().scrollTop||0;var left=_42c.pageXOffset||_42d.documentElement.scrollLeft||dojo.body().scrollLeft||0;return {top:top,left:left,offset:{x:left,y:top}};};dojo.html.getParentByType=function(node,type){var _432=dojo.doc();var _433=dojo.byId(node);type=type.toLowerCase();while((_433)&&(_433.nodeName.toLowerCase()!=type)){if(_433==(_432["body"]||_432["documentElement"])){return null;}
_433=_433.parentNode;}
return _433;};dojo.html.getAttribute=function(node,attr){node=dojo.byId(node);if((!node)||(!node.getAttribute)){return null;}
var ta=typeof attr=="string"?attr:new String(attr);var v=node.getAttribute(ta.toUpperCase());if((v)&&(typeof v=="string")&&(v!="")){return v;}
if(v&&v.value){return v.value;}
if((node.getAttributeNode)&&(node.getAttributeNode(ta))){return (node.getAttributeNode(ta)).value;}else{if(node.getAttribute(ta)){return node.getAttribute(ta);}else{if(node.getAttribute(ta.toLowerCase())){return node.getAttribute(ta.toLowerCase());}}}
return null;};dojo.html.hasAttribute=function(node,attr){return dojo.html.getAttribute(dojo.byId(node),attr)?true:false;};dojo.html.getCursorPosition=function(e){e=e||dojo.global().event;var _43b={x:0,y:0};if(e.pageX||e.pageY){_43b.x=e.pageX;_43b.y=e.pageY;}else{var de=dojo.doc().documentElement;var db=dojo.body();_43b.x=e.clientX+((de||db)["scrollLeft"])-((de||db)["clientLeft"]);_43b.y=e.clientY+((de||db)["scrollTop"])-((de||db)["clientTop"]);}
return _43b;};dojo.html.isTag=function(node){node=dojo.byId(node);if(node&&node.tagName){for(var i=1;i<arguments.length;i++){if(node.tagName.toLowerCase()==String(arguments[i]).toLowerCase()){return String(arguments[i]).toLowerCase();}}}
return "";};if(dojo.render.html.ie&&!dojo.render.html.ie70){if(window.location.href.substr(0,6).toLowerCase()!="https:"){(function(){var _440=dojo.doc().createElement("script");_440.src="javascript:'dojo.html.createExternalElement=function(doc, tag){ return doc.createElement(tag); }'";dojo.doc().getElementsByTagName("head")[0].appendChild(_440);})();}}else{dojo.html.createExternalElement=function(doc,tag){return doc.createElement(tag);};}
dojo.html._callDeprecated=function(_443,_444,args,_446,_447){dojo.deprecated("dojo.html."+_443,"replaced by dojo.html."+_444+"("+(_446?"node, {"+_446+": "+_446+"}":"")+")"+(_447?"."+_447:""),"0.5");var _448=[];if(_446){var _449={};_449[_446]=args[1];_448.push(args[0]);_448.push(_449);}else{_448=args;}
var ret=dojo.html[_444].apply(dojo.html,args);if(_447){return ret[_447];}else{return ret;}};dojo.html.getViewportWidth=function(){return dojo.html._callDeprecated("getViewportWidth","getViewport",arguments,null,"width");};dojo.html.getViewportHeight=function(){return dojo.html._callDeprecated("getViewportHeight","getViewport",arguments,null,"height");};dojo.html.getViewportSize=function(){return dojo.html._callDeprecated("getViewportSize","getViewport",arguments);};dojo.html.getScrollTop=function(){return dojo.html._callDeprecated("getScrollTop","getScroll",arguments,null,"top");};dojo.html.getScrollLeft=function(){return dojo.html._callDeprecated("getScrollLeft","getScroll",arguments,null,"left");};dojo.html.getScrollOffset=function(){return dojo.html._callDeprecated("getScrollOffset","getScroll",arguments,null,"offset");};dojo.provide("dojo.a11y");dojo.a11y={imgPath:dojo.uri.moduleUri("dojo.widget","templates/images"),doAccessibleCheck:true,accessible:null,checkAccessible:function(){if(this.accessible===null){this.accessible=false;if(this.doAccessibleCheck==true){this.accessible=this.testAccessible();}}
return this.accessible;},testAccessible:function(){this.accessible=false;if(dojo.render.html.ie||dojo.render.html.mozilla){var div=document.createElement("div");div.style.backgroundImage="url(\""+this.imgPath+"/tab_close.gif\")";dojo.body().appendChild(div);var _44c=null;if(window.getComputedStyle){var _44d=getComputedStyle(div,"");_44c=_44d.getPropertyValue("background-image");}else{_44c=div.currentStyle.backgroundImage;}
var _44e=false;if(_44c!=null&&(_44c=="none"||_44c=="url(invalid-url:)")){this.accessible=true;}
dojo.body().removeChild(div);}
return this.accessible;},setCheckAccessible:function(_44f){this.doAccessibleCheck=_44f;},setAccessibleMode:function(){if(this.accessible===null){if(this.checkAccessible()){dojo.render.html.prefixes.unshift("a11y");}}
return this.accessible;}};dojo.provide("dojo.widget.Widget");dojo.declare("dojo.widget.Widget",null,function(){this.children=[];this.extraArgs={};},{parent:null,isTopLevel:false,disabled:false,isContainer:false,widgetId:"",widgetType:"Widget",ns:"dojo",getNamespacedType:function(){return (this.ns?this.ns+":"+this.widgetType:this.widgetType).toLowerCase();},toString:function(){return "[Widget "+this.getNamespacedType()+", "+(this.widgetId||"NO ID")+"]";},repr:function(){return this.toString();},enable:function(){this.disabled=false;},disable:function(){this.disabled=true;},onResized:function(){this.notifyChildrenOfResize();},notifyChildrenOfResize:function(){for(var i=0;i<this.children.length;i++){var _451=this.children[i];if(_451.onResized){_451.onResized();}}},create:function(args,_453,_454,ns){if(ns){this.ns=ns;}
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
_469[x]=true;if((typeof this[x])!=(typeof _466)){if(typeof args[x]!="string"){this[x]=args[x];}else{if(dojo.lang.isString(this[x])){this[x]=args[x];}else{if(dojo.lang.isNumber(this[x])){this[x]=new Number(args[x]);}else{if(dojo.lang.isBoolean(this[x])){this[x]=(args[x].toLowerCase()=="false")?false:true;}else{if(dojo.lang.isFunction(this[x])){if(args[x].search(/[^\w\.]+/i)==-1){this[x]=dojo.evalObjPath(args[x],false);}else{var tn=dojo.lang.nameAnonFunc(new Function(args[x]),this);dojo.event.kwConnect({srcObj:this,srcFunc:x,adviceObj:this,adviceFunc:tn});}}else{if(dojo.lang.isArray(this[x])){this[x]=args[x].split(";");}else{if(this[x] instanceof Date){this[x]=new Date(Number(args[x]));}else{if(typeof this[x]=="object"){if(this[x] instanceof dojo.uri.Uri){this[x]=dojo.uri.dojoUri(args[x]);}else{var _46b=args[x].split(";");for(var y=0;y<_46b.length;y++){var si=_46b[y].indexOf(":");if((si!=-1)&&(_46b[y].length>si)){this[x][_46b[y].substr(0,si).replace(/^\s+|\s+$/g,"")]=_46b[y].substr(si+1);}}}}else{this[x]=args[x];}}}}}}}}}else{this.extraArgs[x.toLowerCase()]=args[x];}}},postMixInProperties:function(args,frag,_46f){},initialize:function(args,frag,_472){return false;},postInitialize:function(args,frag,_475){return false;},postCreate:function(args,frag,_478){return false;},uninitialize:function(){return false;},buildRendering:function(args,frag,_47b){dojo.unimplemented("dojo.widget.Widget.buildRendering, on "+this.toString()+", ");return false;},destroyRendering:function(){dojo.unimplemented("dojo.widget.Widget.destroyRendering");return false;},addedTo:function(_47c){},addChild:function(_47d){dojo.unimplemented("dojo.widget.Widget.addChild");return false;},removeChild:function(_47e){for(var x=0;x<this.children.length;x++){if(this.children[x]===_47e){this.children.splice(x,1);_47e.parent=null;break;}}
return _47e;},getPreviousSibling:function(){var idx=this.getParentIndex();if(idx<=0){return null;}
return this.parent.children[idx-1];},getSiblings:function(){return this.parent.children;},getParentIndex:function(){return dojo.lang.indexOf(this.parent.children,this,true);},getNextSibling:function(){var idx=this.getParentIndex();if(idx==this.parent.children.length-1){return null;}
if(idx<0){return null;}
return this.parent.children[idx+1];}});dojo.widget.lcArgsCache={};dojo.widget.tags={};dojo.widget.tags.addParseTreeHandler=function(type){dojo.deprecated("addParseTreeHandler",". ParseTreeHandlers are now reserved for components. Any unfiltered DojoML tag without a ParseTreeHandler is assumed to be a widget","0.5");};dojo.widget.tags["dojo:propertyset"]=function(_483,_484,_485){var _486=_484.parseProperties(_483["dojo:propertyset"]);};dojo.widget.tags["dojo:connect"]=function(_487,_488,_489){var _48a=_488.parseProperties(_487["dojo:connect"]);};dojo.widget.buildWidgetFromParseTree=function(type,frag,_48d,_48e,_48f,_490){dojo.a11y.setAccessibleMode();var _491=type.split(":");_491=(_491.length==2)?_491[1]:type;var _492=_490||_48d.parseProperties(frag[frag["ns"]+":"+_491]);var _493=dojo.widget.manager.getImplementation(_491,null,null,frag["ns"]);if(!_493){throw new Error("cannot find \""+type+"\" widget");}else{if(!_493.create){throw new Error("\""+type+"\" widget object has no \"create\" method and does not appear to implement *Widget");}}
_492["dojoinsertionindex"]=_48f;var ret=_493.create(_492,frag,_48e,frag["ns"]);return ret;};dojo.widget.defineWidget=function(_495,_496,_497,init,_499){if(dojo.lang.isString(arguments[3])){dojo.widget._defineWidget(arguments[0],arguments[3],arguments[1],arguments[4],arguments[2]);}else{var args=[arguments[0]],p=3;if(dojo.lang.isString(arguments[1])){args.push(arguments[1],arguments[2]);}else{args.push("",arguments[1]);p=2;}
if(dojo.lang.isFunction(arguments[p])){args.push(arguments[p],arguments[p+1]);}else{args.push(null,arguments[p]);}
dojo.widget._defineWidget.apply(this,args);}};dojo.widget.defineWidget.renderers="html|svg|vml";dojo.widget._defineWidget=function(_49c,_49d,_49e,init,_4a0){var _4a1=_49c.split(".");var type=_4a1.pop();var regx="\\.("+(_49d?_49d+"|":"")+dojo.widget.defineWidget.renderers+")\\.";var r=_49c.search(new RegExp(regx));_4a1=(r<0?_4a1.join("."):_49c.substr(0,r));dojo.widget.manager.registerWidgetPackage(_4a1);var pos=_4a1.indexOf(".");var _4a6=(pos>-1)?_4a1.substring(0,pos):_4a1;_4a0=(_4a0)||{};_4a0.widgetType=type;if((!init)&&(_4a0["classConstructor"])){init=_4a0.classConstructor;delete _4a0.classConstructor;}
dojo.declare(_49c,_49e,init,_4a0);};dojo.provide("dojo.widget.Parse");dojo.widget.Parse=function(_4a7){this.propertySetsList=[];this.fragment=_4a7;this.createComponents=function(frag,_4a9){var _4aa=[];var _4ab=false;try{if(frag&&frag.tagName&&(frag!=frag.nodeRef)){var _4ac=dojo.widget.tags;var tna=String(frag.tagName).split(";");for(var x=0;x<tna.length;x++){var ltn=tna[x].replace(/^\s+|\s+$/g,"").toLowerCase();frag.tagName=ltn;var ret;if(_4ac[ltn]){_4ab=true;ret=_4ac[ltn](frag,this,_4a9,frag.index);_4aa.push(ret);}else{if(ltn.indexOf(":")==-1){ltn="dojo:"+ltn;}
ret=dojo.widget.buildWidgetFromParseTree(ltn,frag,this,_4a9,frag.index);if(ret){_4ab=true;_4aa.push(ret);}}}}}
catch(e){dojo.debug("dojo.widget.Parse: error:",e);}
if(!_4ab){_4aa=_4aa.concat(this.createSubComponents(frag,_4a9));}
return _4aa;};this.createSubComponents=function(_4b1,_4b2){var frag,_4b4=[];for(var item in _4b1){frag=_4b1[item];if(frag&&typeof frag=="object"&&(frag!=_4b1.nodeRef)&&(frag!=_4b1.tagName)&&(!dojo.dom.isNode(frag))){_4b4=_4b4.concat(this.createComponents(frag,_4b2));}}
return _4b4;};this.parsePropertySets=function(_4b6){return [];};this.parseProperties=function(_4b7){var _4b8={};for(var item in _4b7){if((_4b7[item]==_4b7.tagName)||(_4b7[item]==_4b7.nodeRef)){}else{var frag=_4b7[item];if(frag.tagName&&dojo.widget.tags[frag.tagName.toLowerCase()]){}else{if(frag[0]&&frag[0].value!=""&&frag[0].value!=null){try{if(item.toLowerCase()=="dataprovider"){var _4bb=this;this.getDataProvider(_4bb,frag[0].value);_4b8.dataProvider=this.dataProvider;}
_4b8[item]=frag[0].value;var _4bc=this.parseProperties(frag);for(var _4bd in _4bc){_4b8[_4bd]=_4bc[_4bd];}}
catch(e){dojo.debug(e);}}}
switch(item.toLowerCase()){case "checked":
case "disabled":
if(typeof _4b8[item]!="boolean"){_4b8[item]=true;}
break;}}}
return _4b8;};this.getDataProvider=function(_4be,_4bf){dojo.io.bind({url:_4bf,load:function(type,_4c1){if(type=="load"){_4be.dataProvider=_4c1;}},mimetype:"text/javascript",sync:true});};this.getPropertySetById=function(_4c2){for(var x=0;x<this.propertySetsList.length;x++){if(_4c2==this.propertySetsList[x]["id"][0].value){return this.propertySetsList[x];}}
return "";};this.getPropertySetsByType=function(_4c4){var _4c5=[];for(var x=0;x<this.propertySetsList.length;x++){var cpl=this.propertySetsList[x];var cpcc=cpl.componentClass||cpl.componentType||null;var _4c9=this.propertySetsList[x]["id"][0].value;if(cpcc&&(_4c9==cpcc[0].value)){_4c5.push(cpl);}}
return _4c5;};this.getPropertySets=function(_4ca){var ppl="dojo:propertyproviderlist";var _4cc=[];var _4cd=_4ca.tagName;if(_4ca[ppl]){var _4ce=_4ca[ppl].value.split(" ");for(var _4cf in _4ce){if((_4cf.indexOf("..")==-1)&&(_4cf.indexOf("://")==-1)){var _4d0=this.getPropertySetById(_4cf);if(_4d0!=""){_4cc.push(_4d0);}}else{}}}
return this.getPropertySetsByType(_4cd).concat(_4cc);};this.createComponentFromScript=function(_4d1,_4d2,_4d3,ns){_4d3.fastMixIn=true;var ltn=(ns||"dojo")+":"+_4d2.toLowerCase();if(dojo.widget.tags[ltn]){return [dojo.widget.tags[ltn](_4d3,this,null,null,_4d3)];}
return [dojo.widget.buildWidgetFromParseTree(ltn,_4d3,this,null,null,_4d3)];};};dojo.widget._parser_collection={"dojo":new dojo.widget.Parse()};dojo.widget.getParser=function(name){if(!name){name="dojo";}
if(!this._parser_collection[name]){this._parser_collection[name]=new dojo.widget.Parse();}
return this._parser_collection[name];};dojo.widget.createWidget=function(name,_4d8,_4d9,_4da){var _4db=false;var _4dc=(typeof name=="string");if(_4dc){var pos=name.indexOf(":");var ns=(pos>-1)?name.substring(0,pos):"dojo";if(pos>-1){name=name.substring(pos+1);}
var _4df=name.toLowerCase();var _4e0=ns+":"+_4df;_4db=(dojo.byId(name)&&!dojo.widget.tags[_4e0]);}
if((arguments.length==1)&&(_4db||!_4dc)){var xp=new dojo.xml.Parse();var tn=_4db?dojo.byId(name):name;return dojo.widget.getParser().createComponents(xp.parseElement(tn,null,true))[0];}
function fromScript(_4e3,name,_4e5,ns){_4e5[_4e0]={dojotype:[{value:_4df}],nodeRef:_4e3,fastMixIn:true};_4e5.ns=ns;return dojo.widget.getParser().createComponentFromScript(_4e3,name,_4e5,ns);}
_4d8=_4d8||{};var _4e7=false;var tn=null;var h=dojo.render.html.capable;if(h){tn=document.createElement("span");}
if(!_4d9){_4e7=true;_4d9=tn;if(h){dojo.body().appendChild(_4d9);}}else{if(_4da){dojo.dom.insertAtPosition(tn,_4d9,_4da);}else{tn=_4d9;}}
var _4e9=fromScript(tn,name.toLowerCase(),_4d8,ns);if((!_4e9)||(!_4e9[0])||(typeof _4e9[0].widgetType=="undefined")){throw new Error("createWidget: Creation of \""+name+"\" widget failed.");}
try{if(_4e7&&_4e9[0].domNode.parentNode){_4e9[0].domNode.parentNode.removeChild(_4e9[0].domNode);}}
catch(e){dojo.debug(e);}
return _4e9[0];};dojo.provide("dojo.html.style");dojo.html.getClass=function(node){node=dojo.byId(node);if(!node){return "";}
var cs="";if(node.className){cs=node.className;}else{if(dojo.html.hasAttribute(node,"class")){cs=dojo.html.getAttribute(node,"class");}}
return cs.replace(/^\s+|\s+$/g,"");};dojo.html.getClasses=function(node){var c=dojo.html.getClass(node);return (c=="")?[]:c.split(/\s+/g);};dojo.html.hasClass=function(node,_4ef){return (new RegExp("(^|\\s+)"+_4ef+"(\\s+|$)")).test(dojo.html.getClass(node));};dojo.html.prependClass=function(node,_4f1){_4f1+=" "+dojo.html.getClass(node);return dojo.html.setClass(node,_4f1);};dojo.html.addClass=function(node,_4f3){if(dojo.html.hasClass(node,_4f3)){return false;}
_4f3=(dojo.html.getClass(node)+" "+_4f3).replace(/^\s+|\s+$/g,"");return dojo.html.setClass(node,_4f3);};dojo.html.setClass=function(node,_4f5){node=dojo.byId(node);var cs=new String(_4f5);try{if(typeof node.className=="string"){node.className=cs;}else{if(node.setAttribute){node.setAttribute("class",_4f5);node.className=cs;}else{return false;}}}
catch(e){dojo.debug("dojo.html.setClass() failed",e);}
return true;};dojo.html.removeClass=function(node,_4f8,_4f9){try{if(!_4f9){var _4fa=dojo.html.getClass(node).replace(new RegExp("(^|\\s+)"+_4f8+"(\\s+|$)"),"$1$2");}else{var _4fa=dojo.html.getClass(node).replace(_4f8,"");}
dojo.html.setClass(node,_4fa);}
catch(e){dojo.debug("dojo.html.removeClass() failed",e);}
return true;};dojo.html.replaceClass=function(node,_4fc,_4fd){dojo.html.removeClass(node,_4fd);dojo.html.addClass(node,_4fc);};dojo.html.classMatchType={ContainsAll:0,ContainsAny:1,IsOnly:2};dojo.html.getElementsByClass=function(_4fe,_4ff,_500,_501,_502){_502=false;var _503=dojo.doc();_4ff=dojo.byId(_4ff)||_503;var _504=_4fe.split(/\s+/g);var _505=[];if(_501!=1&&_501!=2){_501=0;}
var _506=new RegExp("(\\s|^)(("+_504.join(")|(")+"))(\\s|$)");var _507=_504.join(" ").length;var _508=[];if(!_502&&_503.evaluate){var _509=".//"+(_500||"*")+"[contains(";if(_501!=dojo.html.classMatchType.ContainsAny){_509+="concat(' ',@class,' '), ' "+_504.join(" ') and contains(concat(' ',@class,' '), ' ")+" ')";if(_501==2){_509+=" and string-length(@class)="+_507+"]";}else{_509+="]";}}else{_509+="concat(' ',@class,' '), ' "+_504.join(" ') or contains(concat(' ',@class,' '), ' ")+" ')]";}
var _50a=_503.evaluate(_509,_4ff,null,XPathResult.ANY_TYPE,null);var _50b=_50a.iterateNext();while(_50b){try{_508.push(_50b);_50b=_50a.iterateNext();}
catch(e){break;}}
return _508;}else{if(!_500){_500="*";}
_508=_4ff.getElementsByTagName(_500);var node,i=0;outer:
while(node=_508[i++]){var _50e=dojo.html.getClasses(node);if(_50e.length==0){continue outer;}
var _50f=0;for(var j=0;j<_50e.length;j++){if(_506.test(_50e[j])){if(_501==dojo.html.classMatchType.ContainsAny){_505.push(node);continue outer;}else{_50f++;}}else{if(_501==dojo.html.classMatchType.IsOnly){continue outer;}}}
if(_50f==_504.length){if((_501==dojo.html.classMatchType.IsOnly)&&(_50f==_50e.length)){_505.push(node);}else{if(_501==dojo.html.classMatchType.ContainsAll){_505.push(node);}}}}
return _505;}};dojo.html.getElementsByClassName=dojo.html.getElementsByClass;dojo.html.toCamelCase=function(_511){var arr=_511.split("-"),cc=arr[0];for(var i=1;i<arr.length;i++){cc+=arr[i].charAt(0).toUpperCase()+arr[i].substring(1);}
return cc;};dojo.html.toSelectorCase=function(_515){return _515.replace(/([A-Z])/g,"-$1").toLowerCase();};if(dojo.render.html.ie){dojo.html.getComputedStyle=function(node,_517,_518){node=dojo.byId(node);if(!node||!node.style){return _518;}
return node.currentStyle[dojo.html.toCamelCase(_517)];};dojo.html.getComputedStyles=function(node){return node.currentStyle;};}else{dojo.html.getComputedStyle=function(node,_51b,_51c){node=dojo.byId(node);if(!node||!node.style){return _51c;}
var s=document.defaultView.getComputedStyle(node,null);return (s&&s[dojo.html.toCamelCase(_51b)])||"";};dojo.html.getComputedStyles=function(node){return document.defaultView.getComputedStyle(node,null);};}
dojo.html.getStyleProperty=function(node,_520){node=dojo.byId(node);return (node&&node.style?node.style[dojo.html.toCamelCase(_520)]:undefined);};dojo.html.getStyle=function(node,_522){var _523=dojo.html.getStyleProperty(node,_522);return (_523?_523:dojo.html.getComputedStyle(node,_522));};dojo.html.setStyle=function(node,_525,_526){node=dojo.byId(node);if(node&&node.style){var _527=dojo.html.toCamelCase(_525);node.style[_527]=_526;}};dojo.html.setStyleText=function(_528,text){try{_528.style.cssText=text;}
catch(e){_528.setAttribute("style",text);}};dojo.html.copyStyle=function(_52a,_52b){if(!_52b.style.cssText){_52a.setAttribute("style",_52b.getAttribute("style"));}else{_52a.style.cssText=_52b.style.cssText;}
dojo.html.addClass(_52a,dojo.html.getClass(_52b));};dojo.html.getUnitValue=function(node,_52d,_52e){var s=dojo.html.getComputedStyle(node,_52d);if((!s)||((s=="auto")&&(_52e))){return {value:0,units:"px"};}
var _530=s.match(/(\-?[\d.]+)([a-z%]*)/i);if(!_530){return dojo.html.getUnitValue.bad;}
return {value:Number(_530[1]),units:_530[2].toLowerCase()};};dojo.html.getUnitValue.bad={value:NaN,units:""};if(dojo.render.html.ie){dojo.html.toPixelValue=function(_531,_532){if(!_532){return 0;}
if(_532.slice(-2)=="px"){return parseFloat(_532);}
var _533=0;with(_531){var _534=style.left;var _535=runtimeStyle.left;runtimeStyle.left=currentStyle.left;try{style.left=_532||0;_533=style.pixelLeft;style.left=_534;runtimeStyle.left=_535;}
catch(e){}}
return _533;};}else{dojo.html.toPixelValue=function(_536,_537){return (_537&&(_537.slice(-2)=="px")?parseFloat(_537):0);};}
dojo.html.getPixelValue=function(node,_539,_53a){return dojo.html.toPixelValue(node,dojo.html.getComputedStyle(node,_539));};dojo.html.setPositivePixelValue=function(node,_53c,_53d){if(isNaN(_53d)){return false;}
node.style[_53c]=Math.max(0,_53d)+"px";return true;};dojo.html.styleSheet=null;dojo.html.insertCssRule=function(_53e,_53f,_540){if(!dojo.html.styleSheet){if(document.createStyleSheet){dojo.html.styleSheet=document.createStyleSheet();}else{if(document.styleSheets[0]){dojo.html.styleSheet=document.styleSheets[0];}else{return null;}}}
if(arguments.length<3){if(dojo.html.styleSheet.cssRules){_540=dojo.html.styleSheet.cssRules.length;}else{if(dojo.html.styleSheet.rules){_540=dojo.html.styleSheet.rules.length;}else{return null;}}}
if(dojo.html.styleSheet.insertRule){var rule=_53e+" { "+_53f+" }";return dojo.html.styleSheet.insertRule(rule,_540);}else{if(dojo.html.styleSheet.addRule){return dojo.html.styleSheet.addRule(_53e,_53f,_540);}else{return null;}}};dojo.html.removeCssRule=function(_542){if(!dojo.html.styleSheet){dojo.debug("no stylesheet defined for removing rules");return false;}
if(dojo.render.html.ie){if(!_542){_542=dojo.html.styleSheet.rules.length;dojo.html.styleSheet.removeRule(_542);}}else{if(document.styleSheets[0]){if(!_542){_542=dojo.html.styleSheet.cssRules.length;}
dojo.html.styleSheet.deleteRule(_542);}}
return true;};dojo.html._insertedCssFiles=[];dojo.html.insertCssFile=function(URI,doc,_545,_546){if(!URI){return;}
if(!doc){doc=document;}
var _547=dojo.hostenv.getText(URI,false,_546);if(_547===null){return;}
_547=dojo.html.fixPathsInCssText(_547,URI);if(_545){var idx=-1,node,ent=dojo.html._insertedCssFiles;for(var i=0;i<ent.length;i++){if((ent[i].doc==doc)&&(ent[i].cssText==_547)){idx=i;node=ent[i].nodeRef;break;}}
if(node){var _54c=doc.getElementsByTagName("style");for(var i=0;i<_54c.length;i++){if(_54c[i]==node){return;}}
dojo.html._insertedCssFiles.shift(idx,1);}}
var _54d=dojo.html.insertCssText(_547,doc);dojo.html._insertedCssFiles.push({"doc":doc,"cssText":_547,"nodeRef":_54d});if(_54d&&djConfig.isDebug){_54d.setAttribute("dbgHref",URI);}
return _54d;};dojo.html.insertCssText=function(_54e,doc,URI){if(!_54e){return;}
if(!doc){doc=document;}
if(URI){_54e=dojo.html.fixPathsInCssText(_54e,URI);}
var _551=doc.createElement("style");_551.setAttribute("type","text/css");var head=doc.getElementsByTagName("head")[0];if(!head){dojo.debug("No head tag in document, aborting styles");return;}else{head.appendChild(_551);}
if(_551.styleSheet){var _553=function(){try{_551.styleSheet.cssText=_54e;}
catch(e){dojo.debug(e);}};if(_551.styleSheet.disabled){setTimeout(_553,10);}else{_553();}}else{var _554=doc.createTextNode(_54e);_551.appendChild(_554);}
return _551;};dojo.html.fixPathsInCssText=function(_555,URI){if(!_555||!URI){return;}
var _557,str="",url="",_55a="[\\t\\s\\w\\(\\)\\/\\.\\\\'\"-:#=&?~]+";var _55b=new RegExp("url\\(\\s*("+_55a+")\\s*\\)");var _55c=/(file|https?|ftps?):\/\//;regexTrim=new RegExp("^[\\s]*(['\"]?)("+_55a+")\\1[\\s]*?$");if(dojo.render.html.ie55||dojo.render.html.ie60){var _55d=new RegExp("AlphaImageLoader\\((.*)src=['\"]("+_55a+")['\"]");while(_557=_55d.exec(_555)){url=_557[2].replace(regexTrim,"$2");if(!_55c.exec(url)){url=(new dojo.uri.Uri(URI,url).toString());}
str+=_555.substring(0,_557.index)+"AlphaImageLoader("+_557[1]+"src='"+url+"'";_555=_555.substr(_557.index+_557[0].length);}
_555=str+_555;str="";}
while(_557=_55b.exec(_555)){url=_557[1].replace(regexTrim,"$2");if(!_55c.exec(url)){url=(new dojo.uri.Uri(URI,url).toString());}
str+=_555.substring(0,_557.index)+"url("+url+")";_555=_555.substr(_557.index+_557[0].length);}
return str+_555;};dojo.html.setActiveStyleSheet=function(_55e){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")){a.disabled=true;if(a.getAttribute("title")==_55e){a.disabled=false;}}}};dojo.html.getActiveStyleSheet=function(){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")&&!a.disabled){return a.getAttribute("title");}}
return null;};dojo.html.getPreferredStyleSheet=function(){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("rel").indexOf("alt")==-1&&a.getAttribute("title")){return a.getAttribute("title");}}
return null;};dojo.html.applyBrowserClass=function(node){var drh=dojo.render.html;var _56a={dj_ie:drh.ie,dj_ie55:drh.ie55,dj_ie6:drh.ie60,dj_ie7:drh.ie70,dj_iequirks:drh.ie&&drh.quirks,dj_opera:drh.opera,dj_opera8:drh.opera&&(Math.floor(dojo.render.version)==8),dj_opera9:drh.opera&&(Math.floor(dojo.render.version)==9),dj_khtml:drh.khtml,dj_safari:drh.safari,dj_gecko:drh.mozilla};for(var p in _56a){if(_56a[p]){dojo.html.addClass(node,p);}}};dojo.provide("dojo.widget.DomWidget");dojo.widget._cssFiles={};dojo.widget._cssStrings={};dojo.widget._templateCache={};dojo.widget.defaultStrings={dojoRoot:dojo.hostenv.getBaseScriptUri(),dojoWidgetModuleUri:dojo.uri.moduleUri("dojo.widget"),baseScriptUri:dojo.hostenv.getBaseScriptUri()};dojo.widget.fillFromTemplateCache=function(obj,_56d,_56e,_56f){var _570=_56d||obj.templatePath;var _571=dojo.widget._templateCache;if(!_570&&!obj["widgetType"]){do{var _572="__dummyTemplate__"+dojo.widget._templateCache.dummyCount++;}while(_571[_572]);obj.widgetType=_572;}
var wt=_570?_570.toString():obj.widgetType;var ts=_571[wt];if(!ts){_571[wt]={"string":null,"node":null};if(_56f){ts={};}else{ts=_571[wt];}}
if((!obj.templateString)&&(!_56f)){obj.templateString=_56e||ts["string"];}
if(obj.templateString){obj.templateString=this._sanitizeTemplateString(obj.templateString);}
if((!obj.templateNode)&&(!_56f)){obj.templateNode=ts["node"];}
if((!obj.templateNode)&&(!obj.templateString)&&(_570)){var _575=this._sanitizeTemplateString(dojo.hostenv.getText(_570));obj.templateString=_575;if(!_56f){_571[wt]["string"]=_575;}}
if((!ts["string"])&&(!_56f)){ts.string=obj.templateString;}};dojo.widget._sanitizeTemplateString=function(_576){if(_576){_576=_576.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im,"");var _577=_576.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);if(_577){_576=_577[1];}}else{_576="";}
return _576;};dojo.widget._templateCache.dummyCount=0;dojo.widget.attachProperties=["dojoAttachPoint","id"];dojo.widget.eventAttachProperty="dojoAttachEvent";dojo.widget.onBuildProperty="dojoOnBuild";dojo.widget.waiNames=["waiRole","waiState"];dojo.widget.wai={waiRole:{name:"waiRole","namespace":"http://www.w3.org/TR/xhtml2",alias:"x2",prefix:"wairole:"},waiState:{name:"waiState","namespace":"http://www.w3.org/2005/07/aaa",alias:"aaa",prefix:""},setAttr:function(node,ns,attr,_57b){if(dojo.render.html.ie){node.setAttribute(this[ns].alias+":"+attr,this[ns].prefix+_57b);}else{node.setAttributeNS(this[ns]["namespace"],attr,this[ns].prefix+_57b);}},getAttr:function(node,ns,attr){if(dojo.render.html.ie){return node.getAttribute(this[ns].alias+":"+attr);}else{return node.getAttributeNS(this[ns]["namespace"],attr);}},removeAttr:function(node,ns,attr){var _582=true;if(dojo.render.html.ie){_582=node.removeAttribute(this[ns].alias+":"+attr);}else{node.removeAttributeNS(this[ns]["namespace"],attr);}
return _582;}};dojo.widget.attachTemplateNodes=function(_583,_584,_585){var _586=dojo.dom.ELEMENT_NODE;function trim(str){return str.replace(/^\s+|\s+$/g,"");}
if(!_583){_583=_584.domNode;}
if(_583.nodeType!=_586){return;}
var _588=_583.all||_583.getElementsByTagName("*");var _589=_584;for(var x=-1;x<_588.length;x++){var _58b=(x==-1)?_583:_588[x];var _58c=[];if(!_584.widgetsInTemplate||!_58b.getAttribute("dojoType")){for(var y=0;y<this.attachProperties.length;y++){var _58e=_58b.getAttribute(this.attachProperties[y]);if(_58e){_58c=_58e.split(";");for(var z=0;z<_58c.length;z++){if(dojo.lang.isArray(_584[_58c[z]])){_584[_58c[z]].push(_58b);}else{_584[_58c[z]]=_58b;}}
break;}}
var _590=_58b.getAttribute(this.eventAttachProperty);if(_590){var evts=_590.split(";");for(var y=0;y<evts.length;y++){if((!evts[y])||(!evts[y].length)){continue;}
var _592=null;var tevt=trim(evts[y]);if(evts[y].indexOf(":")>=0){var _594=tevt.split(":");tevt=trim(_594[0]);_592=trim(_594[1]);}
if(!_592){_592=tevt;}
var tf=function(){var ntf=new String(_592);return function(evt){if(_589[ntf]){_589[ntf](dojo.event.browser.fixEvent(evt,this));}};}();dojo.event.browser.addListener(_58b,tevt,tf,false,true);}}
for(var y=0;y<_585.length;y++){var _598=_58b.getAttribute(_585[y]);if((_598)&&(_598.length)){var _592=null;var _599=_585[y].substr(4);_592=trim(_598);var _59a=[_592];if(_592.indexOf(";")>=0){_59a=dojo.lang.map(_592.split(";"),trim);}
for(var z=0;z<_59a.length;z++){if(!_59a[z].length){continue;}
var tf=function(){var ntf=new String(_59a[z]);return function(evt){if(_589[ntf]){_589[ntf](dojo.event.browser.fixEvent(evt,this));}};}();dojo.event.browser.addListener(_58b,_599,tf,false,true);}}}}
var _59d=_58b.getAttribute(this.templateProperty);if(_59d){_584[_59d]=_58b;}
dojo.lang.forEach(dojo.widget.waiNames,function(name){var wai=dojo.widget.wai[name];var val=_58b.getAttribute(wai.name);if(val){if(val.indexOf("-")==-1){dojo.widget.wai.setAttr(_58b,wai.name,"role",val);}else{var _5a1=val.split("-");dojo.widget.wai.setAttr(_58b,wai.name,_5a1[0],_5a1[1]);}}},this);var _5a2=_58b.getAttribute(this.onBuildProperty);if(_5a2){eval("var node = baseNode; var widget = targetObj; "+_5a2);}}};dojo.widget.getDojoEventsFromStr=function(str){var re=/(dojoOn([a-z]+)(\s?))=/gi;var evts=str?str.match(re)||[]:[];var ret=[];var lem={};for(var x=0;x<evts.length;x++){if(evts[x].length<1){continue;}
var cm=evts[x].replace(/\s/,"");cm=(cm.slice(0,cm.length-1));if(!lem[cm]){lem[cm]=true;ret.push(cm);}}
return ret;};dojo.declare("dojo.widget.DomWidget",dojo.widget.Widget,function(){if((arguments.length>0)&&(typeof arguments[0]=="object")){this.create(arguments[0]);}},{templateNode:null,templateString:null,templateCssString:null,preventClobber:false,domNode:null,containerNode:null,widgetsInTemplate:false,addChild:function(_5aa,_5ab,pos,ref,_5ae){if(!this.isContainer){dojo.debug("dojo.widget.DomWidget.addChild() attempted on non-container widget");return null;}else{if(_5ae==undefined){_5ae=this.children.length;}
this.addWidgetAsDirectChild(_5aa,_5ab,pos,ref,_5ae);this.registerChild(_5aa,_5ae);}
return _5aa;},addWidgetAsDirectChild:function(_5af,_5b0,pos,ref,_5b3){if((!this.containerNode)&&(!_5b0)){this.containerNode=this.domNode;}
var cn=(_5b0)?_5b0:this.containerNode;if(!pos){pos="after";}
if(!ref){if(!cn){cn=dojo.body();}
ref=cn.lastChild;}
if(!_5b3){_5b3=0;}
_5af.domNode.setAttribute("dojoinsertionindex",_5b3);if(!ref){cn.appendChild(_5af.domNode);}else{if(pos=="insertAtIndex"){dojo.dom.insertAtIndex(_5af.domNode,ref.parentNode,_5b3);}else{if((pos=="after")&&(ref===cn.lastChild)){cn.appendChild(_5af.domNode);}else{dojo.dom.insertAtPosition(_5af.domNode,cn,pos);}}}},registerChild:function(_5b5,_5b6){_5b5.dojoInsertionIndex=_5b6;var idx=-1;for(var i=0;i<this.children.length;i++){if(this.children[i].dojoInsertionIndex<=_5b6){idx=i;}}
this.children.splice(idx+1,0,_5b5);_5b5.parent=this;_5b5.addedTo(this,idx+1);delete dojo.widget.manager.topWidgets[_5b5.widgetId];},removeChild:function(_5b9){dojo.dom.removeNode(_5b9.domNode);return dojo.widget.DomWidget.superclass.removeChild.call(this,_5b9);},getFragNodeRef:function(frag){if(!frag){return null;}
if(!frag[this.getNamespacedType()]){dojo.raise("Error: no frag for widget type "+this.getNamespacedType()+", id "+this.widgetId+" (maybe a widget has set it's type incorrectly)");}
return frag[this.getNamespacedType()]["nodeRef"];},postInitialize:function(args,frag,_5bd){var _5be=this.getFragNodeRef(frag);if(_5bd&&(_5bd.snarfChildDomOutput||!_5be)){_5bd.addWidgetAsDirectChild(this,"","insertAtIndex","",args["dojoinsertionindex"],_5be);}else{if(_5be){if(this.domNode&&(this.domNode!==_5be)){this._sourceNodeRef=dojo.dom.replaceNode(_5be,this.domNode);}}}
if(_5bd){_5bd.registerChild(this,args.dojoinsertionindex);}else{dojo.widget.manager.topWidgets[this.widgetId]=this;}
if(this.widgetsInTemplate){var _5bf=new dojo.xml.Parse();var _5c0;var _5c1=this.domNode.getElementsByTagName("*");for(var i=0;i<_5c1.length;i++){if(_5c1[i].getAttribute("dojoAttachPoint")=="subContainerWidget"){_5c0=_5c1[i];}
if(_5c1[i].getAttribute("dojoType")){_5c1[i].setAttribute("isSubWidget",true);}}
if(this.isContainer&&!this.containerNode){if(_5c0){var src=this.getFragNodeRef(frag);if(src){dojo.dom.moveChildren(src,_5c0);frag["dojoDontFollow"]=true;}}else{dojo.debug("No subContainerWidget node can be found in template file for widget "+this);}}
var _5c4=_5bf.parseElement(this.domNode,null,true);dojo.widget.getParser().createSubComponents(_5c4,this);var _5c5=[];var _5c6=[this];var w;while((w=_5c6.pop())){for(var i=0;i<w.children.length;i++){var _5c8=w.children[i];if(_5c8._processedSubWidgets||!_5c8.extraArgs["issubwidget"]){continue;}
_5c5.push(_5c8);if(_5c8.isContainer){_5c6.push(_5c8);}}}
for(var i=0;i<_5c5.length;i++){var _5c9=_5c5[i];if(_5c9._processedSubWidgets){dojo.debug("This should not happen: widget._processedSubWidgets is already true!");return;}
_5c9._processedSubWidgets=true;if(_5c9.extraArgs["dojoattachevent"]){var evts=_5c9.extraArgs["dojoattachevent"].split(";");for(var j=0;j<evts.length;j++){var _5cc=null;var tevt=dojo.string.trim(evts[j]);if(tevt.indexOf(":")>=0){var _5ce=tevt.split(":");tevt=dojo.string.trim(_5ce[0]);_5cc=dojo.string.trim(_5ce[1]);}
if(!_5cc){_5cc=tevt;}
if(dojo.lang.isFunction(_5c9[tevt])){dojo.event.kwConnect({srcObj:_5c9,srcFunc:tevt,targetObj:this,targetFunc:_5cc});}else{alert(tevt+" is not a function in widget "+_5c9);}}}
if(_5c9.extraArgs["dojoattachpoint"]){this[_5c9.extraArgs["dojoattachpoint"]]=_5c9;}}}
if(this.isContainer&&!frag["dojoDontFollow"]){dojo.widget.getParser().createSubComponents(frag,this);}},buildRendering:function(args,frag){var ts=dojo.widget._templateCache[this.widgetType];if(args["templatecsspath"]){args["templateCssPath"]=args["templatecsspath"];}
var _5d2=args["templateCssPath"]||this.templateCssPath;if(_5d2&&!dojo.widget._cssFiles[_5d2.toString()]){if((!this.templateCssString)&&(_5d2)){this.templateCssString=dojo.hostenv.getText(_5d2);this.templateCssPath=null;}
dojo.widget._cssFiles[_5d2.toString()]=true;}
if((this["templateCssString"])&&(!dojo.widget._cssStrings[this.templateCssString])){dojo.html.insertCssText(this.templateCssString,null,_5d2);dojo.widget._cssStrings[this.templateCssString]=true;}
if((!this.preventClobber)&&((this.templatePath)||(this.templateNode)||((this["templateString"])&&(this.templateString.length))||((typeof ts!="undefined")&&((ts["string"])||(ts["node"]))))){this.buildFromTemplate(args,frag);}else{this.domNode=this.getFragNodeRef(frag);}
this.fillInTemplate(args,frag);},buildFromTemplate:function(args,frag){var _5d5=false;if(args["templatepath"]){args["templatePath"]=args["templatepath"];}
dojo.widget.fillFromTemplateCache(this,args["templatePath"],null,_5d5);var ts=dojo.widget._templateCache[this.templatePath?this.templatePath.toString():this.widgetType];if((ts)&&(!_5d5)){if(!this.templateString.length){this.templateString=ts["string"];}
if(!this.templateNode){this.templateNode=ts["node"];}}
var _5d7=false;var node=null;var tstr=this.templateString;if((!this.templateNode)&&(this.templateString)){_5d7=this.templateString.match(/\$\{([^\}]+)\}/g);if(_5d7){var hash=this.strings||{};for(var key in dojo.widget.defaultStrings){if(dojo.lang.isUndefined(hash[key])){hash[key]=dojo.widget.defaultStrings[key];}}
for(var i=0;i<_5d7.length;i++){var key=_5d7[i];key=key.substring(2,key.length-1);var kval=(key.substring(0,5)=="this.")?dojo.lang.getObjPathValue(key.substring(5),this):hash[key];var _5de;if((kval)||(dojo.lang.isString(kval))){_5de=new String((dojo.lang.isFunction(kval))?kval.call(this,key,this.templateString):kval);while(_5de.indexOf("\"")>-1){_5de=_5de.replace("\"","&quot;");}
tstr=tstr.replace(_5d7[i],_5de);}}}else{this.templateNode=this.createNodesFromText(this.templateString,true)[0];if(!_5d5){ts.node=this.templateNode;}}}
if((!this.templateNode)&&(!_5d7)){dojo.debug("DomWidget.buildFromTemplate: could not create template");return false;}else{if(!_5d7){node=this.templateNode.cloneNode(true);if(!node){return false;}}else{node=this.createNodesFromText(tstr,true)[0];}}
this.domNode=node;this.attachTemplateNodes();if(this.isContainer&&this.containerNode){var src=this.getFragNodeRef(frag);if(src){dojo.dom.moveChildren(src,this.containerNode);}}},attachTemplateNodes:function(_5e0,_5e1){if(!_5e0){_5e0=this.domNode;}
if(!_5e1){_5e1=this;}
return dojo.widget.attachTemplateNodes(_5e0,_5e1,dojo.widget.getDojoEventsFromStr(this.templateString));},fillInTemplate:function(){},destroyRendering:function(){try{dojo.dom.destroyNode(this.domNode);delete this.domNode;}
catch(e){}
if(this._sourceNodeRef){try{dojo.dom.destroyNode(this._sourceNodeRef);}
catch(e){}}},createNodesFromText:function(){dojo.unimplemented("dojo.widget.DomWidget.createNodesFromText");}});dojo.provide("dojo.html.display");dojo.html._toggle=function(node,_5e3,_5e4){node=dojo.byId(node);_5e4(node,!_5e3(node));return _5e3(node);};dojo.html.show=function(node){node=dojo.byId(node);if(dojo.html.getStyleProperty(node,"display")=="none"){dojo.html.setStyle(node,"display",(node.dojoDisplayCache||""));node.dojoDisplayCache=undefined;}};dojo.html.hide=function(node){node=dojo.byId(node);if(typeof node["dojoDisplayCache"]=="undefined"){var d=dojo.html.getStyleProperty(node,"display");if(d!="none"){node.dojoDisplayCache=d;}}
dojo.html.setStyle(node,"display","none");};dojo.html.setShowing=function(node,_5e9){dojo.html[(_5e9?"show":"hide")](node);};dojo.html.isShowing=function(node){return (dojo.html.getStyleProperty(node,"display")!="none");};dojo.html.toggleShowing=function(node){return dojo.html._toggle(node,dojo.html.isShowing,dojo.html.setShowing);};dojo.html.displayMap={tr:"",td:"",th:"",img:"inline",span:"inline",input:"inline",button:"inline"};dojo.html.suggestDisplayByTagName=function(node){node=dojo.byId(node);if(node&&node.tagName){var tag=node.tagName.toLowerCase();return (tag in dojo.html.displayMap?dojo.html.displayMap[tag]:"block");}};dojo.html.setDisplay=function(node,_5ef){dojo.html.setStyle(node,"display",((_5ef instanceof String||typeof _5ef=="string")?_5ef:(_5ef?dojo.html.suggestDisplayByTagName(node):"none")));};dojo.html.isDisplayed=function(node){return (dojo.html.getComputedStyle(node,"display")!="none");};dojo.html.toggleDisplay=function(node){return dojo.html._toggle(node,dojo.html.isDisplayed,dojo.html.setDisplay);};dojo.html.setVisibility=function(node,_5f3){dojo.html.setStyle(node,"visibility",((_5f3 instanceof String||typeof _5f3=="string")?_5f3:(_5f3?"visible":"hidden")));};dojo.html.isVisible=function(node){return (dojo.html.getComputedStyle(node,"visibility")!="hidden");};dojo.html.toggleVisibility=function(node){return dojo.html._toggle(node,dojo.html.isVisible,dojo.html.setVisibility);};dojo.html.setOpacity=function(node,_5f7,_5f8){node=dojo.byId(node);var h=dojo.render.html;if(!_5f8){if(_5f7>=1){if(h.ie){dojo.html.clearOpacity(node);return;}else{_5f7=0.999999;}}else{if(_5f7<0){_5f7=0;}}}
if(h.ie){if(node.nodeName.toLowerCase()=="tr"){var tds=node.getElementsByTagName("td");for(var x=0;x<tds.length;x++){tds[x].style.filter="Alpha(Opacity="+_5f7*100+")";}}
node.style.filter="Alpha(Opacity="+_5f7*100+")";}else{if(h.moz){node.style.opacity=_5f7;node.style.MozOpacity=_5f7;}else{if(h.safari){node.style.opacity=_5f7;node.style.KhtmlOpacity=_5f7;}else{node.style.opacity=_5f7;}}}};dojo.html.clearOpacity=function(node){node=dojo.byId(node);var ns=node.style;var h=dojo.render.html;if(h.ie){try{if(node.filters&&node.filters.alpha){ns.filter="";}}
catch(e){}}else{if(h.moz){ns.opacity=1;ns.MozOpacity=1;}else{if(h.safari){ns.opacity=1;ns.KhtmlOpacity=1;}else{ns.opacity=1;}}}};dojo.html.getOpacity=function(node){node=dojo.byId(node);var h=dojo.render.html;if(h.ie){var opac=(node.filters&&node.filters.alpha&&typeof node.filters.alpha.opacity=="number"?node.filters.alpha.opacity:100)/100;}else{var opac=node.style.opacity||node.style.MozOpacity||node.style.KhtmlOpacity||1;}
return opac>=0.999999?1:Number(opac);};dojo.provide("dojo.html.layout");dojo.html.sumAncestorProperties=function(node,prop){node=dojo.byId(node);if(!node){return 0;}
var _604=0;while(node){if(dojo.html.getComputedStyle(node,"position")=="fixed"){return 0;}
var val=node[prop];if(val){_604+=val-0;if(node==dojo.body()){break;}}
node=node.parentNode;}
return _604;};dojo.html.setStyleAttributes=function(node,_607){node=dojo.byId(node);var _608=_607.replace(/(;)?\s*$/,"").split(";");for(var i=0;i<_608.length;i++){var _60a=_608[i].split(":");var name=_60a[0].replace(/\s*$/,"").replace(/^\s*/,"").toLowerCase();var _60c=_60a[1].replace(/\s*$/,"").replace(/^\s*/,"");switch(name){case "opacity":
dojo.html.setOpacity(node,_60c);break;case "content-height":
dojo.html.setContentBox(node,{height:_60c});break;case "content-width":
dojo.html.setContentBox(node,{width:_60c});break;case "outer-height":
dojo.html.setMarginBox(node,{height:_60c});break;case "outer-width":
dojo.html.setMarginBox(node,{width:_60c});break;default:
node.style[dojo.html.toCamelCase(name)]=_60c;}}};dojo.html.boxSizing={MARGIN_BOX:"margin-box",BORDER_BOX:"border-box",PADDING_BOX:"padding-box",CONTENT_BOX:"content-box"};dojo.html.getAbsolutePosition=dojo.html.abs=function(node,_60e,_60f){node=dojo.byId(node,node.ownerDocument);var ret={x:0,y:0};var bs=dojo.html.boxSizing;if(!_60f){_60f=bs.CONTENT_BOX;}
var _612=2;var _613;switch(_60f){case bs.MARGIN_BOX:
_613=3;break;case bs.BORDER_BOX:
_613=2;break;case bs.PADDING_BOX:
default:
_613=1;break;case bs.CONTENT_BOX:
_613=0;break;}
var h=dojo.render.html;var db=document["body"]||document["documentElement"];if(h.ie){with(node.getBoundingClientRect()){ret.x=left-2;ret.y=top-2;}}else{if(document.getBoxObjectFor){_612=1;try{var bo=document.getBoxObjectFor(node);ret.x=bo.x-dojo.html.sumAncestorProperties(node,"scrollLeft");ret.y=bo.y-dojo.html.sumAncestorProperties(node,"scrollTop");}
catch(e){}}else{if(node["offsetParent"]){var _617;if((h.safari)&&(node.style.getPropertyValue("position")=="absolute")&&(node.parentNode==db)){_617=db;}else{_617=db.parentNode;}
if(node.parentNode!=db){var nd=node;if(dojo.render.html.opera){nd=db;}
ret.x-=dojo.html.sumAncestorProperties(nd,"scrollLeft");ret.y-=dojo.html.sumAncestorProperties(nd,"scrollTop");}
var _619=node;do{var n=_619["offsetLeft"];if(!h.opera||n>0){ret.x+=isNaN(n)?0:n;}
var m=_619["offsetTop"];ret.y+=isNaN(m)?0:m;_619=_619.offsetParent;}while((_619!=_617)&&(_619!=null));}else{if(node["x"]&&node["y"]){ret.x+=isNaN(node.x)?0:node.x;ret.y+=isNaN(node.y)?0:node.y;}}}}
if(_60e){var _61c=dojo.html.getScroll();ret.y+=_61c.top;ret.x+=_61c.left;}
var _61d=[dojo.html.getPaddingExtent,dojo.html.getBorderExtent,dojo.html.getMarginExtent];if(_612>_613){for(var i=_613;i<_612;++i){ret.y+=_61d[i](node,"top");ret.x+=_61d[i](node,"left");}}else{if(_612<_613){for(var i=_613;i>_612;--i){ret.y-=_61d[i-1](node,"top");ret.x-=_61d[i-1](node,"left");}}}
ret.top=ret.y;ret.left=ret.x;return ret;};dojo.html.isPositionAbsolute=function(node){return (dojo.html.getComputedStyle(node,"position")=="absolute");};dojo.html._sumPixelValues=function(node,_621,_622){var _623=0;for(var x=0;x<_621.length;x++){_623+=dojo.html.getPixelValue(node,_621[x],_622);}
return _623;};dojo.html.getMargin=function(node){return {width:dojo.html._sumPixelValues(node,["margin-left","margin-right"],(dojo.html.getComputedStyle(node,"position")=="absolute")),height:dojo.html._sumPixelValues(node,["margin-top","margin-bottom"],(dojo.html.getComputedStyle(node,"position")=="absolute"))};};dojo.html.getBorder=function(node){return {width:dojo.html.getBorderExtent(node,"left")+dojo.html.getBorderExtent(node,"right"),height:dojo.html.getBorderExtent(node,"top")+dojo.html.getBorderExtent(node,"bottom")};};dojo.html.getBorderExtent=function(node,side){return (dojo.html.getStyle(node,"border-"+side+"-style")=="none"?0:dojo.html.getPixelValue(node,"border-"+side+"-width"));};dojo.html.getMarginExtent=function(node,side){return dojo.html._sumPixelValues(node,["margin-"+side],dojo.html.isPositionAbsolute(node));};dojo.html.getPaddingExtent=function(node,side){return dojo.html._sumPixelValues(node,["padding-"+side],true);};dojo.html.getPadding=function(node){return {width:dojo.html._sumPixelValues(node,["padding-left","padding-right"],true),height:dojo.html._sumPixelValues(node,["padding-top","padding-bottom"],true)};};dojo.html.getPadBorder=function(node){var pad=dojo.html.getPadding(node);var _630=dojo.html.getBorder(node);return {width:pad.width+_630.width,height:pad.height+_630.height};};dojo.html.getBoxSizing=function(node){var h=dojo.render.html;var bs=dojo.html.boxSizing;if(((h.ie)||(h.opera))&&node.nodeName.toLowerCase()!="img"){var cm=document["compatMode"];if((cm=="BackCompat")||(cm=="QuirksMode")){return bs.BORDER_BOX;}else{return bs.CONTENT_BOX;}}else{if(arguments.length==0){node=document.documentElement;}
var _635;if(!h.ie){_635=dojo.html.getStyle(node,"-moz-box-sizing");if(!_635){_635=dojo.html.getStyle(node,"box-sizing");}}
return (_635?_635:bs.CONTENT_BOX);}};dojo.html.isBorderBox=function(node){return (dojo.html.getBoxSizing(node)==dojo.html.boxSizing.BORDER_BOX);};dojo.html.getBorderBox=function(node){node=dojo.byId(node);return {width:node.offsetWidth,height:node.offsetHeight};};dojo.html.getPaddingBox=function(node){var box=dojo.html.getBorderBox(node);var _63a=dojo.html.getBorder(node);return {width:box.width-_63a.width,height:box.height-_63a.height};};dojo.html.getContentBox=function(node){node=dojo.byId(node);var _63c=dojo.html.getPadBorder(node);return {width:node.offsetWidth-_63c.width,height:node.offsetHeight-_63c.height};};dojo.html.setContentBox=function(node,args){node=dojo.byId(node);var _63f=0;var _640=0;var isbb=dojo.html.isBorderBox(node);var _642=(isbb?dojo.html.getPadBorder(node):{width:0,height:0});var ret={};if(typeof args.width!="undefined"){_63f=args.width+_642.width;ret.width=dojo.html.setPositivePixelValue(node,"width",_63f);}
if(typeof args.height!="undefined"){_640=args.height+_642.height;ret.height=dojo.html.setPositivePixelValue(node,"height",_640);}
return ret;};dojo.html.getMarginBox=function(node){var _645=dojo.html.getBorderBox(node);var _646=dojo.html.getMargin(node);return {width:_645.width+_646.width,height:_645.height+_646.height};};dojo.html.setMarginBox=function(node,args){node=dojo.byId(node);var _649=0;var _64a=0;var isbb=dojo.html.isBorderBox(node);var _64c=(!isbb?dojo.html.getPadBorder(node):{width:0,height:0});var _64d=dojo.html.getMargin(node);var ret={};if(typeof args.width!="undefined"){_649=args.width-_64c.width;_649-=_64d.width;ret.width=dojo.html.setPositivePixelValue(node,"width",_649);}
if(typeof args.height!="undefined"){_64a=args.height-_64c.height;_64a-=_64d.height;ret.height=dojo.html.setPositivePixelValue(node,"height",_64a);}
return ret;};dojo.html.getElementBox=function(node,type){var bs=dojo.html.boxSizing;switch(type){case bs.MARGIN_BOX:
return dojo.html.getMarginBox(node);case bs.BORDER_BOX:
return dojo.html.getBorderBox(node);case bs.PADDING_BOX:
return dojo.html.getPaddingBox(node);case bs.CONTENT_BOX:
default:
return dojo.html.getContentBox(node);}};dojo.html.toCoordinateObject=dojo.html.toCoordinateArray=function(_652,_653,_654){if(_652 instanceof Array||typeof _652=="array"){dojo.deprecated("dojo.html.toCoordinateArray","use dojo.html.toCoordinateObject({left: , top: , width: , height: }) instead","0.5");while(_652.length<4){_652.push(0);}
while(_652.length>4){_652.pop();}
var ret={left:_652[0],top:_652[1],width:_652[2],height:_652[3]};}else{if(!_652.nodeType&&!(_652 instanceof String||typeof _652=="string")&&("width" in _652||"height" in _652||"left" in _652||"x" in _652||"top" in _652||"y" in _652)){var ret={left:_652.left||_652.x||0,top:_652.top||_652.y||0,width:_652.width||0,height:_652.height||0};}else{var node=dojo.byId(_652);var pos=dojo.html.abs(node,_653,_654);var _658=dojo.html.getMarginBox(node);var ret={left:pos.left,top:pos.top,width:_658.width,height:_658.height};}}
ret.x=ret.left;ret.y=ret.top;return ret;};dojo.html.setMarginBoxWidth=dojo.html.setOuterWidth=function(node,_65a){return dojo.html._callDeprecated("setMarginBoxWidth","setMarginBox",arguments,"width");};dojo.html.setMarginBoxHeight=dojo.html.setOuterHeight=function(){return dojo.html._callDeprecated("setMarginBoxHeight","setMarginBox",arguments,"height");};dojo.html.getMarginBoxWidth=dojo.html.getOuterWidth=function(){return dojo.html._callDeprecated("getMarginBoxWidth","getMarginBox",arguments,null,"width");};dojo.html.getMarginBoxHeight=dojo.html.getOuterHeight=function(){return dojo.html._callDeprecated("getMarginBoxHeight","getMarginBox",arguments,null,"height");};dojo.html.getTotalOffset=function(node,type,_65d){return dojo.html._callDeprecated("getTotalOffset","getAbsolutePosition",arguments,null,type);};dojo.html.getAbsoluteX=function(node,_65f){return dojo.html._callDeprecated("getAbsoluteX","getAbsolutePosition",arguments,null,"x");};dojo.html.getAbsoluteY=function(node,_661){return dojo.html._callDeprecated("getAbsoluteY","getAbsolutePosition",arguments,null,"y");};dojo.html.totalOffsetLeft=function(node,_663){return dojo.html._callDeprecated("totalOffsetLeft","getAbsolutePosition",arguments,null,"left");};dojo.html.totalOffsetTop=function(node,_665){return dojo.html._callDeprecated("totalOffsetTop","getAbsolutePosition",arguments,null,"top");};dojo.html.getMarginWidth=function(node){return dojo.html._callDeprecated("getMarginWidth","getMargin",arguments,null,"width");};dojo.html.getMarginHeight=function(node){return dojo.html._callDeprecated("getMarginHeight","getMargin",arguments,null,"height");};dojo.html.getBorderWidth=function(node){return dojo.html._callDeprecated("getBorderWidth","getBorder",arguments,null,"width");};dojo.html.getBorderHeight=function(node){return dojo.html._callDeprecated("getBorderHeight","getBorder",arguments,null,"height");};dojo.html.getPaddingWidth=function(node){return dojo.html._callDeprecated("getPaddingWidth","getPadding",arguments,null,"width");};dojo.html.getPaddingHeight=function(node){return dojo.html._callDeprecated("getPaddingHeight","getPadding",arguments,null,"height");};dojo.html.getPadBorderWidth=function(node){return dojo.html._callDeprecated("getPadBorderWidth","getPadBorder",arguments,null,"width");};dojo.html.getPadBorderHeight=function(node){return dojo.html._callDeprecated("getPadBorderHeight","getPadBorder",arguments,null,"height");};dojo.html.getBorderBoxWidth=dojo.html.getInnerWidth=function(){return dojo.html._callDeprecated("getBorderBoxWidth","getBorderBox",arguments,null,"width");};dojo.html.getBorderBoxHeight=dojo.html.getInnerHeight=function(){return dojo.html._callDeprecated("getBorderBoxHeight","getBorderBox",arguments,null,"height");};dojo.html.getContentBoxWidth=dojo.html.getContentWidth=function(){return dojo.html._callDeprecated("getContentBoxWidth","getContentBox",arguments,null,"width");};dojo.html.getContentBoxHeight=dojo.html.getContentHeight=function(){return dojo.html._callDeprecated("getContentBoxHeight","getContentBox",arguments,null,"height");};dojo.html.setContentBoxWidth=dojo.html.setContentWidth=function(node,_66f){return dojo.html._callDeprecated("setContentBoxWidth","setContentBox",arguments,"width");};dojo.html.setContentBoxHeight=dojo.html.setContentHeight=function(node,_671){return dojo.html._callDeprecated("setContentBoxHeight","setContentBox",arguments,"height");};dojo.provide("dojo.html.util");dojo.html.getElementWindow=function(_672){return dojo.html.getDocumentWindow(_672.ownerDocument);};dojo.html.getDocumentWindow=function(doc){if(dojo.render.html.safari&&!doc._parentWindow){var fix=function(win){win.document._parentWindow=win;for(var i=0;i<win.frames.length;i++){fix(win.frames[i]);}};fix(window.top);}
if(dojo.render.html.ie&&window!==document.parentWindow&&!doc._parentWindow){doc.parentWindow.execScript("document._parentWindow = window;","Javascript");var win=doc._parentWindow;doc._parentWindow=null;return win;}
return doc._parentWindow||doc.parentWindow||doc.defaultView;};dojo.html.gravity=function(node,e){node=dojo.byId(node);var _67a=dojo.html.getCursorPosition(e);with(dojo.html){var _67b=getAbsolutePosition(node,true);var bb=getBorderBox(node);var _67d=_67b.x+(bb.width/2);var _67e=_67b.y+(bb.height/2);}
with(dojo.html.gravity){return ((_67a.x<_67d?WEST:EAST)|(_67a.y<_67e?NORTH:SOUTH));}};dojo.html.gravity.NORTH=1;dojo.html.gravity.SOUTH=1<<1;dojo.html.gravity.EAST=1<<2;dojo.html.gravity.WEST=1<<3;dojo.html.overElement=function(_67f,e){_67f=dojo.byId(_67f);var _681=dojo.html.getCursorPosition(e);var bb=dojo.html.getBorderBox(_67f);var _683=dojo.html.getAbsolutePosition(_67f,true,dojo.html.boxSizing.BORDER_BOX);var top=_683.y;var _685=top+bb.height;var left=_683.x;var _687=left+bb.width;return (_681.x>=left&&_681.x<=_687&&_681.y>=top&&_681.y<=_685);};dojo.html.renderedTextContent=function(node){node=dojo.byId(node);var _689="";if(node==null){return _689;}
for(var i=0;i<node.childNodes.length;i++){switch(node.childNodes[i].nodeType){case 1:
case 5:
var _68b="unknown";try{_68b=dojo.html.getStyle(node.childNodes[i],"display");}
catch(E){}
switch(_68b){case "block":
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
_689+="\n";_689+=dojo.html.renderedTextContent(node.childNodes[i]);_689+="\n";break;case "none":
break;default:
if(node.childNodes[i].tagName&&node.childNodes[i].tagName.toLowerCase()=="br"){_689+="\n";}else{_689+=dojo.html.renderedTextContent(node.childNodes[i]);}
break;}
break;case 3:
case 2:
case 4:
var text=node.childNodes[i].nodeValue;var _68d="unknown";try{_68d=dojo.html.getStyle(node,"text-transform");}
catch(E){}
switch(_68d){case "capitalize":
var _68e=text.split(" ");for(var i=0;i<_68e.length;i++){_68e[i]=_68e[i].charAt(0).toUpperCase()+_68e[i].substring(1);}
text=_68e.join(" ");break;case "uppercase":
text=text.toUpperCase();break;case "lowercase":
text=text.toLowerCase();break;default:
break;}
switch(_68d){case "nowrap":
break;case "pre-wrap":
break;case "pre-line":
break;case "pre":
break;default:
text=text.replace(/\s+/," ");if(/\s$/.test(_689)){text.replace(/^\s/,"");}
break;}
_689+=text;break;default:
break;}}
return _689;};dojo.html.createNodesFromText=function(txt,trim){if(trim){txt=txt.replace(/^\s+|\s+$/g,"");}
var tn=dojo.doc().createElement("div");tn.style.visibility="hidden";dojo.body().appendChild(tn);var _692="none";if((/^<t[dh][\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table><tbody><tr>"+txt+"</tr></tbody></table>";_692="cell";}else{if((/^<tr[\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table><tbody>"+txt+"</tbody></table>";_692="row";}else{if((/^<(thead|tbody|tfoot)[\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table>"+txt+"</table>";_692="section";}}}
tn.innerHTML=txt;if(tn["normalize"]){tn.normalize();}
var _693=null;switch(_692){case "cell":
_693=tn.getElementsByTagName("tr")[0];break;case "row":
_693=tn.getElementsByTagName("tbody")[0];break;case "section":
_693=tn.getElementsByTagName("table")[0];break;default:
_693=tn;break;}
var _694=[];for(var x=0;x<_693.childNodes.length;x++){_694.push(_693.childNodes[x].cloneNode(true));}
tn.style.display="none";dojo.html.destroyNode(tn);return _694;};dojo.html.placeOnScreen=function(node,_697,_698,_699,_69a,_69b,_69c){if(_697 instanceof Array||typeof _697=="array"){_69c=_69b;_69b=_69a;_69a=_699;_699=_698;_698=_697[1];_697=_697[0];}
if(_69b instanceof String||typeof _69b=="string"){_69b=_69b.split(",");}
if(!isNaN(_699)){_699=[Number(_699),Number(_699)];}else{if(!(_699 instanceof Array||typeof _699=="array")){_699=[0,0];}}
var _69d=dojo.html.getScroll().offset;var view=dojo.html.getViewport();node=dojo.byId(node);var _69f=node.style.display;node.style.display="";var bb=dojo.html.getBorderBox(node);var w=bb.width;var h=bb.height;node.style.display=_69f;if(!(_69b instanceof Array||typeof _69b=="array")){_69b=["TL"];}
var _6a3,_6a4,_6a5=Infinity,_6a6;for(var _6a7=0;_6a7<_69b.length;++_6a7){var _6a8=_69b[_6a7];var _6a9=true;var tryX=_697-(_6a8.charAt(1)=="L"?0:w)+_699[0]*(_6a8.charAt(1)=="L"?1:-1);var tryY=_698-(_6a8.charAt(0)=="T"?0:h)+_699[1]*(_6a8.charAt(0)=="T"?1:-1);if(_69a){tryX-=_69d.x;tryY-=_69d.y;}
if(tryX<0){tryX=0;_6a9=false;}
if(tryY<0){tryY=0;_6a9=false;}
var x=tryX+w;if(x>view.width){x=view.width-w;_6a9=false;}else{x=tryX;}
x=Math.max(_699[0],x)+_69d.x;var y=tryY+h;if(y>view.height){y=view.height-h;_6a9=false;}else{y=tryY;}
y=Math.max(_699[1],y)+_69d.y;if(_6a9){_6a3=x;_6a4=y;_6a5=0;_6a6=_6a8;break;}else{var dist=Math.pow(x-tryX-_69d.x,2)+Math.pow(y-tryY-_69d.y,2);if(_6a5>dist){_6a5=dist;_6a3=x;_6a4=y;_6a6=_6a8;}}}
if(!_69c){node.style.left=_6a3+"px";node.style.top=_6a4+"px";}
return {left:_6a3,top:_6a4,x:_6a3,y:_6a4,dist:_6a5,corner:_6a6};};dojo.html.placeOnScreenPoint=function(node,_6b0,_6b1,_6b2,_6b3){dojo.deprecated("dojo.html.placeOnScreenPoint","use dojo.html.placeOnScreen() instead","0.5");return dojo.html.placeOnScreen(node,_6b0,_6b1,_6b2,_6b3,["TL","TR","BL","BR"]);};dojo.html.placeOnScreenAroundElement=function(node,_6b5,_6b6,_6b7,_6b8,_6b9){var best,_6bb=Infinity;_6b5=dojo.byId(_6b5);var _6bc=_6b5.style.display;_6b5.style.display="";var mb=dojo.html.getElementBox(_6b5,_6b7);var _6be=mb.width;var _6bf=mb.height;var _6c0=dojo.html.getAbsolutePosition(_6b5,true,_6b7);_6b5.style.display=_6bc;for(var _6c1 in _6b8){var pos,_6c3,_6c4;var _6c5=_6b8[_6c1];_6c3=_6c0.x+(_6c1.charAt(1)=="L"?0:_6be);_6c4=_6c0.y+(_6c1.charAt(0)=="T"?0:_6bf);pos=dojo.html.placeOnScreen(node,_6c3,_6c4,_6b6,true,_6c5,true);if(pos.dist==0){best=pos;break;}else{if(_6bb>pos.dist){_6bb=pos.dist;best=pos;}}}
if(!_6b9){node.style.left=best.left+"px";node.style.top=best.top+"px";}
return best;};dojo.html.scrollIntoView=function(node){if(!node){return;}
if(dojo.render.html.ie){if(dojo.html.getBorderBox(node.parentNode).height<=node.parentNode.scrollHeight){node.scrollIntoView(false);}}else{if(dojo.render.html.mozilla){node.scrollIntoView(false);}else{var _6c7=node.parentNode;var _6c8=_6c7.scrollTop+dojo.html.getBorderBox(_6c7).height;var _6c9=node.offsetTop+dojo.html.getMarginBox(node).height;if(_6c8<_6c9){_6c7.scrollTop+=(_6c9-_6c8);}else{if(_6c7.scrollTop>node.offsetTop){_6c7.scrollTop-=(_6c7.scrollTop-node.offsetTop);}}}}};dojo.provide("dojo.gfx.color");dojo.gfx.color.Color=function(r,g,b,a){if(dojo.lang.isArray(r)){this.r=r[0];this.g=r[1];this.b=r[2];this.a=r[3]||1;}else{if(dojo.lang.isString(r)){var rgb=dojo.gfx.color.extractRGB(r);this.r=rgb[0];this.g=rgb[1];this.b=rgb[2];this.a=g||1;}else{if(r instanceof dojo.gfx.color.Color){this.r=r.r;this.b=r.b;this.g=r.g;this.a=r.a;}else{this.r=r;this.g=g;this.b=b;this.a=a;}}}};dojo.gfx.color.Color.fromArray=function(arr){return new dojo.gfx.color.Color(arr[0],arr[1],arr[2],arr[3]);};dojo.extend(dojo.gfx.color.Color,{toRgb:function(_6d0){if(_6d0){return this.toRgba();}else{return [this.r,this.g,this.b];}},toRgba:function(){return [this.r,this.g,this.b,this.a];},toHex:function(){return dojo.gfx.color.rgb2hex(this.toRgb());},toCss:function(){return "rgb("+this.toRgb().join()+")";},toString:function(){return this.toHex();},blend:function(_6d1,_6d2){var rgb=null;if(dojo.lang.isArray(_6d1)){rgb=_6d1;}else{if(_6d1 instanceof dojo.gfx.color.Color){rgb=_6d1.toRgb();}else{rgb=new dojo.gfx.color.Color(_6d1).toRgb();}}
return dojo.gfx.color.blend(this.toRgb(),rgb,_6d2);}});dojo.gfx.color.named={white:[255,255,255],black:[0,0,0],red:[255,0,0],green:[0,255,0],lime:[0,255,0],blue:[0,0,255],navy:[0,0,128],gray:[128,128,128],silver:[192,192,192]};dojo.gfx.color.blend=function(a,b,_6d6){if(typeof a=="string"){return dojo.gfx.color.blendHex(a,b,_6d6);}
if(!_6d6){_6d6=0;}
_6d6=Math.min(Math.max(-1,_6d6),1);_6d6=((_6d6+1)/2);var c=[];for(var x=0;x<3;x++){c[x]=parseInt(b[x]+((a[x]-b[x])*_6d6));}
return c;};dojo.gfx.color.blendHex=function(a,b,_6db){return dojo.gfx.color.rgb2hex(dojo.gfx.color.blend(dojo.gfx.color.hex2rgb(a),dojo.gfx.color.hex2rgb(b),_6db));};dojo.gfx.color.extractRGB=function(_6dc){var hex="0123456789abcdef";_6dc=_6dc.toLowerCase();if(_6dc.indexOf("rgb")==0){var _6de=_6dc.match(/rgba*\((\d+), *(\d+), *(\d+)/i);var ret=_6de.splice(1,3);return ret;}else{var _6e0=dojo.gfx.color.hex2rgb(_6dc);if(_6e0){return _6e0;}else{return dojo.gfx.color.named[_6dc]||[255,255,255];}}};dojo.gfx.color.hex2rgb=function(hex){var _6e2="0123456789ABCDEF";var rgb=new Array(3);if(hex.indexOf("#")==0){hex=hex.substring(1);}
hex=hex.toUpperCase();if(hex.replace(new RegExp("["+_6e2+"]","g"),"")!=""){return null;}
if(hex.length==3){rgb[0]=hex.charAt(0)+hex.charAt(0);rgb[1]=hex.charAt(1)+hex.charAt(1);rgb[2]=hex.charAt(2)+hex.charAt(2);}else{rgb[0]=hex.substring(0,2);rgb[1]=hex.substring(2,4);rgb[2]=hex.substring(4);}
for(var i=0;i<rgb.length;i++){rgb[i]=_6e2.indexOf(rgb[i].charAt(0))*16+_6e2.indexOf(rgb[i].charAt(1));}
return rgb;};dojo.gfx.color.rgb2hex=function(r,g,b){if(dojo.lang.isArray(r)){g=r[1]||0;b=r[2]||0;r=r[0]||0;}
var ret=dojo.lang.map([r,g,b],function(x){x=new Number(x);var s=x.toString(16);while(s.length<2){s="0"+s;}
return s;});ret.unshift("#");return ret.join("");};dojo.provide("dojo.lfx.Animation");dojo.lfx.Line=function(_6eb,end){this.start=_6eb;this.end=end;if(dojo.lang.isArray(_6eb)){var diff=[];dojo.lang.forEach(this.start,function(s,i){diff[i]=this.end[i]-s;},this);this.getValue=function(n){var res=[];dojo.lang.forEach(this.start,function(s,i){res[i]=(diff[i]*n)+s;},this);return res;};}else{var diff=end-_6eb;this.getValue=function(n){return (diff*n)+this.start;};}};if((dojo.render.html.khtml)&&(!dojo.render.html.safari)){dojo.lfx.easeDefault=function(n){return (parseFloat("0.5")+((Math.sin((n+parseFloat("1.5"))*Math.PI))/2));};}else{dojo.lfx.easeDefault=function(n){return (0.5+((Math.sin((n+1.5)*Math.PI))/2));};}
dojo.lfx.easeIn=function(n){return Math.pow(n,3);};dojo.lfx.easeOut=function(n){return (1-Math.pow(1-n,3));};dojo.lfx.easeInOut=function(n){return ((3*Math.pow(n,2))-(2*Math.pow(n,3)));};dojo.lfx.IAnimation=function(){};dojo.lang.extend(dojo.lfx.IAnimation,{curve:null,duration:1000,easing:null,repeatCount:0,rate:10,handler:null,beforeBegin:null,onBegin:null,onAnimate:null,onEnd:null,onPlay:null,onPause:null,onStop:null,play:null,pause:null,stop:null,connect:function(evt,_6fb,_6fc){if(!_6fc){_6fc=_6fb;_6fb=this;}
_6fc=dojo.lang.hitch(_6fb,_6fc);var _6fd=this[evt]||function(){};this[evt]=function(){var ret=_6fd.apply(this,arguments);_6fc.apply(this,arguments);return ret;};return this;},fire:function(evt,args){if(this[evt]){this[evt].apply(this,(args||[]));}
return this;},repeat:function(_701){this.repeatCount=_701;return this;},_active:false,_paused:false});dojo.lfx.Animation=function(_702,_703,_704,_705,_706,rate){dojo.lfx.IAnimation.call(this);if(dojo.lang.isNumber(_702)||(!_702&&_703.getValue)){rate=_706;_706=_705;_705=_704;_704=_703;_703=_702;_702=null;}else{if(_702.getValue||dojo.lang.isArray(_702)){rate=_705;_706=_704;_705=_703;_704=_702;_703=null;_702=null;}}
if(dojo.lang.isArray(_704)){this.curve=new dojo.lfx.Line(_704[0],_704[1]);}else{this.curve=_704;}
if(_703!=null&&_703>0){this.duration=_703;}
if(_706){this.repeatCount=_706;}
if(rate){this.rate=rate;}
if(_702){dojo.lang.forEach(["handler","beforeBegin","onBegin","onEnd","onPlay","onStop","onAnimate"],function(item){if(_702[item]){this.connect(item,_702[item]);}},this);}
if(_705&&dojo.lang.isFunction(_705)){this.easing=_705;}};dojo.inherits(dojo.lfx.Animation,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Animation,{_startTime:null,_endTime:null,_timer:null,_percent:0,_startRepeatCount:0,play:function(_709,_70a){if(_70a){clearTimeout(this._timer);this._active=false;this._paused=false;this._percent=0;}else{if(this._active&&!this._paused){return this;}}
this.fire("handler",["beforeBegin"]);this.fire("beforeBegin");if(_709>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_70a);}),_709);return this;}
this._startTime=new Date().valueOf();if(this._paused){this._startTime-=(this.duration*this._percent/100);}
this._endTime=this._startTime+this.duration;this._active=true;this._paused=false;var step=this._percent/100;var _70c=this.curve.getValue(step);if(this._percent==0){if(!this._startRepeatCount){this._startRepeatCount=this.repeatCount;}
this.fire("handler",["begin",_70c]);this.fire("onBegin",[_70c]);}
this.fire("handler",["play",_70c]);this.fire("onPlay",[_70c]);this._cycle();return this;},pause:function(){clearTimeout(this._timer);if(!this._active){return this;}
this._paused=true;var _70d=this.curve.getValue(this._percent/100);this.fire("handler",["pause",_70d]);this.fire("onPause",[_70d]);return this;},gotoPercent:function(pct,_70f){clearTimeout(this._timer);this._active=true;this._paused=true;this._percent=pct;if(_70f){this.play();}
return this;},stop:function(_710){clearTimeout(this._timer);var step=this._percent/100;if(_710){step=1;}
var _712=this.curve.getValue(step);this.fire("handler",["stop",_712]);this.fire("onStop",[_712]);this._active=false;this._paused=false;return this;},status:function(){if(this._active){return this._paused?"paused":"playing";}else{return "stopped";}
return this;},_cycle:function(){clearTimeout(this._timer);if(this._active){var curr=new Date().valueOf();var step=(curr-this._startTime)/(this._endTime-this._startTime);if(step>=1){step=1;this._percent=100;}else{this._percent=step*100;}
if((this.easing)&&(dojo.lang.isFunction(this.easing))){step=this.easing(step);}
var _715=this.curve.getValue(step);this.fire("handler",["animate",_715]);this.fire("onAnimate",[_715]);if(step<1){this._timer=setTimeout(dojo.lang.hitch(this,"_cycle"),this.rate);}else{this._active=false;this.fire("handler",["end"]);this.fire("onEnd");if(this.repeatCount>0){this.repeatCount--;this.play(null,true);}else{if(this.repeatCount==-1){this.play(null,true);}else{if(this._startRepeatCount){this.repeatCount=this._startRepeatCount;this._startRepeatCount=0;}}}}}
return this;}});dojo.lfx.Combine=function(_716){dojo.lfx.IAnimation.call(this);this._anims=[];this._animsEnded=0;var _717=arguments;if(_717.length==1&&(dojo.lang.isArray(_717[0])||dojo.lang.isArrayLike(_717[0]))){_717=_717[0];}
dojo.lang.forEach(_717,function(anim){this._anims.push(anim);anim.connect("onEnd",dojo.lang.hitch(this,"_onAnimsEnded"));},this);};dojo.inherits(dojo.lfx.Combine,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Combine,{_animsEnded:0,play:function(_719,_71a){if(!this._anims.length){return this;}
this.fire("beforeBegin");if(_719>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_71a);}),_719);return this;}
if(_71a||this._anims[0].percent==0){this.fire("onBegin");}
this.fire("onPlay");this._animsCall("play",null,_71a);return this;},pause:function(){this.fire("onPause");this._animsCall("pause");return this;},stop:function(_71b){this.fire("onStop");this._animsCall("stop",_71b);return this;},_onAnimsEnded:function(){this._animsEnded++;if(this._animsEnded>=this._anims.length){this.fire("onEnd");}
return this;},_animsCall:function(_71c){var args=[];if(arguments.length>1){for(var i=1;i<arguments.length;i++){args.push(arguments[i]);}}
var _71f=this;dojo.lang.forEach(this._anims,function(anim){anim[_71c](args);},_71f);return this;}});dojo.lfx.Chain=function(_721){dojo.lfx.IAnimation.call(this);this._anims=[];this._currAnim=-1;var _722=arguments;if(_722.length==1&&(dojo.lang.isArray(_722[0])||dojo.lang.isArrayLike(_722[0]))){_722=_722[0];}
var _723=this;dojo.lang.forEach(_722,function(anim,i,_726){this._anims.push(anim);if(i<_726.length-1){anim.connect("onEnd",dojo.lang.hitch(this,"_playNext"));}else{anim.connect("onEnd",dojo.lang.hitch(this,function(){this.fire("onEnd");}));}},this);};dojo.inherits(dojo.lfx.Chain,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Chain,{_currAnim:-1,play:function(_727,_728){if(!this._anims.length){return this;}
if(_728||!this._anims[this._currAnim]){this._currAnim=0;}
var _729=this._anims[this._currAnim];this.fire("beforeBegin");if(_727>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_728);}),_727);return this;}
if(_729){if(this._currAnim==0){this.fire("handler",["begin",this._currAnim]);this.fire("onBegin",[this._currAnim]);}
this.fire("onPlay",[this._currAnim]);_729.play(null,_728);}
return this;},pause:function(){if(this._anims[this._currAnim]){this._anims[this._currAnim].pause();this.fire("onPause",[this._currAnim]);}
return this;},playPause:function(){if(this._anims.length==0){return this;}
if(this._currAnim==-1){this._currAnim=0;}
var _72a=this._anims[this._currAnim];if(_72a){if(!_72a._active||_72a._paused){this.play();}else{this.pause();}}
return this;},stop:function(){var _72b=this._anims[this._currAnim];if(_72b){_72b.stop();this.fire("onStop",[this._currAnim]);}
return _72b;},_playNext:function(){if(this._currAnim==-1||this._anims.length==0){return this;}
this._currAnim++;if(this._anims[this._currAnim]){this._anims[this._currAnim].play(null,true);}
return this;}});dojo.lfx.combine=function(_72c){var _72d=arguments;if(dojo.lang.isArray(arguments[0])){_72d=arguments[0];}
if(_72d.length==1){return _72d[0];}
return new dojo.lfx.Combine(_72d);};dojo.lfx.chain=function(_72e){var _72f=arguments;if(dojo.lang.isArray(arguments[0])){_72f=arguments[0];}
if(_72f.length==1){return _72f[0];}
return new dojo.lfx.Chain(_72f);};dojo.provide("dojo.html.color");dojo.html.getBackgroundColor=function(node){node=dojo.byId(node);var _731;do{_731=dojo.html.getStyle(node,"background-color");if(_731.toLowerCase()=="rgba(0, 0, 0, 0)"){_731="transparent";}
if(node==document.getElementsByTagName("body")[0]){node=null;break;}
node=node.parentNode;}while(node&&dojo.lang.inArray(["transparent",""],_731));if(_731=="transparent"){_731=[255,255,255,0];}else{_731=dojo.gfx.color.extractRGB(_731);}
return _731;};dojo.provide("dojo.lfx.html");dojo.lfx.html._byId=function(_732){if(!_732){return [];}
if(dojo.lang.isArrayLike(_732)){if(!_732.alreadyChecked){var n=[];dojo.lang.forEach(_732,function(node){n.push(dojo.byId(node));});n.alreadyChecked=true;return n;}else{return _732;}}else{var n=[];n.push(dojo.byId(_732));n.alreadyChecked=true;return n;}};dojo.lfx.html.propertyAnimation=function(_735,_736,_737,_738,_739){_735=dojo.lfx.html._byId(_735);var _73a={"propertyMap":_736,"nodes":_735,"duration":_737,"easing":_738||dojo.lfx.easeDefault};var _73b=function(args){if(args.nodes.length==1){var pm=args.propertyMap;if(!dojo.lang.isArray(args.propertyMap)){var parr=[];for(var _73f in pm){pm[_73f].property=_73f;parr.push(pm[_73f]);}
pm=args.propertyMap=parr;}
dojo.lang.forEach(pm,function(prop){if(dj_undef("start",prop)){if(prop.property!="opacity"){prop.start=parseInt(dojo.html.getComputedStyle(args.nodes[0],prop.property));}else{prop.start=dojo.html.getOpacity(args.nodes[0]);}}});}};var _741=function(_742){var _743=[];dojo.lang.forEach(_742,function(c){_743.push(Math.round(c));});return _743;};var _745=function(n,_747){n=dojo.byId(n);if(!n||!n.style){return;}
for(var s in _747){try{if(s=="opacity"){dojo.html.setOpacity(n,_747[s]);}else{n.style[s]=_747[s];}}
catch(e){dojo.debug(e);}}};var _749=function(_74a){this._properties=_74a;this.diffs=new Array(_74a.length);dojo.lang.forEach(_74a,function(prop,i){if(dojo.lang.isFunction(prop.start)){prop.start=prop.start(prop,i);}
if(dojo.lang.isFunction(prop.end)){prop.end=prop.end(prop,i);}
if(dojo.lang.isArray(prop.start)){this.diffs[i]=null;}else{if(prop.start instanceof dojo.gfx.color.Color){prop.startRgb=prop.start.toRgb();prop.endRgb=prop.end.toRgb();}else{this.diffs[i]=prop.end-prop.start;}}},this);this.getValue=function(n){var ret={};dojo.lang.forEach(this._properties,function(prop,i){var _751=null;if(dojo.lang.isArray(prop.start)){}else{if(prop.start instanceof dojo.gfx.color.Color){_751=(prop.units||"rgb")+"(";for(var j=0;j<prop.startRgb.length;j++){_751+=Math.round(((prop.endRgb[j]-prop.startRgb[j])*n)+prop.startRgb[j])+(j<prop.startRgb.length-1?",":"");}
_751+=")";}else{_751=((this.diffs[i])*n)+prop.start+(prop.property!="opacity"?prop.units||"px":"");}}
ret[dojo.html.toCamelCase(prop.property)]=_751;},this);return ret;};};var anim=new dojo.lfx.Animation({beforeBegin:function(){_73b(_73a);anim.curve=new _749(_73a.propertyMap);},onAnimate:function(_754){dojo.lang.forEach(_73a.nodes,function(node){_745(node,_754);});}},_73a.duration,null,_73a.easing);if(_739){for(var x in _739){if(dojo.lang.isFunction(_739[x])){anim.connect(x,anim,_739[x]);}}}
return anim;};dojo.lfx.html._makeFadeable=function(_757){var _758=function(node){if(dojo.render.html.ie){if((node.style.zoom.length==0)&&(dojo.html.getStyle(node,"zoom")=="normal")){node.style.zoom="1";}
if((node.style.width.length==0)&&(dojo.html.getStyle(node,"width")=="auto")){node.style.width="auto";}}};if(dojo.lang.isArrayLike(_757)){dojo.lang.forEach(_757,_758);}else{_758(_757);}};dojo.lfx.html.fade=function(_75a,_75b,_75c,_75d,_75e){_75a=dojo.lfx.html._byId(_75a);var _75f={property:"opacity"};if(!dj_undef("start",_75b)){_75f.start=_75b.start;}else{_75f.start=function(){return dojo.html.getOpacity(_75a[0]);};}
if(!dj_undef("end",_75b)){_75f.end=_75b.end;}else{dojo.raise("dojo.lfx.html.fade needs an end value");}
var anim=dojo.lfx.propertyAnimation(_75a,[_75f],_75c,_75d);anim.connect("beforeBegin",function(){dojo.lfx.html._makeFadeable(_75a);});if(_75e){anim.connect("onEnd",function(){_75e(_75a,anim);});}
return anim;};dojo.lfx.html.fadeIn=function(_761,_762,_763,_764){return dojo.lfx.html.fade(_761,{end:1},_762,_763,_764);};dojo.lfx.html.fadeOut=function(_765,_766,_767,_768){return dojo.lfx.html.fade(_765,{end:0},_766,_767,_768);};dojo.lfx.html.fadeShow=function(_769,_76a,_76b,_76c){_769=dojo.lfx.html._byId(_769);dojo.lang.forEach(_769,function(node){dojo.html.setOpacity(node,0);});var anim=dojo.lfx.html.fadeIn(_769,_76a,_76b,_76c);anim.connect("beforeBegin",function(){if(dojo.lang.isArrayLike(_769)){dojo.lang.forEach(_769,dojo.html.show);}else{dojo.html.show(_769);}});return anim;};dojo.lfx.html.fadeHide=function(_76f,_770,_771,_772){var anim=dojo.lfx.html.fadeOut(_76f,_770,_771,function(){if(dojo.lang.isArrayLike(_76f)){dojo.lang.forEach(_76f,dojo.html.hide);}else{dojo.html.hide(_76f);}
if(_772){_772(_76f,anim);}});return anim;};dojo.lfx.html.wipeIn=function(_774,_775,_776,_777){_774=dojo.lfx.html._byId(_774);var _778=[];dojo.lang.forEach(_774,function(node){var _77a={};var _77b,_77c,_77d;with(node.style){_77b=top;_77c=left;_77d=position;top="-9999px";left="-9999px";position="absolute";display="";}
var _77e=dojo.html.getBorderBox(node).height;with(node.style){top=_77b;left=_77c;position=_77d;display="none";}
var anim=dojo.lfx.propertyAnimation(node,{"height":{start:1,end:function(){return _77e;}}},_775,_776);anim.connect("beforeBegin",function(){_77a.overflow=node.style.overflow;_77a.height=node.style.height;with(node.style){overflow="hidden";height="1px";}
dojo.html.show(node);});anim.connect("onEnd",function(){with(node.style){overflow=_77a.overflow;height=_77a.height;}
if(_777){_777(node,anim);}});_778.push(anim);});return dojo.lfx.combine(_778);};dojo.lfx.html.wipeOut=function(_780,_781,_782,_783){_780=dojo.lfx.html._byId(_780);var _784=[];dojo.lang.forEach(_780,function(node){var _786={};var anim=dojo.lfx.propertyAnimation(node,{"height":{start:function(){return dojo.html.getContentBox(node).height;},end:1}},_781,_782,{"beforeBegin":function(){_786.overflow=node.style.overflow;_786.height=node.style.height;with(node.style){overflow="hidden";}
dojo.html.show(node);},"onEnd":function(){dojo.html.hide(node);with(node.style){overflow=_786.overflow;height=_786.height;}
if(_783){_783(node,anim);}}});_784.push(anim);});return dojo.lfx.combine(_784);};dojo.lfx.html.slideTo=function(_788,_789,_78a,_78b,_78c){_788=dojo.lfx.html._byId(_788);var _78d=[];var _78e=dojo.html.getComputedStyle;if(dojo.lang.isArray(_789)){dojo.deprecated("dojo.lfx.html.slideTo(node, array)","use dojo.lfx.html.slideTo(node, {top: value, left: value});","0.5");_789={top:_789[0],left:_789[1]};}
dojo.lang.forEach(_788,function(node){var top=null;var left=null;var init=(function(){var _793=node;return function(){var pos=_78e(_793,"position");top=(pos=="absolute"?node.offsetTop:parseInt(_78e(node,"top"))||0);left=(pos=="absolute"?node.offsetLeft:parseInt(_78e(node,"left"))||0);if(!dojo.lang.inArray(["absolute","relative"],pos)){var ret=dojo.html.abs(_793,true);dojo.html.setStyleAttributes(_793,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");top=ret.y;left=ret.x;}};})();init();var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:(_789.top||0)},"left":{start:left,end:(_789.left||0)}},_78a,_78b,{"beforeBegin":init});if(_78c){anim.connect("onEnd",function(){_78c(_788,anim);});}
_78d.push(anim);});return dojo.lfx.combine(_78d);};dojo.lfx.html.slideBy=function(_797,_798,_799,_79a,_79b){_797=dojo.lfx.html._byId(_797);var _79c=[];var _79d=dojo.html.getComputedStyle;if(dojo.lang.isArray(_798)){dojo.deprecated("dojo.lfx.html.slideBy(node, array)","use dojo.lfx.html.slideBy(node, {top: value, left: value});","0.5");_798={top:_798[0],left:_798[1]};}
dojo.lang.forEach(_797,function(node){var top=null;var left=null;var init=(function(){var _7a2=node;return function(){var pos=_79d(_7a2,"position");top=(pos=="absolute"?node.offsetTop:parseInt(_79d(node,"top"))||0);left=(pos=="absolute"?node.offsetLeft:parseInt(_79d(node,"left"))||0);if(!dojo.lang.inArray(["absolute","relative"],pos)){var ret=dojo.html.abs(_7a2,true);dojo.html.setStyleAttributes(_7a2,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");top=ret.y;left=ret.x;}};})();init();var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:top+(_798.top||0)},"left":{start:left,end:left+(_798.left||0)}},_799,_79a).connect("beforeBegin",init);if(_79b){anim.connect("onEnd",function(){_79b(_797,anim);});}
_79c.push(anim);});return dojo.lfx.combine(_79c);};dojo.lfx.html.explode=function(_7a6,_7a7,_7a8,_7a9,_7aa){var h=dojo.html;_7a6=dojo.byId(_7a6);_7a7=dojo.byId(_7a7);var _7ac=h.toCoordinateObject(_7a6,true);var _7ad=document.createElement("div");h.copyStyle(_7ad,_7a7);if(_7a7.explodeClassName){_7ad.className=_7a7.explodeClassName;}
with(_7ad.style){position="absolute";display="none";var _7ae=h.getStyle(_7a6,"background-color");backgroundColor=_7ae?_7ae.toLowerCase():"transparent";backgroundColor=(backgroundColor=="transparent")?"rgb(221, 221, 221)":backgroundColor;}
dojo.body().appendChild(_7ad);with(_7a7.style){visibility="hidden";display="block";}
var _7af=h.toCoordinateObject(_7a7,true);with(_7a7.style){display="none";visibility="visible";}
var _7b0={opacity:{start:0.5,end:1}};dojo.lang.forEach(["height","width","top","left"],function(type){_7b0[type]={start:_7ac[type],end:_7af[type]};});var anim=new dojo.lfx.propertyAnimation(_7ad,_7b0,_7a8,_7a9,{"beforeBegin":function(){h.setDisplay(_7ad,"block");},"onEnd":function(){h.setDisplay(_7a7,"block");_7ad.parentNode.removeChild(_7ad);}});if(_7aa){anim.connect("onEnd",function(){_7aa(_7a7,anim);});}
return anim;};dojo.lfx.html.implode=function(_7b3,end,_7b5,_7b6,_7b7){var h=dojo.html;_7b3=dojo.byId(_7b3);end=dojo.byId(end);var _7b9=dojo.html.toCoordinateObject(_7b3,true);var _7ba=dojo.html.toCoordinateObject(end,true);var _7bb=document.createElement("div");dojo.html.copyStyle(_7bb,_7b3);if(_7b3.explodeClassName){_7bb.className=_7b3.explodeClassName;}
dojo.html.setOpacity(_7bb,0.3);with(_7bb.style){position="absolute";display="none";backgroundColor=h.getStyle(_7b3,"background-color").toLowerCase();}
dojo.body().appendChild(_7bb);var _7bc={opacity:{start:1,end:0.5}};dojo.lang.forEach(["height","width","top","left"],function(type){_7bc[type]={start:_7b9[type],end:_7ba[type]};});var anim=new dojo.lfx.propertyAnimation(_7bb,_7bc,_7b5,_7b6,{"beforeBegin":function(){dojo.html.hide(_7b3);dojo.html.show(_7bb);},"onEnd":function(){_7bb.parentNode.removeChild(_7bb);}});if(_7b7){anim.connect("onEnd",function(){_7b7(_7b3,anim);});}
return anim;};dojo.lfx.html.highlight=function(_7bf,_7c0,_7c1,_7c2,_7c3){_7bf=dojo.lfx.html._byId(_7bf);var _7c4=[];dojo.lang.forEach(_7bf,function(node){var _7c6=dojo.html.getBackgroundColor(node);var bg=dojo.html.getStyle(node,"background-color").toLowerCase();var _7c8=dojo.html.getStyle(node,"background-image");var _7c9=(bg=="transparent"||bg=="rgba(0, 0, 0, 0)");while(_7c6.length>3){_7c6.pop();}
var rgb=new dojo.gfx.color.Color(_7c0);var _7cb=new dojo.gfx.color.Color(_7c6);var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:rgb,end:_7cb}},_7c1,_7c2,{"beforeBegin":function(){if(_7c8){node.style.backgroundImage="none";}
node.style.backgroundColor="rgb("+rgb.toRgb().join(",")+")";},"onEnd":function(){if(_7c8){node.style.backgroundImage=_7c8;}
if(_7c9){node.style.backgroundColor="transparent";}
if(_7c3){_7c3(node,anim);}}});_7c4.push(anim);});return dojo.lfx.combine(_7c4);};dojo.lfx.html.unhighlight=function(_7cd,_7ce,_7cf,_7d0,_7d1){_7cd=dojo.lfx.html._byId(_7cd);var _7d2=[];dojo.lang.forEach(_7cd,function(node){var _7d4=new dojo.gfx.color.Color(dojo.html.getBackgroundColor(node));var rgb=new dojo.gfx.color.Color(_7ce);var _7d6=dojo.html.getStyle(node,"background-image");var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:_7d4,end:rgb}},_7cf,_7d0,{"beforeBegin":function(){if(_7d6){node.style.backgroundImage="none";}
node.style.backgroundColor="rgb("+_7d4.toRgb().join(",")+")";},"onEnd":function(){if(_7d1){_7d1(node,anim);}}});_7d2.push(anim);});return dojo.lfx.combine(_7d2);};dojo.lang.mixin(dojo.lfx,dojo.lfx.html);dojo.kwCompoundRequire({browser:["dojo.lfx.html"],dashboard:["dojo.lfx.html"]});dojo.provide("dojo.lfx.*");dojo.provide("dojo.lfx.toggle");dojo.lfx.toggle.plain={show:function(node,_7d9,_7da,_7db){dojo.html.show(node);if(dojo.lang.isFunction(_7db)){_7db();}},hide:function(node,_7dd,_7de,_7df){dojo.html.hide(node);if(dojo.lang.isFunction(_7df)){_7df();}}};dojo.lfx.toggle.fade={show:function(node,_7e1,_7e2,_7e3){dojo.lfx.fadeShow(node,_7e1,_7e2,_7e3).play();},hide:function(node,_7e5,_7e6,_7e7){dojo.lfx.fadeHide(node,_7e5,_7e6,_7e7).play();}};dojo.lfx.toggle.wipe={show:function(node,_7e9,_7ea,_7eb){dojo.lfx.wipeIn(node,_7e9,_7ea,_7eb).play();},hide:function(node,_7ed,_7ee,_7ef){dojo.lfx.wipeOut(node,_7ed,_7ee,_7ef).play();}};dojo.lfx.toggle.explode={show:function(node,_7f1,_7f2,_7f3,_7f4){dojo.lfx.explode(_7f4||{x:0,y:0,width:0,height:0},node,_7f1,_7f2,_7f3).play();},hide:function(node,_7f6,_7f7,_7f8,_7f9){dojo.lfx.implode(node,_7f9||{x:0,y:0,width:0,height:0},_7f6,_7f7,_7f8).play();}};dojo.provide("dojo.widget.HtmlWidget");dojo.declare("dojo.widget.HtmlWidget",dojo.widget.DomWidget,{templateCssPath:null,templatePath:null,lang:"",toggle:"plain",toggleDuration:150,initialize:function(args,frag){},postMixInProperties:function(args,frag){if(this.lang===""){this.lang=null;}
this.toggleObj=dojo.lfx.toggle[this.toggle.toLowerCase()]||dojo.lfx.toggle.plain;},createNodesFromText:function(txt,wrap){return dojo.html.createNodesFromText(txt,wrap);},destroyRendering:function(_800){try{if(this.bgIframe){this.bgIframe.remove();delete this.bgIframe;}
if(!_800&&this.domNode){dojo.event.browser.clean(this.domNode);}
dojo.widget.HtmlWidget.superclass.destroyRendering.call(this);}
catch(e){}},isShowing:function(){return dojo.html.isShowing(this.domNode);},toggleShowing:function(){if(this.isShowing()){this.hide();}else{this.show();}},show:function(){if(this.isShowing()){return;}
this.animationInProgress=true;this.toggleObj.show(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onShow),this.explodeSrc);},onShow:function(){this.animationInProgress=false;this.checkSize();},hide:function(){if(!this.isShowing()){return;}
this.animationInProgress=true;this.toggleObj.hide(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onHide),this.explodeSrc);},onHide:function(){this.animationInProgress=false;},_isResized:function(w,h){if(!this.isShowing()){return false;}
var wh=dojo.html.getMarginBox(this.domNode);var _804=w||wh.width;var _805=h||wh.height;if(this.width==_804&&this.height==_805){return false;}
this.width=_804;this.height=_805;return true;},checkSize:function(){if(!this._isResized()){return;}
this.onResized();},resizeTo:function(w,h){dojo.html.setMarginBox(this.domNode,{width:w,height:h});if(this.isShowing()){this.onResized();}},resizeSoon:function(){if(this.isShowing()){dojo.lang.setTimeout(this,this.onResized,0);}},onResized:function(){dojo.lang.forEach(this.children,function(_808){if(_808.checkSize){_808.checkSize();}});}});dojo.kwCompoundRequire({common:["dojo.xml.Parse","dojo.widget.Widget","dojo.widget.Parse","dojo.widget.Manager"],browser:["dojo.widget.DomWidget","dojo.widget.HtmlWidget"],dashboard:["dojo.widget.DomWidget","dojo.widget.HtmlWidget"],svg:["dojo.widget.SvgWidget"],rhino:["dojo.widget.SwtWidget"]});dojo.provide("dojo.widget.*");dojo.provide("dojo.html.selection");dojo.html.selectionType={NONE:0,TEXT:1,CONTROL:2};dojo.html.clearSelection=function(){var _809=dojo.global();var _80a=dojo.doc();try{if(_809["getSelection"]){if(dojo.render.html.safari){_809.getSelection().collapse();}else{_809.getSelection().removeAllRanges();}}else{if(_80a.selection){if(_80a.selection.empty){_80a.selection.empty();}else{if(_80a.selection.clear){_80a.selection.clear();}}}}
return true;}
catch(e){dojo.debug(e);return false;}};dojo.html.disableSelection=function(_80b){_80b=dojo.byId(_80b)||dojo.body();var h=dojo.render.html;if(h.mozilla){_80b.style.MozUserSelect="none";}else{if(h.safari){_80b.style.KhtmlUserSelect="none";}else{if(h.ie){_80b.unselectable="on";}else{return false;}}}
return true;};dojo.html.enableSelection=function(_80d){_80d=dojo.byId(_80d)||dojo.body();var h=dojo.render.html;if(h.mozilla){_80d.style.MozUserSelect="";}else{if(h.safari){_80d.style.KhtmlUserSelect="";}else{if(h.ie){_80d.unselectable="off";}else{return false;}}}
return true;};dojo.html.selectElement=function(_80f){dojo.deprecated("dojo.html.selectElement","replaced by dojo.html.selection.selectElementChildren",0.5);};dojo.html.selectInputText=function(_810){var _811=dojo.global();var _812=dojo.doc();_810=dojo.byId(_810);if(_812["selection"]&&dojo.body()["createTextRange"]){var _813=_810.createTextRange();_813.moveStart("character",0);_813.moveEnd("character",_810.value.length);_813.select();}else{if(_811["getSelection"]){var _814=_811.getSelection();_810.setSelectionRange(0,_810.value.length);}}
_810.focus();};dojo.html.isSelectionCollapsed=function(){dojo.deprecated("dojo.html.isSelectionCollapsed","replaced by dojo.html.selection.isCollapsed",0.5);return dojo.html.selection.isCollapsed();};dojo.lang.mixin(dojo.html.selection,{getType:function(){if(dojo.doc()["selection"]){return dojo.html.selectionType[dojo.doc().selection.type.toUpperCase()];}else{var _815=dojo.html.selectionType.TEXT;var oSel;try{oSel=dojo.global().getSelection();}
catch(e){}
if(oSel&&oSel.rangeCount==1){var _817=oSel.getRangeAt(0);if(_817.startContainer==_817.endContainer&&(_817.endOffset-_817.startOffset)==1&&_817.startContainer.nodeType!=dojo.dom.TEXT_NODE){_815=dojo.html.selectionType.CONTROL;}}
return _815;}},isCollapsed:function(){var _818=dojo.global();var _819=dojo.doc();if(_819["selection"]){return _819.selection.createRange().text=="";}else{if(_818["getSelection"]){var _81a=_818.getSelection();if(dojo.lang.isString(_81a)){return _81a=="";}else{return _81a.isCollapsed||_81a.toString()=="";}}}},getSelectedElement:function(){if(dojo.html.selection.getType()==dojo.html.selectionType.CONTROL){if(dojo.doc()["selection"]){var _81b=dojo.doc().selection.createRange();if(_81b&&_81b.item){return dojo.doc().selection.createRange().item(0);}}else{var _81c=dojo.global().getSelection();return _81c.anchorNode.childNodes[_81c.anchorOffset];}}},getParentElement:function(){if(dojo.html.selection.getType()==dojo.html.selectionType.CONTROL){var p=dojo.html.selection.getSelectedElement();if(p){return p.parentNode;}}else{if(dojo.doc()["selection"]){return dojo.doc().selection.createRange().parentElement();}else{var _81e=dojo.global().getSelection();if(_81e){var node=_81e.anchorNode;while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){node=node.parentNode;}
return node;}}}},getSelectedText:function(){if(dojo.doc()["selection"]){if(dojo.html.selection.getType()==dojo.html.selectionType.CONTROL){return null;}
return dojo.doc().selection.createRange().text;}else{var _820=dojo.global().getSelection();if(_820){return _820.toString();}}},getSelectedHtml:function(){if(dojo.doc()["selection"]){if(dojo.html.selection.getType()==dojo.html.selectionType.CONTROL){return null;}
return dojo.doc().selection.createRange().htmlText;}else{var _821=dojo.global().getSelection();if(_821&&_821.rangeCount){var frag=_821.getRangeAt(0).cloneContents();var div=document.createElement("div");div.appendChild(frag);return div.innerHTML;}
return null;}},hasAncestorElement:function(_824){return (dojo.html.selection.getAncestorElement.apply(this,arguments)!=null);},getAncestorElement:function(_825){var node=dojo.html.selection.getSelectedElement()||dojo.html.selection.getParentElement();while(node){if(dojo.html.selection.isTag(node,arguments).length>0){return node;}
node=node.parentNode;}
return null;},isTag:function(node,tags){if(node&&node.tagName){for(var i=0;i<tags.length;i++){if(node.tagName.toLowerCase()==String(tags[i]).toLowerCase()){return String(tags[i]).toLowerCase();}}}
return "";},selectElement:function(_82a){var _82b=dojo.global();var _82c=dojo.doc();_82a=dojo.byId(_82a);if(_82c.selection&&dojo.body().createTextRange){try{var _82d=dojo.body().createControlRange();_82d.addElement(_82a);_82d.select();}
catch(e){dojo.html.selection.selectElementChildren(_82a);}}else{if(_82b["getSelection"]){var _82e=_82b.getSelection();if(_82e["removeAllRanges"]){var _82d=_82c.createRange();_82d.selectNode(_82a);_82e.removeAllRanges();_82e.addRange(_82d);}}}},selectElementChildren:function(_82f){var _830=dojo.global();var _831=dojo.doc();_82f=dojo.byId(_82f);if(_831.selection&&dojo.body().createTextRange){var _832=dojo.body().createTextRange();_832.moveToElementText(_82f);_832.select();}else{if(_830["getSelection"]){var _833=_830.getSelection();if(_833["setBaseAndExtent"]){_833.setBaseAndExtent(_82f,0,_82f,_82f.innerText.length-1);}else{if(_833["selectAllChildren"]){_833.selectAllChildren(_82f);}}}}},getBookmark:function(){var _834;var _835=dojo.doc();if(_835["selection"]){var _836=_835.selection.createRange();_834=_836.getBookmark();}else{var _837;try{_837=dojo.global().getSelection();}
catch(e){}
if(_837){var _836=_837.getRangeAt(0);_834=_836.cloneRange();}else{dojo.debug("No idea how to store the current selection for this browser!");}}
return _834;},moveToBookmark:function(_838){var _839=dojo.doc();if(_839["selection"]){var _83a=_839.selection.createRange();_83a.moveToBookmark(_838);_83a.select();}else{var _83b;try{_83b=dojo.global().getSelection();}
catch(e){}
if(_83b&&_83b["removeAllRanges"]){_83b.removeAllRanges();_83b.addRange(_838);}else{dojo.debug("No idea how to restore selection for this browser!");}}},collapse:function(_83c){if(dojo.global()["getSelection"]){var _83d=dojo.global().getSelection();if(_83d.removeAllRanges){if(_83c){_83d.collapseToStart();}else{_83d.collapseToEnd();}}else{dojo.global().getSelection().collapse(_83c);}}else{if(dojo.doc().selection){var _83e=dojo.doc().selection.createRange();_83e.collapse(_83c);_83e.select();}}},remove:function(){if(dojo.doc().selection){var _83f=dojo.doc().selection;if(_83f.type.toUpperCase()!="NONE"){_83f.clear();}
return _83f;}else{var _83f=dojo.global().getSelection();for(var i=0;i<_83f.rangeCount;i++){_83f.getRangeAt(i).deleteContents();}
return _83f;}}});dojo.provide("dojo.html.iframe");dojo.html.iframeContentWindow=function(_841){var win=dojo.html.getDocumentWindow(dojo.html.iframeContentDocument(_841))||dojo.html.iframeContentDocument(_841).__parent__||(_841.name&&document.frames[_841.name])||null;return win;};dojo.html.iframeContentDocument=function(_843){var doc=_843.contentDocument||((_843.contentWindow)&&(_843.contentWindow.document))||((_843.name)&&(document.frames[_843.name])&&(document.frames[_843.name].document))||null;return doc;};dojo.html.BackgroundIframe=function(node){if(dojo.render.html.ie55||dojo.render.html.ie60){var html="<iframe src='javascript:false'"+" style='position: absolute; left: 0px; top: 0px; width: 100%; height: 100%;"+"z-index: -1; filter:Alpha(Opacity=\"0\");' "+">";this.iframe=dojo.doc().createElement(html);this.iframe.tabIndex=-1;if(node){node.appendChild(this.iframe);this.domNode=node;}else{dojo.body().appendChild(this.iframe);this.iframe.style.display="none";}}};dojo.lang.extend(dojo.html.BackgroundIframe,{iframe:null,onResized:function(){if(this.iframe&&this.domNode&&this.domNode.parentNode){var _847=dojo.html.getMarginBox(this.domNode);if(_847.width==0||_847.height==0){dojo.lang.setTimeout(this,this.onResized,100);return;}
this.iframe.style.width=_847.width+"px";this.iframe.style.height=_847.height+"px";}},size:function(node){if(!this.iframe){return;}
var _849=dojo.html.toCoordinateObject(node,true,dojo.html.boxSizing.BORDER_BOX);with(this.iframe.style){width=_849.width+"px";height=_849.height+"px";left=_849.left+"px";top=_849.top+"px";}},setZIndex:function(node){if(!this.iframe){return;}
if(dojo.dom.isNode(node)){this.iframe.style.zIndex=dojo.html.getStyle(node,"z-index")-1;}else{if(!isNaN(node)){this.iframe.style.zIndex=node;}}},show:function(){if(this.iframe){this.iframe.style.display="block";}},hide:function(){if(this.iframe){this.iframe.style.display="none";}},remove:function(){if(this.iframe){dojo.html.removeNode(this.iframe,true);delete this.iframe;this.iframe=null;}}});dojo.provide("dojo.widget.PopupContainer");dojo.declare("dojo.widget.PopupContainerBase",null,function(){this.queueOnAnimationFinish=[];},{isShowingNow:false,currentSubpopup:null,beginZIndex:1000,parentPopup:null,parent:null,popupIndex:0,aroundBox:dojo.html.boxSizing.BORDER_BOX,openedForWindow:null,processKey:function(evt){return false;},applyPopupBasicStyle:function(){with(this.domNode.style){display="none";position="absolute";}},aboutToShow:function(){},open:function(x,y,_84e,_84f,_850,_851){if(this.isShowingNow){return;}
if(this.animationInProgress){this.queueOnAnimationFinish.push(this.open,arguments);return;}
this.aboutToShow();var _852=false,node,_854;if(typeof x=="object"){node=x;_854=_84f;_84f=_84e;_84e=y;_852=true;}
this.parent=_84e;dojo.body().appendChild(this.domNode);_84f=_84f||_84e["domNode"]||[];var _855=null;this.isTopLevel=true;while(_84e){if(_84e!==this&&(_84e.setOpenedSubpopup!=undefined&&_84e.applyPopupBasicStyle!=undefined)){_855=_84e;this.isTopLevel=false;_855.setOpenedSubpopup(this);break;}
_84e=_84e.parent;}
this.parentPopup=_855;this.popupIndex=_855?_855.popupIndex+1:1;if(this.isTopLevel){var _856=dojo.html.isNode(_84f)?_84f:null;dojo.widget.PopupManager.opened(this,_856);}
if(this.isTopLevel&&!dojo.withGlobal(this.openedForWindow||dojo.global(),dojo.html.selection.isCollapsed)){this._bookmark=dojo.withGlobal(this.openedForWindow||dojo.global(),dojo.html.selection.getBookmark);}else{this._bookmark=null;}
if(_84f instanceof Array){_84f={left:_84f[0],top:_84f[1],width:0,height:0};}
with(this.domNode.style){display="";zIndex=this.beginZIndex+this.popupIndex;}
if(_852){this.move(node,_851,_854);}else{this.move(x,y,_851,_850);}
this.domNode.style.display="none";this.explodeSrc=_84f;this.show();this.isShowingNow=true;},move:function(x,y,_859,_85a){var _85b=(typeof x=="object");if(_85b){var _85c=_859;var node=x;_859=y;if(!_85c){_85c={"BL":"TL","TL":"BL"};}
dojo.html.placeOnScreenAroundElement(this.domNode,node,_859,this.aroundBox,_85c);}else{if(!_85a){_85a="TL,TR,BL,BR";}
dojo.html.placeOnScreen(this.domNode,x,y,_859,true,_85a);}},close:function(_85e){if(_85e){this.domNode.style.display="none";}
if(this.animationInProgress){this.queueOnAnimationFinish.push(this.close,[]);return;}
this.closeSubpopup(_85e);this.hide();if(this.bgIframe){this.bgIframe.hide();this.bgIframe.size({left:0,top:0,width:0,height:0});}
if(this.isTopLevel){dojo.widget.PopupManager.closed(this);}
this.isShowingNow=false;if(this.parent){setTimeout(dojo.lang.hitch(this,function(){try{if(this.parent["focus"]){this.parent.focus();}else{this.parent.domNode.focus();}}
catch(e){dojo.debug("No idea how to focus to parent",e);}}),10);}
if(this._bookmark&&dojo.withGlobal(this.openedForWindow||dojo.global(),dojo.html.selection.isCollapsed)){if(this.openedForWindow){this.openedForWindow.focus();}
try{dojo.withGlobal(this.openedForWindow||dojo.global(),"moveToBookmark",dojo.html.selection,[this._bookmark]);}
catch(e){}}
this._bookmark=null;},closeAll:function(_85f){if(this.parentPopup){this.parentPopup.closeAll(_85f);}else{this.close(_85f);}},setOpenedSubpopup:function(_860){this.currentSubpopup=_860;},closeSubpopup:function(_861){if(this.currentSubpopup==null){return;}
this.currentSubpopup.close(_861);this.currentSubpopup=null;},onShow:function(){dojo.widget.PopupContainer.superclass.onShow.apply(this,arguments);this.openedSize={w:this.domNode.style.width,h:this.domNode.style.height};if(dojo.render.html.ie){if(!this.bgIframe){this.bgIframe=new dojo.html.BackgroundIframe();this.bgIframe.setZIndex(this.domNode);}
this.bgIframe.size(this.domNode);this.bgIframe.show();}
this.processQueue();},processQueue:function(){if(!this.queueOnAnimationFinish.length){return;}
var func=this.queueOnAnimationFinish.shift();var args=this.queueOnAnimationFinish.shift();func.apply(this,args);},onHide:function(){dojo.widget.HtmlWidget.prototype.onHide.call(this);if(this.openedSize){with(this.domNode.style){width=this.openedSize.w;height=this.openedSize.h;}}
this.processQueue();}});dojo.widget.defineWidget("dojo.widget.PopupContainer",[dojo.widget.HtmlWidget,dojo.widget.PopupContainerBase],{isContainer:true,fillInTemplate:function(){this.applyPopupBasicStyle();dojo.widget.PopupContainer.superclass.fillInTemplate.apply(this,arguments);}});dojo.widget.PopupManager=new function(){this.currentMenu=null;this.currentButton=null;this.currentFocusMenu=null;this.focusNode=null;this.registeredWindows=[];this.registerWin=function(win){if(!win.__PopupManagerRegistered){dojo.event.connect(win.document,"onmousedown",this,"onClick");dojo.event.connect(win,"onscroll",this,"onClick");dojo.event.connect(win.document,"onkey",this,"onKey");win.__PopupManagerRegistered=true;this.registeredWindows.push(win);}};this.registerAllWindows=function(_865){if(!_865){_865=dojo.html.getDocumentWindow(window.top&&window.top.document||window.document);}
this.registerWin(_865);for(var i=0;i<_865.frames.length;i++){try{var win=dojo.html.getDocumentWindow(_865.frames[i].document);if(win){this.registerAllWindows(win);}}
catch(e){}}};this.unRegisterWin=function(win){if(win.__PopupManagerRegistered){dojo.event.disconnect(win.document,"onmousedown",this,"onClick");dojo.event.disconnect(win,"onscroll",this,"onClick");dojo.event.disconnect(win.document,"onkey",this,"onKey");win.__PopupManagerRegistered=false;}};this.unRegisterAllWindows=function(){for(var i=0;i<this.registeredWindows.length;++i){this.unRegisterWin(this.registeredWindows[i]);}
this.registeredWindows=[];};dojo.addOnLoad(this,"registerAllWindows");dojo.addOnUnload(this,"unRegisterAllWindows");this.closed=function(menu){if(this.currentMenu==menu){this.currentMenu=null;this.currentButton=null;this.currentFocusMenu=null;}};this.opened=function(menu,_86c){if(menu==this.currentMenu){return;}
if(this.currentMenu){this.currentMenu.close();}
this.currentMenu=menu;this.currentFocusMenu=menu;this.currentButton=_86c;};this.setFocusedMenu=function(menu){this.currentFocusMenu=menu;};this.onKey=function(e){if(!e.key){return;}
if(!this.currentMenu||!this.currentMenu.isShowingNow){return;}
var m=this.currentFocusMenu;while(m){if(m.processKey(e)){e.preventDefault();e.stopPropagation();break;}
m=m.parentPopup||m.parentMenu;}},this.onClick=function(e){if(!this.currentMenu){return;}
var _871=dojo.html.getScroll().offset;var m=this.currentMenu;while(m){if(dojo.html.overElement(m.domNode,e)||dojo.html.isDescendantOf(e.target,m.domNode)){return;}
m=m.currentSubpopup;}
if(this.currentButton&&dojo.html.overElement(this.currentButton,e)){return;}
this.currentMenu.closeAll(true);};};dojo.provide("dojo.widget.DropdownContainer");dojo.widget.defineWidget("dojo.widget.DropdownContainer",dojo.widget.HtmlWidget,{inputWidth:"7em",id:"",inputId:"",inputName:"",iconURL:dojo.uri.moduleUri("dojo.widget","templates/images/combo_box_arrow.png"),copyClasses:false,iconAlt:"",containerToggle:"plain",containerToggleDuration:150,templateString:"<span style=\"white-space:nowrap\"><input type=\"hidden\" name=\"\" value=\"\" dojoAttachPoint=\"valueNode\" /><input name=\"\" type=\"text\" value=\"\" style=\"vertical-align:middle;\" dojoAttachPoint=\"inputNode\" autocomplete=\"off\" /> <img src=\"${this.iconURL}\" alt=\"${this.iconAlt}\" dojoAttachEvent=\"onclick:onIconClick\" dojoAttachPoint=\"buttonNode\" style=\"vertical-align:middle; cursor:pointer; cursor:hand\" /></span>",templateCssPath:"",isContainer:true,attachTemplateNodes:function(){dojo.widget.DropdownContainer.superclass.attachTemplateNodes.apply(this,arguments);this.popup=dojo.widget.createWidget("PopupContainer",{toggle:this.containerToggle,toggleDuration:this.containerToggleDuration});this.containerNode=this.popup.domNode;},fillInTemplate:function(args,frag){this.domNode.appendChild(this.popup.domNode);if(this.id){this.domNode.id=this.id;}
if(this.inputId){this.inputNode.id=this.inputId;}
if(this.inputName){this.inputNode.name=this.inputName;}
this.inputNode.style.width=this.inputWidth;this.inputNode.disabled=this.disabled;if(this.copyClasses){this.inputNode.style="";this.inputNode.className=this.getFragNodeRef(frag).className;}
dojo.event.connect(this.inputNode,"onchange",this,"onInputChange");},onIconClick:function(evt){if(this.disabled){return;}
if(!this.popup.isShowingNow){this.popup.open(this.inputNode,this,this.buttonNode);}else{this.popup.close();}},hideContainer:function(){if(this.popup.isShowingNow){this.popup.close();}},onInputChange:function(){},enable:function(){this.inputNode.disabled=false;dojo.widget.DropdownContainer.superclass.enable.apply(this,arguments);},disable:function(){this.inputNode.disabled=true;dojo.widget.DropdownContainer.superclass.disable.apply(this,arguments);}});