package net.sf.tapestry.binding;

import net.sf.tapestry.IComponent;

/**
 *  A binding that connects directly to a localized string for
 *  a component.
 *
 *  @see IComponent#getString(String)
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.4
 *
 **/

public class StringBinding extends AbstractBinding
{
    private IComponent _component;
    private String _key;

    public StringBinding(IComponent component, String key)
    {
        _component = component;
        _key = key;
    }

    public IComponent getComponent()
    {
        return _component;
    }

    public String getKey()
    {
        return _key;
    }

    /**
     *  Accesses the specified localized string.  Never returns null.
     *
     **/

    public Object getObject()
    {
        return _component.getString(_key);
    }

    /**
     *  Returns true.  Localized component strings are
     *  read-only.
     * 
     **/

    public boolean isInvariant()
    {
        return true;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("StringBinding");
        buffer.append('[');
        buffer.append(_component.getExtendedId());
        buffer.append(' ');
        buffer.append(_key);
        buffer.append(']');

        return buffer.toString();
    }
}