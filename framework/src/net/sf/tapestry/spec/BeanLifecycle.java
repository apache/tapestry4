package net.sf.tapestry.spec;

import org.apache.commons.lang.enum.Enum;


/**
 *  An {@link Enum} of the different possible lifecycles for a JavaBean.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.4
 * 
 **/

public class BeanLifecycle extends Enum
{
	/**
	 *  No lifecycle; the bean is created fresh on each reference and not retained.
	 *
	 **/

	public static final BeanLifecycle NONE = new BeanLifecycle("NONE");

	/**
	 * The standard lifecycle; the bean is retained for the
	 * duration of the request cycle and is discarded at the end of the
	 * request cycle.
	 *
	 **/

	public static final BeanLifecycle REQUEST = new BeanLifecycle("REQUEST");

	/**
	 * The bean is created once and reused for the lifespan of the page
	 * containing the component.
	 *
	 **/

	public static final BeanLifecycle PAGE = new BeanLifecycle("PAGE");

    /**
     *  The bean is create and reused until the end of the current render,
     *  at which point it is discarded.
     * 
     *  @since 2.2
     * 
     **/
    
    public static final BeanLifecycle RENDER = new BeanLifecycle("RENDER");
    
	private BeanLifecycle(String name)
	{
		super(name);
	}

}