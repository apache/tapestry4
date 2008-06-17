package ${packageName}.pages;

import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.services.ResponseBuilder;
import java.util.Date;

/**
 * Start page of "${artifactId}".
 */
abstract public class Home extends BasePage
{

    abstract public ResponseBuilder getResponseBuilder();

    /**
     * Re-renders (via ajax) the Tapestry node containing the date and time.
     */
    public void refresh()
    {
        getResponseBuilder().updateComponent("time");
    }

    /**
     * A synthesized property returning the current time.
     * 
     * @return the current date and time
     */
    public Date getCurrentTime()
    {
        return new Date();
    }
}
