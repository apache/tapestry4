package net.sf.tapestry.parse;

/**
 *  Exception thrown indicating a problem parsing an HTML template.
 *
 *  @author Howard Ship
 *  @version $Id$
 * 
 **/

public class TemplateParseException extends Exception
{
    private String _resourcePath;
    private int _line = -1;
    private Throwable _rootCause;

    public TemplateParseException(String message)
    {
        this(message, -1, null);
    }

    public TemplateParseException(String message, int line, String resourcePath)
    {
        this(message, line, resourcePath, null);
    }

    public TemplateParseException(String message, int line, String resourcePath, Throwable rootCause)
    {
        super(message);

        _line = line;
        _resourcePath = resourcePath;

        _rootCause = rootCause;

    }

    public int getLine()
    {
        return _line;
    }

    public String getResourcePath()
    {
        return _resourcePath;
    }

    public Throwable getRootCause()
    {
        return _rootCause;
    }

}