package tutorial.hello;

import net.sf.tapestry.ApplicationServlet;

/**
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class HelloWorldServlet extends ApplicationServlet
{
    protected String getApplicationSpecificationPath()
    {
        return "/tutorial/hello/HelloWorld.application";
    }

}