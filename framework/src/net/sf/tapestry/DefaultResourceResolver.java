package net.sf.tapestry;

import java.net.URL;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Default implementation of {@link net.sf.tapestry.IResourceResolver} based
 *  around {@link Thread#getContextClassLoader()} (which is set by the
 *  servlet container).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 * 
 **/

public class DefaultResourceResolver implements IResourceResolver
{
    private static final Log LOG = LogFactory.getLog(DefaultResourceResolver.class);

    private ClassLoader _loader;

    /**
     *  Constructs a new instance using
     *  {@link Thread#getContextClassLoader()}.
     * 
     **/
    
    public DefaultResourceResolver()
    {
        this(Thread.currentThread().getContextClassLoader());
    }

    public DefaultResourceResolver(ClassLoader loader)
    {
        _loader = loader;
    }

    public URL getResource(String name)
    {
        boolean debug = LOG.isDebugEnabled();

        if (debug)
            LOG.debug("getResource(" + name + ")");

        String stripped = removeLeadingSlash(name);

        URL result = _loader.getResource(stripped);

        if (debug)
        {
            if (result == null)
                LOG.debug("Not found.");
            else
                LOG.debug("Found as " + result);
        }

        return result;
    }

    private String removeLeadingSlash(String name)
    {
        if (name.startsWith("/"))
            return name.substring(1);

        return name;
    }

    /**
     *  Invokes {@link Class#forName(java.lang.String, boolean, java.lang.ClassLoader)}.
     *  
     *  @param name the complete class name to locate and load
     *  @return The loaded class
     *  @throws ApplicationRuntimeException if loading the class throws an exception
     *  (typically  {@link ClassNotFoundException} or a security exception)
     * 
     **/
    
    public Class findClass(String name)
    {
        try
        {
            return Class.forName(name, true, _loader);
        }
        catch (Throwable t)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString("ResourceResolver.unable-to-load-class", name, _loader, t.getMessage()),
                t);
        }
    }

    /**
     * 
     *  OGNL Support for dynamic class loading.  Simply invokes {@link #findClass(String)}.
     * 
     **/
    
    public Class classForName(String name, Map map) throws ClassNotFoundException
    {
        return findClass(name);
    }

    /** @since 2.4 **/
    
    public ClassLoader getClassLoader()
    {
        return _loader;
    }

}
