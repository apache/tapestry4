/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.FormContainer");

dojo.require("dojo.widget.*");
dojo.require("dojo.widget.HtmlWidget");

dojo.widget.defineWidget("dojo.widget.FormContainer", dojo.widget.HtmlWidget,
	{
		isContainer: true,

		_getFormElement: function(name) {
			for(var i=0, len=this.domNode.elements.length; i<len; i++) {
				var element = this.domNode.elements[i];
				if (element.name == name) {
					return element;
				} // if
			} // for
			return null;
		},

		_getObject: function(obj, searchString) {
			var namePath = [];
			namePath=searchString.split(".");
			var myObj=obj;
			var name=namePath[namePath.length-1];

			for(var j=0, len=namePath.length;j<len;++j) {
				var p=namePath[j];
				if (typeof(myObj[p]) == "undefined") {
					myObj[p]={};
				}
				myObj=myObj[p];
			}
			return myObj;
		},
		_setToContainers: function (obj, widget) {
			for(var i=0, len=widget.children.length; i<len; ++i) {
				if (widget.children[i].widgetType == "RepeaterContainer") {
					var rIndex=widget.children[i].index;
					var rIndexPos=rIndex.indexOf("%{index}");
					rIndex=rIndex.substr(0,rIndexPos-1);
					var myObj = this._getObject(obj, rIndex);
					if (typeof(myObj) == "object" && myObj.length == 0) {
						myObj=[];
					}
					var rowCount = widget.children[i].getRowCount();
					if (myObj.length > rowCount) {
						for (var j=rowCount, len2=myObj.length; j<len2; j++) {
							widget.children[i].addRow();
						}
					} else if (myObj.length < rowCount) {
						for (var j=rowCount, len2=myObj.length; j>len2; j--) {
							widget.children[i].deleteRow(0);
						}
					}
					for (var j=0, len2=myObj.length;j<len2; ++j) {
						for (var key in myObj[j]) {
							var prefix = dojo.string.substituteParams(widget.children[i].index, {"index": "" + j});
							this._getFormElement(prefix+"."+key).value=myObj[j][key];
						}
					}
				}

				if (widget.children[i].isContainer) {
					this._setToContainers (obj, widget.children[i]);
					continue;
				}

				switch(widget.children[i].widgetType) {
					case "Checkbox":
						continue;
						break;
					case "DropdownDatePicker":
						if(widget.children[i].valueNode.value == "") {
							widget.children[i].inputNode.value="";
							widget.children[i].datePicker.storedDate="";
						} else {
							widget.children[i].datePicker.setDate(widget.children[i].valueNode.value);
							//widget.children[i].datePicker.date=dojo.widget.DatePicker.util.fromRfcDate(widget.children[i].valueNode.value);
							widget.children[i].onSetDate();
						}
						break;
					case "Select":
						//widget.children[i].setValue(myObj[name]);
						continue;
						break;
					case "ComboBox":
						//widget.children[i].setSelectedValue(myObj[name]);
						continue;
						break;
					default:
						break;
				}
			}
		},
		setValues: function(obj) {
			for(var i=0, len=this.domNode.elements.length; i<len; i++) {
				var element = this.domNode.elements[i];
				if (element.name == '') {continue};
				var namePath = new Array();
				namePath=element.name.split(".");
				var myObj=obj;
				var name=namePath[namePath.length-1];
				for(var j=1,len2=namePath.length;j<len2;++j) {
					var p=namePath[j - 1];
					if(typeof(myObj[p]) == "undefined") {
						myObj=undefined;
						break;
						//myObj[p]={}
					};
					myObj=myObj[p];
				}

				if (typeof(myObj) == "undefined") {
					continue;
				}

				var type=element.type;
				if (type == "hidden" || type == "text" || type == "textarea" || type == "password") {
					type = "text";
				}
				switch(type) {
					case "checkbox":
						this.domNode.elements[i].checked=false;
						if (typeof(myObj[name]) == 'undefined') continue;
						for (var j=0,len2=myObj[name].length; j<len2; ++j) {
							if(element.value == myObj[name][j]) {
								element.checked=true;
							}
						}
						break;
					case "radio":
						this.domNode.elements[i].checked=false;
						if (typeof(myObj[name]) == 'undefined') {continue};
						if (myObj[name] == this.domNode.elements[i].value) {
							this.domNode.elements[i].checked=true;
						}
						break;
					case "select-one":
						this.domNode.elements[i].selectedIndex="0";
						for (var j=0,len2=element.options.length; j<len2; ++j) {
							if (element.options[j].value == myObj[name]) {
								element.options[j].selected=true;
							} else {
								//element.options[j].selected=false;
							}
						}
						break;
					case "text":
						var value="";
						if (typeof(myObj[name]) == 'string') {
							value = myObj[name];
						}
						this.domNode.elements[i].value=value;
						break;
					default:
						//dojo.debug("Not supported type ("+type+")");
						break;
				}
      }
			this._setToContainers(obj,this);
		},
		getValues: function() {
			var obj = { };

			for(var i=0,len=this.domNode.elements.length; i<len; i++) {
				// FIXME: would be better to give it an attachPoint:
				var elm = this.domNode.elements[i];
				var namePath = [];
				if (elm.name == '') { continue;}
				namePath=elm.name.split(".");
				var myObj=obj;
				var name=namePath[namePath.length-1];
				for(var j=1,len2=namePath.length;j<len2;++j) {
					var nameIndex = null;
					var p=namePath[j - 1];
					var nameA=p.split("[");
					if (nameA.length > 1) {
						if(typeof(myObj[nameA[0]]) == "undefined") {
							myObj[nameA[0]]=[ ];
						} // if
						nameIndex=parseInt(nameA[1]);
						if(typeof(myObj[nameA[0]][nameIndex]) == "undefined") {
							myObj[nameA[0]][nameIndex]={};
						}
					} else if(typeof(myObj[nameA[0]]) == "undefined") {
						myObj[nameA[0]]={}
					} // if

					if (nameA.length == 1) {
						myObj=myObj[nameA[0]];
					} else {
						myObj=myObj[nameA[0]][nameIndex];
					} // if
				} // for

				if ((elm.type != "checkbox" && elm.type != "radio") || (elm.type=="radio" && elm.checked)) {
					if(name == name.split("[")[0]) {
						myObj[name]=elm.value;
					} else {
						// can not set value when there is no name
					}
				} else if (elm.type == "checkbox" && elm.checked) {
					if(typeof(myObj[name]) == 'undefined') {
						myObj[name]=[ ];
					}
					myObj[name].push(elm.value);
				} // if
				name=undefined;
			} // for
		return obj;
	}
});
