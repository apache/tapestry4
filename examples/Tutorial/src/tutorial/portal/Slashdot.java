package tutorial.portal;

import com.primix.tapestry.*;
import com.primix.tapestry.util.xml.*;
import java.util.*;
import java.net.*;

/**
 *  Parses the Slashdot XML file and provides headlines.
 *
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public class Slashdot
    extends BasePage
{

    /**
     *  The {@link List} of {@link SlashdotStory} items.  This is <em>not</em> cleared
     *  at the end of the request cycle, because it is data that can be shared
     *  between sessions.
     *
     */
    
    private List stories;
    private SlashdotStory story;
    
    private static String RESOURCE_PATH = "http://slashdot.org/slashdot.xml";
    
    public void detach()
    {
        story = null;
        
        super.detach();
    }
    
    public void setStory(SlashdotStory value)
    {
        story = value;
    }
    
    public SlashdotStory getStory()
    {
        return story;
    }
    
    public List getStories()
    {
        if (stories == null)
            readStories();
        
        return stories;
    }
    
    private void readStories()
    {
        URL url = null;
        SlashdotParser parser = new SlashdotParser();
        
        try
        {
            url = new URL(RESOURCE_PATH);
        }
        catch (MalformedURLException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
        
        try
        {
            stories = parser.parseStories(url, RESOURCE_PATH);
        }
        catch (DocumentParseException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
        
    }
}
