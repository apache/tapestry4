/*
	Copyright (c) 2004-2005, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.iCalendar");
dojo.require("dojo.text.textDirectory");
dojo.require("dojo.date");
dojo.require("dojo.lang");


dojo.iCalendar.fromText =  function (/* string */text) {
	// summary
	// Parse text of an iCalendar and return an array of iCalendar objects

	var properties = dojo.textDirectoryTokeniser.tokenise(text);
	var calendars = [];

	//dojo.debug("Parsing iCal String");
	for (var i = 0, begun = false; i < properties.length; i++) {
		var prop = properties[i];
		if (!begun) {
			if (prop.name == 'BEGIN' && prop.value == 'VCALENDAR') {
				begun = true;
				var calbody = [];
			}
		} else if (prop.name == 'END' && prop.value == 'VCALENDAR') {
			calendars.push(new dojo.iCalendar.VCalendar(calbody));
			begun = false;
		} else {
			calbody.push(prop);
		}
	}
	return /* array */calendars;
}


dojo.iCalendar.Component = function (/* string */ body ) {
	// summary
	// A component is the basic container of all this stuff. 

	if (!this.name) {
		this.name = "COMPONENT"
	}

	this.properties = [];
	this.components = [];

	if (body) {
		for (var i = 0, context = ''; i < body.length; i++) {
			if (context == '') {
				if (body[i].name == 'BEGIN') {
					context = body[i].value;
					var childprops = [];
					//dojo.debug("Context: " + context);
				} else {
					this.addProperty(new dojo.iCalendar.Property(body[i]));
				}
			} else if (body[i].name == 'END' && body[i].value == context) {
				if (context=="VEVENT") {
					this.addComponent(new dojo.iCalendar.VEvent(childprops));
				} else if (context=="VTIMEZONE") {
					this.addComponent(new dojo.iCalendar.VTimeZone(childprops));
				} else if (context=="VTODO") {
					this.addComponent(new dojo.iCalendar.VTodo(childprops));
				} else if (context=="VJOURNAL") {
					this.addComponent(new dojo.iCalendar.VJournal(childprops));
				} else if (context=="VFREEBUSY") {
					this.addComponent(new dojo.iCalendar.VFreeBusy(childprops));
				} else if (context=="STANDARD") {
					this.addComponent(new dojo.iCalendar.Standard(childprops));
				} else if (context=="DAYLIGHT") {
					this.addComponent(new dojo.iCalendar.Daylight(childprops));
				} else if (context=="VALARM") {
					this.addComponent(new dojo.iCalendar.VAlarm(childprops));
				}else {
					dojo.unimplemented("dojo.iCalendar." + context);
				}
				context = '';
			} else {
				childprops.push(body[i]);
			}
		}

		if (this._ValidProperties) {
			this.postCreate();
		}
	}
}

