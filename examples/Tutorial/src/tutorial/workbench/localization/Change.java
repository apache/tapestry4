package tutorial.workbench.localization;

import java.util.Locale;

import net.sf.tapestry.html.BasePage;

/**
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class Change extends BasePage
{
    private String localeName;

    public String getLocaleName()
    {
        if (localeName == null)
        {
            Locale locale = getLocale();

            localeName = locale.getDisplayName(locale);
        }

        return localeName;
    }
}