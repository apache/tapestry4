package tutorial.border;

import net.sf.tapestry.engine.BaseEngine;

/**
 * 
 *  Engine for the Border tutorial.  Provides the list of page names used
 *  by the {@link Border} component.
 * 
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class BorderEngine extends BaseEngine
{
    private static final String[] PAGE_NAMES = { "Home", "Credo", "Legal" };

    public String[] getPageNames()
    {
        return PAGE_NAMES;
    }

}