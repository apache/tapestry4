
dojo.provide("dojo.widget.Toaster");dojo.require("dojo.widget.*");dojo.require("dojo.lfx.*");dojo.require("dojo.html.iframe");dojo.widget.defineWidget(
"dojo.widget.Toaster",dojo.widget.HtmlWidget,{templateString: '<div dojoAttachPoint="clipNode"><div dojoAttachPoint="containerNode" dojoAttachEvent="onClick:onSelect"><div dojoAttachPoint="contentNode"></div></div></div>',templateCssPath: dojo.uri.dojoUri("src/widget/templates/Toaster.css"),messageTopic: "",messageTypes: {MESSAGE: "MESSAGE",WARNING: "WARNING",ERROR: "ERROR",FATAL: "FATAL"},defaultType: "MESSAGE",clipCssClass: "dojoToasterClip",containerCssClass: "dojoToasterContainer",contentCssClass: "dojoToasterContent",messageCssClass: "dojoToasterMessage",warningCssClass: "dojoToasterWarning",errorCssClass: "dojoToasterError",fatalCssClass: "dojoToasterFatal",positionDirection: "br-up",positionDirectionTypes: ["br-up", "br-left", "bl-up", "bl-right", "tr-down", "tr-left", "tl-down", "tl-right"],initializer: function(){this.duration = 2000;this.showDelay = '';this.separator = "<hr>";},postCreate: function(){if(this.showDelay!=''){dojo.deprecated("dojo.widget.Toaster", "use 'duration' instead of 'showDelay'", "0.6");this.duration = this.showDelay;}
this.hide();dojo.html.setClass(this.clipNode, this.clipCssClass);dojo.html.addClass(this.containerNode, this.containerCssClass);dojo.html.setClass(this.contentNode, this.contentCssClass);if(this.messageTopic){dojo.event.topic.subscribe(this.messageTopic, this, "_handleMessage");}
if(!this.positionDirection || !dojo.lang.inArray(this.positionDirectionTypes, this.positionDirection)){this.positionDirection = this.positionDirectionTypes.BRU;}},_handleMessage: function(msg){if(dojo.lang.isString(msg)){this.setContent(msg);}else{this.setContent(msg["message"], msg["type"], msg["duration"]);}},setContent: function(msg, messageType, duration){var duration = duration||this.duration;if(this.slideAnim && this.slideAnim.status() == "playing"){dojo.lang.setTimeout(50, dojo.lang.hitch(this, function(){this.setContent(msg, messageType);}));return;}else if(this.slideAnim){this.slideAnim.stop();if(this.fadeAnim && this.fadeAnim.status() == "playing"){dojo.lang.setTimeout(50, dojo.lang.hitch(this, function(){this.setContent(msg, messageType);}));return;}}
if(!msg){dojo.debug(this.widgetId + ".setContent() incoming content was null, ignoring.");return;}
if(!this.positionDirection || !dojo.lang.inArray(this.positionDirectionTypes, this.positionDirection)){dojo.raise(this.widgetId + ".positionDirection is an invalid value: " + this.positionDirection);}
dojo.html.removeClass(this.containerNode, this.messageCssClass);dojo.html.removeClass(this.containerNode, this.warningCssClass);dojo.html.removeClass(this.containerNode, this.errorCssClass);dojo.html.removeClass(this.containerNode, this.fatalCssClass);dojo.html.clearOpacity(this.containerNode);if(msg instanceof String || typeof msg == "string"){var tmpMsg = msg;}else if(dojo.html.isNode(msg)){var tmpMsg = dojo.html.getContentAsString(msg);}else{dojo.raise("Toaster.setContent(): msg is of unknown type:" + msg);}
var curMsg = this.contentNode.innerHTML
if(tmpMsg&&this.isVisible){this.contentNode.innerHTML = curMsg + "<br>" + this.separator + "<br>" + tmpMsg;}else{this.contentNode.innerHTML = tmpMsg}
switch(messageType){case this.messageTypes.WARNING:
dojo.html.addClass(this.containerNode, this.warningCssClass);break;case this.messageTypes.ERROR:
dojo.html.addClass(this.containerNode, this.errorCssClass);break
case this.messageTypes.FATAL:
dojo.html.addClass(this.containerNode, this.fatalCssClass);break;case this.messageTypes.MESSAGE:
default:
dojo.html.addClass(this.containerNode, this.messageCssClass);break;}
this.show();var nodeSize = dojo.html.getMarginBox(this.containerNode);if(this.isVisible){this._placeClip();}else{if(this.positionDirection.indexOf("-up") >= 0){this.containerNode.style.left=0+"px";this.containerNode.style.top=nodeSize.height + 10 + "px";}else if(this.positionDirection.indexOf("-left") >= 0){this.containerNode.style.left=nodeSize.width + 10 +"px";this.containerNode.style.top=0+"px";}else if(this.positionDirection.indexOf("-right") >= 0){this.containerNode.style.left = 0 - nodeSize.width - 10 + "px";this.containerNode.style.top = 0+"px";}else if(this.positionDirection.indexOf("-down") >= 0){this.containerNode.style.left = 0+"px";this.containerNode.style.top = 0 - nodeSize.height - 10 + "px";}else{dojo.raise(this.widgetId + ".positionDirection is an invalid value: " + this.positionDirection);}
this.slideAnim = dojo.lfx.html.slideTo(
this.containerNode,{ top: 0, left: 0 },450,null,dojo.lang.hitch(this, function(nodes, anim){this.fadeAnim = dojo.lfx.html.fadeOut(
this.containerNode,1000,null,dojo.lang.hitch(this, function(evt){this.isVisible = false;this.hide();}));if(duration>0){dojo.lang.setTimeout(dojo.lang.hitch(this, function(evt){if(this.bgIframe){this.bgIframe.hide();}
this.fadeAnim.play();}), duration);}else{dojo.event.connect(
this,'onSelect',dojo.lang.hitch(this, function(evt){this.fadeAnim.play();}));}
this.isVisible = true;})).play();}},_placeClip: function(){var scroll = dojo.html.getScroll();var view = dojo.html.getViewport();var nodeSize = dojo.html.getMarginBox(this.containerNode);this.clipNode.style.height = nodeSize.height+"px";this.clipNode.style.width = nodeSize.width+"px";if(this.positionDirection.match(/^t/)){this.clipNode.style.top = scroll.top+"px";}else if(this.positionDirection.match(/^b/)){this.clipNode.style.top = (view.height - nodeSize.height - 2 + scroll.top)+"px";}
if(this.positionDirection.match(/^[tb]r-/)){this.clipNode.style.left = (view.width - nodeSize.width - 1 - scroll.left)+"px";}else if(this.positionDirection.match(/^[tb]l-/)){this.clipNode.style.left = 0 + "px";}
this.clipNode.style.clip = "rect(0px, " + nodeSize.width + "px, " + nodeSize.height + "px, 0px)";if(dojo.render.html.ie){if(!this.bgIframe){this.bgIframe = new dojo.html.BackgroundIframe(this.clipNode);this.bgIframe.setZIndex(this.clipNode);}
this.bgIframe.onResized();this.bgIframe.show();}},onSelect: function(e) {},show: function(){dojo.widget.Toaster.superclass.show.call(this);this._placeClip();if(!this._scrollConnected){this._scrollConnected = true;dojo.event.connect(window, "onscroll", this, "_placeClip");}},hide: function(){dojo.widget.Toaster.superclass.hide.call(this);if(this._scrollConnected){this._scrollConnected = false;dojo.event.disconnect(window, "onscroll", this, "_placeClip");}
dojo.html.setOpacity(this.containerNode, 1.0);}}
);