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