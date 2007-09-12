dojo.provide("dojo.widget.demoEngine.SourcePane");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.io.*");
dojo.widget.defineWidget("my.widget.demoEngine.SourcePane",dojo.widget.HtmlWidget,{templateString:"<div dojoAttachPoint=\"domNode\">\n\t<textarea dojoAttachPoint=\"sourceNode\" rows=\"100%\"></textarea>\n</div>\n",templateCssString:".sourcePane {\n\twidth: 100%;\n\theight: 100%;\n\tpadding: 0px;\n\tmargin: 0px;\n\toverflow: hidden;\n}\n\n.sourcePane textarea{\n\twidth: 100%;\n\theight: 100%;\n\tborder: 0px;\n\toverflow: auto;\n\tpadding: 0px;\n\tmargin:0px;\n}\n\n* html .sourcePane {\n\toverflow: auto;\n}\n",templateCssPath:dojo.uri.moduleUri("dojo.widget","demoEngine/templates/SourcePane.css"),postCreate:function(){
dojo.html.addClass(this.domNode,this.domNodeClass);
dojo.debug("PostCreate");
},getSource:function(){
if(this.href){
dojo.io.bind({url:this.href,load:dojo.lang.hitch(this,"fillInSource"),mimetype:"text/plain"});
}
},fillInSource:function(_1,_2,e){
this.sourceNode.value=_2;
},setHref:function(_4){
this.href=_4;
this.getSource();
}},"",function(){
dojo.debug("SourcePane Init");
this.domNodeClass="sourcePane";
this.sourceNode="";
this.href="";
});
