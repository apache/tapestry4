package tutorial.hangman;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.html.BasePage;

/**
 *
 *  The home page for the hangman application, allows a new game to
 *  start after selecting the difficulty.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class Home extends BasePage
{
    public static final int EASY = 10;
    public static final int MEDIUM = 5;
    public static final int HARD = 3;

    private int misses;
    private String error;

    public void detach()
    {
        misses = 0;
        error = null;

        super.detach();
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

    public void formSubmit(IRequestCycle cycle)
    {
        if (misses == 0)
        {
            error = "Please select a game difficulty.";
            return;
        }

        Visit visit = (Visit) getVisit();

        visit.start(misses);

        cycle.setPage("Guess");
    }

}