dojo.lang.extend(dojo.iCalendar.Component, {

	addProperty: function (prop) {
		// summary
		// push a new property onto a component.
		this.properties.push(prop);
		this[prop.name.toLowerCase()] = prop;
	},

	addComponent: function (prop) {
		// summary
		// add a component to this components list of children.
		this.components.push(prop);
	},

	postCreate: function() {
		for (var x=0; x<this._ValidProperties.length; x++) {
			var evtProperty = this._ValidProperties[x];
			var found = false;
	
			for (var y=0; y<this.properties.length; y++) {	
				var prop = this.properties[y];
				propName = prop.name.toLowerCase();
				if (dojo.lang.isArray(evtProperty)) {

					var alreadySet = false;
					for (var z=0; z<evtProperty.length; z++) {
						var evtPropertyName = evtProperty[z].name.toLowerCase();
						if((this[evtPropertyName])  && (evtPropertyName != propName )) {
							alreadySet=true;
						} 
					}
					if (!alreadySet) {
						this[propName] = prop;
					}
				} else {
					if (propName == evtProperty.name.toLowerCase()) {
						found = true;
						if (evtProperty.occurance == 1){
							this[propName] = prop;
						} else {
							found = true;
							if (!dojo.lang.isArray(this[propName])) {
							 	this[propName] = [];
							}
							this[propName].push(prop);
						}
					}
				}
			}

			if (evtProperty.required && !found) {	
				dojo.debug("iCalendar - " + this.name + ": Required Property not found: " + evtProperty.name);
			}
		}

		// parse any rrules		
		if (dojo.lang.isArray(this.rrule)) {
			for(var x=0; x<this.rrule.length; x++) {
				var rule = this.rrule[x].value;

				//add a place to cache dates we have checked for recurrance
				this.rrule[x].cache = function() {};

				var temp = rule.split(";");
				for (var y=0; y<temp.length; y++) {
					var pair = temp[y].split("=");
					var key = pair[0].toLowerCase();
					var val = pair[1];

					var unavailable = ["secondly","minutely","hourly","bysecond","byminute","byhour","count","bysetpos"];
					var intervalTypes = ["day","weekly","monthly","yearly"];
					var byTypes = ["byday","bymonthday","byweekno","bymonth","byyearday"];
					this.rrule[x].intervals = []
					this.rrule[x].bys = [];

					if (!dojo.lang.inArray(unavailable, key)) { 
						if (dojo.lang.inArray(intervalTypes,key)) {
							this.rrule[x].intervals.push(pair);
						} else if (dojo.lang.inArray(byTypes,key)) {
							this.rrule[x].bys.push(pair);
						} else {
							this.rrule[x][key] = val;
						}
					}
				}	
			}
			this.recurring = true;
		}

	}, 

	toString: function () {
		// summary
		// output a string representation of this component.
		return "[iCalendar.Component; " + this.name + ", " + this.properties.length +
			" properties, " + this.components.length + " components]";
	}
});

dojo.iCalendar.Property = function (prop) {
	// summary
	// A single property of a component.

	// unpack the values
	this.name = prop.name;
	this.group = prop.group;
	this.params = prop.params;
	this.value = prop.value;

}

dojo.lang.extend(dojo.iCalendar.Property, {
	toString: function () {	
		// summary
		// output a string reprensentation of this component.
		return "[iCalenday.Property; " + this.name + ": " + this.value + "]";
	}
});

// This is just a little helper function for the Component Properties
var _P = function (n, oc, req) {
	return {name: n, required: (req) ? true : false,
		occurance: (oc == '*' || !oc) ? -1 : oc}
}

/*
 * VCALENDAR
 */

dojo.iCalendar.VCalendar = function (/* string */ calbody) {
	// summary
	// VCALENDAR Component

	this.name = "VCALENDAR";
	this.recurringEvents = [];
	dojo.iCalendar.Component.call(this, calbody);
}

dojo.inherits(dojo.iCalendar.VCalendar, dojo.iCalendar.Component);

dojo.lang.extend(dojo.iCalendar.VCalendar, {

	addComponent: function (prop) {
		// summary
		// add component to the calenadar that makes it easy to pull them out again later.
		this.components.push(prop);
		if (prop.name.toLowerCase() == "vevent") {
			if (prop.rrule) {
				this.recurringEvents.push(prop);
			} else {
				startDate = prop.getDate();
				month = startDate.getMonth() + 1;
				dateString = startDate.getFullYear() + "-" + month + "-" + startDate.getDate();

				if (!dojo.lang.isArray(this[dateString])) {
					this[dateString] = [];
				}
				this[dateString].push(prop);
			}
		}
	},

	getEvents: function(/* Date */ date) {
		// summary
		// Gets all events occuring on a particular date
		var events = [];

		month = date.getMonth() + 1;
		var dateStr= date.getFullYear() + "-" + month + "-" + date.getDate();

		if (dojo.lang.isArray(this[dateStr])) {
			var tmp = events.concat(this[dateStr]);
			events = tmp;
		} 

		for(var x=0; x<this.recurringEvents.length; x++) {
			if (this.recurringEvents[x].fallsOn(date)) {
				events.push(this.recurringEvents[x]);
			}
		}
	
		if (events.length > 0) {
			return events;
		} 

		return null;			
	}
});

