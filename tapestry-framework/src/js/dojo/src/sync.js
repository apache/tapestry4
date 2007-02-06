
dojo.provide("dojo.sync");dojo.lang.mixin(dojo.sync, {onStart: null,onRefreshFiles: null,onUpload: null,onDownload: null,onFinished: null,onCancel: null,isSyncing: false,cancelled: false,successful: true,details: null,lastSync: null,autoSync: true,error: false,synchronize: function(){if(this.isSyncing == true
|| dojo.dot.goingOnline == true
|| dojo.dot.isOnline == false){return;}
this.isSyncing = true;this.successful = false;this.details = null;this.cancelled = false;this.start();},cancel: function(){if(this.isSyncing == false){return;}
this.cancelled = true;if(this.onCancel){this.onCancel();}},start: function(){if(this.cancelled == true){this.finished();return;}
if(this.onStart){this.onStart();}
this.refreshUI();},refreshUI: function(){if(this.cancelled == true){this.finished();return;}
if(this.onRefreshUI){this.onRefreshUI();}
dojo.dot.files.refresh(dojo.lang.hitch(this, function(error, errorMessage){if(error == true){this.error = true;this.successful = false;this.details = new Array();this.details.push(errorMessage);this.finished();}else{this.upload();}}));},upload: function(){if(this.cancelled == true){this.finished();return;}
if(this.onUpload){this.onUpload();}
window.setTimeout(dojo.lang.hitch(this, this.download), 2000);},download: function(){if(this.cancelled == true){this.finished();return;}
if(this.onDownload){this.onDownload();}
window.setTimeout(dojo.lang.hitch(this, this.finished), 2000);},finished: function(){this.isSyncing = false;if(this.cancelled == false && this.error == false){this.successful = true;this.details = ["The document 'foobar' had conflicts - yours was chosen","The document 'hello world' was automatically merged"];this.lastSync = new Date();}else{this.successful = false;}
if(this.onFinished){this.onFinished();}},isRecommended: function(){var modifiedItems = this.getNumModifiedItems();if(modifiedItems > 0){return true;}else{return false;}},getNumModifiedItems: function(){return 5;},save: function(){}});