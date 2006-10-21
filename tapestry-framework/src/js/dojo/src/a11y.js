

dojo.provide("dojo.a11y");

dojo.require("dojo.uri.*");
dojo.require("dojo.html.common");

dojo.a11y = {




imgPath:dojo.uri.dojoUri("src/widget/templates/images"),
doAccessibleCheck: true,
accessible: null,

checkAccessible: function(){



if(this.accessible === null){
this.accessible = false; //default
if(this.doAccessibleCheck == true){
this.accessible = this.testAccessible();
}
}
return this.accessible;
},

testAccessible: function(){




this.accessible = false; //default
if (dojo.render.html.ie || dojo.render.html.mozilla){
var div = document.createElement("div");
//div.style.color="rgb(153,204,204)";
div.style.backgroundImage = "url(\"" + this.imgPath + "/tab_close.gif\")";
// must add to hierarchy before can view currentStyle below
dojo.body().appendChild(div);
// in FF and IE the value for the current background style of the added div
// will be "none" in high contrast mode
// in FF the return value will be url(invalid-url:) when running over http
var bkImg = null;
if (window.getComputedStyle  ) {
var cStyle = getComputedStyle(div, "");
bkImg = cStyle.getPropertyValue("background-image");
}else{
bkImg = div.currentStyle.backgroundImage;
}
var bUseImgElem = false;
if (bkImg != null && (bkImg == "none" || bkImg == "url(invalid-url:)" )) {
this.accessible = true;
}

dojo.body().removeChild(div);
}
return this.accessible;
},

setCheckAccessible: function( bTest){




this.doAccessibleCheck = bTest;
},

setAccessibleMode: function(){




if (this.accessible === null){
if (this.checkAccessible()){
dojo.render.html.prefixes.unshift("a11y");
}
}
return this.accessible;
}
};



