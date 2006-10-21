

dojo.provide("dojo.uuid.Uuid");
dojo.require("dojo.lang.common");
dojo.require("dojo.lang.assert");

dojo.uuid.Uuid = function( input) {













this._uuidString = dojo.uuid.Uuid.NIL_UUID;
if (input) {
if (dojo.lang.isString(input)) {
// input: string? A 36-character string that conforms to the UUID spec.
this._uuidString = input.toLowerCase();
dojo.lang.assert(this.isValid());
} else {
if (dojo.lang.isObject(input) && input.generate) {
// input: generator A UUID generator, such as dojo.uuid.TimeBasedGenerator.
var generator = input;
this._uuidString = generator.generate();
dojo.lang.assert(this.isValid());
} else {
// we got passed something other than a string
dojo.lang.assert(false, "The dojo.uuid.Uuid() constructor must be initializated with a UUID string.");
}
}
} else {
var ourGenerator = dojo.uuid.Uuid.getGenerator();
if (ourGenerator) {
this._uuidString = ourGenerator.generate();
dojo.lang.assert(this.isValid());
}
}
};




dojo.uuid.Uuid.NIL_UUID = "00000000-0000-0000-0000-000000000000";
dojo.uuid.Uuid.Version = {
UNKNOWN: 0,
TIME_BASED: 1,
DCE_SECURITY: 2,
NAME_BASED_MD5: 3,
RANDOM: 4,
NAME_BASED_SHA1: 5 };
dojo.uuid.Uuid.Variant = {
NCS: "0",
DCE: "10",
MICROSOFT: "110",
UNKNOWN: "111" };
dojo.uuid.Uuid.HEX_RADIX = 16;

dojo.uuid.Uuid.compare = function( uuidOne,  uuidTwo) {


















var uuidStringOne = uuidOne.toString();
var uuidStringTwo = uuidTwo.toString();
if (uuidStringOne > uuidStringTwo) return 1;   // integer
if (uuidStringOne < uuidStringTwo) return -1;  // integer
return 0; // integer (either 0, 1, or -1)
};

dojo.uuid.Uuid.setGenerator = function( generator) {





dojo.lang.assert(!generator || (dojo.lang.isObject(generator) && generator.generate));
dojo.uuid.Uuid._ourGenerator = generator;
};

dojo.uuid.Uuid.getGenerator = function() {


return dojo.uuid.Uuid._ourGenerator; // generator (A UUID generator, such as dojo.uuid.TimeBasedGenerator).
};

dojo.uuid.Uuid.prototype.toString = function(format) {

















if (format) {
switch (format) {
case '{}':
return '{' + this._uuidString + '}';
break;
case '()':
return '(' + this._uuidString + ')';
break;
case '""':
return '"' + this._uuidString + '"';
break;
case "''":
return "'" + this._uuidString + "'";
break;
case 'urn':
return 'urn:uuid:' + this._uuidString;
break;
case '!-':
return this._uuidString.split('-').join('');
break;
default:
// we got passed something other than what we expected
dojo.lang.assert(false, "The toString() method of dojo.uuid.Uuid was passed a bogus format.");
}
} else {
return this._uuidString; // string
}
};

dojo.uuid.Uuid.prototype.compare = function( otherUuid) {






return dojo.uuid.Uuid.compare(this, otherUuid); // integer (either 0, 1, or -1)
};

dojo.uuid.Uuid.prototype.isEqual = function( otherUuid) {



return (this.compare(otherUuid) == 0); // boolean
};

dojo.uuid.Uuid.prototype.isValid = function() {


try {
dojo.lang.assertType(this._uuidString, String);
dojo.lang.assert(this._uuidString.length == 36);
dojo.lang.assert(this._uuidString == this._uuidString.toLowerCase());
var arrayOfParts = this._uuidString.split("-");
dojo.lang.assert(arrayOfParts.length == 5);
dojo.lang.assert(arrayOfParts[0].length == 8);
dojo.lang.assert(arrayOfParts[1].length == 4);
dojo.lang.assert(arrayOfParts[2].length == 4);
dojo.lang.assert(arrayOfParts[3].length == 4);
dojo.lang.assert(arrayOfParts[4].length == 12);
for (var i in arrayOfParts) {
var part = arrayOfParts[i];
var integer = parseInt(part, dojo.uuid.Uuid.HEX_RADIX);
dojo.lang.assert(isFinite(integer));
}
return true; // boolean
} catch (e) {
return false; // boolean
}
};

dojo.uuid.Uuid.prototype.getVariant = function() {













var variantCharacter = this._uuidString.charAt(19);
var variantNumber = parseInt(variantCharacter, dojo.uuid.Uuid.HEX_RADIX);
dojo.lang.assert((variantNumber >= 0) && (variantNumber <= 16));

if (!dojo.uuid.Uuid._ourVariantLookupTable) {
var Variant = dojo.uuid.Uuid.Variant;
var lookupTable = [];

lookupTable[0x0] = Variant.NCS;       // 0000
lookupTable[0x1] = Variant.NCS;       // 0001
lookupTable[0x2] = Variant.NCS;       // 0010
lookupTable[0x3] = Variant.NCS;       // 0011

lookupTable[0x4] = Variant.NCS;       // 0100
lookupTable[0x5] = Variant.NCS;       // 0101
lookupTable[0x6] = Variant.NCS;       // 0110
lookupTable[0x7] = Variant.NCS;       // 0111

lookupTable[0x8] = Variant.DCE;       // 1000
lookupTable[0x9] = Variant.DCE;       // 1001
lookupTable[0xA] = Variant.DCE;       // 1010
lookupTable[0xB] = Variant.DCE;       // 1011

lookupTable[0xC] = Variant.MICROSOFT; // 1100
lookupTable[0xD] = Variant.MICROSOFT; // 1101
lookupTable[0xE] = Variant.UNKNOWN;   // 1110
lookupTable[0xF] = Variant.UNKNOWN;   // 1111

dojo.uuid.Uuid._ourVariantLookupTable = lookupTable;
}

return dojo.uuid.Uuid._ourVariantLookupTable[variantNumber]; // dojo.uuid.Uuid.Variant
};

