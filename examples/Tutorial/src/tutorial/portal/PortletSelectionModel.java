//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

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

	public void add(PortletChannel def)
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
		Entry e = (Entry) list.get(index);

		return new Integer(e.id);
	}

	public String getLabel(int index)
	{
		Entry e = (Entry) list.get(index);

		return e.title;
	}

	public String getValue(int index)
	{
		Entry e = (Entry) list.get(index);

		return Integer.toString(e.id);
	}

	public Object translateValue(String value)
	{
		return new Integer(value);
	}

}