package wap.hello;

import net.sf.tapestry.ApplicationServlet;

/**
 *  @version $Id$
 *  @author David Solis
 *
 **/

public class HelloWorldServlet extends ApplicationServlet
{
    protected String getApplicationSpecificationPath()
    {
        return "/wap/hello/HelloWorld.application";
    }

}