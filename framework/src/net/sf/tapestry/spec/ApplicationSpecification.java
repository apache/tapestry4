package net.sf.tapestry.spec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sf.tapestry.IEngineService;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.BasePropertyHolder;

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