package net.sf.tapestry.util.xml;

import org.xml.sax.SAXParseException;

/**
 *  Exception thrown if there is any kind of error validating a string
 *  during document parsing
 *
 *  @see AbstractDocumentParser
 *
 *  @author Geoffrey Longman
 *  @version $Id$
 *  @since 2.2
 *
 **/
public class InvalidStringException extends DocumentParseException {

  private String invalidString;

  public InvalidStringException(String message, String invalidString, String resourcePath) 
  {
    super(message, resourcePath);
    this.invalidString = invalidString;
  }

 
  public String getInvalidString() 
  {
    return invalidString;
  }

}
