dojo.provide("tapestry.widget.TimePicker");

dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.html.style");
dojo.require("dojo.html.util");
dojo.require("dojo.html.metrics");
dojo.require("dojo.html.iframe");

tapestry.widget.currentTimePicker=null;

dojo.widget.defineWidget(
        "tapestry.widget.TimePicker",
        dojo.widget.HtmlWidget,
{
    inputNodeId:null, // unique element id of form input text field
    optionValues:[], // param of 12 hour clock selection values
    selectedIndex:null, // param of what the default selected index should be
    dropdownClass:"dropdownCombobox",
    dropdownOptionClass:"dropdownOption",
    optionHoverClass:"optionHover",

    inputNode:null, // form input text node
    selectedNode:null, // currently selected node
    hoveredNode:null, // current node being hovered over with mouse - ie has background color changed
    dropdownNode:null, // drop down div container
    bgIframe:null,
    options:[], // option div nodes
    showing:false,
    preventBlur:false,
    hasFocus:false,
    dropdownDim:{height:0, width:0},

    postCreate: function() {
        this.inputNode = dojo.byId(this.inputNodeId);

        this.dropdownNode = document.createElement("div");
        this.dropdownNode.setAttribute("id", this.widgetId + "dropdown");
        this.dropdownNode.style["display"] = "none";
        dojo.html.setClass(this.dropdownNode, this.dropdownClass);

        var contDiv = document.createElement("div");
        this.dropdownNode.appendChild(contDiv);
        
        for (var i=0; i < this.optionValues.length; i++){
            var option = document.createElement("div");
            option.setAttribute("id", "combooption-" + i);
            dojo.html.setClass(option, this.dropdownOptionClass);
            option.appendChild(document.createTextNode(this.optionValues[i]));
            
            contDiv.appendChild(option);
            this.options.push(option);

            if (this.selectedIndex && i == this.selectedIndex){
                this.selectedNode = option;

                if (!this.inputNode.value || this.inputNode.value.length < 1){
                    this.inputNode.value=this.optionValues[i];
                }
            }

            dojo.event.connect(option, "onmouseover", this, "onOptionMouseOver");
            dojo.event.connect(option, "onmouseout", this, "onOptionMouseOut");
            dojo.event.connect(option, "onmousedown", this, "onOptionClicked");
        }

        var m = dojo.html.getCachedFontMeasurements();
        this.dropdownDim.width = m["1em"] * 6;
        this.dropdownDim.height = m["1em"] * 11;

        var st=this.dropdownNode.style;
        st["overflow"]="auto";
        st["zIndex"]=9000;
        st["position"]="absolute";
        st["width"]=this.dropdownDim.width + "px"
        st["height"]=this.dropdownDim.height + "px";

        dojo.body().appendChild(this.dropdownNode);

        if(dojo.render.html.ie55||dojo.render.html.ie60){
            this.bgIframe = new dojo.html.BackgroundIframe();
            this.bgIframe.setZIndex(this.dropdownNode);
        }
        
        dojo.event.connect(this.inputNode, "onclick", this, "onInputClick");
        dojo.event.connect(this.inputNode, "onblur", this, "onInputBlur");
        dojo.event.connect(this.inputNode, "onkeyup", this, "onInputKeyUp");
        dojo.event.connect(this.inputNode, "onkeydown", this, "onInputKeyDown");
        
        dojo.event.connect(this.dropdownNode, "onmouseover", this, "onDropdownMouseOver");
        dojo.event.connect(this.dropdownNode, "onmouseout", this, "onDropdownMouseOut");
        
        dojo.event.connect(dojo.body(), "onkeyup", this, "onKeyUp");
    },

    onOptionMouseOver: function(evt) {
        this._selectOption(evt.target);
    },

    onOptionMouseOut: function(evt) {
        this._clearOptionSelection(evt.target);
    },

    onChange:function() {},

    onOptionClicked: function(evt) {
        this.selectedNode=evt.target;
        
        this.inputNode.value=tapestry.html.getContentAsString(this.selectedNode);
        this.hide(evt);
        dojo.html.removeClass(this.selectedNode, this.optionHoverClass);

        this.onChange(evt);
    },

    onInputClick: function() {
        if (this.showing){
            this.hide();
            return;
        }

        this.show();

        if (this.selectedNode){
            this.selectedNode.scrollIntoView(true);
        }
    },

    onInputBlur: function(evt) {
        this.hasFocus=false;
        if (this.preventBlur){
            return;
        }

        this.hide();
    },

    onDropdownMouseOver: function(evt) {
        this.preventBlur=true;
    },

    onDropdownMouseOut: function(evt) {
        if (!this.showing){return;}
        this.preventBlur=false;

        if (this.isWidgetNode(evt["relatedTarget"])){
            return;
        }
        if (!this.hasFocus){
            this.hide(evt);
        }
    },

    onKeyUp: function(evt) {
        if (evt.keyCode == evt.KEY_ESCAPE) {
            this.hide(evt);
        }
    },

    onInputKeyUp: function(evt) {
        switch(evt.keyCode){
            case evt.KEY_TAB:
                this.show();
                break;
           /* case evt.KEY_UP_ARROW:
                this.inputNode.focus();
                this._selectPreviousOption();
                break;
            case evt.KEY_DOWN_ARROW:
                this.inputNode.focus();
                this._selectNextOption();
                break;
                */
        }
    },

    onInputKeyDown: function(evt) {
        switch(evt.keyCode){
            case evt.KEY_TAB:
                if (this.showing){this.hide();}
        }
    },

    hide: function() {
        dojo.html.hide(this.dropdownNode);

        if (this.bgIframe){
            this.bgIframe.hide();
        }

        this.hasFocus=false;
        this.preventBlur=false;
        this.showing=false;
        this.hoveredNode=null;
    },

    show: function() {

        if (tapestry.widget.currentTimePicker &&
                tapestry.widget.currentTimePicker != this){
            tapestry.widget.currentTimePicker.hide();
        }

        var oldDisplay = this.inputNode.style.display;
        var mb = dojo.html.getElementBox(this.inputNode, dojo.html.boxSizing.BORDER_BOX);
        var inputPos = dojo.html.getAbsolutePosition(this.inputNode, true, dojo.html.boxSizing.BORDER_BOX);
	    this.inputNode.style.display=oldDisplay;

        var view=dojo.html.getViewport();
        var scroll=dojo.html.getScroll();

        var ddX = inputPos.x + mb.width - this.dropdownDim.width;
        if (ddX < 0){
            ddX = inputPos.x;
        }

        var ddY;
        if ((inputPos.y + mb.height + this.dropdownDim.height) > view.height){
            ddY = inputPos.y - this.dropdownDim.height - 1;
        } else {
            ddY = inputPos.y + mb.height;
        }

        if (dojo.render.html.ie && scroll.top > 0){
            ddY -= scroll.top;
        }

        this.dropdownNode.style["top"]=ddY+'px';
        this.dropdownNode.style["left"]=ddX+'px';
        
        dojo.html.show(this.dropdownNode);
        
        if (this.bgIframe){
            this.bgIframe.size(this.dropdownNode);
            this.bgIframe.show();
        }

        this.showing=true;
        this.hasFocus=true;
        this.preventBlur=true;
        tapestry.widget.currentTimePicker=this;
    },

    getValue:function(){
        return this.inputNode.value;
    },

    isWidgetNode: function(node){
        if (!node){return false;}
        
        return dojo.html.hasClass(node, this.dropdownOptionClass)
            || dojo.html.hasClass(node, this.dropdownClass);
    },

    destroyRendering: function(finalize){
        try{
            dojo.widget.HtmlWidget.prototype.destroyRendering.call(this, finalize);

            dojo.event.disconnect(this.inputNode, "onclick", this, "onInputClick");
            dojo.event.disconnect(this.inputNode, "onblur", this, "onInputBlur");
            dojo.event.disconnect(this.inputNode, "onkeyup", this, "onInputKeyUp");
            dojo.event.disconnect(this.inputNode, "onkeydown", this, "onInputKeyDown");
            dojo.event.browser.clean(this.inputNode);

            dojo.event.disconnect(this.dropdownNode, "onmouseover", this, "onDropdownMouseOver");
            dojo.event.disconnect(this.dropdownNode, "onmouseout", this, "onDropdownMouseOut");

            dojo.dom.destroyNode(this.dropdownNode);
            delete this.dropdownNode;

            dojo.event.disconnect(dojo.body(), "onkeyup", this, "onKeyUp");

             if (this.bgIframe){
                this.bgIframe.remove();
            }
        } catch (e) { }
    },

    _selectOption:function(node){
        if (!node) { return; }
        
        this.preventBlur=true;
        if (!dojo.html.hasClass(node, this.optionHoverClass)) {
            dojo.html.addClass(node, this.optionHoverClass);
        }

        this.hoveredNode=node;
    },

    _clearOptionSelection:function(node){
        dojo.html.removeClass(node, this.optionHoverClass);
    },

    _selectPreviousOption:function(){
        var prevNode;
        if (!this.hoveredNode){
            this.hoveredNode=this.options[0];
            prevNode = this.hoveredNode;
        } else {
            prevNode = this.hoveredNode.previousSibling;
        }

        prevNode.scrollIntoView(true);
        this._clearOptionSelection(this.hoveredNode);
        this._selectOption(prevNode);
    },

    _selectNextOption:function() {
        var nextNode;
        if (!this.hoveredNode){
            this.hoveredNode=this.options[0];
            nextNode = this.hoveredNode;
        } else {
            nextNode = this.hoveredNode.nextSibling;
        }

        nextNode.scrollIntoView(true);
        this._clearOptionSelection(this.hoveredNode);
        this._selectOption(nextNode);
    }
}
);
