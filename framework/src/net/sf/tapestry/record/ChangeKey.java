package net.sf.tapestry.record;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *  Used to identify a property change.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ChangeKey
{
    private int _hashCode = -1;
    private String _componentPath;
    private String _propertyName;

    public ChangeKey(String componentPath, String propertyName)
    {
        _componentPath = componentPath;
        _propertyName = propertyName;
    }

    public boolean equals(Object object)
    {
        if (object == null)
            return false;

        if (this == object)
            return true;

        if (!(object instanceof ChangeKey))
            return false;

        ChangeKey other = (ChangeKey) object;

        EqualsBuilder builder =new EqualsBuilder();
        
        builder.append(_propertyName, other._propertyName);
        builder.append(_componentPath, other._componentPath);
        
        return builder.isEquals();
    }

    public String getComponentPath()
    {
        return _componentPath;
    }

    public String getPropertyName()
    {
        return _propertyName;
    }

    /**
     *
     *  Returns a hash code computed from the
     *  property name and component path.
     *
     **/

    public int hashCode()
    {
        if (_hashCode == -1)
        {
            HashCodeBuilder builder = new HashCodeBuilder(257, 23);  // Random

            builder.append(_propertyName);
            builder.append(_componentPath);

            _hashCode = builder.toHashCode();
        }

        return _hashCode;
    }
}