package tutorial.portal;

import net.sf.tapestry.util.prop.IPublicBean;

/**
 *  Encapsulates some data about a Slashdot story.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class SlashdotStory implements IPublicBean
{
	public String title;
	public String URL;
	public String author;
	public String date;
}