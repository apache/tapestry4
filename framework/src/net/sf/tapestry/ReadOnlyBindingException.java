package net.sf.tapestry;

/**
 *  Thrown when an attempt is made to update through a read-only binding (i.e,
 *  {@link StaticBinding}.
 *
 *  @see IBinding
 *
 * @author Howard Lewis Ship
 * @version $Id$
 **/

public class ReadOnlyBindingException extends BindingException
{
	public ReadOnlyBindingException(IBinding binding)
	{
		super(binding);
	}

	public ReadOnlyBindingException(String message, IBinding binding)
	{
		super(message, binding);
	}
}