package net.sf.tapestry;

/**
 *  Exception thrown when an {@link IComponent} is unable to render.  Often, there
 *  is an underlying exception.  This is a checked exception and part
 *  of the {@link net.sf.tapestry.IRender#render(IMarkupWriter, IRequestCycle)}
 *  signature so as to enforce uniformity of exception reporting.  Where
 *  this exception is not usable or appropriate,
 *  {@link net.sf.tapestry.ApplicationRuntimeException} if typically
 *  used.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class RequestCycleException extends Exception
{
	private transient IComponent _component;
	private Throwable _rootCause;

	public RequestCycleException()
	{
		super();
	}

	public RequestCycleException(String message)
	{
		super(message);
	}

	public RequestCycleException(String message, IComponent component)
	{
		this(message, component, null);
	}

	public RequestCycleException(
		String message,
		IComponent component,
		Throwable rootCause)
	{
		super(message);

		_component = component;
		_rootCause = rootCause;
	}

	/**
	 *  @since 0.2.9
	 *
	 **/

	public RequestCycleException(IComponent component, Throwable rootCause)
	{
		this(rootCause.getMessage(), component, rootCause);
	}

	public IComponent getComponent()
	{
		return _component;
	}

	public Throwable getRootCause()
	{
		return _rootCause;
	}
}