/*
 * STANDARD
 */

var StandardProperties = [
	_P("dtstart", 1, true), _P("tzoffsetto", 1, true), _P("tzoffsetfrom", 1, true),
	_P("comment"), _P("rdate"), _P("rrule"), _P("tzname")
];


dojo.iCalendar.Standard = function (/* string */ body) {
	// summary
	// STANDARD Component

	this.name = "STANDARD";
	this._ValidProperties = StandardProperties;
	dojo.iCalendar.Component.call(this, body);
}

dojo.inherits(dojo.iCalendar.Standard, dojo.iCalendar.Component);

/*
 * DAYLIGHT
 */

var DaylightProperties = [
	_P("dtstart", 1, true), _P("tzoffsetto", 1, true), _P("tzoffsetfrom", 1, true),
	_P("comment"), _P("rdate"), _P("rrule"), _P("tzname")
];

dojo.iCalendar.Daylight = function (/* string */ body) {
	// summary
	// Daylight Component
	this.name = "DAYLIGHT";
	this._ValidProperties = DaylightProperties;
	dojo.iCalendar.Component.call(this, body);
}

dojo.inherits(dojo.iCalendar.Daylight, dojo.iCalendar.Component);

/*
 * VEVENT
 */

var VEventProperties = [
	// these can occur once only
	_P("class", 1), _P("created", 1), _P("description", 1), _P("dtstart", 1),
	_P("geo", 1), _P("last-mod", 1), _P("location", 1), _P("organizer", 1),
	_P("priority", 1), _P("dtstamp", 1), _P("seq", 1), _P("status", 1),
	_P("summary", 1), _P("transp", 1), _P("uid", 1), _P("url", 1), _P("recurid", 1),
	// these two are exclusive
	[_P("dtend", 1), _P("duration", 1)],
	// these can occur many times over
	_P("attach"), _P("attendee"), _P("categories"), _P("comment"), _P("contact"),
	_P("exdate"), _P("exrule"), _P("rstatus"), _P("related"), _P("resources"),
	_P("rdate"), _P("rrule")
];

dojo.iCalendar.VEvent = function (/* string */ body) {
	// summary 
	// VEVENT Component
	this._ValidProperties = VEventProperties;
	this.name = "VEVENT";
	dojo.iCalendar.Component.call(this, body);
	this.recurring = false;
	this.startDate = dojo.date.fromIso8601(this.dtstart.value);
}

dojo.inherits(dojo.iCalendar.VEvent, dojo.iCalendar.Component);

