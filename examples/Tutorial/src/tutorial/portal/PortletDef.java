package tutorial.portal;

import com.primix.tapestry.components.*;

/**
 *  Defines a possible portlet; providing a unique id,
 *  title, and the page and component path of the
 *  {@link Block} providing content.
 *
 *
 *  @author Howard Ship
 *  @version $Id$ 
 *
 */

public class PortletDef 
{
	private int id;
	private String title;
	private String pageName;
	private String componentPath;
	
	public PortletDef(int id, String title, String pageName, String componentPath)
	{
		this.id = id;
		this.title = title;
		this.pageName = pageName;
		this.componentPath = componentPath;
	}
	
	public PortletModel getModel()
	{
		return new PortletModel(id, title, pageName, componentPath);
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getTitle()
	{
		return title;
	}
}

