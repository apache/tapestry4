//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.vlib.components;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.vlib.ejb.SortColumn;

/**
 *  Comopnent for allowing a column to be sorted.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
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
}