dojo.lang.extend(dojo.iCalendar.VEvent, {
		fallsOn: function(startingDate){
			// summary
			// checks to see if any occurance of this (if its recurring) event falls on date
			this.startDate = this.getDate();
			var date = new Date(startingDate);
			date.setHours(this.startDate.getHours());
			date.setMinutes(this.startDate.getMinutes());
			date.setSeconds(this.startDate.getSeconds());
			dateString = date.getMonth() + "-" + date.getDate() + "-" + date.getFullYear();

			var rrule = this.rrule[0];

			if (rrule.cache[dateString]) {
				return true;
			}

			if (date < this.startDate) {
				return false;
			}

			if (this.dtend) {
				if (date < dojo.date.fromIso8601(this.dtend.value)) {
					rrule.cache[dateString] = true;				
					return true;
				}
			}
			// TODO: add one for duration here same as above

			// find miniumum unit
			var order = function() {};
			order["daily"] = 1;
			order["weekly"] = 2;
			order["monthly"] = 3;
			order["yearly"] = 4;
			order["byday"] = 1;
			order["bymonthday"] = 1;
			order["byweekno"] = 2;
			order["bymonth"] = 3;
			order["byyearday"] = 4;


			var minimumUnit = order[rrule.freq.toLowerCase()];
	
			for (var x=0; x< rrule.bys.length; x++) {
				var tmp = rrule.bys[x];
				var by = tmp.toString().split(",");
				var name = by[0].toLowerCase();
				if (order[name] < minimumUnit) { 
					minimumUnit = order[name]; 
				}	
			}

			candidateStartDate = new Date(date);
 			//weekly
			if (minimumUnit==2) {
				if (candidateStartDate.getDay()>this.startDate.getDay()) {
					while (candidateStartDate.getDay()>this.startDate.getDay()) {
						candidateStartDate.setDate(candidateStartDate.getDate()-1);
					}
				} else {
					while (candidateStartDate.getDay()<this.startDate.getDay()) {
						candidateStartDate.setDate(candidateStartDate.getDate()+1);
					}
				}
			} else if (minimumUnit == 3) { 
					if (candidateStartDate.getDate() > dojo.date.getDaysInMonth(this.startDate)) {
							candidateStartDate.setDate(dojo.date.getDaysInMonth(this.startDate));
					} else {
						if (candidateStartDate.getDate() > this.startDate.getDate()) {
							while(candidateStartDate.getDate() > this.startDate.getDate()) {
								candidateStartDate.setDate(candidateStartDate.getDate()-1);
							}
						}else {
							while (candidateStartDate.getDate()<this.startDate.getDate()) {
								candidateStartDate.setDate(candidateStartDate.getDate()+1);
							}
						}
					}
			} else if (minimumUnit == 4) { // yearly
					if (candidateStartDate.getMonth() > this.startDate.getMonth()) {
						while(candidateStartDate.getMonth() > this.startDate.getMonth()){
							candidateStartDate.setMonth(candidateStartDate.getMonth()-1);
						}
					} else {
						while(candidateStartDate.getMonth() < this.startDate.getMonth()){
							candidateStartDate.setMonth(candidateStartDate.getMonth()+1);
						}
					}

					if (candidateStartDate.getDate() != this.startDate.getDate()) {
						if (candidateStartDate.getDate() > dojo.date.getDaysInMonth(this.startDate)) {
								candidateStartDate.setDate(dojo.date.getDaysInMonth(this.startDate));
						} else {
							if (candidateStartDate.getDate() > this.startDate.getDate()) {
								while(candidateStartDate.getDate() > this.startDate.getDate()) {
									candidateStartDate.setDate(candidateStartDate.getDate()-1);
								}
							}else {
	
								while (candidateStartDate.getDate()<this.startDate.getDate()) {
									candidateStartDate.setDate(candidateStartDate.getDate()+1);
								}
							}
						}
					}
			} 

			if (!((candidateStartDate.getMonth() == date.getMonth()) &&
				 (candidateStartDate.getDate() == date.getDate()) &&
				 (candidateStartDate.getFullYear() == date.getFullYear()))) {
					return false;
			}


			if (rrule.until) {
				if (candidateStartDate>dojo.date.fromIso8601(rrule.until) ) {
					return false;
				}
			}

			var freq = rrule.freq.toLowerCase();     
			if (freq=="daily") {
				if (candidateStartDate.getFullYear() == this.startDate.getFullYear()) {
					var diff = dojo.date.getDayOfYear(candidateStartDate) - dojo.date.getDayOfYear(this.startDate)
				} else {
					var beginning = 365 - dojo.date.getDayOfYear(this.startDate);
					if ((candidateStartDate.getFullYear() - this.startDate.getFullYear()) > 1) {
						var diff = beginning + dojo.date.getDayOfYear(candidateStartDate) + ((candiDateStartDate.getFullYear() - this.startDate.getFullYear())*365);
					} else {
						var diff = beginning + dojo.date.getDayOfYear(candidateStartDate);
					}
				}
			} else if (freq == "weekly")	{
				if (candidateStartDate.getFullYear() == this.startDate.getFullYear()) {
					var diff = (dojo.date.getDayOfYear(candidateStartDate) - dojo.date.getDayOfYear(this.startDate))/7;
				} else {
					var beginning = 365 - dojo.date.getDayOfYear(this.startDate);
					if ((candidateStartDate.getFullYear() - this.startDate.getFullYear()) > 1) {
						var diff = (beginning + dojo.date.getDayOfYear(candidateStartDate) + ((candiDateStartDate.getFullYear() - this.startDate.getFullYear())*365))/7;
					} else {
						var diff = (beginning + dojo.date.getDayOfYear(candidateStartDate))/7;
					}
				}
				//get weeks between set as diff
			} else if (freq == "monthly")	{
				if (candidateStartDate.getFullYear() == this.startDate.getFullYear()) {
						var diff = (candidateStartDate.getMonth() - this.startDate.getMonth());
				} else {
					var beginning = 12 - this.startDate.getMonth();
					if ((candidateStartDate.getFullYear() - this.startDate.getFullYear()) > 1) {
						var diff = (beginning + candidateStartDate.getMonth()) + ((candidateStartDate.getFullYear() - this.startDate.getFullYear())*12);
					} else {
						var diff = (beginning + candidateStartDate.getMonth());
					}	
				}
				//get months between set as diff
			} else if (freq == "yearly") {
				//get years between set as diff
				var diff = candidateStartDate.getFullYear() - this.startDate.getFullYear();
		   }	

	
			if (rrule.interval) {
				if ((diff % rrule.interval) != 0 ) {
					return false;
				}
			}
			
			var weekdays=["SU","MO","TU","WE","TH","FR","SA"];
			for (var x=0; x< rrule.bys.length; x++) {
				var tmp = rrule.bys[x].toString();
				var by= tmp.split(",");
				var name = by[0].toLowerCase();
			   var found=false;
				for (var x=1; x<by.length; x++) {
					if (name == "byday") {
						if (weekdays[candidateStartDate.getDay()] == by[x]) {

							rrule.cache[dateString] = true;				
							return true;
						}
					} else if (name == "bymonthday") {
						if (by[x] > dojo.date.getDaysInMonth(candidateStartDate)) {
							var tmp = dojo.date.getDaysInMonth(candidateStartDate);
						} else {
							var tmp = by[x];
						}
						if (tmp == candidateStartDate.getDate()) {

							rrule.cache[dateString] = true;				
							return true;
						}
					} else if (name == "bymonth") {
						if (by[x] == candidateStartDate.getMonth() + 1) {
							rrule.cache[dateString] = true;				
							return true;
						}
					}
				}
				if (!found) { return false; }
			}
	
			rrule.cache[dateString] = true;				
			return true;
		},

		getDate: function() {
			return dojo.date.fromIso8601(this.dtstart.value);
		}
});

