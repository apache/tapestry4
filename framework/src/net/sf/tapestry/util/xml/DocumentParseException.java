package net.sf.tapestry.util.xml;

import org.xml.sax.SAXParseException;

import net.sf.tapestry.IResourceLocation;

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
    private Throwable _rootCause;
    private int _lineNumber;
    private int _column;
    private IResourceLocation _resourceLocation;

    public DocumentParseException(String message, Throwable rootCause)
    {
        this(message, null, rootCause);
    }

    public DocumentParseException(String message, IResourceLocation resourcePath)
    {
        this(message, resourcePath, null);
    }

    public DocumentParseException(String message, SAXParseException rootCause)
    {
        this(message, null, rootCause);
    }

    public DocumentParseException(
        String message,
        IResourceLocation resourceLocation,
        Throwable rootCause)
    {
        super(message);

        _resourceLocation = resourceLocation;

        _rootCause = rootCause;
    }

    public DocumentParseException(
        String message,
        IResourceLocation resourceLocation,
        SAXParseException rootCause)
    {
        this(message, resourceLocation, (Throwable) rootCause);

        if (rootCause != null)
        {
            _lineNumber = rootCause.getLineNumber();
            _column = rootCause.getColumnNumber();
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
        return _rootCause;
    }

    public int getLineNumber()
    {
        return _lineNumber;
    }

    public int getColumn()
    {
        return _column;
    }

    public IResourceLocation getResourcePath()
    {
        return _resourceLocation;
    }

}