package net.sf.tapestry;

/**
 *  A source for configuration properties.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 *
 **/

public interface IPropertySource
{
    /**
     *  Returns the value for a given property, or null if the
     *  source does not provide a value for the named property.
     *  Implementations of IPropertySource may use delegation
     *  to resolve the value (that is, if one property source returns null,
     *  it may forward the request to another source).
     * 
     **/
    
    public String getPropertyValue(String propertyName);
}
