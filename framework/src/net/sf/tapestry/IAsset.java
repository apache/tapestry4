package net.sf.tapestry;

import java.io.InputStream;
import java.util.Locale;

/**
 *  Representation of a asset (GIF, JPEG, etc.) that may be owned by a
 *  {@link IComponent}.
 *
 *  <p>Assets may be completely external (i.e., on some other web site), 
 *  contained by the {@link javax.servlet.ServletContext},  
 *  or stored somewhere in the classpath.
 *
 *  <p>In the latter two cases, the resource may be localized.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public interface IAsset
{
    /**
     *  Returns a URL for the asset, ready to be inserted into the output HTML.
     *  If the asset can be localized, the localized version (matching the
     *  {@link java.util.Locale} of the current {@link IPage page}) is returned.
     *
     **/

    public String buildURL(IRequestCycle cycle);

    /**
     *  Accesses the localized version of the resource (if possible) and returns it as
     *  an input stream.  A version of the resource localized to the
     *  current {@link IPage page} is returned.
     *
     **/

    public InputStream getResourceAsStream(IRequestCycle cycle);
    
    /**
     *  Accesses the localized version of the resource (if possible) and
     *  returns it as an input stream.
     * 
     *  @since 2.2
     * 
     **/
    
    public InputStream getResourceAsStream(IRequestCycle cycle, Locale locale);
}