package tutorial.border;

import net.sf.tapestry.engine.BaseEngine;

/**
 * 
 *  Engine for the Border tutorial.  Provide the list of page names used
 *  by the {@link Border} component.
 * 
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class BorderEngine extends BaseEngine
{
    private static final String[] pageNames = { "Home", "Credo", "Legal" };

    public String[] getPageNames()
    {
        return pageNames;
    }

}