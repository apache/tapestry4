package net.sf.tapestry;

import net.sf.tapestry.spec.ComponentSpecification;

/**
 * Interface exposed to components as they are loaded by the page loader.
 *
 * @see IComponent#finishLoad(IRequestCycle, IPageLoader, ComponentSpecification)
 * 
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public interface IPageLoader
{
	/**
	 *  Returns the engine for which this page loader is curently
	 *  constructing a page.
	 *
	 *  @since 0.2.12
     * 
	 **/

	public IEngine getEngine();

	/**
	 *  A convienience; returns the template source provided by
	 *  the {@link IEngine engine}.
	 *
	 *  @since 0.2.12
     * 
	 **/

	public ITemplateSource getTemplateSource();
    
    /**
     *  Invoked to create an implicit component (one which is defined in the
     *  containing component's template, rather that in the containing component's
     *  specification).
     * 
     *  @see net.sf.tapestry.BaseComponentTemplateLoader
     *  @since NEXT_RELEASE
     * 
     **/

    public IComponent createImplicitComponent(
        IRequestCycle cycle,
        IComponent container,
        String componentId,
        String componentType)
        throws PageLoaderException;    
}