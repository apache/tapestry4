package net.sf.tapestry.contrib.palette;

import org.apache.commons.lang.enum.Enum;

/**
 *  Defines different sorting strategies for the {@link Palette} component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class SortMode extends Enum
{
    /**
     *  Sorting is not relevant and no sort controls should be visible.
     *
     **/

    public static final SortMode NONE = new SortMode("NONE");

    /**
     * Options should be sorted by their label.
     *
     **/

    public static final SortMode LABEL = new SortMode("LABEL");

    /**
     *  Options should be sorted by thier value.
     *
     **/

    public static final SortMode VALUE = new SortMode("VALUE");

    /**
     *  The user controls sort order; additional controls are added
     *  to allow the user to control the order of options in the
     *  selected list.
     *
     **/

    public static final SortMode USER = new SortMode("USER");

    private SortMode(String name)
    {
        super(name);
    }

}