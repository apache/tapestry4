package tests.tapestry;

import com.primix.tapestry.components.*;
import com.ibm.logging.*;
import com.primix.tapestry.event.*;
import com.primix.tapestry.*;

/*
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; wihtout even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  
 *
 * @author Howard Ship
 * @version $Id$
 */


public class HomePage extends BasePage
{
    private String activeSelection = "none";

    private String currentSelection;

    private static final String[] selectionOptions = { "New", "Open", "Closed" };

    public void detachFromApplication()
    {
        super.detachFromApplication();

        // Return page to 'pristine' state.

        activeSelection = "none";
        currentSelection = null;
    }

    public String getActiveSelection()
    {
        return activeSelection;
    }

    public String getCurrentSelection()
    {
        return currentSelection;
    }

    public IActionListener getListener()
    {
        return new IActionListener()
        {
            public void actionTriggered(IComponent component, IRequestCycle cycle)
            {
                setActiveSelection(currentSelection);
            }
        };
    }

    public String[] getSelectionOptions()
    {
        return selectionOptions;
    }

    public void setActiveSelection(String value)
    {
        activeSelection = value;

        fireObservedChange("activeSelection", value);
    }

    public void setCurrentSelection(String value)
    {
        currentSelection = value;
    }
}

