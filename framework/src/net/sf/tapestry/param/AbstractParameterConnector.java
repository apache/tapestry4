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

package net.sf.tapestry.param;

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.RequiredParameterException;
import net.sf.tapestry.spec.ParameterSpecification;
import net.sf.tapestry.util.prop.PropertyHelper;

/**
 *  Standard implementation of {@link IParameterConnector}.
 *  Subclasses add in the ability to clear parameters.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.3
 * 
 **/

public abstract class AbstractParameterConnector implements IParameterConnector
{
    private String parameterName;
    private String propertyName;
    private IBinding binding;
    private IComponent component;
    private boolean required;
    private Object clearValue;

	/**
	 *  Creates a connector.  In addition, obtains the current value
	 *  of the component property; this value will be used to
	 *  restore the component property.
	 * 
	 **/
	
    protected AbstractParameterConnector(
        IComponent component,
        String parameterName,
        IBinding binding)
    {
        this.component = component;
        this.parameterName = parameterName;
        this.binding = binding;

		ParameterSpecification pspec = component.getSpecification().getParameter(parameterName);
        required = pspec.isRequired();
        propertyName = pspec.getPropertyName();
        
        PropertyHelper helper = PropertyHelper.forInstance(component);
        clearValue = helper.get(component, propertyName);
    }


	/**
	 *  Sets the property of the component to the specified value.
	 * 
	 **/
	
	protected void setPropertyValue(Object value)
	{
	    PropertyHelper helper = PropertyHelper.forInstance(component);
	    
	    helper.set(component, propertyName, value);
	}


	/**
	 *  Gets the value of the binding.
	 *  @param requiredType if not null, the expected type of the value object.
	 * 
	 *  @throws 	RequiredParameterException if the value object is null,
	 *  but the parameter is required.
	 * 
	 *  @see IBinding#getObject()
	 *  @see IBinding#getObject(String, Class)
	 **/
	
	protected Object getBindingValue(Class requiredType)
	throws RequiredParameterException
	{
	    Object result;
	    
	    if (requiredType == null)
	    	result = binding.getObject();
	    else
		    result = binding.getObject(parameterName, requiredType);
	    	
	    if (result == null && required)
	    	throw new RequiredParameterException(component, parameterName, binding);
	    	
	    return result;
	}

	protected IBinding getBinding()
	{
	    return binding;
	}

	public String toString()
	{
	    StringBuffer buffer = new StringBuffer(super.toString());
	    buffer.append('[');
	    buffer.append(component.getExtendedId());
	    buffer.append(' ');
	    buffer.append(parameterName);
	    buffer.append(' ');
	    buffer.append(binding);
	    
	    if (required)
	    	buffer.append(" required");
	    	
	    buffer.append(']');
	    
	    return buffer.toString();
	}
	
	/**
	 *  Restores the property to its default value.
	 * 
	 **/
	
    public void clearParameter()
    {
        setPropertyValue(clearValue);
    }

}