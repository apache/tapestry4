package net.sf.tapestry.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.servlet.ServletContext;

/**
 *  Finds localized resources within the web application context.
 * 
 *  @see javax.servlet.ServletContext
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class LocalizedContextResourceFinder
{
    private ServletContext _context;
    
    public LocalizedContextResourceFinder(ServletContext context)
    {
        _context = context;
    }
    
    /**
     *  Resolves the resource, returning a path representing
     *  the closest match (with respect to the provided locale).
     *  Returns null if no match.
     * 
     *  <p>The provided path is split into a base path
     *  and a suffix (at the last period character).  The locale
     *  will provide different suffixes to the base path
     *  and the first match is returned.
     * 
     **/
    
    public String resolve(String contextPath, Locale locale)
    {
        int dotx = contextPath.lastIndexOf('.');
        String basePath = contextPath.substring(0, dotx);
        String suffix = contextPath.substring(dotx);

        LocalizedNameGenerator generator = new LocalizedNameGenerator(basePath, locale, suffix);

        while (generator.more())
        {
            String candidatePath = generator.next();

            if (isExistingResource(candidatePath))
                return candidatePath;
         }

        return null;
    }

    private boolean isExistingResource(String path)
    {
        try
        {
            return _context.getResource(path) != null;
        }
        catch (MalformedURLException ex)
        {
            return false;
        }
    }
}
