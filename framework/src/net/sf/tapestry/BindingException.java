package net.sf.tapestry;

/**
 *  A general exception describing an {@link IBinding}
 *  and an {@link IComponent}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class BindingException extends RuntimeException
{
    private transient IBinding _binding;
    private Throwable _rootCause;

    public BindingException(IBinding binding)
    {
        _binding = binding;
    }

    public BindingException(String message, IBinding binding)
    {
        super(message);

        _binding = binding;
    }

    public BindingException(String message, IBinding binding, Throwable rootCause)
    {
        super(message);

        _binding = binding;
        _rootCause = rootCause;
    }

    public IBinding getBinding()
    {
        return _binding;
    }

    public Throwable getRootCause()
    {
        return _rootCause;
    }
}