package net.sf.tapestry.spec;
/**
 *  Defines a transient or persistant property of a component or page.  
 *  A {@link net.sf.tapestry.IComponentClassEnhancer} uses this information
 *  to create a subclass with the necessary instance variables and methods.  
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 * 
 **/

public class PropertySpecification
{
	private String _name;
	private String _type = "java.lang.Object";
	private boolean _persistent;
	private String _initialValue;
	
    public String getInitialValue()
    {
        return _initialValue;
    }

    public String getName()
    {
        return _name;
    }

    public boolean isPersistent()
    {
        return _persistent;
    }

    public String getType()
    {
        return _type;
    }

    public void setInitialValue(String initialValue)
    {
        _initialValue = initialValue;
    }

	/**
	 *  Sets the name of the property.  This should not be changed
	 *  once this PropertySpecification is added to
	 *  a {@link net.sf.tapestry.spec.ComponentSpecification}.
	 * 
	 **/
	
    public void setName(String name)
    {
        _name = name;
    }

    public void setPersistent(boolean persistant)
    {
        _persistent = persistant;
    }

    public void setType(String type)
    {
        _type = type;
    }

}
