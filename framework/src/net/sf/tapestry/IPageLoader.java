package net.sf.tapestry;

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
}