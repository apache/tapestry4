/* 
 *  Pop Up Calendar 
 *  By Paul Geerts
 *  11 Oct 2002
 *
 *  Designed as a replacement for current Tapestry DatePicker control
 *  to work on both IE5+ and Mozilla based browsers
*/

// Reference Data arrays
var months = new Array("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");
var days = new Array("S", "M", "T", "W", "T", "F", "S");
var monthLengths =new Array(31,28,31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
var monthLongNames = new Array('January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December');
var dayNames = new Array('Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday','Friday', 'Saturday');


// is the calendar created?
var calDiv = null;

// is the calendar showing?
var showing = false;

var pickArea = null;

// function to create the calendar if necessary and pop it up
// the button is the "V" button that triggers the popup event
function goCalendar(button) {
    if (showing) {   // if we're currently showing, stop it
        closeCalendar();
    }

    format = button.previousSibling;      // get the hidden text field containng the desired date format 
    val = format.previousSibling;         // get the date as milliseconds since 1970 
    disp = val.previousSibling;           // get the display field

    currentDate = new Date();             // create a new date to hold the
    if (val.value != "") {
        currentDate.setTime(val.value);   // start value 
        oldDate = currentDate;            // Rememeber the old date so we can show it as blue
    } else {
        oldDate = null;
    }

    var point = getPoint(disp);

    if (!calDiv) {                        // if we haven't create the calendar, go to
        calDiv = document.createElement("DIV");
        calDiv.style.backgroundColor = "#cccccc";
        calDiv.style.border = "2px outset";
        calDiv.style.position = "absolute";
        calDiv.style.top = point.y + disp.offsetHeight + 1;
        calDiv.style.left = point.x;
        calDiv.style.fontFamily = "sans-serif";
        calDiv.appendChild(createCalendar(currentDate));
        document.body.appendChild(calDiv);
    } else {                             // otherwise, just move and unhide it
        calDiv.style.display = "block";
        calDiv.style.top = point.y + disp.offsetHeight + 1;
        calDiv.style.left = point.x;
        updatePickArea();               // update the selection area
    }

    showing = true;  // we are now showing

    // this needs to be done outsite a click event for some reason
    setTimeout(addBodyClick, 1);

}

function addBodyClick() {   // this adds the handler which hides the 
                            // picker when something else is clicked
    addEvent("click", document.body, closeCalendar);
}

function createCalendar(date) {   // create calendar using W3C DOM methods

    var table = document.createElement("TABLE");
    var tbody = document.createElement("TBODY");
    table.appendChild(tbody);
    // create head row with dropdowns
    var headtr = document.createElement("TR");
    tbody.appendChild(headtr);
    headtr.appendChild(getHeadTD(currentDate));

    // create body TR with picker
    var bodytr = document.createElement("TR");
    tbody.appendChild(bodytr);
    pickArea = createPickArea();
    updatePickArea();
    bodytr.appendChild(pickArea);

    // create footer TR with "Today" link
    var foottr = document.createElement("TR");
    foottd = document.createElement("TD");
    foottr.appendChild(foottd);
    foottd.align = "middle";
    var today = new Date();
    foottd.innerHTML = "Today : " + months[today.getMonth()] + " " + today.getDate()  + ", " + today.getFullYear();
    foottd.date = new Date();
    foottd.align="center";
    foottd.style.fontSize="8pt";
    setCursor(foottd);
    foottd.style.backgroundColor="#aaaaaa";

    // add events using the approprate method for each browser
    addEvent("mouseover", foottd, tdMouseOver);
    addEvent("mouseout", foottd, tdMouseOut);
    addEvent("click", foottd, tdClick);
    tbody.appendChild(foottr);


    return table;

}

function getHeadTD(currentDate) {  // create dropdown TD 
    var td = document.createElement("TD");
    td.align = "center";
    td.valign = "top";

    monthSelect = getMonthSelect(currentDate);  // remember these so we can update them later
    yearSelect = getYearSelect(currentDate);
    td.appendChild(monthSelect);
    td.appendChild(yearSelect);

    
    return td;
}

function getMonthSelect(currentDate) {  // return a select with a list of months

    var sel = document.createElement("SELECT");
    for (var i = 0 ; i < months.length ; i++) {
        var opt = document.createElement("OPTION");
        opt.innerHTML = monthLongNames[i];
        opt.value = i;
        if (i == currentDate.getMonth()) {
            opt.selected = true;
        }
        sel.appendChild(opt);
    }
    
    // add events
    addEvent("change", sel, changeMonth);   

    // the doNuffin event is to cancel the bubble
    // without it, the calendar would disappear
    // when you clicked on the dropdpwn
    addEvent("click", sel, doNuffin);  
                                       
    return sel;
}

function getYearSelect(currentDate) {   // generate a list
                                        // of years
    var sel = document.createElement("SELECT");
    var year = currentDate.getFullYear();
    for (var i = 1920; i < 2021; i++) {
        var opt = document.createElement("OPTION");
        opt.innerHTML = i;
        opt.value = i;
        if (i == year) {
            opt.selected = true;
        }
        sel.appendChild(opt);
    }

    addEvent("change", sel, changeYear);
    addEvent("click", sel, doNuffin);
    return sel;
}

function createPickArea() {    // create the area where the calendar actually sits
    var bigtd = document.createElement("TD");
    var table = document.createElement("TABLE");
    var tbody = document.createElement("TBODY");
    bigtd.appendChild(table);
    table.appendChild(tbody);
    table.style.width="100%";
    table.cellSpacing = 0;
    table.style.backgroundColor="#ffffff";
    var trhead = document.createElement("TR");
    
    // create the header labels
    for (var i = 0 ; i < 7 ; i ++) {
        var td = document.createElement("TD");
        td.innerHTML = days[i];
        td.align="center";
        td.style.fontSize="8pt";
        td.style.color = "#ffffff";
        td.style.backgroundColor = "#777777";   
        trhead.appendChild(td);
    }
    tbody.appendChild(trhead);
    // create the grid for the 
    // date picking area
    for (var i = 0 ; i < 6 ; i ++) {
        var tr = document.createElement("TR");
        for (var j = 0 ; j < 7 ; j ++) {
            var td = document.createElement("TD");
            td.style.fontSize="8pt";
            setCursor(td);
            td.innerHTML = i*7+j;
            td.align="center";
            // add events for highlighting and clicking
            addEvent("mouseover", td, tdMouseOver);
            addEvent("mouseout", td, tdMouseOut);
            addEvent("click", td, tdClick);
            tr.appendChild(td);
        }
        tbody.appendChild(tr);
    }

    bigtd.tbody = tbody;
    return bigtd;
}


function updatePickArea() {  // this changes the pick area to reflect the current date

    monthSelect.value = currentDate.getMonth();
    yearSelect.value = currentDate.getFullYear();
    var first = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);
    var dayNumber = new Array();
    var monthOffset = new Array();
    var currdn = 0;

    // work out how what days from the previous month should be displayed
    for (var i = 0 ; i < first.getDay() ; i++) {
        var back = new Date();
        var daysback = first.getDay() - i;
        back.setTime(first - daysback * 1000 * 60 * 60 * 24);
        dayNumber[currdn] = back.getDate();
        monthOffset[currdn] = -1;
        currdn++;
    }

    // work out how what days from the current month should be displayed
    for (var i = 1 ; i <= monthLengths[first.getMonth()] ; i++) {
        dayNumber[currdn] = i;
        monthOffset[currdn] = 0;
        currdn++;
    }

    // work out how what days from the next month should be displayed
    var nm = 1;
    while (currdn < 42) {
        dayNumber[currdn] = nm++;
        monthOffset[currdn] = 1;
        currdn++;
    }
    
    // update the cells to the correct values
    var c = 0;
    for (var i = 1 ; i <= 6 ; i++) {
        row = pickArea.tbody.childNodes[i];
        for (var j = 0 ; j < 7 ; j++) {
            col = row.childNodes[j];
            col.innerHTML = dayNumber[c];
            // store the date for this cell as an ad-hoc property of the cell
            // using the month-offset like this could lead to having negative
            // months (months start at zero for some reason) 
            // but the JS Date object works everything out fine
            col.date = new Date(currentDate.getFullYear(),
                                            currentDate.getMonth() + monthOffset[c],
                                            dayNumber[c]);

            // format the cell colour
            col.style.backgroundColor = "#ffffff";
            if (monthOffset[c]!=0) {
                col.style.color = "#aaaaaa";
            } else {
                col.style.color = "#000000";
            }

            // blue if current date
            if (oldDate != null && 
                col.date.getFullYear() == oldDate.getFullYear() &&
                col.date.getMonth() == oldDate.getMonth() && 
                col.date.getDate() == oldDate.getDate()) {
                col.style.color = "#0000FF";
            }            
            c++;
                                
        }
    }    
}


