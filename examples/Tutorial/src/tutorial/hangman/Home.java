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

    private int _misses;
    private String _error;

    public void initialize()
    {
        _misses = 0;
        _error = null;
    }

    public int getMisses()
    {
        return _misses;
    }

    public void setMisses(int misses)
    {
        _misses = misses;

        fireObservedChange("misses", misses);
    }

    public String getError()
    {
        return _error;
    }

    public void formSubmit(IRequestCycle cycle)
    {
        if (_misses == 0)
        {
            _error = "Please select a game difficulty.";
            return;
        }

        Visit visit = (Visit) getVisit();

        visit.start(_misses);

        cycle.setPage("Guess");
    }

}