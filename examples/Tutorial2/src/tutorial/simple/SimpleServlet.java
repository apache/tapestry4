package tutorial.simple;

import net.sf.tapestry.ApplicationServlet;

/**
 *  @version $Id: SimpleServlet.java,v 1.9 2002/05/04 12:43:31 hship Exp $
 *  @author Howard Lewis Ship
 *
 */

public class SimpleServlet extends ApplicationServlet
{
	protected String getApplicationSpecificationPath()
	{
		return "/tutorial/simple/Simple.application";
	}
}