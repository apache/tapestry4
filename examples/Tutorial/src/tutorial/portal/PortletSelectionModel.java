package tutorial.portal;

import com.primix.tapestry.*;
import com.primix.tapestry.form.*;
import java.util.*;


/**
 *  A special property selection model used
 *  to select a Portlet (from it's title).
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public class PortletSelectionModel
	implements IPropertySelectionModel
{
	private List list = new ArrayList();
	
	private static class Entry
	{
		int id;
		String title;
		
		Entry(int id, String title)
		{
			this.id = id;
			this.title = title;
		}
	}
	
	public void add(PortletDef def)
	{
		Entry e = new Entry(def.getId(), def.getTitle());
		list.add(e);
	}
	
	public int getOptionCount()
	{
		return list.size();
	}
	
	public Object getOption(int index)
	{
		Entry e = (Entry)list.get(index);
		
		return new Integer(e.id);
	}
	
	public String getLabel(int index)
	{
		Entry e = (Entry)list.get(index);
		
		return e.title;
	}
	
	public String getValue(int index)
	{
		Entry e = (Entry)list.get(index);
		
		return Integer.toString(e.id);
	}
	
	public Object translateValue(String value)
	{
		return new Integer(value);
	}
	
}
