/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  @version $Id$
 *  @author Howard Ship
 *
 */ 

package tutorial.adder;

import com.primix.tapestry.*;
import java.util.*;

public class Home extends BasePage
{
	private List items;
	private String newValue;
	private String error;

	public List getItems()
	{
		return items;
	}

	public void setItems(List value)
	{
		items = value;

		fireObservedChange("items", value);
	}

	public void setNewValue(String value)
	{
		newValue = value;
	}
	
	public String getNewValue()
	{
		return newValue;
	}

	public void detach()
	{
		items = null;
		newValue = null;
		error = null;

		super.detach();
	}

	public void addItem(double value)
	{
		if (items == null)
		{
			items = new ArrayList();
			fireObservedChange("items", items);
		}

		items.add(new Double(value));

		fireObservedChange();
	}

	public double getTotal()
	{
		Iterator i;
		Double item;
		double result = 0.0;

		if (items != null)
		{
			i = items.iterator();
			while (i.hasNext())
			{
				item = (Double)i.next();
				result += item.doubleValue();
			}
		}

		return result;
	}

	public IActionListener getFormListener()
	{
		return new IActionListener()
		{
			public void actionTriggered(IComponent component, IRequestCycle cycle)
			{
				try
				{
					double item = Double.parseDouble(newValue);
					addItem(item);
					
					newValue = null;
				}
				catch (NumberFormatException e)
				{
					error = "Please enter a valid number.";
				}
			}
		};

	}

	public String getError()
	{
		return error;
	}
}

