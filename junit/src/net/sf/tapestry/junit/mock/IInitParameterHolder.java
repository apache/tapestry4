package net.sf.tapestry.junit.mock;

import java.util.Enumeration;

/**
 *  Common interface for several mock objects that can contain
 *  initial parameters.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public interface IInitParameterHolder
{
    public String getInitParameter(String name);

    public Enumeration getInitParameterNames();
    
    /**
     *  Allows Mock objects to have initial parameters set.
     * 
     **/
    
    public void setInitParameter(String name, String value);
}
