package net.sf.tapestry;

/**
 *  Exception thown when an {@link IBinding} required by a component does not
 *  exist, or when the value for the binding is null (and the component
 *  requires a non-null value).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class RequiredParameterException extends RequestCycleException
{
	private String parameterName;
	private transient IBinding binding;

	public RequiredParameterException(
		IComponent component,
		String parameterName,
		IBinding binding)
	{
		super(
			Tapestry.getString(
				"RequiredParameterException.message",
				parameterName,
				component.getExtendedId()),
			component);

		this.parameterName = parameterName;
		this.binding = binding;
	}

	public String getParameterName()
	{
		return parameterName;
	}

	public IBinding getBinding()
	{
		return binding;
	}
}