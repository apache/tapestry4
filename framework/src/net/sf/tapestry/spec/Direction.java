package net.sf.tapestry.spec;

import org.apache.commons.lang.enum.Enum;

import net.sf.tapestry.Tapestry;

/**
 *  Represents different types of parameters.  Currently only 
 *  in and custom are supported, but this will likely change
 *  when Tapestry supports out parameters is some form (that reflects
 *  form style processing).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.3
 *
 **/
 
 
public class Direction extends Enum
{
    /**
     *  The parameter value is input only; the component property value
     *  is unchanged or not relevant after the component renders.
     *  The property is set from the binding before the component renders,
     *  then reset to initial value after the component renders.
     * 
     **/
    
	public static final Direction IN = new Direction("IN");
	
    
    /**
     *  Encapsulates the semantics of a form component's value parameter.
     * 
     *  <p>The parameter is associated with a {@link net.sf.tapestry.form.IFormComponent}.
     *  The property value is set from the binding before the component renders (when renderring,
     *  but not when rewinding).
     *  The binding is updated from the property value
     *  after after the component renders when the
     *  <b>containing form</b> is <b>rewinding</b>, <i>and</i>
     *  the component is not {@link net.sf.tapestry.form.IFormComponent#isDisabled() disabled}.
     * 
     *  @since 2.2
     * 
     **/

    public static final Direction FORM = new Direction("FORM");    
    
	/**
	 *  Processing of the parameter is entirely the responsibility
	 *  of the component, which must obtain an manipulate
	 *  the {@link net.sf.tapestry.IBinding} (if any) for the parameter.
	 * 
	 **/
	
	public static final Direction CUSTOM = new Direction("CUSTOM");


    /**
     *  Encapsulates the semantics of a component parameter whose value 
     *  is automatically cached.
     * 
     *  <p>The component must define an abstract getter and possibly an
     *  abstract setter for the property defined by the parameter.
     *  Tapestry will automatically create implementations for those 
     *  methods. They will operate in the following way:
     * 
     *  <ul>
     *  <li>The get method of the property will get the value from the
     *  binding upon the first request and will cache it. Further 
     *  invocations of that method will return the cached value. 
     *  Cached values are discarded when the page is detached.
     * 
     *  <li>The set method (if implemented) will set the value in the 
     *  binding and will update the cache.
     * 
     *  @since 2.3
     * 
     **/

    public static final Direction CACHED = new Direction("CACHED");    
    
    protected Direction(String name)
    {
        super(name);
    }

    /**
     *  Returns a user-presentable name for the direction.
     * 
     **/
    
    public String getDisplayName()
    {
        return Tapestry.getString("Direction." + getName());
    }
}
