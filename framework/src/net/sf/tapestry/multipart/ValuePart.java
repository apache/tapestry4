package net.sf.tapestry.multipart;

import java.util.ArrayList;
import java.util.List;

/**
 *  A portion of a multipart request that stores a value, or values, for
 *  a parameter.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.1
 *
 **/

public class ValuePart implements IPart
{
    private int count;
    // Stores either String or List of String
    private Object value;

    public ValuePart(String value)
    {
        count = 1;
        this.value = value;
    }

    public int getCount()
    {
        return count;
    }

    /**
     *  Returns the value, or the first value (if multi-valued).
     * 
     **/

    public String getValue()
    {
        if (count == 1)
            return (String) value;

        List l = (List) value;

        return (String) l.get(0);
    }

    /**
     *  Returns the values as an array of strings.  If there is only one value,
     *  it is returned wrapped as a single element array.
     * 
     **/

    public String[] getValues()
    {
        if (count == 1)
            return new String[] {(String) value };

        List l = (List) value;

        return (String[]) l.toArray(new String[count]);
    }

    public void add(String newValue)
    {
        if (count == 1)
        {
            List l = new ArrayList();
            l.add(value);
            l.add(newValue);

            value = l;
            count++;
            return;
        }

        List l = (List) value;
        l.add(newValue);
        count++;
    }

    /**
     *  Does nothing.
     * 
     **/

    public void cleanup()
    {
    }
}