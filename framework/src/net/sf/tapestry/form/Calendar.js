//****************************************************************************
// PopCalendar 4.1, Emailware(please mail&commend me if u like it)
// Originally coded by Liming(Victor) Weng, email: victorwon@netease.com
// Release date: 2000.5.9
// Anyone may modify it to satify his needs, but please leave this comment ahead.
//****************************************************************************

//****************************************************************************
// Extensively modified by Paul Geetz
// Date: August 2002
//****************************************************************************

var gdCtrl = new Object();
var gcGray = "#808080";
var gcToggle = "#ffff00";
var gcBG = "#cccccc";

var gRetCtrl;
var gFormat;
var gdCurDate = new Date();
var giYear = gdCurDate.getFullYear();
var giMonth = gdCurDate.getMonth()+1;
var giDay = gdCurDate.getDate();
var VicPopCal = null;

//****************************************************************************
// Param: popCtrl is the widget beyond which you want this calendar to appear;
//        dateCtrl is the widget into which you want to put the selected date;
//        popCal is the widget to display the calendar;  
// i.e.: <input type="text" name="dc" style="text-align:center" readonly>
// <INPUT type="button" value="V" onclick="fPopCalendar(dc,dc,popCal);return false">
//****************************************************************************
function fPopCalendar(popCtrl, dateCtrl, valCtrl, popCal) 
{
  if (VicPopCal) {
        fHideCalendar();
        return;
  }      
  VicPopCal = popCal;
  gdCtrl = dateCtrl;
  gRetCtrl = valCtrl;
  gFormat = dateCtrl.format;

  var d = new Date();
  d.setTime(gRetCtrl.value);
  fSetYearMon(d.getYear(), d.getMonth()+1);

  var point = fGetXY(popCtrl);
  with (VicPopCal.style) {
    left = point.x;
  top  = point.y+popCtrl.offsetHeight+1;
  visibility = 'visible';
  }

  window.event.cancelBubble = true;
  document.body.attachEvent("onclick", fHideCalendar);
  VicPopCal.focus();
}

function fHideCalendar() 
{
 if (VicPopCal) {
     VicPopCal.style.visibility = "hidden";
     VicPopCal = null;
     //document.body.detachEvent("onclick", fHideCalendar);
 } 
}

function fSetDate(iYear, iMonth, iDay)
{
  var d = new Date();
  d.setYear(iYear);
  d.setMonth(iMonth-1);
  d.setDate(iDay);

  gdCtrl.value = formatDate(d);
  gRetCtrl.value = d.getTime();

  fHideCalendar();
}

var monthShortNames = new Array('Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec');
var monthLongNames = new Array('January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December');
var dayNames = new Array('Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday','Friday', 'Saturday');

function pad(number,X) 
{
	X = (!X ? 2 : X);
	number = ""+number;
	while (number.length<X) {
	    number="0"+number;
	}
	return number;
}

function formatDate(date) 
{

    var bits = new Array();
    bits['d'] = date.getDate();
    bits['dd'] = pad(date.getDate(),2);
    bits['dddd'] = dayNames[date.getDay()];

    bits['M'] = date.getMonth()+1;
    bits['MM'] = pad(date.getMonth()+1,2);
    bits['MMM'] = monthShortNames[date.getMonth()];
    bits['MMMM'] = monthLongNames[date.getMonth()];
    bits['yyyy'] = date.getYear();
    bits['yy'] = bits['yyyy'].toString().substr(2,2);

    var frm = new String(gFormat);
    var sect;
    for (sect in bits) {
      frm=eval("frm.replace(/\\b"+sect+"\\b/,'"+bits[sect]+"');");
    }

    return frm;


}

