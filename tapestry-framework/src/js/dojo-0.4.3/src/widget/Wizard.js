dojo.provide("dojo.widget.Wizard");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.LayoutContainer");
dojo.require("dojo.widget.ContentPane");
dojo.require("dojo.event.*");
dojo.require("dojo.html.style");
dojo.widget.defineWidget("dojo.widget.WizardContainer",dojo.widget.LayoutContainer,{templateString:"<div class=\"WizardContainer\" dojoAttachPoint=\"wizardNode\">\n    <div class=\"WizardText\" dojoAttachPoint=\"wizardPanelContainerNode\">\n    </div>\n    <div class=\"WizardButtonHolder\" dojoAttachPoint=\"wizardControlContainerNode\">\n        <input class=\"WizardButton\" type=\"button\" dojoAttachPoint=\"previousButton\"/>\n        <input class=\"WizardButton\" type=\"button\" dojoAttachPoint=\"nextButton\"/>\n        <input class=\"WizardButton\" type=\"button\" dojoAttachPoint=\"doneButton\" style=\"display:none\"/>\n        <input class=\"WizardButton\" type=\"button\" dojoAttachPoint=\"cancelButton\"/>\n    </div>\n</div>\n",templateCssString:".WizardContainer {\n\tbackground: #EEEEEE;\n\tborder: #798EC5 1px solid;\n\tpadding: 2px;\n}\n\n.WizardTitle {\n\tcolor: #003366;\n\tpadding: 8px 5px 15px 2px;\n\tfont-weight: bold;\n\tfont-size: x-small;\n\tfont-style: normal;\n\tfont-family: Verdana, Arial, Helvetica;\n\ttext-align: left;\n}\n\n.WizardText {\n\tcolor: #000033;\n\tfont-weight: normal;\n\tfont-size: xx-small;\n\tfont-family: Verdana, Arial, Helvetica;\n\tpadding: 2 50; text-align: justify;\n}\n\n.WizardLightText {\n\tcolor: #666666;\n\tfont-weight: normal;\n\tfont-size: xx-small;\n\tfont-family: verdana, arial, helvetica;\n\tpadding: 2px 50px;\n\ttext-align: justify;\n}\n\n.WizardButtonHolder {\n\ttext-align: right;\n\tpadding: 10px 5px;\n}\n\n.WizardButton {\n\tcolor: #ffffff;\n\tbackground: #798EC5;\n\tfont-size: xx-small;\n\tfont-family: verdana, arial, helvetica, sans-serif;\n\tborder-right: #000000 1px solid;\n\tborder-bottom: #000000 1px solid;\n\tborder-left: #666666 1px solid;\n\tborder-top: #666666 1px solid;\n\tpadding-right: 4px;\n\tpadding-left: 4px;\n\ttext-decoration: none; height: 18px;\n}\n\n.WizardButton:hover {\n\tcursor: pointer;\n}\n\n.WizardButtonDisabled {\n\tcolor: #eeeeee;\n\tbackground-color: #999999;\n\tfont-size: xx-small;\n\tFONT-FAMILY: verdana, arial, helvetica, sans-serif;\n\tborder-right: #000000 1px solid;\n\tborder-bottom: #000000 1px solid;\n\tborder-left: #798EC5 1px solid;\n\tborder-top: #798EC5 1px solid;\n\tpadding-right: 4px;\n\tpadding-left: 4px;\n\ttext-decoration: none;\n\theight: 18px;\n}\n\n\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/Wizard.css"),selected:null,nextButtonLabel:"next",previousButtonLabel:"previous",cancelButtonLabel:"cancel",doneButtonLabel:"done",cancelFunction:"",hideDisabledButtons:false,fillInTemplate:function(_1,_2){
dojo.event.connect(this.nextButton,"onclick",this,"_onNextButtonClick");
dojo.event.connect(this.previousButton,"onclick",this,"_onPreviousButtonClick");
if(this.cancelFunction){
dojo.event.connect(this.cancelButton,"onclick",this.cancelFunction);
}else{
this.cancelButton.style.display="none";
}
dojo.event.connect(this.doneButton,"onclick",this,"done");
this.nextButton.value=this.nextButtonLabel;
this.previousButton.value=this.previousButtonLabel;
this.cancelButton.value=this.cancelButtonLabel;
this.doneButton.value=this.doneButtonLabel;
},_checkButtons:function(){
var _3=!this.hasNextPanel();
this.nextButton.disabled=_3;
this._setButtonClass(this.nextButton);
if(this.selected.doneFunction){
this.doneButton.style.display="";
if(_3){
this.nextButton.style.display="none";
}
}else{
this.doneButton.style.display="none";
}
this.previousButton.disabled=((!this.hasPreviousPanel())||(!this.selected.canGoBack));
this._setButtonClass(this.previousButton);
},_setButtonClass:function(_4){
if(!this.hideDisabledButtons){
_4.style.display="";
dojo.html.setClass(_4,_4.disabled?"WizardButtonDisabled":"WizardButton");
}else{
_4.style.display=_4.disabled?"none":"";
}
},registerChild:function(_5,_6){
dojo.widget.WizardContainer.superclass.registerChild.call(this,_5,_6);
this.wizardPanelContainerNode.appendChild(_5.domNode);
_5.hide();
if(!this.selected){
this.onSelected(_5);
}
this._checkButtons();
},onSelected:function(_7){
if(this.selected){
if(this.selected._checkPass()){
this.selected.hide();
}else{
return;
}
}
_7.show();
this.selected=_7;
},getPanels:function(){
return this.getChildrenOfType("WizardPane",false);
},selectedIndex:function(){
if(this.selected){
return dojo.lang.indexOf(this.getPanels(),this.selected);
}
return -1;
},_onNextButtonClick:function(){
var _8=this.selectedIndex();
if(_8>-1){
var _9=this.getPanels();
if(_9[_8+1]){
this.onSelected(_9[_8+1]);
}
}
this._checkButtons();
},_onPreviousButtonClick:function(){
var _a=this.selectedIndex();
if(_a>-1){
var _b=this.getPanels();
if(_b[_a-1]){
this.onSelected(_b[_a-1]);
}
}
this._checkButtons();
},hasNextPanel:function(){
var _c=this.selectedIndex();
return (_c<(this.getPanels().length-1));
},hasPreviousPanel:function(){
var _d=this.selectedIndex();
return (_d>0);
},done:function(){
this.selected.done();
}});
dojo.widget.defineWidget("dojo.widget.WizardPane",dojo.widget.ContentPane,{canGoBack:true,passFunction:"",doneFunction:"",postMixInProperties:function(_e,_f){
if(this.passFunction){
this.passFunction=dj_global[this.passFunction];
}
if(this.doneFunction){
this.doneFunction=dj_global[this.doneFunction];
}
dojo.widget.WizardPane.superclass.postMixInProperties.apply(this,arguments);
},_checkPass:function(){
if(this.passFunction&&dojo.lang.isFunction(this.passFunction)){
var _10=this.passFunction();
if(_10){
alert(_10);
return false;
}
}
return true;
},done:function(){
if(this.doneFunction&&dojo.lang.isFunction(this.doneFunction)){
this.doneFunction();
}
}});
