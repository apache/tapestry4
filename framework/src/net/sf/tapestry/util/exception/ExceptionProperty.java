package net.sf.tapestry.util.exception;

import java.io.Serializable;

/**
 *  Captures a name/value property pair from an exception.  Part of
 *  an {@link ExceptionDescription}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ExceptionProperty implements Serializable
{
    /**
     *  @since 2.0.4
     * 
     **/
    
    private static final long serialVersionUID = -4598312382467505134L;
    private String name;
    private String value;

    public ExceptionProperty(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return value;
    }
}