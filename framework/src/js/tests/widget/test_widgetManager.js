dojo.require("dojo.widget.DomWidget");

function test_domwidget_ctor(){
	var dw  = new dojo.widget.DomWidget();
	
	jum.assertEquals("test1", (typeof dw), "object");
	jum.assertEquals("test3", null, dw.templateNode);
	jum.assertEquals("test4", null, dw.templateString);
	dw.buildFromTemplate();
}