/*
 * VTIMEZONE
 */

var VTimeZoneProperties = [
	_P("tzid", 1, true), _P("last-mod", 1), _P("tzurl", 1)

	// one of 'standardc' or 'daylightc' must occur
	// and each may occur more than once.
];

dojo.iCalendar.VTimeZone = function (/* string */ body) {
	// summary
	// VTIMEZONE Component
	this.name = "VTIMEZONE";
	this._ValidProperties = VTimeZoneProperties;
	dojo.iCalendar.Component.call(this, body);
}

dojo.inherits(dojo.iCalendar.VTimeZone, dojo.iCalendar.Component);

/*
 * VTODO
 */

var VTodoProperties = [
	// these can occur once only
	_P("class", 1), _P("completed", 1), _P("created", 1), _P("description", 1),
	_P("dtstart", 1), _P("geo", 1), _P("last-mod", 1), _P("location", 1),
	_P("organizer", 1), _P("percent", 1), _P("priority", 1), _P("dtstamp", 1),
	_P("seq", 1), _P("status", 1), _P("summary", 1), _P("uid", 1), _P("url", 1),
	_P("recurid", 1),
	// these two are exclusive
	[_P("due", 1), _P("duration", 1)],
	// these can occur many times over
	_P("attach"), _P("attendee"), _P("categories"), _P("comment"), _P("contact"),
	_P("exdate"), _P("exrule"), _P("rstatus"), _P("related"), _P("resources"),
	_P("rdate"), _P("rrule")
];

