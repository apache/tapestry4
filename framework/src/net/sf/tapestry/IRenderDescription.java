package net.sf.tapestry;

/**
 *  An object which may render a description of itself, which is used in debugging
 *  (i.e., by the Inspector).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.6
 * 
 **/

public interface IRenderDescription
{
	/**
	 *  Object should render a description of itself to the writer.
	 *
	 **/

	public void renderDescription(IMarkupWriter writer);
}