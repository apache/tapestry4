/**
 *  JavaScript Mask Edit control
 *  Paul Geerts
 *  Oct 2002
 * 
 *  Note:  This probably only works for English
 *  Other languages have been deprecated and will be removed in the 
 *  next version of Speech (TM)
 **/

var dontDoIt;  // hack for Moz because it won't cancel events properly
var isTab;     // another Moz hack


// Init the mask edit field by creating a lookalike DIV
// and hiding the real one
function initMask(field, maskField) {

	if (field.disabled == true) {
	   return;
	}

    var mask = maskField.value;
    var val = field.value;
    
    if (!val) {  // if there's no val, init it with empty mask
        val = displayMask(mask);
        field.value = displayMask(mask);
    }
    // create a div and add a bunch of spans
    // and edits to it.
    div = document.createElement("div");
   	div.style.backgroundColor = "white";
    for (var i = 0 ; i < mask.length ; i++) {
        var ds = document.createElement("SPAN");
        var v = val.substr(i,1);
        var m = mask.substr(i,1);
        if (v==" ") {
            v="&nbsp;";
        }
        ds.innerHTML = v;
        ds.index = i;
        ds.mask = m;
        ds.div = div;
        // if we can edit this char
        // make a little tiny edit field
        if (isEditChar(m)) {
            var es = document.createElement("INPUT");
            es.style.width = "1px";
            es.style.border="0px";
            es.index = i;
            es.field = field;
            es.mask = m;
            es.display = ds;
            ds.editField = es;
            es.div = div;
            div.appendChild(es);  // set up some events
            if (navigator.appName == "Microsoft Internet Explorer") {
                addEvent("keypress", es, changeBitIE);
            } else {
                addEvent("keypress", es, changeBitNS);
            }
            addEvent("keydown", es, specialKey); // keydown handles stuff like home, end etc
            addEvent("click", ds, click);
        } 

        div.appendChild(ds);

    }
    
    // the final edit field on the end
    var es =document.createElement("INPUT");
    es.style.width = "1px";
    es.style.border="0px";
    es.div = div;
    div.appendChild(es);
    if (navigator.appName == "Microsoft Internet Explorer") {
        addEvent("keypress", es, changeBitIE);
    } else {
        addEvent("keypress", es, changeBitNS);
    }
    addEvent("keydown", es, specialKey);

    div.noWrap = true; // force single line display

    formatDiv(div, field); // format the DIV to look like an edit box
    field.style.display = 'none'; 
    field.parentNode.insertBefore(div, field);
    addEvent("click", div, divClick);
}

function formatDiv(div, field) {
    // make it look like an IE edit
    if (navigator.appName == "Microsoft Internet Explorer") {
        div.style.fontFamily="courier"; 
        div.style.fontSize="10pt";      
        div.style.width = field.offsetWidth;
        div.style.height = field.offsetHeight;
        if (navigator.appVersion.match(/6.0/)) { // IE 6 is different
            div.style.border = "1px solid #7F9DB9";
        } else {
            div.style.borderLeft = "2px solid #606060";
            div.style.borderTop = "2px solid #606060";
            div.style.borderRight = "1px solid #aaaaaa";
            div.style.borderBottom = "1px solid #aaaaaa";
        }

    } else {
        // Mozilla edit look-a-like
        div.style.fontFamily="courier";
        div.style.fontSize="10pt";
        div.style.border="2px inset #cccccc";
        if (field.size) {
            div.style.widh = 13 * field.size;
        } else {
            div.style.width = "130px";
        }
    }
}


function isEditChar(c) {  // is this char a meaningful mask char
    switch (c) {
    case "_":
    case "#":
    case "a":
    case "A":
    case "l":
    case "L":
        return true;
    default:
        return false;
    }
    return false;
}

function displayMaskChar(c) {  // display mask chars as _ 
    if (isEditChar(c)) {       // otherwise just show normal char
        return "_";
    } else {
        return c;
    }
}

function displayMask(mask) {  // display entire mask using about subroutine
    var d = "";
    for (var i = 0 ; i < mask.length ; i++) {
        d+=displayMaskChar(mask.substr(i,1));
    }
    return d;
}

function divClick(e) {     // when the main DIV is clicked, focus the end of the edit
    var d = getEventObject(e);
    if (d && d.lastChild) {
        try {
           d.lastChild.focus();
        } catch (e) {
           // nuffin
        }
    }
}

