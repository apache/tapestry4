package tutorial.events;

import net.sf.tapestry.ApplicationServlet;

/**
 *  @version $Id: PageLinkingServlet.java,v 1.1 2002/10/03 15:49:02 scornflake Exp $
 *  @author Howard Lewis Ship
 *
 **/

public class EventHandlingServlet extends ApplicationServlet
{
    protected String getApplicationSpecificationPath()
    {
        return "/tutorial/events/EventHandling.application";
    }

}