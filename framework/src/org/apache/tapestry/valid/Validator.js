// $Id: Validator.js,v 1.1 2002/09/07 13:03:24 hship Exp $
//
// Simple functions to support input field validation in Tapestry.

function validator_invalid_field(field, message)
{
  field.focus();
  field.select();
  
  window.alert(message);
  
  return false;
}

function regexpTestUrl(sUrl) {
	var regexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/
	return regexp.test(sUrl);
}