package net.sf.tapestry.util.xml;

import org.xml.sax.SAXParseException;

import net.sf.tapestry.IResourceLocation;

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

public class InvalidStringException extends DocumentParseException
{
    private String _invalidString;

    public InvalidStringException(String message, String invalidString, IResourceLocation resourceLocation)
    {
        super(message, resourceLocation);

        _invalidString = invalidString;
    }

    public String getInvalidString()
    {
        return _invalidString;
    }

}
