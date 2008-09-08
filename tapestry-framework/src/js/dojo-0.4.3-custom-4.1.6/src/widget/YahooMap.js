dojo.provide("dojo.widget.YahooMap");
dojo.require("dojo.event.*");
dojo.require("dojo.math");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.HtmlWidget");
(function(){
var _1=djConfig["yAppId"]||djConfig["yahooAppId"]||"dojotoolkit";
if(!dojo.hostenv.post_load_){
if(_1=="dojotoolkit"){
dojo.debug("please provide a unique Yahoo App ID in djConfig.yahooAppId when using the map widget");
}
var _2="<scr"+"ipt src='http://api.maps.yahoo.com/ajaxymap?v=3.0&appid="+_1+"'></scri"+"pt>";
if(!dj_global["YMap"]){
document.write(_2);
}
}else{
dojo.debug("cannot initialize map system after the page has been loaded! Please either manually include the script block provided by Yahoo in your page or require() the YahooMap widget before onload has fired");
}
})();
dojo.widget.defineWidget("dojo.widget.YahooMap",dojo.widget.HtmlWidget,function(){
this.map=null;
this.datasrc="";
this.data=[];
this.width=0;
this.height=0;
this.controls=["zoomlong","maptype","pan"];
},{isContainer:false,templatePath:null,templateCssPath:null,findCenter:function(_3){
var _4=new YGeoPoint(37,-90);
if(_3.length==0){
return _4;
}
var _5,_6,_7,_8,_9,_a;
_5=_6=_3[0].Lat;
_7=_8=_3[0].Lon;
for(var i=0;i<_3.length;i++){
_5=Math.min(_5,_3[i].Lat);
_6=Math.max(_6,_3[i].Lat);
_7=Math.min(_7,_3[i].Lon);
_8=Math.max(_8,_3[i].Lon);
}
_9=dojo.math.round((_5+_6)/2,6);
_a=dojo.math.round((_7+_8)/2,6);
return new YGeoPoint(_9,_a);
},setControls:function(){
var _c={maptype:"addTypeControl",pan:"addPanControl",zoomlong:"addZoomLong",zoomshort:"addZoomShort"};
var c=this.controls;
for(var i=0;i<c.length;i++){
var _f=_c[c[i].toLowerCase()];
if(this.map[_f]){
this.map[_f]();
}
}
},parse:function(_10){
this.data=[];
var h=_10.getElementsByTagName("thead")[0];
if(!h){
return;
}
var a=[];
var _13=h.getElementsByTagName("td");
if(_13.length==0){
_13=h.getElementsByTagName("th");
}
for(var i=0;i<_13.length;i++){
var c=_13[i].innerHTML.toLowerCase();
if(c=="long"){
c="lng";
}
a.push(c);
}
var b=_10.getElementsByTagName("tbody")[0];
if(!b){
return;
}
for(var i=0;i<b.childNodes.length;i++){
if(!(b.childNodes[i].nodeName&&b.childNodes[i].nodeName.toLowerCase()=="tr")){
continue;
}
var _17=b.childNodes[i].getElementsByTagName("td");
var o={};
for(var j=0;j<a.length;j++){
var col=a[j];
if(col=="lat"||col=="lng"){
o[col]=parseFloat(_17[j].innerHTML);
}else{
o[col]=_17[j].innerHTML;
}
}
this.data.push(o);
}
},render:function(){
var pts=[];
var d=this.data;
for(var i=0;i<d.length;i++){
var pt=new YGeoPoint(d[i].lat,d[i].lng);
pts.push(pt);
var _1f=d[i].icon||null;
if(_1f){
_1f=new YImage(_1f);
}
var m=new YMarker(pt,_1f);
if(d[i].description){
m.addAutoExpand("<div>"+d[i].description+"</div>");
}
this.map.addOverlay(m);
}
var c=this.findCenter(pts);
var z=this.map.getZoomLevel(pts);
this.map.drawZoomAndCenter(c,z);
},initialize:function(_23,_24){
if(!YMap||!YGeoPoint){
dojo.raise("dojo.widget.YahooMap: The Yahoo Map script must be included in order to use this widget.");
}
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
if(this.width>0&&this.height>0){
this.map=new YMap(this.domNode,YAHOO_MAP_REG,new YSize(this.width,this.height));
}else{
this.map=new YMap(this.domNode);
}
this.setControls();
this.render();
}});
