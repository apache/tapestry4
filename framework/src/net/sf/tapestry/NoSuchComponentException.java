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
	private String _componentId;
	private transient IComponent _container;

	public NoSuchComponentException(String componentId, IComponent container)
	{
		super(
			Tapestry.getString(
				"NoSuchComponentException.message",
				container.getExtendedId(),
				componentId));

		_componentId = componentId;
		_container = container;
	}

	public IComponent getContainer()
	{
		return _container;
	}

	public String getComponentId()
	{
		return _componentId;
	}
}