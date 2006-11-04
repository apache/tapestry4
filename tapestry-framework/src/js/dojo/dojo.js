
if(typeof dojo=="undefined"){var dj_global=this;var dj_currentContext=this;function dj_undef(_1,_2){return (typeof (_2||dj_currentContext)[_1]=="undefined");}
if(dj_undef("djConfig",this)){var djConfig={};}
if(dj_undef("dojo",this)){var dojo={};}
dojo.global=function(){return dj_currentContext;};dojo.locale=djConfig.locale;dojo.version={major:0,minor:0,patch:0,flag:"dev",revision:Number("$Rev: 6392 $".match(/[0-9]+/)[0]),toString:function(){with(dojo.version){return major+"."+minor+"."+patch+flag+" ("+revision+")";}}};dojo.evalProp=function(_3,_4,_5){if((!_4)||(!_3)){return undefined;}
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
dojo.hostenv.callLoaded();}};dojo.hostenv.callLoaded=function(){if(typeof setTimeout=="object"){setTimeout("dojo.hostenv.loaded();",0);}else{dojo.hostenv.loaded();}};dojo.hostenv.getModuleSymbols=function(_42){var _43=_42.split(".");for(var i=_43.length;i>0;i--){var _45=_43.slice(0,i).join(".");if((i==1)&&!this.moduleHasPrefix(_45)){_43[0]="../"+_43[0];}else{var _46=this.getModulePrefix(_45);if(_46!=_45){_43.splice(0,i,_46);break;}}}
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
dojo.require.apply(dojo,_63);}};dojo.requireAfterIf=dojo.requireIf;dojo.provide=function(_65){return dojo.hostenv.startPackage.apply(dojo.hostenv,arguments);};dojo.registerModulePath=function(_66,_67){return dojo.hostenv.setModulePrefix(_66,_67);};dojo.setModulePrefix=function(_68,_69){dojo.deprecated("dojo.setModulePrefix(\""+_68+"\", \""+_69+"\")","replaced by dojo.registerModulePath","0.5");return dojo.registerModulePath(_68,_69);};dojo.exists=function(obj,_6b){var p=_6b.split(".");for(var i=0;i<p.length;i++){if(!obj[p[i]]){return false;}
obj=obj[p[i]];}
return true;};dojo.hostenv.normalizeLocale=function(_6e){return _6e?_6e.toLowerCase():dojo.locale;};dojo.hostenv.searchLocalePath=function(_6f,_70,_71){_6f=dojo.hostenv.normalizeLocale(_6f);var _72=_6f.split("-");var _73=[];for(var i=_72.length;i>0;i--){_73.push(_72.slice(0,i).join("-"));}
_73.push(false);if(_70){_73.reverse();}
for(var j=_73.length-1;j>=0;j--){var loc=_73[j]||"ROOT";var _77=_71(loc);if(_77){break;}}};dojo.hostenv.localesGenerated=["ROOT","es-es","es","it-it","pt-br","de","fr-fr","zh-cn","pt","en-us","zh","fr","zh-tw","it","en-gb","xx","de-de","ko-kr","ja-jp","ko","en","ja"];dojo.hostenv.registerNlsPrefix=function(){dojo.registerModulePath("nls","nls");};dojo.hostenv.preloadLocalizations=function(){if(dojo.hostenv.localesGenerated){dojo.hostenv.registerNlsPrefix();function preload(_78){dojo.log.debug("locale passed in is " + _78);_78=dojo.hostenv.normalizeLocale(_78);dojo.hostenv.searchLocalePath(_78,true,function(loc){for(var i=0;i<dojo.hostenv.localesGenerated.length;i++){if(dojo.hostenv.localesGenerated[i]==loc){dojo["require"]("nls.dojo_"+loc);return true;}}
return false;});}
preload();var _7b=djConfig.extraLocale||[];for(var i=0;i<_7b.length;i++){preload(_7b[i]);}}
dojo.hostenv.preloadLocalizations=function(){};};dojo.requireLocalization=function(_7d,_7e,_7f){dojo.hostenv.preloadLocalizations();var _80=[_7d,"nls",_7e].join(".");var _81=dojo.hostenv.findModule(_80);if(_81){if(djConfig.localizationComplete&&_81._built){return;}
var _82=dojo.hostenv.normalizeLocale(_7f).replace("-","_");var _83=_80+"."+_82;if(dojo.hostenv.findModule(_83)){return;}}
_81=dojo.hostenv.startPackage(_80);var _84=dojo.hostenv.getModuleSymbols(_7d);var _85=_84.concat("nls").join("/");var _86;dojo.hostenv.searchLocalePath(_7f,false,function(loc){var _88=loc.replace("-","_");var _89=_80+"."+_88;var _8a=false;if(!dojo.hostenv.findModule(_89)){dojo.hostenv.startPackage(_89);var _8b=[_85];if(loc!="ROOT"){_8b.push(loc);}
_8b.push(_7e);var _8c=_8b.join("/")+".js";_8a=dojo.hostenv.loadPath(_8c,null,function(_8d){var _8e=function(){};_8e.prototype=_86;_81[_88]=new _8e();for(var j in _8d){_81[_88][j]=_8d[j];}});}else{_8a=true;}
if(_8a&&_81[_88]){_86=_81[_88];}else{_81[_88]=_86;}});};(function(){var _90=djConfig.extraLocale;if(_90){if(!_90 instanceof Array){_90=[_90];}
var req=dojo.requireLocalization;dojo.requireLocalization=function(m,b,_94){req(m,b,_94);if(_94){return;}
for(var i=0;i<_90.length;i++){req(m,b,_90[i]);}};}})();}
if(typeof window!="undefined"){(function(){if(djConfig.allowQueryConfig){var _96=document.location.toString();var _97=_96.split("?",2);if(_97.length>1){var _98=_97[1];var _99=_98.split("&");for(var x in _99){var sp=_99[x].split("=");if((sp[0].length>9)&&(sp[0].substr(0,9)=="djConfig.")){var opt=sp[0].substr(9);try{djConfig[opt]=eval(sp[1]);}
catch(e){djConfig[opt]=sp[1];}}}}}
if(((djConfig["baseScriptUri"]=="")||(djConfig["baseRelativePath"]==""))&&(document&&document.getElementsByTagName)){var _9d=document.getElementsByTagName("script");var _9e=/(__package__|dojo|bootstrap1)\.js([\?\.]|$)/i;for(var i=0;i<_9d.length;i++){var src=_9d[i].getAttribute("src");if(!src){continue;}
var m=src.match(_9e);if(m){var _a2=src.substring(0,m.index);if(src.indexOf("bootstrap1")>-1){_a2+="../";}
if(!this["djConfig"]){djConfig={};}
if(djConfig["baseScriptUri"]==""){djConfig["baseScriptUri"]=_a2;}
if(djConfig["baseRelativePath"]==""){djConfig["baseRelativePath"]=_a2;}
break;}}}
var dr=dojo.render;var drh=dojo.render.html;var drs=dojo.render.svg;var dua=(drh.UA=navigator.userAgent);var dav=(drh.AV=navigator.appVersion);var t=true;var f=false;drh.capable=t;drh.support.builtin=t;dr.ver=parseFloat(drh.AV);dr.os.mac=dav.indexOf("Macintosh")>=0;dr.os.win=dav.indexOf("Windows")>=0;dr.os.linux=dav.indexOf("X11")>=0;drh.opera=dua.indexOf("Opera")>=0;drh.khtml=(dav.indexOf("Konqueror")>=0)||(dav.indexOf("Safari")>=0);drh.safari=dav.indexOf("Safari")>=0;var _aa=dua.indexOf("Gecko");drh.mozilla=drh.moz=(_aa>=0)&&(!drh.khtml);if(drh.mozilla){drh.geckoVersion=dua.substring(_aa+6,_aa+14);}
drh.ie=(document.all)&&(!drh.opera);drh.ie50=drh.ie&&dav.indexOf("MSIE 5.0")>=0;drh.ie55=drh.ie&&dav.indexOf("MSIE 5.5")>=0;drh.ie60=drh.ie&&dav.indexOf("MSIE 6.0")>=0;drh.ie70=drh.ie&&dav.indexOf("MSIE 7.0")>=0;var cm=document["compatMode"];drh.quirks=(cm=="BackCompat")||(cm=="QuirksMode")||drh.ie55||drh.ie50;dojo.locale=dojo.locale||(drh.ie?navigator.userLanguage:navigator.language).toLowerCase();dr.vml.capable=drh.ie;drs.capable=f;drs.support.plugin=f;drs.support.builtin=f;var _ac=window["document"];var tdi=_ac["implementation"];if((tdi)&&(tdi["hasFeature"])&&(tdi.hasFeature("org.w3c.dom.svg","1.0"))){drs.capable=t;drs.support.builtin=t;drs.support.plugin=f;}
if(drh.safari){var tmp=dua.split("AppleWebKit/")[1];var ver=parseFloat(tmp.split(" ")[0]);if(ver>=420){drs.capable=t;drs.support.builtin=t;drs.support.plugin=f;}}})();dojo.hostenv.startPackage("dojo.hostenv");dojo.render.name=dojo.hostenv.name_="browser";dojo.hostenv.searchIds=[];dojo.hostenv._XMLHTTP_PROGIDS=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"];dojo.hostenv.getXmlhttpObject=function(){var _b0=null;var _b1=null;try{_b0=new XMLHttpRequest();}
catch(e){}
if(!_b0){for(var i=0;i<3;++i){var _b3=dojo.hostenv._XMLHTTP_PROGIDS[i];try{_b0=new ActiveXObject(_b3);}
catch(e){_b1=e;}
if(_b0){dojo.hostenv._XMLHTTP_PROGIDS=[_b3];break;}}}
if(!_b0){return dojo.raise("XMLHTTP not available",_b1);}
return _b0;};dojo.hostenv._blockAsync=false;dojo.hostenv.getText=function(uri,_b5,_b6){if(!_b5){this._blockAsync=true;}
var _b7=this.getXmlhttpObject();function isDocumentOk(_b8){var _b9=_b8["status"];return Boolean((!_b9)||((200<=_b9)&&(300>_b9))||(_b9==304));}
if(_b5){var _ba=this,_bb=null,gbl=dojo.global();var xhr=dojo.evalObjPath("dojo.io.XMLHTTPTransport");_b7.onreadystatechange=function(){if(_bb){gbl.clearTimeout(_bb);_bb=null;}
if(_ba._blockAsync||(xhr&&xhr._blockAsync)){_bb=gbl.setTimeout(function(){_b7.onreadystatechange.apply(this);},10);}else{if(4==_b7.readyState){if(isDocumentOk(_b7)){_b5(_b7.responseText);}}}};}
_b7.open("GET",uri,_b5?true:false);try{_b7.send(null);if(_b5){return null;}
if(!isDocumentOk(_b7)){var err=Error("Unable to load "+uri+" status:"+_b7.status);err.status=_b7.status;err.responseText=_b7.responseText;throw err;}}
catch(e){this._blockAsync=false;if((_b6)&&(!_b5)){return null;}else{throw e;}}
this._blockAsync=false;return _b7.responseText;};dojo.hostenv.defaultDebugContainerId="dojoDebug";dojo.hostenv._println_buffer=[];dojo.hostenv._println_safe=false;dojo.hostenv.println=function(_bf){if(!dojo.hostenv._println_safe){dojo.hostenv._println_buffer.push(_bf);}else{try{var _c0=document.getElementById(djConfig.debugContainerId?djConfig.debugContainerId:dojo.hostenv.defaultDebugContainerId);if(!_c0){_c0=dojo.body();}
var div=document.createElement("div");div.appendChild(document.createTextNode(_bf));_c0.appendChild(div);}
catch(e){try{document.write("<div>"+_bf+"</div>");}
catch(e2){window.status=_bf;}}}};dojo.addOnLoad(function(){dojo.hostenv._println_safe=true;while(dojo.hostenv._println_buffer.length>0){dojo.hostenv.println(dojo.hostenv._println_buffer.shift());}});function dj_addNodeEvtHdlr(_c2,_c3,fp,_c5){var _c6=_c2["on"+_c3]||function(){};_c2["on"+_c3]=function(){fp.apply(_c2,arguments);_c6.apply(_c2,arguments);};return true;}
function dj_load_init(e){var _c8=(e&&e.type)?e.type.toLowerCase():"load";if(arguments.callee.initialized||(_c8!="domcontentloaded"&&_c8!="load")){return;}
arguments.callee.initialized=true;if(typeof (_timer)!="undefined"){clearInterval(_timer);delete _timer;}
var _c9=function(){if(dojo.render.html.ie){dojo.hostenv.makeWidgets();}};if(dojo.hostenv.inFlightCount==0){_c9();dojo.hostenv.modulesLoaded();}else{dojo.addOnLoad(_c9);}}
if(document.addEventListener){if(dojo.render.html.opera||(dojo.render.html.moz&&!djConfig.delayMozLoadingFix)){document.addEventListener("DOMContentLoaded",dj_load_init,null);}
window.addEventListener("load",dj_load_init,null);}
if(dojo.render.html.ie&&dojo.render.os.win){document.attachEvent("onreadystatechange",function(e){if(document.readyState=="complete"){dj_load_init();}});}
if(/(WebKit|khtml)/i.test(navigator.userAgent)){var _timer=setInterval(function(){if(/loaded|complete/.test(document.readyState)){dj_load_init();}},10);}
if(dojo.render.html.ie){dj_addNodeEvtHdlr(window,"beforeunload",function(){dojo.hostenv._unloading=true;window.setTimeout(function(){dojo.hostenv._unloading=false;},0);});}
dj_addNodeEvtHdlr(window,"unload",function(){dojo.hostenv.unloaded();if((!dojo.render.html.ie)||(dojo.render.html.ie&&dojo.hostenv._unloading)){dojo.hostenv.unloaded();}});dojo.hostenv.makeWidgets=function(){var _cb=[];if(djConfig.searchIds&&djConfig.searchIds.length>0){_cb=_cb.concat(djConfig.searchIds);}
if(dojo.hostenv.searchIds&&dojo.hostenv.searchIds.length>0){_cb=_cb.concat(dojo.hostenv.searchIds);}
if((djConfig.parseWidgets)||(_cb.length>0)){if(dojo.evalObjPath("dojo.widget.Parse")){var _cc=new dojo.xml.Parse();if(_cb.length>0){for(var x=0;x<_cb.length;x++){var _ce=document.getElementById(_cb[x]);if(!_ce){continue;}
var _cf=_cc.parseElement(_ce,null,true);dojo.widget.getParser().createComponents(_cf);}}else{if(djConfig.parseWidgets){var _cf=_cc.parseElement(dojo.body(),null,true);dojo.widget.getParser().createComponents(_cf);}}}}};dojo.addOnLoad(function(){if(!dojo.render.html.ie){dojo.hostenv.makeWidgets();}});try{if(dojo.render.html.ie){document.namespaces.add("v","urn:schemas-microsoft-com:vml");document.createStyleSheet().addRule("v\\:*","behavior:url(#default#VML)");}}
catch(e){}
dojo.hostenv.writeIncludes=function(){};if(!dj_undef("document",this)){dj_currentDocument=this.document;}
dojo.doc=function(){return dj_currentDocument;};dojo.body=function(){return dojo.doc().body||dojo.doc().getElementsByTagName("body")[0];};dojo.byId=function(id,doc){if((id)&&((typeof id=="string")||(id instanceof String))){if(!doc){doc=dj_currentDocument;}
var ele=doc.getElementById(id);if(ele&&(ele.id!=id)&&doc.all){ele=null;eles=doc.all[id];if(eles){if(eles.length){for(var i=0;i<eles.length;i++){if(eles[i].id==id){ele=eles[i];break;}}}else{ele=eles;}}}
return ele;}
return id;};dojo.setContext=function(_d4,_d5){dj_currentContext=_d4;dj_currentDocument=_d5;};dojo._fireCallback=function(_d6,_d7,_d8){if((_d7)&&((typeof _d6=="string")||(_d6 instanceof String))){_d6=_d7[_d6];}
return (_d7?_d6.apply(_d7,_d8||[]):_d6());};dojo.withGlobal=function(_d9,_da,_db,_dc){var _dd;var _de=dj_currentContext;var _df=dj_currentDocument;try{dojo.setContext(_d9,_d9.document);_dd=dojo._fireCallback(_da,_db,_dc);}
finally{dojo.setContext(_de,_df);}
return _dd;};dojo.withDoc=function(_e0,_e1,_e2,_e3){var _e4;var _e5=dj_currentDocument;try{dj_currentDocument=_e0;_e4=dojo._fireCallback(_e1,_e2,_e3);}
finally{dj_currentDocument=_e5;}
return _e4;};}
(function(){if(typeof dj_usingBootstrap!="undefined"){return;}
var _e6=false;var _e7=false;var _e8=false;if((typeof this["load"]=="function")&&((typeof this["Packages"]=="function")||(typeof this["Packages"]=="object"))){_e6=true;}else{if(typeof this["load"]=="function"){_e7=true;}else{if(window.widget){_e8=true;}}}
var _e9=[];if((this["djConfig"])&&((djConfig["isDebug"])||(djConfig["debugAtAllCosts"]))){_e9.push("debug.js");}
if((this["djConfig"])&&(djConfig["debugAtAllCosts"])&&(!_e6)&&(!_e8)){_e9.push("browser_debug.js");}
var _ea=djConfig["baseScriptUri"];if((this["djConfig"])&&(djConfig["baseLoaderUri"])){_ea=djConfig["baseLoaderUri"];}
for(var x=0;x<_e9.length;x++){var _ec=_ea+"src/"+_e9[x];if(_e6||_e7){load(_ec);}else{try{document.write("<scr"+"ipt type='text/javascript' src='"+_ec+"'></scr"+"ipt>");}
catch(e){var _ed=document.createElement("script");_ed.src=_ec;document.getElementsByTagName("head")[0].appendChild(_ed);}}}})();dojo.provide("dojo.lang.common");dojo.lang.inherits=function(_ee,_ef){if(typeof _ef!="function"){dojo.raise("dojo.inherits: superclass argument ["+_ef+"] must be a function (subclass: ["+_ee+"']");}
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
return false;};dojo.lang.isFunction=function(it){if(!it){return false;}
if((typeof (it)=="function")&&(it=="[object NodeList]")){return false;}
return (it instanceof Function||typeof it=="function");};dojo.lang.isString=function(it){return (typeof it=="string"||it instanceof String);};dojo.lang.isAlien=function(it){if(!it){return false;}
return !dojo.lang.isFunction()&&/\{\s*\[native code\]\s*\}/.test(String(it));};dojo.lang.isBoolean=function(it){return (it instanceof Boolean||typeof it=="boolean");};dojo.lang.isNumber=function(it){return (it instanceof Number||typeof it=="number");};dojo.lang.isUndefined=function(it){return ((typeof (it)=="undefined")&&(it==undefined));};dojo.provide("dojo.lang.array");dojo.lang.has=function(obj,name){try{return typeof obj[name]!="undefined";}
catch(e){return false;}};dojo.lang.isEmpty=function(obj){if(dojo.lang.isObject(obj)){var tmp={};var _117=0;for(var x in obj){if(obj[x]&&(!tmp[x])){_117++;break;}}
return _117==0;}else{if(dojo.lang.isArrayLike(obj)||dojo.lang.isString(obj)){return obj.length==0;}}};dojo.lang.map=function(arr,obj,_11b){var _11c=dojo.lang.isString(arr);if(_11c){arr=arr.split("");}
if(dojo.lang.isFunction(obj)&&(!_11b)){_11b=obj;obj=dj_global;}else{if(dojo.lang.isFunction(obj)&&_11b){var _11d=obj;obj=_11b;_11b=_11d;}}
if(Array.map){var _11e=Array.map(arr,_11b,obj);}else{var _11e=[];for(var i=0;i<arr.length;++i){_11e.push(_11b.call(obj,arr[i]));}}
if(_11c){return _11e.join("");}else{return _11e;}};dojo.lang.reduce=function(arr,_121,obj,_123){var _124=_121;var ob=obj?obj:dj_global;dojo.lang.map(arr,function(val){_124=_123.call(ob,_124,val);});return _124;};dojo.lang.forEach=function(_127,_128,_129){if(dojo.lang.isString(_127)){_127=_127.split("");}
if(Array.forEach){Array.forEach(_127,_128,_129);}else{if(!_129){_129=dj_global;}
for(var i=0,l=_127.length;i<l;i++){_128.call(_129,_127[i],i,_127);}}};dojo.lang._everyOrSome=function(_12c,arr,_12e,_12f){if(dojo.lang.isString(arr)){arr=arr.split("");}
if(Array.every){return Array[_12c?"every":"some"](arr,_12e,_12f);}else{if(!_12f){_12f=dj_global;}
for(var i=0,l=arr.length;i<l;i++){var _132=_12e.call(_12f,arr[i],i,arr);if(_12c&&!_132){return false;}else{if((!_12c)&&(_132)){return true;}}}
return Boolean(_12c);}};dojo.lang.every=function(arr,_134,_135){return this._everyOrSome(true,arr,_134,_135);};dojo.lang.some=function(arr,_137,_138){return this._everyOrSome(false,arr,_137,_138);};dojo.lang.filter=function(arr,_13a,_13b){var _13c=dojo.lang.isString(arr);if(_13c){arr=arr.split("");}
var _13d;if(Array.filter){_13d=Array.filter(arr,_13a,_13b);}else{if(!_13b){if(arguments.length>=3){dojo.raise("thisObject doesn't exist!");}
_13b=dj_global;}
_13d=[];for(var i=0;i<arr.length;i++){if(_13a.call(_13b,arr[i],i,arr)){_13d.push(arr[i]);}}}
if(_13c){return _13d.join("");}else{return _13d;}};dojo.lang.unnest=function(){var out=[];for(var i=0;i<arguments.length;i++){if(dojo.lang.isArrayLike(arguments[i])){var add=dojo.lang.unnest.apply(this,arguments[i]);out=out.concat(add);}else{out.push(arguments[i]);}}
return out;};dojo.lang.toArray=function(_142,_143){var _144=[];for(var i=_143||0;i<_142.length;i++){_144.push(_142[i]);}
return _144;};dojo.provide("dojo.lang.extras");dojo.lang.setTimeout=function(func,_147){var _148=window,_149=2;if(!dojo.lang.isFunction(func)){_148=func;func=_147;_147=arguments[2];_149++;}
if(dojo.lang.isString(func)){func=_148[func];}
var args=[];for(var i=_149;i<arguments.length;i++){args.push(arguments[i]);}
return dojo.global().setTimeout(function(){func.apply(_148,args);},_147);};dojo.lang.clearTimeout=function(_14c){dojo.global().clearTimeout(_14c);};dojo.lang.getNameInObj=function(ns,item){if(!ns){ns=dj_global;}
for(var x in ns){if(ns[x]===item){return new String(x);}}
return null;};dojo.lang.shallowCopy=function(obj,deep){var i,ret;if(obj===null){return null;}
if(dojo.lang.isObject(obj)){ret=new obj.constructor();for(i in obj){if(dojo.lang.isUndefined(ret[i])){ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];}}}else{if(dojo.lang.isArray(obj)){ret=[];for(i=0;i<obj.length;i++){ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];}}else{ret=obj;}}
return ret;};dojo.lang.firstValued=function(){for(var i=0;i<arguments.length;i++){if(typeof arguments[i]!="undefined"){return arguments[i];}}
return undefined;};dojo.lang.getObjPathValue=function(_155,_156,_157){with(dojo.parseObjPath(_155,_156,_157)){return dojo.evalProp(prop,obj,_157);}};dojo.lang.setObjPathValue=function(_158,_159,_15a,_15b){if(arguments.length<4){_15b=true;}
with(dojo.parseObjPath(_158,_15a,_15b)){if(obj&&(_15b||(prop in obj))){obj[prop]=_159;}}};dojo.provide("dojo.lang.func");dojo.lang.hitch=function(_15c,_15d){var fcn=(dojo.lang.isString(_15d)?_15c[_15d]:_15d)||function(){};return function(){return fcn.apply(_15c,arguments);};};dojo.lang.anonCtr=0;dojo.lang.anon={};dojo.lang.nameAnonFunc=function(_15f,_160,_161){var nso=(_160||dojo.lang.anon);if((_161)||((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true))){for(var x in nso){try{if(nso[x]===_15f){return x;}}
catch(e){}}}
var ret="__"+dojo.lang.anonCtr++;while(typeof nso[ret]!="undefined"){ret="__"+dojo.lang.anonCtr++;}
nso[ret]=_15f;return ret;};dojo.lang.forward=function(_165){return function(){return this[_165].apply(this,arguments);};};dojo.lang.curry=function(ns,func){var _168=[];ns=ns||dj_global;if(dojo.lang.isString(func)){func=ns[func];}
for(var x=2;x<arguments.length;x++){_168.push(arguments[x]);}
var _16a=(func["__preJoinArity"]||func.length)-_168.length;function gather(_16b,_16c,_16d){var _16e=_16d;var _16f=_16c.slice(0);for(var x=0;x<_16b.length;x++){_16f.push(_16b[x]);}
_16d=_16d-_16b.length;if(_16d<=0){var res=func.apply(ns,_16f);_16d=_16e;return res;}else{return function(){return gather(arguments,_16f,_16d);};}}
return gather([],_168,_16a);};dojo.lang.curryArguments=function(ns,func,args,_175){var _176=[];var x=_175||0;for(x=_175;x<args.length;x++){_176.push(args[x]);}
return dojo.lang.curry.apply(dojo.lang,[ns,func].concat(_176));};dojo.lang.tryThese=function(){for(var x=0;x<arguments.length;x++){try{if(typeof arguments[x]=="function"){var ret=(arguments[x]());if(ret){return ret;}}}
catch(e){dojo.debug(e);}}};dojo.lang.delayThese=function(farr,cb,_17c,_17d){if(!farr.length){if(typeof _17d=="function"){_17d();}
return;}
if((typeof _17c=="undefined")&&(typeof cb=="number")){_17c=cb;cb=function(){};}else{if(!cb){cb=function(){};if(!_17c){_17c=0;}}}
setTimeout(function(){(farr.shift())();cb();dojo.lang.delayThese(farr,cb,_17c,_17d);},_17c);};dojo.provide("dojo.event.common");dojo.event=new function(){this._canTimeout=dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);function interpolateArgs(args,_17f){var dl=dojo.lang;var ao={srcObj:dj_global,srcFunc:null,adviceObj:dj_global,adviceFunc:null,aroundObj:null,aroundFunc:null,adviceType:(args.length>2)?args[0]:"after",precedence:"last",once:false,delay:null,rate:0,adviceMsg:false};switch(args.length){case 0:
return;case 1:
return;case 2:
ao.srcFunc=args[0];ao.adviceFunc=args[1];break;case 3:
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isString(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];ao.adviceFunc=args[2];}else{if((dl.isString(args[1]))&&(dl.isString(args[2]))){ao.srcFunc=args[1];ao.adviceFunc=args[2];}else{if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isFunction(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];var _182=dl.nameAnonFunc(args[2],ao.adviceObj,_17f);ao.adviceFunc=_182;}else{if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){ao.adviceType="after";ao.srcObj=dj_global;var _182=dl.nameAnonFunc(args[0],ao.srcObj,_17f);ao.srcFunc=_182;ao.adviceObj=args[1];ao.adviceFunc=args[2];}}}}
break;case 4:
if((dl.isObject(args[0]))&&(dl.isObject(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isString(args[1]))&&(dl.isObject(args[2]))){ao.adviceType=args[0];ao.srcObj=dj_global;ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isFunction(args[1]))&&(dl.isObject(args[2]))){ao.adviceType=args[0];ao.srcObj=dj_global;var _182=dl.nameAnonFunc(args[1],dj_global,_17f);ao.srcFunc=_182;ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){ao.srcObj=args[1];ao.srcFunc=args[2];var _182=dl.nameAnonFunc(args[3],dj_global,_17f);ao.adviceObj=dj_global;ao.adviceFunc=_182;}else{if(dl.isObject(args[1])){ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=dj_global;ao.adviceFunc=args[3];}else{if(dl.isObject(args[2])){ao.srcObj=dj_global;ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{ao.srcObj=ao.adviceObj=ao.aroundObj=dj_global;ao.srcFunc=args[1];ao.adviceFunc=args[2];ao.aroundFunc=args[3];}}}}}}
break;case 6:
ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=args[3];ao.adviceFunc=args[4];ao.aroundFunc=args[5];ao.aroundObj=dj_global;break;default:
ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=args[3];ao.adviceFunc=args[4];ao.aroundObj=args[5];ao.aroundFunc=args[6];ao.once=args[7];ao.delay=args[8];ao.rate=args[9];ao.adviceMsg=args[10];break;}
if(dl.isFunction(ao.aroundFunc)){var _182=dl.nameAnonFunc(ao.aroundFunc,ao.aroundObj,_17f);ao.aroundFunc=_182;}
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
if(dojo.lang.isArray(ao.srcObj)&&ao.srcObj!=""){var _184={};for(var x in ao){_184[x]=ao[x];}
var mjps=[];dojo.lang.forEach(ao.srcObj,function(src){if((dojo.render.html.capable)&&(dojo.lang.isString(src))){src=dojo.byId(src);}
_184.srcObj=src;mjps.push(dojo.event.connect.call(dojo.event,_184));});return mjps;}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);if(ao.adviceFunc){var mjp2=dojo.event.MethodJoinPoint.getForMethod(ao.adviceObj,ao.adviceFunc);}
mjp.kwAddAdvice(ao);return mjp;};this.log=function(a1,a2){var _18c;if((arguments.length==1)&&(typeof a1=="object")){_18c=a1;}else{_18c={srcObj:a1,srcFunc:a2};}
_18c.adviceFunc=function(){var _18d=[];for(var x=0;x<arguments.length;x++){_18d.push(arguments[x]);}
dojo.debug("("+_18c.srcObj+")."+_18c.srcFunc,":",_18d.join(", "));};this.kwConnect(_18c);};this.connectBefore=function(){var args=["before"];for(var i=0;i<arguments.length;i++){args.push(arguments[i]);}
return this.connect.apply(this,args);};this.connectAround=function(){var args=["around"];for(var i=0;i<arguments.length;i++){args.push(arguments[i]);}
return this.connect.apply(this,args);};this.connectOnce=function(){var ao=interpolateArgs(arguments,true);ao.once=true;return this.connect(ao);};this._kwConnectImpl=function(_194,_195){var fn=(_195)?"disconnect":"connect";if(typeof _194["srcFunc"]=="function"){_194.srcObj=_194["srcObj"]||dj_global;var _197=dojo.lang.nameAnonFunc(_194.srcFunc,_194.srcObj,true);_194.srcFunc=_197;}
if(typeof _194["adviceFunc"]=="function"){_194.adviceObj=_194["adviceObj"]||dj_global;var _197=dojo.lang.nameAnonFunc(_194.adviceFunc,_194.adviceObj,true);_194.adviceFunc=_197;}
_194.srcObj=_194["srcObj"]||dj_global;_194.adviceObj=_194["adviceObj"]||_194["targetObj"]||dj_global;_194.adviceFunc=_194["adviceFunc"]||_194["targetFunc"];return dojo.event[fn](_194);};this.kwConnect=function(_198){return this._kwConnectImpl(_198,false);};this.disconnect=function(){if(arguments.length==1){var ao=arguments[0];}else{var ao=interpolateArgs(arguments,true);}
if(!ao.adviceFunc){return;}
if(dojo.lang.isString(ao.srcFunc)&&(ao.srcFunc.toLowerCase()=="onkey")){if(dojo.render.html.ie){ao.srcFunc="onkeydown";this.disconnect(ao);}
ao.srcFunc="onkeypress";}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);return mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);};this.kwDisconnect=function(_19b){return this._kwConnectImpl(_19b,true);};};dojo.event.MethodInvocation=function(_19c,obj,args){this.jp_=_19c;this.object=obj;this.args=[];for(var x=0;x<args.length;x++){this.args[x]=args[x];}
this.around_index=-1;};dojo.event.MethodInvocation.prototype.proceed=function(){this.around_index++;if(this.around_index>=this.jp_.around.length){return this.jp_.object[this.jp_.methodname].apply(this.jp_.object,this.args);}else{var ti=this.jp_.around[this.around_index];var mobj=ti[0]||dj_global;var meth=ti[1];return mobj[meth].call(mobj,this);}};dojo.event.MethodJoinPoint=function(obj,_1a4){this.object=obj||dj_global;this.methodname=_1a4;this.methodfunc=this.object[_1a4];this.squelch=false;};dojo.event.MethodJoinPoint.getForMethod=function(obj,_1a6){if(!obj){obj=dj_global;}
if(!obj[_1a6]){obj[_1a6]=function(){};if(!obj[_1a6]){dojo.raise("Cannot set do-nothing method on that object "+_1a6);}}else{if((!dojo.lang.isFunction(obj[_1a6]))&&(!dojo.lang.isAlien(obj[_1a6]))){return null;}}
var _1a7=_1a6+"$joinpoint";var _1a8=_1a6+"$joinpoint$method";var _1a9=obj[_1a7];if(!_1a9){var _1aa=false;if(dojo.event["browser"]){if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){_1aa=true;dojo.event.browser.addClobberNodeAttrs(obj,[_1a7,_1a8,_1a6]);}}
var _1ab=obj[_1a6].length;obj[_1a8]=obj[_1a6];_1a9=obj[_1a7]=new dojo.event.MethodJoinPoint(obj,_1a8);obj[_1a6]=function(){var args=[];if((_1aa)&&(!arguments.length)){var evt=null;try{if(obj.ownerDocument){evt=obj.ownerDocument.parentWindow.event;}else{if(obj.documentElement){evt=obj.documentElement.ownerDocument.parentWindow.event;}else{if(obj.event){evt=obj.event;}else{evt=window.event;}}}}
catch(e){evt=window.event;}
if(evt){args.push(dojo.event.browser.fixEvent(evt,this));}}else{for(var x=0;x<arguments.length;x++){if((x==0)&&(_1aa)&&(dojo.event.browser.isEvent(arguments[x]))){args.push(dojo.event.browser.fixEvent(arguments[x],this));}else{args.push(arguments[x]);}}}
return _1a9.run.apply(_1a9,args);};obj[_1a6].__preJoinArity=_1ab;}
return _1a9;};dojo.lang.extend(dojo.event.MethodJoinPoint,{unintercept:function(){this.object[this.methodname]=this.methodfunc;this.before=[];this.after=[];this.around=[];},disconnect:dojo.lang.forward("unintercept"),run:function(){var obj=this.object||dj_global;var args=arguments;var _1b1=[];for(var x=0;x<args.length;x++){_1b1[x]=args[x];}
var _1b3=function(marr){if(!marr){dojo.debug("Null argument to unrollAdvice()");return;}
var _1b5=marr[0]||dj_global;var _1b6=marr[1];if(!_1b5[_1b6]){dojo.raise("function \""+_1b6+"\" does not exist on \""+_1b5+"\"");}
var _1b7=marr[2]||dj_global;var _1b8=marr[3];var msg=marr[6];var _1ba;var to={args:[],jp_:this,object:obj,proceed:function(){return _1b5[_1b6].apply(_1b5,to.args);}};to.args=_1b1;var _1bc=parseInt(marr[4]);var _1bd=((!isNaN(_1bc))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));if(marr[5]){var rate=parseInt(marr[5]);var cur=new Date();var _1c0=false;if((marr["last"])&&((cur-marr.last)<=rate)){if(dojo.event._canTimeout){if(marr["delayTimer"]){clearTimeout(marr.delayTimer);}
var tod=parseInt(rate*2);var mcpy=dojo.lang.shallowCopy(marr);marr.delayTimer=setTimeout(function(){mcpy[5]=0;_1b3(mcpy);},tod);}
return;}else{marr.last=cur;}}
if(_1b8){_1b7[_1b8].call(_1b7,to);}else{if((_1bd)&&((dojo.render.html)||(dojo.render.svg))){dj_global["setTimeout"](function(){if(msg){_1b5[_1b6].call(_1b5,to);}else{_1b5[_1b6].apply(_1b5,args);}},_1bc);}else{if(msg){_1b5[_1b6].call(_1b5,to);}else{_1b5[_1b6].apply(_1b5,args);}}}};var _1c3=function(){if(this.squelch){try{return _1b3.apply(this,arguments);}
catch(e){dojo.debug(e);}}else{return _1b3.apply(this,arguments);}};if((this["before"])&&(this.before.length>0)){dojo.lang.forEach(this.before.concat(new Array()),_1c3);}
var _1c4;try{if((this["around"])&&(this.around.length>0)){var mi=new dojo.event.MethodInvocation(this,obj,args);_1c4=mi.proceed();}else{if(this.methodfunc){_1c4=this.object[this.methodname].apply(this.object,args);}}}
catch(e){if(!this.squelch){dojo.debug(e,"when calling",this.methodname,"on",this.object,"with arguments",args);dojo.raise(e);}}
if((this["after"])&&(this.after.length>0)){dojo.lang.forEach(this.after.concat(new Array()),_1c3);}
return (this.methodfunc)?_1c4:null;},getArr:function(kind){var type="after";if((typeof kind=="string")&&(kind.indexOf("before")!=-1)){type="before";}else{if(kind=="around"){type="around";}}
if(!this[type]){this[type]=[];}
return this[type];},kwAddAdvice:function(args){this.addAdvice(args["adviceObj"],args["adviceFunc"],args["aroundObj"],args["aroundFunc"],args["adviceType"],args["precedence"],args["once"],args["delay"],args["rate"],args["adviceMsg"]);},addAdvice:function(_1c9,_1ca,_1cb,_1cc,_1cd,_1ce,once,_1d0,rate,_1d2){var arr=this.getArr(_1cd);if(!arr){dojo.raise("bad this: "+this);}
var ao=[_1c9,_1ca,_1cb,_1cc,_1d0,rate,_1d2];if(once){if(this.hasAdvice(_1c9,_1ca,_1cd,arr)>=0){return;}}
if(_1ce=="first"){arr.unshift(ao);}else{arr.push(ao);}},hasAdvice:function(_1d5,_1d6,_1d7,arr){if(!arr){arr=this.getArr(_1d7);}
var ind=-1;for(var x=0;x<arr.length;x++){var aao=(typeof _1d6=="object")?(new String(_1d6)).toString():_1d6;var a1o=(typeof arr[x][1]=="object")?(new String(arr[x][1])).toString():arr[x][1];if((arr[x][0]==_1d5)&&(a1o==aao)){ind=x;}}
return ind;},removeAdvice:function(_1dd,_1de,_1df,once){var arr=this.getArr(_1df);var ind=this.hasAdvice(_1dd,_1de,_1df,arr);if(ind==-1){return false;}
while(ind!=-1){arr.splice(ind,1);if(once){break;}
ind=this.hasAdvice(_1dd,_1de,_1df,arr);}
return true;}});dojo.provide("dojo.event.browser");dojo._ie_clobber=new function(){this.clobberNodes=[];function nukeProp(node,prop){try{node[prop]=null;}
catch(e){}
try{delete node[prop];}
catch(e){}
try{node.removeAttribute(prop);}
catch(e){}}
this.clobber=function(_1e5){var na;var tna;if(_1e5){tna=_1e5.all||_1e5.getElementsByTagName("*");na=[_1e5];for(var x=0;x<tna.length;x++){if(tna[x]["__doClobber__"]){na.push(tna[x]);}}}else{try{window.onload=null;}
catch(e){}
na=(this.clobberNodes.length)?this.clobberNodes:document.all;}
tna=null;var _1e9={};for(var i=na.length-1;i>=0;i=i-1){var el=na[i];try{if(el&&el["__clobberAttrs__"]){for(var j=0;j<el.__clobberAttrs__.length;j++){nukeProp(el,el.__clobberAttrs__[j]);}
nukeProp(el,"__clobberAttrs__");nukeProp(el,"__doClobber__");}}
catch(e){}}
na=null;};};if(dojo.render.html.ie){dojo.addOnUnload(function(){dojo._ie_clobber.clobber();try{if((dojo["widget"])&&(dojo.widget["manager"])){dojo.widget.manager.destroyAll();}}
catch(e){}
try{window.onload=null;}
catch(e){}
try{window.onunload=null;}
catch(e){}
dojo._ie_clobber.clobberNodes=[];});}
dojo.event.browser=new function(){var _1ed=0;this.normalizedEventName=function(_1ee){switch(_1ee){case "CheckboxStateChange":
case "DOMAttrModified":
case "DOMMenuItemActive":
case "DOMMenuItemInactive":
case "DOMMouseScroll":
case "DOMNodeInserted":
case "DOMNodeRemoved":
case "RadioStateChange":
return _1ee;break;default:
return _1ee.toLowerCase();break;}};this.clean=function(node){if(dojo.render.html.ie){dojo._ie_clobber.clobber(node);}};this.addClobberNode=function(node){if(!dojo.render.html.ie){return;}
if(!node["__doClobber__"]){node.__doClobber__=true;dojo._ie_clobber.clobberNodes.push(node);node.__clobberAttrs__=[];}};this.addClobberNodeAttrs=function(node,_1f2){if(!dojo.render.html.ie){return;}
this.addClobberNode(node);for(var x=0;x<_1f2.length;x++){node.__clobberAttrs__.push(_1f2[x]);}};this.removeListener=function(node,_1f5,fp,_1f7){if(!_1f7){var _1f7=false;}
_1f5=dojo.event.browser.normalizedEventName(_1f5);if((_1f5=="onkey")||(_1f5=="key")){if(dojo.render.html.ie){this.removeListener(node,"onkeydown",fp,_1f7);}
_1f5="onkeypress";}
if(_1f5.substr(0,2)=="on"){_1f5=_1f5.substr(2);}
if(node.removeEventListener){node.removeEventListener(_1f5,fp,_1f7);}};this.addListener=function(node,_1f9,fp,_1fb,_1fc){if(!node){return;}
if(!_1fb){var _1fb=false;}
_1f9=dojo.event.browser.normalizedEventName(_1f9);if((_1f9=="onkey")||(_1f9=="key")){if(dojo.render.html.ie){this.addListener(node,"onkeydown",fp,_1fb,_1fc);}
_1f9="onkeypress";}
if(_1f9.substr(0,2)!="on"){_1f9="on"+_1f9;}
if(!_1fc){var _1fd=function(evt){if(!evt){evt=window.event;}
var ret=fp(dojo.event.browser.fixEvent(evt,this));if(_1fb){dojo.event.browser.stopEvent(evt);}
return ret;};}else{_1fd=fp;}
if(node.addEventListener){node.addEventListener(_1f9.substr(2),_1fd,_1fb);return _1fd;}else{if(typeof node[_1f9]=="function"){var _200=node[_1f9];node[_1f9]=function(e){_200(e);return _1fd(e);};}else{node[_1f9]=_1fd;}
if(dojo.render.html.ie){this.addClobberNodeAttrs(node,[_1f9]);}
return _1fd;}};this.isEvent=function(obj){return (typeof obj!="undefined")&&(typeof Event!="undefined")&&(obj.eventPhase);};this.currentEvent=null;this.callListener=function(_203,_204){if(typeof _203!="function"){dojo.raise("listener not a function: "+_203);}
dojo.event.browser.currentEvent.currentTarget=_204;return _203.call(_204,dojo.event.browser.currentEvent);};this._stopPropagation=function(){dojo.event.browser.currentEvent.cancelBubble=true;};this._preventDefault=function(){dojo.event.browser.currentEvent.returnValue=false;};this.keys={KEY_BACKSPACE:8,KEY_TAB:9,KEY_CLEAR:12,KEY_ENTER:13,KEY_SHIFT:16,KEY_CTRL:17,KEY_ALT:18,KEY_PAUSE:19,KEY_CAPS_LOCK:20,KEY_ESCAPE:27,KEY_SPACE:32,KEY_PAGE_UP:33,KEY_PAGE_DOWN:34,KEY_END:35,KEY_HOME:36,KEY_LEFT_ARROW:37,KEY_UP_ARROW:38,KEY_RIGHT_ARROW:39,KEY_DOWN_ARROW:40,KEY_INSERT:45,KEY_DELETE:46,KEY_HELP:47,KEY_LEFT_WINDOW:91,KEY_RIGHT_WINDOW:92,KEY_SELECT:93,KEY_NUMPAD_0:96,KEY_NUMPAD_1:97,KEY_NUMPAD_2:98,KEY_NUMPAD_3:99,KEY_NUMPAD_4:100,KEY_NUMPAD_5:101,KEY_NUMPAD_6:102,KEY_NUMPAD_7:103,KEY_NUMPAD_8:104,KEY_NUMPAD_9:105,KEY_NUMPAD_MULTIPLY:106,KEY_NUMPAD_PLUS:107,KEY_NUMPAD_ENTER:108,KEY_NUMPAD_MINUS:109,KEY_NUMPAD_PERIOD:110,KEY_NUMPAD_DIVIDE:111,KEY_F1:112,KEY_F2:113,KEY_F3:114,KEY_F4:115,KEY_F5:116,KEY_F6:117,KEY_F7:118,KEY_F8:119,KEY_F9:120,KEY_F10:121,KEY_F11:122,KEY_F12:123,KEY_F13:124,KEY_F14:125,KEY_F15:126,KEY_NUM_LOCK:144,KEY_SCROLL_LOCK:145};this.revKeys=[];for(var key in this.keys){this.revKeys[this.keys[key]]=key;}
this.fixEvent=function(evt,_207){if(!evt){if(window["event"]){evt=window.event;}}
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
if(evt.ctrlKey||evt.altKey){var _209=evt.keyCode;if(_209>=65&&_209<=90&&evt.shiftKey==false){_209+=32;}
if(_209>=1&&_209<=26&&evt.ctrlKey){_209+=96;}
evt.key=String.fromCharCode(_209);}}}else{if(evt["type"]=="keypress"){if(dojo.render.html.opera){if(evt.which==0){evt.key=evt.keyCode;}else{if(evt.which>0){switch(evt.which){case evt.KEY_SHIFT:
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
var _209=evt.which;if((evt.ctrlKey||evt.altKey||evt.metaKey)&&(evt.which>=65&&evt.which<=90&&evt.shiftKey==false)){_209+=32;}
evt.key=String.fromCharCode(_209);}}}}else{if(dojo.render.html.ie){if(!evt.ctrlKey&&!evt.altKey&&evt.keyCode>=evt.KEY_SPACE){evt.key=String.fromCharCode(evt.keyCode);}}else{if(dojo.render.html.safari){switch(evt.keyCode){case 63232:
evt.key=evt.KEY_UP_ARROW;break;case 63233:
evt.key=evt.KEY_DOWN_ARROW;break;case 63234:
evt.key=evt.KEY_LEFT_ARROW;break;case 63235:
evt.key=evt.KEY_RIGHT_ARROW;break;default:
evt.key=evt.charCode>0?String.fromCharCode(evt.charCode):evt.keyCode;}}else{evt.key=evt.charCode>0?String.fromCharCode(evt.charCode):evt.keyCode;}}}}}}
if(dojo.render.html.ie){if(!evt.target){evt.target=evt.srcElement;}
if(!evt.currentTarget){evt.currentTarget=(_207?_207:evt.srcElement);}
if(!evt.layerX){evt.layerX=evt.offsetX;}
if(!evt.layerY){evt.layerY=evt.offsetY;}
var doc=(evt.srcElement&&evt.srcElement.ownerDocument)?evt.srcElement.ownerDocument:document;var _20b=((dojo.render.html.ie55)||(doc["compatMode"]=="BackCompat"))?doc.body:doc.documentElement;if(!evt.pageX){evt.pageX=evt.clientX+(_20b.scrollLeft||0);}
if(!evt.pageY){evt.pageY=evt.clientY+(_20b.scrollTop||0);}
if(evt.type=="mouseover"){evt.relatedTarget=evt.fromElement;}
if(evt.type=="mouseout"){evt.relatedTarget=evt.toElement;}
this.currentEvent=evt;evt.callListener=this.callListener;evt.stopPropagation=this._stopPropagation;evt.preventDefault=this._preventDefault;}
return evt;};this.stopEvent=function(evt){if(window.event){evt.cancelBubble=true;evt.returnValue=false;}else{evt.preventDefault();evt.stopPropagation();}};};dojo.provide("dojo.dom");dojo.dom.ELEMENT_NODE=1;dojo.dom.ATTRIBUTE_NODE=2;dojo.dom.TEXT_NODE=3;dojo.dom.CDATA_SECTION_NODE=4;dojo.dom.ENTITY_REFERENCE_NODE=5;dojo.dom.ENTITY_NODE=6;dojo.dom.PROCESSING_INSTRUCTION_NODE=7;dojo.dom.COMMENT_NODE=8;dojo.dom.DOCUMENT_NODE=9;dojo.dom.DOCUMENT_TYPE_NODE=10;dojo.dom.DOCUMENT_FRAGMENT_NODE=11;dojo.dom.NOTATION_NODE=12;dojo.dom.dojoml="http://www.dojotoolkit.org/2004/dojoml";dojo.dom.xmlns={svg:"http://www.w3.org/2000/svg",smil:"http://www.w3.org/2001/SMIL20/",mml:"http://www.w3.org/1998/Math/MathML",cml:"http://www.xml-cml.org",xlink:"http://www.w3.org/1999/xlink",xhtml:"http://www.w3.org/1999/xhtml",xul:"http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul",xbl:"http://www.mozilla.org/xbl",fo:"http://www.w3.org/1999/XSL/Format",xsl:"http://www.w3.org/1999/XSL/Transform",xslt:"http://www.w3.org/1999/XSL/Transform",xi:"http://www.w3.org/2001/XInclude",xforms:"http://www.w3.org/2002/01/xforms",saxon:"http://icl.com/saxon",xalan:"http://xml.apache.org/xslt",xsd:"http://www.w3.org/2001/XMLSchema",dt:"http://www.w3.org/2001/XMLSchema-datatypes",xsi:"http://www.w3.org/2001/XMLSchema-instance",rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#",rdfs:"http://www.w3.org/2000/01/rdf-schema#",dc:"http://purl.org/dc/elements/1.1/",dcq:"http://purl.org/dc/qualifiers/1.0","soap-env":"http://schemas.xmlsoap.org/soap/envelope/",wsdl:"http://schemas.xmlsoap.org/wsdl/",AdobeExtensions:"http://ns.adobe.com/AdobeSVGViewerExtensions/3.0/"};dojo.dom.isNode=function(wh){if(typeof Element=="function"){try{return wh instanceof Element;}
catch(E){}}else{return wh&&!isNaN(wh.nodeType);}};dojo.dom.getUniqueId=function(){var _20e=dojo.doc();do{var id="dj_unique_"+(++arguments.callee._idIncrement);}while(_20e.getElementById(id));return id;};dojo.dom.getUniqueId._idIncrement=0;dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_210,_211){var node=_210.firstChild;while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){node=node.nextSibling;}
if(_211&&node&&node.tagName&&node.tagName.toLowerCase()!=_211.toLowerCase()){node=dojo.dom.nextElement(node,_211);}
return node;};dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_213,_214){var node=_213.lastChild;while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){node=node.previousSibling;}
if(_214&&node&&node.tagName&&node.tagName.toLowerCase()!=_214.toLowerCase()){node=dojo.dom.prevElement(node,_214);}
return node;};dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(node,_217){if(!node){return null;}
do{node=node.nextSibling;}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);if(node&&_217&&_217.toLowerCase()!=node.tagName.toLowerCase()){return dojo.dom.nextElement(node,_217);}
return node;};dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(node,_219){if(!node){return null;}
if(_219){_219=_219.toLowerCase();}
do{node=node.previousSibling;}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);if(node&&_219&&_219.toLowerCase()!=node.tagName.toLowerCase()){return dojo.dom.prevElement(node,_219);}
return node;};dojo.dom.moveChildren=function(_21a,_21b,trim){var _21d=0;if(trim){while(_21a.hasChildNodes()&&_21a.firstChild.nodeType==dojo.dom.TEXT_NODE){_21a.removeChild(_21a.firstChild);}
while(_21a.hasChildNodes()&&_21a.lastChild.nodeType==dojo.dom.TEXT_NODE){_21a.removeChild(_21a.lastChild);}}
while(_21a.hasChildNodes()){_21b.appendChild(_21a.firstChild);_21d++;}
return _21d;};dojo.dom.copyChildren=function(_21e,_21f,trim){var _221=_21e.cloneNode(true);return this.moveChildren(_221,_21f,trim);};dojo.dom.removeChildren=function(node){var _223=node.childNodes.length;while(node.hasChildNodes()){node.removeChild(node.firstChild);}
return _223;};dojo.dom.replaceChildren=function(node,_225){dojo.dom.removeChildren(node);node.appendChild(_225);};dojo.dom.removeNode=function(node){if(node&&node.parentNode){return node.parentNode.removeChild(node);}};dojo.dom.getAncestors=function(node,_228,_229){var _22a=[];var _22b=(_228&&(_228 instanceof Function||typeof _228=="function"));while(node){if(!_22b||_228(node)){_22a.push(node);}
if(_229&&_22a.length>0){return _22a[0];}
node=node.parentNode;}
if(_229){return null;}
return _22a;};dojo.dom.getAncestorsByTag=function(node,tag,_22e){tag=tag.toLowerCase();return dojo.dom.getAncestors(node,function(el){return ((el.tagName)&&(el.tagName.toLowerCase()==tag));},_22e);};dojo.dom.getFirstAncestorByTag=function(node,tag){return dojo.dom.getAncestorsByTag(node,tag,true);};dojo.dom.isDescendantOf=function(node,_233,_234){if(_234&&node){node=node.parentNode;}
while(node){if(node==_233){return true;}
node=node.parentNode;}
return false;};dojo.dom.innerXML=function(node){if(node.innerXML){return node.innerXML;}else{if(node.xml){return node.xml;}else{if(typeof XMLSerializer!="undefined"){return (new XMLSerializer()).serializeToString(node);}}}};dojo.dom.createDocument=function(){var doc=null;var _237=dojo.doc();if(!dj_undef("ActiveXObject")){var _238=["MSXML2","Microsoft","MSXML","MSXML3"];for(var i=0;i<_238.length;i++){try{doc=new ActiveXObject(_238[i]+".XMLDOM");}
catch(e){}
if(doc){break;}}}else{if((_237.implementation)&&(_237.implementation.createDocument)){doc=_237.implementation.createDocument("","",null);}}
return doc;};dojo.dom.createDocumentFromText=function(str,_23b){if(!_23b){_23b="text/xml";}
if(!dj_undef("DOMParser")){var _23c=new DOMParser();return _23c.parseFromString(str,_23b);}else{if(!dj_undef("ActiveXObject")){var _23d=dojo.dom.createDocument();if(_23d){_23d.async=false;_23d.loadXML(str);return _23d;}else{dojo.debug("toXml didn't work?");}}else{var _23e=dojo.doc();if(_23e.createElement){var tmp=_23e.createElement("xml");tmp.innerHTML=str;if(_23e.implementation&&_23e.implementation.createDocument){var _240=_23e.implementation.createDocument("foo","",null);for(var i=0;i<tmp.childNodes.length;i++){_240.importNode(tmp.childNodes.item(i),true);}
return _240;}
return ((tmp.document)&&(tmp.document.firstChild?tmp.document.firstChild:tmp));}}}
return null;};dojo.dom.prependChild=function(node,_243){if(_243.firstChild){_243.insertBefore(node,_243.firstChild);}else{_243.appendChild(node);}
return true;};dojo.dom.insertBefore=function(node,ref,_246){if(_246!=true&&(node===ref||node.nextSibling===ref)){return false;}
var _247=ref.parentNode;_247.insertBefore(node,ref);return true;};dojo.dom.insertAfter=function(node,ref,_24a){var pn=ref.parentNode;if(ref==pn.lastChild){if((_24a!=true)&&(node===ref)){return false;}
pn.appendChild(node);}else{return this.insertBefore(node,ref.nextSibling,_24a);}
return true;};dojo.dom.insertAtPosition=function(node,ref,_24e){if((!node)||(!ref)||(!_24e)){return false;}
switch(_24e.toLowerCase()){case "before":
return dojo.dom.insertBefore(node,ref);case "after":
return dojo.dom.insertAfter(node,ref);case "first":
if(ref.firstChild){return dojo.dom.insertBefore(node,ref.firstChild);}else{ref.appendChild(node);return true;}
break;default:
ref.appendChild(node);return true;}};dojo.dom.insertAtIndex=function(node,_250,_251){var _252=_250.childNodes;if(!_252.length){_250.appendChild(node);return true;}
var _253=null;for(var i=0;i<_252.length;i++){var _255=_252.item(i)["getAttribute"]?parseInt(_252.item(i).getAttribute("dojoinsertionindex")):-1;if(_255<_251){_253=_252.item(i);}}
if(_253){return dojo.dom.insertAfter(node,_253);}else{return dojo.dom.insertBefore(node,_252.item(0));}};dojo.dom.textContent=function(node,text){if(arguments.length>1){var _258=dojo.doc();dojo.dom.replaceChildren(node,_258.createTextNode(text));return text;}else{if(node.textContent!=undefined){return node.textContent;}
var _259="";if(node==null){return _259;}
for(var i=0;i<node.childNodes.length;i++){switch(node.childNodes[i].nodeType){case 1:
case 5:
_259+=dojo.dom.textContent(node.childNodes[i]);break;case 3:
case 2:
case 4:
_259+=node.childNodes[i].nodeValue;break;default:
break;}}
return _259;}};dojo.dom.hasParent=function(node){return node&&node.parentNode&&dojo.dom.isNode(node.parentNode);};dojo.dom.isTag=function(node){if(node&&node.tagName){for(var i=1;i<arguments.length;i++){if(node.tagName==String(arguments[i])){return String(arguments[i]);}}}
return "";};dojo.dom.setAttributeNS=function(elem,_25f,_260,_261){if(elem==null||((elem==undefined)&&(typeof elem=="undefined"))){dojo.raise("No element given to dojo.dom.setAttributeNS");}
if(!((elem.setAttributeNS==undefined)&&(typeof elem.setAttributeNS=="undefined"))){elem.setAttributeNS(_25f,_260,_261);}else{var _262=elem.ownerDocument;var _263=_262.createNode(2,_260,_25f);_263.nodeValue=_261;elem.setAttributeNode(_263);}};dojo.provide("dojo.string.common");dojo.string.trim=function(str,wh){if(!str.replace){return str;}
if(!str.length){return str;}
var re=(wh>0)?(/^\s+/):(wh<0)?(/\s+$/):(/^\s+|\s+$/g);return str.replace(re,"");};dojo.string.trimStart=function(str){return dojo.string.trim(str,1);};dojo.string.trimEnd=function(str){return dojo.string.trim(str,-1);};dojo.string.repeat=function(str,_26a,_26b){var out="";for(var i=0;i<_26a;i++){out+=str;if(_26b&&i<_26a-1){out+=_26b;}}
return out;};dojo.string.pad=function(str,len,c,dir){var out=String(str);if(!c){c="0";}
if(!dir){dir=1;}
while(out.length<len){if(dir>0){out=c+out;}else{out+=c;}}
return out;};dojo.string.padLeft=function(str,len,c){return dojo.string.pad(str,len,c,1);};dojo.string.padRight=function(str,len,c){return dojo.string.pad(str,len,c,-1);};dojo.provide("dojo.string");dojo.provide("dojo.io.common");dojo.io.transports=[];dojo.io.hdlrFuncNames=["load","error","timeout"];dojo.io.Request=function(url,_27a,_27b,_27c){if((arguments.length==1)&&(arguments[0].constructor==Object)){this.fromKwArgs(arguments[0]);}else{this.url=url;if(_27a){this.mimetype=_27a;}
if(_27b){this.transport=_27b;}
if(arguments.length>=4){this.changeUrl=_27c;}}};dojo.lang.extend(dojo.io.Request,{url:"",mimetype:"text/plain",method:"GET",content:undefined,transport:undefined,changeUrl:undefined,formNode:undefined,sync:false,bindSuccess:false,useCache:false,preventCache:false,load:function(type,data,_27f,_280){},error:function(type,_282,_283,_284){},timeout:function(type,_286,_287,_288){},handle:function(type,data,_28b,_28c){},timeoutSeconds:0,abort:function(){},fromKwArgs:function(_28d){if(_28d["url"]){_28d.url=_28d.url.toString();}
if(_28d["formNode"]){_28d.formNode=dojo.byId(_28d.formNode);}
if(!_28d["method"]&&_28d["formNode"]&&_28d["formNode"].method){_28d.method=_28d["formNode"].method;}
if(!_28d["handle"]&&_28d["handler"]){_28d.handle=_28d.handler;}
if(!_28d["load"]&&_28d["loaded"]){_28d.load=_28d.loaded;}
if(!_28d["changeUrl"]&&_28d["changeURL"]){_28d.changeUrl=_28d.changeURL;}
_28d.encoding=dojo.lang.firstValued(_28d["encoding"],djConfig["bindEncoding"],"");_28d.sendTransport=dojo.lang.firstValued(_28d["sendTransport"],djConfig["ioSendTransport"],false);var _28e=dojo.lang.isFunction;for(var x=0;x<dojo.io.hdlrFuncNames.length;x++){var fn=dojo.io.hdlrFuncNames[x];if(_28d[fn]&&_28e(_28d[fn])){continue;}
if(_28d["handle"]&&_28e(_28d["handle"])){_28d[fn]=_28d.handle;}}
dojo.lang.mixin(this,_28d);}});dojo.io.Error=function(msg,type,num){this.message=msg;this.type=type||"unknown";this.number=num||0;};dojo.io.transports.addTransport=function(name){this.push(name);this[name]=dojo.io[name];};dojo.io.bind=function(_295){if(!(_295 instanceof dojo.io.Request)){try{_295=new dojo.io.Request(_295);}
catch(e){dojo.debug(e);}}
var _296="";if(_295["transport"]){_296=_295["transport"];if(!this[_296]){dojo.io.sendBindError(_295,"No dojo.io.bind() transport with name '"+_295["transport"]+"'.");return _295;}
if(!this[_296].canHandle(_295)){dojo.io.sendBindError(_295,"dojo.io.bind() transport with name '"+_295["transport"]+"' cannot handle this type of request.");return _295;}}else{for(var x=0;x<dojo.io.transports.length;x++){var tmp=dojo.io.transports[x];if((this[tmp])&&(this[tmp].canHandle(_295))){_296=tmp;break;}}
if(_296==""){dojo.io.sendBindError(_295,"None of the loaded transports for dojo.io.bind()"+" can handle the request.");return _295;}}
this[_296].bind(_295);_295.bindSuccess=true;return _295;};dojo.io.sendBindError=function(_299,_29a){if((typeof _299.error=="function"||typeof _299.handle=="function")&&(typeof setTimeout=="function"||typeof setTimeout=="object")){var _29b=new dojo.io.Error(_29a);setTimeout(function(){_299[(typeof _299.error=="function")?"error":"handle"]("error",_29b,null,_299);},50);}else{dojo.raise(_29a);}};dojo.io.queueBind=function(_29c){if(!(_29c instanceof dojo.io.Request)){try{_29c=new dojo.io.Request(_29c);}
catch(e){dojo.debug(e);}}
var _29d=_29c.load;_29c.load=function(){dojo.io._queueBindInFlight=false;var ret=_29d.apply(this,arguments);dojo.io._dispatchNextQueueBind();return ret;};var _29f=_29c.error;_29c.error=function(){dojo.io._queueBindInFlight=false;var ret=_29f.apply(this,arguments);dojo.io._dispatchNextQueueBind();return ret;};dojo.io._bindQueue.push(_29c);dojo.io._dispatchNextQueueBind();return _29c;};dojo.io._dispatchNextQueueBind=function(){if(!dojo.io._queueBindInFlight){dojo.io._queueBindInFlight=true;if(dojo.io._bindQueue.length>0){dojo.io.bind(dojo.io._bindQueue.shift());}else{dojo.io._queueBindInFlight=false;}}};dojo.io._bindQueue=[];dojo.io._queueBindInFlight=false;dojo.io.argsFromMap=function(map,_2a2,last){var enc=/utf/i.test(_2a2||"")?encodeURIComponent:dojo.string.encodeAscii;var _2a5=[];var _2a6=new Object();for(var name in map){var _2a8=function(elt){var val=enc(name)+"="+enc(elt);_2a5[(last==name)?"push":"unshift"](val);};if(!_2a6[name]){var _2ab=map[name];if(dojo.lang.isArray(_2ab)){dojo.lang.forEach(_2ab,_2a8);}else{_2a8(_2ab);}}}
return _2a5.join("&");};dojo.io.setIFrameSrc=function(_2ac,src,_2ae){try{var r=dojo.render.html;if(!_2ae){if(r.safari){_2ac.location=src;}else{frames[_2ac.name].location=src;}}else{var idoc;if(r.ie){idoc=_2ac.contentWindow.document;}else{if(r.safari){idoc=_2ac.document;}else{idoc=_2ac.contentWindow;}}
if(!idoc){_2ac.location=src;return;}else{idoc.location.replace(src);}}}
catch(e){dojo.debug(e);dojo.debug("setIFrameSrc: "+e);}};dojo.provide("dojo.string.extras");dojo.string.substituteParams=function(_2b1,hash){var map=(typeof hash=="object")?hash:dojo.lang.toArray(arguments,1);return _2b1.replace(/\%\{(\w+)\}/g,function(_2b4,key){if(typeof (map[key])!="undefined"&&map[key]!=null){return map[key];}
dojo.raise("Substitution not found: "+key);});};dojo.string.capitalize=function(str){if(!dojo.lang.isString(str)){return "";}
if(arguments.length==0){str=this;}
var _2b7=str.split(" ");for(var i=0;i<_2b7.length;i++){_2b7[i]=_2b7[i].charAt(0).toUpperCase()+_2b7[i].substring(1);}
return _2b7.join(" ");};dojo.string.isBlank=function(str){if(!dojo.lang.isString(str)){return true;}
return (dojo.string.trim(str).length==0);};dojo.string.encodeAscii=function(str){if(!dojo.lang.isString(str)){return str;}
var ret="";var _2bc=escape(str);var _2bd,re=/%u([0-9A-F]{4})/i;while((_2bd=_2bc.match(re))){var num=Number("0x"+_2bd[1]);var _2c0=escape("&#"+num+";");ret+=_2bc.substring(0,_2bd.index)+_2c0;_2bc=_2bc.substring(_2bd.index+_2bd[0].length);}
ret+=_2bc.replace(/\+/g,"%2B");return ret;};dojo.string.escape=function(type,str){var args=dojo.lang.toArray(arguments,1);switch(type.toLowerCase()){case "xml":
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
return str;}};dojo.string.escapeXml=function(str,_2c5){str=str.replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");if(!_2c5){str=str.replace(/'/gm,"&#39;");}
return str;};dojo.string.escapeSql=function(str){return str.replace(/'/gm,"''");};dojo.string.escapeRegExp=function(str){return str.replace(/\\/gm,"\\\\").replace(/([\f\b\n\t\r[\^$|?*+(){}])/gm,"\\$1");};dojo.string.escapeJavaScript=function(str){return str.replace(/(["'\f\b\n\t\r])/gm,"\\$1");};dojo.string.escapeString=function(str){return ("\""+str.replace(/(["\\])/g,"\\$1")+"\"").replace(/[\f]/g,"\\f").replace(/[\b]/g,"\\b").replace(/[\n]/g,"\\n").replace(/[\t]/g,"\\t").replace(/[\r]/g,"\\r");};dojo.string.summary=function(str,len){if(!len||str.length<=len){return str;}
return str.substring(0,len).replace(/\.+$/,"")+"...";};dojo.string.endsWith=function(str,end,_2ce){if(_2ce){str=str.toLowerCase();end=end.toLowerCase();}
if((str.length-end.length)<0){return false;}
return str.lastIndexOf(end)==str.length-end.length;};dojo.string.endsWithAny=function(str){for(var i=1;i<arguments.length;i++){if(dojo.string.endsWith(str,arguments[i])){return true;}}
return false;};dojo.string.startsWith=function(str,_2d2,_2d3){if(_2d3){str=str.toLowerCase();_2d2=_2d2.toLowerCase();}
return str.indexOf(_2d2)==0;};dojo.string.startsWithAny=function(str){for(var i=1;i<arguments.length;i++){if(dojo.string.startsWith(str,arguments[i])){return true;}}
return false;};dojo.string.has=function(str){for(var i=1;i<arguments.length;i++){if(str.indexOf(arguments[i])>-1){return true;}}
return false;};dojo.string.normalizeNewlines=function(text,_2d9){if(_2d9=="\n"){text=text.replace(/\r\n/g,"\n");text=text.replace(/\r/g,"\n");}else{if(_2d9=="\r"){text=text.replace(/\r\n/g,"\r");text=text.replace(/\n/g,"\r");}else{text=text.replace(/([^\r])\n/g,"$1\r\n").replace(/\r([^\n])/g,"\r\n$1");}}
return text;};dojo.string.splitEscaped=function(str,_2db){var _2dc=[];for(var i=0,_2de=0;i<str.length;i++){if(str.charAt(i)=="\\"){i++;continue;}
if(str.charAt(i)==_2db){_2dc.push(str.substring(_2de,i));_2de=i+1;}}
_2dc.push(str.substr(_2de));return _2dc;};dojo.provide("dojo.undo.browser");try{if((!djConfig["preventBackButtonFix"])&&(!dojo.hostenv.post_load_)){document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"'></iframe>");}}
catch(e){}
if(dojo.render.html.opera){dojo.debug("Opera is not supported with dojo.undo.browser, so back/forward detection will not work.");}
dojo.undo.browser={initialHref:(!dj_undef("window"))?window.location.href:"",initialHash:(!dj_undef("window"))?window.location.hash:"",moveForward:false,historyStack:[],forwardStack:[],historyIframe:null,bookmarkAnchor:null,locationTimer:null,setInitialState:function(args){this.initialState=this._createState(this.initialHref,args,this.initialHash);},addToHistory:function(args){this.forwardStack=[];var hash=null;var url=null;if(!this.historyIframe){this.historyIframe=window.frames["djhistory"];}
if(!this.bookmarkAnchor){this.bookmarkAnchor=document.createElement("a");dojo.body().appendChild(this.bookmarkAnchor);this.bookmarkAnchor.style.display="none";}
if(args["changeUrl"]){hash="#"+((args["changeUrl"]!==true)?args["changeUrl"]:(new Date()).getTime());if(this.historyStack.length==0&&this.initialState.urlHash==hash){this.initialState=this._createState(url,args,hash);return;}else{if(this.historyStack.length>0&&this.historyStack[this.historyStack.length-1].urlHash==hash){this.historyStack[this.historyStack.length-1]=this._createState(url,args,hash);return;}}
this.changingUrl=true;setTimeout("window.location.href = '"+hash+"'; dojo.undo.browser.changingUrl = false;",1);this.bookmarkAnchor.href=hash;if(dojo.render.html.ie){url=this._loadIframeHistory();var _2e3=args["back"]||args["backButton"]||args["handle"];var tcb=function(_2e5){if(window.location.hash!=""){setTimeout("window.location.href = '"+hash+"';",1);}
_2e3.apply(this,[_2e5]);};if(args["back"]){args.back=tcb;}else{if(args["backButton"]){args.backButton=tcb;}else{if(args["handle"]){args.handle=tcb;}}}
var _2e6=args["forward"]||args["forwardButton"]||args["handle"];var tfw=function(_2e8){if(window.location.hash!=""){window.location.href=hash;}
if(_2e6){_2e6.apply(this,[_2e8]);}};if(args["forward"]){args.forward=tfw;}else{if(args["forwardButton"]){args.forwardButton=tfw;}else{if(args["handle"]){args.handle=tfw;}}}}else{if(dojo.render.html.moz){if(!this.locationTimer){this.locationTimer=setInterval("dojo.undo.browser.checkLocation();",200);}}}}else{url=this._loadIframeHistory();}
this.historyStack.push(this._createState(url,args,hash));},checkLocation:function(){if(!this.changingUrl){var hsl=this.historyStack.length;if((window.location.hash==this.initialHash||window.location.href==this.initialHref)&&(hsl==1)){this.handleBackButton();return;}
if(this.forwardStack.length>0){if(this.forwardStack[this.forwardStack.length-1].urlHash==window.location.hash){this.handleForwardButton();return;}}
if((hsl>=2)&&(this.historyStack[hsl-2])){if(this.historyStack[hsl-2].urlHash==window.location.hash){this.handleBackButton();return;}}}},iframeLoaded:function(evt,_2eb){if(!dojo.render.html.opera){var _2ec=this._getUrlQuery(_2eb.href);if(_2ec==null){if(this.historyStack.length==1){this.handleBackButton();}
return;}
if(this.moveForward){this.moveForward=false;return;}
if(this.historyStack.length>=2&&_2ec==this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){this.handleBackButton();}else{if(this.forwardStack.length>0&&_2ec==this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){this.handleForwardButton();}}}},handleBackButton:function(){var _2ed=this.historyStack.pop();if(!_2ed){return;}
var last=this.historyStack[this.historyStack.length-1];if(!last&&this.historyStack.length==0){last=this.initialState;}
if(last){if(last.kwArgs["back"]){last.kwArgs["back"]();}else{if(last.kwArgs["backButton"]){last.kwArgs["backButton"]();}else{if(last.kwArgs["handle"]){last.kwArgs.handle("back");}}}}
this.forwardStack.push(_2ed);},handleForwardButton:function(){var last=this.forwardStack.pop();if(!last){return;}
if(last.kwArgs["forward"]){last.kwArgs.forward();}else{if(last.kwArgs["forwardButton"]){last.kwArgs.forwardButton();}else{if(last.kwArgs["handle"]){last.kwArgs.handle("forward");}}}
this.historyStack.push(last);},_createState:function(url,args,hash){return {"url":url,"kwArgs":args,"urlHash":hash};},_getUrlQuery:function(url){var _2f4=url.split("?");if(_2f4.length<2){return null;}else{return _2f4[1];}},_loadIframeHistory:function(){var url=dojo.hostenv.getBaseScriptUri()+"iframe_history.html?"+(new Date()).getTime();this.moveForward=true;dojo.io.setIFrameSrc(this.historyIframe,url,false);return url;}};dojo.provide("dojo.io.BrowserIO");if(!dj_undef("window")){dojo.io.checkChildrenForFile=function(node){var _2f7=false;var _2f8=node.getElementsByTagName("input");dojo.lang.forEach(_2f8,function(_2f9){if(_2f7){return;}
if(_2f9.getAttribute("type")=="file"){_2f7=true;}});return _2f7;};dojo.io.formHasFile=function(_2fa){return dojo.io.checkChildrenForFile(_2fa);};dojo.io.updateNode=function(node,_2fc){node=dojo.byId(node);var args=_2fc;if(dojo.lang.isString(_2fc)){args={url:_2fc};}
args.mimetype="text/html";args.load=function(t,d,e){while(node.firstChild){if(dojo["event"]){try{dojo.event.browser.clean(node.firstChild);}
catch(e){}}
node.removeChild(node.firstChild);}
node.innerHTML=d;};dojo.io.bind(args);};dojo.io.formFilter=function(node){var type=(node.type||"").toLowerCase();return !node.disabled&&node.name&&!dojo.lang.inArray(["file","submit","image","reset","button"],type);};dojo.io.encodeForm=function(_303,_304,_305){if((!_303)||(!_303.tagName)||(!_303.tagName.toLowerCase()=="form")){dojo.raise("Attempted to encode a non-form element.");}
if(!_305){_305=dojo.io.formFilter;}
var enc=/utf/i.test(_304||"")?encodeURIComponent:dojo.string.encodeAscii;var _307=[];for(var i=0;i<_303.elements.length;i++){var elm=_303.elements[i];if(!elm||elm.tagName.toLowerCase()=="fieldset"||!_305(elm)){continue;}
var name=enc(elm.name);var type=elm.type.toLowerCase();if(type=="select-multiple"){for(var j=0;j<elm.options.length;j++){if(elm.options[j].selected){_307.push(name+"="+enc(elm.options[j].value));}}}else{if(dojo.lang.inArray(["radio","checkbox"],type)){if(elm.checked){_307.push(name+"="+enc(elm.value));}}else{_307.push(name+"="+enc(elm.value));}}}
var _30d=_303.getElementsByTagName("input");for(var i=0;i<_30d.length;i++){var _30e=_30d[i];if(_30e.type.toLowerCase()=="image"&&_30e.form==_303&&_305(_30e)){var name=enc(_30e.name);_307.push(name+"="+enc(_30e.value));_307.push(name+".x=0");_307.push(name+".y=0");}}
return _307.join("&")+"&";};dojo.io.FormBind=function(args){this.bindArgs={};if(args&&args.formNode){this.init(args);}else{if(args){this.init({formNode:args});}}};dojo.lang.extend(dojo.io.FormBind,{form:null,bindArgs:null,clickedButton:null,init:function(args){var form=dojo.byId(args.formNode);if(!form||!form.tagName||form.tagName.toLowerCase()!="form"){throw new Error("FormBind: Couldn't apply, invalid form");}else{if(this.form==form){return;}else{if(this.form){throw new Error("FormBind: Already applied to a form");}}}
dojo.lang.mixin(this.bindArgs,args);this.form=form;this.connect(form,"onsubmit","submit");for(var i=0;i<form.elements.length;i++){var node=form.elements[i];if(node&&node.type&&dojo.lang.inArray(["submit","button"],node.type.toLowerCase())){this.connect(node,"onclick","click");}}
var _314=form.getElementsByTagName("input");for(var i=0;i<_314.length;i++){var _315=_314[i];if(_315.type.toLowerCase()=="image"&&_315.form==form){this.connect(_315,"onclick","click");}}},onSubmit:function(form){return true;},submit:function(e){e.preventDefault();if(this.onSubmit(this.form)){dojo.io.bind(dojo.lang.mixin(this.bindArgs,{formFilter:dojo.lang.hitch(this,"formFilter")}));}},click:function(e){var node=e.currentTarget;if(node.disabled){return;}
this.clickedButton=node;},formFilter:function(node){var type=(node.type||"").toLowerCase();var _31c=false;if(node.disabled||!node.name){_31c=false;}else{if(dojo.lang.inArray(["submit","button","image"],type)){if(!this.clickedButton){this.clickedButton=node;}
_31c=node==this.clickedButton;}else{_31c=!dojo.lang.inArray(["file","submit","reset","button"],type);}}
return _31c;},connect:function(_31d,_31e,_31f){if(dojo.evalObjPath("dojo.event.connect")){dojo.event.connect(_31d,_31e,this,_31f);}else{var fcn=dojo.lang.hitch(this,_31f);_31d[_31e]=function(e){if(!e){e=window.event;}
if(!e.currentTarget){e.currentTarget=e.srcElement;}
if(!e.preventDefault){e.preventDefault=function(){window.event.returnValue=false;};}
fcn(e);};}}});dojo.io.XMLHTTPTransport=new function(){var _322=this;var _323={};this.useCache=false;this.preventCache=false;function getCacheKey(url,_325,_326){return url+"|"+_325+"|"+_326.toLowerCase();}
function addToCache(url,_328,_329,http){_323[getCacheKey(url,_328,_329)]=http;}
function getFromCache(url,_32c,_32d){return _323[getCacheKey(url,_32c,_32d)];}
this.clearCache=function(){_323={};};function doLoad(_32e,http,url,_331,_332){if(((http.status>=200)&&(http.status<300))||(http.status==304)||(location.protocol=="file:"&&(http.status==0||http.status==undefined))||(location.protocol=="chrome:"&&(http.status==0||http.status==undefined))){var ret;if(_32e.method.toLowerCase()=="head"){var _334=http.getAllResponseHeaders();ret={};ret.toString=function(){return _334;};var _335=_334.split(/[\r\n]+/g);for(var i=0;i<_335.length;i++){var pair=_335[i].match(/^([^:]+)\s*:\s*(.+)$/i);if(pair){ret[pair[1]]=pair[2];}}}else{if(_32e.mimetype=="text/javascript"){try{ret=dj_eval(http.responseText);}
catch(e){dojo.debug(e);dojo.debug(http.responseText);ret=null;}}else{if(_32e.mimetype=="text/json"||_32e.mimetype=="application/json"){try{ret=dj_eval("("+http.responseText+")");}
catch(e){dojo.debug(e);dojo.debug(http.responseText);ret=false;}}else{if((_32e.mimetype=="application/xml")||(_32e.mimetype=="text/xml")){ret=http.responseXML;if(!ret||typeof ret=="string"||!http.getResponseHeader("Content-Type")){ret=dojo.dom.createDocumentFromText(http.responseText);}}else{ret=http.responseText;}}}}
if(_332){addToCache(url,_331,_32e.method,http);}
_32e[(typeof _32e.load=="function")?"load":"handle"]("load",ret,http,_32e);}else{var _338=new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);_32e[(typeof _32e.error=="function")?"error":"handle"]("error",_338,http,_32e);}}
function setHeaders(http,_33a){if(_33a["headers"]){for(var _33b in _33a["headers"]){if(_33b.toLowerCase()=="content-type"&&!_33a["contentType"]){_33a["contentType"]=_33a["headers"][_33b];}else{http.setRequestHeader(_33b,_33a["headers"][_33b]);}}}}
this.inFlight=[];this.inFlightTimer=null;this.startWatchingInFlight=function(){if(!this.inFlightTimer){this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);}};this.watchInFlight=function(){var now=null;if(!dojo.hostenv._blockAsync&&!_322._blockAsync){for(var x=this.inFlight.length-1;x>=0;x--){try{var tif=this.inFlight[x];if(!tif||tif.http._aborted||!tif.http.readyState){this.inFlight.splice(x,1);continue;}
if(4==tif.http.readyState){this.inFlight.splice(x,1);doLoad(tif.req,tif.http,tif.url,tif.query,tif.useCache);}else{if(tif.startTime){if(!now){now=(new Date()).getTime();}
if(tif.startTime+(tif.req.timeoutSeconds*1000)<now){if(typeof tif.http.abort=="function"){tif.http.abort();}
this.inFlight.splice(x,1);tif.req[(typeof tif.req.timeout=="function")?"timeout":"handle"]("timeout",null,tif.http,tif.req);}}}}
catch(e){try{var _33f=new dojo.io.Error("XMLHttpTransport.watchInFlight Error: "+e);tif.req[(typeof tif.req.error=="function")?"error":"handle"]("error",_33f,tif.http,tif.req);}
catch(e2){dojo.debug("XMLHttpTransport error callback failed: "+e2);}}}}
clearTimeout(this.inFlightTimer);if(this.inFlight.length==0){this.inFlightTimer=null;return;}
this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);};var _340=dojo.hostenv.getXmlhttpObject()?true:false;this.canHandle=function(_341){return _340&&dojo.lang.inArray(["text/plain","text/html","application/xml","text/xml","text/javascript","text/json","application/json"],(_341["mimetype"].toLowerCase()||""))&&!(_341["formNode"]&&dojo.io.formHasFile(_341["formNode"]));};this.multipartBoundary="45309FFF-BD65-4d50-99C9-36986896A96F";this.bind=function(_342){if(!_342["url"]){if(!_342["formNode"]&&(_342["backButton"]||_342["back"]||_342["changeUrl"]||_342["watchForURL"])&&(!djConfig.preventBackButtonFix)){dojo.deprecated("Using dojo.io.XMLHTTPTransport.bind() to add to browser history without doing an IO request","Use dojo.undo.browser.addToHistory() instead.","0.4");dojo.undo.browser.addToHistory(_342);return true;}}
var url=_342.url;var _344="";if(_342["formNode"]){var ta=_342.formNode.getAttribute("action");if((ta)&&(!_342["url"])){url=ta;}
var tp=_342.formNode.getAttribute("method");if((tp)&&(!_342["method"])){_342.method=tp;}
_344+=dojo.io.encodeForm(_342.formNode,_342.encoding,_342["formFilter"]);}
if(url.indexOf("#")>-1){dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);url=url.split("#")[0];}
if(_342["file"]){_342.method="post";}
if(!_342["method"]){_342.method="get";}
if(_342.method.toLowerCase()=="get"){_342.multipart=false;}else{if(_342["file"]){_342.multipart=true;}else{if(!_342["multipart"]){_342.multipart=false;}}}
if(_342["backButton"]||_342["back"]||_342["changeUrl"]){dojo.undo.browser.addToHistory(_342);}
var _347=_342["content"]||{};if(_342.sendTransport){_347["dojo.transport"]="xmlhttp";}
do{if(_342.postContent){_344=_342.postContent;break;}
if(_347){_344+=dojo.io.argsFromMap(_347,_342.encoding);}
if(_342.method.toLowerCase()=="get"||!_342.multipart){break;}
var t=[];if(_344.length){var q=_344.split("&");for(var i=0;i<q.length;++i){if(q[i].length){var p=q[i].split("=");t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+p[0]+"\"","",p[1]);}}}
if(_342.file){if(dojo.lang.isArray(_342.file)){for(var i=0;i<_342.file.length;++i){var o=_342.file[i];t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);}}else{var o=_342.file;t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);}}
if(t.length){t.push("--"+this.multipartBoundary+"--","");_344=t.join("\r\n");}}while(false);var _34d=_342["sync"]?false:true;var _34e=_342["preventCache"]||(this.preventCache==true&&_342["preventCache"]!=false);var _34f=_342["useCache"]==true||(this.useCache==true&&_342["useCache"]!=false);if(!_34e&&_34f){var _350=getFromCache(url,_344,_342.method);if(_350){doLoad(_342,_350,url,_344,false);return;}}
var http=dojo.hostenv.getXmlhttpObject(_342);var _352=false;if(_34d){var _353=this.inFlight.push({"req":_342,"http":http,"url":url,"query":_344,"useCache":_34f,"startTime":_342.timeoutSeconds?(new Date()).getTime():0});this.startWatchingInFlight();}else{_322._blockAsync=true;}
if(_342.method.toLowerCase()=="post"){if(!_342.user){http.open("POST",url,_34d);}else{http.open("POST",url,_34d,_342.user,_342.password);}
setHeaders(http,_342);http.setRequestHeader("Content-Type",_342.multipart?("multipart/form-data; boundary="+this.multipartBoundary):(_342.contentType||"application/x-www-form-urlencoded"));try{http.send(_344);}
catch(e){if(typeof http.abort=="function"){http.abort();}
doLoad(_342,{status:404},url,_344,_34f);}}else{var _354=url;if(_344!=""){_354+=(_354.indexOf("?")>-1?"&":"?")+_344;}
if(_34e){_354+=(dojo.string.endsWithAny(_354,"?","&")?"":(_354.indexOf("?")>-1?"&":"?"))+"dojo.preventCache="+new Date().valueOf();}
if(!_342.user){http.open(_342.method.toUpperCase(),_354,_34d);}else{http.open(_342.method.toUpperCase(),_354,_34d,_342.user,_342.password);}
setHeaders(http,_342);try{http.send(null);}
catch(e){if(typeof http.abort=="function"){http.abort();}
doLoad(_342,{status:404},url,_344,_34f);}}
if(!_34d){doLoad(_342,http,url,_344,_34f);_322._blockAsync=false;}
_342.abort=function(){try{http._aborted=true;}
catch(e){}
return http.abort();};return;};dojo.io.transports.addTransport("XMLHTTPTransport");};}
dojo.provide("dojo.io.cookie");dojo.io.cookie.setCookie=function(name,_356,days,path,_359,_35a){var _35b=-1;if(typeof days=="number"&&days>=0){var d=new Date();d.setTime(d.getTime()+(days*24*60*60*1000));_35b=d.toGMTString();}
_356=escape(_356);document.cookie=name+"="+_356+";"+(_35b!=-1?" expires="+_35b+";":"")+(path?"path="+path:"")+(_359?"; domain="+_359:"")+(_35a?"; secure":"");};dojo.io.cookie.set=dojo.io.cookie.setCookie;dojo.io.cookie.getCookie=function(name){var idx=document.cookie.lastIndexOf(name+"=");if(idx==-1){return null;}
var _35f=document.cookie.substring(idx+name.length+1);var end=_35f.indexOf(";");if(end==-1){end=_35f.length;}
_35f=_35f.substring(0,end);_35f=unescape(_35f);return _35f;};dojo.io.cookie.get=dojo.io.cookie.getCookie;dojo.io.cookie.deleteCookie=function(name){dojo.io.cookie.setCookie(name,"-",0);};dojo.io.cookie.setObjectCookie=function(name,obj,days,path,_366,_367,_368){if(arguments.length==5){_368=_366;_366=null;_367=null;}
var _369=[],_36a,_36b="";if(!_368){_36a=dojo.io.cookie.getObjectCookie(name);}
if(days>=0){if(!_36a){_36a={};}
for(var prop in obj){if(prop==null){delete _36a[prop];}else{if(typeof obj[prop]=="string"||typeof obj[prop]=="number"){_36a[prop]=obj[prop];}}}
prop=null;for(var prop in _36a){_369.push(escape(prop)+"="+escape(_36a[prop]));}
_36b=_369.join("&");}
dojo.io.cookie.setCookie(name,_36b,days,path,_366,_367);};dojo.io.cookie.getObjectCookie=function(name){var _36e=null,_36f=dojo.io.cookie.getCookie(name);if(_36f){_36e={};var _370=_36f.split("&");for(var i=0;i<_370.length;i++){var pair=_370[i].split("=");var _373=pair[1];if(isNaN(_373)){_373=unescape(pair[1]);}
_36e[unescape(pair[0])]=_373;}}
return _36e;};dojo.io.cookie.isSupported=function(){if(typeof navigator.cookieEnabled!="boolean"){dojo.io.cookie.setCookie("__TestingYourBrowserForCookieSupport__","CookiesAllowed",90,null);var _374=dojo.io.cookie.getCookie("__TestingYourBrowserForCookieSupport__");navigator.cookieEnabled=(_374=="CookiesAllowed");if(navigator.cookieEnabled){this.deleteCookie("__TestingYourBrowserForCookieSupport__");}}
return navigator.cookieEnabled;};if(!dojo.io.cookies){dojo.io.cookies=dojo.io.cookie;}
dojo.provide("dojo.date.common");dojo.date.setDayOfYear=function(_375,_376){_375.setMonth(0);_375.setDate(_376);return _375;};dojo.date.getDayOfYear=function(_377){var _378=_377.getFullYear();var _379=new Date(_378-1,11,31);return Math.floor((_377.getTime()-_379.getTime())/86400000);};dojo.date.setWeekOfYear=function(_37a,week,_37c){if(arguments.length==1){_37c=0;}
dojo.unimplemented("dojo.date.setWeekOfYear");};dojo.date.getWeekOfYear=function(_37d,_37e){if(arguments.length==1){_37e=0;}
var _37f=new Date(_37d.getFullYear(),0,1);var day=_37f.getDay();_37f.setDate(_37f.getDate()-day+_37e-(day>_37e?7:0));return Math.floor((_37d.getTime()-_37f.getTime())/604800000);};dojo.date.setIsoWeekOfYear=function(_381,week,_383){if(arguments.length==1){_383=1;}
dojo.unimplemented("dojo.date.setIsoWeekOfYear");};dojo.date.getIsoWeekOfYear=function(_384,_385){if(arguments.length==1){_385=1;}
dojo.unimplemented("dojo.date.getIsoWeekOfYear");};dojo.date.shortTimezones=["IDLW","BET","HST","MART","AKST","PST","MST","CST","EST","AST","NFT","BST","FST","AT","GMT","CET","EET","MSK","IRT","GST","AFT","AGTT","IST","NPT","ALMT","MMT","JT","AWST","JST","ACST","AEST","LHST","VUT","NFT","NZT","CHAST","PHOT","LINT"];dojo.date.timezoneOffsets=[-720,-660,-600,-570,-540,-480,-420,-360,-300,-240,-210,-180,-120,-60,0,60,120,180,210,240,270,300,330,345,360,390,420,480,540,570,600,630,660,690,720,765,780,840];dojo.date.getDaysInMonth=function(_386){var _387=_386.getMonth();var days=[31,28,31,30,31,30,31,31,30,31,30,31];if(_387==1&&dojo.date.isLeapYear(_386)){return 29;}else{return days[_387];}};dojo.date.isLeapYear=function(_389){var year=_389.getFullYear();return (year%400==0)?true:(year%100==0)?false:(year%4==0)?true:false;};dojo.date.getTimezoneName=function(_38b){var str=_38b.toString();var tz="";var _38e;var pos=str.indexOf("(");if(pos>-1){pos++;tz=str.substring(pos,str.indexOf(")"));}else{var pat=/([A-Z\/]+) \d{4}$/;if((_38e=str.match(pat))){tz=_38e[1];}else{str=_38b.toLocaleString();pat=/ ([A-Z\/]+)$/;if((_38e=str.match(pat))){tz=_38e[1];}}}
return tz=="AM"||tz=="PM"?"":tz;};dojo.date.getOrdinal=function(_391){var date=_391.getDate();if(date%100!=11&&date%10==1){return "st";}else{if(date%100!=12&&date%10==2){return "nd";}else{if(date%100!=13&&date%10==3){return "rd";}else{return "th";}}}};dojo.date.compareTypes={DATE:1,TIME:2};dojo.date.compare=function(_393,_394,_395){var dA=_393;var dB=_394||new Date();var now=new Date();with(dojo.date.compareTypes){var opt=_395||(DATE|TIME);var d1=new Date((opt&DATE)?dA.getFullYear():now.getFullYear(),(opt&DATE)?dA.getMonth():now.getMonth(),(opt&DATE)?dA.getDate():now.getDate(),(opt&TIME)?dA.getHours():0,(opt&TIME)?dA.getMinutes():0,(opt&TIME)?dA.getSeconds():0);var d2=new Date((opt&DATE)?dB.getFullYear():now.getFullYear(),(opt&DATE)?dB.getMonth():now.getMonth(),(opt&DATE)?dB.getDate():now.getDate(),(opt&TIME)?dB.getHours():0,(opt&TIME)?dB.getMinutes():0,(opt&TIME)?dB.getSeconds():0);}
if(d1.valueOf()>d2.valueOf()){return 1;}
if(d1.valueOf()<d2.valueOf()){return -1;}
return 0;};dojo.date.dateParts={YEAR:0,MONTH:1,DAY:2,HOUR:3,MINUTE:4,SECOND:5,MILLISECOND:6,QUARTER:7,WEEK:8,WEEKDAY:9};dojo.date.add=function(dt,_39d,incr){if(typeof dt=="number"){dt=new Date(dt);}
function fixOvershoot(){if(sum.getDate()<dt.getDate()){sum.setDate(0);}}
var sum=new Date(dt);with(dojo.date.dateParts){switch(_39d){case YEAR:
sum.setFullYear(dt.getFullYear()+incr);fixOvershoot();break;case QUARTER:
incr*=3;case MONTH:
sum.setMonth(dt.getMonth()+incr);fixOvershoot();break;case WEEK:
incr*=7;case DAY:
sum.setDate(dt.getDate()+incr);break;case WEEKDAY:
var dat=dt.getDate();var _3a1=0;var days=0;var strt=0;var trgt=0;var adj=0;var mod=incr%5;if(mod==0){days=(incr>0)?5:-5;_3a1=(incr>0)?((incr-5)/5):((incr+5)/5);}else{days=mod;_3a1=parseInt(incr/5);}
strt=dt.getDay();if(strt==6&&incr>0){adj=1;}else{if(strt==0&&incr<0){adj=-1;}}
trgt=(strt+days);if(trgt==0||trgt==6){adj=(incr>0)?2:-2;}
sum.setDate(dat+(7*_3a1)+days+adj);break;case HOUR:
sum.setHours(sum.getHours()+incr);break;case MINUTE:
sum.setMinutes(sum.getMinutes()+incr);break;case SECOND:
sum.setSeconds(sum.getSeconds()+incr);break;case MILLISECOND:
sum.setMilliseconds(sum.getMilliseconds()+incr);break;default:
break;}}
return sum;};dojo.date.diff=function(dtA,dtB,_3a9){if(typeof dtA=="number"){dtA=new Date(dtA);}
if(typeof dtB=="number"){dtB=new Date(dtB);}
var _3aa=dtB.getFullYear()-dtA.getFullYear();var _3ab=(dtB.getMonth()-dtA.getMonth())+(_3aa*12);var _3ac=dtB.getTime()-dtA.getTime();var _3ad=_3ac/1000;var _3ae=_3ad/60;var _3af=_3ae/60;var _3b0=_3af/24;var _3b1=_3b0/7;var _3b2=0;with(dojo.date.dateParts){switch(_3a9){case YEAR:
_3b2=_3aa;break;case QUARTER:
var mA=dtA.getMonth();var mB=dtB.getMonth();var qA=Math.floor(mA/3)+1;var qB=Math.floor(mB/3)+1;qB+=(_3aa*4);_3b2=qB-qA;break;case MONTH:
_3b2=_3ab;break;case WEEK:
_3b2=parseInt(_3b1);break;case DAY:
_3b2=_3b0;break;case WEEKDAY:
var days=Math.round(_3b0);var _3b8=parseInt(days/7);var mod=days%7;if(mod==0){days=_3b8*5;}else{var adj=0;var aDay=dtA.getDay();var bDay=dtB.getDay();_3b8=parseInt(days/7);mod=days%7;var _3bd=new Date(dtA);_3bd.setDate(_3bd.getDate()+(_3b8*7));var _3be=_3bd.getDay();if(_3b0>0){switch(true){case aDay==6:
adj=-1;break;case aDay==0:
adj=0;break;case bDay==6:
adj=-1;break;case bDay==0:
adj=-2;break;case (_3be+mod)>5:
adj=-2;break;default:
break;}}else{if(_3b0<0){switch(true){case aDay==6:
adj=0;break;case aDay==0:
adj=1;break;case bDay==6:
adj=2;break;case bDay==0:
adj=1;break;case (_3be+mod)<0:
adj=2;break;default:
break;}}}
days+=adj;days-=(_3b8*2);}
_3b2=days;break;case HOUR:
_3b2=_3af;break;case MINUTE:
_3b2=_3ae;break;case SECOND:
_3b2=_3ad;break;case MILLISECOND:
_3b2=_3ac;break;default:
break;}}
return Math.round(_3b2);};dojo.provide("dojo.date.supplemental");dojo.date.getFirstDayOfWeek=function(_3bf){var _3c0={mv:5,ae:6,af:6,bh:6,dj:6,dz:6,eg:6,er:6,et:6,iq:6,ir:6,jo:6,ke:6,kw:6,lb:6,ly:6,ma:6,om:6,qa:6,sa:6,sd:6,so:6,tn:6,ye:6,as:0,au:0,az:0,bw:0,ca:0,cn:0,fo:0,ge:0,gl:0,gu:0,hk:0,ie:0,il:0,is:0,jm:0,jp:0,kg:0,kr:0,la:0,mh:0,mo:0,mp:0,mt:0,nz:0,ph:0,pk:0,sg:0,th:0,tt:0,tw:0,um:0,us:0,uz:0,vi:0,za:0,zw:0,et:0,mw:0,ng:0,tj:0,gb:0,sy:4};_3bf=dojo.hostenv.normalizeLocale(_3bf);var _3c1=_3bf.split("-")[1];var dow=_3c0[_3c1];return (typeof dow=="undefined")?1:dow;};dojo.date.getWeekend=function(_3c3){var _3c4={eg:5,il:5,sy:5,"in":0,ae:4,bh:4,dz:4,iq:4,jo:4,kw:4,lb:4,ly:4,ma:4,om:4,qa:4,sa:4,sd:4,tn:4,ye:4};var _3c5={ae:5,bh:5,dz:5,iq:5,jo:5,kw:5,lb:5,ly:5,ma:5,om:5,qa:5,sa:5,sd:5,tn:5,ye:5,af:5,ir:5,eg:6,il:6,sy:6};_3c3=dojo.hostenv.normalizeLocale(_3c3);var _3c6=_3c3.split("-")[1];var _3c7=_3c4[_3c6];var end=_3c5[_3c6];if(typeof _3c7=="undefined"){_3c7=6;}
if(typeof end=="undefined"){end=0;}
return {start:_3c7,end:end};};dojo.date.isWeekend=function(_3c9,_3ca){var _3cb=dojo.date.getWeekend(_3ca);var day=(_3c9||new Date()).getDay();if(_3cb.end<_3cb.start){_3cb.end+=7;if(day<_3cb.start){day+=7;}}
return day>=_3cb.start&&day<=_3cb.end;};dojo.provide("dojo.i18n.common");dojo.i18n.getLocalization=function(_3cd,_3ce,_3cf){dojo.hostenv.preloadLocalizations();_3cf=dojo.hostenv.normalizeLocale(_3cf);var _3d0=_3cf.split("-");var _3d1=[_3cd,"nls",_3ce].join(".");var _3d2=dojo.hostenv.findModule(_3d1,true);var _3d3;for(var i=_3d0.length;i>0;i--){var loc=_3d0.slice(0,i).join("_");if(_3d2[loc]){_3d3=_3d2[loc];break;}}
if(!_3d3){_3d3=_3d2.ROOT;}
if(_3d3){var _3d6=function(){};_3d6.prototype=_3d3;return new _3d6();}
dojo.raise("Bundle not found: "+_3ce+" in "+_3cd+" , locale="+_3cf);};dojo.i18n.isLTR=function(_3d7){var lang=dojo.hostenv.normalizeLocale(_3d7).split("-")[0];var RTL={ar:true,fa:true,he:true,ur:true,yi:true};return !RTL[lang];};dojo.provide("dojo.date.format");(function(){dojo.date.format=function(_3da,_3db){if(typeof _3db=="string"){dojo.deprecated("dojo.date.format","To format dates with POSIX-style strings, please use dojo.date.strftime instead","0.5");return dojo.date.strftime(_3da,_3db);}
function formatPattern(_3dc,_3dd){return _3dd.replace(/([a-z])\1*/ig,function(_3de){var s;var c=_3de.charAt(0);var l=_3de.length;var pad;var _3e3=["abbr","wide","narrow"];switch(c){case "G":
if(l>3){dojo.unimplemented("Era format not implemented");}
s=info.eras[_3dc.getFullYear()<0?1:0];break;case "y":
s=_3dc.getFullYear();switch(l){case 1:
break;case 2:
s=String(s).substr(-2);break;default:
pad=true;}
break;case "Q":
case "q":
s=Math.ceil((_3dc.getMonth()+1)/3);switch(l){case 1:
case 2:
pad=true;break;case 3:
case 4:
dojo.unimplemented("Quarter format not implemented");}
break;case "M":
case "L":
var m=_3dc.getMonth();var _3e6;switch(l){case 1:
case 2:
s=m+1;pad=true;break;case 3:
case 4:
case 5:
_3e6=_3e3[l-3];break;}
if(_3e6){var type=(c=="L")?"standalone":"format";var prop=["months",type,_3e6].join("-");s=info[prop][m];}
break;case "w":
var _3e9=0;s=dojo.date.getWeekOfYear(_3dc,_3e9);pad=true;break;case "d":
s=_3dc.getDate();pad=true;break;case "D":
s=dojo.date.getDayOfYear(_3dc);pad=true;break;case "E":
case "e":
case "c":
var d=_3dc.getDay();var _3e6;switch(l){case 1:
case 2:
if(c=="e"){var _3eb=dojo.date.getFirstDayOfWeek(_3db.locale);d=(d-_3eb+7)%7;}
if(c!="c"){s=d+1;pad=true;break;}
case 3:
case 4:
case 5:
_3e6=_3e3[l-3];break;}
if(_3e6){var type=(c=="c")?"standalone":"format";var prop=["days",type,_3e6].join("-");s=info[prop][d];}
break;case "a":
var _3ec=(_3dc.getHours()<12)?"am":"pm";s=info[_3ec];break;case "h":
case "H":
case "K":
case "k":
var h=_3dc.getHours();switch(c){case "h":
s=(h%12)||12;break;case "H":
s=h;break;case "K":
s=(h%12);break;case "k":
s=h||24;break;}
pad=true;break;case "m":
s=_3dc.getMinutes();pad=true;break;case "s":
s=_3dc.getSeconds();pad=true;break;case "S":
s=Math.round(_3dc.getMilliseconds()*Math.pow(10,l-3));break;case "v":
case "z":
s=dojo.date.getTimezoneName(_3dc);if(s){break;}
l=4;case "Z":
var _3ee=_3dc.getTimezoneOffset();var tz=[(_3ee<=0?"+":"-"),dojo.string.pad(Math.floor(Math.abs(_3ee)/60),2),dojo.string.pad(Math.abs(_3ee)%60,2)];if(l==4){tz.splice(0,0,"GMT");tz.splice(3,0,":");}
s=tz.join("");break;case "Y":
case "u":
case "W":
case "F":
case "g":
case "A":
dojo.debug(_3de+" modifier not yet implemented");s="?";break;default:
dojo.raise("dojo.date.format: invalid pattern char: "+_3dd);}
if(pad){s=dojo.string.pad(s,l);}
return s;});}
_3db=_3db||{};var _3f0=dojo.hostenv.normalizeLocale(_3db.locale);var _3f1=_3db.formatLength||"full";var info=dojo.date._getGregorianBundle(_3f0);var str=[];var _3f3=dojo.lang.curry(this,formatPattern,_3da);if(_3db.selector!="timeOnly"){var _3f4=_3db.datePattern||info["dateFormat-"+_3f1];if(_3f4){str.push(_processPattern(_3f4,_3f3));}}
if(_3db.selector!="dateOnly"){var _3f5=_3db.timePattern||info["timeFormat-"+_3f1];if(_3f5){str.push(_processPattern(_3f5,_3f3));}}
var _3f6=str.join(" ");return _3f6;};dojo.date.parse=function(_3f7,_3f8){_3f8=_3f8||{};var _3f9=dojo.hostenv.normalizeLocale(_3f8.locale);var info=dojo.date._getGregorianBundle(_3f9);var _3fb=_3f8.formatLength||"full";if(!_3f8.selector){_3f8.selector="dateOnly";}
var _3fc=_3f8.datePattern||info["dateFormat-"+_3fb];var _3fd=_3f8.timePattern||info["timeFormat-"+_3fb];var _3fe;if(_3f8.selector=="dateOnly"){_3fe=_3fc;}else{if(_3f8.selector=="timeOnly"){_3fe=_3fd;}else{if(_3f8.selector=="dateTime"){_3fe=_3fc+" "+_3fd;}else{var msg="dojo.date.parse: Unknown selector param passed: '"+_3f8.selector+"'.";msg+=" Defaulting to date pattern.";dojo.debug(msg);_3fe=_3fc;}}}
var _400=[];var _401=_processPattern(_3fe,dojo.lang.curry(this,_buildDateTimeRE,_400,info,_3f8));var _402=new RegExp("^"+_401+"$");var _403=_402.exec(_3f7);if(!_403){return null;}
var _404=["abbr","wide","narrow"];var _405=new Date(1972,0);var _406={};for(var i=1;i<_403.length;i++){var grp=_400[i-1];var l=grp.length;var v=_403[i];switch(grp.charAt(0)){case "y":
if(l!=2){_405.setFullYear(v);_406.year=v;}else{if(v<100){v=Number(v);var year=""+new Date().getFullYear();var _40c=year.substring(0,2)*100;var _40d=Number(year.substring(2,4));var _40e=Math.min(_40d+20,99);var num=(v<_40e)?_40c+v:_40c-100+v;_405.setFullYear(num);_406.year=num;}else{if(_3f8.strict){return null;}
_405.setFullYear(v);_406.year=v;}}
break;case "M":
if(l>2){if(!_3f8.strict){v=v.replace(/\./g,"");v=v.toLowerCase();}
var _410=info["months-format-"+_404[l-3]].concat();for(var j=0;j<_410.length;j++){if(!_3f8.strict){_410[j]=_410[j].toLowerCase();}
if(v==_410[j]){_405.setMonth(j);_406.month=j;break;}}
if(j==_410.length){dojo.debug("dojo.date.parse: Could not parse month name: '"+v+"'.");return null;}}else{_405.setMonth(v-1);_406.month=v-1;}
break;case "E":
case "e":
if(!_3f8.strict){v=v.toLowerCase();}
var days=info["days-format-"+_404[l-3]].concat();for(var j=0;j<days.length;j++){if(!_3f8.strict){days[j]=days[j].toLowerCase();}
if(v==days[j]){break;}}
if(j==days.length){dojo.debug("dojo.date.parse: Could not parse weekday name: '"+v+"'.");return null;}
break;case "d":
_405.setDate(v);_406.date=v;break;case "a":
var am=_3f8.am||info.am;var pm=_3f8.pm||info.pm;if(!_3f8.strict){v=v.replace(/\./g,"").toLowerCase();am=am.replace(/\./g,"").toLowerCase();pm=pm.replace(/\./g,"").toLowerCase();}
if(_3f8.strict&&v!=am&&v!=pm){dojo.debug("dojo.date.parse: Could not parse am/pm part.");return null;}
var _415=_405.getHours();if(v==pm&&_415<12){_405.setHours(_415+12);}else{if(v==am&&_415==12){_405.setHours(0);}}
break;case "K":
if(v==24){v=0;}
case "h":
case "H":
case "k":
if(v>23){dojo.debug("dojo.date.parse: Illegal hours value");return null;}
_405.setHours(v);break;case "m":
_405.setMinutes(v);break;case "s":
_405.setSeconds(v);break;case "S":
_405.setMilliseconds(v);break;default:
dojo.unimplemented("dojo.date.parse: unsupported pattern char="+grp.charAt(0));}}
if(_406.year&&_405.getFullYear()!=_406.year){dojo.debug("Parsed year: '"+_405.getFullYear()+"' did not match input year: '"+_406.year+"'.");return null;}
if(_406.month&&_405.getMonth()!=_406.month){dojo.debug("Parsed month: '"+_405.getMonth()+"' did not match input month: '"+_406.month+"'.");return null;}
if(_406.date&&_405.getDate()!=_406.date){dojo.debug("Parsed day of month: '"+_405.getDate()+"' did not match input day of month: '"+_406.date+"'.");return null;}
return _405;};function _processPattern(_416,_417,_418,_419){var _41a=function(x){return x;};_417=_417||_41a;_418=_418||_41a;_419=_419||_41a;var _41c=_416.match(/(''|[^'])+/g);var _41d=false;for(var i=0;i<_41c.length;i++){if(!_41c[i]){_41c[i]="";}else{_41c[i]=(_41d?_418:_417)(_41c[i]);_41d=!_41d;}}
return _419(_41c.join(""));}
function _buildDateTimeRE(_41f,info,_421,_422){return _422.replace(/([a-z])\1*/ig,function(_423){var s;var c=_423.charAt(0);var l=_423.length;switch(c){case "y":
s="\\d"+((l==2)?"{2,4}":"+");break;case "M":
s=(l>2)?"\\S+":"\\d{1,2}";break;case "d":
s="\\d{1,2}";break;case "E":
s="\\S+";break;case "h":
case "H":
case "K":
case "k":
s="\\d{1,2}";break;case "m":
case "s":
s="[0-5]\\d";break;case "S":
s="\\d{1,3}";break;case "a":
var am=_421.am||info.am||"AM";var pm=_421.pm||info.pm||"PM";if(_421.strict){s=am+"|"+pm;}else{s=am;s+=(am!=am.toLowerCase())?"|"+am.toLowerCase():"";s+="|";s+=(pm!=pm.toLowerCase())?pm+"|"+pm.toLowerCase():pm;}
break;default:
dojo.unimplemented("parse of date format, pattern="+_422);}
if(_41f){_41f.push(_423);}
return "\\s*("+s+")\\s*";});}})();dojo.date.strftime=function(_429,_42a,_42b){var _42c=null;function _(s,n){return dojo.string.pad(s,n||2,_42c||"0");}
var info=dojo.date._getGregorianBundle(_42b);function $(_430){switch(_430){case "a":
return dojo.date.getDayShortName(_429,_42b);case "A":
return dojo.date.getDayName(_429,_42b);case "b":
case "h":
return dojo.date.getMonthShortName(_429,_42b);case "B":
return dojo.date.getMonthName(_429,_42b);case "c":
return dojo.date.format(_429,{locale:_42b});case "C":
return _(Math.floor(_429.getFullYear()/100));case "d":
return _(_429.getDate());case "D":
return $("m")+"/"+$("d")+"/"+$("y");case "e":
if(_42c==null){_42c=" ";}
return _(_429.getDate());case "f":
if(_42c==null){_42c=" ";}
return _(_429.getMonth()+1);case "g":
break;case "G":
dojo.unimplemented("unimplemented modifier 'G'");break;case "F":
return $("Y")+"-"+$("m")+"-"+$("d");case "H":
return _(_429.getHours());case "I":
return _(_429.getHours()%12||12);case "j":
return _(dojo.date.getDayOfYear(_429),3);case "k":
if(_42c==null){_42c=" ";}
return _(_429.getHours());case "l":
if(_42c==null){_42c=" ";}
return _(_429.getHours()%12||12);case "m":
return _(_429.getMonth()+1);case "M":
return _(_429.getMinutes());case "n":
return "\n";case "p":
return info[_429.getHours()<12?"am":"pm"];case "r":
return $("I")+":"+$("M")+":"+$("S")+" "+$("p");case "R":
return $("H")+":"+$("M");case "S":
return _(_429.getSeconds());case "t":
return "\t";case "T":
return $("H")+":"+$("M")+":"+$("S");case "u":
return String(_429.getDay()||7);case "U":
return _(dojo.date.getWeekOfYear(_429));case "V":
return _(dojo.date.getIsoWeekOfYear(_429));case "W":
return _(dojo.date.getWeekOfYear(_429,1));case "w":
return String(_429.getDay());case "x":
return dojo.date.format(_429,{selector:"dateOnly",locale:_42b});case "X":
return dojo.date.format(_429,{selector:"timeOnly",locale:_42b});case "y":
return _(_429.getFullYear()%100);case "Y":
return String(_429.getFullYear());case "z":
var _431=_429.getTimezoneOffset();return (_431>0?"-":"+")+_(Math.floor(Math.abs(_431)/60))+":"+_(Math.abs(_431)%60);case "Z":
return dojo.date.getTimezoneName(_429);case "%":
return "%";}}
var _432="";var i=0;var _434=0;var _435=null;while((_434=_42a.indexOf("%",i))!=-1){_432+=_42a.substring(i,_434++);switch(_42a.charAt(_434++)){case "_":
_42c=" ";break;case "-":
_42c="";break;case "0":
_42c="0";break;case "^":
_435="upper";break;case "*":
_435="lower";break;case "#":
_435="swap";break;default:
_42c=null;_434--;break;}
var _436=$(_42a.charAt(_434++));switch(_435){case "upper":
_436=_436.toUpperCase();break;case "lower":
_436=_436.toLowerCase();break;case "swap":
var _437=_436.toLowerCase();var _438="";var j=0;var ch="";while(j<_436.length){ch=_436.charAt(j);_438+=(ch==_437.charAt(j))?ch.toUpperCase():ch.toLowerCase();j++;}
_436=_438;break;default:
break;}
_435=null;_432+=_436;i=_434;}
_432+=_42a.substring(i);return _432;};(function(){var _43b=[];dojo.date.addCustomFormats=function(_43c,_43d){_43b.push({pkg:_43c,name:_43d});};dojo.date._getGregorianBundle=function(_43e){var _43f={};dojo.lang.forEach(_43b,function(desc){var _441=dojo.i18n.getLocalization(desc.pkg,desc.name,_43e);_43f=dojo.lang.mixin(_43f,_441);},this);return _43f;};})();dojo.date.addCustomFormats("dojo.i18n.calendar","gregorian");dojo.date.addCustomFormats("dojo.i18n.calendar","gregorianExtras");dojo.date.getNames=function(item,type,use,_445){var _446;var _447=dojo.date._getGregorianBundle(_445);var _448=[item,use,type];if(use=="standAlone"){_446=_447[_448.join("-")];}
_448[1]="format";return (_446||_447[_448.join("-")]).concat();};dojo.date.getDayName=function(_449,_44a){return dojo.date.getNames("days","wide","format",_44a)[_449.getDay()];};dojo.date.getDayShortName=function(_44b,_44c){return dojo.date.getNames("days","abbr","format",_44c)[_44b.getDay()];};dojo.date.getMonthName=function(_44d,_44e){return dojo.date.getNames("months","wide","format",_44e)[_44d.getMonth()];};dojo.date.getMonthShortName=function(_44f,_450){return dojo.date.getNames("months","abbr","format",_450)[_44f.getMonth()];};dojo.date.toRelativeString=function(_451){var now=new Date();var diff=(now-_451)/1000;var end=" ago";var _455=false;if(diff<0){_455=true;end=" from now";diff=-diff;}
if(diff<60){diff=Math.round(diff);return diff+" second"+(diff==1?"":"s")+end;}
if(diff<60*60){diff=Math.round(diff/60);return diff+" minute"+(diff==1?"":"s")+end;}
if(diff<60*60*24){diff=Math.round(diff/3600);return diff+" hour"+(diff==1?"":"s")+end;}
if(diff<60*60*24*7){diff=Math.round(diff/(3600*24));if(diff==1){return _455?"Tomorrow":"Yesterday";}else{return diff+" days"+end;}}
return dojo.date.format(_451);};dojo.date.toSql=function(_456,_457){return dojo.date.strftime(_456,"%F"+!_457?" %T":"");};dojo.date.fromSql=function(_458){var _459=_458.split(/[\- :]/g);while(_459.length<6){_459.push(0);}
return new Date(_459[0],(parseInt(_459[1],10)-1),_459[2],_459[3],_459[4],_459[5]);};dojo.provide("dojo.xml.Parse");dojo.xml.Parse=function(){function getTagName(node){return ((node)&&(node.tagName)?node.tagName.toLowerCase():"");}
function getDojoTagName(node){var _45c=getTagName(node);if(!_45c){return "";}
if((dojo.widget)&&(dojo.widget.tags[_45c])){return _45c;}
var p=_45c.indexOf(":");if(p>=0){return _45c;}
if(_45c.substr(0,5)=="dojo:"){return _45c;}
if(dojo.render.html.capable&&dojo.render.html.ie&&node.scopeName!="HTML"){return node.scopeName.toLowerCase()+":"+_45c;}
if(_45c.substr(0,4)=="dojo"){return "dojo:"+_45c.substring(4);}
var djt=node.getAttribute("dojoType")||node.getAttribute("dojotype");if(djt){if(djt.indexOf(":")<0){djt="dojo:"+djt;}
return djt.toLowerCase();}
djt=node.getAttributeNS&&node.getAttributeNS(dojo.dom.dojoml,"type");if(djt){return "dojo:"+djt.toLowerCase();}
try{djt=node.getAttribute("dojo:type");}
catch(e){}
if(djt){return "dojo:"+djt.toLowerCase();}
if((!dj_global["djConfig"])||(djConfig["ignoreClassNames"])){var _45f=node.className||node.getAttribute("class");if((_45f)&&(_45f.indexOf)&&(_45f.indexOf("dojo-")!=-1)){var _460=_45f.split(" ");for(var x=0,c=_460.length;x<c;x++){if(_460[x].slice(0,5)=="dojo-"){return "dojo:"+_460[x].substr(5).toLowerCase();}}}}
return "";}
this.parseElement=function(node,_464,_465,_466){var _467={};var _468=getTagName(node);if((_468)&&(_468.indexOf("/")==0)){return null;}
var _469=true;if(_465){var _46a=getDojoTagName(node);_468=_46a||_468;_469=Boolean(_46a);}
if(node&&node.getAttribute&&node.getAttribute("parseWidgets")&&node.getAttribute("parseWidgets")=="false"){return {};}
_467[_468]=[];var pos=_468.indexOf(":");if(pos>0){var ns=_468.substring(0,pos);_467["ns"]=ns;if((dojo.ns)&&(!dojo.ns.allow(ns))){_469=false;}}
if(_469){var _46d=this.parseAttributes(node);for(var attr in _46d){if((!_467[_468][attr])||(typeof _467[_468][attr]!="array")){_467[_468][attr]=[];}
_467[_468][attr].push(_46d[attr]);}
_467[_468].nodeRef=node;_467.tagName=_468;_467.index=_466||0;}
var _46f=0;for(var i=0;i<node.childNodes.length;i++){var tcn=node.childNodes.item(i);switch(tcn.nodeType){case dojo.dom.ELEMENT_NODE:
_46f++;var ctn=getDojoTagName(tcn)||getTagName(tcn);if(!_467[ctn]){_467[ctn]=[];}
_467[ctn].push(this.parseElement(tcn,true,_465,_46f));if((tcn.childNodes.length==1)&&(tcn.childNodes.item(0).nodeType==dojo.dom.TEXT_NODE)){_467[ctn][_467[ctn].length-1].value=tcn.childNodes.item(0).nodeValue;}
break;case dojo.dom.TEXT_NODE:
if(node.childNodes.length==1){_467[_468].push({value:node.childNodes.item(0).nodeValue});}
break;default:
break;}}
return _467;};this.parseAttributes=function(node){var _474={};var atts=node.attributes;var _476,i=0;while((_476=atts[i++])){if((dojo.render.html.capable)&&(dojo.render.html.ie)){if(!_476){continue;}
if((typeof _476=="object")&&(typeof _476.nodeValue=="undefined")||(_476.nodeValue==null)||(_476.nodeValue=="")){continue;}}
var nn=_476.nodeName.split(":");nn=(nn.length==2)?nn[1]:_476.nodeName;_474[nn]={value:_476.nodeValue};}
return _474;};};dojo.provide("dojo.lang.declare");dojo.lang.declare=function(_479,_47a,init,_47c){if((dojo.lang.isFunction(_47c))||((!_47c)&&(!dojo.lang.isFunction(init)))){var temp=_47c;_47c=init;init=temp;}
var _47e=[];if(dojo.lang.isArray(_47a)){_47e=_47a;_47a=_47e.shift();}
if(!init){init=dojo.evalObjPath(_479,false);if((init)&&(!dojo.lang.isFunction(init))){init=null;}}
var ctor=dojo.lang.declare._makeConstructor();var scp=(_47a?_47a.prototype:null);if(scp){scp.prototyping=true;ctor.prototype=new _47a();scp.prototyping=false;}
ctor.superclass=scp;ctor.mixins=_47e;for(var i=0,l=_47e.length;i<l;i++){dojo.lang.extend(ctor,_47e[i].prototype);}
ctor.prototype.initializer=null;ctor.prototype.declaredClass=_479;if(dojo.lang.isArray(_47c)){dojo.lang.extend.apply(dojo.lang,[ctor].concat(_47c));}else{dojo.lang.extend(ctor,(_47c)||{});}
dojo.lang.extend(ctor,dojo.lang.declare._common);ctor.prototype.constructor=ctor;ctor.prototype.initializer=(ctor.prototype.initializer)||(init)||(function(){});dojo.lang.setObjPathValue(_479,ctor,null,true);return ctor;};dojo.lang.declare._makeConstructor=function(){return function(){var self=this._getPropContext();var s=self.constructor.superclass;if((s)&&(s.constructor)){if(s.constructor==arguments.callee){this._inherited("constructor",arguments);}else{this._contextMethod(s,"constructor",arguments);}}
var ms=(self.constructor.mixins)||([]);for(var i=0,m;(m=ms[i]);i++){(((m.prototype)&&(m.prototype.initializer))||(m)).apply(this,arguments);}
if((!this.prototyping)&&(self.initializer)){self.initializer.apply(this,arguments);}};};dojo.lang.declare._common={_getPropContext:function(){return (this.___proto||this);},_contextMethod:function(_488,_489,args){var _48b,_48c=this.___proto;this.___proto=_488;try{_48b=_488[_489].apply(this,(args||[]));}
catch(e){throw e;}
finally{this.___proto=_48c;}
return _48b;},_inherited:function(prop,args){var p=this._getPropContext();do{if((!p.constructor)||(!p.constructor.superclass)){return;}
p=p.constructor.superclass;}while(!(prop in p));return (dojo.lang.isFunction(p[prop])?this._contextMethod(p,prop,args):p[prop]);}};dojo.declare=dojo.lang.declare;dojo.provide("dojo.ns");dojo.ns={namespaces:{},failed:{},loading:{},loaded:{},register:function(name,_491,_492,_493){if(!_493||!this.namespaces[name]){this.namespaces[name]=new dojo.ns.Ns(name,_491,_492);}},allow:function(name){if(this.failed[name]){return false;}
if((djConfig.excludeNamespace)&&(dojo.lang.inArray(djConfig.excludeNamespace,name))){return false;}
return ((name==this.dojo)||(!djConfig.includeNamespace)||(dojo.lang.inArray(djConfig.includeNamespace,name)));},get:function(name){return this.namespaces[name];},require:function(name){var ns=this.namespaces[name];if((ns)&&(this.loaded[name])){return ns;}
if(!this.allow(name)){return false;}
if(this.loading[name]){dojo.debug("dojo.namespace.require: re-entrant request to load namespace \""+name+"\" must fail.");return false;}
var req=dojo.require;this.loading[name]=true;try{if(name=="dojo"){req("dojo.namespaces.dojo");}else{if(!dojo.hostenv.moduleHasPrefix(name)){dojo.registerModulePath(name,"../"+name);}
req([name,"manifest"].join("."),false,true);}
if(!this.namespaces[name]){this.failed[name]=true;}}
finally{this.loading[name]=false;}
return this.namespaces[name];}};dojo.ns.Ns=function(name,_49a,_49b){this.name=name;this.module=_49a;this.resolver=_49b;this._loaded=[];this._failed=[];};dojo.ns.Ns.prototype.resolve=function(name,_49d,_49e){if(!this.resolver||djConfig["skipAutoRequire"]){return false;}
var _49f=this.resolver(name,_49d);if((_49f)&&(!this._loaded[_49f])&&(!this._failed[_49f])){var req=dojo.require;req(_49f,false,true);if(dojo.hostenv.findModule(_49f,false)){this._loaded[_49f]=true;}else{if(!_49e){dojo.raise("dojo.ns.Ns.resolve: module '"+_49f+"' not found after loading via namespace '"+this.name+"'");}
this._failed[_49f]=true;}}
return Boolean(this._loaded[_49f]);};dojo.registerNamespace=function(name,_4a2,_4a3){dojo.ns.register.apply(dojo.ns,arguments);};dojo.registerNamespaceResolver=function(name,_4a5){var n=dojo.ns.namespaces[name];if(n){n.resolver=_4a5;}};dojo.registerNamespaceManifest=function(_4a7,path,name,_4aa,_4ab){dojo.registerModulePath(name,path);dojo.registerNamespace(name,_4aa,_4ab);};dojo.registerNamespace("dojo","dojo.widget");dojo.provide("dojo.event.topic");dojo.event.topic=new function(){this.topics={};this.getTopic=function(_4ac){if(!this.topics[_4ac]){this.topics[_4ac]=new this.TopicImpl(_4ac);}
return this.topics[_4ac];};this.registerPublisher=function(_4ad,obj,_4af){var _4ad=this.getTopic(_4ad);_4ad.registerPublisher(obj,_4af);};this.subscribe=function(_4b0,obj,_4b2){var _4b0=this.getTopic(_4b0);_4b0.subscribe(obj,_4b2);};this.unsubscribe=function(_4b3,obj,_4b5){var _4b3=this.getTopic(_4b3);_4b3.unsubscribe(obj,_4b5);};this.destroy=function(_4b6){this.getTopic(_4b6).destroy();delete this.topics[_4b6];};this.publishApply=function(_4b7,args){var _4b7=this.getTopic(_4b7);_4b7.sendMessage.apply(_4b7,args);};this.publish=function(_4b9,_4ba){var _4b9=this.getTopic(_4b9);var args=[];for(var x=1;x<arguments.length;x++){args.push(arguments[x]);}
_4b9.sendMessage.apply(_4b9,args);};};dojo.event.topic.TopicImpl=function(_4bd){this.topicName=_4bd;this.subscribe=function(_4be,_4bf){var tf=_4bf||_4be;var to=(!_4bf)?dj_global:_4be;return dojo.event.kwConnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});};this.unsubscribe=function(_4c2,_4c3){var tf=(!_4c3)?_4c2:_4c3;var to=(!_4c3)?null:_4c2;return dojo.event.kwDisconnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});};this._getJoinPoint=function(){return dojo.event.MethodJoinPoint.getForMethod(this,"sendMessage");};this.setSquelch=function(_4c6){this._getJoinPoint().squelch=_4c6;};this.destroy=function(){this._getJoinPoint().disconnect();};this.registerPublisher=function(_4c7,_4c8){dojo.event.connect(_4c7,_4c8,this,"sendMessage");};this.sendMessage=function(_4c9){};};dojo.provide("dojo.event.*");dojo.provide("dojo.widget.Manager");dojo.widget.manager=new function(){this.widgets=[];this.widgetIds=[];this.topWidgets={};var _4ca={};var _4cb=[];this.getUniqueId=function(_4cc){var _4cd;do{_4cd=_4cc+"_"+(_4ca[_4cc]!=undefined?++_4ca[_4cc]:_4ca[_4cc]=0);}while(this.getWidgetById(_4cd));return _4cd;};this.add=function(_4ce){this.widgets.push(_4ce);if(!_4ce.extraArgs["id"]){_4ce.extraArgs["id"]=_4ce.extraArgs["ID"];}
if(_4ce.widgetId==""){if(_4ce["id"]){_4ce.widgetId=_4ce["id"];}else{if(_4ce.extraArgs["id"]){_4ce.widgetId=_4ce.extraArgs["id"];}else{_4ce.widgetId=this.getUniqueId(_4ce.ns+"_"+_4ce.widgetType);}}}
if(this.widgetIds[_4ce.widgetId]){dojo.debug("widget ID collision on ID: "+_4ce.widgetId);}
this.widgetIds[_4ce.widgetId]=_4ce;};this.destroyAll=function(){for(var x=this.widgets.length-1;x>=0;x--){try{this.widgets[x].destroy(true);delete this.widgets[x];}
catch(e){}}};this.remove=function(_4d0){if(dojo.lang.isNumber(_4d0)){var tw=this.widgets[_4d0].widgetId;delete this.widgetIds[tw];this.widgets.splice(_4d0,1);}else{this.removeById(_4d0);}};this.removeById=function(id){if(!dojo.lang.isString(id)){id=id["widgetId"];if(!id){dojo.debug("invalid widget or id passed to removeById");return;}}
for(var i=0;i<this.widgets.length;i++){if(this.widgets[i].widgetId==id){this.remove(i);break;}}};this.getWidgetById=function(id){if(dojo.lang.isString(id)){return this.widgetIds[id];}
return id;};this.getWidgetsByType=function(type){var lt=type.toLowerCase();var _4d7=(type.indexOf(":")<0?function(x){return x.widgetType.toLowerCase();}:function(x){return x.getNamespacedType();});var ret=[];dojo.lang.forEach(this.widgets,function(x){if(_4d7(x)==lt){ret.push(x);}});return ret;};this.getWidgetsByFilter=function(_4dc,_4dd){var ret=[];dojo.lang.every(this.widgets,function(x){if(_4dc(x)){ret.push(x);if(_4dd){return false;}}
return true;});return (_4dd?ret[0]:ret);};this.getAllWidgets=function(){return this.widgets.concat();};this.getWidgetByNode=function(node){var w=this.getAllWidgets();node=dojo.byId(node);for(var i=0;i<w.length;i++){if(w[i].domNode==node){return w[i];}}
return null;};this.byId=this.getWidgetById;this.byType=this.getWidgetsByType;this.byFilter=this.getWidgetsByFilter;this.byNode=this.getWidgetByNode;var _4e3={};var _4e4=["dojo.widget"];for(var i=0;i<_4e4.length;i++){_4e4[_4e4[i]]=true;}
this.registerWidgetPackage=function(_4e6){if(!_4e4[_4e6]){_4e4[_4e6]=true;_4e4.push(_4e6);}};this.getWidgetPackageList=function(){return dojo.lang.map(_4e4,function(elt){return (elt!==true?elt:undefined);});};this.getImplementation=function(_4e8,_4e9,_4ea,ns){var impl=this.getImplementationName(_4e8,ns);if(impl){var ret=_4e9?new impl(_4e9):new impl();return ret;}};function buildPrefixCache(){for(var _4ee in dojo.render){if(dojo.render[_4ee]["capable"]===true){var _4ef=dojo.render[_4ee].prefixes;for(var i=0;i<_4ef.length;i++){_4cb.push(_4ef[i].toLowerCase());}}}}
var _4f1=function(_4f2,_4f3){if(!_4f3){return null;}
for(var i=0,l=_4cb.length,_4f6;i<=l;i++){_4f6=(i<l?_4f3[_4cb[i]]:_4f3);if(!_4f6){continue;}
for(var name in _4f6){if(name.toLowerCase()==_4f2){return _4f6[name];}}}
return null;};var _4f8=function(_4f9,_4fa){var _4fb=dojo.evalObjPath(_4fa,false);return (_4fb?_4f1(_4f9,_4fb):null);};this.getImplementationName=function(_4fc,ns){var _4fe=_4fc.toLowerCase();ns=ns||"dojo";var imps=_4e3[ns]||(_4e3[ns]={});var impl=imps[_4fe];if(impl){return impl;}
if(!_4cb.length){buildPrefixCache();}
var _501=dojo.ns.get(ns);if(!_501){dojo.ns.register(ns,ns+".widget");_501=dojo.ns.get(ns);}
if(_501){_501.resolve(_4fc);}
impl=_4f8(_4fe,_501.module);if(impl){return (imps[_4fe]=impl);}
_501=dojo.ns.require(ns);if((_501)&&(_501.resolver)){_501.resolve(_4fc);impl=_4f8(_4fe,_501.module);if(impl){return (imps[_4fe]=impl);}}
dojo.deprecated("dojo.widget.Manager.getImplementationName","Could not locate widget implementation for \""+_4fc+"\" in \""+_501.module+"\" registered to namespace \""+_501.name+"\". "+"Developers must specify correct namespaces for all non-Dojo widgets","0.5");for(var i=0;i<_4e4.length;i++){impl=_4f8(_4fe,_4e4[i]);if(impl){return (imps[_4fe]=impl);}}
throw new Error("Could not locate widget implementation for \""+_4fc+"\" in \""+_501.module+"\" registered to namespace \""+_501.name+"\"");};this.resizing=false;this.onWindowResized=function(){if(this.resizing){return;}
try{this.resizing=true;for(var id in this.topWidgets){var _504=this.topWidgets[id];if(_504.checkSize){_504.checkSize();}}}
catch(e){}
finally{this.resizing=false;}};if(typeof window!="undefined"){dojo.addOnLoad(this,"onWindowResized");dojo.event.connect(window,"onresize",this,"onWindowResized");}};(function(){var dw=dojo.widget;var dwm=dw.manager;var h=dojo.lang.curry(dojo.lang,"hitch",dwm);var g=function(_509,_50a){dw[(_50a||_509)]=h(_509);};g("add","addWidget");g("destroyAll","destroyAllWidgets");g("remove","removeWidget");g("removeById","removeWidgetById");g("getWidgetById");g("getWidgetById","byId");g("getWidgetsByType");g("getWidgetsByFilter");g("getWidgetsByType","byType");g("getWidgetsByFilter","byFilter");g("getWidgetByNode","byNode");dw.all=function(n){var _50c=dwm.getAllWidgets.apply(dwm,arguments);if(arguments.length>0){return _50c[n];}
return _50c;};g("registerWidgetPackage");g("getImplementation","getWidgetImplementation");g("getImplementationName","getWidgetImplementationName");dw.widgets=dwm.widgets;dw.widgetIds=dwm.widgetIds;dw.root=dwm.root;})();dojo.provide("dojo.uri.Uri");dojo.uri=new function(){var _50d=new RegExp("^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$");var _50e=new RegExp("(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$");this.dojoUri=function(uri){return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri(),uri);};this.moduleUri=function(_510,uri){var loc=dojo.hostenv.getModuleSymbols(_510).join("/");if(!loc){return null;}
if(loc.lastIndexOf("/")!=loc.length-1){loc+="/";}
return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri()+loc,uri);};this.Uri=function(){var uri=arguments[0];for(var i=1;i<arguments.length;i++){if(!arguments[i]){continue;}
var _515=new dojo.uri.Uri(arguments[i].toString());var _516=new dojo.uri.Uri(uri.toString());if((_515.path=="")&&(_515.scheme==null)&&(_515.authority==null)&&(_515.query==null)){if(_515.fragment!=null){_516.fragment=_515.fragment;}
_515=_516;}
if(_515.scheme!=null&&_515.authority!=null){uri="";}
if(_515.scheme!=null){uri+=_515.scheme+":";}
if(_515.authority!=null){uri+="//"+_515.authority;}
uri+=_515.path;if(_515.query!=null){uri+="?"+_515.query;}
if(_515.fragment!=null){uri+="#"+_515.fragment;}}
this.uri=uri.toString();var r=this.uri.match(_50e);this.scheme=r[2]||(r[1]?"":null);this.authority=r[4]||(r[3]?"":null);this.path=r[5];this.query=r[7]||(r[6]?"":null);this.fragment=r[9]||(r[8]?"":null);if(this.authority!=null){r=this.authority.match(_50d);this.user=r[3]||null;this.password=r[4]||null;this.host=r[5];this.port=r[7]||null;}
this.toString=function(){return this.uri;};};};dojo.provide("dojo.uri.*");dojo.provide("dojo.html.common");dojo.lang.mixin(dojo.html,dojo.dom);dojo.html.body=function(){dojo.deprecated("dojo.html.body() moved to dojo.body()","0.5");return dojo.body();};dojo.html.getEventTarget=function(evt){if(!evt){evt=dojo.global().event||{};}
var t=(evt.srcElement?evt.srcElement:(evt.target?evt.target:null));while((t)&&(t.nodeType!=1)){t=t.parentNode;}
return t;};dojo.html.getViewport=function(){var _51a=dojo.global();var _51b=dojo.doc();var w=0;var h=0;if(dojo.render.html.mozilla){w=_51b.documentElement.clientWidth;h=_51a.innerHeight;}else{if(!dojo.render.html.opera&&_51a.innerWidth){w=_51a.innerWidth;h=_51a.innerHeight;}else{if(!dojo.render.html.opera&&dojo.exists(_51b,"documentElement.clientWidth")){var w2=_51b.documentElement.clientWidth;if(!w||w2&&w2<w){w=w2;}
h=_51b.documentElement.clientHeight;}else{if(dojo.body().clientWidth){w=dojo.body().clientWidth;h=dojo.body().clientHeight;}}}}
return {width:w,height:h};};dojo.html.getScroll=function(){var _51f=dojo.global();var _520=dojo.doc();var top=_51f.pageYOffset||_520.documentElement.scrollTop||dojo.body().scrollTop||0;var left=_51f.pageXOffset||_520.documentElement.scrollLeft||dojo.body().scrollLeft||0;return {top:top,left:left,offset:{x:left,y:top}};};dojo.html.getParentByType=function(node,type){var _525=dojo.doc();var _526=dojo.byId(node);type=type.toLowerCase();while((_526)&&(_526.nodeName.toLowerCase()!=type)){if(_526==(_525["body"]||_525["documentElement"])){return null;}
_526=_526.parentNode;}
return _526;};dojo.html.getAttribute=function(node,attr){node=dojo.byId(node);if((!node)||(!node.getAttribute)){return null;}
var ta=typeof attr=="string"?attr:new String(attr);var v=node.getAttribute(ta.toUpperCase());if((v)&&(typeof v=="string")&&(v!="")){return v;}
if(v&&v.value){return v.value;}
if((node.getAttributeNode)&&(node.getAttributeNode(ta))){return (node.getAttributeNode(ta)).value;}else{if(node.getAttribute(ta)){return node.getAttribute(ta);}else{if(node.getAttribute(ta.toLowerCase())){return node.getAttribute(ta.toLowerCase());}}}
return null;};dojo.html.hasAttribute=function(node,attr){return dojo.html.getAttribute(dojo.byId(node),attr)?true:false;};dojo.html.getCursorPosition=function(e){e=e||dojo.global().event;var _52e={x:0,y:0};if(e.pageX||e.pageY){_52e.x=e.pageX;_52e.y=e.pageY;}else{var de=dojo.doc().documentElement;var db=dojo.body();_52e.x=e.clientX+((de||db)["scrollLeft"])-((de||db)["clientLeft"]);_52e.y=e.clientY+((de||db)["scrollTop"])-((de||db)["clientTop"]);}
return _52e;};dojo.html.isTag=function(node){node=dojo.byId(node);if(node&&node.tagName){for(var i=1;i<arguments.length;i++){if(node.tagName.toLowerCase()==String(arguments[i]).toLowerCase()){return String(arguments[i]).toLowerCase();}}}
return "";};if(dojo.render.html.ie&&!dojo.render.html.ie70){if(window.location.href.substr(0,6).toLowerCase()!="https:"){(function(){var _533=dojo.doc().createElement("script");_533.src="javascript:'dojo.html.createExternalElement=function(doc, tag){ return doc.createElement(tag); }'";dojo.doc().getElementsByTagName("head")[0].appendChild(_533);})();}}else{dojo.html.createExternalElement=function(doc,tag){return doc.createElement(tag);};}
dojo.html._callDeprecated=function(_536,_537,args,_539,_53a){dojo.deprecated("dojo.html."+_536,"replaced by dojo.html."+_537+"("+(_539?"node, {"+_539+": "+_539+"}":"")+")"+(_53a?"."+_53a:""),"0.5");var _53b=[];if(_539){var _53c={};_53c[_539]=args[1];_53b.push(args[0]);_53b.push(_53c);}else{_53b=args;}
var ret=dojo.html[_537].apply(dojo.html,args);if(_53a){return ret[_53a];}else{return ret;}};dojo.html.getViewportWidth=function(){return dojo.html._callDeprecated("getViewportWidth","getViewport",arguments,null,"width");};dojo.html.getViewportHeight=function(){return dojo.html._callDeprecated("getViewportHeight","getViewport",arguments,null,"height");};dojo.html.getViewportSize=function(){return dojo.html._callDeprecated("getViewportSize","getViewport",arguments);};dojo.html.getScrollTop=function(){return dojo.html._callDeprecated("getScrollTop","getScroll",arguments,null,"top");};dojo.html.getScrollLeft=function(){return dojo.html._callDeprecated("getScrollLeft","getScroll",arguments,null,"left");};dojo.html.getScrollOffset=function(){return dojo.html._callDeprecated("getScrollOffset","getScroll",arguments,null,"offset");};dojo.provide("dojo.a11y");dojo.a11y={imgPath:dojo.uri.dojoUri("src/widget/templates/images"),doAccessibleCheck:true,accessible:null,checkAccessible:function(){if(this.accessible===null){this.accessible=false;if(this.doAccessibleCheck==true){this.accessible=this.testAccessible();}}
return this.accessible;},testAccessible:function(){this.accessible=false;if(dojo.render.html.ie||dojo.render.html.mozilla){var div=document.createElement("div");div.style.backgroundImage="url(\""+this.imgPath+"/tab_close.gif\")";dojo.body().appendChild(div);var _53f=null;if(window.getComputedStyle){var _540=getComputedStyle(div,"");_53f=_540.getPropertyValue("background-image");}else{_53f=div.currentStyle.backgroundImage;}
var _541=false;if(_53f!=null&&(_53f=="none"||_53f=="url(invalid-url:)")){this.accessible=true;}
dojo.body().removeChild(div);}
return this.accessible;},setCheckAccessible:function(_542){this.doAccessibleCheck=_542;},setAccessibleMode:function(){if(this.accessible===null){if(this.checkAccessible()){dojo.render.html.prefixes.unshift("a11y");}}
return this.accessible;}};dojo.provide("dojo.widget.Widget");dojo.declare("dojo.widget.Widget",null,function(){this.children=[];this.extraArgs={};},{parent:null,isTopLevel:false,disabled:false,isContainer:false,widgetId:"",widgetType:"Widget",ns:"dojo",getNamespacedType:function(){return (this.ns?this.ns+":"+this.widgetType:this.widgetType).toLowerCase();},toString:function(){return "[Widget "+this.getNamespacedType()+", "+(this.widgetId||"NO ID")+"]";},repr:function(){return this.toString();},enable:function(){this.disabled=false;},disable:function(){this.disabled=true;},onResized:function(){this.notifyChildrenOfResize();},notifyChildrenOfResize:function(){for(var i=0;i<this.children.length;i++){var _544=this.children[i];if(_544.onResized){_544.onResized();}}},create:function(args,_546,_547,ns){if(ns){this.ns=ns;}
this.satisfyPropertySets(args,_546,_547);this.mixInProperties(args,_546,_547);this.postMixInProperties(args,_546,_547);dojo.widget.manager.add(this);this.buildRendering(args,_546,_547);this.initialize(args,_546,_547);this.postInitialize(args,_546,_547);this.postCreate(args,_546,_547);return this;},destroy:function(_549){this.destroyChildren();this.uninitialize();this.destroyRendering(_549);dojo.widget.manager.removeById(this.widgetId);},destroyChildren:function(){var _54a;var i=0;while(this.children.length>i){_54a=this.children[i];if(_54a instanceof dojo.widget.Widget){this.removeChild(_54a);_54a.destroy();continue;}
i++;}},getChildrenOfType:function(type,_54d){var ret=[];var _54f=dojo.lang.isFunction(type);if(!_54f){type=type.toLowerCase();}
for(var x=0;x<this.children.length;x++){if(_54f){if(this.children[x] instanceof type){ret.push(this.children[x]);}}else{if(this.children[x].widgetType.toLowerCase()==type){ret.push(this.children[x]);}}
if(_54d){ret=ret.concat(this.children[x].getChildrenOfType(type,_54d));}}
return ret;},getDescendants:function(){var _551=[];var _552=[this];var elem;while((elem=_552.pop())){_551.push(elem);if(elem.children){dojo.lang.forEach(elem.children,function(elem){_552.push(elem);});}}
return _551;},isFirstChild:function(){return this===this.parent.children[0];},isLastChild:function(){return this===this.parent.children[this.parent.children.length-1];},satisfyPropertySets:function(args){return args;},mixInProperties:function(args,frag){if((args["fastMixIn"])||(frag["fastMixIn"])){for(var x in args){this[x]=args[x];}
return;}
var _559;var _55a=dojo.widget.lcArgsCache[this.widgetType];if(_55a==null){_55a={};for(var y in this){_55a[((new String(y)).toLowerCase())]=y;}
dojo.widget.lcArgsCache[this.widgetType]=_55a;}
var _55c={};for(var x in args){if(!this[x]){var y=_55a[(new String(x)).toLowerCase()];if(y){args[y]=args[x];x=y;}}
if(_55c[x]){continue;}
_55c[x]=true;if((typeof this[x])!=(typeof _559)){if(typeof args[x]!="string"){this[x]=args[x];}else{if(dojo.lang.isString(this[x])){this[x]=args[x];}else{if(dojo.lang.isNumber(this[x])){this[x]=new Number(args[x]);}else{if(dojo.lang.isBoolean(this[x])){this[x]=(args[x].toLowerCase()=="false")?false:true;}else{if(dojo.lang.isFunction(this[x])){if(args[x].search(/[^\w\.]+/i)==-1){this[x]=dojo.evalObjPath(args[x],false);}else{var tn=dojo.lang.nameAnonFunc(new Function(args[x]),this);dojo.event.kwConnect({srcObj:this,srcFunc:x,adviceObj:this,adviceFunc:tn});}}else{if(dojo.lang.isArray(this[x])){this[x]=args[x].split(";");}else{if(this[x] instanceof Date){this[x]=new Date(Number(args[x]));}else{if(typeof this[x]=="object"){if(this[x] instanceof dojo.uri.Uri){this[x]=args[x];}else{var _55e=args[x].split(";");for(var y=0;y<_55e.length;y++){var si=_55e[y].indexOf(":");if((si!=-1)&&(_55e[y].length>si)){this[x][_55e[y].substr(0,si).replace(/^\s+|\s+$/g,"")]=_55e[y].substr(si+1);}}}}else{this[x]=args[x];}}}}}}}}}else{this.extraArgs[x.toLowerCase()]=args[x];}}},postMixInProperties:function(args,frag,_562){},initialize:function(args,frag,_565){return false;},postInitialize:function(args,frag,_568){return false;},postCreate:function(args,frag,_56b){return false;},uninitialize:function(){return false;},buildRendering:function(args,frag,_56e){dojo.unimplemented("dojo.widget.Widget.buildRendering, on "+this.toString()+", ");return false;},destroyRendering:function(){dojo.unimplemented("dojo.widget.Widget.destroyRendering");return false;},addedTo:function(_56f){},addChild:function(_570){dojo.unimplemented("dojo.widget.Widget.addChild");return false;},removeChild:function(_571){for(var x=0;x<this.children.length;x++){if(this.children[x]===_571){this.children.splice(x,1);break;}}
return _571;},getPreviousSibling:function(){var idx=this.getParentIndex();if(idx<=0){return null;}
return this.parent.children[idx-1];},getSiblings:function(){return this.parent.children;},getParentIndex:function(){return dojo.lang.indexOf(this.parent.children,this,true);},getNextSibling:function(){var idx=this.getParentIndex();if(idx==this.parent.children.length-1){return null;}
if(idx<0){return null;}
return this.parent.children[idx+1];}});dojo.widget.lcArgsCache={};dojo.widget.tags={};dojo.widget.tags.addParseTreeHandler=function(type){dojo.deprecated("addParseTreeHandler",". ParseTreeHandlers are now reserved for components. Any unfiltered DojoML tag without a ParseTreeHandler is assumed to be a widget","0.5");};dojo.widget.tags["dojo:propertyset"]=function(_576,_577,_578){var _579=_577.parseProperties(_576["dojo:propertyset"]);};dojo.widget.tags["dojo:connect"]=function(_57a,_57b,_57c){var _57d=_57b.parseProperties(_57a["dojo:connect"]);};dojo.widget.buildWidgetFromParseTree=function(type,frag,_580,_581,_582,_583){dojo.a11y.setAccessibleMode();var _584=type.split(":");_584=(_584.length==2)?_584[1]:type;var _585=_583||_580.parseProperties(frag[frag["ns"]+":"+_584]);var _586=dojo.widget.manager.getImplementation(_584,null,null,frag["ns"]);if(!_586){throw new Error("cannot find \""+type+"\" widget");}else{if(!_586.create){throw new Error("\""+type+"\" widget object has no \"create\" method and does not appear to implement *Widget");}}
_585["dojoinsertionindex"]=_582;var ret=_586.create(_585,frag,_581,frag["ns"]);return ret;};dojo.widget.defineWidget=function(_588,_589,_58a,init,_58c){if(dojo.lang.isString(arguments[3])){dojo.widget._defineWidget(arguments[0],arguments[3],arguments[1],arguments[4],arguments[2]);}else{var args=[arguments[0]],p=3;if(dojo.lang.isString(arguments[1])){args.push(arguments[1],arguments[2]);}else{args.push("",arguments[1]);p=2;}
if(dojo.lang.isFunction(arguments[p])){args.push(arguments[p],arguments[p+1]);}else{args.push(null,arguments[p]);}
dojo.widget._defineWidget.apply(this,args);}};dojo.widget.defineWidget.renderers="html|svg|vml";dojo.widget._defineWidget=function(_58f,_590,_591,init,_593){var _594=_58f.split(".");var type=_594.pop();var regx="\\.("+(_590?_590+"|":"")+dojo.widget.defineWidget.renderers+")\\.";var r=_58f.search(new RegExp(regx));_594=(r<0?_594.join("."):_58f.substr(0,r));dojo.widget.manager.registerWidgetPackage(_594);var pos=_594.indexOf(".");var _599=(pos>-1)?_594.substring(0,pos):_594;_593=(_593)||{};_593.widgetType=type;if((!init)&&(_593["classConstructor"])){init=_593.classConstructor;delete _593.classConstructor;}
dojo.declare(_58f,_591,init,_593);};dojo.provide("dojo.widget.Parse");dojo.widget.Parse=function(_59a){this.propertySetsList=[];this.fragment=_59a;this.createComponents=function(frag,_59c){var _59d=[];var _59e=false;try{if(frag&&frag.tagName&&(frag!=frag.nodeRef)){var _59f=dojo.widget.tags;var tna=String(frag.tagName).split(";");for(var x=0;x<tna.length;x++){var ltn=tna[x].replace(/^\s+|\s+$/g,"").toLowerCase();frag.tagName=ltn;var ret;if(_59f[ltn]){_59e=true;ret=_59f[ltn](frag,this,_59c,frag.index);_59d.push(ret);}else{if(ltn.indexOf(":")==-1){ltn="dojo:"+ltn;}
ret=dojo.widget.buildWidgetFromParseTree(ltn,frag,this,_59c,frag.index);if(ret){_59e=true;_59d.push(ret);}}}}}
catch(e){dojo.debug("dojo.widget.Parse: error:",e);}
if(!_59e){_59d=_59d.concat(this.createSubComponents(frag,_59c));}
return _59d;};this.createSubComponents=function(_5a4,_5a5){var frag,_5a7=[];for(var item in _5a4){frag=_5a4[item];if(frag&&typeof frag=="object"&&(frag!=_5a4.nodeRef)&&(frag!=_5a4.tagName)&&(!dojo.dom.isNode(frag))){_5a7=_5a7.concat(this.createComponents(frag,_5a5));}}
return _5a7;};this.parsePropertySets=function(_5a9){return [];};this.parseProperties=function(_5aa){var _5ab={};for(var item in _5aa){if((_5aa[item]==_5aa.tagName)||(_5aa[item]==_5aa.nodeRef)){}else{var frag=_5aa[item];if(frag.tagName&&dojo.widget.tags[frag.tagName.toLowerCase()]){}else{if(frag[0]&&frag[0].value!=""&&frag[0].value!=null){try{if(item.toLowerCase()=="dataprovider"){var _5ae=this;this.getDataProvider(_5ae,frag[0].value);_5ab.dataProvider=this.dataProvider;}
_5ab[item]=frag[0].value;var _5af=this.parseProperties(frag);for(var _5b0 in _5af){_5ab[_5b0]=_5af[_5b0];}}
catch(e){dojo.debug(e);}}}
switch(item.toLowerCase()){case "checked":
case "disabled":
if(typeof _5ab[item]!="boolean"){_5ab[item]=true;}
break;}}}
return _5ab;};this.getDataProvider=function(_5b1,_5b2){dojo.io.bind({url:_5b2,load:function(type,_5b4){if(type=="load"){_5b1.dataProvider=_5b4;}},mimetype:"text/javascript",sync:true});};this.getPropertySetById=function(_5b5){for(var x=0;x<this.propertySetsList.length;x++){if(_5b5==this.propertySetsList[x]["id"][0].value){return this.propertySetsList[x];}}
return "";};this.getPropertySetsByType=function(_5b7){var _5b8=[];for(var x=0;x<this.propertySetsList.length;x++){var cpl=this.propertySetsList[x];var cpcc=cpl.componentClass||cpl.componentType||null;var _5bc=this.propertySetsList[x]["id"][0].value;if(cpcc&&(_5bc==cpcc[0].value)){_5b8.push(cpl);}}
return _5b8;};this.getPropertySets=function(_5bd){var ppl="dojo:propertyproviderlist";var _5bf=[];var _5c0=_5bd.tagName;if(_5bd[ppl]){var _5c1=_5bd[ppl].value.split(" ");for(var _5c2 in _5c1){if((_5c2.indexOf("..")==-1)&&(_5c2.indexOf("://")==-1)){var _5c3=this.getPropertySetById(_5c2);if(_5c3!=""){_5bf.push(_5c3);}}else{}}}
return this.getPropertySetsByType(_5c0).concat(_5bf);};this.createComponentFromScript=function(_5c4,_5c5,_5c6,ns){_5c6.fastMixIn=true;var ltn=(ns||"dojo")+":"+_5c5.toLowerCase();if(dojo.widget.tags[ltn]){return [dojo.widget.tags[ltn](_5c6,this,null,null,_5c6)];}
return [dojo.widget.buildWidgetFromParseTree(ltn,_5c6,this,null,null,_5c6)];};};dojo.widget._parser_collection={"dojo":new dojo.widget.Parse()};dojo.widget.getParser=function(name){if(!name){name="dojo";}
if(!this._parser_collection[name]){this._parser_collection[name]=new dojo.widget.Parse();}
return this._parser_collection[name];};dojo.widget.createWidget=function(name,_5cb,_5cc,_5cd){var _5ce=false;var _5cf=(typeof name=="string");if(_5cf){var pos=name.indexOf(":");var ns=(pos>-1)?name.substring(0,pos):"dojo";if(pos>-1){name=name.substring(pos+1);}
var _5d2=name.toLowerCase();var _5d3=ns+":"+_5d2;_5ce=(dojo.byId(name)&&!dojo.widget.tags[_5d3]);}
if((arguments.length==1)&&(_5ce||!_5cf)){var xp=new dojo.xml.Parse();var tn=_5ce?dojo.byId(name):name;return dojo.widget.getParser().createComponents(xp.parseElement(tn,null,true))[0];}
function fromScript(_5d6,name,_5d8,ns){_5d8[_5d3]={dojotype:[{value:_5d2}],nodeRef:_5d6,fastMixIn:true};_5d8.ns=ns;return dojo.widget.getParser().createComponentFromScript(_5d6,name,_5d8,ns);}
_5cb=_5cb||{};var _5da=false;var tn=null;var h=dojo.render.html.capable;if(h){tn=document.createElement("span");}
if(!_5cc){_5da=true;_5cc=tn;if(h){dojo.body().appendChild(_5cc);}}else{if(_5cd){dojo.dom.insertAtPosition(tn,_5cc,_5cd);}else{tn=_5cc;}}
var _5dc=fromScript(tn,name.toLowerCase(),_5cb,ns);if((!_5dc)||(!_5dc[0])||(typeof _5dc[0].widgetType=="undefined")){throw new Error("createWidget: Creation of \""+name+"\" widget failed.");}
try{if(_5da&&_5dc[0].domNode.parentNode){_5dc[0].domNode.parentNode.removeChild(_5dc[0].domNode);}}
catch(e){dojo.debug(e);}
return _5dc[0];};dojo.provide("dojo.io.*");dojo.provide("dojo.html.style");dojo.html.getClass=function(node){node=dojo.byId(node);if(!node){return "";}
var cs="";if(node.className){cs=node.className;}else{if(dojo.html.hasAttribute(node,"class")){cs=dojo.html.getAttribute(node,"class");}}
return cs.replace(/^\s+|\s+$/g,"");};dojo.html.getClasses=function(node){var c=dojo.html.getClass(node);return (c=="")?[]:c.split(/\s+/g);};dojo.html.hasClass=function(node,_5e2){return (new RegExp("(^|\\s+)"+_5e2+"(\\s+|$)")).test(dojo.html.getClass(node));};dojo.html.prependClass=function(node,_5e4){_5e4+=" "+dojo.html.getClass(node);return dojo.html.setClass(node,_5e4);};dojo.html.addClass=function(node,_5e6){if(dojo.html.hasClass(node,_5e6)){return false;}
_5e6=(dojo.html.getClass(node)+" "+_5e6).replace(/^\s+|\s+$/g,"");return dojo.html.setClass(node,_5e6);};dojo.html.setClass=function(node,_5e8){node=dojo.byId(node);var cs=new String(_5e8);try{if(typeof node.className=="string"){node.className=cs;}else{if(node.setAttribute){node.setAttribute("class",_5e8);node.className=cs;}else{return false;}}}
catch(e){dojo.debug("dojo.html.setClass() failed",e);}
return true;};dojo.html.removeClass=function(node,_5eb,_5ec){try{if(!_5ec){var _5ed=dojo.html.getClass(node).replace(new RegExp("(^|\\s+)"+_5eb+"(\\s+|$)"),"$1$2");}else{var _5ed=dojo.html.getClass(node).replace(_5eb,"");}
dojo.html.setClass(node,_5ed);}
catch(e){dojo.debug("dojo.html.removeClass() failed",e);}
return true;};dojo.html.replaceClass=function(node,_5ef,_5f0){dojo.html.removeClass(node,_5f0);dojo.html.addClass(node,_5ef);};dojo.html.classMatchType={ContainsAll:0,ContainsAny:1,IsOnly:2};dojo.html.getElementsByClass=function(_5f1,_5f2,_5f3,_5f4,_5f5){_5f5=false;var _5f6=dojo.doc();_5f2=dojo.byId(_5f2)||_5f6;var _5f7=_5f1.split(/\s+/g);var _5f8=[];if(_5f4!=1&&_5f4!=2){_5f4=0;}
var _5f9=new RegExp("(\\s|^)(("+_5f7.join(")|(")+"))(\\s|$)");var _5fa=_5f7.join(" ").length;var _5fb=[];if(!_5f5&&_5f6.evaluate){var _5fc=".//"+(_5f3||"*")+"[contains(";if(_5f4!=dojo.html.classMatchType.ContainsAny){_5fc+="concat(' ',@class,' '), ' "+_5f7.join(" ') and contains(concat(' ',@class,' '), ' ")+" ')";if(_5f4==2){_5fc+=" and string-length(@class)="+_5fa+"]";}else{_5fc+="]";}}else{_5fc+="concat(' ',@class,' '), ' "+_5f7.join(" ') or contains(concat(' ',@class,' '), ' ")+" ')]";}
var _5fd=_5f6.evaluate(_5fc,_5f2,null,XPathResult.ANY_TYPE,null);var _5fe=_5fd.iterateNext();while(_5fe){try{_5fb.push(_5fe);_5fe=_5fd.iterateNext();}
catch(e){break;}}
return _5fb;}else{if(!_5f3){_5f3="*";}
_5fb=_5f2.getElementsByTagName(_5f3);var node,i=0;outer:
while(node=_5fb[i++]){var _601=dojo.html.getClasses(node);if(_601.length==0){continue outer;}
var _602=0;for(var j=0;j<_601.length;j++){if(_5f9.test(_601[j])){if(_5f4==dojo.html.classMatchType.ContainsAny){_5f8.push(node);continue outer;}else{_602++;}}else{if(_5f4==dojo.html.classMatchType.IsOnly){continue outer;}}}
if(_602==_5f7.length){if((_5f4==dojo.html.classMatchType.IsOnly)&&(_602==_601.length)){_5f8.push(node);}else{if(_5f4==dojo.html.classMatchType.ContainsAll){_5f8.push(node);}}}}
return _5f8;}};dojo.html.getElementsByClassName=dojo.html.getElementsByClass;dojo.html.toCamelCase=function(_604){var arr=_604.split("-"),cc=arr[0];for(var i=1;i<arr.length;i++){cc+=arr[i].charAt(0).toUpperCase()+arr[i].substring(1);}
return cc;};dojo.html.toSelectorCase=function(_608){return _608.replace(/([A-Z])/g,"-$1").toLowerCase();};dojo.html.getComputedStyle=function(node,_60a,_60b){node=dojo.byId(node);var _60a=dojo.html.toSelectorCase(_60a);var _60c=dojo.html.toCamelCase(_60a);if(!node||!node.style){return _60b;}else{if(document.defaultView&&dojo.html.isDescendantOf(node,node.ownerDocument)){try{var cs=document.defaultView.getComputedStyle(node,"");if(cs){return cs.getPropertyValue(_60a);}}
catch(e){if(node.style.getPropertyValue){return node.style.getPropertyValue(_60a);}else{return _60b;}}}else{if(node.currentStyle){return node.currentStyle[_60c];}}}
if(node.style.getPropertyValue){return node.style.getPropertyValue(_60a);}else{return _60b;}};dojo.html.getStyleProperty=function(node,_60f){node=dojo.byId(node);return (node&&node.style?node.style[dojo.html.toCamelCase(_60f)]:undefined);};dojo.html.getStyle=function(node,_611){var _612=dojo.html.getStyleProperty(node,_611);return (_612?_612:dojo.html.getComputedStyle(node,_611));};dojo.html.setStyle=function(node,_614,_615){node=dojo.byId(node);if(node&&node.style){var _616=dojo.html.toCamelCase(_614);node.style[_616]=_615;}};dojo.html.setStyleText=function(_617,text){try{_617.style.cssText=text;}
catch(e){_617.setAttribute("style",text);}};dojo.html.copyStyle=function(_619,_61a){if(!_61a.style.cssText){_619.setAttribute("style",_61a.getAttribute("style"));}else{_619.style.cssText=_61a.style.cssText;}
dojo.html.addClass(_619,dojo.html.getClass(_61a));};dojo.html.getUnitValue=function(node,_61c,_61d){var s=dojo.html.getComputedStyle(node,_61c);if((!s)||((s=="auto")&&(_61d))){return {value:0,units:"px"};}
var _61f=s.match(/(\-?[\d.]+)([a-z%]*)/i);if(!_61f){return dojo.html.getUnitValue.bad;}
return {value:Number(_61f[1]),units:_61f[2].toLowerCase()};};dojo.html.getUnitValue.bad={value:NaN,units:""};dojo.html.getPixelValue=function(node,_621,_622){var _623=dojo.html.getUnitValue(node,_621,_622);if(isNaN(_623.value)){return 0;}
if((_623.value)&&(_623.units!="px")){return NaN;}
return _623.value;};dojo.html.setPositivePixelValue=function(node,_625,_626){if(isNaN(_626)){return false;}
node.style[_625]=Math.max(0,_626)+"px";return true;};dojo.html.styleSheet=null;dojo.html.insertCssRule=function(_627,_628,_629){if(!dojo.html.styleSheet){if(document.createStyleSheet){dojo.html.styleSheet=document.createStyleSheet();}else{if(document.styleSheets[0]){dojo.html.styleSheet=document.styleSheets[0];}else{return null;}}}
if(arguments.length<3){if(dojo.html.styleSheet.cssRules){_629=dojo.html.styleSheet.cssRules.length;}else{if(dojo.html.styleSheet.rules){_629=dojo.html.styleSheet.rules.length;}else{return null;}}}
if(dojo.html.styleSheet.insertRule){var rule=_627+" { "+_628+" }";return dojo.html.styleSheet.insertRule(rule,_629);}else{if(dojo.html.styleSheet.addRule){return dojo.html.styleSheet.addRule(_627,_628,_629);}else{return null;}}};dojo.html.removeCssRule=function(_62b){if(!dojo.html.styleSheet){dojo.debug("no stylesheet defined for removing rules");return false;}
if(dojo.render.html.ie){if(!_62b){_62b=dojo.html.styleSheet.rules.length;dojo.html.styleSheet.removeRule(_62b);}}else{if(document.styleSheets[0]){if(!_62b){_62b=dojo.html.styleSheet.cssRules.length;}
dojo.html.styleSheet.deleteRule(_62b);}}
return true;};dojo.html._insertedCssFiles=[];dojo.html.insertCssFile=function(URI,doc,_62e,_62f){if(!URI){return;}
if(!doc){doc=document;}
var _630=dojo.hostenv.getText(URI,false,_62f);if(_630===null){return;}
_630=dojo.html.fixPathsInCssText(_630,URI);if(_62e){var idx=-1,node,ent=dojo.html._insertedCssFiles;for(var i=0;i<ent.length;i++){if((ent[i].doc==doc)&&(ent[i].cssText==_630)){idx=i;node=ent[i].nodeRef;break;}}
if(node){var _635=doc.getElementsByTagName("style");for(var i=0;i<_635.length;i++){if(_635[i]==node){return;}}
dojo.html._insertedCssFiles.shift(idx,1);}}
var _636=dojo.html.insertCssText(_630);dojo.html._insertedCssFiles.push({"doc":doc,"cssText":_630,"nodeRef":_636});if(_636&&djConfig.isDebug){_636.setAttribute("dbgHref",URI);}
return _636;};dojo.html.insertCssText=function(_637,doc,URI){if(!_637){return;}
if(!doc){doc=document;}
if(URI){_637=dojo.html.fixPathsInCssText(_637,URI);}
var _63a=doc.createElement("style");_63a.setAttribute("type","text/css");var head=doc.getElementsByTagName("head")[0];if(!head){dojo.debug("No head tag in document, aborting styles");return;}else{head.appendChild(_63a);}
if(_63a.styleSheet){_63a.styleSheet.cssText=_637;}else{var _63c=doc.createTextNode(_637);_63a.appendChild(_63c);}
return _63a;};dojo.html.fixPathsInCssText=function(_63d,URI){function iefixPathsInCssText(){var _63f=/AlphaImageLoader\(src\=['"]([\t\s\w()\/.\\'"-:#=&?~]*)['"]/;while(_640=_63f.exec(_63d)){url=_640[1].replace(_642,"$2");if(!_643.exec(url)){url=(new dojo.uri.Uri(URI,url).toString());}
str+=_63d.substring(0,_640.index)+"AlphaImageLoader(src='"+url+"'";_63d=_63d.substr(_640.index+_640[0].length);}
return str+_63d;}
if(!_63d||!URI){return;}
var _640,str="",url="";var _645=/url\(\s*([\t\s\w()\/.\\'"-:#=&?]+)\s*\)/;var _643=/(file|https?|ftps?):\/\//;var _642=/^[\s]*(['"]?)([\w()\/.\\'"-:#=&?]*)\1[\s]*?$/;if(dojo.render.html.ie55||dojo.render.html.ie60){_63d=iefixPathsInCssText();}
while(_640=_645.exec(_63d)){url=_640[1].replace(_642,"$2");if(!_643.exec(url)){url=(new dojo.uri.Uri(URI,url).toString());}
str+=_63d.substring(0,_640.index)+"url("+url+")";_63d=_63d.substr(_640.index+_640[0].length);}
return str+_63d;};dojo.html.setActiveStyleSheet=function(_646){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")){a.disabled=true;if(a.getAttribute("title")==_646){a.disabled=false;}}}};dojo.html.getActiveStyleSheet=function(){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")&&!a.disabled){return a.getAttribute("title");}}
return null;};dojo.html.getPreferredStyleSheet=function(){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("rel").indexOf("alt")==-1&&a.getAttribute("title")){return a.getAttribute("title");}}
return null;};dojo.html.applyBrowserClass=function(node){var drh=dojo.render.html;var _652={dj_ie:drh.ie,dj_ie55:drh.ie55,dj_ie6:drh.ie60,dj_ie7:drh.ie70,dj_iequirks:drh.ie&&drh.quirks,dj_opera:drh.opera,dj_opera8:drh.opera&&(Math.floor(dojo.render.version)==8),dj_opera9:drh.opera&&(Math.floor(dojo.render.version)==9),dj_khtml:drh.khtml,dj_safari:drh.safari,dj_gecko:drh.mozilla};for(var p in _652){if(_652[p]){dojo.html.addClass(node,p);}}};dojo.provide("dojo.widget.DomWidget");dojo.widget._cssFiles={};dojo.widget._cssStrings={};dojo.widget._templateCache={};dojo.widget.defaultStrings={dojoRoot:dojo.hostenv.getBaseScriptUri(),baseScriptUri:dojo.hostenv.getBaseScriptUri()};dojo.widget.fillFromTemplateCache=function(obj,_655,_656,_657){var _658=_655||obj.templatePath;var _659=dojo.widget._templateCache;if(!_658&&!obj["widgetType"]){do{var _65a="__dummyTemplate__"+dojo.widget._templateCache.dummyCount++;}while(_659[_65a]);obj.widgetType=_65a;}
var wt=_658?_658.toString():obj.widgetType;var ts=_659[wt];if(!ts){_659[wt]={"string":null,"node":null};if(_657){ts={};}else{ts=_659[wt];}}
if((!obj.templateString)&&(!_657)){obj.templateString=_656||ts["string"];}
if((!obj.templateNode)&&(!_657)){obj.templateNode=ts["node"];}
if((!obj.templateNode)&&(!obj.templateString)&&(_658)){var _65d=dojo.hostenv.getText(_658);if(_65d){_65d=_65d.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im,"");var _65e=_65d.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);if(_65e){_65d=_65e[1];}}else{_65d="";}
obj.templateString=_65d;if(!_657){_659[wt]["string"]=_65d;}}
if((!ts["string"])&&(!_657)){ts.string=obj.templateString;}};dojo.widget._templateCache.dummyCount=0;dojo.widget.attachProperties=["dojoAttachPoint","id"];dojo.widget.eventAttachProperty="dojoAttachEvent";dojo.widget.onBuildProperty="dojoOnBuild";dojo.widget.waiNames=["waiRole","waiState"];dojo.widget.wai={waiRole:{name:"waiRole","namespace":"http://www.w3.org/TR/xhtml2",alias:"x2",prefix:"wairole:"},waiState:{name:"waiState","namespace":"http://www.w3.org/2005/07/aaa",alias:"aaa",prefix:""},setAttr:function(node,ns,attr,_662){if(dojo.render.html.ie){node.setAttribute(this[ns].alias+":"+attr,this[ns].prefix+_662);}else{node.setAttributeNS(this[ns]["namespace"],attr,this[ns].prefix+_662);}},getAttr:function(node,ns,attr){if(dojo.render.html.ie){return node.getAttribute(this[ns].alias+":"+attr);}else{return node.getAttributeNS(this[ns]["namespace"],attr);}},removeAttr:function(node,ns,attr){var _669=true;if(dojo.render.html.ie){_669=node.removeAttribute(this[ns].alias+":"+attr);}else{node.removeAttributeNS(this[ns]["namespace"],attr);}
return _669;}};dojo.widget.attachTemplateNodes=function(_66a,_66b,_66c){var _66d=dojo.dom.ELEMENT_NODE;function trim(str){return str.replace(/^\s+|\s+$/g,"");}
if(!_66a){_66a=_66b.domNode;}
if(_66a.nodeType!=_66d){return;}
var _66f=_66a.all||_66a.getElementsByTagName("*");var _670=_66b;for(var x=-1;x<_66f.length;x++){var _672=(x==-1)?_66a:_66f[x];var _673=[];if(!_66b.widgetsInTemplate||!_672.getAttribute("dojoType")){for(var y=0;y<this.attachProperties.length;y++){var _675=_672.getAttribute(this.attachProperties[y]);if(_675){_673=_675.split(";");for(var z=0;z<_673.length;z++){if(dojo.lang.isArray(_66b[_673[z]])){_66b[_673[z]].push(_672);}else{_66b[_673[z]]=_672;}}
break;}}
var _677=_672.getAttribute(this.eventAttachProperty);if(_677){var evts=_677.split(";");for(var y=0;y<evts.length;y++){if((!evts[y])||(!evts[y].length)){continue;}
var _679=null;var tevt=trim(evts[y]);if(evts[y].indexOf(":")>=0){var _67b=tevt.split(":");tevt=trim(_67b[0]);_679=trim(_67b[1]);}
if(!_679){_679=tevt;}
var tf=function(){var ntf=new String(_679);return function(evt){if(_670[ntf]){_670[ntf](dojo.event.browser.fixEvent(evt,this));}};}();dojo.event.browser.addListener(_672,tevt,tf,false,true);}}
for(var y=0;y<_66c.length;y++){var _67f=_672.getAttribute(_66c[y]);if((_67f)&&(_67f.length)){var _679=null;var _680=_66c[y].substr(4);_679=trim(_67f);var _681=[_679];if(_679.indexOf(";")>=0){_681=dojo.lang.map(_679.split(";"),trim);}
for(var z=0;z<_681.length;z++){if(!_681[z].length){continue;}
var tf=function(){var ntf=new String(_681[z]);return function(evt){if(_670[ntf]){_670[ntf](dojo.event.browser.fixEvent(evt,this));}};}();dojo.event.browser.addListener(_672,_680,tf,false,true);}}}}
var _684=_672.getAttribute(this.templateProperty);if(_684){_66b[_684]=_672;}
dojo.lang.forEach(dojo.widget.waiNames,function(name){var wai=dojo.widget.wai[name];var val=_672.getAttribute(wai.name);if(val){if(val.indexOf("-")==-1){dojo.widget.wai.setAttr(_672,wai.name,"role",val);}else{var _688=val.split("-");dojo.widget.wai.setAttr(_672,wai.name,_688[0],_688[1]);}}},this);var _689=_672.getAttribute(this.onBuildProperty);if(_689){eval("var node = baseNode; var widget = targetObj; "+_689);}}};dojo.widget.getDojoEventsFromStr=function(str){var re=/(dojoOn([a-z]+)(\s?))=/gi;var evts=str?str.match(re)||[]:[];var ret=[];var lem={};for(var x=0;x<evts.length;x++){if(evts[x].length<1){continue;}
var cm=evts[x].replace(/\s/,"");cm=(cm.slice(0,cm.length-1));if(!lem[cm]){lem[cm]=true;ret.push(cm);}}
return ret;};dojo.declare("dojo.widget.DomWidget",dojo.widget.Widget,function(){if((arguments.length>0)&&(typeof arguments[0]=="object")){this.create(arguments[0]);}},{templateNode:null,templateString:null,templateCssString:null,preventClobber:false,domNode:null,containerNode:null,widgetsInTemplate:false,addChild:function(_691,_692,pos,ref,_695){if(!this.isContainer){dojo.debug("dojo.widget.DomWidget.addChild() attempted on non-container widget");return null;}else{if(_695==undefined){_695=this.children.length;}
this.addWidgetAsDirectChild(_691,_692,pos,ref,_695);this.registerChild(_691,_695);}
return _691;},addWidgetAsDirectChild:function(_696,_697,pos,ref,_69a){if((!this.containerNode)&&(!_697)){this.containerNode=this.domNode;}
var cn=(_697)?_697:this.containerNode;if(!pos){pos="after";}
if(!ref){if(!cn){cn=dojo.body();}
ref=cn.lastChild;}
if(!_69a){_69a=0;}
_696.domNode.setAttribute("dojoinsertionindex",_69a);if(!ref){cn.appendChild(_696.domNode);}else{if(pos=="insertAtIndex"){dojo.dom.insertAtIndex(_696.domNode,ref.parentNode,_69a);}else{if((pos=="after")&&(ref===cn.lastChild)){cn.appendChild(_696.domNode);}else{dojo.dom.insertAtPosition(_696.domNode,cn,pos);}}}},registerChild:function(_69c,_69d){_69c.dojoInsertionIndex=_69d;var idx=-1;for(var i=0;i<this.children.length;i++){if(this.children[i].dojoInsertionIndex<=_69d){idx=i;}}
this.children.splice(idx+1,0,_69c);_69c.parent=this;_69c.addedTo(this,idx+1);delete dojo.widget.manager.topWidgets[_69c.widgetId];},removeChild:function(_6a0){dojo.dom.removeNode(_6a0.domNode);return dojo.widget.DomWidget.superclass.removeChild.call(this,_6a0);},getFragNodeRef:function(frag){if(!frag){return null;}
if(!frag[this.getNamespacedType()]){dojo.raise("Error: no frag for widget type "+this.getNamespacedType()+", id "+this.widgetId+" (maybe a widget has set it's type incorrectly)");}
return frag[this.getNamespacedType()]["nodeRef"];},postInitialize:function(args,frag,_6a4){var _6a5=this.getFragNodeRef(frag);if(_6a4&&(_6a4.snarfChildDomOutput||!_6a5)){_6a4.addWidgetAsDirectChild(this,"","insertAtIndex","",args["dojoinsertionindex"],_6a5);}else{if(_6a5){if(this.domNode&&(this.domNode!==_6a5)){var _6a6=_6a5.parentNode.replaceChild(this.domNode,_6a5);}}}
if(_6a4){_6a4.registerChild(this,args.dojoinsertionindex);}else{dojo.widget.manager.topWidgets[this.widgetId]=this;}
if(this.widgetsInTemplate){var _6a7=new dojo.xml.Parse();var _6a8;var _6a9=this.domNode.getElementsByTagName("*");for(var i=0;i<_6a9.length;i++){if(_6a9[i].getAttribute("dojoAttachPoint")=="subContainerWidget"){_6a8=_6a9[i];}
if(_6a9[i].getAttribute("dojoType")){_6a9[i].setAttribute("_isSubWidget",true);}}
if(this.isContainer&&!this.containerNode){if(_6a8){var src=this.getFragNodeRef(frag);if(src){dojo.dom.moveChildren(src,_6a8);frag["dojoDontFollow"]=true;}}else{dojo.debug("No subContainerWidget node can be found in template file for widget "+this);}}
var _6ac=_6a7.parseElement(this.domNode,null,true);dojo.widget.getParser().createSubComponents(_6ac,this);var _6ad=[];var _6ae=[this];var w;while((w=_6ae.pop())){for(var i=0;i<w.children.length;i++){var _6b0=w.children[i];if(_6b0._processedSubWidgets||!_6b0.extraArgs["_issubwidget"]){continue;}
_6ad.push(_6b0);if(_6b0.isContainer){_6ae.push(_6b0);}}}
for(var i=0;i<_6ad.length;i++){var _6b1=_6ad[i];if(_6b1._processedSubWidgets){dojo.debug("This should not happen: widget._processedSubWidgets is already true!");return;}
_6b1._processedSubWidgets=true;if(_6b1.extraArgs["dojoattachevent"]){var evts=_6b1.extraArgs["dojoattachevent"].split(";");for(var j=0;j<evts.length;j++){var _6b4=null;var tevt=dojo.string.trim(evts[j]);if(tevt.indexOf(":")>=0){var _6b6=tevt.split(":");tevt=dojo.string.trim(_6b6[0]);_6b4=dojo.string.trim(_6b6[1]);}
if(!_6b4){_6b4=tevt;}
if(dojo.lang.isFunction(_6b1[tevt])){dojo.event.kwConnect({srcObj:_6b1,srcFunc:tevt,targetObj:this,targetFunc:_6b4});}else{alert(tevt+" is not a function in widget "+_6b1);}}}
if(_6b1.extraArgs["dojoattachpoint"]){this[_6b1.extraArgs["dojoattachpoint"]]=_6b1;}}}
if(this.isContainer&&!frag["dojoDontFollow"]){dojo.widget.getParser().createSubComponents(frag,this);}},buildRendering:function(args,frag){var ts=dojo.widget._templateCache[this.widgetType];if(args["templatecsspath"]){args["templateCssPath"]=args["templatecsspath"];}
var _6ba=args["templateCssPath"]||this.templateCssPath;if(_6ba&&!dojo.widget._cssFiles[_6ba.toString()]){if((!this.templateCssString)&&(_6ba)){this.templateCssString=dojo.hostenv.getText(_6ba);this.templateCssPath=null;}
dojo.widget._cssFiles[_6ba.toString()]=true;}
if((this["templateCssString"])&&(!this.templateCssString["loaded"])){dojo.html.insertCssText(this.templateCssString,null,_6ba);if(!this.templateCssString){this.templateCssString="";}
this.templateCssString.loaded=true;}
if((!this.preventClobber)&&((this.templatePath)||(this.templateNode)||((this["templateString"])&&(this.templateString.length))||((typeof ts!="undefined")&&((ts["string"])||(ts["node"]))))){this.buildFromTemplate(args,frag);}else{this.domNode=this.getFragNodeRef(frag);}
this.fillInTemplate(args,frag);},buildFromTemplate:function(args,frag){var _6bd=false;if(args["templatepath"]){args["templatePath"]=args["templatepath"];}
dojo.widget.fillFromTemplateCache(this,args["templatePath"],null,_6bd);var ts=dojo.widget._templateCache[this.templatePath?this.templatePath.toString():this.widgetType];if((ts)&&(!_6bd)){if(!this.templateString.length){this.templateString=ts["string"];}
if(!this.templateNode){this.templateNode=ts["node"];}}
var _6bf=false;var node=null;var tstr=this.templateString;if((!this.templateNode)&&(this.templateString)){_6bf=this.templateString.match(/\$\{([^\}]+)\}/g);if(_6bf){var hash=this.strings||{};for(var key in dojo.widget.defaultStrings){if(dojo.lang.isUndefined(hash[key])){hash[key]=dojo.widget.defaultStrings[key];}}
for(var i=0;i<_6bf.length;i++){var key=_6bf[i];key=key.substring(2,key.length-1);var kval=(key.substring(0,5)=="this.")?dojo.lang.getObjPathValue(key.substring(5),this):hash[key];var _6c6;if((kval)||(dojo.lang.isString(kval))){_6c6=new String((dojo.lang.isFunction(kval))?kval.call(this,key,this.templateString):kval);while(_6c6.indexOf("\"")>-1){_6c6=_6c6.replace("\"","&quot;");}
tstr=tstr.replace(_6bf[i],_6c6);}}}else{this.templateNode=this.createNodesFromText(this.templateString,true)[0];if(!_6bd){ts.node=this.templateNode;}}}
if((!this.templateNode)&&(!_6bf)){dojo.debug("DomWidget.buildFromTemplate: could not create template");return false;}else{if(!_6bf){node=this.templateNode.cloneNode(true);if(!node){return false;}}else{node=this.createNodesFromText(tstr,true)[0];}}
this.domNode=node;this.attachTemplateNodes();if(this.isContainer&&this.containerNode){var src=this.getFragNodeRef(frag);if(src){dojo.dom.moveChildren(src,this.containerNode);}}},attachTemplateNodes:function(_6c8,_6c9){if(!_6c8){_6c8=this.domNode;}
if(!_6c9){_6c9=this;}
return dojo.widget.attachTemplateNodes(_6c8,_6c9,dojo.widget.getDojoEventsFromStr(this.templateString));},fillInTemplate:function(){},destroyRendering:function(){try{delete this.domNode;}
catch(e){}},createNodesFromText:function(){dojo.unimplemented("dojo.widget.DomWidget.createNodesFromText");}});dojo.provide("dojo.html.display");dojo.html._toggle=function(node,_6cb,_6cc){node=dojo.byId(node);_6cc(node,!_6cb(node));return _6cb(node);};dojo.html.show=function(node){node=dojo.byId(node);if(dojo.html.getStyleProperty(node,"display")=="none"){dojo.html.setStyle(node,"display",(node.dojoDisplayCache||""));node.dojoDisplayCache=undefined;}};dojo.html.hide=function(node){node=dojo.byId(node);if(typeof node["dojoDisplayCache"]=="undefined"){var d=dojo.html.getStyleProperty(node,"display");if(d!="none"){node.dojoDisplayCache=d;}}
dojo.html.setStyle(node,"display","none");};dojo.html.setShowing=function(node,_6d1){dojo.html[(_6d1?"show":"hide")](node);};dojo.html.isShowing=function(node){return (dojo.html.getStyleProperty(node,"display")!="none");};dojo.html.toggleShowing=function(node){return dojo.html._toggle(node,dojo.html.isShowing,dojo.html.setShowing);};dojo.html.displayMap={tr:"",td:"",th:"",img:"inline",span:"inline",input:"inline",button:"inline"};dojo.html.suggestDisplayByTagName=function(node){node=dojo.byId(node);if(node&&node.tagName){var tag=node.tagName.toLowerCase();return (tag in dojo.html.displayMap?dojo.html.displayMap[tag]:"block");}};dojo.html.setDisplay=function(node,_6d7){dojo.html.setStyle(node,"display",((_6d7 instanceof String||typeof _6d7=="string")?_6d7:(_6d7?dojo.html.suggestDisplayByTagName(node):"none")));};dojo.html.isDisplayed=function(node){return (dojo.html.getComputedStyle(node,"display")!="none");};dojo.html.toggleDisplay=function(node){return dojo.html._toggle(node,dojo.html.isDisplayed,dojo.html.setDisplay);};dojo.html.setVisibility=function(node,_6db){dojo.html.setStyle(node,"visibility",((_6db instanceof String||typeof _6db=="string")?_6db:(_6db?"visible":"hidden")));};dojo.html.isVisible=function(node){return (dojo.html.getComputedStyle(node,"visibility")!="hidden");};dojo.html.toggleVisibility=function(node){return dojo.html._toggle(node,dojo.html.isVisible,dojo.html.setVisibility);};dojo.html.setOpacity=function(node,_6df,_6e0){node=dojo.byId(node);var h=dojo.render.html;if(!_6e0){if(_6df>=1){if(h.ie){dojo.html.clearOpacity(node);return;}else{_6df=0.999999;}}else{if(_6df<0){_6df=0;}}}
if(h.ie){if(node.nodeName.toLowerCase()=="tr"){var tds=node.getElementsByTagName("td");for(var x=0;x<tds.length;x++){tds[x].style.filter="Alpha(Opacity="+_6df*100+")";}}
node.style.filter="Alpha(Opacity="+_6df*100+")";}else{if(h.moz){node.style.opacity=_6df;node.style.MozOpacity=_6df;}else{if(h.safari){node.style.opacity=_6df;node.style.KhtmlOpacity=_6df;}else{node.style.opacity=_6df;}}}};dojo.html.clearOpacity=function(node){node=dojo.byId(node);var ns=node.style;var h=dojo.render.html;if(h.ie){try{if(node.filters&&node.filters.alpha){ns.filter="";}}
catch(e){}}else{if(h.moz){ns.opacity=1;ns.MozOpacity=1;}else{if(h.safari){ns.opacity=1;ns.KhtmlOpacity=1;}else{ns.opacity=1;}}}};dojo.html.getOpacity=function(node){node=dojo.byId(node);var h=dojo.render.html;if(h.ie){var opac=(node.filters&&node.filters.alpha&&typeof node.filters.alpha.opacity=="number"?node.filters.alpha.opacity:100)/100;}else{var opac=node.style.opacity||node.style.MozOpacity||node.style.KhtmlOpacity||1;}
return opac>=0.999999?1:Number(opac);};dojo.provide("dojo.html.layout");dojo.html.sumAncestorProperties=function(node,prop){node=dojo.byId(node);if(!node){return 0;}
var _6ec=0;while(node){if(dojo.html.getComputedStyle(node,"position")=="fixed"){return 0;}
var val=node[prop];if(val){_6ec+=val-0;if(node==dojo.body()){break;}}
node=node.parentNode;}
return _6ec;};dojo.html.setStyleAttributes=function(node,_6ef){node=dojo.byId(node);var _6f0=_6ef.replace(/(;)?\s*$/,"").split(";");for(var i=0;i<_6f0.length;i++){var _6f2=_6f0[i].split(":");var name=_6f2[0].replace(/\s*$/,"").replace(/^\s*/,"").toLowerCase();var _6f4=_6f2[1].replace(/\s*$/,"").replace(/^\s*/,"");switch(name){case "opacity":
dojo.html.setOpacity(node,_6f4);break;case "content-height":
dojo.html.setContentBox(node,{height:_6f4});break;case "content-width":
dojo.html.setContentBox(node,{width:_6f4});break;case "outer-height":
dojo.html.setMarginBox(node,{height:_6f4});break;case "outer-width":
dojo.html.setMarginBox(node,{width:_6f4});break;default:
node.style[dojo.html.toCamelCase(name)]=_6f4;}}};dojo.html.boxSizing={MARGIN_BOX:"margin-box",BORDER_BOX:"border-box",PADDING_BOX:"padding-box",CONTENT_BOX:"content-box"};dojo.html.getAbsolutePosition=dojo.html.abs=function(node,_6f6,_6f7){node=dojo.byId(node,node.ownerDocument);var ret={x:0,y:0};var bs=dojo.html.boxSizing;if(!_6f7){_6f7=bs.CONTENT_BOX;}
var _6fa=2;var _6fb;switch(_6f7){case bs.MARGIN_BOX:
_6fb=3;break;case bs.BORDER_BOX:
_6fb=2;break;case bs.PADDING_BOX:
default:
_6fb=1;break;case bs.CONTENT_BOX:
_6fb=0;break;}
var h=dojo.render.html;var db=document["body"]||document["documentElement"];if(h.ie){with(node.getBoundingClientRect()){ret.x=left-2;ret.y=top-2;}}else{if(document.getBoxObjectFor){_6fa=1;try{var bo=document.getBoxObjectFor(node);ret.x=bo.x-dojo.html.sumAncestorProperties(node,"scrollLeft");ret.y=bo.y-dojo.html.sumAncestorProperties(node,"scrollTop");}
catch(e){}}else{if(node["offsetParent"]){var _6ff;if((h.safari)&&(node.style.getPropertyValue("position")=="absolute")&&(node.parentNode==db)){_6ff=db;}else{_6ff=db.parentNode;}
if(node.parentNode!=db){var nd=node;if(dojo.render.html.opera){nd=db;}
ret.x-=dojo.html.sumAncestorProperties(nd,"scrollLeft");ret.y-=dojo.html.sumAncestorProperties(nd,"scrollTop");}
var _701=node;do{var n=_701["offsetLeft"];if(!h.opera||n>0){ret.x+=isNaN(n)?0:n;}
var m=_701["offsetTop"];ret.y+=isNaN(m)?0:m;_701=_701.offsetParent;}while((_701!=_6ff)&&(_701!=null));}else{if(node["x"]&&node["y"]){ret.x+=isNaN(node.x)?0:node.x;ret.y+=isNaN(node.y)?0:node.y;}}}}
if(_6f6){var _704=dojo.html.getScroll();ret.y+=_704.top;ret.x+=_704.left;}
var _705=[dojo.html.getPaddingExtent,dojo.html.getBorderExtent,dojo.html.getMarginExtent];if(_6fa>_6fb){for(var i=_6fb;i<_6fa;++i){ret.y+=_705[i](node,"top");ret.x+=_705[i](node,"left");}}else{if(_6fa<_6fb){for(var i=_6fb;i>_6fa;--i){ret.y-=_705[i-1](node,"top");ret.x-=_705[i-1](node,"left");}}}
ret.top=ret.y;ret.left=ret.x;return ret;};dojo.html.isPositionAbsolute=function(node){return (dojo.html.getComputedStyle(node,"position")=="absolute");};dojo.html._sumPixelValues=function(node,_709,_70a){var _70b=0;for(var x=0;x<_709.length;x++){_70b+=dojo.html.getPixelValue(node,_709[x],_70a);}
return _70b;};dojo.html.getMargin=function(node){return {width:dojo.html._sumPixelValues(node,["margin-left","margin-right"],(dojo.html.getComputedStyle(node,"position")=="absolute")),height:dojo.html._sumPixelValues(node,["margin-top","margin-bottom"],(dojo.html.getComputedStyle(node,"position")=="absolute"))};};dojo.html.getBorder=function(node){return {width:dojo.html.getBorderExtent(node,"left")+dojo.html.getBorderExtent(node,"right"),height:dojo.html.getBorderExtent(node,"top")+dojo.html.getBorderExtent(node,"bottom")};};dojo.html.getBorderExtent=function(node,side){return (dojo.html.getStyle(node,"border-"+side+"-style")=="none"?0:dojo.html.getPixelValue(node,"border-"+side+"-width"));};dojo.html.getMarginExtent=function(node,side){return dojo.html._sumPixelValues(node,["margin-"+side],dojo.html.isPositionAbsolute(node));};dojo.html.getPaddingExtent=function(node,side){return dojo.html._sumPixelValues(node,["padding-"+side],true);};dojo.html.getPadding=function(node){return {width:dojo.html._sumPixelValues(node,["padding-left","padding-right"],true),height:dojo.html._sumPixelValues(node,["padding-top","padding-bottom"],true)};};dojo.html.getPadBorder=function(node){var pad=dojo.html.getPadding(node);var _718=dojo.html.getBorder(node);return {width:pad.width+_718.width,height:pad.height+_718.height};};dojo.html.getBoxSizing=function(node){var h=dojo.render.html;var bs=dojo.html.boxSizing;if((h.ie)||(h.opera)){var cm=document["compatMode"];if((cm=="BackCompat")||(cm=="QuirksMode")){return bs.BORDER_BOX;}else{return bs.CONTENT_BOX;}}else{if(arguments.length==0){node=document.documentElement;}
var _71d=dojo.html.getStyle(node,"-moz-box-sizing");if(!_71d){_71d=dojo.html.getStyle(node,"box-sizing");}
return (_71d?_71d:bs.CONTENT_BOX);}};dojo.html.isBorderBox=function(node){return (dojo.html.getBoxSizing(node)==dojo.html.boxSizing.BORDER_BOX);};dojo.html.getBorderBox=function(node){node=dojo.byId(node);return {width:node.offsetWidth,height:node.offsetHeight};};dojo.html.getPaddingBox=function(node){var box=dojo.html.getBorderBox(node);var _722=dojo.html.getBorder(node);return {width:box.width-_722.width,height:box.height-_722.height};};dojo.html.getContentBox=function(node){node=dojo.byId(node);var _724=dojo.html.getPadBorder(node);return {width:node.offsetWidth-_724.width,height:node.offsetHeight-_724.height};};dojo.html.setContentBox=function(node,args){node=dojo.byId(node);var _727=0;var _728=0;var isbb=dojo.html.isBorderBox(node);var _72a=(isbb?dojo.html.getPadBorder(node):{width:0,height:0});var ret={};if(typeof args.width!="undefined"){_727=args.width+_72a.width;ret.width=dojo.html.setPositivePixelValue(node,"width",_727);}
if(typeof args.height!="undefined"){_728=args.height+_72a.height;ret.height=dojo.html.setPositivePixelValue(node,"height",_728);}
return ret;};dojo.html.getMarginBox=function(node){var _72d=dojo.html.getBorderBox(node);var _72e=dojo.html.getMargin(node);return {width:_72d.width+_72e.width,height:_72d.height+_72e.height};};dojo.html.setMarginBox=function(node,args){node=dojo.byId(node);var _731=0;var _732=0;var isbb=dojo.html.isBorderBox(node);var _734=(!isbb?dojo.html.getPadBorder(node):{width:0,height:0});var _735=dojo.html.getMargin(node);var ret={};if(typeof args.width!="undefined"){_731=args.width-_734.width;_731-=_735.width;ret.width=dojo.html.setPositivePixelValue(node,"width",_731);}
if(typeof args.height!="undefined"){_732=args.height-_734.height;_732-=_735.height;ret.height=dojo.html.setPositivePixelValue(node,"height",_732);}
return ret;};dojo.html.getElementBox=function(node,type){var bs=dojo.html.boxSizing;switch(type){case bs.MARGIN_BOX:
return dojo.html.getMarginBox(node);case bs.BORDER_BOX:
return dojo.html.getBorderBox(node);case bs.PADDING_BOX:
return dojo.html.getPaddingBox(node);case bs.CONTENT_BOX:
default:
return dojo.html.getContentBox(node);}};dojo.html.toCoordinateObject=dojo.html.toCoordinateArray=function(_73a,_73b,_73c){if(_73a instanceof Array||typeof _73a=="array"){dojo.deprecated("dojo.html.toCoordinateArray","use dojo.html.toCoordinateObject({left: , top: , width: , height: }) instead","0.5");while(_73a.length<4){_73a.push(0);}
while(_73a.length>4){_73a.pop();}
var ret={left:_73a[0],top:_73a[1],width:_73a[2],height:_73a[3]};}else{if(!_73a.nodeType&&!(_73a instanceof String||typeof _73a=="string")&&("width" in _73a||"height" in _73a||"left" in _73a||"x" in _73a||"top" in _73a||"y" in _73a)){var ret={left:_73a.left||_73a.x||0,top:_73a.top||_73a.y||0,width:_73a.width||0,height:_73a.height||0};}else{var node=dojo.byId(_73a);var pos=dojo.html.abs(node,_73b,_73c);var _740=dojo.html.getMarginBox(node);var ret={left:pos.left,top:pos.top,width:_740.width,height:_740.height};}}
ret.x=ret.left;ret.y=ret.top;return ret;};dojo.html.setMarginBoxWidth=dojo.html.setOuterWidth=function(node,_742){return dojo.html._callDeprecated("setMarginBoxWidth","setMarginBox",arguments,"width");};dojo.html.setMarginBoxHeight=dojo.html.setOuterHeight=function(){return dojo.html._callDeprecated("setMarginBoxHeight","setMarginBox",arguments,"height");};dojo.html.getMarginBoxWidth=dojo.html.getOuterWidth=function(){return dojo.html._callDeprecated("getMarginBoxWidth","getMarginBox",arguments,null,"width");};dojo.html.getMarginBoxHeight=dojo.html.getOuterHeight=function(){return dojo.html._callDeprecated("getMarginBoxHeight","getMarginBox",arguments,null,"height");};dojo.html.getTotalOffset=function(node,type,_745){return dojo.html._callDeprecated("getTotalOffset","getAbsolutePosition",arguments,null,type);};dojo.html.getAbsoluteX=function(node,_747){return dojo.html._callDeprecated("getAbsoluteX","getAbsolutePosition",arguments,null,"x");};dojo.html.getAbsoluteY=function(node,_749){return dojo.html._callDeprecated("getAbsoluteY","getAbsolutePosition",arguments,null,"y");};dojo.html.totalOffsetLeft=function(node,_74b){return dojo.html._callDeprecated("totalOffsetLeft","getAbsolutePosition",arguments,null,"left");};dojo.html.totalOffsetTop=function(node,_74d){return dojo.html._callDeprecated("totalOffsetTop","getAbsolutePosition",arguments,null,"top");};dojo.html.getMarginWidth=function(node){return dojo.html._callDeprecated("getMarginWidth","getMargin",arguments,null,"width");};dojo.html.getMarginHeight=function(node){return dojo.html._callDeprecated("getMarginHeight","getMargin",arguments,null,"height");};dojo.html.getBorderWidth=function(node){return dojo.html._callDeprecated("getBorderWidth","getBorder",arguments,null,"width");};dojo.html.getBorderHeight=function(node){return dojo.html._callDeprecated("getBorderHeight","getBorder",arguments,null,"height");};dojo.html.getPaddingWidth=function(node){return dojo.html._callDeprecated("getPaddingWidth","getPadding",arguments,null,"width");};dojo.html.getPaddingHeight=function(node){return dojo.html._callDeprecated("getPaddingHeight","getPadding",arguments,null,"height");};dojo.html.getPadBorderWidth=function(node){return dojo.html._callDeprecated("getPadBorderWidth","getPadBorder",arguments,null,"width");};dojo.html.getPadBorderHeight=function(node){return dojo.html._callDeprecated("getPadBorderHeight","getPadBorder",arguments,null,"height");};dojo.html.getBorderBoxWidth=dojo.html.getInnerWidth=function(){return dojo.html._callDeprecated("getBorderBoxWidth","getBorderBox",arguments,null,"width");};dojo.html.getBorderBoxHeight=dojo.html.getInnerHeight=function(){return dojo.html._callDeprecated("getBorderBoxHeight","getBorderBox",arguments,null,"height");};dojo.html.getContentBoxWidth=dojo.html.getContentWidth=function(){return dojo.html._callDeprecated("getContentBoxWidth","getContentBox",arguments,null,"width");};dojo.html.getContentBoxHeight=dojo.html.getContentHeight=function(){return dojo.html._callDeprecated("getContentBoxHeight","getContentBox",arguments,null,"height");};dojo.html.setContentBoxWidth=dojo.html.setContentWidth=function(node,_757){return dojo.html._callDeprecated("setContentBoxWidth","setContentBox",arguments,"width");};dojo.html.setContentBoxHeight=dojo.html.setContentHeight=function(node,_759){return dojo.html._callDeprecated("setContentBoxHeight","setContentBox",arguments,"height");};dojo.provide("dojo.html.util");dojo.html.getElementWindow=function(_75a){return dojo.html.getDocumentWindow(_75a.ownerDocument);};dojo.html.getDocumentWindow=function(doc){if(dojo.render.html.safari&&!doc._parentWindow){var fix=function(win){win.document._parentWindow=win;for(var i=0;i<win.frames.length;i++){fix(win.frames[i]);}};fix(window.top);}
if(dojo.render.html.ie&&window!==document.parentWindow&&!doc._parentWindow){doc.parentWindow.execScript("document._parentWindow = window;","Javascript");var win=doc._parentWindow;doc._parentWindow=null;return win;}
return doc._parentWindow||doc.parentWindow||doc.defaultView;};dojo.html.gravity=function(node,e){node=dojo.byId(node);var _762=dojo.html.getCursorPosition(e);with(dojo.html){var _763=getAbsolutePosition(node,true);var bb=getBorderBox(node);var _765=_763.x+(bb.width/2);var _766=_763.y+(bb.height/2);}
with(dojo.html.gravity){return ((_762.x<_765?WEST:EAST)|(_762.y<_766?NORTH:SOUTH));}};dojo.html.gravity.NORTH=1;dojo.html.gravity.SOUTH=1<<1;dojo.html.gravity.EAST=1<<2;dojo.html.gravity.WEST=1<<3;dojo.html.overElement=function(_767,e){_767=dojo.byId(_767);var _769=dojo.html.getCursorPosition(e);var bb=dojo.html.getBorderBox(_767);var _76b=dojo.html.getAbsolutePosition(_767,true,dojo.html.boxSizing.BORDER_BOX);var top=_76b.y;var _76d=top+bb.height;var left=_76b.x;var _76f=left+bb.width;return (_769.x>=left&&_769.x<=_76f&&_769.y>=top&&_769.y<=_76d);};dojo.html.renderedTextContent=function(node){node=dojo.byId(node);var _771="";if(node==null){return _771;}
for(var i=0;i<node.childNodes.length;i++){switch(node.childNodes[i].nodeType){case 1:
case 5:
var _773="unknown";try{_773=dojo.html.getStyle(node.childNodes[i],"display");}
catch(E){}
switch(_773){case "block":
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
_771+="\n";_771+=dojo.html.renderedTextContent(node.childNodes[i]);_771+="\n";break;case "none":
break;default:
if(node.childNodes[i].tagName&&node.childNodes[i].tagName.toLowerCase()=="br"){_771+="\n";}else{_771+=dojo.html.renderedTextContent(node.childNodes[i]);}
break;}
break;case 3:
case 2:
case 4:
var text=node.childNodes[i].nodeValue;var _775="unknown";try{_775=dojo.html.getStyle(node,"text-transform");}
catch(E){}
switch(_775){case "capitalize":
var _776=text.split(" ");for(var i=0;i<_776.length;i++){_776[i]=_776[i].charAt(0).toUpperCase()+_776[i].substring(1);}
text=_776.join(" ");break;case "uppercase":
text=text.toUpperCase();break;case "lowercase":
text=text.toLowerCase();break;default:
break;}
switch(_775){case "nowrap":
break;case "pre-wrap":
break;case "pre-line":
break;case "pre":
break;default:
text=text.replace(/\s+/," ");if(/\s$/.test(_771)){text.replace(/^\s/,"");}
break;}
_771+=text;break;default:
break;}}
return _771;};dojo.html.createNodesFromText=function(txt,trim){if(trim){txt=txt.replace(/^\s+|\s+$/g,"");}
var tn=dojo.doc().createElement("div");tn.style.visibility="hidden";dojo.body().appendChild(tn);var _77a="none";if((/^<t[dh][\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table><tbody><tr>"+txt+"</tr></tbody></table>";_77a="cell";}else{if((/^<tr[\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table><tbody>"+txt+"</tbody></table>";_77a="row";}else{if((/^<(thead|tbody|tfoot)[\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table>"+txt+"</table>";_77a="section";}}}
tn.innerHTML=txt;if(tn["normalize"]){tn.normalize();}
var _77b=null;switch(_77a){case "cell":
_77b=tn.getElementsByTagName("tr")[0];break;case "row":
_77b=tn.getElementsByTagName("tbody")[0];break;case "section":
_77b=tn.getElementsByTagName("table")[0];break;default:
_77b=tn;break;}
var _77c=[];for(var x=0;x<_77b.childNodes.length;x++){_77c.push(_77b.childNodes[x].cloneNode(true));}
tn.style.display="none";dojo.body().removeChild(tn);return _77c;};dojo.html.placeOnScreen=function(node,_77f,_780,_781,_782,_783,_784){if(_77f instanceof Array||typeof _77f=="array"){_784=_783;_783=_782;_782=_781;_781=_780;_780=_77f[1];_77f=_77f[0];}
if(_783 instanceof String||typeof _783=="string"){_783=_783.split(",");}
if(!isNaN(_781)){_781=[Number(_781),Number(_781)];}else{if(!(_781 instanceof Array||typeof _781=="array")){_781=[0,0];}}
var _785=dojo.html.getScroll().offset;var view=dojo.html.getViewport();node=dojo.byId(node);var _787=node.style.display;node.style.display="";var bb=dojo.html.getBorderBox(node);var w=bb.width;var h=bb.height;node.style.display=_787;if(!(_783 instanceof Array||typeof _783=="array")){_783=["TL"];}
var _78b,_78c,_78d=Infinity,_78e;for(var _78f=0;_78f<_783.length;++_78f){var _790=_783[_78f];var _791=true;var tryX=_77f-(_790.charAt(1)=="L"?0:w)+_781[0]*(_790.charAt(1)=="L"?1:-1);var tryY=_780-(_790.charAt(0)=="T"?0:h)+_781[1]*(_790.charAt(0)=="T"?1:-1);if(_782){tryX-=_785.x;tryY-=_785.y;}
if(tryX<0){tryX=0;_791=false;}
if(tryY<0){tryY=0;_791=false;}
var x=tryX+w;if(x>view.width){x=view.width-w;_791=false;}else{x=tryX;}
x=Math.max(_781[0],x)+_785.x;var y=tryY+h;if(y>view.height){y=view.height-h;_791=false;}else{y=tryY;}
y=Math.max(_781[1],y)+_785.y;if(_791){_78b=x;_78c=y;_78d=0;_78e=_790;break;}else{var dist=Math.pow(x-tryX-_785.x,2)+Math.pow(y-tryY-_785.y,2);if(_78d>dist){_78d=dist;_78b=x;_78c=y;_78e=_790;}}}
if(!_784){node.style.left=_78b+"px";node.style.top=_78c+"px";}
return {left:_78b,top:_78c,x:_78b,y:_78c,dist:_78d,corner:_78e};};dojo.html.placeOnScreenPoint=function(node,_798,_799,_79a,_79b){dojo.deprecated("dojo.html.placeOnScreenPoint","use dojo.html.placeOnScreen() instead","0.5");return dojo.html.placeOnScreen(node,_798,_799,_79a,_79b,["TL","TR","BL","BR"]);};dojo.html.placeOnScreenAroundElement=function(node,_79d,_79e,_79f,_7a0,_7a1){var best,_7a3=Infinity;_79d=dojo.byId(_79d);var _7a4=_79d.style.display;_79d.style.display="";var mb=dojo.html.getElementBox(_79d,_79f);var _7a6=mb.width;var _7a7=mb.height;var _7a8=dojo.html.getAbsolutePosition(_79d,true,_79f);_79d.style.display=_7a4;for(var _7a9 in _7a0){var pos,_7ab,_7ac;var _7ad=_7a0[_7a9];_7ab=_7a8.x+(_7a9.charAt(1)=="L"?0:_7a6);_7ac=_7a8.y+(_7a9.charAt(0)=="T"?0:_7a7);pos=dojo.html.placeOnScreen(node,_7ab,_7ac,_79e,true,_7ad,true);if(pos.dist==0){best=pos;break;}else{if(_7a3>pos.dist){_7a3=pos.dist;best=pos;}}}
if(!_7a1){node.style.left=best.left+"px";node.style.top=best.top+"px";}
return best;};dojo.html.scrollIntoView=function(node){if(!node){return;}
if(dojo.render.html.ie){if(dojo.html.getBorderBox(node.parentNode).height<node.parentNode.scrollHeight){node.scrollIntoView(false);}}else{if(dojo.render.html.mozilla){node.scrollIntoView(false);}else{var _7af=node.parentNode;var _7b0=_7af.scrollTop+dojo.html.getBorderBox(_7af).height;var _7b1=node.offsetTop+dojo.html.getMarginBox(node).height;if(_7b0<_7b1){_7af.scrollTop+=(_7b1-_7b0);}else{if(_7af.scrollTop>node.offsetTop){_7af.scrollTop-=(_7af.scrollTop-node.offsetTop);}}}}};dojo.provide("dojo.gfx.color");dojo.gfx.color.Color=function(r,g,b,a){if(dojo.lang.isArray(r)){this.r=r[0];this.g=r[1];this.b=r[2];this.a=r[3]||1;}else{if(dojo.lang.isString(r)){var rgb=dojo.gfx.color.extractRGB(r);this.r=rgb[0];this.g=rgb[1];this.b=rgb[2];this.a=g||1;}else{if(r instanceof dojo.gfx.color.Color){this.r=r.r;this.b=r.b;this.g=r.g;this.a=r.a;}else{this.r=r;this.g=g;this.b=b;this.a=a;}}}};dojo.gfx.color.Color.fromArray=function(arr){return new dojo.gfx.color.Color(arr[0],arr[1],arr[2],arr[3]);};dojo.extend(dojo.gfx.color.Color,{toRgb:function(_7b8){if(_7b8){return this.toRgba();}else{return [this.r,this.g,this.b];}},toRgba:function(){return [this.r,this.g,this.b,this.a];},toHex:function(){return dojo.gfx.color.rgb2hex(this.toRgb());},toCss:function(){return "rgb("+this.toRgb().join()+")";},toString:function(){return this.toHex();},blend:function(_7b9,_7ba){var rgb=null;if(dojo.lang.isArray(_7b9)){rgb=_7b9;}else{if(_7b9 instanceof dojo.gfx.color.Color){rgb=_7b9.toRgb();}else{rgb=new dojo.gfx.color.Color(_7b9).toRgb();}}
return dojo.gfx.color.blend(this.toRgb(),rgb,_7ba);}});dojo.gfx.color.named={white:[255,255,255],black:[0,0,0],red:[255,0,0],green:[0,255,0],lime:[0,255,0],blue:[0,0,255],navy:[0,0,128],gray:[128,128,128],silver:[192,192,192]};dojo.gfx.color.blend=function(a,b,_7be){if(typeof a=="string"){return dojo.gfx.color.blendHex(a,b,_7be);}
if(!_7be){_7be=0;}
_7be=Math.min(Math.max(-1,_7be),1);_7be=((_7be+1)/2);var c=[];for(var x=0;x<3;x++){c[x]=parseInt(b[x]+((a[x]-b[x])*_7be));}
return c;};dojo.gfx.color.blendHex=function(a,b,_7c3){return dojo.gfx.color.rgb2hex(dojo.gfx.color.blend(dojo.gfx.color.hex2rgb(a),dojo.gfx.color.hex2rgb(b),_7c3));};dojo.gfx.color.extractRGB=function(_7c4){var hex="0123456789abcdef";_7c4=_7c4.toLowerCase();if(_7c4.indexOf("rgb")==0){var _7c6=_7c4.match(/rgba*\((\d+), *(\d+), *(\d+)/i);var ret=_7c6.splice(1,3);return ret;}else{var _7c8=dojo.gfx.color.hex2rgb(_7c4);if(_7c8){return _7c8;}else{return dojo.gfx.color.named[_7c4]||[255,255,255];}}};dojo.gfx.color.hex2rgb=function(hex){var _7ca="0123456789ABCDEF";var rgb=new Array(3);if(hex.indexOf("#")==0){hex=hex.substring(1);}
hex=hex.toUpperCase();if(hex.replace(new RegExp("["+_7ca+"]","g"),"")!=""){return null;}
if(hex.length==3){rgb[0]=hex.charAt(0)+hex.charAt(0);rgb[1]=hex.charAt(1)+hex.charAt(1);rgb[2]=hex.charAt(2)+hex.charAt(2);}else{rgb[0]=hex.substring(0,2);rgb[1]=hex.substring(2,4);rgb[2]=hex.substring(4);}
for(var i=0;i<rgb.length;i++){rgb[i]=_7ca.indexOf(rgb[i].charAt(0))*16+_7ca.indexOf(rgb[i].charAt(1));}
return rgb;};dojo.gfx.color.rgb2hex=function(r,g,b){if(dojo.lang.isArray(r)){g=r[1]||0;b=r[2]||0;r=r[0]||0;}
var ret=dojo.lang.map([r,g,b],function(x){x=new Number(x);var s=x.toString(16);while(s.length<2){s="0"+s;}
return s;});ret.unshift("#");return ret.join("");};dojo.provide("dojo.lfx.Animation");dojo.lfx.Line=function(_7d3,end){this.start=_7d3;this.end=end;if(dojo.lang.isArray(_7d3)){var diff=[];dojo.lang.forEach(this.start,function(s,i){diff[i]=this.end[i]-s;},this);this.getValue=function(n){var res=[];dojo.lang.forEach(this.start,function(s,i){res[i]=(diff[i]*n)+s;},this);return res;};}else{var diff=end-_7d3;this.getValue=function(n){return (diff*n)+this.start;};}};dojo.lfx.easeDefault=function(n){if(dojo.render.html.khtml){return (parseFloat("0.5")+((Math.sin((n+parseFloat("1.5"))*Math.PI))/2));}else{return (0.5+((Math.sin((n+1.5)*Math.PI))/2));}};dojo.lfx.easeIn=function(n){return Math.pow(n,3);};dojo.lfx.easeOut=function(n){return (1-Math.pow(1-n,3));};dojo.lfx.easeInOut=function(n){return ((3*Math.pow(n,2))-(2*Math.pow(n,3)));};dojo.lfx.IAnimation=function(){};dojo.lang.extend(dojo.lfx.IAnimation,{curve:null,duration:1000,easing:null,repeatCount:0,rate:25,handler:null,beforeBegin:null,onBegin:null,onAnimate:null,onEnd:null,onPlay:null,onPause:null,onStop:null,play:null,pause:null,stop:null,connect:function(evt,_7e2,_7e3){if(!_7e3){_7e3=_7e2;_7e2=this;}
_7e3=dojo.lang.hitch(_7e2,_7e3);var _7e4=this[evt]||function(){};this[evt]=function(){var ret=_7e4.apply(this,arguments);_7e3.apply(this,arguments);return ret;};return this;},fire:function(evt,args){if(this[evt]){this[evt].apply(this,(args||[]));}
return this;},repeat:function(_7e8){this.repeatCount=_7e8;return this;},_active:false,_paused:false});dojo.lfx.Animation=function(_7e9,_7ea,_7eb,_7ec,_7ed,rate){dojo.lfx.IAnimation.call(this);if(dojo.lang.isNumber(_7e9)||(!_7e9&&_7ea.getValue)){rate=_7ed;_7ed=_7ec;_7ec=_7eb;_7eb=_7ea;_7ea=_7e9;_7e9=null;}else{if(_7e9.getValue||dojo.lang.isArray(_7e9)){rate=_7ec;_7ed=_7eb;_7ec=_7ea;_7eb=_7e9;_7ea=null;_7e9=null;}}
if(dojo.lang.isArray(_7eb)){this.curve=new dojo.lfx.Line(_7eb[0],_7eb[1]);}else{this.curve=_7eb;}
if(_7ea!=null&&_7ea>0){this.duration=_7ea;}
if(_7ed){this.repeatCount=_7ed;}
if(rate){this.rate=rate;}
if(_7e9){dojo.lang.forEach(["handler","beforeBegin","onBegin","onEnd","onPlay","onStop","onAnimate"],function(item){if(_7e9[item]){this.connect(item,_7e9[item]);}},this);}
if(_7ec&&dojo.lang.isFunction(_7ec)){this.easing=_7ec;}};dojo.inherits(dojo.lfx.Animation,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Animation,{_startTime:null,_endTime:null,_timer:null,_percent:0,_startRepeatCount:0,play:function(_7f0,_7f1){if(_7f1){clearTimeout(this._timer);this._active=false;this._paused=false;this._percent=0;}else{if(this._active&&!this._paused){return this;}}
this.fire("handler",["beforeBegin"]);this.fire("beforeBegin");if(_7f0>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_7f1);}),_7f0);return this;}
this._startTime=new Date().valueOf();if(this._paused){this._startTime-=(this.duration*this._percent/100);}
this._endTime=this._startTime+this.duration;this._active=true;this._paused=false;var step=this._percent/100;var _7f3=this.curve.getValue(step);if(this._percent==0){if(!this._startRepeatCount){this._startRepeatCount=this.repeatCount;}
this.fire("handler",["begin",_7f3]);this.fire("onBegin",[_7f3]);}
this.fire("handler",["play",_7f3]);this.fire("onPlay",[_7f3]);this._cycle();return this;},pause:function(){clearTimeout(this._timer);if(!this._active){return this;}
this._paused=true;var _7f4=this.curve.getValue(this._percent/100);this.fire("handler",["pause",_7f4]);this.fire("onPause",[_7f4]);return this;},gotoPercent:function(pct,_7f6){clearTimeout(this._timer);this._active=true;this._paused=true;this._percent=pct;if(_7f6){this.play();}
return this;},stop:function(_7f7){clearTimeout(this._timer);var step=this._percent/100;if(_7f7){step=1;}
var _7f9=this.curve.getValue(step);this.fire("handler",["stop",_7f9]);this.fire("onStop",[_7f9]);this._active=false;this._paused=false;return this;},status:function(){if(this._active){return this._paused?"paused":"playing";}else{return "stopped";}
return this;},_cycle:function(){clearTimeout(this._timer);if(this._active){var curr=new Date().valueOf();var step=(curr-this._startTime)/(this._endTime-this._startTime);if(step>=1){step=1;this._percent=100;}else{this._percent=step*100;}
if((this.easing)&&(dojo.lang.isFunction(this.easing))){step=this.easing(step);}
var _7fc=this.curve.getValue(step);this.fire("handler",["animate",_7fc]);this.fire("onAnimate",[_7fc]);if(step<1){this._timer=setTimeout(dojo.lang.hitch(this,"_cycle"),this.rate);}else{this._active=false;this.fire("handler",["end"]);this.fire("onEnd");if(this.repeatCount>0){this.repeatCount--;this.play(null,true);}else{if(this.repeatCount==-1){this.play(null,true);}else{if(this._startRepeatCount){this.repeatCount=this._startRepeatCount;this._startRepeatCount=0;}}}}}
return this;}});dojo.lfx.Combine=function(_7fd){dojo.lfx.IAnimation.call(this);this._anims=[];this._animsEnded=0;var _7fe=arguments;if(_7fe.length==1&&(dojo.lang.isArray(_7fe[0])||dojo.lang.isArrayLike(_7fe[0]))){_7fe=_7fe[0];}
dojo.lang.forEach(_7fe,function(anim){this._anims.push(anim);anim.connect("onEnd",dojo.lang.hitch(this,"_onAnimsEnded"));},this);};dojo.inherits(dojo.lfx.Combine,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Combine,{_animsEnded:0,play:function(_800,_801){if(!this._anims.length){return this;}
this.fire("beforeBegin");if(_800>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_801);}),_800);return this;}
if(_801||this._anims[0].percent==0){this.fire("onBegin");}
this.fire("onPlay");this._animsCall("play",null,_801);return this;},pause:function(){this.fire("onPause");this._animsCall("pause");return this;},stop:function(_802){this.fire("onStop");this._animsCall("stop",_802);return this;},_onAnimsEnded:function(){this._animsEnded++;if(this._animsEnded>=this._anims.length){this.fire("onEnd");}
return this;},_animsCall:function(_803){var args=[];if(arguments.length>1){for(var i=1;i<arguments.length;i++){args.push(arguments[i]);}}
var _806=this;dojo.lang.forEach(this._anims,function(anim){anim[_803](args);},_806);return this;}});dojo.lfx.Chain=function(_808){dojo.lfx.IAnimation.call(this);this._anims=[];this._currAnim=-1;var _809=arguments;if(_809.length==1&&(dojo.lang.isArray(_809[0])||dojo.lang.isArrayLike(_809[0]))){_809=_809[0];}
var _80a=this;dojo.lang.forEach(_809,function(anim,i,_80d){this._anims.push(anim);if(i<_80d.length-1){anim.connect("onEnd",dojo.lang.hitch(this,"_playNext"));}else{anim.connect("onEnd",dojo.lang.hitch(this,function(){this.fire("onEnd");}));}},this);};dojo.inherits(dojo.lfx.Chain,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Chain,{_currAnim:-1,play:function(_80e,_80f){if(!this._anims.length){return this;}
if(_80f||!this._anims[this._currAnim]){this._currAnim=0;}
var _810=this._anims[this._currAnim];this.fire("beforeBegin");if(_80e>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_80f);}),_80e);return this;}
if(_810){if(this._currAnim==0){this.fire("handler",["begin",this._currAnim]);this.fire("onBegin",[this._currAnim]);}
this.fire("onPlay",[this._currAnim]);_810.play(null,_80f);}
return this;},pause:function(){if(this._anims[this._currAnim]){this._anims[this._currAnim].pause();this.fire("onPause",[this._currAnim]);}
return this;},playPause:function(){if(this._anims.length==0){return this;}
if(this._currAnim==-1){this._currAnim=0;}
var _811=this._anims[this._currAnim];if(_811){if(!_811._active||_811._paused){this.play();}else{this.pause();}}
return this;},stop:function(){var _812=this._anims[this._currAnim];if(_812){_812.stop();this.fire("onStop",[this._currAnim]);}
return _812;},_playNext:function(){if(this._currAnim==-1||this._anims.length==0){return this;}
this._currAnim++;if(this._anims[this._currAnim]){this._anims[this._currAnim].play(null,true);}
return this;}});dojo.lfx.combine=function(_813){var _814=arguments;if(dojo.lang.isArray(arguments[0])){_814=arguments[0];}
if(_814.length==1){return _814[0];}
return new dojo.lfx.Combine(_814);};dojo.lfx.chain=function(_815){var _816=arguments;if(dojo.lang.isArray(arguments[0])){_816=arguments[0];}
if(_816.length==1){return _816[0];}
return new dojo.lfx.Chain(_816);};dojo.provide("dojo.html.color");dojo.html.getBackgroundColor=function(node){node=dojo.byId(node);var _818;do{_818=dojo.html.getStyle(node,"background-color");if(_818.toLowerCase()=="rgba(0, 0, 0, 0)"){_818="transparent";}
if(node==document.getElementsByTagName("body")[0]){node=null;break;}
node=node.parentNode;}while(node&&dojo.lang.inArray(["transparent",""],_818));if(_818=="transparent"){_818=[255,255,255,0];}else{_818=dojo.gfx.color.extractRGB(_818);}
return _818;};dojo.provide("dojo.lfx.html");dojo.lfx.html._byId=function(_819){if(!_819){return [];}
if(dojo.lang.isArrayLike(_819)){if(!_819.alreadyChecked){var n=[];dojo.lang.forEach(_819,function(node){n.push(dojo.byId(node));});n.alreadyChecked=true;return n;}else{return _819;}}else{var n=[];n.push(dojo.byId(_819));n.alreadyChecked=true;return n;}};dojo.lfx.html.propertyAnimation=function(_81c,_81d,_81e,_81f,_820){_81c=dojo.lfx.html._byId(_81c);var _821={"propertyMap":_81d,"nodes":_81c,"duration":_81e,"easing":_81f||dojo.lfx.easeDefault};var _822=function(args){if(args.nodes.length==1){var pm=args.propertyMap;if(!dojo.lang.isArray(args.propertyMap)){var parr=[];for(var _826 in pm){pm[_826].property=_826;parr.push(pm[_826]);}
pm=args.propertyMap=parr;}
dojo.lang.forEach(pm,function(prop){if(dj_undef("start",prop)){if(prop.property!="opacity"){prop.start=parseInt(dojo.html.getComputedStyle(args.nodes[0],prop.property));}else{prop.start=dojo.html.getOpacity(args.nodes[0]);}}});}};var _828=function(_829){var _82a=[];dojo.lang.forEach(_829,function(c){_82a.push(Math.round(c));});return _82a;};var _82c=function(n,_82e){n=dojo.byId(n);if(!n||!n.style){return;}
for(var s in _82e){if(s=="opacity"){dojo.html.setOpacity(n,_82e[s]);}else{n.style[s]=_82e[s];}}};var _830=function(_831){this._properties=_831;this.diffs=new Array(_831.length);dojo.lang.forEach(_831,function(prop,i){if(dojo.lang.isFunction(prop.start)){prop.start=prop.start(prop,i);}
if(dojo.lang.isFunction(prop.end)){prop.end=prop.end(prop,i);}
if(dojo.lang.isArray(prop.start)){this.diffs[i]=null;}else{if(prop.start instanceof dojo.gfx.color.Color){prop.startRgb=prop.start.toRgb();prop.endRgb=prop.end.toRgb();}else{this.diffs[i]=prop.end-prop.start;}}},this);this.getValue=function(n){var ret={};dojo.lang.forEach(this._properties,function(prop,i){var _838=null;if(dojo.lang.isArray(prop.start)){}else{if(prop.start instanceof dojo.gfx.color.Color){_838=(prop.units||"rgb")+"(";for(var j=0;j<prop.startRgb.length;j++){_838+=Math.round(((prop.endRgb[j]-prop.startRgb[j])*n)+prop.startRgb[j])+(j<prop.startRgb.length-1?",":"");}
_838+=")";}else{_838=((this.diffs[i])*n)+prop.start+(prop.property!="opacity"?prop.units||"px":"");}}
ret[dojo.html.toCamelCase(prop.property)]=_838;},this);return ret;};};var anim=new dojo.lfx.Animation({beforeBegin:function(){_822(_821);anim.curve=new _830(_821.propertyMap);},onAnimate:function(_83b){dojo.lang.forEach(_821.nodes,function(node){_82c(node,_83b);});}},_821.duration,null,_821.easing);if(_820){for(var x in _820){if(dojo.lang.isFunction(_820[x])){anim.connect(x,anim,_820[x]);}}}
return anim;};dojo.lfx.html._makeFadeable=function(_83e){var _83f=function(node){if(dojo.render.html.ie){if((node.style.zoom.length==0)&&(dojo.html.getStyle(node,"zoom")=="normal")){node.style.zoom="1";}
if((node.style.width.length==0)&&(dojo.html.getStyle(node,"width")=="auto")){node.style.width="auto";}}};if(dojo.lang.isArrayLike(_83e)){dojo.lang.forEach(_83e,_83f);}else{_83f(_83e);}};dojo.lfx.html.fade=function(_841,_842,_843,_844,_845){_841=dojo.lfx.html._byId(_841);var _846={property:"opacity"};if(!dj_undef("start",_842)){_846.start=_842.start;}else{_846.start=function(){return dojo.html.getOpacity(_841[0]);};}
if(!dj_undef("end",_842)){_846.end=_842.end;}else{dojo.raise("dojo.lfx.html.fade needs an end value");}
var anim=dojo.lfx.propertyAnimation(_841,[_846],_843,_844);anim.connect("beforeBegin",function(){dojo.lfx.html._makeFadeable(_841);});if(_845){anim.connect("onEnd",function(){_845(_841,anim);});}
return anim;};dojo.lfx.html.fadeIn=function(_848,_849,_84a,_84b){return dojo.lfx.html.fade(_848,{end:1},_849,_84a,_84b);};dojo.lfx.html.fadeOut=function(_84c,_84d,_84e,_84f){return dojo.lfx.html.fade(_84c,{end:0},_84d,_84e,_84f);};dojo.lfx.html.fadeShow=function(_850,_851,_852,_853){_850=dojo.lfx.html._byId(_850);dojo.lang.forEach(_850,function(node){dojo.html.setOpacity(node,0);});var anim=dojo.lfx.html.fadeIn(_850,_851,_852,_853);anim.connect("beforeBegin",function(){if(dojo.lang.isArrayLike(_850)){dojo.lang.forEach(_850,dojo.html.show);}else{dojo.html.show(_850);}});return anim;};dojo.lfx.html.fadeHide=function(_856,_857,_858,_859){var anim=dojo.lfx.html.fadeOut(_856,_857,_858,function(){if(dojo.lang.isArrayLike(_856)){dojo.lang.forEach(_856,dojo.html.hide);}else{dojo.html.hide(_856);}
if(_859){_859(_856,anim);}});return anim;};dojo.lfx.html.wipeIn=function(_85b,_85c,_85d,_85e){_85b=dojo.lfx.html._byId(_85b);var _85f=[];dojo.lang.forEach(_85b,function(node){var _861={};dojo.html.show(node);var _862=dojo.html.getBorderBox(node).height;dojo.html.hide(node);var anim=dojo.lfx.propertyAnimation(node,{"height":{start:1,end:function(){return _862;}}},_85c,_85d);anim.connect("beforeBegin",function(){_861.overflow=node.style.overflow;_861.height=node.style.height;with(node.style){overflow="hidden";_862="1px";}
dojo.html.show(node);});anim.connect("onEnd",function(){with(node.style){overflow=_861.overflow;_862=_861.height;}
if(_85e){_85e(node,anim);}});_85f.push(anim);});return dojo.lfx.combine(_85f);};dojo.lfx.html.wipeOut=function(_864,_865,_866,_867){_864=dojo.lfx.html._byId(_864);var _868=[];dojo.lang.forEach(_864,function(node){var _86a={};var anim=dojo.lfx.propertyAnimation(node,{"height":{start:function(){return dojo.html.getContentBox(node).height;},end:1}},_865,_866,{"beforeBegin":function(){_86a.overflow=node.style.overflow;_86a.height=node.style.height;with(node.style){overflow="hidden";}
dojo.html.show(node);},"onEnd":function(){dojo.html.hide(node);with(node.style){overflow=_86a.overflow;height=_86a.height;}
if(_867){_867(node,anim);}}});_868.push(anim);});return dojo.lfx.combine(_868);};dojo.lfx.html.slideTo=function(_86c,_86d,_86e,_86f,_870){_86c=dojo.lfx.html._byId(_86c);var _871=[];var _872=dojo.html.getComputedStyle;if(dojo.lang.isArray(_86d)){dojo.deprecated("dojo.lfx.html.slideTo(node, array)","use dojo.lfx.html.slideTo(node, {top: value, left: value});","0.5");_86d={top:_86d[0],left:_86d[1]};}
dojo.lang.forEach(_86c,function(node){var top=null;var left=null;var init=(function(){var _877=node;return function(){var pos=_872(_877,"position");top=(pos=="absolute"?node.offsetTop:parseInt(_872(node,"top"))||0);left=(pos=="absolute"?node.offsetLeft:parseInt(_872(node,"left"))||0);if(!dojo.lang.inArray(["absolute","relative"],pos)){var ret=dojo.html.abs(_877,true);dojo.html.setStyleAttributes(_877,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");top=ret.y;left=ret.x;}};})();init();var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:(_86d.top||0)},"left":{start:left,end:(_86d.left||0)}},_86e,_86f,{"beforeBegin":init});if(_870){anim.connect("onEnd",function(){_870(_86c,anim);});}
_871.push(anim);});return dojo.lfx.combine(_871);};dojo.lfx.html.slideBy=function(_87b,_87c,_87d,_87e,_87f){_87b=dojo.lfx.html._byId(_87b);var _880=[];var _881=dojo.html.getComputedStyle;if(dojo.lang.isArray(_87c)){dojo.deprecated("dojo.lfx.html.slideBy(node, array)","use dojo.lfx.html.slideBy(node, {top: value, left: value});","0.5");_87c={top:_87c[0],left:_87c[1]};}
dojo.lang.forEach(_87b,function(node){var top=null;var left=null;var init=(function(){var _886=node;return function(){var pos=_881(_886,"position");top=(pos=="absolute"?node.offsetTop:parseInt(_881(node,"top"))||0);left=(pos=="absolute"?node.offsetLeft:parseInt(_881(node,"left"))||0);if(!dojo.lang.inArray(["absolute","relative"],pos)){var ret=dojo.html.abs(_886,true);dojo.html.setStyleAttributes(_886,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");top=ret.y;left=ret.x;}};})();init();var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:top+(_87c.top||0)},"left":{start:left,end:left+(_87c.left||0)}},_87d,_87e).connect("beforeBegin",init);if(_87f){anim.connect("onEnd",function(){_87f(_87b,anim);});}
_880.push(anim);});return dojo.lfx.combine(_880);};dojo.lfx.html.explode=function(_88a,_88b,_88c,_88d,_88e){var h=dojo.html;_88a=dojo.byId(_88a);_88b=dojo.byId(_88b);var _890=h.toCoordinateObject(_88a,true);var _891=document.createElement("div");h.copyStyle(_891,_88b);if(_88b.explodeClassName){_891.className=_88b.explodeClassName;}
with(_891.style){position="absolute";display="none";}
dojo.body().appendChild(_891);with(_88b.style){visibility="hidden";display="block";}
var _892=h.toCoordinateObject(_88b,true);with(_88b.style){display="none";visibility="visible";}
var _893={opacity:{start:0.5,end:1}};dojo.lang.forEach(["height","width","top","left"],function(type){_893[type]={start:_890[type],end:_892[type]};});var anim=new dojo.lfx.propertyAnimation(_891,_893,_88c,_88d,{"beforeBegin":function(){h.setDisplay(_891,"block");},"onEnd":function(){h.setDisplay(_88b,"block");_891.parentNode.removeChild(_891);}});if(_88e){anim.connect("onEnd",function(){_88e(_88b,anim);});}
return anim;};dojo.lfx.html.implode=function(_896,end,_898,_899,_89a){var h=dojo.html;_896=dojo.byId(_896);end=dojo.byId(end);var _89c=dojo.html.toCoordinateObject(_896,true);var _89d=dojo.html.toCoordinateObject(end,true);var _89e=document.createElement("div");dojo.html.copyStyle(_89e,_896);if(_896.explodeClassName){_89e.className=_896.explodeClassName;}
dojo.html.setOpacity(_89e,0.3);with(_89e.style){position="absolute";display="none";backgroundColor=h.getStyle(_896,"background-color").toLowerCase();}
dojo.body().appendChild(_89e);var _89f={opacity:{start:1,end:0.5}};dojo.lang.forEach(["height","width","top","left"],function(type){_89f[type]={start:_89c[type],end:_89d[type]};});var anim=new dojo.lfx.propertyAnimation(_89e,_89f,_898,_899,{"beforeBegin":function(){dojo.html.hide(_896);dojo.html.show(_89e);},"onEnd":function(){_89e.parentNode.removeChild(_89e);}});if(_89a){anim.connect("onEnd",function(){_89a(_896,anim);});}
return anim;};dojo.lfx.html.highlight=function(_8a2,_8a3,_8a4,_8a5,_8a6){_8a2=dojo.lfx.html._byId(_8a2);var _8a7=[];dojo.lang.forEach(_8a2,function(node){var _8a9=dojo.html.getBackgroundColor(node);var bg=dojo.html.getStyle(node,"background-color").toLowerCase();var _8ab=dojo.html.getStyle(node,"background-image");var _8ac=(bg=="transparent"||bg=="rgba(0, 0, 0, 0)");while(_8a9.length>3){_8a9.pop();}
var rgb=new dojo.gfx.color.Color(_8a3);var _8ae=new dojo.gfx.color.Color(_8a9);var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:rgb,end:_8ae}},_8a4,_8a5,{"beforeBegin":function(){if(_8ab){node.style.backgroundImage="none";}
node.style.backgroundColor="rgb("+rgb.toRgb().join(",")+")";},"onEnd":function(){if(_8ab){node.style.backgroundImage=_8ab;}
if(_8ac){node.style.backgroundColor="transparent";}
if(_8a6){_8a6(node,anim);}}});_8a7.push(anim);});return dojo.lfx.combine(_8a7);};dojo.lfx.html.unhighlight=function(_8b0,_8b1,_8b2,_8b3,_8b4){_8b0=dojo.lfx.html._byId(_8b0);var _8b5=[];dojo.lang.forEach(_8b0,function(node){var _8b7=new dojo.gfx.color.Color(dojo.html.getBackgroundColor(node));var rgb=new dojo.gfx.color.Color(_8b1);var _8b9=dojo.html.getStyle(node,"background-image");var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:_8b7,end:rgb}},_8b2,_8b3,{"beforeBegin":function(){if(_8b9){node.style.backgroundImage="none";}
node.style.backgroundColor="rgb("+_8b7.toRgb().join(",")+")";},"onEnd":function(){if(_8b4){_8b4(node,anim);}}});_8b5.push(anim);});return dojo.lfx.combine(_8b5);};dojo.lang.mixin(dojo.lfx,dojo.lfx.html);dojo.provide("dojo.lfx.*");dojo.provide("dojo.lfx.toggle");dojo.lfx.toggle.plain={show:function(node,_8bc,_8bd,_8be){dojo.html.show(node);if(dojo.lang.isFunction(_8be)){_8be();}},hide:function(node,_8c0,_8c1,_8c2){dojo.html.hide(node);if(dojo.lang.isFunction(_8c2)){_8c2();}}};dojo.lfx.toggle.fade={show:function(node,_8c4,_8c5,_8c6){dojo.lfx.fadeShow(node,_8c4,_8c5,_8c6).play();},hide:function(node,_8c8,_8c9,_8ca){dojo.lfx.fadeHide(node,_8c8,_8c9,_8ca).play();}};dojo.lfx.toggle.wipe={show:function(node,_8cc,_8cd,_8ce){dojo.lfx.wipeIn(node,_8cc,_8cd,_8ce).play();},hide:function(node,_8d0,_8d1,_8d2){dojo.lfx.wipeOut(node,_8d0,_8d1,_8d2).play();}};dojo.lfx.toggle.explode={show:function(node,_8d4,_8d5,_8d6,_8d7){dojo.lfx.explode(_8d7||{x:0,y:0,width:0,height:0},node,_8d4,_8d5,_8d6).play();},hide:function(node,_8d9,_8da,_8db,_8dc){dojo.lfx.implode(node,_8dc||{x:0,y:0,width:0,height:0},_8d9,_8da,_8db).play();}};dojo.provide("dojo.widget.HtmlWidget");dojo.declare("dojo.widget.HtmlWidget",dojo.widget.DomWidget,{templateCssPath:null,templatePath:null,lang:"",toggle:"plain",toggleDuration:150,initialize:function(args,frag){},postMixInProperties:function(args,frag){if(this.lang===""){this.lang=null;}
this.toggleObj=dojo.lfx.toggle[this.toggle.toLowerCase()]||dojo.lfx.toggle.plain;},createNodesFromText:function(txt,wrap){return dojo.html.createNodesFromText(txt,wrap);},destroyRendering:function(_8e3){try{if(!_8e3&&this.domNode){dojo.event.browser.clean(this.domNode);}
this.domNode.parentNode.removeChild(this.domNode);delete this.domNode;}
catch(e){}},isShowing:function(){return dojo.html.isShowing(this.domNode);},toggleShowing:function(){if(this.isShowing()){this.hide();}else{this.show();}},show:function(){if(this.isShowing()){return;}
this.animationInProgress=true;this.toggleObj.show(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onShow),this.explodeSrc);},onShow:function(){this.animationInProgress=false;this.checkSize();},hide:function(){if(!this.isShowing()){return;}
this.animationInProgress=true;this.toggleObj.hide(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onHide),this.explodeSrc);},onHide:function(){this.animationInProgress=false;},_isResized:function(w,h){if(!this.isShowing()){return false;}
var wh=dojo.html.getMarginBox(this.domNode);var _8e7=w||wh.width;var _8e8=h||wh.height;if(this.width==_8e7&&this.height==_8e8){return false;}
this.width=_8e7;this.height=_8e8;return true;},checkSize:function(){if(!this._isResized()){return;}
this.onResized();},resizeTo:function(w,h){dojo.html.setMarginBox(this.domNode,{width:w,height:h});if(this.isShowing()){this.onResized();}},resizeSoon:function(){if(this.isShowing()){dojo.lang.setTimeout(this,this.onResized,0);}},onResized:function(){dojo.lang.forEach(this.children,function(_8eb){if(_8eb.checkSize){_8eb.checkSize();}});}});dojo.provide("dojo.widget.*");
