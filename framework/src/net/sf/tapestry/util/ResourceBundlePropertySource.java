package net.sf.tapestry.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.sf.tapestry.IPropertySource;

/**
 *  A property source that is based on a {@link java.util.ResourceBundle}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class ResourceBundlePropertySource implements IPropertySource
{
    private ResourceBundle _bundle;

    public ResourceBundlePropertySource(ResourceBundle bundle)
    {
        _bundle = bundle;
    }

    /**
     *  Gets the value from the bundle by invoking
     *  {@link ResourceBundle#getString(java.lang.String)}.  If
     *  the bundle does not contain the key (that is, it it
     *  throws {@link java.util.MissingResourceException}), then
     *  null is returned.
     * 
     **/
    
    public String getPropertyValue(String propertyName)
    {
        try
        {
            return _bundle.getString(propertyName);
        }
        catch (MissingResourceException ex)
        {
            return null;
        }
    }

}
