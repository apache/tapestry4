dojo.provide("dojo.widget.GoogleMap");
dojo.require("dojo.event.*");
dojo.require("dojo.math");
dojo.require("dojo.widget.*");
dojo.require("dojo.uri.Uri");
dojo.require("dojo.widget.HtmlWidget");
(function(){
var _1=djConfig["gMapKey"]||djConfig["googleMapKey"];
var _2=new dojo.uri.Uri(window.location.href);
if(_2.host=="www.dojotoolkit.org"){
_1="ABQIAAAACUNdgv_7FGOmUslbm9l6_hRqjp7ri2mNiOEYqetD3xnFHpt5rBSjszDd1sdufPyQKUTyCf_YxoIxvw";
}else{
if(_2.host=="blog.dojotoolkit.org"){
_1="ABQIAAAACUNdgv_7FGOmUslbm9l6_hSkep6Av1xaMhVn3yCLkorJeXeLARQ6fammI_P3qSGleTJhoI5_1JmP_Q";
}else{
if(_2.host=="archive.dojotoolkit.org"){
_1="ABQIAAAACUNdgv_7FGOmUslbm9l6_hTaQpDt0dyGLIHbXMPTzg1kWeAfwRTwZNyrUfbfxYE9yIvRivEjcXoDTg";
}else{
if(_2.host=="dojotoolkit.org"){
_1="ABQIAAAACUNdgv_7FGOmUslbm9l6_hSaOaO_TgJ5c3mtQFnk5JO2zD5dZBRZk-ieqVs7BORREYNzAERmcJoEjQ";
}
}
}
}
if(!dojo.hostenv.post_load_){
if(!_1||_1==""){
dojo.raise("dojo.widget.GoogleMap: The Google Map widget requires a proper API key in order to be used.");
}
var _3="<scr"+"ipt src='http://maps.google.com/maps?file=api&amp;v=2&amp;key="+_1+"'></scri"+"pt>";
if(!dj_global["GMap2"]){
document.write(_3);
}
}else{
dojo.debug("Cannot initialize Google Map system after the page has been loaded! Please either manually include the script block provided by Google in your page or require() the GoogleMap widget before onload has fired.");
}
})();
dojo.widget.defineWidget("dojo.widget.GoogleMap",dojo.widget.HtmlWidget,function(){
this.map=null;
this.geocoder=null;
this.data=[];
this.datasrc="";
this.controls=["largemap","scale","maptype"];
},{templatePath:null,templateCssPath:null,isContainer:false,_defaultPoint:{lat:39.10662,lng:-94.578209},setControls:function(){
var _4={largemap:GLargeMapControl,smallmap:GSmallMapControl,smallzoom:GSmallZoomControl,scale:GScaleControl,maptype:GMapTypeControl,overview:GOverviewMapControl};
for(var i=0;i<this.controls.length;i++){
this.map.addControl(new (_4[this.controls[i].toLowerCase()])());
}
},findCenter:function(_6){
if(this.data.length==1){
return (new GLatLng(this.data[0].lat,this.data[0].lng));
}
var _7=(_6.getNorthEast().lat()+_6.getSouthWest().lat())/2;
var _8=(_6.getNorthEast().lng()+_6.getSouthWest().lng())/2;
return (new GLatLng(_7,_8));
},createPinpoint:function(pt,_a){
var m=new GMarker(pt);
if(_a){
GEvent.addListener(m,"click",function(){
m.openInfoWindowHtml("<div>"+_a+"</div>");
});
}
return m;
},plot:function(_c){
var p=new GLatLng(_c.lat,_c.lng);
var d=_c.description||null;
var m=this.createPinpoint(p,d);
this.map.addOverlay(m);
},plotAddress:function(_10){
var _11=this;
this.geocoder.getLocations(_10,function(_12){
if(!_12||_12.Status.code!=200){
alert("The address \""+_10+"\" was not found.");
return;
}
var obj={lat:_12.Placemark[0].Point.coordinates[1],lng:_12.Placemark[0].Point.coordinates[0],description:_12.Placemark[0].address};
_11.data.push(obj);
_11.render();
});
},parse:function(_14){
this.data=[];
var h=_14.getElementsByTagName("thead")[0];
if(!h){
return;
}
var a=[];
var _17=h.getElementsByTagName("td");
if(_17.length==0){
_17=h.getElementsByTagName("th");
}
for(var i=0;i<_17.length;i++){
var c=_17[i].innerHTML.toLowerCase();
if(c=="long"){
c="lng";
}
a.push(c);
}
var b=_14.getElementsByTagName("tbody")[0];
if(!b){
return;
}
for(var i=0;i<b.childNodes.length;i++){
if(!(b.childNodes[i].nodeName&&b.childNodes[i].nodeName.toLowerCase()=="tr")){
continue;
}
var _1b=b.childNodes[i].getElementsByTagName("td");
var o={};
for(var j=0;j<a.length;j++){
var col=a[j];
if(col=="lat"||col=="lng"){
o[col]=parseFloat(_1b[j].innerHTML);
}else{
o[col]=_1b[j].innerHTML;
}
}
this.data.push(o);
}
},render:function(){
if(this.data.length==0){
this.map.setCenter(new GLatLng(this._defaultPoint.lat,this._defaultPoint.lng),4);
return;
}
this.map.clearOverlays();
var _1f=new GLatLngBounds();
var d=this.data;
for(var i=0;i<d.length;i++){
_1f.extend(new GLatLng(d[i].lat,d[i].lng));
}
var _22=Math.min((this.map.getBoundsZoomLevel(_1f)-1),14);
this.map.setCenter(this.findCenter(_1f),_22);
for(var i=0;i<this.data.length;i++){
this.plot(this.data[i]);
}
},initialize:function(_23,_24){
if(this.datasrc){
this.parse(dojo.byId(this.datasrc));
}else{
if(this.domNode.getElementsByTagName("table")[0]){
this.parse(this.domNode.getElementsByTagName("table")[0]);
}
}
},postCreate:function(){
while(this.domNode.childNodes.length>0){
this.domNode.removeChild(this.domNode.childNodes[0]);
}
if(this.domNode.style.position!="absolute"){
this.domNode.style.position="relative";
}
this.map=new GMap2(this.domNode);
try{
this.geocoder=new GClientGeocoder();
}
catch(ex){
}
this.render();
this.setControls();
}});
