package tutorial.simple;

import java.util.Date;

import net.sf.tapestry.html.BasePage;

/**
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class Home extends BasePage
{
	public Date getCurrentDate()
	{
		return new Date();
	}
}