package net.sf.tapestry.spec;

/**
 *  Defines a formal parameter to a component.  A <code>ParameterSpecification</code>
 *  is contained by a {@link ComponentSpecification}.
 *
 *  <p>TBD: Identify arrays in some way.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ParameterSpecification
{
    private boolean required = false;
    private String type;

    /** @since 1.0.9 **/
    private String description;
    
    /** @since 2.0.3 **/
    private String propertyName;
    
	private Direction direction = Direction.CUSTOM;
	
    /**
     *  Returns the class name of the expected type of the parameter.  The default value
     *  is <code>java.lang.Object</code> which matches anything.
     *
     **/

    public String getType()
    {
        return type;
    }

    /**
     *  Returns true if the parameter is required by the component.
     *  The default is false, meaning the parameter is optional.
     *
     **/

    public boolean isRequired()
    {
        return required;
    }

    public void setRequired(boolean value)
    {
        required = value;
    }

	/**
	 *  Sets the type of value expected for the parameter.  This can be
	 *  left blank to indicate any type.
	 * 
	 **/
	
    public void setType(String value)
    {
        type = value;
    }

    /**
     *  Returns the documentation for this parameter.
     * 
     *  @since 1.0.9
     * 
     **/

    public String getDescription()
    {
        return description;
    }

    /**
     *  Sets the documentation for this parameter.
     * 
     *  @since 1.0.9
     *    	 
     **/

    public void setDescription(String description)
    {
        this.description = description;
    }
    
    /**
     *  Sets the property name (of the component class)
     *  to connect the parameter to.
     * 
     **/
    
    public void setPropertyName(String propertyName)
    {
        this.propertyName = propertyName;
    }
    
    /**
     *  Returns the name of the JavaBeans property to connect the
     *  parameter to.
     * 
     **/
    
    public String getPropertyName()
    {
       return propertyName;
    }

	/**
	 *  Returns the parameter value direction, defaulting to {@link Direction#CUSTOM}
	 *  if not otherwise specified.
	 * 
	 **/
	
    public Direction getDirection()
    {
        return direction;
    }

    public void setDirection(Direction direction)
    {
        if (direction == null)
        	throw new IllegalArgumentException("direction may not be null.");
        	
        this.direction = direction;
    }

}