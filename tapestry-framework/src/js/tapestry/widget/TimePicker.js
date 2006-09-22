dojo.provide("tapestry.widget.TimePicker");

dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.date.common");
dojo.require("dojo.date.format");
dojo.require("dojo.lang.common");

dojo.widget.defineWidget(
	"tapestry.widget.TimePicker",
	dojo.widget.HtmlWidget,
	{
		
	}
);

tapestry.widget.TimePicker=function(){
	dojo.widget.HtmlWidget.call(this);
	
	this.widgetType="TimePicker";
	this.isContainer=false;

	this.hourElement=null;
	this.minuteElement=null;
	this.secondElement=null;
	this.amPmElement=null;

	this.hourUpElement=null;
	this.hourDownElement=null;
	this.minuteUpElement=null;
	this.minuteDownElement=null;
	this.secondUpElement=null;
	this.secondDownElement=null;
	this.amPmUpElement=null;
	this.amPmDownElement=null;

	//	values
	this.is24HourClock=false;
	this.isPm=false;
	this.date=null;

	//	options
	this.displayHours=true;
	this.displayMinutes=true;
	this.displaySeconds=false;

	this.adjustHoursBy=1;
	this.adjustMinutesBy=15;
	this.adjustSecondsBy=15;
};
dojo.inherits(tapestry.widget.TimePicker, dojo.widget.HtmlWidget);

