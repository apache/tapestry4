package net.sf.tapestry.resource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.IAsset;
import net.sf.tapestry.IResourceLocation;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.asset.ContextAsset;
import net.sf.tapestry.util.LocalizedContextResourceFinder;

/**
 *  Implementation of {@link net.sf.tapestry.IResourceLocation}
 *  for resources found within the web application context.
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class ContextResourceLocation extends AbstractResourceLocation
{
    private static final Log LOG = LogFactory.getLog(ContextResourceLocation.class);

    private ServletContext _context;

    public ContextResourceLocation(ServletContext context, String path)
    {
        super(path);
        
        _context = context;
    }

    /**
     *  Locates the resource using {@link LocalizedContextResourceFinder}
     *  and {@link ServletContext#getResource(java.lang.String)}.
     * 
     **/

    public IResourceLocation getLocalization(Locale locale)
    {
        if (locale == null)
            return this;

        LocalizedContextResourceFinder finder = new LocalizedContextResourceFinder(_context);

        String path = getPath();
        String localizedPath = finder.resolve(path, locale);

        if (path.equals(localizedPath))
            return this;

        return new ContextResourceLocation(_context, localizedPath);
    }

    public URL getResourceURL()
    {
        try
        {
            return _context.getResource(getPath());
        }
        catch (MalformedURLException ex)
        {
            LOG.warn(Tapestry.getString("ContextResourceLocation.unable-to-reference-context-path", 
            getPath()), ex);

            return null;
        }
    }

    /**
     *  Returns a new {@link net.sf.tapestry.asset.ContextAsset}.
     * 
     **/

    public IAsset toAsset()
    {
        return new ContextAsset(getPath());
    }
    

    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder(3265, 143);

        builder.append(getPath());

        return builder.toHashCode();
    }    
    
    protected IResourceLocation buildNewResourceLocation(String path)
    {
        return new ContextResourceLocation(_context, path);
    }

}
