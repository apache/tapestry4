dojo.provide("dojo.profile");
dojo.profile={_profiles:{},_pns:[],start:function(_1){
if(!this._profiles[_1]){
this._profiles[_1]={iters:0,total:0};
this._pns[this._pns.length]=_1;
}else{
if(this._profiles[_1]["start"]){
this.end(_1);
}
}
this._profiles[_1].end=null;
this._profiles[_1].start=new Date();
},end:function(_2){
var ed=new Date();
if((this._profiles[_2])&&(this._profiles[_2]["start"])){
with(this._profiles[_2]){
end=ed;
total+=(end-start);
start=null;
iters++;
}
}else{
return true;
}
},dump:function(_4){
var _5=document.createElement("table");
with(_5.style){
border="1px solid black";
borderCollapse="collapse";
}
var _6=_5.createTHead();
var _7=_6.insertRow(0);
var _8=["Identifier","Calls","Total","Avg"];
for(var x=0;x<_8.length;x++){
var _a=_7.insertCell(x);
with(_a.style){
backgroundColor="#225d94";
color="white";
borderBottom="1px solid black";
borderRight="1px solid black";
fontFamily="tahoma";
fontWeight="bolder";
paddingLeft=paddingRight="5px";
}
_a.appendChild(document.createTextNode(_8[x]));
}
for(var x=0;x<this._pns.length;x++){
var _b=this._profiles[this._pns[x]];
this.end(this._pns[x]);
if(_b.iters>0){
var _c=_5.insertRow(true);
var _d=[this._pns[x],_b.iters,_b.total,parseInt(_b.total/_b.iters)];
for(var y=0;y<_d.length;y++){
var cc=_c.insertCell(y);
cc.appendChild(document.createTextNode(_d[y]));
with(cc.style){
borderBottom="1px solid gray";
paddingLeft=paddingRight="5px";
if(x%2){
backgroundColor="#e1f1ff";
}
if(y>0){
textAlign="right";
borderRight="1px solid gray";
}else{
borderRight="1px solid black";
}
}
}
}
}
if(_4){
var ne=document.createElement("div");
ne.id="profileOutputTable";
with(ne.style){
fontFamily="Courier New, monospace";
fontSize="12px";
lineHeight="16px";
borderTop="1px solid black";
padding="10px";
}
if(document.getElementById("profileOutputTable")){
dojo.body().replaceChild(ne,document.getElementById("profileOutputTable"));
}else{
dojo.body().appendChild(ne);
}
ne.appendChild(_5);
}
return _5;
}};
dojo.profile.stop=dojo.profile.end;
