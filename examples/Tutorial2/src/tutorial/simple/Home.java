package tutorial.simple;

import java.util.Date;

import net.sf.tapestry.html.BasePage;

/**
 *  @version $Id: Home.java,v 1.8 2002/05/04 12:43:31 hship Exp $
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