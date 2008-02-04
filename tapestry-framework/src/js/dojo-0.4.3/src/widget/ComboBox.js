dojo.provide("dojo.widget.ComboBox");
dojo.require("dojo.widget.*");
dojo.require("dojo.event.*");
dojo.require("dojo.io.*");
dojo.require("dojo.html.*");
dojo.require("dojo.string");
dojo.require("dojo.widget.html.stabile");
dojo.require("dojo.widget.PopupContainer");
dojo.declare("dojo.widget.incrementalComboBoxDataProvider",null,function(_1){
this.searchUrl=_1.dataUrl;
this._cache={};
this._inFlight=false;
this._lastRequest=null;
this.allowCache=false;
},{_addToCache:function(_2,_3){
if(this.allowCache){
this._cache[_2]=_3;
}
},startSearch:function(_4,_5){
if(this._inFlight){
}
var _6=encodeURIComponent(_4);
var _7=dojo.string.substituteParams(this.searchUrl,{"searchString":_6});
var _8=this;
var _9=this._lastRequest=dojo.io.bind({url:_7,method:"get",mimetype:"text/json",load:function(_a,_b,_c){
_8._inFlight=false;
if(!dojo.lang.isArray(_b)){
var _d=[];
for(var _e in _b){
_d.push([_b[_e],_e]);
}
_b=_d;
}
_8._addToCache(_4,_b);
if(_9==_8._lastRequest){
_5(_b);
}
}});
this._inFlight=true;
}});
dojo.declare("dojo.widget.basicComboBoxDataProvider",null,function(_f,_10){
this._data=[];
this.searchLimit=30;
this.searchType="STARTSTRING";
this.caseSensitive=false;
if(!dj_undef("dataUrl",_f)&&!dojo.string.isBlank(_f.dataUrl)){
this._getData(_f.dataUrl);
}else{
if((_10)&&(_10.nodeName.toLowerCase()=="select")){
var _11=_10.getElementsByTagName("option");
var ol=_11.length;
var _13=[];
for(var x=0;x<ol;x++){
var _15=_11[x].textContent||_11[x].innerText||_11[x].innerHTML;
var _16=[String(_15),String(_11[x].value)];
_13.push(_16);
if(_11[x].selected){
_f.setAllValues(_16[0],_16[1]);
}
}
this.setData(_13);
}
}
},{_getData:function(url){
dojo.io.bind({url:url,load:dojo.lang.hitch(this,function(_18,_19,evt){
if(!dojo.lang.isArray(_19)){
var _1b=[];
for(var key in _19){
_1b.push([_19[key],key]);
}
_19=_1b;
}
this.setData(_19);
}),mimetype:"text/json"});
},startSearch:function(_1d,_1e){
this._performSearch(_1d,_1e);
},_performSearch:function(_1f,_20){
var st=this.searchType;
var ret=[];
if(!this.caseSensitive){
_1f=_1f.toLowerCase();
}
for(var x=0;x<this._data.length;x++){
if((this.searchLimit>0)&&(ret.length>=this.searchLimit)){
break;
}
var _24=new String((!this.caseSensitive)?this._data[x][0].toLowerCase():this._data[x][0]);
if(_24.length<_1f.length){
continue;
}
if(st=="STARTSTRING"){
if(_1f==_24.substr(0,_1f.length)){
ret.push(this._data[x]);
}
}else{
if(st=="SUBSTRING"){
if(_24.indexOf(_1f)>=0){
ret.push(this._data[x]);
}
}else{
if(st=="STARTWORD"){
var idx=_24.indexOf(_1f);
if(idx==0){
ret.push(this._data[x]);
}
if(idx<=0){
continue;
}
var _26=false;
while(idx!=-1){
if(" ,/(".indexOf(_24.charAt(idx-1))!=-1){
_26=true;
break;
}
idx=_24.indexOf(_1f,idx+1);
}
if(!_26){
continue;
}else{
ret.push(this._data[x]);
}
}
}
}
}
_20(ret);
},setData:function(_27){
this._data=_27;
}});
dojo.widget.defineWidget("dojo.widget.ComboBox",dojo.widget.HtmlWidget,{forceValidOption:false,searchType:"stringstart",dataProvider:null,autoComplete:true,searchDelay:100,dataUrl:"",fadeTime:200,maxListLength:8,mode:"local",selectedResult:null,dataProviderClass:"",buttonSrc:dojo.uri.moduleUri("dojo.widget","templates/images/combo_box_arrow.png"),dropdownToggle:"fade",templateString:"<span _=\"whitespace and CR's between tags adds &nbsp; in FF\"\n\tclass=\"dojoComboBoxOuter\"\n\t><input style=\"display:none\"  tabindex=\"-1\" name=\"\" value=\"\" \n\t\tdojoAttachPoint=\"comboBoxValue\"\n\t><input style=\"display:none\"  tabindex=\"-1\" name=\"\" value=\"\" \n\t\tdojoAttachPoint=\"comboBoxSelectionValue\"\n\t><input type=\"text\" autocomplete=\"off\" class=\"dojoComboBox\"\n\t\tdojoAttachEvent=\"key:_handleKeyEvents; keyUp: onKeyUp; compositionEnd; onResize;\"\n\t\tdojoAttachPoint=\"textInputNode\"\n\t><img hspace=\"0\"\n\t\tvspace=\"0\"\n\t\tclass=\"dojoComboBox\"\n\t\tdojoAttachPoint=\"downArrowNode\"\n\t\tdojoAttachEvent=\"onMouseUp: handleArrowClick; onResize;\"\n\t\tsrc=\"${this.buttonSrc}\"\n></span>\n",templateCssString:".dojoComboBoxOuter {\n\tborder: 0px !important;\n\tmargin: 0px !important;\n\tpadding: 0px !important;\n\tbackground: transparent !important;\n\twhite-space: nowrap !important;\n}\n\n.dojoComboBox {\n\tborder: 1px inset #afafaf;\n\tmargin: 0px;\n\tpadding: 0px;\n\tvertical-align: middle !important;\n\tfloat: none !important;\n\tposition: static !important;\n\tdisplay: inline !important;\n}\n\n/* the input box */\ninput.dojoComboBox {\n\tborder-right-width: 0px !important; \n\tmargin-right: 0px !important;\n\tpadding-right: 0px !important;\n}\n\n/* the down arrow */\nimg.dojoComboBox {\n\tborder-left-width: 0px !important;\n\tpadding-left: 0px !important;\n\tmargin-left: 0px !important;\n}\n\n/* IE vertical-alignment calculations can be off by +-1 but these margins are collapsed away */\n.dj_ie img.dojoComboBox {\n\tmargin-top: 1px; \n\tmargin-bottom: 1px; \n}\n\n/* the drop down */\n.dojoComboBoxOptions {\n\tfont-family: Verdana, Helvetica, Garamond, sans-serif;\n\t/* font-size: 0.7em; */\n\tbackground-color: white;\n\tborder: 1px solid #afafaf;\n\tposition: absolute;\n\tz-index: 1000; \n\toverflow: auto;\n\tcursor: default;\n}\n\n.dojoComboBoxItem {\n\tpadding-left: 2px;\n\tpadding-top: 2px;\n\tmargin: 0px;\n}\n\n.dojoComboBoxItemEven {\n\tbackground-color: #f4f4f4;\n}\n\n.dojoComboBoxItemOdd {\n\tbackground-color: white;\n}\n\n.dojoComboBoxItemHighlight {\n\tbackground-color: #63709A;\n\tcolor: white;\n}\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","templates/ComboBox.css"),setValue:function(_28){
this.comboBoxValue.value=_28;
if(this.textInputNode.value!=_28){
this.textInputNode.value=_28;
dojo.widget.html.stabile.setState(this.widgetId,this.getState(),true);
this.onValueChanged(_28);
}
},onValueChanged:function(_29){
},getValue:function(){
return this.comboBoxValue.value;
},getState:function(){
return {value:this.getValue()};
},setState:function(_2a){
this.setValue(_2a.value);
},enable:function(){
this.disabled=false;
this.textInputNode.removeAttribute("disabled");
},disable:function(){
this.disabled=true;
this.textInputNode.setAttribute("disabled",true);
},_getCaretPos:function(_2b){
if(dojo.lang.isNumber(_2b.selectionStart)){
return _2b.selectionStart;
}else{
if(dojo.render.html.ie){
var tr=document.selection.createRange().duplicate();
var ntr=_2b.createTextRange();
tr.move("character",0);
ntr.move("character",0);
try{
ntr.setEndPoint("EndToEnd",tr);
return String(ntr.text).replace(/\r/g,"").length;
}
catch(e){
return 0;
}
}
}
},_setCaretPos:function(_2e,_2f){
_2f=parseInt(_2f);
this._setSelectedRange(_2e,_2f,_2f);
},_setSelectedRange:function(_30,_31,end){
if(!end){
end=_30.value.length;
}
if(_30.setSelectionRange){
_30.focus();
_30.setSelectionRange(_31,end);
}else{
if(_30.createTextRange){
var _33=_30.createTextRange();
with(_33){
collapse(true);
moveEnd("character",end);
moveStart("character",_31);
select();
}
}else{
_30.value=_30.value;
_30.blur();
_30.focus();
var _34=parseInt(_30.value.length)-end;
var _35=String.fromCharCode(37);
var tcc=_35.charCodeAt(0);
for(var x=0;x<_34;x++){
var te=document.createEvent("KeyEvents");
te.initKeyEvent("keypress",true,true,null,false,false,false,false,tcc,tcc);
_30.dispatchEvent(te);
}
}
}
},_handleKeyEvents:function(evt){
if(evt.ctrlKey||evt.altKey||!evt.key){
return;
}
this._prev_key_backspace=false;
this._prev_key_esc=false;
var k=dojo.event.browser.keys;
var _3b=true;
switch(evt.key){
case k.KEY_DOWN_ARROW:
if(!this.popupWidget.isShowingNow){
this._startSearchFromInput();
}
this._highlightNextOption();
dojo.event.browser.stopEvent(evt);
return;
case k.KEY_UP_ARROW:
this._highlightPrevOption();
dojo.event.browser.stopEvent(evt);
return;
case k.KEY_TAB:
if(!this.autoComplete&&this.popupWidget.isShowingNow&&this._highlighted_option){
dojo.event.browser.stopEvent(evt);
this._selectOption({"target":this._highlighted_option,"noHide":false});
this._setSelectedRange(this.textInputNode,this.textInputNode.value.length,null);
}else{
this._selectOption();
return;
}
break;
case k.KEY_ENTER:
if(this.popupWidget.isShowingNow){
dojo.event.browser.stopEvent(evt);
}
if(this.autoComplete){
this._selectOption();
return;
}
case " ":
if(this.popupWidget.isShowingNow&&this._highlighted_option){
dojo.event.browser.stopEvent(evt);
this._selectOption();
this._hideResultList();
return;
}
break;
case k.KEY_ESCAPE:
this._hideResultList();
this._prev_key_esc=true;
return;
case k.KEY_BACKSPACE:
this._prev_key_backspace=true;
if(!this.textInputNode.value.length){
this.setAllValues("","");
this._hideResultList();
_3b=false;
}
break;
case k.KEY_RIGHT_ARROW:
case k.KEY_LEFT_ARROW:
_3b=false;
break;
default:
if(evt.charCode==0){
_3b=false;
}
}
if(this.searchTimer){
clearTimeout(this.searchTimer);
}
if(_3b){
this._blurOptionNode();
this.searchTimer=setTimeout(dojo.lang.hitch(this,this._startSearchFromInput),this.searchDelay);
}
},compositionEnd:function(evt){
evt.key=evt.keyCode;
this._handleKeyEvents(evt);
},onKeyUp:function(evt){
this.setValue(this.textInputNode.value);
},setSelectedValue:function(_3e){
this.comboBoxSelectionValue.value=_3e;
},setAllValues:function(_3f,_40){
this.setSelectedValue(_40);
this.setValue(_3f);
},_focusOptionNode:function(_41){
if(this._highlighted_option!=_41){
this._blurOptionNode();
this._highlighted_option=_41;
dojo.html.addClass(this._highlighted_option,"dojoComboBoxItemHighlight");
}
},_blurOptionNode:function(){
if(this._highlighted_option){
dojo.html.removeClass(this._highlighted_option,"dojoComboBoxItemHighlight");
this._highlighted_option=null;
}
},_highlightNextOption:function(){
if((!this._highlighted_option)||!this._highlighted_option.parentNode){
this._focusOptionNode(this.optionsListNode.firstChild);
}else{
if(this._highlighted_option.nextSibling){
this._focusOptionNode(this._highlighted_option.nextSibling);
}
}
dojo.html.scrollIntoView(this._highlighted_option);
},_highlightPrevOption:function(){
if(this._highlighted_option&&this._highlighted_option.previousSibling){
this._focusOptionNode(this._highlighted_option.previousSibling);
}else{
this._highlighted_option=null;
this._hideResultList();
return;
}
dojo.html.scrollIntoView(this._highlighted_option);
},_itemMouseOver:function(evt){
if(evt.target===this.optionsListNode){
return;
}
this._focusOptionNode(evt.target);
dojo.html.addClass(this._highlighted_option,"dojoComboBoxItemHighlight");
},_itemMouseOut:function(evt){
if(evt.target===this.optionsListNode){
return;
}
this._blurOptionNode();
},onResize:function(){
var _44=dojo.html.getContentBox(this.textInputNode);
if(_44.height<=0){
dojo.lang.setTimeout(this,"onResize",100);
return;
}
var _45={width:_44.height,height:_44.height};
dojo.html.setContentBox(this.downArrowNode,_45);
},fillInTemplate:function(_46,_47){
dojo.html.applyBrowserClass(this.domNode);
var _48=this.getFragNodeRef(_47);
if(!this.name&&_48.name){
this.name=_48.name;
}
this.comboBoxValue.name=this.name;
this.comboBoxSelectionValue.name=this.name+"_selected";
dojo.html.copyStyle(this.domNode,_48);
dojo.html.copyStyle(this.textInputNode,_48);
dojo.html.copyStyle(this.downArrowNode,_48);
with(this.downArrowNode.style){
width="0px";
height="0px";
}
var _49;
if(this.dataProviderClass){
if(typeof this.dataProviderClass=="string"){
_49=dojo.evalObjPath(this.dataProviderClass);
}else{
_49=this.dataProviderClass;
}
}else{
if(this.mode=="remote"){
_49=dojo.widget.incrementalComboBoxDataProvider;
}else{
_49=dojo.widget.basicComboBoxDataProvider;
}
}
this.dataProvider=new _49(this,this.getFragNodeRef(_47));
this.popupWidget=new dojo.widget.createWidget("PopupContainer",{toggle:this.dropdownToggle,toggleDuration:this.toggleDuration});
dojo.event.connect(this,"destroy",this.popupWidget,"destroy");
this.optionsListNode=this.popupWidget.domNode;
this.domNode.appendChild(this.optionsListNode);
dojo.html.addClass(this.optionsListNode,"dojoComboBoxOptions");
dojo.event.connect(this.optionsListNode,"onclick",this,"_selectOption");
dojo.event.connect(this.optionsListNode,"onmouseover",this,"_onMouseOver");
dojo.event.connect(this.optionsListNode,"onmouseout",this,"_onMouseOut");
dojo.event.connect(this.optionsListNode,"onmouseover",this,"_itemMouseOver");
dojo.event.connect(this.optionsListNode,"onmouseout",this,"_itemMouseOut");
},_openResultList:function(_4a){
if(this.disabled){
return;
}
this._clearResultList();
if(!_4a.length){
this._hideResultList();
}
if((this.autoComplete)&&(_4a.length)&&(!this._prev_key_backspace)&&(this.textInputNode.value.length>0)){
var _4b=this._getCaretPos(this.textInputNode);
if((_4b+1)>this.textInputNode.value.length){
this.textInputNode.value+=_4a[0][0].substr(_4b);
this._setSelectedRange(this.textInputNode,_4b,this.textInputNode.value.length);
}
}
var _4c=true;
while(_4a.length){
var tr=_4a.shift();
if(tr){
var td=document.createElement("div");
td.appendChild(document.createTextNode(tr[0]));
td.setAttribute("resultName",tr[0]);
td.setAttribute("resultValue",tr[1]);
td.className="dojoComboBoxItem "+((_4c)?"dojoComboBoxItemEven":"dojoComboBoxItemOdd");
_4c=(!_4c);
this.optionsListNode.appendChild(td);
}
}
this._showResultList();
},_onFocusInput:function(){
this._hasFocus=true;
},_onBlurInput:function(){
this._hasFocus=false;
this._handleBlurTimer(true,500);
},_handleBlurTimer:function(_4f,_50){
if(this.blurTimer&&(_4f||_50)){
clearTimeout(this.blurTimer);
}
if(_50){
this.blurTimer=dojo.lang.setTimeout(this,"_checkBlurred",_50);
}
},_onMouseOver:function(evt){
if(!this._mouseover_list){
this._handleBlurTimer(true,0);
this._mouseover_list=true;
}
},_onMouseOut:function(evt){
var _53=evt.relatedTarget;
try{
if(!_53||_53.parentNode!=this.optionsListNode){
this._mouseover_list=false;
this._handleBlurTimer(true,100);
this._tryFocus();
}
}
catch(e){
}
},_isInputEqualToResult:function(_54){
var _55=this.textInputNode.value;
if(!this.dataProvider.caseSensitive){
_55=_55.toLowerCase();
_54=_54.toLowerCase();
}
return (_55==_54);
},_isValidOption:function(){
var tgt=dojo.html.firstElement(this.optionsListNode);
var _57=false;
while(!_57&&tgt){
if(this._isInputEqualToResult(tgt.getAttribute("resultName"))){
_57=true;
}else{
tgt=dojo.html.nextElement(tgt);
}
}
return _57;
},_checkBlurred:function(){
if(!this._hasFocus&&!this._mouseover_list){
this._hideResultList();
if(!this.textInputNode.value.length){
this.setAllValues("","");
return;
}
var _58=this._isValidOption();
if(this.forceValidOption&&!_58){
this.setAllValues("","");
return;
}
if(!_58){
this.setSelectedValue("");
}
}
},_selectOption:function(evt){
var tgt=null;
if(!evt){
evt={target:this._highlighted_option};
}
if(!dojo.html.isDescendantOf(evt.target,this.optionsListNode)){
if(!this.textInputNode.value.length){
return;
}
tgt=dojo.html.firstElement(this.optionsListNode);
if(!tgt||!this._isInputEqualToResult(tgt.getAttribute("resultName"))){
return;
}
}else{
tgt=evt.target;
}
while((tgt.nodeType!=1)||(!tgt.getAttribute("resultName"))){
tgt=tgt.parentNode;
if(tgt===dojo.body()){
return false;
}
}
this.selectedResult=[tgt.getAttribute("resultName"),tgt.getAttribute("resultValue")];
this.setAllValues(tgt.getAttribute("resultName"),tgt.getAttribute("resultValue"));
if(!evt.noHide){
this._hideResultList();
this._setSelectedRange(this.textInputNode,0,null);
}
this._tryFocus();
},_clearResultList:function(){
if(this.optionsListNode.innerHTML){
this.optionsListNode.innerHTML="";
}
},_hideResultList:function(){
this.popupWidget.close();
},_showResultList:function(){
var _5b=this.optionsListNode.childNodes;
if(_5b.length){
var _5c=Math.min(_5b.length,this.maxListLength);
with(this.optionsListNode.style){
display="";
if(_5c==_5b.length){
height="";
}else{
height=_5c*dojo.html.getMarginBox(_5b[0]).height+"px";
}
width=(dojo.html.getMarginBox(this.domNode).width-2)+"px";
}
this.popupWidget.open(this.domNode,this,this.downArrowNode);
}else{
this._hideResultList();
}
},handleArrowClick:function(){
this._handleBlurTimer(true,0);
this._tryFocus();
if(this.popupWidget.isShowingNow){
this._hideResultList();
}else{
this._startSearch("");
}
},_tryFocus:function(){
try{
this.textInputNode.focus();
}
catch(e){
}
},_startSearchFromInput:function(){
this._startSearch(this.textInputNode.value);
},_startSearch:function(key){
this.dataProvider.startSearch(key,dojo.lang.hitch(this,"_openResultList"));
},postCreate:function(){
this.onResize();
dojo.event.connect(this.textInputNode,"onblur",this,"_onBlurInput");
dojo.event.connect(this.textInputNode,"onfocus",this,"_onFocusInput");
if(this.disabled){
this.disable();
}
var s=dojo.widget.html.stabile.getState(this.widgetId);
if(s){
this.setState(s);
}
}});
