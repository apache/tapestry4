package tutorial.components;

import net.sf.tapestry.ApplicationServlet;

/**
 *  @version $Id: PageLinkingServlet.java,v 1.1 2002/10/03 15:49:02 scornflake Exp $
 *  @author Howard Lewis Ship
 *
 **/

public class ComponentsServlet extends ApplicationServlet
{
    protected String getApplicationSpecificationPath()
    {
        return "/tutorial/components/Components.application";
    }

}