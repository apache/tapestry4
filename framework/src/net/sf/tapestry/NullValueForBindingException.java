package net.sf.tapestry;

/**
 *  Indicates that an {@link IBinding} could not retrieve a
 *  non-null value.  This is used when coercing certain the value to
 *  another type, such as <code>int</code>.
 *
 * @see IBinding
 *
 * @author Howard Lewis Ship
 * @version $Id$
 */

public class NullValueForBindingException extends BindingException
{
	public NullValueForBindingException(IBinding binding)
	{
		super(binding);
	}
}