function changeMonth(e) {    // event handler for changing month
    var sel = getEventObject(e);
    currentDate = new Date(currentDate.getFullYear(), sel.value, currentDate.getDate());
    updatePickArea();

}

function changeYear(e) {  // event handler for changing year
    var sel = getEventObject(e);
    currentDate = new Date(sel.value,currentDate.getMonth(), currentDate.getDate());
    updatePickArea();

}

function doNuffin(e) {  // cancels bubble, see comment on line 140
    e.cancelBubble = true;
}

// highlite the cell
function tdMouseOver(e) {
    var td = getEventObject(e);
    td.oldBackground = td.style.backgroundColor;
    td.style.backgroundColor = "yellow";
}

// put it back the way it was
function tdMouseOut(e) {
    var td = getEventObject(e);
    td.style.backgroundColor = td.oldBackground;
}

// select a date by clicking
function tdClick(e) {
    tdMouseOut(e);   // ensure bg colour is reset, because we are reusing the date picker
    var td = getEventObject(e);
    setDate(td.date);
}

function setDate(date) {   // update the date display and hidden value
    var dstring = formatDate(date, format.value);
    
    disp.value = dstring;
    val.value = date.getTime();
    closeCalendar();
}

function closeCalendar() {   // put the calendar away
    deleteEvent("click", document.body, closeCalendar);  // remove the event
    calDiv.style.display = "none";
    showing = false;
}

