
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
for(var j=_73.length-1;j>=0;j--){var loc=_73[j]||"ROOT";var _77=_71(loc);if(_77){break;}}};dojo.hostenv.localesGenerated=["ROOT","es-es","es","it-it","pt-br","de","de-at","fr-fr","zh-cn","pt","en-us","zh","fr","zh-tw","it","en-gb","xx","de-de","ko-kr","ja-jp","ko","en","ja"];dojo.hostenv.registerNlsPrefix=function(){dojo.registerModulePath("nls","nls");};dojo.hostenv.preloadLocalizations=function(){if(dojo.hostenv.localesGenerated){dojo.hostenv.registerNlsPrefix();function preload(_78){_78=dojo.hostenv.normalizeLocale(_78);dojo.hostenv.searchLocalePath(_78,true,function(loc){for(var i=0;i<dojo.hostenv.localesGenerated.length;i++){if(dojo.hostenv.localesGenerated[i]==loc){dojo["require"]("nls.dojo_"+loc);return true;}}
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
if(dojo.render.html.ie&&dojo.render.os.win){document.write("<scr"+"ipt defer src=\"//:\" "+"onreadystatechange=\"if(this.readyState=='complete'){dj_load_init();}\">"+"</scr"+"ipt>");}
if(/(WebKit|khtml)/i.test(navigator.userAgent)){var _timer=setInterval(function(){if(/loaded|complete/.test(document.readyState)){dj_load_init();}},10);}
if(dojo.render.html.ie){dj_addNodeEvtHdlr(window,"beforeunload",function(){dojo.hostenv._unloading=true;window.setTimeout(function(){dojo.hostenv._unloading=false;},0);});}
dj_addNodeEvtHdlr(window,"unload",function(){dojo.hostenv.unloaded();if((!dojo.render.html.ie)||(dojo.render.html.ie&&dojo.hostenv._unloading)){dojo.hostenv.unloaded();}});dojo.hostenv.makeWidgets=function(){var _c9=[];if(djConfig.searchIds&&djConfig.searchIds.length>0){_c9=_c9.concat(djConfig.searchIds);}
if(dojo.hostenv.searchIds&&dojo.hostenv.searchIds.length>0){_c9=_c9.concat(dojo.hostenv.searchIds);}
if((djConfig.parseWidgets)||(_c9.length>0)){if(dojo.evalObjPath("dojo.widget.Parse")){var _ca=new dojo.xml.Parse();if(_c9.length>0){for(var x=0;x<_c9.length;x++){var _cc=document.getElementById(_c9[x]);if(!_cc){continue;}
var _cd=_ca.parseElement(_cc,null,true);dojo.widget.getParser().createComponents(_cd);}}else{if(djConfig.parseWidgets){var _cd=_ca.parseElement(dojo.body(),null,true);dojo.widget.getParser().createComponents(_cd);}}}}};dojo.addOnLoad(function(){if(!dojo.render.html.ie){dojo.hostenv.makeWidgets();}});try{if(dojo.render.html.ie){document.namespaces.add("v","urn:schemas-microsoft-com:vml");document.createStyleSheet().addRule("v\\:*","behavior:url(#default#VML)");}}
catch(e){}
dojo.hostenv.writeIncludes=function(){};if(!dj_undef("document",this)){dj_currentDocument=this.document;}
dojo.doc=function(){return dj_currentDocument;};dojo.body=function(){return dojo.doc().body||dojo.doc().getElementsByTagName("body")[0];};dojo.byId=function(id,doc){if((id)&&((typeof id=="string")||(id instanceof String))){if(!doc){doc=dj_currentDocument;}
var ele=doc.getElementById(id);if(ele&&(ele.id!=id)&&doc.all){ele=null;eles=doc.all[id];if(eles){if(eles.length){for(var i=0;i<eles.length;i++){if(eles[i].id==id){ele=eles[i];break;}}}else{ele=eles;}}}
return ele;}
return id;};dojo.setContext=function(_d2,_d3){dj_currentContext=_d2;dj_currentDocument=_d3;};dojo._fireCallback=function(_d4,_d5,_d6){if((_d5)&&((typeof _d4=="string")||(_d4 instanceof String))){_d4=_d5[_d4];}
return (_d5?_d4.apply(_d5,_d6||[]):_d4());};dojo.withGlobal=function(_d7,_d8,_d9,_da){var _db;var _dc=dj_currentContext;var _dd=dj_currentDocument;try{dojo.setContext(_d7,_d7.document);_db=dojo._fireCallback(_d8,_d9,_da);}
finally{dojo.setContext(_dc,_dd);}
return _db;};dojo.withDoc=function(_de,_df,_e0,_e1){var _e2;var _e3=dj_currentDocument;try{dj_currentDocument=_de;_e2=dojo._fireCallback(_df,_e0,_e1);}
finally{dj_currentDocument=_e3;}
return _e2;};}
(function(){if(typeof dj_usingBootstrap!="undefined"){return;}
var _e4=false;var _e5=false;var _e6=false;if((typeof this["load"]=="function")&&((typeof this["Packages"]=="function")||(typeof this["Packages"]=="object"))){_e4=true;}else{if(typeof this["load"]=="function"){_e5=true;}else{if(window.widget){_e6=true;}}}
var _e7=[];if((this["djConfig"])&&((djConfig["isDebug"])||(djConfig["debugAtAllCosts"]))){_e7.push("debug.js");}
if((this["djConfig"])&&(djConfig["debugAtAllCosts"])&&(!_e4)&&(!_e6)){_e7.push("browser_debug.js");}
var _e8=djConfig["baseScriptUri"];if((this["djConfig"])&&(djConfig["baseLoaderUri"])){_e8=djConfig["baseLoaderUri"];}
for(var x=0;x<_e7.length;x++){var _ea=_e8+"src/"+_e7[x];if(_e4||_e5){load(_ea);}else{try{document.write("<scr"+"ipt type='text/javascript' src='"+_ea+"'></scr"+"ipt>");}
catch(e){var _eb=document.createElement("script");_eb.src=_ea;document.getElementsByTagName("head")[0].appendChild(_eb);}}}})();dojo.provide("dojo.lang.common");dojo.lang.inherits=function(_ec,_ed){if(typeof _ed!="function"){dojo.raise("dojo.inherits: superclass argument ["+_ed+"] must be a function (subclass: ["+_ec+"']");}
_ec.prototype=new _ed();_ec.prototype.constructor=_ec;_ec.superclass=_ed.prototype;_ec["super"]=_ed.prototype;};dojo.lang._mixin=function(obj,_ef){var _f0={};for(var x in _ef){if((typeof _f0[x]=="undefined")||(_f0[x]!=_ef[x])){obj[x]=_ef[x];}}
if(dojo.render.html.ie&&(typeof (_ef["toString"])=="function")&&(_ef["toString"]!=obj["toString"])&&(_ef["toString"]!=_f0["toString"])){obj.toString=_ef.toString;}
return obj;};dojo.lang.mixin=function(obj,_f3){for(var i=1,l=arguments.length;i<l;i++){dojo.lang._mixin(obj,arguments[i]);}
return obj;};dojo.lang.extend=function(_f6,_f7){for(var i=1,l=arguments.length;i<l;i++){dojo.lang._mixin(_f6.prototype,arguments[i]);}
return _f6;};dojo.lang._delegate=function(obj){function TMP(){}
TMP.prototype=obj;return new TMP();};dojo.inherits=dojo.lang.inherits;dojo.mixin=dojo.lang.mixin;dojo.extend=dojo.lang.extend;dojo.lang.find=function(_fb,_fc,_fd,_fe){if(!dojo.lang.isArrayLike(_fb)&&dojo.lang.isArrayLike(_fc)){dojo.deprecated("dojo.lang.find(value, array)","use dojo.lang.find(array, value) instead","0.5");var _ff=_fb;_fb=_fc;_fc=_ff;}
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
return false;};dojo.lang.isFunction=function(it){return (it instanceof Function||typeof it=="function");};(function(){if((dojo.render.html.capable)&&(dojo.render.html["safari"])){dojo.lang.isFunction=function(it){if((typeof (it)=="function")&&(it=="[object NodeList]")){return false;}
return (it instanceof Function||typeof it=="function");};}})();dojo.lang.isString=function(it){return (typeof it=="string"||it instanceof String);};dojo.lang.isAlien=function(it){if(!it){return false;}
return !dojo.lang.isFunction(it)&&/\{\s*\[native code\]\s*\}/.test(String(it));};dojo.lang.isBoolean=function(it){return (it instanceof Boolean||typeof it=="boolean");};dojo.lang.isNumber=function(it){return (it instanceof Number||typeof it=="number");};dojo.lang.isUndefined=function(it){return ((typeof (it)=="undefined")&&(it==undefined));};dojo.provide("dojo.lang.array");dojo.lang.mixin(dojo.lang,{has:function(obj,name){try{return typeof obj[name]!="undefined";}
catch(e){return false;}},isEmpty:function(obj){if(dojo.lang.isObject(obj)){var tmp={};var _117=0;for(var x in obj){if(obj[x]&&(!tmp[x])){_117++;break;}}
return _117==0;}else{if(dojo.lang.isArrayLike(obj)||dojo.lang.isString(obj)){return obj.length==0;}}},map:function(arr,obj,_11b){var _11c=dojo.lang.isString(arr);if(_11c){arr=arr.split("");}
if(dojo.lang.isFunction(obj)&&(!_11b)){_11b=obj;obj=dj_global;}else{if(dojo.lang.isFunction(obj)&&_11b){var _11d=obj;obj=_11b;_11b=_11d;}}
if(Array.map){var _11e=Array.map(arr,_11b,obj);}else{var _11e=[];for(var i=0;i<arr.length;++i){_11e.push(_11b.call(obj,arr[i]));}}
if(_11c){return _11e.join("");}else{return _11e;}},reduce:function(arr,_121,obj,_123){var _124=_121;var ob=obj?obj:dj_global;dojo.lang.map(arr,function(val){_124=_123.call(ob,_124,val);});return _124;},forEach:function(_127,_128,_129){if(dojo.lang.isString(_127)){_127=_127.split("");}
if(Array.forEach){Array.forEach(_127,_128,_129);}else{if(!_129){_129=dj_global;}
for(var i=0,l=_127.length;i<l;i++){_128.call(_129,_127[i],i,_127);}}},_everyOrSome:function(_12c,arr,_12e,_12f){if(dojo.lang.isString(arr)){arr=arr.split("");}
if(Array.every){return Array[_12c?"every":"some"](arr,_12e,_12f);}else{if(!_12f){_12f=dj_global;}
for(var i=0,l=arr.length;i<l;i++){var _132=_12e.call(_12f,arr[i],i,arr);if(_12c&&!_132){return false;}else{if((!_12c)&&(_132)){return true;}}}
return Boolean(_12c);}},every:function(arr,_134,_135){return this._everyOrSome(true,arr,_134,_135);},some:function(arr,_137,_138){return this._everyOrSome(false,arr,_137,_138);},filter:function(arr,_13a,_13b){var _13c=dojo.lang.isString(arr);if(_13c){arr=arr.split("");}
var _13d;if(Array.filter){_13d=Array.filter(arr,_13a,_13b);}else{if(!_13b){if(arguments.length>=3){dojo.raise("thisObject doesn't exist!");}
_13b=dj_global;}
_13d=[];for(var i=0;i<arr.length;i++){if(_13a.call(_13b,arr[i],i,arr)){_13d.push(arr[i]);}}}
if(_13c){return _13d.join("");}else{return _13d;}},unnest:function(){var out=[];for(var i=0;i<arguments.length;i++){if(dojo.lang.isArrayLike(arguments[i])){var add=dojo.lang.unnest.apply(this,arguments[i]);out=out.concat(add);}else{out.push(arguments[i]);}}
return out;},toArray:function(_142,_143){var _144=[];for(var i=_143||0;i<_142.length;i++){_144.push(_142[i]);}
return _144;}});dojo.provide("dojo.lang.extras");dojo.lang.setTimeout=function(func,_147){var _148=window,_149=2;if(!dojo.lang.isFunction(func)){_148=func;func=_147;_147=arguments[2];_149++;}
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
setTimeout(function(){(farr.shift())();cb();dojo.lang.delayThese(farr,cb,_17c,_17d);},_17c);};dojo.provide("dojo.event.common");dojo.event=new function(){this._canTimeout=dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);function interpolateArgs(args,_17f){var dl=dojo.lang;var ao={srcObj:dj_global,srcFunc:null,adviceObj:dj_global,adviceFunc:null,aroundObj:null,aroundFunc:null,adviceType:(args.length>2)?args[0]:"after",precedence:"last",once:false,delay:null,rate:0,adviceMsg:false,maxCalls:-1};switch(args.length){case 0:
return;case 1:
return;case 2:
ao.srcFunc=args[0];ao.adviceFunc=args[1];break;case 3:
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isString(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];ao.adviceFunc=args[2];}else{if((dl.isString(args[1]))&&(dl.isString(args[2]))){ao.srcFunc=args[1];ao.adviceFunc=args[2];}else{if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isFunction(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];var _182=dl.nameAnonFunc(args[2],ao.adviceObj,_17f);ao.adviceFunc=_182;}else{if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){ao.adviceType="after";ao.srcObj=dj_global;var _182=dl.nameAnonFunc(args[0],ao.srcObj,_17f);ao.srcFunc=_182;ao.adviceObj=args[1];ao.adviceFunc=args[2];}}}}
break;case 4:
if((dl.isObject(args[0]))&&(dl.isObject(args[2]))){ao.adviceType="after";ao.srcObj=args[0];ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isString(args[1]))&&(dl.isObject(args[2]))){ao.adviceType=args[0];ao.srcObj=dj_global;ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isFunction(args[1]))&&(dl.isObject(args[2]))){ao.adviceType=args[0];ao.srcObj=dj_global;var _182=dl.nameAnonFunc(args[1],dj_global,_17f);ao.srcFunc=_182;ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){ao.srcObj=args[1];ao.srcFunc=args[2];var _182=dl.nameAnonFunc(args[3],dj_global,_17f);ao.adviceObj=dj_global;ao.adviceFunc=_182;}else{if(dl.isObject(args[1])){ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=dj_global;ao.adviceFunc=args[3];}else{if(dl.isObject(args[2])){ao.srcObj=dj_global;ao.srcFunc=args[1];ao.adviceObj=args[2];ao.adviceFunc=args[3];}else{ao.srcObj=ao.adviceObj=ao.aroundObj=dj_global;ao.srcFunc=args[1];ao.adviceFunc=args[2];ao.aroundFunc=args[3];}}}}}}
break;case 6:
ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=args[3];ao.adviceFunc=args[4];ao.aroundFunc=args[5];ao.aroundObj=dj_global;break;default:
ao.srcObj=args[1];ao.srcFunc=args[2];ao.adviceObj=args[3];ao.adviceFunc=args[4];ao.aroundObj=args[5];ao.aroundFunc=args[6];ao.once=args[7];ao.delay=args[8];ao.rate=args[9];ao.adviceMsg=args[10];ao.maxCalls=(!isNaN(parseInt(args[11])))?args[11]:-1;break;}
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
return this.connect.apply(this,args);};this.connectOnce=function(){var ao=interpolateArgs(arguments,true);ao.once=true;return this.connect(ao);};this.connectRunOnce=function(){var ao=interpolateArgs(arguments,true);ao.maxCalls=1;return this.connect(ao);};this._kwConnectImpl=function(_195,_196){var fn=(_196)?"disconnect":"connect";if(typeof _195["srcFunc"]=="function"){_195.srcObj=_195["srcObj"]||dj_global;var _198=dojo.lang.nameAnonFunc(_195.srcFunc,_195.srcObj,true);_195.srcFunc=_198;}
if(typeof _195["adviceFunc"]=="function"){_195.adviceObj=_195["adviceObj"]||dj_global;var _198=dojo.lang.nameAnonFunc(_195.adviceFunc,_195.adviceObj,true);_195.adviceFunc=_198;}
_195.srcObj=_195["srcObj"]||dj_global;_195.adviceObj=_195["adviceObj"]||_195["targetObj"]||dj_global;_195.adviceFunc=_195["adviceFunc"]||_195["targetFunc"];return dojo.event[fn](_195);};this.kwConnect=function(_199){return this._kwConnectImpl(_199,false);};this.disconnect=function(){if(arguments.length==1){var ao=arguments[0];}else{var ao=interpolateArgs(arguments,true);}
if(!ao.adviceFunc){return;}
if(dojo.lang.isString(ao.srcFunc)&&(ao.srcFunc.toLowerCase()=="onkey")){if(dojo.render.html.ie){ao.srcFunc="onkeydown";this.disconnect(ao);}
ao.srcFunc="onkeypress";}
if(!ao.srcObj[ao.srcFunc]){return null;}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc,true);mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);return mjp;};this.kwDisconnect=function(_19c){return this._kwConnectImpl(_19c,true);};};dojo.event.MethodInvocation=function(_19d,obj,args){this.jp_=_19d;this.object=obj;this.args=[];for(var x=0;x<args.length;x++){this.args[x]=args[x];}
this.around_index=-1;};dojo.event.MethodInvocation.prototype.proceed=function(){this.around_index++;if(this.around_index>=this.jp_.around.length){return this.jp_.object[this.jp_.methodname].apply(this.jp_.object,this.args);}else{var ti=this.jp_.around[this.around_index];var mobj=ti[0]||dj_global;var meth=ti[1];return mobj[meth].call(mobj,this);}};dojo.event.MethodJoinPoint=function(obj,_1a5){this.object=obj||dj_global;this.methodname=_1a5;this.methodfunc=this.object[_1a5];};dojo.event.MethodJoinPoint.getForMethod=function(obj,_1a7){if(!obj){obj=dj_global;}
var ofn=obj[_1a7];if(!ofn){ofn=obj[_1a7]=function(){};if(!obj[_1a7]){dojo.raise("Cannot set do-nothing method on that object "+_1a7);}}else{if((typeof ofn!="function")&&(!dojo.lang.isFunction(ofn))&&(!dojo.lang.isAlien(ofn))){return null;}}
var _1a9=_1a7+"$joinpoint";var _1aa=_1a7+"$joinpoint$method";var _1ab=obj[_1a9];if(!_1ab){var _1ac=false;if(dojo.event["browser"]){if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){_1ac=true;dojo.event.browser.addClobberNodeAttrs(obj,[_1a9,_1aa,_1a7]);}}
var _1ad=ofn.length;obj[_1aa]=ofn;_1ab=obj[_1a9]=new dojo.event.MethodJoinPoint(obj,_1aa);if(!_1ac){obj[_1a7]=function(){return _1ab.run.apply(_1ab,arguments);};}else{obj[_1a7]=function(){var args=[];if(!arguments.length){var evt=null;try{if(obj.ownerDocument){evt=obj.ownerDocument.parentWindow.event;}else{if(obj.documentElement){evt=obj.documentElement.ownerDocument.parentWindow.event;}else{if(obj.event){evt=obj.event;}else{evt=window.event;}}}}
catch(e){evt=window.event;}
if(evt){args.push(dojo.event.browser.fixEvent(evt,this));}}else{for(var x=0;x<arguments.length;x++){if((x==0)&&(dojo.event.browser.isEvent(arguments[x]))){args.push(dojo.event.browser.fixEvent(arguments[x],this));}else{args.push(arguments[x]);}}}
return _1ab.run.apply(_1ab,args);};}
obj[_1a7].__preJoinArity=_1ad;}
return _1ab;};dojo.lang.extend(dojo.event.MethodJoinPoint,{squelch:false,unintercept:function(){this.object[this.methodname]=this.methodfunc;this.before=[];this.after=[];this.around=[];},disconnect:dojo.lang.forward("unintercept"),run:function(){var obj=this.object||dj_global;var args=arguments;var _1b3=[];for(var x=0;x<args.length;x++){_1b3[x]=args[x];}
var _1b5=function(marr){if(!marr){dojo.debug("Null argument to unrollAdvice()");return;}
var _1b7=marr[0]||dj_global;var _1b8=marr[1];if(!_1b7[_1b8]){dojo.raise("function \""+_1b8+"\" does not exist on \""+_1b7+"\"");}
var _1b9=marr[2]||dj_global;var _1ba=marr[3];var msg=marr[6];var _1bc=marr[7];if(_1bc>-1){if(_1bc==0){return;}
marr[7]--;}
var _1bd;var to={args:[],jp_:this,object:obj,proceed:function(){return _1b7[_1b8].apply(_1b7,to.args);}};to.args=_1b3;var _1bf=parseInt(marr[4]);var _1c0=((!isNaN(_1bf))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));if(marr[5]){var rate=parseInt(marr[5]);var cur=new Date();var _1c3=false;if((marr["last"])&&((cur-marr.last)<=rate)){if(dojo.event._canTimeout){if(marr["delayTimer"]){clearTimeout(marr.delayTimer);}
var tod=parseInt(rate*2);var mcpy=dojo.lang.shallowCopy(marr);marr.delayTimer=setTimeout(function(){mcpy[5]=0;_1b5(mcpy);},tod);}
return;}else{marr.last=cur;}}
if(_1ba){_1b9[_1ba].call(_1b9,to);}else{if((_1c0)&&((dojo.render.html)||(dojo.render.svg))){dj_global["setTimeout"](function(){if(msg){_1b7[_1b8].call(_1b7,to);}else{_1b7[_1b8].apply(_1b7,args);}},_1bf);}else{if(msg){_1b7[_1b8].call(_1b7,to);}else{_1b7[_1b8].apply(_1b7,args);}}}};var _1c6=function(){if(this.squelch){try{return _1b5.apply(this,arguments);}
catch(e){dojo.debug(e);}}else{return _1b5.apply(this,arguments);}};if((this["before"])&&(this.before.length>0)){dojo.lang.forEach(this.before.concat(new Array()),_1c6);}
var _1c7;try{if((this["around"])&&(this.around.length>0)){var mi=new dojo.event.MethodInvocation(this,obj,args);_1c7=mi.proceed();}else{if(this.methodfunc){_1c7=this.object[this.methodname].apply(this.object,args);}}}
catch(e){if(!this.squelch){dojo.debug(e,"when calling",this.methodname,"on",this.object,"with arguments",args);dojo.raise(e);}}
if((this["after"])&&(this.after.length>0)){dojo.lang.forEach(this.after.concat(new Array()),_1c6);}
return (this.methodfunc)?_1c7:null;},getArr:function(kind){var type="after";if((typeof kind=="string")&&(kind.indexOf("before")!=-1)){type="before";}else{if(kind=="around"){type="around";}}
if(!this[type]){this[type]=[];}
return this[type];},kwAddAdvice:function(args){this.addAdvice(args["adviceObj"],args["adviceFunc"],args["aroundObj"],args["aroundFunc"],args["adviceType"],args["precedence"],args["once"],args["delay"],args["rate"],args["adviceMsg"],args["maxCalls"]);},addAdvice:function(_1cc,_1cd,_1ce,_1cf,_1d0,_1d1,once,_1d3,rate,_1d5,_1d6){var arr=this.getArr(_1d0);if(!arr){dojo.raise("bad this: "+this);}
var ao=[_1cc,_1cd,_1ce,_1cf,_1d3,rate,_1d5,_1d6];if(once){if(this.hasAdvice(_1cc,_1cd,_1d0,arr)>=0){return;}}
if(_1d1=="first"){arr.unshift(ao);}else{arr.push(ao);}},hasAdvice:function(_1d9,_1da,_1db,arr){if(!arr){arr=this.getArr(_1db);}
var ind=-1;for(var x=0;x<arr.length;x++){var aao=(typeof _1da=="object")?(new String(_1da)).toString():_1da;var a1o=(typeof arr[x][1]=="object")?(new String(arr[x][1])).toString():arr[x][1];if((arr[x][0]==_1d9)&&(a1o==aao)){ind=x;}}
return ind;},removeAdvice:function(_1e1,_1e2,_1e3,once){var arr=this.getArr(_1e3);var ind=this.hasAdvice(_1e1,_1e2,_1e3,arr);if(ind==-1){return false;}
while(ind!=-1){arr.splice(ind,1);if(once){break;}
ind=this.hasAdvice(_1e1,_1e2,_1e3,arr);}
return true;}});dojo.provide("dojo.event.browser");dojo._ie_clobber=new function(){this.clobberNodes=[];function nukeProp(node,prop){try{node[prop]=null;}
catch(e){}
try{delete node[prop];}
catch(e){}
try{node.removeAttribute(prop);}
catch(e){}}
this.clobber=function(_1e9){var na;var tna;if(_1e9){tna=_1e9.all||_1e9.getElementsByTagName("*");na=[_1e9];for(var x=0;x<tna.length;x++){if(tna[x]["__doClobber__"]){na.push(tna[x]);}}}else{try{window.onload=null;}
catch(e){}
na=(this.clobberNodes.length)?this.clobberNodes:document.all;}
tna=null;var _1ed={};for(var i=na.length-1;i>=0;i=i-1){var el=na[i];try{if(el&&el["__clobberAttrs__"]){for(var j=0;j<el.__clobberAttrs__.length;j++){nukeProp(el,el.__clobberAttrs__[j]);}
nukeProp(el,"__clobberAttrs__");nukeProp(el,"__doClobber__");}}
catch(e){}}
na=null;};};if(dojo.render.html.ie){dojo.addOnUnload(function(){dojo._ie_clobber.clobber();try{if((dojo["widget"])&&(dojo.widget["manager"])){dojo.widget.manager.destroyAll();}}
catch(e){}
if(dojo.widget){for(var name in dojo.widget._templateCache){if(dojo.widget._templateCache[name].node){dojo.dom.removeNode(dojo.widget._templateCache[name].node);dojo.widget._templateCache[name].node=null;delete dojo.widget._templateCache[name].node;}}}
if(dojo.dom){while(dojo.dom._ieRemovedNodes.length>0){var node=dojo.dom._ieRemovedNodes.pop();dojo.dom._discardElement(node);node=null;}}
try{window.onload=null;}
catch(e){}
try{window.onunload=null;}
catch(e){}
dojo._ie_clobber.clobberNodes=[];});}
dojo.event.browser=new function(){var _1f3=0;this.normalizedEventName=function(_1f4){switch(_1f4){case "CheckboxStateChange":
case "DOMAttrModified":
case "DOMMenuItemActive":
case "DOMMenuItemInactive":
case "DOMMouseScroll":
case "DOMNodeInserted":
case "DOMNodeRemoved":
case "RadioStateChange":
return _1f4;break;default:
return _1f4.toLowerCase();break;}};this.clean=function(node){if(dojo.render.html.ie){dojo._ie_clobber.clobber(node);}};this.addClobberNode=function(node){if(!dojo.render.html.ie){return;}
if(!node["__doClobber__"]){node.__doClobber__=true;dojo._ie_clobber.clobberNodes.push(node);node.__clobberAttrs__=[];}};this.addClobberNodeAttrs=function(node,_1f8){if(!dojo.render.html.ie){return;}
this.addClobberNode(node);for(var x=0;x<_1f8.length;x++){node.__clobberAttrs__.push(_1f8[x]);}};this.removeListener=function(node,_1fb,fp,_1fd){if(!_1fd){var _1fd=false;}
_1fb=dojo.event.browser.normalizedEventName(_1fb);if((_1fb=="onkey")||(_1fb=="key")){if(dojo.render.html.ie){this.removeListener(node,"onkeydown",fp,_1fd);}
_1fb="onkeypress";}
if(_1fb.substr(0,2)=="on"){_1fb=_1fb.substr(2);}
if(node.removeEventListener){node.removeEventListener(_1fb,fp,_1fd);}};this.addListener=function(node,_1ff,fp,_201,_202){if(!node){return;}
if(!_201){var _201=false;}
_1ff=dojo.event.browser.normalizedEventName(_1ff);if((_1ff=="onkey")||(_1ff=="key")){if(dojo.render.html.ie){this.addListener(node,"onkeydown",fp,_201,_202);}
_1ff="onkeypress";}
if(_1ff.substr(0,2)!="on"){_1ff="on"+_1ff;}
if(!_202){var _203=function(evt){if(!evt){evt=window.event;}
var ret=fp(dojo.event.browser.fixEvent(evt,this));if(_201){dojo.event.browser.stopEvent(evt);}
return ret;};}else{_203=fp;}
if(node.addEventListener){node.addEventListener(_1ff.substr(2),_203,_201);return _203;}else{if(typeof node[_1ff]=="function"){var _206=node[_1ff];node[_1ff]=function(e){_206(e);return _203(e);};}else{node[_1ff]=_203;}
if(dojo.render.html.ie){this.addClobberNodeAttrs(node,[_1ff]);}
return _203;}};this.isEvent=function(obj){return (typeof obj!="undefined")&&(typeof Event!="undefined")&&(obj.eventPhase);};this.currentEvent=null;this.callListener=function(_209,_20a){if(typeof _209!="function"){dojo.raise("listener not a function: "+_209);}
dojo.event.browser.currentEvent.currentTarget=_20a;return _209.call(_20a,dojo.event.browser.currentEvent);};this._stopPropagation=function(){dojo.event.browser.currentEvent.cancelBubble=true;};this._preventDefault=function(){dojo.event.browser.currentEvent.returnValue=false;};this.keys={KEY_BACKSPACE:8,KEY_TAB:9,KEY_CLEAR:12,KEY_ENTER:13,KEY_SHIFT:16,KEY_CTRL:17,KEY_ALT:18,KEY_PAUSE:19,KEY_CAPS_LOCK:20,KEY_ESCAPE:27,KEY_SPACE:32,KEY_PAGE_UP:33,KEY_PAGE_DOWN:34,KEY_END:35,KEY_HOME:36,KEY_LEFT_ARROW:37,KEY_UP_ARROW:38,KEY_RIGHT_ARROW:39,KEY_DOWN_ARROW:40,KEY_INSERT:45,KEY_DELETE:46,KEY_HELP:47,KEY_LEFT_WINDOW:91,KEY_RIGHT_WINDOW:92,KEY_SELECT:93,KEY_NUMPAD_0:96,KEY_NUMPAD_1:97,KEY_NUMPAD_2:98,KEY_NUMPAD_3:99,KEY_NUMPAD_4:100,KEY_NUMPAD_5:101,KEY_NUMPAD_6:102,KEY_NUMPAD_7:103,KEY_NUMPAD_8:104,KEY_NUMPAD_9:105,KEY_NUMPAD_MULTIPLY:106,KEY_NUMPAD_PLUS:107,KEY_NUMPAD_ENTER:108,KEY_NUMPAD_MINUS:109,KEY_NUMPAD_PERIOD:110,KEY_NUMPAD_DIVIDE:111,KEY_F1:112,KEY_F2:113,KEY_F3:114,KEY_F4:115,KEY_F5:116,KEY_F6:117,KEY_F7:118,KEY_F8:119,KEY_F9:120,KEY_F10:121,KEY_F11:122,KEY_F12:123,KEY_F13:124,KEY_F14:125,KEY_F15:126,KEY_NUM_LOCK:144,KEY_SCROLL_LOCK:145};this.revKeys=[];for(var key in this.keys){this.revKeys[this.keys[key]]=key;}
this.fixEvent=function(evt,_20d){if(!evt){if(window["event"]){evt=window.event;}}
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
if(evt.ctrlKey||evt.altKey){var _20f=evt.keyCode;if(_20f>=65&&_20f<=90&&evt.shiftKey==false){_20f+=32;}
if(_20f>=1&&_20f<=26&&evt.ctrlKey){_20f+=96;}
evt.key=String.fromCharCode(_20f);}}}else{if(evt["type"]=="keypress"){if(dojo.render.html.opera){if(evt.which==0){evt.key=evt.keyCode;}else{if(evt.which>0){switch(evt.which){case evt.KEY_SHIFT:
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
var _20f=evt.which;if((evt.ctrlKey||evt.altKey||evt.metaKey)&&(evt.which>=65&&evt.which<=90&&evt.shiftKey==false)){_20f+=32;}
evt.key=String.fromCharCode(_20f);}}}}else{if(dojo.render.html.ie){if(!evt.ctrlKey&&!evt.altKey&&evt.keyCode>=evt.KEY_SPACE){evt.key=String.fromCharCode(evt.keyCode);}}else{if(dojo.render.html.safari){switch(evt.keyCode){case 25:
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
if(!evt.currentTarget){evt.currentTarget=(_20d?_20d:evt.srcElement);}
if(!evt.layerX){evt.layerX=evt.offsetX;}
if(!evt.layerY){evt.layerY=evt.offsetY;}
var doc=(evt.srcElement&&evt.srcElement.ownerDocument)?evt.srcElement.ownerDocument:document;var _211=((dojo.render.html.ie55)||(doc["compatMode"]=="BackCompat"))?doc.body:doc.documentElement;if(!evt.pageX){evt.pageX=evt.clientX+(_211.scrollLeft||0);}
if(!evt.pageY){evt.pageY=evt.clientY+(_211.scrollTop||0);}
if(evt.type=="mouseover"){evt.relatedTarget=evt.fromElement;}
if(evt.type=="mouseout"){evt.relatedTarget=evt.toElement;}
this.currentEvent=evt;evt.callListener=this.callListener;evt.stopPropagation=this._stopPropagation;evt.preventDefault=this._preventDefault;}
return evt;};this.stopEvent=function(evt){if(window.event){evt.cancelBubble=true;evt.returnValue=false;}else{evt.preventDefault();evt.stopPropagation();}};};dojo.provide("dojo.dom");dojo.dom.ELEMENT_NODE=1;dojo.dom.ATTRIBUTE_NODE=2;dojo.dom.TEXT_NODE=3;dojo.dom.CDATA_SECTION_NODE=4;dojo.dom.ENTITY_REFERENCE_NODE=5;dojo.dom.ENTITY_NODE=6;dojo.dom.PROCESSING_INSTRUCTION_NODE=7;dojo.dom.COMMENT_NODE=8;dojo.dom.DOCUMENT_NODE=9;dojo.dom.DOCUMENT_TYPE_NODE=10;dojo.dom.DOCUMENT_FRAGMENT_NODE=11;dojo.dom.NOTATION_NODE=12;dojo.dom.dojoml="http://www.dojotoolkit.org/2004/dojoml";dojo.dom.xmlns={svg:"http://www.w3.org/2000/svg",smil:"http://www.w3.org/2001/SMIL20/",mml:"http://www.w3.org/1998/Math/MathML",cml:"http://www.xml-cml.org",xlink:"http://www.w3.org/1999/xlink",xhtml:"http://www.w3.org/1999/xhtml",xul:"http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul",xbl:"http://www.mozilla.org/xbl",fo:"http://www.w3.org/1999/XSL/Format",xsl:"http://www.w3.org/1999/XSL/Transform",xslt:"http://www.w3.org/1999/XSL/Transform",xi:"http://www.w3.org/2001/XInclude",xforms:"http://www.w3.org/2002/01/xforms",saxon:"http://icl.com/saxon",xalan:"http://xml.apache.org/xslt",xsd:"http://www.w3.org/2001/XMLSchema",dt:"http://www.w3.org/2001/XMLSchema-datatypes",xsi:"http://www.w3.org/2001/XMLSchema-instance",rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#",rdfs:"http://www.w3.org/2000/01/rdf-schema#",dc:"http://purl.org/dc/elements/1.1/",dcq:"http://purl.org/dc/qualifiers/1.0","soap-env":"http://schemas.xmlsoap.org/soap/envelope/",wsdl:"http://schemas.xmlsoap.org/wsdl/",AdobeExtensions:"http://ns.adobe.com/AdobeSVGViewerExtensions/3.0/"};dojo.dom.isNode=function(wh){if(typeof Element=="function"){try{return wh instanceof Element;}
catch(e){}}else{return wh&&!isNaN(wh.nodeType);}};dojo.dom.getUniqueId=function(){var _214=dojo.doc();do{var id="dj_unique_"+(++arguments.callee._idIncrement);}while(_214.getElementById(id));return id;};dojo.dom.getUniqueId._idIncrement=0;dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_216,_217){var node=_216.firstChild;while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){node=node.nextSibling;}
if(_217&&node&&node.tagName&&node.tagName.toLowerCase()!=_217.toLowerCase()){node=dojo.dom.nextElement(node,_217);}
return node;};dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_219,_21a){var node=_219.lastChild;while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){node=node.previousSibling;}
if(_21a&&node&&node.tagName&&node.tagName.toLowerCase()!=_21a.toLowerCase()){node=dojo.dom.prevElement(node,_21a);}
return node;};dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(node,_21d){if(!node){return null;}
do{node=node.nextSibling;}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);if(node&&_21d&&_21d.toLowerCase()!=node.tagName.toLowerCase()){return dojo.dom.nextElement(node,_21d);}
return node;};dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(node,_21f){if(!node){return null;}
if(_21f){_21f=_21f.toLowerCase();}
do{node=node.previousSibling;}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);if(node&&_21f&&_21f.toLowerCase()!=node.tagName.toLowerCase()){return dojo.dom.prevElement(node,_21f);}
return node;};dojo.dom.moveChildren=function(_220,_221,trim){var _223=0;if(trim){while(_220.hasChildNodes()&&_220.firstChild.nodeType==dojo.dom.TEXT_NODE){_220.removeChild(_220.firstChild);}
while(_220.hasChildNodes()&&_220.lastChild.nodeType==dojo.dom.TEXT_NODE){_220.removeChild(_220.lastChild);}}
while(_220.hasChildNodes()){_221.appendChild(_220.firstChild);_223++;}
return _223;};dojo.dom.copyChildren=function(_224,_225,trim){var _227=_224.cloneNode(true);return this.moveChildren(_227,_225,trim);};dojo.dom.replaceChildren=function(node,_229){dojo.dom.removeChildren(node);node.appendChild(_229);};dojo.dom.removeChildren=function(node){var _22b=node.childNodes.length;while(node.hasChildNodes()){dojo.dom.removeNode(node.firstChild);}
return _22b;};dojo.dom.replaceNode=function(node,_22d){if(dojo.render.html.ie){node.parentNode.insertBefore(_22d,node);return dojo.dom.removeNode(node);}else{return node.parentNode.replaceChild(_22d,node);}};dojo.dom._ieRemovedNodes=[];dojo.dom.removeNode=function(node,_22f){if(node&&node.parentNode){try{if(_22f&&dojo.evalObjPath("dojo.event.browser.clean",false)){dojo.event.browser.clean(node);}}
catch(e){}
if(dojo.render.html.ie){if(_22f){dojo.dom._discardElement(node);}else{dojo.dom._ieRemovedNodes.push(node);}}
if(_22f){return null;}
return node.parentNode.removeChild(node);}};dojo.dom._discardElement=function(_230){var _231=document.getElementById("IELeakGarbageBin");if(!_231){_231=document.createElement("DIV");_231.id="IELeakGarbageBin";_231.style.display="none";document.body.appendChild(_231);}
_231.appendChild(_230);_231.innerHTML="";};dojo.dom.getAncestors=function(node,_233,_234){var _235=[];var _236=(_233&&(_233 instanceof Function||typeof _233=="function"));while(node){if(!_236||_233(node)){_235.push(node);}
if(_234&&_235.length>0){return _235[0];}
node=node.parentNode;}
if(_234){return null;}
return _235;};dojo.dom.getAncestorsByTag=function(node,tag,_239){tag=tag.toLowerCase();return dojo.dom.getAncestors(node,function(el){return ((el.tagName)&&(el.tagName.toLowerCase()==tag));},_239);};dojo.dom.getFirstAncestorByTag=function(node,tag){return dojo.dom.getAncestorsByTag(node,tag,true);};dojo.dom.isDescendantOf=function(node,_23e,_23f){if(_23f&&node){node=node.parentNode;}
while(node){if(node==_23e){return true;}
node=node.parentNode;}
return false;};dojo.dom.innerXML=function(node){if(node.innerXML){return node.innerXML;}else{if(node.xml){return node.xml;}else{if(typeof XMLSerializer!="undefined"){return (new XMLSerializer()).serializeToString(node);}}}};dojo.dom.createDocument=function(){var doc=null;var _242=dojo.doc();if(!dj_undef("ActiveXObject")){var _243=["MSXML2","Microsoft","MSXML","MSXML3"];for(var i=0;i<_243.length;i++){try{doc=new ActiveXObject(_243[i]+".XMLDOM");}
catch(e){}
if(doc){break;}}}else{if((_242.implementation)&&(_242.implementation.createDocument)){doc=_242.implementation.createDocument("","",null);}}
return doc;};dojo.dom.createDocumentFromText=function(str,_246){if(!_246){_246="text/xml";}
if(!dj_undef("DOMParser")){var _247=new DOMParser();return _247.parseFromString(str,_246);}else{if(!dj_undef("ActiveXObject")){var _248=dojo.dom.createDocument();if(_248){_248.async=false;_248.loadXML(str);return _248;}else{dojo.debug("toXml didn't work?");}}else{var _249=dojo.doc();if(_249.createElement){var tmp=_249.createElement("xml");tmp.innerHTML=str;if(_249.implementation&&_249.implementation.createDocument){var _24b=_249.implementation.createDocument("foo","",null);for(var i=0;i<tmp.childNodes.length;i++){_24b.importNode(tmp.childNodes.item(i),true);}
return _24b;}
return ((tmp.document)&&(tmp.document.firstChild?tmp.document.firstChild:tmp));}}}
return null;};dojo.dom.prependChild=function(node,_24e){if(_24e.firstChild){_24e.insertBefore(node,_24e.firstChild);}else{_24e.appendChild(node);}
return true;};dojo.dom.insertBefore=function(node,ref,_251){if((_251!=true)&&(node===ref||node.nextSibling===ref)){return false;}
var _252=ref.parentNode;_252.insertBefore(node,ref);return true;};dojo.dom.insertAfter=function(node,ref,_255){var pn=ref.parentNode;if(ref==pn.lastChild){if((_255!=true)&&(node===ref)){return false;}
pn.appendChild(node);}else{return this.insertBefore(node,ref.nextSibling,_255);}
return true;};dojo.dom.insertAtPosition=function(node,ref,_259){if((!node)||(!ref)||(!_259)){return false;}
switch(_259.toLowerCase()){case "before":
return dojo.dom.insertBefore(node,ref);case "after":
return dojo.dom.insertAfter(node,ref);case "first":
if(ref.firstChild){return dojo.dom.insertBefore(node,ref.firstChild);}else{ref.appendChild(node);return true;}
break;default:
ref.appendChild(node);return true;}};dojo.dom.insertAtIndex=function(node,_25b,_25c){var _25d=_25b.childNodes;if(!_25d.length){_25b.appendChild(node);return true;}
var _25e=null;for(var i=0;i<_25d.length;i++){var _260=_25d.item(i)["getAttribute"]?parseInt(_25d.item(i).getAttribute("dojoinsertionindex")):-1;if(_260<_25c){_25e=_25d.item(i);}}
if(_25e){return dojo.dom.insertAfter(node,_25e);}else{return dojo.dom.insertBefore(node,_25d.item(0));}};dojo.dom.textContent=function(node,text){if(arguments.length>1){var _263=dojo.doc();dojo.dom.replaceChildren(node,_263.createTextNode(text));return text;}else{if(node.textContent!=undefined){return node.textContent;}
var _264="";if(node==null){return _264;}
for(var i=0;i<node.childNodes.length;i++){switch(node.childNodes[i].nodeType){case 1:
case 5:
_264+=dojo.dom.textContent(node.childNodes[i]);break;case 3:
case 2:
case 4:
_264+=node.childNodes[i].nodeValue;break;default:
break;}}
return _264;}};dojo.dom.hasParent=function(node){return Boolean(node&&node.parentNode&&dojo.dom.isNode(node.parentNode));};dojo.dom.isTag=function(node){if(node&&node.tagName){for(var i=1;i<arguments.length;i++){if(node.tagName==String(arguments[i])){return String(arguments[i]);}}}
return "";};dojo.dom.setAttributeNS=function(elem,_26a,_26b,_26c){if(elem==null||((elem==undefined)&&(typeof elem=="undefined"))){dojo.raise("No element given to dojo.dom.setAttributeNS");}
if(!((elem.setAttributeNS==undefined)&&(typeof elem.setAttributeNS=="undefined"))){elem.setAttributeNS(_26a,_26b,_26c);}else{var _26d=elem.ownerDocument;var _26e=_26d.createNode(2,_26b,_26a);_26e.nodeValue=_26c;elem.setAttributeNode(_26e);}};dojo.provide("dojo.string.common");dojo.string.trim=function(str,wh){if(!str.replace){return str;}
if(!str.length){return str;}
var re=(wh>0)?(/^\s+/):(wh<0)?(/\s+$/):(/^\s+|\s+$/g);return str.replace(re,"");};dojo.string.trimStart=function(str){return dojo.string.trim(str,1);};dojo.string.trimEnd=function(str){return dojo.string.trim(str,-1);};dojo.string.repeat=function(str,_275,_276){var out="";for(var i=0;i<_275;i++){out+=str;if(_276&&i<_275-1){out+=_276;}}
return out;};dojo.string.pad=function(str,len,c,dir){var out=String(str);if(!c){c="0";}
if(!dir){dir=1;}
while(out.length<len){if(dir>0){out=c+out;}else{out+=c;}}
return out;};dojo.string.padLeft=function(str,len,c){return dojo.string.pad(str,len,c,1);};dojo.string.padRight=function(str,len,c){return dojo.string.pad(str,len,c,-1);};dojo.provide("dojo.string");dojo.provide("dojo.io.common");dojo.io.transports=[];dojo.io.hdlrFuncNames=["load","error","timeout"];dojo.io.Request=function(url,_285,_286,_287){if((arguments.length==1)&&(arguments[0].constructor==Object)){this.fromKwArgs(arguments[0]);}else{this.url=url;if(_285){this.mimetype=_285;}
if(_286){this.transport=_286;}
if(arguments.length>=4){this.changeUrl=_287;}}};dojo.lang.extend(dojo.io.Request,{url:"",mimetype:"text/plain",method:"GET",content:undefined,transport:undefined,changeUrl:undefined,formNode:undefined,sync:false,bindSuccess:false,useCache:false,preventCache:false,load:function(type,data,_28a,_28b){},error:function(type,_28d,_28e,_28f){},timeout:function(type,_291,_292,_293){},handle:function(type,data,_296,_297){},timeoutSeconds:0,abort:function(){},fromKwArgs:function(_298){if(_298["url"]){_298.url=_298.url.toString();}
if(_298["formNode"]){_298.formNode=dojo.byId(_298.formNode);}
if(!_298["method"]&&_298["formNode"]&&_298["formNode"].method){_298.method=_298["formNode"].method;}
if(!_298["handle"]&&_298["handler"]){_298.handle=_298.handler;}
if(!_298["load"]&&_298["loaded"]){_298.load=_298.loaded;}
if(!_298["changeUrl"]&&_298["changeURL"]){_298.changeUrl=_298.changeURL;}
_298.encoding=dojo.lang.firstValued(_298["encoding"],djConfig["bindEncoding"],"");_298.sendTransport=dojo.lang.firstValued(_298["sendTransport"],djConfig["ioSendTransport"],false);var _299=dojo.lang.isFunction;for(var x=0;x<dojo.io.hdlrFuncNames.length;x++){var fn=dojo.io.hdlrFuncNames[x];if(_298[fn]&&_299(_298[fn])){continue;}
if(_298["handle"]&&_299(_298["handle"])){_298[fn]=_298.handle;}}
dojo.lang.mixin(this,_298);}});dojo.io.Error=function(msg,type,num){this.message=msg;this.type=type||"unknown";this.number=num||0;};dojo.io.transports.addTransport=function(name){this.push(name);this[name]=dojo.io[name];};dojo.io.bind=function(_2a0){if(!(_2a0 instanceof dojo.io.Request)){try{_2a0=new dojo.io.Request(_2a0);}
catch(e){dojo.debug(e);}}
var _2a1="";if(_2a0["transport"]){_2a1=_2a0["transport"];if(!this[_2a1]){dojo.io.sendBindError(_2a0,"No dojo.io.bind() transport with name '"+_2a0["transport"]+"'.");return _2a0;}
if(!this[_2a1].canHandle(_2a0)){dojo.io.sendBindError(_2a0,"dojo.io.bind() transport with name '"+_2a0["transport"]+"' cannot handle this type of request.");return _2a0;}}else{for(var x=0;x<dojo.io.transports.length;x++){var tmp=dojo.io.transports[x];if((this[tmp])&&(this[tmp].canHandle(_2a0))){_2a1=tmp;break;}}
if(_2a1==""){dojo.io.sendBindError(_2a0,"None of the loaded transports for dojo.io.bind()"+" can handle the request.");return _2a0;}}
this[_2a1].bind(_2a0);_2a0.bindSuccess=true;return _2a0;};dojo.io.sendBindError=function(_2a4,_2a5){if((typeof _2a4.error=="function"||typeof _2a4.handle=="function")&&(typeof setTimeout=="function"||typeof setTimeout=="object")){var _2a6=new dojo.io.Error(_2a5);setTimeout(function(){_2a4[(typeof _2a4.error=="function")?"error":"handle"]("error",_2a6,null,_2a4);},50);}else{dojo.raise(_2a5);}};dojo.io.queueBind=function(_2a7){if(!(_2a7 instanceof dojo.io.Request)){try{_2a7=new dojo.io.Request(_2a7);}
catch(e){dojo.debug(e);}}
var _2a8=_2a7.load;_2a7.load=function(){dojo.io._queueBindInFlight=false;var ret=_2a8.apply(this,arguments);dojo.io._dispatchNextQueueBind();return ret;};var _2aa=_2a7.error;_2a7.error=function(){dojo.io._queueBindInFlight=false;var ret=_2aa.apply(this,arguments);dojo.io._dispatchNextQueueBind();return ret;};dojo.io._bindQueue.push(_2a7);dojo.io._dispatchNextQueueBind();return _2a7;};dojo.io._dispatchNextQueueBind=function(){if(!dojo.io._queueBindInFlight){dojo.io._queueBindInFlight=true;if(dojo.io._bindQueue.length>0){dojo.io.bind(dojo.io._bindQueue.shift());}else{dojo.io._queueBindInFlight=false;}}};dojo.io._bindQueue=[];dojo.io._queueBindInFlight=false;dojo.io.argsFromMap=function(map,_2ad,last){var enc=/utf/i.test(_2ad||"")?encodeURIComponent:dojo.string.encodeAscii;var _2b0=[];var _2b1=new Object();for(var name in map){var _2b3=function(elt){var val=enc(name)+"="+enc(elt);_2b0[(last==name)?"push":"unshift"](val);};if(!_2b1[name]){var _2b6=map[name];if(dojo.lang.isArray(_2b6)){dojo.lang.forEach(_2b6,_2b3);}else{_2b3(_2b6);}}}
return _2b0.join("&");};dojo.io.setIFrameSrc=function(_2b7,src,_2b9){try{var r=dojo.render.html;if(!_2b9){if(r.safari){_2b7.location=src;}else{frames[_2b7.name].location=src;}}else{var idoc;if(r.ie){idoc=_2b7.contentWindow.document;}else{if(r.safari){idoc=_2b7.document;}else{idoc=_2b7.contentWindow;}}
if(!idoc){_2b7.location=src;return;}else{idoc.location.replace(src);}}}
catch(e){dojo.debug(e);dojo.debug("setIFrameSrc: "+e);}};dojo.provide("dojo.string.extras");dojo.string.substituteParams=function(_2bc,hash){var map=(typeof hash=="object")?hash:dojo.lang.toArray(arguments,1);return _2bc.replace(/\%\{(\w+)\}/g,function(_2bf,key){if(typeof (map[key])!="undefined"&&map[key]!=null){return map[key];}
dojo.raise("Substitution not found: "+key);});};dojo.string.capitalize=function(str){if(!dojo.lang.isString(str)){return "";}
if(arguments.length==0){str=this;}
var _2c2=str.split(" ");for(var i=0;i<_2c2.length;i++){_2c2[i]=_2c2[i].charAt(0).toUpperCase()+_2c2[i].substring(1);}
return _2c2.join(" ");};dojo.string.isBlank=function(str){if(!dojo.lang.isString(str)){return true;}
return (dojo.string.trim(str).length==0);};dojo.string.encodeAscii=function(str){if(!dojo.lang.isString(str)){return str;}
var ret="";var _2c7=escape(str);var _2c8,re=/%u([0-9A-F]{4})/i;while((_2c8=_2c7.match(re))){var num=Number("0x"+_2c8[1]);var _2cb=escape("&#"+num+";");ret+=_2c7.substring(0,_2c8.index)+_2cb;_2c7=_2c7.substring(_2c8.index+_2c8[0].length);}
ret+=_2c7.replace(/\+/g,"%2B");return ret;};dojo.string.escape=function(type,str){var args=dojo.lang.toArray(arguments,1);switch(type.toLowerCase()){case "xml":
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
return str;}};dojo.string.escapeXml=function(str,_2d0){str=str.replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");if(!_2d0){str=str.replace(/'/gm,"&#39;");}
return str;};dojo.string.escapeSql=function(str){return str.replace(/'/gm,"''");};dojo.string.escapeRegExp=function(str){return str.replace(/\\/gm,"\\\\").replace(/([\f\b\n\t\r[\^$|?*+(){}])/gm,"\\$1");};dojo.string.escapeJavaScript=function(str){return str.replace(/(["'\f\b\n\t\r])/gm,"\\$1");};dojo.string.escapeString=function(str){return ("\""+str.replace(/(["\\])/g,"\\$1")+"\"").replace(/[\f]/g,"\\f").replace(/[\b]/g,"\\b").replace(/[\n]/g,"\\n").replace(/[\t]/g,"\\t").replace(/[\r]/g,"\\r");};dojo.string.summary=function(str,len){if(!len||str.length<=len){return str;}
return str.substring(0,len).replace(/\.+$/,"")+"...";};dojo.string.endsWith=function(str,end,_2d9){if(_2d9){str=str.toLowerCase();end=end.toLowerCase();}
if((str.length-end.length)<0){return false;}
return str.lastIndexOf(end)==str.length-end.length;};dojo.string.endsWithAny=function(str){for(var i=1;i<arguments.length;i++){if(dojo.string.endsWith(str,arguments[i])){return true;}}
return false;};dojo.string.startsWith=function(str,_2dd,_2de){if(_2de){str=str.toLowerCase();_2dd=_2dd.toLowerCase();}
return str.indexOf(_2dd)==0;};dojo.string.startsWithAny=function(str){for(var i=1;i<arguments.length;i++){if(dojo.string.startsWith(str,arguments[i])){return true;}}
return false;};dojo.string.has=function(str){for(var i=1;i<arguments.length;i++){if(str.indexOf(arguments[i])>-1){return true;}}
return false;};dojo.string.normalizeNewlines=function(text,_2e4){if(_2e4=="\n"){text=text.replace(/\r\n/g,"\n");text=text.replace(/\r/g,"\n");}else{if(_2e4=="\r"){text=text.replace(/\r\n/g,"\r");text=text.replace(/\n/g,"\r");}else{text=text.replace(/([^\r])\n/g,"$1\r\n").replace(/\r([^\n])/g,"\r\n$1");}}
return text;};dojo.string.splitEscaped=function(str,_2e6){var _2e7=[];for(var i=0,_2e9=0;i<str.length;i++){if(str.charAt(i)=="\\"){i++;continue;}
if(str.charAt(i)==_2e6){_2e7.push(str.substring(_2e9,i));_2e9=i+1;}}
_2e7.push(str.substr(_2e9));return _2e7;};dojo.provide("dojo.undo.browser");try{if((!djConfig["preventBackButtonFix"])&&(!dojo.hostenv.post_load_)){document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"'></iframe>");}}
catch(e){}
if(dojo.render.html.opera){dojo.debug("Opera is not supported with dojo.undo.browser, so back/forward detection will not work.");}
dojo.undo.browser={initialHref:(!dj_undef("window"))?window.location.href:"",initialHash:(!dj_undef("window"))?window.location.hash:"",moveForward:false,historyStack:[],forwardStack:[],historyIframe:null,bookmarkAnchor:null,locationTimer:null,setInitialState:function(args){this.initialState=this._createState(this.initialHref,args,this.initialHash);},addToHistory:function(args){this.forwardStack=[];var hash=null;var url=null;if(!this.historyIframe){this.historyIframe=window.frames["djhistory"];}
if(!this.bookmarkAnchor){this.bookmarkAnchor=document.createElement("a");dojo.body().appendChild(this.bookmarkAnchor);this.bookmarkAnchor.style.display="none";}
if(args["changeUrl"]){hash="#"+((args["changeUrl"]!==true)?args["changeUrl"]:(new Date()).getTime());if(this.historyStack.length==0&&this.initialState.urlHash==hash){this.initialState=this._createState(url,args,hash);return;}else{if(this.historyStack.length>0&&this.historyStack[this.historyStack.length-1].urlHash==hash){this.historyStack[this.historyStack.length-1]=this._createState(url,args,hash);return;}}
this.changingUrl=true;setTimeout("window.location.href = '"+hash+"'; dojo.undo.browser.changingUrl = false;",1);this.bookmarkAnchor.href=hash;if(dojo.render.html.ie){url=this._loadIframeHistory();var _2ee=args["back"]||args["backButton"]||args["handle"];var tcb=function(_2f0){if(window.location.hash!=""){setTimeout("window.location.href = '"+hash+"';",1);}
_2ee.apply(this,[_2f0]);};if(args["back"]){args.back=tcb;}else{if(args["backButton"]){args.backButton=tcb;}else{if(args["handle"]){args.handle=tcb;}}}
var _2f1=args["forward"]||args["forwardButton"]||args["handle"];var tfw=function(_2f3){if(window.location.hash!=""){window.location.href=hash;}
if(_2f1){_2f1.apply(this,[_2f3]);}};if(args["forward"]){args.forward=tfw;}else{if(args["forwardButton"]){args.forwardButton=tfw;}else{if(args["handle"]){args.handle=tfw;}}}}else{if(dojo.render.html.moz){if(!this.locationTimer){this.locationTimer=setInterval("dojo.undo.browser.checkLocation();",200);}}}}else{url=this._loadIframeHistory();}
this.historyStack.push(this._createState(url,args,hash));},checkLocation:function(){if(!this.changingUrl){var hsl=this.historyStack.length;if((window.location.hash==this.initialHash||window.location.href==this.initialHref)&&(hsl==1)){this.handleBackButton();return;}
if(this.forwardStack.length>0){if(this.forwardStack[this.forwardStack.length-1].urlHash==window.location.hash){this.handleForwardButton();return;}}
if((hsl>=2)&&(this.historyStack[hsl-2])){if(this.historyStack[hsl-2].urlHash==window.location.hash){this.handleBackButton();return;}}}},iframeLoaded:function(evt,_2f6){if(!dojo.render.html.opera){var _2f7=this._getUrlQuery(_2f6.href);if(_2f7==null){if(this.historyStack.length==1){this.handleBackButton();}
return;}
if(this.moveForward){this.moveForward=false;return;}
if(this.historyStack.length>=2&&_2f7==this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){this.handleBackButton();}else{if(this.forwardStack.length>0&&_2f7==this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){this.handleForwardButton();}}}},handleBackButton:function(){var _2f8=this.historyStack.pop();if(!_2f8){return;}
var last=this.historyStack[this.historyStack.length-1];if(!last&&this.historyStack.length==0){last=this.initialState;}
if(last){if(last.kwArgs["back"]){last.kwArgs["back"]();}else{if(last.kwArgs["backButton"]){last.kwArgs["backButton"]();}else{if(last.kwArgs["handle"]){last.kwArgs.handle("back");}}}}
this.forwardStack.push(_2f8);},handleForwardButton:function(){var last=this.forwardStack.pop();if(!last){return;}
if(last.kwArgs["forward"]){last.kwArgs.forward();}else{if(last.kwArgs["forwardButton"]){last.kwArgs.forwardButton();}else{if(last.kwArgs["handle"]){last.kwArgs.handle("forward");}}}
this.historyStack.push(last);},_createState:function(url,args,hash){return {"url":url,"kwArgs":args,"urlHash":hash};},_getUrlQuery:function(url){var _2ff=url.split("?");if(_2ff.length<2){return null;}else{return _2ff[1];}},_loadIframeHistory:function(){var url=dojo.hostenv.getBaseScriptUri()+"iframe_history.html?"+(new Date()).getTime();this.moveForward=true;dojo.io.setIFrameSrc(this.historyIframe,url,false);return url;}};dojo.provide("dojo.io.BrowserIO");if(!dj_undef("window")){dojo.io.checkChildrenForFile=function(node){var _302=false;var _303=node.getElementsByTagName("input");dojo.lang.forEach(_303,function(_304){if(_302){return;}
if(_304.getAttribute("type")=="file"){_302=true;}});return _302;};dojo.io.formHasFile=function(_305){return dojo.io.checkChildrenForFile(_305);};dojo.io.updateNode=function(node,_307){node=dojo.byId(node);var args=_307;if(dojo.lang.isString(_307)){args={url:_307};}
args.mimetype="text/html";args.load=function(t,d,e){while(node.firstChild){if(dojo["event"]){try{dojo.event.browser.clean(node.firstChild);}
catch(e){}}
node.removeChild(node.firstChild);}
node.innerHTML=d;};dojo.io.bind(args);};dojo.io.formFilter=function(node){var type=(node.type||"").toLowerCase();return !node.disabled&&node.name&&!dojo.lang.inArray(["file","submit","image","reset","button"],type);};dojo.io.encodeForm=function(_30e,_30f,_310){if((!_30e)||(!_30e.tagName)||(!_30e.tagName.toLowerCase()=="form")){dojo.raise("Attempted to encode a non-form element.");}
if(!_310){_310=dojo.io.formFilter;}
var enc=/utf/i.test(_30f||"")?encodeURIComponent:dojo.string.encodeAscii;var _312=[];for(var i=0;i<_30e.elements.length;i++){var elm=_30e.elements[i];if(!elm||elm.tagName.toLowerCase()=="fieldset"||!_310(elm)){continue;}
var name=enc(elm.name);var type=elm.type.toLowerCase();if(type=="select-multiple"){for(var j=0;j<elm.options.length;j++){if(elm.options[j].selected){_312.push(name+"="+enc(elm.options[j].value));}}}else{if(dojo.lang.inArray(["radio","checkbox"],type)){if(elm.checked){_312.push(name+"="+enc(elm.value));}}else{_312.push(name+"="+enc(elm.value));}}}
var _318=_30e.getElementsByTagName("input");for(var i=0;i<_318.length;i++){var _319=_318[i];if(_319.type.toLowerCase()=="image"&&_319.form==_30e&&_310(_319)){var name=enc(_319.name);_312.push(name+"="+enc(_319.value));_312.push(name+".x=0");_312.push(name+".y=0");}}
return _312.join("&")+"&";};dojo.io.FormBind=function(args){this.bindArgs={};if(args&&args.formNode){this.init(args);}else{if(args){this.init({formNode:args});}}};dojo.lang.extend(dojo.io.FormBind,{form:null,bindArgs:null,clickedButton:null,init:function(args){var form=dojo.byId(args.formNode);if(!form||!form.tagName||form.tagName.toLowerCase()!="form"){throw new Error("FormBind: Couldn't apply, invalid form");}else{if(this.form==form){return;}else{if(this.form){throw new Error("FormBind: Already applied to a form");}}}
dojo.lang.mixin(this.bindArgs,args);this.form=form;this.connect(form,"onsubmit","submit");for(var i=0;i<form.elements.length;i++){var node=form.elements[i];if(node&&node.type&&dojo.lang.inArray(["submit","button"],node.type.toLowerCase())){this.connect(node,"onclick","click");}}
var _31f=form.getElementsByTagName("input");for(var i=0;i<_31f.length;i++){var _320=_31f[i];if(_320.type.toLowerCase()=="image"&&_320.form==form){this.connect(_320,"onclick","click");}}},onSubmit:function(form){return true;},submit:function(e){e.preventDefault();if(this.onSubmit(this.form)){dojo.io.bind(dojo.lang.mixin(this.bindArgs,{formFilter:dojo.lang.hitch(this,"formFilter")}));}},click:function(e){var node=e.currentTarget;if(node.disabled){return;}
this.clickedButton=node;},formFilter:function(node){var type=(node.type||"").toLowerCase();var _327=false;if(node.disabled||!node.name){_327=false;}else{if(dojo.lang.inArray(["submit","button","image"],type)){if(!this.clickedButton){this.clickedButton=node;}
_327=node==this.clickedButton;}else{_327=!dojo.lang.inArray(["file","submit","reset","button"],type);}}
return _327;},connect:function(_328,_329,_32a){if(dojo.evalObjPath("dojo.event.connect")){dojo.event.connect(_328,_329,this,_32a);}else{var fcn=dojo.lang.hitch(this,_32a);_328[_329]=function(e){if(!e){e=window.event;}
if(!e.currentTarget){e.currentTarget=e.srcElement;}
if(!e.preventDefault){e.preventDefault=function(){window.event.returnValue=false;};}
fcn(e);};}}});dojo.io.XMLHTTPTransport=new function(){var _32d=this;var _32e={};this.useCache=false;this.preventCache=false;function getCacheKey(url,_330,_331){return url+"|"+_330+"|"+_331.toLowerCase();}
function addToCache(url,_333,_334,http){_32e[getCacheKey(url,_333,_334)]=http;}
function getFromCache(url,_337,_338){return _32e[getCacheKey(url,_337,_338)];}
this.clearCache=function(){_32e={};};function doLoad(_339,http,url,_33c,_33d){if(((http.status>=200)&&(http.status<300))||(http.status==304)||(location.protocol=="file:"&&(http.status==0||http.status==undefined))||(location.protocol=="chrome:"&&(http.status==0||http.status==undefined))){var ret;if(_339.method.toLowerCase()=="head"){var _33f=http.getAllResponseHeaders();ret={};ret.toString=function(){return _33f;};var _340=_33f.split(/[\r\n]+/g);for(var i=0;i<_340.length;i++){var pair=_340[i].match(/^([^:]+)\s*:\s*(.+)$/i);if(pair){ret[pair[1]]=pair[2];}}}else{if(_339.mimetype=="text/javascript"){try{ret=dj_eval(http.responseText);}
catch(e){dojo.debug(e);dojo.debug(http.responseText);ret=null;}}else{if(_339.mimetype=="text/json"||_339.mimetype=="application/json"){try{ret=dj_eval("("+http.responseText+")");}
catch(e){dojo.debug(e);dojo.debug(http.responseText);ret=false;}}else{if((_339.mimetype=="application/xml")||(_339.mimetype=="text/xml")){ret=http.responseXML;if(!ret||typeof ret=="string"||!http.getResponseHeader("Content-Type")){ret=dojo.dom.createDocumentFromText(http.responseText);}}else{ret=http.responseText;}}}}
if(_33d){addToCache(url,_33c,_339.method,http);}
_339[(typeof _339.load=="function")?"load":"handle"]("load",ret,http,_339);}else{var _343=new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);_339[(typeof _339.error=="function")?"error":"handle"]("error",_343,http,_339);}}
function setHeaders(http,_345){if(_345["headers"]){for(var _346 in _345["headers"]){if(_346.toLowerCase()=="content-type"&&!_345["contentType"]){_345["contentType"]=_345["headers"][_346];}else{http.setRequestHeader(_346,_345["headers"][_346]);}}}}
this.inFlight=[];this.inFlightTimer=null;this.startWatchingInFlight=function(){if(!this.inFlightTimer){this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);}};this.watchInFlight=function(){var now=null;if(!dojo.hostenv._blockAsync&&!_32d._blockAsync){for(var x=this.inFlight.length-1;x>=0;x--){try{var tif=this.inFlight[x];if(!tif||tif.http._aborted||!tif.http.readyState){this.inFlight.splice(x,1);continue;}
if(4==tif.http.readyState){this.inFlight.splice(x,1);doLoad(tif.req,tif.http,tif.url,tif.query,tif.useCache);}else{if(tif.startTime){if(!now){now=(new Date()).getTime();}
if(tif.startTime+(tif.req.timeoutSeconds*1000)<now){if(typeof tif.http.abort=="function"){tif.http.abort();}
this.inFlight.splice(x,1);tif.req[(typeof tif.req.timeout=="function")?"timeout":"handle"]("timeout",null,tif.http,tif.req);}}}}
catch(e){try{var _34a=new dojo.io.Error("XMLHttpTransport.watchInFlight Error: "+e);tif.req[(typeof tif.req.error=="function")?"error":"handle"]("error",_34a,tif.http,tif.req);}
catch(e2){dojo.debug("XMLHttpTransport error callback failed: "+e2);}}}}
clearTimeout(this.inFlightTimer);if(this.inFlight.length==0){this.inFlightTimer=null;return;}
this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);};var _34b=dojo.hostenv.getXmlhttpObject()?true:false;this.canHandle=function(_34c){return _34b&&dojo.lang.inArray(["text/plain","text/html","application/xml","text/xml","text/javascript","text/json","application/json"],(_34c["mimetype"].toLowerCase()||""))&&!(_34c["formNode"]&&dojo.io.formHasFile(_34c["formNode"]));};this.multipartBoundary="45309FFF-BD65-4d50-99C9-36986896A96F";this.bind=function(_34d){if(!_34d["url"]){if(!_34d["formNode"]&&(_34d["backButton"]||_34d["back"]||_34d["changeUrl"]||_34d["watchForURL"])&&(!djConfig.preventBackButtonFix)){dojo.deprecated("Using dojo.io.XMLHTTPTransport.bind() to add to browser history without doing an IO request","Use dojo.undo.browser.addToHistory() instead.","0.4");dojo.undo.browser.addToHistory(_34d);return true;}}
var url=_34d.url;var _34f="";if(_34d["formNode"]){var ta=_34d.formNode.getAttribute("action");if((ta)&&(!_34d["url"])){url=ta;}
var tp=_34d.formNode.getAttribute("method");if((tp)&&(!_34d["method"])){_34d.method=tp;}
_34f+=dojo.io.encodeForm(_34d.formNode,_34d.encoding,_34d["formFilter"]);}
if(url.indexOf("#")>-1){dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);url=url.split("#")[0];}
if(_34d["file"]){_34d.method="post";}
if(!_34d["method"]){_34d.method="get";}
if(_34d.method.toLowerCase()=="get"){_34d.multipart=false;}else{if(_34d["file"]){_34d.multipart=true;}else{if(!_34d["multipart"]){_34d.multipart=false;}}}
if(_34d["backButton"]||_34d["back"]||_34d["changeUrl"]){dojo.undo.browser.addToHistory(_34d);}
var _352=_34d["content"]||{};if(_34d.sendTransport){_352["dojo.transport"]="xmlhttp";}
do{if(_34d.postContent){_34f=_34d.postContent;break;}
if(_352){_34f+=dojo.io.argsFromMap(_352,_34d.encoding);}
if(_34d.method.toLowerCase()=="get"||!_34d.multipart){break;}
var t=[];if(_34f.length){var q=_34f.split("&");for(var i=0;i<q.length;++i){if(q[i].length){var p=q[i].split("=");t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+p[0]+"\"","",p[1]);}}}
if(_34d.file){if(dojo.lang.isArray(_34d.file)){for(var i=0;i<_34d.file.length;++i){var o=_34d.file[i];t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);}}else{var o=_34d.file;t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);}}
if(t.length){t.push("--"+this.multipartBoundary+"--","");_34f=t.join("\r\n");}}while(false);var _358=_34d["sync"]?false:true;var _359=_34d["preventCache"]||(this.preventCache==true&&_34d["preventCache"]!=false);var _35a=_34d["useCache"]==true||(this.useCache==true&&_34d["useCache"]!=false);if(!_359&&_35a){var _35b=getFromCache(url,_34f,_34d.method);if(_35b){doLoad(_34d,_35b,url,_34f,false);return;}}
var http=dojo.hostenv.getXmlhttpObject(_34d);var _35d=false;if(_358){var _35e=this.inFlight.push({"req":_34d,"http":http,"url":url,"query":_34f,"useCache":_35a,"startTime":_34d.timeoutSeconds?(new Date()).getTime():0});this.startWatchingInFlight();}else{_32d._blockAsync=true;}
if(_34d.method.toLowerCase()=="post"){if(!_34d.user){http.open("POST",url,_358);}else{http.open("POST",url,_358,_34d.user,_34d.password);}
setHeaders(http,_34d);http.setRequestHeader("Content-Type",_34d.multipart?("multipart/form-data; boundary="+this.multipartBoundary):(_34d.contentType||"application/x-www-form-urlencoded"));try{http.send(_34f);}
catch(e){if(typeof http.abort=="function"){http.abort();}
doLoad(_34d,{status:404},url,_34f,_35a);}}else{var _35f=url;if(_34f!=""){_35f+=(_35f.indexOf("?")>-1?"&":"?")+_34f;}
if(_359){_35f+=(dojo.string.endsWithAny(_35f,"?","&")?"":(_35f.indexOf("?")>-1?"&":"?"))+"dojo.preventCache="+new Date().valueOf();}
if(!_34d.user){http.open(_34d.method.toUpperCase(),_35f,_358);}else{http.open(_34d.method.toUpperCase(),_35f,_358,_34d.user,_34d.password);}
setHeaders(http,_34d);try{http.send(null);}
catch(e){if(typeof http.abort=="function"){http.abort();}
doLoad(_34d,{status:404},url,_34f,_35a);}}
if(!_358){doLoad(_34d,http,url,_34f,_35a);_32d._blockAsync=false;}
_34d.abort=function(){try{http._aborted=true;}
catch(e){}
return http.abort();};return;};dojo.io.transports.addTransport("XMLHTTPTransport");};}
dojo.provide("dojo.io.cookie");dojo.io.cookie.setCookie=function(name,_361,days,path,_364,_365){var _366=-1;if(typeof days=="number"&&days>=0){var d=new Date();d.setTime(d.getTime()+(days*24*60*60*1000));_366=d.toGMTString();}
_361=escape(_361);document.cookie=name+"="+_361+";"+(_366!=-1?" expires="+_366+";":"")+(path?"path="+path:"")+(_364?"; domain="+_364:"")+(_365?"; secure":"");};dojo.io.cookie.set=dojo.io.cookie.setCookie;dojo.io.cookie.getCookie=function(name){var idx=document.cookie.lastIndexOf(name+"=");if(idx==-1){return null;}
var _36a=document.cookie.substring(idx+name.length+1);var end=_36a.indexOf(";");if(end==-1){end=_36a.length;}
_36a=_36a.substring(0,end);_36a=unescape(_36a);return _36a;};dojo.io.cookie.get=dojo.io.cookie.getCookie;dojo.io.cookie.deleteCookie=function(name){dojo.io.cookie.setCookie(name,"-",0);};dojo.io.cookie.setObjectCookie=function(name,obj,days,path,_371,_372,_373){if(arguments.length==5){_373=_371;_371=null;_372=null;}
var _374=[],_375,_376="";if(!_373){_375=dojo.io.cookie.getObjectCookie(name);}
if(days>=0){if(!_375){_375={};}
for(var prop in obj){if(prop==null){delete _375[prop];}else{if(typeof obj[prop]=="string"||typeof obj[prop]=="number"){_375[prop]=obj[prop];}}}
prop=null;for(var prop in _375){_374.push(escape(prop)+"="+escape(_375[prop]));}
_376=_374.join("&");}
dojo.io.cookie.setCookie(name,_376,days,path,_371,_372);};dojo.io.cookie.getObjectCookie=function(name){var _379=null,_37a=dojo.io.cookie.getCookie(name);if(_37a){_379={};var _37b=_37a.split("&");for(var i=0;i<_37b.length;i++){var pair=_37b[i].split("=");var _37e=pair[1];if(isNaN(_37e)){_37e=unescape(pair[1]);}
_379[unescape(pair[0])]=_37e;}}
return _379;};dojo.io.cookie.isSupported=function(){if(typeof navigator.cookieEnabled!="boolean"){dojo.io.cookie.setCookie("__TestingYourBrowserForCookieSupport__","CookiesAllowed",90,null);var _37f=dojo.io.cookie.getCookie("__TestingYourBrowserForCookieSupport__");navigator.cookieEnabled=(_37f=="CookiesAllowed");if(navigator.cookieEnabled){this.deleteCookie("__TestingYourBrowserForCookieSupport__");}}
return navigator.cookieEnabled;};if(!dojo.io.cookies){dojo.io.cookies=dojo.io.cookie;}
dojo.provide("dojo.date.common");dojo.date.setDayOfYear=function(_380,_381){_380.setMonth(0);_380.setDate(_381);return _380;};dojo.date.getDayOfYear=function(_382){var _383=_382.getFullYear();var _384=new Date(_383-1,11,31);return Math.floor((_382.getTime()-_384.getTime())/86400000);};dojo.date.setWeekOfYear=function(_385,week,_387){if(arguments.length==1){_387=0;}
dojo.unimplemented("dojo.date.setWeekOfYear");};dojo.date.getWeekOfYear=function(_388,_389){if(arguments.length==1){_389=0;}
var _38a=new Date(_388.getFullYear(),0,1);var day=_38a.getDay();_38a.setDate(_38a.getDate()-day+_389-(day>_389?7:0));return Math.floor((_388.getTime()-_38a.getTime())/604800000);};dojo.date.setIsoWeekOfYear=function(_38c,week,_38e){if(arguments.length==1){_38e=1;}
dojo.unimplemented("dojo.date.setIsoWeekOfYear");};dojo.date.getIsoWeekOfYear=function(_38f,_390){if(arguments.length==1){_390=1;}
dojo.unimplemented("dojo.date.getIsoWeekOfYear");};dojo.date.shortTimezones=["IDLW","BET","HST","MART","AKST","PST","MST","CST","EST","AST","NFT","BST","FST","AT","GMT","CET","EET","MSK","IRT","GST","AFT","AGTT","IST","NPT","ALMT","MMT","JT","AWST","JST","ACST","AEST","LHST","VUT","NFT","NZT","CHAST","PHOT","LINT"];dojo.date.timezoneOffsets=[-720,-660,-600,-570,-540,-480,-420,-360,-300,-240,-210,-180,-120,-60,0,60,120,180,210,240,270,300,330,345,360,390,420,480,540,570,600,630,660,690,720,765,780,840];dojo.date.getDaysInMonth=function(_391){var _392=_391.getMonth();var days=[31,28,31,30,31,30,31,31,30,31,30,31];if(_392==1&&dojo.date.isLeapYear(_391)){return 29;}else{return days[_392];}};dojo.date.isLeapYear=function(_394){var year=_394.getFullYear();return (year%400==0)?true:(year%100==0)?false:(year%4==0)?true:false;};dojo.date.getTimezoneName=function(_396){var str=_396.toString();var tz="";var _399;var pos=str.indexOf("(");if(pos>-1){pos++;tz=str.substring(pos,str.indexOf(")"));}else{var pat=/([A-Z\/]+) \d{4}$/;if((_399=str.match(pat))){tz=_399[1];}else{str=_396.toLocaleString();pat=/ ([A-Z\/]+)$/;if((_399=str.match(pat))){tz=_399[1];}}}
return tz=="AM"||tz=="PM"?"":tz;};dojo.date.getOrdinal=function(_39c){var date=_39c.getDate();if(date%100!=11&&date%10==1){return "st";}else{if(date%100!=12&&date%10==2){return "nd";}else{if(date%100!=13&&date%10==3){return "rd";}else{return "th";}}}};dojo.date.compareTypes={DATE:1,TIME:2};dojo.date.compare=function(_39e,_39f,_3a0){var dA=_39e;var dB=_39f||new Date();var now=new Date();with(dojo.date.compareTypes){var opt=_3a0||(DATE|TIME);var d1=new Date((opt&DATE)?dA.getFullYear():now.getFullYear(),(opt&DATE)?dA.getMonth():now.getMonth(),(opt&DATE)?dA.getDate():now.getDate(),(opt&TIME)?dA.getHours():0,(opt&TIME)?dA.getMinutes():0,(opt&TIME)?dA.getSeconds():0);var d2=new Date((opt&DATE)?dB.getFullYear():now.getFullYear(),(opt&DATE)?dB.getMonth():now.getMonth(),(opt&DATE)?dB.getDate():now.getDate(),(opt&TIME)?dB.getHours():0,(opt&TIME)?dB.getMinutes():0,(opt&TIME)?dB.getSeconds():0);}
if(d1.valueOf()>d2.valueOf()){return 1;}
if(d1.valueOf()<d2.valueOf()){return -1;}
return 0;};dojo.date.dateParts={YEAR:0,MONTH:1,DAY:2,HOUR:3,MINUTE:4,SECOND:5,MILLISECOND:6,QUARTER:7,WEEK:8,WEEKDAY:9};dojo.date.add=function(dt,_3a8,incr){if(typeof dt=="number"){dt=new Date(dt);}
function fixOvershoot(){if(sum.getDate()<dt.getDate()){sum.setDate(0);}}
var sum=new Date(dt);with(dojo.date.dateParts){switch(_3a8){case YEAR:
sum.setFullYear(dt.getFullYear()+incr);fixOvershoot();break;case QUARTER:
incr*=3;case MONTH:
sum.setMonth(dt.getMonth()+incr);fixOvershoot();break;case WEEK:
incr*=7;case DAY:
sum.setDate(dt.getDate()+incr);break;case WEEKDAY:
var dat=dt.getDate();var _3ac=0;var days=0;var strt=0;var trgt=0;var adj=0;var mod=incr%5;if(mod==0){days=(incr>0)?5:-5;_3ac=(incr>0)?((incr-5)/5):((incr+5)/5);}else{days=mod;_3ac=parseInt(incr/5);}
strt=dt.getDay();if(strt==6&&incr>0){adj=1;}else{if(strt==0&&incr<0){adj=-1;}}
trgt=(strt+days);if(trgt==0||trgt==6){adj=(incr>0)?2:-2;}
sum.setDate(dat+(7*_3ac)+days+adj);break;case HOUR:
sum.setHours(sum.getHours()+incr);break;case MINUTE:
sum.setMinutes(sum.getMinutes()+incr);break;case SECOND:
sum.setSeconds(sum.getSeconds()+incr);break;case MILLISECOND:
sum.setMilliseconds(sum.getMilliseconds()+incr);break;default:
break;}}
return sum;};dojo.date.diff=function(dtA,dtB,_3b4){if(typeof dtA=="number"){dtA=new Date(dtA);}
if(typeof dtB=="number"){dtB=new Date(dtB);}
var _3b5=dtB.getFullYear()-dtA.getFullYear();var _3b6=(dtB.getMonth()-dtA.getMonth())+(_3b5*12);var _3b7=dtB.getTime()-dtA.getTime();var _3b8=_3b7/1000;var _3b9=_3b8/60;var _3ba=_3b9/60;var _3bb=_3ba/24;var _3bc=_3bb/7;var _3bd=0;with(dojo.date.dateParts){switch(_3b4){case YEAR:
_3bd=_3b5;break;case QUARTER:
var mA=dtA.getMonth();var mB=dtB.getMonth();var qA=Math.floor(mA/3)+1;var qB=Math.floor(mB/3)+1;qB+=(_3b5*4);_3bd=qB-qA;break;case MONTH:
_3bd=_3b6;break;case WEEK:
_3bd=parseInt(_3bc);break;case DAY:
_3bd=_3bb;break;case WEEKDAY:
var days=Math.round(_3bb);var _3c3=parseInt(days/7);var mod=days%7;if(mod==0){days=_3c3*5;}else{var adj=0;var aDay=dtA.getDay();var bDay=dtB.getDay();_3c3=parseInt(days/7);mod=days%7;var _3c8=new Date(dtA);_3c8.setDate(_3c8.getDate()+(_3c3*7));var _3c9=_3c8.getDay();if(_3bb>0){switch(true){case aDay==6:
adj=-1;break;case aDay==0:
adj=0;break;case bDay==6:
adj=-1;break;case bDay==0:
adj=-2;break;case (_3c9+mod)>5:
adj=-2;break;default:
break;}}else{if(_3bb<0){switch(true){case aDay==6:
adj=0;break;case aDay==0:
adj=1;break;case bDay==6:
adj=2;break;case bDay==0:
adj=1;break;case (_3c9+mod)<0:
adj=2;break;default:
break;}}}
days+=adj;days-=(_3c3*2);}
_3bd=days;break;case HOUR:
_3bd=_3ba;break;case MINUTE:
_3bd=_3b9;break;case SECOND:
_3bd=_3b8;break;case MILLISECOND:
_3bd=_3b7;break;default:
break;}}
return Math.round(_3bd);};dojo.provide("dojo.date.supplemental");dojo.date.getFirstDayOfWeek=function(_3ca){var _3cb={mv:5,ae:6,af:6,bh:6,dj:6,dz:6,eg:6,er:6,et:6,iq:6,ir:6,jo:6,ke:6,kw:6,lb:6,ly:6,ma:6,om:6,qa:6,sa:6,sd:6,so:6,tn:6,ye:6,as:0,au:0,az:0,bw:0,ca:0,cn:0,fo:0,ge:0,gl:0,gu:0,hk:0,ie:0,il:0,is:0,jm:0,jp:0,kg:0,kr:0,la:0,mh:0,mo:0,mp:0,mt:0,nz:0,ph:0,pk:0,sg:0,th:0,tt:0,tw:0,um:0,us:0,uz:0,vi:0,za:0,zw:0,et:0,mw:0,ng:0,tj:0,gb:0,sy:4};_3ca=dojo.hostenv.normalizeLocale(_3ca);var _3cc=_3ca.split("-")[1];var dow=_3cb[_3cc];return (typeof dow=="undefined")?1:dow;};dojo.date.getWeekend=function(_3ce){var _3cf={eg:5,il:5,sy:5,"in":0,ae:4,bh:4,dz:4,iq:4,jo:4,kw:4,lb:4,ly:4,ma:4,om:4,qa:4,sa:4,sd:4,tn:4,ye:4};var _3d0={ae:5,bh:5,dz:5,iq:5,jo:5,kw:5,lb:5,ly:5,ma:5,om:5,qa:5,sa:5,sd:5,tn:5,ye:5,af:5,ir:5,eg:6,il:6,sy:6};_3ce=dojo.hostenv.normalizeLocale(_3ce);var _3d1=_3ce.split("-")[1];var _3d2=_3cf[_3d1];var end=_3d0[_3d1];if(typeof _3d2=="undefined"){_3d2=6;}
if(typeof end=="undefined"){end=0;}
return {start:_3d2,end:end};};dojo.date.isWeekend=function(_3d4,_3d5){var _3d6=dojo.date.getWeekend(_3d5);var day=(_3d4||new Date()).getDay();if(_3d6.end<_3d6.start){_3d6.end+=7;if(day<_3d6.start){day+=7;}}
return day>=_3d6.start&&day<=_3d6.end;};dojo.provide("dojo.i18n.common");dojo.i18n.getLocalization=function(_3d8,_3d9,_3da){dojo.hostenv.preloadLocalizations();_3da=dojo.hostenv.normalizeLocale(_3da);var _3db=_3da.split("-");var _3dc=[_3d8,"nls",_3d9].join(".");var _3dd=dojo.hostenv.findModule(_3dc,true);var _3de;for(var i=_3db.length;i>0;i--){var loc=_3db.slice(0,i).join("_");if(_3dd[loc]){_3de=_3dd[loc];break;}}
if(!_3de){_3de=_3dd.ROOT;}
if(_3de){var _3e1=function(){};_3e1.prototype=_3de;return new _3e1();}
dojo.raise("Bundle not found: "+_3d9+" in "+_3d8+" , locale="+_3da);};dojo.i18n.isLTR=function(_3e2){var lang=dojo.hostenv.normalizeLocale(_3e2).split("-")[0];var RTL={ar:true,fa:true,he:true,ur:true,yi:true};return !RTL[lang];};dojo.provide("dojo.date.format");(function(){dojo.date.format=function(_3e5,_3e6){if(typeof _3e6=="string"){dojo.deprecated("dojo.date.format","To format dates with POSIX-style strings, please use dojo.date.strftime instead","0.5");return dojo.date.strftime(_3e5,_3e6);}
function formatPattern(_3e7,_3e8){return _3e8.replace(/([a-z])\1*/ig,function(_3e9){var s;var c=_3e9.charAt(0);var l=_3e9.length;var pad;var _3ee=["abbr","wide","narrow"];switch(c){case "G":
if(l>3){dojo.unimplemented("Era format not implemented");}
s=info.eras[_3e7.getFullYear()<0?1:0];break;case "y":
s=_3e7.getFullYear();switch(l){case 1:
break;case 2:
s=String(s).substr(-2);break;default:
pad=true;}
break;case "Q":
case "q":
s=Math.ceil((_3e7.getMonth()+1)/3);switch(l){case 1:
case 2:
pad=true;break;case 3:
case 4:
dojo.unimplemented("Quarter format not implemented");}
break;case "M":
case "L":
var m=_3e7.getMonth();var _3f1;switch(l){case 1:
case 2:
s=m+1;pad=true;break;case 3:
case 4:
case 5:
_3f1=_3ee[l-3];break;}
if(_3f1){var type=(c=="L")?"standalone":"format";var prop=["months",type,_3f1].join("-");s=info[prop][m];}
break;case "w":
var _3f4=0;s=dojo.date.getWeekOfYear(_3e7,_3f4);pad=true;break;case "d":
s=_3e7.getDate();pad=true;break;case "D":
s=dojo.date.getDayOfYear(_3e7);pad=true;break;case "E":
case "e":
case "c":
var d=_3e7.getDay();var _3f1;switch(l){case 1:
case 2:
if(c=="e"){var _3f6=dojo.date.getFirstDayOfWeek(_3e6.locale);d=(d-_3f6+7)%7;}
if(c!="c"){s=d+1;pad=true;break;}
case 3:
case 4:
case 5:
_3f1=_3ee[l-3];break;}
if(_3f1){var type=(c=="c")?"standalone":"format";var prop=["days",type,_3f1].join("-");s=info[prop][d];}
break;case "a":
var _3f7=(_3e7.getHours()<12)?"am":"pm";s=info[_3f7];break;case "h":
case "H":
case "K":
case "k":
var h=_3e7.getHours();switch(c){case "h":
s=(h%12)||12;break;case "H":
s=h;break;case "K":
s=(h%12);break;case "k":
s=h||24;break;}
pad=true;break;case "m":
s=_3e7.getMinutes();pad=true;break;case "s":
s=_3e7.getSeconds();pad=true;break;case "S":
s=Math.round(_3e7.getMilliseconds()*Math.pow(10,l-3));break;case "v":
case "z":
s=dojo.date.getTimezoneName(_3e7);if(s){break;}
l=4;case "Z":
var _3f9=_3e7.getTimezoneOffset();var tz=[(_3f9<=0?"+":"-"),dojo.string.pad(Math.floor(Math.abs(_3f9)/60),2),dojo.string.pad(Math.abs(_3f9)%60,2)];if(l==4){tz.splice(0,0,"GMT");tz.splice(3,0,":");}
s=tz.join("");break;case "Y":
case "u":
case "W":
case "F":
case "g":
case "A":
dojo.debug(_3e9+" modifier not yet implemented");s="?";break;default:
dojo.raise("dojo.date.format: invalid pattern char: "+_3e8);}
if(pad){s=dojo.string.pad(s,l);}
return s;});}
_3e6=_3e6||{};var _3fb=dojo.hostenv.normalizeLocale(_3e6.locale);var _3fc=_3e6.formatLength||"full";var info=dojo.date._getGregorianBundle(_3fb);var str=[];var _3fe=dojo.lang.curry(this,formatPattern,_3e5);if(_3e6.selector!="timeOnly"){var _3ff=_3e6.datePattern||info["dateFormat-"+_3fc];if(_3ff){str.push(_processPattern(_3ff,_3fe));}}
if(_3e6.selector!="dateOnly"){var _400=_3e6.timePattern||info["timeFormat-"+_3fc];if(_400){str.push(_processPattern(_400,_3fe));}}
var _401=str.join(" ");return _401;};dojo.date.parse=function(_402,_403){_403=_403||{};var _404=dojo.hostenv.normalizeLocale(_403.locale);var info=dojo.date._getGregorianBundle(_404);var _406=_403.formatLength||"full";if(!_403.selector){_403.selector="dateOnly";}
var _407=_403.datePattern||info["dateFormat-"+_406];var _408=_403.timePattern||info["timeFormat-"+_406];var _409;if(_403.selector=="dateOnly"){_409=_407;}else{if(_403.selector=="timeOnly"){_409=_408;}else{if(_403.selector=="dateTime"){_409=_407+" "+_408;}else{var msg="dojo.date.parse: Unknown selector param passed: '"+_403.selector+"'.";msg+=" Defaulting to date pattern.";dojo.debug(msg);_409=_407;}}}
var _40b=[];var _40c=_processPattern(_409,dojo.lang.curry(this,_buildDateTimeRE,_40b,info,_403));var _40d=new RegExp("^"+_40c+"$");var _40e=_40d.exec(_402);if(!_40e){return null;}
var _40f=["abbr","wide","narrow"];var _410=new Date(1972,0);var _411={};for(var i=1;i<_40e.length;i++){var grp=_40b[i-1];var l=grp.length;var v=_40e[i];switch(grp.charAt(0)){case "y":
if(l!=2){_410.setFullYear(v);_411.year=v;}else{if(v<100){v=Number(v);var year=""+new Date().getFullYear();var _417=year.substring(0,2)*100;var _418=Number(year.substring(2,4));var _419=Math.min(_418+20,99);var num=(v<_419)?_417+v:_417-100+v;_410.setFullYear(num);_411.year=num;}else{if(_403.strict){return null;}
_410.setFullYear(v);_411.year=v;}}
break;case "M":
if(l>2){if(!_403.strict){v=v.replace(/\./g,"");v=v.toLowerCase();}
var _41b=info["months-format-"+_40f[l-3]].concat();for(var j=0;j<_41b.length;j++){if(!_403.strict){_41b[j]=_41b[j].toLowerCase();}
if(v==_41b[j]){_410.setMonth(j);_411.month=j;break;}}
if(j==_41b.length){dojo.debug("dojo.date.parse: Could not parse month name: '"+v+"'.");return null;}}else{_410.setMonth(v-1);_411.month=v-1;}
break;case "E":
case "e":
if(!_403.strict){v=v.toLowerCase();}
var days=info["days-format-"+_40f[l-3]].concat();for(var j=0;j<days.length;j++){if(!_403.strict){days[j]=days[j].toLowerCase();}
if(v==days[j]){break;}}
if(j==days.length){dojo.debug("dojo.date.parse: Could not parse weekday name: '"+v+"'.");return null;}
break;case "d":
_410.setDate(v);_411.date=v;break;case "a":
var am=_403.am||info.am;var pm=_403.pm||info.pm;if(!_403.strict){v=v.replace(/\./g,"").toLowerCase();am=am.replace(/\./g,"").toLowerCase();pm=pm.replace(/\./g,"").toLowerCase();}
if(_403.strict&&v!=am&&v!=pm){dojo.debug("dojo.date.parse: Could not parse am/pm part.");return null;}
var _420=_410.getHours();if(v==pm&&_420<12){_410.setHours(_420+12);}else{if(v==am&&_420==12){_410.setHours(0);}}
break;case "K":
if(v==24){v=0;}
case "h":
case "H":
case "k":
if(v>23){dojo.debug("dojo.date.parse: Illegal hours value");return null;}
_410.setHours(v);break;case "m":
_410.setMinutes(v);break;case "s":
_410.setSeconds(v);break;case "S":
_410.setMilliseconds(v);break;default:
dojo.unimplemented("dojo.date.parse: unsupported pattern char="+grp.charAt(0));}}
if(_411.year&&_410.getFullYear()!=_411.year){dojo.debug("Parsed year: '"+_410.getFullYear()+"' did not match input year: '"+_411.year+"'.");return null;}
if(_411.month&&_410.getMonth()!=_411.month){dojo.debug("Parsed month: '"+_410.getMonth()+"' did not match input month: '"+_411.month+"'.");return null;}
if(_411.date&&_410.getDate()!=_411.date){dojo.debug("Parsed day of month: '"+_410.getDate()+"' did not match input day of month: '"+_411.date+"'.");return null;}
return _410;};function _processPattern(_421,_422,_423,_424){var _425=function(x){return x;};_422=_422||_425;_423=_423||_425;_424=_424||_425;var _427=_421.match(/(''|[^'])+/g);var _428=false;for(var i=0;i<_427.length;i++){if(!_427[i]){_427[i]="";}else{_427[i]=(_428?_423:_422)(_427[i]);_428=!_428;}}
return _424(_427.join(""));}
function _buildDateTimeRE(_42a,info,_42c,_42d){return _42d.replace(/([a-z])\1*/ig,function(_42e){var s;var c=_42e.charAt(0);var l=_42e.length;switch(c){case "y":
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
var am=_42c.am||info.am||"AM";var pm=_42c.pm||info.pm||"PM";if(_42c.strict){s=am+"|"+pm;}else{s=am;s+=(am!=am.toLowerCase())?"|"+am.toLowerCase():"";s+="|";s+=(pm!=pm.toLowerCase())?pm+"|"+pm.toLowerCase():pm;}
break;default:
dojo.unimplemented("parse of date format, pattern="+_42d);}
if(_42a){_42a.push(_42e);}
return "\\s*("+s+")\\s*";});}})();dojo.date.strftime=function(_434,_435,_436){var _437=null;function _(s,n){return dojo.string.pad(s,n||2,_437||"0");}
var info=dojo.date._getGregorianBundle(_436);function $(_43b){switch(_43b){case "a":
return dojo.date.getDayShortName(_434,_436);case "A":
return dojo.date.getDayName(_434,_436);case "b":
case "h":
return dojo.date.getMonthShortName(_434,_436);case "B":
return dojo.date.getMonthName(_434,_436);case "c":
return dojo.date.format(_434,{locale:_436});case "C":
return _(Math.floor(_434.getFullYear()/100));case "d":
return _(_434.getDate());case "D":
return $("m")+"/"+$("d")+"/"+$("y");case "e":
if(_437==null){_437=" ";}
return _(_434.getDate());case "f":
if(_437==null){_437=" ";}
return _(_434.getMonth()+1);case "g":
break;case "G":
dojo.unimplemented("unimplemented modifier 'G'");break;case "F":
return $("Y")+"-"+$("m")+"-"+$("d");case "H":
return _(_434.getHours());case "I":
return _(_434.getHours()%12||12);case "j":
return _(dojo.date.getDayOfYear(_434),3);case "k":
if(_437==null){_437=" ";}
return _(_434.getHours());case "l":
if(_437==null){_437=" ";}
return _(_434.getHours()%12||12);case "m":
return _(_434.getMonth()+1);case "M":
return _(_434.getMinutes());case "n":
return "\n";case "p":
return info[_434.getHours()<12?"am":"pm"];case "r":
return $("I")+":"+$("M")+":"+$("S")+" "+$("p");case "R":
return $("H")+":"+$("M");case "S":
return _(_434.getSeconds());case "t":
return "\t";case "T":
return $("H")+":"+$("M")+":"+$("S");case "u":
return String(_434.getDay()||7);case "U":
return _(dojo.date.getWeekOfYear(_434));case "V":
return _(dojo.date.getIsoWeekOfYear(_434));case "W":
return _(dojo.date.getWeekOfYear(_434,1));case "w":
return String(_434.getDay());case "x":
return dojo.date.format(_434,{selector:"dateOnly",locale:_436});case "X":
return dojo.date.format(_434,{selector:"timeOnly",locale:_436});case "y":
return _(_434.getFullYear()%100);case "Y":
return String(_434.getFullYear());case "z":
var _43c=_434.getTimezoneOffset();return (_43c>0?"-":"+")+_(Math.floor(Math.abs(_43c)/60))+":"+_(Math.abs(_43c)%60);case "Z":
return dojo.date.getTimezoneName(_434);case "%":
return "%";}}
var _43d="";var i=0;var _43f=0;var _440=null;while((_43f=_435.indexOf("%",i))!=-1){_43d+=_435.substring(i,_43f++);switch(_435.charAt(_43f++)){case "_":
_437=" ";break;case "-":
_437="";break;case "0":
_437="0";break;case "^":
_440="upper";break;case "*":
_440="lower";break;case "#":
_440="swap";break;default:
_437=null;_43f--;break;}
var _441=$(_435.charAt(_43f++));switch(_440){case "upper":
_441=_441.toUpperCase();break;case "lower":
_441=_441.toLowerCase();break;case "swap":
var _442=_441.toLowerCase();var _443="";var j=0;var ch="";while(j<_441.length){ch=_441.charAt(j);_443+=(ch==_442.charAt(j))?ch.toUpperCase():ch.toLowerCase();j++;}
_441=_443;break;default:
break;}
_440=null;_43d+=_441;i=_43f;}
_43d+=_435.substring(i);return _43d;};(function(){var _446=[];dojo.date.addCustomFormats=function(_447,_448){_446.push({pkg:_447,name:_448});};dojo.date._getGregorianBundle=function(_449){var _44a={};dojo.lang.forEach(_446,function(desc){var _44c=dojo.i18n.getLocalization(desc.pkg,desc.name,_449);_44a=dojo.lang.mixin(_44a,_44c);},this);return _44a;};})();dojo.date.addCustomFormats("dojo.i18n.calendar","gregorian");dojo.date.addCustomFormats("dojo.i18n.calendar","gregorianExtras");dojo.date.getNames=function(item,type,use,_450){var _451;var _452=dojo.date._getGregorianBundle(_450);var _453=[item,use,type];if(use=="standAlone"){_451=_452[_453.join("-")];}
_453[1]="format";return (_451||_452[_453.join("-")]).concat();};dojo.date.getDayName=function(_454,_455){return dojo.date.getNames("days","wide","format",_455)[_454.getDay()];};dojo.date.getDayShortName=function(_456,_457){return dojo.date.getNames("days","abbr","format",_457)[_456.getDay()];};dojo.date.getMonthName=function(_458,_459){return dojo.date.getNames("months","wide","format",_459)[_458.getMonth()];};dojo.date.getMonthShortName=function(_45a,_45b){return dojo.date.getNames("months","abbr","format",_45b)[_45a.getMonth()];};dojo.date.toRelativeString=function(_45c){var now=new Date();var diff=(now-_45c)/1000;var end=" ago";var _460=false;if(diff<0){_460=true;end=" from now";diff=-diff;}
if(diff<60){diff=Math.round(diff);return diff+" second"+(diff==1?"":"s")+end;}
if(diff<60*60){diff=Math.round(diff/60);return diff+" minute"+(diff==1?"":"s")+end;}
if(diff<60*60*24){diff=Math.round(diff/3600);return diff+" hour"+(diff==1?"":"s")+end;}
if(diff<60*60*24*7){diff=Math.round(diff/(3600*24));if(diff==1){return _460?"Tomorrow":"Yesterday";}else{return diff+" days"+end;}}
return dojo.date.format(_45c);};dojo.date.toSql=function(_461,_462){return dojo.date.strftime(_461,"%F"+!_462?" %T":"");};dojo.date.fromSql=function(_463){var _464=_463.split(/[\- :]/g);while(_464.length<6){_464.push(0);}
return new Date(_464[0],(parseInt(_464[1],10)-1),_464[2],_464[3],_464[4],_464[5]);};dojo.provide("dojo.xml.Parse");dojo.xml.Parse=function(){var isIE=((dojo.render.html.capable)&&(dojo.render.html.ie));function getTagName(node){try{return node.tagName.toLowerCase();}
catch(e){return "";}}
function getDojoTagName(node){var _468=getTagName(node);if(!_468){return "";}
if((dojo.widget)&&(dojo.widget.tags[_468])){return _468;}
var p=_468.indexOf(":");if(p>=0){return _468;}
if(_468.substr(0,5)=="dojo:"){return _468;}
if(dojo.render.html.capable&&dojo.render.html.ie&&node.scopeName!="HTML"){return node.scopeName.toLowerCase()+":"+_468;}
if(_468.substr(0,4)=="dojo"){return "dojo:"+_468.substring(4);}
var djt=node.getAttribute("dojoType")||node.getAttribute("dojotype");if(djt){if(djt.indexOf(":")<0){djt="dojo:"+djt;}
return djt.toLowerCase();}
djt=node.getAttributeNS&&node.getAttributeNS(dojo.dom.dojoml,"type");if(djt){return "dojo:"+djt.toLowerCase();}
try{djt=node.getAttribute("dojo:type");}
catch(e){}
if(djt){return "dojo:"+djt.toLowerCase();}
if((dj_global["djConfig"])&&(!djConfig["ignoreClassNames"])){var _46b=node.className||node.getAttribute("class");if((_46b)&&(_46b.indexOf)&&(_46b.indexOf("dojo-")!=-1)){var _46c=_46b.split(" ");for(var x=0,c=_46c.length;x<c;x++){if(_46c[x].slice(0,5)=="dojo-"){return "dojo:"+_46c[x].substr(5).toLowerCase();}}}}
return "";}
this.parseElement=function(node,_470,_471,_472){var _473=getTagName(node);if(isIE&&_473.indexOf("/")==0){return null;}
try{if(node.getAttribute("parseWidgets").toLowerCase()=="false"){return {};}}
catch(e){}
var _474=true;if(_471){var _475=getDojoTagName(node);_473=_475||_473;_474=Boolean(_475);}
var _476={};_476[_473]=[];var pos=_473.indexOf(":");if(pos>0){var ns=_473.substring(0,pos);_476["ns"]=ns;if((dojo.ns)&&(!dojo.ns.allow(ns))){_474=false;}}
if(_474){var _479=this.parseAttributes(node);for(var attr in _479){if((!_476[_473][attr])||(typeof _476[_473][attr]!="array")){_476[_473][attr]=[];}
_476[_473][attr].push(_479[attr]);}
_476[_473].nodeRef=node;_476.tagName=_473;_476.index=_472||0;}
var _47b=0;for(var i=0;i<node.childNodes.length;i++){var tcn=node.childNodes.item(i);switch(tcn.nodeType){case dojo.dom.ELEMENT_NODE:
_47b++;var ctn=getDojoTagName(tcn)||getTagName(tcn);if(!_476[ctn]){_476[ctn]=[];}
_476[ctn].push(this.parseElement(tcn,true,_471,_47b));if((tcn.childNodes.length==1)&&(tcn.childNodes.item(0).nodeType==dojo.dom.TEXT_NODE)){_476[ctn][_476[ctn].length-1].value=tcn.childNodes.item(0).nodeValue;}
break;case dojo.dom.TEXT_NODE:
if(node.childNodes.length==1){_476[_473].push({value:node.childNodes.item(0).nodeValue});}
break;default:
break;}}
return _476;};this.parseAttributes=function(node){var _480={};var atts=node.attributes;var _482,i=0;while((_482=atts[i++])){if(isIE){if(!_482){continue;}
if((typeof _482=="object")&&(typeof _482.nodeValue=="undefined")||(_482.nodeValue==null)||(_482.nodeValue=="")){continue;}}
var nn=_482.nodeName.split(":");nn=(nn.length==2)?nn[1]:_482.nodeName;_480[nn]={value:_482.nodeValue};}
return _480;};};dojo.provide("dojo.lang.declare");dojo.lang.declare=function(_485,_486,init,_488){if((dojo.lang.isFunction(_488))||((!_488)&&(!dojo.lang.isFunction(init)))){var temp=_488;_488=init;init=temp;}
var _48a=[];if(dojo.lang.isArray(_486)){_48a=_486;_486=_48a.shift();}
if(!init){init=dojo.evalObjPath(_485,false);if((init)&&(!dojo.lang.isFunction(init))){init=null;}}
var ctor=dojo.lang.declare._makeConstructor();var scp=(_486?_486.prototype:null);if(scp){scp.prototyping=true;ctor.prototype=new _486();scp.prototyping=false;}
ctor.superclass=scp;ctor.mixins=_48a;for(var i=0,l=_48a.length;i<l;i++){dojo.lang.extend(ctor,_48a[i].prototype);}
ctor.prototype.initializer=null;ctor.prototype.declaredClass=_485;if(dojo.lang.isArray(_488)){dojo.lang.extend.apply(dojo.lang,[ctor].concat(_488));}else{dojo.lang.extend(ctor,(_488)||{});}
dojo.lang.extend(ctor,dojo.lang.declare._common);ctor.prototype.constructor=ctor;ctor.prototype.initializer=(ctor.prototype.initializer)||(init)||(function(){});dojo.lang.setObjPathValue(_485,ctor,null,true);return ctor;};dojo.lang.declare._makeConstructor=function(){return function(){var self=this._getPropContext();var s=self.constructor.superclass;if((s)&&(s.constructor)){if(s.constructor==arguments.callee){this._inherited("constructor",arguments);}else{this._contextMethod(s,"constructor",arguments);}}
var ms=(self.constructor.mixins)||([]);for(var i=0,m;(m=ms[i]);i++){(((m.prototype)&&(m.prototype.initializer))||(m)).apply(this,arguments);}
if((!this.prototyping)&&(self.initializer)){self.initializer.apply(this,arguments);}};};dojo.lang.declare._common={_getPropContext:function(){return (this.___proto||this);},_contextMethod:function(_494,_495,args){var _497,_498=this.___proto;this.___proto=_494;try{_497=_494[_495].apply(this,(args||[]));}
catch(e){throw e;}
finally{this.___proto=_498;}
return _497;},_inherited:function(prop,args){var p=this._getPropContext();do{if((!p.constructor)||(!p.constructor.superclass)){return;}
p=p.constructor.superclass;}while(!(prop in p));return (dojo.lang.isFunction(p[prop])?this._contextMethod(p,prop,args):p[prop]);},inherited:function(prop,args){dojo.deprecated("'inherited' method is dangerous, do not up-call! 'inherited' is slated for removal in 0.5; name your super class (or use superclass property) instead.","0.5");this._inherited(prop,args);}};dojo.declare=dojo.lang.declare;dojo.provide("dojo.ns");dojo.ns={namespaces:{},failed:{},loading:{},loaded:{},register:function(name,_49f,_4a0,_4a1){if(!_4a1||!this.namespaces[name]){this.namespaces[name]=new dojo.ns.Ns(name,_49f,_4a0);}},allow:function(name){if(this.failed[name]){return false;}
if((djConfig.excludeNamespace)&&(dojo.lang.inArray(djConfig.excludeNamespace,name))){return false;}
return ((name==this.dojo)||(!djConfig.includeNamespace)||(dojo.lang.inArray(djConfig.includeNamespace,name)));},get:function(name){return this.namespaces[name];},require:function(name){var ns=this.namespaces[name];if((ns)&&(this.loaded[name])){return ns;}
if(!this.allow(name)){return false;}
if(this.loading[name]){dojo.debug("dojo.namespace.require: re-entrant request to load namespace \""+name+"\" must fail.");return false;}
var req=dojo.require;this.loading[name]=true;try{if(name=="dojo"){req("dojo.namespaces.dojo");}else{if(!dojo.hostenv.moduleHasPrefix(name)){dojo.registerModulePath(name,"../"+name);}
req([name,"manifest"].join("."),false,true);}
if(!this.namespaces[name]){this.failed[name]=true;}}
finally{this.loading[name]=false;}
return this.namespaces[name];}};dojo.ns.Ns=function(name,_4a8,_4a9){this.name=name;this.module=_4a8;this.resolver=_4a9;this._loaded=[];this._failed=[];};dojo.ns.Ns.prototype.resolve=function(name,_4ab,_4ac){if(!this.resolver||djConfig["skipAutoRequire"]){return false;}
var _4ad=this.resolver(name,_4ab);if((_4ad)&&(!this._loaded[_4ad])&&(!this._failed[_4ad])){var req=dojo.require;req(_4ad,false,true);if(dojo.hostenv.findModule(_4ad,false)){this._loaded[_4ad]=true;}else{if(!_4ac){dojo.raise("dojo.ns.Ns.resolve: module '"+_4ad+"' not found after loading via namespace '"+this.name+"'");}
this._failed[_4ad]=true;}}
return Boolean(this._loaded[_4ad]);};dojo.registerNamespace=function(name,_4b0,_4b1){dojo.ns.register.apply(dojo.ns,arguments);};dojo.registerNamespaceResolver=function(name,_4b3){var n=dojo.ns.namespaces[name];if(n){n.resolver=_4b3;}};dojo.registerNamespaceManifest=function(_4b5,path,name,_4b8,_4b9){dojo.registerModulePath(name,path);dojo.registerNamespace(name,_4b8,_4b9);};dojo.registerNamespace("dojo","dojo.widget");dojo.provide("dojo.event.topic");dojo.event.topic=new function(){this.topics={};this.getTopic=function(_4ba){if(!this.topics[_4ba]){this.topics[_4ba]=new this.TopicImpl(_4ba);}
return this.topics[_4ba];};this.registerPublisher=function(_4bb,obj,_4bd){var _4bb=this.getTopic(_4bb);_4bb.registerPublisher(obj,_4bd);};this.subscribe=function(_4be,obj,_4c0){var _4be=this.getTopic(_4be);_4be.subscribe(obj,_4c0);};this.unsubscribe=function(_4c1,obj,_4c3){var _4c1=this.getTopic(_4c1);_4c1.unsubscribe(obj,_4c3);};this.destroy=function(_4c4){this.getTopic(_4c4).destroy();delete this.topics[_4c4];};this.publishApply=function(_4c5,args){var _4c5=this.getTopic(_4c5);_4c5.sendMessage.apply(_4c5,args);};this.publish=function(_4c7,_4c8){var _4c7=this.getTopic(_4c7);var args=[];for(var x=1;x<arguments.length;x++){args.push(arguments[x]);}
_4c7.sendMessage.apply(_4c7,args);};};dojo.event.topic.TopicImpl=function(_4cb){this.topicName=_4cb;this.subscribe=function(_4cc,_4cd){var tf=_4cd||_4cc;var to=(!_4cd)?dj_global:_4cc;return dojo.event.kwConnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});};this.unsubscribe=function(_4d0,_4d1){var tf=(!_4d1)?_4d0:_4d1;var to=(!_4d1)?null:_4d0;return dojo.event.kwDisconnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});};this._getJoinPoint=function(){return dojo.event.MethodJoinPoint.getForMethod(this,"sendMessage");};this.setSquelch=function(_4d4){this._getJoinPoint().squelch=_4d4;};this.destroy=function(){this._getJoinPoint().disconnect();};this.registerPublisher=function(_4d5,_4d6){dojo.event.connect(_4d5,_4d6,this,"sendMessage");};this.sendMessage=function(_4d7){};};dojo.provide("dojo.event.*");dojo.provide("dojo.widget.Manager");dojo.widget.manager=new function(){this.widgets=[];this.widgetIds=[];this.topWidgets={};var _4d8={};var _4d9=[];this.getUniqueId=function(_4da){var _4db;do{_4db=_4da+"_"+(_4d8[_4da]!=undefined?++_4d8[_4da]:_4d8[_4da]=0);}while(this.getWidgetById(_4db));return _4db;};this.add=function(_4dc){this.widgets.push(_4dc);if(!_4dc.extraArgs["id"]){_4dc.extraArgs["id"]=_4dc.extraArgs["ID"];}
if(_4dc.widgetId==""){if(_4dc["id"]){_4dc.widgetId=_4dc["id"];}else{if(_4dc.extraArgs["id"]){_4dc.widgetId=_4dc.extraArgs["id"];}else{_4dc.widgetId=this.getUniqueId(_4dc.ns+"_"+_4dc.widgetType);}}}
if(this.widgetIds[_4dc.widgetId]){dojo.debug("widget ID collision on ID: "+_4dc.widgetId);}
this.widgetIds[_4dc.widgetId]=_4dc;};this.destroyAll=function(){for(var x=this.widgets.length-1;x>=0;x--){try{this.widgets[x].destroy(true);delete this.widgets[x];}
catch(e){}}};this.remove=function(_4de){if(dojo.lang.isNumber(_4de)){var tw=this.widgets[_4de].widgetId;delete this.widgetIds[tw];this.widgets.splice(_4de,1);}else{this.removeById(_4de);}};this.removeById=function(id){if(!dojo.lang.isString(id)){id=id["widgetId"];if(!id){dojo.debug("invalid widget or id passed to removeById");return;}}
for(var i=0;i<this.widgets.length;i++){if(this.widgets[i].widgetId==id){this.remove(i);break;}}};this.getWidgetById=function(id){if(dojo.lang.isString(id)){return this.widgetIds[id];}
return id;};this.getWidgetsByType=function(type){var lt=type.toLowerCase();var _4e5=(type.indexOf(":")<0?function(x){return x.widgetType.toLowerCase();}:function(x){return x.getNamespacedType();});var ret=[];dojo.lang.forEach(this.widgets,function(x){if(_4e5(x)==lt){ret.push(x);}});return ret;};this.getWidgetsByFilter=function(_4ea,_4eb){var ret=[];dojo.lang.every(this.widgets,function(x){if(_4ea(x)){ret.push(x);if(_4eb){return false;}}
return true;});return (_4eb?ret[0]:ret);};this.getAllWidgets=function(){return this.widgets.concat();};this.getWidgetByNode=function(node){var w=this.getAllWidgets();node=dojo.byId(node);for(var i=0;i<w.length;i++){if(w[i].domNode==node){return w[i];}}
return null;};this.byId=this.getWidgetById;this.byType=this.getWidgetsByType;this.byFilter=this.getWidgetsByFilter;this.byNode=this.getWidgetByNode;var _4f1={};var _4f2=["dojo.widget"];for(var i=0;i<_4f2.length;i++){_4f2[_4f2[i]]=true;}
this.registerWidgetPackage=function(_4f4){if(!_4f2[_4f4]){_4f2[_4f4]=true;_4f2.push(_4f4);}};this.getWidgetPackageList=function(){return dojo.lang.map(_4f2,function(elt){return (elt!==true?elt:undefined);});};this.getImplementation=function(_4f6,_4f7,_4f8,ns){var impl=this.getImplementationName(_4f6,ns);if(impl){var ret=_4f7?new impl(_4f7):new impl();return ret;}};function buildPrefixCache(){for(var _4fc in dojo.render){if(dojo.render[_4fc]["capable"]===true){var _4fd=dojo.render[_4fc].prefixes;for(var i=0;i<_4fd.length;i++){_4d9.push(_4fd[i].toLowerCase());}}}}
var _4ff=function(_500,_501){if(!_501){return null;}
for(var i=0,l=_4d9.length,_504;i<=l;i++){_504=(i<l?_501[_4d9[i]]:_501);if(!_504){continue;}
for(var name in _504){if(name.toLowerCase()==_500){return _504[name];}}}
return null;};var _506=function(_507,_508){var _509=dojo.evalObjPath(_508,false);return (_509?_4ff(_507,_509):null);};this.getImplementationName=function(_50a,ns){var _50c=_50a.toLowerCase();ns=ns||"dojo";var imps=_4f1[ns]||(_4f1[ns]={});var impl=imps[_50c];if(impl){return impl;}
if(!_4d9.length){buildPrefixCache();}
var _50f=dojo.ns.get(ns);if(!_50f){dojo.ns.register(ns,ns+".widget");_50f=dojo.ns.get(ns);}
if(_50f){_50f.resolve(_50a);}
impl=_506(_50c,_50f.module);if(impl){return (imps[_50c]=impl);}
_50f=dojo.ns.require(ns);if((_50f)&&(_50f.resolver)){_50f.resolve(_50a);impl=_506(_50c,_50f.module);if(impl){return (imps[_50c]=impl);}}
dojo.deprecated("dojo.widget.Manager.getImplementationName","Could not locate widget implementation for \""+_50a+"\" in \""+_50f.module+"\" registered to namespace \""+_50f.name+"\". "+"Developers must specify correct namespaces for all non-Dojo widgets","0.5");for(var i=0;i<_4f2.length;i++){impl=_506(_50c,_4f2[i]);if(impl){return (imps[_50c]=impl);}}
throw new Error("Could not locate widget implementation for \""+_50a+"\" in \""+_50f.module+"\" registered to namespace \""+_50f.name+"\"");};this.resizing=false;this.onWindowResized=function(){if(this.resizing){return;}
try{this.resizing=true;for(var id in this.topWidgets){var _512=this.topWidgets[id];if(_512.checkSize){_512.checkSize();}}}
catch(e){}
finally{this.resizing=false;}};if(typeof window!="undefined"){dojo.addOnLoad(this,"onWindowResized");dojo.event.connect(window,"onresize",this,"onWindowResized");}};(function(){var dw=dojo.widget;var dwm=dw.manager;var h=dojo.lang.curry(dojo.lang,"hitch",dwm);var g=function(_517,_518){dw[(_518||_517)]=h(_517);};g("add","addWidget");g("destroyAll","destroyAllWidgets");g("remove","removeWidget");g("removeById","removeWidgetById");g("getWidgetById");g("getWidgetById","byId");g("getWidgetsByType");g("getWidgetsByFilter");g("getWidgetsByType","byType");g("getWidgetsByFilter","byFilter");g("getWidgetByNode","byNode");dw.all=function(n){var _51a=dwm.getAllWidgets.apply(dwm,arguments);if(arguments.length>0){return _51a[n];}
return _51a;};g("registerWidgetPackage");g("getImplementation","getWidgetImplementation");g("getImplementationName","getWidgetImplementationName");dw.widgets=dwm.widgets;dw.widgetIds=dwm.widgetIds;dw.root=dwm.root;})();dojo.provide("dojo.uri.Uri");dojo.uri=new function(){var _51b=new RegExp("^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$");var _51c=new RegExp("(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$");this.dojoUri=function(uri){return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri(),uri);};this.moduleUri=function(_51e,uri){var loc=dojo.hostenv.getModuleSymbols(_51e).join("/");if(!loc){return null;}
if(loc.lastIndexOf("/")!=loc.length-1){loc+="/";}
return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri()+loc,uri);};this.Uri=function(){var uri=arguments[0];for(var i=1;i<arguments.length;i++){if(!arguments[i]){continue;}
var _523=new dojo.uri.Uri(arguments[i].toString());var _524=new dojo.uri.Uri(uri.toString());if((_523.path=="")&&(_523.scheme==null)&&(_523.authority==null)&&(_523.query==null)){if(_523.fragment!=null){_524.fragment=_523.fragment;}
_523=_524;}
if(_523.scheme!=null&&_523.authority!=null){uri="";}
if(_523.scheme!=null){uri+=_523.scheme+":";}
if(_523.authority!=null){uri+="//"+_523.authority;}
uri+=_523.path;if(_523.query!=null){uri+="?"+_523.query;}
if(_523.fragment!=null){uri+="#"+_523.fragment;}}
this.uri=uri.toString();var r=this.uri.match(_51c);this.scheme=r[2]||(r[1]?"":null);this.authority=r[4]||(r[3]?"":null);this.path=r[5];this.query=r[7]||(r[6]?"":null);this.fragment=r[9]||(r[8]?"":null);if(this.authority!=null){r=this.authority.match(_51b);this.user=r[3]||null;this.password=r[4]||null;this.host=r[5];this.port=r[7]||null;}
this.toString=function(){return this.uri;};};};dojo.provide("dojo.uri.*");dojo.provide("dojo.html.common");dojo.lang.mixin(dojo.html,dojo.dom);dojo.html.getEventTarget=function(evt){if(!evt){evt=dojo.global().event||{};}
var t=(evt.srcElement?evt.srcElement:(evt.target?evt.target:null));while((t)&&(t.nodeType!=1)){t=t.parentNode;}
return t;};dojo.html.getViewport=function(){var _528=dojo.global();var _529=dojo.doc();var w=0;var h=0;if(dojo.render.html.mozilla){w=_529.documentElement.clientWidth;h=_528.innerHeight;}else{if(!dojo.render.html.opera&&_528.innerWidth){w=_528.innerWidth;h=_528.innerHeight;}else{if(!dojo.render.html.opera&&dojo.exists(_529,"documentElement.clientWidth")){var w2=_529.documentElement.clientWidth;if(!w||w2&&w2<w){w=w2;}
h=_529.documentElement.clientHeight;}else{if(dojo.body().clientWidth){w=dojo.body().clientWidth;h=dojo.body().clientHeight;}}}}
return {width:w,height:h};};dojo.html.getScroll=function(){var _52d=dojo.global();var _52e=dojo.doc();var top=_52d.pageYOffset||_52e.documentElement.scrollTop||dojo.body().scrollTop||0;var left=_52d.pageXOffset||_52e.documentElement.scrollLeft||dojo.body().scrollLeft||0;return {top:top,left:left,offset:{x:left,y:top}};};dojo.html.getParentByType=function(node,type){var _533=dojo.doc();var _534=dojo.byId(node);type=type.toLowerCase();while((_534)&&(_534.nodeName.toLowerCase()!=type)){if(_534==(_533["body"]||_533["documentElement"])){return null;}
_534=_534.parentNode;}
return _534;};dojo.html.getAttribute=function(node,attr){node=dojo.byId(node);if((!node)||(!node.getAttribute)){return null;}
var ta=typeof attr=="string"?attr:new String(attr);var v=node.getAttribute(ta.toUpperCase());if((v)&&(typeof v=="string")&&(v!="")){return v;}
if(v&&v.value){return v.value;}
if((node.getAttributeNode)&&(node.getAttributeNode(ta))){return (node.getAttributeNode(ta)).value;}else{if(node.getAttribute(ta)){return node.getAttribute(ta);}else{if(node.getAttribute(ta.toLowerCase())){return node.getAttribute(ta.toLowerCase());}}}
return null;};dojo.html.hasAttribute=function(node,attr){return dojo.html.getAttribute(dojo.byId(node),attr)?true:false;};dojo.html.getCursorPosition=function(e){e=e||dojo.global().event;var _53c={x:0,y:0};if(e.pageX||e.pageY){_53c.x=e.pageX;_53c.y=e.pageY;}else{var de=dojo.doc().documentElement;var db=dojo.body();_53c.x=e.clientX+((de||db)["scrollLeft"])-((de||db)["clientLeft"]);_53c.y=e.clientY+((de||db)["scrollTop"])-((de||db)["clientTop"]);}
return _53c;};dojo.html.isTag=function(node){node=dojo.byId(node);if(node&&node.tagName){for(var i=1;i<arguments.length;i++){if(node.tagName.toLowerCase()==String(arguments[i]).toLowerCase()){return String(arguments[i]).toLowerCase();}}}
return "";};if(dojo.render.html.ie&&!dojo.render.html.ie70){if(window.location.href.substr(0,6).toLowerCase()!="https:"){(function(){var _541=dojo.doc().createElement("script");_541.src="javascript:'dojo.html.createExternalElement=function(doc, tag){ return doc.createElement(tag); }'";dojo.doc().getElementsByTagName("head")[0].appendChild(_541);})();}}else{dojo.html.createExternalElement=function(doc,tag){return doc.createElement(tag);};}
dojo.provide("dojo.a11y");dojo.a11y={imgPath:dojo.uri.dojoUri("src/widget/templates/images"),doAccessibleCheck:true,accessible:null,checkAccessible:function(){if(this.accessible===null){this.accessible=false;if(this.doAccessibleCheck==true){this.accessible=this.testAccessible();}}
return this.accessible;},testAccessible:function(){this.accessible=false;if(dojo.render.html.ie||dojo.render.html.mozilla){var div=document.createElement("div");div.style.backgroundImage="url(\""+this.imgPath+"/tab_close.gif\")";dojo.body().appendChild(div);var _545=null;if(window.getComputedStyle){var _546=getComputedStyle(div,"");_545=_546.getPropertyValue("background-image");}else{_545=div.currentStyle.backgroundImage;}
var _547=false;if(_545!=null&&(_545=="none"||_545=="url(invalid-url:)")){this.accessible=true;}
dojo.body().removeChild(div);}
return this.accessible;},setCheckAccessible:function(_548){this.doAccessibleCheck=_548;},setAccessibleMode:function(){if(this.accessible===null){if(this.checkAccessible()){dojo.render.html.prefixes.unshift("a11y");}}
return this.accessible;}};dojo.provide("dojo.widget.Widget");dojo.declare("dojo.widget.Widget",null,function(){this.children=[];this.extraArgs={};},{parent:null,isTopLevel:false,disabled:false,isContainer:false,widgetId:"",widgetType:"Widget",ns:"dojo",getNamespacedType:function(){return (this.ns?this.ns+":"+this.widgetType:this.widgetType).toLowerCase();},toString:function(){return "[Widget "+this.getNamespacedType()+", "+(this.widgetId||"NO ID")+"]";},repr:function(){return this.toString();},enable:function(){this.disabled=false;},disable:function(){this.disabled=true;},onResized:function(){this.notifyChildrenOfResize();},notifyChildrenOfResize:function(){for(var i=0;i<this.children.length;i++){var _54a=this.children[i];if(_54a.onResized){_54a.onResized();}}},create:function(args,_54c,_54d,ns){if(ns){this.ns=ns;}
this.satisfyPropertySets(args,_54c,_54d);this.mixInProperties(args,_54c,_54d);this.postMixInProperties(args,_54c,_54d);dojo.widget.manager.add(this);this.buildRendering(args,_54c,_54d);this.initialize(args,_54c,_54d);this.postInitialize(args,_54c,_54d);this.postCreate(args,_54c,_54d);return this;},destroy:function(_54f){if(this.parent){this.parent.removeChild(this);}
this.destroyChildren();this.uninitialize();this.destroyRendering(_54f);dojo.widget.manager.removeById(this.widgetId);},destroyChildren:function(){var _550;var i=0;while(this.children.length>i){_550=this.children[i];if(_550 instanceof dojo.widget.Widget){this.removeChild(_550);_550.destroy();continue;}
i++;}},getChildrenOfType:function(type,_553){var ret=[];var _555=dojo.lang.isFunction(type);if(!_555){type=type.toLowerCase();}
for(var x=0;x<this.children.length;x++){if(_555){if(this.children[x] instanceof type){ret.push(this.children[x]);}}else{if(this.children[x].widgetType.toLowerCase()==type){ret.push(this.children[x]);}}
if(_553){ret=ret.concat(this.children[x].getChildrenOfType(type,_553));}}
return ret;},getDescendants:function(){var _557=[];var _558=[this];var elem;while((elem=_558.pop())){_557.push(elem);if(elem.children){dojo.lang.forEach(elem.children,function(elem){_558.push(elem);});}}
return _557;},isFirstChild:function(){return this===this.parent.children[0];},isLastChild:function(){return this===this.parent.children[this.parent.children.length-1];},satisfyPropertySets:function(args){return args;},mixInProperties:function(args,frag){if((args["fastMixIn"])||(frag["fastMixIn"])){for(var x in args){this[x]=args[x];}
return;}
var _55f;var _560=dojo.widget.lcArgsCache[this.widgetType];if(_560==null){_560={};for(var y in this){_560[((new String(y)).toLowerCase())]=y;}
dojo.widget.lcArgsCache[this.widgetType]=_560;}
var _562={};for(var x in args){if(!this[x]){var y=_560[(new String(x)).toLowerCase()];if(y){args[y]=args[x];x=y;}}
if(_562[x]){continue;}
_562[x]=true;if((typeof this[x])!=(typeof _55f)){if(typeof args[x]!="string"){this[x]=args[x];}else{if(dojo.lang.isString(this[x])){this[x]=args[x];}else{if(dojo.lang.isNumber(this[x])){this[x]=new Number(args[x]);}else{if(dojo.lang.isBoolean(this[x])){this[x]=(args[x].toLowerCase()=="false")?false:true;}else{if(dojo.lang.isFunction(this[x])){if(args[x].search(/[^\w\.]+/i)==-1){this[x]=dojo.evalObjPath(args[x],false);}else{var tn=dojo.lang.nameAnonFunc(new Function(args[x]),this);dojo.event.kwConnect({srcObj:this,srcFunc:x,adviceObj:this,adviceFunc:tn});}}else{if(dojo.lang.isArray(this[x])){this[x]=args[x].split(";");}else{if(this[x] instanceof Date){this[x]=new Date(Number(args[x]));}else{if(typeof this[x]=="object"){if(this[x] instanceof dojo.uri.Uri){this[x]=dojo.uri.dojoUri(args[x]);}else{var _564=args[x].split(";");for(var y=0;y<_564.length;y++){var si=_564[y].indexOf(":");if((si!=-1)&&(_564[y].length>si)){this[x][_564[y].substr(0,si).replace(/^\s+|\s+$/g,"")]=_564[y].substr(si+1);}}}}else{this[x]=args[x];}}}}}}}}}else{this.extraArgs[x.toLowerCase()]=args[x];}}},postMixInProperties:function(args,frag,_568){},initialize:function(args,frag,_56b){return false;},postInitialize:function(args,frag,_56e){return false;},postCreate:function(args,frag,_571){return false;},uninitialize:function(){return false;},buildRendering:function(args,frag,_574){dojo.unimplemented("dojo.widget.Widget.buildRendering, on "+this.toString()+", ");return false;},destroyRendering:function(){dojo.unimplemented("dojo.widget.Widget.destroyRendering");return false;},addedTo:function(_575){},addChild:function(_576){dojo.unimplemented("dojo.widget.Widget.addChild");return false;},removeChild:function(_577){for(var x=0;x<this.children.length;x++){if(this.children[x]===_577){this.children.splice(x,1);_577.parent=null;break;}}
return _577;},getPreviousSibling:function(){var idx=this.getParentIndex();if(idx<=0){return null;}
return this.parent.children[idx-1];},getSiblings:function(){return this.parent.children;},getParentIndex:function(){return dojo.lang.indexOf(this.parent.children,this,true);},getNextSibling:function(){var idx=this.getParentIndex();if(idx==this.parent.children.length-1){return null;}
if(idx<0){return null;}
return this.parent.children[idx+1];}});dojo.widget.lcArgsCache={};dojo.widget.tags={};dojo.widget.tags.addParseTreeHandler=function(type){dojo.deprecated("addParseTreeHandler",". ParseTreeHandlers are now reserved for components. Any unfiltered DojoML tag without a ParseTreeHandler is assumed to be a widget","0.5");};dojo.widget.tags["dojo:propertyset"]=function(_57c,_57d,_57e){var _57f=_57d.parseProperties(_57c["dojo:propertyset"]);};dojo.widget.tags["dojo:connect"]=function(_580,_581,_582){var _583=_581.parseProperties(_580["dojo:connect"]);};dojo.widget.buildWidgetFromParseTree=function(type,frag,_586,_587,_588,_589){dojo.a11y.setAccessibleMode();var _58a=type.split(":");_58a=(_58a.length==2)?_58a[1]:type;var _58b=_589||_586.parseProperties(frag[frag["ns"]+":"+_58a]);var _58c=dojo.widget.manager.getImplementation(_58a,null,null,frag["ns"]);if(!_58c){throw new Error("cannot find \""+type+"\" widget");}else{if(!_58c.create){throw new Error("\""+type+"\" widget object has no \"create\" method and does not appear to implement *Widget");}}
_58b["dojoinsertionindex"]=_588;var ret=_58c.create(_58b,frag,_587,frag["ns"]);return ret;};dojo.widget.defineWidget=function(_58e,_58f,_590,init,_592){if(dojo.lang.isString(arguments[3])){dojo.widget._defineWidget(arguments[0],arguments[3],arguments[1],arguments[4],arguments[2]);}else{var args=[arguments[0]],p=3;if(dojo.lang.isString(arguments[1])){args.push(arguments[1],arguments[2]);}else{args.push("",arguments[1]);p=2;}
if(dojo.lang.isFunction(arguments[p])){args.push(arguments[p],arguments[p+1]);}else{args.push(null,arguments[p]);}
dojo.widget._defineWidget.apply(this,args);}};dojo.widget.defineWidget.renderers="html|svg|vml";dojo.widget._defineWidget=function(_595,_596,_597,init,_599){var _59a=_595.split(".");var type=_59a.pop();var regx="\\.("+(_596?_596+"|":"")+dojo.widget.defineWidget.renderers+")\\.";var r=_595.search(new RegExp(regx));_59a=(r<0?_59a.join("."):_595.substr(0,r));dojo.widget.manager.registerWidgetPackage(_59a);var pos=_59a.indexOf(".");var _59f=(pos>-1)?_59a.substring(0,pos):_59a;_599=(_599)||{};_599.widgetType=type;if((!init)&&(_599["classConstructor"])){init=_599.classConstructor;delete _599.classConstructor;}
dojo.declare(_595,_597,init,_599);};dojo.provide("dojo.widget.Parse");dojo.widget.Parse=function(_5a0){this.propertySetsList=[];this.fragment=_5a0;this.createComponents=function(frag,_5a2){var _5a3=[];var _5a4=false;try{if(frag&&frag.tagName&&(frag!=frag.nodeRef)){var _5a5=dojo.widget.tags;var tna=String(frag.tagName).split(";");for(var x=0;x<tna.length;x++){var ltn=tna[x].replace(/^\s+|\s+$/g,"").toLowerCase();frag.tagName=ltn;var ret;if(_5a5[ltn]){_5a4=true;ret=_5a5[ltn](frag,this,_5a2,frag.index);_5a3.push(ret);}else{if(ltn.indexOf(":")==-1){ltn="dojo:"+ltn;}
ret=dojo.widget.buildWidgetFromParseTree(ltn,frag,this,_5a2,frag.index);if(ret){_5a4=true;_5a3.push(ret);}}}}}
catch(e){dojo.debug("dojo.widget.Parse: error:",e);}
if(!_5a4){_5a3=_5a3.concat(this.createSubComponents(frag,_5a2));}
return _5a3;};this.createSubComponents=function(_5aa,_5ab){var frag,_5ad=[];for(var item in _5aa){frag=_5aa[item];if(frag&&typeof frag=="object"&&(frag!=_5aa.nodeRef)&&(frag!=_5aa.tagName)&&(!dojo.dom.isNode(frag))){_5ad=_5ad.concat(this.createComponents(frag,_5ab));}}
return _5ad;};this.parsePropertySets=function(_5af){return [];};this.parseProperties=function(_5b0){var _5b1={};for(var item in _5b0){if((_5b0[item]==_5b0.tagName)||(_5b0[item]==_5b0.nodeRef)){}else{var frag=_5b0[item];if(frag.tagName&&dojo.widget.tags[frag.tagName.toLowerCase()]){}else{if(frag[0]&&frag[0].value!=""&&frag[0].value!=null){try{if(item.toLowerCase()=="dataprovider"){var _5b4=this;this.getDataProvider(_5b4,frag[0].value);_5b1.dataProvider=this.dataProvider;}
_5b1[item]=frag[0].value;var _5b5=this.parseProperties(frag);for(var _5b6 in _5b5){_5b1[_5b6]=_5b5[_5b6];}}
catch(e){dojo.debug(e);}}}
switch(item.toLowerCase()){case "checked":
case "disabled":
if(typeof _5b1[item]!="boolean"){_5b1[item]=true;}
break;}}}
return _5b1;};this.getDataProvider=function(_5b7,_5b8){dojo.io.bind({url:_5b8,load:function(type,_5ba){if(type=="load"){_5b7.dataProvider=_5ba;}},mimetype:"text/javascript",sync:true});};this.getPropertySetById=function(_5bb){for(var x=0;x<this.propertySetsList.length;x++){if(_5bb==this.propertySetsList[x]["id"][0].value){return this.propertySetsList[x];}}
return "";};this.getPropertySetsByType=function(_5bd){var _5be=[];for(var x=0;x<this.propertySetsList.length;x++){var cpl=this.propertySetsList[x];var cpcc=cpl.componentClass||cpl.componentType||null;var _5c2=this.propertySetsList[x]["id"][0].value;if(cpcc&&(_5c2==cpcc[0].value)){_5be.push(cpl);}}
return _5be;};this.getPropertySets=function(_5c3){var ppl="dojo:propertyproviderlist";var _5c5=[];var _5c6=_5c3.tagName;if(_5c3[ppl]){var _5c7=_5c3[ppl].value.split(" ");for(var _5c8 in _5c7){if((_5c8.indexOf("..")==-1)&&(_5c8.indexOf("://")==-1)){var _5c9=this.getPropertySetById(_5c8);if(_5c9!=""){_5c5.push(_5c9);}}else{}}}
return this.getPropertySetsByType(_5c6).concat(_5c5);};this.createComponentFromScript=function(_5ca,_5cb,_5cc,ns){_5cc.fastMixIn=true;var ltn=(ns||"dojo")+":"+_5cb.toLowerCase();if(dojo.widget.tags[ltn]){return [dojo.widget.tags[ltn](_5cc,this,null,null,_5cc)];}
return [dojo.widget.buildWidgetFromParseTree(ltn,_5cc,this,null,null,_5cc)];};};dojo.widget._parser_collection={"dojo":new dojo.widget.Parse()};dojo.widget.getParser=function(name){if(!name){name="dojo";}
if(!this._parser_collection[name]){this._parser_collection[name]=new dojo.widget.Parse();}
return this._parser_collection[name];};dojo.widget.createWidget=function(name,_5d1,_5d2,_5d3){var _5d4=false;var _5d5=(typeof name=="string");if(_5d5){var pos=name.indexOf(":");var ns=(pos>-1)?name.substring(0,pos):"dojo";if(pos>-1){name=name.substring(pos+1);}
var _5d8=name.toLowerCase();var _5d9=ns+":"+_5d8;_5d4=(dojo.byId(name)&&!dojo.widget.tags[_5d9]);}
if((arguments.length==1)&&(_5d4||!_5d5)){var xp=new dojo.xml.Parse();var tn=_5d4?dojo.byId(name):name;return dojo.widget.getParser().createComponents(xp.parseElement(tn,null,true))[0];}
function fromScript(_5dc,name,_5de,ns){_5de[_5d9]={dojotype:[{value:_5d8}],nodeRef:_5dc,fastMixIn:true};_5de.ns=ns;return dojo.widget.getParser().createComponentFromScript(_5dc,name,_5de,ns);}
_5d1=_5d1||{};var _5e0=false;var tn=null;var h=dojo.render.html.capable;if(h){tn=document.createElement("span");}
if(!_5d2){_5e0=true;_5d2=tn;if(h){dojo.body().appendChild(_5d2);}}else{if(_5d3){dojo.dom.insertAtPosition(tn,_5d2,_5d3);}else{tn=_5d2;}}
var _5e2=fromScript(tn,name.toLowerCase(),_5d1,ns);if((!_5e2)||(!_5e2[0])||(typeof _5e2[0].widgetType=="undefined")){throw new Error("createWidget: Creation of \""+name+"\" widget failed.");}
try{if(_5e0&&_5e2[0].domNode.parentNode){_5e2[0].domNode.parentNode.removeChild(_5e2[0].domNode);}}
catch(e){dojo.debug(e);}
return _5e2[0];};dojo.provide("dojo.html.style");dojo.html.getClass=function(node){node=dojo.byId(node);if(!node){return "";}
var cs="";if(node.className){cs=node.className;}else{if(dojo.html.hasAttribute(node,"class")){cs=dojo.html.getAttribute(node,"class");}}
return cs.replace(/^\s+|\s+$/g,"");};dojo.html.getClasses=function(node){var c=dojo.html.getClass(node);return (c=="")?[]:c.split(/\s+/g);};dojo.html.hasClass=function(node,_5e8){return (new RegExp("(^|\\s+)"+_5e8+"(\\s+|$)")).test(dojo.html.getClass(node));};dojo.html.prependClass=function(node,_5ea){_5ea+=" "+dojo.html.getClass(node);return dojo.html.setClass(node,_5ea);};dojo.html.addClass=function(node,_5ec){if(dojo.html.hasClass(node,_5ec)){return false;}
_5ec=(dojo.html.getClass(node)+" "+_5ec).replace(/^\s+|\s+$/g,"");return dojo.html.setClass(node,_5ec);};dojo.html.setClass=function(node,_5ee){node=dojo.byId(node);var cs=new String(_5ee);try{if(typeof node.className=="string"){node.className=cs;}else{if(node.setAttribute){node.setAttribute("class",_5ee);node.className=cs;}else{return false;}}}
catch(e){dojo.debug("dojo.html.setClass() failed",e);}
return true;};dojo.html.removeClass=function(node,_5f1,_5f2){try{if(!_5f2){var _5f3=dojo.html.getClass(node).replace(new RegExp("(^|\\s+)"+_5f1+"(\\s+|$)"),"$1$2");}else{var _5f3=dojo.html.getClass(node).replace(_5f1,"");}
dojo.html.setClass(node,_5f3);}
catch(e){dojo.debug("dojo.html.removeClass() failed",e);}
return true;};dojo.html.replaceClass=function(node,_5f5,_5f6){dojo.html.removeClass(node,_5f6);dojo.html.addClass(node,_5f5);};dojo.html.classMatchType={ContainsAll:0,ContainsAny:1,IsOnly:2};dojo.html.getElementsByClass=function(_5f7,_5f8,_5f9,_5fa,_5fb){_5fb=false;var _5fc=dojo.doc();_5f8=dojo.byId(_5f8)||_5fc;var _5fd=_5f7.split(/\s+/g);var _5fe=[];if(_5fa!=1&&_5fa!=2){_5fa=0;}
var _5ff=new RegExp("(\\s|^)(("+_5fd.join(")|(")+"))(\\s|$)");var _600=_5fd.join(" ").length;var _601=[];if(!_5fb&&_5fc.evaluate){var _602=".//"+(_5f9||"*")+"[contains(";if(_5fa!=dojo.html.classMatchType.ContainsAny){_602+="concat(' ',@class,' '), ' "+_5fd.join(" ') and contains(concat(' ',@class,' '), ' ")+" ')";if(_5fa==2){_602+=" and string-length(@class)="+_600+"]";}else{_602+="]";}}else{_602+="concat(' ',@class,' '), ' "+_5fd.join(" ') or contains(concat(' ',@class,' '), ' ")+" ')]";}
var _603=_5fc.evaluate(_602,_5f8,null,XPathResult.ANY_TYPE,null);var _604=_603.iterateNext();while(_604){try{_601.push(_604);_604=_603.iterateNext();}
catch(e){break;}}
return _601;}else{if(!_5f9){_5f9="*";}
_601=_5f8.getElementsByTagName(_5f9);var node,i=0;outer:
while(node=_601[i++]){var _607=dojo.html.getClasses(node);if(_607.length==0){continue outer;}
var _608=0;for(var j=0;j<_607.length;j++){if(_5ff.test(_607[j])){if(_5fa==dojo.html.classMatchType.ContainsAny){_5fe.push(node);continue outer;}else{_608++;}}else{if(_5fa==dojo.html.classMatchType.IsOnly){continue outer;}}}
if(_608==_5fd.length){if((_5fa==dojo.html.classMatchType.IsOnly)&&(_608==_607.length)){_5fe.push(node);}else{if(_5fa==dojo.html.classMatchType.ContainsAll){_5fe.push(node);}}}}
return _5fe;}};dojo.html.getElementsByClassName=dojo.html.getElementsByClass;dojo.html.toCamelCase=function(_60a){var arr=_60a.split("-"),cc=arr[0];for(var i=1;i<arr.length;i++){cc+=arr[i].charAt(0).toUpperCase()+arr[i].substring(1);}
return cc;};dojo.html.toSelectorCase=function(_60e){return _60e.replace(/([A-Z])/g,"-$1").toLowerCase();};dojo.html.getComputedStyle=function(node,_610,_611){node=dojo.byId(node);var _610=dojo.html.toSelectorCase(_610);var _612=dojo.html.toCamelCase(_610);if(!node||!node.style){return _611;}else{if(document.defaultView&&dojo.html.isDescendantOf(node,node.ownerDocument)){try{var cs=document.defaultView.getComputedStyle(node,"");if(cs){return cs.getPropertyValue(_610);}}
catch(e){if(node.style.getPropertyValue){return node.style.getPropertyValue(_610);}else{return _611;}}}else{if(node.currentStyle){return node.currentStyle[_612];}}}
if(node.style.getPropertyValue){return node.style.getPropertyValue(_610);}else{return _611;}};dojo.html.getStyleProperty=function(node,_615){node=dojo.byId(node);return (node&&node.style?node.style[dojo.html.toCamelCase(_615)]:undefined);};dojo.html.getStyle=function(node,_617){var _618=dojo.html.getStyleProperty(node,_617);return (_618?_618:dojo.html.getComputedStyle(node,_617));};dojo.html.setStyle=function(node,_61a,_61b){node=dojo.byId(node);if(node&&node.style){var _61c=dojo.html.toCamelCase(_61a);node.style[_61c]=_61b;}};dojo.html.setStyleText=function(_61d,text){try{_61d.style.cssText=text;}
catch(e){_61d.setAttribute("style",text);}};dojo.html.copyStyle=function(_61f,_620){if(!_620.style.cssText){_61f.setAttribute("style",_620.getAttribute("style"));}else{_61f.style.cssText=_620.style.cssText;}
dojo.html.addClass(_61f,dojo.html.getClass(_620));};dojo.html.getUnitValue=function(node,_622,_623){var s=dojo.html.getComputedStyle(node,_622);if((!s)||((s=="auto")&&(_623))){return {value:0,units:"px"};}
var _625=s.match(/(\-?[\d.]+)([a-z%]*)/i);if(!_625){return dojo.html.getUnitValue.bad;}
return {value:Number(_625[1]),units:_625[2].toLowerCase()};};dojo.html.getUnitValue.bad={value:NaN,units:""};dojo.html.getPixelValue=function(node,_627,_628){var _629=dojo.html.getUnitValue(node,_627,_628);if(isNaN(_629.value)){return 0;}
if((_629.value)&&(_629.units!="px")){return NaN;}
return _629.value;};dojo.html.setPositivePixelValue=function(node,_62b,_62c){if(isNaN(_62c)){return false;}
node.style[_62b]=Math.max(0,_62c)+"px";return true;};dojo.html.styleSheet=null;dojo.html.insertCssRule=function(_62d,_62e,_62f){if(!dojo.html.styleSheet){if(document.createStyleSheet){dojo.html.styleSheet=document.createStyleSheet();}else{if(document.styleSheets[0]){dojo.html.styleSheet=document.styleSheets[0];}else{return null;}}}
if(arguments.length<3){if(dojo.html.styleSheet.cssRules){_62f=dojo.html.styleSheet.cssRules.length;}else{if(dojo.html.styleSheet.rules){_62f=dojo.html.styleSheet.rules.length;}else{return null;}}}
if(dojo.html.styleSheet.insertRule){var rule=_62d+" { "+_62e+" }";return dojo.html.styleSheet.insertRule(rule,_62f);}else{if(dojo.html.styleSheet.addRule){return dojo.html.styleSheet.addRule(_62d,_62e,_62f);}else{return null;}}};dojo.html.removeCssRule=function(_631){if(!dojo.html.styleSheet){dojo.debug("no stylesheet defined for removing rules");return false;}
if(dojo.render.html.ie){if(!_631){_631=dojo.html.styleSheet.rules.length;dojo.html.styleSheet.removeRule(_631);}}else{if(document.styleSheets[0]){if(!_631){_631=dojo.html.styleSheet.cssRules.length;}
dojo.html.styleSheet.deleteRule(_631);}}
return true;};dojo.html._insertedCssFiles=[];dojo.html.insertCssFile=function(URI,doc,_634,_635){if(!URI){return;}
if(!doc){doc=document;}
var _636=dojo.hostenv.getText(URI,false,_635);if(_636===null){return;}
_636=dojo.html.fixPathsInCssText(_636,URI);if(_634){var idx=-1,node,ent=dojo.html._insertedCssFiles;for(var i=0;i<ent.length;i++){if((ent[i].doc==doc)&&(ent[i].cssText==_636)){idx=i;node=ent[i].nodeRef;break;}}
if(node){var _63b=doc.getElementsByTagName("style");for(var i=0;i<_63b.length;i++){if(_63b[i]==node){return;}}
dojo.html._insertedCssFiles.shift(idx,1);}}
var _63c=dojo.html.insertCssText(_636,doc);dojo.html._insertedCssFiles.push({"doc":doc,"cssText":_636,"nodeRef":_63c});if(_63c&&djConfig.isDebug){_63c.setAttribute("dbgHref",URI);}
return _63c;};dojo.html.insertCssText=function(_63d,doc,URI){if(!_63d){return;}
if(!doc){doc=document;}
if(URI){_63d=dojo.html.fixPathsInCssText(_63d,URI);}
var _640=doc.createElement("style");_640.setAttribute("type","text/css");var head=doc.getElementsByTagName("head")[0];if(!head){dojo.debug("No head tag in document, aborting styles");return;}else{head.appendChild(_640);}
if(_640.styleSheet){var _642=function(){try{_640.styleSheet.cssText=_63d;}
catch(e){dojo.debug(e);}};if(_640.styleSheet.disabled){setTimeout(_642,10);}else{_642();}}else{var _643=doc.createTextNode(_63d);_640.appendChild(_643);}
return _640;};dojo.html.fixPathsInCssText=function(_644,URI){if(!_644||!URI){return;}
var _646,str="",url="",_649="[\\t\\s\\w\\(\\)\\/\\.\\\\'\"-:#=&?~]+";var _64a=new RegExp("url\\(\\s*("+_649+")\\s*\\)");var _64b=/(file|https?|ftps?):\/\//;regexTrim=new RegExp("^[\\s]*(['\"]?)("+_649+")\\1[\\s]*?$");if(dojo.render.html.ie55||dojo.render.html.ie60){var _64c=new RegExp("AlphaImageLoader\\((.*)src=['\"]("+_649+")['\"]");while(_646=_64c.exec(_644)){url=_646[2].replace(regexTrim,"$2");if(!_64b.exec(url)){url=(new dojo.uri.Uri(URI,url).toString());}
str+=_644.substring(0,_646.index)+"AlphaImageLoader("+_646[1]+"src='"+url+"'";_644=_644.substr(_646.index+_646[0].length);}
_644=str+_644;str="";}
while(_646=_64a.exec(_644)){url=_646[1].replace(regexTrim,"$2");if(!_64b.exec(url)){url=(new dojo.uri.Uri(URI,url).toString());}
str+=_644.substring(0,_646.index)+"url("+url+")";_644=_644.substr(_646.index+_646[0].length);}
return str+_644;};dojo.html.setActiveStyleSheet=function(_64d){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")){a.disabled=true;if(a.getAttribute("title")==_64d){a.disabled=false;}}}};dojo.html.getActiveStyleSheet=function(){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")&&!a.disabled){return a.getAttribute("title");}}
return null;};dojo.html.getPreferredStyleSheet=function(){var i=0,a,els=dojo.doc().getElementsByTagName("link");while(a=els[i++]){if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("rel").indexOf("alt")==-1&&a.getAttribute("title")){return a.getAttribute("title");}}
return null;};dojo.html.applyBrowserClass=function(node){var drh=dojo.render.html;var _659={dj_ie:drh.ie,dj_ie55:drh.ie55,dj_ie6:drh.ie60,dj_ie7:drh.ie70,dj_iequirks:drh.ie&&drh.quirks,dj_opera:drh.opera,dj_opera8:drh.opera&&(Math.floor(dojo.render.version)==8),dj_opera9:drh.opera&&(Math.floor(dojo.render.version)==9),dj_khtml:drh.khtml,dj_safari:drh.safari,dj_gecko:drh.mozilla};for(var p in _659){if(_659[p]){dojo.html.addClass(node,p);}}};dojo.provide("dojo.widget.DomWidget");dojo.widget._cssFiles={};dojo.widget._cssStrings={};dojo.widget._templateCache={};dojo.widget.defaultStrings={dojoRoot:dojo.hostenv.getBaseScriptUri(),baseScriptUri:dojo.hostenv.getBaseScriptUri()};dojo.widget.fillFromTemplateCache=function(obj,_65c,_65d,_65e){var _65f=_65c||obj.templatePath;var _660=dojo.widget._templateCache;if(!_65f&&!obj["widgetType"]){do{var _661="__dummyTemplate__"+dojo.widget._templateCache.dummyCount++;}while(_660[_661]);obj.widgetType=_661;}
var wt=_65f?_65f.toString():obj.widgetType;var ts=_660[wt];if(!ts){_660[wt]={"string":null,"node":null};if(_65e){ts={};}else{ts=_660[wt];}}
if((!obj.templateString)&&(!_65e)){obj.templateString=_65d||ts["string"];}
if((!obj.templateNode)&&(!_65e)){obj.templateNode=ts["node"];}
if((!obj.templateNode)&&(!obj.templateString)&&(_65f)){var _664=dojo.hostenv.getText(_65f);if(_664){_664=_664.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im,"");var _665=_664.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);if(_665){_664=_665[1];}}else{_664="";}
obj.templateString=_664;if(!_65e){_660[wt]["string"]=_664;}}
if((!ts["string"])&&(!_65e)){ts.string=obj.templateString;}};dojo.widget._templateCache.dummyCount=0;dojo.widget.attachProperties=["dojoAttachPoint","id"];dojo.widget.eventAttachProperty="dojoAttachEvent";dojo.widget.onBuildProperty="dojoOnBuild";dojo.widget.waiNames=["waiRole","waiState"];dojo.widget.wai={waiRole:{name:"waiRole","namespace":"http://www.w3.org/TR/xhtml2",alias:"x2",prefix:"wairole:"},waiState:{name:"waiState","namespace":"http://www.w3.org/2005/07/aaa",alias:"aaa",prefix:""},setAttr:function(node,ns,attr,_669){if(dojo.render.html.ie){node.setAttribute(this[ns].alias+":"+attr,this[ns].prefix+_669);}else{node.setAttributeNS(this[ns]["namespace"],attr,this[ns].prefix+_669);}},getAttr:function(node,ns,attr){if(dojo.render.html.ie){return node.getAttribute(this[ns].alias+":"+attr);}else{return node.getAttributeNS(this[ns]["namespace"],attr);}},removeAttr:function(node,ns,attr){var _670=true;if(dojo.render.html.ie){_670=node.removeAttribute(this[ns].alias+":"+attr);}else{node.removeAttributeNS(this[ns]["namespace"],attr);}
return _670;}};dojo.widget.attachTemplateNodes=function(_671,_672,_673){var _674=dojo.dom.ELEMENT_NODE;function trim(str){return str.replace(/^\s+|\s+$/g,"");}
if(!_671){_671=_672.domNode;}
if(_671.nodeType!=_674){return;}
var _676=_671.all||_671.getElementsByTagName("*");var _677=_672;for(var x=-1;x<_676.length;x++){var _679=(x==-1)?_671:_676[x];var _67a=[];if(!_672.widgetsInTemplate||!_679.getAttribute("dojoType")){for(var y=0;y<this.attachProperties.length;y++){var _67c=_679.getAttribute(this.attachProperties[y]);if(_67c){_67a=_67c.split(";");for(var z=0;z<_67a.length;z++){if(dojo.lang.isArray(_672[_67a[z]])){_672[_67a[z]].push(_679);}else{_672[_67a[z]]=_679;}}
break;}}
var _67e=_679.getAttribute(this.eventAttachProperty);if(_67e){var evts=_67e.split(";");for(var y=0;y<evts.length;y++){if((!evts[y])||(!evts[y].length)){continue;}
var _680=null;var tevt=trim(evts[y]);if(evts[y].indexOf(":")>=0){var _682=tevt.split(":");tevt=trim(_682[0]);_680=trim(_682[1]);}
if(!_680){_680=tevt;}
var tf=function(){var ntf=new String(_680);return function(evt){if(_677[ntf]){_677[ntf](dojo.event.browser.fixEvent(evt,this));}};}();dojo.event.browser.addListener(_679,tevt,tf,false,true);}}
for(var y=0;y<_673.length;y++){var _686=_679.getAttribute(_673[y]);if((_686)&&(_686.length)){var _680=null;var _687=_673[y].substr(4);_680=trim(_686);var _688=[_680];if(_680.indexOf(";")>=0){_688=dojo.lang.map(_680.split(";"),trim);}
for(var z=0;z<_688.length;z++){if(!_688[z].length){continue;}
var tf=function(){var ntf=new String(_688[z]);return function(evt){if(_677[ntf]){_677[ntf](dojo.event.browser.fixEvent(evt,this));}};}();dojo.event.browser.addListener(_679,_687,tf,false,true);}}}}
var _68b=_679.getAttribute(this.templateProperty);if(_68b){_672[_68b]=_679;}
dojo.lang.forEach(dojo.widget.waiNames,function(name){var wai=dojo.widget.wai[name];var val=_679.getAttribute(wai.name);if(val){if(val.indexOf("-")==-1){dojo.widget.wai.setAttr(_679,wai.name,"role",val);}else{var _68f=val.split("-");dojo.widget.wai.setAttr(_679,wai.name,_68f[0],_68f[1]);}}},this);var _690=_679.getAttribute(this.onBuildProperty);if(_690){eval("var node = baseNode; var widget = targetObj; "+_690);}}};dojo.widget.getDojoEventsFromStr=function(str){var re=/(dojoOn([a-z]+)(\s?))=/gi;var evts=str?str.match(re)||[]:[];var ret=[];var lem={};for(var x=0;x<evts.length;x++){if(evts[x].length<1){continue;}
var cm=evts[x].replace(/\s/,"");cm=(cm.slice(0,cm.length-1));if(!lem[cm]){lem[cm]=true;ret.push(cm);}}
return ret;};dojo.declare("dojo.widget.DomWidget",dojo.widget.Widget,function(){if((arguments.length>0)&&(typeof arguments[0]=="object")){this.create(arguments[0]);}},{templateNode:null,templateString:null,templateCssString:null,preventClobber:false,domNode:null,containerNode:null,widgetsInTemplate:false,addChild:function(_698,_699,pos,ref,_69c){if(!this.isContainer){dojo.debug("dojo.widget.DomWidget.addChild() attempted on non-container widget");return null;}else{if(_69c==undefined){_69c=this.children.length;}
this.addWidgetAsDirectChild(_698,_699,pos,ref,_69c);this.registerChild(_698,_69c);}
return _698;},addWidgetAsDirectChild:function(_69d,_69e,pos,ref,_6a1){if((!this.containerNode)&&(!_69e)){this.containerNode=this.domNode;}
var cn=(_69e)?_69e:this.containerNode;if(!pos){pos="after";}
if(!ref){if(!cn){cn=dojo.body();}
ref=cn.lastChild;}
if(!_6a1){_6a1=0;}
_69d.domNode.setAttribute("dojoinsertionindex",_6a1);if(!ref){cn.appendChild(_69d.domNode);}else{if(pos=="insertAtIndex"){dojo.dom.insertAtIndex(_69d.domNode,ref.parentNode,_6a1);}else{if((pos=="after")&&(ref===cn.lastChild)){cn.appendChild(_69d.domNode);}else{dojo.dom.insertAtPosition(_69d.domNode,cn,pos);}}}},registerChild:function(_6a3,_6a4){_6a3.dojoInsertionIndex=_6a4;var idx=-1;for(var i=0;i<this.children.length;i++){if(this.children[i].dojoInsertionIndex<=_6a4){idx=i;}}
this.children.splice(idx+1,0,_6a3);_6a3.parent=this;_6a3.addedTo(this,idx+1);delete dojo.widget.manager.topWidgets[_6a3.widgetId];},removeChild:function(_6a7){dojo.dom.removeNode(_6a7.domNode);return dojo.widget.DomWidget.superclass.removeChild.call(this,_6a7);},getFragNodeRef:function(frag){if(!frag){return null;}
if(!frag[this.getNamespacedType()]){dojo.raise("Error: no frag for widget type "+this.getNamespacedType()+", id "+this.widgetId+" (maybe a widget has set it's type incorrectly)");}
return frag[this.getNamespacedType()]["nodeRef"];},postInitialize:function(args,frag,_6ab){var _6ac=this.getFragNodeRef(frag);if(_6ab&&(_6ab.snarfChildDomOutput||!_6ac)){_6ab.addWidgetAsDirectChild(this,"","insertAtIndex","",args["dojoinsertionindex"],_6ac);}else{if(_6ac){if(this.domNode&&(this.domNode!==_6ac)){dojo.dom.replaceNode(_6ac,this.domNode);}}}
if(_6ab){_6ab.registerChild(this,args.dojoinsertionindex);}else{dojo.widget.manager.topWidgets[this.widgetId]=this;}
if(this.widgetsInTemplate){var _6ad=new dojo.xml.Parse();var _6ae;var _6af=this.domNode.getElementsByTagName("*");for(var i=0;i<_6af.length;i++){if(_6af[i].getAttribute("dojoAttachPoint")=="subContainerWidget"){_6ae=_6af[i];}
if(_6af[i].getAttribute("dojoType")){_6af[i].setAttribute("_isSubWidget",true);}}
if(this.isContainer&&!this.containerNode){if(_6ae){var src=this.getFragNodeRef(frag);if(src){dojo.dom.moveChildren(src,_6ae);frag["dojoDontFollow"]=true;}}else{dojo.debug("No subContainerWidget node can be found in template file for widget "+this);}}
var _6b2=_6ad.parseElement(this.domNode,null,true);dojo.widget.getParser().createSubComponents(_6b2,this);var _6b3=[];var _6b4=[this];var w;while((w=_6b4.pop())){for(var i=0;i<w.children.length;i++){var _6b6=w.children[i];if(_6b6._processedSubWidgets||!_6b6.extraArgs["_issubwidget"]){continue;}
_6b3.push(_6b6);if(_6b6.isContainer){_6b4.push(_6b6);}}}
for(var i=0;i<_6b3.length;i++){var _6b7=_6b3[i];if(_6b7._processedSubWidgets){dojo.debug("This should not happen: widget._processedSubWidgets is already true!");return;}
_6b7._processedSubWidgets=true;if(_6b7.extraArgs["dojoattachevent"]){var evts=_6b7.extraArgs["dojoattachevent"].split(";");for(var j=0;j<evts.length;j++){var _6ba=null;var tevt=dojo.string.trim(evts[j]);if(tevt.indexOf(":")>=0){var _6bc=tevt.split(":");tevt=dojo.string.trim(_6bc[0]);_6ba=dojo.string.trim(_6bc[1]);}
if(!_6ba){_6ba=tevt;}
if(dojo.lang.isFunction(_6b7[tevt])){dojo.event.kwConnect({srcObj:_6b7,srcFunc:tevt,targetObj:this,targetFunc:_6ba});}else{alert(tevt+" is not a function in widget "+_6b7);}}}
if(_6b7.extraArgs["dojoattachpoint"]){this[_6b7.extraArgs["dojoattachpoint"]]=_6b7;}}}
if(this.isContainer&&!frag["dojoDontFollow"]){dojo.widget.getParser().createSubComponents(frag,this);}},buildRendering:function(args,frag){var ts=dojo.widget._templateCache[this.widgetType];if(args["templatecsspath"]){args["templateCssPath"]=args["templatecsspath"];}
var _6c0=args["templateCssPath"]||this.templateCssPath;if(_6c0&&!dojo.widget._cssFiles[_6c0.toString()]){if((!this.templateCssString)&&(_6c0)){this.templateCssString=dojo.hostenv.getText(_6c0);this.templateCssPath=null;}
dojo.widget._cssFiles[_6c0.toString()]=true;}
if((this["templateCssString"])&&(!this.templateCssString["loaded"])){dojo.html.insertCssText(this.templateCssString,null,_6c0);if(!this.templateCssString){this.templateCssString="";}
this.templateCssString.loaded=true;}
if((!this.preventClobber)&&((this.templatePath)||(this.templateNode)||((this["templateString"])&&(this.templateString.length))||((typeof ts!="undefined")&&((ts["string"])||(ts["node"]))))){this.buildFromTemplate(args,frag);}else{this.domNode=this.getFragNodeRef(frag);}
this.fillInTemplate(args,frag);},buildFromTemplate:function(args,frag){var _6c3=false;if(args["templatepath"]){args["templatePath"]=args["templatepath"];}
dojo.widget.fillFromTemplateCache(this,args["templatePath"],null,_6c3);var ts=dojo.widget._templateCache[this.templatePath?this.templatePath.toString():this.widgetType];if((ts)&&(!_6c3)){if(!this.templateString.length){this.templateString=ts["string"];}
if(!this.templateNode){this.templateNode=ts["node"];}}
var _6c5=false;var node=null;var tstr=this.templateString;if((!this.templateNode)&&(this.templateString)){_6c5=this.templateString.match(/\$\{([^\}]+)\}/g);if(_6c5){var hash=this.strings||{};for(var key in dojo.widget.defaultStrings){if(dojo.lang.isUndefined(hash[key])){hash[key]=dojo.widget.defaultStrings[key];}}
for(var i=0;i<_6c5.length;i++){var key=_6c5[i];key=key.substring(2,key.length-1);var kval=(key.substring(0,5)=="this.")?dojo.lang.getObjPathValue(key.substring(5),this):hash[key];var _6cc;if((kval)||(dojo.lang.isString(kval))){_6cc=new String((dojo.lang.isFunction(kval))?kval.call(this,key,this.templateString):kval);while(_6cc.indexOf("\"")>-1){_6cc=_6cc.replace("\"","&quot;");}
tstr=tstr.replace(_6c5[i],_6cc);}}}else{this.templateNode=this.createNodesFromText(this.templateString,true)[0];if(!_6c3){ts.node=this.templateNode;}}}
if((!this.templateNode)&&(!_6c5)){dojo.debug("DomWidget.buildFromTemplate: could not create template");return false;}else{if(!_6c5){node=this.templateNode.cloneNode(true);if(!node){return false;}}else{node=this.createNodesFromText(tstr,true)[0];}}
this.domNode=node;this.attachTemplateNodes();if(this.isContainer&&this.containerNode){var src=this.getFragNodeRef(frag);if(src){dojo.dom.moveChildren(src,this.containerNode);}}},attachTemplateNodes:function(_6ce,_6cf){if(!_6ce){_6ce=this.domNode;}
if(!_6cf){_6cf=this;}
return dojo.widget.attachTemplateNodes(_6ce,_6cf,dojo.widget.getDojoEventsFromStr(this.templateString));},fillInTemplate:function(){},destroyRendering:function(){try{delete this.domNode;}
catch(e){}},createNodesFromText:function(){dojo.unimplemented("dojo.widget.DomWidget.createNodesFromText");}});dojo.provide("dojo.html.display");dojo.html._toggle=function(node,_6d1,_6d2){node=dojo.byId(node);_6d2(node,!_6d1(node));return _6d1(node);};dojo.html.show=function(node){node=dojo.byId(node);if(dojo.html.getStyleProperty(node,"display")=="none"){dojo.html.setStyle(node,"display",(node.dojoDisplayCache||""));node.dojoDisplayCache=undefined;}};dojo.html.hide=function(node){node=dojo.byId(node);if(typeof node["dojoDisplayCache"]=="undefined"){var d=dojo.html.getStyleProperty(node,"display");if(d!="none"){node.dojoDisplayCache=d;}}
dojo.html.setStyle(node,"display","none");};dojo.html.setShowing=function(node,_6d7){dojo.html[(_6d7?"show":"hide")](node);};dojo.html.isShowing=function(node){return (dojo.html.getStyleProperty(node,"display")!="none");};dojo.html.toggleShowing=function(node){return dojo.html._toggle(node,dojo.html.isShowing,dojo.html.setShowing);};dojo.html.displayMap={tr:"",td:"",th:"",img:"inline",span:"inline",input:"inline",button:"inline"};dojo.html.suggestDisplayByTagName=function(node){node=dojo.byId(node);if(node&&node.tagName){var tag=node.tagName.toLowerCase();return (tag in dojo.html.displayMap?dojo.html.displayMap[tag]:"block");}};dojo.html.setDisplay=function(node,_6dd){dojo.html.setStyle(node,"display",((_6dd instanceof String||typeof _6dd=="string")?_6dd:(_6dd?dojo.html.suggestDisplayByTagName(node):"none")));};dojo.html.isDisplayed=function(node){return (dojo.html.getComputedStyle(node,"display")!="none");};dojo.html.toggleDisplay=function(node){return dojo.html._toggle(node,dojo.html.isDisplayed,dojo.html.setDisplay);};dojo.html.setVisibility=function(node,_6e1){dojo.html.setStyle(node,"visibility",((_6e1 instanceof String||typeof _6e1=="string")?_6e1:(_6e1?"visible":"hidden")));};dojo.html.isVisible=function(node){return (dojo.html.getComputedStyle(node,"visibility")!="hidden");};dojo.html.toggleVisibility=function(node){return dojo.html._toggle(node,dojo.html.isVisible,dojo.html.setVisibility);};dojo.html.setOpacity=function(node,_6e5,_6e6){node=dojo.byId(node);var h=dojo.render.html;if(!_6e6){if(_6e5>=1){if(h.ie){dojo.html.clearOpacity(node);return;}else{_6e5=0.999999;}}else{if(_6e5<0){_6e5=0;}}}
if(h.ie){if(node.nodeName.toLowerCase()=="tr"){var tds=node.getElementsByTagName("td");for(var x=0;x<tds.length;x++){tds[x].style.filter="Alpha(Opacity="+_6e5*100+")";}}
node.style.filter="Alpha(Opacity="+_6e5*100+")";}else{if(h.moz){node.style.opacity=_6e5;node.style.MozOpacity=_6e5;}else{if(h.safari){node.style.opacity=_6e5;node.style.KhtmlOpacity=_6e5;}else{node.style.opacity=_6e5;}}}};dojo.html.clearOpacity=function(node){node=dojo.byId(node);var ns=node.style;var h=dojo.render.html;if(h.ie){try{if(node.filters&&node.filters.alpha){ns.filter="";}}
catch(e){}}else{if(h.moz){ns.opacity=1;ns.MozOpacity=1;}else{if(h.safari){ns.opacity=1;ns.KhtmlOpacity=1;}else{ns.opacity=1;}}}};dojo.html.getOpacity=function(node){node=dojo.byId(node);var h=dojo.render.html;if(h.ie){var opac=(node.filters&&node.filters.alpha&&typeof node.filters.alpha.opacity=="number"?node.filters.alpha.opacity:100)/100;}else{var opac=node.style.opacity||node.style.MozOpacity||node.style.KhtmlOpacity||1;}
return opac>=0.999999?1:Number(opac);};dojo.provide("dojo.html.layout");dojo.html.sumAncestorProperties=function(node,prop){node=dojo.byId(node);if(!node){return 0;}
var _6f2=0;while(node){if(dojo.html.getComputedStyle(node,"position")=="fixed"){return 0;}
var val=node[prop];if(val){_6f2+=val-0;if(node==dojo.body()){break;}}
node=node.parentNode;}
return _6f2;};dojo.html.setStyleAttributes=function(node,_6f5){node=dojo.byId(node);var _6f6=_6f5.replace(/(;)?\s*$/,"").split(";");for(var i=0;i<_6f6.length;i++){var _6f8=_6f6[i].split(":");var name=_6f8[0].replace(/\s*$/,"").replace(/^\s*/,"").toLowerCase();var _6fa=_6f8[1].replace(/\s*$/,"").replace(/^\s*/,"");switch(name){case "opacity":
dojo.html.setOpacity(node,_6fa);break;case "content-height":
dojo.html.setContentBox(node,{height:_6fa});break;case "content-width":
dojo.html.setContentBox(node,{width:_6fa});break;case "outer-height":
dojo.html.setMarginBox(node,{height:_6fa});break;case "outer-width":
dojo.html.setMarginBox(node,{width:_6fa});break;default:
node.style[dojo.html.toCamelCase(name)]=_6fa;}}};dojo.html.boxSizing={MARGIN_BOX:"margin-box",BORDER_BOX:"border-box",PADDING_BOX:"padding-box",CONTENT_BOX:"content-box"};dojo.html.getAbsolutePosition=dojo.html.abs=function(node,_6fc,_6fd){node=dojo.byId(node);var _6fe=dojo.doc();var ret={x:0,y:0};var bs=dojo.html.boxSizing;if(!_6fd){_6fd=bs.CONTENT_BOX;}
var _701=2;var _702;switch(_6fd){case bs.MARGIN_BOX:
_702=3;break;case bs.BORDER_BOX:
_702=2;break;case bs.PADDING_BOX:
default:
_702=1;break;case bs.CONTENT_BOX:
_702=0;break;}
var h=dojo.render.html;var db=_6fe["body"]||_6fe["documentElement"];if(h.ie){with(node.getBoundingClientRect()){ret.x=left-2;ret.y=top-2;}}else{if(_6fe["getBoxObjectFor"]){_701=1;try{var bo=_6fe.getBoxObjectFor(node);ret.x=bo.x-dojo.html.sumAncestorProperties(node,"scrollLeft");ret.y=bo.y-dojo.html.sumAncestorProperties(node,"scrollTop");}
catch(e){}}else{if(node["offsetParent"]){var _706;if((h.safari)&&(node.style.getPropertyValue("position")=="absolute")&&(node.parentNode==db)){_706=db;}else{_706=db.parentNode;}
if(node.parentNode!=db){var nd=node;if(dojo.render.html.opera){nd=db;}
ret.x-=dojo.html.sumAncestorProperties(nd,"scrollLeft");ret.y-=dojo.html.sumAncestorProperties(nd,"scrollTop");}
var _708=node;do{var n=_708["offsetLeft"];if(!h.opera||n>0){ret.x+=isNaN(n)?0:n;}
var m=_708["offsetTop"];ret.y+=isNaN(m)?0:m;_708=_708.offsetParent;}while((_708!=_706)&&(_708!=null));}else{if(node["x"]&&node["y"]){ret.x+=isNaN(node.x)?0:node.x;ret.y+=isNaN(node.y)?0:node.y;}}}}
if(_6fc){var _70b=dojo.html.getScroll();ret.y+=_70b.top;ret.x+=_70b.left;}
var _70c=[dojo.html.getPaddingExtent,dojo.html.getBorderExtent,dojo.html.getMarginExtent];if(_701>_702){for(var i=_702;i<_701;++i){ret.y+=_70c[i](node,"top");ret.x+=_70c[i](node,"left");}}else{if(_701<_702){for(var i=_702;i>_701;--i){ret.y-=_70c[i-1](node,"top");ret.x-=_70c[i-1](node,"left");}}}
ret.top=ret.y;ret.left=ret.x;return ret;};dojo.html.isPositionAbsolute=function(node){return (dojo.html.getComputedStyle(node,"position")=="absolute");};dojo.html._sumPixelValues=function(node,_710,_711){var _712=0;for(var x=0;x<_710.length;x++){_712+=dojo.html.getPixelValue(node,_710[x],_711);}
return _712;};dojo.html.getMargin=function(node){return {width:dojo.html._sumPixelValues(node,["margin-left","margin-right"],(dojo.html.getComputedStyle(node,"position")=="absolute")),height:dojo.html._sumPixelValues(node,["margin-top","margin-bottom"],(dojo.html.getComputedStyle(node,"position")=="absolute"))};};dojo.html.getBorder=function(node){return {width:dojo.html.getBorderExtent(node,"left")+dojo.html.getBorderExtent(node,"right"),height:dojo.html.getBorderExtent(node,"top")+dojo.html.getBorderExtent(node,"bottom")};};dojo.html.getBorderExtent=function(node,side){return (dojo.html.getStyle(node,"border-"+side+"-style")=="none"?0:dojo.html.getPixelValue(node,"border-"+side+"-width"));};dojo.html.getMarginExtent=function(node,side){return dojo.html._sumPixelValues(node,["margin-"+side],dojo.html.isPositionAbsolute(node));};dojo.html.getPaddingExtent=function(node,side){return dojo.html._sumPixelValues(node,["padding-"+side],true);};dojo.html.getPadding=function(node){return {width:dojo.html._sumPixelValues(node,["padding-left","padding-right"],true),height:dojo.html._sumPixelValues(node,["padding-top","padding-bottom"],true)};};dojo.html.getPadBorder=function(node){var pad=dojo.html.getPadding(node);var _71f=dojo.html.getBorder(node);return {width:pad.width+_71f.width,height:pad.height+_71f.height};};dojo.html.getBoxSizing=function(node){var h=dojo.render.html;var bs=dojo.html.boxSizing;if((h.ie)||(h.opera)){var cm=document["compatMode"];if((cm=="BackCompat")||(cm=="QuirksMode")){return bs.BORDER_BOX;}else{return bs.CONTENT_BOX;}}else{if(arguments.length==0){node=document.documentElement;}
var _724=dojo.html.getStyle(node,"-moz-box-sizing");if(!_724){_724=dojo.html.getStyle(node,"box-sizing");}
return (_724?_724:bs.CONTENT_BOX);}};dojo.html.isBorderBox=function(node){return (dojo.html.getBoxSizing(node)==dojo.html.boxSizing.BORDER_BOX);};dojo.html.getBorderBox=function(node){node=dojo.byId(node);return {width:node.offsetWidth,height:node.offsetHeight};};dojo.html.getPaddingBox=function(node){var box=dojo.html.getBorderBox(node);var _729=dojo.html.getBorder(node);return {width:box.width-_729.width,height:box.height-_729.height};};dojo.html.getContentBox=function(node){node=dojo.byId(node);var _72b=dojo.html.getPadBorder(node);return {width:node.offsetWidth-_72b.width,height:node.offsetHeight-_72b.height};};dojo.html.setContentBox=function(node,args){node=dojo.byId(node);var _72e=0;var _72f=0;var isbb=dojo.html.isBorderBox(node);var _731=(isbb?dojo.html.getPadBorder(node):{width:0,height:0});var ret={};if(typeof args.width!="undefined"){_72e=args.width+_731.width;ret.width=dojo.html.setPositivePixelValue(node,"width",_72e);}
if(typeof args.height!="undefined"){_72f=args.height+_731.height;ret.height=dojo.html.setPositivePixelValue(node,"height",_72f);}
return ret;};dojo.html.getMarginBox=function(node){var _734=dojo.html.getBorderBox(node);var _735=dojo.html.getMargin(node);return {width:_734.width+_735.width,height:_734.height+_735.height};};dojo.html.setMarginBox=function(node,args){node=dojo.byId(node);var _738=0;var _739=0;var isbb=dojo.html.isBorderBox(node);var _73b=(!isbb?dojo.html.getPadBorder(node):{width:0,height:0});var _73c=dojo.html.getMargin(node);var ret={};if(typeof args.width!="undefined"){_738=args.width-_73b.width;_738-=_73c.width;ret.width=dojo.html.setPositivePixelValue(node,"width",_738);}
if(typeof args.height!="undefined"){_739=args.height-_73b.height;_739-=_73c.height;ret.height=dojo.html.setPositivePixelValue(node,"height",_739);}
return ret;};dojo.html.getElementBox=function(node,type){var bs=dojo.html.boxSizing;switch(type){case bs.MARGIN_BOX:
return dojo.html.getMarginBox(node);case bs.BORDER_BOX:
return dojo.html.getBorderBox(node);case bs.PADDING_BOX:
return dojo.html.getPaddingBox(node);case bs.CONTENT_BOX:
default:
return dojo.html.getContentBox(node);}};dojo.html.toCoordinateObject=dojo.html.toCoordinateArray=function(_741,_742,_743){if(!_741.nodeType&&!(_741 instanceof String||typeof _741=="string")&&("width" in _741||"height" in _741||"left" in _741||"x" in _741||"top" in _741||"y" in _741)){var ret={left:_741.left||_741.x||0,top:_741.top||_741.y||0,width:_741.width||0,height:_741.height||0};}else{var node=dojo.byId(_741);var pos=dojo.html.abs(node,_742,_743);var _747=dojo.html.getMarginBox(node);var ret={left:pos.left,top:pos.top,width:_747.width,height:_747.height};}
ret.x=ret.left;ret.y=ret.top;return ret;};dojo.html.setMarginBoxWidth=dojo.html.setOuterWidth=function(node,_749){return dojo.html._callDeprecated("setMarginBoxWidth","setMarginBox",arguments,"width");};dojo.html.setMarginBoxHeight=dojo.html.setOuterHeight=function(){return dojo.html._callDeprecated("setMarginBoxHeight","setMarginBox",arguments,"height");};dojo.html.getMarginBoxWidth=dojo.html.getOuterWidth=function(){return dojo.html._callDeprecated("getMarginBoxWidth","getMarginBox",arguments,null,"width");};dojo.html.getMarginBoxHeight=dojo.html.getOuterHeight=function(){return dojo.html._callDeprecated("getMarginBoxHeight","getMarginBox",arguments,null,"height");};dojo.html.getTotalOffset=function(node,type,_74c){return dojo.html._callDeprecated("getTotalOffset","getAbsolutePosition",arguments,null,type);};dojo.html.getAbsoluteX=function(node,_74e){return dojo.html._callDeprecated("getAbsoluteX","getAbsolutePosition",arguments,null,"x");};dojo.html.getAbsoluteY=function(node,_750){return dojo.html._callDeprecated("getAbsoluteY","getAbsolutePosition",arguments,null,"y");};dojo.html.totalOffsetLeft=function(node,_752){return dojo.html._callDeprecated("totalOffsetLeft","getAbsolutePosition",arguments,null,"left");};dojo.html.totalOffsetTop=function(node,_754){return dojo.html._callDeprecated("totalOffsetTop","getAbsolutePosition",arguments,null,"top");};dojo.html.getMarginWidth=function(node){return dojo.html._callDeprecated("getMarginWidth","getMargin",arguments,null,"width");};dojo.html.getMarginHeight=function(node){return dojo.html._callDeprecated("getMarginHeight","getMargin",arguments,null,"height");};dojo.html.getBorderWidth=function(node){return dojo.html._callDeprecated("getBorderWidth","getBorder",arguments,null,"width");};dojo.html.getBorderHeight=function(node){return dojo.html._callDeprecated("getBorderHeight","getBorder",arguments,null,"height");};dojo.html.getPaddingWidth=function(node){return dojo.html._callDeprecated("getPaddingWidth","getPadding",arguments,null,"width");};dojo.html.getPaddingHeight=function(node){return dojo.html._callDeprecated("getPaddingHeight","getPadding",arguments,null,"height");};dojo.html.getPadBorderWidth=function(node){return dojo.html._callDeprecated("getPadBorderWidth","getPadBorder",arguments,null,"width");};dojo.html.getPadBorderHeight=function(node){return dojo.html._callDeprecated("getPadBorderHeight","getPadBorder",arguments,null,"height");};dojo.html.getBorderBoxWidth=dojo.html.getInnerWidth=function(){return dojo.html._callDeprecated("getBorderBoxWidth","getBorderBox",arguments,null,"width");};dojo.html.getBorderBoxHeight=dojo.html.getInnerHeight=function(){return dojo.html._callDeprecated("getBorderBoxHeight","getBorderBox",arguments,null,"height");};dojo.html.getContentBoxWidth=dojo.html.getContentWidth=function(){return dojo.html._callDeprecated("getContentBoxWidth","getContentBox",arguments,null,"width");};dojo.html.getContentBoxHeight=dojo.html.getContentHeight=function(){return dojo.html._callDeprecated("getContentBoxHeight","getContentBox",arguments,null,"height");};dojo.html.setContentBoxWidth=dojo.html.setContentWidth=function(node,_75e){return dojo.html._callDeprecated("setContentBoxWidth","setContentBox",arguments,"width");};dojo.html.setContentBoxHeight=dojo.html.setContentHeight=function(node,_760){return dojo.html._callDeprecated("setContentBoxHeight","setContentBox",arguments,"height");};dojo.provide("dojo.html.util");dojo.html.getElementWindow=function(_761){return dojo.html.getDocumentWindow(_761.ownerDocument);};dojo.html.getDocumentWindow=function(doc){if(dojo.render.html.safari&&!doc._parentWindow){var fix=function(win){win.document._parentWindow=win;for(var i=0;i<win.frames.length;i++){fix(win.frames[i]);}};fix(window.top);}
if(dojo.render.html.ie&&window!==document.parentWindow&&!doc._parentWindow){doc.parentWindow.execScript("document._parentWindow = window;","Javascript");var win=doc._parentWindow;doc._parentWindow=null;return win;}
return doc._parentWindow||doc.parentWindow||doc.defaultView;};dojo.html.getAbsolutePositionExt=function(node,_768,_769,_76a){var _76b=dojo.html.getElementWindow(node);var ret=dojo.withGlobal(_76b,"getAbsolutePosition",dojo.html,arguments);var win=dojo.html.getElementWindow(node);if(_76a!=win&&win.frameElement){var ext=dojo.html.getAbsolutePositionExt(win.frameElement,_768,_769,_76a);ret.x+=ext.x;ret.y+=ext.y;}
ret.top=ret.y;ret.left=ret.x;return ret;};dojo.html.gravity=function(node,e){node=dojo.byId(node);var _771=dojo.html.getCursorPosition(e);with(dojo.html){var _772=getAbsolutePosition(node,true);var bb=getBorderBox(node);var _774=_772.x+(bb.width/2);var _775=_772.y+(bb.height/2);}
with(dojo.html.gravity){return ((_771.x<_774?WEST:EAST)|(_771.y<_775?NORTH:SOUTH));}};dojo.html.gravity.NORTH=1;dojo.html.gravity.SOUTH=1<<1;dojo.html.gravity.EAST=1<<2;dojo.html.gravity.WEST=1<<3;dojo.html.overElement=function(_776,e){_776=dojo.byId(_776);var _778=dojo.html.getCursorPosition(e);var bb=dojo.html.getBorderBox(_776);var _77a=dojo.html.getAbsolutePosition(_776,true,dojo.html.boxSizing.BORDER_BOX);var top=_77a.y;var _77c=top+bb.height;var left=_77a.x;var _77e=left+bb.width;return (_778.x>=left&&_778.x<=_77e&&_778.y>=top&&_778.y<=_77c);};dojo.html.renderedTextContent=function(node){node=dojo.byId(node);var _780="";if(node==null){return _780;}
for(var i=0;i<node.childNodes.length;i++){switch(node.childNodes[i].nodeType){case 1:
case 5:
var _782="unknown";try{_782=dojo.html.getStyle(node.childNodes[i],"display");}
catch(E){}
switch(_782){case "block":
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
_780+="\n";_780+=dojo.html.renderedTextContent(node.childNodes[i]);_780+="\n";break;case "none":
break;default:
if(node.childNodes[i].tagName&&node.childNodes[i].tagName.toLowerCase()=="br"){_780+="\n";}else{_780+=dojo.html.renderedTextContent(node.childNodes[i]);}
break;}
break;case 3:
case 2:
case 4:
var text=node.childNodes[i].nodeValue;var _784="unknown";try{_784=dojo.html.getStyle(node,"text-transform");}
catch(E){}
switch(_784){case "capitalize":
var _785=text.split(" ");for(var i=0;i<_785.length;i++){_785[i]=_785[i].charAt(0).toUpperCase()+_785[i].substring(1);}
text=_785.join(" ");break;case "uppercase":
text=text.toUpperCase();break;case "lowercase":
text=text.toLowerCase();break;default:
break;}
switch(_784){case "nowrap":
break;case "pre-wrap":
break;case "pre-line":
break;case "pre":
break;default:
text=text.replace(/\s+/," ");if(/\s$/.test(_780)){text.replace(/^\s/,"");}
break;}
_780+=text;break;default:
break;}}
return _780;};dojo.html.createNodesFromText=function(txt,trim){if(trim){txt=txt.replace(/^\s+|\s+$/g,"");}
var tn=dojo.doc().createElement("div");tn.style.visibility="hidden";dojo.body().appendChild(tn);var _789="none";if((/^<t[dh][\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table><tbody><tr>"+txt+"</tr></tbody></table>";_789="cell";}else{if((/^<tr[\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table><tbody>"+txt+"</tbody></table>";_789="row";}else{if((/^<(thead|tbody|tfoot)[\s\r\n>]/i).test(txt.replace(/^\s+/))){txt="<table>"+txt+"</table>";_789="section";}}}
tn.innerHTML=txt;if(tn["normalize"]){tn.normalize();}
var _78a=null;switch(_789){case "cell":
_78a=tn.getElementsByTagName("tr")[0];break;case "row":
_78a=tn.getElementsByTagName("tbody")[0];break;case "section":
_78a=tn.getElementsByTagName("table")[0];break;default:
_78a=tn;break;}
var _78b=[];for(var x=0;x<_78a.childNodes.length;x++){_78b.push(_78a.childNodes[x].cloneNode(true));}
tn.style.display="none";dojo.dom.removeNode(tn);return _78b;};dojo.html.placeOnScreen=function(node,_78e,_78f,_790,_791,_792,_793){if(_78e instanceof Array||typeof _78e=="array"){_793=_792;_792=_791;_791=_790;_790=_78f;_78f=_78e[1];_78e=_78e[0];}
if(_792 instanceof String||typeof _792=="string"){_792=_792.split(",");}
if(!isNaN(_790)){_790=[Number(_790),Number(_790)];}else{if(!(_790 instanceof Array||typeof _790=="array")){_790=[0,0];}}
var _794=dojo.html.getScroll().offset;var view=dojo.html.getViewport();node=dojo.byId(node);var _796=node.style.display;node.style.display="";var bb=dojo.html.getBorderBox(node);var w=bb.width;var h=bb.height;node.style.display=_796;if(!(_792 instanceof Array||typeof _792=="array")){_792=["TL"];}
var _79a,_79b,_79c=Infinity,_79d;for(var _79e=0;_79e<_792.length;++_79e){var _79f=_792[_79e];var _7a0=true;var tryX=_78e-(_79f.charAt(1)=="L"?0:w)+_790[0]*(_79f.charAt(1)=="L"?1:-1);var tryY=_78f-(_79f.charAt(0)=="T"?0:h)+_790[1]*(_79f.charAt(0)=="T"?1:-1);if(_791){tryX-=_794.x;tryY-=_794.y;}
if(tryX<0){tryX=0;_7a0=false;}
if(tryY<0){tryY=0;_7a0=false;}
var x=tryX+w;if(x>view.width){x=view.width-w;_7a0=false;}else{x=tryX;}
x=Math.max(_790[0],x)+_794.x;var y=tryY+h;if(y>view.height){y=view.height-h;_7a0=false;}else{y=tryY;}
y=Math.max(_790[1],y)+_794.y;if(_7a0){_79a=x;_79b=y;_79c=0;_79d=_79f;break;}else{var dist=Math.pow(x-tryX-_794.x,2)+Math.pow(y-tryY-_794.y,2);if(_79c>dist){_79c=dist;_79a=x;_79b=y;_79d=_79f;}}}
if(!_793){node.style.left=_79a+"px";node.style.top=_79b+"px";}
return {left:_79a,top:_79b,x:_79a,y:_79b,dist:_79c,corner:_79d};};dojo.html.placeOnScreenAroundElement=function(node,_7a7,_7a8,_7a9,_7aa,_7ab){var best,_7ad=Infinity;_7a7=dojo.byId(_7a7);var _7ae=_7a7.style.display;_7a7.style.display="";var mb=dojo.html.getElementBox(_7a7,_7a9);var _7b0=mb.width;var _7b1=mb.height;var _7b2=dojo.html.getAbsolutePosition(_7a7,true,_7a9);_7a7.style.display=_7ae;for(var _7b3 in _7aa){var pos,_7b5,_7b6;var _7b7=_7aa[_7b3];_7b5=_7b2.x+(_7b3.charAt(1)=="L"?0:_7b0);_7b6=_7b2.y+(_7b3.charAt(0)=="T"?0:_7b1);pos=dojo.html.placeOnScreen(node,_7b5,_7b6,_7a8,true,_7b7,true);if(pos.dist==0){best=pos;break;}else{if(_7ad>pos.dist){_7ad=pos.dist;best=pos;}}}
if(!_7ab){node.style.left=best.left+"px";node.style.top=best.top+"px";}
return best;};dojo.html.scrollIntoView=function(node){if(!node){return;}
if(dojo.render.html.ie){if(dojo.html.getBorderBox(node.parentNode).height<=node.parentNode.scrollHeight){node.scrollIntoView(false);}}else{if(dojo.render.html.mozilla){node.scrollIntoView(false);}else{var _7b9=node.parentNode;var _7ba=_7b9.scrollTop+dojo.html.getBorderBox(_7b9).height;var _7bb=node.offsetTop+dojo.html.getMarginBox(node).height;if(_7ba<_7bb){_7b9.scrollTop+=(_7bb-_7ba);}else{if(_7b9.scrollTop>node.offsetTop){_7b9.scrollTop-=(_7b9.scrollTop-node.offsetTop);}}}}};dojo.provide("dojo.gfx.color");dojo.gfx.color.Color=function(r,g,b,a){if(dojo.lang.isArray(r)){this.r=r[0];this.g=r[1];this.b=r[2];this.a=r[3]||1;}else{if(dojo.lang.isString(r)){var rgb=dojo.gfx.color.extractRGB(r);this.r=rgb[0];this.g=rgb[1];this.b=rgb[2];this.a=g||1;}else{if(r instanceof dojo.gfx.color.Color){this.r=r.r;this.b=r.b;this.g=r.g;this.a=r.a;}else{this.r=r;this.g=g;this.b=b;this.a=a;}}}};dojo.gfx.color.Color.fromArray=function(arr){return new dojo.gfx.color.Color(arr[0],arr[1],arr[2],arr[3]);};dojo.extend(dojo.gfx.color.Color,{toRgb:function(_7c2){if(_7c2){return this.toRgba();}else{return [this.r,this.g,this.b];}},toRgba:function(){return [this.r,this.g,this.b,this.a];},toHex:function(){return dojo.gfx.color.rgb2hex(this.toRgb());},toCss:function(){return "rgb("+this.toRgb().join()+")";},toString:function(){return this.toHex();},blend:function(_7c3,_7c4){var rgb=null;if(dojo.lang.isArray(_7c3)){rgb=_7c3;}else{if(_7c3 instanceof dojo.gfx.color.Color){rgb=_7c3.toRgb();}else{rgb=new dojo.gfx.color.Color(_7c3).toRgb();}}
return dojo.gfx.color.blend(this.toRgb(),rgb,_7c4);}});dojo.gfx.color.named={white:[255,255,255],black:[0,0,0],red:[255,0,0],green:[0,255,0],lime:[0,255,0],blue:[0,0,255],navy:[0,0,128],gray:[128,128,128],silver:[192,192,192]};dojo.gfx.color.blend=function(a,b,_7c8){if(typeof a=="string"){return dojo.gfx.color.blendHex(a,b,_7c8);}
if(!_7c8){_7c8=0;}
_7c8=Math.min(Math.max(-1,_7c8),1);_7c8=((_7c8+1)/2);var c=[];for(var x=0;x<3;x++){c[x]=parseInt(b[x]+((a[x]-b[x])*_7c8));}
return c;};dojo.gfx.color.blendHex=function(a,b,_7cd){return dojo.gfx.color.rgb2hex(dojo.gfx.color.blend(dojo.gfx.color.hex2rgb(a),dojo.gfx.color.hex2rgb(b),_7cd));};dojo.gfx.color.extractRGB=function(_7ce){var hex="0123456789abcdef";_7ce=_7ce.toLowerCase();if(_7ce.indexOf("rgb")==0){var _7d0=_7ce.match(/rgba*\((\d+), *(\d+), *(\d+)/i);var ret=_7d0.splice(1,3);return ret;}else{var _7d2=dojo.gfx.color.hex2rgb(_7ce);if(_7d2){return _7d2;}else{return dojo.gfx.color.named[_7ce]||[255,255,255];}}};dojo.gfx.color.hex2rgb=function(hex){var _7d4="0123456789ABCDEF";var rgb=new Array(3);if(hex.indexOf("#")==0){hex=hex.substring(1);}
hex=hex.toUpperCase();if(hex.replace(new RegExp("["+_7d4+"]","g"),"")!=""){return null;}
if(hex.length==3){rgb[0]=hex.charAt(0)+hex.charAt(0);rgb[1]=hex.charAt(1)+hex.charAt(1);rgb[2]=hex.charAt(2)+hex.charAt(2);}else{rgb[0]=hex.substring(0,2);rgb[1]=hex.substring(2,4);rgb[2]=hex.substring(4);}
for(var i=0;i<rgb.length;i++){rgb[i]=_7d4.indexOf(rgb[i].charAt(0))*16+_7d4.indexOf(rgb[i].charAt(1));}
return rgb;};dojo.gfx.color.rgb2hex=function(r,g,b){if(dojo.lang.isArray(r)){g=r[1]||0;b=r[2]||0;r=r[0]||0;}
var ret=dojo.lang.map([r,g,b],function(x){x=new Number(x);var s=x.toString(16);while(s.length<2){s="0"+s;}
return s;});ret.unshift("#");return ret.join("");};dojo.provide("dojo.lfx.Animation");dojo.lfx.Line=function(_7dd,end){this.start=_7dd;this.end=end;if(dojo.lang.isArray(_7dd)){var diff=[];dojo.lang.forEach(this.start,function(s,i){diff[i]=this.end[i]-s;},this);this.getValue=function(n){var res=[];dojo.lang.forEach(this.start,function(s,i){res[i]=(diff[i]*n)+s;},this);return res;};}else{var diff=end-_7dd;this.getValue=function(n){return (diff*n)+this.start;};}};dojo.lfx.easeDefault=function(n){if(dojo.render.html.khtml){return (parseFloat("0.5")+((Math.sin((n+parseFloat("1.5"))*Math.PI))/2));}else{return (0.5+((Math.sin((n+1.5)*Math.PI))/2));}};dojo.lfx.easeIn=function(n){return Math.pow(n,3);};dojo.lfx.easeOut=function(n){return (1-Math.pow(1-n,3));};dojo.lfx.easeInOut=function(n){return ((3*Math.pow(n,2))-(2*Math.pow(n,3)));};dojo.lfx.IAnimation=function(){};dojo.lang.extend(dojo.lfx.IAnimation,{curve:null,duration:1000,easing:null,repeatCount:0,rate:25,handler:null,beforeBegin:null,onBegin:null,onAnimate:null,onEnd:null,onPlay:null,onPause:null,onStop:null,play:null,pause:null,stop:null,connect:function(evt,_7ec,_7ed){if(!_7ed){_7ed=_7ec;_7ec=this;}
_7ed=dojo.lang.hitch(_7ec,_7ed);var _7ee=this[evt]||function(){};this[evt]=function(){var ret=_7ee.apply(this,arguments);_7ed.apply(this,arguments);return ret;};return this;},fire:function(evt,args){if(this[evt]){this[evt].apply(this,(args||[]));}
return this;},repeat:function(_7f2){this.repeatCount=_7f2;return this;},_active:false,_paused:false});dojo.lfx.Animation=function(_7f3,_7f4,_7f5,_7f6,_7f7,rate){dojo.lfx.IAnimation.call(this);if(dojo.lang.isNumber(_7f3)||(!_7f3&&_7f4.getValue)){rate=_7f7;_7f7=_7f6;_7f6=_7f5;_7f5=_7f4;_7f4=_7f3;_7f3=null;}else{if(_7f3.getValue||dojo.lang.isArray(_7f3)){rate=_7f6;_7f7=_7f5;_7f6=_7f4;_7f5=_7f3;_7f4=null;_7f3=null;}}
if(dojo.lang.isArray(_7f5)){this.curve=new dojo.lfx.Line(_7f5[0],_7f5[1]);}else{this.curve=_7f5;}
if(_7f4!=null&&_7f4>0){this.duration=_7f4;}
if(_7f7){this.repeatCount=_7f7;}
if(rate){this.rate=rate;}
if(_7f3){dojo.lang.forEach(["handler","beforeBegin","onBegin","onEnd","onPlay","onStop","onAnimate"],function(item){if(_7f3[item]){this.connect(item,_7f3[item]);}},this);}
if(_7f6&&dojo.lang.isFunction(_7f6)){this.easing=_7f6;}};dojo.inherits(dojo.lfx.Animation,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Animation,{_startTime:null,_endTime:null,_timer:null,_percent:0,_startRepeatCount:0,play:function(_7fa,_7fb){if(_7fb){clearTimeout(this._timer);this._active=false;this._paused=false;this._percent=0;}else{if(this._active&&!this._paused){return this;}}
this.fire("handler",["beforeBegin"]);this.fire("beforeBegin");if(_7fa>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_7fb);}),_7fa);return this;}
this._startTime=new Date().valueOf();if(this._paused){this._startTime-=(this.duration*this._percent/100);}
this._endTime=this._startTime+this.duration;this._active=true;this._paused=false;var step=this._percent/100;var _7fd=this.curve.getValue(step);if(this._percent==0){if(!this._startRepeatCount){this._startRepeatCount=this.repeatCount;}
this.fire("handler",["begin",_7fd]);this.fire("onBegin",[_7fd]);}
this.fire("handler",["play",_7fd]);this.fire("onPlay",[_7fd]);this._cycle();return this;},pause:function(){clearTimeout(this._timer);if(!this._active){return this;}
this._paused=true;var _7fe=this.curve.getValue(this._percent/100);this.fire("handler",["pause",_7fe]);this.fire("onPause",[_7fe]);return this;},gotoPercent:function(pct,_800){clearTimeout(this._timer);this._active=true;this._paused=true;this._percent=pct;if(_800){this.play();}
return this;},stop:function(_801){clearTimeout(this._timer);var step=this._percent/100;if(_801){step=1;}
var _803=this.curve.getValue(step);this.fire("handler",["stop",_803]);this.fire("onStop",[_803]);this._active=false;this._paused=false;return this;},status:function(){if(this._active){return this._paused?"paused":"playing";}else{return "stopped";}
return this;},_cycle:function(){clearTimeout(this._timer);if(this._active){var curr=new Date().valueOf();var step=(curr-this._startTime)/(this._endTime-this._startTime);if(step>=1){step=1;this._percent=100;}else{this._percent=step*100;}
if((this.easing)&&(dojo.lang.isFunction(this.easing))){step=this.easing(step);}
var _806=this.curve.getValue(step);this.fire("handler",["animate",_806]);this.fire("onAnimate",[_806]);if(step<1){this._timer=setTimeout(dojo.lang.hitch(this,"_cycle"),this.rate);}else{this._active=false;this.fire("handler",["end"]);this.fire("onEnd");if(this.repeatCount>0){this.repeatCount--;this.play(null,true);}else{if(this.repeatCount==-1){this.play(null,true);}else{if(this._startRepeatCount){this.repeatCount=this._startRepeatCount;this._startRepeatCount=0;}}}}}
return this;}});dojo.lfx.Combine=function(_807){dojo.lfx.IAnimation.call(this);this._anims=[];this._animsEnded=0;var _808=arguments;if(_808.length==1&&(dojo.lang.isArray(_808[0])||dojo.lang.isArrayLike(_808[0]))){_808=_808[0];}
dojo.lang.forEach(_808,function(anim){this._anims.push(anim);anim.connect("onEnd",dojo.lang.hitch(this,"_onAnimsEnded"));},this);};dojo.inherits(dojo.lfx.Combine,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Combine,{_animsEnded:0,play:function(_80a,_80b){if(!this._anims.length){return this;}
this.fire("beforeBegin");if(_80a>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_80b);}),_80a);return this;}
if(_80b||this._anims[0].percent==0){this.fire("onBegin");}
this.fire("onPlay");this._animsCall("play",null,_80b);return this;},pause:function(){this.fire("onPause");this._animsCall("pause");return this;},stop:function(_80c){this.fire("onStop");this._animsCall("stop",_80c);return this;},_onAnimsEnded:function(){this._animsEnded++;if(this._animsEnded>=this._anims.length){this.fire("onEnd");}
return this;},_animsCall:function(_80d){var args=[];if(arguments.length>1){for(var i=1;i<arguments.length;i++){args.push(arguments[i]);}}
var _810=this;dojo.lang.forEach(this._anims,function(anim){anim[_80d](args);},_810);return this;}});dojo.lfx.Chain=function(_812){dojo.lfx.IAnimation.call(this);this._anims=[];this._currAnim=-1;var _813=arguments;if(_813.length==1&&(dojo.lang.isArray(_813[0])||dojo.lang.isArrayLike(_813[0]))){_813=_813[0];}
var _814=this;dojo.lang.forEach(_813,function(anim,i,_817){this._anims.push(anim);if(i<_817.length-1){anim.connect("onEnd",dojo.lang.hitch(this,"_playNext"));}else{anim.connect("onEnd",dojo.lang.hitch(this,function(){this.fire("onEnd");}));}},this);};dojo.inherits(dojo.lfx.Chain,dojo.lfx.IAnimation);dojo.lang.extend(dojo.lfx.Chain,{_currAnim:-1,play:function(_818,_819){if(!this._anims.length){return this;}
if(_819||!this._anims[this._currAnim]){this._currAnim=0;}
var _81a=this._anims[this._currAnim];this.fire("beforeBegin");if(_818>0){setTimeout(dojo.lang.hitch(this,function(){this.play(null,_819);}),_818);return this;}
if(_81a){if(this._currAnim==0){this.fire("handler",["begin",this._currAnim]);this.fire("onBegin",[this._currAnim]);}
this.fire("onPlay",[this._currAnim]);_81a.play(null,_819);}
return this;},pause:function(){if(this._anims[this._currAnim]){this._anims[this._currAnim].pause();this.fire("onPause",[this._currAnim]);}
return this;},playPause:function(){if(this._anims.length==0){return this;}
if(this._currAnim==-1){this._currAnim=0;}
var _81b=this._anims[this._currAnim];if(_81b){if(!_81b._active||_81b._paused){this.play();}else{this.pause();}}
return this;},stop:function(){var _81c=this._anims[this._currAnim];if(_81c){_81c.stop();this.fire("onStop",[this._currAnim]);}
return _81c;},_playNext:function(){if(this._currAnim==-1||this._anims.length==0){return this;}
this._currAnim++;if(this._anims[this._currAnim]){this._anims[this._currAnim].play(null,true);}
return this;}});dojo.lfx.combine=function(_81d){var _81e=arguments;if(dojo.lang.isArray(arguments[0])){_81e=arguments[0];}
if(_81e.length==1){return _81e[0];}
return new dojo.lfx.Combine(_81e);};dojo.lfx.chain=function(_81f){var _820=arguments;if(dojo.lang.isArray(arguments[0])){_820=arguments[0];}
if(_820.length==1){return _820[0];}
return new dojo.lfx.Chain(_820);};dojo.provide("dojo.html.color");dojo.html.getBackgroundColor=function(node){node=dojo.byId(node);var _822;do{_822=dojo.html.getStyle(node,"background-color");if(_822.toLowerCase()=="rgba(0, 0, 0, 0)"){_822="transparent";}
if(node==document.getElementsByTagName("body")[0]){node=null;break;}
node=node.parentNode;}while(node&&dojo.lang.inArray(["transparent",""],_822));if(_822=="transparent"){_822=[255,255,255,0];}else{_822=dojo.gfx.color.extractRGB(_822);}
return _822;};dojo.provide("dojo.lfx.html");dojo.lfx.html._byId=function(_823){if(!_823){return [];}
if(dojo.lang.isArrayLike(_823)){if(!_823.alreadyChecked){var n=[];dojo.lang.forEach(_823,function(node){n.push(dojo.byId(node));});n.alreadyChecked=true;return n;}else{return _823;}}else{var n=[];n.push(dojo.byId(_823));n.alreadyChecked=true;return n;}};dojo.lfx.html.propertyAnimation=function(_826,_827,_828,_829,_82a){_826=dojo.lfx.html._byId(_826);var _82b={"propertyMap":_827,"nodes":_826,"duration":_828,"easing":_829||dojo.lfx.easeDefault};var _82c=function(args){if(args.nodes.length==1){var pm=args.propertyMap;if(!dojo.lang.isArray(args.propertyMap)){var parr=[];for(var _830 in pm){pm[_830].property=_830;parr.push(pm[_830]);}
pm=args.propertyMap=parr;}
dojo.lang.forEach(pm,function(prop){if(dj_undef("start",prop)){if(prop.property!="opacity"){prop.start=parseInt(dojo.html.getComputedStyle(args.nodes[0],prop.property));}else{prop.start=dojo.html.getOpacity(args.nodes[0]);}}});}};var _832=function(_833){var _834=[];dojo.lang.forEach(_833,function(c){_834.push(Math.round(c));});return _834;};var _836=function(n,_838){n=dojo.byId(n);if(!n||!n.style){return;}
for(var s in _838){try{if(s=="opacity"){dojo.html.setOpacity(n,_838[s]);}else{n.style[s]=_838[s];}}
catch(e){dojo.debug(e);}}};var _83a=function(_83b){this._properties=_83b;this.diffs=new Array(_83b.length);dojo.lang.forEach(_83b,function(prop,i){if(dojo.lang.isFunction(prop.start)){prop.start=prop.start(prop,i);}
if(dojo.lang.isFunction(prop.end)){prop.end=prop.end(prop,i);}
if(dojo.lang.isArray(prop.start)){this.diffs[i]=null;}else{if(prop.start instanceof dojo.gfx.color.Color){prop.startRgb=prop.start.toRgb();prop.endRgb=prop.end.toRgb();}else{this.diffs[i]=prop.end-prop.start;}}},this);this.getValue=function(n){var ret={};dojo.lang.forEach(this._properties,function(prop,i){var _842=null;if(dojo.lang.isArray(prop.start)){}else{if(prop.start instanceof dojo.gfx.color.Color){_842=(prop.units||"rgb")+"(";for(var j=0;j<prop.startRgb.length;j++){_842+=Math.round(((prop.endRgb[j]-prop.startRgb[j])*n)+prop.startRgb[j])+(j<prop.startRgb.length-1?",":"");}
_842+=")";}else{_842=((this.diffs[i])*n)+prop.start+(prop.property!="opacity"?prop.units||"px":"");}}
ret[dojo.html.toCamelCase(prop.property)]=_842;},this);return ret;};};var anim=new dojo.lfx.Animation({beforeBegin:function(){_82c(_82b);anim.curve=new _83a(_82b.propertyMap);},onAnimate:function(_845){dojo.lang.forEach(_82b.nodes,function(node){_836(node,_845);});}},_82b.duration,null,_82b.easing);if(_82a){for(var x in _82a){if(dojo.lang.isFunction(_82a[x])){anim.connect(x,anim,_82a[x]);}}}
return anim;};dojo.lfx.html._makeFadeable=function(_848){var _849=function(node){if(dojo.render.html.ie){if((node.style.zoom.length==0)&&(dojo.html.getStyle(node,"zoom")=="normal")){node.style.zoom="1";}
if((node.style.width.length==0)&&(dojo.html.getStyle(node,"width")=="auto")){node.style.width="auto";}}};if(dojo.lang.isArrayLike(_848)){dojo.lang.forEach(_848,_849);}else{_849(_848);}};dojo.lfx.html.fade=function(_84b,_84c,_84d,_84e,_84f){_84b=dojo.lfx.html._byId(_84b);var _850={property:"opacity"};if(!dj_undef("start",_84c)){_850.start=_84c.start;}else{_850.start=function(){return dojo.html.getOpacity(_84b[0]);};}
if(!dj_undef("end",_84c)){_850.end=_84c.end;}else{dojo.raise("dojo.lfx.html.fade needs an end value");}
var anim=dojo.lfx.propertyAnimation(_84b,[_850],_84d,_84e);anim.connect("beforeBegin",function(){dojo.lfx.html._makeFadeable(_84b);});if(_84f){anim.connect("onEnd",function(){_84f(_84b,anim);});}
return anim;};dojo.lfx.html.fadeIn=function(_852,_853,_854,_855){return dojo.lfx.html.fade(_852,{end:1},_853,_854,_855);};dojo.lfx.html.fadeOut=function(_856,_857,_858,_859){return dojo.lfx.html.fade(_856,{end:0},_857,_858,_859);};dojo.lfx.html.fadeShow=function(_85a,_85b,_85c,_85d){_85a=dojo.lfx.html._byId(_85a);dojo.lang.forEach(_85a,function(node){dojo.html.setOpacity(node,0);});var anim=dojo.lfx.html.fadeIn(_85a,_85b,_85c,_85d);anim.connect("beforeBegin",function(){if(dojo.lang.isArrayLike(_85a)){dojo.lang.forEach(_85a,dojo.html.show);}else{dojo.html.show(_85a);}});return anim;};dojo.lfx.html.fadeHide=function(_860,_861,_862,_863){var anim=dojo.lfx.html.fadeOut(_860,_861,_862,function(){if(dojo.lang.isArrayLike(_860)){dojo.lang.forEach(_860,dojo.html.hide);}else{dojo.html.hide(_860);}
if(_863){_863(_860,anim);}});return anim;};dojo.lfx.html.wipeIn=function(_865,_866,_867,_868){_865=dojo.lfx.html._byId(_865);var _869=[];dojo.lang.forEach(_865,function(node){var _86b={};var _86c,_86d,_86e;with(node.style){_86c=top;_86d=left;_86e=position;top="-9999px";left="-9999px";position="absolute";display="";}
var _86f=dojo.html.getBorderBox(node).height;with(node.style){top=_86c;left=_86d;position=_86e;display="none";}
var anim=dojo.lfx.propertyAnimation(node,{"height":{start:1,end:function(){return _86f;}}},_866,_867);anim.connect("beforeBegin",function(){_86b.overflow=node.style.overflow;_86b.height=node.style.height;with(node.style){overflow="hidden";_86f="1px";}
dojo.html.show(node);});anim.connect("onEnd",function(){with(node.style){overflow=_86b.overflow;_86f=_86b.height;}
if(_868){_868(node,anim);}});_869.push(anim);});return dojo.lfx.combine(_869);};dojo.lfx.html.wipeOut=function(_871,_872,_873,_874){_871=dojo.lfx.html._byId(_871);var _875=[];dojo.lang.forEach(_871,function(node){var _877={};var anim=dojo.lfx.propertyAnimation(node,{"height":{start:function(){return dojo.html.getContentBox(node).height;},end:1}},_872,_873,{"beforeBegin":function(){_877.overflow=node.style.overflow;_877.height=node.style.height;with(node.style){overflow="hidden";}
dojo.html.show(node);},"onEnd":function(){dojo.html.hide(node);with(node.style){overflow=_877.overflow;height=_877.height;}
if(_874){_874(node,anim);}}});_875.push(anim);});return dojo.lfx.combine(_875);};dojo.lfx.html.slideTo=function(_879,_87a,_87b,_87c,_87d){_879=dojo.lfx.html._byId(_879);var _87e=[];var _87f=dojo.html.getComputedStyle;dojo.lang.forEach(_879,function(node){var top=null;var left=null;var init=(function(){var _884=node;return function(){var pos=_87f(_884,"position");top=(pos=="absolute"?node.offsetTop:parseInt(_87f(node,"top"))||0);left=(pos=="absolute"?node.offsetLeft:parseInt(_87f(node,"left"))||0);if(!dojo.lang.inArray(["absolute","relative"],pos)){var ret=dojo.html.abs(_884,true);dojo.html.setStyleAttributes(_884,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");top=ret.y;left=ret.x;}};})();init();var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:(_87a.top||0)},"left":{start:left,end:(_87a.left||0)}},_87b,_87c,{"beforeBegin":init});if(_87d){anim.connect("onEnd",function(){_87d(_879,anim);});}
_87e.push(anim);});return dojo.lfx.combine(_87e);};dojo.lfx.html.slideBy=function(_888,_889,_88a,_88b,_88c){_888=dojo.lfx.html._byId(_888);var _88d=[];var _88e=dojo.html.getComputedStyle;dojo.lang.forEach(_888,function(node){var top=null;var left=null;var init=(function(){var _893=node;return function(){var pos=_88e(_893,"position");top=(pos=="absolute"?node.offsetTop:parseInt(_88e(node,"top"))||0);left=(pos=="absolute"?node.offsetLeft:parseInt(_88e(node,"left"))||0);if(!dojo.lang.inArray(["absolute","relative"],pos)){var ret=dojo.html.abs(_893,true);dojo.html.setStyleAttributes(_893,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");top=ret.y;left=ret.x;}};})();init();var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:top+(_889.top||0)},"left":{start:left,end:left+(_889.left||0)}},_88a,_88b).connect("beforeBegin",init);if(_88c){anim.connect("onEnd",function(){_88c(_888,anim);});}
_88d.push(anim);});return dojo.lfx.combine(_88d);};dojo.lfx.html.explode=function(_897,_898,_899,_89a,_89b){var h=dojo.html;_897=dojo.byId(_897);_898=dojo.byId(_898);var _89d=h.toCoordinateObject(_897,true);var _89e=document.createElement("div");h.copyStyle(_89e,_898);if(_898.explodeClassName){_89e.className=_898.explodeClassName;}
with(_89e.style){position="absolute";display="none";var _89f=h.getStyle(_897,"background-color");backgroundColor=_89f?_89f.toLowerCase():"transparent";backgroundColor=(backgroundColor=="transparent")?"rgb(221, 221, 221)":backgroundColor;}
dojo.body().appendChild(_89e);with(_898.style){visibility="hidden";display="block";}
var _8a0=h.toCoordinateObject(_898,true);with(_898.style){display="none";visibility="visible";}
var _8a1={opacity:{start:0.5,end:1}};dojo.lang.forEach(["height","width","top","left"],function(type){_8a1[type]={start:_89d[type],end:_8a0[type]};});var anim=new dojo.lfx.propertyAnimation(_89e,_8a1,_899,_89a,{"beforeBegin":function(){h.setDisplay(_89e,"block");},"onEnd":function(){h.setDisplay(_898,"block");_89e.parentNode.removeChild(_89e);}});if(_89b){anim.connect("onEnd",function(){_89b(_898,anim);});}
return anim;};dojo.lfx.html.implode=function(_8a4,end,_8a6,_8a7,_8a8){var h=dojo.html;_8a4=dojo.byId(_8a4);end=dojo.byId(end);var _8aa=dojo.html.toCoordinateObject(_8a4,true);var _8ab=dojo.html.toCoordinateObject(end,true);var _8ac=document.createElement("div");dojo.html.copyStyle(_8ac,_8a4);if(_8a4.explodeClassName){_8ac.className=_8a4.explodeClassName;}
dojo.html.setOpacity(_8ac,0.3);with(_8ac.style){position="absolute";display="none";backgroundColor=h.getStyle(_8a4,"background-color").toLowerCase();}
dojo.body().appendChild(_8ac);var _8ad={opacity:{start:1,end:0.5}};dojo.lang.forEach(["height","width","top","left"],function(type){_8ad[type]={start:_8aa[type],end:_8ab[type]};});var anim=new dojo.lfx.propertyAnimation(_8ac,_8ad,_8a6,_8a7,{"beforeBegin":function(){dojo.html.hide(_8a4);dojo.html.show(_8ac);},"onEnd":function(){_8ac.parentNode.removeChild(_8ac);}});if(_8a8){anim.connect("onEnd",function(){_8a8(_8a4,anim);});}
return anim;};dojo.lfx.html.highlight=function(_8b0,_8b1,_8b2,_8b3,_8b4){_8b0=dojo.lfx.html._byId(_8b0);var _8b5=[];dojo.lang.forEach(_8b0,function(node){var _8b7=dojo.html.getBackgroundColor(node);var bg=dojo.html.getStyle(node,"background-color").toLowerCase();var _8b9=dojo.html.getStyle(node,"background-image");var _8ba=(bg=="transparent"||bg=="rgba(0, 0, 0, 0)");while(_8b7.length>3){_8b7.pop();}
var rgb=new dojo.gfx.color.Color(_8b1);var _8bc=new dojo.gfx.color.Color(_8b7);var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:rgb,end:_8bc}},_8b2,_8b3,{"beforeBegin":function(){if(_8b9){node.style.backgroundImage="none";}
node.style.backgroundColor="rgb("+rgb.toRgb().join(",")+")";},"onEnd":function(){if(_8b9){node.style.backgroundImage=_8b9;}
if(_8ba){node.style.backgroundColor="transparent";}
if(_8b4){_8b4(node,anim);}}});_8b5.push(anim);});return dojo.lfx.combine(_8b5);};dojo.lfx.html.unhighlight=function(_8be,_8bf,_8c0,_8c1,_8c2){_8be=dojo.lfx.html._byId(_8be);var _8c3=[];dojo.lang.forEach(_8be,function(node){var _8c5=new dojo.gfx.color.Color(dojo.html.getBackgroundColor(node));var rgb=new dojo.gfx.color.Color(_8bf);var _8c7=dojo.html.getStyle(node,"background-image");var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:_8c5,end:rgb}},_8c0,_8c1,{"beforeBegin":function(){if(_8c7){node.style.backgroundImage="none";}
node.style.backgroundColor="rgb("+_8c5.toRgb().join(",")+")";},"onEnd":function(){if(_8c2){_8c2(node,anim);}}});_8c3.push(anim);});return dojo.lfx.combine(_8c3);};dojo.lang.mixin(dojo.lfx,dojo.lfx.html);dojo.provide("dojo.lfx.*");dojo.provide("dojo.lfx.toggle");dojo.lfx.toggle.plain={show:function(node,_8ca,_8cb,_8cc){dojo.html.show(node);if(dojo.lang.isFunction(_8cc)){_8cc();}},hide:function(node,_8ce,_8cf,_8d0){dojo.html.hide(node);if(dojo.lang.isFunction(_8d0)){_8d0();}}};dojo.lfx.toggle.fade={show:function(node,_8d2,_8d3,_8d4){dojo.lfx.fadeShow(node,_8d2,_8d3,_8d4).play();},hide:function(node,_8d6,_8d7,_8d8){dojo.lfx.fadeHide(node,_8d6,_8d7,_8d8).play();}};dojo.lfx.toggle.wipe={show:function(node,_8da,_8db,_8dc){dojo.lfx.wipeIn(node,_8da,_8db,_8dc).play();},hide:function(node,_8de,_8df,_8e0){dojo.lfx.wipeOut(node,_8de,_8df,_8e0).play();}};dojo.lfx.toggle.explode={show:function(node,_8e2,_8e3,_8e4,_8e5){dojo.lfx.explode(_8e5||{x:0,y:0,width:0,height:0},node,_8e2,_8e3,_8e4).play();},hide:function(node,_8e7,_8e8,_8e9,_8ea){dojo.lfx.implode(node,_8ea||{x:0,y:0,width:0,height:0},_8e7,_8e8,_8e9).play();}};dojo.provide("dojo.widget.HtmlWidget");dojo.declare("dojo.widget.HtmlWidget",dojo.widget.DomWidget,{templateCssPath:null,templatePath:null,lang:"",toggle:"plain",toggleDuration:150,initialize:function(args,frag){},postMixInProperties:function(args,frag){if(this.lang===""){this.lang=null;}
this.toggleObj=dojo.lfx.toggle[this.toggle.toLowerCase()]||dojo.lfx.toggle.plain;},createNodesFromText:function(txt,wrap){return dojo.html.createNodesFromText(txt,wrap);},destroyRendering:function(_8f1){try{if(this.bgIframe){this.bgIframe.remove();delete this.bgIframe;}
if(!_8f1&&this.domNode){dojo.event.browser.clean(this.domNode);}
dojo.dom.removeNode(this.domNode);delete this.domNode;}
catch(e){}},isShowing:function(){return dojo.html.isShowing(this.domNode);},toggleShowing:function(){if(this.isShowing()){this.hide();}else{this.show();}},show:function(){if(this.isShowing()){return;}
this.animationInProgress=true;this.toggleObj.show(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onShow),this.explodeSrc);},onShow:function(){this.animationInProgress=false;this.checkSize();},hide:function(){if(!this.isShowing()){return;}
this.animationInProgress=true;this.toggleObj.hide(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onHide),this.explodeSrc);},onHide:function(){this.animationInProgress=false;},_isResized:function(w,h){if(!this.isShowing()){return false;}
var wh=dojo.html.getMarginBox(this.domNode);var _8f5=w||wh.width;var _8f6=h||wh.height;if(this.width==_8f5&&this.height==_8f6){return false;}
this.width=_8f5;this.height=_8f6;return true;},checkSize:function(){if(!this._isResized()){return;}
this.onResized();},resizeTo:function(w,h){dojo.html.setMarginBox(this.domNode,{width:w,height:h});if(this.isShowing()){this.onResized();}},resizeSoon:function(){if(this.isShowing()){dojo.lang.setTimeout(this,this.onResized,0);}},onResized:function(){dojo.lang.forEach(this.children,function(_8f9){if(_8f9.checkSize){_8f9.checkSize();}});}});dojo.provide("dojo.widget.*");