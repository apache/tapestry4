package org.apache.tapestry.vlib.ejb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *  Used with {@link org.apache.tapestry.vlib.ejb.IBookQuery} to represent
 *  the order in which columns are sorted in the result set.  SortOrdering
 *  is immutable.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class SortOrdering implements Serializable
{
    private SortColumn _column;
    private boolean _descending;

    public SortOrdering(SortColumn column)
    {
        this(column, false);
    }

    public SortOrdering(SortColumn column, boolean descending)
    {
        _column = column;
        _descending = descending;
    }

    public boolean isDescending()
    {
        return _descending;
    }

    public SortColumn getColumn()
    {
        return _column;
    }

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("column", _column);
        builder.append("descending", _descending);

        return builder.toString();
    }

}
