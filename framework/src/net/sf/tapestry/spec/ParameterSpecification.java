//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

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