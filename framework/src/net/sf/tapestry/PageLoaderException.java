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
    private Throwable rootCause;
    private String pageName;
    private transient IComponent component;

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

        this.rootCause = rootCause;
        this.component = component;

        if (component != null)
        {
            IPage page = component.getPage();

            if (page != null)
                pageName = page.getName();
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

        this.pageName = pageName;
    }

    public IComponent getComponent()
    {
        return component;
    }

    public String getPageName()
    {
        return pageName;
    }

    public Throwable getRootCause()
    {
        return rootCause;
    }
}