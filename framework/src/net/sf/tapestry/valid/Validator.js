// $Id$
//
// Simple functions to support input field validation in Tapestry.

function validator_invalid_field(field, message)
{
  field.focus();
  field.select();
  
  window.alert(message);
  
  return false;
}