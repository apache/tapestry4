package tutorial.portal;

import com.primix.tapestry.*;
import com.primix.tapestry.form.*;
import java.util.*;
import java.io.*;

public class Visit
	implements Serializable
{
	private static PortletDef[] defs =
	{
		new PortletDef(1020, "TV Listings", "TV", "content"),
			new PortletDef(23, "Stock Quotes", "Stocks", "content"),
			new PortletDef(373, "Weather", "Weather", "content")
			
	};
	
	private List models;
	
	public void removeModel(PortletModel model)
	{
		if (models != null)
			models.remove(model);
	}
	
	public Collection getModels()
	{
		return models;
	}
	
	public void addModel(int id)
	{
		for (int i = 0; i < defs.length; i++)
		{
			PortletDef def = defs[i];
			
			if (def.getId() == id)
			{
				PortletModel model = def.getModel();
				
				if (models == null)
					models = new ArrayList();
				
				models.add(model);
				
				return;
			}
		}
		
		throw new ApplicationRuntimeException("No portlet definition with id " + id + ".");
	}
	
	/**
	 *  Returns a portlet selection model that will produce a list
	 *  of all portlets <em>not already in use</em>.  It will generate
	 *  an integer property which is the id of they selected
	 *  porlet, suitable for passing to {@link #addModel(int)}.
	 *
	 */
	
	public IPropertySelectionModel getPortletSelectionModel()
	{
		PortletSelectionModel model = new PortletSelectionModel();
		
		for (int i = 0; i < defs.length; i++)
		{
			PortletDef def = defs[i];
			
			if (!inUse(def.getId()))
				model.add(def);
		}
		
		return model;
	}
	
	private boolean inUse(int id)
	{
		if (models == null)
			return false;
		
		int count = models.size();
		for (int i = 0;  i < count; i++)
		{
			PortletModel model = (PortletModel)models.get(i);
			
			if (model.getId() == id)
				return true;
		}
		
		return false;
	}
	
	/**
	 *  Returns true if there are any additional portlets that the user has not
	 *  already added.  If it returns false, then the UI should omit the
	 *  form for adding a new portlet.
	 *
	 */
	
	public boolean getMaySelectPortlet()
	{
		if (models == null)
			return true;
		
		return models.size() < defs.length;
	}
}
