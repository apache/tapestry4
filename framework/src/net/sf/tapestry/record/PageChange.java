package net.sf.tapestry.record;

import org.apache.commons.lang.builder.ToStringBuilder;

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
    private String _componentPath;
    private String _propertyName;
    private Object _newValue;

    public PageChange(String componentPath, String propertyName, Object newValue)
    {
        _componentPath = componentPath;
        _propertyName = propertyName;
        _newValue = newValue;
    }

    /**
     *  The path to the component on the page, or null if the property
     *  is a property of the page.
     *
     **/

    public String getComponentPath()
    {
        return _componentPath;
    }

    /**
     *  The new value for the property, which may be null.
     *
     **/

    public Object getNewValue()
    {
        return _newValue;
    }

    /**
     *  The name of the property that changed.
     *
     **/

    public String getPropertyName()
    {
        return _propertyName;
    }

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("componentPath", _componentPath);
        builder.append("propertyName", _propertyName);
        builder.append("newValue", _newValue);
        
        return builder.toString();
    }
}