dojo.uuid.Uuid.prototype.getVersion = function() {










if (!this._versionNumber) {
var errorMessage = "Called getVersion() on a dojo.uuid.Uuid that was not a DCE Variant UUID.";
dojo.lang.assert(this.getVariant() == dojo.uuid.Uuid.Variant.DCE, errorMessage);

// "b4308fb0-86cd-11da-a72b-0800200c9a66"
//                ^
//                |
//       (version 1 == TIME_BASED)
var versionCharacter = this._uuidString.charAt(14);
this._versionNumber = parseInt(versionCharacter, dojo.uuid.Uuid.HEX_RADIX);
}
return this._versionNumber; // dojo.uuid.Uuid.Version
};

dojo.uuid.Uuid.prototype.getNode = function() {







if (!this._nodeString) {
var errorMessage = "Called getNode() on a dojo.uuid.Uuid that was not a TIME_BASED UUID.";
dojo.lang.assert(this.getVersion() == dojo.uuid.Uuid.Version.TIME_BASED, errorMessage);

var arrayOfStrings = this._uuidString.split('-');
this._nodeString = arrayOfStrings[4];
}
return this._nodeString; // String (a 12-character string, which will look something like "917bf397618a")
};

dojo.uuid.Uuid.prototype.getTimestamp = function( returnType) {


















var errorMessage = "Called getTimestamp() on a dojo.uuid.Uuid that was not a TIME_BASED UUID.";
dojo.lang.assert(this.getVersion() == dojo.uuid.Uuid.Version.TIME_BASED, errorMessage);

if (!returnType) {returnType = null};
switch (returnType) {
case "string":
case String:
return this.getTimestamp(Date).toUTCString(); // String (e.g. "Mon, 16 Jan 2006 20:21:41 GMT")
break;
case "hex":
// Return a 15-character string of hex digits containing the
// timestamp for this UUID, with the high-order bits first.
if (!this._timestampAsHexString) {
var arrayOfStrings = this._uuidString.split('-');
var hexTimeLow = arrayOfStrings[0];
var hexTimeMid = arrayOfStrings[1];
var hexTimeHigh = arrayOfStrings[2];

// Chop off the leading "1" character, which is the UUID
// version number for time-based UUIDs.
hexTimeHigh = hexTimeHigh.slice(1);

this._timestampAsHexString = hexTimeHigh + hexTimeMid + hexTimeLow;
dojo.lang.assert(this._timestampAsHexString.length == 15);
}
return this._timestampAsHexString; // String (e.g. "1da86cdb4308fb0")
break;
case null: // no returnType was specified, so default to Date
case "date":
case Date:
// Return a JavaScript Date object.
if (!this._timestampAsDate) {
var GREGORIAN_CHANGE_OFFSET_IN_HOURS = 3394248;

var arrayOfParts = this._uuidString.split('-');
var timeLow = parseInt(arrayOfParts[0], dojo.uuid.Uuid.HEX_RADIX);
var timeMid = parseInt(arrayOfParts[1], dojo.uuid.Uuid.HEX_RADIX);
var timeHigh = parseInt(arrayOfParts[2], dojo.uuid.Uuid.HEX_RADIX);
var hundredNanosecondIntervalsSince1582 = timeHigh & 0x0FFF;
hundredNanosecondIntervalsSince1582 <<= 16;
hundredNanosecondIntervalsSince1582 += timeMid;
// What we really want to do next is shift left 32 bits, but the
// result will be too big to fit in an int, so we'll multiply by 2^32,
// and the result will be a floating point approximation.
hundredNanosecondIntervalsSince1582 *= 0x100000000;
hundredNanosecondIntervalsSince1582 += timeLow;
var millisecondsSince1582 = hundredNanosecondIntervalsSince1582 / 10000;

// Again, this will be a floating point approximation.
// We can make things exact later if we need to.
var secondsPerHour = 60 * 60;
var hoursBetween1582and1970 = GREGORIAN_CHANGE_OFFSET_IN_HOURS;
var secondsBetween1582and1970 = hoursBetween1582and1970 * secondsPerHour;
var millisecondsBetween1582and1970 = secondsBetween1582and1970 * 1000;
var millisecondsSince1970 = millisecondsSince1582 - millisecondsBetween1582and1970;

this._timestampAsDate = new Date(millisecondsSince1970);
}
return this._timestampAsDate; // Date
break;
default:
// we got passed something other than a valid returnType
dojo.lang.assert(false, "The getTimestamp() method dojo.uuid.Uuid was passed a bogus returnType: " + returnType);
break;
}
};
