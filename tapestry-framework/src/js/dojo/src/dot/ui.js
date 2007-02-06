
dojo.provide("dojo.dot.ui");dojo.require("dojo.html.common");dojo.require("dojo.html.style");dojo.require("dojo.event.common");dojo.require("dojo.event.browser");dojo.require("dojo.io.common");dojo.require("dojo.io.BrowserIO");dojo.lang.mixin(dojo.dot.ui, {appName: "Define dojo.dot.ui.appName",autoEmbed: true,autoEmbedID: "dot-widget",runLink: window.location.href,runLinkTitle: "Run Application",learnHowPath: djConfig.baseRelativePath
+ "src/dot/ui-template/learnhow.html",customLearnHowPath: false,htmlTemplatePath: djConfig.baseRelativePath + "src/dot/ui-template/widget.html",cssTemplatePath: djConfig.baseRelativePath + "src/dot/ui-template/widget.css",onlineImagePath: djConfig.baseRelativePath + "src/dot/ui-template/greenball.png",offlineImagePath: djConfig.baseRelativePath + "src/dot/ui-template/redball.png",rollerImagePath: djConfig.baseRelativePath + "src/dot/ui-template/roller.gif",checkmarkImagePath: djConfig.baseRelativePath + "src/dot/ui-template/checkmark.png",learnHowJSPath: djConfig.baseRelativePath + "src/dot/ui-template/learnhow.js",onStart: function(){this._updateSyncUI();},onRefreshUI: function(){this._setSyncMessage("Downloading UI...");},onUpload: function(){this._setSyncMessage("Uploading new data...");},onDownload: function(){this._setSyncMessage("Downloading new data...");},onFinished: function(){this._updateSyncUI();var checkmark = dojo.byId("dot-success-checkmark");var details = dojo.byId("dot-sync-details");if(dojo.sync.successful == true){this._setSyncMessage("Sync Successful");if(checkmark){checkmark.style.display = "inline";}}else if(dojo.sync.cancelled == true){this._setSyncMessage("Cancelled");if(checkmark){checkmark.style.display = "none";}}else{this._setSyncMessage("Error");var messages = dojo.byId("dot-sync-messages");if(messages){dojo.html.addClass(messages, "dot-sync-error");}
if(checkmark){checkmark.style.display = "none";}}
if(dojo.sync.details != null && details){details.style.display = "inline";}
this._updateSyncMetadata();},onCancel: function(){this._setSyncMessage("Canceling...");},onOnline: function(){this._updateNetworkIndicator();this._updateMoreCommands();var elems = new Array();elems.push(dojo.byId("dot-work-offline-button"));elems.push(dojo.byId("dot-work-online-button"));elems.push(dojo.byId("dot-configure-button"));elems.push(dojo.byId("dot-sync-button"));for(var i = 0; i < elems.length; i++){if(elems[i]){dojo.html.removeClass(elems[i], "dot-disabled");}}
if(dojo.sync.autoSync == true){window.setTimeout(dojo.lang.hitch(this, this._synchronize), 1000);}},onOffline: function(){this._updateNetworkIndicator();this._updateMoreCommands();this._setSyncMessage("You are working offline");var syncButton = dojo.byId("dot-sync-button");if(syncButton){dojo.html.addClass(syncButton, "dot-disabled");}
var details = dojo.byId("dot-sync-details");if(details){details.style.display = "none";}},onSave: function(status, isCoreSave, dataStore, item){if(status == dojo.storage.FAILED
&& isCoreSave == true){alert("Please increase the amount of local storage available "
+ "to this application");this._showConfiguration();if(dojo.storage.hasSettingsUI()){dojo.storage.showSettingsUI();}}
},onLoad: function(){},_initialize: function(){if(this._validateAppName(this.appName) == false){alert("You must set dojo.dot.ui.appName; it can only contain "
+ "letters, numbers, and spaces; right now it "
+ "is set to " + dojo.dot.ui.appName);dojo.dot.enabled = false;return;}
this.runLinkText = "Run " + this.appName;dojo.sync.onStart = dojo.lang.hitch(this, this.onStart);dojo.sync.onRefreshUI = dojo.lang.hitch(this, this.onRefreshUI);dojo.sync.onUpload = dojo.lang.hitch(this, this.onUpload);dojo.sync.onDownload = dojo.lang.hitch(this, this.onDownload);dojo.sync.onFinished = dojo.lang.hitch(this, this.onFinished);dojo.sync.onCancel = dojo.lang.hitch(this, this.onCancel);dojo.dot.onOnline = dojo.lang.hitch(this, this.onOnline);dojo.dot.onOffline = dojo.lang.hitch(this, this.onOffline);dojo.dot.files.cache([
this.htmlTemplatePath,this.cssTemplatePath,this.onlineImagePath,this.offlineImagePath,this.rollerImagePath,this.checkmarkImagePath
]);if(this.autoEmbed == true){this._doAutoEmbed();}},_doAutoEmbed: function(){var templatePath = this.htmlTemplatePath;var bindArgs = {url: templatePath,sync:false,mimetype:"text/html",error:function(type, errObj){dojo.dot.enabled = false;alert("Error loading the Dojo Offline Widget from "
+ templatePath + ": " + errObj.message);},load:dojo.lang.hitch(this, this._templateLoaded)
};dojo.io.bind(bindArgs);},_templateLoaded: function(type, data, evt){var container = dojo.byId(this.autoEmbedID);if(container){container.innerHTML = data;}
this._initImages();this._updateNetworkIndicator();this._initLearnHow();if(dojo.dot.requireOfflineCache == true
&& dojo.dot.hasOfflineCache() == false){this._needsOfflineCache();return;}
this._updateSyncUI();this._updateSyncMetadata();this._initMainEvtHandlers();this._initConfigEvtHandlers();this._setOfflineEnabled(dojo.dot.enabled);this._testNetwork();},_testNetwork: function(){var finishedCallback = dojo.lang.hitch(this, function(isOnline){this._goOnlineFinished(isOnline);if(this.onLoad){this.onLoad();}});dojo.dot.goOnline(finishedCallback);},_updateNetworkIndicator: function(){var onlineImg = dojo.byId("dot-widget-network-indicator-online");var offlineImg = dojo.byId("dot-widget-network-indicator-offline");var titleText = dojo.byId("dot-widget-title-text");if(onlineImg && offlineImg){if(dojo.dot.isOnline == true){onlineImg.style.display = "inline";offlineImg.style.display = "none";}else{onlineImg.style.display = "none";offlineImg.style.display = "inline";}}
if(titleText){if(dojo.dot.isOnline == true){titleText.innerHTML = "Online";}else{titleText.innerHTML = "Offline";}}
},_initLearnHow: function(){var learnHow = dojo.byId("dot-widget-learn-how-link");if(learnHow == null || typeof learnHow == "undefined"){return;}
if(this.customLearnHowPath == false){this.learnHowPath += "?appName=" + encodeURIComponent(this.appName)
+ "&requireOfflineCache=" + dojo.dot.requireOfflineCache
+ "&hasOfflineCache=" + dojo.dot.hasOfflineCache()
+ "&runLink=" + encodeURIComponent(this.runLink)
+ "&runLinkText=" + encodeURIComponent(this.runLinkText);dojo.dot.files.cache(this.learnHowJSPath);}
learnHow.setAttribute("href", this.learnHowPath);dojo.dot.files.cache(this.learnHowPath);var appName = dojo.byId("dot-widget-learn-how-app-name");if(appName == null || typeof appName == "undefined"){return;}
appName.innerHTML = "";appName.appendChild(document.createTextNode(this.appName));},_validateAppName: function(appName){if(appName == null || typeof appName == "undefined"){return false;}
return (/^[a-z0-9 ]*$/i.test(appName));},_updateSyncUI: function(){var syncButtons = dojo.byId("dot-sync-buttons");var syncingButtons = dojo.byId("dot-syncing-buttons");var roller = dojo.byId("dot-roller");var checkmark = dojo.byId("dot-success-checkmark");var syncMessages = dojo.byId("dot-sync-messages");var details = dojo.byId("dot-sync-details");var recommended = dojo.byId("dot-recommended");var lastSync = dojo.byId("dot-last-sync");var numItems = dojo.byId("dot-num-modified-items");if(dojo.sync.isSyncing == true){this._clearSyncMessage();if(syncButtons){syncButtons.style.display = "none";}
if(syncingButtons){syncingButtons.style.display = "block";}
if(roller){roller.style.display = "inline";}
if(checkmark){checkmark.style.display = "none";}
if(syncMessages){dojo.html.removeClass(syncMessages, "dot-sync-error");}
if(details){details.style.display = "none";}
if(lastSync){lastSync.innerHTML = "";}
if(numItems){numItems.innerHTML = "";}}else{if(syncButtons){syncButtons.style.display = "block";}
if(syncingButtons){syncingButtons.style.display = "none";}
if(roller){roller.style.display = "none";}
if(recommended){if(dojo.sync.isRecommended()){recommended.style.display = "inline";}else{recommended.style.display = "none";}}
}
this._updateMoreCommands();},_synchronize: function(evt){if(evt && evt.preventDefault){evt.preventDefault();evt.stopPropagation();}
if(evt){var syncButton = dojo.byId("dot-sync-button");if(syncButton && syncButton.blur){syncButton.blur();}}
dojo.sync.synchronize();},_setSyncMessage: function(message){var syncMessage = dojo.byId("dot-sync-messages");if(syncMessage){syncMessage.innerHTML = message;}},_clearSyncMessage: function(){this._setSyncMessage("");},_initImages: function(){var onlineImg = dojo.byId("dot-widget-network-indicator-online");if(onlineImg){onlineImg.setAttribute("src", this.onlineImagePath);}
var offlineImg = dojo.byId("dot-widget-network-indicator-offline");if(offlineImg){offlineImg.setAttribute("src", this.offlineImagePath);}
var roller = dojo.byId("dot-roller");if(roller){roller.setAttribute("src", this.rollerImagePath);}
var checkmark = dojo.byId("dot-success-checkmark");if(checkmark){checkmark.setAttribute("src", this.checkmarkImagePath);}},_showDetails: function(evt){evt.preventDefault();evt.stopPropagation();if(dojo.sync.details == null){return;}
var html = "";html += "<html><head><title>Sync Details</title><head><body>";html += "<h1>Sync Details</h1>\n";html += "<ul>\n";for(var i = 0; i < dojo.sync.details.length; i++){html += "<li>";html += dojo.sync.details[i];html += "</li>";}
html += "</ul>\n";html += "<a href='javascript:window.close()' "
+ "style='text-align: right; padding-right: 2em;'>"
+ "Close Window"
+ "</a>\n";html += "</body></html>";var windowParams = "height=400,width=600,resizable=true,"
+ "scrollbars=true,toolbar=no,menubar=no,"
+ "location=no,directories=no,dependent=yes";var popup = window.open("", "SyncDetails", windowParams);if(popup == null || typeof popup == "undefined"){alert("Please allow popup windows for this domain; can't display sync details window");return;}
popup.document.open();popup.document.write(html);popup.document.close();if(popup.focus){popup.focus();}},_cancel: function(evt){evt.preventDefault();evt.stopPropagation();dojo.sync.cancel();},_updateSyncMetadata: function(){var lastSyncField = dojo.byId("dot-last-sync");var numItemsField = dojo.byId("dot-num-modified-items");if(lastSyncField){if(dojo.sync.lastSync != null){lastSyncField.style.display = "block";var dateStr = this._getDateString(dojo.sync.lastSync);lastSyncField.innerHTML = "Updated " + dateStr;}else{lastSyncField.style.display = "none";}}
if(numItemsField){var numItems = dojo.sync.getNumModifiedItems();if(numItems > 0){numItemsField.style.display = "block";numItemsField.innerHTML = numItems
+ " modified offline items";}else{numItemsField.style.display = "none";}}
},_getDateString: function(date){var now = new Date();var str;if(now.getFullYear() == date.getFullYear()
&& now.getMonth() == date.getMonth()
&& now.getDay() == date.getDay()){str = "Today at " + this._getTimeString(date);}else{str = date.toLocaleString();}
return str;},_getTimeString: function(date){var hour = date.getHours();var amPM;if(hour < 12){amPM = "AM";}else if(hour >= 12 && hour < 24){amPM = "PM";hour = hour - 12;}else if(hour == 24){amPM = "AM";hour = hour - 12;}
var minutes = date.getMinutes();if(minutes < 10){minutes = "0" + minutes;}
return hour + ":" + minutes + " " + amPM;},_updateMoreCommands: function(){var offlineButton = dojo.byId("dot-work-offline-button");var onlineButton = dojo.byId("dot-work-online-button");var configureButton = dojo.byId("dot-configure-button");if(dojo.dot.isOnline == true){if(offlineButton){offlineButton.style.display = "inline";}
if(onlineButton){onlineButton.style.display = "none";}}else{if(offlineButton){offlineButton.style.display = "none";}
if(onlineButton){onlineButton.style.display = "inline";}}
if(dojo.sync.isSyncing == true){if(offlineButton){dojo.html.addClass(offlineButton, "dot-disabled");}
if(onlineButton){dojo.html.addClass(onlineButton, "dot-disabled");}
if(configureButton){dojo.html.addClass(configureButton, "dot-disabled");}}else{if(offlineButton){dojo.html.removeClass(offlineButton, "dot-disabled");}
if(onlineButton){dojo.html.removeClass(onlineButton, "dot-disabled");}
if(configureButton){dojo.html.removeClass(configureButton, "dot-disabled");}}
},_workOnline: function(evt){evt.preventDefault();evt.stopPropagation();if(dojo.sync.isSyncing == true){return;}
var checkmark = dojo.byId("dot-success-checkmark");var roller = dojo.byId("dot-roller");var details = dojo.byId("dot-sync-details");this._setSyncMessage("Checking network... ");if(checkmark){checkmark.style.display = "none";}
if(roller){roller.style.display = "inline";}
if(details){details.style.display = "none";}
var elems = new Array();elems.push(dojo.byId("dot-work-offline-button"));elems.push(dojo.byId("dot-work-online-button"));elems.push(dojo.byId("dot-configure-button"));elems.push(dojo.byId("dot-sync-button"));for(var i = 0; i < elems.length; i++){if(elems[i]){dojo.html.addClass(elems[i], "dot-disabled");}}
dojo.dot.goOnline(dojo.lang.hitch(this, this._goOnlineFinished));},_goOnlineFinished: function(isOnline){var roller = dojo.byId("dot-roller");if(roller){roller.style.display = "none";}
if(isOnline){this._clearSyncMessage();this.onOnline();}else{this._setSyncMessage("Network not available");this.onOffline();}},_workOffline: function(evt){evt.preventDefault();evt.stopPropagation();if(dojo.sync.isSyncing == true
|| dojo.dot.goingOnline == true){return;}
dojo.dot.goOffline();},_needsOfflineCache: function(){var learnHow = dojo.byId("dot-widget-learn-how");if(learnHow){dojo.html.addClass(learnHow, "dot-needs-offline-cache");}
var elems = new Array();elems.push(dojo.byId("dot-sync-controls"));elems.push(dojo.byId("dot-sync-status"));elems.push(dojo.byId("dot-more-commands"));for(var i = 0; i < elems.length; i++){elems[i].style.display = "none";}},_initMainEvtHandlers: function(){var syncButton = dojo.byId("dot-sync-button");if(syncButton){dojo.event.connect(syncButton, "onclick", this, this._synchronize);}
var detailsButton = dojo.byId("dot-sync-details-button");if(detailsButton){dojo.event.connect(detailsButton, "onclick", this, this._showDetails);}
var cancelButton = dojo.byId("dot-sync-cancel-button");if(cancelButton){dojo.event.connect(cancelButton, "onclick", this, this._cancel);}
var onlineButton = dojo.byId("dot-work-online-button");if(onlineButton){dojo.event.connect(onlineButton, "onclick", this, this._workOnline);}
var offlineButton = dojo.byId("dot-work-offline-button");if(offlineButton){dojo.event.connect(offlineButton, "onclick", this, this._workOffline);}
var configureButton = dojo.byId("dot-configure-button");if(configureButton){dojo.event.connect(configureButton, "onclick", this, this._showConfiguration);}},_initConfigEvtHandlers: function(){var enableOfflineField = dojo.byId("dot-enableOffline");var autoSyncField = dojo.byId("dot-autoSync");var clearButton = dojo.byId("dot-clear-button");var storageButton = dojo.byId("dot-storage-settings-button");var okButton = dojo.byId("dot-configure-ok-button");if(enableOfflineField){dojo.event.connect(enableOfflineField, "onchange", function(evt){evt.preventDefault();evt.stopPropagation();dojo.dot.enabled = !dojo.dot.enabled;dojo.dot.save();});}
if(autoSyncField){dojo.event.connect(autoSyncField, "onchange", function(evt){evt.preventDefault();evt.stopPropagation();dojo.sync.autoSync = !dojo.sync.autoSync;dojo.sync.save();});}
if(clearButton){dojo.event.connect(clearButton, "onclick", function(evt){evt.preventDefault();evt.stopPropagation();if(confirm("Are you sure?")){dojo.dot.clear();}});}
if(storageButton){dojo.event.connect(storageButton, "onclick", function(evt){evt.preventDefault();evt.stopPropagation();dojo.storage.showSettingsUI();});}
if(okButton){dojo.event.connect(okButton, "onclick", this, this._hideConfiguration);}},_showConfiguration: function(evt){if(evt){evt.preventDefault();evt.stopPropagation();}
if(dojo.sync.isSyncing == true
|| dojo.dot.goingOnline == true){return;}
var elems = new Array();elems.push(dojo.byId("dot-widget-learn-how"));elems.push(dojo.byId("dot-sync-controls"));elems.push(dojo.byId("dot-sync-status"));elems.push(dojo.byId("dot-more-commands"));elems.push(dojo.byId("dot-last-sync"));elems.push(dojo.byId("dot-num-modified-items"));for(var i = 0; i < elems.length; i++){if(elems[i]){elems[i].style.display = "none";}}
var configArea = dojo.byId("dot-configure");if(configArea){configArea.style.display = "block";}
var enableOfflineField = dojo.byId("dot-enableOffline");var autoSyncField = dojo.byId("dot-autoSync");if(enableOfflineField){enableOfflineField.checked = dojo.dot.enabled;}
if(autoSyncField){autoSyncField.checked = dojo.sync.autoSync;}
var storageButton = dojo.byId("dot-storage-settings-button");if(storageButton && dojo.storage.hasSettingsUI() == false){dojo.html.addClass(storageButton, "dot-disabled");storageButton.enabled = false;}},_hideConfiguration: function(){var elems = new Array();elems.push(dojo.byId("dot-widget-learn-how"));elems.push(dojo.byId("dot-sync-controls"));elems.push(dojo.byId("dot-sync-status"));elems.push(dojo.byId("dot-more-commands"));elems.push(dojo.byId("dot-last-sync"));elems.push(dojo.byId("dot-num-modified-items"));for(var i = 0; i < elems.length; i++){if(elems[i]){elems[i].style.display = "block";}}
var configUI = dojo.byId("dot-configure");if(configUI){configUI.style.display = "none";}
this._setOfflineEnabled(dojo.dot.enabled);},_setOfflineEnabled: function(enabled){var elems = new Array();elems.push(dojo.byId("dot-sync-controls"));elems.push(dojo.byId("dot-sync-status"));elems.push(dojo.byId("dot-last-sync"));elems.push(dojo.byId("dot-num-modified-items"));elems.push(dojo.byId("dot-work-online-button"));elems.push(dojo.byId("dot-work-offline-button"));for(var i = 0; i < elems.length; i++){if(elems[i]){if(enabled){elems[i].style.visibility = "visible";}else{elems[i].style.visibility = "hidden";}}
}}
});dojo.dot.onSave = dojo.lang.hitch(dojo.dot.ui, dojo.dot.ui.onSave);dojo.dot.addOnLoad(dojo.lang.hitch(dojo.dot.ui, dojo.dot.ui._initialize));