dojo.provide("tapestry.test");

if (dj_undef("byId", dj_global)) {
dojo.byId=function(id) {
	return document.getElementById(id);
}
}