package net.sf.tapestry.record;

import net.sf.tapestry.IPageChange;

/**
 *  Represents a change to a component on a page.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class PageChange implements IPageChange
{
    String componentPath;
    String propertyName;
    Object newValue;

    public PageChange(String componentPath, String propertyName, Object newValue)
    {
        this.componentPath = componentPath;
        this.propertyName = propertyName;
        this.newValue = newValue;
    }

    /**
     *  The path to the component on the page, or null if the property
     *  is a property of the page.
     *
     **/

    public String getComponentPath()
    {
        return componentPath;
    }

    /**
     *  The new value for the property, which may be null.
     *
     **/

    public Object getNewValue()
    {
        return newValue;
    }

    /**
     *  The name of the property that changed.
     *
     **/

    public String getPropertyName()
    {
        return propertyName;
    }

    public String toString()
    {
        StringBuffer buffer;

        buffer = new StringBuffer(getClass().getName());

        buffer.append('[');

        if (componentPath != null)
        {
            buffer.append(componentPath);
            buffer.append(' ');
        }

        buffer.append(propertyName);

        buffer.append(' ');
        buffer.append(newValue);

        buffer.append(']');

        return buffer.toString();
    }
}