dojo.iCalendar.VTodo= function (/* string */ body) {
	// summary
	// VTODO Componenet
	this.name = "VTODO";
	this._ValidProperties = VTodoProperties;
	dojo.iCalendar.Component.call(this, body);
}

dojo.inherits(dojo.iCalendar.VTodo, dojo.iCalendar.Component);

/*
 * VJOURNAL
 */

var VJournalProperties = [
	// these can occur once only
	_P("class", 1), _P("created", 1), _P("description", 1), _P("dtstart", 1),
	_P("last-mod", 1), _P("organizer", 1), _P("dtstamp", 1), _P("seq", 1),
	_P("status", 1), _P("summary", 1), _P("uid", 1), _P("url", 1), _P("recurid", 1),
	// these can occur many times over
	_P("attach"), _P("attendee"), _P("categories"), _P("comment"), _P("contact"),
	_P("exdate"), _P("exrule"), _P("related"), _P("rstatus"), _P("rdate"), _P("rrule")
];

dojo.iCalendar.VJournal= function (/* string */ body) {
	// summary
	// VJOURNAL Component
	this.name = "VJOURNAL";
	this._ValidProperties = VJournalProperties;
	dojo.iCalendar.Component.call(this, body);
}

dojo.inherits(dojo.iCalendar.VJournal, dojo.iCalendar.Component);

/*
 * VFREEBUSY
 */

var VFreeBusyProperties = [
	// these can occur once only
	_P("contact"), _P("dtstart", 1), _P("dtend"), _P("duration"),
	_P("organizer", 1), _P("dtstamp", 1), _P("uid", 1), _P("url", 1),
	// these can occur many times over
	_P("attendee"), _P("comment"), _P("freebusy"), _P("rstatus")
];

dojo.iCalendar.VFreeBusy= function (/* string */ body) {
	// summary
	// VFREEBUSY Component
	this.name = "VFREEBUSY";
	this._ValidProperties = VFreeBusyProperties;
	dojo.iCalendar.Component.call(this, body);
}

dojo.inherits(dojo.iCalendar.VFreeBusy, dojo.iCalendar.Component);

/*
 * VALARM
 */

var VAlarmProperties = [
	[_P("action", 1, true), _P("trigger", 1, true), [_P("duration", 1), _P("repeat", 1)],
	_P("attach", 1)],

	[_P("action", 1, true), _P("description", 1, true), _P("trigger", 1, true),
	[_P("duration", 1), _P("repeat", 1)]],

	[_P("action", 1, true), _P("description", 1, true), _P("trigger", 1, true),
	_P("summary", 1, true), _P("attendee", "*", true),
	[_P("duration", 1), _P("repeat", 1)],
	_P("attach", 1)],

	[_P("action", 1, true), _P("attach", 1, true), _P("trigger", 1, true),
	[_P("duration", 1), _P("repeat", 1)],
	_P("description", 1)],
];

dojo.iCalendar.VAlarm= function (/* string */ body) {
	// summary
	// VALARM Component
	this.name = "VALARM";
	this._ValidProperties = VAlarmProperties;
	dojo.iCalendar.Component.call(this, body);
}

dojo.inherits(dojo.iCalendar.VAlarm, dojo.iCalendar.Component);

