package net.sf.tapestry.spec;

import org.apache.commons.lang.enum.Enum;

/**
 *  Defines the different types of bindings possible for a component.
 *  These are used in the {@link BindingSpecification} and ultimately
 *  used to create an instance of {@link net.sf.tapestry.IBinding}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public final class BindingType extends Enum
{
    /**
     *  Indicates a {@link net.sf.tapestry.binding.StaticBinding}.
     *
     **/

    public static final BindingType STATIC = new BindingType("STATIC");

    /**
     *  Indicates a standard {@link net.sf.tapestry.binding.ExpressionBinding}.
     *
     **/

    public static final BindingType DYNAMIC = new BindingType("DYNAMIC");

    /**
     *  Indicates that an existing binding (from the container) will be
     *  re-used.
     *
     **/

    public static final BindingType INHERITED = new BindingType("INHERITED");

    /**
     *  Indicates a {@link net.sf.tapestry.binding.FieldBinding}.
     *
     *  <p>
     *  Field bindings are only available in the 1.3 DTD.  The 1.4 DTD
     *  does not support them (since OGNL expressions can do the same thing).
     * 
     **/

    public static final BindingType FIELD = new BindingType("FIELD");

    /**
     *  Indicates a {@link net.sf.tapestry.binding.ListenerBinding}, a
     *  specialized kind of binding that encapsulates a component listener
     *  as a script.  Uses a subclass of {@link BindingSpecification},
     *  {@link ListenerBindingSpecification}.
     * 
     *  @since 2.4
     * 
     **/
    
    public static final BindingType LISTENER = new BindingType("LISTENER");

	/**
	 *  A binding to one of a component's localized strings.
	 * 
	 *  @see net.sf.tapestry.IComponent#getString(String)
	 * 
	 *  @since 2.0.4
	 * 
	 **/
	
	public static final BindingType STRING = new BindingType("STRING");
	
    private BindingType(String name)
    {
        super(name);
    }

}