function specialKey(e) { // deal with special keys like backspace, delete etc
    var s = getEventObject(e);
    var code = e.keyCode;
    dontDoIt = true;  // Moz needs these, as I can't seem to cancel events properly
    isTab = false;    // Moz can't handle tabs well either
    switch (code) {
    case 8:   // backspace
        var b = getPrevEdit(s);
        if (b) {
            b.display.innerHTML = displayMaskChar(b.mask);
            var i = b.index;
            b.field.value = b.field.value.substr(0, i) + 
                displayMaskChar(b.mask) + b.field.value.substr(i+1, b.field.value.length - i);
            b.focus();
        }
        cancelEvent(e);
        return false;
    case 46:  // delete
        if (s.display) {
            s.display.innerHTML = displayMaskChar(s.mask);
            var i = s.index;
            s.field.value = s.field.value.substr(0, i) + displayMaskChar(s.mask) + 
                s.field.value.substr(i+1, s.field.value.length - i);
        }
        cancelEvent(e);
        return false;
        break;
    case 37: // left
        var p = getPrevEdit(s);
        if (p) {
            p.focus();
        }
        cancelEvent(e);
        return false;
    case 39: // right
        var n = getNextEdit(s);
        if (n) {
            n.focus();
        }
        cancelEvent(e);
        return false;
    case 36: // home
        s.div.firstChild.focus();
        cancelEvent(e);
        return false;
    case 35: // end
        s.div.lastChild.focus();
        cancelEvent(e);
        return false;
    case 9: // tab
        if (navigator.appName == "Microsoft Internet Explorer") {
            if (!e.shiftKey) {
                s.div.lastChild.focus();
            } else {
                s.div.firstChild.focus();
            }
            return;
        } else {  // is mozilla/netscape
            isTab = true;  // best i can do really
        }
        break;
    }
       
    dontDoIt = false;
}

function moveForward(s) { // focus next edit
    var b = getNextEdit(s);
    if (b) {
        b.focus();
    }
}

function moveBackward(s) { // focus previous edit
    var b = getPrevEdit(s);
    if (b) {
        b.focus();
    }
}

function isInsertOK(code, s) {  // check if you're good to insert a char
    var mchar = s.mask;
    switch (mchar) {
    case "_":
        return true;
        break;
    case "#":
        return checkDigit(code);
        break;
    case "a":
        return checkAlphaNumeric(code);
        break;
    case "A":
        return checkUpCaseAlphaNumeric(code);
        break;
    case "l":
        return checkAlpha(code);
        break;
    case "L":
        return checkUpCaseAlpha(code);
        break;
    }
    return false;
}

// functions to check the key code, good ol ASCII
// fairly straightforward

function checkDigit(code) {
    if ((code>=48) && (code<=57)) {
        return code;
    } else {
        return null;
    }
}

function checkAlpha(code) {
    if (((code>=65) && (code<=90)) || ((code>=97) && (code<=122))) {
        return code;
    } else {
        return null;
    }
}

function checkUpCaseAlpha(code) {
    if ((code>=65) && (code<=90)) {
        return code;
    } else if ((code>=97) && (code<=122)) {
        return code - 32;
    } else {
        return null;
    }
}

function checkAlphaNumeric(code) {
    if (((code>=65) && (code<=90)) || ((code>=97) && (code<=122)) || ((code>=48) && (code<=57))) {
        return code;
    } else {
        return null;
    }
}

function checkUpCaseAlphaNumeric(code) {
    if ((code>=65) && (code<=90)) {
        return code;
    } else if ((code>=97) && (code<=122)) {
        return code - 32;
    } else if ((code>=48) && (code<=57)) {
        return code;
    } else {
        return null;
    }
}


function changeBitNS(e) {  // handle key events in NS
    var es = getEventObject(e);
    if (!isTab) {
        if (es.display) {
            if (!dontDoIt) {
                var code = e.charCode;
                if (code = isInsertOK(code, es)) {
                    var  c = String.fromCharCode(code);
                    es.display.innerHTML = c
                        var i = es.index;
                    es.field.value = es.field.value.substr(0, i) + c + es.field.value.substr(i+1, es.field.value.length - i);
                    moveForward(es);
                }
            }
            es.value = "";
            cancelEvent(e);
        }        
        return false;
    }  
}

function changeBitIE(e) { // handle key events in IE
    var es = getEventObject(e);
    if (es.display) {
        var code = e.keyCode;
        if (code = isInsertOK(code, es)) {
            var  c = String.fromCharCode(code);
            es.display.innerHTML = c;
            var i = es.index;
            es.field.value = es.field.value.substr(0, i) + c + es.field.value.substr(i+1, es.field.value.length - i);
            moveForward(es);
            es.value = "";
        }
    }
    cancelEvent(e);
    return false;
}

function click(e) {  // clicking on a display span focuses the edit
    var s = getEventObject(e);
    s.editField.focus();
    cancelEvent(e);
    return false;
}

function getPrevEdit(s) {    // get previous input field 
    var b = s.previousSibling;
    while (b && (b.tagName!="INPUT")) {
        b = b.previousSibling;
    }
    return b;
}

function getNextEdit(s) { // get previous next field 
    var b = s.nextSibling;
    while (b && (b.tagName!="INPUT")) {
        b = b.nextSibling;
    }
    return b;
}

function cancelEvent(e) {   // kill event propagation
    e.cancelBubble = true;
    e.cancel = true;
    if (navigator.appName != "Microsoft Internet Explorer") {
        e.stopPropagation();  // doesn't seem to work for key events
        e.preventDefault();
    }
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
