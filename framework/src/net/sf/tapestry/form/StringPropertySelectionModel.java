package net.sf.tapestry.form;

/**
 *  Implementation of {@link IPropertySelectionModel} that allows one String from
 *  an array of Strings to be selected as the property.
 *
 *  <p>Uses a simple index number as the value (used to represent the selected String).
 *  This assumes that the possible values for the Strings will remain constant between
 *  request cycles.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 * 
 **/

public class StringPropertySelectionModel implements IPropertySelectionModel
{
    private String[] options;

    /**
     * Standard constructor.
     *
     * The options are retained (not copied).
     **/

    public StringPropertySelectionModel(String[] options)
    {
        this.options = options;
    }

    public int getOptionCount()
    {
        return options.length;
    }

    public Object getOption(int index)
    {
        return options[index];
    }

    /**
     *  Labels match options.
     *
     **/

    public String getLabel(int index)
    {
        return options[index];
    }

    /**
     *  Values are indexes into the array of options.
     *
     **/

    public String getValue(int index)
    {
        return Integer.toString(index);
    }

    public Object translateValue(String value)
    {
        int index;

        index = Integer.parseInt(value);

        return options[index];
    }

}