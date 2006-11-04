
dojo.require("dojo.lang.common");dojo.require("dojo.lang.declare");dojo.provide("dojo.dnd.DragAndDrop");dojo.declare("dojo.dnd.DragSource", null, {type: "",onDragEnd: function(){},onDragStart: function(){},onSelected: function(){},unregister: function(){dojo.dnd.dragManager.unregisterDragSource(this);},reregister: function(){dojo.dnd.dragManager.registerDragSource(this);}}, function(){var dm = dojo.dnd.dragManager;if(dm["registerDragSource"]){dm.registerDragSource(this);}});dojo.declare("dojo.dnd.DragObject", null, {type: "",onDragStart: function(){},onDragMove: function(){},onDragOver: function(){},onDragOut: function(){},onDragEnd: function(){},onDragLeave: this.onDragOut,onDragEnter: this.onDragOver,ondragout: this.onDragOut,ondragover: this.onDragOver}, function(){var dm = dojo.dnd.dragManager;if(dm["registerDragObject"]){dm.registerDragObject(this);}});dojo.declare("dojo.dnd.DropTarget", null, {acceptsType: function(type){if(!dojo.lang.inArray(this.acceptedTypes, "*")){if(!dojo.lang.inArray(this.acceptedTypes, type)) { return false; }}
return true;},accepts: function(dragObjects){if(!dojo.lang.inArray(this.acceptedTypes, "*")){for (var i = 0; i < dragObjects.length; i++) {if (!dojo.lang.inArray(this.acceptedTypes,dragObjects[i].type)) { return false; }}}
return true;},unregister: function(){dojo.dnd.dragManager.unregisterDropTarget(this);},onDragOver: function(){},onDragOut: function(){},onDragMove: function(){},onDropStart: function(){},onDrop: function(){},onDropEnd: function(){}}, function(){if (this.constructor == dojo.dnd.DropTarget) { return; }
this.acceptedTypes = [];dojo.dnd.dragManager.registerDropTarget(this);});dojo.dnd.DragEvent = function(){this.dragSource = null;this.dragObject = null;this.target = null;this.eventStatus = "success";}
dojo.declare("dojo.dnd.DragManager", null, {selectedSources: [],dragObjects: [],dragSources: [],registerDragSource: function(){},dropTargets: [],registerDropTarget: function(){},lastDragTarget: null,currentDragTarget: null,onKeyDown: function(){},onMouseOut: function(){},onMouseMove: function(){},onMouseUp: function(){}});