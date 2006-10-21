

dojo.provide("dojo.data.csv.CsvStore");
dojo.require("dojo.data.Read");
dojo.require("dojo.lang.assert");

dojo.declare("dojo.data.csv.CsvStore", dojo.data.Read, {



initializer:
function( keywordParameters) {
// keywordParameters: {string: String, url: String}
this._arrayOfItems = [];
this._loadFinished = false;
this._csvFileUrl = keywordParameters["url"];
this._csvFileContents = keywordParameters["string"];
},
get:
function( item,  attribute,  defaultValue) {
// summary: See dojo.data.Read.get()
var attributeValue = item[attribute] || defaultValue;
return attributeValue; // a literal, an item, null, or undefined (never an array)
},
getValues:
function( item,  attribute) {
// summary: See dojo.data.Read.getValues()
var array = [this.get(item, attribute)];
return array; // an array that may contain literals and items
},
getAttributes:
function( item) {
// summary: See dojo.data.Read.getAttributes()
var array = this._arrayOfKeys;
return array; // array
},
hasAttribute:
function( item,  attribute) {
// summary: See dojo.data.Read.hasAttribute()
for (var i in this._arrayOfKeys) {
if (this._arrayOfKeys[i] == attribute) {
return true;
}
}
return false; // boolean
},
hasAttributeValue:
function( item,  attribute,  value) {
// summary: See dojo.data.Read.hasAttributeValue()
return (this.get(item, attribute) == value); // boolean
},
isItem:
function( something) {
// summary: See dojo.data.Read.isItem()
for (var i in this._arrayOfItems) {
if (this._arrayOfItems[i] == something) {
return true;
}
}
return false; // boolean
},
find:
function( query,  optionalKeywordArgs ) {
// summary: See dojo.data.Read.find()
if (!this._loadFinished) {
if (this._csvFileUrl) {
this._csvFileContents = dojo.hostenv.getText(this._csvFileUrl);
}
var arrayOfArrays = this._getArrayOfArraysFromCsvFileContents(this._csvFileContents);
if (arrayOfArrays.length == 0) {
this._arrayOfKeys = [];
} else {
this._arrayOfKeys = arrayOfArrays[0];
}
this._arrayOfItems = this._getArrayOfItemsFromArrayOfArrays(arrayOfArrays);
}
var result = new dojo.data.csv.Result(this._arrayOfItems, this);
return result; // dojo.data.csv.Result
},
getIdentity:
function( item) {
// summary: See dojo.data.Read.getIdentity()
for (var i in this._arrayOfItems) {
if (this._arrayOfItems[i] == item) {
return i;
}
}
return null; // boolean
},
getByIdentity:
function( id) {
// summary: See dojo.data.Read.getByIdentity()
var i = parseInt(id);
if (i < this._arrayOfItems.length) {
return this._arrayOfItems[i];
} else {
return null;
}
},



_getArrayOfArraysFromCsvFileContents:
function( csvFileContents) {



dojo.lang.assertType(csvFileContents, String);

var lineEndingCharacters = new RegExp("\r\n|\n|\r");
var leadingWhiteSpaceCharacters = new RegExp("^\\s+",'g');
var trailingWhiteSpaceCharacters = new RegExp("\\s+$",'g');
var doubleQuotes = new RegExp('""','g');
var arrayOfOutputRecords = [];

var arrayOfInputLines = csvFileContents.split(lineEndingCharacters);
for (var i in arrayOfInputLines) {
var singleLine = arrayOfInputLines[i];
if (singleLine.length > 0) {
var listOfFields = singleLine.split(',');
var j = 0;
while (j < listOfFields.length) {
var space_field_space = listOfFields[j];
var field_space = space_field_space.replace(leadingWhiteSpaceCharacters, ''); // trim leading whitespace
var field = field_space.replace(trailingWhiteSpaceCharacters, ''); // trim trailing whitespace
var firstChar = field.charAt(0);
var lastChar = field.charAt(field.length - 1);
var secondToLastChar = field.charAt(field.length - 2);
var thirdToLastChar = field.charAt(field.length - 3);
if ((firstChar == '"') &&
((lastChar != '"') ||
((lastChar == '"') && (secondToLastChar == '"') && (thirdToLastChar != '"')) )) {
if (j+1 === listOfFields.length) {
// alert("The last field in record " + i + " is corrupted:\n" + field);
return null;
}
var nextField = listOfFields[j+1];
listOfFields[j] = field_space + ',' + nextField;
listOfFields.splice(j+1, 1); // delete element [j+1] from the list
} else {
if ((firstChar == '"') && (lastChar == '"')) {
field = field.slice(1, (field.length - 1)); // trim the " characters off the ends
field = field.replace(doubleQuotes, '"');   // replace "" with "
}
listOfFields[j] = field;
j += 1;
}
}
arrayOfOutputRecords.push(listOfFields);
}
}
return arrayOfOutputRecords; // Array
},

_getArrayOfItemsFromArrayOfArrays:
function( arrayOfArrays) {



dojo.lang.assertType(arrayOfArrays, Array);
var arrayOfItems = [];
if (arrayOfArrays.length > 1) {
var arrayOfKeys = arrayOfArrays[0];
for (var i = 1; i < arrayOfArrays.length; ++i) {
var row = arrayOfArrays[i];
var item = {};
for (var j in row) {
var value = row[j];
var key = arrayOfKeys[j];
item[key] = value;
}
arrayOfItems.push(item);
}
}
return arrayOfItems; // Array
}
});

