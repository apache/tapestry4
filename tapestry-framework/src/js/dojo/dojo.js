
if(typeof dojo=="undefined"){var dj_global=this;var dj_currentContext=this;function dj_undef(_1,_2){return (typeof (_2||dj_currentContext)[_1]=="undefined");}
if(dj_undef("djConfig",this)){var djConfig={};}
if(dj_undef("dojo",this)){var dojo={};}
dojo.global=function(){return dj_currentContext;};dojo.locale=djConfig.locale;dojo.version={major:0,minor:0,patch:0,flag:"dev",revision:Number("$Rev: 6425 $".match(/[0-9]+/)[0]),toString:function(){with(dojo.version){return major+"."+minor+"."+patch+flag+" ("+revision+")";}}};dojo.evalProp=function(_3,_4,_5){if((!_4)||(!_3)){return undefined;}
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
for(var j=_73.length-1;j>=0;j--){var loc=_73[j]||"ROOT";var _77=_71(loc);if(_77){break;}}};dojo.hostenv.localesGenerated=["ROOT","es-es","es","it-it","pt-br","de","fr-fr","zh-cn","pt","en-us","zh","fr","zh-tw","it","en-gb","xx","de-de","ko-kr","ja-jp","ko","en","ja"];dojo.hostenv.registerNlsPrefix=function(){dojo.registerModulePath("nls","nls");};dojo.hostenv.preloadLocalizations=function(){if(dojo.hostenv.localesGenerated){dojo.hostenv.registerNlsPrefix();function preload(_78){_78=dojo.hostenv.normalizeLocale(_78);dojo.hostenv.searchLocalePath(_78,true,function(loc){for(var i=0;i<dojo.hostenv.localesGenerated.length;i++){if(dojo.hostenv.localesGenerated[i]==loc){dojo["require"]("nls.dojo_"+loc);return true;}}
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
if(drh.safari){var tmp=dua.split("AppleWebKit/")[1];var ver=parseFloat(tmp.split(" ")[0]);if(ver>=420){drs.capable=t;drs.support.builtin=t;drs.support.plugin=f;}}else{}})();dojo.hostenv.startPackage("dojo.hostenv");dojo.render.name=dojo.hostenv.name_="browser";dojo.hostenv.searchIds=[];dojo.hostenv._XMLHTTP_PROGIDS=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"];dojo.hostenv.getXmlhttpObject=function(){var _b0=null;var _b1=null;try{_b0=new XMLHttpRequest();}
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
catch(e2){window.status=_bf;}}}};dojo.addOnLoad(function(){dojo.hostenv._println_safe=true;while(dojo.hostenv._println_buffer.length>0){dojo.hostenv.println(dojo.hostenv._println_buffer.shift());}});function dj_addNodeEvtHdlr(_c2,_c3,fp){var _c5=_c2["on"+_c3]||function(){};_c2["on"+_c3]=function(){fp.apply(_c2,arguments);_c5.apply(_c2,arguments);};return true;}
function dj_load_init(e){var _c7=(e&&e.type)?e.type.toLowerCase():"load";if(arguments.callee.initialized||(_c7!="domcontentloaded"&&_c7!="load")){return;}
arguments.callee.initialized=true;if(typeof (_timer)!="undefined"){clearInterval(_timer);delete _timer;}
var _c8=function(){if(dojo.render.html.ie){dojo.hostenv.makeWidgets();}};if(dojo.hostenv.inFlightCount==0){_c8();dojo.hostenv.modulesLoaded();}else{dojo.hostenv.modulesLoadedListeners.unshift(_c8);}}
if(document.addEventListener){if(dojo.render.html.opera||(dojo.render.html.moz&&!djConfig.delayMozLoadingFix)){document.addEventListener("DOMContentLoaded",dj_load_init,null);}
window.addEventListener("load",dj_load_init,null);}
if(dojo.render.html.ie&&dojo.render.os.win){document.attachEvent("onreadystatechange",function(e){if(document.readyState=="complete"){dj_load_init();}});}
if(/(WebKit|khtml)/i.test(navigator.userAgent)){var _timer=setInterval(function(){if(/loaded|complete/.test(document.readyState)){dj_load_init();}},10);}
if(dojo.render.html.ie){dj_addNodeEvtHdlr(window,"beforeunload",function(){dojo.hostenv._unloading=true;window.setTimeout(function(){dojo.hostenv._unloading=false;},0);});}
dj_addNodeEvtHdlr(window,"unload",function(){dojo.hostenv.unloaded();if((!dojo.render.html.ie)||(dojo.render.html.ie&&dojo.hostenv._unloading)){dojo.hostenv.unloaded();}});dojo.hostenv.makeWidgets=function(){var _ca=[];if(djConfig.searchIds&&djConfig.searchIds.length>0){_ca=_ca.concat(djConfig.searchIds);}
if(dojo.hostenv.searchIds&&dojo.hostenv.searchIds.length>0){_ca=_ca.concat(dojo.hostenv.searchIds);}
if((djConfig.parseWidgets)||(_ca.length>0)){if(dojo.evalObjPath("dojo.widget.Parse")){var _cb=new dojo.xml.Parse();if(_ca.length>0){for(var x=0;x<_ca.length;x++){var _cd=document.getElementById(_ca[x]);if(!_cd){continue;}
var _ce=_cb.parseElement(_cd,null,true);dojo.widget.getParser().createComponents(_ce);}}else{if(djConfig.parseWidgets){var _ce=_cb.parseElement(dojo.body(),null,true);dojo.widget.getParser().createComponents(_ce);}}}}};dojo.addOnLoad(function(){if(!dojo.render.html.ie){dojo.hostenv.makeWidgets();}});try{if(dojo.render.html.ie){document.namespaces.add("v","urn:schemas-microsoft-com:vml");document.createStyleSheet().addRule("v\\:*","behavior:url(#default#VML)");}}
catch(e){}
dojo.hostenv.writeIncludes=function(){};if(!dj_undef("document",this)){dj_currentDocument=this.document;}
dojo.doc=function(){return dj_currentDocument;};dojo.body=function(){return dojo.doc().body||dojo.doc().getElementsByTagName("body")[0];};dojo.byId=function(id,doc){if((id)&&((typeof id=="string")||(id instanceof String))){if(!doc){doc=dj_currentDocument;}
var ele=doc.getElementById(id);if(ele&&(ele.id!=id)&&doc.all){ele=null;eles=doc.all[id];if(eles){if(eles.length){for(var i=0;i<eles.length;i++){if(eles[i].id==id){ele=eles[i];break;}}}else{ele=eles;}}}
return ele;}
return id;};dojo.setContext=function(_d3,_d4){dj_currentContext=_d3;dj_currentDocument=_d4;};dojo._fireCallback=function(_d5,_d6,_d7){if((_d6)&&((typeof _d5=="string")||(_d5 instanceof String))){_d5=_d6[_d5];}
return (_d6?_d5.apply(_d6,_d7||[]):_d5());};dojo.withGlobal=function(_d8,_d9,_da,_db){var _dc;var _dd=dj_currentContext;var _de=dj_currentDocument;try{dojo.setContext(_d8,_d8.document);_dc=dojo._fireCallback(_d9,_da,_db);}
finally{dojo.setContext(_dd,_de);}
return _dc;};dojo.withDoc=function(_df,_e0,_e1,_e2){var _e3;var _e4=dj_currentDocument;try{dj_currentDocument=_df;_e3=dojo._fireCallback(_e0,_e1,_e2);}
finally{dj_currentDocument=_e4;}
return _e3;};}
(function(){if(typeof dj_usingBootstrap!="undefined"){return;}
var _e5=false;var _e6=false;var _e7=false;if((typeof this["load"]=="function")&&((typeof this["Packages"]=="function")||(typeof this["Packages"]=="object"))){_e5=true;}else{if(typeof this["load"]=="function"){_e6=true;}else{if(window.widget){_e7=true;}}}
var _e8=[];if((this["djConfig"])&&((djConfig["isDebug"])||(djConfig["debugAtAllCosts"]))){_e8.push("debug.js");}
if((this["djConfig"])&&(djConfig["debugAtAllCosts"])&&(!_e5)&&(!_e7)){_e8.push("browser_debug.js");}
var _e9=djConfig["baseScriptUri"];if((this["djConfig"])&&(djConfig["baseLoaderUri"])){_e9=djConfig["baseLoaderUri"];}
for(var x=0;x<_e8.length;x++){var _eb=_e9+"src/"+_e8[x];if(_e5||_e6){load(_eb);}else{try{document.write("<scr"+"ipt type='text/javascript' src='"+_eb+"'></scr"+"ipt>");}
catch(e){var _ec=document.createElement("script");_ec.src=_eb;document.getElementsByTagName("head")[0].appendChild(_ec);}}}})();dojo.provide("dojo.lang.common");dojo.lang.inherits=function(_ed,_ee){if(typeof _ee!="function"){dojo.raise("dojo.inherits: superclass argument ["+_ee+"] must be a function (subclass: ["+_ed+"']");}
_ed.prototype=new _ee();_ed.prototype.constructor=_ed;_ed.superclass=_ee.prototype;_ed["super"]=_ee.prototype;};dojo.lang._mixin=function(obj,_f0){var _f1={};for(var x in _f0){if((typeof _f1[x]=="undefined")||(_f1[x]!=_f0[x])){obj[x]=_f0[x];}}
if(dojo.render.html.ie&&(typeof (_f0["toString"])=="function")&&(_f0["toString"]!=obj["toString"])&&(_f0["toString"]!=_f1["toString"])){obj.toString=_f0.toString;}
return obj;};dojo.lang.mixin=function(obj,_f4){for(var i=1,l=arguments.length;i<l;i++){dojo.lang._mixin(obj,arguments[i]);}
return obj;};dojo.lang.extend=function(_f7,_f8){for(var i=1,l=arguments.length;i<l;i++){dojo.lang._mixin(_f7.prototype,arguments[i]);}
return _f7;};dojo.inherits=dojo.lang.inherits;dojo.mixin=dojo.lang.mixin;dojo.extend=dojo.lang.extend;dojo.lang.find=function(_fb,_fc,_fd,_fe){if(!dojo.lang.isArrayLike(_fb)&&dojo.lang.isArrayLike(_fc)){dojo.deprecated("dojo.lang.find(value, array)","use dojo.lang.find(array, value) instead","0.5");var _ff=_fb;_fb=_fc;_fc=_ff;}
var _100=dojo.lang.isString(_fb);if(_100){_fb=_fb.split("");}
if(_fe){var step=-1;var i=_fb.length-1;var end=-1;}else{var step=1;var i=0;var end=_fb.length;}
if(_fd){while(i!=end){if(_fb[i]===_fc){return i;}
i+=step;}}else{while(i!=end){if(_fb[i]==_fc){return i;}
i+=step;}}
return -1;};dojo.lang.indexOf=dojo.lang.find;dojo.lang.findLast=function(_104,_105,_106){return dojo.lang.find(_104,_105,_106,true);};dojo.lang.lastIndexOf=dojo.lang.findLast;dojo.lang.inArray=function(_107,_108){return dojo.lang.find(_107,_108)>-1;};dojo.lang.isObject=function(it){if(typeof it=="undefined"){return false;}
return (typeof it=="object"||it===null||dojo.lang.isArray(it)||dojo.lang.isFunction(it));};dojo.lang.isArray=function(it){return (it&&it instanceof Array||typeof it=="array");};dojo.lang.isArrayLike=function(it){if((!it)||(dojo.lang.isUndefined(it))){return false;}
if(dojo.lang.isString(it)){return false;}
if(dojo.lang.isFunction(it)){return false;}
if(dojo.lang.isArray(it)){return true;}
if((it.tagName)&&(it.tagName.toLowerCase()=="form")){return false;}
if(dojo.lang.isNumber(it.length)&&isFinite(it.length)){return true;}
return false;};dojo.lang.isFunction=function(it){if(!it){return false;}
if((typeof (it)=="function")&&(it=="[object NodeList]")){return false;}
return (it instanceof Function||typeof it=="function");};dojo.lang.isString=function(it){return (typeof it=="string"||it instanceof String);};dojo.lang.isAlien=function(it){if(!it){return false;}
return !dojo.lang.isFunction()&&/\{\s*\[native code\]\s*\}/.test(String(it));};dojo.lang.isBoolean=function(it){return (it instanceof Boolean||typeof it=="boolean");};dojo.lang.isNumber=function(it){return (it instanceof Number||typeof it=="number");};dojo.lang.isUndefined=function(it){return ((typeof (it)=="undefined")&&(it==undefined));};dojo.provide("dojo.lang.array");dojo.lang.mixin(dojo.lang,{has:function(obj,name){try{return typeof obj[name]!="undefined";}
catch(e){return false;}},isEmpty:function(obj){if(dojo.lang.isObject(obj)){var tmp={};var _116=0;for(var x in obj){if(obj[x]&&(!tmp[x])){_116++;break;}}
return _116==0;}else{if(dojo.lang.isArrayLike(obj)||dojo.lang.isString(obj)){return obj.length==0;}}},map:function(arr,obj,_11a){var _11b=dojo.lang.isString(arr);if(_11b){arr=arr.split("");}
if(dojo.lang.isFunction(obj)&&(!_11a)){_11a=obj;obj=dj_global;}else{if(dojo.lang.isFunction(obj)&&_11a){var _11c=obj;obj=_11a;_11a=_11c;}}
if(Array.map){var _11d=Array.map(arr,_11a,obj);}else{var _11d=[];for(var i=0;i<arr.length;++i){_11d.push(_11a.call(obj,arr[i]));}}
if(_11b){return _11d.join("");}else{return _11d;}},reduce:function(arr,_120,obj,_122){var _123=_120;var ob=obj?obj:dj_global;dojo.lang.map(arr,function(val){_123=_122.call(ob,_123,val);});return _123;},forEach:function(_126,_127,_128){if(dojo.lang.isString(_126)){_126=_126.split("");}
if(Array.forEach){Array.forEach(_126,_127,_128);}else{if(!_128){_128=dj_global;}
for(var i=0,l=_126.length;i<l;i++){_127.call(_128,_126[i],i,_126);}}},_everyOrSome:function(_12b,arr,_12d,_12e){if(dojo.lang.isString(arr)){arr=arr.split("");}
if(Array.every){return Array[_12b?"every":"some"](arr,_12d,_12e);}else{if(!_12e){_12e=dj_global;}
for(var i=0,l=arr.length;i<l;i++){var _131=_12d.call(_12e,arr[i],i,arr);if(_12b&&!_131){return false;}else{if((!_12b)&&(_131)){return true;}}}
return Boolean(_12b);}},every:function(arr,_133,_134){return this._everyOrSome(true,arr,_133,_134);},some:function(arr,_136,_137){return this._everyOrSome(false,arr,_136,_137);},filter:function(arr,_139,_13a){var _13b=dojo.lang.isString(arr);if(_13b){arr=arr.split("");}
var _13c;if(Array.filter){_13c=Array.filter(arr,_139,_13a);}else{if(!_13a){if(arguments.length>=3){dojo.raise("thisObject doesn't exist!");}
_13a=dj_global;}
_13c=[];for(var i=0;i<arr.length;i++){if(_139.call(_13a,arr[i],i,arr)){_13c.push(arr[i]);}}}
if(_13b){return _13c.join("");}else{return _13c;}},unnest:function(){var out=[];for(var i=0;i<arguments.length;i++){if(dojo.lang.isArrayLike(arguments[i])){var add=dojo.lang.unnest.apply(this,arguments[i]);out=out.concat(add);}else{out.push(arguments[i]);}}
return out;},toArray:function(_141,_142){var _143=[];for(var i=_142||0;i<_141.length;i++){_143.push(_141[i]);}
return _143;}});dojo.provide("dojo.lang.extras");dojo.lang.setTimeout=function(func,_146){var _147=window,_148=2;if(!dojo.lang.isFunction(func)){_147=func;func=_146;_146=arguments[2];_148++;}
if(dojo.lang.isString(func)){func=_147[func];}
var args=[];for(var i=_148;i<arguments.length;i++){args.push(arguments[i]);}
return dojo.global().setTimeout(function(){func.apply(_147,args);},_146);};dojo.lang.clearTimeout=function(_14b){dojo.global().clearTimeout(_14b);};dojo.lang.getNameInObj=function(ns,item){if(!ns){ns=dj_global;}
for(var x in ns){if(ns[x]===item){return new String(x);}}
return null;};dojo.lang.shallowCopy=function(obj,deep){var i,ret;if(obj===null){return null;}
if(dojo.lang.isObject(obj)){ret=new obj.constructor();for(i in obj){if(dojo.lang.isUndefined(ret[i])){ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];}}}else{if(dojo.lang.isArray(obj)){ret=[];for(i=0;i<obj.length;i++){ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];}}else{ret=obj;}}
return ret;};dojo.lang.firstValued=function(){for(var i=0;i<arguments.length;i++){if(typeof arguments[i]!="undefined"){return arguments[i];}}
return undefined;};dojo.lang.getObjPathValue=function(_154,_155,_156){with(dojo.parseObjPath(_154,_155,_156)){return dojo.evalProp(prop,obj,_156);}};dojo.lang.setObjPathValue=function(_157,_158,_159,_15a){if(arguments.length<4){_15a=true;}
with(dojo.parseObjPath(_157,_159,_15a)){if(obj&&(_15a||(prop in obj))){obj[prop]=_158;}}};dojo.provide("dojo.lang.func");dojo.lang.hitch=function(_15b,_15c){var fcn=(dojo.lang.isString(_15c)?_15b[_15c]:_15c)||function(){};return function(){return fcn.apply(_15b,arguments);};};dojo.lang.anonCtr=0;dojo.lang.anon={};dojo.lang.nameAnonFunc=function(_15e,_15f,_160){var nso=(_15f||dojo.lang.anon);if((_160)||((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true))){for(var x in nso){try{if(nso[x]===_15e){return x;}}
catch(e){}}}
var ret="__"+dojo.lang.anonCtr++;while(typeof nso[ret]!="undefined"){ret="__"+dojo.lang.anonCtr++;}
nso[ret]=_15e;return ret;};dojo.lang.forward=function(_164){return function(){return this[_164].apply(this,arguments);};};dojo.lang.curry=function(ns,func){var _167=[];ns=ns||dj_global;if(dojo.lang.isString(func)){func=ns[func];}
for(var x=2;x<arguments.length;x++){_167.push(arguments[x]);}
var _169=(func["__preJoinArity"]||func.length)-_167.length;function gather(_16a,_16b,_16c){var _16d=_16c;var _16e=_16b.slice(0);for(var x=0;x<_16a.length;x++){_16e.push(_16a[x]);}
_16c=_16c-_16a.length;if(_16c<=0){var res=func.apply(ns,_16e);_16c=_16d;return res;}else{return function(){return gather(arguments,_16e,_16c);};}}
return gather([],_167,_169);};dojo.lang.curryArguments=function(ns,func,args,_174){var _175=[];var x=_174||0;for(x=_174;x<args.length;x++){_175.push(args[x]);}
return dojo.lang.curry.apply(dojo.lang,[ns,func].concat(_175));};dojo.lang.tryThese=function(){for(var x=0;x<arguments.length;x++){try{if(typeof arguments[x]=="function"){var ret=(arguments[x]());if(ret){return ret;}}}
catch(e){dojo.debug(e);}}};dojo.lang.delayThese=function(farr,cb,_17b,_17c){if(!farr.length){if(typeof _17c=="function"){_17c();}
return;}
if((typeof _17b=="undefined")&&(typeof cb=="number")){_17b=cb;cb=function(){};}else{if(!cb){cb=function(){};if(!_17b){_17b=0;}}}
setTimeout(function(){(farr.shift())();cb();dojo.lang.delayThese(farr,cb,_17b,_17c);},_17b);};dojo.provide("dojo.event.common");dojo.event=new function(){this._canTimeout=dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);function interpolateArgs(args,_17e){var dl=dojo.lang;var ao={srcObj:dj_global,srcFunc:null,adviceObj:dj_global,adviceFunc:null,aroundObj:null,aroundFunc:null,adviceType:(args.length>2)?args[0]:"after",precedence:"last",once:false,delay:null,rate:0,adviceMsg:false};switch(args.length){case 0:
return;case 1:
return;case 2:
ao.srcFunc=args[0];ao.adviceFunc=args[1];break;case 3:
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isString(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];ao.adviceFunc=args[2];}else{if((dl.isString(args[1]))&&(dl.isString(args[2]))){ao.srcFunc=args[1];ao.adviceFunc=args[2];}else{if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isFunction(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];var _181=dl.nameAnonFunc(args[2],ao.adviceObj,_17e);ao.adviceFunc=_181;}else{if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){ao.adviceType="after";ao.srcObj=dj_global;var _181=dl.nameAnonFunc(args[0],ao.srcObj,_17e);ao.srcFunc=_181;ao.adviceObj=args[1];ao.adviceFunc=args[2];}}}}
break;case 4:
if((dl.isObject(args[0]))&&(dl.isObject(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isString(args[1]))&&(dl.isObject(args[2]))){ao.adviceType=args[0];ao.srcObj=dj_global;ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isFunction(args[1]))&&(dl.isObject(args[2]))){ao.adviceType=args[0];ao.srcObj=dj_global;var _181=dl.nameAnonFunc(args[1],dj_global,_17e);ao.srcFunc=_181;ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){ao.srcObj=args[1];ao.srcFunc=args[2];var _181=dl.nameAnonFunc(args[3],dj_global,_17e);ao.adviceObj=dj_global;ao.adviceFunc=_181;}else{if(dl.isObject(args[1])){ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=dj_global;ao.adviceFunc=args[3];}else{if(dl.isObject(args[2])){ao.srcObj=dj_global;ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{ao.srcObj=ao.adviceObj=ao.aroundObj=dj_global;ao.srcFunc=args[1];ao.adviceFunc=args[2];ao.aroundFunc=args[3];}}}}}}
break;case 6:
ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=args[3];ao.adviceFunc=args[4];ao.aroundFunc=args[5];ao.aroundObj=dj_global;break;default:
ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=args[3];ao.adviceFunc=args[4];ao.aroundObj=args[5];ao.aroundFunc=args[6];ao.once=args[7];ao.delay=args[8];ao.rate=args[9];ao.adviceMsg=args[10];break;}
if(dl.isFunction(ao.aroundFunc)){var _181=dl.nameAnonFunc(ao.aroundFunc,ao.aroundObj,_17e);ao.aroundFunc=_181;}
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
if(dojo.lang.isArray(ao.srcObj)&&ao.srcObj!=""){var _183={};for(var x in ao){_183[x]=ao[x];}
var mjps=[];dojo.lang.forEach(ao.srcObj,function(src){if((dojo.render.html.capable)&&(dojo.lang.isString(src))){src=dojo.byId(src);}
_183.srcObj=src;mjps.push(dojo.event.connect.call(dojo.event,_183));});return mjps;}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);if(ao.adviceFunc){var mjp2=dojo.event.MethodJoinPoint.getForMethod(ao.adviceObj,ao.adviceFunc);}
mjp.kwAddAdvice(ao);return mjp;};this.log=function(a1,a2){var _18b;if((arguments.length==1)&&(typeof a1=="object")){_18b=a1;}else{_18b={srcObj:a1,srcFunc:a2};}
_18b.adviceFunc=function(){var _18c=[];for(var x=0;x<arguments.length;x++){_18c.push(arguments[x]);}
dojo.debug("("+_18b.srcObj+")."+_18b.srcFunc,":",_18c.join(", "));};this.kwConnect(_18b);};this.connectBefore=function(){var args=["before"];for(var i=0;i<arguments.length;i++){args.push(arguments[i]);}
return this.connect.apply(this,args);};this.connectAround=function(){var args=["around"];for(var i=0;i<arguments.length;i++){args.push(arguments[i]);}
return this.connect.apply(this,args);};this.connectOnce=function(){var ao=interpolateArgs(arguments,true);ao.once=true;return this.connect(ao);};this._kwConnectImpl=function(_193,_194){var fn=(_194)?"disconnect":"connect";if(typeof _193["srcFunc"]=="function"){_193.srcObj=_193["srcObj"]||dj_global;var _196=dojo.lang.nameAnonFunc(_193.srcFunc,_193.srcObj,true);_193.srcFunc=_196;}
if(typeof _193["adviceFunc"]=="function"){_193.adviceObj=_193["adviceObj"]||dj_global;var _196=dojo.lang.nameAnonFunc(_193.adviceFunc,_193.adviceObj,true);_193.adviceFunc=_196;}
_193.srcObj=_193["srcObj"]||dj_global;_193.adviceObj=_193["adviceObj"]||_193["targetObj"]||dj_global;_193.adviceFunc=_193["adviceFunc"]||_193["targetFunc"];return dojo.event[fn](_193);};this.kwConnect=function(_197){return this._kwConnectImpl(_197,false);};this.disconnect=function(){if(arguments.length==1){var ao=arguments[0];}else{var ao=interpolateArgs(arguments,true);}
if(!ao.adviceFunc){return;}
if(dojo.lang.isString(ao.srcFunc)&&(ao.srcFunc.toLowerCase()=="onkey")){if(dojo.render.html.ie){ao.srcFunc="onkeydown";this.disconnect(ao);}
ao.srcFunc="onkeypress";}
if(!ao.srcObj[ao.srcFunc]){return null;}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc,true);mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);return mjp;};this.kwDisconnect=function(_19a){return this._kwConnectImpl(_19a,true);};};dojo.event.MethodInvocation=function(_19b,obj,args){this.jp_=_19b;this.object=obj;this.args=[];for(var x=0;x<args.length;x++){this.args[x]=args[x];}
this.around_index=-1;};dojo.event.MethodInvocation.prototype.proceed=function(){this.around_index++;if(this.around_index>=this.jp_.around.length){return this.jp_.object[this.jp_.methodname].apply(this.jp_.object,this.args);}else{var ti=this.jp_.around[this.around_index];var mobj=ti[0]||dj_global;var meth=ti[1];return mobj[meth].call(mobj,this);}};dojo.event.MethodJoinPoint=function(obj,_1a3){this.object=obj||dj_global;this.methodname=_1a3;this.methodfunc=this.object[_1a3];this.squelch=false;};dojo.event.MethodJoinPoint.getForMethod=function(obj,_1a5){if(!obj){obj=dj_global;}
if(!obj[_1a5]){obj[_1a5]=function(){};if(!obj[_1a5]){dojo.raise("Cannot set do-nothing method on that object "+_1a5);}}else{if((!dojo.lang.isFunction(obj[_1a5]))&&(!dojo.lang.isAlien(obj[_1a5]))){return null;}}
var _1a6=_1a5+"$joinpoint";var _1a7=_1a5+"$joinpoint$method";var _1a8=obj[_1a6];if(!_1a8){var _1a9=false;if(dojo.event["browser"]){if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){_1a9=true;dojo.event.browser.addClobberNodeAttrs(obj,[_1a6,_1a7,_1a5]);}}
var _1aa=obj[_1a5].length;obj[_1a7]=obj[_1a5];_1a8=obj[_1a6]=new dojo.event.MethodJoinPoint(obj,_1a7);obj[_1a5]=function(){var args=[];if((_1a9)&&(!arguments.length)){var evt=null;try{if(obj.ownerDocument){evt=obj.ownerDocument.parentWindow.event;}else{if(obj.documentElement){evt=obj.documentElement.ownerDocument.parentWindow.event;}else{if(obj.event){evt=obj.event;}else{evt=window.event;}}}}
catch(e){evt=window.event;}
if(evt){args.push(dojo.event.browser.fixEvent(evt,this));}}else{for(var x=0;x<arguments.length;x++){if((x==0)&&(_1a9)&&(dojo.event.browser.isEvent(arguments[x]))){args.push(dojo.event.browser.fixEvent(arguments[x],this));}else{args.push(arguments[x]);}}}
return _1a8.run.apply(_1a8,args);};obj[_1a5].__preJoinArity=_1aa;}
return _1a8;};dojo.lang.extend(dojo.event.MethodJoinPoint,{unintercept:function(){this.object[this.methodname]=this.methodfunc;this.before=[];this.after=[];this.around=[];},disconnect:dojo.lang.forward("unintercept"),run:function(){var obj=this.object||dj_global;var args=arguments;var _1b0=[];for(var x=0;x<args.length;x++){_1b0[x]=args[x];}
var _1b2=function(marr){if(!marr){dojo.debug("Null argument to unrollAdvice()");return;}
var _1b4=marr[0]||dj_global;var _1b5=marr[1];if(!_1b4[_1b5]){dojo.raise("function \""+_1b5+"\" does not exist on \""+_1b4+"\"");}
var _1b6=marr[2]||dj_global;var _1b7=marr[3];var msg=marr[6];var _1b9;var to={args:[],jp_:this,object:obj,proceed:function(){return _1b4[_1b5].apply(_1b4,to.args);}};to.args=_1b0;var _1bb=parseInt(marr[4]);var _1bc=((!isNaN(_1bb))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));if(marr[5]){var rate=parseInt(marr[5]);var cur=new Date();var _1bf=false;if((marr["last"])&&((cur-marr.last)<=rate)){if(dojo.event._canTimeout){if(marr["delayTimer"]){clearTimeout(marr.delayTimer);}
var tod=parseInt(rate*2);var mcpy=dojo.lang.shallowCopy(marr);marr.delayTimer=setTimeout(function(){mcpy[5]=0;_1b2(mcpy);},tod);}
return;}else{marr.last=cur;}}
if(_1b7){_1b6[_1b7].call(_1b6,to);}else{if((_1bc)&&((dojo.render.html)||(dojo.render.svg))){dj_global["setTimeout"](function(){if(msg){_1b4[_1b5].call(_1b4,to);}else{_1b4[_1b5].apply(_1b4,args);}},_1bb);}else{if(msg){_1b4[_1b5].call(_1b4,to);}else{_1b4[_1b5].apply(_1b4,args);}}}};var _1c2=function(){if(this.squelch){try{return _1b2.apply(this,arguments);}
catch(e){dojo.debug(e);}}else{return _1b2.apply(this,arguments);}};if((this["before"])&&(this.before.length>0)){dojo.lang.forEach(this.before.concat(new Array()),_1c2);}
var _1c3;try{if((this["around"])&&(this.around.length>0)){var mi=new dojo.event.MethodInvocation(this,obj,args);_1c3=mi.proceed();}else{if(this.methodfunc){_1c3=this.object[this.methodname].apply(this.object,args);}}}
catch(e){if(!this.squelch){dojo.debug(e,"when calling",this.methodname,"on",this.object,"with arguments",args);dojo.raise(e);}}
if((this["after"])&&(this.after.length>0)){dojo.lang.forEach(this.after.concat(new Array()),_1c2);}
return (this.methodfunc)?_1c3:null;},getArr:function(kind){var type="after";if((typeof kind=="string")&&(kind.indexOf("before")!=-1)){type="before";}else{if(kind=="around"){type="around";}}
if(!this[type]){this[type]=[];}
return this[type];},kwAddAdvice:function(args){this.addAdvice(args["adviceObj"],args["adviceFunc"],args["aroundObj"],args["aroundFunc"],args["adviceType"],args["precedence"],args["once"],args["delay"],args["rate"],args["adviceMsg"]);},addAdvice:function(_1c8,_1c9,_1ca,_1cb,_1cc,_1cd,once,_1cf,rate,_1d1){var arr=this.getArr(_1cc);if(!arr){dojo.raise("bad this: "+this);}
var ao=[_1c8,_1c9,_1ca,_1cb,_1cf,rate,_1d1];if(once){if(this.hasAdvice(_1c8,_1c9,_1cc,arr)>=0){return;}}
if(_1cd=="first"){arr.unshift(ao);}else{arr.push(ao);}},hasAdvice:function(_1d4,_1d5,_1d6,arr){if(!arr){arr=this.getArr(_1d6);}
var ind=-1;for(var x=0;x<arr.length;x++){var aao=(typeof _1d5=="object")?(new String(_1d5)).toString():_1d5;var a1o=(typeof arr[x][1]=="object")?(new String(arr[x][1])).toString():arr[x][1];if((arr[x][0]==_1d4)&&(a1o==aao)){ind=x;}}
return ind;},removeAdvice:function(_1dc,_1dd,_1de,once){var arr=this.getArr(_1de);var ind=this.hasAdvice(_1dc,_1dd,_1de,arr);if(ind==-1){return false;}
while(ind!=-1){arr.splice(ind,1);if(once){break;}
ind=this.hasAdvice(_1dc,_1dd,_1de,arr);}
return true;}});dojo.provide("dojo.event.browser");dojo._ie_clobber=new function(){this.clobberNodes=[];function nukeProp(node,prop){try{node[prop]=null;}
catch(e){}
try{delete node[prop];}
catch(e){}
try{node.removeAttribute(prop);}
catch(e){}}
this.clobber=function(_1e4){var na;var tna;if(_1e4){tna=_1e4.all||_1e4.getElementsByTagName("*");na=[_1e4];for(var x=0;x<tna.length;x++){if(tna[x]["__doClobber__"]){na.push(tna[x]);}}}else{try{window.onload=null;}
catch(e){}
na=(this.clobberNodes.length)?this.clobberNodes:document.all;}
tna=null;var _1e8={};for(var i=na.length-1;i>=0;i=i-1){var el=na[i];try{if(el&&el["__clobberAttrs__"]){for(var j=0;j<el.__clobberAttrs__.length;j++){nukeProp(el,el.__clobberAttrs__[j]);}
nukeProp(el,"__clobberAttrs__");nukeProp(el,"__doClobber__");}}
catch(e){}}
na=null;};};if(dojo.render.html.ie){dojo.addOnUnload(function(){dojo._ie_clobber.clobber();try{if((dojo["widget"])&&(dojo.widget["manager"])){dojo.widget.manager.destroyAll();}}
catch(e){}
for(var name in dojo.widget._templateCache){if(dojo.widget._templateCache[name].node){dojo.dom.removeNode(dojo.widget._templateCache[name].node);dojo.widget._templateCache[name].node=null;delete dojo.widget._templateCache[name].node;}}
while(dojo.dom._ieRemovedNodes.length>0){var node=dojo.dom._ieRemovedNodes.pop();dojo.dom._discardElement(node);node=null;}
try{window.onload=null;}
catch(e){}
try{window.onunload=null;}
catch(e){}
dojo._ie_clobber.clobberNodes=[];});}
dojo.event.browser=new function(){var _1ee=0;this.normalizedEventName=function(_1ef){switch(_1ef){case "CheckboxStateChange":
case "DOMAttrModified":
case "DOMMenuItemActive":
case "DOMMenuItemInactive":
case "DOMMouseScroll":
case "DOMNodeInserted":
case "DOMNodeRemoved":
case "RadioStateChange":
return _1ef;break;default:
return _1ef.toLowerCase();break;}};this.clean=function(node){if(dojo.render.html.ie){dojo._ie_clobber.clobber(node);}};this.addClobberNode=function(node){if(!dojo.render.html.ie){return;}
if(!node["__doClobber__"]){node.__doClobber__=true;dojo._ie_clobber.clobberNodes.push(node);node.__clobberAttrs__=[];}};this.addClobberNodeAttrs=function(node,_1f3){if(!dojo.render.html.ie){return;}
this.addClobberNode(node);for(var x=0;x<_1f3.length;x++){node.__clobberAttrs__.push(_1f3[x]);}};this.removeListener=function(node,_1f6,fp,_1f8){if(!_1f8){var _1f8=false;}
_1f6=dojo.event.browser.normalizedEventName(_1f6);if((_1f6=="onkey")||(_1f6=="key")){if(dojo.render.html.ie){this.removeListener(node,"onkeydown",fp,_1f8);}
_1f6="onkeypress";}
if(_1f6.substr(0,2)=="on"){_1f6=_1f6.substr(2);}
if(node.removeEventListener){node.removeEventListener(_1f6,fp,_1f8);}};this.addListener=function(node,_1fa,fp,_1fc,_1fd){if(!node){return;}
if(!_1fc){var _1fc=false;}
_1fa=dojo.event.browser.normalizedEventName(_1fa);if((_1fa=="onkey")||(_1fa=="key")){if(dojo.render.html.ie){this.addListener(node,"onkeydown",fp,_1fc,_1fd);}
_1fa="onkeypress";}
if(_1fa.substr(0,2)!="on"){_1fa="on"+_1fa;}
if(!_1fd){var _1fe=function(evt){if(!evt){evt=window.event;}
var ret=fp(dojo.event.browser.fixEvent(evt,this));if(_1fc){dojo.event.browser.stopEvent(evt);}
return ret;};}else{_1fe=fp;}
if(node.addEventListener){node.addEventListener(_1fa.substr(2),_1fe,_1fc);return _1fe;}else{if(typeof node[_1fa]=="function"){var _201=node[_1fa];node[_1fa]=function(e){_201(e);return _1fe(e);};}else{node[_1fa]=_1fe;}
if(dojo.render.html.ie){this.addClobberNodeAttrs(node,[_1fa]);}
return _1fe;}};this.isEvent=function(obj){return (typeof obj!="undefined")&&(typeof Event!="undefined")&&(obj.eventPhase);};this.currentEvent=null;this.callListener=function(_204,_205){if(typeof _204!="function"){dojo.raise("listener not a function: "+_204);}
dojo.event.browser.currentEvent.currentTarget=_205;return _204.call(_205,dojo.event.browser.currentEvent);};this._stopPropagation=function(){dojo.event.browser.currentEvent.cancelBubble=true;};this._preventDefault=function(){dojo.event.browser.currentEvent.returnValue=false;};this.keys={KEY_BACKSPACE:8,KEY_TAB:9,KEY_CLEAR:12,KEY_ENTER:13,KEY_SHIFT:16,KEY_CTRL:17,KEY_ALT:18,KEY_PAUSE:19,KEY_CAPS_LOCK:20,KEY_ESCAPE:27,KEY_SPACE:32,KEY_PAGE_UP:33,KEY_PAGE_DOWN:34,KEY_END:35,KEY_HOME:36,KEY_LEFT_ARROW:37,KEY_UP_ARROW:38,KEY_RIGHT_ARROW:39,KEY_DOWN_ARROW:40,KEY_INSERT:45,KEY_DELETE:46,KEY_HELP:47,KEY_LEFT_WINDOW:91,KEY_RIGHT_WINDOW:92,KEY_SELECT:93,KEY_NUMPAD_0:96,KEY_NUMPAD_1:97,KEY_NUMPAD_2:98,KEY_NUMPAD_3:99,KEY_NUMPAD_4:100,KEY_NUMPAD_5:101,KEY_NUMPAD_6:102,KEY_NUMPAD_7:103,KEY_NUMPAD_8:104,KEY_NUMPAD_9:105,KEY_NUMPAD_MULTIPLY:106,KEY_NUMPAD_PLUS:107,KEY_NUMPAD_ENTER:108,KEY_NUMPAD_MINUS:109,KEY_NUMPAD_PERIOD:110,KEY_NUMPAD_DIVIDE:111,KEY_F1:112,KEY_F2:113,KEY_F3:114,KEY_F4:115,KEY_F5:116,KEY_F6:117,KEY_F7:118,KEY_F8:119,KEY_F9:120,KEY_F10:121,KEY_F11:122,KEY_F12:123,KEY_F13:124,KEY_F14:125,KEY_F15:126,KEY_NUM_LOCK:144,KEY_SCROLL_LOCK:145};this.revKeys=[];for(var key in this.keys){this.revKeys[this.keys[key]]=key;}
this.fixEvent=function(evt,_208){if(!evt){if(window["event"]){evt=window.event;}}
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
if(evt.ctrlKey||evt.altKey){var _20a=evt.keyCode;if(_20a>=65&&_20a<=90&&evt.shiftKey==false){_20a+=32;}
if(_20a>=1&&_20a<=26&&evt.ctrlKey){_20a+=96;}
evt.key=String.fromCharCode(_20a);}}}else{if(evt["type"]=="keypress"){if(dojo.render.html.opera){if(evt.which==0){evt.key=evt.keyCode;}else{if(evt.which>0){switch(evt.which){case evt.KEY_SHIFT:
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
var _20a=evt.which;if((evt.ctrlKey||evt.altKey||evt.metaKey)&&(evt.which>=65&&evt.which<=90&&evt.shiftKey==false)){_20a+=32;}
evt.key=String.fromCharCode(_20a);}}}}else{if(dojo.render.html.ie){if(!evt.ctrlKey&&!evt.altKey&&evt.keyCode>=evt.KEY_SPACE){evt.key=String.fromCharCode(evt.keyCode);}}else{if(dojo.render.html.safari){switch(evt.keyCode){case 25:
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
if(!evt.currentTarget){evt.currentTarget=(_208?_208:evt.srcElement);}
if(!evt.layerX){evt.layerX=evt.offsetX;}
if(!evt.layerY){evt.layerY=evt.offsetY;}
var doc=(evt.srcElement&&evt.srcElement.ownerDocument)?evt.srcElement.ownerDocument:document;var _20c=((dojo.render.html.ie55)||(doc["compatMode"]=="BackCompat"))?doc.body:doc.documentElement;if(!evt.pageX){evt.pageX=evt.clientX+(_20c.scrollLeft||0);}
if(!evt.pageY){evt.pageY=evt.clientY+(_20c.scrollTop||0);}
if(evt.type=="mouseover"){evt.relatedTarget=evt.fromElement;}
if(evt.type=="mouseout"){evt.relatedTarget=evt.toElement;}
this.currentEvent=evt;evt.callListener=this.callListener;evt.stopPropagation=this._stopPropagation;evt.preventDefault=this._preventDefault;}
return evt;};this.stopEvent=function(evt){if(window.event){evt.cancelBubble=true;evt.returnValue=false;}else{evt.preventDefault();evt.stopPropagation();}};};dojo.provide("dojo.dom");dojo.dom.ELEMENT_NODE=1;dojo.dom.ATTRIBUTE_NODE=2;dojo.dom.TEXT_NODE=3;dojo.dom.CDATA_SECTION_NODE=4;dojo.dom.ENTITY_REFERENCE_NODE=5;dojo.dom.ENTITY_NODE=6;dojo.dom.PROCESSING_INSTRUCTION_NODE=7;dojo.dom.COMMENT_NODE=8;dojo.dom.DOCUMENT_NODE=9;dojo.dom.DOCUMENT_TYPE_NODE=10;dojo.dom.DOCUMENT_FRAGMENT_NODE=11;dojo.dom.NOTATION_NODE=12;dojo.dom.dojoml="http://www.dojotoolkit.org/2004/dojoml";dojo.dom.xmlns={svg:"http://www.w3.org/2000/svg",smil:"http://www.w3.org/2001/SMIL20/",mml:"http://www.w3.org/1998/Math/MathML",cml:"http://www.xml-cml.org",xlink:"http://www.w3.org/1999/xlink",xhtml:"http://www.w3.org/1999/xhtml",xul:"http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul",xbl:"http://www.mozilla.org/xbl",fo:"http://www.w3.org/1999/XSL/Format",xsl:"http://www.w3.org/1999/XSL/Transform",xslt:"http://www.w3.org/1999/XSL/Transform",xi:"http://www.w3.org/2001/XInclude",xforms:"http://www.w3.org/2002/01/xforms",saxon:"http://icl.com/saxon",xalan:"http://xml.apache.org/xslt",xsd:"http://www.w3.org/2001/XMLSchema",dt:"http://www.w3.org/2001/XMLSchema-datatypes",xsi:"http://www.w3.org/2001/XMLSchema-instance",rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#",rdfs:"http://www.w3.org/2000/01/rdf-schema#",dc:"http://purl.org/dc/elements/1.1/",dcq:"http://purl.org/dc/qualifiers/1.0","soap-env":"http://schemas.xmlsoap.org/soap/envelope/",wsdl:"http://schemas.xmlsoap.org/wsdl/",AdobeExtensions:"http://ns.adobe.com/AdobeSVGViewerExtensions/3.0/"};dojo.dom.isNode=function(wh){if(typeof Element=="function"){try{return wh instanceof Element;}
catch(e){}}else{return wh&&!isNaN(wh.nodeType);}};dojo.dom.getUniqueId=function(){var _20f=dojo.doc();do{var id="dj_unique_"+(++arguments.callee._idIncrement);}while(_20f.getElementById(id));return id;};dojo.dom.getUniqueId._idIncrement=0;dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_211,_212){var node=_211.firstChild;while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){node=node.nextSibling;}
if(_212&&node&&node.tagName&&node.tagName.toLowerCase()!=_212.toLowerCase()){node=dojo.dom.nextElement(node,_212);}
return node;};dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_214,_215){var node=_214.lastChild;while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){node=node.previousSibling;}
if(_215&&node&&node.tagName&&node.tagName.toLowerCase()!=_215.toLowerCase()){node=dojo.dom.prevElement(node,_215);}
return node;};dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(node,_218){if(!node){return null;}
do{node=node.nextSibling;}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);if(node&&_218&&_218.toLowerCase()!=node.tagName.toLowerCase()){return dojo.dom.nextElement(node,_218);}
return node;};dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(node,_21a){if(!node){return null;}
if(_21a){_21a=_21a.toLowerCase();}
do{node=node.previousSibling;}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);if(node&&_21a&&_21a.toLowerCase()!=node.tagName.toLowerCase()){return dojo.dom.prevElement(node,_21a);}
return node;};dojo.dom.moveChildren=function(_21b,_21c,trim){var _21e=0;if(trim){while(_21b.hasChildNodes()&&_21b.firstChild.nodeType==dojo.dom.TEXT_NODE){_21b.removeChild(_21b.firstChild);}
while(_21b.hasChildNodes()&&_21b.lastChild.nodeType==dojo.dom.TEXT_NODE){_21b.removeChild(_21b.lastChild);}}
while(_21b.hasChildNodes()){_21c.appendChild(_21b.firstChild);_21e++;}
return _21e;};dojo.dom.copyChildren=function(_21f,_220,trim){var _222=_21f.cloneNode(true);return this.moveChildren(_222,_220,trim);};dojo.dom.replaceChildren=function(node,_224){dojo.dom.removeChildren(node);node.appendChild(_224);};dojo.dom.removeChildren=function(node){var _226=node.childNodes.length;while(node.hasChildNodes()){dojo.dom.removeNode(node.firstChild);}
return _226;};dojo.dom.replaceNode=function(node,_228){if(dojo.render.html.ie){node.parentNode.insertBefore(_228,node);return dojo.dom.removeNode(node);}else{return node.parentNode.replaceChild(_228,node);}};dojo.dom._ieRemovedNodes=[];dojo.dom.removeNode=function(node,_22a){if(node&&node.parentNode){try{if(_22a&&dojo.evalObjPath("dojo.event.browser.clean",false)){dojo.event.browser.clean(node);}}
catch(e){}
if(dojo.render.html.ie){if(_22a){dojo.dom._discardElement(node);}else{dojo.dom._ieRemovedNodes.push(node);}}
if(_22a){return null;}
return node.parentNode.removeChild(node);}};dojo.dom._discardElement=function(_22b){var _22c=document.getElementById("IELeakGarbageBin");if(!_22c){_22c=document.createElement("DIV");_22c.id="IELeakGarbageBin";_22c.style.display="none";document.body.appendChild(_22c);}
_22c.appendChild(_22b);_22c.innerHTML="";};dojo.dom.getAncestors=function(node,_22e,_22f){var _230=[];var _231=(_22e&&(_22e instanceof Function||typeof _22e=="function"));while(node){if(!_231||_22e(node)){_230.push(node);}
if(_22f&&_230.length>0){return _230[0];}
node=node.parentNode;}
if(_22f){return null;}
return _230;};dojo.dom.getAncestorsByTag=function(node,tag,_234){tag=tag.toLowerCase();return dojo.dom.getAncestors(node,function(el){return ((el.tagName)&&(el.tagName.toLowerCase()==tag));},_234);};dojo.dom.getFirstAncestorByTag=function(node,tag){return dojo.dom.getAncestorsByTag(node,tag,true);};dojo.dom.isDescendantOf=function(node,_239,_23a){if(_23a&&node){node=node.parentNode;}
while(node){if(node==_239){return true;}
node=node.parentNode;}
return false;};dojo.dom.innerXML=function(node){if(node.innerXML){return node.innerXML;}else{if(node.xml){return node.xml;}else{if(typeof XMLSerializer!="undefined"){return (new XMLSerializer()).serializeToString(node);}}}};dojo.dom.createDocument=function(){var doc=null;var _23d=dojo.doc();if(!dj_undef("ActiveXObject")){var _23e=["MSXML2","Microsoft","MSXML","MSXML3"];for(var i=0;i<_23e.length;i++){try{doc=new ActiveXObject(_23e[i]+".XMLDOM");}
catch(e){}
if(doc){break;}}}else{if((_23d.implementation)&&(_23d.implementation.createDocument)){doc=_23d.implementation.createDocument("","",null);}}
return doc;};dojo.dom.createDocumentFromText=function(str,_241){if(!_241){_241="text/xml";}
if(!dj_undef("DOMParser")){var _242=new DOMParser();return _242.parseFromString(str,_241);}else{if(!dj_undef("ActiveXObject")){var _243=dojo.dom.createDocument();if(_243){_243.async=false;_243.loadXML(str);return _243;}else{dojo.debug("toXml didn't work?");}}else{var _244=dojo.doc();if(_244.createElement){var tmp=_244.createElement("xml");tmp.innerHTML=str;if(_244.implementation&&_244.implementation.createDocument){var _246=_244.implementation.createDocument("foo","",null);for(var i=0;i<tmp.childNodes.length;i++){_246.importNode(tmp.childNodes.item(i),true);}
return _246;}
return ((tmp.document)&&(tmp.document.firstChild?tmp.document.firstChild:tmp));}}}
return null;};dojo.dom.prependChild=function(node,_249){if(_249.firstChild){_249.insertBefore(node,_249.firstChild);}else{_249.appendChild(node);}
return true;};dojo.dom.insertBefore=function(node,ref,_24c){if((_24c!=true)&&(node===ref||node.nextSibling===ref)){return false;}
var _24d=ref.parentNode;_24d.insertBefore(node,ref);return true;};dojo.dom.insertAfter=function(node,ref,_250){var pn=ref.parentNode;if(ref==pn.lastChild){if((_250!=true)&&(node===ref)){return false;}
pn.appendChild(node);}else{return this.insertBefore(node,ref.nextSibling,_250);}
return true;};dojo.dom.insertAtPosition=function(node,ref,_254){if((!node)||(!ref)||(!_254)){return false;}
switch(_254.toLowerCase()){case "before":
return dojo.dom.insertBefore(node,ref);case "after":
return dojo.dom.insertAfter(node,ref);case "first":
if(ref.firstChild){return dojo.dom.insertBefore(node,ref.firstChild);}else{ref.appendChild(node);return true;}
break;default:
ref.appendChild(node);return true;}};dojo.dom.insertAtIndex=function(node,_256,_257){var _258=_256.childNodes;if(!_258.length){_256.appendChild(node);return true;}
var _259=null;for(var i=0;i<_258.length;i++){var _25b=_258.item(i)["getAttribute"]?parseInt(_258.item(i).getAttribute("dojoinsertionindex")):-1;if(_25b<_257){_259=_258.item(i);}}
if(_259){return dojo.dom.insertAfter(node,_259);}else{return dojo.dom.insertBefore(node,_258.item(0));}};dojo.dom.textContent=function(node,text){if(arguments.length>1){var _25e=dojo.doc();dojo.dom.replaceChildren(node,_25e.createTextNode(text));return text;}else{if(node.textContent!=undefined){return node.textContent;}
var _25f="";if(node==null){return _25f;}
for(var i=0;i<node.childNodes.length;i++){switch(node.childNodes[i].nodeType){case 1:
case 5:
_25f+=dojo.dom.textContent(node.childNodes[i]);break;case 3:
case 2:
case 4:
_25f+=node.childNodes[i].nodeValue;break;default:
break;}}
return _25f;}};dojo.dom.hasParent=function(node){return Boolean(node&&node.parentNode&&dojo.dom.isNode(node.parentNode));};dojo.dom.isTag=function(node){if(node&&node.tagName){for(var i=1;i<arguments.length;i++){if(node.tagName==String(arguments[i])){return String(arguments[i]);}}}
return "";};dojo.dom.setAttributeNS=function(elem,_265,_266,_267){if(elem==null||((elem==undefined)&&(typeof elem=="undefined"))){dojo.raise("No element given to dojo.dom.setAttributeNS");}
if(!((elem.setAttributeNS==undefined)&&(typeof elem.setAttributeNS=="undefined"))){elem.setAttributeNS(_265,_266,_267);}else{var _268=elem.ownerDocument;var _269=_268.createNode(2,_266,_265);_269.nodeValue=_267;elem.setAttributeNode(_269);}};dojo.provide("dojo.string.common");dojo.string.trim=function(str,wh){if(!str.replace){return str;}
if(!str.length){return str;}
var re=(wh>0)?(/^\s+/):(wh<0)?(/\s+$/):(/^\s+|\s+$/g);return str.replace(re,"");};dojo.string.trimStart=function(str){return dojo.string.trim(str,1);};dojo.string.trimEnd=function(str){return dojo.string.trim(str,-1);};dojo.string.repeat=function(str,_270,_271){var out="";for(var i=0;i<_270;i++){out+=str;if(_271&&i<_270-1){out+=_271;}}
return out;};dojo.string.pad=function(str,len,c,dir){var out=String(str);if(!c){c="0";}
if(!dir){dir=1;}
while(out.length<len){if(dir>0){out=c+out;}else{out+=c;}}
return out;};dojo.string.padLeft=function(str,len,c){return dojo.string.pad(str,len,c,1);};dojo.string.padRight=function(str,len,c){return dojo.string.pad(str,len,c,-1);};dojo.provide("dojo.string");dojo.provide("dojo.io.common");dojo.io.transports=[];dojo.io.hdlrFuncNames=["load","error","timeout"];dojo.io.Request=function(url,_280,_281,_282){if((arguments.length==1)&&(arguments[0].constructor==Object)){this.fromKwArgs(arguments[0]);}else{this.url=url;if(_280){this.mimetype=_280;}
if(_281){this.transport=_281;}
if(arguments.length>=4){this.changeUrl=_282;}}};dojo.lang.extend(dojo.io.Request,{url:"",mimetype:"text/plain",method:"GET",content:undefined,transport:undefined,changeUrl:undefined,formNode:undefined,sync:false,bindSuccess:false,useCache:false,preventCache:false,load:function(type,data,_285,_286){},error:function(type,_288,_289,_28a){},timeout:function(type,_28c,_28d,_28e){},handle:function(type,data,_291,_292){},timeoutSeconds:0,abort:function(){},fromKwArgs:function(_293){if(_293["url"]){_293.url=_293.url.toString();}
if(_293["formNode"]){_293.formNode=dojo.byId(_293.formNode);}
if(!_293["method"]&&_293["formNode"]&&_293["formNode"].method){_293.method=_293["formNode"].method;}
if(!_293["handle"]&&_293["handler"]){_293.handle=_293.handler;}
if(!_293["load"]&&_293["loaded"]){_293.load=_293.loaded;}
if(!_293["changeUrl"]&&_293["changeURL"]){_293.changeUrl=_293.changeURL;}
_293.encoding=dojo.lang.firstValued(_293["encoding"],djConfig["bindEncoding"],"");_293.sendTransport=dojo.lang.firstValued(_293["sendTransport"],djConfig["ioSendTransport"],false);var _294=dojo.lang.isFunction;for(var x=0;x<dojo.io.hdlrFuncNames.length;x++){var fn=dojo.io.hdlrFuncNames[x];if(_293[fn]&&_294(_293[fn])){continue;}
if(_293["handle"]&&_294(_293["handle"])){_293[fn]=_293.handle;}}
dojo.lang.mixin(this,_293);}});dojo.io.Error=function(msg,type,num){this.message=msg;this.type=type||"unknown";this.number=num||0;};dojo.io.transports.addTransport=function(name){this.push(name);this[name]=dojo.io[name];};dojo.io.bind=function(_29b){if(!(_29b instanceof dojo.io.Request)){try{_29b=new dojo.io.Request(_29b);}
catch(e){dojo.debug(e);}}
var _29c="";if(_29b["transport"]){_29c=_29b["transport"];if(!this[_29c]){dojo.io.sendBindError(_29b,"No dojo.io.bind() transport with name '"+_29b["transport"]+"'.");return _29b;}
if(!this[_29c].canHandle(_29b)){dojo.io.sendBindError(_29b,"dojo.io.bind() transport with name '"+_29b["transport"]+"' cannot handle this type of request.");return _29b;}}else{for(var x=0;x<dojo.io.transports.length;x++){var tmp=dojo.io.transports[x];if((this[tmp])&&(this[tmp].canHandle(_29b))){_29c=tmp;break;}}
if(_29c==""){dojo.io.sendBindError(_29b,"None of the loaded transports for dojo.io.bind()"+" can handle the request.");return _29b;}}
this[_29c].bind(_29b);_29b.bindSuccess=true;return _29b;};dojo.io.sendBindError=function(_29f,_2a0){if((typeof _29f.error=="function"||typeof _29f.handle=="function")&&(typeof setTimeout=="function"||typeof setTimeout=="object")){var _2a1=new dojo.io.Error(_2a0);setTimeout(function(){_29f[(typeof _29f.error=="function")?"error":"handle"]("error",_2a1,null,_29f);},50);}else{dojo.raise(_2a0);}};dojo.io.queueBind=function(_2a2){if(!(_2a2 instanceof dojo.io.Request)){try{_2a2=new dojo.io.Request(_2a2);}
catch(e){dojo.debug(e);}}
var _2a3=_2a2.load;_2a2.load=function(){dojo.io._queueBindInFlight=false;var ret=_2a3.apply(this,arguments);dojo.io._dispatchNextQueueBind();return ret;};var _2a5=_2a2.error;_2a2.error=function(){dojo.io._queueBindInFlight=false;var ret=_2a5.apply(this,arguments);dojo.io._dispatchNextQueueBind();return ret;};dojo.io._bindQueue.push(_2a2);dojo.io._dispatchNextQueueBind();return _2a2;};dojo.io._dispatchNextQueueBind=function(){if(!dojo.io._queueBindInFlight){dojo.io._queueBindInFlight=true;if(dojo.io._bindQueue.length>0){dojo.io.bind(dojo.io._bindQueue.shift());}else{dojo.io._queueBindInFlight=false;}}};dojo.io._bindQueue=[];dojo.io._queueBindInFlight=false;dojo.io.argsFromMap=function(map,_2a8,last){var enc=/utf/i.test(_2a8||"")?encodeURIComponent:dojo.string.encodeAscii;var _2ab=[];var _2ac=new Object();for(var name in map){var _2ae=function(elt){var val=enc(name)+"="+enc(elt);_2ab[(last==name)?"push":"unshift"](val);};if(!_2ac[name]){var _2b1=map[name];if(dojo.lang.isArray(_2b1)){dojo.lang.forEach(_2b1,_2ae);}else{_2ae(_2b1);}}}
return _2ab.join("&");};dojo.io.setIFrameSrc=function(_2b2,src,_2b4){try{var r=dojo.render.html;if(!_2b4){if(r.safari){_2b2.location=src;}else{frames[_2b2.name].location=src;}}else{var idoc;if(r.ie){idoc=_2b2.contentWindow.document;}else{if(r.safari){idoc=_2b2.document;}else{idoc=_2b2.contentWindow;}}
if(!idoc){_2b2.location=src;return;}else{idoc.location.replace(src);}}}
catch(e){dojo.debug(e);dojo.debug("setIFrameSrc: "+e);}};dojo.provide("dojo.string.extras");dojo.string.substituteParams=function(_2b7,hash){var map=(typeof hash=="object")?hash:dojo.lang.toArray(arguments,1);return _2b7.replace(/\%\{(\w+)\}/g,function(_2ba,key){if(typeof (map[key])!="undefined"&&map[key]!=null){return map[key];}
dojo.raise("Substitution not found: "+key);});};dojo.string.capitalize=function(str){if(!dojo.lang.isString(str)){return "";}
if(arguments.length==0){str=this;}
var _2bd=str.split(" ");for(var i=0;i<_2bd.length;i++){_2bd[i]=_2bd[i].charAt(0).toUpperCase()+_2bd[i].substring(1);}
return _2bd.join(" ");};dojo.string.isBlank=function(str){if(!dojo.lang.isString(str)){return true;}
return (dojo.string.trim(str).length==0);};dojo.string.encodeAscii=function(str){if(!dojo.lang.isString(str)){return str;}
var ret="";var _2c2=escape(str);var _2c3,re=/%u([0-9A-F]{4})/i;while((_2c3=_2c2.match(re))){var num=Number("0x"+_2c3[1]);var _2c6=escape("&#"+num+";");ret+=_2c2.substring(0,_2c3.index)+_2c6;_2c2=_2c2.substring(_2c3.index+_2c3[0].length);}
ret+=_2c2.replace(/\+/g,"%2B");return ret;};dojo.string.escape=function(type,str){var args=dojo.lang.toArray(arguments,1);switch(type.toLowerCase()){case "xml":
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
return str;}};dojo.string.escapeXml=function(str,_2cb){str=str.replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");if(!_2cb){str=str.replace(/'/gm,"&#39;");}
return str;};dojo.string.escapeSql=function(str){return str.replace(/'/gm,"''");};dojo.string.escapeRegExp=function(str){return str.replace(/\\/gm,"\\\\").replace(/([\f\b\n\t\r[\^$|?*+(){}])/gm,"\\$1");};dojo.string.escapeJavaScript=function(str){return str.replace(/(["'\f\b\n\t\r])/gm,"\\$1");};dojo.string.escapeString=function(str){return ("\""+str.replace(/(["\\])/g,"\\$1")+"\"").replace(/[\f]/g,"\\f").replace(/[\b]/g,"\\b").replace(/[\n]/g,"\\n").replace(/[\t]/g,"\\t").replace(/[\r]/g,"\\r");};dojo.string.summary=function(str,len){if(!len||str.length<=len){return str;}
return str.substring(0,len).replace(/\.+$/,"")+"...";};dojo.string.endsWith=function(str,end,_2d4){if(_2d4){str=str.toLowerCase();end=end.toLowerCase();}
if((str.length-end.length)<0){return false;}
return str.lastIndexOf(end)==str.length-end.length;};dojo.string.endsWithAny=function(str){for(var i=1;i<arguments.length;i++){if(dojo.string.endsWith(str,arguments[i])){return true;}}
return false;};dojo.string.startsWith=function(str,_2d8,_2d9){if(_2d9){str=str.toLowerCase();_2d8=_2d8.toLowerCase();}
return str.indexOf(_2d8)==0;};dojo.string.startsWithAny=function(str){for(var i=1;i<arguments.length;i++){if(dojo.string.startsWith(str,arguments[i])){return true;}}
return false;};dojo.string.has=function(str){for(var i=1;i<arguments.length;i++){if(str.indexOf(arguments[i])>-1){return true;}}
return false;};dojo.string.normalizeNewlines=function(text,_2df){if(_2df=="\n"){text=text.replace(/\r\n/g,"\n");text=text.replace(/\r/g,"\n");}else{if(_2df=="\r"){text=text.replace(/\r\n/g,"\r");text=text.replace(/\n/g,"\r");}else{text=text.replace(/([^\r])\n/g,"$1\r\n").replace(/\r([^\n])/g,"\r\n$1");}}
return text;};dojo.string.splitEscaped=function(str,_2e1){var _2e2=[];for(var i=0,_2e4=0;i<str.length;i++){if(str.charAt(i)=="\\"){i++;continue;}
if(str.charAt(i)==_2e1){_2e2.push(str.substring(_2e4,i));_2e4=i+1;}}
_2e2.push(str.substr(_2e4));return _2e2;};dojo.provide("dojo.undo.browser");try{if((!djConfig["preventBackButtonFix"])&&(!dojo.hostenv.post_load_)){document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"'></iframe>");}}
catch(e){}
if(dojo.render.html.opera){dojo.debug("Opera is not supported with dojo.undo.browser, so back/forward detection will not work.");}
dojo.undo.browser={initialHref:(!dj_undef("window"))?window.location.href:"",initialHash:(!dj_undef("window"))?window.location.hash:"",moveForward:false,historyStack:[],forwardStack:[],historyIframe:null,bookmarkAnchor:null,locationTimer:null,setInitialState:function(args){this.initialState=this._createState(this.initialHref,args,this.initialHash);},addToHistory:function(args){this.forwardStack=[];var hash=null;var url=null;if(!this.historyIframe){this.historyIframe=window.frames["djhistory"];}
if(!this.bookmarkAnchor){this.bookmarkAnchor=document.createElement("a");dojo.body().appendChild(this.bookmarkAnchor);this.bookmarkAnchor.style.display="none";}
if(args["changeUrl"]){hash="#"+((args["changeUrl"]!==true)?args["changeUrl"]:(new Date()).getTime());if(this.historyStack.length==0&&this.initialState.urlHash==hash){this.initialState=this._createState(url,args,hash);return;}else{if(this.historyStack.length>0&&this.historyStack[this.historyStack.length-1].urlHash==hash){this.historyStack[this.historyStack.length-1]=this._createState(url,args,hash);return;}}
this.changingUrl=true;setTimeout("window.location.href = '"+hash+"'; dojo.undo.browser.changingUrl = false;",1);this.bookmarkAnchor.href=hash;if(dojo.render.html.ie){url=this._loadIframeHistory();var _2e9=args["back"]||args["backButton"]||args["handle"];var tcb=function(_2eb){if(window.location.hash!=""){setTimeout("window.location.href = '"+hash+"';",1);}
_2e9.apply(this,[_2eb]);};if(args["back"]){args.back=tcb;}else{if(args["backButton"]){args.backButton=tcb;}else{if(args["handle"]){args.handle=tcb;}}}
var _2ec=args["forward"]||args["forwardButton"]||args["handle"];var tfw=function(_2ee){if(window.location.hash!=""){window.location.href=hash;}
if(_2ec){_2ec.apply(this,[_2ee]);}};if(args["forward"]){args.forward=tfw;}else{if(args["forwardButton"]){args.forwardButton=tfw;}else{if(args["handle"]){args.handle=tfw;}}}}else{if(dojo.render.html.moz){if(!this.locationTimer){this.locationTimer=setInterval("dojo.undo.browser.checkLocation();",200);}}}}else{url=this._loadIframeHistory();}
this.historyStack.push(this._createState(url,args,hash));},checkLocation:function(){if(!this.changingUrl){var hsl=this.historyStack.length;if((window.location.hash==this.initialHash||window.location.href==this.initialHref)&&(hsl==1)){this.handleBackButton();return;}
if(this.forwardStack.length>0){if(this.forwardStack[this.forwardStack.length-1].urlHash==window.location.hash){this.handleForwardButton();return;}}
if((hsl>=2)&&(this.historyStack[hsl-2])){if(this.historyStack[hsl-2].urlHash==window.location.hash){this.handleBackButton();return;}}}},iframeLoaded:function(evt,_2f1){if(!dojo.render.html.opera){var _2f2=this._getUrlQuery(_2f1.href);if(_2f2==null){if(this.historyStack.length==1){this.handleBackButton();}
return;}
if(this.moveForward){this.moveForward=false;return;}
if(this.historyStack.length>=2&&_2f2==this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){this.handleBackButton();}else{if(this.forwardStack.length>0&&_2f2==this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){this.handleForwardButton();}}}},handleBackButton:function(){var _2f3=this.historyStack.pop();if(!_2f3){return;}
var last=this.historyStack[this.historyStack.length-1];if(!last&&this.historyStack.length==0){last=this.initialState;}
if(last){if(last.kwArgs["back"]){last.kwArgs["back"]();}else{if(last.kwArgs["backButton"]){last.kwArgs["backButton"]();}else{if(last.kwArgs["handle"]){last.kwArgs.handle("back");}}}}
this.forwardStack.push(_2f3);},handleForwardButton:function(){var last=this.forwardStack.pop();if(!last){return;}
if(last.kwArgs["forward"]){last.kwArgs.forward();}else{if(last.kwArgs["forwardButton"]){last.kwArgs.forwardButton();}else{if(last.kwArgs["handle"]){last.kwArgs.handle("forward");}}}
this.historyStack.push(last);},_createState:function(url,args,hash){return {"url":url,"kwArgs":args,"urlHash":hash};},_getUrlQuery:function(url){var _2fa=url.split("?");if(_2fa.length<2){return null;}else{return _2fa[1];}},_loadIframeHistory:function(){var url=dojo.hostenv.getBaseScriptUri()+"iframe_history.html?"+(new Date()).getTime();this.moveForward=true;dojo.io.setIFrameSrc(this.historyIframe,url,false);return url;}};dojo.provide("dojo.io.BrowserIO");if(!dj_undef("window")){dojo.io.checkChildrenForFile=function(node){var _2fd=false;var _2fe=node.getElementsByTagName("input");dojo.lang.forEach(_2fe,function(_2ff){if(_2fd){return;}
if(_2ff.getAttribute("type")=="file"){_2fd=true;}});return _2fd;};dojo.io.formHasFile=function(_300){return dojo.io.checkChildrenForFile(_300);};dojo.io.updateNode=function(node,_302){node=dojo.byId(node);var args=_302;if(dojo.lang.isString(_302)){args={url:_302};}
args.mimetype="text/html";args.load=function(t,d,e){while(node.firstChild){if(dojo["event"]){try{dojo.event.browser.clean(node.firstChild);}
catch(e){}}
node.removeChild(node.firstChild);}
node.innerHTML=d;};dojo.io.bind(args);};dojo.io.formFilter=function(node){var type=(node.type||"").toLowerCase();return !node.disabled&&node.name&&!dojo.lang.inArray(["file","submit","image","reset","button"],type);};dojo.io.encodeForm=function(_309,_30a,_30b){if((!_309)||(!_309.tagName)||(!_309.tagName.toLowerCase()=="form")){dojo.raise("Attempted to encode a non-form element.");}
if(!_30b){_30b=dojo.io.formFilter;}
var enc=/utf/i.test(_30a||"")?encodeURIComponent:dojo.string.encodeAscii;var _30d=[];for(var i=0;i<_309.elements.length;i++){var elm=_309.elements[i];if(!elm||elm.tagName.toLowerCase()=="fieldset"||!_30b(elm)){continue;}
var name=enc(elm.name);var type=elm.type.toLowerCase();if(type=="select-multiple"){for(var j=0;j<elm.options.length;j++){if(elm.options[j].selected){_30d.push(name+"="+enc(elm.options[j].value));}}}else{if(dojo.lang.inArray(["radio","checkbox"],type)){if(elm.checked){_30d.push(name+"="+enc(elm.value));}}else{_30d.push(name+"="+enc(elm.value));}}}
var _313=_309.getElementsByTagName("input");for(var i=0;i<_313.length;i++){var _314=_313[i];if(_314.type.toLowerCase()=="image"&&_314.form==_309&&_30b(_314)){var name=enc(_314.name);_30d.push(name+"="+enc(_314.value));_30d.push(name+".x=0");_30d.push(name+".y=0");}}
return _30d.join("&")+"&";};dojo.io.FormBind=function(args){this.bindArgs={};if(args&&args.formNode){this.init(args);}else{if(args){this.init({formNode:args});}}};dojo.lang.extend(dojo.io.FormBind,{form:null,bindArgs:null,clickedButton:null,init:function(args){var form=dojo.byId(args.formNode);if(!form||!form.tagName||form.tagName.toLowerCase()!="form"){throw new Error("FormBind: Couldn't apply, invalid form");}else{if(this.form==form){return;}else{if(this.form){throw new Error("FormBind: Already applied to a form");}}}
dojo.lang.mixin(this.bindArgs,args);this.form=form;this.connect(form,"onsubmit","submit");for(var i=0;i<form.elements.length;i++){var node=form.elements[i];if(node&&node.type&&dojo.lang.inArray(["submit","button"],node.type.toLowerCase())){this.connect(node,"onclick","click");}}
var _31a=form.getElementsByTagName("input");for(var i=0;i<_31a.length;i++){var _31b=_31a[i];if(_31b.type.toLowerCase()=="image"&&_31b.form==form){this.connect(_31b,"onclick","click");}}},onSubmit:function(form){return true;},submit:function(e){e.preventDefault();if(this.onSubmit(this.form)){dojo.io.bind(dojo.lang.mixin(this.bindArgs,{formFilter:dojo.lang.hitch(this,"formFilter")}));}},click:function(e){var node=e.currentTarget;if(node.disabled){return;}
this.clickedButton=node;},formFilter:function(node){var type=(node.type||"").toLowerCase();var _322=false;if(node.disabled||!node.name){_322=false;}else{if(dojo.lang.inArray(["submit","button","image"],type)){if(!this.clickedButton){this.clickedButton=node;}
_322=node==this.clickedButton;}else{_322=!dojo.lang.inArray(["file","submit","reset","button"],type);}}
return _322;},connect:function(_323,_324,_325){if(dojo.evalObjPath("dojo.event.connect")){dojo.event.connect(_323,_324,this,_325);}else{var fcn=dojo.lang.hitch(this,_325);_323[_324]=function(e){if(!e){e=window.event;}
if(!e.currentTarget){e.currentTarget=e.srcElement;}
if(!e.preventDefault){e.preventDefault=function(){window.event.returnValue=false;};}
fcn(e);};}}});dojo.io.XMLHTTPTransport=new function(){var _328=this;var _329={};this.useCache=false;this.preventCache=false;function getCacheKey(url,_32b,_32c){return url+"|"+_32b+"|"+_32c.toLowerCase();}
function addToCache(url,_32e,_32f,http){_329[getCacheKey(url,_32e,_32f)]=http;}
function getFromCache(url,_332,_333){return _329[getCacheKey(url,_332,_333)];}
this.clearCache=function(){_329={};};function doLoad(_334,http,url,_337,_338){if(((http.status>=200)&&(http.status<300))||(http.status==304)||(location.protocol=="file:"&&(http.status==0||http.status==undefined))||(location.protocol=="chrome:"&&(http.status==0||http.status==undefined))){var ret;if(_334.method.toLowerCase()=="head"){var _33a=http.getAllResponseHeaders();ret={};ret.toString=function(){return _33a;};var _33b=_33a.split(/[\r\n]+/g);for(var i=0;i<_33b.length;i++){var pair=_33b[i].match(/^([^:]+)\s*:\s*(.+)$/i);if(pair){ret[pair[1]]=pair[2];}}}else{if(_334.mimetype=="text/javascript"){try{ret=dj_eval(http.responseText);}
catch(e){dojo.debug(e);dojo.debug(http.responseText);ret=null;}}else{if(_334.mimetype=="text/json"||_334.mimetype=="application/json"){try{ret=dj_eval("("+http.responseText+")");}
catch(e){dojo.debug(e);dojo.debug(http.responseText);ret=false;}}else{if((_334.mimetype=="application/xml")||(_334.mimetype=="text/xml")){ret=http.responseXML;if(!ret||typeof ret=="string"||!http.getResponseHeader("Content-Type")){ret=dojo.dom.createDocumentFromText(http.responseText);}}else{ret=http.responseText;}}}}
if(_338){addToCache(url,_337,_334.method,http);}
_334[(typeof _334.load=="function")?"load":"handle"]("load",ret,http,_334);}else{var _33e=new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);_334[(typeof _334.error=="function")?"error":"handle"]("error",_33e,http,_334);}}
function setHeaders(http,_340){if(_340["headers"]){for(var _341 in _340["headers"]){if(_341.toLowerCase()=="content-type"&&!_340["contentType"]){_340["contentType"]=_340["headers"][_341];}else{http.setRequestHeader(_341,_340["headers"][_341]);}}}}
this.inFlight=[];this.inFlightTimer=null;this.startWatchingInFlight=function(){if(!this.inFlightTimer){this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);}};this.watchInFlight=function(){var now=null;if(!dojo.hostenv._blockAsync&&!_328._blockAsync){for(var x=this.inFlight.length-1;x>=0;x--){try{var tif=this.inFlight[x];if(!tif||tif.http._aborted||!tif.http.readyState){this.inFlight.splice(x,1);continue;}
if(4==tif.http.readyState){this.inFlight.splice(x,1);doLoad(tif.req,tif.http,tif.url,tif.query,tif.useCache);}else{if(tif.startTime){if(!now){now=(new Date()).getTime();}
if(tif.startTime+(tif.req.timeoutSeconds*1000)<now){if(typeof tif.http.abort=="function"){tif.http.abort();}
this.inFlight.splice(x,1);tif.req[(typeof tif.req.timeout=="function")?"timeout":"handle"]("timeout",null,tif.http,tif.req);}}}}
catch(e){try{var _345=new dojo.io.Error("XMLHttpTransport.watchInFlight Error: "+e);tif.req[(typeof tif.req.error=="function")?"error":"handle"]("error",_345,tif.http,tif.req);}
catch(e2){dojo.debug("XMLHttpTransport error callback failed: "+e2);}}}}
clearTimeout(this.inFlightTimer);if(this.inFlight.length==0){this.inFlightTimer=null;return;}
this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);};var _346=dojo.hostenv.getXmlhttpObject()?true:false;this.canHandle=function(_347){return _346&&dojo.lang.inArray(["text/plain","text/html","application/xml","text/xml","text/javascript","text/json","application/json"],(_347["mimetype"].toLowerCase()||""))&&!(_347["formNode"]&&dojo.io.formHasFile(_347["formNode"]));};this.multipartBoundary="45309FFF-BD65-4d50-99C9-36986896A96F";this.bind=function(_348){if(!_348["url"]){if(!_348["formNode"]&&(_348["backButton"]||_348["back"]||_348["changeUrl"]||_348["watchForURL"])&&(!djConfig.preventBackButtonFix)){dojo.deprecated("Using dojo.io.XMLHTTPTransport.bind() to add to browser history without doing an IO request","Use dojo.undo.browser.addToHistory() instead.","0.4");dojo.undo.browser.addToHistory(_348);return true;}}
var url=_348.url;var _34a="";if(_348["formNode"]){var ta=_348.formNode.getAttribute("action");if((ta)&&(!_348["url"])){url=ta;}
var tp=_348.formNode.getAttribute("method");if((tp)&&(!_348["method"])){_348.method=tp;}
_34a+=dojo.io.encodeForm(_348.formNode,_348.encoding,_348["formFilter"]);}
if(url.indexOf("#")>-1){dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);url=url.split("#")[0];}
if(_348["file"]){_348.method="post";}
if(!_348["method"]){_348.method="get";}
if(_348.method.toLowerCase()=="get"){_348.multipart=false;}else{if(_348["file"]){_348.multipart=true;}else{if(!_348["multipart"]){_348.multipart=false;}}}
if(_348["backButton"]||_348["back"]||_348["changeUrl"]){dojo.undo.browser.addToHistory(_348);}
var _34d=_348["content"]||{};if(_348.sendTransport){_34d["dojo.transport"]="xmlhttp";}
do{if(_348.postContent){_34a=_348.postContent;break;}
if(_34d){_34a+=dojo.io.argsFromMap(_34d,_348.encoding);}
if(_348.method.toLowerCase()=="get"||!_348.multipart){break;}
var t=[];if(_34a.length){var q=_34a.split("&");for(var i=0;i<q.length;++i){if(q[i].length){var p=q[i].split("=");t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+p[0]+"\"","",p[1]);}}}
if(_348.file){if(dojo.lang.isArray(_348.file)){for(var i=0;i<_348.file.length;++i){var o=_348.file[i];t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);}}else{var o=_348.file;t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);}}
if(t.length){t.push("--"+this.multipartBoundary+"--","");_34a=t.join("\r\n");}}while(false);var _353=_348["sync"]?false:true;var _354=_348["preventCache"]||(this.preventCache==true&&_348["preventCache"]!=false);var _355=_348["useCache"]==true||(this.useCache==true&&_348["useCache"]!=false);if(!_354&&_355){var _356=getFromCache(url,_34a,_348.method);if(_356){doLoad(_348,_356,url,_34a,false);return;}}
var http=dojo.hostenv.getXmlhttpObject(_348);var _358=false;if(_353){var _359=this.inFlight.push({"req":_348,"http":http,"url":url,"query":_34a,"useCache":_355,"startTime":_348.timeoutSeconds?(new Date()).getTime():0});this.startWatchingInFlight();}else{_328._blockAsync=true;}
if(_348.method.toLowerCase()=="post"){if(!_348.user){http.open("POST",url,_353);}else{http.open("POST",url,_353,_348.user,_348.password);}
setHeaders(http,_348);http.setRequestHeader("Content-Type",_348.multipart?("multipart/form-data; boundary="+this.multipartBoundary):(_348.contentType||"application/x-www-form-urlencoded"));try{http.send(_34a);}
catch(e){if(typeof http.abort=="function"){http.abort();}
doLoad(_348,{status:404},url,_34a,_355);}}else{var _35a=url;if(_34a!=""){_35a+=(_35a.indexOf("?")>-1?"&":"?")+_34a;}
if(_354){_35a+=(dojo.string.endsWithAny(_35a,"?","&")?"":(_35a.indexOf("?")>-1?"&":"?"))+"dojo.preventCache="+new Date().valueOf();}
if(!_348.user){http.open(_348.method.toUpperCase(),_35a,_353);}else{http.open(_348.method.toUpperCase(),_35a,_353,_348.user,_348.password);}
setHeaders(http,_348);try{http.send(null);}
catch(e){if(typeof http.abort=="function"){http.abort();}
doLoad(_348,{status:404},url,_34a,_355);}}
if(!_353){doLoad(_348,http,url,_34a,_355);_328._blockAsync=false;}
_348.abort=function(){try{http._aborted=true;}
catch(e){}
return http.abort();};return;};dojo.io.transports.addTransport("XMLHTTPTransport");};}
dojo.provide("dojo.io.cookie");dojo.io.cookie.setCookie=function(name,_35c,days,path,_35f,_360){var _361=-1;if(typeof days=="number"&&days>=0){var d=new Date();d.setTime(d.getTime()+(days*24*60*60*1000));_361=d.toGMTString();}
_35c=escape(_35c);document.cookie=name+"="+_35c+";"+(_361!=-1?" expires="+_361+";":"")+(path?"path="+path:"")+(_35f?"; domain="+_35f:"")+(_360?"; secure":"");};dojo.io.cookie.set=dojo.io.cookie.setCookie;dojo.io.cookie.getCookie=function(name){var idx=document.cookie.lastIndexOf(name+"=");if(idx==-1){return null;}
var _365=document.cookie.substring(idx+name.length+1);var end=_365.indexOf(";");if(end==-1){end=_365.length;}
_365=_365.substring(0,end);_365=unescape(_365);return _365;};dojo.io.cookie.get=dojo.io.cookie.getCookie;dojo.io.cookie.deleteCookie=function(name){dojo.io.cookie.setCookie(name,"-",0);};dojo.io.cookie.setObjectCookie=function(name,obj,days,path,_36c,_36d,_36e){if(arguments.length==5){_36e=_36c;_36c=null;_36d=null;}
var _36f=[],_370,_371="";if(!_36e){_370=dojo.io.cookie.getObjectCookie(name);}
if(days>=0){if(!_370){_370={};}
for(var prop in obj){if(prop==null){delete _370[prop];}else{if(typeof obj[prop]=="string"||typeof obj[prop]=="number"){_370[prop]=obj[prop];}}}
prop=null;for(var prop in _370){_36f.push(escape(prop)+"="+escape(_370[prop]));}
_371=_36f.join("&");}
dojo.io.cookie.setCookie(name,_371,days,path,_36c,_36d);};dojo.io.cookie.getObjectCookie=function(name){var _374=null,_375=dojo.io.cookie.getCookie(name);if(_375){_374={};var _376=_375.split("&");for(var i=0;i<_376.length;i++){var pair=_376[i].split("=");var _379=pair[1];if(isNaN(_379)){_379=unescape(pair[1]);}
_374[unescape(pair[0])]=_379;}}
return _374;};dojo.io.cookie.isSupported=function(){if(typeof navigator.cookieEnabled!="boolean"){dojo.io.cookie.setCookie("__TestingYourBrowserForCookieSupport__","CookiesAllowed",90,null);var _37a=dojo.io.cookie.getCookie("__TestingYourBrowserForCookieSupport__");navigator.cookieEnabled=(_37a=="CookiesAllowed");if(navigator.cookieEnabled){this.deleteCookie("__TestingYourBrowserForCookieSupport__");}}
return navigator.cookieEnabled;};if(!dojo.io.cookies){dojo.io.cookies=dojo.io.cookie;}
dojo.provide("dojo.date.common");dojo.date.setDayOfYear=function(_37b,_37c){_37b.setMonth(0);_37b.setDate(_37c);return _37b;};dojo.date.getDayOfYear=function(_37d){var _37e=_37d.getFullYear();var _37f=new Date(_37e-1,11,31);return Math.floor((_37d.getTime()-_37f.getTime())/86400000);};dojo.date.setWeekOfYear=function(_380,week,_382){if(arguments.length==1){_382=0;}
dojo.unimplemented("dojo.date.setWeekOfYear");};dojo.date.getWeekOfYear=function(_383,_384){if(arguments.length==1){_384=0;}
var _385=new Date(_383.getFullYear(),0,1);var day=_385.getDay();_385.setDate(_385.getDate()-day+_384-(day>_384?7:0));return Math.floor((_383.getTime()-_385.getTime())/604800000);};dojo.date.setIsoWeekOfYear=function(_387,week,_389){if(arguments.length==1){_389=1;}
dojo.unimplemented("dojo.date.setIsoWeekOfYear");};dojo.date.getIsoWeekOfYear=function(_38a,_38b){if(arguments.length==1){_38b=1;}
dojo.unimplemented("dojo.date.getIsoWeekOfYear");};dojo.date.shortTimezones=["IDLW","BET","HST","MART","AKST","PST","MST","CST","EST","AST","NFT","BST","FST","AT","GMT","CET","EET","MSK","IRT","GST","AFT","AGTT","IST","NPT","ALMT","MMT","JT","AWST","JST","ACST","AEST","LHST","VUT","NFT","NZT","CHAST","PHOT","LINT"];dojo.date.timezoneOffsets=[-720,-660,-600,-570,-540,-480,-420,-360,-300,-240,-210,-180,-120,-60,0,60,120,180,210,240,270,300,330,345,360,390,420,480,540,570,600,630,660,690,720,765,780,840];dojo.date.getDaysInMonth=function(_38c){var _38d=_38c.getMonth();var days=[31,28,31,30,31,30,31,31,30,31,30,31];if(_38d==1&&dojo.date.isLeapYear(_38c)){return 29;}else{return days[_38d];}};dojo.date.isLeapYear=function(_38f){var year=_38f.getFullYear();return (year%400==0)?true:(year%100==0)?false:(year%4==0)?true:false;};dojo.date.getTimezoneName=function(_391){var str=_391.toString();var tz="";var _394;var pos=str.indexOf("(");if(pos>-1){pos++;tz=str.substring(pos,str.indexOf(")"));}else{var pat=/([A-Z\/]+) \d{4}$/;if((_394=str.match(pat))){tz=_394[1];}else{str=_391.toLocaleString();pat=/ ([A-Z\/]+)$/;if((_394=str.match(pat))){tz=_394[1];}}}
return tz=="AM"||tz=="PM"?"":tz;};dojo.date.getOrdinal=function(_397){var date=_397.getDate();if(date%100!=11&&date%10==1){return "st";}else{if(date%100!=12&&date%10==2){return "nd";}else{if(date%100!=13&&date%10==3){return "rd";}else{return "th";}}}};dojo.date.compareTypes={DATE:1,TIME:2};dojo.date.compare=function(_399,_39a,_39b){var dA=_399;var dB=_39a||new Date();var now=new Date();with(dojo.date.compareTypes){var opt=_39b||(DATE|TIME);var d1=new Date((opt&DATE)?dA.getFullYear():now.getFullYear(),(opt&DATE)?dA.getMonth():now.getMonth(),(opt&DATE)?dA.getDate():now.getDate(),(opt&TIME)?dA.getHours():0,(opt&TIME)?dA.getMinutes():0,(opt&TIME)?dA.getSeconds():0);var d2=new Date((opt&DATE)?dB.getFullYear():now.getFullYear(),(opt&DATE)?dB.getMonth():now.getMonth(),(opt&DATE)?dB.getDate():now.getDate(),(opt&TIME)?dB.getHours():0,(opt&TIME)?dB.getMinutes():0,(opt&TIME)?dB.getSeconds():0);}
if(d1.valueOf()>d2.valueOf()){return 1;}
if(d1.valueOf()<d2.valueOf()){return -1;}
return 0;};dojo.date.dateParts={YEAR:0,MONTH:1,DAY:2,HOUR:3,MINUTE:4,SECOND:5,MILLISECOND:6,QUARTER:7,WEEK:8,WEEKDAY:9};dojo.date.add=function(dt,_3a3,incr){if(typeof dt=="number"){dt=new Date(dt);}
function fixOvershoot(){if(sum.getDate()<dt.getDate()){sum.setDate(0);}}
var sum=new Date(dt);with(dojo.date.dateParts){switch(_3a3){case YEAR:
sum.setFullYear(dt.getFullYear()+incr);fixOvershoot();break;case QUARTER:
incr*=3;case MONTH:
sum.setMonth(dt.getMonth()+incr);fixOvershoot();break;case WEEK:
incr*=7;case DAY:
sum.setDate(dt.getDate()+incr);break;case WEEKDAY:
var dat=dt.getDate();var _3a7=0;var days=0;var strt=0;var trgt=0;var adj=0;var mod=incr%5;if(mod==0){days=(incr>0)?5:-5;_3a7=(incr>0)?((incr-5)/5):((incr+5)/5);}else{days=mod;_3a7=parseInt(incr/5);}
strt=dt.getDay();if(strt==6&&incr>0){adj=1;}else{if(strt==0&&incr<0){adj=-1;}}
trgt=(strt+days);if(trgt==0||trgt==6){adj=(incr>0)?2:-2;}
sum.setDate(dat+(7*_3a7)+days+adj);break;case HOUR:
sum.setHours(sum.getHours()+incr);break;case MINUTE:
sum.setMinutes(sum.getMinutes()+incr);break;case SECOND:
sum.setSeconds(sum.getSeconds()+incr);break;case MILLISECOND:
sum.setMilliseconds(sum.getMilliseconds()+incr);break;default:
break;}}
return sum;};dojo.date.diff=function(dtA,dtB,_3af){if(typeof dtA=="number"){dtA=new Date(dtA);}
if(typeof dtB=="number"){dtB=new Date(dtB);}
var _3b0=dtB.getFullYear()-dtA.getFullYear();var _3b1=(dtB.getMonth()-dtA.getMonth())+(_3b0*12);var _3b2=dtB.getTime()-dtA.getTime();var _3b3=_3b2/1000;var _3b4=_3b3/60;var _3b5=_3b4/60;var _3b6=_3b5/24;var _3b7=_3b6/7;var _3b8=0;with(dojo.date.dateParts){switch(_3af){case YEAR:
_3b8=_3b0;break;case QUARTER:
var mA=dtA.getMonth();var mB=dtB.getMonth();var qA=Math.floor(mA/3)+1;var qB=Math.floor(mB/3)+1;qB+=(_3b0*4);_3b8=qB-qA;break;case MONTH:
_3b8=_3b1;break;case WEEK:
_3b8=parseInt(_3b7);break;case DAY:
_3b8=_3b6;break;case WEEKDAY:
var days=Math.round(_3b6);var _3be=parseInt(days/7);var mod=days%7;if(mod==0){days=_3be*5;}else{var adj=0;var aDay=dtA.getDay();var bDay=dtB.getDay();_3be=parseInt(days/7);mod=days%7;var _3c3=new Date(dtA);_3c3.setDate(_3c3.getDate()+(_3be*7));var _3c4=_3c3.getDay();if(_3b6>0){switch(true){case aDay==6:
adj=-1;break;case aDay==0:
adj=0;break;case bDay==6:
adj=-1;break;case bDay==0:
adj=-2;break;case (_3c4+mod)>5:
adj=-2;break;default:
break;}}else{if(_3b6<0){switch(true){case aDay==6:
adj=0;break;case aDay==0:
adj=1;break;case bDay==6:
adj=2;break;case bDay==0:
adj=1;break;case (_3c4+mod)<0:
adj=2;break;default:
break;}}}
days+=adj;days-=(_3be*2);}
_3b8=days;break;case HOUR:
_3b8=_3b5;break;case MINUTE:
_3b8=_3b4;break;case SECOND:
_3b8=_3b3;break;case MILLISECOND:
_3b8=_3b2;break;default:
break;}}
return Math.round(_3b8);};dojo.provide("dojo.date.supplemental");dojo.date.getFirstDayOfWeek=function(_3c5){var _3c6={mv:5,ae:6,af:6,bh:6,dj:6,dz:6,eg:6,er:6,et:6,iq:6,ir:6,jo:6,ke:6,kw:6,lb:6,ly:6,ma:6,om:6,qa:6,sa:6,sd:6,so:6,tn:6,ye:6,as:0,au:0,az:0,bw:0,ca:0,cn:0,fo:0,ge:0,gl:0,gu:0,hk:0,ie:0,il:0,is:0,jm:0,jp:0,kg:0,kr:0,la:0,mh:0,mo:0,mp:0,mt:0,nz:0,ph:0,pk:0,sg:0,th:0,tt:0,tw:0,um:0,us:0,uz:0,vi:0,za:0,zw:0,et:0,mw:0,ng:0,tj:0,gb:0,sy:4};_3c5=dojo.hostenv.normalizeLocale(_3c5);var _3c7=_3c5.split("-")[1];var dow=_3c6[_3c7];return (typeof dow=="undefined")?1:dow;};dojo.date.getWeekend=function(_3c9){var _3ca={eg:5,il:5,sy:5,"in":0,ae:4,bh:4,dz:4,iq:4,jo:4,kw:4,lb:4,ly:4,ma:4,om:4,qa:4,sa:4,sd:4,tn:4,ye:4};var _3cb={ae:5,bh:5,dz:5,iq:5,jo:5,kw:5,lb:5,ly:5,ma:5,om:5,qa:5,sa:5,sd:5,tn:5,ye:5,af:5,ir:5,eg:6,il:6,sy:6};_3c9=dojo.hostenv.normalizeLocale(_3c9);var _3cc=_3c9.split("-")[1];var _3cd=_3ca[_3cc];var end=_3cb[_3cc];if(typeof _3cd=="undefined"){_3cd=6;}
if(typeof end=="undefined"){end=0;}
return {start:_3cd,end:end};};dojo.date.isWeekend=function(_3cf,_3d0){var _3d1=dojo.date.getWeekend(_3d0);var day=(_3cf||new Date()).getDay();if(_3d1.end<_3d1.start){_3d1.end+=7;if(day<_3d1.start){day+=7;}}
return day>=_3d1.start&&day<=_3d1.end;};dojo.provide("dojo.i18n.common");dojo.i18n.getLocalization=function(_3d3,_3d4,_3d5){dojo.hostenv.preloadLocalizations();_3d5=dojo.hostenv.normalizeLocale(_3d5);var _3d6=_3d5.split("-");var _3d7=[_3d3,"nls",_3d4].join(".");var _3d8=dojo.hostenv.findModule(_3d7,true);var _3d9;for(var i=_3d6.length;i>0;i--){var loc=_3d6.slice(0,i).join("_");if(_3d8[loc]){_3d9=_3d8[loc];break;}}
if(!_3d9){_3d9=_3d8.ROOT;}
if(_3d9){var _3dc=function(){};_3dc.prototype=_3d9;return new _3dc();}
dojo.raise("Bundle not found: "+_3d4+" in "+_3d3+" , locale="+_3d5);};dojo.i18n.isLTR=function(_3dd){var lang=dojo.hostenv.normalizeLocale(_3dd).split("-")[0];var RTL={ar:true,fa:true,he:true,ur:true,yi:true};return !RTL[lang];};dojo.provide("dojo.date.format");(function(){dojo.date.format=function(_3e0,_3e1){if(typeof _3e1=="string"){dojo.deprecated("dojo.date.format","To format dates with POSIX-style strings, please use dojo.date.strftime instead","0.5");return dojo.date.strftime(_3e0,_3e1);}
function formatPattern(_3e2,_3e3){return _3e3.replace(/([a-z])\1*/ig,function(_3e4){var s;var c=_3e4.charAt(0);var l=_3e4.length;var pad;var _3e9=["abbr","wide","narrow"];switch(c){case "G":
if(l>3){dojo.unimplemented("Era format not implemented");}
s=info.eras[_3e2.getFullYear()<0?1:0];break;case "y":
s=_3e2.getFullYear();switch(l){case 1:
break;case 2:
s=String(s).substr(-2);break;default:
pad=true;}
break;case "Q":
case "q":
s=Math.ceil((_3e2.getMonth()+1)/3);switch(l){case 1:
case 2:
pad=true;break;case 3:
case 4:
dojo.unimplemented("Quarter format not implemented");}
break;case "M":
case "L":
var m=_3e2.getMonth();var _3ec;switch(l){case 1:
case 2:
s=m+1;pad=true;break;case 3:
case 4:
case 5:
_3ec=_3e9[l-3];break;}
if(_3ec){var type=(c=="L")?"standalone":"format";var prop=["months",type,_3ec].join("-");s=info[prop][m];}
break;case "w":
var _3ef=0;s=dojo.date.getWeekOfYear(_3e2,_3ef);pad=true;break;case "d":
s=_3e2.getDate();pad=true;break;case "D":
s=dojo.date.getDayOfYear(_3e2);pad=true;break;case "E":
case "e":
case "c":
var d=_3e2.getDay();var _3ec;switch(l){case 1:
case 2:
if(c=="e"){var _3f1=dojo.date.getFirstDayOfWeek(_3e1.locale);d=(d-_3f1+7)%7;}
if(c!="c"){s=d+1;pad=true;break;}
case 3:
case 4:
case 5:
_3ec=_3e9[l-3];break;}
if(_3ec){var type=(c=="c")?"standalone":"format";var prop=["days",type,_3ec].join("-");s=info[prop][d];}
break;case "a":
var _3f2=(_3e2.getHours()<12)?"am":"pm";s=info[_3f2];break;case "h":
case "H":
case "K":
case "k":
var h=_3e2.getHours();switch(c){case "h":
s=(h%12)||12;break;case "H":
s=h;break;case "K":
s=(h%12);break;case "k":
s=h||24;break;}
pad=true;break;case "m":
s=_3e2.getMinutes();pad=true;break;case "s":
s=_3e2.getSeconds();pad=true;break;case "S":
s=Math.round(_3e2.getMilliseconds()*Math.pow(10,l-3));break;case "v":
case "z":
s=dojo.date.getTimezoneName(_3e2);if(s){break;}
l=4;case "Z":
var _3f4=_3e2.getTimezoneOffset();var tz=[(_3f4<=0?"+":"-"),dojo.string.pad(Math.floor(Math.abs(_3f4)/60),2),dojo.string.pad(Math.abs(_3f4)%60,2)];if(l==4){tz.splice(0,0,"GMT");tz.splice(3,0,":");}
s=tz.join("");break;case "Y":
case "u":
case "W":
case "F":
case "g":
case "A":
dojo.debug(_3e4+" modifier not yet implemented");s="?";break;default:
dojo.raise("dojo.date.format: invalid pattern char: "+_3e3);}
if(pad){s=dojo.string.pad(s,l);}
return s;});}
_3e1=_3e1||{};var _3f6=dojo.hostenv.normalizeLocale(_3e1.locale);var _3f7=_3e1.formatLength||"full";var info=dojo.date._getGregorianBundle(_3f6);var str=[];var _3f9=dojo.lang.curry(this,formatPattern,_3e0);if(_3e1.selector!="timeOnly"){var _3fa=_3e1.datePattern||info["dateFormat-"+_3f7];if(_3fa){str.push(_processPattern(_3fa,_3f9));}}
if(_3e1.selector!="dateOnly"){var _3fb=_3e1.timePattern||info["timeFormat-"+_3f7];if(_3fb){str.push(_processPattern(_3fb,_3f9));}}
var _3fc=str.join(" ");return _3fc;};dojo.date.parse=function(_3fd,_3fe){_3fe=_3fe||{};var _3ff=dojo.hostenv.normalizeLocale(_3fe.locale);var info=dojo.date._getGregorianBundle(_3ff);var _401=_3fe.formatLength||"full";if(!_3fe.selector){_3fe.selector="dateOnly";}
var _402=_3fe.datePattern||info["dateFormat-"+_401];var _403=_3fe.timePattern||info["timeFormat-"+_401];var _404;if(_3fe.selector=="dateOnly"){_404=_402;}else{if(_3fe.selector=="timeOnly"){_404=_403;}else{if(_3fe.selector=="dateTime"){_404=_402+" "+_403;}else{var msg="dojo.date.parse: Unknown selector param passed: '"+_3fe.selector+"'.";msg+=" Defaulting to date pattern.";dojo.debug(msg);_404=_402;}}}
var _406=[];var _407=_processPattern(_404,dojo.lang.curry(this,_buildDateTimeRE,_406,info,_3fe));var _408=new RegExp("^"+_407+"$");var _409=_408.exec(_3fd);if(!_409){return null;}
var _40a=["abbr","wide","narrow"];var _40b=new Date(1972,0);var _40c={};for(var i=1;i<_409.length;i++){var grp=_406[i-1];var l=grp.length;var v=_409[i];switch(grp.charAt(0)){case "y":
if(l!=2){_40b.setFullYear(v);_40c.year=v;}else{if(v<100){v=Number(v);var year=""+new Date().getFullYear();var _412=year.substring(0,2)*100;var _413=Number(year.substring(2,4));var _414=Math.min(_413+20,99);var num=(v<_414)?_412+v:_412-100+v;_40b.setFullYear(num);_40c.year=num;}else{if(_3fe.strict){return null;}
_40b.setFullYear(v);_40c.year=v;}}
break;case "M":
if(l>2){if(!_3fe.strict){v=v.replace(/\./g,"");v=v.toLowerCase();}
var _416=info["months-format-"+_40a[l-3]].concat();for(var j=0;j<_416.length;j++){if(!_3fe.strict){_416[j]=_416[j].toLowerCase();}
if(v==_416[j]){_40b.setMonth(j);_40c.month=j;break;}}
if(j==_416.length){dojo.debug("dojo.date.parse: Could not parse month name: '"+v+"'.");return null;}}else{_40b.setMonth(v-1);_40c.month=v-1;}
break;case "E":
case "e":
if(!_3fe.strict){v=v.toLowerCase();}
var days=info["days-format-"+_40a[l-3]].concat();for(var j=0;j<days.length;j++){if(!_3fe.strict){days[j]=days[j].toLowerCase();}
if(v==days[j]){break;}}
if(j==days.length){dojo.debug("dojo.date.parse: Could not parse weekday name: '"+v+"'.");return null;}
break;case "d":
_40b.setDate(v);_40c.date=v;break;case "a":
var am=_3fe.am||info.am;var pm=_3fe.pm||info.pm;if(!_3fe.strict){v=v.replace(/\./g,"").toLowerCase();am=am.replace(/\./g,"").toLowerCase();pm=pm.replace(/\./g,"").toLowerCase();}
if(_3fe.strict&&v!=am&&v!=pm){dojo.debug("dojo.date.parse: Could not parse am/pm part.");return null;}
var _41b=_40b.getHours();if(v==pm&&_41b<12){_40b.setHours(_41b+12);}else{if(v==am&&_41b==12){_40b.setHours(0);}}
break;case "K":
if(v==24){v=0;}
case "h":
case "H":
case "k":
if(v>23){dojo.debug("dojo.date.parse: Illegal hours value");return null;}
_40b.setHours(v);break;case "m":
_40b.setMinutes(v);break;case "s":
_40b.setSeconds(v);break;case "S":
_40b.setMilliseconds(v);break;default:
dojo.unimplemented("dojo.date.parse: unsupported pattern char="+grp.charAt(0));}}
if(_40c.year&&_40b.getFullYear()!=_40c.year){dojo.debug("Parsed year: '"+_40b.getFullYear()+"' did not match input year: '"+_40c.year+"'.");return null;}
if(_40c.month&&_40b.getMonth()!=_40c.month){dojo.debug("Parsed month: '"+_40b.getMonth()+"' did not match input month: '"+_40c.month+"'.");return null;}
if(_40c.date&&_40b.getDate()!=_40c.date){dojo.debug("Parsed day of month: '"+_40b.getDate()+"' did not match input day of month: '"+_40c.date+"'.");return null;}
return _40b;};function _processPattern(_41c,_41d,_41e,_41f){var _420=function(x){return x;};_41d=_41d||_420;_41e=_41e||_420;_41f=_41f||_420;var _422=_41c.match(/(''|[^'])+/g);var _423=false;for(var i=0;i<_422.length;i++){if(!_422[i]){_422[i]="";}else{_422[i]=(_423?_41e:_41d)(_422[i]);_423=!_423;}}
return _41f(_422.join(""));}
function _buildDateTimeRE(_425,info,_427,_428){return _428.replace(/([a-z])\1*/ig,function(_429){var s;var c=_429.charAt(0);var l=_429.length;switch(c){case "y":
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
var am=_427.am||info.am||"AM";var pm=_427.pm||info.pm||"PM";if(_427.strict){s=am+"|"+pm;}else{s=am;s+=(am!=am.toLowerCase())?"|"+am.toLowerCase():"";s+="|";s+=(pm!=pm.toLowerCase())?pm+"|"+pm.toLowerCase():pm;}
break;default:
dojo.unimplemented("parse of date format, pattern="+_428);}
if(_425){_425.push(_429);}
return "\\s*("+s+")\\s*";});}})();dojo.date.strftime=function(_42f,_430,_431){var _432=null;function _(s,n){return dojo.string.pad(s,n||2,_432||"0");}
var info=dojo.date._getGregorianBundle(_431);function $(_436){switch(_436){case "a":
return dojo.date.getDayShortName(_42f,_431);case "A":
return dojo.date.getDayName(_42f,_431);case "b":
case "h":
return dojo.date.getMonthShortName(_42f,_431);case "B":
return dojo.date.getMonthName(_42f,_431);case "c":
return dojo.date.format(_42f,{locale:_431});case "C":
return _(Math.floor(_42f.getFullYear()/100));case "d":
return _(_42f.getDate());case "D":
return $("m")+"/"+$("d")+"/"+$("y");case "e":
if(_432==null){_432=" ";}
return _(_42f.getDate());case "f":
if(_432==null){_432=" ";}
return _(_42f.getMonth()+1);case "g":
break;case "G":
dojo.unimplemented("unimplemented modifier 'G'");break;case "F":
return $("Y")+"-"+$("m")+"-"+$("d");case "H":
return _(_42f.getHours());case "I":
return _(_42f.getHours()%12||12);case "j":
return _(dojo.date.getDayOfYear(_42f),3);case "k":
if(_432==null){_432=" ";}
return _(_42f.getHours());case "l":
if(_432==null){_432=" ";}
return _(_42f.getHours()%12||12);case "m":
return _(_42f.getMonth()+1);case "M":
return _(_42f.getMinutes());case "n":
return "\n";case "p":
return info[_42f.getHours()<12?"am":"pm"];case "r":
return $("I")+":"+$("M")+":"+$("S")+" "+$("p");case "R":
return $("H")+":"+$("M");case "S":
return _(_42f.getSeconds());case "t":
return "\t";case "T":
return $("H")+":"+$("M")+":"+$("S");case "u":
return String(_42f.getDay()||7);case "U":
return _(dojo.date.getWeekOfYear(_42f));case "V":
return _(dojo.date.getIsoWeekOfYear(_42f));case "W":
return _(dojo.date.getWeekOfYear(_42f,1));case "w":
return String(_42f.getDay());case "x":
return dojo.date.format(_42f,{selector:"dateOnly",locale:_431});case "X":
return dojo.date.format(_42f,{selector:"timeOnly",locale:_431});case "y":
return _(_42f.getFullYear()%100);case "Y":
return String(_42f.getFullYear());case "z":
var _437=_42f.getTimezoneOffset();return (_437>0?"-":"+")+_(Math.floor(Math.abs(_437)/60))+":"+_(Math.abs(_437)%60);case "Z":
return dojo.date.getTimezoneName(_42f);case "%":
return "%";}}
var _438="";var i=0;var _43a=0;var _43b=null;while((_43a=_430.indexOf("%",i))!=-1){_438+=_430.substring(i,_43a++);switch(_430.charAt(_43a++)){case "_":
_432=" ";break;case "-":
_432="";break;case "0":
_432="0";break;case "^":
_43b="upper";break;case "*":
_43b="lower";break;case "#":
_43b="swap";break;default:
_432=null;_43a--;break;}
var _43c=$(_430.charAt(_43a++));switch(_43b){case "upper":
_43c=_43c.toUpperCase();break;case "lower":
_43c=_43c.toLowerCase();break;case "swap":
var _43d=_43c.toLowerCase();var _43e="";var j=0;var ch="";while(j<_43c.length){ch=_43c.charAt(j);_43e+=(ch==_43d.charAt(j))?ch.toUpperCase():ch.toLowerCase();j++;}
_43c=_43e;break;default:
break;}
_43b=null;_438+=_43c;i=_43a;}
_438+=_430.substring(i);return _438;};(function(){var _441=[];dojo.date.addCustomFormats=function(_442,_443){_441.push({pkg:_442,name:_443});};dojo.date._getGregorianBundle=function(_444){var _445={};dojo.lang.forEach(_441,function(desc){var _447=dojo.i18n.getLocalization(desc.pkg,desc.name,_444);_445=dojo.lang.mixin(_445,_447);},this);return _445;};})();dojo.date.addCustomFormats("dojo.i18n.calendar","gregorian");dojo.date.addCustomFormats("dojo.i18n.calendar","gregorianExtras");dojo.date.getNames=function(item,type,use,_44b){var _44c;var _44d=dojo.date._getGregorianBundle(_44b);var _44e=[item,use,type];if(use=="standAlone"){_44c=_44d[_44e.join("-")];}
_44e[1]="format";return (_44c||_44d[_44e.join("-")]).concat();};dojo.date.getDayName=function(_44f,_450){return dojo.date.getNames("days","wide","format",_450)[_44f.getDay()];};dojo.date.getDayShortName=function(_451,_452){return dojo.date.getNames("days","abbr","format",_452)[_451.getDay()];};dojo.date.getMonthName=function(_453,_454){return dojo.date.getNames("months","wide","format",_454)[_453.getMonth()];};dojo.date.getMonthShortName=function(_455,_456){return dojo.date.getNames("months","abbr","format",_456)[_455.getMonth()];};dojo.date.toRelativeString=function(_457){var now=new Date();var diff=(now-_457)/1000;var end=" ago";var _45b=false;if(diff<0){_45b=true;end=" from now";diff=-diff;}
if(diff<60){diff=Math.round(diff);return diff+" second"+(diff==1?"":"s")+end;}
if(diff<60*60){diff=Math.round(diff/60);return diff+" minute"+(diff==1?"":"s")+end;}
if(diff<60*60*24){diff=Math.round(diff/3600);return diff+" hour"+(diff==1?"":"s")+end;}
if(diff<60*60*24*7){diff=Math.round(diff/(3600*24));if(diff==1){return _45b?"Tomorrow":"Yesterday";}else{return diff+" days"+end;}}
return dojo.date.format(_457);};dojo.date.toSql=function(_45c,_45d){return dojo.date.strftime(_45c,"%F"+!_45d?" %T":"");};dojo.date.fromSql=function(_45e){var _45f=_45e.split(/[\- :]/g);while(_45f.length<6){_45f.push(0);}
return new Date(_45f[0],(parseInt(_45f[1],10)-1),_45f[2],_45f[3],_45f[4],_45f[5]);};dojo.provide("dojo.xml.Parse");dojo.xml.Parse=function(){var isIE=((dojo.render.html.capable)&&(dojo.render.html.ie));function getTagName(node){try{return node.tagName.toLowerCase();}
catch(e){return "";}}
function getDojoTagName(node){var _463=getTagName(node);if(!_463){return "";}
if((dojo.widget)&&(dojo.widget.tags[_463])){return _463;}
var p=_463.indexOf(":");if(p>=0){return _463;}
if(_463.substr(0,5)=="dojo:"){return _463;}
if(dojo.render.html.capable&&dojo.render.html.ie&&node.scopeName!="HTML"){return node.scopeName.toLowerCase()+":"+_463;}
if(_463.substr(0,4)=="dojo"){return "dojo:"+_463.substring(4);}
var djt=node.getAttribute("dojoType")||node.getAttribute("dojotype");if(djt){if(djt.indexOf(":")<0){djt="dojo:"+djt;}
return djt.toLowerCase();}
djt=node.getAttributeNS&&node.getAttributeNS(dojo.dom.dojoml,"type");if(djt){return "dojo:"+djt.toLowerCase();}
try{djt=node.getAttribute("dojo:type");}
catch(e){}
if(djt){return "dojo:"+djt.toLowerCase();}
if((dj_global["djConfig"])&&(!djConfig["ignoreClassNames"])){var _466=node.className||node.getAttribute("class");if((_466)&&(_466.indexOf)&&(_466.indexOf("dojo-")!=-1)){var _467=_466.split(" ");for(var x=0,c=_467.length;x<c;x++){if(_467[x].slice(0,5)=="dojo-"){return "dojo:"+_467[x].substr(5).toLowerCase();}}}}
return "";}
this.parseElement=function(node,_46b,_46c,_46d){var _46e=getTagName(node);if(isIE&&_46e.indexOf("/")==0){return null;}
try{if(node.getAttribute("parseWidgets").toLowerCase()=="false"){return {};}}
catch(e){}
var _46f=true;if(_46c){var _470=getDojoTagName(node);_46e=_470||_46e;_46f=Boolean(_470);}
var _471={};_471[_46e]=[];var pos=_46e.indexOf(":");if(pos>0){var ns=_46e.substring(0,pos);_471["ns"]=ns;if((dojo.ns)&&(!dojo.ns.allow(ns))){_46f=false;}}
if(_46f){var _474=this.parseAttributes(node);for(var attr in _474){if((!_471[_46e][attr])||(typeof _471[_46e][attr]!="array")){_471[_46e][attr]=[];}
_471[_46e][attr].push(_474[attr]);}
_471[_46e].nodeRef=node;_471.tagName=_46e;_471.index=_46d||0;}
var _476=0;for(var i=0;i<node.childNodes.length;i++){var tcn=node.childNodes.item(i);switch(tcn.nodeType){case dojo.dom.ELEMENT_NODE:
_476++;var ctn=getDojoTagName(tcn)||getTagName(tcn);if(!_471[ctn]){_471[ctn]=[];}
_471[ctn].push(this.parseElement(tcn,true,_46c,_476));if((tcn.childNodes.length==1)&&(tcn.childNodes.item(0).nodeType==dojo.dom.TEXT_NODE)){_471[ctn][_471[ctn].length-1].value=tcn.childNodes.item(0).nodeValue;}
break;case dojo.dom.TEXT_NODE:
if(node.childNodes.length==1){_471[_46e].push({value:node.childNodes.item(0).nodeValue});}
break;default:
break;}}
return _471;};this.parseAttributes=function(node){var _47b={};var atts=node.attributes;var _47d,i=0;while((_47d=atts[i++])){if(isIE){if(!_47d){continue;}
if((typeof _47d=="object")&&(typeof _47d.nodeValue=="undefined")||(_47d.nodeValue==null)||(_47d.nodeValue=="")){continue;}}
var nn=_47d.nodeName.split(":");nn=(nn.length==2)?nn[1]:_47d.nodeName;_47b[nn]={value:_47d.nodeValue};}
return _47b;};};dojo.provide("dojo.lang.declare");dojo.lang.declare=function(_480,_481,init,_483){if((dojo.lang.isFunction(_483))||((!_483)&&(!dojo.lang.isFunction(init)))){var temp=_483;_483=init;init=temp;}
var _485=[];if(dojo.lang.isArray(_481)){_485=_481;_481=_485.shift();}
if(!init){init=dojo.evalObjPath(_480,false);if((init)&&(!dojo.lang.isFunction(init))){init=null;}}
var ctor=dojo.lang.declare._makeConstructor();var scp=(_481?_481.prototype:null);if(scp){scp.prototyping=true;ctor.prototype=new _481();scp.prototyping=false;}
ctor.superclass=scp;ctor.mixins=_485;for(var i=0,l=_485.length;i<l;i++){dojo.lang.extend(ctor,_485[i].prototype);}
ctor.prototype.initializer=null;ctor.prototype.declaredClass=_480;if(dojo.lang.isArray(_483)){dojo.lang.extend.apply(dojo.lang,[ctor].concat(_483));}else{dojo.lang.extend(ctor,(_483)||{});}
dojo.lang.extend(ctor,dojo.lang.declare._common);ctor.prototype.constructor=ctor;ctor.prototype.initializer=(ctor.prototype.initializer)||(init)||(function(){});dojo.lang.setObjPathValue(_480,ctor,null,true);return ctor;};dojo.lang.declare._makeConstructor=function(){return function(){var self=this._getPropContext();var s=self.constructor.superclass;if((s)&&(s.constructor)){if(s.constructor==arguments.callee){this._inherited("constructor",arguments);}else{this._contextMethod(s,"constructor",arguments);}}
var ms=(self.constructor.mixins)||([]);for(var i=0,m;(m=ms[i]);i++){(((m.prototype)&&(m.prototype.initializer))||(m)).apply(this,arguments);}
if((!this.prototyping)&&(self.initializer)){self.initializer.apply(this,arguments);}};};dojo.lang.declare._common={_getPropContext:function(){return (this.___proto||this);},_contextMethod:function(_48f,_490,args){var _492,_493=this.___proto;this.___proto=_48f;try{_492=_48f[_490].apply(this,(args||[]));}
catch(e){throw e;}
finally{this.___proto=_493;}
return _492;},_inherited:function(prop,args){var p=this._getPropContext();do{if((!p.constructor)||(!p.constructor.superclass)){return;}
p=p.constructor.superclass;}while(!(prop in p));return (dojo.lang.isFunction(p[prop])?this._contextMethod(p,prop,args):p[prop]);},inherited:function(prop,args){dojo.deprecated("'inherited' method is dangerous, do not up-call! 'inherited' is slated for removal in 0.5; name your super class (or use superclass property) instead.","0.5");this._inherited(prop,args);}};dojo.declare=dojo.lang.declare;dojo.provide("dojo.ns");dojo.ns={namespaces:{},failed:{},loading:{},loaded:{},register:function(name,_49a,_49b,_49c){if(!_49c||!this.namespaces[name]){this.namespaces[name]=new dojo.ns.Ns(name,_49a,_49b);}},allow:function(name){if(this.failed[name]){return false;}
if((djConfig.excludeNamespace)&&(dojo.lang.inArray(djConfig.excludeNamespace,name))){return false;}
return ((name==this.dojo)||(!djConfig.includeNamespace)||(dojo.lang.inArray(djConfig.includeNamespace,name)));},get:function(name){return this.namespaces[name];},require:function(name){var ns=this.namespaces[name];if((ns)&&(this.loaded[name])){return ns;}
if(!this.allow(name)){return false;}
if(this.loading[name]){dojo.debug("dojo.namespace.require: re-entrant request to load namespace \""+name+"\" must fail.");return false;}
var req=dojo.require;this.loading[name]=true;try{if(name=="dojo"){req("dojo.namespaces.dojo");}else{if(!dojo.hostenv.moduleHasPrefix(name)){dojo.registerModulePath(name,"../"+name);}
req([name,"manifest"].join("."),false,true);}
if(!this.namespaces[name]){this.failed[name]=true;}}
finally{this.loading[name]=false;}
return this.namespaces[name];}};dojo.ns.Ns=function(name,_4a3,_4a4){this.name=name;this.module=_4a3;this.resolver=_4a4;this._loaded=[];this._failed=[];};dojo.ns.Ns.prototype.resolve=function(name,_4a6,_4a7){if(!this.resolver||djConfig["skipAutoRequire"]){return false;}
var _4a8=this.resolver(name,_4a6);if((_4a8)&&(!this._loaded[_4a8])&&(!this._failed[_4a8])){var req=dojo.require;req(_4a8,false,true);if(dojo.hostenv.findModule(_4a8,false)){this._loaded[_4a8]=true;}else{if(!_4a7){dojo.raise("dojo.ns.Ns.resolve: module '"+_4a8+"' not found after loading via namespace '"+this.name+"'");}
this._failed[_4a8]=true;}}
return Boolean(this._loaded[_4a8]);};dojo.registerNamespace=function(name,_4ab,_4ac){dojo.ns.register.apply(dojo.ns,arguments);};dojo.registerNamespaceResolver=function(name,_4ae){var n=dojo.ns.namespaces[name];if(n){n.resolver=_4ae;}};dojo.registerNamespaceManifest=function(_4b0,path,name,_4b3,_4b4){dojo.registerModulePath(name,path);dojo.registerNamespace(name,_4b3,_4b4);};dojo.registerNamespace("dojo","dojo.widget");dojo.provide("dojo.event.topic");dojo.event.topic=new function(){this.topics={};this.getTopic=function(_4b5){if(!this.topics[_4b5]){this.topics[_4b5]=new this.TopicImpl(_4b5);}
return this.topics[_4b5];};this.registerPublisher=function(_4b6,obj,_4b8){var _4b6=this.getTopic(_4b6);_4b6.registerPublisher(obj,_4b8);};this.subscribe=function(_4b9,obj,_4bb){var _4b9=this.getTopic(_4b9);_4b9.subscribe(obj,_4bb);};this.unsubscribe=function(_4bc,obj,_4be){var _4bc=this.getTopic(_4bc);_4bc.unsubscribe(obj,_4be);};this.destroy=function(_4bf){this.getTopic(_4bf).destroy();delete this.topics[_4bf];};this.publishApply=function(_4c0,args){var _4c0=this.getTopic(_4c0);_4c0.sendMessage.apply(_4c0,args);};this.publish=function(_4c2,_4c3){var _4c2=this.getTopic(_4c2);var args=[];for(var x=1;x<arguments.length;x++){args.push(arguments[x]);}
_4c2.sendMessage.apply(_4c2,args);};};dojo.event.topic.TopicImpl=function(_4c6){this.topicName=_4c6;this.subscribe=function(_4c7,_4c8){var tf=_4c8||_4c7;var to=(!_4c8)?dj_global:_4c7;return dojo.event.kwConnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});};this.unsubscribe=function(_4cb,_4cc){var tf=(!_4cc)?_4cb:_4cc;var to=(!_4cc)?null:_4cb;return dojo.event.kwDisconnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});};this._getJoinPoint=function(){return dojo.event.MethodJoinPoint.getForMethod(this,"sendMessage");};this.setSquelch=function(_4cf){this._getJoinPoint().squelch=_4cf;};this.destroy=function(){this._getJoinPoint().disconnect();};this.registerPublisher=function(_4d0,_4d1){dojo.event.connect(_4d0,_4d1,this,"sendMessage");};this.sendMessage=function(_4d2){};};dojo.provide("dojo.event.*");dojo.provide("dojo.widget.Manager");dojo.widget.manager=new function(){this.widgets=[];this.widgetIds=[];this.topWidgets={};var _4d3={};var _4d4=[];this.getUniqueId=function(_4d5){var _4d6;do{_4d6=_4d5+"_"+(_4d3[_4d5]!=undefined?++_4d3[_4d5]:_4d3[_4d5]=0);}while(this.getWidgetById(_4d6));return _4d6;};this.add=function(_4d7){this.widgets.push(_4d7);if(!_4d7.extraArgs["id"]){_4d7.extraArgs["id"]=_4d7.extraArgs["ID"];}
if(_4d7.widgetId==""){if(_4d7["id"]){_4d7.widgetId=_4d7["id"];}else{if(_4d7.extraArgs["id"]){_4d7.widgetId=_4d7.extraArgs["id"];}else{_4d7.widgetId=this.getUniqueId(_4d7.ns+"_"+_4d7.widgetType);}}}
if(this.widgetIds[_4d7.widgetId]){dojo.debug("widget ID collision on ID: "+_4d7.widgetId);}
this.widgetIds[_4d7.widgetId]=_4d7;};this.destroyAll=function(){for(var x=this.widgets.length-1;x>=0;x--){try{this.widgets[x].destroy(true);delete this.widgets[x];}
catch(e){}}};this.remove=function(_4d9){if(dojo.lang.isNumber(_4d9)){var tw=this.widgets[_4d9].widgetId;delete this.widgetIds[tw];this.widgets.splice(_4d9,1);}else{this.removeById(_4d9);}};this.removeById=function(id){if(!dojo.lang.isString(id)){id=id["widgetId"];if(!id){dojo.debug("invalid widget or id passed to removeById");return;}}
for(var i=0;i<this.widgets.length;i++){if(this.widgets[i].widgetId==id){this.remove(i);break;}}};this.getWidgetById=function(id){if(dojo.lang.isString(id)){return this.widgetIds[id];}
return id;};this.getWidgetsByType=function(type){var lt=type.toLowerCase();var _4e0=(type.indexOf(":")<0?function(x){return x.widgetType.toLowerCase();}:function(x){return x.getNamespacedType();});var ret=[];dojo.lang.forEach(this.widgets,function(x){if(_4e0(x)==lt){ret.push(x);}});return ret;};this.getWidgetsByFilter=function(_4e5,_4e6){var ret=[];dojo.lang.every(this.widgets,function(x){if(_4e5(x)){ret.push(x);if(_4e6){return false;}}
return true;});return (_4e6?ret[0]:ret);};this.getAllWidgets=function(){return this.widgets.concat();};this.getWidgetByNode=function(node){var w=this.getAllWidgets();node=dojo.byId(node);for(var i=0;i<w.length;i++){if(w[i].domNode==node){return w[i];}}
return null;};this.byId=this.getWidgetById;this.byType=this.getWidgetsByType;this.byFilter=this.getWidgetsByFilter;this.byNode=this.getWidgetByNode;var _4ec={};var _4ed=["dojo.widget"];for(var i=0;i<_4ed.length;i++){_4ed[_4ed[i]]=true;}
this.registerWidgetPackage=function(_4ef){if(!_4ed[_4ef]){_4ed[_4ef]=true;_4ed.push(_4ef);}};this.getWidgetPackageList=function(){return dojo.lang.map(_4ed,function(elt){return (elt!==true?elt:undefined);});};this.getImplementation=function(_4f1,_4f2,_4f3,ns){var impl=this.getImplementationName(_4f1,ns);if(impl){var ret=_4f2?new impl(_4f2):new impl();return ret;}};function buildPrefixCache(){for(var _4f7 in dojo.render){if(dojo.render[_4f7]["capable"]===true){var _4f8=dojo.render[_4f7].prefixes;for(var i=0;i<_4f8.length;i++){_4d4.push(_4f8[i].toLowerCase());}}}}
var _4fa=function(_4fb,_4fc){if(!_4fc){return null;}
for(var i=0,l=_4d4.length,_4ff;i<=l;i++){_4ff=(i<l?_4fc[_4d4[i]]:_4fc);if(!_4ff){continue;}
for(var name in _4ff){if(name.toLowerCase()==_4fb){return _4ff[name];}}}
return null;};var _501=function(_502,_503){var _504=dojo.evalObjPath(_503,false);return (_504?_4fa(_502,_504):null);};this.getImplementationName=function(_505,ns){var _507=_505.toLowerCase();ns=ns||"dojo";var imps=_4ec[ns]||(_4ec[ns]={});var impl=imps[_507];if(impl){return impl;}
if(!_4d4.length){buildPrefixCache();}
var _50a=dojo.ns.get(ns);if(!_50a){dojo.ns.register(ns,ns+".widget");_50a=dojo.ns.get(ns);}
if(_50a){_50a.resolve(_505);}
impl=_501(_507,_50a.module);if(impl){return (imps[_507]=impl);}
_50a=dojo.ns.require(ns);if((_50a)&&(_50a.resolver)){_50a.resolve(_505);impl=_501(_507,_50a.module);if(impl){return (imps[_507]=impl);}}
dojo.deprecated("dojo.widget.Manager.getImplementationName","Could not locate widget implementation for \""+_505+"\" in \""+_50a.module+"\" registered to namespace \""+_50a.name+"\". "+"Developers must specify correct namespaces for all non-Dojo widgets","0.5");for(var i=0;i<_4ed.length;i++){impl=_501(_507,_4ed[i]);if(impl){return (imps[_507]=impl);}}
throw new Error("Could not locate widget implementation for \""+_505+"\" in \""+_50a.module+"\" registered to namespace \""+_50a.name+"\"");};this.resizing=false;this.onWindowResized=function(){if(this.resizing){return;}
try{this.resizing=true;for(var id in this.topWidgets){var _50d=this.topWidgets[id];if(_50d.checkSize){_50d.checkSize();}}}
catch(e){}
finally{this.resizing=false;}};if(typeof window!="undefined"){dojo.addOnLoad(this,"onWindowResized");dojo.event.connect(window,"onresize",this,"onWindowResized");}};(function(){var dw=dojo.widget;var dwm=dw.manager;var h=dojo.lang.curry(dojo.lang,"hitch",dwm);var g=function(_512,_513){dw[(_513||_512)]=h(_512);};g("add","addWidget");g("destroyAll","destroyAllWidgets");g("remove","removeWidget");g("removeById","removeWidgetById");g("getWidgetById");g("getWidgetById","byId");g("getWidgetsByType");g("getWidgetsByFilter");g("getWidgetsByType","byType");g("getWidgetsByFilter","byFilter");g("getWidgetByNode","byNode");dw.all=function(n){var _515=dwm.getAllWidgets.apply(dwm,arguments);if(arguments.length>0){return _515[n];}
return _515;};g("registerWidgetPackage");g("getImplementation","getWidgetImplementation");g("getImplementationName","getWidgetImplementationName");dw.widgets=dwm.widgets;dw.widgetIds=dwm.widgetIds;dw.root=dwm.root;})();dojo.provide("dojo.uri.Uri");dojo.uri=new function(){var _516=new RegExp("^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$");var _517=new RegExp("(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$");this.dojoUri=function(uri){return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri(),uri);};this.moduleUri=function(_519,uri){var loc=dojo.hostenv.getModuleSymbols(_519).join("/");if(!loc){return null;}
if(loc.lastIndexOf("/")!=loc.length-1){loc+="/";}
return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri()+loc,uri);};this.Uri=function(){var uri=arguments[0];for(var i=1;i<arguments.length;i++){if(!arguments[i]){continue;}
var _51e=new dojo.uri.Uri(arguments[i].toString());var _51f=new dojo.uri.Uri(uri.toString());if((_51e.path=="")&&(_51e.scheme==null)&&(_51e.authority==null)&&(_51e.query==null)){if(_51e.fragment!=null){_51f.fragment=_51e.fragment;}
_51e=_51f;}
if(_51e.scheme!=null&&_51e.authority!=null){uri="";}
if(_51e.scheme!=null){uri+=_51e.scheme+":";}
if(_51e.authority!=null){uri+="//"+_51e.authority;}
uri+=_51e.path;if(_51e.query!=null){uri+="?"+_51e.query;}
if(_51e.fragment!=null){uri+="#"+_51e.fragment;}}
this.uri=uri.toString();var r=this.uri.match(_517);this.scheme=r[2]||(r[1]?"":null);this.authority=r[4]||(r[3]?"":null);this.path=r[5];this.query=r[7]||(r[6]?"":null);this.fragment=r[9]||(r[8]?"":null);if(this.authority!=null){r=this.authority.match(_516);this.user=r[3]||null;this.password=r[4]||null;this.host=r[5];this.port=r[7]||null;}
this.toString=function(){return this.uri;};};};dojo.provide("dojo.uri.*");dojo.provide("dojo.html.common");dojo.lang.mixin(dojo.html,dojo.dom);dojo.html.getEventTarget=function(evt){if(!evt){evt=dojo.global().event||{};}
var t=(evt.srcElement?evt.srcElement:(evt.target?evt.target:null));while((t)&&(t.nodeType!=1)){t=t.parentNode;}
return t;};dojo.html.getViewport=function(){var _523=dojo.global();var _524=dojo.doc();var w=0;var h=0;if(dojo.render.html.mozilla){w=_524.documentElement.clientWidth;h=_523.innerHeight;}else{if(!dojo.render.html.opera&&_523.innerWidth){w=_523.innerWidth;h=_523.innerHeight;}else{if(!dojo.render.html.opera&&dojo.exists(_524,"documentElement.clientWidth")){var w2=_524.documentElement.clientWidth;if(!w||w2&&w2<w){w=w2;}
h=_524.documentElement.clientHeight;}else{if(dojo.body().clientWidth){w=dojo.body().clientWidth;h=dojo.body().clientHeight;}}}}
return {width:w,height:h};};dojo.html.getScroll=function(){var _528=dojo.global();var _529=dojo.doc();var top=_528.pageYOffset||_529.documentElement.scrollTop||dojo.body().scrollTop||0;var left=_528.pageXOffset||_529.documentElement.scrollLeft||dojo.body().scrollLeft||0;return {top:top,left:left,offset:{x:left,y:top}};};dojo.html.getParentByType=function(node,type){var _52e=dojo.doc();var _52f=dojo.byId(node);type=type.toLowerCase();while((_52f)&&(_52f.nodeName.toLowerCase()!=type)){if(_52f==(_52e["body"]||_52e["documentElement"])){return null;}
_52f=_52f.parentNode;}
return _52f;};dojo.html.getAttribute=function(node,attr){node=dojo.byId(node);if((!node)||(!node.getAttribute)){return null;}
var ta=typeof attr=="string"?attr:new String(attr);var v=node.getAttribute(ta.toUpperCase());if((v)&&(typeof v=="string")&&(v!="")){return v;}
if(v&&v.value){return v.value;}
if((node.getAttributeNode)&&(node.getAttributeNode(ta))){return (node.getAttributeNode(ta)).value;}else{if(node.getAttribute(ta)){return node.getAttribute(ta);}else{if(node.getAttribute(ta.toLowerCase())){return node.getAttribute(ta.toLowerCase());}}}
return null;};dojo.html.hasAttribute=function(node,attr){return dojo.html.getAttribute(dojo.byId(node),attr)?true:false;};dojo.html.getCursorPosition=function(e){e=e||dojo.global().event;var _537={x:0,y:0};if(e.pageX||e.pageY){_537.x=e.pageX;_537.y=e.pageY;}else{var de=dojo.doc().documentElement;var db=dojo.body();_537.x=e.clientX+((de||db)["scrollLeft"])-((de||db)["clientLeft"]);_537.y=e.clientY+((de||db)["scrollTop"])-((de||db)["clientTop"]);}
return _537;};dojo.html.isTag=function(node){node=dojo.byId(node);if(node&&node.tagName){for(var i=1;i<arguments.length;i++){if(node.tagName.toLowerCase()==String(arguments[i]).toLowerCase()){return String(arguments[i]).toLowerCase();}}}
return "";};if(dojo.render.html.ie&&!dojo.render.html.ie70){if(window.location.href.substr(0,6).toLowerCase()!="https:"){(function(){var _53c=dojo.doc().createElement("script");_53c.src="javascript:'dojo.html.createExternalElement=function(doc, tag){ return doc.createElement(tag); }'";dojo.doc().getElementsByTagName("head")[0].appendChild(_53c);})();}}else{dojo.html.createExternalElement=function(doc,tag){return doc.createElement(tag);};}
dojo.provide("dojo.a11y");dojo.a11y={imgPath:dojo.uri.dojoUri("src/widget/templates/images"),doAccessibleCheck:true,accessible:null,checkAccessible:function(){if(this.accessible===null){this.accessible=false;if(this.doAccessibleCheck==true){this.accessible=this.testAccessible();}}
return this.accessible;},testAccessible:function(){this.accessible=false;if(dojo.render.html.ie||dojo.render.html.mozilla){var div=document.createElement("div");div.style.backgroundImage="url(\""+this.imgPath+"/tab_close.gif\")";dojo.body().appendChild(div);var _540=null;if(window.getComputedStyle){var _541=getComputedStyle(div,"");_540=_541.getPropertyValue("background-image");}else{_540=div.currentStyle.backgroundImage;}
var _542=false;if(_540!=null&&(_540=="none"||_540=="url(invalid-url:)")){this.accessible=true;}
dojo.body().removeChild(div);}
return this.accessible;},setCheckAccessible:function(_543){this.doAccessibleCheck=_543;},setAccessibleMode:function(){if(this.accessible===null){if(this.checkAccessible()){dojo.render.html.prefixes.unshift("a11y");}}
return this.accessible;}};dojo.provide("dojo.widget.Widget");dojo.declare("dojo.widget.Widget",null,function(){this.children=[];this.extraArgs={};},{parent:null,isTopLevel:false,disabled:false,isContainer:false,widgetId:"",widgetType:"Widget",ns:"dojo",getNamespacedType:function(){return (this.ns?this.ns+":"+this.widgetType:this.widgetType).toLowerCase();},toString:function(){return "[Widget "+this.getNamespacedType()+", "+(this.widgetId||"NO ID")+"]";},repr:function(){return this.toString();},enable:function(){this.disabled=false;},disable:function(){this.disabled=true;},onResized:function(){this.notifyChildrenOfResize();},notifyChildrenOfResize:function(){for(var i=0;i<this.children.length;i++){var _545=this.children[i];if(_545.onResized){_545.onResized();}}},create:function(args,_547,_548,ns){if(ns){this.ns=ns;}
this.satisfyPropertySets(args,_547,_548);this.mixInProperties(args,_547,_548);this.postMixInProperties(args,_547,_548);dojo.widget.manager.add(this);this.buildRendering(args,_547,_548);this.initialize(args,_547,_548);this.postInitialize(args,_547,_548);this.postCreate(args,_547,_548);return this;},destroy:function(_54a){this.destroyChildren();this.uninitialize();this.destroyRendering(_54a);dojo.widget.manager.removeById(this.widgetId);},destroyChildren:function(){var _54b;var i=0;while(this.children.length>i){_54b=this.children[i];if(_54b instanceof dojo.widget.Widget){this.removeChild(_54b);_54b.destroy();continue;}
i++;}},getChildrenOfType:function(type,_54e){var ret=[];var _550=dojo.lang.isFunction(type);if(!_550){type=type.toLowerCase();}
for(var x=0;x<this.children.length;x++){if(_550){if(this.children[x] instanceof type){ret.push(this.children[x]);}}else{if(this.children[x].widgetType.toLowerCase()==type){ret.push(this.children[x]);}}
if(_54e){ret=ret.concat(this.children[x].getChildrenOfType(type,_54e));}}
return ret;},getDescendants:function(){var _552=[];var _553=[this];var elem;while((elem=_553.pop())){_552.push(elem);if(elem.children){dojo.lang.forEach(elem.children,function(elem){_553.push(elem);});}}
return _552;},isFirstChild:function(){return this===this.parent.children[0];},isLastChild:function(){return this===this.parent.children[this.parent.children.length-1];},satisfyPropertySets:function(args){return args;},mixInProperties:function(args,frag){if((args["fastMixIn"])||(frag["fastMixIn"])){for(var x in args){this[x]=args[x];}
return;}
var _55a;var _55b=dojo.widget.lcArgsCache[this.widgetType];if(_55b==null){_55b={};for(var y in this){_55b[((new String(y)).toLowerCase())]=y;}
dojo.widget.lcArgsCache[this.widgetType]=_55b;}
var _55d={};for(var x in args){if(!this[x]){var y=_55b[(new String(x)).toLowerCase()];if(y){args[y]=args[x];x=y;}}
if(_55d[x]){continue;}
_55d[x]=true;if((typeof this[x])!=(typeof _55a)){if(typeof args[x]!="string"){this[x]=args[x];}else{if(dojo.lang.isString(this[x])){this[x]=args[x];}else{if(dojo.lang.isNumber(this[x])){this[x]=new Number(args[x]);}else{if(dojo.lang.isBoolean(this[x])){this[x]=(args[x].toLowerCase()=="false")?false:true;}else{if(dojo.lang.isFunction(this[x])){if(args[x].search(/[^\w\.]+/i)==-1){this[x]=dojo.evalObjPath(args[x],false);}else{var tn=dojo.lang.nameAnonFunc(new Function(args[x]),this);dojo.event.kwConnect({srcObj:this,srcFunc:x,adviceObj:this,adviceFunc:tn});}}else{if(dojo.lang.isArray(this[x])){this[x]=args[x].split(";");}else{if(this[x] instanceof Date){this[x]=new Date(Number(args[x]));}else{if(typeof this[x]=="object"){if(this[x] instanceof dojo.uri.Uri){this[x]=dojo.uri.dojoUri(args[x]);}else{var _55f=args[x].split(";");for(var y=0;y<_55f.length;y++){var si=_55f[y].indexOf(":");if((si!=-1)&&(_55f[y].length>si)){this[x][_55f[y].substr(0,si).replace(/^\s+|\s+$/g,"")]=_55f[y].substr(si+1);}}}}else{this[x]=args[x];}}}}}}}}}else{this.extraArgs[x.toLowerCase()]=args[x];}}},postMixInProperties:function(args,frag,_563){},initialize:function(args,frag,_566){return false;},postInitialize:function(args,frag,_569){return false;},postCreate:function(args,frag,_56c){return false;},uninitialize:function(){return false;},buildRendering:function(args,frag,_56f){dojo.unimplemented("dojo.widget.Widget.buildRendering, on "+this.toString()+", ");return false;},destroyRendering:function(){dojo.unimplemented("dojo.widget.Widget.destroyRendering");return false;},addedTo:function(_570){},addChild:function(_571){dojo.unimplemented("dojo.widget.Widget.addChild");return false;},removeChild:function(_572){for(var x=0;x<this.children.length;x++){if(this.children[x]===_572){this.children.splice(x,1);break;}}
return _572;},getPreviousSibling:function(){var idx=this.getParentIndex();if(idx<=0){return null;}
return this.parent.children[idx-1];},getSiblings:function(){return this.parent.children;},getParentIndex:function(){return dojo.lang.indexOf(this.parent.children,this,true);},getNextSibling:function(){var idx=this.getParentIndex();if(idx==this.parent.children.length-1){return null;}
if(idx<0){return null;}
return this.parent.children[idx+1];}});dojo.widget.lcArgsCache={};dojo.widget.tags={};dojo.widget.tags.addParseTreeHandler=function(type){dojo.deprecated("addParseTreeHandler",". ParseTreeHandlers are now reserved for components. Any unfiltered DojoML tag without a ParseTreeHandler is assumed to be a widget","0.5");};dojo.widget.tags["dojo:propertyset"]=function(_577,_578,_579){var _57a=_578.parseProperties(_577["dojo:propertyset"]);};dojo.widget.tags["dojo:connect"]=function(_57b,_57c,_57d){var _57e=_57c.parseProperties(_57b["dojo:connect"]);};dojo.widget.buildWidgetFromParseTree=function(type,frag,_581,_582,_583,_584){dojo.a11y.setAccessibleMode();var _585=type.split(":");_585=(_585.length==2)?_585[1]:type;var _586=_584||_581.parseProperties(frag[frag["ns"]+":"+_585]);var _587=dojo.widget.manager.getImplementation(_585,null,null,frag["ns"]);if(!_587){throw new Error("cannot find \""+type+"\" widget");}else{if(!_587.create){throw new Error("\""+type+"\" widget object has no \"create\" method and does not appear to implement *Widget");}}
_586["dojoinsertionindex"]=_583;var ret=_587.create(_586,frag,_582,frag["ns"]);return ret;};dojo.widget.defineWidget=function(_589,_58a,_58b,init,_58d){if(dojo.lang.isString(arguments[3])){dojo.widget._defineWidget(arguments[0],arguments[3],arguments[1],arguments[4],arguments[2]);}else{var args=[arguments[0]],p=3;if(dojo.lang.isString(arguments[1])){args.push(arguments[1],arguments[2]);}else{args.push("",arguments[1]);p=2;}
if(dojo.lang.isFunction(arguments[p])){args.push(arguments[p],arguments[p+1]);}else{args.push(null,arguments[p]);}
dojo.widget._defineWidget.apply(this,args);}};dojo.widget.defineWidget.renderers="html|svg|vml";dojo.widget._defineWidget=function(_590,_591,_592,init,_594){var _595=_590.split(".");var type=_595.pop();var regx="\\.("+(_591?_591+"|":"")+dojo.widget.defineWidget.renderers+")\\.";var r=_590.search(new RegExp(regx));_595=(r<0?_595.join("."):_590.substr(0,r));dojo.widget.manager.registerWidgetPackage(_595);var pos=_595.indexOf(".");var _59a=(pos>-1)?_595.substring(0,pos):_595;_594=(_594)||{};_594.widgetType=type;if((!init)&&(_594["classConstructor"])){init=_594.classConstructor;delete _594.classConstructor;}
dojo.declare(_590,_592,init,_594);};dojo.provide("dojo.widget.Parse");dojo.widget.Parse=function(_59b){this.propertySetsList=[];this.fragment=_59b;this.createComponents=function(frag,_59d){var _59e=[];var _59f=false;try{if(frag&&frag.tagName&&(frag!=frag.nodeRef)){var _5a0=dojo.widget.tags;var tna=String(frag.tagName).split(";");for(var x=0;x<tna.length;x++){var ltn=tna[x].replace(/^\s+|\s+$/g,"").toLowerCase();frag.tagName=ltn;var ret;if(_5a0[ltn]){_59f=true;ret=_5a0[ltn](frag,this,_59d,frag.index);_59e.push(ret);}else{if(ltn.indexOf(":")==-1){ltn="dojo:"+ltn;}
ret=dojo.widget.buildWidgetFromParseTree(ltn,frag,this,_59d,frag.index);if(ret){_59f=true;_59e.push(ret);}}}}}
catch(e){dojo.debug("dojo.widget.Parse: error:",e);}
if(!_59f){_59e=_59e.concat(this.createSubComponents(frag,_59d));}
return _59e;};this.createSubComponents=function(_5a5,_5a6){var frag,_5a8=[];for(var item in _5a5){frag=_5a5[item];if(frag&&typeof frag=="object"&&(frag!=_5a5.nodeRef)&&(frag!=_5a5.tagName)&&(!dojo.dom.isNode(frag))){_5a8=_5a8.concat(this.createComponents(frag,_5a6));}}
return _5a8;};this.parsePropertySets=function(_5aa){return [];};this.parseProperties=function(_5ab){var _5ac={};for(var item in _5ab){if((_5ab[item]==_5ab.tagName)||(_5ab[item]==_5ab.nodeRef)){}else{var frag=_5ab[item];if(frag.tagName&&dojo.widget.tags[frag.tagName.toLowerCase()]){}else{if(frag[0]&&frag[0].value!=""&&frag[0].value!=null){try{if(item.toLowerCase()=="dataprovider"){var _5af=this;this.getDataProvider(_5af,frag[0].value);_5ac.dataProvider=this.dataProvider;}
_5ac[item]=frag[0].value;var _5b0=this.parseProperties(frag);for(var _5b1 in _5b0){_5ac[_5b1]=_5b0[_5b1];}}
catch(e){dojo.debug(e);}}}
switch(item.toLowerCase()){case "checked":
case "disabled":
if(typeof _5ac[item]!="boolean"){_5ac[item]=true;}
break;}}}
return _5ac;};this.getDataProvider=function(_5b2,_5b3){dojo.io.bind({url:_5b3,load:function(type,_5b5){if(type=="load"){_5b2.dataProvider=_5b5;}},mimetype:"text/javascript",sync:true});};this.getPropertySetById=function(_5b6){for(var x=0;x<this.propertySetsList.length;x++){if(_5b6==this.propertySetsList[x]["id"][0].value){return this.propertySetsList[x];}}
return "";};this.getPropertySetsByType=function(_5b8){var _5b9=[];for(var x=0;x<this.propertySetsList.length;x++){var cpl=this.propertySetsList[x];var cpcc=cpl.componentClass||cpl.componentType||null;var _5bd=this.propertySetsList[x]["id"][0].value;if(cpcc&&(_5bd==cpcc[0].value)){_5b9.push(cpl);}}
return _5b9;};this.getPropertySets=function(_5be){var ppl="dojo:propertyproviderlist";var _5c0=[];var _5c1=_5be.tagName;if(_5be[ppl]){var _5c2=_5be[ppl].value.split(" ");for(var _5c3 in _5c2){if((_5c3.indexOf("..")==-1)&&(_5c3.indexOf("://")==-1)){var _5c4=this.getPropertySetById(_5c3);if(_5c4!=""){_5c0.push(_5c4);}}else{}}}
return this.getPropertySetsByType(_5c1).concat(_5c0);};this.createComponentFromScript=function(_5c5,_5c6,_5c7,ns){_5c7.fastMixIn=true;var ltn=(ns||"dojo")+":"+_5c6.toLowerCase();if(dojo.widget.tags[ltn]){return [dojo.widget.tags[ltn](_5c7,this,null,null,_5c7)];}
return [dojo.widget.buildWidgetFromParseTree(ltn,_5c7,this,null,null,_5c7)];};};dojo.widget._parser_collection={"dojo":new dojo.widget.Parse()};dojo.widget.getParser=function(name){if(!name){name="dojo";}
if(!this._parser_collection[name]){this._parser_collection[name]=new dojo.widget.Parse();}
return this._parser_collection[name];};dojo.widget.createWidget=function(name,_5cc,_5cd,_5ce){var _5cf=false;var _5d0=(typeof name=="string");if(_5d0){var pos=name.indexOf(":");var ns=(pos>-1)?name.substring(0,pos):"dojo";if(pos>-1){name=name.substring(pos+1);}
var _5d3=name.toLowerCase();var _5d4=ns+":"+_5d3;_5cf=(dojo.byId(name)&&!dojo.widget.tags[_5d4]);}
if((arguments.length==1)&&(_5cf||!_5d0)){var xp=new dojo.xml.Parse();var tn=_5cf?dojo.byId(name):name;return dojo.widget.getParser().createComponents(xp.parseElement(tn,null,true))[0];}
function fromScript(_5d7,name,_5d9,ns){_5d9[_5d4]={dojotype:[{value:_5d3}],nodeRef:_5d7,fastMixIn:true};_5d9.ns=ns;return dojo.widget.getParser().createComponentFromScript(_5d7,name,_5d9,ns);}
_5cc=_5cc||{};var _5db=false;var tn=null;var h=dojo.render.html.capable;if(h){tn=document.createElement("span");}
if(!_5cd){_5db=true;_5cd=tn;if(h){dojo.body().appendChild(_5cd);}}else{if(_5ce){dojo.dom.insertAtPosition(tn,_5cd,_5ce);}else{tn=_5cd;}}
var _5dd=fromScript(tn,name.toLowerCase(),_5cc,ns);if((!_5dd)||(!_5dd[0])||(typeof _5dd[0].widgetType=="undefined")){throw new Error("createWidget: Creation of \""+name+"\" widget failed.");}
try{if(_5db&&_5dd[0].domNode.parentNode){_5dd[0].domNode.parentNode.removeChild(_5dd[0].domNode);}}
catch(e){dojo.debug(e);}
return _5dd[0];};dojo.provide("dojo.html.style");dojo.html.getClass=function(node){node=dojo.byId(node);if(!node){return "";}
var cs="";if(node.className){cs=node.className;}else{if(dojo.html.hasAttribute(node,"class")){cs=dojo.html.getAttribute(node,"class");}}
return cs.replace(/^\s+|\s+$/g,"");};dojo.html.getClasses=function(node){var c=dojo.html.getClass(node);return (c=="")?[]:c.split(/\s+/g);};dojo.html.hasClass=function(node,_5e3){return (new RegExp("(^|\\s+)"+_5e3+"(\\s+|$)")).test(dojo.html.getClass(node));};dojo.html.prependClass=function(node,_5e5){_5e5+=" "+dojo.html.getClass(node);return dojo.html.setClass(node,_5e5);};dojo.html.addClass=function(node,_5e7){if(dojo.html.hasClass(node,_5e7)){return false;}
_5e7=(dojo.html.getClass(node)+" "+_5e7).replace(/^\s+|\s+$/g,"");return dojo.html.setClass(node,_5e7);};dojo.html.setClass=function(node,_5e9){node=dojo.byId(node);var cs=new String(_5e9);try{if(typeof node.className=="string"){node.className=cs;}else{if(node.setAttribute){node.setAttribute("class",_5e9);node.className=cs;}else{return false;}}}
catch(e){dojo.debug("dojo.html.setClass() failed",e);}
return true;};dojo.html.removeClass=function(node,_5ec,_5ed){try{if(!_5ed){var _5ee=dojo.html.getClass(node).replace(new RegExp("(^|\\s+)"+_5ec+"(\\s+|$)"),"$1$2");}else{var _5ee=dojo.html.getClass(node).replace(_5ec,"");}
dojo.html.setClass(node,_5ee);}
catch(e){dojo.debug("dojo.html.removeClass() failed",e);}
return true;};dojo.html.replaceClass=function(node,_5f0,_5f1){dojo.html.removeClass(node,_5f1);dojo.html.addClass(node,_5f0);};dojo.html.classMatchType={ContainsAll:0,ContainsAny:1,IsOnly:2};dojo.html.getElementsByClass=function(_5f2,_5f3,_5f4,_5f5,_5f6){_5f6=false;var _5f7=dojo.doc();_5f3=dojo.byId(_5f3)||_5f7;var _5f8=_5f2.split(/\s+/g);var _5f9=[];if(_5f5!=1&&_5f5!=2){_5f5=0;}
var _5fa=new RegExp("(\\s|^)(("+_5f8.join(")|(")+"))(\\s|$)");var _5fb=_5f8.join(" ").length;var _5fc=[];if(!_5f6&&_5f7.evaluate){var _5fd=".//"+(_5f4||"*")+"[contains(";if(_5f5!=dojo.html.classMatchType.ContainsAny){_5fd+="concat(' ',@class,' '), ' "+_5f8.join(" ') and contains(concat(' ',@class,' '), ' ")+" ')";if(_5f5==2){_5fd+=" and string-length(@class)="+_5fb+"]";}else{_5fd+="]";}}else{_5fd+="concat(' ',@class,' '), ' "+_5f8.join(" ') or contains(concat(' ',@class,' '), ' ")+" ')]";}
var _5fe=_5f7.evaluate(_5fd,_5f3,null,XPathResult.ANY_TYPE,null);var _5ff=_5fe.iterateNext();while(_5ff){try{_5fc.push(_5ff);_5ff=_5fe.iterateNext();}
catch(e){break;}}
return _5fc;}else{if(!_5f4){_5f4="*";}
_5fc=_5f3.getElementsByTagName(_5f4);var node,i=0;outer:
while(node=_5fc[i++]){var _602=dojo.html.getClasses(node);if(_602.length==0){continue outer;}
var _603=0;for(var j=0;j<_602.length;j++){if(_5fa.test(_602[j])){if(_5f5==dojo.html.classMatchType.ContainsAny){_5f9.push(node);continue outer;}else{_603++;}}else{if(_5f5==dojo.html.classMatchType.IsOnly){continue outer;}}}
if(_603==_5f8.length){if((_5f5==dojo.html.classMatchType.IsOnly)&&(_603==_602.length)){_5f9.push(node);}else{if(_5f5==dojo.html.classMatchType.ContainsAll){_5f9.push(node);}}}}
return _5f9;}};dojo.html.getElementsByClassName=dojo.html.getElementsByClass;dojo.html.toCamelCase=function(_605){var arr=_605.split("-"),cc=arr[0];for(var i=1;i<arr.length;i++){cc+=arr[i].charAt(0).toUpperCase()+arr[i].substring(1);}
return cc;};dojo.html.toSelectorCase=function(_609){return _609.replace(/([A-Z])/g,"-$1").toLowerCase();};dojo.html.getComputedStyle=function(node,_60b,_60c){node=dojo.byId(node);var _60b=dojo.html.toSelectorCase(_60b);var _60d=dojo.html.toCamelCase(_60b);if(!node||!node.style){return _60c;}else{if(document.defaultView&&dojo.html.isDescendantOf(node,node.ownerDocument)){try{var cs=document.defaultView.getComputedStyle(node,"");if(cs){return cs.getPropertyValue(_60b);}}
catch(e){if(node.style.getPropertyValue){return node.style.getPropertyValue(_60b);}else{return _60c;}}}else{if(node.currentStyle){return node.currentStyle[_60d];}}}
if(node.style.getPropertyValue){return node.style.getPropertyValue(_60b);}else{return _60c;}};dojo.html.getStyleProperty=function(node,_610){node=dojo.byId(node);return (node&&node.style?node.style[dojo.html.toCamelCase(_610)]:undefined);};dojo.html.getStyle=function(node,_612){var _613=dojo.html.getStyleProperty(node,_612);return (_613?_613:dojo.html.getComputedStyle(node,_612));};dojo.html.setStyle=function(node,_615,_616){node=dojo.byId(node);if(node&&node.style){var _617=dojo.html.toCamelCase(_615);node.style[_617]=_616;}};dojo.html.setStyleText=function(_618,text){try{_618.style.cssText=text;}
catch(e){_618.setAttribute("style",text);}};dojo.html.copyStyle=function(_61a,_61b){if(!_61b.style.cssText){_61a.setAttribute("style",_61b.getAttribute("style"));}else{_61a.style.cssText=_61b.style.cssText;}
dojo.html.addClass(_61a,dojo.html.getClass(_61b));};dojo.html.getUnitValue=function(node,_61d,_61e){var s=dojo.html.getComputedStyle(node,_61d);if((!s)||((s=="auto")&&(_61e))){return {value:0,units:"px"};}
var _620=s.match(/(\-?[\d.]+)([a-z%]*)/i);if(!_620){return dojo.html.getUnitValue.bad;}
return {value:Number(_620[1]),units:_620[2].toLowerCase()};};dojo.html.getUnitValue.bad={value:NaN,units:""};dojo.html.getPixelValue=function(node,_622,_623){var _624=dojo.html.getUnitValue(node,_622,_623);if(isNaN(_624.value)){return 0;}
if((_624.value)&&(_624.units!="px")){return NaN;}
return _624.value;};dojo.html.setPositivePixelValue=function(node,_626,_627){if(isNaN(_627)){return false;}
node.style[_626]=Math.max(0,_627)+"px";return true;};dojo.html.styleSheet=null;dojo.html.insertCssRule=function(_628,_629,_62a){if(!dojo.html.styleSheet){if(document.createStyleSheet){dojo.html.styleSheet=document.createStyleSheet();}else{if(document.styleSheets[0]){dojo.html.styleSheet=document.styleSheets[0];}else{return null;}}}
if(arguments.length<3){if(dojo.html.styleSheet.cssRules){_62a=dojo.html.styleSheet.cssRules.length;}else{if(dojo.html.styleSheet.rules){_62a=dojo.html.styleSheet.rules.length;}else{return null;}}}
if(dojo.html.styleSheet.insertRule){var rule=_628+" { "+_629+" }";return dojo.html.styleSheet.insertRule(rule,_62a);}else{if(dojo.html.styleSheet.addRule){return dojo.html.styleSheet.addRule(_628,_629,_62a);}else{return null;}}};dojo.html.removeCssRule=function(_62c){if(!dojo.html.styleSheet){dojo.debug("no stylesheet defined for removing rules");return false;}
if(dojo.render.html.ie){if(!_62c){_62c=dojo.html.styleSheet.rules.length;dojo.html.styleSheet.removeRule(_62c);}}else{if(document.styleSheets[0]){if(!_62c){_62c=dojo.html.styleSheet.cssRules.length;}
dojo.html.styleSheet.deleteRule(_62c);}}
return true;};dojo.html._insertedCssFiles=[];dojo.html.insertCssFile=function(URI,doc,_62f,_630){if(!URI){return;}
if(!doc){doc=document;}
var _631=dojo.hostenv.getText(URI,false,_630);if(_631===null){return;}
_631=dojo.html.fixPathsInCssText(_631,URI);if(_62f){var idx=-1,node,ent=dojo.html._insertedCssFiles;for(var i=0;i<ent.length;i++){if((ent[i].doc==doc)&&(ent[i].cssText==_631)){idx=i;node=ent[i].nodeRef;break;}}
if(node){var _636=doc.getElementsByTagName("style");for(var i=0;i<_636.length;i++){if(_636[i]==node){return;}}
dojo.html._insertedCssFiles.shift(idx,1);}}
var _637=dojo.html.insertCssText(_631,doc);dojo.html._insertedCssFiles.push({"doc":doc,"cssText":_631,"nodeRef":_637});if(_637&&djConfig.isDebug){_637.setAttribute("dbgHref",URI);}
return _637;};dojo.html.insertCssText=function(_638,doc,URI){if(!_638){return;}
if(!doc){doc=document;}
if(URI){_638=dojo.html.fixPathsInCssText(_638,URI);}
var _63b=doc.createElement("style");_63b.setAttribute("type","text/css");var head=doc.getElementsByTagName("head")[0];if(!head){dojo.debug("No head tag in document, aborting styles");return;}else{head.appendChild(_63b);}
if(_63b.styleSheet){_63b.styleSheet.cssText=_638;}else{var _63d=doc.createTextNode(_638);_63b.appendChild(_63d);}
return _63b;};dojo.html.fixPathsInCssText=function(_63e,URI){if(!_63e||!URI){return;}
var _640,str="",url="",_643="[\\t\\s\\w\\(\\)\\/\\.\\\\'\"-:#=&?~]+";var _644=new RegExp("url\\(\\s*("+_643+")\\s*\\)");var _645=/(file|https?|ftps?):\/\//;regexTrim=new RegExp("^[\\s]*(['\"]?)("+_643+")\\1[\\s]*?$");if(dojo.render.html.ie55||dojo.render.html.ie60){var _646=new RegExp("AlphaImageLoader\\((.*)src=['\"]("+_643+")['\"]");while(_640=_646.exec(_63e)){url=_640[2].replace(regexTrim,"$2");if(!_645.exec(url)){url=(new dojo.uri.Uri(URI,url).toString());}
str+=_63e.substring(0,_640.index)+"AlphaImageLoader("+_640[1]+"src='"+url+"'";_63e=_63e.substr(_640.index+_640[0].length);}
_63e=str+_63e;str="";}
while(_640=_644.exec(_63e)){url=_640[1].replace(regexTrim,"$2");if(!_645.exec(url)){url=(new dojo.uri.Uri(URI,url).toString());}
str+=_63e.substring(0,_640.index)+"url("+url+")";_63e=_63e.substr(_640.index+_640[0].length);}
return str+_63e;};dojo.html.setActiveStyleSheet=function(_647){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")){a.disabled=true;if(a.getAttribute("title")==_647){a.disabled=false;}}}};dojo.html.getActiveStyleSheet=function(){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")&&!a.disabled){return a.getAttribute("title");}}
return null;};dojo.html.getPreferredStyleSheet=function(){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("rel").indexOf("alt")==-1&&a.getAttribute("title")){return a.getAttribute("title");}}
return null;};dojo.html.applyBrowserClass=function(node){var drh=dojo.render.html;var _653={dj_ie:drh.ie,dj_ie55:drh.ie55,dj_ie6:drh.ie60,dj_ie7:drh.ie70,dj_iequirks:drh.ie&&drh.quirks,dj_opera:drh.opera,dj_opera8:drh.opera&&(Math.floor(dojo.render.version)==8),dj_opera9:drh.opera&&(Math.floor(dojo.render.version)==9),dj_khtml:drh.khtml,dj_safari:drh.safari,dj_gecko:drh.mozilla};for(var p in _653){if(_653[p]){dojo.html.addClass(node,p);}}};dojo.provide("dojo.widget.DomWidget");dojo.widget._cssFiles={};dojo.widget._cssStrings={};dojo.widget._templateCache={};dojo.widget.defaultStrings={dojoRoot:dojo.hostenv.getBaseScriptUri(),baseScriptUri:dojo.hostenv.getBaseScriptUri()};dojo.widget.fillFromTemplateCache=function(obj,_656,_657,_658){var _659=_656||obj.templatePath;var _65a=dojo.widget._templateCache;if(!_659&&!obj["widgetType"]){do{var _65b="__dummyTemplate__"+dojo.widget._templateCache.dummyCount++;}while(_65a[_65b]);obj.widgetType=_65b;}
var wt=_659?_659.toString():obj.widgetType;var ts=_65a[wt];if(!ts){_65a[wt]={"string":null,"node":null};if(_658){ts={};}else{ts=_65a[wt];}}
if((!obj.templateString)&&(!_658)){obj.templateString=_657||ts["string"];}
if((!obj.templateNode)&&(!_658)){obj.templateNode=ts["node"];}
if((!obj.templateNode)&&(!obj.templateString)&&(_659)){var _65e=dojo.hostenv.getText(_659);if(_65e){_65e=_65e.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im,"");var _65f=_65e.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);if(_65f){_65e=_65f[1];}}else{_65e="";}
obj.templateString=_65e;if(!_658){_65a[wt]["string"]=_65e;}}
if((!ts["string"])&&(!_658)){ts.string=obj.templateString;}};dojo.widget._templateCache.dummyCount=0;dojo.widget.attachProperties=["dojoAttachPoint","id"];dojo.widget.eventAttachProperty="dojoAttachEvent";dojo.widget.onBuildProperty="dojoOnBuild";dojo.widget.waiNames=["waiRole","waiState"];dojo.widget.wai={waiRole:{name:"waiRole","namespace":"http://www.w3.org/TR/xhtml2",alias:"x2",prefix:"wairole:"},waiState:{name:"waiState","namespace":"http://www.w3.org/2005/07/aaa",alias:"aaa",prefix:""},setAttr:function(node,ns,attr,_663){if(dojo.render.html.ie){node.setAttribute(this[ns].alias+":"+attr,this[ns].prefix+_663);}else{node.setAttributeNS(this[ns]["namespace"],attr,this[ns].prefix+_663);}},getAttr:function(node,ns,attr){if(dojo.render.html.ie){return node.getAttribute(this[ns].alias+":"+attr);}else{return node.getAttributeNS(this[ns]["namespace"],attr);}},removeAttr:function(node,ns,attr){var _66a=true;if(dojo.render.html.ie){_66a=node.removeAttribute(this[ns].alias+":"+attr);}else{node.removeAttributeNS(this[ns]["namespace"],attr);}
return _66a;}};dojo.widget.attachTemplateNodes=function(_66b,_66c,_66d){var _66e=dojo.dom.ELEMENT_NODE;function trim(str){return str.replace(/^\s+|\s+$/g,"");}
if(!_66b){_66b=_66c.domNode;}
if(_66b.nodeType!=_66e){return;}
var _670=_66b.all||_66b.getElementsByTagName("*");var _671=_66c;for(var x=-1;x<_670.length;x++){var _673=(x==-1)?_66b:_670[x];var _674=[];if(!_66c.widgetsInTemplate||!_673.getAttribute("dojoType")){for(var y=0;y<this.attachProperties.length;y++){var _676=_673.getAttribute(this.attachProperties[y]);if(_676){_674=_676.split(";");for(var z=0;z<_674.length;z++){if(dojo.lang.isArray(_66c[_674[z]])){_66c[_674[z]].push(_673);}else{_66c[_674[z]]=_673;}}
break;}}
var _678=_673.getAttribute(this.eventAttachProperty);if(_678){var evts=_678.split(";");for(var y=0;y<evts.length;y++){if((!evts[y])||(!evts[y].length)){continue;}
var _67a=null;var tevt=trim(evts[y]);if(evts[y].indexOf(":")>=0){var _67c=tevt.split(":");tevt=trim(_67c[0]);_67a=trim(_67c[1]);}
if(!_67a){_67a=tevt;}
var tf=function(){var ntf=new String(_67a);return function(evt){if(_671[ntf]){_671[ntf](dojo.event.browser.fixEvent(evt,this));}};}();dojo.event.browser.addListener(_673,tevt,tf,false,true);}}
for(var y=0;y<_66d.length;y++){var _680=_673.getAttribute(_66d[y]);if((_680)&&(_680.length)){var _67a=null;var _681=_66d[y].substr(4);_67a=trim(_680);var _682=[_67a];if(_67a.indexOf(";")>=0){_682=dojo.lang.map(_67a.split(";"),trim);}
for(var z=0;z<_682.length;z++){if(!_682[z].length){continue;}
var tf=function(){var ntf=new String(_682[z]);return function(evt){if(_671[ntf]){_671[ntf](dojo.event.browser.fixEvent(evt,this));}};}();dojo.event.browser.addListener(_673,_681,tf,false,true);}}}}
var _685=_673.getAttribute(this.templateProperty);if(_685){_66c[_685]=_673;}
dojo.lang.forEach(dojo.widget.waiNames,function(name){var wai=dojo.widget.wai[name];var val=_673.getAttribute(wai.name);if(val){if(val.indexOf("-")==-1){dojo.widget.wai.setAttr(_673,wai.name,"role",val);}else{var _689=val.split("-");dojo.widget.wai.setAttr(_673,wai.name,_689[0],_689[1]);}}},this);var _68a=_673.getAttribute(this.onBuildProperty);if(_68a){eval("var node = baseNode; var widget = targetObj; "+_68a);}}};dojo.widget.getDojoEventsFromStr=function(str){var re=/(dojoOn([a-z]+)(\s?))=/gi;var evts=str?str.match(re)||[]:[];var ret=[];var lem={};for(var x=0;x<evts.length;x++){if(evts[x].length<1){continue;}
var cm=evts[x].replace(/\s/,"");cm=(cm.slice(0,cm.length-1));if(!lem[cm]){lem[cm]=true;ret.push(cm);}}
return ret;};dojo.declare("dojo.widget.DomWidget",dojo.widget.Widget,function(){if((arguments.length>0)&&(typeof arguments[0]=="object")){this.create(arguments[0]);}},{templateNode:null,templateString:null,templateCssString:null,preventClobber:false,domNode:null,containerNode:null,widgetsInTemplate:false,addChild:function(_692,_693,pos,ref,_696){if(!this.isContainer){dojo.debug("dojo.widget.DomWidget.addChild() attempted on non-container widget");return null;}else{if(_696==undefined){_696=this.children.length;}
this.addWidgetAsDirectChild(_692,_693,pos,ref,_696);this.registerChild(_692,_696);}
return _692;},addWidgetAsDirectChild:function(_697,_698,pos,ref,_69b){if((!this.containerNode)&&(!_698)){this.containerNode=this.domNode;}
var cn=(_698)?_698:this.containerNode;if(!pos){pos="after";}
if(!ref){if(!cn){cn=dojo.body();}
ref=cn.lastChild;}
if(!_69b){_69b=0;}
_697.domNode.setAttribute("dojoinsertionindex",_69b);if(!ref){cn.appendChild(_697.domNode);}else{if(pos=="insertAtIndex"){dojo.dom.insertAtIndex(_697.domNode,ref.parentNode,_69b);}else{if((pos=="after")&&(ref===cn.lastChild)){cn.appendChild(_697.domNode);}else{dojo.dom.insertAtPosition(_697.domNode,cn,pos);}}}},registerChild:function(_69d,_69e){_69d.dojoInsertionIndex=_69e;var idx=-1;for(var i=0;i<this.children.length;i++){if(this.children[i].dojoInsertionIndex<=_69e){idx=i;}}
this.children.splice(idx+1,0,_69d);_69d.parent=this;_69d.addedTo(this,idx+1);delete dojo.widget.manager.topWidgets[_69d.widgetId];},removeChild:function(_6a1){dojo.dom.removeNode(_6a1.domNode);return dojo.widget.DomWidget.superclass.removeChild.call(this,_6a1);},getFragNodeRef:function(frag){if(!frag){return null;}
if(!frag[this.getNamespacedType()]){dojo.raise("Error: no frag for widget type "+this.getNamespacedType()+", id "+this.widgetId+" (maybe a widget has set it's type incorrectly)");}
return frag[this.getNamespacedType()]["nodeRef"];},postInitialize:function(args,frag,_6a5){var _6a6=this.getFragNodeRef(frag);if(_6a5&&(_6a5.snarfChildDomOutput||!_6a6)){_6a5.addWidgetAsDirectChild(this,"","insertAtIndex","",args["dojoinsertionindex"],_6a6);}else{if(_6a6){if(this.domNode&&(this.domNode!==_6a6)){dojo.dom.replaceNode(_6a6,this.domNode);}}}
if(_6a5){_6a5.registerChild(this,args.dojoinsertionindex);}else{dojo.widget.manager.topWidgets[this.widgetId]=this;}
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
node.style[dojo.html.toCamelCase(name)]=_6f4;}}};dojo.html.boxSizing={MARGIN_BOX:"margin-box",BORDER_BOX:"border-box",PADDING_BOX:"padding-box",CONTENT_BOX:"content-box"};dojo.html.getAbsolutePosition=dojo.html.abs=function(node,_6f6,_6f7){node=dojo.byId(node);var _6f8=dojo.doc();var ret={x:0,y:0};var bs=dojo.html.boxSizing;if(!_6f7){_6f7=bs.CONTENT_BOX;}
var _6fb=2;var _6fc;switch(_6f7){case bs.MARGIN_BOX:
_6fc=3;break;case bs.BORDER_BOX:
_6fc=2;break;case bs.PADDING_BOX:
default:
_6fc=1;break;case bs.CONTENT_BOX:
_6fc=0;break;}
var h=dojo.render.html;var db=_6f8["body"]||_6f8["documentElement"];if(h.ie){with(node.getBoundingClientRect()){ret.x=left-2;ret.y=top-2;}}else{if(_6f8["getBoxObjectFor"]){_6fb=1;try{var bo=_6f8.getBoxObjectFor(node);ret.x=bo.x-dojo.html.sumAncestorProperties(node,"scrollLeft");ret.y=bo.y-dojo.html.sumAncestorProperties(node,"scrollTop");}
catch(e){}}else{if(node["offsetParent"]){var _700;if((h.safari)&&(node.style.getPropertyValue("position")=="absolute")&&(node.parentNode==db)){_700=db;}else{_700=db.parentNode;}
if(node.parentNode!=db){var nd=node;if(dojo.render.html.opera){nd=db;}
ret.x-=dojo.html.sumAncestorProperties(nd,"scrollLeft");ret.y-=dojo.html.sumAncestorProperties(nd,"scrollTop");}
var _702=node;do{var n=_702["offsetLeft"];if(!h.opera||n>0){ret.x+=isNaN(n)?0:n;}
var m=_702["offsetTop"];ret.y+=isNaN(m)?0:m;_702=_702.offsetParent;}while((_702!=_700)&&(_702!=null));}else{if(node["x"]&&node["y"]){ret.x+=isNaN(node.x)?0:node.x;ret.y+=isNaN(node.y)?0:node.y;}}}}
if(_6f6){var _705=dojo.html.getScroll();ret.y+=_705.top;ret.x+=_705.left;}
var _706=[dojo.html.getPaddingExtent,dojo.html.getBorderExtent,dojo.html.getMarginExtent];if(_6fb>_6fc){for(var i=_6fc;i<_6fb;++i){ret.y+=_706[i](node,"top");ret.x+=_706[i](node,"left");}}else{if(_6fb<_6fc){for(var i=_6fc;i>_6fb;--i){ret.y-=_706[i-1](node,"top");ret.x-=_706[i-1](node,"left");}}}
ret.top=ret.y;ret.left=ret.x;return ret;};dojo.html.isPositionAbsolute=function(node){return (dojo.html.getComputedStyle(node,"position")=="absolute");};dojo.html._sumPixelValues=function(node,_70a,_70b){var _70c=0;for(var x=0;x<_70a.length;x++){_70c+=dojo.html.getPixelValue(node,_70a[x],_70b);}
return _70c;};dojo.html.getMargin=function(node){return {width:dojo.html._sumPixelValues(node,["margin-left","margin-right"],(dojo.html.getComputedStyle(node,"position")=="absolute")),height:dojo.html._sumPixelValues(node,["margin-top","margin-bottom"],(dojo.html.getComputedStyle(node,"position")=="absolute"))};};dojo.html.getBorder=function(node){return {width:dojo.html.getBorderExtent(node,"left")+dojo.html.getBorderExtent(node,"right"),height:dojo.html.getBorderExtent(node,"top")+dojo.html.getBorderExtent(node,"bottom")};};dojo.html.getBorderExtent=function(node,side){return (dojo.html.getStyle(node,"border-"+side+"-style")=="none"?0:dojo.html.getPixelValue(node,"border-"+side+"-width"));};dojo.html.getMarginExtent=function(node,side){return dojo.html._sumPixelValues(node,["margin-"+side],dojo.html.isPositionAbsolute(node));};dojo.html.getPaddingExtent=function(node,side){return dojo.html._sumPixelValues(node,["padding-"+side],true);};dojo.html.getPadding=function(node){return {width:dojo.html._sumPixelValues(node,["padding-left","padding-right"],true),height:dojo.html._sumPixelValues(node,["padding-top","padding-bottom"],true)};};dojo.html.getPadBorder=function(node){var pad=dojo.html.getPadding(node);var _719=dojo.html.getBorder(node);return {width:pad.width+_719.width,height:pad.height+_719.height};};dojo.html.getBoxSizing=function(node){var h=dojo.render.html;var bs=dojo.html.boxSizing;if((h.ie)||(h.opera)){var cm=document["compatMode"];if((cm=="BackCompat")||(cm=="QuirksMode")){return bs.BORDER_BOX;}else{return bs.CONTENT_BOX;}}else{if(arguments.length==0){node=document.documentElement;}
var _71e=dojo.html.getStyle(node,"-moz-box-sizing");if(!_71e){_71e=dojo.html.getStyle(node,"box-sizing");}
return (_71e?_71e:bs.CONTENT_BOX);}};dojo.html.isBorderBox=function(node){return (dojo.html.getBoxSizing(node)==dojo.html.boxSizing.BORDER_BOX);};dojo.html.getBorderBox=function(node){node=dojo.byId(node);return {width:node.offsetWidth,height:node.offsetHeight};};dojo.html.getPaddingBox=function(node){var box=dojo.html.getBorderBox(node);var _723=dojo.html.getBorder(node);return {width:box.width-_723.width,height:box.height-_723.height};};dojo.html.getContentBox=function(node){node=dojo.byId(node);var _725=dojo.html.getPadBorder(node);return {width:node.offsetWidth-_725.width,height:node.offsetHeight-_725.height};};dojo.html.setContentBox=function(node,args){node=dojo.byId(node);var _728=0;var _729=0;var isbb=dojo.html.isBorderBox(node);var _72b=(isbb?dojo.html.getPadBorder(node):{width:0,height:0});var ret={};if(typeof args.width!="undefined"){_728=args.width+_72b.width;ret.width=dojo.html.setPositivePixelValue(node,"width",_728);}
if(typeof args.height!="undefined"){_729=args.height+_72b.height;ret.height=dojo.html.setPositivePixelValue(node,"height",_729);}
return ret;};dojo.html.getMarginBox=function(node){var _72e=dojo.html.getBorderBox(node);var _72f=dojo.html.getMargin(node);return {width:_72e.width+_72f.width,height:_72e.height+_72f.height};};dojo.html.setMarginBox=function(node,args){node=dojo.byId(node);var _732=0;var _733=0;var isbb=dojo.html.isBorderBox(node);var _735=(!isbb?dojo.html.getPadBorder(node):{width:0,height:0});var _736=dojo.html.getMargin(node);var ret={};if(typeof args.width!="undefined"){_732=args.width-_735.width;_732-=_736.width;ret.width=dojo.html.setPositivePixelValue(node,"width",_732);}
if(typeof args.height!="undefined"){_733=args.height-_735.height;_733-=_736.height;ret.height=dojo.html.setPositivePixelValue(node,"height",_733);}
return ret;};dojo.html.getElementBox=function(node,type){var bs=dojo.html.boxSizing;switch(type){case bs.MARGIN_BOX:
return dojo.html.getMarginBox(node);case bs.BORDER_BOX:
return dojo.html.getBorderBox(node);case bs.PADDING_BOX:
return dojo.html.getPaddingBox(node);case bs.CONTENT_BOX:
default:
return dojo.html.getContentBox(node);}};dojo.html.toCoordinateObject=dojo.html.toCoordinateArray=function(_73b,_73c,_73d){if(!_73b.nodeType&&!(_73b instanceof String||typeof _73b=="string")&&("width" in _73b||"height" in _73b||"left" in _73b||"x" in _73b||"top" in _73b||"y" in _73b)){var ret={left:_73b.left||_73b.x||0,top:_73b.top||_73b.y||0,width:_73b.width||0,height:_73b.height||0};}else{var node=dojo.byId(_73b);var pos=dojo.html.abs(node,_73c,_73d);var _741=dojo.html.getMarginBox(node);var ret={left:pos.left,top:pos.top,width:_741.width,height:_741.height};}
ret.x=ret.left;ret.y=ret.top;return ret;};dojo.html.setMarginBoxWidth=dojo.html.setOuterWidth=function(node,_743){return dojo.html._callDeprecated("setMarginBoxWidth","setMarginBox",arguments,"width");};dojo.html.setMarginBoxHeight=dojo.html.setOuterHeight=function(){return dojo.html._callDeprecated("setMarginBoxHeight","setMarginBox",arguments,"height");};dojo.html.getMarginBoxWidth=dojo.html.getOuterWidth=function(){return dojo.html._callDeprecated("getMarginBoxWidth","getMarginBox",arguments,null,"width");};dojo.html.getMarginBoxHeight=dojo.html.getOuterHeight=function(){return dojo.html._callDeprecated("getMarginBoxHeight","getMarginBox",arguments,null,"height");};dojo.html.getTotalOffset=function(node,type,_746){return dojo.html._callDeprecated("getTotalOffset","getAbsolutePosition",arguments,null,type);};dojo.html.getAbsoluteX=function(node,_748){return dojo.html._callDeprecated("getAbsoluteX","getAbsolutePosition",arguments,null,"x");};dojo.html.getAbsoluteY=function(node,_74a){return dojo.html._callDeprecated("getAbsoluteY","getAbsolutePosition",arguments,null,"y");};dojo.html.totalOffsetLeft=function(node,_74c){return dojo.html._callDeprecated("totalOffsetLeft","getAbsolutePosition",arguments,null,"left");};dojo.html.totalOffsetTop=function(node,_74e){return dojo.html._callDeprecated("totalOffsetTop","getAbsolutePosition",arguments,null,"top");};dojo.html.getMarginWidth=function(node){return dojo.html._callDeprecated("getMarginWidth","getMargin",arguments,null,"width");};dojo.html.getMarginHeight=function(node){return dojo.html._callDeprecated("getMarginHeight","getMargin",arguments,null,"height");};dojo.html.getBorderWidth=function(node){return dojo.html._callDeprecated("getBorderWidth","getBorder",arguments,null,"width");};dojo.html.getBorderHeight=function(node){return dojo.html._callDeprecated("getBorderHeight","getBorder",arguments,null,"height");};dojo.html.getPaddingWidth=function(node){return dojo.html._callDeprecated("getPaddingWidth","getPadding",arguments,null,"width");};dojo.html.getPaddingHeight=function(node){return dojo.html._callDeprecated("getPaddingHeight","getPadding",arguments,null,"height");};dojo.html.getPadBorderWidth=function(node){return dojo.html._callDeprecated("getPadBorderWidth","getPadBorder",arguments,null,"width");};dojo.html.getPadBorderHeight=function(node){return dojo.html._callDeprecated("getPadBorderHeight","getPadBorder",arguments,null,"height");};dojo.html.getBorderBoxWidth=dojo.html.getInnerWidth=function(){return dojo.html._callDeprecated("getBorderBoxWidth","getBorderBox",arguments,null,"width");};dojo.html.getBorderBoxHeight=dojo.html.getInnerHeight=function(){return dojo.html._callDeprecated("getBorderBoxHeight","getBorderBox",arguments,null,"height");};dojo.html.getContentBoxWidth=dojo.html.getContentWidth=function(){return dojo.html._callDeprecated("getContentBoxWidth","getContentBox",arguments,null,"width");};dojo.html.getContentBoxHeight=dojo.html.getContentHeight=function(){return dojo.html._callDeprecated("getContentBoxHeight","getContentBox",arguments,null,"height");};dojo.html.setContentBoxWidth=dojo.html.setContentWidth=function(node,_758){return dojo.html._callDeprecated("setContentBoxWidth","setContentBox",arguments,"width");};dojo.html.setContentBoxHeight=dojo.html.setContentHeight=function(node,_75a){return dojo.html._callDeprecated("setContentBoxHeight","setContentBox",arguments,"height");};dojo.provide("dojo.html.util");dojo.html.getElementWindow=function(_75b){return dojo.html.getDocumentWindow(_75b.ownerDocument);};dojo.html.getDocumentWindow=function(doc){if(dojo.render.html.safari&&!doc._parentWindow){var fix=function(win){win.document._parentWindow=win;for(var i=0;i<win.frames.length;i++){fix(win.frames[i]);}};fix(window.top);}
if(dojo.render.html.ie&&window!==document.parentWindow&&!doc._parentWindow){doc.parentWindow.execScript("document._parentWindow = window;","Javascript");var win=doc._parentWindow;doc._parentWindow=null;return win;}
return doc._parentWindow||doc.parentWindow||doc.defaultView;};dojo.html.getAbsolutePositionExt=function(node,_762,_763,_764){var _765=dojo.html.getElementWindow(node);var ret=dojo.withGlobal(_765,"getAbsolutePosition",dojo.html,arguments);var win=dojo.html.getElementWindow(node);if(_764!=win&&win.frameElement){var ext=dojo.html.getAbsolutePositionExt(win.frameElement,_762,_763,_764);ret.x+=ext.x;ret.y+=ext.y;}
ret.top=ret.y;ret.left=ret.x;return ret;};dojo.html.gravity=function(node,e){node=dojo.byId(node);var _76b=dojo.html.getCursorPosition(e);with(dojo.html){var _76c=getAbsolutePosition(node,true);var bb=getBorderBox(node);var _76e=_76c.x+(bb.width/2);var _76f=_76c.y+(bb.height/2);}
with(dojo.html.gravity){return ((_76b.x<_76e?WEST:EAST)|(_76b.y<_76f?NORTH:SOUTH));}};dojo.html.gravity.NORTH=1;dojo.html.gravity.SOUTH=1<<1;dojo.html.gravity.EAST=1<<2;dojo.html.gravity.WEST=1<<3;dojo.html.overElement=function(_770,e){_770=dojo.byId(_770);var _772=dojo.html.getCursorPosition(e);var bb=dojo.html.getBorderBox(_770);var _774=dojo.html.getAbsolutePosition(_770,true,dojo.html.boxSizing.BORDER_BOX);var top=_774.y;var _776=top+bb.height;var left=_774.x;var _778=left+bb.width;return (_772.x>=left&&_772.x<=_778&&_772.y>=top&&_772.y<=_776);};dojo.html.renderedTextContent=function(node){node=dojo.byId(node);var _77a="";if(node==null){return _77a;}
for(var i=0;i<node.childNodes.length;i++){switch(node.childNodes[i].nodeType){case 1:
case 5:
var _77c="unknown";try{_77c=dojo.html.getStyle(node.childNodes[i],"display");}
catch(E){}
switch(_77c){case "block":
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
_77a+="\n";_77a+=dojo.html.renderedTextContent(node.childNodes[i]);_77a+="\n";break;case "none":
break;default:
if(node.childNodes[i].tagName&&node.childNodes[i].tagName.toLowerCase()=="br"){_77a+="\n";}else{_77a+=dojo.html.renderedTextContent(node.childNodes[i]);}
break;}
break;case 3:
case 2:
case 4:
var text=node.childNodes[i].nodeValue;var _77e="unknown";try{_77e=dojo.html.getStyle(node,"text-transform");}
catch(E){}
switch(_77e){case "capitalize":
var _77f=text.split(" ");for(var i=0;i<_77f.length;i++){_77f[i]=_77f[i].charAt(0).toUpperCase()+_77f[i].substring(1);}
text=_77f.join(" ");break;case "uppercase":
text=text.toUpperCase();break;case "lowercase":
text=text.toLowerCase();break;default:
break;}
switch(_77e){case "nowrap":
break;case "pre-wrap":
break;case "pre-line":
break;case "pre":
break;default:
text=text.replace(/\s+/," ");if(/\s$/.test(_77a)){text.replace(/^\s/,"");}
break;}
_77a+=text;break;default:
break;}}
return _77a;};dojo.html.createNodesFromText=function(txt,trim){if(trim){txt=txt.replace(/^\s+|\s+$/g,"");}
var tn=dojo.doc().createElement("div");tn.style.visibility="hidden";dojo.body().appendChild(tn);var _783="none";if((/^<t[dh][\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table><tbody><tr>"+txt+"</tr></tbody></table>";_783="cell";}else{if((/^<tr[\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table><tbody>"+txt+"</tbody></table>";_783="row";}else{if((/^<(thead|tbody|tfoot)[\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table>"+txt+"</table>";_783="section";}}}
tn.innerHTML=txt;if(tn["normalize"]){tn.normalize();}
var _784=null;switch(_783){case "cell":
_784=tn.getElementsByTagName("tr")[0];break;case "row":
_784=tn.getElementsByTagName("tbody")[0];break;case "section":
_784=tn.getElementsByTagName("table")[0];break;default:
_784=tn;break;}
var _785=[];for(var x=0;x<_784.childNodes.length;x++){_785.push(_784.childNodes[x].cloneNode(true));}
tn.style.display="none";dojo.dom.removeNode(tn);return _785;};dojo.html.placeOnScreen=function(node,_788,_789,_78a,_78b,_78c,_78d){if(_788 instanceof Array||typeof _788=="array"){_78d=_78c;_78c=_78b;_78b=_78a;_78a=_789;_789=_788[1];_788=_788[0];}
if(_78c instanceof String||typeof _78c=="string"){_78c=_78c.split(",");}
if(!isNaN(_78a)){_78a=[Number(_78a),Number(_78a)];}else{if(!(_78a instanceof Array||typeof _78a=="array")){_78a=[0,0];}}
var _78e=dojo.html.getScroll().offset;var view=dojo.html.getViewport();node=dojo.byId(node);var _790=node.style.display;node.style.display="";var bb=dojo.html.getBorderBox(node);var w=bb.width;var h=bb.height;node.style.display=_790;if(!(_78c instanceof Array||typeof _78c=="array")){_78c=["TL"];}
var _794,_795,_796=Infinity,_797;for(var _798=0;_798<_78c.length;++_798){var _799=_78c[_798];var _79a=true;var tryX=_788-(_799.charAt(1)=="L"?0:w)+_78a[0]*(_799.charAt(1)=="L"?1:-1);var tryY=_789-(_799.charAt(0)=="T"?0:h)+_78a[1]*(_799.charAt(0)=="T"?1:-1);if(_78b){tryX-=_78e.x;tryY-=_78e.y;}
if(tryX<0){tryX=0;_79a=false;}
if(tryY<0){tryY=0;_79a=false;}
var x=tryX+w;if(x>view.width){x=view.width-w;_79a=false;}else{x=tryX;}
x=Math.max(_78a[0],x)+_78e.x;var y=tryY+h;if(y>view.height){y=view.height-h;_79a=false;}else{y=tryY;}
y=Math.max(_78a[1],y)+_78e.y;if(_79a){_794=x;_795=y;_796=0;_797=_799;break;}else{var dist=Math.pow(x-tryX-_78e.x,2)+Math.pow(y-tryY-_78e.y,2);if(_796>dist){_796=dist;_794=x;_795=y;_797=_799;}}}
if(!_78d){node.style.left=_794+"px";node.style.top=_795+"px";}
return {left:_794,top:_795,x:_794,y:_795,dist:_796,corner:_797};};dojo.html.placeOnScreenAroundElement=function(node,_7a1,_7a2,_7a3,_7a4,_7a5){var best,_7a7=Infinity;_7a1=dojo.byId(_7a1);var _7a8=_7a1.style.display;_7a1.style.display="";var mb=dojo.html.getElementBox(_7a1,_7a3);var _7aa=mb.width;var _7ab=mb.height;var _7ac=dojo.html.getAbsolutePosition(_7a1,true,_7a3);_7a1.style.display=_7a8;for(var _7ad in _7a4){var pos,_7af,_7b0;var _7b1=_7a4[_7ad];_7af=_7ac.x+(_7ad.charAt(1)=="L"?0:_7aa);_7b0=_7ac.y+(_7ad.charAt(0)=="T"?0:_7ab);pos=dojo.html.placeOnScreen(node,_7af,_7b0,_7a2,true,_7b1,true);if(pos.dist==0){best=pos;break;}else{if(_7a7>pos.dist){_7a7=pos.dist;best=pos;}}}
if(!_7a5){node.style.left=best.left+"px";node.style.top=best.top+"px";}
return best;};dojo.html.scrollIntoView=function(node){if(!node){return;}
if(dojo.render.html.ie){if(dojo.html.getBorderBox(node.parentNode).height<=node.parentNode.scrollHeight){node.scrollIntoView(false);}}else{if(dojo.render.html.mozilla){node.scrollIntoView(false);}else{var _7b3=node.parentNode;var _7b4=_7b3.scrollTop+dojo.html.getBorderBox(_7b3).height;var _7b5=node.offsetTop+dojo.html.getMarginBox(node).height;if(_7b4<_7b5){_7b3.scrollTop+=(_7b5-_7b4);}else{if(_7b3.scrollTop>node.offsetTop){_7b3.scrollTop-=(_7b3.scrollTop-node.offsetTop);}}}}};dojo.provide("dojo.gfx.color");dojo.gfx.color.Color=function(r,g,b,a){if(dojo.lang.isArray(r)){this.r=r[0];this.g=r[1];this.b=r[2];this.a=r[3]||1;}else{if(dojo.lang.isString(r)){var rgb=dojo.gfx.color.extractRGB(r);this.r=rgb[0];this.g=rgb[1];this.b=rgb[2];this.a=g||1;}else{if(r instanceof dojo.gfx.color.Color){this.r=r.r;this.b=r.b;this.g=r.g;this.a=r.a;}else{this.r=r;this.g=g;this.b=b;this.a=a;}}}};dojo.gfx.color.Color.fromArray=function(arr){return new dojo.gfx.color.Color(arr[0],arr[1],arr[2],arr[3]);};dojo.extend(dojo.gfx.color.Color,{toRgb:function(_7bc){if(_7bc){return this.toRgba();}else{return [this.r,this.g,this.b];}},toRgba:function(){return [this.r,this.g,this.b,this.a];},toHex:function(){return dojo.gfx.color.rgb2hex(this.toRgb());},toCss:function(){return "rgb("+this.toRgb().join()+")";},toString:function(){return this.toHex();},blend:function(_7bd,_7be){var rgb=null;if(dojo.lang.isArray(_7bd)){rgb=_7bd;}else{if(_7bd instanceof dojo.gfx.color.Color){rgb=_7bd.toRgb();}else{rgb=new dojo.gfx.color.Color(_7bd).toRgb();}}
return dojo.gfx.color.blend(this.toRgb(),rgb,_7be);}});dojo.gfx.color.named={white:[255,255,255],black:[0,0,0],red:[255,0,0],green:[0,255,0],lime:[0,255,0],blue:[0,0,255],navy:[0,0,128],gray:[128,128,128],silver:[192,192,192]};dojo.gfx.color.blend=function(a,b,_7c2){if(typeof a=="string"){return dojo.gfx.color.blendHex(a,b,_7c2);}
if(!_7c2){_7c2=0;}
_7c2=Math.min(Math.max(-1,_7c2),1);_7c2=((_7c2+1)/2);var c=[];for(var x=0;x<3;x++){c[x]=parseInt(b[x]+((a[x]-b[x])*_7c2));}
return c;};dojo.gfx.color.blendHex=function(a,b,_7c7){return dojo.gfx.color.rgb2hex(dojo.gfx.color.blend(dojo.gfx.color.hex2rgb(a),dojo.gfx.color.hex2rgb(b),_7c7));};dojo.gfx.color.extractRGB=function(_7c8){var hex="0123456789abcdef";_7c8=_7c8.toLowerCase();if(_7c8.indexOf("rgb")==0){var _7ca=_7c8.match(/rgba*\((\d+), *(\d+), *(\d+)/i);var ret=_7ca.splice(1,3);return ret;}else{var _7cc=dojo.gfx.color.hex2rgb(_7c8);if(_7cc){return _7cc;}else{return dojo.gfx.color.named[_7c8]||[255,255,255];}}};dojo.gfx.color.hex2rgb=function(hex){var _7ce="0123456789ABCDEF";var rgb=new Array(3);if(hex.indexOf("#")==0){hex=hex.substring(1);}
hex=hex.toUpperCase();if(hex.replace(new RegExp("["+_7ce+"]","g"),"")!=""){return null;}
if(hex.length==3){rgb[0]=hex.charAt(0)+hex.charAt(0);rgb[1]=hex.charAt(1)+hex.charAt(1);rgb[2]=hex.charAt(2)+hex.charAt(2);}else{rgb[0]=hex.substring(0,2);rgb[1]=hex.substring(2,4);rgb[2]=hex.substring(4);}
for(var i=0;i<rgb.length;i++){rgb[i]=_7ce.indexOf(rgb[i].charAt(0))*16+_7ce.indexOf(rgb[i].charAt(1));}
return rgb;};dojo.gfx.color.rgb2hex=function(r,g,b){if(dojo.lang.isArray(r)){g=r[1]||0;b=r[2]||0;r=r[0]||0;}
var ret=dojo.lang.map([r,g,b],function(x){x=new Number(x);var s=x.toString(16);while(s.length<2){s="0"+s;}
return s;});ret.unshift("#");return ret.join("");};dojo.provide("dojo.lfx.Animation");dojo.lfx.Line=function(_7d7,end){this.start=_7d7;this.end=end;if(dojo.lang.isArray(_7d7)){var diff=[];dojo.lang.forEach(this.start,function(s,i){diff[i]=this.end[i]-s;},this);this.getValue=function(n){var res=[];dojo.lang.forEach(this.start,function(s,i){res[i]=(diff[i]*n)+s;},this);return res;};}else{var diff=end-_7d7;this.getValue=function(n){return (diff*n)+this.start;};}};dojo.lfx.easeDefault=function(n){if(dojo.render.html.khtml){return (parseFloat("0.5")+((Math.sin((n+parseFloat("1.5"))*Math.PI))/2));}else{return (0.5+((Math.sin((n+1.5)*Math.PI))/2));}};dojo.lfx.easeIn=function(n){return Math.pow(n,3);};dojo.lfx.easeOut=function(n){return (1-Math.pow(1-n,3));};dojo.lfx.easeInOut=function(n){return ((3*Math.pow(n,2))-(2*Math.pow(n,3)));};dojo.lfx.IAnimation=function(){};dojo.lang.extend(dojo.lfx.IAnimation,{curve:null,duration:1000,easing:null,repeatCount:0,rate:25,handler:null,beforeBegin:null,onBegin:null,onAnimate:null,onEnd:null,onPlay:null,onPause:null,onStop:null,play:null,pause:null,stop:null,connect:function(evt,_7e6,_7e7){if(!_7e7){_7e7=_7e6;_7e6=this;}
_7e7=dojo.lang.hitch(_7e6,_7e7);var _7e8=this[evt]||function(){};this[evt]=function(){var ret=_7e8.apply(this,arguments);_7e7.apply(this,arguments);return ret;};return this;},fire:function(evt,args){if(this[evt]){this[evt].apply(this,(args||[]));}
return this;},repeat:function(_7ec){this.repeatCount=_7ec;return this;},_active:false,_paused:false});dojo.lfx.Animation=function(_7ed,_7ee,_7ef,_7f0,_7f1,rate){dojo.lfx.IAnimation.call(this);if(dojo.lang.isNumber(_7ed)||(!_7ed&&_7ee.getValue)){rate=_7f1;_7f1=_7f0;_7f0=_7ef;_7ef=_7ee;_7ee=_7ed;_7ed=null;}else{if(_7ed.getValue||dojo.lang.isArray(_7ed)){rate=_7f0;_7f1=_7ef;_7f0=_7ee;_7ef=_7ed;_7ee=null;_7ed=null;}}
if(dojo.lang.isArray(_7ef)){this.curve=new dojo.lfx.Line(_7ef[0],_7ef[1]);}else{this.curve=_7ef;}
if(_7ee!=null&&_7ee>0){this.duration=_7ee;}
if(_7f1){this.repeatCount=_7f1;}
if(rate){this.rate=rate;}
if(_7ed){dojo.lang.forEach(["handler","beforeBegin","onBegin","onEnd","onPlay","onStop","onAnimate"],function(item){if(_7ed[item]){this.connect(item,_7ed[item]);}},this);}
if(_7f0&&dojo.lang.isFunction(_7f0)){this.easing=_7f0;}};dojo.inherits(dojo.lfx.Animation,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Animation,{_startTime:null,_endTime:null,_timer:null,_percent:0,_startRepeatCount:0,play:function(_7f4,_7f5){if(_7f5){clearTimeout(this._timer);this._active=false;this._paused=false;this._percent=0;}else{if(this._active&&!this._paused){return this;}}
this.fire("handler",["beforeBegin"]);this.fire("beforeBegin");if(_7f4>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_7f5);}),_7f4);return this;}
this._startTime=new Date().valueOf();if(this._paused){this._startTime-=(this.duration*this._percent/100);}
this._endTime=this._startTime+this.duration;this._active=true;this._paused=false;var step=this._percent/100;var _7f7=this.curve.getValue(step);if(this._percent==0){if(!this._startRepeatCount){this._startRepeatCount=this.repeatCount;}
this.fire("handler",["begin",_7f7]);this.fire("onBegin",[_7f7]);}
this.fire("handler",["play",_7f7]);this.fire("onPlay",[_7f7]);this._cycle();return this;},pause:function(){clearTimeout(this._timer);if(!this._active){return this;}
this._paused=true;var _7f8=this.curve.getValue(this._percent/100);this.fire("handler",["pause",_7f8]);this.fire("onPause",[_7f8]);return this;},gotoPercent:function(pct,_7fa){clearTimeout(this._timer);this._active=true;this._paused=true;this._percent=pct;if(_7fa){this.play();}
return this;},stop:function(_7fb){clearTimeout(this._timer);var step=this._percent/100;if(_7fb){step=1;}
var _7fd=this.curve.getValue(step);this.fire("handler",["stop",_7fd]);this.fire("onStop",[_7fd]);this._active=false;this._paused=false;return this;},status:function(){if(this._active){return this._paused?"paused":"playing";}else{return "stopped";}
return this;},_cycle:function(){clearTimeout(this._timer);if(this._active){var curr=new Date().valueOf();var step=(curr-this._startTime)/(this._endTime-this._startTime);if(step>=1){step=1;this._percent=100;}else{this._percent=step*100;}
if((this.easing)&&(dojo.lang.isFunction(this.easing))){step=this.easing(step);}
var _800=this.curve.getValue(step);this.fire("handler",["animate",_800]);this.fire("onAnimate",[_800]);if(step<1){this._timer=setTimeout(dojo.lang.hitch(this,"_cycle"),this.rate);}else{this._active=false;this.fire("handler",["end"]);this.fire("onEnd");if(this.repeatCount>0){this.repeatCount--;this.play(null,true);}else{if(this.repeatCount==-1){this.play(null,true);}else{if(this._startRepeatCount){this.repeatCount=this._startRepeatCount;this._startRepeatCount=0;}}}}}
return this;}});dojo.lfx.Combine=function(_801){dojo.lfx.IAnimation.call(this);this._anims=[];this._animsEnded=0;var _802=arguments;if(_802.length==1&&(dojo.lang.isArray(_802[0])||dojo.lang.isArrayLike(_802[0]))){_802=_802[0];}
dojo.lang.forEach(_802,function(anim){this._anims.push(anim);anim.connect("onEnd",dojo.lang.hitch(this,"_onAnimsEnded"));},this);};dojo.inherits(dojo.lfx.Combine,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Combine,{_animsEnded:0,play:function(_804,_805){if(!this._anims.length){return this;}
this.fire("beforeBegin");if(_804>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_805);}),_804);return this;}
if(_805||this._anims[0].percent==0){this.fire("onBegin");}
this.fire("onPlay");this._animsCall("play",null,_805);return this;},pause:function(){this.fire("onPause");this._animsCall("pause");return this;},stop:function(_806){this.fire("onStop");this._animsCall("stop",_806);return this;},_onAnimsEnded:function(){this._animsEnded++;if(this._animsEnded>=this._anims.length){this.fire("onEnd");}
return this;},_animsCall:function(_807){var args=[];if(arguments.length>1){for(var i=1;i<arguments.length;i++){args.push(arguments[i]);}}
var _80a=this;dojo.lang.forEach(this._anims,function(anim){anim[_807](args);},_80a);return this;}});dojo.lfx.Chain=function(_80c){dojo.lfx.IAnimation.call(this);this._anims=[];this._currAnim=-1;var _80d=arguments;if(_80d.length==1&&(dojo.lang.isArray(_80d[0])||dojo.lang.isArrayLike(_80d[0]))){_80d=_80d[0];}
var _80e=this;dojo.lang.forEach(_80d,function(anim,i,_811){this._anims.push(anim);if(i<_811.length-1){anim.connect("onEnd",dojo.lang.hitch(this,"_playNext"));}else{anim.connect("onEnd",dojo.lang.hitch(this,function(){this.fire("onEnd");}));}},this);};dojo.inherits(dojo.lfx.Chain,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Chain,{_currAnim:-1,play:function(_812,_813){if(!this._anims.length){return this;}
if(_813||!this._anims[this._currAnim]){this._currAnim=0;}
var _814=this._anims[this._currAnim];this.fire("beforeBegin");if(_812>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_813);}),_812);return this;}
if(_814){if(this._currAnim==0){this.fire("handler",["begin",this._currAnim]);this.fire("onBegin",[this._currAnim]);}
this.fire("onPlay",[this._currAnim]);_814.play(null,_813);}
return this;},pause:function(){if(this._anims[this._currAnim]){this._anims[this._currAnim].pause();this.fire("onPause",[this._currAnim]);}
return this;},playPause:function(){if(this._anims.length==0){return this;}
if(this._currAnim==-1){this._currAnim=0;}
var _815=this._anims[this._currAnim];if(_815){if(!_815._active||_815._paused){this.play();}else{this.pause();}}
return this;},stop:function(){var _816=this._anims[this._currAnim];if(_816){_816.stop();this.fire("onStop",[this._currAnim]);}
return _816;},_playNext:function(){if(this._currAnim==-1||this._anims.length==0){return this;}
this._currAnim++;if(this._anims[this._currAnim]){this._anims[this._currAnim].play(null,true);}
return this;}});dojo.lfx.combine=function(_817){var _818=arguments;if(dojo.lang.isArray(arguments[0])){_818=arguments[0];}
if(_818.length==1){return _818[0];}
return new dojo.lfx.Combine(_818);};dojo.lfx.chain=function(_819){var _81a=arguments;if(dojo.lang.isArray(arguments[0])){_81a=arguments[0];}
if(_81a.length==1){return _81a[0];}
return new dojo.lfx.Chain(_81a);};dojo.provide("dojo.html.color");dojo.html.getBackgroundColor=function(node){node=dojo.byId(node);var _81c;do{_81c=dojo.html.getStyle(node,"background-color");if(_81c.toLowerCase()=="rgba(0, 0, 0, 0)"){_81c="transparent";}
if(node==document.getElementsByTagName("body")[0]){node=null;break;}
node=node.parentNode;}while(node&&dojo.lang.inArray(["transparent",""],_81c));if(_81c=="transparent"){_81c=[255,255,255,0];}else{_81c=dojo.gfx.color.extractRGB(_81c);}
return _81c;};dojo.provide("dojo.lfx.html");dojo.lfx.html._byId=function(_81d){if(!_81d){return [];}
if(dojo.lang.isArrayLike(_81d)){if(!_81d.alreadyChecked){var n=[];dojo.lang.forEach(_81d,function(node){n.push(dojo.byId(node));});n.alreadyChecked=true;return n;}else{return _81d;}}else{var n=[];n.push(dojo.byId(_81d));n.alreadyChecked=true;return n;}};dojo.lfx.html.propertyAnimation=function(_820,_821,_822,_823,_824){_820=dojo.lfx.html._byId(_820);var _825={"propertyMap":_821,"nodes":_820,"duration":_822,"easing":_823||dojo.lfx.easeDefault};var _826=function(args){if(args.nodes.length==1){var pm=args.propertyMap;if(!dojo.lang.isArray(args.propertyMap)){var parr=[];for(var _82a in pm){pm[_82a].property=_82a;parr.push(pm[_82a]);}
pm=args.propertyMap=parr;}
dojo.lang.forEach(pm,function(prop){if(dj_undef("start",prop)){if(prop.property!="opacity"){prop.start=parseInt(dojo.html.getComputedStyle(args.nodes[0],prop.property));}else{prop.start=dojo.html.getOpacity(args.nodes[0]);}}});}};var _82c=function(_82d){var _82e=[];dojo.lang.forEach(_82d,function(c){_82e.push(Math.round(c));});return _82e;};var _830=function(n,_832){n=dojo.byId(n);if(!n||!n.style){return;}
for(var s in _832){try{if(s=="opacity"){dojo.html.setOpacity(n,_832[s]);}else{n.style[s]=_832[s];}}
catch(e){dojo.debug(e);}}};var _834=function(_835){this._properties=_835;this.diffs=new Array(_835.length);dojo.lang.forEach(_835,function(prop,i){if(dojo.lang.isFunction(prop.start)){prop.start=prop.start(prop,i);}
if(dojo.lang.isFunction(prop.end)){prop.end=prop.end(prop,i);}
if(dojo.lang.isArray(prop.start)){this.diffs[i]=null;}else{if(prop.start instanceof dojo.gfx.color.Color){prop.startRgb=prop.start.toRgb();prop.endRgb=prop.end.toRgb();}else{this.diffs[i]=prop.end-prop.start;}}},this);this.getValue=function(n){var ret={};dojo.lang.forEach(this._properties,function(prop,i){var _83c=null;if(dojo.lang.isArray(prop.start)){}else{if(prop.start instanceof dojo.gfx.color.Color){_83c=(prop.units||"rgb")+"(";for(var j=0;j<prop.startRgb.length;j++){_83c+=Math.round(((prop.endRgb[j]-prop.startRgb[j])*n)+prop.startRgb[j])+(j<prop.startRgb.length-1?",":"");}
_83c+=")";}else{_83c=((this.diffs[i])*n)+prop.start+(prop.property!="opacity"?prop.units||"px":"");}}
ret[dojo.html.toCamelCase(prop.property)]=_83c;},this);return ret;};};var anim=new dojo.lfx.Animation({beforeBegin:function(){_826(_825);anim.curve=new _834(_825.propertyMap);},onAnimate:function(_83f){dojo.lang.forEach(_825.nodes,function(node){_830(node,_83f);});}},_825.duration,null,_825.easing);if(_824){for(var x in _824){if(dojo.lang.isFunction(_824[x])){anim.connect(x,anim,_824[x]);}}}
return anim;};dojo.lfx.html._makeFadeable=function(_842){var _843=function(node){if(dojo.render.html.ie){if((node.style.zoom.length==0)&&(dojo.html.getStyle(node,"zoom")=="normal")){node.style.zoom="1";}
if((node.style.width.length==0)&&(dojo.html.getStyle(node,"width")=="auto")){node.style.width="auto";}}};if(dojo.lang.isArrayLike(_842)){dojo.lang.forEach(_842,_843);}else{_843(_842);}};dojo.lfx.html.fade=function(_845,_846,_847,_848,_849){_845=dojo.lfx.html._byId(_845);var _84a={property:"opacity"};if(!dj_undef("start",_846)){_84a.start=_846.start;}else{_84a.start=function(){return dojo.html.getOpacity(_845[0]);};}
if(!dj_undef("end",_846)){_84a.end=_846.end;}else{dojo.raise("dojo.lfx.html.fade needs an end value");}
var anim=dojo.lfx.propertyAnimation(_845,[_84a],_847,_848);anim.connect("beforeBegin",function(){dojo.lfx.html._makeFadeable(_845);});if(_849){anim.connect("onEnd",function(){_849(_845,anim);});}
return anim;};dojo.lfx.html.fadeIn=function(_84c,_84d,_84e,_84f){return dojo.lfx.html.fade(_84c,{end:1},_84d,_84e,_84f);};dojo.lfx.html.fadeOut=function(_850,_851,_852,_853){return dojo.lfx.html.fade(_850,{end:0},_851,_852,_853);};dojo.lfx.html.fadeShow=function(_854,_855,_856,_857){_854=dojo.lfx.html._byId(_854);dojo.lang.forEach(_854,function(node){dojo.html.setOpacity(node,0);});var anim=dojo.lfx.html.fadeIn(_854,_855,_856,_857);anim.connect("beforeBegin",function(){if(dojo.lang.isArrayLike(_854)){dojo.lang.forEach(_854,dojo.html.show);}else{dojo.html.show(_854);}});return anim;};dojo.lfx.html.fadeHide=function(_85a,_85b,_85c,_85d){var anim=dojo.lfx.html.fadeOut(_85a,_85b,_85c,function(){if(dojo.lang.isArrayLike(_85a)){dojo.lang.forEach(_85a,dojo.html.hide);}else{dojo.html.hide(_85a);}
if(_85d){_85d(_85a,anim);}});return anim;};dojo.lfx.html.wipeIn=function(_85f,_860,_861,_862){_85f=dojo.lfx.html._byId(_85f);var _863=[];dojo.lang.forEach(_85f,function(node){var _865={};dojo.html.show(node);var _866=dojo.html.getBorderBox(node).height;dojo.html.hide(node);var anim=dojo.lfx.propertyAnimation(node,{"height":{start:1,end:function(){return _866;}}},_860,_861);anim.connect("beforeBegin",function(){_865.overflow=node.style.overflow;_865.height=node.style.height;with(node.style){overflow="hidden";_866="1px";}
dojo.html.show(node);});anim.connect("onEnd",function(){with(node.style){overflow=_865.overflow;_866=_865.height;}
if(_862){_862(node,anim);}});_863.push(anim);});return dojo.lfx.combine(_863);};dojo.lfx.html.wipeOut=function(_868,_869,_86a,_86b){_868=dojo.lfx.html._byId(_868);var _86c=[];dojo.lang.forEach(_868,function(node){var _86e={};var anim=dojo.lfx.propertyAnimation(node,{"height":{start:function(){return dojo.html.getContentBox(node).height;},end:1}},_869,_86a,{"beforeBegin":function(){_86e.overflow=node.style.overflow;_86e.height=node.style.height;with(node.style){overflow="hidden";}
dojo.html.show(node);},"onEnd":function(){dojo.html.hide(node);with(node.style){overflow=_86e.overflow;height=_86e.height;}
if(_86b){_86b(node,anim);}}});_86c.push(anim);});return dojo.lfx.combine(_86c);};dojo.lfx.html.slideTo=function(_870,_871,_872,_873,_874){_870=dojo.lfx.html._byId(_870);var _875=[];var _876=dojo.html.getComputedStyle;dojo.lang.forEach(_870,function(node){var top=null;var left=null;var init=(function(){var _87b=node;return function(){var pos=_876(_87b,"position");top=(pos=="absolute"?node.offsetTop:parseInt(_876(node,"top"))||0);left=(pos=="absolute"?node.offsetLeft:parseInt(_876(node,"left"))||0);if(!dojo.lang.inArray(["absolute","relative"],pos)){var ret=dojo.html.abs(_87b,true);dojo.html.setStyleAttributes(_87b,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");top=ret.y;left=ret.x;}};})();init();var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:(_871.top||0)},"left":{start:left,end:(_871.left||0)}},_872,_873,{"beforeBegin":init});if(_874){anim.connect("onEnd",function(){_874(_870,anim);});}
_875.push(anim);});return dojo.lfx.combine(_875);};dojo.lfx.html.slideBy=function(_87f,_880,_881,_882,_883){_87f=dojo.lfx.html._byId(_87f);var _884=[];var _885=dojo.html.getComputedStyle;dojo.lang.forEach(_87f,function(node){var top=null;var left=null;var init=(function(){var _88a=node;return function(){var pos=_885(_88a,"position");top=(pos=="absolute"?node.offsetTop:parseInt(_885(node,"top"))||0);left=(pos=="absolute"?node.offsetLeft:parseInt(_885(node,"left"))||0);if(!dojo.lang.inArray(["absolute","relative"],pos)){var ret=dojo.html.abs(_88a,true);dojo.html.setStyleAttributes(_88a,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");top=ret.y;left=ret.x;}};})();init();var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:top+(_880.top||0)},"left":{start:left,end:left+(_880.left||0)}},_881,_882).connect("beforeBegin",init);if(_883){anim.connect("onEnd",function(){_883(_87f,anim);});}
_884.push(anim);});return dojo.lfx.combine(_884);};dojo.lfx.html.explode=function(_88e,_88f,_890,_891,_892){var h=dojo.html;_88e=dojo.byId(_88e);_88f=dojo.byId(_88f);var _894=h.toCoordinateObject(_88e,true);var _895=document.createElement("div");h.copyStyle(_895,_88f);if(_88f.explodeClassName){_895.className=_88f.explodeClassName;}
with(_895.style){position="absolute";display="none";var _896=h.getStyle(_88e,"background-color");backgroundColor=_896?_896.toLowerCase():"transparent";backgroundColor=(backgroundColor=="transparent")?"rgb(221, 221, 221)":backgroundColor;}
dojo.body().appendChild(_895);with(_88f.style){visibility="hidden";display="block";}
var _897=h.toCoordinateObject(_88f,true);with(_88f.style){display="none";visibility="visible";}
var _898={opacity:{start:0.5,end:1}};dojo.lang.forEach(["height","width","top","left"],function(type){_898[type]={start:_894[type],end:_897[type]};});var anim=new dojo.lfx.propertyAnimation(_895,_898,_890,_891,{"beforeBegin":function(){h.setDisplay(_895,"block");},"onEnd":function(){h.setDisplay(_88f,"block");_895.parentNode.removeChild(_895);}});if(_892){anim.connect("onEnd",function(){_892(_88f,anim);});}
return anim;};dojo.lfx.html.implode=function(_89b,end,_89d,_89e,_89f){var h=dojo.html;_89b=dojo.byId(_89b);end=dojo.byId(end);var _8a1=dojo.html.toCoordinateObject(_89b,true);var _8a2=dojo.html.toCoordinateObject(end,true);var _8a3=document.createElement("div");dojo.html.copyStyle(_8a3,_89b);if(_89b.explodeClassName){_8a3.className=_89b.explodeClassName;}
dojo.html.setOpacity(_8a3,0.3);with(_8a3.style){position="absolute";display="none";backgroundColor=h.getStyle(_89b,"background-color").toLowerCase();}
dojo.body().appendChild(_8a3);var _8a4={opacity:{start:1,end:0.5}};dojo.lang.forEach(["height","width","top","left"],function(type){_8a4[type]={start:_8a1[type],end:_8a2[type]};});var anim=new dojo.lfx.propertyAnimation(_8a3,_8a4,_89d,_89e,{"beforeBegin":function(){dojo.html.hide(_89b);dojo.html.show(_8a3);},"onEnd":function(){_8a3.parentNode.removeChild(_8a3);}});if(_89f){anim.connect("onEnd",function(){_89f(_89b,anim);});}
return anim;};dojo.lfx.html.highlight=function(_8a7,_8a8,_8a9,_8aa,_8ab){_8a7=dojo.lfx.html._byId(_8a7);var _8ac=[];dojo.lang.forEach(_8a7,function(node){var _8ae=dojo.html.getBackgroundColor(node);var bg=dojo.html.getStyle(node,"background-color").toLowerCase();var _8b0=dojo.html.getStyle(node,"background-image");var _8b1=(bg=="transparent"||bg=="rgba(0, 0, 0, 0)");while(_8ae.length>3){_8ae.pop();}
var rgb=new dojo.gfx.color.Color(_8a8);var _8b3=new dojo.gfx.color.Color(_8ae);var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:rgb,end:_8b3}},_8a9,_8aa,{"beforeBegin":function(){if(_8b0){node.style.backgroundImage="none";}
node.style.backgroundColor="rgb("+rgb.toRgb().join(",")+")";},"onEnd":function(){if(_8b0){node.style.backgroundImage=_8b0;}
if(_8b1){node.style.backgroundColor="transparent";}
if(_8ab){_8ab(node,anim);}}});_8ac.push(anim);});return dojo.lfx.combine(_8ac);};dojo.lfx.html.unhighlight=function(_8b5,_8b6,_8b7,_8b8,_8b9){_8b5=dojo.lfx.html._byId(_8b5);var _8ba=[];dojo.lang.forEach(_8b5,function(node){var _8bc=new dojo.gfx.color.Color(dojo.html.getBackgroundColor(node));var rgb=new dojo.gfx.color.Color(_8b6);var _8be=dojo.html.getStyle(node,"background-image");var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:_8bc,end:rgb}},_8b7,_8b8,{"beforeBegin":function(){if(_8be){node.style.backgroundImage="none";}
node.style.backgroundColor="rgb("+_8bc.toRgb().join(",")+")";},"onEnd":function(){if(_8b9){_8b9(node,anim);}}});_8ba.push(anim);});return dojo.lfx.combine(_8ba);};dojo.lang.mixin(dojo.lfx,dojo.lfx.html);dojo.provide("dojo.lfx.*");dojo.provide("dojo.lfx.toggle");dojo.lfx.toggle.plain={show:function(node,_8c1,_8c2,_8c3){dojo.html.show(node);if(dojo.lang.isFunction(_8c3)){_8c3();}},hide:function(node,_8c5,_8c6,_8c7){dojo.html.hide(node);if(dojo.lang.isFunction(_8c7)){_8c7();}}};dojo.lfx.toggle.fade={show:function(node,_8c9,_8ca,_8cb){dojo.lfx.fadeShow(node,_8c9,_8ca,_8cb).play();},hide:function(node,_8cd,_8ce,_8cf){dojo.lfx.fadeHide(node,_8cd,_8ce,_8cf).play();}};dojo.lfx.toggle.wipe={show:function(node,_8d1,_8d2,_8d3){dojo.lfx.wipeIn(node,_8d1,_8d2,_8d3).play();},hide:function(node,_8d5,_8d6,_8d7){dojo.lfx.wipeOut(node,_8d5,_8d6,_8d7).play();}};dojo.lfx.toggle.explode={show:function(node,_8d9,_8da,_8db,_8dc){dojo.lfx.explode(_8dc||{x:0,y:0,width:0,height:0},node,_8d9,_8da,_8db).play();},hide:function(node,_8de,_8df,_8e0,_8e1){dojo.lfx.implode(node,_8e1||{x:0,y:0,width:0,height:0},_8de,_8df,_8e0).play();}};dojo.provide("dojo.widget.HtmlWidget");dojo.declare("dojo.widget.HtmlWidget",dojo.widget.DomWidget,{templateCssPath:null,templatePath:null,lang:"",toggle:"plain",toggleDuration:150,initialize:function(args,frag){},postMixInProperties:function(args,frag){if(this.lang===""){this.lang=null;}
this.toggleObj=dojo.lfx.toggle[this.toggle.toLowerCase()]||dojo.lfx.toggle.plain;},createNodesFromText:function(txt,wrap){return dojo.html.createNodesFromText(txt,wrap);},destroyRendering:function(_8e8){try{if(this.bgIframe){this.bgIframe.remove();delete this.bgIframe;}
if(!_8e8&&this.domNode){dojo.event.browser.clean(this.domNode);}
dojo.dom.removeNode(this.domNode);delete this.domNode;}
catch(e){}},isShowing:function(){return dojo.html.isShowing(this.domNode);},toggleShowing:function(){if(this.isShowing()){this.hide();}else{this.show();}},show:function(){if(this.isShowing()){return;}
this.animationInProgress=true;this.toggleObj.show(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onShow),this.explodeSrc);},onShow:function(){this.animationInProgress=false;this.checkSize();},hide:function(){if(!this.isShowing()){return;}
this.animationInProgress=true;this.toggleObj.hide(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onHide),this.explodeSrc);},onHide:function(){this.animationInProgress=false;},_isResized:function(w,h){if(!this.isShowing()){return false;}
var wh=dojo.html.getMarginBox(this.domNode);var _8ec=w||wh.width;var _8ed=h||wh.height;if(this.width==_8ec&&this.height==_8ed){return false;}
this.width=_8ec;this.height=_8ed;return true;},checkSize:function(){if(!this._isResized()){return;}
this.onResized();},resizeTo:function(w,h){dojo.html.setMarginBox(this.domNode,{width:w,height:h});if(this.isShowing()){this.onResized();}},resizeSoon:function(){if(this.isShowing()){dojo.lang.setTimeout(this,this.onResized,0);}},onResized:function(){dojo.lang.forEach(this.children,function(_8f0){if(_8f0.checkSize){_8f0.checkSize();}});}});dojo.provide("dojo.widget.*");