package tutorial.pagelinking;

import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 * Provides the listeners and a place to store a simple 'result' string
 * that can be shown back to the user.
 */
public class SecondPage extends BasePage {
    public void linkListener(IRequestCycle cycle) throws RequestCycleException {
        result = "The link listener was called - which is a good thing.";
    }

    public void buttonListener(IRequestCycle cycle) throws RequestCycleException {
        result = "The submit listener was called - which is also a good thing.";
    }

    public String getResult() {
        return result;
    }

    /** Clean up */
    public void detach() {
        super.detach();
        result = null;
    }

    private String result;
}