function getEventObject(e) {  // utility function to retrieve object from event
    if (navigator.appName == "Microsoft Internet Explorer") {
        return e.srcElement;
    } else {  // is mozilla/netscape
        // need to crawl up the tree to get the first "real" element
        // i.e. a tag, not raw text
        var o = e.target;
        while (!o.tagName) {
            o = o.parentNode;
        }
        return o;
    }
}

function pad(number,X) {   // utility function to pad a number to a given width
	X = (!X ? 2 : X);
	number = ""+number;
	while (number.length < X) {
	    number = "0" + number;
	}
	return number;
}

function formatDate(date, format) {  // formats the date based on the date format specified
    var bits = new Array();
    // work out what each bit should be
    bits['d'] = date.getDate();
    bits['dd'] = pad(date.getDate(),2);
    bits['dddd'] = dayNames[date.getDay()];

    bits['M'] = date.getMonth()+1;
    bits['MM'] = pad(date.getMonth()+1,2);
    bits['MMM'] = months[date.getMonth()];
    bits['MMMM'] = monthLongNames[date.getMonth()];
    
    var yearStr = "" + date.getFullYear();
    yearStr = (yearStr.length == 2) ? '19' + yearStr: yearStr;
    bits['yyyy'] = yearStr;
    bits['yy'] = bits['yyyy'].toString().substr(2,2);

    // do some funky regexs to replace the format string
    // with the real values
    var frm = new String(format);
    var sect;
    for (sect in bits) {
      frm = eval("frm.replace(/\\b" + sect + "\\b/,'" + bits[sect] + "');");
    }

    return frm;
}


function addEvent(name, obj, funct) { // utility function to add event handlers

    if (navigator.appName == "Microsoft Internet Explorer") {
        obj.attachEvent("on"+name, funct);
    } else {  // is mozilla/netscape
        obj.addEventListener(name, funct, false);
    }
}


function deleteEvent(name, obj, funct) { // utility function to delete event handlers

    if (navigator.appName == "Microsoft Internet Explorer") {
        obj.detachEvent("on"+name, funct);
    } else {  // is mozilla/netscape
        obj.removeEventListener(name, funct, false);
    }
}

function setCursor(obj) {
   if (navigator.appName == "Microsoft Internet Explorer") {
        obj.style.cursor = "hand";
    } else {  // is mozilla/netscape
        obj.style.cursor = "pointer";
    }
}

// Point x, y class
function Point(iX, iY)
{
   this.x = iX;
   this.y = iY;
}

// Get the Point of the given tag
function getPoint(aTag)
{
   var oTmp = aTag;  
   var point = new Point(0,0);
  
   do 
   {
      point.x += oTmp.offsetLeft;
      point.y += oTmp.offsetTop;
      oTmp = oTmp.offsetParent;
   } 
   while (oTmp.tagName != "BODY");

   return point;
}
