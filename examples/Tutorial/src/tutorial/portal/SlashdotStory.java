package tutorial.portal;

import com.primix.tapestry.util.prop.*;

/**
 *  Encapsulates some data about a Slashdot story.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public class SlashdotStory
    implements IPublicBean
{
    public String title;
    public String URL;
    public String author;
    public String date;
}
