package net.sf.tapestry.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *  Base class implementation for the {@link IPropertyHolder} interface.
 *
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class BasePropertyHolder implements IPropertyHolder
{
    private static final int MAP_SIZE = 7;
    private Map properties;

    public String getProperty(String name)
    {
        if (properties == null)
            return null;

        return (String) properties.get(name);
    }

    public void setProperty(String name, String value)
    {
        if (value == null)
        {
            removeProperty(name);
            return;
        }

        if (properties == null)
            properties = new HashMap(MAP_SIZE);

        properties.put(name, value);
    }

    public void removeProperty(String name)
    {
        if (properties == null)
            return;

        properties.remove(name);
    }

    public List getPropertyNames()
    {
        if (properties == null)
            return Collections.EMPTY_LIST;

        List result = new ArrayList(properties.keySet());
        
        Collections.sort(result);
        
        return result;
    }

}