function fSetSelected(aCell)
{
  var iOffset = 0;
  var iYear = parseInt(tbSelYear.value);
  var iMonth = parseInt(tbSelMonth.value);

  aCell.bgColor = gcBG;
  with (aCell.children["cellText"]){
    var iDay = parseInt(innerText);
    if (color==gcGray)
    iOffset = -1;
  iMonth += iOffset;
  if (iMonth<1) {
    iYear--;
    iMonth = 12;
  }else if (iMonth>12){
    iYear++;
    iMonth = 1;
  }
  }
  fSetDate(iYear, iMonth, iDay);
  window.event.cancelBubble = true;
}

function Point(iX, iY)
{
  this.x = iX;
  this.y = iY;
}

function fBuildCal(iYear, iMonth) 
{
  var aMonth=new Array();
  for(i=1;i<7;i++)
    aMonth[i]=new Array(i);

  var dCalDate=new Date(iYear, iMonth-1, 1);
  var iDayOfFirst=dCalDate.getDay();
  var iDaysInMonth=new Date(iYear, iMonth, 0).getDate();
  var iOffsetLast=new Date(iYear, iMonth-1, 0).getDate()-iDayOfFirst+1;
  var iDate = 1;
  var iNext = 1;

  for (d = 0; d < 7; d++)
  aMonth[1][d] = (d<iDayOfFirst)?-(iOffsetLast+d):iDate++;
  for (w = 2; w < 7; w++)
    for (d = 0; d < 7; d++)
    aMonth[w][d] = (iDate<=iDaysInMonth)?iDate++:-(iNext++);
  return aMonth;
}

function fDrawCal(iYear, iMonth, iCellWidth, iDateTextSize) 
{
  var WeekDay = new Array("Su","Mo","Tu","We","Th","Fr","Sa");
  var styleTD = " bgcolor='"+gcBG+"' width='"+iCellWidth+"' height = 19 bordercolor='"+gcBG+"' valign='middle' align='center'  style='font:bold "+iDateTextSize+" Courier;";            //Coded by Liming Weng(Victor Won)  email:victorwon@netease.com

  var txt="";
  txt+="<tr>";
  for(i=0; i<7; i++)
    txt+="<td "+styleTD+"color:#000099' >" + WeekDay[i] + "</td>";
  txt+="</tr>";

    for (w = 1; w < 7; w++) {
    txt+="<tr>";
    for (d = 0; d < 7; d++) {
      txt+="<td id=calCell "+styleTD+"cursor:hand;' onMouseOver='this.bgColor=gcToggle' onMouseOut='this.bgColor=gcBG' onclick='fSetSelected(this)'>";
      txt+="<font id=cellText> </font>";
      txt+="</td>";
    }
    txt+="</tr>";
  }
  return txt;

}

function fUpdateCal(iYear, iMonth) 
{
  myMonth = fBuildCal(iYear, iMonth);
  var i = 0;
  for (w = 0; w < 6; w++)
  for (d = 0; d < 7; d++)
    with (cellText[(7*w)+d]) {
      i++;
      if (myMonth[w+1][d]<0) {
        color = gcGray;
        innerText = -myMonth[w+1][d];
      }else{
        color = ((d==0)||(d==6))?"white":"black";
        innerText = myMonth[w+1][d];
      }
    }
  window.event.cancelBubble = true;
}

function fSetYearMon(iYear, iMon)
{
  tbSelMonth.options[iMon-1].selected = true;
  for (i = 0; i < tbSelYear.length; i++)
  if (tbSelYear.options[i].value == iYear)
    tbSelYear.options[i].selected = true;
  fUpdateCal(iYear, iMon);
}

function fPrevMonth()
{
  var iMon = tbSelMonth.value;
  var iYear = tbSelYear.value;

  if (--iMon<1) {
    iMon = 12;
    iYear--;
  }

  fSetYearMon(iYear, iMon);
  window.event.cancelBubble = true;
}

function fNextMonth()
{
  var iMon = tbSelMonth.value;
  var iYear = tbSelYear.value;

  if (++iMon>12) {
    iMon = 1;
    iYear++;
  }

  fSetYearMon(iYear, iMon);
  window.event.cancelBubble = true;
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

function doNuffin() 
{
    window.event.cancelBubble = true;
}
