package net.sf.tapestry.junit.mock.app;

import java.util.Locale;

import net.sf.tapestry.*;
import net.sf.tapestry.html.BasePage;

/**
 *  Provides functionality to switch the locale to French.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class Assets extends BasePage
{
    public void selectFrench(IRequestCycle cycle)
    {
        IEngine engine = cycle.getEngine();
        
        engine.setLocale(Locale.FRENCH);
        
        // Currently, when you change locale there is no way to
        // reload the current page.
        
        cycle.setPage("Home");
    }
}
