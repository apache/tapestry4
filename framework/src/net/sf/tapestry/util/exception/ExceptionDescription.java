package net.sf.tapestry.util.exception;

import java.io.Serializable;

/**
 *  A description of an <code>Exception</code>.  This is useful when presenting an
 *  exception (in output or on a web page).
 *
 *  <p>We capture all the information about an exception as
 *  Strings.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ExceptionDescription implements Serializable
{
    /**
     *  @since 2.0.4
     * 
     **/

    private static final long serialVersionUID = -4874930784340781514L;

    private String exceptionClassName;
    private String message;
    private ExceptionProperty[] properties;
    private String[] stackTrace;

    public ExceptionDescription(
        String exceptionClassName,
        String message,
        ExceptionProperty[] properties,
        String[] stackTrace)
    {
        this.exceptionClassName = exceptionClassName;
        this.message = message;
        this.properties = properties;
        this.stackTrace = stackTrace;
    }

    public String getExceptionClassName()
    {
        return exceptionClassName;
    }

    public String getMessage()
    {
        return message;
    }

    public ExceptionProperty[] getProperties()
    {
        return properties;
    }

    public String[] getStackTrace()
    {
        return stackTrace;
    }
}