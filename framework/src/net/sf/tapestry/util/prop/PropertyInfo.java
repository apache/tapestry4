package net.sf.tapestry.util.prop;

/**
 *  Used by {@link net.sf.tapestry.util.prop.PropertyFinder}
 *  to identify information about a property. 
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class PropertyInfo
{
    private String _name;
    private Class _type;
    private boolean _read;
    private boolean _write;
    
    PropertyInfo(String name, Class type, boolean read, boolean write)
    {
        _name = name;
        _type = type;
        _read = read;
        _write = write;
    }
    
    public String getName()
    {
        return _name;
    }

    public Class getType()
    {
        return _type;
    }

    public boolean isRead()
    {
        return _read;
    }

    public boolean isWrite()
    {
        return _write;
    }
    
    public boolean isReadWrite()
    {
        return _read && _write;
    }

}
