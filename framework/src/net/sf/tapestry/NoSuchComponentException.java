package net.sf.tapestry;

/**
 *  A runtime exception thrown when an {@link IComponent} is asked for a contained
 *  component that does not exist.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 *
 **/

public class NoSuchComponentException extends RuntimeException
{
	private String componentId;
	private transient IComponent container;

	public NoSuchComponentException(String componentId, IComponent container)
	{
		super(
			Tapestry.getString(
				"NoSuchComponentException.message",
				container.getExtendedId(),
				componentId));

		this.componentId = componentId;
		this.container = container;
	}

	public IComponent getContainer()
	{
		return container;
	}

	public String getComponentId()
	{
		return componentId;
	}
}