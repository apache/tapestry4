//****************************************************************************
// PopCalendar 4.1, Emailware(please mail&commend me if u like it)
// Originally coded by Liming(Victor) Weng, email: victorwon@netease.com
// Release date: 2000.5.9
// Anyone may modify it to satify his needs, but please leave this comment ahead.
//****************************************************************************

//****************************************************************************
// Extensively modified by Paul Geerts and Malcolm Edgar
// Date: August-Sept 2002
//****************************************************************************

// Global variables

// Date display text field
var gDateField = new Object();

// Hidden date field
var gHiddenField;

// Date display format
var gFormat;

// Popup calendar
var gVicPopCal = null;

// Current Date
var gdCurDate = new Date();

var giYear = gdCurDate.getFullYear();
var giMonth = gdCurDate.getMonth()+1;
var giDay = gdCurDate.getDate();

// Selected date
var gdSelectedDate = null;

// Constants

var MONTH_SHORT_NAMES = new Array('Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec');
var MONTH_LONG_NAMES = new Array('January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December');
var DAY_NAMES = new Array('Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday','Friday', 'Saturday');

// Colors
var gcOtherMonth = "#808080";
var gcToggle = "#ffff00";
var gcTodayRollover = "blue";
var gcBG = "#cccccc";
var gcTableHeader = "white";
var gcTableHeaderBG = "#808080";
var gcTableCell = "black";
var gcTableCellBG = "white";

/**
 * This function displays the popup Calendar control.
 * E.g.
 * <input type="text" name="dc" style="text-align:center" readonly>
 * <input type="button" value="V" onclick="showPopCalendar(dc,dc,popCal);return false">
 *
 * param dateField is the widget into which you want to put the selected date,
 * param dateHidden is the date hidden field used to set the initial date
 * param popCal is the widget to display the calendar
 **/
function showPopCalendar(dateField, dateHidden, popCal) 
{
  if (gVicPopCal) 
  {
     hidePopCalendar();
     return;
  }      
  gDateField = dateField;
  gHiddenField = dateHidden;
  gVicPopCal = popCal;
  
  gFormat = gDateField.format;

  var d = new Date();
  if (gHiddenField.value != "") {
    d.setTime(gHiddenField.value);
    gdSelectedDate = d;
  } else {
    gdSelectedDate = null;
  }

  fSetYearMon(d.getYear(), d.getMonth() + 1);

  var point = fGetXY(gDateField);
  with (gVicPopCal.style) {
    left = point.x;
    top  = point.y + gDateField.offsetHeight + 1;
    visibility = 'visible';
  }

  window.event.cancelBubble = true;
  document.body.onclick = hidePopCalendar;
  gVicPopCal.focus();
}

/**
 * Hide the popup calendar
 **/
function hidePopCalendar() 
{
  if (gVicPopCal) 
  {
     gVicPopCal.style.visibility = "hidden";
     gVicPopCal = null;
  } 
}

/**
 * Set displayed date and hide the popup calendar
 **/
function setDate(iYear, iMonth, iDay)
{
  var d = new Date();
  d.setYear(iYear);
  d.setMonth(iMonth - 1);
  d.setDate(iDay);

  gdSelectedDate = d;
  gDateField.value = formatDate(d);
  gHiddenField.value = d.getTime();

  hidePopCalendar();
}

function pad(number,X) 
{
	X = (!X ? 2 : X);
	number = ""+number;
	while (number.length < X) {
	    number = "0" + number;
	}
	return number;
}

function formatDate(date) 
{
    var bits = new Array();
    bits['d'] = date.getDate();
    bits['dd'] = pad(date.getDate(),2);
    bits['dddd'] = DAY_NAMES[date.getDay()];

    bits['M'] = date.getMonth()+1;
    bits['MM'] = pad(date.getMonth()+1,2);
    bits['MMM'] = MONTH_SHORT_NAMES[date.getMonth()];
    bits['MMMM'] = MONTH_LONG_NAMES[date.getMonth()];
    
    var yearStr = "" + date.getYear();
    yearStr = (yearStr.length == 2) ? '19' + yearStr: yearStr;
    bits['yyyy'] = yearStr;
    bits['yy'] = bits['yyyy'].toString().substr(2,2);

    var frm = new String(gFormat);
    var sect;
    for (sect in bits) {
      frm = eval("frm.replace(/\\b" + sect + "\\b/,'" + bits[sect] + "');");
    }

    return frm;
}

