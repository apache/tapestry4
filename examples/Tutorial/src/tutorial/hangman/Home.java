package tutorial.hangman;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;

/*
 * Tapestry Web Application Framework
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *
 *  The home page for the hangman application, allows a new game to
 *  start after selecting the difficulty.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */ 

public class Home
extends BasePage
implements IActionListener
{
    public static final int EASY = 10;
    public static final int MEDIUM = 5;
    public static final int HARD = 3;

    private int misses;
    private String error;

    public void detachFromApplication()
    {
        misses = 0;
        error = null;
        
        super.detachFromApplication();
    }

    public int getMisses()
    {
        return misses;
    }

    public void setMisses(int value)
    {
        misses = value;

        fireObservedChange("misses", value);
    }

    public String getError()
    {
        return error;
    }

    public IActionListener getFormListener()
    {
        return this;
    }

    public void actionTriggered(IComponent component, IRequestCycle cycle)
    throws RequestCycleException
    {
        if (misses == 0)
        {
            error = "Please select a game difficulty.";
            return;
        }

        Visit visit = (Visit)getVisit();
 
        visit.start(misses);

        cycle.setPage("Guess");
    }

}