package net.sf.tapestry.util.xml;

import org.xml.sax.SAXParseException;

/**
 *  Exception thrown if there is any kind of error parsing the
 *  an XML document. 
 *
 *  @see AbstractDocumentParser
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 0.2.10
 *
 **/

public class DocumentParseException extends Exception
{
    private Throwable rootCause;
    private int lineNumber;
    private int column;
    private String resourcePath;

    public DocumentParseException(String message, Throwable rootCause)
    {
        this(message, null, rootCause);
    }

    public DocumentParseException(String message, String resourcePath)
    {
        this(message, resourcePath, null);
    }

    public DocumentParseException(String message, SAXParseException rootCause)
    {
        this(message, null, rootCause);
    }

    public DocumentParseException(
        String message,
        String resourcePath,
        Throwable rootCause)
    {
        super(message);

        this.resourcePath = resourcePath;

        this.rootCause = rootCause;
    }

    public DocumentParseException(
        String message,
        String resourcePath,
        SAXParseException rootCause)
    {
        this(message, resourcePath, (Throwable) rootCause);

        if (rootCause != null)
        {
            lineNumber = rootCause.getLineNumber();
            column = rootCause.getColumnNumber();
        }
    }

    public DocumentParseException(String message)
    {
        super(message);
    }

    public DocumentParseException(Throwable rootCause)
    {
        this(rootCause.getMessage(), rootCause);
    }

    public DocumentParseException(SAXParseException rootCause)
    {
        this(rootCause.getMessage(), (Throwable) rootCause);
    }

    public Throwable getRootCause()
    {
        return rootCause;
    }

    public int getLineNumber()
    {
        return lineNumber;
    }

    public int getColumn()
    {
        return column;
    }

    public String getResourcePath()
    {
        return resourcePath;
    }

}