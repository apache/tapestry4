package net.sf.tapestry.resource;

import java.net.URL;
import java.util.Locale;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import net.sf.tapestry.IAsset;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IResourceLocation;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.asset.PrivateAsset;
import net.sf.tapestry.util.LocalizedResourceFinder;

/**
 *  Implementation of {@link net.sf.tapestry.IResourceLocation}
 *  for resources found within the classpath.
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class ClasspathResourceLocation extends AbstractResourceLocation
{
    private IResourceResolver _resolver;

    public ClasspathResourceLocation(IResourceResolver resolver, String path)
    {
        super(path);
        
        _resolver = resolver;
    }

    /**
     *  Locates the localization of the
     *  resource using {@link net.sf.tapestry.util.LocalizedResourceFinder}
     * 
     **/

    public IResourceLocation getLocalization(Locale locale)
    {
        if (locale == null)
            return this;

        String path = getPath();
        LocalizedResourceFinder finder = new LocalizedResourceFinder(_resolver);

        String localizedPath = finder.resolve(path, locale);

        if (path.equals(localizedPath))
            return this;

        return new ClasspathResourceLocation(_resolver, localizedPath);
    }

    /**
     *  Invokes {@link IResourceResolver#getResource(String)
     * 
     **/

    public URL getResourceURL()
    {
        return _resolver.getResource(getPath());
    }


    /**
     *  Returns a {@link net.sf.tapestry.asset.PrivateAsset}.
     * 
     **/

    public IAsset toAsset()
    {
        return new PrivateAsset(getPath());
    }


    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder(4783, 23);

        builder.append(getPath());

        return builder.toHashCode();
    }

    protected IResourceLocation buildNewResourceLocation(String path)
    {
        return new ClasspathResourceLocation(_resolver, path);
    }

}
