package net.sf.tapestry.vlib;

import java.util.ArrayList;
import java.util.List;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.form.IPropertySelectionModel;

/**
 *  This class is used as a property selection model to select a primary key.
 *  We assume that the primary keys are integers, which makes it easy to
 *  translate between the various representations.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class EntitySelectionModel implements IPropertySelectionModel
{
    private static class Entry
    {
        Integer primaryKey;
        String label;

        Entry(Integer primaryKey, String label)
        {
            this.primaryKey = primaryKey;
            this.label = label;
        }

    }

    private static final int LIST_SIZE = 20;

    private List entries = new ArrayList(LIST_SIZE);

    public void add(Integer key, String label)
    {
        Entry entry;

        entry = new Entry(key, label);
        entries.add(entry);
    }

    public int getOptionCount()
    {
        return entries.size();
    }

    private Entry get(int index)
    {
        return (Entry) entries.get(index);
    }

    public Object getOption(int index)
    {
        return get(index).primaryKey;
    }

    public String getLabel(int index)
    {
        return get(index).label;
    }

    public String getValue(int index)
    {
        Integer primaryKey;

        primaryKey = get(index).primaryKey;

        if (primaryKey == null)
            return "";

        return primaryKey.toString();
    }

    public Object translateValue(String value)
    {
        if (value.equals(""))
            return null;

        try
        {
            return new Integer(value);
        }
        catch (NumberFormatException e)
        {
            throw new ApplicationRuntimeException("Could not convert '" + value + "' to an Integer.", e);
        }
    }
}