package tutorial.hello;

import net.sf.tapestry.html.BasePage;

/**
 *  @version $Id$
 *  @author Neil Clayton
 *
 **/
public class Home extends BasePage
{
	public String getSomeText()
	{
		return "It's a brave new world!";
	}
}