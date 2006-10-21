

dojo.provide("dojo.uuid.LightweightGenerator");



dojo.uuid.LightweightGenerator = new function() {
var HEX_RADIX = 16;

function _generateRandomEightCharacterHexString() {
// Make random32bitNumber be a randomly generated floating point number
// between 0 and (4,294,967,296 - 1), inclusive.
var random32bitNumber = Math.floor( (Math.random() % 1) * Math.pow(2, 32) );
var eightCharacterHexString = random32bitNumber.toString(HEX_RADIX);
while (eightCharacterHexString.length < 8) {
eightCharacterHexString = "0" + eightCharacterHexString;
}
return eightCharacterHexString; // for example: "3B12F1DF"
}

this.generate = function( returnType) {
// summary:
//   This function generates random UUIDs, meaning "version 4" UUIDs.
// description:
//   A typical generated value would be something like this:
//   "3b12f1df-5232-4804-897e-917bf397618a"
// returnType: The type of object to return. Usually String or dojo.uuid.Uuid

// examples:
//   var string = dojo.uuid.LightweightGenerator.generate();
//   var string = dojo.uuid.LightweightGenerator.generate(String);
//   var uuid   = dojo.uuid.LightweightGenerator.generate(dojo.uuid.Uuid);
var hyphen = "-";
var versionCodeForRandomlyGeneratedUuids = "4"; // 8 == binary2hex("0100")
var variantCodeForDCEUuids = "8"; // 8 == binary2hex("1000")
var a = _generateRandomEightCharacterHexString();
var b = _generateRandomEightCharacterHexString();
b = b.substring(0, 4) + hyphen + versionCodeForRandomlyGeneratedUuids + b.substring(5, 8);
var c = _generateRandomEightCharacterHexString();
c = variantCodeForDCEUuids + c.substring(1, 4) + hyphen + c.substring(4, 8);
var d = _generateRandomEightCharacterHexString();
var returnValue = a + hyphen + b + hyphen + c + d;
returnValue = returnValue.toLowerCase();
if (returnType && (returnType != String)) {
returnValue = new returnType(returnValue);
}
return returnValue;
};
}();