function fSetSelected(aCell)
{
  var iOffset = 0;
  var iYear = parseInt(tbSelYear.value);
  var iMonth = parseInt(tbSelMonth.value);

  aCell.bgColor = gcTableCellBG;
  
  with (aCell.children["cellText"])
  {
    var iDay = parseInt(innerText);
    if (color == gcOtherMonth) 
    {
      if (iDay < 13) 
      {
        iOffset = 1;
      } 
      else 
      {
        iOffset = -1;
      }
    }
    iMonth += iOffset;
    
    if (iMonth < 1) 
    {
      iYear--;
      iMonth = 12;
    } 
    else if (iMonth > 12) 
    {
      iYear++;
      iMonth = 1;
    }
  }
  setDate(iYear, iMonth, iDay);
  window.event.cancelBubble = true;
}

function Point(iX, iY)
{
  this.x = iX;
  this.y = iY;
}

function fBuildCal(iYear, iMonth) 
{
  var aMonth = new Array();
  for(i = 1; i < 7; i++) 
  {
    aMonth[i] = new Array(i);
  }

  var dCalDate = new Date(iYear, iMonth-1, 1);
  var iDayOfFirst = dCalDate.getDay();
  var iDaysInMonth = new Date(iYear, iMonth, 0).getDate();
  var iOffsetLast = new Date(iYear, iMonth-1, 0).getDate() - iDayOfFirst + 1;
  var iDate = 1;
  var iNext = 1;

  for (d = 0; d < 7; d++) 
  {
    aMonth[1][d] = (d<iDayOfFirst) ? -(iOffsetLast+d) : iDate++;
  }
  
  for (w = 2; w < 7; w++) 
  {
    for (d = 0; d < 7; d++) 
    {
      aMonth[w][d] = (iDate<=iDaysInMonth) ? iDate++ : -(iNext++);
    }
  }
  return aMonth;
}

function fDrawCal(iYear, iMonth) 
{
  var WeekDay = new Array("S","M","T","W","T","F","S");
  var styleTD = " bgcolor='" + gcTableCellBG + "' bordercolor='" + gcTableCellBG + 
                "' valign='middle' align='center' style='font:normal 11 Arial;";

  // Draw calendar table header
  var txt= "";
  txt += "<tr>";
  for (i = 0; i < 7; i++) {
    txt += "<td bgcolor='" + gcTableHeaderBG + "' " + 
           styleTD + "color:" + gcTableHeader + "'  >" + WeekDay[i] + "</td>";
  }
  txt += "</tr>";

  // Draw calendar table
  for (w = 1; w < 7; w++) {
    txt += "<tr>";
    for (d = 0; d < 7; d++) {
      txt += "<td id=calCell " + styleTD + 
             "cursor:hand;' onMouseOver='this.bgColor=gcToggle' onMouseOut='this.bgColor=gcTableCellBG' onclick='fSetSelected(this)'>";
      txt += "<font id=cellText> </font>";
      txt += "</td>";
    }
    txt += "</tr>";
  }
  return txt;
}

/**
 * Update the calendar. This function is called by month select and year select
 * onChange event handler.
 **/
function updateCalendar(iYear, iMonth) 
{
  var isSelectedYearMonth = false;
  var selectedDate = 0;
  if (gdSelectedDate != null) {
    isSelectedYearMonth = (gdSelectedDate.getFullYear() == iYear) &&
                          (gdSelectedDate.getMonth() + 1 == iMonth);   
    selectedDate = gdSelectedDate.getDate();
  }

  myMonth = fBuildCal(iYear, iMonth);
  var i = 0;
  for (w = 0; w < 6; w++) 
  {
    for (d = 0; d < 7; d++) 
    {
      // Mozilla 5.0 Error: cellText is not defined 
      with (cellText[(7*w)+d]) 
      {
        i++;
        if (myMonth[w+1][d] < 0) 
        {
          color = gcOtherMonth;
          innerText = -myMonth[w+1][d];
        } 
        else
        {
          color = gcTableCell;
          innerText = myMonth[w+1][d];
          if (isSelectedYearMonth && selectedDate == myMonth[w+1][d]) {
            color = "blue";
          }
        }
      }
    }
  }
  window.event.cancelBubble = true;
}

function fSetYearMon(iYear, iMon)
{
  tbSelMonth.options[iMon-1].selected = true;
  for (i = 0; i < tbSelYear.length; i++) 
  {
    if (tbSelYear.options[i].value == iYear)
    {
      tbSelYear.options[i].selected = true;
    }
  }
  updateCalendar(iYear, iMon);
}

function fGetXY(aTag)
{
  var oTmp = aTag;
  var pt = new Point(0,0);
  do {
    pt.x += oTmp.offsetLeft;
    pt.y += oTmp.offsetTop;
    oTmp = oTmp.offsetParent;
  } while(oTmp.tagName!="BODY");
  return pt;
}

function consumeEvent() 
{
    window.event.cancelBubble = true;
}
