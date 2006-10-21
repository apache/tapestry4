

dojo.provide("dojo.widget.Select");

dojo.require("dojo.widget.ComboBox");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.html.stabile");



dojo.widget.defineWidget(
"dojo.widget.Select",
dojo.widget.ComboBox,
{
forceValidOption: true,

setValue: function(value) {
this.comboBoxValue.value = value;
dojo.widget.html.stabile.setState(this.widgetId, this.getState(), true);
this.onValueChanged(value);
},

setLabel: function(value){
// FIXME, not sure what to do here!
this.comboBoxSelectionValue.value = value;
if (this.textInputNode.value != value) { // prevent mucking up of selection
this.textInputNode.value = value;
}
},

getLabel: function(){
return this.comboBoxSelectionValue.value;
},

getState: function() {
return {
value: this.getValue(),
label: this.getLabel()
};
},

onKeyUp: function(evt){
this.setLabel(this.textInputNode.value);
},

setState: function(state) {
this.setValue(state.value);
this.setLabel(state.label);
},

setAllValues: function(value1, value2){
this.setLabel(value1);
this.setValue(value2);
}
}
);