dojo.lang.extend(tapestry.widget.TimePicker, {
	
	templatePath:null,
	templateCssPath:null,
	imagePath:dojo.uri.dojoUri("../tapestry/widget/templates/images/"),
	
	getValue:function(){
		return this.date;
	},
	
	setValue:function(date){
		if (dojo.lang.isString(date)) { this.date=new Date(date); }
		else { this.date=date; }
		
		this.isPm=this.date.getHours()>=12;
	},
	parseValue:function(str){
		var a=str.match(/\d{1,2}/g);
		var t=str.match(/(am|pm|a|p)/ig);
		if(a){
			var d=new Date();
			var h=a[0];
			var m=a[1]?a[1]:0;
			if(t) t=String(t[0]);
			if(t&&t.charAt(0).toLowerCase()=="p") h=h%12+12;
			d.setHours(h);
			d.setMinutes(m);
			setValue(d);
		}
	},

	adjust:function(){
		//	set up for set value.
		var h=this.date.getHours();
		if(!this.is24HourClock&&this.isPm&&h<12){
			h+=12;
		}
		this.date.setHours(h);
		this.setValue(this.date);

		var h=this.date.getHours();
		var m=""+this.date.getMinutes();
		var s=""+this.date.getSeconds();
		this.isPm=h>=12?true:false;	//	isPm

		if(h==0) h=24;
		if(h>12) h-=12;
		if(m.length==1) m="0"+m;
		if(s.length==1) s="0"+s;

		this.hourElement.value=h;
		this.minuteElement.value=m;
		if (this.secondElement) this.secondElement.value=s;
		if (!this.is24HourClock){
			if(this.amPmElement) this.amPmElement.innerHTML=((this.isPm)?"pm":"am");
		}

	},

	//	event handlers
	adjustHours:function(e){
		if(e.target==this.hourUpElement){
			this.date.setHours(this.date.getHours()+parseInt(this.adjustHoursBy));
		}
		if(e.target==this.hourDownElement){
			this.date.setHours(this.date.getHours()-parseInt(this.adjustHoursBy));
		}
		if(e.target==this.hourElement){
			this.date.setHours(parseInt(this.hourElement.value));
		}
		this.adjust();
	},
	adjustMinutes:function(e){
		if(e.target==this.minuteUpElement){
			this.date.setMinutes(this.date.getMinutes()+parseInt(this.adjustMinutesBy));
		}
		if(e.target==this.minuteDownElement){
			this.date.setMinutes(this.date.getMinutes()-parseInt(this.adjustMinutesBy));
		}
		if(e.target==this.minuteElement){
			this.date.setMinutes(parseInt(this.minuteElement.value));
		}
		this.adjust();
	},
	adjustSeconds:function(e){
		if(e.target==this.secondUpElement){
			this.date.setSeconds(this.date.getSeconds()+parseInt(this.adjustSecondsBy));
		}
		if(e.target==this.secondDownElement){
			this.date.setSeconds(this.date.getSeconds()-parseInt(this.adjustSecondsBy));
		}
		if(e.target==this.minuteElement){
			this.date.setSeconds(parseInt(this.secondElement.value));
		}
		this.adjust();
	},
	adjustAmPm:function(e){
		this.isPm=!this.isPm;
		if(this.date.getHours()>12) this.date.setHours(this.date.getHours()-12);
		this.adjust();
	},

	postCreate:function(args){
		//	make sure any attributes are the right type.
		this.is24HourClock=Boolean(this.is24HourClock);
		if (args.date) { this.date = new Date(args.date); }
		else { this.date = new Date(); }
		this.isPm=Boolean(this.isPm);
		
		//	options
		this.displayHours=Boolean(this.displayHours);
		this.displayMinutes=Boolean(this.displayMinutes);
		this.displaySeconds=Boolean(this.displaySeconds);

		this.adjustHoursBy=parseInt(this.adjustHoursBy);
		this.adjustMinutesBy=parseInt(this.adjustMinutesBy);
		this.adjustSecondsBy=parseInt(this.adjustSecondsBy);
	
		//	from here we build.
		this.hourElement=document.createElement("input");
		this.hourElement.style.width="20px";
		this.hourElement.style.textAlign="right";
		dojo.event.connect(this.hourElement, "onchange", this, "adjustHours");

		this.minuteElement=document.createElement("input");
		this.minuteElement.style.width="20px";
		this.minuteElement.style.textAlign="right";
		dojo.event.connect(this.minuteElement, "onchange", this, "adjustMinutes");
		
		if (this.displaySeconds){
			this.secondElement=document.createElement("input");
			this.secondElement.style.width="20px";
			this.secondElement.style.textAlign="right";
			dojo.event.connect(this.secondElement, "onchange", this, "adjustSeconds");
		}
		
		if (!this.is24HourClock){
			this.amPmElement=document.createElement("span");
			this.amPmElement.style.width="24px";
		}
		
		this.adjust();

		var container=document.createElement("table");
		container.setAttribute("cellpadding","0");
		container.setAttribute("cellspacing","0");
		container.setAttribute("border","0");

		var row=document.createElement("tr");
		container.appendChild(row);

		var cell=document.createElement("td");
		cell.setAttribute("rowspan","2");
		cell.style.padding="0";
		cell.appendChild(this.hourElement);
		row.appendChild(cell);

		cell=document.createElement("td");
		cell.setAttribute("width","12");
		cell.style.padding="0";
		this.hourUpElement=document.createElement("img");
		this.hourUpElement.setAttribute("src", this.imagePath + "domain_up.gif");
		this.hourUpElement.setAttribute("width","10");
		this.hourUpElement.setAttribute("height","10");
		cell.appendChild(this.hourUpElement);
		dojo.event.connect(this.hourUpElement, "onclick", this, "adjustHours");
		row.appendChild(cell);

		cell=document.createElement("td");
		cell.setAttribute("rowspan","2");
		cell.style.padding="0";
		cell.style.paddingLeft="2px";
		cell.appendChild(this.minuteElement);
		row.appendChild(cell);

		cell=document.createElement("td");
		cell.setAttribute("width","12");
		cell.style.padding="0";
		this.minuteUpElement=document.createElement("img");
		this.minuteUpElement.setAttribute("src", this.imagePath + "domain_up.gif");
		this.minuteUpElement.setAttribute("width","10");
		this.minuteUpElement.setAttribute("height","10");
		cell.appendChild(this.minuteUpElement);
		dojo.event.connect(this.minuteUpElement, "onclick", this, "adjustMinutes");
		row.appendChild(cell);

		if (this.displaySeconds){
			cell=document.createElement("td");
			cell.style.padding="0";
			cell.setAttribute("rowspan","2");
			cell.appendChild(this.secondElement);
			cell.style.paddingLeft="2px";
			row.appendChild(cell);

			cell=document.createElement("td");
			cell.setAttribute("width","12");
			cell.style.padding="0";
			this.secondUpElement=document.createElement("img");
			this.secondUpElement.setAttribute("src", this.imagePath + "domain_up.gif");
			this.secondUpElement.setAttribute("width","10");
			this.secondUpElement.setAttribute("height","10");
			cell.appendChild(this.secondUpElement);
			dojo.event.connect(this.secondUpElement, "onclick", this, "adjustSeconds");
			row.appendChild(cell);
		}

		if (!this.is24HourClock){
			cell=document.createElement("td");
			cell.setAttribute("rowspan","2");
			cell.setAttribute("valign","bottom");
			cell.setAttribute("align","right");
			cell.style.padding="0 2px";
			cell.style.border="1px solid #ededde";
			cell.appendChild(this.amPmElement);
			row.appendChild(cell);

			cell=document.createElement("td");
			cell.setAttribute("width","12");
			cell.style.padding="0";
			this.amPmUpElement=document.createElement("img");
			this.amPmUpElement.setAttribute("src", this.imagePath + "domain_up.gif");
			this.amPmUpElement.setAttribute("width","10");
			this.amPmUpElement.setAttribute("height","10");
			cell.appendChild(this.amPmUpElement);
			dojo.event.connect(this.amPmUpElement, "onclick", this, "adjustAmPm");
			row.appendChild(cell);
		}

		row=document.createElement("tr");
		container.appendChild(row);

		cell=document.createElement("td");
		cell.setAttribute("width","12");
		cell.setAttribute("valign","bottom");
		cell.style.padding="0";
		this.hourDownElement=document.createElement("img");
		this.hourDownElement.setAttribute("src", this.imagePath + "domain_down.gif");
		this.hourDownElement.setAttribute("width","10");
		this.hourDownElement.setAttribute("height","10");
		cell.appendChild(this.hourDownElement);
		dojo.event.connect(this.hourDownElement, "onclick", this, "adjustHours");
		row.appendChild(cell);

		cell=document.createElement("td");
		cell.setAttribute("width","12");
		cell.setAttribute("valign","bottom");
		cell.style.padding="0";
		this.minuteDownElement=document.createElement("img");
		this.minuteDownElement.setAttribute("src", this.imagePath + "domain_down.gif");
		this.minuteDownElement.setAttribute("width","10");
		this.minuteDownElement.setAttribute("height","10");
		cell.appendChild(this.minuteDownElement);
		dojo.event.connect(this.minuteDownElement, "onclick", this, "adjustMinutes");
		row.appendChild(cell);

		if (this.displaySeconds){
			cell=document.createElement("td");
			cell.setAttribute("width","12");
			cell.setAttribute("valign","bottom");
			cell.style.padding="0";
			this.secondDownElement=document.createElement("img");
			this.secondDownElement.setAttribute("src", this.imagePath + "domain_down.gif");
			this.secondDownElement.setAttribute("width","10");
			this.secondDownElement.setAttribute("height","10");
			cell.appendChild(this.secondDownElement);
			dojo.event.connect(this.secondDownElement, "onclick", this, "adjustSeconds");
			row.appendChild(cell);
		}

		if (!this.is24HourClock){
			cell=document.createElement("td");
			cell.setAttribute("width","12");
			cell.setAttribute("valign","bottom");
			cell.style.padding="0";
			this.amPmDownElement=document.createElement("img");
			this.amPmDownElement.setAttribute("src", this.imagePath + "domain_down.gif");
			this.amPmDownElement.setAttribute("width","10");
			this.amPmDownElement.setAttribute("height","10");
			cell.appendChild(this.amPmDownElement);
			dojo.event.connect(this.amPmDownElement, "onclick", this, "adjustAmPm");
			row.appendChild(cell);
		}

		this.domNode.appendChild(container);
	}
});
