package net.sf.tapestry.link;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  An implementation of {@link net.sf.tapestry.components.IServiceLink} 
 *  that allows
 *  the exact HREF to be specified, usually used for client side
 *  scripting.  
 * 
 *  [<a href="../../../../../ComponentReference/GenericLink.html">Component Reference</a>]
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.2
 * 
 **/

public class GenericLink extends AbstractServiceLink
{
    private String href;

    /**
     *  Returns the String specified by the href binding (this method is invoked
     *  while renderring).
     * 
     *  @throws RequiredParameterException if no href value was supplied.
     * 
     **/

    protected String getURL(IRequestCycle cycle) throws RequestCycleException
    {
        return href;
    }

    public String getHref()
    {
        return href;
    }

    public void setHref(String href)
    {
        this.href = href;
    }

}