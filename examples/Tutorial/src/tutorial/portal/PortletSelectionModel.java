package tutorial.portal;

import java.util.ArrayList;
import java.util.List;

import net.sf.tapestry.form.IPropertySelectionModel;

/**
 *  A special property selection model used
 *  to select a Portlet (from it's title).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class PortletSelectionModel implements IPropertySelectionModel
{
	private List _list = new ArrayList();

	private static class Entry
	{
		private int _id;
		private String _title;

		Entry(int id, String title)
		{
			_id = id;
			_title = title;
		}
        
        public int getId()
        {
            return _id;
        }

        public String getTitle()
        {
            return _title;
        }

	}

	public void add(PortletChannel channel)
	{
		Entry e = new Entry(channel.getId(), channel.getTitle());
        
		_list.add(e);
	}

	public int getOptionCount()
	{
		return _list.size();
	}

	public Object getOption(int index)
	{
		Entry e = (Entry) _list.get(index);

		return new Integer(e.getId());
	}

	public String getLabel(int index)
	{
		Entry e = (Entry) _list.get(index);

		return e.getTitle();
	}

	public String getValue(int index)
	{
		Entry e = (Entry) _list.get(index);

		return Integer.toString(e.getId());
	}

	public Object translateValue(String value)
	{
		return new Integer(value);
	}

}