package net.sf.tapestry.junit.mock.lib;

import net.sf.tapestry.html.BasePage;

/**
 *  Dumps out an array of objects.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Dumper extends BasePage
{
    private Object[] _objects;
    private Object _currentObject;
    
    public void detach()
    {
        _objects = null;
        _currentObject = null;
        
        super.detach();
    }
    
    public Object[] getObjects()
    {
        return _objects;
    }
    
    public void setObjects(Object[] objects)
    {
        _objects = objects;
        
        fireObservedChange("objects", _objects);
    }
    
    public Object getCurrentObject()
    {
        return _currentObject;
    }

    public void setCurrentObject(Object currentObject)
    {
        _currentObject = currentObject;
    }

    /**
     *  Returns the class name of the current object.  OGNL has trouble
     *  getting properties from Class objects.
     * 
     **/
    
    public String getClassName()
    {
        if (_currentObject == null)
            return "<Null>";
            
        return _currentObject.getClass().getName();
    }
}
