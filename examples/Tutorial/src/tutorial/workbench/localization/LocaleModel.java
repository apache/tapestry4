package tutorial.workbench.localization;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sf.tapestry.form.IPropertySelectionModel;

/**
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class LocaleModel implements IPropertySelectionModel
{
    private Locale locale;
    private List locales = new ArrayList();

    public LocaleModel(Locale locale)
    {
        this.locale = locale;
    }

    public void add(Locale locale)
    {
        locales.add(locale);
    }

    private Locale get(int index)
    {
        return (Locale) locales.get(index);
    }

    public String getLabel(int index)
    {
        return get(index).getDisplayName(locale);
    }

    public int getOptionCount()
    {
        return locales.size();
    }

    public Object getOption(int index)
    {
        return locales.get(index);
    }

    /**
     *  Returns the String version of the integer index.
     *
     **/

    public String getValue(int index)
    {
        return Integer.toString(index);
    }

    public Object translateValue(String value)
    {
        int index = Integer.parseInt(value);

        return locales.get(index);
    }
}