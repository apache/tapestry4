package net.sf.tapestry.util;

import java.util.Locale;

import net.sf.tapestry.IResourceResolver;

/**
 *  
 *  Searches for a localization of a
 *  particular resource in the classpath (using
 *  a {@link net.sf.tapestry.IResourceResolver}. 
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class LocalizedResourceFinder
{
    private IResourceResolver _resolver;

    public LocalizedResourceFinder(IResourceResolver resolver)
    {
        _resolver = resolver;
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
    
    public String resolve(String resourcePath, Locale locale)
    {
        int dotx = resourcePath.lastIndexOf('.');
        String basePath = resourcePath.substring(0, dotx);
        String suffix = resourcePath.substring(dotx);

        LocalizedNameGenerator generator = new LocalizedNameGenerator(basePath, locale, suffix);

        while (generator.more())
        {
            String candidatePath = generator.next();

            if (_resolver.getResource(candidatePath) != null)
                return candidatePath;
        }

        return null;
    }
}
