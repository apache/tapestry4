package org.apache.tapestry.timetracker.page;

import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.scriptaculous.ListItemRenderer;
import org.apache.tapestry.scriptaculous.Suggest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Demonstration of using the {@link org.apache.tapestry.scriptaculous.Suggest} component.
 */
public abstract class LocaleSuggest extends BasePage {

    static ListItemRenderer ITEM_RENDERER = new LocaleListItemRenderer();

    @Component(bindings = {"value=inputLocale",
            "displayName=literal:Locale",
            "listSource=localeList",
            "listener=listener:suggestLocale",
            "listItemRenderer=itemRenderer"})
    public abstract Suggest getSuggest();

    public abstract String getInputLocale();

    public ListItemRenderer getItemRenderer()
    {
        return ITEM_RENDERER;
    }

    public abstract String getFilterString();
    public abstract void setFilterString(String filter);
    
    public List getLocaleList()
    {
        String filter = getFilterString();
        Locale[] locales = Locale.getAvailableLocales();

        if (filter == null || filter.length() < 1)
        {
            return Arrays.asList(locales);
        }

        filter = filter.toUpperCase();

        List<Locale> ret = new ArrayList<Locale>();
        List temp = new ArrayList();

        for (Locale locale : locales)
        {
            if (locale.getDisplayCountry().toUpperCase().indexOf(filter) > -1
                    && !temp.contains(locale.getDisplayCountry()))
            {
                ret.add(locale);
                temp.add(locale.getDisplayCountry());
            }
        }

        return ret;
    }
    
    public void suggestLocale(String filter)
    {
        setFilterString(filter);
    }
}
