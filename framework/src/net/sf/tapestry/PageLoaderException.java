package net.sf.tapestry;

/**
 *  Encapsulates exceptions that occur when loading a page and its components.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class PageLoaderException extends Exception
{
    private Throwable _rootCause;
    private String _pageName;
    private transient IComponent _component;

    /** @since NEXT_RELEASE **/
    
    public PageLoaderException(String message)
    {
        this(message, (IComponent)null, null);
    }

    /**
     * @since 0.2.12
     *
     **/

    public PageLoaderException(String message, Throwable rootCause)
    {
        this(message, (IComponent) null, rootCause);
    }

    /**
     * @since 0.2.12
     *
     **/

    public PageLoaderException(String message, IComponent component, Throwable rootCause)
    {
        super(message);

        _rootCause = rootCause;
        _component = component;

        if (component != null)
        {
            IPage page = component.getPage();

            if (page != null)
                _pageName = page.getName();
        }

    }

    /**
     * @since 0.2.12
     *
     **/

    public PageLoaderException(String message, IComponent component)
    {
        this(message, component, null);
    }

    /**
     * @since 0.2.12
     *
     **/

    public PageLoaderException(String message, String pageName, Throwable rootCause)
    {
        this(message, (IComponent) null, rootCause);

        _pageName = pageName;
    }

    public IComponent getComponent()
    {
        return _component;
    }

    public String getPageName()
    {
        return _pageName;
    }

    public Throwable getRootCause()
    {
        return _rootCause;
    }
}