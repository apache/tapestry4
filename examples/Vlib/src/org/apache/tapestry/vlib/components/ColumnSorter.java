package org.apache.tapestry.vlib.components;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.vlib.ejb.SortColumn;

/**
 *  Comopnent for allowing a column to be sorted.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public abstract class ColumnSorter extends BaseComponent
{
    public abstract SortColumn getSortColumn();

    public abstract SortColumn getSelected();
    public abstract void setSelected(SortColumn selected);

    public abstract boolean isDescending();
    public abstract void setDescending(boolean descending);

    public abstract IActionListener getListener();

    public void handleClick(IRequestCycle cycle)
    {
        SortColumn selected = getSelected();
        SortColumn sortColumn = getSortColumn();

        if (selected != sortColumn)
        {
            setSelected(sortColumn);
            setDescending(false);
        }
        else
        {
            boolean current = isDescending();
            setDescending(!current);
        }

        IActionListener listener = getListener();
        if (listener == null)
            throw Tapestry.createRequiredParameterException(this, "listener");

        listener.actionTriggered(this, cycle);
    }

    public IAsset getImage()
    {
        return getAsset(isDescending() ? "up" : "down");

    }

    public IAsset getFocus()
    {
        return getAsset(isDescending() ? "up_h" : "down_h");
    }
}
