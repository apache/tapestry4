package net.sf.tapestry;

/**
 *  A special subclass of {@link RequestCycleException} that can be thrown
 *  when a component has determined that the state of the page has been
 *  rewound.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 **/

public class RenderRewoundException extends RequestCycleException
{
	public RenderRewoundException(IComponent component)
	{
		super(null, component);
	}
}