package net.sf.tapestry;

import java.util.Collection;

/**
 *  An object that provides a component with access to helper beans.
 *  Helper beans are JavaBeans associated with a page or component
 *  that are used to extend the functionality of the component via
 *  aggregation.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.4
 **/


public interface IBeanProvider
{
	/**
	 *  Returns the JavaBean with the specified name.  The bean is created as needed.
	 *
	 *  @throws ApplicationRuntimeException if no such bean is available.
	 *
	 **/
	
	public Object getBean(String name);
	
	/**
	 *  Returns the {@link IComponent} (which may be a 
	 *  {@link net.sf.tapestry.IPage}) for which
	 *  this bean provider is providing beans.
	 *
	 *  @since 1.0.5
	 **/
	
	public IComponent getComponent();
	
	/**
	 *  Returns a collection of the names of any beans which may
	 *  be provided.
	 *
	 *  @since 1.0.6
	 *  @see net.sf.tapestry.spec.ComponentSpecification#getBeanNames()
	 *
	 **/
	
	public Collection getBeanNames();
	
    /**
     *  Returns true if the provider can provide the named bean.
     * 
     *  @since 2.2
     * 
     **/
    
    public boolean canProvideBean(String name);
    
	/**
	 *  Returns a resource resolver.
	 * 
	 *  @since 1.0.8
	 * 
	 **/
	
	public IResourceResolver getResourceResolver();
	
}

