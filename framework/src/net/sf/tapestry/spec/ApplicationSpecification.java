package net.sf.tapestry.spec;


/**
 *  Defines the configuration for a Tapestry application.  An ApplicationSpecification
 *  extends {@link LibrarySpecification} by adding new properties
 *  name and engineClassName.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class ApplicationSpecification extends LibrarySpecification implements IApplicationSpecification
{
    private String _name;
    private String _engineClassName;
 
    public String getName()
    {
        return _name;
    }

    public void setEngineClassName(String value)
    {
        _engineClassName = value;
    }

    public String getEngineClassName()
    {
        return _engineClassName;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public String toString()
    {
        return "ApplicationSpecification[" + _name + " " + _engineClassName + "]";
    }

}