package net.sf.tapestry.vlib;

import net.sf.tapestry.ApplicationServlet;

/**
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class VirtualLibraryServlet extends ApplicationServlet
{

    protected String getApplicationSpecificationPath()
    {
        return "/net/sf/tapestry/vlib/Vlib.application";
    }

}