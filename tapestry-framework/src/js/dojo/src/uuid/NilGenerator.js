

dojo.provide("dojo.uuid.NilGenerator");

dojo.uuid.NilGenerator = new function() {
this.generate = function( returnType) {
// summary:
//   This function returns the Nil UUID: "00000000-0000-0000-0000-000000000000".
// description:
//   The Nil UUID is described in section 4.1.7 of
//   RFC 4122: http://www.ietf.org/rfc/rfc4122.txt
// returnType: The type of object to return. Usually String or dojo.uuid.Uuid

// examples:
//   var string = dojo.uuid.NilGenerator.generate();
//   var string = dojo.uuid.NilGenerator.generate(String);
//   var uuid   = dojo.uuid.NilGenerator.generate(dojo.uuid.Uuid);
var returnValue = "00000000-0000-0000-0000-000000000000";
if (returnType && (returnType != String)) {
returnValue = new returnType(returnValue);
}
return returnValue; // object
};
}();