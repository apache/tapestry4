package tutorial.pagelinking;

import net.sf.tapestry.ApplicationServlet;

/**
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class PageLinkingServlet extends ApplicationServlet
{
    protected String getApplicationSpecificationPath()
    {
        return "/tutorial/pagelinking/PageLinking.application";
    }

}