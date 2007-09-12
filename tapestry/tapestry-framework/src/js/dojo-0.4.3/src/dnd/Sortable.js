dojo.provide("dojo.dnd.Sortable");
dojo.require("dojo.dnd.*");
dojo.dnd.Sortable=function(){
};
dojo.lang.extend(dojo.dnd.Sortable,{ondragstart:function(e){
var _2=e.target;
while(_2.parentNode&&_2.parentNode!=this){
_2=_2.parentNode;
}
return _2;
}});
